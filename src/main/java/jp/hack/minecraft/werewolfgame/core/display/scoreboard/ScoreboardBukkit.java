package jp.hack.minecraft.werewolfgame.core.display.scoreboard;

import org.bukkit.Bukkit;
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
        boolean isThereAObjective = scoreboard.getObjectives().stream()
                .anyMatch(objective -> id.equals(objective.getName()));

        if (isThereAObjective) {
            objective = scoreboard.getObjective(id);
        } else {
            objective = scoreboard.registerNewObjective(id, "dummy");
        }
    }

    public void setScore(String entry, int score) {
        objective.getScore(entry).setScore(score);
    }

    public void setScoreboard(Player player) {
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(scoreboard);
    }

    public void setDisplaySlot(DisplaySlot slot) {
        objective.setDisplaySlot(slot);
    }

    public void resetScoreboard(Player player) {
        player.setScoreboard(manager.getMainScoreboard());
    }
}
