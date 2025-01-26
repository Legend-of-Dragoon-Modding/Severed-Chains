package legend.game.combat.ui;

import legend.core.Config;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.HorizontalAlign;
import legend.game.inventory.screens.TextColour;
import legend.game.scripting.RunningScript;
import legend.game.types.ActiveStatsa0;
import legend.game.types.AdditionData0e;
import legend.game.types.CharacterData2c;
import legend.game.types.MenuAdditionInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static legend.game.SItem.additions_80114070;
import static legend.game.SItem.loadAdditions;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_8004.additionCounts_8004f5c0;
import static legend.game.Scus94491BpeSegment_8004.additionOffsets_8004f5ac;
import static legend.game.Scus94491BpeSegment_8005.additionData_80052884;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.combat.Battle.additionNames_800fa8d4;
import static legend.game.combat.SBtld.loadAdditions;

public class AdditionListMenu extends ListMenu {
  private final FontOptions fontOptions = new FontOptions().colour(TextColour.WHITE);

  private UiBox description;

  private final List<String> additions = new ArrayList<>();
  private final MenuAdditionInfo[] menuAdditions = new MenuAdditionInfo[9];

  public AdditionListMenu(final BattleHud hud, final PlayerBattleEntity activePlayer, final ListPosition lastPosition, final Runnable onClose) {
    super(hud, activePlayer, 186, modifyLastPosition(activePlayer, lastPosition), onClose);
    this.prepareAdditionList(activePlayer.charId_272);
    Arrays.setAll(this.menuAdditions, i -> new MenuAdditionInfo());
    loadAdditions(activePlayer.charId_272, this.menuAdditions);
  }

  private static ListPosition modifyLastPosition(final PlayerBattleEntity player, final ListPosition lastPosition) {
    final CharacterData2c charData = gameState_800babc8.charData_32c[player.charId_272];
    final int index = charData.selectedAddition_19 - additionOffsets_8004f5ac[player.charId_272];

    if(index > 6) {
      lastPosition.lastListIndex_26 = 6;
      lastPosition.lastListScroll_28 = index - 6;
    } else {
      lastPosition.lastListIndex_26 = index;
      lastPosition.lastListScroll_28 = 0;
    }

    return lastPosition;
  }

  @Override
  protected int getListCount() {
    return this.additions.size();
  }

  @Override
  protected void drawListEntry(final int index, final int x, final int y, final int trim) {
    final CharacterData2c charData = gameState_800babc8.charData_32c[this.player_08.charId_272];

    this.fontOptions.trim(trim);
    this.fontOptions.horizontalAlign(HorizontalAlign.LEFT);
    renderText(this.additions.get(index), x, y, this.fontOptions);
    renderText("/", x + 146, y, this.fontOptions);

    this.fontOptions.horizontalAlign(HorizontalAlign.RIGHT);
    renderText(String.valueOf(charData.additionXp_22[index]), x + 145, y, this.fontOptions);

    final String max;
    if(charData.additionLevels_1a[index] < 5) {
      max = String.valueOf(charData.additionLevels_1a[index] * 20);
    } else {
      max = "-";
    }

    renderText(max, x + 168, y, this.fontOptions);
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
        final int offset = this.menuAdditions[listIndex].offset_00;
        final int index = this.menuAdditions[listIndex].index_01;
        final CharacterData2c charData = gameState_800babc8.charData_32c[this.player_08.charId_272];
        final int level = charData.additionLevels_1a[index];
        final AdditionData0e additionData = additionData_80052884[offset];
        final int damage = additionData.damage_0c * (additions_80114070[offset][level].damageMultiplier_03 + 100) / 100;
        final int sp = additionData.sp_02[level - 1];

        //Selected item description
        if(this.description == null) {
          this.description = new UiBox("Battle UI Addition Description", 44, 156, 232, 14);
        }

        this.description.render(Config.changeBattleRgb() ? Config.getBattleRgb() : Config.defaultUiColour);

        this.fontOptions.trim(0);
        this.fontOptions.horizontalAlign(HorizontalAlign.CENTRE);
        renderText("Hits: " + additionData.attacks_01 + ", damage: " + damage + ", SP: " + sp, 160, 157, this.fontOptions);
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
