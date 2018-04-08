package com.github.xhiroyui.tasks;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.mutable.MutableBoolean;

import com.github.xhiroyui.DiscordClient;
import com.github.xhiroyui.TaskLoader;
import com.github.xhiroyui.constant.BotConstant;
import com.github.xhiroyui.util.DBConnection;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageDeleteEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MessageHistory;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.RequestBuffer;
import sx.blah.discord.util.RequestBuffer.RequestFuture;

public class ChannelMonitor implements ITask {

	private IChannel channel;
	private IChannel logChannel = null;
	private Long channelID;
	private MutableBoolean fifoFlag = new MutableBoolean();
	private MutableBoolean lifoFlag = new MutableBoolean();
	private int postLimit;
	private MutableBoolean durationFlag = new MutableBoolean();
	private int duration;
	private MutableBoolean allowDelete = new MutableBoolean();
	private ScheduledExecutorService scheduledExecutorService = null;
	volatile RequestFuture<Void> rf;

	public ChannelMonitor(long channelID) {
		this.channelID = channelID;
		channel = DiscordClient.getClient().getChannelByID(channelID);
		refreshSettings();
		System.out.println("Initializing Channel Monitor for " + channelID);

	}

	@EventSubscriber
	public void OnMesageEvent(MessageReceivedEvent event)
			throws RateLimitException, DiscordException, MissingPermissionsException, IOException {
		if (this.fifoFlag.booleanValue()) {
			if (channelID.compareTo(event.getChannel().getLongID()) == 0) {
				if (getChannelPostCount(event.getAuthor().getLongID(), event.getChannel().getLongID()) >= postLimit) {
					long messageIDtoDelete = getOldestPostID(event.getAuthor().getLongID(),
							event.getChannel().getLongID());
					// String timeString =
					// event.getChannel().fetchMessage(messageIDtoDelete).getTimestamp().toString();
					String timeString = event.getChannel().fetchMessage(messageIDtoDelete).getTimestamp()
							.truncatedTo(ChronoUnit.MICROS).toString();
					try {
						RequestBuffer.request(() -> event.getChannel().fetchMessage(messageIDtoDelete).delete());
					} catch (Exception e) {
						e.printStackTrace();
					}
					updateRowEntry(messageIDtoDelete, event.getMessage().getLongID());
					logEvent(BotConstant.FUNC_FLAG_FIFO, event.getAuthor().mention(), timeString);
				} else {
					logToDB(event.getAuthor().getLongID(), event.getChannel().getLongID(),
							event.getMessage().getLongID(),
							event.getMessage().getTimestamp().toEpochMilli());
				}
			}
		} else if (this.lifoFlag.booleanValue()) {
			if (channelID.compareTo(event.getChannel().getLongID()) == 0) {
				if (getChannelPostCount(event.getAuthor().getLongID(), event.getChannel().getLongID()) == postLimit) {
					RequestBuffer.request(() -> event.getMessage().delete());
					logEvent(BotConstant.FUNC_FLAG_LIFO, event.getAuthor().mention(),
							event.getMessage().getTimestamp().toString());
				} else {
					logToDB(event.getAuthor().getLongID(), event.getChannel().getLongID(),
							event.getMessage().getLongID(),
							event.getMessage().getTimestamp().toEpochMilli());

				}
			}
		}
	}

	@EventSubscriber
	public void OnMessageDeleteEvent(MessageDeleteEvent event) {
		if (this.allowDelete.isTrue() && channelID.compareTo(event.getChannel().getLongID()) == 0)
			deleteRowEntry(event.getMessageID());
	}

	public long getChannelPostCount(long userID, long channelID) {
		return Long.parseLong(DBConnection.getDBConnection().selectQuerySingleResult(
				"SELECT count(post_id) AS count FROM " + BotConstant.DB_CHANNEL_MONITOR_TABLE + " WHERE user_id = ? AND channel_id = ?",
				userID, channelID));
	}

