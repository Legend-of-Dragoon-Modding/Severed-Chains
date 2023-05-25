package legend.game;

import legend.core.DebugHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;


public final class FirstTest {
  private FirstTest() {
  }

  public static boolean startOfGame()  {
    DebugHelper.sleep(3000); //wait for game to load
    UserInputs.enter(); // skip first screen
    DebugHelper.sleep(3000);
    UserInputs.spaceBar(); // skip second screen
    DebugHelper.sleep(10000);
    UserInputs.sKey(); // select continue game only works if you have a save file
    DebugHelper.sleep(1000);
    UserInputs.sKey(); // start first save game
    DebugHelper.sleep(1000);
    UserInputs.sKey(); // say yes to starting game
    UserInputs.arrowDown(1000); // move character down.
    DebugHelper.sleep(1000);
    UserInputs.wKey(); // open pause menu
    DebugHelper.sleep(1000);
    UserInputs.arrowDown(100); // move selection down one in menu
    DebugHelper.sleep(1000);
    UserInputs.sKey(); // select current selection
    DebugHelper.sleep(1000);
    UserInputs.escapeButton(); // back out of screen
    DebugHelper.sleep(1000);
    UserInputs.escapeButton(); // close pause menus
    DebugHelper.sleep(1000);
    UserInputs.arrowLeft(3000); // move character left for 3 seconds
    //
    // start of loop to get through dialog before fight with commander
    long t= System.currentTimeMillis();
    long end = t+120000;
    while(System.currentTimeMillis() < end) {
      UserInputs.sKey(); // skip dialog
      DebugHelper.sleep( 2000 );
    }
    DebugHelper.sleep(1000);
    UserInputs.sKey(); // select attack menu button
    DebugHelper.sleep(1000);
    UserInputs.sKey(); // select target and attack
    DebugHelper.sleep(2000); // timing for addition dart double slash
    UserInputs.sKey(); // hit addition marker
    return true;
  }
}
