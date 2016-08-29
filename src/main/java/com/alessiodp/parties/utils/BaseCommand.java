package com.alessiodp.parties.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BaseCommand extends Command{
	private CommandExecutor exe = null;
	 
    public BaseCommand(String name) {
        super(name);
    }

    public boolean execute(CommandSender sender, String commandLabel,String[] args) {
        if(exe != null){
            exe.onCommand(sender, this, commandLabel,args);
        }
        return false;
    }
   
    public void setExecutor(CommandExecutor exe){
        this.exe = exe;
    }
}
