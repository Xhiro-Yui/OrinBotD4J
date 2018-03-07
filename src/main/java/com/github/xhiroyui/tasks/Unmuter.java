package com.github.xhiroyui.tasks;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.github.xhiroyui.DiscordClient;
import com.github.xhiroyui.bean.MutedUser;
import com.github.xhiroyui.constant.BotConstant;
import com.github.xhiroyui.util.BotCache;
import com.github.xhiroyui.util.DBConnection;

public class Unmuter implements ITask {
	private ScheduledExecutorService unmuterService = null;

	public Unmuter() {
		System.out.println("Initializing Unmuter.");
		unmuterService = Executors.newScheduledThreadPool(0);
		unmuterService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					for (MutedUser user : BotCache.mutedUsersCache) {
						if (Integer.parseInt(user.getTimeStamp()) > 100) {
							DiscordClient.getClient().fetchUser(user.getUserID()).removeRole(DiscordClient.getClient()
									.getRoleByID(BotCache.mutedRoleIDCache.get(user.getGuildID())));
							DBConnection.getDBConnection().deleteQuery(
									"DELETE FROM " + BotConstant.DB_MUTED_USERS_TABLE + " WHERE guild_id = '"
											+ user.getGuildID() + "' AND user_id = '" + user.getUserID() + "'");
						}

					}
					// If current duration > past time -> unmute user
					// Evict user from cache & from DB
					// Automatically calls for shutdown when no other user left
					// in cache/db
					if (BotCache.refreshMutedUsersCache() == 0)
						shutdown();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 1, 5 * 60, TimeUnit.SECONDS); // Runs every 5 minutes
	}

	@Override
	public void refreshSettings() {
	}

	@Override
	public long getChannelID() {
		return 0;
	}

	@Override
	public ArrayList<String> getSettings() {
		return null;
	}

	@Override
	public void shutdown() {
		if (unmuterService != null) {
			System.out.println("Shutting down Unmuter.");
			unmuterService.shutdown();
			unmuterService = null;
		}
	}

}
