package jp.hack.minecraft.werewolfgame.core.state;

public interface GameState {
    // チャットできるならtrue, できないならfalse
    boolean canSpeak();
    // 動けるならtrue, 動けないならfalse
    boolean canMove();

    void update();
}
