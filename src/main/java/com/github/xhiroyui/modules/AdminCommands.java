package com.github.xhiroyui.modules;

import com.github.xhiroyui.util.ModuleList;
import com.github.xhiroyui.util.UserWhitelist;

import sx.blah.discord.api.IDiscordClient;

public class AdminCommands implements IModules{

    private String moduleName = "AdminCommands";
    private String moduleVersion = "1.0";
    private String moduleMinimumVersion = "2.3.0";
    private String author = "Xhiro Yui / Rhestia";
    private String prefix = "!";
    public static IDiscordClient client;
    private UserWhitelist whitelist;
    private ModuleList moduleList;
    
    public AdminCommands(UserWhitelist _whitelist, ModuleList _moduleList, IDiscordClient _client) {
		whitelist = _whitelist;
		client = _client;
		moduleList = _moduleList;
	}

	public void disable() {
        //Disabled. This module should never be disabled.
    }

    public boolean enable() {
        client.getDispatcher().registerListener(new AdminCommandsHandler(this, whitelist, moduleList));
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