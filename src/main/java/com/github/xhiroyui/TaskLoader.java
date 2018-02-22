package com.github.xhiroyui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.mutable.MutableBoolean;

import com.github.xhiroyui.constant.BotConstant;
import com.github.xhiroyui.tasks.ChannelMonitor;
import com.github.xhiroyui.tasks.ITask;
import com.github.xhiroyui.util.DBConnection;

public class TaskLoader {
	
	private static TaskLoader taskLoader;
	private HashMap<ITask, MutableBoolean> taskList = new HashMap<ITask, MutableBoolean>();
	
	public static TaskLoader getTaskLoader() {
		if (taskLoader == null)
			taskLoader = new TaskLoader();
		return taskLoader;
	}

	public void initTasks() {
		// Channel Monitors
		ArrayList<Long> channelList = DBConnection.getDBConnection().getChannelsWithFlags(BotConstant.FUNC_FLAG_LIMITED);
		for (Long each : channelList) {
			taskList.put(new ChannelMonitor(each), new MutableBoolean(true));
		}
		
		// Other type of tasks below (currently none)
		
	}
	
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
	

	// Only called when Bot is booted up
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
	
	public void addMonitor(long channelID) {
		ChannelMonitor newChannelMonitor = new ChannelMonitor(channelID);
		taskList.put(newChannelMonitor, new MutableBoolean(true));
		enableTask(newChannelMonitor);
	}
	
	
}

