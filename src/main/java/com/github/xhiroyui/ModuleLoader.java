package com.github.xhiroyui;

import java.util.HashMap;

import com.github.xhiroyui.modules.AdminCommands;
import com.github.xhiroyui.modules.BullyCommands;
import com.github.xhiroyui.modules.GBFCommands;
import com.github.xhiroyui.modules.IModules;
import com.github.xhiroyui.util.UserWhitelist;

import sx.blah.discord.api.IDiscordClient;

public class ModuleLoader {

	private IDiscordClient client;
	private UserWhitelist whitelist = new UserWhitelist();
//	private ArrayList<IModules> moduleList = new ArrayList<IModules>();
	private HashMap<IModules, Boolean> moduleList = new HashMap<IModules, Boolean>();
	
	public ModuleLoader(IDiscordClient _client) {
		client = _client;
	}

	public void loadMandatoryModules() {
		new AdminCommands(whitelist, moduleList, client).enable();
		new GBFCommands(client).enable();
	}
	
	public void loadModules() {
//		moduleList.add(new AdminCommands(whitelist));
//		moduleList.add(new BullyCommands(whitelist));
	}
	
//	public void enableModules() {
//		for (int i = 0; i < moduleList.size(); i++) {
//			(moduleList.get(i)).enable();
//		}
//	}
	
	public void initializeModules() {
		moduleList.put(new BullyCommands(whitelist), false);
	}
}

