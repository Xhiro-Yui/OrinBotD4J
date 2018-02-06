package com.github.xhiroyui.modules;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import com.github.xhiroyui.DiscordClient;
import com.github.xhiroyui.util.Command;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.RequestBuffer;

public class ModuleHandler {
	
	protected ArrayList<Command> commandList = new ArrayList<Command>();
	
	public String[] parseMessage(String message) {
		String[] command = StringUtils.split(message.toLowerCase(), ' ');
		return command;
	}
	
	
	
	protected void sendMessage(String message, MessageReceivedEvent event)
			throws RateLimitException, DiscordException, MissingPermissionsException {
		new MessageBuilder(DiscordClient.getClient()).appendContent(message).withChannel(event.getMessage().getChannel())
				.build();
	}
	
	protected Command getCommandObj(String command) {
		for (int i = 0; i < commandList.size(); i++) {
			for (int j = 0; j < commandList.get(i).getCommandCallers().size(); j++) {
				if (command.equalsIgnoreCase(commandList.get(i).getCommandCallers().get(j))) {
					System.out.println(commandList.get(i).getCommandCode());
					return commandList.get(i);
				}
			}
		}
		return null;
	}

	protected void helpEmbed(Command command, MessageReceivedEvent event) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.withAuthorName(command.getCommandName());
		builder.withDesc(command.getCommandDescription());
		builder.appendField("Maximum Arguments", Integer.toString(command.getMaximumArgs()), true);
//		builder.appendField("fieldTitleInline2", "fieldContentInline2", true);
		builder.appendField("Alternative command calls", "Ill put the loop later", false);
		builder.withColor(255, 0, 0);

		builder.withFooterIcon("http://i.imgur.com/Ch0wy1e.png");
		builder.withFooterText("by Rhestia :3");
		builder.withFooterIcon("http://i.imgur.com/TELh8OT.png");
		RequestBuffer.request(() -> event.getChannel().sendMessage(builder.build()));
	}
	

	protected void invalidArgsLength(Command commandObj, MessageReceivedEvent event) {
		String error = "Too many arguments given for the command `"+ commandObj.getCommandName() + "`.";
		sendMessage(error,event);
	}
	
	protected String validateCommand(MessageReceivedEvent event, String[] command) {
		Command commandObj = getCommandObj(command[0]);
		if (commandObj != null) {
			if (command.length > 1 && command[1].equalsIgnoreCase("help")) {
				sendMessage(commandObj.getCommandDescription(), event);
				helpEmbed(commandObj, event);
			} else {
				if (command.length - 1 > commandObj.getMaximumArgs()) {
					invalidArgsLength(commandObj, event);
				}
				else {
					return commandObj.getCommandCode();	
				}
			}
		}
		return null;
	}
	
	protected void sendEmbed(EmbedBuilder embed, MessageReceivedEvent event) {
		RequestBuffer.request(() -> event.getChannel().sendMessage(embed.build()));
	}
}
