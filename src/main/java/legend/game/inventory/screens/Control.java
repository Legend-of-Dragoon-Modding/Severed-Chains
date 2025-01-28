package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.game.SItem;
import legend.game.input.InputAction;
import legend.game.types.Renderable58;

import static legend.game.Scus94491BpeSegment_8002.allocateRenderable;
import static legend.game.Scus94491BpeSegment_800b.uiFile_800bdc3c;

public abstract class Control extends ControlHost {
  private MenuScreen screen;
  private ControlHost parent;

  private int x;
  private int y;
  private int z = 35;
  private int width;
  private int height;
  private float scale = 1.0f;

  private boolean visible = true;
  private boolean acceptsInput = true;
  private boolean disabled;
  private boolean hovered;
  private boolean focused;

  @Override
  public MenuScreen getScreen() {
    return this.screen;
  }

  @Override
  protected ControlHost getParent() {
    return this.parent;
  }

  void setScreen(final MenuScreen screen) {
    this.screen = screen;
  }

  void setParent(final ControlHost parent) {
    this.parent = parent;
  }

  public void setPos(final int x, final int y) {
    this.x = x;
    this.y = y;
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

  @Override
  public int getWidth() {
    return this.width;
  }

  public void setWidth(final int width) {
    this.width = width;
    this.onResize();
  }

  @Override
  public int getHeight() {
    return this.height;
  }

  public void setHeight(final int height) {
    this.height = height;
    this.onResize();
  }

  public float getScale() {
    return this.scale;
  }

  public void setScale(final float scale) {
    this.scale = scale;
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

  public boolean hasFocus() {
    return this.focused;
  }

  public void focus() {
    this.screen.setFocus(this);
  }

  public void unfocus() {
    this.screen.setFocus(null);
  }

  protected void onResize() {

  }

  protected abstract void render(final int x, final int y);

  protected void renderNumber(final int x, final int y, final int value, final int digitCount) {
    SItem.renderNumber(x, y, value, 0x2, digitCount);
  }

  protected void renderNumber(final int x, final int y, final String value, final int digitCount) {
    for(int i = 0; i < Math.min(digitCount, value.length()); i++) {
      final Renderable58 struct = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      struct.flags_00 |= Renderable58.FLAG_NO_ANIMATION | Renderable58.FLAG_DELETE_AFTER_RENDER;
      struct.tpage_2c = 0x19;
      struct.clut_30 = 0;
      struct.z_3c = 33;
      struct.x_40 = x + 6 * i;
      struct.y_44 = y;

      final char digitChar = value.charAt(i);

      final int digit;
      switch(digitChar) {
        case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> digit = digitChar - 0x30;
        case '.' -> {
          // Render the bottom half of a colon, because for some reason that's the easiest way to render a decimal
          struct.heightCut = 4;
          digit = 10;
          struct.y_44++;
        }
        default -> {
          continue;
        }
      }

      struct.glyph_04 = digit;
    }
  }

  protected void renderNumber(final int x, final int y, final int value, final int digitCount, final int flags) {
    SItem.renderNumber(x, y, value, flags | 0x2, digitCount);
  }

  protected void renderCharacter(final int x, final int y, final int character) {
    final Renderable58 v0 = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
    v0.flags_00 |= Renderable58.FLAG_NO_ANIMATION | Renderable58.FLAG_DELETE_AFTER_RENDER;
    v0.glyph_04 = character;
    v0.tpage_2c = 0x19;
    v0.clut_30 = 0x7ca9;
    v0.z_3c = 0x21;
    v0.x_40 = x;
    v0.y_44 = y;
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
