package jp.hack.minecraft.werewolfgame.core.state;

import jp.hack.minecraft.werewolfgame.Game;
import jp.hack.minecraft.werewolfgame.core.command.werewolf.SetLobbyCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;

@PowerMockIgnore("jdk.internal.reflect.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest(SetLobbyCommand.class)
public class LobbyStateTest {

    @Mock
    JavaPlugin plugin;
    @Mock
    Game game;

    private LobbyState lobbyState;

    @Before
    public void setUp() {
        plugin = PowerMock.createMock(JavaPlugin.class);
        game = PowerMock.createMock(Game.class);
        lobbyState = new LobbyState(plugin, game);
    }


    @Test
    public void testOnStart() {
    }


}
