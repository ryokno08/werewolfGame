package jp.hack.minecraft.werewolfgame.core.command.werewolf;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.Main;
import jp.hack.minecraft.werewolfgame.core.command.CommandManager;
import jp.hack.minecraft.werewolfgame.core.command.CommandMaster;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class StartCommand extends CommandMaster {
    public StartCommand(CommandManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "start";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        manager.plugin.getLogger().info("startコマンドが実行されました");
        Game game = ((GameConfigurator) manager.plugin).getGame();

        Game.ErrorJudge errorJudge = game.gameStart();
        switch (errorJudge) {
            case ALREADY_STARTED:
                sender.sendMessage(ChatColor.RED + "すでにゲームはスタートしています");
                break;
            case CONFIG_NULL:
                sender.sendMessage(ChatColor.RED + "座標の設定を行ってください");
                break;
            case MANAGER_NULL:
                sender.sendMessage(ChatColor.RED + "エラーを確認しました：ManagerがNULLです");
                break;
            case WPLAYERS_NULL:
                sender.sendMessage(ChatColor.RED + "エラーを確認しました：WPlayersがNULLです");
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // return subCommands.keySet().stream().filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
        return new ArrayList<>();
    }
}
