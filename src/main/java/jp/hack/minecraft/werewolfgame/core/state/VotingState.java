package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class VotingState extends GameState {
    private BukkitTask task;

    public VotingState(JavaPlugin plugin, Game game) {
        super(plugin, game);
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
    public void onActive() {
        super.onActive();
        // ((GameConfigurator)plugin).getGame().nextStates.add(playingState);
        plugin.getLogger().info("VotingStateがアクティブになりました");

        // 設定などからロードする、単位は秒
        int limitOfVoting = game.getLimitOfVoting();

        game.getJoinedPlayers().forEach(player -> player.sendTitle("投票開始", "", 10, 20, 10));
        if (task == null) {
            task = new BukkitRunnable() {
                int counter = 0;

                @Override
                public void run() {
                    counter++;
                    if (game.votedPlayers.size() >= game.getJoinedPlayers().size()) {
                        stopVote();
                    }
                    if (counter < limitOfVoting) {
                        if (counter < 20) {
                            game.getJoinedPlayers().forEach(player -> player.sendMessage("投票終了まで" + (limitOfVoting - counter) + "秒"));
                        }
                    } else {
//                        for(Player p : plugin.getServer().getOnlinePlayers()){
//                            if(!game.votedPlayers.containsKey(p.getUniqueId())){
//                                game.votedPlayers.put(p.getUniqueId(), "Skip");
//                            }
//                        }
                        stopVote();
                    }
                }

                private void stopVote() {
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
                                game.ejectPlayer(UUID.fromString(SortedResults.get(0).getKey()));
                            }
                        } else if (!SortedResults.get(0).getValue().equals(SortedResults.get(1).getValue())) {
                            if (SortedResults.get(0).getKey().equals("Skip")) {
                                game.voteSkipped();
                            } else {
                                game.ejectPlayer(UUID.fromString(SortedResults.get(0).getKey()));
                            }
                        }
                    }


                    game.changeState(game.getPlayingState());
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
