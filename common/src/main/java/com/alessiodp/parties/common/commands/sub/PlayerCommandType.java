package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.addons.external.LLAPIHandler;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.PartiesPermission;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

public class PlayerCommandType extends PartiesSubCommand implements CommandStart{
    private User sender;
    private PartyPlayerImpl partyPlayer;
    private String syntaxPlayer;
    private PartyPlayerImpl targetPlayer;
    private String playerName;

    public PlayerCommandType(ADPPlugin plugin, ADPMainCommand mainCommand, User sender,
                             PartyPlayerImpl partyPlayer, String syntaxPlayer, PartyPlayerImpl targetPlayer) {
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
        this.syntaxPlayer = syntaxPlayer;
        this.targetPlayer = targetPlayer;
    }
    @Override
    public boolean preRequisites(@NotNull CommandData commandData) {
        return handlePreRequisitesFull(commandData, null, 2, Integer.MAX_VALUE);
    }

    @Override
    public void onCommand(@NotNull CommandData commandData) {
        if (commandData.getArgs().length == 2) {
            targetPlayer = partyPlayer;
        } else if (commandData.getArgs().length == 3) {
            playerName = commandData.getArgs()[2];

            User targetUser = plugin.getPlayerByName(playerName);
            if (targetUser != null) {
                targetPlayer = getPlugin().getPlayerManager().getPlayer(targetUser.getUUID());
            } else {
                Set<UUID> targetPlayersUuid = LLAPIHandler.getPlayerByName(playerName);
                if (targetPlayersUuid.size() > 0) {
                    targetPlayer = getPlugin().getPlayerManager().getPlayer(targetPlayersUuid.iterator().next());
                } else {
                    // Not found
                    sendMessage(sender, partyPlayer, Messages.ADDCMD_DEBUG_PLAYER_PLAYER_OFFLINE
                            .replace("%player%", playerName));
                    return;
                }
            }
        } else {
            sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
                    .replace("%syntax%", syntaxPlayer));
            return;
        }

    }

    @Override
    public void commandStart() {
        User targetUser = plugin.getPlayer(targetPlayer.getPlayerUUID());
        sendMessage(sender, partyPlayer, Messages.ADDCMD_DEBUG_PLAYER_HEADER);

        for (String line : Messages.ADDCMD_DEBUG_PLAYER_TEXT) {
            sendMessage(sender, partyPlayer, line
                    .replace("%uuid%", targetPlayer.getPlayerUUID().toString())
                    .replace("%name%", targetPlayer.getName())
                    .replace("%rank%", Integer.toString(targetPlayer.getRank()))
                    .replace("%party%", targetPlayer.getPartyId() != null ? targetPlayer.getPartyId().toString() : Messages.PARTIES_OPTIONS_NONE)
                    .replace("%chat%", getPlugin().getMessageUtils().formatYesNo(targetPlayer.isChatParty()))
                    .replace("%spy%", getPlugin().getMessageUtils().formatYesNo(targetPlayer.isSpy()))
                    .replace("%muted%", getPlugin().getMessageUtils().formatYesNo(targetPlayer.isMuted()))
                    .replace("%protection_bypass%", getPlugin().getMessageUtils().formatYesNo(targetUser != null && targetUser.hasPermission(PartiesPermission.ADMIN_PROTECTION_BYPASS)))
            );
        }


    }
}
