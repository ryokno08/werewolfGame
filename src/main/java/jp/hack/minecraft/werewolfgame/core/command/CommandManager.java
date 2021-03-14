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

    public CommandManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    // protected static final Logger LOGGER = Logger.getLogger("WerewolfGame");
    // protected final GamePlugin plugin;

    private Map<String, CommandMaster> rootCommands = new HashMap<>();

    public void addRootCommand(CommandMaster command) {
        if (rootCommands.containsKey(command.getName()))
            rootCommands.remove(command.getName());
        rootCommands.put(command.getName(), command);
        plugin.getCommand(command.getName()).setExecutor(this);
        Objects.requireNonNull(plugin.getCommand(command.getName())).setTabCompleter(this);
    }

    private boolean onCommandImpl(CommandSender sender, Command command, String label, String[] args) {
        if (rootCommands.get(command.getName()).subCommands.containsKey("help")) {
            return rootCommands.get(command.getName()).subCommands.get("help").onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
        }
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> compiledArgs = new ArrayList<>(Arrays.asList(args));
        compiledArgs.add(0, label);
        args = compiledArgs.toArray(new String[compiledArgs.size()]);
        /*if (args.length == 0 || !rootCommands.containsKey(args[0])) {
            return onCommandImpl(sender, command, label, args);
        }

         */

        // if(args.length <= 1){
        CommandMaster rootCommannd = rootCommands.get(command.getName());
        // rootCommannd.onCommand(sender, command, label, args);
        // }

        // SubCommand subCommand = subCommands.get(args[0]);
/*
        if (rootCommannd.getPermission() != null && !sender.hasPermission(rootCommannd.getPermission())) {
            sender.sendMessage(I18n.tl("error.command.permission"));
            return false;
        }
        */
        if (!rootCommannd.onCommand(sender, command, label, args)) return onCommandImpl(sender, command, label, args);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> compiledArgs = new ArrayList<>(Arrays.asList(args));
        compiledArgs.add(0, alias);
        args = compiledArgs.toArray(new String[compiledArgs.size()]);


        List<String> commands = new ArrayList<>(rootCommands.keySet());

        // plugin.getLogger().info("args:" + Arrays.toString(args));
        // plugin.getLogger().info("args.length:" + args.length);

        /*if (args.length == 0 || args[0].length() == 0) {
            return commands;
        } else if (args.length == 1) {
            return commands.stream().filter(s->s.startsWith(args[0])).collect(Collectors.toList());
        } else {*/
        // if(!rootCommands.containsKey(args[0])) return new ArrayList<>();
        return rootCommands.get(command.getName()).onTabComplete(sender, command, alias, args);
        // }
        // return new ArrayList<>();
    }
}
