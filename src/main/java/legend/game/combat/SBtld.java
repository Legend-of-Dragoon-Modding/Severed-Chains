package legend.game.combat;

import legend.core.MathHelper;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.Pointer;
import legend.game.combat.bobj.BattleObject27c;
import legend.game.combat.bobj.MonsterBattleObject;
import legend.game.combat.deff.DeffManager7cc;
import legend.game.combat.environment.BattlePreloadedEntities_18cb0;
import legend.game.combat.environment.EncounterData38;
import legend.game.combat.environment.StageData10;
import legend.game.combat.types.BattleScriptDataBase;
import legend.game.combat.types.CombatantStruct1a8;
import legend.game.combat.types.EnemyRewards08;
import legend.game.combat.types.MonsterStats1c;
import legend.game.modding.events.battle.EnemyRewardsEvent;
import legend.game.modding.events.characters.BattleMapActiveAdditionHitPropertiesEvent;
import legend.game.scripting.ScriptFile;
import legend.game.scripting.ScriptState;
import legend.game.types.LodString;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Unpacker;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.MEMORY;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.Scus94491BpeSegment.battlePreloadedEntities_1f8003f4;
import static legend.game.Scus94491BpeSegment.loadDrgnFile;
import static legend.game.Scus94491BpeSegment.loadFile;
import static legend.game.Scus94491BpeSegment.loadSupportOverlay;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;
import static legend.game.Scus94491BpeSegment_800b._800bc960;
import static legend.game.Scus94491BpeSegment_800b.combatStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.combat.Bttl_800c.currentCameraPositionIndicesIndex_800c66b0;
import static legend.game.combat.Bttl_800c._800c6748;
import static legend.game.combat.Bttl_800c.currentCameraIndex_800c6780;
import static legend.game.combat.Bttl_800c.addCombatant;
import static legend.game.combat.Bttl_800c.allBobjCount_800c66d0;
import static legend.game.combat.Bttl_800c.currentStageData_800c6718;
import static legend.game.combat.Bttl_800c.deffManager_800c693c;
import static legend.game.combat.Bttl_800c.getCombatant;
import static legend.game.combat.Bttl_800c.getCombatantIndex;
import static legend.game.combat.Bttl_800c.melbuStageIndices_800fb064;
import static legend.game.combat.Bttl_800c.monsterCount_800c6768;
import static legend.game.combat.Bttl_800c.scriptState_800c674c;
import static legend.game.combat.Bttl_800c.script_800c66fc;
import static legend.game.combat.Bttl_800c.uniqueMonsterCount_800c6698;
import static legend.game.combat.Bttl_800e.applyStageAmbiance;
import static legend.game.combat.Bttl_800f.loadMonster;

public class SBtld {
  public static final ArrayRef<StageData10> stageData_80109a98 = MEMORY.ref(4, 0x80109a98L, ArrayRef.of(StageData10.class, 0x200, 0x10, StageData10::new));
  public static final ArrayRef<MonsterStats1c> monsterStats_8010ba98 = MEMORY.ref(4, 0x8010ba98L, ArrayRef.of(MonsterStats1c.class, 0x190, 0x1c, MonsterStats1c::new));
  /** TODO 0x80-byte struct array */
  public static final Value _8010e658 = MEMORY.ref(4, 0x8010e658L);

  public static final ArrayRef<Pointer<LodString>> monsterNames_80112068 = MEMORY.ref(4, 0x80112068L, ArrayRef.of(Pointer.classFor(LodString.class), 0x200, 4, Pointer.deferred(4, LodString::new)));

  public static final ArrayRef<EnemyRewards08> enemyRewards_80112868 = MEMORY.ref(4, 0x80112868L, ArrayRef.of(EnemyRewards08.class, 400, 0x8, EnemyRewards08::new));
  public static final Value _801134e8 = MEMORY.ref(2, 0x801134e8L);

