package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.core.display.DisplayManager;
import jp.hack.minecraft.werewolfgame.core.task.TaskManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayingState extends GameState {

    public PlayingState(JavaPlugin plugin, Game game) {
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
        plugin.getLogger().info("PlayingStateに切り替わりました");

        DisplayManager displayManager = game.getDisplayManager();
        displayManager.setTaskBarVisible(true);

        System.out.println(game.getJoinedPlayers());
        game.getJoinedPlayers().forEach(player -> player.teleport(game.getMeetingPos()));
    }

    @Override
    public void onInactive() {
        super.onInactive();

        TaskManager taskManager = game.getTaskManager();
        taskManager.taskBarUpdate();
    }

    @Override
    public void onEnd() {
        super.onEnd();
    }
}
