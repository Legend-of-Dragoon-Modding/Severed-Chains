package legend.game.inventory.screens.controls;

import legend.core.MathHelper;
import legend.game.input.InputAction;
import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.HorizontalAlign;
import legend.game.inventory.screens.InputPropagation;

import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;

import static legend.game.Scus94491BpeSegment_8002.textWidth;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class NumberSpinner<T extends Number> extends Control {
  private final Glyph upArrow;
  private final Glyph downArrow;
  private final Brackets highlight;
  private final Label label;

  private T number;
  private T step;
  private T bigStep;
  private final BiFunction<T, T, T> add;
  private final BiFunction<T, T, T> subtract;
  private final BiFunction<T, Integer, T> scroll;
  private final Function<T, T> clamp;
  private final Function<T, String> toString;

  public static NumberSpinner<Integer> intSpinner(final int number, final int min, final int max) {
    return new NumberSpinner<>(number, 1, 5, Integer::sum, (a, b) -> a - b, Integer::sum, num -> MathHelper.clamp(num, min, max));
  }

  public static NumberSpinner<Float> floatSpinner(final float number, final float step, final float min, final float max) {
    return new NumberSpinner<>(number, step, step * 5, Float::sum, (a, b) -> a - b, (num, s) -> num + s * step, num -> MathHelper.clamp(num, min, max), num -> String.format(Locale.US, "%.2f", num));
  }

  public static NumberSpinner<Float> floatSpinner(final float number, final float step, final float bigStep, final float min, final float max) {
    return new NumberSpinner<>(number, step, bigStep, Float::sum, (a, b) -> a - b, (num, s) -> num + s * step, num -> MathHelper.clamp(num, min, max), num -> String.format(Locale.US, "%.2f", num));
  }

  public static NumberSpinner<Float> percentSpinner(final float number, final float step, final float bigStep, final float min, final float max) {
    return new NumberSpinner<>(number, step, bigStep, Float::sum, (a, b) -> a - b, (num, s) -> num + s * step, num -> MathHelper.clamp(num, min, max), num -> Math.round(num * 100) + "%");
  }

  public NumberSpinner(final T number, final T step, final T bigStep, final BiFunction<T, T, T> add, final BiFunction<T, T, T> subtract, final BiFunction<T, Integer, T> scroll, final Function<T, T> clamp) {
    this(number, step, bigStep, add, subtract, scroll, clamp, T::toString);
  }

  public NumberSpinner(final T number, final T step, final T bigStep, final BiFunction<T, T, T> add, final BiFunction<T, T, T> subtract, final BiFunction<T, Integer, T> scroll, final Function<T, T> clamp, final Function<T, String> toString) {
    this.label = this.addControl(new Label(""));
    this.label.setVerticalAlign(Label.VerticalAlign.CENTRE);
    this.label.getFontOptions().horizontalAlign(HorizontalAlign.CENTRE);
    this.label.ignoreInput();

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
    this.setStep(step, bigStep);
  }

  public void setNumber(final T number) {
    this.number = this.clamp.apply(number);
    this.label.setText(this.toString.apply(this.number));
    this.highlight.setWidth((int)((textWidth(this.label.getText()) + 14) * this.getScale()));
    this.highlight.setX((this.getWidth() - this.highlight.getWidth()) / 2 + 1);

    if(this.changeHandler != null) {
      this.changeHandler.change(this.number);
    }
  }

  public T getNumber() {
    return this.number;
  }

  public void setStep(final T step, final T bigStep) {
    this.step = step;
    this.bigStep = bigStep;
  }

  public T getStep() {
    return this.step;
  }

  public T getBigStep() {
    return this.bigStep;
  }

  @Override
  public void setScale(final float scale) {
    super.setScale(scale);
    this.label.setScale(scale);
    this.upArrow.setScale(scale);
    this.upArrow.setPos((int)(this.getWidth() - 10 * this.getScale()), (this.getHeight() - 17) / 2);
    this.downArrow.setScale(scale);
    this.downArrow.setPos(this.getWidth() - 1, (this.getHeight() - 17) / 2);
    this.highlight.setWidth((int)((textWidth(this.label.getText()) + 14) * this.getScale()));
    this.highlight.setX((this.getWidth() - this.highlight.getWidth()) / 2 + 1);
  }

  @Override
  protected void onResize() {
    super.onResize();
    this.label.setSize(this.getWidth(), this.getHeight());
    this.upArrow.setPos((int)(this.getWidth() - 10 * this.getScale()), (this.getHeight() - 17) / 2);
    this.downArrow.setPos(this.getWidth() - 1, (this.getHeight() - 17) / 2);
    this.highlight.setHeight(this.getHeight());
    this.highlight.setX((this.getWidth() - this.highlight.getWidth()) / 2 + 1);
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

      if(inputAction == InputAction.DPAD_RIGHT || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_RIGHT) {
        this.setNumber(this.add.apply(this.number, this.bigStep));
        return InputPropagation.HANDLED;
      }

      if(inputAction == InputAction.DPAD_LEFT || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_LEFT) {
        this.setNumber(this.subtract.apply(this.number, this.bigStep));
        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected void render(final int x, final int y) {

  }

  public void onChange(final Change<T> change) {
    this.changeHandler = change;
  }

  private Change<T> changeHandler;

  @FunctionalInterface public interface Change<T extends Number> { void change(final T index); }
}
