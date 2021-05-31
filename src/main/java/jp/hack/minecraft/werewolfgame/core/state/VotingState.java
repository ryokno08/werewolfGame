package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

public class VotingState extends GameState {
    private BukkitTask task;

    public VotingState(JavaPlugin plugin, Game game) {
        super(plugin, game);
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
    public void onActive() {
        super.onActive();
        game.getDisplayManager().log("VotingStateがアクティブになりました");

        // 設定などからロードする、単位は秒
        int limitOfVoting = game.getLimitOfVoting();
        game.setVotedPlayers(new HashMap<>());
        game.setSkippedPlayers(new ArrayList<>());

        game.getDisplayManager().allSendTitle("投票開始");
        game.getAlivePlayer().forEach(player -> player.getInventory().addItem(game.getGuiLogic().getItem()));

        if (task == null) {
            task = new BukkitRunnable() {
                int counter = 0;

                @Override
                public void run() {
                    counter++;
                    if (counter >= limitOfVoting) {
                        game.resultVote();
                        this.cancel();
                    } else {
                        game.getDisplayManager().allSendActionBarMessage("投票終了まで" + (limitOfVoting - counter) + "秒");
                    }
                }
            }.runTaskTimer(plugin, 0, 20);
        }
    }

    @Override
    public void onInactive() {
        super.onInactive();

        if (task != null) {
            if (!task.isCancelled()) task.cancel();
        }
        task = null;

        game.getJoinedPlayers().forEach(player -> {
            game.getDisplayManager().clearWithoutArmor(player);
        });

        game.getVoteBoard().unregister();
    }

    @Override
    public void onEnd() {
        super.onEnd();

        if (task != null) {
            if (!task.isCancelled()) task.cancel();
        }
        task = null;
    }
}
