package com.github.xhiroyui.modules;

import java.io.IOException;

import com.github.xhiroyui.TaskLoader;
import com.github.xhiroyui.UserWhitelist;
import com.github.xhiroyui.constant.BotConstant;
import com.github.xhiroyui.constant.FunctionConstant;
import com.github.xhiroyui.util.Command;
import com.github.xhiroyui.util.DBConnection;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

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
		command.setParams(new String[] { "Flag" });
		command.setParams(new String[] { "Amount limited to" });
		command.setParams(new String[] { "Post duration (in hours), 1 day = 24 hours, 1 month = 720 hours" });
		command.setMaximumArgs(3);
		command.setExample(BotConstant.FUNC_FLAG_LIMITED + " 1 720");
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
					flag_channel(command[1], command[2], command[3], event);
				} catch (Exception e) {
					throwError(FunctionConstant.MOD_FLAG_CHANNEL, e, event);
				}
				break;

			}

		}
	}

	// Command functions are placed below here

	private void flag_channel(String flag, String amount, String duration, MessageReceivedEvent event) {
		String channelFlag = null;
		if (flag.equalsIgnoreCase("NONE"))
			channelFlag = BotConstant.FUNC_FLAG_NONE;
		if (flag.equalsIgnoreCase("limited"))
			channelFlag = BotConstant.FUNC_FLAG_LIMITED;
		
		try {
			DBConnection.getDBConnection().insertQuery("INSERT INTO channel_flags (`channel_id`, `flags`, `post_amount`, `duration`) VALUES ('"
				+ event.getChannel().getLongID() + "', '" + channelFlag + "', '" + amount + "', '" + duration
				+ "') "); 
			TaskLoader.getTaskLoader().addMonitor(event.getChannel().getLongID());
		} catch (Exception e) {
			// Some error
		}
	}
}