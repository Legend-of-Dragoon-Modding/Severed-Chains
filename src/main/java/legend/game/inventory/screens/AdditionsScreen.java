package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputMod;
import legend.game.additions.Addition;
import legend.game.characters.CharacterAdditionInfo;
import legend.game.characters.CharacterData2c;
import legend.game.i18n.I18n;
import legend.game.inventory.screens.controls.CharacterCard;
import legend.game.inventory.screens.controls.Glyph;
import legend.game.types.Renderable58;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static legend.core.GameEngine.PLATFORM;
import static legend.core.GameEngine.REGISTRIES;
import static legend.game.FullScreenEffects.startFadeEffect;
import static legend.game.Menus.deallocateRenderables;
import static legend.game.Menus.unloadRenderable;
import static legend.game.SItem.UI_TEXT;
import static legend.game.SItem.UI_TEXT_CENTERED;
import static legend.game.SItem.UI_TEXT_SELECTED;
import static legend.game.SItem.addLeftRightArrows;
import static legend.game.SItem.additionGlyphs_801141e4;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.initHighlight;
import static legend.game.SItem.renderCharacter;
import static legend.game.SItem.renderGlyphs;
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
import static legend.game.sound.Audio.playMenuSound;
import static legend.game.types.Renderable58.FLAG_DELETE_AFTER_RENDER;

public class AdditionsScreen extends MenuScreen {
  private static final int ADDITION_SLOTS = 7;

  private int loadingStage;
  private double scrollAccumulator;
  private final Runnable unload;

  private final CharacterCard characterCard;
  private int charSlot;
  private int selectedSlot;
  private Renderable58 additionHighlight;
  private final List<RegistryId> additionIds = new ArrayList<>();
  private int additionCount;
  private int additionScroll;

  private final Glyph upArrow;
  private final Glyph downArrow;

  /** Allows list wrapping, but only on new input */
  private boolean allowWrapX = true;
  private boolean allowWrapY = true;

  public AdditionsScreen(final Runnable unload) {
    this.unload = unload;

    this.characterCard = this.addControl(new CharacterCard());
    this.characterCard.setPos(8, 20);

    this.upArrow = this.addControl(Glyph.blueSpinnerUp());
    this.upArrow.setPos(this.getWidth(), 91);

    this.downArrow = this.addControl(Glyph.blueSpinnerDown());
    this.downArrow.setPos(this.getWidth(), this.getHeight() - 45);
  }

