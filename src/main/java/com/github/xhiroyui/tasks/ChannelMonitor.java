package com.github.xhiroyui.tasks;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import com.github.xhiroyui.util.DBConnection;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class ChannelMonitor implements ITask{

	private Long channelID;
	private String flag;
	private int postLimit;
//	private int duration;

	public ChannelMonitor(long channelID) {
		this.channelID = channelID;
		refreshSettings();
		System.out.println("Initializing Channel Monitor for " + channelID);
	}

	@EventSubscriber
	public void OnMesageEvent(MessageReceivedEvent event)
			throws RateLimitException, DiscordException, MissingPermissionsException, IOException {
		if (flag.equalsIgnoreCase("limited")) {
			if (channelID.compareTo(event.getChannel().getLongID()) == 0) {
				if (DBConnection.getDBConnection().channelPostCount(event.getAuthor().getLongID(),
						event.getChannel().getLongID()) >= postLimit) {
					long messageIDtoDelete = DBConnection.getDBConnection()
							.getOldestPost(event.getAuthor().getLongID(), event.getChannel().getLongID());
					event.getChannel().getMessageByID(messageIDtoDelete).delete();
					updateRowEntry(messageIDtoDelete, event.getMessage().getLongID());
					
				} else {
					logToDB(event.getAuthor().getLongID(), event.getChannel().getLongID(), event.getMessage().getLongID());	
				}
			}
		}
	}

	public void logToDB(long userID, long channelID, long messageID) {
		DBConnection.getDBConnection().insertQuery("INSERT INTO channel_monitor (channel_id, user_id, post_id, datetime_of_post) values ('" + channelID + "','" + userID + "','" + messageID + "','" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "')");
	}
	
	public void updateRowEntry(long oldMessageID, long messageID) {
		DBConnection.getDBConnection().updateQuery("UPDATE channel_monitor SET post_id='" + messageID + "' WHERE post_id='" + oldMessageID + "'");
	}
	
	public void refreshSettings() {
		ArrayList<String> parameters = DBConnection.getDBConnection()
				.refreshChannelMonitorSettings("SELECT * FROM channel_flags WHERE channel_id = '" + this.channelID + "'");
		flag = parameters.get(0);
		postLimit = Integer.parseInt(parameters.get(1));
//		duration = Integer.parseInt(parameters.get(2));

	}
	
	@Override
	public long getChannelID() {
		return channelID;
	}
}
