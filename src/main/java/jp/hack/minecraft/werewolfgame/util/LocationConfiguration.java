package jp.hack.minecraft.werewolfgame.util;

import jp.hack.minecraft.werewolfgame.core.utils.Configuration;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Map;

/*
public class LocationConfiguration extends Configuration {
    public LocationConfiguration(File configFile) {
        super(configFile);
        setTemplateName("config.yml");
        save();
    }


    public Location getLocationData(String name, JavaPlugin plugin) {
        synchronized (this) {
            System.out.println(name);
            System.out.println(super.getMap(name));
            System.out.println(super.getMap(name).values());
            System.out.println(super.getMap(name).get("world"));
            return new Location(
                    plugin.getServer().getWorld(super.getString(name + ".world")),
                    super.getDouble(name + ".x"),
                    super.getDouble(name + ".y"),
                    super.getDouble(name + ".z")
            );
            // Location.deserialize((Map<String, Object>) super.getProperty(name));
        }
    }

    public void setLocationData(String name, Location loc) {
        synchronized (this) {
            super.setProperty(name, loc);
            super.save();
        }
    }
}


 */
public class LocationConfiguration {
    private JavaPlugin plugin;
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
            System.out.println(name);
//            System.out.println(getMap(name));
//            System.out.println(getMap(name).values());
//            System.out.println(getMap(name).get("world"));

//            return new Location(
//                    plugin.getServer().getWorld(super.getString(name + ".world")),
//                    getDouble(name + ".x"),
//                    getDouble(name + ".y"),
//                    getDouble(name + ".z")
//            );

            return config.getSerializable(name, Location.class);
            // Location.deserialize((Map<String, Object>) super.getProperty(name));
        }
    }

    public void setLocationData(String name, Location loc) {
        synchronized (this) {
            config.set(name, loc);
            save();
            // super.setProperty(name, loc);
            // super.save();
        }
    }
}