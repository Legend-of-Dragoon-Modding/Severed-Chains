package legend.game.combat;

import legend.core.MathHelper;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandLine;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gte.COLOUR;
import legend.core.gte.DVECTOR;
import legend.core.gte.GsCOORD2PARAM;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.Tmd;
import legend.core.gte.TmdObjTable;
import legend.core.gte.TmdWithId;
import legend.core.gte.VECTOR;
import legend.core.memory.Method;
import legend.core.memory.types.CString;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.game.combat.deff.Anim;
import legend.game.combat.deff.Cmb;
import legend.game.combat.deff.Lmb;
import legend.game.combat.deff.LmbTransforms14;
import legend.game.combat.deff.LmbType0;
import legend.game.combat.types.AdditionCharEffectData0c;
import legend.game.combat.types.AdditionScriptData1c;
import legend.game.combat.types.AdditionSparksEffect08;
import legend.game.combat.types.AdditionStarburstEffect10;
import legend.game.combat.types.BattleCamera;
import legend.game.combat.types.BattleObject27c;
import legend.game.combat.types.BattleScriptDataBase;
import legend.game.combat.types.BttlScriptData40;
import legend.game.combat.types.BttlScriptData6cSub13c;
import legend.game.combat.types.CtmdUnpackingData50;
import legend.game.combat.types.EffectManagerData6c;
import legend.game.combat.types.EffectManagerData6cInner;
import legend.game.combat.types.GuardEffect06;
import legend.game.combat.types.MonsterDeathEffect34;
import legend.game.combat.types.PotionEffect14;
import legend.game.combat.types.ProjectileHitEffect14;
import legend.game.combat.types.ProjectileHitEffect14Sub48;
import legend.game.combat.types.SpriteMetrics08;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptState;
import legend.game.types.ExtendedTmd;
import legend.game.types.Model124;
import legend.game.types.ModelPartTransforms;
import legend.game.types.TmdAnimationFile;
import legend.game.types.Translucency;

import java.util.Arrays;

import static legend.core.GameEngine.CPU;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.MEMORY;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.Scus94491BpeSegment.FUN_80018a5c;
import static legend.game.Scus94491BpeSegment.FUN_80018d60;
import static legend.game.Scus94491BpeSegment.free;
import static legend.game.Scus94491BpeSegment.mallocTail;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment.tmdGp0CommandId_1f8003ee;
import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment_8002.FUN_800213c4;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021628;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021724;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022018;
import static legend.game.Scus94491BpeSegment_8002.SetGeomOffset;
import static legend.game.Scus94491BpeSegment_8002.SetRotMatrix;
import static legend.game.Scus94491BpeSegment_8002.SetTransMatrix;
import static legend.game.Scus94491BpeSegment_8002.SquareRoot0;
import static legend.game.Scus94491BpeSegment_8002.applyModelPartTransforms;
import static legend.game.Scus94491BpeSegment_8002.initObjTable2;
import static legend.game.Scus94491BpeSegment_8002.loadModelStandardAnimation;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003eba0;
import static legend.game.Scus94491BpeSegment_8003.GsGetLws;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_8003.GsSetRefView2;
import static legend.game.Scus94491BpeSegment_8003.MulMatrix0;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_8003faf0;
import static legend.game.Scus94491BpeSegment_8003.RotTrans;
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
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;
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
import static legend.game.combat.Bttl_800c.ctmdUnpackingData_800c6920;
import static legend.game.combat.Bttl_800c.currentAddition_800c6790;
import static legend.game.combat.Bttl_800c.deffManager_800c693c;
import static legend.game.combat.Bttl_800c.effectRenderers_800fa758;
import static legend.game.combat.Bttl_800c.screenOffsetX_800c67bc;
import static legend.game.combat.Bttl_800c.screenOffsetY_800c67c0;
import static legend.game.combat.Bttl_800c.scriptGetScriptedObjectPos;
import static legend.game.combat.Bttl_800c.seed_800fa754;
import static legend.game.combat.Bttl_800c.spriteMetrics_800c6948;
import static legend.game.combat.Bttl_800e.FUN_800e7ea4;
import static legend.game.combat.Bttl_800e.allocateEffectManager;
import static legend.game.combat.Bttl_800e.getLightColour;
import static legend.game.combat.Bttl_800e.renderCtmd;

public final class Bttl_800d {
  private Bttl_800d() { }

  @Method(0x800d0094L)
  public static void FUN_800d0094(final int scriptIndex, final int animIndex, final boolean clearBit) {
    final BattleObject27c v1 = (BattleObject27c)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00;

    //LAB_800d00d4
    if(clearBit) {
      v1.model_148.ui_f4 &= ~(0x1L << animIndex);
    } else {
      //LAB_800d0104
      v1.model_148.ui_f4 |= 0x1L << animIndex;
    }
  }

  @Method(0x800d0124L)
  public static FlowControl FUN_800d0124(final RunningScript<?> script) {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0[script.params_20[0].get()];
    final BattleScriptDataBase data = (BattleScriptDataBase)state.innerStruct_00;

    if(BattleScriptDataBase.EM__.equals(data.magic_00)) {
      script.params_20[1].set(((BttlScriptData6cSub13c)((EffectManagerData6c)data).effect_44).model_10.animCount_98);
    } else {
      //LAB_800d017c
      script.params_20[1].set(((BattleObject27c)data).model_148.animCount_98);
    }

    //LAB_800d0194
    return FlowControl.CONTINUE;
  }

