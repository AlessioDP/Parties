package com.alessiodp.parties.common.addons.external;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import lombok.RequiredArgsConstructor;
import me.leoko.advancedban.manager.PunishmentManager;
import me.leoko.advancedban.utils.Punishment;
import me.leoko.advancedban.utils.PunishmentType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@RequiredArgsConstructor
public abstract class AdvancedBanHandler {
	@NotNull protected final PartiesPlugin partiesPlugin;
	private static final String ADDON_NAME = "AdvancedBan";
	protected static boolean active;
	
	public void init() {
		active = false;
		if (ConfigMain.ADDITIONAL_MODERATION_ENABLE && isEnabled()) {
			if (partiesPlugin.isPluginEnabled(ADDON_NAME)) {
				active = true;
				
				registerListener();
				
				partiesPlugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_HOOKED, ADDON_NAME), true);
			} else {
				active = false;
				
				partiesPlugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_FAILED, ADDON_NAME), true);
			}
		}
	}
	
	protected abstract boolean isEnabled();
	
	public static boolean isMuted(UUID uuid) {
		if (active) {
			return PunishmentManager.get().isMuted(uuid.toString());
		}
		return false;
	}
	
	public void onBan(Punishment punishment) {
		if (active && ConfigMain.ADDITIONAL_MODERATION_AUTOKICK) {
			if (punishment.getType() == PunishmentType.BAN || punishment.getType() == PunishmentType.IP_BAN) {
				try {
					UUID uuid = UUID.fromString(punishment.getUuid());
					partiesPlugin.getPartyManager().kickBannedPlayer(uuid);
				} catch (IllegalArgumentException ignored) {}
			}
		}
	}
	
	public abstract void registerListener();
}
