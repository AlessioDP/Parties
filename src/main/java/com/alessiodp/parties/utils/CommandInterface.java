package com.alessiodp.parties.utils;

import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
 
public interface CommandInterface {
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args);
}