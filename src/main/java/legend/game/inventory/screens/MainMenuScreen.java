package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.game.types.InventoryMenuState;

import static legend.game.SItem.FUN_800fc900;
import static legend.game.SItem.FUN_800fca0c;
import static legend.game.SItem.FUN_80102484;
import static legend.game.SItem.FUN_80103b10;
import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.canSave_8011dc88;
import static legend.game.SItem.getItemSubmenuOptionY;
import static legend.game.SItem.getMenuOptionY;
import static legend.game.SItem.glyphs_80114130;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.messageBox_8011dc90;
import static legend.game.SItem.renderGlyphs;
import static legend.game.SItem.renderInventoryMenu;
import static legend.game.SItem.renderItemSubmenu;
import static legend.game.SItem.selectedItemSubmenuOption_8011d73c;
import static legend.game.SItem.selectedMenuOption_8011d738;
import static legend.game.SItem.setMessageBoxText;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_8002.recalcInventory;
import static legend.game.Scus94491BpeSegment_800b._800bb168;
import static legend.game.Scus94491BpeSegment_800b.inventoryMenuState_800bdc28;
import static legend.game.Scus94491BpeSegment_800b.selectedMenuOptionRenderablePtr_800bdbe0;
import static legend.game.Scus94491BpeSegment_800b.selectedMenuOptionRenderablePtr_800bdbe4;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

public class MainMenuScreen extends MenuScreen {
  private int loadingStage;
  private final Runnable unload;

  public MainMenuScreen(final Runnable unload) {
    this.unload = unload;
  }

  @Override
  protected void render() {
    switch(this.loadingStage) {
      case 0 -> {
        deallocateRenderables(0xff);
        recalcInventory();
        FUN_80103b10();
        scriptStartEffect(2, 10);

        renderGlyphs(glyphs_80114130, 0, 0);
        selectedMenuOptionRenderablePtr_800bdbe0 = allocateUiElement(115, 115, 29, getMenuOptionY(selectedMenuOption_8011d738.get()));
        selectedMenuOptionRenderablePtr_800bdbe4 = FUN_800fc900(selectedItemSubmenuOption_8011d73c.get());
        FUN_80104b60(selectedMenuOptionRenderablePtr_800bdbe0);
        renderInventoryMenu(selectedMenuOption_8011d738.get(), 4, 0xff);
        this.loadingStage++;
      }

      case 1 -> {
        FUN_80102484(0);
        renderItemSubmenu(selectedItemSubmenuOption_8011d73c.get(), 4);
        renderInventoryMenu(selectedMenuOption_8011d738.get(), 4, 0);
      }

      // Fade out
      case 100 -> {
        renderInventoryMenu(selectedMenuOption_8011d738.get(), 4, 0);
        scriptStartEffect(1, 10);
        this.loadingStage++;
      }

      // Unload
      case 101 -> {
        renderInventoryMenu(selectedMenuOption_8011d738.get(), 4, 0);

        if(_800bb168.get() >= 0xff) {
          this.unload.run();
        }
      }
    }
  }

  @Override
  protected void mouseMove(final int x, final int y) {
    if(this.loadingStage == 1) {
      for(int i = 0; i < 6; i++) {
        if(selectedMenuOption_8011d738.get() != i && MathHelper.inBox(x, y, 22, getMenuOptionY(i) + 2, 84, 13)) {
          playSound(1);
          selectedMenuOption_8011d738.set(i);
          selectedMenuOptionRenderablePtr_800bdbe0.y_44 = getMenuOptionY(i);
        }
      }

      for(int i = 0; i < 4; i++) {
        if(selectedItemSubmenuOption_8011d73c.get() != i && MathHelper.inBox(x, y, 114, getItemSubmenuOptionY(i) + 2, 55, 13)) {
          playSound(1);
          selectedItemSubmenuOption_8011d73c.set(i);
          selectedMenuOptionRenderablePtr_800bdbe4.y_44 = getItemSubmenuOptionY(i) - 2;
        }
      }
    }
  }

  @Override
  protected void mouseClick(final int x, final int y, final int button, final int mods) {
    if(this.loadingStage == 1) {
      for(int i = 0; i < 6; i++) {
        if(MathHelper.inBox(x, y, 22, getMenuOptionY(i) + 2, 84, 13)) {
          selectedMenuOption_8011d738.set(i);
          selectedMenuOptionRenderablePtr_800bdbe0.y_44 = getMenuOptionY(i);

          switch(i) {
            case 0 -> {
              playSound(2);

              menuStack.pushScreen(new StatusScreen(() -> {
                menuStack.popScreen();
                this.loadingStage = 0;
              }));
            }

            case 1 -> {
              playSound(2);

              menuStack.pushScreen(new EquipmentScreen(() -> {
                menuStack.popScreen();
                this.loadingStage = 0;
              }));
            }

            case 2 -> {
              playSound(2);

              menuStack.pushScreen(new AdditionsScreen(() -> {
                menuStack.popScreen();
                this.loadingStage = 0;
              }));
            }

            case 3 -> {
              playSound(2);

              menuStack.pushScreen(new CharSwapScreen(() -> {
                menuStack.popScreen();
                this.loadingStage = 0;
              }));
            }

            case 4 -> {
              playSound(4);
              selectedItemSubmenuOption_8011d73c.set(0);
              selectedMenuOptionRenderablePtr_800bdbe4 = null;
              setMessageBoxText(messageBox_8011dc90, null, 0x1);
              inventoryMenuState_800bdc28.set(InventoryMenuState.CONFIG_6);
            }

            case 5 -> {
              if(canSave_8011dc88.get() != 0) {
                playSound(2);
                FUN_800fca0c(InventoryMenuState.INIT_LOAD_GAME_37, 0x1L);
              } else {
                playSound(40);
              }
            }
          }
        }
      }

      for(int i = 0; i < 4; i++) {
        if(MathHelper.inBox(x, y, 114, getItemSubmenuOptionY(i) + 2, 55, 13)) {
          selectedItemSubmenuOption_8011d73c.set(i);
          selectedMenuOptionRenderablePtr_800bdbe4.y_44 = getItemSubmenuOptionY(i) - 2;

          if(i == 0) {
            FUN_800fca0c(InventoryMenuState.USE_ITEM_MENU_INIT_26, 2);
          } else if(i == 1) {
            FUN_800fca0c(InventoryMenuState.DISCARD_INIT_31, 2);
          } else if(i == 2) {
            FUN_800fca0c(InventoryMenuState.GOODS_INIT_35, 2);
          } else {
            FUN_800fca0c(InventoryMenuState.DABAS_INIT_72, 2);
          }
        }
      }
    }
  }

  @Override
  protected void keyPress(final int key, final int scancode, final int mods) {
    if(this.loadingStage == 1) {
      if(key == GLFW_KEY_ESCAPE) {
        playSound(3);
        this.loadingStage = 100;
      }
    }
  }
}
