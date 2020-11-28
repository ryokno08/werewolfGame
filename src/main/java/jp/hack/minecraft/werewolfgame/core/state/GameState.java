package jp.hack.minecraft.werewolfgame.core.state;

public interface GameState {
    // チャットできるならtrue, できないならfalse
    boolean canSpeak();
    boolean canMove();
}
