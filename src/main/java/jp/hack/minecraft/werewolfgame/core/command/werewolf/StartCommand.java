package jp.hack.minecraft.werewolfgame.core.command.werewolf;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.Main;
import jp.hack.minecraft.werewolfgame.core.command.CommandManager;
import jp.hack.minecraft.werewolfgame.core.command.CommandMaster;
import jp.hack.minecraft.werewolfgame.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.error("you.notPlayer"));
            return true;
        }

        Game game = ((GameConfigurator) manager.plugin).getGame();
        Game.ErrorJudge errorJudge = game.gameStart();
        switch (errorJudge) {
            case ALREADY_STARTED:
                sender.sendMessage(Messages.error("game.alreadyStarted"));
                break;
            case CONFIG_NULL:
                sender.sendMessage(Messages.error("game.noData", "座標"));
                break;
            case MANAGER_NULL:
                sender.sendMessage(Messages.error("game.nullData", "manager"));
                break;
            case WPLAYERS_NULL:
                sender.sendMessage(Messages.error("you.noPlayers"));
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
