package legend.core.opengl;

import legend.core.Config;
import legend.core.DebugHelper;
import legend.game.input.Controller;
import legend.game.input.Input;
import legend.game.input.InputAction;
import legend.game.input.InputBinding;
import legend.game.input.InputState;
import legend.game.modding.coremod.CoreMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static legend.core.GameEngine.CONFIG;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_CONNECTED;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_DECORATED;
import static org.lwjgl.glfw.GLFW.GLFW_DISCONNECTED;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_DEBUG_CONTEXT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetClipboardString;
import static org.lwjgl.glfw.GLFW.glfwGetFramebufferSize;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowContentScale;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetCharCallback;
import static org.lwjgl.glfw.GLFW.glfwSetClipboardString;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetJoystickCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowAttrib;
import static org.lwjgl.glfw.GLFW.glfwSetWindowFocusCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11C.GL_VENDOR;
import static org.lwjgl.opengl.GL11C.GL_VERSION;
import static org.lwjgl.opengl.GL11C.glGetString;
import static org.lwjgl.opengl.GL20C.GL_SHADING_LANGUAGE_VERSION;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
  private static final Logger LOGGER = LogManager.getLogger(Window.class.getName());

  static {
    LOGGER.info("Initialising LWJGL version {}", Version.getVersion());

    GLFWErrorCallback.createPrint(System.err).set();

    if(!glfwInit()) {
      throw new IllegalStateException("Unable to initialize GLFW");
    }
  }

  public static void free() {
    LOGGER.info("Shutting down...");

    glfwSetErrorCallback(null).free();
    glfwSetJoystickCallback(null).free();
    glfwTerminate();

    try {
      Config.save();
    } catch(final IOException e) {
      System.err.println("Failed to save config");
    }
  }

  private final long window;
  private Runnable eventPoller = GLFW::glfwPollEvents;

  public final Events events = new Events(this);

  private int width;
  private int height;
  private final float scale = 1.0f;
  private boolean hasFocus;

  private final GLFWVidMode vidMode;

  private final List<Action> actions = new ArrayList<>();
  private final Action render = this.addAction(new Action(this::tickFrame, 60));

  public Window(final String title, final int width, final int height) {
    this.vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

    if(this.vidMode == null) {
      throw new RuntimeException("Failed to get video mode");
    }

    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
//    glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE);

    if("true".equals(System.getenv("opengl_debug"))) {
      glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
    }

    if(CONFIG.getConfig(CoreMod.FULLSCREEN_CONFIG.get())) {
      glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
      this.window = glfwCreateWindow(this.vidMode.width(), this.vidMode.height(), title, NULL, NULL);
    } else {
      this.window = glfwCreateWindow(width, height, title, NULL, NULL);
    }

    if(this.window == NULL) {
      throw new RuntimeException("Failed to create the GLFW window");
    }

    glfwSetFramebufferSizeCallback(this.window, this.events::onResize);
    glfwSetWindowFocusCallback(this.window,this.events::onFocus);
    glfwSetKeyCallback(this.window, this.events::onKey);
    glfwSetCharCallback(this.window, this.events::onChar);
    glfwSetCursorPosCallback(this.window, this.events::onMouseMove);
    glfwSetMouseButtonCallback(this.window, this.events::onMouseButton);
    glfwSetScrollCallback(this.window, this.events::onMouseScroll);
    glfwSetJoystickCallback((jid, event) -> {
      if(event == GLFW_CONNECTED) {
        this.events.onControllerConnected(this.window, jid);
      } else if(event == GLFW_DISCONNECTED) {
        this.events.onControllerDisconnected(this.window, jid);
      }
    });

    this.centerWindow();
    this.makeContextCurrent();
    GL.createCapabilities();

    LOGGER.info("OpenGL version: {}", glGetString(GL_VERSION));
    LOGGER.info("GLSL version: {}", glGetString(GL_SHADING_LANGUAGE_VERSION));
    LOGGER.info("Device manufacturer: {}", glGetString(GL_VENDOR));

    if("true".equals(System.getenv("opengl_debug"))) {
      GLUtil.setupDebugMessageCallback(System.err);
    }
  }

  public void centerWindow() {
    try(final MemoryStack stack = stackPush()) {
      final IntBuffer pWidth = stack.mallocInt(1);
      final IntBuffer pHeight = stack.mallocInt(1);

      glfwGetFramebufferSize(this.window, pWidth, pHeight);

      glfwSetWindowPos(
        this.window,
        (this.vidMode.width() - pWidth.get(0)) / 2,
        (this.vidMode.height() - pHeight.get(0)) / 2
      );
    }
  }

  public void makeFullscreen() {
    glfwSetWindowAttrib(this.window, GLFW_DECORATED, GLFW_FALSE);
    glfwSetWindowSize(this.window, this.vidMode.width(), this.vidMode.height());
    this.centerWindow();
  }

  public void makeWindowed() {
    glfwSetWindowAttrib(this.window, GLFW_DECORATED, GLFW_TRUE);
    glfwSetWindowSize(this.window, Config.windowWidth(), Config.windowHeight());
    this.centerWindow();
  }

  public Action addAction(final Action action) {
    this.actions.add(action);
    return action;
  }

  private void makeContextCurrent() {
    glfwMakeContextCurrent(this.window);
    glfwSwapInterval(0);
  }

  public void setEventPoller(final Runnable poller) {
    this.eventPoller = poller;
  }

  public void setFpsLimit(final int limit) {
    this.render.setExpectedFps(limit);
  }

  public int getFpsLimit() {
    return this.render.getExpectedFps();
  }

  public void show() {
    glfwShowWindow(this.window);

    try(final MemoryStack stack = MemoryStack.stackPush()) {
      final IntBuffer x = stack.mallocInt(1);
      final IntBuffer y = stack.mallocInt(1);
      glfwGetFramebufferSize(this.window, x, y);

      this.events.onResize(this.window, x.get(0), y.get(0));
    }
  }

  public void close() {
    glfwSetWindowShouldClose(this.window, true);
  }

  public void hideCursor() {
    glfwSetInputMode(this.window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
  }

  public void showCursor() {
    glfwSetInputMode(this.window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
  }

  public void setCursorPos(final double x, final double y) {
    glfwSetCursorPos(this.window, x, y);
  }

  public boolean isKeyPressed(final int key) {
    return glfwGetKey(this.window, key) == GLFW_PRESS;
  }

  public boolean hasFocus() {
    return this.hasFocus;
  }

  public void setTitle(final String title) {
    glfwSetWindowTitle(this.window, title);
  }

  public String getClipboardString() {
    final String string = glfwGetClipboardString(this.window);
    return string != null ? string : "";
  }

  public void setClipboardString(final CharSequence string) {
    glfwSetClipboardString(this.window, string);
  }

  public void setClipboardString(final ByteBuffer string) {
    glfwSetClipboardString(this.window, string);
  }

  public void resize(final int width, final int height) {
    glfwSetWindowSize(this.window, (int)(width * this.scale), (int)(height * this.scale));
  }

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }

  public float getScale() {
    return this.scale;
  }

  public void run() {
    this.addAction(new Action(this::tickInput, 60));

    while(!glfwWindowShouldClose(this.window)) {
      Action nextAction = null;

      for(int i = 0; i < this.actions.size(); i++) {
        final Action action = this.actions.get(i);
        action.tick();

        if(nextAction == null || action.nanosUntilNextRun() < nextAction.nanosUntilNextRun()) {
          nextAction = action;
        }
      }

      DebugHelper.sleep(Math.max(0, nextAction.nanosUntilNextRun() / 1_000_000));
    }

    this.events.onShutdown();

    glfwFreeCallbacks(this.window);
    glfwDestroyWindow(this.window);
  }

  private void tickInput() {
    this.eventPoller.run();
    Input.update();
  }

  private void tickFrame() {
    Input.updateLegacyInput();
    this.events.onDraw();
    glfwSwapBuffers(this.window);
    Input.clearLegacyInput();
  }

  public static final class Events {
    private static final Object LOCK = new Object();

    private final List<Resize> resize = new ArrayList<>();
    private final List<Focus> gotFocus = new ArrayList<>();
    private final List<Focus> lostFocus = new ArrayList<>();
    private final List<Key> keyPress = new ArrayList<>();
    private final List<Key> keyRelease = new ArrayList<>();
    private final List<Key> keyRepeat = new ArrayList<>();
    private final List<Char> charPress = new ArrayList<>();
    private final List<Cursor> mouseMove = new ArrayList<>();
    private final List<Click> mousePress = new ArrayList<>();
    private final List<Click> mouseRelease = new ArrayList<>();
    private final List<Scroll> mouseScroll = new ArrayList<>();
    private final List<ControllerState> controllerConnected = new ArrayList<>();
    private final List<ControllerState> controllerDisconnected = new ArrayList<>();
    private final List<OnPressedThisFrame> pressedThisFrame = new ArrayList<>();
    private final List<OnReleasedThisFrame> releasedThisFrame = new ArrayList<>();
    private final List<OnPressedWithRepeatPulse> pressedWithRepeatPulse = new ArrayList<>();
    private final List<Runnable> draw = new ArrayList<>();
    private final List<Runnable> shutdown = new ArrayList<>();
    private final Window window;

    private double mouseX;
    private double mouseY;

    private Events(final Window window) {
      this.window = window;
    }

    private void onResize(final long window, final int width, final int height) {
      synchronized(LOCK) {
        try(final MemoryStack stack = MemoryStack.stackPush()) {
          final FloatBuffer x = stack.mallocFloat(1);
          final FloatBuffer y = stack.mallocFloat(1);
          glfwGetWindowContentScale(window, x, y);
//          this.window.scale = x.get(0);
        }

        this.window.width = (int)(width / this.window.scale);
        this.window.height = (int)(height / this.window.scale);

        this.resize.forEach(cb -> cb.resize(this.window, this.window.width, this.window.height));
      }
    }

    public Resize onResize(final Resize callback) {
      synchronized(LOCK) {
        this.resize.add(callback);
        return callback;
      }
    }

    public void removeOnResize(final Resize callback) {
      synchronized(LOCK) {
        this.resize.remove(callback);
      }
    }

    private void onFocus(final long window, final boolean focus) {
      synchronized(LOCK) {
        this.window.hasFocus = focus;

        if(focus) {
          this.gotFocus.forEach(cb -> cb.focus(this.window));
        } else {
          this.lostFocus.forEach(cb -> cb.focus(this.window));
        }
      }
    }

    public Focus onGotFocus(final Focus callback) {
      synchronized(LOCK) {
        this.gotFocus.add(callback);
        return callback;
      }
    }

    public Focus onLostFocus(final Focus callback) {
      synchronized(LOCK) {
        this.lostFocus.add(callback);
        return callback;
      }
    }

    public void removeOnGotFocus(final Focus callback) {
      synchronized(LOCK) {
        this.gotFocus.remove(callback);
      }
    }

    public void removeOnLostFocus(final Focus callback) {
      synchronized(LOCK) {
        this.lostFocus.remove(callback);
      }
    }

    private void onKey(final long window, final int key, final int scancode, final int action, final int mods) {
      synchronized(LOCK) {
        switch(action) {
          case GLFW_PRESS -> this.keyPress.forEach(cb -> cb.action(this.window, key, scancode, mods));
          case GLFW_RELEASE -> this.keyRelease.forEach(cb -> cb.action(this.window, key, scancode, mods));
          case GLFW_REPEAT -> this.keyRepeat.forEach(cb -> cb.action(this.window, key, scancode, mods));
        }
      }
    }

    private void onChar(final long window, final int codepoint) {
      synchronized(LOCK) {
        this.charPress.forEach(cb -> cb.action(this.window, codepoint));
      }
    }

    private void onMouseMove(final long window, final double x, final double y) {
      synchronized(LOCK) {
        final double scaledX = x / this.window.scale;
        final double scaledY = y / this.window.scale;

        this.mouseX = scaledX;
        this.mouseY = scaledY;
        this.mouseMove.forEach(cb -> cb.action(this.window, scaledX, scaledY));
      }
    }

    private void onMouseButton(final long window, final int button, final int action, final int mods) {
      synchronized(LOCK) {
        switch(action) {
          case GLFW_PRESS -> this.mousePress.forEach(cb -> cb.action(this.window, this.mouseX, this.mouseY, button, mods));
          case GLFW_RELEASE -> this.mouseRelease.forEach(cb -> cb.action(this.window, this.mouseX, this.mouseY, button, mods));
        }
      }
    }

    private void onMouseScroll(final long window, final double deltaX, final double deltaY) {
      synchronized(LOCK) {
        this.mouseScroll.forEach(cb -> cb.action(this.window, deltaX, deltaY));
      }
    }

    private void onInputPressedThisFrame(final InputAction inputAction) {
      synchronized(LOCK) {
        this.pressedThisFrame.forEach(cb -> cb.action(this.window, inputAction));
      }
    }

    private void onInputReleasedThisFrame(final InputAction inputAction) {
      synchronized(LOCK) {
        this.releasedThisFrame.forEach(cb -> cb.action(this.window, inputAction));
      }
    }

    private void onInputPressedWithRepeat(final InputAction inputAction) {
      synchronized(LOCK) {
        this.pressedWithRepeatPulse.forEach(cb -> cb.action(this.window, inputAction));
      }
    }

    public void callInputEvents(final Controller controller) {
      for(int i = 0; i < controller.bindings.size(); i++) {
        final InputBinding binding = controller.bindings.get(i);
        if(binding.getState() == InputState.PRESSED_THIS_FRAME) {
          this.onInputPressedThisFrame(binding.getInputAction());
          this.onInputPressedWithRepeat(binding.getInputAction());
        } else if(binding.getState() == InputState.RELEASED_THIS_FRAME) {
          this.onInputReleasedThisFrame(binding.getInputAction());
        } else if(binding.getState() == InputState.PRESSED_REPEAT) {
          this.onInputPressedWithRepeat(binding.getInputAction());
        }
      }
    }

    private void onControllerConnected(final long window, final int id) {
      synchronized(LOCK) {
        this.controllerConnected.forEach(cb -> cb.action(this.window, id));
      }
    }

    private void onControllerDisconnected(final long window, final int id) {
      synchronized(LOCK) {
        this.controllerDisconnected.forEach(cb -> cb.action(this.window, id));
      }
    }

    public Key onKeyPress(final Key callback) {
      synchronized(LOCK) {
        this.keyPress.add(callback);
        return callback;
      }
    }

    public void removeKeyPress(final Key callback) {
      synchronized(LOCK) {
        this.keyPress.remove(callback);
      }
    }

    public Key onKeyRelease(final Key callback) {
      synchronized(LOCK) {
        this.keyRelease.add(callback);
        return callback;
      }
    }

    public void removeKeyRelease(final Key callback) {
      synchronized(LOCK) {
        this.keyRelease.remove(callback);
      }
    }

    public Key onKeyRepeat(final Key callback) {
      synchronized(LOCK) {
        this.keyRepeat.add(callback);
        return callback;
      }
    }

    public void removeKeyRepeat(final Key callback) {
      synchronized(LOCK) {
        this.keyRepeat.remove(callback);
      }
    }

    public Char onCharPress(final Char callback) {
      synchronized(LOCK) {
        this.charPress.add(callback);
        return callback;
      }
    }

    public void removeCharPress(final Char callback) {
      synchronized(LOCK) {
        this.charPress.remove(callback);
      }
    }

    public Cursor onMouseMove(final Cursor callback) {
      synchronized(LOCK) {
        this.mouseMove.add(callback);
        return callback;
      }
    }

    public void removeMouseMove(final Cursor callback) {
      synchronized(LOCK) {
        this.mouseMove.remove(callback);
      }
    }

    public Click onMousePress(final Click callback) {
      synchronized(LOCK) {
        this.mousePress.add(callback);
        return callback;
      }
    }

    public void removeMousePress(final Click callback) {
      synchronized(LOCK) {
        this.mousePress.remove(callback);
      }
    }

    public Click onMouseRelease(final Click callback) {
      synchronized(LOCK) {
        this.mouseRelease.add(callback);
        return callback;
      }
    }

    public void removeMouseRelease(final Click callback) {
      synchronized(LOCK) {
        this.mouseRelease.remove(callback);
      }
    }

    public Scroll onMouseScroll(final Scroll callback) {
      synchronized(LOCK) {
        this.mouseScroll.add(callback);
        return callback;
      }
    }

    public void removeMouseScroll(final Scroll callback) {
      synchronized(LOCK) {
        this.mouseScroll.remove(callback);
      }
    }

    public OnPressedThisFrame onPressedThisFrame(final OnPressedThisFrame callback) {
      synchronized(LOCK) {
        this.pressedThisFrame.add(callback);
        return callback;
      }
    }

    public void removePressedThisFrame(final OnPressedThisFrame callback) {
      synchronized(LOCK) {
        this.pressedThisFrame.remove(callback);
      }
    }

    public OnReleasedThisFrame onReleasedThisFrame(final OnReleasedThisFrame callback) {
      synchronized(LOCK) {
        this.releasedThisFrame.add(callback);
        return callback;
      }
    }

    public void removeReleasedThisFrame(final OnReleasedThisFrame callback) {
      synchronized(LOCK) {
        this.releasedThisFrame.remove(callback);
      }
    }

    public OnPressedWithRepeatPulse onPressedWithRepeatPulse(final OnPressedWithRepeatPulse callback) {
      synchronized(LOCK) {
        this.pressedWithRepeatPulse.add(callback);
        return callback;
      }
    }

    public void removePressedWithRepeatPulse(final OnPressedWithRepeatPulse callback) {
      synchronized(LOCK) {
        this.pressedWithRepeatPulse.remove(callback);
      }
    }

    public ControllerState onControllerConnected(final ControllerState callback) {
      synchronized(LOCK) {
        this.controllerConnected.add(callback);
        return callback;
      }
    }

    public void removeControllerConnected(final ControllerState callback) {
      synchronized(LOCK) {
        this.controllerConnected.remove(callback);
      }
    }

    public ControllerState onControllerDisconnected(final ControllerState callback) {
      synchronized(LOCK) {
        this.controllerDisconnected.add(callback);
        return callback;
      }
    }

    public void removeControllerDisconnected(final ControllerState callback) {
      synchronized(LOCK) {
        this.controllerDisconnected.remove(callback);
      }
    }

    private void onDraw() {
      synchronized(LOCK) {
        for(int i = 0; i < this.draw.size(); i++) {
          this.draw.get(i).run();
        }
      }
    }

    public Runnable onDraw(final Runnable callback) {
      synchronized(LOCK) {
        this.draw.add(callback);
        return callback;
      }
    }

    public void removeDraw(final Runnable callback) {
      synchronized(LOCK) {
        this.draw.remove(callback);
      }
    }

    private void onShutdown() {
      synchronized(LOCK) {
        for(final Runnable shutdown : this.shutdown) {
          shutdown.run();
        }
      }
    }

    public Runnable onShutdown(final Runnable callback) {
      synchronized(LOCK) {
        this.shutdown.add(callback);
        return callback;
      }
    }

    public void removeShutdown(final Runnable callback) {
      synchronized(LOCK) {
        this.shutdown.remove(callback);
      }
    }

    @FunctionalInterface
    public interface Resize {
      void resize(final Window window, final int width, final int height);
    }

    @FunctionalInterface
    public interface Focus {
      void focus(final Window window);
    }

    @FunctionalInterface
    public interface Key {
      void action(final Window window, final int key, final int scancode, final int mods);
    }

    @FunctionalInterface
    public interface Char {
      void action(final Window window, final int codepoint);
    }

    @FunctionalInterface
    public interface Cursor {
      void action(final Window window, final double x, final double y);
    }

    @FunctionalInterface
    public interface Click {
      void action(final Window window, final double x, final double y, final int button, final int mods);
    }

    @FunctionalInterface
    public interface Scroll {
      void action(final Window window, final double deltaX, final double deltaY);
    }

    @FunctionalInterface
    public interface ControllerState {
      void action(final Window window, final int id);
    }

    @FunctionalInterface
    public interface OnPressedThisFrame {
      void action(final Window window, final InputAction inputAction);
    }

    @FunctionalInterface
    public interface OnReleasedThisFrame {
      void action(final Window window, final InputAction inputAction);
    }

    @FunctionalInterface
    public interface OnPressedWithRepeatPulse {
      void action(final Window window, final InputAction inputAction);
    }
  }
}
