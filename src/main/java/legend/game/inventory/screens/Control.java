package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.game.input.InputAction;

public abstract class Control extends ControlHost {
  private int x;
  private int y;
  private int z;
  private int width;
  private int height;

  private boolean visible = true;
  private boolean acceptsInput = true;

  public void setPos(final int x, final int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return this.x;
  }

  public void setX(final int x) {
    this.x = x;
  }

  public int getY() {
    return this.y;
  }

  public void setY(final int y) {
    this.y = y;
  }

  public int getZ() {
    return this.z;
  }

  public void setZ(final int z) {
    this.z = z;
  }

  public void setSize(final int width, final int height) {
    this.width = width;
    this.height = height;
  }

  public int getWidth() {
    return this.width;
  }

  public void setWidth(final int width) {
    this.width = width;
  }

  public int getHeight() {
    return this.height;
  }

  public void setHeight(final int height) {
    this.height = height;
  }

  public boolean isVisible() {
    return this.visible;
  }

  public void show() {
    this.visible = true;
  }

  public void hide() {
    this.visible = false;
  }

  public void toggleVisibility() {
    this.visible = !this.visible;
  }

  public void setVisibility(final boolean visible) {
    this.visible = visible;
  }

  public boolean acceptsInput() {
    return this.acceptsInput;
  }

  public void acceptInput() {
    this.acceptsInput = true;
  }

  public void ignoreInput() {
    this.acceptsInput = false;
  }

  protected abstract void render(final int x, final int y);

  void renderControl(final int parentX, final int parentY) {
    if(this.visible) {
      this.render(parentX + this.x, parentY + this.y);
      this.renderControls(parentX + this.x, parentY + this.y);
    }
  }

  protected boolean containsPoint(final int x, final int y) {
    return MathHelper.inBox(x, y, this.x, this.y, this.width, this.height);
  }

  protected void hoverIn() {
    if(this.hoverInHandler != null) {
      this.hoverInHandler.hoverIn();
    }
  }

  protected void hoverOut() {
    if(this.hoverOutHandler != null) {
      this.hoverOutHandler.hoverOut();
    }
  }

  protected void gotFocus() {
    if(this.gotFocusHandler != null) {
      this.gotFocusHandler.gotFocus();
    }
  }

  protected void lostFocus() {
    if(this.lostFocusHandler != null) {
      this.lostFocusHandler.lostFocus();
    }
  }

  @Override
  protected void mouseMove(final int x, final int y) {
    super.mouseMove(x, y);

    if(this.mouseMoveHandler != null) {
      this.mouseMoveHandler.mouseMove(x, y);
    }
  }

  @Override
  protected void mouseClick(final int x, final int y, final int button, final int mods) {
    super.mouseClick(x, y, button, mods);

    if(this.mouseClickHandler != null) {
      this.mouseClickHandler.mouseClick(x, y, button, mods);
    }
  }

  @Override
  protected void mouseScroll(final double deltaX, final double deltaY) {
    super.mouseScroll(deltaX, deltaY);

    if(this.mouseScrollHandler != null) {
      this.mouseScrollHandler.mouseScroll(deltaX, deltaY);
    }
  }

  @Override
  protected void keyPress(final int key, final int scancode, final int mods) {
    super.keyPress(key, scancode, mods);

    if(this.keyPressHandler != null) {
      this.keyPressHandler.keyPress(key, scancode, mods);
    }
  }

  @Override
  protected void pressedThisFrame(final InputAction inputAction) {
    super.pressedThisFrame(inputAction);

    if(this.pressedThisFrameHandler != null) {
      this.pressedThisFrameHandler.pressedThisFrame(inputAction);
    }
  }

  @Override
  protected void pressedWithRepeatPulse(final InputAction inputAction) {
    super.pressedWithRepeatPulse(inputAction);

    if(this.pressedWithRepeatPulseHandler != null) {
      this.pressedWithRepeatPulseHandler.pressedWithRepeatPulse(inputAction);
    }
  }

  @Override
  protected void releasedThisFrame(final InputAction inputAction) {
    super.releasedThisFrame(inputAction);

    if(this.releasedThisFrameHandler != null) {
      this.releasedThisFrameHandler.releasedThisFrame(inputAction);
    }
  }

  public void onHoverIn(final HoverIn handler) {
    this.hoverInHandler = handler;
  }

  public void onHoverOut(final HoverOut handler) {
    this.hoverOutHandler = handler;
  }

  public void onGotFocus(final GotFocus handler) {
    this.gotFocusHandler = handler;
  }

  public void onLostFocus(final LostFocus handler) {
    this.lostFocusHandler = handler;
  }

  public void onMouseMove(final MouseMove handler) {
    this.mouseMoveHandler = handler;
  }

  public void onMouseClick(final MouseClick handler) {
    this.mouseClickHandler = handler;
  }

  public void onMouseScroll(final MouseScroll handler) {
    this.mouseScrollHandler = handler;
  }

  public void onKeyPress(final KeyPress handler) {
    this.keyPressHandler = handler;
  }

  public void onPressedThisFrame(final PressedThisFrame handler) {
    this.pressedThisFrameHandler = handler;
  }

  public void onPressedWithRepeatPulse(final PressedWithRepeatPulse handler) {
    this.pressedWithRepeatPulseHandler = handler;
  }

  public void onReleasedThisFrame(final ReleasedThisFrame handler) {
    this.releasedThisFrameHandler = handler;
  }

  private HoverIn hoverInHandler;
  private HoverOut hoverOutHandler;
  private GotFocus gotFocusHandler;
  private LostFocus lostFocusHandler;
  private MouseMove mouseMoveHandler;
  private MouseClick mouseClickHandler;
  private MouseScroll mouseScrollHandler;
  private KeyPress keyPressHandler;
  private PressedThisFrame pressedThisFrameHandler;
  private PressedWithRepeatPulse pressedWithRepeatPulseHandler;
  private ReleasedThisFrame releasedThisFrameHandler;

  @FunctionalInterface public interface HoverIn { void hoverIn(); }
  @FunctionalInterface public interface HoverOut { void hoverOut(); }
  @FunctionalInterface public interface GotFocus { void gotFocus(); }
  @FunctionalInterface public interface LostFocus { void lostFocus(); }
  @FunctionalInterface public interface MouseMove { void mouseMove(final int x, final int y); }
  @FunctionalInterface public interface MouseClick { void mouseClick(final int x, final int y, final int button, final int mds); }
  @FunctionalInterface public interface MouseScroll { void mouseScroll(final double deltaX, final double deltaY); }
  @FunctionalInterface public interface KeyPress { void keyPress(final int key, final int scancode, final int mods); }
  @FunctionalInterface public interface PressedThisFrame { void pressedThisFrame(final InputAction inputAction); }
  @FunctionalInterface public interface PressedWithRepeatPulse { void pressedWithRepeatPulse(final InputAction inputAction); }
  @FunctionalInterface public interface ReleasedThisFrame { void releasedThisFrame(final InputAction inputAction); }
}