  @Override
  protected void render() {
    switch(this.loadingStage) {
      case 0 -> {
        this.charSlot = 0;
        this.selectedSlot = 0;
        this.additionScroll = 0;
        this.additionHighlight = null;
        startFadeEffect(2, 10);
        deallocateRenderables(0xff);
        this.loadingStage++;
      }

      case 1 -> {
        deallocateRenderables(0);

        final CharacterData2c character = gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.getInt(this.charSlot));
        this.characterCard.setCharacter(character);

        this.additionIds.clear();
        this.additionIds.addAll(character.getUnlockedAdditions());
        this.additionCount = character.getAllAdditions().size();

        if(!this.additionIds.isEmpty()) {
          this.additionHighlight = allocateUiElement(117, 117, 39, this.getAdditionSlotY(this.selectedSlot) - 4);
          initHighlight(this.additionHighlight);
        }

        this.updateAdditionScroll();

        allocateUiElement(69, 69, 0, 0); // Background left
        allocateUiElement(70, 70, 192, 0); // Background right
        this.renderAdditions(this.charSlot, this.additionIds, character.selectedAddition_19, 0xffL);
        this.loadingStage++;
      }

      case 2 -> {
        addLeftRightArrows(this.charSlot, characterIndices_800bdbb8.size());
        this.renderAdditions(this.charSlot, this.additionIds, gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.getInt(this.charSlot)).selectedAddition_19, 0);

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

      // Fade out
      case 100 -> {
        this.renderAdditions(this.charSlot, this.additionIds, gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.getInt(this.charSlot)).selectedAddition_19, 0);
        this.unload.run();
      }
    }
  }

  private void renderAdditions(final int charSlot, final List<RegistryId> additions, final RegistryId selectedAddition, final long a4) {
    final boolean allocate = a4 == 0xff;
    final int charIndex = characterIndices_800bdbb8.getInt(charSlot);

    if(additions.isEmpty()) {
      renderText(I18n.translate("lod_core.ui.additions.no_additions"), this.getWidth() / 2.0f, 150, UI_TEXT_CENTERED);
    } else {
      if(allocate) {
        renderGlyphs(additionGlyphs_801141e4, 0, 0);
      }

      final CharacterData2c charData = gameState_800babc8.charData_32c.get(charIndex);

      for(int i = 0; i < Math.min(this.additionCount - this.additionScroll, ADDITION_SLOTS); i++) {
        final int y = this.getAdditionSlotY(i);
        this.renderNumber(21, y, i + this.additionScroll + 1, 2, 0, 0x7ca9);
      }

      for(int i = 0; i < Math.min(this.additionIds.size() - this.additionScroll, ADDITION_SLOTS); i++) {
        final RegistryId additionId = this.additionIds.get(i + this.additionScroll);
        final Addition addition = REGISTRIES.additions.getEntry(additionId).get();
        final int y = this.getAdditionSlotY(i);

        renderText(addition.getName(), 33, y - 2, !additionId.equals(selectedAddition) ? UI_TEXT : UI_TEXT_SELECTED);

        final CharacterAdditionInfo info = charData.getAdditionInfo(additionId);
        this.renderNumber(197, y, info.level, 3);
        this.renderNumber(230, y, addition.getHitCount(charData, info), 3);
        this.renderNumber(263, y, addition.getSp(charData, info), 3);
        this.renderNumber(297, y, addition.getDamage(charData, info), 3);
        this.renderNumber(322, y, info.xp, 3);

        if(info.level < addition.getMaxLevel(charData, info)) {
          this.renderNumber(342, y, addition.getXpToNextLevel(charData, info), 3);
        } else {
          renderCharacter(354, y, 218).flags_00 |= FLAG_DELETE_AFTER_RENDER; // Dash if at max XP
        }
      }
    }
  }

  private int getAdditionSlotY(final int slot) {
    return 113 + slot * 14;
  }

  private void scroll(final int scroll) {
    playMenuSound(1);
    this.charSlot = scroll;

    if(this.additionHighlight != null) {
      unloadRenderable(this.additionHighlight);
      this.additionHighlight = null;
    }

    this.loadingStage = 1;
  }

  private void updateAdditionScroll() {
    if(this.additionScroll > this.selectedSlot) {
      this.additionScroll = this.selectedSlot;
    }

    if(this.additionScroll < this.selectedSlot - ADDITION_SLOTS + 1) {
      this.additionScroll = this.selectedSlot - ADDITION_SLOTS + 1;
    }

    if(!this.additionIds.isEmpty()) {
      this.additionHighlight.y_44 = this.getAdditionSlotY(this.selectedSlot - this.additionScroll) - 4;
    }

    this.upArrow.setVisibility(!this.additionIds.isEmpty() && this.additionScroll > 0);
    this.downArrow.setVisibility(!this.additionIds.isEmpty() && this.additionCount - this.additionScroll > ADDITION_SLOTS);
  }

  @Override
  protected InputPropagation mouseMove(final double x, final double y) {
    if(super.mouseMove(x, y) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage != 2) {
      return InputPropagation.PROPAGATE;
    }

    for(int i = 0; i < Math.min(this.additionIds.size(), ADDITION_SLOTS); i++) {
      if(this.selectedSlot != i && MathHelper.inBox((int)x, (int)y, 31, this.getAdditionSlotY(i) - 3, 141, 13)) {
        playMenuSound(1);
        this.selectedSlot = i;
        this.additionHighlight.y_44 = this.getAdditionSlotY(i) - 4;
        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation mouseClick(final double x, final double y, final int button, final Set<InputMod> mods) {
    if(super.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage != 2 || !mods.isEmpty()) {
      return InputPropagation.PROPAGATE;
    }

    if(button == PLATFORM.getMouseButton(0) && !this.additionIds.isEmpty()) {
      for(int i = 0; i < Math.min(this.additionIds.size(), ADDITION_SLOTS); i++) {
        if(MathHelper.inBox((int)x, (int)y, 31, this.getAdditionSlotY(i) - 3, 141, 13)) {
          this.selectedSlot = i + this.additionScroll;
          this.additionHighlight.y_44 = this.getAdditionSlotY(i) - 4;
          gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.getInt(this.charSlot)).selectedAddition_19 = this.additionIds.get(this.selectedSlot);
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
    if(!this.additionIds.isEmpty()) {
      if(this.selectedSlot > 0) {
        playMenuSound(1);
        this.selectedSlot--;
      } else if(this.allowWrapY) {
        playMenuSound(1);
        this.selectedSlot = this.additionCount - 1;
      }

      this.updateAdditionScroll();
    }
  }

  private void menuNavigateDown() {
    if(!this.additionIds.isEmpty()) {
      if(this.selectedSlot < this.additionCount - 1) {
        playMenuSound(1);
        this.selectedSlot++;
      } else if(this.allowWrapY) {
        playMenuSound(1);
        this.selectedSlot = 0;
      }

      this.updateAdditionScroll();
    }
  }

  private void menuNavigateLeft() {
    if(this.charSlot > 0) {
      this.scroll(this.charSlot - 1);
    } else if(characterIndices_800bdbb8.size() > 1 && this.allowWrapX) {
      this.scroll(characterIndices_800bdbb8.size() - 1);
    }
  }

  private void menuNavigateRight() {
    if(this.charSlot < characterIndices_800bdbb8.size() - 1) {
      this.scroll(this.charSlot + 1);
    } else if(characterIndices_800bdbb8.size() > 1 && this.allowWrapX) {
      this.scroll(0);
    }
  }

  private void menuNavigateHome() {
    if(this.selectedSlot > 0) {
      playMenuSound(1);
      this.selectedSlot = 0;
      this.updateAdditionScroll();
    }
  }

  private void menuNavigateEnd() {
    if(this.selectedSlot < ADDITION_SLOTS - 1) {
      playMenuSound(1);
      this.selectedSlot = ADDITION_SLOTS - 1;
      this.updateAdditionScroll();
    }
  }

  private void menuSelect() {
    if(this.selectedSlot < this.additionIds.size()) {
      gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.getInt(this.charSlot)).selectedAddition_19 = this.additionIds.get(this.selectedSlot);
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
