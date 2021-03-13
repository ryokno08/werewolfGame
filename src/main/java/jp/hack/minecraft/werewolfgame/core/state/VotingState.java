package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class VotingState extends GameState {
    private final JavaPlugin plugin;
    private BukkitTask task;

    public VotingState(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean canSpeak() {
        return true;
    }

    @Override
    public boolean canMove() {
        return false;
    }

    @Override
    public void update() {

    }

    @Override
    public void onStart(Game game) {
        super.onStart(game);

    }

    @Override
    public void onActive() {
        super.onActive();
        // ((GameConfigurator)plugin).getGame().nextStates.add(playingState);
        plugin.getLogger().info("VotingStateがアクティブになりました");

        // 設定などからロードする、単位は秒
        final int voteLength = 60;

        plugin.getServer().getOnlinePlayers().forEach(player -> player.sendTitle("投票開始", "", 10, 20, 10));
        if (task == null) {
            task = new BukkitRunnable() {
                int counter = 0;

                @Override
                public void run() {
                    counter++;
                    Game game = ((GameConfigurator) plugin).getGame();
                    if (game.votedPlayers.size() >= plugin.getServer().getOnlinePlayers().size()) {
                        stopVote(game);
                    }
                    if (counter < voteLength) {
                        if (counter < 20) {
                            plugin.getServer().getOnlinePlayers().forEach(player -> player.sendMessage("投票終了まで" + (voteLength - counter) + "秒"));
                        }
                    } else {
//                        for(Player p : plugin.getServer().getOnlinePlayers()){
//                            if(!game.votedPlayers.containsKey(p.getUniqueId())){
//                                game.votedPlayers.put(p.getUniqueId(), "Skip");
//                            }
//                        }
                        stopVote(game);
                    }
                }

                private void stopVote(Game game) {
                    Map<String, Integer> VotingResult = new HashMap<>();
                    game.votedPlayers.forEach((k, v) -> VotingResult.put(v, VotingResult.getOrDefault(v, 0) + 1));

                    List<Map.Entry<String, Integer>> SortedResults = new ArrayList<>(VotingResult.entrySet());
                    SortedResults.sort((obj1, obj2) -> obj2.getValue().compareTo(obj1.getValue()));

                    if (VotingResult.getOrDefault("Skip", 0)
                            > plugin.getServer().getOnlinePlayers().size() / 2) {
                        if (SortedResults.size() < 2) {
                            if (SortedResults.get(0).getKey().equals("Skip")) {
                                game.voteSkipped();
                            } else {
                                game.ejectPlayer(SortedResults.get(0).getKey());
                            }
                        } else if (!SortedResults.get(0).getValue().equals(SortedResults.get(1).getValue())) {
                            if (SortedResults.get(0).getKey().equals("Skip")) {
                                game.voteSkipped();
                            } else {
                                game.ejectPlayer(SortedResults.get(0).getKey());
                            }
                        }
                    }


                    game.returnToPlay();
                    this.cancel();
                }
            }.runTaskTimer(plugin, 0, 20);
        }
    }

    @Override
    public void onInactive() {
        super.onInactive();
    }

    @Override
    public void onEnd() {
        super.onEnd();
    }
}
