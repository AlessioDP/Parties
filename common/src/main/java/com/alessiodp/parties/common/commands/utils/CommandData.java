package com.alessiodp.parties.common.commands.utils;

import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.user.User;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class CommandData {
	@Getter @Setter private User sender;
	@Getter @Setter private String commandLabel;
	@Getter @Setter private String[] args;
	
	@Getter @Setter private PartyPlayerImpl partyPlayer;
	@Getter @Setter private PartyImpl party;
	private HashMap<PartiesPermission, Boolean> permissionsPayload;
	
	public CommandData() {
		permissionsPayload = new HashMap<>();
	}
	
	public void addPermission(PartiesPermission perm) {
		permissionsPayload.put(perm, sender.hasPermission(perm.toString()));
	}
	
	public boolean havePermission(PartiesPermission perm) {
		if (permissionsPayload.containsKey(perm))
			return permissionsPayload.get(perm);
		return false;
	}
}
