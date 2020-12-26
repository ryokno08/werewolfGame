package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;

public interface GameState {
    // チャットできるならtrue, できないならfalse
    boolean canSpeak();
    // 動けるならtrue, 動けないならfalse
    boolean canMove();

    void update();

    void onStart(Game game); // Stateがnewされた時

    void onActive(); // このStateに切り替わった時
    void onInactive(); // 違うStateに切り替わった時
    void onEnd(); // Stateが完全に終了した時
}
