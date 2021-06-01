package jp.hack.minecraft.werewolfgame;

import jp.hack.minecraft.werewolfgame.core.*;
import jp.hack.minecraft.werewolfgame.core.display.*;
import jp.hack.minecraft.werewolfgame.core.task.Task;
import jp.hack.minecraft.werewolfgame.core.task.TaskManager;
import jp.hack.minecraft.werewolfgame.core.state.*;
import jp.hack.minecraft.werewolfgame.logic.GameDirector;
import jp.hack.minecraft.werewolfgame.logic.GuiLogic;
import jp.hack.minecraft.werewolfgame.util.LocationConfiguration;
import jp.hack.minecraft.werewolfgame.util.Messages;
import me.mattstudios.mfgui.gui.components.ItemBuilder;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class Game {
    private final JavaPlugin plugin;
    private final LocationConfiguration configuration;
    private final GameDirector gameDirector;

    public enum ErrorJudge {
        CONFIG_NULL,
        ALREADY_STARTED,
        MANAGER_NULL,
        WPLAYERS_NULL,
        WPLAYERS_FULL,
        NONE
    }

    enum WinnerJudge {
        CLUE_WIN,
        IMPOSTER_WIN,
        NONE
    }

    private final Map<UUID, WPlayer> wPlayers = new HashMap<>();
    private final Map<UUID, Cadaver> cadavers = new HashMap<>();
    private final Map<UUID, Scapegoat> scapegoats = new HashMap<>();
    private final Map<String, UUID> colorUuidMap = new HashMap<>();
    private List<Player> joinedPlayers = new ArrayList<>();
    private DisplayManager displayManager;
    private TaskManager taskManager;
    private VoteBoard voteBoard;
    private TaskBoard taskBoard;

    private Boolean wasStarted = false;
    private int numberOfImposter = 1;
    private int numberOfTasks = 2;
    private int limitOfVoting = 60;
    private double reportDistance = 5.0;
    private Location lobbyPos;
    private Location meetingPos;
    private List<Location> tasksPos;
    private final ItemStack itemForKill = new ItemStack(Material.IRON_SWORD);
    private final ItemStack itemForReport = new ItemStack(Material.COMPASS);

    //ゲームの初期状態はロビーでスタート
    private LobbyState lobbyState;
    private MeetingState meetingState;
    private PlayingState playingState;
    private VotingState votingState;
    private GameState currentState;

    private List<Task> tasks = new ArrayList<>();
    private GuiLogic guiLogic;

    // UUIDは投票者のもの Stringは投票先のUUIDもしくは"Skip"が入る
    public Map<UUID, UUID> votedPlayers = new HashMap<>();
    public List<UUID> skippedPlayers = new ArrayList<>();

    public Game getGame() {
        return this;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public GameDirector getGameLogic() {
        return gameDirector;
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

    public void createCadaver(Player player) {
        UUID uuid = player.getUniqueId();

        Cadaver cadaver = new Cadaver(player, getWPlayer(uuid));
        cadavers.put(uuid, cadaver);
    }

    public Map<UUID, Cadaver> getCadavers() {
        return cadavers;
    }

    public Map<String, UUID> getColorUuidMap() {
        return colorUuidMap;
    }

    public List<Player> getJoinedPlayers() {
        return joinedPlayers;
    }

    public Player getPlayer(UUID uuid) {
        return joinedPlayers
                .stream()
                .filter(p->p.getUniqueId().equals(uuid))
                .findFirst()
                .orElse(null);
    }

    public Player getNearPlayer(Location location) {
        Map<UUID, Double> distanceMap = getAlivePlayer().stream().collect(Collectors.toMap(Entity::getUniqueId, p -> location.distance(p.getLocation())));
        return getPlayer(
                distanceMap.entrySet().stream()
                        .sorted(Map.Entry.comparingByValue())
                        .collect(Collectors.toList())
                        .get(0)
                        .getKey()
        );
    }

    public List<Player> getAlivePlayer() {
        return getJoinedPlayers().stream()
                .filter(player -> !getWPlayer(player.getUniqueId()).isDied())
                .collect(Collectors.toList());
    }

    public void setJoinedPlayers() {
        joinedPlayers = new ArrayList<>(plugin.getServer().getOnlinePlayers())
                .stream()
                .filter(o->wPlayers.containsKey(o.getUniqueId()))
                .collect(Collectors.toList());
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

    public List<Location> getTasksPos() {
        return tasksPos;
    }

    public void setTasksPos(List<Location> tasksPos) {
        this.tasksPos = tasksPos;
        for (int i=0; i<tasksPos.size(); i++) {
            configuration.setLocationData("task" + i, tasksPos.get(i));
        }
        configuration.save();
    }

    public void addTaskPos(int no, Location location) {
        if (tasksPos.size() <= no) {
            this.tasksPos.add(location);
            configuration.setLocationData("task" + ( this.tasksPos.size() - 1 ) , location);
        } else {
            this.tasksPos.set(no, location);
            configuration.setLocationData("task" + no, location);
        }
        configuration.save();
    }

    public ItemStack getItemForKill() {
        final String SWORD_NAME = ChatColor.RED + "KILL";

        ItemMeta itemMeta = itemForKill.getItemMeta();
        itemMeta.setDisplayName(SWORD_NAME);
        itemForKill.setItemMeta(itemMeta);
        return itemForKill;
    }

    public ItemStack getItemForReport() {
        final String COMPASS_NAME = ChatColor.RED + "REPORT";

        ItemMeta itemMeta = itemForReport.getItemMeta();
        itemMeta.setDisplayName(COMPASS_NAME);
        itemForReport.setItemMeta(itemMeta);
        return itemForReport;
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

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
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


    public GuiLogic getGuiLogic() {
        if (guiLogic == null) guiLogic = new GuiLogic(plugin, 3, "投票先を選んでください");
        return guiLogic;
    }

    public VoteBoard getVoteBoard() {
        return voteBoard;
    }

    public TaskBoard getTaskBoard() {
        return taskBoard;
    }

    public Map<UUID, UUID> getVotedPlayers() {
        return votedPlayers;
    }

    public void setVotedPlayers(Map<UUID, UUID> votedPlayers) {
        this.votedPlayers = votedPlayers;
    }

    public List<UUID> getSkippedPlayers() {
        return skippedPlayers;
    }

    public void setSkippedPlayers(List<UUID> skippedPlayers) {
        this.skippedPlayers = skippedPlayers;
    }

    Game(JavaPlugin plugin, LocationConfiguration configuration) {
        this.plugin = plugin;
        this.configuration = configuration;
        this.gameDirector = new GameDirector(plugin);
    }

    // gameStart、meetingStartはコマンドが来たとき呼び出す
    public ErrorJudge gameStart() {

        if (wasStarted) return ErrorJudge.ALREADY_STARTED;

        ErrorJudge resetJudge = allReset();
        if (!resetJudge.equals(ErrorJudge.NONE)) return resetJudge;

        taskManager.setTasks();
        taskManager.taskBarUpdate();
        displayManager.allAddTaskBar();

        currentState = lobbyState;
        currentState.onActive();

        wasStarted = true;

        displayManager.log("ゲームが開始されました");
        return ErrorJudge.NONE;
    }

    public void initialize() {
        if (taskBoard != null) taskBoard.disable();
        if (voteBoard != null) voteBoard.disable();
        if (!cadavers.isEmpty()) resetCadaver();
        reloadConfig();
        resetManagers();
        resetWPlayers();
    }

    private ErrorJudge allReset() {
        //resetManagers();
        resetStates();
        if (!resetWPlayers()) return ErrorJudge.MANAGER_NULL;
        if (!reloadConfig()) return ErrorJudge.CONFIG_NULL;
        if (!setImposters()) return ErrorJudge.WPLAYERS_NULL; //wPlayersリセット後に実行
        return ErrorJudge.NONE;

    }

    private boolean reloadConfig() {

        lobbyPos = configuration.getLocationData("lobby");
        meetingPos = configuration.getLocationData("meeting");
        String task = "task";

        tasksPos = new ArrayList<>();
        for (int i=0; configuration.containKey(task + i); i++) {
            tasksPos.add(configuration.getLocationData(task + i));
        }
        return (meetingPos != null && lobbyPos != null && tasksPos != null);

    }

    private void resetManagers() {

        displayManager = new DisplayManager(this);
        taskManager = new TaskManager(this);

    }

    public void resetScoreboard() {

        voteBoard = new VoteBoard(plugin);
        taskBoard = new TaskBoard(plugin);

    }

    private void resetStates() {

        lobbyState = new LobbyState(plugin, this);
        meetingState = new MeetingState(plugin, this);
        playingState = new PlayingState(plugin, this);
        votingState = new VotingState(plugin, this);

    }

    private Boolean resetWPlayers() {

        if (displayManager == null || taskManager == null) return false;

        joinedPlayers = new ArrayList<>(plugin.getServer().getOnlinePlayers());
        Map<String, Color> colors = new HashMap<>(Colors.values()).entrySet()
                .stream()
                .filter(o -> !colorUuidMap.containsKey(o.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        for (Player player : joinedPlayers) {
            if (addWPlayer(player).equals(ErrorJudge.WPLAYERS_FULL)) break;
            if (!wPlayers.containsKey(player.getUniqueId())) {
                WPlayer wPlayer = new WPlayer(player.getUniqueId());
                this.putWPlayer(wPlayer);

                int random = new Random().nextInt(colors.size());
                Map.Entry<String, Color> color = new ArrayList<>(colors.entrySet()).get(random);
                wPlayer.setColor(color.getKey());
                colors.remove(color.getKey());
                displayManager.resetColorArmor(player);
            }

            displayManager.clearWithoutArmor(player);
        }

        setJoinedPlayers();
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
        }

        return true;
    }

    public ErrorJudge addWPlayer(Player player) {

        if (wPlayers.containsKey(player.getUniqueId())) return ErrorJudge.NONE;
        WPlayer wPlayer = new WPlayer(player.getUniqueId());
        Optional<String> colorOptional = Colors.values().keySet().stream().filter(c1 -> colorUuidMap.keySet().stream().noneMatch(c1::equals)).findFirst();

        if (colorOptional.isPresent()) {
            String colorName = colorOptional.get();
            wPlayer.setColor(colorName);
            colorUuidMap.put(colorName, player.getUniqueId());
            wPlayers.put(player.getUniqueId(), wPlayer);
            displayManager.resetColorArmor(player);
        } else {
            return ErrorJudge.WPLAYERS_FULL;
        }

        return ErrorJudge.NONE;
    }

    public void removePlayer(Player player) {
        if (!wPlayers.containsKey(player.getUniqueId())) return;
        joinedPlayers.remove(player);
        wPlayers.remove(player.getUniqueId());
    }

    public void changePlayerColor(Player player, String colorName) {
        if (!Colors.values().containsKey(colorName)) return;

        WPlayer changer = getWPlayer(player.getUniqueId());

        if (colorUuidMap.containsKey(colorName)) {
            WPlayer swapper = getWPlayer(colorUuidMap.get(colorName));
            colorUuidMap.replace(colorName, player.getUniqueId());
            colorUuidMap.put(changer.getColorName(), swapper.getUuid());

            swapper.setColor(changer.getColorName());
            displayManager.resetColorArmor(getPlayer(swapper.getUuid()));
        } else {
            colorUuidMap.remove(changer.getColorName());
            colorUuidMap.put(colorName, player.getUniqueId());
        }

        changer.setColor(colorName);
        displayManager.resetColorArmor(player);
    }

    public void killPlayer(Player player, Boolean isEjected) {
        WPlayer wPlayer = getWPlayer(player.getUniqueId());

        wPlayer.setDied(true);
        displayManager.invisible(player);
        player.getInventory().clear();
        if (!isEjected) createCadaver(player);
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

    public void placeScapegoat(Player player) { //タスクをしている身代わりを置く
        Scapegoat scapegoat = new Scapegoat(player);
        scapegoats.put(player.getUniqueId(), scapegoat);
    }

    public void removeScapegoat(Player player) { //身代わりを消す
        UUID uuid = player.getUniqueId();
        scapegoats.get(uuid).destroy();
        scapegoats.remove(uuid);
    }

    public void doTask(Player player, int no) {
        if (getTasksPos().size() < no) {
            player.teleport(getTasksPos().get(0));
        } else {
            player.teleport(getTasksPos().get(no));
        }
    }

    public void report(Player reportedPlayer) {
        removeAllCadavers();

        WPlayer reportedWPlayer = getWPlayer(reportedPlayer.getUniqueId());

        reportedWPlayer.setReport(true);
        displayManager.allSendTitle(ChatColor.GREEN + Messages.message("008", reportedPlayer.getDisplayName().toString()));
        displayManager.allMakeSound(Sound.ENTITY_LIGHTNING_THUNDER, SoundCategory.MASTER, (float) 1.0, (float) 1.0);
        changeState(meetingState);
    }

    public void report(Player reportedPlayer, Player diedPlayer) {
        removeAllCadavers();

        displayManager.allSendTitle(ChatColor.BOLD.toString() + ChatColor.RED.toString() + diedPlayer.getDisplayName().toString(), Messages.message("004"));
        displayManager.allSendMessage("007", reportedPlayer.getDisplayName().toString());
        displayManager.allMakeSound(Sound.ENTITY_ZOMBIE_VILLAGER_AMBIENT, SoundCategory.MASTER, (float) 1.0, (float) 0.3);
        changeState(meetingState);
    }

    public void removeAllCadavers() {
        cadavers.values().forEach(Cadaver::removeBlock);
        cadavers.clear();
    }

    public void voteToPlayer(Player voter, Player target) {
        if (currentState != votingState) return;
        WPlayer wPlayer = getWPlayer(voter.getUniqueId());
        if (!wPlayer.wasVoted()) {
            wPlayer.setVoted(true);
            votedPlayers.put(voter.getUniqueId(), target.getUniqueId());
            displayManager.allSendVoteMessage(voter, target);
            voteBoard.voteUpdate(target.getUniqueId());
            confirmVote();
        } else {
            displayManager.sendErrorMessage(voter, "you.alreadyVoted");
        }
    }

    public void voteToSkip(Player voter) {
        if (currentState != votingState) return;
        WPlayer wPlayer = getWPlayer(voter.getUniqueId());
        if (!wPlayer.wasVoted()) {
            wPlayer.setVoted(true);
            skippedPlayers.add(voter.getUniqueId());
            displayManager.allSendSkipMessage(voter);
            voteBoard.voteUpdate();
            confirmVote();
        } else {
            displayManager.sendErrorMessage(voter, "you.alreadyVoted");
        }
    }

    private void confirmVote() {
        if (getAlivePlayer().stream().map(p -> getWPlayer(p.getUniqueId())).allMatch(WPlayer::wasVoted)) {
            resultVote();
        }
    }

    public void resultVote() {

        if (votedPlayers.isEmpty()) {
            voteSkipped();
            return;
        }

        Map<UUID, Integer> votingResult = new HashMap<>();
        votedPlayers.forEach((k, v) -> votingResult.put(v, votingResult.getOrDefault(v, 0) + 1));

        List<Map.Entry<UUID, Integer>> sortedResults = new ArrayList<>(votingResult.entrySet());
        sortedResults.sort((obj1, obj2) -> obj2.getValue().compareTo(obj1.getValue()));

        int sumOfSkip = skippedPlayers.size();
        Map.Entry<UUID, Integer> mostVotes = sortedResults.get(0);

        if (sumOfSkip >= mostVotes.getValue()) {
            voteSkipped();
        } else {
            ejectPlayer(mostVotes.getKey());
        }

        displayManager.allSendGreenMessage("vote.end");
        
    }

    private WinnerJudge confirmTask() {
        int numberOfClue = getWPlayers().size() - getNumberOfImposter();
        int finishedTask = taskManager.getFinishedTask();

        if ((numberOfTasks * numberOfClue) <= finishedTask) {
            return WinnerJudge.CLUE_WIN;
        }
        return WinnerJudge.NONE;
    }

    private WinnerJudge confirmNoOfPlayers() {
        int clueMateRemains = (int) getAlivePlayer().stream().filter(p -> !getWPlayer(p.getUniqueId()).getRole().isImposter()).count();
        int impostorRemains = (int) getAlivePlayer().stream().filter(p -> getWPlayer(p.getUniqueId()).getRole().isImposter()).count();

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
    private final int limit = 3 * 20;

    public void changeScene(GameState state) {
        GameState state1 = state;
        displayManager.allBlindness(limit + (25));

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
        Player player = getPlayer(uuid);
        displayManager.allSendMessage("002", player.getDisplayName().toString());
        displayManager.log(Messages.message("002", player.getDisplayName().toString()));

        killPlayer(player, true);

        getWPlayers().values().forEach(wPlayer -> wPlayer.setVoted(false));
        confirmGame();

        if (wasStarted) {
            changeState(getPlayingState());
        }
    }

    public void voteSkipped() {
        displayManager.allSendMessage("006");
        displayManager.log(Messages.message("006"));

        getWPlayers().values().forEach(wPlayer -> wPlayer.setVoted(false));
        changeState(getPlayingState());
    }

    public void gameStop() {
        if (!wasStarted) return;
        displayManager.log("ゲームが終了しました");
        if (currentState != null) currentState.onEnd();
        currentState = null;
        wasStarted = false;

        joinedPlayers.forEach(p -> {
            displayManager.clearWithoutArmor(p);
            p.setGameMode(GameMode.ADVENTURE);
            p.teleport(getLobbyPos());
        });

        displayManager.setTaskBarVisible(false);
        initialize();
    }
}