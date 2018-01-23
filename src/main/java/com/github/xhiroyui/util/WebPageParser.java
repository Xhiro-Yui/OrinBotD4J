package com.github.xhiroyui.util;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.github.xhiroyui.bean.GBFCharacter;

public class WebPageParser {
	
	public String gbfWikiSearch(String[] query) {
		String searchUrl = "https://gbf.wiki/index.php?search=";
		for (int i = 0; i < query.length ; i++) {
			if (i == 0) {
				searchUrl = searchUrl + query[i];
			} else {
				searchUrl = searchUrl + "+" + query[i];	
			}
		}
		return searchUrl;
	}
	
	public GBFCharacter parseGbfCharacter(String url) throws IOException {
		GBFCharacter character = new GBFCharacter();
		Document doc = Jsoup.connect(url).get();
		character.setBaseUri(doc.baseUri());
		
		Element content = doc.getElementById("content");
		
		Elements h1 = content.select("h1#firstHeading");
		character.setName(h1.text());

		Elements baseArt = content.select("[title=\"Base Art\"] img[src]");	
		character.setImageUrl(baseArt.attr("abs:src"));
		
		Elements thumbnail = content.select("div#mw-content-text a[title=\""+h1.text()+"\"] img");		
		character.setThumbnailUrl(thumbnail.attr("abs:src"));
		
//		Elements spriteArt = content.select("[title$=\"Sprite\"] img[src]");	
//		for (Element sprites : spriteArt) {
//			character.setThumbnailUrl(sprites.attr("abs:src"));
//		}
		
		Elements table = content.select("[class^=\"wikitable character\"]");
		for (Element tablerow : table) {
			Elements tablecontents = tablerow.select("tr");
			for (Element trcontents : tablecontents) {
				if (trcontents.text().toLowerCase().contains("element".toLowerCase())) {
					Elements td = trcontents.select("img");
					if (td.toString().toLowerCase().contains("label".toLowerCase())) {
						character.setElement(td.attr("alt").substring(14, td.attr("alt").length() - 4));
					}
				}
				if (trcontents.text().toLowerCase().contains("race".toLowerCase())) {
					Elements td = trcontents.select("img");
					if (td.toString().toLowerCase().contains("label".toLowerCase())) {
						character.setRace(td.attr("alt").substring(11, td.attr("alt").length() - 4));
					}
				}
				if (trcontents.text().toLowerCase().contains("style".toLowerCase())) {
					Elements td = trcontents.select("img");
					if (td.toString().toLowerCase().contains("label".toLowerCase())) {
						character.setStyle(td.attr("alt").substring(11, td.attr("alt").length() - 4));
					}
				}
				if (trcontents.text().toLowerCase().contains("gender".toLowerCase())) {
					Elements td = trcontents.select("td");
					character.setGender(td.text());
				}
				if (trcontents.text().toLowerCase().contains("specialty".toLowerCase())) {
					Elements td = trcontents.select("img");
					if (td.toString().toLowerCase().contains("label".toLowerCase())) {
						character.getSpecialty().add(td.attr("alt").substring(13, td.attr("alt").length() - 4));
					}
				}
			}

		}
		return character;
	}
}
