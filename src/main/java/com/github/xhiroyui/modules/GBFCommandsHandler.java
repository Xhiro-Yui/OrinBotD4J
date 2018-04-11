package com.github.xhiroyui.modules;

import java.io.IOException;
import java.util.Arrays;

import com.github.xhiroyui.DiscordClient;
import com.github.xhiroyui.bean.GBFCharacter;
import com.github.xhiroyui.bean.GBFWeapon;
import com.github.xhiroyui.constant.FunctionConstant;
import com.github.xhiroyui.util.Command;
import com.github.xhiroyui.util.GBFWikiParser;
import com.github.xhiroyui.util.MiscUtils;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class GBFCommandsHandler extends ModuleHandler {

	private GBFWikiParser gbfWikiParser = new GBFWikiParser();

	public GBFCommandsHandler() {
		createCommands();
	}

	private void createCommands() {
		Command command;

		command = new Command(FunctionConstant.GBF_WIKI_SEARCH);
		command.setCommandName("GBF Wiki Search");
		command.setCommandDescription("Lazy people's method of searching the GBF Wiki");
		command.getCommandCallers().add("gbfwiki");
		command.getCommandCallers().add("gbfsearch");
		command.setParams(new String[] { "Query" });
		command.setMaximumArgs(69);
		command.setExample("wEapOn skILLS");
		commandList.add(command);
		
		command = new Command(FunctionConstant.GBF_GAIJIN_TIERLIST); // i plan to change this into reading an external file instead in the future
		command.setCommandName("GBF Gaijins Tier-List Link");
		command.setCommandDescription("So you can stop searching for it and just type the command instead");
		command.getCommandCallers().add("tierlist");
		command.getCommandCallers().add("gaijintierlist");
		command.setMaximumArgs(0);
		commandList.add(command);
		
		command = new Command(FunctionConstant.GBF_DAMA_GOLD_SUNSTONE); // i plan to change this into reading an external file instead in the future
		command.setCommandName("GBF Path-of-Regrets");
		command.setCommandDescription("When someone asks you \"Do I Dama/GoldBar/Sunstone <Insert weapon/summon here>?\"");
		command.getCommandCallers().add("dama");
		command.getCommandCallers().add("sunstone");
		command.getCommandCallers().add("goldbar");
		command.getCommandCallers().add("boldgar");
		command.getCommandCallers().add("zeed");
		command.getCommandCallers().add("regretlifedecisions");
		command.setMaximumArgs(0);
		commandList.add(command);
		
		command = new Command(FunctionConstant.GBF_GET_CHARACTER);
		command.setCommandName("GBF Character");
		command.setCommandDescription("Displays a GBF character with info");
		command.getCommandCallers().add("char");
		command.getCommandCallers().add("character");
		command.setParams(new String[] { "Character name" });
		command.setMaximumArgs(5);
		command.setExample("Vajra");
		commandList.add(command);
		
		command = new Command(FunctionConstant.GBF_GET_WEAPON);
		command.setCommandName("GBF Weapon");
		command.setCommandDescription("Displays a GBF weapon with info");
		command.getCommandCallers().add("wpn");
		command.getCommandCallers().add("weapon");
		command.setParams(new String[] { "Weapon name" });
		command.setMaximumArgs(5);
		command.setExample("Gargantua");
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
		String[] command = processCommand(event);
		if (command != null) {
			executeCommand(event, command);
		}
	}

	public void executeCommand(MessageReceivedEvent event, String[] command) throws IOException {
		String commandCode = validateCommand(event, command);
		if (commandCode != null) {
			switch (commandCode) {
			case FunctionConstant.GBF_GET_CHARACTER:
				createCharEmbed(gbfWikiParser.gbfWikiSearch(Arrays.copyOfRange(command, 1, command.length)) ,event);
				break;
			case FunctionConstant.GBF_GET_WEAPON:
				createWeaponEmbed(gbfWikiParser.gbfWikiSearch(Arrays.copyOfRange(command, 1, command.length)) ,event);
				break;
			case FunctionConstant.GBF_WIKI_SEARCH:
				gbfLazySearch(gbfWikiParser.gbfWikiSearch(Arrays.copyOfRange(command, 1, command.length)) ,event);
				break;
			case FunctionConstant.GBF_GAIJIN_TIERLIST:
				gaijinTierListEmbed(event);
				break;
			case FunctionConstant.GBF_DAMA_GOLD_SUNSTONE:
				pathOfRegrets(event);
				break;
			}
		}
	}


	
	private void pathOfRegrets(MessageReceivedEvent event) {
		sendMessage("Q: What do I use my **Sunstone / Gold Bar / Damascus Ingot** on?\n"
				+ "A: The fact that you are asking means you don't know what it's worth and what you *should* be doing with it and as such, you *DON'T* use it.\n\n"
				+ "If it is something you should be using one of the above items on, you wouldn't have to ask. Asking means you *don't* know what it's worth.\n\n"
				+ "The time will come eventually when you know when to use it/what to use it on __*without asking*__ and that is when you actually use it.\n\n"
				+ "If anyone tells you to use it on <insert something here>, they are leading you to the path of regrets/doom/sadness/ <a:blobSeppuku:422628396334317568>", event);
	}

	private void gaijinTierListEmbed(MessageReceivedEvent event) {
		EmbedBuilder embed = new EmbedBuilder().setLenient(true);
		embed.withAuthorName("Diamonit & Co");
		// embed.withAuthorUrl("");
		embed.withTitle("Gaijins Tier List, because filthy Gaijins can't read moonrunes");
		embed.withUrl("https://docs.google.com/spreadsheets/d/1lo-r5oP5PVDBjDtN8SlJBpFCqCcYnZmvy1d0mIQsriw/htmlview?sle=true#gid=0");
		embed.withAuthorIcon(
				"https://rin-kaenbyou.is-my-waifu.com/ToVWmJKcG.png");
		embed.withThumbnail(DiscordClient.getClient().fetchUser(157516003272687616L).getAvatarURL());
		embed.appendField("Diamonit on Patreon", "[Patreon Link](https://www.patreon.com/gaijintierlist)", false);
		embed.withColor(125, 125, 125);
		// embed.withImage("");
		embed.withDesc(
				"ITU PUN DIA! ~~*lame joke*~~");
		sendEmbed(embed, event);
	
	}

	private void gbfLazySearch(String gbfWikiSearch, MessageReceivedEvent event) throws DiscordException, MissingPermissionsException, IOException {
		String results = gbfWikiParser.lazySearch(gbfWikiSearch);
		if (results == null) 
			sendMessage("Medusa-chan couldn't find anything <a:blobSeppuku:422628396334317568>"  , event);
		else 
			sendMessage("Here's what I found for you b-baka! \n<a:blobReach2:422628404483981322> " + results , event);
		
	}

	private void createCharEmbed(String webUrl, MessageReceivedEvent event) throws IOException {
		// Do a check to see if search failed or not
		// Do X if search fails (select top result?), do Y if search success
		// Below is do Y
		try {
			EmbedBuilder embed = new EmbedBuilder().setLenient(true); 
			GBFCharacter character = gbfWikiParser.parseGbfCharacter(webUrl);
			if (character == null) {
				sendMessage("Character not found.", event);
				return;
			}
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
//			embed.withImage(character.getImageUrl());
			StringBuilder voiceActorSB = new StringBuilder();
			for (String[] voiceActor : character.getVoiceActor()) {
				voiceActorSB.append("["+voiceActor[0]+"]("+voiceActor[1]+") ");
			}
			embed.appendField("Voice Actor(s)", voiceActorSB.toString(), true);
			embed.appendField("How to Obtain", "["+character.getObtainableFrom()[0]+"]("+character.getObtainableFrom()[1]+")", true);
			if (character.getRecruitmentWeapon()!=null) 
			{ embed.appendField("Recruitment Weapon", "["+character.getRecruitmentWeapon()[0]+"]("+character.getRecruitmentWeapon()[1]+")", true); }
			
//			if (character.getBonusAtk()!=null && character.getFlbAtk()!=null)
//				embed.appendField("Atk (MIN | MAX | FLB | Fate)", character.getMinAtk() + " | " + character.getMaxAtk() + " | " + character.getFlbAtk() + " | (+" + character.getBonusAtk() + ")", true);
//			else if (character.getBonusAtk()==null && character.getFlbAtk()!=null)
//				embed.appendField("Atk (MIN | MAX | FLB )", character.getMinAtk() + " | " + character.getMaxAtk() + " | " + character.getFlbAtk(), true);
//			else if (character.getBonusAtk()!=null && character.getFlbAtk()==null)
//				embed.appendField("Atk (MIN | MAX | Fate)", character.getMinAtk() + " | " + character.getMaxAtk()  + " | (+" + character.getBonusAtk() + ")", true);
//			else
//				embed.appendField("Atk (MIN | MAX)", character.getMinAtk() + " | " + character.getMaxAtk(), true);
//
//			if (character.getBonusHp()!=null && character.getFlbHp()!=null)
//				embed.appendField("HP (MIN | MAX | FLB | Fate)", character.getMinHp() + " | " + character.getMaxHp() + " | " + character.getFlbHp() + " | (+" + character.getBonusHp() + ")", true);
//			else if (character.getBonusHp()==null && character.getFlbHp()!=null)
//				embed.appendField("HP (MIN | MAX | FLB )", character.getMinHp() + " | " + character.getMaxHp() + " | " + character.getFlbHp(), true);
//			else if (character.getBonusHp()!=null && character.getFlbHp()==null)
//				embed.appendField("HP (MIN | MAX | Fate)", character.getMinHp() + " | " + character.getMaxHp()  + " | (+" + character.getBonusHp() + ")", true);
//			else
//				embed.appendField("HP (MIN | MAX)", character.getMinHp() + " | " + character.getMaxHp(), true);
			
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
			embed.withFooterText("Data obtained from GBF Wiki");
			embed.withFooterIcon("https://cdn.discordapp.com/emojis/321247751830634496.png?v=1");
			sendEmbed(embed, event);
		}
		catch (IllegalArgumentException e) {
			sendMessage("ERROR : Failed to create embed.", event);
			e.printStackTrace();
		}
		
	}
	
	private void createWeaponEmbed(String webUrl, MessageReceivedEvent event) throws IOException {
		// Do a check to see if search failed or not
		// Do X if search fails (select top result?), do Y if search success
		// Below is do Y
		try {
			EmbedBuilder embed = new EmbedBuilder().setLenient(true);
			GBFWeapon weapon = gbfWikiParser.parseGbfWeapon(webUrl);
			if (weapon == null) {
				sendMessage("Weapon not found.", event);
				return;
			}
			if (weapon.getTitle() == null || weapon.getTitle().isEmpty())
				embed.withAuthorName("[Skybound]");
			else
				embed.withAuthorName("[" + weapon.getTitle() +"]");
			embed.withAuthorIcon(weapon.getRarityImageUrl());
			embed.withThumbnail(weapon.getThumbnailUrl());
			embed.appendDesc(weapon.getFlavor());
			embed.withTitle(weapon.getName());
			embed.withUrl(weapon.getBaseUri());
			embed.appendField(weapon.getSkill1Name() + " (" +  weapon.getSkill1Level() + ")", weapon.getSkill1Desc(), false);
			embed.appendField(weapon.getSkill2Name() + " (" + weapon.getSkill2Level() + ")", weapon.getSkill2Desc(), false);
			embed.appendField("Ougi", weapon.getOugiName() + " - " + MiscUtils.gbfWikiTextParser(weapon.getOugiEffect()), false);
			
			switch (weapon.getElement().toLowerCase()) {
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
			embed.withFooterText("Data obtained from GBF Wiki");
			embed.withFooterIcon("https://cdn.discordapp.com/emojis/321247751830634496.png?v=1");
			sendEmbed(embed, event);
		}
		catch (IllegalArgumentException e) {
			sendMessage("ERROR : Failed to create embed.", event);
			e.printStackTrace();
		}
		
	}
}