package com.github.xhiroyui.util;

import java.util.Map;
import java.util.Set;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;

import com.github.xhiroyui.bean.MALAnime;
import com.github.xhiroyui.bean.MALSearch;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.ws.rs.core.MediaType;
// This is not an actual MAL parser but uses the JikanAPI.

public class MALParser {
	private static final String searchUrl = "https://api.jikan.me/";
	
	public MALSearch malSearch(String flag, String query) {
		try {
			Client client = ClientBuilder.newClient();
			Response response = client.target(searchUrl + "search/" + flag + "/" + query)
			  .request(MediaType.TEXT_PLAIN_TYPE)
			  .get();
			
			MALSearch search = new MALSearch();
			search.setStatus(response.getStatus());
			search.setFlag(flag);
			
			JsonParser jp = new JsonParser();
			JsonArray ja = jp.parse(response.readEntity(String.class)).getAsJsonObject().get("result").getAsJsonArray();
			for (JsonElement each : ja) {				
				Set<Map.Entry<String, JsonElement>> entries = ((JsonObject) each).entrySet();
				String[] animu = new String[3];
				for (Map.Entry<String, JsonElement> entry: entries) {
					if (entry.getKey().equalsIgnoreCase("id"))
						animu[0] = entry.getValue().toString();
					if (entry.getKey().equalsIgnoreCase("title"))
						animu[1] = entry.getValue().toString().substring(1, entry.getValue().toString().length()-1);
					if (entry.getKey().equalsIgnoreCase("url"))
						animu[2] = entry.getValue().toString().substring(1, entry.getValue().toString().length()-1);
					
				}
				search.getResults().add(animu);
				System.out.println(animu[0] + animu[1] + animu[2]);
				if (search.getResults().size() == 5)
					return search;
			}
			return search;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
	}
	
	public MALAnime malAnimeSearch(String id) {
		try {
			Client client = ClientBuilder.newClient();
			Response response = client.target(searchUrl + "anime/" + id)
			  .request(MediaType.TEXT_PLAIN_TYPE)
			  .get();
			
			MALAnime anime = new MALAnime();
			anime.setStatusCode(response.getStatus());
			
			JsonParser jp = new JsonParser();
			JsonObject ja = jp.parse(response.readEntity(String.class)).getAsJsonObject();
			Set<Map.Entry<String, JsonElement>> entries = ja.entrySet();
			for (Map.Entry<String, JsonElement> entry: entries) {
				if (entry.getKey().equalsIgnoreCase("link_canonical"))
					anime.setLink(entry.getValue().toString().substring(1, entry.getValue().toString().length()-1));
				if (entry.getKey().equalsIgnoreCase("title"))
					anime.setTitle(entry.getValue().toString().substring(1, entry.getValue().toString().length()-1));
				if (entry.getKey().equalsIgnoreCase("title_japanese"))
					anime.setJpTitle(entry.getValue().toString().substring(1, entry.getValue().toString().length()-1));
				if (entry.getKey().equalsIgnoreCase("image_url"))
					anime.setThumbnailUrl(entry.getValue().toString().substring(1, entry.getValue().toString().length()-1));
				if (entry.getKey().equalsIgnoreCase("type"))
					anime.setType(entry.getValue().toString().substring(1, entry.getValue().toString().length()-1));
				if (entry.getKey().equalsIgnoreCase("source"))
					anime.setSource(entry.getValue().toString().substring(1, entry.getValue().toString().length()-1));
				if (entry.getKey().equalsIgnoreCase("episodes"))
					anime.setEpisodes(entry.getValue().getAsInt());
				if (entry.getKey().equalsIgnoreCase("status"))
					anime.setStatus(entry.getValue().toString().substring(1, entry.getValue().toString().length()-1));
				if (entry.getKey().equalsIgnoreCase("aired_string"))
					anime.setAiredTime(entry.getValue().toString().substring(1, entry.getValue().toString().length()-1));
				if (entry.getKey().equalsIgnoreCase("score"))
					anime.setScore(entry.getValue().getAsDouble());
				if (entry.getKey().equalsIgnoreCase("scored_by"))
					anime.setScoredBy(entry.getValue().getAsInt());
				if (entry.getKey().equalsIgnoreCase("rank"))
					anime.setRank(entry.getValue().getAsInt());
				if (entry.getKey().equalsIgnoreCase("synopsis"))
					anime.setSynopsis(entry.getValue().toString().substring(1, entry.getValue().toString().length()-1));
				if (entry.getKey().equalsIgnoreCase("studio"))
					anime.setStudio(entry.getValue().getAsJsonArray());
			}
			return anime;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
	}
}
