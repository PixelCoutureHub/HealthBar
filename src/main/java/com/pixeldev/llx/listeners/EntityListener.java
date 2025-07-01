package com.pixeldev.llx.listeners;

import com.pixeldev.llx.LifeLineX;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityListener implements Listener {
    
    private final LifeLineX plugin;
    
    public EntityListener(LifeLineX plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        // Notify health bar manager of damage
        plugin.getHealthBarManager().onEntityDamage(event.getEntity());
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeath(EntityDeathEvent event) {
        // Remove health bar when entity dies
        plugin.getHealthBarManager().removeHealthBar(event.getEntity().getUniqueId());
    }
}