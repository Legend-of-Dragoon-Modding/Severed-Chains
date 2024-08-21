package legend.game.combat.ui;

import legend.core.Config;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.inventory.screens.TextColour;
import legend.game.modding.coremod.CoreMod;
import legend.game.scripting.RunningScript;
import legend.game.types.ActiveStatsa0;
import legend.game.types.CharacterData2c;
import legend.game.types.MenuAdditionInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static legend.game.SItem.loadAdditions;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.Scus94491BpeSegment_8002.renderCentredText;
import static legend.game.Scus94491BpeSegment_8002.renderRightText;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_8004.additionCounts_8004f5c0;
import static legend.game.Scus94491BpeSegment_8004.additionOffsets_8004f5ac;
import static legend.game.Scus94491BpeSegment_8005.additionData_80052884;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.combat.Battle.additionNames_800fa8d4;
import static legend.game.combat.SBtld.loadAdditions;

public class AdditionListMenu extends ListMenu {
  private UiBox description;

  private final List<String> additions = new ArrayList<>();
  private final MenuAdditionInfo[] menuAdditions = new MenuAdditionInfo[9];

  public AdditionListMenu(final BattleHud hud, final PlayerBattleEntity activePlayer, final Runnable onClose) {
    super(hud, activePlayer, 186, onClose);
    this.prepareAdditionList(activePlayer.charId_272);
    Arrays.setAll(this.menuAdditions, i -> new MenuAdditionInfo());
    loadAdditions(activePlayer.charId_272, this.menuAdditions);
  }

  @Override
  protected int getListCount() {
    return this.additions.size();
  }

  @Override
  protected void drawListEntry(final int index, final int x, final int y, final int trim) {
    final CharacterData2c charData = gameState_800babc8.charData_32c[this.player_08.charId_272];

    renderText(this.additions.get(index), x, y, TextColour.WHITE, trim);
    renderRightText(String.valueOf(charData.additionXp_22[index]), x + 145, y, TextColour.WHITE, trim);
    renderText("/", x + 146, y, TextColour.WHITE, trim);

    final String max;
    if(charData.additionLevels_1a[index] < 5) {
      max = String.valueOf(charData.additionLevels_1a[index] * 20);
    } else {
      max = "-";
    }

    renderRightText(max, x + 168, y, TextColour.WHITE, trim);
  }

  @Override
  protected void onSelection(final int index) {

  }

  @Override
  protected void onUse(final int index) {
    final ActiveStatsa0 stats = stats_800be5f8[this.player_08.charId_272];
    final CharacterData2c charData = gameState_800babc8.charData_32c[this.player_08.charId_272];
    this.player_08.combatant_144.mrg_04 = null;
    charData.selectedAddition_19 = additionOffsets_8004f5ac[this.player_08.charId_272] + index;
    loadCharacterStats();
    this.player_08.additionSpMultiplier_11a = stats.additionSpMultiplier_9e;
    this.player_08.additionDamageMultiplier_11c = stats.additionDamageMultiplier_9f;
    loadAdditions();
    this.hud.battle.loadAttackAnimations(this.player_08.combatant_144);
    this.flags_02 &= ~0x8;
    this.menuState_00 = 8;
  }

  @Override
  protected void onClose() {

  }

  @Override
  protected int handleTargeting() {
    return 2;
  }

  @Override
  public void getTargetingInfo(final RunningScript<?> script) {

  }

  private void prepareAdditionList(final int charId) {
    //LAB_800f83dc
    this.additions.clear();

    //LAB_800f8420
    for(int additionSlot = 0; additionSlot < additionCounts_8004f5c0[charId]; additionSlot++) {
      final int additionOffset = additionOffsets_8004f5ac[charId];

      final int level = additionData_80052884[additionOffset + additionSlot].level_00;

      if(level == -1 && (gameState_800babc8.charData_32c[charId].partyFlags_04 & 0x40) != 0) {
        final String additionName = additionNames_800fa8d4[additionOffset + additionSlot];
        this.additions.add(additionName);
      } else if(level > 0 && level <= gameState_800babc8.charData_32c[charId].level_12) {
        final String additionName = additionNames_800fa8d4[additionOffset + additionSlot];
        this.additions.add(additionName);

        if(gameState_800babc8.charData_32c[charId].additionLevels_1a[additionSlot] == 0) {
          gameState_800babc8.charData_32c[charId].additionLevels_1a[additionSlot] = 1;
        }
      }
    }
  }

  @Override
  public void draw() {
    super.draw();

    if(this.menuState_00 != 0 && (this.flags_02 & 0x1) != 0) {
      //LAB_800f5f50
      if((this.flags_02 & 0x40) != 0) {
        final int listIndex = this.listScroll_1e + this.listIndex_24;
        final int index = this.menuAdditions[listIndex].index_01;
        final CharacterData2c charData = gameState_800babc8.charData_32c[this.player_08.charId_272];
        final int level = charData.additionLevels_1a[index];
        final int hits = CoreMod.CHARACTER_DATA[this.player_08.charId_272].getAdditionHitCount(listIndex);
        final int damage = CoreMod.CHARACTER_DATA[this.player_08.charId_272].getAdditionDamage(listIndex, level);
        final int sp = CoreMod.CHARACTER_DATA[this.player_08.charId_272].getAdditionLevelSp(listIndex, level);

        //Selected item description
        if(this.description == null) {
          this.description = new UiBox("Battle UI Addition Description", 44, 156, 232, 14);
        }

        this.description.render(Config.changeBattleRgb() ? Config.getBattleRgb() : Config.defaultUiColour);
        renderCentredText("Hits: " + hits + ", damage: " + damage + ", SP: " + sp, 160, 157, TextColour.WHITE, 0);
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
