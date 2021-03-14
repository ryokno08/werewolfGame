package jp.hack.minecraft.werewolfgame.util;

import jp.hack.minecraft.werewolfgame.core.utils.Configuration;
import org.bukkit.Location;

import java.io.File;
import java.util.Map;

public class LocationConfiguration extends Configuration {
    public LocationConfiguration(File configFile) {
        super(configFile);
        setTemplateName("config.yml");
        save();
    }


    public Location getLocationData(String name) {
        synchronized (this) {
            System.out.println(name);
            System.out.println(super.getMap(name));
            System.out.println(super.getMap(name).values());
            System.out.println(super.getMap(name).get("world"));
            return Location.deserialize(super.getMap(name));
        }
    }

    public void setLocationData(String name, Location loc) {
        synchronized (this) {
            super.setProperty(name, loc.serialize());
            super.save();
        }
    }
}
