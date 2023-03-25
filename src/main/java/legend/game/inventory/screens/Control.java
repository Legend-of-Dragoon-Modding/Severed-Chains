package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.game.SItem;
import legend.game.input.InputAction;

public abstract class Control extends ControlHost {
  private int x;
  private int y;
  private int z;
  private int width;
  private int height;

  private boolean visible = true;
  private boolean acceptsInput = true;
  private boolean disabled;

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
    this.onResize();
  }

  public int getWidth() {
    return this.width;
  }

  public void setWidth(final int width) {
    this.width = width;
    this.onResize();
  }

  public int getHeight() {
    return this.height;
  }

  public void setHeight(final int height) {
    this.height = height;
    this.onResize();
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

  public boolean isDisabled() {
    return this.disabled;
  }

  public void disable() {
    this.disabled = true;
  }

  public void enable() {
    this.disabled = true;
  }

  public void setDisabled(final boolean disabled) {
    this.disabled = disabled;
  }

  protected void onResize() {

  }

  protected abstract void render(final int x, final int y);

  protected void renderNumber(final int x, final int y, final int value, final int digitCount) {
    SItem.renderNumber(x, y, value, 0x2, digitCount);
  }

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
  protected InputPropagation mouseMove(final int x, final int y) {
    if(this.isDisabled()) {
      return InputPropagation.PROPAGATE;
    }

    if(super.mouseMove(x, y) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.mouseMoveHandler != null) {
      return this.mouseMoveHandler.mouseMove(x, y);
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation mouseClick(final int x, final int y, final int button, final int mods) {
    if(this.isDisabled()) {
      return InputPropagation.PROPAGATE;
    }

    if(super.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.mouseClickHandler != null) {
      return this.mouseClickHandler.mouseClick(x, y, button, mods);
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation mouseScroll(final double deltaX, final double deltaY) {
    if(this.isDisabled()) {
      return InputPropagation.PROPAGATE;
    }

    if(super.mouseScroll(deltaX, deltaY) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.mouseScrollHandler != null) {
      return this.mouseScrollHandler.mouseScroll(deltaX, deltaY);
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation keyPress(final int key, final int scancode, final int mods) {
    if(this.isDisabled()) {
      return InputPropagation.PROPAGATE;
    }

    if(super.keyPress(key, scancode, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.keyPressHandler != null) {
      return this.keyPressHandler.keyPress(key, scancode, mods);
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(this.isDisabled()) {
      return InputPropagation.PROPAGATE;
    }

    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.pressedThisFrameHandler != null) {
      return this.pressedThisFrameHandler.pressedThisFrame(inputAction);
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation pressedWithRepeatPulse(final InputAction inputAction) {
    if(this.isDisabled()) {
      return InputPropagation.PROPAGATE;
    }

    if(super.pressedWithRepeatPulse(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.pressedWithRepeatPulseHandler != null) {
      return this.pressedWithRepeatPulseHandler.pressedWithRepeatPulse(inputAction);
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation releasedThisFrame(final InputAction inputAction) {
    if(this.isDisabled()) {
      return InputPropagation.PROPAGATE;
    }

    if(super.releasedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.releasedThisFrameHandler != null) {
      return this.releasedThisFrameHandler.releasedThisFrame(inputAction);
    }

    return InputPropagation.PROPAGATE;
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
  @FunctionalInterface public interface MouseMove { InputPropagation mouseMove(final int x, final int y); }
  @FunctionalInterface public interface MouseClick { InputPropagation mouseClick(final int x, final int y, final int button, final int mods); }
  @FunctionalInterface public interface MouseScroll { InputPropagation mouseScroll(final double deltaX, final double deltaY); }
  @FunctionalInterface public interface KeyPress { InputPropagation keyPress(final int key, final int scancode, final int mods); }
  @FunctionalInterface public interface PressedThisFrame { InputPropagation pressedThisFrame(final InputAction inputAction); }
  @FunctionalInterface public interface PressedWithRepeatPulse { InputPropagation pressedWithRepeatPulse(final InputAction inputAction); }
  @FunctionalInterface public interface ReleasedThisFrame { InputPropagation releasedThisFrame(final InputAction inputAction); }
}
