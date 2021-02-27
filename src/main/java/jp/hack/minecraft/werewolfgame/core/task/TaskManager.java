package jp.hack.minecraft.werewolfgame.core.task;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.core.display.DisplayManager;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private final Game game;
    private int maxTasks;
    private int finishedTask = 0;
    private List<Task> taskList;

    public TaskManager(Game game) {
        this.game = game;
        this.maxTasks = game.getNumberOfTasks();
        setTasks(maxTasks);
    }

    public void onTaskFinished(int no) {
        if (taskList.size() <= no - 1) {
            System.out.println("Unknown data");
            return;
        }
        taskList.get(no).finished();
        System.out.println("taskNo." + no + ": " + taskList.get(no).isFinished());
    }

    public void taskBarUpdate() {

        DisplayManager manager = game.getDisplayManager();
        manager.setTask( (float)finishedTask / (float) maxTasks);

    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTasks(int no) {
        taskList = new ArrayList<>();
        for (int i = 0; i < no; i++) {
            this.taskList.add(new Task(no));
        }
    }

    public int getMaxTasks() {
        return maxTasks;
    }

    public void setMaxTasks(int maxTasks) {
        this.maxTasks = maxTasks;
    }

    public int getFinishedTask() {
        return finishedTask;
    }

    public void setFinishedTask(int finishedTask) {
        this.finishedTask = finishedTask;
    }
}
