package com.alessiodp.parties.common.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.list.CommonCommands;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConfigurationManager;
import com.alessiodp.parties.common.configuration.data.ConfigMain;
import com.alessiodp.parties.common.configuration.data.ConfigParties;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.players.objects.PartyRankImpl;
import com.alessiodp.parties.common.utils.PartiesPermission;
import org.jetbrains.annotations.NotNull;

public class ConfigCommandType extends PartiesSubCommand implements CommandStart {

    private User sender;
    private PartyPlayerImpl partyPlayer;
    private String syntaxConfig;

    public ConfigCommandType(ADPPlugin plugin, ADPMainCommand mainCommand, User sender, PartyPlayerImpl partyPlayer, String syntaxConfig) {
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
        this.syntaxConfig = syntaxConfig;
    }

    @Override
    public void commandStart() {
        sendMessage(sender, partyPlayer, Messages.ADDCMD_DEBUG_CONFIG_HEADER);

        StringBuilder ranks = new StringBuilder();
        for (PartyRankImpl rank : ConfigParties.RANK_LIST) {
            if (ranks.length() > 0)
                ranks.append(Messages.ADDCMD_DEBUG_CONFIG_RANK_SEPARATOR);
            ranks.append(rank.parseWithPlaceholders((PartiesPlugin) plugin, Messages.ADDCMD_DEBUG_CONFIG_RANK_FORMAT));
        }

        for (String line : Messages.ADDCMD_DEBUG_CONFIG_TEXT) {
            sendMessage(sender, partyPlayer, line
                    .replace("%outdated_config%", getPlugin().getMessageUtils().formatYesNo(((PartiesConfigurationManager) plugin.getConfigurationManager()).getConfigMain().isOutdated()))
                    .replace("%outdated_parties%", getPlugin().getMessageUtils().formatYesNo(((PartiesConfigurationManager) plugin.getConfigurationManager()).getConfigParties().isOutdated()))
                    .replace("%outdated_messages%", getPlugin().getMessageUtils().formatYesNo(((PartiesConfigurationManager) plugin.getConfigurationManager()).getMessages().isOutdated()))
                    .replace("%storage%", plugin.getDatabaseManager().getDatabaseType().toString())
                    .replace("%ranks%", ranks.toString())
            );
        }
    }

    @Override
    public boolean preRequisites(@NotNull CommandData commandData) {
        return handlePreRequisitesFull(commandData, null, 2, Integer.MAX_VALUE);
    }

    @Override
    public void onCommand(@NotNull CommandData commandData) {
        if (commandData.getArgs().length != 2) {
            sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
                    .replace("%syntax%", syntaxConfig));
        }
    }
}
