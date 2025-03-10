package legend.game.inventory.screens;

import legend.core.MathHelper;
import legend.core.memory.Method;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputMod;
import legend.game.i18n.I18n;
import legend.game.inventory.Item;
import legend.game.inventory.UseItemResponse;
import legend.game.types.ActiveStatsa0;
import legend.game.types.MenuEntries;
import legend.game.types.MenuEntryStruct04;
import legend.game.types.Renderable58;

import java.util.Set;

import static legend.game.SItem.FUN_80104b60;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.characterCount_8011d7c4;
import static legend.game.SItem.glyph_801142d4;
import static legend.game.SItem.initGlyph;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.renderCharacterStatusEffect;
import static legend.game.SItem.renderFourDigitHp;
import static legend.game.SItem.renderFourDigitNumber;
import static legend.game.SItem.renderGlyphs;
import static legend.game.SItem.renderMenuItems;
import static legend.game.SItem.renderString;
import static legend.game.SItem.useItemGlyphs_801141fc;
import static legend.game.Scus94491BpeSegment.startFadeEffect;
import static legend.game.Scus94491BpeSegment_8002.allocateRenderable;
import static legend.game.Scus94491BpeSegment_8002.deallocateRenderables;
import static legend.game.Scus94491BpeSegment_8002.menuItemIconComparator;
import static legend.game.Scus94491BpeSegment_8002.playMenuSound;
import static legend.game.Scus94491BpeSegment_8002.takeItemId;
import static legend.game.Scus94491BpeSegment_8002.unloadRenderable;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.saveListDownArrow_800bdb98;
import static legend.game.Scus94491BpeSegment_800b.saveListUpArrow_800bdb94;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800b.uiFile_800bdc3c;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BOTTOM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_END;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_HOME;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_LEFT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_PAGE_DOWN;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_PAGE_UP;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_RIGHT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_TOP;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_UP;

public class UseItemScreen extends MenuScreen {
  private static final String HP_recovered_for_all_8011cfcc = "HP recovered for all";
  private static final String MP_recovered_for_all_8011cff8 = "MP recovered for all";
  private static final String Completely_recovered_8011d534 = "completely recovered";
  private static final String Recovered_8011d560 = "Recovered";
  private static final String HP_8011d57c = " HP ";
  private static final String MP_8011d584 = " MP ";
  private static final String SP_8011d58c = " SP ";
  private static final String Encounter_risk_reduced_8011d594 = "Encounter risk reduced";
  private static final String Detoxified_8011d5c8 = "Detoxified";
  private static final String Spirit_recovered_8011d5e0 = "Spirit recovered";
  private static final String Fear_gone_8011d604 = "Fear gone";
  private static final String Nothing_happened_8011d618 = "Nothing happened";

  private int loadingStage;
  private double scrollAccumulator;
  private final Runnable unload;

  /** Allows list wrapping, but only on new input */
  private boolean allowWrapY = true;

  private int charSlot;
  private int selectedSlot;
  private int slotScroll;
  private int itemCount;
  private boolean itemTargetAll;
  private final UseItemResponse useItemResponse = new UseItemResponse();
  private Renderable58 itemHighlight;
  private Renderable58 charHighlight;
  private final Renderable58[] _8011d718 = new Renderable58[7];

  private final MenuEntries<Item> menuItems = new MenuEntries<>();

  public UseItemScreen(final Runnable unload) {
    this.unload = unload;
  }

  @Override
  protected void render() {
    switch(this.loadingStage) {
      case 0 -> {
        startFadeEffect(2, 10);
        this.charSlot = 0;
        this.slotScroll = 0;
        this.selectedSlot = 0;
        this.useItemResponse._00 = 0;
        this.useItemResponse.value_04 = 0;
        this.loadingStage++;
      }

      case 1 -> {
        deallocateRenderables(0xff);
        renderGlyphs(useItemGlyphs_801141fc, 0, 0);
        this.itemHighlight = allocateUiElement(0x77, 0x77, 42, this.getItemSlotY(this.selectedSlot));
        FUN_80104b60(this.itemHighlight);
        this.itemCount = this.getUsableItemsInMenu();
        if(this.slotScroll > this.itemCount - 5) {
          this.slotScroll = Math.max(0, this.itemCount - 5);
        }
        this.renderUseItemMenu(this.selectedSlot, this.slotScroll, 0xff);
        this.loadingStage++;
      }

      // Render menu
      case 2 -> {
        this.renderUseItemMenu(this.selectedSlot, this.slotScroll, 0);

        if(this.scrollAccumulator >= 1.0d) {
          this.scrollAccumulator -= 1.0d;

          if(this.slotScroll > 0) {
            this.scroll(this.slotScroll - 1);
          }
        }

        if(this.scrollAccumulator <= -1.0d) {
          this.scrollAccumulator += 1.0d;

          if(this.slotScroll < this.itemCount - 5) {
            this.scroll(this.slotScroll + 1);
          }
        }
      }

      // Select character on whom to use item
      case 3 -> this.renderUseItemMenu(this.selectedSlot, this.slotScroll, 0);

      // Fade out
      case 100 -> {
        this.renderUseItemMenu(this.selectedSlot, this.slotScroll, 0);
        saveListDownArrow_800bdb98 = null;
        saveListUpArrow_800bdb94 = null;
        this.unload.run();
      }
    }
  }

