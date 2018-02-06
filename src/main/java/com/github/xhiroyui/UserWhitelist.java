package com.github.xhiroyui;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class UserWhitelist {
	
	private static UserWhitelist userWhitelist;
	private ArrayList<String> whitelist = null;
	Gson gson;
	
	public static UserWhitelist getWhitelist() {
		if (userWhitelist == null) 
			userWhitelist = new UserWhitelist();
		return userWhitelist;
	}
	
	private UserWhitelist() {
		if (loadWhitelist()) {
			System.out.println("Whitelist loaded");
		} else {
			System.out.println("Creating new whitelist");
			whitelist = new ArrayList<String>();	
		}
	}

	public boolean addUserToWhitelist(String stringID) {
		try {
			if (validateUser(stringID)) {
				System.out.println("User already exists in whitelist");
				return false;
			} else {
				whitelist.add(stringID);
				saveWhitelist();
				return true;
			}
				
		} catch (Exception e) {
			return false;
		}
	}
	
	private boolean loadWhitelist() {
		gson = new Gson();
		try (BufferedReader br = new BufferedReader(new FileReader("whitelist.json"))) {
			whitelist = gson.fromJson(br, new TypeToken<ArrayList<String>>(){}.getType());
			System.out.println(whitelist);
		} catch (FileNotFoundException e) {
			System.out.println("No whitelist found");
			return false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
		return true;
	}

	private void saveWhitelist() {
		try (Writer writer = new FileWriter("whitelist.json")) {
			Gson gson = new GsonBuilder().create();
		    gson.toJson(whitelist, writer);
		} catch (Exception e) {
			
		}
	}
	
	public boolean validateUser(String userID) {
		for (String user : whitelist) {
			if (userID.contentEquals(user)) {
				return true;
			}
		}
		return false;
	}
}
