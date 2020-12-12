package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

public class LobbyState implements GameState {
    /*
    private static final LobbyState singleton = new LobbyState();
    private LobbyState(){}
    public static LobbyState getInstance() {
        return singleton;
    }

     */
    private Game currentGame;
    public LobbyState(Game game){
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

    @Override
    public void update() {

    }

    public void gameStart() {
        // 5秒ほどタイマー処理してそのあと下行を実行
        currentGame.currentState = currentGame.playingState;
    }
}
