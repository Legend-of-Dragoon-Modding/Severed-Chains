package legend.game.combat;

import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.Pointer;
import legend.game.combat.deff.DeffManager7cc;
import legend.game.combat.types.BattleObject27c;
import legend.game.combat.types.BattleScriptDataBase;
import legend.game.combat.types.BattlePreloadedEntities_18cb0;
import legend.game.combat.types.CombatantStruct1a8;
import legend.game.combat.types.EncounterData38;
import legend.game.combat.types.EnemyRewards08;
import legend.game.combat.types.MonsterStats1c;
import legend.game.combat.types.StageData10;
import legend.game.modding.events.EventManager;
import legend.game.modding.events.characters.BattleMapActiveAdditionHitPropertiesEvent;
import legend.game.modding.events.combat.EnemyRewardsEvent;
import legend.game.scripting.ScriptFile;
import legend.game.scripting.ScriptState;
import legend.game.types.LodString;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Unpacker;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static legend.core.GameEngine.MEMORY;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.Scus94491BpeSegment.battlePreloadedEntities_1f8003f4;
import static legend.game.Scus94491BpeSegment.decrementOverlayCount;
import static legend.game.Scus94491BpeSegment.loadDrgnFile;
import static legend.game.Scus94491BpeSegment.loadFile;
import static legend.game.Scus94491BpeSegment.loadSupportOverlay;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment_8006._8006e398;
import static legend.game.Scus94491BpeSegment_800b._800bc960;
import static legend.game.Scus94491BpeSegment_800b.combatStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.combat.Bttl_800c._800c66b0;
import static legend.game.combat.Bttl_800c._800c66d0;
import static legend.game.combat.Bttl_800c._800c6718;
import static legend.game.combat.Bttl_800c._800c6748;
import static legend.game.combat.Bttl_800c._800c6780;
import static legend.game.combat.Bttl_800c.addCombatant;
import static legend.game.combat.Bttl_800c.deffManager_800c693c;
import static legend.game.combat.Bttl_800c.getCombatant;
import static legend.game.combat.Bttl_800c.getCombatantIndex;
import static legend.game.combat.Bttl_800c.monsterCount_800c6768;
import static legend.game.combat.Bttl_800c.scriptState_800c674c;
import static legend.game.combat.Bttl_800c.script_800c66fc;
import static legend.game.combat.Bttl_800c.melbuStageIndices_800fb064;
import static legend.game.combat.Bttl_800c.uniqueMonsterCount_800c6698;
import static legend.game.combat.Bttl_800e.FUN_800e5768;
import static legend.game.combat.Bttl_800f.loadMonster;

public class SBtld {
  private static final Value bpe_800fb77c = MEMORY.ref(4, 0x800fb77cL);

  public static final ArrayRef<StageData10> stageData_80109a98 = MEMORY.ref(4, 0x80109a98L, ArrayRef.of(StageData10.class, 0x200, 0x10, StageData10::new));
  public static final ArrayRef<MonsterStats1c> monsterStats_8010ba98 = MEMORY.ref(4, 0x8010ba98L, ArrayRef.of(MonsterStats1c.class, 0x190, 0x1c, MonsterStats1c::new));
  /** TODO 0x80-byte struct array */
  public static final Value _8010e658 = MEMORY.ref(4, 0x8010e658L);

  public static final ArrayRef<Pointer<LodString>> enemyNames_80112068 = MEMORY.ref(4, 0x80112068L, ArrayRef.of(Pointer.classFor(LodString.class), 0x200, 4, Pointer.deferred(4, LodString::new)));

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
    _800c6718.offset(0x00L).setu(stageData._00.get());
    _800c6718.offset(0x04L).setu(stageData.musicIndex_01.get());
    _800c6718.offset(0x08L).setu(stageData._02.get());
    _800c6718.offset(0x0cL).setu(stageData._03.get());
    _800c6718.offset(0x10L).setu(stageData._04.get());
    _800c6718.offset(0x14L).setu(stageData._05.get());
    _800c6718.offset(0x18L).setu(stageData._06.get());
    _800c6718.offset(0x1cL).setu(stageData._08.get());
    _800c6718.offset(0x20L).setu(stageData._0a.get());
    _800c6718.offset(0x24L).setu(stageData._0c.get());
    _800c6718.offset(0x28L).setu(stageData._0e.get());

