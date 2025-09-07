package nl.onys.resizeplayers.configs;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class PlayerData {
    private static File file;
    private static FileConfiguration playerData;

    public static void setup() {
        file = new File(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("ResizePlayers")).getDataFolder(), "playerData.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().severe("[ResizePlayers] Could not create playerData.yml!");
                Bukkit.getLogger().severe("[ResizePlayers] Error: " + e.getMessage());
            }
        }
        playerData = YamlConfiguration.loadConfiguration(file);
    }

    public synchronized static FileConfiguration get() {
        return playerData;
    }

    public synchronized static void save() {
        try {
            playerData.save(file);
        } catch (IOException e) {
            Bukkit.getLogger().severe("[ResizePlayers] Could not save playerData.yml!");
            Bukkit.getLogger().severe("[ResizePlayers] Error: " + e.getMessage());
        }
    }

    public synchronized static void reload() {
        try {
            playerData = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
            Bukkit.getLogger().severe("[ResizePlayers] Could not reload playerData.yml!");
            Bukkit.getLogger().severe("[ResizePlayers] Error: " + e.getMessage());
        }
    }
}
