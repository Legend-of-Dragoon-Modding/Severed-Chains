package legend.game.inventory.screens.controls;

import legend.game.input.InputAction;
import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.InputPropagation;
import legend.game.inventory.screens.TextColour;
import legend.game.types.LodString;
import legend.game.types.MenuItemStruct04;
import legend.game.types.Renderable58;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.equipment_8011972c;
import static legend.game.SItem.renderText;
import static legend.game.Scus94491BpeSegment_8002.allocateManualRenderable;
import static legend.game.Scus94491BpeSegment_8002.getItemIcon;
import static legend.game.Scus94491BpeSegment_8002.textWidth;
import static legend.game.Scus94491BpeSegment_8002.uploadRenderable;

public class ItemList extends Control {
  private final ListBox<MenuItemStruct04> items;
  private final Renderable58 background;
  private LodString title = new LodString("");
  private int max;

  public ItemList() {
    this(menuItem -> equipment_8011972c.get(menuItem.itemId_00).deref(), menuItem -> getItemIcon(menuItem.itemId_00));
  }

  public ItemList(final Function<MenuItemStruct04, LodString> getItemName, @Nullable final Function<MenuItemStruct04, Integer> getItemIcon) {
    this.setSize(173, 147);

    this.items = new ListBox<>(getItemName, getItemIcon);
    this.items.setPos(0, 26);
    this.items.setSize(173, 119);
    this.addControl(this.items);

    this.background = allocateUiElement(allocateManualRenderable(), 0x55, 0x55, 0, 0);
    this.background.z_3c = 80;
  }

  public void setTitle(final LodString title) {
    this.title = title;
  }

  public void setTitle(final String title) {
    this.title = new LodString(title);
  }

  public LodString getTitle() {
    return this.title;
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

  public MenuItemStruct04 getSelectedItem() {
    return this.items.getSelectedEntry();
  }

  public List<MenuItemStruct04> getItems() {
    return this.items.getEntries();
  }

  public void add(final MenuItemStruct04 item) {
    this.items.add(item);
  }

  public void remove(final MenuItemStruct04 item) {
    this.items.remove(item);
  }

  public void clear() {
    this.items.clear();
  }

  public void sort(final Comparator<MenuItemStruct04> comparator) {
    this.items.sort(comparator);
  }

  public void onHighlight(final ListBox.Highlight<MenuItemStruct04> handler) {
    this.items.onHighlight(handler);
  }

  public void onSelection(final ListBox.Selection<MenuItemStruct04> handler) {
    this.items.onSelection(handler);
  }

  @Override
  protected InputPropagation pressedWithRepeatPulse(final InputAction inputAction) {
    if(super.pressedWithRepeatPulse(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    return this.items.pressedWithRepeatPulse(inputAction);
  }

  @Override
  protected void render(final int x, final int y) {
    uploadRenderable(this.background, x + 8, y + 1);

    renderText(this.title, x + 24, y + 8, TextColour.BROWN);

    if(this.max != 0) {
      final LodString count = new LodString(this.items.getCount() + "/" + this.max);
      renderText(count, x + 162 - textWidth(count), y + 8, TextColour.BROWN);
    }
  }
}
