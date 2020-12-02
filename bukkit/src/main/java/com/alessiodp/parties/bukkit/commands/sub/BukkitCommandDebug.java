package com.alessiodp.parties.bukkit.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.parties.common.commands.sub.CommandDebug;

public class BukkitCommandDebug extends CommandDebug {
	
	public BukkitCommandDebug(ADPPlugin plugin, ADPMainCommand mainCommand) {
		super(plugin, mainCommand);
	}
	
	@Override
	protected String parseDebugExp(String line) {
		/* wip exp of debug command
		if (line.contains("%levels_options%")) {
			if (((BukkitPartiesPlugin) plugin).getExpManager().getMode() == ExpManager.ExpMode.PROGRESSIVE) {
				line = line.replace("%levels_options%", Messages.ADDCMD_DEBUG_EXP_LEVEL_OPTIONS_PROGRESSIVE
						.replace("%start%", Integer.toString((int) BukkitConfigMain.ADDITIONAL_EXP_LEVELS_PROGRESSIVE_START))
						.replace("%formula%", ((BukkitPartiesPlugin) plugin).getMessageUtils().formatText(BukkitConfigMain.ADDITIONAL_EXP_LEVELS_PROGRESSIVE_LEVEL_EXP)
						));
			} else {
				line = line.replace("%levels_options%", Messages.ADDCMD_DEBUG_EXP_LEVEL_OPTIONS_FIXED
						.replace("%levels%", Integer.toString(BukkitConfigMain.ADDITIONAL_EXP_LEVELS_FIXED.size()))
				);
			}
		}
		return line
				.replace("%exp%", ((BukkitPartiesPlugin) plugin).getMessageUtils().formatOnOff(BukkitConfigMain.ADDITIONAL_EXP_ENABLE))
				.replace("%levels%", ((BukkitPartiesPlugin) plugin).getMessageUtils().formatYesNo(BukkitConfigMain.ADDITIONAL_EXP_LEVELS_ENABLE))
				.replace("%levels_mode%", BukkitConfigMain.ADDITIONAL_EXP_LEVELS_MODE)
				.replace("%drop%", ((BukkitPartiesPlugin) plugin).getMessageUtils().formatYesNo(BukkitConfigMain.ADDITIONAL_EXP_DROP_ENABLE))
				.replace("%sharing%", ((BukkitPartiesPlugin) plugin).getMessageUtils().formatYesNo(BukkitConfigMain.ADDITIONAL_EXP_DROP_SHARING_ENABLE))
				.replace("%sharing_number%", Integer.toString(BukkitConfigMain.ADDITIONAL_EXP_DROP_SHARING_IFMORETHAN))
				.replace("%sharing_range%", Integer.toString(BukkitConfigMain.ADDITIONAL_EXP_DROP_SHARING_RANGE))
				.replace("%get_normal%", ((BukkitPartiesPlugin) plugin).getMessageUtils().formatYesNo(BukkitConfigMain.ADDITIONAL_EXP_DROP_GET_NORMAL))
				.replace("%get_skillapi%", ((BukkitPartiesPlugin) plugin).getMessageUtils().formatYesNo(BukkitConfigMain.ADDITIONAL_EXP_DROP_GET_SKILLAPI))
				.replace("%convert_normal%", BukkitConfigMain.ADDITIONAL_EXP_DROP_CONVERT_NORMAL)
				.replace("%convert_skillapi%", BukkitConfigMain.ADDITIONAL_EXP_DROP_CONVERT_SKILLAPI)
				.replace("%convert_remove%", ((BukkitPartiesPlugin) plugin).getMessageUtils().formatYesNo(BukkitConfigMain.ADDITIONAL_EXP_DROP_CONVERT_REMOVEREALEXP))
				.replace("%addons_skillapi%", ((BukkitPartiesPlugin) plugin).getMessageUtils().formatYesNo(BukkitConfigMain.ADDITIONAL_EXP_DROP_ADDITIONAL_SKILLAPI_ENABLE))
				.replace("%addons_mmocore%", ((BukkitPartiesPlugin) plugin).getMessageUtils().formatYesNo(BukkitConfigMain.ADDITIONAL_EXP_DROP_ADDITIONAL_MMOCORE_ENABLE))
				.replace("%addons_mythicmobs%", ((BukkitPartiesPlugin) plugin).getMessageUtils().formatYesNo(BukkitConfigMain.ADDITIONAL_EXP_DROP_ADDITIONAL_MYTHICMOBS_ENABLE));
				*/
		return line;
	}
}
