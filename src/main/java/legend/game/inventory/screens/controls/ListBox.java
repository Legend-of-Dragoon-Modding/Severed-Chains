package legend.game.inventory.screens.controls;

import legend.core.MathHelper;
import legend.game.input.InputAction;
import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.TextColour;
import legend.game.types.LodString;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.renderItemIcon;
import static legend.game.SItem.renderText;
import static legend.game.Scus94491BpeSegment_8002.playSound;

public class ListBox<T> extends Control {
  private final Function<T, LodString> entryToString;
  @Nullable
  private final Function<T, Integer> entryToIcon;
  private final List<Entry> entries = new ArrayList<>();
  private final int entryHeight = 17;
  private int maxVisibleEntries;

  private double scrollAccumulator;
  private int scroll;
  private int slot;

  private final Glyph highlight;

  private final Glyph upArrow;
  private final Glyph downArrow;

  public ListBox(final Function<T, LodString> entryToString, @Nullable final Function<T, Integer> entryToIcon) {
    this.entryToString = entryToString;
    this.entryToIcon = entryToIcon;

    this.highlight = this.addControl(Glyph.uiElement(118, 118));
    FUN_80104b60(this.highlight.getRenderable()); //TODO not sure exactly what this does but without it the middle part of the highlight doesn't stretch
    this.upArrow = this.addControl(Glyph.uiElement(61, 68));
    this.downArrow = this.addControl(Glyph.uiElement(53, 60));

    this.setSize(171, 119);

    this.highlight.setPos(34, 1);
    this.highlight.ignoreInput();
    this.upArrow.setPos(this.getWidth(), 0);
    this.upArrow.ignoreInput();
    this.downArrow.setPos(this.getWidth(), this.getHeight() - 15);
    this.downArrow.ignoreInput();
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

  public void add(final T data) {
    final Entry entry = this.addControl(new Entry(data));
    this.entries.add(entry);
    entry.setSize(this.getWidth(), this.entryHeight);
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
  protected void mouseMove(final int x, final int y) {
    super.mouseMove(x, y);

    for(int i = 0; i < this.visibleEntries(); i++) {
      if(this.slot != i && MathHelper.inBox(x, y, 0, i * this.entryHeight + 1, this.getWidth(), this.entryHeight)) {
        playSound(1);
        this.select(i);
      }
    }
  }

  @Override
  protected void mouseClick(final int x, final int y, final int button, final int mods) {
    super.mouseClick(x, y, button, mods);

    for(int i = 0; i < this.visibleEntries(); i++) {
      if(MathHelper.inBox(x, y, 0, i * this.entryHeight + 1, this.getWidth(), this.entryHeight)) {
        this.select(i);

        if(this.selectionHandler != null) {
          this.selectionHandler.selection(this.getSelectedEntry());
        }
      }
    }
  }

  @Override
  protected void mouseScroll(final double deltaX, final double deltaY) {
    super.mouseScroll(deltaX, deltaY);

    if(this.scrollAccumulator < 0 && deltaY > 0 || this.scrollAccumulator > 0 && deltaY < 0) {
      this.scrollAccumulator = 0;
    }

    this.scrollAccumulator += deltaY;
  }

  @Override
  protected void pressedThisFrame(final InputAction inputAction) {
    super.pressedThisFrame(inputAction);

    if(inputAction == InputAction.BUTTON_SOUTH) {
      if(this.selectionHandler != null) {
        this.selectionHandler.selection(this.getSelectedEntry());
      }
    }
  }

  @Override
  protected void pressedWithRepeatPulse(final InputAction inputAction) {
    super.pressedWithRepeatPulse(inputAction);

    switch(inputAction) {
      case DPAD_UP, JOYSTICK_LEFT_BUTTON_UP -> {
        if(this.slot > 0) {
          playSound(1);
          this.select(this.slot - 1);
        } else if(this.scroll > 0) {
          playSound(1);
          this.scroll--;
          this.updateEntries();
          this.select(this.slot);
        }
      }

      case DPAD_DOWN, JOYSTICK_LEFT_BUTTON_DOWN -> {
        if(this.slot < this.visibleEntries() - 1) {
          playSound(1);
          this.select(this.slot + 1);
        } else if(this.scroll < this.entries.size() - this.maxVisibleEntries) {
          playSound(1);
          this.scroll++;
          this.updateEntries();
          this.select(this.slot);
        }
      }
    }
  }

  @Override
  protected void render(final int x, final int y) {
    if(this.scrollAccumulator >= 1.0d) {
      this.scrollAccumulator -= 1.0d;

      if(this.scroll > 0) {
        playSound(1);
        this.scroll--;
        this.updateEntries();
        this.select(this.slot);
      }
    }

    if(this.scrollAccumulator <= -1.0d) {
      this.scrollAccumulator += 1.0d;

      if(this.scroll < this.entries.size() - this.maxVisibleEntries) {
        playSound(1);
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

    public Entry(final T data) {
      this.data = data;
    }

    @Override
    protected void render(final int x, final int y) {
      renderText(ListBox.this.entryToString.apply(this.data), x + 28, y + 3, TextColour.BROWN);

      if(ListBox.this.entryToIcon != null) {
        renderItemIcon(ListBox.this.entryToIcon.apply(this.data), x + 13, y + 1, 0x8L);
      }
    }

    /** Override here to allow access above */
    @Override
    protected boolean containsPoint(final int x, final int y) {
      return super.containsPoint(x, y);
    }
  }
}
