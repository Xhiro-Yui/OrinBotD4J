package com.github.xhiroyui.modules;

import com.github.xhiroyui.UserWhitelist;
import com.github.xhiroyui.constant.BotConstant;
import com.github.xhiroyui.util.Command;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class AdminCommandsHandler extends ModuleHandler {
	public AdminCommandsHandler() {
		createCommands();
	}

	private void createCommands() {
		Command command;
		
		command = new Command("ADD_TO_WHITELIST");
		command.setCommandName("wl");
		command.setCommandDescription("Gives the name of the author of this Module");
		command.getCommandCallers().add("wl");
		command.setMaximumArgs(1);
		commandList.add(command);

		command = new Command("ADMIN_PING");
		command.setCommandName("aping");
		command.setCommandDescription("Gives the name of the author of this Module");
		command.getCommandCallers().add("aping");
		command.setMaximumArgs(0);
		commandList.add(command);

		
	}

	@EventSubscriber
	public void OnMesageEvent(MessageReceivedEvent event)
			throws RateLimitException, DiscordException, MissingPermissionsException {
		if (event.getMessage().getContent().startsWith(BotConstant.PREFIX)) {
			if (!adminCheck(event.getAuthor(), event.getGuild())) {
				System.out.println("Admin check failed");
				if (UserWhitelist.getWhitelist().validateUser(event.getAuthor().getStringID())) {
					System.out.println("User is Whitelisted");
					executeCommand(event);
				} else {
					System.out.println("Whitelist check failed");
				}
			} else {
				System.out.println("User is Admin");
				executeCommand(event);
			}
		}
	}

	public void executeCommand(MessageReceivedEvent event) {
		String[] command = parseMessage(
				event.getMessage().getContent().substring(1, event.getMessage().getContent().length()));
		
		String commandCode = validateCommand(event, command);
		if (commandCode != null) {
			switch (commandCode) {
			case "ADMIN_PING":
				sendMessage("PONG", event);
				break;
			case "ADD_TO_WHITELIST":
				UserWhitelist.getWhitelist().addUserToWhitelist(command[1]);
				sendMessage("User with ID `"+command[1]+"' added to whitelist", event);
				break;
			}
			
		}
	}
	
	private boolean adminCheck(IUser user, IGuild guild) {
		for (Permissions perm : user.getPermissionsForGuild(guild)) {
			if (perm.toString().equalsIgnoreCase("administrator")) {
				return true;
			}
		}
		return false;
	}  
}