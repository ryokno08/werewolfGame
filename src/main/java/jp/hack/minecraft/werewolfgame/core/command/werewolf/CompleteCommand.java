package jp.hack.minecraft.werewolfgame.core.command.werewolf;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.WPlayer;
import jp.hack.minecraft.werewolfgame.core.command.CommandManager;
import jp.hack.minecraft.werewolfgame.core.command.CommandMaster;
import jp.hack.minecraft.werewolfgame.core.display.DisplayManager;
import jp.hack.minecraft.werewolfgame.util.Messages;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

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
        Game game = ((GameConfigurator) manager.plugin).getGame();
        DisplayManager displayManager = game.getDisplayManager();
        if (!game.wasStarted()) {
            displayManager.sendErrorMessage(sender, "game.notStartYet");
            return true;
        }

        Player player;

        if (!(sender instanceof Player)) {
            if (sender instanceof BlockCommandSender) {
                Location commandBlockLocation = ((BlockCommandSender) sender).getBlock().getLocation();
                player = game.getNearPlayer(commandBlockLocation);
                if (4 <= commandBlockLocation.distance(player.getLocation())) {
                    displayManager.sendErrorMessage(player, "you.tooFarFromButton");
                    return true;
                }
            } else {
                displayManager.sendErrorMessage(sender, "you.notPlayer");
                return true;
            }
        } else {
            player = (Player) sender;
        }
        WPlayer wPlayer = game.getWPlayer(player.getUniqueId());
        if(!game.getWPlayers().containsKey(wPlayer.getUuid())) {
            displayManager.sendErrorMessage(player, "you.notJoinYet");
            return true;
        }
        if (args.length < 2) {
            displayManager.sendErrorMessage(player, "command.noArgument", "タスクの数字");
            return true;
        }
        if (wPlayer.isImposter()) {
            displayManager.sendErrorMessage(player, "you.notClueMate");
            return true;
        }

        int no = -1;
        try {
            no = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            sender.sendMessage(Messages.error("command.illegalArgument"));
            return true;
        }
        if (no > game.getTasks().size() - 1 || no < 0) {
            displayManager.sendErrorMessage(player, "command.undefinedTask");
            return true;
        }

        game.taskCompleted(player, no);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>(subCommands.keySet());
    }
}
