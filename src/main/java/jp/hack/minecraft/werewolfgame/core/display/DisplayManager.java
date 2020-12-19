package jp.hack.minecraft.werewolfgame.core.display;

import org.bukkit.entity.Player;

public class DisplayManager {
    TaskBar taskBar;

    public DisplayManager() {
        taskBar = new TaskBar();
        taskBar.setVisible(false);
    }

    public Boolean isTaskBarVisible() {
        return taskBar.isVisible();
    }

    public void setTaskBarVisible(Boolean visible) {
        taskBar.setVisible(visible);
    }

    public void setTask(float percent) {
        taskBar.setTask(percent);
    }

    public void addTaskBar(Player player) {
        taskBar.addPlayer(player);
    }
}
