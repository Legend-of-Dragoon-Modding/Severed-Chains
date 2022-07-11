package legend.game.combat;

import legend.core.MathHelper;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.TmdObjTable;
import legend.core.gte.VECTOR;
import legend.core.memory.Memory;
import legend.core.memory.Method;
import legend.core.memory.Ref;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.QuadConsumerRef;
import legend.core.memory.types.TriConsumerRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.Scus94491BpeSegment_800c;
import legend.game.combat.types.BattleCamera;
import legend.game.combat.types.BattleObject27c;
import legend.game.combat.types.BattleScriptDataBase;
import legend.game.combat.types.BttlScriptData1c;
import legend.game.combat.types.BttlScriptData40;
import legend.game.combat.types.BttlScriptData6c;
import legend.game.combat.types.BttlScriptData6cInner;
import legend.game.combat.types.BttlScriptData6cSub06;
import legend.game.combat.types.BttlScriptData6cSub08;
import legend.game.combat.types.BttlScriptData6cSub10;
import legend.game.combat.types.BttlScriptData6cSub14;
import legend.game.combat.types.BttlScriptData6cSub14_3;
import legend.game.combat.types.BttlScriptData6cSub34_2;
import legend.game.combat.types.BttlStruct50;
import legend.game.types.DR_MODE;
import legend.game.types.RunningScript;
import legend.game.types.ScriptState;

import java.util.Arrays;

import static legend.core.Hardware.CPU;
import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.Scus94491BpeSegment.FUN_80015d38;
import static legend.game.Scus94491BpeSegment.FUN_80018a5c;
import static legend.game.Scus94491BpeSegment.FUN_80018d60;
import static legend.game.Scus94491BpeSegment.addToLinkedListTail;
import static legend.game.Scus94491BpeSegment.allocateScriptState;
import static legend.game.Scus94491BpeSegment.insertElementIntoLinkedList;
import static legend.game.Scus94491BpeSegment.linkedListAddress_1f8003d8;
import static legend.game.Scus94491BpeSegment.loadScriptFile;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.removeFromLinkedList;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment.setCallback04;
import static legend.game.Scus94491BpeSegment.setCallback0c;
import static legend.game.Scus94491BpeSegment.tags_1f8003d0;
import static legend.game.Scus94491BpeSegment_8002.SetGeomOffset;
import static legend.game.Scus94491BpeSegment_8002.SetRotMatrix;
import static legend.game.Scus94491BpeSegment_8002.SetTransMatrix;
import static legend.game.Scus94491BpeSegment_8002.SquareRoot0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003eba0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003f210;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003f990;
import static legend.game.Scus94491BpeSegment_8003.GetTPage;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_8003.GsSetRefView2;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_8003faf0;
import static legend.game.Scus94491BpeSegment_8003.ScaleMatrixL;
import static legend.game.Scus94491BpeSegment_8003.SetDrawMode;
import static legend.game.Scus94491BpeSegment_8003.getProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.setRotTransMatrix;
import static legend.game.Scus94491BpeSegment_8003.updateTmdPacketIlen;
import static legend.game.Scus94491BpeSegment_8004.RotMatrixX;
import static legend.game.Scus94491BpeSegment_8004.RotMatrixY;
import static legend.game.Scus94491BpeSegment_8004._8004f650;
import static legend.game.Scus94491BpeSegment_8004.ratan2;
import static legend.game.Scus94491BpeSegment_800b._800bb0fc;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.combat.Bttl_800c.FUN_800cea1c;
import static legend.game.combat.Bttl_800c.FUN_800cf244;
import static legend.game.combat.Bttl_800c.FUN_800cf37c;
import static legend.game.combat.Bttl_800c.FUN_800cf4f4;
import static legend.game.combat.Bttl_800c.FUN_800cfb14;
import static legend.game.combat.Bttl_800c.FUN_800cffd8;
import static legend.game.combat.Bttl_800c._800c6790;
import static legend.game.combat.Bttl_800c._800c6798;
import static legend.game.combat.Bttl_800c._800c67b8;
import static legend.game.combat.Bttl_800c._800c67c4;
import static legend.game.combat.Bttl_800c._800c67d4;
import static legend.game.combat.Bttl_800c._800c67d8;
import static legend.game.combat.Bttl_800c._800c67e4;
import static legend.game.combat.Bttl_800c._800c67e8;
import static legend.game.combat.Bttl_800c._800c6912;
import static legend.game.combat.Bttl_800c._800c6913;
import static legend.game.combat.Bttl_800c._800c6920;
import static legend.game.combat.Bttl_800c._800c693c;
import static legend.game.combat.Bttl_800c._800c6948;
import static legend.game.combat.Bttl_800c._800c6d94;
import static legend.game.combat.Bttl_800c._800c6dac;
import static legend.game.combat.Bttl_800c._800c6dc4;
import static legend.game.combat.Bttl_800c._800fa76c;
import static legend.game.combat.Bttl_800c._800fa788;
import static legend.game.combat.Bttl_800c._800fa7cc;
import static legend.game.combat.Bttl_800c._800faa90;
import static legend.game.combat.Bttl_800c._800faa92;
import static legend.game.combat.Bttl_800c._800faa94;
import static legend.game.combat.Bttl_800c._800faa98;
import static legend.game.combat.Bttl_800c._800faa9c;
import static legend.game.combat.Bttl_800c._800faa9d;
import static legend.game.combat.Bttl_800c._800faaa0;
import static legend.game.combat.Bttl_800c._800fab98;
import static legend.game.combat.Bttl_800c._800faba0;
import static legend.game.combat.Bttl_800c._800faba8;
import static legend.game.combat.Bttl_800c._800fabb8;
import static legend.game.combat.Bttl_800c._800fabbc;
import static legend.game.combat.Bttl_800c._800fabdc;
import static legend.game.combat.Bttl_800c._800fabfc;
import static legend.game.combat.Bttl_800c._800fac3c;
import static legend.game.combat.Bttl_800c._800fac5c;
import static legend.game.combat.Bttl_800c._800fac9c;
import static legend.game.combat.Bttl_800c._800facbc;
import static legend.game.combat.Bttl_800c._800fad1c;
import static legend.game.combat.Bttl_800c._800fad7c;
import static legend.game.combat.Bttl_800c._800fad9c;
import static legend.game.combat.Bttl_800c.additionNames_800fa8d4;
import static legend.game.combat.Bttl_800c.callbackIndex_800c6878;
import static legend.game.combat.Bttl_800c.callbackIndex_800c68ec;
import static legend.game.combat.Bttl_800c.camera_800c67f0;
import static legend.game.combat.Bttl_800c.effectRenderers_800fa758;
import static legend.game.combat.Bttl_800c.seed_800fa754;
import static legend.game.combat.Bttl_800c.x_800c67bc;
import static legend.game.combat.Bttl_800c.y_800c67c0;
import static legend.game.combat.Bttl_800e.FUN_800e3e6c;
import static legend.game.combat.Bttl_800e.FUN_800e4d74;
import static legend.game.combat.Bttl_800e.FUN_800e7ea4;
import static legend.game.combat.Bttl_800e.FUN_800e80c4;

public final class Bttl_800d {
  private Bttl_800d() { }

  @Method(0x800d0094L)
  public static void FUN_800d0094(final int scriptIndex, final int animIndex, final long a2) {
    final BattleObject27c v1 = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(BattleObject27c.class);

    final UnsignedIntRef a0;
    final long a3;
    if(animIndex < 32) {
      //LAB_800d00d0
      a0 = v1._148.ui_f4;
      a3 = animIndex;
    } else {
      a0 = v1._148.ui_f8;
      a3 = animIndex - 32;
    }

    //LAB_800d00d4
    if((a2 & 0xffL) != 0) {
      a0.set(~(1 << a3) & a0.get());
      return;
    }

    //LAB_800d0104
    a0.or(1 << a3);
  }

  @Method(0x800d0124L)
  public static long FUN_800d0124(final RunningScript a0) {
    final ScriptState<?> a2 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref();
    final BattleScriptDataBase a1 = a2.innerStruct_00.derefAs(BattleScriptDataBase.class);

    if(a1.magic_00.get() == BattleScriptDataBase.EM__) {
      final long v1 = ((BttlScriptData6c)a1)._44.getPointer(); //TODO
      a0.params_20.get(1).deref().set((int)MEMORY.ref(2, v1).offset(0xa8L).getSigned());
    } else {
      //LAB_800d017c
      a0.params_20.get(1).deref().set(((BattleObject27c)a1)._148.animCount_98.get());
    }

    //LAB_800d0194
    return 0;
  }

  @Method(0x800d019cL)
  public static void FUN_800d019c(final int scriptIndex, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c data) {
    long a0 = 0;
    final BttlScriptData6cSub14_3 fp = data._44.derefAs(BttlScriptData6cSub14_3.class);
    long s4 = fp._08.get();

    //LAB_800d01ec
    for(int s7 = 0; s7 < fp.count_00.get(); s7++) {
      if(MEMORY.ref(1, s4).offset(0x0L).get() != 0) {
        MEMORY.ref(4, s4).offset(0x40L).addu(0x1L);
        MEMORY.ref(2, s4).offset(0x44L).subu(0x1L);

        if(MEMORY.ref(2, s4).offset(0x44L).getSigned() == 0) {
          MEMORY.ref(1, s4).offset(0x0L).setu(0);
        }

        //LAB_800d0220
        MEMORY.ref(2, s4).offset(0x34L).subu(MEMORY.ref(2, s4).offset(0x3aL).get());
        MEMORY.ref(2, s4).offset(0x36L).subu(MEMORY.ref(2, s4).offset(0x3cL).get());
        MEMORY.ref(2, s4).offset(0x38L).subu(MEMORY.ref(2, s4).offset(0x3eL).get());

        //LAB_800d0254
        final Ref<Long>[] sp0x18 = new Ref[] {new Ref<>(), new Ref<>()};
        final Ref<Long>[] sp0x20 = new Ref[] {new Ref<>(), new Ref<>()};
        long s1;
        long a1;
        for(int s3 = 0; s3 < 2; s3++) {
          s1 = s4 + s3 * 0x10L;
          final VECTOR sp0x58 = new VECTOR().set(
            (int)MEMORY.ref(4, s1).offset(0x4L).get(),
            (int)MEMORY.ref(4, s1).offset(0x8L).get(),
            (int)MEMORY.ref(4, s1).offset(0xcL).get()
          );
          a0 = FUN_800cfb14(data, sp0x58, sp0x18[s3], sp0x20[s3]);
          a1 = s4 + s3 * 0x8L;
          MEMORY.ref(4, s1).offset(0x4L).addu(MEMORY.ref(2, a1).offset(0x24L).getSigned());
          MEMORY.ref(4, s1).offset(0x8L).addu(MEMORY.ref(2, a1).offset(0x26L).getSigned());
          MEMORY.ref(4, s1).offset(0xcL).addu(MEMORY.ref(2, a1).offset(0x28L).getSigned());
          MEMORY.ref(2, a1).offset(0x26L).addu(0x19L);

          if(MEMORY.ref(2, a1).offset(0x24L).getSigned() > 0xaL) {
            MEMORY.ref(2, a1).offset(0x24L).subu(0xaL);
          }

          //LAB_800d0308
          if((int)(MEMORY.ref(4, s1).offset(0x8L).get() + data._10.vec_04.getY()) >= 0) {
            MEMORY.ref(4, s1).offset(0x8L).setu(-data._10.vec_04.getY());
            MEMORY.ref(2, a1).offset(0x26L).neg();
          }

          //LAB_800d033c
        }

        s1 = a0 >> 2;
        long v1 = s1;
        if(v1 >= 0x140L) {
          if(v1 >= 0xffeL) {
            s1 = 0xffeL;
          }

          //LAB_800d037c
          a1 = linkedListAddress_1f8003d8.get();
          MEMORY.ref(1, a1).offset(0x3L).setu(0x4L);
          MEMORY.ref(4, a1).offset(0x4L).setu(0x5200_0000L);
          MEMORY.ref(2, a1).offset(0x8L).setu(sp0x18[0].get());
          MEMORY.ref(2, a1).offset(0xaL).setu(sp0x20[0].get());
          MEMORY.ref(1, a1).offset(0xcL).setu(MEMORY.ref(2, s4).offset(0x34L).get() >>> 8);
          MEMORY.ref(1, a1).offset(0xdL).setu(MEMORY.ref(2, s4).offset(0x36L).get() >>> 8);
          MEMORY.ref(1, a1).offset(0xeL).setu(MEMORY.ref(2, s4).offset(0x38L).get() >>> 8);
          MEMORY.ref(2, a1).offset(0x10L).setu(sp0x18[1].get());
          MEMORY.ref(2, a1).offset(0x12L).setu(sp0x20[1].get());

          long a2_0 = data._10._22.get();
          v1 = s1 + a2_0;
          if(v1 >= 0xa0L) {
            if(v1 >= 0xffeL) {
              a2_0 = 0xffeL - s1;
            }

            //LAB_800d0444
            insertElementIntoLinkedList(tags_1f8003d0.getPointer() + (s1 + a2_0) / 4 * 4, a1);
          }
          linkedListAddress_1f8003d8.addu(0x14L);

          //LAB_800d0460
          SetDrawMode(linkedListAddress_1f8003d8.deref(4).cast(DR_MODE::new), false, true, GetTPage(0x1L, 0x1L, 0, 0), null);

          a2_0 = data._10._22.get();
          v1 = s1 + a2_0;
          if(v1 >= 0xa0L) {
            if(v1 < 0xffeL) {
              a2_0 = 0xffeL - s1;
            }

            //LAB_800d04c8
            insertElementIntoLinkedList(tags_1f8003d0.getPointer() + (s1 + a2_0) / 4 * 4, linkedListAddress_1f8003d8.get());
          }

          linkedListAddress_1f8003d8.addu(0xcL);
        }
      }

      //LAB_800d04e8
      s4 = s4 + 0x48L;
    }

    //LAB_800d0508
  }

  @Method(0x800d0538L)
  public static void FUN_800d0538(final int scriptIndex, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c data) {
    removeFromLinkedList(data._44.derefAs(BttlScriptData6cSub14_3.class)._08.get());
  }

  @Method(0x800d0564L)
  public static long FUN_800d0564(final RunningScript s2) {
    final int scriptIndex = FUN_800e80c4(
      s2.scriptStateIndex_00.get(),
      0x14L,
      null,
      MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "FUN_800d019c", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "FUN_800d0538", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new),
      BttlScriptData6cSub14_3::new
    );

    final BttlScriptData6cSub14_3 struct = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(BttlScriptData6c.class)._44.derefAs(BttlScriptData6cSub14_3.class);

    final int count = s2.params_20.get(1).deref().get();
    long t6 = addToLinkedListTail(count * 0x48L);
    struct.count_00.set(count);
    struct._04.set(0);
    struct._08.set(t6);

    //LAB_800d0634
    for(int i = 0; i < count; i++) {
      MEMORY.ref(1, t6).offset(0x00L).setu(0x1L);
      MEMORY.ref(2, t6).offset(0x34L).setu(s2.params_20.get(2).deref().get() << 8);
      MEMORY.ref(2, t6).offset(0x36L).setu(s2.params_20.get(3).deref().get() << 8);
      MEMORY.ref(2, t6).offset(0x38L).setu(s2.params_20.get(4).deref().get() << 8);

      final long a3 = seed_800fa754.advance().get() % 301 + 200;
      MEMORY.ref(2, t6).offset(0x2cL).setu(a3);
      MEMORY.ref(2, t6).offset(0x24L).setu(a3);

      final long t0 = seed_800fa754.advance().get() % 401 - 300;
      MEMORY.ref(2, t6).offset(0x2eL).setu(t0);
      MEMORY.ref(2, t6).offset(0x26L).setu(t0);

      final long a2 = seed_800fa754.advance().get() % 601 - 300;
      MEMORY.ref(2, t6).offset(0x30L).setu(a2);
      MEMORY.ref(2, t6).offset(0x28L).setu(a2);

      MEMORY.ref(4, t6).offset(0x08L).setu(seed_800fa754.advance().get() % 101 - 50);
      MEMORY.ref(4, t6).offset(0x0cL).setu(seed_800fa754.advance().get() % 101 - 50);
      MEMORY.ref(2, t6).offset(0x44L).setu(seed_800fa754.advance().get() % 9 + 7);

      MEMORY.ref(4, t6).offset(0x40L).setu(0);
      MEMORY.ref(4, t6).offset(0x04L).setu(0);
      MEMORY.ref(2, t6).offset(0x2eL).addu(0x19L);
      MEMORY.ref(4, t6).offset(0x14L).setu(MEMORY.ref(4, t6).offset(0x04L).get() + MEMORY.ref(2, t6).offset(0x24L).getSigned());
      MEMORY.ref(4, t6).offset(0x1cL).setu(MEMORY.ref(4, t6).offset(0x0cL).get() + MEMORY.ref(2, t6).offset(0x28L).getSigned());
      MEMORY.ref(4, t6).offset(0x18L).setu(MEMORY.ref(4, t6).offset(0x08L).get() + MEMORY.ref(2, t6).offset(0x26L).getSigned());
      MEMORY.ref(2, t6).offset(0x3aL).setu(MEMORY.ref(2, t6).offset(0x34L).get() / MEMORY.ref(2, t6).offset(0x44L).getSigned());
      MEMORY.ref(2, t6).offset(0x3cL).setu(MEMORY.ref(2, t6).offset(0x36L).get() / MEMORY.ref(2, t6).offset(0x44L).getSigned());
      MEMORY.ref(2, t6).offset(0x3eL).setu(MEMORY.ref(2, t6).offset(0x38L).get() / MEMORY.ref(2, t6).offset(0x44L).getSigned());
      t6 = t6 + 0x48L;
    }

