package com.github.xhiroyui.bean;

import java.util.ArrayList;

public class MALSearch {
	private int status;
	private String flag;
	private ArrayList<String[]> results = new ArrayList<String[]>();
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public ArrayList<String[]> getResults() {
		return results;
	}
	public void setResults(ArrayList<String[]> results) {
		this.results = results;
	}
	
}
