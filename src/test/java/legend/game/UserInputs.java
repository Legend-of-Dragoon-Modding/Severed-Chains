package legend.game;


import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import org.apache.logging.log4j.LogManager;
import legend.core.DebugHelper;
import org.apache.logging.log4j.Logger;

public final class UserInputs {
    private static final Logger LOGGER = LogManager.getFormatterLogger();

    private UserInputs() {
    }

    public static void arrowUp(final int pressDur) throws AWTException {
        final Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_UP);
        DebugHelper.sleep(pressDur);
        robot.keyRelease(KeyEvent.VK_UP);
        LOGGER.info("Up key has been pressed for {} milliseconds", pressDur);
    }

    public static void arrowDown(final int pressDur) throws AWTException {
        final Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_DOWN);
        DebugHelper.sleep(pressDur);
        robot.keyRelease(KeyEvent.VK_DOWN);
        LOGGER.info("Down key has been pressed for {} milliseconds", pressDur);
    }

    public static void arrowLeft(final int pressDur) throws AWTException  {
        final Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_LEFT);
        DebugHelper.sleep(pressDur);
        robot.keyRelease(KeyEvent.VK_LEFT);
        LOGGER.info("Left key has been pressed for {} milliseconds", pressDur);
    }
    public static void arrowRight(final int pressDur) throws AWTException {
        final Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_RIGHT);
        DebugHelper.sleep(pressDur);
        robot.keyRelease(KeyEvent.VK_RIGHT);
        LOGGER.info("Right key has been pressed for {} milliseconds", pressDur);
    }

    public static void enter() throws AWTException {
        final Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_ENTER);
        DebugHelper.sleep(100);
        robot.keyRelease(KeyEvent.VK_ENTER);
        LOGGER.info("Enter key has been pressed");
    }

    public static void spaceBar() throws AWTException {
        final Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_SPACE);
        DebugHelper.sleep(100);
        robot.keyRelease(KeyEvent.VK_SPACE);
        LOGGER.info("Space bar key has been pressed");
    }

    public static void wKey() throws AWTException {
        final Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_W);
        DebugHelper.sleep(100);
        robot.keyRelease(KeyEvent.VK_W);
        LOGGER.info("W key has been pressed");
    }

    public static void aKey() throws AWTException {
        final Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_A);
        DebugHelper.sleep(100);
        robot.keyRelease(KeyEvent.VK_A);
        LOGGER.info("A key has been pressed");
    }

    public static void sKey() throws AWTException {
        final Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_S);
        DebugHelper.sleep(100);
        robot.keyRelease(KeyEvent.VK_S);
        LOGGER.info("S key has been pressed");
    }

    public static void dKey() throws AWTException {
        final Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_D);
        DebugHelper.sleep(100);
        robot.keyRelease(KeyEvent.VK_D);
        LOGGER.info("D key has been pressed");
    }

    public static void l1Button() throws AWTException {
        final Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_Q);
        DebugHelper.sleep(100);
        robot.keyRelease(KeyEvent.VK_Q);
        LOGGER.info("L1 button has been pressed");
    }

    public static void l2Button() throws AWTException {
        final Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_1);
        DebugHelper.sleep(100);
        robot.keyRelease(KeyEvent.VK_1);
        LOGGER.info("L2 button has been pressed");
    }

    public static void l3Button() throws AWTException {
        final Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_Z);
        DebugHelper.sleep(100);
        robot.keyRelease(KeyEvent.VK_Z);
        LOGGER.info("L3 button has been pressed");
    }

    public static void r1Button() throws AWTException {
        final Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_E);
        DebugHelper.sleep(100);
        robot.keyRelease(KeyEvent.VK_E);
        LOGGER.info("R1 button has been pressed");
    }

    public static void r2Button() throws AWTException {
        final Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_3);
        DebugHelper.sleep(100);
        robot.keyRelease(KeyEvent.VK_3);
        LOGGER.info("R2 button has been pressed");
    }

    public static void r3Button() throws AWTException {
        final Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_C);
        DebugHelper.sleep(100);
        robot.keyRelease(KeyEvent.VK_C);
        LOGGER.info("R3 button has been pressed");
    }

    public static void moveMouseTo(final int x, final int y) throws AWTException {
        final Robot robot = new Robot();
        robot.mouseMove(x, y);
        LOGGER.info("Mouse has been moved to 100, 100");
    }

    public static void mouseLeftClick() throws AWTException {
        final Robot robot = new Robot();
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        DebugHelper.sleep(100);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        LOGGER.info("Mouse left click has been pressed");
    }

    public static void mouseRightClick() throws AWTException {
        final Robot robot = new Robot();
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        DebugHelper.sleep(100);
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        LOGGER.info("Mouse right click has been pressed");
    }

    public static void mouseMiddleClick() throws AWTException {
        final Robot robot = new Robot();
        robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
        DebugHelper.sleep(100);
        robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
        LOGGER.info("Mouse middle click has been pressed");
    }

    public static void escapeButton() throws AWTException {
        final Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_ESCAPE);
        DebugHelper.sleep(100);
        robot.keyRelease(KeyEvent.VK_ESCAPE);
        LOGGER.info("Escape button has been pressed");
    }
}