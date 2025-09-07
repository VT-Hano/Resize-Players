package nl.onys.resizeplayers.listeners;

import nl.onys.resizeplayers.ResizePlayers;
import nl.onys.resizeplayers.configs.PlayerData;
import nl.onys.resizeplayers.utils.PlayerDataUtils;
import nl.onys.resizeplayers.utils.ReachUtils;
import nl.onys.resizeplayers.utils.ScaleUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration pluginConfig = ResizePlayers.getPlugin().getConfig();
        ConfigurationSection configurationSection = PlayerData.get().getConfigurationSection(player.getUniqueId().toString());

        if (configurationSection == null) {
            PlayerDataUtils.savePlayerHeight(player, 1.8);
            PlayerDataUtils.savePlayerReach(player, 4.5, 3.0);
        } else {
            double blockHeight = configurationSection.getDouble("height", 1.8);
            double blockReach = configurationSection.getDouble("block-reach", 4.5);
            double entityReach = configurationSection.getDouble("entity-reach", 3.0);

            ScaleUtils.setPlayerScale(player, ScaleUtils.convertBlocksToScale(blockHeight), false, false);
            if (pluginConfig.getBoolean("affects-reach")) {
                ReachUtils.setPlayerEntityReach(player, entityReach);
                ReachUtils.setPlayerBlockReach(player, blockReach);
            }

            double preArmorHeight = PlayerDataUtils.getPreArmorHeight(player);
            if (preArmorHeight != -1) {
                ArmorListener.isWearingSpecialArmor.put(player.getUniqueId(), true);
            }
        }
    }
}