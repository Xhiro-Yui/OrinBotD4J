package com.github.xhiroyui.modules;

import org.apache.commons.lang3.StringUtils;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class ModuleHandler {
	public String[] parseMessage(String message) {
		String[] command = StringUtils.split(message.toLowerCase(), ' ');
		return command;
	}
	
	public boolean adminCheck(IUser user, IGuild guild) {
		for (Permissions perm : user.getPermissionsForGuild(guild)) {
			if (perm.toString().equalsIgnoreCase("administrator")) {
				return true;
			}
		}
		return false;
	}  
	
	public void sendMessage(String message, MessageReceivedEvent event)
			throws RateLimitException, DiscordException, MissingPermissionsException {
		new MessageBuilder(AdminCommands.client).appendContent(message).withChannel(event.getMessage().getChannel())
				.build();
	}

}
