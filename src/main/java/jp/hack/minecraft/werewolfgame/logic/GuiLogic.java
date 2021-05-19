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

import java.util.*;

public class GuiLogic {
    private JavaPlugin plugin;
    private Game game;
    private ItemStack item;
    private ItemStack skipItem;
    private int guiRows = 3;
    private String guiTitle = "";

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

        // heads = new ArrayList<>();

        guiRows = rows;
        guiTitle = title;

        // gui = new Gui(rows, title);
        // guis = new HashMap<>();
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

        initGui().open(player);
    }

    private Gui initGui() {
        Gui gui = new Gui(guiRows, guiTitle);
        gui.getGuiItems().values().forEach(guiItem -> guiItem.setItemStack(new ItemStack(Material.AIR)));

        // heads = new ArrayList<>();
        ItemBuilder skipBuilder = ItemBuilder.from(skipItem);
        gui.setItem(26, skipBuilder.asGuiItem(e -> {
            game.getDisplayManager().log("clicked: skip");
            e.setCancelled(true);
            game.voteToSkip((Player) e.getWhoClicked());
        }));
        List<GuiItem> heads = loadHeads();
        for (int i = 0; i < heads.size(); i++) {
            gui.setItem(i, heads.get(i));
        }
        // heads.forEach(i -> gui.setItem(i));\
        return gui;
    }

    private List<GuiItem> loadHeads() {
        List<GuiItem> heads = new ArrayList<>();
        game.getWPlayers().values().stream()
                .filter(wPlayer -> !wPlayer.isDied())
                .map(wPlayer -> game.getPlayer(wPlayer.getUuid()))
                .forEach(headPlayer -> {

                    ItemStack skullStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                    SkullMeta skull = (SkullMeta) skullStack.getItemMeta();
                    skull.setDisplayName(headPlayer.getName());
                    skull.setOwningPlayer(headPlayer);
                    skullStack.setItemMeta(skull);

                    ItemBuilder skullBuilder = ItemBuilder.from(skullStack);
                    heads.add(
                            skullBuilder.asGuiItem(e -> {
                                game.getDisplayManager().log("clicked: "+e.getWhoClicked().getName());
                                e.setCancelled(true);
                                game.voteToPlayer((Player) e.getWhoClicked(), headPlayer);
                                e.getWhoClicked().closeInventory();
                            })
                    );
                });
        return heads;
    }
}