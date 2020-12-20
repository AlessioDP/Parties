package com.alessiodp.parties.bukkit.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.sub.CommandDebug;

public class BukkitCommandDebug extends CommandDebug {
	
	public BukkitCommandDebug(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	protected String parseDebugExp(String line) {
		return super.parseDebugExp(line)
				.replace("%drop%", ((PartiesPlugin) plugin).getMessageUtils().formatYesNo(BukkitConfigMain.ADDITIONAL_EXP_DROP_ENABLE))
				.replace("%sharing%", ((PartiesPlugin) plugin).getMessageUtils().formatYesNo(BukkitConfigMain.ADDITIONAL_EXP_DROP_SHARING_ENABLE))
				.replace("%sharing_number%", Integer.toString(BukkitConfigMain.ADDITIONAL_EXP_DROP_SHARING_IFMORETHAN))
				.replace("%sharing_range%", Integer.toString(BukkitConfigMain.ADDITIONAL_EXP_DROP_SHARING_RANGE))
				.replace("%sharing_round%", ((PartiesPlugin) plugin).getMessageUtils().formatYesNo(BukkitConfigMain.ADDITIONAL_EXP_DROP_SHARING_ROUND_EXP_DROP))
				.replace("%get_normal%", ((PartiesPlugin) plugin).getMessageUtils().formatYesNo(BukkitConfigMain.ADDITIONAL_EXP_DROP_GET_NORMAL))
				.replace("%get_skillapi%", ((PartiesPlugin) plugin).getMessageUtils().formatYesNo(BukkitConfigMain.ADDITIONAL_EXP_DROP_GET_SKILLAPI))
				.replace("%convert_normal%", BukkitConfigMain.ADDITIONAL_EXP_DROP_CONVERT_NORMAL)
				.replace("%convert_skillapi%", BukkitConfigMain.ADDITIONAL_EXP_DROP_CONVERT_SKILLAPI)
				.replace("%convert_remove%", ((PartiesPlugin) plugin).getMessageUtils().formatYesNo(BukkitConfigMain.ADDITIONAL_EXP_DROP_CONVERT_REMOVEREALEXP))
				.replace("%addons_skillapi%", ((PartiesPlugin) plugin).getMessageUtils().formatYesNo(BukkitConfigMain.ADDITIONAL_EXP_DROP_ADDITIONAL_SKILLAPI_ENABLE))
				.replace("%addons_mmocore%", ((PartiesPlugin) plugin).getMessageUtils().formatYesNo(BukkitConfigMain.ADDITIONAL_EXP_DROP_ADDITIONAL_MMOCORE_ENABLE))
				.replace("%addons_mythicmobs%", ((PartiesPlugin) plugin).getMessageUtils().formatYesNo(BukkitConfigMain.ADDITIONAL_EXP_DROP_ADDITIONAL_MYTHICMOBS_ENABLE));
	}
}
