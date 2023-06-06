package legend.game;

import legend.core.DebugHelper;
import legend.core.GameEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;

public class TestHarness {
  private static final Logger LOGGER = LogManager.getFormatterLogger();
  @Test
    public void testStartOfGame() {
        final Thread tests = new Thread(() -> {
           while(GameEngine.isLoading()) {
               DebugHelper.sleep(5);
           }

          try {
            Assertions.assertTrue(FirstTest.startOfGame());
          } catch(final AWTException e) {
            LOGGER.error("FirstTest.startOfGame() failed to start. See stack trace.");
          }
        });

        tests.setName("Tests");
        tests.start();

        Main.main(new String[0]);
    }
}
