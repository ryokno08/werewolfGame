package jp.hack.minecraft.werewolfgame.util;

import org.bukkit.ChatColor;

import java.util.HashMap;

public class Messages {
    private static Messages _instance = new Messages();
    private HashMap<String, String> messages = new HashMap<>();
    private HashMap<String, String> errors = new HashMap<>();

    {
        messages.put("001", "討論開始");
        messages.put("002", "%sが追放されました");
        messages.put("003", "ゲーム開始まで%d秒");
        messages.put("004", "%sの死体が発見されました");
        messages.put("005", "あなたは%sです");
        messages.put("006", "誰も追放されませんでした");

        errors.put("you.notPlayer", "あなたはプレイヤーではないため実行できません。早くサーバーに入って");
        errors.put("you.notJoinYet", "あなたはゲームに参加していないため、実行できません");
        errors.put("you.notClueMate", "あんたはクルーメイトではありません。タスクはできないよ");
        errors.put("you.alreadyReported", "リポートは一回のみすることができます");

        errors.put("game.notStartYet", "まだゲームは始まっていません");
        errors.put("game.alreadyStarted", "すでにゲームは始まっています");
        errors.put("game.inTheMiddle", "ゲーム中は設定できません");
        errors.put("game.nullData", "%sがNULLです");
        errors.put("game.noData", "%sの設定がありません");
        errors.put("game.noPlayers", "プレイヤーがいません");

        errors.put("command.noArgument","%sが入力されていません");
        errors.put("command.illegalArgument","正しい引数を入力してください");

        errors.put("001", "存在しないコードのメッセージを取得しようとしました");
    }

    public static String message(String code, Object... args) {
        if (_instance.messages.containsKey(code)) return String.format(_instance.messages.get(code), args);
        return error("001");
    }

    public static String error(String code, Object... args) {
        return ChatColor.RED + String.format(_instance.errors.get(code), args);
    }
}
