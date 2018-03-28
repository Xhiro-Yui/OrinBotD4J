package com.github.xhiroyui.modules;

import java.io.IOException;
import java.util.ArrayList;

import com.github.xhiroyui.DiscordClient;
import com.github.xhiroyui.TaskLoader;
import com.github.xhiroyui.constant.FunctionConstant;
import com.github.xhiroyui.tasks.ITask;
import com.github.xhiroyui.util.Command;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IEmoji;
import sx.blah.discord.handle.obj.StatusType;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.Image;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.RequestBuffer;

public class OwnerCommandsHandler extends ModuleHandler {
	public OwnerCommandsHandler() {
		createCommands();
	}

	private void createCommands() {
		Command command;

		command = new Command(FunctionConstant.OWNER_KILL_REQUEST_BUFFER);
		command.setCommandName("Kill Request Buffer");
		command.setCommandDescription("Kills the request buffer");
		command.setCommandCallers("krb");
		command.setCommandCallers("kill");
		command.setMaximumArgs(0);
		commandList.add(command);

		command = new Command(FunctionConstant.OWNER_CHANNEL_MONITOR_LOOKUP);
		command.setCommandName("Monitor Lookup");
		command.setCommandDescription("Searches for all monitors being run by Orin.");
		command.setCommandCallers("monitorsearch");
		command.setMaximumArgs(0);
		commandList.add(command);

		command = new Command(FunctionConstant.OWNER_CHANGE_DISPLAY_PICTURE);
		command.setCommandName("Change Avatar");
		command.setCommandDescription("Changes the Bot's Display Picture");
		command.setCommandCallers("changedp");
		command.setCommandCallers("changeavatar");
		command.setParams(new String[] { "Image Type" });
		command.setMaximumArgs(1);
		commandList.add(command);

		command = new Command(FunctionConstant.OWNER_CHANGE_STATUS);
		command.setCommandName("Change Status");
		command.setCommandDescription("Changes the Bot's Status");
		command.setCommandCallers("changestatus");
		command.setParams(new String[] { "Status text" });
		command.setMaximumArgs(10);
		commandList.add(command);
		
		command = new Command(FunctionConstant.OWNER_EMOJI_STRING);
		command.setCommandName("Emoji String Printer");
		command.setCommandDescription("Prints all the emojis in string format");
		command.setCommandCallers("emojistring");
		command.setMaximumArgs(0);
		commandList.add(command);
		
		command = new Command(FunctionConstant.OWNER_EMBED_PING);
		command.setCommandName("Ping (Embed Version)");
		command.setCommandDescription("Ping Pong! (Embed Version)");
		command.getCommandCallers().add("ping2");
		command.getCommandCallers().add("pingembed");
		command.setMaximumArgs(0);
		commandList.add(command);

		// command = new Command(FunctionConstant.OWNER_ADD_TO_WL);
		// command.setCommandName("Add to Whitelist");
		// command.setCommandDescription("Adds a user to whitelist");
		// command.setCommandCallers("wl");
		// command.setCommandCallers("whitelist");
		// command.setParams(new String[] { "User ID", "@Mention" });
		// command.setMaximumArgs(1);
		// command.setExample("@Rhestia");
		// commandList.add(command);
		//
		// command = new Command(FunctionConstant.OWNER_REMOVE_FROM_WL);
		// command.setCommandName("Remove from Whitelist");
		// command.setCommandDescription("Removes a user from the whitelist");
		// command.setCommandCallers("removewl");
		// command.setCommandCallers("removewhitelist");
		// command.setParams(new String[] { "User ID", "@Mention" });
		// command.setMaximumArgs(1);
		// command.setExample("@Rhestia");
		// commandList.add(command);

	}

	@EventSubscriber
	public void OnMesageEvent(MessageReceivedEvent event)
			throws RateLimitException, DiscordException, MissingPermissionsException, IOException {
		String[] command = processCommand(event);
		if (command != null) {
			if (ownerCheck(event.getAuthor().getLongID())) {
				executeCommand(event, command);
			} else {
				sendMessage(event.getAuthor().getDisplayName(event.getGuild())
						+ " is not the owner and is unable to use the command.", event);
			}
		}
	}

	public void executeCommand(MessageReceivedEvent event, String[] command) {
		String commandCode = validateCommand(event, command);
		if (commandCode != null) {
			switch (commandCode) {
			case FunctionConstant.OWNER_KILL_REQUEST_BUFFER:
				try {
					sendMessage(Integer.toString(killRequestBuffer()) + " requests killed. Merciless ;)", event);
				} catch (Exception e) {
					throwError(FunctionConstant.OWNER_KILL_REQUEST_BUFFER, e, event);
				}
				break;
			case FunctionConstant.OWNER_CHANNEL_MONITOR_LOOKUP:
				try {
					lookForChannelMonitors(event);
				} catch (Exception e) {
					throwError(FunctionConstant.OWNER_CHANNEL_MONITOR_LOOKUP, e, event);
				}
				break;
			case FunctionConstant.OWNER_CHANGE_DISPLAY_PICTURE:
				try {
					changeDisplayPicture(command, event);
				} catch (Exception e) {
					throwError(FunctionConstant.OWNER_CHANGE_DISPLAY_PICTURE, e, event);
				}
				break;
			case FunctionConstant.OWNER_CHANGE_STATUS:
				try {
					changeStatus(command, event);
				} catch (Exception e) {
					throwError(FunctionConstant.OWNER_CHANGE_STATUS, e, event);
				}
				break;
			case FunctionConstant.OWNER_EMOJI_STRING:
				try {
					getEmojiString(event);
				} catch (Exception e) {
					throwError(FunctionConstant.OWNER_EMOJI_STRING, e, event);
				}
				break;
			case FunctionConstant.OWNER_EMBED_PING:
				try {
					embedPong(event);
				} catch (Exception e) {
					throwError(FunctionConstant.OWNER_EMBED_PING, e, event);
				}
				break;

			// case FunctionConstant.OWNER_ADD_TO_WL:
			// try {
			// addToWhitelist(command[1], event);
			// } catch (Exception e) {
			// throwError(FunctionConstant.OWNER_ADD_TO_WL, e, event);
			// }
			// break;
			// case FunctionConstant.OWNER_REMOVE_FROM_WL:
			// try {
			// removeFromWhitelist(command[1], event);
			// } catch (Exception e) {
			// throwError(FunctionConstant.OWNER_REMOVE_FROM_WL, e, event);
			// }
			// break;
			}

		}
	}

