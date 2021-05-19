package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;
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
        game.getDisplayManager().log("PlayingStateに切り替わりました");

        game.getJoinedPlayers().forEach(player -> {
            player.teleport(game.getMeetingPos());
            game.getDisplayManager().resetAllInventory();
        });
        game.getTaskBoard().register();
    }

    @Override
    public void onInactive() {
        super.onInactive();

        TaskManager taskManager = game.getTaskManager();
        taskManager.taskBarUpdate();

        game.getJoinedPlayers().forEach(player -> {
            player.getInventory().clear();
            game.getDisplayManager().resetColorArmor(player);
        });

        game.getTaskBoard().unregister();
    }

    @Override
    public void onEnd() {
        super.onEnd();
    }
}
