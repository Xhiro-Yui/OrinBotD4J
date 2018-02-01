package com.github.xhiroyui;

import com.github.xhiroyui.modules.AdminCommands;
import com.github.xhiroyui.modules.GBFCommands;
import com.github.xhiroyui.modules.GeneralCommands;
import com.github.xhiroyui.util.ModuleList;
import com.github.xhiroyui.util.UserWhitelist;

import sx.blah.discord.api.IDiscordClient;

public class ModuleLoader {

	private IDiscordClient client;
	private UserWhitelist whitelist = new UserWhitelist();
	private ModuleList moduleList = new ModuleList();
	
	
	public ModuleLoader(IDiscordClient _client) {
		client = _client;
	}

	public void loadModules() {
		moduleList.insertModule(new AdminCommands(whitelist, moduleList, client));
		moduleList.insertModule(new GBFCommands(client));
		moduleList.insertModule(new GeneralCommands(client));
	}
	
	public void enableModules() {
		moduleList.enableModules();
	}
	
	
}

