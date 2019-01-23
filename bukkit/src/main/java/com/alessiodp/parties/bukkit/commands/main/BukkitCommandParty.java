package com.alessiodp.parties.bukkit.commands.main;

import com.alessiodp.parties.bukkit.commands.sub.BukkitCommandTeleport;
import com.alessiodp.parties.bukkit.commands.sub.CommandClaim;
import com.alessiodp.parties.bukkit.commands.sub.CommandConfirm;
import com.alessiodp.parties.bukkit.commands.sub.CommandHome;
import com.alessiodp.parties.bukkit.commands.sub.CommandProtection;
import com.alessiodp.parties.bukkit.commands.sub.CommandSetHome;
import com.alessiodp.parties.bukkit.commands.list.BukkitCommands;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.main.CommandParty;

public class BukkitCommandParty extends CommandParty {
	
	public BukkitCommandParty(PartiesPlugin instance) {
		super(instance);
	}
	
	@Override
	public void reload() {
		super.reload();
		
		// Claim
		if (BukkitConfigMain.ADDONS_GRIEFPREVENTION_ENABLE)
			super.register(BukkitCommands.CLAIM, new CommandClaim(plugin));
		
		// Confirm
		if (BukkitConfigMain.ADDONS_VAULT_ENABLE && BukkitConfigMain.ADDONS_VAULT_CONFIRM_ENABLE)
			super.register(BukkitCommands.CONFIRM, new CommandConfirm(plugin));
		
		// Home
		if (BukkitConfigParties.HOME_ENABLE) {
			super.register(BukkitCommands.HOME, new CommandHome(plugin));
			super.register(BukkitCommands.SETHOME, new CommandSetHome(plugin));
		}
		
		// Protection
		if (BukkitConfigParties.FRIENDLYFIRE_TYPE.equalsIgnoreCase("command"))
			super.register(BukkitCommands.PROTECTION, new CommandProtection(plugin));
		
		// Teleport
		if (BukkitConfigParties.TELEPORT_ENABLE)
			super.register(CommonCommands.TELEPORT, new BukkitCommandTeleport(plugin));
	}
}
