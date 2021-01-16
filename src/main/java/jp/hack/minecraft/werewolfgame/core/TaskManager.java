package jp.hack.minecraft.werewolfgame.core;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.core.display.DisplayManager;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private Game game;
    private int maxTask = 10;
    private int finishedTask = 0;
    private List<Task> taskList;

    public TaskManager(Game game) {
        this.game = game;
        setTaskList(maxTask);
    }

    public void taskFinished(int no) {
        taskList.get(no).finished();
        System.out.println("taskNo." + no + ": " + taskList.get(no).isFinished());
    }

    public void taskUpdate(int count) {
        finishedTask = count;

        DisplayManager manager = game.getDisplayManager();
        manager.setTask(maxTask / finishedTask);
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(int no) {
        maxTask = no;

        taskList = new ArrayList<>();
        for (int i = 0; i < no; i++) {
            this.taskList.add(new Task(no));
        }
    }
}
