package com.github.xhiroyui.util;

import com.github.xhiroyui.constant.BotConstant;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

public class BotCache {
	public static LoadingCache<Long, Long> mutedRoleIDCache = CacheBuilder.newBuilder().recordStats().maximumSize(100)
			.build(new CacheLoader<Long, Long>() {
				public Long load(Long guildID) throws Exception { 
					System.out.println("Muted Role ID not found for current guild. Obtaining from DB");
					return Long.parseLong(DBConnection.getDBConnection().selectQuerySingleResult("SELECT role_id FROM " + BotConstant.DB_GUILD_MUTE_TABLE + " WHERE guild_id = '" + guildID + "'"));
					}
			});	
}