	public long getOldestPostID(long userID, long channelID) {
		return Long.parseLong(DBConnection.getDBConnection().selectQuerySingleResult(
				"SELECT post_id FROM " + BotConstant.DB_CHANNEL_MONITOR_TABLE + " WHERE user_id = ? AND channel_id = ? ORDER BY 'datetime_of_post' ASC LIMIT 1",
				userID, channelID));
	}

	public void logToDB(long userID, long channelID, long messageID, long time) {
		DBConnection.getDBConnection().insertQuery(
				"INSERT INTO " + BotConstant.DB_CHANNEL_MONITOR_TABLE + " (channel_id, user_id, post_id, datetime_of_post) VALUES (?,?,?,?)",
				channelID, userID, messageID, time);
	}

	public void updateRowEntry(long oldMessageID, long messageID) {
		DBConnection.getDBConnection().updateQuery(
				"UPDATE " + BotConstant.DB_CHANNEL_MONITOR_TABLE + " SET post_id = ? WHERE post_id = ?", messageID,
				oldMessageID);
	}

	public void deleteRowEntry(long messageID) {
		DBConnection.getDBConnection()
				.deleteQuery("DELETE FROM " + BotConstant.DB_CHANNEL_MONITOR_TABLE + " WHERE post_id = ?", messageID);
	}

	public void deleteChannelLogs() {
		DBConnection.getDBConnection().deleteQuery(
				"DELETE FROM " + BotConstant.DB_CHANNEL_MONITOR_TABLE + " WHERE channel_id = ?", channel.getLongID());
	}

	public ArrayList<String> getMonitorFlags() {
		ArrayList<String> currentFlags = new ArrayList<String>();
		if (lifoFlag.isTrue())
			currentFlags.add(BotConstant.FUNC_FLAG_LIFO);
		if (fifoFlag.isTrue())
			currentFlags.add(BotConstant.FUNC_FLAG_FIFO);
		if (durationFlag.isTrue())
			currentFlags.add(BotConstant.FUNC_FLAG_DURATION);
		if (logChannel != null)
			currentFlags.add(BotConstant.FUNC_FLAG_LOGCHANNEL);
		return currentFlags;
	}

	private void createTimeMonitor(int hours) {
		scheduledExecutorService = Executors.newScheduledThreadPool(0);
		scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					Instant timeMinusHoursSet = Instant.now().minus(hours, ChronoUnit.HOURS);
					MessageHistory mh = DiscordClient.getClient().getChannelByID(channelID).getMessageHistoryFrom(timeMinusHoursSet);
					if (mh.size() > 0) {
						for (IMessage msg : mh) {
							if (!msg.isPinned())
								rf = RequestBuffer.request(() -> msg.delete());
						}
						logEvent(BotConstant.FUNC_FLAG_DURATION, timeMinusHoursSet.toString());
						DBConnection.getDBConnection().deleteQuery(
								"DELETE FROM " + BotConstant.DB_CHANNEL_MONITOR_TABLE + " WHERE channel_id = ? AND datetime_of_post < ?",
								DiscordClient.getClient().getChannelByID(channelID), Instant.now().toEpochMilli());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 1, 60 * 60, TimeUnit.SECONDS); // Runs every hour

	}

	public void stopTimeMonitor() {
		if (scheduledExecutorService != null) {
			System.out.println("Shutting down Time monitor for channel " + channelID);
			scheduledExecutorService.shutdown();
			scheduledExecutorService = null;
		}
	}

