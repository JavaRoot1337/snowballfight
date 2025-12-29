package ru.javaroot.snowballfight.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.javaroot.snowballfight.SnowballFight;

public class ReloadCommand implements CommandExecutor {

    private final SnowballFight plugin;

    public ReloadCommand(SnowballFight plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!player.hasPermission("snowballfight.reload")) {
                String message = plugin.getSettings().getNoPermissionMessage();
                message = ChatColor.translateAlternateColorCodes('&', message);
                player.sendMessage(message);
                return true;
            }
        } else {
            String message = plugin.getSettings().getPlayerOnlyMessage();
            message = ChatColor.translateAlternateColorCodes('&', message);
            sender.sendMessage(message);
            return true;
        }

        if (args.length == 0 || !args[0].equalsIgnoreCase("reload")) {
            String message = plugin.getSettings().getCommandUsageMessage();
            message = message.replace("%command%", label);
            message = ChatColor.translateAlternateColorCodes('&', message);
            sender.sendMessage(message);
            return true;
        }

        plugin.getSettings().loadConfig();

        String message = plugin.getSettings().getReloadSuccessMessage();
        message = ChatColor.translateAlternateColorCodes('&', message);
        sender.sendMessage(message);

        return true;
    }
}