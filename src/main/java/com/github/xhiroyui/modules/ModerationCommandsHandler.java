package com.github.xhiroyui.modules;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import com.github.xhiroyui.TaskLoader;
import com.github.xhiroyui.UserWhitelist;
import com.github.xhiroyui.constant.BotConstant;
import com.github.xhiroyui.constant.FunctionConstant;
import com.github.xhiroyui.util.Command;
import com.github.xhiroyui.util.DBConnection;
import com.github.xhiroyui.util.MiscUtils;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.RequestBuffer;

public class ModerationCommandsHandler extends ModuleHandler {
	public ModerationCommandsHandler() {
		createCommands();
	}

	private void createCommands() {
		Command command;

		command = new Command(FunctionConstant.MOD_FLAG_CHANNEL);
		command.setCommandName("Flag Channel");
		command.setCommandDescription("Flags a channel. Type of flags can be found by using the List Flags function.");
		command.setCommandCallers("flag");
		command.setParams(new String[] { "Flag (not case-sensitive)" });
		command.setParams(new String[] { "Additional arguments based on flag. Check with List Flags" });
		command.setMaximumArgs(2);
		command.setExample(BotConstant.FUNC_FLAG_LIMITED + " 1");
		commandList.add(command);
		
		command = new Command(FunctionConstant.MOD_UNFLAG_CHANNEL);
		command.setCommandName("Unflag Channel");
		command.setCommandDescription("Removes all flags from a channel.");
		command.setCommandCallers("unflag");
		command.setMaximumArgs(0);
		commandList.add(command);
		
		command = new Command(FunctionConstant.MOD_CHECK_CHANNEL_FLAGS);
		command.setCommandName("Check Flags");
		command.setCommandDescription("Displays all flags in the current channel.");
		command.setCommandCallers("checkflag");
		command.setCommandCallers("checkflags");
		command.setMaximumArgs(0);
		commandList.add(command);
		
		command = new Command(FunctionConstant.MOD_LIST_FLAGS);
		command.setCommandName("List Flags");
		command.setCommandDescription("Lists all available flags offered by this bot and its functionalities");
		command.setCommandCallers("listflag");
		command.setCommandCallers("listflags");
		command.setMaximumArgs(0);
		commandList.add(command);
		
		command = new Command(FunctionConstant.MOD_MESSAGE_PURGE);
		command.setCommandName("Purge Message (Minutes)");
		command.setCommandDescription("Purge Messages less than {minutes} old.\n*Note : this command does not purge messages older then 2 weeks.");
		command.setCommandCallers("purge");
		command.setParams(new String[] { "Denomination (Days / Hours / Minutes)" });
		command.setParams(new String[] { "Amount by denomination" });
		command.setMaximumArgs(2);
		command.setExample("HOURS 2");
		commandList.add(command);
		
	}

	@EventSubscriber
	public void OnMesageEvent(MessageReceivedEvent event)
			throws RateLimitException, DiscordException, MissingPermissionsException, IOException {
		String[] command = processCommand(event);
		if (command != null) {
			if (!moderatorCheck(event.getAuthor(), event.getGuild())) {
				if (UserWhitelist.getWhitelist().validateUser(event.getAuthor().getStringID())) {
					executeCommand(event, command);
				} else {
					sendMessage(event.getAuthor().getDisplayName(event.getGuild())
							+ " is not a moderator or whitelisted and is unable to use this command!", event);
				}
			} else {
				executeCommand(event, command);
			}
		}
	}

	public void executeCommand(MessageReceivedEvent event, String[] command) {
		String commandCode = validateCommand(event, command);
		if (commandCode != null) {
			switch (commandCode) {
			case FunctionConstant.MOD_FLAG_CHANNEL:
				try {
					flag_channel(command, event);
				} catch (Exception e) {
					throwError(FunctionConstant.MOD_FLAG_CHANNEL, e, event);
				}
				break;

			case FunctionConstant.MOD_UNFLAG_CHANNEL:
				try {
					removeAllFlags(event.getChannel().getLongID());
					printFlagsForCurrentChannel(event);
				} catch (Exception e) {
					throwError(FunctionConstant.MOD_UNFLAG_CHANNEL, e, event);
				}
				break;
			case FunctionConstant.MOD_CHECK_CHANNEL_FLAGS:
				try {
					printFlagsForCurrentChannel(event);
				} catch (Exception e) {
					throwError(FunctionConstant.MOD_CHECK_CHANNEL_FLAGS, e, event);
				}
				break;
			case FunctionConstant.MOD_LIST_FLAGS:
				try {
					printAvailableFlags(event);
				} catch (Exception e) {
					throwError(FunctionConstant.MOD_LIST_FLAGS, e, event);
				}
				break;
			case FunctionConstant.MOD_MESSAGE_PURGE:
				try {
					purgeMessages(command, event);
				} catch (Exception e) {
					throwError(FunctionConstant.MOD_MESSAGE_PURGE, e, event);
				}
				break;
			}
		}
	}

