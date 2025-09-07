package nl.onys.resizeplayers.commands;

import nl.onys.resizeplayers.configs.ArmorConfig;
import nl.onys.resizeplayers.utils.MessageUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GetItemCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtils.onOnlyPlayers(sender);
            return true;
        }

        Player player = (Player) sender;
        FileConfiguration armorConfig = ArmorConfig.get();

        String permission = "resizeplayers.getitem";

        if (!player.hasPermission(permission) && !player.isOp()) {
            MessageUtils.onNoPermission(player, permission);
            return false;
        }

        if (!armorConfig.getBoolean("special-armor.enabled", false)) {
            player.sendMessage(ChatColor.RED + "The special armor feature is disabled in the config.");
            return true;
        }

        String materialName = armorConfig.getString("special-armor.item-id", "DIAMOND_HELMET");
        Material material = Material.matchMaterial(materialName);
        if (material == null) {
            player.sendMessage(ChatColor.RED + "Invalid item ID '" + materialName + "' in armor.yml.");
            return true;
        }

        ItemStack specialHelmet = new ItemStack(material);
        ItemMeta meta = specialHelmet.getItemMeta();

        if (meta != null) {
            String displayName = ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(armorConfig.getString("special-armor.display-name")));
            meta.setDisplayName(displayName);

            List<String> loreLines = armorConfig.getStringList("special-armor.lore");
            List<String> coloredLore = new ArrayList<>();
            String percentage = String.valueOf(armorConfig.getInt("special-armor.resize-percentage"));

            for (String line : loreLines) {
                coloredLore.add(ChatColor.translateAlternateColorCodes('&', line.replace("{percentage}", percentage)));
            }
            meta.setLore(coloredLore);
            specialHelmet.setItemMeta(meta);
        }

        player.getInventory().addItem(specialHelmet);
        String successMessage = armorConfig.getString("command.success-message", "&aYou received the special item!");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', successMessage));

        return true;
    }
}