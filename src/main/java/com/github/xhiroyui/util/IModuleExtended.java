package com.github.xhiroyui.util;

import java.util.ArrayList;

import sx.blah.discord.modules.IModule;

public interface IModuleExtended extends IModule {
	
	public ArrayList<Command> getModuleCommands();

}
