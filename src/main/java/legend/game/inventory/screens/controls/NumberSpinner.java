package legend.game.inventory.screens.controls;

import legend.core.MathHelper;
import legend.game.input.InputAction;
import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.InputPropagation;

import java.util.function.BiFunction;
import java.util.function.Function;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class NumberSpinner<T extends Number> extends Control {
  private final Glyph upArrow;
  private final Glyph downArrow;
  private final Brackets highlight;

  private T number;
  private T step;
  private String numberStr;
  private final BiFunction<T, T, T> add;
  private final BiFunction<T, T, T> subtract;
  private final BiFunction<T, Integer, T> scroll;
  private final Function<T, T> clamp;
  private final Function<T, String> toString;

  public static NumberSpinner<Integer> intSpinner(final int number, final int min, final int max) {
    return new NumberSpinner<>(number, 1, Integer::sum, (a, b) -> a - b, Integer::sum, num -> MathHelper.clamp(num, min, max));
  }

  public static NumberSpinner<Float> floatSpinner(final float number, final float step, final float min, final float max) {
    return new NumberSpinner<>(number, step, Float::sum, (a, b) -> a - b, (num, s) -> num + s * step, num -> MathHelper.clamp(num, min, max), "%.2f"::formatted);
  }

  public NumberSpinner(final T number, final T step, final BiFunction<T, T, T> add, final BiFunction<T, T, T> subtract, final BiFunction<T, Integer, T> scroll, final Function<T, T> clamp) {
    this(number, step, add, subtract, scroll, clamp, T::toString);
  }

  public NumberSpinner(final T number, final T step, final BiFunction<T, T, T> add, final BiFunction<T, T, T> subtract, final BiFunction<T, Integer, T> scroll, final Function<T, T> clamp, final Function<T, String> toString) {
    this.upArrow = this.addControl(Glyph.uiElement(61, 68));
    this.upArrow.ignoreInput();

    this.downArrow = this.addControl(Glyph.uiElement(53, 60));
    this.downArrow.ignoreInput();

    this.highlight = this.addControl(new Brackets());
    this.highlight.setHeight(16);
    this.highlight.ignoreInput();
    this.highlight.hide();

    this.add = add;
    this.subtract = subtract;
    this.scroll = scroll;
    this.clamp = clamp;
    this.toString = toString;

    this.setNumber(number);
    this.setStep(step);
  }

  public void setNumber(final T number) {
    this.number = this.clamp.apply(number);
    this.numberStr = this.toString.apply(this.number);
    this.highlight.setWidth(this.numberStr.length() * 6 + 10);
    this.highlight.setX((this.getWidth() - this.highlight.getWidth()) / 2 - 8);

    if(this.changeHandler != null) {
      this.changeHandler.change(this.number);
    }
  }

  public T getNumber() {
    return this.number;
  }

  public void setStep(final T step) {
    this.step = step;
  }

  public T getStep() {
    return this.step;
  }

  @Override
  protected void onResize() {
    super.onResize();
    this.upArrow.setPos(this.getWidth() - 10, -1);
    this.downArrow.setPos(this.getWidth(), -2);
    this.highlight.setHeight(this.getHeight());
    this.highlight.setX((this.getWidth() - this.highlight.getWidth()) / 2 - 8);
  }

  @Override
  protected void lostFocus() {
    super.lostFocus();
    this.highlight.hide();
  }

  @Override
  protected InputPropagation mouseScroll(final int deltaX, final int deltaY) {
    if(super.mouseScroll(deltaX, deltaY) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    this.setNumber(this.scroll.apply(this.number, deltaY));
    return InputPropagation.HANDLED;
  }

  @Override
  protected InputPropagation mouseClick(final int x, final int y, final int button, final int mods) {
    if(super.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(button == GLFW_MOUSE_BUTTON_LEFT && mods == 0 && !this.highlight.isVisible()) {
      this.highlight.show();
    }

    return InputPropagation.HANDLED;
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
        this.setNumber(this.add.apply(this.number, this.step));
        return InputPropagation.HANDLED;
      }

      if(inputAction == InputAction.DPAD_DOWN || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_DOWN) {
        this.setNumber(this.subtract.apply(this.number, this.step));
        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected void render(final int x, final int y) {
    this.renderNumber(x + (this.getWidth() - this.numberStr.length() * 6) / 2, y + 3, this.numberStr, this.numberStr.length());
  }

  public void onChange(final Change<T> change) {
    this.changeHandler = change;
  }

  private Change<T> changeHandler;

  @FunctionalInterface public interface Change<T extends Number> { void change(final T index); }
}
