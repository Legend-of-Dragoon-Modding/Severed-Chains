package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputMod;
import legend.game.DabasManager;
import legend.game.i18n.I18n;
import legend.game.inventory.Equipment;
import legend.game.inventory.InventoryEntry;
import legend.game.inventory.Item;
import legend.game.modding.coremod.CoreMod;
import legend.game.types.DabasData100;
import legend.game.types.MenuEntries;
import legend.game.types.MenuEntryStruct04;
import legend.game.types.MessageBox20;
import legend.game.types.MessageBoxResult;
import legend.game.types.Renderable58;
import legend.game.unpacker.FileData;
import legend.lodmod.LodMod;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.PLATFORM;
import static legend.core.GameEngine.REGISTRIES;
import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.UI_TEXT;
import static legend.game.SItem.UI_TEXT_CENTERED;
import static legend.game.SItem.UI_TEXT_DISABLED_CENTERED;
import static legend.game.SItem.UI_TEXT_SELECTED_CENTERED;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.dabasMenuGlyphs_80114228;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.messageBox;
import static legend.game.SItem.renderEightDigitNumber;
import static legend.game.SItem.renderGlyphs;
import static legend.game.SItem.renderItemIcon;
import static legend.game.SItem.renderMenuItems;
import static legend.game.SItem.renderString;
import static legend.game.SItem.setMessageBoxText;
import static legend.game.Scus94491BpeSegment.loadDrgnFile;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.allocateRenderable;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.giveEquipment;
import static legend.game.Scus94491BpeSegment_8002.giveItem;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_8002.unloadRenderable;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.Scus94491BpeSegment_800b.uiFile_800bdc3c;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;

public class DabasScreen extends MenuScreen {
  private static final String DigDabas_8011d04c = "Diiig Dabas!";
  private static final String AcquiredGold_8011cdd4 = "Acquired Gold";
  private static final String AcquiredItems_8011d050 = "Acquired Items";
  private static final String SpecialItem_8011d054 = "Special Item";
  private static final String Take_8011d058 = "Take";
  private static final String Discard_8011d05c = "Discard";
  private static final String NextDig_8011d064 = "Next Dig";

  private int loadingStage;

  private DabasData100 dabasData_8011d7c0;
  private Renderable58 renderable1;
  private Renderable58 renderable2;
  private FileData dabasFilePtr_8011dd00;

  private final MessageBox20 messageBox_8011dc90 = new MessageBox20();

  private int menuIndex;

  private int gold;
  private boolean hasItems;
  private boolean newDigEnabled;

  private final Runnable unload;

  private final MenuEntries<InventoryEntry> menuItems = new MenuEntries<>();
  private MenuEntryStruct04<Equipment> specialItem;

  public DabasScreen(final Runnable unload) {
    this.unload = unload;
  }

