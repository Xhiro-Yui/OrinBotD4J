package com.github.xhiroyui.modules;

import com.github.xhiroyui.UserWhitelist;
import com.github.xhiroyui.constant.BotConstant;
import com.github.xhiroyui.constant.FunctionConstant;
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

		command = new Command(FunctionConstant.ADMIN_PING);
		command.setCommandName("Admin Ping");
		command.setCommandDescription("Ping (only usable by admins and whitelisted users).");
		command.setCommandCallers("aping");
		command.setMaximumArgs(0);
		commandList.add(command);

		command = new Command(FunctionConstant.ADMIN_ADD_TO_WL);
		command.setCommandName("Add to Whitelist");
		command.setCommandDescription("Adds a user to whitelist");
		command.setCommandCallers("wl");
		command.setCommandCallers("whitelist");
		command.setParams(new String[] { "User ID", "@Mention" });
		command.setMaximumArgs(1);
		command.setExample("@Rhestia");
		commandList.add(command);

		command = new Command(FunctionConstant.ADMIN_REMOVE_FROM_WL);
		command.setCommandName("Remove from Whitelist");
		command.setCommandDescription("Removes a user from the whitelist");
		command.setCommandCallers("removewl");
		command.setCommandCallers("removewhitelist");
		command.setParams(new String[] { "User ID", "@Mention" });
		command.setMaximumArgs(1);
		command.setExample("@Rhestia");
		commandList.add(command);

	}

	@EventSubscriber
	public void OnMesageEvent(MessageReceivedEvent event)
			throws RateLimitException, DiscordException, MissingPermissionsException {
		if (event.getMessage().getContent().startsWith(BotConstant.PREFIX)) {
			if (!adminCheck(event.getAuthor(), event.getGuild())) {
				// System.out.println("Admin check failed");
				if (UserWhitelist.getWhitelist().validateUser(event.getAuthor().getStringID())) {
					// System.out.println("User is Whitelisted");
					executeCommand(event);
				} else {
					// System.out.println("Whitelist check failed");
					sendMessage(event.getAuthor().getDisplayName(event.getGuild())
							+ " is not an admin or whitelisted and is unable to use this command!", event);
				}
			} else {
				// System.out.println("User is Admin");
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
			case FunctionConstant.ADMIN_PING:
				try {
					adminPing(event);					
				} catch (Exception e) {
					throwError(FunctionConstant.ADMIN_PING, e, event);
				}
				break;
			case FunctionConstant.ADMIN_ADD_TO_WL:
				try {
					addToWhitelist(command[1], event);
				} catch (Exception e) {
					throwError(FunctionConstant.ADMIN_ADD_TO_WL, e, event);
				}
				break;
			case FunctionConstant.ADMIN_REMOVE_FROM_WL:
				if (UserWhitelist.getWhitelist().validateUser(command[1])) {
					if (UserWhitelist.getWhitelist().validateUser(command[1]))
					sendMessage("User " + event.getGuild().getUserByID(Long.parseLong(command[1]))
									.getNicknameForGuild(event.getGuild())
									+ " added to whitelist.\\nReminder : Whitelist is persistant across servers.",
							event);
					else
						sendMessage("Error updating whitelist. Please contact bot author to rectify this issue", event);
				}
				else
					sendMessage("User not found in whitelist. No actions were taken.", event);
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
	
	// Command functions are placed below here
	
	private void adminPing(MessageReceivedEvent event) {
		sendMessage("PONG", event);
	}
	
	private void addToWhitelist(String userID, MessageReceivedEvent event) {
		if (!UserWhitelist.getWhitelist().validateUser(userID)) {
			if (UserWhitelist.getWhitelist().addUserToWhitelist(userID))
			sendMessage("User " + event.getGuild().getUserByID(Long.parseLong(userID))
							.getNicknameForGuild(event.getGuild())
							+ " added to whitelist.\\nReminder : Whitelist is persistant across servers.",
					event);
			else
				sendMessage("Error updating whitelist. Please contact bot author to rectify this issue", event);
		}
		else
			sendMessage("User already in whitelist. No actions were taken.", event);
	}
}