package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.game.input.InputAction;
import legend.game.types.MenuAdditionInfo;
import legend.game.types.Renderable58;

import java.util.Arrays;

import static legend.game.SItem.FUN_801034cc;
import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.additionGlyphs_801141e4;
import static legend.game.SItem.additions_80114070;
import static legend.game.SItem.additions_8011a064;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.characterCount_8011d7c4;
import static legend.game.SItem.loadAdditions;
import static legend.game.SItem.renderCharacter;
import static legend.game.SItem.renderCharacterSlot;
import static legend.game.SItem.renderGlyphs;
import static legend.game.SItem.renderText;
import static legend.game.SItem.renderThreeDigitNumber;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_8002.unloadRenderable;
import static legend.game.Scus94491BpeSegment_8004.additionCounts_8004f5c0;
import static legend.game.Scus94491BpeSegment_8005.additionData_80052884;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class AdditionsScreen extends MenuScreen {
  private static final String Addition_cannot_be_used_8011c340 = "Additions cannot be used";

  private int loadingStage;
  private double scrollAccumulator;
  private final Runnable unload;

  private int charSlot;
  private int selectedSlot;
  private Renderable58 additionHighlight;
  private final MenuAdditionInfo[] additions = new MenuAdditionInfo[9];

  public AdditionsScreen(final Runnable unload) {
    this.unload = unload;
    Arrays.setAll(this.additions, i -> new MenuAdditionInfo());
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

        if(this.additions[0].offset_00 != -1) {
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

  private void renderAdditions(final int charSlot, final MenuAdditionInfo[] additions, final int selectedAdditionOffset, final long a4) {
    final boolean allocate = a4 == 0xff;
    final int charIndex = characterIndices_800bdbb8[charSlot];

    if(additions[0].offset_00 == -1) {
      renderText(Addition_cannot_be_used_8011c340, 106, 150, TextColour.BROWN);
    } else {
      if(allocate) {
        renderGlyphs(additionGlyphs_801141e4, 0, 0);
      }

      for(int i = 0; i < 8; i++) {
        final int y = this.getAdditionSlotY(i);

        if(allocate && i < additionCounts_8004f5c0[charIndex]) { // Total number of additions
          renderCharacter(24, y, i + 1); // Addition number
        }

        final int offset = additions[i].offset_00;
        final int index = additions[i].index_01;

        if(offset != -1) {
          renderText(additions_8011a064[offset], 33, y - 2, offset != selectedAdditionOffset ? TextColour.BROWN : TextColour.RED);

          if(allocate) {
            final int level = gameState_800babc8.charData_32c[charIndex].additionLevels_1a[index];
            renderThreeDigitNumber(197, y, level); // Addition level
            renderThreeDigitNumber(230, y, additionData_80052884[offset].attacks_01); // Number of attacks
            renderThreeDigitNumber(263, y, additionData_80052884[offset].sp_02[level - 1]); // SP
            renderThreeDigitNumber(297, y, additionData_80052884[offset].damage_0c * (additions_80114070[offset][level].damageMultiplier_03 + 100) / 100); // Damage
            renderThreeDigitNumber(322, y, gameState_800babc8.charData_32c[charIndex].additionXp_22[index]); // Current XP

            if(level < 5) {
              renderThreeDigitNumber(342, y, level * 20); // Max XP
            } else {
              renderCharacter(354, y, 218); // Dash if at max XP
            }
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
      if(this.selectedSlot != i && MathHelper.inBox(x, y, 31, this.getAdditionSlotY(i) - 3, 141, 13)) {
        playMenuSound(1);
        this.selectedSlot = i;
        this.additionHighlight.y_44 = this.getAdditionSlotY(i) - 4;
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

    if(this.loadingStage != 2 || mods != 0) {
      return InputPropagation.PROPAGATE;
    }

    if(button == GLFW_MOUSE_BUTTON_LEFT && this.additions[0].offset_00 != -1) {
      for(int i = 0; i < 7; i++) {
        if(MathHelper.inBox(x, y, 31, this.getAdditionSlotY(i) - 3, 141, 13)) {
          this.selectedSlot = i;
          this.additionHighlight.y_44 = this.getAdditionSlotY(i) - 4;

          final int additionOffset = this.additions[i].offset_00;

          if(additionOffset != -1) {
            gameState_800babc8.charData_32c[characterIndices_800bdbb8[this.charSlot]].selectedAddition_19 = additionOffset;
            playMenuSound(2);
            unloadRenderable(this.additionHighlight);
            this.loadingStage = 1;
          } else {
            playMenuSound(40);
          }

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
    if(this.selectedSlot > 0) {
      this.selectedSlot--;
    }

    playMenuSound(1);
    this.additionHighlight.y_44 = this.getAdditionSlotY(this.selectedSlot) - 4;
  }

  private void menuNavigateDown() {
    if(this.selectedSlot < 6) {
      this.selectedSlot++;
    }

    playMenuSound(1);
    this.additionHighlight.y_44 = this.getAdditionSlotY(this.selectedSlot) - 4;
  }

  private void menuNavigateLeft() {
    this.scrollAccumulator++;
  }

  private void menuNavigateRight() {
    this.scrollAccumulator--;
  }

  private void menuSelect() {
    final int additionOffset = this.additions[this.selectedSlot].offset_00;

    if(additionOffset != -1) {
      gameState_800babc8.charData_32c[characterIndices_800bdbb8[this.charSlot]].selectedAddition_19 = additionOffset;
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
  public InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage != 2) {
      return InputPropagation.PROPAGATE;
    }

    if(inputAction == InputAction.DPAD_LEFT || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_LEFT) {
      this.menuNavigateLeft();
      return InputPropagation.HANDLED;
    } else if(inputAction == InputAction.DPAD_RIGHT || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_RIGHT) {
      this.menuNavigateRight();
      return InputPropagation.HANDLED;
    } else if(inputAction == InputAction.BUTTON_EAST) {
      this.menuEscape();
      return InputPropagation.HANDLED;
    } else if(inputAction == InputAction.BUTTON_SOUTH) {
      this.menuSelect();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  public InputPropagation pressedWithRepeatPulse(final InputAction inputAction) {
    if(super.pressedWithRepeatPulse(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage != 2) {
      return InputPropagation.PROPAGATE;
    }

    if(inputAction == InputAction.DPAD_UP || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_UP) {
      this.menuNavigateUp();
      return InputPropagation.HANDLED;
    } else if(inputAction == InputAction.DPAD_DOWN || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_DOWN) {
      this.menuNavigateDown();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }
}
