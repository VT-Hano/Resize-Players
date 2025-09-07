package nl.onys.resizeplayers.configs;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Objects;

public class ArmorConfig {
    private static FileConfiguration armorConfig;
    private static File file; // Thêm dòng này

    public static void setup() {
        file = new File(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("ResizePlayers")).getDataFolder(), "armor.yml");
        if (!file.exists()) {
            Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("ResizePlayers")).saveResource("armor.yml", false);
        }
        armorConfig = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return armorConfig;
    }

    public synchronized static void reload() {
        try {
            armorConfig = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
            Bukkit.getLogger().severe("[ResizePlayers] Could not reload armor.yml!");
            Bukkit.getLogger().severe("[ResizePlayers] Error: " + e.getMessage());
        }
    }
}