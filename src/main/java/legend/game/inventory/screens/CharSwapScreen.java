package legend.game.inventory.screens;

import legend.core.GameEngine;
import legend.core.MathHelper;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputMod;
import legend.game.characters.CharacterData2c;
import legend.game.characters.VitalsStat;
import legend.game.inventory.screens.controls.CharacterCard;
import legend.game.modding.coremod.CoreMod;
import legend.game.textures.TextureAtlasIcon;
import legend.game.types.Renderable58;

import java.util.Set;

import static legend.core.GameEngine.CONFIG;
import static legend.game.FullScreenEffects.startFadeEffect;
import static legend.game.Menus.deallocateRenderables;
import static legend.game.Menus.unloadRenderable;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.cacheCharacterSlots;
import static legend.game.SItem.charSwapGlyphs_80114160;
import static legend.game.SItem.initHighlight;
import static legend.game.SItem.renderFourDigitHp;
import static legend.game.SItem.renderFourDigitNumber;
import static legend.game.SItem.renderGlyphs;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.secondaryCharIds_800bdbf8;
import static legend.game.characters.CharacterData2c.CANT_REMOVE;
import static legend.game.characters.CharacterData2c.CAN_BE_IN_PARTY;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_LEFT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_RIGHT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_UP;
import static legend.game.sound.Audio.playMenuSound;
import static legend.lodmod.LodMod.HP_STAT;
import static legend.lodmod.LodMod.MP_STAT;

public class CharSwapScreen extends MenuScreen {
  private int loadingStage;
  private final Runnable unload;

  /** Allows list wrapping, but only on new input */
  private boolean allowWrapX = true;
  private boolean allowWrapY = true;

  private final CharacterCard[] characterCard = new CharacterCard[3];

  private int primaryCharSlot;
  private int secondaryCharSlot;
  private Renderable58 primaryCharHighlight;
  private Renderable58 secondaryCharHighlight;

  private final MV transforms = new MV();

  public CharSwapScreen(final Runnable unload) {
    this.unload = unload;

    for(int i = 0; i < this.characterCard.length; i++) {
      this.characterCard[i] = this.addControl(new CharacterCard());
      this.characterCard[i].setPos(8, 16 + i * 72);
    }
  }

