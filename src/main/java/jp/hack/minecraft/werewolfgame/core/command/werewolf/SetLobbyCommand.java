package jp.hack.minecraft.werewolfgame.core.command.werewolf;

import jp.hack.minecraft.werewolfgame.core.command.CommandManager;
import jp.hack.minecraft.werewolfgame.core.command.CommandMaster;
import jp.hack.minecraft.werewolfgame.logic.GameDirector;
import jp.hack.minecraft.werewolfgame.logic.WerewolfLogic;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetLobbyCommand extends CommandMaster {
    public SetLobbyCommand(CommandManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "setLobby";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("SetLobbyコマンドが実行されました");
        Player player = (Player) sender;
        manager.plugin.getLogger().info(player.toString());
        GameDirector.lobbyLocation = player.getLocation();
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>(subCommands.keySet());
    }
}
