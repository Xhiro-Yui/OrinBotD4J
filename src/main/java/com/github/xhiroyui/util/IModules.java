package com.github.xhiroyui.util;

public interface IModules {

	boolean enable();
	void disable();
	String getName();
	String getAuthor();
	String getVersion();
	String getMinimumDiscord4JVersion();
	String getModuleName();
}
