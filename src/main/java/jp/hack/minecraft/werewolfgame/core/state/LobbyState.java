package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class LobbyState implements GameState {
    private final JavaPlugin plugin;
    private BukkitTask task;

    public LobbyState(JavaPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean canSpeak() {
        return false;
    }

    @Override
    public boolean canMove() {
        return true;
    }

    @Override
    public void update() {

    }

    @Override
    public void onStart(Game game) {
        Bukkit.broadcastMessage("LobbyStateに切り替わりました");
    }

    @Override
    public void onActive() {
        // 5秒ほどタイマー処理してそのあと下行を実行
        task =  new BukkitRunnable() {
            int counter = 0;
            @Override
            public void run() {
                counter++;
                if (counter < 5) {
                    // for(Player p : plugin.getServer().getOnlinePlayers()) p.sendTitle(,,,);
                }else{
                    Game game = ((GameConfigurator)plugin).getGame();
                    game.nextState();
                    task.cancel();
                }
            }
        }.runTaskLater(plugin, 20);
    }

    @Override
    public void onInactive() {

    }

    @Override
    public void onEnd() {

    }

}
