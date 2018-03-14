package com.github.xhiroyui.modules;

import java.util.ArrayList;

import com.github.xhiroyui.DiscordClient;
import com.github.xhiroyui.util.Command;

import sx.blah.discord.api.IDiscordClient;

public class MALCommands implements IModuleExtended{

    private String moduleName = "MALCommands";
    private String moduleVersion = "1.0";
    private String moduleMinimumVersion = "2.3.0";
    private String author = "Xhiro Yui / Rhestia";
    MALCommandsHandler malCommandsHandler = new MALCommandsHandler();

	public void disable() {
		DiscordClient.getClient().getDispatcher().registerListener(malCommandsHandler);
    }
	
	@Override
    public boolean enable(IDiscordClient client) {
		client.getDispatcher().registerListener(malCommandsHandler);
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
    	return malCommandsHandler.getModuleCommands();
    }
}