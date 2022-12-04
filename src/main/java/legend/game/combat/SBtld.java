package legend.game.combat;

import legend.core.gte.DVECTOR;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.TriConsumerRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.game.combat.types.BattleObject27c;
import legend.game.combat.types.BattleScriptDataBase;
import legend.game.combat.types.BattleStruct4c;
import legend.game.combat.types.BattleStruct7cc;
import legend.game.combat.types.CombatantStruct1a8;
import legend.game.combat.types.MonsterStats1c;
import legend.game.combat.types.StageData10;
import legend.game.types.LodString;
import legend.game.types.ScriptFile;
import legend.game.types.ScriptState;

import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getConsumerAddress;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.Scus94491BpeSegment._1f8003f4;
import static legend.game.Scus94491BpeSegment.allocateScriptState;
import static legend.game.Scus94491BpeSegment.decompress;
import static legend.game.Scus94491BpeSegment.decrementOverlayCount;
import static legend.game.Scus94491BpeSegment.loadDrgnBinFile;
import static legend.game.Scus94491BpeSegment.loadScriptFile;
import static legend.game.Scus94491BpeSegment.loadSupportOverlay;
import static legend.game.Scus94491BpeSegment.memcpy;
import static legend.game.Scus94491BpeSegment.setScriptDestructor;
import static legend.game.Scus94491BpeSegment.setScriptTicker;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment_8006._8006e398;
import static legend.game.Scus94491BpeSegment_800b._800bc960;
import static legend.game.Scus94491BpeSegment_800b.combatStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.combat.Bttl_800c._800c66b0;
import static legend.game.combat.Bttl_800c._800c66d0;
import static legend.game.combat.Bttl_800c._800c66d8;
import static legend.game.combat.Bttl_800c._800c6718;
import static legend.game.combat.Bttl_800c._800c6748;
import static legend.game.combat.Bttl_800c.monsterCount_800c6768;
import static legend.game.combat.Bttl_800c._800c6780;
import static legend.game.combat.Bttl_800c.addCombatant;
import static legend.game.combat.Bttl_800c.getCombatant;
import static legend.game.combat.Bttl_800c.getCombatantIndex;
import static legend.game.combat.Bttl_800c.scriptIndex_800c674c;
import static legend.game.combat.Bttl_800c.script_800c66fc;
import static legend.game.combat.Bttl_800c.script_800c670c;
import static legend.game.combat.Bttl_800c.stageIndices_800fb064;
import static legend.game.combat.Bttl_800c.struct7cc_800c693c;
import static legend.game.combat.Bttl_800c.uniqueMonsterCount_800c6698;
import static legend.game.combat.Bttl_800e.FUN_800e5768;
import static legend.game.combat.Bttl_800f.loadMonster;

public class SBtld {
  private static final Value bpe_800fb77c = MEMORY.ref(4, 0x800fb77cL);

  /** TODO 0x38-byte struct array */
  public static final Value _80102050 = MEMORY.ref(4, 0x80102050L);

  public static final ArrayRef<StageData10> stageData_80109a98 = MEMORY.ref(4, 0x80109a98L, ArrayRef.of(StageData10.class, 0x200, 0x10, StageData10::new));
  public static final ArrayRef<MonsterStats1c> monsterStats_8010ba98 = MEMORY.ref(4, 0x8010ba98L, ArrayRef.of(MonsterStats1c.class, 0x190, 0x1c, MonsterStats1c::new));
  /** TODO 0x80-byte struct array */
  public static final Value _8010e658 = MEMORY.ref(4, 0x8010e658L);

  public static final ArrayRef<Pointer<LodString>> enemyNames_80112068 = MEMORY.ref(4, 0x80112068L, ArrayRef.of(Pointer.classFor(LodString.class), 0x200, 4, Pointer.deferred(4, LodString::new)));

  /** TODO 0x8-byte struct */
  public static final Value _80112868 = MEMORY.ref(4, 0x80112868L);

  public static final Value _801134e8 = MEMORY.ref(2, 0x801134e8L);

