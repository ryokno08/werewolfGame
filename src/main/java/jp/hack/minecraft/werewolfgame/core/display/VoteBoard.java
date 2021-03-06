package jp.hack.minecraft.werewolfgame.core.display;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Objects;

public class VoteBoard {
    private final Scoreboard scoreboard;
    private Objective objective;
    private Boolean isVisible;

    VoteBoard() {
        scoreboard = Objects.requireNonNull(Bukkit.getServer().getScoreboardManager()).getNewScoreboard();
        objective = scoreboard.registerNewObjective("VoteBoard", "dummy");
        isVisible = false;
    }

    public void registerPlayerNames() {
        Bukkit.getOnlinePlayers().forEach(p -> {
            p.setScoreboard(scoreboard);
        });
    }

    public void setVisible(Boolean isVisible) {
        this.isVisible = isVisible;

        Bukkit.getServer().getOnlinePlayers().forEach(p ->{
            if (isVisible) {
            } else {

            }
        });
    }

    public Boolean isVisible() {
        return isVisible;
    }
    
}
