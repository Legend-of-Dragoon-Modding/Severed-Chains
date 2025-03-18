package legend.game.inventory.screens;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import legend.core.opengl.SubmapWidescreenMode;
import legend.core.platform.Window;
import legend.core.platform.WindowEvents;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputKey;
import legend.core.platform.input.InputMod;
import legend.game.modding.coremod.CoreMod;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment_8002.uploadRenderables;

public class MenuStack {
  private final Deque<MenuScreen> screens = new LinkedList<>();

  private WindowEvents.Cursor onMouseMove;
  private WindowEvents.Click onMousePress;
  private WindowEvents.Click onMouseRelease;
  private WindowEvents.Scroll onMouseScroll;
  private WindowEvents.KeyPressed onKeyPress;
  private WindowEvents.Char onCharPress;
  private WindowEvents.InputActionPressed onInputActionPressed;
  private WindowEvents.InputActionReleased onInputActionReleased;

  private final Int2ObjectMap<Point2D> mousePressCoords = new Int2ObjectOpenHashMap<>();

  private double scrollAccumulatorX;
  private double scrollAccumulatorY;

  public void pushScreen(final MenuScreen screen) {
    if(this.screens.isEmpty()) {
      this.registerInputHandlers();
    }

    screen.setStack(this);
    this.screens.push(screen);
  }

  public void popScreen() {
    final MenuScreen screen = this.screens.pop();
    screen.setStack(null);
    screen.delete();

    if(this.screens.isEmpty()) {
      this.removeInputHandlers();
    }
  }

  public void reset() {
    while(!this.screens.isEmpty()) {
      this.popScreen();
    }
  }

  public void render() {
    int scrollX = 0;
    int scrollY = 0;

    if(this.scrollAccumulatorX >= 1.0d) {
      this.scrollAccumulatorX -= 1.0d;
      scrollX = 1;
    }

    if(this.scrollAccumulatorX <= -1.0d) {
      this.scrollAccumulatorX += 1.0d;
      scrollX = -1;
    }

    if(this.scrollAccumulatorY >= 1.0d) {
      this.scrollAccumulatorY -= 1.0d;
      scrollY = 1;
    }

    if(this.scrollAccumulatorY <= -1.0d) {
      this.scrollAccumulatorY += 1.0d;
      scrollY = -1;
    }

    if(scrollX != 0 || scrollY != 0) {
      final int finalScrollX = scrollX;
      final int finalScrollY = scrollY;
      this.input(screen -> screen.mouseScroll(finalScrollX, finalScrollY));
    }

    final Iterator<MenuScreen> it = this.screens.iterator();

    if(it.hasNext()) {
      this.propagate(it, MenuScreen::renderScreen, MenuScreen::propagateRender, true);
    }

    //TODO temporary until everything is moved over to controls and no longer uses the LOD system
    uploadRenderables();
  }

  private void input(final Consumer<MenuScreen> method) {
    final Iterator<MenuScreen> it = this.screens.iterator();

    if(it.hasNext()) {
      this.propagate(it, method, MenuScreen::propagateInput, false);
    }
  }

  private void propagate(final Iterator<MenuScreen> it, final Consumer<MenuScreen> method, final Predicate<MenuScreen> shouldPropagate, final boolean lowerFirst) {
    final MenuScreen screen = it.next();

    if(!lowerFirst) {
      method.accept(screen);
    }

    // Propagate to screen below this one?
    if(shouldPropagate.test(screen) && it.hasNext()) {
      this.propagate(it, method, shouldPropagate, lowerFirst);
    }

    if(lowerFirst) {
      method.accept(screen);
    }
  }

  public void registerInputHandlers() {
    this.onMouseMove = RENDERER.events().onMouseMove(this::mouseMove);
    this.onMousePress = RENDERER.events().onMousePress(this::mousePress);
    this.onMouseRelease = RENDERER.events().onMouseRelease(this::mouseRelease);
    this.onMouseScroll = RENDERER.events().onMouseScroll(this::mouseScroll);
    this.onKeyPress = RENDERER.events().onKeyPress(this::keyPress);
    this.onCharPress = RENDERER.events().onCharPress(this::charPress);
    this.onInputActionPressed = RENDERER.events().onInputActionPressed(this::inputActionPressed);
    this.onInputActionReleased = RENDERER.events().onInputActionReleased(this::inputActionReleased);
  }

