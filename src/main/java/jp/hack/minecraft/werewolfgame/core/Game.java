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
    private Boolean canTalk = true;
    private Boolean canCommunicate = false;

    public Map<UUID, WPlayer> getwPlayers() {
        return wPlayers;
    }

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

    public Role getRole(UUID uuid) {
        return getWPlayer(uuid).getRole();
    }

    public Boolean setRole(UUID uuid, Role role) {
        if (!getwPlayers().containsKey(uuid)) return false;

        getWPlayer(uuid).setRole(role);
        return true;
    }

    public Boolean canTalk() {
        return canTalk;
    }

    public void setCanTalk(Boolean canTalk) {
        this.canTalk = canTalk;
    }

    public Boolean canCommunicate() {
        return canCommunicate;
    }

    public void setCanCommunicate(Boolean canCommunicate) {
        this.canCommunicate = canCommunicate;
    }
}