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
    final String CLUEMATE_MESSAGE = ChatColor.AQUA + "クルーメイト";

    final String DEFEAT_MESSAGE = ChatColor.RED+"敗北";
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
        sendTitle(player, CLUEMATE_MESSAGE);
    }

    public void playerVictory() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            WPlayer wPlayer = game.getWPlayer(player.getUniqueId());

            if (wPlayer.getRole().isImposter()) {
                sendTitle(player, DEFEAT_MESSAGE);
            } else {
                sendTitle(player, VICTORY_MESSAGE);
            }
        }
    }

    public void playerDefeat() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            WPlayer wPlayer = game.getWPlayer(player.getUniqueId());

            if (wPlayer.getRole().isImposter()) {
                sendTitle(player, VICTORY_MESSAGE);
            } else {
                sendTitle(player, DEFEAT_MESSAGE);
            }

        }
    }
}
