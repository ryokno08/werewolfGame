package jp.hack.minecraft.werewolfgame.logic;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.state.PlayingState;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

public class GameEventManager implements Listener {
    private final JavaPlugin plugin;
    private Game game;

    public GameEventManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        this.game = ((GameConfigurator) this.plugin).getGame();
        if (game == null) return;
        if (game.wasStarted()) return;

        game.getGameLogic().onPlayerJoin(e);
    }


    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        this.game = ((GameConfigurator) this.plugin).getGame();
        if (game == null) return;
        if (!game.wasStarted()) return;
        if (game.canSpeak()) return; //　現在喋ることが許されているか取得し判断する。許されていたらreturn
        e.setCancelled(true);

        e.getPlayer().sendMessage(ChatColor.RED + "現在、チャットで会話することはできません。");

    }

    @EventHandler
    public void onPlayerPickItem(EntityPickupItemEvent e) {
        this.game = ((GameConfigurator) this.plugin).getGame();
        if (game == null) return;
        if (!game.wasStarted()) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        this.game = ((GameConfigurator) this.plugin).getGame();
        if (game == null) return;
        if (!game.wasStarted()) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        this.game = ((GameConfigurator) this.plugin).getGame();
        if (game == null) return;
        if (!game.wasStarted()) return;
        if (game.getWPlayer(e.getPlayer().getUniqueId()).isDied()) return;
        if (e.getFrom().getX() == e.getTo().getX() && e.getFrom().getZ() == e.getTo().getZ()) return;
        if (!game.canMove()) e.setCancelled(true);
        if (game.getWPlayer(e.getPlayer().getUniqueId()).isKilling()) e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        this.game = ((GameConfigurator) this.plugin).getGame();
        if (game == null) return;
        if (!game.wasStarted()) return;

        Player player = e.getPlayer();
        player.getInventory().clear();
        player.setGameMode(GameMode.SPECTATOR);
        game.removePlayer(player);
        game.confirmGame();
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        this.game = ((GameConfigurator) this.plugin).getGame();
        if (game == null) return;
        if (!game.wasStarted()) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        this.game = ((GameConfigurator) this.plugin).getGame();
        if (game == null) return;
        if (!game.wasStarted()) return;
        e.setCancelled(true);

        if (!(game.getCurrentState() instanceof PlayingState)) return;
        if (!(e.getDamager() instanceof Player && e.getEntity() instanceof Player)) return;

        game.getGameLogic().onPlayerAttack(e);
    }

    @EventHandler
    public void onPlayerItemAction(PlayerInteractEvent e) {
        this.game = ((GameConfigurator) this.plugin).getGame();
        if (game == null) return;
        if (!game.wasStarted()) return;
        if (game.getWPlayer(e.getPlayer().getUniqueId()).isDied()) return;

        Action action = e.getAction();
        if (!(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))) return;

        game.getGameLogic().onPlayerItemAction(e);
    }

}