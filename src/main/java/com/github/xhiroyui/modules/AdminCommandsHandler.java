package com.github.xhiroyui.modules;

import com.github.xhiroyui.util.Command;
import com.github.xhiroyui.util.ModuleList;
import com.github.xhiroyui.util.UserWhitelist;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class AdminCommandsHandler extends ModuleHandler {

	private UserWhitelist whitelist;
	private AdminCommands module;
	private ModuleList moduleList;

	public AdminCommandsHandler(AdminCommands _module, UserWhitelist _whitelist,
			ModuleList _moduleList) {
		whitelist = _whitelist;
		module = _module;
		moduleList = _moduleList;
		createCommands();
	}

	private void createCommands() {
		Command command;
		
		command = new Command("GET_AUTHOR");
		command.setCommandName("author");
		command.setCommandDescription("Gives the name of the author of this Module");
		command.getCommandCallers().add("moduleauthor");
		command.getCommandCallers().add("author");
		command.setMaximumArgs(0);
		commandList.add(command);

		
	}

	@EventSubscriber
	public void OnMesageEvent(MessageReceivedEvent event)
			throws RateLimitException, DiscordException, MissingPermissionsException {
		if (event.getMessage().getContent().startsWith(module.getPrefix())) {
			if (!adminCheck(event.getAuthor(), event.getGuild())) {
				System.out.println("Admin check failed");
				if (whitelist.validateUser(event.getAuthor().getStringID())) {
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
//			case "addwhitelist":
//			case "addwl":
//				commandAddToWhitelist(Arrays.copyOfRange(command, 1, command.length), event);
//				break;
			case "GET_AUTHOR":
				sendMessage(module.getAuthor(), event);
				break;
			}
		}
	}

//	private void commandAddToWhitelist(String[] command, MessageReceivedEvent event) {
//		String message = "";
//		for (IUser user : event.getMessage().getMentions()) {
//			whitelist.addUserToWhitelist(user.getStringID());
//			message += user.mention() + " ";
//		}
//
//		sendMessage("User added to whitelist " + message, event);
//
//	}


}