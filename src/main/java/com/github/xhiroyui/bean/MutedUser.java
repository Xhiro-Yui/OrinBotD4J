package com.github.xhiroyui.bean;

public class MutedUser {
	private Long userID;
	private Long guildID;
	private String timeStamp;
	
	public Long getUserID() {
		return userID;
	}
	public void setUserID(Long userID) {
		this.userID = userID;
	}
	public Long getGuildID() {
		return guildID;
	}
	public void setGuildID(Long guildID) {
		this.guildID = guildID;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	@Override
	public String toString() {
		return "User " + userID + " is muted until " + timeStamp;
	}
}
