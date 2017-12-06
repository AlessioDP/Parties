package com.alessiodp.parties.utils;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.configuration.Messages;
import com.alessiodp.parties.objects.ThePlayer;
import com.alessiodp.parties.utils.enums.PartiesPermissions;
import com.alessiodp.partiesapi.interfaces.Rank;

public class PlayerUtil {
	private static Parties plugin;
	
	public PlayerUtil() {
		plugin = Parties.getInstance();
	}
	
	public static boolean checkPlayerRankAlerter(ThePlayer tp, PartiesPermissions perm) {
		boolean ret = true;
		Rank r = plugin.getPartyHandler().searchRank(tp.getRank());
		if (r != null && !tp.getPlayer().hasPermission(PartiesPermissions.ADMIN_RANKBYPASS.toString())) {
			if (!r.havePermission(perm.toString())) {
				Rank rr = plugin.getPartyHandler().searchUpRank(tp.getRank(), perm.toString());
				if (rr != null)
					tp.sendMessage(Messages.nopermission_party
							.replace("%rank_name%", rr.getName())
							.replace("%rank_chat%", rr.getChat()));
				else
					tp.sendMessage(Messages.nopermission.replace("%permission%", perm.toString()));
				ret = false;
			}
		}
		return ret;
	}
	public static boolean checkPlayerRank(ThePlayer tp, PartiesPermissions perm) {
		boolean ret = true;
		Rank r = plugin.getPartyHandler().searchRank(tp.getRank());
		if (r != null && !tp.getPlayer().hasPermission(PartiesPermissions.ADMIN_RANKBYPASS.toString())) {
			if (!r.havePermission(perm.toString())) {
				ret = false;
			}
		}
		return ret;
	}
}
