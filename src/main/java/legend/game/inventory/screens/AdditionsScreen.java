package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.game.types.MenuAdditionInfo;
import legend.game.types.Renderable58;

import java.util.Arrays;

import static legend.game.SItem.Addition_cannot_be_used_8011c340;
import static legend.game.SItem.FUN_801034cc;
import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.additionGlyphs_801141e4;
import static legend.game.SItem.additionXpPerLevel_800fba2c;
import static legend.game.SItem.additions_8011a064;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.characterCount_8011d7c4;
import static legend.game.SItem.loadAdditions;
import static legend.game.SItem.ptrTable_80114070;
import static legend.game.SItem.renderCharacter;
import static legend.game.SItem.renderCharacterSlot;
import static legend.game.SItem.renderGlyphs;
import static legend.game.SItem.renderText;
import static legend.game.SItem.renderThreeDigitNumber;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_8002.unloadRenderable;
import static legend.game.Scus94491BpeSegment_8002.uploadRenderables;
import static legend.game.Scus94491BpeSegment_8004.additionCounts_8004f5c0;
import static legend.game.Scus94491BpeSegment_8005.additionData_80052884;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class AdditionsScreen extends MenuScreen {
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
        scriptStartEffect(2, 10);
        deallocateRenderables(0xff);
        this.loadingStage++;
      }

      case 1 -> {
        deallocateRenderables(0);
        loadAdditions(characterIndices_800bdbb8.get(this.charSlot).get(), this.additions);

        if(this.additions[0].offset_00 != -1) {
          this.additionHighlight = allocateUiElement(117, 117, 39, this.getAdditionSlotY(this.selectedSlot) - 4);
          FUN_80104b60(this.additionHighlight);
        }

        allocateUiElement(69, 69, 0, 0);
        allocateUiElement(70, 70, 192, 0);
        this.renderAdditions(this.charSlot, this.additions, gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.get(this.charSlot).get()).selectedAddition_19.get(), 0xffL);
        this.loadingStage++;
      }

      case 2 -> {
        FUN_801034cc(this.charSlot, characterCount_8011d7c4.get());
        this.renderAdditions(this.charSlot, this.additions, gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.get(this.charSlot).get()).selectedAddition_19.get(), 0);

        if(this.scrollAccumulator >= 1.0d) {
          this.scrollAccumulator -= 1.0d;

          if(this.charSlot > 0) {
            this.scroll(this.charSlot - 1);
          }
        }

        if(this.scrollAccumulator <= -1.0d) {
          this.scrollAccumulator += 1.0d;

          if(this.charSlot < characterCount_8011d7c4.get() - 1) {
            this.scroll(this.charSlot + 1);
          }
        }
      }

      // Fade out
      case 100 -> {
        this.renderAdditions(this.charSlot, this.additions, gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.get(this.charSlot).get()).selectedAddition_19.get(), 0);
        this.unload.run();
      }
    }
  }

  private void renderAdditions(final int charSlot, final MenuAdditionInfo[] additions, final int selectedAdditionOffset, final long a4) {
    final boolean allocate = a4 == 0xff;
    final int charIndex = characterIndices_800bdbb8.get(charSlot).get();

    if(additions[0].offset_00 == -1) {
      renderText(Addition_cannot_be_used_8011c340, 106, 150, 4);
    } else {
      if(allocate) {
        renderGlyphs(additionGlyphs_801141e4, 0, 0);
      }

      for(int i = 0; i < 8; i++) {
        final int y = this.getAdditionSlotY(i);

        if(allocate && i < additionCounts_8004f5c0.get(charIndex).get()) { // Total number of additions
          renderCharacter(24, y, i + 1); // Addition number
        }

        final int offset = additions[i].offset_00;
        final int index = additions[i].index_01;

        if(offset != -1) {
          renderText(additions_8011a064.get(offset).deref(), 33, y - 2, offset != selectedAdditionOffset ? 4 : 5);

          if(allocate) {
            final int level = gameState_800babc8.charData_32c.get(charIndex).additionLevels_1a.get(index).get();
            renderThreeDigitNumber(197, y, level); // Addition level
            renderThreeDigitNumber(230, y, additionData_80052884.get(offset).attacks_01.get()); // Number of attacks
            renderThreeDigitNumber(263, y, additionData_80052884.get(offset).sp_02.get(level - 1).get()); // SP
            renderThreeDigitNumber(297, y, (int)(additionData_80052884.get(offset).damage_0c.get() * (ptrTable_80114070.offset(offset * 0x4L).deref(1).offset(level * 0x4L).offset(0x3L).get() + 100) / 100)); // Damage
            renderThreeDigitNumber(322, y, gameState_800babc8.charData_32c.get(charIndex).additionXp_22.get(index).get()); // Current XP

            if(level < 5) {
              renderThreeDigitNumber(342, y, additionXpPerLevel_800fba2c.get(level).get()); // Max XP
            } else {
              renderCharacter(354, y, 218); // Dash if at max XP
            }
          }
        }
      }
    }

    renderCharacterSlot(16, 21, charIndex, allocate, false);
    uploadRenderables();
  }

  private int getAdditionSlotY(final int slot) {
    return 113 + slot * 14;
  }

  private void scroll(final int scroll) {
    playSound(1);
    this.charSlot = scroll;
    unloadRenderable(this.additionHighlight);
    this.loadingStage = 1;
  }

  @Override
  protected void mouseMove(final int x, final int y) {
    if(this.loadingStage != 2) {
      return;
    }

    for(int i = 0; i < 7; i++) {
      if(this.selectedSlot != i && MathHelper.inBox(x, y, 31, this.getAdditionSlotY(i) - 3, 141, 13)) {
        playSound(1);
        this.selectedSlot = i;
        this.additionHighlight.y_44 = this.getAdditionSlotY(i) - 4;
      }
    }
  }

  @Override
  protected void mouseClick(final int x, final int y, final int button, final int mods) {
    if(this.loadingStage != 2 || mods != 0) {
      return;
    }

    if(button == GLFW_MOUSE_BUTTON_LEFT && this.additions[0].offset_00 != -1) {
      for(int i = 0; i < 7; i++) {
        if(MathHelper.inBox(x, y, 31, this.getAdditionSlotY(i) - 3, 141, 13)) {
          this.selectedSlot = i;
          this.additionHighlight.y_44 = this.getAdditionSlotY(i) - 4;

          final int additionOffset = this.additions[i].offset_00;

          if(additionOffset != -1) {
            gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.get(this.charSlot).get()).selectedAddition_19.set(additionOffset);
            playSound(2);
            unloadRenderable(this.additionHighlight);
            this.loadingStage = 1;
          } else {
            playSound(40);
          }
        }
      }
    }
  }

  @Override
  protected void keyPress(final int key, final int scancode, final int mods) {
    if(this.loadingStage != 2 || mods != 0) {
      return;
    }
    switch(key) {
      case GLFW_KEY_LEFT -> this.scrollAccumulator++;
      case GLFW_KEY_RIGHT -> this.scrollAccumulator--;

      case GLFW_KEY_DOWN -> {
        if(this.selectedSlot < 6) {
          this.selectedSlot++;
        }

        playSound(1);
        this.additionHighlight.y_44 = this.getAdditionSlotY(this.selectedSlot) - 4;
      }

      case GLFW_KEY_UP -> {
        if(this.selectedSlot > 0) {
          this.selectedSlot--;
        }

        playSound(1);
        this.additionHighlight.y_44 = this.getAdditionSlotY(this.selectedSlot) - 4;
      }

      case GLFW_KEY_ENTER, GLFW_KEY_S -> {
        final int additionOffset = this.additions[this.selectedSlot].offset_00;

        if(additionOffset != -1) {
          gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.get(this.charSlot).get()).selectedAddition_19.set(additionOffset);
          playSound(2);
          unloadRenderable(this.additionHighlight);
          this.loadingStage = 1;
        } else {
          playSound(40);
        }
      }

      case GLFW_KEY_ESCAPE -> {
        playSound(3);
        this.loadingStage = 100;
      }
    }
  }

  @Override
  protected void mouseScroll(final double deltaX, final double deltaY) {
    if(this.loadingStage != 2) {
      return;
    }

    if(this.scrollAccumulator < 0 && deltaY > 0 || this.scrollAccumulator > 0 && deltaY < 0) {
      this.scrollAccumulator = 0;
    }

    this.scrollAccumulator += deltaY;
  }
}
