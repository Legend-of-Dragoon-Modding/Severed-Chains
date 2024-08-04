package legend.game.inventory.screens.controls;

import legend.core.MathHelper;
import legend.game.input.InputAction;
import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.InputPropagation;
import legend.game.inventory.screens.TextColour;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.renderItemIcon;
import static legend.game.SItem.renderText;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;

public class ListBox<T> extends Control {
  private final Function<T, String> entryToString;
  @Nullable
  private final ToIntFunction<T> entryToIcon;
  @Nullable
  private final ToIntFunction<T> entryToRightIcon;
  @Nullable
  private final Predicate<T> isDisabled;
  private final List<Entry> entries = new ArrayList<>();
  private final int entryHeight = 17;
  private int maxVisibleEntries;

  private double scrollAccumulator;
  private int scroll;
  private int slot;

  private final Glyph highlight;

  private final Glyph upArrow;
  private final Glyph downArrow;

  public ListBox(final Function<T, String> entryToString, @Nullable final ToIntFunction<T> entryToIcon, @Nullable final ToIntFunction<T> entryToRightIcon, @Nullable final Predicate<T> isDisabled) {
    this.entryToString = entryToString;
    this.entryToIcon = entryToIcon;
    this.entryToRightIcon = entryToRightIcon;
    this.isDisabled = isDisabled;

    this.highlight = this.addControl(Glyph.uiElement(118, 118));
    FUN_80104b60(this.highlight.getRenderable()); //TODO not sure exactly what this does but without it the middle part of the highlight doesn't stretch
    this.upArrow = this.addControl(Glyph.uiElement(61, 68));
    this.downArrow = this.addControl(Glyph.uiElement(53, 60));

    this.setSize(171, 119);

    this.highlight.setPos(34, 1);
    this.highlight.ignoreInput();
    this.upArrow.setPos(this.getWidth(), 0);
    this.downArrow.setPos(this.getWidth(), this.getHeight() - 15);
    this.select(0);
  }

  public boolean isEmpty() {
    return this.entries.isEmpty();
  }

  public T getSelectedEntry() {
    final int index = this.scroll + this.slot;

    if(index >= this.entries.size()) {
      return null;
    }

    return this.entries.get(index).data;
  }

  public List<T> getEntries() {
    return this.entries.stream().map(e -> e.data).toList();
  }

  @Override
  public void setZ(final int z) {
    super.setZ(z);

    for(final Entry entry : this.entries) {
      entry.setZ(z - 1);
    }
  }

  public void add(final T data) {
    final Entry entry = this.addControl(new Entry(data));
    this.entries.add(entry);
    entry.setSize(this.getWidth(), this.entryHeight);
    entry.setZ(this.getZ() - 1);
    entry.hide();
    entry.ignoreInput();
    this.updateEntries();

    if(this.entries.size() == 1) {
      this.select(0);
    }
  }

  public void remove(final T data) {
    this.findControl(ListBox.Entry.class, c -> c.data == data).ifPresent(entry -> {
      this.removeControl(entry);
      this.entries.removeIf(e -> e.data == data);

      if(this.visibleEntries() < this.maxVisibleEntries) {
        if(this.scroll > 0) {
          this.scroll--;
        } else if(this.slot >= this.entries.size() && !this.entries.isEmpty()) {
          this.select(this.entries.size() - 1);
        }
      }

      this.updateEntries();
    });
  }

  public void clear() {
    this.scroll = 0;
    this.slot = 0;
  }

  public void sort(final Comparator<T> comparator) {
    this.entries.sort((o1, o2) -> comparator.compare(o1.data, o2.data));
    this.updateEntries();
    this.select(this.slot);
  }

  public int getCount() {
    return this.entries.size();
  }

  public boolean isHighlightShown() {
    return this.highlight.isVisible();
  }

  public void showHighlight() {
    this.highlight.show();
  }

  public void hideHighlight() {
    this.highlight.hide();
  }

  private void select(final int index) {
    if(index < 0) {
      throw new IllegalArgumentException("Index must be > 0");
    }

    this.slot = index;

    this.highlight.setPos(34, index * this.entryHeight + 1);

    if(this.highlightHandler != null) {
      final T entry = this.getSelectedEntry();

      if(entry != null) {
        this.highlightHandler.highlight(entry);
      }
    }
  }

  private void updateEntries() {
    for(int i = 0; i < this.entries.size(); i++) {
      final Entry entry = this.entries.get(i);

      if(i >= this.scroll && i < this.scroll + this.maxVisibleEntries) {
        if(this.isDisabled != null) {
          if(this.isDisabled.test(entry.data)) {
            entry.updateText(TextColour.LIGHT_BROWN);
          } else {
            entry.updateText(TextColour.BROWN);
          }
        }

        entry.setY((i - this.scroll) * this.entryHeight);
        entry.show();
      } else {
        entry.hide();
      }
    }

    this.highlight.setSize(this.getWidth(), this.entryHeight);
    this.upArrow.setVisibility(this.scroll > 0);
    this.downArrow.setVisibility(this.entries.size() - this.scroll > this.maxVisibleEntries);
  }

  private int visibleEntries() {
    return Math.min(this.maxVisibleEntries, this.entries.size() - this.scroll);
  }

