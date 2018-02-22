package com.github.xhiroyui.modules;

import java.util.ArrayList;

import com.github.xhiroyui.DiscordClient;
import com.github.xhiroyui.util.Command;

import sx.blah.discord.api.IDiscordClient;

public class GBFCommands implements IModuleExtended{

    private String moduleName = "GBFCommands";
    private String moduleVersion = "1.0";
    private String moduleMinimumVersion = "2.3.0";
    private String author = "Xhiro Yui / Rhestia";
    GBFCommandsHandler gbfCommandsHandler = new GBFCommandsHandler();

	public void disable() {
		DiscordClient.getClient().getDispatcher().registerListener(gbfCommandsHandler);
    }
	
	@Override
    public boolean enable(IDiscordClient client) {
		client.getDispatcher().registerListener(gbfCommandsHandler);
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

    public ArrayList<Command> getModuleCommands() {
    	return gbfCommandsHandler.getModuleCommands();
    }
}