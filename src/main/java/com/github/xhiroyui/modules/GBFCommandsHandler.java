package com.github.xhiroyui.modules;

import java.io.IOException;
import java.util.Arrays;

import com.github.xhiroyui.bean.GBFCharacter;
import com.github.xhiroyui.constant.BotConstant;
import com.github.xhiroyui.constant.FunctionConstant;
import com.github.xhiroyui.util.Command;
import com.github.xhiroyui.util.GBFWikiParser;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class GBFCommandsHandler extends ModuleHandler {

	private GBFWikiParser gbfWikiParser = new GBFWikiParser();
//	private final String faviconUrl = "https://gbf.wiki/images/favicon.ico";

	public GBFCommandsHandler() {
		createCommands();
	}

	private void createCommands() {
		Command command;

		command = new Command(FunctionConstant.GBF_GET_CHARACTER);
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
			case FunctionConstant.GBF_GET_CHARACTER:
				createCharEmbed(gbfWikiParser.gbfWikiSearch(Arrays.copyOfRange(command, 1, command.length)) ,event);
				break;

			}
		}
	}


	
	public void createCharEmbed(String webUrl, MessageReceivedEvent event) throws IOException {
		// Do a check to see if search failed or not
		// Do X if search fails (select top result?), do Y if search success
		// Below is do Y
		try {
			EmbedBuilder embed = new EmbedBuilder();
			GBFCharacter character = gbfWikiParser.parseGbfCharacter(webUrl);
			if (!character.getTitle().isEmpty())
				embed.withAuthorName("[" + character.getTitle() +"]");
			else
				embed.withAuthorName("[Skybound]");
			embed.withAuthorIcon(character.getRarityImageUrl());
			embed.withThumbnail(character.getThumbnailUrl());
			embed.appendDesc(character.getDescription());
			embed.withTitle(character.getName());
			embed.withUrl(character.getBaseUri());
			embed.appendField("Element", character.getElement(), true);
			embed.appendField("Race", character.getRace(), true);
			embed.appendField("Style", character.getStyle(), true);
			if( character.getGender().equalsIgnoreCase("m"))
				embed.appendField("Gender", "Male", true);
			else 
				embed.appendField("Gender", "Female", true);
			embed.appendField("Specialty", character.getSpecialty(), true);
			embed.withImage(character.getImageUrl());
			embed.withFooterText("Data obtained from GBF Wiki");
			embed.appendField("Voice Actor", "["+character.getVoiceActor()[0]+"]("+character.getVoiceActor()[1]+")", true);
			embed.appendField("How to Obtain", "["+character.getObtainableFrom()[0]+"]("+character.getObtainableFrom()[1]+")", true);
			if (character.getRecruitmentWeapon()!=null) 
			{ embed.appendField("Recruitment Weapon", "["+character.getRecruitmentWeapon()[0]+"]("+character.getRecruitmentWeapon()[1]+")", true); }
			if (character.getBonusAtk()!=null)
				embed.appendField("Atk (MIN | MAX | FLB [Fate])", character.getMinAtk() + " | " + character.getMaxAtk() + " | " + character.getFlbAtk() + " | (+" + character.getBonusAtk() + ")", true);
			else
				embed.appendField("Atk (MIN | MAX | FLB)", character.getMinAtk() + " | " + character.getMaxAtk() + " | " + character.getFlbAtk() + " | (+" + character.getBonusAtk() + ")", true);
			embed.appendField("HP (MIN | MAX | FLB [Fate])", character.getMinHp() + " | " + character.getMaxHp() + " | " + character.getFlbHp() + " | (+" + character.getBonusHp() + ")", true);
			switch (character.getElement().toLowerCase()) {
			case "fire":
				embed.withColor(255, 0, 35);
				break;
			case "water":
				embed.withColor(0, 115, 255);
				break;
			case "earth":
				embed.withColor(175, 100, 35);
				break;
			case "wind":
				embed.withColor(135, 255, 0);
				break;
			case "light":
				embed.withColor(255, 235, 0);
				break;
			case "dark":
				embed.withColor(135, 0, 255);
				break;
			}
//			embed.withFooterIcon(this.getClass().getResource("GBFWikiIcon.jpg"));
			sendEmbed(embed, event);
		}
		catch (IllegalArgumentException e) {
			sendMessage("ERROR : Failed to create embed.", event);
		}
		
	}
}