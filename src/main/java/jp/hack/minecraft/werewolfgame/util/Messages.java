package jp.hack.minecraft.werewolfgame.util;

import java.util.HashMap;

public class Messages {
    private static Messages _instance = new Messages();
    private HashMap<String, String> messages = new HashMap<>();
    private HashMap<String, String> errors = new HashMap<>();

    {
        messages.put("001", "討論開始");
        messages.put("002", "%sが追放されました");
        messages.put("003", "ゲーム開始まで%d秒");
        errors.put("001", "存在しないコードのメッセージを取得しようとしました");
    }

    public static String message(String code, Object... args) {
        if (_instance.messages.containsKey(code)) return String.format(_instance.messages.get(code), args);
        return error("001");
    }

    public static String error(String code, Object... args) {
        return "ERR" + code + " " + String.format(_instance.errors.get(code), args);
    }
}
