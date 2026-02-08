package legend.game.modding.coremod.shops;

import legend.core.platform.input.InputAction;
import legend.game.i18n.I18n;
import legend.game.inventory.EquipItemResult;
import legend.game.inventory.Equipment;
import legend.game.inventory.screens.InputPropagation;
import legend.game.inventory.screens.MessageBoxScreen;
import legend.game.inventory.screens.ShopExtension;
import legend.game.inventory.screens.ShopScreen;
import legend.game.inventory.screens.controls.CharacterPortrait;
import legend.game.inventory.screens.controls.Glyph;
import legend.game.types.ActiveStatsa0;
import legend.game.types.EquipmentSlot;
import legend.game.types.GameState52c;
import legend.game.types.MessageBoxResult;
import legend.game.types.Renderable58;
import legend.game.types.Shop;

import java.util.EnumMap;
import java.util.Map;

import static legend.game.SItem.UI_TEXT;
import static legend.game.SItem.allocateOneFrameGlyph;
import static legend.game.SItem.allocateUiElement;
import static legend.game.SItem.canEquip;
import static legend.game.SItem.characterCount_8011d7c4;
import static legend.game.SItem.equipItem;
import static legend.game.SItem.fadeOutArrow;
import static legend.game.SItem.giveEquipment;
import static legend.game.SItem.initArrowRenderable;
import static legend.game.SItem.initHighlight;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.SItem.menuStack;
import static legend.game.SItem.renderThreeDigitNumber;
import static legend.game.SItem.renderThreeDigitNumberComparison;
import static legend.game.SItem.setRandomRepeatGlyph;
import static legend.game.Scus94491BpeSegment_800b.characterIndices_800bdbb8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Text.renderText;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_BACK;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_CONFIRM;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_LEFT;
import static legend.game.modding.coremod.CoreMod.INPUT_ACTION_MENU_RIGHT;
import static legend.game.sound.Audio.playMenuSound;

public class EquipmentShopExtension extends ShopExtension<Equipment> {
  private static final int PORTRAIT_COUNT = 7;

  private final CharacterPortrait[] portraits;

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

    this.portraits = new CharacterPortrait[characterIndices_800bdbb8.length];

