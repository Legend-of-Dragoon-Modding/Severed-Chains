package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputMod;
import legend.game.additions.Addition;
import legend.game.additions.CharacterAdditionStats;
import legend.game.i18n.I18n;
import legend.game.types.CharacterData2c;
import legend.game.types.Renderable58;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static legend.core.GameEngine.PLATFORM;
import static legend.game.Audio.playMenuSound;
import static legend.game.FullScreenEffects.startFadeEffect;
import static legend.game.Menus.deallocateRenderables;
import static legend.game.Menus.unloadRenderable;
import static legend.game.SItem.FUN_801034cc;
import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.UI_TEXT;
import static legend.game.SItem.UI_TEXT_SELECTED;
import static legend.game.SItem.additionGlyphs_801141e4;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.characterCount_8011d7c4;
import static legend.game.SItem.loadAdditions;
import static legend.game.SItem.renderCharacter;
import static legend.game.SItem.renderCharacterSlot;
import static legend.game.SItem.renderGlyphs;
import static legend.game.SItem.renderThreeDigitNumber;
import static legend.game.Scus94491BpeSegment_8004.CHARACTER_ADDITIONS;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Text.renderText;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_END;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_HOME;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_LEFT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_RIGHT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_UP;

public class AdditionsScreen extends MenuScreen {
  private static final String Addition_cannot_be_used_8011c340 = "Additions cannot be used";

  private int loadingStage;
  private double scrollAccumulator;
  private final Runnable unload;

  private int charSlot;
  private int selectedSlot;
  private Renderable58 additionHighlight;
  private final List<Addition> additions = new ArrayList<>();

  /** Allows list wrapping, but only on new input */
  private boolean allowWrapX = true;
  private boolean allowWrapY = true;

  public AdditionsScreen(final Runnable unload) {
    this.unload = unload;
  }

  @Override
  protected void render() {
    switch(this.loadingStage) {
      case 0 -> {
        this.charSlot = 0;
        this.selectedSlot = 0;
        this.additionHighlight = null;
        startFadeEffect(2, 10);
        deallocateRenderables(0xff);
        this.loadingStage++;
      }

      case 1 -> {
        deallocateRenderables(0);
        loadAdditions(characterIndices_800bdbb8[this.charSlot], this.additions);

        if(!this.additions.isEmpty()) {
          this.additionHighlight = allocateUiElement(117, 117, 39, this.getAdditionSlotY(this.selectedSlot) - 4);
          FUN_80104b60(this.additionHighlight);
        }

        allocateUiElement(69, 69, 0, 0); // Background left
        allocateUiElement(70, 70, 192, 0); // Background right
        this.renderAdditions(this.charSlot, this.additions, gameState_800babc8.charData_32c[characterIndices_800bdbb8[this.charSlot]].selectedAddition_19, 0xffL);
        this.loadingStage++;
      }

      case 2 -> {
        FUN_801034cc(this.charSlot, characterCount_8011d7c4); // Left/right arrows
        this.renderAdditions(this.charSlot, this.additions, gameState_800babc8.charData_32c[characterIndices_800bdbb8[this.charSlot]].selectedAddition_19, 0);

        if(this.scrollAccumulator >= 1.0d) {
          this.scrollAccumulator -= 1.0d;

          if(this.charSlot > 0) {
            this.scroll(this.charSlot - 1);
          }
        }

        if(this.scrollAccumulator <= -1.0d) {
          this.scrollAccumulator += 1.0d;

          if(this.charSlot < characterCount_8011d7c4 - 1) {
            this.scroll(this.charSlot + 1);
          }
        }
      }

      // Fade out
      case 100 -> {
        this.renderAdditions(this.charSlot, this.additions, gameState_800babc8.charData_32c[characterIndices_800bdbb8[this.charSlot]].selectedAddition_19, 0);
        this.unload.run();
      }
    }
  }

