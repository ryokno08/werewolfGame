package jp.hack.minecraft.werewolfgame.core;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class Scapegoat {
    private final UUID playerUuid;
    private final ArmorStand armorStand;

    public Scapegoat(Player player) {
        playerUuid = player.getUniqueId();

        World world = player.getWorld();
        this.armorStand = (ArmorStand) world.spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
        armorStand.setBasePlate(false);
        armorStand.setVisible(false);

        ItemStack skullStack = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta skullMeta = (SkullMeta) skullStack.getItemMeta();
        skullMeta.setOwningPlayer(player);
        skullStack.setItemMeta(skullMeta);
        armorStand.setHelmet(skullStack);

        // armorStand.setHelmet(player.getInventory().getHelmet());
        armorStand.setChestplate(player.getInventory().getChestplate());
        armorStand.setLeggings(player.getInventory().getLeggings());
        armorStand.setBoots(player.getInventory().getBoots());
    }

    public void destroy() {
        if (armorStand != null) armorStand.remove();
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }
}
