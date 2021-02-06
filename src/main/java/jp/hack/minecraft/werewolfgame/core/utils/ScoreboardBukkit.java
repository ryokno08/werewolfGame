package jp.hack.minecraft.werewolfgame.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class ScoreboardBukkit {
    private final ScoreboardManager manager = Bukkit.getScoreboardManager();
    private final Scoreboard scoreboard = manager.getNewScoreboard();
    private String displayName;
    private Objective objective;

    ScoreboardBukkit(String id, String displayName) {
        this.displayName = displayName;
        Boolean isThereAObjective = scoreboard.getObjectives().stream()
                .filter(objective -> id.equals(objective.getName()))
                .findAny()
                .isPresent();

        if (isThereAObjective) {
            objective = scoreboard.getObjective(id);
        } else {
            objective = scoreboard.registerNewObjective(id, "dummy", this.displayName);
        }
    }

    public void setScore(String entry, int score) {
        objective.getScore(entry).setScore(score);
    }

    public void setScoreboard(Player player) {
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(scoreboard);
    }

    public void setScoreboard(Player player, DisplaySlot slot) {
        objective.setDisplaySlot(slot);
        player.setScoreboard(scoreboard);
    }

    public void resetScoreboard(Player player) {
        player.setScoreboard(manager.getMainScoreboard());
    }
}
