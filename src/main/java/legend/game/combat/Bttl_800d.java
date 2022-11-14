package legend.game.combat;

import legend.core.MathHelper;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandLine;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gte.COLOUR;
import legend.core.gte.DVECTOR;
import legend.core.gte.GsCOORD2PARAM;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.Tmd;
import legend.core.gte.TmdObjTable;
import legend.core.gte.TmdWithId;
import legend.core.gte.VECTOR;
import legend.core.memory.Memory;
import legend.core.memory.Method;
import legend.core.memory.types.CString;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.QuadConsumerRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.TriConsumerRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.game.combat.types.AdditionCharEffectData0c;
import legend.game.combat.types.AdditionScriptData1c;
import legend.game.combat.types.AdditionSparksEffect08;
import legend.game.combat.types.AdditionStarburstEffect10;
import legend.game.combat.types.BattleCamera;
import legend.game.combat.types.BattleObject27c;
import legend.game.combat.types.BattleScriptDataBase;
import legend.game.combat.types.BttlScriptData40;
import legend.game.combat.types.BttlScriptData6cSub13c;
import legend.game.combat.types.PotionEffect14;
import legend.game.combat.types.BttlStruct50;
import legend.game.combat.types.EffectManagerData6c;
import legend.game.combat.types.EffectManagerData6cInner;
import legend.game.combat.types.GuardEffect06;
import legend.game.combat.types.MonsterDeathEffect34;
import legend.game.combat.types.ProjectileHitEffect14;
import legend.game.combat.types.ProjectileHitEffect14Sub48;
import legend.game.types.Model124;
import legend.game.types.RunningScript;
import legend.game.types.ScriptState;
import legend.game.types.TmdAnimationFile;
import legend.game.types.Translucency;

import java.util.Arrays;

import static legend.core.Hardware.CPU;
import static legend.core.Hardware.GPU;
import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.Scus94491BpeSegment.FUN_80018a5c;
import static legend.game.Scus94491BpeSegment.FUN_80018d60;
import static legend.game.Scus94491BpeSegment._1f8003ec;
import static legend.game.Scus94491BpeSegment.allocateScriptState;
import static legend.game.Scus94491BpeSegment.ctmdGp0CommandId_1f8003ee;
import static legend.game.Scus94491BpeSegment.deallocateScriptAndChildren;
import static legend.game.Scus94491BpeSegment.free;
import static legend.game.Scus94491BpeSegment.loadScriptFile;
import static legend.game.Scus94491BpeSegment.mallocHead;
import static legend.game.Scus94491BpeSegment.mallocTail;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment.setScriptDestructor;
import static legend.game.Scus94491BpeSegment.setScriptTicker;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment_8002.FUN_800213c4;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021584;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021628;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021724;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022018;
import static legend.game.Scus94491BpeSegment_8002.SetGeomOffset;
import static legend.game.Scus94491BpeSegment_8002.SetRotMatrix;
import static legend.game.Scus94491BpeSegment_8002.SetTransMatrix;
import static legend.game.Scus94491BpeSegment_8002.SquareRoot0;
import static legend.game.Scus94491BpeSegment_8002.applyModelPartTransforms;
import static legend.game.Scus94491BpeSegment_8002.initObjTable2;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003eba0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003f210;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003f990;
import static legend.game.Scus94491BpeSegment_8003.GsGetLws;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_8003.GsSetRefView2;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_8003faf0;
import static legend.game.Scus94491BpeSegment_8003.ScaleMatrixL;
import static legend.game.Scus94491BpeSegment_8003.TransMatrix;
import static legend.game.Scus94491BpeSegment_8003.adjustTmdPointers;
import static legend.game.Scus94491BpeSegment_8003.getProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.getScreenOffset;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.setRotTransMatrix;
import static legend.game.Scus94491BpeSegment_8003.updateTmdPacketIlen;
import static legend.game.Scus94491BpeSegment_8004.RotMatrixX;
import static legend.game.Scus94491BpeSegment_8004.RotMatrixY;
import static legend.game.Scus94491BpeSegment_8004.RotMatrixZ;
import static legend.game.Scus94491BpeSegment_8004.RotMatrix_80040010;
import static legend.game.Scus94491BpeSegment_8004.doNothingScript_8004f650;
import static legend.game.Scus94491BpeSegment_8004.ratan2;
import static legend.game.Scus94491BpeSegment_800b._800bb0fc;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800c.matrix_800c3548;
import static legend.game.combat.Bttl_800c.scriptGetScriptedObjectPos;
import static legend.game.combat.Bttl_800c.FUN_800cf244;
import static legend.game.combat.Bttl_800c.FUN_800cf37c;
import static legend.game.combat.Bttl_800c.FUN_800cf4f4;
import static legend.game.combat.Bttl_800c.FUN_800cfb14;
import static legend.game.combat.Bttl_800c.FUN_800cffd8;
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
import static legend.game.combat.Bttl_800c._800c6948;
import static legend.game.combat.Bttl_800c._800c6d94;
import static legend.game.combat.Bttl_800c._800c6dac;
import static legend.game.combat.Bttl_800c._800fa76c;
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
import static legend.game.combat.Bttl_800c._800fac1c;
import static legend.game.combat.Bttl_800c._800fac3c;
import static legend.game.combat.Bttl_800c._800fac5c;
import static legend.game.combat.Bttl_800c._800fac7c;
import static legend.game.combat.Bttl_800c._800fac9c;
import static legend.game.combat.Bttl_800c._800facbc;
import static legend.game.combat.Bttl_800c._800fad1c;
import static legend.game.combat.Bttl_800c._800fad7c;
import static legend.game.combat.Bttl_800c._800fad9c;
import static legend.game.combat.Bttl_800c.additionNames_800fa8d4;
import static legend.game.combat.Bttl_800c.additionStarburstRenderers_800c6dc4;
import static legend.game.combat.Bttl_800c.asciiTable_800fa788;
import static legend.game.combat.Bttl_800c.camera_800c67f0;
import static legend.game.combat.Bttl_800c.charWidthAdjustTable_800fa7cc;
import static legend.game.combat.Bttl_800c.currentAddition_800c6790;
import static legend.game.combat.Bttl_800c.effectRenderers_800fa758;
import static legend.game.combat.Bttl_800c.screenOffsetX_800c67bc;
import static legend.game.combat.Bttl_800c.screenOffsetY_800c67c0;
import static legend.game.combat.Bttl_800c.seed_800fa754;
import static legend.game.combat.Bttl_800c.struct7cc_800c693c;
import static legend.game.combat.Bttl_800e.FUN_800e7ea4;
import static legend.game.combat.Bttl_800e.allocateEffectManager;
import static legend.game.combat.Bttl_800e.getLightColour;
import static legend.game.combat.Bttl_800e.renderCtmd;

public final class Bttl_800d {
  private Bttl_800d() { }

  @Method(0x800d0094L)
  public static void FUN_800d0094(final int scriptIndex, final int animIndex, final long a2) {
    final BattleObject27c v1 = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(BattleObject27c.class);

    final UnsignedIntRef a0;
    final int a3;
    if(animIndex < 32) {
      //LAB_800d00d0
      a0 = v1.model_148.ui_f4;
      a3 = animIndex;
    } else {
      a0 = v1.model_148.ui_f8;
      a3 = animIndex - 32;
    }

    //LAB_800d00d4
    if((a2 & 0xffL) != 0) {
      a0.set(~(1L << a3) & a0.get());
      return;
    }

    //LAB_800d0104
    a0.or(1L << a3);
  }

  @Method(0x800d0124L)
  public static long FUN_800d0124(final RunningScript script) {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0.get(script.params_20.get(0).deref().get()).deref();
    final BattleScriptDataBase data = state.innerStruct_00.derefAs(BattleScriptDataBase.class);

    if(data.magic_00.get() == BattleScriptDataBase.EM__) {
      script.params_20.get(1).deref().set(((EffectManagerData6c)data).effect_44.derefAs(BttlScriptData6cSub13c.class).model_10.animCount_98.get());
    } else {
      //LAB_800d017c
      script.params_20.get(1).deref().set(((BattleObject27c)data).model_148.animCount_98.get());
    }

    //LAB_800d0194
    return 0;
  }

  @Method(0x800d019cL)
  public static void renderProjectileHitEffect(final int scriptIndex, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    int a0 = 0;
    final ProjectileHitEffect14 effect = data.effect_44.derefAs(ProjectileHitEffect14.class);

    //LAB_800d01ec
    for(int s7 = 0; s7 < effect.count_00.get(); s7++) {
      final ProjectileHitEffect14Sub48 s4 = effect._08.deref().get(s7);

      if(s4.used_00.get()) {
        s4._40.incr();
        s4.frames_44.decr();

        if(s4.frames_44.get() == 0) {
          s4.used_00.set(false);
        }

        //LAB_800d0220
        s4.r_34.sub(s4.fadeR_3a.get());
        s4.g_36.sub(s4.fadeG_3c.get());
        s4.b_38.sub(s4.fadeB_3e.get());

        //LAB_800d0254
        final ShortRef[] x = {new ShortRef(), new ShortRef()};
        final ShortRef[] y = {new ShortRef(), new ShortRef()};
        for(int s3 = 0; s3 < 2; s3++) {
          final VECTOR s1 = s4._04.get(s3);
          final SVECTOR a1 = s4._24.get(s3);
          a0 = FUN_800cfb14(data, s1, x[s3], y[s3]);
          s1.add(a1);
          a1.y.add((short)25);

          if(a1.getX() > 10) {
            a1.x.sub((short)10);
          }

          //LAB_800d0308
          if(s1.getY() + data._10.trans_04.getY() >= 0) {
            s1.setY(-data._10.trans_04.getY());
            a1.y.neg();
          }

          //LAB_800d033c
        }

        int s1_0 = a0 >> 2;
        if(s1_0 >= 0x140) {
          if(s1_0 >= 0xffe) {
            s1_0 = 0xffe;
          }

          //LAB_800d037c
          int a2_0 = data._10.z_22.get();
          final int v1 = s1_0 + a2_0;
          if(v1 >= 0xa0) {
            if(v1 >= 0xffe) {
              a2_0 = 0xffe - s1_0;
            }

            //LAB_800d0444
            GPU.queueCommand(s1_0 + a2_0 >> 2, new GpuCommandLine()
              .translucent(Translucency.B_PLUS_F)
              .monochrome(0, 0)
              .rgb(1, s4.r_34.get() >>> 8, s4.g_36.get() >>> 8, s4.b_38.get() >>> 8)
              .pos(0, x[0].get(), y[0].get())
              .pos(1, x[1].get(), y[1].get())
            );
          }

          //LAB_800d0460
        }
      }
    }

    //LAB_800d0508
  }

  @Method(0x800d0538L)
  public static void deallocateProjectileHitEffect(final int scriptIndex, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    free(manager.effect_44.derefAs(ProjectileHitEffect14.class)._08.getPointer());
  }

  @Method(0x800d0564L)
  public static long allocateProjectileHitEffect(final RunningScript script) {
    final int scriptIndex = allocateEffectManager(
      script.scriptStateIndex_00.get(),
      0x14,
      null,
      MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "renderProjectileHitEffect", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "deallocateProjectileHitEffect", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      ProjectileHitEffect14::new
    );

    final ProjectileHitEffect14 effect = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class).effect_44.derefAs(ProjectileHitEffect14.class);

    final int count = script.params_20.get(1).deref().get();
    effect.count_00.set(count);
    effect._04.set(0);
    effect._08.setPointer(mallocTail(count * 0x48));

    //LAB_800d0634
    for(int i = 0; i < count; i++) {
      final ProjectileHitEffect14Sub48 struct = effect._08.deref().get(i);

      struct.used_00.set(true);
      struct.r_34.set(script.params_20.get(2).deref().get() << 8);
      struct.g_36.set(script.params_20.get(3).deref().get() << 8);
      struct.b_38.set(script.params_20.get(4).deref().get() << 8);

      final short x = (short)(seed_800fa754.advance().get() % 301 + 200);
      final short y = (short)(seed_800fa754.advance().get() % 401 - 300);
      final short z = (short)(seed_800fa754.advance().get() % 601 - 300);
      struct._24.get(0).set(x, y, z);
      struct._24.get(1).set(x, y, z);

      struct._04.get(0).setX(0);
      struct._04.get(0).setY((int)(seed_800fa754.advance().get() % 101 - 50));
      struct._04.get(0).setZ((int)(seed_800fa754.advance().get() % 101 - 50));
      struct.frames_44.set((int)(seed_800fa754.advance().get() % 9 + 7));

      struct._40.set(0);
      struct._24.get(1).y.add((short)25);
      struct._04.get(1).set(struct._04.get(0)).add(struct._24.get(0));
      struct.fadeR_3a.set(struct.r_34.get() / struct.frames_44.get());
      struct.fadeG_3c.set(struct.g_36.get() / struct.frames_44.get());
      struct.fadeB_3e.set(struct.b_38.get() / struct.frames_44.get());
    }

