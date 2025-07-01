package com.pixeldev.llx.resources;

import com.pixeldev.llx.LifeLineX;
import org.bukkit.entity.Player;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ResourcePackManager {
    
    private final LifeLineX plugin;
    private String resourcePackUrl;
    private byte[] resourcePackHash;
    private File resourcePackFile;
    
    public ResourcePackManager(LifeLineX plugin) {
        this.plugin = plugin;
    }
    
    public void initialize() {
        try {
            createResourcePack();
            generateResourcePackHash();
            plugin.logInfo("Resource pack initialized successfully!");
        } catch (Exception e) {
            plugin.logError("Failed to initialize resource pack", e);
        }
    }
    
    private void createResourcePack() throws IOException {
        File resourcePackDir = new File(plugin.getDataFolder(), "resourcepack");
        resourcePackFile = new File(plugin.getDataFolder(), "LifeLineX-ResourcePack.zip");
        
        // Create resource pack directory structure
        createResourcePackStructure(resourcePackDir);
        
        // Create the zip file
        createZipFile(resourcePackDir, resourcePackFile);
        
        plugin.logInfo("Resource pack created at: " + resourcePackFile.getAbsolutePath());
    }
    
    private void createResourcePackStructure(File baseDir) throws IOException {
        // Create pack.mcmeta
        createPackMcMeta(baseDir);
        
        // Create textures
        createHealthBarTextures(baseDir);
        
        // Create models
        createHealthBarModels(baseDir);
    }
    
    private void createPackMcMeta(File baseDir) throws IOException {
        File packMcMeta = new File(baseDir, "pack.mcmeta");
        packMcMeta.getParentFile().mkdirs();
        
        String content = """
            {
                "pack": {
                    "pack_format": 34,
                    "description": "LifeLineX Health Bar Resource Pack\\nCreated by Pixeldev"
                }
            }
            """;
        
        Files.write(packMcMeta.toPath(), content.getBytes());
    }
    
    private void createHealthBarTextures(File baseDir) throws IOException {
        // Create texture directories
        File texturesDir = new File(baseDir, "assets/minecraft/textures/entity");
        texturesDir.mkdirs();
        
        // Copy health bar textures from plugin resources
        copyResourceToFile("/textures/health_bar/heart.png", new File(texturesDir, "health_bar_heart.png"));
        
        // Create health bar textures (0-50)
        for (int i = 0; i <= 50; i++) {
            String fileName = String.format("health_bar_%02d.png", i);
            copyResourceToFile("/textures/health_bar/bar/" + String.format("%02d.png", i), 
                new File(texturesDir, fileName));
        }
        
        // Create font textures (0-9)
        for (int i = 0; i <= 9; i++) {
            String fileName = "health_bar_font_" + i + ".png";
            copyResourceToFile("/textures/health_bar/font/" + i + ".png", 
                new File(texturesDir, fileName));
        }
    }
    
    private void createHealthBarModels(File baseDir) throws IOException {
        File modelsDir = new File(baseDir, "assets/minecraft/models/entity");
        modelsDir.mkdirs();
        
        // Create health bar model
        File healthBarModel = new File(modelsDir, "health_bar.json");
        String modelContent = """
            {
                "parent": "item/generated",
                "textures": {
                    "layer0": "entity/health_bar_heart"
                },
                "display": {
                    "head": {
                        "rotation": [0, 0, 0],
                        "translation": [0, 0, 0],
                        "scale": [1, 1, 1]
                    }
                }
            }
            """;
        
        Files.write(healthBarModel.toPath(), modelContent.getBytes());
    }
    
    private void copyResourceToFile(String resourcePath, File targetFile) throws IOException {
        targetFile.getParentFile().mkdirs();
        
        try (InputStream inputStream = getClass().getResourceAsStream(resourcePath)) {
            if (inputStream != null) {
                Files.copy(inputStream, targetFile.toPath());
            } else {
                // Create a placeholder texture if resource not found
                createPlaceholderTexture(targetFile);
            }
        }
    }
    
    private void createPlaceholderTexture(File file) throws IOException {
        // Create a simple 16x16 placeholder texture
        // This is a simplified approach - in a real implementation,
        // you would create proper PNG files
        file.createNewFile();
    }
    
    private void createZipFile(File sourceDir, File zipFile) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(fos)) {
            
            addDirectoryToZip(sourceDir, sourceDir, zos);
        }
    }
    
    private void addDirectoryToZip(File rootDir, File currentDir, ZipOutputStream zos) throws IOException {
        File[] files = currentDir.listFiles();
        if (files == null) return;
        
        for (File file : files) {
            if (file.isDirectory()) {
                addDirectoryToZip(rootDir, file, zos);
            } else {
                String relativePath = rootDir.toPath().relativize(file.toPath()).toString();
                ZipEntry entry = new ZipEntry(relativePath);
                zos.putNextEntry(entry);
                
                try (FileInputStream fis = new FileInputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                }
                
                zos.closeEntry();
            }
        }
    }
    
    private void generateResourcePackHash() throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        
        try (FileInputStream fis = new FileInputStream(resourcePackFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                digest.update(buffer, 0, length);
            }
        }
        
        resourcePackHash = digest.digest();
    }
    
    public void applyResourcePack(Player player) {
        if (resourcePackFile == null || !resourcePackFile.exists()) {
            plugin.logWarning("Resource pack file not found, cannot apply to player: " + player.getName());
            return;
        }
        
        try {
            // In a real implementation, you would host the resource pack on a web server
            // For now, we'll just log that we would apply it
            plugin.logInfo("Would apply resource pack to player: " + player.getName());
            
            // player.setResourcePack(resourcePackUrl, resourcePackHash);
        } catch (Exception e) {
            plugin.logError("Failed to apply resource pack to player: " + player.getName(), e);
        }
    }
    
    public void cleanup() {
        // Clean up temporary files if needed
        if (resourcePackFile != null && resourcePackFile.exists()) {
            // Don't delete the resource pack file as it might be needed
        }
    }
}