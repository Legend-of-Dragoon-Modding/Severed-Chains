package legend.game.inventory.screens.controls;

import legend.game.input.InputAction;
import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.InputPropagation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class BigList<T> extends Control {
  private static final int ENTRY_HEIGHT = 24;
  private static final int MAX_VISIBLE_ENTRIES = 5;

  private final Function<T, String> entryToString;
  private final List<T> entries = new ArrayList<>();

  private int scroll;
  private int slot;

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
      if(button == GLFW_MOUSE_BUTTON_LEFT && mods == 0) {
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
      playMenuSound(1);
      this.scroll--;
      this.updateEntries();
      this.highlight(this.slot - 1);
    }

    if(deltaY < 0 && this.scroll < this.entries.size() - MAX_VISIBLE_ENTRIES) {
      playMenuSound(1);
      this.scroll++;
      this.updateEntries();
      this.highlight(this.slot + 1);
    }

    return InputPropagation.HANDLED;
  }

  @Override
  protected InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_SOUTH) {
      if(this.selectionHandler != null) {
        this.selectionHandler.selection(this.getSelected());
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
        if(this.slot > this.scroll) {
          playMenuSound(1);
          this.highlight(this.slot - 1);
          return InputPropagation.HANDLED;
        } else if(this.scroll > 0) {
          playMenuSound(1);
          this.scroll--;
          this.updateEntries();
          this.highlight(this.slot - 1);
          return InputPropagation.HANDLED;
        }
      }

      case DPAD_DOWN, JOYSTICK_LEFT_BUTTON_DOWN -> {
        if(this.slot < this.scroll + this.visibleEntries() - 1) {
          playMenuSound(1);
          this.highlight(this.slot + 1);
          return InputPropagation.HANDLED;
        } else if(this.scroll < this.entries.size() - MAX_VISIBLE_ENTRIES) {
          playMenuSound(1);
          this.scroll++;
          this.updateEntries();
          this.highlight(this.slot + 1);
          return InputPropagation.HANDLED;
        }
      }

      case BUTTON_SHOULDER_LEFT_1 -> {
        playMenuSound(1);
        if(this.scroll == 0) {
          this.slot = 0;
        } else {
          this.slot = this.scroll;
        }
        this.highlight(this.slot);
        return InputPropagation.HANDLED;
      }

      case BUTTON_SHOULDER_LEFT_2 -> {
        if(this.visibleEntries() >= MAX_VISIBLE_ENTRIES) {
          if(this.scroll == 0) {
            playMenuSound(1);
            this.slot = MAX_VISIBLE_ENTRIES - 1;
          } else {
            playMenuSound(1);
            this.slot = this.scroll + MAX_VISIBLE_ENTRIES - 1;
          }
          this.highlight(this.slot);
          return InputPropagation.HANDLED;
        }

        playMenuSound(1);
        this.slot = this.entries.size() - 1;
        this.highlight(this.slot);
        return InputPropagation.HANDLED;
      }

      case BUTTON_SHOULDER_RIGHT_1 -> {
        if(this.entries.size() > MAX_VISIBLE_ENTRIES) {
          if(this.slot - MAX_VISIBLE_ENTRIES > 0) {
            playMenuSound(1);
            this.slot -= MAX_VISIBLE_ENTRIES;
            this.scroll = Math.max(0, this.slot - MAX_VISIBLE_ENTRIES);
            this.updateEntries();
            this.highlight(this.slot);
            return InputPropagation.HANDLED;
          }

          if(this.slot != 0) {
            playMenuSound(1);
            this.slot = 0;
            this.scroll = 0;
            this.updateEntries();
            this.highlight(this.slot);
            return InputPropagation.HANDLED;
          }
        }
      }

      case BUTTON_SHOULDER_RIGHT_2 -> {
        if(this.entries.size() > MAX_VISIBLE_ENTRIES) {
          if(this.slot + MAX_VISIBLE_ENTRIES < this.entries.size()) {
            playMenuSound(1);
            this.slot += MAX_VISIBLE_ENTRIES;
            this.scroll = this.slot - MAX_VISIBLE_ENTRIES + 1;
            this.updateEntries();
            this.highlight(this.slot);
            return InputPropagation.HANDLED;
          }

          if(this.slot != this.entries.size() - 1) {
            playMenuSound(1);
            this.slot = this.entries.size() - 1;
            this.scroll = this.entries.size() - MAX_VISIBLE_ENTRIES;
            this.updateEntries();
            this.highlight(this.slot);
            return InputPropagation.HANDLED;
          }
        }
      }
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
