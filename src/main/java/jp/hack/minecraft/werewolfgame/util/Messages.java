package jp.hack.minecraft.werewolfgame.util;

import org.bukkit.ChatColor;

import java.util.HashMap;

public class Messages {
    private static Messages _instance = new Messages();
    private HashMap<String, String> messages = new HashMap<>();
    private HashMap<String, String> errors = new HashMap<>();

    {
        messages.put("001", "議論開始");
        messages.put("002", "%sが追放されました");
        messages.put("003", "ゲーム開始まで%d秒");
        messages.put("004", "の死体が発見されました");
        messages.put("005", "あなたは%sです");
        messages.put("006", "誰も追放されませんでした");
        messages.put("007", "死体の発見者：%s");
        messages.put("008", "リポートされました");
        messages.put("009", "%d番目のタスク状況をtrueに変更しました");

        messages.put("you.fakeTask", "タスクをするふりをしています...");
        messages.put("you.doTask", "タスクをしてください");

        messages.put("you.voted", "%sに投票しました");
        messages.put("you.skipped", "スキップに投票しました");
        messages.put("other.voted", "%sが%sに投票しました");
        messages.put("other.skipped", "%sがスキップに投票しました");
        messages.put("vote.end", "投票が終わりました");

        errors.put("you.notPlayer", "あなたはプレイヤーではないため実行できません。早くサーバーに入って！");
        errors.put("you.notJoinYet", "あなたはゲームに参加していないため、実行できません");
        errors.put("you.notClueMate", "あなたはクルーメイトではないため、タスクはできません");
        errors.put("you.alreadyReported", "すでにリポートを消費しているため、リポートできません");
        errors.put("you.alreadyVoted", "すでに投票をしているため、投票できません");
        errors.put("you.cannotCommandNow", "このコマンドは現在実行できません");
        errors.put("you.disturbImposter", "インポスターの邪魔になっています！速やかに退去してください");

        errors.put("game.notStartYet", "まだゲームは始まっていません");
        errors.put("game.alreadyStarted", "すでにゲームは始まっています");
        errors.put("game.inTheMiddle", "ゲーム中は設定できません");
        errors.put("game.nullData", "%sがNULLです");
        errors.put("game.noData", "%sの設定がありません");
        errors.put("game.noPlayers", "プレイヤーがいません");
        errors.put("game.fullPlayers", "プレイヤーが満員です");

        errors.put("command.noArgument","%sが入力されていません");
        errors.put("command.illegalArgument","正しい引数を入力してください");
        errors.put("command.undefinedTask", "存在しないタスクです");

        errors.put("001", "存在しないコードのメッセージを取得しようとしました");
    }

    public static String message(String code, Object... args) {
        if (_instance.messages.containsKey(code)) return ChatColor.GREEN + String.format(_instance.messages.get(code), args);
        return error("001");
    }

    public static String error(String code, Object... args) {
        return ChatColor.RED + String.format(_instance.errors.get(code), args);
    }
}
