package legend.game.combat;

import legend.core.Config;
import legend.core.MathHelper;
import legend.core.gpu.GpuCommandLine;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gte.COLOUR;
import legend.core.gte.MATRIX;
import legend.core.gte.ModelPart10;
import legend.core.gte.SVECTOR;
import legend.core.gte.Tmd;
import legend.core.gte.TmdObjTable1c;
import legend.core.gte.TmdWithId;
import legend.core.gte.VECTOR;
import legend.core.memory.Method;
import legend.core.memory.types.CString;
import legend.core.memory.types.ComponentFunction;
import legend.core.memory.types.FloatRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.ShortRef;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.deff.Anim;
import legend.game.combat.deff.Cmb;
import legend.game.combat.deff.Lmb;
import legend.game.combat.deff.LmbTransforms14;
import legend.game.combat.deff.LmbType0;
import legend.game.combat.effects.AdditionCharEffectData0c;
import legend.game.combat.effects.AdditionNameTextEffect1c;
import legend.game.combat.effects.AdditionSparksEffect08;
import legend.game.combat.effects.AdditionSparksEffectInstance4c;
import legend.game.combat.effects.AdditionStarburstEffect10;
import legend.game.combat.effects.AdditionStarburstEffectRay10;
import legend.game.combat.effects.ButtonPressHudMetrics06;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerData6cInner;
import legend.game.combat.effects.GuardEffect06;
import legend.game.combat.effects.GuardEffectMetrics04;
import legend.game.combat.effects.ModelEffect13c;
import legend.game.combat.effects.MonsterDeathEffect34;
import legend.game.combat.effects.MonsterDeathEffectObjectDestructor30;
import legend.game.combat.effects.ProjectileHitEffect14;
import legend.game.combat.effects.ProjectileHitEffect14Sub48;
import legend.game.combat.effects.RadialGradientEffect14;
import legend.game.combat.effects.SpTextEffect40;
import legend.game.combat.effects.SpTextEffectTrail10;
import legend.game.combat.effects.SpriteMetrics08;
import legend.game.combat.environment.BattleCamera;
import legend.game.combat.types.BattleObject;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptDescription;
import legend.game.scripting.ScriptParam;
import legend.game.scripting.ScriptState;
import legend.game.tmd.Renderer;
import legend.game.types.CContainer;
import legend.game.types.Model124;
import legend.game.types.ModelPartTransforms0c;
import legend.game.types.TmdAnimationFile;
import legend.game.types.Translucency;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.joml.Math;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.Arrays;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.MEMORY;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.renderButtonPressHudElement;
import static legend.game.Scus94491BpeSegment.renderButtonPressHudTexturedRect;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.zMax_1f8003cc;
import static legend.game.Scus94491BpeSegment.zMin;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment.zShift_1f8003c4;
import static legend.game.Scus94491BpeSegment_8002.SetGeomOffset;
import static legend.game.Scus94491BpeSegment_8002.adjustModelUvs;
import static legend.game.Scus94491BpeSegment_8002.animateModelTextures;
import static legend.game.Scus94491BpeSegment_8002.applyInterpolationFrame;
import static legend.game.Scus94491BpeSegment_8002.applyModelPartTransforms;
import static legend.game.Scus94491BpeSegment_8002.initObjTable2;
import static legend.game.Scus94491BpeSegment_8002.loadModelStandardAnimation;
import static legend.game.Scus94491BpeSegment_8002.renderShadow;
import static legend.game.Scus94491BpeSegment_8003.GsGetLws;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_8003.GsSetRefView2L;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_Xyz;
import static legend.game.Scus94491BpeSegment_8003.getProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.getScreenOffset;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.setRotTransMatrix;
import static legend.game.Scus94491BpeSegment_8004.RotMatrix_Zyx;
import static legend.game.Scus94491BpeSegment_8004.doNothingScript_8004f650;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;
import static legend.game.combat.Bttl_800c.getRelativeOffset;
import static legend.game.combat.Bttl_800c.FUN_800cfb14;
import static legend.game.combat.Bttl_800c.ZERO;
import static legend.game.combat.Bttl_800c._800faa90;
import static legend.game.combat.Bttl_800c._800faa92;
import static legend.game.combat.Bttl_800c._800faa94;
import static legend.game.combat.Bttl_800c._800faa98;
import static legend.game.combat.Bttl_800c._800faa9c;
import static legend.game.combat.Bttl_800c._800faa9d;
import static legend.game.combat.Bttl_800c._800fabfc;
import static legend.game.combat.Bttl_800c._800fac1c;
import static legend.game.combat.Bttl_800c._800fac3c;
import static legend.game.combat.Bttl_800c._800fac5c;
import static legend.game.combat.Bttl_800c._800fac7c;
import static legend.game.combat.Bttl_800c._800fac9c;
import static legend.game.combat.Bttl_800c.additionNames_800fa8d4;
import static legend.game.combat.Bttl_800c.additionStarburstRenderers_800c6dc4;
import static legend.game.combat.Bttl_800c.asciiTable_800fa788;
import static legend.game.combat.Bttl_800c.buttonPressHudMetrics_800faaa0;
import static legend.game.combat.Bttl_800c.cameraRefpointMethods_800fad1c;
import static legend.game.combat.Bttl_800c.cameraRotationVector_800fab98;
import static legend.game.combat.Bttl_800c.cameraTransformMatrix_800c6798;
import static legend.game.combat.Bttl_800c.cameraViewpointMethods_800facbc;
import static legend.game.combat.Bttl_800c.cameraWobbleOffsetX_800c67e4;
import static legend.game.combat.Bttl_800c.cameraWobbleOffsetY_800c67e8;
import static legend.game.combat.Bttl_800c.camera_800c67f0;
import static legend.game.combat.Bttl_800c.charWidthAdjustTable_800fa7cc;
import static legend.game.combat.Bttl_800c.completedAdditionStarburstAngleModifiers_800c6dac;
import static legend.game.combat.Bttl_800c.completedAdditionStarburstTranslationMagnitudes_800c6d94;
import static legend.game.combat.Bttl_800c.currentAddition_800c6790;
import static legend.game.combat.Bttl_800c.deffManager_800c693c;
import static legend.game.combat.Bttl_800c.framesUntilWobble_800c67d4;
import static legend.game.combat.Bttl_800c.getModelObjectTranslation;
import static legend.game.combat.Bttl_800c.guardEffectMetrics_800fa76c;
import static legend.game.combat.Bttl_800c.radialGradientEffectRenderers_800fa758;
import static legend.game.combat.Bttl_800c.refpointComponentMethods_800fad7c;
import static legend.game.combat.Bttl_800c.refpointSetFromScriptMethods_800fabdc;
import static legend.game.combat.Bttl_800c.rotateAndTranslateEffect;
import static legend.game.combat.Bttl_800c.screenOffsetX_800c67bc;
import static legend.game.combat.Bttl_800c.screenOffsetY_800c67c0;
import static legend.game.combat.Bttl_800c.scriptGetScriptedObjectPos;
import static legend.game.combat.Bttl_800c.seed_800fa754;
import static legend.game.combat.Bttl_800c.spriteMetrics_800c6948;
import static legend.game.combat.Bttl_800c.temp1_800faba0;
import static legend.game.combat.Bttl_800c.temp2_800faba8;
import static legend.game.combat.Bttl_800c.transformWorldspaceToScreenspace;
import static legend.game.combat.Bttl_800c.unused_800c67d8;
import static legend.game.combat.Bttl_800c.useCameraWobble_800fabb8;
import static legend.game.combat.Bttl_800c.viewpointComponentMethods_800fad9c;
import static legend.game.combat.Bttl_800c.viewpointSetFromScriptMethods_800fabbc;
import static legend.game.combat.Bttl_800c.wobbleFramesRemaining_800c67c4;
import static legend.game.combat.Bttl_800e.allocateEffectManager;
import static legend.game.combat.Bttl_800e.renderGenericSpriteAtZOffset0;
import static legend.game.combat.environment.BattleCamera.UPDATE_REFPOINT;
import static legend.game.combat.environment.BattleCamera.UPDATE_VIEWPOINT;

public final class Bttl_800d {
  private Bttl_800d() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger();
  private static final Marker CAMERA = MarkerManager.getMarker("CAMERA");

  @Method(0x800d0094L)
  public static void setModelObjectVisibility(final int scriptIndex, final int objIndex, final boolean clearBit) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00;

