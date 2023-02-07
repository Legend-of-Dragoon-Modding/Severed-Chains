package legend.game.inventory.screens;

import legend.core.Config;
import legend.core.MathHelper;
import legend.game.DabasManager;
import legend.game.types.DabasData100;
import legend.game.types.LodString;
import legend.game.types.MenuItemStruct04;
import legend.game.types.MessageBoxResult;
import legend.game.types.Renderable58;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static legend.core.GameEngine.MEMORY;
import static legend.game.SItem.AcquiredGold_8011cdd4;
import static legend.game.SItem.AcquiredItems_8011d050;
import static legend.game.SItem.DigDabas_8011d04c;
import static legend.game.SItem.Discard_8011d05c;
import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.NextDig_8011d064;
import static legend.game.SItem.SpecialItem_8011d054;
import static legend.game.SItem.Take_8011d058;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.dabasMenuGlyphs_80114228;
import static legend.game.SItem.equipment_8011972c;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.messageBox;
import static legend.game.SItem.messageBox_8011dc90;
import static legend.game.SItem.renderCentredText;
import static legend.game.SItem.renderEightDigitNumber;
import static legend.game.SItem.renderGlyphs;
import static legend.game.SItem.renderItemIcon;
import static legend.game.SItem.renderMenuItems;
import static legend.game.SItem.renderString;
import static legend.game.SItem.renderText;
import static legend.game.SItem.setMessageBoxText;
import static legend.game.Scus94491BpeSegment.free;
import static legend.game.Scus94491BpeSegment.loadDrgnBinFile;
import static legend.game.Scus94491BpeSegment.mallocTail;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment_8002.allocateRenderable;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.getItemIcon;
import static legend.game.Scus94491BpeSegment_8002.giveItem;
import static legend.game.Scus94491BpeSegment_8002.playSound;
import static legend.game.Scus94491BpeSegment_8002.unloadRenderable;
import static legend.game.Scus94491BpeSegment_8002.uploadRenderables;
import static legend.game.Scus94491BpeSegment_8003.bzero;
import static legend.game.Scus94491BpeSegment_800b.drgn0_6666FilePtr_800bdc3c;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.inventoryJoypadInput_800bdc44;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class DabasScreen extends MenuScreen {
  private int loadingStage;

  private DabasData100 dabasData_8011d7c0;
  private Renderable58 renderable1;
  private Renderable58 renderable2;
  private long dabasFilePtr_8011dd00;

  private int menuIndex;

  private int gold;
  private boolean hasItems;
  private int _8011e094;

  private final Runnable unload;

  private final List<MenuItemStruct04> menuItems = new ArrayList<>();
  private MenuItemStruct04 specialItem;

  public DabasScreen(final Runnable unload) {
    this.unload = unload;
  }

  @Override
  protected void render() {
    switch(this.loadingStage) {
      case 0 -> {
        this.dabasData_8011d7c0 = MEMORY.ref(4, mallocTail(0x100), DabasData100::new);
        bzero(this.dabasData_8011d7c0.getAddress(), 0x100);

        //TODO this is the Pocketstation minigame to upload to the memcard
        loadDrgnBinFile(0, 6668, 0, (address, size, unused) -> this.dabasFilePtr_8011dd00 = address, 0, 0x2);

        scriptStartEffect(2, 10);
        this.menuIndex = 0;

        deallocateRenderables(0xff);
        renderGlyphs(dabasMenuGlyphs_80114228, 0, 0);
        this.renderable1 = allocateUiElement(0x9f, 0x9f, 60, this.getDabasMenuY(0));
        FUN_80104b60(this.renderable1);
        this.renderDabasMenu(0);

        this._8011e094 = 0;
        this.gold = 0;
        this.hasItems = false;

        if(DabasManager.hasSave()) {
          this.loadingStage = 1;
        } else {
          this.loadingStage = 2;
        }
      }

      // Load save
      case 1 -> {
        this.renderDabasMenu(this.menuIndex);

        MEMORY.setBytes(this.dabasData_8011d7c0.getAddress(), DabasManager.loadSave(), 0x580, 0x80);

        final DabasData100 dabasData = this.dabasData_8011d7c0;

        for(int i = 0; i < 6; i++) {
          final int itemId = dabasData.items_14.get(i).get();

          if(itemId != 0) {
            final MenuItemStruct04 item = new MenuItemStruct04();
            item.itemId_00 = itemId;
            this.menuItems.add(item);
            this.hasItems = true;
          }
        }

        final int specialItemId = dabasData.specialItem_2c.get();
        if(specialItemId != 0) {
          this.hasItems = true;

          this.specialItem = new MenuItemStruct04();
          this.specialItem.itemId_00 = specialItemId;
        }

        this.gold = dabasData.gold_34.get();

        if(dabasData._3c.get() == 1) {
          this._8011e094 = dabasData._3c.get();
        }

        this.loadingStage = 2;
      }

      case 2 -> this.renderDabasMenu(this.menuIndex);

      case 3 -> {
        messageBox(messageBox_8011dc90);

        if(messageBox_8011dc90.ticks_10 < 3) {
          this.renderDabasMenu(this.menuIndex);
          break;
        }

        if(this.renderable2 == null) {
          this.renderable2 = allocateUiElement(0xd3, 0xd3, 68, 80);
          this.renderable2.z_3c = 31;
        }

        this.FUN_801073f8(112, 144, this.gold);
        this.FUN_80106d10(226, 144, gameState_800babc8.gold_94.get());

        if((inventoryJoypadInput_800bdc44.get() & 0x20) == 0) {
          this.renderDabasMenu(this.menuIndex);
          break;
        }

        unloadRenderable(this.renderable2);
        this.renderable2 = allocateUiElement(0xd3, 0xd9, 68, 80);
        this.renderable2.z_3c = 31;
        this.renderDabasMenu(this.menuIndex);
        this.loadingStage++;
      }

      case 4 -> {
        messageBox(messageBox_8011dc90);

        if(this.gold <= 10 || (inventoryJoypadInput_800bdc44.get() & 0x20) != 0) {
          gameState_800babc8.gold_94.add(this.gold);
          this.gold = 0;
          unloadRenderable(this.renderable2);
          this.renderable2 = allocateUiElement(0xd3, 0xd3, 68, 80);
          this.renderable2.z_3c = 31;
          this.loadingStage++;
        } else {
          this.gold -= 10;
          gameState_800babc8.gold_94.add(10);
        }

        if(gameState_800babc8.gold_94.get() > 99999999) {
          gameState_800babc8.gold_94.set(99999999);
        }

        if((tickCount_800bb0fc.get() & 0x1) != 0) {
          playSound(1);
        }

        this.FUN_801073f8(112, 144, this.gold);
        this.FUN_80106d10(226, 144, gameState_800babc8.gold_94.get());
        this.renderDabasMenu(this.menuIndex);
      }

      case 5 -> {
        messageBox(messageBox_8011dc90);
        if((inventoryJoypadInput_800bdc44.get() & 0x20) != 0) {
          unloadRenderable(this.renderable2);
          messageBox_8011dc90.state_0c++;
          this.loadingStage++;
        }

        this.FUN_801073f8(112, 144, this.gold);
        this.FUN_80106d10(226, 144, gameState_800babc8.gold_94.get());
        this.renderDabasMenu(this.menuIndex);
      }

      case 6 -> {
        this.renderDabasMenu(this.menuIndex);

        if(messageBox(messageBox_8011dc90) != MessageBoxResult.AWAITING_INPUT) {
          this.loadingStage = 2;
        }
      }

      case 100 -> {
        this.renderDabasMenu(this.menuIndex);
        free(this.dabasFilePtr_8011dd00);
        free(this.dabasData_8011d7c0.getAddress());
        this.unload.run();
      }
    }
  }

  private void takeItems() {
    final DabasData100 dabasData = this.dabasData_8011d7c0;
    dabasData.chapterIndex_00.set(gameState_800babc8.chapterIndex_98.get());

    int equipmentCount = 0;
    int itemCount = 0;
    dabasData.gold_34.set(0);

    for(final MenuItemStruct04 item : this.menuItems) {
      if(item != null) {
        if(item.itemId_00 < 0xc0) {
          equipmentCount++;
        } else {
          itemCount++;
        }
      }
    }

    if(this.specialItem != null) {
      equipmentCount++;
    }

    if(equipmentCount != 0 && gameState_800babc8.equipmentCount_1e4.get() + equipmentCount >= 0x100 || itemCount != 0 && gameState_800babc8.itemCount_1e6.get() + itemCount > Config.inventorySize()) {
      menuStack.pushScreen(new MessageBoxScreen(new LodString("Dabas has more items\nthan you can hold"), 0, result -> {}));
      return;
    }

    this.hasItems = false;

    for(final MenuItemStruct04 item : this.menuItems) {
      if(item != null) {
        giveItem(item.itemId_00);
      }
    }

    if(this.specialItem != null) {
      giveItem(this.specialItem.itemId_00);
    }

    this.menuItems.clear();
    this.specialItem = null;

    for(int i = 0; i < 6; i++) {
      dabasData.items_14.get(i).set(0);
    }

    dabasData.specialItem_2c.set(0);

    setMessageBoxText(messageBox_8011dc90, new LodString(TAKE_RESPONSES[ThreadLocalRandom.current().nextInt(TAKE_RESPONSES.length)]), 0x1);
    this.renderable2 = null;
    this.loadingStage = 3;
  }

  private void discardItems() {
    final DabasData100 dabasData = this.dabasData_8011d7c0;
    dabasData.chapterIndex_00.set(gameState_800babc8.chapterIndex_98.get());

    for(int i = 0; i < 6; i++) {
      dabasData.items_14.get(i).set(0);
    }

    this.hasItems = false;
    dabasData.specialItem_2c.set(0);

    this.menuItems.clear();
    this.specialItem = null;

    menuStack.pushScreen(new MessageBoxScreen(new LodString(DISCARD_RESPONSES[ThreadLocalRandom.current().nextInt(DISCARD_RESPONSES.length)]), 0, result -> this.loadingStage = 2));
  }

  private void newDig() {
    final DabasData100 dabasData = this.dabasData_8011d7c0;
    dabasData.chapterIndex_00.set(gameState_800babc8.chapterIndex_98.get());

    this.menuItems.clear();
    this.specialItem = null;

    dabasData._3c.set(2);
    dabasData.specialItem_2c.set(0);
    this._8011e094 = 0;

    menuStack.pushScreen(new MessageBoxScreen(new LodString(NEW_DIG_RESPONSES[ThreadLocalRandom.current().nextInt(NEW_DIG_RESPONSES.length)]), 0, result -> this.loadingStage = 2));
  }

  @Override
  protected void mouseMove(final int x, final int y) {
    if(this.loadingStage != 2) {
      return;
    }

    for(int i = 0; i < 3; i++) {
      if(this.menuIndex != i && MathHelper.inBox(x, y, 52, this.getDabasMenuY(i), 85, 14)) {
        playSound(1);
        this.menuIndex = i;
        this.renderable1.y_44 = this.getDabasMenuY(this.menuIndex);
      }
    }
  }

  @Override
  protected void mouseClick(final int x, final int y, final int button, final int mods) {
    if(this.loadingStage != 2 || mods != 0) {
      return;
    }

    if(button == GLFW_MOUSE_BUTTON_LEFT) {
      if(MathHelper.inBox(x, y, 52, this.getDabasMenuY(0), 85, 14)) {
        if(this.hasItems || this.gold != 0) {
          playSound(2);

          menuStack.pushScreen(new MessageBoxScreen(new LodString("Take items from Dabas?"), 2, result -> {
            if(result == MessageBoxResult.YES) {
              this.takeItems();
            }
          }));
        } else {
          playSound(40);
        }
      } else if(MathHelper.inBox(x, y, 52, this.getDabasMenuY(1), 85, 14)) {
        if(this.hasItems) {
          playSound(2);

          menuStack.pushScreen(new MessageBoxScreen(new LodString("Discard items?"), 2, result -> {
            if(result == MessageBoxResult.YES) {
              this.discardItems();
            }
          }));
        } else {
          playSound(40);
        }
      } else if(MathHelper.inBox(x, y, 52, this.getDabasMenuY(2), 85, 14)) {
        if(this._8011e094 != 0) {
          playSound(2);

          menuStack.pushScreen(new MessageBoxScreen(new LodString("Begin new expedition?"), 2, result -> {
            if(result == MessageBoxResult.YES) {
              this.newDig();
            }
          }));
        } else {
          playSound(40);
        }
      }
    }
  }

  @Override
  protected void keyPress(final int key, final int scancode, final int mods) {
    if(this.loadingStage != 2 || mods != 0) {
      return;
    }

    if(key == GLFW_KEY_ESCAPE) {
      playSound(3);
      this.loadingStage = 100;
    }
  }

  private void FUN_80106d10(final int x, final int y, final int value) {
    long sp10 = 0x2L;

    //LAB_80106d5c
    int s0 = value / 10000000 % 10;
    if(s0 != 0) {
      final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      renderable.flags_00 |= 0xc;
      renderable.glyph_04 = s0;
      renderable.tpage_2c = 0x19;
      renderable.clut_30 = 0;
      renderable.z_3c = 0x1f;
      renderable.x_40 = x;
      renderable.y_44 = y;
      sp10 |= 0x1L;
    }

    //LAB_80106e10
    s0 = value / 1000000 % 10;
    if(s0 != 0 || (sp10 & 0x1) != 0) {
      //LAB_80106e78
      final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      renderable.flags_00 |= 0xc;
      renderable.glyph_04 = s0;
      renderable.tpage_2c = 0x19;
      renderable.clut_30 = 0;
      renderable.z_3c = 0x1f;
      renderable.x_40 = x + 6;
      renderable.y_44 = y;
      sp10 |= 0x1L;
    }

    //LAB_80106ee8
    s0 = value / 100000 % 10;
    if(s0 != 0 || (sp10 & 0x1) != 0) {
      //LAB_80106f50
      final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      renderable.flags_00 |= 0xc;
      renderable.glyph_04 = s0;
      renderable.tpage_2c = 0x19;
      renderable.clut_30 = 0;
      renderable.z_3c = 0x1f;
      renderable.x_40 = x + 12;
      renderable.y_44 = y;
      sp10 |= 0x1L;
    }

    //LAB_80106fbc
    s0 = value / 10000 % 10;
    if(s0 != 0 || (sp10 & 0x1) != 0) {
      //LAB_80107024
      final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      renderable.flags_00 |= 0xc;
      renderable.glyph_04 = s0;
      renderable.tpage_2c = 0x19;
      renderable.clut_30 = 0;
      renderable.z_3c = 0x1f;
      renderable.x_40 = x + 18;
      renderable.y_44 = y;
      sp10 |= 0x1L;
    }

    //LAB_80107094
    s0 = value / 1000 % 10;
    if(s0 != 0 || (sp10 & 0x1) != 0) {
      //LAB_801070f8
      final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      renderable.flags_00 |= 0xc;
      renderable.glyph_04 = s0;
      renderable.tpage_2c = 0x19;
      renderable.clut_30 = 0;
      renderable.z_3c = 0x1f;
      renderable.x_40 = x + 24;
      renderable.y_44 = y;
      sp10 |= 0x1L;
    }

    //LAB_80107168
    s0 = value / 100 % 10;
    if(s0 != 0 || (sp10 & 0x1) != 0) {
      //LAB_801071cc
      final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      renderable.flags_00 |= 0xc;
      renderable.glyph_04 = s0;
      renderable.tpage_2c = 0x19;
      renderable.clut_30 = 0;
      renderable.z_3c = 0x1f;
      renderable.x_40 = x + 30;
      renderable.y_44 = y;
      sp10 |= 0x1L;
    }

    //LAB_80107238
    s0 = value / 10 % 10;
    if(s0 != 0 || (sp10 & 0x1) != 0) {
      //LAB_8010729c
      final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      renderable.flags_00 |= 0xc;
      renderable.glyph_04 = s0;
      renderable.tpage_2c = 0x19;
      renderable.clut_30 = 0;
      renderable.z_3c = 0x1f;
      renderable.x_40 = x + 36;
      renderable.y_44 = y;
    }

    //LAB_80107308
    //LAB_80107360
    final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
    renderable.flags_00 |= 0xc;
    renderable.glyph_04 = value % 10;
    renderable.tpage_2c = 0x19;
    renderable.clut_30 = 0;
    renderable.z_3c = 0x1f;
    renderable.x_40 = x + 42;
    renderable.y_44 = y;
  }

  private void FUN_801073f8(final int x, final int y, final int value) {
    long sp10 = 0x2L;

    //LAB_80107438
    int s0 = value / 1000 % 10;
    if(s0 != 0) {
      final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      renderable.flags_00 |= 0xc;
      renderable.glyph_04 = s0;
      renderable.tpage_2c = 0x19;
      renderable.x_40 = x;
      renderable.y_44 = y;
      renderable.clut_30 = 0;
      renderable.z_3c = 0x1f;
      sp10 |= 0x1L;
    }

    //LAB_801074ec
    s0 = value / 100 % 10;
    if(s0 != 0 || (sp10 & 0x1) != 0) {
      //LAB_80107554
      final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      renderable.flags_00 |= 0xc;
      renderable.glyph_04 = s0;
      renderable.tpage_2c = 0x19;
      renderable.x_40 = x + 6;
      renderable.y_44 = y;
      renderable.clut_30 = 0;
      renderable.z_3c = 0x1f;
      sp10 |= 0x1L;
    }

    //LAB_801075c0
    s0 = value / 10 % 10;
    if(s0 != 0 || (sp10 & 0x1) != 0) {
      //LAB_80107624
      final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
      renderable.flags_00 |= 0xc;
      renderable.glyph_04 = s0;
      renderable.tpage_2c = 0x19;
      renderable.x_40 = x + 12;
      renderable.y_44 = y;
      renderable.clut_30 = 0;
      renderable.z_3c = 0x1f;
    }

    //LAB_80107690
    final Renderable58 renderable = allocateRenderable(drgn0_6666FilePtr_800bdc3c.deref()._0000, null);
    renderable.flags_00 |= 0xc;
    renderable.glyph_04 = value % 10;
    renderable.tpage_2c = 0x19;
    renderable.x_40 = x + 18;
    renderable.y_44 = y;
    renderable.clut_30 = 0;
    renderable.z_3c = 0x1f;
  }

  private void renderDabasMenu(final int selectedSlot) {
    //LAB_801031cc
    renderText(DigDabas_8011d04c, 48, 28, 4);
    renderText(AcquiredItems_8011d050, 210, 28, 4);
    renderText(SpecialItem_8011d054, 210, 170, 4);
    renderText(AcquiredGold_8011cdd4, 30, 124, 4);
    renderCentredText(Take_8011d058, 94, this.getDabasMenuY(0) + 2, selectedSlot == 0 ? 5 : !this.hasItems && this.gold == 0 ? 6 : 4);
    renderCentredText(Discard_8011d05c, 94, this.getDabasMenuY(1) + 2, selectedSlot == 1 ? 5 : !this.hasItems ? 6 : 4);
    renderCentredText(NextDig_8011d064, 94, this.getDabasMenuY(2) + 2, selectedSlot == 2 ? 5 : this._8011e094 == 0 ? 6 : 4);
    renderMenuItems(194, 37, this.menuItems, 0, 6, null, null);
    renderEightDigitNumber(100, 147, this.gold, 0x2L);

    if(this.specialItem != null) {
      renderItemIcon(getItemIcon(this.specialItem.itemId_00), 198, 192, 0x8L);
      renderText(equipment_8011972c.get(this.specialItem.itemId_00).deref(), 214, 194, 4);
    }

    //LAB_80103390
    renderString(2, 16, 178, selectedSlot, false);
    uploadRenderables();
  }

  private int getDabasMenuY(final int slot) {
    return 57 + slot * 14;
  }

  private static final String[] TAKE_RESPONSES = {
    "Dabas thanks you.",
    "Dabas thanks you for\nhis hard work.",
    "Dabas pats himself\non the back.",
    "Dabas plays a happy song\non his accordion.",
    "Dabas wonders why he is\nthe one paying you.",
    "You thank Dabas even though\nthe items are dirty.",
  };

  private static final String[] DISCARD_RESPONSES = {
    "Dabas is disappointed.",
    "Dabas isn't angry, he's\njust disappointed.",
    "Dabas isn't angry. He swears.",
    "You discarded all of\nDabas' hard work.",
    "Dabas reconsiders his\nchoice of career.",
    "Dabas used his good\npickaxe for that.",
    "Dabas throws the items\ninto the ocean.",
    "Dabas reconsiders his\nchoice of friends.",
    "A lone tear rolls down\nDabas' cheek.",
    "Dabas plays a sad song\non his accordion.",
  };

  private static final String[] NEW_DIG_RESPONSES = {
    "Dabas bids you farewell.",
    "Dabas gives Dart an uncomfortable hug.",
    "Dabas gives Dart a comforting hug.",
    "Dabas gives Dart a comforting hug.\nHe was covered in dirt.",
    "Dabas gives Dart a comforting hug.\nShana is jealous.",
    "Dabas gives Rose a hug.\nShe punches him.",
    "Dabas gives Rose a hug.\nIt has been 1000 years.\nShe needed that.",
    "Dabas grabs his pickaxe.",
    "Dabas dons his helmet",
    "Dabas says goodbye.",
    "Dabas pulls out an accordion\nand plays himself off.",
    "Dabas walks off into the sunset.",
    "Dabas runs off into the sunset.",
    "Dabas leaves, mumbling about\na Dragoni Plant.",
  };
}
