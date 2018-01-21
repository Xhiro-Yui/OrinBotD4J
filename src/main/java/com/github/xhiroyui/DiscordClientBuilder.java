package com.github.xhiroyui;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

public class DiscordClientBuilder {
	
    public IDiscordClient buildClient(String token) { // Returns a new instance of the Discord client
    	ClientBuilder clientBuilder = new ClientBuilder(); // Creates the ClientBuilder instance
        clientBuilder.withToken(token); // Adds the login info to the builder
        try {
        	return clientBuilder.build(); //
        } catch (DiscordException e) { // This is thrown if there was a problem building the client
            e.printStackTrace();
            return null;
        }
    }

	public void loginClient(IDiscordClient client) { client.login(); }
    
}