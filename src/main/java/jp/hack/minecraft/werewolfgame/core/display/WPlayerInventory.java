package jp.hack.minecraft.werewolfgame.core.display;

import com.sun.tools.javac.jvm.Items;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.*;

public class WPlayerInventory {
    private final Map<Integer, ItemStack> clueInv = new HashMap<>();
    private final Map<Integer, ItemStack> imposterInv = new HashMap<>();
    private ItemStack compass = new ItemStack(Material.COMPASS);
    private ItemStack sword = new ItemStack(Material.IRON_SWORD);
    private final String COMPASS_NAME = ChatColor.RED + "REPORT";
    private final String SWORD_NAME = ChatColor.RED + "KILL";

    public WPlayerInventory() {

        ItemMeta itemMeta = compass.getItemMeta();
        itemMeta.setDisplayName(COMPASS_NAME);
        compass.setItemMeta(itemMeta);
        clueInv.put(0, new ItemStack(compass));

        itemMeta = sword.getItemMeta();
        itemMeta.setDisplayName(SWORD_NAME);
        sword.setItemMeta(itemMeta);
        imposterInv.put(0, new ItemStack(compass));
        imposterInv.put(4, new ItemStack(sword));

    }

    public enum WPlayerInventoryType {
        CLUE_INV,
        IMPOSTER_INV
    }

    private PlayerInventory createInventory() {
        return (PlayerInventory) Bukkit.createInventory(null, InventoryType.PLAYER);
    }

    public Map<Integer, ItemStack> createClueInv() {
        return new HashMap<>(clueInv);
    }

    public Map<Integer, ItemStack> createImposterInv() {
        return new HashMap<>(imposterInv);
    }

    public ItemStack[] getColoredArmors(Color color) {
        ItemStack[] armors = new ItemStack[4];

        ItemStack armor = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta armorMeta = (LeatherArmorMeta) armor.getItemMeta();
        armorMeta.setColor(color);
        armor.setItemMeta(armorMeta);

        armors[0] = new ItemStack(armor);
        armor.setType(Material.LEATHER_LEGGINGS);
        armors[1] = new ItemStack(armor);
        armor.setType(Material.LEATHER_CHESTPLATE);
        armors[2] = new ItemStack(armor);
        armor.setType(Material.LEATHER_HELMET);
        armors[3] = new ItemStack(armor);
        return armors;
    }
}
