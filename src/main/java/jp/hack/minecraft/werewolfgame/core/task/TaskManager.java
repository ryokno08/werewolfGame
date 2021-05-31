package jp.hack.minecraft.werewolfgame.core.task;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.core.display.DisplayManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private final Game game;
    private int finishedTask = 0;

    public TaskManager(Game game) {
        this.game = game;
    }

    private void updateFinishedTask() {
        int count = 0;
        for (Task task : game.getTasks()) {
            if (task.isFinished()) {
                count++;
            }
        }
        finishedTask = count;
    }

    public void onTaskFinished(Player player, int no) {

        DisplayManager manager = game.getDisplayManager();

        game.getTasks().get(no).finished();
        manager.sendMessage(player, "009", no);

        updateFinishedTask();
        manager.updateTaskBoard();
        taskBarUpdate();
        
        game.getTaskBoard().addScores();
        game.confirmGame();

    }

    public void taskBarUpdate() {

        int numberOfTasks = game.getNumberOfTasks();
        DisplayManager manager = game.getDisplayManager();

        float progress = (float) finishedTask / (float) numberOfTasks;
        if (progress < 0.0) {
            manager.setProgress((float) 0.0);
            return;
        } else if (progress > 1.0) {
            manager.setProgress((float) 1.0);
            return;
        }
        manager.setProgress(progress);

    }

    public void setTasks() {

        int numberOfClue = game.getWPlayers().size() - game.getNumberOfImposter();
        int numberOfTasks = game.getNumberOfTasks();

        game.getTasks().clear();
        List<Task> taskList = new ArrayList<>();
        for (int i=0; i<numberOfClue * numberOfTasks; i++) {
            taskList.add(new Task(i));
        }
        game.setTasks(taskList);

    }

    public int getFinishedTask() {
        return finishedTask;
    }
}
