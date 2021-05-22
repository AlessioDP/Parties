package com.alessiodp.parties.common.parties;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.RequestCooldown;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CooldownManager {
	protected final PartiesPlugin plugin;
	private HashMap<UUID, List<RequestCooldown>> askCooldown;
	private HashMap<UUID, Long> chatCooldown;
	private HashMap<UUID, Long> homeCooldown;
	private HashMap<UUID, List<RequestCooldown>> inviteCooldown;
	private HashMap<UUID, List<RequestCooldown>> inviteAfterLeaveCooldown;
	private HashMap<UUID, Long> renameCooldown;
	private HashMap<UUID, Long> setHomeCooldown;
	private HashMap<UUID, Long> teleportCooldown;
	
	public CooldownManager(@NonNull PartiesPlugin plugin) {
		this.plugin = plugin;
		reload();
	}
	
	protected void reload() {
		askCooldown = new HashMap<>();
		chatCooldown = new HashMap<>();
		homeCooldown = new HashMap<>();
		inviteCooldown = new HashMap<>();
		inviteAfterLeaveCooldown = new HashMap<>();
		renameCooldown = new HashMap<>();
		setHomeCooldown = new HashMap<>();
		teleportCooldown = new HashMap<>();
	}
	
	private RequestCooldown getRequestCooldown(List<RequestCooldown> list, UUID target) {
		RequestCooldown ret = null;
		if (list != null) {
			for (RequestCooldown ic : list) {
				if (ic.isGlobal()) {
					// Global
					if (ic.isWaiting()) {
						// Get the highest one
						ret = ret == null ? ic : (ic.getWaitTime() > ret.getWaitTime() ? ic : ret);
					}
				} else {
					// Individual
					if (target != null
							&& target.equals(ic.getTarget())
							&& ic.isWaiting()) {
						// Get the highest one
						ret = ret == null ? ic : (ic.getWaitTime() > ret.getWaitTime() ? ic : ret);
					}
				}
			}
		}
		return ret;
	}
	
	public void insertRequestCooldown(HashMap<UUID, List<RequestCooldown>> map, UUID player, UUID target, int seconds, String debugMessage) {
		if (seconds > 0) {
			List<RequestCooldown> list = map.get(player);
			if (list == null)
				list = new ArrayList<>();
			RequestCooldown ic = new RequestCooldown(
					plugin,
					player,
					target,
					seconds
			);
			list.add(ic);
			map.put(player, list);
			
			plugin.getScheduler().scheduleAsyncLater(() -> {
				List<RequestCooldown> newList = map.get(player);
				if (newList != null) {
					newList.remove(ic);
					
					if (newList.isEmpty()) {
						map.remove(player);
					} else {
						map.put(player, newList);
					}
				}
				
				plugin.getLoggerManager().logDebug(String.format(debugMessage, player.toString()), true);
			}, seconds, TimeUnit.SECONDS);
		}
	}
	
	public RequestCooldown canAsk(UUID player, UUID targetParty) {
		return getRequestCooldown(askCooldown.get(player), targetParty);
	}
	
	public void startAskCooldown(UUID player, UUID targetParty, int seconds) {
		insertRequestCooldown(askCooldown, player, targetParty, seconds, PartiesConstants.DEBUG_TASK_ASK_COOLDOWN_EXPIRED);
	}
	
	public long canChat(UUID player, int cooldown) {
		if (player != null) {
			return calculateCooldown(chatCooldown.get(player), cooldown);
		}
		return 0;
	}
	
	public void startChatCooldown(UUID player, int seconds) {
		if (seconds > 0) {
			long unixNow = System.currentTimeMillis() / 1000L;
			
			chatCooldown.put(player, unixNow);
			
			plugin.getScheduler().scheduleAsyncLater(() -> {
				chatCooldown.remove(player);
				
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_TASK_CHAT_EXPIRED, player.toString()), true);
			}, seconds, TimeUnit.SECONDS);
		}
	}
	
	public RequestCooldown canInvite(UUID player, UUID targetPlayer) {
		return getRequestCooldown(inviteCooldown.get(player), targetPlayer);
	}
	
	public void startInviteCooldown(UUID player, UUID targetPlayer, int seconds) {
		insertRequestCooldown(inviteCooldown, player, targetPlayer, seconds, PartiesConstants.DEBUG_TASK_INVITE_COOLDOWN_EXPIRED);
	}
	
	public RequestCooldown canInviteAfterLeave(UUID targetPlayer, UUID party) {
		return getRequestCooldown(inviteAfterLeaveCooldown.get(targetPlayer), party);
	}
	
	public void startInviteAfterLeave(UUID targetPlayer, UUID party, int seconds) {
		insertRequestCooldown(inviteAfterLeaveCooldown, targetPlayer, party, seconds, PartiesConstants.DEBUG_TASK_INVITE_COOLDOWN_ON_LEAVE_EXPIRED);
	}
	
	
	public long canRename(PartyImpl party, int cooldown) {
		if (party != null) {
			return calculateCooldown(renameCooldown.get(party.getId()), cooldown);
		}
		return 0;
	}
	
	public void startRenameCooldown(PartyImpl party, int seconds) {
		if (seconds > 0) {
			long unixNow = System.currentTimeMillis() / 1000L;
			
			renameCooldown.put(party.getId(), unixNow);
			
			plugin.getScheduler().scheduleAsyncLater(() -> {
				renameCooldown.remove(party.getId());
				
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_TASK_RENAME_EXPIRED, party.getId().toString()), true);
			}, seconds, TimeUnit.SECONDS);
		}
	}
	
	public long canTeleport(UUID player, int cooldown) {
		if (player != null) {
			return calculateCooldown(teleportCooldown.get(player), cooldown);
		}
		return 0;
	}
	
	public void startTeleportCooldown(UUID player, int seconds) {
		if (player != null && seconds > 0) {
			long unixNow = System.currentTimeMillis() / 1000L;
			
			teleportCooldown.put(player, unixNow);
			
			plugin.getScheduler().scheduleAsyncLater(() -> {
				teleportCooldown.remove(player);
				
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_TASK_TELEPORT_EXPIRED, player.toString()), true);
			}, seconds, TimeUnit.SECONDS);
		}
	}
	
	protected long calculateCooldown(Long unixBefore, int cooldown) {
		long unixNow = System.currentTimeMillis() / 1000L;
		if (unixBefore != null && (unixNow - unixBefore) < cooldown) {
			return cooldown - (unixNow - unixBefore);
		}
		return 0;
	}
	
	public long canHome(UUID player, int cooldown) {
		if (player != null) {
			return calculateCooldown(homeCooldown.get(player), cooldown);
		}
		return 0;
	}
	
	public void startHomeCooldown(UUID player, int seconds) {
		if (player != null && seconds > 0) {
			long unixNow = System.currentTimeMillis() / 1000L;
			
			homeCooldown.put(player, unixNow);
			
			plugin.getScheduler().scheduleAsyncLater(() -> {
				homeCooldown.remove(player);
				
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_TASK_HOME_EXPIRED, player.toString()), true);
			}, seconds, TimeUnit.SECONDS);
		}
	}
	
	public void resetHomeCooldown(UUID player) {
		homeCooldown.remove(player);
	}
	
	public long canSetHome(UUID player, int cooldown) {
		if (player != null) {
			return calculateCooldown(setHomeCooldown.get(player), cooldown);
		}
		return 0;
	}
	
	public void startSetHomeCooldown(UUID player, int seconds) {
		if (player != null && seconds > 0) {
			long unixNow = System.currentTimeMillis() / 1000L;
			
			setHomeCooldown.put(player, unixNow);
			
			plugin.getScheduler().scheduleAsyncLater(() -> {
				setHomeCooldown.remove(player);
				
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_TASK_SETHOME_EXPIRED, player.toString()), true);
			}, seconds, TimeUnit.SECONDS);
		}
	}
}
