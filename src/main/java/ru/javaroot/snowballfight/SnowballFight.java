package ru.javaroot.snowballfight;

import org.bukkit.plugin.java.JavaPlugin;

public class SnowballFight extends JavaPlugin {
    
    private static SnowballFight instance;
    private Settings settings;
    private SnowballList snowballListener;
    
    @Override
    public void onEnable() {
        instance = this;
        
        settings = new Settings(this);
        settings.loadConfig();
        
        snowballListener = new SnowballList(this);
        
        getServer().getPluginManager().registerEvents(snowballListener, this);
        
        getCommand("snowballfight").setExecutor(new ru.javaroot.snowballfight.commands.ReloadCommand(this));
        getCommand("sbf").setExecutor(new ru.javaroot.snowballfight.commands.ReloadCommand(this));
        
        saveDefaultConfig();
        
        getLogger().info("SnowballFight плагин включен!");
    }
    
    
    @Override
    public void onDisable() {
        getLogger().info("SnowballFight плагин выключен!");
    }
    
    /**
     * Получить экземпляр плагина
     * @return экземпляр SnowballFight
     */
    public static SnowballFight getInstance() {
        return instance;
    }
    
    /**
     * Получить объект настроек
     * @return объект Settings
     */
    public Settings getSettings() {
        return settings;
    }
}