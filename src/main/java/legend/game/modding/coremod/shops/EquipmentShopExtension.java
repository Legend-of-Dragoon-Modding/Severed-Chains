package legend.game.modding.coremod.shops;

import legend.core.GameEngine;
import legend.core.platform.input.InputAction;
import legend.game.characters.CharacterData2c;
import legend.game.i18n.I18n;
import legend.game.inventory.EquipItemResult;
import legend.game.inventory.Equipment;
import legend.game.inventory.screens.InputPropagation;
import legend.game.inventory.screens.MessageBoxScreen;
import legend.game.inventory.screens.ShopExtension;
import legend.game.inventory.screens.ShopScreen;
import legend.game.inventory.screens.controls.AtlasIcon;
import legend.game.inventory.screens.controls.Glyph;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;
import legend.game.types.MessageBoxResult;
import legend.game.types.Renderable58;
import legend.game.types.Shop;

import static legend.game.SItem.UI_TEXT;
import static legend.game.SItem.allocateOneFrameGlyph;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.equipItem;
import static legend.game.SItem.fadeOutArrow;
import static legend.game.SItem.giveEquipment;
import static legend.game.SItem.initArrowRenderable;
import static legend.game.SItem.initHighlight;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.renderFraction;
import static legend.game.SItem.renderNumber;
import static legend.game.SItem.renderNumberComparison;
import static legend.game.SItem.setRandomRepeatGlyph;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Text.renderText;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_LEFT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_RIGHT;
import static legend.game.sound.Audio.playMenuSound;

public class EquipmentShopExtension extends ShopExtension<Equipment> {
  private static final int PORTRAIT_COUNT = 7;

  private final AtlasIcon[] portraits;

  private int selectedCharSlot;
  private int charScroll;
  private Renderable58 leftArrowRenderable;
  private Renderable58 rightArrowRenderable;

  private final Glyph charHighlight = Glyph.uiElement(0x83, 0x83);

  private ShopScreen screen;
  private ShopScreen.ShopEntry<Equipment> entry;

  private boolean returnControl;

