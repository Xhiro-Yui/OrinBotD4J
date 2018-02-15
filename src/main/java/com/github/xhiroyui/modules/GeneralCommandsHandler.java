package com.github.xhiroyui.modules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import com.github.xhiroyui.ModuleLoader;
import com.github.xhiroyui.constant.FunctionConstant;
import com.github.xhiroyui.util.Command;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class GeneralCommandsHandler extends ModuleHandler {

	public GeneralCommandsHandler() {
		createCommands();
	}

	private void createCommands() {
		Command command;

		command = new Command(FunctionConstant.GEN_PING);
		command.setCommandName("Ping");
		command.setCommandDescription("Ping Pong!");
		command.getCommandCallers().add("ping");
		command.setMaximumArgs(0);
		commandList.add(command);

		command = new Command(FunctionConstant.GEN_EMBED_PING);
		command.setCommandName("Ping (Embed Version)");
		command.setCommandDescription("Ping Pong! (Embed Version)");
		command.getCommandCallers().add("ping2");
		command.getCommandCallers().add("pingembed");
		command.setMaximumArgs(0);
		commandList.add(command);

		command = new Command(FunctionConstant.GEN_BOT_AUTHOR);
		command.setCommandName("Bot Author");
		command.setCommandDescription("Displays the original Bot Author.");
		command.getCommandCallers().add("botauthor");
		command.getCommandCallers().add("author");
		command.setMaximumArgs(0);
		commandList.add(command);

		command = new Command(FunctionConstant.GEN_GET_AVAILABLE_COMMANDS);
		command.setCommandName("Get Available Commands");
		command.setCommandDescription("Displays all commands available, sorted by Modules.");
		command.getCommandCallers().add("commands");
		command.setMaximumArgs(0);
		commandList.add(command);

	}

	@EventSubscriber
	public void OnMesageEvent(MessageReceivedEvent event)
			throws RateLimitException, DiscordException, MissingPermissionsException, IOException {
		String[] command = processCommand(event);
		if (command != null) {
			executeCommand(event, command);
		}
	}

	public void executeCommand(MessageReceivedEvent event, String[] command) throws IOException {
		String commandCode = validateCommand(event, command);
		if (commandCode != null) {
			switch (commandCode) {
			case FunctionConstant.GEN_PING:
				sendMessage("Pong", event);
				break;
			case FunctionConstant.GEN_EMBED_PING:
				embedPong(event);
				break;
			case FunctionConstant.GEN_BOT_AUTHOR:
				botAuthor(event);
				break;
			case FunctionConstant.GEN_GET_AVAILABLE_COMMANDS:
				getAvailableCommands(event);
				break;

			}
		}
	}

	public void embedPong(MessageReceivedEvent event) {
		EmbedBuilder embed = new EmbedBuilder();
		embed.withAuthorName("Orin");
		embed.withThumbnail("https://pbs.twimg.com/media/CdbhdctXIAA25Zu.jpg");
		embed.withImage("https://i.pinimg.com/564x/7f/61/3f/7f613f1c4c5a6ec291049d1acb056f04.jpg");
		embed.appendField("Image Credits",
				"[Image taken from Pinterest](https://i.pinimg.com/564x/7f/61/3f/7f613f1c4c5a6ec291049d1acb056f04.jpg)",
				false);
		embed.appendField("Thumbnail Credits",
				"[Thumbnail Credits to @TouhouFanarts](https://twitter.com/TouhouFanarts)", false);
		embed.withDesc("Pong");

		sendEmbed(embed, event);
	}

	public void botAuthor(MessageReceivedEvent event) {
		EmbedBuilder embed = new EmbedBuilder();
		embed.withAuthorName("Rhestia");
		embed.withThumbnail("A");
		embed.withImage("https://i.pinimg.com/564x/7f/61/3f/7f613f1c4c5a6ec291049d1acb056f04.jpg");
		embed.appendField("Image Credits",
				"[Image taken from Pinterest](https://i.pinimg.com/564x/7f/61/3f/7f613f1c4c5a6ec291049d1acb056f04.jpg)",
				false);
		embed.appendField("Thumbnail Credits",
				"[Thumbnail Credits to @TouhouFanarts](https://twitter.com/TouhouFanarts)", false);
		embed.withDesc("Pong");

		sendEmbed(embed, event);
	}

	private void getAvailableCommands(MessageReceivedEvent event) {
		StringBuilder commandString = new StringBuilder();
		commandString.append("__**List of Available Commands**__\n\n");
		for (Map.Entry<String, ArrayList<Command>> entry : ModuleLoader.getModuleLoader().getModuleCommands()
				.entrySet()) {
			commandString.append("__Module : **" + entry.getKey() + "**__\n");
			for (Command each : entry.getValue()) {
				commandString.append(each.getCommandName() + " : ");
				for (int i = 0; i < each.getCommandCallers().size(); i++) {
					if (i == 0)
						commandString.append("`" + each.getCommandCallers().get(i) + "`");
					else
						commandString.append(", `" + each.getCommandCallers().get(i) + "`");
				}
				commandString.append("\n");
			}
			commandString.append("\n\n");
		}
		commandString.append("Type `!<command> help` to get more information about specific commands.");
		
		sendMessage(commandString.toString(), event);
	}
}