package jp.hack.minecraft.werewolfgame.core.command.werewolf;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jp.hack.minecraft.werewolfgame.core.command.CommandManager;
import jp.hack.minecraft.werewolfgame.core.command.CommandMaster;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class HelpCommand extends CommandMaster {

    public HelpCommand(CommandManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("人狼プラグイン ヘルプメニュー");
        sender.sendMessage("人狼ゲームにはサーバー接続時に自動的に参加しています。");
        sender.sendMessage("----- 共通コマンド -----");
        sender.sendMessage("/werewolf help : このメニューを開く");
        sender.sendMessage("/werewolf report :　プレイヤーを召集する");
        sender.sendMessage("----- 運営向けコマンド -----");
        sender.sendMessage("/werewolf start : 人狼ゲームを起動");
        sender.sendMessage("/werewolf setlobby : 現在地をロビーの位置に設定");
        sender.sendMessage("/werewolf bar : タスクバーを表示／非表示");
        sender.sendMessage("/werewolf complete <Number> :　Numberのタスクを完了");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // return subCommands.keySet().stream().filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
        return new ArrayList<>();
    }
}
