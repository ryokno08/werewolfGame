package jp.hack.minecraft.werewolfgame.core.display;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

public class Scoreboard {
    final JavaPlugin plugin;
    final org.bukkit.scoreboard.Scoreboard scoreboard;
    Objective objective;

    public Scoreboard(JavaPlugin plugin) {
        this.plugin = plugin;
        scoreboard = plugin.getServer().getScoreboardManager().getNewScoreboard();
    }

    public void setObjective(String name, String criteria, DisplaySlot slot) {
        objective = scoreboard.registerNewObjective(name, criteria);
        objective.setDisplaySlot(slot);
    }

    public void setPlayer(Player player) {
        player.setScoreboard(scoreboard);
    }

    public Objective getObjective() {
        return objective;
    }

    public org.bukkit.scoreboard.Scoreboard getScoreboard() {
        return scoreboard;
    }

    public void setScore(String name, int value) {
        Score score = objective.getScore(name);
        score.setScore(value);
    }

    public void removeScore(Player player, String name) {
        player.getScoreboard().resetScores(name);
    }
}
