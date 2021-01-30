package jp.hack.minecraft.werewolfgame.core.utils;

import org.bukkit.entity.Player;

public class Scoreboard {
    private final ScoreboardBukkit scoreboardBukkit;
    private final String id;

    public Scoreboard(String id, String displayName) {
        this.id = id;
        scoreboardBukkit = new ScoreboardBukkit(id, displayName);
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
