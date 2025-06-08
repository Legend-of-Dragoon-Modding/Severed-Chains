package legend.core.platform;

import legend.core.Config;
import legend.core.opengl.Action;
import legend.core.platform.input.InputClass;
import legend.core.platform.input.InputMod;
import legend.game.modding.coremod.CoreMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.sdl.SDL_DisplayMode;
import org.lwjgl.sdl.SDL_Rect;
import org.lwjgl.sdl.SDL_Surface;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.Set;

import static legend.core.GameEngine.CONFIG;
import static org.lwjgl.sdl.SDLError.SDL_GetError;
import static org.lwjgl.sdl.SDLKeyboard.SDL_StartTextInput;
import static org.lwjgl.sdl.SDLKeyboard.SDL_StopTextInput;
import static org.lwjgl.sdl.SDLMouse.SDL_CreateSystemCursor;
import static org.lwjgl.sdl.SDLMouse.SDL_DestroyCursor;
import static org.lwjgl.sdl.SDLMouse.SDL_GetDefaultCursor;
import static org.lwjgl.sdl.SDLMouse.SDL_HideCursor;
import static org.lwjgl.sdl.SDLMouse.SDL_SYSTEM_CURSOR_POINTER;
import static org.lwjgl.sdl.SDLMouse.SDL_SetCursor;
import static org.lwjgl.sdl.SDLMouse.SDL_SetWindowRelativeMouseMode;
import static org.lwjgl.sdl.SDLMouse.SDL_ShowCursor;
import static org.lwjgl.sdl.SDLPixels.SDL_PIXELFORMAT_ARGB8888;
import static org.lwjgl.sdl.SDLSurface.SDL_CreateSurfaceFrom;
import static org.lwjgl.sdl.SDLSurface.SDL_DestroySurface;
import static org.lwjgl.sdl.SDLVideo.SDL_CreateWindow;
import static org.lwjgl.sdl.SDLVideo.SDL_DestroyWindow;
import static org.lwjgl.sdl.SDLVideo.SDL_GL_CONTEXT_DEBUG_FLAG;
import static org.lwjgl.sdl.SDLVideo.SDL_GL_CONTEXT_FLAGS;
import static org.lwjgl.sdl.SDLVideo.SDL_GL_CONTEXT_MAJOR_VERSION;
import static org.lwjgl.sdl.SDLVideo.SDL_GL_CONTEXT_MINOR_VERSION;
import static org.lwjgl.sdl.SDLVideo.SDL_GL_CONTEXT_PROFILE_CORE;
import static org.lwjgl.sdl.SDLVideo.SDL_GL_CONTEXT_PROFILE_MASK;
import static org.lwjgl.sdl.SDLVideo.SDL_GL_CreateContext;
import static org.lwjgl.sdl.SDLVideo.SDL_GL_DestroyContext;
import static org.lwjgl.sdl.SDLVideo.SDL_GL_MakeCurrent;
import static org.lwjgl.sdl.SDLVideo.SDL_GL_SetAttribute;
import static org.lwjgl.sdl.SDLVideo.SDL_GL_SetSwapInterval;
import static org.lwjgl.sdl.SDLVideo.SDL_GL_SwapWindow;
import static org.lwjgl.sdl.SDLVideo.SDL_GetDesktopDisplayMode;
import static org.lwjgl.sdl.SDLVideo.SDL_GetDisplayBounds;
import static org.lwjgl.sdl.SDLVideo.SDL_GetDisplays;
import static org.lwjgl.sdl.SDLVideo.SDL_GetPrimaryDisplay;
import static org.lwjgl.sdl.SDLVideo.SDL_GetWindowSize;
import static org.lwjgl.sdl.SDLVideo.SDL_RestoreWindow;
import static org.lwjgl.sdl.SDLVideo.SDL_SetWindowBordered;
import static org.lwjgl.sdl.SDLVideo.SDL_SetWindowIcon;
import static org.lwjgl.sdl.SDLVideo.SDL_SetWindowMinimumSize;
import static org.lwjgl.sdl.SDLVideo.SDL_SetWindowPosition;
import static org.lwjgl.sdl.SDLVideo.SDL_SetWindowSize;
import static org.lwjgl.sdl.SDLVideo.SDL_SetWindowTitle;
import static org.lwjgl.sdl.SDLVideo.SDL_ShowWindow;
import static org.lwjgl.sdl.SDLVideo.SDL_WINDOW_HIDDEN;
import static org.lwjgl.sdl.SDLVideo.SDL_WINDOW_OPENGL;
import static org.lwjgl.sdl.SDLVideo.SDL_WINDOW_RESIZABLE;
import static org.lwjgl.stb.STBImage.stbi_failure_reason;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memFree;

