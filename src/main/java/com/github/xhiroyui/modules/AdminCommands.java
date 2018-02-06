package com.github.xhiroyui.modules;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.modules.IModule;

public class AdminCommands implements IModule{

    private String moduleName = "AdminCommands";
    private String moduleVersion = "1.0";
    private String moduleMinimumVersion = "2.3.0";
    private String author = "Xhiro Yui / Rhestia";
    
    public void disable() {
        //Disabled. This module should never be disabled.
    }

	@Override
	public boolean enable(IDiscordClient client) {
		client.getDispatcher().registerListener(new AdminCommandsHandler());
		return false;
	}
	
    public String getAuthor() {
        return author;
    }

    public String getMinimumDiscord4JVersion() {
        return moduleMinimumVersion;
    }

    public String getName() {
        return moduleName;
    }

    public String getVersion() {
        return moduleVersion;
    }
}