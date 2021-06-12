package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;
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

        game.getDisplayManager().setTaskBarVisible(true);
        game.getJoinedPlayers().forEach(player -> {
            player.teleport(game.getMeetingPos());
            game.getDisplayManager().resetAllInventory();
        });
        game.getTaskBoard().register();
        game.setCoolTime();
    }

    @Override
    public void onInactive() {
        super.onInactive();

        game.getJoinedPlayers().forEach(player -> game.getDisplayManager().clearWithoutArmor(player));
        game.getTaskBoard().unregister();
        game.clearCoolTime();
    }

    @Override
    public void onEnd() {
        super.onEnd();
    }
}
