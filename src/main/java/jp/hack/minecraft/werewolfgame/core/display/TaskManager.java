package jp.hack.minecraft.werewolfgame.core.display;

import jp.hack.minecraft.werewolfgame.core.Game;

import java.util.List;

public class TaskManager {

    public static class Task {
        int taskNo;
        boolean finished = false;
        public Task(int no){
            this.taskNo = no;
        }
    }


    private int maxTask = 10;
    private int finishedTask = 0;
    private List<Task> tasklist;

    public TaskManager(){
    }

    public void taskUpdate() {
        this.finishedTask = finishedTask;

        DisplayManager manager = Game.getInstance().getDisplayManager();
        manager.setTask(maxTask / finishedTask);
    }

    public int getMaxTask() {
        return tasklist.size();
    }

    public void setMaxTask(int no) {
        for(int i=0; i<no; i++) {
            this.tasklist.add(new Task(no));
        }
    }
}
