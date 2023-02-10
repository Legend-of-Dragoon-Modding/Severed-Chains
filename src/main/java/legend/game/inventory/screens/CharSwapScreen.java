package legend.game.inventory.screens;

import legend.core.Config;
import legend.core.MathHelper;
import legend.game.types.ActiveStatsa0;
import legend.game.types.Renderable58;

import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.charSwapGlyphs_80114160;
import static legend.game.SItem.getSlotY;
import static legend.game.SItem.glyph_801142d4;
import static legend.game.SItem.initGlyph;
import static legend.game.SItem.renderCharacterSlot;
import static legend.game.SItem.renderFourDigitNumber;
import static legend.game.SItem.renderGlyphs;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment_8002.allocateRenderable;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_8002.unloadRenderable;
import static legend.game.Scus94491BpeSegment_8002.uploadRenderables;
import static legend.game.Scus94491BpeSegment_800b.drgn0_6666FilePtr_800bdc3c;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.secondaryCharIndices_800bdbf8;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;

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
        scriptStartEffect(2, 10);
        this.primaryCharIndex = 0;
        this.secondaryCharIndex = 0;
        this.loadingStage++;
      }

      case 1 -> {
        deallocateRenderables(0xff);
        renderGlyphs(charSwapGlyphs_80114160, 0, 0);
        this.primaryCharHighlight = allocateUiElement(0x7f, 0x7f, 16, getSlotY(this.primaryCharIndex));
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

    this.renderSecondaryChar(198, 16, secondaryCharIndices_800bdbf8.get(0).get(), allocate);
    this.renderSecondaryChar(255, 16, secondaryCharIndices_800bdbf8.get(1).get(), allocate);
    this.renderSecondaryChar(312, 16, secondaryCharIndices_800bdbf8.get(2).get(), allocate);
    this.renderSecondaryChar(198, 122, secondaryCharIndices_800bdbf8.get(3).get(), allocate);
    this.renderSecondaryChar(255, 122, secondaryCharIndices_800bdbf8.get(4).get(), allocate);
    this.renderSecondaryChar(312, 122, secondaryCharIndices_800bdbf8.get(5).get(), allocate);

    if(gameState_800babc8.charIndex_88.get(0).get() != -1) {
      renderCharacterSlot(16, 16, gameState_800babc8.charIndex_88.get(0).get(), allocate, !Config.unlockParty() && (gameState_800babc8.charData_32c.get(gameState_800babc8.charIndex_88.get(0).get()).partyFlags_04.get() & 0x20) != 0);
    }

    if(gameState_800babc8.charIndex_88.get(1).get() != -1) {
      renderCharacterSlot(16, 88, gameState_800babc8.charIndex_88.get(1).get(), allocate, !Config.unlockParty() && (gameState_800babc8.charData_32c.get(gameState_800babc8.charIndex_88.get(1).get()).partyFlags_04.get() & 0x20) != 0);
    }

    if(gameState_800babc8.charIndex_88.get(2).get() != -1) {
      renderCharacterSlot(16, 160, gameState_800babc8.charIndex_88.get(2).get(), allocate, !Config.unlockParty() && (gameState_800babc8.charData_32c.get(gameState_800babc8.charIndex_88.get(2).get()).partyFlags_04.get() & 0x20) != 0);
    }

    uploadRenderables();
  }

  private void renderSecondaryChar(final int x, final int y, final int charIndex, final boolean allocate) {
    if(allocate && charIndex != -1) {
      if(charIndex < 9) {
        final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._cfac, null);
        initGlyph(renderable, glyph_801142d4);
        renderable.glyph_04 = charIndex;
        renderable.tpage_2c++;
        renderable.z_3c = 33;
        renderable.x_40 = x + 2;
        renderable.y_44 = y + 8;
      }

      allocateUiElement(0x50, 0x50, x, y).z_3c = 33;
      allocateUiElement(0x9c, 0x9c, x, y);

      if((gameState_800babc8.charData_32c.get(charIndex).partyFlags_04.get() & 0x2) == 0) {
        allocateUiElement(0x72, 0x72, x, y + 24).z_3c = 33;
      }

      final ActiveStatsa0 stats = stats_800be5f8.get(charIndex);
      renderFourDigitNumber(x + 25, y + 57, stats.level_0e.get());
      renderFourDigitNumber(x + 25, y + 68, stats.dlevel_0f.get());
      renderFourDigitNumber(x + 25, y + 79, stats.hp_04.get(), stats.maxHp_66.get());
      renderFourDigitNumber(x + 25, y + 90, stats.mp_06.get());
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
  protected void mouseMove(final int x, final int y) {
    if(this.loadingStage == 2) {
      for(int i = 0; i < 3; i++) {
        if(this.primaryCharIndex != i && MathHelper.inBox(x, y, 8, getSlotY(i), 174, 65)) {
          playSound(1);
          this.primaryCharIndex = i;
          this.primaryCharHighlight.y_44 = getSlotY(i);
        }
      }
    } else if(this.loadingStage == 3) {
      for(int i = 0; i < 6; i++) {
        if(this.secondaryCharIndex != i && MathHelper.inBox(x, y, this.getSecondaryCharX(i) - 8, this.getSecondaryCharY(i), 57, 102)) {
          playSound(1);
          this.secondaryCharIndex = i;
          this.secondaryCharHighlight.x_40 = this.getSecondaryCharX(this.secondaryCharIndex);
          this.secondaryCharHighlight.y_44 = this.getSecondaryCharY(this.secondaryCharIndex);
        }
      }
    }
  }

  @Override
  protected void mouseClick(final int x, final int y, final int button, final int mods) {
    if(this.loadingStage == 2) {
      for(int i = 0; i < 3; i++) {
        if(MathHelper.inBox(x, y, 8, getSlotY(i), 174, 65)) {
          playSound(2);
          this.primaryCharIndex = i;
          this.primaryCharHighlight.y_44 = getSlotY(i);

          final int charIndex = gameState_800babc8.charIndex_88.get(this.primaryCharIndex).get();
          if(Config.unlockParty() || charIndex == -1 || (gameState_800babc8.charData_32c.get(charIndex).partyFlags_04.get() & 0x20) == 0) {
            playSound(2);
            this.secondaryCharHighlight = allocateUiElement(0x80, 0x80, this.getSecondaryCharX(this.secondaryCharIndex), this.getSecondaryCharY(this.secondaryCharIndex));
            FUN_80104b60(this.secondaryCharHighlight);
            this.loadingStage = 3;
          } else {
            playSound(40);
          }
        }
      }
    } else if(this.loadingStage == 3) {
      for(int i = 0; i < 6; i++) {
        if(MathHelper.inBox(x, y, this.getSecondaryCharX(i) - 8, this.getSecondaryCharY(i), 57, 102)) {
          playSound(1);
          this.secondaryCharIndex = i;
          this.secondaryCharHighlight.x_40 = this.getSecondaryCharX(this.secondaryCharIndex);
          this.secondaryCharHighlight.y_44 = this.getSecondaryCharY(this.secondaryCharIndex);

          int charCount = 0;
          for(int charSlot = 0; charSlot < 3; charSlot++) {
            if(gameState_800babc8.charIndex_88.get(charSlot).get() != -1) {
              charCount++;
            }
          }

          final int secondaryCharIndex = secondaryCharIndices_800bdbf8.get(this.secondaryCharIndex).get();

          if(((Config.unlockParty() && charCount >= 2) || secondaryCharIndex != -1) && (secondaryCharIndex == -1 || (gameState_800babc8.charData_32c.get(secondaryCharIndex).partyFlags_04.get() & 0x2) != 0)) {
            playSound(2);
            final int charIndex = gameState_800babc8.charIndex_88.get(this.primaryCharIndex).get();
            gameState_800babc8.charIndex_88.get(this.primaryCharIndex).set(secondaryCharIndex);
            secondaryCharIndices_800bdbf8.get(this.secondaryCharIndex).set(charIndex);
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
    super.keyPress(key, scancode, mods);

    if(this.loadingStage == 2) {
      // primary character left side
      switch(key) {
        case GLFW_KEY_ESCAPE -> {
          playSound(3);
          this.loadingStage = 100;
        }

        case GLFW_KEY_DOWN -> {
          playSound(1);
          if(this.primaryCharIndex < 2) {
            this.primaryCharIndex++;
          }

          this.primaryCharHighlight.y_44 = getSlotY(this.primaryCharIndex);
        }

        case GLFW_KEY_UP -> {
          playSound(1);
          if(this.primaryCharIndex > 0) {
            this.primaryCharIndex--;
          }

          this.primaryCharHighlight.y_44 = getSlotY(this.primaryCharIndex);
        }

        case GLFW_KEY_S -> {
          final int charIndex = gameState_800babc8.charIndex_88.get(this.primaryCharIndex).get();
          if(Config.unlockParty() || charIndex == -1 || (gameState_800babc8.charData_32c.get(charIndex).partyFlags_04.get() & 0x20) == 0) {
            playSound(2);
            this.secondaryCharHighlight = allocateUiElement(0x80, 0x80, this.getSecondaryCharX(this.secondaryCharIndex), this.getSecondaryCharY(this.secondaryCharIndex));
            FUN_80104b60(this.secondaryCharHighlight);
            this.loadingStage = 3;
          } else {
            playSound(40);
          }
        }
      }
    } else if(this.loadingStage == 3) {
      switch(key) {
        case GLFW_KEY_DOWN -> {
          playSound(1);

          if(this.secondaryCharIndex < 3) {
            this.secondaryCharIndex += 3;
          }

          this.secondaryCharHighlight.x_40 = this.getSecondaryCharX(this.secondaryCharIndex);
          this.secondaryCharHighlight.y_44 = this.getSecondaryCharY(this.secondaryCharIndex);
        }

        case GLFW_KEY_UP -> {
          playSound(1);

          if(this.secondaryCharIndex > 2) {
            this.secondaryCharIndex -= 3;
          }

          this.secondaryCharHighlight.x_40 = this.getSecondaryCharX(this.secondaryCharIndex);
          this.secondaryCharHighlight.y_44 = this.getSecondaryCharY(this.secondaryCharIndex);
        }

        case GLFW_KEY_LEFT -> {
          playSound(1);

          if(this.secondaryCharIndex > 0) {
            this.secondaryCharIndex--;
          }

          this.secondaryCharHighlight.x_40 = this.getSecondaryCharX(this.secondaryCharIndex);
          this.secondaryCharHighlight.y_44 = this.getSecondaryCharY(this.secondaryCharIndex);
        }

        case GLFW_KEY_RIGHT -> {
          playSound(1);

          if(this.secondaryCharIndex < 5) {
            this.secondaryCharIndex++;
          }

          this.secondaryCharHighlight.x_40 = this.getSecondaryCharX(this.secondaryCharIndex);
          this.secondaryCharHighlight.y_44 = this.getSecondaryCharY(this.secondaryCharIndex);
        }

        case GLFW_KEY_ESCAPE -> {
          playSound(3);
          unloadRenderable(this.secondaryCharHighlight);
          this.loadingStage = 2;
        }

        case GLFW_KEY_ENTER, GLFW_KEY_S -> {
          this.secondaryCharHighlight.x_40 = this.getSecondaryCharX(this.secondaryCharIndex);
          this.secondaryCharHighlight.y_44 = this.getSecondaryCharY(this.secondaryCharIndex);

          int charCount = 0;
          for(int charSlot = 0; charSlot < 3; charSlot++) {
            if(gameState_800babc8.charIndex_88.get(charSlot).get() != -1) {
              charCount++;
            }
          }

          final int secondaryCharIndex = secondaryCharIndices_800bdbf8.get(this.secondaryCharIndex).get();

          if(((Config.unlockParty() && charCount >= 2) || secondaryCharIndex != -1) && (secondaryCharIndex == -1 || (gameState_800babc8.charData_32c.get(secondaryCharIndex).partyFlags_04.get() & 0x2) != 0)) {
            playSound(2);
            final int charIndex = gameState_800babc8.charIndex_88.get(this.primaryCharIndex).get();
            gameState_800babc8.charIndex_88.get(this.primaryCharIndex).set(secondaryCharIndex);
            secondaryCharIndices_800bdbf8.get(this.secondaryCharIndex).set(charIndex);
            this.loadingStage = 1;
          } else {
            playSound(40);
          }
        }
      }
    }
  }
}