	// Command functions are placed below here
		
	private void printAvailableFlags(MessageReceivedEvent event) {
		StringBuilder listFlagsSB = new StringBuilder();
		listFlagsSB.append("__**List of all available Flags**__\n\n");
		
		listFlagsSB.append("**" + BotConstant.FUNC_FLAG_LIMITED + "** : ");
		listFlagsSB.append("Limits the amount of posts per user in the channel. \n");
		listFlagsSB.append("Accepts a single variable {postAmount} in integer. \n");
		listFlagsSB.append("Example : `" + BotConstant.PREFIX + "flag " + BotConstant.FUNC_FLAG_LIMITED + " 1` \n");
		listFlagsSB.append("*NOTE : Decreasing the {postAmount} on an already flagged channel will delete all stored records\n"
				+ "As such, it is advisable to purge all messages from the channel before doing so.* \n\n");
		
		listFlagsSB.append("**" + BotConstant.FUNC_FLAG_DURATION+ "** : ");
		listFlagsSB.append("Deletes posts older than the specified amount of time in hours. \n");
		listFlagsSB.append("Accepts a single variable {hours} in integer. \n");
		listFlagsSB.append("Example : `" + BotConstant.PREFIX + "flag " + BotConstant.FUNC_FLAG_DURATION + " 24` \n\n");
		
		listFlagsSB.append("**" + BotConstant.FUNC_FLAG_LOGCHANNEL+ "** : ");
		listFlagsSB.append("Logs bot activity from this channel due to flags in the specified channel. \n");
		listFlagsSB.append("Accepts a single {channelMention}. \n");
		listFlagsSB.append("Example : `" + BotConstant.PREFIX + "flag " + BotConstant.FUNC_FLAG_LOGCHANNEL + " #selectedChannel` \n\n");
		
		listFlagsSB.append("All individual flags can be disabled by passing `0` as their arguments or by calling "
				+ "!unflag to remove all flags from the channel");
		sendMessage(listFlagsSB.toString(), event);
	}

	private void flag_channel(String[] command, MessageReceivedEvent event) {
		if (command[1].equalsIgnoreCase("NONE"))
			removeAllFlags(event.getChannel().getLongID());
		else if (command[1].equalsIgnoreCase("LIMITED"))
			if (command.length == 3)
				setLimitedFlag(BotConstant.FUNC_FLAG_LIMITED, event, command[2]);
			else
				sendMessage("Missing parameter. The parameters for `" + BotConstant.FUNC_FLAG_LIMITED
						+ "` flag is :\n**Post Limit** : {amount}", event);
		else if (command[1].equalsIgnoreCase("LIMITED"))
			if (command.length == 3) 
				setChannelLogFlag(BotConstant.FUNC_FLAG_LOGCHANNEL, event, command[2]);
			else
				sendMessage("Missing parameter. The parameters for `" + BotConstant.FUNC_FLAG_LOGCHANNEL
						+ "` flag is :\n**#selectedChannel** *channel mention*", event);
		else if (command[1].equalsIgnoreCase("DURATION"))
			if (command.length == 3)
				setDurationFlag(BotConstant.FUNC_FLAG_DURATION, event, command[2]);	
			else
				sendMessage("Missing parameter. The parameters for `" + BotConstant.FUNC_FLAG_DURATION
						+ "` flag is :\n**Duration** *in hours* : {hours}", event);
		else if (command[1].equalsIgnoreCase("LOGCHANNEL"))
			if (command.length == 3) 
				setChannelLogFlag(BotConstant.FUNC_FLAG_LOGCHANNEL, event, command[2]);
			else
				sendMessage("Missing parameter. The parameters for `" + BotConstant.FUNC_FLAG_LOGCHANNEL
						+ "` flag is :\n**#selectedChannel** *channel mention*", event);
		else
			sendMessage("Invalid flag. Please check list of available flags using the List Flags command", event);	
		printFlagsForCurrentChannel(event);
	}

