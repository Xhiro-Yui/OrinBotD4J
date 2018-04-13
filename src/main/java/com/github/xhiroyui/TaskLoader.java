package com.github.xhiroyui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.xhiroyui.constant.BotConstant;
import com.github.xhiroyui.tasks.OldChannelMonitor;
import com.github.xhiroyui.tasks.ITask;
import com.github.xhiroyui.tasks.Unmuter;
import com.github.xhiroyui.util.BotCache;
import com.github.xhiroyui.util.DBConnection;

public class TaskLoader {
	private static final Logger logger = LoggerFactory.getLogger(TaskLoader.class.getSimpleName());
	private static TaskLoader taskLoader;
	private HashMap<ITask, MutableBoolean> taskList = new HashMap<ITask, MutableBoolean>();
	
	public static TaskLoader getTaskLoader() {
		if (taskLoader == null) {
			logger.debug("Instantiating TaskLoader");
			taskLoader = new TaskLoader();
		}
		return taskLoader;
	}

	// Only called when Bot is booted up
	public void initTasks() {
		// Channel Monitors
		ArrayList<String> channelList = DBConnection.getDBConnection().selectQuerySingleColumnMultipleResults("SELECT DISTINCT channel_id FROM " + BotConstant.DB_CHANNEL_FLAGS_TABLE);
		if (channelList == null) {
			
		} else {
			for (String each : channelList) {
				if (OrinBot.db_cstring == null) // Means deployment
					taskList.put(new OldChannelMonitor(Long.parseLong(each)), new MutableBoolean(true));
			}
		}

		// Unmuter
		if (BotCache.refreshMutedUsersCache() > 0)
			refreshUnmuter();
		
		// Other type of tasks below (currently none)
		enableAllTasks();
	}
	
	// Not used atm, redundant. Will remove if no further use of it is found
	public void updateTask(Long channelID, boolean flag) {
		for (Map.Entry<ITask, MutableBoolean> entry : taskList.entrySet()) {
			if (channelID.compareTo(entry.getKey().getChannelID()) == 0) {
				entry.getValue().setValue(flag);
				if (flag == true) 
					enableTask(entry.getKey());
				if (flag == false) 
					disableTask(entry.getKey());
			}
		}
		
	}
	

	public void enableAllTasks() {
		for (Map.Entry<ITask, MutableBoolean> entry : taskList.entrySet()) {
			if (entry.getValue().isTrue())
				DiscordClient.getClient().getDispatcher().registerListener(entry.getKey());
		}
	}
	
	public void enableTask(ITask task) {
		DiscordClient.getClient().getDispatcher().registerListener(task);		
	}
	
	public void disableTask(ITask task) {
		DiscordClient.getClient().getDispatcher().unregisterListener(task);		
	}

	// Task specific functions
	// Channel Monitors!
	public boolean checkMonitors(Long channelID) {
		for (ITask entry : taskList.keySet()) {
			if (channelID.compareTo(entry.getChannelID()) == 0) {
				return true; // A monitor exists for this channel
			}
		}
		return false;
	}
	
	public ArrayList<ITask> getAllChannelMonitors() {
		ArrayList<ITask> monitorList = new ArrayList<ITask>();		
		for (ITask entry : taskList.keySet()) {
			monitorList.add(entry);
		}
		return monitorList;
	}
	
	public void addMonitor(long channelID) {
		OldChannelMonitor newChannelMonitor = new OldChannelMonitor(channelID);
		taskList.put(newChannelMonitor, new MutableBoolean(true));
		enableTask(newChannelMonitor);
	}
	
	public void removeMonitor(Long channelID) {
		ITask placeholder = null;
		for (ITask entry : taskList.keySet()) {
			if (channelID.compareTo(entry.getChannelID()) == 0) {
				placeholder = entry;
			}
		}
		disableTask(placeholder);
		taskList.remove(placeholder);
		System.out.println("Monitor for channel " + channelID + " has been shut down.");
	}
	
	public ArrayList<String> getMonitorFlags(Long channelID) {
		for (ITask entry : taskList.keySet()) {
			if (channelID.compareTo(entry.getChannelID()) == 0) {
				return ((OldChannelMonitor)entry).getMonitorFlags();
			}
		}
		return null;
	}
	
	public ArrayList<String> getMonitorSettings(Long channelID) {
		for (ITask entry : taskList.keySet()) {
			if (channelID.compareTo(entry.getChannelID()) == 0) {
				return entry.getSettings();
			}
		}
		return null;
	}
	
	public ITask getMonitor(Long channelID) {
		for (ITask entry : taskList.keySet()) {
			if (channelID.compareTo(entry.getChannelID()) == 0) {
				return entry;
			}
		}
		return null;
	}
	
	public void refreshMonitorSettings(Long channelID) {
		for (ITask entry : taskList.keySet()) {
			if (channelID.compareTo(entry.getChannelID()) == 0) {
				entry.refreshSettings();
			}
		}
	}
	
	// Unmuter call
	public void refreshUnmuter() {
		if (BotCache.refreshMutedUsersCache() > 0)
			Unmuter.getUnmuter().refreshSettings();
	}
}

