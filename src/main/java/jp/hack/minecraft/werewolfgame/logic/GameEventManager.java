package jp.hack.minecraft.werewolfgame.logic;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.Role;
import jp.hack.minecraft.werewolfgame.core.WPlayer;
import jp.hack.minecraft.werewolfgame.core.gamerule.PlayerKill;
import jp.hack.minecraft.werewolfgame.core.state.PlayingState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class GameEventManager implements Listener {
    private JavaPlugin plugin;

    public GameEventManager(JavaPlugin Plugin) {
        plugin = Plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Game game = ((GameConfigurator) plugin).getGame();
        if (game == null) return;
        if (!game.wasStarted()) return;

        Player p = event.getPlayer();
        game.putWPlayer(new WPlayer(p.getUniqueId()));
        game.getDisplayManager().addTaskBar(p);


        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, () -> {
            // デフォルト値の設定
            // if(lobbyLocation == null)  lobbyLocation = new Location(player.getWorld(),182,5,-134);
            if (game.getLobbyPos() != null) p.teleport(game.getLobbyPos());
        }, 1);
    }


    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Game game = ((GameConfigurator) plugin).getGame();
        if (game == null) return;
        if (!game.wasStarted()) return;

        Player p = e.getPlayer();

        if (game.canSpeak()) return; //　現在喋ることが許されているか取得し判断する。許されていたらreturn

        e.setCancelled(true);
        p.sendMessage(ChatColor.RED+"現在、チャットで会話することはできません。");

    }

    @EventHandler
    public void OnPlayerMove(PlayerMoveEvent e) {
        Game game = ((GameConfigurator) plugin).getGame();
        if (game == null) return;
        if (!game.wasStarted()) return;

        if (e.getFrom().getX() == e.getTo().getX() && e.getFrom().getY() == e.getTo().getY() && e.getFrom().getZ() == e.getTo().getZ())
            return;
        if (!game.canMove()) e.setCancelled(true);
        if (game.getWPlayer(e.getPlayer().getUniqueId()).isKilling()) e.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        Game game = ((GameConfigurator) plugin).getGame();
        if (game == null) return;
        if (!game.wasStarted()) return;

        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player damager = (Player) e.getDamager();
            Player attacker = (Player) e.getEntity();

            if ( game.getCurrentState() instanceof PlayingState ) {

                if (Role.VILLAGER.equals(game.getPlayerRole(damager.getUniqueId())))
                    if (Role.WOLF.equals(game.getPlayerRole(attacker.getUniqueId()))) {
                        if (attacker.getInventory().getItemInMainHand().getType().equals(game.getItemForKill().getType())) {

                            System.out.println( "[!KILL]" + attacker.getDisplayName() + " killed " + damager.getDisplayName() );

                            PlayerKill playerKill = new PlayerKill(plugin);
                            playerKill.OnPlayerAttack(e);

                            return;
                        }
                    }
            }
        }
        e.setCancelled(true);
    }
}
