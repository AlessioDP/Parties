package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.PartiesPermission;
import org.jetbrains.annotations.NotNull;

public class PartyCommandType extends PartiesSubCommand implements CommandStart{
    private User sender;
    private PartyPlayerImpl partyPlayer;
    private String syntaxParty;
    private PartyImpl targetParty;

    public PartyCommandType(ADPPlugin plugin, ADPMainCommand mainCommand, User sender, PartyPlayerImpl partyPlayer,
                            String syntaxParty,PartyImpl targetParty) {
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
        this.syntaxParty = syntaxParty;
        this.targetParty = targetParty;
    }
    @Override
    public boolean preRequisites(@NotNull CommandData commandData) {
        return handlePreRequisitesFull(commandData, null, 2, Integer.MAX_VALUE);
    }

    @Override
    public void onCommand(@NotNull CommandData commandData) {
        if (commandData.getArgs().length == 3) {
            targetParty = getPlugin().getPartyManager().getParty(commandData.getArgs()[2]);

            if (targetParty == null) {
                sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_PARTYNOTFOUND
                        .replace("%party%", commandData.getArgs()[2]));

            }
        } else {
            sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
                    .replace("%syntax%", syntaxParty));

        }

    }

    @Override
    public void commandStart() {
        sendMessage(sender, partyPlayer, Messages.ADDCMD_DEBUG_PARTY_HEADER);

        for (String line : Messages.ADDCMD_DEBUG_PARTY_TEXT) {
            sendMessage(sender, partyPlayer, line
                    .replace("%id%", targetParty.getId().toString())
                    .replace("%name%", getPlugin().getMessageUtils().formatText(targetParty.getName()))
                    .replace("%tag%", getPlugin().getMessageUtils().formatText(targetParty.getTag()))
                    .replace("%leader%", targetParty.getLeader() != null ? targetParty.getLeader().toString() : Messages.PARTIES_OPTIONS_NONE)
                    .replace("%members%", Integer.toString(targetParty.getMembers().size()))
                    .replace("%members_online%", Integer.toString(targetParty.getOnlineMembers(true).size()))
                    .replace("%description%", getPlugin().getMessageUtils().formatText(targetParty.getDescription()))
                    .replace("%motd_size%", Integer.toString(targetParty.getMotd() != null ? targetParty.getMotd().length() : 0))
                    .replace("%homes%", Integer.toString(targetParty.getHomes().size()))
                    .replace("%kills%", Integer.toString(targetParty.getKills()))
                    .replace("%password%", getPlugin().getMessageUtils().formatYesNo(targetParty.getPassword() != null))
                    .replace("%protection%", getPlugin().getMessageUtils().formatYesNo(targetParty.getProtection()))
                    .replace("%follow%", getPlugin().getMessageUtils().formatYesNo(targetParty.isFollowEnabled()))
                    .replace("%open%", getPlugin().getMessageUtils().formatYesNo(targetParty.isOpen()))
                    .replace("%color%", (targetParty.getColor() != null ? targetParty.getColor().getName() : Messages.PARTIES_OPTIONS_NONE))
                    .replace("%color_active%", (targetParty.getCurrentColor() != null ? targetParty.getCurrentColor().getName() : Messages.PARTIES_OPTIONS_NONE))
                    .replace("%color_dynamic%", (targetParty.getDynamicColor() != null ? targetParty.getDynamicColor().getName() : Messages.PARTIES_OPTIONS_NONE))
                    .replace("%experience%", Integer.toString((int) targetParty.getExperience()))
            );
        }

    }
}
