package jp.hack.minecraft.werewolfgame.core.task;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.core.WPlayer;
import jp.hack.minecraft.werewolfgame.core.display.DisplayManager;
import org.bukkit.ChatColor;
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
        this.sumOfTask = sumOfTask;
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

        WPlayer wPlayer = game.getWPlayer(player.getUniqueId());
        if (wPlayer.getRole().isImposter()) {
            player.sendMessage("インポスターはタスクができません");
        }
        List<Task> taskList = wPlayer.getTasks();

        if (no > taskList.size()-1 || no < 0) {
            player.sendMessage("存在しないタスクです");
            return;
        }
        taskList.get(no).finished();
        player.sendMessage(ChatColor.GREEN.toString() + no + "番目のタスクが終わりました");

        updateFinishedTask();
        game.confirmGame();

    }

    public void taskBarUpdate() {

        DisplayManager manager = game.getDisplayManager();
        float progress = (float) finishedTask / (float) sumOfTask;
        if (progress < 0.0) {
            manager.setTask( (float) 0.0);
            return;
        } else if (progress > 1.0) {
            manager.setTask( (float) 1.0);
            return;
        }
        manager.setTask( progress );

    }

    public void setTasks(WPlayer wPlayer) {
        wPlayer.clearTasks();
        List<Task> taskList = new ArrayList<>();
        for (int i = 0; i< sumOfTask; i++) {
            taskList.add(new Task(i));
        }
        wPlayer.setTasks(taskList);
    }

    public int getFinishedTask() {
        return finishedTask;
    }
}