  private void renderAdditions(final int charSlot, final List<Addition> additions, final RegistryId selectedAddition, final long a4) {
    final boolean allocate = a4 == 0xff;
    final int charIndex = characterIndices_800bdbb8[charSlot];

    if(additions.isEmpty()) {
      renderText(Addition_cannot_be_used_8011c340, 106, 150, UI_TEXT);
    } else {
      if(allocate) {
        renderGlyphs(additionGlyphs_801141e4, 0, 0);
      }

      final CharacterData2c charData = gameState_800babc8.charData_32c[charIndex];

      if(allocate) {
        for(int i = 0; i < CHARACTER_ADDITIONS[charIndex].length; i++) {
          final int y = this.getAdditionSlotY(i);
          renderCharacter(24, y, i + 1); // Addition number
        }
      }

      for(int i = 0; i < this.additions.size(); i++) {
        final Addition addition = this.additions.get(i);
        final int y = this.getAdditionSlotY(i);

        renderText(I18n.translate(addition), 33, y - 2, !addition.getRegistryId().equals(selectedAddition) ? UI_TEXT : UI_TEXT_SELECTED);

        if(allocate) {
          final CharacterAdditionStats additionStats = charData.additionStats.get(addition.getRegistryId());
          final int level = additionStats.level + 1;
          renderThreeDigitNumber(197, y, level);
          renderThreeDigitNumber(230, y, addition.getHitCount(charData, additionStats));
          renderThreeDigitNumber(263, y, addition.getSp(charData, additionStats));
          renderThreeDigitNumber(297, y, addition.getDamage(charData, additionStats));
          renderThreeDigitNumber(322, y, additionStats.xp);

          if(level < 5) {
            renderThreeDigitNumber(342, y, level * 20); // Max XP
          } else {
            renderCharacter(354, y, 218); // Dash if at max XP
          }
        }
      }
    }

    renderCharacterSlot(16, 21, charIndex, allocate, false);
  }

  private int getAdditionSlotY(final int slot) {
    return 113 + slot * 14;
  }

  private void scroll(final int scroll) {
    playMenuSound(1);
    this.charSlot = scroll;
    unloadRenderable(this.additionHighlight);
    this.loadingStage = 1;
  }

