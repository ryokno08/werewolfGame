package jp.hack.minecraft.werewolfgame.core.display;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Scoreboard {
    final JavaPlugin plugin;
    private final String name;
    private final String criteria;
    private final DisplaySlot slot;
    private final Map<String, Integer> scores = new HashMap<>();
    private final Map<UUID, org.bukkit.scoreboard.Scoreboard> eachBoards = new HashMap<>();

    public Scoreboard(JavaPlugin plugin, String name, DisplaySlot slot) {
        this.plugin = plugin;
        this.name = name;
        this.criteria = "dummy";
        this.slot = slot;
    }

    public void setPlayer(Player player) {
        org.bukkit.scoreboard.Scoreboard scoreboard = getNewBoard();
        eachBoards.put(player.getUniqueId(), scoreboard);
        player.setScoreboard(getNewBoard());
    }

    private org.bukkit.scoreboard.Scoreboard getNewBoard() {
        org.bukkit.scoreboard.Scoreboard scoreboard = plugin.getServer().getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(name, criteria);
        objective.setDisplaySlot(slot);
        scores.entrySet().forEach(v-> {
            objective.getScore(v.getKey()).setScore(v.getValue());
        });

        return scoreboard;
    }

    public Map<String, Integer> getScores() {
        return scores;
    }

    public Map<UUID, org.bukkit.scoreboard.Scoreboard> getEachBoards() {
        return eachBoards;
    }

    public void removeScore(Player player, String entry) {
        eachBoards.get(player.getUniqueId()).resetScores(entry);
    }
}