  private void scroll(final int scroll) {
    playMenuSound(1);
    this.slotScroll = scroll;
    this.itemHighlight.y_44 = this.getItemSlotY(this.selectedSlot);
  }

  private void renderUseItemMenu(final int selectedSlot, final int slotScroll, final long a3) {
    final boolean allocate = a3 == 0xff;

    //LAB_80102e48
    for(int i = 0; i < characterCount_8011d7c4; i++) {
      this.renderUseItemCharacterPortrait(this.getCharacterPortraitX(i) - 5, 120, characterIndices_800bdbb8[i], allocate);
    }

    //LAB_80102e88
    if(allocate) {
      allocateUiElement(84, 84, 16, 16);
      saveListUpArrow_800bdb94 = allocateUiElement(61, 68, 180, this.getItemSlotY(0) + 2);
      saveListDownArrow_800bdb98 = allocateUiElement(53, 60, 180, this.getItemSlotY(4) + 2);
    }

    //LAB_80102ee8
    renderMenuItems(16, 10, this.menuItems, slotScroll, 5, saveListUpArrow_800bdb94, saveListDownArrow_800bdb98);

    if(selectedSlot + slotScroll < this.menuItems.size()) {
      renderString(194, 16, I18n.translate(this.menuItems.get(selectedSlot + slotScroll).item_00.getDescriptionTranslationKey()), allocate);
    } else {
      renderString(194, 16, "", allocate);
    }
  }

  @Method(0x80108464L)
  private void renderUseItemCharacterPortrait(final int x, final int y, final int charIndex, final boolean allocate) {
    if(charIndex != -1) {
      renderCharacterStatusEffect(x - 4, y - 6, charIndex);

      if(allocate) {
        allocateUiElement(112, 112, x, y).z_3c = 33;

        if(charIndex < 9) {
          final Renderable58 renderable = allocateRenderable(uiFile_800bdc3c.portraits_cfac(), null);
          initGlyph(renderable, glyph_801142d4);
          renderable.glyph_04 = charIndex;
          renderable.tpage_2c++;
          renderable.z_3c = 33;
          renderable.x_40 = x + 2;
          renderable.y_44 = y + 8;
        }

        //LAB_80108544
        final ActiveStatsa0 stats = stats_800be5f8[charIndex];
        renderFourDigitHp(x + 25, y + 57, stats.hp_04, stats.maxHp_66);
        renderFourDigitNumber(x + 25, y + 68, stats.maxHp_66);
        renderFourDigitNumber(x + 25, y + 79, stats.mp_06);
        renderFourDigitNumber(x + 25, y + 90, stats.maxMp_6e);
      }
    }
  }

  private int getUsableItemsInMenu() {
    this.menuItems.clear();

    for(int i = 0; i < gameState_800babc8.items_2e9.size(); i++) {
      final Item item = gameState_800babc8.items_2e9.get(i);

      if(item.canBeUsed(Item.UsageLocation.MENU)) {
        final MenuEntryStruct04<Item> menuEntry = MenuEntryStruct04.make(item);
        menuEntry.flags_02 = 0;

        if(!item.canBeUsedNow(Item.UsageLocation.MENU)) {
          menuEntry.flags_02 = 0x4000;
        }

        this.menuItems.add(menuEntry);
      }
    }

    this.menuItems.sort(menuItemIconComparator());
    return this.menuItems.size();
  }

