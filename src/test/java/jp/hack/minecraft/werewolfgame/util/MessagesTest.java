package jp.hack.minecraft.werewolfgame.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;

@PowerMockIgnore("jdk.internal.reflect.*")
@RunWith(PowerMockRunner.class)
@PrepareForTest(Messages.class)
public class MessagesTest {
    @Test
    public void testMessage() throws Exception {
        assertEquals("討論開始", Messages.message("001"));
    }

    @Test
    public void testError() throws Exception {
        assertEquals("ERR001 存在しないコードのメッセージを取得しようとしました", Messages.error("001"));
    }
}