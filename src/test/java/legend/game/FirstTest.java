package legend.game;

import legend.core.DebugHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;


public class FirstTest {
  public static boolean startOfGame()  {
    DebugHelper.sleep(3000); //wait for game to load
    UserInputs.enter(); // skip first screen
    DebugHelper.sleep(3000);
    UserInputs.spaceBar(); // skip second screen
    DebugHelper.sleep(10000);
    UserInputs.arrowDown(100); // move selection to options menu when no continue game option is available
    DebugHelper.sleep(1000);
    UserInputs.sKey(); // select options menu
    return true;
  }
}
