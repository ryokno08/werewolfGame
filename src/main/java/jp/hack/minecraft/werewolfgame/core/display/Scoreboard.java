package jp.hack.minecraft.werewolfgame.core.display;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Scoreboard {
    final JavaPlugin plugin;
    private final String name;
    private final DisplaySlot slot;
    private final Map<String, Integer> scores = new HashMap<>();
    private final Map<UUID, org.bukkit.scoreboard.Scoreboard> eachBoards = new HashMap<>();

    public Scoreboard(JavaPlugin plugin, String name, DisplaySlot slot) {
        this.plugin = plugin;
        this.name = name;
        this.slot = slot;
    }

    public void setPlayer(Player player) {
        org.bukkit.scoreboard.Scoreboard scoreboard;
        if(eachBoards.containsKey(player.getUniqueId())){
            scoreboard = eachBoards.get(player.getUniqueId());
        } else {
            scoreboard = getNewBoard();
            eachBoards.put(player.getUniqueId(), scoreboard);
        }

        player.setScoreboard(scoreboard);
    }

    public void setPlayers(List<Player> players) {
        players.forEach(this::setPlayer);
    }


    private org.bukkit.scoreboard.Scoreboard getNewBoard() {
        org.bukkit.scoreboard.Scoreboard scoreboard = plugin.getServer().getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(name, "dummy");
        scores.forEach((k, v) -> objective.getScore(k).setScore(v));
        register(scoreboard);

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

    public void disable() {
        eachBoards.forEach((key, value) -> {
            value.getObjective(name).unregister();
        });
    }

    public void register(org.bukkit.scoreboard.Scoreboard scoreboard) {
        scoreboard.getObjective(name).setDisplaySlot(slot);
    }

    public void register() {
        eachBoards.forEach((key, value) -> {
            register(value);
        });
    }

    public void unregister(org.bukkit.scoreboard.Scoreboard scoreboard) {
        scoreboard.clearSlot(slot);
    }

    public void unregister() {
        eachBoards.forEach((key, value) -> {
            unregister(value);
        });
    }

}
