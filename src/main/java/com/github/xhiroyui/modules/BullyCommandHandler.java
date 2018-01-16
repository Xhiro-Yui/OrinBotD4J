package com.github.xhiroyui.modules;

import java.util.ArrayList;

import com.github.xhiroyui.util.UserWhitelist;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.RequestBuffer;

public class BullyCommandHandler extends CommandHandler {

	private BullyCommands module;
	private UserWhitelist whitelist;
	private ArrayList<String> bullyList = new ArrayList<String>();
	private boolean bullyMode = false;

	public BullyCommandHandler(BullyCommands _module, UserWhitelist _whitelist) {
		module = _module;
		whitelist = _whitelist;
	}

	@EventSubscriber
	public void OnMesageEvent(MessageReceivedEvent event)
			throws RateLimitException, DiscordException, MissingPermissionsException {
		if (event.getMessage().getContent().startsWith(module.getPrefix())) {
			if (!adminCheck(event.getAuthor(), event.getGuild())) {
				System.out.println("Admin check failed");
				if (whitelist.validateUser(event.getAuthor().getStringID())) {
					System.out.println("User is Whitelisted");
					parseCommand(event);
				} else {
					System.out.println("Whitelist check failed");
				}
			} else {
				System.out.println("User is Admin");
				parseCommand(event);
			}

		}
		if (bullyMode) {
			sendMessage("BL Modo", event);
			for (String userid : bullyList) {
				if (userid.contentEquals(event.getAuthor().getStringID())) {
					event.getMessage().delete();
				}
			}
			
		}

	}

	public boolean adminCheck(IUser user, IGuild guild) {
		for (Permissions perm : user.getPermissionsForGuild(guild)) {
			if (perm.toString().equalsIgnoreCase("administrator")) {
				return true;
			}
		}
		return false;
	}

	public void parseCommand(MessageReceivedEvent event) {
		System.out.println("Parsing Command");
		String[] command = parseMessage(
				event.getMessage().getContent().substring(1, event.getMessage().getContent().length()));
		switch (command[0]) {
		case "bl":
		case "bully":
			toggleBully(event);
			break;
		case "addbullylist":
		case "addbl":
			commandAddToBullylist(command, event);
			break;
		case "printbl":
			sendMessage("Printing BL", event);
			for (String userid : bullyList) {
				sendMessage(userid, event);
			}
		}
	}

	private void toggleBully(MessageReceivedEvent event) {
		sendMessage("Bl toggled", event);
		if (bullyMode) {
			bullyMode = false;
			sendMessage("Bl = True", event);
		} else {
			bullyMode = true;
			sendMessage("Bl = False", event);
		}

	}

	private void commandAddToBullylist(String[] command, MessageReceivedEvent event) {
		String message = "";
		for (IUser user : event.getMessage().getMentions()) {
			bullyList.add(user.getStringID());
			message += user.mention() + " ";
		}
		sendMessage("User added to bl " + message, event);
	}

	public void sendMessage(String message, MessageReceivedEvent event)
			throws RateLimitException, DiscordException, MissingPermissionsException {
		new MessageBuilder(AdminCommands.client).appendContent(message).withChannel(event.getMessage().getChannel())
				.build();
	}

}