package jp.hack.minecraft.werewolfgame;

import jp.hack.minecraft.werewolfgame.core.ChatManager;
import jp.hack.minecraft.werewolfgame.core.JoinEvent;
import jp.hack.minecraft.werewolfgame.core.command.CommandManager;
import jp.hack.minecraft.werewolfgame.core.command.WerewolfCommand;
import jp.hack.minecraft.werewolfgame.logic.GameEventManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private CommandManager commandManager;
    private Game game;
    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new ChatManager(), this);
        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        getServer().getPluginManager().registerEvents(new GameEventManager(this), this); // イベントはここに統一
        commandManager = new CommandManager(this);
        commandManager.addRootCommand(new WerewolfCommand(commandManager)); // plugin.ymlへの登録を忘れずに

        game = new Game(this);
        game.runTaskTimer(this, 10, 20);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Game getGame() {
        return game;
    }
}
