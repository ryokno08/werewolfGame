package jp.hack.minecraft.werewolfgame.core.observer;

import jp.hack.minecraft.werewolfgame.core.Game;
import jp.hack.minecraft.werewolfgame.core.display.TaskGenerator;
import jp.hack.minecraft.werewolfgame.core.display.TaskManager;

public class TaskObserver implements Observer {

    @Override
    public void update(TaskGenerator generator) {
        // 観察者が持つ乱数を取得して表示
        TaskManager manager = Game.getInstance().getTaskManager();
        manager.taskUpdate(generator.getFinishedTask());

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
    }
}
