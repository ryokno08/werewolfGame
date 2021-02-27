package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.display.DisplayManager;
import jp.hack.minecraft.werewolfgame.core.task.Task;
import jp.hack.minecraft.werewolfgame.core.task.TaskManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayingState extends GameState {
    private final JavaPlugin plugin;

    private BukkitRunnable runnable;

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

    public void update() {
    }

    @Override
    public void onStart(Game game) {
        super.onStart(game);
    }

    @Override
    public void onActive() {
        super.onActive();
        plugin.getLogger().info("PlayingStateに切り替わりました");

        Game game = ((GameConfigurator) plugin).getGame();
        DisplayManager displayManager = game.getDisplayManager();
        displayManager.setTaskBarVisible(true);

        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                System.out.println("タスク進行・人数　確認処理");

                TaskManager taskManager = game.getTaskManager();
                int count = 0;
                for (Task task : taskManager.getTaskList()) {
                    if (task.isFinished()) {
                        count++;
                    }
                }
                taskManager.setFinishedTask(count);
                game.confirmGame();
            }
        };
        runnable.runTaskTimer(plugin, 0, 20);

    }

    @Override
    public void onInactive() {
        super.onInactive();

        runnable.cancel();

        TaskManager taskManager = ((GameConfigurator) plugin).getGame().getTaskManager();
        taskManager.taskBarUpdate();
    }

    @Override
    public void onEnd() {
        super.onEnd();
        runnable.cancel();
    }
}
