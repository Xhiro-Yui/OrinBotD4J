package com.github.xhiroyui;

import java.util.ArrayList;

import com.github.xhiroyui.modules.AdminCommands;
import com.github.xhiroyui.modules.BullyCommands;
import com.github.xhiroyui.util.UserWhitelist;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.modules.IModule;

public class ModuleLoader {

	UserWhitelist whitelist = new UserWhitelist();
	ArrayList<Object> moduleList = new ArrayList<Object>();

	public void loadModules() {
		moduleList.add(new AdminCommands(whitelist));
		moduleList.add(new BullyCommands(whitelist));
	}
	
	public void enableModules(IDiscordClient client) {
		for (int i = 0; i < moduleList.size(); i++) {
			((IModule) moduleList.get(i)).enable(client);
		}
	}

}
