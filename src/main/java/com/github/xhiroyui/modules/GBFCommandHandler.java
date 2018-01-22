package com.github.xhiroyui.modules;

import java.io.IOException;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.github.xhiroyui.util.Command;
import com.github.xhiroyui.util.SearchUtil;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.RequestBuffer;

public class GBFCommandHandler extends ModuleHandler {

	private GBFCommands module;
	private SearchUtil searchUtil = new SearchUtil();

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
		command.setMaximumArgs(1);
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
				searchUtil.gbfWikiSearch(Arrays.copyOfRange(command, 1, command.length));
				parseGBFWiki(event);
				break;
			case "SEARCH_GBF_WIKI":
				sendMessage(searchUtil.gbfWikiSearch(Arrays.copyOfRange(command, 1, command.length)), event);
				break;
			}
		}
	}

	public void sendEmbed(MessageReceivedEvent event) {

		EmbedBuilder builder = new EmbedBuilder();
		builder.appendField("fieldTitleInline", "fieldContentInline", true);
		builder.appendField("fieldTitleInline2", "fieldContentInline2", true);
		builder.appendField("fieldTitleNotInline", "fieldContentNotInline", false);
		builder.appendField(":tada: fieldWithCoolThings :tada:", "[hiddenLink](http://i.imgur.com/Y9utuDe.png)", false);
		builder.withAuthorName("Murgleis");
		builder.withAuthorIcon("https://gbf.wiki/images/thumb/d/db/Label_Weapon_Sabre.png/61px-Label_Weapon_Sabre.png");
		builder.withAuthorUrl("http://i.imgur.com/oPvYFj3.png");
		builder.withColor(255, 0, 0);
		builder.withDesc(
				"Sincerity intensifies the deep blue radiance of this sabre. Its ornamentation makes any vow sworn on this blade as unbreakable as platinum.");
		builder.withTitle("withTitle");
		builder.withUrl("http://i.imgur.com/IrEVKQq.png");
		builder.withImage("https://gbf.wiki/images/thumb/f/ff/Murgleis.png/600px-Murgleis.png");
		builder.withFooterIcon("http://i.imgur.com/Ch0wy1e.png");
		builder.withFooterText("footerText");
		builder.withFooterIcon("http://i.imgur.com/TELh8OT.png");
		builder.withThumbnail("https://gbf.wiki/images/thumb/f/ff/Murgleis.png/600px-Murgleis.png");
		RequestBuffer.request(() -> event.getChannel().sendMessage(builder.build()));
	}

	public void parseGBFWiki(MessageReceivedEvent event) throws IOException {
		EmbedBuilder builder = new EmbedBuilder();
		
		Document doc = Jsoup.connect("https://gbf.wiki/Yuel").get();
		Element content = doc.getElementById("content");
		
		Elements h1 = content.select("h1#firstHeading");
		builder.withAuthorName(h1.text());

		Elements baseArt = content.select("[title=\"Base Art\"] img[src]");	
		builder.withImage(baseArt.attr("abs:src"));
		
		RequestBuffer.request(() -> event.getChannel().sendMessage(builder.build()));
	}
}