package com.github.xhiroyui.modules;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.StringUtils;
//import org.apache.http.client.methods.RequestBuilder;

import com.github.xhiroyui.DiscordClient;
import com.github.xhiroyui.constant.BotConstant;
import com.github.xhiroyui.util.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RequestBuffer;

public class ModuleHandler {

	protected ArrayList<Command> commandList = new ArrayList<Command>();

	public String[] parseMessage(String message) {
		String[] command = StringUtils.split(message, ' ');
		command[0] = command[0].toLowerCase();
		return command;
	}

	protected void sendMessage(String message, MessageReceivedEvent event)
			throws DiscordException, MissingPermissionsException {
		RequestBuffer.request(() -> {
			new MessageBuilder(DiscordClient.getClient()).appendContent(message)
			.withChannel(event.getMessage().getChannel()).build();
			});	
	}
	
	protected void sendLogMessage(String message, Long channelID)
			throws DiscordException, MissingPermissionsException {
		RequestBuffer.request(() -> {
			new MessageBuilder(DiscordClient.getClient()).appendContent(message)
			.withChannel(DiscordClient.getClient().getChannelByID(channelID)).build();
			});	
	}

	protected void sendLogMessage(String message, IChannel channel)
			throws DiscordException, MissingPermissionsException {
		RequestBuffer.request(() -> {
			new MessageBuilder(DiscordClient.getClient()).appendContent(message)
			.withChannel(channel).build();
			});	
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
		StringBuilder param = new StringBuilder();
		int paramCount = 0;
		for (String[] params : command.getParams()) {
			paramCount++;
			if (params.length > 1) {
				for (int i = 0; i < params.length; i++) {
					if (i == 0)
						param.append(paramCount + " : " + params[i]);
					else
						param.append(" / ").append(params[i]);
				}
				param.append("\n");
			}
			else
				param.append(paramCount + " : " + params[0] + "\n");
		}
		if (param.length() == 0)
			param.append("None");
		StringBuilder caller = new StringBuilder();
		for (int i = 0; i < command.getCommandCallers().size(); i++) {
			if (i == 0)
				caller.append(command.getCommandCallers().get(i));
			else
				caller.append(" | ").append(command.getCommandCallers().get(i));
		}

		EmbedBuilder builder = new EmbedBuilder();
		builder.withAuthorName(command.getCommandCode());
		builder.withTitle("*" + command.getCommandName() + "*");
		builder.withDesc(command.getCommandDescription());
		builder.appendField("Params", param.toString(), true);
		builder.appendField("Maximum Arguments", Integer.toString(command.getMaximumArgs()), true);
		builder.appendField("Example Use", command.getExample().toString(), false);
		builder.appendField("Alternative command calls", caller.toString(), false);
		builder.withColor(ThreadLocalRandom.current().nextInt(0, 255 + 1),
				ThreadLocalRandom.current().nextInt(0, 255 + 1), ThreadLocalRandom.current().nextInt(0, 255 + 1));

		builder.withFooterText("OrinBot by Rhestia :3");
		sendEmbed(builder, event);
	}

	protected void invalidArgsLength(Command commandObj, MessageReceivedEvent event) {
		String error = "Too many arguments given for the command `" + commandObj.getCommandName() + "`. \n"
				+ "Maximum arguments allowed is `" + commandObj.getMaximumArgs() + "`.";
		sendMessage(error, event);
	}

	protected String validateCommand(MessageReceivedEvent event, String[] command) {
		Command commandObj = getCommandObj(command[0]);
		if (commandObj != null) {
			if (command.length > 1 && command[1].equalsIgnoreCase("help")) {
				helpEmbed(commandObj, event);
			} else {
				if (command.length - 1 > commandObj.getMaximumArgs()) {
					invalidArgsLength(commandObj, event);
				} else {
					return commandObj.getCommandCode();
				}
			}
		}
		return null;
	}

	protected void sendEmbed(EmbedBuilder embed, MessageReceivedEvent event) {
		RequestBuffer.request(() -> event.getChannel().sendMessage(embed.build()));
	}

	protected void throwError(String command, Exception e, MessageReceivedEvent event) {
		sendMessage("Command **" + command + "** faced an error `" + e.getClass().getName()
				+ "`.\nPlease contact the bot author to solve this error.", event);
		e.printStackTrace();
	}
	
	protected boolean checkCommands(String command) {
		for (Command commands : commandList) {
			for (String commandCaller : commands.getCommandCallers()) {
				if (commandCaller.equalsIgnoreCase(command))
					return true;
			}
		}
		return false;
	}
	
	protected String[] processCommand(MessageReceivedEvent event) {
		if (event.getMessage().getContent().startsWith(BotConstant.PREFIX)) {
			String[] command = parseMessage(
					event.getMessage().getContent().substring(1, event.getMessage().getContent().length()));
			if (checkCommands(command[0])) {
				return command;
			}
		}
		return null;
	}
	
	public ArrayList<Command> getModuleCommands() {
		return commandList;
	}
	
	protected boolean adminCheck(IUser user, IGuild guild) {
		return user.getPermissionsForGuild(guild).contains(Permissions.ADMINISTRATOR);
	}
	
	protected boolean moderatorCheck(IUser user, IGuild guild) {
		return user.getPermissionsForGuild(guild).contains(Permissions.MANAGE_SERVER);
	}
}
