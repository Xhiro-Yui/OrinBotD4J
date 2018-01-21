package com.github.xhiroyui.util;

import java.util.ArrayList;

public class Command {
	private String commandName;
	private String commandDescription;
	private ArrayList<String> commandCallers = new ArrayList<String>();
	private final String commandCode;
	private int maximumArgs;
	private String[] example;

	public Command(String commandCode) {
		this.commandCode = commandCode;
	}
	
	public String getCommandName() {
		return commandName;
	}
	
	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}

	public String getCommandDescription() {
		return commandDescription;
	}

	public void setCommandDescription(String commandDescription) {
		this.commandDescription = commandDescription;
	}

	public ArrayList<String> getCommandCallers() {
		return commandCallers;
	}

	public void setCommandCallers(ArrayList<String> commandCallers) {
		this.commandCallers = commandCallers;
	}

	public String getCommandCode() {
		return commandCode;
	}

	public int getMaximumArgs() {
		return maximumArgs;
	}

	public void setMaximumArgs(int maximumArgs) {
		this.maximumArgs = maximumArgs;
	}

	public String[] getExample() {
		return example;
	}

	public void setExample(String[] example) {
		this.example = example;
	}
}
