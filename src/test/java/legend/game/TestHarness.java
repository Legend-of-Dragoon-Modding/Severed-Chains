package legend.game;

import java.awt.AWTException;
import java.io.IOException;
public class TestHarness {

    public static void main(String[] args) throws IOException, InterruptedException, AWTException {
        try {
            legend.game.Main.main(args);
            Thread.sleep(3000);
            UserInputs.enter();
            Thread.sleep(3000);
            UserInputs.spaceBar();
            Thread.sleep(10000);
            UserInputs.arrowDown(100);
            Thread.sleep(1000);
            UserInputs.sKey();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Game could not be started, see stack trace above.");
        }
    }
}