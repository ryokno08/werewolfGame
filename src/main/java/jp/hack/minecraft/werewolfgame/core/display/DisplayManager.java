package jp.hack.minecraft.werewolfgame.core.display;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.core.WPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class DisplayManager {
    private Game game;
    private TaskBar taskBar;
    private VoteBoard voteBoard;
    private WPlayerInventory wPlayerInventory;

    final String IMPOSTER_MESSAGE = ChatColor.RED + "インポスター";
    final String CLUE_MATE_MESSAGE = ChatColor.AQUA + "クルーメイト";

    final String VICTORY_MESSAGE = ChatColor.GREEN+"勝利";
    final String DEFEAT_MESSAGE = ChatColor.RED+"敗北";

    public DisplayManager(Game game) {
        this.game = game;
        taskBar = new TaskBar();
        taskBar.setVisible(false);
        voteBoard = new VoteBoard();
        wPlayerInventory = new WPlayerInventory();
    }

    public void sendTitle(Player player, String title) {
        player.sendTitle(title, "", 10, 2*20, 10);
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

    public void showIssue(Boolean clueWin) {
        for (Player player : game.getJoinedPlayers()) {
            WPlayer wPlayer = game.getWPlayer(player.getUniqueId());
            if (clueWin) {
                if (wPlayer.getRole().isImposter()) {
                    sendTitle(player, DEFEAT_MESSAGE, CLUE_MATE_MESSAGE+"の勝利");
                } else {
                    sendTitle(player, VICTORY_MESSAGE, CLUE_MATE_MESSAGE+"の勝利");
                }
            } else {
                if (wPlayer.getRole().isImposter()) {
                    sendTitle(player, VICTORY_MESSAGE, IMPOSTER_MESSAGE+"の勝利");
                } else {
                    sendTitle(player, DEFEAT_MESSAGE, IMPOSTER_MESSAGE+"の勝利");
                }
            }
        }
    }

    public void resetInventory(Player player, WPlayerInventory.WPlayerInventoryType inventoryType) {
        if (player == null || inventoryType == null) return;

        player.getInventory().clear();
        Map<Integer, ItemStack> itemStackMap = new HashMap<>();
        switch (inventoryType) {
            case CLUE_INV:
                itemStackMap = wPlayerInventory.createClueInv();
                break;
            case IMPOSTER_INV:
                itemStackMap = wPlayerInventory.createImposterInv();
                break;
        }

        for (Map.Entry<Integer, ItemStack> entry : itemStackMap.entrySet()) {
            player.getInventory().setItem(entry.getKey(), entry.getValue());
        }

        resetColorArmor(player);
    }

    public void changeWPlayerColor(Player player, Color color) {
        WPlayer wPlayer = game.getWPlayer(player.getUniqueId());
        wPlayer.setColor(color);

        resetColorArmor(player);
    }

    public void resetColorArmor(Player player) {
        WPlayer wPlayer = game.getWPlayer(player.getUniqueId());

        ItemStack[] armor = wPlayerInventory.getColoredArmors(wPlayer.getColor());
        player.getInventory().setArmorContents(armor);
    }
}