  public static final UnboundedArrayRef<BattleStruct4c> _801134fc = MEMORY.ref(4, 0x801134fcL, UnboundedArrayRef.of(0x4c, BattleStruct4c::new));

  public static final ArrayRef<BattleStruct4c> _80114a10 = MEMORY.ref(4, 0x80114a10L, ArrayRef.of(BattleStruct4c.class, 8, 0x4c, BattleStruct4c::new));

  public static final Value _8011517c = MEMORY.ref(2, 0x8011517cL);

  @Method(0x80109050L)
  public static void FUN_80109050(final int param) {
    final StageData10 stageData = stageData_80109a98.get(encounterId_800bb0f8.get());
    _800c6718.offset(0x00L).setu(stageData._00.get());
    _800c6718.offset(0x04L).setu(stageData._01.get());
    _800c6718.offset(0x08L).setu(stageData._02.get());
    _800c6718.offset(0x0cL).setu(stageData._03.get());
    _800c6718.offset(0x10L).setu(stageData._04.get());
    _800c6718.offset(0x14L).setu(stageData._05.get());
    _800c6718.offset(0x18L).setu(stageData._06.get());
    _800c6718.offset(0x1cL).setu(stageData._08.get());
    _800c6718.offset(0x20L).setu(stageData._0a.get());
    _800c6718.offset(0x24L).setu(stageData._0c.get());
    _800c6718.offset(0x28L).setu(stageData._0e.get());

    decompress(bpe_800fb77c.getAddress(), 0, getMethodAddress(SBtld.class, "btldBpeDecompressed", long.class, long.class, long.class), 0, 0);
    loadDrgnBinFile(1, 401, 0, getMethodAddress(SBtld.class, "FUN_80109170", long.class, long.class, long.class), 0, 0x2L);
  }

  @Method(0x80109164L)
  public static void btldBpeDecompressed(final long address, final long fileSize, final long param) {
    script_800c66fc.setPointer(address);
  }

  @Method(0x80109170L)
  public static void FUN_80109170(final long address, final long fileSize, final long param) {
    script_800c670c.set(MEMORY.ref(4, address, ScriptFile::new));
    scriptIndex_800c674c.setu(allocateScriptState(5, 0, false, null, 0, null));
    loadScriptFile((int)scriptIndex_800c674c.get(), script_800c670c.deref());

    final long v1;
    if((simpleRand() & 0x8000L) == 0) {
      v1 = 0x14L;
    } else {
      v1 = 0x10L;
    }

    //LAB_801091dc
    _800c6748.setu(_800c6718.offset(v1).get() + 0x1L);
    _800c66b0.setu(simpleRand() & 0x3L);
    _800c6780.setu(_800c6718.offset((_800c66b0.get() + 0x6L) * 0x4L).get());
    _800bc960.oru(0x2L);
    decrementOverlayCount();
  }

  @Method(0x80109250L)
  public static void FUN_80109250(final int param) {
    long s1 = _1f8003f4.getPointer() + 0x38L; //TODO

    //LAB_801092a0
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      final int charIndex = gameState_800babc8.charIndex_88.get(charSlot).get();

      if(charIndex >= 0) {
        int addition = gameState_800babc8.charData_32c.get(charIndex).selectedAddition_19.get();
        if(charIndex == 5) {
          addition = addition + 28;
        }

        //LAB_801092dc
        final long s0;
        if(charIndex != 0 || (gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xff) >>> 7 == 0) {
          //LAB_80109308
          s0 = _801134e8.offset(charIndex * 0x2L).getSigned();
        } else {
          s0 = _801134e8.offset(0x12L).getSigned();
        }

        //LAB_80109310
        if(addition < 0) {
          MEMORY.ref(2, s1).offset(0x1eL).setu(0);
        } else {
          //LAB_80109320
          FUN_80109454(_8010e658.offset(addition * 0x80L).getAddress(), s1);
          FUN_80109454(_8010e658.offset(s0 * 0x80L).getAddress(), s1 + 0x300L);
        }

        //LAB_8010933c
        s1 = s1 + 0x100L;
      }

      //LAB_80109340
    }

