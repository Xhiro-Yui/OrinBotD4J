package com.github.xhiroyui.bean;

import java.util.ArrayList;

public class GBFCharacterSkills {
	
	private String type;
	private ArrayList<String> skill1 = new ArrayList<String>();
	private ArrayList<String> skill2 = new ArrayList<String>();
	private ArrayList<String> skill3 = new ArrayList<String>();
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public ArrayList<String> getSkill1() {
		return skill1;
	}
	public void setSkill1(ArrayList<String> skill1) {
		this.skill1 = skill1;
	}
	public ArrayList<String> getSkill2() {
		return skill2;
	}
	public void setSkill2(ArrayList<String> skill2) {
		this.skill2 = skill2;
	}
	public ArrayList<String> getSkill3() {
		return skill3;
	}
	public void setSkill3(ArrayList<String> skill3) {
		this.skill3 = skill3;
	}
	
}
