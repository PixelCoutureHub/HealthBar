package com.pixeldev.llx.utils;

import com.pixeldev.llx.LifeLineX;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;

public class MetricsManager {
    
    private final LifeLineX plugin;
    private Metrics metrics;
    
    public MetricsManager(LifeLineX plugin) {
        this.plugin = plugin;
    }
    
    public void initialize() {
        // bStats plugin ID for LifeLineX (you would need to register on bStats)
        metrics = new Metrics(plugin, 12345); // Replace with actual plugin ID
        
        // Add custom charts
        addCustomCharts();
        
        plugin.logInfo("Metrics initialized successfully!");
    }
    
    private void addCustomCharts() {
        // Display mode chart
        metrics.addCustomChart(new SimplePie("display_mode", () -> 
            plugin.getConfigManager().getDisplayMode().name()));
        
        // Enabled status chart
        metrics.addCustomChart(new SimplePie("enabled_status", () -> 
            plugin.getConfigManager().isEnabled() ? "Enabled" : "Disabled"));
        
        // Custom textures usage chart
        metrics.addCustomChart(new SimplePie("custom_textures", () -> 
            plugin.getConfigManager().isUseCustomTextures() ? "Enabled" : "Disabled"));
    }
}