package com.pixeldev.llx.commands;

import com.pixeldev.llx.LifeLineX;
import com.pixeldev.llx.config.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LifeLineXCommand implements CommandExecutor, TabCompleter {
    
    private final LifeLineX plugin;
    
    public LifeLineXCommand(LifeLineX plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelpMessage(sender);
            return true;
        }
        
        switch (args[0].toLowerCase()) {
            case "help":
                sendHelpMessage(sender);
                break;
                
            case "reload":
                if (!sender.hasPermission("lifelinex.admin")) {
                    sender.sendMessage(Component.text("You don't have permission to use this command!", NamedTextColor.RED));
                    return true;
                }
                
                plugin.getConfigManager().reloadConfig();
                plugin.getHealthBarManager().clearAllHealthBars();
                sender.sendMessage(Component.text("LifeLineX configuration reloaded!", NamedTextColor.GREEN));
                break;
                
            case "toggle":
                if (!sender.hasPermission("lifelinex.admin")) {
                    sender.sendMessage(Component.text("You don't have permission to use this command!", NamedTextColor.RED));
                    return true;
                }
                
                boolean enabled = !plugin.getConfigManager().isEnabled();
                plugin.getConfigManager().setEnabled(enabled);
                
                String status = enabled ? "enabled" : "disabled";
                NamedTextColor color = enabled ? NamedTextColor.GREEN : NamedTextColor.RED;
                sender.sendMessage(Component.text("LifeLineX has been " + status + "!", color));
                break;
                
            case "mode":
                if (!sender.hasPermission("lifelinex.admin")) {
                    sender.sendMessage(Component.text("You don't have permission to use this command!", NamedTextColor.RED));
                    return true;
                }
                
                if (args.length < 2) {
                    sender.sendMessage(Component.text("Usage: /lifelinex mode <ALWAYS_SHOW|HIDE_UNTIL_HURT|DAMAGE_ONLY>", NamedTextColor.YELLOW));
                    return true;
                }
                
                try {
                    ConfigManager.DisplayMode mode = ConfigManager.DisplayMode.valueOf(args[1].toUpperCase());
                    plugin.getConfigManager().setDisplayMode(mode);
                    sender.sendMessage(Component.text("Display mode set to: " + mode.name(), NamedTextColor.GREEN));
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(Component.text("Invalid display mode! Use: ALWAYS_SHOW, HIDE_UNTIL_HURT, or DAMAGE_ONLY", NamedTextColor.RED));
                }
                break;
                
            case "distance":
                if (!sender.hasPermission("lifelinex.admin")) {
                    sender.sendMessage(Component.text("You don't have permission to use this command!", NamedTextColor.RED));
                    return true;
                }
                
                if (args.length < 2) {
                    sender.sendMessage(Component.text("Usage: /lifelinex distance <number>", NamedTextColor.YELLOW));
                    return true;
                }
                
                try {
                    double distance = Double.parseDouble(args[1]);
                    if (distance <= 0 || distance > 100) {
                        sender.sendMessage(Component.text("Distance must be between 1 and 100!", NamedTextColor.RED));
                        return true;
                    }
                    
                    plugin.getConfigManager().setMaxDistance(distance);
                    sender.sendMessage(Component.text("Max distance set to: " + distance + " blocks", NamedTextColor.GREEN));
                } catch (NumberFormatException e) {
                    sender.sendMessage(Component.text("Invalid number format!", NamedTextColor.RED));
                }
                break;
                
            case "resourcepack":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(Component.text("This command can only be used by players!", NamedTextColor.RED));
                    return true;
                }
                
                Player player = (Player) sender;
                plugin.getResourcePackManager().applyResourcePack(player);
                player.sendMessage(Component.text("Resource pack applied!", NamedTextColor.GREEN));
                break;
                
            case "info":
                sendInfoMessage(sender);
                break;
                
            default:
                sender.sendMessage(Component.text("Unknown command! Use /lifelinex help for available commands.", NamedTextColor.RED));
                break;
        }
        
        return true;
    }
    
    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(Component.text()
            .append(Component.text("=== LifeLineX Commands ===", NamedTextColor.GOLD, TextDecoration.BOLD))
            .append(Component.newline())
            .append(Component.text("/lifelinex help", NamedTextColor.YELLOW))
            .append(Component.text(" - Show this help message", NamedTextColor.GRAY))
            .append(Component.newline())
            .append(Component.text("/lifelinex info", NamedTextColor.YELLOW))
            .append(Component.text(" - Show plugin information", NamedTextColor.GRAY))
            .append(Component.newline())
            .append(Component.text("/lifelinex reload", NamedTextColor.YELLOW))
            .append(Component.text(" - Reload configuration", NamedTextColor.GRAY))
            .append(Component.newline())
            .append(Component.text("/lifelinex toggle", NamedTextColor.YELLOW))
            .append(Component.text(" - Toggle health bars on/off", NamedTextColor.GRAY))
            .append(Component.newline())
            .append(Component.text("/lifelinex mode <mode>", NamedTextColor.YELLOW))
            .append(Component.text(" - Set display mode", NamedTextColor.GRAY))
            .append(Component.newline())
            .append(Component.text("/lifelinex distance <number>", NamedTextColor.YELLOW))
            .append(Component.text(" - Set max display distance", NamedTextColor.GRAY))
            .append(Component.newline())
            .append(Component.text("/lifelinex resourcepack", NamedTextColor.YELLOW))
            .append(Component.text(" - Apply resource pack", NamedTextColor.GRAY))
            .build());
    }
    
    private void sendInfoMessage(CommandSender sender) {
        ConfigManager config = plugin.getConfigManager();
        
        sender.sendMessage(Component.text()
            .append(Component.text("=== LifeLineX Information ===", NamedTextColor.GOLD, TextDecoration.BOLD))
            .append(Component.newline())
            .append(Component.text("Version: ", NamedTextColor.YELLOW))
            .append(Component.text(plugin.getDescription().getVersion(), NamedTextColor.WHITE))
            .append(Component.newline())
            .append(Component.text("Author: ", NamedTextColor.YELLOW))
            .append(Component.text("Pixeldev", NamedTextColor.WHITE))
            .append(Component.newline())
            .append(Component.text("Status: ", NamedTextColor.YELLOW))
            .append(Component.text(config.isEnabled() ? "Enabled" : "Disabled", 
                config.isEnabled() ? NamedTextColor.GREEN : NamedTextColor.RED))
            .append(Component.newline())
            .append(Component.text("Display Mode: ", NamedTextColor.YELLOW))
            .append(Component.text(config.getDisplayMode().name(), NamedTextColor.WHITE))
            .append(Component.newline())
            .append(Component.text("Max Distance: ", NamedTextColor.YELLOW))
            .append(Component.text(config.getMaxDistance() + " blocks", NamedTextColor.WHITE))
            .build());
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            List<String> commands = Arrays.asList("help", "info", "reload", "toggle", "mode", "distance", "resourcepack");
            for (String cmd : commands) {
                if (cmd.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(cmd);
                }
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("mode")) {
            List<String> modes = Arrays.asList("ALWAYS_SHOW", "HIDE_UNTIL_HURT", "DAMAGE_ONLY");
            for (String mode : modes) {
                if (mode.toLowerCase().startsWith(args[1].toLowerCase())) {
                    completions.add(mode);
                }
            }
        }
        
        return completions;
    }
}