package jp.hack.minecraft.werewolfgame.logic;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.Imposter;
import jp.hack.minecraft.werewolfgame.core.WPlayer;
import jp.hack.minecraft.werewolfgame.core.state.PlayingState;
import jp.hack.minecraft.werewolfgame.core.state.VotingState;
import jp.hack.minecraft.werewolfgame.util.Messages;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

public class GameDirector {
    private final JavaPlugin plugin;

    public GameDirector(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void onPlayerJoin(PlayerJoinEvent e) {
        Game game = ((GameConfigurator) plugin).getGame();

        Player player = e.getPlayer();
        if (game.addWPlayer(player).equals(Game.ErrorJudge.WPLAYERS_FULL)) {
            player.sendMessage(Messages.message("game.fullPlayers"));
        }

        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.scheduleSyncDelayedTask(plugin, () -> {
            if (game.getLobbyPos() != null) player.teleport(game.getLobbyPos());
            player.setGameMode(GameMode.ADVENTURE);
        }, 1);
    }

    public void onPlayerAttack(EntityDamageByEntityEvent e) {
        Game game = ((GameConfigurator) plugin).getGame();

        Player attacker = (Player) e.getDamager();
        Player entity = (Player) e.getEntity();

        WPlayer wAttacker = game.getWPlayer(attacker.getUniqueId());
        WPlayer target = game.getWPlayer(entity.getUniqueId());

        if (wAttacker.wasDied()) return;
        if (target.wasDied()) {
            game.getDisplayManager().sendErrorMessage(entity, "you.disturbImposter");
            return;
        }
        if (target.isImposter()) return;
        if (!wAttacker.isImposter()) return;
        if (!attacker.getInventory().getItemInMainHand().getType().equals(game.getItemForKill().getType())) return;

        Imposter imposter = (Imposter) wAttacker;
        if (!imposter.canKill()) {
            game.getDisplayManager().sendErrorMessage(attacker, "you.coolingTime");
            return;
        }

        Location entityLocation = entity.getLocation();
        attacker.teleport(entityLocation);
        attacker.spawnParticle(Particle.REDSTONE, attacker.getLocation(), 30, 2.0, 3.0, 2.0);
        game.getDisplayManager().showDeath(entity, "By " + attacker.getDisplayName());

        game.killPlayer(entity, false);
        imposter.setCoolDown(game);

        imposter.setIsKilling(true);
        target.setIsKilling(true);
        game.confirmGame();
        System.out.println("[!KILL]" + attacker.getDisplayName() + " killed " + entity.getDisplayName());

        new BukkitRunnable() {
            @Override
            public void run() {
                imposter.setIsKilling(false);
                target.setIsKilling(false);
            }
        }.runTaskLater(plugin, 10);
    }

    public void onPlayerItemAction(PlayerInteractEvent e) {
        Game game = ((GameConfigurator) plugin).getGame();

        Player player = e.getPlayer();
        ItemStack itemInHand = player.getInventory().getItemInMainHand();

        if (game.getCurrentState() instanceof VotingState) {
            if (itemInHand.getType() != game.getGuiLogic().getItem().getType()) return;
            game.getGuiLogic().OpenGUI(player, itemInHand);
        }

        if (!(game.getCurrentState() instanceof PlayingState)) return;
        if (itemInHand.getType() != game.getItemForReport().getType()) return;
        if (game.getCadavers() == null) return;
        if (game.getCadavers().isEmpty()) return;

        Location playerLoc = player.getLocation();
        game.getCadavers().values().stream().filter(o -> {
            Location cadaverLoc = o.getCadaverBlock().getLocation();
            return cadaverLoc.distance(playerLoc) <= game.getReportDistance();
        }).findFirst().ifPresent(o -> game.report(player, o.getPlayer()));
    }

    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        Game game = ((GameConfigurator) this.plugin).getGame();
    }
}
