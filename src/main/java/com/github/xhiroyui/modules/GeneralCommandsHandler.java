package com.github.xhiroyui.modules;

import java.io.IOException;

import com.github.xhiroyui.constant.BotConstant;
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

	}

	@EventSubscriber
	public void OnMesageEvent(MessageReceivedEvent event)
			throws RateLimitException, DiscordException, MissingPermissionsException, IOException {
		if (event.getMessage().getContent().startsWith(BotConstant.PREFIX)) {
			executeCommand(event);
		}
	}

	public void executeCommand(MessageReceivedEvent event) throws IOException {
		String[] command = parseMessage(
				event.getMessage().getContent().substring(1, event.getMessage().getContent().length()));
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
}