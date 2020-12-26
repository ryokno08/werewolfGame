package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;

public abstract class GameState {
    public enum State{
        START,
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

    public abstract void update();

    public void onStart(Game game){
        state = State.START;
    } // Stateがnewされた時

    public void onActive(){
        state = State.ACTIVE;
    } // このStateに切り替わった時

    public void onInactive(){
        state = State.INACTIVE;
    } // 違うStateに切り替わった時

    public void onEnd(){
        state = State.END;
    } // Stateが完全に終了した時

    public boolean isActive(){
        return state == State.ACTIVE;
    }
}
