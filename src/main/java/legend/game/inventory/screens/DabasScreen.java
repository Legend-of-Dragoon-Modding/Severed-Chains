package legend.game.inventory.screens;

import legend.core.platform.input.InputAction;
import legend.game.DabasManager;
import legend.game.dabas.Dabas;
import legend.game.dabas.DabasRewardsEvent;
import legend.game.i18n.I18n;
import legend.game.inventory.Equipment;
import legend.game.inventory.Good;
import legend.game.inventory.InventoryEntry;
import legend.game.inventory.ItemStack;
import legend.game.inventory.screens.controls.Button;
import legend.game.modding.coremod.CoreMod;
import legend.game.types.MenuEntries;
import legend.game.types.MenuEntryStruct04;
import legend.game.types.MessageBox20;
import legend.game.types.MessageBoxResult;
import legend.game.types.MessageBoxType;
import legend.game.types.Renderable58;
import legend.lodmod.LodMod;
import org.legendofdragoon.dabas.game.types.Save60;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.PLATFORM;
import static legend.core.GameEngine.REGISTRIES;
import static legend.game.FullScreenEffects.fullScreenEffect_800bb140;
import static legend.game.FullScreenEffects.startFadeEffect;
import static legend.game.Menus.deallocateRenderables;
import static legend.game.Menus.unloadRenderable;
import static legend.game.SItem.UI_TEXT;
import static legend.game.SItem.UI_TEXT_CENTERED;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.dabasMenuGlyphs_80114228;
import static legend.game.SItem.giveEquipment;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.messageBox;
import static legend.game.SItem.renderGlyphs;
import static legend.game.SItem.renderMenuItems;
import static legend.game.SItem.renderString;
import static legend.game.SItem.setMessageBoxText;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.Text.renderText;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_UP;
import static legend.game.sound.Audio.playMenuSound;
import static legend.game.sound.Audio.sssqFadeIn;
import static legend.game.sound.Audio.sssqFadeOut;

public class DabasScreen extends MenuScreen {
  private LoadingStage loadingStage = LoadingStage.INIT_0;

  private Save60 dabasData_8011d7c0;
  private Renderable58 renderable2;

  private final MessageBox20 messageBox_8011dc90 = new MessageBox20();

  private final List<Button> menuButtons = new ArrayList<>();
  private final Button menuTake;
  private final Button menuDiscard;
  private final Button menuNextDig;
  /** If it's stupid and it works, it ain't stupid */
  private boolean playTickSound;

  private int menuIndex;

  private final Runnable unload;

  private int gold;

  private final MenuEntries<InventoryEntry<?>> menuItems = new MenuEntries<>();
  private MenuEntryStruct04<InventoryEntry<?>> specialItem;

  public DabasScreen(final Runnable unload) {
    this.unload = unload;

    this.menuTake = this.addButton(0, "lod.ui.dabas.menu_take");
    this.menuTake.onPressed(this::confirmTakeItems);

    this.menuDiscard = this.addButton(1, "lod.ui.dabas.menu_discard");
    this.menuDiscard.onPressed(this::confirmDiscardItems);

    this.menuNextDig = this.addButton(2, "lod.ui.dabas.menu_next_dig");
    this.menuNextDig.onPressed(this::confirmNextDig);

    this.setFocus(this.menuButtons.getFirst());
  }

