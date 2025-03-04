package legend.game.inventory.screens.controls;

import legend.core.platform.input.InputAction;
import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.InputPropagation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static legend.core.GameEngine.PLATFORM;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_END;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_HOME;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_PAGE_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_PAGE_UP;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_UP;

public class BigList<T> extends Control {
  private static final int ENTRY_HEIGHT = 24;
  private static final int MAX_VISIBLE_ENTRIES = 5;

  private final Function<T, String> entryToString;
  private final List<T> entries = new ArrayList<>();

  private int scroll;
  private int slot;

  /** Allows list wrapping, but only on new input */
  private boolean allowWrapY = true;

  private final List<Label> labels = new ArrayList<>();
  private final Brackets highlight;

  private final Glyph upArrow;
  private final Glyph downArrow;

  public BigList(final Function<T, String> entryToString) {
    this.entryToString = entryToString;

    this.highlight = this.addControl(new Brackets());
    this.highlight.setX(8);

    this.upArrow = this.addControl(Glyph.uiElement(61, 68));
    this.downArrow = this.addControl(Glyph.uiElement(53, 60));
  }

  public void addEntry(final T entry) {
    this.entries.add(entry);
    this.labels.add(this.addLabel());
    this.updateEntries();

    if(this.entries.size() == 1) {
      this.highlight(0);
    }
  }

  public void removeEntry(final T entry) {
    final int index = this.entries.indexOf(entry);

    if(index != -1) {
      this.entries.remove(index);
      this.removeControl(this.labels.get(index));
      this.labels.remove(index);

      if(this.labels.isEmpty() && this.highlightHandler != null) {
        this.highlightHandler.highlight(null);
      } else if(this.slot >= this.entries.size()) {
        if(this.scroll > 0) {
          this.scroll--;
        }

        this.highlight(this.slot - 1);
        this.updateEntries();
      } else {
        this.updateEntries();
        this.highlight(this.slot);
      }
    }
  }

  private Label addLabel() {
    final int index = this.labels.size();

    final Label label = this.addControl(new Label(this.entryToString.apply(this.entries.get(index))));
    label.setVerticalAlign(Label.VerticalAlign.CENTRE);
    label.setX(16);
    label.setHeight(ENTRY_HEIGHT);
    label.acceptsInput();
    label.hide();

    label.onMouseMove((x, y) -> {
      this.highlight(this.labels.indexOf(label));
      return InputPropagation.HANDLED;
    });

    label.onMouseClick((x, y, button, mods) -> {
      if(button == PLATFORM.getMouseButton(0) && mods.isEmpty()) {
        if(this.selectionHandler != null) {
          this.selectionHandler.selection(this.getSelected());
        }
      }

      return InputPropagation.HANDLED;
    });

    return label;
  }

  public int size() {
    return this.entries.size();
  }

  public T getSelected() {
    if(this.slot >= this.entries.size()) {
      return null;
    }

    return this.entries.get(this.slot);
  }

  private int visibleEntries() {
    return Math.min(MAX_VISIBLE_ENTRIES, this.entries.size() - this.scroll);
  }

  private void updateEntries() {
    for(int i = 0; i < this.labels.size(); i++) {
      final Label save = this.labels.get(i);

      if(i >= this.scroll && i < this.scroll + MAX_VISIBLE_ENTRIES) {
        save.setY(8 + (i - this.scroll) * ENTRY_HEIGHT);
        save.setWidth(this.getWidth() - 48);
        save.show();
      } else {
        save.hide();
      }
    }

    if(!this.labels.isEmpty()) {
      this.highlight.setSize(this.getWidth() - 40, ENTRY_HEIGHT);
      this.highlight.setY(this.labels.get(this.slot).getY());
      this.highlight.show();
    } else {
      this.highlight.hide();
    }

    this.upArrow.setVisibility(this.scroll > 0);
    this.upArrow.setPos(this.getWidth() - 20, 0);

    this.downArrow.setVisibility(this.labels.size() - this.scroll > MAX_VISIBLE_ENTRIES);
    this.downArrow.setPos(this.getWidth() - 20, this.getHeight() - 24);
  }

  private void highlight(final int index) {
    if(index < 0) {
      throw new IllegalArgumentException("Index must be > 0");
    }

    if(index != this.slot) {
      playMenuSound(1);
    }

    this.slot = index;
    this.highlight.setY(this.labels.get(index).getY());

    if(this.highlightHandler != null) {
      this.highlightHandler.highlight(this.getSelected());
    }
  }

