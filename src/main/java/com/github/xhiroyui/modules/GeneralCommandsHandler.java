package com.github.xhiroyui.modules;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Map;

import com.github.xhiroyui.DiscordClient;
import com.github.xhiroyui.ModuleLoader;
import com.github.xhiroyui.OrinBot;
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

		command = new Command(FunctionConstant.GEN_BOT_AUTHOR);
		command.setCommandName("Bot Author");
		command.setCommandDescription("Displays the original Bot Author.");
		command.getCommandCallers().add("botauthor");
		command.getCommandCallers().add("author");
		command.setMaximumArgs(0);
		commandList.add(command);

		command = new Command(FunctionConstant.GEN_UPTIME);
		command.setCommandName("Bot Uptime");
		command.setCommandDescription("Displays the uptime of the bot.");
		command.getCommandCallers().add("uptime");
		command.setMaximumArgs(0);
		commandList.add(command);

		command = new Command(FunctionConstant.GEN_GET_AVAILABLE_COMMANDS);
		command.setCommandName("Get Available Commands");
		command.setCommandDescription("Displays all commands available, sorted by Modules.");
		command.getCommandCallers().add("commands");
		command.setMaximumArgs(0);
		commandList.add(command);

		command = new Command(FunctionConstant.GEN_GET_GUILD_STATS);
		command.setCommandName("Get Guild Statistics");
		command.setCommandDescription("Displays miscellaneous statistics of the guild.");
		command.getCommandCallers().add("guildstats");
		command.setMaximumArgs(0);
		commandList.add(command);
		
		command = new Command(FunctionConstant.GEN_GET_USER_STATS);
		command.setCommandName("Get User Statistics");
		command.setCommandDescription("Displays miscellaneous statistics of the user.");
		command.getCommandCallers().add("userstats");
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
				try {
					sendMessage("Pong <a:ablobaww:422603863820861452>", event);
				} catch (Exception e) {
					throwError(FunctionConstant.GEN_PING, e, event);
				}
				break;
			case FunctionConstant.GEN_BOT_AUTHOR:
				try {
					botAuthor(event);
				} catch (Exception e) {
					throwError(FunctionConstant.GEN_BOT_AUTHOR, e, event);
				}
				break;
			case FunctionConstant.GEN_UPTIME:
				try {
					sendMessage(
							BotConstant.DATE_TIME_FORMATTER.format(
									Instant.now().minusMillis(OrinBot.rb.getUptime()).truncatedTo(ChronoUnit.SECONDS)),
							event);
				} catch (Exception e) {
					throwError(FunctionConstant.GEN_UPTIME, e, event);
				}
				break;
			case FunctionConstant.GEN_GET_AVAILABLE_COMMANDS:
				try {
					getAvailableCommands(event);
				} catch (Exception e) {
					throwError(FunctionConstant.GEN_GET_AVAILABLE_COMMANDS, e, event);
				}
				break;
			case FunctionConstant.GEN_GET_GUILD_STATS:
				try {
					getGuildStatistics(event);
				} catch (Exception e) {
					throwError(FunctionConstant.GEN_GET_GUILD_STATS, e, event);
				}
				break;
			case FunctionConstant.GEN_GET_USER_STATS:
				try {
					getUserStatistics(event);
				} catch (Exception e) {
					throwError(FunctionConstant.GEN_GET_USER_STATS, e, event);
				}
				break;

			}
		}
	}



	public void botAuthor(MessageReceivedEvent event) {
		EmbedBuilder embed = new EmbedBuilder();
		embed.withAuthorName("Rhestia");
		// embed.withAuthorUrl("");
		embed.withTitle("Not-so-proud owner/author of OrinBot");
		embed.withUrl("https://github.com/Xhiro-Yui/OrinBotD4J");
		embed.withAuthorIcon(
				"https://gravatar.com/avatar/215c7e6f5f54a8b888536f32f09c0a7e?s=96&d=https://www.herokucdn.com/images/ninja-avatar-96x96.png");
		embed.withThumbnail(DiscordClient.getClient().getApplicationOwner().getAvatarURL());
		embed.appendField("OrinBot on GitHub", "[GitHub Link](https://github.com/Xhiro-Yui/OrinBotD4J)", false);
		embed.withColor(125, 125, 125);
		// embed.withImage("");
		embed.withDesc(
				"I spend more time on things that I shouldn't be doing than things I should be like working on this bot <a:uwu:422628406807494676>");
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
	
	private void getGuildStatistics(MessageReceivedEvent event) {
		EmbedBuilder embed = new EmbedBuilder();
		embed.withTitle(event.getGuild().getName());
		embed.withThumbnail(event.getGuild().getIconURL());
		try {
			embed.withUrl("https://discord.gg/" + event.getGuild().getExtendedInvites().get(0).getCode());
		} catch (MissingPermissionsException e) {}
		embed.appendField("Guild Owner", event.getGuild().getOwner().getDisplayName(event.getGuild()), true);
		embed.appendField("Creation Date", BotConstant.DATE_TIME_FORMATTER.format(event.getGuild().getCreationDate()), true);
		embed.appendField("Total Guild Members", Integer.toString(event.getGuild().getTotalMemberCount()), true);
		try {
			embed.appendField("Total Banned Users", Integer.toString(event.getGuild().getBannedUsers().size()), true);
		} catch (MissingPermissionsException e) {}
		sendEmbed(embed, event);
	}
	
	private void getUserStatistics(MessageReceivedEvent event) {
		EmbedBuilder embed = new EmbedBuilder();
		embed.withAuthorName(event.getAuthor().getName());
		embed.withAuthorIcon(event.getAuthor().getAvatarURL());
		embed.withThumbnail(event.getAuthor().getAvatarURL());
		embed.appendField("Display Name", event.getAuthor().getDisplayName(event.getGuild()), true);
		embed.appendField("Account Creation Date", BotConstant.DATE_TIME_FORMATTER.format(event.getAuthor().getCreationDate()), true);
		embed.withColor(event.getAuthor().getColorForGuild(event.getGuild()));
		sendEmbed(embed, event);
	}

}