  @Override
  protected InputPropagation mouseMove(final int x, final int y) {
    if(super.mouseMove(x, y) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage == 2) {
      for(int slot = 0; slot < Math.min(5, this.itemCount - this.slotScroll); slot++) {
        if(this.selectedSlot != slot && MathHelper.inBox(x, y, 33, this.getItemSlotY(slot), 136, 17)) {
          playMenuSound(1);
          this.selectedSlot = slot;
          this.itemHighlight.y_44 = this.getItemSlotY(this.selectedSlot);
          return InputPropagation.HANDLED;
        }
      }
    } else if(this.loadingStage == 3 && !this.itemTargetAll) {
      for(int slot = 0; slot < characterCount_8011d7c4; slot++) {
        if(this.charSlot != slot && MathHelper.inBox(x, y, this.getCharacterPortraitX(slot) - 11, 110, 48, 112)) {
          playMenuSound(1);
          this.charSlot = slot;
          this.charHighlight.x_40 = this.getCharacterPortraitX(this.charSlot) - 3;
          return InputPropagation.HANDLED;
        }
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Override
  protected InputPropagation mouseClick(final int x, final int y, final int button, final Set<InputMod> mods) {
    if(super.mouseClick(x, y, button, mods) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(!mods.isEmpty()) {
      return InputPropagation.PROPAGATE;
    }

    if(this.loadingStage == 2) {
      for(int slot = 0; slot < Math.min(5, this.itemCount - this.slotScroll); slot++) {
        if(MathHelper.inBox(x, y, 33, this.getItemSlotY(slot), 136, 17)) {
          this.selectedSlot = slot;
          this.itemHighlight.y_44 = this.getItemSlotY(this.selectedSlot);

          final Item item = this.menuItems.get(this.selectedSlot + this.slotScroll).item_00;
          this.itemTargetAll = item.canTarget(Item.TargetType.ALL);

          if(item.canBeUsed(Item.UsageLocation.MENU) && (this.menuItems.get(this.selectedSlot + this.slotScroll).flags_02 & 0x4000) == 0) {
            if(this.itemTargetAll) {
              for(int i = 0; i < 7; i++) {
                this._8011d718[i] = allocateUiElement(0x7e, 0x7e, this.getCharacterPortraitX(i) - 3, 110);
                FUN_80104b60(this._8011d718[i]);
              }
            } else {
              this.charHighlight = allocateUiElement(0x7e, 0x7e, this.getCharacterPortraitX(this.charSlot) - 3, 110);
              FUN_80104b60(this.charHighlight);
            }

            playMenuSound(2);
            this.loadingStage = 3;
          } else {
            playMenuSound(40);
          }

          return InputPropagation.HANDLED;
        }
      }
    } else if(this.loadingStage == 3) {
      for(int slot = 0; slot < characterCount_8011d7c4; slot++) {
        if(MathHelper.inBox(x, y, this.getCharacterPortraitX(slot) - 11, 110, 48, 112)) {
          if(!this.itemTargetAll) {
            this.menuItems.get(this.selectedSlot + this.slotScroll).item_00.useInMenu(this.useItemResponse, characterIndices_800bdbb8[this.charSlot]);
          } else {
            int responseValue = -2;

            for(int i = 0; i < characterCount_8011d7c4; i++) {
              this.menuItems.get(this.selectedSlot + this.slotScroll).item_00.useInMenu(this.useItemResponse, characterIndices_800bdbb8[i]);

              if(this.useItemResponse.value_04 != -2) {
                responseValue = 0;
              }
            }

            this.useItemResponse.value_04 = responseValue;
          }

          if(this.useItemResponse.value_04 == -2) {
            playMenuSound(40);
          } else {
            playMenuSound(2);
            takeItemId(this.menuItems.get(this.selectedSlot + this.slotScroll).item_00);
            this.itemCount = this.getUsableItemsInMenu();
            loadCharacterStats();
            this.getItemResponseText(this.useItemResponse);
            menuStack.pushScreen(new MessageBoxScreen(this.useItemResponse.string_08, 0, result -> {}));
            this.loadingStage = 1;
          }

          return InputPropagation.HANDLED;
        }
      }
    }

    return InputPropagation.PROPAGATE;
  }

  private void getItemResponseText(final UseItemResponse response) {
    switch(response._00) {
      case 2:
        this.getRecoveryResponseText(HP_8011d57c, response);
        break;

      case 3:
        this.getRecoveryResponseText(HP_recovered_for_all_8011cfcc, response);
        break;

      case 4:
        this.getRecoveryResponseText(MP_8011d584, response);
        break;

      case 5:
        this.getRecoveryResponseText(MP_recovered_for_all_8011cff8, response);
        break;

      case 6:
        this.getRecoveryResponseText(SP_8011d58c, response);
        break;

      case 8:
        response.string_08 = Encounter_risk_reduced_8011d594;
        break;

      case 7:
        final int value = response.value_04;

        if((value & 0x80) != 0) {
          response.string_08 = Detoxified_8011d5c8;
          break;
        }

        if((value & 0x40) != 0) {
          response.string_08 = Spirit_recovered_8011d5e0;
          break;
        }

        if((value & 0x8) != 0) {
          response.string_08 = Fear_gone_8011d604;
          break;
        }

      case 9:
        response.string_08 = Nothing_happened_8011d618;
        break;
    }
  }

  private void getRecoveryResponseText(final String baseString, final UseItemResponse response) {
    if(response.value_04 == -2) {
      response.string_08 = Nothing_happened_8011d618;
    } else if(response.value_04 == -1) {
      response.string_08 = baseString + Completely_recovered_8011d534;
    } else if(response.value_04 != 0) {
      response.string_08 = response.value_04 + baseString + Recovered_8011d560;
    } else {
      response.string_08 = baseString;
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

  private void menuStage2Escape() {
    playMenuSound(3);
    this.loadingStage = 100;
  }

  private void menuStage2NavigateUp() {
    if(this.selectedSlot > 0) {
      playMenuSound(1);
      this.selectedSlot--;
    } else if(this.slotScroll > 0) {
      playMenuSound(1);
      this.slotScroll--;
    } else if(this.itemCount > 1 && this.allowWrapY) {
      this.selectedSlot = this.itemCount > 4 ? 4 : this.itemCount - 1;
      this.scroll(this.itemCount > 5 ? this.itemCount - 5 : 0);
    }

    this.itemHighlight.y_44 = this.getItemSlotY(this.selectedSlot);
  }

  private void menuStage2NavigateDown() {
    if(this.slotScroll + this.selectedSlot < this.itemCount - 1) {
      playMenuSound(1);
      if(this.selectedSlot < 4) {
        this.selectedSlot++;
      } else {
        this.slotScroll++;
      }
    } else if(this.itemCount > 1 && this.allowWrapY) {
      this.selectedSlot = 0;
      this.scroll(0);
    }

    this.itemHighlight.y_44 = this.getItemSlotY(this.selectedSlot);
  }

  private void menuStage2NavigateTop() {
    if(this.selectedSlot != 0) {
      playMenuSound(1);
      this.selectedSlot = 0;
      this.itemHighlight.y_44 = this.getItemSlotY(this.selectedSlot);
    }
  }

  private void menuStage2NavigateBottom() {
    if(this.selectedSlot != 4) {
      playMenuSound(1);
      this.selectedSlot = 4;
      this.itemHighlight.y_44 = this.getItemSlotY(this.selectedSlot);
    }
  }

  private void menuStage2NavigatePageUp() {
    if(this.slotScroll - 4 >= 0) {
      this.scroll(this.slotScroll - 4);
    } else if(this.slotScroll != 0) {
      this.scroll(0);
    }
  }

  private void menuStage2NavigatePageDown() {
    if(this.slotScroll + 4 <= this.itemCount - 5) {
      this.scroll(this.slotScroll + 4);
    } else if(this.itemCount > 5 && this.slotScroll != this.itemCount - 5) {
      this.scroll(this.itemCount - 5);
    }
  }

  private void menuStage2NavigateHome() {
    if(this.selectedSlot > 0 || this.slotScroll > 0) {
      this.selectedSlot = 0;
      this.scroll(0);
    }
  }

  private void menuStage2NavigateEnd() {
    if(this.itemCount > 0 && this.slotScroll + this.selectedSlot != this.itemCount - 1) {
      this.selectedSlot = Math.min(4, this.itemCount - 1);
      this.scroll(this.itemCount - 1 - this.selectedSlot);
    }
  }

  private void menuStage2Select() {
    if(this.slotScroll + this.selectedSlot >= this.itemCount) {
      playMenuSound(40);
      return;
    }

    final Item item = this.menuItems.get(this.selectedSlot + this.slotScroll).item_00;
    this.itemTargetAll = item.canTarget(Item.TargetType.ALL);

    if(!item.canBeUsed(Item.UsageLocation.MENU) || (this.menuItems.get(this.selectedSlot + this.slotScroll).flags_02 & 0x4000) != 0) {
      playMenuSound(40);
      return;
    }

    if(this.itemTargetAll) {
      for(int i = 0; i < 7; i++) {
        this._8011d718[i] = allocateUiElement(0x7e, 0x7e, this.getCharacterPortraitX(i) - 3, 110);
        FUN_80104b60(this._8011d718[i]);
      }
    } else {
      this.charHighlight = allocateUiElement(0x7e, 0x7e, this.getCharacterPortraitX(this.charSlot) - 3, 110);
      FUN_80104b60(this.charHighlight);
    }

    playMenuSound(2);
    this.loadingStage = 3;
  }

  private void menuStage3Escape() {
    if(!this.itemTargetAll) {
      unloadRenderable(this.charHighlight);
    } else {
      for(int i = 0; i < 7; i++) {
        unloadRenderable(this._8011d718[i]);
      }
    }

    playMenuSound(3);
    this.loadingStage = 2;
  }

  private void menuStage3NavigateLeft() {
    if(!this.itemTargetAll) {
      playMenuSound(1);

      if(this.charSlot > 0) {
        this.charSlot--;
      } else {
        this.charSlot = characterCount_8011d7c4 - 1;
      }

      this.charHighlight.x_40 = this.getCharacterPortraitX(this.charSlot) - 3;
    }
  }

  private void menuStage3NavigateRight() {
    if(!this.itemTargetAll) {
      playMenuSound(1);

      if(this.charSlot < characterCount_8011d7c4 - 1) {
        this.charSlot++;
      } else {
        this.charSlot = 0;
      }

      this.charHighlight.x_40 = this.getCharacterPortraitX(this.charSlot) - 3;
    }
  }

  private void menuStage3Select() {
    if(!this.itemTargetAll) {
      this.menuItems.get(this.selectedSlot + this.slotScroll).item_00.useInMenu(this.useItemResponse, characterIndices_800bdbb8[this.charSlot]);
    } else {
      int responseValue = -2;

      for(int i = 0; i < characterCount_8011d7c4; i++) {
        this.menuItems.get(this.selectedSlot + this.slotScroll).item_00.useInMenu(this.useItemResponse, characterIndices_800bdbb8[i]);

        if(this.useItemResponse.value_04 != -2) {
          responseValue = 0;
        }
      }

      this.useItemResponse.value_04 = responseValue;
    }

    if(this.useItemResponse.value_04 == -2) {
      playMenuSound(40);
    } else {
      playMenuSound(2);
      takeItemId(this.menuItems.get(this.selectedSlot + this.slotScroll).item_00);
      this.itemCount = this.getUsableItemsInMenu();
      loadCharacterStats();

      if(this.slotScroll == 0 && this.selectedSlot > this.itemCount - 1) {
        this.selectedSlot = Math.max(0, --this.selectedSlot);
      }

      this.getItemResponseText(this.useItemResponse);
      this.deferAction(() -> menuStack.pushScreen(new MessageBoxScreen(this.useItemResponse.string_08, 0, result -> {})));
      this.loadingStage = 1;
    }
  }

  @Override
  public InputPropagation inputActionPressed(final InputAction action, final boolean repeat) {
    if(super.inputActionPressed(action, repeat) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage == 2) {
      if(action == INPUT_ACTION_MENU_HOME.get()) {
        this.menuStage2NavigateHome();
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_END.get()) {
        this.menuStage2NavigateEnd();
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_PAGE_UP.get()) {
        this.menuStage2NavigatePageUp();
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_PAGE_DOWN.get()) {
        this.menuStage2NavigatePageDown();
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_TOP.get()) {
        this.menuStage2NavigateTop();
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_BOTTOM.get()) {
        this.menuStage2NavigateBottom();
        return InputPropagation.HANDLED;
      }

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
      if(action == INPUT_ACTION_MENU_LEFT.get()) {
        this.menuStage3NavigateLeft();
        return InputPropagation.HANDLED;
      }

      if(action == INPUT_ACTION_MENU_RIGHT.get()) {
        this.menuStage3NavigateRight();
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

    if(this.loadingStage == 2) {
      if(action == INPUT_ACTION_MENU_UP.get() || action == INPUT_ACTION_MENU_DOWN.get()) {
        this.allowWrapY = true;
        return InputPropagation.HANDLED;
      }
    }

    return InputPropagation.PROPAGATE;
  }

  @Method(0x800fc8c0L)
  private int getCharacterPortraitX(final int slot) {
    return 21 + slot * 50;
  }

  @Method(0x800fc8dcL)
  private int getItemSlotY(final int slot) {
    return 18 + slot * 17;
  }
}
