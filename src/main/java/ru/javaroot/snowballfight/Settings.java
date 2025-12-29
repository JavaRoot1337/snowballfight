package ru.javaroot.snowballfight;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Settings {
    
    private final SnowballFight plugin;
    private FileConfiguration config;
    private FileConfiguration messageConfig;
    
    private int slownessDuration;
    private int slownessAmplifier;
    private boolean cooldownEnabled;
    private long cooldownDuration;
    private boolean soundEnabled;
    private String soundType;
    private int particleMultiplier;
    private String reloadSuccessMessage;
    private String noPermissionMessage;
    private String commandUsageMessage;
    private String playerOnlyMessage;
    
    public Settings(SnowballFight plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.messageConfig = null;
    }
    

    public void loadConfig() {
        plugin.saveDefaultConfig();
        
        plugin.saveDefaultConfig();
        
        reloadConfig();
        loadMessages();
    }
    
    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
        
        slownessDuration = config.getInt("snowball_effects.slowness.duration", 3);
        slownessAmplifier = config.getInt("snowball_effects.slowness.amplifier", 4);
        
        cooldownEnabled = config.getBoolean("snowball_cooldown.enabled", true);
        cooldownDuration = config.getLong("snowball_cooldown.duration", 2);
        
        soundEnabled = config.getBoolean("snowball_sound.enabled", true);
        soundType = config.getString("snowball_sound.sound_type", "minecraft:entity.firework_rocket.blast");
        
        particleMultiplier = config.getInt("particle_effects.multiplier", 1);
    }

    public void loadMessages() {
        File messageFile = new File(plugin.getDataFolder(), "message.yml");
        if (!messageFile.exists()) {
            plugin.saveResource("message.yml", true);
        }

        messageConfig = YamlConfiguration.loadConfiguration(messageFile);
        reloadSuccessMessage = messageConfig.getString("reload_success", "&aПлагин перезагружен");
        noPermissionMessage = messageConfig.getString("no_permission", "&cУ вас нет прав на выполнение этой команды!");
        commandUsageMessage = messageConfig.getString("command_usage", "&cИспользование: /%command% [reload]");
        playerOnlyMessage = messageConfig.getString("player_only", "&cТолько игрок может выполнить эту команду!");
    }
    
    public int getSlownessDuration() {
        return slownessDuration;
    }
    
    public int getSlownessAmplifier() {
        return slownessAmplifier;
    }
    
    public boolean isCooldownEnabled() {
        return cooldownEnabled;
    }

    public String getReloadSuccessMessage() {
        return reloadSuccessMessage;
    }

    public String getNoPermissionMessage() {
        return noPermissionMessage;
    }

    public String getCommandUsageMessage() {
        return commandUsageMessage;
    }

    public String getPlayerOnlyMessage() {
        return playerOnlyMessage;
    }
    
    public long getCooldownDuration() {
        return cooldownDuration;
    }

    public long getCooldownDurationInMilliseconds() {
        return cooldownDuration * 1000;
    }
    
    public boolean isSoundEnabled() {
        return soundEnabled;
    }
    
    public String getSoundType() {
        return soundType;
    }
    
    public int getParticleMultiplier() {
        return particleMultiplier;
    }
}