package jp.hack.minecraft.werewolfgame.core.command;

import jp.hack.minecraft.werewolfgame.core.command.werewolf.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WerewolfCommand extends CommandMaster {
    public WerewolfCommand(CommandManager manager) {
        super(manager);
        addSubCommand(new HelpCommand(this.manager)); // サブコマンドの追加 この場合 /werewolf help を追加したことになる
        addSubCommand(new SetLobbyCommand(this.manager));
        addSubCommand(new BarCommand(this.manager));
        addSubCommand(new CompleteCommand(this.manager));
        addSubCommand(new StartCommand(this.manager));
        addSubCommand(new ReportCommand(this.manager));
        addSubCommand(new VoteCommand(this.manager));
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
        for (String s : args) System.out.println(s);
        if (args.length < 2) return false;
        return subCommands.get(args[1]).onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length <= 1)
            return Stream.of(getName()).filter(s -> s.startsWith(args[0])).collect(Collectors.toList()); // "werewolf"の途中 "wer"など (ここには来ない?)
        if (args.length <= 2)
            return subCommands.keySet().stream().filter(s -> s.startsWith(args[1])).collect(Collectors.toList()); // "werewolf aaa"の途中 "werewolf a"など
        return subCommands.get(args[1]).onTabComplete(sender, command, alias, Arrays.copyOfRange(args, 1, args.length - 1)); // "werewolf aaa bbb"の途中 "werewolf aaa b"など それぞれのコマンドに任せる
    }
}
