package jp.hack.minecraft.werewolfgame.core;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoinEvent (PlayerJoinEvent e) {
        Player player = e.getPlayer();
        Game.getInstance().getTaskBar().addPlayer(player);
    }

}
