package jp.hack.minecraft.werewolfgame.core.command.werewolf;

import jp.hack.minecraft.werewolfgame.core.command.CommandManager;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.easymock.PowerMock.createMock;

@PowerMockIgnore("jdk.internal.reflect.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest(SetLobbyCommand.class)
public class SetLobbyCommandTest {

    @Mock
    CommandManager commandManager;

    @Before
    public void setUp(){
        commandManager = createMock(CommandManager.class);
    }

    @Test
    public void testGetName() throws Exception{
        SetLobbyCommand command = new SetLobbyCommand(commandManager);
        System.out.println("getName=" + command.getName());
        assertEquals("setLobby", command.getName());
    }
}
