package com.thunderstick.plugin.listeners;

import com.thunderstick.plugin.ThunderStickPlugin;
import com.thunderstick.plugin.managers.CooldownManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class ThunderStickListener implements Listener {
    
    private final ThunderStickPlugin plugin;
    private final CooldownManager cooldownManager;
    
    public ThunderStickListener(ThunderStickPlugin plugin, CooldownManager cooldownManager) {
        this.plugin = plugin;
        this.cooldownManager = cooldownManager;
    }
    
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // Check if the damager is a player
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getDamager();
        
        // Check if player has permission to use ThunderStick
        if (!player.hasPermission("thunderstick.use")) {
            return;
        }
        
        // Check if the weapon is a golden sword
        ItemStack weapon = player.getInventory().getItemInMainHand();
        if (weapon.getType() != Material.GOLDEN_SWORD) {
            return;
        }
        
        // Check if player is on cooldown
        if (cooldownManager.isOnCooldown(player)) {
            if (plugin.getConfig().getBoolean("cooldown.show-messages", true)) {
                int remainingTime = cooldownManager.getRemainingCooldown(player);
                String message = plugin.getConfig().getString("messages.cooldown", "&cYou must wait {time} seconds before using ThunderStick again!");
                message = ChatColor.translateAlternateColorCodes('&', message.replace("{time}", String.valueOf(remainingTime)));
                player.sendMessage(message);
                
                // Play cooldown sound
                if (plugin.getConfig().getBoolean("sounds.enabled", true)) {
                    try {
                        Sound cooldownSound = Sound.valueOf(plugin.getConfig().getString("sounds.cooldown", "BLOCK_NOTE_BLOCK_BASS"));
                        player.playSound(player.getLocation(), cooldownSound, 0.5f, 1.0f);
                    } catch (IllegalArgumentException e) {
                        plugin.getLogger().warning("Invalid cooldown sound in config: " + plugin.getConfig().getString("sounds.cooldown"));
                    }
                }
            }
            return;
        }
        
        // Get the target's location
        Location targetLocation = event.getEntity().getLocation();
        
        // Strike lightning at the target
        boolean shouldDamage = plugin.getConfig().getBoolean("lightning.damage", true);
        targetLocation.getWorld().strikeLightning(targetLocation);
        
        // If damage is disabled, we need to prevent the lightning from actually damaging
        if (!shouldDamage) {
            targetLocation.getWorld().strikeLightningEffect(targetLocation);
        }
        
        // Spawn particle effects
        if (plugin.getConfig().getBoolean("particles.enabled", true)) {
            spawnParticleEffects(targetLocation);
        }
        
        // Play success sound
        if (plugin.getConfig().getBoolean("sounds.enabled", true)) {
            try {
                Sound successSound = Sound.valueOf(plugin.getConfig().getString("sounds.success", "ENTITY_LIGHTNING_BOLT_THUNDER"));
                player.playSound(targetLocation, successSound, 1.0f, 1.0f);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Invalid success sound in config: " + plugin.getConfig().getString("sounds.success"));
            }
        }
        
        // Send success message
        String message = plugin.getConfig().getString("messages.lightning-strike", "&6⚡ Lightning strikes your target!");
        message = ChatColor.translateAlternateColorCodes('&', message);
        player.sendMessage(message);
        
        // Set cooldown for the player
        cooldownManager.setCooldown(player);
        
        // Add extra damage if the target is a living entity
        if (event.getEntity() instanceof LivingEntity && shouldDamage) {
            LivingEntity target = (LivingEntity) event.getEntity();
            // Add some extra lightning damage (configurable)
            double extraDamage = plugin.getConfig().getDouble("lightning.extra-damage", 4.0);
            target.damage(extraDamage);
        }
    }
    
    private void spawnParticleEffects(Location location) {
        try {
            String particleType = plugin.getConfig().getString("particles.type", "ELECTRIC_SPARK");
            Particle particle = Particle.valueOf(particleType);
            int count = plugin.getConfig().getInt("particles.count", 15);
            
            // Spawn particles in a small radius around the lightning strike
            for (int i = 0; i < count; i++) {
                double offsetX = (Math.random() - 0.5) * 2.0;
                double offsetY = Math.random() * 2.0;
                double offsetZ = (Math.random() - 0.5) * 2.0;
                
                Location particleLocation = location.clone().add(offsetX, offsetY, offsetZ);
                location.getWorld().spawnParticle(particle, particleLocation, 1, 0, 0, 0, 0);
            }
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid particle type in config: " + plugin.getConfig().getString("particles.type"));
            // Fallback to default particle
            location.getWorld().spawnParticle(Particle.ELECTRIC_SPARK, location, 15, 1.0, 1.0, 1.0, 0.1);
        }
    }
}