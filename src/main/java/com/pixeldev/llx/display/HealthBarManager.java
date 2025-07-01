package com.pixeldev.llx.display;

import com.pixeldev.llx.LifeLineX;
import com.pixeldev.llx.config.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class HealthBarManager {
    
    private final LifeLineX plugin;
    private final ConfigManager config;
    private final Map<UUID, HealthBarData> healthBars;
    private final Map<UUID, Long> lastDamageTime;
    private BukkitTask updateTask;
    
    public HealthBarManager(LifeLineX plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfigManager();
        this.healthBars = new ConcurrentHashMap<>();
        this.lastDamageTime = new ConcurrentHashMap<>();
    }
    
    public void start() {
        if (updateTask != null) {
            updateTask.cancel();
        }
        
        updateTask = new BukkitRunnable() {
            @Override
            public void run() {
                updateHealthBars();
            }
        }.runTaskTimer(plugin, 0L, config.getUpdateInterval());
    }
    
    public void shutdown() {
        if (updateTask != null) {
            updateTask.cancel();
            updateTask = null;
        }
        
        // Clear all health bars
        healthBars.clear();
        lastDamageTime.clear();
    }
    
    private void updateHealthBars() {
        if (!config.isEnabled()) {
            return;
        }
        
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (config.getDisabledWorlds().contains(player.getWorld().getName())) {
                continue;
            }
            
            updateHealthBarsForPlayer(player);
        }
        
        // Clean up old damage times
        long currentTime = System.currentTimeMillis();
        lastDamageTime.entrySet().removeIf(entry -> 
            currentTime - entry.getValue() > 5000); // 5 seconds
    }
    
    private void updateHealthBarsForPlayer(Player player) {
        Location playerLoc = player.getLocation();
        double maxDistance = config.getMaxDistance();
        
        for (Entity entity : player.getWorld().getNearbyEntities(playerLoc, maxDistance, maxDistance, maxDistance)) {
            if (entity.equals(player)) continue;
            
            if (shouldShowHealthBar(entity, player)) {
                updateEntityHealthBar(entity, player);
            } else {
                removeHealthBar(entity.getUniqueId());
            }
        }
    }
    
    private boolean shouldShowHealthBar(Entity entity, Player player) {
        if (!(entity instanceof LivingEntity)) {
            return false;
        }
        
        LivingEntity living = (LivingEntity) entity;
        
        // Check entity type permissions
        if (entity instanceof Player && !config.isShowForPlayers()) {
            return false;
        }
        
        if ((entity instanceof Boss || entity instanceof EnderDragon || entity instanceof Wither) && !config.isShowForBosses()) {
            return false;
        }
        
        if (!(entity instanceof Player) && !(entity instanceof Boss) && !config.isShowForMobs()) {
            return false;
        }
        
        // Check display mode
        ConfigManager.DisplayMode mode = config.getDisplayMode();
        switch (mode) {
            case ALWAYS_SHOW:
                return true;
            case HIDE_UNTIL_HURT:
                return living.getHealth() < living.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() ||
                       lastDamageTime.containsKey(entity.getUniqueId());
            case DAMAGE_ONLY:
                return lastDamageTime.containsKey(entity.getUniqueId()) &&
                       System.currentTimeMillis() - lastDamageTime.get(entity.getUniqueId()) < 3000;
            default:
                return false;
        }
    }
    
    private void updateEntityHealthBar(Entity entity, Player player) {
        if (!(entity instanceof LivingEntity)) {
            return;
        }
        
        LivingEntity living = (LivingEntity) entity;
        double health = living.getHealth();
        double maxHealth = living.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        
        HealthBarData data = healthBars.computeIfAbsent(entity.getUniqueId(), 
            k -> new HealthBarData(entity.getUniqueId()));
        
        // Update health bar display
        Component healthBar = createHealthBarComponent(health, maxHealth, entity);
        
        // Show health bar above entity
        showHealthBarToPlayer(player, entity, healthBar);
        
        data.setLastUpdate(System.currentTimeMillis());
        data.setHealth(health);
        data.setMaxHealth(maxHealth);
    }
    
    private Component createHealthBarComponent(double health, double maxHealth, Entity entity) {
        double percentage = health / maxHealth;
        int barLength = 20; // Length of the health bar
        int filledBars = (int) (percentage * barLength);
        
        StringBuilder bar = new StringBuilder();
        
        // Add filled portion
        for (int i = 0; i < filledBars; i++) {
            bar.append("█");
        }
        
        // Add empty portion
        for (int i = filledBars; i < barLength; i++) {
            bar.append("░");
        }
        
        // Determine color based on health percentage
        TextColor barColor;
        if (percentage > 0.6) {
            barColor = NamedTextColor.GREEN;
        } else if (percentage > 0.3) {
            barColor = NamedTextColor.YELLOW;
        } else {
            barColor = NamedTextColor.RED;
        }
        
        // Create the component
        String healthText = String.format("%.1f/%.1f", health, maxHealth);
        
        return Component.text()
            .append(Component.text("❤ ", NamedTextColor.RED))
            .append(Component.text(bar.toString(), barColor))
            .append(Component.text(" " + healthText, NamedTextColor.WHITE))
            .build();
    }
    
    private void showHealthBarToPlayer(Player player, Entity entity, Component healthBar) {
        // Use armor stand or text display for health bar
        Location loc = entity.getLocation().add(0, getHealthBarHeight(entity), 0);
        
        // Send action bar or use hologram-like display
        // For now, we'll use action bar as a simple implementation
        if (player.getLocation().distance(entity.getLocation()) <= 8) {
            player.sendActionBar(healthBar);
        }
    }
    
    private double getHealthBarHeight(Entity entity) {
        if (entity instanceof Player) {
            return 2.3;
        } else if (entity instanceof EnderDragon) {
            return 8.0;
        } else if (entity instanceof Wither) {
            return 4.0;
        } else if (entity instanceof Giant) {
            return 12.0;
        } else if (entity instanceof Enderman) {
            return 3.2;
        } else if (entity instanceof IronGolem) {
            return 3.0;
        } else if (entity instanceof Horse || entity instanceof Llama) {
            return 2.0;
        } else if (entity instanceof Chicken || entity instanceof Rabbit) {
            return 1.0;
        } else {
            return 1.8; // Default height
        }
    }
    
    public void onEntityDamage(Entity entity) {
        if (entity instanceof LivingEntity) {
            lastDamageTime.put(entity.getUniqueId(), System.currentTimeMillis());
        }
    }
    
    public void removeHealthBar(UUID entityId) {
        healthBars.remove(entityId);
        lastDamageTime.remove(entityId);
    }
    
    public void clearAllHealthBars() {
        healthBars.clear();
        lastDamageTime.clear();
    }
    
    private static class HealthBarData {
        private final UUID entityId;
        private double health;
        private double maxHealth;
        private long lastUpdate;
        
        public HealthBarData(UUID entityId) {
            this.entityId = entityId;
            this.lastUpdate = System.currentTimeMillis();
        }
        
        // Getters and setters
        public UUID getEntityId() { return entityId; }
        public double getHealth() { return health; }
        public void setHealth(double health) { this.health = health; }
        public double getMaxHealth() { return maxHealth; }
        public void setMaxHealth(double maxHealth) { this.maxHealth = maxHealth; }
        public long getLastUpdate() { return lastUpdate; }
        public void setLastUpdate(long lastUpdate) { this.lastUpdate = lastUpdate; }
    }
}