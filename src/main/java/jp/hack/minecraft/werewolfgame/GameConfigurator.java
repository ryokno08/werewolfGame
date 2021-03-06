package jp.hack.minecraft.werewolfgame;

import org.bukkit.entity.Player;

import java.util.List;

public interface GameConfigurator {
    Game getGame();
    List<Player> getPlayers();
    void setPlayers(List<Player> players);
}
