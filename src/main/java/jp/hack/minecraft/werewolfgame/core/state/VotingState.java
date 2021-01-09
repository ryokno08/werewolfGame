package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class VotingState extends GameState {
    private final JavaPlugin plugin;
    private BukkitTask task;

    public VotingState(JavaPlugin plugin) {
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
        super.onStart(game);

    }

    @Override
    public void onActive() {
        super.onActive();
        Bukkit.broadcastMessage("VotingStateがアクティブになりました");

        // 設定などからロードする、単位は秒
        final int voteLength = 120;

        for (Player p : plugin.getServer().getOnlinePlayers()) {
            p.sendTitle("投票開始", "", 20, 20, 20);
        }
        if (task == null) {
            task = new BukkitRunnable() {
                int counter = 0;

                @Override
                public void run() {
                    counter++;
                    if (counter < voteLength) {
                        Bukkit.broadcastMessage("投票終了まで" + (voteLength - counter) + "秒");
                    } else {
                        Game game = ((GameConfigurator) plugin).getGame();
                        game.nextState();
                        task.cancel();
                    }
                }
            }.runTaskLater(plugin, 20);
        }
    }

    @Override
    public void onInactive() {
        super.onInactive();

    }

    @Override
    public void onEnd() {
        super.onEnd();

    }
}
