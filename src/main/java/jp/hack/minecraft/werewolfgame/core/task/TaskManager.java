package jp.hack.minecraft.werewolfgame.core.task;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.core.WPlayer;
import jp.hack.minecraft.werewolfgame.core.display.DisplayManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private final Game game;
    private int maxTasks;
    private int finishedTask = 0;

    public TaskManager(Game game) {
        this.game = game;
        this.maxTasks = game.getNumberOfTasks();
    }

    public int getMaxTasks() {
        return maxTasks;
    }

    public void setMaxTasks(int maxTasks) {
        this.maxTasks = maxTasks;
    }

    private void updateFinishedTask() {
        int count = 0;
        for (WPlayer wPlayer : game.getWPlayers().values()) {
            for (Task task : wPlayer.getTasks()) {
                if (task.isFinished()) {
                    count++;
                }
            }
        }
        finishedTask = count;
    }

    public void onTaskFinished(WPlayer wPlayer, int no) {

        List<Task> taskList = wPlayer.getTasks();

        if (no > taskList.size()-1 || no < 0) {
            System.out.println("Unknown data");
            return;
        }
        taskList.get(no).finished();
        System.out.println("taskNo." + no + ": " + taskList.get(no).isFinished());

        updateFinishedTask();
        game.confirmGame();

    }

    public void taskBarUpdate() {

        DisplayManager manager = game.getDisplayManager();
        manager.setTask( (float) finishedTask / (float) maxTasks );

    }

    public void setTasks(WPlayer wPlayer) {
        wPlayer.clearTasks();
        List<Task> taskList = new ArrayList<>();
        for (int i=0; i<maxTasks; i++) {
            taskList.add(new Task(i));
        }
        wPlayer.setTasks(taskList);
    }

    public int getFinishedTask() {
        return finishedTask;
    }
}
