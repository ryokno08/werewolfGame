package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.core.Task;
import jp.hack.minecraft.werewolfgame.core.TaskManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class PlayingState extends GameState {
    private final JavaPlugin plugin;

    private BukkitTask bukkitTask;

    public PlayingState(JavaPlugin plugin) {
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

    public void update(){}

    @Override
    public void onStart(Game game) {
        super.onStart(game);
        Bukkit.broadcastMessage("PlayingStateに切り替わりました");
        if(bukkitTask == null) {
            bukkitTask = new BukkitRunnable() {
                @Override
                public void run() {
                    TaskManager taskManager = game.getTaskManager();
                    int count = 0;
                    for (Task task : taskManager.getTaskList()) {
                        if(task.isFinished()) {
                            count++;
                        }
                    }
                    taskManager.taskUpdate(count);
                }
            }.runTaskLater(plugin, 20);

        }
    }

    @Override
    public void onActive() {
        bukkitTask.cancel();
    }

    @Override
    public void onInactive() {

    }

    @Override
    public void onEnd() {
        if (bukkitTask != null) bukkitTask.cancel();
    }
}
