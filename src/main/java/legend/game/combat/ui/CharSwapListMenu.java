package legend.game.combat.ui;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import legend.game.characters.CharacterData2c;
import legend.game.characters.StatCollection;
import legend.game.characters.VitalsStat;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.types.CombatantStruct1a8;
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
import static legend.game.characters.CharacterData2c.CANT_REMOVE;
import static legend.game.characters.CharacterData2c.CAN_BE_IN_PARTY;
import static legend.game.characters.CharacterData2c.IN_PARTY;
import static legend.game.combat.SBtld.loadAdditions;
import static legend.game.modding.coremod.CoreMod.UNLOCK_PARTY_CONFIG;
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

  private CombatantStruct1a8 loadingCombatant;

  private final ListPosition lastPosition;
  private final Runnable onClose;

  public CharSwapListMenu(final BattleHud hud, final PlayerBattleEntity activePlayer, final ListPosition lastPosition, final Runnable onClose) {
    lastPosition.lastListIndex_26 = 0;
    lastPosition.lastListScroll_28 = 0;

    super(hud, activePlayer, 186, lastPosition, onClose);

    this.characters.addAll(getUsableCharacterList());

    this.lastPosition = lastPosition;
    this.onClose = onClose;
  }

  public static boolean canUseCharSwap(final PlayerBattleEntity player) {
    if((player.character.partyFlags_04 & CANT_REMOVE) != 0 && !CONFIG.getConfig(UNLOCK_PARTY_CONFIG.get())) {
      return false;
    }

    return !getUsableCharacterList().isEmpty();
  }

  public static IntList getUsableCharacterList() {
    final IntList characters = new IntArrayList();

    for(int charIndex = 0; charIndex < gameState_800babc8.charData_32c.size(); charIndex++) {
      final CharacterData2c character = gameState_800babc8.charData_32c.get(charIndex);

      if((character.partyFlags_04 & IN_PARTY) != 0 && (character.partyFlags_04 & CAN_BE_IN_PARTY) != 0) {
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
    this.hud.battle.combatDisabled_800c66b9 = true;

    this.hud.setBattleHudVisibility(false);
    final int removedState = this.removePlayer();
    this.addPlayer(removedState, this.characters.getInt(index));

    this.hud.deleteUiElements();

    this.flags_02 &= ~0x8;
    this.menuState_00 = 8;
  }

  public int removePlayer() {
    //noinspection unchecked
    final ScriptState<PlayerBattleEntity> removedState = (ScriptState<PlayerBattleEntity>)this.hud.battle.currentTurnBent_800c66c8;
    this.hud.battle.charSwapBentCache.put(removedState.innerStruct_00.character, removedState.innerStruct_00);

    final int removedScriptStateId = removedState.index;
    final int combatantId = removedState.innerStruct_00.combatantIndex_26c;
    removedState.destructor_0c = (a, b) -> {};
    removedState.innerStruct_00.model_148.deleteModelParts();

    this.oldBentSlot = removedState.innerStruct_00.allBentSlot_274;
    this.oldPlayerSlot = removedState.innerStruct_00.typeBentSlot_276;
    this.hud.battle.deallocateCombatant(removedState.innerStruct_00.combatant_144);
    this.hud.battle.removeCombatant(combatantId);
    this.hud.clearNumbers(removedScriptStateId - 6);
    removedState.deallocateWithChildren();
    return removedScriptStateId;
  }

  public void addPlayer(final int removedState, final int charIndex) {
    final int charSlot = removedState - 6;
    gameState_800babc8.charIds_88.set(charSlot, charIndex);
    loadAdditions();

    final CharacterData2c character = gameState_800babc8.charData_32c.get(charIndex);
    final int addedCombatantSlot = this.hud.battle.addCombatant(charIndex, charSlot);

    PlayerBattleEntity bent = this.hud.battle.charSwapBentCache.get(character);
    final ScriptState<PlayerBattleEntity> state;

    if(bent == null) {
      state = this.hud.battle.allocatePlayerBattleEntity(character, charSlot, charIndex, removedState, addedCombatantSlot, new AtomicInteger());
      bent = state.innerStruct_00;
      this.hud.battle.initPlayerBattleEntityStats(bent);
    } else {
      state = this.hud.battle.initPlayerBattleEntity(bent, character, charSlot, removedState, addedCombatantSlot);
    }

    battleState_8006e398.playerBents_e40.set(this.oldPlayerSlot, state);
    battleState_8006e398.allBents_e0c.set(this.oldBentSlot, state);
    bent.allBentSlot_274 = this.oldBentSlot;
    bent.typeBentSlot_276 = this.oldPlayerSlot;

    this.loadingCombatant = this.hud.battle.getCombatant(addedCombatantSlot);
    this.hud.battle.loadCombatantTmdAndAnims(this.loadingCombatant);

    Loader
      .loadFile(character.getBattleTexturePath(bent))
      .thenAccept(files -> this.hud.battle.loadCharacterTim(files, charSlot))
    ;

    this.hud.battle.loadAttackAnimations(this.hud.battle.getCombatant(addedCombatantSlot));
  }

  @Override
  public void tick() {
    // This whole switch is just to delay battle until assets finish loading
    switch(this.menuState_00) {
      case 6 -> {
        super.tick();
        this.selectionState_a0 = 0;
        return;
      }

      case 9 -> {
        if(this.selectionState_a0 == -1) { // cancelled
          super.tick();
          return;
        }

        this.lastPosition.lastListIndex_26 = this.listIndex_24;
        this.lastPosition.lastListScroll_28 = this.listScroll_1e;
        this.onClose();
        this.delete();
        this.menuState_00 = 10;
        return;
      }

      case 10 -> {
        if((this.loadingCombatant.flags_19e & 0x20) == 0 && Loader.getLoadingFileCount() == 0) {
          this.menuState_00 = 11;
        }

        return;
      }

      case 11 -> {
        this.hud.battle.combatDisabled_800c66b9 = false;
        this.onClose.run();
        return;
      }
    }

    super.tick();
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
      final PlayerBattleEntity player = this.hud.battle.charSwapBentCache.get(character);

      final StatCollection stats = player != null ? player.stats : character.stats;

      final VitalsStat hp = stats.getStat(HP_STAT.get());
      final VitalsStat mp = stats.getStat(MP_STAT.get());
      final VitalsStat sp = stats.getStat(SP_STAT.get());

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
