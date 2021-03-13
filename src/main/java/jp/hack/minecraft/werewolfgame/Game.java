package jp.hack.minecraft.werewolfgame;

import jp.hack.minecraft.werewolfgame.core.Role;
import jp.hack.minecraft.werewolfgame.core.task.TaskManager;
import jp.hack.minecraft.werewolfgame.core.WPlayer;
import jp.hack.minecraft.werewolfgame.core.display.DisplayManager;
import jp.hack.minecraft.werewolfgame.core.state.*;
import jp.hack.minecraft.werewolfgame.util.LocationConfiguration;
import org.bukkit.Bukkit;
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
import org.bukkit.scheduler.BukkitTask;

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
        if (lobbyPos == null) lobbyPos = configuration.getLocationData("lobbyPos");
        return lobbyPos;
    }

    public void setLobbyPos(Location lobbyPos) {
        configuration.setLocationData("lobbyPos", lobbyPos);
        this.lobbyPos = lobbyPos;
    }

    public Location getMeetingPos() {
        if (meetingPos == null) meetingPos = configuration.getLocationData("meetingPos");
        return meetingPos;
    }

    public void setMeetingPos(Location meetingPos) {
        configuration.setLocationData("meetingPos", meetingPos);
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
    public Boolean gameStart() {

        if (wasStarted) return false;
        wasStarted = true;

        resetManagers();
        resetStates();
        resetWPlayers(); //Managerリセット後に実行
        setImposters(); //wPlayersリセット後に実行

        displayManager.setTaskBarVisible(false);

        currentState = lobbyState;
        currentState.onActive();

        return true;
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

    private void resetWPlayers() {

        wPlayers.clear();
        setJoinedPlayers(new ArrayList<>(plugin.getServer().getOnlinePlayers()));

        for (Player player : joinedPlayers) {
            WPlayer wPlayer = new WPlayer(player.getUniqueId());
            this.putWPlayer(wPlayer);

            player.getInventory().clear();

            displayManager.addTaskBar(player);
            taskManager.setTasks(wPlayer);
        }
    }

    private void setImposters() {

        List<Player> players = new ArrayList<>(this.joinedPlayers);

        Random random = new Random();
        for (int i = 0; i < numberOfImposter; i++) {
            Player selectedPlayer = players.get(random.nextInt(players.size()));
            players.remove(selectedPlayer);

            WPlayer wPlayer = getWPlayer(selectedPlayer.getUniqueId());
            wPlayer.setRole(Role.IMPOSTER);
        }

    }

    public void changeState(GameState state) {
        if (currentState == state) return;
        currentState.onInactive();
        currentState = state;
        if (currentState == lobbyState || currentState == playingState) {
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

    private GameJudge confirmTask() {
        int finishedTask = taskManager.getFinishedTask();

        if(numberOfTasks == finishedTask) {
            return GameJudge.CLUE_WIN;
        }
        return GameJudge.NONE;
    }

    private GameJudge confirmNoOfPlayers() {
        int clueMateRemains = wPlayers.values().stream().filter(p -> !p.getRole().isImposter() && !p.isDied()).collect(Collectors.toSet()).size();
        int impostorRemains = wPlayers.values().stream().filter(p -> p.getRole().isImposter() && !p.isDied()).collect(Collectors.toSet()).size();

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

    private BukkitTask task;
    private int counter = 0;
    private final int limit = 3;

    public void changeScene() {
        this.joinedPlayers.forEach(p->{
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, limit*20, 100));
        });
        task = new MyRunTask().runTask(plugin);
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

    class MyRunTask extends BukkitRunnable {

        @Override
        public void run() {
            System.out.println(counter);
            if (counter >= limit) {
                currentState.onActive();
                return;
            }
            counter++;
            task = new MyRunTask().runTaskLater(plugin, 20);
        }
    }
}