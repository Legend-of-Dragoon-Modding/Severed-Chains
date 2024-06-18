package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.game.input.InputAction;
import legend.game.inventory.screens.controls.Brackets;
import legend.game.inventory.screens.controls.Button;
import legend.game.inventory.screens.controls.Glyph;
import legend.game.inventory.screens.controls.Label;
import legend.game.inventory.screens.controls.Textbox;

import java.util.ArrayList;
import java.util.List;

import static legend.game.Scus94491BpeSegment_8002.playMenuSound;

public class VerticalLayoutScreen extends MenuScreen {
  private static final int MAX_VISIBLE_ENTRIES = 9;

  private final Brackets highlight;
  private int highlightedRow = -1;

  private int scroll;

  private final List<Control> rows = new ArrayList<>();
  private final List<Control> configControls = new ArrayList<>();

  private final Glyph upArrow;
  private final Glyph downArrow;

  public VerticalLayoutScreen() {
    this.highlight = this.addControl(new Brackets());
    this.highlight.setPos(26, 30);
    this.highlight.setSize(320, 20);
    this.highlight.setClut(0xfc29);

    this.upArrow = this.addControl(Glyph.uiElement(61, 68));
    this.downArrow = this.addControl(Glyph.uiElement(53, 60));
  }

  public <T extends Control> T addRow(final String name, final T control) {
    final Label label = this.addControl(new Label(name));
    label.setVerticalAlign(Label.VerticalAlign.CENTRE);
    label.setSize(this.getWidth() - 64, 16);
    label.setPos(32, 32 + this.rows.size() * 20);

    label.onGotFocus(() -> this.setFocus(control));

    control.setSize(140, 16);
    control.setPos(label.getWidth() - control.getWidth(), 0);
    label.addControl(control);
    this.rows.add(label);
    this.configControls.add(control);
    this.updateEntries();

    if(this.rows.size() == 1) {
      this.highlightRow(0);
    }

    return control;
  }

  private void highlightRow(final int index) {
    if(this.highlightedRow != index) {
      if(this.highlightedRow != -1 && this.configControls.get(this.highlightedRow).isHovered()) {
        this.configControls.get(this.highlightedRow).hoverOut();
      }

      this.highlightedRow = index;
      this.highlight.setY(this.rows.get(index).getY() - 2);

      final Control current = this.configControls.get(this.highlightedRow);
      current.hoverIn();

      if(!(current instanceof Textbox)) {
        current.focus();
      } else {
        this.setFocus(null);
      }
    }
  }

  private int visibleEntries() {
    return Math.min(MAX_VISIBLE_ENTRIES, this.rows.size() - this.scroll);
  }

  private void updateEntries() {
    for(int i = 0; i < this.rows.size(); i++) {
      final Control save = this.rows.get(i);

      if(i >= this.scroll && i < this.scroll + MAX_VISIBLE_ENTRIES) {
        save.setY(32 + (i - this.scroll) * 20);
        save.show();
      } else {
        save.hide();
      }
    }

    if(!this.rows.isEmpty() && this.highlightedRow != -1) {
      this.highlight.setY(this.rows.get(this.highlightedRow).getY() - 2);
      this.highlight.show();
    } else {
      this.highlight.hide();
    }

    this.upArrow.setVisibility(this.scroll > 0);
    this.upArrow.setPos(this.getWidth() - 20, 0);

    this.downArrow.setVisibility(this.rows.size() - this.scroll > MAX_VISIBLE_ENTRIES);
    this.downArrow.setPos(this.getWidth() - 20, this.getHeight() - 24);
  }

  @Override
  protected void render() {

  }

  @Override
  protected InputPropagation mouseMove(final int x, final int y) {
    if(super.mouseMove(x, y) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    for(int i = 0; i < this.visibleEntries(); i++) {
      if(MathHelper.inBox(x, y, 34, 30 + i * 20, 320, 20)) {
        this.highlightRow(this.scroll + i);
        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation mouseScroll(final int deltaX, final int deltaY) {
    if(super.mouseScroll(deltaX, deltaY) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(deltaY > 0 && this.scroll > 0) {
      playMenuSound(1);
      this.scroll--;
      this.updateEntries();
      this.highlightRow(this.highlightedRow - 1);
    }

    if(deltaY < 0 && this.scroll < this.rows.size() - MAX_VISIBLE_ENTRIES) {
      playMenuSound(1);
      this.scroll++;
      this.updateEntries();
      this.highlightRow(this.highlightedRow + 1);
    }

    return InputPropagation.HANDLED;
  }

  @Override
  protected InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_SOUTH) {
      final Control control = this.configControls.get(this.highlightedRow);

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

    switch(inputAction) {
      case DPAD_UP, JOYSTICK_LEFT_BUTTON_UP -> {
        if(this.highlightedRow > this.scroll) {
          playMenuSound(1);
          this.highlightRow(Math.floorMod(this.highlightedRow - 1, this.rows.size()));
          return InputPropagation.HANDLED;
        } else if(this.scroll > 0) {
          playMenuSound(1);
          this.scroll--;
          this.updateEntries();
          this.highlightRow(Math.floorMod(this.highlightedRow - 1, this.rows.size()));
          return InputPropagation.HANDLED;
        }
      }

      case DPAD_DOWN, JOYSTICK_LEFT_BUTTON_DOWN -> {
        if(this.highlightedRow < this.scroll + this.visibleEntries() - 1) {
          playMenuSound(1);
          this.highlightRow(this.highlightedRow + 1);
          return InputPropagation.HANDLED;
        } else if(this.scroll < this.rows.size() - MAX_VISIBLE_ENTRIES) {
          playMenuSound(1);
          this.scroll++;
          this.updateEntries();
          this.highlightRow(this.highlightedRow + 1);
          return InputPropagation.HANDLED;
        }
      }
    }

    return InputPropagation.PROPAGATE;
  }
}
