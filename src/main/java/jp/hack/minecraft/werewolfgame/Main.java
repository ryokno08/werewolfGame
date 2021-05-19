package jp.hack.minecraft.werewolfgame;

import jp.hack.minecraft.werewolfgame.core.command.CommandManager;
import jp.hack.minecraft.werewolfgame.core.command.WerewolfCommand;
import jp.hack.minecraft.werewolfgame.logic.GameEventManager;
import jp.hack.minecraft.werewolfgame.util.LocationConfiguration;
import org.bukkit.Difficulty;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Main extends JavaPlugin implements GameConfigurator {

    private CommandManager commandManager;
    private Game game;
    private List<Player> players;
    private LocationConfiguration configuration;

    @Override
    public void onEnable() {
        // Plugin startup logic
        super.onEnable();
        getServer().getPluginManager().registerEvents(new GameEventManager(this), this); // イベントはここに統一

//        configuration = new LocationConfiguration(new File(getDataFolder(), "config.yml"));
        if (configuration == null)
            configuration = new LocationConfiguration(this);
        configuration.load();

        if (game == null) {
            game = new Game(this, configuration);
            game.initialize();
        }

        game.getLobbyPos().getWorld().setDifficulty(Difficulty.PEACEFUL);

        players = new ArrayList<>();
        if (commandManager == null) {
            commandManager = new CommandManager(this);
            commandManager.addRootCommand(new WerewolfCommand(commandManager)); // plugin.ymlへの登録を忘れずに
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        super.onDisable();

        if (this.configuration != null) {
            this.configuration.save();
        }

        game.gameStop();
    }

    @Override
    public Game getGame() {
        return game;
    }
}
