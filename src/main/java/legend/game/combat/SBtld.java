package legend.game.combat;

import legend.core.Tuple;
import legend.core.gte.DVECTOR;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.TriConsumerRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.game.combat.types.BattleScriptDataBase;
import legend.game.combat.types.BattleStruct4c;
import legend.game.combat.types.BattleStruct7cc;
import legend.game.combat.types.CombatantStruct1a8;
import legend.game.combat.types.BattleObject27c;
import legend.game.combat.types.MonsterStats1c;
import legend.game.types.LodString;
import legend.game.types.ScriptFile;
import legend.game.types.ScriptState;

import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.Scus94491BpeSegment.loadAndRunOverlay;
import static legend.game.Scus94491BpeSegment.FUN_80012bb4;
import static legend.game.Scus94491BpeSegment.memcpy;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment._1f8003f4;
import static legend.game.Scus94491BpeSegment.allocateScriptState;
import static legend.game.Scus94491BpeSegment.decompress;
import static legend.game.Scus94491BpeSegment.loadDrgnBinFile;
import static legend.game.Scus94491BpeSegment.loadScriptFile;
import static legend.game.Scus94491BpeSegment.setScriptTicker;
import static legend.game.Scus94491BpeSegment.setScriptDestructor;
import static legend.game.Scus94491BpeSegment_8005._8005e398_SCRIPT_SIZES;
import static legend.game.Scus94491BpeSegment_8006._8006e398;
import static legend.game.Scus94491BpeSegment_800b._800bc960;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.submapStage_800bb0f4;
import static legend.game.combat.Bttl_800c.struct7cc_800c693c;
import static legend.game.combat.Bttl_800c.stageIndices_800fb064;
import static legend.game.combat.Bttl_800c.addCombatant;
import static legend.game.combat.Bttl_800c.getCombatantIndex;
import static legend.game.combat.Bttl_800c._800c6698;
import static legend.game.combat.Bttl_800c._800c66b0;
import static legend.game.combat.Bttl_800c._800c66d0;
import static legend.game.combat.Bttl_800c._800c66d8;
import static legend.game.combat.Bttl_800c._800c6718;
import static legend.game.combat.Bttl_800c._800c6748;
import static legend.game.combat.Bttl_800c._800c6768;
import static legend.game.combat.Bttl_800c._800c6780;
import static legend.game.combat.Bttl_800c.getCombatant;
import static legend.game.combat.Bttl_800c.scriptIndex_800c674c;
import static legend.game.combat.Bttl_800c.script_800c66fc;
import static legend.game.combat.Bttl_800c.script_800c66fc_length;
import static legend.game.combat.Bttl_800c.script_800c670c;
import static legend.game.combat.Bttl_800e.FUN_800e5768;
import static legend.game.combat.Bttl_800f.loadMonster;

public class SBtld {
  private static final Value bpe_800fb77c = MEMORY.ref(4, 0x800fb77cL);

  /** TODO 0x38-byte struct array */
  public static final Value _80102050 = MEMORY.ref(4, 0x80102050L);

  /** TODO 0x10-byte struct */
  public static final Value _80109a98 = MEMORY.ref(4, 0x80109a98L);

  public static final ArrayRef<MonsterStats1c> monsterStats_8010ba98 = MEMORY.ref(4, 0x8010ba98L, ArrayRef.of(MonsterStats1c.class, 0x200, 0x1c, MonsterStats1c::new));

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
  public static void FUN_80109050(final long param) {
    final long v1 = _80109a98.offset(encounterId_800bb0f8.get() * 0x10L).getAddress();
    _800c6718.offset(0x00L).setu(MEMORY.ref(1, v1).offset(0x0L).get());
    _800c6718.offset(0x04L).setu(MEMORY.ref(1, v1).offset(0x1L).get());
    _800c6718.offset(0x08L).setu(MEMORY.ref(1, v1).offset(0x2L).get());
    _800c6718.offset(0x0cL).setu(MEMORY.ref(1, v1).offset(0x3L).get());
    _800c6718.offset(0x10L).setu(MEMORY.ref(1, v1).offset(0x4L).get());
    _800c6718.offset(0x14L).setu(MEMORY.ref(1, v1).offset(0x5L).get());
    _800c6718.offset(0x18L).setu(MEMORY.ref(2, v1).offset(0x6L).getSigned());
    _800c6718.offset(0x1cL).setu(MEMORY.ref(2, v1).offset(0x8L).getSigned());
    _800c6718.offset(0x20L).setu(MEMORY.ref(2, v1).offset(0xaL).getSigned());
    _800c6718.offset(0x24L).setu(MEMORY.ref(2, v1).offset(0xcL).getSigned());
    _800c6718.offset(0x28L).setu(MEMORY.ref(2, v1).offset(0xeL).get());

    decompress(bpe_800fb77c.getAddress(), 0, getMethodAddress(SBtld.class, "btldBpeDecompressed", long.class, long.class, long.class), 0, 0);
    loadDrgnBinFile(1, 401, 0, getMethodAddress(SBtld.class, "FUN_80109170", long.class, long.class, long.class), 0, 0x2L);
  }

  @Method(0x80109164L)
  public static void btldBpeDecompressed(final long address, final long fileSize, final long param) {
    script_800c66fc.setPointer(address);
    script_800c66fc_length = (int)fileSize;
  }

  @Method(0x80109170L)
  public static void FUN_80109170(final long address, final long fileSize, final long param) {
    script_800c670c.set(MEMORY.ref(4, address, ScriptFile::new));
    scriptIndex_800c674c.setu(allocateScriptState(5, 0, false, null, 0));
    loadScriptFile((int)scriptIndex_800c674c.get(), script_800c670c.deref(), "DRGN1 401", (int)fileSize);

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
    FUN_80012bb4();
  }

