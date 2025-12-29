package ru.javaroot.snowballfight;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SnowballList implements Listener {
    
    private final SnowballFight plugin;
    private final Map<UUID, Long> snowballCooldowns = new HashMap<>();
    
    public SnowballList(SnowballFight plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().name().contains("RIGHT_CLICK_AIR") || event.getAction().name().contains("RIGHT_CLICK_BLOCK")) {
            if (event.getItem() != null && event.getItem().getType() == Material.SNOWBALL) {
                Player player = event.getPlayer();
                
                if (plugin.getSettings().isCooldownEnabled()) {
                    long cooldownDurationSec = plugin.getSettings().getCooldownDuration();
                    if (cooldownDurationSec > 0) {
                        long cooldownDurationMs = cooldownDurationSec * 1000;
                        long currentTime = System.currentTimeMillis();
                        long lastThrowTime = snowballCooldowns.getOrDefault(player.getUniqueId(), 0L);
                        
                        if (currentTime - lastThrowTime < cooldownDurationMs) {
                            event.setCancelled(true);
                            return;
                        }
                        
                        player.setCooldown(Material.SNOWBALL, (int) (cooldownDurationMs / 50));
                    }
                }
                
                snowballCooldowns.put(player.getUniqueId(), System.currentTimeMillis());
                
                ItemStack snowballItem = event.getItem();
                if (snowballItem.getAmount() > 1) {
                    snowballItem.setAmount(snowballItem.getAmount() - 1);
                } else {
                    player.getInventory().remove(snowballItem);
                }
                
                Snowball snowball = player.launchProjectile(Snowball.class);
            }
        }
    }
    
    @EventHandler
    public void onSnowballHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Snowball)) {
            return;
        }
        
        if (event.getEntity().getShooter() instanceof Player) {
            spawnNewYearEffect(event.getEntity().getLocation());
            
            if (plugin.getSettings().isSoundEnabled()) {
                String soundType = plugin.getSettings().getSoundType();
                event.getEntity().getWorld().playSound(event.getEntity().getLocation(), soundType, 1.0F, 1.0F);
            }
            
            if (event.getHitEntity() != null && event.getHitEntity() instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) event.getHitEntity();
                applyEffectsToTarget(target);
            }
        }
    }
    
    private void applyEffectsToTarget(LivingEntity target) {
        int durationSec = plugin.getSettings().getSlownessDuration();
        int amplifier = plugin.getSettings().getSlownessAmplifier();
        int duration = durationSec * 20;
        
        PotionEffect slowEffect = new PotionEffect(PotionEffectType.SLOWNESS, duration, amplifier);
        target.addPotionEffect(slowEffect);
        
        spawnNewYearEffect(target.getLocation());
    }
    
    private void spawnNewYearEffect(Location location) {
        int multiplier = plugin.getSettings().getParticleMultiplier();
        
        for (int i = 0; i < 20 * multiplier; i++) {
            double offsetX = (Math.random() - 0.5) * 1.5;
            double offsetY = (Math.random() - 0.5) * 1.5;
            double offsetZ = (Math.random() - 0.5) * 1.5;
            location.getWorld().spawnParticle(Particle.DUST, location.clone().add(offsetX, offsetY, offsetZ), 1, new Particle.DustOptions(Color.WHITE, 1.0f));
        }
        
        for (int i = 0; i < 15 * multiplier; i++) {
            double offsetX = (Math.random() - 0.5) * 1.5;
            double offsetY = (Math.random() - 0.5) * 1.5;
            double offsetZ = (Math.random() - 0.5) * 1.5;
            
            Color[] colors = {Color.RED, Color.BLUE, Color.YELLOW, Color.SILVER, Color.WHITE, Color.FUCHSIA};
            Color randomColor = colors[(int)(Math.random() * colors.length)];
            
            location.getWorld().spawnParticle(Particle.DUST, location.clone().add(offsetX, offsetY, offsetZ), 1, new Particle.DustOptions(randomColor, 1.0f));
        }
        
        for (int i = 0; i < 10 * multiplier; i++) {
            double offsetX = (Math.random() - 0.5) * 1.0;
            double offsetY = (Math.random() - 0.5) * 1.0;
            double offsetZ = (Math.random() - 0.5) * 1.0;
            location.getWorld().spawnParticle(Particle.FLAME, location.clone().add(offsetX, offsetY, offsetZ), 1, 0.1, 0.1, 0.1, 0.1);
        }
    }
    
}