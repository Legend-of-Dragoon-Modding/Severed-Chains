package legend.game.scripting;

import legend.game.EngineStateEnum;
import legend.game.Scus94491BpeSegment_8004;
import legend.game.Scus94491BpeSegment_8005;
import legend.game.Scus94491BpeSegment_8007;
import legend.game.Scus94491BpeSegment_800b;
import legend.game.combat.Battle;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.bent.MonsterBattleEntity;
import legend.game.combat.bent.PlayerBattleEntity;
import legend.game.combat.effects.TransformationMode;
import legend.game.inventory.Equipment;
import legend.game.inventory.InventoryEntry;
import legend.game.inventory.Item;
import legend.game.modding.coremod.CoreMod;
import legend.game.submap.SMap;
import legend.game.submap.SubmapObject210;
import legend.lodmod.LodMod;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.REGISTRIES;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.Scus94491BpeSegment_8004.currentEngineState_8004dd04;
import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;
import static legend.game.Scus94491BpeSegment_800b.equipmentOverflow;
import static legend.game.Scus94491BpeSegment_800b.itemOverflow;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;

public class GameVarParam extends Param {
  private final int index;

  public GameVarParam(final int index) {
    this.index = index;
  }

  @Override
  public int get() {
    return switch(this.index) {
      case 0 -> Scus94491BpeSegment_8004.engineState_8004dd20.ordinal();
      case 1 -> Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
      case 2 -> Scus94491BpeSegment_800b.tickCount_800bb0fc;
      case 3 -> SCRIPTS.joypadInput;
      case 4 -> SCRIPTS.joypadPress;
      case 5 -> Scus94491BpeSegment_800b.gameState_800babc8.gold_94;
      case 6 -> Scus94491BpeSegment_800b.gameState_800babc8.scriptData_08[0];
      case 7 -> Scus94491BpeSegment_8007.clearRed_8007a3a8;
      case 8 -> Scus94491BpeSegment_800b.clearGreen_800bb104;
      case 9 -> Scus94491BpeSegment_800b.clearBlue_800babc0;
      case 10 -> Scus94491BpeSegment_800b.fullScreenEffect_800bb140.currentColour_28;
      case 11 -> Scus94491BpeSegment_800b.fullScreenEffect_800bb140.red0_20;
      case 12 -> Scus94491BpeSegment_800b.fullScreenEffect_800bb140.green0_1c;
      case 13 -> Scus94491BpeSegment_800b.fullScreenEffect_800bb140.blue0_14;
      case 14 -> Scus94491BpeSegment_800b.fullScreenEffect_800bb140.red1_18;
      case 15 -> Scus94491BpeSegment_800b.fullScreenEffect_800bb140.green1_10;
      case 16 -> Scus94491BpeSegment_800b.fullScreenEffect_800bb140.blue1_0c;
      case 17 -> Scus94491BpeSegment_800b.gameState_800babc8.charIds_88[0];
      case 18 -> Scus94491BpeSegment_800b.gameState_800babc8.chapterIndex_98;
      case 19 -> Scus94491BpeSegment_800b.gameState_800babc8.stardust_9c;
      case 20 -> Scus94491BpeSegment_800b.gameState_800babc8.timestamp_a0;
      case 21, 23 -> Scus94491BpeSegment_800b.gameState_800babc8.submapScene_a4;
      case 22 -> Scus94491BpeSegment_800b.gameState_800babc8.submapCut_a8;
      case 24 -> Scus94491BpeSegment_800b.gameState_800babc8._b0;
      case 25 -> Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
      case 26 -> SCRIPTS.joypadRepeat;
      case 27 -> Scus94491BpeSegment_800b.analogInput_800beebc;
      case 28 -> Scus94491BpeSegment_800b.analogAngle_800bee9c;
      case 29 -> Scus94491BpeSegment_800b._800beea4;
      case 30 -> Scus94491BpeSegment_800b._800beeac;
      case 31 -> Scus94491BpeSegment_800b.analogMagnitude_800beeb4;
      case 32 -> battleState_8006e398.allBents_e0c[0] != null ? battleState_8006e398.allBents_e0c[0].index : -1;
      case 33 -> battleState_8006e398.getAllBentCount();
      case 34 -> battleState_8006e398.playerBents_e40[0] != null ? battleState_8006e398.playerBents_e40[0].index : -1;
      case 35 -> battleState_8006e398.getPlayerCount();
      case 36 -> battleState_8006e398.monsterBents_e50[0] != null ? battleState_8006e398.monsterBents_e50[0].index : -1;
      case 37 -> battleState_8006e398.getMonsterCount();
      case 38 -> CONFIG.getConfig(CoreMod.TRANSFORMATION_MODE_CONFIG.get()).ordinal();
      case 39 -> battleState_8006e398.battlePhase_eec;
      case 40 -> Scus94491BpeSegment_800b.itemsDroppedByEnemies_800bc928.size();
      case 41 -> throw new RuntimeException("Not implemented"); //Scus94491BpeSegment_800b.itemsDroppedByEnemies_800bc928.get(0);
      case 42 -> ((Battle)currentEngineState_8004dd04).forcedTurnBent_800c66bc != null ? ((Battle)currentEngineState_8004dd04).forcedTurnBent_800c66bc.index : -1;
      case 43 -> Scus94491BpeSegment_800b.encounterId_800bb0f8;
      case 44 -> ((Battle)currentEngineState_8004dd04).cameraScriptMainTableJumpIndex_800c6748;
//      case 45 -> Scus94491BpeSegment_8006._8006e398._180.get(0);
//      case 46 -> Bttl_800c.intRef_800c6718.get();
      case 47 -> Scus94491BpeSegment_800b.battleStage_800bb0f4;
      case 48 -> battleState_8006e398.aliveBents_e78[0] != null ? battleState_8006e398.aliveBents_e78[0].index : -1;
      case 49 -> battleState_8006e398.getAliveBentCount();
      case 50 -> battleState_8006e398.alivePlayerBents_eac[0] != null ? battleState_8006e398.alivePlayerBents_eac[0].index : -1;
      case 51 -> battleState_8006e398.getAlivePlayerCount();
      case 52 -> battleState_8006e398.aliveMonsterBents_ebc[0] != null ? battleState_8006e398.aliveMonsterBents_ebc[0].index : -1;
      case 53 -> battleState_8006e398.getAliveMonsterCount();
      case 54 -> battleState_8006e398.cameraControllerScriptTicksParam_ef0;
      case 55 -> Scus94491BpeSegment_800b.gameState_800babc8._b4;
      case 56 -> Scus94491BpeSegment_800b.gameState_800babc8._b8;
      case 57 -> Scus94491BpeSegment_800b.postBattleAction_800bc974;
      case 58 -> Scus94491BpeSegment_800b.battleFlags_800bc960;
      case 59 -> ((Battle)currentEngineState_8004dd04).currentTurnBent_800c66c8 != null ? ((Battle)currentEngineState_8004dd04).currentTurnBent_800c66c8.index : -1;
      case 60 -> Scus94491BpeSegment_800b.goldGainedFromCombat_800bc920;
      case 61 -> Scus94491BpeSegment_800b.totalXpFromCombat_800bc95c;

      case 64 -> ((SMap)currentEngineState_8004dd04).sobjs_800c6880[0].index;
      case 65 -> ((SMap)currentEngineState_8004dd04).submapControllerState_800c6740.index;
      case 66 -> ((SMap)currentEngineState_8004dd04).sobjCount_800c6730;
      case 67 -> Scus94491BpeSegment_800b._800bd7b0;
      case 68 -> Scus94491BpeSegment_800b.previousSubmapCut_800bda08;
      case 69 -> Scus94491BpeSegment_8005.submapCut_80052c30;
      case 70 -> Scus94491BpeSegment_8005.submapScene_80052c34;
//      case 71 -> SMap._800cb44c;
      case 72 -> (int)((SMap)currentEngineState_8004dd04).encounterAccumulator_800c6ae8;
      case 73 -> ((SMap)currentEngineState_8004dd04).indicatorTickCountArray_800c6970[0];
//      case 74 -> Scus94491BpeSegment_8004._8004de54;
//      case 75 -> Scus94491BpeSegment_8004._8004de50;

      case 80 -> ((Battle)currentEngineState_8004dd04).scriptState_800c6914 != null ? ((Battle)currentEngineState_8004dd04).scriptState_800c6914.index : -1;
      case 81 -> ((Battle)currentEngineState_8004dd04)._800c6918;
      case 82 -> ((Battle)currentEngineState_8004dd04)._800c67c8;
      case 83 -> ((Battle)currentEngineState_8004dd04)._800c67cc;
      case 84 -> ((Battle)currentEngineState_8004dd04)._800c67d0;
      case 85 -> ((Battle)currentEngineState_8004dd04)._800c6710;
      case 86 -> ((Battle)currentEngineState_8004dd04).currentCameraIndex_800c6780;
      case 87 -> ((Battle)currentEngineState_8004dd04).battleInitialCameraMovementFinished_800c66a8 ? 1 : 0;
      case 88 -> ((Battle)currentEngineState_8004dd04).cameraScriptSubtableJumpIndex_800c6700;
      case 89 -> ((Battle)currentEngineState_8004dd04).cameraScriptSubtableJumpIndex_800c6704;
      case 90 -> ((Battle)currentEngineState_8004dd04).hud.currentCameraPositionIndicesIndex_800c66b0;

      case 96 -> ((Battle)currentEngineState_8004dd04).shouldRenderStage_800c6754 ? 1 : 0;
      case 97 -> ((Battle)currentEngineState_8004dd04).currentStage_800c66a4;

      case 104 -> ((Battle)currentEngineState_8004dd04).shouldRenderMcq_800c6764 ? 1 : 0;
      case 105 -> ((Battle)currentEngineState_8004dd04).mcqOffsetX_800c6774;
      case 106 -> ((Battle)currentEngineState_8004dd04).mcqOffsetY_800c6778;
      case 107 -> ((Battle)currentEngineState_8004dd04).mcqStepX_800c676c;
      case 108 -> ((Battle)currentEngineState_8004dd04).mcqStepY_800c6770;
      case 109 -> ((Battle)currentEngineState_8004dd04).mcqColour_800fa6dc;

      case 112 -> Scus94491BpeSegment_800b.gameState_800babc8.wmapFlags_15c.getRaw(0);
      case 113 -> Scus94491BpeSegment_800b.gameState_800babc8.visitedLocations_17c.getRaw(0);
      case 114 -> Scus94491BpeSegment_800b.gameState_800babc8.goods_19c[0];
      case 115 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c[0].partyFlags_04;
      case 116 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c[1].partyFlags_04;
      case 117 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c[2].partyFlags_04;
      case 118 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c[3].partyFlags_04;
      case 119 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c[4].partyFlags_04;
      case 120 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c[5].partyFlags_04;
      case 121 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c[6].partyFlags_04;
      case 122 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c[7].partyFlags_04;
      case 123 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c[8].partyFlags_04;
      case 124 -> Scus94491BpeSegment_8005.standingInSavePoint_8005a368 ? 1 : 0;
      case 125 -> Scus94491BpeSegment_8007.shopId_8007a3b4;
      case 126 -> Scus94491BpeSegment_800b.gameState_800babc8._1a4[0];
      case 127 -> Scus94491BpeSegment_800b.gameState_800babc8.chestFlags_1c4[0];
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
      case 0 -> Scus94491BpeSegment_8004.engineState_8004dd20 = EngineStateEnum.values()[val];
      case 1 -> Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c = val;
      case 2 -> Scus94491BpeSegment_800b.tickCount_800bb0fc = val;
      case 3 -> Scus94491BpeSegment_800b.input_800bee90 = val;
      case 4 -> Scus94491BpeSegment_800b.press_800bee94 = val;
      case 5 -> Scus94491BpeSegment_800b.gameState_800babc8.gold_94 = val;
      case 6 -> Scus94491BpeSegment_800b.gameState_800babc8.scriptData_08[0] = val;
      case 7 -> Scus94491BpeSegment_8007.clearRed_8007a3a8 = val;
      case 8 -> Scus94491BpeSegment_800b.clearGreen_800bb104 = val;
      case 9 -> Scus94491BpeSegment_800b.clearBlue_800babc0 = val;
      case 10 -> Scus94491BpeSegment_800b.fullScreenEffect_800bb140.currentColour_28 = val;
      case 11 -> Scus94491BpeSegment_800b.fullScreenEffect_800bb140.red0_20 = val;
      case 12 -> Scus94491BpeSegment_800b.fullScreenEffect_800bb140.green0_1c = val;
      case 13 -> Scus94491BpeSegment_800b.fullScreenEffect_800bb140.blue0_14 = val;
      case 14 -> Scus94491BpeSegment_800b.fullScreenEffect_800bb140.red1_18 = val;
      case 15 -> Scus94491BpeSegment_800b.fullScreenEffect_800bb140.green1_10 = val;
      case 16 -> Scus94491BpeSegment_800b.fullScreenEffect_800bb140.blue1_0c = val;
      case 17 -> Scus94491BpeSegment_800b.gameState_800babc8.charIds_88[0] = val;
      case 18 -> Scus94491BpeSegment_800b.gameState_800babc8.chapterIndex_98 = val;
      case 19 -> Scus94491BpeSegment_800b.gameState_800babc8.stardust_9c = val;
      case 20 -> Scus94491BpeSegment_800b.gameState_800babc8.timestamp_a0 = val;
      case 21, 23 -> Scus94491BpeSegment_800b.gameState_800babc8.submapScene_a4 = val;
      case 22 -> Scus94491BpeSegment_800b.gameState_800babc8.submapCut_a8 = val;
      case 24 -> Scus94491BpeSegment_800b.gameState_800babc8._b0 = val;
      case 25 -> Scus94491BpeSegment_8007.vsyncMode_8007a3b8 = val;
      case 26 -> Scus94491BpeSegment_800b.repeat_800bee98 = val;
      case 27 -> Scus94491BpeSegment_800b.analogInput_800beebc = val;
      case 28 -> Scus94491BpeSegment_800b.analogAngle_800bee9c = val;
      case 29 -> Scus94491BpeSegment_800b._800beea4 = val;
      case 30 -> Scus94491BpeSegment_800b._800beeac = val;
      case 31 -> Scus94491BpeSegment_800b.analogMagnitude_800beeb4 = val;
      case 32 -> battleState_8006e398.allBents_e0c[0] = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[val];
//      case 33 -> battleState_8006e398.allBentCount_800c66d0 = val;
      case 34 -> battleState_8006e398.playerBents_e40[0] = (ScriptState<PlayerBattleEntity>)scriptStatePtrArr_800bc1c0[val];
//      case 35 -> battleState_8006e398.playerCount_800c677c = val;
      case 36 -> battleState_8006e398.monsterBents_e50[0] = (ScriptState<MonsterBattleEntity>)scriptStatePtrArr_800bc1c0[val];
//      case 37 -> battleState_8006e398.monsterCount_800c6768 = val;
      case 38 -> CONFIG.setConfig(CoreMod.TRANSFORMATION_MODE_CONFIG.get(), TransformationMode.values()[val]);
      case 39 -> battleState_8006e398.battlePhase_eec = val;
      case 40 -> {
        // When given items on submaps (Sapphire Pin from Dabas, Psych Bomb from Savan), if you have a full inventory it'll
        // set the dropped items to 1, add the item to the drops list, and open the "Too Many Items" screen. We ignore the
        // size and simply add the drop to the list if var[41] is set.
      }
      case 41 -> {
        final InventoryEntry invEntry = val < 192 ? REGISTRIES.equipment.getEntry(LodMod.equipmentIdMap.get(val)).get() : REGISTRIES.items.getEntry(LodMod.itemIdMap.get(val - 192)).get();

        if(invEntry instanceof final Equipment equipment) {
          equipmentOverflow.add(equipment);
        } else if(invEntry instanceof final Item item) {
          itemOverflow.add(item);
        }
      }
      case 42 -> ((Battle)currentEngineState_8004dd04).forcedTurnBent_800c66bc = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[val];
      case 43 -> Scus94491BpeSegment_800b.encounterId_800bb0f8 = val;
      case 44 -> ((Battle)currentEngineState_8004dd04).cameraScriptMainTableJumpIndex_800c6748 = val;
//      case 45 -> Scus94491BpeSegment_8006._8006e398._180.get(0);
//      case 46 -> Bttl_800c.intRef_800c6718.set(val);
      case 47 -> Scus94491BpeSegment_800b.battleStage_800bb0f4 = val;
      case 48 -> battleState_8006e398.aliveBents_e78[0] = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[val];
//      case 49 -> battleState_8006e398.aliveBentCount_800c669c = val;
      case 50 -> battleState_8006e398.alivePlayerBents_eac[0] = (ScriptState<PlayerBattleEntity>)scriptStatePtrArr_800bc1c0[val];
//      case 51 -> battleState_8006e398.alivePlayerCount_800c6760 = val;
      case 52 -> battleState_8006e398.aliveMonsterBents_ebc[0] = (ScriptState<MonsterBattleEntity>)scriptStatePtrArr_800bc1c0[val];
//      case 53 -> battleState_8006e398.aliveMonsterCount_800c6758 = val;
      case 54 -> battleState_8006e398.cameraControllerScriptTicksParam_ef0 = val;
      case 55 -> Scus94491BpeSegment_800b.gameState_800babc8._b4 = val;
      case 56 -> Scus94491BpeSegment_800b.gameState_800babc8._b8 = val;
      case 57 -> Scus94491BpeSegment_800b.postBattleAction_800bc974 = val;
      case 58 -> Scus94491BpeSegment_800b.battleFlags_800bc960 = val;
      case 59 -> ((Battle)currentEngineState_8004dd04).currentTurnBent_800c66c8 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[val];
      case 60 -> Scus94491BpeSegment_800b.goldGainedFromCombat_800bc920 = val;
      case 61 -> Scus94491BpeSegment_800b.totalXpFromCombat_800bc95c = val;

      case 64 -> ((SMap)currentEngineState_8004dd04).sobjs_800c6880[0] = (ScriptState<SubmapObject210>)scriptStatePtrArr_800bc1c0[val];
      case 65 -> ((SMap)currentEngineState_8004dd04).submapControllerState_800c6740 = (ScriptState<Void>)scriptStatePtrArr_800bc1c0[val];
      case 66 -> ((SMap)currentEngineState_8004dd04).sobjCount_800c6730 = val;
      case 67 -> Scus94491BpeSegment_800b._800bd7b0 = val;
      case 68 -> Scus94491BpeSegment_800b.previousSubmapCut_800bda08 = val;
      case 69 -> Scus94491BpeSegment_8005.submapCut_80052c30 = val;
      case 70 -> Scus94491BpeSegment_8005.submapScene_80052c34 = val;
//      case 71 -> SMap._800cb44c;
      case 72 -> ((SMap)currentEngineState_8004dd04).encounterAccumulator_800c6ae8 = val;
      case 73 -> ((SMap)currentEngineState_8004dd04).indicatorTickCountArray_800c6970[0] = val;
//      case 74 -> Scus94491BpeSegment_8004._8004de54;
//      case 75 -> Scus94491BpeSegment_8004._8004de50;

      case 80 -> ((Battle)currentEngineState_8004dd04).scriptState_800c6914 = (ScriptState<BattleEntity27c>)scriptStatePtrArr_800bc1c0[val];
      case 81 -> ((Battle)currentEngineState_8004dd04)._800c6918 = val;
      case 82 -> ((Battle)currentEngineState_8004dd04)._800c67c8 = val;
      case 83 -> ((Battle)currentEngineState_8004dd04)._800c67cc = val;
      case 84 -> ((Battle)currentEngineState_8004dd04)._800c67d0 = val;
      case 85 -> ((Battle)currentEngineState_8004dd04)._800c6710 = val;
      case 86 -> ((Battle)currentEngineState_8004dd04).currentCameraIndex_800c6780 = val;
      case 87 -> ((Battle)currentEngineState_8004dd04).battleInitialCameraMovementFinished_800c66a8 = val != 0;
      case 88 -> ((Battle)currentEngineState_8004dd04).cameraScriptSubtableJumpIndex_800c6700 = val;
      case 89 -> ((Battle)currentEngineState_8004dd04).cameraScriptSubtableJumpIndex_800c6704 = val;
      case 90 -> ((Battle)currentEngineState_8004dd04).hud.currentCameraPositionIndicesIndex_800c66b0 = val;

      case 96 -> ((Battle)currentEngineState_8004dd04).shouldRenderStage_800c6754 = val != 0;
      case 97 -> ((Battle)currentEngineState_8004dd04).currentStage_800c66a4 = val;

      case 104 -> ((Battle)currentEngineState_8004dd04).shouldRenderMcq_800c6764 = val != 0;
      case 105 -> ((Battle)currentEngineState_8004dd04).mcqOffsetX_800c6774 = val;
      case 106 -> ((Battle)currentEngineState_8004dd04).mcqOffsetY_800c6778 = val;
      case 107 -> ((Battle)currentEngineState_8004dd04).mcqStepX_800c676c = val;
      case 108 -> ((Battle)currentEngineState_8004dd04).mcqStepY_800c6770 = val;
      case 109 -> ((Battle)currentEngineState_8004dd04).mcqColour_800fa6dc = val;

      case 112 -> Scus94491BpeSegment_800b.gameState_800babc8.wmapFlags_15c.setRaw(0, val);
      case 113 -> Scus94491BpeSegment_800b.gameState_800babc8.visitedLocations_17c.setRaw(0, val);
      case 114 -> Scus94491BpeSegment_800b.gameState_800babc8.goods_19c[0] = val;
      case 115 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c[0].partyFlags_04 = val;
      case 116 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c[1].partyFlags_04 = val;
      case 117 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c[2].partyFlags_04 = val;
      case 118 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c[3].partyFlags_04 = val;
      case 119 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c[4].partyFlags_04 = val;
      case 120 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c[5].partyFlags_04 = val;
      case 121 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c[6].partyFlags_04 = val;
      case 122 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c[7].partyFlags_04 = val;
      case 123 -> Scus94491BpeSegment_800b.gameState_800babc8.charData_32c[8].partyFlags_04 = val;
      case 124 -> Scus94491BpeSegment_8005.standingInSavePoint_8005a368 = val != 0;
      case 125 -> Scus94491BpeSegment_8007.shopId_8007a3b4 = val;
      case 126 -> Scus94491BpeSegment_800b.gameState_800babc8._1a4[0] = val;
      case 127 -> Scus94491BpeSegment_800b.gameState_800babc8.chestFlags_1c4[0] = val;
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
