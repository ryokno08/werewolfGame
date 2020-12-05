package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.core.Game;

public class VotingState implements GameState {
    /*
    private static final VotingState singleton = new VotingState();
    private VotingState(){}
    public static VotingState getInstance() {
        return singleton;
    }

     */
    private Game currentGame;
    public VotingState(Game game){
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