	@Override
	public void refreshSettings() {
		ArrayList<String[]> parameters = DBConnection.getDBConnection().selectQueryMultipleColumnMultipleResults(
				"SELECT * FROM " + BotConstant.DB_CHANNEL_FLAGS_TABLE + " WHERE channel_id = ?", this.channelID);
		if (parameters == null) {
			shutdown();
		} else {
			this.logChannel = null;
			this.lifoFlag.setFalse();
			this.fifoFlag.setFalse();
			this.durationFlag.setFalse();

			// Due to parameters being column sensitive, if there are any
			// changes to column structure in the db, the below code breaks
			for (String[] flags : parameters) {
				if (flags[1].equalsIgnoreCase(BotConstant.FUNC_FLAG_LIFO)) {
					if (postLimit > Integer.parseInt(flags[2])) {
						deleteChannelLogs();
					}
					postLimit = Integer.parseInt(flags[2]);
					if (postLimit > 0)
						this.lifoFlag.setTrue();
				}
				if (flags[1].equalsIgnoreCase(BotConstant.FUNC_FLAG_FIFO)) {
					if (postLimit > Integer.parseInt(flags[2])) {
						deleteChannelLogs();
					}
					postLimit = Integer.parseInt(flags[2]);
					if (postLimit > 0)
						this.fifoFlag.setTrue();
				}
				if (flags[1].equalsIgnoreCase(BotConstant.FUNC_FLAG_DURATION)) {
					duration = Integer.parseInt(flags[3]);
					if (duration > 0) {
						createTimeMonitor(duration);
						this.durationFlag.setTrue();
					}
				}
				if (flags[1].equalsIgnoreCase(BotConstant.FUNC_FLAG_LOGCHANNEL)) {
					logChannel = DiscordClient.getClient().getChannelByID(Long.parseLong(flags[4]));
				}
				if (flags[1].equalsIgnoreCase(BotConstant.FUNC_FLAG_ALLOW_DELETE)) {
					if (flags[5].equalsIgnoreCase("0"))
						this.allowDelete.setFalse();
					if (flags[5].equalsIgnoreCase("1"))
						this.allowDelete.setTrue();
				}
			}
		}
	}

	@Override
	public ArrayList<String> getSettings() {
		ArrayList<String> settings = new ArrayList<String>();
		if (this.fifoFlag.isTrue()) {
			settings.add(BotConstant.FUNC_FLAG_FIFO);
			settings.add(Integer.toString(this.postLimit) + " post(s)");
		}
		if (this.lifoFlag.isTrue()) {
			settings.add(BotConstant.FUNC_FLAG_LIFO);
			settings.add(Integer.toString(this.postLimit) + " post(s)");
		}
		if (this.durationFlag.isTrue()) {
			settings.add(BotConstant.FUNC_FLAG_DURATION);
			settings.add(Integer.toString(this.duration) + " hour(s)");
		}
		if (!(logChannel == null)) {
			settings.add(BotConstant.FUNC_FLAG_LOGCHANNEL);
			settings.add("<#" + String.valueOf(logChannel.getLongID()) + ">");
		}
		if (this.allowDelete.isTrue()) {
			settings.add(BotConstant.FUNC_FLAG_ALLOW_DELETE);
			settings.add("True");
		}
		return settings;
	}

	@Override
	public long getChannelID() {
		return channelID;
	}

	@Override
	public void shutdown() {
		stopTimeMonitor();
		TaskLoader.getTaskLoader().removeMonitor(channelID);
	}

	private void logEvent(String eventFlag, Object... args) {
		if (!(logChannel == null)) {
			StringBuilder logSB = new StringBuilder();
			logSB.append("**LOG** -> ");
			if (eventFlag.equalsIgnoreCase(BotConstant.FUNC_FLAG_LIFO)) {
				logSB.append("Flag `" + BotConstant.FUNC_FLAG_LIFO + "` : Message by " + args[0] + " at " + args[1]
						+ " is deleted");
			}
			if (eventFlag.equalsIgnoreCase(BotConstant.FUNC_FLAG_FIFO)) {
				logSB.append("Flag `" + BotConstant.FUNC_FLAG_FIFO + "` : Message by " + args[0] + " at " + args[1]
						+ " is deleted");
			}
			if (eventFlag.equalsIgnoreCase(BotConstant.FUNC_FLAG_DURATION)) {
				logSB.append("Flag `" + BotConstant.FUNC_FLAG_DURATION + "` : Messages older than " + args[0]
						+ " is deleted");
			}
			RequestBuffer.request(() -> new MessageBuilder(DiscordClient.getClient()).appendContent(logSB.toString())
					.withChannel(logChannel).build());
		}
	}
}
