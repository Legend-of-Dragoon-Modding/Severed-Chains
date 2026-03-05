package legend.game.combat.ui;

import legend.core.QueuedModelStandard;
import legend.game.characters.VitalsStat;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.types.DragoonSpells09;
import legend.game.i18n.I18n;
import legend.game.inventory.SpellStats0c;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.HorizontalAlign;
import legend.game.inventory.screens.TextColour;
import legend.game.modding.events.battle.SpellStatsEvent;
import legend.game.scripting.RunningScript;
import legend.game.ui.UiBox;
import legend.lodmod.LodMod;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Text.renderText;
import static legend.lodmod.LodConfig.UI_COLOUR;

public class SpellListMenu extends ListMenu {
  private final FontOptions fontOptions = new FontOptions().colour(TextColour.WHITE);

  private UiBox description;

  private final DragoonSpells09 spells;

  public SpellListMenu(final BattleHud hud, final PlayerBattleEntity activePlayer, final ListPosition lastPosition, final Runnable onClose) {
    super(hud, activePlayer, 186, modifyLastPosition(lastPosition), onClose);
    this.spells = this.hud.battle.dragoonSpells_800c6960.get(activePlayer.charSlot_276);
  }

  private static ListPosition modifyLastPosition(final ListPosition lastPosition) {
    lastPosition.lastListIndex_26 = 0;
    lastPosition.lastListScroll_28 = 0;
    return lastPosition;
  }

  @Override
  protected int getListCount() {
    return this.spells.spells_01.size();
  }

  @Override
  protected void drawListEntry(final int index, final int x, final int y, final int trim) {
    final TextColour textColour;
    this.setActiveSpell(index);
    if(!this.canUse()) {
      textColour = TextColour.GREY;
    } else {
      textColour = TextColour.WHITE;
    }

    final SpellStats0c spell = this.spells.spells_01.get(index);

    this.fontOptions.trim(trim);
    this.fontOptions.horizontalAlign(HorizontalAlign.LEFT);
    this.fontOptions.colour(textColour);
    renderText(I18n.translate(spell), x, y, this.fontOptions);
    this.fontOptions.horizontalAlign(HorizontalAlign.RIGHT);
    this.fontOptions.colour(TextColour.WHITE);
    renderText(String.valueOf(this.player_08.spell_94.mp_06), x + 152, y, this.fontOptions);

    this.transforms.scaling(0.75f);
    this.transforms.transfer.set(x + 152, y, 124.0f);
    RENDERER.queueOrthoModel(this.menuObj, this.transforms, QueuedModelStandard.class)
      .vertices(this.mpObjOffset, 4);
  }

  @Override
  protected void onSelection(final int index) {
    this.setActiveSpell(index);
  }

  @Override
  protected boolean canUse() {
    final int currentMP = this.player_08.stats.getStat(LodMod.MP_STAT.get()).getCurrent();
    final int spellMPCost = this.player_08.spell_94.mp_06;
    return currentMP >= spellMPCost;
  }

  private void setActiveSpell(final int index) {
    this.player_08.spell_94 = this.spells.spells_01.get(index);
  }

  @Override
  protected void onUse(final int index) {
    final VitalsStat mp = this.player_08.stats.getStat(LodMod.MP_STAT.get());
    mp.setCurrent(mp.getCurrent() - this.player_08.spell_94.mp_06);
  }

  @Override
  protected void onClose() {

  }

  @Override
  protected int handleTargeting() {
    final int itemTargetType = this.player_08.spell_94.targetType_00;
    final int targetType = (itemTargetType & 0x40) > 0 ? 1 : 0;
    final boolean targetAll = (itemTargetType & 0x8) != 0;
    return this.hud.handleTargeting(targetType, targetAll);
  }

  @Override
  public void getTargetingInfo(final RunningScript<?> script) {
    script.params_20[0].set(this.selectionState_a0);
    script.params_20[1].set(this.hud.battleMenu_800c6c34.target_48);
    script.params_20[2].set(this.player_08.spell_94.getRegistryId());
  }

  @Override
  public void draw() {
    super.draw();

    if(this.menuState_00 != 0 && (this.flags_02 & 0x1) != 0) {
      //LAB_800f5f50
      if((this.flags_02 & 0x40) != 0) {
        final SpellStats0c spell = EVENTS.postEvent(new SpellStatsEvent(this.spells.character_00, this.spells.spells_01.get(this.listScroll_1e + this.listIndex_24))).spell;

        //Selected item description
        if(this.description == null) {
          this.description = new UiBox(44, 156, 232, 14);
        }

        this.description.render(CONFIG.getConfig(UI_COLOUR.get()));

        this.fontOptions.trim(0);
        this.fontOptions.horizontalAlign(HorizontalAlign.CENTRE);
        renderText(I18n.translate(spell.getTranslationKey("description")), 160, 157, this.fontOptions);
      }
    }
  }
}
