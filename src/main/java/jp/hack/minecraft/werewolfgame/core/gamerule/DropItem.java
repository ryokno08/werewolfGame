package jp.hack.minecraft.werewolfgame.core.gamerule;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;

public class DropItem {
    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }
}
