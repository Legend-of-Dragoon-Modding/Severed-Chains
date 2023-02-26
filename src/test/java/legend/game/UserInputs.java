package legend.game;


import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class UserInputs {

    public static void arrowUp(int pressDur)  {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_UP);
            Thread.sleep(pressDur);
            robot.keyRelease(KeyEvent.VK_UP);
            System.out.println("Up key has been pressed for " + pressDur + " milliseconds");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("arrowUp() failed, see stack trace");
        }
    }

    public static void arrowDown(int pressDur)  {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_DOWN);
            Thread.sleep(pressDur);
            robot.keyRelease(KeyEvent.VK_DOWN);
            System.out.println("Down key has been pressed for " + pressDur + " milliseconds");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("arrowDown() failed, see stack trace");
        }
    }

    public static void arrowLeft(int pressDur)  {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_LEFT);
            Thread.sleep(pressDur);
            robot.keyRelease(KeyEvent.VK_LEFT);
            System.out.println("Left key has been pressed for " + pressDur + " milliseconds");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("arrowLeft() failed, see stack trace");
        }
    }
    public static void arrowRight(int pressDur)  {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_RIGHT);
            Thread.sleep(pressDur);
            robot.keyRelease(KeyEvent.VK_RIGHT);
            System.out.println("Right key has been pressed for " + pressDur + " milliseconds");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("arrowRight() failed, see stack trace");
        }
    }

    public static void enter()  {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ENTER);
            Thread.sleep(100);
            robot.keyRelease(KeyEvent.VK_ENTER);
            System.out.println("Enter key has been pressed");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("enter() failed, see stack trace");
        }
    }

    public static void spaceBar()  {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_SPACE);
            Thread.sleep(100);
            robot.keyRelease(KeyEvent.VK_SPACE);
            System.out.println("Space bar key has been pressed");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("spaceBar() failed, see stack trace");
        }
    }

    public static void wKey()  {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_W);
            Thread.sleep(100);
            robot.keyRelease(KeyEvent.VK_W);
            System.out.println("W key has been pressed");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("wKey() failed, see stack trace");
        }
    }

    public static void aKey()  {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_A);
            Thread.sleep(100);
            robot.keyRelease(KeyEvent.VK_A);
            System.out.println("A key has been pressed");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("aKey() failed, see stack trace");
        }
    }

    public static void sKey()  {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_S);
            Thread.sleep(100);
            robot.keyRelease(KeyEvent.VK_S);
            System.out.println("S key has been pressed");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("sKey() failed, see stack trace");
        }
    }

    public static void dKey()  {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_D);
            Thread.sleep(100);
            robot.keyRelease(KeyEvent.VK_D);
            System.out.println("D key has been pressed");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("dKey() failed, see stack trace");
        }
    }

    public static void l1Button()  {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_Q);
            Thread.sleep(100);
            robot.keyRelease(KeyEvent.VK_Q);
            System.out.println("L1 button has been pressed");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("l1Button() failed, see stack trace");
        }
    }

    public static void l2Button()  {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_1);
            Thread.sleep(100);
            robot.keyRelease(KeyEvent.VK_1);
            System.out.println("L2 button has been pressed");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("l2Button() failed, see stack trace");
        }
    }

    public static void l3Button()  {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_Z);
            Thread.sleep(100);
            robot.keyRelease(KeyEvent.VK_Z);
            System.out.println("L3 button has been pressed");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("l3Button() failed, see stack trace");
        }
    }

    public static void r1Button()  {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_E);
            Thread.sleep(100);
            robot.keyRelease(KeyEvent.VK_E);
            System.out.println("R1 button has been pressed");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("r1Button() failed, see stack trace");
        }
    }

    public static void r2Button()  {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_3);
            Thread.sleep(100);
            robot.keyRelease(KeyEvent.VK_3);
            System.out.println("R2 button has been pressed");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("r2Button() failed, see stack trace");
        }
    }

    public static void r3Button()  {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_C);
            Thread.sleep(100);
            robot.keyRelease(KeyEvent.VK_C);
            System.out.println("R3 button has been pressed");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("r3Button() failed, see stack trace");
        }
    }

    public static void moveMouseTo(int x, int y) {
        try {
            Robot robot = new Robot();
            robot.mouseMove(x, y);
            System.out.println("Mouse has been moved to 100, 100");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("moveMouseTo() failed, see stack trace");
        }
    }

    public static void mouseLeftClick() {
        try {
            Robot robot = new Robot();
            robot.mousePress(InputEvent.BUTTON1_MASK);
            Thread.sleep(100);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
            System.out.println("Mouse left click has been pressed");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("mouseLeftClick() failed, see stack trace");
        }
    }

    public static void mouseRightClick() {
        try {
            Robot robot = new Robot();
            robot.mousePress(InputEvent.BUTTON3_MASK);
            Thread.sleep(100);
            robot.mouseRelease(InputEvent.BUTTON3_MASK);
            System.out.println("Mouse right click has been pressed");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("mouseRightClick() failed, see stack trace");
        }
    }

    public static void mouseMiddleClick() {
        try {
            Robot robot = new Robot();
            robot.mousePress(InputEvent.BUTTON2_MASK);
            Thread.sleep(100);
            robot.mouseRelease(InputEvent.BUTTON2_MASK);
            System.out.println("Mouse middle click has been pressed");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("mouseMiddleClick() failed, see stack trace");
        }
    }

    public static void escapeButton() {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ESCAPE);
            Thread.sleep(100);
            robot.keyRelease(KeyEvent.VK_ESCAPE);
            System.out.println("Escape button has been pressed");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("escapeButton() failed, see stack trace");
        }
    }
}