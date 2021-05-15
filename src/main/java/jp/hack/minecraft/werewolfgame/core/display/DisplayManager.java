package jp.hack.minecraft.werewolfgame.core.display;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.core.WPlayer;
import jp.hack.minecraft.werewolfgame.util.Messages;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;

public class DisplayManager {
    private Game game;
    private TaskBar taskBar;
    private WPlayerInventory wPlayerInventory;

    final String IMPOSTER_MESSAGE = ChatColor.RED + "インポスター";
    final String CLUE_MATE_MESSAGE = ChatColor.AQUA + "クルーメイト";

    final String VICTORY_MESSAGE = ChatColor.GREEN+"勝利";
    final String DEFEAT_MESSAGE = ChatColor.RED+"敗北";

    public DisplayManager(Game game) {
        this.game = game;
        taskBar = new TaskBar();
        taskBar.setVisible(false);
        wPlayerInventory = new WPlayerInventory(game.getItemForReport(), game.getItemForKill());
    }

    public void log(String log) {
        game.getPlugin().getLogger().info(log);
    }
    public void allBlindness(int duration) {
        game.getJoinedPlayers().forEach(p->{
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 100));
        });
    }
    public void invisible(Player p) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10000 * 20, 1, false, false));
    }
    public void clearEffect(Player p) {
        p.removePotionEffect(PotionEffectType.BLINDNESS);
        p.removePotionEffect(PotionEffectType.INVISIBILITY);
    }
    public void allMakeSound(Sound sound, SoundCategory soundCategory, float volume, float pitch) {
        game.getJoinedPlayers().forEach(player -> player.playSound(player.getLocation(), sound, soundCategory, volume, pitch));
    }
    public void allSendMessage(String code, Object args) {
        game.getJoinedPlayers().forEach(p->p.sendMessage(Messages.message(code, args)));
    }
    public void allSendMessage(String code) {
        game.getJoinedPlayers().forEach(p->p.sendMessage(Messages.message(code)));
    }
    public void sendMessage(Player player, String code, Integer args) {
        player.sendMessage(Messages.message(code, args));
    }
    public void sendMessage(Player player, String code) {
        player.sendMessage(Messages.message(code));
    }
    public void sendErrorMessage(Player player, String code, Object... args) {
        player.sendMessage(Messages.error(code, args));
    }
    public void allSendActionBarMessage(String text) {
        TextComponent component = new TextComponent(text);
        game.getJoinedPlayers().forEach(p -> p.spigot().sendMessage(ChatMessageType.ACTION_BAR, component));
    }
    public void allSendTitle(String title) {
        game.getJoinedPlayers().forEach(player -> {
            player.sendTitle(title, "", 10, 2*20, 10);
        });
    }
    public void allSendTitle(String title, String subTitle) {
        game.getJoinedPlayers().forEach(player -> {
            player.sendTitle(title, subTitle, 10, 2*20, 10);
        });
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

    public void showDeath(Player player, String message) {
        sendTitle(player, ChatColor.RED+"You Died!", message);
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

    public void changeWPlayerColor(Player player, String colorName, Color color) {
        WPlayer wPlayer = game.getWPlayer(player.getUniqueId());
        wPlayer.setColor(colorName, color);

        resetColorArmor(player);
    }

    public void resetColorArmor(Player player) {
        WPlayer wPlayer = game.getWPlayer(player.getUniqueId());

        ItemStack[] armor = wPlayerInventory.getColoredArmors(wPlayer.getColor());
        player.getInventory().setArmorContents(armor);
    }

    public void takeOffArmor(Player player) {
        player.getInventory().setArmorContents(null);
    }
    public void clearInventory(Player player) {
        player.getInventory().clear();
    }
    public void clear(Player player) {
        clearInventory(player);
        clearEffect(player);
    }

    public void updateTaskBoard(Player player) {
        game.getTaskBoard().update(player);
    }
}
