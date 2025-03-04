package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.core.memory.Method;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputMod;
import legend.game.modding.coremod.CoreMod;
import legend.game.types.ActiveStatsa0;
import legend.game.types.Renderable58;

import java.util.Set;

import static legend.core.GameEngine.CONFIG;
import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.charSwapGlyphs_80114160;
import static legend.game.SItem.glyph_801142d4;
import static legend.game.SItem.initGlyph;
import static legend.game.SItem.renderCharacterSlot;
import static legend.game.SItem.renderFourDigitHp;
import static legend.game.SItem.renderFourDigitNumber;
import static legend.game.SItem.renderGlyphs;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.allocateRenderable;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_8002.unloadRenderable;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.secondaryCharIds_800bdbf8;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800b.uiFile_800bdc3c;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_LEFT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_RIGHT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_UP;

public class CharSwapScreen extends MenuScreen {
  private int loadingStage;
  private final Runnable unload;

  /** Allows list wrapping, but only on new input */
  private boolean allowWrapX = true;
  private boolean allowWrapY = true;

  private int primaryCharIndex;
  private int secondaryCharIndex;
  private Renderable58 primaryCharHighlight;
  private Renderable58 secondaryCharHighlight;

  public CharSwapScreen(final Runnable unload) {
    this.unload = unload;
  }

  @Override
  protected void render() {
    switch(this.loadingStage) {
      case 0 -> {
        startFadeEffect(2, 10);

        this.primaryCharIndex = 0;
        this.secondaryCharIndex = 0;

        /* When unlock party is disabled, rearrange party to fit retail expectations. */
        if(!CONFIG.getConfig(CoreMod.UNLOCK_PARTY_CONFIG.get())) {
          boolean sortPrimary = false;
          boolean requiredInSecondary = false;
          int primarySlotIndex;
          int secondarySlotIndex;

          /* Check current party for empty slots or swappable characters above locked characters. */
          for(primarySlotIndex = 0; primarySlotIndex < 2 && !sortPrimary; primarySlotIndex++) {
            final int charA = gameState_800babc8.charIds_88[primarySlotIndex];
            final int charB = gameState_800babc8.charIds_88[primarySlotIndex + 1];
            sortPrimary = charA == -1 || charB == -1 || (gameState_800babc8.charData_32c[charA].partyFlags_04 & 0x20) == 0 && (gameState_800babc8.charData_32c[charB].partyFlags_04 & 0x20) != 0;
          }
          /* Check for locked characters not in the active party. */
          for(secondarySlotIndex = 0; secondarySlotIndex < 6 && !sortPrimary && !requiredInSecondary; secondarySlotIndex++) {
            requiredInSecondary = secondaryCharIds_800bdbf8[secondarySlotIndex] != -1 && (gameState_800babc8.charData_32c[secondaryCharIds_800bdbf8[secondarySlotIndex]].partyFlags_04 & 0x20) != 0;
          }

          /* Check for swappable character in first slot. */
          if(sortPrimary || requiredInSecondary || (gameState_800babc8.charData_32c[gameState_800babc8.charIds_88[0]].partyFlags_04 & 0x20) == 0) {
            final int[] slots = {-1, -1, -1};
            int charIndex;

            /* Building new party. Priority by loop.
                1. Locked/required characters.
                2. Available characters in current party.
                3. Available characters not in current party. */
            for(charIndex = 0, primarySlotIndex = 0; charIndex < 9 && primarySlotIndex < 3; charIndex++) {
              if((gameState_800babc8.charData_32c[charIndex].partyFlags_04 & 0x20) != 0) {
                slots[primarySlotIndex++] = charIndex;
              }
            }
            for(int i = 0; i < 3 && primarySlotIndex < 3; i++) {
              charIndex = gameState_800babc8.charIds_88[i];
              if(charIndex != -1 && (gameState_800babc8.charData_32c[charIndex].partyFlags_04 & 0x2) != 0 && charIndex != slots[0] && charIndex != slots[1] && charIndex != slots[2]) {
                slots[primarySlotIndex++] = charIndex;
              }
            }
            for(charIndex = 0; charIndex < 9 && primarySlotIndex < 3; charIndex++) {
              if((gameState_800babc8.charData_32c[charIndex].partyFlags_04 & 0x2) != 0 && charIndex != slots[0] && charIndex != slots[1] && charIndex != slots[2]) {
                slots[primarySlotIndex++] = charIndex;
              }
            }

            /* Rebuilding secondary characters to avoid duplicates. */
            for(charIndex = 0, secondarySlotIndex = 0; charIndex < 9 && secondarySlotIndex < 6; charIndex++) {
              if((gameState_800babc8.charData_32c[charIndex].partyFlags_04 & 0x1) != 0 && charIndex != slots[0] && charIndex != slots[1] && charIndex != slots[2]) {
                secondaryCharIds_800bdbf8[secondarySlotIndex++] = charIndex;
              }
            }
            while(secondarySlotIndex < 6) {
              secondaryCharIds_800bdbf8[secondarySlotIndex++] = -1;
            }

            gameState_800babc8.charIds_88[0] = slots[0];
            gameState_800babc8.charIds_88[1] = slots[1];
            gameState_800babc8.charIds_88[2] = slots[2];
          }
        }

        for(int i = 0; i < 3; i++) {
          if(CONFIG.getConfig(CoreMod.UNLOCK_PARTY_CONFIG.get()) || gameState_800babc8.charIds_88[i] == -1 || (gameState_800babc8.charData_32c[gameState_800babc8.charIds_88[i]].partyFlags_04 & 0x20) == 0) {
            this.primaryCharIndex = i;
            break;
          }
        }

        this.loadingStage++;
      }

      case 1 -> {
        deallocateRenderables(0xff);
        renderGlyphs(charSwapGlyphs_80114160, 0, 0);
        this.primaryCharHighlight = allocateUiElement(0x7f, 0x7f, 16, this.getSlotY(this.primaryCharIndex));
        FUN_80104b60(this.primaryCharHighlight);
        this.renderCharacterSwapScreen(0xff);
        this.loadingStage++;
      }

      case 2, 3 -> this.renderCharacterSwapScreen(0);

      // Fade out
      case 100 -> {
        this.renderCharacterSwapScreen(0);
        this.unload.run();
      }
    }
  }