    //LAB_800d00d4
    if(clearBit) {
      bent.model_148.partInvisible_f4 &= ~(0x1L << objIndex);
    } else {
      //LAB_800d0104
      bent.model_148.partInvisible_f4 |= 0x1L << objIndex;
    }
  }

  @ScriptDescription("Gets a battle object model's part count")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bobjIndex", description = "The battle object index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "partCount", description = "The part count")
  @Method(0x800d0124L)
  public static FlowControl scriptGetBobjModelPartCount(final RunningScript<?> script) {
    final BattleObject data = SCRIPTS.getObject(script.params_20[0].get(), BattleObject.class);

    if(BattleObject.EM__.equals(data.magic_00)) {
      script.params_20[1].set(((ModelEffect13c)((EffectManagerData6c<?>)data).effect_44).model_10.partCount_98);
    } else {
      //LAB_800d017c
      script.params_20[1].set(((BattleEntity27c)data).model_148.partCount_98);
    }

    //LAB_800d0194
    return FlowControl.CONTINUE;
  }

  @Method(0x800d019cL)
  public static void renderProjectileHitEffect(final ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> state, final EffectManagerData6c<EffectManagerData6cInner.VoidType> data) {
    int a0 = 0;
    final ProjectileHitEffect14 effect = (ProjectileHitEffect14)data.effect_44;

    //LAB_800d01ec
    for(int s7 = 0; s7 < effect.count_00; s7++) {
      final ProjectileHitEffect14Sub48 s4 = effect._08[s7];

      if(s4.used_00) {
        s4._40++;
        s4.frames_44--;

        if(s4.frames_44 == 0) {
          s4.used_00 = false;
        }

        //LAB_800d0220
        s4.r_34 -= s4.fadeR_3a;
        s4.g_36 -= s4.fadeG_3c;
        s4.b_38 -= s4.fadeB_3e;

        //LAB_800d0254
        final ShortRef[] x = {new ShortRef(), new ShortRef()};
        final ShortRef[] y = {new ShortRef(), new ShortRef()};
        for(int s3 = 0; s3 < 2; s3++) {
          final VECTOR s1 = s4._04[s3];
          final SVECTOR a1 = s4._24[s3];
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
              .rgb(1, s4.r_34 >>> 8, s4.g_36 >>> 8, s4.b_38 >>> 8)
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

  @ScriptDescription("Allocates a projectile hit effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "count", description = "The effect count")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "r", description = "The red channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "g", description = "The green channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "b", description = "The blue channel")
  @Method(0x800d0564L)
  public static FlowControl scriptAllocateProjectileHitEffect(final RunningScript<? extends BattleObject> script) {
    final int count = script.params_20[1].get();

    final ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> state = allocateEffectManager(
      "ProjectileHitEffect14",
      script.scriptState_04,
      null,
      Bttl_800d::renderProjectileHitEffect,
      null,
      new ProjectileHitEffect14(count)
    );

    final EffectManagerData6c<EffectManagerData6cInner.VoidType> manager = state.innerStruct_00;
    final ProjectileHitEffect14 effect = (ProjectileHitEffect14)manager.effect_44;

    //LAB_800d0634
    for(int i = 0; i < count; i++) {
      final ProjectileHitEffect14Sub48 struct = effect._08[i];

      struct.used_00 = true;
      struct.r_34 = script.params_20[2].get() << 8;
      struct.g_36 = script.params_20[3].get() << 8;
      struct.b_38 = script.params_20[4].get() << 8;

      final short x = (short)(seed_800fa754.advance().get() % 301 + 200);
      final short y = (short)(seed_800fa754.advance().get() % 401 - 300);
      final short z = (short)(seed_800fa754.advance().get() % 601 - 300);
      struct._24[0].set(x, y, z);
      struct._24[1].set(x, y, z);

      struct._04[0].setX(0);
      struct._04[0].setY((int)(seed_800fa754.advance().get() % 101 - 50));
      struct._04[0].setZ((int)(seed_800fa754.advance().get() % 101 - 50));
      struct.frames_44 = (int)(seed_800fa754.advance().get() % 9 + 7);

      struct._40 = 0;
      struct._24[1].y.add((short)25);
      struct._04[1].set(struct._04[0]).add(struct._24[0]);
      struct.fadeR_3a = struct.r_34 / struct.frames_44;
      struct.fadeG_3c = struct.g_36 / struct.frames_44;
      struct.fadeB_3e = struct.b_38 / struct.frames_44;
    }

    //LAB_800d0980
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("No-op")
  @Method(0x800d09b8L)
  public static FlowControl FUN_800d09b8(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @Method(0x800d09c0L)
  public static void FUN_800d09c0(final EffectManagerData6c<EffectManagerData6cInner.VoidType> a0, final AdditionSparksEffectInstance4c inst) {
    getRelativeOffset(a0, null, inst.startPos_08, inst.startPos_08);
    rotateAndTranslateEffect(a0, null, inst.speed_28, inst.speed_28);
    inst.endPos_18.set(inst.startPos_08);
  }

  @Method(0x800d0a30L)
  public static void renderAdditionSparks(final ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> state, final EffectManagerData6c<EffectManagerData6cInner.VoidType> data) {
    final AdditionSparksEffect08 s6 = (AdditionSparksEffect08)data.effect_44;

    //LAB_800d0a7c
    int s7 = 0;
    for(int i = 0; i < s6.count_00; i++) {
      final AdditionSparksEffectInstance4c inst = s6.instances_04[i];

      if(inst.delay_04 != 0) {
        inst.delay_04--;
        //LAB_800d0a94
      } else if(inst.ticksRemaining_05 != 0) {
        if(inst.ticksExisted_00 == 0) {
          FUN_800d09c0(data, inst);
        }

        //LAB_800d0ac8
        inst.ticksExisted_00++;
        inst.ticksRemaining_05--;
        inst.startPos_08.add(inst.speed_28);
        final IntRef startX = new IntRef();
        final IntRef startY = new IntRef();
        final IntRef endX = new IntRef();
        final IntRef endY = new IntRef();
        final int s1 = transformWorldspaceToScreenspace(inst.startPos_08, startX, startY);
        transformWorldspaceToScreenspace(inst.endPos_18, endX, endY);

        if(i == 0) {
          s7 = (short)s1 >> 2;
        }

        //LAB_800d0b3c
        inst.speed_28.add(inst.acceleration_38);

        if(inst.startPos_08.getY() > 0) {
          inst.speed_28.setY(-inst.speed_28.getY() / 2);
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
            .rgb(0, inst.r_40 >>> 8, inst.g_42 >>> 8, inst.b_44 >>> 8)
            .rgb(1, inst.r_40 >>> 9, inst.g_42 >>> 9, inst.b_44 >>> 9)
            .pos(0, startX.get(), startY.get())
            .pos(1, endX.get(), endY.get());

          //LAB_800d0c84
          GPU.queueCommand(s7 + a3 >> 2, cmd);
        }

        //LAB_800d0ca0
        inst.r_40 -= inst.stepR_46;
        inst.g_42 -= inst.stepG_48;
        inst.b_44 -= inst.stepB_4a;
        inst.endPos_18.set(inst.startPos_08);
      }

      //LAB_800d0cec
      //LAB_800d0cf0
    }

    //LAB_800d0d10
    //LAB_800d0d94
  }

  @ScriptDescription("Allocates an addition sparks effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "count", description = "The effect count")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "r", description = "The red channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "g", description = "The green channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "b", description = "The blue channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p5", description = "Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p6", description = "Unknown, possibly ticks")
  @Method(0x800d0decL)
  public static FlowControl scriptAllocateAdditionSparksEffect(final RunningScript<? extends BattleObject> script) {
    final int count = script.params_20[1].get();
    final int s4 = script.params_20[6].get();

    final ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> state = allocateEffectManager(
      "AdditionSparksEffect08",
      script.scriptState_04,
      null,
      Bttl_800d::renderAdditionSparks,
      null,
      new AdditionSparksEffect08(count)
    );

    final EffectManagerData6c<EffectManagerData6cInner.VoidType> manager = state.innerStruct_00;
    final AdditionSparksEffect08 effect = (AdditionSparksEffect08)manager.effect_44;

    final int s1 = script.params_20[5].get() / s4;

    //LAB_800d0ee0
    for(int i = 0; i < count; i++) {
      final AdditionSparksEffectInstance4c inst = effect.instances_04[i];

      inst.ticksExisted_00 = 0;

      inst.delay_04 = (byte)(seed_800fa754.advance().get() % (s4 + 1));
      inst.ticksRemaining_05 = (byte)(seed_800fa754.advance().get() % 9 + 7);

      inst.startPos_08.set(inst.delay_04 * s1, 0, 0);
      inst.endPos_18.set(0, 0, 0);
      inst.speed_28.set((int)seed_800fa754.advance().get() % 201, (int)seed_800fa754.advance().get() % 201 - 100, (int)seed_800fa754.advance().get() % 201 - 100);
      inst.acceleration_38.set((short)0, (short)15, (short)0);

      inst.r_40 = script.params_20[2].get() << 8;
      inst.g_42 = script.params_20[3].get() << 8;
      inst.b_44 = script.params_20[4].get() << 8;

      inst.stepR_46 = inst.r_40 / inst.ticksRemaining_05;
      inst.stepG_48 = inst.g_42 / inst.ticksRemaining_05;
      inst.stepB_4a = inst.b_44 / inst.ticksRemaining_05;
    }

    //LAB_800d1154
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  /** If a secondary script is specified, modifies the translations of the starburst rays by the secondary script's translation. */
  @Method(0x800d1194L)
  public static void modifyAdditionStarburstTranslation(final EffectManagerData6c<EffectManagerData6cInner.VoidType> manager, final AdditionStarburstEffect10 starburstEffect, final IntRef[] outTranslations) {
    if(starburstEffect.parentIndex_00 == -1) {
      outTranslations[0].set(0);
      outTranslations[1].set(0);
    } else {
      //LAB_800d11c4
      final VECTOR scriptTranslation = new VECTOR();
      scriptGetScriptedObjectPos(starburstEffect.parentIndex_00, scriptTranslation);
      scriptTranslation.add(manager._10.trans_04);
      transformWorldspaceToScreenspace(scriptTranslation, outTranslations[0], outTranslations[1]);
    }

    //LAB_800d120c
  }

  @Method(0x800d1220L)
  public static void renderAdditionHitStarburst(final ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> state, final EffectManagerData6c<EffectManagerData6cInner.VoidType> manager) {
    final int[] baseAngle = {-16, 16};
    final AdditionStarburstEffect10 starburstEffect = (AdditionStarburstEffect10)manager.effect_44;
    final AdditionStarburstEffectRay10[] rayArray = starburstEffect.rayArray_0c;

    //LAB_800d128c
    for(int rayNum = 0; rayNum < starburstEffect.rayCount_04; rayNum++) {
      if(rayArray[rayNum].renderRay_00) {
        //LAB_800d12a4
        for(int i = 0; i < 2; i++) {
          int angleModifier = baseAngle[i] + (int)rayArray[rayNum].angleModifier_0a;
          int translationScale = 30 + (int)rayArray[rayNum].endpointTranslationMagnitude_06;
          int x2 = rcos(rayArray[rayNum].angle_02 + angleModifier) * translationScale >> 12;
          int y2 = rsin(rayArray[rayNum].angle_02 + angleModifier) * translationScale >> 12;
          int x3 = rcos(rayArray[rayNum].angle_02) * translationScale >> 12;
          int y3 = rsin(rayArray[rayNum].angle_02) * translationScale >> 12;
          angleModifier = baseAngle[i] + (int)rayArray[rayNum].angleModifier_0a;
          translationScale = 210 + (int)rayArray[rayNum].endpointTranslationMagnitude_06;
          final int x0 = rcos(rayArray[rayNum].angle_02 + angleModifier) * translationScale >> 12;
          final int y0 = rsin(rayArray[rayNum].angle_02 + angleModifier) * translationScale >> 12;
          final int x1 = rcos(rayArray[rayNum].angle_02) * translationScale >> 12;
          final int y1 = rsin(rayArray[rayNum].angle_02) * translationScale >> 12;
          final IntRef[] translationRefs = {new IntRef(), new IntRef()};
          modifyAdditionStarburstTranslation(manager, starburstEffect, translationRefs);
          x2 = x2 + translationRefs[0].get();
          y2 = y2 + translationRefs[1].get();
          x3 = x3 + translationRefs[0].get();
          y3 = y3 + translationRefs[1].get();

          GPU.queueCommand(30, new GpuCommandPoly(4)
            .translucent(Translucency.B_PLUS_F)
            .monochrome(0, 0)
            .rgb(1, manager._10.colour_1c.getX(), manager._10.colour_1c.getY(), manager._10.colour_1c.getZ())
            .monochrome(2, 0)
            .rgb(3, 0)
            .pos(0, x0, y0)
            .pos(1, x1, y1)
            .pos(2, x2, y2)
            .pos(3, x3, y3)
          );
        }
      }
      //LAB_800d1538
    }
    //LAB_800d1558
  }

  @Method(0x800d15d8L)
  public static void renderAdditionCompletedStarburst(final ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> state, final EffectManagerData6c<EffectManagerData6cInner.VoidType> manager) {
    final AdditionStarburstEffect10 starburstEffect = (AdditionStarburstEffect10)manager.effect_44;
    final AdditionStarburstEffectRay10[] rayArray = starburstEffect.rayArray_0c;

    final int[] xArray = new int[3];
    final int[] yArray = new int[3];

    //LAB_800d16fc
    for(int rayNum = 0; rayNum < starburstEffect.rayCount_04; rayNum++) {
      if(rayArray[rayNum].renderRay_00) {
        rayArray[rayNum].endpointTranslationMagnitude_06 += rayArray[rayNum].endpointTranslationMagnitudeVelocity_08;

        //LAB_800d1728
        for(int i = 0; i < 4; i++) {
          final IntRef[] translationRefs = {new IntRef(), new IntRef()};
          modifyAdditionStarburstTranslation(manager, starburstEffect, translationRefs);

          //LAB_800d174c
          for(int j = 0; j < 3; j++) {
            final int translationScale = Math.max(0, completedAdditionStarburstTranslationMagnitudes_800c6d94.get(i).get(j) - rayArray[rayNum].endpointTranslationMagnitude_06);

            //LAB_800d1784
            final int angleModifier = completedAdditionStarburstAngleModifiers_800c6dac.get(i).get(j);
            xArray[j] = (rcos(rayArray[rayNum].angle_02 + angleModifier) * translationScale >> 12) + translationRefs[0].get();
            yArray[j] = (rsin(rayArray[rayNum].angle_02 + angleModifier) * translationScale >> 12) + translationRefs[1].get();
          }

          GPU.queueCommand(30, new GpuCommandPoly(3)
            .translucent(Translucency.B_PLUS_F)
            .monochrome(0, 0)
            .monochrome(1, 0)
            .rgb(2, manager._10.colour_1c.getX(), manager._10.colour_1c.getY(), manager._10.colour_1c.getZ())
            .pos(0, xArray[0], yArray[0])
            .pos(1, xArray[1], yArray[1])
            .pos(2, xArray[2], yArray[2])
          );
        }
      }
      //LAB_800d190c
    }
    //LAB_800d1940
  }

  @ScriptDescription("Allocates an addition starburst effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "parentIndex", description = "The parent battle entity index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "rayCount", description = "The number of rays")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "Controls how the effect behaves")
  @Method(0x800d19ecL)
  public static FlowControl scriptAllocateAdditionStarburstEffect(final RunningScript<? extends BattleObject> script) {
    final int rayCount = script.params_20[2].get();

    final ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> state = allocateEffectManager(
      "AdditionStarburstEffect10",
      script.scriptState_04,
      null,
      additionStarburstRenderers_800c6dc4[script.params_20[3].get()],
      null,
      new AdditionStarburstEffect10(rayCount)
    );

    final EffectManagerData6c<EffectManagerData6cInner.VoidType> manager = state.innerStruct_00;
    final AdditionStarburstEffect10 effect = (AdditionStarburstEffect10)manager.effect_44;
    effect.parentIndex_00 = script.params_20[1].get();
    effect.unused_08 = 0;
    final AdditionStarburstEffectRay10[] rayArray = effect.rayArray_0c;

    //LAB_800d1ac4
    for(int rayNum = 0; rayNum < rayCount; rayNum++) {
      rayArray[rayNum].renderRay_00 = true;
      rayArray[rayNum].angle_02 = (short)(seed_800fa754.advance().get() % 4097);
      rayArray[rayNum].unused_04 = 16;
      rayArray[rayNum].endpointTranslationMagnitude_06 = (short)(seed_800fa754.advance().get() % 31);
      rayArray[rayNum].endpointTranslationMagnitudeVelocity_08 = (short)(seed_800fa754.advance().get() % 21 + 10);
      rayArray[rayNum].angleModifier_0a = (short)(seed_800fa754.advance().get() % 11 - 5);
      rayArray[rayNum].unused_0c = 0;
    }

    //LAB_800d1c7c
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates an empty effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @Method(0x800d1cacL)
  public static FlowControl FUN_800d1cac(final RunningScript<? extends BattleObject> script) {
    script.params_20[0].set(allocateEffectManager("Unknown (FUN_800d1cac)", script.scriptState_04, null, null, null, null).index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates an empty effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @Method(0x800d1cf4L)
  public static FlowControl FUN_800d1cf4(final RunningScript<? extends BattleObject> script) {
    script.params_20[0].set(allocateEffectManager("Unknown (FUN_800d1cf4)", script.scriptState_04, null, null, null, null).index);
    return FlowControl.CONTINUE;
  }

  /** Renders things like the two-tone disc at the start of Detonating Arrow */
  @Method(0x800d1d3cL)
  public static void renderDiscGradientEffect(final EffectManagerData6c<EffectManagerData6cInner.RadialGradientType> manager, final int angle, final short[] vertices, final RadialGradientEffect14 effect, final Translucency translucency) {
    if(manager._10.flags_00 >= 0) {
      GPU.queueCommand(effect.z_04 + manager._10.z_22 >> 2, new GpuCommandPoly(3)
        .translucent(translucency)
        .rgb(0, manager._10.colour_1c.getX(), manager._10.colour_1c.getY(), manager._10.colour_1c.getZ())
        .rgb(1, effect.r_0c, effect.g_0d, effect.b_0e)
        .rgb(2, effect.r_0c, effect.g_0d, effect.b_0e)
        .pos(0, vertices[0], vertices[1])
        .pos(1, vertices[2], vertices[3])
        .pos(2, vertices[4], vertices[5])
      );
    }

    //LAB_800d1e70
  }

  @Method(0x800d1e80L)
  public static void FUN_800d1e80(final EffectManagerData6c<EffectManagerData6cInner.RadialGradientType> manager, final int angle, final short[] vertices, final RadialGradientEffect14 effect, final Translucency translucency) {
    throw new RuntimeException("Not implemented");
  }

  /** Renders things like the ring effect when using a healing potion */
  @Method(0x800d21b8L)
  public static void renderRingGradientEffect(final EffectManagerData6c<EffectManagerData6cInner.RadialGradientType> manager, final int angle, final short[] vertices, final RadialGradientEffect14 effect, final Translucency translucency) {
    if(manager._10.flags_00 >= 0) {
      final VECTOR sp0x20 = new VECTOR().set(
        (int)(rcos(angle) * (manager._10.scale_16.x / effect.scaleModifier_01 + (manager._10.size_28 >> 12))),
        (int)(rsin(angle) * (manager._10.scale_16.y / effect.scaleModifier_01 + (manager._10.size_28 >> 12))),
        manager._10.z_2c
      );

      final ShortRef x0 = new ShortRef();
      final ShortRef y0 = new ShortRef();
      FUN_800cfb14(manager, sp0x20, x0, y0);

      final VECTOR sp0x30 = new VECTOR().set(
        (int)(rcos(angle + effect.angleStep_08) * (manager._10.scale_16.x / effect.scaleModifier_01 + (manager._10.size_28 >> 12))),
        (int)(rsin(angle + effect.angleStep_08) * (manager._10.scale_16.y / effect.scaleModifier_01 + (manager._10.size_28 >> 12))),
        manager._10.z_2c
      );

      final ShortRef x1 = new ShortRef();
      final ShortRef y1 = new ShortRef();
      FUN_800cfb14(manager, sp0x30, x1, y1);

      GPU.queueCommand(effect.z_04 + manager._10.z_22 >> 2, new GpuCommandPoly(4)
        .translucent(translucency)
        .rgb(0, manager._10.colour_1c.getX(), manager._10.colour_1c.getY(), manager._10.colour_1c.getZ())
        .rgb(1, manager._10.colour_1c.getX(), manager._10.colour_1c.getY(), manager._10.colour_1c.getZ())
        .rgb(2, effect.r_0c, effect.g_0d, effect.b_0e)
        .rgb(3, effect.r_0c, effect.g_0d, effect.b_0e)
        .pos(0, x0.get(), y0.get())
        .pos(1, x1.get(), y1.get())
        .pos(2, vertices[2], vertices[3])
        .pos(3, vertices[4], vertices[5])
      );
    }

    //LAB_800d2460
  }

  @Method(0x800d247cL)
  public static void renderRadialGradientEffect(final ScriptState<EffectManagerData6c<EffectManagerData6cInner.RadialGradientType>> state, final EffectManagerData6c<EffectManagerData6cInner.RadialGradientType> manager) {
    final RadialGradientEffect14 effect = (RadialGradientEffect14)manager.effect_44;
    effect.angleStep_08 = 0x1000 / (0x4 << effect.circleSubdivisionModifier_00);

    final ShortRef refX0 = new ShortRef();
    final ShortRef refY0 = new ShortRef();
    effect.z_04 = FUN_800cfb14(manager, new VECTOR(), refX0, refY0) >> 2;

    final int z = effect.z_04 + manager._10.z_22;
    if(z >= 0xa0) {
      if(z >= 0xffe) {
        effect.z_04 = 0xffe - manager._10.z_22;
      }

      //LAB_800d2510
      final VECTOR sp0x38 = new VECTOR().set(
        (int)(rcos(0) * (manager._10.scale_16.x / effect.scaleModifier_01)),
        (int)(rsin(0) * (manager._10.scale_16.y / effect.scaleModifier_01)),
        0
      );

      final ShortRef refX2 = new ShortRef();
      final ShortRef refY2 = new ShortRef();
      FUN_800cfb14(manager, sp0x38, refX2, refY2);
      effect.r_0c = manager._10.colour_24 >>> 16 & 0xff;
      effect.g_0d = manager._10.colour_24 >>>  8 & 0xff;
      effect.b_0e = manager._10.colour_24 & 0xff;

      //LAB_800d25b4
      for(int angle = 0; angle < 0x1000; ) {
        final ShortRef refX1 = new ShortRef().set(refX2.get());
        final ShortRef refY1 = new ShortRef().set(refY2.get());

        sp0x38.set(
          (int)(rcos(angle + effect.angleStep_08) * (manager._10.scale_16.x / effect.scaleModifier_01)),
          (int)(rsin(angle + effect.angleStep_08) * (manager._10.scale_16.y / effect.scaleModifier_01)),
          0
        );

        FUN_800cfb14(manager, sp0x38, refX2, refY2);
        effect.renderer_10.accept(manager, angle, new short[] {refX0.get(), refY0.get(), refX1.get(), refY1.get(), refX2.get(), refY2.get()}, effect, (manager._10.flags_00 & 0x1000_0000) != 0 ? Translucency.B_PLUS_F : Translucency.B_MINUS_F);
        angle += effect.angleStep_08;
      }
    }

    //LAB_800d2710
  }

  @ScriptDescription("Allocates a radial gradient effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "count", description = "The number of subdivisions in the gradient")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "The gradient type")
  @Method(0x800d2734L)
  public static FlowControl scriptAllocateRadialGradientEffect(final RunningScript<? extends BattleObject> script) {
    final int circleSubdivisionModifier = script.params_20[1].get();
    final int type = script.params_20[2].get();

    final ScriptState<EffectManagerData6c<EffectManagerData6cInner.RadialGradientType>> state = allocateEffectManager(
      "RadialGradientEffect14",
      script.scriptState_04,
      null,
      Bttl_800d::renderRadialGradientEffect,
      null,
      new RadialGradientEffect14(),
      new EffectManagerData6cInner.RadialGradientType()
    );

    final EffectManagerData6c<EffectManagerData6cInner.RadialGradientType> manager = state.innerStruct_00;

    //LAB_800d27b4
    manager._10.scale_16.set(1.0f, 1.0f, 1.0f);

    final RadialGradientEffect14 effect = (RadialGradientEffect14)manager.effect_44;
    effect.circleSubdivisionModifier_00 = circleSubdivisionModifier;
    effect.scaleModifier_01 = (type - 3 & 0xffff_ffffL) >= 2 ? 4.0f : 1.0f;
    effect.renderer_10 = radialGradientEffectRenderers_800fa758[type];
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x800d2810L)
  public static void renderGuardEffect(final ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> state, final EffectManagerData6c<EffectManagerData6cInner.VoidType> manager) {
    final VECTOR translation = new VECTOR();
    final IntRef[] refXArray = new IntRef[7];
    final IntRef[] refYArray = new IntRef[7];

    Arrays.setAll(refXArray, i -> new IntRef());
    Arrays.setAll(refYArray, i -> new IntRef());

    final GuardEffect06 effect = (GuardEffect06)manager.effect_44;
    effect._02++;
    effect._04 += 0x400;

    //LAB_800d2888
    GuardEffectMetrics04 guardEffectMetrics;
    int effectZ = 0;
    for(int i = 6; i >= 0; i--) {
      //LAB_800d289c
      guardEffectMetrics = guardEffectMetrics_800fa76c.get(i);
      translation.setX(manager._10.trans_04.getX() + (int)(i != 0 ? manager._10.scale_16.x * 0x1000 / 4 : 0));
      translation.setY(manager._10.trans_04.getY() + (int)(guardEffectMetrics.y_02.get() * manager._10.scale_16.y));
      translation.setZ(manager._10.trans_04.getZ() + (int)(guardEffectMetrics.z_00.get() * manager._10.scale_16.z));
      effectZ = transformWorldspaceToScreenspace(translation, refXArray[i], refYArray[i]);
    }

    effectZ = effectZ >> 2;
    int r = MathHelper.clamp(manager._10.colour_1c.getX() - 1 << 8, 0, 0x8000) >>> 7;
    int g = MathHelper.clamp(manager._10.colour_1c.getY() - 1 << 8, 0, 0x8000) >>> 7;
    int b = MathHelper.clamp(manager._10.colour_1c.getZ() - 1 << 8, 0, 0x8000) >>> 7;
    r = Math.min((r + g + b) / 3 * 2, 0xff);

    //LAB_800d2a80
    //LAB_800d2a9c
    for(int i = 0; i < 5; i++) {
      int managerZ = manager._10.z_22;
      final int totalZ = effectZ + managerZ;
      if(totalZ >= 0xa0) {
        if(totalZ >= 0xffe) {
          managerZ = 0xffe - effectZ;
        }

        //LAB_800d2bc0
        // Main part of shield effect
        GPU.queueCommand(effectZ + managerZ >> 2, new GpuCommandPoly(3)
          .translucent(Translucency.B_PLUS_F)
          .pos(0, refXArray[i + 1].get(), refYArray[i + 1].get())
          .pos(1, refXArray[i + 2].get(), refYArray[i + 2].get())
          .pos(2, refXArray[0    ].get(), refYArray[0    ].get())
          .rgb(0, manager._10.colour_1c.getX(), manager._10.colour_1c.getY(), manager._10.colour_1c.getZ())
          .rgb(1, manager._10.colour_1c.getX(), manager._10.colour_1c.getY(), manager._10.colour_1c.getZ())
          .monochrome(2, r)
        );
      }
    }

    //LAB_800d2c78
    int s6 = 0x1000;
    r = manager._10.colour_1c.getX();
    g = manager._10.colour_1c.getY();
    b = manager._10.colour_1c.getZ();
    final int stepR = r >>> 2;
    final int stepG = g >>> 2;
    final int stepB = b >>> 2;

    //LAB_800d2cfc
    int baseX = 0;
    for(int i = 0; i < 4; i++) {
      s6 = s6 + effect._04 / 4;
      baseX = (int)(baseX + manager._10.scale_16.x * 0x1000 / 4);
      r = r - stepR;
      g = g - stepG;
      b = b - stepB;

      //LAB_800d2d4c
      for(int n = 1; n < 7; n++) {
        guardEffectMetrics = guardEffectMetrics_800fa76c.get(n);
        translation.setX(baseX + manager._10.trans_04.getX());
        translation.setY(((int)(guardEffectMetrics.y_02.get() * manager._10.scale_16.y * s6) >> 12) + manager._10.trans_04.getY());
        translation.setZ(((int)(guardEffectMetrics.z_00.get() * manager._10.scale_16.z * s6) >> 12) + manager._10.trans_04.getZ());
        effectZ = transformWorldspaceToScreenspace(translation, refXArray[n], refYArray[n]) >> 2;
      }

      //LAB_800d2e20
      for(int n = 0; n < 5; n++) {
        int managerZ = manager._10.z_22;
        final int totalZ = effectZ + managerZ;
        if(totalZ >= 0xa0) {
          if(totalZ >= 0xffe) {
            managerZ = 0xffe - effectZ;
          }

          //LAB_800d2ee8
          // Radiant lines of shield effect
          GPU.queueCommand(effectZ + managerZ >> 2, new GpuCommandLine()
            .translucent(Translucency.B_PLUS_F)
            .pos(0, refXArray[n + 1].get(), refYArray[n + 1].get())
            .pos(1, refXArray[n + 2].get(), refYArray[n + 2].get())
            .rgb(r, g, b)
          );
        }
      }

      //LAB_800d2fa4
    }
  }

  @ScriptDescription("Allocates a guard effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @Method(0x800d2ff4L)
  public static FlowControl scriptAllocateGuardEffect(final RunningScript<? extends BattleObject> script) {
    final ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> state = allocateEffectManager(
      "GuardEffect06",
      script.scriptState_04,
      null,
      Bttl_800d::renderGuardEffect,
      null,
      new GuardEffect06()
    );

    final EffectManagerData6c<EffectManagerData6cInner.VoidType> manager = state.innerStruct_00;
    final GuardEffect06 effect = (GuardEffect06)manager.effect_44;
    effect._00 = 1;
    effect._02 = 0;
    effect._04 = 0;

    // Hack to make shield color default if counter overlay color is default
    // Otherwise, just use the overlay color. Maybe we can make shields toggleable later.
    final int rgb = Config.getCounterOverlayRgb();
    if(Config.changeAdditionOverlayRgb() && rgb != 0x2060d8) {
      manager._10.colour_1c.set(rgb & 0xff, rgb >> 8 & 0xff, rgb >> 16 & 0xff);
    } else {
      manager._10.colour_1c.set(255, 0, 0);
    }

    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("No-op")
  @Method(0x800d3090L)
  public static FlowControl FUN_800d3090(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("No-op")
  @Method(0x800d3098L)
  public static FlowControl FUN_800d3098(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("No-op")
  @Method(0x800d30a0L)
  public static FlowControl FUN_800d30a0(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("No-op")
  @Method(0x800d30a8L)
  public static FlowControl FUN_800d30a8(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("No-op")
  @Method(0x800d30b0L)
  public static FlowControl FUN_800d30b0(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("No-op")
  @Method(0x800d30b8L)
  public static FlowControl FUN_800d30b8(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @Method(0x800d30c0L)
  public static void monsterDeathEffectRenderer(final ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> state, final EffectManagerData6c<EffectManagerData6cInner.VoidType> manager) {
    final MonsterDeathEffect34 deathEffect = (MonsterDeathEffect34)manager.effect_44;
    final MonsterDeathEffectObjectDestructor30[] objArray = deathEffect.objectDestructorArray_30;

    //LAB_800d30fc
    for(int objIndex = 0; objIndex < deathEffect.modelObjectCount_04; objIndex++) {
      if(objArray[objIndex].destructionState_00 == 1) {
        deathEffect.sprite_0c.r_14 = objArray[objIndex].r_24 >>> 8;
        deathEffect.sprite_0c.g_15 = objArray[objIndex].g_26 >>> 8;
        deathEffect.sprite_0c.b_16 = objArray[objIndex].b_28 >>> 8;
        deathEffect.sprite_0c.angle_20 = objArray[objIndex].angleModifier_0c;
        deathEffect.sprite_0c.scaleX_1c = manager._10.scale_16.x + objArray[objIndex].scaleModifier_04;
        deathEffect.sprite_0c.scaleY_1e = manager._10.scale_16.y + objArray[objIndex].scaleModifier_04;
        renderGenericSpriteAtZOffset0(deathEffect.sprite_0c, objArray[objIndex].translation_14);
      }
      //LAB_800d3174
    }
    //LAB_800d3190
  }

  @Method(0x800d31b0L)
  public static void monsterDeathEffectTicker(final ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> state, final EffectManagerData6c<EffectManagerData6cInner.VoidType> manager) {
    final MonsterDeathEffect34 deathEffect = (MonsterDeathEffect34)manager.effect_44;

    deathEffect.remainingFrameLimit_02--;
    if(deathEffect.remainingFrameLimit_02 == 0) {
      state.deallocateWithChildren();
    } else {
      //LAB_800d320c
      final MonsterDeathEffectObjectDestructor30[] objArray = deathEffect.objectDestructorArray_30;
      deathEffect.destroyedPartsCutoffIndex_00 += 2;

      //LAB_800d322c
      for(int objIndex = 0; objIndex < deathEffect.modelObjectCount_04; objIndex++) {
        if(deathEffect.destroyedPartsCutoffIndex_00 >= objIndex + 1 && objArray[objIndex].destructionState_00 == -1) {
          objArray[objIndex].destructionState_00 = 1;
          objArray[objIndex].stepCount_01 = 8;
          objArray[objIndex].scaleModifier_04 = 0;
          objArray[objIndex].scaleModifierVelocity_08 = (int)(seed_800fa754.advance().get() % 49 + 104) / (float)0x1000;
          objArray[objIndex].angleModifier_0c = MathHelper.psxDegToRad((int)(seed_800fa754.advance().get() % 4097));
          objArray[objIndex].angleModifierVelocity_10 = 0;
          objArray[objIndex].r_24 = manager._10.colour_1c.getX() << 8;
          objArray[objIndex].g_26 = manager._10.colour_1c.getY() << 8;
          objArray[objIndex].b_28 = manager._10.colour_1c.getZ() << 8;
          objArray[objIndex].stepR_2a = objArray[objIndex].r_24 / objArray[objIndex].stepCount_01;
          objArray[objIndex].stepG_2c = objArray[objIndex].g_26 / objArray[objIndex].stepCount_01;
          objArray[objIndex].stepB_2e = objArray[objIndex].b_28 / objArray[objIndex].stepCount_01;
          final VECTOR translation = new VECTOR();
          getModelObjectTranslation(deathEffect.scriptIndex_08, translation, objIndex);
          objArray[objIndex].translation_14.set(translation);
          setModelObjectVisibility(deathEffect.scriptIndex_08, objIndex, false);
        }

        //LAB_800d33d0
        if(objArray[objIndex].destructionState_00 > 0) {
          objArray[objIndex].stepCount_01--;

          if(objArray[objIndex].stepCount_01 == 0) {
            objArray[objIndex].destructionState_00 = 0;
          }

          //LAB_800d3400
          objArray[objIndex].angleModifier_0c +=  objArray[objIndex].angleModifierVelocity_10;
          objArray[objIndex].r_24 -= objArray[objIndex].stepR_2a;
          objArray[objIndex].g_26 -= objArray[objIndex].stepG_2c;
          objArray[objIndex].b_28 -= objArray[objIndex].stepB_2e;
          objArray[objIndex].scaleModifier_04 +=  objArray[objIndex].scaleModifierVelocity_08;
        }
        //LAB_800d3450
      }
    }
    //LAB_800d346c
  }

  @ScriptDescription("Allocates a monster death effect effect for a monster battle entity")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The battle object index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "spriteIndex", description = "Which sprite to use")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "unused", description = "Unused in code but passed by scripts")
  @Method(0x800d34bcL)
  public static FlowControl scriptAllocateMonsterDeathEffect(final RunningScript<? extends BattleObject> script) {
    final BattleEntity27c bent = SCRIPTS.getObject(script.params_20[1].get(), BattleEntity27c.class);
    final int modelObjectCount = bent.model_148.partCount_98;

    final ScriptState<EffectManagerData6c<EffectManagerData6cInner.VoidType>> state = allocateEffectManager(
      "MonsterDeathEffect34",
      script.scriptState_04,
      Bttl_800d::monsterDeathEffectTicker,
      Bttl_800d::monsterDeathEffectRenderer,
      null,
      new MonsterDeathEffect34(modelObjectCount)
    );

    final EffectManagerData6c<EffectManagerData6cInner.VoidType> manager = state.innerStruct_00;
    final MonsterDeathEffect34 deathEffect = (MonsterDeathEffect34)manager.effect_44;

    deathEffect.destroyedPartsCutoffIndex_00 = 0;
    deathEffect.remainingFrameLimit_02 = modelObjectCount + 8;
    deathEffect.unused_06 = 0;
    deathEffect.scriptIndex_08 = script.params_20[1].get();

    //LAB_800d35a0
    final MonsterDeathEffectObjectDestructor30[] objArray = deathEffect.objectDestructorArray_30;
    for(int objIndex = 0; objIndex < deathEffect.modelObjectCount_04; objIndex++) {
      setModelObjectVisibility(deathEffect.scriptIndex_08, objIndex, true);
      objArray[objIndex].destructionState_00 = -1;
    }

    //LAB_800d35cc
    final SpriteMetrics08 metrics = spriteMetrics_800c6948[script.params_20[2].get() & 0xff];
    deathEffect.sprite_0c.flags_00 = manager._10.flags_00 & 0xffff_ffffL;
    deathEffect.sprite_0c.w_08 = metrics.w_04;
    deathEffect.sprite_0c.h_0a = metrics.h_05;
    deathEffect.sprite_0c.x_04 = (short)(-deathEffect.sprite_0c.w_08 >> 1);
    deathEffect.sprite_0c.y_06 = (short)(-deathEffect.sprite_0c.h_0a >> 1);
    deathEffect.sprite_0c.tpage_0c = (metrics.v_02 & 0x100) >>> 4 | (metrics.u_00 & 0x3ff) >>> 6;
    deathEffect.sprite_0c.u_0e = (metrics.u_00 & 0x3f) * 4;
    deathEffect.sprite_0c.v_0f = metrics.v_02;
    deathEffect.sprite_0c.clutX_10 = metrics.clut_06 << 4 & 0x3ff;
    deathEffect.sprite_0c.clutY_12 = metrics.clut_06 >>> 6 & 0x1ff;
    deathEffect.sprite_0c.unused_18 = 0;
    deathEffect.sprite_0c.unused_1a = 0;
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
    renderButtonPressHudTexturedRect(displayX, displayY, charIdx % 21 * 12 & 0xfc, charIdx / 21 * 12 + 144 & 0xfc, 12, 12, 0xa, Translucency.B_PLUS_F, new COLOUR().set(charAlpha, charAlpha, charAlpha), 0x1000);
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
  public static void renderAdditionNameChar(final AdditionNameTextEffect1c additionStruct, final AdditionCharEffectData0c charStruct, final long charAlpha, final long charIdx) {
    renderAdditionNameChar((short)charStruct.position_04, (short)charStruct.offsetY_06, (short)additionStruct.addition_02, (short)charIdx, (int)charAlpha);
  }

  @Method(0x800d3a64L)
  public static void renderAdditionNameEffect(final AdditionNameTextEffect1c a0, final AdditionCharEffectData0c a1, final long charAlpha, final long a3) {
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
  public static void tickAdditionNameEffect(final ScriptState<AdditionNameTextEffect1c> state, final AdditionNameTextEffect1c additionStruct) {
    additionStruct._04++;

    if(_800faa9d.get() == 0) {
      state.deallocateWithChildren();
    } else {
      //LAB_800d3c10
      //LAB_800d3c24
      for(int charIdx = 0; charIdx < additionStruct.length_08; charIdx++) {
        final AdditionCharEffectData0c charStruct = additionStruct.ptr_18[charIdx];

        if(charStruct.scrolling_00 != 0) {
          charStruct.position_04 += additionStruct.positionMovement_0c;

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

  @ScriptDescription("Allocates an addition name display script state")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "charId", description = "The character ID")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1", description = "Unknown, -1 will deallocate next tick")
  @Method(0x800d3d74L)
  public static FlowControl scriptAllocateAdditionScript(final RunningScript<?> script) {
    if(script.params_20[1].get() == -1) {
      _800faa9d.set(0);
    } else {
      //LAB_800d3dc0
      final int addition = gameState_800babc8.charData_32c[script.params_20[0].get()].selectedAddition_19;
      final ScriptState<AdditionNameTextEffect1c> state = SCRIPTS.allocateScriptState("AdditionNameTextEffect1c", new AdditionNameTextEffect1c());
      state.loadScriptFile(doNothingScript_8004f650);
      state.setTicker(Bttl_800d::tickAdditionNameEffect);
      final CString additionName = getAdditionName(0, addition);

      //LAB_800d3e5c
      int textLength;
      for(textLength = 0; additionName.charAt(textLength) != 0; textLength++) {
        //
      }

      //LAB_800d3e7c
      final AdditionNameTextEffect1c additionStruct = state.innerStruct_00;
      additionStruct._00 = 0;
      additionStruct.addition_02 = addition;
      additionStruct._04 = 0;
      additionStruct.length_08 = textLength;
      additionStruct.positionMovement_0c = 120;
      additionStruct.renderer_14 = Bttl_800d::renderAdditionNameChar;
      additionStruct.ptr_18 = new AdditionCharEffectData0c[additionStruct.length_08];
      Arrays.setAll(additionStruct.ptr_18, i -> new AdditionCharEffectData0c());
      _800faa9d.set(1);

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
    renderButtonPressHudTexturedRect(x, y, a2 * 8 + 16 & 0xf8, 40, 8, 16, a3, Translucency.B_PLUS_F, new COLOUR().set(colour, colour, colour), 0x1000);
  }

  @Method(0x800d4018L)
  public static void tickSpTextEffect(final ScriptState<SpTextEffect40> state, final SpTextEffect40 s3) {
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
    final SpTextEffectTrail10[] charArray = s3.charArray_3c;

    //LAB_800d4118
    int i;
    for (i = 7; i > 0; i--) {
      charArray[i]._00 = charArray[i - 1]._00;
      charArray[i]._04 = charArray[i - 1]._04;
    }

    charArray[0]._00 = s3._0c;
    charArray[0]._04 = s3._10;
    final String sp0x18 = Integer.toString(s3._08);
    int fp = s3._02;
    final int sp20 = (fp & 0xff) >>> 3;
    final int sp2c = s3._01 + 0x21 & 0xff;

    //LAB_800d419c
    short s2;
    short s5;
    for (i = 0; i < 8; i++) {
      fp = fp - sp20;

      if(i == 0 || charArray[i]._00 != s3._0c || charArray[i]._04 != s3._10) {
        //LAB_800d41d8
        s2 = (short)(charArray[i]._00 >> 8);
        s5 = (short)(charArray[i]._04 >> 8);

        if(s3._01 != 0) {
          FUN_800d3f98(s2, s5, 10, (short)sp2c, fp & 0xff);
          s2 += 8;
        }

        //LAB_800d4224
        //LAB_800d423c
        for(int j = 0; j < sp0x18.length(); j++) {
          FUN_800d3f98(s2, s5, sp0x18.charAt(j) - 0x30, (short)sp2c, fp & 0xff);
          s2 += 8;
        }

        //LAB_800d4274
        FUN_800d3f98((short)(s2 - 2), s5, 11, (short)sp2c, fp & 0xff);
        FUN_800d3f98((short)(s2 + 4), s5, 12, (short)sp2c, fp & 0xff);
      }
      //LAB_800d42c0
    }
    //LAB_800d42dc
  }

  @ScriptDescription("Allocates an addition SP text display effect manager")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @ScriptParam(direction = ScriptParam.Direction.BOTH, type = ScriptParam.Type.INT, name = "p1")
  @Method(0x800d4338L)
  public static FlowControl scriptAllocateSpTextEffect(final RunningScript<?> script) {
    final int s2 = script.params_20[0].get();
    final int s3 = script.params_20[1].get();

    if(s2 == -1) {
      _800faa94.set(0);
    } else {
      //LAB_800d4388
      final ScriptState<SpTextEffect40> state = SCRIPTS.allocateScriptState("SpTextEffect40", new SpTextEffect40());
      state.loadScriptFile(doNothingScript_8004f650);
      state.setTicker(Bttl_800d::tickSpTextEffect);

      final SpTextEffect40 s1 = state.innerStruct_00;
      s1._00 = 1;
      s1._02 = 0x80;
      s1._04 = 0;
      s1._08 = s2;
      s1._0c = 0;
      s1._10 = 30;
      s1._1c = _800faa90.get() << 8;
      s1._20 = 0x5000;

      if(s3 == 1) {
        _800faa92.set((short)0);
        _800faa94.set(s3);
        _800faa98.set(0);
        s1._01 = 0;
        s1._1c = 0xffff6e00;
        _800faa90.set((short)-0x92);
      } else {
        //LAB_800d4470
        _800faa92.add((short)1);
        s1._01 = _800faa92.get();
      }

      //LAB_800d448c
      s1._2c = (s1._1c - s1._0c) / 14;
      s1._30 = -0x800;
      _800faa98.add(s2);

      //LAB_800d44dc
      for(int i = 0; i < 8; i++) {
        s1.charArray_3c[i]._00 = s1._0c;
        s1.charArray_3c[i]._04 = s1._10;
      }

      final int strLen = String.valueOf(s1._08).length();

      final long v1;
      if(s1._01 == 0) {
        v1 = strLen + 2;
      } else {
        v1 = strLen + 3;
      }

      //LAB_800d453c
      _800faa90.set((short)((s1._1c >> 8) + v1 * 8 - 3));
    }

    script.params_20[1].set(0);

    //LAB_800d4560
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates an addition name effect manager")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x800d4580L)
  public static FlowControl scriptAllocateAdditionNameEffect(final RunningScript<?> script) {
    final int s2 = script.params_20[0].get();
    if(s2 != -1) {
      final ScriptState<AdditionNameTextEffect1c> state = SCRIPTS.allocateScriptState("AdditionScriptData1c", new AdditionNameTextEffect1c());
      state.loadScriptFile(doNothingScript_8004f650);
      state.setTicker(Bttl_800d::tickAdditionNameEffect);
      final AdditionNameTextEffect1c s0 = state.innerStruct_00;
      s0.ptr_18 = new AdditionCharEffectData0c[] {new AdditionCharEffectData0c()};
      _800faa9c.set(1);
      s0.positionMovement_0c = 40;
      s0.renderer_14 = Bttl_800d::renderAdditionNameEffect;
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
      _800faa9c.set(0);
    }

    //LAB_800d46bc
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates the button press HUD for additions")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "The button press type")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "translucency", description = "The translucency mode")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "brightness", description = "The brightness")
  @Method(0x800d46d4L)
  public static FlowControl scriptRenderButtonPressHudElement(final RunningScript<?> script) {
    final ButtonPressHudMetrics06 metrics = buttonPressHudMetrics_800faaa0.get(script.params_20[0].get());

    final COLOUR colour = new COLOUR().set(script.params_20[4].get(), script.params_20[4].get(), script.params_20[4].get());

    if(metrics.hudElementType_00.get() == 0) {
      renderButtonPressHudTexturedRect(script.params_20[1].get(), script.params_20[2].get(), metrics.u_01.get(), metrics.v_02.get(), metrics.wOrRightU_03.get(), metrics.hOrBottomV_04.get(), metrics.clutOffset_05.get(), Translucency.of(script.params_20[3].get()), colour, 0x1000);
    } else {
      //LAB_800d4784
      renderButtonPressHudElement(script.params_20[1].get(), script.params_20[2].get(), metrics.u_01.get(), metrics.v_02.get(), metrics.wOrRightU_03.get(), metrics.hOrBottomV_04.get(), metrics.clutOffset_05.get(), Translucency.of(script.params_20[3].get()), colour, 0x1000, 0x1000);
    }

    //LAB_800d47cc
    return FlowControl.CONTINUE;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d47dcL)
  public static void FUN_800d47dc(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    cam.viewpointBaseTranslation_94.set(cam.rview2_00.viewpoint_00);

    if(a5 == 0) {
      //LAB_800d4854
      cam.viewpointTicksRemaining_d0 = ticks;
      cam.stepX_b0 = (x - cam.viewpointBaseTranslation_94.x) / ticks;
      cam.stepY_bc = (y - cam.viewpointBaseTranslation_94.y) / ticks;
      cam.stepZ_c8 = (z - cam.viewpointBaseTranslation_94.z) / ticks;
    } else assert a5 != 1 : "Undefined t0/t1";

    //LAB_800d492c
    //LAB_800d4934
    cam.flags_11c |= UPDATE_VIEWPOINT;
    cam.viewpointCallbackIndex_120 = 8;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d496cL)
  public static void FUN_800d496c(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    cam.viewpointAngleX_ac = calculateCameraValue(false, 1, 0, null); // Angle from 0 to viewpoint X
    cam.viewpointAngleY_b8 = calculateCameraValue(false, 1, 1, null); // Angle from 0 to viewpoint Y
    cam.viewpointDeltaMagnitude_a0 = calculateCameraValue(false, 1, 2, null); // Angle from 0 to viewpoint Z

    if(a5 == 0) {
      //LAB_800d4a24
      cam.viewpointTicksRemaining_d0 = ticks;
      cam.stepX_b0 = calculateStep(0, cam.viewpointAngleX_ac, x, ticks, stepType & 3);
      cam.stepY_bc = calculateStep(1, cam.viewpointAngleY_b8, y, ticks, stepType >> 2 & 3);
      cam.stepZ_c8 = calculateStep(2, cam.viewpointDeltaMagnitude_a0, z, ticks, 0);
    } else if(a5 == 1) {
      //LAB_800d4a7c
      final float x2 = calculateDifference(0, cam.viewpointAngleX_ac, x, stepType & 3);
      final float y2 = calculateDifference(1, cam.viewpointAngleY_b8, y, stepType >> 2 & 3);
      final float z2 = calculateDifference(2, cam.viewpointDeltaMagnitude_a0, z, 0);
      cam.viewpointTicksRemaining_d0 = (int)(Math.sqrt(x2 * x2 + y2 * y2 + z2 * z2) / ticks);
      cam.stepX_b0 = calculateStep(0, cam.viewpointAngleX_ac, x, cam.viewpointTicksRemaining_d0, stepType & 3);
      cam.stepY_bc = calculateStep(1, cam.viewpointAngleY_b8, y, cam.viewpointTicksRemaining_d0, stepType >> 2 & 3);
      cam.stepZ_c8 = calculateStep(2, cam.viewpointDeltaMagnitude_a0, z, cam.viewpointTicksRemaining_d0, 0);
    }

    //LAB_800d4b68
    cam.flags_11c |= UPDATE_VIEWPOINT;
    cam.viewpointCallbackIndex_120 = 9;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d4bacL)
  public static void FUN_800d4bac(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    cam.viewpointBaseTranslation_94.x = calculateCameraValue(false, 4, 0, null);
    cam.viewpointBaseTranslation_94.y = calculateCameraValue(false, 4, 1, null);
    cam.viewpointBaseTranslation_94.z = calculateCameraValue(false, 4, 2, null);

    if(a5 == 0) {
      //LAB_800d4c5c
      cam.viewpointTicksRemaining_d0 = ticks;
      cam.stepX_b0 = (x - cam.viewpointBaseTranslation_94.x) / ticks;
      cam.stepY_bc = (y - cam.viewpointBaseTranslation_94.y) / ticks;
      cam.stepZ_c8 = (z - cam.viewpointBaseTranslation_94.z) / ticks;
    } else assert a5 != 1 : "Undefined s3/s5";

    //LAB_800d4d34
    //LAB_800d4d3c
    cam.flags_11c |= UPDATE_VIEWPOINT;
    cam.viewpointCallbackIndex_120 = 12;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d4d7cL)
  public static void FUN_800d4d7c(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    cam.viewpointAngleX_ac = calculateCameraValue(false, 5, 0, null);
    cam.viewpointAngleY_b8 = calculateCameraValue(false, 5, 1, null);
    cam.viewpointDeltaMagnitude_a0 = calculateCameraValue(false, 5, 2, null);

    if(a5 == 0) {
      //LAB_800d4e34
      cam.viewpointTicksRemaining_d0 = ticks;
      cam.stepX_b0 = calculateStep(0, cam.viewpointAngleX_ac, x, ticks, stepType & 3);
      cam.stepY_bc = calculateStep(1, cam.viewpointAngleY_b8, y, ticks, stepType >> 2 & 3);
      cam.stepZ_c8 = calculateStep(2, cam.viewpointDeltaMagnitude_a0, z, ticks, 0);
    } else if(a5 == 1) {
      //LAB_800d4e8c
      final float x2 = calculateDifference(0, cam.viewpointAngleX_ac, x, stepType & 3);
      final float y2 = calculateDifference(1, cam.viewpointAngleY_b8, y, stepType >> 2 & 3);
      final float z2 = calculateDifference(2, cam.viewpointDeltaMagnitude_a0, z, 0);
      cam.viewpointTicksRemaining_d0 = (int)(Math.sqrt(x2 * x2 + y2 * y2 + z2 * z2) / ticks);
      cam.stepX_b0 = calculateStep(0, cam.viewpointAngleX_ac, x, cam.viewpointTicksRemaining_d0, stepType & 3);
      cam.stepY_bc = calculateStep(1, cam.viewpointAngleY_b8, y, cam.viewpointTicksRemaining_d0, stepType >> 2 & 3);
      cam.stepZ_c8 = calculateStep(2, cam.viewpointDeltaMagnitude_a0, z, cam.viewpointTicksRemaining_d0, 0);
    }

    //LAB_800d4f78
    cam.flags_11c |= UPDATE_VIEWPOINT;
    cam.viewpointCallbackIndex_120 = 13;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d4fbcL)
  public static void FUN_800d4fbc(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    cam.viewpointBobj_f4 = bobj;
    cam.viewpointBaseTranslation_94.x = calculateCameraValue(false, 6, 0, bobj);
    cam.viewpointBaseTranslation_94.y = calculateCameraValue(false, 6, 1, bobj);
    cam.viewpointBaseTranslation_94.z = calculateCameraValue(false, 6, 2, bobj);

    if(a5 == 0) {
      //LAB_800d5078
      cam.viewpointTicksRemaining_d0 = ticks;

      if(ticks != 0) {
        cam.stepX_b0 = (x - cam.viewpointBaseTranslation_94.x) / ticks;
        cam.stepY_bc = (y - cam.viewpointBaseTranslation_94.y) / ticks;
        cam.stepZ_c8 = (z - cam.viewpointBaseTranslation_94.z) / ticks;
      } else {
        cam.stepX_b0 = -1;
        cam.stepY_bc = -1;
        cam.stepZ_c8 = -1;
      }
    } else if(a5 == 1) {
      //LAB_800d50c4
      final float x2 = x - cam.viewpointBaseTranslation_94.x;
      final float y2 = y - cam.viewpointBaseTranslation_94.y;
      final float z2 = z - cam.viewpointBaseTranslation_94.z;
      final float v0 = Math.sqrt(z2 * z2 + y2 * y2 + x2 * x2) / ticks;
      cam.viewpointTicksRemaining_d0 = (int)v0;

      if(v0 != 0) {
        cam.stepX_b0 = (x - cam.viewpointBaseTranslation_94.x) / v0;
        cam.stepY_bc = (y - cam.viewpointBaseTranslation_94.y) / v0;
        cam.stepZ_c8 = (z - cam.viewpointBaseTranslation_94.z) / v0;
      } else {
        cam.stepX_b0 = -1;
        cam.stepY_bc = -1;
        cam.stepZ_c8 = -1;
      }
    }

    //LAB_800d5150
    //LAB_800d5158
    cam.flags_11c |= UPDATE_VIEWPOINT;
    cam.viewpointCallbackIndex_120 = 14;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d519cL)
  public static void FUN_800d519c(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    cam.viewpointBobj_f4 = bobj;
    cam.viewpointAngleX_ac = calculateCameraValue(false, 7, 0, bobj);
    cam.viewpointAngleY_b8 = calculateCameraValue(false, 7, 1, bobj);
    cam.viewpointDeltaMagnitude_a0 = calculateCameraValue(false, 7, 2, bobj);

    if(a5 == 0) {
      //LAB_800d525c
      cam.viewpointTicksRemaining_d0 = ticks;
      cam.stepX_b0 = calculateStep(0, cam.viewpointAngleX_ac, x, ticks, stepType & 3);
      cam.stepY_bc = calculateStep(1, cam.viewpointAngleY_b8, y, ticks, stepType >> 2 & 3);
      cam.stepZ_c8 = calculateStep(2, cam.viewpointDeltaMagnitude_a0, z, ticks, 0);
    } else if(a5 == 1) {
      //LAB_800d52b4
      final float x2 = calculateDifference(0, cam.viewpointAngleX_ac, x, stepType & 3);
      final float y2 = calculateDifference(1, cam.viewpointAngleY_b8, y, stepType >> 2 & 3);
      final float z2 = calculateDifference(2, cam.viewpointDeltaMagnitude_a0, z, 0);
      cam.viewpointTicksRemaining_d0 = (int)(Math.sqrt(x2 * x2 + y2 * y2 + z2 * z2) / ticks);
      cam.stepX_b0 = calculateStep(0, cam.viewpointAngleX_ac, x, cam.viewpointTicksRemaining_d0, stepType & 3);
      cam.stepY_bc = calculateStep(1, cam.viewpointAngleY_b8, y, cam.viewpointTicksRemaining_d0, stepType >> 2 & 3);
      cam.stepZ_c8 = calculateStep(2, cam.viewpointDeltaMagnitude_a0, z, cam.viewpointTicksRemaining_d0, 0);
    }

    //LAB_800d53a0
    cam.flags_11c |= UPDATE_VIEWPOINT;
    cam.viewpointCallbackIndex_120 = 15;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d53e4L)
  public static void FUN_800d53e4(final float x, final float y, final float z, final int initialStepZ, final int finalStepZ, final int stepType, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    final float dx = x - camera_800c67f0.rview2_00.viewpoint_00.x;
    final float dy = y - camera_800c67f0.rview2_00.viewpoint_00.y;
    final float dz = z - camera_800c67f0.rview2_00.viewpoint_00.z;
    cam.vec_d4.z = Math.sqrt(dx * dx + dy * dy + dz * dz);
    cam.vec_d4.x = MathHelper.floorMod(MathHelper.atan2(dz, dx), MathHelper.TWO_PI);
    final float ticks = cam.vec_d4.z * 2.0f / (finalStepZ + initialStepZ);
    final float zAccel = (finalStepZ - initialStepZ) / ticks;
    cam.vec_d4.y = MathHelper.floorMod(MathHelper.atan2(dy, Math.sqrt(dx * dx + dz * dz)), MathHelper.TWO_PI);
    cam.stepZ_a4 = initialStepZ;
    cam.viewpointTargetTranslation_e8.set(x, y, z);
    cam.flags_11c |= UPDATE_VIEWPOINT;
    cam.viewpointCallbackIndex_120 = 16;
    cam.viewpointTicksRemaining_d0 = (int)ticks;
    cam.stepZAcceleration_b4 = zAccel;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d5ec8L)
  public static void FUN_800d5ec8(final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    final float dx = x - cam.rview2_00.viewpoint_00.x;
    final float dy = y - cam.rview2_00.viewpoint_00.y;
    final float dz = z - cam.rview2_00.viewpoint_00.z;
    cam.viewpointTicksRemaining_d0 = ticks;
    cam.vec_d4.x = MathHelper.floorMod(MathHelper.atan2(dz, dx), MathHelper.TWO_PI);
    cam.vec_d4.y = MathHelper.floorMod(MathHelper.atan2(dy, Math.sqrt(dx * dx + dz * dz)), MathHelper.TWO_PI);
    cam.vec_d4.z = Math.sqrt(dx * dx + dy * dy + dz * dz);

    final FloatRef initialStepZ = new FloatRef();
    final FloatRef finalStepZ = new FloatRef();
    setInitialAndFinalCameraVelocities(stepSmoothingMode, stepZ, cam.vec_d4.z, ticks, initialStepZ, finalStepZ);

    //LAB_800d6030
    //LAB_800d6038
    cam.stepZ_a4 = initialStepZ.get();
    cam.stepZAcceleration_b4 = (finalStepZ.get() - initialStepZ.get()) / cam.viewpointTicksRemaining_d0;
    cam.viewpointTargetTranslation_e8.set(x, y, z);
    cam.flags_11c |= UPDATE_VIEWPOINT;
    cam.viewpointCallbackIndex_120 = 16;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d60b0L)
  public static void FUN_800d60b0(final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    cam.viewpointAngleX_ac = calculateCameraValue(false, 1, 0, null);
    cam.viewpointAngleY_b8 = calculateCameraValue(false, 1, 1, null);
    cam.viewpointDeltaMagnitude_a0 = calculateCameraValue(false, 1, 2, null);
    final float deltaX = calculateDifference(0, cam.viewpointAngleX_ac, x, stepType & 3);
    final float deltaY = calculateDifference(1, cam.viewpointAngleY_b8, y, stepType >> 2 & 3);
    cam.viewpointTicksRemaining_d0 = ticks;
    cam.vec_d4.x = MathHelper.floorMod(MathHelper.atan2(deltaY, deltaX), MathHelper.TWO_PI);
    cam.vec_d4.y = 0.0f;
    cam.vec_d4.z = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

    final FloatRef initialStepZ = new FloatRef();
    final FloatRef finalStepZ = new FloatRef();
    setInitialAndFinalCameraVelocities(stepSmoothingMode, stepZ, cam.vec_d4.z, ticks, initialStepZ, finalStepZ);

    //LAB_800d6238
    //LAB_800d6240
    cam.stepZ_e0 = finalStepZ.get();
    cam.viewpointTargetTranslation_e8.set(x, y, z);
    cam.stepZAcceleration_e4 = (initialStepZ.get() - finalStepZ.get()) / cam.viewpointTicksRemaining_d0;
    cam.stepZ_a4 = calculateDifference(2, cam.viewpointDeltaMagnitude_a0, z, 0) / cam.viewpointTicksRemaining_d0;
    cam.flags_11c |= UPDATE_VIEWPOINT;
    cam.viewpointCallbackIndex_120 = 17;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d62d8L)
  public static void FUN_800d62d8(final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    cam.viewpointBaseTranslation_94.x = calculateCameraValue(false, 4, 0, null);
    cam.viewpointBaseTranslation_94.y = calculateCameraValue(false, 4, 1, null);
    cam.viewpointBaseTranslation_94.z = calculateCameraValue(false, 4, 2, null);
    unused_800c67d8.set(cam.viewpointBaseTranslation_94);
    final float dx = x - cam.viewpointBaseTranslation_94.x;
    final float dy = y - cam.viewpointBaseTranslation_94.y;
    final float dz = z - cam.viewpointBaseTranslation_94.z;
    cam.viewpointTicksRemaining_d0 = ticks;
    cam.vec_d4.x = MathHelper.floorMod(MathHelper.atan2(dz, dx), MathHelper.TWO_PI);
    cam.vec_d4.y = MathHelper.floorMod(MathHelper.atan2(dy, Math.sqrt(dx * dx + dz * dz)), MathHelper.TWO_PI);
    cam.vec_d4.z = Math.sqrt(dx * dx + dy * dy + dz * dz);
    final FloatRef initialStepZ = new FloatRef();
    final FloatRef finalStepZ = new FloatRef();
    setInitialAndFinalCameraVelocities(stepSmoothingMode, stepZ, cam.vec_d4.z, ticks, initialStepZ, finalStepZ);
    cam.viewpointTargetTranslation_e8.set(x, y, z);
    cam.stepZ_a4 = initialStepZ.get();
    cam.stepZAcceleration_b4 = (finalStepZ.get() - initialStepZ.get()) / cam.viewpointTicksRemaining_d0;
    cam.flags_11c |= UPDATE_VIEWPOINT;
    cam.viewpointCallbackIndex_120 = 20;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d64e4L)
  public static void FUN_800d64e4(final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    cam.viewpointAngleX_ac = calculateCameraValue(false, 5, 0, null);
    cam.viewpointAngleY_b8 = calculateCameraValue(false, 5, 1, null);
    cam.viewpointDeltaMagnitude_a0 = calculateCameraValue(false, 5, 2, null);
    final float deltaX = calculateDifference(0, cam.viewpointAngleX_ac, x, stepType & 3);
    final float deltaY = calculateDifference(1, cam.viewpointAngleY_b8, y, stepType >> 2 & 3);
    cam.viewpointTicksRemaining_d0 = ticks;
    cam.vec_d4.x = MathHelper.floorMod(MathHelper.atan2(deltaY, deltaX), MathHelper.TWO_PI);
    cam.vec_d4.y = 0.0f;
    cam.vec_d4.z = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

    final FloatRef initialStepZ = new FloatRef();
    final FloatRef finalStepZ = new FloatRef();
    setInitialAndFinalCameraVelocities(stepSmoothingMode, stepZ, cam.vec_d4.z, ticks, initialStepZ, finalStepZ);

    //LAB_800d666c
    //LAB_800d6674
    cam.stepZ_e0 = initialStepZ.get();
    cam.viewpointTargetTranslation_e8.set(x, y, z);
    cam.stepZAcceleration_e4 = (finalStepZ.get() - initialStepZ.get()) / cam.viewpointTicksRemaining_d0;
    cam.stepZ_a4 = calculateDifference(2, cam.viewpointDeltaMagnitude_a0, z, 0) / cam.viewpointTicksRemaining_d0;
    cam.flags_11c |= UPDATE_VIEWPOINT;
    cam.viewpointCallbackIndex_120 = 21;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d670cL)
  public static void FUN_800d670c(final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    cam.viewpointBobj_f4 = bobj;
    cam.viewpointBaseTranslation_94.x = calculateCameraValue(false, 6, 0, bobj);
    cam.viewpointBaseTranslation_94.y = calculateCameraValue(false, 6, 1, bobj);
    cam.viewpointBaseTranslation_94.z = calculateCameraValue(false, 6, 2, bobj);
    final float dx = x - cam.viewpointBaseTranslation_94.x;
    final float dy = y - cam.viewpointBaseTranslation_94.y;
    final float dz = z - cam.viewpointBaseTranslation_94.z;
    cam.viewpointTicksRemaining_d0 = ticks;
    cam.vec_d4.x = MathHelper.floorMod(MathHelper.atan2(dz, dx), MathHelper.TWO_PI);
    cam.vec_d4.y = MathHelper.floorMod(MathHelper.atan2(dy, Math.sqrt(dx * dx + dz * dz)), MathHelper.TWO_PI);
    cam.vec_d4.z = Math.sqrt(dx * dx + dy * dy + dz * dz);

    final FloatRef initialStepZ = new FloatRef();
    final FloatRef finalStepZ = new FloatRef();
    setInitialAndFinalCameraVelocities(stepSmoothingMode, stepZ, cam.vec_d4.z, ticks, initialStepZ, finalStepZ);

    //LAB_800d68e0
    //LAB_800d68e8
    cam.stepZ_a4 = initialStepZ.get();
    cam.viewpointTargetTranslation_e8.set(x, y, z);
    cam.stepZAcceleration_b4 = (finalStepZ.get() - initialStepZ.get()) / cam.viewpointTicksRemaining_d0;
    cam.flags_11c |= UPDATE_VIEWPOINT;
    cam.viewpointCallbackIndex_120 = 22;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d6960L)
  public static void FUN_800d6960(final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    cam.viewpointBobj_f4 = bobj;
    cam.viewpointAngleX_ac = calculateCameraValue(false, 7, 0, bobj);
    cam.viewpointAngleY_b8 = calculateCameraValue(false, 7, 1, bobj);
    cam.viewpointDeltaMagnitude_a0 = calculateCameraValue(false, 7, 2, bobj);
    final float s1 = calculateDifference(0, cam.viewpointAngleX_ac, x, stepType & 3);
    final float s0 = calculateDifference(1, cam.viewpointAngleY_b8, y, stepType >> 2 & 3);
    cam.viewpointTicksRemaining_d0 = ticks;
    cam.vec_d4.x = MathHelper.floorMod(MathHelper.atan2(s0, s1), MathHelper.TWO_PI);
    cam.vec_d4.y = 0.0f;
    cam.vec_d4.z = Math.sqrt(s1 * s1 + s0 * s0);
    final FloatRef initialStepZ = new FloatRef();
    final FloatRef finalStepZ = new FloatRef();
    setInitialAndFinalCameraVelocities(stepSmoothingMode, stepZ, cam.vec_d4.z, ticks, initialStepZ, finalStepZ);

    //LAB_800d6af0
    //LAB_800d6af8
    cam.stepZ_e0 = initialStepZ.get();
    cam.viewpointTargetTranslation_e8.set(x, y, z);
    cam.stepZAcceleration_e4 = (finalStepZ.get() - initialStepZ.get()) / cam.viewpointTicksRemaining_d0;
    cam.stepZ_a4 = calculateDifference(2, cam.viewpointDeltaMagnitude_a0, z, 0) / cam.viewpointTicksRemaining_d0;
    cam.flags_11c |= UPDATE_VIEWPOINT;
    cam.viewpointCallbackIndex_120 = 23;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d6b90L)
  public static void FUN_800d6b90(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    cam.refpointBaseTranslation_20.set(cam.rview2_00.refpoint_0c);

    if(a5 == 0) {
      //LAB_800d6c04
      cam.refpointTicksRemaining_5c = ticks;

      // Retail bug: divide by 0 is possible here - the processor sets LO to -1 in this case
      if(ticks != 0) {
        cam.stepX_3c = (x - cam.refpointBaseTranslation_20.x) / ticks;
        cam.stepY_48 = (y - cam.refpointBaseTranslation_20.y) / ticks;
        cam.stepZ_54 = (z - cam.refpointBaseTranslation_20.z) / ticks;
      } else {
        cam.stepX_3c = -1;
        cam.stepY_48 = -1;
        cam.stepZ_54 = -1;
      }
    } else if(a5 == 1) {
      throw new RuntimeException("t0/t1 undefined");
    }

    //LAB_800d6cdc
    //LAB_800d6ce4
    cam.flags_11c |= UPDATE_REFPOINT;
    cam.refpointCallbackIndex_121 = 8;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d6d18L)
  public static void FUN_800d6d18(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    cam.refpointAngleX_38 = calculateCameraValue(true, 1, 0, null);
    cam.refpointAngleY_44 = calculateCameraValue(true, 1, 1, null);
    cam.refpointDeltaMagnitude_2c = calculateCameraValue(true, 1, 2, null);

    if(a5 == 0) {
      //LAB_800d6dd0
      cam.refpointTicksRemaining_5c = ticks;
      cam.stepX_3c = calculateStep(0, cam.refpointAngleX_38, x, ticks, stepType & 3);
      cam.stepY_48 = calculateStep(1, cam.refpointAngleY_44, y, ticks, stepType >> 2 & 3);
      cam.stepZ_54 = calculateStep(2, cam.refpointDeltaMagnitude_2c, z, ticks, 0);
    } else if(a5 == 1) {
      //LAB_800d6e28
      final float x2 = calculateDifference(0, cam.refpointAngleX_38, x, stepType & 3);
      final float y2 = calculateDifference(1, cam.refpointAngleY_44, y, stepType >> 2 & 3);
      final float z2 = calculateDifference(2, cam.refpointDeltaMagnitude_2c, z, 0);
      cam.refpointTicksRemaining_5c = (int)(Math.sqrt(x2 * x2 + y2 * y2 + z2 * z2) / ticks);
      cam.stepX_3c = calculateStep(0, cam.refpointAngleX_38, x, cam.refpointTicksRemaining_5c, stepType & 3);
      cam.stepY_48 = calculateStep(1, cam.refpointAngleY_44, y, cam.refpointTicksRemaining_5c, stepType >> 2 & 3);
      cam.stepZ_54 = calculateStep(2, cam.refpointDeltaMagnitude_2c, z, cam.refpointTicksRemaining_5c, 0);
    }

    //LAB_800d6f14
    cam.flags_11c |= UPDATE_REFPOINT;
    cam.refpointCallbackIndex_121 = 9;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d6f58L)
  public static void FUN_800d6f58(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    cam.refpointBaseTranslation_20.x = calculateCameraValue(true, 2, 0, null);
    cam.refpointBaseTranslation_20.y = calculateCameraValue(true, 2, 1, null);
    cam.refpointBaseTranslation_20.z = calculateCameraValue(true, 2, 2, null);

    if(a5 == 0) {
      //LAB_800d7008
      cam.refpointTicksRemaining_5c = ticks;
      cam.stepX_3c = (x - cam.refpointBaseTranslation_20.x) / ticks;
      cam.stepY_48 = (y - cam.refpointBaseTranslation_20.y) / ticks;
      cam.stepZ_54 = (z - cam.refpointBaseTranslation_20.z) / ticks;
    } else if(a5 == 1) {
      throw new RuntimeException("Broken code");
    }

    //LAB_800d70e0
    //LAB_800d70e8
    cam.flags_11c |= UPDATE_REFPOINT;
    cam.refpointCallbackIndex_121 = 10;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d7128L)
  public static void FUN_800d7128(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    cam.refpointAngleX_38 = calculateCameraValue(true, 3, 0, null);
    cam.refpointAngleY_44 = calculateCameraValue(true, 3, 1, null);
    cam.refpointDeltaMagnitude_2c = calculateCameraValue(true, 3, 2, null);

    if(a5 == 0) {
      //LAB_800d71e0
      cam.refpointTicksRemaining_5c = ticks;
      cam.stepX_3c = calculateStep(0, cam.refpointAngleX_38, x, ticks, stepType & 3);
      cam.stepY_48 = calculateStep(1, cam.refpointAngleY_44, y, ticks, stepType >> 2 & 3);
      cam.stepZ_54 = calculateStep(2, cam.refpointDeltaMagnitude_2c, z, ticks, 0);
    } else if(a5 == 1) {
      //LAB_800d7238
      final float x2 = calculateDifference(0, cam.refpointAngleX_38, x, stepType & 3);
      final float y2 = calculateDifference(1, cam.refpointAngleY_44, y, stepType >> 2 & 3);
      final float z2 = calculateDifference(2, cam.refpointDeltaMagnitude_2c, z, 0);
      cam.refpointTicksRemaining_5c = (int)(Math.sqrt(x2 * x2 + y2 * y2 + z2 * z2) / ticks);
      cam.stepX_3c = calculateStep(0, cam.refpointAngleX_38, x, cam.refpointTicksRemaining_5c, stepType & 3);
      cam.stepY_48 = calculateStep(1, cam.refpointAngleY_44, y, cam.refpointTicksRemaining_5c, stepType >> 2 & 3);
      cam.stepZ_54 = calculateStep(2, cam.refpointDeltaMagnitude_2c, z, cam.refpointTicksRemaining_5c, 0);
    }

    //LAB_800d7324
    cam.flags_11c |= UPDATE_REFPOINT;
    cam.refpointCallbackIndex_121 = 11;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d7368L)
  public static void FUN_800d7368(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    cam.refpointBobj_80 = bobj;
    cam.refpointBaseTranslation_20.x = calculateCameraValue(true, 6, 0, bobj);
    cam.refpointBaseTranslation_20.y = calculateCameraValue(true, 6, 1, bobj);
    cam.refpointBaseTranslation_20.z = calculateCameraValue(true, 6, 2, bobj);

    if(a5 == 0) {
      //LAB_800d7424
      cam.refpointTicksRemaining_5c = ticks;
      cam.stepX_3c = MathHelper.safeDiv(x - cam.refpointBaseTranslation_20.x, ticks);
      cam.stepY_48 = MathHelper.safeDiv(y - cam.refpointBaseTranslation_20.y, ticks);
      cam.stepZ_54 = MathHelper.safeDiv(z - cam.refpointBaseTranslation_20.z, ticks);
    } else if(a5 == 1) {
      throw new RuntimeException("Undefined s5/s6");
    }

    //LAB_800d74fc
    //LAB_800d7504
    cam.flags_11c |= UPDATE_REFPOINT;
    cam.refpointCallbackIndex_121 = 14;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d7548L)
  public static void FUN_800d7548(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    cam.refpointBobj_80 = bobj;
    cam.refpointAngleX_38 = calculateCameraValue(true, 7, 0, bobj);
    cam.refpointAngleY_44 = calculateCameraValue(true, 7, 1, bobj);
    cam.refpointDeltaMagnitude_2c = calculateCameraValue(true, 7, 2, bobj);

    if(a5 == 0) {
      //LAB_800d7608
      cam.refpointTicksRemaining_5c = ticks;
      cam.stepX_3c = calculateStep(0, cam.refpointAngleX_38, x, ticks, stepType & 3);
      cam.stepY_48 = calculateStep(1, cam.refpointAngleY_44, y, ticks, stepType >> 2 & 3);
      cam.stepZ_54 = calculateStep(2, cam.refpointDeltaMagnitude_2c, z, ticks, 0);
    } else if(a5 == 1) {
      //LAB_800d7660
      final float x2 = calculateDifference(0, cam.refpointAngleX_38, x, stepType & 3);
      final float y2 = calculateDifference(1, cam.refpointAngleY_44, y, stepType >> 2 & 3);
      final float z2 = calculateDifference(2, cam.refpointDeltaMagnitude_2c, z, 0);
      cam.refpointTicksRemaining_5c = (int)(Math.sqrt(x2 * x2 + y2 * y2 + z2 * z2) / ticks);
      cam.stepX_3c = calculateStep(0, cam.refpointAngleX_38, x, cam.refpointTicksRemaining_5c, stepType & 3);
      cam.stepY_48 = calculateStep(1, cam.refpointAngleY_44, y, cam.refpointTicksRemaining_5c, stepType >> 2 & 3);
      cam.stepZ_54 = calculateStep(2, cam.refpointDeltaMagnitude_2c, z, cam.refpointTicksRemaining_5c, 0);
    }

    //LAB_800d774c
    cam.flags_11c |= UPDATE_REFPOINT;
    cam.refpointCallbackIndex_121 = 15;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d7790L)
  public static void FUN_800d7790(final float x, final float y, final float z, final int initialStepZ, final int finalStepZ, final int stepType, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    final float dx = x - cam.rview2_00.refpoint_0c.x;
    final float dy = y - cam.rview2_00.refpoint_0c.y;
    final float dz = z - cam.rview2_00.refpoint_0c.z;
    cam.vec_60.x = MathHelper.floorMod(MathHelper.atan2(dz, dx), MathHelper.TWO_PI);
    cam.vec_60.y = MathHelper.floorMod(MathHelper.atan2(dy, Math.sqrt(dx * dx + dz * dz)), MathHelper.TWO_PI);
    cam.vec_60.z = Math.sqrt(dx * dx + dy * dy + dz * dz);
    cam.stepZ_30 = initialStepZ;
    cam.refpointTargetTranslation_74.set(x, y, z);
    cam.flags_11c |= UPDATE_REFPOINT;
    cam.refpointCallbackIndex_121 = 16;
    cam.refpointTicksRemaining_5c = (int)(cam.vec_60.z * 2.0f / (finalStepZ + initialStepZ));

    if(cam.refpointTicksRemaining_5c > 0) {
      cam.stepZAcceleration_40 = (finalStepZ - initialStepZ) / (float)cam.refpointTicksRemaining_5c;
    } else {
      cam.stepZAcceleration_40 = -1;
    }
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d7920L)
  public static void FUN_800d7920(final float x, final float y, final float z, final int initialStepZ, final int finalStepZ, final int stepType, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    cam.refpointAngleX_38 = calculateCameraValue(true, 1, 0, null);
    cam.refpointAngleY_44 = calculateCameraValue(true, 1, 1, null);
    cam.refpointDeltaMagnitude_2c = calculateCameraValue(true, 1, 2, null);
    final float deltaX = calculateDifference(0, cam.refpointAngleX_38, x, stepType & 3);
    final float deltaY = calculateDifference(1, cam.refpointAngleY_44, y, stepType >> 2 & 3);
    cam.vec_60.x = MathHelper.floorMod(MathHelper.atan2(deltaY, deltaX), MathHelper.TWO_PI);
    cam.vec_60.y = 0.0f;
    cam.vec_60.z = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    cam.stepZ_6c = initialStepZ;
    cam.refpointTargetTranslation_74.set(x, y, z);
    cam.refpointTicksRemaining_5c = (int)(cam.vec_60.z * 2.0f / (finalStepZ + initialStepZ));
    cam.refpointCallbackIndex_121 = 17;
    cam.flags_11c |= UPDATE_REFPOINT;

    if(cam.refpointTicksRemaining_5c > 0) {
      cam.stepZ_30 = calculateDifference(2, cam.refpointDeltaMagnitude_2c, z, 0) / cam.refpointTicksRemaining_5c;
      cam.stepZAcceleration_70 = (finalStepZ - initialStepZ) / (float)cam.refpointTicksRemaining_5c;
    } else {
      cam.stepZ_30 = -1;
      cam.stepZAcceleration_70 = -1;
    }
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d7aecL)
  public static void FUN_800d7aec(final float x, final float y, final float z, final int initialStepZ, final int finalStepZ, final int stepType, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    cam.refpointBaseTranslation_20.x = calculateCameraValue(true, 2, 0, null);
    cam.refpointBaseTranslation_20.y = calculateCameraValue(true, 2, 1, null);
    cam.refpointBaseTranslation_20.z = calculateCameraValue(true, 2, 2, null);
    final float dx = x - cam.refpointBaseTranslation_20.x;
    final float dy = y - cam.refpointBaseTranslation_20.y;
    final float dz = z - cam.refpointBaseTranslation_20.z;
    cam.vec_60.x = MathHelper.floorMod(MathHelper.atan2(dz, dx), MathHelper.TWO_PI);
    cam.vec_60.y = MathHelper.floorMod(MathHelper.atan2(dy, Math.sqrt(dx * dx + dz * dz)), MathHelper.TWO_PI);
    cam.vec_60.z = Math.sqrt(dx * dx + dy * dy + dz * dz);
    cam.stepZ_30 = initialStepZ;
    cam.refpointTargetTranslation_74.set(x, y, z);
    cam.flags_11c |= UPDATE_REFPOINT;
    cam.refpointCallbackIndex_121 = 18;
    cam.refpointTicksRemaining_5c = (int)(cam.vec_60.z * 2.0f / (finalStepZ + initialStepZ));

    if(cam.refpointTicksRemaining_5c > 0) {
      cam.stepZAcceleration_40 = (finalStepZ - initialStepZ) / (float)cam.refpointTicksRemaining_5c;
    } else {
      cam.stepZAcceleration_40 = -1;
    }
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d7cdcL)
  public static void FUN_800d7cdc(final float x, final float y, final float z, final int initialStepZ, final int finalStepZ, final int stepType, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    cam.refpointAngleX_38 = calculateCameraValue(true, 3, 0, null);
    cam.refpointAngleY_44 = calculateCameraValue(true, 3, 1, null);
    cam.refpointDeltaMagnitude_2c = calculateCameraValue(true, 3, 2, null);
    final float deltaX = calculateDifference(0, cam.refpointAngleX_38, x, stepType & 0x3);
    final float deltaY = calculateDifference(1, cam.refpointAngleY_44, y, stepType >> 2 & 0x3);
    cam.vec_60.x = MathHelper.floorMod(MathHelper.atan2(deltaY, deltaX), MathHelper.TWO_PI);
    cam.vec_60.y = 0.0f;
    cam.vec_60.z = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    cam.refpointTicksRemaining_5c = (int)(cam.vec_60.z * 2.0f / (finalStepZ + initialStepZ));
    cam.stepZ_6c = initialStepZ;
    cam.refpointTargetTranslation_74.set(x, y, z);
    cam.flags_11c |= UPDATE_REFPOINT;
    cam.refpointCallbackIndex_121 = 19;

    if(cam.refpointTicksRemaining_5c > 0) {
      cam.stepZ_30 = calculateDifference(2, cam.refpointDeltaMagnitude_2c, z, 0) / cam.refpointTicksRemaining_5c;
      cam.stepZAcceleration_70 = (finalStepZ - initialStepZ) / (float)cam.refpointTicksRemaining_5c;
    } else {
      cam.stepZ_30 = -1;
      cam.stepZAcceleration_70 = -1;
    }
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d8274L)
  public static void FUN_800d8274(final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    final float dx = x - cam.rview2_00.refpoint_0c.x;
    final float dy = y - cam.rview2_00.refpoint_0c.y;
    final float dz = z - cam.rview2_00.refpoint_0c.z;
    cam.refpointTicksRemaining_5c = ticks;
    cam.vec_60.x = MathHelper.floorMod(MathHelper.atan2(dz, dx), MathHelper.TWO_PI);
    cam.vec_60.y = MathHelper.floorMod(MathHelper.atan2(dy, Math.sqrt(dx * dx + dz * dz)), MathHelper.TWO_PI);
    cam.vec_60.z = Math.sqrt(dx * dx + dy * dy + dz * dz);
    final FloatRef initialStepZ = new FloatRef();
    final FloatRef finalStepZ = new FloatRef();
    setInitialAndFinalCameraVelocities(stepSmoothingMode, stepZ, cam.vec_60.z, cam.refpointTicksRemaining_5c, initialStepZ, finalStepZ);
    cam.stepZ_30 = initialStepZ.get();
    cam.stepZAcceleration_40 = (finalStepZ.get() - initialStepZ.get()) / cam.refpointTicksRemaining_5c;
    cam.refpointTargetTranslation_74.set(x, y, z);
    cam.flags_11c |= UPDATE_REFPOINT;
    cam.refpointCallbackIndex_121 = 16;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d8424L)
  public static void FUN_800d8424(final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    cam.refpointAngleX_38 = calculateCameraValue(true, 1, 0, null);
    cam.refpointAngleY_44 = calculateCameraValue(true, 1, 1, null);
    cam.refpointDeltaMagnitude_2c = calculateCameraValue(true, 1, 2, null);
    final float deltaX = calculateDifference(0, cam.refpointAngleX_38, x, stepType & 3);
    final float deltaY = calculateDifference(1, cam.refpointAngleY_44, y, stepType >> 2 & 3);
    cam.refpointTicksRemaining_5c = ticks;
    cam.vec_60.x = MathHelper.floorMod(MathHelper.atan2(deltaY, deltaX), MathHelper.TWO_PI);
    cam.vec_60.y = 0.0f;
    cam.vec_60.z = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    final FloatRef initialStepZ = new FloatRef();
    final FloatRef finalStepZ = new FloatRef();
    setInitialAndFinalCameraVelocities(stepSmoothingMode, stepZ, cam.vec_60.z, ticks, initialStepZ, finalStepZ);
    cam.stepZ_6c = initialStepZ.get();
    cam.stepZAcceleration_70 = (finalStepZ.get() - initialStepZ.get()) / cam.refpointTicksRemaining_5c;
    cam.refpointTargetTranslation_74.set(x, y, z);
    cam.flags_11c |= UPDATE_REFPOINT;
    cam.refpointCallbackIndex_121 = 17;
    cam.stepZ_30 = calculateDifference(2, cam.refpointDeltaMagnitude_2c, z, 0) / cam.refpointTicksRemaining_5c;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d8614L)
  public static void FUN_800d8614(final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    cam.refpointBaseTranslation_20.x = calculateCameraValue(true, 2, 0, null);
    cam.refpointBaseTranslation_20.y = calculateCameraValue(true, 2, 1, null);
    cam.refpointBaseTranslation_20.z = calculateCameraValue(true, 2, 2, null);
    final float dx = x - cam.refpointBaseTranslation_20.x;
    final float dy = y - cam.refpointBaseTranslation_20.y;
    final float dz = z - cam.refpointBaseTranslation_20.z;
    cam.vec_60.x = MathHelper.floorMod(MathHelper.atan2(dz, dx), MathHelper.TWO_PI);
    cam.vec_60.y = MathHelper.floorMod(MathHelper.atan2(dy, Math.sqrt(dx * dx + dz * dz)), MathHelper.TWO_PI);
    cam.vec_60.z = Math.sqrt(dx * dx + dy * dy + dz * dz);
    cam.refpointTicksRemaining_5c = ticks;
    final FloatRef initialStepZ = new FloatRef();
    final FloatRef finalStepZ = new FloatRef();
    setInitialAndFinalCameraVelocities(stepSmoothingMode, stepZ, cam.vec_60.z, ticks, initialStepZ, finalStepZ);
    cam.stepZ_30 = initialStepZ.get();
    cam.stepZAcceleration_40 = (finalStepZ.get() - initialStepZ.get()) / cam.refpointTicksRemaining_5c;
    cam.refpointTargetTranslation_74.set(x, y, z);
    cam.flags_11c |= UPDATE_REFPOINT;
    cam.refpointCallbackIndex_121 = 18;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d8808L)
  public static void FUN_800d8808(final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    cam.refpointAngleX_38 = calculateCameraValue(true, 3, 0, null);
    cam.refpointAngleY_44 = calculateCameraValue(true, 3, 1, null);
    cam.refpointDeltaMagnitude_2c = calculateCameraValue(true, 3, 2, null);
    final float deltaX = calculateDifference(0, cam.refpointAngleX_38, x, stepType & 3);
    final float deltaY = calculateDifference(1, cam.refpointAngleY_44, y, stepType >> 2 & 3);
    cam.vec_60.x = MathHelper.floorMod(MathHelper.atan2(deltaY, deltaX), MathHelper.TWO_PI);
    cam.vec_60.y = 0.0f;
    cam.vec_60.z = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    cam.refpointTicksRemaining_5c = ticks;
    final FloatRef initialStepZ = new FloatRef();
    final FloatRef finalStepZ = new FloatRef();
    setInitialAndFinalCameraVelocities(stepSmoothingMode, stepZ, cam.vec_60.z, ticks, initialStepZ, finalStepZ);
    cam.stepZ_30 = calculateDifference(2, cam.refpointDeltaMagnitude_2c, z, 0) / cam.refpointTicksRemaining_5c;
    cam.stepZ_6c = initialStepZ.get();
    cam.stepZAcceleration_70 = (finalStepZ.get() - initialStepZ.get()) / cam.refpointTicksRemaining_5c;
    cam.refpointTargetTranslation_74.set(x, y, z);
    cam.flags_11c |= UPDATE_REFPOINT;
    cam.refpointCallbackIndex_121 = 19;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d89f8L)
  public static void FUN_800d89f8(final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    cam.refpointBobj_80 = bobj;
    cam.refpointBaseTranslation_20.x = calculateCameraValue(true, 6, 0, bobj);
    cam.refpointBaseTranslation_20.y = calculateCameraValue(true, 6, 1, bobj);
    cam.refpointBaseTranslation_20.z = calculateCameraValue(true, 6, 2, bobj);
    final float dx = x - cam.refpointBaseTranslation_20.x;
    final float dy = y - cam.refpointBaseTranslation_20.y;
    final float dz = z - cam.refpointBaseTranslation_20.z;
    cam.refpointTicksRemaining_5c = ticks;
    cam.vec_60.x = MathHelper.floorMod(MathHelper.atan2(dz, dx), MathHelper.TWO_PI);
    cam.vec_60.y = MathHelper.floorMod(MathHelper.atan2(dy, Math.sqrt(dx * dx + dz * dz)), MathHelper.TWO_PI);
    cam.vec_60.z = Math.sqrt(dx * dx + dy * dy + dz * dz);
    final FloatRef initialStepZ = new FloatRef();
    final FloatRef finalStepZ = new FloatRef();
    setInitialAndFinalCameraVelocities(stepSmoothingMode, stepZ, cam.vec_60.z, ticks, initialStepZ, finalStepZ);
    cam.refpointTargetTranslation_74.set(x, y, z);
    cam.stepZ_30 = initialStepZ.get();
    cam.stepZAcceleration_40 = (finalStepZ.get() - initialStepZ.get()) / cam.refpointTicksRemaining_5c;
    cam.flags_11c |= UPDATE_REFPOINT;
    cam.refpointCallbackIndex_121 = 22;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800d8bf4L)
  public static void FUN_800d8bf4(final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    cam.refpointBobj_80 = bobj;
    cam.refpointAngleX_38 = calculateCameraValue(true, 7, 0, bobj);
    cam.refpointAngleY_44 = calculateCameraValue(true, 7, 1, bobj);
    cam.refpointDeltaMagnitude_2c = calculateCameraValue(true, 7, 2, bobj);
    final float s2 = calculateDifference(0, cam.refpointAngleX_38, x, stepType & 3);
    final float s0 = calculateDifference(1, cam.refpointAngleY_44, y, stepType >> 2 & 3);
    cam.vec_60.x = MathHelper.floorMod(MathHelper.atan2(s0, s2), MathHelper.TWO_PI);
    cam.vec_60.y = 0.0f;
    cam.vec_60.z = Math.sqrt(s2 * s2 + s0 * s0);
    cam.refpointTicksRemaining_5c = ticks;
    final FloatRef initialStepZ = new FloatRef();
    final FloatRef finalStepZ = new FloatRef();
    setInitialAndFinalCameraVelocities(stepSmoothingMode, stepZ, cam.vec_60.z, ticks, initialStepZ, finalStepZ);
    cam.stepZ_30 = calculateDifference(2, cam.refpointDeltaMagnitude_2c, z, 0) / cam.refpointTicksRemaining_5c;
    cam.refpointTargetTranslation_74.set(x, y, z);
    cam.stepZ_6c = initialStepZ.get();
    cam.stepZAcceleration_70 = (finalStepZ.get() - initialStepZ.get()) / cam.refpointTicksRemaining_5c;
    cam.flags_11c |= UPDATE_REFPOINT;
    cam.refpointCallbackIndex_121 = 23;
  }

  @ScriptDescription("Causes the battle camera projection plane distance to begin moving")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "How the camera should move")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "newDistance", description = "The new projection plane distance")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "frames", description = "The number of frames it should take to change the distance")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stepZ1")
  @Method(0x800d8decL)
  public static FlowControl scriptMoveCameraProjectionPlane(final RunningScript<?> script) {
    final int mode = script.params_20[0].get();
    final float newProjectionPlaneDistance = script.params_20[1].get();
    final int projectionPlaneChangeFrames = script.params_20[2].get();
    final int stepZ1 = script.params_20[3].get();

    LOGGER.info(CAMERA, "[CAMERA] scriptMoveCameraProjectionPlane mode=%d, new=%f, frames=%d, s4=%d", mode, newProjectionPlaneDistance, projectionPlaneChangeFrames, stepZ1);

    final BattleCamera cam = camera_800c67f0;
    cam.projectionPlaneDistance_100 = getProjectionPlaneDistance();
    cam.newProjectionPlaneDistance_104 = newProjectionPlaneDistance;
    cam.projectionPlaneChanging_118 = true;

    if(newProjectionPlaneDistance < getProjectionPlaneDistance()) {
      //LAB_800d8e64
      cam.projectionPlaneMovementDirection_114 = 1;
    } else {
      cam.projectionPlaneMovementDirection_114 = 0;
    }

    //LAB_800d8e68
    final float projectionPlaneDelta = Math.abs(newProjectionPlaneDistance - getProjectionPlaneDistance());
    cam.projectionPlaneChangeFrames_108 = projectionPlaneChangeFrames;
    if(mode == 0) {
      cam.projectionPlaneDistanceStep_10c = projectionPlaneDelta / projectionPlaneChangeFrames;
      cam.projectionPlaneDistanceStepAcceleration_110 = 0.0f;
    } else {
      //LAB_800d8ea0
      final FloatRef initialStepZ = new FloatRef();
      final FloatRef finalStepZ = new FloatRef();
      setInitialAndFinalCameraVelocities(mode - 1, stepZ1, projectionPlaneDelta, projectionPlaneChangeFrames, initialStepZ, finalStepZ);
      cam.projectionPlaneDistanceStep_10c = initialStepZ.get();
      cam.projectionPlaneDistanceStepAcceleration_110 = (finalStepZ.get() - initialStepZ.get()) / projectionPlaneChangeFrames;
    }

    //LAB_800d8eec
    return FlowControl.CONTINUE;
  }

  @Method(0x800d8f10L)
  public static void FUN_800d8f10() {
    final BattleCamera cam = camera_800c67f0;

    if((cam.flags_11c & UPDATE_VIEWPOINT) != 0) {
      LOGGER.info(CAMERA, "[CAMERA] Array=_800facbc, FUN index=%d", cam.viewpointCallbackIndex_120);
      cameraViewpointMethods_800facbc[cam.viewpointCallbackIndex_120].run();
    }

    if((cam.flags_11c & UPDATE_REFPOINT) != 0) {
      LOGGER.info(CAMERA, "[CAMERA] Array=_800fad1c, FUN index=%d", cam.refpointCallbackIndex_121);
      cameraRefpointMethods_800fad1c[cam.refpointCallbackIndex_121].run();
    }

    GsSetRefView2L(camera_800c67f0.rview2_00);
    wobbleCamera();
    FUN_800d8fe0();
  }

  @Method(0x800d8fe0L)
  public static void FUN_800d8fe0() {
    final BattleCamera cam = camera_800c67f0;

    if(cam.projectionPlaneChanging_118 && cam.projectionPlaneChangeFrames_108 == 0) {
      setProjectionPlaneDistance((int)cam.projectionPlaneDistance_100);
      cam.projectionPlaneChanging_118 = false;
    }

    //LAB_800d9028
    if(cam.projectionPlaneChanging_118 && cam.projectionPlaneChangeFrames_108 != 0) {
      if(cam.projectionPlaneMovementDirection_114 == 0) {
        cam.projectionPlaneDistance_100 += cam.projectionPlaneDistanceStep_10c;
      } else {
        //LAB_800d906c
        cam.projectionPlaneDistance_100 -= cam.projectionPlaneDistanceStep_10c;
      }

      //LAB_800d907c
      cam.projectionPlaneDistanceStep_10c += cam.projectionPlaneDistanceStepAcceleration_110;
      setProjectionPlaneDistance((int)cam.projectionPlaneDistance_100);

      cam.projectionPlaneChangeFrames_108--;
      if(cam.projectionPlaneChangeFrames_108 == 0) {
        cam.projectionPlaneChanging_118 = false;
      }
    }
    //LAB_800d90b8
  }

  @Method(0x800d90c8L)
  public static void FUN_800d90c8() {
    final BattleCamera cam = camera_800c67f0;

    cam.viewpointBaseTranslation_94.x += cam.stepX_b0;
    cam.viewpointBaseTranslation_94.y += cam.stepY_bc;
    cam.viewpointBaseTranslation_94.z += cam.stepZ_c8;
    setViewpoint(cam.viewpointBaseTranslation_94.x, cam.viewpointBaseTranslation_94.y, cam.viewpointBaseTranslation_94.z);

    cam.viewpointTicksRemaining_d0--;
    if(cam.viewpointTicksRemaining_d0 <= 0) {
      cam.flags_11c &= ~UPDATE_VIEWPOINT;
      cam.viewpointMoving_122 = false;
    }
    //LAB_800d9144
  }

  @Method(0x800d9154L)
  public static void FUN_800d9154() {
    final BattleCamera cam = camera_800c67f0;

    cam.viewpointAngleX_ac += cam.stepX_b0;
    cam.viewpointAngleY_b8 += cam.stepY_bc;
    cam.viewpointDeltaMagnitude_a0 += cam.stepZ_c8;
    final Vector3f v1 = new Vector3f(cam.viewpointAngleX_ac, cam.viewpointAngleY_b8, cam.viewpointDeltaMagnitude_a0);
    FUN_800dcc94(ZERO, v1);
    setViewpoint(v1.x, v1.y, v1.z);

    cam.viewpointTicksRemaining_d0--;
    if(cam.viewpointTicksRemaining_d0 <= 0) {
      cam.flags_11c &= ~UPDATE_VIEWPOINT;
      cam.viewpointMoving_122 = false;
    }
    //LAB_800d9210
  }

  @Method(0x800d9220L)
  public static void FUN_800d9220() {
    final BattleCamera cam = camera_800c67f0;

    cam.viewpointBaseTranslation_94.x += cam.stepX_b0;
    cam.viewpointBaseTranslation_94.y += cam.stepY_bc;
    cam.viewpointBaseTranslation_94.z += cam.stepZ_c8;

    setViewpoint(
      cam.rview2_00.refpoint_0c.x + cam.viewpointBaseTranslation_94.x,
      cam.rview2_00.refpoint_0c.y + cam.viewpointBaseTranslation_94.y,
      cam.rview2_00.refpoint_0c.z + cam.viewpointBaseTranslation_94.z
    );

    cam.viewpointTicksRemaining_d0--;
    if(cam.viewpointTicksRemaining_d0 <= 0) {
      cam.viewpointCallbackIndex_120 = 4;
      cam.viewpointMoving_122 = false;
    }
    //LAB_800d92ac
  }

  @Method(0x800d92bcL)
  public static void FUN_800d92bc() {
    final BattleCamera cam = camera_800c67f0;

    cam.viewpointDeltaMagnitude_a0 += cam.stepZ_c8;
    cam.viewpointAngleX_ac += cam.stepX_b0;
    cam.viewpointAngleY_b8 += cam.stepY_bc;

    final Vector3f v1 = new Vector3f(cam.viewpointAngleX_ac, cam.viewpointAngleY_b8, cam.viewpointDeltaMagnitude_a0);
    FUN_800dcc94(cam.rview2_00.refpoint_0c, v1);
    setViewpoint(v1.x, v1.y, v1.z);

    cam.viewpointTicksRemaining_d0--;
    if(cam.viewpointTicksRemaining_d0 <= 0) {
      cam.viewpointCallbackIndex_120 = 5;
      cam.viewpointMoving_122 = false;
    }
    //LAB_800d9370
  }

  @Method(0x800d9380L)
  public static void FUN_800d9380() {
    final BattleCamera cam = camera_800c67f0;

    cam.viewpointBaseTranslation_94.x += cam.stepX_b0;
    cam.viewpointBaseTranslation_94.y += cam.stepY_bc;
    cam.viewpointBaseTranslation_94.z += cam.stepZ_c8;

    final VECTOR pos = cam.viewpointBobj_f4.getPosition();

    setViewpoint(
      pos.getX() + cam.viewpointBaseTranslation_94.x,
      pos.getY() + cam.viewpointBaseTranslation_94.y,
      pos.getZ() + cam.viewpointBaseTranslation_94.z
    );

    cam.viewpointTicksRemaining_d0--;
    if(cam.viewpointTicksRemaining_d0 <= 0) {
      cam.viewpointCallbackIndex_120 = 6;
      cam.viewpointMoving_122 = false;
    }
    //LAB_800d9428
  }

  @Method(0x800d9438L)
  public static void FUN_800d9438() {
    final BattleCamera cam = camera_800c67f0;

    cam.viewpointAngleX_ac += cam.stepX_b0;
    cam.viewpointAngleY_b8 += cam.stepY_bc;
    cam.viewpointDeltaMagnitude_a0 += cam.stepZ_c8;
    final Vector3f v1 = new Vector3f(cam.viewpointAngleX_ac, cam.viewpointAngleY_b8, cam.viewpointDeltaMagnitude_a0);
    FUN_800dcc94(cam.viewpointBobj_f4.getPosition(), v1);
    setViewpoint(v1.x, v1.y, v1.z);

    cam.viewpointTicksRemaining_d0--;
    if(cam.viewpointTicksRemaining_d0 <= 0) {
      cam.viewpointCallbackIndex_120 = 7;
      cam.viewpointMoving_122 = false;
    }
    //LAB_800d9508
  }

  @Method(0x800d9518L)
  public static void FUN_800d9518() {
    final BattleCamera cam = camera_800c67f0;

    cameraRotationVector_800fab98.x = cam.vec_d4.x;
    cameraRotationVector_800fab98.y = cam.vec_d4.y;
    cameraRotationVector_800fab98.z = 0.0f;
    RotMatrix_Xyz(cameraRotationVector_800fab98, cameraTransformMatrix_800c6798);
    cam.stepZ_a4 += cam.stepZAcceleration_b4;
    cam.vec_d4.z -= cam.stepZ_a4;
    MATRIX.mul(temp1_800faba0.set(0.0f, 0.0f, cam.vec_d4.z), cameraTransformMatrix_800c6798, temp2_800faba8);
    temp2_800faba8.add(
      cameraTransformMatrix_800c6798.transfer.getX(),
      cameraTransformMatrix_800c6798.transfer.getY(),
      cameraTransformMatrix_800c6798.transfer.getZ()
    );
    cam.viewpointBaseTranslation_94.x = cam.viewpointTargetTranslation_e8.x - temp2_800faba8.z;
    cam.viewpointBaseTranslation_94.y = cam.viewpointTargetTranslation_e8.y - temp2_800faba8.x;
    cam.viewpointBaseTranslation_94.z = cam.viewpointTargetTranslation_e8.z + temp2_800faba8.y;

    setViewpoint(cam.viewpointBaseTranslation_94.x, cam.viewpointBaseTranslation_94.y, cam.viewpointBaseTranslation_94.z);

    cam.viewpointTicksRemaining_d0--;
    if(cam.viewpointTicksRemaining_d0 <= 0) {
      cam.flags_11c &= ~UPDATE_VIEWPOINT;
      cam.viewpointMoving_122 = false;
    }
    //LAB_800d9638
  }

  @Method(0x800d9650L)
  public static void FUN_800d9650() {
    final BattleCamera cam = camera_800c67f0;

    final Vector3f v1 = new Vector3f(cam.vec_d4);
    FUN_800dcc94(ZERO, v1);
    v1.x += cam.viewpointTargetTranslation_e8.x;
    v1.y = v1.z + cam.viewpointTargetTranslation_e8.y;
    cam.viewpointDeltaMagnitude_a0 += cam.stepZ_a4;
    v1.z = cam.viewpointDeltaMagnitude_a0;
    FUN_800dcc94(ZERO, v1);
    setViewpoint(v1.x, v1.y, v1.z);
    cam.stepZ_e0 += cam.stepZAcceleration_e4;
    cam.vec_d4.z -= cam.stepZ_e0;

    cam.viewpointTicksRemaining_d0--;
    if(cam.viewpointTicksRemaining_d0 <= 0) {
      cam.flags_11c &= ~UPDATE_VIEWPOINT;
      cam.viewpointMoving_122 = false;
    }
    //LAB_800d976c
  }

  @Method(0x800d9788L)
  public static void FUN_800d9788() {
    final BattleCamera cam = camera_800c67f0;

    cameraRotationVector_800fab98.x = cam.vec_d4.x;
    cameraRotationVector_800fab98.y = cam.vec_d4.y;
    cameraRotationVector_800fab98.z = 0.0f;
    RotMatrix_Xyz(cameraRotationVector_800fab98, cameraTransformMatrix_800c6798);
    cam.stepZ_a4 += cam.stepZAcceleration_b4;
    cam.vec_d4.z -= cam.stepZ_a4;
    MATRIX.mul(temp1_800faba0.set(0.0f, 0.0f, cam.vec_d4.z), cameraTransformMatrix_800c6798, temp2_800faba8);
    temp2_800faba8.add(
      cameraTransformMatrix_800c6798.transfer.getX(),
      cameraTransformMatrix_800c6798.transfer.getY(),
      cameraTransformMatrix_800c6798.transfer.getZ()
    );

    cam.viewpointBaseTranslation_94.x = cam.viewpointTargetTranslation_e8.x - temp2_800faba8.z;
    cam.viewpointBaseTranslation_94.y = cam.viewpointTargetTranslation_e8.y - temp2_800faba8.x;
    cam.viewpointBaseTranslation_94.z = cam.viewpointTargetTranslation_e8.z + temp2_800faba8.y;

    setViewpoint(
      cam.rview2_00.refpoint_0c.x + cam.viewpointBaseTranslation_94.x,
      cam.rview2_00.refpoint_0c.y + cam.viewpointBaseTranslation_94.y,
      cam.rview2_00.refpoint_0c.z + cam.viewpointBaseTranslation_94.z
    );

    cam.viewpointTicksRemaining_d0--;
    if(cam.viewpointTicksRemaining_d0 <= 0) {
      cam.viewpointCallbackIndex_120 = 4;
      cam.viewpointMoving_122 = false;
    }
    //LAB_800d98b8
  }

  @Method(0x800d98d0L)
  public static void FUN_800d98d0() {
    final BattleCamera cam = camera_800c67f0;

    final Vector3f v1 = new Vector3f(cam.vec_d4);
    FUN_800dcc94(ZERO, v1);
    cam.viewpointDeltaMagnitude_a0 += cam.stepZ_a4;
    v1.x += cam.viewpointTargetTranslation_e8.x;
    v1.y = v1.z + cam.viewpointTargetTranslation_e8.y;
    v1.z = cam.viewpointDeltaMagnitude_a0;
    FUN_800dcc94(ZERO, v1);
    setViewpoint(cam.rview2_00.refpoint_0c.x + v1.x, cam.rview2_00.refpoint_0c.y + v1.y, cam.rview2_00.refpoint_0c.z + v1.z);
    cam.stepZ_e0 += cam.stepZAcceleration_e4;
    cam.vec_d4.z -= cam.stepZ_e0;

    cam.viewpointTicksRemaining_d0--;
    if(cam.viewpointTicksRemaining_d0 <= 0) {
      cam.viewpointAngleX_ac = calculateCameraValue(false, 5, 0, null);
      cam.viewpointAngleY_b8 = calculateCameraValue(false, 5, 1, null);
      cam.viewpointDeltaMagnitude_a0 = calculateCameraValue(false, 5, 2, null);
      cam.viewpointCallbackIndex_120 = 5;
      cam.viewpointMoving_122 = false;
    }
    //LAB_800d9a4c
  }

  @Method(0x800d9a68L)
  public static void FUN_800d9a68() {
    final BattleCamera cam = camera_800c67f0;

    cameraRotationVector_800fab98.x = cam.vec_d4.x;
    cameraRotationVector_800fab98.y = cam.vec_d4.y;
    cameraRotationVector_800fab98.z = 0.0f;
    RotMatrix_Xyz(cameraRotationVector_800fab98, cameraTransformMatrix_800c6798);
    cam.stepZ_a4 += cam.stepZAcceleration_b4;
    cam.vec_d4.z -= cam.stepZ_a4;
    MATRIX.mul(temp1_800faba0.set(0.0f, 0.0f, cam.vec_d4.z), cameraTransformMatrix_800c6798, temp2_800faba8);
    temp2_800faba8.add(
      cameraTransformMatrix_800c6798.transfer.getX(),
      cameraTransformMatrix_800c6798.transfer.getY(),
      cameraTransformMatrix_800c6798.transfer.getZ()
    );
    cam.viewpointBaseTranslation_94.x = cam.viewpointTargetTranslation_e8.x - temp2_800faba8.z;
    cam.viewpointBaseTranslation_94.y = cam.viewpointTargetTranslation_e8.y - temp2_800faba8.x;
    cam.viewpointBaseTranslation_94.z = cam.viewpointTargetTranslation_e8.z + temp2_800faba8.y;

    final VECTOR pos = cam.viewpointBobj_f4.getPosition();
    setViewpoint(pos.getX() + cam.viewpointBaseTranslation_94.x, pos.getY() + cam.viewpointBaseTranslation_94.y, pos.getZ() + cam.viewpointBaseTranslation_94.z);

    cam.viewpointTicksRemaining_d0--;
    if(cam.viewpointTicksRemaining_d0 <= 0) {
      cam.viewpointCallbackIndex_120 = 6;
      cam.viewpointMoving_122 = false;
    }
    //LAB_800d9bb8
  }

  /** TODO I might have messed this up */
  @Method(0x800d9bd4L)
  public static void FUN_800d9bd4() {
    final BattleCamera cam = camera_800c67f0;

    final Vector3f ref = new Vector3f(cam.vec_d4);
    FUN_800dcc94(ZERO, ref);
    cam.viewpointDeltaMagnitude_a0 += cam.stepZ_a4;
    ref.x += cam.viewpointTargetTranslation_e8.x;
    ref.y = ref.z + cam.viewpointTargetTranslation_e8.y;
    ref.z = cam.viewpointDeltaMagnitude_a0;
    FUN_800dcc94(ZERO, ref);
    final VECTOR pos = cam.viewpointBobj_f4.getPosition();
    setViewpoint(pos.getX() + ref.x, pos.getY() + ref.y, pos.getZ() + ref.z);
    cam.stepZ_e0 += cam.stepZAcceleration_e4;
    cam.vec_d4.z -= cam.stepZ_e0;

    cam.viewpointTicksRemaining_d0--;
    if(cam.viewpointTicksRemaining_d0 <= 0) {
      calculate3dAngleOrMagnitude(pos, cam.rview2_00.viewpoint_00, ref);
      cam.viewpointAngleX_ac = ref.x;
      cam.viewpointAngleY_b8 = ref.y;
      cam.viewpointDeltaMagnitude_a0 = ref.z;
      cam.viewpointCallbackIndex_120 = 7;
      cam.viewpointMoving_122 = false;
    }
    //LAB_800d9d7c
  }

  @Method(0x800d9da0L)
  public static void FUN_800d9da0() {
    final BattleCamera cam = camera_800c67f0;

    cam.refpointBaseTranslation_20.x += cam.stepX_3c;
    cam.refpointBaseTranslation_20.y += cam.stepY_48;
    cam.refpointBaseTranslation_20.z += cam.stepZ_54;
    setRefpoint(cam.refpointBaseTranslation_20.x, cam.refpointBaseTranslation_20.y, cam.refpointBaseTranslation_20.z);

    cam.refpointTicksRemaining_5c--;
    if(cam.refpointTicksRemaining_5c <= 0) {
      cam.flags_11c &= ~UPDATE_REFPOINT;
      cam.refpointMoving_123 = false;
    }
    //LAB_800d9e1c
  }

  @Method(0x800d9e2cL)
  public static void FUN_800d9e2c() {
    final BattleCamera cam = camera_800c67f0;

    cam.refpointAngleX_38 += cam.stepX_3c;
    cam.refpointAngleY_44 += cam.stepY_48;
    cam.refpointDeltaMagnitude_2c += cam.stepZ_54;

    final Vector3f v1 = new Vector3f(cam.refpointAngleX_38, cam.refpointAngleY_44, cam.refpointDeltaMagnitude_2c);
    FUN_800dcc94(ZERO, v1);
    setRefpoint(v1.x, v1.y, v1.z);

    cam.refpointTicksRemaining_5c--;
    if(cam.refpointTicksRemaining_5c <= 0) {
      cam.flags_11c &= ~UPDATE_REFPOINT;
      cam.refpointMoving_123 = false;
    }
    //LAB_800d9ee8
  }

  @Method(0x800d9ef8L)
  public static void FUN_800d9ef8() {
    final BattleCamera cam = camera_800c67f0;

    cam.refpointBaseTranslation_20.x += cam.stepX_3c;
    cam.refpointBaseTranslation_20.y += cam.stepY_48;
    cam.refpointBaseTranslation_20.z += cam.stepZ_54;

    setRefpoint(
      cam.rview2_00.viewpoint_00.x + cam.refpointBaseTranslation_20.x,
      cam.rview2_00.viewpoint_00.y + cam.refpointBaseTranslation_20.y,
      cam.rview2_00.viewpoint_00.z + cam.refpointBaseTranslation_20.z
    );

    cam.refpointTicksRemaining_5c--;
    if(cam.refpointTicksRemaining_5c <= 0) {
      cam.refpointCallbackIndex_121 = 2;
      cam.refpointMoving_123 = false;
    }
    //LAB_800d9f84
  }

  @Method(0x800d9f94L)
  public static void FUN_800d9f94() {
    final BattleCamera cam = camera_800c67f0;

    cam.refpointAngleX_38 += cam.stepX_3c;
    cam.refpointAngleY_44 += cam.stepY_48;
    cam.refpointDeltaMagnitude_2c += cam.stepZ_54;
    final Vector3f v1 = new Vector3f(cam.refpointAngleX_38, cam.refpointAngleY_44, cam.refpointDeltaMagnitude_2c);
    FUN_800dcc94(cam.rview2_00.viewpoint_00, v1);
    setRefpoint(v1.x, v1.y, v1.z);

    cam.refpointTicksRemaining_5c--;
    if(cam.refpointTicksRemaining_5c <= 0) {
      cam.refpointCallbackIndex_121 = 3;
      cam.refpointMoving_123 = false;
    }
    //LAB_800da048
  }

  @Method(0x800da058L)
  public static void FUN_800da058() {
    final BattleCamera cam = camera_800c67f0;

    cam.refpointBaseTranslation_20.x += cam.stepX_3c;
    cam.refpointBaseTranslation_20.y += cam.stepY_48;
    cam.refpointBaseTranslation_20.z += cam.stepZ_54;

    final VECTOR pos = cam.refpointBobj_80.getPosition();
    setRefpoint(pos.getX() + cam.refpointBaseTranslation_20.x, pos.getY() + cam.refpointBaseTranslation_20.y, pos.getZ() + cam.refpointBaseTranslation_20.z);

    cam.refpointTicksRemaining_5c--;
    if(cam.refpointTicksRemaining_5c <= 0) {
      cam.refpointCallbackIndex_121 = 6;
      cam.refpointMoving_123 = false;
    }
    //LAB_800da100
  }

  @Method(0x800da110L)
  public static void FUN_800da110() {
    final BattleCamera cam = camera_800c67f0;

    cam.refpointAngleX_38 += cam.stepX_3c;
    cam.refpointAngleY_44 += cam.stepY_48;
    cam.refpointDeltaMagnitude_2c += cam.stepZ_54;

    final Vector3f v1 = new Vector3f(cam.refpointAngleX_38, cam.refpointAngleY_44, cam.refpointDeltaMagnitude_2c);
    FUN_800dcc94(cam.refpointBobj_80.getPosition(), v1);
    setRefpoint(v1.x, v1.y, v1.z);

    cam.refpointTicksRemaining_5c--;
    if(cam.refpointTicksRemaining_5c <= 0) {
      cam.refpointCallbackIndex_121 = 7;
      cam.refpointMoving_123 = false;
    }
    //LAB_800da1e0
  }

  @Method(0x800da1f0L)
  public static void FUN_800da1f0() {
    final BattleCamera cam = camera_800c67f0;

    cameraRotationVector_800fab98.x = cam.vec_60.x;
    cameraRotationVector_800fab98.y = cam.vec_60.y;
    cameraRotationVector_800fab98.z = 0.0f;
    RotMatrix_Xyz(cameraRotationVector_800fab98, cameraTransformMatrix_800c6798);
    cam.stepZ_30 += cam.stepZAcceleration_40;
    cam.vec_60.z -= cam.stepZ_30;
    MATRIX.mul(temp1_800faba0.set(0.0f, 0.0f, cam.vec_60.z), cameraTransformMatrix_800c6798, temp2_800faba8);
    temp2_800faba8.add(
      cameraTransformMatrix_800c6798.transfer.getX(),
      cameraTransformMatrix_800c6798.transfer.getY(),
      cameraTransformMatrix_800c6798.transfer.getZ()
    );
    cam.refpointBaseTranslation_20.x = cam.refpointTargetTranslation_74.x - temp2_800faba8.z;
    cam.refpointBaseTranslation_20.y = cam.refpointTargetTranslation_74.y - temp2_800faba8.x;
    cam.refpointBaseTranslation_20.z = cam.refpointTargetTranslation_74.z + temp2_800faba8.y;
    setRefpoint(cam.refpointBaseTranslation_20.x, cam.refpointBaseTranslation_20.y, cam.refpointBaseTranslation_20.z);

    cam.refpointTicksRemaining_5c--;
    if(cam.refpointTicksRemaining_5c <= 0) {
      cam.flags_11c &= ~UPDATE_REFPOINT;
      cam.refpointMoving_123 = false;
    }
    //LAB_800da310
  }

  @Method(0x800da328L)
  public static void FUN_800da328() {
    final BattleCamera cam = camera_800c67f0;

    final Vector3f v1 = new Vector3f(cam.vec_60);
    FUN_800dcc94(ZERO, v1);

    cam.refpointDeltaMagnitude_2c += cam.stepZ_30;
    v1.x += cam.refpointTargetTranslation_74.x;
    v1.y = v1.z + cam.refpointTargetTranslation_74.y;
    v1.z = cam.refpointDeltaMagnitude_2c;
    FUN_800dcc94(ZERO, v1);
    setRefpoint(v1.x, v1.y, v1.z);

    cam.stepZ_6c += cam.stepZAcceleration_70;
    cam.vec_60.z -= cam.stepZ_6c;
    cam.refpointTicksRemaining_5c--;
    if(cam.refpointTicksRemaining_5c <= 0) {
      cam.flags_11c &= ~UPDATE_REFPOINT;
      cam.refpointMoving_123 = false;
    }
    //LAB_800da444
  }

  @Method(0x800da460L)
  public static void FUN_800da460() {
    final BattleCamera cam = camera_800c67f0;

    cameraRotationVector_800fab98.x = cam.vec_60.x;
    cameraRotationVector_800fab98.y = cam.vec_60.y;
    cameraRotationVector_800fab98.z = 0.0f;

    RotMatrix_Xyz(cameraRotationVector_800fab98, cameraTransformMatrix_800c6798);

    cam.stepZ_30 += cam.stepZAcceleration_40;
    cam.vec_60.z -= cam.stepZ_30;
    MATRIX.mul(temp1_800faba0.set(0.0f, 0.0f, cam.vec_60.z), cameraTransformMatrix_800c6798, temp2_800faba8);
    temp2_800faba8.add(
      cameraTransformMatrix_800c6798.transfer.getX(),
      cameraTransformMatrix_800c6798.transfer.getY(),
      cameraTransformMatrix_800c6798.transfer.getZ()
    );

    cam.refpointBaseTranslation_20.x = cam.refpointTargetTranslation_74.x - temp2_800faba8.z;
    cam.refpointBaseTranslation_20.y = cam.refpointTargetTranslation_74.y - temp2_800faba8.x;
    cam.refpointBaseTranslation_20.z = cam.refpointTargetTranslation_74.z + temp2_800faba8.y;

    setRefpoint(
      cam.rview2_00.viewpoint_00.x + cam.refpointBaseTranslation_20.x,
      cam.rview2_00.viewpoint_00.y + cam.refpointBaseTranslation_20.y,
      cam.rview2_00.viewpoint_00.z + cam.refpointBaseTranslation_20.z
    );

    cam.refpointTicksRemaining_5c--;
    if(cam.refpointTicksRemaining_5c <= 0) {
      cam.refpointCallbackIndex_121 = 2;
      cam.refpointMoving_123 = false;
    }
    //LAB_800da594
  }

  @Method(0x800da5b0L)
  public static void FUN_800da5b0() {
    final BattleCamera cam = camera_800c67f0;

    final Vector3f v1 = new Vector3f(cam.vec_60);
    FUN_800dcc94(ZERO, v1);

    cam.refpointDeltaMagnitude_2c += cam.stepZ_30;
    v1.x += cam.refpointTargetTranslation_74.x;
    v1.y = v1.z + cam.refpointTargetTranslation_74.y;
    v1.z = cam.refpointDeltaMagnitude_2c;
    FUN_800dcc94(ZERO, v1);
    setRefpoint(cam.rview2_00.viewpoint_00.x + v1.x, cam.rview2_00.viewpoint_00.y + v1.y, cam.rview2_00.viewpoint_00.z + v1.z);

    cam.stepZ_6c += cam.stepZAcceleration_70;
    cam.vec_60.z -= cam.stepZ_6c;

    cam.refpointTicksRemaining_5c--;
    if(cam.refpointTicksRemaining_5c <= 0) {
      cam.refpointAngleX_38 = calculateCameraValue(true, 3, 0, null);
      cam.refpointAngleY_44 = calculateCameraValue(true, 3, 1, null);
      cam.refpointDeltaMagnitude_2c = calculateCameraValue(true, 3, 2, null);
      cam.refpointCallbackIndex_121 = 3;
      cam.refpointMoving_123 = false;
    }
    //LAB_800da730
  }

  @Method(0x800da750L)
  public static void FUN_800da750() {
    final BattleCamera cam = camera_800c67f0;

    cam.stepZ_30 += cam.stepZAcceleration_40;
    cam.vec_60.z -= cam.stepZ_30;

    cameraRotationVector_800fab98.x = cam.vec_60.x;
    cameraRotationVector_800fab98.y = cam.vec_60.y;
    cameraRotationVector_800fab98.z = 0.0f;

    RotMatrix_Xyz(cameraRotationVector_800fab98, cameraTransformMatrix_800c6798);

    MATRIX.mul(temp1_800faba0.set(0.0f, 0.0f, cam.vec_60.z), cameraTransformMatrix_800c6798, temp2_800faba8);
    temp2_800faba8.add(
      cameraTransformMatrix_800c6798.transfer.getX(),
      cameraTransformMatrix_800c6798.transfer.getY(),
      cameraTransformMatrix_800c6798.transfer.getZ()
    );

    cam.refpointBaseTranslation_20.x = cam.refpointTargetTranslation_74.x - temp2_800faba8.z;
    cam.refpointBaseTranslation_20.y = cam.refpointTargetTranslation_74.y - temp2_800faba8.x;
    cam.refpointBaseTranslation_20.z = cam.refpointTargetTranslation_74.z + temp2_800faba8.y;

    final VECTOR pos = cam.refpointBobj_80.getPosition();
    setRefpoint(
      pos.getX() + cam.refpointBaseTranslation_20.x,
      pos.getY() + cam.refpointBaseTranslation_20.y,
      pos.getZ() + cam.refpointBaseTranslation_20.z
    );

    cam.refpointTicksRemaining_5c--;
    if(cam.refpointTicksRemaining_5c <= 0) {
      cam.refpointCallbackIndex_121 = 6;
      cam.refpointMoving_123 = false;
    }
    //LAB_800da8a0
  }

  @Method(0x800da8bcL)
  public static void FUN_800da8bc() {
    final BattleCamera cam = camera_800c67f0;

    final Vector3f ref = new Vector3f().set(cam.vec_60);
    FUN_800dcc94(ZERO, ref);

    cam.refpointDeltaMagnitude_2c += cam.stepZ_30;
    ref.x += cam.refpointTargetTranslation_74.x;
    ref.y = ref.z + cam.refpointTargetTranslation_74.y;
    ref.z = cam.refpointDeltaMagnitude_2c;
    FUN_800dcc94(ZERO, ref);

    final VECTOR pos = cam.refpointBobj_80.getPosition();
    setRefpoint(pos.getX() + ref.x, pos.getY() + ref.y, pos.getZ() + ref.z);

    cam.stepZ_6c += cam.stepZAcceleration_70;
    cam.vec_60.z -= cam.stepZ_6c;

    cam.refpointTicksRemaining_5c--;
    if(cam.refpointTicksRemaining_5c <= 0) {
      calculate3dAngleOrMagnitude(pos, cam.rview2_00.refpoint_0c, ref);
      cam.refpointAngleX_38 = ref.x;
      cam.refpointAngleY_44 = ref.y;
      cam.refpointDeltaMagnitude_2c = ref.z;
      cam.refpointCallbackIndex_121 = 7;
      cam.refpointMoving_123 = false;
    }
    //LAB_800daa60
  }

  @Method(0x800daa80L)
  public static void wobbleCamera() {
    if(useCameraWobble_800fabb8.get()) {
      if(framesUntilWobble_800c67d4.get() != 0) {
        framesUntilWobble_800c67d4.sub(1);
        return;
      }

      //LAB_800daabc
      final int x;
      final int y;
      final int type = tickCount_800bb0fc.get() & 0x3;
      if(type == 0) {
        //LAB_800dab04
        x = cameraWobbleOffsetX_800c67e4.get();
        y = cameraWobbleOffsetY_800c67e8.get() * 2;
      } else if(type == 1) {
        //LAB_800dab1c
        x = -cameraWobbleOffsetX_800c67e4.get() * 2;
        y = -cameraWobbleOffsetY_800c67e8.get();
        //LAB_800daaec
      } else if(type == 2) {
        //LAB_800dab3c
        x = cameraWobbleOffsetX_800c67e4.get() * 2;
        y = cameraWobbleOffsetY_800c67e8.get();
      } else {
        //LAB_800dab54
        x = -cameraWobbleOffsetX_800c67e4.get();
        y = -cameraWobbleOffsetY_800c67e8.get() * 2;
      }

      //LAB_800dab70
      //LAB_800dab78
      SetGeomOffset(screenOffsetX_800c67bc.get() + x, screenOffsetY_800c67c0.get() + y);

      wobbleFramesRemaining_800c67c4.sub(1);
      if(wobbleFramesRemaining_800c67c4.get() <= 0) {
        useCameraWobble_800fabb8.set(false);
        SetGeomOffset(screenOffsetX_800c67bc.get(), screenOffsetY_800c67c0.get());
      }
    }

    //LAB_800dabb8
  }

  @ScriptDescription("Resets battle camera movement")
  @Method(0x800dabccL)
  public static FlowControl scriptResetCameraMovement(final RunningScript<?> script) {
    resetCameraMovement();
    return FlowControl.CONTINUE;
  }

  @Method(0x800dabecL)
  public static void resetCameraMovement() {
    final BattleCamera cam = camera_800c67f0;
    cam.projectionPlaneChangeFrames_108 = 0;
    cam.projectionPlaneChanging_118 = false;
    cam.flags_11c = 0;
    cam.viewpointCallbackIndex_120 = 0;
    cam.refpointCallbackIndex_121 = 0;
    cam.viewpointMoving_122 = false;
    cam.refpointMoving_123 = false;
  }

  @ScriptDescription("Related to battle camera movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "How the camera moves")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "8-bit fixed-point position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "Only used in some modes, the scripted object used in calculations")
  @Method(0x800dac20L)
  public static FlowControl FUN_800dac20(final RunningScript<?> script) {
    float x = script.params_20[1].get() / (float)0x100;
    float y = script.params_20[2].get() / (float)0x100;
    final float z = script.params_20[3].get() / (float)0x100;

    // Odd funcs operate on angles
    if((script.params_20[0].get() & 1) != 0) {
      x = MathHelper.psxDegToRad(x);
      y = MathHelper.psxDegToRad(y);
    }

    FUN_800dac70(script.params_20[0].get(), x, y, z, SCRIPTS.getObject(script.params_20[4].get(), BattleObject.class));
    return FlowControl.CONTINUE;
  }

  @Method(0x800dac70L)
  public static void FUN_800dac70(final int index, final float x, final float y, final float z, final BattleObject bobj) {
    LOGGER.info(CAMERA, "[CAMERA] Array=_800fabbc, FUN index=%d, x=%f, y=%f, z=%f, bobj=%s", index, x, y, z, bobj);
    viewpointSetFromScriptMethods_800fabbc[index].accept(x, y, z, bobj);
    camera_800c67f0.viewpointCallbackIndex_fc = index;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800dacc4L)
  public static void setViewpointFromScriptTranslation(final float x, final float y, final float z, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;
    cam.viewpointBaseTranslation_94.set(x, y, z);
    setViewpoint(x, y, z);
    cam.flags_11c |= UPDATE_VIEWPOINT;
    cam.viewpointCallbackIndex_120 = 0;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800dad14L)
  public static void setViewpointFromScriptAngle(final float x, final float y, final float z, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;
    cam.viewpointAngleX_ac = x;
    cam.viewpointAngleY_b8 = y;
    cam.viewpointDeltaMagnitude_a0 = z;
    final Vector3f v1 = new Vector3f(x, y, z);
    FUN_800dcc94(ZERO, v1);
    setViewpoint(v1.x, v1.y, v1.z);

    cam.flags_11c |= UPDATE_VIEWPOINT;
    cam.viewpointCallbackIndex_120 = 1;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800dadc0L)
  public static void setViewpointFromScriptTranslationNoOp(final float x, final float y, final float z, final BattleObject bobj) {
    // no-op
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800dadc8L)
  public static void setViewpointFromScriptAngleNoOp(final float x, final float y, final float z, final BattleObject bobj) {
    // no-op
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800dadd0L)
  public static void setViewpointFromScriptTranslationRelativeToRefpoint(final float x, final float y, final float z, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;
    cam.viewpointBaseTranslation_94.set(x, y, z);

    setViewpoint(
      cam.rview2_00.refpoint_0c.x + x,
      cam.rview2_00.refpoint_0c.y + y,
      cam.rview2_00.refpoint_0c.z + z
    );

    cam.flags_11c |= UPDATE_VIEWPOINT;
    cam.viewpointCallbackIndex_120 = 4;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800dae3cL)
  public static void setViewpointFromScriptAngleRelativeToRefpoint(final float x, final float y, final float z, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;
    cam.viewpointDeltaMagnitude_a0 = z;
    cam.viewpointAngleX_ac = x;
    cam.viewpointAngleY_b8 = y;
    final Vector3f v1 = new Vector3f(x, y, z);
    FUN_800dcc94(cam.rview2_00.refpoint_0c, v1);
    setViewpoint(v1.x, v1.y, v1.z);
    cam.flags_11c |= UPDATE_VIEWPOINT;
    cam.viewpointCallbackIndex_120 = 5;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800daedcL)
  public static void setViewpointFromScriptTranslationRelativeToObject(final float x, final float y, final float z, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;
    cam.viewpointBaseTranslation_94.set(x, y, z);

    final VECTOR v0 = bobj.getPosition();

    setViewpoint(
      v0.getX() + cam.viewpointBaseTranslation_94.x,
      v0.getY() + cam.viewpointBaseTranslation_94.y,
      v0.getZ() + cam.viewpointBaseTranslation_94.z
    );

    cam.viewpointBobj_f4 = bobj;
    cam.flags_11c |= UPDATE_VIEWPOINT;
    cam.viewpointCallbackIndex_120 = 6;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800daf6cL)
  public static void setViewpointFromScriptAngleRelativeToObject(final float x, final float y, final float z, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;
    cam.viewpointAngleX_ac = x;
    cam.viewpointAngleY_b8 = y;
    cam.viewpointDeltaMagnitude_a0 = z;
    final Vector3f v1 = new Vector3f(x, y, z);
    FUN_800dcc94(bobj.getPosition(), v1);
    setViewpoint(v1.x, v1.y, v1.z);
    cam.viewpointBobj_f4 = bobj;
    cam.flags_11c |= UPDATE_VIEWPOINT;
    cam.viewpointCallbackIndex_120 = 7;
  }

  @ScriptDescription("Related to battle camera movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "How the camera moves")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "8-bit fixed-point position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "Only used in some modes, the scripted object used in calculations")
  @Method(0x800db034L)
  public static FlowControl FUN_800db034(final RunningScript<?> script) {
    float x = script.params_20[1].get() / (float)0x100;
    float y = script.params_20[2].get() / (float)0x100;
    final float z = script.params_20[3].get() / (float)0x100;

    // Odd funcs operate on angles
    if((script.params_20[0].get() & 1) != 0) {
      x = MathHelper.psxDegToRad(x);
      y = MathHelper.psxDegToRad(y);
    }

    FUN_800db084(script.params_20[0].get(), x, y, z, SCRIPTS.getObject(script.params_20[4].get(), BattleObject.class));
    return FlowControl.CONTINUE;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db084L)
  public static void FUN_800db084(final int index, final float x, final float y, final float z, final BattleObject bobj) {
    LOGGER.info(CAMERA, "[CAMERA] Array=_800fabdc, FUN index=%d, x=%f, y=%f, z=%f, bobj=%s", index, x, y, z, bobj);
    refpointSetFromScriptMethods_800fabdc[index].accept(x, y, z, bobj);
    camera_800c67f0.refpointCallbackIndex_88 = index;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db0d8L)
  public static void setRefpointFromScriptTranslation(final float x, final float y, final float z, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;
    cam.refpointBaseTranslation_20.set(x, y, z);
    setRefpoint(x, y, z);
    cam.flags_11c |= UPDATE_REFPOINT;
    cam.refpointCallbackIndex_121 = 0;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db128L)
  public static void setRefpointFromScriptAngle(final float x, final float y, final float z, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;
    cam.refpointDeltaMagnitude_2c = z;
    cam.refpointAngleX_38 = x;
    cam.refpointAngleY_44 = y;
    final Vector3f v1 = new Vector3f(x, y, z);
    FUN_800dcc94(ZERO, v1);
    setRefpoint(v1.x, v1.y, v1.z);
    cam.flags_11c |= UPDATE_REFPOINT;
    cam.refpointCallbackIndex_121 = 1;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db1d4L)
  public static void setRefpointFromScriptTranslationRelativeToViewpoint(final float x, final float y, final float z, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;
    cam.refpointBaseTranslation_20.set(x, y, z);
    setRefpoint(
      cam.rview2_00.viewpoint_00.x + x,
      cam.rview2_00.viewpoint_00.y + y,
      cam.rview2_00.viewpoint_00.z + z);
    cam.flags_11c |= UPDATE_REFPOINT;
    cam.refpointCallbackIndex_121 = 2;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db240L)
  public static void setRefpointFromScriptAngleRelativeToViewpoint(final float x, final float y, final float z, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;
    cam.refpointAngleX_38 = x;
    cam.refpointAngleY_44 = y;
    cam.refpointDeltaMagnitude_2c = z;
    final Vector3f v1 = new Vector3f(cam.refpointAngleX_38, cam.refpointAngleY_44, cam.refpointDeltaMagnitude_2c);
    FUN_800dcc94(cam.rview2_00.viewpoint_00, v1);
    setRefpoint(v1.x, v1.y, v1.z);
    cam.flags_11c |= UPDATE_REFPOINT;
    cam.refpointCallbackIndex_121 = 3;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db2e0L)
  public static void setRefpointFromScriptTranslationNoOp(final float x, final float y, final float z, final BattleObject bobj) {
    // no-op
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db2e8L)
  public static void setRefpointFromScriptAngleNoOp(final float x, final float y, final float z, final BattleObject bobj) {
    // no-op
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db2f0L)
  public static void setRefpointFromScriptTranslationRelativeToObject(final float x, final float y, final float z, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;
    cam.refpointBaseTranslation_20.set(x, y, z);

    final VECTOR v0 = bobj.getPosition();
    setRefpoint(
      v0.getX() + x,
      v0.getY() + y,
      v0.getZ() + z
    );

    cam.refpointBobj_80 = bobj;
    cam.flags_11c |= UPDATE_REFPOINT;
    cam.refpointCallbackIndex_121 = 6;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db398L)
  public static void setRefpointFromScriptAngleRelativeToObject(final float x, final float y, final float z, final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;
    cam.refpointAngleX_38 = x;
    cam.refpointAngleY_44 = y;
    cam.refpointDeltaMagnitude_2c = z;
    final Vector3f v1 = new Vector3f(x, y, z);
    FUN_800dcc94(bobj.getPosition(), v1);
    setRefpoint(v1.x, v1.y, v1.z);
    cam.refpointBobj_80 = bobj;
    cam.flags_11c |= UPDATE_REFPOINT;
    cam.refpointCallbackIndex_121 = 7;
  }

  @ScriptDescription("Related to battle camera movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "How the camera moves")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "8-bit fixed-point position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "?")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "Duration of movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stepType", description = "Two 2-bit packed values for X and Y respectively")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "Only used in some modes, the scripted object used in calculations")
  @Method(0x800db460L)
  public static FlowControl FUN_800db460(final RunningScript<?> script) {
    float x = script.params_20[1].get() / (float)0x100;
    float y = script.params_20[2].get() / (float)0x100;
    final float z = script.params_20[3].get() / (float)0x100;

    // Odd funcs operate on angles
    if((script.params_20[0].get() & 1) != 0) {
      x = MathHelper.psxDegToRad(x);
      y = MathHelper.psxDegToRad(y);
    }

    FUN_800db4ec(script.params_20[0].get(), x, y, z, script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get(), SCRIPTS.getObject(script.params_20[7].get(), BattleObject.class));
    return FlowControl.CONTINUE;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db4ecL)
  public static void FUN_800db4ec(final int callbackIndex, final float x, final float y, final float z, final int a4, final int ticks, final int stepType, final BattleObject bobj) {
    LOGGER.info(CAMERA, "[CAMERA] Array=_800fabfc, FUN index=%d, x=%f, y=%f, z=%f, a4=%d, ticks=%d, stepType=%d, bobj=%s", callbackIndex, x, y, z, a4, ticks, stepType, bobj);
    _800fabfc[callbackIndex].accept(x, y, z, ticks, stepType, a4, bobj);
    final BattleCamera cam = camera_800c67f0;
    cam.viewpointCallbackIndex_fc = callbackIndex;
    cam.viewpointMoving_122 = true;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db564L)
  public static void FUN_800db564(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    // no-op
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db56cL)
  public static void FUN_800db56c(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    // no-op
  }

  @ScriptDescription("Related to battle camera movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "How the camera moves")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "8-bit fixed-point position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "?")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "Duration of movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stepType", description = "Two 2-bit packed values for X and Y respectively")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "Only used in some modes, the scripted object used in calculations")
  @Method(0x800db574L)
  public static FlowControl FUN_800db574(final RunningScript<?> script) {
    float x = script.params_20[1].get() / (float)0x100;
    float y = script.params_20[2].get() / (float)0x100;
    final float z = script.params_20[3].get() / (float)0x100;

    // Odd funcs operate on angles
    if((script.params_20[0].get() & 1) != 0) {
      x = MathHelper.psxDegToRad(x);
      y = MathHelper.psxDegToRad(y);
    }

    FUN_800db600(script.params_20[0].get(), x, y, z, script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get(), SCRIPTS.getObject(script.params_20[7].get(), BattleObject.class));
    return FlowControl.CONTINUE;
  }

  @Method(0x800db600L)
  public static void FUN_800db600(final int callbackIndex, final float x, final float y, final float z, final int a4, final int ticks, final int stepType, final BattleObject bobj) {
    LOGGER.info(CAMERA, "[CAMERA] Array=_800fac5c, FUN index=%d, x=%f, y=%f, z=%f, ticks=%d, stepType=%d, a4=%d, bobj=%s", callbackIndex, x, y, z, ticks, stepType, a4, bobj);
    _800fac5c[callbackIndex].accept(x, y, z, ticks, stepType, a4, bobj);
    final BattleCamera cam = camera_800c67f0;
    cam.refpointCallbackIndex_88 = callbackIndex;
    cam.refpointMoving_123 = true;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db678L)
  public static void FUN_800db678(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    // no-op
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db680L)
  public static void FUN_800db680(final float x, final float y, final float z, final int ticks, final int stepType, final int a5, final BattleObject bobj) {
    // no-op
  }

  @ScriptDescription("Related to battle camera movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "How the camera moves")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "8-bit fixed-point position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "initialStepZ")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "finalStepZ")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stepType", description = "Two 2-bit packed values for X and Y respectively")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "Only used in some modes, the scripted object used in calculations")
  @Method(0x800db688L)
  public static FlowControl FUN_800db688(final RunningScript<?> script) {
    float x = script.params_20[1].get() / (float)0x100;
    float y = script.params_20[2].get() / (float)0x100;
    final float z = script.params_20[3].get() / (float)0x100;

    // Odd funcs operate on angles
    if((script.params_20[0].get() & 1) != 0) {
      x = MathHelper.psxDegToRad(x);
      y = MathHelper.psxDegToRad(y);
    }

    FUN_800db714(script.params_20[0].get(), x, y, z, script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get(), SCRIPTS.getObject(script.params_20[7].get(), BattleObject.class));
    return FlowControl.CONTINUE;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db714L)
  public static void FUN_800db714(final int callbackIndex, final float x, final float y, final float z, final int initialStepZ, final int finalStepZ, final int stepType, final BattleObject bobj) {
    LOGGER.info(CAMERA, "[CAMERA] Array=_800fac1c, FUN index=%d, x=%f, y=%f, z=%f, initialStepZ=%d, finalStepZ=%d, stepType=%d, bobj=%s", callbackIndex, x, y, z, initialStepZ, finalStepZ, stepType, bobj);
    _800fac1c[callbackIndex].accept(x, y, z, initialStepZ, finalStepZ, stepType, bobj);

    final BattleCamera cam = camera_800c67f0;
    cam.viewpointCallbackIndex_fc = callbackIndex;
    cam.viewpointMoving_122 = true;
  }

  @ScriptDescription("Related to battle camera movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "How the camera moves")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "8-bit fixed-point position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "initialStepZ")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "finalStepZ")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stepType", description = "Two 2-bit packed values for X and Y respectively")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "Only used in some modes, the scripted object used in calculations")
  @Method(0x800db79cL)
  public static FlowControl FUN_800db79c(final RunningScript<?> script) {
    float x = script.params_20[1].get() / (float)0x100;
    float y = script.params_20[2].get() / (float)0x100;
    final float z = script.params_20[3].get() / (float)0x100;

    // Odd funcs operate on angles
    if((script.params_20[0].get() & 1) != 0) {
      x = MathHelper.psxDegToRad(x);
      y = MathHelper.psxDegToRad(y);
    }

    FUN_800db828(script.params_20[0].get(), x, y, z, script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get(), SCRIPTS.getObject(script.params_20[7].get(), BattleObject.class));
    return FlowControl.CONTINUE;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db828L)
  public static void FUN_800db828(final int callbackIndex, final float x, final float y, final float z, final int initialStepZ, final int finalStepZ, final int stepType, final BattleObject bobj) {
    LOGGER.info(CAMERA, "[CAMERA] Array=_800fac7c, FUN index=%d, x=%f, y=%f, z=%f, initialStepZ=%d, finalStepZ=%d, stepType=%d, scriptIndex=%d", callbackIndex, x, y, z, initialStepZ, finalStepZ, stepType, bobj);
    _800fac7c[callbackIndex].accept(x, y, z, initialStepZ, finalStepZ, stepType, bobj);
    final BattleCamera cam = camera_800c67f0;
    cam.refpointCallbackIndex_88 = callbackIndex;
    cam.refpointMoving_123 = true;
  }

  @ScriptDescription("Related to battle camera movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "How the camera moves")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "8-bit fixed-point position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "Duration of movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stepSmoothingMode")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stepZ", description = "8-bit fixed-point")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stepType", description = "Two 2-bit packed values for X and Y respectively")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "Only used in some modes, the scripted object used in calculations")
  @Method(0x800db8b0L)
  public static FlowControl FUN_800db8b0(final RunningScript<?> script) {
    float x = script.params_20[1].get() / (float)0x100;
    float y = script.params_20[2].get() / (float)0x100;
    final float z = script.params_20[3].get() / (float)0x100;

    // Odd funcs operate on angles
    if((script.params_20[0].get() & 1) != 0) {
      x = MathHelper.psxDegToRad(x);
      y = MathHelper.psxDegToRad(y);
    }

    FUN_800db950(script.params_20[0].get(), x, y, z, script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get() / (float)0x100, script.params_20[7].get(), SCRIPTS.getObject(script.params_20[8].get(), BattleObject.class));
    return FlowControl.CONTINUE;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db950L)
  public static void FUN_800db950(final int callbackIndex, final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    LOGGER.info(CAMERA, "[CAMERA] Array=_800fac3c, FUN index=%d, x=%f, y=%f, z=%f, ticks=%d, stepSmoothingMode=%d, stepZ=%f, stepType=%d, bobj=%s", callbackIndex, x, y, z, ticks, stepSmoothingMode, stepZ, stepType, bobj);
    _800fac3c[callbackIndex].accept(x, y, z, ticks, stepSmoothingMode, stepZ, stepType, bobj);
    final BattleCamera cam = camera_800c67f0;
    cam.viewpointCallbackIndex_fc = callbackIndex;
    cam.viewpointMoving_122 = true;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db9d0L)
  public static void FUN_800db9d0(final float x, final float y, final float z, final int ticks, final int a4, final float a5, final int stepType, final BattleObject bobj) {
    // no-op
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800db9d8L)
  public static void FUN_800db9d8(final float x, final float y, final float z, final int ticks, final int a4, final float a5, final int stepType, final BattleObject bobj) {
    // no-op
  }

  @ScriptDescription("Related to battle camera movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "How the camera moves")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "If mode is even, 8-bit fixed-point position; if odd, PSX degree angle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "8-bit fixed-point position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "Duration of movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stepSmoothingMode")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stepZ", description = "8-bit fixed-point")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "stepType", description = "Two 2-bit packed values for X and Y respectively")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "Only used in some modes, the scripted object used in calculations")
  @Method(0x800db9e0L)
  public static FlowControl FUN_800db9e0(final RunningScript<?> script) {
    float x = script.params_20[1].get() / (float)0x100;
    float y = script.params_20[2].get() / (float)0x100;
    final float z = script.params_20[3].get() / (float)0x100;

    // Odd funcs operate on angles
    if((script.params_20[0].get() & 1) != 0) {
      x = MathHelper.psxDegToRad(x);
      y = MathHelper.psxDegToRad(y);
    }

    FUN_800dba80(script.params_20[0].get(), x, y, z, script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get() / (float)0x100, script.params_20[7].get(), SCRIPTS.getObject(script.params_20[8].get(), BattleObject.class));
    return FlowControl.CONTINUE;
  }

  @Method(0x800dba80L)
  public static void FUN_800dba80(final int callbackIndex, final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    LOGGER.info(CAMERA, "[CAMERA] Array=_800fac9c, FUN index=%d, x=%f, y=%f, z=%f, ticks=%d, stepSmoothingMode=%d, stepZ=%f, stepType=%d, bobj=%s", callbackIndex, x, y, z, ticks, stepSmoothingMode, stepZ, stepType, bobj);
    _800fac9c[callbackIndex].accept(x, y, z, ticks, stepSmoothingMode, stepZ, stepType, bobj);
    final BattleCamera cam = camera_800c67f0;
    cam.refpointCallbackIndex_88 = callbackIndex;
    cam.refpointMoving_123 = true;
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800dbb00L)
  public static void FUN_800dbb00(final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    // no-op
  }

  /**
   * @param x 8-bit fixed-point
   * @param y 8-bit fixed-point
   * @param z 8-bit fixed-point
   */
  @Method(0x800dbb08L)
  public static void FUN_800dbb08(final float x, final float y, final float z, final int ticks, final int stepSmoothingMode, final float stepZ, final int stepType, final BattleObject bobj) {
    // no-op
  }

  @ScriptDescription("Whether or not the camera is currently moving")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "0 = viewpoint moving, 1 = refpoint moving")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "isMoving")
  @Method(0x800dbb10L)
  public static FlowControl scriptIsCameraMoving(final RunningScript<?> script) {
    final BattleCamera cam = camera_800c67f0;
    final int type = script.params_20[0].get();
    final boolean moving;
    if(type == 0) {
      //LAB_800dbb3c
      moving = cam.viewpointMoving_122;
    } else if(type == 1) {
      //LAB_800dbb48
      moving = cam.refpointMoving_123;
    } else {
      throw new RuntimeException("Undefined a1");
    }

    //LAB_800dbb50
    script.params_20[1].set(moving ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unused")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "?")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "?")
  @Method(0x800dbb9cL)
  public static FlowControl FUN_800dbb9c(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @ScriptDescription("Sets the viewport twist")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "twist", description = "The new viewpoint twist")
  @Method(0x800dbc2cL)
  public static FlowControl scriptSetViewportTwist(final RunningScript<?> script) {
    camera_800c67f0.rview2_00.viewpointTwist_18 = script.params_20[0].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "0 = viewpoint, 1 = refpoint")
  @Method(0x800dbc80L)
  public static FlowControl FUN_800dbc80(final RunningScript<?> script) {
    final int type = script.params_20[0].get();

    if((type & UPDATE_VIEWPOINT) != 0) {
      camera_800c67f0.stepZAcceleration_e4 = 0.0f;
      camera_800c67f0.stepZAcceleration_b4 = 0.0f;
    }

    //LAB_800dbca8
    if((type & UPDATE_REFPOINT) != 0) {
      camera_800c67f0.stepZAcceleration_70 = 0.0f;
      camera_800c67f0.stepZAcceleration_40 = 0.0f;
    }

    //LAB_800dbcc0
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets camera projection plane distance")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "distance", description = "The projection plane distance")
  @Method(0x800dbcc8L)
  public static FlowControl scriptSetCameraProjectionPlaneDistance(final RunningScript<?> script) {
    LOGGER.info(CAMERA, "[CAMERA] scriptSetCameraProjectionPlaneDistance distance=%d", script.params_20[0].get());

    final BattleCamera cam = camera_800c67f0;
    cam.projectionPlaneDistance_100 = script.params_20[0].get();
    cam.projectionPlaneChangeFrames_108 = 0;
    cam.projectionPlaneChanging_118 = true;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the camera projection plane distance")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "distance", description = "The projection plane distance")
  @Method(0x800dbcfcL)
  public static FlowControl scriptGetProjectionPlaneDistance(final RunningScript<?> script) {
    script.params_20[0].set(getProjectionPlaneDistance());
    return FlowControl.CONTINUE;
  }

  @Method(0x800dbe40L)
  public static void FUN_800dbe40() {
    final BattleCamera cam = camera_800c67f0;
    cam.flags_11c &= ~UPDATE_VIEWPOINT;
    cam.viewpointMoving_122 = false;
  }

  @Method(0x800dbe60L)
  public static void FUN_800dbe60() {
    final BattleCamera cam = camera_800c67f0;
    cam.flags_11c &= ~UPDATE_VIEWPOINT;
    cam.viewpointMoving_122 = false;
  }

  @Method(0x800dbe80L)
  public static void FUN_800dbe80() {
    camera_800c67f0.viewpointMoving_122 = false;
  }

  @Method(0x800dbe8cL)
  public static void FUN_800dbe8c() {
    camera_800c67f0.viewpointMoving_122 = false;
  }

  @Method(0x800dbe98L)
  public static void FUN_800dbe98() {
    final BattleCamera cam = camera_800c67f0;
    cam.viewpointMoving_122 = false;

    setViewpoint(
      cam.rview2_00.refpoint_0c.x + cam.viewpointBaseTranslation_94.x,
      cam.rview2_00.refpoint_0c.y + cam.viewpointBaseTranslation_94.y,
      cam.rview2_00.refpoint_0c.z + cam.viewpointBaseTranslation_94.z
    );
  }

  @Method(0x800dbef0L)
  public static void FUN_800dbef0() {
    final BattleCamera cam = camera_800c67f0;
    cam.viewpointMoving_122 = false;
    final Vector3f v1 = new Vector3f(cam.viewpointAngleX_ac, cam.viewpointAngleY_b8, cam.viewpointDeltaMagnitude_a0);
    FUN_800dcc94(cam.rview2_00.refpoint_0c, v1);
    setViewpoint(v1.x, v1.y, v1.z);
  }

  @Method(0x800dbf70L)
  public static void FUN_800dbf70() {
    final BattleCamera cam = camera_800c67f0;
    cam.viewpointMoving_122 = false;

    final VECTOR v0 = cam.viewpointBobj_f4.getPosition();
    setViewpoint(v0.getX() + cam.viewpointBaseTranslation_94.x, v0.getY() + cam.viewpointBaseTranslation_94.y, v0.getZ() + cam.viewpointBaseTranslation_94.z);
  }

  @Method(0x800dbfd4L)
  public static void FUN_800dbfd4() {
    final BattleCamera cam = camera_800c67f0;
    cam.viewpointMoving_122 = false;

    final Vector3f v1 = new Vector3f(cam.viewpointAngleX_ac, cam.viewpointAngleY_b8, cam.viewpointDeltaMagnitude_a0);
    FUN_800dcc94(cam.viewpointBobj_f4.getPosition(), v1);
    setViewpoint(v1.x, v1.y, v1.z);
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
    cam.flags_11c &= ~UPDATE_REFPOINT;
    cam.refpointMoving_123 = false;
  }

  @Method(0x800dc0b0L)
  public static void FUN_800dc0b0() {
    final BattleCamera cam = camera_800c67f0;
    cam.flags_11c &= ~UPDATE_REFPOINT;
    cam.refpointMoving_123 = false;
  }

  @Method(0x800dc0d0L)
  public static void FUN_800dc0d0() {
    final BattleCamera cam = camera_800c67f0;
    cam.refpointMoving_123 = false;

    setRefpoint(
      cam.rview2_00.viewpoint_00.x + cam.refpointBaseTranslation_20.x,
      cam.rview2_00.viewpoint_00.y + cam.refpointBaseTranslation_20.y,
      cam.rview2_00.viewpoint_00.z + cam.refpointBaseTranslation_20.z
    );
  }

  @Method(0x800dc128L)
  public static void FUN_800dc128() {
    final BattleCamera cam = camera_800c67f0;
    cam.refpointMoving_123 = false;

    final Vector3f v1 = new Vector3f(cam.refpointAngleX_38, cam.refpointAngleY_44, cam.refpointDeltaMagnitude_2c);
    FUN_800dcc94(cam.rview2_00.viewpoint_00, v1);
    setRefpoint(v1.x, v1.y, v1.z);
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
    cam.refpointMoving_123 = false;

    final VECTOR v0 = cam.refpointBobj_80.getPosition();
    setRefpoint(
      v0.getX() + cam.refpointBaseTranslation_20.x,
      v0.getY() + cam.refpointBaseTranslation_20.y,
      v0.getZ() + cam.refpointBaseTranslation_20.z
    );
  }

  @Method(0x800dc21cL)
  public static void FUN_800dc21c() {
    final BattleCamera cam = camera_800c67f0;
    cam.refpointMoving_123 = false;

    final Vector3f v1 = new Vector3f(cam.refpointAngleX_38, cam.refpointAngleY_44, cam.refpointDeltaMagnitude_2c);
    FUN_800dcc94(cam.refpointBobj_80.getPosition(), v1);
    setRefpoint(v1.x, v1.y, v1.z);
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

  @ScriptDescription("Calculates multiple different absolute or relative camera values")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "0 = viewpoint, 1 = refpoint")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "Which calculation to perform")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "component", description = "0 = X, 1 = Y, 2 = Z")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "Only used in some modes, the scripted object used in calculations (or -1 for none)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "out", description = "If mode is even, values are 8-bit fixed-point; if mode is odd, values are PSX degrees")
  @Method(0x800dc2d8L)
  public static FlowControl scriptCalculateCameraValue(final RunningScript<?> script) {
    LOGGER.info(CAMERA, "[CAMERA] Calc val: use refpoint=%b, FUN index=%d, component=%d, script index=%d", script.params_20[0].get() != 0, script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());

    float value = calculateCameraValue(script.params_20[0].get() != 0, script.params_20[1].get(), script.params_20[2].get(), SCRIPTS.getObject(script.params_20[3].get(), BattleObject.class));

    // Odd funcs operate on angles, but Z values in these methods are delta vector mag, not angles
    if((script.params_20[1].get() & 1) != 0 && script.params_20[2].get() != 2) {
      value = MathHelper.radToPsxDeg(value);
    }

    script.params_20[4].set((int)value);
    return FlowControl.CONTINUE;
  }

  /**
   * @param callbackIndex <ol start="0">
   *                        <li>Raw component</li>
   *                        <li>Angle from 0 to raw component</li>
   *                        <li>Delta from viewpoint to component (only for viewpoint)</li>
   *                        <li>Angle from viewpoint to component (only for viewpoint)</li>
   *                        <li>Delta from refpoint to component (only for refpoint)</li>
   *                        <li>Angle from refpoint to component (only for refpoint)</li>
   *                        <li>Delta from scripted object to component</li>
   *                        <li>Angle from scripted object to component</li>
   *                      </ol>
   */
  @Method(0x800dc384L)
  public static float calculateCameraValue(final boolean useRefpoint, final int callbackIndex, final int component, @Nullable final BattleObject bobj) {
    final BattleCamera cam = camera_800c67f0;

    final Vector3f point;
    final ComponentFunction[] componentMethod;
    if(useRefpoint) {
      point = cam.rview2_00.refpoint_0c;
      componentMethod = viewpointComponentMethods_800fad9c;
    } else {
      point = cam.rview2_00.viewpoint_00;
      componentMethod = refpointComponentMethods_800fad7c;
    }

    return componentMethod[callbackIndex].apply(component, bobj, point);
  }

  @Method(0x800dc408L)
  public static float refpointRawComponent(final int component, final BattleObject bobj, final Vector3f point) {
    if(component == 0) {
      return point.x;
    }

    if(component == 1) {
      return point.y;
    }

    if(component == 2) {
      return point.z;
    }

    throw new IllegalArgumentException("Illegal component " + component);
  }

  @Method(0x800dc45cL)
  public static float refpointAngleFrom0ToComponent(final int component, final BattleObject bobj, final Vector3f point) {
    return calculate3dAngleOrMagnitude(ZERO, point, component);
  }

  @Method(0x800dc504L)
  public static float refpointNoop1(final int component, final BattleObject bobj, final Vector3f point) {
    // no-op
    return 0;
  }

  @Method(0x800dc50cL)
  public static float refpointNoop2(final int component, final BattleObject bobj, final Vector3f point) {
    // no-op
    return 0;
  }

  @Method(0x800dc514L)
  public static float refpointDeltaFromRefpointToComponent(final int component, final BattleObject bobj, final Vector3f point) {
    final BattleCamera cam = camera_800c67f0;

    if(component == 0) {
      return point.x - cam.rview2_00.refpoint_0c.x;
    }

    if(component == 1) {
      return point.y - cam.rview2_00.refpoint_0c.y;
    }

    if(component == 2) {
      return point.z - cam.rview2_00.refpoint_0c.z;
    }

    throw new IllegalArgumentException("Illegal component " + component);
  }

  @Method(0x800dc580L)
  public static float refpointAngleFromRefpointToComponent(final int component, final BattleObject bobj, final Vector3f point) {
    return calculate3dAngleOrMagnitude(camera_800c67f0.rview2_00.refpoint_0c, point, component);
  }

  @Method(0x800dc630L)
  public static float refpointDeltaFromScriptedObjToComponent(final int component, final BattleObject bobj, final Vector3f point) {
    final VECTOR vec = bobj.getPosition();

    if(component == 0) {
      return point.x - vec.getX();
    }

    if(component == 1) {
      return point.y - vec.getY();
    }

    if(component == 2) {
      return point.z - vec.getZ();
    }

    throw new IllegalArgumentException("Illegal component " + component);
  }

  @Method(0x800dc6d8L)
  public static float refpointAngleFromScriptedObjToComponent(final int component, final BattleObject bobj, final Vector3f point) {
    return calculate3dAngleOrMagnitude(bobj.getPosition(), point, component);
  }

  @Method(0x800dc798L)
  public static float viewpointRawComponent(final int component, final BattleObject bobj, final Vector3f point) {
    if(component == 0) {
      return point.x;
    }

    if(component == 1) {
      return point.y;
    }

    if(component == 2) {
      return point.z;
    }

    throw new IllegalArgumentException("Illegal component " + component);
  }

  @Method(0x800dc7ecL)
  public static float viewpointAngleFrom0ToComponent(final int component, final BattleObject bobj, final Vector3f point) {
    return calculate3dAngleOrMagnitude(ZERO, point, component);
  }

  @Method(0x800dc894L)
  public static float viewpointDeltaFromViewpointToComponent(final int component, final BattleObject bobj, final Vector3f point) {
    if(component == 0) {
      return point.x - camera_800c67f0.rview2_00.viewpoint_00.x;
    }

    if(component == 1) {
      return point.y - camera_800c67f0.rview2_00.viewpoint_00.y;
    }

    if(component == 2) {
      return point.z - camera_800c67f0.rview2_00.viewpoint_00.z;
    }

    throw new IllegalArgumentException("Illegal component " + component);
  }

  @Method(0x800dc900L)
  public static float viewpointAngleFromViewpointToComponent(final int component, final BattleObject bobj, final Vector3f point) {
    return calculate3dAngleOrMagnitude(camera_800c67f0.rview2_00.viewpoint_00, point, component);
  }

  @Method(0x800dc9b0L)
  public static float viewpointNoop1(final int component, final BattleObject bobj, final Vector3f point) {
    // no-op
    return 0;
  }

  @Method(0x800dc9b8L)
  public static float viewpointNoop2(final int component, final BattleObject bobj, final Vector3f point) {
    // no-op
    return 0;
  }

  @Method(0x800dc9c0L)
  public static float viewpointDeltaFromScriptedObjToComponent(final int component, final BattleObject bobj, final Vector3f point) {
    final VECTOR objPos = bobj.getPosition();

    if(component == 0) {
      return point.x - objPos.getX();
    }

    if(component == 1) {
      return point.y - objPos.getY();
    }

    if(component == 2) {
      return point.z - objPos.getZ();
    }

    throw new IllegalArgumentException("Illegal component " + component);
  }

  @Method(0x800dca68L)
  public static float viewpointAngleFromScriptedObjToComponent(final int component, final BattleObject bobj, final Vector3f point) {
    return calculate3dAngleOrMagnitude(bobj.getPosition(), point, component);
  }

  @ScriptDescription("Stops camera movement")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "0 = viewpoint, 1 = refpoint")
  @Method(0x800dcb84L)
  public static FlowControl scriptStopCameraMovement(final RunningScript<?> script) {
    final BattleCamera cam = camera_800c67f0;
    final int type = script.params_20[0].get();

    LOGGER.info(CAMERA, "[CAMERA] type=%d", type);

    if((type & UPDATE_VIEWPOINT) != 0) {
      cam.viewpointCallbackIndex_120 = 0;
      cam.viewpointMoving_122 = false;
      cam.flags_11c &= ~UPDATE_VIEWPOINT;
    }

    //LAB_800dcbbc
    if((type & UPDATE_REFPOINT) != 0) {
      cam.refpointCallbackIndex_121 = 0;
      cam.refpointMoving_123 = false;
      cam.flags_11c &= ~UPDATE_REFPOINT;
    }

    //LAB_800dcbe4
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the camera wobble")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "frames", description = "The number of frames to perform the wobble")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "framesUntilWobble", description = "The number of frames until the wobble starts")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "Wobble offset X")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "Wobble offset Y")
  @Method(0x800dcbecL)
  public static FlowControl scriptWobbleCamera(final RunningScript<?> script) {
    wobbleFramesRemaining_800c67c4.set(script.params_20[0].get());
    framesUntilWobble_800c67d4.set(script.params_20[1].get());
    cameraWobbleOffsetX_800c67e4.set(script.params_20[2].get());
    cameraWobbleOffsetY_800c67e8.set(script.params_20[3].get());
    useCameraWobble_800fabb8.set(true);
    getScreenOffset(screenOffsetX_800c67bc, screenOffsetY_800c67c0);
    return FlowControl.CONTINUE;
  }

  @Method(0x800dcc64L)
  public static void setViewpoint(final float x, final float y, final float z) {
    camera_800c67f0.rview2_00.viewpoint_00.set(x, y, z);
  }

  @Method(0x800dcc7cL)
  public static void setRefpoint(final float x, final float y, final float z) {
    camera_800c67f0.rview2_00.refpoint_0c.set(x, y, z);
  }

  @Method(0x800dcc94L)
  public static void FUN_800dcc94(final Vector3f v0, final Vector3f v1) {
    cameraRotationVector_800fab98.set(v1.x, v1.y, 0.0f);
    RotMatrix_Xyz(cameraRotationVector_800fab98, cameraTransformMatrix_800c6798);
    MATRIX.mul(temp1_800faba0.set(0.0f, 0.0f, v1.z), cameraTransformMatrix_800c6798, temp2_800faba8);
    temp2_800faba8.add(
      cameraTransformMatrix_800c6798.transfer.getX(),
      cameraTransformMatrix_800c6798.transfer.getY(),
      cameraTransformMatrix_800c6798.transfer.getZ()
    );
    v1.x = v0.x - temp2_800faba8.z;
    v1.y = v0.y - temp2_800faba8.x;
    v1.z = v0.z + temp2_800faba8.y;
  }

  public static void FUN_800dcc94(final VECTOR v0, final Vector3f v1) {
    cameraRotationVector_800fab98.set(v1.x, v1.y, 0.0f);
    RotMatrix_Xyz(cameraRotationVector_800fab98, cameraTransformMatrix_800c6798);
    MATRIX.mul(temp1_800faba0.set(0.0f, 0.0f, v1.z), cameraTransformMatrix_800c6798, temp2_800faba8);
    temp2_800faba8.add(
      cameraTransformMatrix_800c6798.transfer.getX(),
      cameraTransformMatrix_800c6798.transfer.getY(),
      cameraTransformMatrix_800c6798.transfer.getZ()
    );
    v1.x = v0.x.get() - temp2_800faba8.z;
    v1.y = v0.y.get() - temp2_800faba8.x;
    v1.z = v0.z.get() + temp2_800faba8.y;
  }

  /**
   * @return Component 0/1 are angles, 2 is the magnitude of the delta vector
   */
  @Method(0x800dcd9cL)
  public static float calculate3dAngleOrMagnitude(final Vector3f pos0, final Vector3f pos1, final int component) {
    final float dx = pos0.x - pos1.x;
    final float dy = pos0.y - pos1.y;
    final float dz = pos0.z - pos1.z;
    final float horizontalHypotenuse = Math.sqrt(dx * dx + dz * dz);

    return switch(component) {
      case 0 -> MathHelper.floorMod(MathHelper.atan2(dz, dx), MathHelper.TWO_PI); // Angle between vector and +X axis
      case 1 -> MathHelper.floorMod(MathHelper.atan2(dy, horizontalHypotenuse), MathHelper.TWO_PI); // Angle between vector and +Y axis
      case 2 -> Math.sqrt(dx * dx + dy * dy + dz * dz); // Angle between vector and +Z axis?
      default -> throw new IllegalStateException("Illegal component " + component);
    };
  }

  /**
   * @return Component 0/1 are angles, 2 is the magnitude of the delta vector
   */
  public static float calculate3dAngleOrMagnitude(final VECTOR pos0, final Vector3f pos1, final int component) {
    final float dx = pos0.x.get() - pos1.x;
    final float dy = pos0.y.get() - pos1.y;
    final float dz = pos0.z.get() - pos1.z;
    final float horizontalHypotenuse = Math.sqrt(dx * dx + dz * dz);

    return switch(component) {
      case 0 -> MathHelper.floorMod(MathHelper.atan2(dz, dx), MathHelper.TWO_PI); // Angle between vector and +X axis
      case 1 -> MathHelper.floorMod(MathHelper.atan2(dy, horizontalHypotenuse), MathHelper.TWO_PI); // Angle between vector and +Y axis
      case 2 -> Math.sqrt(dx * dx + dy * dy + dz * dz); // Angle between vector and +Z axis?
      default -> throw new IllegalStateException("Illegal component " + component);
    };
  }

  /**
   * @param pos1 Returned x/y are angles, z is the magnitude of the delta vector
   */
  public static void calculate3dAngleOrMagnitude(final VECTOR pos0, final Vector3f pos1, final Vector3f out) {
    final float dx = pos0.x.get() - pos1.x;
    final float dy = pos0.y.get() - pos1.y;
    final float dz = pos0.z.get() - pos1.z;
    final float horizontalHypotenuse = Math.sqrt(dx * dx + dz * dz);

    out.x = MathHelper.floorMod(MathHelper.atan2(dz, dx), MathHelper.TWO_PI); // Angle between vector and +X axis
    out.y = MathHelper.floorMod(MathHelper.atan2(dy, horizontalHypotenuse), MathHelper.TWO_PI); // Angle between vector and +Y axis
    out.z = Math.sqrt(dx * dx + dy * dy + dz * dz); // Angle between vector and +Z axis?
  }

  @Method(0x800dcebcL)
  public static void setInitialAndFinalCameraVelocities(final int mode, final float stepZ1, final float stepZ2, final int divisor, final FloatRef initialStepZ, final FloatRef finalStepZ) {
    if(mode == 0) {
      //LAB_800dcedc
      initialStepZ.set(stepZ1);
      finalStepZ.set(stepZ2 * 2.0f / divisor - stepZ1);
    } else if(mode == 1) {
      //LAB_800dcef8
      initialStepZ.set(stepZ2 * 2.0f / divisor - stepZ1);
      finalStepZ.set(stepZ1);
    } else {
      throw new IllegalArgumentException("Invalid mode " + mode);
    }
  }

  /**
   * @param stepType <ul>
   *                   <li>0 - no step (always 0)</li>
   *                   <li>1 - positive step</li>
   *                   <li>other - negative step</li>
   *                 </ol>
   */
  @Method(0x800dcf10L)
  public static float calculateStep(final int component, final float val1, final float val2, final int divisor, final int stepType) {
    if(component == 2) {
      //LAB_800dcfa4
      //LAB_800dcfb0
      return (val2 - val1) / divisor;
    }

    //LAB_800dcf38
    if(stepType == 0) {
      return 0;
    }

    //LAB_800dcf48
    if(stepType == 1) {
      if(val1 > val2) {
        //LAB_800dcf6c
        return (val2 - val1 + MathHelper.TWO_PI) / divisor;
      }

      return (val2 - val1) / divisor;
    }

    //LAB_800dcf84
    if(val1 <= val2) {
      return (val2 - val1 - MathHelper.TWO_PI) / divisor;
    }

    //LAB_800dcf58
    return (val2 - val1) / divisor;
  }

  /**
   * @param stepType <ul>
   *                   <li>0 - no step (always 0)</li>
   *                   <li>1 - positive step</li>
   *                   <li>other - negative step</li>
   *                 </ol>
   *
   * @return 8-bit fixed-point
   */
  @Method(0x800dcfb8L)
  public static float calculateDifference(final int component, final float val1, final float val2, final int stepType) {
    if(component == 2) {
      //LAB_800dd020
      //LAB_800dd024
      return val2 - val1;
    }

    //LAB_800dcfdc
    if(stepType == 0) {
      return 0;
    }

    //LAB_800dcfec
    if(stepType == 1) {
      if(val2 >= val1) {
        //LAB_800dcffc
        return val2 - val1;
      }

      //LAB_800dd004
      //LAB_800dd008
      return val2 - val1 + MathHelper.TWO_PI;
    }

    //LAB_800dd010
    if(val2 < val1) {
      return val2 - val1;
    }

    return val2 - (val1 + MathHelper.TWO_PI);
  }

  @Method(0x800dd0d4L)
  public static float calculateYAngleFromRefpointToViewpoint() {
    return refpointComponentMethods_800fad7c[5].apply(1, null, camera_800c67f0.rview2_00.viewpoint_00);
  }

  @Method(0x800dd118L)
  public static float calculateXAngleFromRefpointToViewpoint() {
    return refpointComponentMethods_800fad7c[5].apply(0, null, camera_800c67f0.rview2_00.viewpoint_00);
  }

  @Method(0x800dd15cL)
  public static MATRIX lerp(final MATRIX a0, final MATRIX a1, final int ratio) {
    if(ratio > 0) {
      if(ratio != 0x800) {
        final int v1 = 0x1000 - ratio;
        a0.set(0, (short)(a0.get(0) * v1 + a1.get(0) * ratio >> 12));
        a0.set(1, (short)(a0.get(1) * v1 + a1.get(1) * ratio >> 12));
        a0.set(2, (short)(a0.get(2) * v1 + a1.get(2) * ratio >> 12));
        a0.set(3, (short)(a0.get(3) * v1 + a1.get(3) * ratio >> 12));
        a0.set(4, (short)(a0.get(4) * v1 + a1.get(4) * ratio >> 12));
        a0.set(5, (short)(a0.get(5) * v1 + a1.get(5) * ratio >> 12));
        a0.set(6, (short)(a0.get(6) * v1 + a1.get(6) * ratio >> 12));
        a0.set(7, (short)(a0.get(7) * v1 + a1.get(7) * ratio >> 12));
        a0.set(8, (short)(a0.get(8) * v1 + a1.get(8) * ratio >> 12));
        a0.transfer.setX(a0.transfer.getX() * v1 + a1.transfer.getX() * ratio >> 12);
        a0.transfer.setY(a0.transfer.getY() * v1 + a1.transfer.getY() * ratio >> 12);
        a0.transfer.setZ(a0.transfer.getZ() * v1 + a1.transfer.getZ() * ratio >> 12);
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
        a0.normalize();
      }
    }

    //LAB_800dd4b8
    //LAB_800dd4bc
    return a0;
  }

  @Method(0x800dd4ccL)
  public static int applyStandardAnimation(final Model124 model, final int animationTicks) {
    if(model.animationState_9c == 2) {
      return 2;
    }

    //LAB_800dd4fc
    final int totalFrames;
    final int frame;
    if(model.disableInterpolation_a2) {
      frame = animationTicks % (model.totalFrames_9a / 2);
      model.partTransforms_94 = Arrays.copyOfRange(model.partTransforms_90, frame, model.partTransforms_90.length);
      applyModelPartTransforms(model);
      totalFrames = (short)model.totalFrames_9a >> 1;
    } else {
      //LAB_800dd568
      frame = animationTicks % model.totalFrames_9a;
      model.partTransforms_94 = Arrays.copyOfRange(model.partTransforms_90, frame / 2, model.partTransforms_90.length);
      applyModelPartTransforms(model);

      if((frame & 0x1) != 0 && frame != model.totalFrames_9a - 1 && model.ub_a3 == 0) { // Interpolation frame
        final ModelPartTransforms0c[][] original = model.partTransforms_94;
        applyInterpolationFrame(model);
        model.partTransforms_94 = original;
      }

      //LAB_800dd5ec
      totalFrames = model.totalFrames_9a;
    }

    //LAB_800dd5f0
    model.remainingFrames_9e = totalFrames - frame - 1;
    model.interpolationFrameIndex = 0;

    if(model.remainingFrames_9e == 0) {
      model.animationState_9c = 0;
    } else {
      //LAB_800dd618
      model.animationState_9c = 1;
    }

    //LAB_800dd61c
    //LAB_800dd620
    return model.remainingFrames_9e;
  }

  @Method(0x800dd638L)
  public static int applyLmbAnimation(final Model124 model, final int animationTicks) {
    if(model.animationState_9c == 2) {
      return 2;
    }

    //LAB_800dd680
    final int count = Math.min(model.modelParts_00.length, model.partCount_98);

    //LAB_800dd69c
    final LmbType0 lmb = (LmbType0)model.lmbAnim_08.lmb_00;

    final int a0_0;
    final int frame;
    final int remainingFrames;
    final int isInterpolationFrame;
    if(model.disableInterpolation_a2) {
      isInterpolationFrame = 0;
      a0_0 = (animationTicks << 1) % model.totalFrames_9a >>> 1;
      remainingFrames = (model.totalFrames_9a >> 1) - a0_0;
    } else {
      //LAB_800dd6dc
      frame = animationTicks % model.totalFrames_9a;
      isInterpolationFrame = (animationTicks & 0x1) << 11; // Dunno why this is shifted, makes no difference
      a0_0 = frame >>> 1;
      remainingFrames = model.totalFrames_9a - frame;
    }

    //LAB_800dd700
    model.remainingFrames_9e = remainingFrames - 1;
    model.interpolationFrameIndex = 0;

    //LAB_800dd720
    for(int i = 0; i < count; i++) {
      final LmbTransforms14 transforms = lmb._08[i]._08[a0_0];
      final MATRIX matrix = model.modelParts_00[i].coord2_04.coord;

      final VECTOR trans = new VECTOR();
      trans.set(transforms.trans_06);

      if(isInterpolationFrame != 0) { // Interpolation frame
        final LmbTransforms14 nextFrame;
        if(animationTicks == model.totalFrames_9a - 1) {
          nextFrame = lmb._08[i]._08[0]; // Wrap around to frame 0
        } else {
          //LAB_800dd7cc
          nextFrame = lmb._08[i]._08[a0_0 + 1];
        }

        //LAB_800dd7d0
        trans.set(
          (trans.getX() + nextFrame.trans_06.getX()) / 2,
          (trans.getY() + nextFrame.trans_06.getY()) / 2,
          (trans.getZ() + nextFrame.trans_06.getZ()) / 2
        );
      }

      //LAB_800dd818
      RotMatrix_Zyx(transforms.rot_0c, matrix);
      matrix.transfer.set(trans);
      matrix.scaleL(transforms.scale_00);
    }

    //LAB_800dd84c
    if(model.remainingFrames_9e == 0) {
      model.animationState_9c = 0;
    } else {
      //LAB_800dd864
      model.animationState_9c = 1;
    }

    //LAB_800dd868
    //LAB_800dd86c
    return model.remainingFrames_9e;
  }

  /**
   * used renderCtmd
   */
  @Method(0x800dd89cL)
  public static void FUN_800dd89c(final Model124 model, final int newAttribute) {
    final long v0;
    final long v1;
    long s6;
    zOffset_1f8003e8.set(model.zOffset_a0);
    tmdGp0Tpage_1f8003ec.set(model.tpage_108);
    s6 = deffManager_800c693c.flags_20 & 0x4;
    v1 = (int)s6 >> 1;
    v0 = (int)s6 >> 2;
    s6 = v1 | v0;

    //LAB_800dd928
    for(int i = 0; i < model.modelParts_00.length; i++) {
      final ModelPart10 s2 = model.modelParts_00[i];

      //LAB_800dd940
      if((model.partInvisible_f4 & 1L << i) == 0) {
        final MATRIX lw = new MATRIX();
        final MATRIX ls = new MATRIX();
        GsGetLws(s2.coord2_04, lw, ls);

        if((s6 & (ls.transfer.getZ() ^ tickCount_800bb0fc.get())) == 0 || ls.transfer.getZ() - ls.transfer.getX() >= -0x800 && ls.transfer.getZ() + ls.transfer.getX() >= -0x800 && ls.transfer.getZ() - ls.transfer.getY() >= -0x800 && ls.transfer.getZ() + ls.transfer.getY() >= -0x800) {
          //LAB_800dd9bc
          if((newAttribute & 0x8) != 0) {
            lw.normalize();
          }

          //LAB_800dd9d8
          GsSetLightMatrix(lw);
          GTE.setRotationMatrix(ls);
          GTE.setTranslationVector(ls.transfer);

          final int oldAttrib = s2.attribute_00;
          s2.attribute_00 = newAttribute;

          final int oldZShift = zShift_1f8003c4.get();
          final int oldZMax = zMax_1f8003cc.get();
          final int oldZMin = zMin;
          zShift_1f8003c4.set(2);
          zMax_1f8003cc.set(0xffe);
          zMin = 0xb;
          Renderer.renderDobj2(s2, false, 0x20);
          zShift_1f8003c4.set(oldZShift);
          zMax_1f8003cc.set(oldZMax);
          zMin = oldZMin;

          s2.attribute_00 = oldAttrib;
        }
      }
    }

    //LAB_800dda54
    //LAB_800dda58
    for(int i = 0; i < 7; i++) {
      if(model.animateTextures_ec[i]) {
        animateModelTextures(model, i);
      }

      //LAB_800dda70
    }

    if(model.shadowType_cc != 0) {
      renderShadow(model);
    }

    //LAB_800dda98
  }

  @Method(0x800ddac8L)
  public static void loadModelTmd(final Model124 model, final CContainer extTmd) {
    final VECTOR sp0x18 = new VECTOR().set(model.coord2_14.coord.transfer);

    //LAB_800ddb18
    for(int i = 0; i < 7; i++) {
      model.animateTextures_ec[i] = false;
    }

    final TmdWithId tmdWithId = extTmd.tmdPtr_00;
    final Tmd tmd = tmdWithId.tmd;

    final int count = tmd.header.nobj;
    model.modelParts_00 = new ModelPart10[count];

    Arrays.setAll(model.modelParts_00, i -> new ModelPart10());

    model.tpage_108 = (int)((tmdWithId.id & 0xffff_0000L) >>> 11);
    initObjTable2(model.modelParts_00);
    GsInitCoordinate2(null, model.coord2_14);

    //LAB_800ddc0c
    for(int i = 0; i < count; i++) {
      if((tmd.header.flags & 0x2) != 0) { // CTMD, no longer used
        model.modelParts_00[i].tmd_08 = tmd.objTable[i];
      } else {
        final ModelPart10 dobj2 = new ModelPart10();
        dobj2.tmd_08 = tmd.objTable[i];
        model.modelParts_00[i].tmd_08 = dobj2.tmd_08;
      }

      //LAB_800ddc34
    }

    //LAB_800ddc54
    //LAB_800ddc64
    for(int i = 0; i < count; i++) {
      model.modelParts_00[i].coord2_04.super_ = model.coord2_14;
    }

    //LAB_800ddc80
    model.disableInterpolation_a2 = false;
    model.ub_a3 = 0;
    model.partInvisible_f4 = 0;
    model.zOffset_a0 = 0;
    model.coord2_14.coord.transfer.set(sp0x18);

    if((tmd.header.flags & 0x2) == 0 && model.colourMap_9d != 0) {
      adjustModelUvs(model);
    }

    //LAB_800ddce8
    model.coord2_14.transforms.scale.set(1.0f, 1.0f, 1.0f);
    model.shadowType_cc = 0;
    model.shadowSize_10c.set(1.0f, 1.0f, 1.0f);
    model.shadowOffset_118.set(0, 0, 0);
  }

  @Method(0x800ddd3cL)
  public static int applyCmbAnimation(final Model124 model, final int animationTicks) {
    if(model.animationState_9c == 2) {
      return 2;
    }

    //LAB_800ddd9c
    final Model124.CmbAnim cmbAnim = model.cmbAnim_08;

    if(animationTicks == cmbAnim.animationTicks_00) {
      return model.animationState_9c;
    }

    final Cmb cmb = cmbAnim.cmb_04;

    // Note: these two variables _should_ be the same
    final int modelPartCount = cmb.modelPartCount_0c;
    final int count = Math.min(model.modelParts_00.length, model.partCount_98);

    //LAB_800dddc4
    int frameIndex;
    final int a1_0;
    final int isInterpolationFrame;
    if(model.disableInterpolation_a2) {
      isInterpolationFrame = 0;
      a1_0 = (animationTicks << 1) % model.totalFrames_9a >>> 1;
      frameIndex = (cmbAnim.animationTicks_00 << 1) % model.totalFrames_9a >> 1;
      model.remainingFrames_9e = (model.totalFrames_9a >> 1) - a1_0 - 1;
    } else {
      //LAB_800dde1c
      // This modulo has to be unsigned due to a bug causing the number of ticks
      // to go negative. This matches the retail behaviour (it uses divu).
      final int frame = (int)((animationTicks & 0xffff_ffffL) % model.totalFrames_9a);
      isInterpolationFrame = (animationTicks & 0x1) << 11; // Dunno why this is shifted, makes no difference
      a1_0 = frame >>> 1;
      frameIndex = cmbAnim.animationTicks_00 % model.totalFrames_9a >> 1;
      model.remainingFrames_9e = model.totalFrames_9a - frame - 1;

      // This is another retail bug - it's possible for the frame index to go negative
      if(frameIndex < 0) {
        frameIndex = 0;
      }
    }

    model.interpolationFrameIndex = 0;

    //LAB_800dde60
    if(frameIndex > a1_0) {
      //LAB_800dde88
      for(int partIndex = 0; partIndex < modelPartCount; partIndex++) {
        final ModelPartTransforms0c fileTransforms = cmb.partTransforms_10[0][partIndex];
        final ModelPartTransforms0c modelTransforms = cmbAnim.transforms_08[partIndex];

        modelTransforms.rotate_00.set(fileTransforms.rotate_00);
        modelTransforms.translate_06.set(fileTransforms.translate_06);
      }

      //LAB_800ddee0
      cmbAnim.animationTicks_00 = 0;
      frameIndex = 0;
    }

    //LAB_800ddeec
    //LAB_800ddf1c
    for(; frameIndex < a1_0; frameIndex++) {
      //LAB_800ddf2c
      for(int partIndex = 0; partIndex < modelPartCount; partIndex++) {
        final Cmb.SubTransforms08 subTransforms = cmb.subTransforms[frameIndex][partIndex];
        final ModelPartTransforms0c modelTransforms = cmbAnim.transforms_08[partIndex];

        modelTransforms.rotate_00.add(subTransforms.rot_01);
        modelTransforms.translate_06.add(subTransforms.trans_05);
      }

      //LAB_800ddfd4
    }

    //LAB_800ddfe4
    //LAB_800de158
    if(isInterpolationFrame != 0 && model.ub_a3 == 0 && a1_0 != (model.totalFrames_9a >> 1) - 1) { // Interpolation frame
      //LAB_800de050
      for(int i = 0; i < count; i++) {
        final Cmb.SubTransforms08 subTransforms = cmb.subTransforms[a1_0][i];
        final ModelPartTransforms0c modelTransforms = cmbAnim.transforms_08[i];

        final MATRIX modelPartMatrix = model.modelParts_00[i].coord2_04.coord;
        RotMatrix_Zyx(modelTransforms.rotate_00, modelPartMatrix);
        modelPartMatrix.transfer.set(modelTransforms.translate_06);

        final Vector3f rotation = new Vector3f();
        rotation.set(modelTransforms.rotate_00).add(subTransforms.rot_01);

        final MATRIX translation = new MATRIX();
        RotMatrix_Zyx(rotation, translation);
        translation.transfer.set(
          (int)(modelTransforms.translate_06.x + subTransforms.trans_05.x),
          (int)(modelTransforms.translate_06.y + subTransforms.trans_05.y),
          (int)(modelTransforms.translate_06.z + subTransforms.trans_05.z)
        );

        lerp(modelPartMatrix, translation, 0x800);
      }
    } else {
      //LAB_800de164
      for(int i = 0; i < count; i++) {
        final ModelPartTransforms0c modelTransforms = cmbAnim.transforms_08[i];
        final MATRIX modelPartMatrix = model.modelParts_00[i].coord2_04.coord;
        RotMatrix_Zyx(modelTransforms.rotate_00, modelPartMatrix);
        modelPartMatrix.transfer.set(modelTransforms.translate_06);
      }
    }

    //LAB_800de1b4
    if(model.remainingFrames_9e == 0) {
      model.animationState_9c = 0;
    } else {
      //LAB_800de1cc
      model.animationState_9c = 1;
    }

    //LAB_800de1d0
    cmbAnim.animationTicks_00 = animationTicks;

    //LAB_800de1e0
    return model.remainingFrames_9e;
  }

  @Method(0x800de210L)
  public static void loadModelCmb(final Model124 model, final Cmb cmb) {
    final Model124.CmbAnim anim = model.cmbAnim_08;
    final int count = cmb.modelPartCount_0c;

    anim.cmb_04 = cmb;
    anim.transforms_08 = new ModelPartTransforms0c[count];

    Arrays.setAll(anim.transforms_08, i -> new ModelPartTransforms0c());

    model.animType_90 = 2;
    model.lmbUnknown_94 = 0;
    model.partCount_98 = count;
    model.totalFrames_9a = cmb.totalFrames_0e * 2;
    model.animationState_9c = 1;
    model.remainingFrames_9e = cmb.totalFrames_0e * 2;
    model.interpolationFrameIndex = 0;

    //LAB_800de270
    for(int i = 0; i < count; i++) {
      final ModelPartTransforms0c v1 = cmb.partTransforms_10[0][i];
      final ModelPartTransforms0c a1_0 = anim.transforms_08[i];
      a1_0.rotate_00.set(v1.rotate_00);
      a1_0.translate_06.set(v1.translate_06);
    }

    //LAB_800de2c8
    anim.animationTicks_00 = 1;
    applyCmbAnimation(model, 0);
  }

  @Method(0x800de2e8L)
  public static void applyAnimation(final Model124 model, final int animationTicks) {
    final int type = model.animType_90;
    if(type == 1) {
      //LAB_800de318
      applyLmbAnimation(model, animationTicks);
    } else if(type == 0 || type == 2) {
      //LAB_800de328
      applyCmbAnimation(model, animationTicks);
    } else {
      //LAB_800de338
      applyStandardAnimation(model, animationTicks);
    }

    //LAB_800de340
  }

  @Method(0x800de36cL)
  public static void loadModelAnim(final Model124 model, final Anim anim) {
    if(anim.magic_00 == Cmb.MAGIC) { // "CMB "
      loadModelCmb(model, (Cmb)anim);
      //LAB_800de398
    } else if(anim.magic_00 == Lmb.MAGIC) { // "LMB"
      final LmbType0 lmb = (LmbType0)anim;

      model.lmbAnim_08.lmb_00 = lmb;
      model.animType_90 = 1;
      model.lmbUnknown_94 = 0;
      model.partCount_98 = lmb.objectCount_04;
      model.totalFrames_9a = lmb._08[0].count_04 * 2;
      model.animationState_9c = 1;
      model.remainingFrames_9e = lmb._08[0].count_04 * 2;
      model.interpolationFrameIndex = 0;
    } else {
      //LAB_800de3dc
      loadModelStandardAnimation(model, (TmdAnimationFile)anim);
    }

    //LAB_800de3e4
  }

  /** used renderCtmd */
  @Method(0x800de3f4L)
  public static void renderTmdSpriteEffect(final TmdObjTable1c a0, final EffectManagerData6cInner<?> a1, final MATRIX a2) {
    final int s0 = deffManager_800c693c.flags_20 & 0x4;

    final MATRIX sp0x10 = new MATRIX();
    if((a1.flags_00 & 0x8) != 0) {
      a2.normalize(sp0x10);
      GsSetLightMatrix(sp0x10);
    } else {
      //LAB_800de458
      GsSetLightMatrix(a2);
    }

    //LAB_800de45c
    a2.compose(worldToScreenMatrix_800c3548, sp0x10);

    if((a1.flags_00 & 0x400_0000) == 0) {
      RotMatrix_Xyz(a1.rot_10, sp0x10);
      sp0x10.scaleL(a1.scale_16);
    }

    //LAB_800de4a8
    if(((s0 >> 1 | s0 >> 2) & (sp0x10.transfer.getZ() ^ tickCount_800bb0fc.get())) == 0 || sp0x10.transfer.getZ() - sp0x10.transfer.getX() >= -0x800 && sp0x10.transfer.getZ() + sp0x10.transfer.getX() >= -0x800 && sp0x10.transfer.getZ() - sp0x10.transfer.getY() >= -0x800 && sp0x10.transfer.getZ() + sp0x10.transfer.getY() >= -0x800) {
      //LAB_800de50c
      setRotTransMatrix(sp0x10);

      final ModelPart10 dobj2 = new ModelPart10();
      dobj2.attribute_00 = a1.flags_00;
      dobj2.tmd_08 = a0;

      final int oldZShift = zShift_1f8003c4.get();
      final int oldZMax = zMax_1f8003cc.get();
      final int oldZMin = zMin;
      zShift_1f8003c4.set(2);
      zMax_1f8003cc.set(0xffe);
      zMin = 0xb;
      Renderer.renderDobj2(dobj2, false, 0x20);
      zShift_1f8003c4.set(oldZShift);
      zMax_1f8003cc.set(oldZMax);
      zMin = oldZMin;
    }

    //LAB_800de528
  }

  @Method(0x800de544L)
  public static Vector3f getRotationFromTransforms(final Vector3f rotOut, final MATRIX transforms) {
    final MATRIX mat = new MATRIX().set(transforms);
    rotOut.x = MathHelper.atan2(-mat.get(5), mat.get(8));
    mat.rotateX(-rotOut.x);
    rotOut.y = MathHelper.atan2(mat.get(2), mat.get(8));
    mat.rotateY(-rotOut.y);
    rotOut.z = MathHelper.atan2(mat.get(3), mat.get(0));
    return rotOut;
  }

  @Method(0x800de618L)
  public static void getRotationAndScaleFromTransforms(final Vector3f rotOut, final Vector3f scaleOut, final MATRIX transforms) {
    final MATRIX mat = new MATRIX().set(transforms);
    rotOut.x = MathHelper.atan2(-mat.get(5), mat.get(8));
    mat.rotateX(-rotOut.x);
    rotOut.y = MathHelper.atan2(mat.get(2), mat.get(8));
    mat.rotateY(-rotOut.y);
    rotOut.z = MathHelper.atan2(mat.get(3), mat.get(0));
    mat.rotateZ(-rotOut.z);
    scaleOut.set(mat.get(0) / (float)0x1000, mat.get(4) / (float)0x1000, mat.get(8) / (float)0x1000);
  }

  @Method(0x800de76cL)
  public static TmdObjTable1c optimisePacketsIfNecessary(final TmdWithId tmd, final int objIndex) {
    if((tmd.tmd.header.flags & 0x2) == 0) {
      final ModelPart10 dobj2 = new ModelPart10();
      dobj2.tmd_08 = tmd.tmd.objTable[objIndex];
      return dobj2.tmd_08;
    }

    //LAB_800de7a0
    //LAB_800de7b4
    return tmd.tmd.objTable[objIndex];
  }
}
