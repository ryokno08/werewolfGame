package jp.hack.minecraft.werewolfgame;

import jp.hack.minecraft.werewolfgame.core.ChatManager;
import jp.hack.minecraft.werewolfgame.core.command.CommandManager;
import jp.hack.minecraft.werewolfgame.core.command.WerewolfCommand;
import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private CommandManager commandManager;
    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new ChatManager(), this);
        commandManager = new CommandManager(this);
        commandManager.addRootCommand(new WerewolfCommand(commandManager));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
