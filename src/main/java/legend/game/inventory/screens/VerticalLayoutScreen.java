package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.core.platform.input.InputAction;
import legend.game.inventory.screens.controls.Brackets;
import legend.game.inventory.screens.controls.Button;
import legend.game.inventory.screens.controls.Glyph;
import legend.game.inventory.screens.controls.Label;
import legend.game.inventory.screens.controls.Textbox;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_END;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_HOME;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_PAGE_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_PAGE_UP;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_UP;

public class VerticalLayoutScreen extends MenuScreen {
  private static final int MAX_VISIBLE_ENTRIES = 14;

  private final Brackets highlight;
  private int highlightedRow = -1;

  private int scroll;
  /** Allows list wrapping, but only on new input */
  private boolean allowWrapY = true;

  private final List<Label> rows = new ArrayList<>();
  private final List<Control> configControls = new ArrayList<>();

  private final Glyph upArrow;
  private final Glyph downArrow;

  public VerticalLayoutScreen() {
    this.highlight = this.addControl(new Brackets());
    this.highlight.setPos(26, 30);
    this.highlight.setSize(320, 14);
    this.highlight.setClut(0xfc29);

    this.upArrow = this.addControl(Glyph.uiElement(61, 68));
    this.downArrow = this.addControl(Glyph.uiElement(53, 60));
  }

  public <T extends Control> Label addRow(final String name, @Nullable final T control) {
    final Label label = this.addControl(new Label(name));
    label.setVerticalAlign(Label.VerticalAlign.CENTRE);
    label.setSize(this.getWidth() - 64, 11);
    label.setPos(32, 32 + this.rows.size() * 13);
    label.setScale(0.66f);

    if(control != null) {
      label.onGotFocus(() -> this.setFocus(control));

      control.setSize(140, 11);
      control.setPos(this.getWidth() - 64 - control.getWidth(), 0);
      control.setScale(0.66f);
      label.addControl(control);
      this.configControls.add(control);
    } else {
      this.configControls.add(null);
    }

    this.rows.add(label);

    if(this.rows.size() == 1) {
      this.highlightRow(0);
    }

    this.updateEntries();
    return label;
  }

  public Label getHighlightedRow() {
    return this.rows.get(this.highlightedRow);
  }

  private void highlightRow(final int index) {
    if(this.highlightedRow != index) {
      if(this.highlightedRow != -1 && this.configControls.get(this.highlightedRow) != null && this.configControls.get(this.highlightedRow).isHovered()) {
        this.configControls.get(this.highlightedRow).hoverOut();
      }

      this.highlightedRow = index;
      this.highlight.setY(this.rows.get(index).getY() - 2);
      this.setFocus(null);

      final Control current = this.configControls.get(this.highlightedRow);

      if(current != null) {
        current.hoverIn();

        if(!(current instanceof Textbox)) {
          current.focus();
        }
      }
    }
  }

  private int visibleEntries() {
    return Math.min(MAX_VISIBLE_ENTRIES, this.rows.size() - this.scroll);
  }

