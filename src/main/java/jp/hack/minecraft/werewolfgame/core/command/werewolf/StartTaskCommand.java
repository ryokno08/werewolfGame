package jp.hack.minecraft.werewolfgame.core.command.werewolf;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.command.CommandManager;
import jp.hack.minecraft.werewolfgame.core.command.CommandMaster;
import jp.hack.minecraft.werewolfgame.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class StartTaskCommand extends CommandMaster {
    public StartTaskCommand(CommandManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "starttask";
    }

    @Override
    public String getPermission() {
        return "werewolf.admin";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        manager.plugin.getLogger().info(args.toString());
        Game game = ((GameConfigurator) manager.plugin).getGame();
        if (!game.wasStarted()) {
            sender.sendMessage(Messages.error("game.notStartYet"));
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(Messages.error("command.noArgument", "タスクのID"));
            return true;
        } else if (args.length < 3) {
            sender.sendMessage(Messages.error("command.noArgument", "プレイヤー名"));
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
        Player player = manager.plugin.getServer().getPlayer(args[2]);
        if (!game.getWPlayers().containsKey(player.getUniqueId())) {
            sender.sendMessage(Messages.error("you.notJoinYet"));
            return true;
        }

        game.startTask(player, no);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