  private Button addButton(final int index, final String translationKey) {
    final Button button = this.addControl(new Button(I18n.translate(translationKey)));
    button.setPos(52, this.getDabasMenuY(index));
    button.setSize(88, 14);

    button.onHoverIn(button::focus);

    button.onLostFocus(() -> {
      button.hoverOut();
      button.setTextColour(TextColour.BROWN);
    });

    button.onGotFocus(() -> {
      button.hoverIn();
      button.setTextColour(TextColour.RED);
      this.menuIndex = index;

      if(this.playTickSound) {
        playMenuSound(1);
      }

      this.playTickSound = true;
    });

    button.onInputActionPressed((action, repeat) -> {
      if(action == INPUT_ACTION_MENU_DOWN.get()) {
        for(int i = 1; i < this.menuButtons.size(); i++) {
          final Button otherButton = this.menuButtons.get(Math.floorMod(index + i, this.menuButtons.size()));

          if(!otherButton.isDisabled() && otherButton.isVisible()) {
            otherButton.focus();
            break;
          }
        }

        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_UP.get()) {
        for(int i = 1; i < this.menuButtons.size(); i++) {
          final Button otherButton = this.menuButtons.get(Math.floorMod(index - i, this.menuButtons.size()));

          if(!otherButton.isDisabled() && otherButton.isVisible()) {
            otherButton.focus();
            break;
          }
        }

        return InputPropagation.HANDLED;
      }

      return InputPropagation.PROPAGATE;
    });

    this.menuButtons.add(button);

    return button;
  }

  private void disableButtons() {
    this.menuTake.disable();
    this.menuDiscard.disable();
    this.menuNextDig.disable();
  }

  private void enableButtons() {
    if(!this.menuItems.isEmpty() || this.specialItem != null || this.gold != 0) {
      this.menuTake.enable();
      this.menuDiscard.enable();
    }

    this.menuNextDig.enable();

    if(this.menuTake.isDisabled()) {
      this.menuNextDig.focus();
    }
  }

