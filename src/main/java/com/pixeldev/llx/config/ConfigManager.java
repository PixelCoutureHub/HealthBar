package com.pixeldev.llx.config;

import com.pixeldev.llx.LifeLineX;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ConfigManager {
    
    private final LifeLineX plugin;
    private FileConfiguration config;
    private File configFile;
    
    // Configuration values
    private boolean enabled;
    private DisplayMode displayMode;
    private boolean showForPlayers;
    private boolean showForMobs;
    private boolean showForBosses;
    private double maxDistance;
    private int updateInterval;
    private boolean useCustomTextures;
    private String healthBarFormat;
    private List<String> disabledWorlds;
    
    public enum DisplayMode {
        ALWAYS_SHOW,
        HIDE_UNTIL_HURT,
        DAMAGE_ONLY
    }
    
    public ConfigManager(LifeLineX plugin) {
        this.plugin = plugin;
    }
    
    public void loadConfig() {
        createConfigFile();
        loadConfigValues();
    }
    
    private void createConfigFile() {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        
        if (!configFile.exists()) {
            plugin.getDataFolder().mkdirs();
            plugin.saveDefaultConfig();
        }
        
        config = YamlConfiguration.loadConfiguration(configFile);
        
        // Set default values if they don't exist
        setDefaults();
        saveConfig();
    }
    
    private void setDefaults() {
        if (!config.contains("enabled")) {
            config.set("enabled", true);
        }
        
        if (!config.contains("display-mode")) {
            config.set("display-mode", "HIDE_UNTIL_HURT");
        }
        
        if (!config.contains("show-for-players")) {
            config.set("show-for-players", true);
        }
        
        if (!config.contains("show-for-mobs")) {
            config.set("show-for-mobs", true);
        }
        
        if (!config.contains("show-for-bosses")) {
            config.set("show-for-bosses", true);
        }
        
        if (!config.contains("max-distance")) {
            config.set("max-distance", 32.0);
        }
        
        if (!config.contains("update-interval")) {
            config.set("update-interval", 5);
        }
        
        if (!config.contains("use-custom-textures")) {
            config.set("use-custom-textures", true);
        }
        
        if (!config.contains("health-bar-format")) {
            config.set("health-bar-format", "❤ %current%/%max%");
        }
        
        if (!config.contains("disabled-worlds")) {
            config.set("disabled-worlds", Arrays.asList("example_world"));
        }
        
        // Add comments
        config.setComments("enabled", Arrays.asList(
            "Enable or disable the health bar display",
            "Default: true"
        ));
        
        config.setComments("display-mode", Arrays.asList(
            "Display mode for health bars:",
            "ALWAYS_SHOW - Always show health bars",
            "HIDE_UNTIL_HURT - Only show when entity is damaged",
            "DAMAGE_ONLY - Show only during damage events",
            "Default: HIDE_UNTIL_HURT"
        ));
        
        config.setComments("max-distance", Arrays.asList(
            "Maximum distance to show health bars (in blocks)",
            "Default: 32.0"
        ));
        
        config.setComments("update-interval", Arrays.asList(
            "Update interval for health bars (in ticks)",
            "Lower values = more frequent updates but higher performance cost",
            "Default: 5"
        ));
    }
    
    private void loadConfigValues() {
        enabled = config.getBoolean("enabled", true);
        
        try {
            displayMode = DisplayMode.valueOf(config.getString("display-mode", "HIDE_UNTIL_HURT"));
        } catch (IllegalArgumentException e) {
            displayMode = DisplayMode.HIDE_UNTIL_HURT;
            plugin.logWarning("Invalid display mode in config, using default: HIDE_UNTIL_HURT");
        }
        
        showForPlayers = config.getBoolean("show-for-players", true);
        showForMobs = config.getBoolean("show-for-mobs", true);
        showForBosses = config.getBoolean("show-for-bosses", true);
        maxDistance = config.getDouble("max-distance", 32.0);
        updateInterval = config.getInt("update-interval", 5);
        useCustomTextures = config.getBoolean("use-custom-textures", true);
        healthBarFormat = config.getString("health-bar-format", "❤ %current%/%max%");
        disabledWorlds = config.getStringList("disabled-worlds");
    }
    
    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.logError("Could not save config file", e);
        }
    }
    
    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
        loadConfigValues();
    }
    
    // Getters
    public boolean isEnabled() { return enabled; }
    public DisplayMode getDisplayMode() { return displayMode; }
    public boolean isShowForPlayers() { return showForPlayers; }
    public boolean isShowForMobs() { return showForMobs; }
    public boolean isShowForBosses() { return showForBosses; }
    public double getMaxDistance() { return maxDistance; }
    public int getUpdateInterval() { return updateInterval; }
    public boolean isUseCustomTextures() { return useCustomTextures; }
    public String getHealthBarFormat() { return healthBarFormat; }
    public List<String> getDisabledWorlds() { return disabledWorlds; }
    
    // Setters
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        config.set("enabled", enabled);
        saveConfig();
    }
    
    public void setDisplayMode(DisplayMode displayMode) {
        this.displayMode = displayMode;
        config.set("display-mode", displayMode.name());
        saveConfig();
    }
    
    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
        config.set("max-distance", maxDistance);
        saveConfig();
    }
}