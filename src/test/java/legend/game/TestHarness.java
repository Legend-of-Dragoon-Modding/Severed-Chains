package legend.game;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

//TODO use Junit for all test cases
public class TestHarness {

    public static void main(String[] args)  {
        legend.game.Main.main(args);
        }


    @Test
    public void testStartOfGame() {
        FirstTest.startOfGame();
        Assertions.assertTrue(true);

    }
}