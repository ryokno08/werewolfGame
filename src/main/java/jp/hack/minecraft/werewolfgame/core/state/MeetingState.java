package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.util.Messages;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.stream.Collectors;

public class MeetingState extends GameState {
    private BukkitTask task;

    public MeetingState(JavaPlugin plugin, Game game) {
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
        game.getDisplayManager().log("MeetingStateに切り替わりました");

        game.getVoteBoard().register();
        // 設定などからロードする、単位は秒
        final int meetingLength = 15;

        // 設定などからロードする、ロビーにてレポートするときの半径、単位はブロック
        final float robbyRadius = 3;
        List<Player> players = game.getWPlayers().values().stream()
                .filter(wPlayer -> !wPlayer.isDied())
                .map(wPlayer -> plugin.getServer().getPlayer(wPlayer.getUuid()))
                .collect(Collectors.toList());
        for (int i = 0; i < players.size(); i++) {
            double rad = (2 * Math.PI / (float) players.size()) * i;
            Player p = players.get(i);
            Location centerLoc = game.getMeetingPos();
            Location loc = new Location(p.getWorld(),
                    centerLoc.getX() + Math.cos(rad) * robbyRadius,
                    centerLoc.getY(),
                    centerLoc.getZ() + Math.sin(rad) * robbyRadius,
                    (360f / (float) players.size()) * i + 90,
                    0);
            p.teleport(loc);
        }

        game.getDisplayManager().allSendTitle(Messages.message("001"));
        if (task == null) {
            task = new BukkitRunnable() {
                int counter = 0;

                @Override
                public void run() {
                    counter++;
                    if (counter < meetingLength) {
                        game.getDisplayManager().allSendActionBarMessage("投票まで" + (meetingLength - counter) + "秒");
                    } else {
                        game.changeState(game.getVotingState());
                        this.cancel();
                    }
                }
            }.runTaskTimer(plugin, 0, 20);
        }
//        for (int i = 0; i < meetingLength; i++) {
//            String massage = "投票まで"+ (meetingLength - i) +"秒";
//            scheduler.scheduleSyncDelayedTask(plugin, () -> Bukkit.broadcastMessage(massage), 20 * i);
//        }
//        scheduler.scheduleSyncDelayedTask(plugin, () -> ((GameConfigurator)plugin).getGame().nextState(), 20 * meetingLength);
    }

    @Override
    public void onInactive() {
        super.onInactive();
        if (task != null) {
            if (!task.isCancelled()) task.cancel();
        }
        task = null;
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