  @Override
  public void setSize(final int width, final int height) {
    for(final Entry entry : this.entries) {
      entry.setSize(width, this.entryHeight);
    }

    super.setSize(width, (height + this.entryHeight - 1) / this.entryHeight * this.entryHeight);

    this.maxVisibleEntries = this.getHeight() / this.entryHeight;
    this.updateEntries();
  }

  @Override
  protected InputPropagation mouseMove(final int x, final int y) {
    if(super.mouseMove(x, y) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    for(int i = 0; i < this.visibleEntries(); i++) {
      if(this.slot != i && MathHelper.inBox(x, y, 0, i * this.entryHeight + 1, this.getWidth(), this.entryHeight)) {
        playMenuSound(1);
        this.select(i);
        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation mouseClick(final int x, final int y, final int button, final int mods) {
    if(super.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    for(int i = 0; i < this.visibleEntries(); i++) {
      if(MathHelper.inBox(x, y, 0, i * this.entryHeight + 1, this.getWidth(), this.entryHeight)) {
        this.select(i);

        if(this.isDisabled != null && this.isDisabled.test(this.getSelectedEntry())) {
          return InputPropagation.HANDLED;
        }

        if(this.selectionHandler != null) {
          this.selectionHandler.selection(this.getSelectedEntry());
        }

        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation mouseScrollHighRes(final double deltaX, final double deltaY) {
    if(super.mouseScrollHighRes(deltaX, deltaY) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.scrollAccumulator < 0 && deltaY > 0 || this.scrollAccumulator > 0 && deltaY < 0) {
      this.scrollAccumulator = 0;
    }

    this.scrollAccumulator += deltaY;
    return InputPropagation.HANDLED;
  }

  @Override
  protected InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_SOUTH) {
      if(this.isDisabled != null && this.isDisabled.test(this.getSelectedEntry())) {
        return InputPropagation.HANDLED;
      }

      if(this.selectionHandler != null) {
        this.selectionHandler.selection(this.getSelectedEntry());
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
        if(this.slot > 0) {
          playMenuSound(1);
          this.select(this.slot - 1);
          return InputPropagation.HANDLED;
        } else if(this.scroll > 0) {
          playMenuSound(1);
          this.scroll--;
          this.updateEntries();
          this.select(this.slot);
          return InputPropagation.HANDLED;
        }
      }

      case DPAD_DOWN, JOYSTICK_LEFT_BUTTON_DOWN -> {
        if(this.slot < this.visibleEntries() - 1) {
          playMenuSound(1);
          this.select(this.slot + 1);
          return InputPropagation.HANDLED;
        } else if(this.scroll < this.entries.size() - this.maxVisibleEntries) {
          playMenuSound(1);
          this.scroll++;
          this.updateEntries();
          this.select(this.slot);
          return InputPropagation.HANDLED;
        }
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected void render(final int x, final int y) {
    if(this.scrollAccumulator >= 1.0d) {
      this.scrollAccumulator -= 1.0d;

      if(this.scroll > 0) {
        playMenuSound(1);
        this.scroll--;
        this.updateEntries();
        this.select(this.slot);
      }
    }

    if(this.scrollAccumulator <= -1.0d) {
      this.scrollAccumulator += 1.0d;

      if(this.scroll < this.entries.size() - this.maxVisibleEntries) {
        playMenuSound(1);
        this.scroll++;
        this.updateEntries();
        this.select(this.slot);
      }
    }
  }

  public void onHighlight(final Highlight<T> handler) {
    this.highlightHandler = handler;
  }

  public void onSelection(final Selection<T> handler) {
    this.selectionHandler = handler;
  }

  private Highlight<T> highlightHandler;
  private Selection<T> selectionHandler;

  @FunctionalInterface public interface Highlight<T> { void highlight(T type); }
  @FunctionalInterface public interface Selection<T> { void selection(T type); }

  public class Entry extends Control {
    public final T data;
    private String string;
    private TextColour colour;

    public Entry(final T data) {
      this.data = data;
      this.updateText(TextColour.BROWN);
    }

    private void updateText(final TextColour colour) {
      this.string = ListBox.this.entryToString.apply(this.data);
      this.colour = colour;
    }

    @Override
    protected void render(final int x, final int y) {
      final int oldZ = textZ_800bdf00;
      textZ_800bdf00 = this.getZ() - 1;
      renderText(this.string, x + 28, y + 3, this.colour);
      textZ_800bdf00 = oldZ;

      if(ListBox.this.entryToIcon != null) {
        renderItemIcon(ListBox.this.entryToIcon.applyAsInt(this.data), x + 13, y + 1, 0x8);
      }

      if(ListBox.this.entryToRightIcon != null) {
        final int icon = ListBox.this.entryToRightIcon.applyAsInt(this.data);

        if(icon != -1) {
          renderItemIcon(48 | icon, x + this.getWidth() - 20, y + 1, 0x8).clut_30 = (500 + icon & 0x1ff) << 6 | 0x2b;
        }
      }
    }

    /** Override here to allow access above */
    @Override
    protected boolean containsPoint(final int x, final int y) {
      return super.containsPoint(x, y);
    }
  }
}
