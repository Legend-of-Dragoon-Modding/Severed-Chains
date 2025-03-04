package legend.core.platform;

import legend.core.platform.input.InputAction;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.glfwGetCurrentContext;
import static org.lwjgl.glfw.GLFW.glfwGetMonitorName;
import static org.lwjgl.glfw.GLFW.glfwGetMonitors;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetJoystickCallback;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

public class GlfwPlatformManager extends PlatformManager {
  @Override
  public void init() {
    GLFWErrorCallback.createPrint(System.err).set();

    if(!glfwInit()) {
      throw new IllegalStateException("Unable to initialize GLFW");
    }
  }

  @Override
  public void destroy() {
    super.destroy();
    glfwSetErrorCallback(null).free();
    glfwSetJoystickCallback(null).free();
    glfwTerminate();
  }

  @Override
  public boolean isContextCurrent() {
    return glfwGetCurrentContext() != 0;
  }

  @Override
  public boolean hasGamepad() {
    return false;
  }

  @Override
  public int getMouseButton(final int index) {
    return GLFW_MOUSE_BUTTON_LEFT + index;
  }

  @Override
  public String[] listDisplays() {
    final PointerBuffer monitors = glfwGetMonitors();
    final String[] out = new String[monitors.limit()];

    for(int i = 0; i < monitors.limit(); i++) {
      out[i] = glfwGetMonitorName(monitors.get(i));
    }

    return out;
  }

  @Override
  protected Window createWindow(final String title, final int width, final int height) {
    return new GlfwWindow(this, title, width, height);
  }

  @Override
  protected void tickInput() {
    glfwPollEvents();
    super.tickInput();
  }

  @Override
  public void rumble(final float intensity, final int ms) {

  }

  @Override
  public void rumble(final float bigIntensity, final float smallIntensity, final int ms) {

  }

  @Override
  public void adjustRumble(final float intensity, final int ms) {

  }

  @Override
  public void adjustRumble(final float bigIntensity, final float smallIntensity, final int ms) {

  }

  @Override
  public void stopRumble() {

  }

  @Override
  public boolean isActionPressed(final InputAction action) {
    throw new RuntimeException("Not yet implemented");
  }

  @Override
  public boolean isActionRepeat(final InputAction action) {
    throw new RuntimeException("Not yet implemented");
  }

  @Override
  public boolean isActionHeld(final InputAction action) {
    throw new RuntimeException("Not yet implemented");
  }

  @Override
  public float getAxis(final InputAction action) {
    throw new RuntimeException("Not yet implemented");
  }
}
