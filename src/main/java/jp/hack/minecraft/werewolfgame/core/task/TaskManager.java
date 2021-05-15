package jp.hack.minecraft.werewolfgame.core.task;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.core.WPlayer;
import jp.hack.minecraft.werewolfgame.core.display.DisplayManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    private final Game game;
    private int sumOfTask;
    private int finishedTask = 0;

    public TaskManager(Game game) {
        this.game = game;
    }

    public int getSumOfTask() {
        return sumOfTask;
    }

    public void setSumOfTask(int sumOfTask) {
        if (sumOfTask <= 0) {
            this.sumOfTask = 1;
        } else {
            this.sumOfTask = sumOfTask;
        }
    }

    private void updateFinishedTask() {
        int count = 0;
        for (WPlayer wPlayer : game.getWPlayers().values()) {
            for (Task task : wPlayer.getTasks()) {
                if (task.isFinished()) {
                    count++;
                }
            }
        }
        finishedTask = count;
    }

    public void onTaskFinished(Player player, int no) {

        DisplayManager manager = game.getDisplayManager();
        WPlayer wPlayer = game.getWPlayer(player.getUniqueId());

        if (wPlayer.getRole().isImposter()) {
            manager.sendErrorMessage(player, "you.notClueMate");
            return;
        }
        List<Task> taskList = wPlayer.getTasks();

        if (no > taskList.size() - 1 || no < 0) {
            manager.sendErrorMessage(player, "command.undefinedTask");
            return;
        }
        taskList.get(no).finished();
        manager.sendMessage(player, "009", no);

        updateFinishedTask();
        manager.updateTaskBoard(player);
        if ( !wPlayer.getTasks().stream().filter(t -> !t.isFinished()) .isParallel() && wPlayer.isDied() ) {
            player.setGameMode(GameMode.SPECTATOR);
        }
        game.confirmGame();

    }

    public void taskBarUpdate() {

        DisplayManager manager = game.getDisplayManager();
        float progress = (float) finishedTask / (float) sumOfTask;
        if (sumOfTask == 0) {
            manager.setTask((float) 1.0);
            return;
        }
        if (progress < 0.0) {
            manager.setTask((float) 0.0);
            return;
        } else if (progress > 1.0) {
            manager.setTask((float) 1.0);
            return;
        }
        manager.setTask(progress);

    }

    public void setTasks(WPlayer wPlayer) {
        int numberOfTask = game.getNumberOfTasks();

        wPlayer.clearTasks();
        List<Task> taskList = new ArrayList<>();
        for (int i = 0; i < numberOfTask; i++) {
            taskList.add(new Task(i));
        }
        wPlayer.setTasks(taskList);
    }

    public int getFinishedTask() {
        return finishedTask;
    }
}
