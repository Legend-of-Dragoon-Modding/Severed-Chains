package legend.game.combat.ui;

import legend.game.additions.Addition;
import legend.game.additions.CharacterAdditionStats;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.i18n.I18n;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.HorizontalAlign;
import legend.game.inventory.screens.TextColour;
import legend.game.scripting.RunningScript;
import legend.game.types.ActiveStatsa0;
import legend.game.types.CharacterData2c;
import legend.game.ui.UiBox;

import java.util.ArrayList;
import java.util.List;

import static legend.core.GameEngine.CONFIG;
import static legend.game.SItem.loadAdditions;
import static legend.game.SItem.loadCharacterStats;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Text.renderText;
import static legend.game.combat.SBtld.loadAdditions;
import static legend.lodmod.LodConfig.UI_COLOUR;

public class AdditionListMenu extends ListMenu {
  private final FontOptions fontOptions = new FontOptions().colour(TextColour.WHITE);

  private UiBox description;

  private final List<Addition> menuAdditions = new ArrayList<>();

  public AdditionListMenu(final BattleHud hud, final PlayerBattleEntity activePlayer, final ListPosition lastPosition, final Runnable onClose) {
    super(hud, activePlayer, 186, lastPosition, onClose);
    loadAdditions(activePlayer.charId_272, this.menuAdditions);

    final CharacterData2c charData = gameState_800babc8.charData_32c[activePlayer.charId_272];

    int index = 0;
    for(int i = 0; i < this.menuAdditions.size(); i++) {
      final Addition additionDelegate = this.menuAdditions.get(i);
      if(additionDelegate.getRegistryId().equals(charData.selectedAddition_19)) {
        index = i;
      }
    }

    if(index > 6) {
      lastPosition.lastListIndex_26 = 6;
      lastPosition.lastListScroll_28 = index - 6;
    } else {
      lastPosition.lastListIndex_26 = index;
      lastPosition.lastListScroll_28 = 0;
    }
  }

  @Override
  protected int getListCount() {
    return this.menuAdditions.size();
  }

  @Override
  protected void drawListEntry(final int index, final int x, final int y, final int trim) {
    final CharacterData2c charData = gameState_800babc8.charData_32c[this.player_08.charId_272];
    final CharacterAdditionStats additionStats = charData.additionStats.get(this.menuAdditions.get(index).getRegistryId());

    this.fontOptions.trim(trim);
    this.fontOptions.horizontalAlign(HorizontalAlign.LEFT);
    renderText(I18n.translate(this.menuAdditions.get(index)), x, y, this.fontOptions);
    renderText("/", x + 146, y, this.fontOptions);

    this.fontOptions.horizontalAlign(HorizontalAlign.RIGHT);
    renderText(String.valueOf(additionStats.xp), x + 145, y, this.fontOptions);

    final String max;
    if(additionStats.level < 4) {
      max = String.valueOf((additionStats.level + 1) * 20);
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
    charData.selectedAddition_19 = this.menuAdditions.get(index).getRegistryId();
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

  @Override
  public void draw() {
    super.draw();

    if(this.menuState_00 != 0 && (this.flags_02 & 0x1) != 0) {
      //LAB_800f5f50
      if((this.flags_02 & 0x40) != 0) {
        final int listIndex = this.listScroll_1e + this.listIndex_24;
        final Addition addition = this.menuAdditions.get(listIndex);
        final CharacterData2c charData = gameState_800babc8.charData_32c[this.player_08.charId_272];
        final CharacterAdditionStats additionStats = charData.additionStats.get(addition.getRegistryId());
        final int damage = addition.getDamage(gameState_800babc8, charData, additionStats);
        final int sp = addition.getSp(gameState_800babc8, charData, additionStats);

        //Selected item description
        if(this.description == null) {
          this.description = new UiBox("Battle UI Addition Description", 44, 156, 232, 14);
        }

        this.description.render(CONFIG.getConfig(UI_COLOUR.get()));

        this.fontOptions.trim(0);
        this.fontOptions.horizontalAlign(HorizontalAlign.CENTRE);
        renderText("Hits: " + addition.getHitCount(gameState_800babc8, charData, additionStats) + ", damage: " + damage + ", SP: " + sp, 160, 157, this.fontOptions);
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
