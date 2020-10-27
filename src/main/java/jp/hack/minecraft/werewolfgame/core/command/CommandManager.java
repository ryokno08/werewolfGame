package jp.hack.minecraft.werewolfgame.core.command;

import jp.hack.minecraft.werewolfgame.core.utils.I18n;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

public class CommandManager implements TabExecutor {
    public JavaPlugin plugin;
    public CommandManager(JavaPlugin plugin){
        this.plugin = plugin;
    }
    // protected static final Logger LOGGER = Logger.getLogger("WerewolfGame");
    // protected final GamePlugin plugin;

    private Map<String, CommandMaster> rootCommands = new HashMap<>();

    public void addRootCommand(CommandMaster command){
        if(rootCommands.containsKey(command.getName()))
            rootCommands.remove(command.getName());
        rootCommands.put(command.getName(), command);
        plugin.getCommand(command.getName()).setExecutor(this);
        Objects.requireNonNull(plugin.getCommand(command.getName())).setTabCompleter(this);
    }
    /*
    public CommandManager() {
    }

     */
/*
    public ArrayList<String> getSubCommands(String parentName) {
        return new ArrayList<String>(rootCommands.get(parentName).stream().map(subCommand -> subCommand.getName())); // 2020/10/10 19:11 サブコマンドの名前一覧のList<Str>を出したい
    }
*/
/*
    public void addSubCommand(SubCommand subCommand){
        subCommands.put(subCommand.getName(), subCommand);
    }
*/
    private boolean onCommandImpl(CommandSender sender, Command command, String label, String[] args){
        if(rootCommands.containsKey("help")) return rootCommands.get("help").onCommand(sender, command, args[0], Arrays.copyOfRange(args, 1, args.length));
        return false;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || !rootCommands.containsKey(args[0])) {
            return onCommandImpl(sender, command, label, args);
        }

        // SubCommand subCommand = subCommands.get(args[0]);
        CommandMaster rootCommannd = rootCommands.get(args[0]);

        if (rootCommannd.getPermission() != null && !sender.hasPermission(rootCommannd.getPermission())) {
            sender.sendMessage(I18n.tl("error.command.permission"));
            return false;
        }
        return rootCommannd.onCommand(sender, command, args[0], Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> commands = new ArrayList<>(rootCommands.keySet());

        if (args.length == 0 || args[0].length() == 0) {
            return commands;
        } else if (args.length == 1) {
            return commands.stream().filter(s->s.startsWith(args[0])).collect(Collectors.toList());
        } else {
            if(!rootCommands.containsKey(args[0])) return new ArrayList<>();
            return rootCommands.get(args[0]).onTabComplete(sender, command, args[0], Arrays.copyOfRange(args, 1, args.length));
        }
        // return new ArrayList<>();
    }
}
