package jp.hack.minecraft.werewolfgame.core;

import java.util.UUID;

public class WPlayer {
    private final UUID uuid;
    private Role role;


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
}
