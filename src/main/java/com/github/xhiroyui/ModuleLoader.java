package com.github.xhiroyui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableBoolean;

import com.github.xhiroyui.modules.AdminCommands;
import com.github.xhiroyui.modules.GBFCommands;
import com.github.xhiroyui.modules.GeneralCommands;

import sx.blah.discord.modules.IModule;

public class ModuleLoader {
	
	private static ModuleLoader moduleLoader;
	private HashMap<IModule, MutableBoolean> moduleList = new HashMap<IModule, MutableBoolean>();
	
	public static ModuleLoader getModuleLoader() {
		if (moduleLoader == null)
			moduleLoader = new ModuleLoader();
		return moduleLoader;
	}

	public void initModules() {
		moduleList.put(new AdminCommands(), new MutableBoolean(true));
		moduleList.put(new GeneralCommands(), new MutableBoolean(true));
		moduleList.put(new GBFCommands(), new MutableBoolean(true));
	}
	
	public ArrayList<String> getAllModules() {
		ArrayList<String> modules = new ArrayList<String>();
		for (Map.Entry<IModule, MutableBoolean> entry : moduleList.entrySet()) {
			if (entry.getValue().isTrue())
				modules.add(entry.getKey().getName() + " | Enabled");
			else
				modules.add(entry.getKey().getName() + " | Disabled");
		}
		return modules;
	}
	
	public void enableModules() {
		for (Map.Entry<IModule, MutableBoolean> entry : moduleList.entrySet()) {
			if (entry.getValue().isTrue())
				entry.getKey().enable(DiscordClient.getClient());
		}
	}
	
}