  @Override
  protected InputPropagation mouseMove(final int x, final int y) {
    if(super.mouseMove(x, y) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage != 2) {
      return InputPropagation.PROPAGATE;
    }

    for(int i = 0; i < 7; i++) {
      if(!this.additions.isEmpty() && this.selectedSlot != i && MathHelper.inBox(x, y, 31, this.getAdditionSlotY(i) - 3, 141, 13)) {
        playMenuSound(1);
        this.selectedSlot = i;
        this.additionHighlight.y_44 = this.getAdditionSlotY(i) - 4;
        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation mouseClick(final int x, final int y, final int button, final Set<InputMod> mods) {
    if(super.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage != 2 || !mods.isEmpty()) {
      return InputPropagation.PROPAGATE;
    }

    if(button == PLATFORM.getMouseButton(0) && !this.additions.isEmpty()) {
      for(int i = 0; i < this.additions.size(); i++) {
        if(MathHelper.inBox(x, y, 31, this.getAdditionSlotY(i) - 3, 141, 13)) {
          this.selectedSlot = i;
          this.additionHighlight.y_44 = this.getAdditionSlotY(i) - 4;
          gameState_800babc8.charData_32c[characterIndices_800bdbb8[this.charSlot]].selectedAddition_19 = this.additions.get(i).getRegistryId();
          playMenuSound(2);
          unloadRenderable(this.additionHighlight);
          this.loadingStage = 1;

          return InputPropagation.HANDLED;
        }
      }
    }

    return InputPropagation.PROPAGATE;
  }

  private void menuEscape() {
    playMenuSound(3);
    this.loadingStage = 100;
  }

  private void menuNavigateUp() {
    if(!this.additions.isEmpty()) {
      if(this.selectedSlot > 0) {
        playMenuSound(1);
        this.selectedSlot--;
      } else if(this.allowWrapY) {
        playMenuSound(1);
        this.selectedSlot = 6;
      }

      this.additionHighlight.y_44 = this.getAdditionSlotY(this.selectedSlot) - 4;
    }
  }

  private void menuNavigateDown() {
    if(!this.additions.isEmpty()) {
      if(this.selectedSlot < 6) {
        playMenuSound(1);
        this.selectedSlot++;
      } else if(this.allowWrapY) {
        playMenuSound(1);
        this.selectedSlot = 0;
      }

      this.additionHighlight.y_44 = this.getAdditionSlotY(this.selectedSlot) - 4;
    }
  }

  private void menuNavigateLeft() {
    if(this.charSlot > 0) {
      this.scroll(this.charSlot - 1);
    } else if(characterCount_8011d7c4 > 1 && this.allowWrapX) {
      this.scroll(characterCount_8011d7c4 - 1);
    }
  }

  private void menuNavigateRight() {
    if(this.charSlot < characterCount_8011d7c4 - 1) {
      this.scroll(this.charSlot + 1);
    } else if(characterCount_8011d7c4 > 1 && this.allowWrapX) {
      this.scroll(0);
    }
  }

  private void menuNavigateHome() {
    if(this.selectedSlot != 0) {
      playMenuSound(1);
      this.selectedSlot = 0;
      this.additionHighlight.y_44 = this.getAdditionSlotY(this.selectedSlot) - 4;
    }
  }

  private void menuNavigateEnd() {
    if(this.selectedSlot != 6) {
      playMenuSound(1);
      this.selectedSlot = 6;
      this.additionHighlight.y_44 = this.getAdditionSlotY(this.selectedSlot) - 4;
    }
  }

  private void menuSelect() {
    if(this.selectedSlot < this.additions.size()) {
      gameState_800babc8.charData_32c[characterIndices_800bdbb8[this.charSlot]].selectedAddition_19 = this.additions.get(this.selectedSlot).getRegistryId();
      playMenuSound(2);
      unloadRenderable(this.additionHighlight);
      this.loadingStage = 1;
    } else {
      playMenuSound(40);
    }
  }

  @Override
  protected InputPropagation mouseScrollHighRes(final double deltaX, final double deltaY) {
    if(super.mouseScrollHighRes(deltaX, deltaY) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage != 2) {
      return InputPropagation.PROPAGATE;
    }

    if(this.scrollAccumulator < 0 && deltaY > 0 || this.scrollAccumulator > 0 && deltaY < 0) {
      this.scrollAccumulator = 0;
    }

    this.scrollAccumulator += deltaY;
    return InputPropagation.HANDLED;
  }

  @Override
  public InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(super.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage != 2) {
      return InputPropagation.PROPAGATE;
    }

    if(action == INPUT_ACTION_MENU_HOME.get()) {
      this.menuNavigateHome();
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_END.get()) {
      this.menuNavigateEnd();
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

    if(action == INPUT_ACTION_MENU_LEFT.get()) {
      this.menuNavigateLeft();
      this.allowWrapX = false;
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_RIGHT.get()) {
      this.menuNavigateRight();
      this.allowWrapX = false;
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_BACK.get() && !repeat) {
      this.menuEscape();
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_CONFIRM.get() && !repeat) {
      this.menuSelect();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  public InputPropagation inputActionReleased(final InputAction action) {
    if(super.inputActionReleased(action) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage != 2) {
      return InputPropagation.PROPAGATE;
    }

    if(action == INPUT_ACTION_MENU_UP.get() || action == INPUT_ACTION_MENU_DOWN.get()) {
      this.allowWrapY = true;
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_LEFT.get() || action == INPUT_ACTION_MENU_RIGHT.get()) {
      this.allowWrapX = true;
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }
}
