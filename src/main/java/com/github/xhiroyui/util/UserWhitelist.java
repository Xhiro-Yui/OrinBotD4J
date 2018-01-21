package com.github.xhiroyui.util;

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
	ArrayList<String> whitelist = null;
	Gson gson;  
	public UserWhitelist() {
		if (loadWhitelist()) {
			System.out.print("Whitelist loaded");
		} else {
			System.out.print("Creating new whitelist");
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
	
	public boolean loadWhitelist() {
		Gson gson = new Gson();
		try (BufferedReader br = new BufferedReader(new FileReader("whitelist.json"))) {
			whitelist = gson.fromJson(br, new TypeToken<ArrayList<String>>(){}.getType());
			System.out.println(whitelist);
		} catch (FileNotFoundException e) {
			System.out.println("No whitelist found");
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		return true;
	}
	
	public ArrayList<String> getWhitelist() {
		return whitelist;
	}
	
	public void saveWhitelist() {
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
