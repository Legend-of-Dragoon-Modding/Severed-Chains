package legend.game.inventory.screens;

import legend.core.Config;
import legend.core.MathHelper;
import legend.game.types.MenuItemStruct04;
import legend.game.types.Renderable58;

import java.util.Arrays;

import static legend.game.SItem.FUN_800fc824;
import static legend.game.SItem.FUN_801034cc;
import static legend.game.SItem.FUN_80104738;
import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.canEquip;
import static legend.game.SItem.characterCount_8011d7c4;
import static legend.game.SItem.equipItem;
import static legend.game.SItem.equipmentGlyphs_80114180;
import static legend.game.SItem.getEquipmentSlot;
import static legend.game.SItem.loadCharacterStats;
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
import static legend.game.Scus94491BpeSegment_8002.recalcInventory;
import static legend.game.Scus94491BpeSegment_8002.sortItems;
import static legend.game.Scus94491BpeSegment_8002.takeEquipment;
import static legend.game.Scus94491BpeSegment_8002.uploadRenderables;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;

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

  private final MenuItemStruct04[] equipment = new MenuItemStruct04[304];
  private final MenuItemStruct04[] items = new MenuItemStruct04[Config.inventorySize()];
  private final MenuItemStruct04[] menuItems = new MenuItemStruct04[256];

  public EquipmentScreen(final Runnable unload) {
    this.unload = unload;
    Arrays.setAll(this.equipment, i -> new MenuItemStruct04());
    Arrays.setAll(this.items, i -> new MenuItemStruct04());
    Arrays.setAll(this.menuItems, i -> new MenuItemStruct04());
  }

  @Override
  protected void render() {
    switch(this.loadingStage) {
      case 0:
        recalcInventory();
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
    for(int i = 0; i < 256; i++) {
      this.menuItems[i].itemId_00 = 0xff;
    }

    int equippable = 0;
    for(int equipmentSlot = 0; equipmentSlot < gameState_800babc8.equipmentCount_1e4.get(); equipmentSlot++) {
      final int equipmentId = gameState_800babc8.equipment_1e8.get(equipmentSlot).get();
      if(canEquip(equipmentId, charIndex)) {
        final int equipmentSlot2 = getEquipmentSlot(equipmentId);

        if(equipmentSlot2 != 0xff) {
          if(equipmentId != gameState_800babc8.charData_32c.get(charIndex).equipment_14.get(equipmentSlot2).get()) {
            final MenuItemStruct04 s2 = this.menuItems[equippable];
            s2.itemId_00 = equipmentId;
            s2.itemSlot_01 = equipmentSlot;
            s2.price_02 = 0;
            equippable++;
          }
        }
      }
    }

    return equippable;
  }

  private void FUN_80102660(final int charSlot, final int slotIndex, final int slotScroll, final long a3) {
    final boolean allocate = a3 == 0xff;

    renderCharacterSlot(16, 21, characterIndices_800bdbb8.get(charSlot).get(), allocate, false);
    renderCharacterStats(characterIndices_800bdbb8.get(charSlot).get(), this.menuItems[slotIndex + slotScroll].itemId_00, allocate);
    renderCharacterEquipment(characterIndices_800bdbb8.get(charSlot).get(), allocate);

    if(allocate) {
      allocateUiElement(0x5a, 0x5a, 194, 96);
      this._800bdb9c = allocateUiElement(0x3d, 0x44, 358, this.FUN_800fc804(0));
      this._800bdba0 = allocateUiElement(0x35, 0x3c, 358, this.FUN_800fc804(3));
    }

    renderMenuItems(194, 92, this.menuItems, slotScroll, 4, this._800bdb9c, this._800bdba0);
    renderString(0, 194, 178, this.menuItems[slotIndex + slotScroll].itemId_00, allocate);

    uploadRenderables();
  }

  private int FUN_800fc804(final int slot) {
    return 99 + slot * 17;
  }

  @Override
  protected void mouseMove(final int x, final int y) {
    if(this.loadingStage != 3) {
      return;
    }

    for(int slot = 0; slot < Math.min(4, this.equipmentCount - this.slotScroll); slot++) {
      if(this.selectedSlot != slot && MathHelper.inBox(x, y, 212, this.FUN_800fc804(slot), 139, 15)) {
        playSound(1);
        this.selectedSlot = slot;
        this.itemHighlight.y_44 = this.FUN_800fc804(slot);
      }
    }
  }

  @Override
  protected void mouseClick(final int x, final int y, final int button, final int mods) {
    if(this.loadingStage != 3) {
      return;
    }

    for(int slot = 0; slot < Math.min(4, this.equipmentCount - this.slotScroll); slot++) {
      if(MathHelper.inBox(x, y, 212, this.FUN_800fc804(slot), 139, 15)) {
        this.selectedSlot = slot;
        this.itemHighlight.y_44 = this.FUN_800fc804(slot);

        final int itemIndex = this.selectedSlot + this.slotScroll;
        if(itemIndex < gameState_800babc8.equipmentCount_1e4.get()) {
          final int equipmentId = this.menuItems[itemIndex].itemId_00;
          if(equipmentId != 0xff) {
            final int previousEquipmentId = equipItem(equipmentId, characterIndices_800bdbb8.get(this.charSlot).get());
            takeEquipment(this.menuItems[itemIndex].itemSlot_01);
            giveItem(previousEquipmentId);
            playSound(2);
            loadCharacterStats(0);
            addHp(characterIndices_800bdbb8.get(this.charSlot).get(), 0);
            addMp(characterIndices_800bdbb8.get(this.charSlot).get(), 0);
            this.loadingStage = 2;
          } else {
            playSound(40);
          }
        }
      }
    }
  }

  @Override
  protected void mouseScroll(final double deltaX, final double deltaY) {
    if(this.loadingStage != 3) {
      return;
    }

    if(this.scrollAccumulator < 0 && deltaY > 0 || this.scrollAccumulator > 0 && deltaY < 0) {
      this.scrollAccumulator = 0;
    }

    this.scrollAccumulator += deltaY;
  }

  @Override
  protected void keyPress(final int key, final int scancode, final int mods) {
    if(this.loadingStage != 3) {
      return;
    }

    // Exit menu
    if(key == GLFW_KEY_ESCAPE) {
      playSound(3);
      this.loadingStage = 100;
    }

    if(key == GLFW_KEY_LEFT && this.charSlot > 0) {
      this.charSlot--;
      this.loadingStage = 1;
    }

    if(key == GLFW_KEY_RIGHT && this.charSlot < characterCount_8011d7c4.get() - 1) {
      this.charSlot++;
      this.loadingStage = 1;
    }

    // Sort items
    if(key == GLFW_KEY_S) {
      playSound(2);
      FUN_80104738(this.equipment, this.items, 1);
      sortItems(this.equipment, gameState_800babc8.equipment_1e8, gameState_800babc8.equipmentCount_1e4.get());
      this.loadingStage = 2;
    }
  }
}
