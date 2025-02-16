package legend.game.inventory.screens;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import legend.core.opengl.SubmapWidescreenMode;
import legend.core.opengl.Window;
import legend.game.input.InputAction;
import legend.game.modding.coremod.CoreMod;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment_8002.uploadRenderables;

public class MenuStack {
  private final Deque<MenuScreen> screens = new LinkedList<>();

  private Window.Events.Cursor onMouseMove;
  private Window.Events.Click onMousePress;
  private Window.Events.Click onMouseRelease;
  private Window.Events.Scroll onMouseScroll;
  private Window.Events.Key onKeyPress;
  private Window.Events.Key onKeyRepeat;
  private Window.Events.Char onCharPress;

  private Window.Events.OnPressedThisFrame onPressedThisFrame;
  private Window.Events.OnReleasedThisFrame onReleasedThisFrame;

  private Window.Events.OnPressedWithRepeatPulse onPressedWithRepeatPulse;

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
    this.onKeyRepeat = RENDERER.events().onKeyRepeat(this::keyPress);
    this.onCharPress = RENDERER.events().onCharPress(this::charPress);
    this.onPressedThisFrame = RENDERER.events().onPressedThisFrame(this::pressedThisFrame);
    this.onReleasedThisFrame = RENDERER.events().onReleasedThisFrame(this::releasedThisFrame);
    this.onPressedWithRepeatPulse = RENDERER.events().onPressedWithRepeatPulse(this::pressedWithRepeatPulse);
  }

  public void removeInputHandlers() {
    RENDERER.events().removeMouseMove(this.onMouseMove);
    RENDERER.events().removeMousePress(this.onMousePress);
    RENDERER.events().removeMouseRelease(this.onMouseRelease);
    RENDERER.events().removeMouseScroll(this.onMouseScroll);
    RENDERER.events().removeKeyPress(this.onKeyPress);
    RENDERER.events().removeKeyRepeat(this.onKeyRepeat);
    RENDERER.events().removeCharPress(this.onCharPress);
    RENDERER.events().removePressedThisFrame(this.onPressedThisFrame);
    RENDERER.events().removeReleasedThisFrame(this.onReleasedThisFrame);
    RENDERER.events().removePressedWithRepeatPulse(this.onPressedWithRepeatPulse);
  }

  private void mouseMove(final Window window, final double x, final double y) {
    if(CONFIG.getConfig(CoreMod.LEGACY_WIDESCREEN_MODE_CONFIG.get()) == SubmapWidescreenMode.STRETCHED) {
      this.input(screen -> screen.mouseMove((int)(x / window.getWidth() * RENDERER.getProjectionWidth()), (int)(y / window.getHeight() * RENDERER.getProjectionHeight())));
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

    final float scaleX = w / RENDERER.getProjectionWidth();
    final float scaleY = h / RENDERER.getProjectionHeight();

    this.input(screen -> screen.mouseMove((int)((x - left) / scaleX), (int)((y - top) / scaleY)));
  }

  private void mousePress(final Window window, final double x, final double y, final int button, final int mods) {
    this.mousePressCoords.put(button, new Point2D(x, y));
  }

  private void mouseRelease(final Window window, final double x, final double y, final int button, final int mods) {
    final Point2D point = this.mousePressCoords.remove(button);

    if(point != null && Math.abs(point.x - x) < 4 && Math.abs(point.y - y) < 4) {
      if(CONFIG.getConfig(CoreMod.LEGACY_WIDESCREEN_MODE_CONFIG.get()) == SubmapWidescreenMode.STRETCHED) {
        this.input(screen -> screen.mouseClick((int)(x / window.getWidth() * RENDERER.getProjectionWidth()), (int)(y / window.getHeight() * RENDERER.getProjectionHeight()), button, mods));
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

  private void keyPress(final Window window, final int key, final int scancode, final int mods) {
    this.input(screen -> screen.keyPress(key, scancode, mods));
  }

  private void pressedThisFrame(final Window window, final InputAction inputAction) {
    this.input(screen -> screen.pressedThisFrame(inputAction));
  }

  private void charPress(final Window window, final int codepoint) {
    this.input(screen -> screen.charPress(codepoint));
  }

  private void pressedWithRepeatPulse(final Window window, final InputAction inputAction) {
    this.input(screen -> screen.pressedWithRepeatPulse(inputAction));
  }

  private void releasedThisFrame(final Window window, final InputAction inputAction) {
    this.input(screen -> screen.releasedThisFrame(inputAction));
  }

  private record Point2D(double x, double y) { }
}
