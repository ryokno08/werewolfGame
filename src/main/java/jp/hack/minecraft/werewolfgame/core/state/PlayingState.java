package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.core.Game;
import jp.hack.minecraft.werewolfgame.core.display.TaskManager;

public class PlayingState implements GameState {
    /*
    private static final PlayingState singleton = new PlayingState();
    private PlayingState(){}
    public static PlayingState getInstance() {
        return singleton;
    }

     */
    private Game currentGame;
    public PlayingState(Game game){
        currentGame = game;
    }
    @Override
    public boolean canSpeak() {
        return false;
    }

    @Override
    public boolean canMove() {
        return true;
    }

    public void update(){
        TaskManager manager = Game.getInstance().getTaskManager();
        manager.taskUpdate();

        // if();
    }
}
