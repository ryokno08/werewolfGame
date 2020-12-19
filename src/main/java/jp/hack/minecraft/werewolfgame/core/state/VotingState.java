package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class VotingState implements GameState {
    private JavaPlugin plugin;

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
    public void init(Game game) {
        Bukkit.broadcastMessage("VotingStateに切り替わりました");

        // 設定などからロードする、単位は秒
        final int voteLength = 120;

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        // チャット欄でなくBossBarやTitleなどを使うべき
        Bukkit.broadcastMessage("投票開始");
        for (int i = 0; i < voteLength; i++) {
            int finalI = i;
            scheduler.scheduleSyncDelayedTask(game.getPlugin(), () -> Bukkit.broadcastMessage("投票終了まで"+ (voteLength - finalI) +"秒"), 20 * i);
        }
        scheduler.scheduleSyncDelayedTask(game.getPlugin(), game::endEvent, 20 * voteLength);
    }
}
