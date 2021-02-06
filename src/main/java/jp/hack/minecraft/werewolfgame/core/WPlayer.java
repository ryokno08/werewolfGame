package jp.hack.minecraft.werewolfgame.core;

import java.util.UUID;

public class WPlayer {
    private final UUID uuid;
    private Role role;
    private Boolean killing = false;
    private Boolean died = false;


    public WPlayer(UUID uuid) {
        this(uuid, Role.UNSET);
    }

    public WPlayer(UUID uuid, Role role) {
        this.uuid = uuid;
        this.role = role;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Boolean isKilling() {
        return killing;
    }

    public void setKilling(Boolean killing) {
        this.killing = killing;
    }

    public Boolean isDied() {
        return died;
    }

    public void setDied(Boolean died) {
        this.died = died;
    }
}
