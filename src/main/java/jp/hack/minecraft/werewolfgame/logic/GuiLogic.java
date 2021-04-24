package jp.hack.minecraft.werewolfgame.logic;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import me.mattstudios.mfgui.gui.components.ItemBuilder;
import me.mattstudios.mfgui.gui.guis.Gui;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GuiLogic {
    private JavaPlugin plugin;
    private Game game;
    private ItemStack item;
    private ItemStack skipItem;
    private Gui gui;
    private List<GuiItem> heads;

    public GuiLogic(JavaPlugin plugin, int rows, String title) {
        this.plugin = plugin;
        game = ((GameConfigurator) plugin).getGame();

        // init item
        item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("投票");
        item.setItemMeta(meta);

        skipItem = new ItemStack(Material.BARRIER);
        ItemMeta skipMeta = skipItem.getItemMeta();
        skipMeta.setDisplayName("Skip");
        skipItem.setItemMeta(skipMeta);

        heads = new ArrayList<>();

        gui = new Gui(rows, title);
    }

    public ItemStack getItem() {
        return item;
    }

    public void OpenGUI(Player player, ItemStack clickedItem) {
        if (game == null) return;
        if (!game.wasStarted()) return;
        if (game.getWPlayer(player.getUniqueId()).isDied()) return;

        if (!clickedItem.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
            return;
        }

        initGui();
        gui.open(player);
    }

    private void initGui() {
        gui.getGuiItems().values().forEach(guiItem -> guiItem.setItemStack(new ItemStack(Material.AIR)));

        heads.forEach(i -> gui.addItem(i));

        ItemBuilder skipBuilder = ItemBuilder.from(skipItem);
        gui.setItem(26, skipBuilder.asGuiItem(e -> {
            e.setCancelled(true);
            game.voteToSkip(e.getWhoClicked().getUniqueId());
            game.getScoreboardVoted().setScore(e.getWhoClicked().getUniqueId(), 1);
        }));
        reloadHeads();
    }

    private void reloadHeads() {

        game.getWPlayers().values().stream()
                .filter(wPlayer -> !wPlayer.isDied())
                .map(wPlayer -> plugin.getServer().getPlayer(wPlayer.getUuid()))
                .forEach(headPlayer -> {
                    UUID uuid = headPlayer.getUniqueId();

                    ItemStack skullStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                    SkullMeta skull = (SkullMeta) skullStack.getItemMeta();
                    skull.setDisplayName(headPlayer.getName());
                    skull.setOwningPlayer(headPlayer);
                    skullStack.setItemMeta(skull);

                    ItemBuilder skullBuilder = ItemBuilder.from(skullStack);
                    heads.add(
                            skullBuilder.asGuiItem(e -> {
                                e.setCancelled(true);
                                game.voteToPlayer(e.getWhoClicked().getUniqueId(), uuid);
                                game.getScoreboardVoted().setScore(e.getWhoClicked().getUniqueId(), 1);
                            })
                    );
                });
    }
}