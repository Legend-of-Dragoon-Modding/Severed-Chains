package legend.game.combat.environment;

import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.game.additions.AdditionHitProperties10;
import legend.game.additions.AdditionHits80;
import legend.game.additions.CharacterAdditionStats;
import legend.game.types.CharacterData2c;
import legend.game.types.McqHeader;

import static legend.core.GameEngine.REGISTRIES;
import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.combat.bent.BattleEntity27c.FLAG_DRAGOON;

/** 0x18cb0 bytes */
public class BattlePreloadedEntities_18cb0 {
//  public EncounterData38 encounterData_00;
  public final AdditionHits80[] dragoonAdditionHits_38 = new AdditionHits80[3];
  /** This reference is only valid while it's loading */
//  public MrgFile stageMrg_638;
//  public MrgFile stageTmdMrg_63c;

  public final BattleStage stage_963c = new BattleStage();
  public McqHeader stageMcq_9cb0;
  public Obj skyboxObj;
  public final MV skyboxTransforms = new MV();

  public AdditionHitProperties10 getHit(final int charSlot, final int hitNum) {
    if(battleState_8006e398.playerBents_e40[charSlot].hasFlag(FLAG_DRAGOON)) { // Is dragoon
      return this.dragoonAdditionHits_38[charSlot].hits_00[hitNum];
    }

    //LAB_800c74fc
    final CharacterData2c charData = gameState_800babc8.charData_32c[gameState_800babc8.charIds_88.getInt(charSlot)];
    final CharacterAdditionStats additionStats = charData.additionStats.get(charData.selectedAddition_19);
    return REGISTRIES.additions.getEntry(charData.selectedAddition_19).get().getHit(charData, additionStats, hitNum);
  }

  public int getHitCount(final int charSlot) {
    if(battleState_8006e398.playerBents_e40[charSlot].hasFlag(FLAG_DRAGOON)) { // Is dragoon
      return this.dragoonAdditionHits_38[charSlot].hits_00.length;
    }

    //LAB_800c74fc
    final CharacterData2c charData = gameState_800babc8.charData_32c[gameState_800babc8.charIds_88.getInt(charSlot)];
    final CharacterAdditionStats additionStats = charData.additionStats.get(charData.selectedAddition_19);
    return REGISTRIES.additions.getEntry(charData.selectedAddition_19).get().getHitCount(charData, additionStats);
  }

  @Method(0x800c7488L)
  public int getHitProperty(final int charSlot, final int hitNum, final int hitPropertyIndex) {
    return this.getHit(charSlot, hitNum).get(hitPropertyIndex);
  }
}
