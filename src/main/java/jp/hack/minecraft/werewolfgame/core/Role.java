package jp.hack.minecraft.werewolfgame.core;

public enum Role {
    UNSET(false),
    VILLAGER(false),
    SEER(false),
    PSYCHIC(false),
    HUNTER(false),
    MADMAN(true),
    WOLF(true);

    private Boolean isWolf;

    Role(Boolean isWolf) {
        this.isWolf = isWolf;
    }

    public Boolean isWolf() {
        return isWolf;
    }
}
