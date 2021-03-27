package jp.hack.minecraft.werewolfgame;

import jp.hack.minecraft.werewolfgame.core.Cadaver;
import jp.hack.minecraft.werewolfgame.core.Colors;
import jp.hack.minecraft.werewolfgame.core.Role;
import jp.hack.minecraft.werewolfgame.core.display.WPlayerInventory;
import jp.hack.minecraft.werewolfgame.core.task.TaskManager;
import jp.hack.minecraft.werewolfgame.core.WPlayer;
import jp.hack.minecraft.werewolfgame.core.display.DisplayManager;
import jp.hack.minecraft.werewolfgame.core.state.*;
import jp.hack.minecraft.werewolfgame.util.LocationConfiguration;
import jp.hack.minecraft.werewolfgame.util.Messages;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
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
    private final Map<UUID, Cadaver> cadavers = new HashMap<>();
    private List<Player> joinedPlayers = new ArrayList<>();
    private DisplayManager displayManager;
    private TaskManager taskManager;
    private WPlayerInventory wPlayerInventory;

    private Boolean wasStarted = false;
    private int numberOfImposter = 1;
    private int numberOfTasks = 3;
    private int limitOfVoting = 60;
    private double reportDistance = 5.0;
    private Location lobbyPos;
    private Location meetingPos;
    private ItemStack itemForKill = new ItemStack(Material.IRON_SWORD);
    private ItemStack itemForReport = new ItemStack(Material.COMPASS);

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

    public Cadaver createCadaver(Player player) {
        Cadaver cadaver = new Cadaver(player);
        cadavers.put(player.getUniqueId(), cadaver);
        return cadaver;
    }

    public Map<UUID, Cadaver> getCadavers() {
        return cadavers;
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

    public double getReportDistance() {
        return reportDistance;
    }

    public void setReportDistance(int reportDistance) {
        this.reportDistance = reportDistance;
    }

    public Location getLobbyPos() {
        return lobbyPos;
    }

    public void setLobbyPos(Location lobbyPos) {
        this.lobbyPos = lobbyPos;
        configuration.setLocationData("lobby", lobbyPos);
        configuration.save();
    }

    public Location getMeetingPos() {
        return meetingPos;
    }

    public void setMeetingPos(Location meetingPos) {
        this.meetingPos = meetingPos;
        configuration.setLocationData("meeting", meetingPos);
        configuration.save();
    }

    public ItemStack getItemForKill() {
        return itemForKill;
    }

    public void setItemForKill(ItemStack itemForKill) {
        this.itemForKill = itemForKill;
    }

    public ItemStack getItemForReport() {
        return itemForReport;
    }

    public void setItemForReport(ItemStack itemForReport) {
        this.itemForReport = itemForReport;
    }

    public Role getPlayerRole(UUID uuid) {
        return getWPlayer(uuid).getRole();
    }

    public void setPlayerRole(UUID uuid, Role role) {
        getWPlayer(uuid).setRole(role);
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

        ErrorJudge resetJudge = allReset();
        if ( !resetJudge.equals(ErrorJudge.NONE) ) return resetJudge;

        displayManager.setTaskBarVisible(false);
        taskManager.setMaxTasks(numberOfTasks * (wPlayers.size() - numberOfImposter));
        taskManager.taskBarUpdate();

        currentState = lobbyState;
        currentState.onActive();

        wasStarted = true;

        return ErrorJudge.NONE;
    }

    private ErrorJudge allReset() {

        resetManagers();
        resetStates();
        if (!reloadConfig()) return ErrorJudge.CONFIG_NULL;
        if (!resetWPlayers()) return ErrorJudge.MANAGER_NULL; //Managerリセット後に実行
        if (!setImposters()) return ErrorJudge.WPLAYERS_NULL; //wPlayersリセット後に実行
        return ErrorJudge.NONE;

    }

    private boolean reloadConfig() {

        lobbyPos = configuration.getLocationData("lobby");
        meetingPos = configuration.getLocationData("meeting");
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
        List<Color> colors = new ArrayList<>(Colors.values());

        for (Player player : joinedPlayers) {
            WPlayer wPlayer = new WPlayer(player.getUniqueId());
            this.putWPlayer(wPlayer);

            int random = new Random().nextInt(colors.size());
            wPlayer.setColor(colors.get(random));
            colors.remove(random);

            player.getInventory().clear();
            displayManager.resetColorArmor(player);
            displayManager.addTaskBar(player);
            taskManager.setTasks(wPlayer);
        }
        return true;
    }

    private void resetCadaver() {
        cadavers.values().forEach(Cadaver::removeBlock);
        cadavers.clear();
    }

    private Boolean setImposters() {

        if (wPlayers.isEmpty()) return false;

        List<Player> players = new ArrayList<>(this.joinedPlayers);

        for (int i = 0; i < numberOfImposter; i++) {
            Player selectedPlayer = players.get(new Random().nextInt(players.size()));
            players.remove(selectedPlayer);

            WPlayer wPlayer = getWPlayer(selectedPlayer.getUniqueId());
            wPlayer.setRole(Role.IMPOSTER);
            wPlayer.clearTasks();
        }

        return true;

    }

    public void changeState(GameState state) {
        if (currentState == state) return;
        currentState.onInactive();
        if (currentState != meetingState) {
            changeScene(state);
        } else {
            currentState = state;
            currentState.onActive();
        }
    }

    public void report(Player reportedPlayer) {
        WPlayer reportedWPlayer = getWPlayer(reportedPlayer.getUniqueId());

        reportedWPlayer.setReport(true);
        displayManager.allSendTitle(ChatColor.GREEN + Messages.message("008"), reportedPlayer.getDisplayName());
        displayManager.allMakeSound(Sound.ENTITY_LIGHTNING_THUNDER, SoundCategory.MASTER, (float)1.0, (float)1.0);
        changeState(meetingState);
    }

    public void report(Player reportedPlayer, Player diedPlayer) {
        cadavers.values().forEach(Cadaver::removeBlock);
        cadavers.clear();

        displayManager.allSendTitle(ChatColor.BOLD.toString() + ChatColor.LIGHT_PURPLE.toString() + diedPlayer.getDisplayName(), Messages.message("004"));
        displayManager.allSendMessage(Messages.message("007", reportedPlayer.getDisplayName()));
        displayManager.allMakeSound(Sound.ENTITY_ZOMBIE_VILLAGER_AMBIENT, SoundCategory.MASTER, (float)1.0, (float)0.3);
        changeState(meetingState);
    }

    public void playerDied(Player player) {
        WPlayer wPlayer = getWPlayer(player.getUniqueId());

        wPlayer.setDied(true);
        createCadaver(player);
        player.setGameMode(GameMode.SPECTATOR);
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
        int maxTask = taskManager.getMaxTasks();

        if ( maxTask == finishedTask ) {
            return WinnerJudge.CLUE_WIN;
        }
        return WinnerJudge.NONE;
    }

    private WinnerJudge confirmNoOfPlayers() {
        int clueMateRemains = wPlayers.values().stream().filter(p -> !p.getRole().isImposter() && !p.isDied()).collect(Collectors.toSet()).size();
        int impostorRemains = wPlayers.values().stream().filter(p -> p.getRole().isImposter() && !p.isDied()).collect(Collectors.toSet()).size();

        if ( clueMateRemains <= impostorRemains ) {
            return WinnerJudge.IMPOSTER_WIN;
        }
        if ( impostorRemains == 0 ) {
            return WinnerJudge.CLUE_WIN;
        }
        return WinnerJudge.NONE;
    }

    public void confirmGame() {
        WinnerJudge winnerJudge = confirmTask();

        if (winnerJudge == WinnerJudge.CLUE_WIN) {
            displayManager.showIssue(true);
            gameStop();
        } else {
            winnerJudge = confirmNoOfPlayers();

            if (winnerJudge == WinnerJudge.CLUE_WIN) {
                displayManager.showIssue(true);
                gameStop();
            } else if (winnerJudge == WinnerJudge.IMPOSTER_WIN) {
                displayManager.showIssue(false);
                gameStop();
            }
        }
    }

    private BukkitRunnable task;
    private final int limit = 3*20;

    public void changeScene(GameState state) {
        GameState state1 = state;
        displayManager.allBlindness(limit +(25));

        task = new BukkitRunnable() {
            @Override
            public void run() {
                currentState = state1;
                currentState.onActive();
            }
        };

        task.runTaskLater(plugin, limit);
    }

    public void ejectPlayer(UUID uuid) {
        Player player = plugin.getServer().getPlayer(uuid);
        displayManager.allSendMessage("002", player.getDisplayName());
        displayManager.log(Messages.message("002", player.getDisplayName()));

        player.setGameMode(GameMode.SPECTATOR);
    }

    public void voteSkipped() {
        displayManager.allSendMessage("006");
        displayManager.log(Messages.message("006"));
    }

    public void gameStop() {
        wasStarted = false;
        currentState.onInactive();
        displayManager.setTaskBarVisible(false);
        resetCadaver();

        this.joinedPlayers.forEach(p -> {
            p.setGameMode(GameMode.ADVENTURE);
            p.teleport(getLobbyPos());
        });
    }
}