package jp.hack.minecraft.werewolfgame.core.display;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VoteBoard {
    private final JavaPlugin plugin;
    private final String name;
    private final DisplaySlot slot;
    private final Scoreboard scoreboard;
    private final Objective objective;
    private final Map<String, Integer> scores = new HashMap<>();
    private final String SKIP_KEY = ChatColor.GREEN+"> SKIP";

    public VoteBoard(JavaPlugin plugin) {
        this.plugin = plugin;
        scoreboard = plugin.getServer().getScoreboardManager().getNewScoreboard();
        name = "VoteList";
        slot = DisplaySlot.SIDEBAR;
        objective = scoreboard.registerNewObjective(name, "dummy");
    }

    private void reset() {
        Game game = ((GameConfigurator) plugin).getGame();

        scores.clear();
        game.getJoinedPlayers().stream()
                .filter(player -> !game.getWPlayer(player.getUniqueId()).isDied())
                .forEach(player -> scores.put(ChatColor.RED.toString() + player.getDisplayName().toString(), 0));
        scores.put(SKIP_KEY, 0);

        update();
    }

    public void voteUpdate() {
        scores.put(SKIP_KEY, scores.getOrDefault(SKIP_KEY, 0) + 1);

        update();
    }

    public void voteUpdate(UUID uuid) {
        Game game = ((GameConfigurator) plugin).getGame();
        String key = ChatColor.RED.toString() + game.getPlayer(uuid).getDisplayName().toString();
        scores.put(key, scores.getOrDefault(key, 0) + 1);

        update();
    }

    private void setAllPlayer() {
        Game game = ((GameConfigurator) plugin).getGame();
        game.getJoinedPlayers().forEach(player -> player.setScoreboard(scoreboard));
    }

    private void update() {
        scores.forEach((key, value) -> {
            scoreboard.resetScores(key);
            objective.getScore(key).setScore(value);
        });
    }

    public void disable() {
        scoreboard.getObjective(name).unregister();
    }

    public void register() {
        reset();
        scoreboard.getObjective(name).setDisplaySlot(slot);
        setAllPlayer();
    }

    public void unregister() {
        scoreboard.clearSlot(slot);
    }
}