  public EquipmentShopExtension() {
    initHighlight(this.charHighlight.getRenderable());
    this.charHighlight.hide();

    this.portraits = new AtlasIcon[characterIndices_800bdbb8.size()];

    for(int i = 0; i < this.portraits.length; i++) {
      this.portraits[i] = new AtlasIcon();
      this.portraits[i].setPos(9 + i * 50, 174);
      this.portraits[i].setSize(48, 48);
      this.portraits[i].setZ(31);
      this.portraits[i].setIcon(GameEngine.getTextureAtlas().getIcon(gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.getInt(i)).template.getRegistryId()));
      this.portraits[i].hide();

      final int finalI = i;
      this.portraits[i].onMouseMove((x, y) -> {
        if(this.selectedCharSlot != finalI) {
          playMenuSound(1);
          this.selectedCharSlot = finalI;
          final AtlasIcon portrait = this.portraits[this.selectedCharSlot];
          this.charHighlight.setX(portrait.getX() + 8);
          return InputPropagation.HANDLED;
        }

        return InputPropagation.PROPAGATE;
      });

      final int finalI1 = i;
      this.portraits[i].onMouseClick((x, y, button, mods) -> {
        this.selectedCharSlot = finalI1;
        final AtlasIcon portrait = this.portraits[this.selectedCharSlot];
        this.charHighlight.setX(portrait.getX() + 8);
        this.menuSelectChar5Select();
        return InputPropagation.HANDLED;
      });
    }
  }

  @Override
  public boolean accepts(final ShopScreen.ShopEntry<?> entry) {
    return entry.item instanceof Equipment;
  }

  @Override
  public String getName(final ShopScreen.ShopEntry<Equipment> entry) {
    return I18n.translate("lod_core.ui.shop.equipment");
  }

  @Override
  public void attach(final ShopScreen screen, final Shop shop, final GameState52c gameState) {
    for(int i = 0; i < this.portraits.length; i++) {
      screen.addControl(this.portraits[i]);
    }

    screen.addControl(this.charHighlight);
  }

  @Override
  public void activate(final ShopScreen screen, final Shop shop, final GameState52c gameState, final ShopScreen.ShopEntry<Equipment> entry) {
    this.returnControl = false;

    for(int i = 0; i < characterIndices_800bdbb8.size(); i++) {
      final CharacterData2c character = gameState.charData_32c.get(characterIndices_800bdbb8.getInt(i));
      this.portraits[i].setVisibility(character.canEquip(entry.item.slot, entry.item));
    }

    this.selectedCharSlot = this.getFirstEquippableCharSlot();
    this.scrollSelectedIntoView(entry);
  }

  @Override
  public void deactivate(final ShopScreen screen, final Shop shop, final GameState52c gameState) {
    for(int i = 0; i < this.portraits.length; i++) {
      this.portraits[i].hide();
    }

    this.charHighlight.hide();

    if(this.leftArrowRenderable != null) {
      fadeOutArrow(this.leftArrowRenderable);
      this.leftArrowRenderable = null;
    }

    if(this.rightArrowRenderable != null) {
      fadeOutArrow(this.rightArrowRenderable);
      this.rightArrowRenderable = null;
    }
  }

  @Override
  public void drawShopHeader(final ShopScreen screen, final Shop shop, final GameState52c gameState, final ShopScreen.ShopEntry<Equipment> entry, final int x, final int y) {
    super.drawShopHeader(screen, shop, gameState, entry, x, y);
    renderFraction(x + 120, y + 8, gameState.equipment_1e8.size(), 255);
  }

  @Override
  public void drawShopDetails(final ShopScreen screen, final Shop shop, final GameState52c gameState, final ShopScreen.ShopEntry<Equipment> entry) {
    this.drawArrows();

    final int charId = this.selectedCharSlot != -1 ? characterIndices_800bdbb8.getInt(this.selectedCharSlot) : -1;

    if(charId != -1) {
      final CharacterData2c character = gameState.charData_32c.get(charId);
      final CharacterData2c clone = character.template.make(gameState);
      clone.set(character);

      int equipmentAttack = 0;
      int equipmentDefense = 0;
      int equipmentMagicAttack = 0;
      int equipmentMagicDefense = 0;

      for(final EquipmentSlot slot : EquipmentSlot.values()) {
        final Equipment equipped = character.getEquipment(slot);

        if(equipped != null) {
          equipmentAttack += equipped.attack1_0a + equipped.attack2_10;
          equipmentDefense += equipped.defence_12;
          equipmentMagicAttack += equipped.magicAttack_11;
          equipmentMagicDefense += equipped.magicDefence_13;
        }
      }

      if(equipItem(entry.item, charId).success) {
        int newEquipmentAttack = 0;
        int newEquipmentDefense = 0;
        int newEquipmentMagicAttack = 0;
        int newEquipmentMagicDefense = 0;

        for(final EquipmentSlot slot : EquipmentSlot.values()) {
          final Equipment equipped = character.getEquipment(slot);

          if(equipped != null) {
            newEquipmentAttack += equipped.attack1_0a + equipped.attack2_10;
            newEquipmentDefense += equipped.defence_12;
            newEquipmentMagicAttack += equipped.magicAttack_11;
            newEquipmentMagicDefense += equipped.magicDefence_13;
          }
        }

        allocateOneFrameGlyph(0x67, 210, 127);
        allocateOneFrameGlyph(0x68, 210, 137);
        allocateOneFrameGlyph(0x69, 210, 147);
        allocateOneFrameGlyph(0x6a, 210, 157);

        allocateOneFrameGlyph(0x6b, 274, 127);
        allocateOneFrameGlyph(0x6b, 274, 137);
        allocateOneFrameGlyph(0x6b, 274, 147);
        allocateOneFrameGlyph(0x6b, 274, 157);

        renderNumber(246, 127, equipmentAttack, 0x2, 4);
        renderNumber(246, 137, equipmentDefense, 0x2, 4);
        renderNumber(246, 147, equipmentMagicAttack, 0x2, 4);
        renderNumber(246, 157, equipmentMagicDefense, 0x2, 4);

        renderNumberComparison(284, 127, equipmentAttack, newEquipmentAttack, 4);
        renderNumberComparison(284, 137, equipmentDefense, newEquipmentDefense, 4);
        renderNumberComparison(284, 147, equipmentMagicAttack, newEquipmentMagicAttack, 4);
        renderNumberComparison(284, 157, equipmentMagicDefense, newEquipmentMagicDefense, 4);
      } else {
        renderText(I18n.translate("lod_core.ui.shop.cannot_equip"), 228, 137, UI_TEXT);
      }

      character.set(clone);
    } else {
      renderText(I18n.translate("lod_core.ui.shop.cannot_equip"), 228, 137, UI_TEXT);
    }
  }

  private void drawArrows() {
    setRandomRepeatGlyph(this.leftArrowRenderable, 0x2d, 0x34, 0xaa, 0xb1);
    setRandomRepeatGlyph(this.rightArrowRenderable, 0x25, 0x2c, 0xa2, 0xa9);

    if(this.charScroll > 0) {
      if(this.leftArrowRenderable == null) {
        final Renderable58 renderable = allocateUiElement(0x6f, 0x6c, 18, 173);
        renderable.repeatStartGlyph_18 = 0x2d;
        renderable.repeatEndGlyph_1c = 0x34;
        this.leftArrowRenderable = renderable;
        initArrowRenderable(renderable);
      }
    } else {
      if(this.leftArrowRenderable != null) {
        fadeOutArrow(this.leftArrowRenderable);
        this.leftArrowRenderable = null;
      }
    }

    if(characterIndices_800bdbb8.size() - this.charScroll > PORTRAIT_COUNT) {
      if(this.rightArrowRenderable == null) {
        final Renderable58 renderable = allocateUiElement(0x6f, 0x6c, 350, 173);
        renderable.repeatStartGlyph_18 = 0x25;
        renderable.repeatEndGlyph_1c = 0x2c;
        this.rightArrowRenderable = renderable;
        initArrowRenderable(renderable);
      }
    } else if(this.rightArrowRenderable != null) {
      fadeOutArrow(this.rightArrowRenderable);
      this.rightArrowRenderable = null;
    }
  }

  @Override
  public boolean selectEntry(final ShopScreen screen, final Shop shop, final GameState52c gameState, final ShopScreen.ShopEntry<Equipment> entry, final int index) {
    this.screen = screen;
    this.entry = entry;
    this.selectedCharSlot = this.getFirstEquippableCharSlot();
    this.charScroll = Math.max(0, this.selectedCharSlot - PORTRAIT_COUNT);
    this.returnControl = false;

    if(gameState_800babc8.equipment_1e8.size() >= 255) {
      screen.deferAction(() -> menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.shop.inventory_full"), 0, result -> { })));
      return false;
    }

    if(gameState_800babc8.gold_94 < entry.price) {
      screen.deferAction(() -> menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.shop.not_enough_gold"), 0, result -> { })));
      return false;
    }

    if(this.selectedCharSlot != -1) {
      final AtlasIcon portrait = this.portraits[this.selectedCharSlot];
      this.charHighlight.setPos(portrait.getX() + 8, portrait.getY());
      this.charHighlight.show();
    } else {
      this.menuSelectChar5Select();
    }

    return true;
  }

  @Override
  public boolean shouldReturnControl() {
    return this.returnControl;
  }

  private int getFirstEquippableCharSlot() {
    for(int i = 0; i < this.portraits.length; i++) {
      if(this.portraits[i].isVisible()) {
        return i;
      }
    }

    return -1;
  }

  @Override
  public InputPropagation inputActionPressed(final ShopScreen screen, final Shop shop, final GameState52c gameState, final ShopScreen.ShopEntry<Equipment> entry, final int index, final InputAction action, final boolean repeat) {
    if(action == INPUT_ACTION_MENU_LEFT.get()) {
      this.menuSelectChar5NavigateLeft(entry);
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_RIGHT.get()) {
      this.menuSelectChar5NavigateRight(entry);
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_BACK.get() && !repeat) {
      this.menuSelectChar5Escape();
      return InputPropagation.HANDLED;
    }

    if(action == INPUT_ACTION_MENU_CONFIRM.get() && !repeat) {
      this.menuSelectChar5Select();
      return InputPropagation.HANDLED;
    }

    return InputPropagation.PROPAGATE;
  }

  private void scrollSelectedIntoView(final ShopScreen.ShopEntry<Equipment> entry) {
    if(this.selectedCharSlot != -1) {
      if(this.selectedCharSlot < this.charScroll) {
        this.charScroll = this.selectedCharSlot;
      }

      if(this.selectedCharSlot - this.charScroll >= PORTRAIT_COUNT) {
        this.charScroll = this.selectedCharSlot - PORTRAIT_COUNT + 1;
      }
    } else {
      this.charScroll = 0;
    }

    for(int i = 0; i < characterIndices_800bdbb8.size(); i++) {
      final CharacterData2c character = gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.getInt(i));
      this.portraits[i].setVisibility(i >= this.charScroll && i < PORTRAIT_COUNT + this.charScroll && character.canEquip(entry.item.slot, entry.item));
      this.portraits[i].setX(9 + (i - this.charScroll) * 50);
    }
  }

  private void menuSelectChar5NavigateLeft(final ShopScreen.ShopEntry<Equipment> entry) {
    playMenuSound(1);

    if(this.selectedCharSlot > 0) {
      this.selectedCharSlot--;
    } else {
      this.selectedCharSlot = characterIndices_800bdbb8.size() - 1;
    }

    this.scrollSelectedIntoView(entry);
    final AtlasIcon portrait = this.portraits[this.selectedCharSlot];
    this.charHighlight.setX(portrait.getX() + 8);
  }

  private void menuSelectChar5NavigateRight(final ShopScreen.ShopEntry<Equipment> entry) {
    playMenuSound(1);

    if(this.selectedCharSlot < characterIndices_800bdbb8.size() - 1) {
      this.selectedCharSlot++;
    } else {
      this.selectedCharSlot = 0;
    }

    this.scrollSelectedIntoView(entry);
    final AtlasIcon portrait = this.portraits[this.selectedCharSlot];
    this.charHighlight.setX(portrait.getX() + 8);
  }

  private void menuSelectChar5Escape() {
    playMenuSound(3);
    this.charHighlight.hide();
    this.returnControl = true;
  }

  private void menuSelectChar5Select() {
    playMenuSound(2);

    menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.shop.buy", I18n.translate(this.entry.item.getNameTranslationKey())), 2, result -> {
      if(result == MessageBoxResult.YES) {
        final CharacterData2c character = gameState_800babc8.charData_32c.get(characterIndices_800bdbb8.getInt(this.selectedCharSlot));
        if(this.selectedCharSlot != -1 && character.canEquip(this.entry.item.slot, this.entry.item)) {
          menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.shop.equip", I18n.translate(this.entry.item.getNameTranslationKey())), 2, result1 -> {
            if(result1 == MessageBoxResult.YES) {
              final EquipItemResult equipResult = equipItem(this.entry.item, characterIndices_800bdbb8.getInt(this.selectedCharSlot));

              if(equipResult.previousEquipment != null) {
                if(equipResult.success) {
                  if(giveEquipment(equipResult.previousEquipment)) {
                    gameState_800babc8.gold_94 -= this.entry.price;
                  } else {
                    equipItem(equipResult.previousEquipment, characterIndices_800bdbb8.getInt(this.selectedCharSlot));
                    this.screen.deferAction(() -> menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.shop.inventory_full"), 0, onResult -> {})));
                  }
                } else {
                  equipItem(equipResult.previousEquipment, characterIndices_800bdbb8.getInt(this.selectedCharSlot));
                  this.screen.deferAction(() -> menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.shop.failed_to_equip", I18n.translate(this.entry.item.getNameTranslationKey())), 0, onResult -> {})));
                }
              }
            } else {
              this.giveUnequipped(this.screen, this.entry);
            }

            this.returnControl = true;
          }));
        } else {
          this.giveUnequipped(this.screen, this.entry);
          this.returnControl = true;
        }
      } else if(this.selectedCharSlot == -1) {
        this.menuSelectChar5Escape();
      }
    }));
  }

  private void giveUnequipped(final ShopScreen screen, final ShopScreen.ShopEntry<Equipment> shopEntry) {
    if(giveEquipment(shopEntry.item)) {
      gameState_800babc8.gold_94 -= shopEntry.price;
    } else {
      screen.deferAction(() -> menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.shop.inventory_full"), 0, onResult -> { })));
    }
  }
}
