package jp.hack.minecraft.werewolfgame.core.gamerule;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.WPlayer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
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

        Player damager = (Player) e.getDamager();
        Player attacker = (Player) e.getEntity();
        WPlayer wDamager = game.getWPlayer(damager.getUniqueId());
        WPlayer wAttacker = game.getWPlayer(attacker.getUniqueId());

        wAttacker.setKilling(true);
        wDamager.setKilling(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                wAttacker.setKilling(false);
                wDamager.setKilling(false);
            }
        }.runTaskLater(plugin, 10);

        Location damagerLoc = damager.getLocation();

        damagerLoc.add(0, 1, 0).getBlock().setType( Material.SKULL );
        attacker.teleport( damagerLoc.add(0, 0.5, 0) );

        damager.setGameMode(GameMode.SPECTATOR);
        wDamager.setDied(true);

        game.confirmGame();
    }
}
