package com.github.xhiroyui.modules;

import com.github.xhiroyui.util.UserWhitelist;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.modules.IModule;

public class AdminCommands implements IModule{

    private String moduleName = "AdminCommand";
    private String moduleVersion = "1.0";
    private String moduleMinimumVersion = "2.3.0";
    private String author = "Xhiro Yui / Rhestia";
    private String prefix = "!";
    public static IDiscordClient client;
    private UserWhitelist whitelist;
    
    public AdminCommands(UserWhitelist _whitelist) {
		whitelist = _whitelist;
	}

	public void disable() {
        
    }

    public boolean enable(IDiscordClient dclient) {
        client = dclient;
        EventDispatcher dispatcher = client.getDispatcher();
        dispatcher.registerListener(new AdminCommandHandler(this, whitelist));
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

}