public class SdlWindow extends Window {
  private static final Logger LOGGER = LogManager.getFormatterLogger(SdlWindow.class);

  private final SdlPlatformManager manager;
  private final long window;
  private final long context;

  private int width;
  private int height;
  boolean hasFocus;
  private boolean shouldClose;

  /** Currently-held mods */
  final Set<InputMod> mods = EnumSet.noneOf(InputMod.class);
  /** The last way the user interacted with this window */
  InputClass currentInputClass = InputClass.GAMEPAD;

  private int monitor;
  private SDL_DisplayMode vidMode;

  private final long pointerCursor;

  private final Action render;

  public SdlWindow(final SdlPlatformManager manager, final String title, final int width, final int height) {
    this.manager = manager;
    this.monitor = this.getMonitorFromConfig();
    this.vidMode = SDL_GetDesktopDisplayMode(this.monitor);

    if(this.vidMode == null) {
      throw new RuntimeException("Failed to get video mode");
    }

    SDL_GL_SetAttribute(SDL_GL_CONTEXT_MAJOR_VERSION, 3);
    SDL_GL_SetAttribute(SDL_GL_CONTEXT_MINOR_VERSION, 3);
    SDL_GL_SetAttribute(SDL_GL_CONTEXT_PROFILE_MASK, SDL_GL_CONTEXT_PROFILE_CORE);

    if("true".equals(System.getenv("opengl_debug"))) {
      SDL_GL_SetAttribute(SDL_GL_CONTEXT_FLAGS, SDL_GL_CONTEXT_DEBUG_FLAG);
    }

    this.window = SDL_CreateWindow(title, width,  height, SDL_WINDOW_OPENGL | SDL_WINDOW_HIDDEN | SDL_WINDOW_RESIZABLE);

    if(this.window == NULL) {
      throw new RuntimeException("Failed to create the SDL window: " + SDL_GetError());
    }

    SDL_SetWindowMinimumSize(this.window, 320, 240);

    this.pointerCursor = SDL_CreateSystemCursor(SDL_SYSTEM_CURSOR_POINTER);

    this.context = SDL_GL_CreateContext(this.window);

    if(this.context == NULL) {
      throw new RuntimeException("Failed to create SDL OpenGL context: " + SDL_GetError());
    }

    if(!SDL_GL_MakeCurrent(this.window, this.context)) {
      throw new RuntimeException("Failed to make OpenGL context current: " + SDL_GetError());
    }

    SDL_GL_SetSwapInterval(0);

    this.render = this.manager.addAction(new Action(this::tick, 60));
  }

  @Override
  protected void destroy() {
    this.manager.removeAction(this.render);
    this.events().onClose();
    SDL_DestroyCursor(this.pointerCursor);
    SDL_GL_DestroyContext(this.context);
    SDL_DestroyWindow(this.window);
  }

  long getWindowPtr() {
    return this.window;
  }

  void updateSize() {
    try(final MemoryStack stack = MemoryStack.stackPush()) {
      final IntBuffer x = stack.mallocInt(1);
      final IntBuffer y = stack.mallocInt(1);
      SDL_GetWindowSize(this.window, x, y);

      this.width = x.get(0);
      this.height = y.get(0);
    }
  }

  private int getMonitorFromConfig() {
    if(CONFIG.getConfig(CoreMod.FULLSCREEN_CONFIG.get())) {
      final int monitorIndex = CONFIG.getConfig(CoreMod.MONITOR_CONFIG.get());

      final IntBuffer monitorPtrs = SDL_GetDisplays();

      if(monitorIndex >= 0 && monitorIndex < monitorPtrs.limit()) {
        return monitorPtrs.get(monitorIndex);
      }
    }

    return SDL_GetPrimaryDisplay();
  }

  @Override
  protected boolean shouldClose() {
    return this.shouldClose;
  }

