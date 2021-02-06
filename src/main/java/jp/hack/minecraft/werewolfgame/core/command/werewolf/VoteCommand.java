package jp.hack.minecraft.werewolfgame.core.command.werewolf;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.command.CommandManager;
import jp.hack.minecraft.werewolfgame.core.command.CommandMaster;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VoteCommand extends CommandMaster {
    public VoteCommand(CommandManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "vote";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Game game = ((GameConfigurator) manager.plugin).getGame();
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        if (args.length <= 1) {
            sender.sendMessage("引数に、投票先のユーザーネーム、又は\"skip\"を入力してください");
            return true;
        }
        if (args[1].equals("skip")){
            if(!game.voteSkip(player.getUniqueId())){
                sender.sendMessage("投票できません");
                return true;
            }
        }
        Player target = manager.plugin.getServer().getPlayer(args[1]);
        if(target == null) {
            sender.sendMessage("そのプレイヤーは存在しません");
            return true;
        }
        game.votePlayer(player.getUniqueId(), target.getUniqueId());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> returnList = manager.plugin.getServer().getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        returnList.add("skip");
        return returnList;
    }
}
