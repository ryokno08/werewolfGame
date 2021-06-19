package jp.hack.minecraft.werewolfgame.logic;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.WPlayer;
import jp.hack.minecraft.werewolfgame.core.state.PlayingState;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.ArmorStand;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.*;
import org.bukkit.material.Button;
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

        game.getGameDirector().onPlayerJoin(e);
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
        Location from = e.getFrom();
        Location to = e.getTo();
        if (from.getX() == to.getX() && from.getZ() == to.getZ() && from.getY() == to.getY()) return;

        this.game = ((GameConfigurator) this.plugin).getGame();
        if (game == null) return;
        if (!game.wasStarted()) return;
        if (game.getWPlayer(e.getPlayer().getUniqueId()).wasDied()) return;
        WPlayer wPlayer = game.getWPlayer(e.getPlayer().getUniqueId());
        if (game.canMove() && wPlayer.canMove()) return;
        e.setCancelled(true);
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
        if (!(e.getDamager() instanceof Player && (e.getEntity() instanceof Player || e.getEntity() instanceof ArmorStand)))
            return;

        game.getGameDirector().onPlayerAttack(e);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        System.out.println(e.getEventName());
        this.game = ((GameConfigurator) this.plugin).getGame();
        if (game == null) return;
        if (!game.wasStarted()) return;

        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.SPECTATOR) {
            /*
            if (game.getWPlayer(player.getUniqueId()).wasDied()) {
             */
                game.getGameDirector().onSpectatorInteract(e);
                /*
            } else {
                game.getGameDirector().onSpectatorInteract(player);
                return;
            }

                 */
        } else {
            if (e.getClickedBlock().getState().getData() instanceof Button) return;
            e.setCancelled(true);
            if (game.getWPlayer(e.getPlayer().getUniqueId()).wasDied()) return;
            Action action = e.getAction();
            if (!(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))) return;
            game.getGameDirector().onPlayerInteract(e);
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        System.out.println(e.getEventName());
        this.game = ((GameConfigurator) this.plugin).getGame();
        if (game == null) return;
        if (!game.wasStarted()) return;
        e.setCancelled(true);
        if (game.getWPlayer(e.getPlayer().getUniqueId()).wasDied()) return;
        Player player = e.getPlayer();
        if (!player.getGameMode().equals(GameMode.SPECTATOR)) return;
        if (game.getWPlayer(player.getUniqueId()).wasDied()) return;
        game.getGameDirector().onSpectatorInteract(player);
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e) {
        System.out.println(e.getEventName());
        this.game = ((GameConfigurator) this.plugin).getGame();
        if (game == null) return;
        if (!game.wasStarted()) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryMoveItemEvent e) {
        System.out.println(e.getEventName());
        this.game = ((GameConfigurator) this.plugin).getGame();
        if (game == null) return;
        if (!game.wasStarted()) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryPickupItem(InventoryPickupItemEvent e) {
        System.out.println(e.getEventName());
        this.game = ((GameConfigurator) this.plugin).getGame();
        if (game == null) return;
        if (!game.wasStarted()) return;
        e.setCancelled(true);
    }

}