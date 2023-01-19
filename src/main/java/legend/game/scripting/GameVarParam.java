package legend.game.scripting;

import legend.game.SMap;
import legend.game.Scus94491BpeSegment_8004;
import legend.game.Scus94491BpeSegment_8005;
import legend.game.Scus94491BpeSegment_8006;
import legend.game.Scus94491BpeSegment_8007;
import legend.game.Scus94491BpeSegment_800b;
import legend.game.combat.Bttl_800c;
import legend.game.combat.types.BattleObject27c;
import legend.game.types.SubmapObject210;

import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;

public class GameVarParam extends Param {
  private final int index;

  public GameVarParam(final int index) {
    this.index = index;
  }

  @Override
  public int get() {
    return switch(this.index) {
      case 0 -> Scus94491BpeSegment_8004.mainCallbackIndex_8004dd20.get();
      case 1 -> Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c.get();
      case 2 -> Scus94491BpeSegment_800b.tickCount_800bb0fc.get();
      case 3 -> Scus94491BpeSegment_800b._800bee90.get();
      case 4 -> Scus94491BpeSegment_800b._800bee94.get();
      case 5 -> Scus94491BpeSegment_800b.gameState_800babc8.gold_94.get();
      case 6 -> Scus94491BpeSegment_800b.gameState_800babc8.scriptData_08.get(0).get();
      case 7 -> Scus94491BpeSegment_8007._8007a3a8.get();
      case 8 -> Scus94491BpeSegment_800b._800bb104.get();
      case 9 -> Scus94491BpeSegment_800b._800babc0.get();
      case 10 -> Scus94491BpeSegment_800b._800bb168.get();
      case 11 -> Scus94491BpeSegment_800b.scriptEffect_800bb140.red0_20.get();
      case 12 -> Scus94491BpeSegment_800b.scriptEffect_800bb140.green0_1c.get();
      case 13 -> Scus94491BpeSegment_800b.scriptEffect_800bb140.blue0_14.get();
      case 14 -> Scus94491BpeSegment_800b.scriptEffect_800bb140.red1_18.get();
      case 15 -> Scus94491BpeSegment_800b.scriptEffect_800bb140.green1_10.get();
      case 16 -> Scus94491BpeSegment_800b.scriptEffect_800bb140.blue1_0c.get();
      case 17 -> Scus94491BpeSegment_800b.gameState_800babc8.charIndex_88.get(0).get();
      case 18 -> Scus94491BpeSegment_800b.gameState_800babc8.chapterIndex_98.get();
      case 19 -> Scus94491BpeSegment_800b.gameState_800babc8.stardust_9c.get();
      case 20 -> Scus94491BpeSegment_800b.gameState_800babc8.timestamp_a0.get();
      case 21, 23 -> Scus94491BpeSegment_800b.gameState_800babc8.submapScene_a4.get();
      case 22 -> Scus94491BpeSegment_800b.gameState_800babc8.submapCut_a8.get();
      case 24 -> Scus94491BpeSegment_800b.gameState_800babc8._b0.get();
      case 25 -> Scus94491BpeSegment_8007.vsyncMode_8007a3b8.get();
      case 26 -> Scus94491BpeSegment_800b._800bee98.get();
      case 27 -> Scus94491BpeSegment_800b._800beebc.get();
      case 28 -> Scus94491BpeSegment_800b._800bee9c.get();
      case 29 -> Scus94491BpeSegment_800b._800beea4.get();
      case 30 -> Scus94491BpeSegment_800b._800beeac.get();
      case 31 -> Scus94491BpeSegment_800b._800beeb4.get();
      case 32 -> Scus94491BpeSegment_8006._8006e398.bobjIndices_e0c[0] != null ? Scus94491BpeSegment_8006._8006e398.bobjIndices_e0c[0].index : -1;
      case 33 -> Bttl_800c._800c66d0.get();
      case 34 -> Scus94491BpeSegment_8006._8006e398.charBobjIndices_e40[0] != null ? Scus94491BpeSegment_8006._8006e398.charBobjIndices_e40[0].index : -1;
      case 35 -> Bttl_800c.charCount_800c677c.get();
      case 36 -> Scus94491BpeSegment_8006._8006e398.bobjIndices_e50[0] != null ? Scus94491BpeSegment_8006._8006e398.bobjIndices_e50[0].index : -1;
      case 37 -> Bttl_800c.monsterCount_800c6768.get();
      case 38 -> Scus94491BpeSegment_8006._8006e398.morphMode_ee4;
      case 39 -> Scus94491BpeSegment_8006._8006e398.stageProgression_eec;
      case 40 -> Scus94491BpeSegment_800b.itemsDroppedByEnemiesCount_800bc978.get();
      case 41 -> Scus94491BpeSegment_800b.itemsDroppedByEnemies_800bc928.get(0).get();
      case 42 -> Bttl_800c.scriptIndex_800c66bc != null ? Bttl_800c.scriptIndex_800c66bc.index : -1;
      case 43 -> Scus94491BpeSegment_800b.encounterId_800bb0f8.get();
      case 44 -> Bttl_800c._800c6748.get();
//      case 45 -> Scus94491BpeSegment_8006._8006e398._180.get(0);
//      case 46 -> Bttl_800c.intRef_800c6718.get();
      case 47 -> Scus94491BpeSegment_800b.combatStage_800bb0f4.get();
      case 48 -> Scus94491BpeSegment_8006._8006e398.bobjIndices_e78[0] != null ? Scus94491BpeSegment_8006._8006e398.bobjIndices_e78[0].index : -1;
      case 49 -> Bttl_800c._800c669c.get();
      case 50 -> Scus94491BpeSegment_8006._8006e398.bobjIndices_eac[0] != null ? Scus94491BpeSegment_8006._8006e398.bobjIndices_eac[0].index : -1;
      case 51 -> Bttl_800c._800c6760.get();
      case 52 -> Scus94491BpeSegment_8006._8006e398.enemyBobjIndices_ebc[0] != null ? Scus94491BpeSegment_8006._8006e398.enemyBobjIndices_ebc[0].index : -1;
      case 53 -> Bttl_800c.enemyCount_800c6758.get();
      case 54 -> Scus94491BpeSegment_8006._8006e398._ef0;
      case 55 -> Scus94491BpeSegment_800b.gameState_800babc8._b4.get();
      case 56 -> Scus94491BpeSegment_800b.gameState_800babc8._b8.get();
      case 57 -> Scus94491BpeSegment_800b._800bc974.get();
      case 58 -> Scus94491BpeSegment_800b._800bc960.get();
      case 59 -> Bttl_800c.currentTurnBobj_800c66c8 != null ? Bttl_800c.currentTurnBobj_800c66c8.index : -1;
      case 60 -> Scus94491BpeSegment_800b.goldGainedFromCombat_800bc920.get();
      case 61 -> Scus94491BpeSegment_800b.totalXpFromCombat_800bc95c.get();

      case 64 -> SMap.sobjs_800c6880[0].index;
      case 65 -> SMap.submapControllerState_800c6740.index;
      case 66 -> SMap.sobjCount_800c6730.get();
      case 67 -> Scus94491BpeSegment_800b._800bd7b0.get();
      case 68 -> Scus94491BpeSegment_800b._800bda08.get();
      case 69 -> Scus94491BpeSegment_8005.submapCut_80052c30.get();
      case 70 -> Scus94491BpeSegment_8005.submapScene_80052c34.get();
//      case 71 -> SMap._800cb44c;
      case 72 -> SMap.encounterAccumulator_800c6ae8.get();
      case 73 -> SMap._800c6970.get(0).get();
//      case 74 -> Scus94491BpeSegment_8004._8004de54;
//      case 75 -> Scus94491BpeSegment_8004._8004de50;

      case 80 -> Bttl_800c.scriptState_800c6914 != null ? Bttl_800c.scriptState_800c6914.index : -1;
      case 81 -> Bttl_800c._800c6918.get();
      case 82 -> Bttl_800c._800c67c8.get();
      case 83 -> Bttl_800c._800c67cc.get();
      case 84 -> Bttl_800c._800c67d0.get();
      case 85 -> Bttl_800c._800c6710.get();
      case 86 -> Bttl_800c._800c6780.get();
      case 87 -> Bttl_800c._800c66a8.get();
      case 88 -> Bttl_800c._800c6700.get();
      case 89 -> Bttl_800c._800c6704.get();
      case 90 -> Bttl_800c._800c66b0.get();

      case 96 -> Bttl_800c._800c6754.get();
      case 97 -> Bttl_800c.currentStage_800c66a4.get();

      case 104 -> Bttl_800c._800c6764.get();
      case 105 -> Bttl_800c._800c6774.get();
      case 106 -> Bttl_800c._800c6778.get();
      case 107 -> Bttl_800c._800c676c.get();
      case 108 -> Bttl_800c._800c6770.get();
      case 109 -> Bttl_800c.mcqColour_800fa6dc.get();

      case 112 -> Scus94491BpeSegment_800b.gameState_800babc8._15c.get(0).get();
      case 113 -> Scus94491BpeSegment_800b.gameState_800babc8._17c.get(0).get();
      case 114 -> Scus94491BpeSegment_800b.gameState_800babc8.dragoonSpirits_19c.get(0).get();
      case 115 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c.get(0).partyFlags_04.get();
      case 116 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c.get(1).partyFlags_04.get();
      case 117 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c.get(2).partyFlags_04.get();
      case 118 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c.get(3).partyFlags_04.get();
      case 119 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c.get(4).partyFlags_04.get();
      case 120 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c.get(5).partyFlags_04.get();
      case 121 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c.get(6).partyFlags_04.get();
      case 122 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c.get(7).partyFlags_04.get();
      case 123 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c.get(8).partyFlags_04.get();
      case 124 -> Scus94491BpeSegment_8005.standingInSavePoint_8005a368.get();
      case 125 -> Scus94491BpeSegment_8007.shopId_8007a3b4.get();
      case 126 -> Scus94491BpeSegment_800b.gameState_800babc8._1a4.get(0).get();
      case 127 -> Scus94491BpeSegment_800b.gameState_800babc8.chestFlags_1c4.get(0).get();
//      case 128 -> Scus94491BpeSegment_8006._8006e398.specialEffect_00[0]._00.get();
//      case 129 -> Scus94491BpeSegment_8006._8006e398.specialEffect_00[1]._00.get();
//      case 130 -> Scus94491BpeSegment_8006._8006e398.specialEffect_00[2]._00.get();
//      case 131 -> Scus94491BpeSegment_8006._8006e398.specialEffect_00[3]._00.get();
//      case 132 -> Scus94491BpeSegment_8006._8006e398.specialEffect_00[4]._00.get();
//      case 133 -> Scus94491BpeSegment_8006._8006e398.specialEffect_00[5]._00.get();
//      case 134 -> Scus94491BpeSegment_8006._8006e398.specialEffect_00[6]._00.get();
//      case 135 -> Scus94491BpeSegment_8006._8006e398.specialEffect_00[7]._00.get();
//      case 136 -> Scus94491BpeSegment_8006._8006e398.specialEffect_00[8]._00.get();
//      case 137 -> Scus94491BpeSegment_8006._8006e398.specialEffect_00[9]._00.get();

      default -> throw new IllegalArgumentException("Unknown game data index " + this.index);
    };
  }

