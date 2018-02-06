package com.github.xhiroyui;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

public class DiscordClient {
	
	private static IDiscordClient client;
	
	private DiscordClient() {}
	
	public static IDiscordClient getClient() {
		return client;
	}
	
	public static void buildClient(String token) {
		 ClientBuilder clientBuilder = new ClientBuilder();
		 clientBuilder.withToken(token);
		 try {
			 client = clientBuilder.build();
		 } catch (DiscordException e) {
			 e.printStackTrace();
		 }
	}
}