  private void renderCharacterSwapScreen(final int a0) {
    final boolean allocate = a0 == 0xff;

    this.renderSecondaryChar(198, 16, secondaryCharIds_800bdbf8[0], allocate);
    this.renderSecondaryChar(255, 16, secondaryCharIds_800bdbf8[1], allocate);
    this.renderSecondaryChar(312, 16, secondaryCharIds_800bdbf8[2], allocate);
    this.renderSecondaryChar(198, 122, secondaryCharIds_800bdbf8[3], allocate);
    this.renderSecondaryChar(255, 122, secondaryCharIds_800bdbf8[4], allocate);
    this.renderSecondaryChar(312, 122, secondaryCharIds_800bdbf8[5], allocate);

    if(gameState_800babc8.charIds_88[0] != -1) {
      renderCharacterSlot(16, 16, gameState_800babc8.charIds_88[0], allocate, !CONFIG.getConfig(CoreMod.UNLOCK_PARTY_CONFIG.get()) && (gameState_800babc8.charData_32c[gameState_800babc8.charIds_88[0]].partyFlags_04 & 0x20) != 0);
    }

    if(gameState_800babc8.charIds_88[1] != -1) {
      renderCharacterSlot(16, 88, gameState_800babc8.charIds_88[1], allocate, !CONFIG.getConfig(CoreMod.UNLOCK_PARTY_CONFIG.get()) && (gameState_800babc8.charData_32c[gameState_800babc8.charIds_88[1]].partyFlags_04 & 0x20) != 0);
    }

    if(gameState_800babc8.charIds_88[2] != -1) {
      renderCharacterSlot(16, 160, gameState_800babc8.charIds_88[2], allocate, !CONFIG.getConfig(CoreMod.UNLOCK_PARTY_CONFIG.get()) && (gameState_800babc8.charData_32c[gameState_800babc8.charIds_88[2]].partyFlags_04 & 0x20) != 0);
    }
  }

