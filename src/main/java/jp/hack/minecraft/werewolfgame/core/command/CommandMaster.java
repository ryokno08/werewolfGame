package jp.hack.minecraft.werewolfgame.core.command;

import com.sun.tools.javac.util.Assert;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CommandMaster {
    protected CommandManager manager;
    public CommandMaster(CommandManager manager){
        this.manager = manager;

        //Nameを実装していない場合はAssertを発生させる
        assert getName() != null;
    }
    protected Map<String, CommandMaster> subCommands = new HashMap<>();
    public abstract String getName();

    public abstract String getPermission();

    protected void addSubCommand(CommandMaster childCommand){
        subCommands.put(childCommand.getName(), childCommand);
    }

    public abstract boolean onCommand(CommandSender sender, Command command, String label, String[] args);

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>(subCommands.keySet());
        // return null;
    }
}
