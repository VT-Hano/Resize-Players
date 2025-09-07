package nl.onys.resizeplayers.commands;

import nl.onys.resizeplayers.ResizePlayers;
import nl.onys.resizeplayers.configs.PlayerData;
import nl.onys.resizeplayers.utils.MessageUtils;
import nl.onys.resizeplayers.utils.PlayerDataUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PluginCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] args) {
        // Sorry I know this function is very messy
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            MessageUtils.onHelpMessage(commandSender);
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            if (commandSender.hasPermission("resizeplayers.reload") || commandSender.isOp()) {
                ResizePlayers.getPlugin().reloadConfig();
                PlayerData.reload();
                nl.onys.resizeplayers.configs.ArmorConfig.reload();
                PlayerDataUtils.reloadAllOnlinePlayersScaleAndReach();
                MessageUtils.onConfigReloaded(commandSender);
            } else {
                MessageUtils.onNoPermission(commandSender, "resizeplayers.reload");
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("info")) {
            if (args.length == 1) {
                if (!commandSender.hasPermission("resizeplayers.info.self") && !commandSender.isOp()) {
                    MessageUtils.onNoPermission(commandSender, "resizeplayers.info.self");
                    return false;
                }
                if (!(commandSender instanceof Player)) {
                    MessageUtils.onOnlyPlayers(commandSender);
                    return true;
                }
                Player player = (Player) commandSender;
                return sendInfoMessage(commandSender, player.getName());
            } else if (args.length == 2) {
                if (!commandSender.hasPermission("resizeplayers.info.others") && !commandSender.isOp()) {
                    MessageUtils.onNoPermission(commandSender, "resizeplayers.info.others");
                    return false;
                }
                return sendInfoMessage(commandSender, args[1]);
            } else {
                MessageUtils.onWrongUsage(commandSender, "/resizeplayers info [player]");
            }
        }
        return false;
    }

    /**
     * Sends player height and reach information to the command sender
     * @param commandSender The command sender to send the information to
     * @param username The username of the player to get the information of
     */
    private boolean sendInfoMessage(CommandSender commandSender, String username) {
        String uuid = PlayerDataUtils.getUUIDFromUsername(username);
        if (uuid == null) {
            MessageUtils.onPlayerNotFound(commandSender, username);
            return true;
        }
        OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
        return sendInfoMessage(commandSender, player);
    }

    /**
     * Sends player height and reach information to the command sender
     * @param commandSender The command sender to send the information to
     * @param player The player to get the information of
     */
    private boolean sendInfoMessage(CommandSender commandSender, OfflinePlayer player) {
        DecimalFormat df = new DecimalFormat("#.##");
        String blockHeight = df.format(PlayerDataUtils.getPlayerHeight(player));
        String blockReach = df.format(PlayerDataUtils.getPlayerBlockReach(player));
        String entityReach = df.format(PlayerDataUtils.getPlayerEntityReach(player));

        MessageUtils.onInfoMessage(commandSender, player.getName(), blockHeight, blockReach, entityReach);
        return true;
    }


    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();
            if (commandSender.hasPermission("resizeplayers.reload")) {
                completions.add("reload");
            }
            if (commandSender.hasPermission("resizeplayers.info")) {
                completions.add("info");
            }
            completions.add("help");
            return completions;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("info")) {
            List<String> completions = new ArrayList<>();
            if (commandSender.hasPermission("resizeplayers.info.others")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    completions.add(player.getName());
                }
            }
            return completions;
        }
        return Collections.emptyList();
    }

}
