package com.alessiodp.parties.bukkit.addons.external;

import com.alessiodp.core.common.configuration.Constants;
import com.alessiodp.parties.bukkit.configuration.data.BukkitConfigMain;
import com.alessiodp.parties.common.PartiesPlugin;
import com.alessiodp.parties.common.parties.objects.PartyImpl;
import com.flowpowered.math.vector.Vector3i;
import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.claim.Claim;
import com.griefdefender.api.claim.TrustType;
import com.griefdefender.api.claim.TrustTypes;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@RequiredArgsConstructor
public class GriefDefenderHandler {
    @NonNull private final PartiesPlugin plugin;
    private static final String ADDON_NAME = "GriefDefender";
    private static boolean active;

    public void init() {
        active = false;
        if (BukkitConfigMain.ADDONS_GRIEFDEFENDER_ENABLE) {
            if (Bukkit.getPluginManager().getPlugin(ADDON_NAME) != null) {
                active = true;

                plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_HOOKED, ADDON_NAME), true);
            } else {
                BukkitConfigMain.ADDONS_GRIEFDEFENDER_ENABLE = false;
                plugin.getLoggerManager().log(String.format(Constants.DEBUG_ADDON_FAILED, ADDON_NAME), true);
            }
        }
    }


    public static Result isManager(Player claimer) {
        if (active) {
            Vector3i vector = Vector3i.from(claimer.getLocation().getBlockX(), claimer.getLocation().getBlockY(), claimer.getLocation().getBlockZ());
            Claim claim = GriefDefender.getCore().getClaimManager(claimer.getWorld().getUID()).getClaimAt(vector);
            if (claim.isWilderness()) {
                return Result.NOEXIST;
            }
            if (!claim.getOwnerName().equalsIgnoreCase(claimer.getName())
                    && (BukkitConfigMain.ADDONS_GRIEFDEFENDER_NEEDOWNER || !claim.getUserTrusts(TrustTypes.MANAGER).contains(claimer.getUniqueId()))) {
                return Result.NOMANAGER;
            }
            return Result.SUCCESS;
        }
        return Result.NOEXIST;
    }

    public static void addPartyPermission(Player claimer, PartyImpl party, PermissionType perm) {
        if (active) {
            Vector3i vector = Vector3i.from(claimer.getLocation().getBlockX(), claimer.getLocation().getBlockY(), claimer.getLocation().getBlockZ());
            Claim claim = GriefDefender.getCore().getClaimManager(claimer.getWorld().getUID()).getClaimAt(vector);
            for (UUID uuid : party.getMembers()) {
                if (claimer.getUniqueId().equals(uuid))
                    continue;
                if (perm.isRemove())
                    claim.removeUserTrust(uuid, perm.convertPermission());
                else
                    claim.addUserTrust(uuid, perm.convertPermission());
            }
        }
    }

    public enum PermissionType {
        ACCESS, BUILD, INVENTORY, MANAGE, REMOVE;

        public boolean isRemove() {
            return this.equals(PermissionType.REMOVE);
        }

        TrustType convertPermission() {
            TrustType ret;
            switch (this) {
                case ACCESS:
                    ret = TrustTypes.ACCESSOR;
                    break;
                case BUILD:
                    ret = TrustTypes.BUILDER;
                    break;
                case INVENTORY:
                    ret = TrustTypes.CONTAINER;
                    break;
                case MANAGE:
                    ret = TrustTypes.MANAGER;
                    break;
                default:
                    ret = null;
                    break;
            }
            return ret;
        }
    }

    public enum Result {
        NOEXIST, NOMANAGER, SUCCESS
    }
}
