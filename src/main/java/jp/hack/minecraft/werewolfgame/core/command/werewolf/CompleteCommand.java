package jp.hack.minecraft.werewolfgame.core.command.werewolf;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.command.CommandManager;
import jp.hack.minecraft.werewolfgame.core.command.CommandMaster;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CompleteCommand extends CommandMaster {

    public CompleteCommand(CommandManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "complete";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        manager.plugin.getLogger().info("completeコマンドが実行されました");

        Player player = (Player) sender;

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED +"終わったタスクの数字が入力されていません。");
            return false;
        }

        int no = -1;
        try {
            no = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED+"正しい数字が入力されていません。");
            e.printStackTrace();
            return false;
        }

        Game game = ((GameConfigurator) manager.plugin).getGame();
        game.taskCompleted(player, no);

        sender.sendMessage(ChatColor.GREEN + "" + no + "番のタスク状況がtrueに変更されました。");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>(subCommands.keySet());
    }
}
