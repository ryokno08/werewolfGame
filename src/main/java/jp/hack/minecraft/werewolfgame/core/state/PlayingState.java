package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.core.Game;
import jp.hack.minecraft.werewolfgame.core.display.TaskManager;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayingState extends BukkitRunnable implements GameState {
<<<<<<< HEAD
    /*
    private static final PlayingState singleton = new PlayingState();
    private PlayingState(){}
    public static PlayingState getInstance() {
        return singleton;
    }

     */
=======
>>>>>>> cc10a1e70fdcf0d7262d0a4826e50d998bb07b11
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

    @Override
    public void run() {
<<<<<<< HEAD
        
        // if();
=======
>>>>>>> cc10a1e70fdcf0d7262d0a4826e50d998bb07b11
    }
}
