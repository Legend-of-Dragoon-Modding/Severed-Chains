package legend.game.combat.ui;

import legend.core.Config;
import legend.core.QueuedModelStandard;
import legend.game.characters.VitalsStat;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.inventory.screens.TextColour;
import legend.game.modding.events.battle.SpellStatsEvent;
import legend.game.scripting.RunningScript;
import legend.game.types.SpellStats0c;
import legend.lodmod.LodMod;

import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment_8002.renderCentredText;
import static legend.game.Scus94491BpeSegment_8002.renderRightText;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.combat.Battle.spellStats_800fa0b8;

public class SpellListMenu extends ListMenu {
  private UiBox description;

  private final int listCount;

  public SpellListMenu(final BattleHud hud, final PlayerBattleEntity activePlayer, final ListPosition lastPosition, final Runnable onClose) {
    super(hud, activePlayer, 186, modifyLastPosition(lastPosition), onClose);

    int spellIndex;
    for(spellIndex = 0; spellIndex < 8; spellIndex++) {
      if(this.hud.battle.dragoonSpells_800c6960[activePlayer.charSlot_276].spellIndex_01[spellIndex] == -1) {
        break;
      }
    }

    this.listCount = spellIndex;
  }

  private static ListPosition modifyLastPosition(final ListPosition lastPosition) {
    lastPosition.lastListIndex_26 = 0;
    lastPosition.lastListScroll_28 = 0;
    return lastPosition;
  }

  @Override
  protected int getListCount() {
    return this.listCount;
  }

  @Override
  protected void drawListEntry(final int index, final int x, final int y, final int trim) {
    final int currentSpellId = this.player_08.spellId_4e;

    final TextColour textColour;
    this.setActiveSpell(index);
    if(this.player_08.stats.getStat(LodMod.MP_STAT.get()).getCurrent() < this.player_08.spell_94.mp_06) {
      textColour = TextColour.GREY;
    } else {
      textColour = TextColour.WHITE;
    }

    final int spellId = this.hud.battle.dragoonSpells_800c6960[this.player_08.charSlot_276].spellIndex_01[index];
    renderText(spellStats_800fa0b8[spellId].name, x, y, textColour, trim);
    renderRightText(String.valueOf(this.player_08.spell_94.mp_06), x + 152, y, TextColour.WHITE, trim);

    this.transforms.scaling(0.75f);
    this.transforms.transfer.set(x + 152, y, 124.0f);
    RENDERER.queueOrthoModel(this.menuObj, this.transforms, QueuedModelStandard.class)
      .vertices(this.mpObjOffset, 4);

    this.player_08.setActiveSpell(currentSpellId);
  }

  @Override
  protected void onSelection(final int index) {
    this.setActiveSpell(index);
  }

  private void setActiveSpell(final int index) {
    int spellId = this.hud.battle.dragoonSpells_800c6960[this.player_08.charSlot_276].spellIndex_01[index];

    if(this.player_08.charId_272 == 8) { // Miranda
      if(spellId == 65) {
        spellId = 10;
      }

      //LAB_800f5ab4
      if(spellId == 66) {
        spellId = 11;
      }

      //LAB_800f5ac0
      if(spellId == 67) {
        spellId = 12;
      }
    }

    //LAB_800f5acc
    this.player_08.setActiveSpell(spellId);
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

    int itemOrSpellId = this.player_08.spellId_4e;
    if(this.player_08.charId_272 == 8) {
      if(itemOrSpellId == 10) {
        itemOrSpellId = 65;
      }

      //LAB_800f46ec
      if(itemOrSpellId == 11) {
        itemOrSpellId = 66;
      }

      //LAB_800f46f8
      if(itemOrSpellId == 12) {
        itemOrSpellId = 67;
      }
    }

    //LAB_800f4704
    //LAB_800f4708
    script.params_20[1].set(this.hud.battleMenu_800c6c34.target_48);
    script.params_20[2].set(itemOrSpellId);
  }

  @Override
  public void draw() {
    super.draw();

    if(this.menuState_00 != 0 && (this.flags_02 & 0x1) != 0) {
      //LAB_800f5f50
      if((this.flags_02 & 0x40) != 0) {
        final int spellId = this.hud.battle.dragoonSpells_800c6960[this.player_08.charSlot_276].spellIndex_01[this.listScroll_1e + this.listIndex_24];
        final SpellStats0c spell = EVENTS.postEvent(new SpellStatsEvent(spellId, spellStats_800fa0b8[spellId])).spell;

        //Selected item description
        if(this.description == null) {
          this.description = new UiBox("Battle UI Spell Description", 44, 156, 232, 14);
        }

        this.description.render(Config.changeBattleRgb() ? Config.getBattleRgb() : Config.defaultUiColour);
        renderCentredText(spell.battleDescription, 160, 157, TextColour.WHITE, 0);
      }
    }
  }

  @Override
  public void delete() {
    super.delete();

    if(this.description != null) {
      this.description.delete();
      this.description = null;
    }
  }
}
