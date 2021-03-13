package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class GameState {
    final JavaPlugin plugin;
    final Game game;

    GameState(JavaPlugin plugin, Game game) {
        this.plugin = plugin;
        this.game = game;
    }

    public enum State {
        ACTIVE,
        INACTIVE,
        END,
        NONE
    }

    public State state = State.NONE;

    // チャットできるならtrue, できないならfalse
    public boolean canSpeak() {
        return false;
    }

    // 動けるならtrue, 動けないならfalse
    public boolean canMove() {
        return false;
    }

    public void onActive() {
        state = State.ACTIVE;
    } // このStateに切り替わった時

    public void onInactive() {
        state = State.INACTIVE;
    } // 違うStateに切り替わった時

    public void onEnd() {
        state = State.END;
    } // Stateが完全に終了した時

    public boolean isActive() {
        return state == State.ACTIVE;
    }

    public State getState() {
        return state;
    }
}
