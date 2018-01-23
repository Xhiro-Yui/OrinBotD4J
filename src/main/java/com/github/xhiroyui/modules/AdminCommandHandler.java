package com.github.xhiroyui.modules;

import java.util.Arrays;
import java.util.HashMap;

import com.github.xhiroyui.util.Command;
import com.github.xhiroyui.util.UserWhitelist;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.RequestBuffer;

public class AdminCommandHandler extends ModuleHandler {

	private UserWhitelist whitelist;
	private AdminCommands module;
	private HashMap<IModules, Boolean> moduleList;

	public AdminCommandHandler(AdminCommands _module, UserWhitelist _whitelist,
			HashMap<IModules, Boolean> _moduleList) {
		whitelist = _whitelist;
		module = _module;
		moduleList = _moduleList;
		createCommands();
	}

	private void createCommands() {
		Command command;
		
		command = new Command("GET_AUTHOR");
		command.setCommandName("author");
		command.setCommandDescription("Gives the name of the author of this Module");
		command.getCommandCallers().add("moduleauthor");
		command.getCommandCallers().add("author");
		command.setMaximumArgs(0);
		commandList.add(command);
		
		command = new Command("GET_PEEK");
		command.setCommandName("boo");
		command.setCommandDescription("Peeks at you in the bath");
		command.getCommandCallers().add("peek");
		command.getCommandCallers().add("peeks");
		command.setMaximumArgs(0);
		commandList.add(command);
		
	}

	@EventSubscriber
	public void OnMesageEvent(MessageReceivedEvent event)
			throws RateLimitException, DiscordException, MissingPermissionsException {
		if (event.getMessage().getContent().startsWith(module.getPrefix())) {
			if (!adminCheck(event.getAuthor(), event.getGuild())) {
				System.out.println("Admin check failed");
				if (whitelist.validateUser(event.getAuthor().getStringID())) {
					System.out.println("User is Whitelisted");
					executeCommand(event);
				} else {
					System.out.println("Whitelist check failed");
				}
			} else {
				System.out.println("User is Admin");
				executeCommand(event);
			}
		}
	}

	public void executeCommand(MessageReceivedEvent event) {
		String[] command = parseMessage(
				event.getMessage().getContent().substring(1, event.getMessage().getContent().length()));
		
		String commandCode = validateCommand(event, command);
		if (commandCode != null) {
			switch (commandCode) {
			case "ping":
				sendMessage("pong", event);
				break;
			case "peek":
				sendMessage("peek-a-boo", event);
				break;
			case "addwhitelist":
			case "addwl":
				commandAddToWhitelist(Arrays.copyOfRange(command, 1, command.length), event);
				break;
			case "embed":
				sendEmbed(event);
				break;
			case "GET_AUTHOR":
				sendMessage(module.getAuthor(), event);
				break;
			}
		}
	}

	private void commandAddToWhitelist(String[] command, MessageReceivedEvent event) {
		String message = "";
		for (IUser user : event.getMessage().getMentions()) {
			whitelist.addUserToWhitelist(user.getStringID());
			message += user.mention() + " ";
		}

		sendMessage("User added to whitelist " + message, event);

	}

	public void sendEmbed(MessageReceivedEvent event) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.appendField("fieldTitleInline", "fieldContentInline", true);
		builder.appendField("fieldTitleInline2", "fieldContentInline2", true);
		builder.appendField("fieldTitleNotInline", "fieldContentNotInline", false);
		builder.appendField(":tada: fieldWithCoolThings :tada:", "[hiddenLink](http://i.imgur.com/Y9utuDe.png)", false);
		builder.withAuthorName("Murgleis");
		builder.withAuthorIcon("https://gbf.wiki/images/thumb/d/db/Label_Weapon_Sabre.png/61px-Label_Weapon_Sabre.png");
		builder.withAuthorUrl("http://i.imgur.com/oPvYFj3.png");
		builder.withColor(255, 0, 0);
		builder.withDesc(
				"Sincerity intensifies the deep blue radiance of this sabre. Its ornamentation makes any vow sworn on this blade as unbreakable as platinum.");
		builder.withTitle("withTitle");
		builder.withUrl("http://i.imgur.com/IrEVKQq.png");
		builder.withImage("https://gbf.wiki/images/thumb/f/ff/Murgleis.png/600px-Murgleis.png");
		builder.withFooterIcon("http://i.imgur.com/Ch0wy1e.png");
		builder.withFooterText("footerText");
		builder.withFooterIcon("http://i.imgur.com/TELh8OT.png");
		builder.withThumbnail("https://gbf.wiki/images/thumb/f/ff/Murgleis.png/600px-Murgleis.png");
		RequestBuffer.request(() -> event.getChannel().sendMessage(builder.build()));
	}
}