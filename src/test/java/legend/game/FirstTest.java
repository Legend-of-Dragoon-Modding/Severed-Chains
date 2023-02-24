package legend.game;

import legend.core.DebugHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;


public class FirstTest {
  public static boolean startOfGame()  {
    DebugHelper.sleep(3000);
    UserInputs.enter();
    DebugHelper.sleep(3000);
    UserInputs.spaceBar();
    DebugHelper.sleep(10000);
    UserInputs.arrowDown(100);
    DebugHelper.sleep(1000);
    UserInputs.sKey();
    return true;
  }
}
