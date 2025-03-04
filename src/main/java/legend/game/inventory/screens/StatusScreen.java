package legend.game.inventory.screens;

import legend.core.platform.input.InputAction;

import static legend.game.SItem.FUN_801034cc;
import static legend.game.SItem.UI_TEXT;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.characterCount_8011d7c4;
import static legend.game.SItem.characterStatusGlyphs_801141a4;
import static legend.game.SItem.hasDragoon;
import static legend.game.SItem.renderCharacter;
import static legend.game.SItem.renderCharacterEquipment;
import static legend.game.SItem.renderCharacterSlot;
import static legend.game.SItem.renderCharacterStats;
import static legend.game.SItem.renderGlyphs;
import static legend.game.SItem.renderThreeDigitNumber;
import static legend.game.SItem.spellMp_80114290;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.getUnlockedDragoonSpells;
import static legend.game.Scus94491BpeSegment_8002.getUnlockedSpellCount;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.combat.Battle.spellStats_800fa0b8;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_LEFT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_RIGHT;

public class StatusScreen extends MenuScreen {
  protected int loadingStage;

  private int charSlot;

  private boolean allowWrapX = true;
  private double scrollAccumulator;

  private final Runnable unload;

  public StatusScreen(final Runnable unload) {
    this.unload = unload;
  }

  @Override
  protected void render() {
    switch(this.loadingStage) {
      case 0 -> {
        startFadeEffect(2, 10);
        this.loadingStage++;
      }

      case 1 -> {
        deallocateRenderables(0xff);
        deallocateRenderables(0);
        renderGlyphs(characterStatusGlyphs_801141a4, 0, 0);
        this.renderStatusMenu(this.charSlot, 0xff);
        this.loadingStage++;
      }

      case 2 -> {
        FUN_801034cc(this.charSlot, characterCount_8011d7c4);
        this.renderStatusMenu(this.charSlot, 0);

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

      case 3 -> {
        FUN_801034cc(this.charSlot, characterCount_8011d7c4);
        this.renderStatusMenu(this.charSlot, 0);
        this.unload.run();
      }
    }
  }

  private void scroll(final int slot) {
    if(characterCount_8011d7c4 > 1) {
      playMenuSound(1);
      this.charSlot = slot;
      this.loadingStage = 1;
    }
  }

  private void renderStatusMenu(final int charSlot, final long a1) {
    renderCharacterStats(characterIndices_800bdbb8[charSlot], null, a1 == 0xff);
    renderCharacterSlot(16, 21, characterIndices_800bdbb8[charSlot], a1 == 0xff, false);
    renderCharacterEquipment(characterIndices_800bdbb8[charSlot], a1 == 0xff);
    this.renderCharacterSpells(characterIndices_800bdbb8[charSlot], a1 == 0xff);
  }

  private void renderCharacterSpells(final int charIndex, final boolean allocate) {
    if(charIndex == -1) {
      return;
    }

    if(allocate) {
      allocateUiElement(0x58, 0x58, 194, 101);
    }

    if(hasDragoon(gameState_800babc8.goods_19c[0], charIndex)) {
      final int[] spellIndices = new int[8];
      getUnlockedDragoonSpells(spellIndices, charIndex);
      final int unlockedSpellCount = getUnlockedSpellCount(charIndex);

      for(int i = 0; i < 4; i++) {
        if(allocate && i < unlockedSpellCount) {
          renderCharacter(200, 127 + i * 14, i + 1);
        }

        //LAB_80109370
        final int spellIndex = spellIndices[i];
        if(spellIndex != -1) {
          renderText(spellStats_800fa0b8[spellIndex].name, 210, 125 + i * 14, UI_TEXT);

          if(allocate) {
            renderThreeDigitNumber(342, 128 + i * 14, spellMp_80114290[spellIndex]);
          }
        }
      }
    }
  }

  private void menuEscape() {
    playMenuSound(3);
    this.loadingStage = 3;
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

    return InputPropagation.PROPAGATE;
  }

  @Override
  public InputPropagation inputActionReleased(final InputAction action) {
    if(super.inputActionReleased(action) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_LEFT.get() || action == INPUT_ACTION_MENU_RIGHT.get()) {
      this.allowWrapX = true;
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }
}
