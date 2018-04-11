package com.github.xhiroyui.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonElement;

public class MiscUtils {
	
	public static boolean isInteger(String s) {
		try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}
	
	public static boolean isLong(String s) {
		try { 
	        Long.parseLong(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}
	
	public static String gbfWikiTextParser(String s) {
		Matcher matcher;
		String finalString = "";
		Pattern pt = Pattern.compile("(.*?)\\{\\{(.*?)\\}\\}(.*)", Pattern.MULTILINE);
		matcher = pt.matcher(s);
		if (matcher.find()) {
			for (int i = 1; i < matcher.groupCount() + 1; i++) {
				if (i == 1)
					finalString += matcher.group(i);
				else if (i == 2)
					finalString += gbfWikiTextParserAux(matcher.group(i));
				else {
					finalString += gbfWikiTextParser(matcher.group(i));
				}
			}
		} 
		else
			finalString += s;
		return finalString;
	}
	
	private static String gbfWikiTextParserAux(String s) {
		String[] text = s.split("\\|");
		if (text[0].equalsIgnoreCase("status"))
			return text[1];
		else
			return s;
	}
	
	public static String getJsonString(JsonElement e) {
		return e.isJsonNull()? null : e.getAsString();
	}
}
