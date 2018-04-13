package com.github.xhiroyui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

public class DiscordClient {
	private static final Logger logger = LoggerFactory.getLogger(DiscordClient.class.getSimpleName());
	private static IDiscordClient client;

	private DiscordClient() {}
	
	public static IDiscordClient getClient() {
		return client;
	}
	
	public static void buildClient(String token) {
		 ClientBuilder clientBuilder = new ClientBuilder();
		 clientBuilder.withToken(token);
		 try {
			 logger.debug("Building DiscordClient.");
			 client = clientBuilder.build();
			 logger.debug("DiscordClient built successfully");
		 } catch (DiscordException e) {
			 logger.error("Failed to build Discord Client");
			 e.printStackTrace();
		 }
	}
	
	public static void login() {		
		try {
			client.login();
			logger.info("Orin has logged in to Discord successfully nyahaha");
		} catch (Exception e) {
			logger.error("Discord client failed to login");
			e.printStackTrace();
		}
		
	}
}