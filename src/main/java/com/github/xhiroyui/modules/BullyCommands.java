package com.github.xhiroyui.modules;

import com.github.xhiroyui.util.IModules;
import com.github.xhiroyui.util.UserWhitelist;

import sx.blah.discord.api.IDiscordClient;

public class BullyCommands implements IModules{

    private String moduleName = "BullyCommands";
    private String moduleVersion = "1.0";
    private String moduleMinimumVersion = "2.3.0";
    private String author = "Xhiro Yui / Rhestia";
    private String prefix = "$";
    public static IDiscordClient client;
    private UserWhitelist whitelist;
    
    public BullyCommands(UserWhitelist _whitelist) {
		whitelist = _whitelist;
	}

	public void disable() {
		client.getDispatcher().unregisterListener(new BullyCommandHandler(this, whitelist));
    }

    public boolean enable() {
        client.getDispatcher().registerListener(new BullyCommandHandler(this, whitelist));
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