  private void updateEntries() {
    for(int i = 0; i < this.rows.size(); i++) {
      final Control control = this.rows.get(i);

      if(i >= this.scroll && i < this.scroll + MAX_VISIBLE_ENTRIES) {
        control.setY(32 + (i - this.scroll) * 13);
        control.show();
      } else {
        control.hide();
      }
    }

    if(!this.rows.isEmpty() && this.highlightedRow != -1) {
      this.highlight.setY(this.rows.get(this.highlightedRow).getY() - 2);
      this.highlight.show();
    } else {
      this.highlight.hide();
    }

    this.upArrow.setVisibility(this.scroll > 0);
    this.upArrow.setPos(this.getWidth() - 20, 6);

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
      if(MathHelper.inBox(x, y, 26, 30 + i * 13, 319, 13) && this.highlightedRow != this.scroll + i) {
        playMenuSound(1);
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

  private void menuNavigateUp() {
    final int optionCount = this.rows.size();
    if(this.highlightedRow > this.scroll) {
      playMenuSound(1);
      this.highlightRow(this.highlightedRow - 1);
    } else if(this.scroll > 0) {
      playMenuSound(1);
      this.scroll--;
      this.updateEntries();
      this.highlightRow(Math.floorMod(this.highlightedRow - 1, optionCount));
    } else if(optionCount > 1 && this.allowWrapY) {
      playMenuSound(1);
      this.highlightRow(optionCount - 1);
      this.scroll = Math.max(0, optionCount - MAX_VISIBLE_ENTRIES);
      this.updateEntries();
    }
  }

  private void menuNavigateDown() {
    final int optionCount = this.rows.size();
    if(this.highlightedRow < this.scroll + this.visibleEntries() - 1) {
      playMenuSound(1);
      this.highlightRow(this.highlightedRow + 1);
    } else if(this.highlightedRow != optionCount - 1) {
      playMenuSound(1);
      this.highlightRow(this.highlightedRow + 1);
      this.scroll++;
      this.updateEntries();
    } else if(optionCount > 1 && this.allowWrapY) {
      playMenuSound(1);
      this.highlightRow(0);
      this.scroll = 0;
      this.updateEntries();
    }
  }

  private void menuNavigatePageUp() {
    if(this.scroll - MAX_VISIBLE_ENTRIES > 0) {
      playMenuSound(1);
      this.highlightRow(this.highlightedRow - MAX_VISIBLE_ENTRIES);
      this.scroll -= MAX_VISIBLE_ENTRIES;
      this.updateEntries();
    } else if(this.scroll != 0) {
      playMenuSound(1);
      this.highlightRow(this.highlightedRow - this.scroll);
      this.scroll = 0;
      this.updateEntries();
    }
  }

  private void menuNavigatePageDown() {
    final int optionCount = this.rows.size();
    if(this.scroll + MAX_VISIBLE_ENTRIES < optionCount - MAX_VISIBLE_ENTRIES) {
      playMenuSound(1);
      this.highlightRow(this.highlightedRow + MAX_VISIBLE_ENTRIES);
      this.scroll += MAX_VISIBLE_ENTRIES;
      this.updateEntries();
    } else if(this.scroll != optionCount - this.visibleEntries()) {
      playMenuSound(1);
      this.highlightRow(this.highlightedRow + (optionCount - this.visibleEntries() - this.scroll));
      this.scroll = optionCount - this.visibleEntries();
      this.updateEntries();
    }
  }

  private void menuNavigateHome() {
    if(this.highlightedRow != 0 || this.scroll != 0) {
      playMenuSound(1);
      this.highlightRow(0);
      this.scroll = 0;
      this.updateEntries();
    }
  }

  private void menuNavigateEnd() {
    final int optionCount = this.rows.size();
    if(this.highlightedRow != optionCount - 1 || this.scroll != Math.max(0, optionCount - MAX_VISIBLE_ENTRIES)) {
      playMenuSound(1);
      this.highlightRow(optionCount - 1);
      this.scroll = Math.max(0, optionCount - MAX_VISIBLE_ENTRIES);
      this.updateEntries();
    }
  }

  @Override
  protected InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(super.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_UP.get()) {
      this.menuNavigateUp();
      this.allowWrapY = false;
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_DOWN.get()) {
      this.menuNavigateDown();
      this.allowWrapY = false;
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_HOME.get()) {
      this.menuNavigateHome();
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_END.get()) {
      this.menuNavigateEnd();
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_PAGE_UP.get()) {
      this.menuNavigatePageUp();
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_PAGE_DOWN.get()) {
      this.menuNavigatePageDown();
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_CONFIRM.get() && !repeat) {
      final Control control = this.configControls.get(this.highlightedRow);
      if(control != null) {
        if(control instanceof final Button button) {
          this.deferAction(button::press);
        } else {
          this.deferAction(control::focus);
        }

        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation inputActionReleased(final InputAction action) {
    if(super.inputActionReleased(action) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_UP.get() || action == INPUT_ACTION_MENU_DOWN.get()) {
      this.allowWrapY = true;
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }
}