  @Override
  public void show() {
    SDL_ShowWindow(this.window);
    this.updateSize();
    this.events().onResize(this.width, this.height);
  }

  @Override
  public void close() {
    this.shouldClose = true;
  }

  @Override
  public void updateMonitor() {
    this.monitor = this.getMonitorFromConfig();

    if(CONFIG.getConfig(CoreMod.FULLSCREEN_CONFIG.get())) {
      this.makeFullscreen();
    }
  }

  @Override
  public void makeFullscreen() {
    this.monitor = this.getMonitorFromConfig();
    this.vidMode = SDL_GetDesktopDisplayMode(this.monitor);
    this.err(SDL_RestoreWindow(this.window), "RestoreWindow");
    this.err(SDL_SetWindowBordered(this.window, false), "SetWindowBordered");
    this.moveToMonitor();

    // Overscan by 1 pixel to stop Windows from putting it into exclusive fullscreen
    this.err(SDL_SetWindowSize(this.window, this.vidMode.w(), this.vidMode.h() + 1), "SetWindowSize");
  }

  @Override
  public void makeWindowed() {
    this.err(SDL_SetWindowBordered(this.window, true), "SetWindowBordered");
    this.err(SDL_SetWindowSize(this.window, Config.windowWidth(), Config.windowHeight()), "SetWindowSize");
    this.centerWindow();
  }

  @Override
  public void centerWindow() {
    try(final MemoryStack stack = stackPush()) {
      final IntBuffer pWidth = stack.mallocInt(1);
      final IntBuffer pHeight = stack.mallocInt(1);
      final SDL_Rect displayRect = SDL_Rect.malloc(stack);

      this.err(SDL_GetWindowSize(this.window, pWidth, pHeight), "GetWindowSize");
      this.err(SDL_GetDisplayBounds(this.monitor, displayRect), "GetDisplayBounds");

      this.err(SDL_SetWindowPosition(
        this.window,
        displayRect.x() + (displayRect.w() - pWidth.get(0)) / 2,
        displayRect.y() + (displayRect.h() - pHeight.get(0)) / 2
      ), "SetWindowPosition");
    }
  }

  private void moveToMonitor() {
    try(final MemoryStack stack = stackPush()) {
      final SDL_Rect displayRect = SDL_Rect.malloc(stack);
      this.err(SDL_GetDisplayBounds(this.monitor, displayRect), "GetDisplayBounds");
      this.err(SDL_SetWindowPosition(this.window, displayRect.x(), displayRect.y()), "SetWindowPosition");
    }
  }

  private void err(final boolean status, final String action) {
    if(!status) {
      LOGGER.error("SDL error (%s): %s", action, SDL_GetError());
    }
  }

  @Override
  public void setTitle(final String title) {
    SDL_SetWindowTitle(this.window, title);
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
  public boolean hasFocus() {
    return this.hasFocus;
  }

  @Override
  public InputClass getInputClass() {
    return this.currentInputClass;
  }

  @Override
  public void startTextInput() {
    SDL_StartTextInput(this.window);
  }

  @Override
  public void stopTextInput() {
    SDL_StopTextInput(this.window);
  }

  @Override
  public void disableCursor() {
    SDL_SetWindowRelativeMouseMode(this.window, true);
  }

  @Override
  public void showCursor() {
    SDL_SetWindowRelativeMouseMode(this.window, false);
    SDL_ShowCursor();
  }

  @Override
  public void hideCursor() {
    SDL_HideCursor();
  }

  @Override
  public void useNormalCursor() {
    SDL_SetCursor(SDL_GetDefaultCursor());
  }

  @Override
  public void usePointerCursor() {
    SDL_SetCursor(this.pointerCursor);
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

      final SDL_Surface surface = SDL_CreateSurfaceFrom(w.get(0), h.get(0), SDL_PIXELFORMAT_ARGB8888, data, w.get(0) * 4);

      if(surface != null) {
        SDL_SetWindowIcon(this.window, surface);
        SDL_DestroySurface(surface);
      } else {
        LOGGER.error("Failed to set window icon: %s", SDL_GetError());
      }

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

  private void tick() {
    SDL_GL_SwapWindow(this.window);
    this.events().onDraw();
  }
}
