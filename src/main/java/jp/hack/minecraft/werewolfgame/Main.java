package jp.hack.minecraft.werewolfgame;

import jp.hack.minecraft.werewolfgame.core.ChatManager;
import jp.hack.minecraft.werewolfgame.core.JoinEvent;
import jp.hack.minecraft.werewolfgame.core.command.CommandManager;
import jp.hack.minecraft.werewolfgame.core.command.WerewolfCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private CommandManager commandManager;
    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new ChatManager(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        commandManager = new CommandManager(this);
        commandManager.addRootCommand(new WerewolfCommand(commandManager)); // plugin.ymlへの登録を忘れずに
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
