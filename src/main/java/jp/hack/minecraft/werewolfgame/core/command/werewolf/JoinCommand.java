package jp.hack.minecraft.werewolfgame.core.command.werewolf;

import jp.hack.minecraft.werewolfgame.core.command.CommandManager;
import jp.hack.minecraft.werewolfgame.core.command.CommandMaster;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class JoinCommand extends CommandMaster {
    public JoinCommand(CommandManager manager) {
        super(manager);
    }
    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        manager.plugin.getLogger().info("joinコマンドが実行されました");
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>(subCommands.keySet());
    }
}
