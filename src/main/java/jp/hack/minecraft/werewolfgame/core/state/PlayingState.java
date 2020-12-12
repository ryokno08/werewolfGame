package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.core.Task;
import jp.hack.minecraft.werewolfgame.core.TaskManager;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayingState implements GameState {
    /*
    private static final PlayingState singleton = new PlayingState();
    private PlayingState(){}
    public static PlayingState getInstance() {
        return singleton;
    }

     */
    private Game currentGame;
    public PlayingState(Game game){
        currentGame = game;

        new BukkitRunnable() {

            @Override
            public void run() {
                TaskManager taskManager = currentGame.getTaskManager();
                int count = 0;
                for (Task task : taskManager.getTasklist()) {
                    if(task.isFinished()) {
                        count++;
                    }
                }
                taskManager.taskUpdate(count);
            }
        }.runTaskLater(game.getPlugin(), 20);
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
}
