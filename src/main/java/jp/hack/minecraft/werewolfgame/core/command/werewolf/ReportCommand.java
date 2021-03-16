package jp.hack.minecraft.werewolfgame.core.command.werewolf;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.WPlayer;
import jp.hack.minecraft.werewolfgame.core.command.CommandManager;
import jp.hack.minecraft.werewolfgame.core.command.CommandMaster;
import jp.hack.minecraft.werewolfgame.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ReportCommand extends CommandMaster {
    public ReportCommand(CommandManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "report";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        manager.plugin.getLogger().info("reportコマンドが実行されました");
        if (!(sender instanceof Player)) {
            sender.sendMessage(Messages.error("you.notPlayer"));
            return true;
        }

        Game game = ((GameConfigurator) manager.plugin).getGame();
        Player player = (Player) sender;
        if (!game.wasStarted()) {
            sender.sendMessage(Messages.error("game.notStartYet"));
            return true;
        }
        if(!game.getWPlayers().containsKey(player.getUniqueId())) {
            sender.sendMessage(Messages.error("you.notJoinYet"));
            return true;
        }
        WPlayer wPlayer = game.getWPlayer(player.getUniqueId());

        if (wPlayer.getReport()) {
            sender.sendMessage(Messages.error("you.alreadyReported"));
            return true;
        } else {
            sender.sendMessage(ChatColor.LIGHT_PURPLE+"＊リポートを消費しました");
            wPlayer.setReport(true);
            game.changeState(game.getMeetingState());
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>();
    }
}
