package jp.hack.minecraft.werewolfgame.core;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TimeManager {

    private final JavaPlugin plugin;
    private int counter;

    public TimeManager(JavaPlugin plugin, int counter) {
        this.plugin = plugin;

        this.counter = counter;
    }
}
