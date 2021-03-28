package jp.hack.minecraft.werewolfgame.core;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

import java.util.UUID;

public class Cadaver {
    private Player player;
    private Block cadaverBlock;
    private Block woolBlock;

    public Cadaver(Player player, WPlayer wPlayer) {
        this.player = player;
        cadaverBlock = player.getLocation().add(0,1,0).getBlock();
        woolBlock = player.getLocation().getBlock();

        woolBlock.setType(Material.WOOL);
        woolBlock.setData(DyeColor.getByColor(wPlayer.getColor()).getWoolData());
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
