package com.thunderstick.plugin;

import com.thunderstick.plugin.listeners.ThunderStickListener;
import com.thunderstick.plugin.managers.CooldownManager;
import com.thunderstick.plugin.commands.ThunderStickCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class ThunderStickPlugin extends JavaPlugin {
    
    private CooldownManager cooldownManager;
    private ThunderStickListener thunderStickListener;
    
    @Override
    public void onEnable() {
        // Save default config if it doesn't exist
        saveDefaultConfig();
        
        // Initialize managers
        cooldownManager = new CooldownManager(this);
        
        // Register event listeners
        thunderStickListener = new ThunderStickListener(this, cooldownManager);
        getServer().getPluginManager().registerEvents(thunderStickListener, this);
        
        // Register commands
        getCommand("thunderstick").setExecutor(new ThunderStickCommand(this));
        
        getLogger().info("ThunderStick plugin has been enabled!");
        getLogger().info("All golden swords are now ThunderSticks!");
    }
    
    @Override
    public void onDisable() {
        // Clear cooldowns on shutdown
        if (cooldownManager != null) {
            cooldownManager.clearAllCooldowns();
        }
        
        getLogger().info("ThunderStick plugin has been disabled!");
    }
    
    public void reloadPluginConfig() {
        reloadConfig();
        if (cooldownManager != null) {
            cooldownManager.clearAllCooldowns();
        }
    }
    
    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }
}