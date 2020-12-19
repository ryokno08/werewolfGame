package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class MeetingState implements GameState {

    public MeetingState(){}
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
        Bukkit.broadcastMessage("MeetingStateに切り替わりました");
        // 設定などからロードする、単位は秒
        final int meetingLength = 15;

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        // チャット欄でなくBossBarやTitleなどを使うべき
        Bukkit.broadcastMessage("討論開始");
        for (int i = 0; i < meetingLength; i++) {
            int finalI = i;
            scheduler.scheduleSyncDelayedTask(game.getPlugin(), () -> Bukkit.broadcastMessage("投票まで"+ (meetingLength - finalI) +"秒"), 20 * i);
        }
        scheduler.scheduleSyncDelayedTask(game.getPlugin(), () -> game.nextState(), 20 * meetingLength);
    }
}
