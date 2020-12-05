package jp.hack.minecraft.werewolfgame.core;

import jp.hack.minecraft.werewolfgame.core.display.DisplayManager;
import jp.hack.minecraft.werewolfgame.core.display.TaskManager;
import jp.hack.minecraft.werewolfgame.core.state.*;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Game extends BukkitRunnable {
    private final static Game game = new Game();
    public static synchronized Game getInstance() {
        return game;
    }

    private final DisplayManager displayManager = new DisplayManager();
    private final TaskManager taskManager = new TaskManager();

    private Map<UUID, WPlayer> wPlayers = new HashMap<>();
    private Location respawn;
    private Boolean canCommunicate = false;

    //ゲームの初期状態はロビーでスタートします
    private GameState currentState = new LobbyState(this);

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

    public DisplayManager getDisplayManager() {
        return displayManager;
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public void taskCompleted() {
        /*taskManager.notifyObservers();*/
    }

    // public void start() {}
    public void hostStart() {
        currentState = new LobbyState(this);
    }
    public void gameStart() {
        currentState = new PlayingState(this);
    }
    public void meetingStart() {
        currentState = new MeetingState(this);
    }
    public void voteStart() {
        currentState = new VotingState(this);
    }

    public void stop() {
        this.cancel();
    }

    @Override
    public void run() {
        System.out.println("更新処理");
    }
}