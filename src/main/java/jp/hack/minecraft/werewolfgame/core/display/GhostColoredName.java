package jp.hack.minecraft.werewolfgame.core.display;

import jp.hack.minecraft.werewolfgame.Game;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class GhostColoredName {
    private JavaPlugin plugin;
    private Team team;

    public GhostColoredName(JavaPlugin plugin) {
        this.plugin = plugin;
        ScoreboardManager manager = plugin.getServer().getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();
        team = scoreboard.registerNewTeam("ghost");
        team.setColor(ChatColor.RED);
    }

    
}