  /** BattleStruct4c[71] */
  public static final Value stageAmbiance_801134fc = MEMORY.ref(4, 0x801134fcL);
  /** BattleStruct4c[8] */
  public static final Value dragoonSpaceAmbiance_80114a10 = MEMORY.ref(4, 0x80114a10L);

  public static final Value _8011517c = MEMORY.ref(2, 0x8011517cL);

  @Method(0x80109050L)
  public static void FUN_80109050() {
    final StageData10 stageData = stageData_80109a98.get(encounterId_800bb0f8.get());
    currentStageData_800c6718._00 = stageData._00.get();
    currentStageData_800c6718.musicIndex_04 = stageData.musicIndex_01.get();
    currentStageData_800c6718._08 = stageData._02.get();
    currentStageData_800c6718.postCombatSubmapStage_0c = stageData.postCombatSubmapStage_03.get();
    currentStageData_800c6718._10 = stageData._04.get();
    currentStageData_800c6718._14 = stageData._05.get();
    currentStageData_800c6718.cameraPosIndex0_18 = stageData.cameraPosIndex0_06.get();
    currentStageData_800c6718.cameraPosIndex1_1c = stageData.cameraPosIndex1_08.get();
    currentStageData_800c6718.cameraPosIndex2_20 = stageData.cameraPosIndex2_0a.get();
    currentStageData_800c6718.cameraPosIndex3_24 = stageData.cameraPosIndex3_0c.get();
    currentStageData_800c6718.postCombatSubmapCut_28 = stageData.postCombatSubmapCut_0e.get();

    script_800c66fc = new ScriptFile("player_combat_script", Unpacker.loadFile("player_combat_script").getBytes());

    loadDrgnFile(1, "401", SBtld::FUN_80109170);
  }

  @Method(0x80109170L)
  public static void FUN_80109170(final FileData file) {
    scriptState_800c674c = SCRIPTS.allocateScriptState(5, "DRGN1.401", 0, null);
    scriptState_800c674c.loadScriptFile(new ScriptFile("DRGN1.401", file.getBytes()));

    final int v1;
    if((simpleRand() & 0x8000L) == 0) {
      v1 = currentStageData_800c6718._14;
    } else {
      v1 = currentStageData_800c6718._10;
    }

    //LAB_801091dc
    _800c6748.set(v1 + 1);
    currentCameraPositionIndicesIndex_800c66b0.set(simpleRand() & 3);
    currentCameraIndex_800c6780.set(currentStageData_800c6718.get(currentCameraPositionIndicesIndex_800c66b0.get() + 6));
    _800bc960.or(0x2);
  }

  @Method(0x80109250L)
  public static void battlePrepareSelectedAdditionHitProperties_80109250() {
    //LAB_801092a0
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      final BattlePreloadedEntities_18cb0.AdditionHits100 activeAdditionHits = battlePreloadedEntities_1f8003f4.additionHits_38[charSlot];
      final BattlePreloadedEntities_18cb0.AdditionHits100 activeDragoonAdditionHits = battlePreloadedEntities_1f8003f4.additionHits_38[charSlot + 3];
      final int charIndex = gameState_800babc8.charIds_88[charSlot];

      if(charIndex >= 0) {
        int activeAdditionIndex = gameState_800babc8.charData_32c[charIndex].selectedAddition_19;
        if(charIndex == 5) {
          activeAdditionIndex = activeAdditionIndex + 28;
        }

        //LAB_801092dc
        final int activeDragoonAdditionIndex;
        if(charIndex != 0 || (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 == 0) {
          //LAB_80109308
          activeDragoonAdditionIndex = (int)_801134e8.offset(charIndex * 0x2L).getSigned();
        } else {
          activeDragoonAdditionIndex = (int)_801134e8.offset(0x12L).getSigned();
        }

        //LAB_80109310
        if(activeAdditionIndex < 0) {
          activeAdditionHits.hits_00[0].overlayStartingFrameOffset_1e = 0;
        } else {
          //LAB_80109320
          battleMapSelectedAdditionHitProperties_80109454(_8010e658.offset(activeAdditionIndex * 0x80L).getAddress(), activeAdditionHits);
          EVENTS.postEvent(new BattleMapActiveAdditionHitPropertiesEvent(activeAdditionHits, activeAdditionIndex, charIndex, charSlot, false));

          battleMapSelectedAdditionHitProperties_80109454(_8010e658.offset(activeDragoonAdditionIndex * 0x80L).getAddress(), activeDragoonAdditionHits);
          EVENTS.postEvent(new BattleMapActiveAdditionHitPropertiesEvent(activeDragoonAdditionHits, activeDragoonAdditionIndex, charIndex, charSlot, true));
        }
      }

      //LAB_80109340
    }

    loadFile("encounters", file -> battlePreloadedEntities_1f8003f4.encounterData_00 = new EncounterData38(file.getBytes(), encounterId_800bb0f8.get() * 0x38));
  }

