package com.alessiodp.parties.bukkit.commands.sub;

import com.alessiodp.core.common.ADPPlugin;
import com.alessiodp.core.common.commands.utils.ADPMainCommand;
import com.alessiodp.core.common.commands.utils.CommandData;
import com.alessiodp.core.common.user.User;
import com.alessiodp.parties.bukkit.addons.external.GriefDefenderHandler;
import com.alessiodp.parties.bukkit.commands.list.BukkitCommands;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.bukkit.configuration.data.BukkitMessages;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.commands.utils.PartiesCommandData;
import com.alessiodp.parties.common.commands.utils.PartiesSubCommand;
import com.alessiodp.parties.common.configuration.PartiesConstants;
import com.alessiodp.parties.common.configuration.data.Messages;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.alessiodp.parties.common.utils.PartiesPermission;
import com.alessiodp.parties.common.players.objects.PartyPlayerImpl;
import com.alessiodp.parties.common.utils.EconomyManager;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class BukkitCommandGDClaim extends PartiesSubCommand {

    public BukkitCommandGDClaim(ADPPlugin plugin, ADPMainCommand mainCommand) {
        super(
                plugin,
                mainCommand,
                BukkitCommands.CLAIM,
                PartiesPermission.USER_CLAIM,
                BukkitConfigMain.COMMANDS_CMD_CLAIM,
                false
        );

        syntax = String.format("%s <%s>",
                baseSyntax(),
                BukkitMessages.PARTIES_SYNTAX_PERMISSION
        );

        description = BukkitMessages.HELP_ADDITIONAL_DESCRIPTIONS_CLAIM;
        help = BukkitMessages.HELP_ADDITIONAL_COMMANDS_CLAIM;
    }

    @Override
    public boolean preRequisites(CommandData commandData) {
        User sender = commandData.getSender();
        PartyPlayerImpl partyPlayer = ((PartiesPlugin) plugin).getPlayerManager().getPlayer(sender.getUUID());

        // Checks for command prerequisites
        if (!sender.hasPermission(permission)) {
            sendNoPermissionMessage(partyPlayer, permission);
            return false;
        }

        PartyImpl party = ((PartiesPlugin) plugin).getPartyManager().getParty(partyPlayer.getPartyId());
        if (party == null) {
            sendMessage(sender, partyPlayer, Messages.PARTIES_COMMON_NOTINPARTY);
            return false;
        }

        if (!((PartiesPlugin) plugin).getRankManager().checkPlayerRankAlerter(partyPlayer, PartiesPermission.PRIVATE_CLAIM))
            return false;

        if (commandData.getArgs().length != 2) {
            sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
                    .replace("%syntax%", syntax));
            sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_CLAIM_ALLOWED_PERMISSIONS);
            return false;
        }

        ((PartiesCommandData) commandData).setPartyPlayer(partyPlayer);
        ((PartiesCommandData) commandData).setParty(party);
        return true;
    }

    @Override
    public void onCommand(CommandData commandData) {
        User sender = commandData.getSender();
        PartyPlayerImpl partyPlayer = ((PartiesCommandData) commandData).getPartyPlayer();
        PartyImpl party = ((PartiesCommandData) commandData).getParty();

        // Command handling
        Selection selection = Selection.FAILED_GENERAL;
        if (commandData.getArgs()[1].equalsIgnoreCase(BukkitConfigMain.ADDONS_GRIEFDEFENDER_CMD_TRUST))
            selection = Selection.TRUST;
        else if (commandData.getArgs()[1].equalsIgnoreCase(BukkitConfigMain.ADDONS_GRIEFDEFENDER_CMD_CONTAINER))
            selection = Selection.CONTAINER;
        else if (commandData.getArgs()[1].equalsIgnoreCase(BukkitConfigMain.ADDONS_GRIEFDEFENDER_CMD_ACCESS))
            selection = Selection.ACCESS;
        else if (commandData.getArgs()[1].equalsIgnoreCase(BukkitConfigMain.ADDONS_GRIEFDEFENDER_CMD_MANAGER))
            selection = Selection.MANAGER;
        else if (commandData.getArgs()[1].equalsIgnoreCase(BukkitConfigMain.ADDONS_GRIEFDEFENDER_CMD_REMOVE))
            selection = Selection.REMOVE;

        if (!selection.equals(Selection.FAILED_GENERAL)) {
            GriefDefenderHandler.Result res = GriefDefenderHandler.isManager(Bukkit.getPlayer(commandData.getSender().getUUID()));
            switch (res) {
                case NOEXIST:
                    selection = Selection.FAILED_NOEXIST;
                    break;
                case NOMANAGER:
                    selection = Selection.FAILED_NOMANAGER;
                    break;
                default:
                    // Success, selection it's okay
                    break;
            }
        }

        if (((PartiesPlugin) plugin).getEconomyManager().payCommand(EconomyManager.PaidCommand.CLAIM, partyPlayer, commandData.getCommandLabel(), commandData.getArgs()))
            return;

        // Command starts
        switch (selection) {
            case FAILED_NOEXIST:
                // Return: No exist claim
                sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_CLAIM_CLAIMNOEXISTS);
                break;
            case FAILED_NOMANAGER:
                // Return: No manager
                sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_CLAIM_NOMANAGER);
                break;
            case FAILED_GENERAL:
                // Return: Wrong command
                sendMessage(sender, partyPlayer, Messages.PARTIES_SYNTAX_WRONG_MESSAGE
                        .replace("%syntax%", syntax));
                sendMessage(sender, partyPlayer, BukkitMessages.ADDCMD_CLAIM_ALLOWED_PERMISSIONS);
                break;
            default:
                GriefDefenderHandler.addPartyPermission(Bukkit.getPlayer(commandData.getSender().getUUID()), party, selection.getGDPermission());
                sendMessage(sender, partyPlayer, selection.getGDPermission().isRemove() ? BukkitMessages.ADDCMD_CLAIM_REMOVED : BukkitMessages.ADDCMD_CLAIM_CLAIMED);

                plugin.getLoggerManager().logDebug(String.format(PartiesConstants.DEBUG_CMD_CLAIM,
                        partyPlayer.getName(), selection.name()), true);
                break;
        }
    }

    @Override
    public List<String> onTabComplete(User sender, String[] args) {
        List<String> ret = new ArrayList<>();
        if (args.length == 2) {
            ret.add(BukkitConfigMain.ADDONS_GRIEFPREVENTION_CMD_ACCESS);
            ret.add(BukkitConfigMain.ADDONS_GRIEFPREVENTION_CMD_CONTAINER);
            ret.add(BukkitConfigMain.ADDONS_GRIEFPREVENTION_CMD_TRUST);
            ret.add(BukkitConfigMain.ADDONS_GRIEFDEFENDER_CMD_MANAGER);
            ret.add(BukkitConfigMain.ADDONS_GRIEFPREVENTION_CMD_REMOVE);
        }
        return ret;
    }

    private enum Selection {
        TRUST, CONTAINER, ACCESS, MANAGER, REMOVE, FAILED_GENERAL, FAILED_NOEXIST, FAILED_NOMANAGER;

        GriefDefenderHandler.PermissionType getGDPermission() {
            GriefDefenderHandler.PermissionType ret;
            switch (this) {
                case TRUST:
                    ret = GriefDefenderHandler.PermissionType.BUILD;
                    break;
                case CONTAINER:
                    ret = GriefDefenderHandler.PermissionType.INVENTORY;
                    break;
                case ACCESS:
                    ret = GriefDefenderHandler.PermissionType.ACCESS;
                    break;
                case MANAGER:
                    ret = GriefDefenderHandler.PermissionType.MANAGE;
                    break;
                default:
                    ret = GriefDefenderHandler.PermissionType.REMOVE;
                    break;
            }
            return ret;
        }
    }
}
