package legend.game.inventory.screens;

import legend.core.GameEngine;
import legend.core.MathHelper;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.platform.input.InputAction;
import legend.core.platform.input.InputMod;
import legend.game.characters.CharacterData2c;
import legend.game.characters.VitalsStat;
import legend.game.i18n.I18n;
import legend.game.inventory.Item;
import legend.game.inventory.ItemStack;
import legend.game.inventory.UseItemResponse;
import legend.game.textures.TextureAtlasIcon;
import legend.game.types.MenuEntries;
import legend.game.types.MenuEntryStruct04;
import legend.game.types.MessageBoxType;
import legend.game.types.Renderable58;
import legend.lodmod.LodMod;

import java.util.List;
import java.util.Set;

import static legend.game.FullScreenEffects.startFadeEffect;
import static legend.game.Menus.deallocateRenderables;
import static legend.game.Menus.downArrow_800bdb98;
import static legend.game.Menus.upArrow_800bdb94;
import static legend.game.Menus.uploadRenderable;
import static legend.game.SItem.allocateManualUiElement;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.initHighlight;
import static legend.game.SItem.menuItemIconComparator;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.renderCharacterStatusEffect;
import static legend.game.SItem.renderFourDigitHp;
import static legend.game.SItem.renderGlyphs;
import static legend.game.SItem.renderMenuItems;
import static legend.game.SItem.renderString;
import static legend.game.SItem.takeItemFromSlot;
import static legend.game.SItem.useItemGlyphs_801141fc;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
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
import static legend.game.sound.Audio.playMenuSound;
import static legend.lodmod.LodMod.HP_STAT;
import static legend.lodmod.LodMod.MP_STAT;

public class UseItemScreen extends MenuScreen {
  private static final int CHAR_SLOTS = 7;

  private int loadingStage;
  private double scrollAccumulator;
  private final Runnable unload;

  /** Allows list wrapping, but only on new input */
  private boolean allowWrapY = true;

  private int charSlot;
  private int charScroll;
  private int itemSlot;
  private int itemScroll;
  private int itemCount;
  private boolean itemTargetAll;
  private final UseItemResponse useItemResponse = new UseItemResponse();
  private Renderable58 itemHighlight;
  private final Renderable58[] charHighlight = new Renderable58[CHAR_SLOTS];
  private final MV transforms = new MV();

  private final MenuEntries<ItemStack> menuItems = new MenuEntries<>();

  public UseItemScreen(final Runnable unload) {
    this.unload = unload;
  }

  @Override
  protected void render() {
    switch(this.loadingStage) {
      case 0 -> {
        startFadeEffect(2, 10);
        this.charSlot = 0;
        this.charScroll = 0;
        this.itemScroll = 0;
        this.itemSlot = 0;
        this.useItemResponse.success = false;
        this.loadingStage++;
      }

      case 1 -> {
        deallocateRenderables(0xff);
        renderGlyphs(useItemGlyphs_801141fc, 0, 0);

        this.itemHighlight = allocateUiElement(0x77, 0x77, 42, this.getItemSlotY(this.itemSlot));
        initHighlight(this.itemHighlight);

        for(int i = 0; i < CHAR_SLOTS; i++) {
          this.charHighlight[i] = allocateManualUiElement(0x7e, 0x7e, this.getCharacterPortraitX(i) - 3, 110);
          initHighlight(this.charHighlight[i]);
        }

        this.itemCount = this.getUsableItemsInMenu();

        if(this.itemScroll > this.itemCount - 5) {
          this.itemScroll = Math.max(0, this.itemCount - 5);
        }

        this.renderUseItemMenu(this.itemSlot, this.itemScroll, 0xff);
        this.loadingStage++;
      }

      // Render menu
      case 2 -> {
        this.renderUseItemMenu(this.itemSlot, this.itemScroll, 0);

        if(this.scrollAccumulator >= 1.0d) {
          this.scrollAccumulator -= 1.0d;

          if(this.itemScroll > 0) {
            this.scroll(this.itemScroll - 1);
          }
        }

        if(this.scrollAccumulator <= -1.0d) {
          this.scrollAccumulator += 1.0d;

          if(this.itemScroll < this.itemCount - 5) {
            this.scroll(this.itemScroll + 1);
          }
        }
      }

      // Select character on whom to use item
      case 3 -> this.renderUseItemMenu(this.itemSlot, this.itemScroll, 0);

      // Fade out
      case 100 -> {
        this.renderUseItemMenu(this.itemSlot, this.itemScroll, 0);
        downArrow_800bdb98 = null;
        upArrow_800bdb94 = null;
        this.unload.run();
      }
    }
  }