  @Method(0x80109454L)
  public static void battleMapSelectedAdditionHitProperties_80109454(final long mainAdditionHitsTablePtr, final BattlePreloadedEntities_18cb0.AdditionHits100 activeAdditionHits) {
    //LAB_80109460
    for(int i = 0; i < activeAdditionHits.hits_00.length; i++) {
      final BattlePreloadedEntities_18cb0.AdditionHitProperties20 hitIndex = activeAdditionHits.hits_00[i];
      final long additionHitRefCounter = mainAdditionHitsTablePtr + i * 0x10L;

      for(int j = 0; j < hitIndex.length; j++) {
        if(j > 5 && j < 9) {
          hitIndex.set(j, (short)MEMORY.ref(1, additionHitRefCounter).offset(j).getSigned());
          continue;
        }
        hitIndex.set(j, (short)MEMORY.ref(1, additionHitRefCounter).offset(j).get());
      }
    }
  }

  @Method(0x8010955cL)
  public static void allocateEnemyBattleObjects() {
    final BattlePreloadedEntities_18cb0 fp = battlePreloadedEntities_1f8003f4;

    //LAB_801095a0
    for(int i = 0; i < 3; i++) {
      final int enemyIndex = fp.encounterData_00.enemyIndices_00[i] & 0x1ff;
      if(enemyIndex == 0x1ff) {
        break;
      }

      loadSupportOverlay(1, () -> SBtld.FUN_80109808((addCombatant(enemyIndex, -1) << 16) + enemyIndex));
    }

    //LAB_801095ec
    //LAB_801095fc
    for(int i = 0; i < 6; i++) {
      final EncounterData38.EnemyInfo08 s5 = fp.encounterData_00.enemyInfo_08[i];
      final int charIndex = s5.index_00 & 0x1ff;
      if(charIndex == 0x1ff) {
        break;
      }

      final int combatantIndex = getCombatantIndex(charIndex);
      final String name = "Enemy combatant index " + combatantIndex;
      final ScriptState<MonsterBattleObject> state = SCRIPTS.allocateScriptState(name, new MonsterBattleObject(name));
      state.setTicker(Bttl_800c::bobjTicker);
      state.setDestructor(Bttl_800c::bobjDestructor);
      battleState_8006e398.allBobjs_e0c[allBobjCount_800c66d0.get()] = state;
      battleState_8006e398.monsterBobjs_e50[monsterCount_800c6768.get()] = state;
      final BattleObject27c data = state.innerStruct_00;
      data.magic_00 = BattleScriptDataBase.BOBJ;
      data.charId_272 = charIndex;
      data.bobjIndex_274 = allBobjCount_800c66d0.get();
      data.charSlot_276 = monsterCount_800c6768.get();
      data.combatant_144 = getCombatant(combatantIndex);
      data.combatantIndex_26c = combatantIndex;
      data.model_148.coord2_14.coord.transfer.set(s5.pos_02);
      data.model_148.coord2Param_64.rotate.set(0.0f, MathHelper.TWO_PI * 0.75f, 0.0f);
      state.storage_44[7] |= 0x4;
      allBobjCount_800c66d0.incr();
      monsterCount_800c6768.incr();
    }

    //LAB_8010975c
    battleState_8006e398.allBobjs_e0c[allBobjCount_800c66d0.get()] = null;
    battleState_8006e398.monsterBobjs_e50[monsterCount_800c6768.get()] = null;

    //LAB_801097ac
    for(int i = 0; i < monsterCount_800c6768.get(); i++) {
      loadMonster(battleState_8006e398.monsterBobjs_e50[i]);
    }
  }

