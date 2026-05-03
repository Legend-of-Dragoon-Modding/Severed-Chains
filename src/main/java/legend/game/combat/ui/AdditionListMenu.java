package legend.game.combat.ui;

import legend.game.additions.Addition;
import legend.game.characters.CharacterAdditionInfo;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.HorizontalAlign;
import legend.game.inventory.screens.TextColour;
import legend.game.scripting.RunningScript;
import legend.game.ui.UiBox;
import org.legendofdragoon.modloader.registries.RegistryId;

import java.util.ArrayList;
import java.util.List;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.REGISTRIES;
import static legend.game.Text.renderText;
import static legend.lodmod.LodConfig.UI_COLOUR;

public class AdditionListMenu extends ListMenu {
  private final FontOptions fontOptions = new FontOptions().colour(TextColour.WHITE);

  private UiBox description;

  private final List<RegistryId> additionIds = new ArrayList<>();

  public AdditionListMenu(final BattleHud hud, final PlayerBattleEntity activePlayer, final ListPosition lastPosition, final Runnable onClose) {
    super(hud, activePlayer, 186, lastPosition, onClose);

    this.additionIds.addAll(activePlayer.character.getUnlockedAdditions());

    int index = 0;
    for(int i = 0; i < this.additionIds.size(); i++) {
      final RegistryId id = this.additionIds.get(i);
      if(id.equals(activePlayer.character.selectedAddition_19)) {
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
    return this.additionIds.size();
  }

  @Override
  protected void drawListEntry(final int index, final int x, final int y, final int trim) {
    final RegistryId additionId = this.additionIds.get(index);
    final CharacterAdditionInfo additionInfo = this.player_08.character.getAdditionInfo(additionId);
    final Addition addition = REGISTRIES.additions.getEntry(additionId).get();

    this.fontOptions.trim(trim);
    this.fontOptions.horizontalAlign(HorizontalAlign.LEFT);
    renderText(addition.getName(), x, y, this.fontOptions);
    renderText("/", x + 146, y, this.fontOptions);

    this.fontOptions.horizontalAlign(HorizontalAlign.RIGHT);
    renderText(String.valueOf(additionInfo.xp), x + 145, y, this.fontOptions);

    final String max;
    if(additionInfo.level < addition.getMaxLevel(this.player_08.character, additionInfo)) {
      max = String.valueOf(addition.getXpToNextLevel(this.player_08.character, additionInfo));
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
    this.player_08.combatant_144.mrg_04 = null;
    this.player_08.character.selectedAddition_19 = this.additionIds.get(index);
    this.player_08.addition = REGISTRIES.additions.getEntry(this.player_08.character.selectedAddition_19).get();
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
        final RegistryId additionId = this.additionIds.get(listIndex);
        final Addition addition = REGISTRIES.additions.getEntry(additionId).get();
        final CharacterAdditionInfo additionInfo = this.player_08.character.getAdditionInfo(additionId);
        final int damage = addition.getDamage(this.player_08.character, additionInfo);
        final int sp = addition.getSp(this.player_08.character, additionInfo);

        //Selected item description
        if(this.description == null) {
          this.description = new UiBox(44, 156, 232, 14);
        }

        this.description.render(CONFIG.getConfig(UI_COLOUR.get()));

        this.fontOptions.trim(0);
        this.fontOptions.horizontalAlign(HorizontalAlign.CENTRE);
        renderText("Hits: " + addition.getHitCount(this.player_08.character, additionInfo) + ", damage: " + damage + ", SP: " + sp, 160, 157, this.fontOptions);
      }
    }
  }
}
