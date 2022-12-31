package legend.core.opengl;

import legend.core.Config;
import legend.core.DebugHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_CONNECTED;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_DISABLED;
import static org.lwjgl.glfw.GLFW.GLFW_CURSOR_NORMAL;
import static org.lwjgl.glfw.GLFW.GLFW_DISCONNECTED;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_DEBUG_CONTEXT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_SCALE_TO_MONITOR;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetClipboardString;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowContentScale;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetCharCallback;
import static org.lwjgl.glfw.GLFW.glfwSetClipboardString;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.glfw.GLFW.glfwSetJoystickCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
  private static final Logger LOGGER = LogManager.getLogger(Window.class.getName());

  private static final GLFWErrorCallback ERROR_CALLBACK;

  static {
    LOGGER.info("Initialising LWJGL version {}", Version.getVersion());

    ERROR_CALLBACK = GLFWErrorCallback.createPrint(System.err).set();

    if(!glfwInit()) {
      throw new IllegalStateException("Unable to initialize GLFW");
    }

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      LOGGER.info("Shutting down...");

      glfwTerminate();
      ERROR_CALLBACK.free();

      try {
        Config.save();
      } catch (final IOException e) {
        System.err.println("Failed to save config");
      }
    }));
  }

  private final long window;
  private Runnable eventPoller = GLFW::glfwPollEvents;

  public final Events events = new Events(this);

  private int width;
  private int height;
  private float scale = 1.0f;

  private int fpsLimit = Integer.MAX_VALUE;

  public Window(final String title, final int width, final int height) {
    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

    glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
    glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
    glfwWindowHint(GLFW_SCALE_TO_MONITOR, GLFW_TRUE);

    if("true".equals(System.getenv("opengl_debug"))) {
      glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
    }

    this.window = glfwCreateWindow(width, height, title, NULL, NULL);

    if(this.window == NULL) {
      throw new RuntimeException("Failed to create the GLFW window");
    }

    glfwSetWindowSizeCallback(this.window, this.events::onResize);
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

    try(final MemoryStack stack = stackPush()) {
      final IntBuffer pWidth = stack.mallocInt(1);
      final IntBuffer pHeight = stack.mallocInt(1);

      glfwGetWindowSize(this.window, pWidth, pHeight);

      final GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());

      if(vidMode == null) {
        throw new RuntimeException("Failed to get video mode");
      }

      glfwSetWindowPos(
        this.window,
        (vidMode.width() - pWidth.get(0)) / 2,
        (vidMode.height() - pHeight.get(0)) / 2
      );
    }
  }

  void makeContextCurrent() {
    glfwMakeContextCurrent(this.window);
    glfwSwapInterval(1);
  }

  public void setEventPoller(final Runnable poller) {
    this.eventPoller = poller;
  }

  public void setFpsLimit(final int limit) {
    this.fpsLimit = limit;
  }

  public int getFpsLimit() {
    return this.fpsLimit;
  }

  public void show() {
    glfwShowWindow(this.window);

    // Well this is bad code
    if(!System.getProperty("os.name").startsWith("Windows")) {
      try(final MemoryStack stack = MemoryStack.stackPush()) {
        final IntBuffer x = stack.mallocInt(1);
        final IntBuffer y = stack.mallocInt(1);
        glfwGetWindowSize(this.window, x, y);

        final FloatBuffer scaleX = stack.mallocFloat(1);
        final FloatBuffer scaleY = stack.mallocFloat(1);
        glfwGetWindowContentScale(this.window, scaleX, scaleY);

        this.events.onResize(this.window, (int)(x.get() / scaleX.get()), (int)(y.get() / scaleY.get()));
      }
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
    return (int)(this.width / this.scale);
  }

  public int getHeight() {
    return (int)(this.height / this.scale);
  }

  public float getScale() {
    return this.scale;
  }

  public void run() {
    long timer = System.nanoTime() + 1_000_000_000 / this.fpsLimit;

    while(!glfwWindowShouldClose(this.window)) {
      this.eventPoller.run();
      this.events.onDraw();

      glfwSwapBuffers(this.window);

      if(this.fpsLimit != Integer.MAX_VALUE) {
        while(System.nanoTime() <= timer) {
          DebugHelper.sleep(0);
        }

        timer = System.nanoTime() + 1_000_000_000 / this.fpsLimit;
      }
    }

    this.events.onShutdown();

    glfwFreeCallbacks(this.window);
    glfwDestroyWindow(this.window);
  }

  public static final class Events {
    private static final Object LOCK = new Object();

    private final List<Resize> resize = new ArrayList<>();
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
        this.window.width = width;
        this.window.height = height;

        try(final MemoryStack stack = MemoryStack.stackPush()) {
          final FloatBuffer x = stack.mallocFloat(1);
          final FloatBuffer y = stack.mallocFloat(1);
          glfwGetWindowContentScale(window, x, y);
          this.window.scale = x.get(0);
        }

        this.resize.forEach(cb -> cb.resize(this.window, width, height));
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
        for(final Runnable draw : this.draw) {
          draw.run();
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

    @FunctionalInterface public interface Resize {
      void resize(final Window window, final int width, final int height);
    }

    @FunctionalInterface public interface Key {
      void action(final Window window, final int key, final int scancode, final int mods);
    }

    @FunctionalInterface public interface Char {
      void action(final Window window, final int codepoint);
    }

    @FunctionalInterface public interface Cursor {
      void action(final Window window, final double x, final double y);
    }

    @FunctionalInterface public interface Click {
      void action(final Window window, final double x, final double y, final int button, final int mods);
    }

    @FunctionalInterface public interface Scroll {
      void action(final Window window, final double deltaX, final double deltaY);
    }

    @FunctionalInterface public interface ControllerState {
      void action(final Window window, final int id);
    }
  }
}
