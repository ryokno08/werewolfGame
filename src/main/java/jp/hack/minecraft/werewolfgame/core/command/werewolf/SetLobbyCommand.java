package jp.hack.minecraft.werewolfgame.core.command.werewolf;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.GameConfigurator;
import jp.hack.minecraft.werewolfgame.core.command.CommandManager;
import jp.hack.minecraft.werewolfgame.core.command.CommandMaster;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SetLobbyCommand extends CommandMaster {
    public SetLobbyCommand(CommandManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "setlobby";
    }

    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("SetLobbyコマンドが実行されました");
        if(sender instanceof Player) {
            sender.sendMessage("あなたはプレイヤーではないため実行できません。早くサーバーに入って");
            return true;
        }
            Player player = (Player) sender;


        Game game = ((GameConfigurator) manager.plugin).getGame();
        game.setLobbyPos(player.getLocation());
        sender.sendMessage("ロビーの座標が保存されました");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // return subCommands.keySet().stream().filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
        return new ArrayList<>();
    }
}
