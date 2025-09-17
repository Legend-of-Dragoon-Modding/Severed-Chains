package legend.game.inventory.screens;

import legend.core.platform.input.InputAction;
import legend.game.inventory.InventoryEntry;
import legend.game.inventory.ItemIcon;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Glyph;
import legend.game.inventory.screens.controls.ItemList;
import legend.game.inventory.screens.controls.Label;
import legend.game.inventory.screens.controls.ListBox;
import legend.game.types.MenuEntryStruct04;
import org.legendofdragoon.modloader.registries.RegistryId;

import static legend.core.IoHelper.getPackedFlag;
import static legend.game.SItem.goodsDescriptions_8011b75c;
import static legend.game.SItem.goodsItemNames_8011c008;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_LEFT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_RIGHT;

public class GoodsScreen extends MenuScreen {
  private final Runnable unload;

  private final ItemList<GoodsEntry> leftList;
  private final ItemList<GoodsEntry> rightList;
  private final Label description = new Label("");

  public GoodsScreen(final Runnable unload) {
    deallocateRenderables(0xff);
    startFadeEffect(2, 10);

    this.unload = unload;

    final ListBox.Highlight<MenuEntryStruct04<GoodsEntry>> description = item -> this.description.setText(item == null || item.item_00.id >= 0xff ? "" : goodsDescriptions_8011b75c[item.item_00.id]);

    this.leftList = new ItemList<>(entry -> goodsItemNames_8011c008[entry.item_00.id], null, null, null, null, null);
    this.leftList.setPos(8, 15);
    this.leftList.setTitle("Goods");

    this.rightList = new ItemList<>(entry -> goodsItemNames_8011c008[entry.item_00.id], null, null, null, null, null);
    this.rightList.setPos(188, 15);
    this.rightList.setTitle("Goods");

    this.leftList.onHoverIn(() -> this.setFocus(this.leftList));
    this.leftList.onGotFocus(() -> {
      this.leftList.showHighlight();
      this.rightList.hideHighlight();
      this.description.show();
      description.highlight(this.leftList.getSelectedItem());
    });
    this.leftList.onInputActionPressed((action, repeat) -> {
      if(action == INPUT_ACTION_MENU_RIGHT.get()) {
        playMenuSound(1);
        this.setFocus(this.rightList);
        this.rightList.select(this.leftList.getSelectedIndex());
        return InputPropagation.HANDLED;
      }

      return InputPropagation.PROPAGATE;
    });
    this.leftList.onHighlight(description);

    this.rightList.onHoverIn(() -> this.setFocus(this.rightList));
    this.rightList.onGotFocus(() -> {
      this.leftList.hideHighlight();
      this.rightList.showHighlight();
      description.highlight(this.rightList.getSelectedItem());
    });
    this.rightList.onInputActionPressed((action, repeat) -> {
      if(action == INPUT_ACTION_MENU_LEFT.get()) {
        playMenuSound(1);
        this.setFocus(this.leftList);
        this.leftList.select(this.rightList.getSelectedIndex());
        return InputPropagation.HANDLED;
      }

      return InputPropagation.PROPAGATE;
    });
    this.rightList.onHighlight(description);

    this.addControl(new Background());
    this.addControl(Glyph.glyph(91)).setPos(194, 173); // Description pane

    this.description.setPos(194, 178);

    this.addControl(this.leftList);
    this.addControl(this.rightList);
    this.addControl(this.description);

    this.setFocus(this.leftList);

    for(int i = 0, listIndex = 0; i < 64; i++) {
      if(getPackedFlag(gameState_800babc8.goods_19c, i)) {
        final MenuEntryStruct04<GoodsEntry> item = new MenuEntryStruct04<>(new GoodsEntry(i));

        if(listIndex % 2 == 0) {
          this.leftList.add(item);
        } else {
          this.rightList.add(item);
        }

        listIndex++;
      }
    }

    description.highlight(this.leftList.getSelectedItem());
  }

  @Override
  protected void render() {

  }

  private void menuEscape() {
    playMenuSound(3);
    this.unload.run();
  }

  @Override
  protected InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(super.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_BACK.get() && !repeat) {
      this.menuEscape();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }

  private static class GoodsEntry implements InventoryEntry {
    public final int id;

    private GoodsEntry(final int id) {
      this.id = id;
    }

    @Override
    public RegistryId getRegistryId() {
      return null;
    }

    @Override
    public ItemIcon getIcon() {
      return null;
    }

    @Override
    public String getNameTranslationKey() {
      return "";
    }

    @Override
    public String getDescriptionTranslationKey() {
      return "";
    }

    @Override
    public int getPrice() {
      return 0;
    }

    @Override
    public int getSize() {
      return 1;
    }

    @Override
    public int getMaxSize() {
      return 1;
    }

    @Override
    public boolean isEmpty() {
      return false;
    }
  }
}
