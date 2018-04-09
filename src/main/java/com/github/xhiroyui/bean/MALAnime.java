package com.github.xhiroyui.bean;

import com.google.gson.JsonArray;

public class MALAnime {
	private int id;
	private int statusCode;
	private String link;
	private String title;
	private String jpTitle;
	private String thumbnailUrl; // image_url
	private String type;
	private String source;
	private int episodes;
	private String status;
	private String airedString; //aired_string
	private double score;
	private int scoredBy;
	private int rank;
	private String synopsis;
	private JsonArray studio;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getJpTitle() {
		return jpTitle;
	}
	public void setJpTitle(String jpTitle) {
		this.jpTitle = jpTitle;
	}
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public int getEpisodes() {
		return episodes;
	}
	public void setEpisodes(int episodes) {
		this.episodes = episodes;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAiredString() {
		return airedString;
	}
	public void setAiredString(String airedString) {
		this.airedString = airedString;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public int getScoredBy() {
		return scoredBy;
	}
	public void setScoredBy(int scoredBy) {
		this.scoredBy = scoredBy;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public String getSynopsis() {
		return synopsis;
	}
	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}
	public JsonArray getStudio() {
		return studio;
	}
	public void setStudio(JsonArray studio) {
		this.studio = studio;
	}
	

}
