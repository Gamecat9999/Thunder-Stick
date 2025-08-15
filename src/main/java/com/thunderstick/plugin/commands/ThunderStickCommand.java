package com.thunderstick.plugin.commands;

import com.thunderstick.plugin.ThunderStickPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThunderStickCommand implements CommandExecutor, TabCompleter {
    
    private final ThunderStickPlugin plugin;
    
    public ThunderStickCommand(ThunderStickPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("thunderstick.admin")) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }
        
        if (args.length == 0) {
            sendHelpMessage(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "reload":
                plugin.reloadPluginConfig();
                String reloadMessage = plugin.getConfig().getString("messages.reload", "&aThunderStick configuration reloaded!");
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', reloadMessage));
                break;
                
            case "info":
                String version = plugin.getDescription().getVersion();
                String infoMessage = plugin.getConfig().getString("messages.info", "&6ThunderStick &7v{version} - Transform golden swords into lightning weapons!");
                infoMessage = infoMessage.replace("{version}", version);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', infoMessage));
                break;
                
            default:
                sendHelpMessage(sender);
                break;
        }
        
        return true;
    }
    
    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "=== ThunderStick Commands ===");
        sender.sendMessage(ChatColor.YELLOW + "/thunderstick info" + ChatColor.WHITE + " - Show plugin information");
        sender.sendMessage(ChatColor.YELLOW + "/thunderstick reload" + ChatColor.WHITE + " - Reload plugin configuration");
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("info", "reload");
        }
        return new ArrayList<>();
    }
}