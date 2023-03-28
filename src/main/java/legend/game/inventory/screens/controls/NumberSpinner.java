package legend.game.inventory.screens.controls;

import legend.core.MathHelper;
import legend.game.input.InputAction;
import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.InputPropagation;

public class NumberSpinner extends Control {
  private final Glyph upArrow;
  private final Glyph downArrow;
  private final Highlight highlight;

  private int min;
  private int max = Integer.MAX_VALUE;
  private int number;
  private int digitCount;

  public NumberSpinner(final int number) {
    this.upArrow = this.addControl(Glyph.uiElement(61, 68));
    this.upArrow.ignoreInput();

    this.downArrow = this.addControl(Glyph.uiElement(53, 60));
    this.downArrow.ignoreInput();

    this.highlight = this.addControl(new Highlight());
    this.highlight.setHeight(16);
    this.highlight.hide();

    this.setNumber(number);
  }

  public void setNumber(final int number) {
    this.number = MathHelper.clamp(number, this.min, this.max);
    this.digitCount = MathHelper.digitCount(this.number);
    this.highlight.setWidth(this.digitCount * 6 + 10);
    this.highlight.setX((this.getWidth() - this.highlight.getWidth()) / 2);

    if(this.changeHandler != null) {
      this.changeHandler.change(this.number);
    }
  }

  public int getNumber() {
    return this.number;
  }

  public void setMin(final int min) {
    this.min = min;
  }

  public int getMin() {
    return this.min;
  }

  public void setMax(final int max) {
    this.max = max;
  }

  public int getMax() {
    return this.max;
  }

  @Override
  protected void onResize() {
    super.onResize();
    this.upArrow.setPos(this.getWidth() - 10, -1);
    this.downArrow.setPos(this.getWidth(), -2);
    this.highlight.setHeight(this.getHeight());
    this.highlight.setX((this.getWidth() - this.highlight.getWidth()) / 2);
  }

  @Override
  protected void lostFocus() {
    super.lostFocus();
    this.highlight.hide();
  }

  @Override
  protected InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.highlight.isVisible()) {
      if(inputAction == InputAction.BUTTON_SOUTH || inputAction == InputAction.BUTTON_EAST) {
        this.highlight.hide();
        return InputPropagation.HANDLED;
      }
    } else {
      if(inputAction == InputAction.BUTTON_SOUTH) {
        this.highlight.show();
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation pressedWithRepeatPulse(final InputAction inputAction) {
    if(super.pressedWithRepeatPulse(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.highlight.isVisible()) {
      if(inputAction == InputAction.DPAD_UP || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_UP) {
        this.setNumber(this.number + 1);
        return InputPropagation.HANDLED;
      }

      if(inputAction == InputAction.DPAD_DOWN || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_DOWN) {
        this.setNumber(this.number - 1);
        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected void render(final int x, final int y) {
    this.renderNumber(x + (this.getWidth() - this.digitCount * 6) / 2, y + 3, this.number, this.digitCount);
  }

  public void onChange(final Change change) {
    this.changeHandler = change;
  }

  private Change changeHandler;

  @FunctionalInterface public interface Change { void change(final int index); }
}
