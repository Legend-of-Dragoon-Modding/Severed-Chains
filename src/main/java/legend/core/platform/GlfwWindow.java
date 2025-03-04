package legend.core.platform;

import legend.core.Config;
import legend.core.opengl.Action;
import legend.core.platform.input.InputClass;
import legend.game.modding.coremod.CoreMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;

import static legend.core.GameEngine.CONFIG;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_CONNECTED;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_HIDDEN;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_DECORATED;
import static org.lwjgl.glfw.GLFW.GLFW_DISCONNECTED;
import static org.lwjgl.glfw.GLFW.GLFW_DONT_CARE;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_HAND_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_DEBUG_CONTEXT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateStandardCursor;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyCursor;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;
import static org.lwjgl.glfw.GLFW.glfwGetMonitorPos;
import static org.lwjgl.glfw.GLFW.glfwGetMonitors;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetCharCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursor;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetJoystickCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowAttrib;
import static org.lwjgl.glfw.GLFW.glfwSetWindowFocusCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowIcon;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeLimits;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memFree;

public class GlfwWindow extends Window {
  private static final Logger LOGGER = LogManager.getFormatterLogger(GlfwWindow.class);

  private final GlfwPlatformManager manager;
  private final long window;

  private int width;
  private int height;
  private boolean hasFocus;

  private long monitor;
  private GLFWVidMode vidMode;

  private final long pointerCursor;

  private final Action render;

  protected GlfwWindow(final GlfwPlatformManager manager, final String title, final int width, final int height) {
    this.manager = manager;
    this.monitor = this.getMonitorFromConfig();
    this.vidMode = glfwGetVideoMode(this.monitor);

    if(this.vidMode == null) {
      throw new RuntimeException("Failed to get video mode");
    }

    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

    if("true".equals(System.getenv("opengl_debug"))) {
      glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
    }

    this.window = glfwCreateWindow(width, height, title, NULL, NULL);

    if(this.window == NULL) {
      throw new RuntimeException("Failed to create the GLFW window");
    }

    glfwSetWindowSizeLimits(this.window, 320, 240, GLFW_DONT_CARE, GLFW_DONT_CARE);

    this.pointerCursor = glfwCreateStandardCursor(GLFW_HAND_CURSOR);

    glfwSetFramebufferSizeCallback(this.window, (window, w, h) -> {
      this.width = w;
      this.height = h;
      this.events().onResize(w, h);
    });

    glfwSetWindowFocusCallback(this.window, (window, focus) -> {
      this.hasFocus = focus;
      this.events().onFocus(focus);
    });

    glfwSetKeyCallback(this.window, (window, key, scancode, action, mods) -> {
      switch(action) {
        //TODO
//        case GLFW_PRESS -> this.events().onKeyPress(key, scancode, mods);
//        case GLFW_RELEASE -> this.events().onKeyRelease(key, scancode, mods);
//        case GLFW_REPEAT -> this.events().onKeyRepeat(key, scancode, mods);
      }
    });

    glfwSetCharCallback(this.window, (window, codepoint) -> this.events().onChar(codepoint));
    glfwSetCursorPosCallback(this.window, (window, x, y) -> this.events().onMouseMove(x, y));
    glfwSetMouseButtonCallback(this.window, (window, button, action, mods) -> {
      switch(action) {
        //TODO
//        case GLFW_PRESS -> this.events().onMousePress(button, mods);
//        case GLFW_RELEASE -> this.events().onMouseRelease(button, mods);
      }
    });

    glfwSetScrollCallback(this.window, (window1, deltaX, deltaY) -> this.events().onMouseScroll(deltaX, deltaY));

    glfwSetJoystickCallback((jid, event) -> {
      if(event == GLFW_CONNECTED) {
        this.events().onControllerConnected(jid);
      } else if(event == GLFW_DISCONNECTED) {
        this.events().onControllerDisconnected(jid);
      }
    });

    glfwMakeContextCurrent(this.window);
    glfwSwapInterval(0);

    this.render = this.manager.addAction(new Action(this::tick, 60));
  }

  @Override
  public InputClass getInputClass() {
    return InputClass.GAMEPAD; //TODO
  }

  private long getMonitorFromConfig() {
    if(CONFIG.getConfig(CoreMod.FULLSCREEN_CONFIG.get())) {
      final int monitorIndex = CONFIG.getConfig(CoreMod.MONITOR_CONFIG.get());
      final PointerBuffer monitorPtrs = glfwGetMonitors();

      if(monitorIndex >= 0 && monitorIndex < monitorPtrs.limit()) {
        return monitorPtrs.get(monitorIndex);
      }
    }

    return glfwGetPrimaryMonitor();
  }

