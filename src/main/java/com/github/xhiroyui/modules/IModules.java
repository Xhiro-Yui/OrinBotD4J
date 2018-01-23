package com.github.xhiroyui.modules;

public interface IModules {

	boolean enable();
	void disable();
	String getName();
	String getAuthor();
	String getVersion();
	String getMinimumDiscord4JVersion();
	String getModuleName();
}
