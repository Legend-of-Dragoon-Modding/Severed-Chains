package legend.game.combat;

import legend.core.Tuple;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.TriConsumerRef;
import legend.game.combat.types.BattleStruct1a8;
import legend.game.combat.types.BtldScriptData27c;
import legend.game.types.ScriptFile;
import legend.game.types.ScriptState;

import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.Scus94491BpeSegment.FUN_80012b1c;
import static legend.game.Scus94491BpeSegment.FUN_80012bb4;
import static legend.game.Scus94491BpeSegment.FUN_800133ac;
import static legend.game.Scus94491BpeSegment._1f8003f4;
import static legend.game.Scus94491BpeSegment.allocateScriptState;
import static legend.game.Scus94491BpeSegment.decompress;
import static legend.game.Scus94491BpeSegment.loadDrgnBinFile;
import static legend.game.Scus94491BpeSegment.loadScriptFile;
import static legend.game.Scus94491BpeSegment.setCallback04;
import static legend.game.Scus94491BpeSegment.setCallback0c;
import static legend.game.Scus94491BpeSegment_8005._8005e398_SCRIPT_SIZES;
import static legend.game.Scus94491BpeSegment_8006._8006e398;
import static legend.game.Scus94491BpeSegment_800b._800bc960;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.combat.Bttl_800c.FUN_800c8f50;
import static legend.game.combat.Bttl_800c.FUN_800c9060;
import static legend.game.combat.Bttl_800c._800c6698;
import static legend.game.combat.Bttl_800c._800c66b0;
import static legend.game.combat.Bttl_800c._800c66d0;
import static legend.game.combat.Bttl_800c._800c66d8;
import static legend.game.combat.Bttl_800c._800c6718;
import static legend.game.combat.Bttl_800c._800c6748;
import static legend.game.combat.Bttl_800c._800c6768;
import static legend.game.combat.Bttl_800c._800c6780;
import static legend.game.combat.Bttl_800c.getBattleStruct1a8;
import static legend.game.combat.Bttl_800c.scriptIndex_800c674c;
import static legend.game.combat.Bttl_800c.script_800c66fc;
import static legend.game.combat.Bttl_800c.script_800c66fc_length;
import static legend.game.combat.Bttl_800c.script_800c670c;
import static legend.game.combat.Bttl_800e.FUN_800e5768;
import static legend.game.combat.Bttl_800f.FUN_800f8670;

public class SBtld {
  private static final Value bpe_800fb77c = MEMORY.ref(4, 0x800fb77cL);

  /** TODO 0x10-byte struct */
  public static final Value _80109a98 = MEMORY.ref(4, 0x80109a98L);

  /** TODO 0x8-byte struct */
  public static final Value _80112868 = MEMORY.ref(4, 0x80112868L);

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
    scriptIndex_800c674c.setu(allocateScriptState(5, 0, false, 0, 0));
    loadScriptFile(scriptIndex_800c674c.get(), script_800c670c.deref(), "DRGN1 401", (int)fileSize);

    final long v1;
    if((FUN_800133ac() & 0x8000L) == 0) {
      v1 = 0x14L;
    } else {
      v1 = 0x10L;
    }

