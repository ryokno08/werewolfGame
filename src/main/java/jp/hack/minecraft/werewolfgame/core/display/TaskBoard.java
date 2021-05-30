package jp.hack.minecraft.werewolfgame.core.display;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.task.Task;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;
import java.util.stream.Collectors;

public class TaskBoard {
    private final JavaPlugin plugin;
    private final String name;
    private final DisplaySlot slot;
    private final Scoreboard scoreboard;
    private final Objective objective;
    private final Map<String, Integer> scores = new HashMap<>();
    private final String TASK = "Task";

    public TaskBoard(JavaPlugin plugin) {
        this.plugin = plugin;
        scoreboard = plugin.getServer().getScoreboardManager().getNewScoreboard();
        name = "TaskList";
        slot = DisplaySlot.SIDEBAR;
        objective = scoreboard.registerNewObjective(name, "dummy");
    }

    public void taskUpdate() {
        Game game = ((GameConfigurator) plugin).getGame();

        scores.clear();
        List<Task> remainTasks = game.getTasks().stream().filter(task -> !task.isFinished()).collect(Collectors.toList());
        for (Task remainTask : remainTasks) {
            scores.put(TASK + remainTask.getTaskNo(), 1);
        }

        update();
    }

    private void setAllPlayer() {
        Game game = ((GameConfigurator) plugin).getGame();
        game.getJoinedPlayers().forEach(player -> player.setScoreboard(scoreboard));
    }

    public void update() {
        scores.forEach((key, value) -> {
            scoreboard.resetScores(key);
            scoreboard.getObjective(name).getScore(key).setScore(value);
        });
    }

    public void disable() {
        objective.unregister();
    }

    public void register(){
        System.out.println(objective);
        System.out.println(slot);

        objective.setDisplaySlot(slot);
        setAllPlayer();
        taskUpdate();

        scores.forEach((k, v) -> System.out.println("k:"+k+", v:"+v));
    }

    public void unregister() {
        scoreboard.clearSlot(slot);
    }
}