    final byte[] archive = MEMORY.getBytes(bpe_800fb77c.getAddress(), 26836);
    script_800c66fc = new ScriptFile("S_BTLD BPE @ 800fb77c", Unpacker.decompress(new FileData(archive)));

    loadDrgnFile(1, "401", SBtld::FUN_80109170);
  }

  @Method(0x80109170L)
  public static void FUN_80109170(final FileData file) {
    scriptState_800c674c = SCRIPTS.allocateScriptState(5, null, 0, null);
    scriptState_800c674c.loadScriptFile(new ScriptFile("DRGN1.401", file.getBytes()));

    final long v1;
    if((simpleRand() & 0x8000L) == 0) {
      v1 = 0x14L;
    } else {
      v1 = 0x10L;
    }

    //LAB_801091dc
    _800c6748.set((int)_800c6718.offset(v1).get() + 1);
    _800c66b0.set(simpleRand() & 3);
    _800c6780.set((int)_800c6718.offset((_800c66b0.get() + 6) * 0x4L).get());
    _800bc960.or(0x2);
    decrementOverlayCount();
  }

  @Method(0x80109250L)
  public static void battlePrepareSelectedAdditionHitProperties_80109250() {
    //LAB_801092a0
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      final BattlePreloadedEntities_18cb0.AdditionHits_100 activeAdditionHits = battlePreloadedEntities_1f8003f4.additionHits_38[charSlot];
      final BattlePreloadedEntities_18cb0.AdditionHits_100 activeDragoonAdditionHits = battlePreloadedEntities_1f8003f4.additionHits_38[charSlot + 3];
      final int charIndex = gameState_800babc8.charIndex_88.get(charSlot).get();

      if(charIndex >= 0) {
        int activeAdditionIndex = gameState_800babc8.charData_32c.get(charIndex).selectedAddition_19.get();
        if(charIndex == 5) {
          activeAdditionIndex = activeAdditionIndex + 28;
        }

        //LAB_801092dc
        final long activeDragoonAdditionIndex;
        if(charIndex != 0 || (gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xff) >>> 7 == 0) {
          //LAB_80109308
          activeDragoonAdditionIndex = _801134e8.offset(charIndex * 0x2L).getSigned();
        } else {
          activeDragoonAdditionIndex = _801134e8.offset(0x12L).getSigned();
        }

        //LAB_80109310
        if(activeAdditionIndex < 0) {
          activeAdditionHits.hits_00[0].hitProperty_00[15] = 0;
        } else {
          //LAB_80109320
          battleMapSelectedAdditionHitProperties_80109454(_8010e658.offset(activeAdditionIndex * 0x80L).getAddress(), activeAdditionHits);
          EventManager.INSTANCE.postEventAndApply(new BattleMapActiveAdditionHitPropertiesEvent(activeAdditionHits, activeAdditionIndex, charIndex, charSlot, false));

          battleMapSelectedAdditionHitProperties_80109454(_8010e658.offset(activeDragoonAdditionIndex * 0x80L).getAddress(), activeDragoonAdditionHits);
          EventManager.INSTANCE.postEventAndApply(new BattleMapActiveAdditionHitPropertiesEvent(activeAdditionHits, activeAdditionIndex, charIndex, charSlot, true));
        }
      }

      //LAB_80109340
    }

    loadFile("encounters", file -> battlePreloadedEntities_1f8003f4.encounterData_00 = new EncounterData38(file.getBytes(), encounterId_800bb0f8.get() * 0x38));

    decrementOverlayCount();
  }

  @Method(0x80109454L)
  public static void battleMapSelectedAdditionHitProperties_80109454(final long mainAdditionHitsTablePtr, final BattlePreloadedEntities_18cb0.AdditionHits_100 activeAdditionHits) {
    //LAB_80109460
    for(int i = 0; i < activeAdditionHits.hits_00.length; i++) {
      final BattlePreloadedEntities_18cb0.AdditionHitProperties_20 hitIndex = activeAdditionHits.hits_00[i];
      final long additionHitRefCounter = mainAdditionHitsTablePtr + i * 0x10L;

      for(int j = 0; j < hitIndex.hitProperty_00.length; j++) {
        if(j > 5 && j < 9) {
          hitIndex.hitProperty_00[j] = (short)MEMORY.ref(1, additionHitRefCounter).offset(j).getSigned();
          continue;
        }
        hitIndex.hitProperty_00[j] = (short)MEMORY.ref(1, additionHitRefCounter).offset(j).get();
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
      final ScriptState<BattleObject27c> state = SCRIPTS.allocateScriptState(new BattleObject27c("Enemy combatant index " + combatantIndex));
      state.setTicker(Bttl_800c::bobjTicker);
      state.setDestructor(Bttl_800c::bobjDestructor);
      _8006e398.bobjIndices_e0c[_800c66d0.get()] = state;
      _8006e398.bobjIndices_e50[monsterCount_800c6768.get()] = state;
      final BattleObject27c data = state.innerStruct_00;
      data.magic_00 = BattleScriptDataBase.BOBJ;
      data.charIndex_272 = charIndex;
      data._274 = _800c66d0.get();
      data.charSlot_276 = monsterCount_800c6768.get();
      data.combatant_144 = getCombatant(combatantIndex);
      data.combatantIndex_26c = combatantIndex;
      data.model_148.coord2_14.coord.transfer.set(s5.pos_02);
      data.model_148.coord2Param_64.rotate.set((short)0, (short)0xc01, (short)0);
      state.storage_44[7] |= 0x4;
      _800c66d0.incr();
      monsterCount_800c6768.incr();
    }

    //LAB_8010975c
    _8006e398.bobjIndices_e0c[_800c66d0.get()] = null;
    _8006e398.bobjIndices_e50[monsterCount_800c6768.get()] = null;

    //LAB_801097ac
    for(int i = 0; i < monsterCount_800c6768.get(); i++) {
      loadMonster(_8006e398.bobjIndices_e50[i]);
    }

    //LAB_801097d0
    decrementOverlayCount();
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

    final EnemyRewardsEvent event = EventManager.INSTANCE.postEvent(new EnemyRewardsEvent(enemyId, rewards.xp_00.get(), rewards.gold_02.get(), combatant.drops));

    combatant.xp_194 = event.xp;
    combatant.gold_196 = event.gold;
    combatant._19a = rewards._06.get();

    loadDrgnFile(1, Integer.toString(enemyId + 1), file -> FUN_8010989c(file.getBytes(), combatantIndex));
  }

  @Method(0x8010989cL)
  public static void FUN_8010989c(final byte[] file, final int index) {
    getCombatant(index).scriptPtr_10 = new ScriptFile("Combatant " + index, file);
    uniqueMonsterCount_800c6698.add(1);
    decrementOverlayCount();
  }

  @Method(0x801098f4L)
  public static void FUN_801098f4() {
    final DeffManager7cc struct7cc = deffManager_800c693c;
    final int stage = Math.max(0, combatStage_800bb0f4.get());

    //LAB_8010993c
    //LAB_80109954
    struct7cc.stageAmbiance_4c.set(ByteBuffer.wrap(MEMORY.getBytes(stageAmbiance_801134fc.offset(stage * 0x4c).getAddress(), 0x4c)).order(ByteOrder.LITTLE_ENDIAN));

    FUN_800e5768(struct7cc.stageAmbiance_4c);

    //LAB_8010999c
    final ByteBuffer buffer = ByteBuffer.wrap(MEMORY.getBytes(dragoonSpaceAmbiance_80114a10.getAddress(), 0x4c * 8)).order(ByteOrder.LITTLE_ENDIAN);
    for(int i = 0; i < struct7cc.dragoonSpaceAmbiance_98.length; i++) {
      struct7cc.dragoonSpaceAmbiance_98[i].set(buffer);
    }

    struct7cc._00._00 = (int)_8011517c.offset(combatStage_800bb0f4.get() * 0x8L).offset(2, 0x00L).get();
    struct7cc._00._02 = (int)_8011517c.offset(combatStage_800bb0f4.get() * 0x8L).offset(2, 0x02L).get();
    struct7cc._00._04 = (int)_8011517c.offset(combatStage_800bb0f4.get() * 0x8L).offset(2, 0x04L).get();

    //LAB_80109a30
    for(int i = 0; melbuStageIndices_800fb064.get(i).get() != -1; i++) {
      struct7cc._08[i]._00 = (int)_8011517c.offset(melbuStageIndices_800fb064.get(i).get() * 0x8L).offset(2, 0x00L).get();
      struct7cc._08[i]._02 = (int)_8011517c.offset(melbuStageIndices_800fb064.get(i).get() * 0x8L).offset(2, 0x02L).get();
    }

    //LAB_80109a80
    decrementOverlayCount();
  }
}
