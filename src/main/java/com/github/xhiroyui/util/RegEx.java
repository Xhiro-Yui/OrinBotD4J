package com.github.xhiroyui.util;

import java.util.regex.Pattern;

public class RegEx {
	// Used to extract contents from GBFWiki Json -> join = {{ }}
	public final static Pattern joinPattern = Pattern.compile("\\[\\[([\\w\\s]+)", Pattern.MULTILINE); //Original  -> \[\[([\w\s]+)
	
	// Used to extract contents which contain {{ }} in skills. Slightly different from joinPattern
	public final static Pattern curlyPattern = Pattern.compile("\\{\\{([\\w|\\{\\}=\\s]*)\\}\\}", Pattern.MULTILINE); //Original  -> {{([\w|{}=\s]*)}}
}
