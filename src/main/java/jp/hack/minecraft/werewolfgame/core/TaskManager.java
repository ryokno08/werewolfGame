package jp.hack.minecraft.werewolfgame.core;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.core.display.DisplayManager;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private Game game;
    private int maxTask = 10;
    private int finishedTask = 0;
    private List<Task> tasklist;

    public TaskManager(Game game){
        this.game = game;
    }

    public void taskFinished(int no) {
        tasklist.get(no).finished();
    }

    public void taskUpdate(int count) {
        finishedTask = count;

        DisplayManager manager = game.getDisplayManager();
        manager.setTask(maxTask / finishedTask);
    }

    public List<Task> getTasklist() {
        return tasklist;
    }

    public int getMaxTask() {
        return tasklist.size();
    }

    public void setMaxTask(int no) {
        maxTask = no;

        tasklist = new ArrayList<>();
        for(int i=0; i<no; i++) {
            this.tasklist.add(new Task(no));
        }
    }
}
