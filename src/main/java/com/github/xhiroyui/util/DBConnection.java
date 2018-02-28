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
				config.setMaximumPoolSize(8);
			}
			else {
				config.setJdbcUrl(OrinBot.db_cstring);
				config.setUsername(OrinBot.db_userid);
				config.setPassword(OrinBot.db_pw);
				config.setMaximumPoolSize(2);
			}
			config.addDataSourceProperty("cachePrepStmts", true);
			config.addDataSourceProperty("prepStmtCacheSize", "250");
			config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
			
			hikariDS = new HikariDataSource(config);
		}
	}
	
	public static DBConnection getDBConnection() {
		if (dbConnection == null) 
			dbConnection = new DBConnection();
		return dbConnection;
	}
	
	public String selectQuerySingleResult(String sql) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		String result = null;
		try {
			connection = hikariDS.getConnection();
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			if (!rs.isBeforeFirst()) {
				// Empty results
			}
			else {
				while (rs.next()) { 
					result = rs.getString(1);
				}
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
		return result;	
	}
	
	public ArrayList<String> selectQuerySingleColumnMultipleResults(String sql) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		ArrayList<String> result = null;
		try {
			connection = hikariDS.getConnection();
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			if (!rs.isBeforeFirst()) {
				// Empty results
			}
			else {
				result = new ArrayList<String>();
				while (rs.next()) { 
					result.add(rs.getString(1));
				}
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
		return result;	
	}
	
	public ArrayList<String[]> selectQueryMultipleColumnMultipleResults(String sql) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		ArrayList<String[]> result = null;
		try {
			connection = hikariDS.getConnection();
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			if (!rs.isBeforeFirst()) {
				// Empty results
			}
			else {
				result = new ArrayList<String[]>();
				int size = rs.getMetaData().getColumnCount();
				while (rs.next()) {
					String[] row = new String[size];
					for (int i = 1; i < size+1; i++) {
						row[i-1] = rs.getString(i);
					}
					result.add(row);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		return result;	
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
	
	// Its a copy of updateQuery, if I find no use of this will eventually remove it
	public void deleteQuery(String sql) {
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
}