package jp.hack.minecraft.werewolfgame.core;

public enum RoleType {
    UNSET(false),
    VILLAGER(false),
    SEER(false),
    PSYCHIC(false),
    HUNTER(false),
    MADMAN(true),
    WOLF(true);

    private Boolean isWolf;

    RoleType(Boolean isWolf) {
        this.isWolf = isWolf;
    }

    public Boolean isWolf() {
        return isWolf;
    }
}
