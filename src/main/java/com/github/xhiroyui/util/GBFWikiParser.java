package com.github.xhiroyui.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import com.github.xhiroyui.bean.GBFCharacter;
import com.google.gson.JsonParser;

public class GBFWikiParser {
	final static String baseJsonUrl = "https://gbf.wiki/api.php?action=query&prop=revisions&rvprop=content&format=json&formatversion=2&titles=";
	final static String baseSearchUrl = "https://gbf.wiki/index.php?search=";
	private Matcher matcher; 
	
	public String gbfWikiSearch(String[] query) {
		String searchUrl = baseSearchUrl + query[0];
		for (int i = 1; i < query.length; i++) {
				searchUrl = searchUrl + "+" + query[i];
		}
		return searchUrl;
	}

	public String[] nameSplitter(String message) {
		String[] name = StringUtils.split(message.toLowerCase(), ' ');
		return name;
	}
	
	public String lazySearch(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		if (doc.baseUri().contains("index.php?search=")) {
			try {
				return doc.select("div.mw-search-result-heading").first().select("a").attr("abs:href");
			} catch (NullPointerException e) {
				return null;
			}
		} else {
			return doc.baseUri();
		}
	}
	
	public GBFCharacter parseGbfCharacter(String url) throws IOException {
		// Web Connection
		Document doc = Jsoup.connect(url).get();
		
		// Json Connection
		URL jsonurl = new URL(baseJsonUrl + URLEncoder.encode(doc.select("h1#firstHeading").text(), "UTF-8"));
		InputStreamReader reader = new InputStreamReader(jsonurl.openStream());
		JsonParser jsonParser = new JsonParser();
		String jsonContent = jsonParser.parse(reader).getAsJsonObject().get("query").getAsJsonObject().get("pages")
				.getAsJsonArray().get(0).getAsJsonObject().get("revisions").getAsJsonArray().get(0).getAsJsonObject()
				.get("content").getAsString();
		if (StringUtils.indexOfIgnoreCase(jsonContent, "{{Character") == -1)
			return null;
		String[] values = jsonContent.split("\\\n\\|");
		
		// Building char object
		GBFCharacter character = new GBFCharacter();
		character.setBaseUri(doc.baseUri());
		character.setThumbnailUrl(doc.select("meta[property=\"og:image\"]").attr("content"));
		character.setDescription(doc.select("meta[name=\"description\"]").attr("content"));
		Element content = doc.getElementById("content");
		character.setImageUrl(content.select("[title=\"Base Art\"] img[src]").attr("abs:src"));
		character.setRarityImageUrl(content.select("img[alt^=\"Rarity\"]").attr("abs:src"));
		for (Element node : doc.select("a.extiw"))
			character.setVoiceActor(new String[] {node.text(), node.attr("abs:href")});
		
		
		for (String contents : values) {
			if (contents.toLowerCase().startsWith("id"))
				character.setId(contents.substring(contents.lastIndexOf("=") + 1).trim());
			else if (contents.toLowerCase().startsWith("name"))
				character.setName(contents.substring(contents.lastIndexOf("=") + 1).trim());
			else if (contents.toLowerCase().startsWith("releaseDate"))
				character.setReleaseDate(contents.substring(contents.lastIndexOf("=") + 1).trim());
			else if (contents.toLowerCase().startsWith("gender"))
				character.setGender(contents.substring(contents.lastIndexOf("=") + 1).trim());
			else if (contents.toLowerCase().startsWith("title"))
				character.setTitle(contents.substring(contents.lastIndexOf("=") + 1).trim());
			else if (contents.toLowerCase().startsWith("rarity"))
				character.setRarity(contents.substring(contents.lastIndexOf("=") + 1).trim());
			else if (contents.toLowerCase().startsWith("element"))
				character.setElement(contents.substring(contents.lastIndexOf("=") + 1).trim());
			else if (contents.toLowerCase().startsWith("type"))
				character.setStyle(contents.substring(contents.lastIndexOf("=") + 1).trim());
			else if (contents.toLowerCase().startsWith("race"))
				character.setRace(contents.substring(contents.lastIndexOf("=") + 1).trim());
			else if (contents.toLowerCase().startsWith("join=")) {
				matcher = RegEx.joinPattern.matcher(contents.substring(contents.lastIndexOf("=") + 1).trim());
				if (matcher.find()) {
					character.setObtainableFrom(new String[] { matcher.group(1), doc.select("a[title=\""+ matcher.group(1) +"\"]").attr("abs:href")});
				}
			}
//			 else if (contents.toLowerCase().startsWith("recruitmentWeapon"))
			// character.setRecruitmentWeapon(contents.substring(contents.lastIndexOf("=")
			// + 1).trim());
			else if (contents.toLowerCase().startsWith("weapon"))
				character.setSpecialty(contents.substring(contents.lastIndexOf("=") + 1).trim());
			else if (contents.toLowerCase().startsWith("min_atk"))
				character.setMinAtk(contents.substring(contents.lastIndexOf("=") + 1).trim());
			else if (contents.toLowerCase().startsWith("max_atk"))
				character.setMaxAtk(contents.substring(contents.lastIndexOf("=") + 1).trim());
			else if (contents.toLowerCase().startsWith("flb_atk"))
				character.setFlbAtk(contents.substring(contents.lastIndexOf("=") + 1).trim());
			else if (contents.toLowerCase().startsWith("bonus_atk"))
				character.setBonusAtk(contents.substring(contents.lastIndexOf("=") + 1).trim());
			else if (contents.toLowerCase().startsWith("min_hp"))
				character.setMinHp(contents.substring(contents.lastIndexOf("=") + 1).trim());
			else if (contents.toLowerCase().startsWith("max_hp"))
				character.setMaxHp(contents.substring(contents.lastIndexOf("=") + 1).trim());
			else if (contents.toLowerCase().startsWith("flb_hp"))
				character.setFlbHp(contents.substring(contents.lastIndexOf("=") + 1).trim());
			else if (contents.toLowerCase().startsWith("bonus_hp"))
				character.setBonusHp(contents.substring(contents.lastIndexOf("=") + 1).trim());
		}
		return character;
	}
}
