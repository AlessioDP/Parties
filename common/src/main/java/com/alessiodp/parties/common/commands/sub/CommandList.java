package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.core.common.utils.Pair;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.storage.PartiesDatabaseManager;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CommandList extends PartiesSubCommand {
	private final String syntaxOrder;
	
	public CommandList(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(
				plugin,
				mainCommand,
				CommonCommands.LIST,
				PartiesPermission.USER_LIST,
				ConfigMain.COMMANDS_SUB_LIST,
				true
		);
		
		syntax = String.format("%s [%s]",
				baseSyntax(),
				Messages.PARTIES_SYNTAX_PAGE
		);
		
		if (ConfigParties.ADDITIONAL_LIST_CHANGE_ORDER) {
			syntaxOrder = String.format("%s [%s] [%s]",
					baseSyntax(),
					Messages.PARTIES_SYNTAX_ORDER,
					Messages.PARTIES_SYNTAX_PAGE
			);
		} else {
			syntaxOrder = syntax;
		}
		
		description = Messages.HELP_ADDITIONAL_DESCRIPTIONS_LIST;
		help = Messages.HELP_ADDITIONAL_COMMANDS_LIST;
	}
	
	@Override
	public String getSyntaxForUser(User user) {
		if (!user.hasPermission(permission))
			return syntax;
		return syntaxOrder;
	}
	
	@Override
	public boolean preRequisites(CommandData commandData) {
		User sender = commandData.getSender();
		if (sender.isPlayer()) {
			PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());
			
			// Checks for command prerequisites
			if (!sender.hasPermission(permission)
					&& !sender.hasPermission(PartiesPermission.USER_LIST_NAME)
					&& !sender.hasPermission(PartiesPermission.USER_LIST_ONLINE_MEMBERS)
					&& !sender.hasPermission(PartiesPermission.USER_LIST_MEMBERS)
					&& !sender.hasPermission(PartiesPermission.USER_LIST_KILLS)
					&& !sender.hasPermission(PartiesPermission.USER_LIST_EXPERIENCE)) {
				sendNoPermissionMessage(partyPlayer, permission);
				return false;
			}
			
			((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
			commandData.addPermission(permission);
			commandData.addPermission(PartiesPermission.USER_LIST_NAME);
			commandData.addPermission(PartiesPermission.USER_LIST_ONLINE_MEMBERS);
			commandData.addPermission(PartiesPermission.USER_LIST_MEMBERS);
			commandData.addPermission(PartiesPermission.USER_LIST_KILLS);
			commandData.addPermission(PartiesPermission.USER_LIST_EXPERIENCE);
		}
		return true;
	}
	
	@Override
	public void onCommand(CommandData commandData) {
		User sender = commandData.getSender();
		PartyPlayerImpl player = ((PartiesCommandData) commandData).getPartyPlayer();
		
		// Command handling
		int selectedPage = 1;
		PartiesDatabaseManager.ListOrder orderBy = null;
		
		if (commandData.getArgs().length > 1) {
			if (commandData.getArgs().length == 2) {
				try {
					selectedPage = Integer.parseInt(commandData.getArgs()[1]);
				} catch (NumberFormatException ignored) {
					orderBy = PartiesDatabaseManager.ListOrder.parse(commandData.getArgs()[1]);
					if (orderBy == null) {
						sendMessage(sender, player, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
								.replace("%syntax%", getSyntaxForUser(sender)));
						return;
					}
				}
			} else if (commandData.getArgs().length == 3) {
				orderBy = PartiesDatabaseManager.ListOrder.parse(commandData.getArgs()[1]);
				if (orderBy == null) {
					sendMessage(sender, player, Messages.ADDCMD_LIST_INVALID_ORDER);
					return;
				}
				
				try {
					selectedPage = Integer.parseInt(commandData.getArgs()[2]);
				} catch (NumberFormatException ignored) {
					sendMessage(sender, player, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
							.replace("%syntax%", getSyntaxForUser(sender)));
					return;
				}
				
			} else {
				sendMessage(sender, player, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
						.replace("%syntax%", getSyntaxForUser(sender)));
				return;
			}
		}
		
		if (orderBy != null) {
			if (!commandData.havePermission(permission) &&
					(
							(orderBy == PartiesDatabaseManager.ListOrder.NAME && !commandData.havePermission(PartiesPermission.USER_LIST_NAME))
									|| (orderBy == PartiesDatabaseManager.ListOrder.ONLINE_MEMBERS && !commandData.havePermission(PartiesPermission.USER_LIST_ONLINE_MEMBERS))
									|| (orderBy == PartiesDatabaseManager.ListOrder.MEMBERS && !commandData.havePermission(PartiesPermission.USER_LIST_MEMBERS))
									|| (orderBy == PartiesDatabaseManager.ListOrder.KILLS && !commandData.havePermission(PartiesPermission.USER_LIST_KILLS))
									|| (orderBy == PartiesDatabaseManager.ListOrder.EXPERIENCE && !commandData.havePermission(PartiesPermission.USER_LIST_EXPERIENCE))
					)) {
				sendMessage(sender, player, Messages.ADDCMD_LIST_INVALID_ORDER);
				return;
			}
		} else {
			if (commandData.havePermission(permission))
				orderBy = PartiesDatabaseManager.ListOrder.getType(ConfigParties.ADDITIONAL_LIST_ORDERBY);
			else if (commandData.havePermission(PartiesPermission.USER_LIST_NAME))
				orderBy = PartiesDatabaseManager.ListOrder.NAME;
			else if (commandData.havePermission(PartiesPermission.USER_LIST_ONLINE_MEMBERS))
				orderBy = PartiesDatabaseManager.ListOrder.ONLINE_MEMBERS;
			else if (commandData.havePermission(PartiesPermission.USER_LIST_MEMBERS))
				orderBy = PartiesDatabaseManager.ListOrder.MEMBERS;
			else if (commandData.havePermission(PartiesPermission.USER_LIST_KILLS))
				orderBy = PartiesDatabaseManager.ListOrder.KILLS;
			else if (commandData.havePermission(PartiesPermission.USER_LIST_EXPERIENCE))
				orderBy = PartiesDatabaseManager.ListOrder.EXPERIENCE;
			
			if (orderBy == null) orderBy = PartiesDatabaseManager.ListOrder.ONLINE_MEMBERS;
		}
		
		// Command starts
		int numberPlayers = Math.min(
				orderBy == PartiesDatabaseManager.ListOrder.ONLINE_MEMBERS ? ((PartiesPlugin) plugin).getPartyManager().getCacheParties().size() : ((PartiesPlugin) plugin).getDatabaseManager().getListPartiesNumber(),
				ConfigParties.ADDITIONAL_LIST_LIMITPARTIES);
		int limit = Math.max(1, ConfigParties.ADDITIONAL_LIST_PERPAGE);
		
		int maxPages;
		if (numberPlayers == 0)
			maxPages = 1;
		else if (numberPlayers % ConfigParties.ADDITIONAL_LIST_PERPAGE == 0)
			maxPages = numberPlayers / ConfigParties.ADDITIONAL_LIST_PERPAGE;
		else
			maxPages = (numberPlayers / ConfigParties.ADDITIONAL_LIST_PERPAGE) + 1;
		
		if (selectedPage > maxPages)
			selectedPage = maxPages;
		
		int offset = selectedPage > 1 ? limit * (selectedPage - 1) : 0;
		LinkedHashSet<Pair<Integer, PartyImpl>> parties = new LinkedHashSet<>();
		if (orderBy == PartiesDatabaseManager.ListOrder.ONLINE_MEMBERS) {
			Set<PartyImpl> onlineParties = new LinkedHashSet<>(new TreeSet<PartyImpl>(Comparator.comparingInt(p -> p.getOnlineMembers(false).size())));
			((PartiesPlugin) plugin).getPartyManager().getCacheParties().values().forEach((party) -> {
				if (party.getName() != null && !ConfigParties.ADDITIONAL_LIST_HIDDENPARTIES.contains(party.getName()) && !ConfigParties.ADDITIONAL_LIST_HIDDENPARTIES.contains(party.getId().toString())) {
					onlineParties.add(party);
				}
			});
			
			// Limit and offset
			Iterator<PartyImpl> iterator = onlineParties.iterator();
			int n = 0;
			for (int c = 0; iterator.hasNext() && n < limit; c++) {
				PartyImpl p = iterator.next();
				if (c >= offset) {
					parties.add(new Pair<>(c + 1, p));
					n++;
				}
			}
		} else {
			int index = 1;
			for (PartyImpl party : ((PartiesPlugin) plugin).getDatabaseManager().getListParties(orderBy, limit, offset)) {
				parties.add(new Pair<>(index, party));
				index++;
			}
		}
		
		sendMessage(sender, player, Messages.ADDCMD_LIST_HEADER
				.replace("%total%", Integer.toString(numberPlayers))
				.replace("%page%", Integer.toString(selectedPage))
				.replace("%maxpages%", Integer.toString(maxPages)));
		
		if (parties.size() > 0) {
			parties.forEach((pair) -> {
				pair.getValue().refresh();
				sendMessage(sender, player, Messages.ADDCMD_LIST_FORMATPARTY
						.replace("%index%", pair.getKey().toString()), pair.getValue());
			});
		} else {
			sendMessage(sender, player, Messages.ADDCMD_LIST_NOONE);
		}
		
		sendMessage(sender, player, Messages.ADDCMD_LIST_FOOTER
				.replace("%total%", Integer.toString(numberPlayers))
				.replace("%page%", Integer.toString(selectedPage))
				.replace("%maxpages%", Integer.toString(maxPages)));
	}
	
	@Override
	public List<String> onTabComplete(@NonNull User sender, String[] args) {
		List<String> ret = new ArrayList<>();
		if (sender.hasPermission(permission)
				&& args.length == 2
				&& ConfigParties.ADDITIONAL_LIST_CHANGE_ORDER) {
			ret.add(Messages.PARTIES_SYNTAX_NAME);
			ret.add(Messages.PARTIES_SYNTAX_ONLINE_MEMBERS);
			ret.add(Messages.PARTIES_SYNTAX_MEMBERS);
			ret.add(Messages.PARTIES_SYNTAX_KILLS);
			ret.add(Messages.PARTIES_SYNTAX_EXPERIENCE);
		}
		return ret;
	}
}