    memcpy(_1f8003f4.getPointer(), _80102050.offset(encounterId_800bb0f8.get() * 0x38L).getAddress(), 0x38);

    decrementOverlayCount();
  }

  @Method(0x80109454L)
  public static void FUN_80109454(final long a3, final long a1) {
    //LAB_80109460
    for(int i = 0; i < 8; i++) {
      final long a0 = a1 + i * 0x20L;
      final long v1 = a3 + i * 0x10L;
      MEMORY.ref(2, a0).offset(0x00L).setu(MEMORY.ref(1, v1).offset(0x0L).get());
      MEMORY.ref(2, a0).offset(0x02L).setu(MEMORY.ref(1, v1).offset(0x1L).get());
      MEMORY.ref(2, a0).offset(0x04L).setu(MEMORY.ref(1, v1).offset(0x2L).get());
      MEMORY.ref(2, a0).offset(0x06L).setu(MEMORY.ref(1, v1).offset(0x3L).get());
      MEMORY.ref(2, a0).offset(0x08L).setu(MEMORY.ref(1, v1).offset(0x4L).get());
      MEMORY.ref(2, a0).offset(0x0aL).setu(MEMORY.ref(1, v1).offset(0x5L).get());
      MEMORY.ref(2, a0).offset(0x0cL).setu(MEMORY.ref(1, v1).offset(0x6L).getSigned());
      MEMORY.ref(2, a0).offset(0x0eL).setu(MEMORY.ref(1, v1).offset(0x7L).getSigned());
      MEMORY.ref(2, a0).offset(0x10L).setu(MEMORY.ref(1, v1).offset(0x8L).getSigned());
      MEMORY.ref(2, a0).offset(0x12L).setu(MEMORY.ref(1, v1).offset(0x9L).get());
      MEMORY.ref(2, a0).offset(0x14L).setu(MEMORY.ref(1, v1).offset(0xaL).get());
      MEMORY.ref(2, a0).offset(0x16L).setu(MEMORY.ref(1, v1).offset(0xbL).get());
      MEMORY.ref(2, a0).offset(0x18L).setu(MEMORY.ref(1, v1).offset(0xcL).get());
      MEMORY.ref(2, a0).offset(0x1aL).setu(MEMORY.ref(1, v1).offset(0xdL).get());
      MEMORY.ref(2, a0).offset(0x1cL).setu(MEMORY.ref(1, v1).offset(0xeL).get());
      MEMORY.ref(2, a0).offset(0x1eL).setu(MEMORY.ref(1, v1).offset(0xfL).get());
    }
  }

  @Method(0x8010955cL)
  public static void allocateEnemyBattleObjects(final int a0) {
    final long fp = _1f8003f4.getPointer(); //TODO

    //LAB_801095a0
    for(int i = 0; i < 3; i++) {
      final int s2 = (int)(MEMORY.ref(2, fp).offset(i * 0x2L).get() & 0x1ff);
      if(s2 == 0x1ff) {
        break;
      }

      loadSupportOverlay(1, getConsumerAddress(SBtld.class, "FUN_80109808", int.class), (addCombatant(s2, -1) << 16) + s2);
    }

    //LAB_801095ec
    //LAB_801095fc
    for(int i = 0; i < 6; i++) {
      final long s5 = fp + i * 0x8L;
      final int charIndex = (int)(MEMORY.ref(2, s5).offset(0x8L).get() & 0x1ff);
      if(charIndex == 0x1ff) {
        break;
      }

      final int combatantIndex = getCombatantIndex(charIndex);
      final int bobjIndex = allocateScriptState(0x27c, BattleObject27c::new);
      setScriptTicker(bobjIndex, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "bobjTicker", int.class, ScriptState.classFor(BattleObject27c.class), BattleObject27c.class), TriConsumerRef::new));
      setScriptDestructor(bobjIndex, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "bobjDestructor", int.class, ScriptState.classFor(BattleObject27c.class), BattleObject27c.class), TriConsumerRef::new));
      _8006e398.bobjIndices_e0c.get(_800c66d0.get()).set(bobjIndex);
      _8006e398.bobjIndices_e50.get(monsterCount_800c6768.get()).set(bobjIndex);
      final ScriptState<BattleObject27c> state = scriptStatePtrArr_800bc1c0.get(bobjIndex).derefAs(ScriptState.classFor(BattleObject27c.class));
      final BattleObject27c data = state.innerStruct_00.deref();
      data.magic_00.set(BattleScriptDataBase.BOBJ);
      data.charIndex_272.set((short)charIndex);
      data._274.set((short)_800c66d0.get());
      data.charSlot_276.set((short)monsterCount_800c6768.get());
      data.combatant_144.set(getCombatant(combatantIndex));
      data.combatantIndex_26c.set((short)combatantIndex);
      data.model_148.coord2_14.coord.transfer.setX((int)MEMORY.ref(2, s5).offset(0xaL).getSigned());
      data.model_148.coord2_14.coord.transfer.setY((int)MEMORY.ref(2, s5).offset(0xcL).getSigned());
      data.model_148.coord2_14.coord.transfer.setZ((int)MEMORY.ref(2, s5).offset(0xeL).getSigned());
      data.model_148.coord2Param_64.rotate.set((short)0, (short)0xc01, (short)0);
      state.ui_60.or(0x4L);
      _800c66d0.incr();
      monsterCount_800c6768.incr();
    }

    //LAB_8010975c
    _8006e398.bobjIndices_e0c.get(_800c66d0.get()).set(-1);
    _8006e398.bobjIndices_e50.get(monsterCount_800c6768.get()).set(-1);

    //LAB_801097ac
    for(int i = 0; i < monsterCount_800c6768.get(); i++) {
      loadMonster(_8006e398.bobjIndices_e50.get(i).get());
    }

    //LAB_801097d0
    decrementOverlayCount();
  }

  @Method(0x80109808L)
  public static void FUN_80109808(final int param) {
    final int fileIndex = param & 0xffff;
    final int s0 = param >>> 16;
    final CombatantStruct1a8 v0 = getCombatant(s0);
    final long v1 = _80112868.offset(fileIndex * 0x8L).getAddress(); //TODO
    v0._194.set(MEMORY.ref(4, v1).offset(0x0L).get());
    v0._198.set(MEMORY.ref(4, v1).offset(0x4L).get());
    loadDrgnBinFile(1, fileIndex + 1, 0, getMethodAddress(SBtld.class, "FUN_8010989c", long.class, long.class, long.class), s0, 0x2L);
  }

  @Method(0x8010989cL)
  public static void FUN_8010989c(final long address, final long fileSize, final long index) {
    final ScriptFile script = MEMORY.ref(4, address, ScriptFile::new);

    getCombatant((int)index).filePtr_10.set(script.getAddress());
    _800c66d8.offset(uniqueMonsterCount_800c6698.get() * 0x4L).setu(script.getAddress()); //TODO
    uniqueMonsterCount_800c6698.add(1);
    decrementOverlayCount();
  }

  @Method(0x801098f4L)
  public static void FUN_801098f4(final int param) {
    final BattleStruct7cc struct7cc = struct7cc_800c693c.deref();
    final int stage = Math.max(0, combatStage_800bb0f4.get());

    //LAB_8010993c
    //LAB_80109954
    memcpy(struct7cc._4c.getAddress(), _801134fc.get(stage).getAddress(), 0x4c);

    FUN_800e5768(struct7cc._4c);

    //LAB_8010999c
    memcpy(struct7cc._98.getAddress(), _80114a10.getAddress(), 0x260);
    memcpy(struct7cc.svec_00.getAddress(), _8011517c.offset(combatStage_800bb0f4.get() * 0x8L).getAddress(), 0x8);

    //LAB_80109a30
    for(int i = 0; stageIndices_800fb064.offset(i).get() != 0xffL; i++) {
      struct7cc.dvecs_08.get(i).set(_8011517c.offset(stageIndices_800fb064.offset(i).get() * 0x8L).cast(DVECTOR::new));
    }

    //LAB_80109a80
    decrementOverlayCount();
  }
}
