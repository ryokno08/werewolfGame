package jp.hack.minecraft.werewolfgame;

import jp.hack.minecraft.werewolfgame.core.Role;
import jp.hack.minecraft.werewolfgame.core.WPlayer;
import jp.hack.minecraft.werewolfgame.core.display.DisplayManager;
import jp.hack.minecraft.werewolfgame.core.TaskManager;
import jp.hack.minecraft.werewolfgame.core.state.*;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Game extends BukkitRunnable {
    private JavaPlugin plugin;

    private final DisplayManager displayManager = new DisplayManager();
    private final TaskManager taskManager = new TaskManager(this);

    private Map<UUID, WPlayer> wPlayers = new HashMap<>();
    private Location respawn;
    private Location lobbyPos;
    private Boolean canCommunicate = false;

    //ゲームの初期状態はロビーでスタート
    private final LobbyState lobbyState = new LobbyState(plugin);
    private final MeetingState meetingState = new MeetingState(plugin);
    public final PlayingState playingState = new PlayingState(plugin);
    private final VotingState votingState = new VotingState(plugin);
    public GameState currentState = lobbyState;

    Game(JavaPlugin plugin) {
        this.plugin = plugin;
        displayManager.setTaskBarVisible(false);
    }

    public Game getGame() {
        return this;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

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

    public Location getLobbyPos() {
        return lobbyPos;
    }

    public void setLobbyPos(Location lobbyPos) {
        this.lobbyPos = lobbyPos;
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

    public void taskCompleted(int no) {
        taskManager.taskFinished(no);
    }

    // public void start() {}
    /*
    public void hostStart() {
        currentState = new LobbyState(this);
    }
     */

    // gameStart、meetingStartはコマンドが来たとき呼び出す
    public void gameStart() {
        lobbyState.gameStart();
        runTaskTimer(plugin, 10, 20);
        displayManager.setTaskBarVisible(true);
    }
    public void meetingStart() {
        meetingState.meetingLogic(plugin);
        currentState = meetingState;
        currentState.init(this);
    }
    public void voteStart() {
        currentState = votingState;
        currentState.init(this);
    }

    public void endEvent(){
        currentState = playingState;
        currentState.init(this);
    }



    public void stop() {
        this.cancel();
    }

    @Override
    public void run() {
        System.out.println("更新処理");
    }
}