    for(int i = 0; i < this.portraits.length; i++) {
      this.portraits[i] = new CharacterPortrait();
      this.portraits[i].setPos(9 + i * 50, 174);
      this.portraits[i].setCharId(characterIndices_800bdbb8[i]);
      this.portraits[i].hide();

      final int finalI = i;
      this.portraits[i].onMouseMove((x, y) -> {
        if(this.selectedCharSlot != finalI) {
          playMenuSound(1);
          this.selectedCharSlot = finalI;
          final CharacterPortrait portrait = this.portraits[this.selectedCharSlot];
          this.charHighlight.setX(portrait.getX() + 8);
          return InputPropagation.HANDLED;
        }

        return InputPropagation.PROPAGATE;
      });

      final int finalI1 = i;
      this.portraits[i].onMouseClick((x, y, button, mods) -> {
        this.selectedCharSlot = finalI1;
        final CharacterPortrait portrait = this.portraits[this.selectedCharSlot];
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

    for(int i = 0; i < characterCount_8011d7c4; i++) {
      this.portraits[i].setVisibility(characterIndices_800bdbb8[i] != -1 && canEquip(entry.item, characterIndices_800bdbb8[i]));
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
  public void drawShopDetails(final ShopScreen screen, final Shop shop, final GameState52c gameState, final ShopScreen.ShopEntry<Equipment> entry) {
    this.drawArrows();

    final int charId = this.selectedCharSlot != -1 ? characterIndices_800bdbb8[this.selectedCharSlot] : -1;

    if(charId != -1) {
      final ActiveStatsa0 oldStats = new ActiveStatsa0(stats_800be5f8[charId]);

      final Map<EquipmentSlot, Equipment> oldEquipment = new EnumMap<>(gameState.charData_32c[charId].equipment_14);

      if(equipItem(entry.item, charId).success) {
        allocateOneFrameGlyph(0x67, 210, 127);
        allocateOneFrameGlyph(0x68, 210, 137);
        allocateOneFrameGlyph(0x69, 210, 147);
        allocateOneFrameGlyph(0x6a, 210, 157);
        final ActiveStatsa0 newStats = stats_800be5f8[charId];
        renderThreeDigitNumber(246, 127, newStats.equipmentAttack_88, 0x2);
        renderThreeDigitNumber(246, 137, newStats.equipmentDefence_8c, 0x2);
        renderThreeDigitNumber(246, 147, newStats.equipmentMagicAttack_8a, 0x2);
        renderThreeDigitNumber(246, 157, newStats.equipmentMagicDefence_8e, 0x2);
        allocateOneFrameGlyph(0x6b, 274, 127);
        allocateOneFrameGlyph(0x6b, 274, 137);
        allocateOneFrameGlyph(0x6b, 274, 147);
        allocateOneFrameGlyph(0x6b, 274, 157);
        loadCharacterStats();
        renderThreeDigitNumberComparison(284, 127, oldStats.equipmentAttack_88, newStats.equipmentAttack_88);
        renderThreeDigitNumberComparison(284, 137, oldStats.equipmentDefence_8c, newStats.equipmentDefence_8c);
        renderThreeDigitNumberComparison(284, 147, oldStats.equipmentMagicAttack_8a, newStats.equipmentMagicAttack_8a);
        renderThreeDigitNumberComparison(284, 157, oldStats.equipmentMagicDefence_8e, newStats.equipmentMagicDefence_8e);
      } else {
        renderText(I18n.translate("lod_core.ui.shop.cannot_equip"), 228, 137, UI_TEXT);
      }

      gameState.charData_32c[charId].equipment_14.clear();
      gameState.charData_32c[charId].equipment_14.putAll(oldEquipment);

      loadCharacterStats();
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

    if(characterCount_8011d7c4 - this.charScroll > PORTRAIT_COUNT) {
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
      final CharacterPortrait portrait = this.portraits[this.selectedCharSlot];
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

    for(int i = 0; i < characterCount_8011d7c4; i++) {
      this.portraits[i].setVisibility(i >= this.charScroll && i < PORTRAIT_COUNT + this.charScroll && characterIndices_800bdbb8[i] != -1 && canEquip(entry.item, characterIndices_800bdbb8[i]));
      this.portraits[i].setX(9 + (i - this.charScroll) * 50);
    }
  }

  private void menuSelectChar5NavigateLeft(final ShopScreen.ShopEntry<Equipment> entry) {
    playMenuSound(1);

    if(this.selectedCharSlot > 0) {
      this.selectedCharSlot--;
    } else {
      this.selectedCharSlot = characterCount_8011d7c4 - 1;
    }

    this.scrollSelectedIntoView(entry);
    final CharacterPortrait portrait = this.portraits[this.selectedCharSlot];
    this.charHighlight.setX(portrait.getX() + 8);
  }

  private void menuSelectChar5NavigateRight(final ShopScreen.ShopEntry<Equipment> entry) {
    playMenuSound(1);

    if(this.selectedCharSlot < characterCount_8011d7c4 - 1) {
      this.selectedCharSlot++;
    } else {
      this.selectedCharSlot = 0;
    }

    this.scrollSelectedIntoView(entry);
    final CharacterPortrait portrait = this.portraits[this.selectedCharSlot];
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
        if(this.selectedCharSlot != -1 && canEquip(this.entry.item, characterIndices_800bdbb8[this.selectedCharSlot])) {
          menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.shop.equip", I18n.translate(this.entry.item.getNameTranslationKey())), 2, result1 -> {
            if(result1 == MessageBoxResult.YES) {
              final EquipItemResult equipResult = equipItem(this.entry.item, characterIndices_800bdbb8[this.selectedCharSlot]);

              if(equipResult.previousEquipment != null) {
                if(equipResult.success) {
                  if(giveEquipment(equipResult.previousEquipment)) {
                    gameState_800babc8.gold_94 -= this.entry.price;
                  } else {
                    equipItem(equipResult.previousEquipment, characterIndices_800bdbb8[this.selectedCharSlot]);
                    this.screen.deferAction(() -> menuStack.pushScreen(new MessageBoxScreen(I18n.translate("lod_core.ui.shop.inventory_full"), 0, onResult -> {})));
                  }
                } else {
                  equipItem(equipResult.previousEquipment, characterIndices_800bdbb8[this.selectedCharSlot]);
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
