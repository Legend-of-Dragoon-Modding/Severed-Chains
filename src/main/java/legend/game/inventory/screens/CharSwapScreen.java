package legend.game.inventory.screens;

import legend.core.Config;
import legend.core.MathHelper;
import legend.core.memory.Method;
import legend.game.input.InputAction;
import legend.game.types.ActiveStatsa0;
import legend.game.types.Renderable58;

import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.charSwapGlyphs_80114160;
import static legend.game.SItem.glyph_801142d4;
import static legend.game.SItem.initGlyph;
import static legend.game.SItem.renderCharacterSlot;
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

public class CharSwapScreen extends MenuScreen {
  private int loadingStage;
  private final Runnable unload;

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
      renderCharacterSlot(16, 16, gameState_800babc8.charIds_88[0], allocate, !Config.unlockParty() && (gameState_800babc8.charData_32c[gameState_800babc8.charIds_88[0]].partyFlags_04 & 0x20) != 0);
    }

    if(gameState_800babc8.charIds_88[1] != -1) {
      renderCharacterSlot(16, 88, gameState_800babc8.charIds_88[1], allocate, !Config.unlockParty() && (gameState_800babc8.charData_32c[gameState_800babc8.charIds_88[1]].partyFlags_04 & 0x20) != 0);
    }

    if(gameState_800babc8.charIds_88[2] != -1) {
      renderCharacterSlot(16, 160, gameState_800babc8.charIds_88[2], allocate, !Config.unlockParty() && (gameState_800babc8.charData_32c[gameState_800babc8.charIds_88[2]].partyFlags_04 & 0x20) != 0);
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

      if(!Config.unlockParty() && (gameState_800babc8.charData_32c[charIndex].partyFlags_04 & 0x2) == 0) {
        allocateUiElement(0x72, 0x72, x, y + 24).z_3c = 33;
      }

      final ActiveStatsa0 stats = stats_800be5f8[charIndex];
      renderFourDigitNumber(x + 25, y + 57, stats.level_0e);
      renderFourDigitNumber(x + 25, y + 68, stats.dlevel_0f);
      renderFourDigitNumber(x + 25, y + 79, stats.hp_04, stats.maxHp_66);
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
        if(this.primaryCharIndex != i && MathHelper.inBox(x, y, 8, this.getSlotY(i), 174, 65)) {
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
  protected InputPropagation mouseClick(final int x, final int y, final int button, final int mods) {
    if(super.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage == 2) {
      for(int i = 0; i < 3; i++) {
        if(MathHelper.inBox(x, y, 8, this.getSlotY(i), 174, 65)) {
          playMenuSound(2);
          this.primaryCharIndex = i;
          this.primaryCharHighlight.y_44 = this.getSlotY(i);

          final int charIndex = gameState_800babc8.charIds_88[this.primaryCharIndex];
          if(Config.unlockParty() || charIndex == -1 || (gameState_800babc8.charData_32c[charIndex].partyFlags_04 & 0x20) == 0) {
            playMenuSound(2);
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

          if(((Config.unlockParty() && charCount >= 2) || secondaryCharIndex != -1) && (secondaryCharIndex == -1 || Config.unlockParty() || (gameState_800babc8.charData_32c[secondaryCharIndex].partyFlags_04 & 0x2) != 0)) {
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
    playMenuSound(1);
    if(this.primaryCharIndex > 0) {
      this.primaryCharIndex--;
    }

    this.primaryCharHighlight.y_44 = this.getSlotY(this.primaryCharIndex);
  }

  private void menuStage2NavigateDown() {
    playMenuSound(1);
    if(this.primaryCharIndex < 2) {
      this.primaryCharIndex++;
    }

    this.primaryCharHighlight.y_44 = this.getSlotY(this.primaryCharIndex);
  }

  private void menuStage2Select() {
    final int charIndex = gameState_800babc8.charIds_88[this.primaryCharIndex];
    if(Config.unlockParty() || charIndex == -1 || (gameState_800babc8.charData_32c[charIndex].partyFlags_04 & 0x20) == 0) {
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
    playMenuSound(1);

    if(this.secondaryCharIndex > 2) {
      this.secondaryCharIndex -= 3;
    }

    this.secondaryCharHighlight.x_40 = this.getSecondaryCharX(this.secondaryCharIndex);
    this.secondaryCharHighlight.y_44 = this.getSecondaryCharY(this.secondaryCharIndex);
  }

  private void menuStage3NavigateDown() {
    playMenuSound(1);

    if(this.secondaryCharIndex < 3) {
      this.secondaryCharIndex += 3;
    }

    this.secondaryCharHighlight.x_40 = this.getSecondaryCharX(this.secondaryCharIndex);
    this.secondaryCharHighlight.y_44 = this.getSecondaryCharY(this.secondaryCharIndex);
  }

  private void menuStage3NavigateLeft() {
    playMenuSound(1);

    if(this.secondaryCharIndex > 0) {
      this.secondaryCharIndex--;
    }

    this.secondaryCharHighlight.x_40 = this.getSecondaryCharX(this.secondaryCharIndex);
    this.secondaryCharHighlight.y_44 = this.getSecondaryCharY(this.secondaryCharIndex);
  }

  private void menuStage3NavigateRight() {
    playMenuSound(1);

    if(this.secondaryCharIndex < 5) {
      this.secondaryCharIndex++;
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

    if(((Config.unlockParty() && charCount >= 2) || secondaryCharIndex != -1) && (secondaryCharIndex == -1 || Config.unlockParty() || (gameState_800babc8.charData_32c[secondaryCharIndex].partyFlags_04 & 0x2) != 0)) {
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
  public InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage == 2) {
      // primary character left side
      if(inputAction == InputAction.DPAD_UP || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_UP) {
        this.menuStage2NavigateUp();
        return InputPropagation.HANDLED;
      } else if(inputAction == InputAction.DPAD_DOWN || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_DOWN) {
        this.menuStage2NavigateDown();
        return InputPropagation.HANDLED;
      } else if(inputAction == InputAction.BUTTON_EAST) {
        this.menuStage2Escape();
        return InputPropagation.HANDLED;
      } else if(inputAction == InputAction.BUTTON_SOUTH) {
        this.menuStage2Select();
        return InputPropagation.HANDLED;
      }
    } else if(this.loadingStage == 3) {
      if(inputAction == InputAction.DPAD_UP || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_UP) {
        this.menuStage3NavigateUp();
        return InputPropagation.HANDLED;
      } else if(inputAction == InputAction.DPAD_DOWN || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_DOWN) {
        this.menuStage3NavigateDown();
        return InputPropagation.HANDLED;
      } else if(inputAction == InputAction.DPAD_LEFT || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_LEFT) {
        this.menuStage3NavigateLeft();
        return InputPropagation.HANDLED;
      } else if(inputAction == InputAction.DPAD_RIGHT || inputAction == InputAction.JOYSTICK_LEFT_BUTTON_RIGHT) {
        this.menuStage3NavigateRight();
        return InputPropagation.HANDLED;
      } else if(inputAction == InputAction.BUTTON_EAST) {
        this.menuStage3Escape();
        return InputPropagation.HANDLED;
      } else if(inputAction == InputAction.BUTTON_SOUTH) {
        this.menuStage3Select();
        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Method(0x800fc84cL)
  private int getSlotY(final int slot) {
    return 16 + slot * 72;
  }
}
