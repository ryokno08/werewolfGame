package jp.hack.minecraft.werewolfgame;

import jp.hack.minecraft.werewolfgame.core.Role;
import jp.hack.minecraft.werewolfgame.core.task.TaskManager;
import jp.hack.minecraft.werewolfgame.core.WPlayer;
import jp.hack.minecraft.werewolfgame.core.display.DisplayManager;
import jp.hack.minecraft.werewolfgame.core.state.*;
import jp.hack.minecraft.werewolfgame.util.LocationConfiguration;
import jp.hack.minecraft.werewolfgame.util.Messages;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private final JavaPlugin plugin;

    public enum ErrorJudge {
        CONFIG_NULL,
        ALREADY_STARTED,
        MANAGER_NULL,
        WPLAYERS_NULL,
        NONE
    }

    enum WinnerJudge {
        CLUE_WIN,
        IMPOSTER_WIN,
        NONE
    }

    private final Map<UUID, WPlayer> wPlayers = new HashMap<>();
    private List<Player> joinedPlayers = new ArrayList<>();
    private DisplayManager displayManager;
    private TaskManager taskManager;

    private Boolean wasStarted = false;
    private int numberOfImposter = 1;
    private int numberOfTasks = 3;
    private int limitOfVoting = 60;
    private Location lobbyPos;
    private Location meetingPos;
    private ItemStack itemForKill = new ItemStack(Material.IRON_SWORD);

    //ゲームの初期状態はロビーでスタート
    private LobbyState lobbyState;
    private MeetingState meetingState;
    private PlayingState playingState;
    private VotingState votingState;
    private GameState currentState;

    private LocationConfiguration configuration;

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

    public Map<UUID, WPlayer> getWPlayers() {
        return wPlayers;
    }

    public void putWPlayer(WPlayer wPlayer) {
        wPlayers.put(wPlayer.getUuid(), wPlayer);
    }

    public WPlayer getWPlayer(UUID uuid) {
        return wPlayers.get(uuid);
    }

    public List<Player> getJoinedPlayers() {
        return joinedPlayers;
    }

    public void setJoinedPlayers(List<Player> joinedPlayers) {
        this.joinedPlayers = joinedPlayers;
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

    public int getLimitOfVoting() {
        return limitOfVoting;
    }

    public void setLimitOfVoting(int limitOfVoting) {
        this.limitOfVoting = limitOfVoting;
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
        if (!getWPlayers().containsKey(uuid)) return false;

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

    public void taskCompleted(Player player, int no) {
        taskManager.onTaskFinished(player, no);
    }

    public void setCurrentState(GameState currentState) {
        this.currentState = currentState;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public LobbyState getLobbyState() {
        return lobbyState;
    }
    public PlayingState getPlayingState() {
        return playingState;
    }
    public MeetingState getMeetingState() {
        return meetingState;
    }
    public VotingState getVotingState() {
        return votingState;
    }


    Game(JavaPlugin plugin, LocationConfiguration configuration) {
        this.plugin = plugin;
        this.configuration = configuration;
    }





    // gameStart、meetingStartはコマンドが来たとき呼び出す
    public ErrorJudge gameStart() {

        if (wasStarted) return ErrorJudge.ALREADY_STARTED;

        resetManagers();
        resetStates();
        if (!reloadConfig()) return ErrorJudge.CONFIG_NULL;
        if (!resetWPlayers()) return ErrorJudge.MANAGER_NULL; //Managerリセット後に実行
        if (!setImposters()) return ErrorJudge.WPLAYERS_NULL; //wPlayersリセット後に実行

        displayManager.setTaskBarVisible(false);

        currentState = lobbyState;
        currentState.onActive();

        wasStarted = true;

        return ErrorJudge.NONE;
    }



    public void saveConfig() {
        if (lobbyPos != null) configuration.setLocationData("lobby", lobbyPos);
        if (meetingPos != null) configuration.setLocationData("meeting", meetingPos);

    }

    private boolean reloadConfig() {

        lobbyPos = configuration.getLocationData("lobby");
        meetingPos = configuration.getLocationData("meeting");
        System.out.println("lobbyPos:"+lobbyPos);
        System.out.println("meetingPos:"+meetingPos);
        System.out.println("return:"+(meetingPos != null && lobbyPos != null));
        System.out.println("------------------------------");
        return (meetingPos != null && lobbyPos != null);

    }

    private void resetManagers() {

        displayManager = new DisplayManager(this);
        taskManager = new TaskManager(this);

    }

    private void resetStates() {

        lobbyState = new LobbyState(plugin, this);
        meetingState = new MeetingState(plugin, this);
        playingState = new PlayingState(plugin, this);
        votingState = new VotingState(plugin, this);

    }

    private Boolean resetWPlayers() {

        if (displayManager == null || taskManager == null) return false;

        wPlayers.clear();
        setJoinedPlayers(new ArrayList<>(plugin.getServer().getOnlinePlayers()));

        for (Player player : joinedPlayers) {
            WPlayer wPlayer = new WPlayer(player.getUniqueId());
            this.putWPlayer(wPlayer);

            player.getInventory().clear();

            displayManager.addTaskBar(player);
            taskManager.setTasks(wPlayer);
        }
        return true;
    }

    private Boolean setImposters() {

        if (wPlayers.isEmpty()) return false;

        List<Player> players = new ArrayList<>(this.joinedPlayers);

        Random random = new Random();
        for (int i = 0; i < numberOfImposter; i++) {
            Player selectedPlayer = players.get(random.nextInt(players.size()));
            players.remove(selectedPlayer);

            WPlayer wPlayer = getWPlayer(selectedPlayer.getUniqueId());
            wPlayer.setRole(Role.IMPOSTER);
        }

        return true;

    }

    public void changeState(GameState state) {
        if (currentState == state) return;
        currentState.onInactive();
        currentState = state;
        if (currentState != votingState) {
            changeScene();
        } else {
            currentState.onActive();
        }
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

    private WinnerJudge confirmTask() {
        int finishedTask = taskManager.getFinishedTask();

        if(numberOfTasks == finishedTask) {
            return WinnerJudge.CLUE_WIN;
        }
        return WinnerJudge.NONE;
    }

    private WinnerJudge confirmNoOfPlayers() {
        int clueMateRemains = wPlayers.values().stream().filter(p -> !p.getRole().isImposter() && !p.isDied()).collect(Collectors.toSet()).size();
        int impostorRemains = wPlayers.values().stream().filter(p -> p.getRole().isImposter() && !p.isDied()).collect(Collectors.toSet()).size();

        if (clueMateRemains <= impostorRemains) {
            return WinnerJudge.IMPOSTER_WIN;
        }
        if (impostorRemains == 0) {
            return WinnerJudge.CLUE_WIN;
        }
        return WinnerJudge.NONE;
    }

    public void confirmGame() {

        WinnerJudge winnerJudge = confirmTask();

        if (winnerJudge == WinnerJudge.CLUE_WIN) {
            playerVictory();
        } else {
            winnerJudge = confirmNoOfPlayers();

            if (winnerJudge == WinnerJudge.CLUE_WIN) {
                playerVictory();
            } else if (winnerJudge == WinnerJudge.IMPOSTER_WIN) {
                playerDefeat();
            }
        }

    }

    private BukkitRunnable task;
    private final int limit = 3*20;

    public void changeScene() {
        this.joinedPlayers.forEach(p->{
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, limit, 100));
        });

        task = new BukkitRunnable() {
            @Override
            public void run() {
                currentState.onActive();
            }
        };

        task.runTaskLater(plugin, limit);
    }

    public void ejectPlayer(String uuid) {
        Player player = plugin.getServer().getPlayer(uuid);
        this.joinedPlayers.forEach(p -> p.sendMessage(Messages.message("002", player.getDisplayName())));
        player.setGameMode(GameMode.SPECTATOR);
        plugin.getLogger().info(Messages.message("002", player.getDisplayName()));
    }

    public void voteSkipped() {
        this.joinedPlayers.forEach(player -> player.sendMessage("Lobby"));
        this.joinedPlayers.forEach(p -> p.sendMessage(Messages.message("006")));
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


    private void stop() {
        wasStarted = false;
        currentState.onInactive();
        displayManager.setTaskBarVisible(false);

        this.joinedPlayers.forEach(p -> {
            p.setGameMode(GameMode.ADVENTURE);
            p.teleport(getLobbyPos());
            plugin.getLogger().info(getLobbyPos().serialize().toString());
        });
    }
}