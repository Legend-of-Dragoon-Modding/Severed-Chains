package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.core.memory.Method;
import legend.game.i18n.I18n;
import legend.game.input.InputAction;
import legend.game.inventory.EquipItemResult;
import legend.game.inventory.Equipment;
import legend.game.types.MenuEntries;
import legend.game.types.MenuEntryStruct04;
import legend.game.types.Renderable58;

import static legend.game.SItem.FUN_801034cc;
import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.canEquip;
import static legend.game.SItem.characterCount_8011d7c4;
import static legend.game.SItem.equipItem;
import static legend.game.SItem.equipmentGlyphs_80114180;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.SItem.loadItemsAndEquipmentForDisplay;
import static legend.game.SItem.renderCharacterEquipment;
import static legend.game.SItem.renderCharacterSlot;
import static legend.game.SItem.renderCharacterStats;
import static legend.game.SItem.renderGlyphs;
import static legend.game.SItem.renderMenuItems;
import static legend.game.SItem.renderString;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.addHp;
import static legend.game.Scus94491BpeSegment_8002.addMp;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.giveEquipment;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_8002.setInventoryFromDisplay;
import static legend.game.Scus94491BpeSegment_8002.takeEquipment;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;

public class EquipmentScreen extends MenuScreen {
  private int loadingStage;
  private double scrollAccumulator;
  private final Runnable unload;

  private int slotScroll;
  private int selectedSlot;
  private int charSlot;
  private int equipmentCount;
  private Renderable58 itemHighlight;
  private Renderable58 _800bdb9c;
  private Renderable58 _800bdba0;

  private final MenuEntries<Equipment> menuItems = new MenuEntries<>();

  public EquipmentScreen(final Runnable unload) {
    this.unload = unload;
  }

  @Override
  protected void render() {
    switch(this.loadingStage) {
      case 0:
        startFadeEffect(2, 10);
        deallocateRenderables(0xff);
        this.loadingStage++;

      case 1:
        this.slotScroll = 0;
        this.selectedSlot = 0;
        this.loadingStage++;

      case 2:
        deallocateRenderables(0);
        renderGlyphs(equipmentGlyphs_80114180, 0, 0);

        if(this.itemHighlight == null) {
          this.itemHighlight = allocateUiElement(0x79, 0x79, this.FUN_800fc824(1), 0);
          FUN_80104b60(this.itemHighlight);
        }

        this.itemHighlight.y_44 = this.menuHighlightPositionY(this.selectedSlot);
        this.equipmentCount = this.getEquippableItemsForCharacter(characterIndices_800bdbb8[this.charSlot]);
        this.slotScroll = MathHelper.clamp(this.slotScroll, 0, Math.max(0, this.equipmentCount - 4));

        this.renderEquipmentScreen(this.charSlot, this.selectedSlot, this.slotScroll, 0xff);
        this.loadingStage++;
        break;

      case 3:
        FUN_801034cc(this.charSlot, characterCount_8011d7c4);
        this.renderEquipmentScreen(this.charSlot, this.selectedSlot, this.slotScroll, 0);

        if(this.scrollAccumulator >= 1.0d) {
          this.scrollAccumulator -= 1.0d;

          if(this.slotScroll > 0) {
            playMenuSound(1);
            this.scroll(this.slotScroll - 1);
          }
        }

        if(this.scrollAccumulator <= -1.0d) {
          this.scrollAccumulator += 1.0d;

          if(this.slotScroll < this.equipmentCount - 4) {
            playMenuSound(1);
            this.scroll(this.slotScroll + 1);
          }
        }

        break;

      // Fade out
      case 100:
        this.renderEquipmentScreen(this.charSlot, this.selectedSlot, this.slotScroll, 0);
        this.unload.run();
        break;
    }
  }

  private void scroll(final int scroll) {
    this.slotScroll = scroll;
    this.itemHighlight.y_44 = this.menuHighlightPositionY(this.selectedSlot);
  }

  private int getEquippableItemsForCharacter(final int charIndex) {
    this.menuItems.clear();

    for(int equipmentSlot = 0; equipmentSlot < gameState_800babc8.equipment_1e8.size(); equipmentSlot++) {
      final Equipment equipment = gameState_800babc8.equipment_1e8.get(equipmentSlot);
      if(canEquip(equipment, charIndex)) {
        if(equipment != gameState_800babc8.charData_32c[charIndex].equipment_14.get(equipment.slot)) {
          final MenuEntryStruct04<Equipment> menuEntry = MenuEntryStruct04.make(equipment);
          menuEntry.itemSlot_01 = equipmentSlot;
          this.menuItems.add(menuEntry);
        }
      }
    }

    return this.menuItems.size();
  }

