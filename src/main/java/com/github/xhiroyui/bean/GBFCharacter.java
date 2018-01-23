package com.github.xhiroyui.bean;

import java.util.ArrayList;

public class GBFCharacter {
	
	private String baseUri;
	private String name;
	private String element;
	private String race;
	private String style;
	private ArrayList<String> specialty = new ArrayList<String>();
	private String gender;
	private String imageUrl;
	private String thumbnailUrl;
	public String getBaseUri() {
		return baseUri;
	}
	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getElement() {
		return element;
	}
	public void setElement(String element) {
		this.element = element;
	}
	public String getRace() {
		return race;
	}
	public void setRace(String race) {
		this.race = race;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public ArrayList<String> getSpecialty() {
		return specialty;
	}
	public void setSpecialty(ArrayList<String> specialty) {
		this.specialty = specialty;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
	
	
}
