package jp.hack.minecraft.werewolfgame.core;

import jp.hack.minecraft.werewolfgame.core.display.TaskBar;
import jp.hack.minecraft.werewolfgame.core.state.*;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Game extends BukkitRunnable {
    private final static Game game = new Game();
    public static synchronized Game getInstance() {
        return game;
    }

    private Map<UUID, WPlayer> wPlayers = new HashMap<>();
    private Location respawn;
    // private Boolean canTalk = true;
    private Boolean canCommunicate = false; // 設定用にOptionクラスみたいなの作ってそこに入れる?
    private final TaskBar taskBar = new TaskBar();
    private int maxTask = 10;
    private int task = 0;
    
    //ゲームの初期状態はロビーでスタートします
    private GameState currentState = LobbyState.getInstance();

    public Map<UUID, WPlayer> getwPlayers() {
        return wPlayers;
    }

    public void putWPlayer(WPlayer wPlayer) {
        wPlayers.put(wPlayer.getUuid(), wPlayer);
    }

    public WPlayer getWPlayer(UUID uuid) {
        return wPlayers.get(uuid);
    }

    public Location getRespawn() {
        return respawn;
    }

    public void setRespawn(Location respawn) {
        this.respawn = respawn;
    }

    public Role getPlayerRole(UUID uuid) {
        return getWPlayer(uuid).getRole();
    }

    public Boolean setPlayerRole(UUID uuid, Role role) {
        if (!getwPlayers().containsKey(uuid)) return false;

        getWPlayer(uuid).setRole(role);
        return true;
    }

    public Boolean canSpeak() {
        return currentState.canSpeak();
    }
    /*
    public void setCanTalk(Boolean canTalk) {
        this.canTalk = canTalk;
    }
    */
    public Boolean canCommunicate() {
        return canCommunicate;
    }

    public void setCanCommunicate(Boolean canCommunicate) {
        this.canCommunicate = canCommunicate;
    }

    public TaskBar getTaskBar() {
        return taskBar;
    }

    public void taskCompleted() {

        task++;

        if (maxTask <= task) {
            stop();
        }

        taskBar.setTask(maxTask / task);
    }

    // public void start() {}
    public void hostStart() {
        currentState = LobbyState.getInstance();
    }
    public void gameStart() {
        currentState = PlayingState.getInstance();
    }
    public void meetingStart() {
        currentState = MeetingState.getInstance();
    }
    public void voteStart() {
        currentState = VotingState.getInstance();
    }

    public void stop() {
        task = 0;
    }

    @Override
    public void run() {
        System.out.println("更新処理");
    }
}