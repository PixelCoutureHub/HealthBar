package com.pixeldev.llx;

import com.pixeldev.llx.commands.LifeLineXCommand;
import com.pixeldev.llx.config.ConfigManager;
import com.pixeldev.llx.display.HealthBarManager;
import com.pixeldev.llx.listeners.EntityListener;
import com.pixeldev.llx.listeners.PlayerListener;
import com.pixeldev.llx.resources.ResourcePackManager;
import com.pixeldev.llx.utils.MetricsManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * LifeLineX - Advanced Health Bar Display Plugin
 * 
 * @author Pixeldev
 * @version 2.0.0
 */
public final class LifeLineX extends JavaPlugin {
    
    private static LifeLineX instance;
    private ConfigManager configManager;
    private HealthBarManager healthBarManager;
    private ResourcePackManager resourcePackManager;
    private MetricsManager metricsManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Initialize managers
        this.configManager = new ConfigManager(this);
        this.resourcePackManager = new ResourcePackManager(this);
        this.healthBarManager = new HealthBarManager(this);
        this.metricsManager = new MetricsManager(this);
        
        // Load configuration
        configManager.loadConfig();
        
        // Initialize resource pack
        resourcePackManager.initialize();
        
        // Register listeners
        registerListeners();
        
        // Register commands
        registerCommands();
        
        // Start health bar manager
        healthBarManager.start();
        
        // Initialize metrics
        metricsManager.initialize();
        
        getLogger().info("LifeLineX v" + getDescription().getVersion() + " has been enabled!");
        getLogger().info("Created by Pixeldev - Advanced Health Bar Display");
    }
    
    @Override
    public void onDisable() {
        if (healthBarManager != null) {
            healthBarManager.shutdown();
        }
        
        if (resourcePackManager != null) {
            resourcePackManager.cleanup();
        }
        
        getLogger().info("LifeLineX has been disabled!");
    }
    
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(new EntityListener(this), this);
    }
    
    private void registerCommands() {
        getCommand("lifelinex").setExecutor(new LifeLineXCommand(this));
    }
    
    public static LifeLineX getInstance() {
        return instance;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public HealthBarManager getHealthBarManager() {
        return healthBarManager;
    }
    
    public ResourcePackManager getResourcePackManager() {
        return resourcePackManager;
    }
    
    public void logInfo(String message) {
        getLogger().info(message);
    }
    
    public void logWarning(String message) {
        getLogger().warning(message);
    }
    
    public void logError(String message, Throwable throwable) {
        getLogger().log(Level.SEVERE, message, throwable);
    }
}