  @Override
  protected void onResize() {
    super.onResize();
    this.updateEntries();
  }

  @Override
  protected InputPropagation mouseScroll(final int deltaX, final int deltaY) {
    if(super.mouseScroll(deltaX, deltaY) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(deltaY > 0 && this.scroll > 0) {
      this.scroll--;
      this.updateEntries();
      this.highlight(this.slot - 1);
    }

    if(deltaY < 0 && this.scroll < this.entries.size() - MAX_VISIBLE_ENTRIES) {
      this.scroll++;
      this.updateEntries();
      this.highlight(this.slot + 1);
    }

    return InputPropagation.HANDLED;
  }

  private void menuNavigateUp() {
    final int entryCount = this.entries.size();
    if(this.slot > this.scroll) {
      this.highlight(this.slot - 1);
    } else if(this.scroll > 0) {
      this.scroll--;
      this.updateEntries();
      this.highlight(this.slot - 1);
    } else if(entryCount > 1 && this.allowWrapY) {
      this.scroll = entryCount > MAX_VISIBLE_ENTRIES ? entryCount - MAX_VISIBLE_ENTRIES : 0;
      this.updateEntries();
      this.highlight(entryCount - 1);
    }
  }

  private void menuNavigateDown() {
    final int entryCount = this.entries.size();
    if(this.slot < this.scroll + this.visibleEntries() - 1) {
      this.highlight(this.slot + 1);
    } else if(this.scroll + MAX_VISIBLE_ENTRIES < entryCount) {
      this.scroll++;
      this.updateEntries();
      this.highlight(this.slot + 1);
    } else if(entryCount > 1 && this.allowWrapY) {
      this.scroll = 0;
      this.updateEntries();
      this.highlight(0);
    }
  }

  private void menuNavigatePageUp() {
    if(this.scroll - MAX_VISIBLE_ENTRIES >= 0) {
      playMenuSound(1);
      this.slot -= MAX_VISIBLE_ENTRIES;
      this.scroll -= MAX_VISIBLE_ENTRIES;
      this.updateEntries();
    } else if(this.scroll != 0) {
      playMenuSound(1);
      this.slot -= this.scroll;
      this.scroll = 0;
      this.updateEntries();
    }
    this.highlight(this.slot);
  }

  private void menuNavigatePageDown() {
    final int count = this.entries.size();
    if(this.scroll + MAX_VISIBLE_ENTRIES < count - 1 - MAX_VISIBLE_ENTRIES) {
      playMenuSound(1);
      this.slot += MAX_VISIBLE_ENTRIES;
      this.scroll += MAX_VISIBLE_ENTRIES;
      this.updateEntries();
    } else if(count > MAX_VISIBLE_ENTRIES && this.scroll != count - MAX_VISIBLE_ENTRIES) {
      playMenuSound(1);
      this.slot += count - MAX_VISIBLE_ENTRIES - this.scroll;
      this.scroll = count - MAX_VISIBLE_ENTRIES;
      this.updateEntries();
    }

    this.highlight(this.slot);
  }

  private void menuNavigateHome() {
    if(this.slot != 0) {
      this.scroll = 0;
      this.updateEntries();
      this.highlight(0);
    }
  }

  private void menuNavigateEnd() {
    final int count = this.entries.size();
    if(this.slot != count - 1) {
      this.scroll = Math.max(0, count - MAX_VISIBLE_ENTRIES);
      this.updateEntries();
      this.highlight(count - 1);
    }
  }

  @Override
  protected InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(super.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
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

    if(action == INPUT_ACTION_MENU_CONFIRM.get() && !repeat) {
      if(this.selectionHandler != null) {
        this.selectionHandler.selection(this.getSelected());
      }

      return InputPropagation.HANDLED;
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

  @Override
  protected void render(final int x, final int y) {

  }

  public void onHighlight(final OnHighlight<T> handler) {
    this.highlightHandler = handler;
  }

  public void onSelection(final OnSelection<T> handler) {
    this.selectionHandler = handler;
  }

  private OnHighlight<T> highlightHandler;
  private OnSelection<T> selectionHandler;

  @FunctionalInterface public interface OnHighlight<T> { void highlight(T type); }
  @FunctionalInterface public interface OnSelection<T> { void selection(T type); }
}
