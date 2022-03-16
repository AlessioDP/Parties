package com.alessiodp.parties.bukkit.api;

import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.api.PartiesOptionsHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BukkitPartiesOptionsHandler extends PartiesOptionsHandler {
	@Override
	public boolean isSkriptHookEnabled() {
		return BukkitConfigMain.PARTIES_HOOK_INTO_SKRIPT;
	}
	
	@Override
	public @Nullable String getBungeecordName() {
		return BukkitConfigMain.PARTIES_BUNGEECORD_SERVER_NAME;
	}
	
	@Override
	public @Nullable String getBungeecordId() {
		return BukkitConfigMain.PARTIES_BUNGEECORD_SERVER_ID;
	}
	
	@Override
	public boolean isClaimEnabled() {
		return BukkitConfigMain.ADDONS_CLAIM_ENABLE;
	}
	
	@Override
	public boolean isDynmapEnabled() {
		return BukkitConfigMain.ADDONS_DYNMAP_ENABLE;
	}
	
	@Override
	public @NotNull String getDynmapLayer() {
		return BukkitConfigMain.ADDONS_DYNMAP_MARKER_LAYER;
	}
	
	@Override
	public boolean isVaultEnabled() {
		return BukkitConfigMain.ADDONS_VAULT_ENABLE;
	}
	
	@Override
	public boolean isVaultCommandEnabled() {
		return BukkitConfigMain.ADDONS_VAULT_CONFIRM_ENABLE;
	}
}