	private void removeAllFlags(long channelID) {
		DBConnection.getDBConnection().deleteQuery("DELETE FROM " + BotConstant.DB_CHANNEL_FLAGS_TABLE + " WHERE channel_id = '" + channelID + "'");
		TaskLoader.getTaskLoader().getMonitor(channelID).shutdown();
	}

	private void setLimitedFlag(String flag, MessageReceivedEvent event, String amount) {
		if (MiscUtils.isInteger(amount) && Integer.parseInt(amount) >= 0) {
			ArrayList<String> monitorFlags = TaskLoader.getTaskLoader().getMonitorFlags(event.getChannel().getLongID());
			if ((monitorFlags == null || !(monitorFlags.contains(BotConstant.FUNC_FLAG_LIMITED))) && Integer.parseInt(amount) > 0) {
				DBConnection.getDBConnection()
						.insertQuery("INSERT INTO " + BotConstant.DB_CHANNEL_FLAGS_TABLE + " (`channel_id`, `flags`, `post_amount`) VALUES ('"
								+ event.getChannel().getLongID() + "', '" + flag + "', '" + amount + "') ");
			} else if (Integer.parseInt(amount) == 0) {
				DBConnection.getDBConnection().deleteQuery("DELETE FROM " + BotConstant.DB_CHANNEL_FLAGS_TABLE + " WHERE channel_id = '" + event.getChannel().getLongID() + "' AND flags = '" + BotConstant.FUNC_FLAG_LIMITED + "'");
			} else {
				DBConnection.getDBConnection()
						.updateQuery("UPDATE " + BotConstant.DB_CHANNEL_FLAGS_TABLE + " SET post_amount = '" + amount + "' WHERE channel_id = '"
								+ event.getChannel().getLongID() + "' AND flags = '" + BotConstant.FUNC_FLAG_LIMITED
								+ "'");				
			}
			createUpdateMonitor(event.getChannel().getLongID());
		} else {
			sendMessage("Parameter error. The parameter {postAmount} only accepts integers.", event);
		}
	}

	private void setDurationFlag(String flag, MessageReceivedEvent event, String duration) {
		try {
		if (MiscUtils.isInteger(duration) && Integer.parseInt(duration) >= 0) {
			ArrayList<String> monitorFlags = TaskLoader.getTaskLoader().getMonitorFlags(event.getChannel().getLongID());
			if ((monitorFlags == null || !(monitorFlags.contains(BotConstant.FUNC_FLAG_DURATION))) && Integer.parseInt(duration) > 0) {
				DBConnection.getDBConnection()
						.insertQuery("INSERT INTO " + BotConstant.DB_CHANNEL_FLAGS_TABLE + " (`channel_id`, `flags`, `duration`) VALUES ('"
								+ event.getChannel().getLongID() + "', '" + flag + "', '" + duration + "') ");
			} else if (Integer.parseInt(duration) == 0) {
				DBConnection.getDBConnection().deleteQuery("DELETE FROM " + BotConstant.DB_CHANNEL_FLAGS_TABLE + " WHERE channel_id = '" + event.getChannel().getLongID() + "' AND flags = '" + BotConstant.FUNC_FLAG_DURATION + "'");
			} else {			
				DBConnection.getDBConnection()
						.updateQuery("UPDATE " + BotConstant.DB_CHANNEL_FLAGS_TABLE + " SET duration = '" + duration + "' WHERE channel_id = '"
								+ event.getChannel().getLongID() + "' AND flags = '" + BotConstant.FUNC_FLAG_DURATION
								+ "'");
			}
			createUpdateMonitor(event.getChannel().getLongID());
		} else {
			sendMessage("Parameter error. The parameter {duration} only accepts integers.", event);
		} 
		} catch (Exception e) {e.printStackTrace();}
	}
	
