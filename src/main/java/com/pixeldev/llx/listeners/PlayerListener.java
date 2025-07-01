package com.pixeldev.llx.listeners;

import com.pixeldev.llx.LifeLineX;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    
    private final LifeLineX plugin;
    
    public PlayerListener(LifeLineX plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Apply resource pack if enabled
        if (plugin.getConfigManager().isUseCustomTextures()) {
            plugin.getResourcePackManager().applyResourcePack(event.getPlayer());
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Clean up any player-specific data
        plugin.getHealthBarManager().removeHealthBar(event.getPlayer().getUniqueId());
    }
}