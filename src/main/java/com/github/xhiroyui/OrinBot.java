<<<<<<< HEAD
package com.github.xhiroyui;

import sx.blah.discord.api.IDiscordClient;

public class OrinBot {
	
	public static void main(String[] args) {
		IDiscordClient client = new DiscordClientBuilder().buildClient(args[0]);
		ModuleLoader modLoader = new ModuleLoader(client);
		
		modLoader.loadMandatoryModules();
		modLoader.initializeModules();
//		modLoader.loadModules();
//		modLoader.enableModules();
		
		client.login();
	}
}
=======
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
>>>>>>> branch 'master' of https://github.com/Xhiro-Yui/OrinBotD4J