  @Override
  protected void render() {
    switch(this.loadingStage) {
      case 0 -> {
        startFadeEffect(2, 10);

        this.primaryCharSlot = 0;
        this.secondaryCharSlot = 0;

        /* When unlock party is disabled, rearrange party to fit retail expectations. */
        if(!CONFIG.getConfig(CoreMod.UNLOCK_PARTY_CONFIG.get())) {
          // Remove any chars from the main party that can't be in the party
          gameState_800babc8.charIds_88.removeIf(charId -> (gameState_800babc8.charData_32c.get(charId).partyFlags_04 & CAN_BE_IN_PARTY) == 0);

          // Add any chars who are required in the party but aren't currently in it
          secondaryCharIds_800bdbf8.intStream()
            .filter(charId -> !gameState_800babc8.charIds_88.contains(charId))
            .filter(charId -> (gameState_800babc8.charData_32c.get(charId).partyFlags_04 & CANT_REMOVE) != 0)
            .forEach(gameState_800babc8.charIds_88::add);

          cacheCharacterSlots();

          // Add secondary chars to the main party if there are empty slots
          if(gameState_800babc8.charIds_88.size() < 3) {
            for(int i = 0; i < 3 - gameState_800babc8.charIds_88.size() && i < secondaryCharIds_800bdbf8.size(); i++) {
              gameState_800babc8.charIds_88.add(secondaryCharIds_800bdbf8.getInt(i));
            }
          }

          // Sort Dart, then required chars, then others
          gameState_800babc8.charIds_88.sort((id1, id2) -> {
            // Dart is always first
            if(id1 == 0) {
              return -1;
            }

            // Sort required before others
            final boolean required1 = (gameState_800babc8.charData_32c.get(id1).partyFlags_04 & CANT_REMOVE) != 0;
            final boolean required2 = (gameState_800babc8.charData_32c.get(id2).partyFlags_04 & CANT_REMOVE) != 0;

            if(required1 && !required2) {
              return -1;
            }

            if(required2 && !required1) {
              return 1;
            }

            // Maintain order of other chars
            return 0;
          });

          // Remove chars if active party size is > 3
          if(gameState_800babc8.charIds_88.size() > 3) {
            gameState_800babc8.charIds_88.removeElements(3, gameState_800babc8.charIds_88.size());
          }

          cacheCharacterSlots();
        }

        for(int i = 0; i < gameState_800babc8.charIds_88.size(); i++) {
          if(CONFIG.getConfig(CoreMod.UNLOCK_PARTY_CONFIG.get()) || (gameState_800babc8.charData_32c.get(gameState_800babc8.charIds_88.getInt(i)).partyFlags_04 & CANT_REMOVE) == 0) {
            this.primaryCharSlot = i;
            break;
          }
        }

        this.loadingStage++;
      }

      case 1 -> {
        deallocateRenderables(0xff);
        renderGlyphs(charSwapGlyphs_80114160, 0, 0);
        this.primaryCharHighlight = allocateUiElement(0x7f, 0x7f, 16, this.getSlotY(this.primaryCharSlot));
        initHighlight(this.primaryCharHighlight);
        this.primaryCharHighlight.z_3c += 0.5f;
        this.renderCharacterSwapScreen(0xff);

        for(int i = 0; i < this.characterCard.length; i++) {
          if(i < gameState_800babc8.charIds_88.size()) {
            this.characterCard[i].setCharacter(gameState_800babc8.getCharacterBySlot(i));
            this.characterCard[i].setDontSelect(!CONFIG.getConfig(CoreMod.UNLOCK_PARTY_CONFIG.get()) && (gameState_800babc8.charData_32c.get(gameState_800babc8.charIds_88.getInt(i)).partyFlags_04 & CANT_REMOVE) != 0);
            this.characterCard[i].show();
          } else {
            this.characterCard[i].hide();
          }
        }

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

    for(int i = 0; i < secondaryCharIds_800bdbf8.size(); i++) {
      this.renderSecondaryChar(this.getSecondaryCharX(i), this.getSecondaryCharY(i), secondaryCharIds_800bdbf8.getInt(i), allocate);
    }
  }

  private void renderSecondaryChar(final int x, final int y, final int charIndex, final boolean allocate) {
    final CharacterData2c character = gameState_800babc8.charData_32c.get(charIndex);
    final TextureAtlasIcon icon = GameEngine.getTextureAtlas().getIcon(character.template.getRegistryId());
    this.transforms.transfer.set(x - 6.0f, y + 8.0f, 132.0f);
    this.transforms.scaling(48.0f, 48.0f, 1.0f);
    icon.render(this.transforms);

    if(allocate) {
      final VitalsStat hp = character.stats.getStat(HP_STAT.get());
      final VitalsStat mp = character.stats.getStat(MP_STAT.get());

      allocateUiElement(0x50, 0x50, x, y).z_3c = 33;
      allocateUiElement(0x9c, 0x9c, x, y);

      if(!CONFIG.getConfig(CoreMod.UNLOCK_PARTY_CONFIG.get()) && (character.partyFlags_04 & CAN_BE_IN_PARTY) == 0) {
        allocateUiElement(0x72, 0x72, x, y + 24).z_3c = 33;
      }

      renderFourDigitNumber(x + 25, y + 57, character.level_12);
      renderFourDigitNumber(x + 25, y + 68, character.dlevel_13);
      renderFourDigitHp(x + 25, y + 79, hp.getCurrent(), hp.getMax());
      renderFourDigitNumber(x + 25, y + 90, mp.getCurrent());
    }
  }

  private int getSecondaryCharX(final int slot) {
    return 198 + slot % 3 * 57;
  }

  private int getSecondaryCharY(final int slot) {
    return 16 + slot / 3 * 106;
  }

  @Override
  protected InputPropagation mouseMove(final double x, final double y) {
    if(super.mouseMove(x, y) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage == 2) {
      for(int i = 0; i < gameState_800babc8.charIds_88.size(); i++) {
        if((CONFIG.getConfig(CoreMod.UNLOCK_PARTY_CONFIG.get()) || (gameState_800babc8.charData_32c.get(gameState_800babc8.charIds_88.getInt(i)).partyFlags_04 & CANT_REMOVE) == 0) && this.primaryCharSlot != i && MathHelper.inBox((int)x, (int)y, 8, this.getSlotY(i), 174, 65)) {
          playMenuSound(1);
          this.primaryCharSlot = i;
          this.primaryCharHighlight.y_44 = this.getSlotY(i);
          return InputPropagation.HANDLED;
        }
      }
    } else if(this.loadingStage == 3) {
      for(int i = 0; i < 6; i++) {
        if(this.secondaryCharSlot != i && MathHelper.inBox((int)x, (int)y, this.getSecondaryCharX(i) - 8, this.getSecondaryCharY(i), 57, 102)) {
          playMenuSound(1);
          this.secondaryCharSlot = i;
          this.secondaryCharHighlight.x_40 = this.getSecondaryCharX(this.secondaryCharSlot);
          this.secondaryCharHighlight.y_44 = this.getSecondaryCharY(this.secondaryCharSlot);
          return InputPropagation.HANDLED;
        }
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation mouseClick(final double x, final double y, final int button, final Set<InputMod> mods) {
    if(super.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage == 2) {
      for(int i = 0; i < gameState_800babc8.charIds_88.size(); i++) {
        if(MathHelper.inBox((int)x, (int)y, 8, this.getSlotY(i), 174, 65)) {
          this.menuStage2Select();
          return InputPropagation.HANDLED;
        }
      }
    } else if(this.loadingStage == 3) {
      for(int i = 0; i < 6; i++) {
        if(MathHelper.inBox((int)x, (int)y, this.getSecondaryCharX(i) - 8, this.getSecondaryCharY(i), 57, 102)) {
          this.menuStage3Select();
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
    if(this.primaryCharSlot > 0 && (CONFIG.getConfig(CoreMod.UNLOCK_PARTY_CONFIG.get()) || (gameState_800babc8.charData_32c.get(gameState_800babc8.charIds_88.getInt(this.primaryCharSlot - 1)).partyFlags_04 & CANT_REMOVE) == 0)) {
      playMenuSound(1);
      this.primaryCharSlot--;
    } else if(this.allowWrapY) {
      for(int i = gameState_800babc8.charIds_88.size() - 1; i > this.primaryCharSlot; i--) {
        if(CONFIG.getConfig(CoreMod.UNLOCK_PARTY_CONFIG.get()) || (gameState_800babc8.charData_32c.get(gameState_800babc8.charIds_88.getInt(i)).partyFlags_04 & CANT_REMOVE) == 0) {
          playMenuSound(1);
          this.primaryCharSlot = i;
          break;
        }
      }
    }

    this.primaryCharHighlight.y_44 = this.getSlotY(this.primaryCharSlot);
  }

  private void menuStage2NavigateDown() {
    if((this.primaryCharSlot < 2 && CONFIG.getConfig(CoreMod.UNLOCK_PARTY_CONFIG.get()) || this.primaryCharSlot < gameState_800babc8.charIds_88.size() - 1 && (gameState_800babc8.charData_32c.get(gameState_800babc8.charIds_88.getInt(this.primaryCharSlot + 1)).partyFlags_04 & CANT_REMOVE) == 0)) {
      playMenuSound(1);
      this.primaryCharSlot++;
    } else if(this.allowWrapY) {
      for(int i = 0; i < this.primaryCharSlot; i++) {
        if(CONFIG.getConfig(CoreMod.UNLOCK_PARTY_CONFIG.get()) || (gameState_800babc8.charData_32c.get(gameState_800babc8.charIds_88.getInt(i)).partyFlags_04 & CANT_REMOVE) == 0) {
          playMenuSound(1);
          this.primaryCharSlot = i;
          break;
        }
      }
    }

    this.primaryCharHighlight.y_44 = this.getSlotY(this.primaryCharSlot);
  }

  private void menuStage2Select() {
    if(this.primaryCharSlot >= gameState_800babc8.charIds_88.size() || CONFIG.getConfig(CoreMod.UNLOCK_PARTY_CONFIG.get()) || (gameState_800babc8.charData_32c.get(gameState_800babc8.charIds_88.getInt(this.primaryCharSlot)).partyFlags_04 & CANT_REMOVE) == 0) {
      playMenuSound(2);
      this.secondaryCharHighlight = allocateUiElement(0x80, 0x80, this.getSecondaryCharX(this.secondaryCharSlot), this.getSecondaryCharY(this.secondaryCharSlot));
      initHighlight(this.secondaryCharHighlight);
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
    if(this.secondaryCharSlot > 2) {
      playMenuSound(1);
      this.secondaryCharSlot -= 3;
    } else if(this.allowWrapY) {
      playMenuSound(1);
      this.secondaryCharSlot += 3;
    }

    this.secondaryCharHighlight.x_40 = this.getSecondaryCharX(this.secondaryCharSlot);
    this.secondaryCharHighlight.y_44 = this.getSecondaryCharY(this.secondaryCharSlot);
  }

  private void menuStage3NavigateDown() {
    if(this.secondaryCharSlot < 3) {
      playMenuSound(1);
      this.secondaryCharSlot += 3;
    } else if(this.allowWrapY) {
      playMenuSound(1);
      this.secondaryCharSlot -= 3;
    }

    this.secondaryCharHighlight.x_40 = this.getSecondaryCharX(this.secondaryCharSlot);
    this.secondaryCharHighlight.y_44 = this.getSecondaryCharY(this.secondaryCharSlot);
  }

  private void menuStage3NavigateLeft() {
    if(this.secondaryCharSlot > 0) {
      playMenuSound(1);
      this.secondaryCharSlot--;
    } else if(this.allowWrapX) {
      playMenuSound(1);
      this.secondaryCharSlot = 5;
    }

    this.secondaryCharHighlight.x_40 = this.getSecondaryCharX(this.secondaryCharSlot);
    this.secondaryCharHighlight.y_44 = this.getSecondaryCharY(this.secondaryCharSlot);
  }

  private void menuStage3NavigateRight() {
    if(this.secondaryCharSlot < 5) {
      playMenuSound(1);
      this.secondaryCharSlot++;
    } else if(this.allowWrapX) {
      playMenuSound(1);
      this.secondaryCharSlot = 0;
    }

    this.secondaryCharHighlight.x_40 = this.getSecondaryCharX(this.secondaryCharSlot);
    this.secondaryCharHighlight.y_44 = this.getSecondaryCharY(this.secondaryCharSlot);
  }

  private void menuStage3Select() {
    this.secondaryCharHighlight.x_40 = this.getSecondaryCharX(this.secondaryCharSlot);
    this.secondaryCharHighlight.y_44 = this.getSecondaryCharY(this.secondaryCharSlot);

    final int charCount = gameState_800babc8.charIds_88.size();
    final int primaryCharId = this.primaryCharSlot < gameState_800babc8.charIds_88.size() ? gameState_800babc8.charIds_88.getInt(this.primaryCharSlot) : -1;
    final int secondaryCharId = this.secondaryCharSlot < secondaryCharIds_800bdbf8.size() ? secondaryCharIds_800bdbf8.getInt(this.secondaryCharSlot) : -1;

    if(
      // Unlock party
      CONFIG.getConfig(CoreMod.UNLOCK_PARTY_CONFIG.get()) && (
        // If we have one char in primary, you can't swap with an empty secondary
        charCount == 1 && secondaryCharId != -1 ||
        // If we have more than one char in primary, you can swap anything except for two empty spaces
        charCount > 1 && !(primaryCharId == -1 && secondaryCharId == -1)
      ) ||
      // Retail
      secondaryCharId != -1 && (gameState_800babc8.charData_32c.get(secondaryCharId).partyFlags_04 & CAN_BE_IN_PARTY) != 0
    ) {
      playMenuSound(2);

      if(primaryCharId != -1) {
        if(secondaryCharId != -1) {
          gameState_800babc8.charIds_88.set(this.primaryCharSlot, secondaryCharId);
        } else {
          gameState_800babc8.charIds_88.removeInt(this.primaryCharSlot);
        }

        if(this.secondaryCharSlot < secondaryCharIds_800bdbf8.size()) {
          secondaryCharIds_800bdbf8.set(this.secondaryCharSlot, primaryCharId);
        } else {
          secondaryCharIds_800bdbf8.add(primaryCharId);
        }
      } else {
        gameState_800babc8.charIds_88.add(secondaryCharId);
        secondaryCharIds_800bdbf8.removeInt(this.secondaryCharSlot);
      }
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
