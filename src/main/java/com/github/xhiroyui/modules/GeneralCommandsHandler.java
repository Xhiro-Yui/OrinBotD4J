package com.github.xhiroyui.modules;

import java.io.IOException;
import com.github.xhiroyui.util.Command;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class GeneralCommandsHandler extends ModuleHandler {

	private GeneralCommands module;

	public GeneralCommandsHandler(GeneralCommands _module) {
		module = _module;
		createCommands();
	}

	private void createCommands() {
		Command command;

		command = new Command("PING");
		command.setCommandName("Ping");
		command.setCommandDescription("Ping Pong!");
		command.getCommandCallers().add("ping");
		command.setMaximumArgs(0);
		commandList.add(command);

		command = new Command("PRETTY_PING");
		command.setCommandName("Ping (Embed Version)");
		command.setCommandDescription("Ping Pong! (Embed Version)");
		command.getCommandCallers().add("ping2");
		command.getCommandCallers().add("pingembed");
		command.setMaximumArgs(0);
		commandList.add(command);

	}

	@EventSubscriber
	public void OnMesageEvent(MessageReceivedEvent event)
			throws RateLimitException, DiscordException, MissingPermissionsException, IOException {
		if (event.getMessage().getContent().startsWith(module.getPrefix())) {
			executeCommand(event);
		}
	}

	public void executeCommand(MessageReceivedEvent event) throws IOException {
		String[] command = parseMessage(
				event.getMessage().getContent().substring(1, event.getMessage().getContent().length()));
		String commandCode = validateCommand(event, command);
		if (commandCode != null) {
			switch (commandCode) {
			case "PING":
				sendMessage("Pong", event);
				break;
			case "PRETTY_PING":
				prettyPong(event);
				break;

			}
		}
	}

	public void prettyPong(MessageReceivedEvent event) {
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
}