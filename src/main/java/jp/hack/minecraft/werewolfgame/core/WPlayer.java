package jp.hack.minecraft.werewolfgame.core;

import java.util.UUID;

public class WPlayer {
    private final UUID uuid;
    private RoleType roleType;

    public WPlayer(UUID uuid) {
        this.uuid = uuid;
        roleType = RoleType.UNSET;
    }

    public WPlayer(UUID uuid, RoleType roleType) {
        this.uuid = uuid;
        this.roleType = roleType;
    }

    public UUID getUuid() {
        return uuid;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }
}
