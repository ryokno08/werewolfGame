package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;

public interface GameState {
    // チャットできるならtrue, できないならfalse
    boolean canSpeak();
    // 動けるならtrue, 動けないならfalse
    boolean canMove();

    void update();

    void init(Game game);

    void active();
    void inactive();
    void end();
}