  @Method(0x80109250L)
  public static void FUN_80109250(final long param) {
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

    FUN_80012bb4();
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
  public static void allocateEnemyBattleObjects(final long a0) {
    final long fp = _1f8003f4.getPointer(); //TODO

    //LAB_801095a0
    for(int i = 0; i < 3; i++) {
      final long s2 = MEMORY.ref(2, fp).offset(i * 0x2L).get() & 0x1ffL;
      if(s2 == 0x1ffL) {
        break;
      }

      loadAndRunOverlay(1, getMethodAddress(SBtld.class, "FUN_80109808", long.class), addCombatant(s2, -1) * 0x10000 + s2);
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
      final int bobjIndex = allocateScriptState(0x27cL, BattleObject27c::new);
      setScriptTicker(bobjIndex, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cae50", int.class, ScriptState.classFor(BattleObject27c.class), BattleObject27c.class), TriConsumerRef::new));
      setScriptDestructor(bobjIndex, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cb058", int.class, ScriptState.classFor(BattleObject27c.class), BattleObject27c.class), TriConsumerRef::new));
      _8006e398.bobjIndices_e0c.get(_800c66d0.get()).set(bobjIndex);
      _8006e398.bobjIndices_e50.get(_800c6768.get()).set(bobjIndex);
      final ScriptState<BattleObject27c> state = scriptStatePtrArr_800bc1c0.get(bobjIndex).derefAs(ScriptState.classFor(BattleObject27c.class));
      final BattleObject27c data = state.innerStruct_00.deref();
      data.magic_00.set(BattleScriptDataBase.BOBJ);
      data.charIndex_272.set((short)charIndex);
      data._274.set((short)_800c66d0.get());
      data.charSlot_276.set((short)_800c6768.get());
      data.combatant_144.set(getCombatant(combatantIndex));
      data.combatantIndex_26c.set((short)combatantIndex);
      data._148.coord2_14.coord.transfer.setX((int)MEMORY.ref(2, s5).offset(0xaL).getSigned());
      data._148.coord2_14.coord.transfer.setY((int)MEMORY.ref(2, s5).offset(0xcL).getSigned());
      data._148.coord2_14.coord.transfer.setZ((int)MEMORY.ref(2, s5).offset(0xeL).getSigned());
      data._148.coord2Param_64.rotate.set((short)0, (short)0xc01, (short)0);
      state.ui_60.or(0x4L);
      _800c66d0.incr();
      _800c6768.incr();
    }

    //LAB_8010975c
    _8006e398.bobjIndices_e0c.get(_800c66d0.get()).set(-1);
    _8006e398.bobjIndices_e50.get(_800c6768.get()).set(-1);

    //LAB_801097ac
    for(int i = 0; i < _800c6768.get(); i++) {
      loadMonster(_8006e398.bobjIndices_e50.get(i).get());
    }

    //LAB_801097d0
    FUN_80012bb4();
  }

  @Method(0x80109808L)
  public static void FUN_80109808(final long param) {
    final int fileIndex = (int)(param & 0xffff);
    final long s0 = param >>> 16;
    final CombatantStruct1a8 v0 = getCombatant((int)s0);
    final long v1 = _80112868.offset(fileIndex * 0x8L).getAddress(); //TODO
    v0._194.set(MEMORY.ref(4, v1).offset(0x0L).get());
    v0._198.set(MEMORY.ref(4, v1).offset(0x4L).get());
    loadDrgnBinFile(1, fileIndex + 1, 0, getMethodAddress(SBtld.class, "FUN_8010989c", long.class, long.class, long.class), s0, 0x2L);
  }

  @Method(0x8010989cL)
  public static void FUN_8010989c(final long address, final long fileSize, final long index) {
    final ScriptFile script = MEMORY.ref(4, address, ScriptFile::new);

    getCombatant((int)index).filePtr_10.set(script.getAddress());
    _8005e398_SCRIPT_SIZES.remove((int)index);
    _8005e398_SCRIPT_SIZES.put((int)index, new Tuple<>("S_BTLD Script %d".formatted(index), (int)fileSize));
    _800c66d8.offset(_800c6698.get() * 0x4L).setu(script.getAddress()); //TODO
    _800c6698.addu(0x1L);
    FUN_80012bb4();
  }

  @Method(0x801098f4L)
  public static void FUN_801098f4(final long param) {
    final BattleStruct7cc struct7cc = struct7cc_800c693c.deref();
    final int stage = Math.max(0, submapStage_800bb0f4.get());

    //LAB_8010993c
    //LAB_80109954
    memcpy(struct7cc._4c.getAddress(), _801134fc.get(stage).getAddress(), 0x4c);

    FUN_800e5768(struct7cc._4c);

    //LAB_8010999c
    memcpy(struct7cc._98.getAddress(), _80114a10.getAddress(), 0x260);
    memcpy(struct7cc.svec_00.getAddress(), _8011517c.offset(submapStage_800bb0f4.get() * 0x8L).getAddress(), 0x8);

    //LAB_80109a30
    for(int i = 0; stageIndices_800fb064.offset(i).get() != 0xffL; i++) {
      struct7cc.dvecs_08.get(i).set(_8011517c.offset(stageIndices_800fb064.offset(i).get() * 0x8L).cast(DVECTOR::new));
    }

    //LAB_80109a80
    FUN_80012bb4();
  }
}
