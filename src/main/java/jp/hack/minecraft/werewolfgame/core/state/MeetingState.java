package jp.hack.minecraft.werewolfgame.core.state;

public class MeetingState implements GameState {
    private static final MeetingState singleton = new MeetingState();
    private MeetingState(){}
    public static MeetingState getInstance() {
        return singleton;
    }

    @Override
    public boolean canSpeak() {
        return true;
    }

    @Override
    public boolean canMove() {
        return false;
    }

    @Override
    public void update() {

    }
}
