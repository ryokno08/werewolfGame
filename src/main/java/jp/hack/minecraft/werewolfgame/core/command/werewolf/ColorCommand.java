package jp.hack.minecraft.werewolfgame.core.command.werewolf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.Colors;
import jp.hack.minecraft.werewolfgame.core.command.CommandManager;
import jp.hack.minecraft.werewolfgame.core.command.CommandMaster;
import jp.hack.minecraft.werewolfgame.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ColorCommand extends CommandMaster {

    public ColorCommand(CommandManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "color";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        manager.plugin.getLogger().info("colorコマンドが実行されました");
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.error("you.notPlayer"));
            return true;
        }

        Game game = ((GameConfigurator) manager.plugin).getGame();
        if (game.wasStarted()) {
            sender.sendMessage(Messages.error("game.inTheMiddle"));
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(Messages.error("command.noArgument", "カラー"));
            return true;
        }

        String colorName;
        try {
            colorName = Colors.values().keySet().stream().filter(v->v.equals(args[1])).findFirst().get();
        } catch (NoSuchElementException e) {
            sender.sendMessage(Messages.error("command.illegalArgument"));
            return true;
        }

        Player player = (Player) sender;
        game.changePlayerColor(player, colorName);
        sender.sendMessage(ChatColor.GREEN + "色を変更しました");
        return true;

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // return subCommands.keySet().stream().filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
        return new ArrayList<>(Colors.values().keySet());
    }
}