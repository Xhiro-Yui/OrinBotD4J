package com.github.xhiroyui.tasks;

import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.github.xhiroyui.DiscordClient;
import com.github.xhiroyui.bean.MutedUser;
import com.github.xhiroyui.constant.BotConstant;
import com.github.xhiroyui.util.BotCache;
import com.github.xhiroyui.util.DBConnection;

import sx.blah.discord.util.RequestBuffer;

public class Unmuter implements ITask {
	private ScheduledExecutorService unmuterService = null;

	private static Unmuter unmuter;

	private Unmuter() {
	}

	public static Unmuter getUnmuter() {
		if (unmuter == null)
			unmuter = new Unmuter();
		return unmuter;
	}

	private void startUnmuter() {
		System.out.println("Initializing Unmuter.");
		unmuterService = Executors.newScheduledThreadPool(0);
		unmuterService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					for (MutedUser user : BotCache.mutedUsersCache) {
						if (Instant.now().compareTo(Instant.ofEpochMilli(user.getTimeStamp())) > 0) {
							DiscordClient.getClient().fetchUser(user.getUserID()).removeRole(DiscordClient.getClient()
									.getRoleByID(BotCache.mutedRoleIDCache.get(user.getGuildID())));
							DBConnection.getDBConnection().deleteQuery(
									"DELETE FROM " + BotConstant.DB_MUTED_USERS_TABLE + " WHERE guild_id = ? AND user_id = ?",
									user.getGuildID(), user.getUserID());
							RequestBuffer.request(() -> DiscordClient.getClient()
									.getOrCreatePMChannel(DiscordClient.getClient().fetchUser(user.getUserID()))
									.sendMessage("You have been unmuted in "
											+ DiscordClient.getClient().getGuildByID(user.getGuildID()).getName()
											+ "."));
							Long logChannelID = BotCache.guildLogChannelIDCache.get(user.getGuildID());
							if (logChannelID.compareTo(0L) != 0)
								RequestBuffer.request(() -> DiscordClient.getClient().getChannelByID(logChannelID)
										.sendMessage(DiscordClient.getClient().fetchUser(user.getUserID()).mention()
												+ " has been unmuted."));
						}

					}
					if (BotCache.refreshMutedUsersCache() == 0)
						shutdown();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 1, 1 * 60, TimeUnit.SECONDS); // Runs every 1 minutes
	}

	@Override
	public void refreshSettings() {
		if (BotCache.refreshMutedUsersCache() > 0)
			startUnmuter();
		else
			shutdown();
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
