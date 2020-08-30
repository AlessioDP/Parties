package com.alessiodp.parties.bukkit.players.objects;

import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;

@AllArgsConstructor
public class ExpDrop {
	@Getter private final PartyPlayerImpl killer;
	@Getter private final Entity entityKilled;
	@Getter @Setter private int normal;
	@Getter @Setter private int skillApi;
}
