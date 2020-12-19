package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.Main;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class LobbyState implements GameState {
    private JavaPlugin plugin;

    public LobbyState(JavaPlugin plugin){
        this.plugin = plugin;
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

    @Override
    public void init(Game game) {
        Bukkit.broadcastMessage("LobbyStateに切り替わりました");
    }

    @Override
    public void active() {

    }

    @Override
    public void inactive() {

    }

    @Override
    public void end() {

    }

    public void gameStart() {
        // 5秒ほどタイマー処理してそのあと下行を実行
        // Game game = ((GameConfigurator)plugin).getGame();
        // game.currentState = game.playingState;
    }
}
