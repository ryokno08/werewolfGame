package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class MeetingState extends GameState {
    private final JavaPlugin plugin;
    private BukkitTask task;

    public MeetingState(JavaPlugin plugin){
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
        Bukkit.broadcastMessage("MeetingStateに切り替わりました");
        // 設定などからロードする、単位は秒
        final int meetingLength = 15;

        for (Player p : plugin.getServer().getOnlinePlayers()) {
            p.sendTitle("討論開始", "", 20, 20, 20);
        }
        if(task == null) {
            task = new BukkitRunnable() {
                int counter = 0;

                @Override
                public void run() {
                    counter++;
                    if (counter < meetingLength) {
                        Bukkit.broadcastMessage("投票まで" + (meetingLength - counter) + "秒");
                    } else {
                        Game game = ((GameConfigurator) plugin).getGame();
                        game.nextState();
                        task.cancel();
                    }
                }
            }.runTaskLater(plugin, 20);
        }
//        for (int i = 0; i < meetingLength; i++) {
//            String massage = "投票まで"+ (meetingLength - i) +"秒";
//            scheduler.scheduleSyncDelayedTask(plugin, () -> Bukkit.broadcastMessage(massage), 20 * i);
//        }
//        scheduler.scheduleSyncDelayedTask(plugin, () -> ((GameConfigurator)plugin).getGame().nextState(), 20 * meetingLength);
    }

    @Override
    public void onInactive() {
        task.cancel();
        task = null;
    }

    @Override
    public void onEnd() {
        task.cancel();
        task = null;
    }
}
