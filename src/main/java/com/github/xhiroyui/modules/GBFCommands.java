package com.github.xhiroyui.modules;

import sx.blah.discord.api.IDiscordClient;

public class GBFCommands implements IModules{

    private String moduleName = "GBFCommands";
    private String moduleVersion = "1.0";
    private String moduleMinimumVersion = "2.3.0";
    private String author = "Xhiro Yui / Rhestia";
    private String prefix = "!";
    public static IDiscordClient client;
    
    public GBFCommands(IDiscordClient _client) {
		client = _client;
	}

	public void disable() {
        //Disabled. This module should never be disabled.
    }

    public boolean enable() {
        client.getDispatcher().registerListener(new GBFCommandHandler(this));
        return true;
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

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
}