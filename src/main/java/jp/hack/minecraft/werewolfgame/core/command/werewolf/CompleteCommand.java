package jp.hack.minecraft.werewolfgame.core.command.werewolf;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.command.CommandManager;
import jp.hack.minecraft.werewolfgame.core.command.CommandMaster;
import jp.hack.minecraft.werewolfgame.util.Messages;
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
        return "werewolf.admin";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        manager.plugin.getLogger().info("completeコマンドが実行されました");
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.error("you.notPlayer"));
            return true;
        }

        Game game = ((GameConfigurator) manager.plugin).getGame();
        Player player = (Player) sender;
        if (!game.wasStarted()) {
            sender.sendMessage(Messages.error("game.notStartYet"));
            return true;
        }
        if(!game.getWPlayers().containsKey(player.getUniqueId())) {
            sender.sendMessage(Messages.error("you.notJoinYet"));
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(Messages.error("command.noArgument", "タスクの数字"));
            return true;
        }

        int no = -1;
        try {
            no = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Messages.error("command.illegalArgument"));
            e.printStackTrace();
            return true;
        }

        game.taskCompleted(player.getUniqueId(), no);

        sender.sendMessage(ChatColor.GREEN + "" + no + "番のタスク状況がtrueに変更されました。");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>(subCommands.keySet());
    }
}
