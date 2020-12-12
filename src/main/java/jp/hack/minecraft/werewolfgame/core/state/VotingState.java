package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class VotingState implements GameState {
    /*
    private static final VotingState singleton = new VotingState();
    private VotingState(){}
    public static VotingState getInstance() {
        return singleton;
    }

     */
    private Game currentGame;
    public VotingState(Game game){
        currentGame = game;
        // votingLogic(plugin);
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

    public void votingLogic(JavaPlugin plugin) {
        // 設定などからロードする、単位は秒
        final int voteLength = 120;

        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();

        // チャット欄でなくBossBarやTitleなどを使うべき
        Bukkit.broadcastMessage("投票開始");
        for (int i = 0; i < voteLength; i++) {
            int finalI = i;
            scheduler.scheduleSyncDelayedTask(plugin, () -> Bukkit.broadcastMessage("投票終了まで"+ (voteLength - finalI) +"秒"), 20 * i);
        }
        scheduler.scheduleSyncDelayedTask(plugin, () -> currentGame.endEvent(), 20 * voteLength);
    }
}
