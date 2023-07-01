package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.game.input.InputAction;
import legend.game.types.MenuItemStruct04;
import legend.game.types.Renderable58;

import java.util.ArrayList;
import java.util.List;

import static legend.game.SItem.FUN_800fc824;
import static legend.game.SItem.FUN_801034cc;
import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.canEquip;
import static legend.game.SItem.characterCount_8011d7c4;
import static legend.game.SItem.equipItem;
import static legend.game.SItem.equipmentGlyphs_80114180;
import static legend.game.SItem.getEquipmentSlot;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.SItem.loadItemsAndEquipmentForDisplay;
import static legend.game.SItem.renderCharacterEquipment;
import static legend.game.SItem.renderCharacterSlot;
import static legend.game.SItem.renderCharacterStats;
import static legend.game.SItem.renderGlyphs;
import static legend.game.SItem.renderMenuItems;
import static legend.game.SItem.renderString;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment_8002.addHp;
import static legend.game.Scus94491BpeSegment_8002.addMp;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.giveItem;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_8002.sortItems;
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

  private final List<MenuItemStruct04> equipment = new ArrayList<>();
  private final List<MenuItemStruct04> items = new ArrayList<>();
  private final List<MenuItemStruct04> menuItems = new ArrayList<>();

  public EquipmentScreen(final Runnable unload) {
    this.unload = unload;
  }

  @Override
  protected void render() {
    switch(this.loadingStage) {
      case 0:
        scriptStartEffect(2, 10);
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
          this.itemHighlight = allocateUiElement(0x79, 0x79, FUN_800fc824(1), 0);
          FUN_80104b60(this.itemHighlight);
        }

        this.itemHighlight.y_44 = this.FUN_800fc804(this.selectedSlot);
        this.equipmentCount = this.getEquippableItemsForCharacter(characterIndices_800bdbb8.get(this.charSlot).get());
        this.FUN_80102660(this.charSlot, this.selectedSlot, this.slotScroll, 0xff);
        this.loadingStage++;
        break;

      case 3:
        FUN_801034cc(this.charSlot, characterCount_8011d7c4.get());
        this.FUN_80102660(this.charSlot, this.selectedSlot, this.slotScroll, 0);

        if(this.scrollAccumulator >= 1.0d) {
          this.scrollAccumulator -= 1.0d;

          if(this.slotScroll > 0) {
            this.scroll(this.slotScroll - 1);
          }
        }

        if(this.scrollAccumulator <= -1.0d) {
          this.scrollAccumulator += 1.0d;

          if(this.slotScroll < this.equipmentCount - 4) {
            this.scroll(this.slotScroll + 1);
          }
        }

        break;

      // Fade out
      case 100:
        this.FUN_80102660(this.charSlot, this.selectedSlot, this.slotScroll, 0);
        this.unload.run();
        break;
    }
  }

  private void scroll(final int scroll) {
    playSound(1);
    this.slotScroll = scroll;
    this.itemHighlight.y_44 = this.FUN_800fc804(this.selectedSlot);
  }

  private int getEquippableItemsForCharacter(final int charIndex) {
    this.menuItems.clear();

    for(int equipmentSlot = 0; equipmentSlot < gameState_800babc8.equipment_1e8.size(); equipmentSlot++) {
      final int equipmentId = gameState_800babc8.equipment_1e8.getInt(equipmentSlot);
      if(canEquip(equipmentId, charIndex)) {
        final int equipmentSlot2 = getEquipmentSlot(equipmentId);

        if(equipmentSlot2 != 0xff) {
          if(equipmentId != gameState_800babc8.charData_32c[charIndex].equipment_14[equipmentSlot2]) {
            final MenuItemStruct04 item = new MenuItemStruct04();
            item.itemId_00 = equipmentId;
            item.itemSlot_01 = equipmentSlot;
            this.menuItems.add(item);
          }
        }
      }
    }

    return this.menuItems.size();
  }

  private void FUN_80102660(final int charSlot, final int slotIndex, final int slotScroll, final long a3) {
    final boolean allocate = a3 == 0xff;

    renderCharacterSlot(16, 21, characterIndices_800bdbb8.get(charSlot).get(), allocate, false);
    renderCharacterStats(characterIndices_800bdbb8.get(charSlot).get(), slotIndex + slotScroll >= this.menuItems.size() ? 0xff : this.menuItems.get(slotIndex + slotScroll).itemId_00, allocate);
    renderCharacterEquipment(characterIndices_800bdbb8.get(charSlot).get(), allocate);

    if(allocate) {
      allocateUiElement(90, 0x5a, 194, 96);
      this._800bdb9c = allocateUiElement(61, 0x44, 358, this.FUN_800fc804(0));
      this._800bdba0 = allocateUiElement(53, 0x3c, 358, this.FUN_800fc804(3));
    }

    renderMenuItems(194, 92, this.menuItems, slotScroll, 4, this._800bdb9c, this._800bdba0);

    if(slotIndex + slotScroll < this.menuItems.size()) {
      renderString(0, 194, 178, this.menuItems.get(slotIndex + slotScroll).itemId_00, allocate);
    }
  }

  private int FUN_800fc804(final int slot) {
    return 99 + slot * 17;
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
      if(this.selectedSlot != slot && MathHelper.inBox(x, y, 212, this.FUN_800fc804(slot), 139, 15)) {
        playSound(1);
        this.selectedSlot = slot;
        this.itemHighlight.y_44 = this.FUN_800fc804(slot);
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
      if(MathHelper.inBox(x, y, 212, this.FUN_800fc804(slot), 139, 15)) {
        this.selectedSlot = slot;
        this.itemHighlight.y_44 = this.FUN_800fc804(slot);

        final int itemIndex = this.selectedSlot + this.slotScroll;
        if(itemIndex < this.menuItems.size()) {
          final int equipmentId = this.menuItems.get(itemIndex).itemId_00;
          if(equipmentId != 0xff) {
            final int previousEquipmentId = equipItem(equipmentId, characterIndices_800bdbb8.get(this.charSlot).get());
            takeEquipment(this.menuItems.get(itemIndex).itemSlot_01);
            giveItem(previousEquipmentId);
            playSound(2);
            loadCharacterStats();
            addHp(characterIndices_800bdbb8.get(this.charSlot).get(), 0);
            addMp(characterIndices_800bdbb8.get(this.charSlot).get(), 0);
            this.loadingStage = 2;
          } else {
            playSound(40);
          }
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
    playSound(3);
    this.loadingStage = 100;
  }

  private void menuNavigateUp() {
    playSound(1);

    if(this.selectedSlot > 0) {
      this.selectedSlot--;
    } else {
      this.scrollAccumulator = 1;
    }

    this.itemHighlight.y_44 = this.FUN_800fc804(this.selectedSlot);
  }

  private void menuNavigateDown() {
    playSound(1);

    if(this.selectedSlot < 3) {
      this.selectedSlot++;
    } else {
      this.scrollAccumulator = -1;
    }

    this.itemHighlight.y_44 = this.FUN_800fc804(this.selectedSlot);
  }

  private void menuNavigateLeft() {
    if(this.charSlot > 0) {
      this.charSlot--;
      this.loadingStage = 1;
    }
  }

  private void menuNavigateRight() {
    if(this.charSlot < characterCount_8011d7c4.get() - 1) {
      this.charSlot++;
      this.loadingStage = 1;
    }
  }

  private void menuSelect() {
    playSound(2);

    final int itemIndex = this.selectedSlot + this.slotScroll;

    if(itemIndex < this.menuItems.size()) {
      final int equipmentId = this.menuItems.get(itemIndex).itemId_00;

      if(equipmentId != 0xff) {
        final int previousEquipmentId = equipItem(equipmentId, characterIndices_800bdbb8.get(this.charSlot).get());
        takeEquipment(this.menuItems.get(itemIndex).itemSlot_01);
        giveItem(previousEquipmentId);
        playSound(2);
        loadCharacterStats();
        addHp(characterIndices_800bdbb8.get(this.charSlot).get(), 0);
        addMp(characterIndices_800bdbb8.get(this.charSlot).get(), 0);
        this.loadingStage = 2;
      } else {
        playSound(40);
      }
    }
  }

  private void menuItemSort() {
    playSound(2);
    loadItemsAndEquipmentForDisplay(this.equipment, this.items, 1);
    sortItems(this.equipment, gameState_800babc8.equipment_1e8, gameState_800babc8.equipment_1e8.size());
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
    } else if(inputAction == InputAction.BUTTON_NORTH) {
      this.menuItemSort();
      return InputPropagation.HANDLED;
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