  @Override
  public void updateMonitor() {
    this.monitor = this.getMonitorFromConfig();

    if(CONFIG.getConfig(CoreMod.FULLSCREEN_CONFIG.get())) {
      this.makeFullscreen();
    }
  }

  private void moveToMonitor() {
    final int[] x = new int[1];
    final int[] y = new int[1];
    glfwGetMonitorPos(this.monitor, x, y);
    glfwSetWindowPos(this.window, x[0], y[0]);
  }

  @Override
  public void centerWindow() {
    try(final MemoryStack stack = stackPush()) {
      final IntBuffer pWidth = stack.mallocInt(1);
      final IntBuffer pHeight = stack.mallocInt(1);

      glfwGetFramebufferSize(this.window, pWidth, pHeight);

      final int[] x = new int[1];
      final int[] y = new int[1];
      glfwGetMonitorPos(this.monitor, x, y);

      glfwSetWindowPos(
        this.window,
        x[0] + (this.vidMode.width() - pWidth.get(0)) / 2,
        y[0] + (this.vidMode.height() - pHeight.get(0)) / 2
      );
    }
  }

  @Override
  public void makeFullscreen() {
    this.monitor = this.getMonitorFromConfig();
    this.vidMode = glfwGetVideoMode(this.monitor);
    this.moveToMonitor();
    glfwSetWindowAttrib(this.window, GLFW_DECORATED, GLFW_FALSE);

    // Overscan by 1 pixel to stop Windows from putting it into exclusive fullscreen
    glfwSetWindowSize(this.window, this.vidMode.width(), this.vidMode.height() + 1);
  }

  @Override
  public void makeWindowed() {
    glfwSetWindowAttrib(this.window, GLFW_DECORATED, GLFW_TRUE);
    glfwSetWindowSize(this.window, Config.windowWidth(), Config.windowHeight());
    this.centerWindow();
  }

  @Override
  public void useNormalCursor() {
    glfwSetCursor(this.window, NULL);
  }

  @Override
  public void usePointerCursor() {
    glfwSetCursor(this.window, this.pointerCursor);
  }

  @Override
  public void setWindowIcon(final Path path) {
    try(final MemoryStack stack = stackPush()) {
      final IntBuffer w = stack.mallocInt(1);
      final IntBuffer h = stack.mallocInt(1);
      final IntBuffer comp = stack.mallocInt(1);

      final ByteBuffer data = stbi_load(path.toString(), w, h, comp, 4);
      if(data == null) {
        LOGGER.warn("Failed to load icon %s: %s", path, stbi_failure_reason());
        return;
      }

      final GLFWImage image = GLFWImage.malloc(stack);
      final GLFWImage.Buffer buffer = GLFWImage.malloc(1, stack);
      image.set(w.get(0), h.get(0), data);
      buffer.put(0, image);
      glfwSetWindowIcon(this.window, buffer);
      memFree(data);
    }
  }

  @Override
  public void setFpsLimit(final int limit) {
    this.render.setExpectedFps(limit);
  }

  @Override
  public int getFpsLimit() {
    return this.render.getExpectedFps();
  }

  @Override
  public void show() {
    glfwShowWindow(this.window);

    try(final MemoryStack stack = MemoryStack.stackPush()) {
      final IntBuffer x = stack.mallocInt(1);
      final IntBuffer y = stack.mallocInt(1);
      glfwGetFramebufferSize(this.window, x, y);

      this.width = x.get(0);
      this.height = y.get(0);
      this.events().onResize(this.width, this.height);
    }
  }

  @Override
  public void close() {
    glfwSetWindowShouldClose(this.window, true);
  }

  @Override
  public void disableCursor() {
    glfwSetInputMode(this.window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
  }

  @Override
  public void showCursor() {
    glfwSetInputMode(this.window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
  }

  @Override
  public void hideCursor() {
    glfwSetInputMode(this.window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
  }

  @Override
  public boolean hasFocus() {
    return this.hasFocus;
  }

  @Override
  public void setTitle(final String title) {
    glfwSetWindowTitle(this.window, title);
  }

  @Override
  public int getWidth() {
    return this.width;
  }

  @Override
  public int getHeight() {
    return this.height;
  }

  @Override
  protected void destroy() {
    this.manager.removeAction(this.render);
    this.events().onClose();
    glfwDestroyCursor(this.pointerCursor);
    glfwFreeCallbacks(this.window);
    glfwDestroyWindow(this.window);
  }

  @Override
  protected boolean shouldClose() {
    return glfwWindowShouldClose(this.window);
  }

  private void tick() {
    this.events().onDraw();
    glfwSwapBuffers(this.window);
  }
}
