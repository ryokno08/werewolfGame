package jp.hack.minecraft.werewolfgame.core;

public enum TimeState {
    WAITING(false),
    MEETING(true),
    VOTING(true);

    private Boolean canTalk;

    TimeState(Boolean canTalk) {
        this.canTalk = canTalk;
    }

    public Boolean canTalk() {
        return canTalk;
    }
}
