package jp.hack.minecraft.werewolfgame.core.command.werewolf;

import jp.hack.minecraft.werewolfgame.core.Game;
import jp.hack.minecraft.werewolfgame.core.command.CommandManager;
import jp.hack.minecraft.werewolfgame.core.command.CommandMaster;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TaskCompletedCommand extends CommandMaster {

    public TaskCompletedCommand(CommandManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "completed";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        manager.plugin.getLogger().info("TaskCompletedコマンドが実行されました");

        Game game = Game.getInstance();

        game.taskCompleted();
        return true;
    }
}
