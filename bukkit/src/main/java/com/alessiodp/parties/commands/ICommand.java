package com.alessiodp.parties.commands;

import org.bukkit.command.CommandSender;
 
public interface ICommand {
	public void onCommand(CommandSender sender, String commandLabel, String[] args);
}