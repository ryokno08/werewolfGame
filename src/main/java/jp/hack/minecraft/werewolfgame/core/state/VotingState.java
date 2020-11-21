package jp.hack.minecraft.werewolfgame.core.state;

public class VotingState implements GameState {
    private static final VotingState singleton = new VotingState();
    private VotingState(){}
    public static VotingState getInstance() {
        return singleton;
    }

    @Override
    public boolean canSpeak() {
        return true;
    }
}
