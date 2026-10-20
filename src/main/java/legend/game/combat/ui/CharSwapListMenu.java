package legend.game.combat.ui;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import legend.game.characters.CharacterData2c;
import legend.game.characters.VitalsStat;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.i18n.I18n;
import legend.game.inventory.screens.FontOptions;
import legend.game.inventory.screens.HorizontalAlign;
import legend.game.inventory.screens.TextColour;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptState;
import legend.game.ui.UiBox;
import legend.game.unpacker.Loader;

import java.util.concurrent.atomic.AtomicInteger;

import static legend.core.GameEngine.CONFIG;
import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Text.renderText;
import static legend.game.characters.CharacterData2c.IN_PARTY;
import static legend.game.combat.SBtld.loadAdditions;
import static legend.lodmod.LodConfig.UI_BACKGROUND_COLOUR;
import static legend.lodmod.LodMod.HP_STAT;
import static legend.lodmod.LodMod.MP_STAT;
import static legend.lodmod.LodMod.SP_STAT;

public class CharSwapListMenu extends ListMenu {
  private final FontOptions fontOptions = new FontOptions().colour(TextColour.WHITE);

  private UiBox description;

  private final IntList characters = new IntArrayList();

  private int oldBentSlot;
  private int oldPlayerSlot;

  public CharSwapListMenu(final BattleHud hud, final PlayerBattleEntity activePlayer, final ListPosition lastPosition, final Runnable onClose) {
    lastPosition.lastListIndex_26 = 0;
    lastPosition.lastListScroll_28 = 0;

    super(hud, activePlayer, 186, lastPosition, onClose);

    this.characters.addAll(getUsableCharacterList());
  }

  public static IntList getUsableCharacterList() {
    //LAB_800f83dc
    final IntList characters = new IntArrayList();

    for(int charIndex = 0; charIndex < gameState_800babc8.charData_32c.size(); charIndex++) {
      final CharacterData2c character = gameState_800babc8.charData_32c.get(charIndex);

      if((character.partyFlags_04 & IN_PARTY) != 0) {
        boolean foundInBattle = false;
        for(int i = 0; i < battleState_8006e398.getPlayerCount(); i++) {
          final PlayerBattleEntity bent = battleState_8006e398.playerBents_e40.get(i).innerStruct_00;
          if(character == bent.character) {
            foundInBattle = true;
            break;
          }
        }

        if(!foundInBattle) {
          characters.add(charIndex);
        }
      }
    }

    return characters;
  }

  @Override
  protected int getListCount() {
    return this.characters.size();
  }

  @Override
  protected void drawListEntry(final int index, final int x, final int y, final int trim) {
    final CharacterData2c character = gameState_800babc8.charData_32c.get(this.characters.getInt(index));

    this.fontOptions.trim(trim);
    this.fontOptions.horizontalAlign(HorizontalAlign.LEFT);
    this.fontOptions.size(1.0f);
    renderText(character.getName().get(), x, y, this.fontOptions);

    this.fontOptions.horizontalAlign(HorizontalAlign.RIGHT);
    this.fontOptions.size(0.75f);
    renderText(I18n.translate("lod.battle_actions.characters.level", character.level_12), x + 168, y + 2.0f, this.fontOptions);
  }

  @Override
  protected void onSelection(final int index) {

  }

  @Override
  protected void onUse(final int index) {
    this.hud.setBattleHudVisibility(false);
    final int removedState = this.removePlayer();
    this.addPlayer(removedState, this.characters.getInt(index));

    this.hud.deleteUiElements();

    this.flags_02 &= ~0x8;
    this.menuState_00 = 8;
  }

