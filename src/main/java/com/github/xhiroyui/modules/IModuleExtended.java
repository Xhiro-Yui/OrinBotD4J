package com.github.xhiroyui.modules;

import java.util.ArrayList;

import com.github.xhiroyui.util.Command;

import sx.blah.discord.modules.IModule;

public interface IModuleExtended extends IModule {
	
	public ArrayList<Command> getModuleCommands();

}
