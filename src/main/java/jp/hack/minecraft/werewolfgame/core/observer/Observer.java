package jp.hack.minecraft.werewolfgame.core.observer;

import jp.hack.minecraft.werewolfgame.core.display.TaskGenerator;
import jp.hack.minecraft.werewolfgame.core.display.TaskManager;

public interface Observer {
    public abstract void update(TaskGenerator generator);
}
