package legend.game;

import legend.core.DebugHelper;
import legend.core.GameEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestHarness {
    @Test
    public void testStartOfGame() {
        final Thread tests = new Thread(() -> {
           while(GameEngine.isLoading()) {
               DebugHelper.sleep(5);
           }

            Assertions.assertTrue(FirstTest.startOfGame());
        });

        tests.setName("Tests");
        tests.start();

        Main.main(new String[0]);
    }
}
