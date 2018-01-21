<<<<<<< HEAD
package com.github.xhiroyui.modules;

import java.util.HashMap;
import java.util.Map;

import com.github.xhiroyui.util.IModules;
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

	public AdminCommandHandler(AdminCommands _module, HashMap<IModules, Boolean> _moduleList,
			UserWhitelist _whitelist) {
		whitelist = _whitelist;
		moduleList = _moduleList;
		module = _module;
	}

	@EventSubscriber
	public void OnMesageEvent(MessageReceivedEvent event)
			throws RateLimitException, DiscordException, MissingPermissionsException {
		if (event.getMessage().getContent().startsWith(module.getPrefix())) {
			if (!adminCheck(event.getAuthor(), event.getGuild())) {
				System.out.println("Admin check failed");
				if (whitelist.validateUser(event.getAuthor().getStringID())) {
					System.out.println("User is Whitelisted");
					parseCommand(event);
				} else {
					System.out.println("Whitelist check failed");
				}
			} else {
				System.out.println("User is Admin");
				parseCommand(event);
			}
		}

	}

	public void parseCommand(MessageReceivedEvent event) {
		String[] command = parseMessage(
				event.getMessage().getContent().substring(1, event.getMessage().getContent().length()));
		if (command.length > 1 && command[1] == "help") {
			// Do a help print here
		}

		switch (command[0]) {
		case "ping":
			sendMessage("pong", event);
			break;
		case "peek":
			sendMessage("peek-a-boo", event);
			break;
		case "addwhitelist":
		case "addwl":
			commandAddToWhitelist(command, event);
			break;
		case "embed":
			sendEmbed(event);
			break;
		case "embed2":
			sendEmbed2(event);
			break;
		case "author":
			sendMessage(module.getAuthor(), event);
			break;
		case "checkmodules":
			for (Map.Entry<IModules, Boolean> entry : moduleList.entrySet()) {
				IModules key = entry.getKey();
				Boolean value = entry.getValue();
				System.out.println(key.toString() + value.booleanValue());
			}
			break;
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

	public void sendEmbed2(MessageReceivedEvent event) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.appendField("# Chain Burst DMG formula",
				"Total Ougi DMG x \nChain constant x \n(1 + Weakness constant) x \n(1 + Chain Burst Up buff) \n \n# Chain Constant:  \n2 =1/4 \n3 = 1/3 \n4 = 1/2 \n \n# Weakness Constant: \nAdvantage = 0.5 \nDisadvantage = -0.25 \n",
				false);
		builder.withColor(255, 0, 0);
		builder.withThumbnail("https://gbf.wiki/images/thumb/f/ff/Murgleis.png/600px-Murgleis.png");
		RequestBuffer.request(() -> event.getChannel().sendMessage(builder.build()));
	}
=======
package com.github.xhiroyui.modules;

import com.github.xhiroyui.util.UserWhitelist;

import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MessageBuilder;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;
import sx.blah.discord.util.RequestBuffer;

public class AdminCommandHandler extends CommandHandler {

	private UserWhitelist whitelist;
	private AdminCommands module;

	public AdminCommandHandler(AdminCommands _module, UserWhitelist _whitelist) {
		whitelist = _whitelist;
		module = _module;
	}

	@EventSubscriber
	public void OnMesageEvent(MessageReceivedEvent event)
			throws RateLimitException, DiscordException, MissingPermissionsException {
		if (event.getMessage().getContent().startsWith(module.getPrefix())) {
			if (!adminCheck(event.getAuthor(), event.getGuild())) {
				System.out.println("Admin check failed");
				if (whitelist.validateUser(event.getAuthor().getStringID())) {
					System.out.println("User is Whitelisted");
					parseCommand(event);
				}
				else {
					System.out.println("Whitelist check failed");
				}
			} else {
				System.out.println("User is Admin");
				parseCommand(event);
			} 
		}

	}

	

	public boolean adminCheck(IUser user, IGuild guild) {
		for (Permissions perm : user.getPermissionsForGuild(guild)) {
			if (perm.toString().equalsIgnoreCase("administrator")) {
				return true;
			}
		}
		return false;
	}  
	
	public void parseCommand(MessageReceivedEvent event) {
		String[] command = parseMessage(
				event.getMessage().getContent().substring(1, event.getMessage().getContent().length()));
		switch (command[0]) {
		case "ping":
			sendMessage("pong", event);
			break;
		case "peek":
			sendMessage("peek-a-boo", event);
			break;
		case "addwhitelist":
		case "addwl":
			commandAddToWhitelist(command, event);
			break;
		case "embed":
			sendEmbed(event);
			break;
		case "embed2":
			sendEmbed2(event);
			break;
		case "author":
			sendMessage(module.getAuthor(), event);
			break;
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

	public void sendMessage(String message, MessageReceivedEvent event)
			throws RateLimitException, DiscordException, MissingPermissionsException {
		new MessageBuilder(AdminCommands.client).appendContent(message).withChannel(event.getMessage().getChannel())
				.build();
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
		    builder.withDesc("Sincerity intensifies the deep blue radiance of this sabre. Its ornamentation makes any vow sworn on this blade as unbreakable as platinum.");
		    builder.withTitle("withTitle");
		    builder.withUrl("http://i.imgur.com/IrEVKQq.png");
		    builder.withImage("https://gbf.wiki/images/thumb/f/ff/Murgleis.png/600px-Murgleis.png");
		    builder.withFooterIcon("http://i.imgur.com/Ch0wy1e.png");
		    builder.withFooterText("footerText");
		    builder.withFooterIcon("http://i.imgur.com/TELh8OT.png");
		    builder.withThumbnail("https://gbf.wiki/images/thumb/f/ff/Murgleis.png/600px-Murgleis.png");
		    RequestBuffer.request(() -> event.getChannel().sendMessage(builder.build()));
	}
	
	public void sendEmbed2(MessageReceivedEvent event) {
		 EmbedBuilder builder = new EmbedBuilder();
		    builder.appendField("# Chain Burst DMG formula", "Total Ougi DMG x \nChain constant x \n(1 + Weakness constant) x \n(1 + Chain Burst Up buff) \n \n# Chain Constant:  \n2 =1/4 \n3 = 1/3 \n4 = 1/2 \n \n# Weakness Constant: \nAdvantage = 0.5 \nDisadvantage = -0.25 \n", false);
		    builder.withColor(255, 0, 0);
		    builder.withThumbnail("https://gbf.wiki/images/thumb/f/ff/Murgleis.png/600px-Murgleis.png");
		    RequestBuffer.request(() -> event.getChannel().sendMessage(builder.build()));
	}
>>>>>>> branch 'master' of https://github.com/Xhiro-Yui/OrinBotD4J
}