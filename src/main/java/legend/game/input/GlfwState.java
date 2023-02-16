package legend.game.input;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
public class GlfwState {

  private static FloatBuffer axis;
  private static ByteBuffer hats;
  private static ByteBuffer buttons;
  public void UpdateState(final FloatBuffer axis, final ByteBuffer hats, final ByteBuffer buttons) {
    GlfwState.axis = axis;
    GlfwState.hats = hats;
    GlfwState.buttons = buttons;
  }

  public static boolean checkButton(final int glfwCode) {
    return buttons.get(glfwCode) != 0;
  }

  public static float checkAxis(final int glfwCode) {
    return axis.get(glfwCode);
  }

  public static boolean checkHat(final int glfwCode) {
    final int hat = hats.get(0);
    return (hat & glfwCode) != 0;
  }


}