  private void scroll(final int scroll) {
    playMenuSound(1);
    this.itemScroll = scroll;
    this.itemHighlight.y_44 = this.getItemSlotY(this.itemSlot);
  }

  private void renderUseItemMenu(final int selectedSlot, final int slotScroll, final long a3) {
    final boolean allocate = a3 == 0xff;

    //LAB_80102e48
    for(int i = 0; i < Math.min(characterIndices_800bdbb8.size(), CHAR_SLOTS); i++) {
      this.renderUseItemCharacterPortrait(this.getCharacterPortraitX(i) - 5, 120, characterIndices_800bdbb8.getInt(this.charScroll + i));
    }

    //LAB_80102e88
    if(allocate) {
      allocateUiElement(84, 84, 16, 16);
      upArrow_800bdb94 = allocateUiElement(61, 68, 180, this.getItemSlotY(0) + 2);
      downArrow_800bdb98 = allocateUiElement(53, 60, 180, this.getItemSlotY(4) + 2);
    }

    //LAB_80102ee8
    renderMenuItems(16, 10, this.menuItems, slotScroll, 5, upArrow_800bdb94, downArrow_800bdb98);

    if(selectedSlot + slotScroll < this.menuItems.size()) {
      renderString(194, 16, I18n.translate(this.menuItems.get(selectedSlot + slotScroll).item_00.getDescriptionTranslationKey()), allocate);
    } else {
      renderString(194, 16, "", allocate);
    }

    if(this.loadingStage == 3) {
      if(this.itemTargetAll) {
        for(int i = 0; i < CHAR_SLOTS; i++) {
          uploadRenderable(this.charHighlight[i], 0, 0);
        }
      } else {
        uploadRenderable(this.charHighlight[this.charSlot - this.charScroll], 0, 0);
      }
    }
  }

  @Method(0x80108464L)
  private void renderUseItemCharacterPortrait(final int x, final int y, final int charIndex) {
    final CharacterData2c character = gameState_800babc8.charData_32c.get(charIndex);
    final TextureAtlasIcon icon = GameEngine.getTextureAtlas().getIcon(character.template.getRegistryId());
    this.transforms.transfer.set(x - 6.0f, y + 8.0f, 132.0f);
    this.transforms.scaling(48.0f, 48.0f, 1.0f);
    icon.render(this.transforms);

    renderCharacterStatusEffect(x - 4, y - 6, character);

    final Renderable58 renderable = allocateUiElement(112, 112, x, y);
    renderable.flags_00 |= Renderable58.FLAG_DELETE_AFTER_RENDER;
    renderable.z_3c = 33;

    final VitalsStat hp = character.stats.getStat(HP_STAT.get());
    final VitalsStat mp = character.stats.getStat(MP_STAT.get());

    renderFourDigitHp(x + 25, y + 57, hp.getCurrent(), hp.getMax(), Renderable58.FLAG_DELETE_AFTER_RENDER);
    this.renderNumber(x + 25, y + 68, hp.getMax(), 4);
    this.renderNumber(x + 25, y + 79, mp.getCurrent(), 4);
    this.renderNumber(x + 25, y + 90, mp.getMax(), 4);
  }

  private int getUsableItemsInMenu() {
    this.menuItems.clear();

    for(int i = 0; i < gameState_800babc8.items_2e9.getSize(); i++) {
      final ItemStack stack = gameState_800babc8.items_2e9.get(i);

      if(stack.canBeUsed(Item.UsageLocation.MENU)) {
        final MenuEntryStruct04<ItemStack> menuEntry = new MenuEntryStruct04<>(stack);
        menuEntry.itemSlot_01 = i;
        menuEntry.flags_02 = 0;

        if(!stack.canBeUsedNow(Item.UsageLocation.MENU)) {
          menuEntry.flags_02 = 0x4000;
        }

        this.menuItems.add(menuEntry);
      }
    }

    this.menuItems.sort(menuItemIconComparator(List.of(LodMod.ITEM_IDS), stack -> stack.getItem().getRegistryId()));
    return this.menuItems.size();
  }