  private void renderEquipmentScreen(final int charSlot, final int slotIndex, final int slotScroll, final long a3) {
    final boolean allocate = a3 == 0xff;

    renderCharacterSlot(16, 21, characterIndices_800bdbb8[charSlot], allocate, false);
    renderCharacterStats(characterIndices_800bdbb8[charSlot], slotIndex + slotScroll >= this.menuItems.size() ? null : this.menuItems.get(slotIndex + slotScroll).item_00, allocate);
    renderCharacterEquipment(characterIndices_800bdbb8[charSlot], allocate);

    if(allocate) {
      allocateUiElement(90, 0x5a, 194, 96);
      this._800bdb9c = allocateUiElement(61, 0x44, 358, this.menuHighlightPositionY(0));
      this._800bdba0 = allocateUiElement(53, 0x3c, 358, this.menuHighlightPositionY(3));
    }

    renderMenuItems(194, 92, this.menuItems, slotScroll, 4, this._800bdb9c, this._800bdba0);

    if(slotIndex + slotScroll < this.menuItems.size()) {
      renderString(194, 178, I18n.translate(this.menuItems.get(slotIndex + slotScroll).item_00.getDescriptionTranslationKey()), allocate);
    }
  }

  private int menuHighlightPositionY(final int slot) {
    return 99 + slot * 17;
  }

  @Method(0x800fc824L)
  private int FUN_800fc824(final int a0) {
    if(a0 == 0) {
      return 43;
    }

    return 221;
  }