  @Override
  protected void render() {
    switch(this.loadingStage) {
      case INIT_0 -> {
        startFadeEffect(2, 10);
        this.menuIndex = 0;

        deallocateRenderables(0xff);
        renderGlyphs(dabasMenuGlyphs_80114228, 0, 0);
        this.renderDabasMenu(0);
        this.loadingStage = LoadingStage.LOAD_DATA_1;
      }

      // Load save
      case LOAD_DATA_1 -> {
        this.renderDabasMenu(this.menuIndex);

        this.gold = 0;
        this.disableButtons();

        if(!DabasManager.hasSave()) {
          this.dabasData_8011d7c0 = new Save60();
          this.enableButtons();
          this.loadingStage = LoadingStage.DISPLAY_DATA_2;
          return;
        }

        this.menuItems.clear();
        this.specialItem = null;

        final Save60 dabasData = DabasManager.load();
        this.dabasData_8011d7c0 = dabasData;

        final DabasRewardsEvent event = new DabasRewardsEvent();
        event.gold = dabasData.gold_34;

        for(int i = 0; i < 6; i++) {
          final int itemId = dabasData.items_14[i];

          if(itemId != 0) {
            if(itemId > 192) {
              event.rewards.add(new ItemStack(REGISTRIES.items.getEntry(LodMod.id(LodMod.ITEM_IDS[itemId - 192])).get()));
            } else {
              event.rewards.add(REGISTRIES.equipment.getEntry(LodMod.id(LodMod.EQUIPMENT_IDS[itemId])).get());
            }
          }
        }

        if(dabasData.specialItem_2c != 0) {
          event.specialReward = REGISTRIES.equipment.getEntry(LodMod.id(LodMod.EQUIPMENT_IDS[dabasData.specialItem_2c])).get();
        }

        EVENTS.postEvent(event);

        if(!event.rewards.isEmpty()) {
          for(final InventoryEntry<?> entry : event.rewards) {
            this.menuItems.add(new MenuEntryStruct04<>(entry));
          }
        }

        if(event.specialReward != null) {
          this.specialItem = new MenuEntryStruct04<>(event.specialReward);
        }

        this.gold = dabasData.gold_34;

        this.enableButtons();
        this.loadingStage = LoadingStage.DISPLAY_DATA_2;
      }

      case DISPLAY_DATA_2 -> this.renderDabasMenu(this.menuIndex);

      case START_GIVE_GOLD_3 -> {
        messageBox(this.messageBox_8011dc90);

        if(this.messageBox_8011dc90.ticks_10 < 3) {
          this.renderDabasMenu(this.menuIndex);
          break;
        }

        if(this.renderable2 == null) {
          this.renderable2 = allocateUiElement(0xd3, 0xd3, 68, 80);
          this.renderable2.z_3c = 31;
        }

        this.renderNumber(112, 144, 31, this.gold, 4, 0x2, 0);
        this.renderNumber(226, 144, 31, gameState_800babc8.gold_94, 8, 0x2, 0);

        if(!PLATFORM.isActionPressed(INPUT_ACTION_MENU_CONFIRM.get())) {
          this.renderDabasMenu(this.menuIndex);
          break;
        }

        unloadRenderable(this.renderable2);
        this.renderable2 = allocateUiElement(0xd3, 0xd9, 68, 80);
        this.renderable2.z_3c = 31;
        this.renderDabasMenu(this.menuIndex);
        this.loadingStage = LoadingStage.GIVE_GOLD_4;
      }

      case GIVE_GOLD_4 -> {
        messageBox(this.messageBox_8011dc90);

        if(this.gold <= 10 || PLATFORM.isActionPressed(INPUT_ACTION_MENU_CONFIRM.get())) {
          gameState_800babc8.gold_94 += this.gold;
          this.gold = 0;
          unloadRenderable(this.renderable2);
          this.renderable2 = allocateUiElement(0xd3, 0xd3, 68, 80);
          this.renderable2.z_3c = 31;
          this.loadingStage = LoadingStage.FINISH_GIVE_GOLD_5;
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

        this.renderNumber(112, 144, 31, this.gold, 4, 0x2, 0);
        this.renderNumber(226, 144, 31, gameState_800babc8.gold_94, 8, 0x2, 0);
        this.renderDabasMenu(this.menuIndex);
      }

      case FINISH_GIVE_GOLD_5 -> {
        messageBox(this.messageBox_8011dc90);
        if(PLATFORM.isActionPressed(INPUT_ACTION_MENU_CONFIRM.get())) {
          unloadRenderable(this.renderable2);
          this.messageBox_8011dc90.state_0c++;
          this.loadingStage = LoadingStage.CONFIRM_GIVE_GOLD_6;
        }

        this.renderNumber(112, 144, 31, this.gold, 4, 0x2, 0);
        this.renderNumber(226, 144, 31, gameState_800babc8.gold_94, 8, 0x2, 0);
        this.renderDabasMenu(this.menuIndex);
      }

      case CONFIRM_GIVE_GOLD_6 -> {
        this.renderDabasMenu(this.menuIndex);

        if(messageBox(this.messageBox_8011dc90) != MessageBoxResult.AWAITING_INPUT) {
          this.enableButtons();
          this.loadingStage = LoadingStage.DISPLAY_DATA_2;
        }
      }

      case FADE_OUT_7 -> {
        this.renderDabasMenu(this.menuIndex);
        startFadeEffect(1, 10);
        sssqFadeOut((short)20);
        this.loadingStage = LoadingStage.PLAY_8;
      }

      case PLAY_8 -> {
        this.renderDabasMenu(this.menuIndex);

        if(fullScreenEffect_800bb140.currentColour_28 == 0xff) {
          new Dabas();

          // Dabas takes over so this won't run til the game is closed
          this.loadingStage = LoadingStage.FADE_IN_9;
        }
      }

      case FADE_IN_9 -> {
        startFadeEffect(2, 10);
        sssqFadeIn(20, 0x7f);
        this.loadingStage = LoadingStage.LOAD_DATA_1;
      }

      case CLOSE_100 -> {
        this.renderDabasMenu(this.menuIndex);
        this.dabasData_8011d7c0 = null;
        this.unload.run();
      }
    }
  }

  private void confirmTakeItems() {
    menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod.ui.dabas.take_items_confirm"), MessageBoxType.CONFIRMATION, result -> {
      if(result == MessageBoxResult.YES) {
        this.takeItems();
      }
    }));
  }

  private void takeItems() {
    this.disableButtons();

    final Save60 dabasData = this.dabasData_8011d7c0;
    dabasData.chapter_00 = gameState_800babc8.chapterIndex_98;

    int equipmentCount = 0;
    int itemCount = 0;

    for(final MenuEntryStruct04<? extends InventoryEntry<?>> item : this.menuItems) {
      if(item != null) {
        if(item.item_00 instanceof Equipment) {
          equipmentCount++;
        } else {
          itemCount++;
        }
      }
    }

    if(this.specialItem != null) {
      if(this.specialItem.item_00 instanceof Equipment) {
        equipmentCount++;
      } else {
        itemCount++;
      }
    }

    if(equipmentCount != 0 && gameState_800babc8.equipment_1e8.size() + equipmentCount >= 0x100 || itemCount != 0 && gameState_800babc8.items_2e9.getSize() + itemCount > CONFIG.getConfig(CoreMod.INVENTORY_SIZE_CONFIG.get())) {
      menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod.ui.dabas.too_many_items"), MessageBoxType.ALERT, result -> {}));
      return;
    }

    for(final MenuEntryStruct04<? extends InventoryEntry<?>> entry : this.menuItems) {
      this.giveReward(entry.item_00);
    }

    if(this.specialItem != null) {
      this.giveReward(this.specialItem.item_00);
    }

    this.menuItems.clear();
    this.specialItem = null;

    for(int i = 0; i < 6; i++) {
      dabasData.items_14[i] = 0;
    }

    dabasData.specialItem_2c = 0;
    dabasData.gold_34 = 0;

    DabasManager.save(dabasData);

    setMessageBoxText(this.messageBox_8011dc90, I18n.translate(TAKE_RESPONSES[ThreadLocalRandom.current().nextInt(TAKE_RESPONSES.length)]), MessageBoxType.UNKNOWN);
    this.renderable2 = null;
    this.loadingStage = LoadingStage.START_GIVE_GOLD_3;
  }

  private void giveReward(final InventoryEntry<?> entry) {
    if(entry instanceof final ItemStack item) {
      gameState_800babc8.items_2e9.give(item);
    } else if(entry instanceof final Equipment equipment) {
      giveEquipment(equipment);
    } else if(entry instanceof final Good good) {
      gameState_800babc8.goods_19c.give(good);
    }
  }

  private void confirmDiscardItems() {
    menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod.ui.dabas.discard_items_confirm"), MessageBoxType.CONFIRMATION, result -> {
      if(result == MessageBoxResult.YES) {
        this.discardItems();
      }
    }));
  }

  private void discardItems() {
    this.disableButtons();

    final Save60 dabasData = this.dabasData_8011d7c0;
    dabasData.chapter_00 = gameState_800babc8.chapterIndex_98;

    for(int i = 0; i < 6; i++) {
      dabasData.items_14[i] = 0;
    }

    dabasData.specialItem_2c = 0;

    this.menuItems.clear();
    this.specialItem = null;

    DabasManager.save(dabasData);

    menuStack.pushScreen(new MessageBoxScreen(I18n.translate(DISCARD_RESPONSES[ThreadLocalRandom.current().nextInt(DISCARD_RESPONSES.length)]), MessageBoxType.ALERT, result -> {
      this.enableButtons();
      this.loadingStage = LoadingStage.DISPLAY_DATA_2;
    }));
  }

  private void confirmNextDig() {
    menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod.ui.dabas.next_dig_confirm"), MessageBoxType.CONFIRMATION, result -> {
      if(result == MessageBoxResult.YES) {
        this.nextDig();
      }
    }));
  }

  private void nextDig() {
    this.disableButtons();

    final Save60 dabasData = this.dabasData_8011d7c0;
    dabasData.chapter_00 = gameState_800babc8.chapterIndex_98;

    if(dabasData.completionType_3c == 1) {
      this.menuItems.clear();
      this.specialItem = null;

      dabasData.completionType_3c = 2;
      dabasData.specialItem_2c = 0;

      if(dabasData.bossesKilled_38 == 5) {
        dabasData.numberOfTimesSaved_04 = 0;
      }
    }

    DabasManager.save(dabasData);

    menuStack.pushScreen(new MessageBoxScreen(I18n.translate(NEXT_DIG_RESPONSES[ThreadLocalRandom.current().nextInt(NEXT_DIG_RESPONSES.length)]), MessageBoxType.ALERT, result -> {
      this.loadingStage = LoadingStage.FADE_OUT_7;
    }));
  }

  private void menuEscape() {
    playMenuSound(3);
    this.loadingStage = LoadingStage.CLOSE_100;
  }

  @Override
  protected InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(super.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage != LoadingStage.DISPLAY_DATA_2) {
      return InputPropagation.PROPAGATE;
    }

    if(action == INPUT_ACTION_MENU_BACK.get() && !repeat) {
      this.menuEscape();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }

  private void renderDabasMenu(final int selectedSlot) {
    //LAB_801031cc
    renderText(I18n.translate("lod.ui.dabas.title"), 95, 26, UI_TEXT_CENTERED);
    renderText(I18n.translate("lod.ui.dabas.acquired_items"), 275, 28, UI_TEXT_CENTERED);
    renderText(I18n.translate("lod.ui.dabas.special_items"), 275, 171, UI_TEXT_CENTERED);
    renderText(I18n.translate("lod.ui.dabas.acquired_gold"), 95, 125, UI_TEXT_CENTERED);

    renderMenuItems(194, 38, this.menuItems, 0, 6, null, null);
    this.renderNumber(100, 147, this.gold, 8);

    if(this.specialItem != null) {
      this.specialItem.item_00.renderIcon(198, 192, 0x8);
      renderText(I18n.translate(this.specialItem.getNameTranslationKey()), 214, 195, UI_TEXT);
    }

    //LAB_80103390
    renderString(16, 178, I18n.translate(MENU_DESCRIPTIONS[selectedSlot]), false);
  }

  private int getDabasMenuY(final int slot) {
    return 58 + slot * 14;
  }

  private static final String[] MENU_DESCRIPTIONS = {
    "lod.ui.dabas.menu_take_description",
    "lod.ui.dabas.menu_discard_description",
    "lod.ui.dabas.menu_next_dig_description",
  };

  private static final String[] TAKE_RESPONSES = {
    "lod.ui.dabas.take_response_1",
    "lod.ui.dabas.take_response_2",
    "lod.ui.dabas.take_response_3",
    "lod.ui.dabas.take_response_4",
    "lod.ui.dabas.take_response_5",
    "lod.ui.dabas.take_response_6",
  };

  private static final String[] DISCARD_RESPONSES = {
    "lod.ui.dabas.discard_response_1",
    "lod.ui.dabas.discard_response_2",
    "lod.ui.dabas.discard_response_3",
    "lod.ui.dabas.discard_response_4",
    "lod.ui.dabas.discard_response_5",
    "lod.ui.dabas.discard_response_6",
    "lod.ui.dabas.discard_response_7",
    "lod.ui.dabas.discard_response_8",
    "lod.ui.dabas.discard_response_9",
    "lod.ui.dabas.discard_response_10",
  };

  private static final String[] NEXT_DIG_RESPONSES = {
    "lod.ui.dabas.next_dig_response_1",
    "lod.ui.dabas.next_dig_response_2",
    "lod.ui.dabas.next_dig_response_3",
    "lod.ui.dabas.next_dig_response_4",
    "lod.ui.dabas.next_dig_response_5",
    "lod.ui.dabas.next_dig_response_6",
    "lod.ui.dabas.next_dig_response_7",
    "lod.ui.dabas.next_dig_response_8",
    "lod.ui.dabas.next_dig_response_9",
    "lod.ui.dabas.next_dig_response_10",
    "lod.ui.dabas.next_dig_response_11",
    "lod.ui.dabas.next_dig_response_12",
    "lod.ui.dabas.next_dig_response_13",
    "lod.ui.dabas.next_dig_response_14",
  };

  private enum LoadingStage {
    INIT_0,
    LOAD_DATA_1,
    DISPLAY_DATA_2,
    START_GIVE_GOLD_3,
    GIVE_GOLD_4,
    FINISH_GIVE_GOLD_5,
    CONFIRM_GIVE_GOLD_6,
    FADE_OUT_7,
    PLAY_8,
    FADE_IN_9,

    CLOSE_100,
  }
}