	private void setChannelLogFlag(String flag, MessageReceivedEvent event, String command) {
		if (!event.getMessage().getChannelMentions().isEmpty()) {
			ArrayList<String> monitorFlags = TaskLoader.getTaskLoader().getMonitorFlags(event.getChannel().getLongID());
			if (monitorFlags == null || !(monitorFlags.contains(BotConstant.FUNC_FLAG_LOGCHANNEL))) {
				DBConnection.getDBConnection()
				.insertQuery("INSERT INTO " + BotConstant.DB_CHANNEL_FLAGS_TABLE + " (`channel_id`, `flags`, `log_channel_id`) VALUES ('"
						+ event.getChannel().getLongID() + "', '" + flag + "', '" + event.getMessage().getChannelMentions().get(0).getLongID() + "') ");
			} else {
				DBConnection.getDBConnection()
				.updateQuery("UPDATE " + BotConstant.DB_CHANNEL_FLAGS_TABLE + " SET log_channel_id = '" + event.getMessage().getChannelMentions().get(0).getLongID() + "' WHERE channel_id = '"
						+ event.getChannel().getLongID() + "' AND flags = '" + BotConstant.FUNC_FLAG_LOGCHANNEL
						+ "'");
			}
			createUpdateMonitor(event.getChannel().getLongID());
		} else if (command.equalsIgnoreCase("0")) {
			DBConnection.getDBConnection().deleteQuery("DELETE FROM " + BotConstant.DB_CHANNEL_FLAGS_TABLE + " WHERE channel_id = '" + event.getChannel().getLongID() + "' AND flags = '" + BotConstant.FUNC_FLAG_LOGCHANNEL + "'");
			createUpdateMonitor(event.getChannel().getLongID());
		} else {
			sendMessage("Parameter error. Please mention a channel as a parameter for this flag." , event);
		}
	}
	
	private void printFlagsForCurrentChannel(MessageReceivedEvent event) {
		ArrayList<String> monitorFlags = TaskLoader.getTaskLoader().getMonitorSettings(event.getChannel().getLongID());
		if (monitorFlags == null || monitorFlags.size() == 0) {
			sendMessage("Channel has no flags." , event);
		}
		else {
			StringBuilder flagsSB = new StringBuilder();
			flagsSB.append("The channel has the following flags :\n");
			for (int i = 0; i < monitorFlags.size(); i++) {
				if ( i == 0 || i%2 == 0 ) {
					flagsSB.append("**" + monitorFlags.get(i) + "** : ");
				}
				else {
					flagsSB.append(monitorFlags.get(i) + "\n");
				}
			}
			sendMessage(flagsSB.toString(), event);
		}
		
	}
	
	private void purgeMessages(String[] command, MessageReceivedEvent event) {
		int totalMessagesDeleted = 0;
		if (MiscUtils.isInteger(command[2])) {
			if (command[1].equalsIgnoreCase("min") || command[1].equalsIgnoreCase("minute") || command[1].equalsIgnoreCase("minutes"))
					totalMessagesDeleted = RequestBuffer.request(() -> (Integer) event.getChannel().getMessageHistoryTo(Instant.now().minus(Integer.parseInt(command[2]), ChronoUnit.MINUTES)).bulkDelete().size()).get();					
			else if (command[1].equalsIgnoreCase("hour") || command[1].equalsIgnoreCase("hours"))
					totalMessagesDeleted = RequestBuffer.request(() -> (Integer) event.getChannel().getMessageHistoryTo(Instant.now().minus(Integer.parseInt(command[2]), ChronoUnit.HOURS)).bulkDelete().size()).get();				 
			else if (command[1].equalsIgnoreCase("day") || command[1].equalsIgnoreCase("days")) 
					totalMessagesDeleted = RequestBuffer.request(() -> (Integer) event.getChannel().getMessageHistoryTo(Instant.now().minus(Integer.parseInt(command[2]), ChronoUnit.DAYS)).bulkDelete().size()).get();
		}
		sendMessage("Deleted a total of " + totalMessagesDeleted + " message(s) ~!", event);
	}

	private void createUpdateMonitor(long channelID) {
		if (TaskLoader.getTaskLoader().checkMonitors(channelID))
			TaskLoader.getTaskLoader().refreshMonitorSettings(channelID);
		else
			TaskLoader.getTaskLoader().addMonitor(channelID);
	}
}