  @Override
  protected InputPropagation mouseMove(final int x, final int y) {
    if(super.mouseMove(x, y) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage != 3) {
      return InputPropagation.PROPAGATE;
    }

    for(int slot = 0; slot < Math.min(4, this.equipmentCount - this.slotScroll); slot++) {
      if(this.selectedSlot != slot && MathHelper.inBox(x, y, 212, this.menuHighlightPositionY(slot), 139, 15)) {
        playMenuSound(1);
        this.selectedSlot = slot;
        this.itemHighlight.y_44 = this.menuHighlightPositionY(slot);
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

    if(this.loadingStage != 3) {
      return InputPropagation.PROPAGATE;
    }

    for(int slot = 0; slot < Math.min(4, this.equipmentCount - this.slotScroll); slot++) {
      if(MathHelper.inBox(x, y, 212, this.menuHighlightPositionY(slot), 139, 15)) {
        this.selectedSlot = slot;
        this.itemHighlight.y_44 = this.menuHighlightPositionY(slot);

        final int itemIndex = this.selectedSlot + this.slotScroll;
        if(itemIndex < this.menuItems.size()) {
          final Equipment equipment = this.menuItems.get(itemIndex).item_00;
          final EquipItemResult previousEquipment = equipItem(equipment, characterIndices_800bdbb8[this.charSlot]);
          takeEquipment(this.menuItems.get(itemIndex).itemSlot_01);

          if(previousEquipment.previousEquipment != null) {
            giveEquipment(previousEquipment.previousEquipment);
          }

          playMenuSound(2);
          loadCharacterStats();
          addHp(characterIndices_800bdbb8[this.charSlot], 0);
          addMp(characterIndices_800bdbb8[this.charSlot], 0);
          this.loadingStage = 2;
        }

        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation mouseScrollHighRes(final double deltaX, final double deltaY) {
    if(super.mouseScrollHighRes(deltaX, deltaY) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage != 3) {
      return InputPropagation.PROPAGATE;
    }

    if(this.scrollAccumulator < 0 && deltaY > 0 || this.scrollAccumulator > 0 && deltaY < 0) {
      this.scrollAccumulator = 0;
    }

    this.scrollAccumulator += deltaY;
    return InputPropagation.HANDLED;
  }

  private void menuEscape() {
    playMenuSound(3);
    this.loadingStage = 100;
  }

  private void menuNavigateUp() {
    if(this.selectedSlot > 0) {
      playMenuSound(1);
      this.selectedSlot--;
    } else {
      this.scrollAccumulator = 1;
    }

    this.itemHighlight.y_44 = this.menuHighlightPositionY(this.selectedSlot);
  }

  private void menuNavigateDown() {
    if(this.selectedSlot < 3) {
      playMenuSound(1);
      this.selectedSlot++;
    } else {
      this.scrollAccumulator = -1;
    }

    this.itemHighlight.y_44 = this.menuHighlightPositionY(this.selectedSlot);
  }

  private void menuNavigateTop() {
    if(this.selectedSlot != 0) {
      playMenuSound(1);
      this.selectedSlot = 0;
      this.itemHighlight.y_44 = this.menuHighlightPositionY(this.selectedSlot);
    }
  }

  private void menuNavigateBottom() {
    if(this.selectedSlot != 3) {
      playMenuSound(1);
      this.selectedSlot = 3;
      this.itemHighlight.y_44 = this.menuHighlightPositionY(this.selectedSlot);
    }
  }

  private void menuNavigatePageUp() {
    if(this.slotScroll - 3 >= 0) {
      playMenuSound(1);
      this.scroll(this.slotScroll - 3);
    } else {
      if(this.slotScroll != 0) {
        this.scroll(0);
      }
    }
  }

  private void menuNavigatePageDown() {
    if(this.slotScroll + 3 < this.equipmentCount - 4) {
      playMenuSound(1);
      this.scroll(this.slotScroll + 3);
    } else {
      if(this.equipmentCount > 4 && this.slotScroll != this.equipmentCount - 4) {
        this.scroll(this.equipmentCount - 4);
      }
    }
  }

  private void menuNavigateLeft() {
    if(this.charSlot > 0) {
      playMenuSound(1);
      this.charSlot--;
      this.loadingStage = 1;
    }
  }

  private void menuNavigateRight() {
    if(this.charSlot < characterCount_8011d7c4 - 1) {
      playMenuSound(1);
      this.charSlot++;
      this.loadingStage = 1;
    }
  }

  private void menuSelect() {
    final int itemIndex = this.selectedSlot + this.slotScroll;

    if(itemIndex < this.menuItems.size()) {
      final Equipment equipment = this.menuItems.get(itemIndex).item_00;
      final EquipItemResult previousEquipment = equipItem(equipment, characterIndices_800bdbb8[this.charSlot]);
      takeEquipment(this.menuItems.get(itemIndex).itemSlot_01);

      if(previousEquipment.previousEquipment != null) {
        giveEquipment(previousEquipment.previousEquipment);
      }

      playMenuSound(2);
      loadCharacterStats();
      addHp(characterIndices_800bdbb8[this.charSlot], 0);
      addMp(characterIndices_800bdbb8[this.charSlot], 0);
      this.loadingStage = 2;
    }
  }

  private void menuItemSort() {
    playMenuSound(2);
    final MenuEntries<Equipment> equipment = new MenuEntries<>();
    loadItemsAndEquipmentForDisplay(equipment, null, 1);
    equipment.sort();
    setInventoryFromDisplay(equipment, gameState_800babc8.equipment_1e8, equipment.size());
    this.loadingStage = 2;
  }

  @Override
  public InputPropagation pressedThisFrame(final InputAction inputAction) {
    if(super.pressedThisFrame(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage != 3) {
      return InputPropagation.PROPAGATE;
    }

    switch(inputAction) {
      case BUTTON_EAST -> {
        this.menuEscape();
        return InputPropagation.HANDLED;
      }

      case BUTTON_SOUTH -> {
        this.menuSelect();
        return InputPropagation.HANDLED;
      }

      case BUTTON_NORTH -> {
        this.menuItemSort();
        return InputPropagation.HANDLED;
      }

      case BUTTON_SHOULDER_LEFT_1 -> {
        this.menuNavigateTop();
        return InputPropagation.HANDLED;
      }

      case BUTTON_SHOULDER_LEFT_2 -> {
        this.menuNavigateBottom();
        return InputPropagation.HANDLED;
      }

      case BUTTON_SHOULDER_RIGHT_1 -> {
        this.menuNavigatePageUp();
        return InputPropagation.HANDLED;
      }

      case BUTTON_SHOULDER_RIGHT_2 -> {
        this.menuNavigatePageDown();
        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  public InputPropagation pressedWithRepeatPulse(final InputAction inputAction) {
    if(super.pressedWithRepeatPulse(inputAction) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage != 3) {
      return InputPropagation.PROPAGATE;
    }

    switch(inputAction) {
      case DPAD_UP, JOYSTICK_LEFT_BUTTON_UP -> {
        this.menuNavigateUp();
        return InputPropagation.HANDLED;
      }

      case DPAD_DOWN, JOYSTICK_LEFT_BUTTON_DOWN -> {
        this.menuNavigateDown();
        return InputPropagation.HANDLED;
      }

      case DPAD_LEFT, JOYSTICK_LEFT_BUTTON_LEFT -> {
        this.menuNavigateLeft();
        return InputPropagation.HANDLED;
      }

      case DPAD_RIGHT, JOYSTICK_LEFT_BUTTON_RIGHT -> {
        this.menuNavigateRight();
        return InputPropagation.HANDLED;
      }

      case BUTTON_SHOULDER_RIGHT_1 -> {
        this.menuNavigatePageUp();
        return InputPropagation.HANDLED;
      }

      case BUTTON_SHOULDER_RIGHT_2 -> {
        this.menuNavigatePageDown();
        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }
}
