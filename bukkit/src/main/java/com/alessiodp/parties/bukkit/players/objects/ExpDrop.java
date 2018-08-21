package com.alessiodp.parties.bukkit.players.objects;

import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import lombok.Getter;
import org.bukkit.entity.Entity;

public class ExpDrop {
	@Getter private int normal;
	@Getter private int skillapi;
	@Getter private PartyPlayerImpl killer;
	@Getter private Entity entityKilled;
	
	public ExpDrop(int normal, int skillapi, PartyPlayerImpl killer, Entity entityKilled) {
		this.normal = normal;
		this.skillapi = skillapi;
		this.killer = killer;
		this.entityKilled = entityKilled;
	}
}
