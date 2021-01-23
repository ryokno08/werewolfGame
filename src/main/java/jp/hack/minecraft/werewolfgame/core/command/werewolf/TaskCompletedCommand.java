package jp.hack.minecraft.werewolfgame.core.command.werewolf;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.command.CommandManager;
import jp.hack.minecraft.werewolfgame.core.command.CommandMaster;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class TaskCompletedCommand extends CommandMaster {

    public TaskCompletedCommand(CommandManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "taskcomp";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        manager.plugin.getLogger().info("taskcompコマンドが実行されました");

        if (args.length < 1) {
            sender.sendMessage("0");
            return false;
        }

        int num = -1;
        try {
            num = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage("1");
            e.printStackTrace();
            return false;
        }

        Game game = ((GameConfigurator)manager.plugin).getGame();

        sender.sendMessage("2");
        game.taskCompleted(num);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>(subCommands.keySet());
    }
}
