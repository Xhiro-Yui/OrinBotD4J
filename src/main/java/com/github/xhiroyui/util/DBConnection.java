package com.github.xhiroyui.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.github.xhiroyui.OrinBot;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DBConnection {
	private static DBConnection dbConnection;
	
	private static HikariDataSource hikariDS;
	
	private DBConnection() {
		if (hikariDS == null) {
			HikariConfig config = new HikariConfig();
			String cstring = System.getenv("CSTRING");
			String userid = System.getenv("USERID");
			String pw = System.getenv("PW");
			if (!(cstring == null || userid == null || pw == null)){
				config.setJdbcUrl(cstring);
				config.setUsername(userid);
				config.setPassword(pw);
			}
			else {
				config.setJdbcUrl(OrinBot.db_cstring);
				config.setUsername(OrinBot.db_userid);
				config.setPassword(OrinBot.db_pw);
			}
			config.addDataSourceProperty("cachePrepStmts", true);
			config.addDataSourceProperty("prepStmtCacheSize", "250");
			config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
			config.setMaximumPoolSize(3);
			hikariDS = new HikariDataSource(config);
		}
	}
	
	public static DBConnection getDBConnection() {
		if (dbConnection == null) 
			dbConnection = new DBConnection();
		return dbConnection;
	}
	
	public void insertQuery(String sql) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = hikariDS.getConnection();
			statement = connection.prepareStatement(sql);
			statement.execute();
		} catch (Exception e) {
			System.out.println("Error executing SQL statement : " + sql);
		} finally {
			try {
				statement.close();
			} catch (Exception e) {
			}
			try {
				connection.close();
			} catch (Exception e) {
			}
		}
		
	}
	
	public void updateQuery(String sql) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = hikariDS.getConnection();
			statement = connection.prepareStatement(sql);
			statement.execute();
		} catch (Exception e) {
			System.out.println("Error executing SQL statement : " + sql);
		} finally {
			try {
				statement.close();
			} catch (Exception e) {
			}
			try {
				connection.close();
			} catch (Exception e) {
			}
		}
		
	}
	
	public ArrayList<String> refreshChannelMonitorSettings(String sql) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		ArrayList<String> parameters = new ArrayList<String>();
		try {
			connection = hikariDS.getConnection();
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			rs.first();
			parameters.add(rs.getString("flags"));
			parameters.add(rs.getString("post_amount"));
			parameters.add(rs.getString("duration"));
		} catch (Exception e) {
			System.out.println("Refresh channel monitor settings error | " + e.getMessage());
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
			try {
				statement.close();
			} catch (Exception e) {
			}
			try {
				connection.close();
			} catch (Exception e) {
			}
		}
		return parameters;	
	}
	
	public int channelPostCount(long userID, long channelID) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		int postCount = -1;
		try {
			connection = hikariDS.getConnection();
			statement = connection.prepareStatement("select count(post_id) as count from channel_monitor where user_id = '" + userID + "' and channel_id = '" + channelID + "'");
			rs = statement.executeQuery();
			rs.first();
			postCount = rs.getInt("count");
		} catch (Exception e) {
			
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
			try {
				statement.close();
			} catch (Exception e) {
			}
			try {
				connection.close();
			} catch (Exception e) {
			}
		}
		return postCount;
	}
	
	public long getOldestPost(long userID, long channelID) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		String sql = "select * from channel_monitor where user_id = '" + userID + "' and channel_id = '" + channelID + "' order by 'datetime_of_post' ASC LIMIT 1";
		long postID = -1;
		try {
			connection = hikariDS.getConnection();
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			rs.first();
			postID = rs.getLong("post_id");
		} catch (Exception e) {

		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
			try {
				statement.close();
			} catch (Exception e) {
			}
			try {
				connection.close();
			} catch (Exception e) {
			}
		}
		return postID;
	}
	
	public ArrayList<Long> getChannelsWithFlags(String flag) {
		ArrayList<Long> channels = new ArrayList<Long>();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		String sql = "select * from channel_flags where flags = '" + flag + "'";
		try {
			connection = hikariDS.getConnection();
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();		
			while (rs.next()) {
				channels.add(Long.parseLong(rs.getString("channel_id")));
			}
			
		} catch (Exception e) {

		} finally {
			try {
				rs.close();
			} catch (Exception e) {
			}
			try {
				statement.close();
			} catch (Exception e) {
			}
			try {
				connection.close();
			} catch (Exception e) {
			}
		}
		return channels;
	}
	
	
}