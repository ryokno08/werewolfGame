package jp.hack.minecraft.werewolfgame.core;

import jp.hack.minecraft.werewolfgame.Game;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;

public class Cadaver {
    private final Player player;
    private final Location location;
    private final ArmorStand armorStand;
    private BukkitTask task;

    public Cadaver(Player player) {
        this.player = player;
        location = player.getLocation();

        armorStand = (ArmorStand) player.getWorld().spawnEntity(player.getLocation().add(0.0d, -0.5d, 0.0d), EntityType.ARMOR_STAND);
        armorStand.setBasePlate(false);
        armorStand.setGravity(false);
        armorStand.setVisible(false);
        armorStand.setLeftLegPose(new EulerAngle(1.55d, 9d, 0d));
        armorStand.setRightLegPose(new EulerAngle(1.55d, -9d, 0d));

        armorStand.setChestplate(player.getInventory().getChestplate());
        armorStand.setLeggings(player.getInventory().getLeggings());
        armorStand.setBoots(player.getInventory().getBoots());
    }

    public void spawnBlood(Game game) {
        task = new BloodParticle(game).runTask(game.getPlugin());
    }

    public void clearBlood() {
        if (task != null) task.cancel();
    }

    public Player getPlayer() {
        return player;
    }

    public Location getLocation() {
        return location;
    }

    public ArmorStand getArmorStand() {
        return armorStand;
    }

    public void destroy() {
        armorStand.remove();
        clearBlood();
    }

    public class BloodParticle extends BukkitRunnable {
        private Game game;
        public BloodParticle(Game game) {
            this.game = game;
        }

        @Override
        public void run() {
            player.getWorld().spawnParticle(Particle.DRIP_LAVA, getLocation().getX(), getLocation().getY() + 1.0, getLocation().getZ(), 20, 0.10d, 0.10d, 0.10d);
            task = new BloodParticle(game).runTaskLater(game.getPlugin(), 60);
        }
    }
}
