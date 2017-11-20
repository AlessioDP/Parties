package com.alessiodp.parties.utils.addon;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;

import com.alessiodp.parties.Parties;
import com.alessiodp.parties.handlers.LogHandler;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.drops.DropManager;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;

public class MythicMobsHandler implements Listener {
	private Parties plugin;
	private final static Random RANDOM = new Random();
	
	/*
	 * This class is unused because unsupported by MythicMobs.
	 * It cause the drop of NORMAL EXP to the killer and I can't prevent that.
	 * Waiting for MM author response.
	 */
	public MythicMobsHandler(Parties instance) {
		plugin = instance;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@Deprecated
	public static HashMap<String, Double> getExp(double oldExp, Entity entity) {
		HashMap<String, Double> ret = new HashMap<String, Double>();
		try {
			MythicMob mob = MythicMobs.inst()
					.getAPIHelper()
					.getMythicMobInstance(entity)
					.getType();
			if (mob != null) {
				for (String drop : mob.getDrops()) {
					double value = 0;
					float chance = 1;
					try {
						String[] split = drop.split(" ");
						switch (split[0].toLowerCase()) {
						case "exp":
						case "skillapi-exp":
							double newValue = DropManager.parseAmount(split[1]);
							if (split.length > 2)
								chance = Float.valueOf(split[2]);
							
							if (RANDOM.nextFloat() <= chance)
								value = newValue;
							if (value > 0)
								ret.put(split[0].toLowerCase(), value);
						}
					} catch (Exception ex) {
						LogHandler.printError("Something gone wrong on getting EXP via MythicMobs: " + ex.getMessage());
						ex.printStackTrace();
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (ret.isEmpty())
			ret.put("exp", oldExp);
		return ret;
	}
	
	/*
	 * On kill event tester
	@EventHandler
	public void onTest(MythicMobDeathEvent event) {
		if (event.getEntity().hasMetadata("parties_killed")) {
			//ActiveMob aa = ((MythicMob)event.getMob()).getDrops();
			System.out.println("###############");
		}
		System.out.println("CALLED");
	}*/
}
