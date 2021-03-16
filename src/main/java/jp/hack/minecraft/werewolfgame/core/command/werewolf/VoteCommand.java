package jp.hack.minecraft.werewolfgame.core.command.werewolf;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.command.CommandManager;
import jp.hack.minecraft.werewolfgame.core.command.CommandMaster;
import jp.hack.minecraft.werewolfgame.util.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

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
        manager.plugin.getLogger().info("voteコマンドが実行されました");
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
        if (args.length <= 1) {
            sender.sendMessage(ChatColor.RED + "引数に、投票先のユーザーネーム、又は\"skip\"を入力してください");
            return true;
        }
        if (args[1].equals("skip")) { // skip投票時
            if (!game.voteSkip(player.getUniqueId())) {
                sender.sendMessage(ChatColor.RED + "投票できません");
                return true;
            }
            sender.sendMessage(ChatColor.GREEN + "スキップに投票しました");
        } else { // プレイヤー投票時
            Player target = manager.plugin.getServer().getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "そのプレイヤーは存在しません");
                return true;
            }
            if (!game.votePlayer(player.getUniqueId(), target.getUniqueId())) {
                sender.sendMessage(ChatColor.RED + "投票できません");
                return true;
            }
            sender.sendMessage(ChatColor.GREEN + target.getDisplayName() + "に投票しました");

        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> returnList = manager.plugin.getServer().getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        returnList.add("skip");
        return returnList;
    }
}
