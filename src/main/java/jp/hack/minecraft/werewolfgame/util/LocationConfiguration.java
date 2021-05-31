package jp.hack.minecraft.werewolfgame.util;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LocationConfiguration {
    private final JavaPlugin plugin;
    private FileConfiguration config;

    public LocationConfiguration(JavaPlugin plugin) {
        this.plugin = plugin;
        config = plugin.getConfig();
    }

    public void load() {
        config = plugin.getConfig();
    }

    public void save() {
        plugin.saveConfig();
    }

    public Location getLocationData(String name) {
        synchronized (this) {
            return config.getSerializable(name, Location.class);
        }
    }

    public void setLocationData(String name, Location loc) {
        synchronized (this) {
            config.set(name, loc);
            save();
        }
    }

    public Boolean containKey(String key) {
        synchronized (this) {
            return config.contains(key);
        }
    }
}