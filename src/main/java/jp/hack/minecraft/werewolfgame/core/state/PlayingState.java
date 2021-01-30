package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.Task;
import jp.hack.minecraft.werewolfgame.core.TaskManager;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class PlayingState extends GameState {
    private final JavaPlugin plugin;

    private BukkitTask bukkitTask;

    public PlayingState(JavaPlugin plugin) {
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

    public void update() {
    }

    @Override
    public void onStart(Game game) {
        super.onStart(game);
    }

    @Override
    public void onActive() {
        super.onActive();
        plugin.getLogger().info("PlayingStateに切り替わりました");
        plugin.getLogger().info(plugin.getServer().getOnlinePlayers().toString());
        plugin.getServer().getOnlinePlayers().forEach(player -> player.sendTitle(ChatColor.RED + "ゲーム開始！", "", 10, 20, 10));


        bukkitTask = new BukkitRunnable() {
            @Override
            public void run() {
                TaskManager taskManager = ((GameConfigurator) plugin).getGame().getTaskManager();
                int count = 0;
                for (Task task : taskManager.getTaskList()) {
                    if (task.isFinished()) {
                        count++;
                    }
                }
                taskManager.taskUpdate(count);
            }
        }.runTaskTimer(plugin, 0, 20);

    }

    @Override
    public void onInactive() {
        super.onInactive();
        if (bukkitTask != null) bukkitTask.cancel();
    }

    @Override
    public void onEnd() {
        super.onEnd();
        if (bukkitTask != null) bukkitTask.cancel();
    }
}
