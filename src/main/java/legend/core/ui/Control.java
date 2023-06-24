package legend.core.ui;

import legend.core.MathHelper;
import legend.core.opengl.MatrixStack;
import legend.core.opengl.ScissorStack;
import legend.game.input.InputAction;

public abstract class Control extends ControlHost {
  private Screen screen;
  private ControlHost parent;

  private boolean initialized;

  private int x;
  private int y;
  private int width;
  private int height;
  private int order;

  private boolean visible = true;
  private boolean acceptsFocus = true;
  private boolean disabled;
  private boolean hovered;
  private boolean focused;

  @Override
  public Screen getScreen() {
    return this.screen;
  }

  @Override
  protected ControlHost getParent() {
    return this.parent;
  }

  void setScreen(final Screen screen) {
    this.screen = screen;
  }

  void setParent(final ControlHost parent) {
    this.parent = parent;
  }

  protected void deferAction(final Runnable action) {
    this.screen.deferAction(action);
  }

  @Override
  public int getX() {
    return this.x;
  }

  public void setX(final int x) {
    this.x = x;
  }

  @Override
  public int getY() {
    return this.y;
  }

  public void setY(final int y) {
    this.y = y;
  }

  @Override
  public int getWidth() {
    return this.width;
  }

  public void setWidth(final int width) {
    this.width = width;
  }

  @Override
  public int getHeight() {
    return this.height;
  }

  public void setHeight(final int height) {
    this.height = height;
  }

  public int getOrder() {
    return this.order;
  }

  public void setOrder(final int order) {
    this.order = order;
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

  public boolean acceptsFocus() {
    return this.acceptsFocus;
  }

  public void acceptFocus() {
    this.acceptsFocus = true;
  }

  public void ignoreFocus() {
    this.acceptsFocus = false;
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

  public boolean isInteractable() {
    return this.isVisible() && !this.isDisabled() && this.acceptsFocus();
  }

  public boolean hasFocus() {
    return this.focused;
  }

  public void focus() {
    this.screen.setFocus(this);
  }

  public void unfocus() {
    this.screen.setFocus(null);
  }

  protected abstract void initialize();
  protected abstract void updateLayout();
  protected abstract void render(final MatrixStack matrixStack);

  void renderControl(final MatrixStack matrixStack, final ScissorStack scissorStack) {
    if(!this.initialized) {
      this.initialize();
      this.initialized = true;
    }

    if(this.isVisible()) {
      matrixStack.push();
      matrixStack.translate(this.getX(), this.getY(), 0);
      scissorStack.push(this.getX(), this.getY(), this.getWidth(), this.getHeight());

      this.render(matrixStack);
      this.renderControls(matrixStack, scissorStack);

      scissorStack.pop();
      matrixStack.pop();
    }
  }

  protected boolean containsPoint(final int x, final int y) {
    return MathHelper.inBox(x, y, this.getX(), this.getY(), this.getWidth(), this.getHeight());
  }

  public boolean isHovered() {
    return this.hovered;
  }

  protected void hoverIn() {
    if(this.parent instanceof final Control control) {
      control.hoverIn();
    }

    this.hovered = true;

    if(this.hoverInHandler != null) {
      this.hoverInHandler.hoverIn();
    }
  }

  protected void hoverOut() {
    this.hovered = false;

    if(this.hoverOutHandler != null) {
      this.hoverOutHandler.hoverOut();
    }
  }

  protected void gotFocus() {
    this.focused = true;

    if(this.gotFocusHandler != null) {
      this.gotFocusHandler.gotFocus();
    }
  }

  protected void lostFocus() {
    this.focused = false;

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
  protected InputPropagation mouseScroll(final int deltaX, final int deltaY) {
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
  protected InputPropagation mouseScrollHighRes(final double deltaX, final double deltaY) {
    if(this.isDisabled()) {
      return InputPropagation.PROPAGATE;
    }

    if(super.mouseScrollHighRes(deltaX, deltaY) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.mouseScrollHighResHandler != null) {
      return this.mouseScrollHighResHandler.mouseScroll(deltaX, deltaY);
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
  protected InputPropagation charPress(final int codepoint) {
    if(this.isDisabled()) {
      return InputPropagation.PROPAGATE;
    }

    if(super.charPress(codepoint) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.charPressHandler != null) {
      return this.charPressHandler.charPress(codepoint);
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

  public void onMouseScrollHighRes(final MouseScrollHighRes handler) {
    this.mouseScrollHighResHandler = handler;
  }

  public void onKeyPress(final KeyPress handler) {
    this.keyPressHandler = handler;
  }

  public void onCharPress(final CharPress handler) {
    this.charPressHandler = handler;
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
  private MouseScrollHighRes mouseScrollHighResHandler;
  private KeyPress keyPressHandler;
  private CharPress charPressHandler;
  private PressedThisFrame pressedThisFrameHandler;
  private PressedWithRepeatPulse pressedWithRepeatPulseHandler;
  private ReleasedThisFrame releasedThisFrameHandler;

  @FunctionalInterface public interface HoverIn { void hoverIn(); }
  @FunctionalInterface public interface HoverOut { void hoverOut(); }
  @FunctionalInterface public interface GotFocus { void gotFocus(); }
  @FunctionalInterface public interface LostFocus { void lostFocus(); }
  @FunctionalInterface public interface MouseMove { InputPropagation mouseMove(final int x, final int y); }
  @FunctionalInterface public interface MouseClick { InputPropagation mouseClick(final int x, final int y, final int button, final int mods); }
  @FunctionalInterface public interface MouseScroll { InputPropagation mouseScroll(final int deltaX, final int deltaY); }
  @FunctionalInterface public interface MouseScrollHighRes { InputPropagation mouseScroll(final double deltaX, final double deltaY); }
  @FunctionalInterface public interface KeyPress { InputPropagation keyPress(final int key, final int scancode, final int mods); }
  @FunctionalInterface public interface CharPress { InputPropagation charPress(final int codepoint); }
  @FunctionalInterface public interface PressedThisFrame { InputPropagation pressedThisFrame(final InputAction inputAction); }
  @FunctionalInterface public interface PressedWithRepeatPulse { InputPropagation pressedWithRepeatPulse(final InputAction inputAction); }
  @FunctionalInterface public interface ReleasedThisFrame { InputPropagation releasedThisFrame(final InputAction inputAction); }
}
