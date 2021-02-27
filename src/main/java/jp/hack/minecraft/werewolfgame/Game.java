package jp.hack.minecraft.werewolfgame;

import jp.hack.minecraft.werewolfgame.core.Role;
import jp.hack.minecraft.werewolfgame.core.task.TaskManager;
import jp.hack.minecraft.werewolfgame.core.WPlayer;
import jp.hack.minecraft.werewolfgame.core.display.DisplayManager;
import jp.hack.minecraft.werewolfgame.core.state.*;
import jp.hack.minecraft.werewolfgame.core.utils.Scoreboard;
import org.bukkit.Bukkit;
import jp.hack.minecraft.werewolfgame.util.Messages;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class Game extends BukkitRunnable {
    private final JavaPlugin plugin;

    private final Map<UUID, WPlayer> wPlayers = new HashMap<>();
    private final DisplayManager displayManager;
    private final TaskManager taskManager;

    private Boolean wasStarted = false;
    private Boolean impostorVictory = false;
    private Boolean clueMateVictory = false;

    private int numberOfImposter = 1;
    private int numberOfTasks = 10;
    private Location respawn;
    private Location lobbyPos;
    private Boolean canCommunicate = false;
    private ItemStack itemForKill = new ItemStack(Material.IRON_SWORD);

    //ゲームの初期状態はロビーでスタート
    private final LobbyState lobbyState;
    private final MeetingState meetingState;
    private final PlayingState playingState;
    private final VotingState votingState;
    // private ArrayDeque<GameState> nextStates;
    private GameState currentState;

    private Map<UUID, Scoreboard> scoreboards; // = new

    // UUIDは投票者のもの Stringは投票先のUUIDもしくは"Skip"が入る
    public Map<UUID, String> votedPlayers = new HashMap<>();

    Game(JavaPlugin plugin) {
        this.plugin = plugin;

        displayManager = new DisplayManager(this);
        taskManager = new TaskManager(this);

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

    public Boolean wasStarted() {
        return wasStarted;
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

    public int getNumberOfImposter() {
        return numberOfImposter;
    }

    public void setNumberOfImposter(int numberOfImposter) {
        this.numberOfImposter = numberOfImposter;
    }

    public int getNumberOfTasks() {
        return numberOfTasks;
    }

    public void setNumberOfTasks(int numberOfTasks) {
        this.numberOfTasks = numberOfTasks;
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

    public ItemStack getItemForKill() {
        return itemForKill;
    }

    public void setItemForKill(ItemStack itemForKill) {
        this.itemForKill = itemForKill;
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
        taskManager.onTaskFinished(no);
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
        wasStarted = true;
        currentState = lobbyState;
        currentState.onActive();

        Random random = new Random();
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (int i = 0; i < numberOfImposter; i++) {
            Player selectedPlayer = players.get(random.nextInt(players.size()));
            players.remove(selectedPlayer);

            WPlayer wPlayer = getWPlayer(selectedPlayer.getUniqueId());
            wPlayer.setRole(Role.WOLF);
        }
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

    public void confirmTask() {
        int finishedTask = taskManager.getFinishedTask();

        if(numberOfTasks == finishedTask) {
            clueMateVictory = true;
        }
    }

    public void confirmNoOfPlayers() {
        int clueMateRemains = wPlayers.values().stream().filter(v -> !v.getRole().isWolf() && !v.isDied()).collect(Collectors.toSet()).size();
        int impostorRemains = wPlayers.values().stream().filter(v -> v.getRole().isWolf() && !v.isDied()).collect(Collectors.toSet()).size();

        if (clueMateRemains <= impostorRemains) {
            impostorVictory = true;
        }
        if (impostorRemains == 0) {
            clueMateVictory = true;
        }
    }

    public void confirmGame() {
        confirmTask();
        confirmNoOfPlayers();

        if (impostorVictory && clueMateVictory) {
            playerDraw();
        } else if (impostorVictory) {
            playerDefeat();
        } else if (clueMateVictory){
            playerVictory();
        }
    }

    public void ejectPlayer(String uuid) {
        Player player = plugin.getServer().getPlayer(uuid);
        plugin.getServer().getOnlinePlayers().forEach(p -> p.sendMessage(Messages.message("002", player.getDisplayName())));
        player.setHealth(0);
        plugin.getLogger().info(Messages.message("002", player.getDisplayName()));
    }

    public void voteSkipped() {
        plugin.getServer().getOnlinePlayers().forEach(player -> player.sendMessage("Lobby"));
        plugin.getServer().getOnlinePlayers().forEach(p -> p.sendMessage(Messages.message("006")));
        plugin.getLogger().info(Messages.message("006"));
    }

    public void playerVictory() {
        displayManager.playerVictory();
        stop();
    }

    public void playerDefeat() {
        displayManager.playerDefeat();
        stop();
    }

    public void playerDraw() {
        stop();
    }


    public void stop() {
        displayManager.setTaskBarVisible(false);
        wasStarted = false;
        this.cancel();
    }

    @Override
    public void run() {
        System.out.println("更新処理");
    }
}