    //LAB_801091dc
    _800c6748.setu(_800c6718.offset(v1).get() + 0x1L);
    _800c66b0.setu(FUN_800133ac() & 0x3L);
    _800c6780.setu(_800c6718.offset((_800c66b0.get() + 0x6L) * 0x4L).get());
    _800bc960.oru(0x2L);
    FUN_80012bb4();
  }

  @Method(0x80109250L)
  public static void FUN_80109250(final long param) {
    long v0;
    long v1;
    long a0;
    long a1;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    v0 = 0x1f80_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x3f4L).get();

    s1 = v0 + 0x38L;
    s2 = 0;
    v0 = 0x800c_0000L;
    s5 = v0 + -0x5438L;
    v0 = 0x8011_0000L;
    s4 = v0 + 0x34e8L;
    v0 = 0x8011_0000L;
    s3 = v0 + -0x19a8L;

    //LAB_801092a0
    do {
      v0 = s2 << 2;
      v0 = v0 + s5;
      v1 = MEMORY.ref(4, v0).offset(0x88L).get();

      if((int)v1 >= 0) {
        v0 = v1 << 1;
        v0 = v0 + v1;
        v0 = v0 << 2;
        v0 = v0 - v1;
        v0 = v0 << 2;
        v0 = v0 + s5;
        a0 = MEMORY.ref(1, v0).offset(0x345L).getSigned();
        v0 = 0x5L;
        if(v1 == v0) {
          a0 = a0 + 0x1cL;
        }

        //LAB_801092dc
        if(v1 != 0 || (gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xff) >>> 7 == 0) {
          //LAB_80109308
          v0 = v1 << 1;
          v0 = v0 + s4;
          s0 = MEMORY.ref(2, v0).offset(0x0L).getSigned();
        } else {
          s0 = MEMORY.ref(2, s4).offset(0x12L).getSigned();
        }

        //LAB_80109310
        if((int)a0 < 0) {
          MEMORY.ref(2, s1).offset(0x1eL).setu(0);
        } else {
          a0 = a0 << 7;

          //LAB_80109320
          a0 = a0 + s3;
          a1 = s1;
          FUN_80109454(a0, a1);
          a0 = s0 << 7;
          a0 = a0 + s3;
          a1 = s1 + 0x300L;
          FUN_80109454(a0, a1);
        }

        //LAB_8010933c
        s1 = s1 + 0x100L;
      }

      //LAB_80109340
      s2 = s2 + 0x1L;
    } while((int)s2 < 0x3L);

    a1 = 0x1f80_0000L;
    a0 = 0x8010_0000L;
    a0 = a0 + 0x2050L;
    v0 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v0).offset(-0x4f08L).get();

    v0 = v1 << 3;
    v0 = v0 - v1;
    v0 = v0 << 3;
    a1 = MEMORY.ref(4, a1).offset(0x3f4L).get();
    v1 = v0 + a0;
    v0 = v1 + 0x30L;
    do {
      MEMORY.ref(4, a1).offset(0x0L).setu(MEMORY.ref(4, v1).offset(0x0L).get());
      MEMORY.ref(4, a1).offset(0x4L).setu(MEMORY.ref(4, v1).offset(0x4L).get());
      MEMORY.ref(4, a1).offset(0x8L).setu(MEMORY.ref(4, v1).offset(0x8L).get());
      MEMORY.ref(4, a1).offset(0xcL).setu(MEMORY.ref(4, v1).offset(0xcL).get());
      a1 = a1 + 0x10L;
      v1 = v1 + 0x10L;
    } while(v1 != v0);

    //LAB_8010940c
    MEMORY.ref(4, a1).offset(0x0L).setu(MEMORY.ref(4, v1).offset(0x0L).get());
    MEMORY.ref(4, a1).offset(0x4L).setu(MEMORY.ref(4, v1).offset(0x4L).get());
    FUN_80012bb4();
  }

  @Method(0x80109454L)
  public static void FUN_80109454(long a0, long a1) {
    long v0;
    long v1;
    long a2;
    long a3;
    a3 = a0;
    a2 = 0;

    //LAB_80109460
    do {
      a0 = a2 << 5;
      a0 = a1 + a0;
      v1 = a2 << 4;
      v1 = a3 + v1;
      v0 = MEMORY.ref(1, v1).offset(0x0L).get();

      MEMORY.ref(2, a0).offset(0x0L).setu(v0);
      v0 = MEMORY.ref(1, v1).offset(0x1L).get();

      MEMORY.ref(2, a0).offset(0x2L).setu(v0);
      v0 = MEMORY.ref(1, v1).offset(0x2L).get();

      MEMORY.ref(2, a0).offset(0x4L).setu(v0);
      v0 = MEMORY.ref(1, v1).offset(0x3L).get();

      MEMORY.ref(2, a0).offset(0x6L).setu(v0);
      v0 = MEMORY.ref(1, v1).offset(0x4L).get();

      MEMORY.ref(2, a0).offset(0x8L).setu(v0);
      v0 = MEMORY.ref(1, v1).offset(0x5L).get();

      MEMORY.ref(2, a0).offset(0xaL).setu(v0);
      v0 = MEMORY.ref(1, v1).offset(0x6L).get();

      v0 = v0 << 24;
      v0 = (int)v0 >> 24;
      MEMORY.ref(2, a0).offset(0xcL).setu(v0);
      v0 = MEMORY.ref(1, v1).offset(0x7L).get();

      v0 = v0 << 24;
      v0 = (int)v0 >> 24;
      MEMORY.ref(2, a0).offset(0xeL).setu(v0);
      v0 = MEMORY.ref(1, v1).offset(0x8L).get();

      v0 = v0 << 24;
      v0 = (int)v0 >> 24;
      MEMORY.ref(2, a0).offset(0x10L).setu(v0);
      v0 = MEMORY.ref(1, v1).offset(0x9L).get();

      MEMORY.ref(2, a0).offset(0x12L).setu(v0);
      v0 = MEMORY.ref(1, v1).offset(0xaL).get();

      MEMORY.ref(2, a0).offset(0x14L).setu(v0);
      v0 = MEMORY.ref(1, v1).offset(0xbL).get();

      MEMORY.ref(2, a0).offset(0x16L).setu(v0);
      v0 = MEMORY.ref(1, v1).offset(0xcL).get();

      MEMORY.ref(2, a0).offset(0x18L).setu(v0);
      v0 = MEMORY.ref(1, v1).offset(0xdL).get();

      MEMORY.ref(2, a0).offset(0x1aL).setu(v0);
      v0 = MEMORY.ref(1, v1).offset(0xeL).get();

      MEMORY.ref(2, a0).offset(0x1cL).setu(v0);
      v0 = MEMORY.ref(1, v1).offset(0xfL).get();

      MEMORY.ref(2, a0).offset(0x1eL).setu(v0);
      a2 = a2 + 0x1L;
    } while((int)a2 < 0x8L);
  }

  @Method(0x8010955cL)
  public static void FUN_8010955c(final long a0) {
    final long fp = _1f8003f4.getPointer(); //TODO

    //LAB_801095a0
    for(int i = 0; i < 3; i++) {
      final long s2 = MEMORY.ref(2, fp).offset(i * 0x2L).get() & 0x1ffL;
      if(s2 == 0x1ffL) {
        break;
      }

      FUN_80012b1c(0x1L, getMethodAddress(SBtld.class, "FUN_80109808", long.class), FUN_800c8f50(s2, -0x1L) * 0x10000 + s2);
    }

    //LAB_801095ec
    //LAB_801095fc
    for(int i = 0; i < 6; i++) {
      final long s5 = fp + i * 0x8L;
      final long s2 = MEMORY.ref(2, s5).offset(0x8L).get() & 0x1ffL;
      if(s2 == 0x1ffL) {
        break;
      }

      final int s4 = FUN_800c9060(s2);
      long index = allocateScriptState(0x27cL, BtldScriptData27c::new);
      setCallback04(index, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cae50", int.class, ScriptState.classFor(BtldScriptData27c.class), BtldScriptData27c.class), TriConsumerRef::new));
      setCallback0c(index, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cb058", int.class, ScriptState.classFor(BtldScriptData27c.class), BtldScriptData27c.class), TriConsumerRef::new));
      _8006e398.offset(_800c66d0.get() * 0x4L).offset(0xe0cL).setu(index);
      _8006e398.offset(_800c6768.get() * 0x4L).offset(0xe50L).setu(index);
      final ScriptState<BtldScriptData27c> state = scriptStatePtrArr_800bc1c0.get((int)index).derefAs(ScriptState.classFor(BtldScriptData27c.class));
      final BtldScriptData27c data = state.innerStruct_00.deref();
      data.magic_00.set(0x4a42_4f42L);
      data._272.set((short)s2);
      data._274.set((short)_800c66d0.get());
      data._276.set((short)_800c6768.get());
      data._144.set(getBattleStruct1a8(s4));
      data._26c.set((short)s4);
      data._148.coord2_14.coord.transfer.setX((int)MEMORY.ref(2, s5).offset(0xaL).getSigned());
      data._148.coord2_14.coord.transfer.setY((int)MEMORY.ref(2, s5).offset(0xcL).getSigned());
      data._148.coord2_14.coord.transfer.setZ((int)MEMORY.ref(2, s5).offset(0xeL).getSigned());
      data._148.coord2Param_64.rotate.set((short)0, (short)0xc01, (short)0);
      state.ui_60.or(0x4L);
      _800c66d0.addu(0x1L);
      _800c6768.addu(0x1L);
    }

    //LAB_8010975c
    _8006e398.offset(0xe0cL).offset(_800c66d0.get() * 0x4L).setu(-0x1L);
    _8006e398.offset(0xe50L).offset(_800c6768.get() * 0x4L).setu(-0x1L);

    //LAB_801097ac
    for(int i = 0; i < _800c6768.get(); i++) {
      FUN_800f8670(_8006e398.offset(0xe50L).offset(i * 0x4L).get());
    }

    //LAB_801097d0
    FUN_80012bb4();
  }

  @Method(0x80109808L)
  public static void FUN_80109808(final long param) {
    final long fileIndex = param & 0xffffL;
    final long s0 = param >>> 16;
    final BattleStruct1a8 v0 = getBattleStruct1a8((int)s0);
    final long v1 = _80112868.offset(fileIndex * 0x8L).getAddress(); //TODO
    v0._194.set(MEMORY.ref(4, v1).offset(0x0L).get());
    v0._198.set(MEMORY.ref(4, v1).offset(0x4L).get());
    loadDrgnBinFile(1, fileIndex + 1, 0, getMethodAddress(SBtld.class, "FUN_8010989c", long.class, long.class, long.class), s0, 0x2L);
  }

  @Method(0x8010989cL)
  public static void FUN_8010989c(final long address, final long fileSize, final long index) {
    final ScriptFile script = MEMORY.ref(4, address, ScriptFile::new);

    getBattleStruct1a8((int)index).script_10.set(script);
    _8005e398_SCRIPT_SIZES.remove((int)index);
    _8005e398_SCRIPT_SIZES.put((int)index, new Tuple<>("S_BTLD Script %d".formatted(index), (int)fileSize));
    _800c66d8.offset(_800c6698.get() * 0x4L).setu(script.getAddress()); //TODO
    _800c6698.addu(0x1L);
    FUN_80012bb4();
  }

  @Method(0x801098f4L)
  public static void FUN_801098f4(final long param) {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long t0;
    long t1;
    long s0;
    v0 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v0).offset(-0x4f0cL).get();

    if((int)v1 > 0) {
      v0 = v1 << 2;
      v0 = v0 + v1;
      v0 = v0 << 2;
      v0 = v0 - v1;
      v0 = v0 << 2;
      v1 = 0x8011_0000L;
      v1 = v1 + 0x34fcL;
      v1 = v0 + v1;
    } else {
      //LAB_80109934
      v0 = 0x8011_0000L;
      v1 = v0 + 0x34fcL;
    }

    //LAB_8010993c
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x693cL).get();

    a1 = v0 + 0x4cL;
    a0 = v1;
    v1 = 0x12L;

    //LAB_80109954
    do {
      v0 = MEMORY.ref(4, a0).offset(0x0L).get();
      MEMORY.ref(4, a1).offset(0x0L).setu(v0);
      a0 = a0 + 0x4L;
      a1 = a1 + 0x4L;
      v0 = v1;
      v1 = v1 + -0x1L;
    } while((int)v0 > 0);

    s0 = 0x800c_0000L;
    a0 = MEMORY.ref(4, s0).offset(0x693cL).get();
    a0 = a0 + 0x4cL;
    FUN_800e5768(a0);
    v0 = MEMORY.ref(4, s0).offset(0x693cL).get();

    a1 = v0 + 0x98L;
    v0 = 0x8011_0000L;
    a0 = v0 + 0x4a10L;
    v1 = 0x97L;

    //LAB_8010999c
    do {
      v0 = MEMORY.ref(4, a0).offset(0x0L).get();
      MEMORY.ref(4, a1).offset(0x0L).setu(v0);
      a0 = a0 + 0x4L;
      a1 = a1 + 0x4L;
      v0 = v1;
      v1 = v1 + -0x1L;
    } while((int)v0 > 0);

    v0 = 0x800c_0000L;
    a0 = MEMORY.ref(4, v0).offset(0x693cL).get();
    v1 = 0x8011_0000L;
    v1 = v1 + 0x517cL;
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(-0x4f0cL).get();

    v0 = v0 << 3;
    v0 = v0 + v1;
    MEMORY.ref(2, a0).offset(0x0L).setu(MEMORY.ref(2, v0).offset(0x0L).get());
    MEMORY.ref(2, a0).offset(0x2L).setu(MEMORY.ref(2, v0).offset(0x2L).get());
    MEMORY.ref(2, a0).offset(0x4L).setu(MEMORY.ref(2, v0).offset(0x4L).get());
    MEMORY.ref(2, a0).offset(0x6L).setu(MEMORY.ref(2, v0).offset(0x6L).get());
    v0 = 0x8010_0000L;
    t1 = v0 + -0x4f9cL;
    t0 = 0xffL;
    v0 = 0x800c_0000L;
    a3 = MEMORY.ref(4, v0).offset(0x693cL).get();
    v0 = 0x8011_0000L;
    a2 = v0 + 0x517cL;

    //LAB_80109a30
    for(a1 = 0; MEMORY.ref(1, t1).offset(a1).get() != t0; a1++) {
      MEMORY.ref(2, a3).offset(0x8L).offset(a1 * 0x4L).setu(MEMORY.ref(2, a2).offset(MEMORY.ref(1, t1).offset(a1).get() * 0x8L).offset(0x0L).get());
      MEMORY.ref(2, a3).offset(0xaL).offset(a1 * 0x4L).setu(MEMORY.ref(2, a2).offset(MEMORY.ref(1, t1).offset(a1).get() * 0x8L).offset(0x2L).get());
    }

    //LAB_80109a80
    FUN_80012bb4();
  }
}
