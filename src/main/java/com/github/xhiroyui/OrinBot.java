package com.github.xhiroyui;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.xhiroyui.util.BotCache;

public class OrinBot {
	private static final Logger logger = LoggerFactory.getLogger(OrinBot.class.getSimpleName());
	public static String db_cstring; // For testing
	public static String db_userid; // For testing
	public static String db_pw; // For testing
	public static RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();
	
	
	public static void main(String[] args) {
		logger.info("Orin has been revived nya! Orin is now preparing herself :3");
		String token = System.getenv("TOKEN");
		if (!(token == null)){
			logger.debug("Discord token obtained from environment variable");
			DiscordClient.buildClient(token);
		}
		else {
			logger.debug("Discord token obtained run command");
			DiscordClient.buildClient(args[0]);
			db_cstring = args[1]; // For testing
			db_userid = args[2]; // For testing
			db_pw = args[3]; // For testing
		}
		
		ModuleLoader.getModuleLoader().initModules();
		ModuleLoader.getModuleLoader().enableModules();
		logger.debug("Command modules loaded successfully.");
		
		logger.debug("Logging in to Discord.");
		DiscordClient.login();
		
		logger.debug("Initializing extra modules");
		BotCache.refreshMutedUsersCache();
		TaskLoader.getTaskLoader().initTasks();
	}
}
