package com.github.xhiroyui;

import sx.blah.discord.api.IDiscordClient;

public class OrinBot {
	
	public static void main(String[] args) {
		IDiscordClient client;
		try {
//			For heroku deployment? Not sure if it works
			 client = new DiscordClientBuilder().buildClient(System.getenv("TOKEN"));
		}
		catch (Exception e) {
			client = new DiscordClientBuilder().buildClient(args[0]);
		}
		
		ModuleLoader modLoader = new ModuleLoader(client);
		
		modLoader.loadMandatoryModules();
		modLoader.initializeModules();
//		modLoader.loadModules();
//		modLoader.enableModules();
		
		client.login();
	}
}
