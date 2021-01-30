package jp.hack.minecraft.werewolfgame;

import jp.hack.minecraft.werewolfgame.core.Role;
import jp.hack.minecraft.werewolfgame.core.WPlayer;
import jp.hack.minecraft.werewolfgame.core.display.DisplayManager;
import jp.hack.minecraft.werewolfgame.core.TaskManager;
import jp.hack.minecraft.werewolfgame.core.state.*;
import jp.hack.minecraft.werewolfgame.core.utils.Scoreboard;
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
    private final LobbyState lobbyState;
    private final MeetingState meetingState;
    private final PlayingState playingState;
    private final VotingState votingState;
    // private ArrayDeque<GameState> nextStates;
    private GameState currentState;

    private Map<UUID, Scoreboard> scoreboards; // = new

    // UUIDは投票者のもの Stringは投票先のUUIDもしくは"Skip"が入る
    private Map<UUID, String> votedPlayers = new HashMap<>();

    Game(JavaPlugin plugin) {
        this.plugin = plugin;
        displayManager.setTaskBarVisible(false);

        lobbyState = new LobbyState(plugin);
        lobbyState.onStart(this);
        meetingState = new MeetingState(plugin);
        meetingState.onStart(this);
        playingState = new PlayingState(plugin);
        playingState.onStart(this);
        votingState = new VotingState(plugin);
        votingState.onStart(this);
        // nextStates = new ArrayDeque<>(Arrays.asList(playingState));
        currentState = lobbyState;
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

    public Boolean canMove() {
        return currentState.canMove();
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

    public void setCurrentState(GameState currentState) {
        this.currentState = currentState;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    // gameStart、meetingStartはコマンドが来たとき呼び出す
    public void gameStart() {
        if (currentState == playingState) return;
        currentState.onInactive();
        currentState = lobbyState;
        currentState.onActive();

        displayManager.setTaskBarVisible(true);
    }
    public void returnToGame() {
        if (currentState == playingState) return;
        currentState.onInactive();
        currentState = playingState;
        currentState.onActive();
    }
    public void meetingStart() {
        if (currentState == meetingState) return;
        currentState.onInactive();
        currentState = meetingState;
        currentState.onActive();

    }

    public void voteStart() {
        if (currentState == votingState) return;
        currentState.onInactive();
        currentState = votingState;
        currentState.onActive();
    }

//    public void nextState() {
//        if (nextStates.size() < 1) return;
//
//        currentState.onInactive();
//        currentState = nextStates.removeFirst();
//        currentState.onActive();
//    }

    public boolean votePlayer(UUID voter, UUID target) {
        if (currentState == votingState && !votedPlayers.containsKey(voter)) {
            votedPlayers.put(voter, target.toString());
            return true;
        }
        return false;
    }
    public boolean voteSkip(UUID uuid) {
        if (currentState == votingState && !votedPlayers.containsKey(uuid)) {
            votedPlayers.put(uuid, "Skip");
            return true;
        }
        return false;
    }

    public void victory() {

    }

    public void defeat() {

    }


    public void stop() {
        this.cancel();
    }

    @Override
    public void run() {
        System.out.println("更新処理");
    }
}