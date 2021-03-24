package jp.hack.minecraft.werewolfgame.core;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class Cadaver {
    private Player player;
    private Block cadaverBlock;

    public Cadaver(Player player) {
        this.player = player;
        cadaverBlock = player.getLocation().getBlock();
        cadaverBlock.setType(Material.SKULL);

        Skull skull = (Skull) cadaverBlock.getState();
        skull.setSkullType(SkullType.PLAYER);
        skull.setOwningPlayer(player);
    }

    public void removeBlock() {
        cadaverBlock.setType(Material.AIR);
    }
}
