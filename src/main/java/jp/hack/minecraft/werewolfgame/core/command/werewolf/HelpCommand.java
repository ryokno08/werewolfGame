package jp.hack.minecraft.werewolfgame.core.command.werewolf;

import jp.hack.minecraft.werewolfgame.core.command.CommandManager;
import jp.hack.minecraft.werewolfgame.core.command.CommandMaster;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class HelpCommand extends CommandMaster {
    public HelpCommand(CommandManager manager) {
        super(manager);
    }
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        manager.plugin.getLogger().info("人狼プラグイン ヘルプメニュー");
        manager.plugin.getLogger().info("/werewolf help : このメニューを開く");
        manager.plugin.getLogger().info("/werewolf join : 人狼ゲームに参加");
        return true;
    }
}
