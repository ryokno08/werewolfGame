package jp.hack.minecraft.werewolfgame.core;

public enum Role {
    CLUE_MATE(false),
    IMPOSTER(true);


    private Boolean isImposter;

    Role(Boolean isImposter) {
        this.isImposter = isImposter;
    }

    public Boolean isImposter() {
        return isImposter;
    }
}
