package legend.game.inventory.screens.controls;

import legend.game.input.InputAction;
import legend.game.inventory.screens.Control;
import legend.game.inventory.screens.InputPropagation;
import legend.game.types.MenuItemStruct04;
import legend.game.types.Renderable58;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.equipment_8011972c;
import static legend.game.Scus94491BpeSegment_8002.allocateManualRenderable;
import static legend.game.Scus94491BpeSegment_8002.getItemIcon;
import static legend.game.Scus94491BpeSegment_8002.textWidth;
import static legend.game.Scus94491BpeSegment_8002.uploadRenderable;

public class ItemList extends Control {
  private final ListBox<MenuItemStruct04> items;
  private final Renderable58 background;
  private final Label titleLabel;
  private final Label maxLabel;
  private int max;

  public ItemList() {
    this(
      menuItem -> equipment_8011972c.get(menuItem.itemId_00).deref().get(),
      menuItem -> getItemIcon(menuItem.itemId_00),
      menuItem -> (menuItem.flags_02 & 0x1000) != 0 ? menuItem.flags_02 & 0xf : -1,
      menuItem -> (menuItem.flags_02 & 0x1000) != 0
    );
  }

  public ItemList(final Function<MenuItemStruct04, String> getItemName, @Nullable final Function<MenuItemStruct04, Integer> getItemIcon, @Nullable final Function<MenuItemStruct04, Integer> getFaceIcon, @Nullable final Function<MenuItemStruct04, Boolean> isDisabled) {
    this.setSize(173, 147);

    this.items = new ListBox<>(getItemName, getItemIcon, getFaceIcon, isDisabled);
    this.items.setPos(0, 26);
    this.items.setSize(173, 119);
    this.addControl(this.items);

    this.titleLabel = this.addControl(new Label(""));
    this.titleLabel.setPos(24, 8);

    this.maxLabel = this.addControl(new Label(""));
    this.maxLabel.setY(8);
    this.maxLabel.hide();

    this.background = allocateUiElement(allocateManualRenderable(), 0x55, 0x55, 0, 0);
    this.background.z_3c = 80;
  }

  public void setTitle(final String title) {
    this.titleLabel.setText(title);
  }

  public String getTitle() {
    return this.titleLabel.getText();
  }

  public void setMax(final int max) {
    this.max = max;
    this.updateMaxLabel();
    this.maxLabel.setVisibility(max != 0);
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
    this.updateMaxLabel();
  }

  public void remove(final MenuItemStruct04 item) {
    this.items.remove(item);
    this.updateMaxLabel();
  }

  public void clear() {
    this.items.clear();
    this.updateMaxLabel();
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

  private void updateMaxLabel() {
    this.maxLabel.setText(this.items.getCount() + "/" + this.max);
    this.maxLabel.setX(162 - textWidth(this.maxLabel.getText()));
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
  }
}
