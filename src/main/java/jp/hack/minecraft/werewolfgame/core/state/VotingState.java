package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class VotingState implements GameState {
    private final JavaPlugin plugin;

    public VotingState(JavaPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean canSpeak() {
        return true;
    }

    @Override
    public boolean canMove() {
        return false;
    }

    @Override
    public void update() {
        
    }

    @Override
    public void onStart(Game game) {

    }

    @Override
    public void onActive() {
        Bukkit.broadcastMessage("VotingStateがアクティブになりました");

        // 設定などからロードする、単位は秒
        final int voteLength = 120;

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        for (Player p : plugin.getServer().getOnlinePlayers()) {
            p.sendTitle("投票開始", "", 20, 20, 20);
        }
        for (int i = 0; i < voteLength; i++) {
            String massage = "投票終了まで"+ (voteLength - i) +"秒";
            scheduler.scheduleSyncDelayedTask(plugin, () -> Bukkit.broadcastMessage(massage), 20 * i);
        }
        scheduler.scheduleSyncDelayedTask(plugin, () -> ((GameConfigurator)plugin).getGame().nextState(), 20 * voteLength);
    }

    @Override
    public void onInactive() {

    }

    @Override
    public void onEnd() {

    }
}
