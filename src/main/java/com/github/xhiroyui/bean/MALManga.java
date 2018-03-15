package com.github.xhiroyui.bean;

import com.google.gson.JsonArray;

public class MALManga {
	private int statusCode;
	private String link;
	private String title;
	private String jpTitle;
	private String status;
	private String thumbnailUrl; // image_url
	private String type;
	private String volumes;
	private String chapters;
	private boolean publishing;
	private String publishedString;
	private int rank;
	private double score;
	private int scoredBy;
	private String synopsis;
	private JsonArray genre;
	private JsonArray author;
	private JsonArray serialization;
	
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getVolumes() {
		return volumes;
	}
	public void setVolumes(String volumes) {
		this.volumes = volumes;
	}
	public String getChapters() {
		return chapters;
	}
	public void setChapters(String chapters) {
		this.chapters = chapters;
	}
	public boolean isPublishing() {
		return publishing;
	}
	public void setPublishing(boolean publishing) {
		this.publishing = publishing;
	}
	public String getPublishedString() {
		return publishedString;
	}
	public void setPublishedString(String publishedString) {
		this.publishedString = publishedString;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
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
	public String getSynopsis() {
		return synopsis;
	}
	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}
	public JsonArray getGenre() {
		return genre;
	}
	public void setGenre(JsonArray genre) {
		this.genre = genre;
	}
	public JsonArray getAuthor() {
		return author;
	}
	public void setAuthor(JsonArray author) {
		this.author = author;
	}
	public JsonArray getSerialization() {
		return serialization;
	}
	public void setSerialization(JsonArray serialization) {
		this.serialization = serialization;
	}
}
