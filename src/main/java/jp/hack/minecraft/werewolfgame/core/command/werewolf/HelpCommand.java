package jp.hack.minecraft.werewolfgame.core.command.werewolf;

import java.util.ArrayList;
import java.util.List;
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
  public boolean onCommand(
    CommandSender sender,
    Command command,
    String label,
    String[] args
  ) {
    sender.sendMessage("人狼プラグイン ヘルプメニュー");
    sender.sendMessage("/werewolf help : このメニューを開く");
    sender.sendMessage("/werewolf join : 人狼ゲームに参加");
    return true;
  }

  @Override
  public List<String> onTabComplete(
    CommandSender sender,
    Command command,
    String alias,
    String[] args
  ) {
    return new ArrayList<>(subCommands.keySet());
  }
}
