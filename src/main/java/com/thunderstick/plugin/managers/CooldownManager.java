package com.thunderstick.plugin.managers;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownManager {
    
    private final JavaPlugin plugin;
    private final Map<UUID, Long> cooldowns;
    
    public CooldownManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.cooldowns = new HashMap<>();
    }
    
    /**
     * Check if a player is on cooldown
     */
    public boolean isOnCooldown(Player player) {
        UUID playerId = player.getUniqueId();
        if (!cooldowns.containsKey(playerId)) {
            return false;
        }
        
        long cooldownTime = plugin.getConfig().getLong("cooldown.time", 3) * 1000; // Convert to milliseconds
        long timeSinceLastUse = System.currentTimeMillis() - cooldowns.get(playerId);
        
        return timeSinceLastUse < cooldownTime;
    }
    
    /**
     * Get remaining cooldown time in seconds
     */
    public int getRemainingCooldown(Player player) {
        UUID playerId = player.getUniqueId();
        if (!cooldowns.containsKey(playerId)) {
            return 0;
        }
        
        long cooldownTime = plugin.getConfig().getLong("cooldown.time", 3) * 1000; // Convert to milliseconds
        long timeSinceLastUse = System.currentTimeMillis() - cooldowns.get(playerId);
        long remainingTime = cooldownTime - timeSinceLastUse;
        
        return remainingTime > 0 ? (int) Math.ceil(remainingTime / 1000.0) : 0;
    }
    
    /**
     * Set cooldown for a player
     */
    public void setCooldown(Player player) {
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
    }
    
    /**
     * Remove cooldown for a player
     */
    public void removeCooldown(Player player) {
        cooldowns.remove(player.getUniqueId());
    }
    
    /**
     * Clear all cooldowns
     */
    public void clearAllCooldowns() {
        cooldowns.clear();
    }
}