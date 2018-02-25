package com.github.xhiroyui.tasks;

import java.util.ArrayList;

//import sx.blah.discord.api.IDiscordClient;

public interface ITask {
//	boolean enable(IDiscordClient client);
//
//	void disable();
	
	public void refreshSettings();
	public long getChannelID();
	public ArrayList<String> getSettings();
	public void shutdown();
}