	public boolean ownerCheck(Long userID) {
		if (userID.equals(DiscordClient.getClient().getApplicationOwner().getLongID()))
			return true;
		return false;
	}

	// Command functions are placed below here

	private void changeDisplayPicture(String[] command, MessageReceivedEvent event) {
		if (command.length < 2)
			sendMessage("Image URL cannot be left blank.", event);
		else if (!(command[1].substring(command[1].length() - 3, command[1].length()).equalsIgnoreCase("jpg")
				|| command[1].substring(command[1].length() - 3, command[1].length()).equalsIgnoreCase("png")))
			sendMessage("Image URL must end with PNG or JPG", event);
		else
			RequestBuffer.request(() -> DiscordClient.getClient().changeAvatar(Image.forUrl(command[1].substring(command[1].length() - 3, command[1].length()), command[1])));
	}
	
	private void changeStatus(String[] command, MessageReceivedEvent event) {
		if (command.length < 2)
			sendMessage("Status message cannot be left blank.", event);
		else {
			StringBuilder status = new StringBuilder();
			for (int i = 1; i < command.length; i++) 
				status.append(command[i] + " ");
			RequestBuffer.request(() -> DiscordClient.getClient().changePresence(StatusType.ONLINE, ActivityType.PLAYING, status.toString()) );
		}
	}

	private int killRequestBuffer() {
		return RequestBuffer.killAllRequests();
	}

	private void lookForChannelMonitors(MessageReceivedEvent event) {
		StringBuilder monitorSB = new StringBuilder().append("No monitors found.");
		ArrayList<ITask> monitorList = TaskLoader.getTaskLoader().getAllChannelMonitors();
		if (monitorList.isEmpty())
			sendMessage(monitorSB.toString(), event);
		else {
			monitorSB = new StringBuilder();
			for (ITask monitors : TaskLoader.getTaskLoader().getAllChannelMonitors()) {
				ArrayList<String> flags = monitors.getSettings();
				IChannel monitorChannel = DiscordClient.getClient().getChannelByID(monitors.getChannelID());
				monitorSB.append("Monitor in Guild __" + monitorChannel.getGuild().getName() + "__ at channel #"
						+ monitorChannel.getName());
				monitorSB.append(" with flags ");
				for (int i = 0; i < flags.size(); i++) {
					if (i == 0)
						monitorSB.append(flags.get(i));
					else if (i % 2 == 0)
						monitorSB.append(", " + flags.get(i));
				}
				monitorSB.append(".\n");
			}
			sendMessage(monitorSB.toString(), event);
		}

	}
	
	public void embedPong(MessageReceivedEvent event) {
		EmbedBuilder embed = new EmbedBuilder().setLenient(true);
		embed.withAuthorName("Orin");
		embed.withThumbnail("https://pbs.twimg.com/media/CdbhdctXIAA25Zu.jpg");
		embed.withImage("https://i.pinimg.com/564x/7f/61/3f/7f613f1c4c5a6ec291049d1acb056f04.jpg");
		embed.appendField("Image Credits",
				"[Image taken from Pinterest](https://i.pinimg.com/564x/7f/61/3f/7f613f1c4c5a6ec291049d1acb056f04.jpg)",
				false);
		embed.appendField("Thumbnail Credits",
				"[Thumbnail Credits to @TouhouFanarts](https://twitter.com/TouhouFanarts)", false);
		embed.withDesc("Pong");

		sendEmbed(embed, event);
	}
	
	private void getEmojiString(MessageReceivedEvent event) {
		StringBuilder emojiString = new StringBuilder();
		for (IEmoji emoji : event.getGuild().getEmojis()) {
			if (emoji.isAnimated()) {
				emojiString.append("<a:" + emoji.getName() + ":" + emoji.getStringID() + "> - `<a:" + emoji.getName() + ":" + emoji.getStringID() + ">`\n");
			}
		}
		sendMessage(emojiString.toString(), event);
	}

	// private void addToWhitelist(String userID, MessageReceivedEvent event) {
	//// sendMessage(userID, event);
	// if (!UserWhitelist.getWhitelist().validateUser(userID)) {
	// if (UserWhitelist.getWhitelist().addUserToWhitelist(userID))
	// sendMessage("User " +
	// event.getGuild().getUserByID(Long.parseLong(userID))
	// .getNicknameForGuild(event.getGuild())
	// + " added to whitelist.\nReminder : Whitelist is persistant across
	// servers.",
	// event);
	// else
	// sendMessage("Error updating whitelist. Please contact bot author to
	// rectify this issue", event);
	// }
	// else
	// sendMessage("User already in whitelist. No actions were taken.", event);
	// }
	//
	// private void removeFromWhitelist(String userID, MessageReceivedEvent
	// event) {
	// sendMessage("Command under construction. Tehepero XP", event);
	// }
}