package legend.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class TestHarness {

    public static void main(String[] args)  {

        legend.game.Main.main(args);
        testStartOfGame();
        }


    @Test
    public static void testStartOfGame() {
        Assertions.assertTrue(FirstTest.startOfGame());
    }
}