  @Override
  public Param set(final int val) {
    switch(this.index) {
      case 0 -> Scus94491BpeSegment_8004.mainCallbackIndex_8004dd20.set(val);
      case 1 -> Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c.set(val);
      case 2 -> Scus94491BpeSegment_800b.tickCount_800bb0fc.set(val);
      case 3 -> Scus94491BpeSegment_800b._800bee90.set(val);
      case 4 -> Scus94491BpeSegment_800b._800bee94.set(val);
      case 5 -> Scus94491BpeSegment_800b.gameState_800babc8.gold_94.set(val);
      case 6 -> Scus94491BpeSegment_800b.gameState_800babc8.scriptData_08.get(0).set(val);
      case 7 -> Scus94491BpeSegment_8007._8007a3a8.set(val);
      case 8 -> Scus94491BpeSegment_800b._800bb104.set(val);
      case 9 -> Scus94491BpeSegment_800b._800babc0.set(val);
      case 10 -> Scus94491BpeSegment_800b._800bb168.set(val);
      case 11 -> Scus94491BpeSegment_800b.scriptEffect_800bb140.red0_20.set(val);
      case 12 -> Scus94491BpeSegment_800b.scriptEffect_800bb140.green0_1c.set(val);
      case 13 -> Scus94491BpeSegment_800b.scriptEffect_800bb140.blue0_14.set(val);
      case 14 -> Scus94491BpeSegment_800b.scriptEffect_800bb140.red1_18.set(val);
      case 15 -> Scus94491BpeSegment_800b.scriptEffect_800bb140.green1_10.set(val);
      case 16 -> Scus94491BpeSegment_800b.scriptEffect_800bb140.blue1_0c.set(val);
      case 17 -> Scus94491BpeSegment_800b.gameState_800babc8.charIndex_88.get(0).set(val);
      case 18 -> Scus94491BpeSegment_800b.gameState_800babc8.chapterIndex_98.set(val);
      case 19 -> Scus94491BpeSegment_800b.gameState_800babc8.stardust_9c.set(val);
      case 20 -> Scus94491BpeSegment_800b.gameState_800babc8.timestamp_a0.set(val);
      case 21, 23 -> Scus94491BpeSegment_800b.gameState_800babc8.submapScene_a4.set(val);
      case 22 -> Scus94491BpeSegment_800b.gameState_800babc8.submapCut_a8.set(val);
      case 24 -> Scus94491BpeSegment_800b.gameState_800babc8._b0.set(val);
      case 25 -> Scus94491BpeSegment_8007.vsyncMode_8007a3b8.set(val);
      case 26 -> Scus94491BpeSegment_800b._800bee98.set(val);
      case 27 -> Scus94491BpeSegment_800b._800beebc.set(val);
      case 28 -> Scus94491BpeSegment_800b._800bee9c.set(val);
      case 29 -> Scus94491BpeSegment_800b._800beea4.set(val);
      case 30 -> Scus94491BpeSegment_800b._800beeac.set(val);
      case 31 -> Scus94491BpeSegment_800b._800beeb4.set(val);
      case 32 -> Scus94491BpeSegment_8006._8006e398.bobjIndices_e0c[0] = (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[val];
      case 33 -> Bttl_800c._800c66d0.set(val);
      case 34 -> Scus94491BpeSegment_8006._8006e398.charBobjIndices_e40[0] = (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[val];
      case 35 -> Bttl_800c.charCount_800c677c.set(val);
      case 36 -> Scus94491BpeSegment_8006._8006e398.bobjIndices_e50[0] = (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[val];
      case 37 -> Bttl_800c.monsterCount_800c6768.set(val);
      case 38 -> Scus94491BpeSegment_8006._8006e398.morphMode_ee4 = val;
      case 39 -> Scus94491BpeSegment_8006._8006e398.stageProgression_eec = val;
      case 40 -> Scus94491BpeSegment_800b.itemsDroppedByEnemiesCount_800bc978.set(val);
      case 41 -> Scus94491BpeSegment_800b.itemsDroppedByEnemies_800bc928.get(0).set(val);
      case 42 -> Bttl_800c.scriptIndex_800c66bc = (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[val];
      case 43 -> Scus94491BpeSegment_800b.encounterId_800bb0f8.set(val);
      case 44 -> Bttl_800c._800c6748.set(val);
//      case 45 -> Scus94491BpeSegment_8006._8006e398._180.get(0);
//      case 46 -> Bttl_800c.intRef_800c6718.set(val);
      case 47 -> Scus94491BpeSegment_800b.combatStage_800bb0f4.set(val);
      case 48 -> Scus94491BpeSegment_8006._8006e398.bobjIndices_e78[0] = (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[val];
      case 49 -> Bttl_800c._800c669c.set(val);
      case 50 -> Scus94491BpeSegment_8006._8006e398.bobjIndices_eac[0] = (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[val];
      case 51 -> Bttl_800c._800c6760.set(val);
      case 52 -> Scus94491BpeSegment_8006._8006e398.enemyBobjIndices_ebc[0] = (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[val];
      case 53 -> Bttl_800c.enemyCount_800c6758.set(val);
      case 54 -> Scus94491BpeSegment_8006._8006e398._ef0 = val;
      case 55 -> Scus94491BpeSegment_800b.gameState_800babc8._b4.set(val);
      case 56 -> Scus94491BpeSegment_800b.gameState_800babc8._b8.set(val);
      case 57 -> Scus94491BpeSegment_800b._800bc974.set(val);
      case 58 -> Scus94491BpeSegment_800b._800bc960.set(val);
      case 59 -> Bttl_800c.currentTurnBobj_800c66c8 = (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[val];
      case 60 -> Scus94491BpeSegment_800b.goldGainedFromCombat_800bc920.set(val);
      case 61 -> Scus94491BpeSegment_800b.totalXpFromCombat_800bc95c.set(val);

      case 64 -> SMap.sobjs_800c6880[0] = (ScriptState<SubmapObject210>)scriptStatePtrArr_800bc1c0[val];
      case 65 -> SMap.submapControllerState_800c6740 = (ScriptState<Void>)scriptStatePtrArr_800bc1c0[val];
      case 66 -> SMap.sobjCount_800c6730.set(val);
      case 67 -> Scus94491BpeSegment_800b._800bd7b0.set(val);
      case 68 -> Scus94491BpeSegment_800b._800bda08.set(val);
      case 69 -> Scus94491BpeSegment_8005.submapCut_80052c30.set(val);
      case 70 -> Scus94491BpeSegment_8005.submapScene_80052c34.set(val);
//      case 71 -> SMap._800cb44c;
      case 72 -> SMap.encounterAccumulator_800c6ae8.set(val);
      case 73 -> SMap._800c6970.get(0).set(val);
//      case 74 -> Scus94491BpeSegment_8004._8004de54;
//      case 75 -> Scus94491BpeSegment_8004._8004de50;

      case 80 -> Bttl_800c.scriptState_800c6914 = (ScriptState<BattleObject27c>)scriptStatePtrArr_800bc1c0[val];
      case 81 -> Bttl_800c._800c6918.set(val);
      case 82 -> Bttl_800c._800c67c8.set(val);
      case 83 -> Bttl_800c._800c67cc.set(val);
      case 84 -> Bttl_800c._800c67d0.set(val);
      case 85 -> Bttl_800c._800c6710.set(val);
      case 86 -> Bttl_800c._800c6780.set(val);
      case 87 -> Bttl_800c._800c66a8.set(val);
      case 88 -> Bttl_800c._800c6700.set(val);
      case 89 -> Bttl_800c._800c6704.set(val);
      case 90 -> Bttl_800c._800c66b0.set(val);

      case 96 -> Bttl_800c._800c6754.set(val);
      case 97 -> Bttl_800c.currentStage_800c66a4.set(val);

      case 104 -> Bttl_800c._800c6764.set(val);
      case 105 -> Bttl_800c._800c6774.set(val);
      case 106 -> Bttl_800c._800c6778.set(val);
      case 107 -> Bttl_800c._800c676c.set(val);
      case 108 -> Bttl_800c._800c6770.set(val);
      case 109 -> Bttl_800c.mcqColour_800fa6dc.set(val);

      case 112 -> Scus94491BpeSegment_800b.gameState_800babc8._15c.get(0).set(val);
      case 113 -> Scus94491BpeSegment_800b.gameState_800babc8._17c.get(0).set(val);
      case 114 -> Scus94491BpeSegment_800b.gameState_800babc8.dragoonSpirits_19c.get(0).set(val);
      case 115 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c.get(0).partyFlags_04.set(val);
      case 116 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c.get(1).partyFlags_04.set(val);
      case 117 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c.get(2).partyFlags_04.set(val);
      case 118 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c.get(3).partyFlags_04.set(val);
      case 119 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c.get(4).partyFlags_04.set(val);
      case 120 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c.get(5).partyFlags_04.set(val);
      case 121 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c.get(6).partyFlags_04.set(val);
      case 122 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c.get(7).partyFlags_04.set(val);
      case 123 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c.get(8).partyFlags_04.set(val);
      case 124 -> Scus94491BpeSegment_8005.standingInSavePoint_8005a368.set(val);
      case 125 -> Scus94491BpeSegment_8007.shopId_8007a3b4.set(val);
      case 126 -> Scus94491BpeSegment_800b.gameState_800babc8._1a4.get(0).set(val);
      case 127 -> Scus94491BpeSegment_800b.gameState_800babc8.chestFlags_1c4.get(0).set(val);
//      case 128 -> Scus94491BpeSegment_8006._8006e398.specialEffect_00[0]._00.set(val);
//      case 129 -> Scus94491BpeSegment_8006._8006e398.specialEffect_00[1]._00.set(val);
//      case 130 -> Scus94491BpeSegment_8006._8006e398.specialEffect_00[2]._00.set(val);
//      case 131 -> Scus94491BpeSegment_8006._8006e398.specialEffect_00[3]._00.set(val);
//      case 132 -> Scus94491BpeSegment_8006._8006e398.specialEffect_00[4]._00.set(val);
//      case 133 -> Scus94491BpeSegment_8006._8006e398.specialEffect_00[5]._00.set(val);
//      case 134 -> Scus94491BpeSegment_8006._8006e398.specialEffect_00[6]._00.set(val);
//      case 135 -> Scus94491BpeSegment_8006._8006e398.specialEffect_00[7]._00.set(val);
//      case 136 -> Scus94491BpeSegment_8006._8006e398.specialEffect_00[8]._00.set(val);
//      case 137 -> Scus94491BpeSegment_8006._8006e398.specialEffect_00[9]._00.set(val);

      default -> throw new IllegalArgumentException("Unknown game data index " + this.index);
    }

    return this;
  }

  @Override
  public Param array(final int index) {
    return new GameVarArrayParam(this.index, index);
  }

  @Override
  public String toString() {
    return "GameVar[%d] %d".formatted(this.index, this.get());
  }
}