  @Override
  protected InputPropagation mouseMove(final double x, final double y) {
    if(super.mouseMove(x, y) == InputPropagation.HANDLED) {
      return InputPropagation.HANDLED;
    }

    if(this.loadingStage == 2) {
      for(int slot = 0; slot < Math.min(5, this.itemCount - this.itemScroll); slot++) {
        if(this.itemSlot != slot && MathHelper.inBox((int)x, (int)y, 33, this.getItemSlotY(slot), 136, 17)) {
          playMenuSound(1);
          this.itemSlot = slot;
          this.itemHighlight.y_44 = this.getItemSlotY(this.itemSlot);
          return InputPropagation.HANDLED;
        }
      }
    } else if(this.loadingStage == 3 && !this.itemTargetAll) {
      for(int slot = 0; slot < Math.min(characterIndices_800bdbb8.size(), CHAR_SLOTS); slot++) {
        if(this.charSlot != slot + this.charScroll && MathHelper.inBox((int)x, (int)y, this.getCharacterPortraitX(slot) - 11, 110, 48, 112)) {
          playMenuSound(1);
          this.charSlot = slot + this.charScroll;
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

    if(!mods.isEmpty()) {
      return InputPropagation.PROPAGATE;
    }

    if(this.loadingStage == 2) {
      for(int slot = 0; slot < Math.min(5, this.itemCount - this.itemScroll); slot++) {
        if(MathHelper.inBox((int)x, (int)y, 33, this.getItemSlotY(slot), 136, 17)) {
          this.itemSlot = slot;
          this.itemHighlight.y_44 = this.getItemSlotY(this.itemSlot);
          this.menuStage2Select();
          return InputPropagation.HANDLED;
        }
      }
    } else if(this.loadingStage == 3) {
      for(int slot = 0; slot < Math.min(characterIndices_800bdbb8.size(), CHAR_SLOTS); slot++) {
        if(MathHelper.inBox((int)x, (int)y, this.getCharacterPortraitX(slot) - 11, 110, 48, 112)) {
          this.menuStage3Select();
          return InputPropagation.HANDLED;
        }
      }
    }

    return InputPropagation.PROPAGATE;
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
    if(this.itemSlot > 0) {
      playMenuSound(1);
      this.itemSlot--;
    } else if(this.itemScroll > 0) {
      playMenuSound(1);
      this.itemScroll--;
    } else if(this.itemCount > 1 && this.allowWrapY) {
      this.itemSlot = this.itemCount > 4 ? 4 : this.itemCount - 1;
      this.scroll(this.itemCount > 5 ? this.itemCount - 5 : 0);
    }

    this.itemHighlight.y_44 = this.getItemSlotY(this.itemSlot);
  }

  private void menuStage2NavigateDown() {
    if(this.itemScroll + this.itemSlot < this.itemCount - 1) {
      playMenuSound(1);
      if(this.itemSlot < 4) {
        this.itemSlot++;
      } else {
        this.itemScroll++;
      }
    } else if(this.itemCount > 1 && this.allowWrapY) {
      this.itemSlot = 0;
      this.scroll(0);
    }

    this.itemHighlight.y_44 = this.getItemSlotY(this.itemSlot);
  }

  private void menuStage2NavigateTop() {
    if(this.itemSlot != 0) {
      playMenuSound(1);
      this.itemSlot = 0;
      this.itemHighlight.y_44 = this.getItemSlotY(this.itemSlot);
    }
  }

  private void menuStage2NavigateBottom() {
    if(this.itemSlot != 4) {
      playMenuSound(1);
      this.itemSlot = 4;
      this.itemHighlight.y_44 = this.getItemSlotY(this.itemSlot);
    }
  }

  private void menuStage2NavigatePageUp() {
    if(this.itemScroll - 4 >= 0) {
      this.scroll(this.itemScroll - 4);
    } else if(this.itemScroll != 0) {
      this.scroll(0);
    }
  }

  private void menuStage2NavigatePageDown() {
    if(this.itemScroll + 4 <= this.itemCount - 5) {
      this.scroll(this.itemScroll + 4);
    } else if(this.itemCount > 5 && this.itemScroll != this.itemCount - 5) {
      this.scroll(this.itemCount - 5);
    }
  }

  private void menuStage2NavigateHome() {
    if(this.itemSlot > 0 || this.itemScroll > 0) {
      this.itemSlot = 0;
      this.scroll(0);
    }
  }

  private void menuStage2NavigateEnd() {
    if(this.itemCount > 0 && this.itemScroll + this.itemSlot != this.itemCount - 1) {
      this.itemSlot = Math.min(4, this.itemCount - 1);
      this.scroll(this.itemCount - 1 - this.itemSlot);
    }
  }

  private void menuStage2Select() {
    if(this.itemScroll + this.itemSlot >= this.itemCount) {
      playMenuSound(40);
      return;
    }

    final ItemStack stack = this.menuItems.get(this.itemSlot + this.itemScroll).item_00;
    this.itemTargetAll = stack.canTarget(Item.TargetType.ALL);

    if(!stack.canBeUsed(Item.UsageLocation.MENU) || (this.menuItems.get(this.itemSlot + this.itemScroll).flags_02 & 0x4000) != 0) {
      playMenuSound(40);
      return;
    }

    playMenuSound(2);
    this.loadingStage = 3;
  }

  private void menuStage3Escape() {
    playMenuSound(3);
    this.loadingStage = 2;
  }

  private void menuStage3NavigateLeft() {
    if(!this.itemTargetAll) {
      playMenuSound(1);

      if(this.charSlot > 0) {
        this.charSlot--;

        if(this.charScroll > this.charSlot) {
          this.charScroll = this.charSlot;
        }
      } else {
        this.charSlot = characterIndices_800bdbb8.size() - 1;
        this.charScroll = Math.max(0, characterIndices_800bdbb8.size() - CHAR_SLOTS);
      }
    }
  }

  private void menuStage3NavigateRight() {
    if(!this.itemTargetAll) {
      playMenuSound(1);

      if(this.charSlot < characterIndices_800bdbb8.size() - 1) {
        this.charSlot++;

        if(this.charScroll < this.charSlot - CHAR_SLOTS + 1) {
          this.charScroll = this.charSlot - CHAR_SLOTS + 1;
        }
      } else {
        this.charSlot = 0;
        this.charScroll = 0;
      }
    }
  }

  private void menuStage3Select() {
    if(!this.itemTargetAll) {
      this.menuItems.get(this.itemSlot + this.itemScroll).item_00.useInMenu(this.useItemResponse, characterIndices_800bdbb8.getInt(this.charSlot));
    } else {
      boolean success = false;

      for(int i = 0; i < characterIndices_800bdbb8.size(); i++) {
        this.menuItems.get(this.itemSlot + this.itemScroll).item_00.useInMenu(this.useItemResponse, characterIndices_800bdbb8.getInt(i));

        if(this.useItemResponse.success) {
          success = true;
        }
      }

      this.useItemResponse.success = success;
    }

    if(!this.useItemResponse.success) {
      playMenuSound(40);
      return;
    }

    playMenuSound(2);
    takeItemFromSlot(this.menuItems.get(this.itemSlot + this.itemScroll).itemSlot_01, 1);
    this.itemCount = this.getUsableItemsInMenu();

    if(this.itemScroll == 0 && this.itemSlot > this.itemCount - 1) {
      this.itemSlot = Math.max(0, --this.itemSlot);
    }

    if(this.useItemResponse.text != null) {
      final String text = this.useItemResponse.text;
      this.deferAction(() -> menuStack.pushScreen(new MessageBoxScreen(text, MessageBoxType.ALERT, result -> {})));
    }

    this.useItemResponse.success = false;
    this.useItemResponse.text = null;
    this.loadingStage = 1;
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
