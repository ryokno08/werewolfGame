package jp.hack.minecraft.werewolfgame.util;

import jp.hack.minecraft.werewolfgame.core.utils.Configuration;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocationConfiguration extends Configuration {
    public LocationConfiguration(File configFile) {
        super(configFile);
        setTemplateName("config.yml");
        save();
    }


    public Location getLocationData(String name) {
        synchronized (this) {
            return super.getSerializable("location." + name, Location.class);
        }
    }

    public void setLocationData(String name, Location loc) {
        synchronized (this) {
            super.setProperty("location." + name, loc.serialize());
        }
    }
}
