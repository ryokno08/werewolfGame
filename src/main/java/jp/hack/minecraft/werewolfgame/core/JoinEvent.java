package jp.hack.minecraft.werewolfgame.core;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoinEvent (PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Game game = Game.getInstance();

        game.putWPlayer(new WPlayer(p.getUniqueId()));

        Game.getInstance().getTaskBar().addPlayer(p);

        game.getTaskBar().setTask(0);
    }

}
