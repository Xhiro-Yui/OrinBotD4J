package com.github.xhiroyui;

import com.github.xhiroyui.util.BotCache;

public class OrinBot {
	public static String db_cstring; // For testing
	public static String db_userid; // For testing
	public static String db_pw; // For testing
	
	public static void main(String[] args) {
		String token = System.getenv("TOKEN");
		if (!(token == null)){
			DiscordClient.buildClient(token);
		}
		else {
			DiscordClient.buildClient(args[0]);
			db_cstring = args[1]; // For testing
			db_userid = args[2]; // For testing
			db_pw = args[3]; // For testing
		}
		
		
		ModuleLoader.getModuleLoader().initModules();
		ModuleLoader.getModuleLoader().enableModules();
		
		DiscordClient.getClient().login();
		BotCache.refreshMutedUsersCache();
		TaskLoader.getTaskLoader().initTasks();
	}
}
