package com.github.xhiroyui.modules;

import java.io.IOException;
import java.util.Arrays;

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
	private final String faviconUrl = "https://gbf.wiki/images/favicon.ico";

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
		command.getCommandCallers().add("character");
		command.setMaximumArgs(3);
		commandList.add(command);
		
		command = new Command("GET_GBF_CHAR2");
		command.setCommandName("GBF Character");
		command.setCommandDescription("Displays a GBF character with info");
		command.getCommandCallers().add("char2");
		command.setMaximumArgs(3);
		commandList.add(command);
		
//		command = new Command("SEARCH_GBF_WIKI");
//		command.setCommandName("GBF Character");
//		command.setCommandDescription("Searches GBF wiki based on query");
//		command.getCommandCallers().add("search");
//		command.setMaximumArgs(1);
//		commandList.add(command);

	}

	@EventSubscriber
	public void OnMesageEvent(MessageReceivedEvent event)
			throws RateLimitException, DiscordException, MissingPermissionsException, IOException {
		System.out.println("Message received");
		if (event.getMessage().getContent().startsWith(module.getPrefix())) {
			System.out.println("Starting execute command");
			executeCommand(event);
			System.out.println("Ending execute command");
		}
	}

	public void executeCommand(MessageReceivedEvent event) throws IOException {
		System.out.println("Starting parsing message into substrings");
		String[] command = parseMessage(
				event.getMessage().getContent().substring(1, event.getMessage().getContent().length()));
		System.out.println("Ending parsing message into substrings - Starting validate command");
		String commandCode = validateCommand(event, command);
		System.out.println("Ending validate command");
		if (commandCode != null) {
			switch (commandCode) {
			case "ping":
				sendMessage("pong", event);
				break;
			case "GET_GBF_CHAR":
				createCharEmbed(gbfWikiParser.gbfWikiSearch(Arrays.copyOfRange(command, 1, command.length)) ,event);
				break;
			case "GET_GBF_CHAR2":
				createCharEmbed2(gbfWikiParser.gbfWikiSearch(Arrays.copyOfRange(command, 1, command.length)) ,event);
				break;
			}
		}
	}

	public void createCharEmbed(String webUrl, MessageReceivedEvent event) throws IOException {
		// Do a check to see if search failed or not
		// Do X if search fails (select top result?), do Y if search success
		// Below is do Y
		try {
			System.out.println("Starting embed building");
			EmbedBuilder builder = new EmbedBuilder();
			System.out.println("Starting parse GBF char");
			GBFCharacter character = gbfWikiParser.parseGbfCharacterOld(webUrl);
			System.out.println("Ending parse gbf char - resuming embed building");
			if (!character.getTitle().isEmpty())
				builder.withAuthorName("[" + character.getTitle() +"]");
			else
				builder.withAuthorName("[Skybound]");
			builder.withAuthorIcon(character.getRarityImageUrl());
			builder.withThumbnail(character.getThumbnailUrl());
			builder.appendDesc(character.getDescription());
			builder.withTitle(character.getName());
			builder.withUrl(character.getBaseUri());
			builder.appendField("Element", character.getElement(), true);
			builder.appendField("Race", character.getRace(), true);
			builder.appendField("Style", character.getStyle(), true);
			if( character.getGender().equalsIgnoreCase("m"))
				builder.appendField("Gender", "Male", true);
			else 
				builder.appendField("Gender", "Female", true);
			builder.appendField("Specialty", character.getSpecialty(), true);
			builder.withImage(character.getImageUrl());
			builder.withFooterText("Data obtained from GBF Wiki");
			builder.appendField("Voice Actor", "["+character.getVoiceActor()[0]+"]("+character.getVoiceActor()[1]+")", true);
			builder.appendField("How to Obtain", "["+character.getObtainableFrom()[0]+"]("+character.getObtainableFrom()[1]+")", true);
			if (character.getRecruitmentWeapon()!=null) 
			{ builder.appendField("Recruitment Weapon", "["+character.getRecruitmentWeapon()[0]+"]("+character.getRecruitmentWeapon()[1]+")", true); }
			switch (character.getElement().toLowerCase()) {
			case "fire":
				builder.withColor(255, 0, 35);
				break;
			case "water":
				builder.withColor(0, 115, 255);
				break;
			case "earth":
				builder.withColor(175, 100, 35);
				break;
			case "wind":
				builder.withColor(135, 255, 0);
				break;
			case "light":
				builder.withColor(255, 235, 0);
				break;
			case "dark":
				builder.withColor(135, 0, 255);
				break;
			}
			builder.withFooterIcon(faviconUrl);
			System.out.println("Ending embed building - Starting send embed");
			RequestBuffer.request(() -> event.getChannel().sendMessage(builder.build()));
			System.out.println("Ending send embed");
		}
		catch (IllegalArgumentException e) {
			sendMessage("ERROR : Failed to create embed.", event);
		}
		
	}
	
	public void createCharEmbed2(String webUrl, MessageReceivedEvent event) throws IOException {
		// Do a check to see if search failed or not
		// Do X if search fails (select top result?), do Y if search success
		// Below is do Y
		try {
			EmbedBuilder builder = new EmbedBuilder();
			GBFCharacter character = gbfWikiParser.parseGbfCharacter(webUrl);
			if (!character.getTitle().isEmpty())
				builder.withAuthorName("[" + character.getTitle() +"]");
			else
				builder.withAuthorName("[Skybound]");
			builder.withAuthorIcon(character.getRarityImageUrl());
			builder.withThumbnail(character.getThumbnailUrl());
			builder.appendDesc(character.getDescription());
			builder.withTitle(character.getName());
			builder.withUrl(character.getBaseUri());
			builder.appendField("Element", character.getElement(), true);
			builder.appendField("Race", character.getRace(), true);
			builder.appendField("Style", character.getStyle(), true);
			if( character.getGender().equalsIgnoreCase("m"))
				builder.appendField("Gender", "Male", true);
			else 
				builder.appendField("Gender", "Female", true);
			builder.appendField("Specialty", character.getSpecialty(), true);
			builder.withImage(character.getImageUrl());
			builder.withFooterText("Data obtained from GBF Wiki");
			builder.appendField("Voice Actor", "["+character.getVoiceActor()[0]+"]("+character.getVoiceActor()[1]+")", true);
			builder.appendField("How to Obtain", "["+character.getObtainableFrom()[0]+"]("+character.getObtainableFrom()[1]+")", true);
			if (character.getRecruitmentWeapon()!=null) 
			{ builder.appendField("Recruitment Weapon", "["+character.getRecruitmentWeapon()[0]+"]("+character.getRecruitmentWeapon()[1]+")", true); }
			switch (character.getElement().toLowerCase()) {
			case "fire":
				builder.withColor(255, 0, 35);
				break;
			case "water":
				builder.withColor(0, 115, 255);
				break;
			case "earth":
				builder.withColor(175, 100, 35);
				break;
			case "wind":
				builder.withColor(135, 255, 0);
				break;
			case "light":
				builder.withColor(255, 235, 0);
				break;
			case "dark":
				builder.withColor(135, 0, 255);
				break;
			}
			builder.withFooterIcon(faviconUrl);
			RequestBuffer.request(() -> event.getChannel().sendMessage(builder.build()));
		}
		catch (IllegalArgumentException e) {
			sendMessage("ERROR : Failed to create embed.", event);
		}
		
	}
}