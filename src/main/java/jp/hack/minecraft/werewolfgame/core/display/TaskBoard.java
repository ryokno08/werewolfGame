package jp.hack.minecraft.werewolfgame.core.display;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.WPlayer;
import jp.hack.minecraft.werewolfgame.core.task.Task;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.*;

public class TaskBoard extends Scoreboard {
    private final String TASK = "Task";

    public TaskBoard(JavaPlugin plugin) {
        super(plugin, "TaskList", DisplaySlot.SIDEBAR);
    }

    public void resetAll() {
        Game game = ((GameConfigurator) plugin).getGame();
        for (int i = 0; i < game.getNumberOfTasks(); i++) {
            super.getScores().put(TASK + i, 1);
        }
        for (Player player : game.getJoinedPlayers()) {
            super.setPlayer(player);
        }
    }

    public void update(Player player) {
        Game game = ((GameConfigurator)plugin).getGame();
        WPlayer wPlayer = game.getWPlayer(player.getUniqueId());
        List<Task> tasks = wPlayer.getTasks();
        org.bukkit.scoreboard.Scoreboard playerTaskBoard = super.getEachBoards().get(player.getUniqueId());

        for (int i=0; i<tasks.size(); i++) {
            if (! playerTaskBoard.getScores(TASK + i).isEmpty()) {
                if (tasks.get(i).isFinished()) {
                    playerTaskBoard.resetScores(TASK + i);
                }
            }
        }
    }

    public void removeAt(Player player, int index) {
        super.removeScore(player, TASK + index);
    }
}
