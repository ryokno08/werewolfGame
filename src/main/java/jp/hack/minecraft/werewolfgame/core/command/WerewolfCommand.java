package jp.hack.minecraft.werewolfgame.core.command;

import jp.hack.minecraft.werewolfgame.core.command.werewolf.HelpCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class WerewolfCommand extends CommandMaster {
    public WerewolfCommand(CommandManager manager) {
        super(manager);
        addSubCommand(new HelpCommand(this.manager)); // サブコマンドの追加 この場合 /werewolf help を追加したことになる
    }

    @Override
    public String getName() {
        return "werewolf"; // 変更?
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }
}
