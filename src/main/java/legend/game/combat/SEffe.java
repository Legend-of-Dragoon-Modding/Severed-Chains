package legend.game.combat;

import legend.core.Config;
import legend.core.MathHelper;
import legend.core.gpu.Bpp;
import legend.core.gpu.Gpu;
import legend.core.gpu.GpuCommand;
import legend.core.gpu.GpuCommandCopyDisplayBufferToVram;
import legend.core.gpu.GpuCommandLine;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gpu.GpuCommandQuad;
import legend.core.gpu.GpuCommandSetMaskBit;
import legend.core.gpu.RECT;
import legend.core.gpu.Rect4i;
import legend.core.gte.COLOUR;
import legend.core.gte.DVECTOR;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.Tmd;
import legend.core.gte.TmdObjTable1c;
import legend.core.gte.TmdWithId;
import legend.core.gte.USCOLOUR;
import legend.core.gte.VECTOR;
import legend.core.memory.Method;
import legend.core.memory.Ref;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.QuadConsumer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.TriConsumer;
import legend.core.memory.types.UnsignedByteRef;
import legend.game.combat.bobj.BattleObject27c;
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
import legend.game.combat.effects.BillboardSpriteEffect0c;
import legend.game.combat.effects.BttlScriptData6cSub13c;
import legend.game.combat.effects.BttlScriptData6cSub1c_3;
import legend.game.combat.effects.BttlScriptData6cSub5c;
import legend.game.combat.effects.DeffTmdRenderer14;
import legend.game.combat.effects.Effect;
import legend.game.combat.effects.EffectManagerData6c;
import legend.game.combat.effects.EffectManagerData6cInner;
import legend.game.combat.effects.ElectricityEffect38;
import legend.game.combat.effects.FrozenJetEffect28;
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
import legend.game.combat.effects.MoonlightStarsEffect18;
import legend.game.combat.effects.MoonlightStarsEffectInstance3c;
import legend.game.combat.effects.ParticleEffectData98;
import legend.game.combat.effects.ParticleEffectData98Inner24;
import legend.game.combat.effects.ParticleEffectInstance94;
import legend.game.combat.effects.ParticleEffectInstance94Sub10;
import legend.game.combat.effects.ParticleInnerStuff04;
import legend.game.combat.effects.ParticleMetrics48;
import legend.game.combat.effects.RainEffect08;
import legend.game.combat.effects.RaindropEffect0c;
import legend.game.combat.effects.ScreenCaptureEffect1c;
import legend.game.combat.effects.ScreenCaptureEffectMetrics8;
import legend.game.combat.effects.ScreenDistortionEffectData08;
import legend.game.combat.effects.SomeParticleStruct10;
import legend.game.combat.effects.SpriteMetrics08;
import legend.game.combat.effects.SpriteWithTrailEffect30;
import legend.game.combat.effects.StarChildrenImpactEffect20;
import legend.game.combat.effects.StarChildrenImpactEffectInstancea8;
import legend.game.combat.effects.StarChildrenMeteorEffect10;
import legend.game.combat.effects.StarChildrenMeteorEffectInstance10;
import legend.game.combat.effects.ThunderArrowEffect1c;
import legend.game.combat.effects.ThunderArrowEffectBolt1e;
import legend.game.combat.effects.TmdSpriteEffect10;
import legend.game.combat.effects.TransformScalerEffect34;
import legend.game.combat.effects.WsDragoonTransformationFeatherInstance70;
import legend.game.combat.effects.WsDragoonTransformationFeathersEffect14;
import legend.game.combat.types.BattleScriptDataBase;
import legend.game.combat.types.DragoonAdditionScriptData1c;
import legend.game.combat.types.EffeScriptData30;
import legend.game.combat.types.EffeScriptData30Sub06;
import legend.game.combat.types.VertexDifferenceAnimation18;
import legend.game.combat.ui.AdditionOverlayMode;
import legend.game.modding.coremod.CoreMod;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptFile;
import legend.game.scripting.ScriptState;
import legend.game.tmd.Renderer;
import legend.game.types.CContainer;
import legend.game.types.Model124;
import legend.game.types.Translucency;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.MEMORY;
import static legend.core.GameEngine.SCRIPTS;
import static legend.game.Scus94491BpeSegment.battlePreloadedEntities_1f8003f4;
import static legend.game.Scus94491BpeSegment.displayHeight_1f8003e4;
import static legend.game.Scus94491BpeSegment.displayWidth_1f8003e0;
import static legend.game.Scus94491BpeSegment.playSound;
import static legend.game.Scus94491BpeSegment.projectionPlaneDistance_1f8003f8;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.renderButtonPressHudElement;
import static legend.game.Scus94491BpeSegment.renderButtonPressHudTexturedRect;
import static legend.game.Scus94491BpeSegment.renderDivineDragoonAdditionPressIris;
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
import static legend.game.Scus94491BpeSegment_8002.renderDobj2;
import static legend.game.Scus94491BpeSegment_8003.GetClut;
import static legend.game.Scus94491BpeSegment_8003.GsGetLw;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_Xyz;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_Yxz;
import static legend.game.Scus94491BpeSegment_8003.RotTransPers4;
import static legend.game.Scus94491BpeSegment_8003.getProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.perspectiveTransform;
import static legend.game.Scus94491BpeSegment_8003.perspectiveTransformTriple;
import static legend.game.Scus94491BpeSegment_8003.setRotTransMatrix;
import static legend.game.Scus94491BpeSegment_8004.RotMatrix_Zyx;
import static legend.game.Scus94491BpeSegment_8004.doNothingScript_8004f650;
import static legend.game.Scus94491BpeSegment_8004.ratan2;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800bf0cf;
import static legend.game.Scus94491BpeSegment_800b.model_800bda10;
import static legend.game.Scus94491BpeSegment_800b.press_800bee94;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.stage_800bda0c;
import static legend.game.Scus94491BpeSegment_800c.identityMatrix_800c3568;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;
import static legend.game.combat.Bttl_800c.FUN_800cf4f4;
import static legend.game.combat.Bttl_800c.FUN_800cf684;
import static legend.game.combat.Bttl_800c.FUN_800cfb94;
import static legend.game.combat.Bttl_800c.FUN_800cfc20;
import static legend.game.combat.Bttl_800c.callScriptFunction;
import static legend.game.combat.Bttl_800c.colourMapUvs_800fb0ec;
import static legend.game.combat.Bttl_800c.currentStage_800c66a4;
import static legend.game.combat.Bttl_800c.deffManager_800c693c;
import static legend.game.combat.Bttl_800c.getModelObjectTranslation;
import static legend.game.combat.Bttl_800c.melbuStageIndices_800fb064;
import static legend.game.combat.Bttl_800c.rotateAndTranslateEffect;
import static legend.game.combat.Bttl_800c.scriptGetScriptedObjectPos;
import static legend.game.combat.Bttl_800c.seed_800fa754;
import static legend.game.combat.Bttl_800c.spriteMetrics_800c6948;
import static legend.game.combat.Bttl_800c.tmds_800c6944;
import static legend.game.combat.Bttl_800d.FUN_800dc408;
import static legend.game.combat.Bttl_800d.getRotationAndScaleFromTransforms;
import static legend.game.combat.Bttl_800d.getRotationFromTransforms;
import static legend.game.combat.Bttl_800d.loadModelAnim;
import static legend.game.combat.Bttl_800d.optimisePacketsIfNecessary;
import static legend.game.combat.Bttl_800d.renderTmdSpriteEffect;
import static legend.game.combat.Bttl_800e.FUN_800e60e0;
import static legend.game.combat.Bttl_800e.FUN_800e6170;
import static legend.game.combat.Bttl_800e.FUN_800e61e4;
import static legend.game.combat.Bttl_800e.FUN_800e62a8;
import static legend.game.combat.Bttl_800e.FUN_800e7dbc;
import static legend.game.combat.Bttl_800e.FUN_800e8594;
import static legend.game.combat.Bttl_800e.FUN_800e8c84;
import static legend.game.combat.Bttl_800e.FUN_800e8d04;
import static legend.game.combat.Bttl_800e.FUN_800e8dd4;
import static legend.game.combat.Bttl_800e.FUN_800e9178;
import static legend.game.combat.Bttl_800e.allocateEffectManager;
import static legend.game.combat.Bttl_800e.applyScreenDarkening;
import static legend.game.combat.Bttl_800e.getDeffPart;
import static legend.game.combat.Bttl_800e.getSpriteMetricsFromSource;
import static legend.game.combat.Bttl_800e.perspectiveTransformXyz;
import static legend.game.combat.Bttl_800e.renderBillboardSpriteEffect_;
import static legend.game.combat.Bttl_800e.renderGenericSpriteAtZOffset0;

public final class SEffe {
  private SEffe() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(SEffe.class);

  private static final Value _800fb794 = MEMORY.ref(2, 0x800fb794L);

  private static final Value _800fb7bc = MEMORY.ref(1, 0x800fb7bcL);

  /** Some kind of mysterious global 2-hit addition array */
  private static final Value _800fb7c0 = MEMORY.ref(1, 0x800fb7c0L);

  /** Four sets of color values used for addition overlay borders; only last actually used */
  public static final ArrayRef<UnsignedByteRef> additionBorderColours_800fb7f0 = MEMORY.ref(1, 0x800fb7f0L, ArrayRef.of(UnsignedByteRef.class, 0xc, 0x1, UnsignedByteRef::new));

  private static final Value _800fb7fc = MEMORY.ref(1, 0x800fb7fcL);

  private static final Value _800fb804 = MEMORY.ref(1, 0x800fb804L);

  private static final Value _800fb818 = MEMORY.ref(1, 0x800fb818L);

  private static final Value _800fb82c = MEMORY.ref(1, 0x800fb82cL);

  private static final Value _800fb840 = MEMORY.ref(1, 0x800fb840L);

  private static final Value _800fb84c = MEMORY.ref(1, 0x800fb84cL);

  private static final COLOUR _800fb8cc = MEMORY.ref(2, 0x800fb8ccL, COLOUR::new);
  private static final SVECTOR _800fb8d0 = MEMORY.ref(2, 0x800fb8d0L, SVECTOR::new);

  private static final Value _800fb8fc = MEMORY.ref(4, 0x800fb8fcL);
  private static final ArrayRef<ArrayRef<IntRef>> _800fb910 = MEMORY.ref(4, 0x800fb910L, ArrayRef.of(ArrayRef.classFor(IntRef.class), 4, 8, ArrayRef.of(IntRef.class, 2, 4, IntRef::new)));
  private static final ArrayRef<ArrayRef<ByteRef>> _800fb930 = MEMORY.ref(1, 0x800fb930L, ArrayRef.of(ArrayRef.classFor(ByteRef.class), 4, 4, ArrayRef.of(ByteRef.class, 4, 1, ByteRef::new)));

  private static final USCOLOUR _800fb94c = MEMORY.ref(2, 0x800fb94cL, USCOLOUR::new);

  /**
   * <ol start="0">
   *   <li>{@link SEffe#renderLineParticles}</li>
   *   <li>null</li>
   *   <li>{@link SEffe#FUN_800fcf18}</li>
   * </ol>
   */
  private static final BiConsumer<EffectManagerData6c, ParticleMetrics48>[] lineParticleRenderers_801197c0 = new BiConsumer[3];
  static {
    lineParticleRenderers_801197c0[0] = SEffe::renderLineParticles;
    lineParticleRenderers_801197c0[1] = null;
    lineParticleRenderers_801197c0[2] = SEffe::FUN_800fcf18; // no-op
  }

  private static final ArrayRef<ParticleInnerStuff04> particleInnerStuffDefaultsArray_801197ec = MEMORY.ref(4, 0x801197ecL, ArrayRef.of(ParticleInnerStuff04.class, 65, 0x4, ParticleInnerStuff04::new));

  private static final ArrayRef<SomeParticleStruct10> _801198f0 = MEMORY.ref(4, 0x801198f0L, ArrayRef.of(SomeParticleStruct10.class, 65, 0xa, SomeParticleStruct10::new));

  /**
   * Particle effect renderers
   * <ol start="0">
   *   <li>{@link SEffe#renderQuadParticleEffect}</li>
   *   <li>{@link SEffe#renderTmdParticleEffect}</li>
   *   <li>{@link SEffe#renderLineParticleEffect}</li>
   *   <li>{@link SEffe#renderPixelParticleEffect}</li>
   *   <li>{@link SEffe#renderLineParticleEffect}</li>
   *   <li>{@link SEffe#renderNoParticlesWhatsoever}</li>
   * </ol>
   */
  private static final BiConsumer<ScriptState<EffectManagerData6c>, EffectManagerData6c>[] particleEffectRenderers_80119b7c = new BiConsumer[6];
  static {
    particleEffectRenderers_80119b7c[0] = SEffe::renderQuadParticleEffect;
    particleEffectRenderers_80119b7c[1] = SEffe::renderTmdParticleEffect;
    particleEffectRenderers_80119b7c[2] = SEffe::renderLineParticleEffect;
    particleEffectRenderers_80119b7c[3] = SEffe::renderPixelParticleEffect;
    particleEffectRenderers_80119b7c[4] = SEffe::renderLineParticleEffect;
    particleEffectRenderers_80119b7c[5] = SEffe::renderNoParticlesWhatsoever; // no-op
  }

  /**
   * <ol start="0">
   *   <li>{@link SEffe#FUN_80101e84}</li>
   *   <li>{@link SEffe#FUN_80101d3c}</li>
   *   <li>{@link SEffe#FUN_80101c74}</li>
   *   <li>{@link SEffe#FUN_80101c68}</li>
   *   <li>{@link SEffe#FUN_80101c74}</li>
   *   <li>{@link SEffe#FUN_80101c74}</li>
   * </ol>
   */
  private static final QuadConsumer<ParticleEffectData98, ParticleEffectInstance94, ParticleEffectData98Inner24, Integer>[] subParticleInitializers_80119b94 = new QuadConsumer[6];
  static {
    subParticleInitializers_80119b94[0] = SEffe::FUN_80101e84;
    subParticleInitializers_80119b94[1] = SEffe::FUN_80101d3c;
    subParticleInitializers_80119b94[2] = SEffe::FUN_80101c74;
    subParticleInitializers_80119b94[3] = SEffe::FUN_80101c68;
    subParticleInitializers_80119b94[4] = SEffe::FUN_80101c74;
    subParticleInitializers_80119b94[5] = SEffe::FUN_80101c74;
  }

  private static final TriConsumer<EffectManagerData6c, ParticleEffectData98, ParticleEffectInstance94>[] prerenderCallbacks_80119bac = new TriConsumer[65];

  static {
    prerenderCallbacks_80119bac[0] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[1] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[2] = SEffe::FUN_800fb9c8;
    prerenderCallbacks_80119bac[3] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[4] = SEffe::FUN_800fb9ec;
    prerenderCallbacks_80119bac[5] = SEffe::FUN_800fba58;
    prerenderCallbacks_80119bac[6] = SEffe::FUN_800fbb14;
    prerenderCallbacks_80119bac[7] = SEffe::FUN_800fbb14;
    prerenderCallbacks_80119bac[8] = SEffe::FUN_800fbbe0;
    prerenderCallbacks_80119bac[9] = SEffe::FUN_800fbbe0;
    prerenderCallbacks_80119bac[10] = SEffe::FUN_800fbd04;
    prerenderCallbacks_80119bac[11] = SEffe::FUN_800fbd04;
    prerenderCallbacks_80119bac[12] = SEffe::FUN_800fba58;
    prerenderCallbacks_80119bac[13] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[14] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[15] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[16] = SEffe::FUN_800fbd68;
    prerenderCallbacks_80119bac[17] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[18] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[19] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[20] = SEffe::FUN_800fbe94;
    prerenderCallbacks_80119bac[21] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[22] = SEffe::FUN_800fbf50;
    prerenderCallbacks_80119bac[23] = SEffe::FUN_800fbfd0;
    prerenderCallbacks_80119bac[24] = SEffe::FUN_800fc068;
    prerenderCallbacks_80119bac[25] = SEffe::FUN_800fc0d0;
    prerenderCallbacks_80119bac[26] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[27] = SEffe::FUN_800fc1fc;
    prerenderCallbacks_80119bac[28] = SEffe::FUN_800fc068;
    prerenderCallbacks_80119bac[29] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[30] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[31] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[32] = SEffe::FUN_800fc280;
    prerenderCallbacks_80119bac[33] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[34] = SEffe::FUN_800fc348;
    prerenderCallbacks_80119bac[35] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[36] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[37] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[38] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[39] = SEffe::FUN_800fc410;
    prerenderCallbacks_80119bac[40] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[41] = SEffe::FUN_800fc42c;
    prerenderCallbacks_80119bac[42] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[43] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[44] = SEffe::FUN_800fc410;
    prerenderCallbacks_80119bac[45] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[46] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[47] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[48] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[49] = SEffe::FUN_800fc528;
    prerenderCallbacks_80119bac[50] = SEffe::FUN_800fc5a8;
    prerenderCallbacks_80119bac[51] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[52] = SEffe::FUN_800fc61c;
    prerenderCallbacks_80119bac[53] = SEffe::FUN_800fc6bc;
    prerenderCallbacks_80119bac[54] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[55] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[56] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[57] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[58] = SEffe::FUN_800fc768;
    prerenderCallbacks_80119bac[59] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[60] = SEffe::FUN_800fc7c8;
    prerenderCallbacks_80119bac[61] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[62] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[63] = SEffe::FUN_800fb9c0; // no-op
    prerenderCallbacks_80119bac[64] = SEffe::FUN_800fb9c0; // no-op
  }
  private static final QuadConsumer<ScriptState<EffectManagerData6c>, EffectManagerData6c, ParticleEffectData98, ParticleEffectInstance94>[] _80119cb0 = new QuadConsumer[65];
  static {
    _80119cb0[0] = SEffe::FUN_80100d58; // no-op
    _80119cb0[1] = SEffe::FUN_80100d58; // no-op
    _80119cb0[2] = SEffe::FUN_80100d60;
    _80119cb0[3] = SEffe::FUN_80100d58; // no-op
    _80119cb0[4] = SEffe::FUN_80100d58; // no-op
    _80119cb0[5] = SEffe::FUN_80100d58; // no-op
    _80119cb0[6] = SEffe::FUN_80100d58; // no-op
    _80119cb0[7] = SEffe::FUN_80100d58; // no-op
    _80119cb0[8] = SEffe::FUN_80100d58; // no-op
    _80119cb0[9] = SEffe::FUN_80100d58; // no-op
    _80119cb0[10] = SEffe::FUN_80100d58; // no-op
    _80119cb0[11] = SEffe::FUN_80100d58; // no-op
    _80119cb0[12] = SEffe::FUN_80100d58; // no-op
    _80119cb0[13] = SEffe::FUN_80100d58; // no-op
    _80119cb0[14] = SEffe::FUN_80100d58; // no-op
    _80119cb0[15] = SEffe::FUN_80100d58; // no-op
    _80119cb0[16] = SEffe::FUN_80100e28;
    _80119cb0[17] = SEffe::FUN_80100d58; // no-op
    _80119cb0[18] = SEffe::FUN_80100d58; // no-op
    _80119cb0[19] = SEffe::FUN_80100e28;
    _80119cb0[20] = SEffe::FUN_80100e28;
    _80119cb0[21] = SEffe::FUN_80100d58; // no-op
    _80119cb0[22] = SEffe::FUN_80100d58; // no-op
    _80119cb0[23] = SEffe::FUN_80100e4c;
    _80119cb0[24] = SEffe::FUN_80100e28;
    _80119cb0[25] = SEffe::FUN_80100d58; // no-op
    _80119cb0[26] = SEffe::FUN_80100d58; // no-op
    _80119cb0[27] = SEffe::FUN_80100d58; // no-op
    _80119cb0[28] = SEffe::FUN_80100d58; // no-op
    _80119cb0[29] = SEffe::FUN_80100d58; // no-op
    _80119cb0[30] = SEffe::FUN_80100ea0;
    _80119cb0[31] = SEffe::FUN_80100d58; // no-op
    _80119cb0[32] = SEffe::FUN_80100d58; // no-op
    _80119cb0[33] = SEffe::FUN_80100d58; // no-op
    _80119cb0[34] = SEffe::FUN_80100d58; // no-op
    _80119cb0[35] = SEffe::FUN_80100d58; // no-op
    _80119cb0[36] = SEffe::FUN_80100d58; // no-op
    _80119cb0[37] = SEffe::FUN_80100d58; // no-op
    _80119cb0[38] = SEffe::FUN_80100d58; // no-op
    _80119cb0[39] = SEffe::FUN_80100d58; // no-op
    _80119cb0[40] = SEffe::FUN_80100d58; // no-op
    _80119cb0[41] = SEffe::FUN_80100d58; // no-op
    _80119cb0[42] = SEffe::FUN_80100d58; // no-op
    _80119cb0[43] = SEffe::FUN_80100d58; // no-op
    _80119cb0[44] = SEffe::FUN_80100d58; // no-op
    _80119cb0[45] = SEffe::FUN_80100d58; // no-op
    _80119cb0[46] = SEffe::FUN_80100d58; // no-op
    _80119cb0[47] = SEffe::FUN_80100d58; // no-op
    _80119cb0[48] = SEffe::FUN_80100d58; // no-op
    _80119cb0[49] = SEffe::FUN_80100d58; // no-op
    _80119cb0[50] = SEffe::FUN_80100d58; // no-op
    _80119cb0[51] = SEffe::FUN_80100d58; // no-op
    _80119cb0[52] = SEffe::FUN_80100d58; // no-op
    _80119cb0[53] = SEffe::FUN_80100d58; // no-op
    _80119cb0[54] = SEffe::FUN_80100d58; // no-op
    _80119cb0[55] = SEffe::FUN_80100d58; // no-op
    _80119cb0[56] = SEffe::FUN_80100d58; // no-op
    _80119cb0[57] = SEffe::FUN_80100d58; // no-op
    _80119cb0[58] = SEffe::FUN_80100d58; // no-op
    _80119cb0[59] = SEffe::FUN_80100d58; // no-op
    _80119cb0[60] = SEffe::FUN_80100d58; // no-op
    _80119cb0[61] = SEffe::FUN_80100d58; // no-op
    _80119cb0[62] = SEffe::FUN_80100d58; // no-op
    _80119cb0[63] = SEffe::FUN_80100d58; // no-op
    _80119cb0[64] = SEffe::FUN_80100d58; // no-op
  }
  private static final QuadConsumer<EffectManagerData6c, ParticleEffectData98, ParticleEffectInstance94, ParticleEffectData98Inner24>[] initializerCallbacks_80119db4 = new QuadConsumer[65];
  static {
    initializerCallbacks_80119db4[0] = SEffe::FUN_800fea70;
    initializerCallbacks_80119db4[1] = SEffe::FUN_800fec3c;
    initializerCallbacks_80119db4[2] = SEffe::FUN_800feccc;
    initializerCallbacks_80119db4[3] = SEffe::FUN_800fee9c;
    initializerCallbacks_80119db4[4] = SEffe::FUN_800fefe4;
    initializerCallbacks_80119db4[5] = SEffe::FUN_800ff15c;
    initializerCallbacks_80119db4[6] = SEffe::FUN_800ff3e0;
    initializerCallbacks_80119db4[7] = SEffe::FUN_800ff3e0;
    initializerCallbacks_80119db4[8] = SEffe::FUN_800ff430;
    initializerCallbacks_80119db4[9] = SEffe::FUN_800ff590;
    initializerCallbacks_80119db4[10] = SEffe::FUN_800ff5c4;
    initializerCallbacks_80119db4[11] = SEffe::FUN_800ff6d4;
    initializerCallbacks_80119db4[12] = SEffe::FUN_800ff15c;
    initializerCallbacks_80119db4[13] = SEffe::FUN_800fea68; // no-op
    initializerCallbacks_80119db4[14] = SEffe::FUN_800fea68; // no-op
    initializerCallbacks_80119db4[15] = SEffe::FUN_800fea68; // no-op
    initializerCallbacks_80119db4[16] = SEffe::FUN_800ff6fc;
    initializerCallbacks_80119db4[17] = SEffe::FUN_800fea68; // no-op
    initializerCallbacks_80119db4[18] = SEffe::FUN_800fea70;
    initializerCallbacks_80119db4[19] = SEffe::FUN_800fea68; // no-op
    initializerCallbacks_80119db4[20] = SEffe::FUN_800ff788;
    initializerCallbacks_80119db4[21] = SEffe::FUN_800ff890;
    initializerCallbacks_80119db4[22] = SEffe::FUN_800ffa80;
    initializerCallbacks_80119db4[23] = SEffe::FUN_800ffadc;
    initializerCallbacks_80119db4[24] = SEffe::FUN_800ffb80;
    initializerCallbacks_80119db4[25] = SEffe::FUN_800ffbd8;
    initializerCallbacks_80119db4[26] = SEffe::FUN_800fea68; // no-op
    initializerCallbacks_80119db4[27] = SEffe::FUN_800ffb80;
    initializerCallbacks_80119db4[28] = SEffe::FUN_800ffe80;
    initializerCallbacks_80119db4[29] = SEffe::FUN_800fea68; // no-op
    initializerCallbacks_80119db4[30] = SEffe::FUN_800ffefc; // no-op
    initializerCallbacks_80119db4[31] = SEffe::FUN_800fea70;
    initializerCallbacks_80119db4[32] = SEffe::FUN_800fff04;
    initializerCallbacks_80119db4[33] = SEffe::FUN_800fff30;
    initializerCallbacks_80119db4[34] = SEffe::FUN_800fffa0;
    initializerCallbacks_80119db4[35] = SEffe::FUN_801000b8;
    initializerCallbacks_80119db4[36] = SEffe::FUN_800fea68; // no-op
    initializerCallbacks_80119db4[37] = SEffe::FUN_800fea68; // no-op
    initializerCallbacks_80119db4[38] = SEffe::FUN_800fea68; // no-op
    initializerCallbacks_80119db4[39] = SEffe::FUN_801000f8;
    initializerCallbacks_80119db4[40] = SEffe::FUN_800fea68; // no-op
    initializerCallbacks_80119db4[41] = SEffe::FUN_80100150;
    initializerCallbacks_80119db4[42] = SEffe::FUN_8010025c;
    initializerCallbacks_80119db4[43] = SEffe::FUN_8010025c;
    initializerCallbacks_80119db4[44] = SEffe::FUN_80100364;
    initializerCallbacks_80119db4[45] = SEffe::FUN_800fea68; // no-op
    initializerCallbacks_80119db4[46] = SEffe::FUN_801003e8;
    initializerCallbacks_80119db4[47] = SEffe::FUN_801005b8;
    initializerCallbacks_80119db4[48] = SEffe::FUN_801007b4;
    initializerCallbacks_80119db4[49] = SEffe::FUN_80100800;
    initializerCallbacks_80119db4[50] = SEffe::FUN_80100878;
    initializerCallbacks_80119db4[51] = SEffe::FUN_800fea68; // no-op
    initializerCallbacks_80119db4[52] = SEffe::FUN_801008f8;
    initializerCallbacks_80119db4[53] = SEffe::FUN_80100978;
    initializerCallbacks_80119db4[54] = SEffe::FUN_80100af4;
    initializerCallbacks_80119db4[55] = SEffe::FUN_80100bb4;
    initializerCallbacks_80119db4[56] = SEffe::FUN_800fea68; // no-op
    initializerCallbacks_80119db4[57] = SEffe::FUN_800fea68; // no-op
    initializerCallbacks_80119db4[58] = SEffe::FUN_80100c18;
    initializerCallbacks_80119db4[59] = SEffe::FUN_80100cac;
    initializerCallbacks_80119db4[60] = SEffe::FUN_80100cec;
    initializerCallbacks_80119db4[61] = SEffe::FUN_800fea68; // no-op
    initializerCallbacks_80119db4[62] = SEffe::FUN_800fea70;
    initializerCallbacks_80119db4[63] = SEffe::FUN_800fea68; // no-op
    initializerCallbacks_80119db4[64] = SEffe::FUN_80100d00;
  }

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
  private static final QuadConsumer<EffectManagerData6c, ElectricityEffect38, LightningBoltEffect14, Integer>[] electricityEffectCallbacks_80119ee8 = new QuadConsumer[11];
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
  private static final BiConsumer<ScriptState<EffectManagerData6c>, EffectManagerData6c>[] electricityEffectRenderers_80119f14 = new BiConsumer[11];
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
  private static final BiConsumer<ScriptState<EffectManagerData6c>, EffectManagerData6c>[] screenDistortionEffectRenderers_80119fd4 = new BiConsumer[3];
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
  private static final BiConsumer<ScriptState<EffectManagerData6c>, EffectManagerData6c>[] screenDistortionEffectTickers_80119fe0 = new BiConsumer[3];
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
  private static final TriConsumer<EffectManagerData6c, ScreenCaptureEffect1c, MATRIX>[] screenCaptureRenderers_80119fec = new TriConsumer[2];
  static {
    screenCaptureRenderers_80119fec[0] = SEffe::FUN_8010b594;
    screenCaptureRenderers_80119fec[1] = SEffe::renderScreenCapture;
  }

  private static final Value _80119f40 = MEMORY.ref(1, 0x80119f40L);
  private static byte additionOverlayActive_80119f41;
  /** Active when spinning */
  private static byte daddyOverlayActive_80119f42;

  /**
   * Struct or something, 0x1c bytes
   */
  private static final Value _80119f44 = MEMORY.ref(1, 0x80119f44L);
  /**
   * Struct or something, 0x1c bytes
   */
  private static final Value _80119f60 = MEMORY.ref(1, 0x80119f60L);
  /**
   * Struct or something, 0x1c bytes
   */
  private static final Value _80119f7c = MEMORY.ref(1, 0x80119f7cL);
  /**
   * Struct or something, 0x1c bytes
   */
  private static final Value _80119f98 = MEMORY.ref(1, 0x80119f98L);

  private static final Value _80119fb4 = MEMORY.ref(1, 0x80119fb4L);

  private static final ArrayRef<UnsignedByteRef> _80119fbc = MEMORY.ref(1, 0x80119fbcL, ArrayRef.of(UnsignedByteRef.class, 8, 1, UnsignedByteRef::new));
  private static final ArrayRef<UnsignedByteRef> _80119fc4 = MEMORY.ref(1, 0x80119fc4L, ArrayRef.of(UnsignedByteRef.class, 8, 1, UnsignedByteRef::new));

  /**
   * <ol start="0">
   *   <li>{@link SEffe#initializeWsDragoonTransformationEffect}</li>
   *   <li>{@link SEffe#expandWsDragoonTransformationEffect}</li>
   *   <li>{@link SEffe#spinWsDragoonTransformationEffect}</li>
   *   <li>{@link SEffe#contractWsDragoonTransformationEffect}</li>
   *   <li>{@link SEffe#WsDragoonTransformationCallback4}</li>
   * </ol>
   */
  private static final BiConsumer<EffectManagerData6c, WsDragoonTransformationFeatherInstance70>[] WsDragoonTransformationFeatherCallbacks_80119ff4 = new BiConsumer[5];
  static {
    WsDragoonTransformationFeatherCallbacks_80119ff4[0] = SEffe::initializeWsDragoonTransformationEffect;
    WsDragoonTransformationFeatherCallbacks_80119ff4[1] = SEffe::expandWsDragoonTransformationEffect;
    WsDragoonTransformationFeatherCallbacks_80119ff4[2] = SEffe::spinWsDragoonTransformationEffect;
    WsDragoonTransformationFeatherCallbacks_80119ff4[3] = SEffe::contractWsDragoonTransformationEffect;
    WsDragoonTransformationFeatherCallbacks_80119ff4[4] = SEffe::WsDragoonTransformationCallback4; // no-op
  }

  private static final IntRef currentParticleIndex_8011a008 = MEMORY.ref(4, 0x8011a008L, IntRef::new);
  private static ParticleEffectData98 firstParticle_8011a00c;
  private static ParticleEffectData98 lastParticle_8011a010;
  /** Success values for each addition hit: 0 = not attempted, 1 = success, -1 = too early, -2 = too late, -3 = wrong button */
  private static final byte[] additionHitCompletionState_8011a014 = new byte[8];

  private static final Value _8011a01c = MEMORY.ref(4, 0x8011a01cL);
  private static final Value _8011a020 = MEMORY.ref(4, 0x8011a020L);
  private static final Value _8011a024 = MEMORY.ref(4, 0x8011a024L);
  private static final Value _8011a028 = MEMORY.ref(4, 0x8011a028L);
  private static final Value _8011a02c = MEMORY.ref(4, 0x8011a02cL);

  private static final Value _8011a030 = MEMORY.ref(1, 0x8011a030L);

  private static final Value _8011a048 = MEMORY.ref(1, 0x8011a048L);

  @Method(0x800fb95cL)
  public static void FUN_800fb95c(final ParticleEffectInstance94 particle) {
    particle.particlePosition_50.setX((short)(rcos(particle._14) * particle._16 >> 12));
    particle.particlePosition_50.setZ((short)(rsin(particle._14) * particle._16 >> 12));
  }

  @Method(0x800fb9c0L)
  public static void FUN_800fb9c0(final EffectManagerData6c a0, final ParticleEffectData98 a1, final ParticleEffectInstance94 a2) {
    // no-op
  }

  @Method(0x800fb9c8L)
  public static void FUN_800fb9c8(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.particleVelocity_58.x.sub(particle._16);
    particle.particleVelocity_58.y.add(particle._14);
  }

  @Method(0x800fb9ecL)
  public static void FUN_800fb9ec(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    FUN_800fb95c(particle);

    particle._14 += particle._18;
    particle._16 += particle._1a.getX();
    particle.particleVelocity_58.y.sub((short)2);

    if(particle._1a.getX() >= 8) {
      particle._1a.x.sub((short)8);
    }
  }

  @Method(0x800fba58L)
  public static void FUN_800fba58(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    FUN_800fb95c(particle);

    particle.particlePosition_50.x.add((short)(rcos(particle._1a.getX()) * particle._1a.getY() / 0x1000));
    particle.particlePosition_50.z.add((short)(rsin(particle._1a.getX()) * particle._1a.getY() / 0x1000));
    particle._16 += particle._18;

    if(particle._18 >= 4) {
      particle._18 -= 4;
    }

    //LAB_800fbaf0
    particle._1a.x.add(particle._1a.getZ());
  }

  @Method(0x800fbb14L)
  public static void FUN_800fbb14(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    FUN_800fb95c(particle);

    particle.particlePosition_50.x.add((short)(rcos(particle._1a.getX()) * particle._1a.getY() >> 12));
    particle.particlePosition_50.y.add((short)(rsin(particle._1a.getX()) * particle._1a.getY() >> 12));

    if(particle._16 >= particle._18 * 4) {
      particle._16 -= particle._18;

      if(particle._18 >= 4) {
        particle._18 -= 4;
      }
    }

    //LAB_800fbbbc
    particle._1a.x.add(particle._1a.z.get());
  }

  @Method(0x800fbbe0L)
  public static void FUN_800fbbe0(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    FUN_800fb95c(particle);
    particle._16 += particle._1a.getY();
    particle.particleVelocity_58.y.add((short)20);
    particle.angleVelocity_10 -= particle.angleAcceleration_24;
    if(particle._18 == 1) {
      if(particle.ticksUntilMovementModeChanges_22 != 0) {
        //LAB_800fbca4
        particle.particlePosition_50.setY((short)-particle.managerTranslation_2c.getY());
        particle.ticksUntilMovementModeChanges_22--;
      } else {
        particle.particlePosition_50.setY((short)((rsin(particle._1a.getX()) * particle.verticalPositionScale_20 >> 12) - (short)particle.managerTranslation_2c.getY()));
        particle._1a.x.add((short)0x7f);
        particle.scaleHorizontalStep_0a = particle._1a.getZ();
        particle.scaleVerticalStep_0c = particle._1a.getZ();
        particle._1a.y.add((short)5);
        particle.verticalPositionScale_20 += 20;
      }
      //LAB_800fbcc0
    } else if(particle.managerTranslation_2c.getY() + particle.particlePosition_50.getY() >= -1000) {
      particle._1a.setY((short)0);
      particle._18 = 1;
      particle.scaleHorizontalStep_0a = particle._1a.getZ();
      particle.scaleVerticalStep_0c = particle._1a.getZ();
    }

    //LAB_800fbcf4
  }

  @Method(0x800fbd04L)
  public static void FUN_800fbd04(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    FUN_800fb95c(particle);

    particle._16 += particle._18;
    if(particle._18 >= 3) {
      particle._18 -= 3;
    }

    //LAB_800fbd44
    particle._14 += particle._1a.getX();
  }

  @Method(0x800fbd68L)
  public static void FUN_800fbd68(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    final SVECTOR sp0x38 = new SVECTOR();
    if(particle._18 == 0) {
      FUN_800fb95c(particle);
      particle.particlePosition_50.setY((short)0);
      particle.particlePosition_50.z.shl(1);
      sp0x38.set(particle._1a.getX(), (short)0, (short)0);
    } else {
      //LAB_800fbdb8
      particle.particlePosition_50.setX((short)0);
      particle.particlePosition_50.setY((short)(rsin(particle._14) * particle._16 >> 11));
      particle.particlePosition_50.setZ((short)(rcos(particle._14) * particle._16 >> 12));
      sp0x38.set((short)0, (short)0, particle._1a.getX());
    }

    //LAB_800fbe10
    particle._14 -= 0x80;
    particle._1a.x.sub((short)0x8);

    final VECTOR sp0x18 = new VECTOR().set(particle.particlePosition_50);
    final VECTOR sp0x28 = new VECTOR();
    rotateAndTranslateEffect(manager, sp0x38, sp0x18, sp0x28);
    particle.particlePosition_50.set(sp0x28);
  }

  @Method(0x800fbe94L)
  public static void FUN_800fbe94(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    FUN_800fb95c(particle);
    particle.particlePosition_50.setY((short)0);
    particle.particlePosition_50.z.shl(1);
    final SVECTOR sp0x38 = new SVECTOR().set(particle._18, (short)0, particle._1a.getX());
    particle._14 += 0x80;
    final VECTOR sp0x18 = new VECTOR().set(particle.particlePosition_50);
    final VECTOR sp0x28 = new VECTOR();
    rotateAndTranslateEffect(manager, sp0x38, sp0x18, sp0x28);
    particle.particlePosition_50.set(sp0x28);
  }

  @Method(0x800fbf50L)
  public static void FUN_800fbf50(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    FUN_800fb95c(particle);

    particle._16 = (short)(particle._16 * particle._18 >> 8);

    final int v1 = particle._1a.getY() >> 2;
    if(particle._16 < v1) {
      particle._16 = (short)v1;
    }

    //LAB_800fbfac
    particle._14 += particle._1a.getX();
  }

  @Method(0x800fbfd0L)
  public static void FUN_800fbfd0(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.particlePosition_50.setY((short)(rsin(particle._14) * particle._16 >> 12));
    particle.particlePosition_50.setZ((short)(rcos(particle._14) * particle._16 >> 12));
    particle._16 += particle._18;
    if(particle._16 < 0) {
      particle._16 = 0;
    }

    //LAB_800fc044
    particle._14 += particle._1a.getX();
  }

  @Method(0x800fc068L)
  public static void FUN_800fc068(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    FUN_800fb95c(particle);

    particle._16 += particle._18;

    if(particle.particlePosition_50.getY() + particle.managerTranslation_2c.getY() >= manager._10._30) {
      particle.ticksRemaining_12 = 1;
    }
    //LAB_800fc0bc
  }

  @Method(0x800fc0d0L)
  public static void FUN_800fc0d0(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    if(particle.particlePosition_50.getY() + particle.managerTranslation_2c.getY() >= -400 && particle._14 == 0) {
      particle._14 = 1;
      particle.particleAcceleration_60.setY((short)-8);
      //LAB_800fc11c
    } else if(effect.parentScriptIndex_04 != -1 && particle._14 == 0) {
      final VECTOR sp0x10 = new VECTOR();
      scriptGetScriptedObjectPos(effect.parentScriptIndex_04, sp0x10);
      particle.particleVelocity_58.setX((short)((sp0x10.getX() - (particle.particlePositionCopy1.getX() + particle.managerTranslation_2c.getX())) / particle._1a.getZ()));
      particle.particleVelocity_58.setY((short)((sp0x10.getY() - (particle.particlePositionCopy1.getY() + particle.managerTranslation_2c.getY())) / particle._1a.getZ()));
      particle.particleVelocity_58.setZ((short)((sp0x10.getZ() - (particle.particlePositionCopy1.getZ() + particle.managerTranslation_2c.getZ())) / particle._1a.getZ()));
      particle.particleVelocity_58.x.add(particle._18);
      particle.particleVelocity_58.y.add(particle._1a.getX());
      particle.particleVelocity_58.z.add(particle._1a.getY());
    }
    //LAB_800fc1ec
  }

  @Method(0x800fc1fcL)
  public static void FUN_800fc1fc(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    FUN_800fb95c(particle);

    particle._16 += particle._18;
    if(particle.particlePosition_50.getY() + particle.managerTranslation_2c.getY() >= manager._10._30) {
      particle.particlePosition_50.setY((short)(manager._10._30 - particle.managerTranslation_2c.getY()));
      particle.particleVelocity_58.y.neg().shra(1);
    }
    //LAB_800fc26c
  }

  @Method(0x800fc280L)
  public static void FUN_800fc280(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.particlePosition_50.setZ((short)(rcos(particle._14) * 2 * particle._1a.getY() >> 12));
    particle.particlePosition_50.setX((short)(rsin(particle._1a.getZ()) * particle._1a.getY() >> 12));
    particle._14 += particle._16;
    particle._1a.setZ((short)(particle._1a.getZ() + particle._16 * 2));
    particle.particlePosition_50.y.add((short)(0x40 + (((rcos(particle._18) >> 1) + 0x800) * particle._1a.getY() >> 15)));
    particle._18 += particle._1a.getX();
  }

  @Method(0x800fc348L)
  public static void FUN_800fc348(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.particlePosition_50.setX((short)((rcos(particle._14) * particle._1a.getX() >> 12) * rsin(particle._18) >> 12));
    particle.particlePosition_50.setY((short)(particle._18 * 2 - 0x800));
    particle.particlePosition_50.setZ((short)((rsin(particle._14) * particle._1a.getX() >> 12) * rsin(particle._18) >> 12));
    particle._14 += particle._16;
  }

  @Method(0x800fc410L)
  public static void FUN_800fc410(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.particleVelocity_58.y.add((short)(particle._14 / 0x100));
  }

  @Method(0x800fc42cL)
  public static void FUN_800fc42c(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.particlePosition_50.setX((short)(particle._14 + (rcos(particle._1a.getX()) * particle._1a.getZ() >> 12)));
    particle.particlePosition_50.setZ((short)(particle._18 + (rsin(particle._1a.getX()) * particle._1a.getZ() >> 12)));
    particle._1a.z.add((short)16);
    particle._1a.x.add(particle._1a.getY());
  }

  @Method(0x800fc4bcL)
  public static void FUN_800fc4bc(final MATRIX out, final EffectManagerData6c a1, final ParticleMetrics48 particleMetrics) {
    RotMatrix_Xyz(particleMetrics.rotation_38, out);
    out.transfer.set(particleMetrics.translation_18);
    out.scaleL(particleMetrics.scale_28);
  }

  @Method(0x800fc528L)
  public static void FUN_800fc528(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.particlePosition_50.setX((short)(rsin(particle._14) * (particle._18 >> 1) >> 12));
    particle.particlePosition_50.setZ((short)(rcos(particle._14) * particle._18 >> 12));
    particle._14 += particle._16;
  }

  @Method(0x800fc5a8L)
  public static void FUN_800fc5a8(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.particlePosition_50.setX((short)(rsin(particle._14) * particle._18 >> 12));
    particle.particlePosition_50.setZ((short)(rcos(particle._14) * particle._18 >> 12));
    particle._18 = (short)(particle._18 * 7 / 8);
  }

  @Method(0x800fc61cL)
  public static void FUN_800fc61c(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.scaleVertical_08 = (short)(rcos(particle._14) * particle._16 >> 12);
    particle._14 -= particle._18;
    particle.scaleVertical_08 = (short)(particle.scaleVertical_08 * manager._10.scale_16.getY() >> 12);

    if(particle._16 > 0) {
      particle._16 -= particle._1a.getX();
    }

    //LAB_800fc6a8
  }

  @Method(0x800fc6bcL)
  public static void FUN_800fc6bc(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.particlePosition_50.setX((short)(rsin(particle._14) * particle._16 >> 12));
    particle.particlePosition_50.setZ((short)(rcos(particle._14) * particle._16 >> 12));
    particle.particlePosition_50.x.add((short)(rsin(particle._18) << 8 >> 12));
    particle.particlePosition_50.z.add((short)(rcos(particle._18) << 8 >> 12));
    particle._18 += particle._1a.getX();
  }

  @Method(0x800fc768L)
  public static void FUN_800fc768(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle._1a.x.add(particle._14);
    particle._1a.y.add(particle._16);
    particle._1a.z.add(particle._18);
    particle.particleVelocity_58.setX((short)(particle._1a.getX() >> 8));
    particle.particleVelocity_58.setY((short)(particle._1a.getY() >> 8));
    particle.particleVelocity_58.setZ((short)(particle._1a.getZ() >> 8));
  }

  @Method(0x800fc7c8L)
  public static void FUN_800fc7c8(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    if(particle.particlePosition_50.getY() + particle.managerTranslation_2c.getY() >= manager._10._30) {
      particle.particlePosition_50.setY((short)(manager._10._30 - particle.managerTranslation_2c.getY()));
      particle.particleVelocity_58.y.neg().shra(1);
      if(particle._14 == 0) {
        final int angle = (int)(seed_800fa754.advance().get() % 0x1001);
        particle.particleVelocity_58.setX((short)((rcos(angle) >>> 8) * effect.effectInner_08._18 >> 8));
        particle.particleVelocity_58.setZ((short)((rcos(angle) >>> 8) * effect.effectInner_08._18 >> 8));
      }

      //LAB_800fc8d8
      particle._14 = 1;
    }
    //LAB_800fc8e0
  }

  @Method(0x800fc8f8L)
  public static void FUN_800fc8f8(@Nullable VECTOR in, @Nullable final SVECTOR out) {
    if(in == null) {
      in = new VECTOR();
    }

    //LAB_800fc920
    final MATRIX wsTransposed = new MATRIX().set(worldToScreenMatrix_800c3548).transpose();
    final VECTOR wsNegated = new VECTOR().set(wsTransposed.transfer).negate();
    wsNegated.mul(wsTransposed, in);

    if(out != null) {
      wsNegated.set(wsTransposed.transfer).negate();
      wsNegated.z.add(0x1000);

      final VECTOR sp0x10 = new VECTOR();
      wsNegated.mul(wsTransposed, sp0x10);
      sp0x10.sub(in);
      final short angle = (short)ratan2(sp0x10.getX(), sp0x10.getZ());
      out.setY(angle);

      //LAB_800fca44
      out.setX((short)ratan2(-sp0x10.getY(), (rcos(-angle) * sp0x10.getZ() - rsin(-angle) * sp0x10.getX()) / 0x1000));
      out.setZ((short)0);
    }
    //LAB_800fca5c
  }

  /** Returns Z */
  @Method(0x800fca78L)
  public static int FUN_800fca78(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final VECTOR translation, final GpuCommandPoly cmd) {
    final ShortRef refX = new ShortRef();
    final ShortRef refY = new ShortRef();
    final int z = FUN_800cfc20(particle.managerRotation_68, particle.managerTranslation_2c, translation, refX, refY);
    if(z >= 40) {
      final int zScale = 0x50_0000 / z;
      final int horizontalScale = zScale * (manager._10.scale_16.getX() + particle.scaleHorizontal_06) >> 12;
      final int verticalScale = zScale * (manager._10.scale_16.getY() + particle.scaleVertical_08) >> 12;

      final int angle;
      if((manager._10._24 & 0x2) != 0) {
        final VECTOR sp0x28 = new VECTOR();
        final SVECTOR sp0x38 = new SVECTOR();
        FUN_800fc8f8(sp0x28, sp0x38);

        //LAB_800fcb90
        //LAB_800fcbd4
        final int sp18 = (particle.particlePositionCopy2_48.getZ() - translation.getZ()) * -Math.abs(rsin(sp0x38.getY() - manager._10.rot_10.getY())) / 0x1000 - (translation.getX() - particle.particlePositionCopy2_48.getX()) * -Math.abs(rcos(sp0x38.getY() + manager._10.rot_10.getY())) / 0x1000;
        final int sp1c = translation.getY() - particle.particlePositionCopy2_48.getY();
        angle = -ratan2(sp1c, sp18) + 0x400;
        particle.particlePositionCopy2_48.set(translation);
      } else {
        angle = particle.angle_0e + manager._10.rot_10.getX() - 0xa00;
      }

      //LAB_800fcc20
      final int width = effect.w_5e / 2 * horizontalScale;
      final int height = effect.h_5f / 2 * verticalScale;
      final int left = -width >> 8;
      final int right = width >> 8;
      final int top = -height >> 8;
      final int bottom = height >> 8;
      final int cos = rcos(angle);
      final int sin = rsin(angle);

      // Rotate coords
      final int rotX0 = (short)left * cos >> 12;
      final int rotY0 = (short)left * sin >> 12;
      final int rotX1 = (short)right * cos >> 12;
      final int rotY1 = (short)right * sin >> 12;
      final int rotX2 = (short)top * sin >> 12;
      final int rotY2 = (short)top * cos >> 12;
      final int rotX3 = (short)bottom * sin >> 12;
      final int rotY3 = (short)bottom * cos >> 12;

      final short x = refX.get();
      final short y = refY.get();
      cmd.pos(0, (short)(x + rotX0 - rotX2), (short)(y + rotY0 + rotY2));
      cmd.pos(1, (short)(x + rotX1 - rotX2), (short)(y + rotY1 + rotY2));
      cmd.pos(2, (short)(x + rotX0 - rotX3), (short)(y + rotY0 + rotY3));
      cmd.pos(3, (short)(x + rotX1 - rotX3), (short)(y + rotY1 + rotY3));
    }

    //LAB_800fcde0
    return z;
  }

  @Method(0x800fce10L)
  public static void renderLineParticles(final EffectManagerData6c manager, final ParticleMetrics48 particleMetrics) {
    if(particleMetrics.flags_00 >= 0) {
      GPU.queueCommand(particleMetrics.z_04 + manager._10.z_22 >> 2, new GpuCommandLine()
        .translucent(Translucency.B_PLUS_F)
        .rgb(0, (int)(particleMetrics.colour0_40.x * 0xff), (int)(particleMetrics.colour0_40.y * 0xff), (int)(particleMetrics.colour0_40.z * 0xff))
        .rgb(1, (int)(particleMetrics.colour1_44.x * 0xff), (int)(particleMetrics.colour1_44.y * 0xff), (int)(particleMetrics.colour1_44.z * 0xff))
        .pos(0, particleMetrics.x0_08, particleMetrics.y0_10)
        .pos(1, particleMetrics.x1_0c, particleMetrics.y1_14)
      );
    }
    //LAB_800fcf08
  }

  @Method(0x800fcf18L)
  public static void FUN_800fcf18(final EffectManagerData6c a0, final ParticleMetrics48 a1) {
    // no-op
  }

  /**
   * Used by particle renderer index 1 (renders ice chunk particles and ???). Used renderCtmd
   * Seems to involve lit TMDs.
   */
  @Method(0x800fcf20L)
  public static void renderTmdParticle(final EffectManagerData6c manager, final TmdObjTable1c tmd, final ParticleMetrics48 particleMetrics, final int tpage) {
    if(particleMetrics.flags_00 >= 0) {
      final MATRIX lightMatrix = new MATRIX();
      FUN_800fc4bc(lightMatrix, manager, particleMetrics);
      if((particleMetrics.flags_00 & 0x40) == 0) {
        FUN_800e61e4(particleMetrics.colour0_40.x, particleMetrics.colour0_40.y, particleMetrics.colour0_40.z);
      }

      //LAB_800fcf94
      GsSetLightMatrix(lightMatrix);
      final MATRIX transformMatrix = new MATRIX();
      lightMatrix.compose(worldToScreenMatrix_800c3548, transformMatrix);

      if((particleMetrics.flags_00 & 0x400_0000) == 0) {
        RotMatrix_Xyz(manager._10.rot_10, transformMatrix);
        transformMatrix.scaleL(manager._10.scale_16);
      }

      //LAB_800fcff8
      setRotTransMatrix(transformMatrix);
      zOffset_1f8003e8.set(0);
      if((manager._10.flags_00 & 0x4000_0000) != 0) {
        tmdGp0Tpage_1f8003ec.set(manager._10.flags_00 >>> 23 & 0x60);
      } else {
        //LAB_800fd038
        tmdGp0Tpage_1f8003ec.set(tpage);
      }

      //LAB_800fd040
      final GsDOBJ2 dobj = new GsDOBJ2();
      dobj.attribute_00 = particleMetrics.flags_00;
      dobj.tmd_08 = tmd;

      final int oldZShift = zShift_1f8003c4.get();
      final int oldZMax = zMax_1f8003cc.get();
      final int oldZMin = zMin;
      zShift_1f8003c4.set(2);
      zMax_1f8003cc.set(0xffe);
      zMin = 0xb;
      Renderer.renderDobj2(dobj, false, 0x20);
      zShift_1f8003c4.set(oldZShift);
      zMax_1f8003cc.set(oldZMax);
      zMin = oldZMin;

      if((particleMetrics.flags_00 & 0x40) == 0) {
        FUN_800e62a8();
      }
    }
    //LAB_800fd064
  }

  @Method(0x800fd084L)
  public static void FUN_800fd084(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.managerTranslation_2c.set(manager._10.trans_04);
    particle.managerRotation_68.set(manager._10.rot_10);

    if(particle.ticksRemaining_12 == 0) {
      particle.ticksRemaining_12 = 1;
    }

    if((effect.effectInner_08.particleInnerStuff_1c & 0x400_0000) != 0) {
      particle.r_84 = manager._10.colour_1c.getX() / (float)0xff;
      particle.g_86 = manager._10.colour_1c.getY() / (float)0xff;
      particle.b_88 = manager._10.colour_1c.getZ() / (float)0xff;

      if((manager._10._24 & 0x1) == 0) {
        particle.stepR_8a = 0;
        particle.stepG_8c = 0;
        particle.stepB_8e = 0;
        return;
      }
    }

    //LAB_800fd18c
    //LAB_800fd0dc
    particle.stepR_8a = particle.r_84 / particle.ticksRemaining_12;
    particle.stepG_8c = particle.g_86 / particle.ticksRemaining_12;
    particle.stepB_8e = particle.b_88 / particle.ticksRemaining_12;

    //LAB_800fd1d4
  }

  @Method(0x800fd1dcL)
  public static void tickParticleAttributes(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final Vector3f colour) {
    if((effect.effectInner_08.particleInnerStuff_1c & 0x800_0000) == 0 || (manager._10._24 & 0x1) != 0) {
      //LAB_800fd23c
      particle.r_84 -= particle.stepR_8a;
      particle.g_86 -= particle.stepG_8c;
      particle.b_88 -= particle.stepB_8e;
    } else {
      particle.r_84 = manager._10.colour_1c.getX() / (float)0xff;
      particle.g_86 = manager._10.colour_1c.getY() / (float)0xff;
      particle.b_88 = manager._10.colour_1c.getZ() / (float)0xff;
    }

    //LAB_800fd26c
    colour.x = particle.r_84;
    colour.y = particle.g_86;
    colour.z = particle.b_88;

    particle.particlePosition_50.add(particle.particleVelocity_58);
    particle.particleVelocity_58.add(particle.particleAcceleration_60);

    if(particle.particlePosition_50.getY() + particle.managerTranslation_2c.getY() >= manager._10._30) {
      if((manager._10._24 & 0x20) != 0) {
        particle.ticksRemaining_12 = 1;
      }

      //LAB_800fd324
      if((manager._10._24 & 0x8) != 0) {
        particle.particlePosition_50.setY((short)(manager._10._30 - particle.managerTranslation_2c.getY()));
        particle.particleVelocity_58.setY((short)(-particle.particleVelocity_58.getY() / 2));
      }
    }

    //LAB_800fd358
    if((effect.effectInner_08.particleInnerStuff_1c & 0x200_0000) == 0) {
      particle.scaleHorizontal_06 += particle.scaleHorizontalStep_0a;
      particle.scaleVertical_08 += particle.scaleVerticalStep_0c;
    }

    //LAB_800fd38c
    if((effect.effectInner_08.particleInnerStuff_1c & 0x100_0000) == 0) {
      particle.angle_0e += particle.angleVelocity_10;
      particle.spriteRotation_70.add(particle.spriteRotationStep_78);
    } else {
      //LAB_800fd3e4
      particle.angle_0e = (short)(manager._10._24 >>> 12 & 0xff0);
    }

    //LAB_800fd3f8
    particle.particleVelocity_58.y.add((short)(manager._10._2c >> 8));

    if(effect.scaleOrUseEffectAcceleration_6c) {
      particle.particleVelocity_58.add(effect.effectAcceleration_70).shra(8);
    }
    //LAB_800fd458
  }

  @Method(0x800fd460L)
  public static boolean checkParticleShouldRender(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    particle.framesUntilRender_04--;

    final short framesUntilRender = particle.framesUntilRender_04;

    //LAB_800fd53c
    if(framesUntilRender >= 0)  {
      if(framesUntilRender != 0) {
        return false;
      }
      FUN_800fd084(manager, effect, particle);

      if((manager._10._24 & 0x10) != 0) {
        particle.particlePosition_50.setY((short)0);

        if(effect.subParticleType_60 == 2 || effect.subParticleType_60 == 5) {
          //LAB_800fd4f0
          //LAB_800fd504
          for(int i = 0; i < effect.countParticleSub_54; i++) {
            particle.subParticlePositionsArray_44[i].setY((short)0);
          }
        }
      }

      //LAB_800fd520
      if((manager._10._24 & 0x40) != 0) {
        particle.particleVelocity_58.setY((short)0);
      }
    }

    //LAB_800fd54c
    effect.callback_88.accept(state, manager, effect, particle);

    if((particle.flags_90 & 0x1) == 0) {
      return false;
    }

    if(particle.ticksRemaining_12 > 0) {
      particle.ticksRemaining_12--;
    }

    //LAB_800fd58c
    if(particle.ticksRemaining_12 == 0 && (manager._10._24 & 0x80) == 0) {
      particle.flags_90 &= 0xffff_fffe;
      effect.callback_90.accept(state, manager, effect, particle);
      return false;
    }

    //LAB_800fd5e0
    return true;
  }

  @Method(0x800fd600L)
  public static void renderTmdParticleEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final ParticleEffectData98 effect = (ParticleEffectData98)manager.effect_44;
    effect.countFramesRendered_52++;

    //LAB_800fd660
    for(int i = 0; i < effect.countParticleInstance_50; i++) {
      final ParticleEffectInstance94 particle = effect.particleArray_68[i];
      if(checkParticleShouldRender(state, manager, effect, particle)) {
        effect.prerenderCallback_84.accept(manager, effect, particle);
        final Vector3f colour = new Vector3f();
        tickParticleAttributes(manager, effect, particle, colour);

        final VECTOR rotatedAndTranslatedPosition = new VECTOR();
        rotateAndTranslateEffect(manager, null, new VECTOR().set(particle.particlePosition_50), rotatedAndTranslatedPosition);

        final ParticleMetrics48 particleMetrics = new ParticleMetrics48();
        particleMetrics.flags_00 = manager._10.flags_00;
        particleMetrics.translation_18.set(particle.managerTranslation_2c).add(rotatedAndTranslatedPosition);
        particleMetrics.scale_28.setX(manager._10.scale_16.getX() + particle.scaleHorizontal_06);
        particleMetrics.scale_28.setY(manager._10.scale_16.getY() + particle.scaleVertical_08);
        particleMetrics.scale_28.setZ(manager._10.scale_16.getX() + particle.scaleHorizontal_06); // This is correct
        particleMetrics.rotation_38.set(particle.spriteRotation_70).add(particle.managerRotation_68);
        particleMetrics.colour0_40.set(colour.x, colour.y, colour.z);
        particleMetrics.colour1_44.set(0, 0, 0);
        renderTmdParticle(manager, effect.tmd_30, particleMetrics, effect.tpage_56);
      }
      //LAB_800fd7e0
    }

    //LAB_800fd7fc
    if(effect.scaleOrUseEffectAcceleration_6c) {
      effect.effectAcceleration_70.mul(effect.scaleParticleAcceleration_80).shra(8);
    }
    //LAB_800fd858
  }

  @Method(0x800fd87cL)
  public static void renderLineParticleEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final ParticleEffectData98 effect = (ParticleEffectData98)manager.effect_44;
    effect.countFramesRendered_52++;

    if(effect.countParticleInstance_50 != 0) {
      final ParticleMetrics48 particleMetrics = new ParticleMetrics48();

      //LAB_800fd8dc
      for(int i = 0; i < effect.countParticleInstance_50; i++) {
        final ParticleEffectInstance94 particle = effect.particleArray_68[i];

        if(checkParticleShouldRender(state, manager, effect, particle)) {
          //LAB_800fd918
          for(int j = effect.countParticleSub_54 - 1; j > 0; j--) {
            particle.subParticlePositionsArray_44[j].set(particle.subParticlePositionsArray_44[j - 1]);
          }

          //LAB_800fd950
          particle.subParticlePositionsArray_44[0].set(particle.particlePosition_50);
          effect.prerenderCallback_84.accept(manager, effect, particle);

          final Vector3f colour = new Vector3f();
          tickParticleAttributes(manager, effect, particle, colour);

          final Vector3f colourMod = new Vector3f();
          if((effect.effectInner_08.particleInnerStuff_1c & 0x1000_0000) == 0 || (particle.flags_90 & 0x8) == 0) {
            //LAB_800fd9f4
            colourMod.set(0, 0, 0);
          } else {
            colourMod.set(colour).negate().div(2.0f);
          }

          //LAB_800fda00
          particle.flags_90 = particle.flags_90 & 0xffff_fff7 | (~(particle.flags_90 >>> 3) & 0x1) << 3;

          //LAB_800fda58
          //LAB_800fda90
          MathHelper.clamp(colour.add(colourMod), 0.0f, 0.5f);

          //LAB_800fdac8
          if((particle.flags_90 & 0x6) != 0) {
            particleMetrics.flags_00 = manager._10.flags_00 & 0x67ff_ffff | (particle.flags_90 >>> 1 & 0x3) << 28;
          } else {
            particleMetrics.flags_00 = manager._10.flags_00;
          }

          //LAB_800fdb14
          final Vector3f stepColour = new Vector3f();
          stepColour.set(colour).div(effect.countParticleSub_54);

          final VECTOR subTranslation = new VECTOR().set(particle.subParticlePositionsArray_44[0]);
          final ShortRef refX1 = new ShortRef();
          final ShortRef refY1 = new ShortRef();
          int z = FUN_800cfc20(particle.managerRotation_68, particle.managerTranslation_2c, subTranslation, refX1, refY1) / 4;
          particleMetrics.x0_08 = refX1.get();
          particleMetrics.y0_10 = refY1.get();

          if(z + manager._10.z_22 >= 0xa0) {
            if(z + manager._10.z_22 >= 0xffe) {
              z = 0xffe - manager._10.z_22;
            }

            //LAB_800fdbc0
            particleMetrics.z_04 = z;

            //LAB_800fdbe4
            for(int k = 0; k < effect.countParticleSub_54 - 1; k++) {
              particleMetrics.colour0_40.set(colour.x, colour.y, colour.z);
              colour.sub(stepColour);
              particleMetrics.colour1_44.set(colour.x, colour.y, colour.z);
              subTranslation.set(particle.subParticlePositionsArray_44[k]);

              final ShortRef refX2 = new ShortRef();
              final ShortRef refY2 = new ShortRef();
              FUN_800cfc20(particle.managerRotation_68, particle.managerTranslation_2c, subTranslation, refX2, refY2);
              particleMetrics.x1_0c = refX2.get();
              particleMetrics.y1_14 = refY2.get();
              lineParticleRenderers_801197c0[effect.subParticleType_60 - 2].accept(manager, particleMetrics);
              particleMetrics.x0_08 = particleMetrics.x1_0c;
              particleMetrics.y0_10 = particleMetrics.y1_14;
            }
            //LAB_800fdcc8
          }
        }
        //LAB_800fdd28
      }
    }

    //LAB_800fdd44
    if(effect.scaleOrUseEffectAcceleration_6c) {
      effect.effectAcceleration_70.mul(effect.scaleParticleAcceleration_80).shra(8);
    }
    //LAB_800fdda0
  }

  @Method(0x800fddd0L)
  public static void renderNoParticlesWhatsoever(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    // no-op
  }

  @Method(0x800fddd8L)
  public static void renderPixelParticleEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final ParticleEffectData98 effect = (ParticleEffectData98)manager.effect_44;
    effect.countFramesRendered_52++;

    //LAB_800fde38
    for(int i = 0; i < effect.countParticleInstance_50; i++) {
      final ParticleEffectInstance94 particle = effect.particleArray_68[i];

      if(checkParticleShouldRender(state, manager, effect, particle)) {
        effect.prerenderCallback_84.accept(manager, effect, particle);

        final Vector3f colour = new Vector3f();
        tickParticleAttributes(manager, effect, particle, colour);

        final VECTOR translation = new VECTOR().set(particle.particlePosition_50);
        final ShortRef refX = new ShortRef();
        final ShortRef refY = new ShortRef();

        int z = FUN_800cfc20(particle.managerRotation_68, particle.managerTranslation_2c, translation, refX, refY) >> 2;
        final int zCombined = z + manager._10.z_22;
        if(zCombined >= 0xa0) {
          if(zCombined >= 0xffe) {
            z = 0xffe - manager._10.z_22;
          }

          //LAB_800fdf44
          // gp0 command 68h, which is an opaque dot (1x1)
          GPU.queueCommand(z + manager._10.z_22 >> 2, new GpuCommandQuad()
            .rgb((int)(colour.x * 0xff), (int)(colour.y * 0xff), (int)(colour.z * 0xff))
            .pos(refX.get(), refY.get(), 1, 1)
          );
        }
      }
      //LAB_800fdfcc
    }

    //LAB_800fdfe8
    if(effect.scaleOrUseEffectAcceleration_6c) {
      effect.effectAcceleration_70.mul(effect.scaleParticleAcceleration_80).shra(8);
    }
    //LAB_800fe044
  }

  /** Has some kind of sub-particles */
  @Method(0x800fe120L)
  public static void renderQuadParticleEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final ParticleEffectData98 effect = (ParticleEffectData98)manager.effect_44;

    effect.countFramesRendered_52++;

    final Vector3f colour = new Vector3f();
    final Vector3f colourMod = new Vector3f();
    final Vector3f colourStep = new Vector3f();

    //LAB_800fe180
    for(int i = 0; i < effect.countParticleInstance_50; i++) {
      final ParticleEffectInstance94 particle = effect.particleArray_68[i];

      if(checkParticleShouldRender(state, manager, effect, particle)) {
        //LAB_800fe1bc
        for(int j = effect.countParticleSub_54 - 1; j > 0; j--) {
          particle.particleInstanceSubArray_80[j].copy(particle.particleInstanceSubArray_80[j - 1]);
        }

        //LAB_800fe1fc
        effect.prerenderCallback_84.accept(manager, effect, particle);

        tickParticleAttributes(manager, effect, particle, colour);

        if((effect.effectInner_08.particleInnerStuff_1c & 0x1000_0000) == 0 || (particle.flags_90 & 0x8) == 0) {
          //LAB_800fe280
          colourMod.set(0, 0, 0);
        } else {
          colourMod.set(colour).negate().div(2.0f);
        }

        //LAB_800fe28c
        particle.flags_90 = particle.flags_90 & 0xffff_fff7 | (~(particle.flags_90 >>> 3) & 0x1) << 3;

        if((manager._10.flags_00 & 0x400_0000) == 0) {
          // This is super bugged in retail and passes garbage as the last 3 params to both methods.
          // Hopefully this is fine with them all zeroed. This is used for the Glare's bewitching attack.
          particle.managerRotation_68.setY((short)(ratan2(
            FUN_800dc408(0, 0, 0, 0, 0) - particle.particlePosition_50.getX(),
            FUN_800dc408(2, 0, 0, 0, 0) - particle.particlePosition_50.getZ()
          ) + 0x400));
        }

        //LAB_800fe300
        final VECTOR translation = new VECTOR().set(particle.particlePosition_50);

        MathHelper.clamp(colour.add(colourMod), 0.0f, 0.5f);

        final GpuCommandPoly cmd1 = new GpuCommandPoly(4)
          .clut((effect.clut_5c & 0b111111) * 16, effect.clut_5c >>> 6)
          .vramPos(effect.u_58 & 0x3c0, effect.v_5a < 256 ? 0 : 256)
          .rgb((int)(colour.x * 0xff), (int)(colour.y * 0xff), (int)(colour.z * 0xff))
          .uv(0, (effect.u_58 & 0x3f) * 4, effect.v_5a)
          .uv(1, (effect.u_58 & 0x3f) * 4 + effect.w_5e - 1, effect.v_5a)
          .uv(2, (effect.u_58 & 0x3f) * 4, effect.v_5a + effect.h_5f - 1)
          .uv(3, (effect.u_58 & 0x3f) * 4 + effect.w_5e - 1, effect.v_5a + effect.h_5f - 1);

        if((manager._10.flags_00 & 1 << 30) != 0) {
          cmd1.translucent(Translucency.of(manager._10.flags_00 >>> 28 & 0b11));
        }

        final int instZ = FUN_800fca78(manager, effect, particle, translation, cmd1) >> 2;
        int effectZ = manager._10.z_22;
        if(effectZ + instZ >= 160) {
          if(effectZ + instZ >= 4094) {
            effectZ = 4094 - instZ;
          }

          //LAB_800fe548
          GPU.queueCommand(instZ + effectZ >> 2, cmd1);
        }

        //LAB_800fe564
        if((effect.effectInner_08.particleInnerStuff_1c & 0x6000_0000) != 0) {
          ParticleEffectInstance94Sub10 particleSub = particle.particleInstanceSubArray_80[0];
          particleSub.x0_00 = cmd1.getX(0);
          particleSub.y0_02 = cmd1.getY(0);
          particleSub.x1_04 = cmd1.getX(1);
          particleSub.y1_06 = cmd1.getY(1);
          particleSub.x2_08 = cmd1.getX(2);
          particleSub.y2_0a = cmd1.getY(2);
          particleSub.x3_0c = cmd1.getX(3);
          particleSub.y3_0e = cmd1.getY(3);
          colourStep.set(colour).div(effect.countParticleSub_54);

          final int count = Math.min(-particle.framesUntilRender_04, effect.countParticleSub_54);

          //LAB_800fe61c
          //LAB_800fe628
          for(int k = 0; k < count; k++) {
            effectZ = manager._10.z_22;
            if(effectZ + instZ >= 160) {
              if(effectZ + instZ >= 4094) {
                effectZ = 4094 - instZ;
              }

              final GpuCommandPoly cmd2 = new GpuCommandPoly(cmd1);

              particleSub = particle.particleInstanceSubArray_80[k];

              //LAB_800fe644
              cmd2
                .rgb((int)(colour.x * 0xff), (int)(colour.y * 0xff), (int)(colour.z * 0xff))
                .pos(0, particleSub.x0_00, particleSub.y0_02)
                .pos(1, particleSub.x1_04, particleSub.y1_06)
                .pos(2, particleSub.x2_08, particleSub.y2_0a)
                .pos(3, particleSub.x3_0c, particleSub.y3_0e);

              //LAB_800fe78c
              GPU.queueCommand(instZ + effectZ >> 2, cmd2);
            }

            colour.sub(colourStep);
          }
        }
      }
      //LAB_800fe7b8
    }

    //LAB_800fe7ec
    if(effect.scaleOrUseEffectAcceleration_6c) {
      effect.effectAcceleration_70.mul(effect.scaleParticleAcceleration_80).shra(8);
    }
    //LAB_800fe848
  }

  @Method(0x800fe878L)
  @Nullable
  public static ParticleEffectData98 findParticleParent(final ParticleEffectData98 effect) {
    if(firstParticle_8011a00c == effect) {
      return null;
    }

    //LAB_800fe894
    ParticleEffectData98 parent = firstParticle_8011a00c;
    do {
      if(parent.next_94 == effect) {
        break;
      }

      parent = parent.next_94;
    } while(parent != null);

    //LAB_800fe8b0
    return parent;
  }

  @Method(0x800fe8b8L)
  public static void particleEffectDestructor(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final ParticleEffectData98 effect = (ParticleEffectData98)manager.effect_44;
    final ParticleEffectData98 effectParent = findParticleParent(effect);

    if(effectParent == null) {
      firstParticle_8011a00c = effect.next_94;
    } else {
      //LAB_800fe8f0
      effectParent.next_94 = effect.next_94;
    }

    //LAB_800fe8fc
    if(effect.next_94 == null) {
      lastParticle_8011a010 = effectParent;
    }
    //LAB_800fea30
    //LAB_800fea3c
  }

  @Method(0x800fea68L)
  public static void FUN_800fea68(final EffectManagerData6c a0, final ParticleEffectData98 a1, final ParticleEffectInstance94 a2, final ParticleEffectData98Inner24 a3) {
    // no-op
  }

  @Method(0x800fea70L)
  public static long FUN_800fea70(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    seed_800fa754.advance();
    final int angle = (int)(seed_800fa754.get() % 4097);
    particle.particleVelocity_58.setX((short)(rcos(angle) >> 8));
    particle.particleVelocity_58.setZ((short)(rsin(angle) >> 8));

    seed_800fa754.advance();
    particle.particleVelocity_58.setY((short)-(seed_800fa754.get() % 91 + 10));

    seed_800fa754.advance();
    final float colourStep = (seed_800fa754.get() % 101 - 50) / (float)0x80;
    particle.r_84 += colourStep;
    particle.g_86 += colourStep;
    particle.b_88 += colourStep;

    return angle;
  }

  @Method(0x800fec3cL)
  public static void FUN_800fec3c(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    final int s0 = (short)FUN_800fea70(manager, effect, particle, effectInner);
    seed_800fa754.advance();
    particle.particleVelocity_58.setX((short)(rcos(s0) >> 6));
    particle.particleVelocity_58.setY((short)0);
    particle.particleVelocity_58.setZ((short)(rsin(s0) >> 6));
  }

  @Method(0x800fecccL)
  public static void FUN_800feccc(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    seed_800fa754.advance();
    final int angle = (int)(seed_800fa754.get() % 4097);
    particle.particleVelocity_58.setX((short)(rcos(angle) >> 10));
    seed_800fa754.advance();
    particle.particleVelocity_58.setY((short)-(seed_800fa754.get() % 33 + 13));
    particle.particleVelocity_58.setZ((short)(rsin(angle) >> 10));
    seed_800fa754.advance();
    particle._14 = (short)(seed_800fa754.get() % 3 + 1);
    seed_800fa754.advance();
    particle._16 = (short)(seed_800fa754.get() % 3);
    particle.framesUntilRender_04 = (short)(particle.framesUntilRender_04 / 4 * 4 + 1);
  }

  @Method(0x800fee9cL)
  public static void FUN_800fee9c(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    seed_800fa754.advance();
    final int angle = (int)(seed_800fa754.get() % 4097);
    particle.particleVelocity_58.setX((short)(rcos(angle) / 0x80));
    particle.particleVelocity_58.setY((short)0);
    particle.particleVelocity_58.setZ((short)(rsin(angle) / 0x80));
    particle.framesUntilRender_04 = 1;
    seed_800fa754.advance();
    particle._14 = (short)(seed_800fa754.get() % 6);
  }

  @Method(0x800fefe4L)
  public static void FUN_800fefe4(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    FUN_800fee9c(manager, effect, particle, effectInner);
    particle._14 = (short)(seed_800fa754.advance().get() % 4097);
    particle._16 = effectInner._10;
    particle._18 = (short)(seed_800fa754.advance().get() % 91 + 10);
    particle._1a.setX((short)120);
    particle.particleVelocity_58.setX((short)0);
    particle.particleVelocity_58.setY((short)-(seed_800fa754.advance().get() % 11 + 5));
    particle.particleVelocity_58.setZ((short)0);
  }

  @Method(0x800ff15cL)
  public static void FUN_800ff15c(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    seed_800fa754.advance();
    particle.particleVelocity_58.setY((short)-(seed_800fa754.get() % 61 + 60));

    final short scaleStep;
    if(vsyncMode_8007a3b8 != 4) {
      scaleStep = (short)-(seed_800fa754.advance().get() % 14 + 5);
    } else {
      //LAB_800ff248
      scaleStep = (short)-(seed_800fa754.advance().get() % 21 + 10);
    }

    particle.scaleHorizontalStep_0a = scaleStep;
    particle.scaleVerticalStep_0c = scaleStep;

    //LAB_800ff2b4
    particle._14 = (short)(seed_800fa754.advance().get() % 4097);
    particle._1a.setX((short)(seed_800fa754.advance().get() % 4097));
    particle._1a.setZ((short)(seed_800fa754.advance().get() % 1025 - 512));

    particle._1a.setY((short)100);
    particle._16 = effectInner._10;
    particle._18 = (short)(effectInner._18 * 100 / 256);
  }

  @Method(0x800ff3e0L)
  public static void FUN_800ff3e0(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    FUN_800ff15c(manager, effect, particle, effectInner);
    particle.verticalPositionScale_20 = (short)0;
    particle.ticksUntilMovementModeChanges_22 = (short)(0x8000 / particle.ticksRemaining_12);
    particle.angleAcceleration_24 = (short)((effectInner.particleInnerStuff_1c >>> 8 & 0xff) << 8);
  }

  @Method(0x800ff430L)
  public static void FUN_800ff430(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle._16 = effectInner._10;
    particle._18 = (short)0;
    particle._1a.setX((short)0);
    particle._1a.setY((short)50);
    particle.verticalPositionScale_20 = (short)100;
    particle.ticksUntilMovementModeChanges_22 = (short)0;
    particle.angleAcceleration_24 = (short)(particle.angleVelocity_10 / particle.ticksRemaining_12);

    particle.particleVelocity_58.setY((short)(seed_800fa754.advance().get() % 31 + 10));
    particle._1a.setZ((short)(seed_800fa754.advance().get() % 41 + 40));
    particle._14 = (short)(seed_800fa754.advance().get() % 4097);
  }

  @Method(0x800ff590L)
  public static void FUN_800ff590(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    FUN_800ff430(manager, effect, particle, effectInner);
    particle.ticksUntilMovementModeChanges_22 = 20;
    particle.verticalPositionScale_20 = 10;
  }

  /**
   * {@link SEffe#FUN_800ff5c4} uses t2 which isn't set... assuming the value even matters, this is to pass in t2 from the previous method
   */
  private static Long brokenT2For800ff5c4;

  @Method(0x800ff5c4L)
  public static void FUN_800ff5c4(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    if(brokenT2For800ff5c4 == null) {
      throw new RuntimeException("t2 was not set");
    }

    final long t2 = brokenT2For800ff5c4;
    brokenT2For800ff5c4 = null;

    seed_800fa754.advance();
    particle.particleVelocity_58.setY((short)(-(seed_800fa754.get() % 61 + 60) * effectInner._18 / 256));
    seed_800fa754.advance();
    particle._14 = (short)(seed_800fa754.get() % 4097);
    particle._16 = effectInner._10;
    particle._18 = (short)100;
    particle._1a.setX((short)(t2 * 4));
    particle.particleAcceleration_60.setY((short)(-particle.particleVelocity_58.getY() / particle.ticksRemaining_12));
  }

  @Method(0x800ff6d4L)
  public static void FUN_800ff6d4(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    FUN_800ff5c4(manager, effect, particle, effectInner);
    particle._18 = 0;
  }

  @Method(0x800ff6fcL)
  public static void FUN_800ff6fc(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    final int a0 = currentParticleIndex_8011a008.get();
    particle.angleVelocity_10 = -0x80;
    final int v1 = a0 >>> 1;
    particle.angle_0e = (short)(v1 * 128);
    particle._14 = (short)(v1 * 128);
    particle._16 = effectInner._10;
    particle._18 = (short)(a0 & 0x1);
    particle._1a.setX((short)(v1 * 8));

    final float colour = Math.max(0, (effectInner.particleInnerStuff_1c >>> 8 & 0xff) - a0 * 16) / (float)0x80;

    //LAB_800ff754
    particle.ticksRemaining_12 = -1;
    particle.r_84 = colour;
    particle.g_86 = colour;
    particle.b_88 = colour;
  }

  @Method(0x800ff788L)
  public static void FUN_800ff788(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle.ticksRemaining_12 = -1;
    particle._14 = (short)(seed_800fa754.advance().get() % 4097);
    particle._16 = effectInner._10;
    particle._18 = (short)(seed_800fa754.advance().get() % 4097);
    particle._1a.setX((short)(seed_800fa754.advance().get() % 4097));
  }

  @Method(0x800ff890L)
  public static void FUN_800ff890(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    seed_800fa754.advance();
    seed_800fa754.advance();
    final int angle1 = (int)(seed_800fa754.get() % 4097);

    seed_800fa754.advance();
    final int angle2 = (int)(seed_800fa754.get() % 2049);

    particle.particleVelocity_58.setX((short)(rcos(angle1) * rsin(angle2) / 0x40000));
    particle.particleVelocity_58.setY((short)(rcos(angle2) / 0x40));
    particle.particleVelocity_58.setZ((short)(rsin(angle1) * rsin(angle2) / 0x40000));

    seed_800fa754.advance();
    particle.ticksRemaining_12 += (short)(seed_800fa754.get() % 21 - 10);

    if(particle.ticksRemaining_12 <= 0) {
      particle.ticksRemaining_12 = 1;
    }
    //LAB_800ffa60
  }

  @Method(0x800ffa80L)
  public static void FUN_800ffa80(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    FUN_800ff5c4(manager, effect, particle, effectInner);
    final short s2 = effectInner._10;
    particle._16 = s2;
    particle._1a.setY(s2);
    particle._18 = (short)(effectInner._18 * 0xe0 >> 8);
  }

  @Method(0x800ffadcL)
  public static void FUN_800ffadc(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle._14 = (short)(seed_800fa754.advance().get() % 4097);
    final int v0 = -effectInner._10 >> 5;
    final int a0 = effectInner._18;
    particle.r_84 = 0;
    particle.g_86 = 0;
    particle.b_88 = 0;
    particle._16 = effectInner._10;
    particle._18 = (short)(v0 * a0 >> 8);
    particle._1a.setX((short)(a0 >>> 1));
  }

  @Method(0x800ffb80L)
  public static void FUN_800ffb80(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    final long v0;
    long a0;
    particle.particleAcceleration_60.setY((short)8);
    a0 = currentParticleIndex_8011a008.get();
    v0 = a0 << 9;
    particle._14 = (short)v0;
    a0 = a0 >>> 2;
    a0 = a0 | 0x1L;
    particle._16 = effectInner._10;
    particle.framesUntilRender_04 = (short)a0;
    particle._18 = (short)(effectInner._18 >>> 2);
    particle.particleVelocity_58.setY((short)(effectInner._14 * -0x40 / 0x100));
  }

  @Method(0x800ffbd8L)
  public static void FUN_800ffbd8(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    final int t6 = effectInner._18;
    particle._14 = 0;
    particle._18 = (short)(seed_800fa754.advance().get() % 21 - 10);
    particle._1a.setX((short)(seed_800fa754.advance().get() % 21 - 10));
    particle._1a.setY((short)(seed_800fa754.advance().get() % 81 - 40));
    particle._1a.setZ(particle.ticksRemaining_12);
    particle.particleVelocity_58.setX((short)((seed_800fa754.advance().get() % 41 + 44) * t6 >> 8));
    particle.particleVelocity_58.setY((short)((seed_800fa754.advance().get() % 81 - 40) * t6 >> 8));
    particle.particleVelocity_58.setZ((short)((seed_800fa754.advance().get() % 41 + 44) * t6 >> 8));
    particle.ticksRemaining_12 += 20;
  }

  @Method(0x800ffe80L)
  public static void FUN_800ffe80(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle._16 = effectInner._10;
    particle._18 = (short)(effectInner._18 >>> 3);
    particle.particleAcceleration_60.setY((short)(effectInner._18 >>> 7));

    seed_800fa754.advance();
    particle._14 = (short)(seed_800fa754.get() % 4097);
  }

  @Method(0x800ffefcL)
  public static void FUN_800ffefc(final EffectManagerData6c a0, final ParticleEffectData98 a1, final ParticleEffectInstance94 a2, final ParticleEffectData98Inner24 a3) {
    // no-op
  }

  @Method(0x800fff04L)
  public static void FUN_800fff04(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle._14 = 0;
    particle._18 = 0;
    particle._1a.setZ((short)0);
    final short v0 = (short)((effectInner._18 & 0xffff) >>> 2);
    particle._16 = v0;
    particle._1a.setX(v0);
    particle._1a.setY(effectInner._10);
  }

  @Method(0x800fff30L)
  public static void FUN_800fff30(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle.particleVelocity_58.setX((short)(seed_800fa754.advance().get() % 769 + 256));
  }

  @Method(0x800fffa0L)
  public static void FUN_800fffa0(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle._1a.setX(effectInner._10);
    particle._14 = (short)(seed_800fa754.advance().get() % 4097);
    particle._16 = (short)((seed_800fa754.advance().get() % 123 + 64) * effectInner._18 >> 8);
    particle._18 = (short)(0x800 / effect.countParticleInstance_50 * currentParticleIndex_8011a008.get());
  }

  @Method(0x801000b8L)
  public static void FUN_801000b8(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle.particleVelocity_58.setX((short)(-particle.particlePosition_50.getX() / 32));
    particle.particleVelocity_58.setZ((short)(-particle.particlePosition_50.getZ() / 32));
  }

  @Method(0x801000f8L)
  public static void FUN_801000f8(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    FUN_800ff890(manager, effect, particle, effectInner);
    particle.particleVelocity_58.setY((short)-Math.abs(particle.particleVelocity_58.getY()));
    particle._14 = (short)(effectInner._18 * 0x300 / 0x100);
  }

  @Method(0x80100150L)
  public static void FUN_80100150(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    seed_800fa754.advance();
    particle._1a.setX((short)(seed_800fa754.get() % 4097));
    particle._14 = particle.particlePosition_50.getX();
    particle._16 = particle.particlePosition_50.getY();
    particle._18 = particle.particlePosition_50.getZ();
    particle._1a.setZ((short)(effectInner._10 >>> 2));
    particle.particleVelocity_58.setY((short)(effectInner._18 * -64 >> 8));
    seed_800fa754.advance();
    particle._1a.setY((short)((int)((seed_800fa754.get() % 513 - 256) * effectInner._18) >> 8));
  }

  @Method(0x8010025cL)
  public static void FUN_8010025c(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle.particleVelocity_58.setY((short)64);
    final int angle = (int)(seed_800fa754.advance().get() % 4097);
    if(effectInner.callbackIndex_20 == 0x2a) {
      final int velocityMagnitude = (effectInner._10 & 0xffff) >>> 5;
      particle.particleVelocity_58.setX((short)(rcos(angle) * velocityMagnitude >> 12));
      particle.particleVelocity_58.setY((short)(rsin(angle) * velocityMagnitude >> 12));
    } else {
      //LAB_80100328
      particle.particleVelocity_58.setX((short)(rcos(angle) >>> 7));
      particle.particleVelocity_58.setY((short)(rsin(angle) >>> 7));
    }
  }

  @Method(0x801003e8L)
  public static void FUN_801003e8(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    final int s4 = effectInner._10; //TODO read with lw here but as a short everywhere else? Is this a bug?
    FUN_800ff890(manager, effect, particle, effectInner);

    final int angle1 = (int)(seed_800fa754.advance().get() % 4097);
    final int angle2 = (int)(seed_800fa754.advance().get() % 2049);
    particle.ticksRemaining_12 = (short)(effectInner.particleInnerStuff_1c >>> 16 & 0xff);
    particle.particlePosition_50.setX((short)((rcos(angle1) * rsin(angle2) >> 12) * s4 >> 12));
    particle.particlePosition_50.setY((short)(rcos(angle2) * s4 >> 12));
    particle.particlePosition_50.setZ((short)((rsin(angle1) * rsin(angle2) >> 12) * s4 >> 12));
    particle.particleVelocity_58.setX((short)(rcos(angle1) * rsin(angle2) >> 18));
    particle.particleVelocity_58.setY((short)(rcos(angle2) >> 6));
    particle.particleVelocity_58.setZ((short)(rsin(angle1) * rsin(angle2) >> 18));
  }

  @Method(0x80100364L)
  public static void FUN_80100364(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 particleEffect) {
    FUN_800ff890(manager, effect, particle, particleEffect);
    particle._14 = 0;
    particle.particleVelocity_58.setY((short)-Math.abs(particle.particleVelocity_58.getY()));
    particle.particleAcceleration_60.set(particle.particleVelocity_58).negate().div(particle.ticksRemaining_12);
  }

  @Method(0x801005b8L)
  public static void FUN_801005b8(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    final int s2 = effectInner._10;
    final int s4 = effectInner._18;
    FUN_800ff890(manager, effect, particle, effectInner);
    final int angle = (int)(seed_800fa754.advance().get() % 4097);
    particle.particlePosition_50.setY((short)(rsin(angle) * s2 >> 12));
    particle.particlePosition_50.setZ((short)(rcos(angle) * s2 >> 12));
    particle.particleVelocity_58.setX((short)((seed_800fa754.advance().get() % 65 + 54) * s4 >> 8));
    particle.particleAcceleration_60.setX((short)(-particle.particleVelocity_58.getX() / particle.ticksRemaining_12));
    particle.particleAcceleration_60.setY((short)0x10);
    final int a1_0 = -((rsin(angle) * s2 >> 12) + s2) / 2;
    particle.particleVelocity_58.setY((short)((seed_800fa754.advance().get() % (-a1_0 + 1) + a1_0) * s4 >> 8));
  }

  @Method(0x801007b4L)
  public static void FUN_801007b4(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle.particleVelocity_58.set(particle.particlePosition_50).negate().div(particle.ticksRemaining_12);
  }

  @Method(0x80100800L)
  public static void FUN_80100800(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle._16 = (short)(effectInner._18 >>> 2);
    particle._18 = effectInner._10;
    particle._14 = (short)(seed_800fa754.advance().get() % 4097);
  }

  @Method(0x80100878L)
  public static void FUN_80100878(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle.particleVelocity_58.setY((short)-0x40);
    particle._18 = effectInner._10;
    particle._16 = (short)(effectInner._18 * 0x20);
    particle._14 = (short)(seed_800fa754.advance().get() % 4097);
  }

  @Method(0x801008f8L)
  public static void FUN_801008f8(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle.angle_0e = 0;
    particle.angleVelocity_10 = 0;
    particle.spriteRotation_70.set((short)0, (short)0, (short)0);
    particle.spriteRotationStep_78.set((short)0, (short)0, (short)0);
    particle._18 = effectInner._18;
    particle._14 = 0x800;
    particle._16 = (short)(effectInner._10 << 4);
    particle._1a.setX((short)(effectInner._18 >>> 2));
    particle.framesUntilRender_04 = (short)(currentParticleIndex_8011a008.get() * (effectInner._14 / effectInner.totalCountParticleInstance_0c));
  }

  @Method(0x80100978L)
  public static void FUN_80100978(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle._14 = (short)(seed_800fa754.advance().get() % 4097);
    particle._16 = (short)(seed_800fa754.advance().get() % ((effectInner._10 & 0xffff) + 1));
    particle._18 = (short)(seed_800fa754.advance().get() % 4097);
    particle._1a.setX((short)((seed_800fa754.advance().get() % 41 + 150) * effectInner._18 >> 8));
    particle.particleVelocity_58.setY((short)-0x40);
  }

  @Method(0x80100af4L)
  public static void FUN_80100af4(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    FUN_800ff890(manager, effect, particle, effectInner);
    particle.particleVelocity_58.setX((short)(particle.particleVelocity_58.getX() * effectInner._18 >> 8));
    particle.particleVelocity_58.setY((short)(particle.particleVelocity_58.getY() * effectInner._18 >> 8));
    particle.particleVelocity_58.setZ((short)(particle.particleVelocity_58.getZ() * effectInner._18 >> 8));
    particle.particleAcceleration_60.set(particle.particleVelocity_58).negate().div(particle.ticksRemaining_12);
  }

  @Method(0x80100bb4L)
  public static void FUN_80100bb4(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle.particleVelocity_58.setY((short)(seed_800fa754.advance().get() % 33 + 16));
  }

  @Method(0x80100c18L)
  public static void FUN_80100c18(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    final int v1 = effectInner._18;
    particle._14 = (short)((-particle.particlePosition_50.getX() >> 1) * v1 >> 8);
    particle._16 = (short)((-particle.particlePosition_50.getY() >> 1) * v1 >> 8);
    particle._18 = (short)((-particle.particlePosition_50.getZ() >> 1) * v1 >> 8);
    particle._1a.set((short)0, (short)0, (short)0);
  }

  @Method(0x80100cacL)
  public static void FUN_80100cac(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    FUN_800ff890(manager, effect, particle, effectInner);
    particle.particleVelocity_58.setY((short)-Math.abs(particle.particleVelocity_58.getY()));
    particle.particleVelocity_58.setX((short)0);
    particle._14 = 0;
  }

  @Method(0x80100cecL)
  public static void FUN_80100cec(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    particle._14 = 0;
    particle.particleAcceleration_60.setY((short)(effectInner._18 >>> 7));
  }

  @Method(0x80100d00L)
  public static void FUN_80100d00(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    final VECTOR translation = new VECTOR();
    getModelObjectTranslation(effectInner.parentScriptIndex_04, translation, currentParticleIndex_8011a008.get());
    particle.particlePosition_50.set(translation);
  }

  @Method(0x80100d58L)
  public static void FUN_80100d58(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c a1, final ParticleEffectData98 a2, final ParticleEffectInstance94 a3) {
    // no-op
  }

  @Method(0x80100d60L)
  public static void FUN_80100d60(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    if(particle.framesUntilRender_04 == 0 && effect.parentScriptIndex_04 != -1) {
      final VECTOR translation = new VECTOR();
      scriptGetScriptedObjectPos(state.index, translation);

      final VECTOR parentTranslation = new VECTOR();
      scriptGetScriptedObjectPos(effect.parentScriptIndex_04, parentTranslation);

      final VECTOR diffTranslation = new VECTOR().set(translation).sub(parentTranslation);
      particle.particlePosition_50.sub(diffTranslation);
    }
    //LAB_80100e14
  }

  @Method(0x80100e28L)
  public static void FUN_80100e28(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    if(particle.framesUntilRender_04 == 0) {
      particle.stepR_8a = 0;
      particle.stepG_8c = 0;
      particle.stepB_8e = 0;
    }
    //LAB_80100e44
  }

  @Method(0x80100e4cL)
  public static void FUN_80100e4c(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    if(particle.framesUntilRender_04 == 0) {
      particle.stepR_8a = -1.0f / particle.ticksRemaining_12;
      particle.stepG_8c = -1.0f / particle.ticksRemaining_12;
      particle.stepB_8e = -1.0f / particle.ticksRemaining_12;
    }
    //LAB_80100e98
  }

  @Method(0x80100ea0L)
  public static void FUN_80100ea0(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    final int s2 = effect.effectInner_08._18;
    final int s1 = effect.effectInner_08._10 & 0xffff;

    final VECTOR selfTranslation = new VECTOR();
    final VECTOR parentTranslation = new VECTOR();
    scriptGetScriptedObjectPos(effect.myState_00.index, selfTranslation);
    scriptGetScriptedObjectPos(effect.parentScriptIndex_04, parentTranslation);

    final VECTOR diffTranslation = new VECTOR().set(parentTranslation).sub(selfTranslation);

    final int mod = s1 * 2 + 1;
    particle.particleVelocity_58.setX((short)((diffTranslation.getX() >> 3) + (seed_800fa754.advance().get() % mod - s1 >>> 4)));
    particle.particleVelocity_58.setY((short)((diffTranslation.getY() >> 3) + (seed_800fa754.advance().get() % mod - s1 >>> 4)));
    particle.particleVelocity_58.setZ((short)((diffTranslation.getZ() >> 3) + (seed_800fa754.advance().get() % mod - s1 >>> 4)));
    particle.particleVelocity_58.x.mul((short)s2).shra(8);
    particle.particleVelocity_58.y.mul((short)s2).shra(8);
    particle.particleVelocity_58.z.mul((short)s2).shra(8);

    if(particle.particleVelocity_58.getX() == 0) {
      particle.particleVelocity_58.setX((short)1);
    }

    //LAB_80101068
    particle.ticksRemaining_12 = (short)(diffTranslation.getX() / particle.particleVelocity_58.getX());
  }

  @Method(0x801010a0L)
  public static void FUN_801010a0(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    // Calculate the index of this array element
    currentParticleIndex_8011a008.set(particle.index);

    initializeParticleInstance(manager, effect, particle, effect.effectInner_08);

    if(effect.countParticleSub_54 != 0) {
      final int v1 = effect.subParticleType_60;

      if(v1 == 0) {
        //LAB_801011a0
        final GpuCommandPoly cmd = new GpuCommandPoly(4);

        //LAB_801011d8
        for(int i = 0; i < effect.countParticleSub_54; i++) {
          final ParticleEffectInstance94Sub10 s0 = particle.particleInstanceSubArray_80[i];

          final VECTOR translation = new VECTOR().set(particle.particlePosition_50);
          FUN_800fca78(manager, effect, particle, translation, cmd);
          s0.x0_00 = cmd.getX(0);
          s0.y0_02 = cmd.getY(0);
          s0.x1_04 = cmd.getX(1);
          s0.y1_06 = cmd.getY(1);
          s0.x2_08 = cmd.getX(2);
          s0.y2_0a = cmd.getY(2);
          s0.x3_0c = cmd.getX(3);
          s0.y3_0e = cmd.getY(3);
        }
        //LAB_8010114c
      } else if(v1 == 2 || v1 >= 4 && v1 < 6) {
        //LAB_80101160
        //LAB_80101170
        for(int i = 0; i < effect.countParticleSub_54; i++) {
          particle.subParticlePositionsArray_44[i].set(particle.particlePosition_50);
        }
      }
    }
    //LAB_8010127c
  }

  @Method(0x801012a0L)
  public static void FUN_801012a0(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    if((manager._10._24 & 0x4) != 0) {
      FUN_801010a0(state, manager, effect, particle);
    }
    //LAB_801012c4
  }

  @Method(0x801012d4L)
  public static void FUN_801012d4(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle) {
    if((manager._10._24 & 0x4) == 0) {
      FUN_801010a0(state, manager, effect, particle);
    }
    //LAB_801012f8
  }

  @Method(0x80101308L)
  public static void initializeParticleInstance(final EffectManagerData6c manager, final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner) {
    if((effectInner.particleInnerStuff_1c & 0xff) == 0) {
      effectInner.particleInnerStuff_1c &= 0xffff_ff00;
      effectInner.particleInnerStuff_1c |= particleInnerStuffDefaultsArray_801197ec.get(effectInner.callbackIndex_20).ticksRemaining_02.get();
    }

    //LAB_8010137c
    if((effectInner.particleInnerStuff_1c & 0xff00) == 0) {
      effectInner.particleInnerStuff_1c &= 0xffff_00ff;
      effectInner.particleInnerStuff_1c |= particleInnerStuffDefaultsArray_801197ec.get(effectInner.callbackIndex_20).colour_01.get() << 8;
    }

    //LAB_801013c0
    if((effectInner.particleInnerStuff_1c & 0xff_0000) == 0) {
      effectInner.particleInnerStuff_1c &= 0xff00_ffff;
      effectInner.particleInnerStuff_1c |= particleInnerStuffDefaultsArray_801197ec.get(effectInner.callbackIndex_20).renderFrameCount_00.get() << 16;
    }

    //LAB_80101400
    effect.callback90Type_61 = particleInnerStuffDefaultsArray_801197ec.get(effectInner.callbackIndex_20).callbackType_03.get();
    particle.unused_00 = 0x73;
    particle.unused_01 = 0x6d;
    particle.unused_02 = 0x6b;
    particle.particlePosition_50.set((short)0, (short)0, (short)0);
    particle.particleVelocity_58.set((short)0, (short)0, (short)0);
    particle.scaleHorizontal_06 = 0;
    particle.scaleVertical_08 = 0;
    particle.scaleHorizontalStep_0a = 0;
    particle.scaleVerticalStep_0c = 0;
    particle.particleAcceleration_60.set((short)0, (short)0, (short)0);
    particle.stepR_8a = 0;
    particle.stepG_8c = 0;
    particle.stepB_8e = 0;
    final int callbackIndex = effectInner.callbackIndex_20;
    brokenT2For800ff5c4 = (long)callbackIndex;
    particle.framesUntilRender_04 = (short)(seed_800fa754.advance().get() % (effectInner._14 + 1) + 1);
    particle.ticksRemaining_12 = (short)((effectInner.particleInnerStuff_1c & 0xff_0000) >>> 16);
    final float colour = ((effectInner.particleInnerStuff_1c & 0xff00) >>> 8) / (float)0x80;
    particle.r_84 = colour;
    particle.g_86 = colour;
    particle.b_88 = colour;
    particle.flags_90 |= 0x1;
    particle.angle_0e = (short)(seed_800fa754.advance().get() % 4097);
    particle.angleVelocity_10 = (short)(seed_800fa754.advance().get() % 513 - 256);
    particle.spriteRotation_70.setX((short)(seed_800fa754.advance().get() % 4097));
    particle.spriteRotation_70.setY((short)(seed_800fa754.advance().get() % 4097));
    particle.spriteRotation_70.setZ((short)(seed_800fa754.advance().get() % 4097));
    particle.spriteRotationStep_78.setX((short)(seed_800fa754.advance().get() % 129 - 64));
    particle.spriteRotationStep_78.setY((short)(seed_800fa754.advance().get() % 129 - 64));
    particle.spriteRotationStep_78.setZ((short)0);
    particle.flags_90 = particle.flags_90 & 0xffff_fff1 | (seed_800fa754.advance().get() % 101 < 50 ? 0 : 0x8);
    final SomeParticleStruct10 s5 = _801198f0.get(callbackIndex);
    final int v1 = s5._00.get();
    if(v1 == 1) {
      //LAB_80101840
      final int angle = (int)(seed_800fa754.advance().get() % 4097);
      final short baseTranslationMagnitude = effectInner._10;
      particle.particlePosition_50.setX((short)(rcos(angle) * (int)baseTranslationMagnitude >> s5.initialTranslationMagnitudeReduction_02.get()));
      particle.particlePosition_50.setY((short)0);
      particle.particlePosition_50.setZ((short)(rsin(angle) * (int)baseTranslationMagnitude >> s5.initialTranslationMagnitudeReduction_02.get()));
      //LAB_80101824
    } else if(v1 == 2) {
      //LAB_801018c8
      final int angle = (int)(seed_800fa754.advance().get() % 4097);
      final int baseTranslationMagnitude = (int)(seed_800fa754.advance().get() % (effectInner._10 + 1));
      particle.particlePosition_50.setX((short)(rcos(angle) * baseTranslationMagnitude >> s5.initialTranslationMagnitudeReduction_02.get()));
      particle.particlePosition_50.setY((short)0);
      particle.particlePosition_50.setZ((short)(rsin(angle) * baseTranslationMagnitude >> s5.initialTranslationMagnitudeReduction_02.get()));
    } else if(v1 == 3) {
      //LAB_80101990
      particle.particlePosition_50.setY((short)(seed_800fa754.advance().get() % (s5._04.get() - s5.initialTranslationMagnitudeReduction_02.get() + 1) + s5.initialTranslationMagnitudeReduction_02.get()));
    } else if(v1 == 4) {
      //LAB_801019e4
      final int angle1 = (int)(seed_800fa754.advance().get() % 4097);
      final int angle2 = (int)(seed_800fa754.advance().get() % 2049);
      particle.particlePosition_50.setX((short)((rcos(angle1) * rsin(angle2) >> s5.initialTranslationMagnitudeReduction_02.get()) * effectInner._10 >> s5._04.get()));
      particle.particlePosition_50.setY((short)(rcos(angle2) * effectInner._10 >> s5._04.get()));
      particle.particlePosition_50.setZ((short)((rsin(angle1) * rsin(angle2) >> s5.initialTranslationMagnitudeReduction_02.get()) * effectInner._10 >> s5._04.get()));
    }

    //LAB_80101b10
    //LAB_80101b18
    effect.initializerCallback_8c.accept(manager, effect, particle, effectInner);

    if(s5.hasVelocity_06.get() == 1) {
      particle.particleVelocity_58.setX((short)(particle.particleVelocity_58.getX() * effectInner._18 >> 8));
      particle.particleVelocity_58.setY((short)(particle.particleVelocity_58.getY() * effectInner._18 >> 8));
      particle.particleVelocity_58.setZ((short)(particle.particleVelocity_58.getZ() * effectInner._18 >> 8));
    }

    //LAB_80101ba4
    if(s5.hasScaleStep_07.get() == 1) {
      final byte scaleStep = (byte)(seed_800fa754.advance().get() % (s5._09.get() - s5._08.get() + 1) + s5._08.get());
      particle.scaleHorizontalStep_0a = scaleStep;
      particle.scaleVerticalStep_0c = scaleStep;
    }

    //LAB_80101c20
    particle.particlePositionCopy2_48.set(particle.particlePosition_50);
  }

  @Method(0x80101c68L)
  public static void FUN_80101c68(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner, final int flags) {
    effect.subParticleType_60 = 3;
  }

  @Method(0x80101c74L)
  public static void FUN_80101c74(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner, final int flags) {
    effect.subParticleType_60 = (byte)(flags >> 20);
    effect.countParticleSub_54 = (short)flags;

    //LAB_80101cb0
    for(int i = 0; i < effect.countParticleInstance_50; i++) {
      final ParticleEffectInstance94 inst = effect.particleArray_68[i];
      inst.subParticlePositionsArray_44 = new SVECTOR[effect.countParticleSub_54];
      Arrays.setAll(inst.subParticlePositionsArray_44, n -> new SVECTOR().set(inst.particlePosition_50));
      //LAB_80101cdc
      //LAB_80101d04
    }
    //LAB_80101d1c
  }

  @Method(0x80101d3cL)
  public static void FUN_80101d3c(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner, final int flags) {
    effect.countParticleSub_54 = 0;
    effect.subParticleType_60 = 1;

    if((flags & 0xf_ff00) == 0xf_ff00) {
      //TODO I added the first deref here, this might have been a retail bug...
      //     Looking at how _800c6944 gets set, I don't see how it could have been correct
      effect.tmd_30 = tmds_800c6944[flags & 0xff];
      effect.tpage_56 = 0x20;
    } else {
      //LAB_80101d98
      final DeffPart.TmdType tmdType = (DeffPart.TmdType)getDeffPart(0x300_0000 | flags & 0xf_ffff);
      final CContainer extTmd = tmdType.tmd_0c;
      final TmdWithId tmd = extTmd.tmdPtr_00;
      effect.tmd_30 = tmd.tmd.objTable[0];
      effect.tpage_56 = (int)((tmd.id & 0xffff_0000L) >>> 11);
    }

    //LAB_80101dd8
    if((effect.effectInner_08.particleInnerStuff_1c & 0x6000_0000L) != 0) {
      effect.countParticleSub_54 = (int)_800fb794.offset((effect.effectInner_08.particleInnerStuff_1c & 0x6000_0000L) >>> 27).get();

      //LAB_80101e3c
      for(int i = 0; i < effect.countParticleInstance_50; i++) {
        effect.particleArray_68[i].subParticlePositionsArray_44 = new SVECTOR[effect.countParticleSub_54];
        Arrays.setAll(effect.particleArray_68[i].subParticlePositionsArray_44, n -> new SVECTOR());
      }
    }
    //LAB_80101e6c
  }

  @Method(0x80101e84L)
  public static void FUN_80101e84(final ParticleEffectData98 effect, final ParticleEffectInstance94 particle, final ParticleEffectData98Inner24 effectInner, final int flags) {
    effect.subParticleType_60 = 0;
    effect.countParticleSub_54 = 0;

    if((effect.effectInner_08.particleInnerStuff_1c & 0x6000_0000) != 0) {
      final long v0 = (effect.effectInner_08.particleInnerStuff_1c & 0x6000_0000) >>> 27;
      effect.countParticleSub_54 = (int)_800fb794.offset(2, v0).get();

      //LAB_80101f2c
      for(int i = 0; i < effect.countParticleInstance_50; i++) {
        final ParticleEffectInstance94 inst = effect.particleArray_68[i];
        //TODO why is a GP0 packet started here but not used?
        //        final long v1 = gpuPacketAddr_1f8003d8.get();
        //        gpuPacketAddr_1f8003d8.addu(0x28L);
        //        MEMORY.ref(1, v1).offset(0x3L).setu(0x9L);
        //        MEMORY.ref(4, v1).offset(0x4L).setu(0x2c80_8080L);
        inst.particleInstanceSubArray_80 = new ParticleEffectInstance94Sub10[effect.countParticleSub_54];
        Arrays.setAll(inst.particleInstanceSubArray_80, n -> new ParticleEffectInstance94Sub10());
      }
    }

    //LAB_80101f70
    if((flags & 0xf_ff00) == 0xf_ff00) {
      final SpriteMetrics08 metrics = spriteMetrics_800c6948[(flags & 0xff)];
      effect.u_58 = metrics.u_00;
      effect.v_5a = metrics.v_02;
      effect.w_5e = metrics.w_04;
      effect.h_5f = metrics.h_05;
      effect.clut_5c = metrics.clut_06;
    } else {
      //LAB_80101fec
      final DeffPart.SpriteType spriteType = (DeffPart.SpriteType)getDeffPart(flags | 0x400_0000);
      final DeffPart.SpriteMetrics deffMetrics = spriteType.metrics_08;
      effect.u_58 = deffMetrics.u_00;
      effect.v_5a = deffMetrics.v_02;
      effect.w_5e = deffMetrics.w_04 * 4;
      effect.h_5f = deffMetrics.h_06;
      effect.clut_5c = GetClut(deffMetrics.clutX_08, deffMetrics.clutY_0a);
    }

    //LAB_80102048
    effect.halfW_34 = (short)(effect.w_5e >>> 1);
    effect.halfH_36 = (short)(effect.h_5f >>> 1);
  }

  @Method(0x80102088L)
  public static FlowControl allocateParticleEffect(final RunningScript<? extends BattleScriptDataBase> script) {
    final int particleCount = script.params_20[3].get();

    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      "Particle effect %x".formatted(script.params_20[2].get()),
      script.scriptState_04,
      null,
      particleEffectRenderers_80119b7c[script.params_20[2].get() >> 20],
      SEffe::particleEffectDestructor,
      new ParticleEffectData98(particleCount)
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    final ParticleEffectData98 effect = (ParticleEffectData98)manager.effect_44;

    if(firstParticle_8011a00c == null) {
      firstParticle_8011a00c = effect;
    }

    //LAB_801021a8
    if(lastParticle_8011a010 != null) {
      lastParticle_8011a010.next_94 = effect;
    }

    lastParticle_8011a010 = effect;

    //LAB_801021c0
    effect.myState_00 = state;
    effect.parentScriptIndex_04 = script.params_20[1].get();
    effect.prerenderCallback_84 = prerenderCallbacks_80119bac[script.params_20[8].get()];
    effect.callback_88 = _80119cb0[script.params_20[8].get()];
    effect.countFramesRendered_52 = 0;
    effect.halfW_34 = 0;
    effect.halfH_36 = 0;
    effect.scaleOrUseEffectAcceleration_6c = false;
    effect.next_94 = null;
    effect.initializerCallback_8c = initializerCallbacks_80119db4[script.params_20[8].get()];
    script.params_20[0].set(state.index);

    //LAB_8010223c
    effect.effectInner_08.scriptIndex_00 = script.params_20[0].get();
    effect.effectInner_08.parentScriptIndex_04 = script.params_20[1].get();
    effect.effectInner_08.particleTypeId_08 = script.params_20[2].get();
    effect.effectInner_08.totalCountParticleInstance_0c = (short)script.params_20[3].get();
    effect.effectInner_08._10 = (short)script.params_20[4].get();
    effect.effectInner_08._14 = (short)script.params_20[5].get();
    effect.effectInner_08._18 = (short)script.params_20[6].get();
    effect.effectInner_08.particleInnerStuff_1c = script.params_20[7].get();
    effect.effectInner_08.callbackIndex_20 = (short)script.params_20[8].get();

    //LAB_80102278
    for(int i = 0; i < effect.countParticleInstance_50; i++) {
      final ParticleEffectInstance94 particle = effect.particleArray_68[i];
      currentParticleIndex_8011a008.set(i);
      initializeParticleInstance(manager, effect, particle, effect.effectInner_08);
      particle.particlePositionCopy1.set(particle.particlePosition_50);
    }

    //final EffectData98Sub94 s2_0 = effect._68[effect.count_50]; // This looks like a retail bug - index out of bounds

    //LAB_801022b4
    if(effect.callback90Type_61 != 0) {
      effect.callback_90 = SEffe::FUN_801012d4;
    } else {
      //LAB_801022cc
      effect.callback_90 = SEffe::FUN_801012a0;
    }

    //LAB_801022d4
    effect.countParticleSub_54 = 0;
    subParticleInitializers_80119b94[script.params_20[2].get() >> 20].accept(effect, null/*s2_0 see above*/, effect.effectInner_08, script.params_20[2].get());
    manager._10.flags_00 |= 0x5000_0000;
    manager.flags_04 |= 0x4_0000;
    return FlowControl.CONTINUE;
  }

  @Method(0x80102364L)
  public static FlowControl FUN_80102364(final RunningScript<?> script) {
    final ParticleEffectData98 effect = (ParticleEffectData98)((EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00).effect_44;

    final int a2 = script.params_20[0].get();
    if(a2 == 0) {
      effect.scaleOrUseEffectAcceleration_6c = true;
      effect.effectAcceleration_70.set(script.params_20[2].get(), script.params_20[3].get(), script.params_20[4].get());
      effect.scaleParticleAcceleration_80 = script.params_20[5].get();
      //LAB_801023d0
    } else if(a2 == 1) {
      effect.scaleOrUseEffectAcceleration_6c = false;
      //LAB_801023e0
    } else if(a2 == 2) {
      //LAB_801023e8
      effect.scaleParticleAcceleration_80 = script.params_20[5].get();
    }

    //LAB_801023ec
    return FlowControl.CONTINUE;
  }

  @Method(0x801023f4L)
  public static FlowControl FUN_801023f4(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @Method(0x801023fcL)
  public static FlowControl FUN_801023fc(final RunningScript<?> script) {
    final ParticleEffectData98 effect = (ParticleEffectData98)((EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00).effect_44;

    //LAB_8010243c
    for(int i = 0; i < effect.countParticleInstance_50; i++) {
      final ParticleEffectInstance94 particle = effect.particleArray_68[i];
      script.params_20[1].array(i).set(particle.flags_90 & 1);
    }

    //LAB_80102464
    return FlowControl.CONTINUE;
  }

  @Method(0x8010246cL)
  public static FlowControl FUN_8010246c(final RunningScript<?> script) {
    final ParticleEffectData98 effect = (ParticleEffectData98)((EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00).effect_44;
    final ParticleEffectInstance94 particle = effect.particleArray_68[script.params_20[1].get()];

    final VECTOR sp0x20 = new VECTOR();
    FUN_800cf684(particle.managerRotation_68, particle.managerTranslation_2c, new VECTOR().set(particle.particlePosition_50), sp0x20);
    script.params_20[2].set(sp0x20.getX());
    script.params_20[3].set(sp0x20.getY());
    script.params_20[4].set(sp0x20.getZ());
    return FlowControl.CONTINUE;
  }

  @Method(0x80102608L)
  public static FlowControl FUN_80102608(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x80102610L)
  public static FlowControl FUN_80102610(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @Method(0x80102618L)
  public static void modifyLightningSegmentOriginsBySecondaryScriptTranslation(final EffectManagerData6c manager, final ElectricityEffect38 electricEffect, final LightningBoltEffect14 boltEffect) {
    if(electricEffect.scriptIndex_08 != -1) {
      final VECTOR secondaryScriptTranslation = new VECTOR();
      final VECTOR newOrigin = new VECTOR();
      scriptGetScriptedObjectPos(electricEffect.scriptIndex_08, secondaryScriptTranslation);
      secondaryScriptTranslation.sub(manager._10.trans_04).div(electricEffect.boltSegmentCount_28);

      //LAB_801026f0
      for(int i = 0; i < electricEffect.boltSegmentCount_28; i++) {
        final LightningBoltEffectSegment30 boltSegment = boltEffect.boltSegments_10[i];
        boltSegment.origin_00.set(newOrigin);

        newOrigin.x.add((int)((seed_800fa754.advance().get() % 257 - 128) * electricEffect.segmentOriginTranslationModifier_26 >>> 7));
        newOrigin.z.add((int)((seed_800fa754.advance().get() % 257 - 128) * electricEffect.segmentOriginTranslationModifier_26 >>> 7));
        newOrigin.add(secondaryScriptTranslation);
      }
    }
    //LAB_80102848
  }

  @Method(0x80102860L)
  public static void initializeRadialElectricityBoltColour(final EffectManagerData6c manager, final ElectricityEffect38 electricEffect) {
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

      outerColourR = manager._10.colour_1c.getX() << 8;
      outerColourG = manager._10.colour_1c.getY() << 8;
      outerColourB = manager._10.colour_1c.getZ() << 8;

      final int managerR = manager._10.colour_1c.getX();
      int managerG = manager._10.colour_1c.getY();
      int managerB = manager._10.colour_1c.getZ();

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
  public static void initializeElectricityNodes(final EffectManagerData6c manager, final ElectricityEffect38 electricEffect, final LightningBoltEffect14 bolt) {
    short segmentOriginX = 0;
    final int segmentOriginY = -(short)(electricEffect.boltAngleRangeCutoff_1c / electricEffect.boltSegmentCount_28);
    short segmentOriginZ = 0;

    //LAB_80102c58
    for(int i = 0; i < electricEffect.boltSegmentCount_28; i++) {
      final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[i];
      segment.origin_00.set(segmentOriginX, segmentOriginY * i, segmentOriginZ);
      segment.scaleMultiplier_28 = (int)(seed_800fa754.advance().get() % 7 + 5);
      segment.unused_2a = 0;
      segment.originTranslationMagnitude_2c = segmentOriginX >> 4;
      segment.baseVertexTranslationScale_2e = (int)(seed_800fa754.advance().get() % 193 + 64);

      if(electricEffect.addSuccessiveSegmentOriginTranslations_14) {
        segmentOriginX += (seed_800fa754.advance().get() % 257 - 128) * electricEffect.segmentOriginTranslationModifier_26 >>> 7;
        segmentOriginZ += (seed_800fa754.advance().get() % 257 - 128) * electricEffect.segmentOriginTranslationModifier_26 >>> 7;
        //LAB_80102e58
      } else if(i < electricEffect.boltSegmentCount_28 - 2) {
        segmentOriginX = (short)((seed_800fa754.advance().get() % 257 - 128) * electricEffect.segmentOriginTranslationModifier_26 >>> 7);
        segmentOriginZ = (short)((seed_800fa754.advance().get() % 257 - 128) * electricEffect.segmentOriginTranslationModifier_26 >>> 7);
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
  public static void renderSegmentGradient(final USCOLOUR colour1, final USCOLOUR colour2, final DVECTOR[] xy, final int a3, final int a4, final Translucency translucency) {
    final GpuCommandPoly cmd = new GpuCommandPoly(4)
      .translucent(translucency)
      .pos(0, xy[0].getX(), xy[0].getY())
      .pos(1, xy[1].getX(), xy[1].getY())
      .pos(2, xy[2].getX(), xy[2].getY())
      .pos(3, xy[3].getX(), xy[3].getY())
      .monochrome(0, 0)
      .rgb(1, colour2.getX() >>> 8, colour2.getY() >>> 8, colour2.getZ() >>> 8)
      .monochrome(2, 0)
      .rgb(3, colour1.getX() >>> 8, colour1.getY() >>> 8, colour1.getZ() >>> 8);

    GPU.queueCommand(a3 + a4 >> 2, cmd);
  }

  /**
   * Renders certain types of lightning effects. (Confirmed for Rose's D transformation)
   * Used by allocator 0x801052dc
   */
  @Method(0x801030d8L)
  public static void renderElectricEffectType0(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
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

    final boolean effectShouldRender = (manager._10._24 >> electricEffect.frameNum_2a & 0x1) == 0;

    final DVECTOR[] vertexArray = new DVECTOR[4];
    Arrays.setAll(vertexArray, i -> new DVECTOR());

    final ShortRef refOuterOriginXa = new ShortRef();
    final ShortRef refOuterOriginYa = new ShortRef();
    final ShortRef lastSegmentRefX = new ShortRef();
    final ShortRef lastSegmentRefY = new ShortRef();
    final ShortRef refOuterOriginXb = new ShortRef();
    final ShortRef refOuterOriginYb = new ShortRef();
    final VECTOR segmentOrigin = new VECTOR();

    //LAB_80103200
    //LAB_8010322c
    for(int boltNum = 0; boltNum < electricEffect.boltCount_00; boltNum++) {
      final LightningBoltEffect14 bolt = electricEffect.bolts_34[boltNum];

      if(electricEffect.reinitializeNodes_24) {
        initializeElectricityNodes(manager, electricEffect, bolt);
      }

      //LAB_8010324c
      electricEffect.callback_2c.accept(manager, electricEffect, bolt, boltNum);
      bolt.angle_02 -= electricEffect.boltAngleStep_10 << 7 >> 8;
      segmentOrigin.set(bolt.boltSegments_10[0].origin_00);
      int zMod = FUN_800cfb94(manager, bolt.rotation_04, segmentOrigin, refOuterOriginXa, refOuterOriginYa) >> 2;
      segmentOrigin.set(bolt.boltSegments_10[electricEffect.boltSegmentCount_28 - 1].origin_00);
      FUN_800cfb94(manager, bolt.rotation_04, segmentOrigin, lastSegmentRefX, lastSegmentRefY);
      final int boltLengthX = lastSegmentRefX.get() - refOuterOriginXa.get() << 8;
      final int boltLengthY = lastSegmentRefY.get() - refOuterOriginYa.get() << 8;
      final int segmentLengthX = boltLengthX / (electricEffect.boltSegmentCount_28 - 1);
      final int segmentLengthY = boltLengthY / (electricEffect.boltSegmentCount_28 - 1);
      int previousOriginX = refOuterOriginXa.get() << 8;
      int centerLineOriginX = refOuterOriginXa.get() << 8;
      int previousOriginY = refOuterOriginYa.get() << 8;
      int centerLineOriginY = refOuterOriginYa.get() << 8;
      final short firstSegmentScaleX = (short)(bolt.boltSegments_10[0].scaleMultiplier_28 * manager._10.scale_16.getX() >> 12);
      final int angle = -ratan2(boltLengthX, boltLengthY);
      final short outerXOffset = (short)(rcos(angle) * firstSegmentScaleX >> 12);
      final short outerYOffset = (short)(rsin(angle) * firstSegmentScaleX >> 12);
      refOuterOriginXb.set((short)(refOuterOriginXa.get() - outerXOffset));
      refOuterOriginYb.set((short)(refOuterOriginYa.get() - outerYOffset));
      refOuterOriginXa.add(outerXOffset);
      refOuterOriginYa.add(outerYOffset);

      //LAB_80103488
      for(int segmentNum = 0; segmentNum < electricEffect.boltSegmentCount_28; segmentNum++) {
        final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[segmentNum];
        segment.innerColour_10.sub(segment.innerColourFadeStep_1c);
        segment.outerColour_16.sub(segment.outerColourFadeStep_22);
        segment.unused_2a += electricEffect.boltAngleStep_10 << 8 >> 8;
      }

      //LAB_80103538
      if(effectShouldRender) {
        final int z = manager._10.z_22 + zMod;
        if(z >= 0xa0) {
          if(z >= 0xffe) {
            zMod = 0xffe - manager._10.z_22;
          }

          final Translucency translucency = Translucency.of(manager._10.flags_00 >>> 28 & 3);

          //LAB_80103574
          //LAB_80103594
          for(int segmentNum = 0; segmentNum < electricEffect.boltSegmentCount_28 - 1; segmentNum++) {
            final LightningBoltEffectSegment30 currentSegment = bolt.boltSegments_10[segmentNum];
            final LightningBoltEffectSegment30 nextSegment = bolt.boltSegments_10[segmentNum + 1];
            int centerLineEndpointX = centerLineOriginX + segmentLengthX;
            int centerLineEndpointY = centerLineOriginY + segmentLengthY;
            final int currentSegmentEndpointX = centerLineOriginX + segmentLengthX;
            final int currentSegmentEndpointY = centerLineOriginY + segmentLengthY;
            centerLineOriginX += rcos(angle) * currentSegment.originTranslationMagnitude_2c >> 12 << 8;
            centerLineOriginY += rsin(angle) * currentSegment.originTranslationMagnitude_2c >> 12 << 8;
            centerLineEndpointX += rcos(angle) * nextSegment.originTranslationMagnitude_2c >> 12 << 8;
            centerLineEndpointY += rsin(angle) * nextSegment.originTranslationMagnitude_2c >> 12 << 8;
            final int scale = currentSegment.scaleMultiplier_28 * manager._10.scale_16.getX() >> 12;
            final int outerEndpointXa = (centerLineEndpointX >> 8) + (rcos(angle) * scale >> 12);
            final int outerEndpointYa = (centerLineEndpointY >> 8) + (rsin(angle) * scale >> 12);
            final int outerEndpointXb = (centerLineEndpointX >> 8) - (rcos(angle) * scale >> 12);
            final int outerEndpointYb = (centerLineEndpointY >> 8) - (rsin(angle) * scale >> 12);

            if(electricEffect.hasMonochromeBase_29) {
              final int baseX0 = (previousOriginX - centerLineOriginX >> 8) * currentSegment.baseVertexTranslationScale_2e + centerLineOriginX;
              final int baseY0 = (previousOriginY - centerLineOriginY >> 8) * currentSegment.baseVertexTranslationScale_2e + centerLineOriginY;
              final int baseX2 = (centerLineEndpointX - centerLineOriginX >> 8) * currentSegment.baseVertexTranslationScale_2e + centerLineOriginX;
              final int baseY2 = (centerLineEndpointY - centerLineOriginY >> 8) * currentSegment.baseVertexTranslationScale_2e + centerLineOriginY;

              //LAB_80103808
              int baseColour = currentSegment.innerColour_10.getX() + Math.abs(currentSegment.originTranslationMagnitude_2c - nextSegment.originTranslationMagnitude_2c) * 8;
              if((baseColour & 0xffff) > 0xff00) {
                baseColour = 0xff00;
              }

              //LAB_80103834
              final GpuCommandPoly cmd = new GpuCommandPoly(3)
                .translucent(translucency)
                .pos(0, baseX0 >> 8, baseY0 >> 8)
                .pos(1, centerLineOriginX >> 8, centerLineOriginY >> 8)
                .pos(2, baseX2 >> 8, baseY2 >> 8)
                .monochrome(0, baseColour >>> 9)
                .monochrome(1, baseColour >>> 8)
                .monochrome(2, baseColour >>> 9);

              GPU.queueCommand(manager._10.z_22 + zMod >> 2, cmd);
            }

            //LAB_80103994
            vertexArray[1].setX((short)(centerLineEndpointX >> 8));
            vertexArray[1].setY((short)(centerLineEndpointY >> 8));
            vertexArray[3].setX((short)(centerLineOriginX >> 8));
            vertexArray[3].setY((short)(centerLineOriginY >> 8));

            vertexArray[0].setX((short)outerEndpointXa);
            vertexArray[0].setY((short)outerEndpointYa);
            vertexArray[2].setX(refOuterOriginXa.get());
            vertexArray[2].setY(refOuterOriginYa.get());
            renderSegmentGradient(currentSegment.outerColour_16, nextSegment.outerColour_16, vertexArray, zMod, manager._10.z_22, translucency);

            vertexArray[0].setX((short)((vertexArray[0].getX() - vertexArray[1].getX()) / manager._10._30 + vertexArray[1].getX()));
            vertexArray[0].setY((short)((vertexArray[0].getY() - vertexArray[1].getY()) / manager._10._30 + vertexArray[1].getY()));
            vertexArray[2].setX((short)((vertexArray[2].getX() - vertexArray[3].getX()) / manager._10._30 + vertexArray[3].getX()));
            vertexArray[2].setY((short)((vertexArray[2].getY() - vertexArray[3].getY()) / manager._10._30 + vertexArray[3].getY()));
            renderSegmentGradient(currentSegment.innerColour_10, nextSegment.innerColour_10, vertexArray, zMod, manager._10.z_22, translucency);

            vertexArray[0].setX((short)outerEndpointXb);
            vertexArray[0].setY((short)outerEndpointYb);
            vertexArray[2].setX(refOuterOriginXb.get());
            vertexArray[2].setY(refOuterOriginYb.get());
            renderSegmentGradient(currentSegment.outerColour_16, nextSegment.outerColour_16, vertexArray, zMod, manager._10.z_22, translucency);

            vertexArray[0].setX((short)((vertexArray[0].getX() - vertexArray[1].getX()) / manager._10._30 + vertexArray[1].getX()));
            vertexArray[0].setY((short)((vertexArray[0].getY() - vertexArray[1].getY()) / manager._10._30 + vertexArray[1].getY()));
            vertexArray[2].setX((short)((vertexArray[2].getX() - vertexArray[3].getX()) / manager._10._30 + vertexArray[3].getX()));
            vertexArray[2].setY((short)((vertexArray[2].getY() - vertexArray[3].getY()) / manager._10._30 + vertexArray[3].getY()));
            renderSegmentGradient(currentSegment.innerColour_10, nextSegment.innerColour_10, vertexArray, zMod, manager._10.z_22, translucency);

            refOuterOriginXa.set((short)outerEndpointXa);
            refOuterOriginYa.set((short)outerEndpointYa);
            refOuterOriginXb.set((short)outerEndpointXb);
            refOuterOriginYb.set((short)outerEndpointYb);
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
  public static void renderElectricEffectType1(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
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
      final boolean effectShouldRender = (manager._10._24 >> electricEffect.frameNum_2a & 0x1) == 0;

      //LAB_80103f18
      //LAB_80103f44
      for(int i = 0; i < electricEffect.boltCount_00; i++) {
        final LightningBoltEffect14 bolt = electricEffect.bolts_34[i];

        if(electricEffect.reinitializeNodes_24) {
          initializeElectricityNodes(manager, electricEffect, bolt);
        }

        //LAB_80103f6c
        electricEffect.callback_2c.accept(manager, electricEffect, bolt, i);

        bolt.angle_02 -= electricEffect.boltAngleStep_10 << 7 >> 8;

        //LAB_80103fc4
        int segmentNum;
        for(segmentNum = 0; segmentNum < electricEffect.boltSegmentCount_28; segmentNum++) {
          final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[segmentNum];
          final LightningBoltEffectSegmentOrigin08 segmentOrigin = segmentArray[segmentNum];
          final ShortRef refX = new ShortRef();
          final ShortRef refY = new ShortRef();
          bolt.sz3_0c = FUN_800cfb94(manager, bolt.rotation_04, new VECTOR().set(segment.origin_00), refX, refY) / 4;
          segmentOrigin.x_00 = refX.get();
          segmentOrigin.y_04 = refY.get();
          segment.innerColour_10.sub(segment.innerColourFadeStep_1c);
          segment.outerColour_16.sub(segment.outerColourFadeStep_22);
          segment.unused_2a += electricEffect.boltAngleStep_10 << 8 >> 8;
        }

        //LAB_801040d0
        final int boltLengthX = segmentArray[segmentNum - 1].x_00 - segmentArray[0].x_00;
        final int boltLengthY = segmentArray[segmentNum - 1].y_04 - segmentArray[0].y_04;
        final int angle = -ratan2(boltLengthX, boltLengthY);
        int currentSegmentScale = bolt.boltSegments_10[0].scaleMultiplier_28 * manager._10.scale_16.getX() >> 12;
        int outerOriginXa = segmentArray[0].x_00 + (rcos(angle) * currentSegmentScale >> 12);
        int outerOriginYa = segmentArray[0].y_04 + (rsin(angle) * currentSegmentScale >> 12);
        int outerOriginXb = segmentArray[0].x_00 - (rcos(angle) * currentSegmentScale >> 12);
        int outerOriginYb = segmentArray[0].y_04 - (rsin(angle) * currentSegmentScale >> 12);

        if(effectShouldRender) {
          final Translucency translucency = Translucency.of(manager._10.flags_00 >>> 28 & 3);

          final int z = manager._10.z_22 + bolt.sz3_0c;
          if(z >= 0xa0) {
            if(z >= 0xffe) {
              bolt.sz3_0c = 0xffe - manager._10.z_22;
            }

            //LAB_8010422c
            //LAB_8010424c
            for(segmentNum = 0; segmentNum < electricEffect.boltSegmentCount_28 - 1; segmentNum++) {
              final LightningBoltEffectSegment30 currentSegment = bolt.boltSegments_10[segmentNum];
              final LightningBoltEffectSegment30 nextSegment = bolt.boltSegments_10[segmentNum + 1];
              currentSegmentScale = currentSegment.scaleMultiplier_28 * manager._10.scale_16.getX() >> 12;
              final int nextSegmentScale = nextSegment.scaleMultiplier_28 * manager._10.scale_16.getX() >> 12;

              if(electricEffect.type1RendererType_18) {
                currentSegmentOrigin = segmentArray[segmentNum];
                nextSegmentOrigin = segmentArray[segmentNum + 1];
                final int outerEndpointXa = nextSegmentOrigin.x_00 + (rcos(angle) * currentSegmentScale >> 12);
                final int outerEndpointYa = nextSegmentOrigin.y_04 + (rsin(angle) * currentSegmentScale >> 12);
                final int outerEndpointXb = nextSegmentOrigin.x_00 - (rcos(angle) * currentSegmentScale >> 12);
                final int outerEndpointYb = nextSegmentOrigin.y_04 - (rsin(angle) * currentSegmentScale >> 12);

                final DVECTOR[] vertexArray = new DVECTOR[4];
                Arrays.setAll(vertexArray, n -> new DVECTOR());

                vertexArray[1].setX((short)nextSegmentOrigin.x_00);
                vertexArray[1].setY((short)nextSegmentOrigin.y_04);
                vertexArray[3].setX((short)currentSegmentOrigin.x_00);
                vertexArray[3].setY((short)currentSegmentOrigin.y_04);

                vertexArray[0].setX((short)outerEndpointXa);
                vertexArray[0].setY((short)outerEndpointYa);
                vertexArray[2].setX((short)outerOriginXa);
                vertexArray[2].setY((short)outerOriginYa);
                renderSegmentGradient(currentSegment.outerColour_16, nextSegment.outerColour_16, vertexArray, bolt.sz3_0c, manager._10.z_22, translucency);

                vertexArray[0].setX((short)((vertexArray[0].getX() - vertexArray[1].getX()) / manager._10._30 + vertexArray[1].getX()));
                vertexArray[0].setY((short)((vertexArray[0].getY() - vertexArray[1].getY()) / manager._10._30 + vertexArray[1].getY()));
                vertexArray[2].setX((short)((vertexArray[2].getX() - vertexArray[3].getX()) / manager._10._30 + vertexArray[3].getX()));
                vertexArray[2].setY((short)((vertexArray[2].getY() - vertexArray[3].getY()) / manager._10._30 + vertexArray[3].getY()));
                renderSegmentGradient(currentSegment.innerColour_10, nextSegment.innerColour_10, vertexArray, bolt.sz3_0c, manager._10.z_22, translucency);

                vertexArray[0].setX((short)outerEndpointXb);
                vertexArray[0].setY((short)outerEndpointYb);
                vertexArray[2].setX((short)outerOriginXb);
                vertexArray[2].setY((short)outerOriginYb);
                renderSegmentGradient(currentSegment.outerColour_16, nextSegment.outerColour_16, vertexArray, bolt.sz3_0c, manager._10.z_22, translucency);

                vertexArray[0].setX((short)((vertexArray[0].getX() - vertexArray[1].getX()) / manager._10._30 + vertexArray[1].getX()));
                vertexArray[0].setY((short)((vertexArray[0].getY() - vertexArray[1].getY()) / manager._10._30 + vertexArray[1].getY()));
                vertexArray[2].setX((short)((vertexArray[2].getX() - vertexArray[3].getX()) / manager._10._30 + vertexArray[3].getX()));
                vertexArray[2].setY((short)((vertexArray[2].getY() - vertexArray[3].getY()) / manager._10._30 + vertexArray[3].getY()));
                renderSegmentGradient(currentSegment.innerColour_10, nextSegment.innerColour_10, vertexArray, bolt.sz3_0c, manager._10.z_22, translucency);

                outerOriginXa = outerEndpointXa;
                outerOriginYa = outerEndpointYa;
                outerOriginXb = outerEndpointXb;
                outerOriginYb = outerEndpointYb;
              } else {
                //LAB_801045e8
                currentSegmentOrigin = segmentArray[segmentNum];
                nextSegmentOrigin = segmentArray[segmentNum + 1];
                int centerLineEndpointX = Math.abs(currentSegmentOrigin.x_00 - nextSegmentOrigin.x_00);
                centerLineEndpointX = (int)(seed_800fa754.advance().get() % (centerLineEndpointX * 2 + 1) - centerLineEndpointX + currentSegmentOrigin.x_00);
                int centerLineOriginX = currentSegmentOrigin.x_00;
                int centerLineOriginY = currentSegmentOrigin.y_04;
                int centerLineEndpointY = (nextSegmentOrigin.y_04 - currentSegmentOrigin.y_04) / 2 + currentSegmentOrigin.y_04;
                final int nextSegmentQuarterScale = nextSegmentScale >> 2;
                final int currentSegmentQuarterScale = currentSegmentScale >> 2;

                //LAB_801046c4
                for(int j = 2; j > 0; j--) {
                  final DVECTOR[] vertexArray = new DVECTOR[4];
                  Arrays.setAll(vertexArray, n -> new DVECTOR());

                  vertexArray[0].setY((short)centerLineEndpointY);
                  vertexArray[1].setY((short)centerLineEndpointY);
                  vertexArray[2].setY((short)centerLineOriginY);
                  vertexArray[3].setY((short)centerLineOriginY);

                  vertexArray[0].setX((short)(centerLineEndpointX - nextSegmentScale));
                  vertexArray[1].setX((short)(centerLineEndpointX + 1));
                  vertexArray[2].setX((short)(centerLineOriginX - currentSegmentScale));
                  vertexArray[3].setX((short)(centerLineOriginX + 1));
                  renderSegmentGradient(currentSegment.outerColour_16, nextSegment.outerColour_16, vertexArray, bolt.sz3_0c, manager._10.z_22, translucency);
                  vertexArray[0].setX((short)(centerLineEndpointX - nextSegmentQuarterScale));
                  vertexArray[2].setX((short)(centerLineOriginX - currentSegmentQuarterScale));
                  renderSegmentGradient(currentSegment.innerColour_10, nextSegment.innerColour_10, vertexArray, bolt.sz3_0c, manager._10.z_22, translucency);
                  vertexArray[0].setX((short)(centerLineEndpointX + nextSegmentScale));
                  vertexArray[1].setX((short)centerLineEndpointX);
                  vertexArray[2].setX((short)(centerLineOriginX + currentSegmentScale));
                  vertexArray[3].setX((short)centerLineOriginX);
                  renderSegmentGradient(currentSegment.outerColour_16, nextSegment.outerColour_16, vertexArray, bolt.sz3_0c, manager._10.z_22, translucency);
                  vertexArray[0].setX((short)(centerLineEndpointX + nextSegmentQuarterScale));
                  vertexArray[2].setX((short)(centerLineOriginX + currentSegmentQuarterScale));
                  renderSegmentGradient(currentSegment.innerColour_10, nextSegment.innerColour_10, vertexArray, bolt.sz3_0c, manager._10.z_22, translucency);

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
  public static void FUN_801049d4(final EffectManagerData6c a0, final ElectricityEffect38 a1, final LightningBoltEffect14 a2, final int a3) {
    // no-op
  }

  @Method(0x801049dcL)
  public static void FUN_801049dc(final EffectManagerData6c a0, final ElectricityEffect38 electricEffect, final LightningBoltEffect14 boltEffect, final int boltIndex) {
    boltEffect.rotation_04.setY((short)(0x1000 / electricEffect.boltCount_00 * boltIndex));
    boltEffect.rotation_04.setZ((short)(electricEffect.segmentOriginTranslationMagnitude_1e * 2));
  }

  @Method(0x80104a14L)
  public static void FUN_80104a14(final EffectManagerData6c a0, final ElectricityEffect38 a1, final LightningBoltEffect14 bolt, final int a3) {
    bolt.rotation_04.setX((short)(seed_800fa754.advance().get() % 4097));
    bolt.rotation_04.setY((short)(seed_800fa754.advance().get() % 4097));
    bolt.rotation_04.setZ((short)(seed_800fa754.advance().get() % 4097));
  }

  @Method(0x80104b10L)
  public static void FUN_80104b10(final EffectManagerData6c manager, final ElectricityEffect38 electricEffect, final LightningBoltEffect14 bolt, final int a3) {
    final int angleStep = manager._10._28 << 8 >> 8;
    int angle = bolt.angle_02;

    //LAB_80104b58
    for(int i = 0; i < electricEffect.boltSegmentCount_28; i++) {
      final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[i];
      segment.origin_00.x.add(rcos(angle) * electricEffect.segmentOriginTranslationMagnitude_1e >> 12);
      segment.origin_00.z.add(rsin(angle) * electricEffect.segmentOriginTranslationMagnitude_1e >> 12);
      angle = angle + angleStep;
    }

    //LAB_80104bcc
  }

  @Method(0x80104becL)
  public static void FUN_80104bec(final EffectManagerData6c manager, final ElectricityEffect38 electricEffect, final LightningBoltEffect14 bolt, final int a3) {
    final int angleStep = manager._10._28 << 8 >> 8;
    int angle = bolt.angle_02;

    //LAB_80104c34
    for(int i = 0; i < electricEffect.boltSegmentCount_28; i++) {
      final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[i];
      segment.origin_00.z.add(rsin(angle) * electricEffect.segmentOriginTranslationMagnitude_1e >> 12);
      angle = angle + angleStep;
    }

    //LAB_80104c7c
  }

  @Method(0x80104c9cL)
  public static void FUN_80104c9c(final EffectManagerData6c manager, final ElectricityEffect38 electricEffect, final LightningBoltEffect14 bolt, final int a3) {
    int translationMagnitudeModifier = 0;
    int segmentIndex = (electricEffect.boltSegmentCount_28 - 2) / 2;
    final int angle0 = (manager._10._28 & 0xff) << 4;
    final int angle1 = (manager._10._28 & 0xff00) >>> 4;
    final int angle2 = manager._10._28 >>> 12 & 0xff0;

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
  public static void FUN_80104e40(final EffectManagerData6c manager, final ElectricityEffect38 electricEffect, final LightningBoltEffect14 bolt, final int a3) {
    final int translationMagnitude = electricEffect.segmentOriginTranslationMagnitude_1e / electricEffect.boltCount_00 & 0xffff;
    final int angle = 0x1000 / electricEffect.boltCount_00 * a3;

    //LAB_80104ec4
    short originTranslationX = 0;
    short originTranslationY = 0;
    for(int i = 1; i < electricEffect.boltSegmentCount_28; i++) {
      final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[i];
      segment.origin_00.x.add(originTranslationX);
      segment.origin_00.z.add(originTranslationY);
      originTranslationX += rcos(angle) * translationMagnitude >> 12;
      originTranslationY += rsin(angle) * translationMagnitude >> 12;
    }
  }

  @Method(0x80104f70L)
  public static void FUN_80104f70(final EffectManagerData6c manager, final ElectricityEffect38 effect, final LightningBoltEffect14 bolt, final int a3) {
    final int angleStep = 0x1000 / (effect.boltSegmentCount_28 - 1);
    int angle = 0;

    //LAB_80104fb8
    for(int i = 0; i < effect.boltSegmentCount_28; i++) {
      final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[i];
      segment.origin_00.x.add(rsin(angle) * effect.segmentOriginTranslationMagnitude_1e >> 12);
      segment.origin_00.z.add(rcos(angle) * effect.segmentOriginTranslationMagnitude_1e >> 12);
      segment.origin_00.y.set(0);
      angle += angleStep;
    }
  }

  @Method(0x80105050L)
  public static void FUN_80105050(final EffectManagerData6c manager, final ElectricityEffect38 electricEffect, final LightningBoltEffect14 bolt, final int boltAngleModifier) {
    final int segmentCount = electricEffect.boltSegmentCount_28;
    final int angleStep = 0x800 / (segmentCount - 1);
    final int boltAngleZ = 0x1000 / electricEffect.boltCount_00 * boltAngleModifier;
    int angle = 0;

    //LAB_801050c0
    for(int i = 0; i < segmentCount; i++) {
      final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[i];
      segment.origin_00.x.add(rcos(angle) * electricEffect.segmentOriginTranslationMagnitude_1e >> 12);
      segment.origin_00.y.set((rsin(angle) * rsin(angle) >> 12) * electricEffect.segmentOriginTranslationMagnitude_1e >> 12);
      segment.origin_00.z.add((rsin(angle) * rcos(boltAngleZ) >> 12) * electricEffect.segmentOriginTranslationMagnitude_1e >> 12);
      angle += angleStep;
    }
  }

  @Method(0x801051acL)
  public static void FUN_801051ac(final EffectManagerData6c manager, final ElectricityEffect38 electricEffect, final LightningBoltEffect14 bolt, final int a3) {
    final int segmentCount = electricEffect.boltSegmentCount_28;
    final int angleStepXZ = 0x1000 / (segmentCount - 1);
    final int angleStepY = manager._10._28 << 8 >> 8;
    int angleY = bolt.angle_02;
    int angleXZ = 0;

    //LAB_80105210
    for(int i = 0; i < segmentCount; i++) {
      final LightningBoltEffectSegment30 segment = bolt.boltSegments_10[i];
      segment.origin_00.x.add(rsin(angleXZ) * electricEffect.segmentOriginTranslationMagnitude_1e >> 12);
      segment.origin_00.y.set(rsin(angleY) * electricEffect.segmentOriginTranslationMagnitude_1e / 4 >> 12);
      segment.origin_00.z.add(rcos(angleXZ) * electricEffect.segmentOriginTranslationMagnitude_1e >> 12);
      angleXZ = angleXZ + angleStepXZ;
      angleY = angleY + angleStepY;
    }
  }

  @Method(0x801052dcL)
  public static FlowControl allocateElectricityEffect(final RunningScript<? extends BattleScriptDataBase> script) {
    final int effectFlag = script.params_20[6].get();
    final int callbackIndex = script.params_20[7].get();
    final int boltCount = script.params_20[3].get();
    final int boltSegmentCount = effectFlag >> 8 & 0xff;

    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      "ElectricityEffect38",
      script.scriptState_04,
      null,
      electricityEffectRenderers_80119f14[callbackIndex],
      null,
      new ElectricityEffect38(boltCount, boltSegmentCount)
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    final ElectricityEffect38 electricEffect = (ElectricityEffect38)manager.effect_44;
    electricEffect.currentColourFadeStep_04 = 0;
    electricEffect.scriptIndex_08 = script.params_20[1].get();
    electricEffect.numColourFadeSteps_0c = effectFlag >> 16 & 0xff;
    electricEffect.boltAngleStep_10 = script.params_20[5].get();
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
      boltEffect.angle_02 = (short)seed_800fa754.advance().get() % 4097;
      boltEffect.rotation_04.set((short)0, (short)0, (short)0);
      // Ran callback here from method array _80119ebc, which was filled with copies of the same no-op method FUN_801052d4
      initializeElectricityNodes(manager, electricEffect, boltEffect);
    }

    //LAB_80105590
    manager._10.flags_00 |= 0x5000_0000;
    manager._10.colour_1c.set(0, 0, 0xff);
    manager._10._28 = 0x100;
    manager._10._30 = 3;

    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x80105604L)
  public static FlowControl FUN_80105604(final RunningScript<?> script) {
    final EffectManagerData6c a0 = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final LightningBoltEffect14 a1 = ((ElectricityEffect38)a0.effect_44).bolts_34[script.params_20[1].get()];
    final LightningBoltEffectSegment30 v0 = a1.boltSegments_10[script.params_20[2].get()];

    final VECTOR sp0x10 = new VECTOR().set(v0.origin_00);
    final VECTOR sp0x20 = new VECTOR();
    FUN_800cf4f4(a0, a1.rotation_04, sp0x10, sp0x20);
    script.params_20[3].set(sp0x20.getX());
    script.params_20[4].set(sp0x20.getY());
    script.params_20[5].set(sp0x20.getZ());
    return FlowControl.CONTINUE;
  }

  @Method(0x80105704L)
  public static void FUN_80105704(final ScriptState<ThunderArrowEffect1c> state, final ThunderArrowEffect1c data) {
    final DVECTOR[] sp0x18 = new DVECTOR[4];
    Arrays.setAll(sp0x18, n -> new DVECTOR());

    //LAB_8010575c
    final Translucency translucency = Translucency.of(data._10 >>> 28 & 3);

    for(int s7 = 0; s7 < data.count_00; s7++) {
      //LAB_8010577c
      for(int s6 = 0; s6 < data.count_0c - 1; s6++) {
        final ThunderArrowEffectBolt1e s4_1 = data._18[s7][s6];
        final ThunderArrowEffectBolt1e s4_2 = data._18[s7][s6 + 1];

        sp0x18[0].setX((short)(s4_2.x_00 - s4_2._1c));
        sp0x18[0].setY(s4_2.y_02);
        sp0x18[1].setX((short)(s4_2.x_00 + 1));
        sp0x18[1].setY(s4_2.y_02);
        sp0x18[2].setX((short)(s4_1.x_00 - s4_1._1c));
        sp0x18[2].setY(s4_1.y_02);
        sp0x18[3].setX((short)(s4_1.x_00 + 1));
        sp0x18[3].setY(s4_1.y_02);
        renderSegmentGradient(s4_1.colour_0a, s4_2.colour_0a, sp0x18, data.z_14, data._08, translucency);
        sp0x18[0].setX((short)(s4_2.x_00 - s4_2._1c / 3));
        sp0x18[2].setX((short)(s4_1.x_00 - s4_1._1c / 3));
        renderSegmentGradient(s4_1.colour_04, s4_2.colour_04, sp0x18, data.z_14, data._08, translucency);
        sp0x18[0].setX((short)(s4_2.x_00 + s4_2._1c));
        sp0x18[1].setX(s4_2.x_00);
        sp0x18[2].setX((short)(s4_1.x_00 + s4_1._1c));
        sp0x18[3].setX(s4_1.x_00);
        renderSegmentGradient(s4_1.colour_0a, s4_2.colour_0a, sp0x18, data.z_14, data._08, translucency);
        sp0x18[0].setX((short)(s4_2.x_00 + s4_2._1c / 3));
        sp0x18[2].setX((short)(s4_1.x_00 + s4_1._1c / 3));
        renderSegmentGradient(s4_1.colour_04, s4_2.colour_04, sp0x18, data.z_14, data._08, translucency);
      }
    }

    //LAB_801059c8
  }

  @Method(0x80105aa0L)
  public static void FUN_80105aa0(final ScriptState<ThunderArrowEffect1c> state, final ThunderArrowEffect1c data) {
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

  @Method(0x80105c38L)
  public static FlowControl FUN_80105c38(final RunningScript<?> script) {
    final VECTOR sp0x18 = new VECTOR();
    final ShortRef refX = new ShortRef();
    final ShortRef refY = new ShortRef();

    final int scriptIndex = script.params_20[0].get();
    final int s3 = script.params_20[1].get();
    final EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00;
    final ElectricityEffect38 s1 = (ElectricityEffect38)manager.effect_44;
    final ScriptState<ThunderArrowEffect1c> state = SCRIPTS.allocateScriptState("ThunderArrowEffect1c", new ThunderArrowEffect1c());
    state.loadScriptFile(doNothingScript_8004f650);
    state.setTicker(SEffe::FUN_80105aa0);
    state.setRenderer(SEffe::FUN_80105704);
    final ThunderArrowEffect1c effect = state.innerStruct_00;
    effect.count_00 = s1.boltCount_00;
    effect._04 = s3;
    effect.count_0c = s1.boltSegmentCount_28;
    effect._10 = manager._10.flags_00;
    effect._18 = new ThunderArrowEffectBolt1e[effect.count_00][];

    //LAB_80105d64
    for(int s7 = 0; s7 < effect.count_00; s7++) {
      final LightningBoltEffect14 struct14 = s1.bolts_34[s7];
      effect._18[s7] = new ThunderArrowEffectBolt1e[effect.count_0c];

      //LAB_80105da0
      for(int s4 = 0; s4 < effect.count_0c; s4++) {
        final ThunderArrowEffectBolt1e struct1e = new ThunderArrowEffectBolt1e();
        effect._18[s7][s4] = struct1e;

        final LightningBoltEffectSegment30 s0 = struct14.boltSegments_10[s4];
        struct1e._1c = (byte)(s0.scaleMultiplier_28 * manager._10.scale_16.getX() >> 12);

        sp0x18.set(s0.origin_00);
        final int z = FUN_800cfb94(manager, struct14.rotation_04, sp0x18, refX, refY) >> 2;
        effect.z_14 = z;
        if(z < 0x140) {
          effect._04 = 0;
        } else {
          //LAB_80105e18
          if(effect.z_14 >= 0xffe) {
            effect.z_14 = 0xffe;
          }

          //LAB_80105e30
          effect._08 = manager._10.z_22;
          struct1e.x_00 = refX.get();
          struct1e.y_02 = refY.get();
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
  public static void getBobjTranslation(final int scriptIndex, final VECTOR out, final long coordType) {
    final MATRIX transformationMatrix = new MATRIX();

    final BattleObject27c bobj = (BattleObject27c)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00;

    final GsCOORDINATE2 coord2;
    if(coordType == 0) {
      coord2 = bobj.model_148.coord2ArrPtr_04[1];
    } else {
      //LAB_80105fe4
      coord2 = bobj.model_148.coord2_14;
    }

    //LAB_80105fec
    GsGetLw(coord2, transformationMatrix);
    // Does nothing? Changed line below to set //ApplyMatrixLV(transformationMatrix, zeroVec, out);
    out.set(transformationMatrix.transfer);
  }

  /** Runs callbacks to render correct button icon effects during addition */
  @Method(0x80106050L)
  public static void renderAdditionButton(final long a0, final boolean isCounter) {
    final Consumer<RunningScript<?>> callback = Bttl_800d::scriptRenderButtonPressHudElement;

    // Params: ?, x, y, translucency, colour
    final int offset = isCounter ? 1 : 0;
    if(Math.abs((byte)a0) >= 2) {
      callScriptFunction(callback, 0x24, 119, 43, Translucency.B_PLUS_F.ordinal(), 0x80);
      callScriptFunction(callback, (int)_800fb7bc.offset(1, 0x0L).offset(offset).get(), 115, 48, 0x1, 0x80);
    } else {
      //LAB_80106114
      callScriptFunction(callback, 0x24, 119, 51, Translucency.B_PLUS_F.ordinal(), 0x80);
      callScriptFunction(callback, (int)_800fb7bc.offset(1, 0x2L).offset(offset).get(), 115, 48, Translucency.B_PLUS_F.ordinal(), 0x80);
      callScriptFunction(callback, 0x25, 115, 50, Translucency.B_PLUS_F.ordinal(), 0x80);
    }
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
      hitPropertyValue = (int)_800fb7c0.offset(hitNum * 0x10L).offset(1, hitPropertyIndex).get();
    } else {
      //LAB_8010628c
      hitPropertyValue = (Bttl_800c.getHitProperty(charSlot, hitNum, hitPropertyIndex) & 0xff);
    }

    //LAB_80106298
    return hitPropertyValue;
  }

  @Method(0x801062a8L)
  public static void initializeAdditionOverlaysEffect(final int attackerScriptIndex, final int targetScriptIndex, final AdditionOverlaysEffect44 effect, final int autoCompleteType) {
    final BattleObject27c s5 = (BattleObject27c)scriptStatePtrArr_800bc1c0[attackerScriptIndex].innerStruct_00;

    //LAB_8010633c
    int hitNum;
    for(hitNum = 0; hitNum < 8; hitNum++) {
      // Number of hits calculated by counting to first hit with 0 total frames
      if((getHitProperty(s5.charSlot_276, hitNum, 1, autoCompleteType) & 0xffL) == 0) {
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
      seed_800fa754.advance();
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
        additionBorderColours_800fb7f0.get(6).set(counterRgb & 0xff);
        additionBorderColours_800fb7f0.get(7).set(counterRgb >> 8 & 0xff);
        additionBorderColours_800fb7f0.get(8).set(counterRgb >> 16 & 0xff);
        additionBorderColours_800fb7f0.get(9).set(additionRgb & 0xff);
        additionBorderColours_800fb7f0.get(10).set(additionRgb >> 8 & 0xff);
        additionBorderColours_800fb7f0.get(11).set(additionRgb >> 16 & 0xff);
      }

      int val = 16;
      for(int borderNum = 0; borderNum < 17; borderNum++) {
        final AdditionOverlaysBorder0e borderOverlay = borderArray[borderNum];
        borderOverlay.size_08 = (short)((0x12 - val) * 0xa);
        borderOverlay.isVisible_00 = true;
        //LAB_8010656c
        //LAB_80106574
        borderOverlay.angleModifier_02 = (short)((0x10 - val) * 0x80 + 0x200);
        borderOverlay.countFramesVisible_0c = 5;
        borderOverlay.sideEffects_0d = 0;
        borderOverlay.framesUntilRender_0a = (short)((hitOverlay.frameSuccessLowerBound_10 + (hitOverlay.numSuccessFrames_0e - 0x1) / 2 + val - 0x11));
        borderOverlay.r_04 = additionBorderColours_800fb7f0.get(hitOverlay.borderColoursArrayIndex_02 * 3).get()& 0xff;
        borderOverlay.g_05 = additionBorderColours_800fb7f0.get(hitOverlay.borderColoursArrayIndex_02 * 3 + 1).get() & 0xff;
        borderOverlay.b_06 = additionBorderColours_800fb7f0.get(hitOverlay.borderColoursArrayIndex_02 * 3 + 2).get() & 0xff;

        val--;
      }

      //LAB_80106634
      val = 0;
      for(int borderNum = 16; borderNum >= 14; borderNum--) {
        final AdditionOverlaysBorder0e borderOverlay = borderArray[borderNum];
        borderOverlay.size_08 = (short)(0x14 - val * 0x2);
        borderOverlay.angleModifier_02 = (short)0x200;
        borderOverlay.countFramesVisible_0c = 0x11;
        borderOverlay.framesUntilRender_0a = (short)(hitOverlay.frameSuccessLowerBound_10 - 0x11);

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
    getBobjTranslation(effect.attackerScriptIndex_00, effect.attackerStartingPosition_10, 0);

    final VECTOR targetStartingPosition = new VECTOR();
    getBobjTranslation(effect.targetScriptIndex_04, targetStartingPosition, 1);

    final int firstHitSuccessLowerBound = effect.hitOverlays_40[0].frameSuccessLowerBound_10;
    effect.distancePerFrame_20.setX((targetStartingPosition.getX() - effect.attackerStartingPosition_10.getX()) / firstHitSuccessLowerBound);
    effect.distancePerFrame_20.setY((targetStartingPosition.getY() - effect.attackerStartingPosition_10.getY()) / firstHitSuccessLowerBound);
    effect.distancePerFrame_20.setZ((targetStartingPosition.getZ() - effect.attackerStartingPosition_10.getZ()) / firstHitSuccessLowerBound);
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
  public static void renderAdditionCentreSolidSquare(final Effect a0, final AdditionOverlaysHit20 hitOverlay, final int completionState, final ScriptState<EffectManagerData6c> a3, final EffectManagerData6c effect) {
    if(effect._10.flags_00 >= 0) {
      final AdditionOverlaysBorder0e[] targetBorderArray = hitOverlay.borderArray_18;

      //LAB_8010685c
      for(int targetBorderNum = 0; targetBorderNum < 2; targetBorderNum++) {
        final int squareSize = targetBorderArray[16].size_08 - targetBorderNum * 8;

        //LAB_80106874
        final int[] vertexCoords = new int[8];
        for(int i = 0; i < 4; i++) {
          vertexCoords[i * 2] = rcos(targetBorderArray[16].angleModifier_02 + i * 0x400) * squareSize >> 12;
          vertexCoords[i * 2 + 1] = (rsin(targetBorderArray[16].angleModifier_02 + i * 0x400) * squareSize >> 12) + 30;
        }

        final GpuCommandPoly cmd = new GpuCommandPoly(4);

        if(completionState == 1) {  // Success
          cmd.monochrome(0xff);
          //LAB_80106918
        } else if(completionState != -2) {  // Too early
          //LAB_80106988
          cmd.monochrome(0x30);
        } else if(hitOverlay.isCounter_1c) {  // Counter-attack too late
          if(Config.changeAdditionOverlayRgb()) {
            cmd.rgb(additionBorderColours_800fb7f0.get(6).get(), additionBorderColours_800fb7f0.get(7).get(), (additionBorderColours_800fb7f0.get(8).get() * 8 - 2) * 8);
          } else {
            cmd.rgb(targetBorderArray[15].r_04 * 3, targetBorderArray[15].g_05, (targetBorderArray[15].b_06 - 1) * 8);
          }
        } else {  // Too late
          //LAB_80106964
          cmd.rgb(targetBorderArray[15].r_04, targetBorderArray[15].g_05, targetBorderArray[15].b_06);
        }

        //LAB_80106994
        cmd
          .translucent(Translucency.B_PLUS_F)
          .pos(0, vertexCoords[0], vertexCoords[1])
          .pos(1, vertexCoords[2], vertexCoords[3])
          .pos(2, vertexCoords[6], vertexCoords[5])
          .pos(3, vertexCoords[4], vertexCoords[7]);
        GPU.queueCommand(30, cmd);
      }
    }
    //LAB_80106a4c
  }

  /** Renders the shadow on the inside of the innermost rotating border. */
  @Method(0x80106ac4L)
  public static void renderAdditionBorderShadow(final AdditionOverlaysHit20 hitOverlay, final int angle0, final int borderSize) {
    final int angle1 = angle0 + 0x400;
    final int offset0 = borderSize - 1;
    final int offset1 = borderSize - 11;
    final int x0 = rcos(angle0) * offset0 >> 12;
    final int x1 = rcos(angle1) * offset0 >> 12;
    final int x2 = rcos(angle0) * offset1 >> 12;
    final int x3 = rcos(angle1) * offset1 >> 12;
    final int y0 = rsin(angle0) * offset0 >> 12;
    final int y1 = rsin(angle1) * offset0 >> 12;
    final int y2 = rsin(angle0) * offset1 >> 12;
    final int y3 = rsin(angle1) * offset1 >> 12;
    final int colour = hitOverlay.shadowColour_08 * 4;

    final GpuCommandPoly cmd = new GpuCommandPoly(4)
      .translucent(Translucency.B_MINUS_F)
      .monochrome(0, colour)
      .monochrome(1, colour)
      .monochrome(2, 0)
      .monochrome(3, 0)
      .pos(0, x0, y0 + 30)
      .pos(1, x1, y1 + 30)
      .pos(2, x2, y2 + 30)
      .pos(3, x3, y3 + 30);

    GPU.queueCommand(31, cmd);
  }

  @Method(0x80106cccL)
  public static void renderAdditionBorders(final int a0, final int hitNum, final AdditionOverlaysEffect44 effect, final AdditionOverlaysHit20[] hitArray, final ScriptState<EffectManagerData6c> state) {
    final AdditionOverlaysBorder0e[] borderArray = hitArray[hitNum].borderArray_18;
    final byte currentHitCompletionState = additionHitCompletionState_8011a014[hitNum];

    //LAB_80106d18
    for(int borderNum = 0; borderNum < 17; borderNum++) {
      final AdditionOverlaysBorder0e borderOverlay = borderArray[borderNum];
      if(borderOverlay.isVisible_00) {
        if(borderOverlay.framesUntilRender_0a <= 0) {
          final int borderSize = borderOverlay.size_08;
          int x0 = rcos(borderOverlay.angleModifier_02) * borderSize >> 12;
          int y0 = (rsin(borderOverlay.angleModifier_02) * borderSize >> 12) + 30;

          //LAB_80106d80
          int angleModifier = 0;
          for(long lineNum = 0; lineNum < 4; lineNum++) {
            final GpuCommandLine cmd = new GpuCommandLine();

            final int sideEffects = borderOverlay.sideEffects_0d;

            //LAB_80106dc0
            // Set translucent if button press is failure and border sideEffects_0d not innermost rotating border or target (15)
            if(sideEffects != 0 && sideEffects != -1 || currentHitCompletionState < 0) {
              //LAB_80106de8
              cmd.translucent(Translucency.B_PLUS_F);
            }


            if(hitArray[hitNum].isCounter_1c && borderNum != 0x10) {
              if(Config.changeAdditionOverlayRgb()) {
                final int rgb = Config.getCounterOverlayRgb();

                // Hack to get around lack of separate counterattack color field until full dememulation
                final float rFactor = borderArray[borderNum].r_04 / (float)additionBorderColours_800fb7f0.get(9).get();
                final float gFactor = borderArray[borderNum].g_05 / (float)additionBorderColours_800fb7f0.get(10).get();
                final float bFactor = borderArray[borderNum].b_06 / (float)additionBorderColours_800fb7f0.get(11).get();

                cmd.rgb(Math.round((rgb & 0xff) * rFactor), Math.round((rgb >> 8 & 0xff) * gFactor), Math.round((rgb >> 16 & 0xff) * bFactor));
              } else {
                cmd.rgb(borderOverlay.r_04 * 3, borderOverlay.g_05, (borderOverlay.b_06 + 1) / 8);
              }
            } else {
              //LAB_80106e58
              cmd.rgb(borderOverlay.r_04, borderOverlay.g_05, borderOverlay.b_06);
            }

            //LAB_80106e74
            angleModifier += 0x400;
            final int x1 = rcos(borderOverlay.angleModifier_02 + angleModifier) * borderSize >> 12;
            final int y1 = (rsin(borderOverlay.angleModifier_02 + angleModifier) * borderSize >> 12) + 30;
            cmd
              .pos(0, x0, y0)
              .pos(1, x1, y1);

            GPU.queueCommand(30, cmd);

            x0 = x1;
            y0 = y1;
          }

          // Renders rotating shadow on innermost rotating border
          if(borderOverlay.sideEffects_0d == 0) {
            renderAdditionBorderShadow(hitArray[hitNum], borderOverlay.angleModifier_02 + angleModifier, borderSize);
            renderAdditionBorderShadow(hitArray[hitNum], borderOverlay.angleModifier_02 + angleModifier + 0x400, borderSize);
            renderAdditionBorderShadow(hitArray[hitNum], borderOverlay.angleModifier_02 + angleModifier + 0x800, borderSize);
            renderAdditionBorderShadow(hitArray[hitNum], borderOverlay.angleModifier_02 + angleModifier + 0xc00, borderSize);
          }
        }
      }
      //LAB_80106fac
    }
  }

  @Method(0x80107088L)
  public static long tickBorderDisplay(final int a0, final int hitNum, final AdditionOverlaysEffect44 effect, final AdditionOverlaysHit20[] hitArray) {
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
  public static void renderAdditionOverlaysEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final AdditionOverlaysEffect44 effect = (AdditionOverlaysEffect44)data.effect_44;

    if(effect.pauseTickerAndRenderer_31 != 1) {
      if(data._10.flags_00 >= 0) {
        final AdditionOverlaysHit20[] hitArray = effect.hitOverlays_40;

        //LAB_801072c4
        int hitNum;
        for(hitNum = 0; hitNum < effect.count_30; hitNum++) {
          if(CONFIG.getConfig(CoreMod.ADDITION_OVERLAY_CONFIG.get()) == AdditionOverlayMode.FULL) {
            renderAdditionBorders(hitArray[hitNum].borderColoursArrayIndex_02, hitNum, effect, hitArray, state);
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
            renderAdditionButton((byte)(hitOverlay.frameSuccessLowerBound_10 + (hitOverlay.frameSuccessUpperBound_12 - hitOverlay.frameSuccessLowerBound_10) / 2 - effect.currentFrame_34 - 0x1L), hitOverlay.isCounter_1c);
          }

          final byte currentFrame = (byte)effect.currentFrame_34;
          if(currentFrame >= hitOverlay.frameSuccessLowerBound_10 && currentFrame <= hitOverlay.frameSuccessUpperBound_12) {
            if(CONFIG.getConfig(CoreMod.ADDITION_OVERLAY_CONFIG.get()) != AdditionOverlayMode.OFF) {
              renderAdditionCentreSolidSquare(effect, hitOverlay, -2, state, data);
            }
          }
        }
      }
    }
    //LAB_801073b4
  }

  @Method(0x801073d4L)
  public static void tickAdditionOverlaysEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
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
                final int buttonPressed = press_800bee94.get();

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
              renderAdditionCentreSolidSquare(effect, effect.hitOverlays_40[effect.lastCompletedHit_39],
                additionHitCompletionState_8011a014[effect.lastCompletedHit_39], state, data);
            }
          }
        }
      }
    }
    //LAB_80107764
  }

  @Method(0x801077bcL)
  public static FlowControl scriptGetHitCompletionState(final RunningScript<?> script) {
    script.params_20[2].set(additionHitCompletionState_8011a014[script.params_20[1].get()]);
    return FlowControl.CONTINUE;
  }

  @Method(0x801077e8L)
  public static FlowControl allocateAdditionOverlaysEffect(final RunningScript<? extends BattleScriptDataBase> script) {
    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      "Addition overlays",
      script.scriptState_04,
      SEffe::tickAdditionOverlaysEffect,
      SEffe::renderAdditionOverlaysEffect,
      null,
      new AdditionOverlaysEffect44()
    );

    initializeAdditionOverlaysEffect(script.params_20[0].get(), script.params_20[1].get(), (AdditionOverlaysEffect44)state.innerStruct_00.effect_44, script.params_20[2].get());
    state.storage_44[8] = 0; // Storage for counterattack state
    script.params_20[4].set(state.index);
    additionOverlayActive_80119f41 = 1;
    return FlowControl.CONTINUE;
  }

  /**
   * Script subfunc related to pausing ticking/rendering of addition overlay during counterattacks. v1 == 0 occurs when
   * counterattack counter is successful; have not gotten other conditions to trigger
   */
  @Method(0x801078c0L)
  public static FlowControl scriptAlterAdditionContinuationState(final RunningScript<?> script) {
    final EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
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

  /** Param 0 is 0 for addition, 1 for dragoon addition */
  @Method(0x801079a4L)
  public static FlowControl scriptGetAdditionOverlayActiveStatus(final RunningScript<?> script) {
    if(script.params_20[0].get() == 0) {
      script.params_20[1].set(additionOverlayActive_80119f41);
    } else {
      //LAB_801079d0
      script.params_20[1].set(daddyOverlayActive_80119f42);
    }

    //LAB_801079e0
    return FlowControl.CONTINUE;
  }

  @Method(0x801079e8L)
  public static void FUN_801079e8(final DragoonAdditionScriptData1c a0) {
    final int s4 = (int)_8011a01c.get();
    final int s3 = (int)_8011a020.get();
    final int fp = s4 + 16;
    final int s7 = s3 + 70;
    final Consumer<RunningScript<?>> func = Bttl_800d::scriptRenderButtonPressHudElement;

    final int a2;
    if(FUN_80108460(a0, 0) != 0) {
      callScriptFunction(func, 36, fp, s3 + 64, 1, 128);
      a2 = 33;
    } else {
      //LAB_80107a80
      if(FUN_80108460(a0, 2) != 0) {
        callScriptFunction(func, 36, fp, s3 + 60, 1, 128);
        a2 = 33;
      } else {
        //LAB_80107ad4
        callScriptFunction(func, 36, fp, s3 + 56, 1, 128);
        a2 = 35;
      }
    }

    //LAB_80107b10
    callScriptFunction(func, a2, s4 + 12, s3 + 66, 1, 128);

    if(a0._11 != 0) {
      final int colour = a0._11 * 0x40 - 1;
      final COLOUR rgb = new COLOUR().set(colour, colour, colour);
      callScriptFunction(func, 20, fp - 4, s7 - 4, 1, 128);
      renderButtonPressHudElement((short)fp - 2, (short)s7 - 5, 232, 120, 255, 143, 0xc, Translucency.B_PLUS_F, rgb, a0._11 * 256 + 6404, a0._11 * 256 + 4096);
    }

    //LAB_80107bf0

    final int s1 = Math.min(4, a0._07);

    //LAB_80107c08
    callScriptFunction(func, s1 + 4, fp + 36, s7,      1, 128);
    callScriptFunction(func, 24,     fp -  4, s7,      1, 128);
    callScriptFunction(func, 26,     fp -  4, s7 + 12, 1, 128);
    callScriptFunction(func, 30,     fp -  4, s7 +  4, 1, 128);
    callScriptFunction(func, 31,     fp + 48, s7 +  4, 1, 128);

    //LAB_80107cd4
    for(int i = 0; i < 48; i += 8) {
      callScriptFunction(func, 28, fp + i, s7,      1, 128);
      callScriptFunction(func, 29, fp + i, s7 + 12, 1, 128);
      callScriptFunction(func, 32, fp + i, s7 -  4, 1, 128);
    }

    callScriptFunction(func, 25, fp + 48, s7     , 1, 128);
    callScriptFunction(func, 27, fp + 48, s7 + 12, 1, 128);
  }

  @Method(0x80107dc4L)
  public static void FUN_80107dc4(final DragoonAdditionScriptData1c a0, final int a1, final int angle) {
    final int colour = (int)((_80119fb4.get() + 1) * 0x40);
    final COLOUR rgb = new COLOUR().set(colour, colour, colour);

    final int y = (short)_8011a020.get() + (rsin(angle) * 17 >> 12) + 24;

    final int x;
    if(_8011a028.deref(4).offset(a0._06 * 0x4L).get() >= 2) {
      x = (short)_8011a01c.get() + (rcos(angle) * 17 >> 12) + 28;
    } else {
      x = (short)_8011a01c.get() + 28;
    }

    //LAB_80108048
    renderButtonPressHudTexturedRect((short)x, (short)y, 128, 64, 16, 16, 51, Translucency.B_PLUS_F, rgb, 0x1000);

    rgb.set(0x80, 0x80, 0x80);

    //LAB_801080ac
    for(int i = 0; i < 5; i++) {
      if(a0._07 < i) {
        rgb.set(0x10, 0x10, 0x10);
      }

      //LAB_801080cc
      renderButtonPressHudTexturedRect((short)_8011a01c.get() + (short)_800fb804.offset(2, i * 0x4L).getSigned(), (short)_8011a020.get() + (short)_800fb804.offset(2, i * 0x4L).offset(0x2L).getSigned(), (int)_800fb818.offset(1, i * 0x4L).getSigned(), (int)_800fb818.offset(1, i * 0x4L).offset(0x2L).getSigned(), (short)_800fb82c.offset(2, i * 0x4L).getSigned(), (short)_800fb82c.offset(2, i * 0x4L).offset(0x2L).getSigned(), 53 + i, Translucency.B_PLUS_F, rgb, 0x1000);
    }

    FUN_801079e8(a0);

    rgb.set(0x80, 0x80, 0x80);

    //LAB_801081a8
    for(int i = 0; i < a0._0d; i++) {
      renderButtonPressHudTexturedRect((short)_8011a01c.get() + 18, (short)_8011a020.get() + 16, 224, 208, 31, 31, (int)_800fb84c.offset(1, a0.charId_18).get(), Translucency.B_PLUS_F, rgb, 0x1000);

      if(a0.charId_18 == 9) {
        renderDivineDragoonAdditionPressIris((int)_8011a01c.getSigned() + 23, (int)_8011a020.getSigned() + 21, 232, 120, 23, 23, 12, Translucency.B_PLUS_F, rgb, 0x800, 0x1800);
      }
      //LAB_80108250
    }

    //LAB_80108268
    renderButtonPressHudTexturedRect((short)_8011a01c.get() + 32, (short)_8011a020.get() -  4, 152, 208,  8, 24, 50, Translucency.HALF_B_PLUS_HALF_F, rgb, 0x1000);
    renderButtonPressHudTexturedRect((short)_8011a01c.get() + 18, (short)_8011a020.get() + 16, 224, 208, 31, 31, (int)_800fb84c.offset(1, a0.charId_18).getSigned(), Translucency.of((int)_800fb7fc.offset(1, a1 * 2).getSigned()), rgb, 0x1000);
    renderButtonPressHudTexturedRect((short)_8011a01c.get() + 17, (short)_8011a020.get() + 14, 112, 200, 40, 40, 52, Translucency.of((int)_800fb7fc.offset(1, a1 * 2).offset(0x1L).getSigned()), rgb, 0x1000);
    renderButtonPressHudTexturedRect((short)_8011a01c.get(),      (short)_8011a020.get(),      160, 192, 64, 48, (int)_800fb840.offset(1, a0.charId_18).getSigned(), null, rgb, 0x1000);
    renderButtonPressHudTexturedRect((short)_8011a01c.get() +  8, (short)_8011a020.get() + 48, 200,  80, 42,  8, (int)_800fb840.offset(1, a0.charId_18).getSigned(), null, rgb, 0x1000);
    _80119fb4.setu(1 - _80119fb4.get());
  }

  @Method(0x80108460L)
  public static int FUN_80108460(final DragoonAdditionScriptData1c a0, final int a1) {
    int t4 = 0;
    int t1 = 0;
    long t0 = _8011a02c.get();
    long t2 = _8011a028.get();
    final int t3 = a0._04 - 1;

    //LAB_80108484
    for(int i = 0; i < 5; i++) {
      t1 += MEMORY.ref(4, t2).offset(0x0L).get();
      final int v1 = t1 - a1 - ((int)MEMORY.ref(4, t0).offset(0x0L).get() >> 1);
      //LAB_801084c4
      if((i & 0x1L) == 0 && t3 >= v1 + 1 || (i & 0x1L) != 0 && t3 >= v1) {
        //LAB_801084cc
        final int a2 = (int)MEMORY.ref(4, t0).offset(0x0L).get();
        if(a2 != 0 && t1 + (a2 >> 1) >= a0._04 - 1) {
          t4 = i + 1;
        }
      }

      //LAB_801084f8
      //LAB_801084fc
      t0 = t0 + 0x4L;
      t2 = t2 + 0x4L;
    }

    return t4;
  }

  @Method(0x80108514L)
  public static void FUN_80108514(final ScriptState<DragoonAdditionScriptData1c> state, final DragoonAdditionScriptData1c data) {
    FUN_80107dc4(data, 0, data._02 - 0x400);
  }

  @Method(0x80108574L)
  public static void FUN_80108574(final ScriptState<DragoonAdditionScriptData1c> state, final DragoonAdditionScriptData1c data) {
    if(data._0f == 0) {
      if(data._10 == 0) {
        data._12--;
        if(data._12 == 0) {
          state.deallocateWithChildren();
        } else if((press_800bee94.get() >>> 4 & 0x2) != 0 && data._13 != 2) {
          data._10 = 1;
          daddyOverlayActive_80119f42 = 1;
        }
      } else {
        //LAB_80108600
        if(data._11 != 0) {
          data._11--;
        }

        //LAB_80108614
        if(data._0e != 0) {
          data._0e--;

          if(data._0e == 0) {
            daddyOverlayActive_80119f42 = 0;

            //LAB_80108638
            state.deallocateWithChildren();
          }
        } else {
          //LAB_8010864c
          data._04++;
          data._02 += 0x1000 / (int)_8011a028.deref(4).offset(data._06 * 0x4L).get();

          if(data._06 + 1 << 12 < data._02) {
            data._06++;
          }

          //LAB_801086a8
          if(data._0d != 0) {
            data._0d--;
          }

          //LAB_801086bc
          //LAB_801086e0
          if(FUN_80108460(data, 0) != 0 && data._13 == 1 || (press_800bee94.get() >>> 4 & 0x2) != 0 && data._13 == 0 || (CONFIG.getConfig(CoreMod.AUTO_DRAGOON_ADDITION_CONFIG.get()) && FUN_80108460(data, 0) != 0)) {
            //LAB_8010870c
            data._11 = 4;
            data._0d = 0;

            final int v0 = FUN_80108460(data, 0);
            if(v0 != 0 || CONFIG.getConfig(CoreMod.AUTO_DRAGOON_ADDITION_CONFIG.get())) {
              data._07 = v0;
              data._0d = 4;
              _80119f40.setu(0);
            } else {
              //LAB_8010873c
              if(data._07 == 0) {
                _80119f40.setu(-1);
              } else {
                //LAB_8010875c
                _80119f40.setu(data._07);
              }

              //LAB_80108760
              data._0e = 1;
            }
          }

          //LAB_80108768
          if(data._07 < data._02 - 0x400 >> 12) {
            data._0d = 0;

            if(data._07 == 0) {
              _80119f40.setu(-1);
            } else {
              //LAB_801087a4
              if(data._07 == data._14) {
                FUN_80108cf4();
              }

              //LAB_801087bc
              _80119f40.setu(data._07);
            }

            //LAB_801087c8
            data._0e = 4;
          }

          //LAB_801087d0
          data._05 = data._02 & 0xff;
        }
      }
    }

    //LAB_801087dc
  }

  @Method(0x801087f0L)
  public static void doNothingScriptDestructor(final int index, final ScriptState<MemoryRef> state, final MemoryRef data) {
    // no-op
  }

  /**
   * Pretty sure this is the script allocation for dragoon additions
   *
   * {@link SEffe#FUN_80108514}
   * {@link SEffe#FUN_80108574}
   * {@link SEffe#doNothingScriptDestructor}
   */
  @Method(0x801087f8L)
  public static FlowControl allocateDragoonAdditionScript(final RunningScript<?> script) {
    final int s4 = script.params_20[1].get();
    final int s2 = script.params_20[0].get();

    final ScriptState<DragoonAdditionScriptData1c> state = SCRIPTS.allocateScriptState("Dragoon addition", new DragoonAdditionScriptData1c());
    state.loadScriptFile(doNothingScript_8004f650);
    state.setTicker(SEffe::FUN_80108574);
    state.setRenderer(SEffe::FUN_80108514);

    final DragoonAdditionScriptData1c s1 = state.innerStruct_00;
    s1._00 = 1;
    s1._02 = 0;
    s1._04 = 0;
    s1._05 = 0;
    s1._06 = 0;
    s1._07 = 0;
    s1._0d = 0;
    s1._0e = 0;
    s1._0f = s2 == 3 ? 1 : 0;
    s1._10 = 0;
    s1._11 = 0;
    s1._12 = script.params_20[2].get();
    s1._13 = s2 & 0xff;
    s1.charId_18 = s4;

    if(s2 == 1) {
      s1._10 = s2 & 0xff;
    }

    //LAB_80108910
    _8011a01c.setu(script.params_20[3].get());
    _8011a020.setu(script.params_20[4].get());

    //LAB_80108924
    for(int i = 0; i < 5; i++) {
      s1._08[i] = 0;
    }

    if(s4 == 7) {
      _8011a028.setu(_80119f7c.getAddress());
      _8011a02c.setu(_80119f98.getAddress());
      s1._14 = 3;
    } else {
      //LAB_80108964
      _8011a028.setu(_80119f44.getAddress());
      _8011a02c.setu(_80119f60.getAddress());
      s1._14 = 4;
    }

    //LAB_80108984
    _80119f40.setu(0);
    daddyOverlayActive_80119f42 = 0;
    _8011a024.setu(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x801089ccL)
  public static FlowControl FUN_801089cc(final RunningScript<?> script) {
    script.params_20[1].set((byte)_80119f40.getSigned());
    return FlowControl.CONTINUE;
  }

  @Method(0x801089e8L)
  public static void FUN_801089e8(final ScriptState<EffeScriptData30> state, final EffeScriptData30 data) {
    //LAB_80108a38
    for(int i = 7; i >= 0; i--) {
      final COLOUR rgb = new COLOUR().set(0x80, 0x80, 0x80);

      final long fp = _80119fbc.get(i).getAddress();
      final long s2 = _80119fc4.get(i).getAddress();

      final EffeScriptData30Sub06 struct = data._00[i];

      final int v1 = struct._00;
      if(v1 == 0) {
        //LAB_80108a78
        struct._02 -= 0x10;

        if(struct._02 < struct._04) {
          struct._02 = struct._04;
          struct._00++;
        }

        //LAB_80108aac
        //LAB_80108ac4
        int s0 = 0;
        do {
          rgb.r.sub(32);
          rgb.g.sub(32);
          rgb.b.sub(32);
          s0++;
          renderButtonPressHudTexturedRect(struct._02 + s0 * 6, (short)_8011a020.get() + 16, (int)MEMORY.ref(1, fp).offset(0x0L).get(), (int)MEMORY.ref(1, s2).offset(0x0L).get(), 8, 16, 41, Translucency.B_PLUS_F, rgb, 0x1000);
        } while(s0 < 4);
      } else if(v1 == 1) {
        //LAB_80108b58
        if(data._00[7]._00 == 1) {
          struct._00 = 2;
        }
        //LAB_80108a60
      } else if(v1 == 2) {
        //LAB_80108b84
        struct._01++;

        if(struct._01 >= 12 && i == 0) {
          data._00[7]._00++;
        }
      } else if(v1 == 3) {
        //LAB_80108bd0
        state.deallocateWithChildren();
        return;
      }

      //LAB_80108be8
      //LAB_80108bec
      //LAB_80108bf0
      renderButtonPressHudTexturedRect(struct._02, (short)_8011a020.get() + 16, (int)MEMORY.ref(1, fp).offset(0x0L).get(), (int)MEMORY.ref(1, s2).offset(0x0L).get(), 8, 16, 41, Translucency.B_PLUS_F, rgb, 0x1000);

      if((struct._01 & 0x1) != 0) {
        renderButtonPressHudTexturedRect(struct._02, (short)_8011a020.get() + 16, (int)MEMORY.ref(1, fp).offset(0x0L).get(), (int)MEMORY.ref(1, s2).offset(0x0L).get(), 8, 16, 41, Translucency.B_PLUS_F, rgb, 0x1000);
      }

      //LAB_80108cb0
    }

    //LAB_80108cc4
  }

  /**
   * {@link SEffe#FUN_801089e8}
   * {@link SEffe#doNothingScriptDestructor}
   */
  @Method(0x80108cf4L)
  public static void FUN_80108cf4() {
    playSound(0, 50, 0, 0, (short)0, (short)0);

    final ScriptState<EffeScriptData30> state = SCRIPTS.allocateScriptState("EffeScriptData30", new EffeScriptData30());
    state.loadScriptFile(doNothingScript_8004f650);
    state.setTicker(SEffe::FUN_801089e8);
    final EffeScriptData30 data = state.innerStruct_00;

    //LAB_80108d9c
    int s2 = (int)_8011a01c.get();
    int s3 = 130;
    for(int i = 0; i < 8; i++) {
      final EffeScriptData30Sub06 s1 = data._00[i];

      s2 = s2 + 8;
      s1._00 = 0;
      s1._01 = 0;
      s1._02 = s3;
      s1._04 = s2;
      s3 = s3 + 32;
    }
  }

  @Method(0x80108de8L)
  public static FlowControl FUN_80108de8(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x80108df0L)
  public static FlowControl FUN_80108df0(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x80108df8L)
  public static FlowControl FUN_80108df8(final RunningScript<? extends BattleScriptDataBase> script) {
    script.params_20[0].set(allocateEffectManager("Unknown (FUN_80108df8)", script.scriptState_04, null, null, null, null).index);
    return FlowControl.CONTINUE;
  }

  @Method(0x80108e40L)
  public static void renderRainEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final RainEffect08 effect = (RainEffect08)data.effect_44;
    final RaindropEffect0c[] rainArray = effect.raindropArray_04;

    //LAB_80108e84
    for(int i = 0; i < effect.count_00; i++) {
      if(Math.abs(Math.abs(rainArray[i].y0_04 + rainArray[i].x0_02) - Math.abs(rainArray[i].y1_08 + rainArray[i].x1_06)) <= 180) {
        GPU.queueCommand(30, new GpuCommandLine()
          .translucent(Translucency.of(data._10.flags_00 >>> 28 & 3))
          .monochrome(0, 0)
          .rgb(1, data._10.colour_1c.getX(), data._10.colour_1c.getY(), data._10.colour_1c.getZ())
          .pos(0, rainArray[i].x1_06 - 256, rainArray[i].y1_08 - 128)
          .pos(1, rainArray[i].x0_02 - 256, rainArray[i].y0_04 - 128)
        );
      }
      //LAB_80108f6c
    }
    //LAB_80108f84
  }

  @Method(0x80109000L)
  public static void tickRainEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final RainEffect08 effect = (RainEffect08)data.effect_44;
    final RaindropEffect0c[] rainArray = effect.raindropArray_04;

    //LAB_80109038
    for(int i = 0; i < effect.count_00; i++) {
      int endpointShiftX = rsin(data._10.rot_10.getX()) << 5 >> 12;
      int endpointShiftY = rcos(data._10.rot_10.getX()) << 5 >> 12;
      endpointShiftX = endpointShiftX * data._10.scale_16.getX() * rainArray[i].angleModifier_0a >> 24;
      endpointShiftY = endpointShiftY * data._10.scale_16.getX() * rainArray[i].angleModifier_0a >> 24;
      rainArray[i].x1_06 = rainArray[i].x0_02;
      rainArray[i].y1_08 = rainArray[i].y0_04;
      rainArray[i].x0_02 = (rainArray[i].x0_02 + endpointShiftX) & 0x1ff;
      rainArray[i].y0_04 = (rainArray[i].y0_04 + endpointShiftY) & 0xff;
    }
    //LAB_80109110
  }

  @Method(0x80109158L)
  public static FlowControl allocateRainEffect(final RunningScript<? extends BattleScriptDataBase> script) {
    final int count = script.params_20[1].get();
    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      "RainEffect08",
      script.scriptState_04,
      SEffe::tickRainEffect,
      SEffe::renderRainEffect,
      null,
      new RainEffect08(count)
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    final RainEffect08 effect = (RainEffect08)manager.effect_44;
    manager._10.flags_00 = 0x5000_0000;

    //LAB_80109204
    final RaindropEffect0c[] rainArray = effect.raindropArray_04;
    for(int i = 0; i < count; i++) {
      rainArray[i]._00 = 1;
      rainArray[i].x0_02 = (short)(seed_800fa754.advance().get() % 513);
      rainArray[i].y0_04 = (short)(seed_800fa754.advance().get() % 257);
      rainArray[i].angleModifier_0a = (short)(seed_800fa754.advance().get() % 3073 + 1024);
    }

    //LAB_80109328
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x80109358L)
  public static void FUN_80109358(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final ScreenDistortionEffectData08 sp48 = (ScreenDistortionEffectData08)data.effect_44;
    final int sp30 = data._10.scale_16.getX() >> 8;
    final int sp2c = data._10.scale_16.getY() >> 11;
    final int sp38 = data._10.scale_16.getZ() * 15 >> 9;

    //LAB_801093f0
    for(int s3 = 1; s3 >= -1; s3 -= 2) {
      final int angle = sp48.angle_00;
      int angle1 = angle;
      int angle2 = angle;
      int s5 = s3 == 1 ? 0 : -1;
      int sp40 = s3 == 1 ? 120 : 119;

      //LAB_80109430
      //LAB_8010944c
      while(s3 != 1 && s5 > -sp38 || s3 == 1 && s5 < sp38) {
        int s2 = (rsin(angle1) * sp2c >> 12) + 1 + sp2c;

        if(s2 == 0) {
          s2 = 1;
        }

        //LAB_8010949c
        //LAB_801094b8
        for(int s6 = 0; s6 < s2; s6++) {
          final int x = rsin(angle2) * sp30 >> 12;
          final int y = s5 + s6 * s3;

          GPU.queueCommand(30, new GpuCommandQuad()
            .bpp(Bpp.BITS_15)
            .translucent(Translucency.of(data._10.flags_00 >>> 28 & 3))
            .rgb(data._10.colour_1c.getX(), data._10.colour_1c.getY(), data._10.colour_1c.getZ())
            .pos(-160 - x, y, 320, 1)
            .uv(0, sp40)
            .texture(GPU.getDisplayBuffer())
          );

          angle2 += s3 * 32;
        }

        //LAB_80109678
        angle1 += s2 * 32;
        sp40 += s3;
        s5 += s2 * s3;
      }
    }
  }

  @Method(0x801097e0L)
  public static void renderScreenDistortionBlurEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    GPU.queueCommand(30, new GpuCommandQuad()
      .bpp(Bpp.BITS_15)
      .translucent(Translucency.of(data._10.flags_00 >>> 28 & 3))
      .rgb(data._10.colour_1c.getX(), data._10.colour_1c.getY(), data._10.colour_1c.getZ())
      .pos(-160, -120, 320, 240)
      .texture(GPU.getDisplayBuffer())
    );
  }

  @Method(0x80109a4cL)
  public static void FUN_80109a4c(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final ScreenDistortionEffectData08 effect = (ScreenDistortionEffectData08)data.effect_44;
    effect.angle_00 += effect.angleStep_04;
  }

  @Method(0x80109a6cL)
  public static void tickScreenDistortionBlurEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    // no-op
  }

  @Method(0x80109a7cL)
  public static FlowControl allocateScreenDistortionEffect(final RunningScript<? extends BattleScriptDataBase> script) {
    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      "Screen distortion",
      script.scriptState_04,
      // Ticker and renderer are swapped for some reason
      screenDistortionEffectRenderers_80119fd4[script.params_20[2].get()],
      screenDistortionEffectTickers_80119fe0[script.params_20[2].get()],
      null,
      new ScreenDistortionEffectData08()
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    final ScreenDistortionEffectData08 effect = (ScreenDistortionEffectData08)manager.effect_44;
    effect.angle_00 = 0x800;
    effect.angleStep_04 = script.params_20[1].get();
    manager._10.flags_00 = 0x4000_0000;
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
      final SVECTOR source = animation.sourceVertices_0c[i];
      final VECTOR current = animation.currentState_10[i];
      final VECTOR previous = animation.previousState_14[i];
      previous.add(current);
      current.setX(current.getX() + (current.getX() * animation.embiggener_04 >> 8));
      current.setY(current.getY() + (current.getY() * animation.embiggener_04 >> 8));
      current.setZ(current.getZ() + (current.getZ() * animation.embiggener_04 >> 8));
      source.setX((short)(previous.getX() >> 8));
      source.setY((short)(previous.getY() >> 8));
      source.setZ((short)(previous.getZ() >> 8));
    }

    //LAB_80109ce0
  }

  /** Kubila demon frog, Lloyd's cape, Selebus' strapple and zambo hands, Grand Jewel heal, etc. */
  @Method(0x80109d30L)
  public static FlowControl allocateVertexDifferenceAnimation(final RunningScript<?> script) {
    final int ticksRemaining = script.params_20[2].get();
    final int embiggener = script.params_20[3].get();

    final ScriptState<EffectManagerData6c> sourceState = (ScriptState<EffectManagerData6c>)scriptStatePtrArr_800bc1c0[script.params_20[0].get()];
    final ScriptState<EffectManagerData6c> diffState = (ScriptState<EffectManagerData6c>)scriptStatePtrArr_800bc1c0[script.params_20[1].get()];

    final ScriptState<VertexDifferenceAnimation18> state = SCRIPTS.allocateScriptState("Vertex difference animation source %d (%s), diff %d (%s)".formatted(sourceState.index, sourceState.name, diffState.index, diffState.name), new VertexDifferenceAnimation18());

    state.loadScriptFile(doNothingScript_8004f650);
    state.setTicker(SEffe::applyVertexDifferenceAnimation);
    final DeffTmdRenderer14 source = ((DeffTmdRenderer14)sourceState.innerStruct_00.effect_44);
    final DeffTmdRenderer14 diff = ((DeffTmdRenderer14)diffState.innerStruct_00.effect_44);
    final TmdObjTable1c sourceModel = source.tmd_08;
    final TmdObjTable1c diffModel = diff.tmd_08;
    final VertexDifferenceAnimation18 animation = state.innerStruct_00;
    animation.ticksRemaining_00 = ticksRemaining;
    animation.embiggener_04 = embiggener;
    animation.vertexCount_08 = sourceModel.n_vert_04;
    animation.sourceVertices_0c = sourceModel.vert_top_00;
    animation.currentState_10 = new VECTOR[sourceModel.n_vert_04];
    animation.previousState_14 = new VECTOR[sourceModel.n_vert_04];
    Arrays.setAll(animation.currentState_10, i -> new VECTOR());
    Arrays.setAll(animation.previousState_14, i -> new VECTOR());
    _8011a030.setu(0x1L);

    //LAB_80109e78
    for(int i = 0; i < sourceModel.n_vert_04; i++) {
      final SVECTOR sourceVertex = sourceModel.vert_top_00[i];
      animation.previousState_14[i].set(sourceVertex).shl(8);
    }

    //LAB_80109ecc
    //LAB_80109ee4
    for(int i = 0; i < animation.vertexCount_08; i++) {
      final SVECTOR diffVertex = diffModel.vert_top_00[i];
      final VECTOR previous = animation.previousState_14[i];
      final VECTOR current = animation.currentState_10[i];
      current.setX(((diffVertex.getX() << 8) - previous.getX()) / ticksRemaining);
      current.setY(((diffVertex.getY() << 8) - previous.getY()) / ticksRemaining);
      current.setZ(((diffVertex.getZ() << 8) - previous.getZ()) / ticksRemaining);
    }

    //LAB_80109f90
    return FlowControl.CONTINUE;
  }

  /**
   * Code deleted from LAB_8010a130. Condition variable was set from script, and value
   * was hardcoded to 0 so that the code in the condition was never run.
   */
  @Method(0x80109fc4L)
  public static void FUN_80109fc4(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
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

        final SVECTOR sp0x10 = new SVECTOR().set(effect.verticesCopy_1c[s1]);
        sp0x10.y.add((short)(rsin(s4) * (manager._10._28 << 10 >> 8) >> 12)); // 2b
        effect.vertices_0c[s1].set(sp0x10);
        s4 += (0x1000 / effect._18 / manager._10._2c >> 8);
        s1++;
      }

      //LAB_8010a124
      s1++;
    }

    //LAB_8010a130
    //LAB_8010a15c
    //LAB_8010a374
    effect._1a += (short)(manager._10._24 << 7 >> 8);
  }

  @Method(0x8010a3fcL)
  public static FlowControl FUN_8010a3fc(final RunningScript<? extends BattleScriptDataBase> script) {
    final int s4 = script.params_20[2].get();
    final int sp18 = script.params_20[3].get();

    final DeffTmdRenderer14 v1 = (DeffTmdRenderer14)((EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00).effect_44;
    final TmdObjTable1c tmd = v1.tmd_08;

    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      "FrozenJetEffect28",
      script.scriptState_04,
      SEffe::FUN_80109fc4,
      null,
      null,
      new FrozenJetEffect28(tmd.vert_top_00, tmd.primitives_10, s4, sp18 & 0xff)
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    manager._10._24 = 0x100;
    manager._10._28 = 0x100;
    manager._10._2c = 0x100;

    //LAB_8010a5c8
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  /** Used in Rose transform */
  @Method(0x8010a610L)
  public static FlowControl allocateGradientRaysEffect(final RunningScript<? extends BattleScriptDataBase> script) {
    final int count = script.params_20[1].get();

    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      "GradientRaysEffect24",
      script.scriptState_04,
      SEffe::gradientRaysEffectTicker,
      SEffe::gradientRaysEffectRenderer,
      null,
      new GradientRaysEffect24(count)
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    final GradientRaysEffect24 effect = (GradientRaysEffect24)manager.effect_44;
    effect._08 = script.params_20[2].get();
    effect._0c = script.params_20[3].get();
    effect._10 = script.params_20[4].get();
    effect._14 = script.params_20[5].get();
    effect.flags_18 = script.params_20[6].get();
    effect.type_1c = script.params_20[7].get();
    effect.projectionPlaneDistanceDiv4_20 = getProjectionPlaneDistance() >> 2;

    //LAB_8010a754
    for(int i = 0; i < effect.count_04; i++) {
      //LAB_8010a770
      effect.rays_00[i]._00 = (short)(rand() % 0x1000);

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
    manager._10.flags_00 |= 0x5400_0000;
    return FlowControl.CONTINUE;
  }

  /** Used in Rose transform */
  @Method(0x8010a860L)
  public static void renderGradientRay(final EffectManagerData6c manager, final GradientRaysEffectInstance04 gradientRay) {
    final SVECTOR sp0x38 = new SVECTOR();
    final SVECTOR sp0x40 = new SVECTOR();
    final SVECTOR sp0x48 = new SVECTOR();
    final SVECTOR sp0x50 = new SVECTOR();
    final SVECTOR xy0 = new SVECTOR();
    final SVECTOR xy1 = new SVECTOR();
    final SVECTOR xy2 = new SVECTOR();
    final SVECTOR xy3 = new SVECTOR();

    final MATRIX sp0x80 = new MATRIX().set(identityMatrix_800c3568);
    final MATRIX sp0xa0 = new MATRIX().set(identityMatrix_800c3568);
    final MATRIX sp0xc0 = new MATRIX().set(identityMatrix_800c3568);

    final GradientRaysEffect24 effect = (GradientRaysEffect24)manager.effect_44;

    //LAB_8010a968
    if((effect.flags_18 & 0x4) == 0) {
      if(effect._10 * 2 < gradientRay._02 * effect._08) {
        sp0x40.setY((short)-effect._10);
        sp0x48.setY((short)-effect._10);
        sp0x50.setY((short)(-effect._10 * 2));
      } else {
        //LAB_8010a9ec
        sp0x40.setY((short)(gradientRay._02 * -effect._08 / 2));
        sp0x48.setY((short)(gradientRay._02 * -effect._08 / 2));
        sp0x50.setY((short)(gradientRay._02 * -effect._08));
      }

      //LAB_8010aa34
      sp0x40.setZ((short)effect._0c);
      sp0x48.setZ((short)-effect._0c);
    }

    //LAB_8010aa54
    final VECTOR translation = new VECTOR().set(0, gradientRay._02 * effect._08, 0);
    final SVECTOR rotation = new SVECTOR().set(gradientRay._00, (short)0, (short)0);
    RotMatrix_Xyz(rotation, sp0xa0);
    sp0x80.transfer.set(translation);
    sp0x80.compose(sp0xa0, sp0xc0);
    FUN_800e8594(sp0x80, manager);

    if((manager._10.flags_00 & 0x400_0000) == 0) {
      sp0x80.compose(worldToScreenMatrix_800c3548, sp0xa0);
      RotMatrix_Xyz(manager._10.rot_10, sp0xa0);
      sp0xc0.compose(sp0xa0, sp0xc0);
      setRotTransMatrix(sp0xc0);
    } else {
      //LAB_8010ab10
      sp0xc0.compose(sp0x80, sp0xa0);
      sp0xa0.compose(worldToScreenMatrix_800c3548, sp0x80);
      setRotTransMatrix(sp0x80);
    }

    //LAB_8010ab34
    final int z = RotTransPers4(sp0x38, sp0x40, sp0x48, sp0x50, xy0, xy1, xy2, xy3);
    if(z >= effect.projectionPlaneDistanceDiv4_20) {
      final GpuCommandPoly cmd = new GpuCommandPoly(4)
        .translucent(Translucency.B_PLUS_F);

      if(effect.type_1c == 1) {
        //LAB_8010abf4
        final int v0 = (0x80 - gradientRay._02) * manager._10.colour_1c.getX() / 0x80;
        final int v1 = (short)v0 / 2;

        cmd
          .monochrome(0, 0)
          .monochrome(1, 0)
          .rgb(2, v0, v1, v1)
          .rgb(3, v0, v1, v1);
      } else if(effect.type_1c == 2) {
        //LAB_8010ac68
        final short s3 = (short)(FUN_8010b058(gradientRay._02) * manager._10.colour_1c.getX() * 8 / 0x80);
        final short s2 = (short)(FUN_8010b0dc(gradientRay._02) * manager._10.colour_1c.getY() * 8 / 0x80);
        final short a2 = (short)(FUN_8010b160(gradientRay._02) * manager._10.colour_1c.getZ() * 8 / 0x80);

        cmd
          .monochrome(0, 0)
          .rgb(1, s3 / 2, s2 / 2, a2 / 2)
          .rgb(2, s3 / 2, s2 / 2, a2 / 2)
          .rgb(3, s3, s2, a2);
      }

      //LAB_8010ad68
      //LAB_8010ad6c
      cmd
        .pos(0, xy0.getX(), xy0.getY())
        .pos(1, xy1.getX(), xy1.getY())
        .pos(2, xy2.getX(), xy2.getY())
        .pos(3, xy3.getX(), xy3.getY());

      GPU.queueCommand(z >> 2, cmd);
    }

    //LAB_8010ae18
  }

  @Method(0x8010ae40L)
  public static void gradientRaysEffectTicker(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
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
  public static void gradientRaysEffectRenderer(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    if(manager._10.flags_00 >= 0) {
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

  /**
   * Used for Death Dimension, Melbu's screenshot attack, and Kubila's demon frog, possibly other unknown effects
   */
  @Method(0x8010b1d8L)
  public static FlowControl allocateScreenCaptureEffect(final RunningScript<? extends BattleScriptDataBase> script) {
    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      "Screen capture",
      script.scriptState_04,
      null,
      SEffe::renderScreenCaptureEffect,
      null,
      new ScreenCaptureEffect1c()
    );

    final EffectManagerData6c manager = state.innerStruct_00;
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
    manager._10.flags_00 |= 0x5000_0000;
    return FlowControl.CONTINUE;
  }

  /**
   * TODO This is the second screen capture function, usage currently unknown
   */
  @Method(0x8010b594L)
  public static void FUN_8010b594(final EffectManagerData6c manager, final ScreenCaptureEffect1c effect, final MATRIX transforms) {
    int v0;
    int v1;
    int a0;
    int a1;

    final COLOUR sp0x48 = new COLOUR();

    if((manager._10.flags_00 & 0x40) != 0) {
      final Vector3f normal = new Vector3f();
      _800fb8d0.mul(transforms, normal);
      normal.add(transforms.transfer.getX() / 4096.0f, transforms.transfer.getY() / 4096.0f, transforms.transfer.getZ() / 4096.0f);
      sp0x48.unpack(GTE.normalColour(normal, (int)_800fb8cc.pack()));
    } else {
      //LAB_8010b6c8
      sp0x48.set(0x80, 0x80, 0x80);
    }

    //LAB_8010b6d8
    sp0x48.setR(sp0x48.getR() * manager._10.colour_1c.getX() / 128);
    sp0x48.setG(sp0x48.getG() * manager._10.colour_1c.getY() / 128);
    sp0x48.setB(sp0x48.getB() * manager._10.colour_1c.getZ() / 128);

    //LAB_8010b764
    for(int i = 0; i < 8; i++) {
      final GpuCommandPoly cmd = new GpuCommandPoly(3)
        .rgb(sp0x48.getR(), sp0x48.getG(), sp0x48.getB());

      switch(i) {
        case 1, 2, 4, 7 -> {
          final SVECTOR vert0 = new SVECTOR();
          final SVECTOR vert1 = new SVECTOR();
          final SVECTOR vert2 = new SVECTOR();
          final DVECTOR sxy0 = new DVECTOR();
          final DVECTOR sxy1 = new DVECTOR();
          final DVECTOR sxy2 = new DVECTOR();

          //LAB_8010b80c
          if(i == 1 || i == 4) {
            //LAB_8010b828
            a0 = i & 0x3;
            v0 = (a0 - 2) * effect.screenspaceW_10 / 4;
            vert1.setZ((short)v0);
            v0 = (a0 - 1) * effect.screenspaceW_10 / 4;
            vert0.setZ((short)v0);
            vert2.setZ((short)v0);
            a0 = i >> 2;
            v0 = (a0 - 1) * effect.screenspaceH_14 / 2;
            vert0.setY((short)v0);
            v0 = a0 * effect.screenspaceH_14 / 2;
            vert1.setY((short)v0);
            vert2.setY((short)v0);
            a0 = (i >> 1) * 64;
            v1 = (i & 0x1) * 32;

            cmd
              .uv(0, a0, v1 + effect.captureW_04 / 4 - 1)
              .uv(1, v1, a0 + effect.captureH_08 / 2 - 1)
              .uv(2, v1 + effect.captureW_04 / 4 - 1, a0 + effect.captureH_08 / 2 - 1);
          } else {
            //LAB_8010b8c8
            a0 = i & 0x3;
            v0 = (a0 - 2) * effect.screenspaceW_10 / 4;
            vert1.setZ((short)v0);
            vert0.setZ((short)v0);
            v0 = (a0 - 1) * effect.screenspaceW_10 / 4;
            vert2.setZ((short)v0);
            a0 = i >> 2;
            v0 = (a0 - 1) * effect.screenspaceH_14 / 2;
            vert0.setY((short)v0);
            v1 = (i & 1) * 32;
            a0 = (i >> 1) * 64;
            v0 = a0 * effect.screenspaceH_14 / 2;
            vert2.setY((short)v0);
            vert1.setY((short)v0);

            cmd
              .uv(0, v1, a0)
              .uv(1, v1, a0 + effect.captureH_08 / 2 - 1)
              .uv(2, v1 + effect.captureW_04 / 4 - 1, a0 + effect.captureH_08 / 2 - 1);
          }

          //LAB_8010b9a4
          final int z = perspectiveTransformTriple(vert0, vert1, vert2, sxy0, sxy1, sxy2);

          if(effect.screenspaceW_10 == 0) {
            //LAB_8010b638
            final int sp8c = getProjectionPlaneDistance();
            final long zShift = (z << 12) * 4; // Must be long or the calc will overflow
            effect.screenspaceW_10 = (int)(effect.captureW_04 * zShift / sp8c >>> 12);
            effect.screenspaceH_14 = (int)(effect.captureH_08 * zShift / sp8c >>> 12);
            break;
          }

          final ScreenCaptureEffectMetrics8 metrics = effect.metrics_00;

          cmd
            .bpp(Bpp.BITS_15)
            .vramPos(metrics.u_00 & 0x3c0, (metrics.v_02 & 0x1) == 0 ? 0 : 256)
            .pos(0, sxy0.getX(), sxy0.getY())
            .pos(1, sxy1.getX(), sxy1.getY())
            .pos(2, sxy2.getX(), sxy2.getY());

          GPU.queueCommand(z >> 2, cmd);
        }

        case 5, 6 -> {
          final SVECTOR vert0 = new SVECTOR();
          final SVECTOR vert1 = new SVECTOR();
          final SVECTOR vert2 = new SVECTOR();
          final SVECTOR vert3 = new SVECTOR();
          final SVECTOR sxy0 = new SVECTOR();
          final SVECTOR sxy1 = new SVECTOR();
          final SVECTOR sxy2 = new SVECTOR();
          final SVECTOR sxy3 = new SVECTOR();

          a0 = i & 0x3;
          a1 = i >> 2;
          v0 = (a0 - 2) * effect.screenspaceW_10 / 4;
          vert2.setZ((short)v0);
          vert0.setZ((short)v0);
          v0 = (a1 - 1) * effect.screenspaceH_14 / 2;
          vert1.setY((short)v0);
          vert0.setY((short)v0);
          v0 = (a0 - 1) * effect.screenspaceW_10 / 4;
          vert3.setZ((short)v0);
          vert1.setZ((short)v0);
          v0 = a1 * effect.screenspaceH_14 / 2;
          vert3.setY((short)v0);
          vert2.setY((short)v0);
          final int z = RotTransPers4(vert0, vert1, vert2, vert3, sxy0, sxy1, sxy2, sxy3);

          if(effect.screenspaceW_10 == 0) {
            //LAB_8010b664
            final int sp90 = getProjectionPlaneDistance();
            a1 = (z << 12) * 4;

            //LAB_8010b688
            effect.screenspaceW_10 = effect.captureW_04 * a1 / sp90 >>> 12;
            effect.screenspaceH_14 = effect.captureH_08 * a1 / sp90 >>> 12;
            break;
          }

          final int u = (i & 0x1) * 32;
          final int v = (i >> 1) * 64;
          final ScreenCaptureEffectMetrics8 metrics = effect.metrics_00;

          GPU.queueCommand(z >> 2, new GpuCommandPoly(4)
            .bpp(Bpp.BITS_15)
            .vramPos(metrics.u_00 & 0x3c0, (metrics.v_02 & 0x1) != 0 ? 256 : 0)
            .rgb(sp0x48.getR(), sp0x48.getG(), sp0x48.getB())
            .pos(0, sxy0.getX(), sxy0.getY())
            .pos(1, sxy1.getX(), sxy1.getY())
            .pos(2, sxy2.getX(), sxy2.getY())
            .pos(3, sxy3.getX(), sxy3.getY())
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
  public static void renderScreenCapture(final EffectManagerData6c manager, final ScreenCaptureEffect1c effect, final MATRIX transforms) {
    final COLOUR rgb = new COLOUR();

    if((manager._10.flags_00 & 0x40) != 0) {
      final Vector3f normal = new Vector3f();
      _800fb8d0.mul(transforms, normal);
      normal.add(transforms.transfer.getX() / 4096.0f, transforms.transfer.getY() / 4096.0f, transforms.transfer.getZ() / 4096.0f);
      rgb.unpack(GTE.normalColour(normal, (int)_800fb8cc.pack()));
    } else {
      //LAB_8010bd6c
      rgb.set(0x80, 0x80, 0x80);
    }

    //LAB_8010bd7c
    rgb.setR(rgb.getR() * manager._10.colour_1c.getX() / 128);
    rgb.setG(rgb.getG() * manager._10.colour_1c.getY() / 128);
    rgb.setB(rgb.getB() * manager._10.colour_1c.getZ() / 128);

    //LAB_8010be14
    for(int s0 = 0; s0 < 15; s0++) {
      final SVECTOR sp0x28 = new SVECTOR();
      final SVECTOR sp0x30 = new SVECTOR();
      final SVECTOR sp0x38 = new SVECTOR();
      final SVECTOR sp0x40 = new SVECTOR();

      final int a0 = s0 % 5;
      int v1 = effect.screenspaceW_10;
      int v0 = a0 * v1 / 5 - v1 / 2;
      sp0x28.setZ((short)v0);
      sp0x38.setZ((short)v0);

      final int a1 = s0 / 5;
      v1 = effect.screenspaceH_14;
      v0 = a1 * v1 / 3 - v1 / 2;
      sp0x28.setY((short)v0);
      sp0x30.setY((short)v0);

      v1 = effect.screenspaceW_10;
      v0 = (a0 + 1) * v1 / 5 - v1 / 2;
      sp0x30.setZ((short)v0);
      sp0x40.setZ((short)v0);

      v1 = effect.screenspaceH_14;
      v0 = (a1 + 1) * v1 / 3 - v1 / 2;
      sp0x38.setY((short)v0);
      sp0x40.setY((short)v0);

      final SVECTOR sxy0 = new SVECTOR();
      final SVECTOR sxy1 = new SVECTOR();
      final SVECTOR sxy2 = new SVECTOR();
      final SVECTOR sxy3 = new SVECTOR();
      final int z = RotTransPers4(sp0x28, sp0x30, sp0x38, sp0x40, sxy0, sxy1, sxy2, sxy3);

      if(effect.screenspaceW_10 == 0) {
        //LAB_8010bd08
        final int sp8c = getProjectionPlaneDistance();
        final long zShift = z << 14; // Must be long or the calc will overflow
        effect.screenspaceW_10 = (int)(effect.captureW_04 * zShift / sp8c >>> 12);
        effect.screenspaceH_14 = (int)(effect.captureH_08 * zShift / sp8c >>> 12);
        break;
      }

      final int leftU = s0 % 2 * 32;
      final int topV = s0 / 2 * 32;
      final int rightU = leftU + effect.captureW_04 / 5 - 1;
      final int bottomV = topV + effect.captureH_08 / 3 - 1;

      final ScreenCaptureEffectMetrics8 metrics = effect.metrics_00;

      GPU.queueCommand(z >> 2, new GpuCommandPoly(4)
        .bpp(Bpp.BITS_15)
        .vramPos(metrics.u_00 & 0x3c0, (metrics.v_02 & 0x1) != 0 ? 256 : 0)
        .rgb(rgb.getR(), rgb.getG(), rgb.getB())
        .pos(0, sxy0.getX(), sxy0.getY())
        .pos(1, sxy1.getX(), sxy1.getY())
        .pos(2, sxy2.getX(), sxy2.getY())
        .pos(3, sxy3.getX(), sxy3.getY())
        .uv(0, leftU, topV)
        .uv(1, rightU, topV)
        .uv(2, leftU, bottomV)
        .uv(3, rightU, bottomV)
      );
    }

    //LAB_8010c0f0
  }

  @Method(0x8010c114L)
  public static void renderScreenCaptureEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final MATRIX sp0x10 = new MATRIX().set(identityMatrix_800c3568);
    final MATRIX transforms = new MATRIX().set(identityMatrix_800c3568);

    if(manager._10.flags_00 >= 0) {
      final ScreenCaptureEffect1c effect = (ScreenCaptureEffect1c)manager.effect_44;
      FUN_800e8594(sp0x10, manager);
      sp0x10.compose(worldToScreenMatrix_800c3548, transforms);
      screenCaptureRenderers_80119fec[effect.rendererIndex_0c].accept(manager, effect, transforms);
    }

    //LAB_8010c278
  }

  @Method(0x8010c2e0L)
  public static void FUN_8010c2e0(final ScreenCaptureEffectMetrics8 metrics, final int a1) {
    if((a1 & 0xf_ff00) != 0xf_ff00) {
      final DeffPart.SpriteType spriteType = (DeffPart.SpriteType)getDeffPart(a1 | 0x400_0000);
      final DeffPart.SpriteMetrics deffMetrics = spriteType.metrics_08;
      metrics.u_00 = deffMetrics.u_00;
      metrics.v_02 = deffMetrics.v_02;
      metrics.clut_06 = deffMetrics.clutY_0a << 6 | (deffMetrics.clutX_08 & 0x3f0) >>> 4;
    }

    //LAB_8010c368
  }

  @Method(0x8010c378L)
  public static FlowControl allocateLensFlareEffect(final RunningScript<? extends BattleScriptDataBase> script) {
    final int bobjIndex = script.params_20[1].get();
    final int x = script.params_20[2].get();
    final int y = script.params_20[3].get();
    final int z = script.params_20[4].get();

    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      "LensFlareEffect50",
      script.scriptState_04,
      SEffe::tickLensFlareEffect,
      SEffe::renderLensFlareEffect,
      null,
      new LensFlareEffect50()
    );

    final EffectManagerData6c manager = state.innerStruct_00;
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
          final SpriteMetrics08 metrics = spriteMetrics_800c6948[a1 & 0xff];
          effect.u_04[i] = metrics.u_00;
          effect.v_0e[i] = metrics.v_02;
          effect.w_18[i] = metrics.w_04;
          effect.h_22[i] = metrics.h_05;
          effect.clut_2c[i] = metrics.clut_06;
        } else {
          //LAB_8010c5a8
          final DeffPart.SpriteType spriteType = (DeffPart.SpriteType)getDeffPart(a1 | 0x400_0000);
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

    effect.bobjIndex_3c = bobjIndex;
    effect.x_40 = (short)x;
    effect.y_42 = (short)y;
    effect.z_44 = (short)z;
    effect.brightness_48 = 0;
    manager._10.flags_00 |= 0x5000_0000;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x8010c69cL)
  public static void tickLensFlareEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final LensFlareEffect50 effect = (LensFlareEffect50)manager.effect_44;
    effect.shouldRender_4a = (short)(rand() % 30);

    if(effect.shouldRender_4a != 0) {
      final DVECTOR screenCoords = perspectiveTransformXyz(((BattleObject27c)scriptStatePtrArr_800bc1c0[effect.bobjIndex_3c].innerStruct_00).model_148, effect.x_40, effect.y_42, effect.z_44);
      final int x = (int)-(screenCoords.getX() * 2.5f);
      final int y = (int)-(screenCoords.getY() * 2.5f);

      //LAB_8010c7c0
      for(int i = 0; i < 5; i++) {
        final LensFlareEffectInstance3c inst = effect.instances_38[i];
        final int dispW = displayWidth_1f8003e0.get();
        final int dispH = displayHeight_1f8003e4.get();
        inst.x_04 = (short)(screenCoords.getX() + dispW / 2);
        inst.y_06 = (short)(screenCoords.getY() + dispH / 2);

        if(inst.x_04 > 0 && inst.x_04 < dispW && inst.y_06 > 0 && inst.y_06 < dispH) {
          inst.onScreen_03 = true;

          final int scale = (int)_800fb8fc.offset(i * 0x4L).get();
          inst.x_04 += (short)(x * scale >> 8);
          inst.y_06 += (short)(y * scale >> 8);
        } else {
          //LAB_8010c870
          inst.onScreen_03 = false;
        }
        //LAB_8010c874
      }

      // Adjust brightness based on X position
      int screenX = Math.abs(screenCoords.getX());
      final int screenWidth = displayWidth_1f8003e0.get() / 2;
      if(screenX > screenWidth) {
        screenX = screenWidth;
      }

      //LAB_8010c8b8
      effect.brightness_48 = (short)(0xff - (0xff00 / screenWidth * screenX >> 8));
    }
    //LAB_8010c8e0
  }

  @Method(0x8010c8f8L)
  public static void renderLensFlareEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final LensFlareEffect50 effect = (LensFlareEffect50)manager.effect_44;

    if(effect.shouldRender_4a != 0) {
      effect._02++;
      final int sp10 = manager._10.flags_00;

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
          final int r = manager._10.colour_1c.getX() * effect.brightness_48 >> 8;
          final int g = manager._10.colour_1c.getY() * effect.brightness_48 >> 8;
          final int b = manager._10.colour_1c.getZ() * effect.brightness_48 >> 8;

          if(i == 0) {
            for(int j = 0; j < 4; j++) {
              final int x = (inst.widthScale_2e * w >> 12) * _800fb910.get(j).get(0).get();
              final int y = (inst.heightScale_30 * h >> 12) * _800fb910.get(j).get(1).get();
              final int halfW = displayWidth_1f8003e0.get() / 2;
              final int halfH = displayHeight_1f8003e4.get() / 2;
              final int[][] sp0x48 = new int[4][2];
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
                .pos(0, sp0x48[_800fb930.get(j).get(0).get()][0], sp0x48[_800fb930.get(j).get(0).get()][1])
                .pos(1, sp0x48[_800fb930.get(j).get(1).get()][0], sp0x48[_800fb930.get(j).get(1).get()][1])
                .pos(2, sp0x48[_800fb930.get(j).get(2).get()][0], sp0x48[_800fb930.get(j).get(2).get()][1])
                .pos(3, sp0x48[_800fb930.get(j).get(3).get()][0], sp0x48[_800fb930.get(j).get(3).get()][1])
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
            final int halfW = displayWidth_1f8003e0.get() / 2;
            final int halfH = displayHeight_1f8003e4.get() / 2;
            final int x = inst.x_04 - halfW - (inst.widthScale_2e * w >> 12) / 2;
            final int y = inst.y_06 - halfH - (inst.heightScale_30 * h >> 12) / 2;
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

  @Method(0x8010d1dcL)
  public static FlowControl allocateWsDragoonTransformationFeathersEffect(final RunningScript<? extends BattleScriptDataBase> script) {
    final int featherCount = script.params_20[1].get();
    final int effectFlags = script.params_20[2].get();

    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      "WsDragoonTransformationFeathersEffect14",
      script.scriptState_04,
      SEffe::tickWsDragoonTransformationFeathersEffect,
      SEffe::renderWsDragoonTransformationFeathersEffect,
      null,
      new WsDragoonTransformationFeathersEffect14(featherCount)
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    final WsDragoonTransformationFeathersEffect14 featherEffect = (WsDragoonTransformationFeathersEffect14)manager.effect_44;
    featherEffect.unused_02 = 0;

    //LAB_8010d298
    for(int i = 0; i < featherCount; i++) {
      final WsDragoonTransformationFeatherInstance70 feather = featherEffect.featherArray_10[i];
      feather.renderFeather_00 = false;
      feather.callbackIndex_02 = 0;
      feather.currentFrame_04 = 0;
      feather.countCallback0Frames_64 = 1;
      final int yOffset = -1500 / featherCount * i << 8;
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
      feather.angleStep_60 = angleStep;
      feather.translationMagnitudeY_50 = rand() % 0x178 << 8;
      simpleRand();
      feather.spriteAngle_6e = rand() % 0x1000;
      feather.translationMagnitudeXz_3c = 0;
      feather.angle_58 = 0;
      feather.angleNoiseXz_5c = rand() % 0x1000 << 8;
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
      final SpriteMetrics08 metrics = spriteMetrics_800c6948[effectFlags & 0xff];
      featherEffect.u_06 = (short)metrics.u_00;
      featherEffect.v_08 = (short)metrics.v_02;
      featherEffect.width_0a = (short)metrics.w_04;
      featherEffect.height_0c = (short)metrics.h_05;
      featherEffect.clut_0e = (short)metrics.clut_06;
    } else {
      //LAB_8010d508
      final DeffPart.SpriteType spriteType = (DeffPart.SpriteType)getDeffPart(effectFlags | 0x400_0000);
      final DeffPart.SpriteMetrics deffMetrics = spriteType.metrics_08;
      featherEffect.u_06 = (short)deffMetrics.u_00;
      featherEffect.v_08 = (short)deffMetrics.v_02;
      featherEffect.width_0a = (short)(deffMetrics.w_04 * 4);
      featherEffect.height_0c = (short)deffMetrics.h_06;
      featherEffect.clut_0e = (short)GetClut(deffMetrics.clutX_08, deffMetrics.clutY_0a);
    }

    //LAB_8010d564
    manager._10.flags_00 |= 0x5000_0000;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x8010d5b4L)
  public static void renderWsDragoonTransformationFeathersEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final WsDragoonTransformationFeathersEffect14 effect = (WsDragoonTransformationFeathersEffect14)manager.effect_44;
    final GenericSpriteEffect24 spriteEffect = new GenericSpriteEffect24();

    spriteEffect.flags_00 = manager._10.flags_00;
    spriteEffect.x_04 = (short)(-effect.width_0a / 2);
    spriteEffect.y_06 = (short)(-effect.height_0c / 2);
    spriteEffect.w_08 = effect.width_0a;
    spriteEffect.h_0a = effect.height_0c;
    spriteEffect.tpage_0c = (effect.v_08 & 0x100) >>> 4 | (effect.u_06 & 0x3ff) >>> 6;
    spriteEffect.u_0e = (effect.u_06 & 0x3f) * 4;
    spriteEffect.v_0f = effect.v_08;
    spriteEffect.clutX_10 = effect.clut_0e << 4 & 0x3ff;
    spriteEffect.clutY_12 = effect.clut_0e >>> 6 & 0x1ff;
    spriteEffect.unused_18 = 0;
    spriteEffect.unused_1a = 0;

    final VECTOR translation = new VECTOR();

    //LAB_8010d6c8
    for(int i = 0; i < effect.count_00; i++) {
      final WsDragoonTransformationFeatherInstance70 feather = effect.featherArray_10[i];

      if(feather.renderFeather_00) {
        if(feather.r_38 == 0x7f && feather.g_39 == 0x7f && feather.b_3a == 0x7f) {
          spriteEffect.r_14 = manager._10.colour_1c.getX();
          spriteEffect.g_15 = manager._10.colour_1c.getY();
          spriteEffect.b_16 = manager._10.colour_1c.getZ();
        } else {
          //LAB_8010d718
          spriteEffect.r_14 = feather.r_38;
          spriteEffect.g_15 = feather.g_39;
          spriteEffect.b_16 = feather.b_3a;
        }

        //LAB_8010d73c
        spriteEffect.scaleX_1c = manager._10.scale_16.getX();
        spriteEffect.scaleY_1e = manager._10.scale_16.getY();
        spriteEffect.angle_20 = feather.spriteAngle_6e;
        translation.set(feather.translation_08).shra(8).add(manager._10.trans_04);
        renderGenericSpriteAtZOffset0(spriteEffect, translation);
      }
    }
  }

  @Method(0x8010d7dcL)
  public static FlowControl allocateGoldDragoonTransformEffect(final RunningScript<? extends BattleScriptDataBase> script) {
    final int s7 = script.params_20[1].get();
    final int count = script.params_20[2].get();
    final int sp1c = 32;
    final int sp20 = script.params_20[4].get();
    final int sp24 = script.params_20[5].get();
    final int sp28 = script.params_20[6].get();
    final int sp2c = script.params_20[7].get();
    final int sp30 = script.params_20[8].get();

    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
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

      final int s2 = rand() % (sp20 - sp1c + 1) + sp1c;
      final int theta = rand() % 4096;
      instance.transStep_28.setX(rcos(theta) * s2 >> 4);
      instance.transStep_28.setY(rand() % (sp28 - sp24 + 1) + sp24 << 8);
      instance.transStep_28.setZ(rsin(theta) * s2 >> 4);
      instance.rot_38.setX(rand() % 4096);
      instance.rot_38.setY(rand() % 4096);
      instance.rot_38.setZ(rand() % 4096);
      instance.rotStep_48.setX((simpleRand() & 1) != 0 ? rand() % 401 : -(rand() % 401));
      instance.rotStep_48.setY((simpleRand() & 1) != 0 ? rand() % 401 : -(rand() % 401));
      instance.rotStep_48.setZ((simpleRand() & 1) != 0 ? rand() % 401 : -(rand() % 401));

      if(sp2c != 0) {
        //LAB_8010dbc4
        instance._7c = (short)(rand() % (sp2c + 1));
      } else {
        instance._7c = 0;
      }

      //LAB_8010dbe8
      instance._7e = (short)(rand() % (sp30 + 2));
      instance._80 = -1;

      if((s7 & 0xf_ff00) == 0xf_ff00) {
        //TODO I added the first deref here, this might have been a retail bug...
        //     Looking at how _800c6944 gets set, I don't see how it could have been correct
        instance.tmd_70 = tmds_800c6944[s7 & 0xff];
      } else {
        //LAB_8010dc40
        final DeffPart.TmdType tmdType = (DeffPart.TmdType)getDeffPart(s7 | 0x300_0000);
        instance.tmd_70 = tmdType.tmd_0c.tmdPtr_00.tmd.objTable[0];
      }

      //LAB_8010dc60
      instance.trans_08.set(0, 0, 0);
    }

    //LAB_8010dc8c
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x8010dcd0L)
  public static void goldDragoonTransformEffectTicker(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
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

        instance.trans_08.x.add(instance.transStep_28.getX());
        instance.trans_08.y.set(counter * counter * 0x1800 - instance.transStep_28.getY() * counter);
        instance.trans_08.z.add(instance.transStep_28.getZ());

        instance.rot_38.add(instance.rotStep_48);

        if(instance._7c <= 0) {
          if(instance.trans_08.getY() > 0x400) {
            instance.used_00 = false;
          }
          //LAB_8010ddf8
        } else if(instance.trans_08.getY() >= 0) {
          instance.counter_04 = 1;
          instance.transStep_28.y.div(2);
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
  public static void goldDragoonTransformEffectRenderer(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final GoldDragoonTransformEffect20 effect = (GoldDragoonTransformEffect20)manager.effect_44;
    effect._04++;

    final GsDOBJ2 dobj2 = new GsDOBJ2();
    final VECTOR trans = new VECTOR();
    final SVECTOR rot = new SVECTOR();
    final MATRIX transforms = new MATRIX();
    final MATRIX sp0x98 = new MATRIX();
    final VECTOR scale = new VECTOR();

    //LAB_8010ded8
    for(final GoldDragoonTransformEffectInstance84 instance : effect.parts_08) {
      if(instance.used_00) {
        trans.setX((instance.trans_08.getX() >> 8) + manager._10.trans_04.getX());
        trans.setY((instance.trans_08.getY() >> 8) + manager._10.trans_04.getY());
        trans.setZ((instance.trans_08.getZ() >> 8) + manager._10.trans_04.getZ());

        rot.set(instance.rot_38);
        scale.set(manager._10.scale_16);

        RotMatrix_Xyz(rot, transforms);
        transforms.transfer.set(trans);
        transforms.scale(scale);

        dobj2.tmd_08 = instance.tmd_70;

        transforms.compose(worldToScreenMatrix_800c3548, sp0x98);
        setRotTransMatrix(sp0x98);

        zOffset_1f8003e8.set(0);
        tmdGp0Tpage_1f8003ec.set(2);

        renderDobj2(dobj2);
      }
    }

    //LAB_8010e020
  }

  @Method(0x8010e04cL)
  public static FlowControl allocateStarChildrenMeteorEffect(final RunningScript<? extends BattleScriptDataBase> script) {
    final int meteorCount = script.params_20[2].get();
    final int effectFlag = script.params_20[1].get();

    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      "StarChildrenMeteorEffect10",
      script.scriptState_04,
      SEffe::tickStarChildrenMeteorEffect,
      SEffe::renderStarChildrenMeteorEffect,
      null,
      new StarChildrenMeteorEffect10(meteorCount)
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    manager._10.flags_00 = 0x5000_0000;

    final StarChildrenMeteorEffect10 meteorEffect = (StarChildrenMeteorEffect10)manager.effect_44;
    final StarChildrenMeteorEffectInstance10[] meteorArray = meteorEffect.meteorArray_0c;

    //LAB_8010e100
    for(int i = 0; i < meteorCount; i++) {
      final StarChildrenMeteorEffectInstance10 meteor = meteorArray[i];
      meteor.centerOffsetX_02 = rand() % 321 - 160;
      meteor.centerOffsetY_04 = rand() % 241 - 120;
      final int sideScale = rand() % 1025 + 1024;
      meteor.scale_0a = sideScale << 1;
      meteor.scaleW_0c = sideScale;
      meteor.scaleH_0e = sideScale;
    }

    //LAB_8010e1d8
    if((effectFlag & 0xf_ff00) == 0xf_ff00) {
      final SpriteMetrics08 metrics = spriteMetrics_800c6948[effectFlag & 0xff];
      meteorEffect.metrics_04.u_00 = metrics.u_00;
      meteorEffect.metrics_04.v_02 = metrics.v_02;
      meteorEffect.metrics_04.w_04 = metrics.w_04;
      meteorEffect.metrics_04.h_05 = metrics.h_05;
      meteorEffect.metrics_04.clut_06 = metrics.clut_06;
    } else {
      //LAB_8010e254
      final DeffPart.SpriteType spriteType = (DeffPart.SpriteType)getDeffPart(effectFlag | 0x400_0000);
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
  public static void renderStarChildrenMeteorEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final StarChildrenMeteorEffect10 meteorEffect = (StarChildrenMeteorEffect10)manager.effect_44;
    final int flags = manager._10.flags_00;
    final int tpage = (meteorEffect.metrics_04.v_02 & 0x100) >>> 4 | (meteorEffect.metrics_04.u_00 & 0x3ff) >>> 6;
    final int vramX = (tpage & 0b1111) * 64;
    final int vramY = (tpage & 0b10000) != 0 ? 256 : 0;
    final int leftU = (meteorEffect.metrics_04.u_00 & 0x3f) * 4;
    final int rightU = leftU + meteorEffect.metrics_04.w_04;
    final int bottomV = meteorEffect.metrics_04.v_02;
    final int topV = bottomV + meteorEffect.metrics_04.h_05;
    final int clutX = meteorEffect.metrics_04.clut_06 << 4 & 0x3ff;
    final int clutY = meteorEffect.metrics_04.clut_06 >>> 6 & 0x1ff;
    final int r = manager._10.colour_1c.getX();
    final int g = manager._10.colour_1c.getY();
    final int b = manager._10.colour_1c.getZ();
    final StarChildrenMeteorEffectInstance10[] meteorArray = meteorEffect.meteorArray_0c;

    //LAB_8010e414
    for(int i = 0; i < meteorEffect.count_00; i++) {
      final StarChildrenMeteorEffectInstance10 meteor = meteorArray[i];

      final int w = meteor.scaleW_0c * meteorEffect.metrics_04.w_04 >> 12;
      final int h = meteor.scaleH_0e * meteorEffect.metrics_04.h_05 >> 12;
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
  public static void tickStarChildrenMeteorEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final StarChildrenMeteorEffect10 meteorEffect = (StarChildrenMeteorEffect10)manager.effect_44;

    //LAB_8010e6ec
    final StarChildrenMeteorEffectInstance10[] meteorArray = meteorEffect.meteorArray_0c;
    for(int i = 0; i < meteorEffect.count_00; i++) {
      final StarChildrenMeteorEffectInstance10 meteor = meteorArray[i];
      meteor.centerOffsetX_02 += (short)((rsin(manager._10.rot_10.getX()) * 32 >> 12) * manager._10.scale_16.getX() * meteor.scale_0a >> 24);
      meteor.centerOffsetY_04 += (short)((rcos(manager._10.rot_10.getX()) * 32 >> 12) * manager._10.scale_16.getX() * meteor.scale_0a >> 24);

      if(meteor.scale_0a * 120 + 50 >> 12 < meteor.centerOffsetY_04) {
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

  @Method(0x8010e89cL)
  public static FlowControl allocateMoonlightStarsEffect(final RunningScript<? extends BattleScriptDataBase> script) {
    final int starCount = script.params_20[4].get();
    final int effectFlags = script.params_20[1].get();
    final int maxToggleFrameThreshold = script.params_20[2].get();
    final int maxScale = script.params_20[3].get();

    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      "MoonlightStarsEffect18",
      script.scriptState_04,
      SEffe::tickMoonlightStarsEffect,
      SEffe::renderMoonlightStarsEffect,
      null,
      new MoonlightStarsEffect18(starCount)
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    final MoonlightStarsEffect18 starEffect = (MoonlightStarsEffect18)manager.effect_44;
    manager._10.flags_00 = 0x5000_0000;

    //LAB_8010e980
    for(int i = 0; i < starCount; i++) {
      final MoonlightStarsEffectInstance3c star = starEffect.starArray_0c[i];
      star.currentFrame_00 = 0;
      star.renderStars_03 = true;
      star.maxToggleFrameThreshold_36 = maxToggleFrameThreshold;
      star.toggleOffFrameThreshold_38 = (int)(seed_800fa754.advance().get() % 181);
      final int scale = (int)(seed_800fa754.advance().get() % (maxScale + 1));
      final int angle = (int)(seed_800fa754.advance().get() & 0xfff);
      final short x = (short)((rcos(angle) - rsin(angle)) * scale >> 12);
      final short z = (short)((rcos(angle) + rsin(angle)) * scale >> 12);
      star.translation_04.set(x, (short)0, z);
    }

    //LAB_8010ead0
    if((effectFlags & 0xf_ff00) == 0xf_ff00) {
      final SpriteMetrics08 metrics = spriteMetrics_800c6948[effectFlags & 0xff];
      starEffect.metrics_04.u_00 = metrics.u_00;
      starEffect.metrics_04.v_02 = metrics.v_02;
      starEffect.metrics_04.w_04 = metrics.w_04;
      starEffect.metrics_04.h_05 = metrics.h_05;
      starEffect.metrics_04.clut_06 = metrics.clut_06;
    } else {
      //LAB_8010eb50
      final DeffPart.SpriteType spriteType = (DeffPart.SpriteType)getDeffPart(effectFlags | 0x400_0000);
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
  public static void renderMoonlightStarsEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final MoonlightStarsEffect18 starEffect = (MoonlightStarsEffect18)manager.effect_44;

    final GenericSpriteEffect24 spriteEffect = new GenericSpriteEffect24();
    spriteEffect.flags_00 = manager._10.flags_00 & 0xffff_ffffL;
    spriteEffect.x_04 = (short)(-starEffect.metrics_04.w_04 / 2);
    spriteEffect.y_06 = (short)(-starEffect.metrics_04.h_05 / 2);
    spriteEffect.w_08 = starEffect.metrics_04.w_04;
    spriteEffect.h_0a = starEffect.metrics_04.h_05;
    spriteEffect.tpage_0c = (starEffect.metrics_04.v_02 & 0x100) >>> 4 | (starEffect.metrics_04.u_00 & 0x3ff) >>> 6;
    spriteEffect.u_0e = (starEffect.metrics_04.u_00 & 0x3f) << 2;
    spriteEffect.v_0f = starEffect.metrics_04.v_02;
    spriteEffect.clutX_10 = starEffect.metrics_04.clut_06 << 4 & 0x3ff;
    spriteEffect.clutY_12 = starEffect.metrics_04.clut_06 >>> 6 & 0x1ff;
    spriteEffect.unused_18 = 0;
    spriteEffect.unused_1a = 0;

    final VECTOR translation = new VECTOR();

    //LAB_8010ed00
    for(int i = 0; i < starEffect.count_00; i++) {
      final MoonlightStarsEffectInstance3c star = starEffect.starArray_0c[i];

      // If a star is set not to render, do not render subsequent stars either.
      if(!star.renderStars_03) {
        break;
      }

      spriteEffect.r_14 = manager._10.colour_1c.getX();
      spriteEffect.g_15 = manager._10.colour_1c.getY();
      spriteEffect.b_16 = manager._10.colour_1c.getZ();
      spriteEffect.scaleX_1c = manager._10.scale_16.getX();
      spriteEffect.scaleY_1e = manager._10.scale_16.getY();
      spriteEffect.angle_20 = manager._10.rot_10.getX();
      translation.set(manager._10.trans_04).add(star.translation_04);
      renderGenericSpriteAtZOffset0(spriteEffect, translation);
    }

    //LAB_8010edac
  }

  @Method(0x8010edc8L)
  public static FlowControl allocateStarChildrenImpactEffect(final RunningScript<? extends BattleScriptDataBase> script) {
    final int impactCount = script.params_20[1].get();
    final int maxStartingFrame = script.params_20[2].get();
    final int maxTranslationMagnitude = script.params_20[3].get();

    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      "StarChildrenImpactEffect20",
      script.scriptState_04,
      SEffe::tickStarChildrenImpactEffect,
      SEffe::renderStarChildrenImpactEffect,
      null,
      new StarChildrenImpactEffect20(impactCount)
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    final StarChildrenImpactEffect20 impactEffect = (StarChildrenImpactEffect20)manager.effect_44;

    //LAB_8010eecc
    for(int i = 0; i < impactCount; i++) {
      final StarChildrenImpactEffectInstancea8 impact = impactEffect.impactArray_08[i];

      impact.renderImpact_00 = false;
      impact.renderShockwave_01 = false;
      impact.startingFrame_04 = rand() % maxStartingFrame + 1;
      final int translationMagnitude = rand() % (maxTranslationMagnitude + 1);
      final int angle = rand() % 4096;
      impact.unused_08 = rand() % maxStartingFrame + 1;
      impact.translation_0c[0].setX((rcos(angle) - rsin(angle)) * translationMagnitude >> 12);
      impact.translation_0c[0].setY(0);
      impact.translation_0c[0].setZ((rcos(angle) + rsin(angle)) * translationMagnitude >> 12);
      impact.translation_0c[1].setX(impact.translation_0c[0].getX());
      impact.translation_0c[1].setY(impact.translation_0c[0].getY() - 0x100);
      impact.translation_0c[1].setZ(impact.translation_0c[0].getZ());
      impact.rotation_2c[0].set(0, rand() % 4096, 0);
      impact.rotation_2c[1].set(0, rand() % 4096, 0);
      impact.scale_6c[0].set(0, 0, 0);
      impact.scale_6c[1].set(0xc00, 0x400, 0xc00);
      impact.opacity_8c[0].set(0xff, 0xff, 0xff);
      impact.opacity_8c[1].set(0xff, 0xff, 0xff);
      impact.explosionObjTable_94 = ((DeffPart.TmdType)getDeffPart(0x300_7100)).tmd_0c.tmdPtr_00.tmd.objTable[0];
      impact.shockwaveObjTable_98 = ((DeffPart.TmdType)getDeffPart(0x300_7101)).tmd_0c.tmdPtr_00.tmd.objTable[0];
      impact.plumeObjTable_9c = ((DeffPart.TmdType)getDeffPart(0x300_7103)).tmd_0c.tmdPtr_00.tmd.objTable[0];
      impact.explosionHeightAngle_a0 = 0;
      impact.animationFrame_a2 = 0;
    }

    //LAB_8010f0d0
    manager._10.flags_00 = 0x1400_0000;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x8010f124L)
  public static void tickStarChildrenImpactEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final StarChildrenImpactEffect20 impactEffect = (StarChildrenImpactEffect20)manager.effect_44;

    //LAB_8010f168
    for(int i = 0; i < impactEffect.impactArray_08.length; i++) {
      final StarChildrenImpactEffectInstancea8 impact = impactEffect.impactArray_08[i];

      if(impactEffect.currentFrame_04 > impact.startingFrame_04) {
        if(impact.explosionHeightAngle_a0 >= 0x1000) {
          impact.renderImpact_00 = false;
        } else {
          //LAB_8010f19c
          final int currentAnimFrame = impact.animationFrame_a2;

          // Stage 0
          if(currentAnimFrame < 9) {
            //LAB_8010f240
            impact.renderImpact_00 = true;
            impact.renderShockwave_01 = true;
            impact.rotation_2c[0].y.add(0x80);
            impact.scale_6c[0].x.add(0x266);
            impact.scale_6c[0].z.add(0x266);
            impact.explosionHeightAngle_a0 += 0xcc;

            final int explosionHeight = rsin(impact.explosionHeightAngle_a0) * 0x1600 >> 12;
            impact.scale_6c[0].setY(Math.max(explosionHeight, 0));

            //LAB_8010f2a8
            impact.opacity_8c[0].r.sub(23);
            impact.opacity_8c[0].g.sub(23);
            impact.opacity_8c[0].b.sub(23);
          } else { // Stage 1
            if(currentAnimFrame == 9) {
              impact.translation_0c[0].setY(-0x800);
              impact.scale_6c[0].setX(0x6800);
              impact.scale_6c[0].setZ(0x6800);
            }

            //LAB_8010f1dc
            // Start transforming plume
            if(currentAnimFrame >= 10) {
              if(currentAnimFrame == 10) {
                impact.renderShockwave_01 = false;
              }

              //LAB_8010f1f0
              impact.rotation_2c[1].y.add(0x80);
              impact.explosionHeightAngle_a0 += 0x200;
              impact.scale_6c[1].x.sub(0x1c0);
              impact.scale_6c[1].y.add(0x600);
              impact.scale_6c[1].z.sub(0x1c0);
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
  public static void renderStarChildrenImpactEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final StarChildrenImpactEffect20 impactEffect = (StarChildrenImpactEffect20)manager.effect_44;
    if(manager._10.flags_00 >= 0) {
      final VECTOR translation = new VECTOR();
      final SVECTOR rotation = new SVECTOR();
      final MATRIX transformMatrix1 = new MATRIX();
      final MATRIX finalTransformMatrix1 = new MATRIX();
      final VECTOR scale = new VECTOR();
      final MATRIX transformMatrix0 = new MATRIX();

      final GsDOBJ2 dobj = new GsDOBJ2();

      //LAB_8010f3a4
      for(int i = 0; i < impactEffect.impactArray_08.length; i++) {
        final StarChildrenImpactEffectInstancea8 impact = impactEffect.impactArray_08[i];

        if(impact.renderImpact_00) {
          FUN_800e8594(transformMatrix0, manager);
          final int stageNum = impact.animationFrame_a2 >= 10 ? 1 : 0;
          translation.setX(impact.translation_0c[stageNum].getX() + manager._10.trans_04.getX());
          translation.setY(impact.translation_0c[stageNum].getY() + manager._10.trans_04.getY());
          translation.setZ(impact.translation_0c[stageNum].getZ() + manager._10.trans_04.getZ());
          rotation.setX((short)impact.rotation_2c[stageNum].getX());
          rotation.setY((short)impact.rotation_2c[stageNum].getY());
          rotation.setZ((short)impact.rotation_2c[stageNum].getZ());
          final short scaleX = (short)(impact.scale_6c[stageNum].getX() * manager._10.scale_16.getX() >> 12);
          final short scaleY = (short)(impact.scale_6c[stageNum].getY() * manager._10.scale_16.getY() >> 12);
          final short scaleZ = (short)(impact.scale_6c[stageNum].getZ() * manager._10.scale_16.getZ() >> 12);
          final int r = impact.opacity_8c[stageNum].getR() * manager._10.colour_1c.getX() >> 8;
          final int g = impact.opacity_8c[stageNum].getG() * manager._10.colour_1c.getY() >> 8;
          final int b = impact.opacity_8c[stageNum].getB() * manager._10.colour_1c.getZ() >> 8;

          if((manager._10.flags_00 & 0x40) == 0) {
            FUN_800e61e4(r / 128.0f, g / 128.0f, b / 128.0f);
          }

          //LAB_8010f50c
          GsSetLightMatrix(transformMatrix0);
          RotMatrix_Xyz(rotation, transformMatrix1);
          transformMatrix1.transfer.set(translation);
          scale.set(scaleX, scaleY, scaleZ);
          transformMatrix1.scale(scale);
          dobj.attribute_00 = manager._10.flags_00;
          transformMatrix1.compose(worldToScreenMatrix_800c3548, finalTransformMatrix1);
          setRotTransMatrix(finalTransformMatrix1);
          zOffset_1f8003e8.set(0);
          tmdGp0Tpage_1f8003ec.set(manager._10.flags_00 >>> 23 & 0x60);

          final int oldZShift = zShift_1f8003c4.get();
          final int oldZMax = zMax_1f8003cc.get();
          final int oldZMin = zMin;
          zShift_1f8003c4.set(2);
          zMax_1f8003cc.set(0xffe);
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

          zShift_1f8003c4.set(oldZShift);
          zMax_1f8003cc.set(oldZMax);
          zMin = oldZMin;

          //LAB_8010f608
          if((manager._10.flags_00 & 0x40) == 0) {
            FUN_800e62a8();
          }
        }
      }
    }
    //LAB_8010f640
  }

  @Method(0x8010f978L)
  public static void tickWsDragoonTransformationFeathersEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final WsDragoonTransformationFeathersEffect14 effect = (WsDragoonTransformationFeathersEffect14)manager.effect_44;

    //LAB_8010f9c0
    for(int i = 0; i < effect.count_00; i++) {
      final WsDragoonTransformationFeatherInstance70 feather = effect.featherArray_10[i];
      WsDragoonTransformationFeatherCallbacks_80119ff4[feather.callbackIndex_02].accept(manager, feather);
    }
    //LAB_8010f9fc
  }

  @Method(0x8010fa4cL)
  public static void initializeWsDragoonTransformationEffect(final EffectManagerData6c manager, final WsDragoonTransformationFeatherInstance70 feather) {
    feather.currentFrame_04++;

    if(feather.currentFrame_04 >= feather.countCallback0Frames_64) {
      feather.renderFeather_00 = true;
      feather.currentFrame_04 = 0;
      feather.callbackIndex_02++;
    }
  }

  @Method(0x8010fa88L)
  public static void expandWsDragoonTransformationEffect(final EffectManagerData6c manager, final WsDragoonTransformationFeatherInstance70 feather) {
    feather.angle_58 += feather.angleStep_60;
    feather.velocityTranslationMagnitudeXz_40 += feather.accelerationTranslationMagnitudeXz_44;
    feather.translationMagnitudeXz_3c += feather.velocityTranslationMagnitudeXz_40;
    final int x = (rcos(feather.angle_58 + feather.angleNoiseXz_5c >> 8) - rsin(feather.angle_58 + feather.angleNoiseXz_5c >> 8)) * (feather.translationMagnitudeXz_3c >> 8) >> 4;
    final int y = feather.yOrigin_54 + (feather.translationMagnitudeY_50 * rsin(feather.angle_58 >> 8 & 0xfff) >> 12);
    final int z = (rcos(feather.angle_58 + feather.angleNoiseXz_5c >> 8) + rsin(feather.angle_58 + feather.angleNoiseXz_5c >> 8)) * (feather.translationMagnitudeXz_3c >> 8) >> 4;
    feather.translation_08.set(x, y, z);

    feather.currentFrame_04++;
    if(feather.currentFrame_04 >= feather.countCallback1and3Frames_4c) {
      feather.currentFrame_04 = 0;
      feather.callbackIndex_02++;
    }
    //LAB_8010fbc0
  }

  @Method(0x8010fbd4L)
  public static void spinWsDragoonTransformationEffect(final EffectManagerData6c manager, final WsDragoonTransformationFeatherInstance70 feather) {
    feather.angle_58 += feather.angleStep_60;
    final int x = (rcos(feather.angle_58 + feather.angleNoiseXz_5c >> 8) - rsin(feather.angle_58 + feather.angleNoiseXz_5c >> 8)) * (feather.translationMagnitudeXz_3c >> 8) >> 4;
    final int y = feather.yOrigin_54 + (feather.translationMagnitudeY_50 * rsin(feather.angle_58 >> 8 & 0xfff) >> 12);
    final int z = (rcos(feather.angle_58 + feather.angleNoiseXz_5c >> 8) + rsin(feather.angle_58 + feather.angleNoiseXz_5c >> 8)) * (feather.translationMagnitudeXz_3c >> 8) >> 4;
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
  public static void contractWsDragoonTransformationEffect(final EffectManagerData6c manager, final WsDragoonTransformationFeatherInstance70 feather) {
    feather.angle_58 += feather.angleStep_60;
    feather.translationMagnitudeXz_3c += feather.velocityTranslationMagnitudeXz_40;
    final int x = (rcos(feather.angle_58 + feather.angleNoiseXz_5c >> 8) - rsin(feather.angle_58 + feather.angleNoiseXz_5c >> 8)) * (feather.translationMagnitudeXz_3c >> 8) >> 4;
    final int y = feather.yOrigin_54 + (feather.translationMagnitudeY_50 * rsin(feather.angle_58 >> 8 & 0xfff) >> 12);
    final int z = (rcos(feather.angle_58 + feather.angleNoiseXz_5c >> 8) + rsin(feather.angle_58 + feather.angleNoiseXz_5c >> 8)) * (feather.translationMagnitudeXz_3c >> 8) >> 4;
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
  public static void WsDragoonTransformationCallback4(final EffectManagerData6c manager, final WsDragoonTransformationFeatherInstance70 a2) {
    // no-op
  }

  @Method(0x8010ff10L)
  public static void tickMoonlightStarsEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
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
        star.toggleOffFrameThreshold_38 = (short)(seed_800fa754.advance().get() % (star.maxToggleFrameThreshold_36 + 1));
      } else {
        //LAB_8010ffb0
        star.renderStars_03 = true;
      }
    }
  }

  @Method(0x80110030L)
  public static VECTOR getScriptedObjectTranslation(final int scriptIndex) {
    final BattleScriptDataBase a0 = (BattleScriptDataBase)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00;

    if(BattleScriptDataBase.EM__.equals(a0.magic_00)) {
      return ((EffectManagerData6c)a0)._10.trans_04;
    }

    //LAB_8011006c
    return ((BattleObject27c)a0).model_148.coord2_14.coord.transfer;
  }

  @Method(0x80110074L)
  public static SVECTOR getScriptedObjectRotation(final int scriptIndex) {
    final BattleScriptDataBase data = (BattleScriptDataBase)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00;
    return BattleScriptDataBase.EM__.equals(data.magic_00) ? ((EffectManagerData6c)data)._10.rot_10 : ((BattleObject27c)data).model_148.coord2Param_64.rotate;
  }

  @Method(0x801100b8L)
  public static void getScriptedObjectRotationAndTranslation(final int scriptIndex, final Ref<SVECTOR> rotation, final Ref<VECTOR> translation) {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0[scriptIndex];
    final BattleScriptDataBase obj = (BattleScriptDataBase)state.innerStruct_00;

    if(BattleScriptDataBase.EM__.equals(obj.magic_00)) {
      final EffectManagerData6c manager = (EffectManagerData6c)obj;
      translation.set(manager._10.trans_04);
      rotation.set(manager._10.rot_10);
      return;
    }

    //LAB_801100fc
    final BattleObject27c bobj = (BattleObject27c)obj;
    translation.set(bobj.model_148.coord2_14.coord.transfer);
    rotation.set(bobj.model_148.coord2Param_64.rotate);
  }

  /** Used in the item throwing parabolic */
  @Method(0x80110120L)
  public static SVECTOR FUN_80110120(final SVECTOR rotation, @Nullable VECTOR translation, final VECTOR in) {
    if(translation == null) {
      translation = new VECTOR();
    }

    //LAB_8011014c
    final VECTOR sp0x10 = new VECTOR().set(in).sub(translation);
    rotation.setZ((short)0);
    rotation.setY((short)ratan2(sp0x10.getX(), sp0x10.getZ()));

    final int s1 = rcos(-rotation.getY()) * sp0x10.getZ() - rsin(-rotation.getY()) * sp0x10.getX();
    rotation.setX((short)ratan2(-sp0x10.getY(), s1 / 0x1000));
    return rotation;
  }

  /** Transform rotation vector of script using rotation and translation of second */
  @Method(0x80110228L)
  public static SVECTOR FUN_80110228(final SVECTOR rotation, @Nullable VECTOR translation1, final VECTOR translation2) {
    if(translation1 == null) {
      translation1 = new VECTOR();
    }

    //LAB_80110258
    final VECTOR sp0x10 = new VECTOR().set(translation2).sub(translation1).negate();
    final SVECTOR sp0x30 = new SVECTOR();
    sp0x30.setY((short)ratan2(sp0x10.getX(), sp0x10.getZ()));

    final int s1 = rcos(-sp0x30.getY()) * sp0x10.getZ() - rsin(-sp0x30.getY()) * sp0x10.getX();
    sp0x30.setX((short)ratan2(-sp0x10.getY(), s1 / 0x1000));

    final MATRIX transforms = new MATRIX();
    RotMatrix_Zyx(sp0x30, transforms);
    getRotationFromTransforms(rotation, transforms);

    return rotation;
  }

  @Method(0x8011035cL)
  public static void FUN_8011035c(final int scriptIndex1, final int scriptIndex2, final VECTOR a2) {
    final VECTOR translation1 = getScriptedObjectTranslation(scriptIndex1);

    if(scriptIndex2 == -1) {
      a2.set(translation1);
    } else {
      //LAB_801103b8
      final Ref<SVECTOR> rotation2 = new Ref<>();
      final Ref<VECTOR> translation2 = new Ref<>();
      getScriptedObjectRotationAndTranslation(scriptIndex2, rotation2, translation2);

      final VECTOR translationDelta = new VECTOR().set(translation1).sub(translation2.get());
      final MATRIX sp0x38 = new MATRIX();
      RotMatrix_Xyz(rotation2.get(), sp0x38);

      final VECTOR sp0x28 = new VECTOR();
      sp0x38.transpose();
      translationDelta.mul(sp0x38, sp0x28);
      a2.set(sp0x28);
    }

    //LAB_80110450
  }

  @Method(0x80110488L)
  public static void FUN_80110488(final int scriptIndex1, final int scriptIndex2, final VECTOR s1) {
    final MATRIX transforms1 = new MATRIX();
    FUN_800e8594(transforms1, (EffectManagerData6c)scriptStatePtrArr_800bc1c0[scriptIndex1].innerStruct_00);

    if(scriptIndex2 == -1) {
      s1.set(transforms1.transfer);
    } else {
      //LAB_80110500
      final MATRIX transforms2 = new MATRIX();
      FUN_800e8594(transforms2, (EffectManagerData6c)scriptStatePtrArr_800bc1c0[scriptIndex2].innerStruct_00);
      transforms1.transfer.sub(transforms2.transfer);

      transforms2.transpose();
      transforms1.transfer.mul(transforms2, s1);
    }

    //LAB_80110594
  }

  @Method(0x801105ccL)
  public static void getTranslationWithRotation(final VECTOR out, final int scriptIndex, final VECTOR in) {
    final Ref<SVECTOR> rotation = new Ref<>();
    final Ref<VECTOR> translation = new Ref<>();
    getScriptedObjectRotationAndTranslation(scriptIndex, rotation, translation);

    final MATRIX rotMatrix = new MATRIX();
    RotMatrix_Xyz(rotation.get(), rotMatrix);

    in.mul(rotMatrix, out);
    out.add(translation.get());
  }

  /** Sets translation on script, from second script if one specified */
  @Method(0x8011066cL)
  public static BattleScriptDataBase setRelativeTranslation(final int scriptIndex1, final int scriptIndex2, final VECTOR translation) {
    final BattleScriptDataBase obj = (BattleScriptDataBase)scriptStatePtrArr_800bc1c0[scriptIndex1].innerStruct_00;

    if(BattleScriptDataBase.EM__.equals(obj.magic_00) && (((EffectManagerData6c)obj).flags_04 & 0x2) != 0) {
      FUN_800e8d04((EffectManagerData6c)obj, 0x1L);
    }

    //LAB_801106dc
    final VECTOR objTranslation = getScriptedObjectTranslation(scriptIndex1);
    if(scriptIndex2 == -1) {
      objTranslation.set(translation);
    } else {
      //LAB_80110718
      getTranslationWithRotation(objTranslation, scriptIndex2, translation);
    }

    //LAB_80110720
    return obj;
  }

  /**
   * Ticks translation scaler, applying additional translation from translation and rotation
   * of a second effect if one specified in scaler
   */
  @Method(0x80110740L)
  public static int tickTranslationScalerWithRotation(final EffectManagerData6c manager, final TransformScalerEffect34 scaler) {
    scaler.velocity_18.add(scaler.acceleration_24);
    scaler.value_0c.add(scaler.velocity_18);

    if(scaler.scriptIndex_30 == -1) {
      manager._10.trans_04.set(scaler.value_0c).shra(8);
    } else {
      //LAB_80110814
      final Ref<SVECTOR> scriptRot = new Ref<>();
      final Ref<VECTOR> scriptTrans = new Ref<>();
      getScriptedObjectRotationAndTranslation(scaler.scriptIndex_30, scriptRot, scriptTrans);

      final MATRIX rotMatrix = new MATRIX();
      RotMatrix_Xyz(scriptRot.get(), rotMatrix);

      final VECTOR transScalerValue = new VECTOR().set(scaler.value_0c).shra(8);
      transScalerValue.mul(rotMatrix, manager._10.trans_04);
      manager._10.trans_04.add(scriptTrans.get());
    }

    //LAB_801108bc
    if(scaler.stepTicker_32 != -1) {
      scaler.stepTicker_32--;

      if(scaler.stepTicker_32 <= 0) {
        //LAB_801108e4
        return 0;
      }
    }

    //LAB_801108e0
    return 1;
  }

  @Method(0x801108fcL)
  public static TransformScalerEffect34 FUN_801108fc(final int a0, final int scriptIndex, final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
    final EffectManagerData6c s0 = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[a0].innerStruct_00;
    if((s0.flags_04 & 0x2) != 0) {
      FUN_800e8d04(s0, 0x1L);
    }

    //LAB_80110980
    final TransformScalerEffect34 s2 = FUN_800e8dd4(s0, 1, 0, SEffe::tickTranslationScalerWithRotation, 0x34, new TransformScalerEffect34());
    s2.value_0c.set(s0._10.trans_04.getX() << 8, s0._10.trans_04.getY() << 8, s0._10.trans_04.getZ() << 8);
    s2.scriptIndex_30 = -1;
    s2.stepTicker_32 = -1;

    final int transformedX1;
    final int transformedY1;
    final int transformedZ1;
    final int transformedX2;
    final int transformedY2;
    final int transformedZ2;
    if(scriptIndex != -1) {
      final MATRIX rotation = new MATRIX();
      RotMatrix_Xyz(getScriptedObjectRotation(scriptIndex), rotation);
      final VECTOR sp0x38 = new VECTOR().set(x1, y1, z1);
      final VECTOR sp0x48 = new VECTOR();
      sp0x38.mul(rotation, sp0x48);
      transformedX1 = sp0x48.getX();
      transformedY1 = sp0x48.getY();
      transformedZ1 = sp0x48.getZ();
      sp0x38.set(x2, y2, z2);
      sp0x38.mul(rotation, sp0x48);
      transformedX2 = sp0x48.getX();
      transformedY2 = sp0x48.getY();
      transformedZ2 = sp0x48.getZ();
    } else {
      transformedX1 = x1;
      transformedY1 = y1;
      transformedZ1 = z1;
      transformedX2 = x2;
      transformedY2 = y2;
      transformedZ2 = z2;
    }

    //LAB_80110a5c
    s2.velocity_18.set(transformedX1, transformedY1, transformedZ1);
    s2.acceleration_24.set(transformedX2, transformedY2, transformedZ2);
    return s2;
  }

  /** Sets translation scaler with additional scaling based on translation and rotation of a second script */
  @Method(0x80110aa8L)
  public static TransformScalerEffect34 setTranslationScalerWithRotation(final int transformType, final int scriptIndex1, final int scriptIndex2, final int stepCount, final int x, final int y, final int z) {
    if(stepCount < 0) {
      return null;
    }

    //LAB_80110afc
    final EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[scriptIndex1].innerStruct_00;
    if((manager.flags_04 & 0x2) != 0) {
      FUN_800e8d04(manager, 0x1L);
    }

    final VECTOR translationVelocity = new VECTOR();

    //LAB_80110b38
    final TransformScalerEffect34 translationScaler = FUN_800e8dd4(manager, 1, 0, SEffe::tickTranslationScalerWithRotation, 0x34, new TransformScalerEffect34());
    if(scriptIndex2 == -1) {
      translationVelocity.set(x, y, z)
        .sub(getScriptedObjectTranslation(scriptIndex1));
      //LAB_80110b9c
    } else if(transformType == 0) {  // XYZ minus script 1 translation + script 2 translation
      //LAB_80110bc0
      translationVelocity.set(x, y, z)
        .sub(getScriptedObjectTranslation(scriptIndex1))
        .add(getScriptedObjectTranslation(scriptIndex2));
    } else if(transformType == 1) {  // XYZ minus script 1 translation + script 2 translation with rotation
      //LAB_80110c0c
      getTranslationWithRotation(translationVelocity, scriptIndex2, new VECTOR().set(x, y, z));
      translationVelocity
        .sub(getScriptedObjectTranslation(scriptIndex1));
    }

    //LAB_80110c6c
    translationScaler.scriptIndex_30 = -1;
    translationScaler.stepTicker_32 = (short)stepCount;

    translationScaler.value_0c.setX(manager._10.trans_04.getX() << 8);
    translationScaler.value_0c.setY(manager._10.trans_04.getY() << 8);
    translationScaler.value_0c.setZ(manager._10.trans_04.getZ() << 8);

    if(stepCount != 0) {
      translationScaler.velocity_18.setX((translationVelocity.getX() << 8) / stepCount);
      translationScaler.velocity_18.setY((translationVelocity.getY() << 8) / stepCount);
      translationScaler.velocity_18.setZ((translationVelocity.getZ() << 8) / stepCount);
    } else {
      translationScaler.velocity_18.set(-1, -1, -1);
    }

    translationScaler.acceleration_24.set(0, 0, 0);

    //LAB_80110d04
    return translationScaler;
  }

  @Method(0x80110d34L)
  public static TransformScalerEffect34 FUN_80110d34(final int a0, final int scriptIndex1, final int scriptIndex2, final int a3, final int x, final int y, final int z) {
    final EffectManagerData6c s1 = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[scriptIndex1].innerStruct_00;
    if((s1.flags_04 & 0x2) != 0) {
      FUN_800e8d04(s1, 1);
    }

    //LAB_80110db8
    final TransformScalerEffect34 s0 = FUN_800e8dd4(s1, 1, 0, SEffe::tickTranslationScalerWithRotation, 0x34, new TransformScalerEffect34());
    s0.value_0c.setX(s1._10.trans_04.getX() << 8);
    s0.value_0c.setY(s1._10.trans_04.getY() << 8);
    s0.value_0c.setZ(s1._10.trans_04.getZ() << 8);
    s0.scriptIndex_30 = -1;

    if(a3 <= 0) {
      s0.stepTicker_32 = -1;
      s0.velocity_18.set(0, 0, 0);
    } else {
      //LAB_80110e30
      final VECTOR sp0x18 = new VECTOR();

      if(scriptIndex2 == -1) {
        sp0x18.set(x, y, z)
          .sub(getScriptedObjectTranslation(scriptIndex1));
        //LAB_80110e70
      } else if(a0 == 0) {
        //LAB_80110e94
        sp0x18.set(x, y, z)
          .sub(getScriptedObjectTranslation(scriptIndex1))
          .add(getScriptedObjectTranslation(scriptIndex2));
      } else if(a0 == 1) {
        //LAB_80110ee0
        getTranslationWithRotation(sp0x18, scriptIndex2, new VECTOR().set(x, y, z));
        sp0x18
          .sub(getScriptedObjectTranslation(scriptIndex1));
      }

      //LAB_80110f38
      s0.stepTicker_32 = (short)((sp0x18.length() << 8) / a3);
      if(s0.stepTicker_32 == 0) {
        s0.stepTicker_32 = 1;
      }

      //LAB_80110f80
      s0.velocity_18.setX((sp0x18.getX() << 8) / s0.stepTicker_32);
      s0.velocity_18.setY((sp0x18.getY() << 8) / s0.stepTicker_32);
      s0.velocity_18.setZ((sp0x18.getZ() << 8) / s0.stepTicker_32);
    }

    //LAB_80110fec
    s0.acceleration_24.set(0, 0, 0);
    return s0;
  }

  @Method(0x8011102cL)
  public static TransformScalerEffect34 FUN_8011102c(final int scriptIndex1, final int scriptIndex2) {
    final EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[scriptIndex1].innerStruct_00;

    if((manager.flags_04 & 0x2) != 0) {
      FUN_800e8d04(manager, 1);
    }

    //LAB_80111084
    final TransformScalerEffect34 effect = FUN_800e8dd4(manager, 1, 0, SEffe::tickTranslationScalerWithRotation, 0x34, new TransformScalerEffect34());
    final VECTOR v1 = getScriptedObjectTranslation(scriptIndex1);
    final VECTOR v2 = getScriptedObjectTranslation(scriptIndex2);
    final VECTOR sp0x18 = new VECTOR().set(v1).sub(v2);
    effect.value_0c.set(sp0x18);
    effect.scriptIndex_30 = scriptIndex2;
    effect.stepTicker_32 = -1;
    effect.velocity_18.set(0, 0, 0);
    effect.acceleration_24.set(0, 0, 0);
    return effect;
  }

  @Method(0x801114b8L)
  public static FlowControl FUN_801114b8(final RunningScript<?> script) {
    throw new RuntimeException("Bugged effect allocator");
  }

  @Method(0x801115ecL)
  public static FlowControl FUN_801115ec(final RunningScript<?> script) {
    final VECTOR sp0x10 = new VECTOR();
    FUN_8011035c(script.params_20[0].get(), script.params_20[1].get(), sp0x10);
    script.params_20[2].set(sp0x10.getX());
    script.params_20[3].set(sp0x10.getY());
    script.params_20[4].set(sp0x10.getZ());
    return FlowControl.CONTINUE;
  }

  @Method(0x80111658L)
  public static FlowControl FUN_80111658(final RunningScript<?> script) {
    final VECTOR sp0x10 = new VECTOR();
    FUN_80110488(script.params_20[0].get(), script.params_20[1].get(), sp0x10);
    script.params_20[2].set(sp0x10.getX());
    script.params_20[3].set(sp0x10.getY());
    script.params_20[4].set(sp0x10.getZ());
    return FlowControl.CONTINUE;
  }

  @Method(0x801116c4L)
  public static MATRIX FUN_801116c4(final MATRIX a0, final int scriptIndex, final int a2) {
    final VECTOR trans = new VECTOR();
    final VECTOR scale = new VECTOR();
    final SVECTOR rot = new SVECTOR();
    final MATRIX transforms = new MATRIX();
    final BattleScriptDataBase s0 = (BattleScriptDataBase)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00;
    if(BattleScriptDataBase.EM__.equals(s0.magic_00)) {
      final EffectManagerData6c effects = (EffectManagerData6c)s0;

      //LAB_8011172c
      final MATRIX sp0x10 = new MATRIX();
      FUN_800e8594(sp0x10, effects);
      final int type = effects.flags_04 & 0xff00_0000;

      //LAB_80111768
      if(type == 0x100_0000 || type == 0x200_0000) {
        //LAB_80111998
        final BttlScriptData6cSub13c struct13c = (BttlScriptData6cSub13c)effects.effect_44;
        final Model124 model = struct13c.model_134;
        model.coord2_14.flg = 0;
        model.coord2_14.coord.set(sp0x10);

        //LAB_80111a0c
        final GsCOORDINATE2 coord2 = model.coord2ArrPtr_04[a2];
        GsGetLw(coord2, a0);
        coord2.flg = 0;
      } else if(type == 0) {
        //LAB_80111778
        final BttlScriptData6cSub5c a2_0 = (BttlScriptData6cSub5c)effects.effect_44;

        if((a2_0.lmbType_00 & 0x7) == 0) {
          final LmbType0 lmb = (LmbType0)a2_0.lmb_0c;

          //LAB_801117ac
          final int a0_0 = Math.max(0, effects._10._24) % (a2_0._08 * 2);
          final int a1_0 = a0_0 / 2;
          final LmbTransforms14 lmbTransforms = lmb._08[a2]._08[a1_0];
          scale.set(lmbTransforms.scale_00);
          trans.set(lmbTransforms.trans_06);
          rot.set(lmbTransforms.rot_0c);

          if((a0_0 & 0x1) != 0) {
            int v1 = a1_0 + 1;

            if(v1 == a2_0._08) {
              v1 = 0;
            }

            //LAB_8011188c
            final LmbTransforms14 a0_1 = lmb._08[a2]._08[v1];
            scale.add(a0_1.scale_00).div(2);
            trans.add(a0_1.trans_06).div(2);
          }

          //LAB_80111958
          RotMatrix_Zyx(rot, transforms);
          transforms.transfer.set(trans);
          transforms.scaleL(scale);
          transforms.compose(sp0x10, a0);
        }
      }
    } else {
      final Model124 model = ((BattleObject27c)s0).model_148;
      applyModelRotationAndScale(model);
      final GsCOORDINATE2 coord2 = model.coord2ArrPtr_04[a2];
      GsGetLw(coord2, a0);
      coord2.flg = 0;
    }

    //LAB_80111a3c
    return a0;
  }

  @Method(0x80111a58L)
  public static FlowControl FUN_80111a58(final RunningScript<?> script) {
    final int a2 = script.params_20[1].get();
    if(a2 == -1) {
      //LAB_80111acc
      return FUN_80111658(script);
    }

    final MATRIX sp0x20 = new MATRIX();
    FUN_801116c4(sp0x20, script.params_20[0].get(), a2);
    script.params_20[2].set(sp0x20.transfer.getX());
    script.params_20[3].set(sp0x20.transfer.getY());
    script.params_20[4].set(sp0x20.transfer.getZ());

    //LAB_80111ad4
    return FlowControl.CONTINUE;
  }

  @Method(0x80111ae4L)
  public static FlowControl scriptSetRelativeTranslation(final RunningScript<?> script) {
    final VECTOR sp0x10 = new VECTOR().set(script.params_20[2].get(), script.params_20[3].get(), script.params_20[4].get());
    setRelativeTranslation(script.params_20[0].get(), script.params_20[1].get(), sp0x10);
    return FlowControl.CONTINUE;
  }

  @Method(0x80111b60L)
  public static FlowControl FUN_80111b60(final RunningScript<?> script) {
    final VECTOR translation = getScriptedObjectTranslation(script.params_20[0].get());
    final DVECTOR sp0x10 = new DVECTOR();
    script.params_20[3].set(FUN_800e7dbc(sp0x10, translation) << 2);
    script.params_20[1].set(sp0x10.getX());
    script.params_20[2].set(sp0x10.getY());
    return FlowControl.CONTINUE;
  }

  @Method(0x80111be8L)
  public static FlowControl FUN_80111be8(final RunningScript<?> script) {
    final EffectManagerData6c data = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    return (data.flags_04 & 0x2) != 0 ? FlowControl.PAUSE_AND_REWIND : FlowControl.CONTINUE;
  }

  @Method(0x80111c2cL)
  public static FlowControl FUN_80111c2c(final RunningScript<?> script) {
    FUN_801108fc(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get(), script.params_20[7].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x80111cc4L)
  public static FlowControl FUN_80111cc4(final RunningScript<?> script) {
    final int a1 = script.params_20[0].get();
    final int s0 = script.params_20[1].get();
    int s4 = script.params_20[2].get();
    int s6 = script.params_20[3].get();
    int s3 = script.params_20[4].get();
    int s5 = script.params_20[5].get();
    int s7 = script.params_20[6].get();
    int fp = script.params_20[7].get();

    final EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[a1].innerStruct_00;
    if((manager.flags_04 & 0x2) == 0) {
      FUN_801108fc(a1, s0, s4, s6, s3, s5, s7, fp);
    } else {
      //LAB_80111dac
      final TransformScalerEffect34 s2 = (TransformScalerEffect34)FUN_800e8c84(manager, 1);

      if(s2._06 == 0) {
        if(s0 != -1) {
          final MATRIX sp0x20 = new MATRIX();
          RotMatrix_Xyz(getScriptedObjectRotation(s0), sp0x20);

          VECTOR sp0x50 = new VECTOR().set(s4, s6, s3).mul(sp0x20);
          s4 = sp0x50.getX();
          s6 = sp0x50.getY();
          s3 = sp0x50.getZ();

          sp0x50 = new VECTOR().set(s5, s7, fp).mul(sp0x20);
          s5 = sp0x50.getX();
          s7 = sp0x50.getY();
          fp = sp0x50.getZ();
        }

        //LAB_80111e40
        s2.velocity_18.x.add(s4);
        s2.velocity_18.y.add(s6);
        s2.velocity_18.z.add(s3);
        s2.acceleration_24.x.add(s5);
        s2.acceleration_24.y.add(s7);
        s2.acceleration_24.z.add(fp);
      }
    }

    //LAB_80111ea4
    return FlowControl.CONTINUE;
  }

  /**
   * Does some kind of transformation on the velocity and acceleration of a TransformScaler.
   * Rotation appears to be involved as well. Used in the item throwing parabolic.
   */
  @Method(0x80111ed4L)
  public static FlowControl FUN_80111ed4(final RunningScript<?> script) {
    final int scriptIndex = script.params_20[0].get();
    final EffectManagerData6c data = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00;

    if((data.flags_04 & 0x2) != 0) {
      final TransformScalerEffect34 s3 = (TransformScalerEffect34)FUN_800e8c84(data, 1);

      if(s3 != null && s3.stepTicker_32 != -1 && s3._06 == 0) {
        final VECTOR translation = getScriptedObjectTranslation(scriptIndex);
        int s0 = s3.stepTicker_32;

        final VECTOR sp0x10 = new VECTOR().set(
          (s0 * s3.acceleration_24.getX() / 2 + s3.velocity_18.getX()) * s0 + s3.value_0c.getX() >> 8,
          (s0 * s3.acceleration_24.getY() / 2 + s3.velocity_18.getY()) * s0 + s3.value_0c.getY() >> 8,
          (s0 * s3.acceleration_24.getZ() / 2 + s3.velocity_18.getZ()) * s0 + s3.value_0c.getZ() >> 8
        );

        final SVECTOR rotation = new SVECTOR();
        FUN_80110120(rotation, translation, sp0x10);

        final MATRIX rotMatrix = new MATRIX();
        RotMatrix_Zyx(rotation, rotMatrix);

        final VECTOR inVec = new VECTOR().set(
          script.params_20[2].get(),
          script.params_20[3].get(),
          script.params_20[4].get()
        );

        final VECTOR sp0x58 = new VECTOR();
        inVec.mul(rotMatrix, sp0x58);
        s0++;
        s3.velocity_18.x.sub(sp0x58.getX() * s0 / 2);
        s3.velocity_18.y.sub(sp0x58.getY() * s0 / 2);
        s3.velocity_18.z.sub(sp0x58.getZ() * s0 / 2);
        s3.acceleration_24.add(sp0x58);
      }
    }

    //LAB_8011215c
    return FlowControl.CONTINUE;
  }

  @Method(0x80112184L)
  public static FlowControl scriptSetTranslationScalerWithRotation0(final RunningScript<?> script) {
    setTranslationScalerWithRotation(0, script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x801121fcL)
  public static FlowControl scriptSetTranslationScalerWithRotation1(final RunningScript<?> script) {
    setTranslationScalerWithRotation(1, script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x80112274L)
  public static FlowControl FUN_80112274(final RunningScript<?> script) {
    FUN_80110d34(0, script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x801122ecL)
  public static FlowControl FUN_801122ec(final RunningScript<?> script) {
    FUN_80110d34(1, script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x80112364L)
  public static FlowControl FUN_80112364(final RunningScript<?> script) {
    FUN_8011102c(script.params_20[0].get(), script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x80112398L)
  public static FlowControl FUN_80112398(final RunningScript<?> script) {
    final EffectManagerData6c a0 = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final VECTOR sp0x10 = new VECTOR();

    if((a0.flags_04 & 0x2) != 0) {
      final TransformScalerEffect34 v1 = (TransformScalerEffect34)FUN_800e8c84(a0, 1);

      if(v1._06 == 0) {
        sp0x10.set(v1.velocity_18);
      }
    }

    //LAB_80112430
    script.params_20[2].set(sp0x10.getX());
    script.params_20[3].set(sp0x10.getY());
    script.params_20[4].set(sp0x10.getZ());
    return FlowControl.CONTINUE;
  }

  @Method(0x80112474L)
  public static void getRotationDifference(final int scriptIndex1, final int scriptIndex2, final SVECTOR outRotation) {
    final SVECTOR rotation = getScriptedObjectRotation(scriptIndex1);

    if(scriptIndex2 == -1) {
      outRotation.set(rotation);
    } else {
      //LAB_801124c8
      outRotation.set(rotation).sub(getScriptedObjectRotation(scriptIndex2)).and(0xfff);
    }

    //LAB_80112518
  }

  /** Sets rotation on script from given vector, adding from second script if one is specified */
  @Method(0x80112530L)
  public static long setRelativeRotation(final int scriptIndex1, final int scriptIndex2, final SVECTOR inVec) {
    final EffectManagerData6c data = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[scriptIndex1].innerStruct_00;

    if(BattleScriptDataBase.EM__.equals(data.magic_00) && (data.flags_04 & 0x4) != 0) {
      FUN_800e8d04(data, 0x2L);
    }

    // scriptIndex1Rotation cannot be factored out of condition because scriptIndex 1 and 2 can
    // refer to the same script. In this scenario, setting the rotation of scriptIndex1 outside
    // the condition will cause the script to always be set to 2 * inVec instead of rotation + inVec.
    //LAB_8011259c
    final SVECTOR scriptIndex1Rotation = getScriptedObjectRotation(scriptIndex1);
    if(scriptIndex2 == -1) {
      scriptIndex1Rotation.set(inVec);
    } else {
      //LAB_801125d8
      scriptIndex1Rotation.set(getScriptedObjectRotation(scriptIndex2)).add(inVec);
    }

    //LAB_8011261c
    return 0;
  }

  /** Scales rotation vector for altering effect orientation */
  @Method(0x80112638L)
  public static int tickRotationScaler(final EffectManagerData6c manager, final TransformScalerEffect34 scaler) {
    scaler.velocity_18.add(scaler.acceleration_24);
    scaler.value_0c.add(scaler.velocity_18);
    manager._10.rot_10.set(scaler.value_0c);
    if(scaler.stepTicker_32 != -1) {
      scaler.stepTicker_32--;

      if(scaler.stepTicker_32 <= 0) {
        //LAB_801126f8
        return 0;
      }
    }

    //LAB_801126fc
    return 1;
  }

  @Method(0x80112704L)
  public static FlowControl scriptGetRotationDifference(final RunningScript<?> script) {
    final SVECTOR sp0x10 = new SVECTOR();
    getRotationDifference(script.params_20[0].get(), script.params_20[1].get(), sp0x10);
    script.params_20[2].set(sp0x10.getX());
    script.params_20[3].set(sp0x10.getY());
    script.params_20[4].set(sp0x10.getZ());
    return FlowControl.CONTINUE;
  }

  @Method(0x80112770L)
  public static FlowControl scriptSetRelativeRotation(final RunningScript<?> script) {
    setRelativeRotation(
      script.params_20[0].get(),
      script.params_20[1].get(),
      new SVECTOR().set(
        (short)script.params_20[2].get(),
        (short)script.params_20[3].get(),
        (short)script.params_20[4].get()
      )
    );

    return FlowControl.CONTINUE;
  }

  @Method(0x801127e0L)
  public static FlowControl FUN_801127e0(final RunningScript<?> script) {
    final MATRIX transforms = new MATRIX();
    FUN_800e8594(transforms, (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00);

    final SVECTOR rot = new SVECTOR();
    getRotationFromTransforms(rot, transforms);
    script.params_20[2].set(rot.getX());
    script.params_20[3].set(rot.getY());
    script.params_20[4].set(rot.getZ());
    return FlowControl.CONTINUE;
  }

  @Method(0x8011287cL)
  public static FlowControl FUN_8011287c(final RunningScript<?> script) {
    final SVECTOR rot = new SVECTOR();
    final MATRIX transforms = new MATRIX();
    FUN_801116c4(transforms, script.params_20[0].get(), script.params_20[1].get());
    getRotationFromTransforms(rot, transforms);
    script.params_20[2].set(rot.getX());
    script.params_20[3].set(rot.getY());
    script.params_20[4].set(rot.getZ());
    return FlowControl.CONTINUE;
  }

  @Method(0x80112900L)
  public static FlowControl FUN_80112900(final RunningScript<?> script) {
    final SVECTOR rot = new SVECTOR();
    FUN_80110228(rot, getScriptedObjectTranslation(script.params_20[0].get()), getScriptedObjectTranslation(script.params_20[1].get()));

    // XZY is the correct order
    script.params_20[2].set(rot.getX());
    script.params_20[3].set(rot.getZ());
    script.params_20[4].set(rot.getY());
    return FlowControl.CONTINUE;
  }

  /** Calculates rotation of one script based on position of a second script */
  @Method(0x8011299cL)
  public static FlowControl FUN_8011299c(final RunningScript<?> script) {
    final int targetScript = script.params_20[1].get();

    final VECTOR translationFinal = new VECTOR().set(script.params_20[2].get(), script.params_20[3].get(), script.params_20[4].get());
    final Ref<SVECTOR> rotation = new Ref<>(new SVECTOR());
    final Ref<VECTOR> translation1 = new Ref<>(new VECTOR());

    getScriptedObjectRotationAndTranslation(script.params_20[0].get(), rotation, translation1);

    if(targetScript != -1) {
      final Ref<SVECTOR> targetRotation = new Ref<>(new SVECTOR());
      final Ref<VECTOR> targetTranslation = new Ref<>(new VECTOR());

      getScriptedObjectRotationAndTranslation(targetScript, targetRotation, targetTranslation);

      final MATRIX rotMatrix = new MATRIX();
      RotMatrix_Xyz(targetRotation.get(), rotMatrix);
      translationFinal.mul(rotMatrix).add(targetTranslation.get());
    }

    //LAB_80112a80
    FUN_80110228(rotation.get(), translation1.get(), translationFinal);
    return FlowControl.CONTINUE;
  }

  /** Set rotation value and rate of change factors */
  @Method(0x80112aa4L)
  public static FlowControl scriptSetRotationScaler(final RunningScript<?> script) {
    final EffectManagerData6c s0 = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    if((s0.flags_04 & 0x4) != 0) {
      FUN_800e8d04(s0, 0x2L);
    }

    //LAB_80112b58
    final TransformScalerEffect34 v0 = FUN_800e8dd4(s0, 2, 0, SEffe::tickRotationScaler, 0x34, new TransformScalerEffect34());
    v0.scriptIndex_30 = -1;
    v0.stepTicker_32 = -1;
    v0.value_0c.set(s0._10.rot_10);
    v0.velocity_18.setX(script.params_20[2].get());
    v0.velocity_18.setY(script.params_20[3].get());
    v0.velocity_18.setZ(script.params_20[4].get());
    v0.acceleration_24.setX(script.params_20[5].get());
    v0.acceleration_24.setY(script.params_20[6].get());
    v0.acceleration_24.setZ(script.params_20[7].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x80112bf0L)
  public static FlowControl FUN_80112bf0(final RunningScript<?> script) {
    final int s0 = script.params_20[0].get();
    final int s4 = script.params_20[1].get();
    final int s2 = script.params_20[2].get();
    final int s5 = script.params_20[3].get();
    final int s6 = script.params_20[4].get();
    final int s7 = script.params_20[5].get();

    if(s2 >= 0) {
      final EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[s0].innerStruct_00;
      final TransformScalerEffect34 effect = FUN_800e8dd4(manager, 2, 0, SEffe::tickRotationScaler, 0x34, new TransformScalerEffect34());

      final SVECTOR v1 = getScriptedObjectRotation(s0);
      final int sp18;
      final int sp1c;
      final int sp20;
      if(s4 == -1) {
        sp18 = s5 - v1.getX();
        sp1c = s6 - v1.getY();
        sp20 = s7 - v1.getZ();
      } else {
        //LAB_80112ce4
        final SVECTOR v2 = getScriptedObjectRotation(s4);
        sp18 = v2.getX() + s5 - v1.getX();
        sp1c = v2.getY() + s6 - v1.getY();
        sp20 = v2.getZ() + s7 - v1.getZ();
      }

      //LAB_80112d48
      effect.scriptIndex_30 = -1;
      effect.stepTicker_32 = (short)s2;
      effect.value_0c.set(manager._10.rot_10);
      effect.value_0c.set(manager._10.rot_10);
      effect.value_0c.set(manager._10.rot_10);
      effect.velocity_18.setX(sp18 / s2);
      effect.velocity_18.setY(sp1c / s2);
      effect.velocity_18.setZ(sp20 / s2);
      effect.acceleration_24.set(0, 0, 0);
    }

    //LAB_80112dd0
    return FlowControl.CONTINUE;
  }

  @Method(0x80112e00L)
  public static FlowControl FUN_80112e00(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x8011306cL)
  public static FlowControl FUN_8011306c(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x801132c8L)
  public static FlowControl FUN_801132c8(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x8011357cL)
  public static FlowControl FUN_8011357c(final RunningScript<?> script) {
    final SVECTOR rot = new SVECTOR().set((short)script.params_20[1].get(), (short)script.params_20[2].get(), (short)script.params_20[3].get());
    final MATRIX transforms = new MATRIX();
    RotMatrix_Yxz(rot, transforms);
    getRotationFromTransforms(rot, transforms);
    script.params_20[4].set(rot.getX());
    script.params_20[5].set(rot.getY());
    script.params_20[6].set(rot.getZ());
    return FlowControl.CONTINUE;
  }

  /**
   * This may be bugged in retail when given a second script index,
   * as it gets the ratio of script index 1 to itself.
   */
  @Method(0x80113624L)
  public static SVECTOR getScaleRatio(final int scriptIndex1, final int scriptIndex2, final SVECTOR outScale) {
    ScriptState<?> a3 = scriptStatePtrArr_800bc1c0[scriptIndex1];
    BattleScriptDataBase t0 = (BattleScriptDataBase)a3.innerStruct_00;

    final SVECTOR t1;
    if(BattleScriptDataBase.EM__.equals(t0.magic_00)) {
      t1 = ((EffectManagerData6c)t0)._10.scale_16;
    } else {
      //LAB_80113660
      t1 = new SVECTOR().set(((BattleObject27c)t0).model_148.scaleVector_fc);
    }

    //LAB_801136a0
    if(scriptIndex2 == -1) {
      outScale.set(t1);
    } else {
      //LAB_801136d0
      a3 = scriptStatePtrArr_800bc1c0[scriptIndex1];
      t0 = (BattleScriptDataBase)a3.innerStruct_00;

      final SVECTOR svec;
      if(BattleScriptDataBase.EM__.equals(t0.magic_00)) {
        svec = ((EffectManagerData6c)t0)._10.scale_16;
      } else {
        //LAB_80113708
        svec = new SVECTOR().set(((BattleObject27c)t0).model_148.scaleVector_fc);
      }

      //LAB_80113744
      if(svec.getX() == 0) {
        svec.setX((short)1);
      }

      //LAB_80113758
      if(svec.getY() == 0) {
        svec.setY((short)1);
      }

      //LAB_8011376c
      if(svec.getZ() == 0) {
        svec.setZ((short)1);
      }

      //LAB_80113780
      outScale.setX((short)((t1.getX() << 12) / svec.getX()));
      outScale.setY((short)((t1.getY() << 12) / svec.getY()));
      outScale.setZ((short)((t1.getZ() << 12) / svec.getZ()));
    }

    //LAB_801137ec
    return outScale;
  }

  @Method(0x801137f8L)
  public static SVECTOR getScaleDifference(final int scriptIndex1, final int scriptIndex2, final SVECTOR a2) {
    ScriptState<?> a0_0 = scriptStatePtrArr_800bc1c0[scriptIndex1];
    BattleScriptDataBase a3 = (BattleScriptDataBase)a0_0.innerStruct_00;

    final SVECTOR t0;
    if(BattleScriptDataBase.EM__.equals(a3.magic_00)) {
      t0 = ((EffectManagerData6c)a3)._10.scale_16;
    } else {
      //LAB_80113834
      t0 = new SVECTOR().set(((BattleObject27c)a3).model_148.scaleVector_fc);
    }

    //LAB_80113874
    if(scriptIndex2 == -1) {
      a2.set(t0);
    } else {
      //LAB_801138a4
      a0_0 = scriptStatePtrArr_800bc1c0[scriptIndex2];
      a3 = (BattleScriptDataBase)a0_0.innerStruct_00;

      final SVECTOR a0_1;
      if(BattleScriptDataBase.EM__.equals(a3.magic_00)) {
        a0_1 = ((EffectManagerData6c)a3)._10.scale_16;
      } else {
        //LAB_801138dc
        a0_1 = new SVECTOR().set(((BattleObject27c)a3).model_148.scaleVector_fc);
      }

      //LAB_8011391c
      a2.set(t0).sub(a0_1);
    }

    //LAB_80113958
    return a2;
  }

  @Method(0x80113964L)
  public static FlowControl scriptGetScaleRatio(final RunningScript<?> script) {
    final SVECTOR sp0x10 = new SVECTOR();
    getScaleRatio(script.params_20[0].get(), script.params_20[1].get(), sp0x10);
    script.params_20[2].set(sp0x10.getX());
    script.params_20[3].set(sp0x10.getY());
    script.params_20[4].set(sp0x10.getZ());
    return FlowControl.CONTINUE;
  }

  /** Set model scale, transfer from second script if included */
  @Method(0x801139d0L)
  public static FlowControl scriptSetRelativeScale(final RunningScript<?> script) {
    final int t1 = script.params_20[0].get();
    final int a1 = script.params_20[1].get();
    final short x = (short)script.params_20[2].get();
    final short y = (short)script.params_20[3].get();
    final short z = (short)script.params_20[4].get();
    final SVECTOR sp0x00 = new SVECTOR();
    if(a1 == -1) {
      sp0x00.set(x, y, z);
    } else {
      //LAB_80113a28
      final ScriptState<?> state = scriptStatePtrArr_800bc1c0[a1];
      final BattleScriptDataBase a1_0 = (BattleScriptDataBase)state.innerStruct_00;

      final SVECTOR v1;
      if(BattleScriptDataBase.EM__.equals(a1_0.magic_00)) {
        v1 = ((EffectManagerData6c)a1_0)._10.scale_16;
      } else {
        //LAB_80113a64
        v1 = new SVECTOR().set(((BattleObject27c)a1_0).model_148.scaleVector_fc);
      }

      //LAB_80113aa0
      //LAB_80113abc
      //LAB_80113ae0
      //LAB_80113b04
      sp0x00.set((short)(x * v1.getX() >> 12), (short)(y * v1.getY() >> 12), (short)(z * v1.getZ() >> 12));
    }

    //LAB_80113b0c
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0[t1];
    final BattleScriptDataBase a0_0 = (BattleScriptDataBase)state.innerStruct_00;

    if(BattleScriptDataBase.EM__.equals(a0_0.magic_00)) {
      ((EffectManagerData6c)a0_0)._10.scale_16.set(sp0x00);
    } else {
      //LAB_80113b64
      ((BattleObject27c)a0_0).model_148.scaleVector_fc.set(sp0x00);
    }

    //LAB_80113b94
    return FlowControl.CONTINUE;
  }

  /** Scales scale vector for changing effect size */
  @Method(0x80113ba0L)
  public static int tickScaleScaler(final EffectManagerData6c manager, final TransformScalerEffect34 scaler) {
    scaler.velocity_18.add(scaler.acceleration_24);
    scaler.value_0c.add(scaler.velocity_18);
    manager._10.scale_16.set(scaler.value_0c);
    if(scaler.stepTicker_32 != -1) {
      scaler.stepTicker_32--;

      if(scaler.stepTicker_32 <= 0) {
        return 0;
      }
    }

    //LAB_80113c60
    //LAB_80113c64
    return 1;
  }

  /** Set scale value and rate of change factors */
  @Method(0x80113c6cL)
  public static FlowControl scriptSetScaleVectorScaler(final RunningScript<?> script) {
    final EffectManagerData6c scalerManager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

    if((scalerManager.flags_04 & 0x8) != 0) {
      FUN_800e8d04(scalerManager, 0x3L);
    }

    //LAB_80113d20
    final TransformScalerEffect34 scaleScaler = FUN_800e8dd4(scalerManager, 3, 0, SEffe::tickScaleScaler, 0x34, new TransformScalerEffect34());
    scaleScaler.scriptIndex_30 = -1;
    scaleScaler.stepTicker_32 = (short)-1;
    scaleScaler.value_0c.set(scalerManager._10.scale_16);
    scaleScaler.velocity_18.set(script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    scaleScaler.acceleration_24.set(script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get());
    return FlowControl.CONTINUE;
  }

  /**
   * Initializes scale vector scaler for expansion of guard-type effects.
   * has two types, not sure if both are for guard effects.
   */
  @Method(0x80113db8L)
  public static FlowControl FUN_80113db8(final long a0, final RunningScript<?> script) {
    final int s7 = script.params_20[0].get();
    final int s3 = script.params_20[1].get();
    final int s2 = script.params_20[2].get();
    final int s4 = script.params_20[3].get();
    final int s5 = script.params_20[4].get();
    final int s6 = script.params_20[5].get();

    if(s2 >= 0) {
      final EffectManagerData6c s1 = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[s7].innerStruct_00;

      if((s1.flags_04 & 0x8) != 0) {
        FUN_800e8d04(s1, 0x3L);
      }

      //LAB_80113e70
      final TransformScalerEffect34 s0 = FUN_800e8dd4(s1, 3, 0, SEffe::tickScaleScaler, 0x34, new TransformScalerEffect34());

      final VECTOR sp0x18 = new VECTOR().set(s4, s5, s6);
      if(a0 == 0) {
        //LAB_80113eac
        final SVECTOR sp0x28 = new SVECTOR();
        getScaleDifference(s7, s3, sp0x28);
        sp0x18.sub(sp0x28);
      } else if(a0 == 1) {
        //LAB_80113ee8
        if(s3 != -1) {
          //LAB_80113f04
          final SVECTOR sp0x28 = new SVECTOR();
          getScaleRatio(s3, -1, sp0x28);

          //LAB_80113f2c
          //LAB_80113f50
          //LAB_80113f74
          sp0x18.mul(sp0x28).div(0x1000);
        }

        //LAB_80113f7c
        //LAB_80113fb4
        sp0x18.sub(s1._10.scale_16);
      } else {
        throw new RuntimeException("Invalid a0 " + a0);
      }

      //LAB_80113fc0
      s0.scriptIndex_30 = -1;
      s0.stepTicker_32 = (short)s2;
      s0.value_0c.set(s1._10.scale_16);

      if(s2 != 0) {
        s0.velocity_18.set(sp0x18).div(s2);
      } else {
        s0.velocity_18.set(-1, -1, -1);
      }

      s0.acceleration_24.set(0, 0, 0);
    }

    //LAB_8011403c
    return FlowControl.CONTINUE;
  }

  @Method(0x80114070L)
  public static FlowControl FUN_80114070(final RunningScript<?> script) {
    return FUN_80113db8(0, script);
  }

  @Method(0x80114094L)
  public static FlowControl FUN_80114094(final RunningScript<?> script) {
    return FUN_80113db8(1, script);
  }

  @Method(0x801143f8L)
  public static FlowControl FUN_801143f8(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  /** Gets color from script, transfer from second script if included */
  @Method(0x8011441cL)
  public static long getColourDifference(final int scriptIndex1, final int scriptIndex2, final USCOLOUR outColour) {
    final BattleScriptDataBase data1 = (BattleScriptDataBase)scriptStatePtrArr_800bc1c0[scriptIndex1].innerStruct_00;
    final USCOLOUR c;
    if(BattleScriptDataBase.EM__.equals(data1.magic_00)) {
      c = ((EffectManagerData6c)data1)._10.colour_1c;
    } else {
      c = _800fb94c;
    }

    //LAB_80114480
    if(scriptIndex2 == -1) {
      outColour.set(c);
    } else {
      //LAB_801144b0
      final BattleScriptDataBase data2 = (BattleScriptDataBase)scriptStatePtrArr_800bc1c0[scriptIndex2].innerStruct_00;
      final USCOLOUR c2;
      if(BattleScriptDataBase.EM__.equals(data2.magic_00)) {
        c2 = ((EffectManagerData6c)data2)._10.colour_1c;
      } else {
        c2 = _800fb94c;
      }

      //LAB_801144e4
      outColour.set(c).sub(c2);
    }

    //LAB_80114520
    return 0;
  }

  @Method(0x8011452cL)
  public static FlowControl scriptGetColourDifference(final RunningScript<?> script) {
    final USCOLOUR sp0x10 = new USCOLOUR();
    getColourDifference(script.params_20[0].get(), script.params_20[1].get(), sp0x10);
    script.params_20[2].set(sp0x10.getX());
    script.params_20[3].set(sp0x10.getY());
    script.params_20[4].set(sp0x10.getZ());
    return FlowControl.CONTINUE;
  }

  @Method(0x80114598L)
  public static FlowControl scriptSetRelativeColour(final RunningScript<?> script) {
    BattleScriptDataBase a1 = (BattleScriptDataBase)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

    final USCOLOUR a3;
    if(BattleScriptDataBase.EM__.equals(a1.magic_00)) {
      a3 = ((EffectManagerData6c)a1)._10.colour_1c;
    } else {
      a3 = new USCOLOUR().set(_800fb94c);
    }

    //LAB_80114614
    if(script.params_20[1].get() == -1) {
      a3.setX(script.params_20[2].get() & 0xffff);
      a3.setY(script.params_20[3].get() & 0xffff);
      a3.setZ(script.params_20[4].get() & 0xffff);
    } else {
      //LAB_80114668
      a1 = (BattleScriptDataBase)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00;

      final USCOLOUR a2;
      if(BattleScriptDataBase.EM__.equals(a1.magic_00)) {
        a2 = ((EffectManagerData6c)a1)._10.colour_1c;
      } else {
        a2 = _800fb94c;
      }

      //LAB_8011469c
      a3.setX((script.params_20[2].get() & 0xffff) + a2.getX());
      a3.setY((script.params_20[3].get() & 0xffff) + a2.getY());
      a3.setZ((script.params_20[4].get() & 0xffff) + a2.getZ());
    }

    //LAB_801146f0
    return FlowControl.CONTINUE;
  }

  /** Scales color vector for fading color in/out */
  @Method(0x801146fcL)
  public static int tickColorScaler(final EffectManagerData6c manager, final TransformScalerEffect34 scaler) {
    scaler.velocity_18.add(scaler.acceleration_24);
    scaler.value_0c.add(scaler.velocity_18);
    manager._10.colour_1c.setX(scaler.value_0c.getX() >> 8 & 0xff);
    manager._10.colour_1c.setY(scaler.value_0c.getY() >> 8 & 0xff);
    manager._10.colour_1c.setZ(scaler.value_0c.getZ() >> 8 & 0xff);

    if(scaler.stepTicker_32 != -1) {
      scaler.stepTicker_32--;

      if(scaler.stepTicker_32 <= 0) {
        return 0;
      }
    }

    //LAB_801147bc
    //LAB_801147c0
    return 1;
  }

  @Method(0x801147c8L)
  public static FlowControl FUN_801147c8(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  /** Set color value and rate of change factors */
  @Method(0x80114920L)
  public static FlowControl scriptSetColorVectorScaler(final RunningScript<?> script) {
    final int scriptIndex1 = script.params_20[0].get();
    final int scriptIndex2 = script.params_20[1].get();
    final int scalerStepCount = script.params_20[2].get();
    final VECTOR velocityVec = new VECTOR().set(
      script.params_20[3].get(),
      script.params_20[4].get(),
      script.params_20[5].get()
    );

    if(scalerStepCount >= 0) {
      final EffectManagerData6c scalerManager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[scriptIndex1].innerStruct_00;
      if((scalerManager.flags_04 & 0x10) != 0) {
        FUN_800e8d04(scalerManager, 0x4L);
      }

      //LAB_801149d0
      final TransformScalerEffect34 colorScaler = FUN_800e8dd4(scalerManager, 4, 0, SEffe::tickColorScaler, 0x34, new TransformScalerEffect34());
      final USCOLOUR sp0x28 = new USCOLOUR();
      getColourDifference(scriptIndex1, scriptIndex2, sp0x28);
      colorScaler.scriptIndex_30 = -1;
      colorScaler.stepTicker_32 = (short)scalerStepCount;
      colorScaler.value_0c.set(scalerManager._10.colour_1c).mul(0x100);
      colorScaler.velocity_18.set(velocityVec).sub(sp0x28).mul(0x100).div(scalerStepCount);
      colorScaler.acceleration_24.set(0, 0, 0);
    }

    //LAB_80114ad0
    return FlowControl.CONTINUE;
  }

  @Method(0x80114b00L)
  public static FlowControl FUN_80114b00(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x80114e0cL)
  public static FlowControl FUN_80114e0c(final RunningScript<?> script) {
    final EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

    final int val = switch(script.params_20[1].get()) {
      case 0 -> manager._10._24;
      case 1 -> manager._10._28;
      case 2 -> manager._10._2c;
      case 3 -> manager._10._30;
      default -> throw new RuntimeException("Invalid value (I think) " + script.params_20[1].get());
    };

    script.params_20[2].set(val);
    return FlowControl.CONTINUE;
  }

  @Method(0x80114eb4L)
  public static FlowControl FUN_80114eb4(final RunningScript<?> script) {
    final EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final int a0 = script.params_20[1].get() + 5;

    if((1L << a0 & manager.flags_04) != 0) {
      FUN_800e8d04(manager, (byte)a0);
    }

    //LAB_80114f24
    return FlowControl.CONTINUE;
  }

  @Method(0x80114e60L)
  public static FlowControl FUN_80114e60(final RunningScript<?> script) {
    final EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

    switch(script.params_20[1].get()) {
      case 0 -> manager._10._24 = script.params_20[2].get();
      case 1 -> manager._10._28 = script.params_20[2].get();
      case 2 -> manager._10._2c = script.params_20[2].get();
      case 3 -> manager._10._30 = script.params_20[2].get();
      default -> throw new RuntimeException("Invalid value (I think) " + script.params_20[1].get());
    }

    return FlowControl.CONTINUE;
  }

  @Method(0x80114f3cL)
  public static void FUN_80114f3c(final ScriptState<EffectManagerData6c> manager, final int a1, final int a2, final int a3) {
    final EffectManagerData6c state = manager.innerStruct_00;

    if((1L << a1 + 5 & state.flags_04) != 0) {
      FUN_800e8d04(state, a1 + 5);
    }

    //LAB_80114fa8
    final BttlScriptData6cSub1c_3 v0 = FUN_800e8dd4(state, a1 + 5, 0, SEffe::FUN_80114d98, 0x1c, new BttlScriptData6cSub1c_3());

    final int val = switch(a1) {
      case 0 -> state._10._24;
      case 1 -> state._10._28;
      case 2 -> state._10._2c;
      case 3 -> state._10._30;
      default -> throw new RuntimeException("Invalid value (I think) " + a1);
    };

    v0.accumulator_0c = val << 8;
    v0.speed_10 = a2;
    v0.acceleration_14 = a3;
    v0._18 = -1;
    v0.ticksRemaining_1a = -1;
  }

  /** TODO this method advances animation frames */
  @Method(0x80114d98L)
  public static int FUN_80114d98(final EffectManagerData6c a0, final BttlScriptData6cSub1c_3 a1) {
    a1.speed_10 += a1.acceleration_14;
    a1.accumulator_0c += a1.speed_10;

    switch(a1._05 - 5) {
      case 0 -> a0._10._24 = a1.accumulator_0c >> 8;
      case 1 -> a0._10._28 = a1.accumulator_0c >> 8;
      case 2 -> a0._10._2c = a1.accumulator_0c >> 8;
      case 3 -> a0._10._30 = a1.accumulator_0c >> 8;
      default -> throw new RuntimeException("Invalid value (I think) " + (a1._05 - 5));
    }

    if(a1.ticksRemaining_1a == -1) {
      return 1;
    }

    a1.ticksRemaining_1a--;
    if(a1.ticksRemaining_1a > 0) {
      //LAB_80114e00
      return 1;
    }

    //LAB_80114e04
    return 0;
  }

  @Method(0x80114f34L)
  public static FlowControl FUN_80114f34(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x80115014L)
  public static FlowControl FUN_80115014(final RunningScript<?> script) {
    FUN_80114f3c((ScriptState<EffectManagerData6c>)scriptStatePtrArr_800bc1c0[script.params_20[0].get()], script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x80115058L)
  public static FlowControl FUN_80115058(final RunningScript<?> script) {
    final int s1 = script.params_20[1].get();
    final int s3 = script.params_20[2].get();
    final int s2 = script.params_20[3].get();

    final EffectManagerData6c s0 = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

    if((0x1L << s1 + 5 & s0.flags_04) != 0) {
      FUN_800e8d04(s0, s1 + 5);
    }

    //LAB_801150e8
    final BttlScriptData6cSub1c_3 v0 = FUN_800e8dd4(s0, s1 + 5, 0, SEffe::FUN_80114d98, 0x1c, new BttlScriptData6cSub1c_3());

    final int val = switch(s1) {
      case 0 -> s0._10._24;
      case 1 -> s0._10._28;
      case 2 -> s0._10._2c;
      case 3 -> s0._10._30;
      default -> throw new RuntimeException("Invalid value (I think) " + s1);
    };

    v0.accumulator_0c = val << 8;
    v0.speed_10 = (s3 * 0x100 - v0.accumulator_0c) / s2;
    v0.acceleration_14 = 0;
    v0._18 = -1;
    v0.ticksRemaining_1a = (short)s2;

    return FlowControl.CONTINUE;
  }

  @Method(0x80115168L)
  public static FlowControl FUN_80115168(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x80115288L)
  public static int FUN_80115288(final EffectManagerData6c a0, final BttlScriptData6cSub1c_3 a1) {
    a1.ticksRemaining_1a--;

    //LAB_801152a8
    return a1.ticksRemaining_1a > 0 ? 1 : 2;
  }

  @Method(0x801152b0L)
  public static FlowControl scriptSetEffectTicksRemaining(final RunningScript<?> script) {
    final BttlScriptData6cSub1c_3 v0 = FUN_800e8dd4((EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00, 0, 0, SEffe::FUN_80115288, 0x1c, new BttlScriptData6cSub1c_3());
    v0.ticksRemaining_1a = (short)script.params_20[1].get();
    return FlowControl.CONTINUE;
  }

  @Method(0x80115324L)
  public static FlowControl FUN_80115324(final RunningScript<?> script) {
    final EffectManagerData6c effects = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    effects._10.flags_00 = effects._10.flags_00 & 0x7fff_ffff | ((script.params_20[1].get() ^ 1) & 1) << 31;
    return FlowControl.CONTINUE;
  }

  @Method(0x80115388L)
  public static FlowControl FUN_80115388(final RunningScript<?> script) {
    final EffectManagerData6c a1 = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    a1._10.flags_00 = a1._10.flags_00 & 0xbfff_ffff | script.params_20[1].get() << 30;
    return FlowControl.CONTINUE;
  }

  @Method(0x801153e4L)
  public static FlowControl FUN_801153e4(final RunningScript<?> script) {
    final EffectManagerData6c a1 = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    a1._10.flags_00 = a1._10.flags_00 & 0xcfff_ffff | script.params_20[1].get() << 28;
    return FlowControl.CONTINUE;
  }

  @Method(0x80115440L)
  public static FlowControl FUN_80115440(final RunningScript<?> script) {
    final EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    manager._10.flags_00 = manager._10.flags_00 & 0xfbff_ffff | script.params_20[1].get() << 26;
    return FlowControl.CONTINUE;
  }

  @Method(0x8011549cL)
  public static FlowControl FUN_8011549c(final RunningScript<?> script) {
    final EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    manager._10.flags_00 = manager._10.flags_00 & 0xffff_ffbf | script.params_20[1].get() << 6;
    return FlowControl.CONTINUE;
  }

  @Method(0x801154f4L)
  public static FlowControl FUN_801154f4(final RunningScript<?> script) {
    final EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    manager._10.flags_00 = manager._10.flags_00 & 0xffff_fff7 | script.params_20[1].get() << 3;
    return FlowControl.CONTINUE;
  }

  @Method(0x8011554cL)
  public static FlowControl FUN_8011554c(final RunningScript<?> script) {
    FUN_800e8d04((EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00, (byte)script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x801155a0L)
  public static FlowControl FUN_801155a0(final RunningScript<?> script) {
    final EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    script.params_20[1].set(1 << script.params_20[2].get() & manager.flags_04);
    return FlowControl.CONTINUE;
  }

  @Method(0x801155f8L)
  public static FlowControl FUN_801155f8(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @Method(0x80115600L)
  public static FlowControl FUN_80115600(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @Method(0x80115608L)
  public static FlowControl FUN_80115608(final RunningScript<?> script) {
    final int scriptIndex = script.params_20[0].get();
    final int offset = script.params_20[2].get();
    final int mrgIndex = script.params_20[1].get();

    final ScriptFile file;
    if(mrgIndex == -1) {
      file = script.scriptState_04.scriptPtr_14;
    } else {
      //LAB_80115654
      file = deffManager_800c693c.scripts_2c[mrgIndex];
    }

    //LAB_80115674
    scriptStatePtrArr_800bc1c0[scriptIndex].loadScriptFile(file, offset);
    return FlowControl.CONTINUE;
  }

  /** Loads the same script that's currently executing into script index param0 and jumps to param1 */
  @Method(0x80115690L)
  public static FlowControl scriptLoadSameScriptAndJump(final RunningScript<?> script) {
    final int s0 = script.params_20[0].get();
    scriptStatePtrArr_800bc1c0[s0].loadScriptFile(script.scriptState_04.scriptPtr_14, 0);
    script.params_20[1].jump(scriptStatePtrArr_800bc1c0[s0]);
    return FlowControl.CONTINUE;
  }

  @Method(0x801156f8L)
  public static FlowControl FUN_801156f8(final RunningScript<?> script) {
    final EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    manager.scriptIndex_0c = script.params_20[1].get();
    manager.coord2Index_0d = script.params_20[2].get();
    return FlowControl.CONTINUE;
  }

  @Method(0x8011574cL)
  public static FlowControl scriptGetEffectZ(final RunningScript<?> script) {
    script.params_20[1].set(((EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00)._10.z_22);
    return FlowControl.CONTINUE;
  }

  @Method(0x8011578cL)
  public static FlowControl scriptSetEffectZ(final RunningScript<?> script) {
    final EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    manager._10.z_22 = script.params_20[1].get();

//    if(manager._10.z_22 < 0) {
//      LOGGER.warn("Negative Z value! %d", manager._10.z_22);
//      manager._10.z_22 = Math.abs(manager._10.z_22);
//    }

    return FlowControl.CONTINUE;
  }

  @Method(0x801157d0L)
  public static FlowControl FUN_801157d0(final RunningScript<?> script) {
    final int scriptIndex = script.params_20[1].get();
    final int coord2Index = script.params_20[2].get();

    final EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

    final MATRIX sp0x10 = new MATRIX();
    final MATRIX sp0x30 = new MATRIX();
    FUN_800e8594(sp0x10, manager);

    if(scriptIndex == -1) {
      sp0x30.set(sp0x10);
    } else {
      //LAB_8011588c
      final EffectManagerData6c sp0x50 = new EffectManagerData6c("Temp");

      sp0x50._10.trans_04.set(0, 0, 0);
      sp0x50._10.rot_10.set((short)0, (short)0, (short)0);
      sp0x50._10.scale_16.set((short)0x1000, (short)0x1000, (short)0x1000);

      sp0x50.scriptIndex_0c = scriptIndex;
      sp0x50.coord2Index_0d = coord2Index;

      final MATRIX transforms = new MATRIX();
      FUN_800e8594(transforms, sp0x50);

      final SVECTOR rot = new SVECTOR();
      final SVECTOR scale = new SVECTOR();
      getRotationAndScaleFromTransforms(rot, scale, transforms);
      rot.negate();
      RotMatrix_Zyx(rot, transforms);

      transforms.scaleL(new VECTOR().set(0x100_0000, 0x100_0000, 0x100_0000).div(scale));
      sp0x10.mul(transforms, sp0x30);

      final VECTOR sp0x100 = new VECTOR().set(sp0x10.transfer).sub(transforms.transfer);
      sp0x100.mul(transforms, sp0x30.transfer);
    }

    //LAB_801159cc
    getRotationAndScaleFromTransforms(manager._10.rot_10, manager._10.scale_16, sp0x30);
    manager._10.trans_04.set(sp0x30.transfer);
    manager.scriptIndex_0c = scriptIndex;
    manager.coord2Index_0d = coord2Index;
    return FlowControl.CONTINUE;
  }

  @Method(0x80115a28L)
  public static FlowControl FUN_80115a28(final RunningScript<?> script) {
    FUN_800e9178(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x80115a58L)
  public static FlowControl scriptPlayXaAudio(final RunningScript<?> script) {
    playXaAudio(script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x80115a94L)
  public static FlowControl FUN_80115a94(final RunningScript<?> script) {
    script.params_20[0].set((int)_800bf0cf.get());
    return FlowControl.CONTINUE;
  }

  @Method(0x80115ab0L)
  public static FlowControl FUN_80115ab0(final RunningScript<?> script) {
//    return _800bf0cf.get() != a0.params_20.get(0).deref().get() ? 2 : 0;
    //TODO GH#3 the XA code is rewritten and it never sets 800bf0cf back to 0, I dunno if this is important or not
    return FlowControl.CONTINUE;
  }

  @Method(0x80115ad8L)
  public static FlowControl FUN_80115ad8(final RunningScript<?> script) {
    final DeffManager7cc v1 = deffManager_800c693c;

    final int v0;
    if(script.params_20[0].get() >= 0) {
      //LAB_80115b08
      v0 = script.params_20[0].get() | v1.flags_20;
    } else {
      v0 = script.params_20[0].get() & v1.flags_20;
    }

    //LAB_80115b20
    v1.flags_20 = v0;
    return FlowControl.CONTINUE;
  }

  @Method(0x80115b2cL)
  public static void screenDarkeningTicker(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
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
  public static void screenDarkeningDestructor(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    applyScreenDarkening(state.storage_44[9]);
  }

  @Method(0x80115c2cL)
  public static void allocateScreenDarkeningEffect(final int startVal, final int targetVal) {
    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
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
  public static long loadDeffStageEffects(final int a0) {
    final int _00;
    final int _02;
    final int _04;

    if(currentStage_800c66a4.get() < 71 || currentStage_800c66a4.get() > 78) { // Not in Dragoon "special transformation" stage
      //LAB_80115d14
      //LAB_80115d2c
      for(int i = 0; ; i++) {
        if(melbuStageIndices_800fb064.get(i).get() == -1) { // This is the normal branch, no special-case handling
          //LAB_80115cd8
          final DeffManager7cc.Struct08 v0 = deffManager_800c693c._00;
          _00 = v0._00;
          _02 = v0._02;
          _04 = v0._04;
          break;
        }

        if(melbuStageIndices_800fb064.get(i).get() == currentStage_800c66a4.get()) { // Melbu stages
          //LAB_80115d58
          final DeffManager7cc.Struct04 v0 = deffManager_800c693c._08[i];
          _00 = v0._00;
          _02 = v0._02;
          _04 = 0;
          break;
        }
      }

      //LAB_80115d84
      if(a0 == 0) {
        //LAB_80115dc0
        if((stage_800bda0c._5e4 & 0x8000) != 0) {
          allocateScreenDarkeningEffect(6, 16);
        }

        //LAB_80115de8
        stage_800bda0c._5e4 &= ~(_00 | _02 | _04);
        //LAB_80115da8
      } else if(a0 == 1) {
        //LAB_80115e18
        stage_800bda0c._5e4 |= _00;
      } else if(a0 == 2) {
        //LAB_80115e34
        stage_800bda0c._5e4 |= _02;

        if((stage_800bda0c._5e4 & 0x8000) != 0) {
          allocateScreenDarkeningEffect(16, 6);
        }
      } else if(a0 == 3) {
        //LAB_80115e70
        //LAB_80115e8c
        stage_800bda0c._5e4 |= _04;
      }
    }

    //LAB_80115e90
    //LAB_80115e94
    return 0;
  }

  @Method(0x80115ea4L)
  public static FlowControl FUN_80115ea4(final RunningScript<?> script) {
    loadDeffStageEffects(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x80115ed4L)
  public static FlowControl FUN_80115ed4(final RunningScript<?> script) {
    final int w;
    final int v;
    final int h;
    final int u;
    final int a2 = script.params_20[0].get();
    final int s0 = script.params_20[1].get();
    final int type = a2 & 0xff00_0000;
    if(type == 0) {
      //LAB_80115f54
      final BattleScriptDataBase a0 = (BattleScriptDataBase)scriptStatePtrArr_800bc1c0[a2].innerStruct_00;
      final long v0;
      if(!BattleScriptDataBase.EM__.equals(a0.magic_00)) {
        //LAB_8011604c
        v0 = colourMapUvs_800fb0ec.get(((BattleObject27c)a0).model_148.colourMap_9d).get();
        u = (int)((v0 & 0xf) << 6);
        v = (int)((v0 & 0x10) << 4);
        w = 0x100;
        h = 0x100;
      } else {
        final EffectManagerData6c manager = (EffectManagerData6c)a0;
        final int v1 = manager.flags_04 & 0xff00_0000;
        if(v1 != 0x200_0000) {
          if(v1 == 0x300_0000) {
            //LAB_80116014
            throw new RuntimeException("ASM is bugged");
//          v0 = manager._44.deref();
//          MEMORY.ref(4, v0).offset(0x4L).setu(0);
//          v0 = t2 + MEMORY.ref(4, t2).offset(0x8L).get() + s0 * 0x10L;
//          u = MEMORY.ref(2, v0).offset(0x0L).getSigned();
//          v = MEMORY.ref(2, v0).offset(0x2L).getSigned();
//          w = MEMORY.ref(2, v0).offset(0x4L).getSigned() * 4;
//          h = MEMORY.ref(2, v0).offset(0x6L).getSigned();
            //LAB_80115fc8
          } else if(v1 == 0x400_0000) {
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
          v0 = colourMapUvs_800fb0ec.get(((BttlScriptData6cSub13c)manager.effect_44).model_134.colourMap_9d).get();
          u = (int)((v0 & 0xf) << 6);
          v = (int)((v0 & 0x10) << 4);
          w = 256;
          h = 256;
        }
      }
    } else if(type == 0x200_0000) {
      //LAB_80116098
      final DeffPart.AnimatedTmdType animatedTmdType = (DeffPart.AnimatedTmdType)getDeffPart(a2);
      final DeffPart.TextureInfo textureInfo = animatedTmdType.textureInfo_08[0];
      u = textureInfo.vramPos_00.x.get();
      v = textureInfo.vramPos_00.y.get();
      w = 256;
      h = 256;
    } else if(type == 0x100_0000 || type == 0x300_0000) {
      //LAB_801160c0
      //LAB_801160d4
      final DeffPart.TmdType tmdType = (DeffPart.TmdType)getDeffPart(a2);
      final DeffPart.TextureInfo textureInfo = tmdType.textureInfo_08[s0 * 2];
      u = textureInfo.vramPos_00.x.get();
      v = textureInfo.vramPos_00.y.get();
      w = textureInfo.vramPos_00.w.get() * 4;
      h = textureInfo.vramPos_00.h.get();
      //LAB_80115f34
    } else if(type == 0x400_0000) {
      //LAB_801160f4
      final BillboardSpriteEffect0c sp0x10 = new BillboardSpriteEffect0c();
      getSpriteMetricsFromSource(sp0x10, a2 & 0xff_ffff);
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
  public static void FUN_8011619c(final EffectManagerData6c manager, final BttlScriptData6cSub5c effect, final int deffFlags, final MATRIX matrix) {
    final MATRIX sp0x10 = new MATRIX();
    RotMatrix_Zyx(manager._10.rot_10, sp0x10);
    sp0x10.transfer.set(manager._10.trans_04);
    sp0x10.scaleL(manager._10.scale_16);
    sp0x10.compose(matrix, sp0x10);
    final int scale = manager._10._28;
    sp0x10.scaleL(new VECTOR().set(scale, scale, scale));
    manager._10.scale_16.setX((short)(manager._10.scale_16.getX() * scale / 0x1000));
    manager._10.scale_16.setY((short)(manager._10.scale_16.getY() * scale / 0x1000));
    manager._10.scale_16.setZ((short)(manager._10.scale_16.getZ() * scale / 0x1000));

    final int type = deffFlags & 0xff00_0000;
    if(type == 0x300_0000) {
      //LAB_80116708
      final TmdObjTable1c tmdObjTable;
      if(effect.deffTmdFlags_48 == deffFlags) {
        tmdObjTable = effect.deffTmdObjTable_4c;
      } else {
        //LAB_80116724
        if((deffFlags & 0xf_ff00) == 0xf_ff00) {
          tmdObjTable = deffManager_800c693c.tmds_2f8[deffFlags & 0xff];
        } else {
          //LAB_80116750
          tmdObjTable = ((DeffPart.TmdType)getDeffPart(deffFlags | 0x300_0000)).tmd_0c.tmdPtr_00.tmd.objTable[0];
        }

        //LAB_8011676c
        effect.deffTmdFlags_48 = deffFlags;
        effect.deffTmdObjTable_4c = tmdObjTable;
      }

      //LAB_80116778
      renderTmdSpriteEffect(tmdObjTable, manager._10, sp0x10);
    } else if(type == 0x400_0000) {
      if(effect.deffSpriteFlags_50 != deffFlags) {
        //LAB_801162e8
        final BillboardSpriteEffect0c sp0x48 = new BillboardSpriteEffect0c();

        getSpriteMetricsFromSource(sp0x48, deffFlags);
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
      final SVECTOR sp0x58 = new SVECTOR();
      final MATRIX sp0x60 = new MATRIX();
      final DVECTOR sp0x80 = new DVECTOR();
      sp0x10.compose(worldToScreenMatrix_800c3548, sp0x60);
      setRotTransMatrix(sp0x60);

      final int z = perspectiveTransform(sp0x58, sp0x80);
      if(z >= 0x50) {
        //LAB_801163c4
        final int a1 = (projectionPlaneDistance_1f8003f8.get() * 2 << 12) / z * manager._10.scale_16.getZ() / 0x1000;
        final int l = -w / 2 * a1 / 0x1000;
        final int r = w / 2 * a1 / 0x1000;
        final int t = -h / 2 * a1 / 0x1000;
        final int b = h / 2 * a1 / 0x1000;
        final int sin = rsin(manager._10.rot_10.getZ());
        final int cos = rcos(manager._10.rot_10.getZ());
        final int sinL = l * sin / 0x1000;
        final int cosL = l * cos / 0x1000;
        final int sinR = r * sin / 0x1000;
        final int cosR = r * cos / 0x1000;
        final int sinT = t * sin / 0x1000;
        final int cosT = t * cos / 0x1000;
        final int sinB = b * sin / 0x1000;
        final int cosB = b * cos / 0x1000;

        final GpuCommandPoly cmd = new GpuCommandPoly(4)
          .clut((clut & 0b111111) * 16, clut >>> 6)
          .vramPos(effect.metrics_54.u_00 & 0x3c0, (effect.metrics_54.v_02 & 0x100) != 0 ? 256 : 0)
          .rgb(manager._10.colour_1c.getX(), manager._10.colour_1c.getY(), manager._10.colour_1c.getZ())
          .pos(0, sp0x80.getX() + cosL - sinT, sp0x80.getY() + sinL + cosT)
          .pos(1, sp0x80.getX() + cosR - sinT, sp0x80.getY() + sinR + cosT)
          .pos(2, sp0x80.getX() + cosL - sinB, sp0x80.getY() + sinL + cosB)
          .pos(3, sp0x80.getX() + cosR - sinB, sp0x80.getY() + sinR + cosB)
          .uv(0, u, v)
          .uv(1, u + w - 1, v)
          .uv(2, u, v + h - 1)
          .uv(3, u + w - 1, v + h - 1);

        if((manager._10.flags_00 >>> 30 & 1) != 0) {
          cmd.translucent(Translucency.of(manager._10.flags_00 >>> 28 & 0b11));
        }

        GPU.queueCommand(z >> 2, cmd);
      }
    } else {
      //LAB_80116790
      final ScriptState<EffectManagerData6c> state = (ScriptState<EffectManagerData6c>)scriptStatePtrArr_800bc1c0[deffFlags];
      final EffectManagerData6c manager2 = state.innerStruct_00;
      manager._10.trans_04.set(sp0x10.transfer);
      getRotationAndScaleFromTransforms(manager._10.rot_10, manager._10.scale_16, sp0x10);

      final int oldScriptIndex = manager2.scriptIndex_0c;
      final int oldCoord2Index = manager2.coord2Index_0d;
      manager2.scriptIndex_0c = manager.myScriptState_0e.index;
      manager2.coord2Index_0d = -1;
      final int r = manager2._10.colour_1c.getX();
      final int g = manager2._10.colour_1c.getY();
      final int b = manager2._10.colour_1c.getZ();
      // As far as I can tell, using R for each of these is right...
      manager2._10.colour_1c.setX(manager._10.colour_1c.getX() * manager2._10.colour_1c.getX() / 128);
      manager2._10.colour_1c.setY(manager._10.colour_1c.getX() * manager2._10.colour_1c.getY() / 128);
      manager2._10.colour_1c.setZ(manager._10.colour_1c.getX() * manager2._10.colour_1c.getZ() / 128);
      state.renderer_08.accept(state, manager2);
      manager2._10.colour_1c.setX(r);
      manager2._10.colour_1c.setY(g);
      manager2._10.colour_1c.setZ(b);
      manager2.scriptIndex_0c = oldScriptIndex;
      manager2.coord2Index_0d = oldCoord2Index;
    }

    //LAB_801168b8
  }

  @Method(0x801168e8L)
  public static void processLmbType0(final EffectManagerData6c manager, final BttlScriptData6cSub5c effect, final int a2, final MATRIX matrix) {
    final LmbType0 lmb = (LmbType0)effect.lmb_0c;
    final int s6 = a2 / 0x2000;
    final int v1 = s6 + 1;
    final int s0 = a2 & 0x1fff;
    final int s1 = 0x2000 - s0;
    final int fp = v1 % lmb._08[0].count_04;

    //LAB_80116960
    for(int i = 0; i < lmb.count_04; i++) {
      if(effect._14[lmb._08[i]._00] != 0) {
        final LmbTransforms14 a1 = lmb._08[i]._08[s6];
        final LmbTransforms14 a0 = lmb._08[i]._08[fp];
        manager._10.rot_10.set(a1.rot_0c);
        manager._10.trans_04.setX((a1.trans_06.getX() * s1 + a0.trans_06.getX() * s0) / 0x2000);
        manager._10.trans_04.setY((a1.trans_06.getY() * s1 + a0.trans_06.getY() * s0) / 0x2000);
        manager._10.trans_04.setZ((a1.trans_06.getZ() * s1 + a0.trans_06.getZ() * s0) / 0x2000);
        manager._10.scale_16.setX((short)((a1.scale_00.getX() * s1 + a0.scale_00.getX() * s0) / 0x2000));
        manager._10.scale_16.setY((short)((a1.scale_00.getY() * s1 + a0.scale_00.getY() * s0) / 0x2000));
        manager._10.scale_16.setZ((short)((a1.scale_00.getZ() * s1 + a0.scale_00.getZ() * s0) / 0x2000));
        FUN_8011619c(manager, effect, effect._14[lmb._08[i]._00], matrix);
      }
    }
  }

  @Method(0x80116b7cL)
  public static void processLmbType1(final EffectManagerData6c manager, final BttlScriptData6cSub5c effect, final int t0, final MATRIX matrix) {
    final LmbType1 lmb = (LmbType1)effect.lmb_0c;
    int a0 = t0 >> 13;
    int s0 = t0 & 0x1fff;
    int s5 = (a0 + 1) % lmb._0a;
    final LmbTransforms14[] s7 = effect.ptr_10;
    int s1;
    if(effect._04 != t0) {
      s1 = 0x2000 - s0;

      if(s5 == 0) {
        if(a0 == 0) {
          return;
        }

        s5 = a0;
        a0 = 0;
        s0 = s1;
        s1 = 0x2000 - s0;
      }

      //LAB_80116c20
      if(a0 == 0) {
        for(int i = 0; i < lmb.count_04; i++) {
          s7[i].set(lmb._10[i]);
        }
      } else {
        //LAB_80116c50
        int a0_0 = (a0 - 1) * lmb._08 / 2;

        //LAB_80116c80
        for(int i = 0; i < lmb.count_04; i++) {
          final LmbTransforms14 transforms = s7[i];
          final LmbType1.Sub04 v1 = lmb._0c[i];

          if((v1._00 & 0x8000) == 0) {
            transforms.scale_00.setX(lmb._14[a0_0]);
            a0_0++;
          }

          //LAB_80116ca0
          if((v1._00 & 0x4000) == 0) {
            transforms.scale_00.setY(lmb._14[a0_0]);
            a0_0++;
          }

          //LAB_80116cc0
          if((v1._00 & 0x2000) == 0) {
            transforms.scale_00.setZ(lmb._14[a0_0]);
            a0_0++;
          }

          //LAB_80116ce0
          if((v1._00 & 0x1000) == 0) {
            transforms.trans_06.setX(lmb._14[a0_0]);
            a0_0++;
          }

          //LAB_80116d00
          if((v1._00 & 0x800) == 0) {
            transforms.trans_06.setY(lmb._14[a0_0]);
            a0_0++;
          }

          //LAB_80116d20
          if((v1._00 & 0x400) == 0) {
            transforms.trans_06.setZ(lmb._14[a0_0]);
            a0_0++;
          }

          //LAB_80116d40
          if((v1._00 & 0x200) == 0) {
            transforms.rot_0c.setX(lmb._14[a0_0]);
            a0_0++;
          }

          //LAB_80116d60
          if((v1._00 & 0x100) == 0) {
            transforms.rot_0c.setY(lmb._14[a0_0]);
            a0_0++;
          }

          //LAB_80116d80
          if((v1._00 & 0x80) == 0) {
            transforms.rot_0c.setZ(lmb._14[a0_0]);
            a0_0++;
          }
        }
      }

      //LAB_80116db8
      int a0_0 = (s5 - 1) * lmb._08 / 2;

      //LAB_80116de8
      for(int i = 0; i < lmb.count_04; i++) {
        final LmbTransforms14 transforms = s7[i];
        final LmbType1.Sub04 a2 = lmb._0c[i];

        if((a2._00 & 0x8000) == 0) {
          transforms.scale_00.setX((short)((transforms.scale_00.getX() * s1 + lmb._14[a0_0] * s0) / 0x2000));
          a0_0++;
        }

        //LAB_80116e34
        if((a2._00 & 0x4000) == 0) {
          transforms.scale_00.setY((short)((transforms.scale_00.getY() * s1 + lmb._14[a0_0] * s0) / 0x2000));
          a0_0++;
        }

        //LAB_80116e80
        if((a2._00 & 0x2000) == 0) {
          transforms.scale_00.setZ((short)((transforms.scale_00.getZ() * s1 + lmb._14[a0_0] * s0) / 0x2000));
          a0_0++;
        }

        //LAB_80116ecc
        if((a2._00 & 0x1000) == 0) {
          transforms.trans_06.setX((short)((transforms.trans_06.getX() * s1 + lmb._14[a0_0] * s0) / 0x2000));
          a0_0++;
        }

        //LAB_80116f18
        if((a2._00 & 0x800) == 0) {
          transforms.trans_06.setY((short)((transforms.trans_06.getY() * s1 + lmb._14[a0_0] * s0) / 0x2000));
          a0_0++;
        }

        //LAB_80116f64
        if((a2._00 & 0x400) == 0) {
          transforms.trans_06.setZ((short)((transforms.trans_06.getZ() * s1 + lmb._14[a0_0] * s0) / 0x2000));
          a0_0++;
        }

        //LAB_80116fb0
        if((a2._00 & 0x200) == 0) {
          a0_0++;
        }

        //LAB_80116fc8
        if((a2._00 & 0x100) == 0) {
          a0_0++;
        }

        //LAB_80116fd4
        if((a2._00 & 0x80) == 0) {
          a0_0++;
        }
      }

      //LAB_80116ff8
      effect._04 = t0;
    }

    //LAB_80116ffc
    //LAB_80117014
    for(int i = 0; i < lmb.count_04; i++) {
      final LmbTransforms14 transforms = s7[i];

      final int deffFlags = effect._14[lmb._0c[i]._03];
      if(deffFlags != 0) {
        manager._10.rot_10.set(transforms.rot_0c);
        manager._10.trans_04.set(transforms.trans_06);
        manager._10.scale_16.set(transforms.scale_00);

        FUN_8011619c(manager, effect, deffFlags, matrix);
      }
    }

    //LAB_801170d4
  }

  @Method(0x80117104L)
  public static void processLmbType2(final EffectManagerData6c manager, final BttlScriptData6cSub5c effect, final int t5, final MATRIX matrix) {
    final LmbType2 lmb = (LmbType2)effect.lmb_0c;
    final LmbTransforms14[] originalTransforms = lmb._10;
    final int s6 = t5 / 0x2000;
    final int s0 = t5 & 0x1fff;
    final int s2 = 0x2000 - s0;
    final LmbTransforms14[] transformsLo = effect.ptr_10;
    final LmbTransforms14[] transformsHi = Arrays.copyOfRange(transformsLo, lmb.count_04, transformsLo.length);
    final int fp = (s6 + 1) % lmb._0a;
    if(effect._04 != t5) {
      int s1 = effect._04 / 0x2000;

      if(fp == 0 && s6 == 0) {
        return;
      }

      //LAB_801171c8
      if(s6 < s1) {
        s1 = 0;

        for(int i = 0; i < lmb.count_04; i++) {
          transformsLo[i].scale_00.set(originalTransforms[i].scale_00);
          transformsLo[i].trans_06.set(originalTransforms[i].trans_06);
          transformsLo[i].rot_0c.set(originalTransforms[i].rot_0c);
        }
      }

      //LAB_801171f8
      //LAB_8011720c
      for(; s1 < s6; s1++) {
        int a0 = s1 * lmb._08;

        //LAB_80117234
        for(int i = 0; i < lmb._08 * 2; i += 2) {
          final long v1_0 = _8011a048.offset(i).getAddress();
          MEMORY.ref(1, v1_0).offset(0x0L).setu(lmb._14[a0] >> 4);
          MEMORY.ref(1, v1_0).offset(0x1L).setu(lmb._14[a0] << 28 >> 28);
          a0++;
        }

        //LAB_80117270
        long a0_0 = _8011a048.getAddress();

        //LAB_8011728c
        for(int i = 0; i < lmb.count_04; i++) {
          final LmbTransforms14 transform = transformsLo[i];

          final int flags = lmb._0c[i];

          if((flags & 0xe000) != 0xe000) {
            final int shift = (int)MEMORY.ref(1, a0_0).offset(0x0L).getSigned() & 0xf;
            a0_0++;

            if((flags & 0x8000) == 0) {
              transform.scale_00.x.add((short)(MEMORY.ref(1, a0_0).offset(0x0L).getSigned() << shift));
              a0_0++;
            }

            //LAB_801172c8
            if((flags & 0x4000) == 0) {
              transform.scale_00.y.add((short)(MEMORY.ref(1, a0_0).offset(0x0L).getSigned() << shift));
              a0_0++;
            }

            //LAB_801172f0
            if((flags & 0x2000) == 0) {
              transform.scale_00.z.add((short)(MEMORY.ref(1, a0_0).offset(0x0L).getSigned() << shift));
              a0_0++;
            }
          }

          //LAB_80117310
          //LAB_80117314
          if((flags & 0x1c00) != 0x1c00) {
            final int shift = (int)MEMORY.ref(1, a0_0).offset(0x0L).getSigned() & 0xf;
            a0_0++;

            if((flags & 0x1000) == 0) {
              transform.trans_06.x.add((short)(MEMORY.ref(1, a0_0).offset(0x0L).getSigned() << shift));
              a0_0++;
            }

            //LAB_80117348
            if((flags & 0x800) == 0) {
              transform.trans_06.y.add((short)(MEMORY.ref(1, a0_0).offset(0x0L).getSigned() << shift));
              a0_0++;
            }

            //LAB_80117370
            if((flags & 0x400) == 0) {
              transform.trans_06.z.add((short)(MEMORY.ref(1, a0_0).offset(0x0L).getSigned() << shift));
              a0_0++;
            }
          }

          //LAB_80117390
          //LAB_80117394
          if((flags & 0x380) != 0x380) {
            final int shift = (int)MEMORY.ref(1, a0_0).offset(0x0L).getSigned() & 0xf;
            a0_0++;

            if((flags & 0x200) == 0) {
              transform.rot_0c.x.add((short)(MEMORY.ref(1, a0_0).offset(0x0L).getSigned() << shift));
              a0_0++;
            }

            //LAB_801173c8
            if((flags & 0x100) == 0) {
              transform.rot_0c.y.add((short)(MEMORY.ref(1, a0_0).offset(0x0L).getSigned() << shift));
              a0_0++;
            }

            //LAB_801173f0
            if((flags & 0x80) == 0) {
              transform.rot_0c.z.add((short)(MEMORY.ref(1, a0_0).offset(0x0L).getSigned() << shift));
              a0_0++;
            }
          }
        }
      }

      //LAB_80117438
      if(fp == 0) {
        //LAB_801176c0
        //LAB_801176e0
        for(int i = 0; i < lmb.count_04; i++) {
          final LmbTransforms14 originalTransform = originalTransforms[i];
          final LmbTransforms14 transformLo = transformsLo[i];
          final LmbTransforms14 transformHi = transformsHi[i];

          final int flags = lmb._0c[i];

          if((flags & 0x8000) == 0) {
            transformHi.scale_00.setX((short)((transformLo.scale_00.getX() * s2 + originalTransform.scale_00.getX() * s0) / 0x2000));
          }

          //LAB_80117730
          if((flags & 0x4000) == 0) {
            transformHi.scale_00.setY((short)((transformLo.scale_00.getY() * s2 + originalTransform.scale_00.getY() * s0) / 0x2000));
          }

          //LAB_80117774
          if((flags & 0x2000) == 0) {
            transformHi.scale_00.setZ((short)((transformLo.scale_00.getZ() * s2 + originalTransform.scale_00.getZ() * s0) / 0x2000));
          }

          //LAB_801177b8
          if((flags & 0x1000) == 0) {
            transformHi.trans_06.setX((short)((transformLo.trans_06.getX() * s2 + originalTransform.trans_06.getX() * s0) / 0x2000));
          }

          //LAB_801177fc
          if((flags & 0x800) == 0) {
            transformHi.trans_06.setY((short)((transformLo.trans_06.getY() * s2 + originalTransform.trans_06.getY() * s0) / 0x2000));
          }

          //LAB_80117840
          if((flags & 0x400) == 0) {
            transformHi.trans_06.setZ((short)((transformLo.trans_06.getZ() * s2 + originalTransform.trans_06.getZ() * s0) / 0x2000));
          }
        }
      } else {
        int a0 = (fp - 1) * lmb._08;

        //LAB_80117470
        for(int i = 0; i < lmb._08 * 2; i += 2) {
          final long v1_1 = _8011a048.offset(i).getAddress();
          MEMORY.ref(1, v1_1).offset(0x0L).setu(lmb._14[a0] >> 4);
          MEMORY.ref(1, v1_1).offset(0x1L).setu(lmb._14[a0] << 28 >> 28);
          a0++;
        }

        //LAB_801174ac
        long a0_0 = _8011a048.getAddress();

        //LAB_801174d0
        for(int i = 0; i < lmb.count_04; i++) {
          final LmbTransforms14 transformLo = transformsLo[i];
          final LmbTransforms14 transformHi = transformsHi[i];

          final int flags = lmb._0c[i];

          if((flags & 0xe000) != 0xe000) {
            final int shift = (int)MEMORY.ref(1, a0_0).offset(0x0L).getSigned() & 0xf;
            a0_0++;

            if((flags & 0x8000) == 0) {
              transformHi.scale_00.setX((short)(transformLo.scale_00.getX() + (MEMORY.ref(1, a0_0).offset(0x0L).getSigned() << shift) * s0 / 0x2000));
              a0_0++;
            }

            //LAB_80117524
            if((flags & 0x4000) == 0) {
              transformHi.scale_00.setY((short)(transformLo.scale_00.getY() + (MEMORY.ref(1, a0_0).offset(0x0L).getSigned() << shift) * s0 / 0x2000));
              a0_0++;
            }

            //LAB_80117564
            if((flags & 0x2000) == 0) {
              transformHi.scale_00.setZ((short)(transformLo.scale_00.getZ() + (MEMORY.ref(1, a0_0).offset(0x0L).getSigned() << shift) * s0 / 0x2000));
              a0_0++;
            }
          }

          //LAB_8011759c
          //LAB_801175a0
          if((flags & 0x1c00) != 0x1c00) {
            final int shift = (int)MEMORY.ref(1, a0_0).offset(0x0L).getSigned() & 0xf;
            a0_0++;

            if((flags & 0x1000) == 0) {
              transformHi.trans_06.setX((short)(transformLo.trans_06.getX() + (MEMORY.ref(1, a0_0).offset(0x0L).getSigned() << shift) * s0 / 0x2000));
              a0_0++;
            }

            //LAB_801175ec
            if((flags & 0x800) == 0) {
              transformHi.trans_06.setY((short)(transformLo.trans_06.getY() + (MEMORY.ref(1, a0_0).offset(0x0L).getSigned() << shift) * s0 / 0x2000));
              a0_0++;
            }

            //LAB_8011762c
            if((flags & 0x400) == 0) {
              transformHi.trans_06.setZ((short)(transformLo.trans_06.getZ() + (MEMORY.ref(1, a0_0).offset(0x0L).getSigned() << shift) * s0 / 0x2000));
              a0_0++;
            }
          }

          //LAB_80117664
          //LAB_80117668
          if((flags & 0x380) != 0x380) {
            a0_0++;

            if((flags & 0x200) == 0) {
              a0_0++;
            }

            //LAB_80117680
            if((flags & 0x100) == 0) {
              a0_0++;
            }

            //LAB_80117690
            if((flags & 0x80) == 0) {
              a0_0++;
            }
          }
        }
      }

      //LAB_801178a0
      effect._04 = t5;
    }

    //LAB_801178a4
    //LAB_801178c0
    for(int i = 0; i < lmb.count_04; i++) {
      final LmbTransforms14 transformLo = transformsLo[i];
      final LmbTransforms14 transformHi = transformsHi[i];
      final int flags = lmb._0c[i];

      if(effect._14[flags >>> 24] != 0) {
        if(manager._10._2c != 0) {
          manager._10.rot_10.set(transformLo.rot_0c);
        } else {
          //LAB_80117914
          manager._10.rot_10.set(transformHi.rot_0c);
        }

        //LAB_80117938
        manager._10.trans_04.set(transformHi.trans_06);
        manager._10.scale_16.set(transformHi.scale_00);
        FUN_8011619c(manager, effect, effect._14[flags >>> 24], matrix);
      }
    }

    //LAB_801179c0
  }

  @Method(0x801179f0L)
  public static void FUN_801179f0(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final int flags = manager._10.flags_00;
    final BttlScriptData6cSub5c effect = (BttlScriptData6cSub5c)manager.effect_44;
    if(flags >= 0) {
      int s1 = Math.max(0, manager._10._24) % (effect._08 * 2) << 12;
      tmdGp0Tpage_1f8003ec.set(flags >>> 23 & 0x60);
      zOffset_1f8003e8.set(manager._10.z_22);
      if((manager._10.flags_00 & 0x40) == 0) {
        FUN_800e61e4(manager._10.colour_1c.getX() / 128.0f, manager._10.colour_1c.getY() / 128.0f, manager._10.colour_1c.getZ() / 128.0f);
      }

      //LAB_80117ac0
      //LAB_80117acc
      final EffectManagerData6c sp0x10 = new EffectManagerData6c("Temp 2");
      sp0x10.set(manager);

      final MATRIX sp0x80 = new MATRIX();
      FUN_800e8594(sp0x80, manager);

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
      final int s4 = effect._38;

      if(s4 >= 2) {
        final int spe4 = effect._3c;
        int s5;
        int s6;
        int s7;
        final int spd0;
        final int spd4;
        final int spd8;
        if((effect._34 & 0x4) != 0) {
          final int v0 = effect._40 - 0x1000;
          spd0 = sp0x10._10.colour_1c.getX() * v0 / s4;
          spd4 = sp0x10._10.colour_1c.getY() * v0 / s4;
          spd8 = sp0x10._10.colour_1c.getZ() * v0 / s4;
          s5 = sp0x10._10.colour_1c.getX() << 12;
          s6 = sp0x10._10.colour_1c.getY() << 12;
          s7 = sp0x10._10.colour_1c.getZ() << 12;
        } else {
          //LAB_80117c30
          s5 = 0;
          s6 = 0;
          s7 = 0;
          spd0 = 0;
          spd4 = 0;
          spd8 = 0;
        }

        //LAB_80117c48
        int spdc = 0;
        int spe0 = 0;
        if((effect._34 & 0x8) != 0) {
          final int t4 = sp0x10._10._28 * (effect._40 - 0x1000);
          spdc = sp0x10._10._28 << 12;
          spe0 = t4 / s4;
        }

        //LAB_80117c88
        s1 -= spe4;

        //LAB_80117ca0
        for(int i = 1; i < s4 && s1 >= 0; i++) {
          if((effect._34 & 0x4) != 0) {
            s5 = s5 + spd0;
            s6 = s6 + spd4;
            s7 = s7 + spd8;
            manager._10.colour_1c.setX(s5 >> 12);
            manager._10.colour_1c.setY(s6 >> 12);
            manager._10.colour_1c.setZ(s7 >> 12);
          }

          //LAB_80117d1c
          if((effect._34 & 0x8) != 0) {
            spdc = spdc + spe0;
            manager._10._28 = spdc >> 12;
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
      manager.set(sp0x10);

      if((manager._10.flags_00 & 0x40) == 0) {
        FUN_800e62a8();
      }
    }

    //LAB_80117e80
  }

  /** Effect renderer for Down Burst and Night Raid items */
  @Method(0x80117eb0L)
  public static FlowControl FUN_80117eb0(final RunningScript<? extends BattleScriptDataBase> script) {
    final int param1 = script.params_20[1].get();
    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      "BttlScriptData6cSub5c",
      script.scriptState_04,
      null,
      SEffe::FUN_801179f0,
      null,
      new BttlScriptData6cSub5c()
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    manager.flags_04 = 0;

    final DeffPart.LmbType lmbType;
    if((param1 & 0xf_ff00) == 0xf_ff00) {
      lmbType = deffManager_800c693c.lmbs_390[param1 & 0xff];
    } else {
      //LAB_80117f58
      lmbType = (DeffPart.LmbType)getDeffPart(param1);
    }

    //LAB_80117f68
    final BttlScriptData6cSub5c effect = (BttlScriptData6cSub5c)manager.effect_44;
    effect.lmbType_00 = lmbType.type_04;
    effect._04 = 0;
    effect.lmb_0c = lmbType.lmb_08;
    effect.ptr_10 = null;
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
      effect._08 = lmb._08[0].count_04;
    } else if(type == 1) {
      //LAB_80118018
      final LmbType1 lmb = (LmbType1)effect.lmb_0c;
      effect._08 = lmb._0a;
      effect.ptr_10 = new LmbTransforms14[lmb.count_04];

      for(int i = 0; i < lmb.count_04; i++) {
        effect.ptr_10[i] = new LmbTransforms14().set(lmb._10[i]);
      }
    } else if(type == 2) {
      //LAB_80118068
      final LmbType2 lmb = (LmbType2)effect.lmb_0c;
      effect._08 = lmb._0a;
      effect.ptr_10 = new LmbTransforms14[lmb.count_04 * 2];

      for(int i = 0; i < lmb.count_04; i++) {
        effect.ptr_10[i] = new LmbTransforms14().set(lmb._10[i]);
        effect.ptr_10[i + lmb.count_04] = new LmbTransforms14().set(lmb._10[i]);
      }
    }

    //LAB_801180e0
    //LAB_801180e8
    manager._10._24 = 0xffff_ffff;
    manager._10.flags_00 |= 0x5000_0000;
    FUN_80114f3c(state, 0, 0x100, 0);
    manager._10._28 = 0x1000;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x801181a8L)
  public static FlowControl FUN_801181a8(final RunningScript<?> script) {
    final EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    ((BttlScriptData6cSub5c)manager.effect_44)._14[script.params_20[1].get()] = script.params_20[2].get();
    return FlowControl.CONTINUE;
  }

  @Method(0x801181f0L)
  public static FlowControl FUN_801181f0(final RunningScript<?> script) {
    final BttlScriptData6cSub5c v0 = (BttlScriptData6cSub5c)((EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00).effect_44;

    final int v1 = script.params_20[3].get() + 1;
    v0._34 = script.params_20[1].get();
    v0._38 = script.params_20[2].get() * v1;
    v0._3c = 0x1000 / v1;
    v0._40 = script.params_20[4].get();
    return FlowControl.CONTINUE;
  }

  /** TODO renders other effects too? Burnout, more? Uses CTMD render pipeline */
  @Method(0x8011826cL)
  public static void renderDeffTmd(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final DeffTmdRenderer14 s1 = (DeffTmdRenderer14)data.effect_44;

    if(data._10.flags_00 >= 0) {
      final MATRIX sp0x10 = new MATRIX();
      FUN_800e8594(sp0x10, data);
      if((data._10.flags_00 & 0x4000_0000) != 0) {
        tmdGp0Tpage_1f8003ec.set(data._10.flags_00 >>> 23 & 0x60);
      } else {
        //LAB_801182bc
        tmdGp0Tpage_1f8003ec.set(s1._10);
      }

      //LAB_801182c8
      zOffset_1f8003e8.set(data._10.z_22);
      if((data._10.flags_00 & 0x40) == 0) {
        FUN_800e61e4(data._10.colour_1c.getX() / 128.0f, data._10.colour_1c.getY() / 128.0f, data._10.colour_1c.getZ() / 128.0f);
      } else {
        //LAB_80118304
        FUN_800e60e0(1.0f, 1.0f, 1.0f);
      }

      //LAB_80118314
      if(data.scriptIndex_0c < -2) {
        if(data.scriptIndex_0c == -4) {
          sp0x10.transfer.setZ(projectionPlaneDistance_1f8003f8.get());
        }

        //LAB_8011833c
        GsSetLightMatrix(sp0x10);
        setRotTransMatrix(sp0x10);

        final GsDOBJ2 dobj2 = new GsDOBJ2();
        dobj2.attribute_00 = data._10.flags_00;
        dobj2.tmd_08 = s1.tmd_08;

        final int oldZShift = zShift_1f8003c4.get();
        final int oldZMax = zMax_1f8003cc.get();
        final int oldZMin = zMin;
        zShift_1f8003c4.set(2);
        zMax_1f8003cc.set(0xffe);
        zMin = 0xb;
        Renderer.renderDobj2(dobj2, false, 0);
        zShift_1f8003c4.set(oldZShift);
        zMax_1f8003cc.set(oldZMax);
        zMin = oldZMin;
      } else {
        //LAB_80118370
        renderTmdSpriteEffect(s1.tmd_08, data._10, sp0x10);
      }

      //LAB_80118380
      if((data._10.flags_00 & 0x40) == 0) {
        FUN_800e62a8();
      } else {
        //LAB_801183a4
        FUN_800e6170();
      }
    }

    //LAB_801183ac
  }

  @Method(0x801183c0L)
  public static FlowControl allocateDeffTmd(final RunningScript<? extends BattleScriptDataBase> script) {
    final int s1 = script.params_20[1].get();
    final String name;
    if((s1 & 0xf_ff00) == 0xf_ff00) {
      name = deffManager_800c693c.tmds_2f8[s1 & 0xff].name;
    } else {
      final DeffPart.TmdType tmdType = (DeffPart.TmdType)getDeffPart(s1 | 0x300_0000);
      name = tmdType.name;
    }

    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      "DEFF TMD " + name,
      script.scriptState_04,
      null,
      SEffe::renderDeffTmd,
      null,
      new DeffTmdRenderer14()
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    manager.flags_04 = 0x300_0000;

    final DeffTmdRenderer14 effect = (DeffTmdRenderer14)manager.effect_44;

    effect._00 = s1 | 0x300_0000;

    if((s1 & 0xf_ff00) == 0xf_ff00) {
      effect.tmdType_04 = null;
      effect.tmd_08 = deffManager_800c693c.tmds_2f8[s1 & 0xff];
      effect._10 = 0x20;
    } else {
      //LAB_8011847c
      final DeffPart.TmdType tmdType = (DeffPart.TmdType)getDeffPart(s1 | 0x300_0000);
      final TmdWithId tmdWithId = tmdType.tmd_0c.tmdPtr_00;
      effect.tmdType_04 = tmdType;
      effect.tmd_08 = tmdWithId.tmd.objTable[0];
      effect._10 = (int)((tmdWithId.id & 0xffff_0000L) >>> 11);
    }

    //LAB_801184ac
    manager._10.flags_00 = 0x1400_0000;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x801184e4L)
  public static FlowControl FUN_801184e4(final RunningScript<? extends BattleScriptDataBase> script) {
    final int flags = script.params_20[1].get();
    final int objIndex = script.params_20[2].get();

    final TmdObjTable1c objTable;

    final int type = flags & 0xff00_0000;
    if(type == 0x100_0000) {
      //LAB_801185e4
      final DeffPart.AnimatedTmdType animatedTmdType = (DeffPart.AnimatedTmdType)getDeffPart(flags);
      final Tmd tmd = animatedTmdType.tmd_0c.tmdPtr_00.tmd;
      objTable = tmd.objTable[0];
    } else if(type == 0x200_0000) {
      //LAB_801185c0
      final DeffPart.AnimatedTmdType animatedTmdType = (DeffPart.AnimatedTmdType)getDeffPart(flags);
      objTable = optimisePacketsIfNecessary(animatedTmdType.tmd_0c.tmdPtr_00, objIndex);
      //LAB_801185b0
    } else if(type == 0x700_0000) {
      //LAB_80118610
      objTable = battlePreloadedEntities_1f8003f4.stage_963c.dobj2s_00[objIndex].tmd_08;
    } else {
      //LAB_80118634
      final BattleScriptDataBase a0_0 = (BattleScriptDataBase)scriptStatePtrArr_800bc1c0[flags].innerStruct_00;
      if(BattleScriptDataBase.EM__.equals(a0_0.magic_00)) {
        final EffectManagerData6c effects = (EffectManagerData6c)a0_0;
        final int v1 = effects.flags_04 & 0xff00_0000;
        if(v1 == 0x100_0000 || v1 == 0x200_0000) {
          //LAB_8011867c
          objTable = ((BttlScriptData6cSub13c)effects.effect_44).model_134.dobj2ArrPtr_00[objIndex].tmd_08;
        } else {
          objTable = null;
        }
      } else {
        //LAB_801186a4
        //LAB_801186b4
        objTable = ((BattleObject27c)a0_0).model_148.dobj2ArrPtr_00[objIndex].tmd_08;
      }
    }

    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      objTable != null ? "Obj table renderer FUN_801184e4 " + objTable.name : "TMD renderer with no TMD? FUN_801184e4",
      script.scriptState_04,
      null,
      SEffe::renderDeffTmd,
      null,
      new DeffTmdRenderer14()
    );

    final EffectManagerData6c s4 = state.innerStruct_00;
    s4.flags_04 = 0x300_0000;

    final DeffTmdRenderer14 s0 = (DeffTmdRenderer14)s4.effect_44;
    s0._10 = 0x20;
    s0._00 = 0x300_0000;
    s0.tmdType_04 = null;
    s0.tmd_08 = objTable;

    //LAB_801186bc
    //LAB_801186c0
    s4._10.flags_00 = 0x1400_0000;
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
      final DeffPart.TmdType tmdType = (DeffPart.TmdType)getDeffPart(flags | 0x300_0000);
      final TmdWithId tmdWithId = tmdType.tmd_0c.tmdPtr_00;
      spriteEffect.tmdType_04 = tmdType;
      spriteEffect.tmd_08 = tmdWithId.tmd.objTable[0];
      spriteEffect.tpage_10 = (int)((tmdWithId.id & 0xffff_0000L) >>> 11);
    }
    //LAB_80118780
  }

  /** TODO renders some kind of deff tmd maybe, uses ctmd render pipeline */
  @Method(0x80118790L)
  public static void FUN_80118790(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    if(manager._10.flags_00 >= 0) {
      final int y = manager._10.trans_04.getY();
      manager._10.trans_04.setY(0);
      final MATRIX sp0x10 = new MATRIX();
      FUN_800e8594(sp0x10, manager);
      sp0x10.transfer.setY(y);
      manager._10.trans_04.setY(y);

      final short rotY = (short)ratan2(-sp0x10.get(6), sp0x10.get(0));
      sp0x10.rotateY(-rotY);
      sp0x10.rotateZ(-ratan2(sp0x10.get(3), sp0x10.get(0)));
      sp0x10.rotateX(-ratan2(-sp0x10.get(5), sp0x10.get(8)));
      sp0x10.rotateY(rotY);
      sp0x10.set(3, (short)0);
      sp0x10.set(4, (short)0);
      sp0x10.set(5, (short)0);
      tmdGp0Tpage_1f8003ec.set(manager._10.flags_00 >>> 23 & 0x60);
      zOffset_1f8003e8.set(manager._10.z_22);
      FUN_800e60e0(manager._10.colour_1c.getX() / 128.0f, manager._10.colour_1c.getY() / 128.0f, manager._10.colour_1c.getZ() / 128.0f);
      renderTmdSpriteEffect(model_800bda10.dobj2ArrPtr_00[0].tmd_08, manager._10, sp0x10);
      FUN_800e6170();
    }

    //LAB_801188d8
  }

  @Method(0x801188ecL)
  public static FlowControl FUN_801188ec(final RunningScript<? extends BattleScriptDataBase> script) {
    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      "Unknown (FUN_801188ec, %s)".formatted(model_800bda10.dobj2ArrPtr_00[0].tmd_08.name),
      script.scriptState_04,
      null,
      SEffe::FUN_80118790,
      null,
      null
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    manager.flags_04 = 0x600_0000;
    manager._10.scale_16.set((short)0x400, (short)0x400, (short)0x400);
    manager._10._24 = 0;
    manager._10.flags_00 = 0x6400_0040;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x80118984L)
  public static FlowControl FUN_80118984(final RunningScript<?> script) {
    final int effectIndex = script.params_20[0].get();
    final ScriptState<EffectManagerData6c> state = (ScriptState<EffectManagerData6c>)scriptStatePtrArr_800bc1c0[effectIndex];
    final EffectManagerData6c manager = state.innerStruct_00;
    final BttlScriptData6cSub13c effect = (BttlScriptData6cSub13c)manager.effect_44;

    final DeffPart part = getDeffPart(script.params_20[1].get() | 0x500_0000);
    final Anim anim = ((DeffPart.AnimatedTmdType)part).anim_14;

    effect.anim_0c = anim;
    loadModelAnim(effect.model_134, anim);
    manager._10._24 = 0;
    FUN_80114f3c(state, 0, 0x100, 0);
    return FlowControl.CONTINUE;
  }

  @Method(0x80118a24L)
  public static void renderShirleyTransformWipeEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final int x = manager._10.trans_04.getX() + 160 - manager._10._24 / 2;
    final int y = manager._10.trans_04.getY() + 120 - manager._10._28 / 2;
    final int minZ = manager._10.trans_04.getZ() - manager._10._2c / 2 >> 2;
    final int maxZ = manager._10.trans_04.getZ() + manager._10._2c / 2 >> 2;
    final int right = 320;
    final int bottom = 240;

    final RECT buffPos = new RECT();

    //LAB_80118ba8
    for(int i = 0; i < 4; i++) {
      buffPos.x.set((short)(x + manager._10._24 / 2 * (i & 1)));
      buffPos.y.set((short)(y + manager._10._28 / 2 * (i >> 1)));
      buffPos.w.set((short)(manager._10._24 / 2));
      buffPos.h.set((short)(manager._10._28 / 2));

      if(buffPos.x.get() < right) {
        if(buffPos.x.get() < 0) {
          buffPos.w.add(buffPos.x.get());
          buffPos.x.set((short)0);
        }

        //LAB_80118c58
        if(buffPos.x.get() + buffPos.w.get() > right) {
          buffPos.w.set((short)(right - buffPos.x.get()));
        }

        //LAB_80118c7c
        if(buffPos.w.get() > 0) {
          if(buffPos.y.get() < bottom) {
            if(buffPos.y.get() < 0) {
              buffPos.y.set((short)0);
            }

            //LAB_80118cc0
            if(buffPos.y.get() + buffPos.h.get() > bottom) {
              buffPos.h.set((short)(bottom - buffPos.y.get()));
            }

            //LAB_80118ce4
            if(buffPos.h.get() > 0) {
              final int scale = GPU.getScale();
              final int[] data = new int[buffPos.w.get() * scale * buffPos.h.get() * scale];
              final Rect4i rect = new Rect4i(buffPos.x.get() * scale, buffPos.y.get() * scale, buffPos.w.get() * scale, buffPos.h.get() * scale);

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
  @Method(0x80118df4L)
  public static FlowControl allocateShirleyTransformWipeEffect(final RunningScript<? extends BattleScriptDataBase> script) {
    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      "Shirley transform wipe effect",
      script.scriptState_04,
      null,
      SEffe::renderShirleyTransformWipeEffect,
      null,
      null
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    manager._10.trans_04.setZ(256);
    manager._10._24 = 0x80;
    manager._10._28 = 0x80;
    manager._10._2c = 0x100;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  /** Some effects use CTMD render pipeline if type == 0x300_0000 */
  @Method(0x80118e98L)
  public static void renderSpriteWithTrailEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final SpriteWithTrailEffect30 effect = (SpriteWithTrailEffect30)manager.effect_44;
    if(manager._10.flags_00 >= 0) {
      final MATRIX transformMatrix = new MATRIX();
      FUN_800e8594(transformMatrix, manager);

      final int type = effect.effectFlag_04 & 0xff00_0000;
      if(type == 0x300_0000) {
        final TmdSpriteEffect10 sprite = (TmdSpriteEffect10)effect.subEffect_1c;

        //LAB_80118f38
        if((manager._10.flags_00 & 0x4000_0000) != 0) {
          tmdGp0Tpage_1f8003ec.set(manager._10.flags_00 >>> 23 & 0x60);
        } else {
          //LAB_80118f5c
          tmdGp0Tpage_1f8003ec.set(sprite.tpage_10);
        }

        //LAB_80118f68
        zOffset_1f8003e8.set(manager._10.z_22);

        if((manager._10.flags_00 & 0x40) == 0) {
          FUN_800e61e4(manager._10.colour_1c.getX() / 128.0f, manager._10.colour_1c.getY() / 128.0f, manager._10.colour_1c.getZ() / 128.0f);
        }

        //LAB_80118f9c
        renderTmdSpriteEffect(sprite.tmd_08, manager._10, transformMatrix);
      } else if(type == 0x400_0000) {
        final BillboardSpriteEffect0c sprite = (BillboardSpriteEffect0c)effect.subEffect_1c;
        renderBillboardSpriteEffect_(sprite.metrics_04, manager._10, transformMatrix);
      }

      //LAB_80118fac
      if(effect.countCopies_08 != 0) {
        final EffectManagerData6cInner managerInner = new EffectManagerData6cInner();
        final VECTOR colour = new VECTOR();
        final VECTOR scale = new VECTOR();
        final VECTOR stepScale = new VECTOR();

        //LAB_80118fc4
        managerInner.set(manager._10);

        final int combinedSteps = effect.countCopies_08 * (effect.countTransformSteps_0c + 1);
        if((effect.colourAndScaleFlags_00 & 0x4) != 0) {
          final int brightness = effect.colourAndScaleTransformModifier_10 - 0x1000;
          managerInner._28 = managerInner.colour_1c.getX() << 12;
          managerInner._2c = managerInner.colour_1c.getY() << 12;
          managerInner._30 = managerInner.colour_1c.getZ() << 12;
          colour.setX(managerInner.colour_1c.getX() * brightness / combinedSteps);
          colour.setY(managerInner.colour_1c.getY() * brightness / combinedSteps);
          colour.setZ(managerInner.colour_1c.getZ() * brightness / combinedSteps);
        }

        //LAB_801190a8
        if((effect.colourAndScaleFlags_00 & 0x8) != 0) {
          final int scaleModifier = effect.colourAndScaleTransformModifier_10 - 0x1000;
          scale.setX(managerInner.scale_16.getX() << 12);
          scale.setY(managerInner.scale_16.getY() << 12);
          scale.setZ(managerInner.scale_16.getZ() << 12);
          stepScale.setX(managerInner.scale_16.getX() * scaleModifier / combinedSteps);
          stepScale.setY(managerInner.scale_16.getY() * scaleModifier / combinedSteps);
          stepScale.setZ(managerInner.scale_16.getZ() * scaleModifier / combinedSteps);
        }

        //LAB_80119130
        //LAB_8011914c
        for(int i = 1; i < effect.countCopies_08 && i < effect.translationIndexBase_14; i++) {
          final VECTOR instTranslation = effect.instanceTranslations_18[(effect.translationIndexBase_14 - i - 1) % effect.countCopies_08];

          final int steps = effect.countTransformSteps_0c + 1;
          final int stepX = (instTranslation.getX() - transformMatrix.transfer.getX() << 12) / steps;
          final int stepY = (instTranslation.getY() - transformMatrix.transfer.getY() << 12) / steps;
          final int stepZ = (instTranslation.getZ() - transformMatrix.transfer.getZ() << 12) / steps;

          int x = transformMatrix.transfer.getX() << 12;
          int y = transformMatrix.transfer.getY() << 12;
          int z = transformMatrix.transfer.getZ() << 12;

          //LAB_80119204
          for(int j = effect.countTransformSteps_0c; j >= 0; j--) {
            if((effect.colourAndScaleFlags_00 & 0x4) != 0) {
              managerInner._28 += colour.getX();
              managerInner._2c += colour.getY();
              managerInner._30 += colour.getZ();
              //LAB_80119254
              //LAB_80119270
              //LAB_8011928c
              managerInner.colour_1c.setX(managerInner._28 >> 12);
              managerInner.colour_1c.setY(managerInner._2c >> 12);
              managerInner.colour_1c.setZ(managerInner._30 >> 12);
            }

            //LAB_80119294
            if((effect.colourAndScaleFlags_00 & 0x8) != 0) {
              scale.add(stepScale);

              //LAB_801192e4
              //LAB_80119300
              //LAB_8011931c
              managerInner.scale_16.setX((short)(scale.getX() >> 12));
              managerInner.scale_16.setY((short)(scale.getY() >> 12));
              managerInner.scale_16.setZ((short)(scale.getZ() >> 12));
            }

            //LAB_80119324
            x += stepX;
            y += stepY;
            z += stepZ;

            //LAB_80119348
            //LAB_80119360
            //LAB_80119378
            transformMatrix.transfer.set(x >> 12, y >> 12, z >> 12);
            transformMatrix.scaleL(managerInner.scale_16);

            if(type == 0x300_0000) {
              //LAB_801193f0
              final TmdSpriteEffect10 subEffect = (TmdSpriteEffect10)effect.subEffect_1c;
              renderTmdSpriteEffect(subEffect.tmd_08, managerInner, transformMatrix);
            } else if(type == 0x400_0000) {
              final BillboardSpriteEffect0c subEffect = (BillboardSpriteEffect0c)effect.subEffect_1c;
              renderBillboardSpriteEffect_(subEffect.metrics_04, managerInner, transformMatrix);
            }
            //LAB_80119400
            //LAB_80119404
          }
          //LAB_8011940c
        }

        //LAB_80119420
        if(type == 0x300_0000 && (manager._10.flags_00 & 0x40) == 0) {
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
  @Method(0x80119484L)
  public static FlowControl allocateSpriteWithTrailEffect(final RunningScript<? extends BattleScriptDataBase> script) {
    final int effectFlag = script.params_20[1].get();

    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      "SpriteWithTrailEffect30",
      script.scriptState_04,
      SEffe::tickSpriteWithTrailEffect,
      SEffe::renderSpriteWithTrailEffect,
      null,
      new SpriteWithTrailEffect30(script.params_20[3].get())
    );

    final EffectManagerData6c manager = state.innerStruct_00;

    final SpriteWithTrailEffect30 effect = (SpriteWithTrailEffect30)manager.effect_44;
    effect.colourAndScaleFlags_00 = script.params_20[2].get();
    effect.effectFlag_04 = effectFlag;
    effect.countTransformSteps_0c = script.params_20[4].get();
    effect.colourAndScaleTransformModifier_10 = script.params_20[5].get();
    effect.translationIndexBase_14 = 0;

    //LAB_8011956c
    final int effectType = effectFlag & 0xff00_0000;
    if(effectType == 0x400_0000) {
      //LAB_80119640
      effect.subEffect_1c = new BillboardSpriteEffect0c();
      getSpriteMetricsFromSource((BillboardSpriteEffect0c)effect.subEffect_1c, effectFlag);
      manager._10.flags_00 = manager._10.flags_00 & 0xfbff_ffff | 0x5000_0000;
    } else if(effectType == 0x300_0000) {
      //LAB_80119668
      effect.subEffect_1c = new TmdSpriteEffect10();
      getSpriteTmdFromSource((TmdSpriteEffect10)effect.subEffect_1c, effectFlag);
      manager._10.flags_00 = 0x1400_0000;
    } else if(effectType == 0) {
      //LAB_801195a8
      throw new RuntimeException("The type is " + ((EffectManagerData6c)scriptStatePtrArr_800bc1c0[effectFlag].innerStruct_00).effect_44.getClass().getSimpleName());
    }

    //LAB_8011967c
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x801196bcL)
  public static void tickSpriteWithTrailEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final SpriteWithTrailEffect30 effect = (SpriteWithTrailEffect30)manager.effect_44;

    if(effect.countCopies_08 != 0) {
      final MATRIX transformMatrix = new MATRIX();
      FUN_800e8594(transformMatrix, manager);
      effect.instanceTranslations_18[effect.translationIndexBase_14 % effect.countCopies_08].set(transformMatrix.transfer);
      effect.translationIndexBase_14++;
    }
    //LAB_80119778
  }
}
