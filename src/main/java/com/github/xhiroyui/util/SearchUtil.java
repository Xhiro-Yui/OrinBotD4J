package com.github.xhiroyui.util;

public class SearchUtil {
	
	public String gbfWikiSearch(String[] query) {
		String searchUrl = "https://gbf.wiki/index.php?search=";
		for (int i = 0; i < query.length ; i++) {
			if (i == 0) {
				searchUrl = searchUrl + query[i];
			}
			searchUrl = searchUrl + "+" + query[i];
		}
		return searchUrl;
	}
}
