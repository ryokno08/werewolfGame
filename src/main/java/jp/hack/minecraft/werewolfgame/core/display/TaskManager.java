package jp.hack.minecraft.werewolfgame.core.display;

import jp.hack.minecraft.werewolfgame.core.Game;

public class TaskManager {
    private int maxTask = 10;
    private int finishedTask = 0;


    public void taskFinished() {
        Game game = Game.getInstance();

        finishedTask++;

        game.getDisplayManager().setTask(maxTask / finishedTask);

        if (maxTask <= finishedTask) {
            game.stop();
        }
    }

    public int getFinishedTask() {
        return finishedTask;
    }

    public int getMaxTask() {
        return maxTask;
    }

    public void setMaxTask(int maxTask) {
        this.maxTask = maxTask;
    }
}
