package com.github.xhiroyui.modules;

import java.io.IOException;

import com.github.xhiroyui.UserWhitelist;
import com.github.xhiroyui.constant.FunctionConstant;
import com.github.xhiroyui.util.Command;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class AdminCommandsHandler extends ModuleHandler {
	public AdminCommandsHandler() {
		createCommands();
	}

	private void createCommands() {
		Command command;

		command = new Command(FunctionConstant.ADMIN_PING);
		command.setCommandName("Admin Ping");
		command.setCommandDescription("Ping (only usable by admins and whitelisted users).");
		command.setCommandCallers("aping");
		command.setMaximumArgs(0);
		commandList.add(command);
	}

	@EventSubscriber
	public void OnMesageEvent(MessageReceivedEvent event)
			throws RateLimitException, DiscordException, MissingPermissionsException, IOException {
		String[] command = processCommand(event);
		if (command != null) {
			if (!adminCheck(event.getAuthor(), event.getGuild())) {
				if (UserWhitelist.getWhitelist().validateUser(event.getAuthor().getStringID())) {
					executeCommand(event, command);
				} else {
					sendMessage(event.getAuthor().getDisplayName(event.getGuild())
							+ " is not an admin or whitelisted and is unable to use this command!", event);
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
			case FunctionConstant.ADMIN_PING:
				try {
					adminPing(event);					
				} catch (Exception e) {
					throwError(FunctionConstant.ADMIN_PING, e, event);
				}
				break;
			
			}

		}
	}
	
	// Command functions are placed below here
	
	private void adminPing(MessageReceivedEvent event) {
		sendMessage("PONG", event);
	}
	
}