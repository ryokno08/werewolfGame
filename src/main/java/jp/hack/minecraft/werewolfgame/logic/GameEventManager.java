package jp.hack.minecraft.werewolfgame.logic;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.WPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
        Player p = event.getPlayer();
        final Game game = ((GameConfigurator) plugin).getGame();
        game.putWPlayer(new WPlayer(p.getUniqueId()));
        game.getDisplayManager().addTaskBar(p);


        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, () -> {
            // デフォルト値の設定
            // if(lobbyLocation == null)  lobbyLocation = new Location(player.getWorld(),182,5,-134);
            if (game.getLobbyPos() != null) p.teleport(game.getLobbyPos());
        }, 1);
    }


    public final String PREFIX = ChatColor.RED + "[Wolf Side]" + ChatColor.GRAY;

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {

        Player p = e.getPlayer();
        Game game = ((GameConfigurator) plugin).getGame();
        WPlayer spokePlayer = game.getWPlayer(p.getUniqueId());
        String message = e.getMessage();

        if (game.canSpeak()) return; //　現在喋ることが許されているか取得し判断する。許されていたらreturn

        boolean canCommunicate = spokePlayer.getRole().isWolf() || (spokePlayer.getRole().isWolfSide() && game.canCommunicate());
        //　人狼かどうか判断＆オプションで狂人も人狼チャットにメッセージを送れる

        if (!canCommunicate) {

            e.setCancelled(true);
            p.sendMessage("You cannot send messages now.");
            return;
            //　市民側、狂人は基本こちらに流れる

        }

        //人狼は基本こちらに流れる。人狼陣営にしか見えないチャットを送ることができる。
        for (WPlayer wp : game.getwPlayers().values()) {

            canCommunicate = (wp.getRole().isWolf() || (wp.getRole().isWolfSide() && game.canCommunicate()));
            if (canCommunicate) Bukkit.getPlayer(wp.getUuid()).sendMessage(PREFIX + message);

        }

    }

    @EventHandler
    public void OnPlayerMove(PlayerMoveEvent e) {
        if (e.getFrom().getX() == e.getTo().getX() && e.getFrom().getY() == e.getTo().getY() && e.getFrom().getZ() == e.getTo().getZ())
            return;
        Game game = ((GameConfigurator) plugin).getGame();
        if (!game.canMove()) e.setCancelled(true);
    }
}