    //LAB_800d0980
    s2.params_20.get(0).deref().set(scriptIndex);
    return 0;
  }

  @Method(0x800d09c0L)
  public static void FUN_800d09c0(final BttlScriptData6c a0, final long a1) {
    FUN_800cf4f4(a0, null, MEMORY.ref(4, a1 + 0x08L, VECTOR::new), MEMORY.ref(4, a1 + 0x08L, VECTOR::new));
    FUN_800cf37c(a0, null, MEMORY.ref(4, a1 + 0x28L, VECTOR::new), MEMORY.ref(4, a1 + 0x28L, VECTOR::new));
    MEMORY.ref(4, a1).offset(0x18L).setu(MEMORY.ref(4, a1).offset(0x08L).get());
    MEMORY.ref(4, a1).offset(0x1cL).setu(MEMORY.ref(4, a1).offset(0x0cL).get());
    MEMORY.ref(4, a1).offset(0x20L).setu(MEMORY.ref(4, a1).offset(0x10L).get());
  }

  @Method(0x800d0a30L)
  public static void FUN_800d0a30(final int scriptIndex, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c data) {
    long v1;
    long a2;
    long a3;
    long s1;
    long s2;
    long s3;
    long s5;
    long s7 = 0;
    s3 = 0;
    final BttlScriptData6cSub08 s6 = data._44.derefAs(BttlScriptData6cSub08.class);
    s2 = s6._04.get(); //TODO

    //LAB_800d0a7c
    for(s5 = 0; s5 < s6.count_00.get(); s5++) {
      if(MEMORY.ref(1, s2).offset(0x4L).get() != 0) {
        MEMORY.ref(1, s2).offset(0x4L).subu(0x1L);
        //LAB_800d0a94
      } else if(MEMORY.ref(1, s2).offset(0x5L).get() != 0) {
        if(MEMORY.ref(4, s2).offset(0x0L).get() == 0) {
          FUN_800d09c0(data, s2);
        }

        //LAB_800d0ac8
        MEMORY.ref(4, s2).offset(0x00L).addu(0x1L);
        MEMORY.ref(1, s2).offset(0x05L).subu(0x1L);
        MEMORY.ref(4, s2).offset(0x08L).addu(MEMORY.ref(4, s2).offset(0x28L).get());
        MEMORY.ref(4, s2).offset(0x0cL).addu(MEMORY.ref(4, s2).offset(0x2cL).get());
        MEMORY.ref(4, s2).offset(0x10L).addu(MEMORY.ref(4, s2).offset(0x30L).get());
        final IntRef sp0x18 = new IntRef();
        final IntRef sp0x1c = new IntRef();
        final IntRef sp0x20 = new IntRef();
        final IntRef sp0x24 = new IntRef();
        s1 = FUN_800cf244(MEMORY.ref(4, s2 + 0x8L, VECTOR::new), sp0x18, sp0x1c);
        FUN_800cf244(MEMORY.ref(4, s2 + 0x18L, VECTOR::new), sp0x20, sp0x24);

        if(s3 == 0) {
          s7 = (short)s1 / 4;
        }

        //LAB_800d0b3c
        MEMORY.ref(4, s2).offset(0x2cL).addu(MEMORY.ref(2, s2).offset(0x3aL).getSigned());
        MEMORY.ref(4, s2).offset(0x28L).addu(MEMORY.ref(2, s2).offset(0x38L).getSigned());
        MEMORY.ref(4, s2).offset(0x30L).addu(MEMORY.ref(2, s2).offset(0x3cL).getSigned());
        if((int)MEMORY.ref(4, s2).offset(0x0cL).get() > 0) {
          MEMORY.ref(4, s2).offset(0x2cL).setu(-MEMORY.ref(4, s2).offset(0x2cL).get() >> 1);
        }

        //LAB_800d0b88
        a2 = linkedListAddress_1f8003d8.get();
        linkedListAddress_1f8003d8.addu(0x14L);
        MEMORY.ref(1, a2).offset(0x3L).setu(0x4L);
        MEMORY.ref(1, a2).offset(0x7L).setu(0x52L);
        MEMORY.ref(1, a2).offset(0x4L).setu(MEMORY.ref(2, s2).offset(0x40L).get() >>> 8);
        MEMORY.ref(1, a2).offset(0x5L).setu(MEMORY.ref(2, s2).offset(0x42L).get() >>> 8);
        MEMORY.ref(1, a2).offset(0x6L).setu(MEMORY.ref(2, s2).offset(0x44L).get() >>> 8);
        MEMORY.ref(1, a2).offset(0xcL).setu(MEMORY.ref(2, s2).offset(0x40L).get() >>> 9);
        MEMORY.ref(1, a2).offset(0xdL).setu(MEMORY.ref(2, s2).offset(0x42L).get() >>> 9);
        MEMORY.ref(1, a2).offset(0xeL).setu(MEMORY.ref(2, s2).offset(0x44L).get() >>> 9);
        MEMORY.ref(2, a2).offset(0x8L).setu(sp0x18.get());
        MEMORY.ref(2, a2).offset(0xaL).setu(sp0x1c.get());
        MEMORY.ref(2, a2).offset(0x10L).setu(sp0x20.get());
        MEMORY.ref(2, a2).offset(0x12L).setu(sp0x24.get());
        a3 = data._10._22.get();
        v1 = s7 + a3;
        if((int)v1 >= 0xa0L) {
          if((int)v1 >= 0xffeL) {
            a3 = 0xffeL - s7;
          }

          //LAB_800d0c84
          insertElementIntoLinkedList(tags_1f8003d0.getPointer() + (s7 + a3) / 4 * 4, a2);
        }

        //LAB_800d0ca0
        MEMORY.ref(2, s2).offset(0x40L).subu(MEMORY.ref(2, s2).offset(0x46L).get());
        MEMORY.ref(2, s2).offset(0x42L).subu(MEMORY.ref(2, s2).offset(0x48L).get());
        MEMORY.ref(4, s2).offset(0x20L).setu(MEMORY.ref(4, s2).offset(0x10L).get());
        MEMORY.ref(2, s2).offset(0x44L).subu(MEMORY.ref(2, s2).offset(0x4aL).get());
        MEMORY.ref(4, s2).offset(0x18L).setu(MEMORY.ref(4, s2).offset(0x08L).get());
        MEMORY.ref(4, s2).offset(0x1cL).setu(MEMORY.ref(4, s2).offset(0x0cL).get());
        s3 = s3 + 0x1L;
      }

      //LAB_800d0cec
      //LAB_800d0cf0
      s2 = s2 + 0x4cL;
    }

    //LAB_800d0d10
    SetDrawMode(linkedListAddress_1f8003d8.deref(4).cast(DR_MODE::new), false, true, GetTPage(0x1L, 0x1L, 0, 0), null);

    long a2_0 = data._10._22.get();
    v1 = s7 + a2_0;
    if((int)v1 >= 0xa0L) {
      if((int)v1 >= 0xffeL) {
        a2_0 = 0xffeL - s7;
      }

      //LAB_800d0d78
      insertElementIntoLinkedList(tags_1f8003d0.getPointer() + (s7 + a2_0) / 4 * 4, linkedListAddress_1f8003d8.get());
    }

    linkedListAddress_1f8003d8.addu(0xcL);

    //LAB_800d0d94
  }

  @Method(0x800d0dc0L)
  public static void FUN_800d0dc0(final int scriptIndex, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c data) {
    removeFromLinkedList(data._44.derefAs(BttlScriptData6cSub08.class)._04.get());
  }

  @Method(0x800d0decL)
  public static long FUN_800d0dec(final RunningScript s3) {
    final int count = s3.params_20.get(1).deref().get();
    final int s4 = s3.params_20.get(6).deref().get();

    final int scriptIndex = FUN_800e80c4(
      s3.scriptStateIndex_00.get(),
      0x8L,
      null,
      MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "FUN_800d0a30", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "FUN_800d0dc0", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new),
      BttlScriptData6cSub08::new
    );

    final BttlScriptData6cSub08 s0 = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(BttlScriptData6c.class)._44.derefAs(BttlScriptData6cSub08.class);

    long t6 = addToLinkedListTail(count * 0x4cL);
    s0._04.set(t6);
    s0.count_00.set(count);

    final long s1 = s3.params_20.get(5).deref().get() / s4;

    //LAB_800d0ee0
    for(int i = 0; i < count; i++) {
      MEMORY.ref(4, t6).offset(0x0L).setu(0);

      seed_800fa754.advance();
      MEMORY.ref(1, t6).offset(0x4L).setu(seed_800fa754.get() % (s4 + 0x1L));
      seed_800fa754.advance();
      MEMORY.ref(1, t6).offset(0x5L).setu(seed_800fa754.get() % 9 + 7);

      MEMORY.ref(2, t6).offset(0x40L).setu(s3.params_20.get(2).deref().get() << 8);
      MEMORY.ref(2, t6).offset(0x46L).setu(MEMORY.ref(2, t6).offset(0x40L).get() / MEMORY.ref(1, t6).offset(0x5L).get());
      MEMORY.ref(2, t6).offset(0x42L).setu(s3.params_20.get(3).deref().get() << 8);
      MEMORY.ref(2, t6).offset(0x48L).setu(MEMORY.ref(2, t6).offset(0x44L).get() / MEMORY.ref(1, t6).offset(0x5L).get());
      MEMORY.ref(2, t6).offset(0x44L).setu(s3.params_20.get(4).deref().get() << 8);
      MEMORY.ref(2, t6).offset(0x4aL).setu(MEMORY.ref(2, t6).offset(0x48L).get() / MEMORY.ref(1, t6).offset(0x5L).get());
      MEMORY.ref(4, t6).offset(0x8L).setu(MEMORY.ref(1, t6).offset(0x4L).get() * s1);
      MEMORY.ref(4, t6).offset(0x1cL).setu(0);
      MEMORY.ref(4, t6).offset(0x18L).setu(0);
      MEMORY.ref(4, t6).offset(0x10L).setu(0);
      MEMORY.ref(4, t6).offset(0xcL).setu(0);
      MEMORY.ref(2, t6).offset(0x38L).setu(0);
      seed_800fa754.advance();
      MEMORY.ref(4, t6).offset(0x28L).setu(seed_800fa754.get() % 201);
      seed_800fa754.advance();
      MEMORY.ref(4, t6).offset(0x2cL).setu(seed_800fa754.get() % 201 - 100);
      seed_800fa754.advance();
      MEMORY.ref(4, t6).offset(0x30L).setu(seed_800fa754.get() % 201 - 100);
      MEMORY.ref(2, t6).offset(0x3aL).setu(0xfL);
      MEMORY.ref(2, t6).offset(0x3cL).setu(0);
      t6 = t6 + 0x4cL;
    }

    //LAB_800d1154
    s3.params_20.get(0).deref().set(scriptIndex);
    return 0;
  }

  @Method(0x800d1194L)
  public static void FUN_800d1194(final BttlScriptData6c a0, final BttlScriptData6cSub10 a1, final IntRef[] a2) {
    if(a1._00.get() == -1) {
      a2[0].set(0);
      a2[1].set(0);
    } else {
      //LAB_800d11c4
      final VECTOR sp0x10 = new VECTOR();
      FUN_800cea1c(a1._00.get(), sp0x10);
      sp0x10.add(a0._10.vec_04);
      FUN_800cf244(sp0x10, a2[0], a2[1]);
    }

    //LAB_800d120c
  }

  @Method(0x800d1220L)
  public static void FUN_800d1220(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c data) {
    final int[] sp0x48 = {-16, 16};
    final BttlScriptData6cSub10 s7 = data._44.derefAs(BttlScriptData6cSub10.class);
    long s6 = s7._0c.get();

    //LAB_800d128c
    for(int fp = 0; fp < s7.count_04.get(); fp++) {
      if(MEMORY.ref(1, s6).offset(0x0L).get() != 0) {
        //LAB_800d12a4
        for(int s5 = 0; s5 < 2; s5++) {
          long s1 = sp0x48[s5] + MEMORY.ref(2, s6).offset(0xaL).get();
          long s0 = 30 + MEMORY.ref(2, s6).offset(0x6L).get();
          long sp18 = (int)(rcos(MEMORY.ref(2, s6).offset(0x2L).getSigned() + s1) * s0) >> 12;
          long sp28 = (int)(rsin(MEMORY.ref(2, s6).offset(0x2L).getSigned() + s1) * s0) >> 12;
          long sp20 = (int)(rcos(MEMORY.ref(2, s6).offset(0x2L).getSigned()) * s0) >> 12;
          long sp30 = (int)(rsin(MEMORY.ref(2, s6).offset(0x2L).getSigned()) * s0) >> 12;
          s1 = sp0x48[s5] + MEMORY.ref(2, s6).offset(0xaL).get();
          s0 = 210 + MEMORY.ref(2, s6).offset(0x6L).get();
          long sp1c = (int)(rcos(MEMORY.ref(2, s6).offset(0x2L).getSigned() + s1) * s0) >> 12;
          long sp2c = (int)(rsin(MEMORY.ref(2, s6).offset(0x2L).getSigned() + s1) * s0) >> 12;
          long sp24 = (int)(rcos(MEMORY.ref(2, s6).offset(0x2L).getSigned()) * s0) >> 12;
          long sp34 = (int)(rsin(MEMORY.ref(2, s6).offset(0x2L).getSigned()) * s0) >> 12;
          final IntRef[] sp0x50 = {new IntRef(), new IntRef()};
          FUN_800d1194(data, s7, sp0x50);
          sp18 = sp18 + sp0x50[0].get();
          sp28 = sp28 + sp0x50[1].get();
          sp20 = sp20 + sp0x50[0].get();
          sp30 = sp30 + sp0x50[1].get();
          long a1 = linkedListAddress_1f8003d8.get();
          MEMORY.ref(1, a1).offset(0x03L).setu(0x8L);
          MEMORY.ref(1, a1).offset(0x04L).setu(0);
          MEMORY.ref(1, a1).offset(0x05L).setu(0);
          MEMORY.ref(1, a1).offset(0x06L).setu(0);
          MEMORY.ref(1, a1).offset(0x07L).setu(0x3aL);
          MEMORY.ref(2, a1).offset(0x08L).setu(sp1c);
          MEMORY.ref(2, a1).offset(0x0aL).setu(sp2c);
          MEMORY.ref(1, a1).offset(0x0cL).setu(data._10.svec_1c.getX());
          MEMORY.ref(1, a1).offset(0x0dL).setu(data._10.svec_1c.getY());
          MEMORY.ref(1, a1).offset(0x0eL).setu(data._10.svec_1c.getZ());
          MEMORY.ref(2, a1).offset(0x10L).setu(sp24);
          MEMORY.ref(2, a1).offset(0x12L).setu(sp34);
          MEMORY.ref(1, a1).offset(0x14L).setu(0);
          MEMORY.ref(1, a1).offset(0x15L).setu(0);
          MEMORY.ref(1, a1).offset(0x16L).setu(0);
          MEMORY.ref(2, a1).offset(0x18L).setu(sp18);
          MEMORY.ref(2, a1).offset(0x1aL).setu(sp28);
          MEMORY.ref(1, a1).offset(0x1cL).setu(0);
          MEMORY.ref(1, a1).offset(0x1dL).setu(0);
          MEMORY.ref(1, a1).offset(0x1eL).setu(0);
          MEMORY.ref(2, a1).offset(0x20L).setu(sp20);
          MEMORY.ref(2, a1).offset(0x22L).setu(sp30);
          insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x78L, a1);
          linkedListAddress_1f8003d8.addu(0x24L);
        }
      }

      //LAB_800d1538
      s6 = s6 + 0x10L;
    }

    //LAB_800d1558
    SetDrawMode(linkedListAddress_1f8003d8.deref(4).cast(DR_MODE::new), false, true, GetTPage(0x1L, 0x1L, 0, 0), null);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x78L, linkedListAddress_1f8003d8.get());
    linkedListAddress_1f8003d8.addu(0xcL);
  }

  @Method(0x800d15d8L)
  public static void FUN_800d15d8(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c data) {
    final BttlScriptData6cSub10 sp80 = data._44.derefAs(BttlScriptData6cSub10.class);
    long sp84 = sp80._0c.get();

    final long[] sp0x18 = new long[3];
    final long[] sp0x28 = new long[3];

    //LAB_800d16fc
    for(int sp78 = 0; sp78 < sp80.count_04.get(); sp78++) {
      if(MEMORY.ref(1, sp84).offset(0x0L).get() != 0) {
        MEMORY.ref(2, sp84).offset(0x6L).addu(MEMORY.ref(2, sp84).offset(0x8L).get());

        //LAB_800d1728
        for(int s7 = 0; s7 < 4; s7++) {
          final IntRef[] sp0x38 = {new IntRef(), new IntRef()};
          FUN_800d1194(data, sp80, sp0x38);

          //LAB_800d174c
          for(int s4 = 0; s4 < 3; s4++) {
            final long s0 = Math.max(0, _800c6d94.offset(s7 * 0x6L).offset(s4 * 0x2L).get() - MEMORY.ref(2, sp84).offset(0x6L).get());

            //LAB_800d1784
            final long s2 = _800c6dac.offset(s7 * 0x6L).offset(s4 * 0x2L).getAddress();
            sp0x18[s4] = (int)(rcos(MEMORY.ref(2, sp84).offset(0x2L).getSigned() + MEMORY.ref(2, s2).offset(0x0L).getSigned()) * s0) >> 12;
            sp0x18[s4] += sp0x38[0].get();
            sp0x28[s4] = (int)(rsin(MEMORY.ref(2, sp84).offset(0x2L).getSigned() + MEMORY.ref(2, s2).offset(0x0L).getSigned()) * s0) >> 12;
            sp0x28[s4] += sp0x38[1].get();
          }

          final long a1 = linkedListAddress_1f8003d8.get();
          linkedListAddress_1f8003d8.addu(0x1cL);
          MEMORY.ref(1, a1).offset(0x03L).setu(0x6L);
          MEMORY.ref(1, a1).offset(0x04L).setu(0);
          MEMORY.ref(1, a1).offset(0x05L).setu(0);
          MEMORY.ref(1, a1).offset(0x06L).setu(0);
          MEMORY.ref(1, a1).offset(0x07L).setu(0x32L);
          MEMORY.ref(2, a1).offset(0x08L).setu(sp0x18[0]);
          MEMORY.ref(2, a1).offset(0x0aL).setu(sp0x28[0]);
          MEMORY.ref(1, a1).offset(0x0cL).setu(0);
          MEMORY.ref(1, a1).offset(0x0dL).setu(0);
          MEMORY.ref(1, a1).offset(0x0eL).setu(0);
          MEMORY.ref(2, a1).offset(0x10L).setu(sp0x18[1]);
          MEMORY.ref(2, a1).offset(0x12L).setu(sp0x28[1]);
          MEMORY.ref(1, a1).offset(0x14L).setu(data._10.svec_1c.getX());
          MEMORY.ref(1, a1).offset(0x15L).setu(data._10.svec_1c.getY());
          MEMORY.ref(1, a1).offset(0x16L).setu(data._10.svec_1c.getZ());
          MEMORY.ref(2, a1).offset(0x18L).setu(sp0x18[2]);
          MEMORY.ref(2, a1).offset(0x1aL).setu(sp0x28[2]);
          insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x78L, a1);
        }
      }

      //LAB_800d190c
      sp84 = sp84 + 0x10L;
    }

    //LAB_800d1940
    SetDrawMode(linkedListAddress_1f8003d8.deref(4).cast(DR_MODE::new), false, true, GetTPage(0x1L, 0x1L, 0, 0), null);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x78L, linkedListAddress_1f8003d8.get());
    linkedListAddress_1f8003d8.addu(0xcL);
  }

  @Method(0x800d19c0L)
  public static void FUN_800d19c0(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c data) {
    removeFromLinkedList(data._44.derefAs(BttlScriptData6cSub10.class)._0c.get());
  }

  @Method(0x800d19ecL)
  public static long FUN_800d19ec(final RunningScript s3) {
    final int count = s3.params_20.get(2).deref().get();

    final int scriptIndex = FUN_800e80c4(
      s3.scriptStateIndex_00.get(),
      0x10L,
      null,
      _800c6dc4.get(s3.params_20.get(3).deref().get()).deref(),
      MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "FUN_800d19c0", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new),
      BttlScriptData6cSub10::new
    );

    final BttlScriptData6cSub10 s0 = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(BttlScriptData6c.class)._44.derefAs(BttlScriptData6cSub10.class);
    long t4 = addToLinkedListTail(count * 0x10L);
    s0._00.set(s3.params_20.get(1).deref().get());
    s0.count_04.set(count);
    s0._08.set(0);
    s0._0c.set(t4);

    //LAB_800d1ac4
    for(int i = 0; i < count; i++) {
      MEMORY.ref(1, t4).offset(0x0L).setu(0x1L);
      MEMORY.ref(2, t4).offset(0x2L).setu(seed_800fa754.advance().get() % 4097);
      MEMORY.ref(2, t4).offset(0x4L).setu(0x10L);
      MEMORY.ref(2, t4).offset(0x6L).setu(seed_800fa754.advance().get() % 31);
      MEMORY.ref(2, t4).offset(0x8L).setu(seed_800fa754.advance().get() % 21 + 10);
      MEMORY.ref(2, t4).offset(0xaL).setu(seed_800fa754.advance().get() % 11 - 5);
      MEMORY.ref(4, t4).offset(0xcL).setu(0);
      t4 = t4 + 0x10L;
    }

    //LAB_800d1c7c
    s3.params_20.get(0).deref().set(scriptIndex);
    return 0;
  }

  @Method(0x800d21b8L)
  public static void FUN_800d21b8(final BttlScriptData6c a0, final long angle, final long[] a2, final BttlScriptData6cSub14 a3) {
    if((int)a0._10._00.get() >= 0) {
      final VECTOR sp0x20 = new VECTOR().set(
        rcos(angle) * (a0._10.svec_16.getX() / a3._01.get() + a0._10.vec_28.getX()) >> 12,
        rsin(angle) * (a0._10.svec_16.getY() / a3._01.get() + a0._10.vec_28.getX()) >> 12,
        a0._10.vec_28.getY()
      );

      final Ref<Long> sp0x10 = new Ref<>();
      final Ref<Long> sp0x14 = new Ref<>();
      FUN_800cfb14(a0, sp0x20, sp0x10, sp0x14);

      final VECTOR sp0x30 = new VECTOR().set(
        rcos(angle + a3._08.get()) * (a0._10.svec_16.getX() / a3._01.get() + a0._10.vec_28.getX()) >> 12,
        rsin(angle + a3._08.get()) * (a0._10.svec_16.getY() / a3._01.get() + a0._10.vec_28.getX()) >> 12,
        a0._10.vec_28.getY()
      );

      final Ref<Long> sp0x18 = new Ref<>();
      final Ref<Long> sp0x1c = new Ref<>();
      FUN_800cfb14(a0, sp0x30, sp0x18, sp0x1c);

      final long addr = linkedListAddress_1f8003d8.get();
      linkedListAddress_1f8003d8.addu(0x24L);
      MEMORY.ref(1, addr).offset(0x03L).setu(0x8L);
      MEMORY.ref(1, addr).offset(0x04L).setu(a0._10.svec_1c.getX());
      MEMORY.ref(1, addr).offset(0x05L).setu(a0._10.svec_1c.getY());
      MEMORY.ref(1, addr).offset(0x06L).setu(a0._10.svec_1c.getZ());
      MEMORY.ref(1, addr).offset(0x07L).setu(0x3aL);
      MEMORY.ref(1, addr).offset(0x0cL).setu(a0._10.svec_1c.getX());
      MEMORY.ref(1, addr).offset(0x0dL).setu(a0._10.svec_1c.getY());
      MEMORY.ref(1, addr).offset(0x0eL).setu(a0._10.svec_1c.getZ());
      MEMORY.ref(1, addr).offset(0x14L).setu(a3._0c.get());
      MEMORY.ref(1, addr).offset(0x15L).setu(a3._0d.get());
      MEMORY.ref(1, addr).offset(0x16L).setu(a3._0e.get());
      MEMORY.ref(1, addr).offset(0x1cL).setu(a3._0c.get());
      MEMORY.ref(1, addr).offset(0x1dL).setu(a3._0d.get());
      MEMORY.ref(1, addr).offset(0x1eL).setu(a3._0e.get());
      MEMORY.ref(2, addr).offset(0x08L).setu(sp0x10.get());
      MEMORY.ref(2, addr).offset(0x0aL).setu(sp0x14.get());
      MEMORY.ref(2, addr).offset(0x10L).setu(sp0x18.get());
      MEMORY.ref(2, addr).offset(0x12L).setu(sp0x1c.get());
      MEMORY.ref(2, addr).offset(0x18L).setu(a2[2]);
      MEMORY.ref(2, addr).offset(0x1aL).setu(a2[3]);
      MEMORY.ref(2, addr).offset(0x20L).setu(a2[4]);
      MEMORY.ref(2, addr).offset(0x22L).setu(a2[5]);
      insertElementIntoLinkedList(tags_1f8003d0.getPointer() + (a3._04.get() + a0._10._22.get()) / 4 * 4, addr);
    }

    //LAB_800d2460
  }

  @Method(0x800d247cL)
  public static void FUN_800d247c(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c data) {
    long v1;
    long a0;
    long s1;
    long sp54;
    long sp50;
    long sp30;
    long sp28;
    long sp2c;
    final BttlScriptData6cSub14 s0 = data._44.derefAs(BttlScriptData6cSub14.class);
    s0._08.set(0x1000L / (0x4L << s0._00.get()));
    final VECTOR sp0x18 = new VECTOR();
    final Ref<Long> sp0x48 = new Ref<>();
    final Ref<Long> sp0x4c = new Ref<>();
    s0._04.set(FUN_800cfb14(data, sp0x18, sp0x48, sp0x4c) / 4);
    a0 = data._10._22.get();
    v1 = s0._04.get() + a0;
    if((int)v1 >= 0xa0L) {
      if((int)v1 >= 0xffeL) {
        s0._04.set(0xffeL - a0);
      }

      //LAB_800d2510
      final VECTOR sp0x38 = new VECTOR().set(
        rcos(0) * (data._10.svec_16.getX() / s0._01.get()) >> 12,
        rsin(0) * (data._10.svec_16.getY() / s0._01.get()) >> 12,
        0
      );

      final Ref<Long> sp0x58 = new Ref<>();
      final Ref<Long> sp0x5c = new Ref<>();
      FUN_800cfb14(data, sp0x38, sp0x58, sp0x5c);
      s0._0c.set((int)(data._10._24.get() >> 16 & 0xff));
      s0._0d.set((int)(data._10._24.get() >>  8 & 0xff));
      s0._0e.set((int)(data._10._24.get()       & 0xff));

      //LAB_800d25b4
      for(s1 = 0; s1 < 0x1000; ) {
        //TODO Why? Unused?
        sp28 = sp0x38.getX();
        sp2c = sp0x38.getY();
        sp30 = sp0x38.getZ();

        sp0x38.set(
          rcos(s1 + s0._08.get()) * (data._10.svec_16.getX() / s0._01.get()) >> 12,
          rsin(s1 + s0._08.get()) * (data._10.svec_16.getY() / s0._01.get()) >> 12,
          0
        );

        FUN_800cfb14(data, sp0x38, sp0x58, sp0x5c);
        s0.renderer_10.deref().run(data, s1, new long[] {sp0x48.get(), sp0x4c.get(), sp0x58.get(), sp0x5c.get(), sp0x58.get(), sp0x5c.get()}, s0);

        s1 = s1 + s0._08.get();
      }

      SetDrawMode(linkedListAddress_1f8003d8.deref(4).cast(DR_MODE::new), false, true, GetTPage(0x1L, (data._10._00.get() & 0x1000_0000L) != 0 ? 0x1L : 0x2L, 0, 0), null);
      insertElementIntoLinkedList(tags_1f8003d0.getPointer() + (s0._04.get() + data._10._22.get()) / 4 * 4, linkedListAddress_1f8003d8.get());
      linkedListAddress_1f8003d8.addu(0xcL);
    }

    //LAB_800d2710
  }

  @Method(0x800d272cL)
  public static void FUN_800d272c(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c data) {
    // no-op
  }

  @Method(0x800d2734L)
  public static long FUN_800d2734(final RunningScript s0) {
    final int s2 = s0.params_20.get(1).deref().get();
    final int s1 = s0.params_20.get(2).deref().get();
    final int scriptIndex = FUN_800e80c4(
      s0.scriptStateIndex_00.get(),
      0x14L,
      null,
      MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "FUN_800d247c", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "FUN_800d272c", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new),
      BttlScriptData6cSub14::new
    );

    final BttlScriptData6c v1 = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(BttlScriptData6c.class);

    //LAB_800d27b4
    v1._10.svec_16.set((short)0x1000, (short)0x1000, (short)0x1000);

    final BttlScriptData6cSub14 a0 = v1._44.derefAs(BttlScriptData6cSub14.class);
    a0._00.set(s2);
    a0._01.set(s1 < 5 ? 1 : 4);
    a0.renderer_10.set(effectRenderers_800fa758.get(s1).deref());
    s0.params_20.get(0).deref().set(scriptIndex);
    return 0;
  }

  @Method(0x800d2810L)
  public static void FUN_800d2810(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c data) {
    long v0;
    long v1;
    long a0;
    long a1;
    long t1;
    long s0;
    long s1;
    long s3 = 0;
    long s6;
    long sp78;
    long sp7a;
    long sp7c;
    long sp80;
    long sp82;
    long sp84;

    final VECTOR sp0x58 = new VECTOR();
    final IntRef[] sp0x18 = new IntRef[7];
    final IntRef[] sp0x38 = new IntRef[7];

    Arrays.setAll(sp0x18, i -> new IntRef());
    Arrays.setAll(sp0x38, i -> new IntRef());

    final BttlScriptData6cSub06 s7 = data._44.derefAs(BttlScriptData6cSub06.class);
    s7._02.incr();
    s7._04.add((short)0x400);
    v0 = _800fa76c.getAddress();
    s1 = v0 + 0x18L;

    //LAB_800d2888
    for(int i = 6; i >= 0; i--) {
      //LAB_800d289c
      sp0x58.setX(data._10.vec_04.getX() + (i != 0 ? data._10.svec_16.getX() / 4 : 0));
      sp0x58.setY((int)(data._10.vec_04.getY() + (MEMORY.ref(2, s1).offset(0x2L).getSigned() * data._10.svec_16.getY() >> 12)));
      sp0x58.setZ((int)(data._10.vec_04.getZ() + (MEMORY.ref(2, s1).offset(0x0L).getSigned() * data._10.svec_16.getZ() >> 12)));
      s3 = FUN_800cf244(sp0x58, sp0x18[i], sp0x38[i]);
      s1 = s1 - 0x4L;
    }

    s3 = (int)s3 >> 2;
    sp78 = MathHelper.clamp(data._10.svec_1c.getX() * 0x100L - 0x100L, 0, 0x8000) >>> 7;
    sp7a = MathHelper.clamp(data._10.svec_1c.getY() * 0x100L - 0x100L, 0, 0x8000) >>> 7;
    sp7c = MathHelper.clamp(data._10.svec_1c.getZ() * 0x100L - 0x100L, 0, 0x8000) >>> 7;
    sp78 = Math.min((sp78 + sp7a + sp7c) / 3 * 2, 0xffL);

    //LAB_800d2a80
    //LAB_800d2a9c
    for(int i = 0; i < 5; i++) {
      a1 = linkedListAddress_1f8003d8.get();
      linkedListAddress_1f8003d8.addu(0x1cL);
      MEMORY.ref(1, a1).offset(0x3L).setu(0x6L);
      MEMORY.ref(4, a1).offset(0x4L).setu(0x3280_8080L);
      MEMORY.ref(1, a1).offset(0x4L).setu(data._10.svec_1c.getX());
      MEMORY.ref(1, a1).offset(0x5L).setu(data._10.svec_1c.getY());
      MEMORY.ref(1, a1).offset(0x6L).setu(data._10.svec_1c.getZ());
      MEMORY.ref(1, a1).offset(0xcL).setu(data._10.svec_1c.getX());
      MEMORY.ref(1, a1).offset(0xdL).setu(data._10.svec_1c.getY());
      MEMORY.ref(1, a1).offset(0xeL).setu(data._10.svec_1c.getZ());
      MEMORY.ref(1, a1).offset(0x14L).setu(sp78);
      MEMORY.ref(1, a1).offset(0x15L).setu(sp78);
      MEMORY.ref(1, a1).offset(0x16L).setu(sp78);
      MEMORY.ref(2, a1).offset(0x08L).setu(sp0x18[i + 1].get());
      MEMORY.ref(2, a1).offset(0x0aL).setu(sp0x38[i + 1].get());
      MEMORY.ref(2, a1).offset(0x10L).setu(sp0x18[i + 2].get());
      MEMORY.ref(2, a1).offset(0x12L).setu(sp0x38[i + 2].get());
      MEMORY.ref(2, a1).offset(0x18L).setu(sp0x18[0].get());
      MEMORY.ref(2, a1).offset(0x1aL).setu(sp0x38[0].get());

      a0 = data._10._22.get();
      v1 = s3 + a0;
      if((int)v1 >= 0xa0L) {
        if((int)v1 >= 0xffeL) {
          a0 = 0xffeL - s3;
        }

        //LAB_800d2bc0
        insertElementIntoLinkedList(tags_1f8003d0.getPointer() + (s3 + a0) / 4 * 4, a1);
      }

      //LAB_800d2bdc
    }

    s0 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0xcL);
    SetDrawMode(MEMORY.ref(4, s0, DR_MODE::new), false, true, GetTPage(0x1L, 0x1L, 0, 0), null);

    v1 = data._10._22.get();
    a0 = s3 + v1;
    if((int)a0 >= 0xa0L) {
      if((int)a0 >= 0xffeL) {
        v1 = 0xffeL - s3;
      }

      //LAB_800d2c5c
      insertElementIntoLinkedList(tags_1f8003d0.getPointer() + (s3 + v1) / 4 * 4, s0);
    }

    //LAB_800d2c78
    s6 = 0x1000L;
    sp78 = data._10.svec_1c.getX();
    sp7a = data._10.svec_1c.getY();
    sp7c = data._10.svec_1c.getZ();
    sp80 = sp78 >>> 2;
    sp82 = sp7a >>> 2;
    sp84 = sp7c >>> 2;

    //LAB_800d2cfc
    long s7_0 = 0;
    for(int i = 0; i < 4; i++) {
      s6 = s6 + s7._04.get() / 4;
      s7_0 = s7_0 + data._10.svec_16.getX() / 4;
      t1 = _800fa76c.getAddress();
      s1 = t1 + 0x4L;
      sp78 = sp78 - sp80;
      sp7a = sp7a - sp82;
      sp7c = sp7c - sp84;

      //LAB_800d2d4c
      for(int n = 1; n < 7; n++) {
        sp0x58.setX((int)(s7_0 + data._10.vec_04.getX()));
        sp0x58.setY((int)(((MEMORY.ref(2, s1).offset(0x2L).getSigned() * data._10.svec_16.getY() >> 12) * s6 >> 12) + data._10.vec_04.getY()));
        sp0x58.setZ((int)(((MEMORY.ref(2, s1).offset(0x0L).getSigned() * data._10.svec_16.getZ() >> 12) * s6 >> 12) + data._10.vec_04.getZ()));
        s3 = (int)FUN_800cf244(sp0x58, sp0x18[n], sp0x38[n]) / 4;
        s1 = s1 + 0x4L;
      }

      //LAB_800d2e20
      for(int n = 0; n < 5; n++) {
        a1 = linkedListAddress_1f8003d8.get();
        linkedListAddress_1f8003d8.addu(0x10L);
        MEMORY.ref(1, a1).offset(0x3L).setu(0x3L);
        MEMORY.ref(1, a1).offset(0x4L).setu(sp78);
        MEMORY.ref(1, a1).offset(0x5L).setu(sp7a);
        MEMORY.ref(1, a1).offset(0x6L).setu(sp7c);
        MEMORY.ref(1, a1).offset(0x7L).setu(0x42L);
        MEMORY.ref(2, a1).offset(0x8L).setu(sp0x18[n + 1].get());
        MEMORY.ref(2, a1).offset(0xaL).setu(sp0x38[n + 1].get());
        MEMORY.ref(2, a1).offset(0xcL).setu(sp0x18[n + 2].get());
        MEMORY.ref(2, a1).offset(0xeL).setu(sp0x38[n + 2].get());

        a0 = data._10._22.get();
        v1 = s3 + a0;
        if((int)v1 >= 0xa0L) {
          if((int)v1 >= 0xffeL) {
            a0 = 0xffeL - s3;
          }

          //LAB_800d2ee8
          insertElementIntoLinkedList(tags_1f8003d0.getPointer() + (s3 + a0) / 4 * 4, a1);
        }

        //LAB_800d2f08
      }

      s0 = linkedListAddress_1f8003d8.get();
      linkedListAddress_1f8003d8.addu(0xcL);
      SetDrawMode(MEMORY.ref(4, s0, DR_MODE::new), false, true, GetTPage(0x1L, 0x1L, 0, 0), null);
      a0 = data._10._22.get();
      v1 = s3 + a0;
      if((int)v1 >= 0xa0L) {
        if((int)v1 >= 0xffeL) {
          a0 = 0xffeL - s3;
        }

        //LAB_800d2f88
        insertElementIntoLinkedList(tags_1f8003d0.getPointer() + (s3 + a0) / 4 * 4, s0);
      }

      //LAB_800d2fa4
    }
  }

  @Method(0x800d2fecL)
  public static void FUN_800d2fec(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c data) {
    // No-op
  }

  @Method(0x800d2ff4L)
  public static long FUN_800d2ff4(final RunningScript s0) {
    final int a1 = FUN_800e80c4(s0.scriptStateIndex_00.get(), 0x6L, null, MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "FUN_800d2810", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new), MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "FUN_800d2fec", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new), BttlScriptData6cSub06::new);
    final BttlScriptData6c a0 = scriptStatePtrArr_800bc1c0.get(a1).deref().innerStruct_00.derefAs(BttlScriptData6c.class);
    final BttlScriptData6cSub06 v0 = a0._44.derefAs(BttlScriptData6cSub06.class);
    v0._00.set(1);
    v0._02.set(0);
    v0._04.set((short)0);
    a0._10.svec_1c.setX((short)255);
    a0._10.svec_1c.setY((short)0);
    a0._10.svec_1c.setZ((short)0);
    s0.params_20.get(0).deref().set(a1);
    return 0;
  }

  @Method(0x800d30c0L)
  public static void FUN_800d30c0(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c data) {
    BttlScriptData6cSub34_2 s1 = data._44.derefAs(BttlScriptData6cSub34_2.class);
    long s2 = s1.ptr_30.get();

    //LAB_800d30fc
    for(int animIndex = 0; animIndex < s1.animCount_04.get(); animIndex++) {
      if(MEMORY.ref(1, s2).offset(0x0L).getSigned() == 0x1L) {
        s1._0c.r_14.set((int)(MEMORY.ref(2, s2).offset(0x24L).get() >>> 8));
        s1._0c.g_15.set((int)(MEMORY.ref(2, s2).offset(0x26L).get() >>> 8));
        s1._0c.b_16.set((int)(MEMORY.ref(2, s2).offset(0x28L).get() >>> 8));
        s1._0c._20.set((int)MEMORY.ref(4, s2).offset(0x0cL).get());
        s1._0c._1c.set((short)(data._10.svec_16.getX() + MEMORY.ref(2, s2).offset(0x04L).get()));
        s1._0c._1e.set((short)(data._10.svec_16.getY() + MEMORY.ref(2, s2).offset(0x04L).get()));
        FUN_800e7ea4(s1._0c, MEMORY.ref(4, s2 + 0x14L, VECTOR::new));
      }

      //LAB_800d3174
      s2 = s2 + 0x30L;
    }

    //LAB_800d3190
  }

  @Method(0x800d31b0L)
  public static void FUN_800d31b0(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c data) {
    final BttlScriptData6cSub34_2 s1 = data._44.derefAs(BttlScriptData6cSub34_2.class);

    s1._02.decr();
    if(s1._02.get() == 0) {
      FUN_80015d38(index);
    } else {
      //LAB_800d320c
      long s2 = s1.ptr_30.get();
      s1._00.add((short)2);

      //LAB_800d322c
      for(int animIndex = 0; animIndex < s1.animCount_04.get(); animIndex++) {
        if(s1._00.get() >= animIndex + 0x1L && MEMORY.ref(1, s2).offset(0x0L).getSigned() == -1) {
          MEMORY.ref(1, s2).offset(0x0L).setu(0x1L);
          MEMORY.ref(1, s2).offset(0x1L).setu(0x8L);
          MEMORY.ref(4, s2).offset(0x10L).setu(0);
          MEMORY.ref(4, s2).offset(0x04L).setu(0);
          seed_800fa754.advance();
          MEMORY.ref(4, s2).offset(0x0cL).setu(seed_800fa754.get() % 4097);
          seed_800fa754.advance();
          MEMORY.ref(4, s2).offset(0x08L).setu(seed_800fa754.get() % 49 + 104);
          MEMORY.ref(2, s2).offset(0x24L).setu(data._10.svec_1c.getX() << 8);
          MEMORY.ref(2, s2).offset(0x26L).setu(data._10.svec_1c.getY() << 8);
          MEMORY.ref(2, s2).offset(0x28L).setu(data._10.svec_1c.getZ() << 8);
          MEMORY.ref(2, s2).offset(0x2aL).setu(MEMORY.ref(2, s2).offset(0x24L).get() / MEMORY.ref(1, s2).offset(0x01L).getSigned());
          MEMORY.ref(2, s2).offset(0x2cL).setu(MEMORY.ref(2, s2).offset(0x26L).get() / MEMORY.ref(1, s2).offset(0x01L).getSigned());
          MEMORY.ref(2, s2).offset(0x2eL).setu(MEMORY.ref(2, s2).offset(0x28L).get() / MEMORY.ref(1, s2).offset(0x01L).getSigned());
          final VECTOR sp0x10 = new VECTOR();
          FUN_800cffd8(s1.scriptIndex_08.get(), sp0x10, animIndex);
          MEMORY.ref(4, s2).offset(0x14L).setu(sp0x10.getX());
          MEMORY.ref(4, s2).offset(0x18L).setu(sp0x10.getY());
          MEMORY.ref(4, s2).offset(0x1cL).setu(sp0x10.getZ());
          FUN_800d0094(s1.scriptIndex_08.get(), animIndex, 0);
        }

        //LAB_800d33d0
        if(MEMORY.ref(1, s2).offset(0x00L).getSigned() > 0) {
          MEMORY.ref(1, s2).offset(0x01L).subu(0x1L);

          if(MEMORY.ref(1, s2).offset(0x01L).get() == 0) {
            MEMORY.ref(1, s2).offset(0x00L).setu(0);
          }

          //LAB_800d3400
          MEMORY.ref(4, s2).offset(0x0cL).addu(MEMORY.ref(4, s2).offset(0x10L).get());
          MEMORY.ref(2, s2).offset(0x24L).subu(MEMORY.ref(2, s2).offset(0x2aL).get());
          MEMORY.ref(2, s2).offset(0x26L).subu(MEMORY.ref(2, s2).offset(0x2cL).get());
          MEMORY.ref(2, s2).offset(0x28L).subu(MEMORY.ref(2, s2).offset(0x2eL).get());
          MEMORY.ref(4, s2).offset(0x04L).addu(MEMORY.ref(4, s2).offset(0x08L).get());
        }

        //LAB_800d3450
        s2 = s2 + 0x30L;
      }
    }

    //LAB_800d346c
  }

  @Method(0x800d3490L)
  public static void FUN_800d3490(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c data) {
    removeFromLinkedList(data._44.derefAs(BttlScriptData6cSub34_2.class).ptr_30.get());
  }

  @Method(0x800d34bcL)
  public static long FUN_800d34bc(final RunningScript a0) {
    long v0;
    long v1;

    final long fp = FUN_800e80c4(
      a0.scriptStateIndex_00.get(),
      0x34L,
      MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "FUN_800d31b0", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "FUN_800d30c0", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "FUN_800d3490", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new),
      BttlScriptData6cSub34_2::new
    );

    final int animCount = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(1).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class)._148.animCount_98.get();
    final BttlScriptData6c data = scriptStatePtrArr_800bc1c0.get((int)fp).deref().innerStruct_00.derefAs(BttlScriptData6c.class);
    final BttlScriptData6cSub34_2 sub = data._44.derefAs(BttlScriptData6cSub34_2.class);
    long s4 = addToLinkedListTail(animCount * 0x30L);
    sub.ptr_30.set(s4); //TODO
    sub._00.set((short)0);
    sub._02.set(animCount + 8);
    sub.animCount_04.set(animCount);
    sub._06.set(0);
    sub.scriptIndex_08.set(a0.params_20.get(1).deref().get());

    //LAB_800d35a0
    for(int animIndex = 0; animIndex < sub.animCount_04.get(); animIndex++) {
      FUN_800d0094(sub.scriptIndex_08.get(), animIndex, 0x1L);
      MEMORY.ref(1, s4).offset(0x0L).setu(-0x1L);
      s4 = s4 + 0x30L;
    }

    //LAB_800d35cc
    v1 = a0.params_20.get(2).deref().get() & 0xffL;
    v1 = v1 << 3;
    v0 = _800c6948.get() + v1;
    v0 = MEMORY.ref(1, v0).offset(0x0L).get() & 0x3fL;
    v0 = v0 << 2;
    sub._0c.u_0e.set((int)v0);
    v0 = _800c6948.get() + v1;
    v0 = MEMORY.ref(1, v0).offset(0x2L).get();
    sub._0c.v_0f.set((int)v0);
    v1 = _800c6948.get() + v1;
    sub._0c.w_08.set((int)MEMORY.ref(1, v1).offset(0x4L).get());
    sub._0c.h_0a.set((int)MEMORY.ref(1, v1).offset(0x5L).get());
    v0 = MEMORY.ref(2, v1).offset(0x6L).get() << 4;
    v0 = v0 & 0x3ffL;
    sub._0c.clutX_10.set((int)v0);
    v0 = MEMORY.ref(2, v1).offset(0x6L).get() >>> 6;
    v0 = v0 & 0x1ffL;
    sub._0c.clutY_12.set((int)v0);
    v0 = MEMORY.ref(2, v1).offset(0x2L).get() & 0x100L;
    v0 = v0 >>> 4;
    v1 = MEMORY.ref(2, v1).offset(0x0L).get() & 0x3ffL;
    v1 = v1 >>> 6;
    v0 = v0 | v1;
    sub._0c._0c.set((int)v0);
    sub._0c.x_04.set((short)(-sub._0c.w_08.get() >> 1));
    sub._0c._18.set((short)0);
    sub._0c._1a.set((short)0);
    sub._0c.y_06.set((short)(-sub._0c.h_0a.get() >> 1));
    sub._0c._00.set(data._10._00.get());
    a0.params_20.get(0).deref().set((int)fp);
    return 0;
  }

  @Method(0x800d36e0L)
  public static long FUN_800d36e0(final int a0, final int addition) {
    _800c6790.setu(additionNames_800fa8d4.getAddress());

    //LAB_800d3708
    for(int i = 0; i < a0; i++) {
      //LAB_800d3724
      while(_800c6790.deref(1).get() != 0x2fL) {
        _800c6790.addu(0x1L);
      }

      //LAB_800d3744
      _800c6790.addu(0x1L);
    }

    //LAB_800d3760
    //LAB_800d3778
    for(int i = 0; i < addition; i++) {
      //LAB_800d3790
      while(_800c6790.deref(1).get() != 0) {
        _800c6790.addu(0x1L);
      }

      //LAB_800d37b0
      _800c6790.addu(0x1L);
    }

    //LAB_800d37cc
    return _800c6790.get();
  }

  @Method(0x800d37dcL)
  public static void FUN_800d37dc(final short a0, final short a1, final short a2, final short addition, final short a4, final byte a5) {
    final long a1_0 = FUN_800d36e0(a2, addition) + a4;
    long a2_0 = 0;

    //LAB_800d3838
    long v1;
    do {
      v1 = _800fa788.offset(a2_0).get();

      if(v1 == 0) {
        //LAB_800d3860
        a2_0 = 0x5bL;
        break;
      }

      a2_0++;
    } while(MEMORY.ref(1, a1_0).offset(0x0L).get() != v1);

    //LAB_800d3864
    FUN_80018d60(a0, a1, a2_0 % 21 * 12 & 0xfcL, a2_0 / 21 * 12 + 144 & 0xfcL, 0xcL, 0xcL, 0xaL, 0x1L, new byte[] {a5, a5, a5}, 0x1000L);
  }

  @Method(0x800d3910L)
  public static int FUN_800d3910(final long a0) {
    //LAB_800d391c
    int a2;
    for(a2 = 0; ; a2++) {
      if(_800fa788.offset(a2).get() == 0) {
        a2 = 0;
        break;
      }

      if(MEMORY.ref(1, a0).offset(0x0L).get() == _800fa788.offset(a2).get()) {
        break;
      }
    }

    //LAB_800d3944
    //LAB_800d3948
    return (int)(10 - _800fa7cc.offset(a2 * 0x4L).get());
  }

  @Method(0x800d3968L)
  public static void FUN_800d3968(final UnsignedShortRef a0, final UnsignedShortRef a1, final int a2, final int addition) {
    final long s2 = FUN_800d36e0(a2, addition);

    int s1 = 0;
    //LAB_800d39b8
    for(int i = 0; MEMORY.ref(1, s2).offset(i).get() != 0; i++) {
      s1 += FUN_800d3910(s2 + i);
    }

    //LAB_800d39ec
    a0.set(144 - s1);
    a1.set(64);
  }

  @Method(0x800d3a20L)
  public static void FUN_800d3a20(final BttlScriptData1c a0, final long a1, final long a2, final long a3) {
    FUN_800d37dc((short)MEMORY.ref(2, a1).offset(0x4L).getSigned(), (short)MEMORY.ref(2, a1).offset(0x6L).getSigned(), (short)0, (short)a0.addition_02.get(), (short)a3, (byte)(a2 & 0xffL));
  }

  @Method(0x800d3a64L)
  public static void FUN_800d3a64(final BttlScriptData1c a0, final long a1, final long a2, final long a3) {
    final String sp0x18 = String.valueOf(a0._10.get());

    long s4;
    if(a0._04.get() < 0x19L) {
      s4 = a0._04.get() & 0x1L;
    } else {
      s4 = 0;
    }

    //LAB_800d3ab8
    long s2 = a2 & 0xffL;

    //LAB_800d3ac4
    for(; s4 >= 0; s4--) {
      long s1 = MEMORY.ref(2, a1).offset(0x4L).get();

      //LAB_800d3ad4
      for(int i = 0; i < sp0x18.length(); i++) {
        FUN_800d3f98((short)s1, (short)MEMORY.ref(2, a1).offset(0x6L).getSigned(), sp0x18.charAt(i) - 0x30, (short)41, (byte)s2);
        s1 = s1 + 0x8L;
      }

      //LAB_800d3b08
      FUN_800d3f98((short) s1         , (short)MEMORY.ref(2, a1).offset(0x6L).getSigned(), 0x0dL, (short)41, (byte)s2);
      FUN_800d3f98((short)(s1 + 0x08L), (short)MEMORY.ref(2, a1).offset(0x6L).getSigned(), 0x0eL, (short)41, (byte)s2);
      FUN_800d3f98((short)(s1 + 0x10L), (short)MEMORY.ref(2, a1).offset(0x6L).getSigned(), 0x0fL, (short)41, (byte)s2);
      FUN_800d3f98((short)(s1 + 0x18L), (short)MEMORY.ref(2, a1).offset(0x6L).getSigned(), 0x10L, (short)41, (byte)s2);
    }

    //LAB_800d3b98
  }

  @Method(0x800d3bb8L)
  public static void FUN_800d3bb8(final int index, final ScriptState<BttlScriptData1c> state, final BttlScriptData1c data) {
    final BttlScriptData1c s5 = state.innerStruct_00.deref();
    s5._04.incr();

    if(_800faa9d.get() == 0) {
      FUN_80015d38(index);
    } else {
      //LAB_800d3c10
      long s6 = s5._18.get();

      //LAB_800d3c24
      for(long s7 = 0; s7 < s5._08.get(); s7++) {
        if(MEMORY.ref(1, s6).offset(0x0L).get() != 0) {
          MEMORY.ref(2, s6).offset(0x4L).addu(s5._0c.get());

          if(MEMORY.ref(2, s6).offset(0x4L).getSigned() >= MEMORY.ref(2, s6).offset(0x8L).get()) {
            MEMORY.ref(2, s6).offset(0x4L).setu(MEMORY.ref(2, s6).offset(0x8L).get());
            MEMORY.ref(1, s6).offset(0x0L).setu(0);
          }
        } else {
          //LAB_800d3c70
          if(MEMORY.ref(2, s6).offset(0x2L).getSigned() > 0) {
            MEMORY.ref(2, s6).offset(0x2L).subu(0x1L);
          }
        }

        //LAB_800d3c84
        //LAB_800d3c88
        s5._14.deref().run(s5, s6, 0x80L, s7);
        long s4 = MEMORY.ref(2, s6).offset(0x4L).getSigned();
        long s2 = MEMORY.ref(2, s6).offset(0x2L).getSigned() * 0x10L;

        //LAB_800d3cbc
        for(long s3 = 0; s3 < MEMORY.ref(2, s6).offset(0x2L).getSigned() - 0x1L; s3++) {
          s2 = s2 - 0x10L;
          s4 = s4 - 0xaL;
          final long s0 = MEMORY.ref(2, s6).offset(0x4L).getSigned();
          MEMORY.ref(2, s6).offset(0x4L).setu(s4);
          s5._14.deref().run(s5, s6, s2 & 0xffL, s7);
          MEMORY.ref(2, s6).offset(0x4L).setu(s0);
        }

        //LAB_800d3d00
        s6 = s6 + 0xcL;
      }
    }

    //LAB_800d3d1c
  }

  @Method(0x800d3d48L)
  public static void FUN_800d3d48(final int index, final ScriptState<BttlScriptData1c> state, final BttlScriptData1c data) {
    removeFromLinkedList(state.innerStruct_00.deref()._18.get());
  }

  @Method(0x800d3d74L)
  public static long FUN_800d3d74(final RunningScript a0) {
    if(a0.params_20.get(1).deref().get() == -1) {
      _800faa9d.setu(0);
    } else {
      //LAB_800d3dc0
      final int addition = gameState_800babc8.charData_32c.get(a0.params_20.get(0).deref().get()).selectedAddition_19.get();
      final int scriptIndex = allocateScriptState(0x1cL, BttlScriptData1c::new);
      final ScriptState<BttlScriptData1c> s1 = scriptStatePtrArr_800bc1c0.get(scriptIndex).derefAs(ScriptState.classFor(BttlScriptData1c.class));
      loadScriptFile(scriptIndex, _8004f650, "", 0); //TODO
      setCallback04(scriptIndex, MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "FUN_800d3bb8", int.class, ScriptState.classFor(BttlScriptData1c.class), BttlScriptData1c.class), TriConsumerRef::new));
      setCallback0c(scriptIndex, MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "FUN_800d3d48", int.class, ScriptState.classFor(BttlScriptData1c.class), BttlScriptData1c.class), TriConsumerRef::new));
      final long a0_0 = FUN_800d36e0(0, addition);

      //LAB_800d3e5c
      int s2;
      for(s2 = 0; MEMORY.ref(1, a0_0).offset(s2).get() != 0; s2++) {
        //
      }

      //LAB_800d3e7c
      final BttlScriptData1c s0 = s1.innerStruct_00.deref();
      s0._00.set(0);
      s0.addition_02.set(addition);
      s0._04.set(0);
      s0._08.set(s2);
      s0._0c.set(120);
      s0._14.set(MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "FUN_800d3a20", BttlScriptData1c.class, long.class, long.class, long.class), QuadConsumerRef::new));
      s0._18.set(addToLinkedListTail(s2 * 0xcL));
      _800faa9d.setu(0x1L);

      final UnsignedShortRef sp0x10 = new UnsignedShortRef();
      final UnsignedShortRef sp0x12 = new UnsignedShortRef();
      FUN_800d3968(sp0x10, sp0x12, 0, addition);
      long s3_0 = FUN_800d36e0(0, addition);
      long s1_0 = s0._18.get();
      long s2_0 = -160;
      long sp10 = sp0x10.get();
      long sp12 = sp0x12.get();

      //LAB_800d3f18
      for(int s4 = 0; s4 < s2; s4++) {
        MEMORY.ref(2, s1_0).offset(0x4L).setu(s2_0);
        MEMORY.ref(2, s1_0).offset(0xaL).setu(sp12);
        MEMORY.ref(2, s1_0).offset(0x6L).setu(sp12);
        MEMORY.ref(2, s1_0).offset(0x8L).setu(sp10);
        MEMORY.ref(1, s1_0).offset(0x0L).setu(0x1L);
        MEMORY.ref(2, s1_0).offset(0x2L).setu(0x8L);
        sp10 = sp10 + FUN_800d3910(s3_0);
        s1_0 = s1_0 + 0xcL;
        s2_0 = s2_0 - 80;
        s3_0 = s3_0 + 0x1L;
      }
    }

    //LAB_800d3f70
    return 0;
  }

  @Method(0x800d3f98L)
  public static void FUN_800d3f98(final short a0, final short a1, final long a2, final short a3, final byte a4) {
    FUN_80018d60(a0, a1, a2 * 8 + 16 & 0xf8L, 0x28L, 0x8L, 0x10L, a3, 0x1L, new byte[] {a4, a4, a4}, 0x1000L);
  }

  @Method(0x800d4018L)
  public static void FUN_800d4018(final int index, final ScriptState<BttlScriptData40> state, final BttlScriptData40 data) {
    long v0;
    long v1;
    long t0;
    long s2;
    long s4;
    long s5;
    long s6;
    long fp;
    long sp20;
    long sp30;
    long sp28;
    long sp2c;
    final BttlScriptData40 s3 = state.innerStruct_00.deref();

    if(_800faa94.get() == 0 && s3._00.get() == 0) {
      if(s3._02.get() == 0) {
        FUN_80015d38(index);
        return;
      }

      //LAB_800d408c
      s3._02.sub(0x10);
    }

    //LAB_800d4090
    s3._04.incr();
    s3._10.add(s3._30.get());
    s3._30.add(0x200);

    if(s3._20.get() < s3._10.get()) {
      s3._10.set(s3._20.get());
      s3._00.set(0);
    }

    //LAB_800d40cc
    s3._0c.add(s3._2c.get());

    if(Math.abs(s3._1c.get()) < Math.abs(s3._0c.get())) {
      s3._0c.set(s3._1c.get());
    }

    //LAB_800d4108
    v0 = s3._3c.get();
    s6 = 0x1L;
    s4 = v0 + 0x70L;
    v1 = v0 + 0x74L;

    //LAB_800d4118
    do {
      MEMORY.ref(4, s4).offset(0x0L).setu(MEMORY.ref(4, v1).offset(-0x14L).get());
      MEMORY.ref(4, v1).offset(0x0L).setu(MEMORY.ref(4, v1).offset(-0x10L).get());
      s4 = s4 - 0x10L;
      v1 = v1 - 0x10L;
      s6 = s6 + 0x1L;
    } while((int)s6 < 0x8L);

    MEMORY.ref(4, s4).offset(0x0L).setu(s3._0c.get());
    MEMORY.ref(4, s4).offset(0x4L).setu(s3._10.get());
    s6 = 0;
    final String sp0x18 = Long.toString(s3._08.get());
    fp = s3._02.get();
    s4 = s3._3c.get();
    v1 = fp & 0xffL;
    v0 = s3._01.get() + 0x21L;
    v1 = v1 >>> 3;
    v0 = v0 & 0xffL;
    t0 = v0;
    sp2c = t0;
    t0 = s4 + 0x4L;
    sp20 = v1;
    sp28 = v0;

    //LAB_800d419c
    do {
      sp30 = t0;
      fp = fp - sp20;

      if(s6 == 0 || MEMORY.ref(4, s4).offset(0x0L).get() != s3._0c.get() || MEMORY.ref(4, sp30).offset(0x0L).get() != s3._10.get()) {
        //LAB_800d41d8
        s2 = (int)MEMORY.ref(4, s4).offset(0x0L).get() >> 8;
        s5 = (int)MEMORY.ref(4, sp30).offset(0x0L).get() >> 8;

        if(s3._01.get() != 0) {
          FUN_800d3f98((short)s2, (short)s5, 0xaL, (short)sp28, (byte)(fp & 0xffL));
          s2 = s2 + 0x8L;
        }

        //LAB_800d4224
        //LAB_800d423c
        for(int i = 0; i < sp0x18.length(); i++) {
          FUN_800d3f98((short)s2, (short)s5, sp0x18.charAt(i) - 0x30L, (short)sp28, (byte)(fp & 0xffL));
          s2 = s2 + 0x8L;
        }

        //LAB_800d4274
        FUN_800d3f98((short)(s2 - 0x2L), (short)s5, 0xbL, (short)sp2c, (byte)(fp & 0xffL));
        FUN_800d3f98((short)(s2 + 0x4L), (short)s5, 0xcL, (short)sp2c, (byte)(fp & 0xffL));
      }

      //LAB_800d42c0
      s6 = s6 + 0x1L;
      s4 = s4 + 0x10L;
      t0 = sp30 + 0x10L;
    } while((int)s6 < 0x8L);

    //LAB_800d42dc
  }

  @Method(0x800d430cL)
  public static void FUN_800d430c(final int index, final ScriptState<BttlScriptData40> state, final BttlScriptData40 data) {
    removeFromLinkedList(state.innerStruct_00.deref()._3c.get());
  }

  @Method(0x800d4338L)
  public static long FUN_800d4338(final RunningScript s4) {
    final int s2 = s4.params_20.get(0).deref().get();
    final int s3 = s4.params_20.get(1).deref().get();

    if(s2 == -1) {
      _800faa94.setu(0);
      s4.params_20.get(1).deref().set(0);
    } else {
      //LAB_800d4388
      final int scriptIndex = allocateScriptState(0x40L, BttlScriptData40::new);
      final ScriptState<?> state = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref();
      loadScriptFile(scriptIndex, _8004f650, "", 0); //TODO
      setCallback04(scriptIndex, MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "FUN_800d4018", int.class, ScriptState.classFor(BttlScriptData40.class), BttlScriptData40.class), TriConsumerRef::new));
      setCallback0c(scriptIndex, MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "FUN_800d430c", int.class, ScriptState.classFor(BttlScriptData40.class), BttlScriptData40.class), TriConsumerRef::new));

      final BttlScriptData40 s1 = state.innerStruct_00.derefAs(BttlScriptData40.class);
      s1._00.set(0x1);
      s1._02.set(0x80);
      s1._04.set(0);
      s1._08.set(s2);
      s1._0c.set(0);
      s1._10.set(30);
      s1._1c.set((int)(_800faa90.getSigned() << 8));
      s1._20.set(0x5000);
      s1._3c.set(addToLinkedListTail(0x80L));

      if(s3 == 1) {
        _800faa92.setu(0);
        _800faa94.setu(s3);
        _800faa98.setu(0);
        s1._01.set(0);
        s1._1c.set(0xffff6e00);
        _800faa90.setu(-0x92L);
      } else {
        //LAB_800d4470
        _800faa92.addu(0x1L);
        s1._01.set((int)_800faa92.get());
      }

      //LAB_800d448c
      s1._2c.set((s1._1c.get() - s1._0c.get()) / 14);
      s1._30.set(-0x800);
      _800faa98.addu(s2);

      //LAB_800d44dc
      long a2 = s1._3c.get();
      for(long a3 = 0; a3 < 8; a3++) {
        MEMORY.ref(4, a2).offset(0x0L).setu(s1._0c.get());
        MEMORY.ref(4, a2).offset(0x4L).setu(s1._10.get());
        a2 = a2 + 0x10L;
      }

      final long v0 = String.valueOf(s1._08.get()).length();

      final long v1;
      if(s1._01.get() == 0) {
        v1 = v0 + 0x2L;
      } else {
        v1 = v0 + 0x3L;
      }

      //LAB_800d453c
      _800faa90.setu((s1._1c.get() >> 8) + v1 * 0x8L - 0x3L);
      s4.params_20.get(1).deref().set(0);
    }

    //LAB_800d4560
    return 0;
  }

  @Method(0x800d4580L)
  public static long FUN_800d4580(final RunningScript a0) {
    final int s2 = a0.params_20.get(0).deref().get();
    if(s2 != -1) {
      final int scriptIndex = allocateScriptState(0x1cL, BttlScriptData1c::new);
      loadScriptFile(scriptIndex, _8004f650, "", 0); //TODO
      setCallback04(scriptIndex, MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "FUN_800d3bb8", int.class, ScriptState.classFor(BttlScriptData1c.class), BttlScriptData1c.class), TriConsumerRef::new));
      setCallback0c(scriptIndex, MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "FUN_800d3d48", int.class, ScriptState.classFor(BttlScriptData1c.class), BttlScriptData1c.class), TriConsumerRef::new));
      final ScriptState<?> state = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref();
      final BttlScriptData1c s0 = state.innerStruct_00.derefAs(BttlScriptData1c.class);
      s0._18.set(addToLinkedListTail(0xcL));
      _800faa9c.setu(0x1L);
      s0._0c.set(40);
      s0._14.set(MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "FUN_800d3a64", BttlScriptData1c.class, long.class, long.class, long.class), QuadConsumerRef::new));
      final long a0_0 = s0._18.get();
      s0._00.set(0);
      s0.addition_02.set(0);
      s0._04.set(0);
      s0._08.set(0x1L);
      s0._10.set(s2);
      MEMORY.ref(1, a0_0).offset(0x0L).setu(0x1L);
      MEMORY.ref(2, a0_0).offset(0x4L).setu(-0xa0L);
      MEMORY.ref(2, a0_0).offset(0x8L).setu(0x90L - (String.valueOf(s2).length() + 4) * 8);
      MEMORY.ref(2, a0_0).offset(0x6L).setu(0x60L);
      MEMORY.ref(2, a0_0).offset(0xaL).setu(0x60L);
      MEMORY.ref(2, a0_0).offset(0x2L).setu(0x8L);
    } else {
      //LAB_800d46b0
      _800faa9c.setu(0);
    }

    //LAB_800d46bc
    return 0;
  }

  @Method(0x800d46d4L)
  public static long FUN_800d46d4(final RunningScript a0) {
    final long v1 = _800faaa0.offset(a0.params_20.get(0).deref().get() * 0x6L).getAddress();

    final byte[] sp0x30 = {(byte)a0.params_20.get(4).deref().get(), (byte)a0.params_20.get(4).deref().get(), (byte)a0.params_20.get(4).deref().get()};

    if(MEMORY.ref(1, v1).offset(0x0L).get() == 0) {
      FUN_80018d60(a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), MEMORY.ref(1, v1).offset(0x1L).get(), MEMORY.ref(1, v1).offset(0x2L).get(), MEMORY.ref(1, v1).offset(0x3L).get(), MEMORY.ref(1, v1).offset(0x4L).get(), MEMORY.ref(1, v1).offset(0x5L).get(), a0.params_20.get(3).deref().get(), sp0x30, 0x1000L);
    } else {
      //LAB_800d4784
      FUN_80018a5c(a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), MEMORY.ref(1, v1).offset(0x1L).get(), MEMORY.ref(1, v1).offset(0x2L).get(), MEMORY.ref(1, v1).offset(0x3L).get(), MEMORY.ref(1, v1).offset(0x4L).get(), MEMORY.ref(1, v1).offset(0x5L).get(), a0.params_20.get(3).deref().get(), sp0x30, 0x1000L, 0x1000L);
    }

    //LAB_800d47cc
    return 0;
  }

  @Method(0x800d47dcL)
  public static void FUN_800d47dc(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.vec_94.set(cam.rview2_00.viewpoint_00);

    if(a5 == 0) {
      //LAB_800d4854
      cam._d0.set(a3);
      cam._b0.set((a0 - cam.vec_94.getX()) / a3);
      cam._bc.set((a1 - cam.vec_94.getY()) / a3);
      cam._c8.set((a2 - cam.vec_94.getZ()) / a3);
    } else assert a5 != 0x1L : "Undefined t0/t1";

    //LAB_800d492c
    //LAB_800d4934
    cam._11c.or(0x1L);
    cam._120.set(8);
  }

  @Method(0x800d4bacL)
  public static void FUN_800d4bac(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.vec_94.setX(FUN_800dc384(0, 0x4L, 0, 0) << 8);
    cam.vec_94.setY(FUN_800dc384(0, 0x4L, 1, 0) << 8);
    cam.vec_94.setZ(FUN_800dc384(0, 0x4L, 2, 0) << 8);

    if(a5 == 0) {
      //LAB_800d4c5c
      cam._d0.set(a3);
      cam._b0.set((a0 - cam.vec_94.getX()) / a3);
      cam._bc.set((a1 - cam.vec_94.getY()) / a3);
      cam._c8.set((a2 - cam.vec_94.getZ()) / a3);
    } else assert a5 != 0x1L : "Undefined s3/s5";

    //LAB_800d4d34
    //LAB_800d4d3c
    cam._11c.or(0x1L);
    cam._120.set(12);
  }

  @Method(0x800d4d7cL)
  public static void FUN_800d4d7c(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;

    cam._ac.set(FUN_800dc384(0, 0x5L, 0, 0) << 8);
    cam._b8.set(FUN_800dc384(0, 0x5L, 1, 0) << 8);
    cam._a0.set(FUN_800dc384(0, 0x5L, 2, 0) << 8);

    if(a5 == 0) {
      //LAB_800d4e34
      cam._d0.set(a3);
      cam._b0.set(FUN_800dcf10(0, cam._ac.get(), a0, a3, a4 & 0x3));
      cam._bc.set(FUN_800dcf10(1, cam._b8.get(), a1, a3, a4 >> 2 & 0x3));
      cam._c8.set(FUN_800dcf10(2, cam._a0.get(), a2, a3, 0));
    } else if(a5 == 1) {
      //LAB_800d4e8c
      final int s1 = FUN_800dcfb8(0, cam._ac.get(), a0, a4 & 0x3);
      final int s0 = FUN_800dcfb8(1, cam._b8.get(), a1, a4 >> 2 & 0x3);
      final int v0 = FUN_800dcfb8(2, cam._a0.get(), a2, 0);
      cam._d0.set(SquareRoot0(s1 * s1 + s0 * s0 + v0 * v0) / a3);
      cam._b0.set(FUN_800dcf10(0, cam._ac.get(), a0, cam._d0.get(), a4 & 0x3));
      cam._bc.set(FUN_800dcf10(1, cam._b8.get(), a1, cam._d0.get(), a4 >> 2 & 0x3));
      cam._c8.set(FUN_800dcf10(2, cam._a0.get(), a2, cam._d0.get(), 0));
    }

    //LAB_800d4f78
    cam._11c.or(0x1L);
    cam._120.set(13);
  }

  @Method(0x800d4fbcL)
  public static void FUN_800d4fbc(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.bobjIndex_f4.set(scriptIndex);
    cam.vec_94.setX(FUN_800dc384(0, 0x6L, 0, scriptIndex) << 8);
    cam.vec_94.setY(FUN_800dc384(0, 0x6L, 1, scriptIndex) << 8);
    cam.vec_94.setZ(FUN_800dc384(0, 0x6L, 2, scriptIndex) << 8);

    if(a5 == 0) {
      //LAB_800d5078
      cam._d0.set(a3);
      cam._b0.set((a0 - cam.vec_94.getX()) / a3);
      cam._bc.set((a1 - cam.vec_94.getY()) / a3);
      cam._c8.set((a2 - cam.vec_94.getZ()) / a3);
    } else if(a5 == 1) {
      //LAB_800d50c4
      int s6 = a0 - cam.vec_94.getX();
      int s5 = a1 - cam.vec_94.getY();
      int v0 = a2 - cam.vec_94.getZ();
      v0 = SquareRoot0(v0 * v0 + s5 * s5 + s6 * s6) / a3;
      cam._d0.set(v0);
      cam._b0.set((a0 - cam.vec_94.getX()) / v0);
      cam._bc.set((a1 - cam.vec_94.getY()) / v0);
      cam._c8.set((a2 - cam.vec_94.getZ()) / v0);
    }

    //LAB_800d5150
    //LAB_800d5158
    cam._11c.or(0x1L);
    cam._120.set(14);
  }

  @Method(0x800d5ec8L)
  public static void FUN_800d5ec8(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    final int s3 = (a0 >> 8) - cam.rview2_00.viewpoint_00.getX();
    final int s4 = (a1 >> 8) - cam.rview2_00.viewpoint_00.getY();
    final int s1 = (a2 >> 8) - cam.rview2_00.viewpoint_00.getZ();
    final int s2 = s3 / 2;
    final int v0 = s4 / 2;
    final int s0 = s1 / 2;
    cam._d0.set(a3);
    cam._d4.set((ratan2(s1, s3) & 0xfff) << 8);
    cam._d8.set((ratan2(s4, SquareRoot0(s2 * s2 + s0 * s0) * 2) & 0xfff) << 8);
    cam._dc.set(SquareRoot0(s2 * s2 + v0 * v0 + s0 * s0) << 9);

    final int s6;
    final int s7;
    if(a4 == 0) {
      //LAB_800d5ff0
      s6 = a5;
      s7 = cam._dc.get() * 2 / a3 - a5;
    } else if(a4 == 0x1L) {
      //LAB_800d6010
      s6 = cam._dc.get() * 2 / a3 - a5;
      s7 = a5;
    } else {
      throw new RuntimeException("Undefined s6/s7");
    }

    //LAB_800d6030
    //LAB_800d6038
    cam._a4.set(s6);
    cam._b4.set((s7 - s6) / cam._d0.get());
    cam._e8.set(a0);
    cam._ec.set(a1);
    cam._f0.set(a2);
    cam._11c.or(0x1L);
    cam._120.set(16);
  }

  @Method(0x800d62d8L)
  public static void FUN_800d62d8(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.vec_94.setX(FUN_800dc384(0, 0x4L, 0, 0) << 8);
    cam.vec_94.setY(FUN_800dc384(0, 0x4L, 1, 0) << 8);
    cam.vec_94.setZ(FUN_800dc384(0, 0x4L, 2, 0) << 8);
    _800c67d8.set(cam.vec_94);
    final int s4 = a0 - cam.vec_94.getX() >> 8;
    final int s5 = a1 - cam.vec_94.getY() >> 8;
    final int s3 = a2 - cam.vec_94.getZ() >> 8;
    final int s1 = s4 / 2;
    final int v1 = s5 / 2;
    final int s0 = s3 / 2;
    cam._d0.set(a3);
    cam._d4.set((ratan2(s3, s4) & 0xfff) * 0x100);
    cam._d8.set((ratan2(s5, SquareRoot0(s1 * s1 + s0 * s0) * 2) & 0xfff) << 8);
    cam._dc.set(SquareRoot0(s1 * s1 + v1 * v1 + s0 * s0) * 0x200);
    final Ref<Long> sp0x18 = new Ref<>();
    final Ref<Long> sp0x1c = new Ref<>();
    FUN_800dcebc(a4, a5, cam._dc.get(), a3, sp0x18, sp0x1c);
    cam._e8.set(a0);
    cam._ec.set(a1);
    cam._f0.set(a2);
    cam._a4.set(sp0x18.get().intValue());
    cam._b4.set((int)(sp0x1c.get() - sp0x18.get()) / cam._d0.get());
    cam._11c.or(0x1L);
    cam._120.set(20);
  }

  @Method(0x800d64e4L)
  public static void FUN_800d64e4(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam._ac.set(FUN_800dc384(0, 0x5L, 0, 0) << 8);
    cam._b8.set(FUN_800dc384(0, 0x5L, 1, 0) << 8);
    cam._a0.set(FUN_800dc384(0, 0x5L, 2, 0) << 8);
    final int s1 = FUN_800dcfb8(0, cam._ac.get(), a0, a6 & 0x3) >> 8;
    final int s0 = FUN_800dcfb8(1, cam._b8.get(), a1, a6 >> 2 & 0x3) >> 8;
    FUN_800dcfb8(2, cam._a0.get(), a2, 0);
    cam._dc.set(SquareRoot0(s1 * s1 + s0 * s0) << 8);
    cam._d4.set((ratan2(s0, s1) & 0xfff) << 8);
    cam._d8.set(0);
    cam._d0.set(a3);

    final int s3;
    final int s4;
    if(a4 == 0) {
      //LAB_800d6630
      s3 = a5;
      s4 = cam._dc.get() * 2 / a3 - a5;
    } else if(a4 == 0x1L) {
      //LAB_800d6650
      s3 = cam._dc.get() * 2 / a3 - a5;
      s4 = a5;
    } else {
      throw new RuntimeException("Undefined s3");
    }

    //LAB_800d666c
    //LAB_800d6674
    cam._e0.set(s3);
    cam._e8.set(a0);
    cam._ec.set(a1);
    cam._f0.set(a2);
    cam._e4.set((s4 - s3) / cam._d0.get());
    cam._a4.set(FUN_800dcfb8(2, cam._a0.get(), a2, 0) / cam._d0.get());
    cam._11c.or(0x1L);
    cam._120.set(21);
  }

  @Method(0x800d670cL)
  public static void FUN_800d670c(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.bobjIndex_f4.set(scriptIndex);
    cam.vec_94.setX(FUN_800dc384(0, 0x6L, 0, scriptIndex) << 8);
    cam.vec_94.setY(FUN_800dc384(0, 0x6L, 1, scriptIndex) << 8);
    cam.vec_94.setZ(FUN_800dc384(0, 0x6L, 2, scriptIndex) << 8);
    final int s3 = a0 - cam.vec_94.getX() >> 8;
    final int s4 = a1 - cam.vec_94.getY() >> 8;
    final int s2 = a2 - cam.vec_94.getZ() >> 8;
    final int s1 = s3 / 2;
    final int v1 = s4 / 2;
    final int s0 = s2 / 2;
    cam._d0.set(a3);
    cam._d4.set((ratan2(s2, s3) & 0xfff) * 0x100);
    cam._d8.set((ratan2(s4, SquareRoot0(s1 * s1 + s0 * s0) * 2) & 0xfff) * 0x100);
    cam._dc.set(SquareRoot0(s1 * s1 + v1 * v1 + s0 * s0) * 0x200);

    final int s6;
    final int s7;
    if(a4 == 0) {
      //LAB_800d68a0
      s6 = a5;
      s7 = (cam._dc.get() * 2) / a3 - a5;
    } else if(a4 == 0x1L) {
      //LAB_800d68c0
      s6 = (cam._dc.get() * 2) / a3 - a5;
      s7 = a5;
    } else {
      throw new RuntimeException("Undefined s6/s7");
    }

    //LAB_800d68e0
    //LAB_800d68e8
    cam._a4.set(s6);
    cam._e8.set(a0);
    cam._ec.set(a1);
    cam._f0.set(a2);
    cam._b4.set((s7 - s6) / cam._d0.get());
    cam._11c.or(0x1L);
    cam._120.set(22);
  }

  @Method(0x800d6960L)
  public static void FUN_800d6960(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.bobjIndex_f4.set(scriptIndex);
    cam._ac.set(FUN_800dc384(0, 0x7L, 0, scriptIndex) << 8);
    cam._b8.set(FUN_800dc384(0, 0x7L, 1, scriptIndex) << 8);
    cam._a0.set(FUN_800dc384(0, 0x7L, 2, scriptIndex) << 8);
    final int s1 = FUN_800dcfb8(0, cam._ac.get(), a0, a6 & 0x3) >> 8;
    final int s0 = FUN_800dcfb8(1, cam._b8.get(), a1, a6 >> 2 & 0x3) >> 8;
    FUN_800dcfb8(2, cam._a0.get(), a2, 0); //TODO this method just returns a value, should it be used in the same way as the two calls above?
    cam._d0.set(a3);
    cam._d4.set(ratan2(s0, s1) & 0xfff << 8);
    cam._d8.set(0);
    cam._dc.set(SquareRoot0(s1 * s1 + s0 * s0) << 8);
    final int s3;
    final int s4;
    if(a4 == 0) {
      //LAB_800d6ab4
      s3 = a5;
      s4 = cam._dc.get() * 2 / a3 - a5;
    } else if(a4 == 1) {
      //LAB_800d6ad4
      s3 = cam._dc.get() * 2 / a3 - a5;
      s4 = a5;
    } else {
      throw new RuntimeException("s3/s4 undefined");
    }

    //LAB_800d6af0
    //LAB_800d6af8
    cam._e0.set(s3);
    cam._e8.set(a0);
    cam._ec.set(a1);
    cam._f0.set(a2);
    cam._e4.set((s4 - s3) / cam._d0.get());
    cam._a4.set(FUN_800dcfb8(2, cam._a0.get(), a2, 0) / cam._d0.get());
    cam._11c.or(0x1L);
    cam._120.set(23);
  }

  @Method(0x800d6b90L)
  public static void FUN_800d6b90(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.vec_20.set(cam.rview2_00.refpoint_0c);

    if(a5 == 0) {
      //LAB_800d6c04
      cam._5c.set(a3);
      cam._3c.set((a0 - cam.vec_20.getX()) / a3);
      cam._48.set((a1 - cam.vec_20.getY()) / a3);
      cam._54.set((a2 - cam.vec_20.getZ()) / a3);
    } else if(a5 == 0x1L) {
      //LAB_800d6c44
      //TODO undefined t0/t1
/*
      v0 = s2 - a1;
      lo = ((long)(int)v0 * (int)v0) & 0xffff_ffffL;
      v1 = lo;
      lo = ((long)(int)t0 * (int)t0) & 0xffff_ffffL;
      t0 = lo;
      lo = ((long)(int)t1 * (int)t1) & 0xffff_ffffL;
      a0 = v1 + t0;
      v0 = lo;
      a0 = a0 + v0;
      v0 = SquareRoot0(a0);
      lo = (int)v0 / (int)s1;
      v0 = lo;
      a1 = MEMORY.ref(4, s0).offset(0xcL).get();
      a1 = a1 << 8;
      a1 = s3 - a1;
      lo = (int)a1 / (int)v0;
      a1 = lo;
      a0 = MEMORY.ref(4, s0).offset(0x10L).get();
      a0 = a0 << 8;
      a0 = s4 - a0;
      lo = (int)a0 / (int)v0;
      a0 = lo;
      v1 = MEMORY.ref(4, s0).offset(0x14L).get();
      v1 = v1 << 8;
      v1 = s2 - v1;
      lo = (int)v1 / (int)v0;
      v1 = lo;
      MEMORY.ref(4, s0).offset(0x5cL).setu(v0);
      MEMORY.ref(4, s0).offset(0x3cL).setu(a1);
      MEMORY.ref(4, s0).offset(0x48L).setu(a0);
      MEMORY.ref(4, s0).offset(0x54L).setu(v1);
*/
    }

    //LAB_800d6cdc
    //LAB_800d6ce4
    cam._11c.or(0x2L);
    cam._121.set(8);
  }

  @Method(0x800d7368L)
  public static void FUN_800d7368(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.bobjIndex_80.set(scriptIndex);
    cam.vec_20.setX(FUN_800dc384(0x1L, 0x6L, 0, scriptIndex) << 8);
    cam.vec_20.setY(FUN_800dc384(0x1L, 0x6L, 1, scriptIndex) << 8);
    cam.vec_20.setZ(FUN_800dc384(0x1L, 0x6L, 2, scriptIndex) << 8);

    if(a5 == 0) {
      //LAB_800d7424
      cam._5c.set(a3);
      cam._3c.set((a0 - cam.vec_20.getX()) / a3);
      cam._48.set((a1 - cam.vec_20.getY()) / a3);
      cam._54.set((a2 - cam.vec_20.getZ()) / a3);
    } else if(a5 == 0x1L) {
      assert false : "Undefined s5/s6";
      final int s5 = 0;
      final int s6 = 0;
      //LAB_800d7470
      int v0 = a2 - cam.vec_20.getZ();
      v0 = SquareRoot0(v0 * v0) / a3;
      cam._5c.set(v0);
      cam._3c.set((a0 - cam.vec_20.getX()) / v0);
      cam._48.set((a1 - cam.vec_20.getY()) / v0);
      cam._54.set((a2 - cam.vec_20.getZ()) / v0);
    }

    //LAB_800d74fc
    //LAB_800d7504
    cam._11c.or(0x2L);
    cam._121.set(14);
  }

  @Method(0x800d8274L)
  public static void FUN_800d8274(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    final int s4 = (a0 >> 8) - cam.rview2_00.refpoint_0c.getX();
    final int s5 = (a1 >> 8) - cam.rview2_00.refpoint_0c.getY();
    final int s2 = (a2 >> 8) - cam.rview2_00.refpoint_0c.getZ();
    final int s3 = s4 / 2;
    final int v0 = s5 / 2;
    final int s0 = s2 / 2;
    cam._5c.set(a3);
    cam.vec_60.setX((ratan2(s2, s4) & 0xfff) << 8);
    cam.vec_60.setY((ratan2(s5, SquareRoot0(s3 * s3 + s0 * s0) * 2) & 0xfff) << 8);
    cam.vec_60.setZ(SquareRoot0(s3 * s3 + v0 * v0 + s0 * s0) << 9);
    final Ref<Long> sp0x18 = new Ref<>();
    final Ref<Long> sp0x1c = new Ref<>();
    FUN_800dcebc(a4, a5, cam.vec_60.getZ(), cam._5c.get(), sp0x18, sp0x1c);
    cam._30.set(sp0x18.get().intValue());
    cam._40.set((sp0x1c.get().intValue() - sp0x18.get().intValue()) / cam._5c.get());
    cam.vec_74.setX(a0);
    cam.vec_74.setY(a1);
    cam.vec_74.setZ(a2);
    cam._11c.or(0x2L);
    cam._121.set(16);
  }

  @Method(0x800d89f8L)
  public static void FUN_800d89f8(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.bobjIndex_80.set(scriptIndex);
    cam.vec_20.setX(FUN_800dc384(0x1L, 0x6L, 0, scriptIndex) << 8);
    cam.vec_20.setY(FUN_800dc384(0x1L, 0x6L, 1, scriptIndex) << 8);
    cam.vec_20.setZ(FUN_800dc384(0x1L, 0x6L, 2, scriptIndex) << 8);
    final int s4 = a0 - cam.vec_20.getX() >> 8;
    final int s5 = a1 - cam.vec_20.getY() >> 8;
    final int s3 = a2 - cam.vec_20.getZ() >> 8;
    final int s1 = s4 / 2;
    final int v1 = s5 / 2;
    final int s0 = s3 / 2;
    cam._5c.set(a3);
    cam.vec_60.setX((ratan2(s3, s4) & 0xfff) << 8);
    cam.vec_60.setY((ratan2(s5, SquareRoot0(s1 * s1 + s0 * s0) * 2) & 0xfff) << 8);
    cam.vec_60.setZ(SquareRoot0(s1 * s1 + v1 * v1 + s0 * s0) << 9);
    final Ref<Long> sp0x18 = new Ref<>();
    final Ref<Long> sp0x1c = new Ref<>();
    FUN_800dcebc(a4, a5, cam.vec_60.getZ(), a3, sp0x18, sp0x1c);
    cam.vec_74.setX(a0);
    cam.vec_74.setY(a1);
    cam.vec_74.setZ(a2);
    cam._30.set(sp0x18.get().intValue());
    cam._40.set((sp0x1c.get().intValue() - sp0x18.get().intValue()) / cam._5c.get());
    cam._11c.or(0x2L);
    cam._121.set(22);
  }

  @Method(0x800d8decL)
  public static long FUN_800d8dec(final RunningScript a0) {
    final int s3 = a0.params_20.get(0).deref().get();
    final int s0 = a0.params_20.get(1).deref().get();
    final int s1 = a0.params_20.get(2).deref().get();
    final int s4 = a0.params_20.get(3).deref().get();

    final BattleCamera cam = camera_800c67f0;
    cam._100.set(getProjectionPlaneDistance() << 16);
    cam._104.set(s0 << 16);
    cam._118.set(1);

    if(s0 < getProjectionPlaneDistance()) {
      //LAB_800d8e64
      cam._114.set(1);
    } else {
      cam._114.set(0);
    }

    //LAB_800d8e68
    final int a2 = Math.abs(s0 - getProjectionPlaneDistance()) << 16;
    cam._108.set(s1);
    if(s3 == 0) {
      cam._10c.set(a2 / s1);
      cam._110.set(0);
    } else {
      //LAB_800d8ea0
      final Ref<Long> sp0x18 = new Ref<>();
      final Ref<Long> sp0x1c = new Ref<>();
      FUN_800dcebc(s3 - 0x1L, s4 << 8, a2, s1, sp0x18, sp0x1c);
      cam._10c.set(sp0x18.get().intValue());
      cam._110.set((sp0x1c.get().intValue() - sp0x18.get().intValue()) / s1);
    }

    //LAB_800d8eec
    return 0;
  }

  @Method(0x800d8f10L)
  public static void FUN_800d8f10() {
    final BattleCamera cam = camera_800c67f0;

    if(cam._11c.get() != 0) {
      if((cam._11c.get() & 0x1L) != 0) {
        _800facbc.offset(cam._120.get() * 0x4L).deref(4).call();
      }

      //LAB_800d8f80
      if((cam._11c.get() & 0x2L) != 0) {
        _800fad1c.offset(cam._121.get() * 0x4L).deref(4).call();
      }
    }

    //LAB_800d8fb4
    GsSetRefView2(camera_800c67f0.rview2_00);
    FUN_800daa80();
    FUN_800d8fe0();
  }

  @Method(0x800d8fe0L)
  public static void FUN_800d8fe0() {
    final BattleCamera cam = camera_800c67f0;

    if(cam._118.get() != 0 && cam._108.get() == 0) {
      setProjectionPlaneDistance(cam._100.get() >> 16);
      cam._118.set(0);
    }

    //LAB_800d9028
    if(cam._118.get() != 0) {
      if(cam._108.get() != 0) {
        if(cam._114.get() == 0) {
          cam._100.add(cam._10c.get());
        } else {
          //LAB_800d906c
          cam._100.sub(cam._10c.get());
        }

        //LAB_800d907c
        cam._10c.add(cam._110.get());
        setProjectionPlaneDistance(cam._100.get() >> 16);

        cam._108.decr();
        if(cam._108.get() == 0) {
          cam._118.set(0);
        }
      }
    }

    //LAB_800d90b8
  }

  @Method(0x800d90c8L)
  public static void FUN_800d90c8() {
    final BattleCamera cam = camera_800c67f0;
    cam.vec_94.x.add(cam._b0.get());
    cam.vec_94.y.add(cam._bc.get());
    cam.vec_94.z.add(cam._c8.get());
    setViewpoint(cam.vec_94.getX() >> 8, cam.vec_94.getY() >> 8, cam.vec_94.getZ() >> 8);

    cam._d0.decr();
    if(cam._d0.get() <= 0) {
      cam._11c.and(0xffff_fffeL);
      cam._122.set(0);
    }

    //LAB_800d9144
  }

  @Method(0x800d9220L)
  public static void FUN_800d9220() {
    final BattleCamera cam = camera_800c67f0;
    cam.vec_94.x.add(cam._b0.get());
    cam.vec_94.y.add(cam._bc.get());
    cam.vec_94.z.add(cam._c8.get());

    setViewpoint(
      cam.rview2_00.refpoint_0c.getX() + (cam.vec_94.getX() >> 8),
      cam.rview2_00.refpoint_0c.getY() + (cam.vec_94.getY() >> 8),
      cam.rview2_00.refpoint_0c.getZ() + (cam.vec_94.getZ() >> 8)
    );

    cam._d0.decr();
    if(cam._d0.get() <= 0) {
      cam._120.set(4);
      cam._122.set(0);
    }

    //LAB_800d92ac
  }

  @Method(0x800d92bcL)
  public static void FUN_800d92bc() {
    final BattleCamera cam = camera_800c67f0;
    cam._a0.add(cam._c8.get());
    cam._ac.add(cam._b0.get());
    cam._b8.add(cam._bc.get());

    final IntRef refX = new IntRef().set(cam._ac.get() >> 8);
    final IntRef refY = new IntRef().set(cam._b8.get() >> 8);
    final IntRef refZ = new IntRef().set(cam._a0.get() >> 8);
    FUN_800dcc94(cam.rview2_00.refpoint_0c.getX(), cam.rview2_00.refpoint_0c.getY(), cam.rview2_00.refpoint_0c.getZ(), refX, refY, refZ);
    setViewpoint(refX.get(), refY.get(), refZ.get());

    cam._d0.decr();
    if(cam._d0.get() <= 0) {
      cam._120.set(5);
      cam._122.set(0);
    }

    //LAB_800d9370
  }

  @Method(0x800d9380L)
  public static void FUN_800d9380() {
    final BattleCamera cam = camera_800c67f0;

    cam.vec_94.x.add(cam._b0.get());
    cam.vec_94.y.add(cam._bc.get());
    cam.vec_94.z.add(cam._c8.get());

    final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(cam.bobjIndex_f4.get()).deref().innerStruct_00.derefAs(BattleObject27c.class);

    setViewpoint(
      bobj._148.coord2_14.coord.transfer.getX() + (cam.vec_94.getX() >> 8),
      bobj._148.coord2_14.coord.transfer.getY() + (cam.vec_94.getY() >> 8),
      bobj._148.coord2_14.coord.transfer.getZ() + (cam.vec_94.getZ() >> 8)
    );

    cam._d0.decr();
    if(cam._d0.get() <= 0) {
      cam._120.set(6);
      cam._122.set(0);
    }

    //LAB_800d9428
  }

  @Method(0x800d9518L)
  public static void FUN_800d9518() {
    final BattleCamera cam = camera_800c67f0;
    _800fab98.setX((short)(cam._d4.get() >> 8));
    _800fab98.setY((short)(cam._d8.get() >> 8));
    _800fab98.setZ((short)0);
    RotMatrix_8003faf0(_800fab98, _800c6798);
    SetRotMatrix(_800c6798);
    SetTransMatrix(_800c6798);
    cam._a4.add(cam._b4.get());
    cam._dc.sub(cam._a4.get());
    _800faba0.setX((short)0);
    _800faba0.setY((short)0);
    _800faba0.setZ((short)(cam._dc.get() >> 8));
    FUN_8003f990(_800faba0, _800faba8, _800c67b8);
    cam.vec_94.setX(cam._e8.get() - (_800faba8.getZ() << 8));
    cam.vec_94.setY(cam._ec.get() - (_800faba8.getX() << 8));
    cam.vec_94.setZ(cam._f0.get() + (_800faba8.getY() << 8));

    setViewpoint(cam.vec_94.getX() >> 8, cam.vec_94.getY() >> 8, cam.vec_94.getZ() >> 8);

    cam._d0.decr();
    if(cam._d0.get() <= 0) {
      cam._11c.and(0xffff_fffeL);
      cam._122.set(0);
    }

    //LAB_800d9638
  }

  @Method(0x800d9788L)
  public static void FUN_800d9788() {
    final BattleCamera cam = camera_800c67f0;
    _800fab98.setX((short)(cam._d4.get() >> 8));
    _800fab98.setY((short)(cam._d8.get() >> 8));
    _800fab98.setZ((short)0);
    RotMatrix_8003faf0(_800fab98, _800c6798);
    SetRotMatrix(_800c6798);
    SetTransMatrix(_800c6798);
    _800faba0.setX((short)0);
    _800faba0.setY((short)0);
    cam._a4.add(cam._b4.get());
    cam._dc.sub(cam._a4.get());
    _800faba0.setZ((short)(cam._dc.get() >> 8));
    FUN_8003f990(_800faba0, _800faba8, _800c67b8);

    cam.vec_94.setX(cam._e8.get() - (_800faba8.getZ() << 8));
    cam.vec_94.setY(cam._ec.get() - (_800faba8.getX() << 8));
    cam.vec_94.setZ(cam._f0.get() + (_800faba8.getY() << 8));

    setViewpoint(
      cam.rview2_00.refpoint_0c.getX() + (cam.vec_94.getX() >> 8),
      cam.rview2_00.refpoint_0c.getY() + (cam.vec_94.getY() >> 8),
      cam.rview2_00.refpoint_0c.getZ() + (cam.vec_94.getZ() >> 8)
    );

    cam._d0.decr();
    if(cam._d0.get() <= 0) {
      cam._120.set(4);
      cam._122.set(0);
    }

    //LAB_800d98b8
  }

  @Method(0x800d98d0L)
  public static void FUN_800d98d0() {
    final BattleCamera cam = camera_800c67f0;
    final IntRef refX = new IntRef().set(cam._d4.get() >> 8);
    final IntRef refY = new IntRef().set(cam._d8.get() >> 8);
    final IntRef refZ = new IntRef().set(cam._dc.get() >> 8);
    FUN_800dcc94(0, 0, 0, refX, refY, refZ);
    cam._a0.add(cam._a4.get());
    refX.add(cam._e8.get() >> 8);
    refY.add(cam._ec.get() >> 8);
    refZ.set(cam._a0.get() >> 8);
    FUN_800dcc94(0, 0, 0, refX, refY, refZ);
    setViewpoint(cam.rview2_00.refpoint_0c.getX() + refX.get(), cam.rview2_00.refpoint_0c.getY() + refY.get(), cam.rview2_00.refpoint_0c.getZ() + refZ.get());
    cam._e0.add(cam._e4.get());
    cam._dc.sub(cam._e0.get());

    cam._d0.decr();
    if(cam._d0.get() <= 0) {
      cam._ac.set(FUN_800dc384(0, 0x5L, 0, 0) << 8);
      cam._b8.set(FUN_800dc384(0, 0x5L, 1, 0) << 8);
      cam._a0.set(FUN_800dc384(0, 0x5L, 2, 0) << 8);
      cam._120.set(5);
      cam._122.set(0);
    }

    //LAB_800d9a4c
  }

  @Method(0x800d9a68L)
  public static void FUN_800d9a68() {
    final BattleCamera cam = camera_800c67f0;
    final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(cam.bobjIndex_f4.get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    _800fab98.setX((short)(cam._d4.get() >> 8));
    _800fab98.setY((short)(cam._d8.get() >> 8));
    _800fab98.setZ((short)0);
    RotMatrix_8003faf0(_800fab98, _800c6798);
    SetRotMatrix(_800c6798);
    SetTransMatrix(_800c6798);
    _800faba0.setX((short)0);
    _800faba0.setY((short)0);
    cam._a4.add(cam._b4.get());
    cam._dc.sub(cam._a4.get());
    _800faba0.setZ((short)(cam._dc.get() >> 8));
    FUN_8003f990(_800faba0, _800faba8, _800c67b8);
    cam.vec_94.setX(cam._e8.get() - (_800faba8.getZ() << 8));
    cam.vec_94.setY(cam._ec.get() - (_800faba8.getX() << 8));
    cam.vec_94.setZ(cam._f0.get() + (_800faba8.getY() << 8));
    setViewpoint(bobj._148.coord2_14.coord.transfer.getX() + (cam.vec_94.getX() >> 8), bobj._148.coord2_14.coord.transfer.getY() + (cam.vec_94.getY() >> 8), bobj._148.coord2_14.coord.transfer.getZ() + (cam.vec_94.getZ() >> 8));

    cam._d0.decr();
    if(cam._d0.get() <= 0) {
      cam._120.set(6);
      cam._122.set(0);
    }

    //LAB_800d9bb8
  }

  /** TODO I might have messed this up */
  @Method(0x800d9bd4L)
  public static void FUN_800d9bd4() {
    final BattleCamera cam = camera_800c67f0;
    final IntRef refX = new IntRef().set(cam._d4.get() >> 8);
    final IntRef refY = new IntRef().set(cam._d8.get() >> 8);
    final IntRef refZ = new IntRef().set(cam._dc.get() >> 8);
    FUN_800dcc94(0, 0, 0, refX, refY, refZ);
    cam._a0.add(cam._a4.get());
    refX.add(cam._e8.get() >> 8);
    refY.add(cam._ec.get() >> 8);
    refZ.set(cam._a0.get() >> 8);
    FUN_800dcc94(0, 0, 0, refX, refY, refZ);
    final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(cam.bobjIndex_f4.get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    setViewpoint(bobj._148.coord2_14.coord.transfer.getX() + refX.get(), bobj._148.coord2_14.coord.transfer.getY() + refY.get(), bobj._148.coord2_14.coord.transfer.getZ() + refZ.get());
    cam._e0.add(cam._e4.get());
    cam._dc.sub(cam._e0.get());

    cam._d0.decr();
    if(cam._d0.get() <= 0) {
      refX.set(cam.rview2_00.viewpoint_00.getX());
      refY.set(cam.rview2_00.viewpoint_00.getY());
      refZ.set(cam.rview2_00.viewpoint_00.getZ());
      FUN_800dcd9c(bobj._148.coord2_14.coord.transfer.getX(), bobj._148.coord2_14.coord.transfer.getY(), bobj._148.coord2_14.coord.transfer.getZ(), refX, refY, refZ);
      cam._ac.set(refX.get() << 8);
      cam._b8.set(refY.get() << 8);
      cam._a0.set(refZ.get() << 8);
      cam._120.set(7);
      cam._122.set(0);
    }

    //LAB_800d9d7c
  }

  @Method(0x800d9da0L)
  public static void FUN_800d9da0() {
    final BattleCamera cam = camera_800c67f0;
    cam.vec_20.x.add(cam._3c.get());
    cam.vec_20.y.add(cam._48.get());
    cam.vec_20.z.add(cam._54.get());
    setRefpoint(cam.vec_20.getX() >> 8, cam.vec_20.getY() >> 8, cam.vec_20.getZ() >> 8);

    cam._5c.decr();
    if(cam._5c.get() <= 0) {
      cam._11c.and(0xffff_fffdL);
      cam._123.set(0);
    }

    //LAB_800d9e1c
  }

  @Method(0x800da058L)
  public static void FUN_800da058() {
    final BattleCamera cam = camera_800c67f0;
    cam.vec_20.x.add(cam._3c.get());
    cam.vec_20.y.add(cam._48.get());
    cam.vec_20.z.add(cam._54.get());

    final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(cam.bobjIndex_80.get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    setRefpoint(bobj._148.coord2_14.coord.transfer.getX() + (cam.vec_20.getX() >> 8), bobj._148.coord2_14.coord.transfer.getY() + (cam.vec_20.getY() >> 8), bobj._148.coord2_14.coord.transfer.getY() + (cam.vec_20.getZ() >> 8));

    cam._5c.decr();
    if(cam._5c.get() <= 0) {
      cam._121.set(6);
      cam._123.set(0);
    }

    //LAB_800da100
  }

  @Method(0x800da1f0L)
  public static void FUN_800da1f0() {
    final BattleCamera cam = camera_800c67f0;
    _800fab98.setX((short)(cam.vec_60.getX() >> 8));
    _800fab98.setY((short)(cam.vec_60.getY() >> 8));
    _800fab98.setZ((short)0);
    RotMatrix_8003faf0(_800fab98, _800c6798);
    SetRotMatrix(_800c6798);
    SetTransMatrix(_800c6798);
    cam._30.add(cam._40.get());
    cam.vec_60.z.sub(cam._30.get());
    _800faba0.setX((short)0);
    _800faba0.setY((short)0);
    _800faba0.setZ((short)(cam.vec_60.getZ() >> 8));
    FUN_8003f990(_800faba0, _800faba8, _800c67b8);
    cam.vec_20.setX(cam.vec_74.getX() - (_800faba8.getZ() << 8));
    cam.vec_20.setY(cam.vec_74.getY() - (_800faba8.getX() << 8));
    cam.vec_20.setZ(cam.vec_74.getZ() + (_800faba8.getY() << 8));
    setRefpoint(cam.vec_20.getX() >> 8, cam.vec_20.getY() >> 8, cam.vec_20.getZ() >> 8);

    cam._5c.decr();
    if(cam._5c.get() <= 0) {
      cam._11c.and(0xffff_fffdL);
      cam._123.set(0);
    }

    //LAB_800da310
  }

  @Method(0x800da750L)
  public static void FUN_800da750() {
    final BattleCamera cam = camera_800c67f0;
    final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(cam.bobjIndex_80.get()).deref().innerStruct_00.derefAs(BattleObject27c.class);

    cam._30.add(cam._40.get());
    cam.vec_60.z.sub(cam._30.get());

    _800fab98.setX((short)(cam.vec_60.getX() >> 8));
    _800fab98.setY((short)(cam.vec_60.getY() >> 8));
    _800fab98.setZ((short)0);

    RotMatrix_8003faf0(_800fab98, _800c6798);
    SetRotMatrix(_800c6798);
    SetTransMatrix(_800c6798);

    _800faba0.setX((short)0);
    _800faba0.setY((short)0);
    _800faba0.setZ((short)(cam.vec_60.getZ() >> 8));

    FUN_8003f990(_800faba0, _800faba8, _800c67b8);

    cam.vec_20.setX(cam.vec_74.getX() - (_800faba8.getZ() << 8));
    cam.vec_20.setY(cam.vec_74.getY() - (_800faba8.getX() << 8));
    cam.vec_20.setZ(cam.vec_74.getZ() + (_800faba8.getY() << 8));

    setRefpoint(
      bobj._148.coord2_14.coord.transfer.getX() + (cam.vec_20.getX() >> 8),
      bobj._148.coord2_14.coord.transfer.getY() + (cam.vec_20.getY() >> 8),
      bobj._148.coord2_14.coord.transfer.getZ() + (cam.vec_20.getZ() >> 8)
    );

    cam._5c.decr();
    if(cam._5c.get() <= 0) {
      cam._121.set(6);
      cam._123.set(0);
    }

    //LAB_800da8a0
  }

  @Method(0x800daa80L)
  public static void FUN_800daa80() {
    if(_800fabb8.get() == 0x1L) {
      if(_800c67d4.get() != 0) {
        _800c67d4.subu(0x1L);
        return;
      }

      //LAB_800daabc
      final long x;
      final long y;
      final long a0 = _800bb0fc.get() & 0x3L;
      if(a0 == 0) {
        //LAB_800dab04
        x = _800c67e4.get();
        y = _800c67e8.get() * 0x2L;
      } else if(a0 == 0x1L) {
        //LAB_800dab1c
        x = -_800c67e4.get() * 0x2L;
        y = -_800c67e8.get();
        //LAB_800daaec
      } else if(a0 == 0x2L) {
        //LAB_800dab3c
        x = _800c67e4.get() * 0x2L;
        y = _800c67e8.get();
      } else {
        //LAB_800dab54
        x = -_800c67e4.get();
        y = -_800c67e8.get() * 0x2L;
      }

      //LAB_800dab70
      //LAB_800dab78
      SetGeomOffset((int)(x_800c67bc.getSigned() + x), (int)(y_800c67c0.getSigned() + y));

      _800c67c4.subu(0x1L);
      if(_800c67c4.getSigned() <= 0) {
        _800fabb8.setu(0);
        SetGeomOffset((int)x_800c67bc.getSigned(), (int)y_800c67c0.getSigned());
      }
    }

    //LAB_800dabb8
  }

  @Method(0x800dabccL)
  public static long FUN_800dabcc(final RunningScript a0) {
    FUN_800dabec();
    return 0;
  }

  @Method(0x800dabecL)
  public static void FUN_800dabec() {
    final BattleCamera cam = camera_800c67f0;
    cam.svec_8c.setX((short)0);
    cam.svec_8c.setY((short)0);
    cam.svec_8c.setZ((short)0);
    cam._108.set(0);
    cam._118.set(0);
    cam._11c.set(0);
    cam._120.set(0);
    cam._121.set(0);
    cam._122.set(0);
    cam._123.set(0);
  }

  @Method(0x800dac20L)
  public static long FUN_800dac20(final RunningScript a0) {
    FUN_800dac70(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get(), a0.params_20.get(4).deref().get());
    return 0;
  }

  @Method(0x800dac70L)
  public static void FUN_800dac70(final int index, final int x, final int y, final int z, final int scriptIndex) {
    _800fabbc.get(index).deref().run(x, y, z, scriptIndex);
    callbackIndex_800c68ec.setu(index);
  }

  @Method(0x800dacc4L)
  public static void FUN_800dacc4(final int x, final int y, final int z, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.vec_94.setX(x);
    cam.vec_94.setY(y);
    cam.vec_94.setZ(z);
    setViewpoint(x >> 8, y >> 8, z >> 8);
    cam._11c.or(0x1L);
    cam._120.set(0);
  }

  @Method(0x800dad14L)
  public static void FUN_800dad14(final int x, final int y, final int z, final int scriptIndex) {
    final IntRef refX = new IntRef().set(x);
    final IntRef refY = new IntRef().set(y);
    final IntRef refZ = new IntRef().set(z);
    final BattleCamera cam = camera_800c67f0;
    cam._a0.set(z);
    cam._ac.set(x);
    cam._b8.set(y);
    FUN_800dcc94(0, 0, 0, refX, refY, refZ);
    setViewpoint(refX.get() >> 8, refY.get() >> 8, refZ.get() >> 8);

    cam._11c.or(0x1L);
    cam._120.set(1);
  }

  @Method(0x800dadd0L)
  public static void FUN_800dadd0(final int x, final int y, final int z, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.vec_94.setX(x);
    cam.vec_94.setY(y);
    cam.vec_94.setZ(z);

    setViewpoint(
      cam.rview2_00.refpoint_0c.getX() + (x >> 8),
      cam.rview2_00.refpoint_0c.getY() + (y >> 8),
      cam.rview2_00.refpoint_0c.getZ() + (z >> 8)
    );

    cam._11c.or(0x1L);
    cam._120.set(4);
  }

  @Method(0x800dae3cL)
  public static void FUN_800dae3c(final int x, final int y, final int z, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam._a0.set(z);
    cam._ac.set(x);
    cam._b8.set(y);
    final IntRef refX = new IntRef().set(cam._ac.get() >> 8);
    final IntRef refY = new IntRef().set(cam._b8.get() >> 8);
    final IntRef refZ = new IntRef().set(cam._a0.get() >> 8);
    FUN_800dcc94(cam.rview2_00.refpoint_0c.getX(), cam.rview2_00.refpoint_0c.getY(), cam.rview2_00.refpoint_0c.getZ(), refX, refY, refZ);
    setViewpoint(refX.get(), refY.get(), refZ.get());
    cam._11c.or(0x1L);
    cam._120.set(5);
  }

  @Method(0x800daedcL)
  public static void FUN_800daedc(final int x, final int y, final int z, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.vec_94.setX(x);
    cam.vec_94.setY(y);
    cam.vec_94.setZ(z);

    final VECTOR v0 = FUN_800dd02c(scriptIndex);

    setViewpoint(
      v0.getX() + (cam.vec_94.getX() >> 8),
      v0.getY() + (cam.vec_94.getY() >> 8),
      v0.getZ() + (cam.vec_94.getZ() >> 8)
    );

    cam.bobjIndex_f4.set(scriptIndex);
    cam._11c.or(0x1L);
    cam._120.set(6);
  }

  @Method(0x800daf6cL)
  public static void FUN_800daf6c(final int x, final int y, final int z, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam._a0.set(z);
    cam._ac.set(x);
    cam._b8.set(y);
    final IntRef refX = new IntRef().set(x >> 8);
    final IntRef refY = new IntRef().set(y >> 8);
    final IntRef refZ = new IntRef().set(z >> 8);
    final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(BattleObject27c.class);
    FUN_800dcc94(bobj._148.coord2_14.coord.transfer.getX(), bobj._148.coord2_14.coord.transfer.getY(), bobj._148.coord2_14.coord.transfer.getZ(), refX, refY, refZ);
    setViewpoint(refX.get(), refY.get(), refZ.get());
    cam.bobjIndex_f4.set(scriptIndex);
    cam._11c.or(0x1L);
    cam._120.set(7);
  }

  @Method(0x800db034L)
  public static long FUN_800db034(final RunningScript a0) {
    FUN_800db084(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get(), a0.params_20.get(4).deref().get());
    return 0;
  }

  @Method(0x800db084L)
  public static void FUN_800db084(final int index, final int x, final int y, final int z, final int scriptIndex) {
    _800fabdc.get(index).deref().run(x, y, z, scriptIndex);
    callbackIndex_800c6878.setu(index);
  }

  @Method(0x800db0d8L)
  public static void FUN_800db0d8(final int x, final int y, final int z, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.vec_20.setX(x);
    cam.vec_20.setY(y);
    cam.vec_20.setZ(z);
    setRefpoint(x >> 8, y >> 8, z >> 8);
    cam._11c.or(0x2L);
    cam._121.set(0);
  }

  @Method(0x800db128L)
  public static void FUN_800db128(final int x, final int y, final int z, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam._2c.set(z);
    cam._38.set(x);
    cam._44.set(y);
    final IntRef refX = new IntRef().set(x >> 8);
    final IntRef refY = new IntRef().set(y >> 8);
    final IntRef refZ = new IntRef().set(z >> 8);
    FUN_800dcc94(0, 0, 0, refX, refY, refZ);
    setRefpoint(refX.get(), refY.get(), refZ.get());
    cam._11c.or(0x2L);
    cam._121.set(1);
  }

  @Method(0x800db2f0L)
  public static void FUN_800db2f0(final int x, final int y, final int z, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.vec_20.setX(x);
    cam.vec_20.setY(y);
    cam.vec_20.setZ(z);

    final VECTOR v0 = FUN_800dd02c(scriptIndex);
    setRefpoint(
      v0.getX() + (x >> 8),
      v0.getY() + (y >> 8),
      v0.getZ() + (z >> 8)
    );

    cam.bobjIndex_80.set(scriptIndex);
    cam._11c.or(0x2L);
    cam._121.set(6);
  }

  @Method(0x800db460L)
  public static long FUN_800db460(final RunningScript a0) {
    FUN_800db4ec(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get(), a0.params_20.get(4).deref().get(), a0.params_20.get(5).deref().get(), a0.params_20.get(6).deref().get(), a0.params_20.get(7).deref().get());
    return 0;
  }

  @Method(0x800db4ecL)
  public static void FUN_800db4ec(final int callbackIndex, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    _800fabfc.offset(callbackIndex * 0x4L).deref(4).call(a1, a2, a3, a5, a6, a4, scriptIndex);
    final BattleCamera cam = camera_800c67f0;
    cam.callbackIndex_fc.set(callbackIndex);
    cam._122.set(1);
  }

  @Method(0x800db574L)
  public static long FUN_800db574(final RunningScript a0) {
    FUN_800db600(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get(), a0.params_20.get(4).deref().get(), a0.params_20.get(5).deref().get(), a0.params_20.get(6).deref().get(), a0.params_20.get(7).deref().get());
    return 0;
  }

  @Method(0x800db600L)
  public static void FUN_800db600(final int callbackIndex, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    _800fac5c.offset(callbackIndex * 0x4L).deref(4).call(a1, a2, a3, a5, a6, a4, scriptIndex);
    final BattleCamera cam = camera_800c67f0;
    cam.callbackIndex_88.set(callbackIndex);
    cam._123.set(1);
  }

  @Method(0x800db8b0L)
  public static long FUN_800db8b0(final RunningScript a0) {
    FUN_800db950(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get(), a0.params_20.get(4).deref().get(), a0.params_20.get(5).deref().get(), a0.params_20.get(6).deref().get(), a0.params_20.get(7).deref().get(), a0.params_20.get(8).deref().get());
    return 0;
  }

  @Method(0x800db950L)
  public static void FUN_800db950(final int callbackIndex, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int a7, final int scriptIndex) {
    _800fac3c.offset(callbackIndex * 0x4L).deref(4).call(a1, a2, a3, a4, a5, a6, a7, scriptIndex);
    final BattleCamera cam = camera_800c67f0;
    cam.callbackIndex_fc.set(callbackIndex);
    cam._122.set(1);
  }

  @Method(0x800db9e0L)
  public static long FUN_800db9e0(final RunningScript a0) {
    FUN_800dba80(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get(), a0.params_20.get(4).deref().get(), a0.params_20.get(5).deref().get(), a0.params_20.get(6).deref().get(), a0.params_20.get(7).deref().get(), a0.params_20.get(8).deref().get());
    return 0;
  }

  @Method(0x800dba80L)
  public static void FUN_800dba80(final int callbackIndex, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int a7, final int scriptIndex) {
    _800fac9c.offset(callbackIndex * 0x4L).deref(4).call(a1, a2, a3, a4, a5, a6, a7, scriptIndex);
    final BattleCamera cam = camera_800c67f0;
    cam.callbackIndex_88.set(callbackIndex);
    cam._123.set(1);
  }

  @Method(0x800dbb10L)
  public static long FUN_800dbb10(final RunningScript a0) {
    final long v1 = a0.params_20.get(0).deref().get();
    final long a1;
    if(v1 == 0) {
      //LAB_800dbb3c
      a1 = _800c6912.get();
    } else if(v1 == 0x1L) {
      //LAB_800dbb48
      a1 = _800c6913.get();
    } else {
      throw new RuntimeException("Undefined a1");
    }

    //LAB_800dbb50
    a0.params_20.get(1).deref().set((int)a1);
    return 0;
  }

  @Method(0x800dbcc8L)
  public static long FUN_800dbcc8(final RunningScript a0) {
    final BattleCamera cam = camera_800c67f0;
    cam._100.set(a0.params_20.get(0).deref().get() << 16);
    cam._108.set(0);
    cam._118.set(1);
    return 0;
  }

  @Method(0x800dbe40L)
  public static void FUN_800dbe40() {
    final BattleCamera cam = camera_800c67f0;
    cam._11c.and(0xffff_fffeL);
    cam._122.set(0);
  }

  @Method(0x800dbe60L)
  public static void FUN_800dbe60() {
    final BattleCamera cam = camera_800c67f0;
    cam._11c.and(0xffff_fffeL);
    cam._122.set(0);
  }

  @Method(0x800dbe98L)
  public static void FUN_800dbe98() {
    final BattleCamera cam = camera_800c67f0;
    cam._122.set(0);

    setViewpoint(
      cam.rview2_00.refpoint_0c.getX() + (cam.vec_94.getX() >> 8),
      cam.rview2_00.refpoint_0c.getY() + (cam.vec_94.getY() >> 8),
      cam.rview2_00.refpoint_0c.getZ() + (cam.vec_94.getZ() >> 8)
    );
  }

  @Method(0x800dbef0L)
  public static void FUN_800dbef0() {
    final BattleCamera cam = camera_800c67f0;
    cam._122.set(0);
    final IntRef refX = new IntRef().set(cam._ac.get() >> 8);
    final IntRef refY = new IntRef().set(cam._b8.get() >> 8);
    final IntRef refZ = new IntRef().set(cam._a0.get() >> 8);
    FUN_800dcc94(cam.rview2_00.refpoint_0c.getX(), cam.rview2_00.refpoint_0c.getY(), cam.rview2_00.refpoint_0c.getZ(), refX, refY, refZ);
    setViewpoint(refX.get(), refY.get(), refZ.get());
  }

  @Method(0x800dbf70L)
  public static void FUN_800dbf70() {
    final BattleCamera cam = camera_800c67f0;
    cam._122.set(0);

    final VECTOR v0 = FUN_800dd02c(cam.bobjIndex_f4.get());
    setViewpoint(v0.getX() + (cam.vec_94.getX() >> 8), v0.getY() + (cam.vec_94.getY() >> 8), v0.getZ() + (cam.vec_94.getZ() >> 8));
  }

  @Method(0x800dbfd4L)
  public static void FUN_800dbfd4() {
    final BattleCamera cam = camera_800c67f0;
    cam._122.set(0);

    final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(cam.bobjIndex_f4.get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    final IntRef refX = new IntRef().set(cam._ac.get() >> 8);
    final IntRef refY = new IntRef().set(cam._b8.get() >> 8);
    final IntRef refZ = new IntRef().set(cam._a0.get() >> 8);
    FUN_800dcc94(bobj._148.coord2_14.coord.transfer.getX(), bobj._148.coord2_14.coord.transfer.getY(), bobj._148.coord2_14.coord.transfer.getZ(), refX, refY, refZ);
    setViewpoint(refX.get(), refY.get(), refZ.get());
  }

  @Method(0x800dc090L)
  public static void FUN_800dc090() {
    final BattleCamera cam = camera_800c67f0;
    cam._11c.and(0xffff_fffdL);
    cam._123.set(0);
  }

  @Method(0x800dc0b0L)
  public static void FUN_800dc0b0() {
    final BattleCamera cam = camera_800c67f0;
    cam._11c.and(0xffff_fffdL);
    cam._123.set(0);
  }

  @Method(0x800dc1b8L)
  public static void FUN_800dc1b8() {
    final BattleCamera cam = camera_800c67f0;
    cam._123.set(0);

    final VECTOR v0 = FUN_800dd02c(cam.bobjIndex_80.get());
    setRefpoint(
      v0.getX() + (cam.vec_20.getX() >> 8),
      v0.getY() + (cam.vec_20.getY() >> 8),
      v0.getZ() + (cam.vec_20.getZ() >> 8)
    );
  }

  @Method(0x800dc2d8L)
  public static long FUN_800dc2d8(final RunningScript s0) {
    final BattleCamera cam = camera_800c67f0;

    final int x;
    final int y;
    final int z;
    final long v1;
    if(s0.params_20.get(0).deref().get() == 0) {
      x = cam.rview2_00.viewpoint_00.getX();
      y = cam.rview2_00.viewpoint_00.getY();
      z = cam.rview2_00.viewpoint_00.getZ();
      v1 = _800fad7c.getAddress();
    } else {
      //LAB_800dc32c
      x = cam.rview2_00.refpoint_0c.getX();
      y = cam.rview2_00.refpoint_0c.getY();
      z = cam.rview2_00.refpoint_0c.getZ();
      v1 = _800fad9c.getAddress();
    }

    //LAB_800dc344
    s0.params_20.get(4).deref().set((int)MEMORY.ref(4, v1).offset(s0.params_20.get(1).deref().get() * 0x4L).deref(4).call(s0.params_20.get(2).deref().get(), s0.params_20.get(3).deref().get(), x, y, z));
    return 0;
  }

  @Method(0x800dc384L)
  public static int FUN_800dc384(final long a0, final long callbackIndex, final int component, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;

    final int x;
    final int y;
    final int z;
    final long v1;
    if(a0 != 0) {
      x = cam.rview2_00.refpoint_0c.getX();
      y = cam.rview2_00.refpoint_0c.getY();
      z = cam.rview2_00.refpoint_0c.getZ();
      v1 = _800fad9c.getAddress();
    } else {
      //LAB_800dc3bc
      x = cam.rview2_00.viewpoint_00.getX();
      y = cam.rview2_00.viewpoint_00.getY();
      z = cam.rview2_00.viewpoint_00.getZ();
      v1 = _800fad7c.getAddress();
    }

    //LAB_800dc3dc
    return (int)MEMORY.ref(4, v1).offset(callbackIndex * 0x4L).deref(4).call(component, scriptIndex, x, y, z);
  }

  @Method(0x800dc408L)
  public static int FUN_800dc408(final int component, final int scriptIndex, final int x, final int y, final int z) {
    if(component == 0) {
      //LAB_800dc440
      return x;
    }

    if(component == 1) {
      //LAB_800dc448
      return y;
    }

    //LAB_800dc42c
    if(component == 2) {
      //LAB_800dc450
      return z;
    }

    throw new IllegalArgumentException("Illegal component " + component);
  }

  @Method(0x800dc45cL)
  public static int FUN_800dc45c(final int component, final int scriptIndex, final int x, final int y, final int z) {
    final IntRef refX = new IntRef().set(x);
    final IntRef refY = new IntRef().set(y);
    final IntRef refZ = new IntRef().set(z);
    FUN_800dcd9c(0, 0, 0, refX, refY, refZ);

    if(component == 0) {
      //LAB_800dc4d8
      return refX.get();
    }

    if(component == 1) {
      //LAB_800dc4e4
      return refY.get();
    }

    //LAB_800dc4c4
    if(component == 2) {
      //LAB_800dc4f0
      return refZ.get();
    }

    throw new IllegalArgumentException("Illegal component " + component);
  }

  @Method(0x800dc514L)
  public static int FUN_800dc514(final int component, final int scriptIndex, final int x, final int y, final int z) {
    final BattleCamera cam = camera_800c67f0;

    if(component == 0) {
      //LAB_800dc550
      return x - cam.rview2_00.refpoint_0c.getX();
    }

    if(component == 1) {
      //LAB_800dc560
      return y - cam.rview2_00.refpoint_0c.getY();
    }

    //LAB_800dc53c
    if(component == 2) {
      //LAB_800dc56c
      return z - cam.rview2_00.refpoint_0c.getZ();
    }

    throw new IllegalArgumentException("Illegal component " + component);
  }

  @Method(0x800dc580L)
  public static int FUN_800dc580(final int component, final int scriptIndex, final int x, final int y, final int z) {
    final BattleCamera cam = camera_800c67f0;

    final IntRef refX = new IntRef().set(x);
    final IntRef refY = new IntRef().set(y);
    final IntRef refZ = new IntRef().set(z);
    FUN_800dcd9c(cam.rview2_00.refpoint_0c.getX(), cam.rview2_00.refpoint_0c.getY(), cam.rview2_00.refpoint_0c.getZ(), refX, refY, refZ);

    if(component == 0) {
      //LAB_800dc604
      return refX.get();
    }

    if(component == 1) {
      //LAB_800dc610
      return refY.get();
    }

    if(component == 2) {
      //LAB_800dc61c
      return refZ.get();
    }

    throw new IllegalArgumentException("Illegal component " + component);
  }

  @Method(0x800dc630L)
  public static int FUN_800dc630(final int component, final int scriptIndex, final int x, final int y, final int z) {
    final VECTOR vec = FUN_800dd02c(scriptIndex);

    if(component == 0) {
      //LAB_800dc698
      return x - vec.getX();
    }

    if(component == 1) {
      //LAB_800dc6a4
      return y - vec.getY();
    }

    if(component == 2) {
      //LAB_800dc6b0
      return z - vec.getZ();
    }

    throw new IllegalArgumentException("Illegal component " + component);
  }

  @Method(0x800dc6d8L)
  public static int FUN_800dc6d8(final int component, final int scriptIndex, final int x, final int y, final int z) {
    final IntRef refX = new IntRef().set(x);
    final IntRef refY = new IntRef().set(y);
    final IntRef refZ = new IntRef().set(z);
    final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(BattleObject27c.class);
    FUN_800dcd9c(bobj._148.coord2_14.coord.transfer.getX(), bobj._148.coord2_14.coord.transfer.getY(), bobj._148.coord2_14.coord.transfer.getZ(), refX, refY, refZ);

    if(component == 0) {
      //LAB_800dc76c
      return refX.get();
    }

    if(component == 1) {
      //LAB_800dc778
      return refY.get();
      //LAB_800dc758
    }

    if(component == 2) {
      //LAB_800dc784
      return refZ.get();
    }

    throw new IllegalArgumentException("Illegal component " + component);
  }

  @Method(0x800dc798L)
  public static int FUN_800dc798(final int component, final int scriptIndex, final int x, final int y, final int z) {
    if(component == 0) {
      //LAB_800dc7d0
      return x;
    }

    if(component == 1) {
      //LAB_800dc7d8
      return y;
    }

    //LAB_800dc7bc
    if(component == 2) {
      //LAB_800dc7e0
      return z;
    }

    throw new IllegalArgumentException("Illegal component " + component);
  }

  @Method(0x800dc7ecL)
  public static int FUN_800dc7ec(final int component, final int scriptIndex, final int x, final int y, final int z) {
    final IntRef refX = new IntRef().set(x);
    final IntRef refY = new IntRef().set(y);
    final IntRef refZ = new IntRef().set(z);
    FUN_800dcd9c(0, 0, 0, refX, refY, refZ);

    if(component == 0) {
      //LAB_800dc868
      return refX.get();
    }

    if(component == 1) {
      //LAB_800dc874
      return refY.get();
    }

    if(component == 2) {
      //LAB_800dc880
      return refZ.get();
    }

    throw new IllegalArgumentException("Illegal component " + component);
  }

  @Method(0x800dc9c0L)
  public static int FUN_800dc9c0(final int component, final int scriptIndex, final int x, final int y, final int z) {
    final VECTOR vec = FUN_800dd02c(scriptIndex);

    if(component == 0) {
      //LAB_800dca28
      return x - vec.getX();
    }

    if(component == 0x1L) {
      //LAB_800dca34
      return y - vec.getY();
    }

    if(component == 0x2L) {
      //LAB_800dca40
      return z - vec.getZ();
    }

    throw new IllegalArgumentException("Illegal component " + component);
  }

  @Method(0x800dcc64L)
  public static void setViewpoint(final int x, final int y, final int z) {
    final BattleCamera cam = camera_800c67f0;

    cam.rview2_00.viewpoint_00.setX(x);
    cam.rview2_00.viewpoint_00.setY(y);
    cam.rview2_00.viewpoint_00.setZ(z);
  }

  @Method(0x800dcc7cL)
  public static void setRefpoint(final int x, final int y, final int z) {
    final BattleCamera cam = camera_800c67f0;

    cam.rview2_00.refpoint_0c.setX(x);
    cam.rview2_00.refpoint_0c.setY(y);
    cam.rview2_00.refpoint_0c.setZ(z);
  }

  @Method(0x800dcc94L)
  public static void FUN_800dcc94(final int a0, final int a1, final int a2, final IntRef x, IntRef y, final IntRef z) {
    _800fab98.set((short)x.get(), (short)y.get(), (short)0);
    RotMatrix_8003faf0(_800fab98, _800c6798);
    SetRotMatrix(_800c6798);
    SetTransMatrix(_800c6798);
    _800faba0.set((short)0, (short)0, (short)z.get());
    FUN_8003f990(_800faba0, _800faba8, _800c67b8);
    x.set(a0 - _800faba8.getZ());
    y.set(a1 - _800faba8.getX());
    z.set(a2 + _800faba8.getY());
  }

  @Method(0x800dcd9cL)
  public static void FUN_800dcd9c(final int a0, final int a1, final int a2, final IntRef x, final IntRef y, final IntRef z) {
    final int dx = a0 - x.get();
    final int dy = a1 - y.get();
    final int dz = a2 - z.get();

    int s1 = dx / 2;
    s1 = s1 * s1;

    final long halfDx = dy / 2;
    final long halfDy = dz / 2;

    x.set(ratan2(dz, dx) & 0xfff);
    y.set(ratan2(dy, SquareRoot0(s1 + halfDy * halfDy) * 2) & 0xfff);
    z.set(SquareRoot0(s1 + halfDx * halfDx + (int)(halfDy * dz) / 2) * 2);
  }

  @Method(0x800dcebcL)
  public static void FUN_800dcebc(long a0, long a1, long a2, long a3, final Ref<Long> a4, final Ref<Long> a5) {
    long v0;
    long lo;
    if(a0 == 0) {
      //LAB_800dcedc
      v0 = a2 << 1;
      lo = (int)v0 / (int)a3;
      v0 = lo;
      a4.set(a1);
      v0 = v0 - a1;
      a5.set(v0);
      return;
    }
    v0 = 0x1L;
    if(a0 != v0) {
      return;
    }

    //LAB_800dcef8
    v0 = a2 << 1;
    lo = (int)v0 / (int)a3;
    v0 = lo;
    a5.set(a1);
    v0 = v0 - a1;
    a4.set(v0);
  }

  @Method(0x800dcf10L)
  public static int FUN_800dcf10(final int a0, final int a1, final int a2, final int a3, final int a4) {
    if(a0 < 0) {
      throw new RuntimeException("Undefined v0");
    }

    if(a0 < 2) {
      //LAB_800dcf38
      if(a4 == 0) {
        return 0;
      }

      //LAB_800dcf48
      if(a4 == 0x1L) {
        if(a2 < a1) {
          //LAB_800dcf6c
          return (a2 - a1 + 0x10_0000) / a3;
        }

        return (a2 - a1) / a3;
      }

      //LAB_800dcf84
      if(a2 < a1) {
        //LAB_800dcf58
        return (a2 - a1) / a3;
      }

      return -(a1 + 0x10_0000 - a2) / a3;
    }

    if(a0 == 2) {
      //LAB_800dcfa4
      //LAB_800dcfb0
      return (a2 - a1) / a3;
    }

    throw new IllegalArgumentException("Invalid a0");
  }

  @Method(0x800dcfb8L)
  public static int FUN_800dcfb8(final int a0, final int a1, final int a2, final int a3) {
    if(a0 < 0) {
      throw new IllegalArgumentException("Invalid a0");
    }

    if(a0 < 0x2L) {
      //LAB_800dcfdc
      if(a3 == 0) {
        return 0;
      }

      //LAB_800dcfec
      if(a3 == 1) {
        if(a2 >= a1) {
          //LAB_800dcffc
          return a2 - a1;
        }

        //LAB_800dd004
        //LAB_800dd008
        return a2 - a1 + 0x10_0000;
      }

      //LAB_800dd010
      if(a2 < a1) {
        return a2 - a1;
      }

      return a2 - (a1 + 0x10_0000);
    }

    if(a0 == 2) {
      //LAB_800dd020
      //LAB_800dd024
      return a2 - a1;
    }

    throw new IllegalArgumentException("Invalid a0");
  }

  @Method(0x800dd02cL)
  public static VECTOR FUN_800dd02c(int scriptIndex) {
    final BattleScriptDataBase data = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(BattleScriptDataBase.class);

    if(data.magic_00.get() == BattleScriptDataBase.EM__) {
      return ((BttlScriptData6c)data)._10.vec_04;
    }

    return ((BattleObject27c)data)._148.coord2_14.coord.transfer;
  }

  @Method(0x800dd0d4L)
  public static long FUN_800dd0d4() {
    final BattleCamera cam = camera_800c67f0;
    return (int)_800fad7c.offset(5 * 0x4L).deref(4).call(1, 0, cam.rview2_00.viewpoint_00.getX(), cam.rview2_00.viewpoint_00.getY(), cam.rview2_00.viewpoint_00.getZ());
  }

  @Method(0x800dd118L)
  public static long FUN_800dd118() {
    final BattleCamera cam = camera_800c67f0;
    return (int)_800fad7c.offset(5 * 0x4L).deref(4).call(0, 0, cam.rview2_00.viewpoint_00.getX(), cam.rview2_00.viewpoint_00.getY(), cam.rview2_00.viewpoint_00.getZ());
  }

  @Method(0x800de3f4L)
  public static void FUN_800de3f4(final TmdObjTable a0, final BttlScriptData6cInner a1, final MATRIX a2) {
    long s0 = _800c693c.deref(4).offset(0x20L).get() & 0x4L;
    s0 = s0 >> 1 | s0 >> 2;

    final MATRIX sp0x10 = new MATRIX();
    if((a1._00.get() & 0x8L) != 0) {
      FUN_8003eba0(a2, sp0x10);
      GsSetLightMatrix(sp0x10);
    } else {
      //LAB_800de458
      GsSetLightMatrix(a2);
    }

    //LAB_800de45c
    FUN_8003f210(Scus94491BpeSegment_800c.matrix_800c3548, a2, sp0x10);
    if((a1._00.get() & 0x400_0000L) == 0) {
      RotMatrix_8003faf0(a1.svec_10, sp0x10);
      ScaleVectorL_SVEC(sp0x10, a1.svec_16);
    }

    //LAB_800de4a8
    if((s0 & (sp0x10.transfer.getZ() ^ _800bb0fc.get())) == 0 || sp0x10.transfer.getZ() - sp0x10.transfer.getX() >= -0x800L && sp0x10.transfer.getZ() + sp0x10.transfer.getX() >= -0x800L && sp0x10.transfer.getZ() - sp0x10.transfer.getY() >= -0x800L && sp0x10.transfer.getZ() + sp0x10.transfer.getY() >= -0x800L) {
      //LAB_800de50c
      setRotTransMatrix(sp0x10);

      final Memory.TemporaryReservation tmp = MEMORY.temp(0x10);
      final GsDOBJ2 dobj2 = new GsDOBJ2(tmp.get());
      dobj2.attribute_00.set(a1._00.get());
      dobj2.tmd_08.set(a0);
      FUN_800e3e6c(dobj2);
      tmp.release();
    }

    //LAB_800de528
  }

  @Method(0x800de544L)
  public static SVECTOR FUN_800de544(final SVECTOR a0, final MATRIX a1) {
    final MATRIX sp0x10 = new MATRIX().set(a1);
    a0.setX((short)ratan2(-sp0x10.get(5), sp0x10.get(8)));
    RotMatrixX(a0.getX(), sp0x10);
    a0.setY((short)ratan2(sp0x10.get(2), sp0x10.get(8)));
    RotMatrixY(-a0.getY(), sp0x10);
    a0.setZ((short)ratan2(sp0x10.get(3), sp0x10.get(0)));
    return a0;
  }

  @Method(0x800de72cL)
  public static void ScaleVectorL_SVEC(final MATRIX a0, final SVECTOR a1) {
    ScaleMatrixL(a0, new VECTOR().set(a1));
  }

  @Method(0x800de76cL)
  public static long FUN_800de76c(final long a0, final int objIndex) {
    if((MEMORY.ref(4, a0).offset(0x4L).get() & 0x2L) == 0) {
      final Memory.TemporaryReservation tmp = MEMORY.temp(0x10);
      final GsDOBJ2 dobj2 = tmp.get().cast(GsDOBJ2::new);
      updateTmdPacketIlen(MEMORY.ref(4, a0 + 0xcL, UnboundedArrayRef.of(0x1c, TmdObjTable::new)), dobj2, objIndex); //TODO
      final long tmd = dobj2.tmd_08.getPointer();
      tmp.release();
      return tmd;
    }

    //LAB_800de7a0
    //LAB_800de7b4
    return a0 + objIndex * 0x1cL + 0xcL;
  }

  @Method(0x800de840L)
  public static long FUN_800de840(final long a0, final long a1, long a2) {
    final BttlStruct50 a3 = _800c6920.deref();

    MEMORY.ref(4, a0).offset(0x0L).setu(a3._10.get((int)a3._04.get()).getAddress());
    long t1 = a1;

    //LAB_800de878
    a2 = (int)a2 >> 1;
    while(a3._0c.get() < (int)a2) {
      if((a3._08.get() & 0x100L) == 0) {
        a3._08.set(MEMORY.ref(1, t1).offset(0x0L).get() | 0xff00L);
        t1 = t1 + 0x1L;
      }

      //LAB_800de89c
      if((a3._08.get() & 0x1L) != 0) {
        a3._10.get((int)a3._00.get()).set((int)(MEMORY.ref(1, t1).offset(0x1L).get() << 8 | MEMORY.ref(1, t1).offset(0x0L).get()));
        a3._00.incr().and(0x1fL);
        a3._0c.incr();
        t1 = t1 + 0x2L;
      } else {
        //LAB_800de8ec
        long a1_0 = MEMORY.ref(1, t1).offset(0x0L).get();
        final long t2 = (a1_0 >>> 5) + 0x1L;

        //LAB_800de904
        int i;
        for(i = 0; i < t2; i++) {
          a1_0 = a1_0 & 0x1fL;
          a3._10.get((int)a3._00.get()).set(a3._10.get((int)a1_0).get());
          a3._00.incr().and(0x1fL);
          a1_0 = a1_0 + 0x1L;
        }

        //LAB_800de940
        a3._0c.add(i);
        t1 = t1 + 0x1L;
      }

      //LAB_800de94c
      a3._08.set(a3._08.get() >> 1);
    }

    //LAB_800de968
    //LAB_800de970
    for(int i = 0; i < a2; i++) {
      MEMORY.ref(2, a0).offset(i * 0x2L).setu(a3._10.get((int)a3._04.get()).get());
      a3._04.incr().and(0x1fL);
      a3._0c.decr();
    }

    //LAB_800de9b4
    return t1;
  }

  @Method(0x800de9bcL)
  public static long FUN_800de9bc(long a0, long a1, long a2, long a3) {
    long v0;
    long v1;
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long t5;
    long t6;
    long t7;
    long s0;
    long s1;
    long s2;
    long t8;
    long t9;
    long s8;
    t9 = a1;
    s8 = a2;
    v1 = 0xe100_0000L;
    v1 = v1 | 0x200L;
    v0 = 0x1f80_0000L;
    a2 = 0x1f80_0000L;
    t1 = MEMORY.ref(4, v0).offset(0x3d8L).get();
    v0 = MEMORY.ref(2, a2).offset(0x3ecL).getSigned();
    a2 = a2 + 0x3ecL;
    t8 = MEMORY.ref(1, a2).offset(0x2L).get();
    s2 = v0 | v1;
    v1 = 0x8006_0000L;
    v0 = 0x800c_0000L;
    a1 = MEMORY.ref(4, v0).offset(-0x4ef8L).get();
    v1 = v1 - 0x5c90L;
    v0 = a1 << 2;
    v0 = v0 + a1;
    v0 = v0 << 2;
    v0 = v0 + v1;
    s1 = MEMORY.ref(4, v0).offset(0x4L).get();
    if(a3 == 0) {
      t3 = a0;
    } else {
      t3 = a0;
      s0 = t8 & 0x2L;
      t4 = 0xff_0000L;
      t4 = t4 | 0xffffL;
      a2 = a0 + 0x14L;
      a1 = t1 + 0x20L;

      //LAB_800dea38
      do {
        t5 = MEMORY.ref(2, t3).offset(0xaL).get();
        t6 = MEMORY.ref(2, t3).offset(0xeL).get();
        t7 = MEMORY.ref(2, t3).offset(0x12L).get();
        t5 = t5 << 3;
        t6 = t6 << 3;
        t7 = t7 << 3;
        t5 = t9 + t5;
        t6 = t9 + t6;
        t7 = t9 + t7;
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);
        CPU.COP2(0x280030L);
        a3 = a3 - 0x1L;
        t2 = CPU.CFC2(31);

        if((int)t2 >= 0) {
          CPU.COP2(0x1400006L);
          t2 = CPU.MFC2(24);

          if((int)t2 > 0 || s0 != 0 && t2 != 0) {
            //LAB_800deab8
            MEMORY.ref(4, t1).offset(0xcL).setu(CPU.MFC2(12));
            MEMORY.ref(4, t1).offset(0x14L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t1).offset(0x1cL).setu(CPU.MFC2(14));
            v0 = MEMORY.ref(2, a2).offset(0x2L).get();
            v0 = v0 << 3;
            v0 = t9 + v0;
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
            CPU.COP2(0x180001L);
            t2 = CPU.CFC2(31);
            v0 = t1 + 0x24L;
            MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(14));
            CPU.COP2(0x168002eL);
            t0 = CPU.MFC2(7);
            v0 = 0x1f80_0000L;
            v0 = MEMORY.ref(4, v0).offset(0x3e8L).get();
            t0 = t0 + v0;
            t0 = (int)t0 >> 2;
            if((int)t0 >= 0xbL) {
              if((int)t0 >= 0xffeL) {
                t0 = 0xffeL;
              }
              a0 = t0 << 2;

              //LAB_800deb34
              a0 = s1 + a0;
              v0 = t3 + 0x4L;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
              t5 = MEMORY.ref(2, t3).offset(0x8L).get();
              t6 = MEMORY.ref(2, t3).offset(0xcL).get();
              t7 = MEMORY.ref(2, t3).offset(0x10L).get();
              t5 = t5 << 3;
              t6 = t6 << 3;
              t7 = t7 << 3;
              t5 = s8 + t5;
              t6 = s8 + t6;
              t7 = s8 + t7;
              CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
              CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
              CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
              CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
              CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
              CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);
              CPU.COP2(0x118043fL);
              MEMORY.ref(4, t1).offset(0x8L).setu(CPU.MFC2(20));
              MEMORY.ref(4, t1).offset(0x10L).setu(CPU.MFC2(21));
              MEMORY.ref(4, t1).offset(0x18L).setu(CPU.MFC2(22));
              MEMORY.ref(1, a1).offset(-0x15L).setu(t8);
              MEMORY.ref(4, a1).offset(-0x1cL).setu(s2);
              v0 = MEMORY.ref(4, a0).offset(0x0L).get();
              v1 = 0x900_0000L;
              v0 = v0 & t4;
              v0 = v0 | v1;
              MEMORY.ref(4, t1).offset(0x0L).setu(v0);
              v0 = MEMORY.ref(2, a2).offset(0x0L).get();
              v0 = v0 << 3;
              v0 = s8 + v0;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
              CPU.COP2(0x108041bL);
              v0 = t1 & t4;
              MEMORY.ref(4, a0).offset(0x0L).setu(v0);
              MEMORY.ref(4, a1).offset(0x0L).setu(CPU.MFC2(22));
              a1 = a1 + 0x28L;
              t1 = t1 + 0x28L;
            }
          }
        }

        //LAB_800debe8
        t3 = t3 + 0x18L;
        a2 = a2 + 0x18L;
      } while(a3 != 0);
    }

    //LAB_800debf4
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t1);
    return t3;
  }

  @Method(0x800dec14L)
  public static long FUN_800dec14(long a0, long a1, long a2, long a3) {
    long v0;
    long v1;
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long t5;
    long t6;
    long t7;
    long s0;
    long t8;
    long t9;
    long s8;
    t4 = a0;
    t9 = a1;
    v0 = 0x1f80_0000L;
    a1 = 0x1f80_0000L;
    v1 = 0x8006_0000L;
    t0 = MEMORY.ref(4, v0).offset(0x3d8L).get();
    v0 = 0x800c_0000L;
    a0 = MEMORY.ref(4, v0).offset(-0x4ef8L).get();
    v1 = v1 - 0x5c90L;
    v0 = a0 << 2;
    v0 = v0 + a0;
    v0 = v0 << 2;
    v0 = v0 + v1;
    a0 = MEMORY.ref(1, a1).offset(0x3eeL).get();
    s0 = MEMORY.ref(4, v0).offset(0x4L).get();
    v1 = a0 & 0x2L;
    if(v1 != 0) {
      v1 = 0x3e80_0000L;
    } else {
      v1 = 0x3c80_0000L;
    }

    //LAB_800dec68
    v1 = v1 | 0x8080L;
    v0 = 0xcL;
    MEMORY.ref(1, t0).offset(0x3L).setu(v0);
    MEMORY.ref(4, t0).offset(0x4L).setu(v1);
    v0 = t0 + 0x4L;
    CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
    if(a3 != 0) {
      s8 = a0 & 0x2L;
      t8 = 0xff_0000L;
      t8 = t8 | 0xffffL;
      a1 = t4 + 0x20L;
      t2 = t0 + 0x28L;

      //LAB_800dec9c
      do {
        t5 = MEMORY.ref(2, t4).offset(0x16L).get();
        t6 = MEMORY.ref(2, t4).offset(0x1aL).get();
        t7 = MEMORY.ref(2, t4).offset(0x1eL).get();
        t5 = t5 << 3;
        t6 = t6 << 3;
        t7 = t7 << 3;
        t5 = t9 + t5;
        t6 = t9 + t6;
        t7 = t9 + t7;
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);
        CPU.COP2(0x280030L);
        v0 = MEMORY.ref(4, a1).offset(-0x1cL).get();
        MEMORY.ref(4, t2).offset(-0x1cL).setu(v0);
        v0 = MEMORY.ref(4, a1).offset(-0x18L).get();
        a3 = a3 - 0x1L;
        MEMORY.ref(4, t2).offset(-0x10L).setu(v0);
        t3 = CPU.CFC2(31);

        if((int)t3 >= 0) {
          CPU.COP2(0x1400006L);
          v0 = MEMORY.ref(4, a1).offset(-0x14L).get();
          MEMORY.ref(4, t2).offset(-0x4L).setu(v0);
          t3 = CPU.MFC2(24);

          if((int)t3 > 0 || s8 != 0 && t3 != 0) {
            //LAB_800ded3c
            MEMORY.ref(4, t0).offset(0x8L).setu(CPU.MFC2(12));
            MEMORY.ref(4, t0).offset(0x14L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t0).offset(0x20L).setu(CPU.MFC2(14));
            v0 = MEMORY.ref(2, a1).offset(0x2L).get();
            v0 = v0 << 3;
            v0 = t9 + v0;
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
            CPU.COP2(0x180001L);
            t3 = CPU.CFC2(31);
            v0 = t0 + 0x2cL;
            MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(14));
            CPU.COP2(0x168002eL);
            v0 = MEMORY.ref(4, a1).offset(-0x10L).get();
            MEMORY.ref(4, t2).offset(0x8L).setu(v0);
            t1 = CPU.MFC2(7);
            v0 = 0x1f80_0000L;
            v0 = MEMORY.ref(4, v0).offset(0x3e8L).get();
            t1 = t1 + v0;
            t1 = (int)t1 >> 2;
            if((int)t1 >= 0xbL) {
              if((int)t1 >= 0xffeL) {
                t1 = 0xffeL;
              }
              a0 = t1 << 2;

              //LAB_800dedc4
              a0 = s0 + a0;
              t5 = MEMORY.ref(2, t4).offset(0x14L).get();
              t6 = MEMORY.ref(2, t4).offset(0x18L).get();
              t7 = MEMORY.ref(2, t4).offset(0x1cL).get();
              t5 = t5 << 3;
              t6 = t6 << 3;
              t7 = t7 << 3;
              t5 = a2 + t5;
              t6 = a2 + t6;
              t7 = a2 + t7;
              CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
              CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
              CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
              CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
              CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
              CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);
              CPU.COP2(0x118043fL);
              MEMORY.ref(4, t0).offset(0x4L).setu(CPU.MFC2(20));
              MEMORY.ref(4, t0).offset(0x10L).setu(CPU.MFC2(21));
              MEMORY.ref(4, t0).offset(0x1cL).setu(CPU.MFC2(22));
              v0 = MEMORY.ref(2, a1).offset(0x0L).get();
              v0 = v0 << 3;
              v0 = a2 + v0;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
              CPU.COP2(0x108041bL);
              v0 = MEMORY.ref(4, a0).offset(0x0L).get();
              v1 = 0xc00_0000L;
              v0 = v0 & t8;
              v0 = v0 | v1;
              MEMORY.ref(4, t0).offset(0x0L).setu(v0);
              v0 = t0 & t8;
              MEMORY.ref(4, a0).offset(0x0L).setu(v0);
              MEMORY.ref(4, t2).offset(0x0L).setu(CPU.MFC2(22));
              t2 = t2 + 0x34L;
              t0 = t0 + 0x34L;
            }
          }
        }

        //LAB_800dee68
        a1 = a1 + 0x24L;
        t4 = t4 + 0x24L;
      } while(a3 != 0);
    }

    //LAB_800dee74
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t0);
    return t4;
  }

  @Method(0x800dee8cL)
  public static long FUN_800dee8c(long a0, long a1, long a2, long a3) {
    long v0;
    long v1;
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long t5;
    long t6;
    long t7;
    long s0;
    long s1;
    long s2;
    long t8;
    long t9;
    long s8;
    s8 = a1;
    t4 = a2;
    v1 = 0xe100_0000L;
    v1 = v1 | 0x200L;
    v0 = 0x1f80_0000L;
    a2 = 0x1f80_0000L;
    t0 = MEMORY.ref(4, v0).offset(0x3d8L).get();
    v0 = MEMORY.ref(2, a2).offset(0x3ecL).getSigned();
    a2 = a2 + 0x3ecL;
    t9 = MEMORY.ref(1, a2).offset(0x2L).get();
    s2 = v0 | v1;
    v1 = 0x8006_0000L;
    v0 = 0x800c_0000L;
    a1 = MEMORY.ref(4, v0).offset(-0x4ef8L).get();
    v1 = v1 - 0x5c90L;
    v0 = a1 << 2;
    v0 = v0 + a1;
    v0 = v0 << 2;
    v0 = v0 + v1;
    s1 = MEMORY.ref(4, v0).offset(0x4L).get();
    t2 = a0;
    if(a3 != 0) {
      s0 = t9 & 0x2L;
      t8 = 0xff_0000L;
      t8 = t8 | 0xffffL;
      a1 = a0 + 0x20L;
      a2 = t0 + 0x4L;

      //LAB_800def08
      do {
        t5 = MEMORY.ref(2, t2).offset(0x16L).get();
        t6 = MEMORY.ref(2, t2).offset(0x1aL).get();
        t7 = MEMORY.ref(2, t2).offset(0x1eL).get();
        t5 = t5 << 3;
        t6 = t6 << 3;
        t7 = t7 << 3;
        t5 = s8 + t5;
        t6 = s8 + t6;
        t7 = s8 + t7;
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);
        CPU.COP2(0x280030L);
        a3 = a3 - 0x1L;
        t3 = CPU.CFC2(31);

        if((int)t3 >= 0) {
          CPU.COP2(0x1400006L);
          t3 = CPU.MFC2(24);

          if((int)t3 > 0 || s0 != 0 && t3 != 0) {
            //LAB_800def88
            MEMORY.ref(4, t0).offset(0xcL).setu(CPU.MFC2(12));
            MEMORY.ref(4, t0).offset(0x14L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t0).offset(0x1cL).setu(CPU.MFC2(14));
            v0 = MEMORY.ref(2, a1).offset(0x2L).get();
            v0 = v0 << 3;
            v0 = s8 + v0;
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
            CPU.COP2(0x180001L);
            t3 = CPU.CFC2(31);
            v0 = t0 + 0x24L;
            MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(14));
            CPU.COP2(0x168002eL);
            t1 = CPU.MFC2(7);
            v0 = 0x1f80_0000L;
            v0 = MEMORY.ref(4, v0).offset(0x3e8L).get();
            t1 = t1 + v0;
            t1 = (int)t1 >> 2;
            if((int)t1 >= 0xbL) {
              if((int)t1 >= 0xffeL) {
                t1 = 0xffeL;
              }
              a0 = t1 << 2;

              //LAB_800df004
              a0 = s1 + a0;
              v0 = t2 + 0x4L;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
              v0 = MEMORY.ref(2, a1).offset(-0xcL).get();
              v0 = v0 << 3;
              v0 = t4 + v0;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
              CPU.COP2(0x108041bL);
              v0 = t0 + 0x8L;
              MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(22));
              v0 = t2 + 0x8L;
              MEMORY.ref(1, a2).offset(0x7L).setu(t9);
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
              v0 = MEMORY.ref(2, a1).offset(-0x8L).get();
              v0 = v0 << 3;
              v0 = t4 + v0;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
              CPU.COP2(0x108041bL);
              v0 = t0 + 0x10L;
              MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(22));
              v0 = t2 + 0xcL;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
              v0 = MEMORY.ref(2, a1).offset(-0x4L).get();
              v0 = v0 << 3;
              v0 = t4 + v0;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
              CPU.COP2(0x108041bL);
              v0 = t0 + 0x18L;
              MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(22));
              v0 = t2 + 0x10L;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
              v0 = MEMORY.ref(2, a1).offset(0x0L).get();
              v0 = v0 << 3;
              v0 = t4 + v0;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
              CPU.COP2(0x108041bL);
              v0 = t0 + 0x20L;
              MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(22));
              MEMORY.ref(4, a2).offset(0x0L).setu(s2);
              a2 = a2 + 0x28L;
              v1 = MEMORY.ref(4, a0).offset(0x0L).get();
              v0 = 0x900_0000L;
              v1 = v1 & t8;
              v1 = v1 | v0;
              v0 = t0 & t8;
              MEMORY.ref(4, t0).offset(0x0L).setu(v1);
              t0 = t0 + 0x28L;
              MEMORY.ref(4, a0).offset(0x0L).setu(v0);
            }
          }
        }

        //LAB_800df104
        a1 = a1 + 0x24L;
        t2 = t2 + 0x24L;
      } while(a3 != 0);
    }

    //LAB_800df110
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t0);
    return t2;
  }

  @Method(0x800df130L)
  public static long FUN_800df130(long a0, long a1, long a2, long a3) {
    long v0;
    long v1;
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long t5;
    long t6;
    long t7;
    long s0;
    long s1;
    long s2;
    long t8;
    long t9;
    long s8;
    s2 = a1;
    t9 = a2;
    v1 = 0xe100_0000L;
    v1 = v1 | 0x200L;
    v0 = 0x1f80_0000L;
    a2 = 0x1f80_0000L;
    t0 = MEMORY.ref(4, v0).offset(0x3d8L).get();
    v0 = MEMORY.ref(2, a2).offset(0x3ecL).getSigned();
    a2 = a2 + 0x3ecL;
    t8 = MEMORY.ref(1, a2).offset(0x2L).get();
    s1 = v0 | v1;
    v1 = 0x8006_0000L;
    v0 = 0x800c_0000L;
    a1 = MEMORY.ref(4, v0).offset(-0x4ef8L).get();
    v1 = v1 - 0x5c90L;
    v0 = a1 << 2;
    v0 = v0 + a1;
    v0 = v0 << 2;
    v0 = v0 + v1;
    s0 = MEMORY.ref(4, v0).offset(0x4L).get();
    t2 = a0;
    if(a3 != 0) {
      s8 = t8 & 0x2L;
      t4 = 0xff_0000L;
      t4 = t4 | 0xffffL;
      a1 = a0 + 0x18L;
      a2 = t0 + 0x4L;

      //LAB_800df1ac
      do {
        t5 = MEMORY.ref(2, t2).offset(0x12L).get();
        t6 = MEMORY.ref(2, t2).offset(0x16L).get();
        t7 = MEMORY.ref(2, t2).offset(0x1aL).get();
        t5 = t5 << 3;
        t6 = t6 << 3;
        t7 = t7 << 3;
        t5 = s2 + t5;
        t6 = s2 + t6;
        t7 = s2 + t7;
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);
        CPU.COP2(0x280030L);
        a3 = a3 - 0x1L;
        t3 = CPU.CFC2(31);

        if((int)t3 >= 0) {
          CPU.COP2(0x1400006L);
          t3 = CPU.MFC2(24);

          if((int)t3 > 0 || s8 != 0 && t3 != 0) {
            //LAB_800df22c
            MEMORY.ref(4, t0).offset(0xcL).setu(CPU.MFC2(12));
            MEMORY.ref(4, t0).offset(0x14L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t0).offset(0x1cL).setu(CPU.MFC2(14));
            CPU.COP2(0x158002dL);
            t1 = CPU.MFC2(7);
            v0 = 0x1f80_0000L;
            v0 = MEMORY.ref(4, v0).offset(0x3e8L).get();
            t1 = t1 + v0;
            t1 = (int)t1 >> 2;
            if((int)t1 >= 0xbL) {
              if((int)t1 >= 0xffeL) {
                t1 = 0xffeL;
              }
              a0 = t1 << 2;

              //LAB_800df278
              a0 = s0 + a0;
              v0 = t2 + 0x4L;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
              v0 = MEMORY.ref(2, a1).offset(-0x8L).get();
              v0 = v0 << 3;
              v0 = t9 + v0;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
              CPU.COP2(0x108041bL);
              v0 = t0 + 0x8L;
              MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(22));
              v0 = t2 + 0x8L;
              MEMORY.ref(1, a2).offset(0x7L).setu(t8);
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
              v0 = MEMORY.ref(2, a1).offset(-0x4L).get();
              v0 = v0 << 3;
              v0 = t9 + v0;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
              CPU.COP2(0x108041bL);
              v0 = t0 + 0x10L;
              MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(22));
              v0 = t2 + 0xcL;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
              v0 = MEMORY.ref(2, a1).offset(0x0L).get();
              v0 = v0 << 3;
              v0 = t9 + v0;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
              CPU.COP2(0x108041bL);
              v0 = t0 + 0x18L;
              MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(22));
              MEMORY.ref(4, a2).offset(0x0L).setu(s1);
              a2 = a2 + 0x20L;
              v1 = MEMORY.ref(4, a0).offset(0x0L).get();
              v0 = 0x700_0000L;
              v1 = v1 & t4;
              v1 = v1 | v0;
              v0 = t0 & t4;
              MEMORY.ref(4, t0).offset(0x0L).setu(v1);
              t0 = t0 + 0x20L;
              MEMORY.ref(4, a0).offset(0x0L).setu(v0);
            }
          }
        }

        //LAB_800df344
        a1 = a1 + 0x1cL;
        t2 = t2 + 0x1cL;
      } while(a3 != 0);
    }

    //LAB_800df350
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t0);
    return t2;
  }

  @Method(0x800df370L)
  public static long FUN_800df370(final long primitives, final long vertices, final long normals, final long count) {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long t0;
    long t1;
    long t5;
    long t6;
    long t7;
    long s0;
    long s1;
    long s2;
    long s5;
    long s7;
    long lo;
    long s3 = primitives;
    long s4 = count;
    v0 = 0x1f80_0000L;
    a3 = 0x1f80_0000L;
    v1 = 0x8006_0000L;
    s1 = MEMORY.ref(4, v0).offset(0x3d8L).get();
    v0 = 0x800c_0000L;
    v1 = v1 - 0x5c90L;
    a0 = MEMORY.ref(4, v0).offset(-0x4ef8L).get();
    s5 = MEMORY.ref(1, a3).offset(0x3eeL).get();
    v0 = a0 << 2;
    v0 = v0 + a0;
    v0 = v0 << 2;
    v0 = v0 + v1;
    s7 = MEMORY.ref(4, v0).offset(0x4L).get();
    final IntRef sp0x10 = new IntRef();
    final IntRef sp0x14 = new IntRef();
    final IntRef sp0x18 = new IntRef();
    FUN_800e4d74(sp0x10, sp0x14, sp0x18);
    final long sp10 = sp0x10.get();
    final long sp14 = sp0x14.get();
    final long sp18 = sp0x18.get();

    if(s4 != 0) {
      t0 = s5 & 0x2L;
      a3 = 0xff_0000L;
      a3 = a3 | 0xffffL;
      a2 = s3 + 0x22L;
      a1 = s1 + 0x2aL;

      //LAB_800df404
      do {
        t5 = MEMORY.ref(2, s3).offset(0x24L).get();
        t6 = MEMORY.ref(2, s3).offset(0x26L).get();
        t7 = MEMORY.ref(2, s3).offset(0x28L).get();
        t5 = t5 << 3;
        t6 = t6 << 3;
        t7 = t7 << 3;
        t5 = vertices + t5;
        t6 = vertices + t6;
        t7 = vertices + t7;
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);
        CPU.COP2(0x280030L);
        v0 = MEMORY.ref(4, a2-0x1eL).get();
        MEMORY.ref(4, a1-0x1eL).setu(v0);
        v0 = MEMORY.ref(4, a2-0x1aL).get();
        s4 = s4 - 0x1L;
        MEMORY.ref(4, a1-0x12L).setu(v0);
        s2 = CPU.CFC2(31);

        if((int)s2 >= 0) {
          CPU.COP2(0x1400006L);
          v0 = MEMORY.ref(4, a2-0x16L).get();
          MEMORY.ref(4, a1-0x6L).setu(v0);
          s2 = CPU.MFC2(24);

          if((int)s2 > 0 || t0 != 0 && s2 != 0) {
            //LAB_800df4ac
            MEMORY.ref(4, s1).offset(0x8L).setu(CPU.MFC2(12));
            MEMORY.ref(4, s1).offset(0x14L).setu(CPU.MFC2(13));
            MEMORY.ref(4, s1).offset(0x20L).setu(CPU.MFC2(14));
            v0 = MEMORY.ref(2, a2).offset(0x8L).get();
            v0 = v0 << 3;
            v0 = vertices + v0;
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
            CPU.COP2(0x180001L);
            v0 = MEMORY.ref(4, a2-0x12L).get();
            MEMORY.ref(1, a1).offset(-0x23L).setu(s5);
            MEMORY.ref(4, a1+0x6L).setu(v0);
            s2 = CPU.CFC2(31);
            v0 = s1 + 0x2cL;
            MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(14));
            CPU.COP2(0x168002eL);
            v1 = MEMORY.ref(1, a2).offset(-0xeL).get();
            v0 = sp10;
            lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
            t1 = lo;
            v0 = (int)t1 >> 12;
            MEMORY.ref(1, a1).offset(-0x26L).setu(v0);
            v1 = MEMORY.ref(1, a2).offset(-0xdL).get();
            v0 = sp14;
            lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
            t1 = lo;
            v0 = (int)t1 >> 12;
            MEMORY.ref(1, a1).offset(-0x25L).setu(v0);
            v1 = MEMORY.ref(1, a2).offset(-0xcL).get();
            v0 = sp18;
            lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
            t1 = lo;
            v0 = (int)t1 >> 12;
            MEMORY.ref(1, a1).offset(-0x24L).setu(v0);
            v1 = MEMORY.ref(1, a2).offset(-0xaL).get();
            v0 = sp10;
            lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
            t1 = lo;
            v0 = (int)t1 >> 12;
            MEMORY.ref(1, a1).offset(-0x1aL).setu(v0);
            v1 = MEMORY.ref(1, a2).offset(-0x9L).get();
            v0 = sp14;
            lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
            t1 = lo;
            v0 = (int)t1 >> 12;
            MEMORY.ref(1, a1).offset(-0x19L).setu(v0);
            v1 = MEMORY.ref(1, a2).offset(-0x8L).get();
            v0 = sp18;
            lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
            t1 = lo;
            v0 = (int)t1 >> 12;
            MEMORY.ref(1, a1).offset(-0x18L).setu(v0);
            v1 = MEMORY.ref(1, a2).offset(-0x6L).get();
            v0 = sp10;
            lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
            t1 = lo;
            v0 = (int)t1 >> 12;
            MEMORY.ref(1, a1).offset(-0xeL).setu(v0);
            v1 = MEMORY.ref(1, a2).offset(-0x5L).get();
            v0 = sp14;
            lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
            t1 = lo;
            v0 = (int)t1 >> 12;
            MEMORY.ref(1, a1).offset(-0xdL).setu(v0);
            v1 = MEMORY.ref(1, a2).offset(-0x4L).get();
            v0 = sp18;
            lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
            t1 = lo;
            v0 = (int)t1 >> 12;
            MEMORY.ref(1, a1).offset(-0xcL).setu(v0);
            v1 = MEMORY.ref(1, a2).offset(-0x2L).get();
            v0 = sp10;
            lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
            t1 = lo;
            v0 = (int)t1 >> 12;
            MEMORY.ref(1, a1).offset(-0x2L).setu(v0);
            v1 = MEMORY.ref(1, a2).offset(-0x1L).get();
            v0 = sp14;
            lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
            t1 = lo;
            v0 = (int)t1 >> 12;
            MEMORY.ref(1, a1).offset(-0x1L).setu(v0);
            v1 = MEMORY.ref(1, a2).offset(0x0L).get();
            v0 = sp18;
            lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
            t1 = lo;
            v0 = (int)t1 >> 12;
            MEMORY.ref(1, a1).offset(0x0L).setu(v0);
            s0 = CPU.MFC2(7);
            v0 = 0x1f80_0000L;
            v0 = MEMORY.ref(4, v0).offset(0x3e8L).get();
            s0 = s0 + v0;
            s0 = (int)s0 >> 2;
            if(s0 >= 0xbL) {
              if(s0 >= 0xffeL) {
                s0 = 0xffeL;
              }
              a0 = s0 << 2;

              //LAB_800df684
              a0 = s7 + a0;
              a1 = a1 + 0x34L;
              v1 = MEMORY.ref(4, a0).offset(0x0L).get();
              v0 = 0xc00_0000L;
              v1 = v1 & a3;
              v1 = v1 | v0;
              v0 = s1 & a3;
              MEMORY.ref(4, s1).offset(0x0L).setu(v1);
              s1 = s1 + 0x34L;
              MEMORY.ref(4, a0).offset(0x0L).setu(v0);
            }
          }
        }

        //LAB_800df6ac
        a2 = a2 + 0x2cL;
        s3 = s3 + 0x2cL;
      } while(s4 != 0);
    }

    //LAB_800df6bc
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(s1);
    return s3;
  }

  @Method(0x800dfc5cL)
  public static long FUN_800dfc5c(final long primitives, final long vertices, final long normals, final long count) {
    long v0;
    long v1;
    long a0 = primitives;
    long a1 = vertices;
    long a2;
    long a3 = count;
    long t0;
    long t1;
    long t5;
    long t6;
    long t7;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long s6;
    long lo;
    s3 = a0;
    s5 = a1;
    s4 = a3;
    v0 = 0x1f80_0000L;
    v1 = 0x8006_0000L;
    s1 = MEMORY.ref(4, v0).offset(0x3d8L).get();
    v0 = 0x800c_0000L;
    v1 = v1 - 0x5c90L;
    a0 = MEMORY.ref(4, v0).offset(-0x4ef8L).get();
    v0 = a0 << 2;
    v0 = v0 + a0;
    v0 = v0 << 2;
    v0 = v0 + v1;
    s6 = MEMORY.ref(4, v0).offset(0x4L).get();
    final IntRef sp0x10 = new IntRef();
    final IntRef sp0x14 = new IntRef();
    final IntRef sp0x18 = new IntRef();
    FUN_800e4d74(sp0x10, sp0x14, sp0x18);
    long sp10 = sp0x10.get();
    long sp14 = sp0x14.get();
    long sp18 = sp0x18.get();
    v0 = 0x1f80_0000L;
    v0 = MEMORY.ref(2, v0).offset(0x3ecL).getSigned();
    if(s4 != 0) {
      t0 = v0 << 16;
      a3 = 0xff_0000L;
      a3 = a3 | 0xffffL;
      a2 = s3 + 0x22L;
      a1 = s1 + 0x2aL;

      //LAB_800dfce8
      do {
        t5 = MEMORY.ref(2, s3).offset(0x24L).get();
        t6 = MEMORY.ref(2, s3).offset(0x26L).get();
        t7 = MEMORY.ref(2, s3).offset(0x28L).get();
        t5 = t5 << 3;
        t6 = t6 << 3;
        t7 = t7 << 3;
        t5 = s5 + t5;
        t6 = s5 + t6;
        t7 = s5 + t7;
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);
        CPU.COP2(0x280030L);
        v1 = 0xff9f_0000L;
        v0 = MEMORY.ref(4, a2-0x1eL).get();
        v1 = v1 | 0xffffL;
        MEMORY.ref(4, a1-0x1eL).setu(v0);
        v0 = MEMORY.ref(4, a2-0x1aL).get();
        s4 = s4 - 0x1L;
        v0 = v0 & v1;
        v0 = v0 | t0;
        MEMORY.ref(4, a1-0x12L).setu(v0);
        s2 = CPU.CFC2(31);

        if((int)s2 >= 0) {
          CPU.COP2(0x1400006L);
          v0 = MEMORY.ref(4, a2-0x16L).get();

          MEMORY.ref(4, a1-0x6L).setu(v0);
          s2 = CPU.MFC2(24);

          if(s2 != 0) {
            MEMORY.ref(4, s1).offset(0x8L).setu(CPU.MFC2(12));
            MEMORY.ref(4, s1).offset(0x14L).setu(CPU.MFC2(13));
            MEMORY.ref(4, s1).offset(0x20L).setu(CPU.MFC2(14));
            v0 = MEMORY.ref(2, a2).offset(0x8L).get();
            v0 = v0 << 3;
            v0 = s5 + v0;
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
            CPU.COP2(0x180001L);
            a0 = 0x3e80_0000L;
            a0 = a0 | 0x8080L;
            v1 = MEMORY.ref(4, a2-0x12L).get();
            v0 = 0xcL;
            MEMORY.ref(1, a1).offset(-0x27L).setu(v0);
            MEMORY.ref(4, a1-0x26L).setu(a0);
            MEMORY.ref(4, a1+0x6L).setu(v1);
            s2 = CPU.CFC2(31);

            if((int)s2 >= 0) {
              v0 = s1 + 0x2cL;
              MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(14));
              CPU.COP2(0x168002eL);
              v1 = MEMORY.ref(1, a2).offset(-0xeL).get();
              v0 = sp10;
              lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
              t1 = lo;
              v0 = (int)t1 >> 12;
              MEMORY.ref(1, a1).offset(-0x26L).setu(v0);
              v1 = MEMORY.ref(1, a2).offset(-0xdL).get();
              v0 = sp14;
              lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
              t1 = lo;
              v0 = (int)t1 >> 12;
              MEMORY.ref(1, a1).offset(-0x25L).setu(v0);
              v1 = MEMORY.ref(1, a2).offset(-0xcL).get();
              v0 = sp18;
              lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
              t1 = lo;
              v0 = (int)t1 >> 12;
              MEMORY.ref(1, a1).offset(-0x24L).setu(v0);
              v1 = MEMORY.ref(1, a2).offset(-0xaL).get();
              v0 = sp10;
              lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
              t1 = lo;
              v0 = (int)t1 >> 12;
              MEMORY.ref(1, a1).offset(-0x1aL).setu(v0);
              v1 = MEMORY.ref(1, a2).offset(-0x9L).get();
              v0 = sp14;
              lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
              t1 = lo;
              v0 = (int)t1 >> 12;
              MEMORY.ref(1, a1).offset(-0x19L).setu(v0);
              v1 = MEMORY.ref(1, a2).offset(-0x8L).get();
              v0 = sp18;
              lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
              t1 = lo;
              v0 = (int)t1 >> 12;
              MEMORY.ref(1, a1).offset(-0x18L).setu(v0);
              v1 = MEMORY.ref(1, a2).offset(-0x6L).get();
              v0 = sp10;
              lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
              t1 = lo;
              v0 = (int)t1 >> 12;
              MEMORY.ref(1, a1).offset(-0xeL).setu(v0);
              v1 = MEMORY.ref(1, a2).offset(-0x5L).get();
              v0 = sp14;
              lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
              t1 = lo;
              v0 = (int)t1 >> 12;
              MEMORY.ref(1, a1).offset(-0xdL).setu(v0);
              v1 = MEMORY.ref(1, a2).offset(-0x4L).get();
              v0 = sp18;
              lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
              t1 = lo;
              v0 = (int)t1 >> 12;
              MEMORY.ref(1, a1).offset(-0xcL).setu(v0);
              v1 = MEMORY.ref(1, a2).offset(-0x2L).get();
              v0 = sp10;
              lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
              t1 = lo;
              v0 = (int)t1 >> 12;
              MEMORY.ref(1, a1).offset(-0x2L).setu(v0);
              v1 = MEMORY.ref(1, a2).offset(-0x1L).get();
              v0 = sp14;
              lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
              t1 = lo;
              v0 = (int)t1 >> 12;
              MEMORY.ref(1, a1).offset(-0x1L).setu(v0);
              v1 = MEMORY.ref(1, a2).offset(0x0L).get();
              v0 = sp18;
              lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
              t1 = lo;
              v0 = (int)t1 >> 12;
              MEMORY.ref(1, a1).offset(0x0L).setu(v0);
              s0 = CPU.MFC2(7);
              v0 = 0x1f80_0000L;
              v0 = MEMORY.ref(4, v0).offset(0x3e8L).get();
              s0 = s0 + v0;
              s0 = (int)s0 >> 2;
              if((int)s0 >= 0xbL) {
                if((int)s0 >= 0xffeL) {
                  s0 = 0xffeL;
                }
                a0 = s0 << 2;

                //LAB_800dff7c
                a0 = s6 + a0;
                a1 = a1 + 0x34L;
                v1 = MEMORY.ref(4, a0).offset(0x0L).get();
                v0 = 0xc00_0000L;
                v1 = v1 & a3;
                v1 = v1 | v0;
                v0 = s1 & a3;
                MEMORY.ref(4, s1).offset(0x0L).setu(v1);
                s1 = s1 + 0x34L;
                MEMORY.ref(4, a0).offset(0x0L).setu(v0);
              }
            }
          }
        }

        //LAB_800dffa4
        a2 = a2 + 0x2cL;
        s3 = s3 + 0x2cL;
      } while(s4 != 0);
    }

    //LAB_800dffb0
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(s1);
    return s3;
  }

  @Method(0x800dffe4L)
  public static long FUN_800dffe4(final long primitives, final long vertices, final long normals, final long count) {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long t0;
    long t1;
    long t5;
    long t6;
    long t7;
    long s0;
    long s1;
    long s2;
    long s5;
    long lo;
    long s3 = primitives;
    long s4 = count;
    v0 = 0x1f80_0000L;
    v1 = 0x8006_0000L;
    s1 = MEMORY.ref(4, v0).offset(0x3d8L).get();
    v0 = 0x800c_0000L;
    v1 = v1 - 0x5c90L;
    a0 = MEMORY.ref(4, v0).offset(-0x4ef8L).get();
    v0 = a0 << 2;
    v0 = v0 + a0;
    v0 = v0 << 2;
    v0 = v0 + v1;
    s5 = MEMORY.ref(4, v0).offset(0x4L).get();
    final IntRef sp0x10 = new IntRef();
    final IntRef sp0x14 = new IntRef();
    final IntRef sp0x18 = new IntRef();
    FUN_800e4d74(sp0x10, sp0x14, sp0x18);
    final long sp10 = sp0x10.get();
    final long sp14 = sp0x14.get();
    final long sp18 = sp0x18.get();
    v0 = 0x1f80_0000L;
    v0 = MEMORY.ref(2, v0).offset(0x3ecL).getSigned();
    if(s4 != 0) {
      t0 = v0 << 16;
      a3 = 0xff_0000L;
      a3 = a3 | 0xffffL;
      a2 = s3 + 0x1aL;
      a1 = s1 + 0x1eL;

      //LAB_800e0070
      do {
        t5 = MEMORY.ref(2, s3).offset(0x1cL).get();
        t6 = MEMORY.ref(2, s3).offset(0x1eL).get();
        t7 = MEMORY.ref(2, s3).offset(0x20L).get();
        t5 = t5 << 3;
        t6 = t6 << 3;
        t7 = t7 << 3;
        t5 = vertices + t5;
        t6 = vertices + t6;
        t7 = vertices + t7;
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);
        CPU.COP2(0x280030L);
        v1 = 0xff9f_0000L;
        v0 = MEMORY.ref(4, a2-0x16L).get();
        v1 = v1 | 0xffffL;
        MEMORY.ref(4, a1-0x12L).setu(v0);
        v0 = MEMORY.ref(4, a2-0x12L).get();
        s4 = s4 - 0x1L;
        v0 = v0 & v1;
        v0 = v0 | t0;
        MEMORY.ref(4, a1-0x6L).setu(v0);
        s2 = CPU.CFC2(31);

        if((int)s2 >= 0) {
          CPU.COP2(0x1400006L);
          v0 = MEMORY.ref(4, a2-0xeL).get();
          MEMORY.ref(4, a1+0x6L).setu(v0);
          s2 = CPU.MFC2(24);

          if(s2 != 0) {
            MEMORY.ref(4, s1).offset(0x8L).setu(CPU.MFC2(12));
            MEMORY.ref(4, s1).offset(0x14L).setu(CPU.MFC2(13));
            MEMORY.ref(4, s1).offset(0x20L).setu(CPU.MFC2(14));
            v1 = 0x3680_0000L;
            v1 = v1 | 0x8080L;
            v0 = 0x9L;
            MEMORY.ref(1, a1).offset(-0x1bL).setu(v0);
            MEMORY.ref(4, a1-0x1aL).setu(v1);
            s2 = CPU.CFC2(31);

            if((int)s2 >= 0) {
              CPU.COP2(0x158002dL);
              v1 = MEMORY.ref(1, a2).offset(-0xaL).get();
              v0 = sp10;
              lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
              t1 = lo;
              v0 = (int)t1 >> 12;
              MEMORY.ref(1, a1).offset(-0x1aL).setu(v0);
              v1 = MEMORY.ref(1, a2).offset(-0x9L).get();
              v0 = sp14;
              lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
              t1 = lo;
              v0 = (int)t1 >> 12;
              MEMORY.ref(1, a1).offset(-0x19L).setu(v0);
              v1 = MEMORY.ref(1, a2).offset(-0x8L).get();
              v0 = sp18;
              lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
              t1 = lo;
              v0 = (int)t1 >> 12;
              MEMORY.ref(1, a1).offset(-0x18L).setu(v0);
              v1 = MEMORY.ref(1, a2).offset(-0x6L).get();
              v0 = sp10;
              lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
              t1 = lo;
              v0 = (int)t1 >> 12;
              MEMORY.ref(1, a1).offset(-0xeL).setu(v0);
              v1 = MEMORY.ref(1, a2).offset(-0x5L).get();
              v0 = sp14;
              lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
              t1 = lo;
              v0 = (int)t1 >> 12;
              MEMORY.ref(1, a1).offset(-0xdL).setu(v0);
              v1 = MEMORY.ref(1, a2).offset(-0x4L).get();
              v0 = sp18;
              lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
              t1 = lo;
              v0 = (int)t1 >> 12;
              MEMORY.ref(1, a1).offset(-0xcL).setu(v0);
              v1 = MEMORY.ref(1, a2).offset(-0x2L).get();
              v0 = sp10;
              lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
              t1 = lo;
              v0 = (int)t1 >> 12;
              MEMORY.ref(1, a1).offset(-0x2L).setu(v0);
              v1 = MEMORY.ref(1, a2).offset(-0x1L).get();
              v0 = sp14;
              lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
              t1 = lo;
              v0 = (int)t1 >> 12;
              MEMORY.ref(1, a1).offset(-0x1L).setu(v0);
              v1 = MEMORY.ref(1, a2).offset(0x0L).get();
              v0 = sp18;
              lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
              t1 = lo;
              v0 = (int)t1 >> 12;
              MEMORY.ref(1, a1).offset(0x0L).setu(v0);
              s0 = CPU.MFC2(7);
              v0 = 0x1f80_0000L;
              v0 = MEMORY.ref(4, v0).offset(0x3e8L).get();
              s0 = s0 + v0;
              s0 = (int)s0 >> 2;
              if(s0 >= 0xbL) {
                if(s0 >= 0xffeL) {
                  s0 = 0xffeL;
                }
                a0 = s0 << 2;

                //LAB_800e0280
                a0 = s5 + a0;
                a1 = a1 + 0x28L;
                v1 = MEMORY.ref(4, a0).offset(0x0L).get();
                v0 = 0x900_0000L;
                v1 = v1 & a3;
                v1 = v1 | v0;
                v0 = s1 & a3;
                MEMORY.ref(4, s1).offset(0x0L).setu(v1);
                s1 = s1 + 0x28L;
                MEMORY.ref(4, a0).offset(0x0L).setu(v0);
              }
            }
          }
        }

        //LAB_800e02a8
        a2 = a2 + 0x24L;
        s3 = s3 + 0x24L;
      } while(s4 != 0);
    }

    //LAB_800e02b4
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(s1);
    return s3;
  }
}
