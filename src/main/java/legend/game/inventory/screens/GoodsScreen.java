package legend.game.inventory.screens;

import legend.game.i18n.I18n;
import legend.game.input.InputAction;
import legend.game.inventory.screens.controls.Background;
import legend.game.inventory.screens.controls.Glyph;
import legend.game.inventory.screens.controls.ItemList;
import legend.game.inventory.screens.controls.Label;
import legend.game.inventory.screens.controls.ListBox;
import legend.game.types.MenuEntryStruct04;

import static legend.core.IoHelper.getPackedFlag;
import static legend.game.SItem.goodsDescriptions_8011b75c;
import static legend.game.SItem.goodsItemNames_8011c008;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;

public class GoodsScreen extends MenuScreen {
  private final Runnable unload;

  private final ItemList<Integer> leftList;
  private final ItemList<Integer> rightList;
  private final Label description = new Label("");

  public GoodsScreen(final Runnable unload) {
    deallocateRenderables(0xff);
    startFadeEffect(2, 10);

    this.unload = unload;

    final ListBox.Highlight<MenuEntryStruct04<Integer>> description = item -> this.description.setText(item == null || item.item_00 >= 0xff ? "" : goodsDescriptions_8011b75c[item.item_00]);

    this.leftList = new ItemList<>(entry -> I18n.translate(entry.getNameTranslationKey()), null, null, null);
    this.leftList.setPos(8, 15);
    this.leftList.setTitle("Goods");

    this.rightList = new ItemList<>(entry -> I18n.translate(entry.getNameTranslationKey()), null, null, null);
    this.rightList.setPos(188, 15);
    this.rightList.setTitle("Goods");

    this.leftList.onHoverIn(() -> this.setFocus(this.leftList));
    this.leftList.onGotFocus(() -> {
      this.leftList.showHighlight();
      this.rightList.hideHighlight();
      this.description.show();
      description.highlight(this.leftList.getSelectedItem());
    });
    this.leftList.onPressedThisFrame(inputAction -> {
      if(inputAction == InputAction.DPAD_RIGHT || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_RIGHT) {
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
    this.rightList.onPressedThisFrame(inputAction -> {
      if(inputAction == InputAction.DPAD_LEFT || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_LEFT) {
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
        final MenuEntryStruct04<Integer> item = new MenuEntryStruct04<>(goodsIndex -> goodsItemNames_8011c008[goodsIndex], null, null, i);

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
  public InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(inputAction == InputAction.BUTTON_EAST) {
      this.menuEscape();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }
}
