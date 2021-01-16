package jp.hack.minecraft.werewolfgame.core.command.werewolf;

import jp.hack.minecraft.werewolfgame.core.command.CommandManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;

@PowerMockIgnore("jdk.internal.reflect.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest(SetLobbyCommand.class)
public class SetLobbyCommandTest {

    class CommandManagerMock extends CommandManager {
        public CommandManagerMock() {
            super(null);
        }
    }

    @Test
    public void testGetName() throws Exception{
        SetLobbyCommand command = new SetLobbyCommand(new CommandManagerMock());
        assertEquals("setLobby", command.getName());
    }


}