  public int removePlayer() {
    final var removedState = this.hud.battle.currentTurnBent_800c66c8;
    final int removedScriptStateId = removedState.index;
    final int combatantId = removedState.innerStruct_00.combatantIndex_26c;
    ((PlayerBattleEntity)removedState.innerStruct_00).character.status_10 = removedState.innerStruct_00.status_0e;
    removedState.destructor_0c = (a, b) -> {};
    removedState.innerStruct_00.model_148.deleteModelParts();

    this.oldBentSlot = removedState.innerStruct_00.allBentSlot_274;
    this.oldPlayerSlot = removedState.innerStruct_00.typeBentSlot_276;
    this.hud.battle.deallocateCombatant(removedState.innerStruct_00.combatant_144);
    this.hud.battle.removeCombatant(combatantId);
    removedState.deallocateWithChildren();
    return removedScriptStateId;
  }

  public void addPlayer(final int removedState, final int charIndex) {
    final int charSlot = removedState - 6;
    gameState_800babc8.charIds_88.set(charSlot, charIndex);

    loadAdditions();
    final int addedCombatantSlot = this.hud.battle.addCombatant(charIndex, charSlot);

    final AtomicInteger remaining = new AtomicInteger();
    final ScriptState<PlayerBattleEntity> bent = this.hud.battle.allocatePlayerBattleEntity(gameState_800babc8.charData_32c.get(charIndex), charSlot, charIndex, removedState, addedCombatantSlot, remaining);

    battleState_8006e398.playerBents_e40.set(this.oldPlayerSlot, bent);
    battleState_8006e398.allBents_e0c.set(this.oldBentSlot, bent);
    bent.innerStruct_00.allBentSlot_274 = this.oldBentSlot;
    bent.innerStruct_00.typeBentSlot_276 = this.oldPlayerSlot;

    this.hud.battle.initPlayerBattleEntityStats(bent.innerStruct_00);

    this.hud.battle.loadCombatantTmdAndAnims(this.hud.battle.getCombatant(addedCombatantSlot));
    bent.innerStruct_00.combatant_144.flags_19e |= 0x2a;

    Loader
      .loadFile(bent.innerStruct_00.character.getBattleTexturePath(bent.innerStruct_00))
      .thenAccept(files -> this.hud.battle.loadCharacterTim(files, charSlot))
    ;

    Loader
      .loadDirectory(bent.innerStruct_00.character.getBattleModelPath(battleState_8006e398.playerBents_e40.get(charSlot).innerStruct_00))
      .thenAccept(files -> this.hud.battle.loadCharTmdAndAnims(files, charSlot))
    ;

    this.hud.battle.loadAttackAnimations(this.hud.battle.getCombatant(addedCombatantSlot));
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

    if(this.menuState_00 != 0 && (this.flags_02 & 0x1) != 0 && (this.flags_02 & 0x40) != 0) {
      final int listIndex = this.listScroll_1e + this.listIndex_24;

      final CharacterData2c character = gameState_800babc8.charData_32c.get(this.characters.getInt(listIndex));
      final VitalsStat hp = character.stats.getStat(HP_STAT.get());
      final VitalsStat mp = character.stats.getStat(MP_STAT.get());
      final VitalsStat sp = character.stats.getStat(SP_STAT.get());

      if(this.description == null) {
        this.description = new UiBox(44, 156, 232, 14);
      }

      this.description.render(CONFIG.getConfig(UI_BACKGROUND_COLOUR.get()));

      String text = I18n.translate("lod.battle_actions.characters.hp", hp.getCurrent(), hp.getMax());

      if(character.canUseSpells()) {
        text += ' ' + I18n.translate("lod.battle_actions.characters.mp", mp.getCurrent(), mp.getMax());
      }

      if(character.hasDragoon()) {
        text += ' ' + I18n.translate("lod.battle_actions.characters.sp", sp.getCurrent(), sp.getMax());
      }

      this.fontOptions.trim(0);
      this.fontOptions.horizontalAlign(HorizontalAlign.CENTRE);
      this.fontOptions.size(0.75f);
      renderText(text, 160, 159, this.fontOptions);
    }
  }
}
