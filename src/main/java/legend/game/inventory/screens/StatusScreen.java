package legend.game.inventory.screens;

import legend.core.platform.input.InputAction;
import legend.game.characters.CharacterData2c;
import legend.game.i18n.I18n;
import legend.game.inventory.SpellStats0c;
import legend.game.inventory.screens.controls.CharacterCard;
import legend.game.types.Renderable58;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.List;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.REGISTRIES;
import static legend.game.FullScreenEffects.startFadeEffect;
import static legend.game.Menus.deallocateRenderables;
import static legend.game.SItem.UI_TEXT;
import static legend.game.SItem.addLeftRightArrows;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.characterStatusGlyphs_801141a4;
import static legend.game.SItem.renderCharacter;
import static legend.game.SItem.renderCharacterEquipment;
import static legend.game.SItem.renderCharacterStats;
import static legend.game.SItem.renderGlyphs;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Text.renderText;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_LEFT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_RIGHT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_UP;
import static legend.game.modding.coremod.CoreMod.REDUCE_MOTION_FLASHING_CONFIG;
import static legend.game.sound.Audio.playMenuSound;

public class StatusScreen extends MenuScreen {
  protected int loadingStage;

  private final CharacterCard characterCard;
  private int charSlot;

  private boolean allowWrapX = true;
  private double scrollAccumulator;

  private final Runnable unload;

  private final int MAX_SPELLS = 7;
  private int spellScrollIndex = 0;
  private Renderable58 upArrow;
  private Renderable58 downArrow;

  public StatusScreen(final Runnable unload) {
    this(0, unload);
  }

  public StatusScreen(final int charSlot, final Runnable unload) {
    this.charSlot = charSlot;
    this.unload = unload;
    this.characterCard = this.addControl(new CharacterCard());
    this.characterCard.setCharacter(gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.getInt(charSlot)));
    this.characterCard.setPos(8, 20);
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
        addLeftRightArrows(this.charSlot, characterIndices_800bdbb8.size());
        this.renderStatusMenu(this.charSlot, 0);

        if(this.scrollAccumulator >= 1.0d) {
          this.scrollAccumulator -= 1.0d;

          if(this.charSlot > 0) {
            this.scroll(this.charSlot - 1);
          }
        }

        if(this.scrollAccumulator <= -1.0d) {
          this.scrollAccumulator += 1.0d;

          if(this.charSlot < characterIndices_800bdbb8.size() - 1) {
            this.scroll(this.charSlot + 1);
          }
        }
      }

      case 3 -> {
        addLeftRightArrows(this.charSlot, characterIndices_800bdbb8.size());
        this.renderStatusMenu(this.charSlot, 0);
        this.unload.run();
      }
    }
  }

  private void scroll(final int slot) {
    if(characterIndices_800bdbb8.size() > 1) {
      playMenuSound(1);
      this.charSlot = slot;
      this.characterCard.setCharacter(gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.getInt(this.charSlot)));
      this.loadingStage = 1;
    }
  }

  private void renderStatusMenu(final int charSlot, final int a1) {
    renderCharacterStats(characterIndices_800bdbb8.getInt(charSlot), null);
    renderCharacterEquipment(characterIndices_800bdbb8.getInt(charSlot), a1 == 0xff);
    this.renderCharacterSpells(characterIndices_800bdbb8.getInt(charSlot), a1 == 0xff);
  }

  private void renderCharacterSpells(final int charIndex, final boolean allocate) {
    if(charIndex == -1) {
      return;
    }

    if(allocate) {
      allocateUiElement(0x58, 0x58, 194, 101);

      if(!CONFIG.getConfig(REDUCE_MOTION_FLASHING_CONFIG.get())) {
        this.upArrow = allocateUiElement(61, 68, 359, 123);
        this.downArrow = allocateUiElement(53, 60, 359, 207);
      } else {
        this.upArrow = allocateUiElement(67, 67, 359, 123);
        this.downArrow = allocateUiElement(59, 59, 359, 207);
      }
    }

    final CharacterData2c character = gameState_800babc8.charData_32c.get(charIndex);

    if(character.hasDragoon()) {
      final List<RegistryId> unlockedSpells = character.getUnlockedSpells();
      final int allSpellCount = character.getAllSpells().size();

      for(int i = this.spellScrollIndex; i < this.spellScrollIndex + this.MAX_SPELLS; i++) {
        if(i < unlockedSpells.size()) {
          this.renderNumber(198, 127 + (i - this.spellScrollIndex) * 14, i, 2, 0, 0x7ca9);
          final RegistryId spellId = unlockedSpells.get(i);
          final SpellStats0c spell = REGISTRIES.spells.getEntry(spellId).get();
          renderText(I18n.translate(spell), 210, 125 + (i - this.spellScrollIndex) * 14, UI_TEXT);
          this.renderNumber(342, 128 + (i - this.spellScrollIndex) * 14, spell.mp_06, 3);
        }
      }

      if(this.upArrow != null) {
        if(this.spellScrollIndex != 0) {
          this.upArrow.flags_00 &= ~Renderable58.FLAG_INVISIBLE;
        } else {
          this.upArrow.flags_00 |= Renderable58.FLAG_INVISIBLE;
        }
      }

      if(this.downArrow != null) {
        if(this.spellScrollIndex + this.MAX_SPELLS < allSpellCount) {
          this.downArrow.flags_00 &= ~Renderable58.FLAG_INVISIBLE;
        } else {
          this.downArrow.flags_00 |= Renderable58.FLAG_INVISIBLE;
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
    } else if(characterIndices_800bdbb8.size() > 1 && this.allowWrapX) {
      this.scroll(characterIndices_800bdbb8.size() - 1);
    }
    this.spellScrollIndex = 0;
  }

  private void menuNavigateRight() {
    if(this.charSlot < characterIndices_800bdbb8.size() - 1) {
      this.scroll(this.charSlot + 1);
    } else if(characterIndices_800bdbb8.size() > 1 && this.allowWrapX) {
      this.scroll(0);
    }
    this.spellScrollIndex = 0;
  }

  private void menuNavigateUp() {
    if(this.spellScrollIndex != 0) {
      this.spellScrollIndex -= 1;
    }
  }

  private void menuNavigateDown() {
    if(this.downArrow.flags_00 < Renderable58.FLAG_INVISIBLE) {
      this.spellScrollIndex += 1;
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

    if(action == INPUT_ACTION_MENU_UP.get()) {
      this.menuNavigateUp();
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_DOWN.get()) {
      this.menuNavigateDown();
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
