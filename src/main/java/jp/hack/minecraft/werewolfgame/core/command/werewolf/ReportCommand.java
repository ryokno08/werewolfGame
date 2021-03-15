package jp.hack.minecraft.werewolfgame.core.command.werewolf;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.WPlayer;
import jp.hack.minecraft.werewolfgame.core.command.CommandManager;
import jp.hack.minecraft.werewolfgame.core.command.CommandMaster;
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
        Player player = (Player) sender;
        Game game = ((GameConfigurator) manager.plugin).getGame();

        if(!game.getWPlayers().containsKey(player.getUniqueId())) {
            sender.sendMessage(ChatColor.RED+"あなたはゲームに参加していないため、実行できません");
        }
        WPlayer wPlayer = game.getWPlayer(player.getUniqueId());

        if (wPlayer.getReport()) {
            sender.sendMessage(ChatColor.RED+"すでにリポートを消費したため招集できません");
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
