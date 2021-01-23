package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.Main;
import jp.hack.minecraft.werewolfgame.util.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class LobbyState extends GameState {
    private final JavaPlugin plugin;
    private BukkitTask task;

    public LobbyState(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean canSpeak() {
        return false;
    }

    @Override
    public boolean canMove() {
        return true;
    }

    @Override
    public void update() {

    }

    @Override
    public void onStart(Game game) {
        super.onStart(game);
    }

    @Override
    public void onActive() {
        super.onActive();
        plugin.getLogger().info("LobbyStateに切り替わりました");
        plugin.getServer().getOnlinePlayers().forEach(player -> player.sendMessage("Lobby"));
        if (task == null) {
            task = new BukkitRunnable() {
                int counter = 0;

                @Override
                public void run() {
                    counter++;
                    if (counter < 5) {
                        for (Player p : plugin.getServer().getOnlinePlayers())
                            p.sendTitle(Messages.message("003", String.valueOf(5 - counter)), "", 0, 20, 0);
                    } else {
                        Game game = ((GameConfigurator) plugin).getGame();
                        game.nextState();
                        task.cancel();
                    }
                }
            }.runTaskLater(plugin, 20);
        }
    }

    @Override
    public void onInactive() {
        super.onInactive();
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    @Override
    public void onEnd() {
        super.onEnd();
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

}