  @Method(0x800d019cL)
  public static void renderProjectileHitEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    int a0 = 0;
    final ProjectileHitEffect14 effect = (ProjectileHitEffect14)data.effect_44;

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
          int a2_0 = data._10.z_22;
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
  public static void deallocateProjectileHitEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    free(((ProjectileHitEffect14)manager.effect_44)._08.getPointer());
  }

  @Method(0x800d0564L)
  public static FlowControl allocateProjectileHitEffect(final RunningScript<? extends BattleScriptDataBase> script) {
    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0x14,
      null,
      Bttl_800d::renderProjectileHitEffect,
      Bttl_800d::deallocateProjectileHitEffect,
      ProjectileHitEffect14::new
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    final ProjectileHitEffect14 effect = (ProjectileHitEffect14)manager.effect_44;

    final int count = script.params_20[1].get();
    effect.count_00.set(count);
    effect._04.set(0);
    effect._08.setPointer(mallocTail(count * 0x48));

    //LAB_800d0634
    for(int i = 0; i < count; i++) {
      final ProjectileHitEffect14Sub48 struct = effect._08.deref().get(i);

      struct.used_00.set(true);
      struct.r_34.set(script.params_20[2].get() << 8);
      struct.g_36.set(script.params_20[3].get() << 8);
      struct.b_38.set(script.params_20[4].get() << 8);

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
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x800d09b8L)
  public static FlowControl FUN_800d09b8(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
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
  public static void renderAdditionSparks(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    int s7 = 0;
    long s3 = 0;
    final AdditionSparksEffect08 s6 = (AdditionSparksEffect08)data.effect_44;
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
        int a3 = data._10.z_22;
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
  public static void deallocateAdditionSparksEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    free(((AdditionSparksEffect08)data.effect_44)._04.get());
  }

  @Method(0x800d0decL)
  public static FlowControl allocateAdditionSparksEffect(final RunningScript<? extends BattleScriptDataBase> script) {
    final int count = script.params_20[1].get();
    final int s4 = script.params_20[6].get();

    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0x8,
      null,
      Bttl_800d::renderAdditionSparks,
      Bttl_800d::deallocateAdditionSparksEffect,
      AdditionSparksEffect08::new
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    final AdditionSparksEffect08 effect = (AdditionSparksEffect08)manager.effect_44;

    long t6 = mallocTail(count * 0x4cL);
    effect._04.set(t6);
    effect.count_00.set(count);

    final long s1 = script.params_20[5].get() / s4;

    //LAB_800d0ee0
    for(int i = 0; i < count; i++) {
      MEMORY.ref(4, t6).offset(0x0L).setu(0);

      MEMORY.ref(1, t6).offset(0x4L).setu(seed_800fa754.advance().get() % (s4 + 0x1L));
      MEMORY.ref(1, t6).offset(0x5L).setu(seed_800fa754.advance().get() % 9 + 7);

      MEMORY.ref(2, t6).offset(0x40L).setu(script.params_20[2].get() << 8);
      MEMORY.ref(2, t6).offset(0x46L).setu(MEMORY.ref(2, t6).offset(0x40L).get() / MEMORY.ref(1, t6).offset(0x5L).get());
      MEMORY.ref(2, t6).offset(0x42L).setu(script.params_20[3].get() << 8);
      MEMORY.ref(2, t6).offset(0x48L).setu(MEMORY.ref(2, t6).offset(0x44L).get() / MEMORY.ref(1, t6).offset(0x5L).get());
      MEMORY.ref(2, t6).offset(0x44L).setu(script.params_20[4].get() << 8);
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
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
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
  public static void renderAdditionHitStarburst(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final int[] sp0x48 = {-16, 16};
    final AdditionStarburstEffect10 s7 = (AdditionStarburstEffect10)data.effect_44;
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
  public static void renderAdditionCompletedStarburst(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final AdditionStarburstEffect10 sp80 = (AdditionStarburstEffect10)data.effect_44;
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
  public static void deallocateAdditionStarburstEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    free(((AdditionStarburstEffect10)data.effect_44)._0c.get());
  }

  @Method(0x800d19ecL)
  public static FlowControl allocateAdditionStarburstEffect(final RunningScript<? extends BattleScriptDataBase> script) {
    final int count = script.params_20[2].get();

    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0x10,
      null,
      additionStarburstRenderers_800c6dc4[script.params_20[3].get()],
      Bttl_800d::deallocateAdditionStarburstEffect,
      AdditionStarburstEffect10::new
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    final AdditionStarburstEffect10 effect = (AdditionStarburstEffect10)manager.effect_44;
    long t4 = mallocTail(count * 0x10L);
    effect.scriptIndex_00.set(script.params_20[1].get());
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
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x800d1cacL)
  public static FlowControl FUN_800d1cac(final RunningScript<? extends BattleScriptDataBase> script) {
    script.params_20[0].set(allocateEffectManager(script.scriptState_04, 0, null, null, null, null).index);
    return FlowControl.CONTINUE;
  }

  @Method(0x800d1cf4L)
  public static FlowControl FUN_800d1cf4(final RunningScript<? extends BattleScriptDataBase> script) {
    script.params_20[0].set(allocateEffectManager(script.scriptState_04, 0, null, null, null, null).index);
    return FlowControl.CONTINUE;
  }

  @Method(0x800d1d3cL)
  public static void FUN_800d1d3c(final EffectManagerData6c manager, final int angle, final short[] vertices, final PotionEffect14 effect, final Translucency translucency) {
    if(manager._10.flags_00 >= 0) {
      GPU.queueCommand(effect.z_04.get() + manager._10.z_22 >> 2, new GpuCommandPoly(3)
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
    if(manager._10.flags_00 >= 0) {
      final VECTOR sp0x20 = new VECTOR().set(
        rcos(angle) * (manager._10.scale_16.getX() / effect._01.get() + manager._10._28) >> 12,
        rsin(angle) * (manager._10.scale_16.getY() / effect._01.get() + manager._10._28) >> 12, // X is correct
        manager._10._2c
      );

      final ShortRef sp0x10 = new ShortRef();
      final ShortRef sp0x14 = new ShortRef();
      FUN_800cfb14(manager, sp0x20, sp0x10, sp0x14);

      final VECTOR sp0x30 = new VECTOR().set(
        rcos(angle + effect.angleStep_08.get()) * (manager._10.scale_16.getX() / effect._01.get() + manager._10._28) >> 12,
        rsin(angle + effect.angleStep_08.get()) * (manager._10.scale_16.getY() / effect._01.get() + manager._10._28) >> 12,
        manager._10._2c
      );

      final ShortRef sp0x18 = new ShortRef();
      final ShortRef sp0x1c = new ShortRef();
      FUN_800cfb14(manager, sp0x30, sp0x18, sp0x1c);

      GPU.queueCommand(effect.z_04.get() + manager._10.z_22 >> 2, new GpuCommandPoly(4)
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
  public static void renderPotionEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final PotionEffect14 effect = (PotionEffect14)manager.effect_44;
    effect.angleStep_08.set(0x1000 / (0x4 << effect._00.get()));

    final ShortRef sp0x48 = new ShortRef();
    final ShortRef sp0x4c = new ShortRef();
    effect.z_04.set(FUN_800cfb14(manager, new VECTOR(), sp0x48, sp0x4c) >> 2);

    final int z = effect.z_04.get() + manager._10.z_22;
    if(z >= 0xa0) {
      if(z >= 0xffe) {
        effect.z_04.set(0xffe - manager._10.z_22);
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
      effect._0c.set(manager._10._24 >>> 16 & 0xff);
      effect._0d.set(manager._10._24 >>>  8 & 0xff);
      effect._0e.set(manager._10._24        & 0xff);

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
        effect.renderer_10.deref().run(manager, angle, new short[] {sp0x48.get(), sp0x4c.get(), sp0x50.get(), sp0x54.get(), sp0x58.get(), sp0x5c.get()}, effect, (manager._10.flags_00 & 0x1000_0000) != 0 ? Translucency.B_PLUS_F : Translucency.B_MINUS_F);
        angle += effect.angleStep_08.get();
      }
    }

    //LAB_800d2710
  }

  @Method(0x800d2734L)
  public static FlowControl allocatePotionEffect(final RunningScript<? extends BattleScriptDataBase> script) {
    final int s2 = script.params_20[1].get();
    final int s1 = script.params_20[2].get();

    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0x14,
      null,
      Bttl_800d::renderPotionEffect,
      null,
      PotionEffect14::new
    );

    final EffectManagerData6c manager = state.innerStruct_00;

    //LAB_800d27b4
    manager._10.scale_16.set((short)0x1000, (short)0x1000, (short)0x1000);

    final PotionEffect14 effect = (PotionEffect14)manager.effect_44;
    effect._00.set(s2);
    effect._01.set(s1 - 3 > 1 ? 4 : 1);
    effect.renderer_10.set(effectRenderers_800fa758.get(s1).deref());
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x800d2810L)
  public static void renderGuardEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final VECTOR sp0x58 = new VECTOR();
    final IntRef[] sp0x18 = new IntRef[7];
    final IntRef[] sp0x38 = new IntRef[7];

    Arrays.setAll(sp0x18, i -> new IntRef());
    Arrays.setAll(sp0x38, i -> new IntRef());

    final GuardEffect06 s7 = (GuardEffect06)data.effect_44;
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
      int a0 = data._10.z_22;
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
        int a0 = data._10.z_22;
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

  @Method(0x800d2ff4L)
  public static FlowControl allocateGuardEffect(final RunningScript<? extends BattleScriptDataBase> script) {
    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0x6,
      null,
      Bttl_800d::renderGuardEffect,
      null,
      GuardEffect06::new
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    final GuardEffect06 effect = (GuardEffect06)manager.effect_44;
    effect._00.set(1);
    effect._02.set(0);
    effect._04.set((short)0);
    manager._10.colour_1c.setX((short)255);
    manager._10.colour_1c.setY((short)0);
    manager._10.colour_1c.setZ((short)0);
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x800d3090L)
  public static FlowControl FUN_800d3090(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @Method(0x800d3098L)
  public static FlowControl FUN_800d3098(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @Method(0x800d30a0L)
  public static FlowControl FUN_800d30a0(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @Method(0x800d30a8L)
  public static FlowControl FUN_800d30a8(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @Method(0x800d30b0L)
  public static FlowControl FUN_800d30b0(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @Method(0x800d30b8L)
  public static FlowControl FUN_800d30b8(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @Method(0x800d30c0L)
  public static void monsterDeathEffectRenderer(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final MonsterDeathEffect34 s1 = (MonsterDeathEffect34)data.effect_44;
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
  public static void monsterDeathEffectTicker(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final MonsterDeathEffect34 s1 = (MonsterDeathEffect34)data.effect_44;

    s1._02.decr();
    if(s1._02.get() == 0) {
      state.deallocateWithChildren();
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
          FUN_800d0094(s1.scriptIndex_08.get(), animIndex, false);
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
  public static void deallocateMonsterDeathEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    free(((MonsterDeathEffect34)data.effect_44).ptr_30.get());
  }

  @Method(0x800d34bcL)
  public static FlowControl allocateMonsterDeathEffect(final RunningScript<? extends BattleScriptDataBase> script) {
    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0x34,
      Bttl_800d::monsterDeathEffectTicker,
      Bttl_800d::monsterDeathEffectRenderer,
      Bttl_800d::deallocateMonsterDeathEffect,
      MonsterDeathEffect34::new
    );

    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00;
    final int animCount = bobj.model_148.animCount_98;
    final EffectManagerData6c manager = state.innerStruct_00;
    final MonsterDeathEffect34 effect = (MonsterDeathEffect34)manager.effect_44;
    long s4 = mallocTail(animCount * 0x30L);
    effect.ptr_30.set(s4); //TODO
    effect._00.set((short)0);
    effect._02.set(animCount + 8);
    effect.animCount_04.set(animCount);
    effect._06.set(0);
    effect.scriptIndex_08.set(script.params_20[1].get());

    //LAB_800d35a0
    for(int animIndex = 0; animIndex < effect.animCount_04.get(); animIndex++) {
      FUN_800d0094(effect.scriptIndex_08.get(), animIndex, true);
      MEMORY.ref(1, s4).offset(0x0L).setu(-0x1L);
      s4 = s4 + 0x30L;
    }

    //LAB_800d35cc
    final SpriteMetrics08 metrics = spriteMetrics_800c6948[script.params_20[2].get() & 0xff];
    effect._0c._00.set(manager._10.flags_00 & 0xffff_ffffL);
    effect._0c.w_08.set(metrics.w_04.get());
    effect._0c.h_0a.set(metrics.h_05.get());
    effect._0c.x_04.set((short)(-effect._0c.w_08.get() >> 1));
    effect._0c.y_06.set((short)(-effect._0c.h_0a.get() >> 1));
    effect._0c.tpage_0c.set((metrics.v_02.get() & 0x100) >>> 4 | (metrics.u_00.get() & 0x3ff) >>> 6);
    effect._0c.u_0e.set((metrics.u_00.get() & 0x3f) * 4);
    effect._0c.v_0f.set(metrics.v_02.get());
    effect._0c.clutX_10.set(metrics.clut_06.get() << 4 & 0x3ff);
    effect._0c.clutY_12.set(metrics.clut_06.get() >>> 6 & 0x1ff);
    effect._0c._18.set((short)0);
    effect._0c._1a.set((short)0);
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
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
    renderAdditionNameChar((short)charStruct.position_04, (short)charStruct.offsetY_06, (short)additionStruct.addition_02, (short)charIdx, (int)charAlpha);
  }

  @Method(0x800d3a64L)
  public static void FUN_800d3a64(final AdditionScriptData1c a0, final AdditionCharEffectData0c a1, final long charAlpha, final long a3) {
    final String sp0x18 = String.valueOf(a0._10);

    long s4;
    if(a0._04 < 25) {
      s4 = a0._04 & 1;
    } else {
      s4 = 0;
    }

    //LAB_800d3ab8
    //LAB_800d3ac4
    for(; s4 >= 0; s4--) {
      int s1 = a1.position_04;

      //LAB_800d3ad4
      for(int i = 0; i < sp0x18.length(); i++) {
        FUN_800d3f98((short)s1, (short)a1.offsetY_06, sp0x18.charAt(i) - 0x30, (short)41, (int)charAlpha);
        s1 = s1 + 8;
      }

      //LAB_800d3b08
      FUN_800d3f98((short) s1      , (short)a1.offsetY_06, 0x0d, (short)41, (int)charAlpha);
      FUN_800d3f98((short)(s1 +  8), (short)a1.offsetY_06, 0x0e, (short)41, (int)charAlpha);
      FUN_800d3f98((short)(s1 + 16), (short)a1.offsetY_06, 0x0f, (short)41, (int)charAlpha);
      FUN_800d3f98((short)(s1 + 24), (short)a1.offsetY_06, 0x10, (short)41, (int)charAlpha);
    }

    //LAB_800d3b98
  }

  @Method(0x800d3bb8L)
  public static void FUN_800d3bb8(final ScriptState<AdditionScriptData1c> state, final AdditionScriptData1c additionStruct) {
    additionStruct._04++;

    if(_800faa9d.get() == 0) {
      state.deallocateWithChildren();
    } else {
      //LAB_800d3c10
      //LAB_800d3c24
      for(int charIdx = 0; charIdx < additionStruct.length_08; charIdx++) {
        final AdditionCharEffectData0c charStruct = additionStruct.ptr_18[charIdx];

        if(charStruct.scrolling_00 != 0) {
          charStruct.position_04 += additionStruct._0c;

          if(charStruct.position_04 >= charStruct.offsetX_08) {
            charStruct.position_04 = charStruct.offsetX_08;
            charStruct.scrolling_00 = 0;
          }
        } else {
          //LAB_800d3c70
          if(charStruct.dupes_02 > 0) {
            charStruct.dupes_02--;
          }
        }

        //LAB_800d3c84
        //LAB_800d3c88
        additionStruct.renderer_14.accept(additionStruct, charStruct, 0x80L, (long)charIdx);
        int currPosition = charStruct.position_04;
        int s2 = charStruct.dupes_02 * 0x10;

        //LAB_800d3cbc
        for(int dupeNum = 0; dupeNum < charStruct.dupes_02 - 1; dupeNum++) {
          s2 -= 16;
          currPosition -= 10;
          final int origCharPosition = charStruct.position_04;
          charStruct.position_04 = currPosition;
          additionStruct.renderer_14.accept(additionStruct, charStruct, s2 & 0xffL, (long)charIdx);
          charStruct.position_04 = origCharPosition;
        }
      }
    }

    //LAB_800d3d1c
  }

  @Method(0x800d3d74L)
  public static FlowControl scriptAllocateAdditionScript(final RunningScript<?> script) {
    if(script.params_20[1].get() == -1) {
      _800faa9d.setu(0);
    } else {
      //LAB_800d3dc0
      final int addition = gameState_800babc8.charData_32c.get(script.params_20[0].get()).selectedAddition_19.get();
      final ScriptState<AdditionScriptData1c> state = SCRIPTS.allocateScriptState(new AdditionScriptData1c());
      state.loadScriptFile(doNothingScript_8004f650);
      state.setTicker(Bttl_800d::FUN_800d3bb8);
      final CString additionName = getAdditionName(0, addition);

      //LAB_800d3e5c
      int textLength;
      for(textLength = 0; additionName.charAt(textLength) != 0; textLength++) {
        //
      }

      //LAB_800d3e7c
      final AdditionScriptData1c additionStruct = state.innerStruct_00;
      additionStruct._00 = 0;
      additionStruct.addition_02 = addition;
      additionStruct._04 = 0;
      additionStruct.length_08 = textLength;
      additionStruct._0c = 120;
      additionStruct.renderer_14 = Bttl_800d::renderAdditionNameChar;
      additionStruct.ptr_18 = new AdditionCharEffectData0c[additionStruct.length_08];
      Arrays.setAll(additionStruct.ptr_18, i -> new AdditionCharEffectData0c());
      _800faa9d.setu(0x1L);

      final int[] displayOffset = setAdditionNameDisplayCoords(0, addition);
      int charPosition = -160;
      int displayOffsetX = displayOffset[0];
      final int displayOffsetY = displayOffset[1];

      //LAB_800d3f18
      for(int charIdx = 0; charIdx < textLength; charIdx++) {
        final AdditionCharEffectData0c charStruct = additionStruct.ptr_18[charIdx];
        charStruct.scrolling_00 = 1;
        charStruct.dupes_02 = 8;
        charStruct.position_04 = charPosition;
        charStruct.offsetY_06 = displayOffsetY;
        charStruct.offsetX_08 = displayOffsetX;
        charStruct.offsetY_0a = displayOffsetY;
        displayOffsetX += getCharDisplayWidth(additionName.charAt(charIdx));
        charPosition -= 80;
      }
    }

    //LAB_800d3f70
    return FlowControl.CONTINUE;
  }

  @Method(0x800d3f98L)
  public static void FUN_800d3f98(final short x, final short y, final int a2, final short a3, final int colour) {
    FUN_80018d60(x, y, a2 * 8 + 16 & 0xf8, 40, 8, 16, a3, Translucency.B_PLUS_F, new COLOUR().set(colour, colour, colour), 0x1000L);
  }

  @Method(0x800d4018L)
  public static void FUN_800d4018(final ScriptState<BttlScriptData40> state, final BttlScriptData40 s3) {
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

    if(_800faa94.get() == 0 && s3._00 == 0) {
      if(s3._02 == 0) {
        state.deallocateWithChildren();
        return;
      }

      //LAB_800d408c
      s3._02 -= 16;
    }

    //LAB_800d4090
    s3._04++;
    s3._10 += s3._30;
    s3._30 += 0x200;

    if(s3._20 < s3._10) {
      s3._10 = s3._20;
      s3._00 = 0;
    }

    //LAB_800d40cc
    s3._0c += s3._2c;

    if(Math.abs(s3._1c) < Math.abs(s3._0c)) {
      s3._0c = s3._1c;
    }

    //LAB_800d4108
    v0 = s3.ptr_3c;
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

    MEMORY.ref(4, s4).offset(0x0L).setu(s3._0c);
    MEMORY.ref(4, s4).offset(0x4L).setu(s3._10);
    s6 = 0;
    final String sp0x18 = Integer.toString(s3._08);
    fp = s3._02;
    s4 = s3.ptr_3c;
    v1 = fp & 0xffL;
    v0 = s3._01 + 0x21L;
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

      if(s6 == 0 || MEMORY.ref(4, s4).offset(0x0L).get() != s3._0c || MEMORY.ref(4, sp30).offset(0x0L).get() != s3._10) {
        //LAB_800d41d8
        s2 = (int)MEMORY.ref(4, s4).offset(0x0L).get() >> 8;
        s5 = (int)MEMORY.ref(4, sp30).offset(0x0L).get() >> 8;

        if(s3._01 != 0) {
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
  public static void FUN_800d430c(final ScriptState<BttlScriptData40> state, final BttlScriptData40 data) {
    free(state.innerStruct_00.ptr_3c);
  }

  @Method(0x800d4338L)
  public static FlowControl FUN_800d4338(final RunningScript<?> script) {
    final int s2 = script.params_20[0].get();
    final int s3 = script.params_20[1].get();

    if(s2 == -1) {
      _800faa94.setu(0);
      script.params_20[1].set(0);
    } else {
      //LAB_800d4388
      final ScriptState<BttlScriptData40> state = SCRIPTS.allocateScriptState(new BttlScriptData40());
      state.loadScriptFile(doNothingScript_8004f650);
      state.setTicker(Bttl_800d::FUN_800d4018);
      state.setDestructor(Bttl_800d::FUN_800d430c);

      final BttlScriptData40 s1 = state.innerStruct_00;
      s1._00 = 1;
      s1._02 = 0x80;
      s1._04 = 0;
      s1._08 = s2;
      s1._0c = 0;
      s1._10 = 30;
      s1._1c = (int)(_800faa90.getSigned() << 8);
      s1._20 = 0x5000;
      s1.ptr_3c = mallocTail(0x80);

      if(s3 == 1) {
        _800faa92.setu(0);
        _800faa94.setu(s3);
        _800faa98.setu(0);
        s1._01 = 0;
        s1._1c = 0xffff6e00;
        _800faa90.setu(-0x92L);
      } else {
        //LAB_800d4470
        _800faa92.addu(0x1L);
        s1._01 = (int)_800faa92.get();
      }

      //LAB_800d448c
      s1._2c = (s1._1c - s1._0c) / 14;
      s1._30 = -0x800;
      _800faa98.addu(s2);

      //LAB_800d44dc
      long a2 = s1.ptr_3c;
      for(long a3 = 0; a3 < 8; a3++) {
        MEMORY.ref(4, a2).offset(0x0L).setu(s1._0c);
        MEMORY.ref(4, a2).offset(0x4L).setu(s1._10);
        a2 = a2 + 0x10L;
      }

      final int strLen = String.valueOf(s1._08).length();

      final long v1;
      if(s1._01 == 0) {
        v1 = strLen + 2;
      } else {
        v1 = strLen + 3;
      }

      //LAB_800d453c
      _800faa90.setu((s1._1c >> 8) + v1 * 8 - 3);
      script.params_20[1].set(0);
    }

    //LAB_800d4560
    return FlowControl.CONTINUE;
  }

  @Method(0x800d4580L)
  public static FlowControl FUN_800d4580(final RunningScript<?> script) {
    final int s2 = script.params_20[0].get();
    if(s2 != -1) {
      final ScriptState<AdditionScriptData1c> state = SCRIPTS.allocateScriptState(new AdditionScriptData1c());
      state.loadScriptFile(doNothingScript_8004f650);
      state.setTicker(Bttl_800d::FUN_800d3bb8);
      final AdditionScriptData1c s0 = state.innerStruct_00;
      s0.ptr_18 = new AdditionCharEffectData0c[] {new AdditionCharEffectData0c()};
      _800faa9c.setu(0x1L);
      s0._0c = 40;
      s0.renderer_14 = Bttl_800d::FUN_800d3a64;
      s0._00 = 0;
      s0.addition_02 = 0;
      s0._04 = 0;
      s0.length_08 = 1;
      s0._10 = s2;
      final AdditionCharEffectData0c struct = s0.ptr_18[0];
      struct.scrolling_00 = 1;
      struct.dupes_02 = 8;
      struct.position_04 = -160;
      struct.offsetY_06 = 96;
      struct.offsetX_08 = 144 - (String.valueOf(s2).length() + 4) * 8;
      struct.offsetY_0a = 96;
    } else {
      //LAB_800d46b0
      _800faa9c.setu(0);
    }

    //LAB_800d46bc
    return FlowControl.CONTINUE;
  }

  @Method(0x800d46d4L)
  public static FlowControl FUN_800d46d4(final RunningScript<?> script) {
    final long v1 = _800faaa0.offset(1, script.params_20[0].get() * 0x6L).getAddress();

    final COLOUR colour = new COLOUR().set(script.params_20[4].get(), script.params_20[4].get(), script.params_20[4].get());

    if(MEMORY.ref(1, v1).offset(0x0L).get() == 0) {
      FUN_80018d60(script.params_20[1].get(), script.params_20[2].get(), (int)MEMORY.ref(1, v1).offset(0x1L).get(), (int)MEMORY.ref(1, v1).offset(0x2L).get(), (int)MEMORY.ref(1, v1).offset(0x3L).get(), (int)MEMORY.ref(1, v1).offset(0x4L).get(), MEMORY.ref(1, v1).offset(0x5L).get(), Translucency.of(script.params_20[3].get()), colour, 0x1000L);
    } else {
      //LAB_800d4784
      FUN_80018a5c(script.params_20[1].get(), script.params_20[2].get(), (int)MEMORY.ref(1, v1).offset(0x1L).get(), (int)MEMORY.ref(1, v1).offset(0x2L).get(), (int)MEMORY.ref(1, v1).offset(0x3L).get(), (int)MEMORY.ref(1, v1).offset(0x4L).get(), MEMORY.ref(1, v1).offset(0x5L).get(), Translucency.of(script.params_20[3].get()), colour, 0x1000L, 0x1000L);
    }

    //LAB_800d47cc
    return FlowControl.CONTINUE;
  }

  @Method(0x800d47dcL)
  public static void FUN_800d47dc(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.vec_94.set(cam.rview2_00.viewpoint_00).mul(0x100);

    if(a5 == 0) {
      //LAB_800d4854
      cam._d0 = a3;
      cam._b0 = (a0 - cam.vec_94.getX()) / a3;
      cam._bc = (a1 - cam.vec_94.getY()) / a3;
      cam._c8 = (a2 - cam.vec_94.getZ()) / a3;
    } else assert a5 != 1 : "Undefined t0/t1";

    //LAB_800d492c
    //LAB_800d4934
    cam._11c |= 0x1;
    cam._120 = 8;
  }

  @Method(0x800d496cL)
  public static void FUN_800d496c(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;

    cam._ac = FUN_800dc384(0, 1, 0, 0) << 8;
    cam._b8 = FUN_800dc384(0, 1, 1, 0) << 8;
    cam._a0 = FUN_800dc384(0, 1, 2, 0) << 8;

    if(a5 == 0) {
      //LAB_800d4a24
      cam._d0 = a3;
      cam._b0 = FUN_800dcf10(0, cam._ac, a0, a3, a4 & 3);
      cam._bc = FUN_800dcf10(1, cam._b8, a1, a3, a4 >> 2 & 3);
      cam._c8 = FUN_800dcf10(2, cam._a0, a2, a3, 0);
    } else if(a5 == 1) {
      //LAB_800d4a7c
      final int s1 = FUN_800dcfb8(0, cam._ac, a0, a4 & 3);
      final int s0 = FUN_800dcfb8(1, cam._b8, a1, a4 >> 2 & 3);
      final int v0 = FUN_800dcfb8(2, cam._a0, a2, 0);
      cam._d0 = SquareRoot0(s1 * s1 + s0 * s0 + v0 * v0) / a3;
      cam._b0 = FUN_800dcf10(0, cam._ac, a0, cam._d0, a4 & 3);
      cam._bc = FUN_800dcf10(1, cam._b8, a1, cam._d0, a4 >> 2 & 3);
      cam._c8 = FUN_800dcf10(2, cam._a0, a2, cam._d0, 0);
    }

    //LAB_800d4b68
    cam._11c |= 0x1;
    cam._120 = 9;
  }

  @Method(0x800d4bacL)
  public static void FUN_800d4bac(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.vec_94.setX(FUN_800dc384(0, 0x4L, 0, 0) << 8);
    cam.vec_94.setY(FUN_800dc384(0, 0x4L, 1, 0) << 8);
    cam.vec_94.setZ(FUN_800dc384(0, 0x4L, 2, 0) << 8);

    if(a5 == 0) {
      //LAB_800d4c5c
      cam._d0 = a3;
      cam._b0 = (a0 - cam.vec_94.getX()) / a3;
      cam._bc = (a1 - cam.vec_94.getY()) / a3;
      cam._c8 = (a2 - cam.vec_94.getZ()) / a3;
    } else assert a5 != 1 : "Undefined s3/s5";

    //LAB_800d4d34
    //LAB_800d4d3c
    cam._11c |= 0x1;
    cam._120 = 12;
  }

  @Method(0x800d4d7cL)
  public static void FUN_800d4d7c(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;

    cam._ac = FUN_800dc384(0, 0x5L, 0, 0) << 8;
    cam._b8 = FUN_800dc384(0, 0x5L, 1, 0) << 8;
    cam._a0 = FUN_800dc384(0, 0x5L, 2, 0) << 8;

    if(a5 == 0) {
      //LAB_800d4e34
      cam._d0 = a3;
      cam._b0 = FUN_800dcf10(0, cam._ac, a0, a3, a4 & 3);
      cam._bc = FUN_800dcf10(1, cam._b8, a1, a3, a4 >> 2 & 3);
      cam._c8 = FUN_800dcf10(2, cam._a0, a2, a3, 0);
    } else if(a5 == 1) {
      //LAB_800d4e8c
      final int s1 = FUN_800dcfb8(0, cam._ac, a0, a4 & 3);
      final int s0 = FUN_800dcfb8(1, cam._b8, a1, a4 >> 2 & 3);
      final int v0 = FUN_800dcfb8(2, cam._a0, a2, 0);
      cam._d0 = SquareRoot0(s1 * s1 + s0 * s0 + v0 * v0) / a3;
      cam._b0 = FUN_800dcf10(0, cam._ac, a0, cam._d0, a4 & 3);
      cam._bc = FUN_800dcf10(1, cam._b8, a1, cam._d0, a4 >> 2 & 3);
      cam._c8 = FUN_800dcf10(2, cam._a0, a2, cam._d0, 0);
    }

    //LAB_800d4f78
    cam._11c |= 0x1;
    cam._120 = 13;
  }

  @Method(0x800d4fbcL)
  public static void FUN_800d4fbc(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.bobjIndex_f4 = scriptIndex;
    cam.vec_94.setX(FUN_800dc384(0, 0x6L, 0, scriptIndex) << 8);
    cam.vec_94.setY(FUN_800dc384(0, 0x6L, 1, scriptIndex) << 8);
    cam.vec_94.setZ(FUN_800dc384(0, 0x6L, 2, scriptIndex) << 8);

    if(a5 == 0) {
      //LAB_800d5078
      cam._d0 = a3;

      if(a3 != 0) {
        cam._b0 = (a0 - cam.vec_94.getX()) / a3;
        cam._bc = (a1 - cam.vec_94.getY()) / a3;
        cam._c8 = (a2 - cam.vec_94.getZ()) / a3;
      } else {
        cam._b0 = -1;
        cam._bc = -1;
        cam._c8 = -1;
      }
    } else if(a5 == 1) {
      //LAB_800d50c4
      final int x = a0 - cam.vec_94.getX();
      final int y = a1 - cam.vec_94.getY();
      final int z = a2 - cam.vec_94.getZ();
      final int v0 = SquareRoot0(z * z + y * y + x * x) / a3;
      cam._d0 = v0;

      if(v0 != 0) {
        cam._b0 = (a0 - cam.vec_94.getX()) / v0;
        cam._bc = (a1 - cam.vec_94.getY()) / v0;
        cam._c8 = (a2 - cam.vec_94.getZ()) / v0;
      } else {
        cam._b0 = -1;
        cam._bc = -1;
        cam._c8 = -1;
      }
    }

    //LAB_800d5150
    //LAB_800d5158
    cam._11c |= 0x1;
    cam._120 = 14;
  }

  @Method(0x800d519cL)
  public static void FUN_800d519c(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    final BattleCamera s4 = camera_800c67f0;
    s4.bobjIndex_f4 = scriptIndex;
    s4._ac = FUN_800dc384(0, 0x7L, 0, scriptIndex) << 8;
    s4._b8 = FUN_800dc384(0, 0x7L, 1, scriptIndex) << 8;
    s4._a0 = FUN_800dc384(0, 0x7L, 2, scriptIndex) << 8;

    if(a5 == 0) {
      //LAB_800d525c
      s4._d0 = a3;
      s4._b0 = FUN_800dcf10(0, s4._ac, a0, a3, a4 & 3);
      s4._bc = FUN_800dcf10(1, s4._b8, a1, a3, a4 >> 2 & 3);
      s4._c8 = FUN_800dcf10(2, s4._a0, a2, a3, 0);
    } else if(a5 == 1) {
      //LAB_800d52b4
      final long s1 = FUN_800dcfb8(0, s4._ac, a0, a4 & 3);
      final long s0 = FUN_800dcfb8(1, s4._b8, a1, a4 >> 2 & 3);
      final long v0 = FUN_800dcfb8(2, s4._a0, a2, 0);
      s4._d0 = SquareRoot0(s1 * s1 + s0 * s0 + v0 * v0) / a3;
      s4._b0 = FUN_800dcf10(0, s4._ac, a0, s4._d0, a4 & 3);
      s4._bc = FUN_800dcf10(1, s4._b8, a1, s4._d0, a4 >> 2 & 3);
      s4._c8 = FUN_800dcf10(2, s4._a0, a2, s4._d0, 0);
    }

    //LAB_800d53a0
    s4._11c |= 0x1;
    s4._120 = 15;
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
    cam._dc = SquareRoot0(s3 * s3 + v0 * v0 + s0 * s0) << 9;
    cam._d4 = (ratan2(s2, s5) & 0xfff) << 8;
    final int a0_0 = cam._dc * 2 / (a3 + a4);
    final int s4 = (a4 - a3) / a0_0;
    cam._d8 = (ratan2(s6, SquareRoot0(s3 * s3 + s0 * s0) * 2) & 0xfff) << 8;
    cam._a4 = a3;
    cam._e8 = a0;
    cam._ec = a1;
    cam._f0 = a2;
    cam._11c |= 0x1;
    cam._120 = 16;
    cam._d0 = a0_0;
    cam._b4 = s4;
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
    cam._d0 = a3;
    cam._d4 = (ratan2(s1, s3) & 0xfff) << 8;
    cam._d8 = (ratan2(s4, SquareRoot0(s2 * s2 + s0 * s0) * 2) & 0xfff) << 8;
    cam._dc = SquareRoot0(s2 * s2 + v0 * v0 + s0 * s0) << 9;

    final int s6;
    final int s7;
    if(a4 == 0) {
      //LAB_800d5ff0
      s6 = a5;
      s7 = cam._dc * 2 / a3 - a5;
    } else if(a4 == 1) {
      //LAB_800d6010
      s6 = cam._dc * 2 / a3 - a5;
      s7 = a5;
    } else {
      throw new RuntimeException("Undefined s6/s7");
    }

    //LAB_800d6030
    //LAB_800d6038
    cam._a4 = s6;
    cam._b4 = (s7 - s6) / cam._d0;
    cam._e8 = a0;
    cam._ec = a1;
    cam._f0 = a2;
    cam._11c |= 0x1;
    cam._120 = 16;
  }

  @Method(0x800d60b0L)
  public static void FUN_800d60b0(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam._ac = FUN_800dc384(0, 1, 0, 0) << 8;
    cam._b8 = FUN_800dc384(0, 1, 1, 0) << 8;
    cam._a0 = FUN_800dc384(0, 1, 2, 0) << 8;
    final int s1 = FUN_800dcfb8(0, cam._ac, a0, a6 & 3) >> 8;
    final int s0 = FUN_800dcfb8(1, cam._b8, a1, a6 >> 2 & 3) >> 8;
    FUN_800dcfb8(2, cam._a0, a2, 0);
    cam._d0 = a3;
    cam._d4 = (ratan2(s0, s1) & 0xfff) << 8;
    cam._d8 = 0;
    cam._dc = SquareRoot0(s1 * s1 + s0 * s0) << 8;

    final int s4;
    final int s3;
    if(a4 == 0) {
      //LAB_800d61fc
      s3 = a5;
      s4 = cam._dc * 2 / a3 - a5;
    } else if(a4 == 1) {
      //LAB_800d621c
      s3 = cam._dc * 2 / a3 - a5;
      s4 = a5;
    } else {
      throw new IllegalArgumentException("a4 must be 0 or 1");
    }

    //LAB_800d6238
    //LAB_800d6240
    cam._e0 = s3;
    cam._e8 = a0;
    cam._ec = a1;
    cam._f0 = a2;
    cam._e4 = (s4 - s3) / cam._d0;
    cam._a4 = FUN_800dcfb8(2, cam._a0, a2, 0) / cam._d0;
    cam._11c |= 0x1;
    cam._120 = 17;
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
    cam._d0 = a3;
    cam._d4 = (ratan2(s3, s4) & 0xfff) << 8;
    cam._d8 = (ratan2(s5, SquareRoot0(s1 * s1 + s0 * s0) * 2) & 0xfff) << 8;
    cam._dc = SquareRoot0(s1 * s1 + v1 * v1 + s0 * s0) << 9;
    final IntRef sp0x18 = new IntRef();
    final IntRef sp0x1c = new IntRef();
    FUN_800dcebc(a4, a5, cam._dc, a3, sp0x18, sp0x1c);
    cam._e8 = a0;
    cam._ec = a1;
    cam._f0 = a2;
    cam._a4 = sp0x18.get();
    cam._b4 = (sp0x1c.get() - sp0x18.get()) / cam._d0;
    cam._11c |= 0x1;
    cam._120 = 20;
  }

  @Method(0x800d64e4L)
  public static void FUN_800d64e4(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam._ac = FUN_800dc384(0, 0x5L, 0, 0) << 8;
    cam._b8 = FUN_800dc384(0, 0x5L, 1, 0) << 8;
    cam._a0 = FUN_800dc384(0, 0x5L, 2, 0) << 8;
    final int s1 = FUN_800dcfb8(0, cam._ac, a0, a6 & 3) >> 8;
    final int s0 = FUN_800dcfb8(1, cam._b8, a1, a6 >> 2 & 3) >> 8;
    FUN_800dcfb8(2, cam._a0, a2, 0);
    cam._dc = SquareRoot0(s1 * s1 + s0 * s0) << 8;
    cam._d4 = (ratan2(s0, s1) & 0xfff) << 8;
    cam._d8 = 0;
    cam._d0 = a3;

    final int s3;
    final int s4;
    if(a4 == 0) {
      //LAB_800d6630
      s3 = a5;
      s4 = cam._dc * 2 / a3 - a5;
    } else if(a4 == 1) {
      //LAB_800d6650
      s3 = cam._dc * 2 / a3 - a5;
      s4 = a5;
    } else {
      throw new RuntimeException("Undefined s3");
    }

    //LAB_800d666c
    //LAB_800d6674
    cam._e0 = s3;
    cam._e8 = a0;
    cam._ec = a1;
    cam._f0 = a2;
    cam._e4 = (s4 - s3) / cam._d0;
    cam._a4 = FUN_800dcfb8(2, cam._a0, a2, 0) / cam._d0;
    cam._11c |= 0x1;
    cam._120 = 21;
  }

  @Method(0x800d670cL)
  public static void FUN_800d670c(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.bobjIndex_f4 = scriptIndex;
    cam.vec_94.setX(FUN_800dc384(0, 0x6L, 0, scriptIndex) << 8);
    cam.vec_94.setY(FUN_800dc384(0, 0x6L, 1, scriptIndex) << 8);
    cam.vec_94.setZ(FUN_800dc384(0, 0x6L, 2, scriptIndex) << 8);
    final int s3 = a0 - cam.vec_94.getX() >> 8;
    final int s4 = a1 - cam.vec_94.getY() >> 8;
    final int s2 = a2 - cam.vec_94.getZ() >> 8;
    final int s1 = s3 / 2;
    final int v1 = s4 / 2;
    final int s0 = s2 / 2;
    cam._d0 = a3;
    cam._d4 = (ratan2(s2, s3) & 0xfff) << 8;
    cam._d8 = (ratan2(s4, SquareRoot0(s1 * s1 + s0 * s0) * 2) & 0xfff) << 8;
    cam._dc = SquareRoot0(s1 * s1 + v1 * v1 + s0 * s0) << 9;

    final int s6;
    final int s7;
    if(a4 == 0) {
      //LAB_800d68a0
      s6 = a5;
      s7 = cam._dc * 2 / a3 - a5;
    } else if(a4 == 1) {
      //LAB_800d68c0
      s6 = cam._dc * 2 / a3 - a5;
      s7 = a5;
    } else {
      throw new RuntimeException("Undefined s6/s7");
    }

    //LAB_800d68e0
    //LAB_800d68e8
    cam._a4 = s6;
    cam._e8 = a0;
    cam._ec = a1;
    cam._f0 = a2;
    cam._b4 = (s7 - s6) / cam._d0;
    cam._11c |= 0x1;
    cam._120 = 22;
  }

  @Method(0x800d6960L)
  public static void FUN_800d6960(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.bobjIndex_f4 = scriptIndex;
    cam._ac = FUN_800dc384(0, 0x7L, 0, scriptIndex) << 8;
    cam._b8 = FUN_800dc384(0, 0x7L, 1, scriptIndex) << 8;
    cam._a0 = FUN_800dc384(0, 0x7L, 2, scriptIndex) << 8;
    final int s1 = FUN_800dcfb8(0, cam._ac, a0, a6 & 3) >> 8;
    final int s0 = FUN_800dcfb8(1, cam._b8, a1, a6 >> 2 & 3) >> 8;
    FUN_800dcfb8(2, cam._a0, a2, 0); //TODO this method just returns a value, should it be used in the same way as the two calls above?
    cam._d0 = a3;
    cam._d4 = ratan2(s0, s1) & 0xfff << 8;
    cam._d8 = 0;
    cam._dc = SquareRoot0(s1 * s1 + s0 * s0) << 8;
    final int s3;
    final int s4;
    if(a4 == 0) {
      //LAB_800d6ab4
      s3 = a5;
      s4 = cam._dc * 2 / a3 - a5;
    } else if(a4 == 1) {
      //LAB_800d6ad4
      s3 = cam._dc * 2 / a3 - a5;
      s4 = a5;
    } else {
      throw new RuntimeException("s3/s4 undefined");
    }

    //LAB_800d6af0
    //LAB_800d6af8
    cam._e0 = s3;
    cam._e8 = a0;
    cam._ec = a1;
    cam._f0 = a2;
    cam._e4 = (s4 - s3) / cam._d0;
    cam._a4 = FUN_800dcfb8(2, cam._a0, a2, 0) / cam._d0;
    cam._11c |= 0x1;
    cam._120 = 23;
  }

  @Method(0x800d6b90L)
  public static void FUN_800d6b90(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.vec_20.set(cam.rview2_00.refpoint_0c).mul(0x100);

    if(a5 == 0) {
      //LAB_800d6c04
      cam._5c = a3;

      // Retail bug: divide by 0 is possible here - the processor sets LO to -1 in this case
      if(a3 != 0) {
        cam._3c = (a0 - cam.vec_20.getX()) / a3;
        cam._48 = (a1 - cam.vec_20.getY()) / a3;
        cam._54 = (a2 - cam.vec_20.getZ()) / a3;
      } else {
        cam._3c = -1;
        cam._48 = -1;
        cam._54 = -1;
      }
    } else if(a5 == 1) {
      throw new RuntimeException("t0/t1 undefined");
    }

    //LAB_800d6cdc
    //LAB_800d6ce4
    cam._11c |= 0x2;
    cam._121 = 8;
  }

  @Method(0x800d6d18L)
  public static void FUN_800d6d18(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam._38 = FUN_800dc384(1, 1, 0, 0) << 8;
    cam._44 = FUN_800dc384(1, 1, 1, 0) << 8;
    cam._2c = FUN_800dc384(1, 1, 2, 0) << 8;

    if(a5 == 0) {
      //LAB_800d6dd0
      cam._5c = a3;
      cam._3c = FUN_800dcf10(0, cam._38, a0, a3, a4 & 3);
      cam._48 = FUN_800dcf10(1, cam._44, a1, a3, a4 >> 2 & 3);
      cam._54 = FUN_800dcf10(2, cam._2c, a2, a3, 0);
    } else if(a5 == 1) {
      //LAB_800d6e28
      final int s1 = FUN_800dcfb8(0, cam._38, a0, a4 & 3);
      final int s0 = FUN_800dcfb8(1, cam._44, a1, a4 >> 2 & 3);
      final int v0 = FUN_800dcfb8(2, cam._2c, a2, 0);
      cam._5c = SquareRoot0(s1 * s1 + s0 * s0 + v0 * v0) / a3;
      cam._3c = FUN_800dcf10(0, cam._38, a0, cam._5c, a4 & 3);
      cam._48 = FUN_800dcf10(1, cam._44, a1, cam._5c, a4 >> 2 & 3);
      cam._54 = FUN_800dcf10(2, cam._2c, a2, cam._5c, 0);
    }

    //LAB_800d6f14
    cam._11c |= 0x2;
    cam._121 = 9;
  }

  @Method(0x800d6f58L)
  public static void FUN_800d6f58(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;

    cam.vec_20.setX(FUN_800dc384(1, 2, 0, 0) << 8);
    cam.vec_20.setY(FUN_800dc384(1, 2, 1, 0) << 8);
    cam.vec_20.setZ(FUN_800dc384(1, 2, 2, 0) << 8);

    if(a5 == 0) {
      //LAB_800d7008
      cam._5c = a3;
      cam._3c = (a0 - cam.vec_20.getX()) / a3;
      cam._48 = (a1 - cam.vec_20.getY()) / a3;
      cam._54 = (a2 - cam.vec_20.getZ()) / a3;
    } else if(a5 == 1) {
      throw new RuntimeException("Broken code");
    }

    //LAB_800d70e0
    //LAB_800d70e8
    cam._11c |= 0x2;
    cam._121 = 10;
  }

  @Method(0x800d7128L)
  public static void FUN_800d7128(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;

    cam._38 = FUN_800dc384(1, 3, 0, 0) << 8;
    cam._44 = FUN_800dc384(1, 3, 1, 0) << 8;
    cam._2c = FUN_800dc384(1, 3, 2, 0) << 8;

    if(a5 == 0) {
      //LAB_800d71e0
      cam._5c = a3;
      cam._3c = FUN_800dcf10(0, cam._38, a0, a3, a4 & 3);
      cam._48 = FUN_800dcf10(1, cam._44, a1, a3, a4 >> 2 & 3);
      cam._54 = FUN_800dcf10(2, cam._2c, a2, a3, 0);
    } else if(a5 == 1) {
      //LAB_800d7238
      final int s1 = FUN_800dcfb8(0, cam._38, a0, a4 & 3);
      final int s0 = FUN_800dcfb8(1, cam._44, a1, a4 >> 2 & 3);
      final int v0 = FUN_800dcfb8(2, cam._2c, a2, 0);
      cam._5c = SquareRoot0(s1 * s1 + s0 * s0 + v0 * v0) / a3;
      cam._3c = FUN_800dcf10(0, cam._38, a0, cam._5c, a4 & 3);
      cam._48 = FUN_800dcf10(1, cam._44, a1, cam._5c, a4 >> 2 & 3);
      cam._54 = FUN_800dcf10(2, cam._2c, a2, cam._5c, 0);
    }

    //LAB_800d7324
    cam._11c |= 0x2;
    cam._121 = 11;
  }

  @Method(0x800d7368L)
  public static void FUN_800d7368(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.bobjIndex_80 = scriptIndex;
    cam.vec_20.setX(FUN_800dc384(1, 0x6L, 0, scriptIndex) << 8);
    cam.vec_20.setY(FUN_800dc384(1, 0x6L, 1, scriptIndex) << 8);
    cam.vec_20.setZ(FUN_800dc384(1, 0x6L, 2, scriptIndex) << 8);

    if(a5 == 0) {
      //LAB_800d7424
      cam._5c = a3;
      cam._3c = (a0 - cam.vec_20.getX()) / a3;
      cam._48 = (a1 - cam.vec_20.getY()) / a3;
      cam._54 = (a2 - cam.vec_20.getZ()) / a3;
    } else if(a5 == 1) {
      throw new RuntimeException("Undefined s5/s6");
    }

    //LAB_800d74fc
    //LAB_800d7504
    cam._11c |= 0x2;
    cam._121 = 14;
  }

  @Method(0x800d7548L)
  public static void FUN_800d7548(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;

    cam.bobjIndex_80 = scriptIndex;
    cam._38 = FUN_800dc384(1, 7, 0, scriptIndex) << 8;
    cam._44 = FUN_800dc384(1, 7, 1, scriptIndex) << 8;
    cam._2c = FUN_800dc384(1, 7, 2, scriptIndex) << 8;

    if(a5 == 0) {
      //LAB_800d7608
      cam._5c = a3;
      cam._3c = FUN_800dcf10(0, cam._38, a0, a3, a4 & 3);
      cam._48 = FUN_800dcf10(1, cam._44, a1, a3, a4 >> 2 & 3);
      cam._54 = FUN_800dcf10(2, cam._2c, a2, a3, 0);
    } else if(a5 == 1) {
      //LAB_800d7660
      final int s1 = FUN_800dcfb8(0, cam._38, a0, a4 & 3);
      final int s0 = FUN_800dcfb8(1, cam._44, a1, a4 >> 2 & 3);
      final int v0 = FUN_800dcfb8(2, cam._2c, a2, 0);
      cam._5c = SquareRoot0(s1 * s1 + s0 * s0 + v0 * v0) / a3;
      cam._3c = FUN_800dcf10(0, cam._38, a0, cam._5c, a4 & 3);
      cam._48 = FUN_800dcf10(1, cam._44, a1, cam._5c, a4 >> 2 & 3);
      cam._54 = FUN_800dcf10(2, cam._2c, a2, cam._5c, 0);
    }

    //LAB_800d774c
    cam._11c |= 0x2;
    cam._121 = 15;
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
    cam._30 = a3;
    cam.vec_74.set(x, y, z);
    cam._11c |= 0x2;
    cam._121 = 16;
    cam._5c = cam.vec_60.getZ() * 2 / (a4 + a3);

    if(cam._5c > 0) {
      cam._40 = (a4 - a3) / cam._5c;
    } else {
      cam._40 = -1;
    }
  }

  @Method(0x800d7920L)
  public static void FUN_800d7920(final int x, final int y, final int z, final int a3, final int a4, final int a5, final int a6) {
    final BattleCamera cam = camera_800c67f0;
    cam._38 = FUN_800dc384(1, 1, 0, 0) << 8;
    cam._44 = FUN_800dc384(1, 1, 1, 0) << 8;
    cam._2c = FUN_800dc384(1, 1, 2, 0) << 8;
    final int s2 = FUN_800dcfb8(0, cam._38, x, a5 & 3) >> 8;
    final int s1 = FUN_800dcfb8(1, cam._44, y, a5 >> 2 & 3) >> 8;
    FUN_800dcfb8(2, cam._2c, z, 0);
    cam.vec_60.setX((ratan2(s1, s2) & 0xfff) << 8);
    cam.vec_60.setY(0);
    cam.vec_60.setZ(SquareRoot0(s2 * s2 + s1 * s1) << 8);
    cam._6c = a3;
    cam.vec_74.set(x, y, z);
    cam._5c = cam.vec_60.getZ() * 2 / (a3 + a4);
    cam._121 = 17;
    cam._11c |= 0x2;

    if(cam._5c > 0) {
      cam._30 = FUN_800dcfb8(2, cam._2c, z, 0) / cam._5c;
      cam._70 = (a4 - a3) / cam._5c;
    } else {
      cam._30 = -1;
      cam._70 = -1;
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
    cam._30 = a3;
    cam.vec_74.set(x, y, z);
    cam._11c |= 0x2;
    cam._121 = 18;
    cam._5c = cam.vec_60.getZ() * 2 / (a3 + a4);

    if(cam._5c > 0) {
      cam._40 = (a4 - a3) / cam._5c;
    } else {
      cam._40 = -1;
    }
  }

  @Method(0x800d7cdcL)
  public static void FUN_800d7cdc(final int x, final int y, final int z, final int a3, final int a4, final int a5, final int a6) {
    final BattleCamera cam = camera_800c67f0;
    cam._38 = FUN_800dc384(1, 3, 0, 0) << 8;
    cam._44 = FUN_800dc384(1, 3, 1, 0) << 8;
    cam._2c = FUN_800dc384(1, 3, 2, 0) << 8;
    final int s2 = FUN_800dcfb8(0, cam._38, x, a5 & 0x3) >> 8;
    final int s1 = FUN_800dcfb8(1, cam._44, y, a5 >> 2 & 0x3) >> 8;
    FUN_800dcfb8(2, cam._2c, z, 0);
    cam.vec_60.setX((ratan2(s1, s2) & 0xfff) << 8);
    cam.vec_60.setY(0);
    cam.vec_60.setZ(SquareRoot0(s2 * s2 + s1 * s1) << 8);
    cam._5c = cam.vec_60.getZ() * 2 / (a3 + a4);
    cam._6c = a3;
    cam.vec_74.set(x, y, z);
    cam._11c |= 0x2;
    cam._121 = 19;

    if(cam._5c > 0) {
      cam._30 = FUN_800dcfb8(2, cam._2c, z, 0) / cam._5c;
      cam._70 = (a4 - a3) / cam._5c;
    } else {
      cam._30 = -1;
      cam._70 = -1;
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
    cam._5c = a3;
    cam.vec_60.setX((ratan2(s2, s4) & 0xfff) << 8);
    cam.vec_60.setY((ratan2(s5, SquareRoot0(s3 * s3 + s0 * s0) * 2) & 0xfff) << 8);
    cam.vec_60.setZ(SquareRoot0(s3 * s3 + v0 * v0 + s0 * s0) << 9);
    final IntRef sp0x18 = new IntRef();
    final IntRef sp0x1c = new IntRef();
    FUN_800dcebc(a4, a5, cam.vec_60.getZ(), cam._5c, sp0x18, sp0x1c);
    cam._30 = sp0x18.get();
    cam._40 = (sp0x1c.get() - sp0x18.get()) / cam._5c;
    cam.vec_74.setX(a0);
    cam.vec_74.setY(a1);
    cam.vec_74.setZ(a2);
    cam._11c |= 0x2;
    cam._121 = 16;
  }

  @Method(0x800d8424L)
  public static void FUN_800d8424(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;

    cam._38 = FUN_800dc384(1, 1, 0, 0) << 8;
    cam._44 = FUN_800dc384(1, 1, 1, 0) << 8;
    cam._2c = FUN_800dc384(1, 1, 2, 0) << 8;
    final int s2 = FUN_800dcfb8(0, cam._38, a0, a6 & 3) >> 8;
    final int s1 = FUN_800dcfb8(1, cam._44, a1, a6 >> 2 & 3) >> 8;
    FUN_800dcfb8(2, cam._2c, a2, 0);
    cam._5c = a3;
    cam.vec_60.setX((ratan2(s1, s2) & 0xfff) << 8);
    cam.vec_60.setY(0);
    cam.vec_60.setZ(SquareRoot0(s2 * s2 + s1 * s1) << 8);
    final IntRef sp0x18 = new IntRef();
    final IntRef sp0x1c = new IntRef();
    FUN_800dcebc(a4, a5, cam.vec_60.getZ(), a3, sp0x18, sp0x1c);
    cam._6c = sp0x18.get();
    cam._70 = (sp0x1c.get() - sp0x18.get()) / cam._5c;
    cam.vec_74.set(a0, a1, a2);
    cam._11c |= 0x2;
    cam._121 = 17;
    cam._30 = FUN_800dcfb8(2, cam._2c, a2, 0) / cam._5c;
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
    cam._5c = a3;
    final IntRef sp0x18 = new IntRef();
    final IntRef sp0x1c = new IntRef();
    FUN_800dcebc(a4, a5, cam.vec_60.getZ(), a3, sp0x18, sp0x1c);
    cam._30 = sp0x18.get();
    cam._40 = (sp0x1c.get() - sp0x18.get()) / cam._5c;
    cam.vec_74.setX(a0);
    cam.vec_74.setY(a1);
    cam.vec_74.setZ(a2);
    cam._11c |= 0x2;
    cam._121 = 18;
  }

  @Method(0x800d8808L)
  public static void FUN_800d8808(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;

    cam._38 = FUN_800dc384(1, 3, 0, 0) << 8;
    cam._44 = FUN_800dc384(1, 3, 1, 0) << 8;
    cam._2c = FUN_800dc384(1, 3, 2, 0) << 8;
    final int s2 = FUN_800dcfb8(0, cam._38, a0, a6 & 3) >> 8;
    final int s1 = FUN_800dcfb8(1, cam._44, a1, a6 >> 2 & 3) >> 8;
    FUN_800dcfb8(2, cam._2c, a2, 0);
    cam.vec_60.setX((ratan2(s1, s2) & 0xfff) << 8);
    cam.vec_60.setY(0);
    cam.vec_60.setZ(SquareRoot0(s2 * s2 + s1 * s1) << 8);
    cam._5c = a3;
    final IntRef sp0x18 = new IntRef();
    final IntRef sp0x1c = new IntRef();
    FUN_800dcebc(a4, a5, cam.vec_60.getZ(), a3, sp0x18, sp0x1c);
    cam._30 = FUN_800dcfb8(2, cam._2c, a2, 0) / cam._5c;
    cam._6c = sp0x18.get();
    cam._70 = (sp0x1c.get() - sp0x18.get()) / cam._5c;
    cam.vec_74.set(a0, a1, a2);
    cam._11c |= 0x2;
    cam._121 = 19;
  }

  @Method(0x800d89f8L)
  public static void FUN_800d89f8(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.bobjIndex_80 = scriptIndex;
    cam.vec_20.setX(FUN_800dc384(1, 6, 0, scriptIndex) << 8);
    cam.vec_20.setY(FUN_800dc384(1, 6, 1, scriptIndex) << 8);
    cam.vec_20.setZ(FUN_800dc384(1, 6, 2, scriptIndex) << 8);
    final int s4 = a0 - cam.vec_20.getX() >> 8;
    final int s5 = a1 - cam.vec_20.getY() >> 8;
    final int s3 = a2 - cam.vec_20.getZ() >> 8;
    final int s1 = s4 / 2;
    final int v1 = s5 / 2;
    final int s0 = s3 / 2;
    cam._5c = a3;
    cam.vec_60.setX((ratan2(s3, s4) & 0xfff) << 8);
    cam.vec_60.setY((ratan2(s5, SquareRoot0(s1 * s1 + s0 * s0) * 2) & 0xfff) << 8);
    cam.vec_60.setZ(SquareRoot0(s1 * s1 + v1 * v1 + s0 * s0) << 9);
    final IntRef sp0x18 = new IntRef();
    final IntRef sp0x1c = new IntRef();
    FUN_800dcebc(a4, a5, cam.vec_60.getZ(), a3, sp0x18, sp0x1c);
    cam.vec_74.setX(a0);
    cam.vec_74.setY(a1);
    cam.vec_74.setZ(a2);
    cam._30 = sp0x18.get();
    cam._40 = (sp0x1c.get() - sp0x18.get()) / cam._5c;
    cam._11c |= 0x2;
    cam._121 = 22;
  }

  @Method(0x800d8bf4L)
  public static void FUN_800d8bf4(final int a0, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;

    cam.bobjIndex_80 = scriptIndex;
    cam._38 = FUN_800dc384(1, 7, 0, scriptIndex) << 8;
    cam._44 = FUN_800dc384(1, 7, 1, scriptIndex) << 8;
    cam._2c = FUN_800dc384(1, 7, 2, scriptIndex) << 8;
    final int s2 = FUN_800dcfb8(0, cam._38, a0, a6 & 3) >> 8;
    final int s0 = FUN_800dcfb8(1, cam._44, a1, a6 >> 2 & 3) >> 8;
    FUN_800dcfb8(2, cam._2c, a2, 0);
    cam.vec_60.setX((ratan2(s0, s2) & 0xfff) << 8);
    cam.vec_60.setY(0);
    cam.vec_60.setZ(SquareRoot0(s2 * s2 + s0 * s0) << 8);
    cam._5c = a3;
    final IntRef sp0x18 = new IntRef();
    final IntRef sp0x1c = new IntRef();
    FUN_800dcebc(a4, a5, cam.vec_60.getZ(), a3, sp0x18, sp0x1c);
    cam._30 = FUN_800dcfb8(2, cam._2c, a2, 0) / cam._5c;
    cam.vec_74.set(a0, a1, a2);
    cam._6c = sp0x18.get();
    cam._70 = (sp0x1c.get() - sp0x18.get()) / cam._5c;
    cam._11c |= 0x2;
    cam._121 = 23;
  }

  @Method(0x800d8decL)
  public static FlowControl FUN_800d8dec(final RunningScript<?> script) {
    final int s3 = script.params_20[0].get();
    final int s0 = script.params_20[1].get();
    final int s1 = script.params_20[2].get();
    final int s4 = script.params_20[3].get();

    final BattleCamera cam = camera_800c67f0;
    cam._100 = getProjectionPlaneDistance() << 16;
    cam._104 = s0 << 16;
    cam._118 = 1;

    if(s0 < getProjectionPlaneDistance()) {
      //LAB_800d8e64
      cam._114 = 1;
    } else {
      cam._114 = 0;
    }

    //LAB_800d8e68
    final int a2 = Math.abs(s0 - getProjectionPlaneDistance()) << 16;
    cam._108 = s1;
    if(s3 == 0) {
      cam._10c = a2 / s1;
      cam._110 = 0;
    } else {
      //LAB_800d8ea0
      final IntRef sp0x18 = new IntRef();
      final IntRef sp0x1c = new IntRef();
      FUN_800dcebc(s3 - 1, s4 << 8, a2, s1, sp0x18, sp0x1c);
      cam._10c = sp0x18.get();
      cam._110 = (sp0x1c.get() - sp0x18.get()) / s1;
    }

    //LAB_800d8eec
    return FlowControl.CONTINUE;
  }

  @Method(0x800d8f10L)
  public static void FUN_800d8f10() {
    final BattleCamera cam = camera_800c67f0;

    if(cam._11c != 0) {
      if((cam._11c & 0x1) != 0) {
        _800facbc.get(cam._120).deref().run();
      }

      //LAB_800d8f80
      if((cam._11c & 0x2) != 0) {
        _800fad1c.get(cam._121).deref().run();
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

    if(cam._118 != 0 && cam._108 == 0) {
      setProjectionPlaneDistance(cam._100 >> 16);
      cam._118 = 0;
    }

    //LAB_800d9028
    if(cam._118 != 0 && cam._108 != 0) {
      if(cam._114 == 0) {
        cam._100 += cam._10c;
      } else {
        //LAB_800d906c
        cam._100 -= cam._10c;
      }

      //LAB_800d907c
      cam._10c += cam._110;
      setProjectionPlaneDistance(cam._100 >> 16);

      cam._108--;
      if(cam._108 == 0) {
        cam._118 = 0;
      }
    }

    //LAB_800d90b8
  }

  @Method(0x800d90c8L)
  public static void FUN_800d90c8() {
    final BattleCamera cam = camera_800c67f0;
    cam.vec_94.x.add(cam._b0);
    cam.vec_94.y.add(cam._bc);
    cam.vec_94.z.add(cam._c8);
    setViewpoint(cam.vec_94.getX() >> 8, cam.vec_94.getY() >> 8, cam.vec_94.getZ() >> 8);

    cam._d0--;
    if(cam._d0 <= 0) {
      cam._11c &= 0xffff_fffe;
      cam._122 = 0;
    }

    //LAB_800d9144
  }

  @Method(0x800d9154L)
  public static void FUN_800d9154() {
    final BattleCamera cam = camera_800c67f0;
    cam._ac += cam._b0;
    cam._b8 += cam._bc;
    cam._a0 += cam._c8;
    final IntRef sp0x18 = new IntRef().set(cam._ac >> 8);
    final IntRef sp0x1c = new IntRef().set(cam._b8 >> 8);
    final IntRef sp0x20 = new IntRef().set(cam._a0 >> 8);
    FUN_800dcc94(0, 0, 0, sp0x18, sp0x1c, sp0x20);
    setViewpoint(sp0x18.get(), sp0x1c.get(), sp0x20.get());

    cam._d0--;
    if(cam._d0 <= 0) {
      cam._11c &= 0xffff_fffe;
      cam._122 = 0;
    }

    //LAB_800d9210
  }

  @Method(0x800d9220L)
  public static void FUN_800d9220() {
    final BattleCamera cam = camera_800c67f0;
    cam.vec_94.x.add(cam._b0);
    cam.vec_94.y.add(cam._bc);
    cam.vec_94.z.add(cam._c8);

    setViewpoint(
      cam.rview2_00.refpoint_0c.getX() + (cam.vec_94.getX() >> 8),
      cam.rview2_00.refpoint_0c.getY() + (cam.vec_94.getY() >> 8),
      cam.rview2_00.refpoint_0c.getZ() + (cam.vec_94.getZ() >> 8)
    );

    cam._d0--;
    if(cam._d0 <= 0) {
      cam._120 = 4;
      cam._122 = 0;
    }

    //LAB_800d92ac
  }

  @Method(0x800d92bcL)
  public static void FUN_800d92bc() {
    final BattleCamera cam = camera_800c67f0;
    cam._a0 += cam._c8;
    cam._ac += cam._b0;
    cam._b8 += cam._bc;

    final IntRef refX = new IntRef().set(cam._ac >> 8);
    final IntRef refY = new IntRef().set(cam._b8 >> 8);
    final IntRef refZ = new IntRef().set(cam._a0 >> 8);
    FUN_800dcc94(cam.rview2_00.refpoint_0c.getX(), cam.rview2_00.refpoint_0c.getY(), cam.rview2_00.refpoint_0c.getZ(), refX, refY, refZ);
    setViewpoint(refX.get(), refY.get(), refZ.get());

    cam._d0--;
    if(cam._d0 <= 0) {
      cam._120 = 5;
      cam._122 = 0;
    }

    //LAB_800d9370
  }

  @Method(0x800d9380L)
  public static void FUN_800d9380() {
    final BattleCamera cam = camera_800c67f0;

    cam.vec_94.x.add(cam._b0);
    cam.vec_94.y.add(cam._bc);
    cam.vec_94.z.add(cam._c8);

    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[cam.bobjIndex_f4].innerStruct_00;

    setViewpoint(
      bobj.model_148.coord2_14.coord.transfer.getX() + (cam.vec_94.getX() >> 8),
      bobj.model_148.coord2_14.coord.transfer.getY() + (cam.vec_94.getY() >> 8),
      bobj.model_148.coord2_14.coord.transfer.getZ() + (cam.vec_94.getZ() >> 8)
    );

    cam._d0--;
    if(cam._d0 <= 0) {
      cam._120 = 6;
      cam._122 = 0;
    }

    //LAB_800d9428
  }

  @Method(0x800d9438L)
  public static void FUN_800d9438() {
    final BattleCamera cam = camera_800c67f0;
    cam._ac += cam._b0;
    cam._b8 += cam._bc;
    cam._a0 += cam._c8;
    final IntRef sp0x18 = new IntRef().set(cam._ac >> 8);
    final IntRef sp0x1c = new IntRef().set(cam._b8 >> 8);
    final IntRef sp0x20 = new IntRef().set(cam._a0 >> 8);
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[cam.bobjIndex_f4].innerStruct_00;
    FUN_800dcc94(bobj.model_148.coord2_14.coord.transfer.getX(), bobj.model_148.coord2_14.coord.transfer.getY(), bobj.model_148.coord2_14.coord.transfer.getZ(), sp0x18, sp0x1c, sp0x20);
    setViewpoint(sp0x18.get(), sp0x1c.get(), sp0x20.get());

    cam._d0--;
    if(cam._d0 <= 0) {
      cam._120 = 7;
      cam._122 = 0;
    }

    //LAB_800d9508
  }

  @Method(0x800d9518L)
  public static void FUN_800d9518() {
    final BattleCamera cam = camera_800c67f0;
    _800fab98.setX((short)(cam._d4 >> 8));
    _800fab98.setY((short)(cam._d8 >> 8));
    _800fab98.setZ((short)0);
    RotMatrix_8003faf0(_800fab98, _800c6798);
    SetRotMatrix(_800c6798);
    SetTransMatrix(_800c6798);
    cam._a4 += cam._b4;
    cam._dc -= cam._a4;
    _800faba0.setX((short)0);
    _800faba0.setY((short)0);
    _800faba0.setZ((short)(cam._dc >> 8));
    RotTrans(_800faba0, _800faba8, _800c67b8);
    cam.vec_94.setX(cam._e8 - (_800faba8.getZ() << 8));
    cam.vec_94.setY(cam._ec - (_800faba8.getX() << 8));
    cam.vec_94.setZ(cam._f0 + (_800faba8.getY() << 8));

    setViewpoint(cam.vec_94.getX() >> 8, cam.vec_94.getY() >> 8, cam.vec_94.getZ() >> 8);

    cam._d0--;
    if(cam._d0 <= 0) {
      cam._11c &= 0xffff_fffe;
      cam._122 = 0;
    }

    //LAB_800d9638
  }

  @Method(0x800d9650L)
  public static void FUN_800d9650() {
    final BattleCamera s3 = camera_800c67f0;
    final IntRef sp0x18 = new IntRef().set(s3._d4 >> 8);
    final IntRef sp0x1c = new IntRef().set(s3._d8 >> 8);
    final IntRef sp0x20 = new IntRef().set(s3._dc >> 8);
    FUN_800dcc94(0, 0, 0, sp0x18, sp0x1c, sp0x20);
    sp0x18.add(s3._e8 >> 8);
    sp0x1c.set(sp0x20.get()).add(s3._ec >> 8);
    s3._a0 += s3._a4;
    sp0x20.set(s3._a0 >> 8);
    FUN_800dcc94(0, 0, 0, sp0x18, sp0x1c, sp0x20);
    setViewpoint(sp0x18.get(), sp0x1c.get(), sp0x20.get());
    s3._e0 += s3._e4;
    s3._dc -= s3._e0;

    s3._d0--;
    if(s3._d0 <= 0) {
      s3._11c &= 0xffff_fffe;
      s3._122 = 0;
    }

    //LAB_800d976c
  }

  @Method(0x800d9788L)
  public static void FUN_800d9788() {
    final BattleCamera cam = camera_800c67f0;
    _800fab98.setX((short)(cam._d4 >> 8));
    _800fab98.setY((short)(cam._d8 >> 8));
    _800fab98.setZ((short)0);
    RotMatrix_8003faf0(_800fab98, _800c6798);
    SetRotMatrix(_800c6798);
    SetTransMatrix(_800c6798);
    _800faba0.setX((short)0);
    _800faba0.setY((short)0);
    cam._a4 += cam._b4;
    cam._dc -= cam._a4;
    _800faba0.setZ((short)(cam._dc >> 8));
    RotTrans(_800faba0, _800faba8, _800c67b8);

    cam.vec_94.setX(cam._e8 - (_800faba8.getZ() << 8));
    cam.vec_94.setY(cam._ec - (_800faba8.getX() << 8));
    cam.vec_94.setZ(cam._f0 + (_800faba8.getY() << 8));

    setViewpoint(
      cam.rview2_00.refpoint_0c.getX() + (cam.vec_94.getX() >> 8),
      cam.rview2_00.refpoint_0c.getY() + (cam.vec_94.getY() >> 8),
      cam.rview2_00.refpoint_0c.getZ() + (cam.vec_94.getZ() >> 8)
    );

    cam._d0--;
    if(cam._d0 <= 0) {
      cam._120 = 4;
      cam._122 = 0;
    }

    //LAB_800d98b8
  }

  @Method(0x800d98d0L)
  public static void FUN_800d98d0() {
    final BattleCamera cam = camera_800c67f0;
    final IntRef refX = new IntRef().set(cam._d4 >> 8);
    final IntRef refY = new IntRef().set(cam._d8 >> 8);
    final IntRef refZ = new IntRef().set(cam._dc >> 8);
    FUN_800dcc94(0, 0, 0, refX, refY, refZ);
    cam._a0 += cam._a4;
    refX.add(cam._e8 >> 8);
    refY.set(refZ.get()).add(cam._ec >> 8);
    refZ.set(cam._a0 >> 8);
    FUN_800dcc94(0, 0, 0, refX, refY, refZ);
    setViewpoint(cam.rview2_00.refpoint_0c.getX() + refX.get(), cam.rview2_00.refpoint_0c.getY() + refY.get(), cam.rview2_00.refpoint_0c.getZ() + refZ.get());
    cam._e0 += cam._e4;
    cam._dc -= cam._e0;

    cam._d0--;
    if(cam._d0 <= 0) {
      cam._ac = FUN_800dc384(0, 5, 0, 0) << 8;
      cam._b8 = FUN_800dc384(0, 5, 1, 0) << 8;
      cam._a0 = FUN_800dc384(0, 5, 2, 0) << 8;
      cam._120 = 5;
      cam._122 = 0;
    }

    //LAB_800d9a4c
  }

  @Method(0x800d9a68L)
  public static void FUN_800d9a68() {
    final BattleCamera cam = camera_800c67f0;
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[cam.bobjIndex_f4].innerStruct_00;
    _800fab98.setX((short)(cam._d4 >> 8));
    _800fab98.setY((short)(cam._d8 >> 8));
    _800fab98.setZ((short)0);
    RotMatrix_8003faf0(_800fab98, _800c6798);
    SetRotMatrix(_800c6798);
    SetTransMatrix(_800c6798);
    _800faba0.setX((short)0);
    _800faba0.setY((short)0);
    cam._a4 += cam._b4;
    cam._dc -= cam._a4;
    _800faba0.setZ((short)(cam._dc >> 8));
    RotTrans(_800faba0, _800faba8, _800c67b8);
    cam.vec_94.setX(cam._e8 - (_800faba8.getZ() << 8));
    cam.vec_94.setY(cam._ec - (_800faba8.getX() << 8));
    cam.vec_94.setZ(cam._f0 + (_800faba8.getY() << 8));
    setViewpoint(bobj.model_148.coord2_14.coord.transfer.getX() + (cam.vec_94.getX() >> 8), bobj.model_148.coord2_14.coord.transfer.getY() + (cam.vec_94.getY() >> 8), bobj.model_148.coord2_14.coord.transfer.getZ() + (cam.vec_94.getZ() >> 8));

    cam._d0--;
    if(cam._d0 <= 0) {
      cam._120 = 6;
      cam._122 = 0;
    }

    //LAB_800d9bb8
  }

  /** TODO I might have messed this up */
  @Method(0x800d9bd4L)
  public static void FUN_800d9bd4() {
    final BattleCamera cam = camera_800c67f0;
    final IntRef refX = new IntRef().set(cam._d4 >> 8);
    final IntRef refY = new IntRef().set(cam._d8 >> 8);
    final IntRef refZ = new IntRef().set(cam._dc >> 8);
    FUN_800dcc94(0, 0, 0, refX, refY, refZ);
    cam._a0 += cam._a4;
    refX.add(cam._e8 >> 8);
    refY.set(refZ.get()).add(cam._ec >> 8);
    refZ.set(cam._a0 >> 8);
    FUN_800dcc94(0, 0, 0, refX, refY, refZ);
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[cam.bobjIndex_f4].innerStruct_00;
    setViewpoint(bobj.model_148.coord2_14.coord.transfer.getX() + refX.get(), bobj.model_148.coord2_14.coord.transfer.getY() + refY.get(), bobj.model_148.coord2_14.coord.transfer.getZ() + refZ.get());
    cam._e0 += cam._e4;
    cam._dc -= cam._e0;

    cam._d0--;
    if(cam._d0 <= 0) {
      refX.set(cam.rview2_00.viewpoint_00.getX());
      refY.set(cam.rview2_00.viewpoint_00.getY());
      refZ.set(cam.rview2_00.viewpoint_00.getZ());
      FUN_800dcd9c(bobj.model_148.coord2_14.coord.transfer.getX(), bobj.model_148.coord2_14.coord.transfer.getY(), bobj.model_148.coord2_14.coord.transfer.getZ(), refX, refY, refZ);
      cam._ac = refX.get() << 8;
      cam._b8 = refY.get() << 8;
      cam._a0 = refZ.get() << 8;
      cam._120 = 7;
      cam._122 = 0;
    }

    //LAB_800d9d7c
  }

  @Method(0x800d9da0L)
  public static void FUN_800d9da0() {
    final BattleCamera cam = camera_800c67f0;
    cam.vec_20.x.add(cam._3c);
    cam.vec_20.y.add(cam._48);
    cam.vec_20.z.add(cam._54);
    setRefpoint(cam.vec_20.getX() >> 8, cam.vec_20.getY() >> 8, cam.vec_20.getZ() >> 8);

    cam._5c--;
    if(cam._5c <= 0) {
      cam._11c &= 0xffff_fffd;
      cam._123 = 0;
    }

    //LAB_800d9e1c
  }

  @Method(0x800d9e2cL)
  public static void FUN_800d9e2c() {
    final BattleCamera cam = camera_800c67f0;

    cam._38 += cam._3c;
    cam._44 += cam._48;
    cam._2c += cam._54;

    final IntRef sp0x18 = new IntRef().set(cam._38 >> 8);
    final IntRef sp0x1c = new IntRef().set(cam._44 >> 8);
    final IntRef sp0x20 = new IntRef().set(cam._2c >> 8);
    FUN_800dcc94(0, 0, 0, sp0x18, sp0x1c, sp0x20);
    setRefpoint(sp0x18.get(), sp0x1c.get(), sp0x20.get());

    cam._5c--;
    if(cam._5c <= 0) {
      cam._11c &= 0xffff_fffd;
      cam._123 = 0;
    }

    //LAB_800d9ee8
  }

  @Method(0x800d9ef8L)
  public static void FUN_800d9ef8() {
    final BattleCamera cam = camera_800c67f0;

    cam.vec_20.x.add(cam._3c);
    cam.vec_20.y.add(cam._48);
    cam.vec_20.z.add(cam._54);

    setRefpoint(
      cam.rview2_00.viewpoint_00.getX() + (cam.vec_20.getX() >> 8),
      cam.rview2_00.viewpoint_00.getY() + (cam.vec_20.getY() >> 8),
      cam.rview2_00.viewpoint_00.getZ() + (cam.vec_20.getZ() >> 8)
    );

    cam._5c--;
    if(cam._5c <= 0) {
      cam._121 = 2;
      cam._123 = 0;
    }

    //LAB_800d9f84
  }

  @Method(0x800d9f94L)
  public static void FUN_800d9f94() {
    final BattleCamera cam = camera_800c67f0;
    cam._38 += cam._3c;
    cam._2c += cam._54;
    cam._44 += cam._48;
    final IntRef sp0x18 = new IntRef().set(cam._38 >> 8);
    final IntRef sp0x1c = new IntRef().set(cam._44 >> 8);
    final IntRef sp0x20 = new IntRef().set(cam._2c >> 8);
    FUN_800dcc94(cam.rview2_00.viewpoint_00.getX(), cam.rview2_00.viewpoint_00.getY(), cam.rview2_00.viewpoint_00.getZ(), sp0x18, sp0x1c, sp0x20);
    setRefpoint(sp0x18.get(), sp0x1c.get(), sp0x20.get());

    cam._5c--;
    if(cam._5c <= 0) {
      cam._121 = 3;
      cam._123 = 0;
    }

    //LAB_800da048
  }

  @Method(0x800da058L)
  public static void FUN_800da058() {
    final BattleCamera cam = camera_800c67f0;

    cam.vec_20.x.add(cam._3c);
    cam.vec_20.y.add(cam._48);
    cam.vec_20.z.add(cam._54);

    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[cam.bobjIndex_80].innerStruct_00;
    setRefpoint(bobj.model_148.coord2_14.coord.transfer.getX() + (cam.vec_20.getX() >> 8), bobj.model_148.coord2_14.coord.transfer.getY() + (cam.vec_20.getY() >> 8), bobj.model_148.coord2_14.coord.transfer.getY() + (cam.vec_20.getZ() >> 8));

    cam._5c--;
    if(cam._5c <= 0) {
      cam._121 = 6;
      cam._123 = 0;
    }

    //LAB_800da100
  }

  @Method(0x800da110L)
  public static void FUN_800da110() {
    final BattleCamera cam = camera_800c67f0;

    cam._38 += cam._3c;
    cam._44 += cam._48;
    cam._2c += cam._54;

    final IntRef sp0x18 = new IntRef().set(cam._38 >> 8);
    final IntRef sp0x1c = new IntRef().set(cam._44 >> 8);
    final IntRef sp0x20 = new IntRef().set(cam._2c >> 8);
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[cam.bobjIndex_80].innerStruct_00;
    FUN_800dcc94(bobj.model_148.coord2_14.coord.transfer.getX(), bobj.model_148.coord2_14.coord.transfer.getX(), bobj.model_148.coord2_14.coord.transfer.getX(), sp0x18, sp0x1c, sp0x20);
    setRefpoint(sp0x18.get(), sp0x1c.get(), sp0x20.get());

    cam._5c--;
    if(cam._5c <= 0) {
      cam._121 = 7;
      cam._123 = 0;
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
    cam._30 += cam._40;
    cam.vec_60.z.sub(cam._30);
    _800faba0.setX((short)0);
    _800faba0.setY((short)0);
    _800faba0.setZ((short)(cam.vec_60.getZ() >> 8));
    RotTrans(_800faba0, _800faba8, _800c67b8);
    cam.vec_20.setX(cam.vec_74.getX() - (_800faba8.getZ() << 8));
    cam.vec_20.setY(cam.vec_74.getY() - (_800faba8.getX() << 8));
    cam.vec_20.setZ(cam.vec_74.getZ() + (_800faba8.getY() << 8));
    setRefpoint(cam.vec_20.getX() >> 8, cam.vec_20.getY() >> 8, cam.vec_20.getZ() >> 8);

    cam._5c--;
    if(cam._5c <= 0) {
      cam._11c &= 0xffff_fffd;
      cam._123 = 0;
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

    cam._2c += cam._30;
    sp0x18.add(cam.vec_74.getX() >> 8);
    sp0x1c.set(sp0x20.get()).add(cam.vec_74.getY() >> 8);
    sp0x20.set(cam._2c >> 8);
    FUN_800dcc94(0, 0, 0, sp0x18, sp0x1c, sp0x20);
    setRefpoint(sp0x18.get(), sp0x1c.get(), sp0x20.get());

    cam._6c += cam._70;
    cam.vec_60.z.sub(cam._6c);
    cam._5c--;
    if(cam._5c <= 0) {
      cam._11c &= 0xffff_fffd;
      cam._123 = 0;
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

    cam._30 += cam._40;
    cam.vec_60.z.sub(cam._30);
    _800faba0.setX((short)0);
    _800faba0.setY((short)0);
    _800faba0.setZ((short)(cam.vec_60.getZ() >> 8));
    RotTrans(_800faba0, _800faba8, _800c67b8);

    cam.vec_20.setX(cam.vec_74.getX() - (_800faba8.getZ() << 8));
    cam.vec_20.setY(cam.vec_74.getY() - (_800faba8.getX() << 8));
    cam.vec_20.setZ(cam.vec_74.getZ() + (_800faba8.getY() << 8));

    setRefpoint(
      cam.rview2_00.viewpoint_00.getX() + (cam.vec_20.getX() >> 8),
      cam.rview2_00.viewpoint_00.getY() + (cam.vec_20.getY() >> 8),
      cam.rview2_00.viewpoint_00.getZ() + (cam.vec_20.getZ() >> 8)
    );

    cam._5c--;
    if(cam._5c <= 0) {
      cam._121 = 2;
      cam._123 = 0;
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

    cam._2c += cam._30;
    sp0x18.add(cam.vec_74.getX() >> 8);
    sp0x1c.set(sp0x20.get()).add(cam.vec_74.getY() >> 8);
    sp0x20.set(cam._2c >> 8);
    FUN_800dcc94(0, 0, 0, sp0x18, sp0x1c, sp0x20);
    setRefpoint(cam.rview2_00.viewpoint_00.getX() + sp0x18.get(), cam.rview2_00.viewpoint_00.getY() + sp0x1c.get(), cam.rview2_00.viewpoint_00.getZ() + sp0x20.get());

    cam._6c += cam._70;
    cam.vec_60.z.sub(cam._6c);

    cam._5c--;
    if(cam._5c <= 0) {
      cam._38 = FUN_800dc384(1, 3, 0, 0) << 8;
      cam._44 = FUN_800dc384(1, 3, 1, 0) << 8;
      cam._2c = FUN_800dc384(1, 3, 2, 0) << 8;
      cam._121 = 3;
      cam._123 = 0;
    }

    //LAB_800da730
  }

  @Method(0x800da750L)
  public static void FUN_800da750() {
    final BattleCamera cam = camera_800c67f0;
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[cam.bobjIndex_80].innerStruct_00;

    cam._30 += cam._40;
    cam.vec_60.z.sub(cam._30);

    _800fab98.setX((short)(cam.vec_60.getX() >> 8));
    _800fab98.setY((short)(cam.vec_60.getY() >> 8));
    _800fab98.setZ((short)0);

    RotMatrix_8003faf0(_800fab98, _800c6798);
    SetRotMatrix(_800c6798);
    SetTransMatrix(_800c6798);

    _800faba0.setX((short)0);
    _800faba0.setY((short)0);
    _800faba0.setZ((short)(cam.vec_60.getZ() >> 8));

    RotTrans(_800faba0, _800faba8, _800c67b8);

    cam.vec_20.setX(cam.vec_74.getX() - (_800faba8.getZ() << 8));
    cam.vec_20.setY(cam.vec_74.getY() - (_800faba8.getX() << 8));
    cam.vec_20.setZ(cam.vec_74.getZ() + (_800faba8.getY() << 8));

    setRefpoint(
      bobj.model_148.coord2_14.coord.transfer.getX() + (cam.vec_20.getX() >> 8),
      bobj.model_148.coord2_14.coord.transfer.getY() + (cam.vec_20.getY() >> 8),
      bobj.model_148.coord2_14.coord.transfer.getZ() + (cam.vec_20.getZ() >> 8)
    );

    cam._5c--;
    if(cam._5c <= 0) {
      cam._121 = 6;
      cam._123 = 0;
    }

    //LAB_800da8a0
  }

  @Method(0x800da8bcL)
  public static void FUN_800da8bc() {
    final BattleCamera cam = camera_800c67f0;
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0[cam.bobjIndex_80];

    final IntRef sp0x18 = new IntRef().set(cam.vec_60.getX() >> 8);
    final IntRef sp0x1c = new IntRef().set(cam.vec_60.getY() >> 8);
    final IntRef sp0x20 = new IntRef().set(cam.vec_60.getZ() >> 8);
    FUN_800dcc94(0, 0, 0, sp0x18, sp0x1c, sp0x20);

    cam._2c += cam._30;
    sp0x18.add(cam.vec_74.getX() >> 8);
    sp0x1c.set(sp0x20.get()).add(cam.vec_74.getY() >> 8);
    sp0x20.set(cam._2c >> 8);
    FUN_800dcc94(0, 0, 0, sp0x18, sp0x1c, sp0x20);

    final BattleObject27c bobj = (BattleObject27c)state.innerStruct_00;
    setRefpoint(bobj.model_148.coord2_14.coord.transfer.getX() + sp0x18.get(), bobj.model_148.coord2_14.coord.transfer.getY() + sp0x1c.get(), bobj.model_148.coord2_14.coord.transfer.getZ() + sp0x20.get());

    cam._6c += cam._70;
    cam.vec_60.z.sub(cam._6c);

    cam._5c--;
    if(cam._5c <= 0) {
      sp0x18.set(cam.rview2_00.refpoint_0c.getX());
      sp0x1c.set(cam.rview2_00.refpoint_0c.getY());
      sp0x20.set(cam.rview2_00.refpoint_0c.getZ());
      FUN_800dcd9c(bobj.model_148.coord2_14.coord.transfer.getX(), bobj.model_148.coord2_14.coord.transfer.getY(), bobj.model_148.coord2_14.coord.transfer.getZ(), sp0x18, sp0x1c, sp0x20);
      cam._38 = sp0x18.get() << 8;
      cam._44 = sp0x1c.get() << 8;
      cam._2c = sp0x20.get() << 8;
      cam._121 = 7;
      cam._123 = 0;
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
      final int a0 = tickCount_800bb0fc.get() & 0x3;
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
  public static FlowControl FUN_800dabcc(final RunningScript<?> script) {
    FUN_800dabec();
    return FlowControl.CONTINUE;
  }

  @Method(0x800dabecL)
  public static void FUN_800dabec() {
    final BattleCamera cam = camera_800c67f0;
    cam.svec_8c.setX((short)0);
    cam.svec_8c.setY((short)0);
    cam.svec_8c.setZ((short)0);
    cam._108 = 0;
    cam._118 = 0;
    cam._11c = 0;
    cam._120 = 0;
    cam._121 = 0;
    cam._122 = 0;
    cam._123 = 0;
  }

  @Method(0x800dac20L)
  public static FlowControl FUN_800dac20(final RunningScript<?> script) {
    FUN_800dac70(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get(), script.params_20[4].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800dac70L)
  public static void FUN_800dac70(final int index, final int x, final int y, final int z, final int scriptIndex) {
    _800fabbc.get(index).deref().run(x, y, z, scriptIndex);
    camera_800c67f0.callbackIndex_fc = index;
  }

  @Method(0x800dacc4L)
  public static void FUN_800dacc4(final int x, final int y, final int z, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.vec_94.setX(x);
    cam.vec_94.setY(y);
    cam.vec_94.setZ(z);
    setViewpoint(x >> 8, y >> 8, z >> 8);
    cam._11c |= 0x1;
    cam._120 = 0;
  }

  @Method(0x800dad14L)
  public static void FUN_800dad14(final int x, final int y, final int z, final int scriptIndex) {
    final IntRef refX = new IntRef().set(x);
    final IntRef refY = new IntRef().set(y);
    final IntRef refZ = new IntRef().set(z);
    final BattleCamera cam = camera_800c67f0;
    cam._a0 = z;
    cam._ac = x;
    cam._b8 = y;
    FUN_800dcc94(0, 0, 0, refX, refY, refZ);
    setViewpoint(refX.get() >> 8, refY.get() >> 8, refZ.get() >> 8);

    cam._11c |= 0x1;
    cam._120 = 1;
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

    cam._11c |= 0x1;
    cam._120 = 4;
  }

  @Method(0x800dae3cL)
  public static void FUN_800dae3c(final int x, final int y, final int z, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam._a0 = z;
    cam._ac = x;
    cam._b8 = y;
    final IntRef refX = new IntRef().set(cam._ac >> 8);
    final IntRef refY = new IntRef().set(cam._b8 >> 8);
    final IntRef refZ = new IntRef().set(cam._a0 >> 8);
    FUN_800dcc94(cam.rview2_00.refpoint_0c.getX(), cam.rview2_00.refpoint_0c.getY(), cam.rview2_00.refpoint_0c.getZ(), refX, refY, refZ);
    setViewpoint(refX.get(), refY.get(), refZ.get());
    cam._11c |= 0x1;
    cam._120 = 5;
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

    cam.bobjIndex_f4 = scriptIndex;
    cam._11c |= 0x1;
    cam._120 = 6;
  }

  @Method(0x800daf6cL)
  public static void FUN_800daf6c(final int x, final int y, final int z, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam._a0 = z;
    cam._ac = x;
    cam._b8 = y;
    final IntRef refX = new IntRef().set(x >> 8);
    final IntRef refY = new IntRef().set(y >> 8);
    final IntRef refZ = new IntRef().set(z >> 8);
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00;
    FUN_800dcc94(bobj.model_148.coord2_14.coord.transfer.getX(), bobj.model_148.coord2_14.coord.transfer.getY(), bobj.model_148.coord2_14.coord.transfer.getZ(), refX, refY, refZ);
    setViewpoint(refX.get(), refY.get(), refZ.get());
    cam.bobjIndex_f4 = scriptIndex;
    cam._11c |= 0x1;
    cam._120 = 7;
  }

  @Method(0x800db034L)
  public static FlowControl FUN_800db034(final RunningScript<?> script) {
    FUN_800db084(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get(), script.params_20[4].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800db084L)
  public static void FUN_800db084(final int index, final int x, final int y, final int z, final int scriptIndex) {
    _800fabdc.get(index).deref().run(x, y, z, scriptIndex);
    camera_800c67f0.callbackIndex_88 = index;
  }

  @Method(0x800db0d8L)
  public static void FUN_800db0d8(final int x, final int y, final int z, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.vec_20.setX(x);
    cam.vec_20.setY(y);
    cam.vec_20.setZ(z);
    setRefpoint(x >> 8, y >> 8, z >> 8);
    cam._11c |= 0x2;
    cam._121 = 0;
  }

  @Method(0x800db128L)
  public static void FUN_800db128(final int x, final int y, final int z, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam._2c = z;
    cam._38 = x;
    cam._44 = y;
    final IntRef refX = new IntRef().set(x >> 8);
    final IntRef refY = new IntRef().set(y >> 8);
    final IntRef refZ = new IntRef().set(z >> 8);
    FUN_800dcc94(0, 0, 0, refX, refY, refZ);
    setRefpoint(refX.get(), refY.get(), refZ.get());
    cam._11c |= 0x2;
    cam._121 = 1;
  }

  @Method(0x800db1d4L)
  public static void FUN_800db1d4(final int x, final int y, final int z, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam.vec_20.set(x, y, z);
    setRefpoint(cam.rview2_00.viewpoint_00.getX() + (cam.vec_20.getX() >> 8), cam.rview2_00.viewpoint_00.getY() + (cam.vec_20.getY() >> 8), cam.rview2_00.viewpoint_00.getZ() + (cam.vec_20.getZ() >> 8));
    cam._11c |= 0x2;
    cam._121 = 2;
  }

  @Method(0x800db240L)
  public static void FUN_800db240(final int x, final int y, final int z, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam._38 = x;
    cam._44 = y;
    cam._2c = z;
    final IntRef refX = new IntRef().set(cam._38 >> 8);
    final IntRef refY = new IntRef().set(cam._44 >> 8);
    final IntRef refZ = new IntRef().set(cam._2c >> 8);
    FUN_800dcc94(cam.rview2_00.viewpoint_00.getX(), cam.rview2_00.viewpoint_00.getY(), cam.rview2_00.viewpoint_00.getZ(), refX, refY, refZ);
    setRefpoint(refX.get(), refY.get(), refZ.get());
    cam._11c |= 0x2;
    cam._121 = 3;
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

    cam.bobjIndex_80 = scriptIndex;
    cam._11c |= 0x2;
    cam._121 = 6;
  }

  @Method(0x800db398L)
  public static void FUN_800db398(final int x, final int y, final int z, final int scriptIndex) {
    final BattleCamera cam = camera_800c67f0;
    cam._38 = x;
    cam._44 = y;
    cam._2c = z;
    final IntRef refX = new IntRef().set(x >> 8);
    final IntRef refY = new IntRef().set(y >> 8);
    final IntRef refZ = new IntRef().set(z >> 8);
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00;
    FUN_800dcc94(bobj.model_148.coord2_14.coord.transfer.getX(), bobj.model_148.coord2_14.coord.transfer.getY(), bobj.model_148.coord2_14.coord.transfer.getZ(), refX, refY, refZ);
    setRefpoint(refX.get(), refY.get(), refZ.get());
    cam.bobjIndex_80 = scriptIndex;
    cam._11c |= 0x2;
    cam._121 = 7;
  }

  @Method(0x800db460L)
  public static FlowControl FUN_800db460(final RunningScript<?> script) {
    FUN_800db4ec(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get(), script.params_20[7].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800db4ecL)
  public static void FUN_800db4ec(final int callbackIndex, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    _800fabfc.offset(callbackIndex * 0x4L).deref(4).call(a1, a2, a3, a5, a6, a4, scriptIndex);
    final BattleCamera cam = camera_800c67f0;
    cam.callbackIndex_fc = callbackIndex;
    cam._122 = 1;
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
  public static FlowControl FUN_800db574(final RunningScript<?> script) {
    FUN_800db600(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get(), script.params_20[7].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800db600L)
  public static void FUN_800db600(final int callbackIndex, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int scriptIndex) {
    _800fac5c.offset(callbackIndex * 0x4L).deref(4).call(a1, a2, a3, a5, a6, a4, scriptIndex);
    final BattleCamera cam = camera_800c67f0;
    cam.callbackIndex_88 = callbackIndex;
    cam._123 = 1;
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
  public static FlowControl FUN_800db688(final RunningScript<?> script) {
    FUN_800db714(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get(), script.params_20[7].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800db714L)
  public static void FUN_800db714(final int callbackIndex, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int a7) {
    _800fac1c.offset(callbackIndex * 0x4L).deref(4).call(a1, a2, a3, a4, a5, a6, a7);

    final BattleCamera cam = camera_800c67f0;
    cam.callbackIndex_fc = callbackIndex;
    cam._122 = 1;
  }

  @Method(0x800db79cL)
  public static FlowControl FUN_800db79c(final RunningScript<?> script) {
    FUN_800db828(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get(), script.params_20[7].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800db828L)
  public static void FUN_800db828(final int callbackIndex, final int x, final int y, final int z, final int a4, final int a5, final int a6, final int a7) {
    _800fac7c.offset(callbackIndex * 0x4L).deref(4).call(x, y, z, a4, a5, a6, a7);
    final BattleCamera cam = camera_800c67f0;
    cam.callbackIndex_88 = callbackIndex;
    cam._123 = 1;
  }

  @Method(0x800db8b0L)
  public static FlowControl FUN_800db8b0(final RunningScript<?> a0) {
    FUN_800db950(a0.params_20[0].get(), a0.params_20[1].get(), a0.params_20[2].get(), a0.params_20[3].get(), a0.params_20[4].get(), a0.params_20[5].get(), a0.params_20[6].get(), a0.params_20[7].get(), a0.params_20[8].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800db950L)
  public static void FUN_800db950(final int callbackIndex, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int a7, final int scriptIndex) {
    _800fac3c.offset(callbackIndex * 0x4L).deref(4).call(a1, a2, a3, a4, a5, a6, a7, scriptIndex);
    final BattleCamera cam = camera_800c67f0;
    cam.callbackIndex_fc = callbackIndex;
    cam._122 = 1;
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
  public static FlowControl FUN_800db9e0(final RunningScript<?> script) {
    FUN_800dba80(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get(), script.params_20[7].get(), script.params_20[8].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x800dba80L)
  public static void FUN_800dba80(final int callbackIndex, final int a1, final int a2, final int a3, final int a4, final int a5, final int a6, final int a7, final int scriptIndex) {
    _800fac9c.offset(callbackIndex * 0x4L).deref(4).call(a1, a2, a3, a4, a5, a6, a7, scriptIndex);
    final BattleCamera cam = camera_800c67f0;
    cam.callbackIndex_88 = callbackIndex;
    cam._123 = 1;
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
  public static FlowControl FUN_800dbb10(final RunningScript<?> script) {
    final int v1 = script.params_20[0].get();
    final int a1;
    if(v1 == 0) {
      //LAB_800dbb3c
      a1 = (int)_800c6912.get();
    } else if(v1 == 1) {
      //LAB_800dbb48
      a1 = (int)_800c6913.get();
    } else {
      throw new RuntimeException("Undefined a1");
    }

    //LAB_800dbb50
    script.params_20[1].set(a1);
    return FlowControl.CONTINUE;
  }

  @Method(0x800dbb9cL)
  public static FlowControl FUN_800dbb9c(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x800dbc2cL)
  public static FlowControl scriptSetViewportTwist(final RunningScript<?> script) {
    camera_800c67f0.rview2_00.viewpointTwist_18 = script.params_20[0].get();
    return FlowControl.CONTINUE;
  }

  @Method(0x800dbc80L)
  public static FlowControl FUN_800dbc80(final RunningScript<?> script) {
    final int v1 = script.params_20[0].get();

    if((v1 & 0x1) != 0) {
      camera_800c67f0._e4 = 0;
      camera_800c67f0._b4 = 0;
    }

    //LAB_800dbca8
    if((v1 & 0x2) != 0) {
      camera_800c67f0._70 = 0;
      camera_800c67f0._40 = 0;
    }

    //LAB_800dbcc0
    return FlowControl.CONTINUE;
  }

  @Method(0x800dbcc8L)
  public static FlowControl FUN_800dbcc8(final RunningScript<?> script) {
    final BattleCamera cam = camera_800c67f0;
    cam._100 = script.params_20[0].get() << 16;
    cam._108 = 0;
    cam._118 = 1;
    return FlowControl.CONTINUE;
  }

  @Method(0x800dbcfcL)
  public static FlowControl scriptGetProjectionPlaneDistance(final RunningScript<?> script) {
    script.params_20[0].set(getProjectionPlaneDistance());
    return FlowControl.CONTINUE;
  }

  @Method(0x800dbe40L)
  public static void FUN_800dbe40() {
    final BattleCamera cam = camera_800c67f0;
    cam._11c &= 0xffff_fffe;
    cam._122 = 0;
  }

  @Method(0x800dbe60L)
  public static void FUN_800dbe60() {
    final BattleCamera cam = camera_800c67f0;
    cam._11c &= 0xffff_fffe;
    cam._122 = 0;
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
    cam._122 = 0;

    setViewpoint(
      cam.rview2_00.refpoint_0c.getX() + (cam.vec_94.getX() >> 8),
      cam.rview2_00.refpoint_0c.getY() + (cam.vec_94.getY() >> 8),
      cam.rview2_00.refpoint_0c.getZ() + (cam.vec_94.getZ() >> 8)
    );
  }

  @Method(0x800dbef0L)
  public static void FUN_800dbef0() {
    final BattleCamera cam = camera_800c67f0;
    cam._122 = 0;
    final IntRef refX = new IntRef().set(cam._ac >> 8);
    final IntRef refY = new IntRef().set(cam._b8 >> 8);
    final IntRef refZ = new IntRef().set(cam._a0 >> 8);
    FUN_800dcc94(cam.rview2_00.refpoint_0c.getX(), cam.rview2_00.refpoint_0c.getY(), cam.rview2_00.refpoint_0c.getZ(), refX, refY, refZ);
    setViewpoint(refX.get(), refY.get(), refZ.get());
  }

  @Method(0x800dbf70L)
  public static void FUN_800dbf70() {
    final BattleCamera cam = camera_800c67f0;
    cam._122 = 0;

    final VECTOR v0 = FUN_800dd02c(cam.bobjIndex_f4);
    setViewpoint(v0.getX() + (cam.vec_94.getX() >> 8), v0.getY() + (cam.vec_94.getY() >> 8), v0.getZ() + (cam.vec_94.getZ() >> 8));
  }

  @Method(0x800dbfd4L)
  public static void FUN_800dbfd4() {
    final BattleCamera cam = camera_800c67f0;
    cam._122 = 0;

    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[cam.bobjIndex_f4].innerStruct_00;
    final IntRef refX = new IntRef().set(cam._ac >> 8);
    final IntRef refY = new IntRef().set(cam._b8 >> 8);
    final IntRef refZ = new IntRef().set(cam._a0 >> 8);
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
    cam._11c &= 0xffff_fffd;
    cam._123 = 0;
  }

  @Method(0x800dc0b0L)
  public static void FUN_800dc0b0() {
    final BattleCamera cam = camera_800c67f0;
    cam._11c &= 0xffff_fffd;
    cam._123 = 0;
  }

  @Method(0x800dc0d0L)
  public static void FUN_800dc0d0() {
    final BattleCamera cam = camera_800c67f0;
    cam._123 = 0;

    setRefpoint(
      cam.rview2_00.viewpoint_00.getX() + (cam.vec_20.getX() >> 8),
      cam.rview2_00.viewpoint_00.getY() + (cam.vec_20.getY() >> 8),
      cam.rview2_00.viewpoint_00.getZ() + (cam.vec_20.getZ() >> 8)
    );
  }

  @Method(0x800dc128L)
  public static void FUN_800dc128() {
    final BattleCamera cam = camera_800c67f0;
    cam._123 = 0;

    final IntRef sp0x18 = new IntRef().set(cam._38 >> 8);
    final IntRef sp0x1c = new IntRef().set(cam._44 >> 8);
    final IntRef sp0x20 = new IntRef().set(cam._2c >> 8);
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
    cam._123 = 0;

    final VECTOR v0 = FUN_800dd02c(cam.bobjIndex_80);
    setRefpoint(
      v0.getX() + (cam.vec_20.getX() >> 8),
      v0.getY() + (cam.vec_20.getY() >> 8),
      v0.getZ() + (cam.vec_20.getZ() >> 8)
    );
  }

  @Method(0x800dc21cL)
  public static void FUN_800dc21c() {
    final BattleCamera cam = camera_800c67f0;
    cam._123 = 0;

    final IntRef sp0x18 = new IntRef().set(cam._38 >> 8);
    final IntRef sp0x1c = new IntRef().set(cam._44 >> 8);
    final IntRef sp0x20 = new IntRef().set(cam._2c >> 8);
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[cam.bobjIndex_80].innerStruct_00;
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
  public static FlowControl FUN_800dc2d8(final RunningScript<?> script) {
    final BattleCamera cam = camera_800c67f0;

    final int x;
    final int y;
    final int z;
    final long v1;
    if(script.params_20[0].get() == 0) {
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
    script.params_20[4].set((int)MEMORY.ref(4, v1).offset(script.params_20[1].get() * 0x4L).deref(4).call(script.params_20[2].get(), script.params_20[3].get(), x, y, z));
    return FlowControl.CONTINUE;
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
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00;
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
    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00;
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
  public static FlowControl FUN_800dcb84(final RunningScript<?> script) {
    final BattleCamera cam = camera_800c67f0;
    final int a1 = script.params_20[0].get();

    if((a1 & 0x1) != 0) {
      cam._120 = 0;
      cam._122 = 0;
      cam._11c &= 0xffff_fffe;
    }

    //LAB_800dcbbc
    if((a1 & 0x2) != 0) {
      cam._121 = 0;
      cam._123 = 0;
      cam._11c &= 0xffff_fffd;
    }

    //LAB_800dcbe4
    return FlowControl.CONTINUE;
  }

  @Method(0x800dcbecL)
  public static FlowControl FUN_800dcbec(final RunningScript<?> script) {
    _800c67c4.setu(script.params_20[0].get());
    _800c67d4.setu(script.params_20[1].get());
    _800c67e4.setu(script.params_20[2].get());
    _800c67e8.setu(script.params_20[3].get());
    _800fabb8.setu(0x1L);
    getScreenOffset(screenOffsetX_800c67bc, screenOffsetY_800c67c0);
    return FlowControl.CONTINUE;
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
    RotTrans(_800faba0, _800faba8, _800c67b8);
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
    final BattleScriptDataBase data = (BattleScriptDataBase)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00;

    if(BattleScriptDataBase.EM__.equals(data.magic_00)) {
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
  public static long applyStandardAnimation(final Model124 a0, final int a1) {
    long v0;
    long v1;
    final long s2;
    final long hi;
    final long lo;
    v1 = a0.ub_9c;
    if(v1 == 2) {
      return v1;
    }

    //LAB_800dd4fc
    v0 = a0.s_9a;
    if(a0.ub_a2 != 0) {
      v0 = v0 << 16;
      v0 = (int)v0 >> 17;
      hi = (a1 & 0xffff_ffffL) % (v0 & 0xffff_ffffL);
      s2 = hi;
      v1 = a0.animCount_98;
      v0 = s2 << 1;
      v0 = v0 + s2;
      lo = (long)(int)v1 * (int)v0 & 0xffff_ffffL;
      a0.partTransforms_94 = a0.partTransforms_90;
      v1 = lo;
      a0.partTransforms_94 = a0.partTransforms_90.slice((int)v1);
      applyModelPartTransforms(a0);
      v0 = a0.s_9a;
      v0 = v0 << 16;
      v0 = (int)v0 >> 17;
    } else {
      //LAB_800dd568
      hi = (a1 & 0xffff_ffffL) % (v0 & 0xffff_ffffL);
      s2 = hi;
      v1 = s2 >>> 1;
      v0 = v1 << 1;
      v0 = v0 + v1;
      lo = a0.animCount_98 * (int)v0 & 0xffff_ffffL;
      a0.partTransforms_94 = a0.partTransforms_90;
      v1 = lo;
      a0.partTransforms_94 = a0.partTransforms_90.slice((int)v1);
      applyModelPartTransforms(a0);
      if((s2 & 0x1L) != 0) {
        if(s2 != a0.s_9a - 1) {
          if(a0.ub_a3 == 0) {
            final UnboundedArrayRef<ModelPartTransforms> original = a0.partTransforms_94;
            FUN_800213c4(a0);
            a0.partTransforms_94 = original;
          }
        }
      }

      //LAB_800dd5ec
      v0 = a0.s_9a;
    }

    //LAB_800dd5f0
    a0.s_9e = (int)(v0 - s2 - 1);

    if(a0.s_9e == 0) {
      a0.ub_9c = 0;
    } else {
      //LAB_800dd618
      a0.ub_9c = 1;
    }

    //LAB_800dd61c
    //LAB_800dd620
    return a0.s_9e;
  }

  @Method(0x800dd638L)
  public static long applyLmbAnimation(final Model124 a0, final int a1) {
    int v0;
    final int s6;
    if(a0.ub_9c == 2) {
      return 2;
    }

    //LAB_800dd680
    final int count = Math.min(a0.count_c8, a0.animCount_98);

    //LAB_800dd69c
    final LmbType0 lmb = (LmbType0) a0.lmbAnim_08.lmb_00;

    final int a0_0;
    final int v1;
    if(a0.ub_a2 != 0) {
      v1 = a1 * 2 % a0.s_9a;
      s6 = 0;
      v0 = a0.s_9a >> 1;
      a0_0 = v1 >>> 1;
      v0 = v0 - a0_0;
    } else {
      //LAB_800dd6dc
      v1 = a1 % a0.s_9a;
      v0 = a1 & 0x1;
      s6 = v0 << 11;
      a0_0 = v1 >>> 1;
      v0 = a0.s_9a - v1;
    }

    //LAB_800dd700
    a0.s_9e = v0 - 1;

    //LAB_800dd720
    for(int i = 0; i < count; i++) {
      LmbTransforms14 a1_0 = lmb._08.get(i)._08.deref().get(a0_0);
      final MATRIX s0 = a0.dobj2ArrPtr_00[i].coord2_04.coord;

      final VECTOR trans = new VECTOR();
      final SVECTOR rot = new SVECTOR();
      final VECTOR scale = new VECTOR();
      trans.set(a1_0.trans_06);
      rot.set(a1_0.rot_0c);
      scale.set(a1_0.scale_00);

      if(s6 != 0) {
        if(a1 == a0.s_9a - 1) {
          a1_0 = lmb._08.get(i)._08.deref().get(0);
        } else {
          //LAB_800dd7cc
          a1_0 = lmb._08.get(i)._08.deref().get(a0_0 + 1);
        }

        //LAB_800dd7d0
        trans.add(a1_0.trans_06).div(2);
      }

      //LAB_800dd818
      RotMatrix_80040010(rot, s0);
      TransMatrix(s0, trans);
      ScaleMatrixL(s0, scale);
    }

    //LAB_800dd84c
    if(a0.s_9e == 0) {
      a0.ub_9c = 0;
    } else {
      //LAB_800dd864
      a0.ub_9c = 1;
    }

    //LAB_800dd868
    //LAB_800dd86c
    return a0.s_9e;
  }

  @Method(0x800dd89cL)
  public static void FUN_800dd89c(final Model124 model, final int a1) {
    final long v0;
    final long v1;
    long s6;
    final int nobj = model.ObjTable_0c.nobj;
    zOffset_1f8003e8.set(model.zOffset_a0);
    tmdGp0Tpage_1f8003ec.set(model.tpage_108);
    s6 = deffManager_800c693c._20 & 0x4;
    v1 = (int)s6 >> 1;
    v0 = (int)s6 >> 2;
    s6 = v1 | v0;

    //LAB_800dd928
    for(int i = 0; i < nobj; i++) {
      final GsDOBJ2 s2 = model.ObjTable_0c.top[i];

      //LAB_800dd940
      if((model.ui_f4 & 1L << i) == 0) {
        final MATRIX lw = new MATRIX();
        final MATRIX ls = new MATRIX();
        GsGetLws(s2.coord2_04, lw, ls);

        if((s6 & (ls.transfer.getZ() ^ tickCount_800bb0fc.get())) == 0 || ls.transfer.getZ() - ls.transfer.getX() >= -0x800 && ls.transfer.getZ() + ls.transfer.getX() >= -0x800 && ls.transfer.getZ() - ls.transfer.getY() >= -0x800 && ls.transfer.getZ() + ls.transfer.getY() >= -0x800) {
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
          final int s0 = s2.attribute_00;
          s2.attribute_00 = a1;
          renderCtmd(s2);
          s2.attribute_00 = s0;
        }
      }
    }

    //LAB_800dda54
    //LAB_800dda58
    for(int i = 0; i < 7; i++) {
      if(model.aub_ec[i] != 0) {
        FUN_80022018(model, i);
      }

      //LAB_800dda70
    }

    if(model.b_cc != 0) {
      FUN_80021724(model);
    }

    //LAB_800dda98
  }

  @Method(0x800ddac8L)
  public static void loadModelTmd(final Model124 model, final ExtendedTmd extTmd) {
    final VECTOR sp0x18 = new VECTOR().set(model.coord2_14.coord.transfer);

    //LAB_800ddb18
    for(int i = 0; i < 7; i++) {
      model.aub_ec[i] = 0;
    }

    final TmdWithId tmdWithId = extTmd.tmdPtr_00.deref();
    final Tmd tmd = tmdWithId.tmd;
    model.tmd_8c = tmd;
    final int count = tmd.header.nobj.get();
    model.tmdNobj_ca = count;
    model.count_c8 = count;
    model.dobj2ArrPtr_00 = new GsDOBJ2[count];
    model.coord2ArrPtr_04 = new GsCOORDINATE2[count];
    model.coord2ParamArrPtr_08 = new GsCOORD2PARAM[count];

    Arrays.setAll(model.dobj2ArrPtr_00, i -> new GsDOBJ2());
    Arrays.setAll(model.coord2ArrPtr_04, i -> new GsCOORDINATE2());
    Arrays.setAll(model.coord2ParamArrPtr_08, i -> new GsCOORD2PARAM());

    model.tpage_108 = (int)((tmdWithId.id.get() & 0xffff_0000L) >>> 11);
    adjustTmdPointers(model.tmd_8c);
    initObjTable2(model.ObjTable_0c, model.dobj2ArrPtr_00, model.coord2ArrPtr_04, model.coord2ParamArrPtr_08, model.count_c8);
    model.coord2_14.param = model.coord2Param_64;
    GsInitCoordinate2(null, model.coord2_14);
    model.ObjTable_0c.nobj = count;

    //LAB_800ddc0c
    for(int i = 0; i < count; i++) {
      if((tmd.header.flags.get() & 0x2) != 0) {
        model.dobj2ArrPtr_00[i].tmd_08 = tmd.objTable.get(i);
      } else {
        final GsDOBJ2 dobj2 = new GsDOBJ2();
        updateTmdPacketIlen(tmd.objTable, dobj2, i);
        model.dobj2ArrPtr_00[i].tmd_08 = dobj2.tmd_08;
      }

      //LAB_800ddc34
    }

    //LAB_800ddc54
    //LAB_800ddc64
    for(int i = 0; i < count; i++) {
      model.coord2ArrPtr_04[i].super_ = model.coord2_14;
    }

    //LAB_800ddc80
    model.ub_a2 = 0;
    model.ub_a3 = 0;
    model.ui_f4 = 0;
    model.zOffset_a0 = 0;
    model.coord2_14.coord.transfer.set(sp0x18);

    if((model.tmd_8c.header.flags.get() & 0x2) == 0 && model.colourMap_9d != 0) {
      FUN_80021628(model);
    }

    //LAB_800ddce8
    model.scaleVector_fc.set(0x1000, 0x1000, 0x1000);
    model.b_cc = 0;
    model.vector_10c.set(0x1000, 0x1000, 0x1000);
    model.vector_118.set(0, 0, 0);
  }

  @Method(0x800ddd3cL)
  public static long applyCmbAnimation(final Model124 a0, final int a1) {
    if(a0.ub_9c == 2) {
      return 2;
    }


    //LAB_800ddd9c
    final Model124.CmbAnim cmbAnim = a0.cmbAnim_08;
    final Cmb cmb = cmbAnim.cmb_04;
    final int a2 = cmbAnim._00;
    if(a1 == a2) {
      return a0.ub_9c;
    }

    // Note: these two variables _should_ be the same
    final int modelPartCount = cmb.modelPartCount_0c.get();
    final int count = Math.min(a0.count_c8, a0.animCount_98);

    //LAB_800dddc4
    int t0;
    final int a1_0;
    final int t3;
    if(a0.ub_a2 != 0) {
      t3 = 0;
      a1_0 = (a1 << 1) % a0.s_9a >>> 1;
      t0 = (a2 << 1) % a0.s_9a >> 1;
      a0.s_9e = (a0.s_9a >> 1) - a1_0 - 1;
    } else {
      //LAB_800dde1c
      final int v1 = a1 % a0.s_9a;
      t3 = (a1 & 0x1) << 11;
      a1_0 = v1 >>> 1;
      t0 = a2 % a0.s_9a >> 1;
      a0.s_9e = a0.s_9a - v1 - 1;
    }

    //LAB_800dde60
    if(a1_0 < t0) {
      //LAB_800dde88
      for(int partIndex = 0; partIndex < modelPartCount; partIndex++) {
        final Cmb.Transforms0c fileTransforms = cmb.transforms_10.get(partIndex);
        final Cmb.Transforms0c modelTransforms = cmbAnim.transforms_08[partIndex];

        modelTransforms.rot_00.set(fileTransforms.rot_00);
        modelTransforms.trans_06.set(fileTransforms.trans_06);
      }

      //LAB_800ddee0
      cmbAnim._00 = 0;
      t0 = 0;
    }

    //LAB_800ddeec
    //LAB_800ddf1c
    for(; t0 < a1_0; t0++) {
      //LAB_800ddf2c
      for(int partIndex = 0; partIndex < modelPartCount; partIndex++) {
        final Cmb.SubTransforms08 subTransforms = cmb.subTransforms().get(t0 * modelPartCount + partIndex);
        final Cmb.Transforms0c modelTransforms = cmbAnim.transforms_08[partIndex];

        modelTransforms.rot_00.x.add((short)(subTransforms.rot_01.getX() << subTransforms.rotScale_00.get()));
        modelTransforms.rot_00.y.add((short)(subTransforms.rot_01.getY() << subTransforms.rotScale_00.get()));
        modelTransforms.rot_00.z.add((short)(subTransforms.rot_01.getZ() << subTransforms.rotScale_00.get()));

        modelTransforms.trans_06.x.add((short)(subTransforms.trans_05.getX() << subTransforms.transScale_04.get()));
        modelTransforms.trans_06.y.add((short)(subTransforms.trans_05.getY() << subTransforms.transScale_04.get()));
        modelTransforms.trans_06.z.add((short)(subTransforms.trans_05.getZ() << subTransforms.transScale_04.get()));
      }

      //LAB_800ddfd4
    }

    //LAB_800ddfe4
    //LAB_800de158
    if(t3 == 0 || a0.ub_a3 != 0 || a1_0 == (a0.s_9a >> 1) - 1) {
      //LAB_800de164
      for(int i = 0; i < count; i++) {
        final Cmb.Transforms0c modelTransforms = cmbAnim.transforms_08[i];
        final MATRIX modelPartMatrix = a0.dobj2ArrPtr_00[i].coord2_04.coord;
        RotMatrix_80040010(modelTransforms.rot_00, modelPartMatrix);
        modelPartMatrix.transfer.set(modelTransforms.trans_06);
      }
    } else {
      //LAB_800de050
      for(int i = 0; i < count; i++) {
        final Cmb.SubTransforms08 subTransforms = cmb.subTransforms().get(a1_0 * modelPartCount + i);
        final Cmb.Transforms0c modelTransforms = cmbAnim.transforms_08[i];

        final MATRIX modelPartMatrix = a0.dobj2ArrPtr_00[i].coord2_04.coord;
        RotMatrix_80040010(modelTransforms.rot_00, modelPartMatrix);
        modelPartMatrix.transfer.set(modelTransforms.trans_06);

        final SVECTOR rotation = new SVECTOR();
        rotation.setX((short)(modelTransforms.rot_00.getX() + (subTransforms.rot_01.getX() << subTransforms.rotScale_00.get())));
        rotation.setY((short)(modelTransforms.rot_00.getY() + (subTransforms.rot_01.getY() << subTransforms.rotScale_00.get())));
        rotation.setZ((short)(modelTransforms.rot_00.getZ() + (subTransforms.rot_01.getZ() << subTransforms.rotScale_00.get())));

        final MATRIX translation = new MATRIX();
        RotMatrix_80040010(rotation, translation);
        translation.transfer.setX(modelTransforms.trans_06.getX() + (subTransforms.trans_05.getX() << subTransforms.transScale_04.get()));
        translation.transfer.setY(modelTransforms.trans_06.getY() + (subTransforms.trans_05.getY() << subTransforms.transScale_04.get()));
        translation.transfer.setZ(modelTransforms.trans_06.getZ() + (subTransforms.trans_05.getZ() << subTransforms.transScale_04.get()));

        FUN_800dd15c(modelPartMatrix, translation, 0x800);
      }
    }

    //LAB_800de1b4
    if(a0.s_9e == 0) {
      a0.ub_9c = 0;
    } else {
      //LAB_800de1cc
      a0.ub_9c = 1;
    }

    //LAB_800de1d0
    cmbAnim._00 = a1;

    //LAB_800de1e0
    return a0.s_9e;
  }

  @Method(0x800de210L)
  public static void loadModelCmb(final Model124 model, final Cmb cmb) {
    final Model124.CmbAnim anim = model.cmbAnim_08;
    final int count = cmb.modelPartCount_0c.get();

    anim.cmb_04 = cmb;
    anim.transforms_08 = new Cmb.Transforms0c[count];

    Arrays.setAll(anim.transforms_08, i -> new Cmb.Transforms0c());

    model.animType_90 = 2;
    model.lmbUnknown_94 = 0;
    model.animCount_98 = count;
    model.s_9a = cmb._0e.get() * 2;
    model.ub_9c = 1;
    model.s_9e = cmb._0e.get() * 2;

    //LAB_800de270
    for(int i = 0; i < count; i++) {
      final Cmb.Transforms0c v1 = cmb.transforms_10.get(i);
      final Cmb.Transforms0c a1_0 = anim.transforms_08[i];
      a1_0.rot_00.set(v1.rot_00);
      a1_0.trans_06.set(v1.trans_06);
    }

    //LAB_800de2c8
    anim._00 = 1;
    applyCmbAnimation(model, 0);
  }

  @Method(0x800de2e8L)
  public static void applyAnimation(final Model124 model, final int a1) {
    final int type = model.animType_90;
    if(type == 1) {
      //LAB_800de318
      applyLmbAnimation(model, a1);
    } else if(type == 0 || type == 2) {
      //LAB_800de328
      applyCmbAnimation(model, a1);
    } else {
      //LAB_800de338
      applyStandardAnimation(model, a1);
    }

    //LAB_800de340
  }

  @Method(0x800de36cL)
  public static void loadModelAnim(final Model124 model, final Anim anim) {
    final int magic = anim.magic_00.get();
    if(magic == Cmb.MAGIC) { // "CMB "
      loadModelCmb(model, (Cmb)anim);
      //LAB_800de398
    } else if(magic == Lmb.MAGIC) { // "LMB"
      final LmbType0 lmb = (LmbType0)anim;

      model.lmbAnim_08.lmb_00 = lmb;
      model.animType_90 = 1;
      model.lmbUnknown_94 = 0;
      model.animCount_98 = lmb.count_04.get();
      model.s_9a = lmb._0c.get() * 2;
      model.ub_9c = 1;
      model.s_9e = lmb._0c.get() * 2;
    } else {
      //LAB_800de3dc
      loadModelStandardAnimation(model, (TmdAnimationFile)anim);
    }

    //LAB_800de3e4
  }

  @Method(0x800de3f4L)
  public static void FUN_800de3f4(final TmdObjTable a0, final EffectManagerData6cInner a1, final MATRIX a2) {
    long s0 = deffManager_800c693c._20 & 0x4;
    s0 = s0 >> 1 | s0 >> 2;

    final MATRIX sp0x10 = new MATRIX();
    if((a1.flags_00 & 0x8) != 0) {
      FUN_8003eba0(a2, sp0x10);
      GsSetLightMatrix(sp0x10);
    } else {
      //LAB_800de458
      GsSetLightMatrix(a2);
    }

    //LAB_800de45c
    MulMatrix0(worldToScreenMatrix_800c3548, a2, sp0x10);
    if((a1.flags_00 & 0x400_0000) == 0) {
      RotMatrix_8003faf0(a1.rot_10, sp0x10);
      ScaleVectorL_SVEC(sp0x10, a1.scale_16);
    }

    //LAB_800de4a8
    if((s0 & (sp0x10.transfer.getZ() ^ tickCount_800bb0fc.get())) == 0 || sp0x10.transfer.getZ() - sp0x10.transfer.getX() >= -0x800 && sp0x10.transfer.getZ() + sp0x10.transfer.getX() >= -0x800 && sp0x10.transfer.getZ() - sp0x10.transfer.getY() >= -0x800 && sp0x10.transfer.getZ() + sp0x10.transfer.getY() >= -0x800) {
      //LAB_800de50c
      setRotTransMatrix(sp0x10);

      final GsDOBJ2 dobj2 = new GsDOBJ2();
      dobj2.attribute_00 = a1.flags_00;
      dobj2.tmd_08 = a0;
      renderCtmd(dobj2);
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
      final GsDOBJ2 dobj2 = new GsDOBJ2();
      updateTmdPacketIlen(tmd.tmd.objTable, dobj2, objIndex);
      return dobj2.tmd_08;
    }

    //LAB_800de7a0
    //LAB_800de7b4
    return tmd.tmd.objTable.get(objIndex);
  }

  @Method(0x800de840L)
  public static long unpackCtmdData(final long unpackedData, long packedData, final int unpackedSize) {
    final CtmdUnpackingData50 unpackingData = ctmdUnpackingData_800c6920;

    //LAB_800de878
    final int unpackedCount = unpackedSize / 2;
    while(unpackingData._0c < unpackedCount) {
      if((unpackingData._08 & 0x100) == 0) {
        unpackingData._08 = (int)MEMORY.get(packedData, 1) | 0xff00;
        packedData++;
      }

      //LAB_800de89c
      if((unpackingData._08 & 0x1) != 0) {
        unpackingData._10[unpackingData._00] = (int)(MEMORY.get(packedData + 1, 1) << 8 | MEMORY.get(packedData, 1));
        unpackingData._00++;
        unpackingData._00 &= 0x1f;
        unpackingData._0c++;
        packedData += 2;
      } else {
        //LAB_800de8ec
        int a1 = (int)MEMORY.get(packedData, 1);
        final int length = (a1 >>> 5) + 1;

        //LAB_800de904
        for(int i = 0; i < length; i++) {
          a1 &= 0x1f;
          unpackingData._10[unpackingData._00] = unpackingData._10[a1];
          unpackingData._00++;
          unpackingData._00 &= 0x1f;
          a1++;
        }

        //LAB_800de940
        unpackingData._0c += length;
        packedData++;
      }

      //LAB_800de94c
      unpackingData._08 >>= 1;
    }

    //LAB_800de968
    //LAB_800de970
    for(int i = 0; i < unpackedCount; i++) {
      MEMORY.set(unpackedData + i * 2, 2, unpackingData._10[unpackingData._04]);
      unpackingData._04++;
      unpackingData._04 &= 0x1f;
      unpackingData._0c--;
    }

    //LAB_800de9b4
    return packedData;
  }

  @Method(0x800de9bcL)
  public static long FUN_800de9bc(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long normals, final long count) {
    final long command = tmdGp0CommandId_1f8003ee.get();

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
              final int tpage = tmdGp0Tpage_1f8003ec.get();
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
    final long command = tmdGp0CommandId_1f8003ee.get();

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
    final long command = tmdGp0CommandId_1f8003ee.get();

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

            final int tpage = tmdGp0Tpage_1f8003ec.get();

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
    final long command = tmdGp0CommandId_1f8003ee.get();

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

            final int tpage = tmdGp0Tpage_1f8003ec.get();

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
    final long command = tmdGp0CommandId_1f8003ee.get();

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
    final long command = tmdGp0CommandId_1f8003ee.get();

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
              final int tpage = (int)MEMORY.ref(2, primitives).offset(0xaL).get() & 0xff9f | tmdGp0Tpage_1f8003ec.get();

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
              final int tpage = (int)MEMORY.ref(2, primitives).offset(0x0aL).get() & 0xff9f | tmdGp0Tpage_1f8003ec.get();

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
              final int tpage = (int)MEMORY.ref(2, primitives).offset(0xaL).get() & 0xff9f | tmdGp0Tpage_1f8003ec.get();

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
