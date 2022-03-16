package com.alessiodp.parties.common.players.objects;

import com.alessiodp.parties.common.PartiesPlugin;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@EqualsAndHashCode
public class RequestCooldown {
	@EqualsAndHashCode.Exclude @Getter private final PartiesPlugin plugin;
	
	@Getter private final long startTime;
	
	@Getter private final UUID sender;
	@Getter private final UUID target;
	@Getter private final int cooldown;
	
	public RequestCooldown(PartiesPlugin plugin, UUID sender, @Nullable UUID target, int cooldown) {
		this.plugin = plugin;
		this.sender = sender;
		this.target = target;
		this.cooldown = cooldown;
		startTime = System.currentTimeMillis() / 1000L;
	}
	
	public boolean isGlobal() {
		return target == null;
	}
	
	public long getDiffTime() {
		return (System.currentTimeMillis() / 1000L) - startTime;
	}
	
	public boolean isWaiting() {
		return getDiffTime() < cooldown;
	}
	
	public long getWaitTime() {
		return getCooldown() - getDiffTime();
	}
}
