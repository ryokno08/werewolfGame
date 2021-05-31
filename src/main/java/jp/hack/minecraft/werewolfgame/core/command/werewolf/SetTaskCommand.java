package jp.hack.minecraft.werewolfgame.core.command.werewolf;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.WPlayer;
import jp.hack.minecraft.werewolfgame.core.command.CommandManager;
import jp.hack.minecraft.werewolfgame.core.command.CommandMaster;
import jp.hack.minecraft.werewolfgame.core.display.DisplayManager;
import jp.hack.minecraft.werewolfgame.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetTaskCommand extends CommandMaster {

    public SetTaskCommand(CommandManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "settask";
    }

    @Override
    public String getPermission() {
        return "werewolf.admin";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        manager.plugin.getLogger().info("settaskコマンドが実行されました");
        Game game = ((GameConfigurator) manager.plugin).getGame();
        DisplayManager displayManager = game.getDisplayManager();
        if (game.wasStarted()) {
            displayManager.sendErrorMessage(sender, "game.inTheMiddle");
            return true;
        }
        if (!(sender instanceof Player)) {
            displayManager.sendErrorMessage(sender, "you.notPlayer");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 2) {
            displayManager.sendErrorMessage(player, "command.noArgument", "タスクの数字");
            return true;
        }

        int no = -1;
        try {
            no = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Messages.error("command.illegalArgument"));
            return true;
        }

        game.addTaskPos(no, player.getLocation());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>(subCommands.keySet());
    }
}

