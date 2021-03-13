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
    private final JavaPlugin plugin;
    private BukkitTask task;
    private int counter = 0;


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

        counter = 0;
        if (task == null) {
            task = new MyRunTask().runTask(plugin);
        }
    }

    @Override
    public void onInactive() {
        super.onInactive();
        Game game = ((GameConfigurator) plugin).getGame();

        for (Player player : plugin.getServer().getOnlinePlayers()) {
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

    @Override
    public void onEnd() {
        super.onEnd();
    }


    class MyRunTask extends BukkitRunnable{

        @Override
        public void run() {
            System.out.println(counter);
            this.cancel();
            if (counter >= 5) {
                Game game = ((GameConfigurator) plugin).getGame();
                game.returnToPlay();
                task = null;
                return;
            }
            for (Player p : plugin.getServer().getOnlinePlayers())
                p.sendTitle(Messages.message("003", 5 - counter), "", 0, 20, 0);
            counter++;
            task = new MyRunTask().runTaskLater(plugin, 20);
        }
    }
}