    //LAB_800d0980
    script.params_20.get(0).deref().set(scriptIndex);
    return 0;
  }

  @Method(0x800d09c0L)
  public static void FUN_800d09c0(final EffectManagerData6c a0, final long a1) {
    FUN_800cf4f4(a0, null, MEMORY.ref(4, a1 + 0x08L, VECTOR::new), MEMORY.ref(4, a1 + 0x08L, VECTOR::new));
    FUN_800cf37c(a0, null, MEMORY.ref(4, a1 + 0x28L, VECTOR::new), MEMORY.ref(4, a1 + 0x28L, VECTOR::new));
    MEMORY.ref(4, a1).offset(0x18L).setu(MEMORY.ref(4, a1).offset(0x08L).get());
    MEMORY.ref(4, a1).offset(0x1cL).setu(MEMORY.ref(4, a1).offset(0x0cL).get());
    MEMORY.ref(4, a1).offset(0x20L).setu(MEMORY.ref(4, a1).offset(0x10L).get());
  }

  @Method(0x800d0a30L)
  public static void renderAdditionSparks(final int scriptIndex, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    int s7 = 0;
    long s3 = 0;
    final AdditionSparksEffect08 s6 = data.effect_44.derefAs(AdditionSparksEffect08.class);
    long s2 = s6._04.get(); //TODO

    //LAB_800d0a7c
    for(int s5 = 0; s5 < s6.count_00.get(); s5++) {
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
        final int s1 = FUN_800cf244(MEMORY.ref(4, s2 + 0x8L, VECTOR::new), sp0x18, sp0x1c);
        FUN_800cf244(MEMORY.ref(4, s2 + 0x18L, VECTOR::new), sp0x20, sp0x24);

        if(s3 == 0) {
          s7 = (short)s1 >> 2;
        }

        //LAB_800d0b3c
        MEMORY.ref(4, s2).offset(0x2cL).addu(MEMORY.ref(2, s2).offset(0x3aL).getSigned());
        MEMORY.ref(4, s2).offset(0x28L).addu(MEMORY.ref(2, s2).offset(0x38L).getSigned());
        MEMORY.ref(4, s2).offset(0x30L).addu(MEMORY.ref(2, s2).offset(0x3cL).getSigned());

        if((int)MEMORY.ref(4, s2).offset(0x0cL).get() > 0) {
          MEMORY.ref(4, s2).offset(0x2cL).setu(-MEMORY.ref(4, s2).offset(0x2cL).get() >> 1);
        }

        //LAB_800d0b88
        int a3 = data._10.z_22.get();
        final int v1 = s7 + a3;
        if(v1 >= 0xa0) {
          if(v1 >= 0xffe) {
            a3 = 0xffe - s7;
          }

          final GpuCommandLine cmd = new GpuCommandLine()
            .translucent(Translucency.B_PLUS_F)
            .rgb(0, (int)MEMORY.ref(2, s2).offset(0x40L).get() >>> 8, (int)MEMORY.ref(2, s2).offset(0x42L).get() >>> 8, (int)MEMORY.ref(2, s2).offset(0x44L).get() >>> 8)
            .rgb(1, (int)MEMORY.ref(2, s2).offset(0x40L).get() >>> 9, (int)MEMORY.ref(2, s2).offset(0x42L).get() >>> 9, (int)MEMORY.ref(2, s2).offset(0x44L).get() >>> 9)
            .pos(0, sp0x18.get(), sp0x1c.get())
            .pos(1, sp0x20.get(), sp0x24.get());

          //LAB_800d0c84
          GPU.queueCommand(s7 + a3 >> 2, cmd);
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
    //LAB_800d0d94
  }

  @Method(0x800d0dc0L)
  public static void deallocateAdditionSparksEffect(final int scriptIndex, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    free(data.effect_44.derefAs(AdditionSparksEffect08.class)._04.get());
  }

  @Method(0x800d0decL)
  public static long allocateAdditionSparksEffect(final RunningScript s3) {
    final int count = s3.params_20.get(1).deref().get();
    final int s4 = s3.params_20.get(6).deref().get();

    final int scriptIndex = allocateEffectManager(
      s3.scriptStateIndex_00.get(),
      0x8L,
      null,
      MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "renderAdditionSparks", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "deallocateAdditionSparksEffect", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      AdditionSparksEffect08::new
    );

    final AdditionSparksEffect08 effect = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class).effect_44.derefAs(AdditionSparksEffect08.class);

    long t6 = mallocTail(count * 0x4cL);
    effect._04.set(t6);
    effect.count_00.set(count);

    final long s1 = s3.params_20.get(5).deref().get() / s4;

    //LAB_800d0ee0
    for(int i = 0; i < count; i++) {
      MEMORY.ref(4, t6).offset(0x0L).setu(0);

      MEMORY.ref(1, t6).offset(0x4L).setu(seed_800fa754.advance().get() % (s4 + 0x1L));
      MEMORY.ref(1, t6).offset(0x5L).setu(seed_800fa754.advance().get() % 9 + 7);

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
      MEMORY.ref(4, t6).offset(0x28L).setu(seed_800fa754.advance().get() % 201);
      MEMORY.ref(4, t6).offset(0x2cL).setu(seed_800fa754.advance().get() % 201 - 100);
      MEMORY.ref(4, t6).offset(0x30L).setu(seed_800fa754.advance().get() % 201 - 100);
      MEMORY.ref(2, t6).offset(0x3aL).setu(0xfL);
      MEMORY.ref(2, t6).offset(0x3cL).setu(0);
      t6 = t6 + 0x4cL;
    }

    //LAB_800d1154
    s3.params_20.get(0).deref().set(scriptIndex);
    return 0;
  }

  @Method(0x800d1194L)
  public static void FUN_800d1194(final EffectManagerData6c a0, final AdditionStarburstEffect10 a1, final IntRef[] a2) {
    if(a1.scriptIndex_00.get() == -1) {
      a2[0].set(0);
      a2[1].set(0);
    } else {
      //LAB_800d11c4
      final VECTOR sp0x10 = new VECTOR();
      scriptGetScriptedObjectPos(a1.scriptIndex_00.get(), sp0x10);
      sp0x10.add(a0._10.trans_04);
      FUN_800cf244(sp0x10, a2[0], a2[1]);
    }

    //LAB_800d120c
  }

  @Method(0x800d1220L)
  public static void renderAdditionHitStarburst(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final int[] sp0x48 = {-16, 16};
    final AdditionStarburstEffect10 s7 = data.effect_44.derefAs(AdditionStarburstEffect10.class);
    long s6 = s7._0c.get();

    //LAB_800d128c
    for(int fp = 0; fp < s7.count_04.get(); fp++) {
      if(MEMORY.ref(1, s6).offset(0x0L).get() != 0) {
        //LAB_800d12a4
        for(int s5 = 0; s5 < 2; s5++) {
          int s1 = sp0x48[s5] + (int)MEMORY.ref(2, s6).offset(0xaL).get();
          int s0 = 30 + (int)MEMORY.ref(2, s6).offset(0x6L).get();
          int sp18 = rcos(MEMORY.ref(2, s6).offset(0x2L).getSigned() + s1) * s0 >> 12;
          int sp28 = rsin(MEMORY.ref(2, s6).offset(0x2L).getSigned() + s1) * s0 >> 12;
          int sp20 = rcos(MEMORY.ref(2, s6).offset(0x2L).getSigned()) * s0 >> 12;
          int sp30 = rsin(MEMORY.ref(2, s6).offset(0x2L).getSigned()) * s0 >> 12;
          s1 = sp0x48[s5] + (int)MEMORY.ref(2, s6).offset(0xaL).get();
          s0 = 210 + (int)MEMORY.ref(2, s6).offset(0x6L).get();
          final int sp1c = rcos(MEMORY.ref(2, s6).offset(0x2L).getSigned() + s1) * s0 >> 12;
          final int sp2c = rsin(MEMORY.ref(2, s6).offset(0x2L).getSigned() + s1) * s0 >> 12;
          final int sp24 = rcos(MEMORY.ref(2, s6).offset(0x2L).getSigned()) * s0 >> 12;
          final int sp34 = rsin(MEMORY.ref(2, s6).offset(0x2L).getSigned()) * s0 >> 12;
          final IntRef[] sp0x50 = {new IntRef(), new IntRef()};
          FUN_800d1194(data, s7, sp0x50);
          sp18 = sp18 + sp0x50[0].get();
          sp28 = sp28 + sp0x50[1].get();
          sp20 = sp20 + sp0x50[0].get();
          sp30 = sp30 + sp0x50[1].get();

          GPU.queueCommand(30, new GpuCommandPoly(4)
            .translucent(Translucency.B_PLUS_F)
            .monochrome(0, 0)
            .rgb(1, data._10.colour_1c.getX(), data._10.colour_1c.getY(), data._10.colour_1c.getZ())
            .monochrome(2, 0)
            .rgb(3, 0)
            .pos(0, sp1c, sp2c)
            .pos(1, sp24, sp34)
            .pos(2, sp18, sp28)
            .pos(3, sp20, sp30)
          );
        }
      }

      //LAB_800d1538
      s6 = s6 + 0x10L;
    }

    //LAB_800d1558
  }

  @Method(0x800d15d8L)
  public static void renderAdditionCompletedStarburst(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final AdditionStarburstEffect10 sp80 = data.effect_44.derefAs(AdditionStarburstEffect10.class);
    long sp84 = sp80._0c.get();

    final int[] sp0x18 = new int[3];
    final int[] sp0x28 = new int[3];

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
            final int s0 = (int)Math.max(0, _800c6d94.offset(s7 * 0x6L).offset(s4 * 0x2L).get() - MEMORY.ref(2, sp84).offset(0x6L).get());

            //LAB_800d1784
            final long s2 = _800c6dac.offset(s7 * 0x6L).offset(s4 * 0x2L).getAddress();
            sp0x18[s4] = (rcos(MEMORY.ref(2, sp84).offset(0x2L).getSigned() + MEMORY.ref(2, s2).offset(0x0L).getSigned()) * s0 >> 12) + sp0x38[0].get();
            sp0x28[s4] = (rsin(MEMORY.ref(2, sp84).offset(0x2L).getSigned() + MEMORY.ref(2, s2).offset(0x0L).getSigned()) * s0 >> 12) + sp0x38[1].get();
          }

          GPU.queueCommand(30, new GpuCommandPoly(3)
            .translucent(Translucency.B_PLUS_F)
            .monochrome(0, 0)
            .monochrome(1, 0)
            .rgb(2, data._10.colour_1c.getX(), data._10.colour_1c.getY(), data._10.colour_1c.getZ())
            .pos(0, sp0x18[0], sp0x28[0])
            .pos(1, sp0x18[1], sp0x28[1])
            .pos(2, sp0x18[2], sp0x28[2])
          );
        }
      }

      //LAB_800d190c
      sp84 = sp84 + 0x10L;
    }

    //LAB_800d1940
  }

  @Method(0x800d19c0L)
  public static void deallocateAdditionStarburstEffect(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    free(data.effect_44.derefAs(AdditionStarburstEffect10.class)._0c.get());
  }

  @Method(0x800d19ecL)
  public static long allocateAdditionStarburstEffect(final RunningScript s3) {
    final int count = s3.params_20.get(2).deref().get();

    final int scriptIndex = allocateEffectManager(
      s3.scriptStateIndex_00.get(),
      0x10L,
      null,
      additionStarburstRenderers_800c6dc4.get(s3.params_20.get(3).deref().get()).deref(),
      MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "deallocateAdditionStarburstEffect", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      AdditionStarburstEffect10::new
    );

    final AdditionStarburstEffect10 effect = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class).effect_44.derefAs(AdditionStarburstEffect10.class);
    long t4 = mallocTail(count * 0x10L);
    effect.scriptIndex_00.set(s3.params_20.get(1).deref().get());
    effect.count_04.set(count);
    effect._08.set(0);
    effect._0c.set(t4);

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

  @Method(0x800d1cacL)
  public static long FUN_800d1cac(final RunningScript a0) {
    a0.params_20.get(0).deref().set(allocateEffectManager(a0.scriptStateIndex_00.get(), 0, null, null, null, null));
    return 0;
  }

  @Method(0x800d1cf4L)
  public static long FUN_800d1cf4(final RunningScript a0) {
    a0.params_20.get(0).deref().set(allocateEffectManager(a0.scriptStateIndex_00.get(), 0, null, null, null, null));
    return 0;
  }

  @Method(0x800d1d3cL)
  public static void FUN_800d1d3c(final EffectManagerData6c manager, final int angle, final short[] vertices, final PotionEffect14 effect, final Translucency translucency) {
    if((int)manager._10._00.get() >= 0) {
      GPU.queueCommand(effect.z_04.get() + manager._10.z_22.get() >> 2, new GpuCommandPoly(3)
        .translucent(translucency)
        .rgb(0, manager._10.colour_1c.getX(), manager._10.colour_1c.getY(), manager._10.colour_1c.getZ())
        .rgb(1, effect._0c.get(), effect._0d.get(), effect._0e.get())
        .rgb(2, effect._0c.get(), effect._0d.get(), effect._0e.get())
        .pos(0, vertices[0], vertices[1])
        .pos(1, vertices[2], vertices[3])
        .pos(2, vertices[4], vertices[5])
      );
    }

    //LAB_800d1e70
  }

  @Method(0x800d21b8L)
  public static void FUN_800d21b8(final EffectManagerData6c manager, final int angle, final short[] vertices, final PotionEffect14 effect, final Translucency translucency) {
    if((int)manager._10._00.get() >= 0) {
      final VECTOR sp0x20 = new VECTOR().set(
        rcos(angle) * (manager._10.scale_16.getX() / effect._01.get() + manager._10.vec_28.getX()) >> 12,
        rsin(angle) * (manager._10.scale_16.getY() / effect._01.get() + manager._10.vec_28.getX()) >> 12, // X is correct
        manager._10.vec_28.getY()
      );

      final ShortRef sp0x10 = new ShortRef();
      final ShortRef sp0x14 = new ShortRef();
      FUN_800cfb14(manager, sp0x20, sp0x10, sp0x14);

      final VECTOR sp0x30 = new VECTOR().set(
        rcos(angle + effect.angleStep_08.get()) * (manager._10.scale_16.getX() / effect._01.get() + manager._10.vec_28.getX()) >> 12,
        rsin(angle + effect.angleStep_08.get()) * (manager._10.scale_16.getY() / effect._01.get() + manager._10.vec_28.getX()) >> 12,
        manager._10.vec_28.getY()
      );

      final ShortRef sp0x18 = new ShortRef();
      final ShortRef sp0x1c = new ShortRef();
      FUN_800cfb14(manager, sp0x30, sp0x18, sp0x1c);

      GPU.queueCommand(effect.z_04.get() + manager._10.z_22.get() >> 2, new GpuCommandPoly(4)
        .translucent(translucency)
        .rgb(0, manager._10.colour_1c.getX(), manager._10.colour_1c.getY(), manager._10.colour_1c.getZ())
        .rgb(1, manager._10.colour_1c.getX(), manager._10.colour_1c.getY(), manager._10.colour_1c.getZ())
        .rgb(2, effect._0c.get(), effect._0d.get(), effect._0e.get())
        .rgb(3, effect._0c.get(), effect._0d.get(), effect._0e.get())
        .pos(0, sp0x10.get(), sp0x14.get())
        .pos(1, sp0x18.get(), sp0x1c.get())
        .pos(2, vertices[2], vertices[3])
        .pos(3, vertices[4], vertices[5])
      );
    }

    //LAB_800d2460
  }

  @Method(0x800d247cL)
  public static void renderPotionEffect(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final PotionEffect14 effect = manager.effect_44.derefAs(PotionEffect14.class);
    effect.angleStep_08.set(0x1000 / (0x4 << effect._00.get()));

    final ShortRef sp0x48 = new ShortRef();
    final ShortRef sp0x4c = new ShortRef();
    effect.z_04.set(FUN_800cfb14(manager, new VECTOR(), sp0x48, sp0x4c) >> 2);

    final int z = effect.z_04.get() + manager._10.z_22.get();
    if(z >= 0xa0) {
      if(z >= 0xffe) {
        effect.z_04.set(0xffe - manager._10.z_22.get());
      }

      //LAB_800d2510
      final VECTOR sp0x38 = new VECTOR().set(
        rcos(0) * (manager._10.scale_16.getX() / effect._01.get()) >> 12,
        rsin(0) * (manager._10.scale_16.getY() / effect._01.get()) >> 12,
        0
      );

      final ShortRef sp0x58 = new ShortRef();
      final ShortRef sp0x5c = new ShortRef();
      FUN_800cfb14(manager, sp0x38, sp0x58, sp0x5c);
      effect._0c.set((int)(manager._10._24.get() >>> 16 & 0xff));
      effect._0d.set((int)(manager._10._24.get() >>>  8 & 0xff));
      effect._0e.set((int)(manager._10._24.get()        & 0xff));

      //LAB_800d25b4
      for(int angle = 0; angle < 0x1000; ) {
        final ShortRef sp0x50 = new ShortRef().set(sp0x58.get());
        final ShortRef sp0x54 = new ShortRef().set(sp0x5c.get());

        sp0x38.set(
          rcos(angle + effect.angleStep_08.get()) * (manager._10.scale_16.getX() / effect._01.get()) >> 12,
          rsin(angle + effect.angleStep_08.get()) * (manager._10.scale_16.getY() / effect._01.get()) >> 12,
          0
        );

        FUN_800cfb14(manager, sp0x38, sp0x58, sp0x5c);
        effect.renderer_10.deref().run(manager, angle, new short[] {sp0x48.get(), sp0x4c.get(), sp0x50.get(), sp0x54.get(), sp0x58.get(), sp0x5c.get()}, effect, (manager._10._00.get() & 0x1000_0000L) != 0 ? Translucency.B_PLUS_F : Translucency.B_MINUS_F);
        angle += effect.angleStep_08.get();
      }
    }

    //LAB_800d2710
  }

  @Method(0x800d272cL)
  public static void deallocatePotionEffect(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    // no-op
  }

  @Method(0x800d2734L)
  public static long allocatePotionEffect(final RunningScript script) {
    final int s2 = script.params_20.get(1).deref().get();
    final int s1 = script.params_20.get(2).deref().get();

    final int effectIndex = allocateEffectManager(
      script.scriptStateIndex_00.get(),
      0x14,
      null,
      MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "renderPotionEffect", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "deallocatePotionEffect", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      PotionEffect14::new
    );

    final EffectManagerData6c manager = scriptStatePtrArr_800bc1c0.get(effectIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class);

    //LAB_800d27b4
    manager._10.scale_16.set((short)0x1000, (short)0x1000, (short)0x1000);

    final PotionEffect14 effect = manager.effect_44.derefAs(PotionEffect14.class);
    effect._00.set(s2);
    effect._01.set(s1 - 3 > 1 ? 4 : 1);
    effect.renderer_10.set(effectRenderers_800fa758.get(s1).deref());
    script.params_20.get(0).deref().set(effectIndex);
    return 0;
  }

  @Method(0x800d2810L)
  public static void renderGuardEffect(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final VECTOR sp0x58 = new VECTOR();
    final IntRef[] sp0x18 = new IntRef[7];
    final IntRef[] sp0x38 = new IntRef[7];

    Arrays.setAll(sp0x18, i -> new IntRef());
    Arrays.setAll(sp0x38, i -> new IntRef());

    final GuardEffect06 s7 = data.effect_44.derefAs(GuardEffect06.class);
    s7._02.incr();
    s7._04.add((short)0x400);

    //LAB_800d2888
    int s3 = 0;
    for(int i = 6; i >= 0; i--) {
      //LAB_800d289c
      final long s1 = _800fa76c.offset(i * 0x4L).getAddress();
      sp0x58.setX(data._10.trans_04.getX() + (i != 0 ? data._10.scale_16.getX() / 4 : 0));
      sp0x58.setY((int)(data._10.trans_04.getY() + (MEMORY.ref(2, s1).offset(0x2L).getSigned() * data._10.scale_16.getY() >> 12)));
      sp0x58.setZ((int)(data._10.trans_04.getZ() + (MEMORY.ref(2, s1).offset(0x0L).getSigned() * data._10.scale_16.getZ() >> 12)));
      s3 = FUN_800cf244(sp0x58, sp0x18[i], sp0x38[i]);
    }

    s3 = s3 >> 2;
    int sp78 = MathHelper.clamp(data._10.colour_1c.getX() - 1 << 8, 0, 0x8000) >>> 7;
    int sp7a = MathHelper.clamp(data._10.colour_1c.getY() - 1 << 8, 0, 0x8000) >>> 7;
    int sp7c = MathHelper.clamp(data._10.colour_1c.getZ() - 1 << 8, 0, 0x8000) >>> 7;
    sp78 = Math.min((sp78 + sp7a + sp7c) / 3 * 2, 0xff);

    //LAB_800d2a80
    //LAB_800d2a9c
    for(int i = 0; i < 5; i++) {
      int a0 = data._10.z_22.get();
      final int v1 = s3 + a0;
      if(v1 >= 0xa0) {
        if(v1 >= 0xffe) {
          a0 = 0xffe - s3;
        }

        //LAB_800d2bc0
        // Main part of shield effect
        GPU.queueCommand(s3 + a0 >> 2, new GpuCommandPoly(3)
          .translucent(Translucency.B_PLUS_F)
          .pos(0, sp0x18[i + 1].get(), sp0x38[i + 1].get())
          .pos(1, sp0x18[i + 2].get(), sp0x38[i + 2].get())
          .pos(2, sp0x18[0    ].get(), sp0x38[0    ].get())
          .rgb(0, data._10.colour_1c.getX(), data._10.colour_1c.getY(), data._10.colour_1c.getZ())
          .rgb(1, data._10.colour_1c.getX(), data._10.colour_1c.getY(), data._10.colour_1c.getZ())
          .monochrome(2, sp78)
        );
      }
    }

    //LAB_800d2c78
    int s6 = 0x1000;
    sp78 = data._10.colour_1c.getX();
    sp7a = data._10.colour_1c.getY();
    sp7c = data._10.colour_1c.getZ();
    final int sp80 = sp78 >>> 2;
    final int sp82 = sp7a >>> 2;
    final int sp84 = sp7c >>> 2;

    //LAB_800d2cfc
    int s7_0 = 0;
    for(int i = 0; i < 4; i++) {
      s6 = s6 + s7._04.get() / 4;
      s7_0 = s7_0 + data._10.scale_16.getX() / 4;
      sp78 = sp78 - sp80;
      sp7a = sp7a - sp82;
      sp7c = sp7c - sp84;

      //LAB_800d2d4c
      for(int n = 1; n < 7; n++) {
        final long s1 = _800fa76c.offset(n * 0x4L).getAddress();
        sp0x58.setX(s7_0 + data._10.trans_04.getX());
        sp0x58.setY((((int)MEMORY.ref(2, s1).offset(0x2L).getSigned() * data._10.scale_16.getY() >> 12) * s6 >> 12) + data._10.trans_04.getY());
        sp0x58.setZ((((int)MEMORY.ref(2, s1).offset(0x0L).getSigned() * data._10.scale_16.getZ() >> 12) * s6 >> 12) + data._10.trans_04.getZ());
        s3 = FUN_800cf244(sp0x58, sp0x18[n], sp0x38[n]) >> 2;
      }

      //LAB_800d2e20
      for(int n = 0; n < 5; n++) {
        int a0 = data._10.z_22.get();
        final int v1 = s3 + a0;
        if(v1 >= 0xa0) {
          if(v1 >= 0xffe) {
            a0 = 0xffe - s3;
          }

          //LAB_800d2ee8
          // Radiant lines of shield effect
          GPU.queueCommand(s3 + a0 >> 2, new GpuCommandLine()
            .translucent(Translucency.B_PLUS_F)
            .pos(0, sp0x18[n + 1].get(), sp0x38[n + 1].get())
            .pos(1, sp0x18[n + 2].get(), sp0x38[n + 2].get())
            .rgb(sp78, sp7a, sp7c)
          );
        }
      }

      //LAB_800d2fa4
    }
  }

  @Method(0x800d2fecL)
  public static void deallocateGuardEffect(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    // No-op
  }

  @Method(0x800d2ff4L)
  public static long allocateGuardEffect(final RunningScript s0) {
    final int scriptIndex = allocateEffectManager(
      s0.scriptStateIndex_00.get(),
      0x6,
      null,
      MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "renderGuardEffect", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "deallocateGuardEffect", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      GuardEffect06::new
    );

    final EffectManagerData6c manager = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    final GuardEffect06 effect = manager.effect_44.derefAs(GuardEffect06.class);
    effect._00.set(1);
    effect._02.set(0);
    effect._04.set((short)0);
    manager._10.colour_1c.setX((short)255);
    manager._10.colour_1c.setY((short)0);
    manager._10.colour_1c.setZ((short)0);
    s0.params_20.get(0).deref().set(scriptIndex);
    return 0;
  }

  @Method(0x800d3090L)
  public static long FUN_800d3090(final RunningScript script) {
    return 0;
  }

  @Method(0x800d3098L)
  public static long FUN_800d3098(final RunningScript script) {
    return 0;
  }

  @Method(0x800d30a0L)
  public static long FUN_800d30a0(final RunningScript script) {
    return 0;
  }

  @Method(0x800d30a8L)
  public static long FUN_800d30a8(final RunningScript script) {
    return 0;
  }

  @Method(0x800d30b0L)
  public static long FUN_800d30b0(final RunningScript script) {
    return 0;
  }

  @Method(0x800d30b8L)
  public static long FUN_800d30b8(final RunningScript script) {
    return 0;
  }

  @Method(0x800d30c0L)
  public static void monsterDeathEffectRenderer(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final MonsterDeathEffect34 s1 = data.effect_44.derefAs(MonsterDeathEffect34.class);
    long s2 = s1.ptr_30.get();

    //LAB_800d30fc
    for(int animIndex = 0; animIndex < s1.animCount_04.get(); animIndex++) {
      if(MEMORY.ref(1, s2).offset(0x0L).getSigned() == 0x1L) {
        s1._0c.r_14.set((int)(MEMORY.ref(2, s2).offset(0x24L).get() >>> 8));
        s1._0c.g_15.set((int)(MEMORY.ref(2, s2).offset(0x26L).get() >>> 8));
        s1._0c.b_16.set((int)(MEMORY.ref(2, s2).offset(0x28L).get() >>> 8));
        s1._0c.rotation_20.set((int)MEMORY.ref(4, s2).offset(0x0cL).get());
        s1._0c._1c.set((short)(data._10.scale_16.getX() + MEMORY.ref(2, s2).offset(0x04L).get()));
        s1._0c._1e.set((short)(data._10.scale_16.getY() + MEMORY.ref(2, s2).offset(0x04L).get()));
        FUN_800e7ea4(s1._0c, MEMORY.ref(4, s2 + 0x14L, VECTOR::new));
      }

      //LAB_800d3174
      s2 = s2 + 0x30L;
    }

    //LAB_800d3190
  }

  @Method(0x800d31b0L)
  public static void monsterDeathEffectTicker(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final MonsterDeathEffect34 s1 = data.effect_44.derefAs(MonsterDeathEffect34.class);

    s1._02.decr();
    if(s1._02.get() == 0) {
      deallocateScriptAndChildren(index);
    } else {
      //LAB_800d320c
      long s2 = s1.ptr_30.get();
      s1._00.add((short)2);

      //LAB_800d322c
      for(int animIndex = 0; animIndex < s1.animCount_04.get(); animIndex++) {
        if(s1._00.get() >= animIndex + 1 && MEMORY.ref(1, s2).offset(0x0L).getSigned() == -1) {
          MEMORY.ref(1, s2).offset(0x0L).setu(0x1L);
          MEMORY.ref(1, s2).offset(0x1L).setu(0x8L);
          MEMORY.ref(4, s2).offset(0x10L).setu(0);
          MEMORY.ref(4, s2).offset(0x04L).setu(0);
          MEMORY.ref(4, s2).offset(0x0cL).setu(seed_800fa754.advance().get() % 4097);
          MEMORY.ref(4, s2).offset(0x08L).setu(seed_800fa754.advance().get() % 49 + 104);
          MEMORY.ref(2, s2).offset(0x24L).setu(data._10.colour_1c.getX() << 8);
          MEMORY.ref(2, s2).offset(0x26L).setu(data._10.colour_1c.getY() << 8);
          MEMORY.ref(2, s2).offset(0x28L).setu(data._10.colour_1c.getZ() << 8);
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
  public static void deallocateMonsterDeathEffect(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    free(data.effect_44.derefAs(MonsterDeathEffect34.class).ptr_30.get());
  }

  @Method(0x800d34bcL)
  public static long allocateMonsterDeathEffect(final RunningScript a0) {
    final long fp = allocateEffectManager(
      a0.scriptStateIndex_00.get(),
      0x34L,
      MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "monsterDeathEffectTicker", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "monsterDeathEffectRenderer", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "deallocateMonsterDeathEffect", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      MonsterDeathEffect34::new
    );

    final int animCount = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(1).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class).model_148.animCount_98.get();
    final EffectManagerData6c manager = scriptStatePtrArr_800bc1c0.get((int)fp).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    final MonsterDeathEffect34 effect = manager.effect_44.derefAs(MonsterDeathEffect34.class);
    long s4 = mallocTail(animCount * 0x30L);
    effect.ptr_30.set(s4); //TODO
    effect._00.set((short)0);
    effect._02.set(animCount + 8);
    effect.animCount_04.set(animCount);
    effect._06.set(0);
    effect.scriptIndex_08.set(a0.params_20.get(1).deref().get());

    //LAB_800d35a0
    for(int animIndex = 0; animIndex < effect.animCount_04.get(); animIndex++) {
      FUN_800d0094(effect.scriptIndex_08.get(), animIndex, 0x1L);
      MEMORY.ref(1, s4).offset(0x0L).setu(-0x1L);
      s4 = s4 + 0x30L;
    }

    //LAB_800d35cc
    final long v0 = _800c6948.get() + (a0.params_20.get(2).deref().get() & 0xff) * 0x8L;
    effect._0c._00.set(manager._10._00.get());
    effect._0c.x_04.set((short)(-effect._0c.w_08.get() >> 1));
    effect._0c.y_06.set((short)(-effect._0c.h_0a.get() >> 1));
    effect._0c.w_08.set((int)MEMORY.ref(1, v0).offset(0x4L).get());
    effect._0c.h_0a.set((int)MEMORY.ref(1, v0).offset(0x5L).get());
    effect._0c.tpage_0c.set((int)((MEMORY.ref(2, v0).offset(0x2L).get() & 0x100) >>> 4 | (MEMORY.ref(2, v0).offset(0x0L).get() & 0x3ff) >>> 6));
    effect._0c.u_0e.set((int)(MEMORY.ref(1, v0).offset(0x0L).get() & 0x3f) * 4);
    effect._0c.v_0f.set((int)MEMORY.ref(1, v0).offset(0x2L).get());
    effect._0c.clutX_10.set((int)(MEMORY.ref(2, v0).offset(0x6L).get() << 4 & 0x3ff));
    effect._0c.clutY_12.set((int)(MEMORY.ref(2, v0).offset(0x6L).get() >>> 6 & 0x1ff));
    effect._0c._18.set((short)0);
    effect._0c._1a.set((short)0);
    a0.params_20.get(0).deref().set((int)fp);
    return 0;
  }

  @Method(0x800d36e0L)
  public static CString getAdditionName(final int a0, final int addition) {
    currentAddition_800c6790.set(additionNames_800fa8d4);

    //LAB_800d3708
    //a0 always seems to be 0
    for(int i = 0; i < a0; i++) {
      //LAB_800d3724
      while(currentAddition_800c6790.deref().charAt(0) != 0x2fL) {
        currentAddition_800c6790.incr();
      }

      //LAB_800d3744
      currentAddition_800c6790.incr();
    }

    //LAB_800d3760
    //LAB_800d3778
    for(int i = 0; i < addition; i++) {
      //LAB_800d3790
      while(currentAddition_800c6790.deref().charAt(0) != 0) {
        currentAddition_800c6790.incr();
      }

      //LAB_800d37b0
      currentAddition_800c6790.incr();
    }

    //LAB_800d37cc
    return currentAddition_800c6790.deref();
  }

  @Method(0x800d37dcL)
  public static void renderAdditionNameChar(final short displayX, final short displayY, final short addition, final short charOffset, final int charAlpha) {
    final CString additionName = MEMORY.ref(1, getAdditionName(0, addition).getAddress() + charOffset, CString.maxLength(30)); //TODO implement string slicing in core
    int charIdx = 0;

    //LAB_800d3838
    long chr;
    do {
      chr = asciiTable_800fa788.get(charIdx).get();

      if(additionName.charAt(0) == chr) {
        break;
      } else if(chr == 0) {
        //LAB_800d3860
        charIdx = 0x5b;
        break;
      }

      charIdx++;
    } while(true);

    //LAB_800d3864
    FUN_80018d60(displayX, displayY, charIdx % 21 * 12 & 0xfc, charIdx / 21 * 12 + 144 & 0xfc, 12, 12, 0xaL, Translucency.B_PLUS_F, new COLOUR().set(charAlpha, charAlpha, charAlpha), 0x1000L);
  }

  /**
   * NOTE: changed param from reference to value
   */
  @Method(0x800d3910L)
  public static int getCharDisplayWidth(final long chr) {
    //LAB_800d391c
    int charTableOffset;
    for(charTableOffset = 0; ; charTableOffset++) {
      if(asciiTable_800fa788.get(charTableOffset).get() == 0) {
        charTableOffset = 0;
        break;
      }

      if(chr == asciiTable_800fa788.get(charTableOffset).get()) {
        break;
      }
    }

    //LAB_800d3944
    //LAB_800d3948
    return 10 - charWidthAdjustTable_800fa7cc.get(charTableOffset).get();
  }

  @Method(0x800d3968L)
  public static int[] setAdditionNameDisplayCoords(final int a2, final int addition) {
    final CString additionName = getAdditionName(a2, addition);

    int additionDisplayWidth = 0;
    //LAB_800d39b8
    for(int i = 0; additionName.charAt(i) != 0; i++) {
      additionDisplayWidth += getCharDisplayWidth(additionName.charAt(i));
    }

    //LAB_800d39ec
    return new int[] {144 - additionDisplayWidth, 64};
  }

  @Method(0x800d3a20L)
  public static void renderAdditionNameChar(final AdditionScriptData1c additionStruct, final AdditionCharEffectData0c charStruct, final long charAlpha, final long charIdx) {
    renderAdditionNameChar(charStruct.position_04.get(), charStruct.offsetY_06.get(), (short)additionStruct.addition_02.get(), (short)charIdx, (int)charAlpha);
  }

  @Method(0x800d3a64L)
  public static void FUN_800d3a64(final AdditionScriptData1c a0, final AdditionCharEffectData0c a1, final long charAlpha, final long a3) {
    final String sp0x18 = String.valueOf(a0._10.get());

    long s4;
    if(a0._04.get() < 0x19L) {
      s4 = a0._04.get() & 0x1L;
    } else {
      s4 = 0;
    }

    //LAB_800d3ab8
    //LAB_800d3ac4
    for(; s4 >= 0; s4--) {
      int s1 = a1.position_04.get();

      //LAB_800d3ad4
      for(int i = 0; i < sp0x18.length(); i++) {
        FUN_800d3f98((short)s1, a1.offsetY_06.get(), sp0x18.charAt(i) - 0x30, (short)41, (int)charAlpha);
        s1 = s1 + 8;
      }

      //LAB_800d3b08
      FUN_800d3f98((short) s1      , a1.offsetY_06.get(), 0x0d, (short)41, (int)charAlpha);
      FUN_800d3f98((short)(s1 +  8), a1.offsetY_06.get(), 0x0e, (short)41, (int)charAlpha);
      FUN_800d3f98((short)(s1 + 16), a1.offsetY_06.get(), 0x0f, (short)41, (int)charAlpha);
      FUN_800d3f98((short)(s1 + 24), a1.offsetY_06.get(), 0x10, (short)41, (int)charAlpha);
    }

    //LAB_800d3b98
  }

  @Method(0x800d3bb8L)
  public static void FUN_800d3bb8(final int index, final ScriptState<AdditionScriptData1c> state, final AdditionScriptData1c data) {
    final AdditionScriptData1c additionStruct = state.innerStruct_00.deref();
    additionStruct._04.incr();

    if(_800faa9d.get() == 0) {
      deallocateScriptAndChildren(index);
    } else {
      //LAB_800d3c10
      //LAB_800d3c24
      for(int charIdx = 0; charIdx < additionStruct.length_08.get(); charIdx++) {
        final AdditionCharEffectData0c charStruct = additionStruct.ptr_18.deref().get(charIdx);

        if(charStruct.scrolling_00.get() != 0) {
          charStruct.position_04.add((short)additionStruct._0c.get());

          if(charStruct.position_04.get() >= charStruct.offsetX_08.get()) {
            charStruct.position_04.set(charStruct.offsetX_08.get());
            charStruct.scrolling_00.set(0);
          }
        } else {
          //LAB_800d3c70
          if(charStruct.dupes_02.get() > 0) {
            charStruct.dupes_02.decr();
          }
        }

        //LAB_800d3c84
        //LAB_800d3c88
        additionStruct.renderer_14.deref().run(additionStruct, charStruct, 0x80L, (long)charIdx);
        int currPosition = charStruct.position_04.get();
        int s2 = charStruct.dupes_02.get() * 0x10;

        //LAB_800d3cbc
        for(int dupeNum = 0; dupeNum < charStruct.dupes_02.get() - 1; dupeNum++) {
          s2 -= 0x10;
          currPosition -= 0xa;
          final int origCharPosition = charStruct.position_04.get();
          charStruct.position_04.set((short)currPosition);
          additionStruct.renderer_14.deref().run(additionStruct, charStruct, s2 & 0xffL, (long)charIdx);
          charStruct.position_04.set((short)origCharPosition);
        }
      }
    }

    //LAB_800d3d1c
  }

  @Method(0x800d3d48L)
  public static void FUN_800d3d48(final int index, final ScriptState<AdditionScriptData1c> state, final AdditionScriptData1c data) {
    free(state.innerStruct_00.deref().ptr_18.getPointer());
  }

  @Method(0x800d3d74L)
  public static long scriptAllocateAdditionScript(final RunningScript a0) {
    if(a0.params_20.get(1).deref().get() == -1) {
      _800faa9d.setu(0);
    } else {
      //LAB_800d3dc0
      final int addition = gameState_800babc8.charData_32c.get(a0.params_20.get(0).deref().get()).selectedAddition_19.get();
      final int scriptIndex = allocateScriptState(0x1cL, AdditionScriptData1c::new);
      final ScriptState<AdditionScriptData1c> s1 = scriptStatePtrArr_800bc1c0.get(scriptIndex).derefAs(ScriptState.classFor(AdditionScriptData1c.class));
      loadScriptFile(scriptIndex, doNothingScript_8004f650);
      setScriptTicker(scriptIndex, MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "FUN_800d3bb8", int.class, ScriptState.classFor(AdditionScriptData1c.class), AdditionScriptData1c.class), TriConsumerRef::new));
      setScriptDestructor(scriptIndex, MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "FUN_800d3d48", int.class, ScriptState.classFor(AdditionScriptData1c.class), AdditionScriptData1c.class), TriConsumerRef::new));
      final CString additionName = getAdditionName(0, addition);

      //LAB_800d3e5c
      int textLength;
      for(textLength = 0; additionName.charAt(textLength) != 0; textLength++) {
        //
      }

      //LAB_800d3e7c
      final AdditionScriptData1c additionStruct = s1.innerStruct_00.deref();
      additionStruct._00.set(0);
      additionStruct.addition_02.set(addition);
      additionStruct._04.set(0);
      additionStruct.length_08.set(textLength);
      additionStruct._0c.set(120);
      additionStruct.renderer_14.set(MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "renderAdditionNameChar", AdditionScriptData1c.class, AdditionCharEffectData0c.class, long.class, long.class), QuadConsumerRef::new));
      additionStruct.ptr_18.setPointer(mallocTail(textLength * 0xcL));
      _800faa9d.setu(0x1L);

      final int[] displayOffset = setAdditionNameDisplayCoords(0, addition);
      int charPosition = -160;
      int displayOffsetX = displayOffset[0];
      final int displayOffsetY = displayOffset[1];

      //LAB_800d3f18
      for(int charIdx = 0; charIdx < textLength; charIdx++) {
        final AdditionCharEffectData0c charStruct = additionStruct.ptr_18.deref().get(charIdx);
        charStruct.scrolling_00.set(1);
        charStruct.dupes_02.set((short)8);
        charStruct.position_04.set((short)charPosition);
        charStruct.offsetY_06.set((short)displayOffsetY);
        charStruct.offsetX_08.set((short)displayOffsetX);
        charStruct.offsetY_0a.set((short)displayOffsetY);
        displayOffsetX += getCharDisplayWidth(additionName.charAt(charIdx));
        charPosition -= 80;
      }
    }

    //LAB_800d3f70
    return 0;
  }

  @Method(0x800d3f98L)
  public static void FUN_800d3f98(final short x, final short y, final int a2, final short a3, final int colour) {
    FUN_80018d60(x, y, a2 * 8 + 16 & 0xf8, 40, 8, 16, a3, Translucency.B_PLUS_F, new COLOUR().set(colour, colour, colour), 0x1000L);
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
    final long sp20;
    long sp30;
    final long sp28;
    final long sp2c;
    final BttlScriptData40 s3 = state.innerStruct_00.deref();

    if(_800faa94.get() == 0 && s3._00.get() == 0) {
      if(s3._02.get() == 0) {
        deallocateScriptAndChildren(index);
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
          FUN_800d3f98((short)s2, (short)s5, 10, (short)sp28, (int)(fp & 0xff));
          s2 = s2 + 0x8L;
        }

        //LAB_800d4224
        //LAB_800d423c
        for(int i = 0; i < sp0x18.length(); i++) {
          FUN_800d3f98((short)s2, (short)s5, sp0x18.charAt(i) - 0x30, (short)sp28, (int)(fp & 0xff));
          s2 = s2 + 0x8L;
        }

        //LAB_800d4274
        FUN_800d3f98((short)(s2 - 0x2L), (short)s5, 11, (short)sp2c, (int)(fp & 0xff));
        FUN_800d3f98((short)(s2 + 0x4L), (short)s5, 12, (short)sp2c, (int)(fp & 0xff));
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
    free(state.innerStruct_00.deref()._3c.get());
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
      loadScriptFile(scriptIndex, doNothingScript_8004f650);
      setScriptTicker(scriptIndex, MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "FUN_800d4018", int.class, ScriptState.classFor(BttlScriptData40.class), BttlScriptData40.class), TriConsumerRef::new));
      setScriptDestructor(scriptIndex, MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "FUN_800d430c", int.class, ScriptState.classFor(BttlScriptData40.class), BttlScriptData40.class), TriConsumerRef::new));

      final BttlScriptData40 s1 = state.innerStruct_00.derefAs(BttlScriptData40.class);
      s1._00.set(0x1);
      s1._02.set(0x80);
      s1._04.set(0);
      s1._08.set(s2);
      s1._0c.set(0);
      s1._10.set(30);
      s1._1c.set((int)(_800faa90.getSigned() << 8));
      s1._20.set(0x5000);
      s1._3c.set(mallocTail(0x80L));

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
      final int scriptIndex = allocateScriptState(0x1cL, AdditionScriptData1c::new);
      loadScriptFile(scriptIndex, doNothingScript_8004f650);
      setScriptTicker(scriptIndex, MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "FUN_800d3bb8", int.class, ScriptState.classFor(AdditionScriptData1c.class), AdditionScriptData1c.class), TriConsumerRef::new));
      setScriptDestructor(scriptIndex, MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "FUN_800d3d48", int.class, ScriptState.classFor(AdditionScriptData1c.class), AdditionScriptData1c.class), TriConsumerRef::new));
      final ScriptState<?> state = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref();
      final AdditionScriptData1c s0 = state.innerStruct_00.derefAs(AdditionScriptData1c.class);
      s0.ptr_18.setPointer(mallocTail(0xcL));
      _800faa9c.setu(0x1L);
      s0._0c.set(40);
      s0.renderer_14.set(MEMORY.ref(4, getMethodAddress(Bttl_800d.class, "FUN_800d3a64", AdditionScriptData1c.class, AdditionCharEffectData0c.class, long.class, long.class), QuadConsumerRef::new));
      s0._00.set(0);
      s0.addition_02.set(0);
      s0._04.set(0);
      s0.length_08.set(0x1L);
      s0._10.set(s2);
      final AdditionCharEffectData0c struct = s0.ptr_18.deref().get(0);
      struct.scrolling_00.set(1);
      struct.dupes_02.set((short)8);
      struct.position_04.set((short)-0xa0);
      struct.offsetY_06.set((short)0x60);
      struct.offsetX_08.set((short)(0x90 - (String.valueOf(s2).length() + 4) * 8));
      struct.offsetY_0a.set((short)0x60);
    } else {
      //LAB_800d46b0
      _800faa9c.setu(0);
    }

    //LAB_800d46bc
    return 0;
  }

  @Method(0x800d46d4L)
  public static long FUN_800d46d4(final RunningScript a0) {
    final long v1 = _800faaa0.offset(1, a0.params_20.get(0).deref().get() * 0x6L).getAddress();

    final COLOUR colour = new COLOUR().set(a0.params_20.get(4).deref().get(), a0.params_20.get(4).deref().get(), a0.params_20.get(4).deref().get());

    if(MEMORY.ref(1, v1).offset(0x0L).get() == 0) {
      FUN_80018d60(a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), (int)MEMORY.ref(1, v1).offset(0x1L).get(), (int)MEMORY.ref(1, v1).offset(0x2L).get(), (int)MEMORY.ref(1, v1).offset(0x3L).get(), (int)MEMORY.ref(1, v1).offset(0x4L).get(), MEMORY.ref(1, v1).offset(0x5L).get(), Translucency.of(a0.params_20.get(3).deref().get()), colour, 0x1000L);
    } else {
      //LAB_800d4784
      FUN_80018a5c(a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), (int)MEMORY.ref(1, v1).offset(0x1L).get(), (int)MEMORY.ref(1, v1).offset(0x2L).get(), (int)MEMORY.ref(1, v1).offset(0x3L).get(), (int)MEMORY.ref(1, v1).offset(0x4L).get(), MEMORY.ref(1, v1).offset(0x5L).get(), Translucency.of(a0.params_20.get(3).deref().get()), colour, 0x1000L, 0x1000L);
    }

    //LAB_800d47cc
    return 0;
  }

  @Method(0x800d47dcL)
  public static void FUN_800d47dc(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.vec_94.set(cam.rview2_00.viewpoint_00).mul(0x100);

    if(a5 == 0) {
      //LAB_800d4854
      cam._d0.set(a3);
      cam._b0.set((a0 - cam.vec_94.getX()) / a3);
      cam._bc.set((a1 - cam.vec_94.getY()) / a3);
      cam._c8.set((a2 - cam.vec_94.getZ()) / a3);
    } else assert a5 != 1 : "Undefined t0/t1";

    //LAB_800d492c
    //LAB_800d4934
    cam._11c.or(0x1L);
    cam._120.set(8);
  }

  @Method(0x800d496cL)
  public static void FUN_800d496c(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;

    cam._ac.set(FUN_800dc384(0, 1, 0, 0) << 8);
    cam._b8.set(FUN_800dc384(0, 1, 1, 0) << 8);
    cam._a0.set(FUN_800dc384(0, 1, 2, 0) << 8);

    if(a5 == 0) {
      //LAB_800d4a24
      cam._d0.set(a3);
      cam._b0.set(FUN_800dcf10(0, cam._ac.get(), a0, a3, a4 & 3));
      cam._bc.set(FUN_800dcf10(1, cam._b8.get(), a1, a3, a4 >> 2 & 3));
      cam._c8.set(FUN_800dcf10(2, cam._a0.get(), a2, a3, 0));
    } else if(a5 == 1) {
      //LAB_800d4a7c
      final int s1 = FUN_800dcfb8(0, cam._ac.get(), a0, a4 & 3);
      final int s0 = FUN_800dcfb8(1, cam._b8.get(), a1, a4 >> 2 & 3);
      final int v0 = FUN_800dcfb8(2, cam._a0.get(), a2, 0);
      cam._d0.set(SquareRoot0(s1 * s1 + s0 * s0 + v0 * v0) / a3);
      cam._b0.set(FUN_800dcf10(0, cam._ac.get(), a0, cam._d0.get(), a4 & 3));
      cam._bc.set(FUN_800dcf10(1, cam._b8.get(), a1, cam._d0.get(), a4 >> 2 & 3));
      cam._c8.set(FUN_800dcf10(2, cam._a0.get(), a2, cam._d0.get(), 0));
    }

    //LAB_800d4b68
    cam._11c.or(0x1L);
    cam._120.set(9);
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
    } else assert a5 != 1 : "Undefined s3/s5";

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
      cam._b0.set(FUN_800dcf10(0, cam._ac.get(), a0, a3, a4 & 3));
      cam._bc.set(FUN_800dcf10(1, cam._b8.get(), a1, a3, a4 >> 2 & 3));
      cam._c8.set(FUN_800dcf10(2, cam._a0.get(), a2, a3, 0));
    } else if(a5 == 1) {
      //LAB_800d4e8c
      final int s1 = FUN_800dcfb8(0, cam._ac.get(), a0, a4 & 3);
      final int s0 = FUN_800dcfb8(1, cam._b8.get(), a1, a4 >> 2 & 3);
      final int v0 = FUN_800dcfb8(2, cam._a0.get(), a2, 0);
      cam._d0.set(SquareRoot0(s1 * s1 + s0 * s0 + v0 * v0) / a3);
      cam._b0.set(FUN_800dcf10(0, cam._ac.get(), a0, cam._d0.get(), a4 & 3));
      cam._bc.set(FUN_800dcf10(1, cam._b8.get(), a1, cam._d0.get(), a4 >> 2 & 3));
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

      if(a3 != 0) {
        cam._b0.set((a0 - cam.vec_94.getX()) / a3);
        cam._bc.set((a1 - cam.vec_94.getY()) / a3);
        cam._c8.set((a2 - cam.vec_94.getZ()) / a3);
      } else {
        cam._b0.set(-1);
        cam._bc.set(-1);
        cam._c8.set(-1);
      }
    } else if(a5 == 1) {
      //LAB_800d50c4
      final int x = a0 - cam.vec_94.getX();
      final int y = a1 - cam.vec_94.getY();
      final int z = a2 - cam.vec_94.getZ();
      final int v0 = SquareRoot0(z * z + y * y + x * x) / a3;
      cam._d0.set(v0);

      if(v0 != 0) {
        cam._b0.set((a0 - cam.vec_94.getX()) / v0);
        cam._bc.set((a1 - cam.vec_94.getY()) / v0);
        cam._c8.set((a2 - cam.vec_94.getZ()) / v0);
      } else {
        cam._b0.set(-1);
        cam._bc.set(-1);
        cam._c8.set(-1);
      }
    }

    //LAB_800d5150
    //LAB_800d5158
    cam._11c.or(0x1L);
    cam._120.set(14);
  }

  @Method(0x800d519cL)
  public static void FUN_800d519c(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    final BattleCamera s4 = camera_800c67f0;
    s4.bobjIndex_f4.set(scriptIndex);
    s4._ac.set(FUN_800dc384(0, 0x7L, 0, scriptIndex) << 8);
    s4._b8.set(FUN_800dc384(0, 0x7L, 1, scriptIndex) << 8);
    s4._a0.set(FUN_800dc384(0, 0x7L, 2, scriptIndex) << 8);

    if(a5 == 0) {
      //LAB_800d525c
      s4._d0.set(a3);
      s4._b0.set(FUN_800dcf10(0, s4._ac.get(), a0, a3, a4 & 3));
      s4._bc.set(FUN_800dcf10(1, s4._b8.get(), a1, a3, a4 >> 2 & 3));
      s4._c8.set(FUN_800dcf10(2, s4._a0.get(), a2, a3, 0));
    } else if(a5 == 1) {
      //LAB_800d52b4
      final long s1 = FUN_800dcfb8(0, s4._ac.get(), a0, a4 & 3);
      final long s0 = FUN_800dcfb8(1, s4._b8.get(), a1, a4 >> 2 & 3);
      final long v0 = FUN_800dcfb8(2, s4._a0.get(), a2, 0);
      s4._d0.set(SquareRoot0(s1 * s1 + s0 * s0 + v0 * v0) / a3);
      s4._b0.set(FUN_800dcf10(0, s4._ac.get(), a0, s4._d0.get(), a4 & 3));
      s4._bc.set(FUN_800dcf10(1, s4._b8.get(), a1, s4._d0.get(), a4 >> 2 & 3));
      s4._c8.set(FUN_800dcf10(2, s4._a0.get(), a2, s4._d0.get(), 0));
    }

    //LAB_800d53a0
    s4._11c.or(0x1L);
    s4._120.set(15);
  }

  @Method(0x800d53e4L)
  public static void FUN_800d53e4(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6) {
    final BattleCamera cam = camera_800c67f0;

    final int s5 = (a0 >> 8) - camera_800c67f0.rview2_00.viewpoint_00.getX();
    final int s6 = (a1 >> 8) - camera_800c67f0.rview2_00.viewpoint_00.getY();
    final int s2 = (a2 >> 8) - camera_800c67f0.rview2_00.viewpoint_00.getZ();
    final int s3 = s5 / 2;
    final int v0 = s6 / 2;
    final int s0 = s2 / 2;
    cam._dc.set(SquareRoot0(s3 * s3 + v0 * v0 + s0 * s0) << 9);
    cam._d4.set((ratan2(s2, s5) & 0xfff) << 8);
    final int a0_0 = cam._dc.get() * 2 / (a3 + a4);
    final int s4 = (a4 - a3) / a0_0;
    cam._d8.set((ratan2(s6, SquareRoot0(s3 * s3 + s0 * s0) * 2) & 0xfff) << 8);
    cam._a4.set(a3);
    cam._e8.set(a0);
    cam._ec.set(a1);
    cam._f0.set(a2);
    cam._11c.or(0x1L);
    cam._120.set(16);
    cam._d0.set(a0_0);
    cam._b4.set(s4);
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
    } else if(a4 == 1) {
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

  @Method(0x800d60b0L)
  public static void FUN_800d60b0(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam._ac.set(FUN_800dc384(0, 1, 0, 0) << 8);
    cam._b8.set(FUN_800dc384(0, 1, 1, 0) << 8);
    cam._a0.set(FUN_800dc384(0, 1, 2, 0) << 8);
    final int s1 = FUN_800dcfb8(0, cam._ac.get(), a0, a6 & 3) >> 8;
    final int s0 = FUN_800dcfb8(1, cam._b8.get(), a1, a6 >> 2 & 3) >> 8;
    FUN_800dcfb8(2, cam._a0.get(), a2, 0);
    cam._d0.set(a3);
    cam._d4.set((ratan2(s0, s1) & 0xfff) << 8);
    cam._d8.set(0);
    cam._dc.set(SquareRoot0(s1 * s1 + s0 * s0) << 8);

    final int s4;
    final int s3;
    if(a4 == 0) {
      //LAB_800d61fc
      s3 = a5;
      s4 = cam._dc.get() * 2 / a3 - a5;
    } else if(a4 == 1) {
      //LAB_800d621c
      s3 = cam._dc.get() * 2 / a3 - a5;
      s4 = a5;
    } else {
      throw new IllegalArgumentException("a4 must be 0 or 1");
    }

    //LAB_800d6238
    //LAB_800d6240
    cam._e0.set(s3);
    cam._e8.set(a0);
    cam._ec.set(a1);
    cam._f0.set(a2);
    cam._e4.set((s4 - s3) / cam._d0.get());
    cam._a4.set(FUN_800dcfb8(2, cam._a0.get(), a2, 0) / cam._d0.get());
    cam._11c.or(0x1L);
    cam._120.set(17);
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
    cam._d4.set((ratan2(s3, s4) & 0xfff) << 8);
    cam._d8.set((ratan2(s5, SquareRoot0(s1 * s1 + s0 * s0) * 2) & 0xfff) << 8);
    cam._dc.set(SquareRoot0(s1 * s1 + v1 * v1 + s0 * s0) << 9);
    final IntRef sp0x18 = new IntRef();
    final IntRef sp0x1c = new IntRef();
    FUN_800dcebc(a4, a5, cam._dc.get(), a3, sp0x18, sp0x1c);
    cam._e8.set(a0);
    cam._ec.set(a1);
    cam._f0.set(a2);
    cam._a4.set(sp0x18.get());
    cam._b4.set((sp0x1c.get() - sp0x18.get()) / cam._d0.get());
    cam._11c.or(0x1L);
    cam._120.set(20);
  }

  @Method(0x800d64e4L)
  public static void FUN_800d64e4(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam._ac.set(FUN_800dc384(0, 0x5L, 0, 0) << 8);
    cam._b8.set(FUN_800dc384(0, 0x5L, 1, 0) << 8);
    cam._a0.set(FUN_800dc384(0, 0x5L, 2, 0) << 8);
    final int s1 = FUN_800dcfb8(0, cam._ac.get(), a0, a6 & 3) >> 8;
    final int s0 = FUN_800dcfb8(1, cam._b8.get(), a1, a6 >> 2 & 3) >> 8;
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
    } else if(a4 == 1) {
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
    cam._d4.set((ratan2(s2, s3) & 0xfff) << 8);
    cam._d8.set((ratan2(s4, SquareRoot0(s1 * s1 + s0 * s0) * 2) & 0xfff) << 8);
    cam._dc.set(SquareRoot0(s1 * s1 + v1 * v1 + s0 * s0) << 9);

    final int s6;
    final int s7;
    if(a4 == 0) {
      //LAB_800d68a0
      s6 = a5;
      s7 = cam._dc.get() * 2 / a3 - a5;
    } else if(a4 == 1) {
      //LAB_800d68c0
      s6 = cam._dc.get() * 2 / a3 - a5;
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
    final int s1 = FUN_800dcfb8(0, cam._ac.get(), a0, a6 & 3) >> 8;
    final int s0 = FUN_800dcfb8(1, cam._b8.get(), a1, a6 >> 2 & 3) >> 8;
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
    cam.vec_20.set(cam.rview2_00.refpoint_0c).mul(0x100);

    if(a5 == 0) {
      //LAB_800d6c04
      cam._5c.set(a3);

      // Retail bug: divide by 0 is possible here - the processor sets LO to -1 in this case
      if(a3 != 0) {
        cam._3c.set((a0 - cam.vec_20.getX()) / a3);
        cam._48.set((a1 - cam.vec_20.getY()) / a3);
        cam._54.set((a2 - cam.vec_20.getZ()) / a3);
      } else {
        cam._3c.set(-1);
        cam._48.set(-1);
        cam._54.set(-1);
      }
    } else if(a5 == 1) {
      throw new RuntimeException("t0/t1 undefined");
    }

    //LAB_800d6cdc
    //LAB_800d6ce4
    cam._11c.or(0x2L);
    cam._121.set(8);
  }

  @Method(0x800d6d18L)
  public static void FUN_800d6d18(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam._38.set(FUN_800dc384(1, 1, 0, 0) << 8);
    cam._44.set(FUN_800dc384(1, 1, 1, 0) << 8);
    cam._2c.set(FUN_800dc384(1, 1, 2, 0) << 8);

    if(a5 == 0) {
      //LAB_800d6dd0
      cam._5c.set(a3);
      cam._3c.set(FUN_800dcf10(0, cam._38.get(), a0, a3, a4 & 3));
      cam._48.set(FUN_800dcf10(1, cam._44.get(), a1, a3, a4 >> 2 & 3));
      cam._54.set(FUN_800dcf10(2, cam._2c.get(), a2, a3, 0));
    } else if(a5 == 1) {
      //LAB_800d6e28
      final int s1 = FUN_800dcfb8(0, cam._38.get(), a0, a4 & 3);
      final int s0 = FUN_800dcfb8(1, cam._44.get(), a1, a4 >> 2 & 3);
      final int v0 = FUN_800dcfb8(2, cam._2c.get(), a2, 0);
      cam._5c.set(SquareRoot0(s1 * s1 + s0 * s0 + v0 * v0) / a3);
      cam._3c.set(FUN_800dcf10(0, cam._38.get(), a0, cam._5c.get(), a4 & 3));
      cam._48.set(FUN_800dcf10(1, cam._44.get(), a1, cam._5c.get(), a4 >> 2 & 3));
      cam._54.set(FUN_800dcf10(2, cam._2c.get(), a2, cam._5c.get(), 0));
    }

    //LAB_800d6f14
    cam._11c.or(0x2L);
    cam._121.set(9);
  }

  @Method(0x800d6f58L)
  public static void FUN_800d6f58(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;

    cam.vec_20.setX(FUN_800dc384(1, 2, 0, 0) << 8);
    cam.vec_20.setY(FUN_800dc384(1, 2, 1, 0) << 8);
    cam.vec_20.setZ(FUN_800dc384(1, 2, 2, 0) << 8);

    if(a5 == 0) {
      //LAB_800d7008
      cam._5c.set(a3);
      cam._3c.set((a0 - cam.vec_20.getX()) / a3);
      cam._48.set((a1 - cam.vec_20.getY()) / a3);
      cam._54.set((a2 - cam.vec_20.getZ()) / a3);
    } else if(a5 == 1) {
      throw new RuntimeException("Broken code");
    }

    //LAB_800d70e0
    //LAB_800d70e8
    cam._11c.or(0x2L);
    cam._121.set(10);
  }

  @Method(0x800d7128L)
  public static void FUN_800d7128(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;

    cam._38.set(FUN_800dc384(1, 3, 0, 0) << 8);
    cam._44.set(FUN_800dc384(1, 3, 1, 0) << 8);
    cam._2c.set(FUN_800dc384(1, 3, 2, 0) << 8);

    if(a5 == 0) {
      //LAB_800d71e0
      cam._5c.set(a3);
      cam._3c.set(FUN_800dcf10(0, cam._38.get(), a0, a3, a4 & 3));
      cam._48.set(FUN_800dcf10(1, cam._44.get(), a1, a3, a4 >> 2 & 3));
      cam._54.set(FUN_800dcf10(2, cam._2c.get(), a2, a3, 0));
    } else if(a5 == 1) {
      //LAB_800d7238
      final int s1 = FUN_800dcfb8(0, cam._38.get(), a0, a4 & 3);
      final int s0 = FUN_800dcfb8(1, cam._44.get(), a1, a4 >> 2 & 3);
      final int v0 = FUN_800dcfb8(2, cam._2c.get(), a2, 0);
      cam._5c.set(SquareRoot0(s1 * s1 + s0 * s0 + v0 * v0) / a3);
      cam._3c.set(FUN_800dcf10(0, cam._38.get(), a0, cam._5c.get(), a4 & 3));
      cam._48.set(FUN_800dcf10(1, cam._44.get(), a1, cam._5c.get(), a4 >> 2 & 3));
      cam._54.set(FUN_800dcf10(2, cam._2c.get(), a2, cam._5c.get(), 0));
    }

    //LAB_800d7324
    cam._11c.or(0x2L);
    cam._121.set(11);
  }

  @Method(0x800d7368L)
  public static void FUN_800d7368(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.bobjIndex_80.set(scriptIndex);
    cam.vec_20.setX(FUN_800dc384(1, 0x6L, 0, scriptIndex) << 8);
    cam.vec_20.setY(FUN_800dc384(1, 0x6L, 1, scriptIndex) << 8);
    cam.vec_20.setZ(FUN_800dc384(1, 0x6L, 2, scriptIndex) << 8);

    if(a5 == 0) {
      //LAB_800d7424
      cam._5c.set(a3);
      cam._3c.set((a0 - cam.vec_20.getX()) / a3);
      cam._48.set((a1 - cam.vec_20.getY()) / a3);
      cam._54.set((a2 - cam.vec_20.getZ()) / a3);
    } else if(a5 == 1) {
      throw new RuntimeException("Undefined s5/s6");
    }

    //LAB_800d74fc
    //LAB_800d7504
    cam._11c.or(0x2L);
    cam._121.set(14);
  }

  @Method(0x800d7548L)
  public static void FUN_800d7548(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;

    cam.bobjIndex_80.set(scriptIndex);
    cam._38.set(FUN_800dc384(1, 7, 0, scriptIndex) << 8);
    cam._44.set(FUN_800dc384(1, 7, 1, scriptIndex) << 8);
    cam._2c.set(FUN_800dc384(1, 7, 2, scriptIndex) << 8);

    if(a5 == 0) {
      //LAB_800d7608
      cam._5c.set(a3);
      cam._3c.set(FUN_800dcf10(0, cam._38.get(), a0, a3, a4 & 3));
      cam._48.set(FUN_800dcf10(1, cam._44.get(), a1, a3, a4 >> 2 & 3));
      cam._54.set(FUN_800dcf10(2, cam._2c.get(), a2, a3, 0));
    } else if(a5 == 1) {
      //LAB_800d7660
      final int s1 = FUN_800dcfb8(0, cam._38.get(), a0, a4 & 3);
      final int s0 = FUN_800dcfb8(1, cam._44.get(), a1, a4 >> 2 & 3);
      final int v0 = FUN_800dcfb8(2, cam._2c.get(), a2, 0);
      cam._5c.set(SquareRoot0(s1 * s1 + s0 * s0 + v0 * v0) / a3);
      cam._3c.set(FUN_800dcf10(0, cam._38.get(), a0, cam._5c.get(), a4 & 3));
      cam._48.set(FUN_800dcf10(1, cam._44.get(), a1, cam._5c.get(), a4 >> 2 & 3));
      cam._54.set(FUN_800dcf10(2, cam._2c.get(), a2, cam._5c.get(), 0));
    }

    //LAB_800d774c
    cam._11c.or(0x2L);
    cam._121.set(15);
  }

  @Method(0x800d7790L)
  public static void FUN_800d7790(final int x, final int y, final int z, final int a3, final int a4, final int a5, final int a6) {
    final BattleCamera cam = camera_800c67f0;
    final int dx = (x >> 8) - cam.rview2_00.refpoint_0c.getX();
    final int dy = (y >> 8) - cam.rview2_00.refpoint_0c.getY();
    final int dz = (z >> 8) - cam.rview2_00.refpoint_0c.getZ();
    final int hdx = dx / 2;
    final int hdy = dy / 2;
    final int hdz = dz / 2;
    cam.vec_60.setX((ratan2(dz, dx) & 0xfff) << 8);
    cam.vec_60.setY((ratan2(dy, SquareRoot0(hdx * hdx + hdz * hdz) * 2) & 0xfff) << 8);
    cam.vec_60.setZ(SquareRoot0(hdx * hdx + hdy * hdy + hdz * hdz) << 9);
    cam._30.set(a3);
    cam.vec_74.set(x, y, z);
    cam._11c.or(0x2L);
    cam._121.set(16);
    cam._5c.set(cam.vec_60.getZ() * 2 / (a4 + a3));

    if(cam._5c.get() > 0) {
      cam._40.set((a4 - a3) / cam._5c.get());
    } else {
      cam._40.set(-1);
    }
  }

  @Method(0x800d7920L)
  public static void FUN_800d7920(final int x, final int y, final int z, final int a3, final int a4, final int a5, final int a6) {
    final BattleCamera cam = camera_800c67f0;
    cam._38.set(FUN_800dc384(1, 1, 0, 0) << 8);
    cam._44.set(FUN_800dc384(1, 1, 1, 0) << 8);
    cam._2c.set(FUN_800dc384(1, 1, 2, 0) << 8);
    final int s2 = FUN_800dcfb8(0, cam._38.get(), x, a5 & 3) >> 8;
    final int s1 = FUN_800dcfb8(1, cam._44.get(), y, a5 >> 2 & 3) >> 8;
    FUN_800dcfb8(2, cam._2c.get(), z, 0);
    cam.vec_60.setX((ratan2(s1, s2) & 0xfff) << 8);
    cam.vec_60.setY(0);
    cam.vec_60.setZ(SquareRoot0(s2 * s2 + s1 * s1) << 8);
    cam._6c.set(a3);
    cam.vec_74.set(x, y, z);
    cam._5c.set(cam.vec_60.getZ() * 2 / (a3 + a4));
    cam._121.set(17);
    cam._11c.or(0x2L);

    if(cam._5c.get() > 0) {
      cam._30.set(FUN_800dcfb8(2, cam._2c.get(), z, 0) / cam._5c.get());
      cam._70.set((a4 - a3) / cam._5c.get());
    } else {
      cam._30.set(-1);
      cam._70.set(-1);
    }
  }

  @Method(0x800d7aecL)
  public static void FUN_800d7aec(final int x, final int y, final int z, final int a3, final int a4, final int a5, final int a6) {
    final BattleCamera cam = camera_800c67f0;
    cam.vec_20.setX(FUN_800dc384(1, 2, 0, 0) << 8);
    cam.vec_20.setY(FUN_800dc384(1, 2, 1, 0) << 8);
    cam.vec_20.setZ(FUN_800dc384(1, 2, 2, 0) << 8);
    final int dx = x - cam.vec_20.getX() >> 8;
    final int dy = y - cam.vec_20.getY() >> 8;
    final int dz = z - cam.vec_20.getZ() >> 8;
    final int hdx = dx / 2;
    final int hdy = dy / 2;
    final int hdz = dz / 2;
    cam.vec_60.setX((ratan2(dz, dx) & 0xfff) << 8);
    cam.vec_60.setY((ratan2(dy, SquareRoot0(hdx * hdx + hdz * hdz) * 2) & 0xfff) << 8);
    cam.vec_60.setZ(SquareRoot0(hdx * hdx + hdy * hdy + hdz * hdz) << 9);
    cam._30.set(a3);
    cam.vec_74.set(x, y, z);
    cam._11c.or(0x2L);
    cam._121.set(18);
    cam._5c.set(cam.vec_60.getZ() * 2 / (a3 + a4));

    if(cam._5c.get() > 0) {
      cam._40.set((a4 - a3) / cam._5c.get());
    } else {
      cam._40.set(-1);
    }
  }

  @Method(0x800d7cdcL)
  public static void FUN_800d7cdc(final int x, final int y, final int z, final int a3, final int a4, final int a5, final int a6) {
    final BattleCamera cam = camera_800c67f0;
    cam._38.set(FUN_800dc384(1, 3, 0, 0) << 8);
    cam._44.set(FUN_800dc384(1, 3, 1, 0) << 8);
    cam._2c.set(FUN_800dc384(1, 3, 2, 0) << 8);
    final int s2 = FUN_800dcfb8(0, cam._38.get(), x, a5 & 0x3) >> 8;
    final int s1 = FUN_800dcfb8(1, cam._44.get(), y, a5 >> 2 & 0x3) >> 8;
    FUN_800dcfb8(2, cam._2c.get(), z, 0);
    cam.vec_60.setX((ratan2(s1, s2) & 0xfff) << 8);
    cam.vec_60.setY(0);
    cam.vec_60.setZ(SquareRoot0(s2 * s2 + s1 * s1) << 8);
    cam._5c.set(cam.vec_60.getZ() * 2 / (a3 + a4));
    cam._6c.set(a3);
    cam.vec_74.set(x, y, z);
    cam._11c.or(0x2L);
    cam._121.set(19);

    if(cam._5c.get() > 0) {
      cam._30.set(FUN_800dcfb8(2, cam._2c.get(), z, 0) / cam._5c.get());
      cam._70.set((a4 - a3) / cam._5c.get());
    } else {
      cam._30.set(-1);
      cam._70.set(-1);
    }
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
    final IntRef sp0x18 = new IntRef();
    final IntRef sp0x1c = new IntRef();
    FUN_800dcebc(a4, a5, cam.vec_60.getZ(), cam._5c.get(), sp0x18, sp0x1c);
    cam._30.set(sp0x18.get());
    cam._40.set((sp0x1c.get() - sp0x18.get()) / cam._5c.get());
    cam.vec_74.setX(a0);
    cam.vec_74.setY(a1);
    cam.vec_74.setZ(a2);
    cam._11c.or(0x2L);
    cam._121.set(16);
  }

  @Method(0x800d8424L)
  public static void FUN_800d8424(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;

    cam._38.set(FUN_800dc384(1, 1, 0, 0) << 8);
    cam._44.set(FUN_800dc384(1, 1, 1, 0) << 8);
    cam._2c.set(FUN_800dc384(1, 1, 2, 0) << 8);
    final int s2 = FUN_800dcfb8(0, cam._38.get(), a0, a6 & 3) >> 8;
    final int s1 = FUN_800dcfb8(1, cam._44.get(), a1, a6 >> 2 & 3) >> 8;
    FUN_800dcfb8(2, cam._2c.get(), a2, 0);
    cam._5c.set(a3);
    cam.vec_60.setX((ratan2(s1, s2) & 0xfff) << 8);
    cam.vec_60.setY(0);
    cam.vec_60.setZ(SquareRoot0(s2 * s2 + s1 * s1) << 8);
    final IntRef sp0x18 = new IntRef();
    final IntRef sp0x1c = new IntRef();
    FUN_800dcebc(a4, a5, cam.vec_60.getZ(), a3, sp0x18, sp0x1c);
    cam._6c.set(sp0x18.get());
    cam._70.set((sp0x1c.get() - sp0x18.get()) / cam._5c.get());
    cam.vec_74.set(a0, a1, a2);
    cam._11c.or(0x2L);
    cam._121.set(17);
    cam._30.set(FUN_800dcfb8(2, cam._2c.get(), a2, 0) / cam._5c.get());
  }

  @Method(0x800d8614L)
  public static void FUN_800d8614(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;

    cam.vec_20.setX(FUN_800dc384(1, 2, 0, 0) << 8);
    cam.vec_20.setY(FUN_800dc384(1, 2, 1, 0) << 8);
    cam.vec_20.setZ(FUN_800dc384(1, 2, 2, 0) << 8);
    final int s4 = a0 - cam.vec_20.getX() >> 8;
    final int s1 = s4 / 2;
    final int s5 = a1 - cam.vec_20.getY() >> 8;
    final int v1 = s5 / 2;
    final int s3 = a2 - cam.vec_20.getZ() >> 8;
    final int s0 = s3 / 2;
    cam.vec_60.setX((ratan2(s3, s4) & 0xfff) << 8);
    cam.vec_60.setY((ratan2(s5, SquareRoot0(s1 * s1 + s0 * s0) * 2) & 0xfff) << 8);
    cam.vec_60.setZ(SquareRoot0(s1 * s1 + v1 * v1 + s0 * s0) << 9);
    cam._5c.set(a3);
    final IntRef sp0x18 = new IntRef();
    final IntRef sp0x1c = new IntRef();
    FUN_800dcebc(a4, a5, cam.vec_60.getZ(), a3, sp0x18, sp0x1c);
    cam._30.set(sp0x18.get());
    cam._40.set((sp0x1c.get() - sp0x18.get()) / cam._5c.get());
    cam.vec_74.setX(a0);
    cam.vec_74.setY(a1);
    cam.vec_74.setZ(a2);
    cam._11c.or(0x2L);
    cam._121.set(18);
  }

  @Method(0x800d8808L)
  public static void FUN_800d8808(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;

    cam._38.set(FUN_800dc384(1, 3, 0, 0) << 8);
    cam._44.set(FUN_800dc384(1, 3, 1, 0) << 8);
    cam._2c.set(FUN_800dc384(1, 3, 2, 0) << 8);
    final int s2 = FUN_800dcfb8(0, cam._38.get(), a0, a6 & 3) >> 8;
    final int s1 = FUN_800dcfb8(1, cam._44.get(), a1, a6 >> 2 & 3) >> 8;
    FUN_800dcfb8(2, cam._2c.get(), a2, 0);
    cam.vec_60.setX((ratan2(s1, s2) & 0xfff) << 8);
    cam.vec_60.setY(0);
    cam.vec_60.setZ(SquareRoot0(s2 * s2 + s1 * s1) << 8);
    cam._5c.set(a3);
    final IntRef sp0x18 = new IntRef();
    final IntRef sp0x1c = new IntRef();
    FUN_800dcebc(a4, a5, cam.vec_60.getZ(), a3, sp0x18, sp0x1c);
    cam._30.set(FUN_800dcfb8(2, cam._2c.get(), a2, 0) / cam._5c.get());
    cam._6c.set(sp0x18.get());
    cam._70.set((sp0x1c.get() - sp0x18.get()) / cam._5c.get());
    cam.vec_74.set(a0, a1, a2);
    cam._11c.or(0x2L);
    cam._121.set(19);
  }

  @Method(0x800d89f8L)
  public static void FUN_800d89f8(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.bobjIndex_80.set(scriptIndex);
    cam.vec_20.setX(FUN_800dc384(1, 6, 0, scriptIndex) << 8);
    cam.vec_20.setY(FUN_800dc384(1, 6, 1, scriptIndex) << 8);
    cam.vec_20.setZ(FUN_800dc384(1, 6, 2, scriptIndex) << 8);
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
    final IntRef sp0x18 = new IntRef();
    final IntRef sp0x1c = new IntRef();
    FUN_800dcebc(a4, a5, cam.vec_60.getZ(), a3, sp0x18, sp0x1c);
    cam.vec_74.setX(a0);
    cam.vec_74.setY(a1);
    cam.vec_74.setZ(a2);
    cam._30.set(sp0x18.get());
    cam._40.set((sp0x1c.get() - sp0x18.get()) / cam._5c.get());
    cam._11c.or(0x2L);
    cam._121.set(22);
  }

  @Method(0x800d8bf4L)
  public static void FUN_800d8bf4(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;

    cam.bobjIndex_80.set(scriptIndex);
    cam._38.set(FUN_800dc384(1, 7, 0, scriptIndex) << 8);
    cam._44.set(FUN_800dc384(1, 7, 1, scriptIndex) << 8);
    cam._2c.set(FUN_800dc384(1, 7, 2, scriptIndex) << 8);
    final int s2 = FUN_800dcfb8(0, cam._38.get(), a0, a6 & 3) >> 8;
    final int s0 = FUN_800dcfb8(1, cam._44.get(), a1, a6 >> 2 & 3) >> 8;
    FUN_800dcfb8(2, cam._2c.get(), a2, 0);
    cam.vec_60.setX((ratan2(s0, s2) & 0xfff) << 8);
    cam.vec_60.setY(0);
    cam.vec_60.setZ(SquareRoot0(s2 * s2 + s0 * s0) << 8);
    cam._5c.set(a3);
    final IntRef sp0x18 = new IntRef();
    final IntRef sp0x1c = new IntRef();
    FUN_800dcebc(a4, a5, cam.vec_60.getZ(), a3, sp0x18, sp0x1c);
    cam._30.set(FUN_800dcfb8(2, cam._2c.get(), a2, 0) / cam._5c.get());
    cam.vec_74.set(a0, a1, a2);
    cam._6c.set(sp0x18.get());
    cam._70.set((sp0x1c.get() - sp0x18.get()) / cam._5c.get());
    cam._11c.or(0x2L);
    cam._121.set(23);
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
      final IntRef sp0x18 = new IntRef();
      final IntRef sp0x1c = new IntRef();
      FUN_800dcebc(s3 - 1, s4 << 8, a2, s1, sp0x18, sp0x1c);
      cam._10c.set(sp0x18.get());
      cam._110.set((sp0x1c.get() - sp0x18.get()) / s1);
    }

    //LAB_800d8eec
    return 0;
  }

  @Method(0x800d8f10L)
  public static void FUN_800d8f10() {
    final BattleCamera cam = camera_800c67f0;

    if(cam._11c.get() != 0) {
      if((cam._11c.get() & 0x1L) != 0) {
        _800facbc.get(cam._120.get()).deref().run();
      }

      //LAB_800d8f80
      if((cam._11c.get() & 0x2L) != 0) {
        _800fad1c.get(cam._121.get()).deref().run();
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
    if(cam._118.get() != 0 && cam._108.get() != 0) {
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

  @Method(0x800d9154L)
  public static void FUN_800d9154() {
    final BattleCamera cam = camera_800c67f0;
    cam._ac.add(cam._b0.get());
    cam._b8.add(cam._bc.get());
    cam._a0.add(cam._c8.get());
    final IntRef sp0x18 = new IntRef().set(cam._ac.get() >> 8);
    final IntRef sp0x1c = new IntRef().set(cam._b8.get() >> 8);
    final IntRef sp0x20 = new IntRef().set(cam._a0.get() >> 8);
    FUN_800dcc94(0, 0, 0, sp0x18, sp0x1c, sp0x20);
    setViewpoint(sp0x18.get(), sp0x1c.get(), sp0x20.get());

    cam._d0.decr();
    if(cam._d0.get() <= 0) {
      cam._11c.and(0xffff_fffeL);
      cam._122.set(0);
    }

    //LAB_800d9210
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
      bobj.model_148.coord2_14.coord.transfer.getX() + (cam.vec_94.getX() >> 8),
      bobj.model_148.coord2_14.coord.transfer.getY() + (cam.vec_94.getY() >> 8),
      bobj.model_148.coord2_14.coord.transfer.getZ() + (cam.vec_94.getZ() >> 8)
    );

    cam._d0.decr();
    if(cam._d0.get() <= 0) {
      cam._120.set(6);
      cam._122.set(0);
    }

    //LAB_800d9428
  }

  @Method(0x800d9438L)
  public static void FUN_800d9438() {
    final BattleCamera cam = camera_800c67f0;
    cam._ac.add(cam._b0.get());
    cam._b8.add(cam._bc.get());
    cam._a0.add(cam._c8.get());
    final IntRef sp0x18 = new IntRef().set(cam._ac.get() >> 8);
    final IntRef sp0x1c = new IntRef().set(cam._b8.get() >> 8);
    final IntRef sp0x20 = new IntRef().set(cam._a0.get() >> 8);
    final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(cam.bobjIndex_f4.get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    FUN_800dcc94(bobj.model_148.coord2_14.coord.transfer.getX(), bobj.model_148.coord2_14.coord.transfer.getY(), bobj.model_148.coord2_14.coord.transfer.getZ(), sp0x18, sp0x1c, sp0x20);
    setViewpoint(sp0x18.get(), sp0x1c.get(), sp0x20.get());

    cam._d0.decr();
    if(cam._d0.get() <= 0) {
      cam._120.set(7);
      cam._122.set(0);
    }

    //LAB_800d9508
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

  @Method(0x800d9650L)
  public static void FUN_800d9650() {
    final BattleCamera s3 = camera_800c67f0;
    final IntRef sp0x18 = new IntRef().set(s3._d4.get() >> 8);
    final IntRef sp0x1c = new IntRef().set(s3._d8.get() >> 8);
    final IntRef sp0x20 = new IntRef().set(s3._dc.get() >> 8);
    FUN_800dcc94(0, 0, 0, sp0x18, sp0x1c, sp0x20);
    sp0x18.add(s3._e8.get() >> 8);
    sp0x1c.set(sp0x20).add(s3._ec.get() >> 8);
    s3._a0.add(s3._a4.get());
    sp0x20.set(s3._a0.get() >> 8);
    FUN_800dcc94(0, 0, 0, sp0x18, sp0x1c, sp0x20);
    setViewpoint(sp0x18.get(), sp0x1c.get(), sp0x20.get());
    s3._e0.add(s3._e4.get());
    s3._dc.sub(s3._e0.get());

    s3._d0.decr();
    if(s3._d0.get() <= 0) {
      s3._11c.and(0xffff_fffeL);
      s3._122.set(0);
    }

    //LAB_800d976c
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
    refY.set(refZ).add(cam._ec.get() >> 8);
    refZ.set(cam._a0.get() >> 8);
    FUN_800dcc94(0, 0, 0, refX, refY, refZ);
    setViewpoint(cam.rview2_00.refpoint_0c.getX() + refX.get(), cam.rview2_00.refpoint_0c.getY() + refY.get(), cam.rview2_00.refpoint_0c.getZ() + refZ.get());
    cam._e0.add(cam._e4.get());
    cam._dc.sub(cam._e0.get());

    cam._d0.decr();
    if(cam._d0.get() <= 0) {
      cam._ac.set(FUN_800dc384(0, 5, 0, 0) << 8);
      cam._b8.set(FUN_800dc384(0, 5, 1, 0) << 8);
      cam._a0.set(FUN_800dc384(0, 5, 2, 0) << 8);
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
    setViewpoint(bobj.model_148.coord2_14.coord.transfer.getX() + (cam.vec_94.getX() >> 8), bobj.model_148.coord2_14.coord.transfer.getY() + (cam.vec_94.getY() >> 8), bobj.model_148.coord2_14.coord.transfer.getZ() + (cam.vec_94.getZ() >> 8));

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
    refY.set(refZ).add(cam._ec.get() >> 8);
    refZ.set(cam._a0.get() >> 8);
    FUN_800dcc94(0, 0, 0, refX, refY, refZ);
    final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(cam.bobjIndex_f4.get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    setViewpoint(bobj.model_148.coord2_14.coord.transfer.getX() + refX.get(), bobj.model_148.coord2_14.coord.transfer.getY() + refY.get(), bobj.model_148.coord2_14.coord.transfer.getZ() + refZ.get());
    cam._e0.add(cam._e4.get());
    cam._dc.sub(cam._e0.get());

    cam._d0.decr();
    if(cam._d0.get() <= 0) {
      refX.set(cam.rview2_00.viewpoint_00.getX());
      refY.set(cam.rview2_00.viewpoint_00.getY());
      refZ.set(cam.rview2_00.viewpoint_00.getZ());
      FUN_800dcd9c(bobj.model_148.coord2_14.coord.transfer.getX(), bobj.model_148.coord2_14.coord.transfer.getY(), bobj.model_148.coord2_14.coord.transfer.getZ(), refX, refY, refZ);
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

  @Method(0x800d9e2cL)
  public static void FUN_800d9e2c() {
    final BattleCamera cam = camera_800c67f0;

    cam._38.add(cam._3c.get());
    cam._44.add(cam._48.get());
    cam._2c.add(cam._54.get());

    final IntRef sp0x18 = new IntRef().set(cam._38.get() >> 8);
    final IntRef sp0x1c = new IntRef().set(cam._44.get() >> 8);
    final IntRef sp0x20 = new IntRef().set(cam._2c.get() >> 8);
    FUN_800dcc94(0, 0, 0, sp0x18, sp0x1c, sp0x20);
    setRefpoint(sp0x18.get(), sp0x1c.get(), sp0x20.get());

    cam._5c.decr();
    if(cam._5c.get() <= 0) {
      cam._11c.and(0xffff_fffdL);
      cam._123.set(0);
    }

    //LAB_800d9ee8
  }

  @Method(0x800d9ef8L)
  public static void FUN_800d9ef8() {
    final BattleCamera cam = camera_800c67f0;

    cam.vec_20.x.add(cam._3c.get());
    cam.vec_20.y.add(cam._48.get());
    cam.vec_20.z.add(cam._54.get());

    setRefpoint(
      cam.rview2_00.viewpoint_00.getX() + (cam.vec_20.getX() >> 8),
      cam.rview2_00.viewpoint_00.getY() + (cam.vec_20.getY() >> 8),
      cam.rview2_00.viewpoint_00.getZ() + (cam.vec_20.getZ() >> 8)
    );

    cam._5c.decr();
    if(cam._5c.get() <= 0) {
      cam._121.set(2);
      cam._123.set(0);
    }

    //LAB_800d9f84
  }

  @Method(0x800d9f94L)
  public static void FUN_800d9f94() {
    final BattleCamera cam = camera_800c67f0;
    cam._38.add(cam._3c.get());
    cam._2c.add(cam._54.get());
    cam._44.add(cam._48.get());
    final IntRef sp0x18 = new IntRef().set(cam._38.get() >> 8);
    final IntRef sp0x1c = new IntRef().set(cam._44.get() >> 8);
    final IntRef sp0x20 = new IntRef().set(cam._2c.get() >> 8);
    FUN_800dcc94(cam.rview2_00.viewpoint_00.getX(), cam.rview2_00.viewpoint_00.getY(), cam.rview2_00.viewpoint_00.getZ(), sp0x18, sp0x1c, sp0x20);
    setRefpoint(sp0x18.get(), sp0x1c.get(), sp0x20.get());

    cam._5c.decr();
    if(cam._5c.get() <= 0) {
      cam._121.set(3);
      cam._123.set(0);
    }

    //LAB_800da048
  }

  @Method(0x800da058L)
  public static void FUN_800da058() {
    final BattleCamera cam = camera_800c67f0;

    cam.vec_20.x.add(cam._3c.get());
    cam.vec_20.y.add(cam._48.get());
    cam.vec_20.z.add(cam._54.get());

    final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(cam.bobjIndex_80.get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    setRefpoint(bobj.model_148.coord2_14.coord.transfer.getX() + (cam.vec_20.getX() >> 8), bobj.model_148.coord2_14.coord.transfer.getY() + (cam.vec_20.getY() >> 8), bobj.model_148.coord2_14.coord.transfer.getY() + (cam.vec_20.getZ() >> 8));

    cam._5c.decr();
    if(cam._5c.get() <= 0) {
      cam._121.set(6);
      cam._123.set(0);
    }

    //LAB_800da100
  }

  @Method(0x800da110L)
  public static void FUN_800da110() {
    final BattleCamera cam = camera_800c67f0;

    cam._38.add(cam._3c.get());
    cam._44.add(cam._48.get());
    cam._2c.add(cam._54.get());

    final IntRef sp0x18 = new IntRef().set(cam._38.get() >> 8);
    final IntRef sp0x1c = new IntRef().set(cam._44.get() >> 8);
    final IntRef sp0x20 = new IntRef().set(cam._2c.get() >> 8);
    final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(cam.bobjIndex_80.get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    FUN_800dcc94(bobj.model_148.coord2_14.coord.transfer.getX(), bobj.model_148.coord2_14.coord.transfer.getX(), bobj.model_148.coord2_14.coord.transfer.getX(), sp0x18, sp0x1c, sp0x20);
    setRefpoint(sp0x18.get(), sp0x1c.get(), sp0x20.get());

    cam._5c.decr();
    if(cam._5c.get() <= 0) {
      cam._121.set(7);
      cam._123.set(0);
    }

    //LAB_800da1e0
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

  @Method(0x800da328L)
  public static void FUN_800da328() {
    final BattleCamera cam = camera_800c67f0;

    final IntRef sp0x18 = new IntRef().set(cam.vec_60.getX() >> 8);
    final IntRef sp0x1c = new IntRef().set(cam.vec_60.getY() >> 8);
    final IntRef sp0x20 = new IntRef().set(cam.vec_60.getZ() >> 8);
    FUN_800dcc94(0, 0, 0, sp0x18, sp0x1c, sp0x20);

    cam._2c.add(cam._30.get());
    sp0x18.add(cam.vec_74.getX() >> 8);
    sp0x1c.set(sp0x20).add(cam.vec_74.getY() >> 8);
    sp0x20.set(cam._2c.get() >> 8);
    FUN_800dcc94(0, 0, 0, sp0x18, sp0x1c, sp0x20);
    setRefpoint(sp0x18.get(), sp0x1c.get(), sp0x20.get());

    cam._6c.add(cam._70.get());
    cam.vec_60.z.sub(cam._6c.get());
    cam._5c.decr();
    if(cam._5c.get() <= 0) {
      cam._11c.and(0xffff_fffdL);
      cam._123.set(0);
    }

    //LAB_800da444
  }

  @Method(0x800da460L)
  public static void FUN_800da460() {
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

    setRefpoint(
      cam.rview2_00.viewpoint_00.getX() + (cam.vec_20.getX() >> 8),
      cam.rview2_00.viewpoint_00.getY() + (cam.vec_20.getY() >> 8),
      cam.rview2_00.viewpoint_00.getZ() + (cam.vec_20.getZ() >> 8)
    );

    cam._5c.decr();
    if(cam._5c.get() <= 0) {
      cam._121.set(2);
      cam._123.set(0);
    }

    //LAB_800da594
  }

  @Method(0x800da5b0L)
  public static void FUN_800da5b0() {
    final BattleCamera cam = camera_800c67f0;

    final IntRef sp0x18 = new IntRef().set(cam.vec_60.getX() >> 8);
    final IntRef sp0x1c = new IntRef().set(cam.vec_60.getY() >> 8);
    final IntRef sp0x20 = new IntRef().set(cam.vec_60.getZ() >> 8);
    FUN_800dcc94(0, 0, 0, sp0x18, sp0x1c, sp0x20);

    cam._2c.add(cam._30.get());
    sp0x18.add(cam.vec_74.getX() >> 8);
    sp0x1c.set(sp0x20).add(cam.vec_74.getY() >> 8);
    sp0x20.set(cam._2c.get() >> 8);
    FUN_800dcc94(0, 0, 0, sp0x18, sp0x1c, sp0x20);
    setRefpoint(cam.rview2_00.viewpoint_00.getX() + sp0x18.get(), cam.rview2_00.viewpoint_00.getY() + sp0x1c.get(), cam.rview2_00.viewpoint_00.getZ() + sp0x20.get());

    cam._6c.add(cam._70.get());
    cam.vec_60.z.sub(cam._6c.get());

    cam._5c.decr();
    if(cam._5c.get() <= 0) {
      cam._38.set(FUN_800dc384(1, 3, 0, 0) << 8);
      cam._44.set(FUN_800dc384(1, 3, 1, 0) << 8);
      cam._2c.set(FUN_800dc384(1, 3, 2, 0) << 8);
      cam._121.set(3);
      cam._123.set(0);
    }

    //LAB_800da730
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
      bobj.model_148.coord2_14.coord.transfer.getX() + (cam.vec_20.getX() >> 8),
      bobj.model_148.coord2_14.coord.transfer.getY() + (cam.vec_20.getY() >> 8),
      bobj.model_148.coord2_14.coord.transfer.getZ() + (cam.vec_20.getZ() >> 8)
    );

    cam._5c.decr();
    if(cam._5c.get() <= 0) {
      cam._121.set(6);
      cam._123.set(0);
    }

    //LAB_800da8a0
  }

  @Method(0x800da8bcL)
  public static void FUN_800da8bc() {
    final BattleCamera cam = camera_800c67f0;
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0.get(cam.bobjIndex_80.get()).deref();

    final IntRef sp0x18 = new IntRef().set(cam.vec_60.getX() >> 8);
    final IntRef sp0x1c = new IntRef().set(cam.vec_60.getY() >> 8);
    final IntRef sp0x20 = new IntRef().set(cam.vec_60.getZ() >> 8);
    FUN_800dcc94(0, 0, 0, sp0x18, sp0x1c, sp0x20);

    cam._2c.add(cam._30.get());
    sp0x18.add(cam.vec_74.getX() >> 8);
    sp0x1c.set(sp0x20).add(cam.vec_74.getY() >> 8);
    sp0x20.set(cam._2c.get() >> 8);
    FUN_800dcc94(0, 0, 0, sp0x18, sp0x1c, sp0x20);

    final BattleObject27c bobj = state.innerStruct_00.derefAs(BattleObject27c.class);
    setRefpoint(bobj.model_148.coord2_14.coord.transfer.getX() + sp0x18.get(), bobj.model_148.coord2_14.coord.transfer.getY() + sp0x1c.get(), bobj.model_148.coord2_14.coord.transfer.getZ() + sp0x20.get());

    cam._6c.add(cam._70.get());
    cam.vec_60.z.sub(cam._6c.get());

    cam._5c.decr();
    if(cam._5c.get() <= 0) {
      sp0x18.set(cam.rview2_00.refpoint_0c.getX());
      sp0x1c.set(cam.rview2_00.refpoint_0c.getY());
      sp0x20.set(cam.rview2_00.refpoint_0c.getZ());
      FUN_800dcd9c(bobj.model_148.coord2_14.coord.transfer.getX(), bobj.model_148.coord2_14.coord.transfer.getY(), bobj.model_148.coord2_14.coord.transfer.getZ(), sp0x18, sp0x1c, sp0x20);
      cam._38.set(sp0x18.get() << 8);
      cam._44.set(sp0x1c.get() << 8);
      cam._2c.set(sp0x20.get() << 8);
      cam._121.set(7);
      cam._123.set(0);
    }

    //LAB_800daa60
  }

  @Method(0x800daa80L)
  public static void FUN_800daa80() {
    if(_800fabb8.get() == 0x1L) {
      if(_800c67d4.get() != 0) {
        _800c67d4.subu(0x1L);
        return;
      }

      //LAB_800daabc
      final int x;
      final int y;
      final long a0 = _800bb0fc.get() & 0x3L;
      if(a0 == 0) {
        //LAB_800dab04
        x = (int)_800c67e4.get();
        y = (int)_800c67e8.get() * 2;
      } else if(a0 == 0x1L) {
        //LAB_800dab1c
        x = -(int)_800c67e4.get() * 2;
        y = -(int)_800c67e8.get();
        //LAB_800daaec
      } else if(a0 == 0x2L) {
        //LAB_800dab3c
        x = (int)_800c67e4.get() * 2;
        y = (int)_800c67e8.get();
      } else {
        //LAB_800dab54
        x = -(int)_800c67e4.get();
        y = -(int)_800c67e8.get() * 2;
      }

      //LAB_800dab70
      //LAB_800dab78
      SetGeomOffset(screenOffsetX_800c67bc.get() + x, screenOffsetY_800c67c0.get() + y);

      _800c67c4.subu(0x1L);
      if(_800c67c4.getSigned() <= 0) {
        _800fabb8.setu(0);
        SetGeomOffset(screenOffsetX_800c67bc.get(), screenOffsetY_800c67c0.get());
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
    camera_800c67f0.callbackIndex_fc.set(index);
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

  @Method(0x800dadc0L)
  public static void FUN_800dadc0(final int x, final int y, final int z, final int scriptIndex) {
    // no-op
  }

  @Method(0x800dadc8L)
  public static void FUN_800dadc8(final int x, final int y, final int z, final int scriptIndex) {
    // no-op
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
    FUN_800dcc94(bobj.model_148.coord2_14.coord.transfer.getX(), bobj.model_148.coord2_14.coord.transfer.getY(), bobj.model_148.coord2_14.coord.transfer.getZ(), refX, refY, refZ);
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
    camera_800c67f0.callbackIndex_88.set(index);
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

  @Method(0x800db1d4L)
  public static void FUN_800db1d4(final int x, final int y, final int z, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.vec_20.set(x, y, z);
    setRefpoint(cam.rview2_00.viewpoint_00.getX() + (cam.vec_20.getX() >> 8), cam.rview2_00.viewpoint_00.getY() + (cam.vec_20.getY() >> 8), cam.rview2_00.viewpoint_00.getZ() + (cam.vec_20.getZ() >> 8));
    cam._11c.or(0x2L);
    cam._121.set(2);
  }

  @Method(0x800db240L)
  public static void FUN_800db240(final int x, final int y, final int z, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam._38.set(x);
    cam._44.set(y);
    cam._2c.set(z);
    final IntRef refX = new IntRef().set(cam._38.get() >> 8);
    final IntRef refY = new IntRef().set(cam._44.get() >> 8);
    final IntRef refZ = new IntRef().set(cam._2c.get() >> 8);
    FUN_800dcc94(cam.rview2_00.viewpoint_00.getX(), cam.rview2_00.viewpoint_00.getY(), cam.rview2_00.viewpoint_00.getZ(), refX, refY, refZ);
    setRefpoint(refX.get(), refY.get(), refZ.get());
    cam._11c.or(0x2L);
    cam._121.set(3);
  }

  @Method(0x800db2e0L)
  public static void FUN_800db2e0(final int x, final int y, final int z, final int scriptIndex) {
    // no-op
  }

  @Method(0x800db2e8L)
  public static void FUN_800db2e8(final int x, final int y, final int z, final int scriptIndex) {
    // no-op
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

  @Method(0x800db398L)
  public static void FUN_800db398(final int x, final int y, final int z, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam._38.set(x);
    cam._44.set(y);
    cam._2c.set(z);
    final IntRef refX = new IntRef().set(x >> 8);
    final IntRef refY = new IntRef().set(y >> 8);
    final IntRef refZ = new IntRef().set(z >> 8);
    final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(BattleObject27c.class);
    FUN_800dcc94(bobj.model_148.coord2_14.coord.transfer.getX(), bobj.model_148.coord2_14.coord.transfer.getY(), bobj.model_148.coord2_14.coord.transfer.getZ(), refX, refY, refZ);
    setRefpoint(refX.get(), refY.get(), refZ.get());
    cam.bobjIndex_80.set(scriptIndex);
    cam._11c.or(0x2L);
    cam._121.set(7);
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

  @Method(0x800db564L)
  public static void FUN_800db564(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    // no-op
  }

  @Method(0x800db56cL)
  public static void FUN_800db56c(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    // no-op
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

  @Method(0x800db678L)
  public static void FUN_800db678(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    // no-op
  }

  @Method(0x800db680L)
  public static void FUN_800db680(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    // no-op
  }

  @Method(0x800db688L)
  public static long FUN_800db688(final RunningScript a0) {
    FUN_800db714(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get(), a0.params_20.get(4).deref().get(), a0.params_20.get(5).deref().get(), a0.params_20.get(6).deref().get(), a0.params_20.get(7).deref().get());
    return 0;
  }

  @Method(0x800db714L)
  public static void FUN_800db714(final int callbackIndex, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int a7) {
    _800fac1c.offset(callbackIndex * 0x4L).deref(4).call(a1, a2, a3, a4, a5, a6, a7);

    final BattleCamera cam = camera_800c67f0;
    cam.callbackIndex_fc.set(callbackIndex);
    cam._122.set(1);
  }

  @Method(0x800db79cL)
  public static long FUN_800db79c(final RunningScript script) {
    FUN_800db828(script.params_20.get(0).deref().get(), script.params_20.get(1).deref().get(), script.params_20.get(2).deref().get(), script.params_20.get(3).deref().get(), script.params_20.get(4).deref().get(), script.params_20.get(5).deref().get(), script.params_20.get(6).deref().get(), script.params_20.get(7).deref().get());
    return 0;
  }

  @Method(0x800db828L)
  public static void FUN_800db828(final int callbackIndex, final int x, final int y, final int z, final int a4, final int a5, final int a6, final int a7) {
    _800fac7c.offset(callbackIndex * 0x4L).deref(4).call(x, y, z, a4, a5, a6, a7);
    final var cam = camera_800c67f0;
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

  @Method(0x800db9d0L)
  public static void FUN_800db9d0(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    // no-op
  }

  @Method(0x800db9d8L)
  public static void FUN_800db9d8(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    // no-op
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

  @Method(0x800dbb00L)
  public static void FUN_800dbb00(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    // no-op
  }

  @Method(0x800dbb08L)
  public static void FUN_800dbb08(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    // no-op
  }

  @Method(0x800dbb10L)
  public static long FUN_800dbb10(final RunningScript a0) {
    final long v1 = a0.params_20.get(0).deref().get();
    final long a1;
    if(v1 == 0) {
      //LAB_800dbb3c
      a1 = _800c6912.get();
    } else if(v1 == 1) {
      //LAB_800dbb48
      a1 = _800c6913.get();
    } else {
      throw new RuntimeException("Undefined a1");
    }

    //LAB_800dbb50
    a0.params_20.get(1).deref().set((int)a1);
    return 0;
  }

  @Method(0x800dbc2cL)
  public static long scriptSetViewportTwist(final RunningScript a0) {
    camera_800c67f0.rview2_00.viewpointTwist_18.set(a0.params_20.get(0).deref().get());
    return 0;
  }

  @Method(0x800dbc80L)
  public static long FUN_800dbc80(final RunningScript a0) {
    final int v1 = a0.params_20.get(0).deref().get();

    if((v1 & 0x1) != 0) {
      camera_800c67f0._e4.set(0);
      camera_800c67f0._b4.set(0);
    }

    //LAB_800dbca8
    if((v1 & 0x2) != 0) {
      camera_800c67f0._70.set(0);
      camera_800c67f0._40.set(0);
    }

    //LAB_800dbcc0
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

  @Method(0x800dbcfcL)
  public static long scriptGetProjectionPlaneDistance(final RunningScript a0) {
    a0.params_20.get(0).deref().set(getProjectionPlaneDistance());
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

  @Method(0x800dbe80L)
  public static void FUN_800dbe80() {
    _800c6912.setu(0);
  }

  @Method(0x800dbe8cL)
  public static void FUN_800dbe8c() {
    _800c6912.setu(0);
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
    FUN_800dcc94(bobj.model_148.coord2_14.coord.transfer.getX(), bobj.model_148.coord2_14.coord.transfer.getY(), bobj.model_148.coord2_14.coord.transfer.getZ(), refX, refY, refZ);
    setViewpoint(refX.get(), refY.get(), refZ.get());
  }

  @Method(0x800dc070L)
  public static void FUN_800dc070() {
    // no-op
  }

  @Method(0x800dc078L)
  public static void FUN_800dc078() {
    // no-op
  }

  @Method(0x800dc080L)
  public static void FUN_800dc080() {
    // no-op
  }

  @Method(0x800dc088L)
  public static void FUN_800dc088() {
    // no-op
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

  @Method(0x800dc0d0L)
  public static void FUN_800dc0d0() {
    final BattleCamera cam = camera_800c67f0;
    cam._123.set(0);

    setRefpoint(
      cam.rview2_00.viewpoint_00.getX() + (cam.vec_20.getX() >> 8),
      cam.rview2_00.viewpoint_00.getY() + (cam.vec_20.getY() >> 8),
      cam.rview2_00.viewpoint_00.getZ() + (cam.vec_20.getZ() >> 8)
    );
  }

  @Method(0x800dc128L)
  public static void FUN_800dc128() {
    final BattleCamera cam = camera_800c67f0;
    cam._123.set(0);

    final IntRef sp0x18 = new IntRef().set(cam._38.get() >> 8);
    final IntRef sp0x1c = new IntRef().set(cam._44.get() >> 8);
    final IntRef sp0x20 = new IntRef().set(cam._2c.get() >> 8);
    FUN_800dcc94(cam.rview2_00.viewpoint_00.getX(), cam.rview2_00.viewpoint_00.getY(), cam.rview2_00.viewpoint_00.getZ(), sp0x18, sp0x1c, sp0x20);
    setRefpoint(sp0x18.get(), sp0x1c.get(), sp0x20.get());
  }

  @Method(0x800dc1a8L)
  public static void FUN_800dc1a8() {
    // no-op
  }

  @Method(0x800dc1b0L)
  public static void FUN_800dc1b0() {
    // no-op
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

  @Method(0x800dc21cL)
  public static void FUN_800dc21c() {
    final BattleCamera cam = camera_800c67f0;
    cam._123.set(0);

    final IntRef sp0x18 = new IntRef().set(cam._38.get() >> 8);
    final IntRef sp0x1c = new IntRef().set(cam._44.get() >> 8);
    final IntRef sp0x20 = new IntRef().set(cam._2c.get() >> 8);
    final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(cam.bobjIndex_80.get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    FUN_800dcc94(bobj.model_148.coord2_14.coord.transfer.getX(), bobj.model_148.coord2_14.coord.transfer.getY(), bobj.model_148.coord2_14.coord.transfer.getZ(), sp0x18, sp0x1c, sp0x20);
    setRefpoint(sp0x18.get(), sp0x1c.get(), sp0x20.get());
  }

  @Method(0x800dc2b8L)
  public static void FUN_800dc2b8() {
    // no-op
  }

  @Method(0x800dc2c0L)
  public static void FUN_800dc2c0() {
    // no-op
  }

  @Method(0x800dc2c8L)
  public static void FUN_800dc2c8() {
    // no-op
  }

  @Method(0x800dc2d0L)
  public static void FUN_800dc2d0() {
    // no-op
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

  @Method(0x800dc504L)
  public static int FUN_800dc504(final int component, final int scriptIndex, final int x, final int y, final int z) {
    // no-op
    return 0;
  }

  @Method(0x800dc50cL)
  public static int FUN_800dc50c(final int component, final int scriptIndex, final int x, final int y, final int z) {
    // no-op
    return 0;
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
    FUN_800dcd9c(bobj.model_148.coord2_14.coord.transfer.getX(), bobj.model_148.coord2_14.coord.transfer.getY(), bobj.model_148.coord2_14.coord.transfer.getZ(), refX, refY, refZ);

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

  @Method(0x800dc894L)
  public static int FUN_800dc894(final int component, final int scriptIndex, final int x, final int y, final int z) {
    if(component == 0) {
      //LAB_800dc8d0
      return x - camera_800c67f0.rview2_00.viewpoint_00.getX();
    }

    if(component == 1) {
      //LAB_800dc8e0
      return y - camera_800c67f0.rview2_00.viewpoint_00.getY();
    }

    //LAB_800dc8bc
    if(component == 2) {
      //LAB_800dc8ec
      return z - camera_800c67f0.rview2_00.viewpoint_00.getZ();
    }

    throw new IllegalArgumentException("Illegal component " + component);
  }

  @Method(0x800dc900L)
  public static int FUN_800dc900(final int component, final int scriptIndex, final int x, final int y, final int z) {
    final BattleCamera cam = camera_800c67f0;

    final IntRef sp0x18 = new IntRef().set(x);
    final IntRef sp0x1c = new IntRef().set(y);
    final IntRef sp0x20 = new IntRef().set(z);
    FUN_800dcd9c(cam.rview2_00.viewpoint_00.getX(), cam.rview2_00.viewpoint_00.getY(), cam.rview2_00.viewpoint_00.getZ(), sp0x18, sp0x1c, sp0x20);

    if(component == 0) {
      //LAB_800dc984
      return sp0x18.get();
    }

    if(component == 1) {
      //LAB_800dc990
      return sp0x1c.get();
    }

    //LAB_800dc970
    if(component == 2) {
      //LAB_800dc99c
      return sp0x20.get();
    }

    //LAB_800dc9a0
    throw new IllegalArgumentException("Illegal component " + component);
  }

  @Method(0x800dc9b0L)
  public static int FUN_800dc9b0(final int component, final int scriptIndex, final int x, final int y, final int z) {
    // no-op
    return 0;
  }

  @Method(0x800dc9b8L)
  public static int FUN_800dc9b8(final int component, final int scriptIndex, final int x, final int y, final int z) {
    // no-op
    return 0;
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

  @Method(0x800dca68L)
  public static int FUN_800dca68(final int component, final int scriptIndex, final int x, final int y, final int z) {
    final IntRef sp0x18 = new IntRef().set(x);
    final IntRef sp0x1c = new IntRef().set(y);
    final IntRef sp0x20 = new IntRef().set(z);
    final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(BattleObject27c.class);
    FUN_800dcd9c(bobj.model_148.coord2_14.coord.transfer.getX(), bobj.model_148.coord2_14.coord.transfer.getY(), bobj.model_148.coord2_14.coord.transfer.getZ(), sp0x18, sp0x1c, sp0x20);

    if(component == 0) {
      //LAB_800dcafc
      return sp0x18.get();
    }

    if(component == 1) {
      //LAB_800dcb08
      return sp0x1c.get();
    }

    //LAB_800dcae8
    if(component == 2) {
      //LAB_800dcb14
      return sp0x20.get();
    }

    //LAB_800dcb18
    throw new IllegalArgumentException("Illegal component " + component);
  }

  @Method(0x800dcb84L)
  public static long FUN_800dcb84(final RunningScript a0) {
    final BattleCamera cam = camera_800c67f0;
    final int a1 = a0.params_20.get(0).deref().get();

    if((a1 & 0x1L) != 0) {
      cam._120.set(0);
      cam._122.set(0);
      cam._11c.and(0xffff_fffeL);
    }

    //LAB_800dcbbc
    if((a1 & 0x2L) != 0) {
      cam._121.set(0);
      cam._123.set(0);
      cam._11c.and(0xffff_fffdL);
    }

    //LAB_800dcbe4
    return 0;
  }

  @Method(0x800dcbecL)
  public static long FUN_800dcbec(final RunningScript a0) {
    _800c67c4.setu(a0.params_20.get(0).deref().get());
    _800c67d4.setu(a0.params_20.get(1).deref().get());
    _800c67e4.setu(a0.params_20.get(2).deref().get());
    _800c67e8.setu(a0.params_20.get(3).deref().get());
    _800fabb8.setu(0x1L);
    getScreenOffset(screenOffsetX_800c67bc, screenOffsetY_800c67c0);
    return 0;
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
  public static void FUN_800dcc94(final int a0, final int a1, final int a2, final IntRef x, final IntRef y, final IntRef z) {
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

    final int halfDx = dx / 2;
    final int halfDy = dy / 2;
    final int halfDz = dz / 2;

    x.set(ratan2(dz, dx) & 0xfff);
    y.set(ratan2(dy, SquareRoot0(halfDx * halfDx + halfDz * halfDz) * 2) & 0xfff);
    z.set(SquareRoot0(halfDx * halfDx + halfDy * halfDy + halfDz * halfDz) * 2);
  }

  @Method(0x800dcebcL)
  public static void FUN_800dcebc(final int a0, final int a1, final int a2, final int a3, final IntRef a4, final IntRef a5) {
    if(a0 == 0) {
      //LAB_800dcedc
      a4.set(a1);
      a5.set(a2 * 2 / a3 - a1);
    } else if(a0 == 1) {
      //LAB_800dcef8
      a4.set(a2 * 2 / a3 - a1);
      a5.set(a1);
    }
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
  public static VECTOR FUN_800dd02c(final int scriptIndex) {
    final BattleScriptDataBase data = scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(BattleScriptDataBase.class);

    if(data.magic_00.get() == BattleScriptDataBase.EM__) {
      return ((EffectManagerData6c)data)._10.trans_04;
    }

    return ((BattleObject27c)data).model_148.coord2_14.coord.transfer;
  }

  @Method(0x800dd0d4L)
  public static int FUN_800dd0d4() {
    final BattleCamera cam = camera_800c67f0;
    return (int)_800fad7c.offset(5 * 0x4L).deref(4).call(1, 0, cam.rview2_00.viewpoint_00.getX(), cam.rview2_00.viewpoint_00.getY(), cam.rview2_00.viewpoint_00.getZ());
  }

  @Method(0x800dd118L)
  public static int FUN_800dd118() {
    final BattleCamera cam = camera_800c67f0;
    return (int)_800fad7c.offset(5 * 0x4L).deref(4).call(0, 0, cam.rview2_00.viewpoint_00.getX(), cam.rview2_00.viewpoint_00.getY(), cam.rview2_00.viewpoint_00.getZ());
  }

  @Method(0x800dd15cL)
  public static MATRIX FUN_800dd15c(final MATRIX a0, final MATRIX a1, final int a2) {
    if(a2 > 0) {
      if(a2 != 0x800) {
        final int v1 = 0x1000 - a2;
        a0.set(0, (short)(a0.get(0) * v1 + a1.get(0) * a2 >> 12));
        a0.set(1, (short)(a0.get(1) * v1 + a1.get(1) * a2 >> 12));
        a0.set(2, (short)(a0.get(2) * v1 + a1.get(2) * a2 >> 12));
        a0.set(3, (short)(a0.get(3) * v1 + a1.get(3) * a2 >> 12));
        a0.set(4, (short)(a0.get(4) * v1 + a1.get(4) * a2 >> 12));
        a0.set(5, (short)(a0.get(5) * v1 + a1.get(5) * a2 >> 12));
        a0.set(6, (short)(a0.get(6) * v1 + a1.get(6) * a2 >> 12));
        a0.set(7, (short)(a0.get(7) * v1 + a1.get(7) * a2 >> 12));
        a0.set(8, (short)(a0.get(8) * v1 + a1.get(8) * a2 >> 12));
        a0.transfer.setX(a0.transfer.getX() * v1 + a1.transfer.getX() * a2 >> 12);
        a0.transfer.setY(a0.transfer.getY() * v1 + a1.transfer.getY() * a2 >> 12);
        a0.transfer.setZ(a0.transfer.getZ() * v1 + a1.transfer.getZ() * a2 >> 12);
      } else {
        //LAB_800dd394
        a0.set(0, (short)(a0.get(0) + a1.get(0) >> 1));
        a0.set(1, (short)(a0.get(1) + a1.get(1) >> 1));
        a0.set(2, (short)(a0.get(2) + a1.get(2) >> 1));
        a0.set(3, (short)(a0.get(3) + a1.get(3) >> 1));
        a0.set(4, (short)(a0.get(4) + a1.get(4) >> 1));
        a0.set(5, (short)(a0.get(5) + a1.get(5) >> 1));
        a0.set(6, (short)(a0.get(6) + a1.get(6) >> 1));
        a0.set(7, (short)(a0.get(7) + a1.get(7) >> 1));
        a0.set(8, (short)(a0.get(8) + a1.get(8) >> 1));
        a0.transfer.setX(a0.transfer.getX() + a1.transfer.getX() >> 1);
        a0.transfer.setY(a0.transfer.getY() + a1.transfer.getY() >> 1);
        a0.transfer.setZ(a0.transfer.getZ() + a1.transfer.getZ() >> 1);
        FUN_8003eba0(a0, a0);
      }
    }

    //LAB_800dd4b8
    //LAB_800dd4bc
    return a0;
  }

  @Method(0x800dd4ccL)
  public static long FUN_800dd4cc(final Model124 a0, final long a1) {
    long v0;
    long v1;
    final long s0;
    final long s2;
    final long hi;
    final long lo;
    v1 = a0.ub_9c.get();
    if(v1 == 0x2L) {
      return v1;
    }

    //LAB_800dd4fc
    if(a0.ub_a2.get() != 0) {
      v0 = a0.s_9a.get();
      v0 = v0 << 16;
      v0 = (int)v0 >> 17;
      hi = (a1 & 0xffff_ffffL) % (v0 & 0xffff_ffffL);
      s2 = hi;
      v1 = a0.animCount_98.get();
      v0 = s2 << 1;
      v0 = v0 + s2;
      lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
      a0.partTransforms_94.setPointer(a0.partTransforms_90.getPointer()); //TODO
      v1 = lo;
      a0.partTransforms_94.setPointer(a0.partTransforms_90.deref().get((int)v1).getAddress()); //TODO
      applyModelPartTransforms(a0);
      v0 = a0.s_9a.get();
      v0 = v0 << 16;
      v0 = (int)v0 >> 17;
    } else {
      //LAB_800dd568
      v0 = a0.s_9a.get();
      hi = (a1 & 0xffff_ffffL) % (v0 & 0xffff_ffffL);
      s2 = hi;
      v1 = s2 >>> 1;
      v0 = v1 << 1;
      v0 = v0 + v1;
      lo = a0.animCount_98.get() * (int)v0 & 0xffff_ffffL;
      a0.partTransforms_94.setPointer(a0.partTransforms_90.getPointer()); //TODO
      v1 = lo;
      a0.partTransforms_94.setPointer(a0.partTransforms_90.deref().get((int)v1).getAddress()); //TODO
      applyModelPartTransforms(a0);
      if((s2 & 0x1L) != 0) {
        if(s2 != a0.s_9a.get() - 1) {
          if(a0.ub_a3.get() == 0) {
            s0 = a0.partTransforms_94.getPointer();
            FUN_800213c4(a0);
            a0.partTransforms_94.setPointer(s0); //TODO
          }
        }
      }

      //LAB_800dd5ec
      v0 = a0.s_9a.get();
    }

    //LAB_800dd5f0
    a0.s_9e.set((short)(v0 - s2 - 1));

    if(a0.s_9e.get() == 0) {
      a0.ub_9c.set(0);
    } else {
      //LAB_800dd618
      a0.ub_9c.set(1);
    }

    //LAB_800dd61c
    //LAB_800dd620
    return a0.s_9e.get();
  }

  @Method(0x800dd638L)
  public static long FUN_800dd638(final Model124 a0, final long a1) {
    long v0;
    final long v1;
    long s3;
    final long s4;
    final long s5;
    final long s6;
    if(a0.ub_9c.get() == 0x2L) {
      return 0x2L;
    }

    //LAB_800dd680
    s5 = Math.min(a0.count_c8.get(), a0.animCount_98.get());

    //LAB_800dd69c
    v0 = a0.coord2ParamArrPtr_08.getPointer(); //TODO
    s4 = MEMORY.ref(4, v0).offset(0x0L).get();

    final long a0_0;
    if(a0.ub_a2.get() != 0) {
      v1 = a1 * 2 % a0.s_9a.get();
      s6 = 0;
      v0 = a0.s_9a.get() >> 1;
      a0_0 = v1 >>> 1;
      v0 = v0 - a0_0;
    } else {
      //LAB_800dd6dc
      v1 = a1 % a0.s_9a.get();
      v0 = a1 & 0x1L;
      s6 = v0 << 11;
      a0_0 = v1 >>> 1;
      v0 = a0.s_9a.get() - v1;
    }

    //LAB_800dd700
    a0.s_9e.set((short)(v0 - 1));
    s3 = 0x8L;

    //LAB_800dd720
    for(int i = 0; i < s5; i++) {
      long a1_0 = s4 + MEMORY.ref(4, s4).offset(s3).offset(0x8L).get() + a0_0 * 0x14L;
      final MATRIX s0 = a0.dobj2ArrPtr_00.deref().get(i).coord2_04.deref().coord;

      final VECTOR sp0x10 = new VECTOR();
      final SVECTOR sp0x20 = new SVECTOR();
      final VECTOR sp0x28 = new VECTOR();
      sp0x10.set((SVECTOR)MEMORY.ref(2, a1_0).offset(0x6L).cast(SVECTOR::new));
      sp0x20.set((SVECTOR)MEMORY.ref(2, a1_0).offset(0xcL).cast(SVECTOR::new));
      sp0x28.set((SVECTOR)MEMORY.ref(2, a1_0).offset(0x0L).cast(SVECTOR::new));

      if(s6 != 0) {
        if(a1 == a0.s_9a.get() - 1) {
          a1_0 = s4 + MEMORY.ref(4, s4).offset(s3).offset(0x8L).get();
        } else {
          //LAB_800dd7cc
          a1_0 = a1_0 + 0x14L;
        }

        //LAB_800dd7d0
        sp0x10.add((SVECTOR)MEMORY.ref(2, a1_0).offset(0x6L).cast(SVECTOR::new)).div(2);
      }

      //LAB_800dd818
      RotMatrix_80040010(sp0x20, s0);
      TransMatrix(s0, sp0x10);
      ScaleMatrixL(s0, sp0x28);
      s3 = s3 + 0xcL;
    }

    //LAB_800dd84c
    if(a0.s_9e.get() == 0) {
      a0.ub_9c.set(0);
    } else {
      //LAB_800dd864
      a0.ub_9c.set(1);
    }

    //LAB_800dd868
    //LAB_800dd86c
    return a0.s_9e.get();
  }

  @Method(0x800dd89cL)
  public static void FUN_800dd89c(final Model124 model, final long a1) {
    final long v0;
    final long v1;
    long s0;
    long s4;
    long s6;
    long s7;
    final long fp;
    final long sp50;
    s4 = 0x1L;
    s7 = model.ui_f4.get();
    fp = model.ObjTable_0c.nobj.get();
    zOffset_1f8003e8.set(model.zOffset_a0.get());
    sp50 = model.ui_f8.get();
    _1f8003ec.setu(model.ui_108.get());
    s6 = struct7cc_800c693c.deref()._20.get() & 0x4L;
    v1 = (int)s6 >> 1;
    v0 = (int)s6 >> 2;
    s6 = v1 | v0;

    //LAB_800dd928
    for(int i = 0; i < fp; i++) {
      final GsDOBJ2 s2 = model.ObjTable_0c.top.deref().get(i);

      if(s4 == 0) {
        s4 = 0x1L;
        s7 = sp50;
      }

      //LAB_800dd940
      if((s4 & s7) == 0) {
        final MATRIX lw = new MATRIX();
        final MATRIX ls = new MATRIX();
        GsGetLws(s2.coord2_04.deref(), lw, ls);

        if((s6 & (ls.transfer.getZ() ^ _800bb0fc.get())) == 0 || ls.transfer.getZ() - ls.transfer.getX() >= -0x800L && ls.transfer.getZ() + ls.transfer.getX() >= -0x800L && ls.transfer.getZ() - ls.transfer.getY() >= -0x800L && ls.transfer.getZ() + ls.transfer.getY() >= -0x800L) {
          //LAB_800dd9bc
          if((a1 & 0x8L) != 0) {
            FUN_8003eba0(lw, lw);
          }

          //LAB_800dd9d8
          GsSetLightMatrix(lw);
          CPU.CTC2(ls.getPacked(0), 0);
          CPU.CTC2(ls.getPacked(2), 1);
          CPU.CTC2(ls.getPacked(4), 2);
          CPU.CTC2(ls.getPacked(6), 3);
          CPU.CTC2(ls.getPacked(8), 4);
          CPU.CTC2(ls.transfer.getX(), 5);
          CPU.CTC2(ls.transfer.getY(), 6);
          CPU.CTC2(ls.transfer.getZ(), 7);
          s0 = s2.attribute_00.get();
          s2.attribute_00.set(a1);
          renderCtmd(s2);
          s2.attribute_00.set(s0);
        }
      }

      //LAB_800dda3c
      s4 = (int)(s4 << 1);
    }

    //LAB_800dda54
    //LAB_800dda58
    for(int i = 0; i < 7; i++) {
      if(model.aub_ec.get(i).get() != 0) {
        FUN_80022018(model, i);
      }

      //LAB_800dda70
    }

    if(model.b_cc.get() != 0) {
      FUN_80021724(model);
    }

    //LAB_800dda98
  }

  @Method(0x800ddac8L)
  public static void FUN_800ddac8(final Model124 model, final long a1) {
    final VECTOR sp0x18 = new VECTOR().set(model.coord2_14.coord.transfer);

    //LAB_800ddb18
    for(int i = 0; i < 7; i++) {
      model.aub_ec.get(i).set(0);
    }

    model.tmd_8c.set(MEMORY.ref(4, a1 + 0x10L, Tmd::new));
    final short count = (short)MEMORY.ref(4, a1).offset(0x14L).get();
    model.tmdNobj_ca.set(count);
    model.count_c8.set(count);
    long v0 = mallocHead(count * 0x10 + count * 0x50 + count * 0x28);
    model.dobj2ArrPtr_00.setPointer(v0);
    v0 = v0 + model.count_c8.get() * 0x10;
    model.coord2ArrPtr_04.setPointer(v0);
    v0 = v0 + model.count_c8.get() * 0x50;
    model.coord2ParamArrPtr_08.setPointer(v0);
    model.ui_108.set((MEMORY.ref(4, a1).offset(0xcL).get() & 0xffff_0000L) >>> 11);
    adjustTmdPointers(model.tmd_8c.deref());
    initObjTable2(model.ObjTable_0c, model.dobj2ArrPtr_00.deref(), model.coord2ArrPtr_04.deref(), model.coord2ParamArrPtr_08.deref(), model.count_c8.get());
    model.coord2_14.param.set(model.coord2Param_64);
    GsInitCoordinate2(null, model.coord2_14);
    model.ObjTable_0c.nobj.set(count);

    final long s3 = a1 + 0xcL;

    //LAB_800ddc0c
    for(int i = 0; i < count; i++) {
      if((MEMORY.ref(4, s3).offset(0x4L).get() & 0x2L) != 0) {
        model.dobj2ArrPtr_00.deref().get(i).tmd_08.setPointer(s3 + i * 0x1cL + 0xcL); //TODO
      } else {
        final Memory.TemporaryReservation tmp = MEMORY.temp(0x10);
        final GsDOBJ2 dobj2 = tmp.get().cast(GsDOBJ2::new);
        updateTmdPacketIlen(MEMORY.ref(4, a1 + 0x18L, UnboundedArrayRef.of(0x1c, TmdObjTable::new)), dobj2, i);
        model.dobj2ArrPtr_00.deref().get(i).tmd_08.set(dobj2.tmd_08.deref());
        tmp.release();
      }

      //LAB_800ddc34
    }

    //LAB_800ddc54
    //LAB_800ddc64
    for(int i = 0; i < count; i++) {
      model.coord2ArrPtr_04.deref().get(i).super_.set(model.coord2_14);
    }

    //LAB_800ddc80
    model.ub_a2.set(0);
    model.ub_a3.set(0);
    model.ui_f4.set(0);
    model.ui_f8.set(0);
    model.zOffset_a0.set((short)0);
    model.coord2_14.coord.transfer.set(sp0x18);

    if((model.tmd_8c.deref().header.flags.get() & 0x2L) == 0 && model.ub_9d.get() != 0) {
      FUN_80021628(model);
    }

    //LAB_800ddce8
    model.scaleVector_fc.set(0x1000, 0x1000, 0x1000);
    model.b_cc.set(0);
    model.vector_10c.set(0x1000, 0x1000, 0x1000);
    model.vector_118.set(0, 0, 0);
  }

  @Method(0x800ddd3cL)
  public static long FUN_800ddd3c(final Model124 a0, final long a1) {
    long v0;
    long v1;
    long a2;
    final long a3;
    long t0;
    final long t1;
    final long t2;
    final long t3;
    long s0;
    long s3;
    long s4;
    final long fp;
    if(a0.ub_9c.get() == 0x2L) {
      return 0x2L;
    }

    fp = Math.min(a0.count_c8.get(), a0.animCount_98.get());

    //LAB_800ddd9c
    final long s7 = a0.coord2ParamArrPtr_08.getPointer(); //TODO
    v0 = MEMORY.ref(4, s7).offset(0x4L).get();
    a2 = MEMORY.ref(4, s7).offset(0x0L).get();
    t1 = MEMORY.ref(2, v0).offset(0xcL).get();
    if(a1 == a2) {
      return a0.ub_9c.get();
    }

    //LAB_800dddc4
    final long a1_0;
    if(a0.ub_a2.get() != 0) {
      t3 = 0;
      a1_0 = (a1 << 1) % a0.s_9a.get() >>> 1;
      t0 = (a2 << 1) % a0.s_9a.get() >> 1;
      a0.s_9e.set((short)((a0.s_9a.get() >> 1) - a1_0 - 1));
    } else {
      //LAB_800dde1c
      v1 = a1 % a0.s_9a.get();
      t3 = (a1 & 0x1L) << 11;
      a1_0 = v1 >>> 1;
      t0 = (int)a2 % a0.s_9a.get() >> 1;
      a0.s_9e.set((short)(a0.s_9a.get() - v1 - 1));
    }

    //LAB_800dde60
    if((int)a1_0 < (int)t0) {
      v0 = MEMORY.ref(4, s7).offset(0x4L).get();
      a3 = MEMORY.ref(2, v0).offset(0xcL).get();
      v1 = v0 + 0x10L;

      //LAB_800dde88
      for(a2 = 0; a2 < a3; a2++) {
        final long a0_0 = s7 + a2 * 0xcL;
        MEMORY.ref(2, a0_0).offset(0x8L).setu(MEMORY.ref(2, v1).offset(0x0L).get());
        MEMORY.ref(2, a0_0).offset(0xaL).setu(MEMORY.ref(2, v1).offset(0x2L).get());
        MEMORY.ref(2, a0_0).offset(0xcL).setu(MEMORY.ref(2, v1).offset(0x4L).get());
        MEMORY.ref(2, a0_0).offset(0xeL).setu(MEMORY.ref(2, v1).offset(0x6L).get());
        MEMORY.ref(2, a0_0).offset(0x10L).setu(MEMORY.ref(2, v1).offset(0x8L).get());
        MEMORY.ref(2, a0_0).offset(0x12L).setu(MEMORY.ref(2, v1).offset(0xcL).get());
        v1 = v1 + 0xcL;
      }

      //LAB_800ddee0
      MEMORY.ref(4, s7).offset(0x0L).setu(0);
      t0 = 0;
    }

    //LAB_800ddeec
    s4 = MEMORY.ref(4, s7).offset(0x4L).get() + 0x10L + t1 * 0xcL + t1 * t0 * 0x8L;
    t2 = s7 + 0x8L;

    //LAB_800ddf1c
    for(; t0 < a1_0; t0++) {
      a2 = t2;

      //LAB_800ddf2c
      for(s3 = 0; s3 < t1; s3++) {
        MEMORY.ref(2, a2).offset(0x0L).addu(MEMORY.ref(1, s4).offset(0x1L).getSigned() << MEMORY.ref(1, s4).offset(0x0L).getSigned());
        MEMORY.ref(2, a2).offset(0x2L).addu(MEMORY.ref(1, s4).offset(0x2L).getSigned() << MEMORY.ref(1, s4).offset(0x0L).getSigned());
        MEMORY.ref(2, a2).offset(0x4L).addu(MEMORY.ref(1, s4).offset(0x3L).getSigned() << MEMORY.ref(1, s4).offset(0x0L).getSigned());
        MEMORY.ref(2, a2).offset(0x6L).addu(MEMORY.ref(1, s4).offset(0x5L).getSigned() << MEMORY.ref(1, s4).offset(0x4L).getSigned());
        MEMORY.ref(2, a2).offset(0x8L).addu(MEMORY.ref(1, s4).offset(0x6L).getSigned() << MEMORY.ref(1, s4).offset(0x4L).getSigned());
        MEMORY.ref(2, a2).offset(0xaL).addu(MEMORY.ref(1, s4).offset(0x7L).getSigned() << MEMORY.ref(1, s4).offset(0x4L).getSigned());
        a2 = a2 + 0xcL;
        s4 = s4 + 0x8L;
      }

      //LAB_800ddfd4
    }

    //LAB_800ddfe4
    //LAB_800de158
    s0 = t2;
    if(t3 == 0 || a0.ub_a3.get() != 0 || a1_0 == (a0.s_9a.get() >> 1) - 1) {
      //LAB_800de164
      for(s3 = 0; s3 < fp; s3++) {
        final MATRIX mat = a0.dobj2ArrPtr_00.deref().get((int)s3).coord2_04.deref().coord;
        RotMatrix_80040010(MEMORY.ref(2, s0, SVECTOR::new), mat); //TODO
        mat.transfer.setX((int)MEMORY.ref(2, s0).offset(0x6L).getSigned());
        mat.transfer.setY((int)MEMORY.ref(2, s0).offset(0x8L).getSigned());
        mat.transfer.setZ((int)MEMORY.ref(2, s0).offset(0xaL).getSigned());
        s0 = s0 + 0xcL;
      }
    } else {
      s4 = MEMORY.ref(4, s7).offset(0x4L).get() + 0x10L + t1 * 0xcL + t1 * a1_0 * 0x8L;

      //LAB_800de050
      for(s3 = 0; s3 < fp; s3++) {
        final MATRIX mat = a0.dobj2ArrPtr_00.deref().get((int)s3).coord2_04.deref().coord;
        RotMatrix_80040010(MEMORY.ref(2, s0, SVECTOR::new), mat); //TODO
        mat.transfer.setX((int)MEMORY.ref(2, s0).offset(0x6L).getSigned());
        mat.transfer.setY((int)MEMORY.ref(2, s0).offset(0x8L).getSigned());
        mat.transfer.setZ((int)MEMORY.ref(2, s0).offset(0xaL).getSigned());
        final SVECTOR sp0x10 = new SVECTOR();
        sp0x10.setX((short)(MEMORY.ref(2, s0).offset(0x0L).get() + (MEMORY.ref(1, s4).offset(0x1L).getSigned() << MEMORY.ref(1, s4).offset(0x0L).getSigned())));
        sp0x10.setY((short)(MEMORY.ref(2, s0).offset(0x2L).get() + (MEMORY.ref(1, s4).offset(0x2L).getSigned() << MEMORY.ref(1, s4).offset(0x0L).getSigned())));
        sp0x10.setZ((short)(MEMORY.ref(2, s0).offset(0x4L).get() + (MEMORY.ref(1, s4).offset(0x3L).getSigned() << MEMORY.ref(1, s4).offset(0x0L).getSigned())));
        final MATRIX sp0x18 = new MATRIX();
        RotMatrix_80040010(sp0x10, sp0x18);
        sp0x18.transfer.setX((int)(MEMORY.ref(2, s0).offset(0x6L).getSigned() + (MEMORY.ref(1, s4).offset(0x5L).getSigned() << MEMORY.ref(1, s4).offset(0x4L).getSigned())));
        sp0x18.transfer.setY((int)(MEMORY.ref(2, s0).offset(0x8L).getSigned() + (MEMORY.ref(1, s4).offset(0x6L).getSigned() << MEMORY.ref(1, s4).offset(0x4L).getSigned())));
        sp0x18.transfer.setZ((int)(MEMORY.ref(2, s0).offset(0xaL).getSigned() + (MEMORY.ref(1, s4).offset(0x7L).getSigned() << MEMORY.ref(1, s4).offset(0x4L).getSigned())));
        FUN_800dd15c(mat, sp0x18, 0x800);
        s0 = s0 + 0xcL;
        s4 = s4 + 0x8L;
      }
    }

    //LAB_800de1b4
    if(a0.s_9e.get() == 0) {
      a0.ub_9c.set(0);
    } else {
      //LAB_800de1cc
      a0.ub_9c.set(1);
    }

    //LAB_800de1d0
    MEMORY.ref(4, s7).offset(0x0L).setu(a1);

    //LAB_800de1e0
    return a0.s_9e.get();
  }

  @Method(0x800de210L)
  public static void FUN_800de210(final Model124 model, final long a1) {
    long v1;
    long a2;
    final long a3;
    final UnboundedArrayRef<GsCOORD2PARAM> t0 = model.coord2ParamArrPtr_08.deref();
    t0.get(0).scale.setY((int)a1); //TODO ?? a1 is a pointer
    model.partTransforms_90.setPointer(0x2L); //TODO ??
    model.partTransforms_94.clear();
    model.animCount_98.set((int)MEMORY.ref(2, a1).offset(0xcL).get());
    model.s_9a.set((short)(MEMORY.ref(2, a1).offset(0xeL).get() * 2));
    model.ub_9c.set(1);
    model.s_9e.set((short)(MEMORY.ref(2, a1).offset(0xeL).get() * 2));
    a3 = MEMORY.ref(2, a1).offset(0xcL).get();

    //LAB_800de270
    for(a2 = 0; a2 < a3; a2++) {
      v1 = a1 + 0x10L + a2 * 0xcL;
      final long a1_0 = t0.getAddress() + a2 * 0xcL;
      MEMORY.ref(2, a1_0).offset(0x08L).setu(MEMORY.ref(2, v1).offset(0x0L).get());
      MEMORY.ref(2, a1_0).offset(0x0aL).setu(MEMORY.ref(2, v1).offset(0x2L).get());
      MEMORY.ref(2, a1_0).offset(0x0cL).setu(MEMORY.ref(2, v1).offset(0x4L).get());
      MEMORY.ref(2, a1_0).offset(0x0eL).setu(MEMORY.ref(2, v1).offset(0x6L).get());
      MEMORY.ref(2, a1_0).offset(0x10L).setu(MEMORY.ref(2, v1).offset(0x8L).get());
      MEMORY.ref(2, a1_0).offset(0x12L).setu(MEMORY.ref(2, v1).offset(0xaL).get());
    }

    //LAB_800de2c8
    t0.get(0).scale.setX(1);
    FUN_800ddd3c(model, 0);
  }

  @Method(0x800de2e8L)
  public static void FUN_800de2e8(final Model124 a0, final long a1) {
    final long v1 = a0.partTransforms_90.getPointer(); //TODO ??
    if(v1 == 1) {
      //LAB_800de318
      FUN_800dd638(a0, a1);
    } else if(v1 == 0 || v1 == 2) {
      //LAB_800de328
      FUN_800ddd3c(a0, a1);
    } else {
      //LAB_800de338
      FUN_800dd4cc(a0, a1);
    }

    //LAB_800de340
  }

  @Method(0x800de36cL)
  public static void FUN_800de36c(final Model124 model, final long a1) {
    final long v1 = MEMORY.ref(4, a1).offset(0x0L).get();
    if(v1 == 0x2042_4d43L) { // "CMB "
      FUN_800de210(model, a1);
      //LAB_800de398
    } else if(v1 == 0x42_4d4cL) { // "LMB"
      model.coord2ParamArrPtr_08.deref().get(0).scale.setX((int)a1); //TODO ?? a1 is a pointer
      model.partTransforms_90.setPointer(0x1L); //TODO ??
      model.partTransforms_94.clear();
      model.animCount_98.set((int)MEMORY.ref(2, a1).offset(0x4L).get());
      model.s_9a.set((short)(MEMORY.ref(2, a1).offset(0xcL).getSigned() * 2));
      model.ub_9c.set(1);
      model.s_9e.set((short)(MEMORY.ref(2, a1).offset(0xcL).getSigned() * 2));
    } else {
      //LAB_800de3dc
      FUN_80021584(model, MEMORY.ref(4, a1, TmdAnimationFile::new)); //TODO
    }

    //LAB_800de3e4
  }

  @Method(0x800de3f4L)
  public static void FUN_800de3f4(final TmdObjTable a0, final EffectManagerData6cInner a1, final MATRIX a2) {
    long s0 = struct7cc_800c693c.deref()._20.get() & 0x4L;
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
    FUN_8003f210(matrix_800c3548, a2, sp0x10);
    if((a1._00.get() & 0x400_0000L) == 0) {
      RotMatrix_8003faf0(a1.rot_10, sp0x10);
      ScaleVectorL_SVEC(sp0x10, a1.scale_16);
    }

    //LAB_800de4a8
    if((s0 & (sp0x10.transfer.getZ() ^ _800bb0fc.get())) == 0 || sp0x10.transfer.getZ() - sp0x10.transfer.getX() >= -0x800 && sp0x10.transfer.getZ() + sp0x10.transfer.getX() >= -0x800 && sp0x10.transfer.getZ() - sp0x10.transfer.getY() >= -0x800 && sp0x10.transfer.getZ() + sp0x10.transfer.getY() >= -0x800) {
      //LAB_800de50c
      setRotTransMatrix(sp0x10);

      final Memory.TemporaryReservation tmp = MEMORY.temp(0x10);
      final GsDOBJ2 dobj2 = new GsDOBJ2(tmp.get());
      dobj2.attribute_00.set(a1._00.get());
      dobj2.tmd_08.set(a0);
      renderCtmd(dobj2);
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

  @Method(0x800de618L)
  public static void FUN_800de618(final SVECTOR a0, final SVECTOR a1, final MATRIX a2) {
    final MATRIX sp0x10 = new MATRIX().set(a2);
    a0.setX((short)ratan2(-sp0x10.get(5), sp0x10.get(8)));
    RotMatrixX(-(short)a0.getX(), sp0x10);
    a0.setY((short)ratan2(sp0x10.get(2), sp0x10.get(8)));
    RotMatrixY(-(short)a0.getY(), sp0x10);
    a0.setZ((short)ratan2(sp0x10.get(3), sp0x10.get(0))); //TODO is this a retail bug? Should it be 4?
    RotMatrixZ(-(short)a0.getZ(), sp0x10);
    a1.set(sp0x10.get(0), sp0x10.get(4), sp0x10.get(8));
  }

  @Method(0x800de72cL)
  public static void ScaleVectorL_SVEC(final MATRIX a0, final SVECTOR a1) {
    ScaleMatrixL(a0, new VECTOR().set(a1));
  }

  @Method(0x800de76cL)
  public static TmdObjTable optimisePacketsIfNecessary(final TmdWithId tmd, final int objIndex) {
    if((tmd.tmd.header.flags.get() & 0x2L) == 0) {
      final Memory.TemporaryReservation tmp = MEMORY.temp(0x10);
      final GsDOBJ2 dobj2 = tmp.get().cast(GsDOBJ2::new);
      updateTmdPacketIlen(tmd.tmd.objTable, dobj2, objIndex);
      final TmdObjTable objTable = dobj2.tmd_08.deref();
      tmp.release();
      return objTable;
    }

    //LAB_800de7a0
    //LAB_800de7b4
    return tmd.tmd.objTable.get(objIndex);
  }

  @Method(0x800de840L)
  public static long unpackCtmdData(final long unpackedData, long packedData, final int unpackedSize) {
    final BttlStruct50 a3 = _800c6920.deref();

    //TODO isn't this just getting overwritten below?
    MEMORY.ref(4, unpackedData).setu(a3._10.get((int)a3._04.get()).getAddress());

    //LAB_800de878
    final int unpackedCount = unpackedSize / 2;
    while(a3._0c.get() < unpackedCount) {
      if((a3._08.get() & 0x100L) == 0) {
        a3._08.set(MEMORY.ref(1, packedData).get() | 0xff00L);
        packedData++;
      }

      //LAB_800de89c
      if((a3._08.get() & 0x1L) != 0) {
        a3._10.get((int)a3._00.get()).set((int)(MEMORY.ref(1, packedData).offset(0x1L).get() << 8 | MEMORY.ref(1, packedData).offset(0x0L).get()));
        a3._00.incr().and(0x1fL);
        a3._0c.incr();
        packedData += 2;
      } else {
        //LAB_800de8ec
        long a1 = MEMORY.ref(1, packedData).get();
        final long t2 = (a1 >>> 5) + 1;

        //LAB_800de904
        int i;
        for(i = 0; i < t2; i++) {
          a1 &= 0x1fL;
          a3._10.get((int)a3._00.get()).set(a3._10.get((int)a1).get());
          a3._00.incr().and(0x1fL);
          a1++;
        }

        //LAB_800de940
        a3._0c.add(i);
        packedData++;
      }

      //LAB_800de94c
      a3._08.set(a3._08.get() >> 1);
    }

    //LAB_800de968
    //LAB_800de970
    for(int i = 0; i < unpackedCount; i++) {
      MEMORY.ref(2, unpackedData).offset(i * 0x2L).setu(a3._10.get((int)a3._04.get()).get());
      a3._04.incr().and(0x1fL);
      a3._0c.decr();
    }

    //LAB_800de9b4
    return packedData;
  }

  @Method(0x800de9bcL)
  public static long FUN_800de9bc(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    final long command = ctmdGp0CommandId_1f8003ee.get();

    //LAB_800dea38
    for(int i = 0; i < count; i++) {
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x0aL).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x0eL).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x12L).get());
      CPU.MTC2(vert0.getXY(), 0);
      CPU.MTC2(vert0.getZ(),  1);
      CPU.MTC2(vert1.getXY(), 2);
      CPU.MTC2(vert1.getZ(),  3);
      CPU.MTC2(vert2.getXY(), 4);
      CPU.MTC2(vert2.getZ(),  5);
      CPU.COP2(0x280030L);

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        final int winding = (int)CPU.MFC2(24);
        if(winding > 0 || (command & 0x2) != 0 && winding != 0) {
          //LAB_800deab8
          final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(12));
          final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(13));
          final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));

          final SVECTOR vert3 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x16L).get());
          CPU.MTC2(vert3.getXY(), 0);
          CPU.MTC2(vert3.getZ(),  1);
          CPU.COP2(0x180001L);

          final DVECTOR v3 = new DVECTOR().setXY(CPU.MFC2(14));

          CPU.COP2(0x168002eL);

          final int z = Math.min((int)CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
          if(z >= 0xb) {
            final GpuCommandPoly cmd = new GpuCommandPoly(4)
              .pos(0, v0.getX(), v0.getY())
              .pos(1, v1.getX(), v1.getY())
              .pos(2, v2.getX(), v2.getY())
              .pos(3, v3.getX(), v3.getY());

            //LAB_800deb34
            CPU.MTC2(MEMORY.ref(4, primitives).offset(0x4L).get(), 6);
            final long norm0 = normals + MEMORY.ref(2, primitives).offset(0x08L).get() * 0x8L;
            final long norm1 = normals + MEMORY.ref(2, primitives).offset(0x0cL).get() * 0x8L;
            final long norm2 = normals + MEMORY.ref(2, primitives).offset(0x10L).get() * 0x8L;
            CPU.MTC2(MEMORY.ref(4, norm0).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, norm0).offset(0x4L).get(), 1);
            CPU.MTC2(MEMORY.ref(4, norm1).offset(0x0L).get(), 2);
            CPU.MTC2(MEMORY.ref(4, norm1).offset(0x4L).get(), 3);
            CPU.MTC2(MEMORY.ref(4, norm2).offset(0x0L).get(), 4);
            CPU.MTC2(MEMORY.ref(4, norm2).offset(0x4L).get(), 5);
            CPU.COP2(0x118043fL);

            cmd
              .rgb(0, (int)CPU.MFC2(20))
              .rgb(1, (int)CPU.MFC2(21))
              .rgb(2, (int)CPU.MFC2(22));

            final long norm3 = normals + MEMORY.ref(2, primitives).offset(0x14L).get() * 0x8L;
            CPU.MTC2(MEMORY.ref(4, norm3).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, norm3).offset(0x4L).get(), 1);
            CPU.COP2(0x108041bL);

            cmd.rgb(3, (int)CPU.MFC2(22));

            if((command & 0x2) != 0 ) {
              final int tpage = (int)_1f8003ec.get();
              cmd.translucent(Translucency.of(tpage >>> 5 & 0b11));
            }

            GPU.queueCommand(z, cmd);
          }
        }
      }

      //LAB_800debe8
      primitives += 0x18L;
    }

    //LAB_800debf4
    return primitives;
  }

  @Method(0x800dec14L)
  public static long FUN_800dec14(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    final long command = ctmdGp0CommandId_1f8003ee.get();

    //LAB_800dec68
    CPU.MTC2(0x808080, 6);

    //LAB_800dec9c
    for(int i = 0; i < count; i++) {
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x16L).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1aL).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1eL).get());
      CPU.MTC2(vert0.getXY(), 0);
      CPU.MTC2(vert0.getZ(),  1);
      CPU.MTC2(vert1.getXY(), 2);
      CPU.MTC2(vert1.getZ(),  3);
      CPU.MTC2(vert2.getXY(), 4);
      CPU.MTC2(vert2.getZ(),  5);
      CPU.COP2(0x280030L);

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        final int winding = (int)CPU.MFC2(24);
        if(winding > 0 || (command & 0x2) != 0 && winding != 0) {
          //LAB_800ded3c
          final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(12));
          final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(13));
          final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));

          final SVECTOR vert3 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x22L).get());
          CPU.MTC2(vert3.getXY(), 0);
          CPU.MTC2(vert3.getZ(),  1);
          CPU.COP2(0x180001L);

          final DVECTOR v3 = new DVECTOR().setXY(CPU.MFC2(14));

          CPU.COP2(0x168002eL);

          final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
          if(z >= 0xb) {
            //LAB_800dedc4
            final int clut = (int)MEMORY.ref(2, primitives).offset(0x06L).get();
            final int tpage = (int)MEMORY.ref(2, primitives).offset(0x0aL).get();

            final GpuCommandPoly cmd = new GpuCommandPoly(4)
              .bpp(Bpp.of(tpage >>> 7 & 0b11))
              .clut((clut & 0b111111) * 16, clut >>> 6)
              .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
              .pos(0, v0.getX(), v0.getY())
              .pos(1, v1.getX(), v1.getY())
              .pos(2, v2.getX(), v2.getY())
              .pos(3, v3.getX(), v3.getY())
              .uv(0, (int)MEMORY.ref(1, primitives).offset(0x04L).get(), (int)MEMORY.ref(1, primitives).offset(0x05L).get())
              .uv(1, (int)MEMORY.ref(1, primitives).offset(0x08L).get(), (int)MEMORY.ref(1, primitives).offset(0x09L).get())
              .uv(2, (int)MEMORY.ref(1, primitives).offset(0x0cL).get(), (int)MEMORY.ref(1, primitives).offset(0x0dL).get())
              .uv(3, (int)MEMORY.ref(1, primitives).offset(0x10L).get(), (int)MEMORY.ref(1, primitives).offset(0x11L).get());

            if((command & 0x2) != 0) {
              cmd.translucent(Translucency.of(tpage >>> 5 & 0b11));
            }

            final long norm0 = normals + MEMORY.ref(2, primitives).offset(0x14L).get() * 0x8L;
            final long norm1 = normals + MEMORY.ref(2, primitives).offset(0x18L).get() * 0x8L;
            final long norm2 = normals + MEMORY.ref(2, primitives).offset(0x1cL).get() * 0x8L;
            CPU.MTC2(MEMORY.ref(4, norm0).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, norm0).offset(0x4L).get(), 1);
            CPU.MTC2(MEMORY.ref(4, norm1).offset(0x0L).get(), 2);
            CPU.MTC2(MEMORY.ref(4, norm1).offset(0x4L).get(), 3);
            CPU.MTC2(MEMORY.ref(4, norm2).offset(0x0L).get(), 4);
            CPU.MTC2(MEMORY.ref(4, norm2).offset(0x4L).get(), 5);
            CPU.COP2(0x118043fL);

            cmd
              .rgb(0, (int)CPU.MFC2(20))
              .rgb(1, (int)CPU.MFC2(21))
              .rgb(2, (int)CPU.MFC2(22));

            final long norm3 = normals + MEMORY.ref(2, primitives).offset(0x20L).get() * 0x8L;
            CPU.MTC2(MEMORY.ref(4, norm3).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, norm3).offset(0x4L).get(), 1);
            CPU.COP2(0x108041bL);

            cmd.rgb(3, (int)CPU.MFC2(22));

            GPU.queueCommand(z, cmd);
          }
        }
      }

      //LAB_800dee68
      primitives += 0x24L;
    }

    //LAB_800dee74
    return primitives;
  }

  @Method(0x800dee8cL)
  public static long FUN_800dee8c(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    final long command = ctmdGp0CommandId_1f8003ee.get();

    //LAB_800def08
    for(int i = 0; i < count; i++) {
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x16L).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1aL).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1eL).get());
      CPU.MTC2(vert0.getXY(), 0);
      CPU.MTC2(vert0.getZ(),  1);
      CPU.MTC2(vert1.getXY(), 2);
      CPU.MTC2(vert1.getZ(),  3);
      CPU.MTC2(vert2.getXY(), 4);
      CPU.MTC2(vert2.getZ(),  5);
      CPU.COP2(0x280030L);

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        final int winding = (int)CPU.MFC2(24);
        if(winding > 0 || (command & 0x2) != 0 && winding != 0) {
          //LAB_800def88
          final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(12));
          final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(13));
          final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));

          final SVECTOR vert3 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x22L).get());
          CPU.MTC2(vert3.getXY(), 0);
          CPU.MTC2(vert3.getZ(),  1);
          CPU.COP2(0x180001L);

          final DVECTOR v3 = new DVECTOR().setXY(CPU.MFC2(14));

          CPU.COP2(0x168002eL);

          final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
          if(z >= 0xbL) {
            //LAB_800df004
            final GpuCommandPoly cmd = new GpuCommandPoly(4)
              .pos(0, v0.getX(), v0.getY())
              .pos(1, v1.getX(), v1.getY())
              .pos(2, v2.getX(), v2.getY())
              .pos(3, v3.getX(), v3.getY());

            CPU.MTC2(MEMORY.ref(4, primitives).offset(0x4L).get(), 6);
            final long norm0 = normals + MEMORY.ref(2, primitives).offset(0x14L).get() * 0x8L;
            CPU.MTC2(MEMORY.ref(4, norm0).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, norm0).offset(0x4L).get(), 1);
            CPU.COP2(0x108041bL);

            cmd.rgb(0, (int)CPU.MFC2(22));

            CPU.MTC2(MEMORY.ref(4, primitives).offset(0x8L).get(), 6);
            final long norm1 = normals + MEMORY.ref(2, primitives).offset(0x18L).get() * 0x8L;
            CPU.MTC2(MEMORY.ref(4, norm1).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, norm1).offset(0x4L).get(), 1);
            CPU.COP2(0x108041bL);

            cmd.rgb(1, (int)CPU.MFC2(22));

            CPU.MTC2(MEMORY.ref(4, primitives).offset(0xcL).get(), 6);
            final long norm2 = normals + MEMORY.ref(2, primitives).offset(0x1cL).get() * 0x8L;
            CPU.MTC2(MEMORY.ref(4, norm2).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, norm2).offset(0x4L).get(), 1);
            CPU.COP2(0x108041bL);

            cmd.rgb(2, (int)CPU.MFC2(22));

            CPU.MTC2(MEMORY.ref(4, primitives).offset(0x10L).get(), 6);
            final long norm3 = normals + MEMORY.ref(2, primitives).offset(0x20L).get() * 0x8L;
            CPU.MTC2(MEMORY.ref(4, norm3).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, norm3).offset(0x4L).get(), 1);
            CPU.COP2(0x108041bL);

            cmd.rgb(3, (int)CPU.MFC2(22));

            final int tpage = (int)_1f8003ec.get();

            if((command & 0x2) != 0) {
              cmd.translucent(Translucency.of(tpage >>> 5 & 0b11));
            }

            GPU.queueCommand(z, cmd);
          }
        }
      }

      //LAB_800df104
      primitives += 0x24L;
    }

    //LAB_800df110
    return primitives;
  }

  @Method(0x800df130L)
  public static long FUN_800df130(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    final long command = ctmdGp0CommandId_1f8003ee.get();

    //LAB_800df1ac
    for(int i = 0; i < count; i++) {
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x12L).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x16L).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1aL).get());
      CPU.MTC2(vert0.getXY(), 0);
      CPU.MTC2(vert0.getZ(),  1);
      CPU.MTC2(vert1.getXY(), 2);
      CPU.MTC2(vert1.getZ(),  3);
      CPU.MTC2(vert2.getXY(), 4);
      CPU.MTC2(vert2.getZ(),  5);
      CPU.COP2(0x280030L);

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        final int winding = (int)CPU.MFC2(24);
        if(winding > 0 || (command & 0x2) != 0 && winding != 0) {
          //LAB_800df22c
          final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(12));
          final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(13));
          final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));

          CPU.COP2(0x158002dL);

          final int z = Math.min((int)CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
          if(z >= 0xb) {
            //LAB_800df278
            final GpuCommandPoly cmd = new GpuCommandPoly(3)
              .pos(0, v0.getX(), v0.getY())
              .pos(1, v1.getX(), v1.getY())
              .pos(2, v2.getX(), v2.getY());

            CPU.MTC2(MEMORY.ref(4, primitives).offset(0x4L).get(), 6);
            final long norm0 = normals + MEMORY.ref(2, primitives).offset(0x10L).get() * 0x8L;
            CPU.MTC2(MEMORY.ref(4, norm0).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, norm0).offset(0x4L).get(), 1);
            CPU.COP2(0x108041bL);
            cmd.rgb(0, (int)CPU.MFC2(22));

            CPU.MTC2(MEMORY.ref(4, primitives).offset(0x8L).get(), 6);
            final long norm1 = normals + MEMORY.ref(2, primitives).offset(0x14L).get() * 0x8L;
            CPU.MTC2(MEMORY.ref(4, norm1).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, norm1).offset(0x4L).get(), 1);
            CPU.COP2(0x108041bL);
            cmd.rgb(1, (int)CPU.MFC2(22));

            CPU.MTC2(MEMORY.ref(4, primitives).offset(0xcL).get(), 6);
            final long norm2 = normals + MEMORY.ref(2, primitives).offset(0x18L).get() * 0x8L;
            CPU.MTC2(MEMORY.ref(4, norm2).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, norm2).offset(0x4L).get(), 1);
            CPU.COP2(0x108041bL);
            cmd.rgb(2, (int)CPU.MFC2(22));

            final int tpage = (int)_1f8003ec.get();

            if((command & 0x2) != 0) {
              cmd.translucent(Translucency.of(tpage >> 5 & 0b11));
            }

            GPU.queueCommand(z, cmd);
          }
        }
      }

      //LAB_800df344
      primitives += 0x1cL;
    }

    //LAB_800df350
    return primitives;
  }

  @Method(0x800df370L)
  public static long FUN_800df370(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    final long command = ctmdGp0CommandId_1f8003ee.get();

    final IntRef refR = new IntRef();
    final IntRef refG = new IntRef();
    final IntRef refB = new IntRef();
    getLightColour(refR, refG, refB);
    final int r = refR.get();
    final int g = refG.get();
    final int b = refB.get();

    //LAB_800df404
    for(int i = 0; i < count; i++) {
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x24L).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x26L).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x28L).get());
      CPU.MTC2(vert0.getXY(), 0);
      CPU.MTC2(vert0.getZ(),  1);
      CPU.MTC2(vert1.getXY(), 2);
      CPU.MTC2(vert1.getZ(),  3);
      CPU.MTC2(vert2.getXY(), 4);
      CPU.MTC2(vert2.getZ(),  5);
      CPU.COP2(0x280030L);

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        final int winding = (int)CPU.MFC2(24);
        if(winding > 0 || (command & 0x2L) != 0 && winding != 0) {
          //LAB_800df4ac
          final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(12));
          final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(13));
          final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));

          final SVECTOR vert3 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x2aL).get());
          CPU.MTC2(vert3.getXY(), 0);
          CPU.MTC2(vert3.getZ(),  1);
          CPU.COP2(0x180001L);
          final DVECTOR v3 = new DVECTOR().setXY(CPU.MFC2(14));

          CPU.COP2(0x168002eL);

          final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
          if(z >= 11) {
            //LAB_800df684
            final int clut = (int)MEMORY.ref(2, primitives).offset(0x6L).get();
            final int tpage = (int)MEMORY.ref(2, primitives).offset(0xaL).get();

            final GpuCommandPoly cmd = new GpuCommandPoly(4)
              .bpp(Bpp.of(tpage >>> 7 & 0b11))
              .clut((clut & 0b111111) * 16, clut >>> 6)
              .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
              .rgb(0, (int)MEMORY.ref(1, primitives).offset(0x14L).get() * r >> 12, (int)MEMORY.ref(1, primitives).offset(0x15L).get() * g >> 12, (int)MEMORY.ref(1, primitives).offset(0x16L).get() * b >> 12)
              .rgb(1, (int)MEMORY.ref(1, primitives).offset(0x18L).get() * r >> 12, (int)MEMORY.ref(1, primitives).offset(0x19L).get() * g >> 12, (int)MEMORY.ref(1, primitives).offset(0x1aL).get() * b >> 12)
              .rgb(2, (int)MEMORY.ref(1, primitives).offset(0x1cL).get() * r >> 12, (int)MEMORY.ref(1, primitives).offset(0x1dL).get() * g >> 12, (int)MEMORY.ref(1, primitives).offset(0x1eL).get() * b >> 12)
              .rgb(3, (int)MEMORY.ref(1, primitives).offset(0x20L).get() * r >> 12, (int)MEMORY.ref(1, primitives).offset(0x21L).get() * g >> 12, (int)MEMORY.ref(1, primitives).offset(0x22L).get() * b >> 12)
              .pos(0, v0.getX(), v0.getY())
              .pos(1, v1.getX(), v1.getY())
              .pos(2, v2.getX(), v2.getY())
              .pos(3, v3.getX(), v3.getY())
              .uv(0, (int)MEMORY.ref(1, primitives).offset(0x4L).get(), (int)MEMORY.ref(1, primitives).offset(0x5L).get())
              .uv(1, (int)MEMORY.ref(1, primitives).offset(0x8L).get(), (int)MEMORY.ref(1, primitives).offset(0x9L).get())
              .uv(2, (int)MEMORY.ref(1, primitives).offset(0xcL).get(), (int)MEMORY.ref(1, primitives).offset(0xdL).get())
              .uv(3, (int)MEMORY.ref(1, primitives).offset(0x10L).get(), (int)MEMORY.ref(1, primitives).offset(0x11L).get());

            if((command & 0x2) != 0) {
              cmd.translucent(Translucency.of(tpage >>> 5 & 0b11));
            }

            GPU.queueCommand(z, cmd);
          }
        }
      }

      //LAB_800df6ac
      primitives += 0x2cL;
    }

    //LAB_800df6bc
    return primitives;
  }

  @Method(0x800df6f0L)
  public static long FUN_800df6f0(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    final long command = ctmdGp0CommandId_1f8003ee.get();

    final IntRef refR = new IntRef();
    final IntRef refG = new IntRef();
    final IntRef refB = new IntRef();
    getLightColour(refR, refG, refB);
    final int r = refR.get();
    final int g = refG.get();
    final int b = refB.get();

    //LAB_800df784
    for(int i = 0; i < count; i++) {
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1cL).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1eL).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x20L).get());
      CPU.MTC2(vert0.getXY(), 0);
      CPU.MTC2(vert0.getZ(),  1);
      CPU.MTC2(vert1.getXY(), 2);
      CPU.MTC2(vert1.getZ(),  3);
      CPU.MTC2(vert2.getXY(), 4);
      CPU.MTC2(vert2.getZ(),  5);
      CPU.COP2(0x280030L);

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        final int winding = (int)CPU.MFC2(24);
        if(winding > 0 || (command & 0x2) != 0 && winding != 0) {
          //LAB_800df82c
          final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(12));
          final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(13));
          final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));

          CPU.COP2(0x158002dL);

          final int z = Math.min((int)CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
          if(z >= 0xb) {
            //LAB_800df97c
            final int clut = (int)MEMORY.ref(2, primitives).offset(0x6L).get();
            final int tpage = (int)MEMORY.ref(2, primitives).offset(0xaL).get();

            final GpuCommandPoly cmd = new GpuCommandPoly(3)
              .bpp(Bpp.of(tpage >>> 7 & 0b11))
              .clut((clut & 0b111111) * 16, clut >>> 6)
              .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
              .rgb(0, (int)MEMORY.ref(1, primitives).offset(0x10L).get() * r >> 12, (int)MEMORY.ref(1, primitives).offset(0x11L).get() * g >> 12, (int)MEMORY.ref(1, primitives).offset(0x12L).get() * b >> 12)
              .rgb(1, (int)MEMORY.ref(1, primitives).offset(0x14L).get() * r >> 12, (int)MEMORY.ref(1, primitives).offset(0x15L).get() * g >> 12, (int)MEMORY.ref(1, primitives).offset(0x16L).get() * b >> 12)
              .rgb(2, (int)MEMORY.ref(1, primitives).offset(0x18L).get() * r >> 12, (int)MEMORY.ref(1, primitives).offset(0x19L).get() * g >> 12, (int)MEMORY.ref(1, primitives).offset(0x1aL).get() * b >> 12)
              .pos(0, v0.getX(), v0.getY())
              .pos(1, v1.getX(), v1.getY())
              .pos(2, v2.getX(), v2.getY())
              .uv(0, (int)MEMORY.ref(1, primitives).offset(0x4L).get(), (int)MEMORY.ref(1, primitives).offset(0x5L).get())
              .uv(1, (int)MEMORY.ref(1, primitives).offset(0x8L).get(), (int)MEMORY.ref(1, primitives).offset(0x9L).get())
              .uv(2, (int)MEMORY.ref(1, primitives).offset(0xcL).get(), (int)MEMORY.ref(1, primitives).offset(0xdL).get());

            GPU.queueCommand(z, cmd);
          }
        }
      }

      //LAB_800df9a4
      primitives += 0x24L;
    }

    //LAB_800df9b4
    return primitives;
  }

  @Method(0x800df9e8L)
  public static long FUN_800df9e8(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    CPU.MTC2(0x808080, 6);

    //LAB_800dfa68
    for(int i = 0; i < count; i++) {
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x16L).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1aL).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1eL).get());
      CPU.MTC2(vert0.getXY(), 0);
      CPU.MTC2(vert0.getZ(),  1);
      CPU.MTC2(vert1.getXY(), 2);
      CPU.MTC2(vert1.getZ(),  3);
      CPU.MTC2(vert2.getXY(), 4);
      CPU.MTC2(vert2.getZ(),  5);
      CPU.COP2(0x280030L);

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        if(CPU.MFC2(24) != 0) {
          final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(12));
          final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(13));
          final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));

          final SVECTOR vert3 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x22L).get());
          CPU.MTC2(vert3.getXY(), 0);
          CPU.MTC2(vert3.getZ(),  1);
          CPU.COP2(0x180001L);

          if((int)CPU.CFC2(31) >= 0) {
            final DVECTOR v3 = new DVECTOR().setXY(CPU.MFC2(14));

            CPU.COP2(0x168002eL);

            final int z = Math.min((int)CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
            if(z >= 0xb) {
              //LAB_800dfb94
              final int clut = (int)MEMORY.ref(2, primitives).offset(0x6L).get();
              final int tpage = (int)MEMORY.ref(2, primitives).offset(0xaL).get() & 0xff9f | (int)_1f8003ec.getSigned();

              final GpuCommandPoly cmd = new GpuCommandPoly(4)
                .bpp(Bpp.of(tpage >>> 7 & 0b11))
                .clut((clut & 0b111111) * 16, clut >>> 6)
                .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
                .pos(0, v0.getX(), v0.getY())
                .pos(1, v1.getX(), v1.getY())
                .pos(2, v2.getX(), v2.getY())
                .pos(3, v3.getX(), v3.getY())
                .uv(0, (int)MEMORY.ref(1, primitives).offset(0x04L).get(), (int)MEMORY.ref(1, primitives).offset(0x05L).get())
                .uv(1, (int)MEMORY.ref(1, primitives).offset(0x08L).get(), (int)MEMORY.ref(1, primitives).offset(0x09L).get())
                .uv(2, (int)MEMORY.ref(1, primitives).offset(0x0cL).get(), (int)MEMORY.ref(1, primitives).offset(0x0dL).get())
                .uv(3, (int)MEMORY.ref(1, primitives).offset(0x10L).get(), (int)MEMORY.ref(1, primitives).offset(0x11L).get());

              final long norm0 = normals + MEMORY.ref(2, primitives).offset(0x14L).get() * 0x8L;
              final long norm1 = normals + MEMORY.ref(2, primitives).offset(0x18L).get() * 0x8L;
              final long norm2 = normals + MEMORY.ref(2, primitives).offset(0x1cL).get() * 0x8L;
              CPU.MTC2(MEMORY.ref(4, norm0).offset(0x0L).get(), 0);
              CPU.MTC2(MEMORY.ref(4, norm0).offset(0x4L).get(), 1);
              CPU.MTC2(MEMORY.ref(4, norm1).offset(0x0L).get(), 2);
              CPU.MTC2(MEMORY.ref(4, norm1).offset(0x4L).get(), 3);
              CPU.MTC2(MEMORY.ref(4, norm2).offset(0x0L).get(), 4);
              CPU.MTC2(MEMORY.ref(4, norm2).offset(0x4L).get(), 5);
              CPU.COP2(0x118043fL);

              cmd
                .rgb(0, (int)CPU.MFC2(20))
                .rgb(1, (int)CPU.MFC2(21))
                .rgb(2, (int)CPU.MFC2(22));

              final long norm3 = normals + MEMORY.ref(2, primitives).offset(0x20L).get() * 0x8L;
              CPU.MTC2(MEMORY.ref(4, norm3).offset(0x0L).get(), 0);
              CPU.MTC2(MEMORY.ref(4, norm3).offset(0x4L).get(), 1);
              CPU.COP2(0x108041bL);

              cmd.rgb(3, (int)CPU.MFC2(22));

              GPU.queueCommand(z, cmd);
            }
          }
        }
      }

      //LAB_800dfc38
      primitives += 0x24L;
    }

    //LAB_800dfc44
    return primitives;
  }

  @Method(0x800dfc5cL)
  public static long FUN_800dfc5c(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    final IntRef refR = new IntRef();
    final IntRef refG = new IntRef();
    final IntRef refB = new IntRef();
    getLightColour(refR, refG, refB);
    final int r = refR.get();
    final int g = refG.get();
    final int b = refB.get();

    //LAB_800dfce8
    for(int i = 0; i < count; i++) {
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x24L).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x26L).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x28L).get());
      CPU.MTC2(vert0.getXY(), 0);
      CPU.MTC2(vert0.getZ(),  1);
      CPU.MTC2(vert1.getXY(), 2);
      CPU.MTC2(vert1.getZ(),  3);
      CPU.MTC2(vert2.getXY(), 4);
      CPU.MTC2(vert2.getZ(),  5);
      CPU.COP2(0x280030L);

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        if(CPU.MFC2(24) != 0) {
          final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(12));
          final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(13));
          final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));
          final SVECTOR vert3 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x2aL).get());
          CPU.MTC2(vert3.getXY(), 0);
          CPU.MTC2(vert3.getZ(),  1);
          CPU.COP2(0x180001L);

          if((int)CPU.CFC2(31) >= 0) {
            final DVECTOR v3 = new DVECTOR().setXY(CPU.MFC2(14));
            CPU.COP2(0x168002eL);

            final int z = Math.min((int)CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
            if(z >= 0xb) {
              final int clut = (int)MEMORY.ref(2, primitives).offset(0x06L).get();
              final int tpage = (int)MEMORY.ref(2, primitives).offset(0x0aL).get() & 0xff9f | (int)_1f8003ec.get();

              final GpuCommandPoly cmd = new GpuCommandPoly(4)
                .bpp(Bpp.of(tpage >>> 7 & 0b11))
                .translucent(Translucency.of(tpage >>> 5 & 0b11))
                .clut((clut & 0b111111) * 16, clut >>> 6)
                .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
                .pos(0, v0.getX(), v0.getY())
                .pos(1, v1.getX(), v1.getY())
                .pos(2, v2.getX(), v2.getY())
                .pos(3, v3.getX(), v3.getY())
                .uv(0, (int)MEMORY.ref(1, primitives).offset(0x04L).get(), (int)MEMORY.ref(1, primitives).offset(0x05L).get())
                .uv(1, (int)MEMORY.ref(1, primitives).offset(0x08L).get(), (int)MEMORY.ref(1, primitives).offset(0x09L).get())
                .uv(2, (int)MEMORY.ref(1, primitives).offset(0x0cL).get(), (int)MEMORY.ref(1, primitives).offset(0x0dL).get())
                .uv(3, (int)MEMORY.ref(1, primitives).offset(0x10L).get(), (int)MEMORY.ref(1, primitives).offset(0x11L).get())
                .rgb(0, (int)MEMORY.ref(1, primitives).offset(0x14L).get() * r >> 12, (int)MEMORY.ref(1, primitives).offset(0x15L).get() * g >> 12, (int)MEMORY.ref(1, primitives).offset(0x16L).get() * b >> 12)
                .rgb(1, (int)MEMORY.ref(1, primitives).offset(0x18L).get() * r >> 12, (int)MEMORY.ref(1, primitives).offset(0x19L).get() * g >> 12, (int)MEMORY.ref(1, primitives).offset(0x1aL).get() * b >> 12)
                .rgb(2, (int)MEMORY.ref(1, primitives).offset(0x1cL).get() * r >> 12, (int)MEMORY.ref(1, primitives).offset(0x1dL).get() * g >> 12, (int)MEMORY.ref(1, primitives).offset(0x1eL).get() * b >> 12)
                .rgb(3, (int)MEMORY.ref(1, primitives).offset(0x20L).get() * r >> 12, (int)MEMORY.ref(1, primitives).offset(0x21L).get() * g >> 12, (int)MEMORY.ref(1, primitives).offset(0x22L).get() * b >> 12);

              //LAB_800dff7c
              GPU.queueCommand(z, cmd);
            }
          }
        }
      }

      //LAB_800dffa4
      primitives += 0x2cL;
    }

    //LAB_800dffb0
    return primitives;
  }

  @Method(0x800dffe4L)
  public static long FUN_800dffe4(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    final IntRef refR = new IntRef();
    final IntRef refG = new IntRef();
    final IntRef refB = new IntRef();
    getLightColour(refR, refG, refB);
    final int r = refR.get();
    final int g = refG.get();
    final int b = refB.get();

    //LAB_800e0070
    for(int i = 0; i < count; i++) {
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1cL).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x1eL).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x20L).get());
      CPU.MTC2(vert0.getXY(), 0);
      CPU.MTC2(vert0.getZ(),  1);
      CPU.MTC2(vert1.getXY(), 2);
      CPU.MTC2(vert1.getZ(),  3);
      CPU.MTC2(vert2.getXY(), 4);
      CPU.MTC2(vert2.getZ(),  5);
      CPU.COP2(0x280030L);

      if((int)CPU.CFC2(31) >= 0) {
        CPU.COP2(0x1400006L);

        if(CPU.MFC2(24) != 0) {
          final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(12));
          final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(13));
          final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));

          if((int)CPU.CFC2(31) >= 0) {
            CPU.COP2(0x158002dL);

            final int z = Math.min((int)CPU.MFC2(7) + zOffset_1f8003e8.get() >> 2, 0xffe);
            if(z >= 0xb) {
              final int clut = (int)MEMORY.ref(2, primitives).offset(0x6L).get();
              final int tpage = (int)MEMORY.ref(2, primitives).offset(0xaL).get() & 0xff9f | (int)_1f8003ec.get();

              final GpuCommandPoly cmd = new GpuCommandPoly(3)
                .bpp(Bpp.of(tpage >>> 7 & 0b11))
                .translucent(Translucency.of(tpage >>> 5 & 0b11))
                .clut((clut & 0b111111) * 16, clut >>> 6)
                .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
                .pos(0, v0.getX(), v0.getY())
                .pos(1, v1.getX(), v1.getY())
                .pos(2, v2.getX(), v2.getY())
                .uv(0, (int)MEMORY.ref(1, primitives).offset(0x4L).get(), (int)MEMORY.ref(1, primitives).offset(0x5L).get())
                .uv(1, (int)MEMORY.ref(1, primitives).offset(0x8L).get(), (int)MEMORY.ref(1, primitives).offset(0x9L).get())
                .uv(2, (int)MEMORY.ref(1, primitives).offset(0xcL).get(), (int)MEMORY.ref(1, primitives).offset(0xdL).get())
                .rgb(0, (int)MEMORY.ref(1, primitives).offset(0x10L).get() * r >> 12, (int)MEMORY.ref(1, primitives).offset(0x11L).get() * g >> 12, (int)MEMORY.ref(1, primitives).offset(0x12L).get() * b >> 12)
                .rgb(1, (int)MEMORY.ref(1, primitives).offset(0x14L).get() * r >> 12, (int)MEMORY.ref(1, primitives).offset(0x15L).get() * g >> 12, (int)MEMORY.ref(1, primitives).offset(0x16L).get() * b >> 12)
                .rgb(2, (int)MEMORY.ref(1, primitives).offset(0x18L).get() * r >> 12, (int)MEMORY.ref(1, primitives).offset(0x19L).get() * g >> 12, (int)MEMORY.ref(1, primitives).offset(0x1aL).get() * b >> 12);

              //LAB_800e0280
              GPU.queueCommand(z, cmd);
            }
          }
        }
      }

      //LAB_800e02a8
      primitives += 0x24L;
    }

    //LAB_800e02b4
    return primitives;
  }
}
