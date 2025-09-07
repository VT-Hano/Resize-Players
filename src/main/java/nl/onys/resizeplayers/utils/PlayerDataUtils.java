package nl.onys.resizeplayers.utils;

import nl.onys.resizeplayers.ResizePlayers;
import nl.onys.resizeplayers.configs.PlayerData;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class PlayerDataUtils {

    public synchronized static void savePlayerHeight(Player player, double height) {
        ConfigurationSection playerDataSection = PlayerData.get().getConfigurationSection(player.getUniqueId().toString());
        if (playerDataSection == null) {
            playerDataSection = PlayerData.get().createSection(player.getUniqueId().toString());
        }
        playerDataSection.set("username", player.getName());
        playerDataSection.set("height", height);
        PlayerData.save();
    }

    public synchronized static void savePlayerReach(Player player, double blockReach, double entityReach) {
        ConfigurationSection playerDataSection = PlayerData.get().getConfigurationSection(player.getUniqueId().toString());
        if (playerDataSection == null) {
            playerDataSection = PlayerData.get().createSection(player.getUniqueId().toString());
        }
        playerDataSection.set("username", player.getName());
        playerDataSection.set("block-reach", blockReach);
        playerDataSection.set("entity-reach", entityReach);
        PlayerData.save();
    }

    public static String getUUIDFromUsername(String playerName) {
        for (String key : PlayerData.get().getKeys(false)) {
            String username = PlayerData.get().getString(key + ".username");
            if (username != null && username.equalsIgnoreCase(playerName)) {
                return key;
            }
        }
        return null;
    }

    public static void reloadAllOnlinePlayersScaleAndReach() {
        for (Player player : ResizePlayers.getPlugin().getServer().getOnlinePlayers()) {
            reloadPlayerScaleAndReach(player);
        }
    }

    public static void reloadPlayerScaleAndReach(Player player) {
        OfflinePlayer offlinePlayer = player;
        ScaleUtils.setPlayerScale(player, ScaleUtils.convertBlocksToScale(getPlayerHeight(offlinePlayer)), false, false);
        ReachUtils.setPlayerBlockReach(player, getPlayerBlockReach(offlinePlayer));
        ReachUtils.setPlayerEntityReach(player, getPlayerEntityReach(offlinePlayer));
    }

    public static double getPlayerHeight(Player player) {
        return getPlayerHeight((OfflinePlayer) player);
    }

    public static double getPlayerHeight(OfflinePlayer player) {
        ConfigurationSection playerDataSection = PlayerData.get().getConfigurationSection(player.getUniqueId().toString());
        if (playerDataSection == null) return 1.8;
        return playerDataSection.getDouble("height", 1.8);
    }

    public static double getPlayerBlockReach(OfflinePlayer player) {
        ConfigurationSection playerDataSection = PlayerData.get().getConfigurationSection(player.getUniqueId().toString());
        if (playerDataSection == null) return 4.5;
        return playerDataSection.getDouble("block-reach", 4.5);
    }

    public static double getPlayerEntityReach(OfflinePlayer player) {
        ConfigurationSection playerDataSection = PlayerData.get().getConfigurationSection(player.getUniqueId().toString());
        if (playerDataSection == null) return 3.0;
        return playerDataSection.getDouble("entity-reach", 3.0);
    }

    public synchronized static void savePreArmorHeight(Player player, Double height) {
        ConfigurationSection playerDataSection = PlayerData.get().getConfigurationSection(player.getUniqueId().toString());
        if (playerDataSection == null) {
            playerDataSection = PlayerData.get().createSection(player.getUniqueId().toString());
        }
        playerDataSection.set("pre-armor-height", height);
        PlayerData.save();
    }

    public static double getPreArmorHeight(OfflinePlayer player) {
        ConfigurationSection playerDataSection = PlayerData.get().getConfigurationSection(player.getUniqueId().toString());
        if (playerDataSection == null) return -1;
        return playerDataSection.getDouble("pre-armor-height", -1);
    }
}