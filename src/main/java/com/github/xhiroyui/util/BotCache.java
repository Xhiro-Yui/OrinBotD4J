package com.github.xhiroyui.util;

import java.util.ArrayList;

import com.github.xhiroyui.bean.MutedUser;
import com.github.xhiroyui.constant.BotConstant;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class BotCache {

	// Cache for the role ID for the bot to mute users
	public static LoadingCache<Long, Long> mutedRoleIDCache = CacheBuilder.newBuilder().recordStats().maximumSize(100)
			.build(new CacheLoader<Long, Long>() {
				public Long load(Long guildID) throws Exception {
					System.out.println("Muted Role ID not found for current guild. Obtaining from DB");
					return Long.parseLong(DBConnection.getDBConnection().selectQuerySingleResult(
							"SELECT role_id FROM " + BotConstant.DB_GUILD_MUTE_TABLE + " WHERE guild_id = ?", guildID));
				}
			});

	// Cache guild log channel IDs
	public static LoadingCache<Long, Long> guildLogChannelIDCache = CacheBuilder.newBuilder().recordStats()
			.maximumSize(100).build(new CacheLoader<Long, Long>() {
				public Long load(Long guildID) throws Exception {
					System.out.println("Guild log channel ID not found. Obtaining from DB");
					String channelID = DBConnection.getDBConnection().selectQuerySingleResult(
							"SELECT channel_id FROM " + BotConstant.DB_GUILD_LOG_CHANNEL_TABLE + " WHERE guild_id = ?",
							guildID);
					if (channelID == null)
						return 0L;
					else
						return Long.parseLong(channelID);
				}
			});

	// Cache of muted users by the bot to automatically remove the mute by a
	// scheduled task
	public static ArrayList<MutedUser> mutedUsersCache;

	// Method call to populate/refresh the mutedUsersCache. Called once upon
	// booting.
	public static int refreshMutedUsersCache() {
		mutedUsersCache = new ArrayList<MutedUser>();
		ArrayList<String[]> temp = DBConnection.getDBConnection()
				.selectQueryMultipleColumnMultipleResults("SELECT * FROM " + BotConstant.DB_MUTED_USERS_TABLE);
		if (temp != null) {
			for (String[] each : temp) {
				MutedUser user = new MutedUser();
				user.setUserID(Long.parseLong(each[1]));
				user.setGuildID(Long.parseLong(each[2]));
				user.setTimeStamp(Long.parseLong(each[3]));
				mutedUsersCache.add(user);
			}
		}
		return mutedUsersCache.size();
	}

}
