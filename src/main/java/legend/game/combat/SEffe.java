package legend.game.combat;

import legend.core.Config;
import legend.core.DebugHelper;
import legend.core.MathHelper;
import legend.core.RenderEngine;
import legend.core.gpu.Bpp;
import legend.core.gpu.Gpu;
import legend.core.gpu.GpuCommand;
import legend.core.gpu.GpuCommandCopyDisplayBufferToVram;
import legend.core.gpu.GpuCommandLine;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gpu.GpuCommandQuad;
import legend.core.gpu.GpuCommandSetMaskBit;
import legend.core.gpu.Rect4i;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MV;
import legend.core.gte.ModelPart10;
import legend.core.gte.Tmd;
import legend.core.gte.TmdObjTable1c;
import legend.core.gte.TmdWithId;
import legend.core.gte.Transforms;
import legend.core.memory.Method;
import legend.core.memory.types.QuadConsumer;
import legend.core.memory.types.TriConsumer;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.core.opengl.TmdObjLoader;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.deff.Anim;
import legend.game.combat.deff.DeffManager7cc;
import legend.game.combat.deff.DeffPart;
import legend.game.combat.deff.LmbTransforms14;
import legend.game.combat.deff.LmbType0;
import legend.game.combat.deff.LmbType1;
import legend.game.combat.deff.LmbType2;
import legend.game.combat.effects.AdditionOverlaysBorder0e;
import legend.game.combat.effects.AdditionOverlaysEffect44;
import legend.game.combat.effects.AdditionOverlaysHit20;
import legend.game.combat.effects.AttachmentHost;
import legend.game.combat.effects.BillboardSpriteEffect0c;
import legend.game.combat.effects.DeffTmdRenderer14;
import legend.game.combat.effects.Effect;
import legend.game.combat.effects.EffectAttachment;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerParams;
import legend.game.combat.effects.ElectricityEffect38;
import legend.game.combat.effects.FrozenJetEffect28;
import legend.game.combat.effects.GenericAttachment1c;
import legend.game.combat.effects.GenericSpriteEffect24;
import legend.game.combat.effects.GoldDragoonTransformEffect20;
import legend.game.combat.effects.GoldDragoonTransformEffectInstance84;
import legend.game.combat.effects.GradientRaysEffect24;
import legend.game.combat.effects.GradientRaysEffectInstance04;
import legend.game.combat.effects.LensFlareEffect50;
import legend.game.combat.effects.LensFlareEffectInstance3c;
import legend.game.combat.effects.LightningBoltEffect14;
import legend.game.combat.effects.LightningBoltEffectSegment30;
import legend.game.combat.effects.LightningBoltEffectSegmentOrigin08;
import legend.game.combat.effects.LmbAnimationEffect5c;
import legend.game.combat.effects.ModelEffect13c;
import legend.game.combat.effects.MoonlightStarsEffect18;
import legend.game.combat.effects.MoonlightStarsEffectInstance3c;
import legend.game.combat.effects.RainEffect08;
import legend.game.combat.effects.RaindropEffect0c;
import legend.game.combat.effects.ScreenCaptureEffect1c;
import legend.game.combat.effects.ScreenCaptureEffectMetrics8;
import legend.game.combat.effects.ScreenDistortionEffectData08;
import legend.game.combat.effects.SpriteMetrics08;
import legend.game.combat.effects.SpriteWithTrailEffect30;
import legend.game.combat.effects.StarChildrenImpactEffect20;
import legend.game.combat.effects.StarChildrenImpactEffectInstancea8;
import legend.game.combat.effects.StarChildrenMeteorEffect10;
import legend.game.combat.effects.StarChildrenMeteorEffectInstance10;
import legend.game.combat.effects.ThunderArrowEffect1c;
import legend.game.combat.effects.ThunderArrowEffectBolt1e;
import legend.game.combat.effects.TmdSpriteEffect10;
import legend.game.combat.effects.TransformScalerAttachment34;
import legend.game.combat.effects.WsDragoonTransformationFeatherInstance70;
import legend.game.combat.effects.WsDragoonTransformationFeathersEffect14;
import legend.game.combat.environment.BattleLightStruct64;
import legend.game.combat.environment.BattleStageDarkening1800;
import legend.game.combat.particles.ParticleEffectData98;
import legend.game.combat.particles.ParticleEffectInstance94;
import legend.game.combat.types.AdditionHitProperties10;
import legend.game.combat.types.BattleObject;
import legend.game.combat.types.DragoonAdditionScriptData1c;
import legend.game.combat.types.PerfectDragoonAdditionEffect30;
import legend.game.combat.types.PerfectDragoonAdditionEffectGlyph06;
import legend.game.combat.types.VertexDifferenceAnimation18;
import legend.game.combat.ui.AdditionOverlayMode;
import legend.game.modding.coremod.CoreMod;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptDescription;
import legend.game.scripting.ScriptFile;
import legend.game.scripting.ScriptParam;
import legend.game.scripting.ScriptState;
import legend.game.tmd.Renderer;
import legend.game.types.GsF_LIGHT;
import legend.game.types.Model124;
import legend.game.types.Translucency;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.RENDERER;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.Scus94491BpeSegment.battlePreloadedEntities_1f8003f4;
import static legend.game.Scus94491BpeSegment.battleUiParts;
import static legend.game.Scus94491BpeSegment.displayHeight_1f8003e4;
import static legend.game.Scus94491BpeSegment.displayWidth_1f8003e0;
import static legend.game.Scus94491BpeSegment.playSound;
import static legend.game.Scus94491BpeSegment.projectionPlaneDistance_1f8003f8;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.zMax_1f8003cc;
import static legend.game.Scus94491BpeSegment.zMin;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment.zShift_1f8003c4;
import static legend.game.Scus94491BpeSegment_8002.applyModelRotationAndScale;
import static legend.game.Scus94491BpeSegment_8002.playXaAudio;
import static legend.game.Scus94491BpeSegment_8002.rand;
import static legend.game.Scus94491BpeSegment_8003.GetClut;
import static legend.game.Scus94491BpeSegment_8003.GsGetLw;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.GsSetFlatLight;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_8003.RotTransPers4;
import static legend.game.Scus94491BpeSegment_8003.getProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.perspectiveTransform;
import static legend.game.Scus94491BpeSegment_8003.perspectiveTransformTriple;
import static legend.game.Scus94491BpeSegment_8004.currentEngineState_8004dd04;
import static legend.game.Scus94491BpeSegment_8004.doNothingScript_8004f650;
import static legend.game.Scus94491BpeSegment_800b._800bf0cf;
import static legend.game.Scus94491BpeSegment_800b.press_800bee94;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.shadowModel_800bda10;
import static legend.game.Scus94491BpeSegment_800b.stage_800bda0c;
import static legend.game.Scus94491BpeSegment_800c.lightColourMatrix_800c3508;
import static legend.game.Scus94491BpeSegment_800c.lightDirectionMatrix_800c34e8;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;
import static legend.game.combat.Battle.deffManager_800c693c;
import static legend.game.combat.Battle.melbuStageIndices_800fb064;
import static legend.game.combat.Battle.seed_800fa754;
import static legend.game.combat.Battle.stageDarkeningClutWidth_800c695c;
import static legend.game.combat.Battle.stageDarkening_800c6958;

public final class SEffe {
  private SEffe() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(SEffe.class);
  private static final Marker EFFECTS = MarkerManager.getMarker("EFFECTS");

  private static final GsF_LIGHT defaultLight_800c6ddc = new GsF_LIGHT(1.0f, 1.0f, 1.0f);

  private static final int[] vramSlotUvs_800fb0ec = {0, 21, 22, 23, 24, 25, 26, 25, 26, 27, 12, 13, 14, 15, 8, 9, 10, 11};

  private static final byte[] additionButtonRenderCallbackIndices_800fb7bc = {35, 40, 33, 38};

  /** Some kind of mysterious global 2-hit addition array. Should probably be yeeted, but need to be sure. */
  private static final AdditionHitProperties10[] staticTestAdditionHitProperties_800fb7c0 = {
    new AdditionHitProperties10(0xc0, 13, 9, 2, 50, 20, 2, 0, 0, 0, 8, 5, 8, 32, 0, 11),
    new AdditionHitProperties10(0xc0, 33, 27, 1, 30, 10, 0, 0, 0, 25, 2, 1, 8, 32, 0, 0),
    new AdditionHitProperties10(0x0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
  };

  /** Four sets of color values used for addition overlay borders; only last actually used */
  public static final int[] additionBorderColours_800fb7f0 = {0x70, 0xc0, 0x75, 0xc0, 0xa8, 0x70, 0x70, 0xb5, 0xc0, 0x48, 0x60, 0xff};
  public static final int[][] daddyHudEyeTranslucencyModes_800fb7fc = {{3, 0}, {1, 1}, {1, 1}, {0, 0}};
  public static final int[][] daddyHudMeterOffsets_800fb804 = {{49, 32}, {46, 49}, {38, 56}, {35, 60}, {31, 60}};
  public static final int[][] daddyHudMeterUvs_800fb818 = {{128, 112}, {128, 80}, {128, 96}, {216, 8}, {216, 16}};
  public static final int[][] daddyHudMeterDimensions_800fb82c = {{24, 32}, {16, 16}, {16, 16}, {8, 8}, {8, 8}};
  public static final int[] daddyHudFrameClutOffsets_800fb840 = {0x50, 0x51, 0x52, 0x53, 0x54, 0x51, 0x55, 0x56, 0x52, 0x57, 0, 0};
  public static final int[] daddyHudEyeClutOffsets_800fb84c = {0x58, 0x59, 0x5a, 0x5b, 0x5c, 0x59, 0x5d, 0x5e, 0x5a, 0x5f, 0, 0, 0, 1, 0, 0};

  private static final Vector3f _800fb8d0 = new Vector3f(1.0f, 0.0f, 0.0f);

  private static final int[] lensFlareGlowScales_800fb8fc = {0xf0, 0xa0, 0x60, 0x30, 0x10};
  private static final int[][] lensFlareTranslationMagnitudeFactors_800fb910 = {{-1, -1}, {0, -1}, {-1, 0}, {0, 0}};
  private static final int[][] lensFlareVertexIndices_800fb930 = {{3, 2, 1, 0}, {2, 3, 0, 1}, {1, 0, 3, 2}, {0, 1, 2, 3}};

  /**
   * <ol start="0">
   *   <li>{@link SEffe#FUN_801049d4}</li>
   *   <li>{@link SEffe#FUN_801049dc}</li>
   *   <li>{@link SEffe#FUN_80104a14}</li>
   *   <li>{@link SEffe#FUN_80104b10}</li>
   *   <li>{@link SEffe#FUN_80104bec}</li>
   *   <li>{@link SEffe#FUN_80104c9c}</li>
   *   <li>{@link SEffe#FUN_80104e40}</li>
   *   <li>{@link SEffe#FUN_80104f70}</li>
   *   <li>{@link SEffe#FUN_80105050}</li>
   *   <li>{@link SEffe#FUN_801051ac}</li>
   *   <li>{@link SEffe#FUN_801049d4}</li>
   * </ol>
   */
  private static final QuadConsumer<EffectManagerData6c<EffectManagerParams.ElectricityType>, ElectricityEffect38, LightningBoltEffect14, Integer>[] electricityEffectCallbacks_80119ee8 = new QuadConsumer[11];
  static {
    electricityEffectCallbacks_80119ee8[0] = SEffe::FUN_801049d4;
    electricityEffectCallbacks_80119ee8[1] = SEffe::FUN_801049dc;
    electricityEffectCallbacks_80119ee8[2] = SEffe::FUN_80104a14;
    electricityEffectCallbacks_80119ee8[3] = SEffe::FUN_80104b10;
    electricityEffectCallbacks_80119ee8[4] = SEffe::FUN_80104bec;
    electricityEffectCallbacks_80119ee8[5] = SEffe::FUN_80104c9c;
    electricityEffectCallbacks_80119ee8[6] = SEffe::FUN_80104e40;
    electricityEffectCallbacks_80119ee8[7] = SEffe::FUN_80104f70;
    electricityEffectCallbacks_80119ee8[8] = SEffe::FUN_80105050;
    electricityEffectCallbacks_80119ee8[9] = SEffe::FUN_801051ac;
    electricityEffectCallbacks_80119ee8[10] = SEffe::FUN_801049d4;
  }
  /**
   * <ol start="0">
   *   <li>{@link SEffe#renderElectricEffectType0}</li>
   *   <li>{@link SEffe#renderElectricEffectType0}</li>
   *   <li>{@link SEffe#renderElectricEffectType0}</li>
   *   <li>{@link SEffe#renderElectricEffectType1}</li>
   *   <li>{@link SEffe#renderElectricEffectType1}</li>
   *   <li>{@link SEffe#renderElectricEffectType1}</li>
   *   <li>{@link SEffe#renderElectricEffectType1}</li>
   *   <li>{@link SEffe#renderElectricEffectType1}</li>
   *   <li>{@link SEffe#renderElectricEffectType1}</li>
   *   <li>{@link SEffe#renderElectricEffectType1}</li>
   *   <li>{@link SEffe#renderElectricEffectType1}</li>
   * </ol>
   */
  private static final BiConsumer<ScriptState<EffectManagerData6c<EffectManagerParams.ElectricityType>>, EffectManagerData6c<EffectManagerParams.ElectricityType>>[] electricityEffectRenderers_80119f14 = new BiConsumer[11];
  static {
    electricityEffectRenderers_80119f14[0] = SEffe::renderElectricEffectType0;
    electricityEffectRenderers_80119f14[1] = SEffe::renderElectricEffectType0;
    electricityEffectRenderers_80119f14[2] = SEffe::renderElectricEffectType0;
    electricityEffectRenderers_80119f14[3] = SEffe::renderElectricEffectType1;
    electricityEffectRenderers_80119f14[4] = SEffe::renderElectricEffectType1;
    electricityEffectRenderers_80119f14[5] = SEffe::renderElectricEffectType1;
    electricityEffectRenderers_80119f14[6] = SEffe::renderElectricEffectType1;
    electricityEffectRenderers_80119f14[7] = SEffe::renderElectricEffectType1;
    electricityEffectRenderers_80119f14[8] = SEffe::renderElectricEffectType1;
    electricityEffectRenderers_80119f14[9] = SEffe::renderElectricEffectType1;
    electricityEffectRenderers_80119f14[10] = SEffe::renderElectricEffectType1;
  }

  /**
   * <ol start="0">
   *   <li>{@link SEffe#FUN_80109358}</li>
   *   <li>{@link SEffe#FUN_80109358}</li>
   *   <li>{@link SEffe#renderScreenDistortionBlurEffect}</li>
   * </ol>
   */
  private static final BiConsumer<ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>>, EffectManagerData6c<EffectManagerParams.VoidType>>[] screenDistortionEffectRenderers_80119fd4 = new BiConsumer[3];
  static {
    screenDistortionEffectRenderers_80119fd4[0] = SEffe::FUN_80109358;
    screenDistortionEffectRenderers_80119fd4[1] = SEffe::FUN_80109358;
    screenDistortionEffectRenderers_80119fd4[2] = SEffe::renderScreenDistortionBlurEffect;
  }
  /**
   * <ol start="0">
   *   <li>{@link SEffe#FUN_80109a4c}</li>
   *   <li>{@link SEffe#FUN_80109a4c}</li>
   *   <li>{@link SEffe#tickScreenDistortionBlurEffect}</li>
   * </ol>
   */
  private static final BiConsumer<ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>>, EffectManagerData6c<EffectManagerParams.VoidType>>[] screenDistortionEffectTickers_80119fe0 = new BiConsumer[3];
  static {
    screenDistortionEffectTickers_80119fe0[0] = SEffe::FUN_80109a4c;
    screenDistortionEffectTickers_80119fe0[1] = SEffe::FUN_80109a4c;
    screenDistortionEffectTickers_80119fe0[2] = SEffe::tickScreenDistortionBlurEffect;
  }

  /**
   * <ol start="0">
   *   <li>{@link SEffe#FUN_8010b594}</li>
   *   <li>{@link SEffe#renderScreenCapture}</li>
   * </ol>
   */
  private static final TriConsumer<EffectManagerData6c<EffectManagerParams.VoidType>, ScreenCaptureEffect1c, MV>[] screenCaptureRenderers_80119fec = new TriConsumer[2];
  static {
    screenCaptureRenderers_80119fec[0] = SEffe::FUN_8010b594;
    screenCaptureRenderers_80119fec[1] = SEffe::renderScreenCapture;
  }

  /** -1 = failed with no successful presses, 0 = continue, 1-4 = number successful presses */
  private static int daddyHitsCompleted_80119f40;
  private static byte additionOverlayActive_80119f41;
  /** Active when spinning */
  private static byte daddyMeterSpinning_80119f42;

  /** Array of daddy spinner step counts */
  private static final int[] daddyHudSpinnerStepCounts_80119f44 = {14, 12, 10, 8, 1, 1, 0};
  /** Array of daddy spinner successful press frame windows */
  private static final int[] daddyHitSuccessWindows_80119f60 = {4, 3, 2, 1, 0, 0, 0};
  /** Array of daddy spinner step counts for special boi Kongol */
  private static final int[] kongolDaddyHudSpinnerStepCounts_80119f7c = {14, 12, 10, 1, 1, 1, 0};
  /** Array of daddy spinner successful press frame windows for special boi Kongol */
  private static final int[] kongolDaddyHitSuccessWindows_80119f98 = {4, 3, 2, 0, 0, 0, 0};
  private static int daddySpinnerBrightnessFactor_80119fb4;

  public static final int[] perfectDaddyGlyphUs_80119fbc = {232, 208, 40, 48, 208, 56, 64, 240};
  public static final int[] perfectDaddyGlyphVs_80119fc4 = {64, 64, 128, 128, 64, 128, 128, 64};

  /**
   * <ol start="0">
   *   <li>{@link SEffe#initializeWsDragoonTransformationEffect}</li>
   *   <li>{@link SEffe#expandWsDragoonTransformationEffect}</li>
   *   <li>{@link SEffe#spinWsDragoonTransformationEffect}</li>
   *   <li>{@link SEffe#contractWsDragoonTransformationEffect}</li>
   *   <li>{@link SEffe#WsDragoonTransformationCallback4}</li>
   * </ol>
   */
  private static final BiConsumer<EffectManagerData6c<EffectManagerParams.VoidType>, WsDragoonTransformationFeatherInstance70>[] WsDragoonTransformationFeatherCallbacks_80119ff4 = new BiConsumer[5];
  static {
    WsDragoonTransformationFeatherCallbacks_80119ff4[0] = SEffe::initializeWsDragoonTransformationEffect;
    WsDragoonTransformationFeatherCallbacks_80119ff4[1] = SEffe::expandWsDragoonTransformationEffect;
    WsDragoonTransformationFeatherCallbacks_80119ff4[2] = SEffe::spinWsDragoonTransformationEffect;
    WsDragoonTransformationFeatherCallbacks_80119ff4[3] = SEffe::contractWsDragoonTransformationEffect;
    WsDragoonTransformationFeatherCallbacks_80119ff4[4] = SEffe::WsDragoonTransformationCallback4; // no-op
  }

  /** Success values for each addition hit: 0 = not attempted, 1 = success, -1 = too early, -2 = too late, -3 = wrong button */
  private static final byte[] additionHitCompletionState_8011a014 = new byte[8];

  private static int daddyHudOffsetX_8011a01c;
  private static int daddyHudOffsetY_8011a020;

  private static int[] daddyHudSpinnerStepCountsPointer_8011a028;
  private static int[] daddyHitSuccessWindowsPointer_8011a02c;

  /** Related to processing type 2 LMBs */
  private static final byte[] lmbType2TransformationData_8011a048 = new byte[0x300];

  @Method(0x800cea1cL)
  public static void scriptGetScriptedObjectPos(final int scriptIndex, final Vector3f posOut) {
    final BattleObject bobj = (BattleObject)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00;
    posOut.set(bobj.getPosition());
  }

  /** @return Z */
  @Method(0x800cf244L)
  public static float transformWorldspaceToScreenspace(final Vector3f pos, final Vector2f out) {
    final Vector3f sp0x10 = new Vector3f();
    pos.mul(worldToScreenMatrix_800c3548, sp0x10);
    sp0x10.add(worldToScreenMatrix_800c3548.transfer);
    out.x = MathHelper.safeDiv(getProjectionPlaneDistance() * sp0x10.x, sp0x10.z);
    out.y = MathHelper.safeDiv(getProjectionPlaneDistance() * sp0x10.y, sp0x10.z);
    return sp0x10.z;
  }

  @Method(0x800cf37cL)
  public static void rotateAndTranslateEffect(final EffectManagerData6c<?> manager, @Nullable final Vector3f extraRotation, final Vector3f vertex, final Vector3f out) {
    final Vector3f rotations = new Vector3f(manager.params_10.rot_10);

    if(extraRotation != null) {
      //LAB_800cf3c4
      rotations.add(extraRotation);
    }

    //LAB_800cf400
    final MV transforms = new MV();
    transforms.rotationXYZ(rotations);

    vertex.mul(transforms, out);
    out.add(transforms.transfer);
  }

  @Method(0x800cf4f4L)
  public static void getRelativeOffset(final EffectManagerData6c<?> manager, @Nullable final Vector3f extraRotation, final Vector3f in, final Vector3f out) {
    final MV sp0x28 = new MV();

    final Vector3f sp0x20 = new Vector3f(manager.getRotation());

    if(extraRotation != null) {
      //LAB_800cf53c
      sp0x20.add(extraRotation);
    }

    //LAB_800cf578
    sp0x28.rotationXYZ(sp0x20);
    sp0x28.transfer.set(manager.getPosition());

    in.mul(sp0x28, out);
    out.add(sp0x28.transfer);
  }

  @Method(0x800cf684L)
  public static void FUN_800cf684(final Vector3f rotation, final Vector3f translation, final Vector3f vector, final Vector3f out) {
    final MV transforms = new MV();
    transforms.rotationXYZ(rotation);
    transforms.transfer.set(translation);
    vector.mul(transforms, out);
    out.add(transforms.transfer);
  }

  /** @return Z */
  @Method(0x800cf7d4L)
  public static float FUN_800cf7d4(final Vector3f rotation, final Vector3f translation1, final Vector3f translation2, final Vector2f out) {
    final Vector3f sp0x10 = new Vector3f(translation1);
    sp0x10.mul(worldToScreenMatrix_800c3548);

    GTE.setTransforms(worldToScreenMatrix_800c3548);

    final MV oldTransforms = new MV();
    GTE.getTransforms(oldTransforms);

    final MV sp0x58 = new MV();
    sp0x58.rotationXYZ(rotation);
    oldTransforms.mul(sp0x58);
    oldTransforms.transfer.add(sp0x10);

    GTE.setTransforms(oldTransforms);
    GTE.perspectiveTransform(translation2);

    out.set(GTE.getScreenX(2), GTE.getScreenY(2));
    return GTE.getScreenZ(3);
  }

  /** @return Z */
  @Method(0x800cfb14L)
  public static float FUN_800cfb14(final EffectManagerData6c<?> manager, final Vector3f translation, final Vector2f out) {
    return FUN_800cf7d4(manager.params_10.rot_10, manager.params_10.trans_04, translation, out);
  }

  /** @return Z */
  @Method(0x800cfb94L)
  public static float FUN_800cfb94(final EffectManagerData6c<?> manager, final Vector3f rotation, final Vector3f translation, final Vector2f out) {
    final Vector3f tempRotation = new Vector3f(manager.params_10.rot_10).add(rotation);
    return FUN_800cf7d4(tempRotation, manager.params_10.trans_04, translation, out);
  }

  /** @return Z */
  @Method(0x800cfc20L)
  public static float FUN_800cfc20(final Vector3f managerRotation, final Vector3f managerTranslation, final Vector3f translation, final Vector2f out) {
    return FUN_800cf7d4(managerRotation, managerTranslation, translation, out);
  }

  /** Sets translation vector to position of individual part of model associated with scriptIndex */
  @Method(0x800cffd8L)
  public static void getModelObjectTranslation(final BattleEntity27c bent, final Vector3f translation, final int objIndex) {
    final MV transformMatrix = new MV();
    GsGetLw(bent.model_148.modelParts_00[objIndex].coord2_04, transformMatrix);
    translation.set(transformMatrix.transfer);
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
      .backgroundColour(GTE.backgroundColour)
      .ctmdFlags(0x20 | ((dobj2.attribute_00 & 0x4000_0000) != 0 ? 0x12 : 0x0))
      .tmdTranslucency(tmdGp0Tpage_1f8003ec >>> 5 & 0b11)
      .battleColour(((Battle)currentEngineState_8004dd04)._800c6930.colour_00);

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

  @Method(0x800e4d74L)
  public static void getBattleBackgroundLightColour(final Vector3f colour) {
    final BattleLightStruct64 light = ((Battle)currentEngineState_8004dd04)._800c6930;
    colour.set(light.colour_00);
  }

  @Method(0x800e60e0L)
  public static void FUN_800e60e0(final float r, final float g, final float b) {
    if(r < 0.0f) {
      LOGGER.warn("Negative R! %f", r);
    }

    if(g < 0.0f) {
      LOGGER.warn("Negative G! %f", g);
    }

    if(b < 0.0f) {
      LOGGER.warn("Negative B! %f", b);
    }

    final BattleLightStruct64 light = ((Battle)currentEngineState_8004dd04)._800c6930;
    final Vector3f colour = light.colours_30[light.colourIndex_60];
    getBattleBackgroundLightColour(colour);

    light.colour_00.set(r, g, b);
    light.colourIndex_60 = light.colourIndex_60 + 1 & 3;
  }

  @Method(0x800e6170L)
  public static void FUN_800e6170() {
    final BattleLightStruct64 light = ((Battle)currentEngineState_8004dd04)._800c6930;
    light.colourIndex_60 = light.colourIndex_60 - 1 & 3;
    light.colour_00.set(light.colours_30[light.colourIndex_60]);
  }

  @Method(0x800e61e4L)
  public static void FUN_800e61e4(final float r, final float g, final float b) {
    if(r < 0.0f) {
      LOGGER.warn("Negative R! %f", r);
    }

    if(g < 0.0f) {
      LOGGER.warn("Negative G! %f", g);
    }

    if(b < 0.0f) {
      LOGGER.warn("Negative B! %f", b);
    }

    GsSetFlatLight(0, defaultLight_800c6ddc);
    GsSetFlatLight(1, defaultLight_800c6ddc);
    GsSetFlatLight(2, defaultLight_800c6ddc);
    FUN_800e60e0(r, g, b);

    final BattleLightStruct64 light = ((Battle)currentEngineState_8004dd04)._800c6930;
    GTE.setBackgroundColour(light.colour_00.x, light.colour_00.y, light.colour_00.z);
  }

  @Method(0x800e62a8L)
  public static void FUN_800e62a8() {
    FUN_800e6170();

    final BattleLightStruct64 light = ((Battle)currentEngineState_8004dd04)._800c6930;
    GTE.setBackgroundColour(light.colour_00.x, light.colour_00.y, light.colour_00.z);

    for(int i = 0; i < 3; i++) {
      GsSetFlatLight(i, ((Battle)currentEngineState_8004dd04).lights_800c692c[i].light_00);
    }
  }

  /** Used in Astral Drain (ground glow) */
  @Method(0x800e75acL)
  public static void FUN_800e75ac(final GenericSpriteEffect24 spriteEffect, final MV transformMatrix) {
    final MV finalTransform = new MV();
    transformMatrix.compose(worldToScreenMatrix_800c3548, finalTransform);
    final float z = java.lang.Math.min(0x3ff8, zOffset_1f8003e8 + finalTransform.transfer.z / 4.0f);

    if(z >= 40) {
      //LAB_800e7610
      GTE.setTransforms(finalTransform);

      GTE.setVertex(0, spriteEffect.x_04 * 64, spriteEffect.y_06 * 64, 0);
      GTE.setVertex(1, (spriteEffect.x_04 + spriteEffect.w_08) * 64, spriteEffect.y_06 * 64, 0);
      GTE.setVertex(2, spriteEffect.x_04 * 64, (spriteEffect.y_06 + spriteEffect.h_0a) * 64, 0);
      GTE.perspectiveTransformTriangle();
      final float sx0 = GTE.getScreenX(0);
      final float sy0 = GTE.getScreenY(0);
      final float sx1 = GTE.getScreenX(1);
      final float sy1 = GTE.getScreenY(1);
      final float sx2 = GTE.getScreenX(2);
      final float sy2 = GTE.getScreenY(2);

      GTE.perspectiveTransform((spriteEffect.x_04 + spriteEffect.w_08) * 64, (spriteEffect.y_06 + spriteEffect.h_0a) * 64, 0);
      final float sx3 = GTE.getScreenX(2);
      final float sy3 = GTE.getScreenY(2);

      final GpuCommandPoly cmd = new GpuCommandPoly(4)
        .clut(spriteEffect.clutX_10, spriteEffect.clutY_12)
        .vramPos((spriteEffect.tpage_0c & 0b1111) * 64, (spriteEffect.tpage_0c & 0b10000) != 0 ? 256 : 0)
        .rgb(spriteEffect.r_14, spriteEffect.g_15, spriteEffect.b_16)
        .pos(0, sx0, sy0)
        .pos(1, sx1, sy1)
        .pos(2, sx2, sy2)
        .pos(3, sx3, sy3)
        .uv(0, spriteEffect.u_0e, spriteEffect.v_0f)
        .uv(1, spriteEffect.u_0e + spriteEffect.w_08, spriteEffect.v_0f)
        .uv(2, spriteEffect.u_0e, spriteEffect.v_0f + spriteEffect.h_0a)
        .uv(3, spriteEffect.u_0e + spriteEffect.w_08, spriteEffect.v_0f + spriteEffect.h_0a);

      if((spriteEffect.flags_00 >>> 30 & 1) != 0) {
        cmd.translucent(Translucency.of(spriteEffect.flags_00 >>> 28 & 0b11));
      }

      GPU.queueCommand(z / 4.0f, cmd);
    }
    //LAB_800e7930
  }

  /**
   * Renderer for some kind of effect sprites like those in HUD DEFF.
   * Used for example for sprite effect overlays on red glow in Death Dimension.
   */
  @Method(0x800e7944L)
  public static void FUN_800e7944(final GenericSpriteEffect24 spriteEffect, final Vector3f translation, final int zMod) {
    if(spriteEffect.flags_00 >= 0) { // No errors
      final Vector3f finalTranslation = new Vector3f();
      translation.mul(worldToScreenMatrix_800c3548, finalTranslation);
      finalTranslation.add(worldToScreenMatrix_800c3548.transfer);

      final float x0 = MathHelper.safeDiv(finalTranslation.x * projectionPlaneDistance_1f8003f8, finalTranslation.z);
      final float y0 = MathHelper.safeDiv(finalTranslation.y * projectionPlaneDistance_1f8003f8, finalTranslation.z);

      // zMod needs to be ignored in z check or poly positions will overflow at low z values
      float z = zMod + finalTranslation.z / 4.0f;
      if(finalTranslation.z / 4.0f >= 40 && z >= 40) {
        if(z > 0x3ff8) {
          z = 0x3ff8;
        }

        //LAB_800e7a38
        final float zDepth = MathHelper.safeDiv(projectionPlaneDistance_1f8003f8 * 0x1000 / 4.0f, finalTranslation.z / 4.0f);
        final float x1 = spriteEffect.x_04 * spriteEffect.scaleX_1c / 8 * zDepth / 8;
        final float x2 = x1 + spriteEffect.w_08 * spriteEffect.scaleX_1c / 8 * zDepth / 8;
        final float y1 = spriteEffect.y_06 * spriteEffect.scaleY_1e / 8 * zDepth / 8;
        final float y2 = y1 + spriteEffect.h_0a * spriteEffect.scaleY_1e / 8 * zDepth / 8;
        final float sin = MathHelper.sin(spriteEffect.angle_20);
        final float cos = MathHelper.cos(spriteEffect.angle_20);

        final GpuCommandPoly cmd = new GpuCommandPoly(4)
          .clut(spriteEffect.clutX_10, spriteEffect.clutY_12)
          .vramPos((spriteEffect.tpage_0c & 0b1111) * 64, (spriteEffect.tpage_0c & 0b10000) != 0 ? 256 : 0)
          .rgb(spriteEffect.r_14, spriteEffect.g_15, spriteEffect.b_16)
          .pos(0, x0 + x1 * cos - y1 * sin, y0 + x1 * sin + y1 * cos)
          .pos(1, x0 + x2 * cos - y1 * sin, y0 + x2 * sin + y1 * cos)
          .pos(2, x0 + x1 * cos - y2 * sin, y0 + x1 * sin + y2 * cos)
          .pos(3, x0 + x2 * cos - y2 * sin, y0 + x2 * sin + y2 * cos)
          .uv(0, spriteEffect.u_0e, spriteEffect.v_0f)
          .uv(1, spriteEffect.w_08 + spriteEffect.u_0e - 1, spriteEffect.v_0f)
          .uv(2, spriteEffect.u_0e, spriteEffect.h_0a + spriteEffect.v_0f - 1)
          .uv(3, spriteEffect.w_08 + spriteEffect.u_0e - 1, spriteEffect.h_0a + spriteEffect.v_0f - 1);

        if((spriteEffect.flags_00 & 0x4000_0000) != 0) {
          cmd.translucent(Translucency.of(spriteEffect.flags_00 >>> 28 & 0b11));
        }

        GPU.queueCommand(z / 4.0f, cmd);
      }
    }

    //LAB_800e7d8c
  }

  @Method(0x800e7dbcL)
  public static float transformToScreenSpace(final Vector2f out, final Vector3f translation) {
    final Vector3f transformed = new Vector3f();
    translation.mul(worldToScreenMatrix_800c3548, transformed);
    transformed.add(worldToScreenMatrix_800c3548.transfer);

    if(transformed.z >= 160) {
      out.x = transformed.x * projectionPlaneDistance_1f8003f8 / transformed.z;
      out.y = transformed.y * projectionPlaneDistance_1f8003f8 / transformed.z;
      return transformed.z / 4.0f;
    }

    //LAB_800e7e8c
    //LAB_800e7e90
    return 0;
  }

  @Method(0x800e7ec4L)
  public static <T extends EffectManagerParams<T>> void effectManagerDestructor(final ScriptState<EffectManagerData6c<T>> state, final EffectManagerData6c<T> struct) {
    LOGGER.info(EFFECTS, "Deallocating effect manager %d", state.index);

    if(struct.parentScript_50 != null) {
      if(struct.newChildScript_56 != null) {
        struct.newChildScript_56.innerStruct_00.oldChildScript_54 = struct.oldChildScript_54;
      } else {
        //LAB_800e7f4c
        struct.parentScript_50.innerStruct_00.childScript_52 = struct.oldChildScript_54;
      }

      //LAB_800e7f6c
      if(struct.oldChildScript_54 != null) {
        struct.oldChildScript_54.innerStruct_00.newChildScript_56 = struct.newChildScript_56;
      }

      //LAB_800e7fa0
      struct.parentScript_50 = null;
      struct.oldChildScript_54 = null;
      struct.newChildScript_56 = null;
    }

    //LAB_800e7fac
    //LAB_800e7fcc
    while(struct.childScript_52 != null) {
      EffectManagerData6c<?> child = struct.childScript_52.innerStruct_00;

      //LAB_800e7ff8
      while(child.childScript_52 != null) {
        child = child.childScript_52.innerStruct_00;
      }

      //LAB_800e8020
      child.myScriptState_0e.deallocateWithChildren();
    }

    //LAB_800e8040
    if(struct.destructor_4c != null) {
      struct.destructor_4c.accept(state, struct);
    }
  }

  public static ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> allocateEffectManager(final String name, @Nullable final ScriptState<? extends BattleObject> parentState, @Nullable final BiConsumer<ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>>, EffectManagerData6c<EffectManagerParams.VoidType>> ticker, @Nullable final BiConsumer<ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>>, EffectManagerData6c<EffectManagerParams.VoidType>> renderer, @Nullable final BiConsumer<ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>>, EffectManagerData6c<EffectManagerParams.VoidType>> destructor, @Nullable final Effect effect) {
    return allocateEffectManager(name, parentState, ticker, renderer, destructor, effect, new EffectManagerParams.VoidType());
  }

  @Method(0x800e80c4L)
  public static <T extends EffectManagerParams<T>> ScriptState<EffectManagerData6c<T>> allocateEffectManager(final String name, @Nullable ScriptState<? extends BattleObject> parentState, @Nullable final BiConsumer<ScriptState<EffectManagerData6c<T>>, EffectManagerData6c<T>> ticker, @Nullable final BiConsumer<ScriptState<EffectManagerData6c<T>>, EffectManagerData6c<T>> renderer, @Nullable final BiConsumer<ScriptState<EffectManagerData6c<T>>, EffectManagerData6c<T>> destructor, @Nullable final Effect effect, final T inner) {
    final ScriptState<EffectManagerData6c<T>> state = SCRIPTS.allocateScriptState(name, new EffectManagerData6c<>(name, inner));
    final EffectManagerData6c<T> manager = state.innerStruct_00;

    state.loadScriptFile(doNothingScript_8004f650);
    state.setTicker(SEffe::effectManagerTicker);

    if(renderer != null) {
      state.setRenderer(renderer);
    }

    state.setDestructor(SEffe::effectManagerDestructor);

    final StackWalker.StackFrame caller = DebugHelper.getCallerFrame();

    manager.effect_44 = effect;

    if(effect != null) {
      LOGGER.info(EFFECTS, "Allocating effect manager %d for %s (parent: %d) from %s.%s(%s:%d)", state.index, manager.effect_44.getClass().getSimpleName(), parentState != null ? parentState.index : -1, caller.getClassName(), caller.getMethodName(), caller.getFileName(), caller.getLineNumber());
    } else {
      LOGGER.info(EFFECTS, "Allocating empty effect manager %d (parent: %d) from %s.%s(%s:%d)", state.index, parentState != null ? parentState.index : -1, caller.getClassName(), caller.getMethodName(), caller.getFileName(), caller.getLineNumber());
    }

    manager.flags_04 = 0xff00_0000;
    manager.scriptIndex_0c = -1;
    manager.coord2Index_0d = -1;
    manager.myScriptState_0e = state;
    manager.params_10.flags_00 = 0x5400_0000;
    manager.params_10.scale_16.set(1.0f, 1.0f, 1.0f);
    manager.params_10.colour_1c.set(0x80, 0x80, 0x80);
    manager.ticker_48 = ticker;
    manager.destructor_4c = destructor;

    if(parentState != null) {
      if(!BattleObject.EM__.equals(parentState.innerStruct_00.magic_00)) {
        parentState = deffManager_800c693c.scriptState_1c;
      }

      final EffectManagerData6c<?> parent = (EffectManagerData6c<?>)parentState.innerStruct_00;
      final EffectManagerData6c<?> child = state.innerStruct_00;

      child.parentScript_50 = (ScriptState<EffectManagerData6c<?>>)parentState;
      if(parent.childScript_52 != null) {
        child.oldChildScript_54 = parent.childScript_52;
        parent.childScript_52.innerStruct_00.newChildScript_56 = (ScriptState)state;
      }

      parent.childScript_52 = (ScriptState)state;
    }

    return state;
  }

  @Method(0x800e8e9cL)
  public static <T extends EffectManagerParams<T>> void effectManagerTicker(final ScriptState<EffectManagerData6c<T>> state, final EffectManagerData6c<T> data) {
    AttachmentHost subPtr = data;

    //LAB_800e8ee0
    while(subPtr.getAttachment() != null) {
      final EffectAttachment sub = subPtr.getAttachment();

      final int ret = (int)((BiFunction)sub.ticker_08).apply(data, subPtr.getAttachment());
      if(ret == 0) { // Remove this attachment
        //LAB_800e8f2c
        data.flags_04 &= ~(1 << sub.id_05);
        subPtr.setAttachment(sub.getAttachment());
      } else if(ret == 1) { // Continue
        //LAB_800e8f6c
        subPtr = sub;
        //LAB_800e8f1c
      } else if(ret == 2) { // Remove this effect entirely
        //LAB_800e8f78
        state.deallocateWithChildren();
        return;
      }

      //LAB_800e8f8c
    }

    //LAB_800e8f9c
    if(data.ticker_48 != null) {
      data.ticker_48.accept(state, data);
    }

    //LAB_800e8fb8
  }

  @Method(0x800e7ea4L)
  public static void renderGenericSpriteAtZOffset0(final GenericSpriteEffect24 spriteEffect, final Vector3f translation) {
    FUN_800e7944(spriteEffect, translation, 0);
  }

  /** Considers all parents */
  @Method(0x800e8594L)
  public static void calculateEffectTransforms(final MV transformMatrix, final EffectManagerData6c<?> manager) {
    transformMatrix.rotationXYZ(manager.params_10.rot_10);
    transformMatrix.transfer.set(manager.params_10.trans_04);
    transformMatrix.scaleLocal(manager.params_10.scale_16);

    EffectManagerData6c<?> currentManager = manager;
    int scriptIndex = manager.scriptIndex_0c;

    //LAB_800e8604
    while(scriptIndex >= 0) {
      final ScriptState<?> state = scriptStatePtrArr_800bc1c0[scriptIndex];
      if(state == null) { // error, parent no longer exists
        manager.params_10.flags_00 |= 0x8000_0000;
        transformMatrix.transfer.z = -0x7fff;
        scriptIndex = -2;
        break;
      }

      final BattleObject base = (BattleObject)state.innerStruct_00;
      if(BattleObject.EM__.equals(base.magic_00)) {
        final EffectManagerData6c<?> baseManager = (EffectManagerData6c<?>)base;
        final MV baseTransformMatrix = new MV();
        baseTransformMatrix.rotationXYZ(baseManager.params_10.rot_10);
        baseTransformMatrix.transfer.set(baseManager.params_10.trans_04);
        baseTransformMatrix.scaleLocal(baseManager.params_10.scale_16);

        if(currentManager.coord2Index_0d != -1) {
          //LAB_800e866c
          FUN_800ea0f4(baseManager, currentManager.coord2Index_0d).coord.compose(baseTransformMatrix, baseTransformMatrix);
        }

        //LAB_800e86ac
        transformMatrix.compose(baseTransformMatrix);
        currentManager = baseManager;
        scriptIndex = currentManager.scriptIndex_0c;
        //LAB_800e86c8
      } else if(BattleObject.BOBJ.equals(base.magic_00)) {
        final BattleEntity27c bent = (BattleEntity27c)base;
        final Model124 s1 = bent.model_148;
        applyModelRotationAndScale(s1);
        final int coord2Index = currentManager.coord2Index_0d;

        final MV sp0x10 = new MV();
        if(coord2Index == -1) {
          sp0x10.set(s1.coord2_14.coord);
        } else {
          //LAB_800e8738
          GsGetLw(s1.modelParts_00[coord2Index].coord2_04, sp0x10);
          s1.modelParts_00[coord2Index].coord2_04.flg = 0;
        }

        //LAB_800e8774
        transformMatrix.compose(sp0x10);
        currentManager = null;
        scriptIndex = -1; // finished
      } else { // error, parent not a bent or effect
        //LAB_800e878c
        //LAB_800e8790
        manager.params_10.flags_00 |= 0x8000_0000;
        transformMatrix.transfer.z = -0x7fff;
        scriptIndex = -2;
        break;
      }
    }

    //LAB_800e87b4
    if(scriptIndex == -2) { // error
      final MV transposedWs = new MV();
      final Vector3f transposedTranslation = new Vector3f();
      worldToScreenMatrix_800c3548.transpose(transposedWs);
      transposedTranslation.set(worldToScreenMatrix_800c3548.transfer).negate();
      transposedTranslation.mul(transposedWs, transposedWs.transfer);
      transformMatrix.compose(transposedWs);
    }
    //LAB_800e8814
  }

  /** Has some relation to rendering of certain effect sprites, like ones from HUD DEFF */
  @Method(0x800e9428L)
  public static void renderBillboardSpriteEffect(final SpriteMetrics08 metrics, final EffectManagerParams<?> managerInner, final MV transformMatrix) {
    if(managerInner.flags_00 >= 0) { // No errors
      final GenericSpriteEffect24 spriteEffect = new GenericSpriteEffect24(managerInner.flags_00, metrics);
      spriteEffect.r_14 = managerInner.colour_1c.x & 0xff;
      spriteEffect.g_15 = managerInner.colour_1c.y & 0xff;
      spriteEffect.b_16 = managerInner.colour_1c.z & 0xff;
      spriteEffect.scaleX_1c = managerInner.scale_16.x;
      spriteEffect.scaleY_1e = managerInner.scale_16.y;
      spriteEffect.angle_20 = managerInner.rot_10.z;

      if((managerInner.flags_00 & 0x400_0000) != 0) {
        zOffset_1f8003e8 = managerInner.z_22;
        FUN_800e75ac(spriteEffect, transformMatrix);
      } else {
        //LAB_800e9574
        FUN_800e7944(spriteEffect, transformMatrix.transfer, managerInner.z_22);
      }
    }
    //LAB_800e9580
  }

  @Method(0x800ea0f4L)
  public static GsCOORDINATE2 FUN_800ea0f4(final EffectManagerData6c<?> effectManager, final int coord2Index) {
    final Model124 model = ((ModelEffect13c)effectManager.effect_44).model_10;
    applyModelRotationAndScale(model);
    return model.modelParts_00[coord2Index].coord2_04;
  }

  @Method(0x800ebb58L)
  public static void applyScreenDarkening(final int multiplier) {
    final BattleStageDarkening1800 darkening = stageDarkening_800c6958;

    //LAB_800ebb7c
    for(int y = 0; y < 16; y++) {
      //LAB_800ebb80
      for(int x = 0; x < stageDarkeningClutWidth_800c695c; x++) {
        final int colour = darkening.original_000[y][x] & 0xffff;
        final int mask = colour >>> 15 & 0x1;
        final int b = (colour >>> 10 & 0x1f) * multiplier >> 4 & 0x1f;
        final int g = (colour >>> 5 & 0x1f) * multiplier >> 4 & 0x1f;
        final int r = (colour & 0x1f) * multiplier >> 4 & 0x1f;

        final int newColour;
        if(r != 0 || g != 0 || b != 0 || colour == 0) {
          newColour = mask << 15 | b << 10 | g << 5 | r;
        } else {
          newColour = colour & 0xffff_8000 | 0x1;
        }

        darkening.modified_800[y][x] = newColour;
      }
    }

    for(int y = 0; y < 16; y++) {
      GPU.uploadData15(new Rect4i(448, (240 + y), 64, 1), stageDarkening_800c6958.modified_800[y]);
    }
  }

  @Method(0x800ec258L)
  public static void renderBttlShadow(final Model124 model) {
    final Model124 shadow = shadowModel_800bda10;

    GsInitCoordinate2(model.coord2_14, shadow.coord2_14);

    if(model.shadowType_cc == 3) {
      //LAB_800ec2ec
      shadow.coord2_14.coord.transfer.x = model.shadowOffset_118.x + model.modelParts_00[model.modelPartWithShadowIndex_cd].coord2_04.coord.transfer.x;
      shadow.coord2_14.coord.transfer.y = model.shadowOffset_118.y - MathHelper.safeDiv(model.coord2_14.coord.transfer.y, model.coord2_14.transforms.scale.y);
      shadow.coord2_14.coord.transfer.z = model.shadowOffset_118.z + model.modelParts_00[model.modelPartWithShadowIndex_cd].coord2_04.coord.transfer.z;
    } else {
      shadow.coord2_14.coord.transfer.x = model.shadowOffset_118.x;

      if(model.shadowType_cc == 1) {
        shadow.coord2_14.coord.transfer.y = model.shadowOffset_118.y;
      } else {
        //LAB_800ec2bc
        shadow.coord2_14.coord.transfer.y = model.shadowOffset_118.y - MathHelper.safeDiv(model.coord2_14.coord.transfer.y, model.coord2_14.transforms.scale.y);
      }

      //LAB_800ec2e0
      shadow.coord2_14.coord.transfer.z = model.shadowOffset_118.z;
    }

    //LAB_800ec370
    shadow.zOffset_a0 = model.zOffset_a0 + 16;
    shadow.coord2_14.transforms.scale.set(model.shadowSize_10c.x).div(4.0f);
    shadow.coord2_14.coord.rotationXYZ(shadow.coord2_14.transforms.rotate);
    shadow.coord2_14.coord.scaleLocal(shadow.coord2_14.transforms.scale);
    shadow.coord2_14.flg = 0;

    final GsCOORDINATE2 coord2 = shadow.modelParts_00[0].coord2_04;
    final Transforms transforms = coord2.transforms;
    transforms.rotate.zero();
    transforms.trans.zero();
    coord2.coord.rotationZYX(transforms.rotate);
    coord2.coord.transfer.set(transforms.trans);

    final MV lw = new MV();
    GsGetLw(shadow.modelParts_00[0].coord2_04, lw);

    RENDERER
      .queueModel(shadow.modelParts_00[0].obj, lw)
      .depthOffset(-0.0001f)
      .lightDirection(lightDirectionMatrix_800c34e8)
      .lightColour(lightColourMatrix_800c3508)
      .backgroundColour(GTE.backgroundColour);

    shadow.modelParts_00[0].coord2_04.flg--;
  }

  @ScriptDescription("Allocates a particle effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "parentIndex", description = "The parent index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "Packed value, controls how the particle behaves")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "count", description = "The particle count")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p4")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p5")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p6")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p7", description = "Unknown packed values")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type2", description = "Also controls how the particle behaves")
  @Method(0x80102088L)
  public static FlowControl scriptAllocateParticleEffect(final RunningScript<? extends BattleObject> script) {
    final int scriptIndex = script.params_20[0].get();
    final int particleTypeId = script.params_20[2].get();
    final int particleCount = script.params_20[3].get();
    final int _10 = script.params_20[4].get();
    final int _14 = script.params_20[5].get();
    final int _18 = script.params_20[6].get();
    final int innerStuff = script.params_20[7].get();
    final int particleType = script.params_20[8].get();

    final ParticleEffectData98 effect = ((Battle)currentEngineState_8004dd04).particles.allocateParticle(script.scriptState_04, particleType, particleCount, particleTypeId, _10, _14, _18, innerStuff, scriptIndex, script.params_20[1].get());

    script.params_20[0].set(effect.myState_00.index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets a particle's acceleration")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "accelerationX", description = "The X acceleration")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "accelerationY", description = "The Y acceleration")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "accelerationZ", description = "The Z acceleration")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "accelerationScale", description = "A value multiplied with the acceleration")
  @Method(0x80102364L)
  public static FlowControl scriptSetParticleAcceleration(final RunningScript<?> script) {
    final ParticleEffectData98 effect = (ParticleEffectData98)SCRIPTS.getObject(script.params_20[1].get(), EffectManagerData6c.classFor(EffectManagerParams.ParticleType.class)).effect_44;

    final int mode = script.params_20[0].get();
    if(mode == 0) {
      effect.scaleOrUseEffectAcceleration_6c = true;
      effect.effectAcceleration_70.set(script.params_20[2].get() / (float)0x100, script.params_20[3].get() / (float)0x100, script.params_20[4].get() / (float)0x100);
      effect.scaleParticleAcceleration_80 = script.params_20[5].get();
      //LAB_801023d0
    } else if(mode == 1) {
      effect.scaleOrUseEffectAcceleration_6c = false;
      //LAB_801023e0
    } else if(mode == 2) {
      //LAB_801023e8
      effect.scaleParticleAcceleration_80 = script.params_20[5].get();
    }

    //LAB_801023ec
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("No-op")
  @Method(0x801023f4L)
  public static FlowControl FUN_801023f4(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Returns which particles are alive")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The particle effect index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL_ARRAY, name = "alive", description = "An array of booleans, each one denoting whether its respective particle instance is alive")
  @Method(0x801023fcL)
  public static FlowControl scriptGetAliveParticles(final RunningScript<?> script) {
    final ParticleEffectData98 effect = (ParticleEffectData98)SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.classFor(EffectManagerParams.ParticleType.class)).effect_44;

    //LAB_8010243c
    for(int i = 0; i < effect.countParticleInstance_50; i++) {
      final ParticleEffectInstance94 particle = effect.particleArray_68[i];
      script.params_20[1].array(i).set(particle.flags_90 & 1);
    }

    //LAB_80102464
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the position of a particle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The particle effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "particleIndex", description = "The particle index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The X position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z position")
  @Method(0x8010246cL)
  public static FlowControl scriptGetParticlePosition(final RunningScript<?> script) {
    final ParticleEffectData98 effect = (ParticleEffectData98)SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.classFor(EffectManagerParams.ParticleType.class)).effect_44;
    final ParticleEffectInstance94 particle = effect.particleArray_68[script.params_20[1].get()];

    final Vector3f sp0x20 = new Vector3f();
    FUN_800cf684(particle.managerRotation_68, particle.managerTranslation_2c, particle.particlePosition_50, sp0x20);
    script.params_20[2].set(Math.round(sp0x20.x));
    script.params_20[3].set(Math.round(sp0x20.y));
    script.params_20[4].set(Math.round(sp0x20.z));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("No-op (pause)")
  @Method(0x80102608L)
  public static FlowControl FUN_80102608(final RunningScript<?> script) {
    return FlowControl.PAUSE;
  }

  @ScriptDescription("No-op")
  @Method(0x80102610L)
  public static FlowControl FUN_80102610(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @Method(0x80102618L)
  public static void modifyLightningSegmentOriginsBySecondaryScriptTranslation(final EffectManagerData6c<EffectManagerParams.ElectricityType> manager, final ElectricityEffect38 electricEffect, final LightningBoltEffect14 boltEffect) {
    if(electricEffect.scriptIndex_08 != -1) {
      final Vector3f secondaryScriptTranslation = new Vector3f();
      final Vector3f newOrigin = new Vector3f();
      scriptGetScriptedObjectPos(electricEffect.scriptIndex_08, secondaryScriptTranslation);
      secondaryScriptTranslation.sub(manager.params_10.trans_04).div(electricEffect.boltSegmentCount_28);

      //LAB_801026f0
      for(int i = 0; i < electricEffect.boltSegmentCount_28; i++) {
        final LightningBoltEffectSegment30 boltSegment = boltEffect.boltSegments_10[i];
        boltSegment.origin_00.set(newOrigin);

        newOrigin.x += (seed_800fa754.nextInt(257) - 128) * electricEffect.segmentOriginTranslationModifier_26 >> 7;
        newOrigin.z += (seed_800fa754.nextInt(257) - 128) * electricEffect.segmentOriginTranslationModifier_26 >> 7;
        newOrigin.add(secondaryScriptTranslation);
      }
    }
    //LAB_80102848
  }

  @Method(0x80102860L)
  public static void initializeRadialElectricityBoltColour(final EffectManagerData6c<EffectManagerParams.ElectricityType> manager, final ElectricityEffect38 electricEffect) {
    int innerColourR;
    int innerColourG;
    int innerColourB;
    int outerColourR;
    int outerColourG;
    int outerColourB;
    int innerColourFadeStepR = 0;
    int innerColourFadeStepG = 0;
    int innerColourFadeStepB = 0;
    int outerColourFadeStepR = 0;
    int outerColourFadeStepG = 0;
    int outerColourFadeStepB = 0;

    //LAB_801028a4
    for(int boltNum = 0; boltNum < electricEffect.boltCount_00; boltNum++) {
      final LightningBoltEffect14 bolt = electricEffect.bolts_34[boltNum];

      outerColourR = manager.params_10.colour_1c.x << 8;
      outerColourG = manager.params_10.colour_1c.y << 8;
      outerColourB = manager.params_10.colour_1c.z << 8;

      final int managerR = manager.params_10.colour_1c.x;
      int managerG = manager.params_10.colour_1c.y;
      int managerB = manager.params_10.colour_1c.z;

      if(managerG < managerR) {
        managerG = managerR;
      }

      //LAB_8010290c
      if(managerB < managerG) {
        managerB = managerG;
      }

      //LAB_8010292c
      innerColourR = managerB << 8;
      innerColourG = managerB << 8;
      innerColourB = managerB << 8;

      if(electricEffect.fadeSuccessiveSegments_23) {
        innerColourFadeStepR = innerColourR / electricEffect.boltSegmentCount_28;
        innerColourFadeStepG = innerColourG / electricEffect.boltSegmentCount_28;
        innerColourFadeStepB = innerColourB / electricEffect.boltSegmentCount_28;
        outerColourFadeStepR = outerColourR / electricEffect.boltSegmentCount_28;
        outerColourFadeStepG = outerColourG / electricEffect.boltSegmentCount_28;
        outerColourFadeStepB = outerColourB / electricEffect.boltSegmentCount_28;
      }

      //LAB_801029f0
      //LAB_80102a04
      for(int segmentNum = 0; segmentNum < electricEffect.boltSegmentCount_28; segmentNum++) {
        final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[segmentNum];
        segment.innerColour_10.set(innerColourR, innerColourG, innerColourB);
        segment.outerColour_16.set(outerColourR, outerColourG, outerColourB);

        if(electricEffect.colourShouldFade_22 && electricEffect.numColourFadeSteps_0c != -1) {
          segment.innerColourFadeStep_1c.set(segment.innerColour_10).div(electricEffect.numColourFadeSteps_0c);
          segment.outerColourFadeStep_22.set(segment.outerColour_16).div(electricEffect.numColourFadeSteps_0c);
        } else {
          //LAB_80102b10
          segment.innerColourFadeStep_1c.set(0, 0, 0);
          segment.outerColourFadeStep_22.set(0, 0, 0);
        }

        //LAB_80102b28
        innerColourR = innerColourR - innerColourFadeStepR;
        innerColourG = innerColourG - innerColourFadeStepG;
        innerColourB = innerColourB - innerColourFadeStepB;
        outerColourR = outerColourR - outerColourFadeStepR;
        outerColourG = outerColourG - outerColourFadeStepG;
        outerColourB = outerColourB - outerColourFadeStepB;
      }

      //LAB_80102bb8
      modifyLightningSegmentOriginsBySecondaryScriptTranslation(manager, electricEffect, bolt);
    }
    //LAB_80102be0
  }

  @Method(0x80102bfcL)
  public static void initializeElectricityNodes(final EffectManagerData6c<EffectManagerParams.ElectricityType> manager, final ElectricityEffect38 electricEffect, final LightningBoltEffect14 bolt) {
    float segmentOriginX = 0;
    final float segmentOriginY = -(electricEffect.boltAngleRangeCutoff_1c / (float)electricEffect.boltSegmentCount_28);
    float segmentOriginZ = 0;

    //LAB_80102c58
    for(int i = 0; i < electricEffect.boltSegmentCount_28; i++) {
      final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[i];
      segment.origin_00.set(segmentOriginX, segmentOriginY * i, segmentOriginZ);
      segment.scaleMultiplier_28 = seed_800fa754.nextInt(7) + 5;
      segment.unused_2a = 0;
      segment.originTranslationMagnitude_2c = segmentOriginX / 16.0f;
      segment.baseVertexTranslationScale_2e = (seed_800fa754.nextInt(193) + 64) / (float)0x100;

      if(electricEffect.addSuccessiveSegmentOriginTranslations_14) {
        segmentOriginX += (seed_800fa754.nextInt(257) - 128) * electricEffect.segmentOriginTranslationModifier_26 >> 7;
        segmentOriginZ += (seed_800fa754.nextInt(257) - 128) * electricEffect.segmentOriginTranslationModifier_26 >> 7;
        //LAB_80102e58
      } else if(i < electricEffect.boltSegmentCount_28 - 2) {
        segmentOriginX = (seed_800fa754.nextInt(257) - 128) * electricEffect.segmentOriginTranslationModifier_26 >> 7;
        segmentOriginZ = (seed_800fa754.nextInt(257) - 128) * electricEffect.segmentOriginTranslationModifier_26 >> 7;
      } else {
        //LAB_80102f44
        segmentOriginX = 0;
        segmentOriginZ = 0;
      }
      //LAB_80102f4c
    }

    //LAB_80102f64
    modifyLightningSegmentOriginsBySecondaryScriptTranslation(manager, electricEffect, bolt);
  }

  /**
   * Used by the following two lightning renderers and FUN_80105704
   *
   * @param xy 4 vertices (note: data was originally passed in as ints so you need to change the calling code)
   */
  @Method(0x80102f7cL)
  public static void renderSegmentGradient(final Vector3i colour1, final Vector3i colour2, final Vector2f[] xy, final float a3, final int a4, final Translucency translucency) {
    final GpuCommandPoly cmd = new GpuCommandPoly(4)
      .translucent(translucency)
      .pos(0, xy[0].x, xy[0].y)
      .pos(1, xy[1].x, xy[1].y)
      .pos(2, xy[2].x, xy[2].y)
      .pos(3, xy[3].x, xy[3].y)
      .monochrome(0, 0)
      .rgb(1, colour2.x >>> 8, colour2.y >>> 8, colour2.z >>> 8)
      .monochrome(2, 0)
      .rgb(3, colour1.x >>> 8, colour1.y >>> 8, colour1.z >>> 8);

    GPU.queueCommand((a3 + a4) / 4.0f, cmd);
  }

  /**
   * Renders certain types of lightning effects. (Confirmed for Rose's D transformation)
   * Used by allocator 0x801052dc
   */
  @Method(0x801030d8L)
  public static void renderElectricEffectType0(final ScriptState<EffectManagerData6c<EffectManagerParams.ElectricityType>> state, final EffectManagerData6c<EffectManagerParams.ElectricityType> manager) {
    final ElectricityEffect38 electricEffect = (ElectricityEffect38)manager.effect_44;

    if(electricEffect.currentColourFadeStep_04 + 1 == electricEffect.numColourFadeSteps_0c) {
      return;
    }

    electricEffect.currentColourFadeStep_04++;

    //LAB_80103140
    if(electricEffect.currentColourFadeStep_04 == 1) {
      initializeRadialElectricityBoltColour(manager, electricEffect);

      //LAB_80103174
      for(int i = 0; i < electricEffect.boltCount_00; i++) {
        electricEffect.callback_2c.accept(manager, electricEffect, electricEffect.bolts_34[i], i);
      }

      return;
    }

    //LAB_801031cc
    electricEffect.frameNum_2a = electricEffect.frameNum_2a + 1 & 0x1f;

    final boolean effectShouldRender = (manager.params_10.shouldRenderFrameBits_24 >> electricEffect.frameNum_2a & 0x1) == 0;

    final Vector2f[] vertexArray = new Vector2f[4];
    Arrays.setAll(vertexArray, i -> new Vector2f());

    final Vector2f refOuterOriginA = new Vector2f();
    final Vector2f lastSegmentRef = new Vector2f();
    final Vector2f refOuterOriginB = new Vector2f();

    //LAB_80103200
    //LAB_8010322c
    for(int boltNum = 0; boltNum < electricEffect.boltCount_00; boltNum++) {
      final LightningBoltEffect14 bolt = electricEffect.bolts_34[boltNum];

      if(electricEffect.reinitializeNodes_24) {
        initializeElectricityNodes(manager, electricEffect, bolt);
      }

      //LAB_8010324c
      electricEffect.callback_2c.accept(manager, electricEffect, bolt, boltNum);
      bolt.angle_02 -= electricEffect.boltAngleStep_10 / 2.0f;
      float zMod = FUN_800cfb94(manager, bolt.rotation_04, bolt.boltSegments_10[0].origin_00, refOuterOriginA) / 4.0f;
      FUN_800cfb94(manager, bolt.rotation_04, bolt.boltSegments_10[electricEffect.boltSegmentCount_28 - 1].origin_00, lastSegmentRef);
      final float boltLengthX = lastSegmentRef.x - refOuterOriginA.x;
      final float boltLengthY = lastSegmentRef.y - refOuterOriginA.y;
      final float segmentLengthX = boltLengthX / (electricEffect.boltSegmentCount_28 - 1);
      final float segmentLengthY = boltLengthY / (electricEffect.boltSegmentCount_28 - 1);
      float previousOriginX = refOuterOriginA.x;
      float centerLineOriginX = refOuterOriginA.x;
      float previousOriginY = refOuterOriginA.y;
      float centerLineOriginY = refOuterOriginA.y;
      final float firstSegmentScaleX = bolt.boltSegments_10[0].scaleMultiplier_28 * manager.params_10.scale_16.x;
      final float angle = -MathHelper.atan2(boltLengthX, boltLengthY);
      final float sin = MathHelper.sin(angle);
      final float cos = MathHelper.cosFromSin(sin, angle);
      final float outerXOffset = cos * firstSegmentScaleX;
      final float outerYOffset = sin * firstSegmentScaleX;
      refOuterOriginB.x = refOuterOriginA.x - outerXOffset;
      refOuterOriginB.y = refOuterOriginA.y - outerYOffset;
      refOuterOriginA.x += outerXOffset;
      refOuterOriginA.y += outerYOffset;

      //LAB_80103488
      for(int segmentNum = 0; segmentNum < electricEffect.boltSegmentCount_28; segmentNum++) {
        final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[segmentNum];
        segment.innerColour_10.sub(segment.innerColourFadeStep_1c);
        segment.outerColour_16.sub(segment.outerColourFadeStep_22);
        segment.unused_2a += electricEffect.boltAngleStep_10 / 2.0f;
      }

      //LAB_80103538
      if(effectShouldRender) {
        final float z = manager.params_10.z_22 + zMod;
        if(z >= 0xa0) {
          if(z >= 0xffe) {
            zMod = 0xffe - manager.params_10.z_22;
          }

          final Translucency translucency = Translucency.of(manager.params_10.flags_00 >>> 28 & 3);

          //LAB_80103574
          //LAB_80103594
          for(int segmentNum = 0; segmentNum < electricEffect.boltSegmentCount_28 - 1; segmentNum++) {
            final LightningBoltEffectSegment30 currentSegment = bolt.boltSegments_10[segmentNum];
            final LightningBoltEffectSegment30 nextSegment = bolt.boltSegments_10[segmentNum + 1];
            float centerLineEndpointX = centerLineOriginX + segmentLengthX;
            float centerLineEndpointY = centerLineOriginY + segmentLengthY;
            final float currentSegmentEndpointX = centerLineOriginX + segmentLengthX;
            final float currentSegmentEndpointY = centerLineOriginY + segmentLengthY;
            centerLineOriginX += cos * currentSegment.originTranslationMagnitude_2c;
            centerLineOriginY += sin * currentSegment.originTranslationMagnitude_2c;
            centerLineEndpointX += cos * nextSegment.originTranslationMagnitude_2c;
            centerLineEndpointY += sin * nextSegment.originTranslationMagnitude_2c;
            final float scale = currentSegment.scaleMultiplier_28 * manager.params_10.scale_16.x;
            final float outerEndpointXa = centerLineEndpointX + cos * scale;
            final float outerEndpointYa = centerLineEndpointY + sin * scale;
            final float outerEndpointXb = centerLineEndpointX - cos * scale;
            final float outerEndpointYb = centerLineEndpointY - sin * scale;

            if(electricEffect.hasMonochromeBase_29) {
              final float baseX0 = (previousOriginX - centerLineOriginX) * currentSegment.baseVertexTranslationScale_2e + centerLineOriginX;
              final float baseY0 = (previousOriginY - centerLineOriginY) * currentSegment.baseVertexTranslationScale_2e + centerLineOriginY;
              final float baseX2 = (centerLineEndpointX - centerLineOriginX) * currentSegment.baseVertexTranslationScale_2e + centerLineOriginX;
              final float baseY2 = (centerLineEndpointY - centerLineOriginY) * currentSegment.baseVertexTranslationScale_2e + centerLineOriginY;

              //LAB_80103808
              int baseColour = (int)(currentSegment.innerColour_10.x + Math.abs(currentSegment.originTranslationMagnitude_2c - nextSegment.originTranslationMagnitude_2c) * 8);
              if((baseColour & 0xffff) > 0xff00) {
                baseColour = 0xff00;
              }

              //LAB_80103834
              final GpuCommandPoly cmd = new GpuCommandPoly(3)
                .translucent(translucency)
                .pos(0, baseX0, baseY0)
                .pos(1, centerLineOriginX, centerLineOriginY)
                .pos(2, baseX2, baseY2)
                .monochrome(0, baseColour >>> 9)
                .monochrome(1, baseColour >>> 8)
                .monochrome(2, baseColour >>> 9);

              GPU.queueCommand((manager.params_10.z_22 + zMod) / 4.0f, cmd);
            }

            //LAB_80103994
            vertexArray[1].set(centerLineEndpointX, centerLineEndpointY);
            vertexArray[3].set(centerLineOriginX, centerLineOriginY);

            vertexArray[0].set(outerEndpointXa, outerEndpointYa);
            vertexArray[2].set(refOuterOriginA);
            renderSegmentGradient(currentSegment.outerColour_16, nextSegment.outerColour_16, vertexArray, zMod, manager.params_10.z_22, translucency);

            vertexArray[0].x = (vertexArray[0].x - vertexArray[1].x) / manager.params_10.sizeDivisor_30 + vertexArray[1].x;
            vertexArray[0].y = (vertexArray[0].y - vertexArray[1].y) / manager.params_10.sizeDivisor_30 + vertexArray[1].y;
            vertexArray[2].x = (vertexArray[2].x - vertexArray[3].x) / manager.params_10.sizeDivisor_30 + vertexArray[3].x;
            vertexArray[2].y = (vertexArray[2].y - vertexArray[3].y) / manager.params_10.sizeDivisor_30 + vertexArray[3].y;
            renderSegmentGradient(currentSegment.innerColour_10, nextSegment.innerColour_10, vertexArray, zMod, manager.params_10.z_22, translucency);

            vertexArray[0].set(outerEndpointXb, outerEndpointYb);
            vertexArray[2].set(refOuterOriginB);
            renderSegmentGradient(currentSegment.outerColour_16, nextSegment.outerColour_16, vertexArray, zMod, manager.params_10.z_22, translucency);

            vertexArray[0].x = (vertexArray[0].x - vertexArray[1].x) / manager.params_10.sizeDivisor_30 + vertexArray[1].x;
            vertexArray[0].y = (vertexArray[0].y - vertexArray[1].y) / manager.params_10.sizeDivisor_30 + vertexArray[1].y;
            vertexArray[2].x = (vertexArray[2].x - vertexArray[3].x) / manager.params_10.sizeDivisor_30 + vertexArray[3].x;
            vertexArray[2].y = (vertexArray[2].y - vertexArray[3].y) / manager.params_10.sizeDivisor_30 + vertexArray[3].y;
            renderSegmentGradient(currentSegment.innerColour_10, nextSegment.innerColour_10, vertexArray, zMod, manager.params_10.z_22, translucency);

            refOuterOriginA.set(outerEndpointXa, outerEndpointYa);
            refOuterOriginB.set(outerEndpointXb, outerEndpointYb);
            previousOriginX = centerLineOriginX;
            previousOriginY = centerLineOriginY;
            centerLineOriginX = currentSegmentEndpointX;
            centerLineOriginY = currentSegmentEndpointY;
          }
          //LAB_80103ca0
        }
      }
    }
  }

  /**
   * Renders some lightning effects (confirmed at the end of Rose's D transformation, Melbu's D Block,
   * some Haschel stuff, Doel's fancy lightning bomb attack)
   * Used by allocator 0x801052dc
   */
  @Method(0x80103db0L)
  public static void renderElectricEffectType1(final ScriptState<EffectManagerData6c<EffectManagerParams.ElectricityType>> state, final EffectManagerData6c<EffectManagerParams.ElectricityType> manager) {
    final ElectricityEffect38 electricEffect = (ElectricityEffect38)manager.effect_44;

    if(electricEffect.currentColourFadeStep_04 + 1 == electricEffect.numColourFadeSteps_0c) {
      return;
    }

    electricEffect.currentColourFadeStep_04++;

    //LAB_80103e40
    if(electricEffect.currentColourFadeStep_04 == 1) {
      initializeRadialElectricityBoltColour(manager, electricEffect);

      //LAB_80103e8c
      for(int i = 0; i < electricEffect.boltCount_00; i++) {
        electricEffect.callback_2c.accept(manager, electricEffect, electricEffect.bolts_34[i], i);
      }
    } else {
      final LightningBoltEffectSegmentOrigin08[] segmentArray = new LightningBoltEffectSegmentOrigin08[electricEffect.boltSegmentCount_28];
      Arrays.setAll(segmentArray, LightningBoltEffectSegmentOrigin08::new);
      LightningBoltEffectSegmentOrigin08 currentSegmentOrigin;
      LightningBoltEffectSegmentOrigin08 nextSegmentOrigin;

      //LAB_80103ee4
      electricEffect.frameNum_2a = electricEffect.frameNum_2a + 1 & 0x1f;
      final boolean effectShouldRender = (manager.params_10.shouldRenderFrameBits_24 >> electricEffect.frameNum_2a & 0x1) == 0;

      //LAB_80103f18
      //LAB_80103f44
      for(int i = 0; i < electricEffect.boltCount_00; i++) {
        final LightningBoltEffect14 bolt = electricEffect.bolts_34[i];

        if(electricEffect.reinitializeNodes_24) {
          initializeElectricityNodes(manager, electricEffect, bolt);
        }

        //LAB_80103f6c
        electricEffect.callback_2c.accept(manager, electricEffect, bolt, i);

        bolt.angle_02 -= electricEffect.boltAngleStep_10 / 2.0f;

        //LAB_80103fc4
        int segmentNum;
        for(segmentNum = 0; segmentNum < electricEffect.boltSegmentCount_28; segmentNum++) {
          final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[segmentNum];
          final LightningBoltEffectSegmentOrigin08 segmentOrigin = segmentArray[segmentNum];
          final Vector2f ref = new Vector2f();
          bolt.sz3_0c = FUN_800cfb94(manager, bolt.rotation_04, segment.origin_00, ref) / 4;
          segmentOrigin.x_00 = ref.x;
          segmentOrigin.y_04 = ref.y;
          segment.innerColour_10.sub(segment.innerColourFadeStep_1c);
          segment.outerColour_16.sub(segment.outerColourFadeStep_22);
          segment.unused_2a += electricEffect.boltAngleStep_10;
        }

        //LAB_801040d0
        final float boltLengthX = segmentArray[segmentNum - 1].x_00 - segmentArray[0].x_00;
        final float boltLengthY = segmentArray[segmentNum - 1].y_04 - segmentArray[0].y_04;
        final float angle = -MathHelper.atan2(boltLengthX, boltLengthY);
        final float sin = MathHelper.sin(angle);
        final float cos = MathHelper.cosFromSin(sin, angle);
        float currentSegmentScale = bolt.boltSegments_10[0].scaleMultiplier_28 * manager.params_10.scale_16.x;
        float outerOriginXa = segmentArray[0].x_00 + cos * currentSegmentScale;
        float outerOriginYa = segmentArray[0].y_04 + sin * currentSegmentScale;
        float outerOriginXb = segmentArray[0].x_00 - cos * currentSegmentScale;
        float outerOriginYb = segmentArray[0].y_04 - sin * currentSegmentScale;

        if(effectShouldRender) {
          final Translucency translucency = Translucency.of(manager.params_10.flags_00 >>> 28 & 3);

          final float z = manager.params_10.z_22 + bolt.sz3_0c;
          if(z >= 0xa0) {
            if(z >= 0xffe) {
              bolt.sz3_0c = 0xffe - manager.params_10.z_22;
            }

            //LAB_8010422c
            //LAB_8010424c
            for(segmentNum = 0; segmentNum < electricEffect.boltSegmentCount_28 - 1; segmentNum++) {
              final LightningBoltEffectSegment30 currentSegment = bolt.boltSegments_10[segmentNum];
              final LightningBoltEffectSegment30 nextSegment = bolt.boltSegments_10[segmentNum + 1];
              currentSegmentScale = currentSegment.scaleMultiplier_28 * manager.params_10.scale_16.x;
              final float nextSegmentScale = nextSegment.scaleMultiplier_28 * manager.params_10.scale_16.x;

              if(electricEffect.type1RendererType_18) {
                currentSegmentOrigin = segmentArray[segmentNum];
                nextSegmentOrigin = segmentArray[segmentNum + 1];
                final float outerEndpointXa = nextSegmentOrigin.x_00 + cos * currentSegmentScale;
                final float outerEndpointYa = nextSegmentOrigin.y_04 + sin * currentSegmentScale;
                final float outerEndpointXb = nextSegmentOrigin.x_00 - cos * currentSegmentScale;
                final float outerEndpointYb = nextSegmentOrigin.y_04 - sin * currentSegmentScale;

                final Vector2f[] vertexArray = new Vector2f[4];
                Arrays.setAll(vertexArray, n -> new Vector2f());

                vertexArray[1].x = nextSegmentOrigin.x_00;
                vertexArray[1].y = nextSegmentOrigin.y_04;
                vertexArray[3].x = currentSegmentOrigin.x_00;
                vertexArray[3].y = currentSegmentOrigin.y_04;

                vertexArray[0].x = outerEndpointXa;
                vertexArray[0].y = outerEndpointYa;
                vertexArray[2].x = outerOriginXa;
                vertexArray[2].y = outerOriginYa;
                renderSegmentGradient(currentSegment.outerColour_16, nextSegment.outerColour_16, vertexArray, bolt.sz3_0c, manager.params_10.z_22, translucency);

                vertexArray[0].x = (vertexArray[0].x - vertexArray[1].x) / manager.params_10.sizeDivisor_30 + vertexArray[1].x;
                vertexArray[0].y = (vertexArray[0].y - vertexArray[1].y) / manager.params_10.sizeDivisor_30 + vertexArray[1].y;
                vertexArray[2].x = (vertexArray[2].x - vertexArray[3].x) / manager.params_10.sizeDivisor_30 + vertexArray[3].x;
                vertexArray[2].y = (vertexArray[2].y - vertexArray[3].y) / manager.params_10.sizeDivisor_30 + vertexArray[3].y;
                renderSegmentGradient(currentSegment.innerColour_10, nextSegment.innerColour_10, vertexArray, bolt.sz3_0c, manager.params_10.z_22, translucency);

                vertexArray[0].x = outerEndpointXb;
                vertexArray[0].y = outerEndpointYb;
                vertexArray[2].x = outerOriginXb;
                vertexArray[2].y = outerOriginYb;
                renderSegmentGradient(currentSegment.outerColour_16, nextSegment.outerColour_16, vertexArray, bolt.sz3_0c, manager.params_10.z_22, translucency);

                vertexArray[0].x = (vertexArray[0].x - vertexArray[1].x) / manager.params_10.sizeDivisor_30 + vertexArray[1].x;
                vertexArray[0].y = (vertexArray[0].y - vertexArray[1].y) / manager.params_10.sizeDivisor_30 + vertexArray[1].y;
                vertexArray[2].x = (vertexArray[2].x - vertexArray[3].x) / manager.params_10.sizeDivisor_30 + vertexArray[3].x;
                vertexArray[2].y = (vertexArray[2].y - vertexArray[3].y) / manager.params_10.sizeDivisor_30 + vertexArray[3].y;
                renderSegmentGradient(currentSegment.innerColour_10, nextSegment.innerColour_10, vertexArray, bolt.sz3_0c, manager.params_10.z_22, translucency);

                outerOriginXa = outerEndpointXa;
                outerOriginYa = outerEndpointYa;
                outerOriginXb = outerEndpointXb;
                outerOriginYb = outerEndpointYb;
              } else {
                //LAB_801045e8
                currentSegmentOrigin = segmentArray[segmentNum];
                nextSegmentOrigin = segmentArray[segmentNum + 1];
                float centerLineEndpointX = Math.abs(currentSegmentOrigin.x_00 - nextSegmentOrigin.x_00);
                centerLineEndpointX = seed_800fa754.nextFloat(centerLineEndpointX * 2 + 1) - centerLineEndpointX + currentSegmentOrigin.x_00;
                float centerLineOriginX = currentSegmentOrigin.x_00;
                float centerLineOriginY = currentSegmentOrigin.y_04;
                float centerLineEndpointY = (nextSegmentOrigin.y_04 - currentSegmentOrigin.y_04) / 2 + currentSegmentOrigin.y_04;
                final float nextSegmentQuarterScale = nextSegmentScale / 4.0f;
                final float currentSegmentQuarterScale = currentSegmentScale / 4.0f;

                //LAB_801046c4
                for(int j = 2; j > 0; j--) {
                  final Vector2f[] vertexArray = new Vector2f[4];
                  Arrays.setAll(vertexArray, n -> new Vector2f());

                  vertexArray[0].y = centerLineEndpointY;
                  vertexArray[1].y = centerLineEndpointY;
                  vertexArray[2].y = centerLineOriginY;
                  vertexArray[3].y = centerLineOriginY;

                  vertexArray[0].x = centerLineEndpointX - nextSegmentScale;
                  vertexArray[1].x = centerLineEndpointX + 1;
                  vertexArray[2].x = centerLineOriginX - currentSegmentScale;
                  vertexArray[3].x = centerLineOriginX + 1;
                  renderSegmentGradient(currentSegment.outerColour_16, nextSegment.outerColour_16, vertexArray, bolt.sz3_0c, manager.params_10.z_22, translucency);
                  vertexArray[0].x = centerLineEndpointX - nextSegmentQuarterScale;
                  vertexArray[2].x = centerLineOriginX - currentSegmentQuarterScale;
                  renderSegmentGradient(currentSegment.innerColour_10, nextSegment.innerColour_10, vertexArray, bolt.sz3_0c, manager.params_10.z_22, translucency);
                  vertexArray[0].x = centerLineEndpointX + nextSegmentScale;
                  vertexArray[1].x = centerLineEndpointX;
                  vertexArray[2].x = centerLineOriginX + currentSegmentScale;
                  vertexArray[3].x = centerLineOriginX;
                  renderSegmentGradient(currentSegment.outerColour_16, nextSegment.outerColour_16, vertexArray, bolt.sz3_0c, manager.params_10.z_22, translucency);
                  vertexArray[0].x = centerLineEndpointX + nextSegmentQuarterScale;
                  vertexArray[2].x = centerLineOriginX + currentSegmentQuarterScale;
                  renderSegmentGradient(currentSegment.innerColour_10, nextSegment.innerColour_10, vertexArray, bolt.sz3_0c, manager.params_10.z_22, translucency);

                  centerLineOriginX = centerLineEndpointX;
                  centerLineOriginY = centerLineEndpointY;
                  centerLineEndpointX = nextSegmentOrigin.x_00;
                  centerLineEndpointY = nextSegmentOrigin.y_04;
                }
              }
            }
          }
        }
      }
      //LAB_8010490c
    }
    //LAB_8010491c
  }

  @Method(0x801049d4L)
  public static void FUN_801049d4(final EffectManagerData6c<EffectManagerParams.ElectricityType> a0, final ElectricityEffect38 a1, final LightningBoltEffect14 a2, final int a3) {
    // no-op
  }

  @Method(0x801049dcL)
  public static void FUN_801049dc(final EffectManagerData6c<EffectManagerParams.ElectricityType> a0, final ElectricityEffect38 electricEffect, final LightningBoltEffect14 boltEffect, final int boltIndex) {
    boltEffect.rotation_04.y = MathHelper.TWO_PI / electricEffect.boltCount_00 * boltIndex;
    boltEffect.rotation_04.z = MathHelper.psxDegToRad(electricEffect.segmentOriginTranslationMagnitude_1e * 2);
  }

  @Method(0x80104a14L)
  public static void FUN_80104a14(final EffectManagerData6c<EffectManagerParams.ElectricityType> a0, final ElectricityEffect38 a1, final LightningBoltEffect14 bolt, final int a3) {
    bolt.rotation_04.x = seed_800fa754.nextFloat(MathHelper.TWO_PI);
    bolt.rotation_04.y = seed_800fa754.nextFloat(MathHelper.TWO_PI);
    bolt.rotation_04.z = seed_800fa754.nextFloat(MathHelper.TWO_PI);
  }

  @Method(0x80104b10L)
  public static void FUN_80104b10(final EffectManagerData6c<EffectManagerParams.ElectricityType> manager, final ElectricityEffect38 electricEffect, final LightningBoltEffect14 bolt, final int a3) {
    final float angleStep = MathHelper.psxDegToRad(manager.params_10._28 << 8 >> 8);
    float angle = bolt.angle_02;

    //LAB_80104b58
    for(int i = 0; i < electricEffect.boltSegmentCount_28; i++) {
      final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[i];
      segment.origin_00.x += MathHelper.cos(angle) * electricEffect.segmentOriginTranslationMagnitude_1e;
      segment.origin_00.z += MathHelper.sin(angle) * electricEffect.segmentOriginTranslationMagnitude_1e;
      angle += angleStep;
    }

    //LAB_80104bcc
  }

  @Method(0x80104becL)
  public static void FUN_80104bec(final EffectManagerData6c<EffectManagerParams.ElectricityType> manager, final ElectricityEffect38 electricEffect, final LightningBoltEffect14 bolt, final int a3) {
    final float angleStep = MathHelper.psxDegToRad(manager.params_10._28 << 8 >> 8);
    float angle = bolt.angle_02;

    //LAB_80104c34
    for(int i = 0; i < electricEffect.boltSegmentCount_28; i++) {
      final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[i];
      segment.origin_00.z += MathHelper.sin(angle) * electricEffect.segmentOriginTranslationMagnitude_1e;
      angle += angleStep;
    }

    //LAB_80104c7c
  }

  @Method(0x80104c9cL)
  public static void FUN_80104c9c(final EffectManagerData6c<EffectManagerParams.ElectricityType> manager, final ElectricityEffect38 electricEffect, final LightningBoltEffect14 bolt, final int a3) {
    int translationMagnitudeModifier = 0;
    int segmentIndex = (electricEffect.boltSegmentCount_28 - 2) / 2;
    final int angle0 = (manager.params_10._28 & 0xff) << 4;
    final int angle1 = (manager.params_10._28 & 0xff00) >>> 4;
    final int angle2 = manager.params_10._28 >>> 12 & 0xff0;

    //LAB_80104d24
    for(int i = 1; i < electricEffect.boltSegmentCount_28; i++) {
      final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[i];
      translationMagnitudeModifier = translationMagnitudeModifier + segmentIndex;
      final int translationMagnitude = translationMagnitudeModifier * electricEffect.segmentOriginTranslationMagnitude_1e;
      final int x = translationMagnitude * (rsin(angle1) + rcos(angle2)) >> 12;
      final int y = translationMagnitude * (rcos(angle0) + rcos(angle2)) >> 12;
      final int z = translationMagnitude * (rcos(angle1) + rsin(angle0)) >> 12;
      segment.origin_00.add(x, y, z);
      segmentIndex--;
    }
  }

  @Method(0x80104e40L)
  public static void FUN_80104e40(final EffectManagerData6c<EffectManagerParams.ElectricityType> manager, final ElectricityEffect38 electricEffect, final LightningBoltEffect14 bolt, final int a3) {
    final int translationMagnitude = electricEffect.segmentOriginTranslationMagnitude_1e / electricEffect.boltCount_00 & 0xffff;
    final int angle = 0x1000 / electricEffect.boltCount_00 * a3;

    //LAB_80104ec4
    short originTranslationX = 0;
    short originTranslationY = 0;
    for(int i = 1; i < electricEffect.boltSegmentCount_28; i++) {
      final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[i];
      segment.origin_00.x += originTranslationX;
      segment.origin_00.z += originTranslationY;
      originTranslationX += rcos(angle) * translationMagnitude >> 12;
      originTranslationY += rsin(angle) * translationMagnitude >> 12;
    }
  }

  @Method(0x80104f70L)
  public static void FUN_80104f70(final EffectManagerData6c<EffectManagerParams.ElectricityType> manager, final ElectricityEffect38 effect, final LightningBoltEffect14 bolt, final int a3) {
    final int angleStep = 0x1000 / (effect.boltSegmentCount_28 - 1);
    int angle = 0;

    //LAB_80104fb8
    for(int i = 0; i < effect.boltSegmentCount_28; i++) {
      final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[i];
      segment.origin_00.x += rsin(angle) * effect.segmentOriginTranslationMagnitude_1e >> 12;
      segment.origin_00.z += rcos(angle) * effect.segmentOriginTranslationMagnitude_1e >> 12;
      segment.origin_00.y = 0.0f;
      angle += angleStep;
    }
  }

  @Method(0x80105050L)
  public static void FUN_80105050(final EffectManagerData6c<EffectManagerParams.ElectricityType> manager, final ElectricityEffect38 electricEffect, final LightningBoltEffect14 bolt, final int boltAngleModifier) {
    final int segmentCount = electricEffect.boltSegmentCount_28;
    final int angleStep = 0x800 / (segmentCount - 1);
    final int boltAngleZ = 0x1000 / electricEffect.boltCount_00 * boltAngleModifier;
    int angle = 0;

    //LAB_801050c0
    for(int i = 0; i < segmentCount; i++) {
      final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[i];
      segment.origin_00.x += rcos(angle) * electricEffect.segmentOriginTranslationMagnitude_1e >> 12;
      segment.origin_00.y = (rsin(angle) * rsin(angle) >> 12) * electricEffect.segmentOriginTranslationMagnitude_1e >> 12;
      segment.origin_00.z += (rsin(angle) * rcos(boltAngleZ) >> 12) * electricEffect.segmentOriginTranslationMagnitude_1e >> 12;
      angle += angleStep;
    }
  }

  @Method(0x801051acL)
  public static void FUN_801051ac(final EffectManagerData6c<EffectManagerParams.ElectricityType> manager, final ElectricityEffect38 electricEffect, final LightningBoltEffect14 bolt, final int a3) {
    final int segmentCount = electricEffect.boltSegmentCount_28;
    final float angleStepXZ = MathHelper.TWO_PI / (segmentCount - 1);
    final float angleStepY = MathHelper.psxDegToRad(manager.params_10._28 << 8 >> 8);
    float angleY = bolt.angle_02;
    float angleXZ = 0;

    //LAB_80105210
    for(int i = 0; i < segmentCount; i++) {
      final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[i];
      segment.origin_00.x += MathHelper.sin(angleXZ) * electricEffect.segmentOriginTranslationMagnitude_1e;
      segment.origin_00.y = MathHelper.sin(angleY) * electricEffect.segmentOriginTranslationMagnitude_1e / 4;
      segment.origin_00.z += MathHelper.cos(angleXZ) * electricEffect.segmentOriginTranslationMagnitude_1e;
      angleXZ = angleXZ + angleStepXZ;
      angleY = angleY + angleStepY;
    }
  }

  @ScriptDescription("Allocates an electricity effect")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "parentIndex", description = "The parent index (or -1 for none)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "boltAngleRangeCutoff", description = "The maximum angle for a bolt")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "boltCount", description = "The number of bolts")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "maxSegmentLength", description = "The maximum length of a segment")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "boltAngleStep", description = "The angle increment per frame")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flags", description = "Multiple packed values")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "The electricity type")
  @Method(0x801052dcL)
  public static FlowControl scriptAllocateElectricityEffect(final RunningScript<? extends BattleObject> script) {
    final int effectFlag = script.params_20[6].get();
    final int callbackIndex = script.params_20[7].get();
    final int boltCount = script.params_20[3].get();
    final int boltSegmentCount = effectFlag >> 8 & 0xff;

    final ScriptState<EffectManagerData6c<EffectManagerParams.ElectricityType>> state = allocateEffectManager(
      "ElectricityEffect38",
      script.scriptState_04,
      null,
      electricityEffectRenderers_80119f14[callbackIndex],
      null,
      new ElectricityEffect38(boltCount, boltSegmentCount),
      new EffectManagerParams.ElectricityType()
    );

    final EffectManagerData6c<EffectManagerParams.ElectricityType> manager = state.innerStruct_00;
    final ElectricityEffect38 electricEffect = (ElectricityEffect38)manager.effect_44;
    electricEffect.currentColourFadeStep_04 = 0;
    electricEffect.scriptIndex_08 = script.params_20[1].get();
    electricEffect.numColourFadeSteps_0c = effectFlag >> 16 & 0xff;
    electricEffect.boltAngleStep_10 = MathHelper.psxDegToRad(script.params_20[5].get());
    electricEffect.addSuccessiveSegmentOriginTranslations_14 = (effectFlag >>> 24 & 0x8) == 0;
    electricEffect.type1RendererType_18 = (effectFlag >>> 24 & 0x10) == 0;
    electricEffect.boltAngleRangeCutoff_1c = script.params_20[2].get();
    electricEffect.segmentOriginTranslationMagnitude_1e = script.params_20[4].get();
    electricEffect.callbackIndex_20 = callbackIndex;
    electricEffect.colourShouldFade_22 = (effectFlag >>> 24 & 0x1) == 0;
    electricEffect.fadeSuccessiveSegments_23 = (effectFlag >>> 24 & 0x2) == 0;
    electricEffect.reinitializeNodes_24 = (effectFlag >>> 24 & 0x4) == 0;
    electricEffect.segmentOriginTranslationModifier_26 = effectFlag & 0xff;
    electricEffect.hasMonochromeBase_29 = (effectFlag >>> 24 & 0x20) == 0;
    electricEffect.frameNum_2a = 0;
    electricEffect.callback_2c = electricityEffectCallbacks_80119ee8[callbackIndex];

    if(electricEffect.numColourFadeSteps_0c == 0) {
      electricEffect.numColourFadeSteps_0c = -1;
    }

    //LAB_8010549c
    //LAB_801054b4
    for(int i = 0; i < electricEffect.boltCount_00; i++) {
      final LightningBoltEffect14 boltEffect = electricEffect.bolts_34[i];
      boltEffect.unused_00 = 1;
      boltEffect.angle_02 = seed_800fa754.nextFloat(MathHelper.TWO_PI);
      boltEffect.rotation_04.zero();
      // Ran callback here from method array _80119ebc, which was filled with copies of the same no-op method FUN_801052d4
      initializeElectricityNodes(manager, electricEffect, boltEffect);
    }

    //LAB_80105590
    manager.params_10.flags_00 |= 0x5000_0000;
    manager.params_10.colour_1c.set(0, 0, 0xff);
    manager.params_10._28 = 0x100;
    manager.params_10.sizeDivisor_30 = 3;

    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Calculates the end of an electricity bolt segment")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The electricity effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "boltIndex", description = "The bolt index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "segmentIndex", description = "The segment index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The X position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Y position")
  @Method(0x80105604L)
  public static FlowControl scriptGetBoltSegmentEnd(final RunningScript<?> script) {
    final EffectManagerData6c<EffectManagerParams.ElectricityType> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.classFor(EffectManagerParams.ElectricityType.class));
    final LightningBoltEffect14 bolt = ((ElectricityEffect38)manager.effect_44).bolts_34[script.params_20[1].get()];
    final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[script.params_20[2].get()];

    final Vector3f sp0x20 = new Vector3f();
    getRelativeOffset(manager, bolt.rotation_04, segment.origin_00, sp0x20);
    script.params_20[3].set(Math.round(sp0x20.x));
    script.params_20[4].set(Math.round(sp0x20.y));
    script.params_20[5].set(Math.round(sp0x20.z));
    return FlowControl.CONTINUE;
  }

  @Method(0x80105704L)
  public static void renderThunderArrowEffect(final ScriptState<ThunderArrowEffect1c> state, final ThunderArrowEffect1c data) {
    final Vector2f[] sp0x18 = new Vector2f[4];
    Arrays.setAll(sp0x18, n -> new Vector2f());

    //LAB_8010575c
    final Translucency translucency = Translucency.of(data._10 >>> 28 & 3);

    for(int s7 = 0; s7 < data.count_00; s7++) {
      //LAB_8010577c
      for(int s6 = 0; s6 < data.count_0c - 1; s6++) {
        final ThunderArrowEffectBolt1e s4_1 = data._18[s7][s6];
        final ThunderArrowEffectBolt1e s4_2 = data._18[s7][s6 + 1];

        sp0x18[0].x = s4_2.x_00 - s4_2.size_1c;
        sp0x18[0].y = s4_2.y_02;
        sp0x18[1].x = s4_2.x_00 + 1.0f;
        sp0x18[1].y = s4_2.y_02;
        sp0x18[2].x = s4_1.x_00 - s4_1.size_1c;
        sp0x18[2].y = s4_1.y_02;
        sp0x18[3].x = s4_1.x_00 + 1.0f;
        sp0x18[3].y = s4_1.y_02;
        renderSegmentGradient(s4_1.colour_0a, s4_2.colour_0a, sp0x18, data.z_14, data._08, translucency);
        sp0x18[0].x = s4_2.x_00 - s4_2.size_1c / 3.0f;
        sp0x18[2].x = s4_1.x_00 - s4_1.size_1c / 3.0f;
        renderSegmentGradient(s4_1.colour_04, s4_2.colour_04, sp0x18, data.z_14, data._08, translucency);
        sp0x18[0].x = s4_2.x_00 + s4_2.size_1c;
        sp0x18[1].x = s4_2.x_00;
        sp0x18[2].x = s4_1.x_00 + s4_1.size_1c;
        sp0x18[3].x = s4_1.x_00;
        renderSegmentGradient(s4_1.colour_0a, s4_2.colour_0a, sp0x18, data.z_14, data._08, translucency);
        sp0x18[0].x = s4_2.x_00 + s4_2.size_1c / 3.0f;
        sp0x18[2].x = s4_1.x_00 + s4_1.size_1c / 3.0f;
        renderSegmentGradient(s4_1.colour_04, s4_2.colour_04, sp0x18, data.z_14, data._08, translucency);
      }
    }

    //LAB_801059c8
  }

  @Method(0x80105aa0L)
  public static void tickThunderArrowEffect(final ScriptState<ThunderArrowEffect1c> state, final ThunderArrowEffect1c data) {
    data._04--;

    if(data._04 <= 0) {
      state.deallocateWithChildren();
    } else {
      //LAB_80105ad8
      //LAB_80105aec
      for(int a3 = 0; a3 < data.count_00; a3++) {
        //LAB_80105b00
        for(int a2 = 0; a2 < data.count_0c; a2++) {
          final ThunderArrowEffectBolt1e a0 = data._18[a3][a2];
          a0.colour_04.sub(a0.svec_10);
          a0.colour_0a.sub(a0.svec_16);
        }
      }
    }
  }

  @ScriptDescription("Allocates a thunder arrow effect extension to an electricity effect")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The electricity effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @Method(0x80105c38L)
  public static FlowControl scriptAllocateThunderArrowEffect(final RunningScript<?> script) {
    final Vector2f ref = new Vector2f();

    final int s3 = script.params_20[1].get();
    final EffectManagerData6c<EffectManagerParams.ElectricityType> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.classFor(EffectManagerParams.ElectricityType.class));
    final ElectricityEffect38 electricityEffect = (ElectricityEffect38)manager.effect_44;
    final ScriptState<ThunderArrowEffect1c> state = SCRIPTS.allocateScriptState("ThunderArrowEffect1c", new ThunderArrowEffect1c());
    state.loadScriptFile(doNothingScript_8004f650);
    state.setTicker(SEffe::tickThunderArrowEffect);
    state.setRenderer(SEffe::renderThunderArrowEffect);
    final ThunderArrowEffect1c effect = state.innerStruct_00;
    effect.count_00 = electricityEffect.boltCount_00;
    effect._04 = s3;
    effect.count_0c = electricityEffect.boltSegmentCount_28;
    effect._10 = manager.params_10.flags_00;
    effect._18 = new ThunderArrowEffectBolt1e[effect.count_00][];

    //LAB_80105d64
    for(int s7 = 0; s7 < effect.count_00; s7++) {
      final LightningBoltEffect14 struct14 = electricityEffect.bolts_34[s7];
      effect._18[s7] = new ThunderArrowEffectBolt1e[effect.count_0c];

      //LAB_80105da0
      for(int s4 = 0; s4 < effect.count_0c; s4++) {
        final ThunderArrowEffectBolt1e struct1e = new ThunderArrowEffectBolt1e();
        effect._18[s7][s4] = struct1e;

        final LightningBoltEffectSegment30 s0 = struct14.boltSegments_10[s4];
        struct1e.size_1c = (byte)(s0.scaleMultiplier_28 * manager.params_10.scale_16.x);

        final float z = FUN_800cfb94(manager, struct14.rotation_04, s0.origin_00, ref) / 4.0f;
        effect.z_14 = z;
        if(z < 0x140) {
          effect._04 = 0;
        } else {
          //LAB_80105e18
          if(effect.z_14 >= 0xffe) {
            effect.z_14 = 0xffe;
          }

          //LAB_80105e30
          effect._08 = manager.params_10.z_22;
          struct1e.x_00 = ref.x;
          struct1e.y_02 = ref.y;
          struct1e.colour_04.set(s0.innerColour_10);
          struct1e.colour_0a.set(s0.outerColour_16);
          struct1e.svec_10.set(struct1e.colour_04).div(s3);
          struct1e.svec_16.set(struct1e.colour_0a).div(s3);
        }
      }
    }

    //LAB_80105f64
    return FlowControl.CONTINUE;
  }

  /**
   * Used to calculate unused vectors on AdditionOverlaysEffect. Gets translation of attacker or target at start of
   * addition, and for some reason does a meaningless 0 rotation.
   */
  @Method(0x80105f98L)
  public static void getBentTranslation(final int scriptIndex, final Vector3f out, final long coordType) {
    final MV transformationMatrix = new MV();

    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00;

    final GsCOORDINATE2 coord2;
    if(coordType == 0) {
      coord2 = bent.model_148.modelParts_00[1].coord2_04;
    } else {
      //LAB_80105fe4
      coord2 = bent.model_148.coord2_14;
    }

    //LAB_80105fec
    GsGetLw(coord2, transformationMatrix);
    // Does nothing? Changed line below to set //ApplyMatrixLV(transformationMatrix, zeroVec, out);
    out.set(transformationMatrix.transfer);
  }

  /** Runs callbacks to render correct button icon effects during addition */
  @Method(0x80106050L)
  public static void renderAdditionButton(final int frames, final boolean isCounter) {
    final int offset = isCounter ? 1 : 0;
    if(Math.abs(frames) >= 2) {  // Button up position
      renderButtonPressHudElement1(0x24, 119, 43, Translucency.B_PLUS_F, 0x80);
      renderButtonPressHudElement1(additionButtonRenderCallbackIndices_800fb7bc[offset], 115, 48, Translucency.B_PLUS_F, 0x80);
    } else {  // Button down position
      //LAB_80106114
      renderButtonPressHudElement1(0x24, 119, 51, Translucency.B_PLUS_F, 0x80);
      renderButtonPressHudElement1(additionButtonRenderCallbackIndices_800fb7bc[offset + 2], 115, 48, Translucency.B_PLUS_F, 0x80);
      renderButtonPressHudElement1(0x25, 115, 50, Translucency.B_PLUS_F, 0x80);
    }
  }

  public static void renderButtonPressHudElement1(final int type, final int x, final int y, final Translucency translucency, final int brightness) {
    battleUiParts.queueButton(type, x, y, translucency, brightness, 1.0f, 1.0f);
  }

  /**
   * Selects where to get hit property from based on auto-complete type. 1 and 3 use a mysterious global 2-hit array,
   * which may be unused testing code.
   */
  @Method(0x801061bcL)
  public static int getHitProperty(final int charSlot, final int hitNum, final int hitPropertyIndex, final int autoCompleteType) {
    //LAB_80106264
    final int hitPropertyValue;
    if(autoCompleteType == 1 || autoCompleteType == 3) {
      //LAB_80106274
      hitPropertyValue = staticTestAdditionHitProperties_800fb7c0[hitNum].get(hitPropertyIndex);
    } else {
      //LAB_8010628c
      hitPropertyValue = battlePreloadedEntities_1f8003f4.getHitProperty(charSlot, hitNum, hitPropertyIndex) & 0xff;
    }

    //LAB_80106298
    return hitPropertyValue;
  }

  @Method(0x801062a8L)
  public static void initializeAdditionOverlaysEffect(final int attackerScriptIndex, final int targetScriptIndex, final AdditionOverlaysEffect44 effect, final int autoCompleteType) {
    final BattleEntity27c s5 = (BattleEntity27c)scriptStatePtrArr_800bc1c0[attackerScriptIndex].innerStruct_00;

    effect.reticleBorderShadow = new QuadBuilder("Reticle background")
      .translucency(Translucency.B_MINUS_F)
      .monochrome(0, 0.0f)
      .monochrome(1, 0.0f)
      .monochrome(2, 1.0f)
      .monochrome(3, 1.0f)
      .pos(-1.0f, -0.5f, 0.0f)
      .size(1.0f, 1.0f)
      .build();

    //LAB_8010633c
    int hitNum;
    for(hitNum = 0; hitNum < 8; hitNum++) {
      // Number of hits calculated by counting to first hit with 0 total frames
      if((getHitProperty(s5.charSlot_276, hitNum, 1, autoCompleteType) & 0xff) == 0) {
        break;
      }
    }

    //LAB_80106374
    final int hitCount = hitNum - 1;
    effect.count_30 = hitCount;
    effect.attackerScriptIndex_00 = attackerScriptIndex;
    effect.targetScriptIndex_04 = targetScriptIndex;
    effect.currentFrame_34 = 0;
    effect.pauseTickerAndRenderer_31 = 0;
    effect.additionComplete_32 = 0;
    effect.numFramesToRenderCenterSquare_38 = 0;
    effect.lastCompletedHit_39 = 0;
    effect.autoCompleteType_3a = autoCompleteType;
    final AdditionOverlaysHit20[] hitArray = new AdditionOverlaysHit20[hitCount];
    Arrays.setAll(hitArray, AdditionOverlaysHit20::new);
    effect.hitOverlays_40 = hitArray;
    int overlayDisplayDelay = getHitProperty(s5.charSlot_276, 0, 15, autoCompleteType) & 0xff;
    effect.unused_36 = overlayDisplayDelay;

    //LAB_801063f0
    for(hitNum = 0; hitNum < effect.count_30; hitNum++) {
      final AdditionOverlaysHit20 hitOverlay = hitArray[hitNum];
      hitOverlay.unused_00 = 1;
      hitOverlay.hitSuccessful_01 = false;
      hitOverlay.shadowColour_08 = (short)0;
      hitOverlay.frameSuccessLowerBound_10 = (short)(overlayDisplayDelay + 2);
      hitOverlay.borderColoursArrayIndex_02 = 3;
      hitOverlay.isCounter_1c = false;
      additionHitCompletionState_8011a014[hitNum] = 0;
      int hitProperty = getHitProperty(s5.charSlot_276, hitNum, 1, autoCompleteType) & 0xff;
      overlayDisplayDelay += hitProperty; // Display delay for each hit
      hitOverlay.totalHitFrames_0a = (short)hitProperty;
      hitProperty = getHitProperty(s5.charSlot_276, hitNum, 2, autoCompleteType) & 0xff;
      hitOverlay.frameBeginDisplay_0c = (short)hitProperty;
      hitProperty = getHitProperty(s5.charSlot_276, hitNum, 3, autoCompleteType) & 0xff;
      hitOverlay.numSuccessFrames_0e = (short)hitProperty;
      final int successFrameTarget = hitOverlay.frameSuccessLowerBound_10 + hitOverlay.frameBeginDisplay_0c;
      hitOverlay.frameSuccessLowerBound_10 = (short)(successFrameTarget - hitOverlay.numSuccessFrames_0e / 2 + 1);
      hitOverlay.frameSuccessUpperBound_12 = (short)(successFrameTarget + hitOverlay.numSuccessFrames_0e - hitOverlay.numSuccessFrames_0e / 2);

      final AdditionOverlaysBorder0e[] borderArray = hitOverlay.borderArray_18;

      //LAB_8010652c
      if(Config.changeAdditionOverlayRgb()) {
        final int counterRgb = Config.getCounterOverlayRgb();
        final int additionRgb = Config.getAdditionOverlayRgb();
        additionBorderColours_800fb7f0[6] = counterRgb & 0xff;
        additionBorderColours_800fb7f0[7] = counterRgb >> 8 & 0xff;
        additionBorderColours_800fb7f0[8] = counterRgb >> 16 & 0xff;
        additionBorderColours_800fb7f0[9] = additionRgb & 0xff;
        additionBorderColours_800fb7f0[10] = additionRgb >> 8 & 0xff;
        additionBorderColours_800fb7f0[11] = additionRgb >> 16 & 0xff;
      }

      int val = 16;
      for(int borderNum = 0; borderNum < 17; borderNum++) {
        final AdditionOverlaysBorder0e borderOverlay = borderArray[borderNum];
        borderOverlay.size_08 = (18 - val) * 10;
        borderOverlay.isVisible_00 = true;
        //LAB_8010656c
        //LAB_80106574
        borderOverlay.angleModifier_02 = Math.toRadians((16 - val) * 11.25f);
        borderOverlay.countFramesVisible_0c = 5;
        borderOverlay.sideEffects_0d = 0;
        borderOverlay.framesUntilRender_0a = (short)(hitOverlay.frameSuccessLowerBound_10 + (hitOverlay.numSuccessFrames_0e - 1) / 2 + val - 17);
        borderOverlay.r_04 = additionBorderColours_800fb7f0[hitOverlay.borderColoursArrayIndex_02 * 3];
        borderOverlay.g_05 = additionBorderColours_800fb7f0[hitOverlay.borderColoursArrayIndex_02 * 3 + 1];
        borderOverlay.b_06 = additionBorderColours_800fb7f0[hitOverlay.borderColoursArrayIndex_02 * 3 + 2];

        val--;
      }

      //LAB_80106634
      val = 0;
      for(int borderNum = 16; borderNum >= 14; borderNum--) {
        final AdditionOverlaysBorder0e borderOverlay = borderArray[borderNum];
        borderOverlay.size_08 = 20 - val * 2;
        borderOverlay.angleModifier_02 = 0.0f;
        borderOverlay.countFramesVisible_0c = 0x11;
        borderOverlay.framesUntilRender_0a = hitOverlay.frameSuccessLowerBound_10 - 17;

        if(val != 0x1L) {
          borderOverlay.r_04 = 0x30;
          borderOverlay.g_05 = 0x30;
          borderOverlay.b_06 = 0x30;
          borderOverlay.sideEffects_0d = 1;
        } else {
          //LAB_80106680
          borderOverlay.sideEffects_0d = -1;
        }
        //LAB_80106684
        val++;
      }
    }

    // These fields are not used for anything
    //LAB_801066c8
    getBentTranslation(effect.attackerScriptIndex_00, effect.attackerStartingPosition_10, 0);

    final Vector3f targetStartingPosition = new Vector3f();
    getBentTranslation(effect.targetScriptIndex_04, targetStartingPosition, 1);

    final int firstHitSuccessLowerBound = effect.hitOverlays_40[0].frameSuccessLowerBound_10;
    effect.distancePerFrame_20.x = (targetStartingPosition.x - effect.attackerStartingPosition_10.x) / firstHitSuccessLowerBound;
    effect.distancePerFrame_20.y = (targetStartingPosition.y - effect.attackerStartingPosition_10.y) / firstHitSuccessLowerBound;
    effect.distancePerFrame_20.z = (targetStartingPosition.z - effect.attackerStartingPosition_10.z) / firstHitSuccessLowerBound;
  }

  @Method(0x80106774L)
  public static long fadeAdditionBorders(final AdditionOverlaysBorder0e square, final int fadeStep) {
    int numberOfNegativeComponents = 0;
    int newColour = square.r_04 - fadeStep;
    final int newR;
    if(newColour > 0) {
      newR = newColour;
    } else {
      newR = 0;
      numberOfNegativeComponents++;
    }

    //LAB_801067b0
    newColour = square.g_05 - fadeStep;
    final int newG;
    if(newColour > 0) {
      newG = newColour;
    } else {
      newG = 0;
      numberOfNegativeComponents++;
    }

    //LAB_801067c4
    newColour = square.b_06 - fadeStep;
    final int newB;
    if(newColour > 0) {
      newB = newColour;
    } else {
      newB = 0;
      numberOfNegativeComponents++;
    }

    //LAB_801067d8
    square.r_04 = newR;
    square.g_05 = newG;
    square.b_06 = newB;
    return numberOfNegativeComponents;
  }

  @Method(0x80106808L)
  public static void renderAdditionCentreSolidSquare(final AdditionOverlaysEffect44 effect, final AdditionOverlaysHit20 hitOverlay, final int completionState, final EffectManagerData6c<?> manager) {
    if(manager.params_10.flags_00 >= 0) {
      final AdditionOverlaysBorder0e[] targetBorderArray = hitOverlay.borderArray_18;

      //LAB_8010685c
      for(int targetBorderNum = 0; targetBorderNum < 2; targetBorderNum++) {
        final int squareSize = targetBorderArray[16].size_08 - targetBorderNum * 8;

        effect.transforms.scaling(squareSize * 2.0f, squareSize * 2.0f, 1.0f);
        effect.transforms.transfer.set(GPU.getOffsetX() + squareSize, GPU.getOffsetY() + squareSize + 30.0f, 120.0f);
        final RenderEngine.QueuedModel<?> model = RENDERER.queueOrthoModel(RENDERER.centredQuadBPlusF, effect.transforms);

        if(completionState == 1) {  // Success
          model.monochrome(1.0f);
        } else if(completionState != -2) {  // Too early
          model.monochrome(0x30 / 255.0f);
        } else if(hitOverlay.isCounter_1c) {  // Counter-attack too late
          if(Config.changeAdditionOverlayRgb()) {
            model.colour(additionBorderColours_800fb7f0[6] / 255.0f, additionBorderColours_800fb7f0[7] / 255.0f, (additionBorderColours_800fb7f0[8] * 8 - 2) * 8 / 255.0f);
          } else {
            model.colour(targetBorderArray[15].r_04 * 3 / 255.0f, targetBorderArray[15].g_05 / 255.0f, (targetBorderArray[15].b_06 - 1) * 8 / 255.0f);
          }
        } else {  // Too late
          model.colour(targetBorderArray[15].r_04 / 255.0f, targetBorderArray[15].g_05 / 255.0f, targetBorderArray[15].b_06 / 255.0f);
        }
      }
    }

    //LAB_80106a4c
  }

  /** Renders the shadow on the inside of the innermost rotating border. */
  @Method(0x80106ac4L)
  public static void renderAdditionBorderShadow(final AdditionOverlaysEffect44 effect, final AdditionOverlaysHit20 hitOverlay, final float angle, final int borderSize) {
    // Would you believe me if I said I knew what I was doing when I wrote any of this?
    final int offset = borderSize - 1;
    final float sin0 = MathHelper.sin(angle);
    final float cos0 = MathHelper.cosFromSin(sin0, angle);
    final float x0 = cos0 * offset / 2.0f;
    final float y0 = sin0 * offset / 2.0f;
    final int colour = hitOverlay.shadowColour_08 * 4;

    effect.transforms.transfer.set(x0 + GPU.getOffsetX(), y0 + GPU.getOffsetY() + 30.0f, 124.0f);
    effect.transforms
      .scaling(10.0f, borderSize, 1.0f)
      .rotateLocalZ(angle);

    RENDERER.queueOrthoModel(effect.reticleBorderShadow, effect.transforms)
      .monochrome(colour / 255.0f);
  }

  @Method(0x80106cccL)
  public static void renderAdditionBorders(final AdditionOverlaysEffect44 effect, final int hitNum, final AdditionOverlaysHit20[] hitArray) {
    final AdditionOverlaysBorder0e[] borderArray = hitArray[hitNum].borderArray_18;
    final byte currentHitCompletionState = additionHitCompletionState_8011a014[hitNum];

    //LAB_80106d18
    for(int borderNum = 0; borderNum < 17; borderNum++) {
      final AdditionOverlaysBorder0e borderOverlay = borderArray[borderNum];

      if(borderOverlay.isVisible_00 && borderOverlay.framesUntilRender_0a <= 0) {
        effect.transforms
          .scaling(borderOverlay.size_08, borderOverlay.size_08, 1.0f)
          .rotateZ(borderOverlay.angleModifier_02);
        effect.transforms.transfer.set(GPU.getOffsetX(), GPU.getOffsetY() + 30.0f, 120.0f);

        final RenderEngine.QueuedModel<?> model;

        // Set translucent if button press is failure and border sideEffects_0d not innermost rotating border or target (15)
        if((borderOverlay.sideEffects_0d == 0 || borderOverlay.sideEffects_0d == -1) && currentHitCompletionState >= 0) {
          model = RENDERER.queueOrthoModel(RENDERER.lineBox, effect.transforms);
        } else {
          model = RENDERER.queueOrthoModel(RENDERER.lineBoxBPlusF, effect.transforms);
        }

        if(hitArray[hitNum].isCounter_1c && borderNum != 16) {
          if(Config.changeAdditionOverlayRgb()) {
            final int rgb = Config.getCounterOverlayRgb();

            // Hack to get around lack of separate counterattack color field until full dememulation
            final float rFactor = borderArray[borderNum].r_04 / (float)additionBorderColours_800fb7f0[9];
            final float gFactor = borderArray[borderNum].g_05 / (float)additionBorderColours_800fb7f0[10];
            final float bFactor = borderArray[borderNum].b_06 / (float)additionBorderColours_800fb7f0[11];

            model.colour((rgb & 0xff) * rFactor / 255.0f, (rgb >> 8 & 0xff) * gFactor / 255.0f, (rgb >> 16 & 0xff) * bFactor / 255.0f);
          } else {
            model.colour(borderOverlay.r_04 * 3 / 255.0f, borderOverlay.g_05 / 255.0f, (borderOverlay.b_06 + 1) / 8.0f / 255.0f);
          }
        } else {
          //LAB_80106e58
          model.colour(borderOverlay.r_04 / 255.0f, borderOverlay.g_05 / 255.0f, borderOverlay.b_06 / 255.0f);
        }

        // Renders rotating shadow on innermost rotating border
        if(borderOverlay.sideEffects_0d == 0) {
          renderAdditionBorderShadow(effect, hitArray[hitNum], borderOverlay.angleModifier_02, borderOverlay.size_08 * 2);
          renderAdditionBorderShadow(effect, hitArray[hitNum], borderOverlay.angleModifier_02 + MathHelper.HALF_PI, borderOverlay.size_08 * 2);
          renderAdditionBorderShadow(effect, hitArray[hitNum], borderOverlay.angleModifier_02 + MathHelper.PI, borderOverlay.size_08 * 2);
          renderAdditionBorderShadow(effect, hitArray[hitNum], borderOverlay.angleModifier_02 + MathHelper.PI + MathHelper.HALF_PI, borderOverlay.size_08 * 2);
        }
      }
      //LAB_80106fac
    }
  }

  @Method(0x80107088L)
  public static int tickBorderDisplay(final int a0, final int hitNum, final AdditionOverlaysEffect44 effect, final AdditionOverlaysHit20[] hitArray) {
    // Darken shadow color of innermost border of current hit
    final AdditionOverlaysHit20 hitOverlay = hitArray[hitNum];
    if(effect.currentFrame_34 >= hitOverlay.frameSuccessLowerBound_10 - 0x11) {
      hitOverlay.shadowColour_08 += 1;

      if(hitOverlay.shadowColour_08 >= 0xe) {
        hitOverlay.shadowColour_08 = 0xd;
      }
    }

    //LAB_801070ec
    final byte currentHitCompletionState = additionHitCompletionState_8011a014[hitNum];
    final AdditionOverlaysBorder0e[] borderArray = hitOverlay.borderArray_18;
    int isRendered = 0;

    //LAB_80107104
    for(int borderNum = 0; borderNum < 17; borderNum++) {
      final AdditionOverlaysBorder0e borderOverlay = borderArray[borderNum];

      // Fade shadow if hit failed, set invisible if border is within 0x20 of fully faded
      if(currentHitCompletionState < 0) {
        hitOverlay.shadowColour_08 -= 3;

        if(hitOverlay.shadowColour_08 < 0) {
          hitOverlay.shadowColour_08 = 0;
        }

        //LAB_80107134
        if(fadeAdditionBorders(borderOverlay, 0x20) == 0x3L) {
          borderOverlay.isVisible_00 = false;
        }
      }

      //LAB_80107150
      if(borderOverlay.isVisible_00) {
        if(borderOverlay.framesUntilRender_0a > 0) {
          borderOverlay.framesUntilRender_0a--;
        } else {
          //LAB_80107178
          if(borderOverlay.sideEffects_0d != -1) {
            borderOverlay.sideEffects_0d++;
          }

          //LAB_80107190
          borderOverlay.countFramesVisible_0c--;
          if(borderOverlay.countFramesVisible_0c == 0) {
            borderOverlay.isVisible_00 = false;
          }

          //LAB_801071b0
          // Fade rotating borders only
          if(borderNum < 14) {
            fadeAdditionBorders(borderOverlay, 0x4e);
          }

          isRendered = 1;
        }
      }
      //LAB_801071c0
    }

    return isRendered;
  }

  /** If a hit is failed, flag all subsequent hits as failed as well */
  @Method(0x801071fcL)
  public static void propagateFailedAdditionHitFlag(final AdditionOverlaysEffect44 effect, final AdditionOverlaysHit20[] hitArray, int hitNum) {
    final byte currentHitCompletionState = additionHitCompletionState_8011a014[hitNum];
    effect.additionComplete_32 = 1;

    //LAB_80107234
    hitNum += 1;
    for(; hitNum < effect.count_30; hitNum++) {
      final AdditionOverlaysHit20 hitOverlay = hitArray[hitNum];
      hitOverlay.frameSuccessUpperBound_12 = -1;
      hitOverlay.frameSuccessLowerBound_10 = -1;
      hitOverlay.numSuccessFrames_0e = 0;
      hitOverlay.frameBeginDisplay_0c = 0;
      additionHitCompletionState_8011a014[hitNum] = currentHitCompletionState;
    }
    //LAB_80107264
  }

  @Method(0x8010726cL)
  public static void renderAdditionOverlaysEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> data) {
    final AdditionOverlaysEffect44 effect = (AdditionOverlaysEffect44)data.effect_44;

    if(effect.pauseTickerAndRenderer_31 != 1) {
      if(data.params_10.flags_00 >= 0) {
        final AdditionOverlaysHit20[] hitArray = effect.hitOverlays_40;

        //LAB_801072c4
        int hitNum;
        for(hitNum = 0; hitNum < effect.count_30; hitNum++) {
          if(CONFIG.getConfig(CoreMod.ADDITION_OVERLAY_CONFIG.get()) == AdditionOverlayMode.FULL) {
            renderAdditionBorders((AdditionOverlaysEffect44)data.effect_44, hitNum, hitArray);
          }
        }

        //LAB_801072f4
        //LAB_8010730c
        for(hitNum = 0; hitNum < effect.count_30; hitNum++) {
          if(additionHitCompletionState_8011a014[hitNum] == 0) {
            break;
          }
        }

        //LAB_80107330
        if(hitNum < effect.count_30) {
          final AdditionOverlaysHit20 hitOverlay = hitArray[hitNum];
          if(CONFIG.getConfig(CoreMod.ADDITION_OVERLAY_CONFIG.get()) == AdditionOverlayMode.FULL) {
            renderAdditionButton(hitOverlay.frameSuccessLowerBound_10 + (hitOverlay.frameSuccessUpperBound_12 - hitOverlay.frameSuccessLowerBound_10) / 2 - effect.currentFrame_34 - 1, hitOverlay.isCounter_1c);
          }

          final byte currentFrame = (byte)effect.currentFrame_34;
          if(currentFrame >= hitOverlay.frameSuccessLowerBound_10 && currentFrame <= hitOverlay.frameSuccessUpperBound_12) {
            if(CONFIG.getConfig(CoreMod.ADDITION_OVERLAY_CONFIG.get()) != AdditionOverlayMode.OFF) {
              renderAdditionCentreSolidSquare(effect, hitOverlay, -2, data);
            }
          }
        }
      }
    }
    //LAB_801073b4
  }

  @Method(0x801073d4L)
  public static void tickAdditionOverlaysEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> data) {
    final AdditionOverlaysEffect44 effect = (AdditionOverlaysEffect44)data.effect_44;

    if(effect.pauseTickerAndRenderer_31 == 0) {
      final AdditionOverlaysHit20[] hitArray = effect.hitOverlays_40;
      effect.currentFrame_34++;

      //LAB_80107440
      int hitNum;
      int hitFailed = 1;
      for(hitNum = 0; hitNum < effect.count_30; hitNum++) {
        // Catch too late failure when no button pressed
        if(effect.currentFrame_34 == hitArray[hitNum].frameSuccessUpperBound_12 + 1) {
          if(additionHitCompletionState_8011a014[hitNum] == 0) {
            additionHitCompletionState_8011a014[hitNum] = -2;
            propagateFailedAdditionHitFlag(effect, hitArray, hitNum);

            //LAB_80107478
          }
        } else if(additionHitCompletionState_8011a014[hitNum] == 0) {
          hitFailed = 0;
        }
        //LAB_8010748c
      }

      //LAB_801074a8
      if(hitFailed != 0) {
        propagateFailedAdditionHitFlag(effect, hitArray, hitNum);
      }

      //LAB_801074bc
      int numberBordersRendering = 0;

      //LAB_801074d0
      for(hitNum = 0; hitNum < effect.count_30; hitNum++) {
        numberBordersRendering += tickBorderDisplay(hitArray[hitNum].borderColoursArrayIndex_02, hitNum, effect, hitArray);
      }

      //LAB_80107500
      // If addition is complete and there are no more visible overlays to render, deallocate effect
      if(numberBordersRendering == 0 && effect.additionComplete_32 != 0) {
        additionOverlayActive_80119f41 = 0;

        //LAB_8010752c
        for(hitNum = 0; hitNum < effect.count_30; hitNum++) {
          hitArray[hitNum].borderArray_18 = null;
        }

        //LAB_80107554
        state.deallocateWithChildren();
      } else {
        //LAB_8010756c
        if(effect.currentFrame_34 >= 9) {
          //LAB_80107598
          for(hitNum = 0; hitNum < effect.count_30; hitNum++) {
            if(additionHitCompletionState_8011a014[hitNum] == 0) {
              break;
            }
          }

          //LAB_801075bc
          if(hitNum < effect.count_30) {
            final AdditionOverlaysHit20 hitOverlay = hitArray[hitNum];

            if(state.storage_44[8] != 0) {
              hitOverlay.isCounter_1c = true;
              state.storage_44[8] = 0;
            }

            //LAB_801075e8
            if(effect.autoCompleteType_3a < 1 || effect.autoCompleteType_3a > 2) {
              //LAB_8010763c
              if(effect.autoCompleteType_3a != 3) {
                final int buttonType;
                if(!hitOverlay.isCounter_1c) {
                  buttonType = 0x20;
                } else {
                  buttonType = 0x40;
                }

                //LAB_80107664
                final int buttonPressed = press_800bee94;

                if((buttonPressed & 0x60) != 0) {
                  additionHitCompletionState_8011a014[hitNum] = -1;

                  if((buttonPressed & buttonType) == 0 || (buttonPressed & ~buttonType) != 0) {
                    //LAB_801076d8
                    //LAB_801076dc
                    additionHitCompletionState_8011a014[hitNum] = -3;
                  } else if(effect.currentFrame_34 >= hitOverlay.frameSuccessLowerBound_10 && effect.currentFrame_34 <= hitOverlay.frameSuccessUpperBound_12) {
                    additionHitCompletionState_8011a014[hitNum] = 1;
                    hitOverlay.hitSuccessful_01 = true;
                  }

                  //LAB_801076f0
                  if(additionHitCompletionState_8011a014[hitNum] < 0) {
                    propagateFailedAdditionHitFlag(effect, hitArray, hitNum);
                  }

                  //LAB_80107718
                  //LAB_8010771c
                  effect.numFramesToRenderCenterSquare_38 = 2;
                  effect.lastCompletedHit_39 = hitNum;
                }
              }
            } else {  // Auto-complete
              if(effect.currentFrame_34 >= hitOverlay.frameSuccessLowerBound_10 && effect.currentFrame_34 <= hitOverlay.frameSuccessUpperBound_12) {
                additionHitCompletionState_8011a014[hitNum] = 1;
                hitOverlay.hitSuccessful_01 = true;

                //LAB_8010771c
                effect.numFramesToRenderCenterSquare_38 = 2;
                effect.lastCompletedHit_39 = hitNum;
              }
            }
          }

          //LAB_80107728
          if(effect.numFramesToRenderCenterSquare_38 != 0) {
            effect.numFramesToRenderCenterSquare_38--;
            if(CONFIG.getConfig(CoreMod.ADDITION_OVERLAY_CONFIG.get()) != AdditionOverlayMode.OFF) {
              renderAdditionCentreSolidSquare(effect, effect.hitOverlays_40[effect.lastCompletedHit_39], additionHitCompletionState_8011a014[effect.lastCompletedHit_39], data);
            }
          }
        }
      }
    }
    //LAB_80107764
  }

  @ScriptDescription("Gets the success or failure of the addition hit")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "unused")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The hit index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "result", description = "0 = not attempted, 1 = success, -1 = too early, -2 = too late, -3 = wrong button")
  @Method(0x801077bcL)
  public static FlowControl scriptGetHitCompletionState(final RunningScript<?> script) {
    script.params_20[2].set(additionHitCompletionState_8011a014[script.params_20[1].get()]);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates an addition overlays effect manager")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attackerIndex", description = "The attacker battle entity index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "targetIndex", description = "The target battle entity index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "autoCompleteType", description = "0 = normal, 2 = Wargod Calling/Ultimate Wargod")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "unused")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @Method(0x801077e8L)
  public static FlowControl scriptAllocateAdditionOverlaysEffect(final RunningScript<? extends BattleObject> script) {
    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager(
      "Addition overlays",
      script.scriptState_04,
      SEffe::tickAdditionOverlaysEffect,
      SEffe::renderAdditionOverlaysEffect,
      (state1, manager) -> ((AdditionOverlaysEffect44)manager.effect_44).reticleBorderShadow.delete(),
      new AdditionOverlaysEffect44()
    );

    initializeAdditionOverlaysEffect(script.params_20[0].get(), script.params_20[1].get(), (AdditionOverlaysEffect44)state.innerStruct_00.effect_44, script.params_20[2].get());
    state.storage_44[8] = 0; // Storage for counterattack state
    script.params_20[4].set(state.index);
    additionOverlayActive_80119f41 = 1;
    return FlowControl.CONTINUE;
  }

  /**
   * Script subfunc related to pausing ticking/rendering of addition overlay during counterattacks. additionContinuationState == 0 occurs when
   * counterattack counter is successful; have not gotten other conditions to trigger
   */
  @ScriptDescription("Pauses ticker/renderer of addition overlays during counterattacks")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "state", description = "0 = counterattack successful, other values unknown")
  @Method(0x801078c0L)
  public static FlowControl scriptAlterAdditionContinuationState(final RunningScript<?> script) {
    final EffectManagerData6c<EffectManagerParams.VoidType> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.classFor(EffectManagerParams.VoidType.class));
    final AdditionOverlaysEffect44 effect = (AdditionOverlaysEffect44)manager.effect_44;
    final int additionContinuationState = script.params_20[1].get();

    if(additionContinuationState == 0) {
      //LAB_80107924
      effect.pauseTickerAndRenderer_31 = effect.pauseTickerAndRenderer_31 < 1 ? 1 : 0;
      return FlowControl.CONTINUE;
      //LAB_80107910
    } else if(additionContinuationState == 2) {
      //LAB_80107984
      //LAB_80107994
      effect.pauseTickerAndRenderer_31 = effect.pauseTickerAndRenderer_31 < 1 ? 2 : 0;
      return FlowControl.CONTINUE;
    }

    //LAB_80107930
    effect.additionComplete_32 = additionContinuationState;

    //LAB_80107954
    final AdditionOverlaysHit20[] hitArray = effect.hitOverlays_40;
    for(int hitNum = 0; hitNum < effect.count_30; hitNum++) {
      hitArray[hitNum].frameSuccessLowerBound_10 = 0;
      hitArray[hitNum].frameSuccessUpperBound_12 = 0;
      additionHitCompletionState_8011a014[hitNum] = -1;
    }

    //LAB_80107998
    //LAB_8010799c
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the addition overlay state")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "0 = addition, 1 = dragoon addition")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "state", description = "The overlay state")
  @Method(0x801079a4L)
  public static FlowControl scriptGetAdditionOverlayActiveStatus(final RunningScript<?> script) {
    if(script.params_20[0].get() == 0) {
      script.params_20[1].set(additionOverlayActive_80119f41);
    } else {
      //LAB_801079d0
      script.params_20[1].set(daddyMeterSpinning_80119f42);
    }

    //LAB_801079e0
    return FlowControl.CONTINUE;
  }

  @Method(0x801079e8L)
  public static void renderDragoonAdditionButtonPressTextures(final DragoonAdditionScriptData1c daddy) {
    final int x0 = daddyHudOffsetX_8011a01c;
    final int y0 = daddyHudOffsetY_8011a020;
    final int x1 = x0 + 16;
    final int y1 = y0 + 70;

    final int buttonHudMetricsIndex;
    // Button arrow placement
    if(getCurrentDragoonAdditionPressNumber(daddy, 0) != 0) {
      renderButtonPressHudElement1(36, x1, y0 + 64, Translucency.B_PLUS_F, 128);
      buttonHudMetricsIndex = 33;
    } else {
      //LAB_80107a80
      if(getCurrentDragoonAdditionPressNumber(daddy, 2) != 0) {
        renderButtonPressHudElement1(36, x1, y0 + 60, Translucency.B_PLUS_F, 128);
        buttonHudMetricsIndex = 33;
      } else {
        //LAB_80107ad4
        renderButtonPressHudElement1(36, x1, y0 + 56, Translucency.B_PLUS_F, 128);
        buttonHudMetricsIndex = 35;
      }
    }

    //LAB_80107b10
    // Button
    renderButtonPressHudElement1(buttonHudMetricsIndex, x0 + 12, y0 + 66, Translucency.B_PLUS_F, 128);

    // Button press red glow
    if(daddy.buttonPressGlowBrightnessFactor_11 != 0) {
      final int brightness = daddy.buttonPressGlowBrightnessFactor_11 * 0x40 - 1;
      renderButtonPressHudElement1(20, x1 - 4, y1 - 4, Translucency.B_PLUS_F, 128);
      battleUiParts.queueDaddyButtonGlow(x1, y1, brightness, (daddy.buttonPressGlowBrightnessFactor_11 * 256 + 0x1904) / (float)0x1000, (daddy.buttonPressGlowBrightnessFactor_11 * 256 + 0x1000) / (float)0x1000);
    }

    //LAB_80107bf0
    final int hudMetricsIndexOffset = Math.min(4, daddy.currentPressNumber_07);

    //LAB_80107c08
    // Press number
    renderButtonPressHudElement1(hudMetricsIndexOffset + 4, x1 + 36, y1, Translucency.B_PLUS_F, 128);
    // Button count background left edge
    renderButtonPressHudElement1(24, x1 -  4, y1,      Translucency.B_PLUS_F, 128);
    renderButtonPressHudElement1(26, x1 -  4, y1 + 12, Translucency.B_PLUS_F, 128);
    renderButtonPressHudElement1(30, x1 -  4, y1 +  4, Translucency.B_PLUS_F, 128);

    //LAB_80107cd4
    // Button count background main
    for(int i = 0; i < 48; i += 8) {
      renderButtonPressHudElement1(28, x1 + i, y1,      Translucency.B_PLUS_F, 128);
      renderButtonPressHudElement1(29, x1 + i, y1 + 12, Translucency.B_PLUS_F, 128);
      renderButtonPressHudElement1(32, x1 + i, y1 +  4, Translucency.B_PLUS_F, 128);
    }

    // Button count background right edge
    renderButtonPressHudElement1(25, x1 + 48, y1     , Translucency.B_PLUS_F, 128);
    renderButtonPressHudElement1(27, x1 + 48, y1 + 12, Translucency.B_PLUS_F, 128);
    renderButtonPressHudElement1(31, x1 + 48, y1 +  4, Translucency.B_PLUS_F, 128);
  }

  @Method(0x80107dc4L)
  public static void renderDragoonAdditionHud_(final DragoonAdditionScriptData1c daddy, final int transModesIndex, final int angle) {
    final int starY = daddyHudOffsetY_8011a020 + (rsin(angle) * 17 >> 12) + 24;
    final int starX;
    if(daddyHudSpinnerStepCountsPointer_8011a028[daddy.stepCountIndex_06] >= 2) {
      starX = daddyHudOffsetX_8011a01c + (rcos(angle) * 17 >> 12) + 28;
    } else {
      starX = daddyHudOffsetX_8011a01c + 28;
    }

    //LAB_80108048
    int brightness = 0x80;

    //LAB_801080ac
    for(int i = 0; i < 5; i++) {
      if(daddy.currentPressNumber_07 < i) {
        brightness = 0x10;
      }

      //LAB_801080cc
      battleUiParts.queueDaddyMeter(i, daddyHudOffsetX_8011a01c, daddyHudOffsetY_8011a020, 0x35 + i, Translucency.B_PLUS_F, brightness);
    }

    // Button press textures
    renderDragoonAdditionButtonPressTextures(daddy);

    //LAB_801081a8
    for(int i = 0; i < daddy.countEyeFlashTicks_0d; i++) {
      battleUiParts.queueDaddyEyeFlash(daddyHudOffsetX_8011a01c, daddyHudOffsetY_8011a020, daddyHudEyeClutOffsets_800fb84c[daddy.charId_18], Translucency.B_PLUS_F, 0x80);

      if(daddy.charId_18 == 9) {
        battleUiParts.queueDaddyDivineIris(daddyHudOffsetX_8011a01c, daddyHudOffsetY_8011a020);
      }
      //LAB_80108250
    }

    //LAB_80108268
    // Daddy spinner
    battleUiParts.queueDaddyFrame(daddyHudOffsetX_8011a01c, daddyHudOffsetY_8011a020, daddyHudFrameClutOffsets_800fb840[daddy.charId_18], null, 0x80);
    battleUiParts.queueDaddyFlatCenter(daddyHudOffsetX_8011a01c, daddyHudOffsetY_8011a020, 0x34, Translucency.of(daddyHudEyeTranslucencyModes_800fb7fc[transModesIndex][1]), 0x80);
    battleUiParts.queueDaddyDarkEye(daddyHudOffsetX_8011a01c, daddyHudOffsetY_8011a020, daddyHudEyeClutOffsets_800fb84c[daddy.charId_18], Translucency.of(daddyHudEyeTranslucencyModes_800fb7fc[transModesIndex][0]), 0x80);
    battleUiParts.queueDaddyArrow(daddyHudOffsetX_8011a01c, daddyHudOffsetY_8011a020, 0x32, Translucency.HALF_B_PLUS_HALF_F, 0x80);

    // Spinner star
    battleUiParts.queueDaddyStar(starX, starY, 0x33, Translucency.B_PLUS_F, (daddySpinnerBrightnessFactor_80119fb4 + 1) * 0x40);

    daddySpinnerBrightnessFactor_80119fb4 = 1 - daddySpinnerBrightnessFactor_80119fb4;
  }

  @Method(0x80108460L)
  public static int getCurrentDragoonAdditionPressNumber(final DragoonAdditionScriptData1c daddy, final int tickThresholdModifier) {
    int currentPressNumber = 0;
    int totalSteps = 0;
    final int[] successWindowArray = daddyHitSuccessWindowsPointer_8011a02c;
    final int[] stepCounts = daddyHudSpinnerStepCountsPointer_8011a028;
    final int previousTick = daddy.currentTick_04 - 1;

    //LAB_80108484
    for(int i = 0; i < 5; i++) {
      totalSteps += stepCounts[i];
      final int tickThreshold = totalSteps - tickThresholdModifier - (successWindowArray[i] >> 1);
      //LAB_801084c4
      if((i & 1) == 0 && previousTick >= tickThreshold + 1 || (i & 1) != 0 && previousTick >= tickThreshold) {
        //LAB_801084cc
        final int successWindow = successWindowArray[i];
        if(successWindow != 0 && totalSteps + (successWindow >> 1) >= previousTick) {
          currentPressNumber = i + 1;
        }
      }
      //LAB_801084f8
      //LAB_801084fc
    }

    return currentPressNumber;
  }

  @Method(0x80108514L)
  public static void renderDragoonAdditionHud(final ScriptState<DragoonAdditionScriptData1c> state, final DragoonAdditionScriptData1c daddy) {
    renderDragoonAdditionHud_(daddy, 0, daddy.baseAngle_02 - 0x400);
  }

  @Method(0x80108574L)
  public static void tickDragoonAdditionHud(final ScriptState<DragoonAdditionScriptData1c> state, final DragoonAdditionScriptData1c daddy) {
    if(daddy.tickEffect_0f == 0) {
      if(daddy.meterSpinning_10 == 0) {
        daddy.ticksRemainingToBeginAddition_12--;
        if(daddy.ticksRemainingToBeginAddition_12 == 0) {
          state.deallocateWithChildren();
        } else if(((press_800bee94 >>> 4 & 0x2) != 0 || CONFIG.getConfig(CoreMod.AUTO_DRAGOON_ADDITION_CONFIG.get())) && daddy.inputMode_13 != 2) {
          daddy.meterSpinning_10 = 1;
          daddyMeterSpinning_80119f42 = 1;
        }
      } else {
        //LAB_80108600
        if(daddy.buttonPressGlowBrightnessFactor_11 != 0) {
          daddy.buttonPressGlowBrightnessFactor_11--;
        }

        //LAB_80108614
        if(daddy.ticksUntilDeallocationAfterCompletion_0e != 0) {
          daddy.ticksUntilDeallocationAfterCompletion_0e--;

          if(daddy.ticksUntilDeallocationAfterCompletion_0e == 0) {
            daddyMeterSpinning_80119f42 = 0;

            //LAB_80108638
            state.deallocateWithChildren();
          }
        } else {
          //LAB_8010864c
          daddy.currentTick_04++;
          daddy.baseAngle_02 += 0x1000 / daddyHudSpinnerStepCountsPointer_8011a028[daddy.stepCountIndex_06];

          if(daddy.stepCountIndex_06 + 1 << 12 < daddy.baseAngle_02) {
            daddy.stepCountIndex_06++;
          }

          //LAB_801086a8
          if(daddy.countEyeFlashTicks_0d != 0) {
            daddy.countEyeFlashTicks_0d--;
          }

          //LAB_801086bc
          //LAB_801086e0
          if(getCurrentDragoonAdditionPressNumber(daddy, 0) != 0 && daddy.inputMode_13 == 1 || (press_800bee94 >>> 4 & 0x2) != 0 && daddy.inputMode_13 == 0) {
            //LAB_8010870c
            daddy.buttonPressGlowBrightnessFactor_11 = 4;
            daddy.countEyeFlashTicks_0d = 0;

            final int currentPressNumber = getCurrentDragoonAdditionPressNumber(daddy, 0);
            if(currentPressNumber != 0) {
              daddy.currentPressNumber_07 = currentPressNumber;
              daddy.countEyeFlashTicks_0d = 4;
              daddyHitsCompleted_80119f40 = 0;
            } else {
              //LAB_8010873c
              if(daddy.currentPressNumber_07 == 0) {
                daddyHitsCompleted_80119f40 = -1;
              } else {
                //LAB_8010875c
                daddyHitsCompleted_80119f40 = daddy.currentPressNumber_07;
              }

              //LAB_80108760
              daddy.ticksUntilDeallocationAfterCompletion_0e = 1;
            }
          }

          //LAB_80108768
          if(daddy.currentPressNumber_07 < daddy.baseAngle_02 - 0x400 >> 12) {
            daddy.countEyeFlashTicks_0d = 0;

            if(daddy.currentPressNumber_07 == 0) {
              daddyHitsCompleted_80119f40 = -1;
            } else {
              //LAB_801087a4
              if(daddy.currentPressNumber_07 == daddy.totalPressCount_14) {
                allocatePerfectDragoonAdditionEffect();
              }

              //LAB_801087bc
              daddyHitsCompleted_80119f40 = daddy.currentPressNumber_07;
            }

            //LAB_801087c8
            daddy.ticksUntilDeallocationAfterCompletion_0e = 4;
          }

          //LAB_801087d0
          daddy.unused_05 = daddy.baseAngle_02 & 0xff;
        }
      }
    }
    //LAB_801087dc
  }

  @ScriptDescription("Allocates a dragoon addition effect")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flags", description = "Various flags")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "charId", description = "The character ID")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticksUntilStart", description = "The number of ticks until the addition starts")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "hudX", description = "The HUD X position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "hudY", description = "The HUD Y position")
  @Method(0x801087f8L)
  public static FlowControl scriptAllocateDragoonAdditionScript(final RunningScript<?> script) {
    final int charId = script.params_20[1].get();
    final int flag = script.params_20[0].get();

    final ScriptState<DragoonAdditionScriptData1c> state = SCRIPTS.allocateScriptState("Dragoon addition", new DragoonAdditionScriptData1c());
    state.loadScriptFile(doNothingScript_8004f650);
    state.setTicker(SEffe::tickDragoonAdditionHud);
    state.setRenderer(SEffe::renderDragoonAdditionHud);

    final DragoonAdditionScriptData1c daddy = state.innerStruct_00;
    daddy.unused_00 = 1;
    daddy.baseAngle_02 = 0;
    daddy.currentTick_04 = 0;
    daddy.unused_05 = 0;
    daddy.stepCountIndex_06 = 0;
    daddy.currentPressNumber_07 = 0;
    daddy.countEyeFlashTicks_0d = 0;
    daddy.ticksUntilDeallocationAfterCompletion_0e = 0;
    daddy.tickEffect_0f = flag == 3 ? 1 : 0;
    daddy.meterSpinning_10 = flag == 1 ? 1 : 0;
    daddy.buttonPressGlowBrightnessFactor_11 = 0;
    daddy.ticksRemainingToBeginAddition_12 = script.params_20[2].get();
    daddy.inputMode_13 = CONFIG.getConfig(CoreMod.AUTO_DRAGOON_ADDITION_CONFIG.get()) ? 1 : flag & 0xff;
    daddy.charId_18 = charId;

    //LAB_80108910
    daddyHudOffsetX_8011a01c = script.params_20[3].get();
    daddyHudOffsetY_8011a020 = script.params_20[4].get();

    //LAB_80108924
    if(charId == 7) {
      daddyHudSpinnerStepCountsPointer_8011a028 = kongolDaddyHudSpinnerStepCounts_80119f7c;
      daddyHitSuccessWindowsPointer_8011a02c = kongolDaddyHitSuccessWindows_80119f98;
      daddy.totalPressCount_14 = 3;
    } else {
      //LAB_80108964
      daddyHudSpinnerStepCountsPointer_8011a028 = daddyHudSpinnerStepCounts_80119f44;
      daddyHitSuccessWindowsPointer_8011a02c = daddyHitSuccessWindows_80119f60;
      daddy.totalPressCount_14 = 4;
    }

    //LAB_80108984
    daddyHitsCompleted_80119f40 = 0;
    daddyMeterSpinning_80119f42 = 0;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the number of dragoon addition hits completed")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "unused")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "count", description = "The number of hits completed")
  @Method(0x801089ccL)
  public static FlowControl scriptGetDragoonAdditionHitsCompleted(final RunningScript<?> script) {
    script.params_20[1].set(daddyHitsCompleted_80119f40);
    return FlowControl.CONTINUE;
  }

  @Method(0x801089e8L)
  public static void tickPerfectDragoonAdditionEffect(final ScriptState<PerfectDragoonAdditionEffect30> state, final PerfectDragoonAdditionEffect30 effect) {
    //LAB_80108a38
    for(int i = 7; i >= 0; i--) {
      int brightness = 0x80;

      final PerfectDragoonAdditionEffectGlyph06 glyph = effect.glyphs_00[i];

      final int renderStage = glyph.renderStage_00;
      if(renderStage == 0) {
        //LAB_80108a78
        glyph.currentXPosition_02 -= 0x10;

        if(glyph.currentXPosition_02 < glyph.finalXPosition_04) {
          glyph.currentXPosition_02 = glyph.finalXPosition_04;
          glyph.renderStage_00++;
        }

        //LAB_80108aac
        //LAB_80108ac4
        for(int j = 0; j < 4; j++) {
          brightness -= 0x20;
          battleUiParts.queueDaddyPerfect(i, glyph.currentXPosition_02 + j * 6, daddyHudOffsetY_8011a020 + 16, brightness);
        }
      } else if(renderStage == 1) {
        //LAB_80108b58
        if(effect.glyphs_00[7].renderStage_00 == 1) {
          glyph.renderStage_00 = 2;
        }
        //LAB_80108a60
      } else if(renderStage == 2) {
        //LAB_80108b84
        glyph.currentStaticRenderTick_01++;

        if(glyph.currentStaticRenderTick_01 >= 12 && i == 0) {
          effect.glyphs_00[7].renderStage_00++;
        }
      } else if(renderStage == 3) {
        //LAB_80108bd0
        state.deallocateWithChildren();
        return;
      }

      //LAB_80108be8
      //LAB_80108bec
      //LAB_80108bf0
      battleUiParts.queueDaddyPerfect(i, glyph.currentXPosition_02, daddyHudOffsetY_8011a020 + 16, brightness);

      if((glyph.currentStaticRenderTick_01 & 0x1) != 0) {
        battleUiParts.queueDaddyPerfect(i, glyph.currentXPosition_02, daddyHudOffsetY_8011a020 + 16, brightness);
      }
      //LAB_80108cb0
    }
    //LAB_80108cc4
  }

  @Method(0x80108cf4L)
  public static void allocatePerfectDragoonAdditionEffect() {
    playSound(0, 50, 0, 0, (short)0, (short)0);

    final ScriptState<PerfectDragoonAdditionEffect30> state = SCRIPTS.allocateScriptState("PerfectDragoonAdditionEffect30", new PerfectDragoonAdditionEffect30());
    state.loadScriptFile(doNothingScript_8004f650);
    state.setTicker(SEffe::tickPerfectDragoonAdditionEffect);
    final PerfectDragoonAdditionEffect30 effect = state.innerStruct_00;

    //LAB_80108d9c
    int finalXPosition = daddyHudOffsetX_8011a01c;
    int initialXPosition = 130;
    for(int i = 0; i < 8; i++) {
      final PerfectDragoonAdditionEffectGlyph06 glyph = effect.glyphs_00[i];

      finalXPosition = finalXPosition + 8;
      glyph.renderStage_00 = 0;
      glyph.currentStaticRenderTick_01 = 0;
      glyph.currentXPosition_02 = initialXPosition;
      glyph.finalXPosition_04 = finalXPosition;
      initialXPosition = initialXPosition + 32;
    }
  }

  @ScriptDescription("No-op")
  @Method(0x80108de8L)
  public static FlowControl FUN_80108de8(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("No-op")
  @Method(0x80108df0L)
  public static FlowControl FUN_80108df0(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates an empty effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @Method(0x80108df8L)
  public static FlowControl FUN_80108df8(final RunningScript<? extends BattleObject> script) {
    script.params_20[0].set(allocateEffectManager("Unknown (FUN_80108df8)", script.scriptState_04, null, null, null, null).index);
    return FlowControl.CONTINUE;
  }

  @Method(0x80108e40L)
  public static void renderRainEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> data) {
    final RainEffect08 effect = (RainEffect08)data.effect_44;
    final RaindropEffect0c[] rainArray = effect.raindropArray_04;

    //LAB_80108e84
    for(int i = 0; i < effect.count_00; i++) {
      if(Math.abs(Math.abs(rainArray[i].y0_04 + rainArray[i].x0_02) - Math.abs(rainArray[i].y1_08 + rainArray[i].x1_06)) <= 180) {
        GPU.queueCommand(30, new GpuCommandLine()
          .translucent(Translucency.of(data.params_10.flags_00 >>> 28 & 3))
          .monochrome(0, 0)
          .rgb(1, data.params_10.colour_1c.x, data.params_10.colour_1c.y, data.params_10.colour_1c.z)
          .pos(0, rainArray[i].x1_06 - 256, rainArray[i].y1_08 - 128)
          .pos(1, rainArray[i].x0_02 - 256, rainArray[i].y0_04 - 128)
        );
      }
      //LAB_80108f6c
    }
    //LAB_80108f84
  }

  @Method(0x80109000L)
  public static void tickRainEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> data) {
    final RainEffect08 effect = (RainEffect08)data.effect_44;
    final RaindropEffect0c[] rainArray = effect.raindropArray_04;

    //LAB_80109038
    for(int i = 0; i < effect.count_00; i++) {
      final int endpointShiftX = (int)(MathHelper.sin(data.params_10.rot_10.x) * 32.0f * data.params_10.scale_16.x * rainArray[i].angleModifier_0a);
      final int endpointShiftY = (int)(MathHelper.cos(data.params_10.rot_10.x) * 32.0f * data.params_10.scale_16.x * rainArray[i].angleModifier_0a);
      rainArray[i].x1_06 = rainArray[i].x0_02;
      rainArray[i].y1_08 = rainArray[i].y0_04;
      rainArray[i].x0_02 = rainArray[i].x0_02 + endpointShiftX & 0x1ff;
      rainArray[i].y0_04 = rainArray[i].y0_04 + endpointShiftY & 0xff;
    }
    //LAB_80109110
  }

  @ScriptDescription("Allocates a rain effect")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "count", description = "How dense the rain should be")
  @Method(0x80109158L)
  public static FlowControl scriptAllocateRainEffect(final RunningScript<? extends BattleObject> script) {
    final int count = script.params_20[1].get();
    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager(
      "RainEffect08",
      script.scriptState_04,
      SEffe::tickRainEffect,
      SEffe::renderRainEffect,
      null,
      new RainEffect08(count)
    );

    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;
    final RainEffect08 effect = (RainEffect08)manager.effect_44;
    manager.params_10.flags_00 = 0x5000_0000;

    //LAB_80109204
    final RaindropEffect0c[] rainArray = effect.raindropArray_04;
    for(int i = 0; i < count; i++) {
      rainArray[i]._00 = 1;
      rainArray[i].x0_02 = (short)seed_800fa754.nextInt(513);
      rainArray[i].y0_04 = (short)seed_800fa754.nextInt(257);
      rainArray[i].angleModifier_0a = seed_800fa754.nextFloat(MathHelper.PI * 1.5f) + MathHelper.PI / 2.0f;
    }

    //LAB_80109328
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x80109358L)
  public static void FUN_80109358(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> data) {
    final ScreenDistortionEffectData08 sp48 = (ScreenDistortionEffectData08)data.effect_44;

    // Dunno why these actually need to be truncated instead of fractions, but it breaks the effect otherwise
    final float sp30 = (int)(data.params_10.scale_16.x * 0x1000) >> 8;
    final float sp2c = (int)(data.params_10.scale_16.y * 0x1000) >> 11;
    final float sp38 = (int)(data.params_10.scale_16.z * 0x1000) * 15 >> 9;

    //LAB_801093f0
    for(int s3 = 1; s3 >= -1; s3 -= 2) {
      final float angle = sp48.angle_00;
      float angle1 = angle;
      float angle2 = angle;
      float s5 = s3 == 1 ? 0.0f : -1.0f;
      int sp40 = s3 == 1 ? 120 : 119;

      //LAB_80109430
      //LAB_8010944c
      while(s3 == -1 && s5 > -sp38 || s3 == 1 && s5 < sp38) {
        float s2 = MathHelper.sin(angle1) * sp2c + 1.0f + sp2c;

        if((int)s2 == 0.0f) {
          s2 = 1.0f;
        }

        //LAB_8010949c
        //LAB_801094b8
        for(int s6 = 0; s6 < (int)s2; s6++) {
          final int x = (int)(MathHelper.sin(angle2) * sp30);
          final int y = (int)(s5 + s6 * s3);

          GPU.queueCommand(30, new GpuCommandQuad()
            .bpp(Bpp.BITS_15)
            .translucent(Translucency.of(data.params_10.flags_00 >>> 28 & 3))
            .rgb(data.params_10.colour_1c)
            .pos(-160 - x, y, 320, 1)
            .uv(0, sp40)
            .texture(GPU.getDisplayBuffer())
          );

          angle2 += s3 * 0.05f;
        }

        //LAB_80109678
        angle1 += s2 * 0.05f;
        sp40 += s3;
        s5 += s2 * s3;
      }
    }
  }

  @Method(0x801097e0L)
  public static void renderScreenDistortionBlurEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> data) {
    GPU.queueCommand(30, new GpuCommandQuad()
      .bpp(Bpp.BITS_15)
      .translucent(Translucency.of(data.params_10.flags_00 >>> 28 & 3))
      .rgb(data.params_10.colour_1c)
      .pos(-160, -120, 320, 240)
      .texture(GPU.getDisplayBuffer())
    );
  }

  @Method(0x80109a4cL)
  public static void FUN_80109a4c(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> data) {
    final ScreenDistortionEffectData08 effect = (ScreenDistortionEffectData08)data.effect_44;
    effect.angle_00 += effect.angleStep_04;
  }

  @Method(0x80109a6cL)
  public static void tickScreenDistortionBlurEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> data) {
    // no-op
  }

  @ScriptDescription("Allocates a screen distortion effect")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "angleStep", description = "The amount to rotate each frame (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "The distortion type")
  @Method(0x80109a7cL)
  public static FlowControl scriptAllocateScreenDistortionEffect(final RunningScript<? extends BattleObject> script) {
    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager(
      "Screen distortion",
      script.scriptState_04,
      // Ticker and renderer are swapped for some reason
      screenDistortionEffectRenderers_80119fd4[script.params_20[2].get()],
      screenDistortionEffectTickers_80119fe0[script.params_20[2].get()],
      null,
      new ScreenDistortionEffectData08()
    );

    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;
    final ScreenDistortionEffectData08 effect = (ScreenDistortionEffectData08)manager.effect_44;
    effect.angle_00 = MathHelper.PI;
    effect.angleStep_04 = MathHelper.psxDegToRad(script.params_20[1].get());
    manager.params_10.flags_00 = 0x4000_0000;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x80109b44L)
  public static void applyVertexDifferenceAnimation(final ScriptState<VertexDifferenceAnimation18> state, final VertexDifferenceAnimation18 animation) {
    animation.ticksRemaining_00--;

    if(animation.ticksRemaining_00 < 0) {
      state.deallocateWithChildren();
      return;
    }

    //LAB_80109b7c
    //LAB_80109b90
    for(int i = 0; i < animation.vertexCount_08; i++) {
      final Vector3f source = animation.sourceVertices_0c[i];
      final Vector3f current = animation.currentState_10[i];
      final Vector3f previous = animation.previousState_14[i];
      previous.add(current);
      current.x += current.x * animation.embiggener_04;
      current.y += current.y * animation.embiggener_04;
      current.z += current.z * animation.embiggener_04;
      source.set(previous);
    }

    //LAB_80109ce0
  }

  /** Kubila demon frog, Lloyd's cape, Selebus' strapple and zambo hands, Grand Jewel heal, etc. */
  @ScriptDescription("Allocates a vertex difference animation for an effect")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "differenceIndex", description = "The difference index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "The number of ticks until the animation finishes")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scale", description = "How much to grow each frame")
  @Method(0x80109d30L)
  public static FlowControl scriptAllocateVertexDifferenceAnimation(final RunningScript<?> script) {
    final int ticksRemaining = script.params_20[2].get();
    final float embiggener = script.params_20[3].get() / (float)0x100;

    final EffectManagerData6c<?> sourceState = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    final EffectManagerData6c<?> diffState = SCRIPTS.getObject(script.params_20[1].get(), EffectManagerData6c.class);

    final ScriptState<VertexDifferenceAnimation18> state = SCRIPTS.allocateScriptState("Vertex difference animation source %d (%s), diff %d (%s)".formatted(sourceState.myScriptState_0e.index, sourceState.name, diffState.myScriptState_0e.index, diffState.name), new VertexDifferenceAnimation18());

    state.loadScriptFile(doNothingScript_8004f650);
    state.setTicker(SEffe::applyVertexDifferenceAnimation);
    final DeffTmdRenderer14 source = (DeffTmdRenderer14)sourceState.effect_44;
    final DeffTmdRenderer14 diff = (DeffTmdRenderer14)diffState.effect_44;
    final TmdObjTable1c sourceModel = source.tmd_08;
    final TmdObjTable1c diffModel = diff.tmd_08;
    final VertexDifferenceAnimation18 animation = state.innerStruct_00;
    animation.ticksRemaining_00 = ticksRemaining;
    animation.embiggener_04 = embiggener;
    animation.vertexCount_08 = sourceModel.n_vert_04;
    animation.sourceVertices_0c = sourceModel.vert_top_00;
    animation.currentState_10 = new Vector3f[sourceModel.n_vert_04];
    animation.previousState_14 = new Vector3f[sourceModel.n_vert_04];
    Arrays.setAll(animation.currentState_10, i -> new Vector3f());
    Arrays.setAll(animation.previousState_14, i -> new Vector3f());
    // Set unused static _8011a030 to 1

    //LAB_80109e78
    for(int i = 0; i < sourceModel.n_vert_04; i++) {
      final Vector3f sourceVertex = sourceModel.vert_top_00[i];
      animation.previousState_14[i].set(sourceVertex);
    }

    //LAB_80109ecc
    //LAB_80109ee4
    for(int i = 0; i < animation.vertexCount_08; i++) {
      final Vector3f diffVertex = diffModel.vert_top_00[i];
      final Vector3f previous = animation.previousState_14[i];
      final Vector3f current = animation.currentState_10[i];
      current.x = (diffVertex.x - previous.x) / ticksRemaining;
      current.y = (diffVertex.y - previous.y) / ticksRemaining;
      current.z = (diffVertex.z - previous.z) / ticksRemaining;
    }

    //LAB_80109f90
    return FlowControl.CONTINUE;
  }

  /**
   * Code deleted from LAB_8010a130. Condition variable was set from script, and value
   * was hardcoded to 0 so that the code in the condition was never run.
   */
  @Method(0x80109fc4L)
  public static void FUN_80109fc4(final ScriptState<EffectManagerData6c<EffectManagerParams.FrozenJetType>> state, final EffectManagerData6c<EffectManagerParams.FrozenJetType> manager) {
    final FrozenJetEffect28 effect = (FrozenJetEffect28)manager.effect_44;
    int s1 = effect._18;

    //LAB_8010a020
    outer:
    while(true) {
      int s4 = effect._1a + (s1 << 11);
      s1++;

      //LAB_8010a04c
      for(int s2 = 1; s2 < effect._18 - 1; s2++) {
        if(s1 >= effect.vertices_0c.length - effect._18) {
          break outer;
        }

        final Vector3f sp0x10 = new Vector3f(effect.verticesCopy_1c[s1]);
        sp0x10.y += rsin(s4) * (manager.params_10._28 << 10 >> 8) >> 12;
        effect.vertices_0c[s1].set(sp0x10);
        s4 += 0x1000 / effect._18 / manager.params_10._2c >> 8;
        s1++;
      }

      //LAB_8010a124
      s1++;
    }

    //LAB_8010a130
    //LAB_8010a15c
    //LAB_8010a374
    effect._1a += (short)(manager.params_10._24 << 7 >> 8);
  }

  @ScriptDescription("Allocates a frozen jet effect")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "tmdDeffIndex", description = "The effect index of the model to use")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p3")
  @Method(0x8010a3fcL)
  public static FlowControl scriptAllocateFrozenJetEffect(final RunningScript<? extends BattleObject> script) {
    final int s4 = script.params_20[2].get();
    final int sp18 = script.params_20[3].get();

    final DeffTmdRenderer14 v1 = (DeffTmdRenderer14)((EffectManagerData6c<?>)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00).effect_44;
    final TmdObjTable1c tmd = v1.tmd_08;

    final ScriptState<EffectManagerData6c<EffectManagerParams.FrozenJetType>> state = allocateEffectManager(
      "FrozenJetEffect28",
      script.scriptState_04,
      SEffe::FUN_80109fc4,
      null,
      null,
      new FrozenJetEffect28(tmd.vert_top_00, tmd.primitives_10, s4, sp18 & 0xff),
      new EffectManagerParams.FrozenJetType()
    );

    final EffectManagerData6c<EffectManagerParams.FrozenJetType> manager = state.innerStruct_00;
    manager.params_10._24 = 0x100;
    manager.params_10._28 = 0x100;
    manager.params_10._2c = 0x100;

    //LAB_8010a5c8
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  /** Used in Rose transform */
  @ScriptDescription("Allocates a gradient rays effect")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "count", description = "The ray count")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p3")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p4")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p5")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flags", description = "The effect flags")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "The effect type")
  @Method(0x8010a610L)
  public static FlowControl scriptAllocateGradientRaysEffect(final RunningScript<? extends BattleObject> script) {
    final int count = script.params_20[1].get();

    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager(
      "GradientRaysEffect24",
      script.scriptState_04,
      SEffe::gradientRaysEffectTicker,
      SEffe::gradientRaysEffectRenderer,
      null,
      new GradientRaysEffect24(count)
    );

    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;
    final GradientRaysEffect24 effect = (GradientRaysEffect24)manager.effect_44;
    effect._08 = script.params_20[2].get();
    effect._0c = script.params_20[3].get();
    effect._10 = script.params_20[4].get();
    effect._14 = script.params_20[5].get();
    effect.flags_18 = script.params_20[6].get();
    effect.type_1c = script.params_20[7].get();
    effect.projectionPlaneDistanceDiv4_20 = getProjectionPlaneDistance() / 4.0f;

    //LAB_8010a754
    for(int i = 0; i < effect.count_04; i++) {
      //LAB_8010a770
      effect.rays_00[i].angle_00 = MathHelper.psxDegToRad((short)(rand() % 0x1000));

      final int v0;
      if((effect.flags_18 & 0x2) == 0) {
        //LAB_8010a7a8
        v0 = rand() % 0x80;
      } else {
        //LAB_8010a7b4
        //LAB_8010a7d0
        v0 = rand() % 0x10;
      }

      //LAB_8010a7d4
      effect.rays_00[i]._02 = (short)v0;
      if((effect.flags_18 & 0x1) != 0) {
        effect.rays_00[i]._02 += 0x70;
      }
      //LAB_8010a800
    }

    //LAB_8010a818
    script.params_20[0].set(state.index);
    manager.params_10.flags_00 |= 0x5400_0000;
    return FlowControl.CONTINUE;
  }

  /** Used in Rose transform */
  @Method(0x8010a860L)
  public static void renderGradientRay(final EffectManagerData6c<EffectManagerParams.VoidType> manager, final GradientRaysEffectInstance04 gradientRay) {
    final Vector3f sp0x38 = new Vector3f();
    final Vector3f sp0x40 = new Vector3f();
    final Vector3f sp0x48 = new Vector3f();
    final Vector3f sp0x50 = new Vector3f();
    final Vector2f xy0 = new Vector2f();
    final Vector2f xy1 = new Vector2f();
    final Vector2f xy2 = new Vector2f();
    final Vector2f xy3 = new Vector2f();

    final MV sp0x80 = new MV();
    final MV sp0xa0 = new MV();
    final MV sp0xc0 = new MV();

    final GradientRaysEffect24 effect = (GradientRaysEffect24)manager.effect_44;

    //LAB_8010a968
    if((effect.flags_18 & 0x4) == 0) {
      if(effect._10 * 2 < gradientRay._02 * effect._08) {
        sp0x40.y = -effect._10;
        sp0x48.y = -effect._10;
        sp0x50.y = -effect._10 * 2.0f;
      } else {
        //LAB_8010a9ec
        sp0x40.y = gradientRay._02 * -effect._08 / 2.0f;
        sp0x48.y = gradientRay._02 * -effect._08 / 2.0f;
        sp0x50.y = gradientRay._02 * -effect._08;
      }

      //LAB_8010aa34
      sp0x40.z = effect._0c;
      sp0x48.z = -effect._0c;
    }

    //LAB_8010aa54
    final Vector3f translation = new Vector3f(0.0f, gradientRay._02 * effect._08, 0.0f);
    final Vector3f rotation = new Vector3f(gradientRay.angle_00, 0.0f, 0.0f);
    sp0xa0.rotationXYZ(rotation);
    sp0x80.transfer.set(translation);
    sp0x80.compose(sp0xa0, sp0xc0);
    calculateEffectTransforms(sp0x80, manager);

    if((manager.params_10.flags_00 & 0x400_0000) == 0) {
      sp0x80.compose(worldToScreenMatrix_800c3548, sp0xa0);
      sp0xa0.rotationXYZ(manager.params_10.rot_10);
      sp0xc0.compose(sp0xa0, sp0xc0);
      GTE.setTransforms(sp0xc0);
    } else {
      //LAB_8010ab10
      sp0xc0.compose(sp0x80, sp0xa0);
      sp0xa0.compose(worldToScreenMatrix_800c3548, sp0x80);
      GTE.setTransforms(sp0x80);
    }

    //LAB_8010ab34
    final float z = RotTransPers4(sp0x38, sp0x40, sp0x48, sp0x50, xy0, xy1, xy2, xy3);
    if(z >= effect.projectionPlaneDistanceDiv4_20) {
      final GpuCommandPoly cmd = new GpuCommandPoly(4)
        .translucent(Translucency.B_PLUS_F);

      if(effect.type_1c == 1) {
        //LAB_8010abf4
        final int v0 = (0x80 - gradientRay._02) * manager.params_10.colour_1c.x / 0x80;
        final int v1 = (short)v0 / 2;

        cmd
          .monochrome(0, 0)
          .monochrome(1, 0)
          .rgb(2, v0, v1, v1)
          .rgb(3, v0, v1, v1);
      } else if(effect.type_1c == 2) {
        //LAB_8010ac68
        final short s3 = (short)(FUN_8010b058(gradientRay._02) * manager.params_10.colour_1c.x * 8 / 0x80);
        final short s2 = (short)(FUN_8010b0dc(gradientRay._02) * manager.params_10.colour_1c.y * 8 / 0x80);
        final short a2 = (short)(FUN_8010b160(gradientRay._02) * manager.params_10.colour_1c.z * 8 / 0x80);

        cmd
          .monochrome(0, 0)
          .rgb(1, s3 / 2, s2 / 2, a2 / 2)
          .rgb(2, s3 / 2, s2 / 2, a2 / 2)
          .rgb(3, s3, s2, a2);
      }

      //LAB_8010ad68
      //LAB_8010ad6c
      cmd
        .pos(0, xy0.x, xy0.y)
        .pos(1, xy1.x, xy1.y)
        .pos(2, xy2.x, xy2.y)
        .pos(3, xy3.x, xy3.y);

      GPU.queueCommand(z / 4.0f, cmd);
    }

    //LAB_8010ae18
  }

  @Method(0x8010ae40L)
  public static void gradientRaysEffectTicker(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> data) {
    final GradientRaysEffect24 rayEffect = (GradientRaysEffect24)data.effect_44;

    //LAB_8010ae80
    for(int i = 0; i < rayEffect.count_04; i++) {
      final GradientRaysEffectInstance04 ray = rayEffect.rays_00[i];

      if((rayEffect.flags_18 & 0x1) == 0) {
        //LAB_8010aee8
        ray._02 += (short)rayEffect._14;

        if((rayEffect.flags_18 & 0x2) != 0 && ray._02 >= 0x80) {
          ray._02 = 0x80;
        } else {
          //LAB_8010af28
          //LAB_8010af3c
          ray._02 %= 0x80;
        }
      } else {
        ray._02 -= (short)rayEffect._14;

        if((rayEffect.flags_18 & 0x2) != 0 && ray._02 <= 0) {
          ray._02 = 0;
        } else {
          //LAB_8010aecc
          ray._02 += 0x80;
          ray._02 %= 0x80;
        }
      }
      //LAB_8010af4c
    }
    //LAB_8010af64
  }

  @Method(0x8010af6cL)
  public static void gradientRaysEffectRenderer(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> manager) {
    if(manager.params_10.flags_00 >= 0) {
      final GradientRaysEffect24 rayEffect = (GradientRaysEffect24)manager.effect_44;

      //LAB_8010afcc
      for(int i = 0; i < rayEffect.count_04; i++) {
        renderGradientRay(manager, rayEffect.rays_00[i]);
      }
    }

    //LAB_8010aff0
  }

  @Method(0x8010b058L)
  public static short FUN_8010b058(final short a0) {
    //LAB_8010b06c
    return (short)switch(a0 / 0x10) {
      case 0, 1, 6 -> 0x10;
      case 2, 7 -> 0x10 - a0 % 0x10;
      case 5 -> a0 % 0x10;
      default -> 0;
    };
  }

  @Method(0x8010b0dcL)
  public static short FUN_8010b0dc(final short a0) {
    //LAB_8010b0f0
    return (short)switch(a0 / 0x10) {
      case 0, 4, 5 -> 0x10;
      case 1, 6 -> 0x10 - a0 % 0x10;
      case 3 -> a0 % 0x10;
      default -> 0;
    };
  }

  @Method(0x8010b160L)
  public static short FUN_8010b160(final short a0) {
    return (short)switch(a0 / 0x10) {
      case 0, 1, 2, 3 -> 0x10L;
      case 4 -> 0x10 - a0 % 0x10;
      default -> 0;
    };
  }

  /** Used for Death Dimension, Melbu's screenshot attack, and Kubila's demon frog, possibly other unknown effects */
  @ScriptDescription("Allocates a screen capture effect")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "deffFlags", description = "The DEFF flags")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "w", description = "The width")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "h", description = "The height")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "The effect type")
  @Method(0x8010b1d8L)
  public static FlowControl scriptAllocateScreenCaptureEffect(final RunningScript<? extends BattleObject> script) {
    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager(
      "Screen capture",
      script.scriptState_04,
      null,
      SEffe::renderScreenCaptureEffect,
      null,
      new ScreenCaptureEffect1c()
    );

    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;
    final ScreenCaptureEffect1c effect = (ScreenCaptureEffect1c)manager.effect_44;
    effect.captureW_04 = script.params_20[4].get();
    effect.captureH_08 = script.params_20[5].get();
    effect.rendererIndex_0c = script.params_20[6].get();
    effect.screenspaceW_10 = 0;
    script.params_20[0].set(state.index);
    FUN_8010c2e0(effect.metrics_00, script.params_20[1].get());

    final int v0 = effect.rendererIndex_0c;
    if(v0 == 0) {
      //LAB_8010b2e4
      final int x = script.params_20[2].get() + 160;
      final int y = script.params_20[3].get() + 120;
      final int w = effect.captureW_04 / 2;
      final int h = effect.captureH_08 / 2;

      //LAB_8010b308
      for(int i = 0; i < 4; i++) {
        final ScreenCaptureEffectMetrics8 metrics = effect.metrics_00;

        GPU.queueCommand(40, new GpuCommandCopyDisplayBufferToVram(x + ((i & 1) - 1) * w, y + (i / 2 - 1) * h, metrics.u_00, metrics.v_02 + i * 64, w, h));
        GPU.queueCommand(40, new GpuCommandSetMaskBit(true, Gpu.DRAW_PIXELS.ALWAYS));
      }
    } else if(v0 < 3) {
      //LAB_8010b3f0
      final int x = script.params_20[2].get() + 160 - effect.captureW_04 / 2;
      final int y = script.params_20[3].get() + 120 - effect.captureH_08 / 2;
      final int w = effect.captureW_04 / 5;
      final int h = effect.captureH_08 / 3;

      //LAB_8010b468
      for(int i = 0; i < 15; i++) {
        final ScreenCaptureEffectMetrics8 metrics = effect.metrics_00;

        GPU.queueCommand(40, new GpuCommandCopyDisplayBufferToVram(x + i % 5 * w, y + i / 5 * h, metrics.u_00 + i % 2 * 32, metrics.v_02 + i / 2 * 32, w, h));
        GPU.queueCommand(40, new GpuCommandSetMaskBit(true, Gpu.DRAW_PIXELS.ALWAYS));
      }
    }

    //LAB_8010b548
    manager.params_10.flags_00 |= 0x5000_0000;
    return FlowControl.CONTINUE;
  }

  /** TODO This is the second screen capture function, usage currently unknown */
  @Method(0x8010b594L)
  public static void FUN_8010b594(final EffectManagerData6c<EffectManagerParams.VoidType> manager, final ScreenCaptureEffect1c effect, final MV transforms) {
    int v1;
    int a0;
    int a1;

    final Vector3i rgb = new Vector3i();

    if((manager.params_10.flags_00 & 0x40) != 0) {
      final Vector3f normal = new Vector3f();
      _800fb8d0.mul(transforms, normal);
      normal.add(transforms.transfer.x / 4096.0f, transforms.transfer.y / 4096.0f, transforms.transfer.z / 4096.0f);
      GTE.normalColour(normal, 0xffffff, rgb);
    } else {
      //LAB_8010b6c8
      rgb.set(0x80, 0x80, 0x80);
    }

    //LAB_8010b6d8
    rgb.x = rgb.x * manager.params_10.colour_1c.x / 128;
    rgb.y = rgb.y * manager.params_10.colour_1c.y / 128;
    rgb.z = rgb.z * manager.params_10.colour_1c.z / 128;

    //LAB_8010b764
    for(int i = 0; i < 8; i++) {
      final GpuCommandPoly cmd = new GpuCommandPoly(3)
        .rgb(rgb.x, rgb.y, rgb.z);

      switch(i) {
        case 1, 2, 4, 7 -> {
          final Vector3f vert0 = new Vector3f();
          final Vector3f vert1 = new Vector3f();
          final Vector3f vert2 = new Vector3f();
          final Vector2f sxy0 = new Vector2f();
          final Vector2f sxy1 = new Vector2f();
          final Vector2f sxy2 = new Vector2f();

          //LAB_8010b80c
          if(i == 1 || i == 4) {
            //LAB_8010b828
            a0 = i & 0x3;
            vert1.z = (a0 - 2) * effect.screenspaceW_10 / 4.0f;
            vert0.z = (a0 - 1) * effect.screenspaceW_10 / 4.0f;
            vert2.z = vert0.z;
            a0 = i >> 2;
            vert0.y = (a0 - 1) * effect.screenspaceH_14 / 2.0f;
            vert1.y = a0 * effect.screenspaceH_14 / 2.0f;
            vert2.y = vert1.y;
            a0 = (i >> 1) * 64;
            v1 = (i & 0x1) * 32;

            cmd
              .uv(0, a0, v1 + effect.captureW_04 / 4 - 1)
              .uv(1, v1, a0 + effect.captureH_08 / 2 - 1)
              .uv(2, v1 + effect.captureW_04 / 4 - 1, a0 + effect.captureH_08 / 2 - 1);
          } else {
            //LAB_8010b8c8
            a0 = i & 0x3;
            vert1.z = (a0 - 2) * effect.screenspaceW_10 / 4.0f;
            vert0.z = vert1.z;
            vert2.z = (a0 - 1) * effect.screenspaceW_10 / 4.0f;
            a0 = i >> 2;
            vert0.y = (a0 - 1) * effect.screenspaceH_14 / 2.0f;
            v1 = (i & 1) * 32;
            a0 = (i >> 1) * 64;
            vert2.y = a0 * effect.screenspaceH_14 / 2.0f;
            vert1.y = vert2.y;

            cmd
              .uv(0, v1, a0)
              .uv(1, v1, a0 + effect.captureH_08 / 2 - 1)
              .uv(2, v1 + effect.captureW_04 / 4 - 1, a0 + effect.captureH_08 / 2 - 1);
          }

          //LAB_8010b9a4
          final float z = perspectiveTransformTriple(vert0, vert1, vert2, sxy0, sxy1, sxy2);

          if(effect.screenspaceW_10 == 0) {
            //LAB_8010b638
            final float sp8c = getProjectionPlaneDistance();
            final float zShift = z * 4;
            effect.screenspaceW_10 = effect.captureW_04 * zShift / sp8c;
            effect.screenspaceH_14 = effect.captureH_08 * zShift / sp8c;
            break;
          }

          final ScreenCaptureEffectMetrics8 metrics = effect.metrics_00;

          cmd
            .bpp(Bpp.BITS_15)
            .vramPos(metrics.u_00 & 0x3c0, (metrics.v_02 & 0x1) == 0 ? 0 : 256)
            .pos(0, sxy0.x, sxy0.y)
            .pos(1, sxy1.x, sxy1.y)
            .pos(2, sxy2.x, sxy2.y);

          GPU.queueCommand(z / 4.0f, cmd);
        }

        case 5, 6 -> {
          final Vector3f vert0 = new Vector3f();
          final Vector3f vert1 = new Vector3f();
          final Vector3f vert2 = new Vector3f();
          final Vector3f vert3 = new Vector3f();
          final Vector2f sxy0 = new Vector2f();
          final Vector2f sxy1 = new Vector2f();
          final Vector2f sxy2 = new Vector2f();
          final Vector2f sxy3 = new Vector2f();

          a0 = i & 0x3;
          a1 = i >> 2;
          vert2.z = (a0 - 2) * effect.screenspaceW_10 / 4.0f;
          vert0.z = vert2.z;
          vert1.y = (a1 - 1) * effect.screenspaceH_14 / 2.0f;
          vert0.y = vert1.y;
          vert3.z = (a0 - 1) * effect.screenspaceW_10 / 4.0f;
          vert1.z = vert3.z;
          vert3.y = a1 * effect.screenspaceH_14 / 2.0f;
          vert2.y = vert3.y;
          final float z = RotTransPers4(vert0, vert1, vert2, vert3, sxy0, sxy1, sxy2, sxy3);

          if(effect.screenspaceW_10 == 0) {
            //LAB_8010b664
            final float sp90 = getProjectionPlaneDistance();
            final float z2 = z * 4.0f;

            //LAB_8010b688
            effect.screenspaceW_10 = effect.captureW_04 * z2 / sp90;
            effect.screenspaceH_14 = effect.captureH_08 * z2 / sp90;
            break;
          }

          final int u = (i & 0x1) * 32;
          final int v = (i >> 1) * 64;
          final ScreenCaptureEffectMetrics8 metrics = effect.metrics_00;

          GPU.queueCommand(z / 4.0f, new GpuCommandPoly(4)
            .bpp(Bpp.BITS_15)
            .vramPos(metrics.u_00 & 0x3c0, (metrics.v_02 & 0x1) != 0 ? 256 : 0)
            .rgb(rgb.x, rgb.y, rgb.z)
            .pos(0, sxy0.x, sxy0.y)
            .pos(1, sxy1.x, sxy1.y)
            .pos(2, sxy2.x, sxy2.y)
            .pos(3, sxy3.x, sxy3.y)
            .uv(0, u, v)
            .uv(1, u + effect.captureW_04 / 4 - 1, v)
            .uv(2, u, v + effect.captureH_08 / 2 - 1)
            .uv(3, u + effect.captureW_04 / 4 - 1, v + effect.captureH_08 / 2 - 1)
          );
        }
      }
    }

    //LAB_8010bc40
  }

  @Method(0x8010bc60L)
  public static void renderScreenCapture(final EffectManagerData6c<EffectManagerParams.VoidType> manager, final ScreenCaptureEffect1c effect, final MV transforms) {
    final Vector3i rgb = new Vector3i();

    if((manager.params_10.flags_00 & 0x40) != 0) {
      final Vector3f normal = new Vector3f();
      _800fb8d0.mul(transforms, normal);
      normal.add(transforms.transfer.x / 4096.0f, transforms.transfer.y / 4096.0f, transforms.transfer.z / 4096.0f);
      GTE.normalColour(normal, 0xffffff, rgb);
    } else {
      //LAB_8010bd6c
      rgb.set(0x80, 0x80, 0x80);
    }

    //LAB_8010bd7c
    rgb.x = rgb.x * manager.params_10.colour_1c.x / 128;
    rgb.y = rgb.y * manager.params_10.colour_1c.y / 128;
    rgb.z = rgb.z * manager.params_10.colour_1c.z / 128;

    //LAB_8010be14
    for(int s0 = 0; s0 < 15; s0++) {
      final Vector3f sp0x28 = new Vector3f();
      final Vector3f sp0x30 = new Vector3f();
      final Vector3f sp0x38 = new Vector3f();
      final Vector3f sp0x40 = new Vector3f();

      final int a0 = s0 % 5;
      float v1 = effect.screenspaceW_10;
      float v0 = a0 * v1 / 5 - v1 / 2;
      sp0x28.z = v0;
      sp0x38.z = v0;

      final int a1 = s0 / 5;
      v1 = effect.screenspaceH_14;
      v0 = a1 * v1 / 3 - v1 / 2;
      sp0x28.y = v0;
      sp0x30.y = v0;

      v1 = effect.screenspaceW_10;
      v0 = (a0 + 1) * v1 / 5 - v1 / 2;
      sp0x30.z = v0;
      sp0x40.z = v0;

      v1 = effect.screenspaceH_14;
      v0 = (a1 + 1) * v1 / 3 - v1 / 2;
      sp0x38.y = v0;
      sp0x40.y = v0;

      final Vector2f sxy0 = new Vector2f();
      final Vector2f sxy1 = new Vector2f();
      final Vector2f sxy2 = new Vector2f();
      final Vector2f sxy3 = new Vector2f();
      final float z = RotTransPers4(sp0x28, sp0x30, sp0x38, sp0x40, sxy0, sxy1, sxy2, sxy3);

      if(effect.screenspaceW_10 == 0) {
        //LAB_8010bd08
        final float sp8c = getProjectionPlaneDistance();
        final float zShift = z * 4.0f;
        effect.screenspaceW_10 = effect.captureW_04 * zShift / sp8c;
        effect.screenspaceH_14 = effect.captureH_08 * zShift / sp8c;
        break;
      }

      final int leftU = s0 % 2 * 32;
      final int topV = s0 / 2 * 32;
      final int rightU = leftU + effect.captureW_04 / 5 - 1;
      final int bottomV = topV + effect.captureH_08 / 3 - 1;

      final ScreenCaptureEffectMetrics8 metrics = effect.metrics_00;

      GPU.queueCommand(z / 4.0f, new GpuCommandPoly(4)
        .bpp(Bpp.BITS_15)
        .vramPos(metrics.u_00 & 0x3c0, (metrics.v_02 & 0x1) != 0 ? 256 : 0)
        .rgb(rgb.x, rgb.y, rgb.z)
        .pos(0, sxy0.x, sxy0.y)
        .pos(1, sxy1.x, sxy1.y)
        .pos(2, sxy2.x, sxy2.y)
        .pos(3, sxy3.x, sxy3.y)
        .uv(0, leftU, topV)
        .uv(1, rightU, topV)
        .uv(2, leftU, bottomV)
        .uv(3, rightU, bottomV)
      );
    }

    //LAB_8010c0f0
  }

  @Method(0x8010c114L)
  public static void renderScreenCaptureEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> manager) {
    final MV transforms = new MV();

    if(manager.params_10.flags_00 >= 0) {
      final ScreenCaptureEffect1c effect = (ScreenCaptureEffect1c)manager.effect_44;
      calculateEffectTransforms(transforms, manager);
      transforms.compose(worldToScreenMatrix_800c3548);
      screenCaptureRenderers_80119fec[effect.rendererIndex_0c].accept(manager, effect, transforms);
    }

    //LAB_8010c278
  }

  @Method(0x8010c2e0L)
  public static void FUN_8010c2e0(final ScreenCaptureEffectMetrics8 metrics, final int deffFlags) {
    if((deffFlags & 0xf_ff00) != 0xf_ff00) {
      final DeffPart.SpriteType spriteType = (DeffPart.SpriteType)deffManager_800c693c.getDeffPart(deffFlags | 0x400_0000);
      final DeffPart.SpriteMetrics deffMetrics = spriteType.metrics_08;
      metrics.u_00 = deffMetrics.u_00;
      metrics.v_02 = deffMetrics.v_02;
      metrics.clut_06 = deffMetrics.clutY_0a << 6 | (deffMetrics.clutX_08 & 0x3f0) >>> 4;
    }

    //LAB_8010c368
  }

  @ScriptDescription("Allocates a lens flare effect attached to a battle entity")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The battle entity to attach to")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "deffFlags1", description = "The DEFF flags for layer 1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "deffFlags2", description = "The DEFF flags for layer 2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "deffFlags3", description = "The DEFF flags for layer 3")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "deffFlags4", description = "The DEFF flags for layer 4")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "deffFlags5", description = "The DEFF flags for layer 5")
  @Method(0x8010c378L)
  public static FlowControl scriptAllocateLensFlareEffect(final RunningScript<? extends BattleObject> script) {
    final int bentIndex = script.params_20[1].get();
    final int x = script.params_20[2].get();
    final int y = script.params_20[3].get();
    final int z = script.params_20[4].get();

    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager(
      "LensFlareEffect50",
      script.scriptState_04,
      SEffe::tickLensFlareEffect,
      SEffe::renderLensFlareEffect,
      null,
      new LensFlareEffect50()
    );

    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;
    final LensFlareEffect50 effect = (LensFlareEffect50)manager.effect_44;
    effect._00 = 5;
    effect._02 = 0;

    //LAB_8010c4a4
    for(int i = 0; i < 5; i++) {
      final LensFlareEffectInstance3c s0 = effect.instances_38[i];
      s0.onScreen_03 = true;
      s0.x_04 = 0;
      s0.y_06 = 0;
      s0._08 = 0;
      s0._0c = 0;
      s0._0e = 0;
      s0._10 = 0;
      s0._14 = 0;
      s0._16 = 0;
      s0._18 = 0;
      s0._28 = 0;
      s0.widthScale_2e = 0x1600;
      s0.heightScale_30 = 0x1600;
      s0._32 = 0;
      s0._34 = 0;

      final int a1 = script.params_20[5 + i].get();
      if(a1 == -1) {
        s0.enabled_02 = false;
      } else {
        //LAB_8010c500
        s0.enabled_02 = true;

        if((a1 & 0xf_ff00) == 0xf_ff00) {
          final SpriteMetrics08 metrics = deffManager_800c693c.spriteMetrics_39c[a1 & 0xff];
          effect.u_04[i] = metrics.u_00;
          effect.v_0e[i] = metrics.v_02;
          effect.w_18[i] = metrics.w_04;
          effect.h_22[i] = metrics.h_05;
          effect.clut_2c[i] = metrics.clut_06;
        } else {
          //LAB_8010c5a8
          final DeffPart.SpriteType spriteType = (DeffPart.SpriteType)deffManager_800c693c.getDeffPart(a1 | 0x400_0000);
          final DeffPart.SpriteMetrics deffMetrics = spriteType.metrics_08;
          effect.u_04[i] = deffMetrics.u_00;
          effect.v_0e[i] = deffMetrics.v_02;
          effect.w_18[i] = deffMetrics.w_04 * 4;
          effect.h_22[i] = deffMetrics.h_06;
          effect.clut_2c[i] = GetClut(deffMetrics.clutX_08, deffMetrics.clutY_0a);
        }
      }

      //LAB_8010c608
    }

    effect.bentIndex_3c = bentIndex;
    effect.x_40 = (short)x;
    effect.y_42 = (short)y;
    effect.z_44 = (short)z;
    effect.brightness_48 = 0;
    manager.params_10.flags_00 |= 0x5000_0000;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x8010c69cL)
  public static void tickLensFlareEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> manager) {
    final LensFlareEffect50 effect = (LensFlareEffect50)manager.effect_44;
    effect.shouldRender_4a = (short)(rand() % 30);

    if(effect.shouldRender_4a != 0) {
      final Vector2f screenCoords = SCRIPTS.getObject(effect.bentIndex_3c, BattleEntity27c.class).transformRelative(effect.x_40, effect.y_42, effect.z_44);
      final float x = -(screenCoords.x * 2.5f);
      final float y = -(screenCoords.y * 2.5f);

      //LAB_8010c7c0
      for(int i = 0; i < 5; i++) {
        final LensFlareEffectInstance3c inst = effect.instances_38[i];
        final int dispW = displayWidth_1f8003e0;
        final int dispH = displayHeight_1f8003e4;
        inst.x_04 = screenCoords.x + dispW / 2.0f;
        inst.y_06 = screenCoords.y + dispH / 2.0f;

        if(inst.x_04 > 0 && inst.x_04 < dispW && inst.y_06 > 0 && inst.y_06 < dispH) {
          inst.onScreen_03 = true;

          final int scale = lensFlareGlowScales_800fb8fc[i];
          inst.x_04 += x * scale / 0x100;
          inst.y_06 += y * scale / 0x100;
        } else {
          //LAB_8010c870
          inst.onScreen_03 = false;
        }
        //LAB_8010c874
      }

      // Adjust brightness based on X position
      float screenX = Math.abs(screenCoords.x);
      final int screenWidth = displayWidth_1f8003e0 / 2;
      if(screenX > screenWidth) {
        screenX = screenWidth;
      }

      //LAB_8010c8b8
      effect.brightness_48 = (short)(255.0f - 255.0f / screenWidth * screenX);
    }
    //LAB_8010c8e0
  }

  @Method(0x8010c8f8L)
  public static void renderLensFlareEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> manager) {
    final LensFlareEffect50 effect = (LensFlareEffect50)manager.effect_44;

    if(effect.shouldRender_4a != 0) {
      effect._02++;
      final int sp10 = manager.params_10.flags_00;

      //LAB_8010c9fc
      for(int i = 0; i < 5; i++) {
        final LensFlareEffectInstance3c inst = effect.instances_38[i];

        if(inst.enabled_02 && inst.onScreen_03) {
          final int w = effect.w_18[i];
          final int h = effect.h_22[i];
          final int tpage = (effect.v_0e[i] & 0x100) >>> 4 | (effect.u_04[i] & 0x3ff) >>> 6;
          final int u = (effect.u_04[i] & 0x3f) * 4;
          final int v = effect.v_0e[i] & 0xff;
          final int clutX = effect.clut_2c[i] << 4 & 0x3ff;
          final int clutY = effect.clut_2c[i] >>> 6 & 0x1ff;
          final int r = manager.params_10.colour_1c.x * effect.brightness_48 >> 8;
          final int g = manager.params_10.colour_1c.y * effect.brightness_48 >> 8;
          final int b = manager.params_10.colour_1c.z * effect.brightness_48 >> 8;

          if(i == 0) {
            for(int j = 0; j < 4; j++) {
              final int x = (inst.widthScale_2e * w >> 12) * lensFlareTranslationMagnitudeFactors_800fb910[j][0];
              final int y = (inst.heightScale_30 * h >> 12) * lensFlareTranslationMagnitudeFactors_800fb910[j][1];
              final int halfW = displayWidth_1f8003e0 / 2;
              final int halfH = displayHeight_1f8003e4 / 2;
              final float[][] sp0x48 = new float[4][2];
              sp0x48[0][0] = inst.x_04 - halfW + x;
              sp0x48[0][1] = inst.y_06 - halfH + y;
              sp0x48[1][0] = inst.x_04 - halfW + x + (w * inst.widthScale_2e >> 12);
              sp0x48[1][1] = inst.y_06 - halfH + y;
              sp0x48[2][0] = inst.x_04 - halfW + x;
              sp0x48[2][1] = inst.y_06 - halfH + y + (h * inst.heightScale_30 >> 12);
              sp0x48[3][0] = inst.x_04 - halfW + x + (w * inst.widthScale_2e >> 12);
              sp0x48[3][1] = inst.y_06 - halfH + y + (h * inst.heightScale_30 >> 12);

              final GpuCommandPoly cmd = new GpuCommandPoly(4)
                .bpp(Bpp.BITS_4)
                .clut(clutX, clutY)
                .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
                .rgb(r, g, b)
                .pos(0, sp0x48[lensFlareVertexIndices_800fb930[j][0]][0], sp0x48[lensFlareVertexIndices_800fb930[j][0]][1])
                .pos(1, sp0x48[lensFlareVertexIndices_800fb930[j][1]][0], sp0x48[lensFlareVertexIndices_800fb930[j][1]][1])
                .pos(2, sp0x48[lensFlareVertexIndices_800fb930[j][2]][0], sp0x48[lensFlareVertexIndices_800fb930[j][2]][1])
                .pos(3, sp0x48[lensFlareVertexIndices_800fb930[j][3]][0], sp0x48[lensFlareVertexIndices_800fb930[j][3]][1])
                .uv(0, u, v)
                .uv(1, u + w - 1, v)
                .uv(2, u, v + h - 1)
                .uv(3, u + w - 1, v + h - 1);

              if((sp10 >>> 30 & 1) != 0) {
                cmd.translucent(Translucency.of(sp10 >>> 28 & 0b11));
              }

              GPU.queueCommand(30, cmd);
            }
          } else {
            //LAB_8010ceec
            final int halfW = displayWidth_1f8003e0 / 2;
            final int halfH = displayHeight_1f8003e4 / 2;
            final float x = inst.x_04 - halfW - (inst.widthScale_2e * w >> 12) / 2.0f;
            final float y = inst.y_06 - halfH - (inst.heightScale_30 * h >> 12) / 2.0f;
            final int w2 = w * inst.widthScale_2e >> 12;
            final int h2 = h * inst.heightScale_30 >> 12;

            final GpuCommandPoly cmd = new GpuCommandPoly(4)
              .bpp(Bpp.BITS_4)
              .clut(clutX, clutY)
              .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
              .rgb(r, g, b)
              .pos(0, x, y)
              .pos(1, x + w2, y)
              .pos(2, x, y + h2)
              .pos(3, x + w2, y + h2)
              .uv(0, u, v)
              .uv(1, u + w - 1, v)
              .uv(2, u, v + h - 1)
              .uv(3, u + w - 1, v + h - 1);

            if((sp10 >>> 30 & 1) != 0) {
              cmd.translucent(Translucency.of(sp10 >>> 28 & 0b11));
            }

            GPU.queueCommand(30, cmd);
          }
        }
        //LAB_8010d198
        //LAB_8010d19c
      }
    }
    //LAB_8010d1ac
  }

  @ScriptDescription("Allocates a white-silver dragoon transformation feathers effect")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "count", description = "The number of feathers")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "deffFlags", description = "The DEFF flags")
  @Method(0x8010d1dcL)
  public static FlowControl scriptAllocateWsDragoonTransformationFeathersEffect(final RunningScript<? extends BattleObject> script) {
    final int featherCount = script.params_20[1].get();
    final int effectFlags = script.params_20[2].get();

    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager(
      "WsDragoonTransformationFeathersEffect14",
      script.scriptState_04,
      SEffe::tickWsDragoonTransformationFeathersEffect,
      SEffe::renderWsDragoonTransformationFeathersEffect,
      null,
      new WsDragoonTransformationFeathersEffect14(featherCount)
    );

    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;
    final WsDragoonTransformationFeathersEffect14 featherEffect = (WsDragoonTransformationFeathersEffect14)manager.effect_44;
    featherEffect.unused_02 = 0;

    //LAB_8010d298
    for(int i = 0; i < featherCount; i++) {
      final WsDragoonTransformationFeatherInstance70 feather = featherEffect.featherArray_10[i];
      feather.renderFeather_00 = false;
      feather.callbackIndex_02 = 0;
      feather.currentFrame_04 = 0;
      feather.countCallback0Frames_64 = 1;
      final int yOffset = -1500 / featherCount * i;
      feather.translation_08.set(0, yOffset, 0);
      feather.yOrigin_54 = yOffset;
      feather.xOffset_48 = rand() % 0x361 + 0x100 << 8;
      feather.countCallback1and3Frames_4c = rand() % 13 + 8;
      final int stepOffsetX = feather.xOffset_48 * 2 / feather.countCallback1and3Frames_4c;
      feather.velocityTranslationMagnitudeXz_40 = stepOffsetX;
      feather.accelerationTranslationMagnitudeXz_44 = -stepOffsetX / feather.countCallback1and3Frames_4c;

      final int angleStep;
      if((simpleRand() & 1) != 0) {
        angleStep = 0x5000 - feather.xOffset_48 >> 4 << 1;
      } else {
        //LAB_8010d3b4
        angleStep = -(0x5000 - (feather.xOffset_48 >> 4) << 1);
      }

      //LAB_8010d3cc
      feather.angleStep_60 = MathHelper.psxDegToRad(angleStep) / 0x100;
      feather.translationMagnitudeY_50 = rand() % 0x178 << 8;
      simpleRand();
      feather.spriteAngle_6e = MathHelper.psxDegToRad(rand() % 0x1000);
      feather.translationMagnitudeXz_3c = 0;
      feather.angle_58 = 0;
      feather.angleNoiseXz_5c = MathHelper.psxDegToRad(rand() % 0x1000);
      feather.r_38 = 0x7f;
      feather.g_39 = 0x7f;
      feather.b_3a = 0x7f;
      feather.unused_66 = 0;
      feather.unused_68 = 0;
      feather.unused_6a = 0;
      feather.unused_6c = 0;
    }

    //LAB_8010d4a4
    if((effectFlags & 0xf_ff00) == 0xf_ff00) {
      final SpriteMetrics08 metrics = deffManager_800c693c.spriteMetrics_39c[effectFlags & 0xff];
      featherEffect.u_06 = (short)metrics.u_00;
      featherEffect.v_08 = (short)metrics.v_02;
      featherEffect.width_0a = (short)metrics.w_04;
      featherEffect.height_0c = (short)metrics.h_05;
      featherEffect.clut_0e = (short)metrics.clut_06;
    } else {
      //LAB_8010d508
      final DeffPart.SpriteType spriteType = (DeffPart.SpriteType)deffManager_800c693c.getDeffPart(effectFlags | 0x400_0000);
      final DeffPart.SpriteMetrics deffMetrics = spriteType.metrics_08;
      featherEffect.u_06 = (short)deffMetrics.u_00;
      featherEffect.v_08 = (short)deffMetrics.v_02;
      featherEffect.width_0a = (short)(deffMetrics.w_04 * 4);
      featherEffect.height_0c = (short)deffMetrics.h_06;
      featherEffect.clut_0e = (short)GetClut(deffMetrics.clutX_08, deffMetrics.clutY_0a);
    }

    //LAB_8010d564
    manager.params_10.flags_00 |= 0x5000_0000;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x8010d5b4L)
  public static void renderWsDragoonTransformationFeathersEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> manager) {
    final WsDragoonTransformationFeathersEffect14 effect = (WsDragoonTransformationFeathersEffect14)manager.effect_44;
    final GenericSpriteEffect24 spriteEffect = new GenericSpriteEffect24();

    spriteEffect.flags_00 = manager.params_10.flags_00;
    spriteEffect.x_04 = (short)(-effect.width_0a / 2);
    spriteEffect.y_06 = (short)(-effect.height_0c / 2);
    spriteEffect.w_08 = effect.width_0a;
    spriteEffect.h_0a = effect.height_0c;
    spriteEffect.tpage_0c = (effect.v_08 & 0x100) >>> 4 | (effect.u_06 & 0x3ff) >>> 6;
    spriteEffect.u_0e = (effect.u_06 & 0x3f) * 4;
    spriteEffect.v_0f = effect.v_08;
    spriteEffect.clutX_10 = effect.clut_0e << 4 & 0x3ff;
    spriteEffect.clutY_12 = effect.clut_0e >>> 6 & 0x1ff;

    final Vector3f translation = new Vector3f();

    //LAB_8010d6c8
    for(int i = 0; i < effect.count_00; i++) {
      final WsDragoonTransformationFeatherInstance70 feather = effect.featherArray_10[i];

      if(feather.renderFeather_00) {
        if(feather.r_38 == 0x7f && feather.g_39 == 0x7f && feather.b_3a == 0x7f) {
          spriteEffect.r_14 = manager.params_10.colour_1c.x;
          spriteEffect.g_15 = manager.params_10.colour_1c.y;
          spriteEffect.b_16 = manager.params_10.colour_1c.z;
        } else {
          //LAB_8010d718
          spriteEffect.r_14 = feather.r_38;
          spriteEffect.g_15 = feather.g_39;
          spriteEffect.b_16 = feather.b_3a;
        }

        //LAB_8010d73c
        spriteEffect.scaleX_1c = manager.params_10.scale_16.x;
        spriteEffect.scaleY_1e = manager.params_10.scale_16.y;
        spriteEffect.angle_20 = feather.spriteAngle_6e;
        translation.set(feather.translation_08).add(manager.params_10.trans_04);
        renderGenericSpriteAtZOffset0(spriteEffect, translation);
      }
    }
  }

  @ScriptDescription("Allocates a gold dragoon transformation effect")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "deffFlags", description = "The DEFF flags")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "count", description = "The effect instance count")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "horizontalMax", description = "The maximum position deviation on the XZ plane")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "verticalMin", description = "The minimum deviation on the Y axis")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "verticalMax", description = "The maximum deviation on the Y axis")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p7")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p8")
  @Method(0x8010d7dcL)
  public static FlowControl scriptAllocateGoldDragoonTransformEffect(final RunningScript<? extends BattleObject> script) {
    final int deffFlags = script.params_20[1].get();
    final int count = script.params_20[2].get();
    final int horizontalMin = 32;
    final int horizontalMax = script.params_20[4].get();
    final int verticalMin = script.params_20[5].get();
    final int verticalMax = script.params_20[6].get();
    final int sp2c = script.params_20[7].get();
    final int sp30 = script.params_20[8].get();

    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager(
      "GoldDragoonTransformEffect20",
      script.scriptState_04,
      SEffe::goldDragoonTransformEffectTicker,
      SEffe::goldDragoonTransformEffectRenderer,
      null,
      new GoldDragoonTransformEffect20(count)
    );

    final GoldDragoonTransformEffect20 effect = (GoldDragoonTransformEffect20)state.innerStruct_00.effect_44;

    //LAB_8010d8ec
    for(int i = 0; i < count; i++) {
      final GoldDragoonTransformEffectInstance84 instance = effect.parts_08[i];
      instance.used_00 = true;
      instance.counter_04 = 0;
      instance._68 = 0x7f;
      instance._69 = 0x7f;
      instance._6a = 0x7f;

      final int horizontalOffset = rand() % (horizontalMax - horizontalMin + 1) + horizontalMin;
      final int theta = rand() % 4096;
      instance.transStep_28.x = rcos(theta) * horizontalOffset / (float)0x1000;
      instance.transStep_28.y = rand() % (verticalMax - verticalMin + 1) + verticalMin;
      instance.transStep_28.z = rsin(theta) * horizontalOffset / (float)0x1000;
      instance.rot_38.x = MathHelper.psxDegToRad(rand() % 4096);
      instance.rot_38.y = MathHelper.psxDegToRad(rand() % 4096);
      instance.rot_38.z = MathHelper.psxDegToRad(rand() % 4096);
      instance.rotStep_48.x = MathHelper.psxDegToRad((simpleRand() & 1) != 0 ? rand() % 401 : -(rand() % 401));
      instance.rotStep_48.y = MathHelper.psxDegToRad((simpleRand() & 1) != 0 ? rand() % 401 : -(rand() % 401));
      instance.rotStep_48.z = MathHelper.psxDegToRad((simpleRand() & 1) != 0 ? rand() % 401 : -(rand() % 401));

      if(sp2c != 0) {
        //LAB_8010dbc4
        instance._7c = (short)(rand() % (sp2c + 1));
      } else {
        instance._7c = 0;
      }

      //LAB_8010dbe8
      instance._7e = (short)(rand() % (sp30 + 2));
      instance._80 = -1;

      if((deffFlags & 0xf_ff00) == 0xf_ff00) {
        instance.tmd_70 = deffManager_800c693c.tmds_2f8[deffFlags & 0xff];
      } else {
        //LAB_8010dc40
        final DeffPart.TmdType tmdType = (DeffPart.TmdType)deffManager_800c693c.getDeffPart(deffFlags | 0x300_0000);
        instance.tmd_70 = tmdType.tmd_0c.tmdPtr_00.tmd.objTable[0];
      }

      //LAB_8010dc60
      instance.trans_08.zero();
    }

    //LAB_8010dc8c
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x8010dcd0L)
  public static void goldDragoonTransformEffectTicker(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> manager) {
    final GoldDragoonTransformEffect20 effect = (GoldDragoonTransformEffect20)manager.effect_44;
    int unusedCount = 0;

    //LAB_8010dd00
    for(final GoldDragoonTransformEffectInstance84 instance : effect.parts_08) {
      if(!instance.used_00) {
        unusedCount++;
        continue;
      }

      //LAB_8010dd18
      instance._80++;

      if(instance._7e != -1) {
        if(instance._7e == instance._80) {
          instance._7e = -1;
        }
      }

      //LAB_8010dd40
      if(instance._7e == -1) {
        //LAB_8010dd50
        instance.counter_04++;
        final int counter = instance.counter_04;

        instance.trans_08.x += instance.transStep_28.x;
        instance.trans_08.y = counter * counter * 24 - instance.transStep_28.y * counter;
        instance.trans_08.z += instance.transStep_28.z;

        instance.rot_38.add(instance.rotStep_48);

        if(instance._7c <= 0) {
          if(instance.trans_08.y > 4.0f) {
            instance.used_00 = false;
          }
          //LAB_8010ddf8
        } else if(instance.trans_08.y >= 0.0f) {
          instance.counter_04 = 1;
          instance.transStep_28.y /= 2.0f;
          instance._7c--;
        }
      }
    }

    if(unusedCount >= effect.parts_08.length) {
      //LAB_8010de5c
      state.deallocateWithChildren();
    }

    //LAB_8010de6c
  }

  @Method(0x8010de7cL)
  public static void goldDragoonTransformEffectRenderer(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> manager) {
    final GoldDragoonTransformEffect20 effect = (GoldDragoonTransformEffect20)manager.effect_44;
    effect._04++;

    final ModelPart10 dobj2 = new ModelPart10();
    final Vector3f trans = new Vector3f();
    final MV transforms = new MV();
    final MV sp0x98 = new MV();

    //LAB_8010ded8
    for(final GoldDragoonTransformEffectInstance84 instance : effect.parts_08) {
      if(instance.used_00) {
        trans.set(instance.trans_08).add(manager.params_10.trans_04);

        transforms.rotationXYZ(instance.rot_38);
        transforms.transfer.set(trans);
        transforms.scale(manager.params_10.scale_16);

        dobj2.tmd_08 = instance.tmd_70;

        transforms.compose(worldToScreenMatrix_800c3548, sp0x98);
        GTE.setTransforms(sp0x98);

        zOffset_1f8003e8 = 0;
        tmdGp0Tpage_1f8003ec = 2;

        Renderer.renderDobj2(dobj2, false, 0);
      }
    }

    //LAB_8010e020
  }

  @ScriptDescription("Allocates a Star Children meteor effect")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "deffFlags", description = "The DEFF flags")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "count", description = "The meteor count")
  @Method(0x8010e04cL)
  public static FlowControl scriptAllocateStarChildrenMeteorEffect(final RunningScript<? extends BattleObject> script) {
    final int meteorCount = script.params_20[2].get();
    final int effectFlag = script.params_20[1].get();

    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager(
      "StarChildrenMeteorEffect10",
      script.scriptState_04,
      SEffe::tickStarChildrenMeteorEffect,
      SEffe::renderStarChildrenMeteorEffect,
      null,
      new StarChildrenMeteorEffect10(meteorCount)
    );

    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;
    manager.params_10.flags_00 = 0x5000_0000;

    final StarChildrenMeteorEffect10 meteorEffect = (StarChildrenMeteorEffect10)manager.effect_44;
    final StarChildrenMeteorEffectInstance10[] meteorArray = meteorEffect.meteorArray_0c;

    //LAB_8010e100
    for(int i = 0; i < meteorCount; i++) {
      final StarChildrenMeteorEffectInstance10 meteor = meteorArray[i];
      meteor.centerOffsetX_02 = rand() % 321 - 160;
      meteor.centerOffsetY_04 = rand() % 241 - 120;
      final float sideScale = (rand() % 1025 + 1024) / (float)0x1000;
      meteor.scale_0a = sideScale * 2.0f;
      meteor.scaleW_0c = sideScale;
      meteor.scaleH_0e = sideScale;
    }

    //LAB_8010e1d8
    if((effectFlag & 0xf_ff00) == 0xf_ff00) {
      final SpriteMetrics08 metrics = deffManager_800c693c.spriteMetrics_39c[effectFlag & 0xff];
      meteorEffect.metrics_04.u_00 = metrics.u_00;
      meteorEffect.metrics_04.v_02 = metrics.v_02;
      meteorEffect.metrics_04.w_04 = metrics.w_04;
      meteorEffect.metrics_04.h_05 = metrics.h_05;
      meteorEffect.metrics_04.clut_06 = metrics.clut_06;
    } else {
      //LAB_8010e254
      final DeffPart.SpriteType spriteType = (DeffPart.SpriteType)deffManager_800c693c.getDeffPart(effectFlag | 0x400_0000);
      final DeffPart.SpriteMetrics deffMetrics = spriteType.metrics_08;
      meteorEffect.metrics_04.u_00 = deffMetrics.u_00;
      meteorEffect.metrics_04.v_02 = deffMetrics.v_02;
      meteorEffect.metrics_04.w_04 = deffMetrics.w_04 * 4;
      meteorEffect.metrics_04.h_05 = deffMetrics.h_06;
      meteorEffect.metrics_04.clut_06 = GetClut(deffMetrics.clutX_08, deffMetrics.clutY_0a);
    }

    //LAB_8010e2b0
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x8010e2fcL)
  public static void renderStarChildrenMeteorEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> manager) {
    final StarChildrenMeteorEffect10 meteorEffect = (StarChildrenMeteorEffect10)manager.effect_44;
    final int flags = manager.params_10.flags_00;
    final int tpage = (meteorEffect.metrics_04.v_02 & 0x100) >>> 4 | (meteorEffect.metrics_04.u_00 & 0x3ff) >>> 6;
    final int vramX = (tpage & 0b1111) * 64;
    final int vramY = (tpage & 0b10000) != 0 ? 256 : 0;
    final int leftU = (meteorEffect.metrics_04.u_00 & 0x3f) * 4;
    final int rightU = leftU + meteorEffect.metrics_04.w_04;
    final int bottomV = meteorEffect.metrics_04.v_02;
    final int topV = bottomV + meteorEffect.metrics_04.h_05;
    final int clutX = meteorEffect.metrics_04.clut_06 << 4 & 0x3ff;
    final int clutY = meteorEffect.metrics_04.clut_06 >>> 6 & 0x1ff;
    final int r = manager.params_10.colour_1c.x;
    final int g = manager.params_10.colour_1c.y;
    final int b = manager.params_10.colour_1c.z;
    final StarChildrenMeteorEffectInstance10[] meteorArray = meteorEffect.meteorArray_0c;

    //LAB_8010e414
    for(int i = 0; i < meteorEffect.count_00; i++) {
      final StarChildrenMeteorEffectInstance10 meteor = meteorArray[i];

      final int w = (int)(meteor.scaleW_0c * meteorEffect.metrics_04.w_04);
      final int h = (int)(meteor.scaleH_0e * meteorEffect.metrics_04.h_05);
      final int x = meteor.centerOffsetX_02 - w / 2;
      final int y = meteor.centerOffsetY_04 - h / 2;

      final GpuCommandPoly cmd = new GpuCommandPoly(4)
        .bpp(Bpp.BITS_4)
        .clut(clutX, clutY)
        .vramPos(vramX, vramY)
        .rgb(r, g, b)
        .pos(0, x, y)
        .pos(1, x + w, y)
        .pos(2, x, y + h)
        .pos(3, x + w, y + h)
        .uv(0, leftU, bottomV)
        .uv(1, rightU, bottomV)
        .uv(2, leftU, topV)
        .uv(3, rightU, topV);

      if((flags >>> 30 & 1) != 0) {
        cmd.translucent(Translucency.of(flags >>> 28 & 0b11));
      }

      GPU.queueCommand(30, cmd);
      //LAB_8010e678
    }
    //LAB_8010e694
  }

  @Method(0x8010e6b0L)
  public static void tickStarChildrenMeteorEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> manager) {
    final StarChildrenMeteorEffect10 meteorEffect = (StarChildrenMeteorEffect10)manager.effect_44;

    //LAB_8010e6ec
    final StarChildrenMeteorEffectInstance10[] meteorArray = meteorEffect.meteorArray_0c;
    for(int i = 0; i < meteorEffect.count_00; i++) {
      final StarChildrenMeteorEffectInstance10 meteor = meteorArray[i];
      meteor.centerOffsetX_02 += (short)(MathHelper.sin(manager.params_10.rot_10.x) * 32 * manager.params_10.scale_16.x * meteor.scale_0a);
      meteor.centerOffsetY_04 += (short)(MathHelper.cos(manager.params_10.rot_10.x) * 32 * manager.params_10.scale_16.x * meteor.scale_0a);

      if(meteor.scale_0a * 120 + 50 < meteor.centerOffsetY_04) {
        meteor.centerOffsetY_04 = -120;
        meteor.centerOffsetX_02 = rand() % 321 - 160;
      }

      //LAB_8010e828
      final int centerOffsetX = meteor.centerOffsetX_02;
      if(centerOffsetX > 160) {
        meteor.centerOffsetX_02 = -160;
        //LAB_8010e848
      } else if(centerOffsetX < -160) {
        //LAB_8010e854
        meteor.centerOffsetX_02 = 160;
      }
      //LAB_8010e860
    }
    //LAB_8010e87c
  }

  @ScriptDescription("Allocates a Moon Light stars effect")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "deffFlags", description = "The DEFF flags")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "maxToggleFrameThreshold", description = "The maximum number of frames for each star before toggling visibility")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "maxScale", description = "The maximum scale multiplier for each star")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "count", description = "The number of stars")
  @Method(0x8010e89cL)
  public static FlowControl scriptAllocateMoonlightStarsEffect(final RunningScript<? extends BattleObject> script) {
    final int starCount = script.params_20[4].get();
    final int effectFlags = script.params_20[1].get();
    final int maxToggleFrameThreshold = script.params_20[2].get();
    final int maxScale = script.params_20[3].get();

    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager(
      "MoonlightStarsEffect18",
      script.scriptState_04,
      SEffe::tickMoonlightStarsEffect,
      SEffe::renderMoonlightStarsEffect,
      null,
      new MoonlightStarsEffect18(starCount)
    );

    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;
    final MoonlightStarsEffect18 starEffect = (MoonlightStarsEffect18)manager.effect_44;
    manager.params_10.flags_00 = 0x5000_0000;

    //LAB_8010e980
    for(int i = 0; i < starCount; i++) {
      final MoonlightStarsEffectInstance3c star = starEffect.starArray_0c[i];
      star.currentFrame_00 = 0;
      star.renderStars_03 = true;
      star.maxToggleFrameThreshold_36 = maxToggleFrameThreshold;
      star.toggleOffFrameThreshold_38 = seed_800fa754.nextInt(181);
      final int scale = seed_800fa754.nextInt(maxScale + 1);
      final float angle = seed_800fa754.nextFloat(MathHelper.TWO_PI);
      final float sin = MathHelper.sin(angle);
      final float cos = MathHelper.cosFromSin(sin, angle);
      final float x = (cos - sin) * scale;
      final float z = (cos + sin) * scale;
      star.translation_04.set(x, 0.0f, z);
    }

    //LAB_8010ead0
    if((effectFlags & 0xf_ff00) == 0xf_ff00) {
      final SpriteMetrics08 metrics = deffManager_800c693c.spriteMetrics_39c[effectFlags & 0xff];
      starEffect.metrics_04.u_00 = metrics.u_00;
      starEffect.metrics_04.v_02 = metrics.v_02;
      starEffect.metrics_04.w_04 = metrics.w_04;
      starEffect.metrics_04.h_05 = metrics.h_05;
      starEffect.metrics_04.clut_06 = metrics.clut_06;
    } else {
      //LAB_8010eb50
      final DeffPart.SpriteType spriteType = (DeffPart.SpriteType)deffManager_800c693c.getDeffPart(effectFlags | 0x400_0000);
      final DeffPart.SpriteMetrics deffMetrics = spriteType.metrics_08;
      starEffect.metrics_04.u_00 = deffMetrics.u_00;
      starEffect.metrics_04.v_02 = deffMetrics.v_02;
      starEffect.metrics_04.w_04 = deffMetrics.w_04 * 4;
      starEffect.metrics_04.h_05 = deffMetrics.h_06;
      starEffect.metrics_04.clut_06 = GetClut(deffMetrics.clutX_08, deffMetrics.clutY_0a);
    }

    //LAB_8010ebac
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x8010ec08L)
  public static void renderMoonlightStarsEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> manager) {
    final MoonlightStarsEffect18 starEffect = (MoonlightStarsEffect18)manager.effect_44;

    final GenericSpriteEffect24 spriteEffect = new GenericSpriteEffect24();
    spriteEffect.flags_00 = manager.params_10.flags_00;
    spriteEffect.x_04 = (short)(-starEffect.metrics_04.w_04 / 2);
    spriteEffect.y_06 = (short)(-starEffect.metrics_04.h_05 / 2);
    spriteEffect.w_08 = starEffect.metrics_04.w_04;
    spriteEffect.h_0a = starEffect.metrics_04.h_05;
    spriteEffect.tpage_0c = (starEffect.metrics_04.v_02 & 0x100) >>> 4 | (starEffect.metrics_04.u_00 & 0x3ff) >>> 6;
    spriteEffect.u_0e = (starEffect.metrics_04.u_00 & 0x3f) << 2;
    spriteEffect.v_0f = starEffect.metrics_04.v_02;
    spriteEffect.clutX_10 = starEffect.metrics_04.clut_06 << 4 & 0x3ff;
    spriteEffect.clutY_12 = starEffect.metrics_04.clut_06 >>> 6 & 0x1ff;

    final Vector3f translation = new Vector3f();

    //LAB_8010ed00
    for(int i = 0; i < starEffect.count_00; i++) {
      final MoonlightStarsEffectInstance3c star = starEffect.starArray_0c[i];

      // If a star is set not to render, do not render subsequent stars either.
      if(!star.renderStars_03) {
        break;
      }

      spriteEffect.r_14 = manager.params_10.colour_1c.x;
      spriteEffect.g_15 = manager.params_10.colour_1c.y;
      spriteEffect.b_16 = manager.params_10.colour_1c.z;
      spriteEffect.scaleX_1c = manager.params_10.scale_16.x;
      spriteEffect.scaleY_1e = manager.params_10.scale_16.y;
      spriteEffect.angle_20 = manager.params_10.rot_10.x;
      translation.set(manager.params_10.trans_04).add(star.translation_04);
      renderGenericSpriteAtZOffset0(spriteEffect, translation);
    }

    //LAB_8010edac
  }

  @ScriptDescription("Allocates a Star Children impact effect")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "count", description = "The impact count")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "maxStartingFrame", description = "The maximum random frame each impact can start on")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "maxTranslationMagnitude", description = "The maximum random multiplier for each impact's X and Z coordinates")
  @Method(0x8010edc8L)
  public static FlowControl scriptAllocateStarChildrenImpactEffect(final RunningScript<? extends BattleObject> script) {
    final int impactCount = script.params_20[1].get();
    final int maxStartingFrame = script.params_20[2].get();
    final int maxTranslationMagnitude = script.params_20[3].get();

    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager(
      "StarChildrenImpactEffect20",
      script.scriptState_04,
      SEffe::tickStarChildrenImpactEffect,
      SEffe::renderStarChildrenImpactEffect,
      null,
      new StarChildrenImpactEffect20(impactCount)
    );

    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;
    final StarChildrenImpactEffect20 impactEffect = (StarChildrenImpactEffect20)manager.effect_44;

    //LAB_8010eecc
    for(int i = 0; i < impactCount; i++) {
      final StarChildrenImpactEffectInstancea8 impact = impactEffect.impactArray_08[i];

      impact.renderImpact_00 = false;
      impact.renderShockwave_01 = false;
      impact.startingFrame_04 = rand() % maxStartingFrame + 1;
      final int translationMagnitude = rand() % (maxTranslationMagnitude + 1);
      final float angle = MathHelper.psxDegToRad(rand() % 4096);
      final float sin = MathHelper.sin(angle);
      final float cos = MathHelper.cosFromSin(sin, angle);
      impact.unused_08 = rand() % maxStartingFrame + 1;
      impact.translation_0c[0].x = (cos - sin) * translationMagnitude;
      impact.translation_0c[0].y = 0;
      impact.translation_0c[0].z = (cos + sin) * translationMagnitude;
      impact.translation_0c[1].x = impact.translation_0c[0].x;
      impact.translation_0c[1].y = impact.translation_0c[0].y - 0x100;
      impact.translation_0c[1].z = impact.translation_0c[0].z;
      impact.rotation_2c[0].set(0.0f, MathHelper.psxDegToRad(rand() % 4096), 0.0f);
      impact.rotation_2c[1].set(0.0f, MathHelper.psxDegToRad(rand() % 4096), 0.0f);
      impact.scale_6c[0].zero();
      impact.scale_6c[1].set(0.75f, 0.25f, 0.75f);
      impact.opacity_8c[0].set(0xff, 0xff, 0xff);
      impact.opacity_8c[1].set(0xff, 0xff, 0xff);
      impact.explosionObjTable_94 = ((DeffPart.TmdType)deffManager_800c693c.getDeffPart(0x300_7100)).tmd_0c.tmdPtr_00.tmd.objTable[0];
      impact.shockwaveObjTable_98 = ((DeffPart.TmdType)deffManager_800c693c.getDeffPart(0x300_7101)).tmd_0c.tmdPtr_00.tmd.objTable[0];
      impact.plumeObjTable_9c = ((DeffPart.TmdType)deffManager_800c693c.getDeffPart(0x300_7103)).tmd_0c.tmdPtr_00.tmd.objTable[0];
      impact.explosionHeightAngle_a0 = 0.0f;
      impact.animationFrame_a2 = 0;
    }

    //LAB_8010f0d0
    manager.params_10.flags_00 = 0x1400_0000;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x8010f124L)
  public static void tickStarChildrenImpactEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> manager) {
    final StarChildrenImpactEffect20 impactEffect = (StarChildrenImpactEffect20)manager.effect_44;

    //LAB_8010f168
    for(int i = 0; i < impactEffect.impactArray_08.length; i++) {
      final StarChildrenImpactEffectInstancea8 impact = impactEffect.impactArray_08[i];

      if(impactEffect.currentFrame_04 > impact.startingFrame_04) {
        if(impact.explosionHeightAngle_a0 >= MathHelper.TWO_PI) {
          impact.renderImpact_00 = false;
        } else {
          //LAB_8010f19c
          final int currentAnimFrame = impact.animationFrame_a2;

          // Stage 0
          if(currentAnimFrame < 9) {
            //LAB_8010f240
            impact.renderImpact_00 = true;
            impact.renderShockwave_01 = true;
            impact.rotation_2c[0].y += MathHelper.TWO_PI / 32.0f;
            impact.scale_6c[0].x += 0.15f;
            impact.scale_6c[0].z += 0.15f;
            impact.explosionHeightAngle_a0 += MathHelper.TWO_PI / 20.0f;

            final float explosionHeight = MathHelper.sin(impact.explosionHeightAngle_a0) * 1.375f;
            impact.scale_6c[0].y = Math.max(explosionHeight, 0.0f);

            //LAB_8010f2a8
            impact.opacity_8c[0].x -= 23;
            impact.opacity_8c[0].y -= 23;
            impact.opacity_8c[0].z -= 23;
          } else { // Stage 1
            if(currentAnimFrame == 9) {
              impact.translation_0c[0].y = -0x800;
              impact.scale_6c[0].x = 6.5f;
              impact.scale_6c[0].z = 6.5f;
            }

            //LAB_8010f1dc
            // Start transforming plume
            if(currentAnimFrame >= 10) {
              if(currentAnimFrame == 10) {
                impact.renderShockwave_01 = false;
              }

              //LAB_8010f1f0
              impact.rotation_2c[1].y += MathHelper.TWO_PI / 32.0f;
              impact.explosionHeightAngle_a0 += MathHelper.TWO_PI / 8.0f;
              impact.scale_6c[1].x -= 0.109375f;
              impact.scale_6c[1].y += 0.375f;
              impact.scale_6c[1].z -= 0.109375f;
            }
            //LAB_8010f22c
          }
          impact.animationFrame_a2++;
        }
      }
    }

    //LAB_8010f2f4
    impactEffect.currentFrame_04++;

    if(impactEffect.impactArray_08.length == 0) {
      state.deallocateWithChildren();
    }
    //LAB_8010f31c
  }

  /** Used renderCtmd */
  @Method(0x8010f340L)
  public static void renderStarChildrenImpactEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> manager) {
    final StarChildrenImpactEffect20 impactEffect = (StarChildrenImpactEffect20)manager.effect_44;
    if(manager.params_10.flags_00 >= 0) {
      final Vector3f translation = new Vector3f();
      final MV transformMatrix1 = new MV();
      final MV finalTransformMatrix1 = new MV();
      final Vector3f scale = new Vector3f();
      final MV transformMatrix0 = new MV();

      final ModelPart10 dobj = new ModelPart10();

      //LAB_8010f3a4
      for(int i = 0; i < impactEffect.impactArray_08.length; i++) {
        final StarChildrenImpactEffectInstancea8 impact = impactEffect.impactArray_08[i];

        if(impact.renderImpact_00) {
          calculateEffectTransforms(transformMatrix0, manager);
          final int stageNum = impact.animationFrame_a2 >= 10 ? 1 : 0;
          translation.set(impact.translation_0c[stageNum]).add(manager.params_10.trans_04);
          final float scaleX = impact.scale_6c[stageNum].x * manager.params_10.scale_16.x;
          final float scaleY = impact.scale_6c[stageNum].y * manager.params_10.scale_16.y;
          final float scaleZ = impact.scale_6c[stageNum].z * manager.params_10.scale_16.z;
          final int r = impact.opacity_8c[stageNum].x * manager.params_10.colour_1c.x >> 8;
          final int g = impact.opacity_8c[stageNum].y * manager.params_10.colour_1c.y >> 8;
          final int b = impact.opacity_8c[stageNum].z * manager.params_10.colour_1c.z >> 8;

          if((manager.params_10.flags_00 & 0x40) == 0) {
            FUN_800e61e4(r / 128.0f, g / 128.0f, b / 128.0f);
          }

          //LAB_8010f50c
          GsSetLightMatrix(transformMatrix0);
          transformMatrix1.rotationXYZ(impact.rotation_2c[stageNum]);
          transformMatrix1.transfer.set(translation);
          scale.set(scaleX, scaleY, scaleZ);
          transformMatrix1.scale(scale);
          dobj.attribute_00 = manager.params_10.flags_00;
          transformMatrix1.compose(worldToScreenMatrix_800c3548, finalTransformMatrix1);
          GTE.setTransforms(finalTransformMatrix1);
          zOffset_1f8003e8 = 0;
          tmdGp0Tpage_1f8003ec = manager.params_10.flags_00 >>> 23 & 0x60;

          final int oldZShift = zShift_1f8003c4;
          final int oldZMax = zMax_1f8003cc;
          final int oldZMin = zMin;
          zShift_1f8003c4 = 2;
          zMax_1f8003cc = 0xffe;
          zMin = 0xb;

          if(impact.renderShockwave_01) {
            dobj.tmd_08 = impact.shockwaveObjTable_98;
            Renderer.renderDobj2(dobj, false, 0x20);
          }

          //LAB_8010f5d0
          if(impact.animationFrame_a2 < 9) {
            dobj.tmd_08 = impact.explosionObjTable_94;
            Renderer.renderDobj2(dobj, false, 0x20);
          } else if(impact.animationFrame_a2 >= 11) {
            dobj.tmd_08 = impact.plumeObjTable_9c;
            Renderer.renderDobj2(dobj, false, 0x20);
          }

          zShift_1f8003c4 = oldZShift;
          zMax_1f8003cc = oldZMax;
          zMin = oldZMin;

          //LAB_8010f608
          if((manager.params_10.flags_00 & 0x40) == 0) {
            FUN_800e62a8();
          }
        }
      }
    }
    //LAB_8010f640
  }

  @Method(0x8010f978L)
  public static void tickWsDragoonTransformationFeathersEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> manager) {
    final WsDragoonTransformationFeathersEffect14 effect = (WsDragoonTransformationFeathersEffect14)manager.effect_44;

    //LAB_8010f9c0
    for(int i = 0; i < effect.count_00; i++) {
      final WsDragoonTransformationFeatherInstance70 feather = effect.featherArray_10[i];
      WsDragoonTransformationFeatherCallbacks_80119ff4[feather.callbackIndex_02].accept(manager, feather);
    }
    //LAB_8010f9fc
  }

  @Method(0x8010fa4cL)
  public static void initializeWsDragoonTransformationEffect(final EffectManagerData6c<EffectManagerParams.VoidType> manager, final WsDragoonTransformationFeatherInstance70 feather) {
    feather.currentFrame_04++;

    if(feather.currentFrame_04 >= feather.countCallback0Frames_64) {
      feather.renderFeather_00 = true;
      feather.currentFrame_04 = 0;
      feather.callbackIndex_02++;
    }
  }

  @Method(0x8010fa88L)
  public static void expandWsDragoonTransformationEffect(final EffectManagerData6c<EffectManagerParams.VoidType> manager, final WsDragoonTransformationFeatherInstance70 feather) {
    feather.angle_58 += feather.angleStep_60;
    feather.angle_58 %= MathHelper.TWO_PI;

    feather.velocityTranslationMagnitudeXz_40 += feather.accelerationTranslationMagnitudeXz_44;
    feather.translationMagnitudeXz_3c += feather.velocityTranslationMagnitudeXz_40;

    final int x = (int)((MathHelper.cos(feather.angle_58 + feather.angleNoiseXz_5c) - MathHelper.sin(feather.angle_58 + feather.angleNoiseXz_5c)) * (feather.translationMagnitudeXz_3c >> 8));
    final int y = (int)(feather.yOrigin_54 + feather.translationMagnitudeY_50 * MathHelper.sin(feather.angle_58));
    final int z = (int)((MathHelper.cos(feather.angle_58 + feather.angleNoiseXz_5c) + MathHelper.sin(feather.angle_58 + feather.angleNoiseXz_5c)) * (feather.translationMagnitudeXz_3c >> 8));
    feather.translation_08.set(x, y, z);

    feather.currentFrame_04++;
    if(feather.currentFrame_04 >= feather.countCallback1and3Frames_4c) {
      feather.currentFrame_04 = 0;
      feather.callbackIndex_02++;
    }
    //LAB_8010fbc0
  }

  @Method(0x8010fbd4L)
  public static void spinWsDragoonTransformationEffect(final EffectManagerData6c<EffectManagerParams.VoidType> manager, final WsDragoonTransformationFeatherInstance70 feather) {
    feather.angle_58 += feather.angleStep_60;
    feather.angle_58 %= MathHelper.TWO_PI;

    final int x = (int)((MathHelper.cos(feather.angle_58 + feather.angleNoiseXz_5c) - MathHelper.sin(feather.angle_58 + feather.angleNoiseXz_5c)) * (feather.translationMagnitudeXz_3c >> 8));
    final int y = (int)(feather.yOrigin_54 + feather.translationMagnitudeY_50 * MathHelper.sin(feather.angle_58));
    final int z = (int)((MathHelper.cos(feather.angle_58 + feather.angleNoiseXz_5c) + MathHelper.sin(feather.angle_58 + feather.angleNoiseXz_5c)) * (feather.translationMagnitudeXz_3c >> 8));
    feather.translation_08.set(x, y, z);

    feather.currentFrame_04++;
    if(feather.currentFrame_04 >= 0xf) {
      feather.currentFrame_04 = 0;
      feather.callbackIndex_02++;
      feather.velocityTranslationMagnitudeXz_40 = -feather.translationMagnitudeXz_3c / feather.countCallback1and3Frames_4c;
      feather.velocityTranslationMagnitudeY_1c = -feather.translationMagnitudeY_50 / feather.countCallback1and3Frames_4c;
    }
    //LAB_8010fd20
  }

  @Method(0x8010fd34L)
  public static void contractWsDragoonTransformationEffect(final EffectManagerData6c<EffectManagerParams.VoidType> manager, final WsDragoonTransformationFeatherInstance70 feather) {
    feather.angle_58 += feather.angleStep_60;
    feather.angle_58 %= MathHelper.TWO_PI;
    feather.translationMagnitudeXz_3c += feather.velocityTranslationMagnitudeXz_40;

    final int x = (int)((MathHelper.cos(feather.angle_58 + feather.angleNoiseXz_5c) - MathHelper.sin(feather.angle_58 + feather.angleNoiseXz_5c)) * (feather.translationMagnitudeXz_3c >> 8));
    final int y = (int)(feather.yOrigin_54 + feather.translationMagnitudeY_50 * MathHelper.sin(feather.angle_58));
    final int z = (int)((MathHelper.cos(feather.angle_58 + feather.angleNoiseXz_5c) + MathHelper.sin(feather.angle_58 + feather.angleNoiseXz_5c)) * (feather.translationMagnitudeXz_3c >> 8));
    feather.translation_08.set(x, y, z);
    feather.translationMagnitudeY_50 += feather.velocityTranslationMagnitudeY_1c;

    feather.currentFrame_04++;
    if(feather.currentFrame_04 >= feather.countCallback1and3Frames_4c) {
      feather.renderFeather_00 = false;
      feather.currentFrame_04 = 0;
      feather.callbackIndex_02++;
    }
    //LAB_8010fe70
  }

  @Method(0x8010fe84L)
  public static void WsDragoonTransformationCallback4(final EffectManagerData6c<EffectManagerParams.VoidType> manager, final WsDragoonTransformationFeatherInstance70 a2) {
    // no-op
  }

  @Method(0x8010ff10L)
  public static void tickMoonlightStarsEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> manager) {
    final MoonlightStarsEffect18 starEffect = (MoonlightStarsEffect18)manager.effect_44;

    //LAB_8010ff34
    for(int i = 0; i < starEffect.count_00; i++) {
      final MoonlightStarsEffectInstance3c star = starEffect.starArray_0c[i];

      // Seems like stars stop rendering when the current frame exceeds a randomized threshold.
      // The threshold is then re-randomized each tick until the current frame falls below the
      // threshold again, and then the star is rendered again.
      star.currentFrame_00++;
      if(star.currentFrame_00 > star.toggleOffFrameThreshold_38) {
        star.renderStars_03 = false;
        star.currentFrame_00 = 0;
        star.toggleOffFrameThreshold_38 = (short)seed_800fa754.nextInt(star.maxToggleFrameThreshold_36 + 1);
      } else {
        //LAB_8010ffb0
        star.renderStars_03 = true;
      }
    }
  }

  /** Returns reference */
  @Method(0x80110074L)
  public static Vector3f getScriptedObjectRotation(final int scriptIndex) {
    final BattleObject bobj = (BattleObject)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00;
    return bobj.getRotation();
  }

  /** Gets rotation between two vectors (used in the item throwing parabolic) */
  @Method(0x80110120L)
  public static Vector3f FUN_80110120(final Vector3f out, @Nullable Vector3f translation, final Vector3f parentTranslation) {
    if(translation == null) {
      translation = new Vector3f();
    }

    final Vector3f translationDelta = new Vector3f(parentTranslation).sub(translation);
    out.z = 0.0f;
    out.y = MathHelper.atan2(translationDelta.x, translationDelta.z);

    final float s1 = MathHelper.cos(-out.y) * translationDelta.z - MathHelper.sin(-out.y) * translationDelta.x;
    out.x = MathHelper.atan2(-translationDelta.y, s1);
    return out;
  }

  /** Gets rotation between two position vectors (converts rotation from ZYX to XYZ rotation) */
  @Method(0x80110228L)
  public static Vector3f calculateRelativeAngleBetweenPositions(final Vector3f out, final Vector3f pos1, final Vector3f pos2) {
    final Vector3f delta = new Vector3f(pos2).sub(pos1).negate();

    out.y = MathHelper.atan2(delta.x, delta.z); // Angle from the X axis

    final float sin = MathHelper.sin(-out.y);
    final float cos = MathHelper.cosFromSin(sin, -out.y);

    final float s1 = cos * delta.z - sin * delta.x; // Hypotenuse rotated to be parallel with X axis
    out.x = MathHelper.atan2(-delta.y, s1); // Angle from XZ plane
    out.z = 0.0f;

    // Convert from ZYX rotation to XYZ
    final MV transforms = new MV();
    transforms.rotationZYX(out);
    getRotationFromTransforms(out, transforms);

    return out;
  }

  /** I'm like 96% sure this name is correct */
  @Method(0x80110488L)
  public static void getEffectTranslationRelativeToParent(final int scriptIndex, final int parentIndex, final Vector3f out) {
    final MV transforms = new MV();
    calculateEffectTransforms(transforms, (EffectManagerData6c<?>)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00);

    if(parentIndex == -1) {
      out.set(transforms.transfer);
    } else {
      //LAB_80110500
      final MV parentTransforms = new MV();
      calculateEffectTransforms(parentTransforms, (EffectManagerData6c<?>)scriptStatePtrArr_800bc1c0[parentIndex].innerStruct_00);
      transforms.transfer.sub(parentTransforms.transfer);

      parentTransforms.transpose();
      transforms.transfer.mul(parentTransforms, out);
    }

    //LAB_80110594
  }

  /** Ticks translation scaler (relative to parent if specified) */
  @Method(0x80110740L)
  public static int tickPositionScalerAttachment(final EffectManagerData6c<?> manager, final TransformScalerAttachment34 scaler) {
    scaler.velocity_18.add(scaler.acceleration_24);
    scaler.value_0c.add(scaler.velocity_18);

    if(scaler.parent_30 == null) {
      manager.params_10.trans_04.set(scaler.value_0c);
    } else {
      //LAB_80110814
      final MV rotMatrix = new MV();
      rotMatrix.rotationXYZ(scaler.parent_30.getRotation());

      scaler.value_0c.mul(rotMatrix, manager.params_10.trans_04);
      manager.params_10.trans_04.add(scaler.parent_30.getPosition());
    }

    //LAB_801108bc
    if(scaler.ticksRemaining_32 != -1) {
      scaler.ticksRemaining_32--;

      if(scaler.ticksRemaining_32 <= 0) {
        //LAB_801108e4
        return 0;
      }
    }

    //LAB_801108e0
    return 1;
  }

  @Method(0x801108fcL)
  public static TransformScalerAttachment34 addPositionScalerAttachment(final EffectManagerData6c<?> manager, @Nullable final BattleObject parent, final float velX, final float velY, final float velZ, final float accX, final float accY, final float accZ) {
    if(manager.hasAttachment(1)) {
      manager.removeAttachment(1);
    }

    final Vector3f managerPos = manager.getPosition();

    //LAB_80110980
    final TransformScalerAttachment34 attachment = manager.addAttachment(1, 0, SEffe::tickPositionScalerAttachment, new TransformScalerAttachment34());
    attachment.value_0c.set(managerPos);
    attachment.parent_30 = null;
    attachment.ticksRemaining_32 = -1;

    attachment.velocity_18.set(velX, velY, velZ);
    attachment.acceleration_24.set(accX, accY, accZ);
    if(parent != null) {
      final MV rotation = new MV();
      rotation.rotationXYZ(parent.getRotation());
      attachment.velocity_18.mul(rotation);
      attachment.acceleration_24.mul(rotation);
    }

    //LAB_80110a5c
    return attachment;
  }

  /** Sets position scaler using ticks (optionally relative to a parent script) */
  @Method(0x80110aa8L)
  public static TransformScalerAttachment34 addRelativePositionScalerTicks(final int mode, final int effectIndex, final int parentIndex, final int ticks, final int x, final int y, final int z) {
    if(ticks < 0) {
      return null;
    }

    //LAB_80110afc
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(effectIndex, EffectManagerData6c.class);
    final BattleObject parent = SCRIPTS.getObject(parentIndex, BattleObject.class);

    if(manager.hasAttachment(1)) {
      manager.removeAttachment(1);
    }

    final Vector3f velocity = new Vector3f();

    //LAB_80110b38
    final TransformScalerAttachment34 translationScaler = manager.addAttachment(1, 0, SEffe::tickPositionScalerAttachment, new TransformScalerAttachment34());
    if(parent == null) {
      velocity.set(x, y, z)
        .sub(manager.getPosition());
      //LAB_80110b9c
    } else if(mode == 0) {  // XYZ minus script 1 translation + script 2 translation
      //LAB_80110bc0
      velocity.set(x, y, z)
        .sub(manager.getPosition())
        .add(parent.getPosition());
    } else if(mode == 1) {  // XYZ minus script 1 translation + script 2 translation with rotation
      //LAB_80110c0c
      parent.getRelativePosition(new Vector3f(x, y, z), velocity);
      velocity
        .sub(manager.getPosition());
    }

    //LAB_80110c6c
    translationScaler.parent_30 = null;
    translationScaler.ticksRemaining_32 = ticks;

    translationScaler.value_0c.set(manager.params_10.trans_04);

    if(ticks != 0) {
      translationScaler.velocity_18.set(velocity).div(ticks);
    } else {
      translationScaler.velocity_18.zero();
    }

    translationScaler.acceleration_24.zero();

    //LAB_80110d04
    return translationScaler;
  }

  /** Sets position scaler using distance (optionally relative to a parent script) */
  @Method(0x80110d34L)
  public static TransformScalerAttachment34 addRelativePositionScalerDistance(final int mode, final int scriptIndex, final int parentIndex, final float distancePerTick, final int x, final int y, final int z) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(scriptIndex, EffectManagerData6c.class);
    final BattleObject parent = SCRIPTS.getObject(parentIndex, BattleObject.class);

    if(manager.hasAttachment(1)) {
      manager.removeAttachment(1);
    }

    //LAB_80110db8
    final TransformScalerAttachment34 translationScaler = manager.addAttachment(1, 0, SEffe::tickPositionScalerAttachment, new TransformScalerAttachment34());

    if(distancePerTick <= 0) {
      translationScaler.ticksRemaining_32 = -1;
      translationScaler.velocity_18.zero();
    } else {
      //LAB_80110e30
      final Vector3f velocity = new Vector3f();

      if(parent == null) {
        velocity.set(x, y, z)
          .sub(manager.getPosition());
        //LAB_80110e70
      } else if(mode == 0) {
        //LAB_80110e94
        velocity.set(x, y, z)
          .sub(manager.getPosition())
          .add(parent.getPosition());
      } else if(mode == 1) {
        //LAB_80110ee0
        parent.getRelativePosition(new Vector3f(x, y, z), velocity);
        velocity
          .sub(manager.getPosition());
      }

      //LAB_80110f38
      translationScaler.parent_30 = null;
      translationScaler.ticksRemaining_32 = (int)Math.max(1, velocity.length() / distancePerTick);

      translationScaler.value_0c.set(manager.params_10.trans_04);

      //LAB_80110f80
      translationScaler.velocity_18.set(velocity).div(translationScaler.ticksRemaining_32);
    }

    //LAB_80110fec
    translationScaler.acceleration_24.zero();
    return translationScaler;
  }

  @Method(0x8011102cL)
  public static TransformScalerAttachment34 addPositionScalerMoveToParent(final int effectIndex, final int parentIndex) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(effectIndex, EffectManagerData6c.class);
    final BattleObject parent = SCRIPTS.getObject(parentIndex, BattleObject.class);

    if(manager.hasAttachment(1)) {
      manager.removeAttachment(1);
    }

    //LAB_80111084
    final TransformScalerAttachment34 effect = manager.addAttachment(1, 0, SEffe::tickPositionScalerAttachment, new TransformScalerAttachment34());
    final Vector3f positionDelta = new Vector3f(manager.getPosition()).sub(parent.getPosition());
    effect.value_0c.set(positionDelta);
    effect.parent_30 = parent;
    effect.ticksRemaining_32 = -1;
    effect.velocity_18.zero();
    effect.acceleration_24.zero();
    return effect;
  }

  @ScriptDescription("A bugged effect, do not use")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p3")
  @Method(0x801114b8L)
  public static FlowControl scriptAllocateBuggedEffect(final RunningScript<?> script) {
    throw new RuntimeException("Bugged effect allocator");
  }

  @ScriptDescription("Calculates the relative offset from one battle object to its parent (or just returns the first object's position if the parent is -1)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bobjIndex", description = "The battle object index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "parentIndex", description = "The battle object parent index (-1 for none)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The calculated X position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The calculated Y position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The calculated Z position")
  @Method(0x801115ecL)
  public static FlowControl scriptGetRelativePosition(final RunningScript<?> script) {
    final int bobjIndex1 = script.params_20[0].get();
    final int bobjIndex2 = script.params_20[1].get();

    final BattleObject bobj = (BattleObject)scriptStatePtrArr_800bc1c0[bobjIndex1].innerStruct_00;

    if(bobjIndex2 == -1) {
      final Vector3f pos = bobj.getPosition();
      script.params_20[2].set(Math.round(pos.x));
      script.params_20[3].set(Math.round(pos.y));
      script.params_20[4].set(Math.round(pos.z));
    } else {
      final BattleObject parent = (BattleObject)scriptStatePtrArr_800bc1c0[bobjIndex2].innerStruct_00;
      final Vector3f pos = bobj.getRelativePositionFrom(parent, new Vector3f());
      script.params_20[2].set(Math.round(pos.x));
      script.params_20[3].set(Math.round(pos.y));
      script.params_20[4].set(Math.round(pos.z));
    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Calculates the relative offset from one effect to its parent (or just returns the first effect's position if the parent is -1)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "parentIndex", description = "The parent index (-1 for none)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The calculated X position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The calculated Y position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The calculated Z position")
  @Method(0x80111658L)
  public static FlowControl scriptGetEffectTranslationRelativeToParent(final RunningScript<?> script) {
    final Vector3f translation = new Vector3f();
    getEffectTranslationRelativeToParent(script.params_20[0].get(), script.params_20[1].get(), translation);
    script.params_20[2].set(Math.round(translation.x));
    script.params_20[3].set(Math.round(translation.y));
    script.params_20[4].set(Math.round(translation.z));
    return FlowControl.CONTINUE;
  }

  @Method(0x801116c4L)
  public static MV FUN_801116c4(final MV a0, final int scriptIndex, final int partIndex) {
    final Vector3f trans = new Vector3f();
    final Vector3f scale = new Vector3f();
    final MV transforms = new MV();
    final BattleObject s0 = (BattleObject)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00;
    if(BattleObject.EM__.equals(s0.magic_00)) {
      final EffectManagerData6c<EffectManagerParams.AnimType> effects = (EffectManagerData6c<EffectManagerParams.AnimType>)s0;

      //LAB_8011172c
      final MV sp0x10 = new MV();
      calculateEffectTransforms(sp0x10, effects);
      final int type = effects.flags_04 & 0xff00_0000;

      //LAB_80111768
      if(type == 0x100_0000 || type == 0x200_0000) {
        //LAB_80111998
        final ModelEffect13c struct13c = (ModelEffect13c)effects.effect_44;
        final Model124 model = struct13c.model_134;
        model.coord2_14.flg = 0;
        model.coord2_14.coord.set(sp0x10);

        //LAB_80111a0c
        final GsCOORDINATE2 coord2 = model.modelParts_00[partIndex].coord2_04;
        GsGetLw(coord2, a0);
        coord2.flg = 0;
      } else if(type == 0) {
        //LAB_80111778
        final LmbAnimationEffect5c a2_0 = (LmbAnimationEffect5c)effects.effect_44;

        if((a2_0.lmbType_00 & 0x7) == 0) {
          final LmbType0 lmb = (LmbType0)a2_0.lmb_0c;

          //LAB_801117ac
          final int frameIndex = Math.max(0, effects.params_10.ticks_24) % (a2_0.keyframeCount_08 * 2);
          final int keyframeIndex = frameIndex / 2;
          final LmbTransforms14 lmbTransforms = lmb.partAnimations_08[partIndex].keyframes_08[keyframeIndex];
          scale.set(lmbTransforms.scale_00);
          trans.set(lmbTransforms.trans_06);

          if((frameIndex & 0x1) != 0) {
            int nextKeyframeIndex = keyframeIndex + 1;

            if(nextKeyframeIndex == a2_0.keyframeCount_08) {
              nextKeyframeIndex = 0;
            }

            //LAB_8011188c
            final LmbTransforms14 nextKeyframe = lmb.partAnimations_08[partIndex].keyframes_08[nextKeyframeIndex];
            scale.add(nextKeyframe.scale_00).div(2);
            trans.add(nextKeyframe.trans_06).div(2);
          }

          //LAB_80111958
          transforms.rotationZYX(lmbTransforms.rot_0c);
          transforms.transfer.set(trans);
          transforms.scaleLocal(scale);
          transforms.compose(sp0x10, a0);
        }
      }
    } else {
      final Model124 model = ((BattleEntity27c)s0).model_148;
      applyModelRotationAndScale(model);
      final GsCOORDINATE2 coord2 = model.modelParts_00[partIndex].coord2_04;
      GsGetLw(coord2, a0);
      coord2.flg = 0;
    }

    //LAB_80111a3c
    return a0;
  }

  @ScriptDescription("Unknown, returns a position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "modelPartIndex", description = "The model part index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The X position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z position")
  @Method(0x80111a58L)
  public static FlowControl FUN_80111a58(final RunningScript<?> script) {
    final int partIndex = script.params_20[1].get();
    if(partIndex == -1) {
      //LAB_80111acc
      return scriptGetEffectTranslationRelativeToParent(script);
    }

    final MV sp0x20 = new MV();
    FUN_801116c4(sp0x20, script.params_20[0].get(), partIndex);
    script.params_20[2].set(Math.round(sp0x20.transfer.x));
    script.params_20[3].set(Math.round(sp0x20.transfer.y));
    script.params_20[4].set(Math.round(sp0x20.transfer.z));

    //LAB_80111ad4
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Translates a battle object relative to its parent, if one is specified")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bobjIndex1", description = "The battle object script index 1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bobjIndex2", description = "The battle object script index 2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The calculated X position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The calculated Y position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The calculated Z position")
  @Method(0x80111ae4L)
  public static FlowControl scriptSetRelativePosition(final RunningScript<?> script) {
    final Vector3f translation = new Vector3f(script.params_20[2].get(), script.params_20[3].get(), script.params_20[4].get());

    final int bobjIndex = script.params_20[0].get();
    final int parentIndex = script.params_20[1].get();

    final BattleObject bobj = SCRIPTS.getObject(bobjIndex, BattleObject.class);

    if(bobj instanceof final EffectManagerData6c<?> manager && manager.hasAttachment(1)) {
      manager.removeAttachment(1);
    }

    //LAB_801106dc
    if(parentIndex == -1) {
      bobj.getPosition().set(translation);
    } else {
      final BattleObject parent = SCRIPTS.getObject(parentIndex, BattleObject.class);
      parent.getRelativePosition(translation, bobj.getPosition());
    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Transforms a battle object's position from world space to screen space")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bobjIndex", description = "The battle object script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The screen space X position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The screen space Y position")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The screen space Z position")
  @Method(0x80111b60L)
  public static FlowControl scriptTransformBobjPositionToScreenSpace(final RunningScript<?> script) {
    final BattleObject bobj = SCRIPTS.getObject(script.params_20[0].get(), BattleObject.class);
    final Vector2f transformed = new Vector2f();
    script.params_20[3].set(Math.round(transformToScreenSpace(transformed, bobj.getPosition()) / 4.0f));
    script.params_20[1].set(Math.round(transformed.x));
    script.params_20[2].set(Math.round(transformed.y));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("If an effect has a position scaler, pause and rewind; otherwise, continue")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @Method(0x80111be8L)
  public static FlowControl scriptWaitForPositionScalerToFinish(final RunningScript<?> script) {
    final EffectManagerData6c<?> data = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    return data.hasAttachment(1) ? FlowControl.PAUSE_AND_REWIND : FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a position scaler attachment to an effect (relative to a battle object if specified)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "parentIndex", description = "The parent index (or -1 for none)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "velocityX", description = "The X velocity (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "velocityY", description = "The Y velocity (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "velocityZ", description = "The Z velocity (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "accelerationX", description = "The X acceleration (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "accelerationY", description = "The Y acceleration (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "accelerationZ", description = "The Z acceleration (8-bit fixed-point)")
  @Method(0x80111c2cL)
  public static FlowControl scriptAddPositionScalerAttachment(final RunningScript<?> script) {
    addPositionScalerAttachment(SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class), SCRIPTS.getObject(script.params_20[1].get(), BattleObject.class), script.params_20[2].get() / (float)0x100, script.params_20[3].get() / (float)0x100, script.params_20[4].get() / (float)0x100, script.params_20[5].get() / (float)0x100, script.params_20[6].get() / (float)0x100, script.params_20[7].get() / (float)0x100);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds or updates a position scaler attachment to an effect (relative to a battle object if specified)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "parentIndex", description = "The parent index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "velocityX", description = "The X velocity (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "velocityY", description = "The Y velocity (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "velocityZ", description = "The Z velocity (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "accelerationX", description = "The X acceleration (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "accelerationY", description = "The Y acceleration (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "accelerationZ", description = "The Z acceleration (8-bit fixed-point)")
  @Method(0x80111cc4L)
  public static FlowControl scriptAddOrUpdatePositionScalerAttachment(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    final BattleObject bobj = SCRIPTS.getObject(script.params_20[1].get(), BattleObject.class);

    float velocityX = script.params_20[2].get() / (float)0x100;
    float velocityY = script.params_20[3].get() / (float)0x100;
    float velocityZ = script.params_20[4].get() / (float)0x100;
    float accelerationX = script.params_20[5].get() / (float)0x100;
    float accelerationY = script.params_20[6].get() / (float)0x100;
    float accelerationZ = script.params_20[7].get() / (float)0x100;

    if(!manager.hasAttachment(1)) {
      addPositionScalerAttachment(manager, bobj, velocityX, velocityY, velocityZ, accelerationX, accelerationY, accelerationZ);
    } else {
      //LAB_80111dac
      final TransformScalerAttachment34 attachment = (TransformScalerAttachment34)manager.findAttachment(1);

      if(attachment._06 == 0) {
        if(bobj != null) {
          final MV rotMatrix = new MV();
          rotMatrix.rotationXYZ(bobj.getRotation());

          final Vector3f velocity = new Vector3f(velocityX, velocityY, velocityZ).mul(rotMatrix);
          velocityX = velocity.x;
          velocityY = velocity.y;
          velocityZ = velocity.z;

          final Vector3f acceleration = new Vector3f(accelerationX, accelerationY, accelerationZ).mul(rotMatrix);
          accelerationX = acceleration.x;
          accelerationY = acceleration.y;
          accelerationZ = acceleration.z;
        }

        //LAB_80111e40
        attachment.velocity_18.add(velocityX, velocityY, velocityZ);
        attachment.acceleration_24.add(accelerationX, accelerationY, accelerationZ);
      }
    }

    //LAB_80111ea4
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Updates a position scaler attachment for parabolic arcs")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "unused")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "accelerationX", description = "The X acceleration")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "accelerationY", description = "The Y acceleration")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "accelerationZ", description = "The Z acceleration")
  @Method(0x80111ed4L)
  public static FlowControl scriptUpdateParabolicPositionScalerAttachment(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);

    if(manager.hasAttachment(1)) {
      final TransformScalerAttachment34 attachment = (TransformScalerAttachment34)manager.findAttachment(1);

      if(attachment != null && attachment.ticksRemaining_32 != -1 && attachment._06 == 0) {
        int ticksRemaining = attachment.ticksRemaining_32;

        final Vector3f sp0x10 = new Vector3f(
          (ticksRemaining * attachment.acceleration_24.x / 2.0f + attachment.velocity_18.x) * ticksRemaining + attachment.value_0c.x,
          (ticksRemaining * attachment.acceleration_24.y / 2.0f + attachment.velocity_18.y) * ticksRemaining + attachment.value_0c.y,
          (ticksRemaining * attachment.acceleration_24.z / 2.0f + attachment.velocity_18.z) * ticksRemaining + attachment.value_0c.z
        );

        final Vector3f rotation = new Vector3f();
        FUN_80110120(rotation, manager.getPosition(), sp0x10);

        final MV rotMatrix = new MV();
        rotMatrix.rotationZYX(rotation);

        final Vector3f acceleration = new Vector3f(
          script.params_20[2].get() / (float)0x100,
          script.params_20[3].get() / (float)0x100,
          script.params_20[4].get() / (float)0x100
        ).mul(rotMatrix);

        ticksRemaining++;
        attachment.velocity_18.x -= acceleration.x * ticksRemaining / 2.0f;
        attachment.velocity_18.y -= acceleration.y * ticksRemaining / 2.0f;
        attachment.velocity_18.z -= acceleration.z * ticksRemaining / 2.0f;
        attachment.acceleration_24.add(acceleration);
      }
    }

    //LAB_8011215c
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("(Note: uses movement mode 0, meaning not fully understood) Adds a position scaler to an effect (relative to a parent, if provided)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "parentIndex", description = "The parent index (or -1 for none)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "The number of frames for the movement to complete")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z position")
  @Method(0x80112184L)
  public static FlowControl scriptAddRelativePositionScalerTicks0(final RunningScript<?> script) {
    addRelativePositionScalerTicks(0, script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("(Note: uses movement mode 1, meaning not fully understood) Adds a position scaler to an effect (relative to a parent, if provided)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "parentIndex", description = "The parent index (or -1 for none)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "The number of frames for the movement to complete")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z position")
  @Method(0x801121fcL)
  public static FlowControl scriptAddRelativePositionScalerTicks1(final RunningScript<?> script) {
    addRelativePositionScalerTicks(1, script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("(Note: uses movement mode 0, meaning not fully understood) Adds a position scaler to an effect (relative to a parent, if provided)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "parentIndex", description = "The parent index (or -1 for none)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "distancePerFrame", description = "The distance to move each frame until complete")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z position")
  @Method(0x80112274L)
  public static FlowControl scriptAddRelativePositionScalerDistance0(final RunningScript<?> script) {
    addRelativePositionScalerDistance(0, script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get() / (float)0x100, script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("(Note: uses movement mode 1, meaning not fully understood) Adds a position scaler to an effect (relative to a parent, if provided)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "parentIndex", description = "The parent index (or -1 for none)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "distancePerFrame", description = "The distance to move each frame until complete")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z position")
  @Method(0x801122ecL)
  public static FlowControl scriptAddRelativePositionScalerDistance1(final RunningScript<?> script) {
    addRelativePositionScalerDistance(1, script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get() / (float)0x100, script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a position scaler to an effect to move it to its parent battle object")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "parentIndex", description = "The parent index")
  @Method(0x80112364L)
  public static FlowControl scriptAddPositionScalerMoveToParent(final RunningScript<?> script) {
    addPositionScalerMoveToParent(script.params_20[0].get(), script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the position scaler attachment velocity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "unused")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The X velocity (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y velocity (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z velocity (8-bit fixed-point)")
  @Method(0x80112398L)
  public static FlowControl scriptGetPositionScalerAttachmentVelocity(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    final Vector3f velocity = new Vector3f();

    if(manager.hasAttachment(1)) {
      final TransformScalerAttachment34 attachment = (TransformScalerAttachment34)manager.findAttachment(1);

      if(attachment._06 == 0) {
        velocity.set(attachment.velocity_18);
      }
    }

    //LAB_80112430
    script.params_20[2].set(Math.round(velocity.x * 0x100));
    script.params_20[3].set(Math.round(velocity.y * 0x100));
    script.params_20[4].set(Math.round(velocity.z * 0x100));
    return FlowControl.CONTINUE;
  }

  /** Sets rotation on script from given vector, adding from second script if one is specified */
  @Method(0x80112530L)
  public static int setRelativeRotation(final int scriptIndex1, final int scriptIndex2, final Vector3f rotation) {
    final EffectManagerData6c<?> data = (EffectManagerData6c<?>)scriptStatePtrArr_800bc1c0[scriptIndex1].innerStruct_00;

    if(BattleObject.EM__.equals(data.magic_00) && data.hasAttachment(2)) {
      data.removeAttachment(2);
    }

    // scriptIndex1Rotation cannot be factored out of condition because scriptIndex 1 and 2 can
    // refer to the same script. In this scenario, setting the rotation of scriptIndex1 outside
    // the condition will cause the script to always be set to 2 * rotation instead of rotation + rotation.
    //LAB_8011259c
    final Vector3f scriptIndex1Rotation = getScriptedObjectRotation(scriptIndex1);
    if(scriptIndex2 == -1) {
      scriptIndex1Rotation.set(rotation);
    } else {
      //LAB_801125d8
      scriptIndex1Rotation.set(getScriptedObjectRotation(scriptIndex2)).add(rotation);
    }

    //LAB_8011261c
    return 0;
  }

  /** Scales rotation vector for altering effect orientation */
  @Method(0x80112638L)
  public static int tickRotationScaler(final EffectManagerData6c<?> manager, final TransformScalerAttachment34 scaler) {
    scaler.velocity_18.add(scaler.acceleration_24);
    scaler.value_0c.add(scaler.velocity_18);
    manager.params_10.rot_10.set(scaler.value_0c);

    if(scaler.ticksRemaining_32 != -1) {
      scaler.ticksRemaining_32--;

      if(scaler.ticksRemaining_32 <= 0) {
        //LAB_801126f8
        return 0;
      }
    }

    //LAB_801126fc
    return 1;
  }

  @ScriptDescription("Calculates the relative rotation between two battle objects (or just returns the first object's rotation if the second is -1)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bobjIndex1", description = "The battle object 1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bobjIndex2", description = "The battle object 2 (or -1 for none)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The calculated X rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The calculated Y rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The calculated Z rotation (PSX degrees)")
  @Method(0x80112704L)
  public static FlowControl scriptGetRotationDifference(final RunningScript<?> script) {
    final BattleObject bobj1 = SCRIPTS.getObject(script.params_20[0].get(), BattleObject.class);
    final BattleObject bobj2 = SCRIPTS.getObject(script.params_20[1].get(), BattleObject.class);

    final Vector3f sp0x10;
    if(bobj2 == null) {
      sp0x10 = bobj1.getRotation();
    } else {
      sp0x10 = bobj1.getRotationDifference(bobj2, new Vector3f());
    }

    script.params_20[2].set(MathHelper.radToPsxDeg(sp0x10.x));
    script.params_20[3].set(MathHelper.radToPsxDeg(sp0x10.y));
    script.params_20[4].set(MathHelper.radToPsxDeg(sp0x10.z));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the relative rotation between two battle objects (or from the origin if the second is -1)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bobjIndex1", description = "The battle object 1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bobjIndex2", description = "The battle object 2 (or -1 for none)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z rotation (PSX degrees)")
  @Method(0x80112770L)
  public static FlowControl scriptSetRelativeRotation(final RunningScript<?> script) {
    setRelativeRotation(
      script.params_20[0].get(),
      script.params_20[1].get(),
      new Vector3f(
        MathHelper.psxDegToRad((short)script.params_20[2].get()),
        MathHelper.psxDegToRad((short)script.params_20[3].get()),
        MathHelper.psxDegToRad((short)script.params_20[4].get())
      )
    );

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets an effect's rotation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "unused")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The X rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z rotation (PSX degrees)")
  @Method(0x801127e0L)
  public static FlowControl scriptGetEffectRotation(final RunningScript<?> script) {
    final MV transforms = new MV();
    calculateEffectTransforms(transforms, SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class));

    final Vector3f rot = new Vector3f();
    getRotationFromTransforms(rot, transforms);
    script.params_20[2].set(MathHelper.radToPsxDeg(rot.x));
    script.params_20[3].set(MathHelper.radToPsxDeg(rot.y));
    script.params_20[4].set(MathHelper.radToPsxDeg(rot.z));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "partIndex", description = "The model part index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The X rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z rotation (PSX degrees)")
  @Method(0x8011287cL)
  public static FlowControl FUN_8011287c(final RunningScript<?> script) {
    final Vector3f rot = new Vector3f();
    final MV transforms = new MV();
    FUN_801116c4(transforms, script.params_20[0].get(), script.params_20[1].get());
    getRotationFromTransforms(rot, transforms);
    script.params_20[2].set(MathHelper.radToPsxDeg(rot.x));
    script.params_20[3].set(MathHelper.radToPsxDeg(rot.y));
    script.params_20[4].set(MathHelper.radToPsxDeg(rot.z));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("(Note: uses movement mode 0, meaning not fully understood) Adds a position scaler to an effect (relative to a parent, if provided)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bobjIndex1", description = "The first battle object script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bobjIndex2", description = "The second battle object script index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The X angle (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The Y angle (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z angle (12-bit fixed-point)")
  @Method(0x80112900L)
  public static FlowControl scriptGetRelativeAngleBetweenBobjs(final RunningScript<?> script) {
    final Vector3f rot = new Vector3f();
    calculateRelativeAngleBetweenPositions(rot, SCRIPTS.getObject(script.params_20[0].get(), BattleObject.class).getPosition(), SCRIPTS.getObject(script.params_20[1].get(), BattleObject.class).getPosition());

    // XZY is the correct order
    script.params_20[2].set(MathHelper.radToPsxDeg(rot.x));
    script.params_20[3].set(MathHelper.radToPsxDeg(rot.z));
    script.params_20[4].set(MathHelper.radToPsxDeg(rot.y));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Rotates a battle object towards a point (relative to a parent, if provided)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bobjIndex", description = "The battle object index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "parentIndex", description = "The parent index (or -1 for none)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z position")
  @Method(0x8011299cL)
  public static FlowControl scriptRotateBobjTowardsPoint(final RunningScript<?> script) {
    final BattleObject bobj = SCRIPTS.getObject(script.params_20[0].get(), BattleObject.class);
    final BattleObject parent = SCRIPTS.getObject(script.params_20[1].get(), BattleObject.class);

    final Vector3f position = new Vector3f(script.params_20[2].get(), script.params_20[3].get(), script.params_20[4].get());

    if(parent != null) {
      parent.getRelativePosition(position);
    }

    //LAB_80112a80
    calculateRelativeAngleBetweenPositions(bobj.getRotation(), bobj.getPosition(), position);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a rotation scaler attachment to an effect")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "unused")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "velocityX", description = "The X rotation velocity (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "velocityY", description = "The Y rotation velocity (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "velocityZ", description = "The Z rotation velocity (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "accelerationX", description = "The X rotation acceleration (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "accelerationY", description = "The Y rotation acceleration (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "accelerationZ", description = "The Z rotation acceleration (PSX degrees)")
  @Method(0x80112aa4L)
  public static FlowControl scriptAddRotationScalerAttachment(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);

    if(manager.hasAttachment(2)) {
      manager.removeAttachment(2);
    }

    //LAB_80112b58
    final TransformScalerAttachment34 v0 = manager.addAttachment(2, 0, SEffe::tickRotationScaler, new TransformScalerAttachment34());
    v0.parent_30 = null;
    v0.ticksRemaining_32 = -1;
    v0.value_0c.set(manager.params_10.rot_10);
    v0.velocity_18.x = MathHelper.psxDegToRad(script.params_20[2].get());
    v0.velocity_18.y = MathHelper.psxDegToRad(script.params_20[3].get());
    v0.velocity_18.z = MathHelper.psxDegToRad(script.params_20[4].get());
    v0.acceleration_24.x = MathHelper.psxDegToRad(script.params_20[5].get());
    v0.acceleration_24.y = MathHelper.psxDegToRad(script.params_20[6].get());
    v0.acceleration_24.z = MathHelper.psxDegToRad(script.params_20[7].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a rotation scaler attachment to an effect (relative to a parent battle object if one is specified)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "parentIndex", description = "The parent index (or -1 if none)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "The number of ticks until the rotation finishes")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The new X rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The new Y rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The new Z rotation (PSX degrees)")
  @Method(0x80112bf0L)
  public static FlowControl scriptAddRotationScalerAttachmentTicks(final RunningScript<?> script) {
    final int ticks = script.params_20[2].get();
    final float x = MathHelper.psxDegToRad(script.params_20[3].get());
    final float y = MathHelper.psxDegToRad(script.params_20[4].get());
    final float z = MathHelper.psxDegToRad(script.params_20[5].get());

    if(ticks >= 0) {
      final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
      final BattleObject parent = SCRIPTS.getObject(script.params_20[1].get(), BattleObject.class);
      final TransformScalerAttachment34 attachment = manager.addAttachment(2, 0, SEffe::tickRotationScaler, new TransformScalerAttachment34());

      final Vector3f effectRotation = manager.getRotation();
      final float dx;
      final float dy;
      final float dz;
      if(parent == null) {
        dx = x - effectRotation.x;
        dy = y - effectRotation.y;
        dz = z - effectRotation.z;
      } else {
        //LAB_80112ce4
        final Vector3f parentRotation = parent.getRotation();
        dx = parentRotation.x + x - effectRotation.x;
        dy = parentRotation.y + y - effectRotation.y;
        dz = parentRotation.z + z - effectRotation.z;
      }

      //LAB_80112d48
      attachment.parent_30 = null;
      attachment.ticksRemaining_32 = ticks;
      attachment.value_0c.set(manager.params_10.rot_10);
      attachment.velocity_18.x = dx / ticks;
      attachment.velocity_18.y = dy / ticks;
      attachment.velocity_18.z = dz / ticks;
      attachment.acceleration_24.zero();
    }

    //LAB_80112dd0
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a rotation scaler attachment to an effect (relative to a parent battle object if one is specified)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "parentIndex", description = "The parent index (or -1 if none)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "distancePerTick", description = "The amount to rotate per frame until the rotation finishes")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "angle", description = "The new rotation (PSX degrees)")
  @Method(0x80112e00L)
  public static FlowControl scriptAddRotationScalerAttachmentDistance(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    final BattleObject parent = SCRIPTS.getObject(script.params_20[1].get(), BattleObject.class);
    final int distancePerTick = script.params_20[2].get();
    final float angle = MathHelper.psxDegToRad(script.params_20[3].get());

    if(manager.hasAttachment(2)) {
      manager.removeAttachment(2);
    }

    //LAB_80112e8c
    final TransformScalerAttachment34 attachment = manager.addAttachment(2, 0, SEffe::tickRotationScaler, new TransformScalerAttachment34());
    attachment.parent_30 = null;
    attachment.value_0c.set(manager.params_10.rot_10);

    if(distancePerTick <= 0) {
      attachment.ticksRemaining_32 = -1;
      attachment.velocity_18.zero();
    } else {
      //LAB_80112ef4
      final Vector3f delta = new Vector3f(angle).sub(manager.getRotation());
      if(parent != null) {
        //LAB_80112f34
        delta.add(parent.getRotation());
      }

      //LAB_80112f98
      attachment.ticksRemaining_32 = (int)Math.max(1, delta.length() / distancePerTick);

      //LAB_80112fe4
      attachment.velocity_18.x = delta.x / attachment.ticksRemaining_32;
      attachment.velocity_18.y = delta.y / attachment.ticksRemaining_32;
      attachment.velocity_18.z = delta.z / attachment.ticksRemaining_32;
    }

    //LAB_80113038
    attachment.acceleration_24.zero();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a rotation scaler attachment towards a point to an effect (relative to a parent battle object if one is specified)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "parentIndex", description = "The parent index (or -1 if none)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "The number of ticks until the rotation finishes")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z position")
  @Method(0x8011306cL)
  public static FlowControl scriptAddRotationScalerAttachmentTowardsPointTicks(final RunningScript<?> script) {
    final int ticks = script.params_20[2].get();
    final float x = MathHelper.psxDegToRad(script.params_20[3].get());
    final float y = MathHelper.psxDegToRad(script.params_20[4].get());
    final float z = MathHelper.psxDegToRad(script.params_20[5].get());

    if(ticks >= 0) {
      final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
      final BattleObject parent = SCRIPTS.getObject(script.params_20[1].get(), BattleObject.class);

      if((manager.flags_04 & 0x4) != 0) {
        manager.removeAttachment(2);
      }

      //LAB_8011311c
      final TransformScalerAttachment34 attachment = manager.addAttachment(2, 0, SEffe::tickRotationScaler, new TransformScalerAttachment34());
      final Vector3f effectRotation = manager.getRotation();
      final Vector3f pos = new Vector3f(x, y, z);
      if(parent != null) {
        //LAB_8011316c
        pos.add(parent.getPosition());
      }

      //LAB_801131a4
      final Vector3f angle = new Vector3f();
      calculateRelativeAngleBetweenPositions(angle, manager.getPosition(), pos);

      attachment.parent_30 = null;
      attachment.ticksRemaining_32 = ticks;
      attachment.value_0c.set(manager.params_10.rot_10);
      attachment.velocity_18.x = (angle.x - effectRotation.x) / ticks;
      attachment.velocity_18.y = (angle.y - effectRotation.y) / ticks;
      attachment.velocity_18.z = (angle.z - effectRotation.z) / ticks;
      attachment.acceleration_24.zero();
    }

    //LAB_80113298
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a rotation scaler attachment towards a point to an effect (relative to a parent battle object if one is specified)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "parentIndex", description = "The parent index (or -1 if none)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "distancePerTick", description = "The amount to rotate per frame until the rotation finishes")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y position")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z position")
  @Method(0x801132c8L)
  public static FlowControl scriptAddRotationScalerAttachmentTowardsPointDistance(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    final BattleObject parent = SCRIPTS.getObject(script.params_20[1].get(), BattleObject.class);
    final int distancePerTick = script.params_20[2].get();
    final float x = script.params_20[3].get();
    final float y = script.params_20[4].get();
    final float z = script.params_20[5].get();

    if(manager.hasAttachment(2)) {
      manager.removeAttachment(2);
    }

    //LAB_80113374
    final TransformScalerAttachment34 attachment = manager.addAttachment(2, 0, SEffe::tickRotationScaler, new TransformScalerAttachment34());
    attachment.parent_30 = null;
    attachment.value_0c.set(manager.params_10.rot_10);

    if(distancePerTick <= 0) {
      attachment.ticksRemaining_32 = -1;
      attachment.velocity_18.zero();
    } else {
      //LAB_801133dc
      final Vector3f pos = new Vector3f(x, y, z);
      if(parent != null) {
        //LAB_80113408
        pos.add(parent.getPosition());
      }

      //LAB_80113440
      final Vector3f delta = new Vector3f();
      calculateRelativeAngleBetweenPositions(delta, manager.getPosition(), pos);
      delta.sub(manager.getRotation());

      attachment.ticksRemaining_32 = (int)Math.max(1, delta.length() / distancePerTick);

      //LAB_801134ec
      attachment.velocity_18.x = delta.x / attachment.ticksRemaining_32;
      attachment.velocity_18.y = delta.y / attachment.ticksRemaining_32;
      attachment.velocity_18.z = delta.z / attachment.ticksRemaining_32;
    }

    //LAB_80113540
    attachment.acceleration_24.zero();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Converts a YXZ rotation to a XYZ rotation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "unused")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "yxzX", description = "The YXZ X rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "yxzY", description = "The YXZ Y rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "yxzZ", description = "The YXZ Z rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "xyzX", description = "The XYZ X rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "xyzY", description = "The XYZ Y rotation (PSX degrees)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "xyzZ", description = "The XYZ Z rotation (PSX degrees)")
  @Method(0x8011357cL)
  public static FlowControl scriptConvertRotationYxzToXyz(final RunningScript<?> script) {
    final Vector3f rot = new Vector3f(
      MathHelper.psxDegToRad((short)script.params_20[1].get()),
      MathHelper.psxDegToRad((short)script.params_20[2].get()),
      MathHelper.psxDegToRad((short)script.params_20[3].get())
    );

    final MV transforms = new MV();
    transforms.rotationYXZ(rot);
    getRotationFromTransforms(rot, transforms);

    script.params_20[4].set(MathHelper.radToPsxDeg(rot.x));
    script.params_20[5].set(MathHelper.radToPsxDeg(rot.y));
    script.params_20[6].set(MathHelper.radToPsxDeg(rot.z));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Calculates the scale ratio between two battle objects (or just returns the first object's rotation if the second is -1)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bobjIndex1", description = "The battle object 1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bobjIndex2", description = "The battle object 2 (or -1 for none)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "x", description = "The calculated X scale ratio (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "y", description = "The calculated Y scale ratio (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The calculated Z scale ratio (12-bit fixed-point)")
  @Method(0x80113964L)
  public static FlowControl scriptGetScaleRatio(final RunningScript<?> script) {
    final BattleObject bobj1 = SCRIPTS.getObject(script.params_20[0].get(), BattleObject.class);
    final BattleObject bobj2 = SCRIPTS.getObject(script.params_20[1].get(), BattleObject.class);

    final Vector3f scale;
    if(bobj2 == null) {
      scale = bobj1.getScale();
    } else {
      scale = bobj1.getScaleRatio(bobj2, new Vector3f());
    }

    script.params_20[2].set(Math.round(scale.x * 0x1000));
    script.params_20[3].set(Math.round(scale.y * 0x1000));
    script.params_20[4].set(Math.round(scale.z * 0x1000));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the relative scale between two battle objects (or from the origin if the second is -1)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bobjIndex1", description = "The battle object 1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bobjIndex2", description = "The battle object 2 (or -1 for none)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X rotation (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y rotation (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z rotation (12-bit fixed-point)")
  @Method(0x801139d0L)
  public static FlowControl scriptSetRelativeScale(final RunningScript<?> script) {
    final BattleObject data1 = SCRIPTS.getObject(script.params_20[0].get(), BattleObject.class);
    final BattleObject data2 = SCRIPTS.getObject(script.params_20[1].get(), BattleObject.class);

    final float x = (short)script.params_20[2].get() / (float)0x1000;
    final float y = (short)script.params_20[3].get() / (float)0x1000;
    final float z = (short)script.params_20[4].get() / (float)0x1000;
    final Vector3f newScale = new Vector3f(x, y, z);

    if(data2 != null) {
      //LAB_80113a28
      newScale.mul(data2.getScale());
    }

    //LAB_80113b0c
    data1.getScale().set(newScale);

    //LAB_80113b94
    return FlowControl.CONTINUE;
  }

  /** Scales scale vector for changing effect size */
  @Method(0x80113ba0L)
  public static int tickScaleScaler(final EffectManagerData6c<?> manager, final TransformScalerAttachment34 scaler) {
    scaler.velocity_18.add(scaler.acceleration_24);
    scaler.value_0c.add(scaler.velocity_18);
    manager.params_10.scale_16.set(scaler.value_0c);

    if(scaler.ticksRemaining_32 != -1) {
      scaler.ticksRemaining_32--;

      if(scaler.ticksRemaining_32 <= 0) {
        return 0;
      }
    }

    //LAB_80113c60
    //LAB_80113c64
    return 1;
  }

  /** Set scale value and rate of change factors */
  @ScriptDescription("Adds a scale scaler attachment towards a point to an effect (relative to a parent battle object if one is specified)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "velocityX", description = "The X velocity (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "velocityY", description = "The Y velocity (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "velocityZ", description = "The Z velocity (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "accelerationX", description = "The X acceleration (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "accelerationY", description = "The Y acceleration (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "accelerationZ", description = "The Z acceleration (12-bit fixed-point)")
  @Method(0x80113c6cL)
  public static FlowControl scriptAddScaleScalerAttachment(final RunningScript<?> script) {
    final EffectManagerData6c<?> scalerManager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);

    if(scalerManager.hasAttachment(3)) {
      scalerManager.removeAttachment(3);
    }

    //LAB_80113d20
    final TransformScalerAttachment34 scaleScaler = scalerManager.addAttachment(3, 0, SEffe::tickScaleScaler, new TransformScalerAttachment34());
    scaleScaler.parent_30 = null;
    scaleScaler.ticksRemaining_32 = -1;

    scaleScaler.value_0c.set(scalerManager.params_10.scale_16);

    scaleScaler.velocity_18.set(script.params_20[1].get() / (float)0x1000, script.params_20[2].get() / (float)0x1000, script.params_20[3].get() / (float)0x1000);
    scaleScaler.acceleration_24.set(script.params_20[4].get() / (float)0x1000, script.params_20[5].get() / (float)0x1000, script.params_20[6].get() / (float)0x1000);
    return FlowControl.CONTINUE;
  }

  /** Used in guard-type effects and dragoon additions */
  @Method(0x80113db8L)
  public static FlowControl addScaleScalerAttachmentTicks(final int mode, final RunningScript<?> script) {
    final int scriptIndex1 = script.params_20[0].get();
    final int scriptIndex2 = script.params_20[1].get();
    final int ticks = script.params_20[2].get();
    final float x = script.params_20[3].get() / (float)0x1000;
    final float y = script.params_20[4].get() / (float)0x1000;
    final float z = script.params_20[5].get() / (float)0x1000;

    if(ticks >= 0) {
      final EffectManagerData6c<?> manager = SCRIPTS.getObject(scriptIndex1, EffectManagerData6c.class);
      final BattleObject parent = SCRIPTS.getObject(scriptIndex2, BattleObject.class);

      if(manager.hasAttachment(3)) {
        manager.removeAttachment(3);
      }

      //LAB_80113e70
      final TransformScalerAttachment34 scaler = manager.addAttachment(3, 0, SEffe::tickScaleScaler, new TransformScalerAttachment34());

      final Vector3f scale = new Vector3f(x, y, z);

      if(parent == null) {
        scale.sub(manager.getScale());
      } else if(mode == 0) {
        scale.sub(manager.getScaleDifference(parent, new Vector3f()));
      } else if(mode == 1) {
        scale.mul(parent.getScale());
        scale.sub(manager.getScale());
      }

      //LAB_80113fc0
      scaler.parent_30 = null;
      scaler.ticksRemaining_32 = ticks;
      scaler.value_0c.set(manager.params_10.scale_16);

      if(ticks != 0) {
        scaler.velocity_18.set(scale).div(ticks);
      } else {
        scaler.velocity_18.zero();
      }

      scaler.acceleration_24.zero();
    }

    //LAB_8011403c
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a difference scale scaler attachment to an effect (relative to a parent battle object if one is specified)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "parentIndex", description = "The parent index (or -1 if none)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "The number of ticks until the scaling finishes")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X scale (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y scale (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z scale (12-bit fixed-point)")
  @Method(0x80114070L)
  public static FlowControl scriptAddScaleScalerDifferenceAttachmentTicks(final RunningScript<?> script) {
    return addScaleScalerAttachmentTicks(0, script);
  }

  @ScriptDescription("Adds a multiplicative scale scaler attachment to an effect (relative to a parent battle object if one is specified)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "parentIndex", description = "The parent index (or -1 if none)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "The number of ticks until the scaling finishes")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X scale (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y scale (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z scale (12-bit fixed-point)")
  @Method(0x80114094L)
  public static FlowControl scriptAddScaleScalerMultiplicativeAttachmentTicks(final RunningScript<?> script) {
    return addScaleScalerAttachmentTicks(1, script);
  }

  @ScriptDescription("Adds a multiplicative scale scaler attachment to an effect (relative to a parent battle object if one is specified)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "parentIndex", description = "The parent index (or -1 if none)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "distancePerTick", description = "The amount to scaling per frame until the scale finishes")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "x", description = "The X scale (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "y", description = "The Y scale (12-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z scale (12-bit fixed-point)")
  @Method(0x801143f8L)
  public static FlowControl scriptAddScaleScalerMultiplicativeAttachmentDistance(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @ScriptDescription("Calculates the relative colour between two battle objects (or just returns the first object's colour if the second is -1)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bobjIndex1", description = "The battle object 1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bobjIndex2", description = "The battle object 2 (or -1 for none)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "r", description = "The calculated red channel")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "g", description = "The calculated green channel")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "b", description = "The calculated blue channel")
  @Method(0x8011452cL)
  public static FlowControl scriptGetColourDifference(final RunningScript<?> script) {
    final BattleObject bobj1 = SCRIPTS.getObject(script.params_20[0].get(), BattleObject.class);
    final BattleObject bobj2 = SCRIPTS.getObject(script.params_20[1].get(), BattleObject.class);

    final Vector3i colour;
    if(bobj2 == null) {
      colour = bobj1.getColour();
    } else {
      colour = bobj1.getColourDifference(bobj2, new Vector3i());
    }

    script.params_20[2].set(colour.x);
    script.params_20[3].set(colour.y);
    script.params_20[4].set(colour.z);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the relative colour between two battle objects (or just sets the colour if the second is -1)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bobjIndex1", description = "The battle object 1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bobjIndex2", description = "The battle object 2 (or -1 for none)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "r", description = "The red channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "g", description = "The green channel")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "b", description = "The blue channel")
  @Method(0x80114598L)
  public static FlowControl scriptSetRelativeColour(final RunningScript<?> script) {
    final BattleObject bobj1 = SCRIPTS.getObject(script.params_20[0].get(), BattleObject.class);
    final BattleObject bobj2 = SCRIPTS.getObject(script.params_20[1].get(), BattleObject.class);

    final Vector3i colour1 = bobj1.getColour();

    //LAB_80114614
    if(bobj2 == null) {
      colour1.x = script.params_20[2].get() & 0xffff;
      colour1.y = script.params_20[3].get() & 0xffff;
      colour1.z = script.params_20[4].get() & 0xffff;
    } else {
      //LAB_80114668
      final Vector3i colour2 = bobj2.getColour();

      //LAB_8011469c
      colour1.x = (script.params_20[2].get() & 0xffff) + colour2.x;
      colour1.y = (script.params_20[3].get() & 0xffff) + colour2.y;
      colour1.z = (script.params_20[4].get() & 0xffff) + colour2.z;
    }

    //LAB_801146f0
    return FlowControl.CONTINUE;
  }

  /** Scales color vector for fading color in/out */
  @Method(0x801146fcL)
  public static int tickColourScalerAttachment(final EffectManagerData6c<?> manager, final TransformScalerAttachment34 scaler) {
    scaler.velocity_18.add(scaler.acceleration_24);
    scaler.value_0c.add(scaler.velocity_18);
    manager.params_10.colour_1c.x = (int)scaler.value_0c.x & 0xff;
    manager.params_10.colour_1c.y = (int)scaler.value_0c.y & 0xff;
    manager.params_10.colour_1c.z = (int)scaler.value_0c.z & 0xff;

    if(scaler.ticksRemaining_32 != -1) {
      scaler.ticksRemaining_32--;

      if(scaler.ticksRemaining_32 <= 0) {
        return 0;
      }
    }

    //LAB_801147bc
    //LAB_801147c0
    return 1;
  }

  @ScriptDescription("Adds a colour scaler attachment to an effect (lasts until removed)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "velocityX", description = "The X velocity (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "velocityY", description = "The Y velocity (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "velocityZ", description = "The Z velocity (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "accelerationX", description = "The X acceleration (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "accelerationY", description = "The Y acceleration (8-bit fixed-point)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "accelerationZ", description = "The Z acceleration (8-bit fixed-point)")
  @Method(0x801147c8L)
  public static FlowControl scriptAddColourScalerAttachment(final RunningScript<?> script) {
    final int effectIndex = script.params_20[0].get();
    //TODO .8?
    final float velocityX = script.params_20[1].get() / (float)0x100;
    final float velocityY = script.params_20[2].get() / (float)0x100;
    final float velocityZ = script.params_20[3].get() / (float)0x100;
    final float accelerationX = script.params_20[4].get() / (float)0x100;
    final float accelerationY = script.params_20[5].get() / (float)0x100;
    final float accelerationZ = script.params_20[6].get() / (float)0x100;
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(effectIndex, EffectManagerData6c.class);

    if(manager.hasAttachment(4)) {
      manager.removeAttachment(4);
    }

    //LAB_8011487c
    final TransformScalerAttachment34 attachment = manager.addAttachment(4, 0, SEffe::tickColourScalerAttachment, new TransformScalerAttachment34());
    attachment.parent_30 = null;
    attachment.ticksRemaining_32 = -1;
    attachment.value_0c.set(manager.getColour());
    attachment.velocity_18.set(velocityX, velocityY, velocityZ);
    attachment.acceleration_24.set(accelerationX, accelerationY, accelerationZ);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a colour scaler attachment to an effect (relative to a parent if one is specified)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "parentIndex", description = "The parent index (or -1 for none)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "The number of ticks until finished")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "velocityX", description = "The X velocity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "velocityY", description = "The Y velocity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "velocityZ", description = "The Z velocity")
  @Method(0x80114920L)
  public static FlowControl scriptAddConstantColourScalerAttachment(final RunningScript<?> script) {
    final int ticks = script.params_20[2].get();

    if(ticks >= 0) {
      final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
      final BattleObject other = SCRIPTS.getObject(script.params_20[1].get(), BattleObject.class);

      if(manager.hasAttachment(4)) {
        manager.removeAttachment(4);
      }

      //LAB_801149d0
      final TransformScalerAttachment34 colorScaler = manager.addAttachment(4, 0, SEffe::tickColourScalerAttachment, new TransformScalerAttachment34());
      final Vector3i colour;
      if(other == null) {
        colour = manager.getColour();
      } else {
        colour = manager.getColourDifference(other, new Vector3i());
      }

      // .8?
      final Vector3f velocityVec = new Vector3f().set(
        script.params_20[3].get(),
        script.params_20[4].get(),
        script.params_20[5].get()
      );

      colorScaler.parent_30 = null;
      colorScaler.ticksRemaining_32 = ticks;
      colorScaler.value_0c.set(manager.getColour());
      colorScaler.velocity_18.set(velocityVec).sub(colour.x, colour.y, colour.z).div(ticks);
      colorScaler.acceleration_24.zero();
    }

    //LAB_80114ad0
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a colour scaler attachment to an effect (relative to a parent if one is specified)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "parentIndex", description = "The parent index (or -1 for none)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "distancePerTick", description = "The distance per tick until finished")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "velocityX", description = "The X velocity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "velocityY", description = "The Y velocity")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "velocityZ", description = "The Z velocity")
  @Method(0x80114b00L)
  public static FlowControl scriptAddConstantColourScalerDistance(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @ScriptDescription("Gets one of the generic values of an EffectManager6c")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "genericIndex", description = "The generic variable index to return")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "value", description = "The value")
  @Method(0x80114e0cL)
  public static FlowControl scriptGetGenericEffectValue(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    script.params_20[2].set(manager.params_10.get24(script.params_20[1].get()));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Removes a generic attachment from an effect")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "genericIndex", description = "The generic index")
  @Method(0x80114eb4L)
  public static FlowControl scriptRemoveGenericEffectAttachment(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    final int attachmentId = script.params_20[1].get() + 5;

    if(manager.hasAttachment(attachmentId)) {
      manager.removeAttachment(attachmentId);
    }

    //LAB_80114f24
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets one of the generic values of an EffectManager6c")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "genericIndex", description = "The generic variable index to return")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "value", description = "The value")
  @Method(0x80114e60L)
  public static FlowControl scriptSetGenericEffectValue(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    manager.params_10.set24(script.params_20[1].get(), script.params_20[2].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x80114f3cL)
  public static <T extends EffectManagerParams<T>> void addGenericAttachment(final EffectManagerData6c<T> manager, final int varIndex, final int speed, final int acceleration) {
    if(manager.hasAttachment(varIndex + 5)) {
      manager.removeAttachment(varIndex + 5);
    }

    //LAB_80114fa8
    final GenericAttachment1c attachment = manager.addAttachment(varIndex + 5, 0, SEffe::tickGenericAttachment, new GenericAttachment1c());

    attachment.accumulator_0c = manager.params_10.get24(varIndex) << 8;
    attachment.speed_10 = speed;
    attachment.acceleration_14 = acceleration;
    attachment._18 = -1;
    attachment.ticksRemaining_1a = -1;
  }

  /** TODO this method advances animation frames */
  @Method(0x80114d98L)
  public static <T extends EffectManagerParams<T>> int tickGenericAttachment(final EffectManagerData6c<T> manager, final GenericAttachment1c attachment) {
    attachment.speed_10 += attachment.acceleration_14;
    attachment.accumulator_0c += attachment.speed_10;

    manager.params_10.set24(attachment.id_05 - 5, attachment.accumulator_0c >> 8);

    if(attachment.ticksRemaining_1a == -1) {
      return 1;
    }

    attachment.ticksRemaining_1a--;
    if(attachment.ticksRemaining_1a > 0) {
      //LAB_80114e00
      return 1;
    }

    //LAB_80114e04
    return 0;
  }

  @ScriptDescription("No-op")
  @Method(0x80114f34L)
  public static FlowControl FUN_80114f34(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a generic attachment to an effect")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "genericIndex", description = "The generic index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "speed", description = "The attachment speed")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "acceleration", description = "The attachment acceleration")
  @Method(0x80115014L)
  public static FlowControl scriptAddGenericAttachment(final RunningScript<?> script) {
    addGenericAttachment(SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class), script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a generic attachment to an effect that increments at a fixed rate")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "varIndex", description = "The generic variable index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "value", description = "The target value")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "The number of ticks until finished")
  @Method(0x80115058L)
  public static FlowControl scriptAddGenericAttachmentTicks(final RunningScript<?> script) {
    final int varIndex = script.params_20[1].get();
    final int newValue = script.params_20[2].get();
    final int ticks = script.params_20[3].get();

    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);

    if(manager.hasAttachment(varIndex + 5)) {
      manager.removeAttachment(varIndex + 5);
    }

    //LAB_801150e8
    final GenericAttachment1c attachment = manager.addAttachment(varIndex + 5, 0, SEffe::tickGenericAttachment, new GenericAttachment1c());
    final int oldValue = manager.params_10.get24(varIndex);

    attachment.accumulator_0c = oldValue << 8;
    attachment.speed_10 = ((newValue << 8) - (oldValue << 8)) / ticks;
    attachment.acceleration_14 = 0;
    attachment._18 = -1;
    attachment.ticksRemaining_1a = ticks;

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adds a generic attachment to an effect that increments at a fixed rate")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "varIndex", description = "The generic variable index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "value", description = "The target value")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "speed", description = "The amount per frame to increment")
  @Method(0x80115168L)
  public static FlowControl scriptAddGenericAttachmentSpeed(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    final int varIndex = script.params_20[1].get();
    final int newValue = script.params_20[2].get();
    final int speed = Math.max(1, script.params_20[3].get());

    if(manager.hasAttachment(varIndex + 5)) {
      manager.removeAttachment(varIndex + 5);
    }

    //LAB_80115204
    final GenericAttachment1c attachment = manager.addAttachment(varIndex + 5, 0, SEffe::tickGenericAttachment, new GenericAttachment1c());
    final int oldValue = manager.params_10.get24(varIndex);

    attachment.accumulator_0c = oldValue << 8;
    attachment.speed_10 = speed;
    attachment.ticksRemaining_1a = ((newValue << 8) - (oldValue << 8)) / speed;
    attachment.acceleration_14 = 0;
    attachment._18 = -1;
    return FlowControl.CONTINUE;
  }

  @Method(0x80115288L)
  public static int tickLifespanAttachment(final EffectManagerData6c<?> manager, final GenericAttachment1c attachment) {
    attachment.ticksRemaining_1a--;
    return attachment.ticksRemaining_1a > 0 ? 1 : 2;
  }

  @ScriptDescription("Adds a lifespan attachment to an effect that deallocates the effect after a certain number of ticks")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "ticks", description = "The number of ticks until the effect is deallocated")
  @Method(0x801152b0L)
  public static FlowControl scriptAddLifespanAttachment(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    final GenericAttachment1c attachment = manager.addAttachment(0, 0, SEffe::tickLifespanAttachment, new GenericAttachment1c());
    attachment.ticksRemaining_1a = script.params_20[1].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Clears an effect manager's flag 0x8000_0000 and then sets or unsets flag 0x8000_0000 (bit 31)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "clear", description = "True to clear, false otherwise (NOTE: backwards from others)")
  @Method(0x80115324L)
  public static FlowControl FUN_80115324(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    manager.params_10.flags_00 = manager.params_10.flags_00 & 0x7fff_ffff | ((script.params_20[1].get() ^ 1) & 1) << 31;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Clears an effect manager's flag 0x4000_0000 and then sets or unsets flag 0x4000_0000 (bit 30)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "set", description = "True to set, false otherwise")
  @Method(0x80115388L)
  public static FlowControl FUN_80115388(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    manager.params_10.flags_00 = manager.params_10.flags_00 & 0xbfff_ffff | script.params_20[1].get() << 30;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Clears an effect manager's flags 0x3000_0000 and then sets or unsets flag 0x1000_0000 (bit 28)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "set", description = "True to set, false otherwise")
  @Method(0x801153e4L)
  public static FlowControl FUN_801153e4(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    manager.params_10.flags_00 = manager.params_10.flags_00 & 0xcfff_ffff | script.params_20[1].get() << 28;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Clears an effect manager's flag 0x400_0000 and then sets or unsets flag 0x400_0000 (bit 28)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "set", description = "True to set, false otherwise")
  @Method(0x80115440L)
  public static FlowControl FUN_80115440(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    manager.params_10.flags_00 = manager.params_10.flags_00 & 0xfbff_ffff | script.params_20[1].get() << 26;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Clears an effect manager's flag 0x40 and then sets or unsets flag 0x40 (bit 6)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "set", description = "True to set, false otherwise")
  @Method(0x8011549cL)
  public static FlowControl FUN_8011549c(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    manager.params_10.flags_00 = manager.params_10.flags_00 & 0xffff_ffbf | script.params_20[1].get() << 6;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Clears an effect manager's flag 0x8 and then sets or unsets flag 0x8 (bit 3)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "set", description = "True to set, false otherwise")
  @Method(0x801154f4L)
  public static FlowControl FUN_801154f4(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    manager.params_10.flags_00 = manager.params_10.flags_00 & 0xffff_fff7 | script.params_20[1].get() << 3;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Removes an attachment from an effect")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attachmentId", description = "The attachment ID")
  @Method(0x8011554cL)
  public static FlowControl scriptRemoveEffectAttachment(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    manager.removeAttachment(script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Checks if an effect has a specific attachment")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.BOOL, name = "hasAttachment", description = "True if the attachment is present, false otherwise")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "attachmentId", description = "The attachment ID")
  @Method(0x801155a0L)
  public static FlowControl scriptHasEffectAttachment(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    script.params_20[1].set(manager.hasAttachment(script.params_20[2].get()) ? 1 : 0);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("No-op")
  @Method(0x801155f8L)
  public static FlowControl FUN_801155f8(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("No-op")
  @Method(0x80115600L)
  public static FlowControl FUN_80115600(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Replaces an existing script state's script")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The script index to replace the script for")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "deffScriptIndex", description = "The DEFF script index to load (or -1 to use this script's script)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "entrypoint", description = "The script entrypoint at which to start")
  @Method(0x80115608L)
  public static FlowControl scriptSetScriptScript(final RunningScript<?> script) {
    final int scriptIndex = script.params_20[0].get();
    final int entrypoint = script.params_20[2].get();
    final int deffScriptIndex = script.params_20[1].get();

    final ScriptFile file;
    if(deffScriptIndex == -1) {
      file = script.scriptState_04.scriptPtr_14;
    } else {
      //LAB_80115654
      file = deffManager_800c693c.scripts_2c[deffScriptIndex];
    }

    //LAB_80115674
    SCRIPTS.getState(scriptIndex).loadScriptFile(file, entrypoint);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Loads this script into another script state and jumps to a script address")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex", description = "The script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "address", description = "The script address", branch = ScriptParam.Branch.REENTRY)
  @Method(0x80115690L)
  public static FlowControl scriptLoadSameScriptAndJump(final RunningScript<?> script) {
    final ScriptState<?> state = SCRIPTS.getState(script.params_20[0].get());
    state.loadScriptFile(script.scriptState_04.scriptPtr_14, 0);
    script.params_20[1].jump(state);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, possibly something to do with effect manager parents")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "coord2Index")
  @Method(0x801156f8L)
  public static FlowControl FUN_801156f8(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    manager.scriptIndex_0c = script.params_20[1].get();
    manager.coord2Index_0d = script.params_20[2].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets an effect manager's Z value")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "z", description = "The Z coordinate")
  @Method(0x8011574cL)
  public static FlowControl scriptGetEffectZ(final RunningScript<?> script) {
    script.params_20[1].set(SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class).params_10.z_22);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets an effect manager's Z value")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "z", description = "The Z coordinate")
  @Method(0x8011578cL)
  public static FlowControl scriptSetEffectZ(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    manager.params_10.z_22 = script.params_20[1].get();

//    if(manager._10.z_22 < 0) {
//      LOGGER.warn("Negative Z value! %d", manager._10.z_22);
//      manager._10.z_22 = Math.abs(manager._10.z_22);
//    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "scriptIndex")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "coord2Index")
  @Method(0x801157d0L)
  public static FlowControl FUN_801157d0(final RunningScript<?> script) {
    final int scriptIndex = script.params_20[1].get();
    final int coord2Index = script.params_20[2].get();

    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);

    final MV sp0x10 = new MV();
    final MV sp0x30 = new MV();
    calculateEffectTransforms(sp0x10, manager);

    if(scriptIndex == -1) {
      sp0x30.set(sp0x10);
    } else {
      //LAB_8011588c
      final EffectManagerData6c<EffectManagerParams.VoidType> sp0x50 = new EffectManagerData6c<>("Temp", new EffectManagerParams.VoidType());

      sp0x50.params_10.trans_04.zero();
      sp0x50.params_10.rot_10.zero();
      sp0x50.params_10.scale_16.set(1.0f, 1.0f, 1.0f);

      sp0x50.scriptIndex_0c = scriptIndex;
      sp0x50.coord2Index_0d = coord2Index;

      final MV transforms = new MV();
      calculateEffectTransforms(transforms, sp0x50);

      final Vector3f rot = new Vector3f();
      final Vector3f scale = new Vector3f();
      getRotationAndScaleFromTransforms(rot, scale, transforms);
      rot.negate();
      transforms.rotationZYX(rot);

      transforms.scaleLocal(new Vector3f(0x100, 0x100, 0x100).div(scale));
      transforms.mul(sp0x10, sp0x30);

      final Vector3f sp0x100 = new Vector3f().set(sp0x10.transfer).sub(transforms.transfer);
      sp0x100.mul(transforms, sp0x30.transfer);
    }

    //LAB_801159cc
    getRotationAndScaleFromTransforms(manager.params_10.rot_10, manager.params_10.scale_16, sp0x30);
    manager.params_10.trans_04.set(sp0x30.transfer);
    manager.scriptIndex_0c = scriptIndex;
    manager.coord2Index_0d = coord2Index;
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Resets the DEFF manager")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode")
  @Method(0x80115a28L)
  public static FlowControl scriptResetDeffManager(final RunningScript<?> script) {
    deffManager_800c693c.reset(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Plays an XA sound")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "loadingStage", description = "The loading stage")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "archiveIndex", description = "The archive index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "fileIndex", description = "The file index")
  @Method(0x80115a58L)
  public static FlowControl scriptPlayXaAudio(final RunningScript<?> script) {
    playXaAudio(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the current XA loading stage (probably doesn't work in SC)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "loadingStage", description = "The stage")
  @Method(0x80115a94L)
  public static FlowControl scriptGetXaLoadingStage(final RunningScript<?> script) {
    script.params_20[0].set(_800bf0cf);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Waits for the currently-loading XA audio to load (no-op in SC)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "expectedStage", description = "The expected stage")
  @Method(0x80115ab0L)
  public static FlowControl scriptWaitForXaToLoad(final RunningScript<?> script) {
//    return _800bf0cf.get() != a0.params_20.get(0).deref().get() ? 2 : 0;
    //TODO GH#3 the XA code is rewritten and it never sets 800bf0cf back to 0, I dunno if this is important or not
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Updates the DEFF manager flags")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flags", description = "OR mask if positive, AND mask if negative")
  @Method(0x80115ad8L)
  public static FlowControl scriptUpdateDeffManagerFlags(final RunningScript<?> script) {
    final DeffManager7cc deffManager = deffManager_800c693c;

    final int flags;
    if(script.params_20[0].get() >= 0) {
      //LAB_80115b08
      flags = script.params_20[0].get() | deffManager.flags_20;
    } else {
      flags = script.params_20[0].get() & deffManager.flags_20;
    }

    //LAB_80115b20
    deffManager.flags_20 = flags;
    return FlowControl.CONTINUE;
  }

  @Method(0x80115b2cL)
  public static void screenDarkeningTicker(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> data) {
    final int currentVal = state.storage_44[8];
    final int targetVal = state.storage_44[9];

    if(currentVal == targetVal) {
      state.deallocateWithChildren();
    } else {
      //LAB_80115b80
      applyScreenDarkening(currentVal);

      //LAB_80115bd4
      if(currentVal > targetVal) {
        state.storage_44[8]--;
      } else {
        //LAB_80115bb4
        state.storage_44[8]++;
      }
    }

    //LAB_80115bd8
  }

  @Method(0x80115bf0L)
  public static void screenDarkeningDestructor(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> data) {
    applyScreenDarkening(state.storage_44[9]);
  }

  @Method(0x80115c2cL)
  public static void allocateScreenDarkeningEffect(final int startVal, final int targetVal) {
    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager(
      "Screen darkening",
      deffManager_800c693c.scriptState_1c,
      SEffe::screenDarkeningTicker,
      null,
      SEffe::screenDarkeningDestructor,
      null
    );

    state.storage_44[8] = startVal;
    state.storage_44[9] = targetVal;
  }

  @Method(0x80115cacL)
  public static long loadDeffStageEffects(final int mode) {
    final int _00;
    final int _02;
    final int _04;

    final int stage = ((Battle)currentEngineState_8004dd04).currentStage_800c66a4;

    if(stage < 71 || stage > 78) { // Not in Dragoon "special transformation" stage
      //LAB_80115d14
      //LAB_80115d2c
      for(int i = 0; ; i++) {
        if(melbuStageIndices_800fb064[i] == -1) { // This is the normal branch, no special-case handling
          //LAB_80115cd8
          final DeffManager7cc.Struct08 v0 = deffManager_800c693c._00;
          _00 = v0._00;
          _02 = v0._02;
          _04 = v0._04;
          break;
        }

        if(melbuStageIndices_800fb064[i] == stage) { // Melbu stages
          //LAB_80115d58
          final DeffManager7cc.Struct04 v0 = deffManager_800c693c._08[i];
          _00 = v0._00;
          _02 = v0._02;
          _04 = 0;
          break;
        }
      }

      //LAB_80115d84
      if(mode == 0) {
        //LAB_80115dc0
        if((stage_800bda0c.flags_5e4 & 0x8000) != 0) {
          allocateScreenDarkeningEffect(6, 16);
        }

        //LAB_80115de8
        stage_800bda0c.flags_5e4 &= ~(_00 | _02 | _04);
        //LAB_80115da8
      } else if(mode == 1) {
        //LAB_80115e18
        stage_800bda0c.flags_5e4 |= _00;
      } else if(mode == 2) {
        //LAB_80115e34
        stage_800bda0c.flags_5e4 |= _02;

        if((stage_800bda0c.flags_5e4 & 0x8000) != 0) {
          allocateScreenDarkeningEffect(16, 6);
        }
      } else if(mode == 3) {
        //LAB_80115e70
        //LAB_80115e8c
        stage_800bda0c.flags_5e4 |= _04;
      }
    }

    //LAB_80115e90
    //LAB_80115e94
    return 0;
  }

  @ScriptDescription("Loads DEFF effects for the current battle stage")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode", description = "Unknown")
  @Method(0x80115ea4L)
  public static FlowControl scriptLoadDeffStageEffects(final RunningScript<?> script) {
    loadDeffStageEffects(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Gets the texture metrics for various effect types")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "typeOrBobjIndex", description = "DEFF type, or battle object index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "textureIndex", description = "Texture index (only for types 0x100_0000 and 0x300_0000)")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "u", description = "The texture U")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "v", description = "The texture V")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "w", description = "The texture W")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "h", description = "The texture H")
  @Method(0x80115ed4L)
  public static FlowControl scriptGetEffectTextureMetrics(final RunningScript<?> script) {
    final int w;
    final int v;
    final int h;
    final int u;
    final int typeOrBobjIndex = script.params_20[0].get();
    final int textureIndex = script.params_20[1].get();
    final int type = typeOrBobjIndex & 0xff00_0000;
    if(type == 0) {
      //LAB_80115f54
      final BattleObject bobj = SCRIPTS.getObject(typeOrBobjIndex, BattleObject.class);
      final int uvs;
      if(!BattleObject.EM__.equals(bobj.magic_00)) {
        //LAB_8011604c
        uvs = vramSlotUvs_800fb0ec[((BattleEntity27c)bobj).model_148.uvAdjustments_9d.index];
        u = (uvs & 0xf) << 6;
        v = (uvs & 0x10) << 4;
        w = 0x100;
        h = 0x100;
      } else {
        final EffectManagerData6c<?> manager = (EffectManagerData6c<?>)bobj;
        final int managerType = manager.flags_04 & 0xff00_0000;
        if(managerType != 0x200_0000) {
          if(managerType == 0x300_0000) {
            //LAB_80116014
            throw new RuntimeException("ASM is bugged");
            //LAB_80115fc8
          } else if(managerType == 0x400_0000) {
            //LAB_8011602c
            final StarChildrenMeteorEffect10 effect = (StarChildrenMeteorEffect10)manager.effect_44;
            u = effect.metrics_04.u_00;
            v = effect.metrics_04.v_02;
            w = effect.metrics_04.w_04;
            h = effect.metrics_04.h_05;
          } else {
            throw new RuntimeException("Invalid state");
          }
        } else {
          //LAB_80115fd8
          uvs = vramSlotUvs_800fb0ec[((ModelEffect13c)manager.effect_44).model_134.uvAdjustments_9d.index];
          u = (uvs & 0xf) << 6;
          v = (uvs & 0x10) << 4;
          w = 256;
          h = 256;
        }
      }
    } else if(type == 0x200_0000) {
      //LAB_80116098
      final DeffPart.AnimatedTmdType animatedTmdType = (DeffPart.AnimatedTmdType)deffManager_800c693c.getDeffPart(typeOrBobjIndex);
      final DeffPart.TextureInfo textureInfo = animatedTmdType.textureInfo_08[0];
      u = textureInfo.vramPos_00.x;
      v = textureInfo.vramPos_00.y;
      w = 256;
      h = 256;
    } else if(type == 0x100_0000 || type == 0x300_0000) {
      //LAB_801160c0
      //LAB_801160d4
      final DeffPart.TmdType tmdType = (DeffPart.TmdType)deffManager_800c693c.getDeffPart(typeOrBobjIndex);
      final DeffPart.TextureInfo textureInfo = tmdType.textureInfo_08[textureIndex * 2];
      u = textureInfo.vramPos_00.x;
      v = textureInfo.vramPos_00.y;
      w = textureInfo.vramPos_00.w * 4;
      h = textureInfo.vramPos_00.h;
      //LAB_80115f34
    } else if(type == 0x400_0000) {
      //LAB_801160f4
      final BillboardSpriteEffect0c sp0x10 = new BillboardSpriteEffect0c();
      sp0x10.set(typeOrBobjIndex & 0xff_ffff);
      u = sp0x10.metrics_04.u_00;
      v = sp0x10.metrics_04.v_02;
      w = sp0x10.metrics_04.w_04;
      h = sp0x10.metrics_04.h_05;
    } else {
      throw new RuntimeException("Invalid state");
    }

    //LAB_80116118
    script.params_20[2].set(u);
    script.params_20[3].set(v);
    script.params_20[4].set(w);
    script.params_20[5].set(h);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unused in SC")
  @Method(0x80116160L)
  public static FlowControl scriptConsolidateEffectMemory(final RunningScript<?> script) {
    // Used to reallocate memory to consolidate it and prevent fragmentation (I think)
    return FlowControl.CONTINUE;
  }

  /**
   * TODO used for rendering deffs of some kind, possibly sprite deffs?
   *   Uses CTMD render pipeline if type == 0x300_0000
   */
  @Method(0x8011619cL)
  public static void FUN_8011619c(final EffectManagerData6c<EffectManagerParams.AnimType> manager, final LmbAnimationEffect5c effect, final int deffFlags, final MV matrix) {
    final MV sp0x10 = new MV();
    sp0x10.rotationZYX(manager.params_10.rot_10);
    sp0x10.transfer.set(manager.params_10.trans_04);
    sp0x10.scaleLocal(manager.params_10.scale_16);
    sp0x10.compose(matrix, sp0x10);
    final float scale = manager.params_10.scale_28 / (float)0x1000;
    sp0x10.scaleLocal(scale, scale, scale);
    manager.params_10.scale_16.mul(scale);

    final int type = deffFlags & 0xff00_0000;
    if(type == 0x300_0000) {
      //LAB_80116708
      final TmdObjTable1c tmdObjTable;
      // If we already have it cached
      if(effect.deffTmdFlags_48 == deffFlags) {
        tmdObjTable = effect.deffTmdObjTable_4c;
      } else {
        //LAB_80116724
        if((deffFlags & 0xf_ff00) == 0xf_ff00) {
          tmdObjTable = deffManager_800c693c.tmds_2f8[deffFlags & 0xff];
        } else {
          //LAB_80116750
          tmdObjTable = ((DeffPart.TmdType)deffManager_800c693c.getDeffPart(deffFlags | 0x300_0000)).tmd_0c.tmdPtr_00.tmd.objTable[0];
        }

        //LAB_8011676c
        // Cache it
        effect.deffTmdFlags_48 = deffFlags;
        effect.deffTmdObjTable_4c = tmdObjTable;

        if(effect.obj != null) {
          effect.obj.delete();
        }

        effect.obj = TmdObjLoader.fromObjTable(manager.name, effect.deffTmdObjTable_4c);
      }

      //LAB_80116778
      renderTmdSpriteEffect(tmdObjTable, effect.obj, manager.params_10, sp0x10);
    } else if(type == 0x400_0000) {
      if(effect.deffSpriteFlags_50 != deffFlags) {
        //LAB_801162e8
        final BillboardSpriteEffect0c sp0x48 = new BillboardSpriteEffect0c();

        sp0x48.set(deffFlags);
        effect.metrics_54.u_00 = sp0x48.metrics_04.u_00;
        effect.metrics_54.v_02 = sp0x48.metrics_04.v_02;
        effect.metrics_54.w_04 = sp0x48.metrics_04.w_04;
        effect.metrics_54.h_05 = sp0x48.metrics_04.h_05;
        effect.metrics_54.clut_06 = sp0x48.metrics_04.clut_06;
        effect.deffSpriteFlags_50 = deffFlags;
      }

      final int u = (effect.metrics_54.u_00 & 0x3f) * 4;
      final int v = effect.metrics_54.v_02 & 0xff;
      final int w = effect.metrics_54.w_04 & 0xff;
      final int h = effect.metrics_54.h_05 & 0xff;
      final int clut = effect.metrics_54.clut_06;

      //LAB_8011633c
      final Vector3f sp0x58 = new Vector3f();
      final MV sp0x60 = new MV();
      final Vector2f sp0x80 = new Vector2f();
      sp0x10.compose(worldToScreenMatrix_800c3548, sp0x60);
      GTE.setTransforms(sp0x60);

      final float z = perspectiveTransform(sp0x58, sp0x80);
      if(z >= 0x50) {
        //LAB_801163c4
        final float a1 = projectionPlaneDistance_1f8003f8 * 2.0f / z * manager.params_10.scale_16.z;
        final float l = -w / 2.0f * a1;
        final float r = w / 2.0f * a1;
        final float t = -h / 2.0f * a1;
        final float b = h / 2.0f * a1;
        final float sin = MathHelper.sin(manager.params_10.rot_10.z);
        final float cos = MathHelper.cos(manager.params_10.rot_10.z);
        final float sinL = l * sin;
        final float cosL = l * cos;
        final float sinR = r * sin;
        final float cosR = r * cos;
        final float sinT = t * sin;
        final float cosT = t * cos;
        final float sinB = b * sin;
        final float cosB = b * cos;

        final GpuCommandPoly cmd = new GpuCommandPoly(4)
          .clut((clut & 0b111111) * 16, clut >>> 6)
          .vramPos(effect.metrics_54.u_00 & 0x3c0, (effect.metrics_54.v_02 & 0x100) != 0 ? 256 : 0)
          .rgb(manager.params_10.colour_1c)
          .pos(0, sp0x80.x + cosL - sinT, sp0x80.y + sinL + cosT)
          .pos(1, sp0x80.x + cosR - sinT, sp0x80.y + sinR + cosT)
          .pos(2, sp0x80.x + cosL - sinB, sp0x80.y + sinL + cosB)
          .pos(3, sp0x80.x + cosR - sinB, sp0x80.y + sinR + cosB)
          .uv(0, u, v)
          .uv(1, u + w - 1, v)
          .uv(2, u, v + h - 1)
          .uv(3, u + w - 1, v + h - 1);

        if((manager.params_10.flags_00 >>> 30 & 1) != 0) {
          cmd.translucent(Translucency.of(manager.params_10.flags_00 >>> 28 & 0b11));
        }

        GPU.queueCommand(z / 4.0f, cmd);
      }
    } else {
      //LAB_80116790
      final ScriptState<EffectManagerData6c<?>> state = (ScriptState<EffectManagerData6c<?>>)scriptStatePtrArr_800bc1c0[deffFlags];
      final EffectManagerData6c<?> manager2 = state.innerStruct_00;
      manager.params_10.trans_04.set(sp0x10.transfer);
      getRotationAndScaleFromTransforms(manager.params_10.rot_10, manager.params_10.scale_16, sp0x10);

      final int oldScriptIndex = manager2.scriptIndex_0c;
      final int oldCoord2Index = manager2.coord2Index_0d;
      manager2.scriptIndex_0c = manager.myScriptState_0e.index;
      manager2.coord2Index_0d = -1;
      final int r = manager2.params_10.colour_1c.x;
      final int g = manager2.params_10.colour_1c.y;
      final int b = manager2.params_10.colour_1c.z;
      // As far as I can tell, using R for each of these is right...
      manager2.params_10.colour_1c.x = manager.params_10.colour_1c.x * manager2.params_10.colour_1c.x / 128;
      manager2.params_10.colour_1c.y = manager.params_10.colour_1c.x * manager2.params_10.colour_1c.y / 128;
      manager2.params_10.colour_1c.z = manager.params_10.colour_1c.x * manager2.params_10.colour_1c.z / 128;
      state.renderer_08.accept(state, manager2);
      manager2.params_10.colour_1c.x = r;
      manager2.params_10.colour_1c.y = g;
      manager2.params_10.colour_1c.z = b;
      manager2.scriptIndex_0c = oldScriptIndex;
      manager2.coord2Index_0d = oldCoord2Index;
    }

    //LAB_801168b8
  }

  @Method(0x801168e8L)
  public static void processLmbType0(final EffectManagerData6c<EffectManagerParams.AnimType> manager, final LmbAnimationEffect5c effect, final int a2, final MV matrix) {
    final LmbType0 lmb = (LmbType0)effect.lmb_0c;
    final int s6 = a2 / 0x2000;
    final int v1 = s6 + 1;
    final int s0 = a2 & 0x1fff;
    final int s1 = 0x2000 - s0;
    final int fp = v1 % lmb.partAnimations_08[0].count_04;

    //LAB_80116960
    for(int i = 0; i < lmb.objectCount_04; i++) {
      if(effect._14[lmb.partAnimations_08[i]._00] != 0) {
        final LmbTransforms14 a1 = lmb.partAnimations_08[i].keyframes_08[s6];
        final LmbTransforms14 a0 = lmb.partAnimations_08[i].keyframes_08[fp];
        manager.params_10.rot_10.set(a1.rot_0c);
        manager.params_10.trans_04.x = (a1.trans_06.x * s1 + a0.trans_06.x * s0) / 0x2000;
        manager.params_10.trans_04.y = (a1.trans_06.y * s1 + a0.trans_06.y * s0) / 0x2000;
        manager.params_10.trans_04.z = (a1.trans_06.z * s1 + a0.trans_06.z * s0) / 0x2000;
        manager.params_10.scale_16.x = (a1.scale_00.x * s1 + a0.scale_00.x * s0) / 0x2000;
        manager.params_10.scale_16.y = (a1.scale_00.y * s1 + a0.scale_00.y * s0) / 0x2000;
        manager.params_10.scale_16.z = (a1.scale_00.z * s1 + a0.scale_00.z * s0) / 0x2000;
        FUN_8011619c(manager, effect, effect._14[lmb.partAnimations_08[i]._00], matrix);
      }
    }
  }

  @Method(0x80116b7cL)
  public static void processLmbType1(final EffectManagerData6c<EffectManagerParams.AnimType> manager, final LmbAnimationEffect5c effect, final int t0, final MV matrix) {
    final LmbType1 lmb = (LmbType1)effect.lmb_0c;
    int a0 = t0 >> 13;
    float lerpScale = (t0 & 0x1fff) / (float)0x2000;
    int s5 = (a0 + 1) % lmb.keyframeCount_0a;
    final LmbTransforms14[] s7 = effect.lmbTransforms_10;
    if(effect._04 != t0) {
      if(s5 == 0) {
        if(a0 == 0) {
          return;
        }

        s5 = a0;
        a0 = 0;
        lerpScale = 1.0f - lerpScale;
      }

      //LAB_80116c20
      if(a0 == 0) {
        for(int i = 0; i < lmb.objectCount_04; i++) {
          s7[i].set(lmb._10[i]);
        }
      } else {
        //LAB_80116c50
        int a0_0 = (a0 - 1) * lmb._08 / 2;

        //LAB_80116c80
        for(int i = 0; i < lmb.objectCount_04; i++) {
          final LmbTransforms14 transforms = s7[i];
          final LmbType1.Sub04 v1 = lmb._0c[i];

          if((v1.transformsType_00 & 0x8000) == 0) {
            transforms.scale_00.x = lmb._14[a0_0];
            a0_0++;
          }

          //LAB_80116ca0
          if((v1.transformsType_00 & 0x4000) == 0) {
            transforms.scale_00.y = lmb._14[a0_0];
            a0_0++;
          }

          //LAB_80116cc0
          if((v1.transformsType_00 & 0x2000) == 0) {
            transforms.scale_00.z = lmb._14[a0_0];
            a0_0++;
          }

          //LAB_80116ce0
          if((v1.transformsType_00 & 0x1000) == 0) {
            transforms.trans_06.x = lmb._14[a0_0];
            a0_0++;
          }

          //LAB_80116d00
          if((v1.transformsType_00 & 0x800) == 0) {
            transforms.trans_06.y = lmb._14[a0_0];
            a0_0++;
          }

          //LAB_80116d20
          if((v1.transformsType_00 & 0x400) == 0) {
            transforms.trans_06.z = lmb._14[a0_0];
            a0_0++;
          }

          //LAB_80116d40
          if((v1.transformsType_00 & 0x200) == 0) {
            transforms.rot_0c.x = MathHelper.psxDegToRad(lmb._14[a0_0]);
            a0_0++;
          }

          //LAB_80116d60
          if((v1.transformsType_00 & 0x100) == 0) {
            transforms.rot_0c.y = MathHelper.psxDegToRad(lmb._14[a0_0]);
            a0_0++;
          }

          //LAB_80116d80
          if((v1.transformsType_00 & 0x80) == 0) {
            transforms.rot_0c.z = MathHelper.psxDegToRad(lmb._14[a0_0]);
            a0_0++;
          }
        }
      }

      //LAB_80116db8
      int a0_0 = (s5 - 1) * lmb._08 / 2;

      //LAB_80116de8
      for(int i = 0; i < lmb.objectCount_04; i++) {
        final LmbTransforms14 transforms = s7[i];
        final LmbType1.Sub04 a2 = lmb._0c[i];

        if((a2.transformsType_00 & 0x8000) == 0) {
          transforms.scale_00.x = Math.lerp(lmb._14[a0_0], transforms.scale_00.x, lerpScale);
          a0_0++;
        }

        //LAB_80116e34
        if((a2.transformsType_00 & 0x4000) == 0) {
          transforms.scale_00.y = Math.lerp(lmb._14[a0_0], transforms.scale_00.y, lerpScale);
          a0_0++;
        }

        //LAB_80116e80
        if((a2.transformsType_00 & 0x2000) == 0) {
          transforms.scale_00.z = Math.lerp(lmb._14[a0_0], transforms.scale_00.z, lerpScale);
          a0_0++;
        }

        //LAB_80116ecc
        if((a2.transformsType_00 & 0x1000) == 0) {
          transforms.trans_06.x = Math.lerp(lmb._14[a0_0], transforms.trans_06.x, lerpScale);
          a0_0++;
        }

        //LAB_80116f18
        if((a2.transformsType_00 & 0x800) == 0) {
          transforms.trans_06.y = Math.lerp(lmb._14[a0_0], transforms.trans_06.y, lerpScale);
          a0_0++;
        }

        //LAB_80116f64
        if((a2.transformsType_00 & 0x400) == 0) {
          transforms.trans_06.z = Math.lerp(lmb._14[a0_0], transforms.trans_06.z, lerpScale);
          a0_0++;
        }

        //LAB_80116fb0
        if((a2.transformsType_00 & 0x200) == 0) {
          a0_0++;
        }

        //LAB_80116fc8
        if((a2.transformsType_00 & 0x100) == 0) {
          a0_0++;
        }

        //LAB_80116fd4
        if((a2.transformsType_00 & 0x80) == 0) {
          a0_0++;
        }
      }

      //LAB_80116ff8
      effect._04 = t0;
    }

    //LAB_80116ffc
    //LAB_80117014
    for(int i = 0; i < lmb.objectCount_04; i++) {
      final LmbTransforms14 transforms = s7[i];

      final int deffFlags = effect._14[lmb._0c[i]._03];
      if(deffFlags != 0) {
        manager.params_10.rot_10.set(transforms.rot_0c);
        manager.params_10.trans_04.set(transforms.trans_06);
        manager.params_10.scale_16.set(transforms.scale_00);

        FUN_8011619c(manager, effect, deffFlags, matrix);
      }
    }

    //LAB_801170d4
  }

  @Method(0x80117104L)
  public static void processLmbType2(final EffectManagerData6c<EffectManagerParams.AnimType> manager, final LmbAnimationEffect5c effect, final int t5, final MV matrix) {
    final LmbType2 lmb = (LmbType2)effect.lmb_0c;
    final LmbTransforms14[] originalTransforms = lmb.initialTransforms_10;
    final int keyframeIndex = t5 / 0x2000;
    final float lerpScale = (t5 & 0x1fff) / (float)0x2000;
    final LmbTransforms14[] transformsLo = effect.lmbTransforms_10;
    final LmbTransforms14[] transformsHi = Arrays.copyOfRange(transformsLo, lmb.objectCount_04, transformsLo.length);
    final int nextKeyframeIndex = (keyframeIndex + 1) % lmb.keyframeCount_0a;
    if(effect._04 != t5) {
      int s1 = effect._04 / 0x2000;

      if(nextKeyframeIndex == 0 && keyframeIndex == 0) {
        return;
      }

      //LAB_801171c8
      if(s1 > keyframeIndex) {
        s1 = 0;

        for(int i = 0; i < lmb.objectCount_04; i++) {
          transformsLo[i].scale_00.set(originalTransforms[i].scale_00);
          transformsLo[i].trans_06.set(originalTransforms[i].trans_06);
          transformsLo[i].rot_0c.set(originalTransforms[i].rot_0c);
        }
      }

      //LAB_801171f8
      //LAB_8011720c
      for(; s1 < keyframeIndex; s1++) {
        int a0 = s1 * lmb.transformDataPairCount_08;

        //LAB_80117234
        for(int i = 0; i < lmb.transformDataPairCount_08 * 2; i += 2) {
          lmbType2TransformationData_8011a048[i] = (byte)(lmb._14[a0] >> 4);
          lmbType2TransformationData_8011a048[i + 1] = (byte)(lmb._14[a0] << 28 >> 28);
          a0++;
        }

        //LAB_80117270
        //LAB_8011728c
        int index = 0;
        for(int i = 0; i < lmb.objectCount_04; i++) {
          final LmbTransforms14 transform = transformsLo[i];

          final int flags = lmb.flags_0c[i];

          if((flags & 0xe000) != 0xe000) {
            final int shift = lmbType2TransformationData_8011a048[index++] & 0xf;

            if((flags & 0x8000) == 0) {
              transform.scale_00.x += lmbType2TransformationData_8011a048[index++] << shift;
            }

            //LAB_801172c8
            if((flags & 0x4000) == 0) {
              transform.scale_00.y += lmbType2TransformationData_8011a048[index++] << shift;
            }

            //LAB_801172f0
            if((flags & 0x2000) == 0) {
              transform.scale_00.z += lmbType2TransformationData_8011a048[index++] << shift;
            }
          }

          //LAB_80117310
          //LAB_80117314
          if((flags & 0x1c00) != 0x1c00) {
            final int shift = lmbType2TransformationData_8011a048[index++] & 0xf;

            if((flags & 0x1000) == 0) {
              transform.trans_06.x += lmbType2TransformationData_8011a048[index++] << shift;
            }

            //LAB_80117348
            if((flags & 0x800) == 0) {
              transform.trans_06.y += lmbType2TransformationData_8011a048[index++] << shift;
            }

            //LAB_80117370
            if((flags & 0x400) == 0) {
              transform.trans_06.z += lmbType2TransformationData_8011a048[index++] << shift;
            }
          }

          //LAB_80117390
          //LAB_80117394
          if((flags & 0x380) != 0x380) {
            final int shift = lmbType2TransformationData_8011a048[index++] & 0xf;

            if((flags & 0x200) == 0) {
              transform.rot_0c.x += lmbType2TransformationData_8011a048[index++] << shift;
            }

            //LAB_801173c8
            if((flags & 0x100) == 0) {
              transform.rot_0c.y += lmbType2TransformationData_8011a048[index++] << shift;
            }

            //LAB_801173f0
            if((flags & 0x80) == 0) {
              transform.rot_0c.z += lmbType2TransformationData_8011a048[index++] << shift;
            }
          }
        }
      }

      //LAB_80117438
      if(nextKeyframeIndex == 0) {
        //LAB_801176c0
        //LAB_801176e0
        for(int i = 0; i < lmb.objectCount_04; i++) {
          final LmbTransforms14 originalTransform = originalTransforms[i];
          final LmbTransforms14 transformLo = transformsLo[i];
          final LmbTransforms14 transformHi = transformsHi[i];

          final int flags = lmb.flags_0c[i];

          if((flags & 0x8000) == 0) {
            transformHi.scale_00.x = Math.lerp(originalTransform.scale_00.x, transformLo.scale_00.x, lerpScale);
          }

          //LAB_80117730
          if((flags & 0x4000) == 0) {
            transformHi.scale_00.y = Math.lerp(originalTransform.scale_00.y, transformLo.scale_00.y, lerpScale);
          }

          //LAB_80117774
          if((flags & 0x2000) == 0) {
            transformHi.scale_00.z = Math.lerp(originalTransform.scale_00.z, transformLo.scale_00.z, lerpScale);
          }

          //LAB_801177b8
          if((flags & 0x1000) == 0) {
            transformHi.trans_06.x = Math.lerp(originalTransform.trans_06.x, transformLo.trans_06.x, lerpScale);
          }

          //LAB_801177fc
          if((flags & 0x800) == 0) {
            transformHi.trans_06.y = Math.lerp(originalTransform.trans_06.y, transformLo.trans_06.y, lerpScale);
          }

          //LAB_80117840
          if((flags & 0x400) == 0) {
            transformHi.trans_06.z = Math.lerp(originalTransform.trans_06.z, transformLo.trans_06.z, lerpScale);
          }
        }
      } else {
        int a0 = (nextKeyframeIndex - 1) * lmb.transformDataPairCount_08;

        //LAB_80117470
        for(int i = 0; i < lmb.transformDataPairCount_08 * 2; i += 2) {
          lmbType2TransformationData_8011a048[i] = (byte)(lmb._14[a0] >> 4);
          lmbType2TransformationData_8011a048[i + 1] = (byte)(lmb._14[a0] << 28 >> 28);
          a0++;
        }

        //LAB_801174ac
        //LAB_801174d0
        int index = 0;
        for(int i = 0; i < lmb.objectCount_04; i++) {
          final LmbTransforms14 transformLo = transformsLo[i];
          final LmbTransforms14 transformHi = transformsHi[i];

          final int flags = lmb.flags_0c[i];

          if((flags & 0xe000) != 0xe000) {
            final int shift = lmbType2TransformationData_8011a048[index++] & 0xf;

            if((flags & 0x8000) == 0) {
              transformHi.scale_00.x = transformLo.scale_00.x + (lmbType2TransformationData_8011a048[index++] << shift) * lerpScale;
            }

            //LAB_80117524
            if((flags & 0x4000) == 0) {
              transformHi.scale_00.y = transformLo.scale_00.y + (lmbType2TransformationData_8011a048[index++] << shift) * lerpScale;
            }

            //LAB_80117564
            if((flags & 0x2000) == 0) {
              transformHi.scale_00.z = transformLo.scale_00.z + (lmbType2TransformationData_8011a048[index++] << shift) * lerpScale;
            }
          }

          //LAB_8011759c
          //LAB_801175a0
          if((flags & 0x1c00) != 0x1c00) {
            final int shift = lmbType2TransformationData_8011a048[index++] & 0xf;

            if((flags & 0x1000) == 0) {
              transformHi.trans_06.x = transformLo.trans_06.x + (lmbType2TransformationData_8011a048[index++] << shift) * lerpScale;
            }

            //LAB_801175ec
            if((flags & 0x800) == 0) {
              transformHi.trans_06.y = transformLo.trans_06.y + (lmbType2TransformationData_8011a048[index++] << shift) * lerpScale;
            }

            //LAB_8011762c
            if((flags & 0x400) == 0) {
              transformHi.trans_06.z = transformLo.trans_06.z + (lmbType2TransformationData_8011a048[index++] << shift) * lerpScale;
            }
          }

          //LAB_80117664
          //LAB_80117668
          if((flags & 0x380) != 0x380) {
            index++;

            if((flags & 0x200) == 0) {
              index++;
            }

            //LAB_80117680
            if((flags & 0x100) == 0) {
              index++;
            }

            //LAB_80117690
            if((flags & 0x80) == 0) {
              index++;
            }
          }
        }
      }

      //LAB_801178a0
      effect._04 = t5;
    }

    //LAB_801178a4
    //LAB_801178c0
    for(int i = 0; i < lmb.objectCount_04; i++) {
      final LmbTransforms14 transformLo = transformsLo[i];
      final LmbTransforms14 transformHi = transformsHi[i];
      final int flags = lmb.flags_0c[i];

      if(effect._14[flags >>> 24] != 0) {
        if(manager.params_10._2c != 0) {
          manager.params_10.rot_10.set(transformLo.rot_0c);
        } else {
          //LAB_80117914
          manager.params_10.rot_10.set(transformHi.rot_0c);
        }

        //LAB_80117938
        manager.params_10.trans_04.set(transformHi.trans_06);
        manager.params_10.scale_16.set(transformHi.scale_00);
        FUN_8011619c(manager, effect, effect._14[flags >>> 24], matrix);
      }
    }
    //LAB_801179c0
  }

  @Method(0x801179f0L)
  public static void lmbAnimationRenderer(final ScriptState<EffectManagerData6c<EffectManagerParams.AnimType>> state, final EffectManagerData6c<EffectManagerParams.AnimType> manager) {
    final int flags = manager.params_10.flags_00;
    final LmbAnimationEffect5c effect = (LmbAnimationEffect5c)manager.effect_44;
    if(flags >= 0) { // No errors
      int s1 = Math.max(0, manager.params_10.ticks_24) % (effect.keyframeCount_08 * 2) << 12;
      tmdGp0Tpage_1f8003ec = flags >>> 23 & 0x60; // tpage
      zOffset_1f8003e8 = manager.params_10.z_22;
      if((manager.params_10.flags_00 & 0x40) == 0) {
        FUN_800e61e4(manager.params_10.colour_1c.x / 128.0f, manager.params_10.colour_1c.y / 128.0f, manager.params_10.colour_1c.z / 128.0f);
      }

      //LAB_80117ac0
      //LAB_80117acc
      final EffectManagerData6c<EffectManagerParams.AnimType> anim = new EffectManagerData6c<>("Temp 2", new EffectManagerParams.AnimType());
      anim.set(manager);

      final MV sp0x80 = new MV();
      calculateEffectTransforms(sp0x80, manager);

      final int type = effect.lmbType_00 & 0x7;
      if(type == 0) {
        //LAB_80117b50
        processLmbType0(manager, effect, s1, sp0x80);
      } else if(type == 1) {
        //LAB_80117b68
        processLmbType1(manager, effect, s1, sp0x80);
      } else if(type == 2) {
        //LAB_80117b80
        processLmbType2(manager, effect, s1, sp0x80);
      }

      //LAB_80117b8c
      final int steps = effect._38;

      if(steps >= 2) {
        final int spe4 = effect._3c;
        int accumulatorR;
        int accumulatorG;
        int accumulatorB;
        final int stepR;
        final int stepG;
        final int stepB;
        if((effect.flags_34 & 0x4) != 0) {
          final int v0 = effect._40 - 0x1000;
          stepR = anim.params_10.colour_1c.x * v0 / steps;
          stepG = anim.params_10.colour_1c.y * v0 / steps;
          stepB = anim.params_10.colour_1c.z * v0 / steps;
          accumulatorR = anim.params_10.colour_1c.x << 12;
          accumulatorG = anim.params_10.colour_1c.y << 12;
          accumulatorB = anim.params_10.colour_1c.z << 12;
        } else {
          //LAB_80117c30
          accumulatorR = 0;
          accumulatorG = 0;
          accumulatorB = 0;
          stepR = 0;
          stepG = 0;
          stepB = 0;
        }

        //LAB_80117c48
        int accumulatorScale = 0;
        int stepScale = 0;
        if((effect.flags_34 & 0x8) != 0) {
          final int t4 = anim.params_10.scale_28 * (effect._40 - 0x1000);
          accumulatorScale = anim.params_10.scale_28 << 12;
          stepScale = t4 / steps;
        }

        //LAB_80117c88
        s1 -= spe4;

        //LAB_80117ca0
        for(int i = 1; i < steps && s1 >= 0; i++) {
          if((effect.flags_34 & 0x4) != 0) {
            accumulatorR += stepR;
            accumulatorG += stepG;
            accumulatorB += stepB;
            manager.params_10.colour_1c.x = accumulatorR >> 12;
            manager.params_10.colour_1c.y = accumulatorG >> 12;
            manager.params_10.colour_1c.z = accumulatorB >> 12;
          }

          //LAB_80117d1c
          if((effect.flags_34 & 0x8) != 0) {
            accumulatorScale += stepScale;
            manager.params_10.scale_28 = accumulatorScale >> 12;
          }

          //LAB_80117d54
          if(type == 0) {
            //LAB_80117dc4
            processLmbType0(manager, effect, s1, sp0x80);
          } else if(type == 1) {
            //LAB_80117ddc
            processLmbType1(manager, effect, s1, sp0x80);
          } else if(type == 2) {
            //LAB_80117df4
            processLmbType2(manager, effect, s1, sp0x80);
          }

          //LAB_80117e04
          s1 -= spe4;
        }
      }

      //LAB_80117e14
      //LAB_80117e20
      manager.set(anim);

      if((manager.params_10.flags_00 & 0x40) == 0) {
        FUN_800e62a8();
      }
    }

    //LAB_80117e80
  }

  /** Used by down burst and night raid */
  @ScriptDescription("Allocates an LMB animation")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "lmbFlags", description = "Unknown, selects which LMB to use")
  @Method(0x80117eb0L)
  public static FlowControl scriptAllocateLmbAnimation(final RunningScript<? extends BattleObject> script) {
    final int param1 = script.params_20[1].get();
    final ScriptState<EffectManagerData6c<EffectManagerParams.AnimType>> state = allocateEffectManager(
      "LMB animation",
      script.scriptState_04,
      null,
      SEffe::lmbAnimationRenderer,
      (s, manager) -> {
        if(((LmbAnimationEffect5c)manager.effect_44).obj != null) {
          ((LmbAnimationEffect5c)manager.effect_44).obj.delete();
        }
      },
      new LmbAnimationEffect5c(),
      new EffectManagerParams.AnimType()
    );

    final EffectManagerData6c<EffectManagerParams.AnimType> manager = state.innerStruct_00;
    manager.flags_04 = 0;

    final DeffPart.LmbType lmbType;
    if((param1 & 0xf_ff00) == 0xf_ff00) {
      lmbType = deffManager_800c693c.lmbs_390[param1 & 0xff];
    } else {
      //LAB_80117f58
      lmbType = (DeffPart.LmbType)deffManager_800c693c.getDeffPart(param1);
    }

    //LAB_80117f68
    final LmbAnimationEffect5c effect = (LmbAnimationEffect5c)manager.effect_44;
    effect.lmbType_00 = lmbType.type_04;
    effect._04 = 0;
    effect.lmb_0c = lmbType.lmb_08;
    effect.lmbTransforms_10 = null;
    effect._38 = 1;
    effect._3c = 0x1000;
    effect.deffTmdFlags_48 = -1;
    effect.deffSpriteFlags_50 = -1;

    //LAB_80117fc4
    for(int i = 0; i < 8; i++) {
      effect._14[i] = 0;
    }

    final int type = effect.lmbType_00 & 0x7;
    if(type == 0) {
      //LAB_80118004
      final LmbType0 lmb = (LmbType0)effect.lmb_0c;
      effect.keyframeCount_08 = lmb.partAnimations_08[0].count_04;
    } else if(type == 1) {
      //LAB_80118018
      final LmbType1 lmb = (LmbType1)effect.lmb_0c;
      effect.keyframeCount_08 = lmb.keyframeCount_0a;
      effect.lmbTransforms_10 = new LmbTransforms14[lmb.objectCount_04];

      for(int i = 0; i < lmb.objectCount_04; i++) {
        effect.lmbTransforms_10[i] = new LmbTransforms14().set(lmb._10[i]);
      }
    } else if(type == 2) {
      //LAB_80118068
      final LmbType2 lmb = (LmbType2)effect.lmb_0c;
      effect.keyframeCount_08 = lmb.keyframeCount_0a;
      effect.lmbTransforms_10 = new LmbTransforms14[lmb.objectCount_04 * 2];

      for(int i = 0; i < lmb.objectCount_04; i++) {
        effect.lmbTransforms_10[i] = new LmbTransforms14().set(lmb.initialTransforms_10[i]);
        effect.lmbTransforms_10[i + lmb.objectCount_04] = new LmbTransforms14().set(lmb.initialTransforms_10[i]);
      }
    }

    //LAB_801180e0
    //LAB_801180e8
    manager.params_10.ticks_24 = -1;
    manager.params_10.flags_00 |= 0x5000_0000; // force B+F translucency
    addGenericAttachment(manager, 0, 0x100, 0);
    manager.params_10.scale_28 = 0x1000;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, something to do with LMB animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "value")
  @Method(0x801181a8L)
  public static FlowControl FUN_801181a8(final RunningScript<?> script) {
    final EffectManagerData6c<?> manager = SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class);
    ((LmbAnimationEffect5c)manager.effect_44)._14[script.params_20[1].get()] = script.params_20[2].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unknown, something to do with LMB animation")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p3")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p4")
  @Method(0x801181f0L)
  public static FlowControl FUN_801181f0(final RunningScript<?> script) {
    final LmbAnimationEffect5c effect = (LmbAnimationEffect5c)SCRIPTS.getObject(script.params_20[0].get(), EffectManagerData6c.class).effect_44;

    final int v1 = script.params_20[3].get() + 1;
    effect.flags_34 = script.params_20[1].get();
    effect._38 = script.params_20[2].get() * v1;
    effect._3c = 0x1000 / v1;
    effect._40 = script.params_20[4].get();
    return FlowControl.CONTINUE;
  }

  /** TODO renders other effects too? Burnout, more? Uses CTMD render pipeline */
  @Method(0x8011826cL)
  public static void renderDeffTmd(final ScriptState<EffectManagerData6c<EffectManagerParams.AnimType>> state, final EffectManagerData6c<EffectManagerParams.AnimType> data) {
    final DeffTmdRenderer14 deffEffect = (DeffTmdRenderer14)data.effect_44;

    if(data.params_10.flags_00 >= 0) {
      final MV sp0x10 = new MV();
      calculateEffectTransforms(sp0x10, data);
      if((data.params_10.flags_00 & 0x4000_0000) != 0) {
        tmdGp0Tpage_1f8003ec = data.params_10.flags_00 >>> 23 & 0x60;
      } else {
        //LAB_801182bc
        tmdGp0Tpage_1f8003ec = deffEffect.tpage_10;
      }

      //LAB_801182c8
      zOffset_1f8003e8 = data.params_10.z_22;
      if((data.params_10.flags_00 & 0x40) == 0) {
        FUN_800e61e4(data.params_10.colour_1c.x / 128.0f, data.params_10.colour_1c.y / 128.0f, data.params_10.colour_1c.z / 128.0f);
      } else {
        //LAB_80118304
        FUN_800e60e0(1.0f, 1.0f, 1.0f);
      }

      //LAB_80118314
      if(data.scriptIndex_0c < -2) {
        if(data.scriptIndex_0c == -4) {
          sp0x10.transfer.z = projectionPlaneDistance_1f8003f8;
        }

        //LAB_8011833c
        GsSetLightMatrix(sp0x10);
        GTE.setTransforms(sp0x10);

        final ModelPart10 dobj2 = new ModelPart10();
        dobj2.attribute_00 = data.params_10.flags_00;
        dobj2.tmd_08 = deffEffect.tmd_08;

        final int oldZShift = zShift_1f8003c4;
        final int oldZMax = zMax_1f8003cc;
        final int oldZMin = zMin;
        zShift_1f8003c4 = 2;
        zMax_1f8003cc = 0xffe;
        zMin = 0xb;
        Renderer.renderDobj2(dobj2, false, 0);
        zShift_1f8003c4 = oldZShift;
        zMax_1f8003cc = oldZMax;
        zMin = oldZMin;

        RENDERER.queueModel(deffEffect.obj, sp0x10)
          .lightDirection(lightDirectionMatrix_800c34e8)
          .lightColour(lightColourMatrix_800c3508)
          .backgroundColour(GTE.backgroundColour)
          .ctmdFlags((dobj2.attribute_00 & 0x4000_0000) != 0 ? 0x12 : 0x0)
          .tmdTranslucency(tmdGp0Tpage_1f8003ec >>> 5 & 0b11)
          .battleColour(((Battle)currentEngineState_8004dd04)._800c6930.colour_00);
      } else {
        //LAB_80118370
        renderTmdSpriteEffect(deffEffect.tmd_08, deffEffect.obj, data.params_10, sp0x10);
      }

      //LAB_80118380
      if((data.params_10.flags_00 & 0x40) == 0) {
        FUN_800e62a8();
      } else {
        //LAB_801183a4
        FUN_800e6170();
      }
    }

    //LAB_801183ac
  }

  @ScriptDescription("Allocates a DEFF TMD effect manager")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flags", description = "The DEFF flags, mostly unknown")
  @Method(0x801183c0L)
  public static FlowControl allocateDeffTmd(final RunningScript<? extends BattleObject> script) {
    final int s1 = script.params_20[1].get();
    final String name;
    if((s1 & 0xf_ff00) == 0xf_ff00) {
      name = deffManager_800c693c.tmds_2f8[s1 & 0xff].name;
    } else {
      final DeffPart.TmdType tmdType = (DeffPart.TmdType)deffManager_800c693c.getDeffPart(s1 | 0x300_0000);
      name = tmdType.name;
    }

    final ScriptState<EffectManagerData6c<EffectManagerParams.AnimType>> state = allocateEffectManager(
      "DEFF TMD " + name,
      script.scriptState_04,
      null,
      SEffe::renderDeffTmd,
      (s, manager) -> ((DeffTmdRenderer14)manager.effect_44).obj.delete(),
      new DeffTmdRenderer14(),
      new EffectManagerParams.AnimType()
    );

    final EffectManagerData6c<EffectManagerParams.AnimType> manager = state.innerStruct_00;
    manager.flags_04 = 0x300_0000;

    final DeffTmdRenderer14 effect = (DeffTmdRenderer14)manager.effect_44;

    effect._00 = s1 | 0x300_0000;

    if((s1 & 0xf_ff00) == 0xf_ff00) {
      effect.tmdType_04 = null;
      effect.tmd_08 = deffManager_800c693c.tmds_2f8[s1 & 0xff];
      effect.tpage_10 = 0x20;
    } else {
      //LAB_8011847c
      final DeffPart.TmdType tmdType = (DeffPart.TmdType)deffManager_800c693c.getDeffPart(s1 | 0x300_0000);
      final TmdWithId tmdWithId = tmdType.tmd_0c.tmdPtr_00;
      effect.tmdType_04 = tmdType;
      effect.tmd_08 = tmdWithId.tmd.objTable[0];
      effect.tpage_10 = (int)((tmdWithId.id & 0xffff_0000L) >>> 11);
    }

    effect.obj = TmdObjLoader.fromObjTable(state.name, effect.tmd_08);

    //LAB_801184ac
    manager.params_10.flags_00 = 0x1400_0000;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Allocates a DEFF TMD renderer effect manager")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flags", description = "DEFF flags, not fully understood")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "objIndex", description = "The model object index")
  @Method(0x801184e4L)
  public static FlowControl allocateDeffTmdRenderer(final RunningScript<? extends BattleObject> script) {
    final int flags = script.params_20[1].get();
    final int objIndex = script.params_20[2].get();

    final TmdObjTable1c objTable;

    final int type = flags & 0xff00_0000;
    if(type == 0x100_0000) {
      //LAB_801185e4
      final DeffPart.AnimatedTmdType animatedTmdType = (DeffPart.AnimatedTmdType)deffManager_800c693c.getDeffPart(flags);
      final Tmd tmd = animatedTmdType.tmd_0c.tmdPtr_00.tmd;
      objTable = tmd.objTable[0];
    } else if(type == 0x200_0000) {
      //LAB_801185c0
      final DeffPart.AnimatedTmdType animatedTmdType = (DeffPart.AnimatedTmdType)deffManager_800c693c.getDeffPart(flags);
      objTable = animatedTmdType.tmd_0c.tmdPtr_00.optimisePacketsIfNecessary(objIndex);
      //LAB_801185b0
    } else if(type == 0x700_0000) {
      //LAB_80118610
      objTable = battlePreloadedEntities_1f8003f4.stage_963c.dobj2s_00[objIndex].tmd_08;
    } else {
      //LAB_80118634
      final BattleObject bobj = (BattleObject)scriptStatePtrArr_800bc1c0[flags].innerStruct_00;
      if(BattleObject.EM__.equals(bobj.magic_00)) {
        final EffectManagerData6c<?> effects = (EffectManagerData6c<?>)bobj;
        final int v1 = effects.flags_04 & 0xff00_0000;
        if(v1 == 0x100_0000 || v1 == 0x200_0000) {
          //LAB_8011867c
          objTable = ((ModelEffect13c)effects.effect_44).model_134.modelParts_00[objIndex].tmd_08;
        } else {
          objTable = null;
        }
      } else {
        //LAB_801186a4
        //LAB_801186b4
        objTable = ((BattleEntity27c)bobj).model_148.modelParts_00[objIndex].tmd_08;
      }
    }

    final ScriptState<EffectManagerData6c<EffectManagerParams.AnimType>> state = allocateEffectManager(
      objTable != null ? "Obj table renderer FUN_801184e4 " + objTable.name : "TMD renderer with no TMD? FUN_801184e4",
      script.scriptState_04,
      null,
      SEffe::renderDeffTmd,
      (s, manager) -> ((DeffTmdRenderer14)manager.effect_44).obj.delete(),
      new DeffTmdRenderer14(),
      new EffectManagerParams.AnimType()
    );

    final EffectManagerData6c<EffectManagerParams.AnimType> s4 = state.innerStruct_00;
    s4.flags_04 = 0x300_0000;

    final DeffTmdRenderer14 s0 = (DeffTmdRenderer14)s4.effect_44;
    s0.tpage_10 = 0x20;
    s0._00 = 0x300_0000;
    s0.tmdType_04 = null;
    s0.tmd_08 = objTable;

    s0.obj = TmdObjLoader.fromObjTable(state.name, s0.tmd_08);

    //LAB_801186bc
    //LAB_801186c0
    s4.params_10.flags_00 = 0x1400_0000;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x801186f8L)
  public static void getSpriteTmdFromSource(final TmdSpriteEffect10 spriteEffect, final int flags) {
    spriteEffect.flags_00 = flags | 0x300_0000;
    if((flags & 0xf_ff00) == 0xf_ff00) {
      spriteEffect.tmdType_04 = null;
      spriteEffect.tmd_08 = deffManager_800c693c.tmds_2f8[flags & 0xff];
      spriteEffect.tpage_10 = 0x20;
    } else {
      //LAB_80118750
      final DeffPart.TmdType tmdType = (DeffPart.TmdType)deffManager_800c693c.getDeffPart(flags | 0x300_0000);
      final TmdWithId tmdWithId = tmdType.tmd_0c.tmdPtr_00;
      spriteEffect.tmdType_04 = tmdType;
      spriteEffect.tmd_08 = tmdWithId.tmd.objTable[0];
      spriteEffect.tpage_10 = (int)((tmdWithId.id & 0xffff_0000L) >>> 11);
    }
    //LAB_80118780
  }

  /** Uses ctmd render pipeline */
  @Method(0x80118790L)
  public static void renderShadowEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> manager) {
    if(manager.params_10.flags_00 >= 0) { // No errors
      final float y = manager.params_10.trans_04.y;
      manager.params_10.trans_04.y = 0.0f;
      final MV sp0x10 = new MV();
      calculateEffectTransforms(sp0x10, manager);
      sp0x10.transfer.y = y;
      manager.params_10.trans_04.y = y;

      final float rotY = MathHelper.atan2(-sp0x10.m02, sp0x10.m00);
      sp0x10.rotateY(-rotY);
      sp0x10.rotateZ(-MathHelper.atan2(sp0x10.m01, sp0x10.m00));
      sp0x10.rotateX(-MathHelper.atan2(-sp0x10.m21, sp0x10.m22));
      sp0x10.rotateY(rotY);
      sp0x10.m01 = 0.0f;
      sp0x10.m11 = 0.0f;
      sp0x10.m21 = 0.0f;
      sp0x10.transfer.y -= 0.05f; // Fix Z-fighting with ground
      tmdGp0Tpage_1f8003ec = manager.params_10.flags_00 >>> 23 & 0x60;
      zOffset_1f8003e8 = manager.params_10.z_22;
      FUN_800e60e0(manager.params_10.colour_1c.x / 128.0f, manager.params_10.colour_1c.y / 128.0f, manager.params_10.colour_1c.z / 128.0f);
      renderTmdSpriteEffect(shadowModel_800bda10.modelParts_00[0].tmd_08, shadowModel_800bda10.modelParts_00[0].obj, manager.params_10, sp0x10);
      FUN_800e6170();
    }

    //LAB_801188d8
  }

  @ScriptDescription("Allocates a shadow effect")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @Method(0x801188ecL)
  public static FlowControl scriptAllocateShadowEffect(final RunningScript<? extends BattleObject> script) {
    final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state = allocateEffectManager(
      "Unknown (FUN_801188ec, %s)".formatted(shadowModel_800bda10.modelParts_00[0].tmd_08.name),
      script.scriptState_04,
      null,
      SEffe::renderShadowEffect,
      null,
      null
    );

    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;
    manager.flags_04 = 0x600_0000;
    manager.params_10.scale_16.set(0.25f, 0.25f, 0.25f);
    manager.params_10.flags_00 = 0x6400_0040; // B-F, vram X 256, use light colour
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Loads a new animation into an effect")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The effect index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "deffPartIndex", description = "The animation (DEFF part) index")
  @Method(0x80118984L)
  public static FlowControl scriptLoadEffectModelAnimation(final RunningScript<?> script) {
    final int effectIndex = script.params_20[0].get();
    final EffectManagerData6c<EffectManagerParams.AnimType> manager = SCRIPTS.getObject(effectIndex, EffectManagerData6c.classFor(EffectManagerParams.AnimType.class));
    final ModelEffect13c effect = (ModelEffect13c)manager.effect_44;

    final DeffPart part = deffManager_800c693c.getDeffPart(script.params_20[1].get() | 0x500_0000);
    final Anim anim = ((DeffPart.AnimatedTmdType)part).anim_14;

    effect.anim_0c = anim;
    anim.loadIntoModel(effect.model_134);
    manager.params_10.ticks_24 = 0;
    addGenericAttachment(manager, 0, 0x100, 0);
    return FlowControl.CONTINUE;
  }

  @Method(0x80118a24L)
  public static void renderShirleyTransformWipeEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.ShirleyType>> state, final EffectManagerData6c<EffectManagerParams.ShirleyType> manager) {
    final float x = manager.params_10.trans_04.x + 160 - manager.params_10.width_24 / 2.0f;
    final float y = manager.params_10.trans_04.y + 120 - manager.params_10.height_28 / 2.0f;
    final float minZ = (manager.params_10.trans_04.z - manager.params_10.depth_2c / 2.0f) / 4.0f;
    final float maxZ = (manager.params_10.trans_04.z + manager.params_10.depth_2c / 2.0f) / 4.0f;
    final int right = 320;
    final int bottom = 240;

    final Rect4i buffPos = new Rect4i();

    //LAB_80118ba8
    for(int i = 0; i < 4; i++) {
      buffPos.x = (int)(x + manager.params_10.width_24 / 2 * (i & 1));
      buffPos.y = (int)(y + manager.params_10.height_28 / 2 * (i >> 1));
      buffPos.w = manager.params_10.width_24 / 2;
      buffPos.h = manager.params_10.height_28 / 2;

      if(buffPos.x < right) {
        if(buffPos.x < 0) {
          buffPos.w += buffPos.x;
          buffPos.x = 0;
        }

        //LAB_80118c58
        if(buffPos.x + buffPos.w > right) {
          buffPos.w = right - buffPos.x;
        }

        //LAB_80118c7c
        if(buffPos.w > 0) {
          if(buffPos.y < bottom) {
            if(buffPos.y < 0) {
              buffPos.y = 0;
            }

            //LAB_80118cc0
            if(buffPos.y + buffPos.h > bottom) {
              buffPos.h = bottom - buffPos.y;
            }

            //LAB_80118ce4
            if(buffPos.h > 0) {
              final int[] data = new int[buffPos.w * buffPos.h];
              final Rect4i rect = new Rect4i(buffPos.x, buffPos.y, buffPos.w, buffPos.h);

              // Back up draw buffer data after background is rendered, but before models are rendered
              GPU.queueCommand(maxZ, new GpuCommand() {
                @Override
                public void render(final Gpu gpu) {
                  gpu.getDrawBuffer().getRegion(rect, data);
                }
              });

              // Overwrite rendered model pixels with the background pixels we backed up to emulate the wipe effect
              GPU.queueCommand(minZ, new GpuCommand() {
                @Override
                public void render(final Gpu gpu) {
                  gpu.getDrawBuffer().setRegion(rect, data);
                }
              });
            }
          }
        }
      }
    }
  }

  /**
   * Used when Shirley transforms into another char. Causes the wipe effect where her model
   * disappears from top to bottom and then reappears as another char from bottom to top.
   */
  @ScriptDescription("Allocates a new wipe effect (used by Shirley when she transforms)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @Method(0x80118df4L)
  public static FlowControl allocateShirleyTransformWipeEffect(final RunningScript<? extends BattleObject> script) {
    final ScriptState<EffectManagerData6c<EffectManagerParams.ShirleyType>> state = allocateEffectManager(
      "Shirley transform wipe effect",
      script.scriptState_04,
      null,
      SEffe::renderShirleyTransformWipeEffect,
      null,
      null,
      new EffectManagerParams.ShirleyType()
    );

    final EffectManagerData6c<EffectManagerParams.ShirleyType> manager = state.innerStruct_00;
    manager.params_10.trans_04.z = 256;
    manager.params_10.width_24 = 0x80;
    manager.params_10.height_28 = 0x80;
    manager.params_10.depth_2c = 0x100;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  /** Some effects use CTMD render pipeline if type == 0x300_0000 */
  @Method(0x80118e98L)
  public static void renderSpriteWithTrailEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.ColourType>> state, final EffectManagerData6c<EffectManagerParams.ColourType> manager) {
    final SpriteWithTrailEffect30 effect = (SpriteWithTrailEffect30)manager.effect_44;
    if(manager.params_10.flags_00 >= 0) { // No errors
      final MV transformMatrix = new MV();
      calculateEffectTransforms(transformMatrix, manager);

      final int type = effect.effectFlag_04 & 0xff00_0000;
      if(type == 0x300_0000) {
        final TmdSpriteEffect10 sprite = (TmdSpriteEffect10)effect.subEffect_1c;

        //LAB_80118f38
        if((manager.params_10.flags_00 & 0x4000_0000) != 0) {
          tmdGp0Tpage_1f8003ec = manager.params_10.flags_00 >>> 23 & 0x60;
        } else {
          //LAB_80118f5c
          tmdGp0Tpage_1f8003ec = sprite.tpage_10;
        }

        //LAB_80118f68
        zOffset_1f8003e8 = manager.params_10.z_22;

        if((manager.params_10.flags_00 & 0x40) == 0) {
          FUN_800e61e4(manager.params_10.colour_1c.x / 128.0f, manager.params_10.colour_1c.y / 128.0f, manager.params_10.colour_1c.z / 128.0f);
        }

        //LAB_80118f9c
        renderTmdSpriteEffect(sprite.tmd_08, sprite.obj, manager.params_10, transformMatrix);
      } else if(type == 0x400_0000) {
        final BillboardSpriteEffect0c sprite = (BillboardSpriteEffect0c)effect.subEffect_1c;
        renderBillboardSpriteEffect(sprite.metrics_04, manager.params_10, transformMatrix);
      }

      //LAB_80118fac
      if(effect.countCopies_08 != 0) {
        final EffectManagerParams.ColourType managerInner = new EffectManagerParams.ColourType();
        final Vector3i colour = new Vector3i();
        final Vector3f stepScale = new Vector3f();

        //LAB_80118fc4
        managerInner.set(manager.params_10);

        final int combinedSteps = effect.countCopies_08 * (effect.countTransformSteps_0c + 1);
        if((effect.colourAndScaleFlags_00 & 0x4) != 0) {
          final int brightness = effect.colourAndScaleTransformModifier_10 - 0x1000;
          managerInner.r_28 = managerInner.colour_1c.x << 12;
          managerInner.g_2c = managerInner.colour_1c.y << 12;
          managerInner.b_30 = managerInner.colour_1c.z << 12;
          colour.x = managerInner.colour_1c.x * brightness / combinedSteps;
          colour.y = managerInner.colour_1c.y * brightness / combinedSteps;
          colour.z = managerInner.colour_1c.z * brightness / combinedSteps;
        }

        //LAB_801190a8
        if((effect.colourAndScaleFlags_00 & 0x8) != 0) {
          final float scaleModifier = (effect.colourAndScaleTransformModifier_10 - 0x1000) / (float)0x1000;
          stepScale.x = managerInner.scale_16.x * scaleModifier / combinedSteps;
          stepScale.y = managerInner.scale_16.y * scaleModifier / combinedSteps;
          stepScale.z = managerInner.scale_16.z * scaleModifier / combinedSteps;
        }

        //LAB_80119130
        //LAB_8011914c
        for(int i = 1; i < effect.countCopies_08 && i < effect.translationIndexBase_14; i++) {
          final Vector3f instTranslation = effect.instanceTranslations_18[(effect.translationIndexBase_14 - i - 1) % effect.countCopies_08];

          final int steps = effect.countTransformSteps_0c + 1;
          final float stepX = (instTranslation.x - transformMatrix.transfer.x) / steps;
          final float stepY = (instTranslation.y - transformMatrix.transfer.y) / steps;
          final float stepZ = (instTranslation.z - transformMatrix.transfer.z) / steps;

          float x = transformMatrix.transfer.x;
          float y = transformMatrix.transfer.y;
          float z = transformMatrix.transfer.z;

          //LAB_80119204
          for(int j = effect.countTransformSteps_0c; j >= 0; j--) {
            if((effect.colourAndScaleFlags_00 & 0x4) != 0) {
              managerInner.r_28 += colour.x;
              managerInner.g_2c += colour.y;
              managerInner.b_30 += colour.z;
              //LAB_80119254
              //LAB_80119270
              //LAB_8011928c
              managerInner.colour_1c.x = managerInner.r_28 >> 12;
              managerInner.colour_1c.y = managerInner.g_2c >> 12;
              managerInner.colour_1c.z = managerInner.b_30 >> 12;
            }

            //LAB_80119294
            if((effect.colourAndScaleFlags_00 & 0x8) != 0) {
              //LAB_801192e4
              //LAB_80119300
              //LAB_8011931c
              managerInner.scale_16.add(stepScale);
            }

            //LAB_80119324
            x += stepX;
            y += stepY;
            z += stepZ;

            //LAB_80119348
            //LAB_80119360
            //LAB_80119378
            transformMatrix.transfer.set(x, y, z);
            transformMatrix.scaleLocal(managerInner.scale_16);

            if(type == 0x300_0000) {
              //LAB_801193f0
              final TmdSpriteEffect10 subEffect = (TmdSpriteEffect10)effect.subEffect_1c;
              renderTmdSpriteEffect(subEffect.tmd_08, subEffect.obj, managerInner, transformMatrix);
            } else if(type == 0x400_0000) {
              final BillboardSpriteEffect0c subEffect = (BillboardSpriteEffect0c)effect.subEffect_1c;
              renderBillboardSpriteEffect(subEffect.metrics_04, managerInner, transformMatrix);
            }
            //LAB_80119400
            //LAB_80119404
          }
          //LAB_8011940c
        }

        //LAB_80119420
        if(type == 0x300_0000 && (manager.params_10.flags_00 & 0x40) == 0) {
          FUN_800e62a8();
        }
      }
    }
    //LAB_80119454
  }

  /**
   * "eco " effect
   * Used for effect sprite overlays on red glow in Death Dimension, for one. Seems to be for
   * generic sprite animations where multiple instances create a "trail" of effect copies.
   */
  @ScriptDescription("Allocates a sprite with a trail effect")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "effectIndex", description = "The new effect manager script index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "flags", description = "Effect flags are unknown")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "colourAndScaleFlags", description = "0x4 - apply colour, 0x8 - apply scale")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "count", description = "The number of copies")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "steps", description = "The number of steps to apply to each copy")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "brightnessAndScaleChange", description = "The amount to change per step")
  @Method(0x80119484L)
  public static FlowControl allocateSpriteWithTrailEffect(final RunningScript<? extends BattleObject> script) {
    final int effectFlag = script.params_20[1].get();
    final int effectType = effectFlag & 0xff00_0000;

    final ScriptState<EffectManagerData6c<EffectManagerParams.ColourType>> state = allocateEffectManager(
      "SpriteWithTrailEffect30",
      script.scriptState_04,
      SEffe::tickSpriteWithTrailEffect,
      SEffe::renderSpriteWithTrailEffect,
      (s, manager) -> {
        if(((SpriteWithTrailEffect30)manager.effect_44).subEffect_1c instanceof final TmdSpriteEffect10 effect) {
          effect.obj.delete();
        }
      },
      new SpriteWithTrailEffect30(script.params_20[3].get()),
      new EffectManagerParams.ColourType()
    );

    final EffectManagerData6c<EffectManagerParams.ColourType> manager = state.innerStruct_00;

    final SpriteWithTrailEffect30 effect = (SpriteWithTrailEffect30)manager.effect_44;
    effect.colourAndScaleFlags_00 = script.params_20[2].get();
    effect.effectFlag_04 = effectFlag;
    effect.countTransformSteps_0c = script.params_20[4].get();
    effect.colourAndScaleTransformModifier_10 = script.params_20[5].get();
    effect.translationIndexBase_14 = 0;

    //LAB_8011956c
    if(effectType == 0x400_0000) {
      //LAB_80119640
      effect.subEffect_1c = new BillboardSpriteEffect0c();
      ((BillboardSpriteEffect0c)effect.subEffect_1c).set(effectFlag);
      manager.params_10.flags_00 = manager.params_10.flags_00 & 0xfbff_ffff | 0x5000_0000;
    } else if(effectType == 0x300_0000) {
      //LAB_80119668
      final TmdSpriteEffect10 subEffect = new TmdSpriteEffect10();
      effect.subEffect_1c = subEffect;
      getSpriteTmdFromSource(subEffect, effectFlag);
      subEffect.obj = TmdObjLoader.fromObjTable(state.name, subEffect.tmd_08);
      manager.params_10.flags_00 = 0x1400_0000;
    } else if(effectType == 0) {
      //LAB_801195a8
      throw new RuntimeException("The type is " + ((EffectManagerData6c<?>)scriptStatePtrArr_800bc1c0[effectFlag].innerStruct_00).effect_44.getClass().getSimpleName());
    }

    //LAB_8011967c
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x801196bcL)
  public static void tickSpriteWithTrailEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.ColourType>> state, final EffectManagerData6c<EffectManagerParams.ColourType> manager) {
    final SpriteWithTrailEffect30 effect = (SpriteWithTrailEffect30)manager.effect_44;

    if(effect.countCopies_08 != 0) {
      final MV transformMatrix = new MV();
      calculateEffectTransforms(transformMatrix, manager);
      effect.instanceTranslations_18[effect.translationIndexBase_14 % effect.countCopies_08].set(transformMatrix.transfer);
      effect.translationIndexBase_14++;
    }
    //LAB_80119778
  }
}
