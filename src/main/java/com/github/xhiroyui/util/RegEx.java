package com.github.xhiroyui.util;

import java.util.regex.Pattern;

public class RegEx {
	/*
	 * Class containing all RegEx used in OrinBot. 
	 * Base is the original RegEx before any escape characters
	 * Original is the RegEx after escaping special chars such as {}[] which causes Illegal Repitition error
	 */
	
	// Used to capture tooltips from weapons/summons in GBFWiki
	// Base -> (.*?){{(.*?)}}(.*)
	// Original -> (.*?)\{\{(.*?)\}\}(.*)
	public final static Pattern toolTipPattern = Pattern.compile("(.*?)\\{\\{(.*?)\\}\\}(.*)", Pattern.MULTILINE);
	
	
	
	// Used to extract contents from GBFWiki Json -> join = {{ }}
	public final static Pattern joinPattern = Pattern.compile("\\[\\[([\\w\\s]+)", Pattern.MULTILINE); // Original
	
	// Used to extract contents which contain {{ }} in skills. Slightly
	// different from joinPattern
	public final static Pattern curlyPattern = Pattern.compile("\\{\\{([\\w|\\{\\}=\\s]*)\\}\\}", Pattern.MULTILINE); // Original
																														// ->
																														// {{([\w|{}=\s]*)}}

	
}
