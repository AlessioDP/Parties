package com.alessiodp.parties.bukkit.commands.main;

import com.alessiodp.parties.bukkit.commands.sub.BukkitCommandClaim;
import com.alessiodp.parties.bukkit.commands.sub.BukkitCommandConfirm;
import com.alessiodp.parties.bukkit.commands.sub.BukkitCommandHome;
import com.alessiodp.parties.bukkit.commands.sub.BukkitCommandProtection;
import com.alessiodp.parties.bukkit.commands.sub.BukkitCommandSetHome;
import com.alessiodp.parties.bukkit.commands.sub.BukkitCommandTeleport;
import com.alessiodp.parties.bukkit.commands.list.BukkitCommands;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigParties;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.main.CommandParty;

public class BukkitCommandParty extends CommandParty {
	
	public BukkitCommandParty(PartiesPlugin instance) {
		super(instance);
		
		description = BukkitConfigMain.COMMANDS_DESC_PARTY;
		
		// Claim
		if (BukkitConfigMain.ADDONS_GRIEFPREVENTION_ENABLE)
			super.register(BukkitCommands.CLAIM, new BukkitCommandClaim(plugin, this));
		
		// Confirm
		if (BukkitConfigMain.ADDONS_VAULT_ENABLE && BukkitConfigMain.ADDONS_VAULT_CONFIRM_ENABLE)
			super.register(BukkitCommands.CONFIRM, new BukkitCommandConfirm(plugin, this));
		
		// Home
		if (BukkitConfigParties.HOME_ENABLE) {
			super.register(BukkitCommands.HOME, new BukkitCommandHome(plugin, this));
			super.register(BukkitCommands.SETHOME, new BukkitCommandSetHome(plugin, this));
		}
		
		// Protection
		if (BukkitConfigParties.FRIENDLYFIRE_TYPE.equalsIgnoreCase("command"))
			super.register(BukkitCommands.PROTECTION, new BukkitCommandProtection(plugin, this));
		
		// Teleport
		if (BukkitConfigParties.TELEPORT_ENABLE)
			super.register(CommonCommands.TELEPORT, new BukkitCommandTeleport(plugin, this));
	}
}
