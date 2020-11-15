package jp.hack.minecraft.werewolfgame.logic;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class GameEventManager implements Listener {
    private JavaPlugin plugin;
    public GameEventManager(JavaPlugin Plugin) {
        plugin = Plugin;
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        GameDirector.onPlayerJoin(plugin, event);
    }
}
