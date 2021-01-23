package jp.hack.minecraft.werewolfgame.core.command.werewolf;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.command.CommandManager;
import jp.hack.minecraft.werewolfgame.core.command.CommandMaster;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

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

        for(String arg : args) System.out.println(arg);
        System.out.println(args.length);

        if (args.length < 2) {
            return false;
        }

        int num = -1;
        try {
            num = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }

        Game game = ((GameConfigurator) manager.plugin).getGame();

        game.taskCompleted(num);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>(subCommands.keySet());
    }
}
