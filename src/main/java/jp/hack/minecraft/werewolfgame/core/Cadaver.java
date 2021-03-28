package jp.hack.minecraft.werewolfgame.core;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;
import org.bukkit.material.Wool;

import java.util.UUID;

public class Cadaver {
    private Player player;
    private Block cadaverBlock;
    private Block woolBlock;

    public Cadaver(Player player, WPlayer wPlayer) {
        this.player = player;
        cadaverBlock = player.getLocation().add(0,1,0).getBlock();
        woolBlock = player.getLocation().getBlock();

        Wool wool = new Wool();
        wool.setColor(DyeColor.valueOf(wPlayer.getColorName()));
        woolBlock.setType(Material.WOOL);
        woolBlock.setData(wool.getColor().getWoolData());

        cadaverBlock.setType(Material.SKULL);

        Skull skull = (Skull) cadaverBlock.getState();
        skull.setSkullType(SkullType.PLAYER);
        skull.setOwningPlayer(player);
        skull.update();
    }

    public Player getPlayer() {
        return player;
    }

    public Block getCadaverBlock() {
        return cadaverBlock;
    }

    public void removeBlock() {
        cadaverBlock.setType(Material.AIR);
        woolBlock.setType(Material.AIR);
    }
}
