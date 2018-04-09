package com.github.xhiroyui.util;

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
	
	public static String getJsonString(JsonElement e) {
		return e.isJsonNull()? null : e.getAsString();
	}
}