  public void removeInputHandlers() {
    RENDERER.events().removeMouseMove(this.onMouseMove);
    RENDERER.events().removeMousePress(this.onMousePress);
    RENDERER.events().removeMouseRelease(this.onMouseRelease);
    RENDERER.events().removeMouseScroll(this.onMouseScroll);
    RENDERER.events().removeKeyPress(this.onKeyPress);
    RENDERER.events().removeCharPress(this.onCharPress);
    RENDERER.events().removeInputActionPressed(this.onInputActionPressed);
    RENDERER.events().removeInputActionReleased(this.onInputActionReleased);
  }

  private void mouseMove(final Window window, final double x, final double y) {
    if(CONFIG.getConfig(CoreMod.LEGACY_WIDESCREEN_MODE_CONFIG.get()) == SubmapWidescreenMode.STRETCHED) {
      this.input(screen -> screen.mouseMove((int)(x / window.getWidth() * RENDERER.getNativeWidth()), (int)(y / window.getHeight() * RENDERER.getNativeHeight())));
      return;
    }

    final float aspect = 4.0f / 3.0f;

    float w = window.getWidth();
    float h = w / aspect;

    if(h > window.getHeight()) {
      h = window.getHeight();
      w = h * aspect;
    }

    final float left = (window.getWidth() - w) / 2;
    final float top = (window.getHeight() - h) / 2;

    final float scaleX = w / RENDERER.getNativeWidth();
    final float scaleY = h / RENDERER.getNativeHeight();

    this.input(screen -> screen.mouseMove((int)((x - left) / scaleX), (int)((y - top) / scaleY)));
  }

  private void mousePress(final Window window, final double x, final double y, final int button, final Set<InputMod> mods) {
    this.mousePressCoords.put(button, new Point2D(x, y));
  }

  private void mouseRelease(final Window window, final double x, final double y, final int button, final Set<InputMod> mods) {
    final Point2D point = this.mousePressCoords.remove(button);

    if(point != null && Math.abs(point.x - x) < 4 && Math.abs(point.y - y) < 4) {
      if(CONFIG.getConfig(CoreMod.LEGACY_WIDESCREEN_MODE_CONFIG.get()) == SubmapWidescreenMode.STRETCHED) {
        this.input(screen -> screen.mouseClick((int)(x / window.getWidth() * RENDERER.getNativeWidth()), (int)(y / window.getHeight() * RENDERER.getNativeHeight()), button, mods));
        return;
      }

      final float aspect = 4.0f / 3.0f;

      float w = window.getWidth();
      float h = w / aspect;

      if(h > window.getHeight()) {
        h = window.getHeight();
        w = h * aspect;
      }

      final float left = (window.getWidth() - w) / 2;
      final float top = (window.getHeight() - h) / 2;

      final float scaleX = w / GPU.getDisplayTextureWidth();
      final float scaleY = h / GPU.getDisplayTextureHeight();

      this.input(screen -> screen.mouseClick((int)((x - left) / scaleX), (int)((y - top) / scaleY), button, mods));
    }
  }

  private void mouseScroll(final Window window, final double deltaX, final double deltaY) {
    if(this.scrollAccumulatorX < 0 && deltaX > 0 || this.scrollAccumulatorX > 0 && deltaX < 0) {
      this.scrollAccumulatorX = 0;
    }

    if(this.scrollAccumulatorY < 0 && deltaY > 0 || this.scrollAccumulatorY > 0 && deltaY < 0) {
      this.scrollAccumulatorY = 0;
    }

    this.scrollAccumulatorX += deltaX;
    this.scrollAccumulatorY += deltaY;

    this.input(screen -> screen.mouseScrollHighRes(deltaX, deltaY));
  }

  private void keyPress(final Window window, final InputKey key, final InputKey scancode, final Set<InputMod> mods, final boolean repeat) {
    this.input(screen -> screen.keyPress(key, scancode, mods, repeat));
  }

  private void charPress(final Window window, final int codepoint) {
    this.input(screen -> screen.charPress(codepoint));
  }

  private void inputActionPressed(final Window window, final InputAction action, final boolean repeat) {
    this.input(screen -> screen.inputActionPressed(action, repeat));
  }

  private void inputActionReleased(final Window window, final InputAction action) {
    this.input(screen -> screen.inputActionReleased(action));
  }

  private record Point2D(double x, double y) { }
}
