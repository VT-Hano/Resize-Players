package nl.onys.resizeplayers;

import nl.onys.resizeplayers.commands.GetItemCommand;
import nl.onys.resizeplayers.commands.PluginCommand;
import nl.onys.resizeplayers.commands.ResizeCommand;
import nl.onys.resizeplayers.configs.ArmorConfig;
import nl.onys.resizeplayers.configs.PlayerData;
import nl.onys.resizeplayers.integrations.placeholderapi.ResizePlayersPlaceholders;
import nl.onys.resizeplayers.listeners.ArmorListener;
import nl.onys.resizeplayers.listeners.PlayerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ResizePlayers extends JavaPlugin {

    private static ResizePlayers plugin;

    @Override
    public void onEnable() {
        plugin = this;

        String version = getDescription().getVersion();
        String originalAuthor = getDescription().getAuthors().get(0);
        String remakeAuthor = "vn.hano";

        Bukkit.getLogger().info("=============================================");
        Bukkit.getLogger().info("           ResizePlayers (Recode)");
        Bukkit.getLogger().info("");
        Bukkit.getLogger().info("  Version: " + version);
        Bukkit.getLogger().info("  Original Author: " + originalAuthor);
        Bukkit.getLogger().info("  Recode by: " + remakeAuthor);
        Bukkit.getLogger().info("");
        Bukkit.getLogger().info("  Status: Successfully Enabled");
        Bukkit.getLogger().info("=============================================");

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().warning("[ResizePlayers] PlaceholderAPI not found! Some features may not work.");
        } else {
            getLogger().info("[ResizePlayers] PlaceholderAPI found! Hooking into PlaceholderAPI...");
            new ResizePlayersPlaceholders().register();
        }

        saveDefaultConfig();
        ArmorConfig.setup();

        PlayerData.setup();
        PlayerData.get().options().copyDefaults(true);
        PlayerData.save();

        setCommandExecutor("resize", new ResizeCommand());
        setCommandExecutor("resizeplayers", new PluginCommand());
        String getItemCmd = ArmorConfig.get().getString("command.name", "getresizeitem");
        setCommandExecutor(getItemCmd, new GetItemCommand());

        registerEvent(new PlayerJoinListener());
        registerEvent(new ArmorListener(this));
    }

    private void setCommandExecutor(String command, CommandExecutor executor) {
        org.bukkit.command.PluginCommand pluginCommand = plugin.getCommand(command);
        if (pluginCommand != null) {
            pluginCommand.setExecutor(executor);
        } else {
            Bukkit.getLogger().warning("Could not set command executor for " + command + "! Make sure it's defined in plugin.yml.");
        }
    }

    private void registerEvent(Listener listener) {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(listener, this);
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("=============================================");
        Bukkit.getLogger().info("           ResizePlayers (Recode)");
        Bukkit.getLogger().info("");
        Bukkit.getLogger().info("  Status: Disabled");
        Bukkit.getLogger().info("");
        Bukkit.getLogger().info("  Goodbye!");
        Bukkit.getLogger().info("=============================================");
    }

    public static ResizePlayers getPlugin() {
        return plugin;
    }
}