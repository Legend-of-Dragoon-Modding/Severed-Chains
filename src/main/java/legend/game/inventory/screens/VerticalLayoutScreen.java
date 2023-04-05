package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.game.input.InputAction;
import legend.game.inventory.screens.controls.Brackets;
import legend.game.inventory.screens.controls.Button;
import legend.game.inventory.screens.controls.Label;
import legend.game.inventory.screens.controls.Textbox;

import java.util.ArrayList;
import java.util.List;

import static legend.game.Scus94491BpeSegment_8002.playSound;

public class VerticalLayoutScreen extends MenuScreen {
  private final Brackets highlight;
  private int highlightedRow = -1;

  private final List<Control> rows = new ArrayList<>();

  public VerticalLayoutScreen() {
    this.highlight = this.addControl(new Brackets());
    this.highlight.setPos(26, 30);
    this.highlight.setSize(320, 20);
    this.highlight.setClut(0xfc29);
  }

  public <T extends Control> T addRow(final String name, final T control) {
    final Label label = this.addControl(new Label(name));
    label.setVerticalAlign(Label.VerticalAlign.CENTRE);
    label.setSize(0, 16);
    label.setPos(32, 32 + this.rows.size() * 20);

    control.setSize(140, 16);
    control.setPos(340 - control.getWidth(), 32 + this.rows.size() * 20);
    this.rows.add(control);
    this.addControl(control);

    if(this.rows.size() == 1) {
      this.highlightRow(0);
    }

    return control;
  }

  private void highlightRow(final int index) {
    if(this.highlightedRow != index) {
      if(this.highlightedRow != -1 && this.rows.get(this.highlightedRow).isHovered()) {
        this.rows.get(this.highlightedRow).hoverOut();
      }

      this.highlightedRow = index;
      this.highlight.setY(30 + index * 20);

      final Control current = this.rows.get(this.highlightedRow);
      current.hoverIn();

      if(!(current instanceof Textbox)) {
        current.focus();
      } else {
        this.setFocus(null);
      }
    }
  }

  @Override
  protected void render() {

  }

  @Override
  protected InputPropagation mouseMove(final int x, final int y) {
    if(super.mouseMove(x, y) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    for(int i = 0; i < this.rows.size(); i++) {
      if(MathHelper.inBox(x, y, 34, 30 + i * 20, 320, 20)) {
        this.highlightRow(i);
        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_SOUTH) {
      final Control control = this.rows.get(this.highlightedRow);

      if(control instanceof final Button button) {
        this.deferAction(button::press);
      } else {
        this.deferAction(control::focus);
      }

      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation pressedWithRepeatPulse(final InputAction inputAction) {
    if(super.pressedWithRepeatPulse(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.DPAD_DOWN || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_DOWN) {
      playSound(1);
      this.highlightRow((this.highlightedRow + 1) % this.rows.size());
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.DPAD_UP || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_UP) {
      playSound(1);
      this.highlightRow(Math.floorMod(this.highlightedRow - 1, this.rows.size()));
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }
}
