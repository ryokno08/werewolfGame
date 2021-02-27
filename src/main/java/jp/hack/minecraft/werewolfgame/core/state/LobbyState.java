package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.WPlayer;
import jp.hack.minecraft.werewolfgame.util.Messages;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
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
            task = new MyRunTask().runTask(plugin);
        }
    }

    @Override
    public void onInactive() {
        super.onInactive();
        Game game = ((GameConfigurator) plugin).getGame();

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            WPlayer wPlayer = game.getWPlayer(player.getUniqueId());
            if (wPlayer.getRole().isWolf()) {
                game.getDisplayManager().youAreImposter(player);
            } else {
                game.getDisplayManager().youAreClueMate(player);
            }
        }
    }

    @Override
    public void onEnd() {
        super.onEnd();
    }


    class MyRunTask extends BukkitRunnable{
        int counter = 0;

        @Override
        public void run() {
            System.out.println(counter);
            if (counter >= 5) {
                Game game = ((GameConfigurator) plugin).getGame();
                // game.nextState();
                game.gameStart();
                task = null;
                return;
            }
            for (Player p : plugin.getServer().getOnlinePlayers())
                p.sendTitle(Messages.message("003", 5 - counter), "", 0, 20, 0);
            counter++;
            task = this.runTaskLater(plugin, 20);
        }
    }
}
