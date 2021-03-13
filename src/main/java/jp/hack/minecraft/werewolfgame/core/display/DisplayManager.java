package jp.hack.minecraft.werewolfgame.core.display;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.core.WPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DisplayManager {
    Game game;
    TaskBar taskBar;
    VoteBoard voteBoard;

    final String IMPOSTER_MESSAGE = ChatColor.RED + "インポスター";
    final String CLUE_MATE_MESSAGE = ChatColor.AQUA + "クルーメイト";

    final String VICTORY_MESSAGE = ChatColor.GREEN+"勝利";

    public DisplayManager(Game game) {
        this.game = game;
        taskBar = new TaskBar();
        taskBar.setVisible(false);
        voteBoard = new VoteBoard();
    }

    public void sendTitle(Player player, String title) {
        player.sendTitle(title, "", 5, 100, 5);
    }
    public void sendTitle(Player player, String title, String subTitle) {
        player.sendTitle(title, subTitle, 5, 100, 5);
    }

    public Boolean isTaskBarVisible() {
        return taskBar.isVisible();
    }

    public void setTaskBarVisible(Boolean visible) {
        taskBar.setVisible(visible);
    }

    public void setTask(float percent) {
        taskBar.setTask(percent);
    }

    public void addTaskBar(Player player) {
        taskBar.addPlayer(player);
    }

    public void youAreImposter(Player player) {
        sendTitle(player, IMPOSTER_MESSAGE);
    }

    public void youAreClueMate(Player player) {
        sendTitle(player, CLUE_MATE_MESSAGE);
    }

    public void playerVictory() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            WPlayer wPlayer = game.getWPlayer(player.getUniqueId());

            sendTitle(player, VICTORY_MESSAGE, CLUE_MATE_MESSAGE);
        }
    }

    public void playerDefeat() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            WPlayer wPlayer = game.getWPlayer(player.getUniqueId());

            sendTitle(player, VICTORY_MESSAGE, IMPOSTER_MESSAGE);
        }
    }
}
