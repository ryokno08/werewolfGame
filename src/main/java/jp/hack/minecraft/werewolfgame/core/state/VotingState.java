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
        plugin.getLogger().info("VotingStateがアクティブになりました");

        // 設定などからロードする、単位は秒
        final int voteLength = 120;

        plugin.getServer().getOnlinePlayers().forEach(player -> player.sendTitle("投票開始", "", 10, 20, 10));
        if (task == null) {
            task = new BukkitRunnable() {
                int counter = 0;

                @Override
                public void run() {
                    counter++;
                    if (counter < voteLength) {
                        plugin.getServer().getOnlinePlayers().forEach(player -> player.sendMessage("投票終了まで" + (voteLength - counter) + "秒"));
                    } else {
                        Game game = ((GameConfigurator) plugin).getGame();
                        game.nextState();
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 0, 20);
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
