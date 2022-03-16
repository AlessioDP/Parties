package com.alessiodp.parties.bukkit.addons.external.hooks;

import com.alessiodp.parties.bukkit.addons.external.MagicHandler;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.elmakers.mine.bukkit.api.entity.TeamProvider;
import com.elmakers.mine.bukkit.api.magic.MagicAPI;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class MagicHook implements TeamProvider {
	@NotNull private final PartiesPlugin plugin;
	
	public void register(Plugin magicPlugin) {
		((MagicAPI) magicPlugin).getController().register(this);
	}
	
	@Override
	public boolean isFriendly(Entity attacker, Entity entity) {
		if (MagicHandler.isActive() && attacker instanceof Player && entity instanceof Player) {
			if (plugin.getApi().areInTheSameParty(attacker.getUniqueId(), entity.getUniqueId())) {
				PartyPlayerImpl partyPlayer = plugin.getPlayerManager().getPlayer(attacker.getUniqueId());
				if (partyPlayer != null) {
					PartyImpl party = plugin.getPartyManager().getPartyOfPlayer(partyPlayer);
					return party.isFriendlyFireProtected();
				}
			}
		}
		return false;
	}
}
