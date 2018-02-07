package com.github.xhiroyui.util;

import java.util.ArrayList;

import com.github.xhiroyui.constant.BotConstant;

public class Command {
	private String commandName;
	private String commandDescription;
	private ArrayList<String> commandCallers = new ArrayList<String>();
	private final String commandCode;
	private int maximumArgs;
	private ArrayList<String[]> params = new ArrayList<String[]>();
	private StringBuilder example = new StringBuilder();

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
	
	public void setCommandCallers(String commandCallers) {
		this.commandCallers.add(commandCallers);
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

	public ArrayList<String[]> getParams() {
		return params;
	}
	
	public void setParams(String[] params) {
		this.params.add(params);
	}

	public StringBuilder getExample() {
		if (example.length() == 0) 
			 this.example.append(BotConstant.PREFIX).append(this.commandCallers.get(0)).append(" ");
		return example;
	}

	public void setExample(String example) {
		if (this.example.length() == 0) 
			 this.example.append(BotConstant.PREFIX).append(this.commandCallers.get(0)).append(" ");
		this.example.append(example);
	}

}
