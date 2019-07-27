package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.OfflineUser;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.api.interfaces.PartyPlayer;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.commands.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.players.objects.RankImpl;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandInfo extends PartiesSubCommand {
	@Getter private final boolean executableByConsole = false;
	
	public CommandInfo(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
		
		// Checks for command prerequisites
		if (!sender.hasPermission(PartiesPermission.INFO.toString())) {
			sendNoPermissionMessage(partyPlayer, PartiesPermission.INFO);
			return false;
		}
		
		PartyImpl party = null;
		if (commandData.getArgs().length == 1) {
			party = ((PartiesPlugin) plugin).getPartyManager().getPartyOfPlayer(partyPlayer);
			if (party == null) {
				sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
				return false;
			}
		}
		
		((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
		((PartiesCommandData) commandData).setParty(party);
		commandData.addPermission(PartiesPermission.INFO_OTHERS);
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
		PartyImpl party = ((PartiesCommandData) commandData).getParty();
		
		// Command handling
		if (party == null && commandData.getArgs().length > 1) {
			if (!commandData.havePermission(PartiesPermission.INFO_OTHERS)) {
				sendNoPermissionMessage(partyPlayer, PartiesPermission.INFO_OTHERS);
				return;
			}
			
			party = ((PartiesPlugin) plugin).getPartyManager().getParty(commandData.getArgs()[1]);
		}
		
		if (party == null) {
			sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_PARTYNOTFOUND
					.replace("%party%", commandData.getArgs()[1]));
			return;
		}
		
		// Command starts
		for (String line : Messages.MAINCMD_INFO_CONTENT) {
			// List ranks
			Matcher mat = Pattern.compile("%list_(.*?)%").matcher(line);
			while (mat.find()) {
				RankImpl rr = ((PartiesPlugin) plugin).getRankManager().searchRankByHardName(mat.group().substring(6, mat.group().length() - 1));
				if (rr != null) {
					StringBuilder list = new StringBuilder();
					int counter = 0;
					
					for (UUID playerUUID : party.getMembers()) {
						PartyPlayerImpl pl = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(playerUUID);
						
						// Check rank level
						if (pl.getRank() == rr.getLevel()) {
							if (list.length() > 0) {
								list.append(Messages.MAINCMD_INFO_LIST_SEPARATOR);
							}
							
							OfflineUser offlinePlayer = plugin.getOfflinePlayer(pl.getPlayerUUID());
							if (offlinePlayer != null) {
								if (offlinePlayer.isOnline() && !pl.isVanished()) {
									list.append(
											((PartiesPlugin) plugin).getMessageUtils().convertAllPlaceholders(
													Messages.MAINCMD_INFO_LIST_ONLINEFORMAT,
													party,
													pl)
									);
								} else {
									list.append(
											((PartiesPlugin) plugin).getMessageUtils().convertAllPlaceholders(
													Messages.MAINCMD_INFO_LIST_OFFLINEFORMAT,
													party,
													pl)
									);
								}
								//list.append(pl.getName());
							} else
								list.append(Messages.MAINCMD_INFO_LIST_UNKNOWN);
							counter++;
						}
					}
					line = line
							.replace(mat.group(), list.toString().isEmpty() ? Messages.MAINCMD_INFO_LIST_EMPTY : list.toString())
							.replace("%number_" + mat.group().substring(6, mat.group().length()-1) + "%",Integer.toString(counter));
				}
			}
			
			// Online players
			if (line.contains("%online%")) {
				StringBuilder sb = new StringBuilder();
				Set<PartyPlayer> list = party.getOnlineMembers(false);
				if (list.size() == 0)
					sb.append(Messages.MAINCMD_INFO_LIST_EMPTY);
				else {
					for (PartyPlayer pp : list) {
						if (sb.length() > 0) {
							sb.append(Messages.MAINCMD_INFO_LIST_SEPARATOR);
						}
						sb.append(((PartiesPlugin) plugin).getMessageUtils().convertAllPlaceholders(
								Messages.MAINCMD_INFO_LIST_ONLINEFORMAT,
								party,
								(PartyPlayerImpl) pp));
					}
				}
				line = line.replace("%online%", sb.toString());
			}
			
			// Other placeholders
			line = ((PartiesPlugin) plugin).getMessageUtils().convertPartyPlaceholders(line, party, Messages.MAINCMD_INFO_LIST_MISSING);
			
			sendMessage(sender, partyPlayer, line);
		}
		
		plugin.getLoggerManager().logDebug(PartiesConstants.DEBUG_CMD_INFO
				.replace("{player}", sender.getName())
				.replace("{party}", party.getName()), true);
	}
}