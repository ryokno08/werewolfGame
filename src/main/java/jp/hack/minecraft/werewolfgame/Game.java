package jp.hack.minecraft.werewolfgame;

import jp.hack.minecraft.werewolfgame.core.Role;
import jp.hack.minecraft.werewolfgame.core.task.TaskManager;
import jp.hack.minecraft.werewolfgame.core.WPlayer;
import jp.hack.minecraft.werewolfgame.core.display.DisplayManager;
import jp.hack.minecraft.werewolfgame.core.state.*;
import org.bukkit.Bukkit;
import jp.hack.minecraft.werewolfgame.util.Messages;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private final JavaPlugin plugin;

    enum GameJudge {
        CLUE_WIN,
        IMPOSTER_WIN,
        NONE
    }

    private final Map<UUID, WPlayer> wPlayers = new HashMap<>();
    private DisplayManager displayManager;
    private TaskManager taskManager;

    private Boolean wasStarted = false;
    private Boolean impostorVictory = false;
    private Boolean clueMateVictory = false;

    private int numberOfImposter = 1;
    private int numberOfTasks = 10;
    private Location respawn;
    private Location lobbyPos;
    private Location meetingPos;
    private ItemStack itemForKill = new ItemStack(Material.IRON_SWORD);

    //ゲームの初期状態はロビーでスタート
    private LobbyState lobbyState;
    private MeetingState meetingState;
    private PlayingState playingState;
    private VotingState votingState;
    // private ArrayDeque<GameState> nextStates;
    private GameState currentState;

    // UUIDは投票者のもの Stringは投票先のUUIDもしくは"Skip"が入る
    public Map<UUID, String> votedPlayers = new HashMap<>();

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

    public Location getMeetingPos() {
        return meetingPos;
    }

    public void setMeetingPos(Location meetingPos) {
        this.meetingPos = meetingPos;
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

    public DisplayManager getDisplayManager() {
        return displayManager;
    }

    public TaskManager getTaskManager() {
        return taskManager;
    }

    public void taskCompleted(int no) {
        taskManager.onTaskFinished(no);
    }

    public void setCurrentState(GameState currentState) {
        this.currentState = currentState;
    }

    public GameState getCurrentState() {
        return currentState;
    }




    Game(JavaPlugin plugin) {
        this.plugin = plugin;
    }



    // gameStart、meetingStartはコマンドが来たとき呼び出す
    public Boolean gameStart() {

        if (wasStarted) return false;
        wasStarted = true;

        displayManager = new DisplayManager(this);
        displayManager.setTaskBarVisible(false);
        taskManager = new TaskManager(this);

        lobbyState = new LobbyState(plugin);
        lobbyState.onStart(this);
        meetingState = new MeetingState(plugin);
        meetingState.onStart(this);
        playingState = new PlayingState(plugin);
        playingState.onStart(this);
        votingState = new VotingState(plugin);
        votingState.onStart(this);

        currentState = lobbyState;
        currentState.onActive();

        Random random = new Random();

        wPlayers.clear();
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

        for (Player player : players) {
            this.putWPlayer(new WPlayer(player.getUniqueId()));
            this.getDisplayManager().addTaskBar(player);
        }
        for (int i = 0; i < numberOfImposter; i++) {
            Player selectedPlayer = players.get(random.nextInt(players.size()));
            players.remove(selectedPlayer);

            WPlayer wPlayer = getWPlayer(selectedPlayer.getUniqueId());
            wPlayer.setRole(Role.IMPOSTER);
        }

        return true;
    }

    public void returnToPlay() {
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

    public GameJudge confirmTask() {
        int finishedTask = taskManager.getFinishedTask();

        if(numberOfTasks == finishedTask) {
            return GameJudge.CLUE_WIN;
        }
        return GameJudge.NONE;
    }

    public GameJudge confirmNoOfPlayers() {
        int clueMateRemains = wPlayers.values().stream().filter(p -> !p.getRole().isImposter() && !p.isDied()).collect(Collectors.toSet()).size();
        int impostorRemains = wPlayers.values().stream().filter(p -> p.getRole().isImposter() && !p.isDied()).collect(Collectors.toSet()).size();

        System.out.println("clueMateRemains: "+clueMateRemains);
        System.out.println("impostorRemains: "+impostorRemains);

        if (clueMateRemains <= impostorRemains) {
            return GameJudge.IMPOSTER_WIN;
        }
        if (impostorRemains == 0) {
            return GameJudge.CLUE_WIN;
        }
        return GameJudge.NONE;
    }

    public void confirmGame() {

        GameJudge gameJudge = confirmTask();

        if (gameJudge == GameJudge.CLUE_WIN) {
            playerVictory();
        } else {
            gameJudge = confirmNoOfPlayers();

            if (gameJudge == GameJudge.CLUE_WIN) {
                playerVictory();
            } else if (gameJudge == GameJudge.IMPOSTER_WIN) {
                playerDefeat();
            }
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


    public void stop() {
        wasStarted = false;
        currentState.onInactive();
        displayManager.setTaskBarVisible(false);

        plugin.getServer().getOnlinePlayers().forEach(p -> {
            p.setGameMode(GameMode.ADVENTURE);
            p.teleport(lobbyPos);
        });
    }
}