package jp.hack.minecraft.werewolfgame.core;

import org.bukkit.Location;

import java.util.*;

public class Game {
    private final static Game game = new Game();
    public static synchronized Game getInstance() {
        return game;
    }

    private Map<UUID, WPlayer> wPlayers = new HashMap<>();
    private Location respawn;

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
}