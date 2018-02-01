package com.github.xhiroyui.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableBoolean;

import com.github.xhiroyui.modules.IModules;

public class ModuleList {
	private HashMap<IModules, MutableBoolean> moduleList = new HashMap<IModules, MutableBoolean>();

	public void insertModule(IModules module) {
		moduleList.put(module, new MutableBoolean(true));
	}

	public ArrayList<String> getAllModules() {
		ArrayList<String> modules = new ArrayList<String>();
		for (Map.Entry<IModules, MutableBoolean> entry : moduleList.entrySet()) {
			if (entry.getValue().isTrue())
				modules.add(entry.getKey().getModuleName() + " | Enabled");
			else
				modules.add(entry.getKey().getModuleName() + " | Disabled");
		}
		return modules;
	}
	
	public void enableModules() {
		for (Map.Entry<IModules, MutableBoolean> entry : moduleList.entrySet()) {
			if (entry.getValue().isTrue())
				entry.getKey().enable();
		}
	}
	
}
