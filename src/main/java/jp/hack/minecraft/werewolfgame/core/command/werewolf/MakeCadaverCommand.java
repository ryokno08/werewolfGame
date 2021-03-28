package jp.hack.minecraft.werewolfgame.core.command.werewolf;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.command.CommandManager;
import jp.hack.minecraft.werewolfgame.core.command.CommandMaster;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MakeCadaverCommand extends CommandMaster {

    public MakeCadaverCommand(CommandManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "make";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Game game = ((GameConfigurator) manager.plugin).getGame();
        Player player = (Player) sender;

        game.createCadaver(player);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // return subCommands.keySet().stream().filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
        return new ArrayList<>();
    }
}
