package com.github.xhiroyui;

import com.github.xhiroyui.util.UserWhitelist;

import sx.blah.discord.api.IDiscordClient;

public class OrinBot {
	
	public static void main(String[] args) {
		IDiscordClient client = new DiscordClientBuilder().buildClient(args[0]);
		ModuleLoader modLoader = new ModuleLoader();
		modLoader.loadModules();
		modLoader.enableModules(client);
		
		client.login();
	}
}
