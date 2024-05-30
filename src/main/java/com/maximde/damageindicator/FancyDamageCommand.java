package com.maximde.damageindicator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FancyDamageCommand implements CommandExecutor {
    private final Config config;

    public FancyDamageCommand(Config config) {
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player player) {
            if(!player.hasPermission("fancydamageindicator.commands")) {
                player.sendMessage(ChatColor.RED + "No permission!");
                return false;
            }
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            config.reload();
            sender.sendMessage(ChatColor.GREEN + "Config reloaded!");
            return true;
        }
        return false;
    }
}