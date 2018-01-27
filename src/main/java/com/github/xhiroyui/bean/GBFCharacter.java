package com.github.xhiroyui.bean;

import java.util.ArrayList;

public class GBFCharacter {
	
	private String baseUri;
	private String thumbnailUrl;
	private String description;
	private String imageUrl;
	private String id;
	private String name;
	private String releaseDate;
	private String gender;
	private String title;
	private String rarity;
	private String rarityImageUrl;
	private String element;
	private String style;
	private String race;
	private String[] voiceActor;
	private String[] obtainableFrom;
	private String[] recruitmentWeapon;
	private String specialty;
	private String minAtk;
	private String maxAtk;
	private String flbAtk;
	private String minHp;
	private String maxHp;
	private String flbHp;

	private ArrayList<String> chargeAttack = new ArrayList<String>();
	private GBFCharacterSkills characterSkills;
	private GBFCharacterSkills supportSkills;
	
	public String getBaseUri() {
		return baseUri;
	}
	public void setBaseUri(String baseUri) {
		this.baseUri = baseUri;
	}
	public String getThumbnailUrl() {
		return thumbnailUrl;
	}
	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReleaseDate() {
		return releaseDate;
	}
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRarity() {
		return rarity;
	}
	public void setRarity(String rarity) {
		this.rarity = rarity;
	}
	public String getRarityImageUrl() {
		return rarityImageUrl;
	}
	public void setRarityImageUrl(String rarityImageUrl) {
		this.rarityImageUrl = rarityImageUrl;
	}
	public String getElement() {
		return element;
	}
	public void setElement(String element) {
		this.element = element;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public String getRace() {
		return race;
	}
	public void setRace(String race) {
		this.race = race;
	}
	public String[] getVoiceActor() {
		return voiceActor;
	}
	public void setVoiceActor(String[] voiceActor) {
		this.voiceActor = voiceActor;
	}
	public String[] getObtainableFrom() {
		return obtainableFrom;
	}
	public void setObtainableFrom(String[] obtainableFrom) {
		this.obtainableFrom = obtainableFrom;
	}
	public String[] getRecruitmentWeapon() {
		return recruitmentWeapon;
	}
	public void setRecruitmentWeapon(String[] recruitmentWeapon) {
		this.recruitmentWeapon = recruitmentWeapon;
	}
	public String getSpecialty() {
		return specialty;
	}
	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}
	public String getMinAtk() {
		return minAtk;
	}
	public void setMinAtk(String minAtk) {
		this.minAtk = minAtk;
	}
	public String getMaxAtk() {
		return maxAtk;
	}
	public void setMaxAtk(String maxAtk) {
		this.maxAtk = maxAtk;
	}
	public String getFlbAtk() {
		return flbAtk;
	}
	public void setFlbAtk(String flbAtk) {
		this.flbAtk = flbAtk;
	}
	public String getMinHp() {
		return minHp;
	}
	public void setMinHp(String minHp) {
		this.minHp = minHp;
	}
	public String getMaxHp() {
		return maxHp;
	}
	public void setMaxHp(String maxHp) {
		this.maxHp = maxHp;
	}
	public String getFlbHp() {
		return flbHp;
	}
	public void setFlbHp(String flbHp) {
		this.flbHp = flbHp;
	}
	public ArrayList<String> getChargeAttack() {
		return chargeAttack;
	}
	public void setChargeAttack(ArrayList<String> chargeAttack) {
		this.chargeAttack = chargeAttack;
	}
	public GBFCharacterSkills getCharacterSkills() {
		return characterSkills;
	}
	public void setCharacterSkills(GBFCharacterSkills characterSkills) {
		this.characterSkills = characterSkills;
	}
	public GBFCharacterSkills getSupportSkills() {
		return supportSkills;
	}
	public void setSupportSkills(GBFCharacterSkills supportSkills) {
		this.supportSkills = supportSkills;
	}
	

	
	
}
