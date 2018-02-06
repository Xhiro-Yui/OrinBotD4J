package com.github.xhiroyui;

public class OrinBot {
	public static void main(String[] args) {
		String token = System.getenv("TOKEN");
		if (!(token == null)){
			DiscordClient.buildClient(token);
		}
		else {
			DiscordClient.buildClient(args[0]);
		}
		ModuleLoader.getModuleLoader().initModules();
		ModuleLoader.getModuleLoader().enableModules();
		
		DiscordClient.getClient().login();
	}
}