  @Method(0x80109808L)
  public static void FUN_80109808(final int enemyAndCombatantId) {
    final int enemyId = enemyAndCombatantId & 0xffff;
    final int combatantIndex = enemyAndCombatantId >>> 16;
    final CombatantStruct1a8 combatant = getCombatant(combatantIndex);
    final EnemyRewards08 rewards = enemyRewards_80112868.get(enemyId);

    combatant.drops.clear();
    if(rewards.itemDrop_05.get() != 0xff) {
      combatant.drops.add(new CombatantStruct1a8.ItemDrop(rewards.itemChance_04.get(), rewards.itemDrop_05.get()));
    }

    final EnemyRewardsEvent event = EVENTS.postEvent(new EnemyRewardsEvent(enemyId, rewards.xp_00.get(), rewards.gold_02.get(), combatant.drops));

    combatant.xp_194 = event.xp;
    combatant.gold_196 = event.gold;
    combatant._19a = rewards._06.get();

    loadDrgnFile(1, Integer.toString(enemyId + 1), file -> FUN_8010989c(file.getBytes(), combatantIndex));
  }

  @Method(0x8010989cL)
  public static void FUN_8010989c(final byte[] file, final int index) {
    getCombatant(index).scriptPtr_10 = new ScriptFile("Combatant " + index, file);
    uniqueMonsterCount_800c6698.add(1);
  }

  @Method(0x801098f4L)
  public static void loadStageAmbiance() {
    final DeffManager7cc deffManager = deffManager_800c693c;
    final int stage = Math.max(0, combatStage_800bb0f4.get());

    //LAB_8010993c
    //LAB_80109954
    deffManager.stageAmbiance_4c.set(ByteBuffer.wrap(MEMORY.getBytes(stageAmbiance_801134fc.offset(stage * 0x4c).getAddress(), 0x4c)).order(ByteOrder.LITTLE_ENDIAN));

    applyStageAmbiance(deffManager.stageAmbiance_4c);

    //LAB_8010999c
    final ByteBuffer buffer = ByteBuffer.wrap(MEMORY.getBytes(dragoonSpaceAmbiance_80114a10.getAddress(), 0x4c * 8)).order(ByteOrder.LITTLE_ENDIAN);
    for(int i = 0; i < deffManager.dragoonSpaceAmbiance_98.length; i++) {
      deffManager.dragoonSpaceAmbiance_98[i].set(buffer);
    }

    deffManager._00._00 = (int)_8011517c.offset(combatStage_800bb0f4.get() * 0x8L).offset(2, 0x00L).get();
    deffManager._00._02 = (int)_8011517c.offset(combatStage_800bb0f4.get() * 0x8L).offset(2, 0x02L).get();
    deffManager._00._04 = (int)_8011517c.offset(combatStage_800bb0f4.get() * 0x8L).offset(2, 0x04L).get();

    //LAB_80109a30
    for(int i = 0; melbuStageIndices_800fb064.get(i).get() != -1; i++) {
      deffManager._08[i]._00 = (int)_8011517c.offset(melbuStageIndices_800fb064.get(i).get() * 0x8L).offset(2, 0x00L).get();
      deffManager._08[i]._02 = (int)_8011517c.offset(melbuStageIndices_800fb064.get(i).get() * 0x8L).offset(2, 0x02L).get();
    }
  }
}
