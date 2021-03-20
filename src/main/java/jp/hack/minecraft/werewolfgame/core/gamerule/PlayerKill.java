package jp.hack.minecraft.werewolfgame.core.gamerule;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.WPlayer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerKill {
    JavaPlugin plugin;

    public PlayerKill(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void OnPlayerAttack(EntityDamageByEntityEvent e) {
        Game game = ((GameConfigurator)plugin).getGame();

        Player entity = (Player) e.getEntity();
        Player attacker = (Player) e.getDamager();
        WPlayer wEntity = game.getWPlayer(entity.getUniqueId());
        WPlayer wAttacker = game.getWPlayer(attacker.getUniqueId());

        wAttacker.setKilling(true);
        wEntity.setKilling(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                wAttacker.setKilling(false);
                wEntity.setKilling(false);
            }
        }.runTaskLater(plugin, 10);

        Location entityLocation = entity.getLocation();

        entityLocation.getBlock().setType( Material.SKULL );
        attacker.teleport( entityLocation.add(0, 0.5, 0) );

        entity.setGameMode(GameMode.SPECTATOR);
        attacker.spawnParticle(Particle.REDSTONE, attacker.getLocation(), 30, 2.0, 3.0, 2.0);
        game.getDisplayManager().showDeath(entity, "By " + attacker.getDisplayName());
        wEntity.setDied(true);

        game.confirmGame();
    }
}
