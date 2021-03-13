package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.WPlayer;
import jp.hack.minecraft.werewolfgame.util.Messages;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class LobbyState extends GameState {
    private BukkitTask task;
    private int counter = 0;

    public LobbyState(JavaPlugin plugin, Game game) {
        super(plugin, game);
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
    public void onActive() {
        super.onActive();
        plugin.getLogger().info("LobbyStateに切り替わりました");

        counter = 0;
        if (task == null) {
            System.out.println("LobbyState Count");
            task = new MyRunTask().runTask(plugin);
        }
    }

    @Override
    public void onInactive() {
        super.onInactive();

        for (Player player : game.getJoinedPlayers()) {
            player.setGameMode(GameMode.ADVENTURE);

            WPlayer wPlayer = game.getWPlayer(player.getUniqueId());
            if (wPlayer.getRole().isImposter()) {
                game.getDisplayManager().youAreImposter(player);
                player.getInventory().setItem(8, new ItemStack(Material.IRON_SWORD));
            } else {
                game.getDisplayManager().youAreClueMate(player);
            }
        }
    }

    class MyRunTask extends BukkitRunnable{

        final int limit = 5;

        @Override
        public void run() {
            System.out.println(counter);
            if (counter >= limit) {
                game.changeState(game.getPlayingState());
                task = null;
                return;
            }
            for (Player p : game.getJoinedPlayers())
                p.sendTitle(Messages.message("003", limit - counter), "", 0, 20, 0);
            counter++;
            task = new MyRunTask().runTaskLater(plugin, 20);
        }
    }
}
