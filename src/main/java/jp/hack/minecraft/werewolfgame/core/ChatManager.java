package jp.hack.minecraft.werewolfgame.core;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class ChatManager implements Listener {
    public ChatManager() {}

    @EventHandler
    public void onPlayerChat (AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        Game game = Game.getInstance();
        WPlayer wp = game.getWPlayer(p.getUniqueId());

        if (wp.getRoleType().isWolf()) return;

        e.setCancelled(true);
        p.sendMessage("You cannot send messages.");
    }

    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent e) {
        Player p = e.getPlayer();
        Game game = Game.getInstance();

        game.putWPlayer(new WPlayer(p.getUniqueId()));
    }
}
