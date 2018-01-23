package com.github.xhiroyui.modules;

import java.io.IOException;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.github.xhiroyui.bean.GBFCharacter;
import com.github.xhiroyui.util.Command;
import com.github.xhiroyui.util.WebPageParser;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.RequestBuffer;

public class GBFCommandHandler extends ModuleHandler {

	private GBFCommands module;
	private WebPageParser gbfWikiParser = new WebPageParser();

	public GBFCommandHandler(GBFCommands _module) {
		module = _module;
		createCommands();
	}

	private void createCommands() {
		Command command;

		command = new Command("GET_GBF_CHAR");
		command.setCommandName("GBF Character");
		command.setCommandDescription("Displays a GBF character with info");
		command.getCommandCallers().add("char");
		command.getCommandCallers().add("caracter");
		command.setMaximumArgs(3);
		commandList.add(command);
		
		command = new Command("SEARCH_GBF_WIKI");
		command.setCommandName("GBF Character");
		command.setCommandDescription("Searches GBF wiki based on query");
		command.getCommandCallers().add("search");
		command.setMaximumArgs(1);
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
			case "ping":
				sendMessage("pong", event);
				break;
			case "GET_GBF_CHAR":
				parseGBFWiki(gbfWikiParser.gbfWikiSearch(Arrays.copyOfRange(command, 1, command.length)) ,event);
				break;
			case "SEARCH_GBF_WIKI":
				sendMessage(gbfWikiParser.gbfWikiSearch(Arrays.copyOfRange(command, 1, command.length)), event);
				break;
			}
		}
	}

	public void parseGBFWiki(String url, MessageReceivedEvent event) throws IOException {
		// Do a check to see if search failed or not
		// Do X if search fails (select top result?), do Y if search success
		// Below is do Y
		EmbedBuilder builder = new EmbedBuilder();
		GBFCharacter charData = gbfWikiParser.parseGbfCharacter(url);
//		builder.withAuthorName("Placeholder");
//		builder.withAuthorIcon("https://gbf.wiki/images/thumb/0/0b/Npc_m_3040078000_01.jpg/100px-Npc_m_3040078000_01.jpg");
		builder.withTitle(charData.getName());
		builder.withUrl(charData.getBaseUri());
		builder.appendField("Element", charData.getElement(), true);
		builder.appendField("Race", charData.getRace(), true);
		builder.appendField("Style", charData.getStyle(), true);
		builder.appendField("Gender", charData.getGender(), true);
		builder.appendField("Specialty", charData.getSpecialty().get(0), false); //temp
		builder.withImage(charData.getImageUrl());
		builder.withThumbnail(charData.getThumbnailUrl());
		builder.withFooterText("Data obtained from GBF Wiki");
		
		RequestBuffer.request(() -> event.getChannel().sendMessage(builder.build()));
	}
}