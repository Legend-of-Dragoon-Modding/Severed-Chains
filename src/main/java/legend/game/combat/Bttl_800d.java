package legend.game.combat;

import legend.core.Config;
import legend.core.MathHelper;
import legend.core.RenderEngine;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gte.MV;
import legend.core.gte.ModelPart10;
import legend.core.gte.Tmd;
import legend.core.gte.TmdObjTable1c;
import legend.core.gte.TmdWithId;
import legend.core.memory.Method;
import legend.core.memory.types.FloatRef;
import legend.core.opengl.Obj;
import legend.core.opengl.TmdObjLoader;
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
import legend.game.combat.effects.EffectManagerParams;
import legend.game.combat.effects.GuardEffect06;
import legend.game.combat.effects.ModelEffect13c;
import legend.game.combat.effects.MonsterDeathEffect34;
import legend.game.combat.effects.MonsterDeathEffectObjectDestructor30;
import legend.game.combat.effects.ProjectileHitEffect14;
import legend.game.combat.effects.ProjectileHitEffect14Sub48;
import legend.game.combat.effects.RadialGradientEffect14;
import legend.game.combat.effects.SpTextEffect40;
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
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Arrays;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.RENDERER;
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
import static legend.game.Scus94491BpeSegment_8002.adjustModelUvs;
import static legend.game.Scus94491BpeSegment_8002.animateModelTextures;
import static legend.game.Scus94491BpeSegment_8002.applyInterpolationFrame;
import static legend.game.Scus94491BpeSegment_8002.applyModelPartTransforms;
import static legend.game.Scus94491BpeSegment_8002.initObjTable2;
import static legend.game.Scus94491BpeSegment_8002.loadModelStandardAnimation;
import static legend.game.Scus94491BpeSegment_8003.GsGetLws;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_8003.getProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.getScreenOffset;
import static legend.game.Scus94491BpeSegment_8004.doNothingScript_8004f650;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800c.lightColourMatrix_800c3508;
import static legend.game.Scus94491BpeSegment_800c.lightDirectionMatrix_800c34e8;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;
import static legend.game.combat.Bttl_800c.FUN_800cfb14;
import static legend.game.combat.Bttl_800c._800faa90;
import static legend.game.combat.Bttl_800c._800faa92;
import static legend.game.combat.Bttl_800c._800faa94;
import static legend.game.combat.Bttl_800c._800faa9d;
import static legend.game.combat.Bttl_800c.additionNames_800fa8d4;
import static legend.game.combat.Bttl_800c.asciiTable_800fa788;
import static legend.game.combat.Bttl_800c.buttonPressHudMetrics_800faaa0;
import static legend.game.combat.Bttl_800c.camera_800c67f0;
import static legend.game.combat.Bttl_800c.charWidthAdjustTable_800fa7cc;
import static legend.game.combat.Bttl_800c.radialGradientEffectRenderers_800fa758;
import static legend.game.combat.Bttl_800c.screenOffsetX_800c67bc;
import static legend.game.combat.Bttl_800c.screenOffsetY_800c67c0;
import static legend.game.combat.Bttl_800c.seed_800fa754;
import static legend.game.combat.Bttl_800c.spriteMetrics_800c6948;
import static legend.game.combat.Bttl_800e.allocateEffectManager;
import static legend.game.combat.Bttl_800e.renderBttlShadow;
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

  @ScriptDescription("Allocates a projectile hit effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "count", description = "The effect count")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "r", description = "The red channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "g", description = "The green channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "b", description = "The blue channel")
  @Method(0x800d0564L)
  public static FlowControl scriptAllocateProjectileHitEffect(final RunningScript<? extends BattleObject> script) {
    final int count = script.params_20[1].get();

    final ProjectileHitEffect14 effect = new ProjectileHitEffect14(count);

    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager(
      "ProjectileHitEffect14",
      script.scriptState_04,
      null,
      effect::renderProjectileHitEffect,
      null,
      effect
    );

    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;

    //LAB_800d0634
    for(int i = 0; i < count; i++) {
      final ProjectileHitEffect14Sub48 struct = effect._08[i];

      struct.used_00 = true;
      struct.r_34 = script.params_20[2].get() << 8;
      struct.g_36 = script.params_20[3].get() << 8;
      struct.b_38 = script.params_20[4].get() << 8;

      final short x = (short)(seed_800fa754.nextInt(301) + 200);
      final short y = (short)(seed_800fa754.nextInt(401) - 300);
      final short z = (short)(seed_800fa754.nextInt(601) - 300);
      struct._24[0].set(x, y, z);
      struct._24[1].set(x, y, z);

      struct._04[0].x = 0.0f;
      struct._04[0].y = seed_800fa754.nextInt(101) - 50;
      struct._04[0].z = seed_800fa754.nextInt(101) - 50;
      struct.frames_44 = seed_800fa754.nextInt(9) + 7;

      struct._40 = 0;
      struct._24[1].y += 25.0f;
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

    final AdditionSparksEffect08 effect = new AdditionSparksEffect08(count);

    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager(
      "AdditionSparksEffect08",
      script.scriptState_04,
      null,
      effect::renderAdditionSparks,
      null,
      effect
    );

    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;

    final int s1 = script.params_20[5].get() / s4;

    //LAB_800d0ee0
    for(int i = 0; i < count; i++) {
      final AdditionSparksEffectInstance4c inst = effect.instances_04[i];

      inst.ticksExisted_00 = 0;

      inst.delay_04 = (byte)(seed_800fa754.nextInt(s4 + 1));
      inst.ticksRemaining_05 = (byte)(seed_800fa754.nextInt(9) + 7);

      inst.startPos_08.set(inst.delay_04 * s1, 0, 0);
      inst.endPos_18.set(0, 0, 0);
      inst.speed_28.set(seed_800fa754.nextInt(201), seed_800fa754.nextInt(201) - 100, seed_800fa754.nextInt(201) - 100);
      inst.acceleration_38.set(0.0f, 15.0f, 0.0f);

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

  @ScriptDescription("Allocates an addition starburst effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "parentIndex", description = "The parent battle entity index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "rayCount", description = "The number of rays")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "Controls how the effect behaves")
  @Method(0x800d19ecL)
  public static FlowControl scriptAllocateAdditionStarburstEffect(final RunningScript<? extends BattleObject> script) {
    final int rayCount = script.params_20[2].get();

    final AdditionStarburstEffect10 effect = new AdditionStarburstEffect10(rayCount);

    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager(
      "AdditionStarburstEffect10",
      script.scriptState_04,
      null,
      effect.additionStarburstRenderers_800c6dc4[script.params_20[3].get()],
      null,
      effect
    );

    effect.parentIndex_00 = script.params_20[1].get();
    effect.unused_08 = 0;
    final AdditionStarburstEffectRay10[] rayArray = effect.rayArray_0c;

    //LAB_800d1ac4
    for(int rayNum = 0; rayNum < rayCount; rayNum++) {
      rayArray[rayNum].renderRay_00 = true;
      rayArray[rayNum].angle_02 = seed_800fa754.nextFloat(MathHelper.TWO_PI);
      rayArray[rayNum].unused_04 = 16;
      rayArray[rayNum].endpointTranslationMagnitude_06 = (short)(seed_800fa754.nextInt(31));
      rayArray[rayNum].endpointTranslationMagnitudeVelocity_08 = (short)(seed_800fa754.nextInt(21) + 10);
      rayArray[rayNum].angleModifier_0a = MathHelper.psxDegToRad(seed_800fa754.nextInt(11) - 5);
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
  public static void renderDiscGradientEffect(final EffectManagerData6c<EffectManagerParams.RadialGradientType> manager, final int angle, final Vector2f[] vertices, final RadialGradientEffect14 effect, final Translucency translucency) {
    if(manager.params_10.flags_00 >= 0) {
      GPU.queueCommand((effect.z_04 + manager.params_10.z_22) / 4.0f, new GpuCommandPoly(3)
        .translucent(translucency)
        .rgb(0, manager.params_10.colour_1c)
        .rgb(1, effect.r_0c, effect.g_0d, effect.b_0e)
        .rgb(2, effect.r_0c, effect.g_0d, effect.b_0e)
        .pos(0, vertices[0].x, vertices[0].y)
        .pos(1, vertices[1].x, vertices[1].y)
        .pos(2, vertices[2].x, vertices[2].y)
      );
    }

    //LAB_800d1e70
  }

  @Method(0x800d1e80L)
  public static void FUN_800d1e80(final EffectManagerData6c<EffectManagerParams.RadialGradientType> manager, final int angle, final Vector2f[] vertices, final RadialGradientEffect14 effect, final Translucency translucency) {
    throw new RuntimeException("Not implemented");
  }

  /** Renders things like the ring effect when using a healing potion */
  @Method(0x800d21b8L)
  public static void renderRingGradientEffect(final EffectManagerData6c<EffectManagerParams.RadialGradientType> manager, final int angle, final Vector2f[] vertices, final RadialGradientEffect14 effect, final Translucency translucency) {
    if(manager.params_10.flags_00 >= 0) {
      //TODO why does rsin/rcos not have to be >> 12?
      final Vector3f sp0x20 = new Vector3f(
        rcos(angle) * (manager.params_10.scale_16.x / effect.scaleModifier_01 + (manager.params_10.size_28 >> 12)),
        rsin(angle) * (manager.params_10.scale_16.y / effect.scaleModifier_01 + (manager.params_10.size_28 >> 12)),
        manager.params_10.z_2c
      );

      final Vector2f screenVert0 = new Vector2f();
      FUN_800cfb14(manager, sp0x20, screenVert0);

      //TODO why does rsin/rcos not have to be >> 12?
      final Vector3f sp0x30 = new Vector3f(
        rcos(angle + effect.angleStep_08) * (manager.params_10.scale_16.x / effect.scaleModifier_01 + (manager.params_10.size_28 >> 12)),
        rsin(angle + effect.angleStep_08) * (manager.params_10.scale_16.y / effect.scaleModifier_01 + (manager.params_10.size_28 >> 12)),
        manager.params_10.z_2c
      );

      final Vector2f screenVert1 = new Vector2f();
      FUN_800cfb14(manager, sp0x30, screenVert1);

      GPU.queueCommand((effect.z_04 + manager.params_10.z_22) / 4.0f, new GpuCommandPoly(4)
        .translucent(translucency)
        .rgb(0, manager.params_10.colour_1c)
        .rgb(1, manager.params_10.colour_1c)
        .rgb(2, effect.r_0c, effect.g_0d, effect.b_0e)
        .rgb(3, effect.r_0c, effect.g_0d, effect.b_0e)
        .pos(0, screenVert0.x, screenVert0.y)
        .pos(1, screenVert1.x, screenVert1.y)
        .pos(2, vertices[1].x, vertices[1].y)
        .pos(3, vertices[2].x, vertices[2].y)
      );
    }

    //LAB_800d2460
  }

  @ScriptDescription("Allocates a radial gradient effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "count", description = "The number of subdivisions in the gradient")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "The gradient type")
  @Method(0x800d2734L)
  public static FlowControl scriptAllocateRadialGradientEffect(final RunningScript<? extends BattleObject> script) {
    final int circleSubdivisionModifier = script.params_20[1].get();
    final int type = script.params_20[2].get();

    final RadialGradientEffect14 effect = new RadialGradientEffect14();

    final ScriptState<EffectManagerData6c<EffectManagerParams.RadialGradientType>> state = allocateEffectManager(
      "RadialGradientEffect14",
      script.scriptState_04,
      null,
      effect::renderRadialGradientEffect,
      null,
      effect,
      new EffectManagerParams.RadialGradientType()
    );

    final EffectManagerData6c<EffectManagerParams.RadialGradientType> manager = state.innerStruct_00;

    //LAB_800d27b4
    manager.params_10.scale_16.set(1.0f, 1.0f, 1.0f);

    effect.circleSubdivisionModifier_00 = circleSubdivisionModifier;
    effect.scaleModifier_01 = (type - 3 & 0xffff_ffffL) >= 2 ? 4.0f : 1.0f;
    effect.renderer_10 = radialGradientEffectRenderers_800fa758[type];
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates a guard effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @Method(0x800d2ff4L)
  public static FlowControl scriptAllocateGuardEffect(final RunningScript<? extends BattleObject> script) {
    final GuardEffect06 effect = new GuardEffect06();

    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager(
      "GuardEffect06",
      script.scriptState_04,
      null,
      effect::renderGuardEffect,
      null,
      effect
    );

    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;
    effect._00 = 1;
    effect._02 = 0;
    effect._04 = 0;

    // Hack to make shield color default if counter overlay color is default
    // Otherwise, just use the overlay color. Maybe we can make shields toggleable later.
    final int rgb = Config.getCounterOverlayRgb();
    if(Config.changeAdditionOverlayRgb() && rgb != 0x2060d8) {
      manager.params_10.colour_1c.set(rgb & 0xff, rgb >> 8 & 0xff, rgb >> 16 & 0xff);
    } else {
      manager.params_10.colour_1c.set(255, 0, 0);
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

  @ScriptDescription("Allocates a monster death effect effect for a monster battle entity")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The battle object index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "spriteIndex", description = "Which sprite to use")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "unused", description = "Unused in code but passed by scripts")
  @Method(0x800d34bcL)
  public static FlowControl scriptAllocateMonsterDeathEffect(final RunningScript<? extends BattleObject> script) {
    final BattleEntity27c bent = SCRIPTS.getObject(script.params_20[1].get(), BattleEntity27c.class);
    final int modelObjectCount = bent.model_148.partCount_98;

    final MonsterDeathEffect34 deathEffect = new MonsterDeathEffect34(modelObjectCount);

    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager(
      "MonsterDeathEffect34",
      script.scriptState_04,
      deathEffect::monsterDeathEffectTicker,
      deathEffect::monsterDeathEffectRenderer,
      null,
      deathEffect
    );

    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;

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
    deathEffect.sprite_0c.flags_00 = manager.params_10.flags_00 & 0xffff_ffffL;
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

  /**
   * NOTE: changed param from reference to value
   */
  @Method(0x800d3910L)
  public static int getCharDisplayWidth(final long chr) {
    //LAB_800d391c
    int charTableOffset;
    for(charTableOffset = 0; ; charTableOffset++) {
      if(asciiTable_800fa788[charTableOffset] == 0) {
        charTableOffset = 0;
        break;
      }

      if(chr == asciiTable_800fa788[charTableOffset]) {
        break;
      }
    }

    //LAB_800d3944
    //LAB_800d3948
    return 10 - charWidthAdjustTable_800fa7cc[charTableOffset];
  }

  @Method(0x800d3968L)
  public static int[] setAdditionNameDisplayCoords(final int addition) {
    final String additionName = additionNames_800fa8d4[addition];

    int additionDisplayWidth = 0;
    //LAB_800d39b8
    for(int i = 0; i < additionName.length(); i++) {
      additionDisplayWidth += getCharDisplayWidth(additionName.charAt(i));
    }

    //LAB_800d39ec
    return new int[] {144 - additionDisplayWidth, 64};
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
      final AdditionNameTextEffect1c additionStruct = new AdditionNameTextEffect1c();
      final int addition = gameState_800babc8.charData_32c[script.params_20[0].get()].selectedAddition_19;
      final ScriptState<AdditionNameTextEffect1c> state = SCRIPTS.allocateScriptState("AdditionNameTextEffect1c", additionStruct);
      state.loadScriptFile(doNothingScript_8004f650);
      state.setTicker(additionStruct::tickAdditionNameEffect);
      final String additionName = additionNames_800fa8d4[addition];

      //LAB_800d3e5c
      //LAB_800d3e7c
      additionStruct.addition_02 = addition;
      additionStruct.length_08 = additionName.length();
      additionStruct.positionMovement_0c = 120;
      additionStruct.renderer_14 = additionStruct::renderAdditionNameChar;
      additionStruct.ptr_18 = new AdditionCharEffectData0c[additionStruct.length_08];
      Arrays.setAll(additionStruct.ptr_18, i -> new AdditionCharEffectData0c());
      _800faa9d.set(1);

      final int[] displayOffset = setAdditionNameDisplayCoords(addition);
      int charPosition = -160;
      int displayOffsetX = displayOffset[0];
      final int displayOffsetY = displayOffset[1];

      //LAB_800d3f18
      for(int charIdx = 0; charIdx < additionName.length(); charIdx++) {
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
  public static void FUN_800d3f98(final short x, final short y, final int a2, final short a3, final int brightness) {
    renderButtonPressHudTexturedRect(x, y, a2 * 8 + 16 & 0xf8, 40, 8, 16, a3, Translucency.B_PLUS_F, brightness, 0x1000);
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
      final SpTextEffect40 s1 = new SpTextEffect40();
      final ScriptState<SpTextEffect40> state = SCRIPTS.allocateScriptState("SpTextEffect40", s1);
      state.loadScriptFile(doNothingScript_8004f650);
      state.setTicker(s1::tickSpTextEffect);

      s1._08 = s2;
      s1._1c = _800faa90.get() << 8;

      if(s3 == 1) {
        _800faa92.set((short)0);
        _800faa94.set(s3);
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

      //LAB_800d44dc
      for(int i = 0; i < 8; i++) {
        s1.charArray_3c[i]._00 = s1._0c;
        s1.charArray_3c[i]._04 = s1._10;
      }

      final int strLen = String.valueOf(s1._08).length();

      final int v1;
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
      final AdditionNameTextEffect1c s0 = new AdditionNameTextEffect1c();
      final ScriptState<AdditionNameTextEffect1c> state = SCRIPTS.allocateScriptState("AdditionScriptData1c", s0);
      state.loadScriptFile(doNothingScript_8004f650);
      state.setTicker(s0::tickAdditionNameEffect);
      s0.ptr_18 = new AdditionCharEffectData0c[] {new AdditionCharEffectData0c()};
      s0.positionMovement_0c = 40;
      s0.renderer_14 = s0::renderAdditionNameEffect;
      s0.length_08 = 1;
      s0._10 = s2;
      final AdditionCharEffectData0c struct = s0.ptr_18[0];
      struct.scrolling_00 = 1;
      struct.dupes_02 = 8;
      struct.position_04 = -160;
      struct.offsetY_06 = 96;
      struct.offsetX_08 = 144 - (String.valueOf(s2).length() + 4) * 8;
      struct.offsetY_0a = 96;
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
    renderButtonPressHudElement1(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), Translucency.of(script.params_20[3].get()), script.params_20[4].get());
    return FlowControl.CONTINUE;
  }

  public static void renderButtonPressHudElement1(final int type, final int x, final int y, final Translucency translucency, final int brightness) {
    final ButtonPressHudMetrics06 metrics = buttonPressHudMetrics_800faaa0[type];

    if(metrics.hudElementType_00 == 0) {
      renderButtonPressHudTexturedRect(x, y, metrics.u_01, metrics.v_02, metrics.wOrRightU_03, metrics.hOrBottomV_04, metrics.clutOffset_05, translucency, brightness, 0x1000);
    } else {
      renderButtonPressHudElement(x, y, metrics.u_01, metrics.v_02, metrics.wOrRightU_03, metrics.hOrBottomV_04, metrics.clutOffset_05, translucency, brightness, 0x1000, 0x1000);
    }
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
      cam.setInitialAndFinalCameraVelocities(mode - 1, stepZ1, projectionPlaneDelta, projectionPlaneChangeFrames, initialStepZ, finalStepZ);
      cam.projectionPlaneDistanceStep_10c = initialStepZ.get();
      cam.projectionPlaneDistanceStepAcceleration_110 = (finalStepZ.get() - initialStepZ.get()) / projectionPlaneChangeFrames;
    }

    //LAB_800d8eec
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Resets battle camera movement")
  @Method(0x800dabccL)
  public static FlowControl scriptResetCameraMovement(final RunningScript<?> script) {
    camera_800c67f0.resetCameraMovement();
    return FlowControl.CONTINUE;
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

    camera_800c67f0.FUN_800dac70(script.params_20[0].get(), x, y, z, SCRIPTS.getObject(script.params_20[4].get(), BattleObject.class));
    return FlowControl.CONTINUE;
  }

  /** Note: sometimes nonsense values are passed for the script index (assassin cock passes -2 during yell attack) */
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

    camera_800c67f0.FUN_800db084(script.params_20[0].get(), x, y, z, SCRIPTS.getObject(script.params_20[4].get(), BattleObject.class));
    return FlowControl.CONTINUE;
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

    camera_800c67f0.FUN_800db4ec(script.params_20[0].get(), x, y, z, script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get(), SCRIPTS.getObject(script.params_20[7].get(), BattleObject.class));
    return FlowControl.CONTINUE;
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

    camera_800c67f0.FUN_800db600(script.params_20[0].get(), x, y, z, script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get(), SCRIPTS.getObject(script.params_20[7].get(), BattleObject.class));
    return FlowControl.CONTINUE;
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

    camera_800c67f0.FUN_800db714(script.params_20[0].get(), x, y, z, script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get(), SCRIPTS.getObject(script.params_20[7].get(), BattleObject.class));
    return FlowControl.CONTINUE;
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

    camera_800c67f0.FUN_800db828(script.params_20[0].get(), x, y, z, script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get(), SCRIPTS.getObject(script.params_20[7].get(), BattleObject.class));
    return FlowControl.CONTINUE;
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

    camera_800c67f0.FUN_800db950(script.params_20[0].get(), x, y, z, script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get() / (float)0x100, script.params_20[7].get(), SCRIPTS.getObject(script.params_20[8].get(), BattleObject.class));
    return FlowControl.CONTINUE;
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

    camera_800c67f0.FUN_800dba80(script.params_20[0].get(), x, y, z, script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get() / (float)0x100, script.params_20[7].get(), SCRIPTS.getObject(script.params_20[8].get(), BattleObject.class));
    return FlowControl.CONTINUE;
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
    script.params_20[0].set(Math.round(getProjectionPlaneDistance()));
    return FlowControl.CONTINUE;
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

    float value = camera_800c67f0.calculateCameraValue(script.params_20[0].get() != 0, script.params_20[1].get(), script.params_20[2].get(), SCRIPTS.getObject(script.params_20[3].get(), BattleObject.class));

    // Odd funcs operate on angles, but Z values in these methods are delta vector mag, not angles
    if((script.params_20[1].get() & 1) != 0 && script.params_20[2].get() != 2) {
      value = MathHelper.radToPsxDeg(value);
    }

    script.params_20[4].set((int)value);
    return FlowControl.CONTINUE;
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
    camera_800c67f0.setWobble(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    getScreenOffset(screenOffsetX_800c67bc, screenOffsetY_800c67c0);
    return FlowControl.CONTINUE;
  }

  @Method(0x800dd15cL)
  public static MV lerp(final MV a0, final MV a1, final float ratio) {
    if(ratio > 0) {
      final float v1 = 1.0f - ratio;
      a0.lerp(a1, ratio);
      a0.transfer.x = a0.transfer.x * v1 + a1.transfer.x * ratio;
      a0.transfer.y = a0.transfer.y * v1 + a1.transfer.y * ratio;
      a0.transfer.z = a0.transfer.z * v1 + a1.transfer.z * ratio;
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
      final LmbTransforms14 transforms = lmb.partAnimations_08[i].keyframes_08[a0_0];
      final MV matrix = model.modelParts_00[i].coord2_04.coord;

      final Vector3f trans = new Vector3f();
      trans.set(transforms.trans_06);

      if(isInterpolationFrame != 0) { // Interpolation frame
        final LmbTransforms14 nextFrame;
        if(animationTicks == model.totalFrames_9a - 1) {
          nextFrame = lmb.partAnimations_08[i].keyframes_08[0]; // Wrap around to frame 0
        } else {
          //LAB_800dd7cc
          nextFrame = lmb.partAnimations_08[i].keyframes_08[a0_0 + 1];
        }

        //LAB_800dd7d0
        trans.set(
          (trans.x + nextFrame.trans_06.x) / 2,
          (trans.y + nextFrame.trans_06.y) / 2,
          (trans.z + nextFrame.trans_06.z) / 2
        );
      }

      //LAB_800dd818
      matrix.rotationZYX(transforms.rot_0c);
      matrix.transfer.set(trans);
      matrix.scaleLocal(transforms.scale_00);
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
    zOffset_1f8003e8 = model.zOffset_a0;
    tmdGp0Tpage_1f8003ec = model.tpage_108;

    final MV lw = new MV();
    final MV ls = new MV();

    //LAB_800dd928
    for(int i = 0; i < model.modelParts_00.length; i++) {
      final ModelPart10 part = model.modelParts_00[i];

      //LAB_800dd940
      if((model.partInvisible_f4 & 1L << i) == 0) {
        GsGetLws(part.coord2_04, lw, ls);

        //LAB_800dd9bc
        if((newAttribute & 0x8) != 0) {
          //TODO pretty sure this is not equivalent to MATRIX#normalize
          lw.normal();
        }

        //LAB_800dd9d8
        GsSetLightMatrix(lw);
        GTE.setTransforms(ls);

        final int oldAttrib = part.attribute_00;
        part.attribute_00 = newAttribute;

        final int oldZShift = zShift_1f8003c4;
        final int oldZMax = zMax_1f8003cc;
        final int oldZMin = zMin;
        zShift_1f8003c4 = 2;
        zMax_1f8003cc = 0xffe;
        zMin = 0xb;
        Renderer.renderDobj2(part, false, 0x20);
        zShift_1f8003c4 = oldZShift;
        zMax_1f8003cc = oldZMax;
        zMin = oldZMin;

        RENDERER.queueModel(part.obj, lw)
          .lightDirection(lightDirectionMatrix_800c34e8)
          .lightColour(lightColourMatrix_800c3508)
          .backgroundColour(GTE.backgroundColour);

        part.attribute_00 = oldAttrib;
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
      renderBttlShadow(model);
    }

    //LAB_800dda98
  }

  @Method(0x800ddac8L)
  public static void loadModelTmd(final Model124 model, final CContainer extTmd) {
    final Vector3f sp0x18 = new Vector3f(model.coord2_14.coord.transfer);

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

    if((tmd.header.flags & 0x2) == 0 && model.vramSlot_9d != 0) {
      adjustModelUvs(model);
    }

    //LAB_800ddce8
    model.coord2_14.transforms.scale.set(1.0f, 1.0f, 1.0f);
    model.shadowType_cc = 0;
    model.shadowSize_10c.set(1.0f, 1.0f, 1.0f);
    model.shadowOffset_118.zero();

    for(int i = 0; i < model.modelParts_00.length; i++) {
      model.modelParts_00[i].obj = TmdObjLoader.fromObjTable("BattleModel (part" + i + ')', tmd.objTable[i]);
    }
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

        final MV modelPartMatrix = model.modelParts_00[i].coord2_04.coord;
        modelPartMatrix.rotationZYX(modelTransforms.rotate_00);
        modelPartMatrix.transfer.set(modelTransforms.translate_06);

        final Vector3f rotation = new Vector3f();
        rotation.set(modelTransforms.rotate_00).add(subTransforms.rot_01);

        final MV translation = new MV();
        translation.rotationZYX(rotation);
        translation.transfer.set(modelTransforms.translate_06).add(subTransforms.trans_05);

        lerp(modelPartMatrix, translation, 0.5f);
      }
    } else {
      //LAB_800de164
      for(int i = 0; i < count; i++) {
        final ModelPartTransforms0c modelTransforms = cmbAnim.transforms_08[i];
        final MV modelPartMatrix = model.modelParts_00[i].coord2_04.coord;
        modelPartMatrix.rotationZYX(modelTransforms.rotate_00);
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
      model.totalFrames_9a = lmb.partAnimations_08[0].count_04 * 2;
      model.animationState_9c = 1;
      model.remainingFrames_9e = lmb.partAnimations_08[0].count_04 * 2;
      model.interpolationFrameIndex = 0;
    } else {
      //LAB_800de3dc
      loadModelStandardAnimation(model, (TmdAnimationFile)anim);
    }

    //LAB_800de3e4
  }

  /** used renderCtmd */
  @Method(0x800de3f4L)
  public static void renderTmdSpriteEffect(final TmdObjTable1c objTable, final Obj obj, final EffectManagerParams<?> effectParams, final MV transforms) {
    final MV sp0x10 = new MV();
    if((effectParams.flags_00 & 0x8) != 0) {
      //TODO pretty sure this isn't equivalent to MATRIX#normalize
      transforms.normal(sp0x10);
      GsSetLightMatrix(sp0x10);
    } else {
      //LAB_800de458
      GsSetLightMatrix(transforms);
    }

    //LAB_800de45c
    if(RenderEngine.legacyMode != 0) {
      transforms.compose(worldToScreenMatrix_800c3548, sp0x10);
    } else {
      sp0x10.set(transforms);
    }

    if((effectParams.flags_00 & 0x400_0000) == 0) {
      sp0x10.rotationXYZ(effectParams.rot_10);
      sp0x10.scaleLocal(effectParams.scale_16);
    }

    //LAB_800de4a8
    //LAB_800de50c
    GTE.setTransforms(sp0x10);

    final ModelPart10 dobj2 = new ModelPart10();
    dobj2.attribute_00 = effectParams.flags_00;
    dobj2.tmd_08 = objTable;

    final int oldZShift = zShift_1f8003c4;
    final int oldZMax = zMax_1f8003cc;
    final int oldZMin = zMin;
    zShift_1f8003c4 = 2;
    zMax_1f8003cc = 0xffe;
    zMin = 0xb;
    Renderer.renderDobj2(dobj2, false, 0x20);
    zShift_1f8003c4 = oldZShift;
    zMax_1f8003cc = oldZMax;
    zMin = oldZMin;

    RENDERER.queueModel(obj, sp0x10)
      .lightDirection(lightDirectionMatrix_800c34e8)
      .lightColour(lightColourMatrix_800c3508)
      .backgroundColour(GTE.backgroundColour);

    //LAB_800de528
  }

  @Method(0x800de544L)
  public static Vector3f getRotationFromTransforms(final Vector3f rotOut, final MV transforms) {
    final MV mat = new MV(transforms);
    rotOut.x = MathHelper.atan2(-mat.m21, mat.m22);
    mat.rotateLocalX(-rotOut.x);
    rotOut.y = MathHelper.atan2(mat.m20, mat.m22);
    mat.rotateLocalY(-rotOut.y);
    rotOut.z = MathHelper.atan2(mat.m01, mat.m00);
    return rotOut;
  }

  @Method(0x800de618L)
  public static void getRotationAndScaleFromTransforms(final Vector3f rotOut, final Vector3f scaleOut, final MV transforms) {
    final MV mat = new MV().set(transforms);
    rotOut.x = MathHelper.atan2(-mat.m21, mat.m22);
    mat.rotateLocalX(-rotOut.x);
    rotOut.y = MathHelper.atan2(mat.m20, mat.m22);
    mat.rotateLocalY(-rotOut.y);
    rotOut.z = MathHelper.atan2(mat.m01, mat.m00);
    mat.rotateLocalZ(-rotOut.z);
    scaleOut.set(mat.m00, mat.m11, mat.m22);
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
