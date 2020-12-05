package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.core.Game;
import jp.hack.minecraft.werewolfgame.core.display.TaskManager;
import org.bukkit.scheduler.BukkitRunnable;

<<<<<<< HEAD
public class PlayingState extends BukkitRunnable implements GameState {
=======
public class PlayingState implements GameState {
    /*
>>>>>>> 748a6f57e6fcf565562dcc269c785ed6e553b216
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
    }

<<<<<<< HEAD
    @Override
    public void run() {
        
=======
        // if();
>>>>>>> 748a6f57e6fcf565562dcc269c785ed6e553b216
    }
}
