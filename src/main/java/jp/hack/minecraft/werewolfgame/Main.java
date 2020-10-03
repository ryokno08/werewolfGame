package jp.hack.minecraft.werewolfgame;

import jp.hack.minecraft.werewolfgame.core.ChatManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new ChatManager(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
