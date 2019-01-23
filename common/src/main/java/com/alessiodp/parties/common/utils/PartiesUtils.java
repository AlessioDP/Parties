package com.alessiodp.parties.common.utils;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.AbstractCommand;
import com.alessiodp.parties.common.commands.utils.CommandData;
import com.alessiodp.parties.common.configuration.Constants;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.logging.LoggerManager;
import com.alessiodp.parties.common.user.User;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PartiesUtils {
	
	/*
	 * Regex utils
	 */
	
	public static boolean checkCensor(String regex, String text, String regexError) {
		boolean ret = false;
		if (!regex.isEmpty()) {
			try {
				Matcher matcher = Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(text);
				
				if (matcher.find()) {
					ret = true;
				}
			} catch (Exception ex) {
				LoggerManager.printError(regexError);
				ex.printStackTrace();
			}
		}
		return ret;
	}
	
	public static boolean checkAllowedCharacters(String regex, String text, String regexError) {
		boolean ret = true;
		try {
			Matcher matcher = Pattern.compile(regex).matcher(text);
			
			ret = matcher.matches();
		} catch (Exception ex) {
			LoggerManager.printError(regexError);
			ex.printStackTrace();
		}
		return ret;
	}
	
	/*
	 * Hash utils
	 */
	public static String hashText(String text) {
		String ret = "";
		try {
			byte[] result = MessageDigest.getInstance(ConfigParties.PASSWORD_HASH)
					.digest(text.getBytes(ConfigParties.PASSWORD_ENCODE));
			StringBuilder stringBuilder = new StringBuilder();
			for (byte b : result) {
				stringBuilder.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
			}
			ret = stringBuilder.toString();
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	/*
	 * Command utils
	 */
	public static void executeCommand(PartiesPlugin plugin, User sender, String mainCommand, AbstractCommand command, String[] args) {
		CommandData cd = new CommandData();
		cd.setSender(sender);
		cd.setCommandLabel(mainCommand);
		cd.setArgs(args);
		if (command.preRequisites(cd)) {
			CompletableFuture.supplyAsync(() -> {
				command.onCommand(cd);
				return true;
			}, plugin.getPartiesScheduler().getCommandsExecutor())
					.exceptionally(ex -> {
						ex.printStackTrace();
						return false;
					});
		}
	}
	
	public static Boolean handleOnOffCommand(Boolean ret, String[] args) {
		if (args.length > 1) {
			if (args[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_ON))
				ret = true;
			else if (args[1].equalsIgnoreCase(ConfigMain.COMMANDS_SUB_OFF))
				ret = false;
			else
				ret = null;
		} else {
			ret = !ret;
		}
		return ret;
	}
	
	/*
	 * Tab completation parser
	 */
	public static List<String> tabCompleteParser(List<String> commands, String word) {
		List<String> ret = new ArrayList<>();
		for (String s : commands) {
			if (s.toLowerCase().startsWith(word.toLowerCase())) {
				ret.add(s);
			}
		}
		return ret;
	}
	
	public static List<String> tabCompleteOnOff(String[] args) {
		// args: <cmd> on/off
		List<String> ret = new ArrayList<>();
		if (args.length == 2) {
			ret.add(ConfigMain.COMMANDS_SUB_ON);
			ret.add(ConfigMain.COMMANDS_SUB_OFF);
			if (!args[1].isEmpty()) {
				ret = PartiesUtils.tabCompleteParser(ret, args[1]);
			}
		}
		return ret;
	}
	
	public static List<String> tabCompletePlayerList(PartiesPlugin plugin, String[] args, int index) {
		// args: <cmd> ... playerListAtIndex ...
		List<String> ret = new ArrayList<>();
		if (args.length == (index + 1)) {
			for (User u : plugin.getOnlinePlayers()) {
				ret.add(u.getName());
			}
			ret = tabCompleteParser(ret, args[index]);
		}
		return ret;
	}
}
