package jp.hack.minecraft.werewolfgame.core.display;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.WPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class DisplayManager {
    JavaPlugin plugin;
    TaskBar taskBar;

    final String DEFEAT_MESSAGE = ChatColor.RED+"敗北";
    final String VICTORY_MESSAGE = ChatColor.GREEN+"勝利";

    public DisplayManager(JavaPlugin plugin) {
        this.plugin = plugin;
        taskBar = new TaskBar();
        taskBar.setVisible(false);
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

    public void playerVictory() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            Game game = ((GameConfigurator)plugin).getGame();
            WPlayer wPlayer = game.getWPlayer(player.getUniqueId());

            if (wPlayer.getRole().isWolf()) {
                sendTitle(player, DEFEAT_MESSAGE);
            } else {
                sendTitle(player, VICTORY_MESSAGE);
            }
        }
    }

    public void playerDefeat() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            Game game = ((GameConfigurator) plugin).getGame();
            WPlayer wPlayer = game.getWPlayer(player.getUniqueId());

            if (wPlayer.getRole().isWolf()) {
                sendTitle(player, VICTORY_MESSAGE);
            } else {
                sendTitle(player, DEFEAT_MESSAGE);
            }

        }
    }
}
