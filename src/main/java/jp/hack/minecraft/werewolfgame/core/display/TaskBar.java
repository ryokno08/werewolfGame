package jp.hack.minecraft.werewolfgame.core.display;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;


public class TaskBar {
    private final String title = ChatColor.GREEN + "Task Progress";
    private final BarColor color = BarColor.GREEN;
    private final BarStyle style = BarStyle.SOLID;
    private final BossBar taskBar;

    public TaskBar() {
        taskBar = Bukkit.createBossBar(title, color, style);
    }

    public void addPlayer(Player player) {
        taskBar.addPlayer(player);
    }

    public Boolean isVisible() {
        return taskBar.isVisible();
    }

    public void setVisible(Boolean visible) {
        taskBar.setVisible(visible);
    }

    public void setTask(float percent) {
        taskBar.setProgress(percent);
    }
}
