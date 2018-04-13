package com.github.xhiroyui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.xhiroyui.modules.AdminCommands;
import com.github.xhiroyui.modules.GBFCommands;
import com.github.xhiroyui.modules.GeneralCommands;
import com.github.xhiroyui.modules.IModuleExtended;
import com.github.xhiroyui.modules.MALCommands;
import com.github.xhiroyui.modules.ModerationCommands;
import com.github.xhiroyui.modules.OwnerCommands;
import com.github.xhiroyui.util.Command;

public class ModuleLoader {
	private static final Logger logger = LoggerFactory.getLogger(ModuleLoader.class.getSimpleName());
	
	private static ModuleLoader moduleLoader;
	private HashMap<IModuleExtended, MutableBoolean> moduleList = new HashMap<IModuleExtended, MutableBoolean>();
	
	public static ModuleLoader getModuleLoader() {
		if (moduleLoader == null) {
			logger.debug("Instantiating ModuleLoader singleton");
			moduleLoader = new ModuleLoader();
		}
		return moduleLoader;
	}

	public void initModules() {
		logger.debug("Initializing command modules");
		moduleList.put(new OwnerCommands(), new MutableBoolean(true));
		moduleList.put(new AdminCommands(), new MutableBoolean(true));
		moduleList.put(new ModerationCommands(), new MutableBoolean(true));
		moduleList.put(new GeneralCommands(), new MutableBoolean(true));
		moduleList.put(new GBFCommands(), new MutableBoolean(true));
		moduleList.put(new MALCommands(), new MutableBoolean(true));
		
	}
	
	public ArrayList<String> getAllModules() {
		ArrayList<String> modules = new ArrayList<String>();
		for (Map.Entry<IModuleExtended, MutableBoolean> entry : moduleList.entrySet()) {
			if (entry.getValue().isTrue())
				modules.add(entry.getKey().getName() + " | Enabled");
			else
				modules.add(entry.getKey().getName() + " | Disabled");
		}
		return modules;
	}
	
	public void enableModules() {
		for (Map.Entry<IModuleExtended, MutableBoolean> entry : moduleList.entrySet()) {
			if (entry.getValue().isTrue())
				entry.getKey().enable(DiscordClient.getClient());
		}
	}
	
	public HashMap<String, ArrayList<Command>> getModuleCommands() {
		HashMap<String, ArrayList<Command>> moduleCommands = new HashMap<String, ArrayList<Command>>();
		for (IModuleExtended modules : moduleList.keySet()) {
			ArrayList<Command> command = new ArrayList<Command>();
			for ( Command commands : modules.getModuleCommands() ) {
				command.add(commands);
			}
			moduleCommands.put(modules.getName(), command);
		}
		return moduleCommands;
	}
	
}

