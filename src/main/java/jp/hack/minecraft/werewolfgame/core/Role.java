package jp.hack.minecraft.werewolfgame.core;

public enum Role {
    CLUEMATE(false),
    IMPOSTER(true, true);


    private Boolean isImposter;


    Role(Boolean isImposterSide) {
        this(isImposterSide, false);
    }

    Role(Boolean isImposterSide, Boolean isImposter) {
        this.isImposter = isImposter;
    }

    public Boolean isImposter() {
        return isImposter;
    }
}
