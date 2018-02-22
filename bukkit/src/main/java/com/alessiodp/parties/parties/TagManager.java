package com.alessiodp.parties.parties;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.alessiodp.parties.addons.external.ProtocolLibHandler;
import com.alessiodp.parties.configuration.Constants;
import com.alessiodp.parties.configuration.data.ConfigMain;
import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;
import com.alessiodp.parties.utils.PartiesUtils;

public class TagManager {
	private Scoreboard scoreboard;
	
	public TagManager() {
		scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
	}
	
	public void refreshTag(PartyEntity party) {
		if (party != null) {
			// ProtocolLib
			ProtocolLibHandler.refreshParty(party);
			
			// Scoreboard
			Team t = scoreboard.getTeam(Constants.SCOREBOARD_PREFIX + party.getName().toLowerCase());
			if (t != null) {
				try {
					t.unregister();
				} catch (Exception ex) {}
			}
			
			if ((ConfigMain.ADDITIONAL_TAG_ENABLE && ConfigMain.ADDITIONAL_TAG_ENGINE.isScoreboard()) || ConfigMain.PARTIES_SEEINVISIBLE) {
				if (party.getOnlinePlayers().size() > 0) {
					String str = Constants.SCOREBOARD_PREFIX + party.getName().toLowerCase();
					if (str.length() > 10)
						str = str.substring(0, 10);
					Team team = scoreboard.getTeam(str);
					if (team == null)
						team = scoreboard.registerNewTeam(str);
					
					if (ConfigMain.ADDITIONAL_TAG_ENABLE) {
						// Prefix/Suffix handler
						if (ConfigMain.ADDITIONAL_TAG_TYPE.isCustom()) {
							// Custom
							if (ConfigMain.ADDITIONAL_TAG_CUSTOM_PREFIX)
								if (!party.getPrefix().isEmpty())
									team.setPrefix(ChatColor.translateAlternateColorCodes('&', ConfigMain.ADDITIONAL_TAG_CUSTOM_FORMATPREFIX)
											.replace("%prefix%", party.getPrefix()));
								else
									team.setPrefix("");
							if (ConfigMain.ADDITIONAL_TAG_CUSTOM_SUFFIX)
								if (!party.getSuffix().isEmpty())
									team.setSuffix(ChatColor.translateAlternateColorCodes('&', ConfigMain.ADDITIONAL_TAG_CUSTOM_FORMATSUFFIX)
											.replace("%suffix%", party.getSuffix()));
								else
									team.setSuffix("");
						} else {
							// Base
							team.setPrefix(ChatColor.translateAlternateColorCodes('&', PartiesUtils.convertPartyPlaceholders(ConfigMain.ADDITIONAL_TAG_BASE_FORMATPREFIX, party)));
							team.setSuffix(ChatColor.translateAlternateColorCodes('&', PartiesUtils.convertPartyPlaceholders(ConfigMain.ADDITIONAL_TAG_BASE_FORMATSUFFIX, party)));
						}
					} else {
						team.setPrefix("");
						team.setSuffix("");
					}
					
					if (ConfigMain.PARTIES_SEEINVISIBLE)
						team.setCanSeeFriendlyInvisibles(true);
					else
						team.setCanSeeFriendlyInvisibles(false);
					
					for (Player player : party.getOnlinePlayers()) {
						team.addEntry(player.getName());
					}
				}
			}
		}
	}
	
	public void removePlayerTag(PartyPlayerEntity player, PartyEntity party) {
		if (player != null) {
			// ProtocolLib
			ProtocolLibHandler.refreshParty(party);
			ProtocolLibHandler.refreshPlayer(player);
			
			// Scoreboard
			if ((ConfigMain.ADDITIONAL_TAG_ENABLE && ConfigMain.ADDITIONAL_TAG_ENGINE.isScoreboard()) || ConfigMain.PARTIES_SEEINVISIBLE) {
				Team team = scoreboard.getEntryTeam(player.getName());
				if (team != null && team.getName().startsWith(Constants.SCOREBOARD_PREFIX))
					team.removeEntry(player.getName());
			}
		}
	}
	
	public void deleteTag(PartyEntity party) {
		// ProtocolLib
		if (party != null) {
			ProtocolLibHandler.refreshParty(party);
		}
		
		// Scoreboard
		if ((ConfigMain.ADDITIONAL_TAG_ENABLE && ConfigMain.ADDITIONAL_TAG_ENGINE.isScoreboard()) || ConfigMain.PARTIES_SEEINVISIBLE) {
			Team t = scoreboard.getTeam(Constants.SCOREBOARD_PREFIX + party.getName().toLowerCase());
			if (t != null) {
				for (String e : t.getEntries())
					t.removeEntry(e);
				try {
					t.unregister();
				} catch (Exception ex) {}
			}
		}
	}
	
	public static String getPlayerPrefix(PartyPlayerEntity player, PartyEntity party) {
		String ret = "";
		if (ConfigMain.ADDITIONAL_TAG_TYPE.isCustom()) {
			// Custom
			if (ConfigMain.ADDITIONAL_TAG_CUSTOM_PREFIX)
				if (!party.getPrefix().isEmpty())
					ret = PartiesUtils.convertAllPlaceholders(ConfigMain.ADDITIONAL_TAG_CUSTOM_FORMATPREFIX, party, player);
		} else {
			// Base
			ret = PartiesUtils.convertAllPlaceholders(ConfigMain.ADDITIONAL_TAG_BASE_FORMATPREFIX, party, player);
		}
		return ret;
	}
	public static String getPlayerSuffix(PartyPlayerEntity player, PartyEntity party) {
		String ret = "";
		if (ConfigMain.ADDITIONAL_TAG_TYPE.isCustom()) {
			// Custom
			if (ConfigMain.ADDITIONAL_TAG_CUSTOM_SUFFIX)
				if (!party.getSuffix().isEmpty())
					ret = PartiesUtils.convertAllPlaceholders(ConfigMain.ADDITIONAL_TAG_CUSTOM_FORMATSUFFIX, party, player);
		} else {
			// Base
			ret = PartiesUtils.convertAllPlaceholders(ConfigMain.ADDITIONAL_TAG_BASE_FORMATSUFFIX, party, player);
		}
		return ret;
	}
}
