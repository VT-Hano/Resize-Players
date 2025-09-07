package nl.onys.resizeplayers.listeners;

import nl.onys.resizeplayers.ResizePlayers;
import nl.onys.resizeplayers.configs.ArmorConfig;
import nl.onys.resizeplayers.utils.PlayerDataUtils;
import nl.onys.resizeplayers.utils.ScaleUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ArmorListener implements Listener {

    public static final ConcurrentHashMap<UUID, Boolean> isWearingSpecialArmor = new ConcurrentHashMap<>();

    public ArmorListener(ResizePlayers plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : plugin.getServer().getOnlinePlayers()) {
                    checkArmorAndResize(player);
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        isWearingSpecialArmor.remove(event.getPlayer().getUniqueId());
    }

    private void checkArmorAndResize(Player player) {
        FileConfiguration armorConfig = ArmorConfig.get();
        if (!armorConfig.getBoolean("special-armor.enabled", false)) {
            return;
        }

        ItemStack helmet = player.getInventory().getHelmet();
        boolean isCurrentlyWearing = isWearingSpecialHelmet(helmet);
        boolean wasWearing = isWearingSpecialArmor.getOrDefault(player.getUniqueId(), false);

        if (isCurrentlyWearing && !wasWearing) {
            isWearingSpecialArmor.put(player.getUniqueId(), true);

            double currentHeight = PlayerDataUtils.getPlayerHeight(player);
            PlayerDataUtils.savePreArmorHeight(player, currentHeight);

            String resizeMode = armorConfig.getString("special-armor.resize-mode", "percentage").toLowerCase();
            double newHeight;

            if (resizeMode.equals("fixed")) {
                newHeight = armorConfig.getDouble("special-armor.fixed-height-in-blocks", 1.0);
            } else {
                double percentage = armorConfig.getDouble("special-armor.resize-percentage", 10);
                double reductionFactor = 1.0 - (percentage / 100.0);
                newHeight = currentHeight * reductionFactor;
            }

            ScaleUtils.setPlayerScale(player, ScaleUtils.convertBlocksToScale(newHeight), true, true);
            String shrinkMessage = armorConfig.getString("special-armor.shrink-message", "&bYou feel your size changing!");
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', shrinkMessage));

        } else if (!isCurrentlyWearing && wasWearing) {
            isWearingSpecialArmor.put(player.getUniqueId(), false);

            double originalHeight = PlayerDataUtils.getPreArmorHeight(player);
            if (originalHeight != -1) {
                ScaleUtils.setPlayerScale(player, ScaleUtils.convertBlocksToScale(originalHeight), true, true);
                PlayerDataUtils.savePreArmorHeight(player, null);
                String unshrinkMessage = armorConfig.getString("special-armor.unshrink-message", "&bYou've returned to your normal size!");
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', unshrinkMessage));
            }
        }
    }

    private boolean isWearingSpecialHelmet(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return false;
        }

        FileConfiguration config = ArmorConfig.get();
        Material expectedMaterial = Material.matchMaterial(config.getString("special-armor.item-id", ""));
        if (item.getType() != expectedMaterial) {
            return false;
        }

        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            return false;
        }

        String expectedDisplayName = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(config.getString("special-armor.display-name")));
        return meta.getDisplayName().equals(expectedDisplayName);
    }
}