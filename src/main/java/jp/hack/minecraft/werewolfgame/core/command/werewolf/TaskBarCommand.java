package jp.hack.minecraft.werewolfgame.core.command.werewolf;

import jp.hack.minecraft.werewolfgame.core.Game;
import jp.hack.minecraft.werewolfgame.core.display.DisplayManager;
import jp.hack.minecraft.werewolfgame.core.command.CommandManager;
import jp.hack.minecraft.werewolfgame.core.command.CommandMaster;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TaskBarCommand extends CommandMaster {

    public TaskBarCommand(CommandManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "task";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        manager.plugin.getLogger().info("TaskBarコマンドが実行されました");

        Game game = Game.getInstance();
        DisplayManager displayManager = game.getDisplayManager();

        displayManager.setTaskBarVisible( !displayManager.isTaskBarVisible() );
        return true;
    }
}
