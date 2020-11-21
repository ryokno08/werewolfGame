package jp.hack.minecraft.werewolfgame.core.state;

public class LobbyState implements GameState {
    private static final LobbyState singleton = new LobbyState();
    private LobbyState(){}
    public static LobbyState getInstance() {
        return singleton;
    }

    @Override
    public boolean canSpeak() {
        return false;
    }
}
