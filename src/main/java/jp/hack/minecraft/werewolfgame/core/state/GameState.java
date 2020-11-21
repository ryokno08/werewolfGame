package jp.hack.minecraft.werewolfgame.core.state;

public interface GameState {
    // チャットできるならtrue, できないならfalse
    public abstract boolean canSpeak();
}
