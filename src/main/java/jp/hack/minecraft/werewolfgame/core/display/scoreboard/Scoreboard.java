package jp.hack.minecraft.werewolfgame.core.display.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

public class Scoreboard {
    private final ScoreboardBukkit scoreboardBukkit;
    private final String id;

    public Scoreboard(String id, String displayName) {
        this.id = id;
        scoreboardBukkit = new ScoreboardBukkit(id, displayName);
    }

    public Scoreboard(String id, String displayName, DisplaySlot slot) {
        this.id = id;
        scoreboardBukkit = new ScoreboardBukkit(id, displayName);
        scoreboardBukkit.setDisplaySlot(slot);
    }

    public String getId() {
        return id;
    }

    public void setScore(String playerName, int score) {
        scoreboardBukkit.setScore(playerName, score);
    }

    public void setScoreboard(Player player) {
        scoreboardBukkit.setScoreboard(player);
    }

    public void resetScoreboard(Player player) {
        scoreboardBukkit.resetScoreboard(player);
    }
}
