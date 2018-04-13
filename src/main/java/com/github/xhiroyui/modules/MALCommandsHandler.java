package com.github.xhiroyui.modules;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.xhiroyui.bean.MALAnime;
import com.github.xhiroyui.bean.MALManga;
import com.github.xhiroyui.bean.MALPerson;
import com.github.xhiroyui.bean.MALSearch;
import com.github.xhiroyui.constant.BotConstant;
import com.github.xhiroyui.constant.FunctionConstant;
import com.github.xhiroyui.util.BotCache;
import com.github.xhiroyui.util.Command;
import com.github.xhiroyui.util.MALParser;
import com.google.gson.JsonElement;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

public class MALCommandsHandler extends ModuleHandler {
	private static final Logger logger = LoggerFactory.getLogger(MALCommandsHandler.class.getSimpleName());
	private MALParser malParser = new MALParser();

	public MALCommandsHandler() {
		createCommands();
		logger.debug("MALCommands successfully initialized");
	}

	private void createCommands() {
		Command command;

		command = new Command(FunctionConstant.MAL_SEARCH);
		command.setCommandName("MyAnimeList Search");
		command.setCommandDescription("Returns a dynamic search of the given query");
		command.getCommandCallers().add("mal");
		command.setParams(new String[] { "Search type" });
		command.setParams(new String[] { "Search query" });
		command.setMaximumArgs(69);
		command.setExample("manga Montage");
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

	@EventSubscriber
	public void OnReactionAddEvent(ReactionAddEvent event)
			throws DiscordException, MissingPermissionsException, ExecutionException {
		MALSearch search = BotCache.malSearchCache
				.get(event.getUser().getStringID() + Long.toString(event.getMessageID()));
		if (search != null)
			if (event.getReaction().getEmoji().equals(BotConstant.REACTION_ONE_TILL_FIVE[0])) {
				createMALEmbed(search.getFlag(), event.getMessage(), search.getResults().get(0)[0]);
			} else if (event.getReaction().getEmoji().equals(BotConstant.REACTION_ONE_TILL_FIVE[1])) {
				createMALEmbed(search.getFlag(), event.getMessage(), search.getResults().get(1)[0]);
			} else if (event.getReaction().getEmoji().equals(BotConstant.REACTION_ONE_TILL_FIVE[2])) {
				createMALEmbed(search.getFlag(), event.getMessage(), search.getResults().get(2)[0]);
			} else if (event.getReaction().getEmoji().equals(BotConstant.REACTION_ONE_TILL_FIVE[3])) {
				createMALEmbed(search.getFlag(), event.getMessage(), search.getResults().get(3)[0]);
			} else if (event.getReaction().getEmoji().equals(BotConstant.REACTION_ONE_TILL_FIVE[4])) {
				createMALEmbed(search.getFlag(), event.getMessage(), search.getResults().get(4)[0]);
			}
	}

	public void executeCommand(MessageReceivedEvent event, String[] command) throws IOException {
		String commandCode = validateCommand(event, command);
		if (commandCode != null) {
			switch (commandCode) {
			case FunctionConstant.MAL_SEARCH:
				try {
					logger.info("{} [{}] - {} - {}", event.getAuthor().getDisplayName(event.getGuild()),
							event.getAuthor().getLongID(), FunctionConstant.MAL_SEARCH, gson.toJson(command));
					searchMAL(command, event);
				} catch (Exception e) {
					throwError(FunctionConstant.MAL_SEARCH, e, event);
				}
				break;

			}
		}
	}

	public void searchMAL(String[] query, MessageReceivedEvent event) throws IOException {
		try {
			if (query[1].equalsIgnoreCase("anime") || query[1].equalsIgnoreCase("manga")
					|| query[1].equalsIgnoreCase("person") || query[1].equalsIgnoreCase("character")) {
				IMessage waitMsg;
				MALSearch searchResults;
				StringBuilder malQuery = new StringBuilder();
				for (int i = 2; i < query.length; i++) {
					if (i == 2)
						malQuery.append(query[i]);
					else
						malQuery.append(" " + query[i]);
				}
				if (malQuery.length() == 0) {
					sendMessage("Please search for something :smirk:", event);
					return;
				} else {
					waitMsg = sendMessage("Querying MyAnimeList . . .", event);
					searchResults = malParser.malSearch(query[1], malQuery.toString());
				}
				if (searchResults == null) {
					sendMessage("Error : This is an internal error. Please contact the bot author to solve this issue.",
							event);
					return;
				} else {
					if (searchResults.getStatus() == 200) {
						createMALSearchEmbed(event.getAuthor().getStringID() + waitMsg.getStringID(), waitMsg,
								searchResults, event);
					} else if (searchResults.getStatus() == 429) {
						sendMessage("Rate limit reached. This command will be unavailable for the rest of the day.",
								event);
					}
				}
			} else
				sendMessage(
						"Please dictate a search type as first parameter. Available search types are `anime`, `manga`, `char`, `person`.",
						event);
		} catch (Exception e) {
			sendMessage("ERROR!", event);
			e.printStackTrace();
		}

	}

	private void createMALSearchEmbed(String key, IMessage waitMsg, MALSearch results, MessageReceivedEvent event) {
		EmbedBuilder embed = new EmbedBuilder().setLenient(true);
		embed.withAuthorName("MyAnimeList");
		embed.withAuthorIcon("https://pbs.twimg.com/profile_images/926302376738377729/SMlpasPv_bigger.jpg");
		embed.withAuthorUrl("https://myanimelist.net/");
		embed.withThumbnail("https://pbs.twimg.com/profile_images/926302376738377729/SMlpasPv_400x400.jpg");
		// embed.withTitle("*Search Results [" + results.getFlag() + "]*");
		embed.withDesc(":mag_right: Displaying top 5 search results [" + results.getFlag().toUpperCase() + "]\n");
		int count = 1;
		for (String[] each : results.getResults()) {
			embed.appendDesc("\n" + count + ". [" + each[1] + "](" + each[2] + ")\n");
			count++;
		}
		embed.withFooterText("Data queried from MyAnimeList via Jikan API");
		embed.withColor(46, 81, 162);
		waitMsg.edit("Search completed.", embed.build());
		BotCache.malSearchCache.put(key, results);
		if (results.getResults().size() > 0)
			addReaction(waitMsg,
					Arrays.copyOfRange(BotConstant.REACTION_ONE_TILL_FIVE, 0, results.getResults().size()));
	}

	private void createMALEmbed(String flag, IMessage message, String id) {
		if (flag.equalsIgnoreCase("anime"))
			createMALAnimeEmbed(malParser.malAnimeSearch(id), message);
		if (flag.equalsIgnoreCase("manga"))
			createMALMangaEmbed(malParser.malMangaSearch(id), message);
		if (flag.equalsIgnoreCase("character"))
			createMALCharaEmbed();
		if (flag.equalsIgnoreCase("person"))
			createMALPersonEmbed(malParser.malPersonSearch(id), message);
	}

	private void createMALCharaEmbed() {
	}

	private void createMALPersonEmbed(MALPerson results, IMessage toEdit) {
		EmbedBuilder embed = new EmbedBuilder().setLenient(true);
		if (results.getStatusCode() == 429) {
			toEdit.edit("Rate limit reached. This command will be unavailable for the rest of the day.");
			toEdit.removeAllReactions();
			return;
		}
		embed.withTitle(
				results.getNameRomaji() + " | " + (results.getFamilyName() != null ? results.getFamilyName() : "")
						+ (results.getGivenName() != null ? results.getGivenName() : ""));
		embed.withUrl(results.getLink());
		embed.appendField("Birthday", results.getBirthday(), true);
		if (results.getWebsite().length() > 7)
			embed.appendField("Website", "[Link](" + results.getWebsite() + ")", true);
		embed.withThumbnail(results.getThumbnailUrl());
		if (results.getDescription().length() > 500)
			embed.withDesc(results.getDescription().substring(0, 500) + "...");
		else
			embed.withDesc(results.getDescription());
		embed.withColor(46, 81, 162);
		toEdit.edit(embed.build());
		toEdit.removeAllReactions();

	}

	private void createMALMangaEmbed(MALManga results, IMessage toEdit) {
		EmbedBuilder embed = new EmbedBuilder().setLenient(true);
		if (results.getStatusCode() == 429) {
			toEdit.edit("Rate limit reached. This command will be unavailable for the rest of the day.");
			toEdit.removeAllReactions();
			return;
		}
		embed.withTitle(results.getTitle());
		embed.withUrl(results.getLink());
		embed.withThumbnail(results.getThumbnailUrl());
		if (results.getSynopsis().length() > 500)
			embed.withDesc(StringEscapeUtils.unescapeHtml4(results.getSynopsis().substring(0, 500)) + "...");
		else
			embed.withDesc(results.getSynopsis());
		embed.appendField("Type", results.getType(), true);
		embed.appendField("Status", results.getStatus(), true);
		embed.appendField("Volumes", results.getVolumes(), true);
		embed.appendField("Chapter", results.getChapters(), true);
		embed.appendField("Publishing Status", Boolean.toString(results.isPublishing()), true);
		embed.appendField("Published Date", results.getPublishedString().replace(" to ", "-"), true);
		embed.appendField("Score", Double.toString(results.getScore()), true);
		embed.appendField("Scored By", Integer.toString(results.getScoredBy()), true);
		embed.appendField("Rank", Integer.toString(results.getRank()), true);
		StringBuilder genre = new StringBuilder();
		for (JsonElement element : results.getGenre()) {
			Set<Map.Entry<String, JsonElement>> entries = element.getAsJsonObject().entrySet();
			String genreName = null;
			String genreLink = null;
			for (Map.Entry<String, JsonElement> entry : entries) {
				if (entry.getKey().equalsIgnoreCase("name"))
					genreName = "[" + entry.getValue().toString().substring(1, entry.getValue().toString().length() - 1)
							+ "]";
				if (entry.getKey().equalsIgnoreCase("url"))
					genreLink = "(" + entry.getValue().toString().substring(1, entry.getValue().toString().length() - 1)
							+ ")";
			}
			if (genre.length() == 0)
				genre.append(genreName + genreLink);
			else
				genre.append(", " + genreName + genreLink);

		}
		embed.appendField("Genre", genre.toString(), false);
		StringBuilder author = new StringBuilder();
		for (JsonElement element : results.getAuthor()) {
			Set<Map.Entry<String, JsonElement>> entries = element.getAsJsonObject().entrySet();
			String authorName = null;
			String authorLink = null;
			for (Map.Entry<String, JsonElement> entry : entries) {
				if (entry.getKey().equalsIgnoreCase("name"))
					authorName = "["
							+ entry.getValue().toString().substring(1, entry.getValue().toString().length() - 1) + "]";
				if (entry.getKey().equalsIgnoreCase("url"))
					authorLink = "("
							+ entry.getValue().toString().substring(1, entry.getValue().toString().length() - 1) + ")";
			}
			if (author.length() == 0)
				author.append(authorName + authorLink);
			else
				author.append(", " + authorName + authorLink);

		}
		embed.appendField("Author", author.toString(), false);
		StringBuilder serialization = new StringBuilder();
		for (JsonElement element : results.getSerialization()) {
			Set<Map.Entry<String, JsonElement>> entries = element.getAsJsonObject().entrySet();
			String serializationName = null;
			String serializationLink = null;
			for (Map.Entry<String, JsonElement> entry : entries) {
				if (entry.getKey().equalsIgnoreCase("name"))
					serializationName = "["
							+ entry.getValue().toString().substring(1, entry.getValue().toString().length() - 1) + "]";
				if (entry.getKey().equalsIgnoreCase("url"))
					serializationLink = "("
							+ entry.getValue().toString().substring(1, entry.getValue().toString().length() - 1) + ")";
			}
			if (serialization.length() == 0)
				serialization.append(serializationName + serializationLink);
			else
				serialization.append(", " + serializationName + serializationLink);

		}
		embed.appendField("Serialization", serialization.toString(), false);

		embed.withColor(46, 81, 162);
		toEdit.edit(embed.build());
		toEdit.removeAllReactions();
	}

	private void createMALAnimeEmbed(MALAnime results, IMessage toEdit) {
		EmbedBuilder embed = new EmbedBuilder().setLenient(true);
		if (results.getStatusCode() == 429) {
			toEdit.edit("Rate limit reached. This command will be unavailable for the rest of the day.");
			toEdit.removeAllReactions();
			return;
		}
		embed.withTitle(results.getTitle());
		embed.withUrl(results.getLink());
		embed.withThumbnail(results.getThumbnailUrl());
		if (results.getSynopsis().length() > 500)
			embed.withDesc(StringEscapeUtils.unescapeHtml4(results.getSynopsis().substring(0, 500)) + "...");
		else
			embed.withDesc(results.getSynopsis());
		embed.appendField("Type", results.getType(), true);
		embed.appendField("Source", results.getSource(), true);
		embed.appendField("Episodes", Integer.toString(results.getEpisodes()), true);
		embed.appendField("Status", results.getStatus(), true);
		embed.appendField("Aired", results.getAiredString().replace(" to ", "-"), true);
		embed.appendField("Score", Double.toString(results.getScore()), true);
		embed.appendField("Scored By", Integer.toString(results.getScoredBy()), true);
		embed.appendField("Rank", Integer.toString(results.getRank()), true);
		StringBuilder producer = new StringBuilder();
		for (JsonElement element : results.getStudio()) {
			Set<Map.Entry<String, JsonElement>> entries = element.getAsJsonObject().entrySet();
			String producerName = null;
			String producerLink = null;
			for (Map.Entry<String, JsonElement> entry : entries) {
				if (entry.getKey().equalsIgnoreCase("name"))
					producerName = "["
							+ entry.getValue().toString().substring(1, entry.getValue().toString().length() - 1) + "]";
				if (entry.getKey().equalsIgnoreCase("url"))
					producerLink = "("
							+ entry.getValue().toString().substring(1, entry.getValue().toString().length() - 1) + ")";
			}
			if (producer.length() == 0)
				producer.append(producerName + producerLink);
			else
				producer.append(", " + producerName + producerLink);

		}
		embed.appendField("Studio", producer.toString(), false);
		embed.withColor(46, 81, 162);
		toEdit.edit(embed.build());
		toEdit.removeAllReactions();

	}
}