  @Override
  protected void render() {
    switch(this.loadingStage) {
      case 0 -> {
        //TODO this is the Pocketstation minigame to upload to the memcard
        loadDrgnFile(0, 6668, file -> this.dabasFilePtr_8011dd00 = file);

        startFadeEffect(2, 10);
        this.menuIndex = 0;

        deallocateRenderables(0xff);
        renderGlyphs(dabasMenuGlyphs_80114228, 0, 0);
        this.renderable1 = allocateUiElement(0x9f, 0x9f, 60, this.getDabasMenuY(0));
        FUN_80104b60(this.renderable1);
        this.renderDabasMenu(0);

        this.newDigEnabled = false;
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

        final DabasData100 dabasData = new DabasData100(DabasManager.loadSave());
        this.dabasData_8011d7c0 = dabasData;

        for(int i = 0; i < 6; i++) {
          final int itemId = dabasData.items_14[i];

          if(itemId != 0) {
            if(itemId > 192) {
              this.menuItems.add(MenuEntryStruct04.make(REGISTRIES.items.getEntry(LodMod.id(LodMod.ITEM_IDS[itemId - 192])).get()));
            } else {
              this.menuItems.add(MenuEntryStruct04.make(REGISTRIES.equipment.getEntry(LodMod.id(LodMod.EQUIPMENT_IDS[itemId])).get()));
            }

            this.hasItems = true;
          }
        }

        final int specialItemId = dabasData.specialItem_2c;
        if(specialItemId != 0) {
          this.specialItem = MenuEntryStruct04.make(REGISTRIES.equipment.getEntry(LodMod.id(LodMod.EQUIPMENT_IDS[specialItemId])).get());
          this.hasItems = true;
        }

        this.gold = dabasData.gold_34;

        if(dabasData._3c == 1) {
          this.newDigEnabled = true;
        }

        this.loadingStage = 2;
      }

      case 2 -> this.renderDabasMenu(this.menuIndex);

      case 3 -> {
        messageBox(this.messageBox_8011dc90);

        if(this.messageBox_8011dc90.ticks_10 < 3) {
          this.renderDabasMenu(this.menuIndex);
          break;
        }

        if(this.renderable2 == null) {
          this.renderable2 = allocateUiElement(0xd3, 0xd3, 68, 80);
          this.renderable2.z_3c = 31;
        }

        this.FUN_801073f8(112, 144, this.gold);
        this.FUN_80106d10(226, 144, gameState_800babc8.gold_94);

        if(!PLATFORM.isActionPressed(INPUT_ACTION_MENU_CONFIRM.get())) {
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
        messageBox(this.messageBox_8011dc90);

        if(this.gold <= 10 || PLATFORM.isActionPressed(INPUT_ACTION_MENU_CONFIRM.get())) {
          gameState_800babc8.gold_94 += this.gold;
          this.gold = 0;
          unloadRenderable(this.renderable2);
          this.renderable2 = allocateUiElement(0xd3, 0xd3, 68, 80);
          this.renderable2.z_3c = 31;
          this.loadingStage++;
        } else {
          this.gold -= 10;
          gameState_800babc8.gold_94 += 10;
        }

        if(gameState_800babc8.gold_94 > 99999999) {
          gameState_800babc8.gold_94 = 99999999;
        }

        if((tickCount_800bb0fc & 0x1) != 0) {
          playMenuSound(1);
        }

        this.FUN_801073f8(112, 144, this.gold);
        this.FUN_80106d10(226, 144, gameState_800babc8.gold_94);
        this.renderDabasMenu(this.menuIndex);
      }

      case 5 -> {
        messageBox(this.messageBox_8011dc90);
        if(PLATFORM.isActionPressed(INPUT_ACTION_MENU_CONFIRM.get())) {
          unloadRenderable(this.renderable2);
          this.messageBox_8011dc90.state_0c++;
          this.loadingStage++;
        }

        this.FUN_801073f8(112, 144, this.gold);
        this.FUN_80106d10(226, 144, gameState_800babc8.gold_94);
        this.renderDabasMenu(this.menuIndex);
      }

      case 6 -> {
        this.renderDabasMenu(this.menuIndex);

        if(messageBox(this.messageBox_8011dc90) != MessageBoxResult.AWAITING_INPUT) {
          this.loadingStage = 2;
        }
      }

      case 100 -> {
        this.renderDabasMenu(this.menuIndex);
        this.dabasData_8011d7c0 = null;
        this.unload.run();
      }
    }
  }

  private void takeItems() {
    final DabasData100 dabasData = this.dabasData_8011d7c0;
    dabasData.chapterIndex_00 = gameState_800babc8.chapterIndex_98;

    int equipmentCount = 0;
    int itemCount = 0;
    dabasData.gold_34 = 0;

    for(final MenuEntryStruct04<? extends InventoryEntry> item : this.menuItems) {
      if(item != null) {
        if(item.item_00 instanceof Equipment) {
          equipmentCount++;
        } else {
          itemCount++;
        }
      }
    }

    if(this.specialItem != null) {
      equipmentCount++;
    }

    if(equipmentCount != 0 && gameState_800babc8.equipment_1e8.size() + equipmentCount >= 0x100 || itemCount != 0 && gameState_800babc8.items_2e9.size() + itemCount > CONFIG.getConfig(CoreMod.INVENTORY_SIZE_CONFIG.get())) {
      menuStack.pushScreen(new MessageBoxScreen("Dabas has more items\nthan you can hold", 0, result -> {}));
      return;
    }

    this.hasItems = false;

    for(final MenuEntryStruct04<? extends InventoryEntry> entry : this.menuItems) {
      if(entry.item_00 instanceof final Item item) {
        giveItem(item);
      } else if(entry.item_00 instanceof final Equipment equipment) {
        giveEquipment(equipment);
      }
    }

    if(this.specialItem != null) {
      giveEquipment(this.specialItem.item_00);
    }

    this.menuItems.clear();
    this.specialItem = null;

    for(int i = 0; i < 6; i++) {
      dabasData.items_14[i] = 0;
    }

    dabasData.specialItem_2c = 0;

    setMessageBoxText(this.messageBox_8011dc90, TAKE_RESPONSES[ThreadLocalRandom.current().nextInt(TAKE_RESPONSES.length)], 0x1);
    this.renderable2 = null;
    this.loadingStage = 3;
  }

  private void discardItems() {
    final DabasData100 dabasData = this.dabasData_8011d7c0;
    dabasData.chapterIndex_00 = gameState_800babc8.chapterIndex_98;

    for(int i = 0; i < 6; i++) {
      dabasData.items_14[i] = 0;
    }

    this.hasItems = false;
    dabasData.specialItem_2c = 0;

    this.menuItems.clear();
    this.specialItem = null;

    menuStack.pushScreen(new MessageBoxScreen(DISCARD_RESPONSES[ThreadLocalRandom.current().nextInt(DISCARD_RESPONSES.length)], 0, result -> this.loadingStage = 2));
  }

  private void newDig() {
    final DabasData100 dabasData = this.dabasData_8011d7c0;
    dabasData.chapterIndex_00 = gameState_800babc8.chapterIndex_98;

    this.menuItems.clear();
    this.specialItem = null;

    dabasData._3c = 2;
    dabasData.specialItem_2c = 0;
    this.newDigEnabled = false;

    menuStack.pushScreen(new MessageBoxScreen(NEW_DIG_RESPONSES[ThreadLocalRandom.current().nextInt(NEW_DIG_RESPONSES.length)], 0, result -> this.loadingStage = 2));
  }

  @Override
  protected InputPropagation mouseMove(final int x, final int y) {
    if(super.mouseMove(x, y) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage != 2) {
      return InputPropagation.PROPAGATE;
    }

    for(int i = 0; i < 3; i++) {
      if(this.menuIndex != i && MathHelper.inBox(x, y, 52, this.getDabasMenuY(i), 85, 14)) {
        playMenuSound(1);
        this.menuIndex = i;
        this.renderable1.y_44 = this.getDabasMenuY(this.menuIndex);
        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation mouseClick(final int x, final int y, final int button, final Set<InputMod> mods) {
    if(super.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage != 2 || !mods.isEmpty()) {
      return InputPropagation.PROPAGATE;
    }

    if(button == PLATFORM.getMouseButton(0)) {
      if(MathHelper.inBox(x, y, 52, this.getDabasMenuY(0), 85, 14)) {
        if(this.hasItems || this.gold != 0) {
          playMenuSound(2);

          menuStack.pushScreen(new MessageBoxScreen("Take items from Dabas?", 2, result -> {
            if(result == MessageBoxResult.YES) {
              this.takeItems();
            }
          }));
        } else {
          playMenuSound(40);
        }

        return InputPropagation.HANDLED;
      } else if(MathHelper.inBox(x, y, 52, this.getDabasMenuY(1), 85, 14)) {
        if(this.hasItems) {
          playMenuSound(2);

          menuStack.pushScreen(new MessageBoxScreen("Discard items?", 2, result -> {
            if(result == MessageBoxResult.YES) {
              this.discardItems();
            }
          }));
        } else {
          playMenuSound(40);
        }

        return InputPropagation.HANDLED;
      } else if(MathHelper.inBox(x, y, 52, this.getDabasMenuY(2), 85, 14)) {
        if(this.newDigEnabled) {
          playMenuSound(2);

          menuStack.pushScreen(new MessageBoxScreen("Begin new expedition?", 2, result -> {
            if(result == MessageBoxResult.YES) {
              this.newDig();
            }
          }));
        } else {
          playMenuSound(40);
        }

        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }

  private void menuEscape() {
    playMenuSound(3);
    this.loadingStage = 100;
  }

  @Override
  protected InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(super.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage != 2) {
      return InputPropagation.PROPAGATE;
    }

    if(action == INPUT_ACTION_MENU_BACK.get() && !repeat) {
      this.menuEscape();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }

  private void FUN_80106d10(final int x, final int y, final int value) {
    long sp10 = 0x2L;

    //LAB_80106d5c
    int s0 = value / 10000000 % 10;
    if(s0 != 0) {
      final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION | Renderable58.FLAG_DELETE_AFTER_RENDER;
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
      final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION | Renderable58.FLAG_DELETE_AFTER_RENDER;
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
      final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION | Renderable58.FLAG_DELETE_AFTER_RENDER;
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
      final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION | Renderable58.FLAG_DELETE_AFTER_RENDER;
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
      final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION | Renderable58.FLAG_DELETE_AFTER_RENDER;
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
      final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION | Renderable58.FLAG_DELETE_AFTER_RENDER;
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
      final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION | Renderable58.FLAG_DELETE_AFTER_RENDER;
      renderable.glyph_04 = s0;
      renderable.tpage_2c = 0x19;
      renderable.clut_30 = 0;
      renderable.z_3c = 0x1f;
      renderable.x_40 = x + 36;
      renderable.y_44 = y;
    }

    //LAB_80107308
    //LAB_80107360
    final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
    renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION | Renderable58.FLAG_DELETE_AFTER_RENDER;
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
      final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION | Renderable58.FLAG_DELETE_AFTER_RENDER;
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
      final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION | Renderable58.FLAG_DELETE_AFTER_RENDER;
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
      final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
      renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION | Renderable58.FLAG_DELETE_AFTER_RENDER;
      renderable.glyph_04 = s0;
      renderable.tpage_2c = 0x19;
      renderable.x_40 = x + 12;
      renderable.y_44 = y;
      renderable.clut_30 = 0;
      renderable.z_3c = 0x1f;
    }

    //LAB_80107690
    final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.uiElements_0000(), null);
    renderable.flags_00 |= Renderable58.FLAG_NO_ANIMATION | Renderable58.FLAG_DELETE_AFTER_RENDER;
    renderable.glyph_04 = value % 10;
    renderable.tpage_2c = 0x19;
    renderable.x_40 = x + 18;
    renderable.y_44 = y;
    renderable.clut_30 = 0;
    renderable.z_3c = 0x1f;
  }

  private void renderDabasMenu(final int selectedSlot) {
    //LAB_801031cc
    renderText(DigDabas_8011d04c, 48, 28, UI_TEXT);
    renderText(AcquiredItems_8011d050, 210, 28, UI_TEXT);
    renderText(SpecialItem_8011d054, 210, 170, UI_TEXT);
    renderText(AcquiredGold_8011cdd4, 30, 124, UI_TEXT);
    renderText(Take_8011d058, 94, this.getDabasMenuY(0) + 2, selectedSlot == 0 ? UI_TEXT_SELECTED_CENTERED : !this.hasItems && this.gold == 0 ? UI_TEXT_DISABLED_CENTERED : UI_TEXT_CENTERED);
    renderText(Discard_8011d05c, 94, this.getDabasMenuY(1) + 2, selectedSlot == 1 ? UI_TEXT_SELECTED_CENTERED : !this.hasItems ? UI_TEXT_DISABLED_CENTERED : UI_TEXT_CENTERED);
    renderText(NextDig_8011d064, 94, this.getDabasMenuY(2) + 2, selectedSlot == 2 ? UI_TEXT_SELECTED_CENTERED : !this.newDigEnabled ? UI_TEXT_DISABLED_CENTERED : UI_TEXT_CENTERED);
    renderMenuItems(194, 37, this.menuItems, 0, 6, null, null);
    renderEightDigitNumber(100, 147, this.gold, 0x2);

    if(this.specialItem != null) {
      renderItemIcon(this.specialItem.getIcon(), 198, 192, 0x8);
      renderText(I18n.translate(this.specialItem.getNameTranslationKey()), 214, 194, UI_TEXT);
    }

    //LAB_80103390
    renderString(16, 178, MENU_DESCRIPTIONS[selectedSlot], false);
  }

  private int getDabasMenuY(final int slot) {
    return 57 + slot * 14;
  }

  private static final String[] MENU_DESCRIPTIONS = {
    "Send gold and items\nDabas has found to\nthe main game.",
    "Delete items from\nthe Pocket Station.",
    "Leave for the\nnext adventure.",
  };

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
