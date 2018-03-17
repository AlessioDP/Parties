package com.alessiodp.parties.commands;

import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.alessiodp.parties.parties.objects.PartyEntity;
import com.alessiodp.parties.players.PartiesPermission;
import com.alessiodp.parties.players.objects.PartyPlayerEntity;

import lombok.Getter;
import lombok.Setter;

public class CommandData {
	@Getter @Setter private CommandSender sender;
	@Getter @Setter private String commandLabel;
	@Getter @Setter private String[] args;
	
	@Getter @Setter private Player player;
	@Getter @Setter private PartyPlayerEntity partyPlayer;
	@Getter @Setter private PartyEntity party;
	private HashMap<PartiesPermission, Boolean> permissionsPayload;
	
	public CommandData() {
		permissionsPayload = new HashMap<>();
	}
	
	public void addPermission(PartiesPermission perm) {
		permissionsPayload.put(perm, player.hasPermission(perm.toString()));
	}
	
	public boolean havePermission(PartiesPermission perm) {
		if (permissionsPayload.containsKey(perm))
			return permissionsPayload.get(perm);
		return false;
	}
}