  private void renderSecondaryChar(final int x, final int y, final int charIndex, final boolean allocate) {
    if(allocate && charIndex != -1) {
      if(charIndex < 9) {
        final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.portraits_cfac(), null);
        initGlyph(renderable, glyph_801142d4);
        renderable.glyph_04 = charIndex;
        renderable.tpage_2c++;
        renderable.z_3c = 33;
        renderable.x_40 = x + 2;
        renderable.y_44 = y + 8;
      }

      allocateUiElement(0x50, 0x50, x, y).z_3c = 33;
      allocateUiElement(0x9c, 0x9c, x, y);

      if(!CONFIG.getConfig(CoreMod.UNLOCK_PARTY_CONFIG.get()) && (gameState_800babc8.charData_32c[charIndex].partyFlags_04 & 0x2) == 0) {
        allocateUiElement(0x72, 0x72, x, y + 24).z_3c = 33;
      }

      final ActiveStatsa0 stats = stats_800be5f8[charIndex];
      renderFourDigitNumber(x + 25, y + 57, stats.level_0e);
      renderFourDigitNumber(x + 25, y + 68, stats.dlevel_0f);
      renderFourDigitHp(x + 25, y + 79, stats.hp_04, stats.maxHp_66);
      renderFourDigitNumber(x + 25, y + 90, stats.mp_06);
    }
  }

  private int getSecondaryCharX(int slot) {
    if(slot >= 3) {
      slot -= 3;
    }

    return 198 + slot * 57;
  }

  private int getSecondaryCharY(final int slot) {
    return slot >= 3 ? 122 : 16;
  }

  @Override
  protected InputPropagation mouseMove(final int x, final int y) {
    if(super.mouseMove(x, y) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage == 2) {
      for(int i = 0; i < 3; i++) {
        if((CONFIG.getConfig(CoreMod.UNLOCK_PARTY_CONFIG.get()) || gameState_800babc8.charIds_88[i] != -1 && (gameState_800babc8.charData_32c[gameState_800babc8.charIds_88[i]].partyFlags_04 & 0x20) == 0) && this.primaryCharIndex != i && MathHelper.inBox(x, y, 8, this.getSlotY(i), 174, 65)) {
          playMenuSound(1);
          this.primaryCharIndex = i;
          this.primaryCharHighlight.y_44 = this.getSlotY(i);
          return InputPropagation.HANDLED;
        }
      }
    } else if(this.loadingStage == 3) {
      for(int i = 0; i < 6; i++) {
        if(this.secondaryCharIndex != i && MathHelper.inBox(x, y, this.getSecondaryCharX(i) - 8, this.getSecondaryCharY(i), 57, 102)) {
          playMenuSound(1);
          this.secondaryCharIndex = i;
          this.secondaryCharHighlight.x_40 = this.getSecondaryCharX(this.secondaryCharIndex);
          this.secondaryCharHighlight.y_44 = this.getSecondaryCharY(this.secondaryCharIndex);
          return InputPropagation.HANDLED;
        }
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation mouseClick(final int x, final int y, final int button, final Set<InputMod> mods) {
    if(super.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage == 2) {
      for(int i = 0; i < 3; i++) {
        if(MathHelper.inBox(x, y, 8, this.getSlotY(i), 174, 65)) {
          if(CONFIG.getConfig(CoreMod.UNLOCK_PARTY_CONFIG.get()) || gameState_800babc8.charIds_88[i] != -1 && (gameState_800babc8.charData_32c[gameState_800babc8.charIds_88[i]].partyFlags_04 & 0x20) == 0) {
            playMenuSound(2);
            this.primaryCharIndex = i;
            this.primaryCharHighlight.y_44 = this.getSlotY(i);
            this.secondaryCharHighlight = allocateUiElement(0x80, 0x80, this.getSecondaryCharX(this.secondaryCharIndex), this.getSecondaryCharY(this.secondaryCharIndex));
            FUN_80104b60(this.secondaryCharHighlight);
            this.loadingStage = 3;
          } else {
            playMenuSound(40);
          }

          return InputPropagation.HANDLED;
        }
      }
    } else if(this.loadingStage == 3) {
      for(int i = 0; i < 6; i++) {
        if(MathHelper.inBox(x, y, this.getSecondaryCharX(i) - 8, this.getSecondaryCharY(i), 57, 102)) {
          playMenuSound(1);
          this.secondaryCharIndex = i;
          this.secondaryCharHighlight.x_40 = this.getSecondaryCharX(this.secondaryCharIndex);
          this.secondaryCharHighlight.y_44 = this.getSecondaryCharY(this.secondaryCharIndex);

          int charCount = 0;
          for(int charSlot = 0; charSlot < 3; charSlot++) {
            if(gameState_800babc8.charIds_88[charSlot] != -1) {
              charCount++;
            }
          }

          final int secondaryCharIndex = secondaryCharIds_800bdbf8[this.secondaryCharIndex];

          if((CONFIG.getConfig(CoreMod.UNLOCK_PARTY_CONFIG.get()) && charCount >= 2) || secondaryCharIndex != -1 && (gameState_800babc8.charData_32c[secondaryCharIndex].partyFlags_04 & 0x2) != 0) {
            playMenuSound(2);
            final int charIndex = gameState_800babc8.charIds_88[this.primaryCharIndex];
            gameState_800babc8.charIds_88[this.primaryCharIndex] = secondaryCharIndex;
            secondaryCharIds_800bdbf8[this.secondaryCharIndex] = charIndex;
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

  private void menuStage2Escape() {
    playMenuSound(3);
    this.loadingStage = 100;
  }

  private void menuStage2NavigateUp() {
    if(this.primaryCharIndex > 0 && (CONFIG.getConfig(CoreMod.UNLOCK_PARTY_CONFIG.get()) || gameState_800babc8.charIds_88[this.primaryCharIndex - 1] != -1 && (gameState_800babc8.charData_32c[gameState_800babc8.charIds_88[this.primaryCharIndex - 1]].partyFlags_04 & 0x20) == 0)) {
      playMenuSound(1);
      this.primaryCharIndex--;
    } else if(this.allowWrapY) {
      for(int i = 2; i > this.primaryCharIndex; i--) {
        if(CONFIG.getConfig(CoreMod.UNLOCK_PARTY_CONFIG.get()) || gameState_800babc8.charIds_88[i] != -1 && (gameState_800babc8.charData_32c[gameState_800babc8.charIds_88[i]].partyFlags_04 & 0x20) == 0) {
          playMenuSound(1);
          this.primaryCharIndex = i;
          break;
        }
      }
    }

    this.primaryCharHighlight.y_44 = this.getSlotY(this.primaryCharIndex);
  }

  private void menuStage2NavigateDown() {
    if(this.primaryCharIndex < 2 && (CONFIG.getConfig(CoreMod.UNLOCK_PARTY_CONFIG.get()) || gameState_800babc8.charIds_88[this.primaryCharIndex + 1] != -1 && (gameState_800babc8.charData_32c[gameState_800babc8.charIds_88[this.primaryCharIndex + 1]].partyFlags_04 & 0x20) == 0)) {
      playMenuSound(1);
      this.primaryCharIndex++;
    } else if(this.allowWrapY) {
      for(int i = 0; i < this.primaryCharIndex; i++) {
        if(CONFIG.getConfig(CoreMod.UNLOCK_PARTY_CONFIG.get()) || gameState_800babc8.charIds_88[i] != -1 && (gameState_800babc8.charData_32c[gameState_800babc8.charIds_88[i]].partyFlags_04 & 0x20) == 0) {
          playMenuSound(1);
          this.primaryCharIndex = i;
          break;
        }
      }
    }

    this.primaryCharHighlight.y_44 = this.getSlotY(this.primaryCharIndex);
  }

  private void menuStage2Select() {
    final int charIndex = gameState_800babc8.charIds_88[this.primaryCharIndex];
    if(CONFIG.getConfig(CoreMod.UNLOCK_PARTY_CONFIG.get()) || charIndex != -1 && (gameState_800babc8.charData_32c[charIndex].partyFlags_04 & 0x20) == 0) {
      playMenuSound(2);
      this.secondaryCharHighlight = allocateUiElement(0x80, 0x80, this.getSecondaryCharX(this.secondaryCharIndex), this.getSecondaryCharY(this.secondaryCharIndex));
      FUN_80104b60(this.secondaryCharHighlight);
      this.loadingStage = 3;
    } else {
      playMenuSound(40);
    }
  }

  private void menuStage3Escape() {
    playMenuSound(3);
    unloadRenderable(this.secondaryCharHighlight);
    this.loadingStage = 2;
  }

  private void menuStage3NavigateUp() {
    if(this.secondaryCharIndex > 2) {
      playMenuSound(1);
      this.secondaryCharIndex -= 3;
    } else if(this.allowWrapY) {
      playMenuSound(1);
      this.secondaryCharIndex += 3;
    }

    this.secondaryCharHighlight.x_40 = this.getSecondaryCharX(this.secondaryCharIndex);
    this.secondaryCharHighlight.y_44 = this.getSecondaryCharY(this.secondaryCharIndex);
  }

  private void menuStage3NavigateDown() {
    if(this.secondaryCharIndex < 3) {
      playMenuSound(1);
      this.secondaryCharIndex += 3;
    } else if(this.allowWrapY) {
      playMenuSound(1);
      this.secondaryCharIndex -= 3;
    }

    this.secondaryCharHighlight.x_40 = this.getSecondaryCharX(this.secondaryCharIndex);
    this.secondaryCharHighlight.y_44 = this.getSecondaryCharY(this.secondaryCharIndex);
  }

  private void menuStage3NavigateLeft() {
    if(this.secondaryCharIndex > 0) {
      playMenuSound(1);
      this.secondaryCharIndex--;
    } else if(this.allowWrapX) {
      playMenuSound(1);
      this.secondaryCharIndex = 5;
    }

    this.secondaryCharHighlight.x_40 = this.getSecondaryCharX(this.secondaryCharIndex);
    this.secondaryCharHighlight.y_44 = this.getSecondaryCharY(this.secondaryCharIndex);
  }

  private void menuStage3NavigateRight() {
    if(this.secondaryCharIndex < 5) {
      playMenuSound(1);
      this.secondaryCharIndex++;
    } else if(this.allowWrapX) {
      playMenuSound(1);
      this.secondaryCharIndex = 0;
    }

    this.secondaryCharHighlight.x_40 = this.getSecondaryCharX(this.secondaryCharIndex);
    this.secondaryCharHighlight.y_44 = this.getSecondaryCharY(this.secondaryCharIndex);
  }

  private void menuStage3Select() {
    this.secondaryCharHighlight.x_40 = this.getSecondaryCharX(this.secondaryCharIndex);
    this.secondaryCharHighlight.y_44 = this.getSecondaryCharY(this.secondaryCharIndex);

    int charCount = 0;
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      if(gameState_800babc8.charIds_88[charSlot] != -1) {
        charCount++;
      }
    }

    final int secondaryCharIndex = secondaryCharIds_800bdbf8[this.secondaryCharIndex];

    if((CONFIG.getConfig(CoreMod.UNLOCK_PARTY_CONFIG.get()) && charCount >= 2) || secondaryCharIndex != -1 && (gameState_800babc8.charData_32c[secondaryCharIndex].partyFlags_04 & 0x2) != 0) {
      playMenuSound(2);
      final int charIndex = gameState_800babc8.charIds_88[this.primaryCharIndex];
      gameState_800babc8.charIds_88[this.primaryCharIndex] = secondaryCharIndex;
      secondaryCharIds_800bdbf8[this.secondaryCharIndex] = charIndex;
      this.loadingStage = 1;
    } else {
      playMenuSound(40);
    }
  }

  @Override
  public InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(super.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage == 2) {
      // primary character left side
      if(action == INPUT_ACTION_MENU_UP.get()) {
        this.menuStage2NavigateUp();
        this.allowWrapY = false;
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_DOWN.get()) {
        this.menuStage2NavigateDown();
        this.allowWrapY = false;
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_BACK.get() && !repeat) {
        this.menuStage2Escape();
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_CONFIRM.get() && !repeat) {
        this.menuStage2Select();
        return InputPropagation.HANDLED;
      }
    } else if(this.loadingStage == 3) {
      if(action == INPUT_ACTION_MENU_UP.get()) {
        this.menuStage3NavigateUp();
        this.allowWrapY = false;
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_DOWN.get()) {
        this.menuStage3NavigateDown();
        this.allowWrapY = false;
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_LEFT.get()) {
        this.menuStage3NavigateLeft();
        this.allowWrapX = false;
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_RIGHT.get()) {
        this.menuStage3NavigateRight();
        this.allowWrapX = false;
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_BACK.get() && !repeat) {
        this.menuStage3Escape();
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_CONFIRM.get() && !repeat) {
        this.menuStage3Select();
        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  public InputPropagation inputActionReleased(final InputAction action) {
    if(super.inputActionReleased(action) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
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

  @Method(0x800fc84cL)
  private int getSlotY(final int slot) {
    return 16 + slot * 72;
  }
}
