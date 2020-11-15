package jp.hack.minecraft.werewolfgame.logic;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class GameDirector {
    public static Location lobbyLocation;
    public static void onPlayerJoin(JavaPlugin plugin, PlayerJoinEvent event){
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                Player player = event.getPlayer();
                // デフォルト値の設定
                // if(lobbyLocation == null)  lobbyLocation = new Location(player.getWorld(),182,5,-134);
                if(lobbyLocation != null) player.teleport(lobbyLocation);
            }
        }, 1);
    }
}
