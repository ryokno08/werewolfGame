package jp.hack.minecraft.werewolfgame.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Messages.class})
public class MessagesTest {

    @Test
    public void testMessage() throws Exception {
        System.out.println(Messages.message("001"));
    }
}