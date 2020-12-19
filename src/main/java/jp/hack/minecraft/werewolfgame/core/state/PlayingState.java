package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.core.Task;
import jp.hack.minecraft.werewolfgame.core.TaskManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayingState implements GameState {
    private JavaPlugin plugin;

    private BukkitRunnable bukkitRunnable;

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
    public void init(Game game) {
        if(bukkitRunnable == null) {
            bukkitRunnable = new BukkitRunnable() {
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
            };
            bukkitRunnable.runTaskLater(game.getPlugin(), 20);
        }
    }
}
