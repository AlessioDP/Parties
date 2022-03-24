package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.ExpManager;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.PartiesPermission;
import org.jetbrains.annotations.NotNull;

public class ExpCommandType extends PartiesSubCommand implements CommandStart{
    private User sender;
    private PartyPlayerImpl partyPlayer;
    private String syntaxExp;

    public ExpCommandType(ADPPlugin plugin, ADPMainCommand mainCommand, User sender, PartyPlayerImpl partyPlayer, String syntaxExp) {
        super(
                plugin,
                mainCommand,
                CommonCommands.DEBUG,
                PartiesPermission.ADMIN_DEBUG,
                ConfigMain.COMMANDS_SUB_DEBUG,
                true
        );
        this.sender = sender;
        this.partyPlayer = partyPlayer;
        this.syntaxExp = syntaxExp;
    }
    @Override
    public boolean preRequisites(@NotNull CommandData commandData) {
        return handlePreRequisitesFull(commandData, null, 2, Integer.MAX_VALUE);
    }

    @Override
    public void onCommand(@NotNull CommandData commandData) {
        if (commandData.getArgs().length != 2) {
            sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
                    .replace("%syntax%", syntaxExp));
        }

    }

    @Override
    public void commandStart() {
        sendMessage(sender, partyPlayer, Messages.ADDCMD_DEBUG_EXP_HEADER);

				for (String line : Messages.ADDCMD_DEBUG_EXP_TEXT) {
					sendMessage(sender, partyPlayer, parseDebugExp(line));
				}
    }

    protected String parseDebugExp(String line) {
        String newLine = line;
        if (newLine.contains("%mode_options%")) {
            if (getPlugin().getExpManager().getMode() == ExpManager.ExpMode.PROGRESSIVE) {

                newLine = newLine.replace("%mode_options%", Messages.ADDCMD_DEBUG_EXP_MODE_OPTIONS_PROGRESSIVE
                        .replace("%start%", Integer.toString((int) ConfigMain.ADDITIONAL_EXP_PROGRESSIVE_START))
                        .replace("%formula%", getPlugin().getMessageUtils().formatText(ConfigMain.ADDITIONAL_EXP_PROGRESSIVE_LEVEL_EXP)
                        ));
            } else {
                newLine = newLine.replace("%mode_options%", Messages.ADDCMD_DEBUG_EXP_MODE_OPTIONS_FIXED
                        .replace("%repeat%", getPlugin().getMessageUtils().formatYesNo(ConfigMain.ADDITIONAL_EXP_FIXED_REPEAT))
                        .replace("%levels%", Integer.toString(ConfigMain.ADDITIONAL_EXP_FIXED_LIST.size())
                        ));
            }
        }
        return newLine
                .replace("%exp%", getPlugin().getMessageUtils().formatEnabledDisabled(ConfigMain.ADDITIONAL_EXP_ENABLE))
                .replace("%earn%", getPlugin().getMessageUtils().formatYesNo(ConfigMain.ADDITIONAL_EXP_EARN_FROM_MOBS))
                .replace("%mode%", ConfigMain.ADDITIONAL_EXP_MODE);
    }
}
