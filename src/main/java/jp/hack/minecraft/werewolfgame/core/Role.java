package jp.hack.minecraft.werewolfgame.core;

public enum Role {
    UNSET(false),
    VILLAGER(false),
    SEER(false),
    PSYCHIC(false),
    HUNTER(false),
    MADMAN(true),
    WOLF(true, true);


    private Boolean isWolfSide;
    private Boolean isWolf;


    Role(Boolean isWolfSide) {
        this(isWolfSide, false);
    }

    Role(Boolean isWolfSide, Boolean isWolf) {
        this.isWolfSide = isWolfSide;
        this.isWolf = isWolf;
    }

    public Boolean isWolfSide() {
        return isWolfSide;
    }

    public Boolean isWolf() {
        return isWolf;
    }
}
