package com.github.xhiroyui;

import sx.blah.discord.api.IDiscordClient;

public class OrinBot {
	
	public static void main(String[] args) {
		IDiscordClient client;
		String token = System.getenv("TOKEN");
		if (!(token == null)){
			client = new DiscordClientBuilder().buildClient(token);
		}
		else {
			client = new DiscordClientBuilder().buildClient(args[0]);
		}
		ModuleLoader modLoader = new ModuleLoader(client);
		
		modLoader.loadModules();
		modLoader.enableModules();
		
		client.login();
	}
}
