package jp.hack.minecraft.werewolfgame.core.display;

import jp.hack.minecraft.werewolfgame.core.Game;

public class TaskManager extends TaskGenerator{
    private int maxTask = 10;
    private int finishedTask = 0;

    @Override
    public int getFinishedTask() {
        return finishedTask;
    }

    public void taskUpdate(int finishedTask) {
        this.finishedTask = finishedTask;

        DisplayManager manager = Game.getInstance().getDisplayManager();
        manager.setTask(maxTask / finishedTask);
    }

    public int getMaxTask() {
        return maxTask;
    }

    public void setMaxTask(int maxTask) {
        this.maxTask = maxTask;
    }
}
