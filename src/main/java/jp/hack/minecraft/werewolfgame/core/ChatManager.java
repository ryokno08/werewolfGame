package jp.hack.minecraft.werewolfgame.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class ChatManager implements Listener {
    public ChatManager() {}
    public final String PREFIX = ChatColor.RED + "[Wolf Side]" + ChatColor.GRAY;

    @EventHandler
    public void onPlayerChat (AsyncPlayerChatEvent e) {

        Player p = e.getPlayer();
        Game game = Game.getInstance();
        WPlayer spokePlayer = game.getWPlayer(p.getUniqueId());
        String message = e.getMessage();

        if ( game.canTalk() ) return; //　現在喋ることが許されているか取得し判断する。許されていたらreturn

        Boolean canCommunicate = spokePlayer.getRole().isWolf() || ( spokePlayer.getRole().isWolfSide() && game.canCommunicate() );
        //　人狼かどうか判断＆オプションで狂人も人狼チャットにメッセージを送れる

        if ( !canCommunicate ) {

            e.setCancelled(true);
            p.sendMessage("You cannot send messages now.");
            return;
            //　市民側、狂人は基本こちらに流れる

        }

        //人狼は基本こちらに流れる。人狼陣営にしか見えないチャットを送ることができる。
        for (WPlayer wp : game.getwPlayers().values()) {

            canCommunicate = ( wp.getRole().isWolf() || ( wp.getRole().isWolfSide() && game.canCommunicate() ) );
            if ( canCommunicate ) Bukkit.getPlayer(wp.getUuid()).sendMessage(PREFIX + message);

        }

    }

}
