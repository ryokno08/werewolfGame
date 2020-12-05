package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.core.Game;

public class MeetingState implements GameState {
    /*
    private static final MeetingState singleton = new MeetingState();
    private MeetingState(){}
    public static MeetingState getInstance() {
        return singleton;
    }

     */
    private Game currentGame;
    public MeetingState(Game game){
        currentGame = game;
    }
    @Override
    public boolean canSpeak() {
        return true;
    }

    @Override
    public boolean canMove() {
        return false;
    }

    @Override
    public void update() {

    }
}
