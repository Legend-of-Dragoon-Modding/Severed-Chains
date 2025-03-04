package legend.game.inventory.screens.controls;

import it.unimi.dsi.fastutil.ints.Int2IntFunction;
import legend.core.platform.input.InputAction;
import legend.game.i18n.I18n;
import legend.game.inventory.ItemIcon;
import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.InputPropagation;
import legend.game.types.MenuEntryStruct04;
import legend.game.types.Renderable58;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;

import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.renderFraction;
import static legend.game.Scus94491BpeSegment_8002.allocateManualRenderable;
import static legend.game.Scus94491BpeSegment_8002.uploadRenderable;

public class ItemList<T> extends Control {
  private final Int2IntFunction itemCount;
  private final ListBox<MenuEntryStruct04<T>> items;
  private final Renderable58 background;
  private final Label titleLabel;
  private int max;

  public ItemList() {
    this(null);
  }

  public ItemList(@Nullable final Int2IntFunction itemCount) {
    this(
      entry -> I18n.translate(entry.getNameTranslationKey()),
      MenuEntryStruct04::getIcon,
      menuItem -> (menuItem.flags_02 & 0x1000) != 0 ? menuItem.flags_02 & 0xf : -1,
      menuItem -> (menuItem.flags_02 & 0x1000) != 0,
      itemCount
    );
  }

  public ItemList(final Function<MenuEntryStruct04<T>, String> getItemName, @Nullable final Function<MenuEntryStruct04<T>, ItemIcon> getItemIcon, @Nullable final ToIntFunction<MenuEntryStruct04<T>> getFaceIcon, @Nullable final Predicate<MenuEntryStruct04<T>> isDisabled, @Nullable final Int2IntFunction itemCount) {
    this.setSize(173, 147);

    this.items = new ListBox<>(getItemName, getItemIcon, getFaceIcon, isDisabled);
    this.items.setPos(0, 26);
    this.items.setSize(173, 119);
    this.addControl(this.items);

    this.titleLabel = this.addControl(new Label(""));
    this.titleLabel.setPos(24, 8);

    this.background = allocateUiElement(allocateManualRenderable(), 0x55, 0x55, 0, 0);
    this.background.z_3c = 80;

    this.itemCount = Objects.requireNonNullElseGet(itemCount, () -> i -> i);
  }

  public void setTitle(final String title) {
    this.titleLabel.setText(title);
  }

  public String getTitle() {
    return this.titleLabel.getText();
  }

  public void setMax(final int max) {
    this.max = max;
  }

  public int getMax() {
    return this.max;
  }

  public boolean isHighlightShown() {
    return this.items.isHighlightShown();
  }

  public void showHighlight() {
    this.items.showHighlight();
  }

  public void hideHighlight() {
    this.items.hideHighlight();
  }

  public boolean isEmpty() {
    return this.items.isEmpty();
  }

  public void select(final int index) {
    this.items.select(index);
  }

  public int getSelectedIndex() {
    return this.items.getSelectedIndex();
  }

  public MenuEntryStruct04<T> getSelectedItem() {
    return this.items.getSelectedEntry();
  }

  public List<MenuEntryStruct04<T>> getItems() {
    return this.items.getEntries();
  }

  public void add(final MenuEntryStruct04<T> item) {
    this.items.add(item);
  }

  public void remove(final MenuEntryStruct04<T> item) {
    this.items.remove(item);
  }

  public void clear() {
    this.items.clear();
  }

  public void sort(final Comparator<MenuEntryStruct04<T>> comparator) {
    this.items.sort(comparator);
  }

  public void onHighlight(final ListBox.Highlight<MenuEntryStruct04<T>> handler) {
    this.items.onHighlight(handler);
  }

  public void onSelection(final ListBox.Selection<MenuEntryStruct04<T>> handler) {
    this.items.onSelection(handler);
  }

  @Override
  protected InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(super.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    return this.items.inputActionPressed(action, repeat);
  }

  @Override
  protected InputPropagation inputActionReleased(final InputAction action) {
    if(super.inputActionReleased(action) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    return this.items.inputActionReleased(action);
  }

  @Override
  protected void render(final int x, final int y) {
    uploadRenderable(this.background, x + 8, y + 1);

    if(this.max != 0) {
      renderFraction(this.calculateTotalX() + 169, this.calculateTotalY() + 10, this.itemCount.applyAsInt(this.items.getCount()), this.max);
    }
  }
}
