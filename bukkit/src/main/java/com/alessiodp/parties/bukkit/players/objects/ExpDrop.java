package com.alessiodp.parties.bukkit.players.objects;

import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;

@RequiredArgsConstructor
public class ExpDrop {
	@Getter private final int normal;
	@Getter private final int skillApi;
	@Getter private final PartyPlayerImpl killer;
	@Getter private final Entity entityKilled;
}
