package com.alessiodp.parties.common.parties;

import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.players.objects.RequestCooldown;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CooldownManager {
	protected final PartiesPlugin plugin;
	
	private HashMap<Action, HashMap<UUID, Long>> actions;
	private HashMap<MultiAction, HashMap<UUID, List<RequestCooldown>>> multiActions;
	
	public CooldownManager(@NotNull PartiesPlugin plugin) {
		this.plugin = plugin;
		reload();
	}
	
	protected void reload() {
		actions = new HashMap<>();
		multiActions = new HashMap<>();
		
		for (Action action : Action.values())
			actions.put(action, new HashMap<>());
		
		for (MultiAction multiAction : MultiAction.values())
			multiActions.put(multiAction, new HashMap<>());
	}
	
	/**
	 * Check if the user can perform the action
	 *
	 * @param action the action to perform
	 * @param subject the subject
	 * @param cooldown the cooldown of the action
	 * @return the time remaining of the cooldown
	 */
	public long canAction(Action action, UUID subject, int cooldown) {
		if (subject != null) {
			return calculateCooldown(actions.get(action).get(subject), cooldown);
		}
		return 0;
	}
	
	/**
	 * Check if the user can perform the multi action. Returning the {@link RequestCooldown}.
	 *
	 * @param multiAction the multi action to perform
	 * @param subject the subject
	 * @param target the target
	 * @return the request cooldown
	 */
	public RequestCooldown canMultiAction(MultiAction multiAction, UUID subject, UUID target) {
		return subject != null ? getRequestCooldown(multiActions.get(multiAction).get(subject), target) : null;
	}
	
	/**
	 * Start the action cooldown for the given player
	 *
	 * @param action the action to perform
	 * @param subject the subject
	 * @param seconds cooldown time in seconds
	 */
	public void startAction(Action action, UUID subject, int seconds) {
		if (subject != null && seconds > 0) {
			long unixNow = System.currentTimeMillis() / 1000L;
			
			actions.get(action).put(subject, unixNow);
			
			plugin.getScheduler().scheduleAsyncLater(() -> {
				actions.get(action).remove(subject);
				
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_TASK_ACTION_EXPIRED, action.name(), subject), true);
			}, seconds, TimeUnit.SECONDS);
		}
	}
	
	/**
	 * Start the multi action cooldown for the given player
	 *
	 * @param multiAction the multi action to perform
	 * @param subject the subject
	 * @param target the target
	 * @param seconds cooldown time in seconds
	 */
	public void startMultiAction(MultiAction multiAction, UUID subject, UUID target, int seconds) {
		insertRequestCooldown(multiActions.get(multiAction), subject, target, seconds, multiAction);
	}
	
	protected long calculateCooldown(Long unixBefore, int cooldown) {
		long unixNow = System.currentTimeMillis() / 1000L;
		if (unixBefore != null && (unixNow - unixBefore) < cooldown) {
			return cooldown - (unixNow - unixBefore);
		}
		return 0;
	}
	
	protected RequestCooldown getRequestCooldown(List<RequestCooldown> list, UUID target) {
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
	
	protected void insertRequestCooldown(HashMap<UUID, List<RequestCooldown>> map, UUID subject, UUID target, int seconds, MultiAction multiAction) {
		if (seconds > 0) {
			List<RequestCooldown> list = map.get(subject);
			if (list == null)
				list = new ArrayList<>();
			RequestCooldown ic = new RequestCooldown(
					plugin,
					subject,
					target,
					seconds
			);
			list.add(ic);
			map.put(subject, list);
			
			plugin.getScheduler().scheduleAsyncLater(() -> {
				List<RequestCooldown> newList = map.get(subject);
				if (newList != null) {
					newList.remove(ic);
					
					if (newList.isEmpty()) {
						map.remove(subject);
					} else {
						map.put(subject, newList);
					}
				}
				
				plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_TASK_ACTION_EXPIRED, multiAction.name(), subject.toString()), true);
			}, seconds, TimeUnit.SECONDS);
		}
	}
	
	
	/**
	 * An action that can be executed only one time
	 */
	public enum Action {
		CHAT, CLOSE, HOME, OPEN, RENAME, SETHOME, TELEPORT
	}
	
	/**
	 * An action that can be executed multiple times with different values
	 */
	public enum MultiAction {
		ASK, INVITE, INVITE_AFTER_LEAVE
	}
}
