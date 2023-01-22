package legend.game.combat;

import legend.core.MathHelper;
import legend.core.gpu.Bpp;
import legend.core.gpu.Gpu;
import legend.core.gpu.GpuCommandCopyVramToVram;
import legend.core.gpu.GpuCommandLine;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gpu.GpuCommandQuad;
import legend.core.gpu.GpuCommandSetMaskBit;
import legend.core.gpu.RECT;
import legend.core.gte.COLOUR;
import legend.core.gte.DVECTOR;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.Tmd;
import legend.core.gte.TmdObjTable;
import legend.core.gte.TmdWithId;
import legend.core.gte.VECTOR;
import legend.core.memory.Memory;
import legend.core.memory.Method;
import legend.core.memory.Ref;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BiConsumerRef;
import legend.core.memory.types.BiFunctionRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.QuadConsumerRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.TriConsumerRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.game.combat.deff.Anim;
import legend.game.combat.deff.DeffManager7cc;
import legend.game.combat.deff.DeffPart;
import legend.game.combat.deff.LmbTransforms14;
import legend.game.combat.deff.LmbType0;
import legend.game.combat.deff.LmbType1;
import legend.game.combat.deff.LmbType2;
import legend.game.combat.types.AdditionOverlaysEffect44;
import legend.game.combat.types.AttackHitFlashEffect0c;
import legend.game.combat.types.BattleObject27c;
import legend.game.combat.types.BattleScriptDataBase;
import legend.game.combat.types.BattleStruct24;
import legend.game.combat.types.BttlScriptData6cSub08_3;
import legend.game.combat.types.BttlScriptData6cSub08_4;
import legend.game.combat.types.BttlScriptData6cSub10_2;
import legend.game.combat.types.BttlScriptData6cSub13c;
import legend.game.combat.types.BttlScriptData6cSub14_4;
import legend.game.combat.types.BttlScriptData6cSub14_4Sub70;
import legend.game.combat.types.BttlScriptData6cSub18;
import legend.game.combat.types.BttlScriptData6cSub18Sub3c;
import legend.game.combat.types.BttlScriptData6cSub1c;
import legend.game.combat.types.BttlScriptData6cSub1c_2;
import legend.game.combat.types.BttlScriptData6cSub1c_2Sub1e;
import legend.game.combat.types.BttlScriptData6cSub20_2;
import legend.game.combat.types.BttlScriptData6cSub24;
import legend.game.combat.types.BttlScriptData6cSub24_2;
import legend.game.combat.types.BttlScriptData6cSub30;
import legend.game.combat.types.BttlScriptData6cSub34;
import legend.game.combat.types.BttlScriptData6cSub38;
import legend.game.combat.types.BttlScriptData6cSub38Sub14;
import legend.game.combat.types.BttlScriptData6cSub38Sub14Sub30;
import legend.game.combat.types.BttlScriptData6cSub50;
import legend.game.combat.types.BttlScriptData6cSub50Sub3c;
import legend.game.combat.types.BttlScriptData6cSub5c;
import legend.game.combat.types.BttlScriptData6cSubBase1;
import legend.game.combat.types.DeathDimensionEffect1c;
import legend.game.combat.types.DragoonAdditionScriptData1c;
import legend.game.combat.types.EffeScriptData18;
import legend.game.combat.types.EffeScriptData30;
import legend.game.combat.types.EffeScriptData30Sub06;
import legend.game.combat.types.EffectData98;
import legend.game.combat.types.EffectData98Inner24;
import legend.game.combat.types.EffectData98Sub94;
import legend.game.combat.types.EffectManagerData6c;
import legend.game.combat.types.EffectManagerData6cInner;
import legend.game.combat.types.FrozenJetEffect28;
import legend.game.combat.types.GoldDragoonTransformEffect20;
import legend.game.combat.types.GoldDragoonTransformEffectInstance84;
import legend.game.combat.types.GuardHealEffect14;
import legend.game.combat.types.ScreenDistortionEffectData08;
import legend.game.combat.types.SpriteMetrics08;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptFile;
import legend.game.scripting.ScriptState;
import legend.game.types.ExtendedTmd;
import legend.game.types.Model124;
import legend.game.types.Translucency;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.BiConsumer;

import static legend.core.GameEngine.CPU;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.MEMORY;
import static legend.core.GameEngine.SCRIPTS;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.Scus94491BpeSegment.FUN_80018a5c;
import static legend.game.Scus94491BpeSegment.FUN_80018d60;
import static legend.game.Scus94491BpeSegment.FUN_80018dec;
import static legend.game.Scus94491BpeSegment._1f8003f4;
import static legend.game.Scus94491BpeSegment.displayHeight_1f8003e4;
import static legend.game.Scus94491BpeSegment.displayWidth_1f8003e0;
import static legend.game.Scus94491BpeSegment.free;
import static legend.game.Scus94491BpeSegment.mallocHead;
import static legend.game.Scus94491BpeSegment.mallocTail;
import static legend.game.Scus94491BpeSegment.memcpy;
import static legend.game.Scus94491BpeSegment.playSound;
import static legend.game.Scus94491BpeSegment.projectionPlaneDistance_1f8003f8;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021de4;
import static legend.game.Scus94491BpeSegment_8002.SetRotMatrix;
import static legend.game.Scus94491BpeSegment_8002.SquareRoot0;
import static legend.game.Scus94491BpeSegment_8002.applyModelRotationAndScale;
import static legend.game.Scus94491BpeSegment_8002.playXaAudio;
import static legend.game.Scus94491BpeSegment_8002.rand;
import static legend.game.Scus94491BpeSegment_8002.renderDobj2;
import static legend.game.Scus94491BpeSegment_8003.ApplyMatrix;
import static legend.game.Scus94491BpeSegment_8003.ApplyMatrixLV;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003ec90;
import static legend.game.Scus94491BpeSegment_8003.GetClut;
import static legend.game.Scus94491BpeSegment_8003.GsGetLw;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_8003.MulMatrix0;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_8003faf0;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_8003fd80;
import static legend.game.Scus94491BpeSegment_8003.RotTrans;
import static legend.game.Scus94491BpeSegment_8003.RotTransPers4;
import static legend.game.Scus94491BpeSegment_8003.ScaleMatrix;
import static legend.game.Scus94491BpeSegment_8003.ScaleMatrixL;
import static legend.game.Scus94491BpeSegment_8003.TransMatrix;
import static legend.game.Scus94491BpeSegment_8003.perspectiveTransform;
import static legend.game.Scus94491BpeSegment_8003.perspectiveTransformTriple;
import static legend.game.Scus94491BpeSegment_8003.setRotTransMatrix;
import static legend.game.Scus94491BpeSegment_8004.ApplyTransposeMatrixLV;
import static legend.game.Scus94491BpeSegment_8004.FUN_80040df0;
import static legend.game.Scus94491BpeSegment_8004.FUN_80040e10;
import static legend.game.Scus94491BpeSegment_8004.RotMatrixX;
import static legend.game.Scus94491BpeSegment_8004.RotMatrixY;
import static legend.game.Scus94491BpeSegment_8004.RotMatrixZ;
import static legend.game.Scus94491BpeSegment_8004.RotMatrix_80040010;
import static legend.game.Scus94491BpeSegment_8004.doNothingScript_8004f650;
import static legend.game.Scus94491BpeSegment_8004.ratan2;
import static legend.game.Scus94491BpeSegment_8007.joypadPress_8007a398;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800bf0cf;
import static legend.game.Scus94491BpeSegment_800b.doubleBufferFrame_800bb108;
import static legend.game.Scus94491BpeSegment_800b.model_800bda10;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.stage_800bda0c;
import static legend.game.Scus94491BpeSegment_800c.DISPENV_800c34b0;
import static legend.game.Scus94491BpeSegment_800c.identityMatrix_800c3568;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;
import static legend.game.combat.Bttl_800c.FUN_800cf37c;
import static legend.game.combat.Bttl_800c.FUN_800cf4f4;
import static legend.game.combat.Bttl_800c.FUN_800cf684;
import static legend.game.combat.Bttl_800c.FUN_800cfb94;
import static legend.game.combat.Bttl_800c.FUN_800cfc20;
import static legend.game.combat.Bttl_800c.FUN_800cffd8;
import static legend.game.combat.Bttl_800c._800fb0ec;
import static legend.game.combat.Bttl_800c._800fb954;
import static legend.game.combat.Bttl_800c.callScriptFunction;
import static legend.game.combat.Bttl_800c.currentStage_800c66a4;
import static legend.game.combat.Bttl_800c.deffManager_800c693c;
import static legend.game.combat.Bttl_800c.getHitMultiplier;
import static legend.game.combat.Bttl_800c.scriptGetScriptedObjectPos;
import static legend.game.combat.Bttl_800c.seed_800fa754;
import static legend.game.combat.Bttl_800c.spriteMetrics_800c6948;
import static legend.game.combat.Bttl_800c.stageIndices_800fb064;
import static legend.game.combat.Bttl_800c.tmds_800c6944;
import static legend.game.combat.Bttl_800d.FUN_800dc408;
import static legend.game.combat.Bttl_800d.FUN_800de3f4;
import static legend.game.combat.Bttl_800d.FUN_800de544;
import static legend.game.combat.Bttl_800d.FUN_800de618;
import static legend.game.combat.Bttl_800d.ScaleVectorL_SVEC;
import static legend.game.combat.Bttl_800d.loadModelAnim;
import static legend.game.combat.Bttl_800d.optimisePacketsIfNecessary;
import static legend.game.combat.Bttl_800e.FUN_800e60e0;
import static legend.game.combat.Bttl_800e.FUN_800e6170;
import static legend.game.combat.Bttl_800e.FUN_800e61e4;
import static legend.game.combat.Bttl_800e.FUN_800e62a8;
import static legend.game.combat.Bttl_800e.FUN_800e7dbc;
import static legend.game.combat.Bttl_800e.FUN_800e7ea4;
import static legend.game.combat.Bttl_800e.FUN_800e8594;
import static legend.game.combat.Bttl_800e.FUN_800e8c84;
import static legend.game.combat.Bttl_800e.FUN_800e8d04;
import static legend.game.combat.Bttl_800e.FUN_800e8dd4;
import static legend.game.combat.Bttl_800e.FUN_800e9178;
import static legend.game.combat.Bttl_800e.FUN_800e9428;
import static legend.game.combat.Bttl_800e.FUN_800e95f0;
import static legend.game.combat.Bttl_800e.allocateEffectManager;
import static legend.game.combat.Bttl_800e.applyScreenDarkening;
import static legend.game.combat.Bttl_800e.getDeffPart;
import static legend.game.combat.Bttl_800e.perspectiveTransformXyz;
import static legend.game.combat.Bttl_800e.renderCtmd;

public final class SEffe {
  private SEffe() { }

  private static final Value _800fb794 = MEMORY.ref(2, 0x800fb794L);

  private static final Value _800fb7bc = MEMORY.ref(1, 0x800fb7bcL);

  private static final Value _800fb7c0 = MEMORY.ref(1, 0x800fb7c0L);

  private static final Value _800fb7f0 = MEMORY.ref(1, 0x800fb7f0L);

  private static final Value _800fb7fc = MEMORY.ref(1, 0x800fb7fcL);

  private static final Value _800fb804 = MEMORY.ref(1, 0x800fb804L);

  private static final Value _800fb818 = MEMORY.ref(1, 0x800fb818L);

  private static final Value _800fb82c = MEMORY.ref(1, 0x800fb82cL);

  private static final Value _800fb840 = MEMORY.ref(1, 0x800fb840L);

  private static final Value _800fb84c = MEMORY.ref(1, 0x800fb84cL);

  private static final COLOUR _800fb8cc = MEMORY.ref(2, 0x800fb8ccL, COLOUR::new);
  private static final SVECTOR _800fb8d0 = MEMORY.ref(2, 0x800fb8d0L, SVECTOR::new);

  private static final Value _800fb8fc = MEMORY.ref(4, 0x800fb8fcL);
  private static final Value _800fb910 = MEMORY.ref(4, 0x800fb910L);
  private static final Value _800fb930 = MEMORY.ref(1, 0x800fb930L);

  private static final Value _800fb940 = MEMORY.ref(4, 0x800fb940L);

  private static final SVECTOR _800fb94c = MEMORY.ref(2, 0x800fb94cL, SVECTOR::new);

  /**
   * <ol start="0">
   *   <li>{@link SEffe#FUN_800fce10}</li>
   *   <li>null</li>
   *   <li>{@link SEffe#FUN_800fcf18}</li>
   * </ol>
   */
  private static final Value _801197c0 = MEMORY.ref(4, 0x801197c0L);

  private static final Value _801197ec = MEMORY.ref(1, 0x801197ecL);

  private static final Value _801198f0 = MEMORY.ref(1, 0x801198f0L);

  /**
   * <ol start="0">
   *   <li>{@link SEffe#FUN_800fe120}</li>
   *   <li>{@link SEffe#FUN_800fd600}</li>
   *   <li>{@link SEffe#FUN_800fd87c}</li>
   *   <li>{@link SEffe#FUN_800fddd8}</li>
   *   <li>{@link SEffe#FUN_800fd87c}</li>
   *   <li>{@link SEffe#FUN_800fddd0}</li>
   * </ol>
   */
  private static final BiConsumer<ScriptState<EffectManagerData6c>, EffectManagerData6c>[] _80119b7c = new BiConsumer[6];
  static {
    _80119b7c[0] = SEffe::FUN_800fe120;
    _80119b7c[1] = SEffe::FUN_800fd600;
    _80119b7c[2] = SEffe::FUN_800fd87c;
    _80119b7c[3] = SEffe::FUN_800fddd8;
    _80119b7c[4] = SEffe::FUN_800fd87c;
    _80119b7c[5] = SEffe::FUN_800fddd0;
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
  private static final ArrayRef<Pointer<QuadConsumerRef<EffectData98, EffectData98Sub94, EffectData98Inner24, Integer>>> _80119b94 = MEMORY.ref(4, 0x80119b94L, ArrayRef.of(Pointer.classFor(QuadConsumerRef.classFor(EffectData98.class, EffectData98Sub94.class, EffectData98Inner24.class, int.class)), 6, 4, Pointer.deferred(4, QuadConsumerRef::new)));
  /**
   * <ol start="0">
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c8}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9ec}</li>
   *   <li>{@link SEffe#FUN_800fba58}</li>
   *   <li>{@link SEffe#FUN_800fbb14}</li>
   *   <li>{@link SEffe#FUN_800fbb14}</li>
   *   <li>{@link SEffe#FUN_800fbbe0}</li>
   *   <li>{@link SEffe#FUN_800fbbe0}</li>
   *   <li>{@link SEffe#FUN_800fbd04}</li>
   *   <li>{@link SEffe#FUN_800fbd04}</li>
   *   <li>{@link SEffe#FUN_800fba58}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fbd68}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fbe94}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fbf50}</li>
   *   <li>{@link SEffe#FUN_800fbfd0}</li>
   *   <li>{@link SEffe#FUN_800fc068}</li>
   *   <li>{@link SEffe#FUN_800fc0d0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fc1fc}</li>
   *   <li>{@link SEffe#FUN_800fc068}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fc280}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fc348}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fc410}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fc42c}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fc410}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fc528}</li>
   *   <li>{@link SEffe#FUN_800fc5a8}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fc61c}</li>
   *   <li>{@link SEffe#FUN_800fc6bc}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fc768}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fc7c8}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   *   <li>{@link SEffe#FUN_800fb9c0}</li>
   * </ol>
   */
  private static final ArrayRef<Pointer<TriConsumerRef<EffectManagerData6c, EffectData98, EffectData98Sub94>>> _80119bac = MEMORY.ref(4, 0x80119bacL, ArrayRef.of(Pointer.classFor(TriConsumerRef.classFor(EffectManagerData6c.class, EffectData98.class, EffectData98Sub94.class)), 65, 4, Pointer.deferred(4, TriConsumerRef::new)));
  /**
   * <ol start="0">
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d60}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100e28}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100e28}</li>
   *   <li>{@link SEffe#FUN_80100e28}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100e4c}</li>
   *   <li>{@link SEffe#FUN_80100e28}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100ea0}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   *   <li>{@link SEffe#FUN_80100d58}</li>
   * </ol>
   */
  private static final ArrayRef<Pointer<QuadConsumerRef<ScriptState<EffectManagerData6c>, EffectManagerData6c, EffectData98, EffectData98Sub94>>> _80119cb0 = MEMORY.ref(4, 0x80119cb0L, ArrayRef.of(Pointer.classFor(QuadConsumerRef.classFor(ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class, EffectData98.class, EffectData98Sub94.class)), 65, 4, Pointer.deferred(4, QuadConsumerRef::new)));
  /**
   * <ol start="0">
   *   <li>{@link SEffe#FUN_800fea70}</li>
   *   <li>{@link SEffe#FUN_800fec3c}</li>
   *   <li>{@link SEffe#FUN_800feccc}</li>
   *   <li>{@link SEffe#FUN_800fee9c}</li>
   *   <li>{@link SEffe#FUN_800fefe4}</li>
   *   <li>{@link SEffe#FUN_800ff15c}</li>
   *   <li>{@link SEffe#FUN_800ff3e0}</li>
   *   <li>{@link SEffe#FUN_800ff3e0}</li>
   *   <li>{@link SEffe#FUN_800ff430}</li>
   *   <li>{@link SEffe#FUN_800ff590}</li>
   *   <li>{@link SEffe#FUN_800ff5c4}</li>
   *   <li>{@link SEffe#FUN_800ff6d4}</li>
   *   <li>{@link SEffe#FUN_800ff15c}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_800ff6fc}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_800fea70}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_800ff788}</li>
   *   <li>{@link SEffe#FUN_800ff890}</li>
   *   <li>{@link SEffe#FUN_800ffa80}</li>
   *   <li>{@link SEffe#FUN_800ffadc}</li>
   *   <li>{@link SEffe#FUN_800ffb80}</li>
   *   <li>{@link SEffe#FUN_800ffbd8}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_800ffb80}</li>
   *   <li>{@link SEffe#FUN_800ffe80}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_800ffefc}</li>
   *   <li>{@link SEffe#FUN_800fea70}</li>
   *   <li>{@link SEffe#FUN_800fff04}</li>
   *   <li>{@link SEffe#FUN_800fff30}</li>
   *   <li>{@link SEffe#FUN_800fffa0}</li>
   *   <li>{@link SEffe#FUN_801000b8}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_801000f8}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_80100150}</li>
   *   <li>{@link SEffe#FUN_8010025c}</li>
   *   <li>{@link SEffe#FUN_8010025c}</li>
   *   <li>{@link SEffe#FUN_80100364}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_801003e8}</li>
   *   <li>{@link SEffe#FUN_801005b8}</li>
   *   <li>{@link SEffe#FUN_801007b4}</li>
   *   <li>{@link SEffe#FUN_80100800}</li>
   *   <li>{@link SEffe#FUN_80100878}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_801008f8}</li>
   *   <li>{@link SEffe#FUN_80100978}</li>
   *   <li>{@link SEffe#FUN_80100af4}</li>
   *   <li>{@link SEffe#FUN_80100bb4}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_80100c18}</li>
   *   <li>{@link SEffe#FUN_80100cac}</li>
   *   <li>{@link SEffe#FUN_80100cec}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_800fea70}</li>
   *   <li>{@link SEffe#FUN_800fea68}</li>
   *   <li>{@link SEffe#FUN_80100d00}</li>
   * </ol>
   */
  private static final ArrayRef<Pointer<QuadConsumerRef<EffectManagerData6c, EffectData98, EffectData98Sub94, EffectData98Inner24>>> _80119db4 = MEMORY.ref(4, 0x80119db4L, ArrayRef.of(Pointer.classFor(QuadConsumerRef.classFor(EffectManagerData6c.class, EffectData98.class, EffectData98Sub94.class, EffectData98Inner24.class)), 65, 4, Pointer.deferred(4, QuadConsumerRef::new)));

  /**
   * <ol start="0">
   *   <li>{@link SEffe#FUN_801052d4}</li>
   *   <li>{@link SEffe#FUN_801052d4}</li>
   *   <li>{@link SEffe#FUN_801052d4}</li>
   *   <li>{@link SEffe#FUN_801052d4}</li>
   *   <li>{@link SEffe#FUN_801052d4}</li>
   *   <li>{@link SEffe#FUN_801052d4}</li>
   *   <li>{@link SEffe#FUN_801052d4}</li>
   *   <li>{@link SEffe#FUN_801052d4}</li>
   *   <li>{@link SEffe#FUN_801052d4}</li>
   *   <li>{@link SEffe#FUN_801052d4}</li>
   *   <li>{@link SEffe#FUN_801052d4}</li>
   * </ol>
   */
  private static final ArrayRef<Pointer<QuadConsumerRef<EffectManagerData6c, BttlScriptData6cSub38, BttlScriptData6cSub38Sub14, Integer>>> _80119ebc = MEMORY.ref(4, 0x80119ebcL, ArrayRef.of(Pointer.classFor(QuadConsumerRef.classFor(EffectManagerData6c.class, BttlScriptData6cSub38.class, BttlScriptData6cSub38Sub14.class, int.class)), 11, 4, Pointer.deferred(4, QuadConsumerRef::new)));
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
  private static final ArrayRef<Pointer<QuadConsumerRef<EffectManagerData6c, BttlScriptData6cSub38, BttlScriptData6cSub38Sub14, Integer>>> _80119ee8 = MEMORY.ref(4, 0x80119ee8L, ArrayRef.of(Pointer.classFor(QuadConsumerRef.classFor(EffectManagerData6c.class, BttlScriptData6cSub38.class, BttlScriptData6cSub38Sub14.class, int.class)), 11, 4, Pointer.deferred(4, QuadConsumerRef::new)));
  /**
   * <ol start="0">
   *   <li>{@link SEffe#FUN_801030d8}</li>
   *   <li>{@link SEffe#FUN_801030d8}</li>
   *   <li>{@link SEffe#FUN_801030d8}</li>
   *   <li>{@link SEffe#FUN_80103db0}</li>
   *   <li>{@link SEffe#FUN_80103db0}</li>
   *   <li>{@link SEffe#FUN_80103db0}</li>
   *   <li>{@link SEffe#FUN_80103db0}</li>
   *   <li>{@link SEffe#FUN_80103db0}</li>
   *   <li>{@link SEffe#FUN_80103db0}</li>
   *   <li>{@link SEffe#FUN_80103db0}</li>
   *   <li>{@link SEffe#FUN_80103db0}</li>
   * </ol>
   */
  private static final BiConsumer<ScriptState<EffectManagerData6c>, EffectManagerData6c>[] _80119f14 = new BiConsumer[11];
  static {
    _80119f14[0] = SEffe::FUN_801030d8;
    _80119f14[1] = SEffe::FUN_801030d8;
    _80119f14[2] = SEffe::FUN_801030d8;
    _80119f14[3] = SEffe::FUN_80103db0;
    _80119f14[4] = SEffe::FUN_80103db0;
    _80119f14[5] = SEffe::FUN_80103db0;
    _80119f14[6] = SEffe::FUN_80103db0;
    _80119f14[7] = SEffe::FUN_80103db0;
    _80119f14[8] = SEffe::FUN_80103db0;
    _80119f14[9] = SEffe::FUN_80103db0;
    _80119f14[10] = SEffe::FUN_80103db0;
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
   *   <li>{@link SEffe#FUN_8010bc60}</li>
   * </ol>
   */
  private static final ArrayRef<Pointer<BiConsumerRef<EffectManagerData6c, DeathDimensionEffect1c>>> deathDimensionRenderers_80119fec = MEMORY.ref(4, 0x80119fecL, ArrayRef.of(Pointer.classFor(BiConsumerRef.classFor(EffectManagerData6c.class, DeathDimensionEffect1c.class)), 2, 4, Pointer.deferred(4, BiConsumerRef::new)));

  private static final Value _80119f40 = MEMORY.ref(1, 0x80119f40L);
  private static final Value _80119f41 = MEMORY.ref(1, 0x80119f41L);
  private static final Value _80119f42 = MEMORY.ref(1, 0x80119f42L);

  /** Struct or something, 0x1c bytes */
  private static final Value _80119f44 = MEMORY.ref(1, 0x80119f44L);
  /** Struct or something, 0x1c bytes */
  private static final Value _80119f60 = MEMORY.ref(1, 0x80119f60L);
  /** Struct or something, 0x1c bytes */
  private static final Value _80119f7c = MEMORY.ref(1, 0x80119f7cL);
  /** Struct or something, 0x1c bytes */
  private static final Value _80119f98 = MEMORY.ref(1, 0x80119f98L);

  private static final Value _80119fb4 = MEMORY.ref(1, 0x80119fb4L);

  private static final ArrayRef<UnsignedByteRef> _80119fbc = MEMORY.ref(1, 0x80119fbcL, ArrayRef.of(UnsignedByteRef.class, 8, 1, UnsignedByteRef::new));
  private static final ArrayRef<UnsignedByteRef> _80119fc4 = MEMORY.ref(1, 0x80119fc4L, ArrayRef.of(UnsignedByteRef.class, 8, 1, UnsignedByteRef::new));

  /**
   * <ol start="0">
   *   <li>{@link SEffe#FUN_8010fa4c}</li>
   *   <li>{@link SEffe#FUN_8010fa88}</li>
   *   <li>{@link SEffe#FUN_8010fbd4}</li>
   *   <li>{@link SEffe#FUN_8010fd34}</li>
   *   <li>{@link SEffe#FUN_8010fe84}</li>
   * </ol>
   */
  private static final ArrayRef<Pointer<BiConsumerRef<EffectManagerData6c, BttlScriptData6cSub14_4Sub70>>> _80119ff4 = MEMORY.ref(4, 0x80119ff4L, ArrayRef.of(Pointer.classFor(BiConsumerRef.classFor(EffectManagerData6c.class, BttlScriptData6cSub14_4Sub70.class)), 5, 4, Pointer.deferred(4, BiConsumerRef::new)));

  private static final Value _8011a008 = MEMORY.ref(4, 0x8011a008L);
  private static final Pointer<EffectData98> _8011a00c = MEMORY.ref(4, 0x8011a00cL, Pointer.deferred(4, EffectData98::new));
  private static final Pointer<EffectData98> _8011a010 = MEMORY.ref(4, 0x8011a010L, Pointer.deferred(4, EffectData98::new));
  private static final Value _8011a014 = MEMORY.ref(1, 0x8011a014L);

  private static final Value _8011a01c = MEMORY.ref(4, 0x8011a01cL);
  private static final Value _8011a020 = MEMORY.ref(4, 0x8011a020L);
  private static final Value _8011a024 = MEMORY.ref(4, 0x8011a024L);
  private static final Value _8011a028 = MEMORY.ref(4, 0x8011a028L);
  private static final Value _8011a02c = MEMORY.ref(4, 0x8011a02cL);

  private static final Value _8011a030 = MEMORY.ref(1, 0x8011a030L);

  private static final ArrayRef<IntRef> _8011a034 = MEMORY.ref(4, 0x8011a034L, ArrayRef.of(IntRef.class, 5, 4, IntRef::new));
  private static final Value _8011a048 = MEMORY.ref(1, 0x8011a048L);

  @Method(0x800fb95cL)
  public static void FUN_800fb95c(final EffectData98Sub94 a0) {
    a0._50.setX((short)(rcos(a0._14.get()) * a0._16.get() >> 12));
    a0._50.setZ((short)(rsin(a0._14.get()) * a0._16.get() >> 12));
  }

  @Method(0x800fb9c0L)
  public static void FUN_800fb9c0(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2) {
    // no-op
  }

  @Method(0x800fb9c8L)
  public static void FUN_800fb9c8(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2) {
    a2._58.x.sub(a2._16.get());
    a2._58.y.add(a2._14.get());
  }

  @Method(0x800fb9ecL)
  public static void FUN_800fb9ec(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2) {
    FUN_800fb95c(a2);

    a2._14.add(a2._18.get());
    a2._16.add(a2._1a.getX());
    a2._58.y.sub((short)2);

    if(a2._1a.getX() >= 8) {
      a2._1a.y.sub((short)8);
    }
  }

  @Method(0x800fba58L)
  public static void FUN_800fba58(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2) {
    FUN_800fb95c(a2);

    a2._50.x.add((short)(rcos(a2._1a.getX()) * a2._1a.getY() / 0x1000));
    a2._50.z.add((short)(rsin(a2._1a.getX()) * a2._1a.getY() / 0x1000));
    a2._16.add(a2._18.get());

    if(a2._18.get() >= 4) {
      a2._18.sub((short)4);
    }

    //LAB_800fbaf0
    a2._1a.x.add(a2._1a.getZ());
  }

  @Method(0x800fbb14L)
  public static void FUN_800fbb14(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2) {
    FUN_800fb95c(a2);

    a2._50.x.add((short)(rcos(a2._1a.getX()) * a2._1a.getY() >> 12));
    a2._50.y.add((short)(rsin(a2._1a.getX()) * a2._1a.getY() >> 12));

    if(a2._16.get() >= a2._18.get() * 4) {
      a2._16.sub(a2._18.get());

      if(a2._18.get() >= 4) {
        a2._18.sub((short)4);
      }
    }

    //LAB_800fbbbc
    a2._1a.x.add(a2._1a.z.get());
  }

  @Method(0x800fbbe0L)
  public static void FUN_800fbbe0(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2) {
    FUN_800fb95c(a2);
    a2._16.add(a2._1a.getY());
    a2._58.y.add((short)20);
    a2._10.sub(a2._24.get());
    if(a2._18.get() == 1) {
      if(a2._22.get() != 0) {
        //LAB_800fbca4
        a2._50.setY((short)-a2._2c.getY());
        a2._22.decr();
      } else {
        a2._50.setY((short)((rsin(a2._1a.getX()) * a2._20.get() >> 12) - (short)a2._2c.getY()));
        a2._1a.x.add((short)0x7f);
        a2._0a.set(a2._1a.getZ());
        a2._0c.set(a2._1a.getZ());
        a2._1a.y.add((short)5);
        a2._20.add((short)20);
      }
      //LAB_800fbcc0
    } else if(a2._2c.getY() + a2._50.getY() >= -1000) {
      a2._1a.setY((short)0);
      a2._18.set((short)1);
      a2._0a.set(a2._1a.getZ());
      a2._0c.set(a2._1a.getZ());
    }

    //LAB_800fbcf4
  }

  @Method(0x800fbd04L)
  public static void FUN_800fbd04(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2) {
    FUN_800fb95c(a2);

    a2._16.add(a2._18.get());
    if(a2._18.get() >= 3) {
      a2._18.sub((short)3);
    }

    //LAB_800fbd44
    a2._14.add(a2._1a.getX());
  }

  @Method(0x800fbd68L)
  public static void FUN_800fbd68(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2) {
    final SVECTOR sp0x38 = new SVECTOR();
    if(a2._18.get() == 0) {
      FUN_800fb95c(a2);
      a2._50.setY((short)0);
      a2._50.z.shl(1);
      sp0x38.set(a2._1a.getX(), (short)0, (short)0);
    } else {
      //LAB_800fbdb8
      a2._50.setX((short)0);
      a2._50.setY((short)(rsin(a2._14.get()) * a2._16.get() >> 11));
      a2._50.setZ((short)(rcos(a2._14.get()) * a2._16.get() >> 12));
      sp0x38.set((short)0, (short)0, a2._1a.getX());
    }

    //LAB_800fbe10
    a2._14.sub((short)0x80);
    a2._1a.x.sub((short)0x8);

    final VECTOR sp0x18 = new VECTOR().set(a2._50);
    final VECTOR sp0x28 = new VECTOR();
    FUN_800cf37c(a0, sp0x38, sp0x18, sp0x28);
    a2._50.set(sp0x28);
  }

  @Method(0x800fbe94L)
  public static void FUN_800fbe94(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2) {
    FUN_800fb95c(a2);
    a2._50.setY((short)0);
    a2._50.z.shl(1);
    final SVECTOR sp0x38 = new SVECTOR().set(a2._18.get(), (short)0, a2._1a.getX());
    a2._14.add((short)0x80);
    final VECTOR sp0x18 = new VECTOR().set(a2._50);
    final VECTOR sp0x28 = new VECTOR();
    FUN_800cf37c(a0, sp0x38, sp0x18, sp0x28);
    a2._50.set(sp0x28);
  }

  @Method(0x800fbf50L)
  public static void FUN_800fbf50(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2) {
    FUN_800fb95c(a2);

    a2._16.mul(a2._18.get()).shra(8);

    final int v1 = a2._1a.getY() >> 2;
    if(a2._16.get() < v1) {
      a2._16.set((short)v1);
    }

    //LAB_800fbfac
    a2._14.add(a2._1a.getX());
  }

  @Method(0x800fbfd0L)
  public static void FUN_800fbfd0(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2) {
    a2._50.setY((short)(rsin(a2._14.get()) * a2._16.get() >> 12));
    a2._50.setZ((short)(rcos(a2._14.get()) * a2._16.get() >> 12));
    a2._16.add(a2._18.get());
    if(a2._16.get() < 0) {
      a2._16.set((short)0);
    }

    //LAB_800fc044
    a2._14.add(a2._1a.getX());
  }

  @Method(0x800fc068L)
  public static void FUN_800fc068(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2) {
    FUN_800fb95c(a2);

    a2._16.add(a2._18.get());

    if(a2._50.getY() + a2._2c.getY() >= a0._10._30) {
      a2._12.set((short)1);
    }

    //LAB_800fc0bc
  }

  @Method(0x800fc0d0L)
  public static void FUN_800fc0d0(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2) {
    if(a2._50.getY() + a2._2c.getY() >= -400 && a2._14.get() == 0) {
      a2._14.set((short)1);
      a2._60.setY((short)-8);
      //LAB_800fc11c
    } else if(a1.scriptIndex_04.get() != -1 && a2._14.get() == 0) {
      final VECTOR sp0x10 = new VECTOR();
      scriptGetScriptedObjectPos(a1.scriptIndex_04.get(), sp0x10);
      a2._58.setX((short)((sp0x10.getX() - (a2._3c.getX() + a2._2c.getX())) / a2._1a.getZ()));
      a2._58.setY((short)((sp0x10.getY() - (a2._3c.getY() + a2._2c.getY())) / a2._1a.getZ()));
      a2._58.setZ((short)((sp0x10.getZ() - (a2._3c.getZ() + a2._2c.getZ())) / a2._1a.getZ()));
      a2._58.x.add(a2._18.get());
      a2._58.y.add(a2._1a.getX());
      a2._58.z.add(a2._1a.getY());
    }

    //LAB_800fc1ec
  }

  @Method(0x800fc1fcL)
  public static void FUN_800fc1fc(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2) {
    FUN_800fb95c(a2);

    a2._16.add(a2._18.get());
    if(a2._50.getY() + a2._2c.getY() >= a0._10._30) {
      a2._50.setY((short)(a0._10._30 - a2._2c.getY()));
      a2._58.y.neg().shra(1);
    }

    //LAB_800fc26c
  }

  @Method(0x800fc280L)
  public static void FUN_800fc280(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2) {
    a2._50.setZ((short)(rcos(a2._14.get()) * 2 * a2._1a.getY() >> 12));
    a2._50.setX((short)(rsin(a2._1a.getZ()) * a2._1a.getY() >> 12));
    a2._14.add(a2._16.get());
    a2._1a.setZ((short)(a2._1a.getZ() + a2._16.get() * 2));
    a2._50.y.add((short)(0x40 + (((rcos(a2._18.get()) >> 1) + 0x800) * a2._1a.getY() >> 15)));
    a2._18.add(a2._1a.getX());
  }

  @Method(0x800fc348L)
  public static void FUN_800fc348(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2) {
    a2._50.setX((short)((rcos(a2._14.get()) * a2._1a.getX() >> 12) * rsin(a2._18.get()) >> 12));
    a2._50.setY((short)(a2._18.get() * 2 - 0x800));
    a2._50.setZ((short)((rsin(a2._14.get()) * a2._1a.getX() >> 12) * rsin(a2._18.get()) >> 12));
    a2._14.add(a2._16.get());
  }

  @Method(0x800fc410L)
  public static void FUN_800fc410(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2) {
    a2._58.y.add((short)(a2._14.get() / 0x100));
  }

  @Method(0x800fc42cL)
  public static void FUN_800fc42c(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2) {
    a2._50.setX((short)(a2._14.get() + (rcos(a2._1a.getX()) * a2._1a.getZ() >> 12)));
    a2._50.setY((short)(a2._18.get() + (rsin(a2._1a.getX()) * a2._1a.getZ() >> 12)));
    a2._1a.z.add((short)16);
    a2._1a.x.add(a2._1a.getY());
  }

  @Method(0x800fc4bcL)
  public static void FUN_800fc4bc(final MATRIX a0, final EffectManagerData6c a1, final long a2) {
    RotMatrix_8003faf0(MEMORY.ref(4, a2 + 0x38L, SVECTOR::new), a0);
    TransMatrix(a0, MEMORY.ref(4, a2 + 0x18L, VECTOR::new));
    ScaleMatrixL(a0, MEMORY.ref(4, a2 + 0x28L, VECTOR::new));
  }

  @Method(0x800fc528L)
  public static void FUN_800fc528(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2) {
    a2._50.setX((short)(rsin(a2._14.get()) * (a2._18.get() >> 1) >> 12));
    a2._50.setZ((short)(rcos(a2._14.get()) * a2._18.get() >> 12));
    a2._14.add(a2._16.get());
  }

  @Method(0x800fc5a8L)
  public static void FUN_800fc5a8(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2) {
    a2._50.setX((short)(rsin(a2._14.get()) * a2._18.get() >> 12));
    a2._50.setZ((short)(rcos(a2._14.get()) * a2._18.get() >> 12));
    a2._18.mul((short)7).div((short)8);
  }

  @Method(0x800fc61cL)
  public static void FUN_800fc61c(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2) {
    a2._08.set((short)(rcos(a2._14.get()) * a2._16.get() >> 12));
    a2._14.sub(a2._18.get());
    a2._08.set((short)(a2._08.get() * a0._10.scale_16.getY() >> 12));

    if(a2._16.get() > 0) {
      a2._16.sub(a2._1a.getX());
    }

    //LAB_800fc6a8
  }

  @Method(0x800fc6bcL)
  public static void FUN_800fc6bc(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2) {
    a2._50.setX((short)(rsin(a2._14.get()) * a2._16.get() >> 12));
    a2._50.setZ((short)(rcos(a2._14.get()) * a2._16.get() >> 12));
    a2._50.x.add((short)(rsin(a2._18.get()) << 8 >> 12));
    a2._50.z.add((short)(rcos(a2._18.get()) << 8 >> 12));
    a2._18.add(a2._1a.getX());
  }

  @Method(0x800fc768L)
  public static void FUN_800fc768(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2) {
    a2._1a.x.add(a2._14.get());
    a2._1a.y.add(a2._16.get());
    a2._1a.z.add(a2._18.get());
    a2._58.setX((short)(a2._1a.getX() >> 8));
    a2._58.setY((short)(a2._1a.getY() >> 8));
    a2._58.setZ((short)(a2._1a.getZ() >> 8));
  }

  @Method(0x800fc7c8L)
  public static void FUN_800fc7c8(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2) {
    if(a2._50.getY() + a2._2c.getY() >= a0._10._30) {
      a2._50.setY((short)(a0._10._30 - a2._2c.getY()));
      a2._58.y.neg().shra(1);
      if(a2._14.get() == 0) {
        final int angle = (int)(seed_800fa754.advance().get() % 0x1001);
        a2._58.setX((short)(rcos(angle) >>> 8));
        a2._58.setZ((short)(rcos(angle) >>> 8));
        a2._58.x.mul((short)MEMORY.ref(4, a1.getAddress()).offset(0x20L).get()).shra(8); //TODO
        a2._58.z.mul((short)MEMORY.ref(4, a1.getAddress()).offset(0x20L).get()).shra(8); //TODO
      }

      //LAB_800fc8d8
      a2._14.set((short)1);
    }

    //LAB_800fc8e0
  }

  @Method(0x800fc8f8L)
  public static void FUN_800fc8f8(@Nullable VECTOR a0, @Nullable final SVECTOR a1) {
    if(a0 == null) {
      a0 = new VECTOR();
    }

    //LAB_800fc920
    final MATRIX s1 = worldToScreenMatrix_800c3548;
    final VECTOR sp0x30 = new VECTOR().set(s1.transfer).negate();
    ApplyTransposeMatrixLV(s1, sp0x30, a0);

    if(a1 != null) {
      sp0x30.set(s1.transfer).negate();
      sp0x30.z.add(0x1000);

      final VECTOR sp0x10 = new VECTOR();
      ApplyTransposeMatrixLV(s1, sp0x30, sp0x10);
      sp0x10.sub(a0);
      final short angle = (short)ratan2(sp0x10.getX(), sp0x10.getZ());
      a1.setY(angle);

      //LAB_800fca44
      a1.setX((short)ratan2(-sp0x10.getY(), (rcos(-angle) * sp0x10.getZ() - rsin(-angle) * sp0x10.getX()) / 0x1000));
      a1.setZ((short)0);
    }

    //LAB_800fca5c
  }

  /** Returns Z */
  @Method(0x800fca78L)
  public static int FUN_800fca78(final EffectManagerData6c s3, final EffectData98 fp, final EffectData98Sub94 s1, final VECTOR s2, final GpuCommandPoly cmd) {
    final ShortRef refX = new ShortRef();
    final ShortRef refY = new ShortRef();
    final int z = FUN_800cfc20(s1._68, s1._2c, s2, refX, refY);
    if(z >= 40) {
      final int a0 = 0x50_0000 / z;
      final int s6 = a0 * (s3._10.scale_16.getX() + s1._06.get()) >> 12;
      final int s7 = a0 * (s3._10.scale_16.getY() + s1._08.get()) >> 12;

      int angle = s1._0e.get() + s3._10.rot_10.getX() - 0xa00;

      if((s3._10._24 & 0x2) != 0) {
        final VECTOR sp0x28 = new VECTOR();
        final SVECTOR sp0x38 = new SVECTOR();
        FUN_800fc8f8(sp0x28, sp0x38);

        //LAB_800fcb90
        //LAB_800fcbd4
        final int sp18 = (s1._48.getZ() - s2.getZ()) * -Math.abs(rsin(sp0x38.getY() - s3._10.rot_10.getY())) / 0x1000 - (s2.getX() - s1._48.getX()) * -Math.abs(rcos(sp0x38.getY() + s3._10.rot_10.getY())) / 0x1000;
        final int sp1c = s2.getY() - s1._48.getY();
        angle = -ratan2(sp1c, sp18) + 0x400;
        s1._48.set(s2);
      }

      //LAB_800fcc20
      final int a1 = fp.w_5e.get() / 2 * s6;
      final int v1 = fp.h_5f.get() / 2 * s7;
      final int sp40 = -a1 >> 8;
      final int sp48 = a1 >> 8;
      final int sp42 = -v1 >> 8;
      final int sp4a = v1 >> 8;
      final int cos = rcos(angle);
      final int sin = rsin(angle);
      final int sp50 = (short)sp40 * sin >> 12;
      final int sp60 = (short)sp40 * cos >> 12;
      final int sp58 = (short)sp48 * sin >> 12;
      final int sp68 = (short)sp48 * cos >> 12;
      final int sp62 = (short)sp42 * cos >> 12;
      final int sp52 = (short)sp42 * sin >> 12;
      final int sp5a = (short)sp4a * sin >> 12;
      final int sp6a = (short)sp4a * cos >> 12;
      final short x = refX.get();
      final short y = refY.get();
      cmd.pos(0, x + sp60 - sp52, y + sp62 + sp50);
      cmd.pos(1, x + sp68 - sp52, y + sp62 + sp58);
      cmd.pos(2, x + sp60 - sp5a, y + sp6a + sp50);
      cmd.pos(3, x + sp68 - sp5a, y + sp6a + sp58);
    }

    //LAB_800fcde0
    return z;
  }

  @Method(0x800fce10L)
  public static void FUN_800fce10(final EffectManagerData6c a0, final long a1) {
    if((int)MEMORY.ref(4, a1).offset(0x0L).get() >= 0) {
      GPU.queueCommand(((int)MEMORY.ref(4, a1).offset(0x04L).get() + a0._10.z_22) / 4, new GpuCommandLine()
        .translucent(Translucency.B_PLUS_F)
        .rgb(0, 0x40, 0x41, 0x42)
        .rgb(1, (int)MEMORY.ref(1, a1).offset(0x44L).get(), (int)MEMORY.ref(1, a1).offset(0x45L).get(), (int)MEMORY.ref(1, a1).offset(0x46L).get())
        .pos(0, (int)MEMORY.ref(2, a1).offset(0x08L).get(), (int)MEMORY.ref(2, a1).offset(0x10L).get())
        .pos(1, (int)MEMORY.ref(2, a1).offset(0x0cL).get(), (int)MEMORY.ref(2, a1).offset(0x14L).get())
      );
    }

    //LAB_800fcf08
  }

  @Method(0x800fcf18L)
  public static void FUN_800fcf18(final EffectManagerData6c a0, final long a1) {
    // no-op
  }

  @Method(0x800fcf20L)
  public static void FUN_800fcf20(final EffectManagerData6c a0, final TmdObjTable tmd, final long a2, final int tpage) {
    if(MEMORY.ref(4, a2).offset(0x0L).getSigned() >= 0) {
      final MATRIX sp0x10 = new MATRIX();
      FUN_800fc4bc(sp0x10, a0, a2);
      if((MEMORY.ref(4, a2).offset(0x0L).get() & 0x40L) == 0) {
        FUN_800e61e4((int)(MEMORY.ref(1, a2).offset(0x40L).getSigned() << 5), (int)(MEMORY.ref(1, a2).offset(0x41L).getSigned() << 5), (int)(MEMORY.ref(1, a2).offset(0x42L).getSigned() << 5));
      }

      //LAB_800fcf94
      GsSetLightMatrix(sp0x10);
      final MATRIX sp0x30 = new MATRIX();
      MulMatrix0(worldToScreenMatrix_800c3548, sp0x10, sp0x30);
      if((MEMORY.ref(4, a2).offset(0x0L).get() & 0x400_0000L) == 0) {
        RotMatrix_8003faf0(a0._10.rot_10, sp0x30);
        ScaleMatrixL(sp0x30, new VECTOR().set(a0._10.scale_16));
      }

      //LAB_800fcff8
      setRotTransMatrix(sp0x30);
      zOffset_1f8003e8.set(0);
      if((a0._10.flags_00 & 0x4000_0000L) != 0) {
        tmdGp0Tpage_1f8003ec.set(a0._10.flags_00 >>> 23 & 0x60);
      } else {
        //LAB_800fd038
        tmdGp0Tpage_1f8003ec.set(tpage);
      }

      //LAB_800fd040
      final GsDOBJ2 sp0x60 = new GsDOBJ2();
      sp0x60.attribute_00 = (int)MEMORY.ref(4, a2).offset(0x0L).get();
      sp0x60.tmd_08 = tmd;
      renderCtmd(sp0x60);
      if((MEMORY.ref(4, a2).offset(0x0L).get() & 0x40L) == 0) {
        FUN_800e62a8();
      }
    }

    //LAB_800fd064
  }

  @Method(0x800fd084L)
  public static void FUN_800fd084(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2) {
    a2._2c.set(a0._10.trans_04);
    a2._68.set(a0._10.rot_10);

    if(a2._12.get() == 0) {
      a2._12.set((short)1);
    }

    if((a1._08._1c.get() & 0x400_0000L) != 0) {
      a2._84.set((short)(a0._10.colour_1c.getX() << 8));
      a2._86.set((short)(a0._10.colour_1c.getY() << 8));
      a2._88.set((short)(a0._10.colour_1c.getZ() << 8));

      if((a0._10._24 & 0x1) == 0) {
        a2._8a.set((short)0);
        a2._8c.set((short)0);
        a2._8e.set((short)0);
        return;
      }
    }

    //LAB_800fd18c
    //LAB_800fd0dc
    a2._8a.set((short)(a2._84.get() / a2._12.get()));
    a2._8c.set((short)(a2._86.get() / a2._12.get()));
    a2._8e.set((short)(a2._88.get() / a2._12.get()));

    //LAB_800fd1d4
  }

  @Method(0x800fd1dcL)
  public static void FUN_800fd1dc(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final VECTOR a3) {
    if((a1._08._1c.get() & 0x800_0000L) == 0 || (a0._10._24 & 0x1) != 0) {
      //LAB_800fd23c
      a2._84.sub(a2._8a.get());
      a2._86.sub(a2._8c.get());
      a2._88.sub(a2._8e.get());
    } else {
      a2._84.set((short)(a0._10.colour_1c.getX() << 8));
      a2._86.set((short)(a0._10.colour_1c.getY() << 8));
      a2._88.set((short)(a0._10.colour_1c.getZ() << 8));
    }

    //LAB_800fd26c
    a3.setX(a2._84.get());
    a3.setY(a2._86.get());
    a3.setZ(a2._88.get());
    a2._50.add(a2._58);
    a2._58.add(a2._60);

    if(a2._50.getY() + a2._2c.getY() >= a0._10._30) {
      if((a0._10._24 & 0x20) != 0) {
        a2._12.set((short)1);
      }

      //LAB_800fd324
      if((a0._10._24 & 0x8) != 0) {
        a2._50.setY((short)(a0._10._30 - a2._2c.getY()));
        a2._58.setY((short)(-a2._58.getY() / 2));
      }
    }

    //LAB_800fd358
    if((a1._08._1c.get() & 0x200_0000L) == 0) {
      a2._06.add(a2._0a.get());
      a2._08.add(a2._0c.get());
    }

    //LAB_800fd38c
    if((a1._08._1c.get() & 0x100_0000L) == 0) {
      a2._0e.add(a2._10.get());
      a2._70.add(a2._78);
    } else {
      //LAB_800fd3e4
      a2._0e.set((short)(a0._10._24 >>> 12 & 0xff0));
    }

    //LAB_800fd3f8
    a2._58.y.add((short)(a0._10._2c >> 8));

    if(a1._6c.get() != 0) {
      a2._58.x.add((short)(a1.vec_70.getX() >> 8));
      a2._58.y.add((short)(a1.vec_70.getY() >> 8));
      a2._58.z.add((short)(a1.vec_70.getZ() >> 8));
    }

    //LAB_800fd458
  }

  @Method(0x800fd460L)
  public static long FUN_800fd460(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c a1, final EffectData98 a2, final EffectData98Sub94 a3) {
    a3._04.decr();

    final long s0 = a3._04.get();

    //LAB_800fd53c
    if(s0 == -0xfffL) {
      a3._04.set((short)-0xfff);
    } else if(s0 == 0) {
      FUN_800fd084(a1, a2, a3);

      if((a1._10._24 & 0x10) != 0) {
        a3._50.setY((short)0);
        final long v1 = a2._60.get();

        if(v1 == 0x2L || v1 == 0x5L) {
          //LAB_800fd4f0
          //LAB_800fd504
          for(int i = 0; i < a2._54.get(); i++) {
            a3._44.deref().get(i).setY((short)0);
          }
        }
      }

      //LAB_800fd520
      if((a1._10._24 & 0x40) != 0) {
        a3._58.setY((short)0);
      }
    }

    //LAB_800fd54c
    a2._88.deref().run(state, a1, a2, a3);

    if((a3._90.get() & 0x1L) == 0) {
      return 0x1L;
    }

    if(a3._12.get() > 0) {
      a3._12.decr();
    }

    //LAB_800fd58c
    if(a3._12.get() == 0 && (a1._10._24 & 0x80) == 0) {
      a3._90.and(0xffff_fffeL);
      a2._90.deref().run(state, a1, a2, a3);
      return 0x1L;
    }

    //LAB_800fd5e0
    return 0;
  }

  @Method(0x800fd600L)
  public static void FUN_800fd600(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final EffectData98 s1 = (EffectData98)data.effect_44;
    s1._52.incr();

    //LAB_800fd660
    for(int i = 0; i < s1.count_50.get(); i++) {
      final EffectData98Sub94 s3 = s1._68.deref().get(i);
      if(FUN_800fd460(state, data, s1, s3) == 0) {
        s1._84.deref().run(data, s1, s3);
        final VECTOR sp0x48 = new VECTOR();
        FUN_800fd1dc(data, s1, s3, sp0x48);

        final VECTOR sp0x28 = new VECTOR();
        FUN_800cf37c(data, new SVECTOR(), new VECTOR().set(s3._50), sp0x28);

        final Memory.TemporaryReservation tmp = MEMORY.temp(0x48);
        final Value sp0x68 = tmp.get();
        sp0x68.offset(0x00L).setu(data._10.flags_00 & 0xffff_ffffL);
        sp0x68.offset(0x18L).setu(s3._2c.getX() + sp0x28.getX());
        sp0x68.offset(0x1cL).setu(s3._2c.getY() + sp0x28.getY());
        sp0x68.offset(0x20L).setu(s3._2c.getZ() + sp0x28.getZ());
        sp0x68.offset(0x28L).setu(data._10.scale_16.getX() + s3._06.get());
        sp0x68.offset(0x2cL).setu(data._10.scale_16.getY() + s3._08.get());
        sp0x68.offset(0x30L).setu(data._10.scale_16.getX() + s3._06.get()); // This is correct
        sp0x68.offset(0x38L).setu(s3._70.getX() + s3._68.getX());
        sp0x68.offset(0x3aL).setu(s3._70.getY() + s3._68.getY());
        sp0x68.offset(0x3cL).setu(s3._70.getZ() + s3._68.getZ());
        sp0x68.offset(1, 0x40L).setu(sp0x48.getX() >> 8);
        sp0x68.offset(1, 0x41L).setu(sp0x48.getY() >> 8);
        sp0x68.offset(1, 0x42L).setu(sp0x48.getZ() >> 8);
        sp0x68.offset(1, 0x44L).setu(0);
        sp0x68.offset(1, 0x45L).setu(0);
        sp0x68.offset(1, 0x46L).setu(0);
        FUN_800fcf20(data, s1.tmd_30.deref(), sp0x68.getAddress(), s1.tpage_56.get());
        tmp.release();
      }

      //LAB_800fd7e0
    }

    //LAB_800fd7fc
    if(s1._6c.get() != 0) {
      s1.vec_70.setX(s1.vec_70.getX() * s1._80.get() >> 8);
      s1.vec_70.setY(s1.vec_70.getY() * s1._80.get() >> 8);
      s1.vec_70.setZ(s1.vec_70.getZ() * s1._80.get() >> 8);
    }

    //LAB_800fd858
  }

  @Method(0x800fd87cL)
  public static void FUN_800fd87c(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    long v0;
    long v1;
    long a0;
    long s3;

    final EffectData98 s2 = (EffectData98)data.effect_44;
    s2._52.incr();

    if(s2.count_50.get() != 0) {
      final Memory.TemporaryReservation sp0x50tmp = MEMORY.temp(0x48);
      final Value sp0x50 = sp0x50tmp.get();

      //LAB_800fd8dc
      for(int s7 = 0; s7 < s2.count_50.get(); s7++) {
        final EffectData98Sub94 s5 = s2._68.deref().get(s7);

        if(FUN_800fd460(state, data, s2, s5) == 0) {
          //LAB_800fd918
          for(int s0 = s2._54.get() - 1; s0 > 0; s0--) {
            s5._44.deref().get(s0).set(s5._44.deref().get(s0 - 1));
          }

          //LAB_800fd950
          s5._44.deref().get(0).sub(s5._50);
          s2._84.deref().run(data, s2, s5);

          final VECTOR sp0x20 = new VECTOR();
          FUN_800fd1dc(data, s2, s5, sp0x20);

          final VECTOR sp0x30 = new VECTOR();
          if((s2._08._1c.get() & 0x1000_0000L) == 0 || (s5._90.get() & 0x8L) == 0) {
            //LAB_800fd9f4
            sp0x30.set(0, 0, 0);
          } else {
            sp0x30.set(sp0x20).negate().div(2);
          }

          //LAB_800fda00
          v0 = s5._90.get();
          v1 = v0 & 0xffff_fff7L;
          v0 = v0 >>> 3;
          v0 = ~v0;
          v0 = v0 & 0x1L;
          v0 = v0 << 3;
          v1 = v1 | v0;
          s5._90.set(v1);

          //LAB_800fda58
          //LAB_800fda90
          sp0x20.setX(MathHelper.clamp(sp0x20.getX() + sp0x30.getX(), 0, 0x8000));
          sp0x20.setY(MathHelper.clamp(sp0x20.getY() + sp0x30.getY(), 0, 0x8000));
          sp0x20.setZ(MathHelper.clamp(sp0x20.getZ() + sp0x30.getZ(), 0, 0x8000));

          //LAB_800fdac8
          if((s5._90.get() & 0x6L) != 0) {
            sp0x50.offset(4, 0x0L).setu(data._10.flags_00 & 0x67ff_ffffL | (s5._90.get() >>> 1 & 0x3L) << 28);
          } else {
            sp0x50.offset(4, 0x0L).setu(data._10.flags_00);
          }

          //LAB_800fdb14
          final SVECTOR sp0x98 = new SVECTOR().set(sp0x20).div(s2._54.get());
          final VECTOR sp0x40 = new VECTOR().set(s5._44.deref().get(0));
          final ShortRef refX1 = new ShortRef();
          final ShortRef refY1 = new ShortRef();
          s3 = FUN_800cfc20(s5._68, s5._2c, sp0x40, refX1, refY1) / 4;
          sp0x50.offset(2, 0x08L).setu(refX1.get());
          sp0x50.offset(2, 0x10L).setu(refY1.get());
          a0 = data._10.z_22;
          v1 = s3 + a0;
          if((int)v1 >= 0xa0L) {
            if((int)v1 >= 0xffeL) {
              s3 = 0xffeL - a0;
            }

            //LAB_800fdbc0
            sp0x50.offset(4, 0x4L).setu(s3);

            //LAB_800fdbe4
            for(int s0 = 0; s0 < s2._54.get() - 0x1L; s0++) {
              sp0x50.offset(1, 0x40L).setu(sp0x20.getX() >> 8);
              sp0x50.offset(1, 0x41L).setu(sp0x20.getY() >> 8);
              sp0x50.offset(1, 0x42L).setu(sp0x20.getZ() >> 8);
              sp0x20.sub(sp0x98);
              sp0x50.offset(1, 0x44L).setu(sp0x20.getX() >> 8);
              sp0x50.offset(1, 0x45L).setu(sp0x20.getY() >> 8);
              sp0x50.offset(1, 0x46L).setu(sp0x20.getZ() >> 8);
              sp0x40.set(s5._44.deref().get(s0));

              final ShortRef refX2 = new ShortRef();
              final ShortRef refY2 = new ShortRef();
              FUN_800cfc20(s5._68, s5._2c, sp0x40, refX2, refY2);
              sp0x50.offset(2, 0x0cL).setu(refX2.get());
              sp0x50.offset(2, 0x14L).setu(refY2.get());
              _801197c0.offset((s2._60.get() - 2) * 0x4L).deref(4).call(data, sp0x50.getAddress());
              sp0x50.offset(2, 0x08L).setu(sp0x50.offset(2, 0x0cL).get());
              sp0x50.offset(2, 0x10L).setu(sp0x50.offset(2, 0x14L).get());
            }

            //LAB_800fdcc8
          }
        }

        //LAB_800fdd28
      }

      sp0x50tmp.release();
    }

    //LAB_800fdd44
    if(s2._6c.get() != 0) {
      s2.vec_70.mul(s2._80.get()).div(0x100);
    }

    //LAB_800fdda0
  }

  @Method(0x800fddd0L)
  public static void FUN_800fddd0(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    // no-op
  }

  @Method(0x800fddd8L)
  public static void FUN_800fddd8(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final EffectData98 s2 = (EffectData98)data.effect_44;
    s2._52.incr();

    //LAB_800fde38
    for(int i = 0; i < s2.count_50.get(); i++) {
      final EffectData98Sub94 s4 = s2._68.deref().get(i);

      if(FUN_800fd460(state, data, s2, s4) == 0) {
        s2._84.deref().run(data, s2, s4);

        final VECTOR sp0x20 = new VECTOR();
        FUN_800fd1dc(data, s2, s4, sp0x20);

        final VECTOR sp0x50 = new VECTOR().set(s4._50);
        final ShortRef sp0x60 = new ShortRef();
        final ShortRef sp0x64 = new ShortRef();

        int s1 = FUN_800cfc20(s4._68, s4._2c, sp0x50, sp0x60, sp0x64) >> 2;
        final int v1 = s1 + data._10.z_22;
        if(v1 >= 0xa0) {
          if(v1 >= 0xffe) {
            s1 = 0xffe - data._10.z_22;
          }

          //LAB_800fdf44
          //TODO 1x1?
          GPU.queueCommand(s1 + data._10.z_22 >> 2, new GpuCommandQuad()
            .rgb(sp0x20.getX() >> 8, sp0x20.getY() >> 8, sp0x20.getZ() >> 8)
            .pos(sp0x60.get(), sp0x64.get(), 1, 1)
          );
        }
      }

      //LAB_800fdfcc
    }

    //LAB_800fdfe8
    if(s2._6c.get() != 0) {
      s2.vec_70.setX(s2.vec_70.getX() * s2._80.get() >> 8);
      s2.vec_70.setY(s2.vec_70.getY() * s2._80.get() >> 8);
      s2.vec_70.setZ(s2.vec_70.getZ() * s2._80.get() >> 8);
    }

    //LAB_800fe044
  }

  @Method(0x800fe120L)
  public static void FUN_800fe120(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final VECTOR sp0x38 = new VECTOR();
    final SVECTOR sp0x48 = new SVECTOR();
    final EffectData98 s2 = (EffectData98)data.effect_44;

    final Memory.TemporaryReservation sp0x28tmp = MEMORY.temp(0x8a);
    final VECTOR sp0x28 = sp0x28tmp.get().cast(VECTOR::new);

    s2._52.incr();

    //LAB_800fe180
    for(int i = 0; i < s2.count_50.get(); i++) {
      final EffectData98Sub94 sp54 = s2._68.deref().get(i);

      if(FUN_800fd460(state, data, s2, sp54) == 0) {
        long a2 = sp54._80.get() + (s2._54.get() - 1) * 0x10L;

        //LAB_800fe1bc
        for(int s4 = 0; s4 < s2._54.get() - 1; s4++) {
          MEMORY.ref(4, a2).offset(0x00L).setu(MEMORY.ref(4, a2).offset(-0x10L).get());
          MEMORY.ref(4, a2).offset(0x04L).setu(MEMORY.ref(4, a2).offset(-0x0cL).get());
          MEMORY.ref(4, a2).offset(0x08L).setu(MEMORY.ref(4, a2).offset(-0x08L).get());
          MEMORY.ref(4, a2).offset(0x0cL).setu(MEMORY.ref(4, a2).offset(-0x04L).get());
          a2 = a2 - 0x10L;
        }

        //LAB_800fe1fc
        s2._84.deref().run(data, s2, sp54);

        FUN_800fd1dc(data, s2, sp54, sp0x28);

        if((s2._08._1c.get() & 0x1000_0000L) == 0 || (sp54._90.get() & 0x8L) == 0) {
          //LAB_800fe280
          sp0x38.set(0, 0, 0);
        } else {
          sp0x38.set(sp0x28).negate().div(2);
        }

        //LAB_800fe28c
        sp54._90.set(sp54._90.get() & 0xffff_fff7L | (~(sp54._90.get() >>> 3) & 0x1L) << 3);

        if((data._10.flags_00 & 0x400_0000) == 0) {
          // This is super bugged in retail and passes garbage as the last 3 params to both methods.
          // Hopefully this is fine with them all zeroed. This is used for the Glare's bewitching attack.
          sp54._68.setY((short)(ratan2(
            FUN_800dc408(0, 0, 0, 0, 0) - sp54._50.getX(),
            FUN_800dc408(2, 0, 0, 0, 0) - sp54._50.getZ()
          ) + 0x400));
        }

        //LAB_800fe300
        final VECTOR sp0x18 = new VECTOR().set(sp54._50);

        sp0x28.setX(MathHelper.clamp(sp0x28.getX() + sp0x38.getX(), 0, 0x8000));
        sp0x28.setY(MathHelper.clamp(sp0x28.getY() + sp0x38.getY(), 0, 0x8000));
        sp0x28.setZ(MathHelper.clamp(sp0x28.getZ() + sp0x38.getZ(), 0, 0x8000));

        final GpuCommandPoly cmd1 = new GpuCommandPoly(4)
          .clut((s2.clut_5c.get() & 0b111111) * 16, s2.clut_5c.get() >>> 6)
          .vramPos(s2.u_58.get() & 0x3c0, s2.v_5a.get() < 256 ? 0 : 256)
          .rgb(sp0x28.getX() >> 8, sp0x28.getY() >> 8, sp0x28.getZ() >> 8)
          .uv(0, (s2.u_58.get() & 0x3f) * 4,                    s2.v_5a.get())
          .uv(1, (s2.u_58.get() & 0x3f) * 4 + s2.w_5e.get() - 1, s2.v_5a.get())
          .uv(2, (s2.u_58.get() & 0x3f) * 4,                    s2.v_5a.get() + s2.h_5f.get() - 1)
          .uv(3, (s2.u_58.get() & 0x3f) * 4 + s2.w_5e.get() - 1, s2.v_5a.get() + s2.h_5f.get() - 1);

        if((data._10.flags_00 & 1) << 30 != 0) {
          cmd1.translucent(Translucency.of(data._10.flags_00 >>> 28 & 0b11));
        }

        final int s5 = FUN_800fca78(data, s2, sp54, sp0x18, cmd1) >> 2;
        int a0 = data._10.z_22;
        if(a0 + s5 >= 160) {
          if(a0 + s5 >= 4094) {
            a0 = 4094 - s5;
          }

          //LAB_800fe548
          GPU.queueCommand(s5 + a0, cmd1);
        }

        //LAB_800fe564
        if((s2._08._1c.get() & 0x6000_0000L) != 0) {
          long s1 = sp54._80.get();
          MEMORY.ref(2, s1).offset(0x0L).setu(cmd1.getX(0));
          MEMORY.ref(2, s1).offset(0x2L).setu(cmd1.getY(0));
          MEMORY.ref(2, s1).offset(0x4L).setu(cmd1.getX(1));
          MEMORY.ref(2, s1).offset(0x6L).setu(cmd1.getY(1));
          MEMORY.ref(2, s1).offset(0x8L).setu(cmd1.getX(2));
          MEMORY.ref(2, s1).offset(0xaL).setu(cmd1.getY(2));
          MEMORY.ref(2, s1).offset(0xcL).setu(cmd1.getX(3));
          MEMORY.ref(2, s1).offset(0xeL).setu(cmd1.getY(3));
          sp0x48.set(sp0x28).div(s2._54.get());

          final int count = Math.min(-sp54._04.get(), s2._54.get());

          //LAB_800fe61c
          //LAB_800fe628
          for(int s4 = 0; s4 < count; s4++) {
            a0 = data._10.z_22;
            if(a0 + s5 >= 160) {
              if(a0 + s5 >= 4094) {
                a0 = 4094 - s5;
              }

              final GpuCommandPoly cmd2 = new GpuCommandPoly(cmd1);

              //LAB_800fe644
              cmd2
                .rgb(sp0x28.getX() >> 8, sp0x28.getY() >> 8, sp0x28.getZ() >> 8)
                .pos(0, (int)MEMORY.ref(2, s1).offset(0x0L).get(), (int)MEMORY.ref(2, s1).offset(0x2L).get())
                .pos(1, (int)MEMORY.ref(2, s1).offset(0x4L).get(), (int)MEMORY.ref(2, s1).offset(0x6L).get())
                .pos(2, (int)MEMORY.ref(2, s1).offset(0x8L).get(), (int)MEMORY.ref(2, s1).offset(0xaL).get())
                .pos(3, (int)MEMORY.ref(2, s1).offset(0xcL).get(), (int)MEMORY.ref(2, s1).offset(0xeL).get());

              //LAB_800fe78c
              GPU.queueCommand(s5 + a0, cmd2);
            }

            sp0x28.sub(sp0x48);

            //LAB_800fe7a8
            s1 = s1 + 0x10L;
          }
        }
      }

      //LAB_800fe7b8
    }

    sp0x28tmp.release();

    //LAB_800fe7ec
    if(s2._6c.get() != 0) {
      s2.vec_70.setX(s2.vec_70.getX() * s2._80.get() >> 8);
      s2.vec_70.setY(s2.vec_70.getY() * s2._80.get() >> 8);
      s2.vec_70.setZ(s2.vec_70.getZ() * s2._80.get() >> 8);
    }

    //LAB_800fe848
  }

  @Method(0x800fe878L)
  @Nullable
  public static EffectData98 FUN_800fe878(final EffectData98 a0) {
    if(_8011a00c.getPointer() == a0.getAddress()) {
      return null;
    }

    //LAB_800fe894
    EffectData98 v0 = _8011a00c.deref();
    do {
      if(v0._94.getPointer() == a0.getAddress()) {
        break;
      }

      v0 = v0._94.derefNullable();
    } while(v0 != null);

    //LAB_800fe8b0
    return v0;
  }

  @Method(0x800fe8b8L)
  public static void FUN_800fe8b8(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final EffectData98 s2 = (EffectData98)data.effect_44;
    final EffectData98 a0 = FUN_800fe878(s2);

    if(a0 == null) {
      _8011a00c.setNullable(s2._94.derefNullable());
    } else {
      //LAB_800fe8f0
      a0._94.setNullable(s2._94.derefNullable());
    }

    //LAB_800fe8fc
    if(s2._94.isNull()) {
      _8011a010.setNullable(a0);
    }

    //LAB_800fe910
    if(s2._54.get() != 0) {
      switch(s2._60.get()) {
        case 2, 4, 5 -> {
          //LAB_800fe960
          for(int i = 0; i < s2.count_50.get(); i++) {
            free(s2._68.deref().get(i)._44.getPointer());
          }
        }

        case 0 -> {
          if((s2._08._1c.get() & 0x6000_0000L) != 0) {
            //LAB_800fe9b4
            for(int i = 0; i < s2.count_50.get(); i++) {
              free(s2._68.deref().get(i)._80.get());
            }
          }
        }

        case 1 -> {
          if((s2._08._1c.get() & 0x6000_0000L) != 0) {
            //LAB_800fea08
            for(int i = 0; i < s2.count_50.get(); i++) {
              free(s2._68.deref().get(i)._44.getPointer());
            }
          }
        }
      }
    }

    //LAB_800fea30
    //LAB_800fea3c
    free(s2._68.getPointer());
    FUN_80102534();
  }

  @Method(0x800fea68L)
  public static void FUN_800fea68(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    // no-op
  }

  @Method(0x800fea70L)
  public static long FUN_800fea70(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    seed_800fa754.advance();
    final long theta = seed_800fa754.get() % 4097;
    a2._58.setX((short)(rcos(theta) >> 8));
    a2._58.setZ((short)(rsin(theta) >> 8));

    seed_800fa754.advance();
    a2._58.setY((short)-(seed_800fa754.get() % 91 + 10));

    seed_800fa754.advance();
    final long a0_0 = seed_800fa754.get() % 101 - 50 << 8;
    a2._84.add((short)a0_0);
    a2._86.add((short)a0_0);
    a2._88.add((short)a0_0);

    return theta;
  }

  @Method(0x800fec3cL)
  public static void FUN_800fec3c(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    final long s0 = (short)FUN_800fea70(a0, a1, a2, a3);
    seed_800fa754.advance();
    a2._58.setX((short)(rcos(s0) >> 6));
    a2._58.setY((short)0);
    a2._58.setZ((short)(rsin(s0) >> 6));
  }

  @Method(0x800fecccL)
  public static void FUN_800feccc(final EffectManagerData6c u0, final EffectData98 u1, final EffectData98Sub94 s2, final EffectData98Inner24 a3) {
    seed_800fa754.advance();
    final long s0 = seed_800fa754.get() % 4097;
    s2._58.setX((short)(rcos(s0) >> 10));
    seed_800fa754.advance();
    s2._58.setY((short)-(seed_800fa754.get() % 33 + 13));
    s2._58.setZ((short)(rsin(s0) >> 10));
    seed_800fa754.advance();
    s2._14.set((short)(seed_800fa754.get() % 3 + 1));
    seed_800fa754.advance();
    s2._16.set((short)(seed_800fa754.get() % 3));
    s2._04.set((short)(s2._04.get() / 4 * 4 + 1));
  }

  @Method(0x800fee9cL)
  public static void FUN_800fee9c(final EffectManagerData6c u0, final EffectData98 u1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    seed_800fa754.advance();
    final long theta = seed_800fa754.get() % 4097;
    a2._58.setX((short)(rcos(theta) / 0x80));
    a2._58.setY((short)0);
    a2._58.setZ((short)(rsin(theta) / 0x80));
    a2._04.set((short)1);
    seed_800fa754.advance();
    a2._14.set((short)(seed_800fa754.get() % 6));
  }

  @Method(0x800fefe4L)
  public static void FUN_800fefe4(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    FUN_800fee9c(a0, a1, a2, a3);
    a2._14.set((short)(seed_800fa754.advance().get() % 4097));
    a2._16.set(a3._10.get());
    a2._18.set((short)(seed_800fa754.advance().get() % 91 + 10));
    a2._1a.setX((short)120);
    a2._58.setX((short)0);
    a2._58.setY((short)-(seed_800fa754.advance().get() % 11 + 5));
    a2._58.setZ((short)0);
  }

  @Method(0x800ff15cL)
  public static void FUN_800ff15c(final EffectManagerData6c u0, final EffectData98 u1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    seed_800fa754.advance();
    a2._58.setY((short)-(seed_800fa754.get() % 61 + 60));

    final long v1;
    if(vsyncMode_8007a3b8.get() != 4) {
      seed_800fa754.advance();
      v1 = -(seed_800fa754.get() % 14 + 5);
    } else {
      //LAB_800ff248
      seed_800fa754.advance();
      v1 = -(seed_800fa754.get() % 21 + 10);
    }

    a2._0a.set((short)v1);
    a2._0c.set((short)v1);

    //LAB_800ff2b4
    seed_800fa754.advance();
    a2._14.set((short)(seed_800fa754.get() % 4097));
    seed_800fa754.advance();
    a2._1a.setX((short)(seed_800fa754.get() % 4097));
    seed_800fa754.advance();
    a2._1a.setZ((short)(seed_800fa754.get() % 1025 - 512));

    a2._1a.setY((short)100);
    a2._16.set(a3._10.get());
    a2._18.set((short)(a3._18.get() * 100 / 256));
  }

  @Method(0x800ff3e0L)
  public static void FUN_800ff3e0(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    FUN_800ff15c(a0, a1, a2, a3);
    a2._20.set((short)0);
    a2._22.set((short)(0x8000 / a2._12.get()));
    a2._24.set((short)(MEMORY.ref(1, a3.getAddress()).offset(0x1dL).get() << 8)); //TODO
  }

  @Method(0x800ff430L)
  public static void FUN_800ff430(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    a2._16.set(a3._10.get());
    a2._18.set((short)0);
    a2._1a.setX((short)0);
    a2._1a.setY((short)50);
    a2._20.set((short)100);
    a2._22.set((short)0);
    a2._24.set((short)(a2._10.get() / a2._12.get()));

    a2._58.setY((short)(seed_800fa754.advance().get() % 31 + 10));
    a2._1a.setZ((short)(seed_800fa754.advance().get() % 41 + 40));
    a2._14.set((short)(seed_800fa754.advance().get() % 4097));
  }

  @Method(0x800ff590L)
  public static void FUN_800ff590(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    FUN_800ff430(a0, a1, a2, a3);
    a2._22.set((short)20);
    a2._20.set((short)10);
  }

  /**
   * {@link SEffe#FUN_800ff5c4} uses t2 which isn't set... assuming the value even matters, this is to pass in t2 from the previous method
   */
  private static Long brokenT2For800ff5c4;

  @Method(0x800ff5c4L)
  public static void FUN_800ff5c4(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    if(brokenT2For800ff5c4 == null) {
      throw new RuntimeException("t2 was not set");
    }

    final long t2 = brokenT2For800ff5c4;
    brokenT2For800ff5c4 = null;

    seed_800fa754.advance();
    a2._58.setY((short)(-(seed_800fa754.get() % 61 + 60) * a3._18.get() / 256));
    seed_800fa754.advance();
    a2._14.set((short)(seed_800fa754.get() % 4097));
    a2._16.set(a3._10.get());
    a2._18.set((short)100);
    a2._1a.setX((short)(t2 * 4));
    a2._60.setY((short)(-a2._58.getY() / a2._12.get()));
  }

  @Method(0x800ff6d4L)
  public static void FUN_800ff6d4(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    FUN_800ff5c4(a0, a1, a2, a3);
    a2._18.set((short)0);
  }

  @Method(0x800ff6fcL)
  public static void FUN_800ff6fc(final EffectManagerData6c u0, final EffectData98 u1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    final long a0 = _8011a008.get();
    a2._10.set((short)-0x80);
    final long v1 = a0 >>> 1;
    a2._0e.set((short)(v1 * 128));
    a2._14.set((short)(v1 * 128));
    a2._16.set(a3._10.get());
    a2._18.set((short)(a0 & 0x1));
    a2._1a.setX((short)(v1 * 8));

    final long a1 = Math.max(0, MEMORY.ref(1, a3.getAddress()).offset(0x1dL).get() - a0 * 16); //TODO

    //LAB_800ff754
    a2._84.set((short)(a1 << 8));
    a2._86.set((short)a1);
    a2._12.set((short)-1);
    a2._88.set((short)a1);
    a2._86.set((short)(a1 << 8));
    a2._88.set((short)(a1 << 8));
  }

  @Method(0x800ff788L)
  public static void FUN_800ff788(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    a2._12.set((short)-1);
    a2._14.set((short)(seed_800fa754.advance().get() % 4097));
    a2._16.set(a3._10.get());
    a2._18.set((short)(seed_800fa754.advance().get() % 4097));
    a2._1a.setX((short)(seed_800fa754.advance().get() % 4097));
  }

  @Method(0x800ff890L)
  public static void FUN_800ff890(final EffectManagerData6c u0, final EffectData98 u1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    seed_800fa754.advance();
    seed_800fa754.advance();
    final long s1 = seed_800fa754.get() % 4097;

    seed_800fa754.advance();
    final long s0 = seed_800fa754.get() % 2049;

    a2._58.setX((short)(rcos(s1) * rsin(s0) / 0x40000));
    a2._58.setY((short)(rcos(s0) / 0x40));
    a2._58.setZ((short)(rsin(s1) * rsin(s0) / 0x40000));

    seed_800fa754.advance();
    a2._12.add((short)(seed_800fa754.get() % 21 - 10));

    if(a2._12.get() <= 0) {
      a2._12.set((short)1);
    }

    //LAB_800ffa60
  }

  @Method(0x800ffa80L)
  public static void FUN_800ffa80(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    FUN_800ff5c4(a0, a1, a2, a3);
    final short s2 = a3._10.get();
    a2._16.set(s2);
    a2._1a.setY(s2);
    a2._18.set((short)(a3._18.get() * 0xe0 >> 8));
  }

  @Method(0x800ffadcL)
  public static void FUN_800ffadc(final EffectManagerData6c u0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    a2._14.set((short)(seed_800fa754.advance().get() % 4097));
    final int v0 = -a3._10.get() >> 5;
    final int a0 = a3._18.get();
    a2._84.set((short)0);
    a2._86.set((short)0);
    a2._88.set((short)0);
    a2._16.set(a3._10.get());
    a2._18.set((short)(v0 * a0 >> 8));
    a2._1a.setX((short)(a0 >>> 1));
  }

  @Method(0x800ffb80L)
  public static void FUN_800ffb80(final EffectManagerData6c u0, final EffectData98 u1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    final long v0;
    long a0;
    a2._60.setY((short)8);
    a0 = _8011a008.get() & 0xffffL;
    v0 = a0 << 9;
    a2._14.set((short)v0);
    a0 = a0 >>> 2;
    a0 = a0 | 0x1L;
    a2._16.set(a3._10.get());
    a2._04.set((short)a0);
    a2._18.set((short)(a3._18.get() >>> 2));
    a2._58.setY((short)(a3._14.get() * -0x40 / 0x100));
  }

  @Method(0x800ffbd8L)
  public static void FUN_800ffbd8(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    final long t6 = a3._18.get();
    a2._14.set((short)0);
    a2._18.set((short)(seed_800fa754.advance().get() % 21 - 10));
    a2._1a.setX((short)(seed_800fa754.advance().get() % 21 - 10));
    a2._1a.setY((short)(seed_800fa754.advance().get() % 81 - 40));
    a2._1a.setZ(a2._12.get());
    a2._58.setX((short)((seed_800fa754.advance().get() % 41 + 44) * t6 >> 8));
    a2._58.setY((short)((seed_800fa754.advance().get() % 81 - 40) * t6 >> 8));
    a2._58.setZ((short)((seed_800fa754.advance().get() % 41 + 44) * t6 >> 8));
    a2._12.add((short)20);
  }

  @Method(0x800ffe80L)
  public static void FUN_800ffe80(final EffectManagerData6c u0, final EffectData98 u1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    a2._16.set(a3._10.get());

    final long a0 = a3._18.get();
    a2._60.setY((short)(a0 >>> 7));
    a2._18.set((short)(a0 >>> 3));

    seed_800fa754.advance();
    a2._14.set((short)(seed_800fa754.get() % 4097));
  }

  @Method(0x800ffefcL)
  public static void FUN_800ffefc(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    // no-op
  }

  @Method(0x800fff04L)
  public static void FUN_800fff04(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    a2._14.set((short)0);
    a2._18.set((short)0);
    a2._1a.setZ((short)0);
    final short v0 = (short)((a3._18.get() & 0xffff) >>> 2);
    a2._16.set(v0);
    a2._1a.setX(v0);
    a2._1a.setY(a3._10.get());
  }

  @Method(0x800fff30L)
  public static void FUN_800fff30(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    a2._58.setX((short)(seed_800fa754.advance().get() % 769 + 256));
  }

  @Method(0x800fffa0L)
  public static void FUN_800fffa0(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    a2._1a.setX(a3._10.get());
    a2._14.set((short)(seed_800fa754.advance().get() % 4097));
    a2._16.set((short)((seed_800fa754.advance().get() % 123 + 64) * a3._18.get() >> 8));
    a2._18.set((short)(0x800 / a1.count_50.get() * (_8011a008.get() & 0xffff)));
  }

  @Method(0x801000b8L)
  public static void FUN_801000b8(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    a2._58.setX((short)(-a2._50.getX() / 32));
    a2._58.setZ((short)(-a2._50.getZ() / 32));
  }

  @Method(0x801000f8L)
  public static void FUN_801000f8(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    FUN_800ff890(a0, a1, a2, a3);
    a2._58.setY((short)-Math.abs(a2._58.getY()));
    a2._14.set((short)(a3._18.get() * 0x300 / 0x100));
  }

  @Method(0x80100150L)
  public static void FUN_80100150(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    seed_800fa754.advance();
    a2._1a.setX((short)(seed_800fa754.get() % 4097));
    a2._14.set(a2._50.getX());
    a2._16.set(a2._50.getY());
    a2._18.set(a2._50.getZ());
    a2._1a.setZ((short)(a3._10.get() >>> 2));
    a2._58.setY((short)(a3._18.get() * -64 >> 8));
    seed_800fa754.advance();
    a2._1a.setY((short)((int)((seed_800fa754.get() % 513 - 256) * a3._18.get()) >> 8));
  }

  @Method(0x8010025cL)
  public static void FUN_8010025c(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    a2._58.setY((short)64);
    final long theta = seed_800fa754.advance().get() % 4097;
    if(a3._20.get() == 0x2a) {
      final int s0 = (a3._10.get() & 0xffff) >>> 5;
      a2._58.setX((short)(rcos(theta) * s0 >> 12));
      a2._58.setY((short)(rsin(theta) * s0 >> 12));
    } else {
      //LAB_80100328
      a2._58.setX((short)(rcos(theta) >>> 7));
      a2._58.setY((short)(rsin(theta) >>> 7));
    }
  }

  @Method(0x801003e8L)
  public static void FUN_801003e8(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    final long s4 = a3._10.get(); //TODO read with lw here but as a short everywhere else? Is this a bug?
    FUN_800ff890(a0, a1, a2, a3);

    final long s1 = seed_800fa754.advance().get() % 4097;
    final long s0 = seed_800fa754.advance().get() % 2049;
    a2._12.set((short)(a3._1c.get() >>> 16 & 0xff));
    a2._50.setX((short)((rcos(s1) * rsin(s0) >> 12) * s4 >> 12));
    a2._50.setY((short)(rcos(s0) * s4 >> 12));
    a2._50.setZ((short)((rsin(s1) * rsin(s0) >> 12) * s4 >> 12));
    a2._58.setX((short)(rcos(s1) * rsin(s0) >> 18));
    a2._58.setY((short)(rcos(s0) >> 6));
    a2._58.setZ((short)(rsin(s1) * rsin(s0) >> 18));
  }

  @Method(0x80100364L)
  public static void FUN_80100364(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    FUN_800ff890(a0, a1, a2, a3);
    a2._14.set((short)0);
    a2._58.setY((short)-Math.abs(a2._58.getY()));
    a2._60.set(a2._58).negate().div(a2._12.get());
  }

  @Method(0x801005b8L)
  public static void FUN_801005b8(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    final int s2 = a3._10.get();
    final int s4 = a3._18.get();
    FUN_800ff890(a0, a1, a2, a3);
    final int s0 = (int)(seed_800fa754.advance().get() % 4097);
    a2._50.setY((short)(rsin(s0) * s2 >> 12));
    a2._50.setZ((short)(rcos(s0) * s2 >> 12));
    a2._58.setX((short)((seed_800fa754.advance().get() % 65 + 54) * s4 >> 8));
    a2._60.setX((short)(-a2._58.getX() / a2._12.get()));
    a2._60.setY((short)0x10);
    final int a1_0 = -((rsin(s0) * s2 >> 12) + s2) / 2;
    a2._58.setY((short)((seed_800fa754.advance().get() % (-a1_0 + 1) + a1_0) * s4 >> 8));
  }

  @Method(0x801007b4L)
  public static void FUN_801007b4(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    a2._58.set(a2._50).negate().div(a2._12.get());
  }

  @Method(0x80100800L)
  public static void FUN_80100800(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    a2._16.set((short)(a3._18.get() >>> 2));
    a2._18.set(a3._10.get());
    a2._14.set((short)(seed_800fa754.advance().get() % 4097));
  }

  @Method(0x80100878L)
  public static void FUN_80100878(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    a2._58.setY((short)-0x40);
    a2._18.set(a3._10.get());
    a2._16.set((short)(a3._18.get() * 0x20));
    a2._14.set((short)(seed_800fa754.advance().get() % 4097));
  }

  @Method(0x801008f8L)
  public static void FUN_801008f8(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    a2._0e.set((short)0);
    a2._10.set((short)0);
    a2._70.set((short)0, (short)0, (short)0);
    a2._78.set((short)0, (short)0, (short)0);
    a2._18.set((short)a3._18.get());
    a2._14.set((short)0x800);
    a2._16.set((short)(a3._10.get() << 4));
    a2._1a.setX((short)(a3._18.get() >>> 2));
    a2._04.set((short)((_8011a008.get() & 0xffff) * (a3._14.get() / a3._0c.get())));
  }

  @Method(0x80100978L)
  public static void FUN_80100978(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    a2._14.set((short)(seed_800fa754.advance().get() % 4097));
    a2._16.set((short)(seed_800fa754.advance().get() % ((a3._10.get() & 0xffff) + 1)));
    a2._18.set((short)(seed_800fa754.advance().get() % 4097));
    a2._1a.setX((short)((seed_800fa754.advance().get() % 41 + 150) * a3._18.get() >> 8));
    a2._58.setY((short)-0x40);
  }

  @Method(0x80100af4L)
  public static void FUN_80100af4(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    FUN_800ff890(a0, a1, a2, a3);
    a2._58.setX((short)(a2._58.getX() * a3._18.get() >> 8));
    a2._58.setY((short)(a2._58.getY() * a3._18.get() >> 8));
    a2._58.setZ((short)(a2._58.getZ() * a3._18.get() >> 8));
    a2._60.set(a2._58).negate().div(a2._12.get());
  }

  @Method(0x80100bb4L)
  public static void FUN_80100bb4(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    seed_800fa754.advance();
    a2._58.setY((short)(seed_800fa754.get() % 33 + 16));
  }

  @Method(0x80100c18L)
  public static void FUN_80100c18(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    final int v1 = a3._18.get();
    a2._14.set((short)((-a2._50.getX() >> 1) * v1 >> 8));
    a2._16.set((short)((-a2._50.getY() >> 1) * v1 >> 8));
    a2._18.set((short)((-a2._50.getZ() >> 1) * v1 >> 8));
    a2._1a.set((short)0, (short)0, (short)0);
  }

  @Method(0x80100cacL)
  public static void FUN_80100cac(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    FUN_800ff890(a0, a1, a2, a3);
    a2._58.setY((short)-Math.abs(a2._58.getY()));
    a2._58.setX((short)0);
    a2._14.set((short)0);
  }

  @Method(0x80100cecL)
  public static void FUN_80100cec(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    a2._14.set((short)0);
    a2._60.setY((short)(a3._18.get() >>> 7));
  }

  @Method(0x80100d00L)
  public static void FUN_80100d00(final EffectManagerData6c a0, final EffectData98 a1, final EffectData98Sub94 a2, final EffectData98Inner24 a3) {
    final VECTOR sp0x10 = new VECTOR();
    FUN_800cffd8(a3.scriptIndex_04.get(), sp0x10, (int)_8011a008.get());
    a2._50.set(sp0x10);
  }

  @Method(0x80100d58L)
  public static void FUN_80100d58(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c a1, final EffectData98 a2, final EffectData98Sub94 a3) {
    // no-op
  }

  @Method(0x80100d60L)
  public static void FUN_80100d60(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c a1, final EffectData98 a2, final EffectData98Sub94 a3) {
    if(a3._04.get() == 0 && a2.scriptIndex_04.get() != -1) {
      final VECTOR sp0x20 = new VECTOR();
      scriptGetScriptedObjectPos(state.index, sp0x20);

      final VECTOR sp0x30 = new VECTOR();
      scriptGetScriptedObjectPos(a2.scriptIndex_04.get(), sp0x30);

      final VECTOR sp0x10 = new VECTOR().set(sp0x20).sub(sp0x30);
      a3._50.set(sp0x10);
      a3._50.set(sp0x10);
    }

    //LAB_80100e14
  }

  @Method(0x80100e28L)
  public static void FUN_80100e28(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c a1, final EffectData98 a2, final EffectData98Sub94 a3) {
    if(a3._04.get() == 0) {
      a3._8a.set((short)0);
      a3._8c.set((short)0);
      a3._8e.set((short)0);
    }

    //LAB_80100e44
  }

  @Method(0x80100e4cL)
  public static void FUN_80100e4c(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c a1, final EffectData98 a2, final EffectData98Sub94 a3) {
    if(a3._04.get() == 0) {
      a3._8a.set((short)(-0x8000 / a3._12.get()));
      a3._8c.set((short)(-0x8000 / a3._12.get()));
      a3._8e.set((short)(-0x8000 / a3._12.get()));
    }

    //LAB_80100e98
  }

  @Method(0x80100ea0L)
  public static void FUN_80100ea0(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c a1, final EffectData98 a2, final EffectData98Sub94 a3) {
    final int s2 = a2._08._18.get();
    final int s1 = a2._08._10.get() & 0xffff;

    final VECTOR sp0x10 = new VECTOR();
    final VECTOR sp0x20 = new VECTOR();
    scriptGetScriptedObjectPos(a2.scriptIndex_00.get(), sp0x10);
    scriptGetScriptedObjectPos(a2.scriptIndex_04.get(), sp0x20);

    final VECTOR sp0x30 = new VECTOR().set(sp0x20).sub(sp0x10);

    final int mod = s1 * 2 + 1;
    a3._58.setX((short)((sp0x30.getX() >> 3) + (seed_800fa754.advance().get() % mod - s1 >>> 4)));
    a3._58.setY((short)((sp0x30.getY() >> 3) + (seed_800fa754.advance().get() % mod - s1 >>> 4)));
    a3._58.setZ((short)((sp0x30.getZ() >> 3) + (seed_800fa754.advance().get() % mod - s1 >>> 4)));
    a3._58.x.mul((short)s2).shra(8);
    a3._58.y.mul((short)s2).shra(8);
    a3._58.z.mul((short)s2).shra(8);

    if(a3._58.getX() == 0) {
      a3._58.setX((short)1);
    }

    //LAB_80101068
    a3._12.set((short)(sp0x30.getX() / a3._58.getX()));
  }

  @Method(0x801010a0L)
  public static void FUN_801010a0(final ScriptState<EffectData98> state, final EffectManagerData6c a1, final EffectData98 a2, final EffectData98Sub94 a3) {
    // Calculate the index of this array element
    _8011a008.setu((a3.getAddress() - a2._68.deref().get(0).getAddress()) / 0x94);

    FUN_80101308(a1, a2, a3, a2._08);

    if(a2._54.get() != 0) {
      final int v1 = a2._60.get();

      if(v1 == 0) {
        //LAB_801011a0
        final GpuCommandPoly cmd = new GpuCommandPoly(4);

        //LAB_801011d8
        for(int i = 0; i < a2._54.get(); i++) {
          final long s0 = a3._80.get() + i * 0x10;

          final VECTOR sp0x18 = new VECTOR().set(a3._50);
          FUN_800fca78(a1, a2, a3, sp0x18, cmd);
          MEMORY.ref(2, s0).offset(0x0L).setu(cmd.getX(0));
          MEMORY.ref(2, s0).offset(0x2L).setu(cmd.getY(0));
          MEMORY.ref(2, s0).offset(0x4L).setu(cmd.getX(1));
          MEMORY.ref(2, s0).offset(0x6L).setu(cmd.getY(1));
          MEMORY.ref(2, s0).offset(0x8L).setu(cmd.getX(2));
          MEMORY.ref(2, s0).offset(0xaL).setu(cmd.getY(2));
          MEMORY.ref(2, s0).offset(0xcL).setu(cmd.getX(3));
          MEMORY.ref(2, s0).offset(0xeL).setu(cmd.getY(3));
        }
        //LAB_8010114c
      } else if(v1 == 2 || v1 >= 4 && v1 < 6) {
        //LAB_80101160
        //LAB_80101170
        for(int i = 0; i < a2._54.get(); i++) {
          a3._44.deref().get(i).set(a3._50);
        }
      }
    }

    //LAB_8010127c
  }

  @Method(0x801012a0L)
  public static void FUN_801012a0(final ScriptState<EffectData98> state, final EffectManagerData6c a1, final EffectData98 a2, final EffectData98Sub94 a3) {
    if((a1._10._24 & 0x4) != 0) {
      FUN_801010a0(state, a1, a2, a3);
    }

    //LAB_801012c4
  }

  @Method(0x801012d4L)
  public static void FUN_801012d4(final ScriptState<EffectData98> state, final EffectManagerData6c a1, final EffectData98 a2, final EffectData98Sub94 a3) {
    if((a1._10._24 & 0x4) == 0) {
      FUN_801010a0(state, a1, a2, a3);
    }

    //LAB_801012f8
  }

  @Method(0x80101308L)
  public static void FUN_80101308(final EffectManagerData6c sp3c, final EffectData98 fp, final EffectData98Sub94 s3, final EffectData98Inner24 a4) {
    long v1;
    final long a1;
    final long t2;
    final long s0;
    final long s2;
    final long s4;
    final long s5;
    final long s7;

    if((a4._1c.get() & 0xffL) == 0) {
      a4._1c.and(0xffff_ff00L).or(_801197ec.offset(a4._20.get() * 0x4L).offset(1, 0x2L).get());
    }

    //LAB_8010137c
    if((a4._1c.get() & 0xff00L) == 0) {
      a4._1c.and(0xffff_00ffL).or(_801197ec.offset(a4._20.get() * 0x4L).offset(1, 0x1L).get() << 8);
    }

    //LAB_801013c0
    if((a4._1c.get() & 0xff_0000L) == 0) {
      a4._1c.and(0xff00_ffffL).or(_801197ec.offset(a4._20.get() * 0x4L).offset(1, 0x0L).get() << 16);
    }

    //LAB_80101400
    fp._61.set((int)_801197ec.offset(a4._20.get() * 0x4L).offset(1, 0x3L).get());
    s3._00.set(0x73);
    s3._01.set(0x6d);
    s3._02.set(0x6b);
    s3._50.set((short)0, (short)0, (short)0);
    s3._58.set((short)0, (short)0, (short)0);
    s3._06.set((short)0);
    s3._08.set((short)0);
    s3._0a.set((short)0);
    s3._0c.set((short)0);
    s3._60.set((short)0, (short)0, (short)0);
    s3._8a.set((short)0);
    s3._8c.set((short)0);
    s3._8e.set((short)0);
    t2 = a4._20.get() * 10;
    brokenT2For800ff5c4 = t2;
    s3._04.set((short)(seed_800fa754.advance().get() % (a4._14.get() + 1) + 1));
    s3._12.set((short)((a4._1c.get() & 0xff_0000L) >>> 16));
    final long a0 = (a4._1c.get() & 0xff00L) >>> 8;
    s3._84.set((short)a0);
    s3._86.set((short)a0);
    s3._88.set((short)a0);
    s3._90.or(0x1L);
    s3._0e.set((short)(seed_800fa754.advance().get() % 4097));
    s3._10.set((short)(seed_800fa754.advance().get() % 513 - 256));
    s3._70.setX((short)(seed_800fa754.advance().get() % 4097));
    s3._70.setY((short)(seed_800fa754.advance().get() % 4097));
    s3._70.setZ((short)(seed_800fa754.advance().get() % 4097));
    s3._78.setX((short)(seed_800fa754.advance().get() % 129 - 64));
    s3._78.setY((short)(seed_800fa754.advance().get() % 129 - 64));
    s3._78.setZ((short)0);
    s3._90.and(0xffff_fff1L).or(seed_800fa754.advance().get() % 101 < 50 ? 0 : 0x8L);
    s3._84.shl(8);
    s3._88.shl(8);
    s3._86.shl(8);
    s5 = _801198f0.offset(t2).getAddress();
    v1 = MEMORY.ref(1, s5).get();
    s7 = a4._20.get();
    if(v1 == 1) {
      //LAB_80101840
      s2 = seed_800fa754.advance().get() % 4097;
      s0 = a4._10.get();
      s3._50.setX((short)(rcos(s2) * (int)s0 >> MEMORY.ref(2, s5).offset(0x2L).getSigned()));
      s3._50.setY((short)0);
      s3._50.setZ((short)(rsin(s2) * (int)s0 >> MEMORY.ref(2, s5).offset(0x2L).getSigned()));
      //LAB_80101824
    } else if(v1 == 2) {
      //LAB_801018c8
      s2 = seed_800fa754.advance().get() % 4097;
      s4 = seed_800fa754.advance().get() % (a4._10.get() + 1);
      s3._50.setX((short)(rcos(s2) * s4 >> MEMORY.ref(2, s5).offset(0x2L).getSigned()));
      s3._50.setY((short)0);
      s3._50.setZ((short)(rsin(s2) * s4 >> MEMORY.ref(2, s5).offset(0x2L).getSigned()));
    } else if(v1 == 3) {
      //LAB_80101990
      s3._50.setY((short)(seed_800fa754.advance().get() % (MEMORY.ref(2, s5).offset(0x4L).getSigned() - MEMORY.ref(2, s5).offset(0x2L).getSigned() + 1) + MEMORY.ref(2, s5).offset(0x2L).getSigned()));
    } else if(v1 == 4) {
      //LAB_801019e4
      s2 = seed_800fa754.advance().get() % 4097;
      s4 = seed_800fa754.advance().get() % 2049;
      s3._50.setX((short)((rcos(s2) * rsin(s4) >> MEMORY.ref(2, s5).offset(0x2L).getSigned()) * a4._10.get() >> MEMORY.ref(2, s5).offset(0x4L).getSigned()));
      s3._50.setY((short)(rcos(s4) * a4._10.get() >> MEMORY.ref(2, s5).offset(0x4L).getSigned()));
      s3._50.setZ((short)((rsin(s2) * rsin(s4) >> MEMORY.ref(2, s5).offset(0x2L).getSigned()) * a4._10.get() >> MEMORY.ref(2, s5).offset(0x4L).getSigned()));
    }

    //LAB_80101b10
    //LAB_80101b18
    fp._8c.deref().run(sp3c, fp, s3, a4);

    a1 = _801198f0.offset(s7 * 0xaL).getAddress();
    if(MEMORY.ref(1, a1).offset(0x6L).get() == 1) {
      s3._58.setX((short)(s3._58.getX() * a4._18.get() >> 8));
      s3._58.setY((short)(s3._58.getY() * a4._18.get() >> 8));
      s3._58.setZ((short)(s3._58.getZ() * a4._18.get() >> 8));
    }

    //LAB_80101ba4
    if(MEMORY.ref(1, a1).offset(0x7L).get() == 1) {
      v1 = (byte)(seed_800fa754.advance().get() % (MEMORY.ref(1, a1).offset(0x9L).getSigned() - MEMORY.ref(1, a1).offset(0x8L).getSigned() + 1) + MEMORY.ref(1, a1).offset(0x8L).getSigned());
      s3._0a.set((byte)v1);
      s3._0c.set((byte)v1);
    }

    //LAB_80101c20
    s3._48.set(s3._50);
  }

  @Method(0x80101c68L)
  public static void FUN_80101c68(final EffectData98 a0, final EffectData98Sub94 a1, final EffectData98Inner24 a2, final long a3) {
    a0._60.set(3);
  }

  @Method(0x80101c74L)
  public static void FUN_80101c74(final EffectData98 a0, final EffectData98Sub94 a1, final EffectData98Inner24 a2, final long a3) {
    a0._60.set((int)a3 >> 20);
    a0._54.set((short)a3);

    //LAB_80101cb0
    for(int s3 = 0; s3 < a0.count_50.get(); s3++) {
      final EffectData98Sub94 s2 = a0._68.deref().get(s3);
      s2._44.setPointer(mallocHead(a0._54.get() * 0x8L));

      //LAB_80101cdc
      for(int s1 = 0; s1 < a0._54.get(); s1++) {
        memcpy(s2._44.deref().get(s1).getAddress(), s2._50.getAddress(), 8);
      }

      //LAB_80101d04
    }

    //LAB_80101d1c
  }

  @Method(0x80101d3cL)
  public static void FUN_80101d3c(final EffectData98 a0, final EffectData98Sub94 a1, final EffectData98Inner24 a2, final long flags) {
    a0._54.set(0);
    a0._60.set(1);

    if((flags & 0xf_ff00) == 0xf_ff00) {
      //TODO I added the first deref here, this might have been a retail bug...
      //     Looking at how _800c6944 gets set, I don't see how it could have been correct
      a0.tmd_30.set(tmds_800c6944[(int)flags & 0xff]);
      a0.tpage_56.set(0x20);
    } else {
      //LAB_80101d98
      final DeffPart.TmdType tmdType = (DeffPart.TmdType)getDeffPart(0x300_0000 | (int)flags & 0xf_ffff);
      final ExtendedTmd extTmd = tmdType.tmd_0c.deref();
      final TmdWithId tmd = extTmd.tmdPtr_00.deref();
      a0.tmd_30.set(tmd.tmd.objTable.get(0));
      a0.tpage_56.set((int)((tmd.id.get() & 0xffff_0000L) >>> 11));
    }

    //LAB_80101dd8
    if((a0._08._1c.get() & 0x6000_0000L) != 0) {
      a0._54.set((int)_800fb794.offset((a0._08._1c.get() & 0x6000_0000L) >>> 27).get());

      //LAB_80101e3c
      for(int i = 0; i < a0.count_50.get(); i++) {
        a0._68.deref().get(i)._44.setPointer(mallocHead(a0._54.get() * 8));
      }
    }

    //LAB_80101e6c
  }

  @Method(0x80101e84L)
  public static void FUN_80101e84(final EffectData98 a0, final EffectData98Sub94 a1, final EffectData98Inner24 a2, final long flags) {
    a0._60.set(0);
    a0._54.set(0);

    if((a0._08._1c.get() & 0x6000_0000L) != 0) {
      final long v0 = (a0._08._1c.get() & 0x6000_0000L) >>> 27;
      a0._54.set((int)_800fb794.offset(2, v0).get());

      //LAB_80101f2c
      for(int i = 0; i < a0.count_50.get(); i++) {
        final EffectData98Sub94 s2 = a0._68.deref().get(i);
        //TODO why is a GP0 packet started here but not used?
//        final long v1 = gpuPacketAddr_1f8003d8.get();
//        gpuPacketAddr_1f8003d8.addu(0x28L);
//        MEMORY.ref(1, v1).offset(0x3L).setu(0x9L);
//        MEMORY.ref(4, v1).offset(0x4L).setu(0x2c80_8080L);
        s2._80.set(mallocHead(a0._54.get() * 0x10L));
      }
    }

    //LAB_80101f70
    if((flags & 0xf_ff00) == 0xf_ff00) {
      final SpriteMetrics08 metrics = spriteMetrics_800c6948[(int)(flags & 0xff)];
      a0.u_58.set(metrics.u_00.get());
      a0.v_5a.set(metrics.v_02.get());
      a0.w_5e.set(metrics.w_04.get());
      a0.h_5f.set(metrics.h_05.get());
      a0.clut_5c.set(metrics.clut_06.get());
    } else {
      //LAB_80101fec
      final DeffPart.SpriteType spriteType = (DeffPart.SpriteType)getDeffPart((int)flags | 0x400_0000);
      final DeffPart.SpriteMetrics deffMetrics = spriteType.metrics_08.deref();
      a0.u_58.set(deffMetrics.u_00.get());
      a0.v_5a.set(deffMetrics.v_02.get());
      a0.w_5e.set(deffMetrics.w_04.get() * 4);
      a0.h_5f.set(deffMetrics.h_06.get());
      a0.clut_5c.set(GetClut(deffMetrics.clutX_08.get(), deffMetrics.clutY_0a.get()));
    }

    //LAB_80102048
    a0._34.set(a0.w_5e.get() >>> 1);
    a0._36.set(a0.h_5f.get() >>> 1);
  }

  @Method(0x80102088L)
  public static FlowControl FUN_80102088(final RunningScript<? extends BattleScriptDataBase> script) {
    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0x98,
      null,
      _80119b7c[script.params_20[2].get() >> 20],
      SEffe::FUN_800fe8b8,
      EffectData98::new
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    final EffectData98 effect = (EffectData98)manager.effect_44;
    effect.count_50.set(script.params_20[3].get());
    effect.size_64.set(effect.count_50.get() * 0x94);
    effect._68.setPointer(mallocHead(effect.size_64.get()));

    if(_8011a00c.isNull()) {
      _8011a00c.set(effect);
    }

    //LAB_801021a8
    if(!_8011a010.isNull()) {
      _8011a010.deref()._94.set(effect);
    }

    _8011a010.set(effect);

    //LAB_801021c0
    effect.scriptIndex_00.set(state.index);
    effect.scriptIndex_04.set(script.params_20[1].get());
    effect._84.set(_80119bac.get(script.params_20[8].get()).deref());
    effect._88.set(_80119cb0.get(script.params_20[8].get()).deref());
    effect._52.set(0);
    effect._34.set(0);
    effect._36.set(0);
    effect._6c.set(0);
    effect._94.clear();
    effect._8c.set(_80119db4.get(script.params_20[8].get()).deref());
    script.params_20[0].set(state.index);

    //LAB_8010223c
    for(int i = 0; i < 9; i++) {
      //TODO this seems weird
      MEMORY.ref(4, effect._08.getAddress()).offset(i * 0x4L).setu(script.params_20[i].get());
    }

    //LAB_80102278
    for(int i = 0; i < effect.count_50.get(); i++) {
      final EffectData98Sub94 s2_0 = effect._68.deref().get(i);
      _8011a008.setu(i);
      FUN_80101308(manager, effect, s2_0, effect._08);
      s2_0._3c.set(s2_0._50);
    }

    final EffectData98Sub94 s2_0 = effect._68.deref().get(effect.count_50.get()); // This looks like a retail bug - index out of bounds

    //LAB_801022b4
    if(effect._61.get() != 0) {
      effect._90.set(MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_801012d4", ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class, EffectData98.class, EffectData98Sub94.class), QuadConsumerRef::new));
    } else {
      //LAB_801022cc
      effect._90.set(MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_801012a0", ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class, EffectData98.class, EffectData98Sub94.class), QuadConsumerRef::new));
    }

    //LAB_801022d4
    effect._54.set(0);
    _80119b94.get(script.params_20[2].get() >> 20).deref().run(effect, s2_0, effect._08, script.params_20[2].get());
    manager._10.flags_00 |= 0x5000_0000;
    manager.flags_04 |= 0x4_0000;
    FUN_80102534();
    return FlowControl.CONTINUE;
  }

  @Method(0x80102364L)
  public static FlowControl FUN_80102364(final RunningScript<?> script) {
    final EffectData98 a0_0 = (EffectData98)((EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00).effect_44;

    final int a2 = script.params_20[0].get();
    if(a2 == 0) {
      a0_0._6c.set(1);
      a0_0.vec_70.set(script.params_20[2].get(), script.params_20[3].get(), script.params_20[4].get());
      a0_0._80.set(script.params_20[5].get());
      //LAB_801023d0
    } else if(a2 == 1) {
      a0_0._6c.set(0);
      //LAB_801023e0
    } else if(a2 == 2) {
      //LAB_801023e8
      a0_0._80.set(script.params_20[5].get());
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
    final EffectData98 a2 = (EffectData98)((EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00).effect_44;

    //LAB_8010243c
    for(int i = 0; i < a2.count_50.get(); i++) {
      final EffectData98Sub94 a0 = a2._68.deref().get(i);
      script.params_20[1].array(i).set((int)(a0._90.get() & 1));
    }

    //LAB_80102464
    return FlowControl.CONTINUE;
  }

  @Method(0x8010246cL)
  public static FlowControl FUN_8010246c(final RunningScript<?> script) {
    final EffectData98 effect = (EffectData98)((EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00).effect_44;
    final EffectData98Sub94 a1 = effect._68.deref().get(script.params_20[1].get());

    final VECTOR sp0x20 = new VECTOR();
    FUN_800cf684(a1._68, a1._2c, new VECTOR().set(a1._50), sp0x20);
    script.params_20[2].set(sp0x20.getX());
    script.params_20[3].set(sp0x20.getY());
    script.params_20[4].set(sp0x20.getZ());
    return FlowControl.CONTINUE;
  }

  @Method(0x80102534L)
  public static void FUN_80102534() {
    EffectData98 s0 = _8011a00c.derefNullable();

    //LAB_80102550
    while(s0 != null) {
      final long s1 = mallocHead(s0.size_64.get());

      if(s1 != 0) {
        final long a1 = s0._68.getPointer();

        if(s1 < a1) {
          memcpy(s1, a1, (int)s0.size_64.get());
          free(s0._68.getPointer());
          s0._68.setPointer(s1);
        } else {
          //LAB_801025a4
          free(s1);
        }
      }

      //LAB_801025b0
      s0 = s0._94.derefNullable();
    }

    //LAB_801025c0
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
  public static void FUN_80102618(final EffectManagerData6c a0, final BttlScriptData6cSub38 a1, final BttlScriptData6cSub38Sub14 a2) {
    if(a1.scriptIndex_08.get() != -1) {
      final VECTOR sp0x10 = new VECTOR();
      final VECTOR sp0x20 = new VECTOR();
      scriptGetScriptedObjectPos(a1.scriptIndex_08.get(), sp0x10);
      sp0x10.sub(a0._10.trans_04).div(a1._28.get());

      //LAB_801026f0
      for(int i = 0; i < a1._28.get(); i++) {
        final BttlScriptData6cSub38Sub14Sub30 struct = a2.ptr_10.deref().get(i);
        struct._00.set(sp0x20);

        sp0x20.x.add((int)((seed_800fa754.advance().get() % 257 - 128) * a1._26.get() >>> 7));
        sp0x20.z.add((int)((seed_800fa754.advance().get() % 257 - 128) * a1._26.get() >>> 7));
        sp0x20.add(sp0x10);
      }
    }

    //LAB_80102848
  }

  @Method(0x80102860L)
  public static void FUN_80102860(final EffectManagerData6c data, final BttlScriptData6cSub38 s0) {
    int sp10;
    int sp12;
    int sp14;
    int sp16;
    int sp18;
    int sp1a;
    int sp20 = 0;
    int sp22 = 0;
    int sp24 = 0;
    int sp26 = 0;
    int sp28 = 0;
    int sp2a = 0;

    //LAB_801028a4
    for(int i = 0; i < s0.count_00.get(); i++) {
      final BttlScriptData6cSub38Sub14 s1 = s0._34.deref().get(i);

      sp16 = data._10.colour_1c.getX() << 8;
      sp18 = data._10.colour_1c.getY() << 8;
      sp1a = data._10.colour_1c.getZ() << 8;

      int a1 = data._10.colour_1c.getX();
      int a0 = data._10.colour_1c.getY();
      int v1 = data._10.colour_1c.getZ();

      if(a0 < a1) {
        a0 = a1;
      }

      //LAB_8010290c
      if(v1 < a0) {
        v1 = a0;
      }

      //LAB_8010292c
      sp10 = v1 << 8;
      sp12 = v1 << 8;
      sp14 = v1 << 8;

      if(s0._23.get() == 0) {
        sp20 = sp10 / s0._28.get();
        sp22 = sp12 / s0._28.get();
        sp24 = sp14 / s0._28.get();
        sp26 = sp16 / s0._28.get();
        sp28 = sp18 / s0._28.get();
        sp2a = sp1a / s0._28.get();
      }

      //LAB_801029f0
      //LAB_80102a04
      for(a1 = 0; a1 < s0._28.get(); a1++) {
        final BttlScriptData6cSub38Sub14Sub30 struct = s1.ptr_10.deref().get(a1);
        struct.colour_10.set((short)sp10, (short)sp12, (short)sp14);
        struct.colour_16.set((short)sp16, (short)sp18, (short)sp1a);

        if(s0._22.get() == 0 && s0._0c.get() != -1) {
          struct._1c.set(struct.colour_10).div(s0._0c.get());
          struct._22.set(struct.colour_16).div(s0._0c.get());
        } else {
          //LAB_80102b10
          struct._1c.set((short)0, (short)0, (short)0);
          struct._22.set((short)0, (short)0, (short)0);
        }

        //LAB_80102b28
        sp10 = sp10 - sp20;
        sp12 = sp12 - sp22;
        sp14 = sp14 - sp24;
        sp16 = sp16 - sp26;
        sp18 = sp18 - sp28;
        sp1a = sp1a - sp2a;
      }

      //LAB_80102bb8
      FUN_80102618(data, s0, s1);
    }

    //LAB_80102be0
  }

  @Method(0x80102bfcL)
  public static void FUN_80102bfc(final EffectManagerData6c a0, final BttlScriptData6cSub38 a1, final BttlScriptData6cSub38Sub14 a2) {
    short sp14 = 0;
    short sp10 = 0;

    final int t3 = -(short)(a1._1c.get() / a1._28.get());

    //LAB_80102c58
    for(int i = 0; i < a1._28.get(); i++) {
      final BttlScriptData6cSub38Sub14Sub30 a3 = a2.ptr_10.deref().get(i);
      a3._00.set(sp10, t3 * i, sp14);
      a3._28.set((int)(seed_800fa754.advance().get() % 7 + 5));
      a3._2a.set((short)0);
      a3._2c.set((short)(sp10 >> 4));
      a3._2e.set((short)(seed_800fa754.advance().get() % 193 + 64));

      if(a1._14.get() == 0) {
        sp10 += (seed_800fa754.advance().get() % 257 - 128) * a1._26.get() >>> 7;
        sp14 += (seed_800fa754.advance().get() % 257 - 128) * a1._26.get() >>> 7;
        //LAB_80102e58
      } else if(i < a1._28.get() - 2) {
        sp10 = (short)((seed_800fa754.advance().get() % 257 - 128) * a1._26.get() >>> 7);
        sp14 = (short)((seed_800fa754.advance().get() % 257 - 128) * a1._26.get() >>> 7);
      } else {
        //LAB_80102f44
        sp10 = 0;
        sp14 = 0;
      }

      //LAB_80102f4c
    }

    //LAB_80102f64
    FUN_80102618(a0, a1, a2);
  }

  /**
   * @param xy 4 vertices (note: data was originally passed in as ints so you need to change the calling code)
   */
  @Method(0x80102f7cL)
  public static void renderGradient(final SVECTOR colour1, final SVECTOR colour2, final DVECTOR[] xy, final int a3, final int a4, final Translucency translucency) {
    final GpuCommandPoly cmd = new GpuCommandPoly(4)
      .translucent(translucency)
      .pos(0, xy[0].getX(), xy[0].getY())
      .pos(1, xy[1].getX(), xy[1].getY())
      .pos(2, xy[2].getX(), xy[2].getY())
      .pos(3, xy[3].getX(), xy[3].getY())
      .monochrome(0, 0)
      .rgb(1, colour2.getX(), colour2.getY(), colour2.getZ())
      .monochrome(2, 0)
      .rgb(3, colour1.getX(), colour1.getY(), colour1.getZ());

    GPU.queueCommand(a3 + a4 >> 2, cmd);
  }

  @Method(0x801030d8L)
  public static void FUN_801030d8(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final BttlScriptData6cSub38 s6 = (BttlScriptData6cSub38)data.effect_44;

    if(s6._04.get() + 1 == s6._0c.get()) {
      return;
    }

    final ShortRef sp0x20 = new ShortRef();
    final ShortRef sp0x24 = new ShortRef();
    final ShortRef sp0x28 = new ShortRef();
    final ShortRef sp0x2c = new ShortRef();
    final VECTOR sp0x70 = new VECTOR();

    s6._04.incr();

    //LAB_80103140
    if(s6._04.get() == 1) {
      FUN_80102860(data, s6);

      //LAB_80103174
      for(int i = 0; i < s6.count_00.get(); i++) {
        s6.callback_2c.deref().run(data, s6, s6._34.deref().get(i), i);
      }

      return;
    }

    //LAB_801031cc
    s6._2a.incr().and(31);

    final int spc0;
    if((data._10._24 >> s6._2a.get() & 0x1) == 0) {
      spc0 = 0;
    } else {
      spc0 = 1;
    }

    final DVECTOR[] sp0x80 = new DVECTOR[4];
    Arrays.setAll(sp0x80, i -> new DVECTOR());

    //LAB_80103200
    //LAB_8010322c
    for(int i = 0; i < s6.count_00.get(); i++) {
      final BttlScriptData6cSub38Sub14 spbc = s6._34.deref().get(i);

      if(s6._24.get() == 0) {
        FUN_80102bfc(data, s6, spbc);
      }

      //LAB_8010324c
      s6.callback_2c.deref().run(data, s6, spbc, i);
      spbc._02.sub((short)(s6._10.get() << 7 >> 8));
      sp0x70.set(spbc.ptr_10.deref().get(0)._00);
      int sp18 = FUN_800cfb94(data, spbc._04, sp0x70, sp0x20, sp0x24) >> 2;
      sp0x70.set(spbc.ptr_10.deref().get(s6._28.get() - 1)._00);
      FUN_800cfb94(data, spbc._04, sp0x70, sp0x28, sp0x2c);
      final int sp40 = sp0x28.get() - sp0x20.get() << 8;
      final int sp44 = sp0x2c.get() - sp0x24.get() << 8;
      final int sp48 = sp40 / (s6._28.get() - 1);
      final int sp4c = sp44 / (s6._28.get() - 1);
      int sp60 = sp0x20.get() << 8;
      int sp50 = sp0x20.get() << 8;
      int sp64 = sp0x24.get() << 8;
      int sp54 = sp0x24.get() << 8;
      final short s0_0 = (short)(spbc.ptr_10.deref().get(0)._28.get() * data._10.scale_16.getX() >> 12);
      final int angle = -ratan2(sp40, sp44);
      final short s1_0 = (short)(rcos(angle) * s0_0 >> 12);
      final short v0_0 = (short)(rsin(angle) * s0_0 >> 12);
      sp0x28.set((short)(sp0x20.get() - s1_0));
      sp0x2c.set((short)(sp0x24.get() - v0_0));
      sp0x20.add(s1_0);
      sp0x24.add(v0_0);

      //LAB_80103488
      for(int fp = 0; fp < s6._28.get(); fp++) {
        final BttlScriptData6cSub38Sub14Sub30 s4 = spbc.ptr_10.deref().get(fp);
        s4.colour_10.sub(s4._1c);
        s4.colour_16.sub(s4._22);
        s4._2a.add((short)(s6._10.get() << 8 >> 8));
      }

      //LAB_80103538
      if(spc0 == 0) {
        final int v1 = data._10.z_22 + sp18;
        if(v1 >= 0xa0) {
          if(v1 >= 0xffe) {
            sp18 = 0xffe - data._10.z_22;
          }

          final Translucency translucency = Translucency.of(data._10.flags_00 >>> 28 & 3);

          //LAB_80103574
          //LAB_80103594
          for(int fp = 0; fp < s6._28.get() - 1; fp++) {
            final BttlScriptData6cSub38Sub14Sub30 s4 = spbc.ptr_10.deref().get(fp);
            final BttlScriptData6cSub38Sub14Sub30 t4 = spbc.ptr_10.deref().get(fp + 1);
            int sp58 = sp50 + sp48;
            int sp5c = sp54 + sp4c;
            final int sp68 = sp50 + sp48;
            final int sp6c = sp54 + sp4c;
            sp50 += rcos(angle) * s4._2c.get() >> 12 << 8;
            sp54 += rsin(angle) * s4._2c.get() >> 12 << 8;
            sp58 += rcos(angle) * t4._2c.get() >> 12 << 8;
            sp5c += rsin(angle) * t4._2c.get() >> 12 << 8;
            final int s0 = s4._28.get() * data._10.scale_16.getX() >> 12;
            final int sp30 = (sp58 >> 8) + (rcos(angle) * s0 >> 12);
            final int sp34 = (sp5c >> 8) + (rsin(angle) * s0 >> 12);
            final int sp38 = (sp58 >> 8) - (rcos(angle) * s0 >> 12);
            final int sp3c = (sp5c >> 8) - (rsin(angle) * s0 >> 12);

            if(s6._29.get() == 0) {
              final int spa0 = (sp60 - sp50 >> 8) * s4._2e.get() + sp50;
              final int spa4 = (sp64 - sp54 >> 8) * s4._2e.get() + sp54;
              final int spa8 = (sp58 - sp50 >> 8) * s4._2e.get() + sp50;
              final int spac = (sp5c - sp54 >> 8) * s4._2e.get() + sp54;

              //LAB_80103808
              int spb0 = s4.colour_10.getX() + Math.abs(s4._2c.get() - t4._2c.get()) * 8;
              if((spb0 & 0xffff) > 0xff00) {
                spb0 = 0xff00;
              }

              //LAB_80103834
              final GpuCommandPoly cmd = new GpuCommandPoly(3)
                .translucent(translucency)
                .pos(0, spa0 >> 8, spa4 >> 8)
                .pos(1, sp50 >> 8, sp54 >> 8)
                .pos(2, spa8 >> 8, spac >> 8)
                .monochrome(spb0 >>> 9);

              GPU.queueCommand(data._10.z_22 + sp18 >> 2, cmd);
            }

            //LAB_80103994
            sp0x80[1].setX((short)(sp58 >> 8));
            sp0x80[1].setY((short)(sp5c >> 8));
            sp0x80[3].setX((short)(sp50 >> 8));
            sp0x80[3].setY((short)(sp54 >> 8));

            sp0x80[0].setX((short)sp30);
            sp0x80[0].setY((short)sp34);
            sp0x80[2].setX(sp0x20.get());
            sp0x80[2].setY(sp0x24.get());
            renderGradient(s4.colour_16, t4.colour_16, sp0x80, sp18, data._10.z_22, translucency);

            sp0x80[0].setX((short)((sp0x80[0].getX() - sp0x80[1].getX()) / data._10._30 + sp0x80[1].getX()));
            sp0x80[0].setY((short)((sp0x80[0].getY() - sp0x80[1].getY()) / data._10._30 + sp0x80[1].getY()));
            sp0x80[2].setX((short)((sp0x80[2].getX() - sp0x80[3].getX()) / data._10._30 + sp0x80[3].getX()));
            sp0x80[2].setY((short)((sp0x80[2].getY() - sp0x80[3].getY()) / data._10._30 + sp0x80[3].getY()));
            renderGradient(s4.colour_10, t4.colour_10, sp0x80, sp18, data._10.z_22, translucency);

            sp0x80[0].setX((short)sp38);
            sp0x80[0].setY((short)sp3c);
            sp0x80[2].setX(sp0x28.get());
            sp0x80[2].setY(sp0x2c.get());
            renderGradient(s4.colour_16, t4.colour_16, sp0x80, sp18, data._10.z_22, translucency);

            sp0x80[0].setX((short)((sp0x80[0].getX() - sp0x80[1].getX()) / data._10._30 + sp0x80[1].getX()));
            sp0x80[0].setY((short)((sp0x80[0].getY() - sp0x80[1].getY()) / data._10._30 + sp0x80[1].getY()));
            sp0x80[2].setX((short)((sp0x80[2].getX() - sp0x80[3].getX()) / data._10._30 + sp0x80[3].getX()));
            sp0x80[2].setY((short)((sp0x80[2].getY() - sp0x80[3].getY()) / data._10._30 + sp0x80[3].getY()));
            renderGradient(s4.colour_10, t4.colour_10, sp0x80, sp18, data._10.z_22, translucency);

            sp0x20.set((short)sp30);
            sp0x24.set((short)sp34);
            sp0x28.set((short)sp38);
            sp0x2c.set((short)sp3c);
            sp60 = sp50;
            sp64 = sp54;
            sp50 = sp68;
            sp54 = sp6c;
          }

          //LAB_80103ca0
        }
      }
    }
  }

  @Method(0x80103db0L)
  public static void FUN_80103db0(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    int v1;
    int a0;
    long s0;
    int s2;
    int s3;
    int s5;
    int sp3c;
    int spdc;
    int sp38;
    int sp2c;
    int spd8;
    int sp34;
    int sp30;
    int sp28;
    int sp1c;
    int sp24;
    int sp20;
    int sp18;
    int spc4;
    int spc0;
    boolean spb4 = true;
    final BttlScriptData6cSub38 spb8 = (BttlScriptData6cSub38)manager.effect_44;
    final long spbc = mallocTail(spb8._28.get() * 0x8L);

    if(spb8._04.get() + 1 == spb8._0c.get()) {
      free(spbc);
      return;
    }

    spb8._04.incr();

    //LAB_80103e40
    if(spb8._04.get() == 1) {
      FUN_80102860(manager, spb8);
      free(spbc);

      //LAB_80103e8c
      for(int i = 0; i < spb8.count_00.get(); i++) {
        spb8.callback_2c.deref().run(manager, spb8, spb8._34.deref().get(i), i);
      }
    } else {
      //LAB_80103ee4
      spb8._2a.incr().and(0x1f);
      if((manager._10._24 >> spb8._2a.get() & 0x1) == 0) {
        spb4 = false;
      }

      //LAB_80103f18
      //LAB_80103f44
      for(int i = 0; i < spb8.count_00.get(); i++) {
        final BttlScriptData6cSub38Sub14 s7 = spb8._34.deref().get(i);

        if(spb8._24.get() == 0) {
          FUN_80102bfc(manager, spb8, s7);
        }

        //LAB_80103f6c
        spb8.callback_2c.deref().run(manager, spb8, s7, i);

        s7._02.sub((short)(spb8._10.get() << 7 >> 8));

        //LAB_80103fc4
        int fp;
        for(fp = 0; fp < spb8._28.get(); fp++) {
          final BttlScriptData6cSub38Sub14Sub30 s4 = s7.ptr_10.deref().get(fp);
          final long a3 = spbc + fp * 0x8L;
          s7.sz3_0c.set(FUN_800cfb94(manager, s7._04, new VECTOR().set(s4._00), MEMORY.ref(2, a3, ShortRef::new), MEMORY.ref(2, a3 + 0x4L, ShortRef::new)) / 4);
          s4.colour_10.sub(s4._1c);
          s4.colour_16.sub(s4._22);
          s4._2a.add((short)(spb8._10.get() << 8 >> 8));
        }

        //LAB_801040d0
        sp38 = (int)(MEMORY.ref(4, spbc).offset((fp - 1) * 0x8L).offset(0x0L).get() - MEMORY.ref(4, spbc).offset(0x0L).get());
        sp3c = (int)(MEMORY.ref(4, spbc).offset((fp - 1) * 0x8L).offset(0x4L).get() - MEMORY.ref(4, spbc).offset(0x4L).get());
        spc4 = -ratan2(sp38, sp3c);
        s5 = s7.ptr_10.deref().get(0)._28.get() * manager._10.scale_16.getX() >> 12;
        sp18 = (int)(MEMORY.ref(4, spbc).offset(0x0L).get() + (rcos(spc4) * s5 >> 12));
        sp1c = (int)(MEMORY.ref(4, spbc).offset(0x4L).get() + (rsin(spc4) * s5 >> 12));
        sp20 = (int)(MEMORY.ref(4, spbc).offset(0x0L).get() - (rcos(spc4) * s5 >> 12));
        sp24 = (int)(MEMORY.ref(4, spbc).offset(0x4L).get() - (rsin(spc4) * s5 >> 12));

        if(!spb4) {
          final Translucency translucency = Translucency.of(manager._10.flags_00 >>> 28 & 3);

          a0 = manager._10.z_22;
          v1 = a0 + s7.sz3_0c.get();
          if(v1 >= 0xa0) {
            if(v1 >= 0xffe) {
              s7.sz3_0c.set(0xffe - a0);
            }

            //LAB_8010422c
            //LAB_8010424c
            for(fp = 0; fp < spb8._28.get() - 1; fp++) {
              final BttlScriptData6cSub38Sub14Sub30 s4 = s7.ptr_10.deref().get(fp);
              final BttlScriptData6cSub38Sub14Sub30 t4 = s7.ptr_10.deref().get(fp + 1);
              s5 = s4._28.get() * manager._10.scale_16.getX() >> 12;
              spc0 = t4._28.get() * manager._10.scale_16.getX() >> 12;

              if(spb8._18.get() == 0) {
                s0 = spbc + fp * 0x8L;
                sp28 = (int)(MEMORY.ref(4, s0).offset(0x8L).get() + (rcos(spc4) * s5 >> 12));
                sp2c = (int)(MEMORY.ref(4, s0).offset(0xcL).get() + (rsin(spc4) * s5 >> 12));
                sp30 = (int)(MEMORY.ref(4, s0).offset(0x8L).get() - (rcos(spc4) * s5 >> 12));
                sp34 = (int)(MEMORY.ref(4, s0).offset(0xcL).get() - (rsin(spc4) * s5 >> 12));

                final DVECTOR[] sp0x50 = new DVECTOR[4];
                Arrays.setAll(sp0x50, n -> new DVECTOR());

                sp0x50[1].setX((short)MEMORY.ref(4, s0).offset(0x8L).get());
                sp0x50[1].setY((short)MEMORY.ref(4, s0).offset(0xcL).get());
                sp0x50[3].setX((short)MEMORY.ref(4, s0).offset(0x0L).get());
                sp0x50[3].setY((short)MEMORY.ref(4, s0).offset(0x4L).get());

                sp0x50[0].setX((short)sp28);
                sp0x50[0].setY((short)sp2c);
                sp0x50[2].setX((short)sp18);
                sp0x50[2].setY((short)sp1c);
                renderGradient(s4.colour_16, t4.colour_16, sp0x50, s7.sz3_0c.get(), manager._10.z_22, translucency);
                sp0x50[0].setX((short)((sp0x50[0].getX() - sp0x50[1].getX()) / manager._10._30 + sp0x50[1].getX()));
                sp0x50[0].setY((short)((sp0x50[0].getY() - sp0x50[1].getY()) / manager._10._30 + sp0x50[1].getY()));
                sp0x50[2].setX((short)((sp0x50[2].getX() - sp0x50[3].getX()) / manager._10._30 + sp0x50[3].getX()));
                sp0x50[2].setY((short)((sp0x50[2].getY() - sp0x50[3].getY()) / manager._10._30 + sp0x50[3].getY()));
                renderGradient(s4.colour_10, t4.colour_10, sp0x50, s7.sz3_0c.get(), manager._10.z_22, translucency);
                sp0x50[0].setX((short)sp30);
                sp0x50[0].setY((short)sp34);
                sp0x50[2].setX((short)sp20);
                sp0x50[2].setY((short)sp24);
                renderGradient(s4.colour_16, t4.colour_16, sp0x50, s7.sz3_0c.get(), manager._10.z_22, translucency);
                sp0x50[0].setX((short)((sp0x50[0].getX() - sp0x50[1].getX()) / manager._10._30 + sp0x50[1].getX()));
                sp0x50[0].setY((short)((sp0x50[0].getY() - sp0x50[1].getY()) / manager._10._30 + sp0x50[1].getY()));
                sp0x50[2].setX((short)((sp0x50[2].getX() - sp0x50[3].getX()) / manager._10._30 + sp0x50[3].getX()));
                sp0x50[2].setY((short)((sp0x50[2].getY() - sp0x50[3].getY()) / manager._10._30 + sp0x50[3].getY()));
                renderGradient(s4.colour_10, t4.colour_10, sp0x50, s7.sz3_0c.get(), manager._10.z_22, translucency);

                sp18 = sp28;
                sp1c = sp2c;
                sp20 = sp30;
                sp24 = sp34;
              } else {
                //LAB_801045e8
                s0 = spbc + fp * 0x8L;
                s2 = (int)Math.abs(MEMORY.ref(4, s0).offset(0x0L).get() - MEMORY.ref(4, s0).offset(0x8L).get());
                s2 = (int)(seed_800fa754.advance().get() % (s2 * 2 + 1) - s2 + MEMORY.ref(4, s0).offset(0x0L).get());
                s3 = (int)MEMORY.ref(4, s0).offset(0xcL).get();
                v1 = (int)MEMORY.ref(4, s0).offset(0x4L).get();
                int s0_0 = (int)((MEMORY.ref(4, s0).offset(0xcL).get() - MEMORY.ref(4, s0).offset(0x4L).get()) / 2 + MEMORY.ref(4, s0).offset(0x4L).get());
                spd8 = spc0 >> 2;
                spdc = s5 >> 2;

                //LAB_801046c4
                for(int s1 = 2; s1 > 0; s1--) {
                  final DVECTOR[] sp0x90 = new DVECTOR[4];
                  Arrays.setAll(sp0x90, n -> new DVECTOR());

                  sp0x90[0].setY((short)s0_0);
                  sp0x90[1].setY((short)s0_0);
                  sp0x90[2].setY((short)v1);
                  sp0x90[3].setY((short)v1);

                  sp0x90[0].setX((short)(s2 - spc0));
                  sp0x90[1].setX((short)(s2 + 1));
                  sp0x90[2].setX((short)(s3 - s5));
                  sp0x90[3].setX((short)(s3 + 1));
                  renderGradient(s4.colour_16, t4.colour_16, sp0x90, s7.sz3_0c.get(), manager._10.z_22, translucency);
                  sp0x90[0].setX((short)(s2 - spd8));
                  sp0x90[2].setX((short)(s3 - spdc));
                  renderGradient(s4.colour_10, t4.colour_10, sp0x90, s7.sz3_0c.get(), manager._10.z_22, translucency);
                  sp0x90[0].setX((short)(s2 + spc0));
                  sp0x90[1].setX((short)s2);
                  sp0x90[2].setX((short)(s3 + s5));
                  sp0x90[3].setX((short)s3);
                  renderGradient(s4.colour_16, t4.colour_16, sp0x90, s7.sz3_0c.get(), manager._10.z_22, translucency);
                  sp0x90[0].setX((short)(s2 + spd8));
                  sp0x90[2].setX((short)spdc);
                  renderGradient(s4.colour_10, t4.colour_10, sp0x90, s7.sz3_0c.get(), manager._10.z_22, translucency);

                  s3 = s2;
                  v1 = s0_0;
                  s2 = (int)MEMORY.ref(4, spbc).offset(fp * 0x8L).offset(0x8L).get();
                  s0_0 = (int)MEMORY.ref(4, spbc).offset(fp * 0x8L).offset(0xcL).get();
                }
              }
            }
          }
        }
      }

      //LAB_8010490c
      free(spbc);
    }

    //LAB_8010491c
  }

  @Method(0x80104954L)
  public static void FUN_80104954(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final BttlScriptData6cSub38 s2 = (BttlScriptData6cSub38)data.effect_44;

    //LAB_80104984
    for(int i = 0; i < s2.count_00.get(); i++) {
      free(s2._34.deref().get(i).ptr_10.getPointer());
    }

    //LAB_801049ac
    free(s2._34.getPointer());
  }

  @Method(0x801049d4L)
  public static void FUN_801049d4(final EffectManagerData6c a0, final BttlScriptData6cSub38 a1, final BttlScriptData6cSub38Sub14 a2, final int a3) {
    // no-op
  }

  @Method(0x801049dcL)
  public static void FUN_801049dc(final EffectManagerData6c a0, final BttlScriptData6cSub38 a1, final BttlScriptData6cSub38Sub14 a2, final int a3) {
    a2._04.setY((short)(0x1000 / a1.count_00.get() * a3));
    a2._04.setZ((short)(a1._1e.get() * 2));
  }

  @Method(0x80104a14L)
  public static void FUN_80104a14(final EffectManagerData6c a0, final BttlScriptData6cSub38 a1, final BttlScriptData6cSub38Sub14 a2, final int a3) {
    a2._04.setX((short)(seed_800fa754.advance().get() % 4097));
    a2._04.setY((short)(seed_800fa754.advance().get() % 4097));
    a2._04.setZ((short)(seed_800fa754.advance().get() % 4097));
  }

  @Method(0x80104b10L)
  public static void FUN_80104b10(final EffectManagerData6c a0, final BttlScriptData6cSub38 a1, final BttlScriptData6cSub38Sub14 a2, final int a3) {
    final int s4 = a0._10._28 << 8 >> 8;
    int s2 = a2._02.get();

    //LAB_80104b58
    for(int i = 0; i < a1._28.get(); i++) {
      final BttlScriptData6cSub38Sub14Sub30 s0 = a2.ptr_10.deref().get(i);
      s0._00.x.add(rcos(s2) * a1._1e.get() >> 12);
      s0._00.z.add(rsin(s2) * a1._1e.get() >> 12);
      s2 = s2 + s4;
    }

    //LAB_80104bcc
  }

  @Method(0x80104becL)
  public static void FUN_80104bec(final EffectManagerData6c a0, final BttlScriptData6cSub38 a1, final BttlScriptData6cSub38Sub14 a2, final int a3) {
    final int s4 = a0._10._28 << 8 >> 8;
    int s2 = a2._02.get();

    //LAB_80104c34
    for(int i = 0; i < a1._28.get(); i++) {
      final BttlScriptData6cSub38Sub14Sub30 s1 = a2.ptr_10.deref().get(i);
      s1._00.z.add(rsin(s2) * a1._1e.get() >> 12);
      s2 = s2 + s4;
    }

    //LAB_80104c7c
  }

  @Method(0x80104c9cL)
  public static void FUN_80104c9c(final EffectManagerData6c manager, final BttlScriptData6cSub38 effect, final BttlScriptData6cSub38Sub14 a2, final int a3) {
    int sp10 = 0;
    int s5 = (effect._28.get() - 2) / 2;
    final int sp14 = (manager._10._28 & 0xff) << 4;
    final int fp = (manager._10._28 & 0xff00) >>> 4;
    final int s6 = manager._10._28 >>> 12 & 0xff0;

    //LAB_80104d24
    for(int i = 1; i < effect._28.get(); i++) {
      final BttlScriptData6cSub38Sub14Sub30 s3 = a2.ptr_10.deref().get(i);
      sp10 = sp10 + s5;
      final int s4 = sp10 * effect._1e.get();
      final int x = s4 * (rsin(fp) + rcos(s6)) >> 12;
      final int y = s4 * (rcos(sp14) + rcos(s6)) >> 12;
      final int z = s4 * (rcos(fp) + rsin(sp14)) >> 12;
      s3._00.add(x, y, z);
      s5--;
    }
  }

  @Method(0x80104e40L)
  public static void FUN_80104e40(final EffectManagerData6c manager, final BttlScriptData6cSub38 effect, final BttlScriptData6cSub38Sub14 a2, final int a3) {
    final int s3 = effect._1e.get() / effect.count_00.get() & 0xffff;
    final int s2 = 0x1000 / effect.count_00.get() * a3;

    //LAB_80104ec4
    short sp10 = 0;
    short sp12 = 0;
    for(int i = 1; i < effect._28.get(); i++) {
      final BttlScriptData6cSub38Sub14Sub30 s0 = a2.ptr_10.deref().get(i);
      s0._00.x.add(sp10);
      s0._00.z.add(sp12);
      sp10 += rcos(s2) * s3 >> 12;
      sp12 += rsin(s2) * s3 >> 12;
    }
  }

  @Method(0x80104f70L)
  public static void FUN_80104f70(final EffectManagerData6c manager, final BttlScriptData6cSub38 effect, final BttlScriptData6cSub38Sub14 a2, final int a3) {
    final int step = 0x1000 / (effect._28.get() - 1);
    int angle = 0;

    //LAB_80104fb8
    for(int i = 0; i < effect._28.get(); i++) {
      final BttlScriptData6cSub38Sub14Sub30 s0 = a2.ptr_10.deref().get(i);
      s0._00.x.add(rsin(angle) * effect._1e.get() >> 12);
      s0._00.z.add(rcos(angle) * effect._1e.get() >> 12);
      s0._00.y.set(0);
      angle += step;
    }
  }

  @Method(0x80105050L)
  public static void FUN_80105050(final EffectManagerData6c manager, final BttlScriptData6cSub38 effect, final BttlScriptData6cSub38Sub14 a2, final int a3) {
    final int a0 = effect._28.get();
    final int s6 = 0x800 / (a0 - 1);
    final int s5 = 0x1000 / effect.count_00.get() * a3;
    int s2 = 0;

    //LAB_801050c0
    for(int i = 0; i < a0; i++) {
      final BttlScriptData6cSub38Sub14Sub30 s1 = a2.ptr_10.deref().get(i);
      s1._00.x.add(rcos(s2) * effect._1e.get() >> 12);
      s1._00.y.set((rsin(s2) * rsin(s2) >> 12) * effect._1e.get() >> 12);
      s1._00.z.add((rsin(s2) * rcos(s5) >> 12) * effect._1e.get() >> 12);
      s2 += s6;
    }
  }

  @Method(0x801051acL)
  public static void FUN_801051ac(final EffectManagerData6c manager, final BttlScriptData6cSub38 effect, final BttlScriptData6cSub38Sub14 a2, final int a3) {
    final int a1 = effect._28.get();
    final int s6 = 0x1000 / (a1 - 1);
    final int s5 = manager._10._28 << 8 >> 8;
    long s3 = a2._02.get();
    long s2 = 0;

    //LAB_80105210
    for(int i = 0; i < a1; i++) {
      final BttlScriptData6cSub38Sub14Sub30 s0 = a2.ptr_10.deref().get(i);
      s0._00.x.add(rsin(s2) * effect._1e.get() >> 12);
      s0._00.y.set(rsin(s3) * effect._1e.get() / 4 >> 12);
      s0._00.z.add(rcos(s2) * effect._1e.get() >> 12);
      s2 = s2 + s6;
      s3 = s3 + s5;
    }
  }

  @Method(0x801052d4L)
  public static void FUN_801052d4(final EffectManagerData6c a0, final BttlScriptData6cSub38 a1, final BttlScriptData6cSub38Sub14 a2, final int a3) {
    // no-op
  }

  @Method(0x801052dcL)
  public static FlowControl FUN_801052dc(final RunningScript<? extends BattleScriptDataBase> script) {
    final int s0 = script.params_20[6].get();
    final int s1 = script.params_20[7].get();

    //TODO counter-attack electricity
    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0x38,
      null,
      _80119f14[s1],
      SEffe::FUN_80104954,
      BttlScriptData6cSub38::new
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    final BttlScriptData6cSub38 effect = (BttlScriptData6cSub38)manager.effect_44;
    effect.count_00.set(script.params_20[3].get());
    effect._04.set(0);
    effect.scriptIndex_08.set(script.params_20[1].get());
    effect._0c.set(s0 >> 16 & 0xff);
    effect._10.set(script.params_20[5].get());
    effect._14.set(s0 >>> 24 & 0x8);
    effect._18.set(s0 >>> 24 & 0x10);
    effect._1c.set((short)script.params_20[2].get());
    effect._1e.set((short)script.params_20[4].get());
    effect._20.set((short)s1);
    effect._22.set(s0 >>> 24 & 0x1);
    effect._23.set(s0 >>> 24 & 0x2);
    effect._24.set(s0 >>> 24 & 0x4);
    effect._26.set(s0 & 0xff);
    effect._28.set(s0 >> 8 & 0xff);
    effect._29.set(s0 >>> 24 & 0x20);
    effect._2a.set(0);
    effect.callback_2c.set(_80119ee8.get(s1).deref());
    effect._34.setPointer(mallocTail(effect.count_00.get() * 0x14L));

    if(effect._0c.get() == 0) {
      effect._0c.set(-1);
    }

    //LAB_8010549c
    //LAB_801054b4
    for(int i = 0; i < effect.count_00.get(); i++) {
      final BttlScriptData6cSub38Sub14 struct = effect._34.deref().get(i);
      struct._00.set(1);
      struct._02.set((short)(seed_800fa754.advance().get() % 4097));
      struct._04.set((short)0, (short)0, (short)0);
      struct.ptr_10.setPointer(mallocTail(effect._28.get() * 0x30L));
      _80119ebc.get(effect._20.get()).deref().run(manager, effect, struct, i);
      FUN_80102bfc(manager, effect, struct);
    }

    //LAB_80105590
    manager._10.flags_00 |= 0x5000_0000;
    manager._10.colour_1c.set((short)0, (short)0, (short)0xff);
    manager._10._28 = 0x100;
    manager._10._30 = 3;

    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x80105604L)
  public static FlowControl FUN_80105604(final RunningScript<?> script) {
    final EffectManagerData6c a0 = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final BttlScriptData6cSub38Sub14 a1 = ((BttlScriptData6cSub38)a0.effect_44)._34.deref().get(script.params_20[1].get());
    final BttlScriptData6cSub38Sub14Sub30 v0 = a1.ptr_10.deref().get(script.params_20[2].get());

    final VECTOR sp0x10 = new VECTOR().set(v0._00);
    final VECTOR sp0x20 = new VECTOR();
    FUN_800cf4f4(a0, a1._04, sp0x10, sp0x20);
    script.params_20[3].set(sp0x20.getX());
    script.params_20[4].set(sp0x20.getY());
    script.params_20[5].set(sp0x20.getZ());
    return FlowControl.CONTINUE;
  }

  @Method(0x80105704L)
  public static void FUN_80105704(final ScriptState<BttlScriptData6cSub1c_2> state, final BttlScriptData6cSub1c_2 data) {
    final DVECTOR[] sp0x18 = new DVECTOR[4];
    Arrays.setAll(sp0x18, n -> new DVECTOR());

    //LAB_8010575c
    final Translucency translucency = Translucency.of(data._10 >>> 28 & 3);

    for(int s7 = 0; s7 < data.count_00; s7++) {
      //LAB_8010577c
      for(int s6 = 0; s6 < data.count_0c - 1; s6++) {
        final BttlScriptData6cSub1c_2Sub1e s4_1 = data._18.get(s7).deref().get(s6);
        final BttlScriptData6cSub1c_2Sub1e s4_2 = data._18.get(s7).deref().get(s6 + 1);

        sp0x18[0].setX((short)(s4_2.x_00.get() - s4_2._1c.get()));
        sp0x18[0].setY(s4_2.y_02.get());
        sp0x18[1].setX((short)(s4_2.x_00.get() + 1));
        sp0x18[1].setY(s4_2.y_02.get());
        sp0x18[2].setX((short)(s4_1.x_00.get() - s4_1._1c.get()));
        sp0x18[2].setY(s4_1.y_02.get());
        sp0x18[3].setX((short)(s4_1.x_00.get() + 1));
        sp0x18[3].setY(s4_1.y_02.get());
        renderGradient(s4_1.svec_0a, s4_2.svec_0a, sp0x18, data.z_14, data._08, translucency);
        sp0x18[0].setX((short)(s4_2.x_00.get() - s4_2._1c.get() / 3));
        sp0x18[2].setX((short)(s4_1.x_00.get() - s4_1._1c.get() / 3));
        renderGradient(s4_1.svec_04, s4_2.svec_04, sp0x18, data.z_14, data._08, translucency);
        sp0x18[0].setX((short)(s4_2.x_00.get() + s4_2._1c.get()));
        sp0x18[1].setX(s4_2.x_00.get());
        sp0x18[2].setX((short)(s4_1.x_00.get() + s4_1._1c.get()));
        sp0x18[3].setX(s4_1.x_00.get());
        renderGradient(s4_1.svec_0a, s4_2.svec_0a, sp0x18, data.z_14, data._08, translucency);
        sp0x18[0].setX((short)(s4_2.x_00.get() + s4_2._1c.get() / 3));
        sp0x18[2].setX((short)(s4_1.x_00.get() + s4_1._1c.get() / 3));
        renderGradient(s4_1.svec_04, s4_2.svec_04, sp0x18, data.z_14, data._08, translucency);
      }
    }

    //LAB_801059c8
  }

  @Method(0x80105aa0L)
  public static void FUN_80105aa0(final ScriptState<BttlScriptData6cSub1c_2> state, final BttlScriptData6cSub1c_2 data) {
    data._04--;

    if(data._04 <= 0) {
      state.deallocateWithChildren();
    } else {
      //LAB_80105ad8
      //LAB_80105aec
      for(int a3 = 0; a3 < data.count_00; a3++) {
        //LAB_80105b00
        for(int a2 = 0; a2 < data.count_0c; a2++) {
          final BttlScriptData6cSub1c_2Sub1e a0 = data._18.get(a3).deref().get(a2);
          a0.svec_04.sub(a0.svec_10);
          a0.svec_0a.sub(a0.svec_16);
        }
      }
    }
  }

  @Method(0x80105bb8L)
  public static void FUN_80105bb8(final ScriptState<BttlScriptData6cSub1c_2> state, final BttlScriptData6cSub1c_2 data) {
    //LAB_80105be8
    for(int i = 0; i < data.count_00; i++) {
      free(data._18.get(i).getPointer());
    }

    //LAB_80105c10
    free(data._18.getAddress());
  }

  @Method(0x80105c38L)
  public static FlowControl FUN_80105c38(final RunningScript<?> script) {
    final VECTOR sp0x18 = new VECTOR();
    final ShortRef refX = new ShortRef();
    final ShortRef refY = new ShortRef();

    final int scriptIndex = script.params_20[0].get();
    final int s3 = script.params_20[1].get();
    final EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00;
    final BttlScriptData6cSub38 s1 = (BttlScriptData6cSub38)manager.effect_44;
    final ScriptState<BttlScriptData6cSub1c_2> state = SCRIPTS.allocateScriptState(new BttlScriptData6cSub1c_2());
    state.loadScriptFile(doNothingScript_8004f650);
    state.setTicker(SEffe::FUN_80105aa0);
    state.setRenderer(SEffe::FUN_80105704);
    state.setDestructor(SEffe::FUN_80105bb8);
    final BttlScriptData6cSub1c_2 effect = state.innerStruct_00;
    effect.count_00 = s1.count_00.get();
    effect._04 = s3;
    effect.count_0c = s1._28.get();
    effect._10 = manager._10.flags_00;
    effect._18 = MEMORY.ref(4, mallocTail(effect.count_00 * 0x4), UnboundedArrayRef.of(0x4, Pointer.deferred(4, UnboundedArrayRef.of(0x1e, BttlScriptData6cSub1c_2Sub1e::new))));

    //LAB_80105d64
    for(int s7 = 0; s7 < effect.count_00; s7++) {
      final BttlScriptData6cSub38Sub14 struct14 = s1._34.deref().get(s7);
      effect._18.get(s7).setPointer(mallocTail(effect.count_0c * 0x1e));

      //LAB_80105da0
      for(int s4 = 0; s4 < effect.count_0c; s4++) {
        final BttlScriptData6cSub38Sub14Sub30 s0 = struct14.ptr_10.deref().get(s4);
        final BttlScriptData6cSub1c_2Sub1e struct1e = effect._18.get(s7).deref().get(s4);
        struct1e._1c.set(s0._28.get() * manager._10.scale_16.getX() >> 12);

        sp0x18.set(s0._00);
        final int z = FUN_800cfb94(manager, struct14._04, sp0x18, refX, refY) >> 2;
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
          struct1e.x_00.set(refX.get());
          struct1e.y_02.set(refY.get());
          struct1e.svec_04.set(s0.colour_10);
          struct1e.svec_0a.set(s0.colour_16);
          struct1e.svec_10.set(struct1e.svec_04).div(s3);
          struct1e.svec_16.set(struct1e.svec_0a).div(s3);
        }
      }
    }

    //LAB_80105f64
    return FlowControl.CONTINUE;
  }

  @Method(0x80105f98L)
  public static void FUN_80105f98(final int scriptIndex, final VECTOR a1, final long a2) {
    final MATRIX sp0x10 = new MATRIX();
    final VECTOR sp0x30 = new VECTOR();

    final BattleObject27c v0 = (BattleObject27c)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00;

    final GsCOORDINATE2 coord2;
    if(a2 == 0) {
      coord2 = v0.model_148.coord2ArrPtr_04[1];
    } else {
      //LAB_80105fe4
      coord2 = v0.model_148.coord2_14;
    }

    //LAB_80105fec
    GsGetLw(coord2, sp0x10);
    a1.set(ApplyMatrixLV(sp0x10, sp0x30)).add(sp0x10.transfer);
  }

  @Method(0x80106050L)
  public static void renderAdditionButton(final long a0, final long a1) {
    final long callback = getMethodAddress(Bttl_800d.class, "FUN_800d46d4", RunningScript.class);

    // Params: ?, x, y, translucency, colour

    if(Math.abs((byte)a0) >= 2) {
      callScriptFunction(callback, 0x24, 119, 43, Translucency.B_PLUS_F.ordinal(), 0x80);
      callScriptFunction(callback, (int)_800fb7bc.offset(1, 0x0L).offset(a1).get(), 115, 48, 0x1, 0x80);
    } else {
      //LAB_80106114
      callScriptFunction(callback, 0x24, 119, 51, Translucency.B_PLUS_F.ordinal(), 0x80);
      callScriptFunction(callback, (int)_800fb7bc.offset(1, 0x2L).offset(a1).get(), 115, 48, Translucency.B_PLUS_F.ordinal(), 0x80);
      callScriptFunction(callback, 0x25, 115, 50, Translucency.B_PLUS_F.ordinal(), 0x80);
    }
  }

  @Method(0x801061bcL)
  public static long FUN_801061bc(final int charSlot, final int hitNum, final int a2, final long a3) {
    //LAB_80106264
    final long v0;
    if(a3 == 0x1L || a3 == 0x3L) {
      //LAB_80106274
      v0 = _800fb7c0.offset(hitNum * 0x10L).offset(1, a2).get();
    } else {
      //LAB_8010628c
      v0 = getHitMultiplier(charSlot, hitNum, a2) & 0xffL;
    }

    //LAB_80106298
    return v0;
  }

  @Method(0x801062a8L)
  public static void FUN_801062a8(final int scriptIndex, final int a1, final AdditionOverlaysEffect44 a2, final long a3) {
    long v0;
    int hitNum;
    long s6;
    long s7;
    final BattleObject27c s5 = (BattleObject27c)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00;

    //LAB_8010633c
    for(hitNum = 0; hitNum < 8; hitNum++) {
      if((FUN_801061bc(s5.charSlot_276, hitNum, 1, a3) & 0xffL) == 0) {
        break;
      }
    }

    //LAB_80106374
    v0 = hitNum - 1;
    a2.count_30.set((int)v0);
    a2.scriptIndex_00.set(scriptIndex);
    a2.scriptIndex_04.set(a1);
    a2._34.set((short)0);
    a2._31.set(0);
    a2._32.set(0);
    a2._38.set(0);
    a2._39.set(0);
    a2._3a.set((int)a3);
    a2._40.set(mallocTail((v0 & 0xffL) * 0x20L));
    s6 = FUN_801061bc(s5.charSlot_276, 0, 15, a3) & 0xffL;
    a2._36.set((int)s6);

    s7 = a2._40.get();

    //LAB_801063f0
    for(hitNum = 0; hitNum < a2.count_30.get(); hitNum++) {
      MEMORY.ref(1, s7).offset(0x0L).setu(0x1L);
      MEMORY.ref(1, s7).offset(0x1L).setu(0);
      MEMORY.ref(2, s7).offset(0x8L).setu(0);
      MEMORY.ref(2, s7).offset(0x10L).setu(s6 + 0x2L);
      seed_800fa754.advance();
      MEMORY.ref(1, s7).offset(0x2L).setu(0x3L);
      MEMORY.ref(1, s7).offset(0x1cL).setu(0);
      _8011a014.offset(hitNum).offset(1, 0x0L).setu(0);
      v0 = FUN_801061bc(s5.charSlot_276, hitNum, 1, a3) & 0xffL;
      s6 = s6 + v0;
      MEMORY.ref(2, s7).offset(0xaL).setu(v0);
      v0 = FUN_801061bc(s5.charSlot_276, hitNum, 2, a3) & 0xffL;
      MEMORY.ref(2, s7).offset(0xcL).setu(v0);
      v0 = FUN_801061bc(s5.charSlot_276, hitNum, 3, a3) & 0xffL;
      MEMORY.ref(2, s7).offset(0xeL).setu(v0);
      long a3_0 = MEMORY.ref(2, s7).offset(0x10L).get() + MEMORY.ref(2, s7).offset(0xcL).get();
      MEMORY.ref(2, s7).offset(0x10L).setu(a3_0 - (short)v0 / 2 + 1);
      MEMORY.ref(2, s7).offset(0x12L).setu(a3_0 + v0 - (short)MEMORY.ref(2, s7).offset(0xeL).get() / 2);

      a3_0 = mallocTail(0xeeL);
      MEMORY.ref(4, s7).offset(0x18L).setu(a3_0);

      //LAB_8010652c
      for(int i = 16; i >= 0; i--) {
        MEMORY.ref(2, a3_0).offset(0x8L).setu((18 - i) * 10);
        MEMORY.ref(1, a3_0).offset(0x0L).setu(0x1L);
        //LAB_8010656c
        //LAB_80106574
        MEMORY.ref(2, a3_0).offset(0x2L).setu((16 - i) * 0x80 + 0x200L);
        MEMORY.ref(1, a3_0).offset(0xcL).setu(0x5L);
        MEMORY.ref(1, a3_0).offset(0xdL).setu(0);
        MEMORY.ref(2, a3_0).offset(0xaL).setu(MEMORY.ref(2, s7).offset(0x10L).get() + (MEMORY.ref(2, s7).offset(0xeL).getSigned() - 0x1L) / 2 + i - 0x11L);
        MEMORY.ref(1, a3_0).offset(0x4L).setu(_800fb7f0.offset(1, MEMORY.ref(1, s7).offset(0x2L).getSigned() * 3).offset(0x0L).get());
        MEMORY.ref(1, a3_0).offset(0x5L).setu(_800fb7f0.offset(1, MEMORY.ref(1, s7).offset(0x2L).getSigned() * 3).offset(0x1L).get());
        MEMORY.ref(1, a3_0).offset(0x6L).setu(_800fb7f0.offset(1, MEMORY.ref(1, s7).offset(0x2L).getSigned() * 3).offset(0x2L).get());
        a3_0 = a3_0 + 0xeL;
      }

      a3_0 = a3_0 - 0xeL;

      //LAB_80106634
      for(int i = 0; i < 3; i++) {
        MEMORY.ref(2, a3_0).offset(0x8L).setu(0x14L - i * 0x2L);
        MEMORY.ref(1, a3_0).offset(0x0L).setu(0x1L);
        MEMORY.ref(2, a3_0).offset(0x2L).setu(0x200L);
        MEMORY.ref(1, a3_0).offset(0xcL).setu(0x11L);
        MEMORY.ref(1, a3_0).offset(0xdL).setu(0x1L);
        MEMORY.ref(2, a3_0).offset(0xaL).setu(MEMORY.ref(2, s7).offset(0x10L).get() - 0x11L);

        if(i != 0x1L) {
          MEMORY.ref(1, a3_0).offset(0x4L).setu(0x30L);
          MEMORY.ref(1, a3_0).offset(0x5L).setu(0x30L);
          MEMORY.ref(1, a3_0).offset(0x6L).setu(0x30L);
        } else {
          //LAB_80106680
          MEMORY.ref(1, a3_0).offset(0xdL).setu(0xffL);
        }

        //LAB_80106684
        a3_0 = a3_0 - 0xeL;
      }

      MEMORY.ref(4, s7).offset(0x14L).setu(a3_0);
      s7 = s7 + 0x20L;
    }

    //LAB_801066c8
    FUN_80105f98(a2.scriptIndex_00.get(), a2.vec_10, 0);

    final VECTOR sp0x10 = new VECTOR();
    FUN_80105f98(a2.scriptIndex_04.get(), sp0x10, 0x1L);

    final int a0_0 = (int)MEMORY.ref(4, a2._40.get()).offset(0x10L).getSigned();
    a2.vec_20.setX(sp0x10.getX() - a2.vec_10.getX() / a0_0);
    a2.vec_20.setY(sp0x10.getY() - a2.vec_10.getY() / a0_0);
    a2.vec_20.setZ(sp0x10.getZ() - a2.vec_10.getZ() / a0_0);
  }

  @Method(0x80106774L)
  public static long FUN_80106774(final long a0, final long a1) {
    long numberOfNegativeComponents = 0;
    long v0 = MEMORY.ref(1, a0).offset(0x4L).get() - a1;
    final long sp10;
    if((int)v0 > 0) {
      sp10 = v0;
    } else {
      sp10 = 0;
      numberOfNegativeComponents++;
    }

    //LAB_801067b0
    v0 = MEMORY.ref(1, a0).offset(0x5L).get() - a1;
    final long sp14;
    if((int)v0 > 0) {
      sp14 = v0;
    } else {
      sp14 = 0;
      numberOfNegativeComponents++;
    }

    //LAB_801067c4
    v0 = MEMORY.ref(1, a0).offset(0x6L).get() - a1;
    final long sp18;
    if((int)v0 > 0) {
      sp18 = v0;
    } else {
      sp18 = 0;
      numberOfNegativeComponents++;
    }

    //LAB_801067d8
    MEMORY.ref(1, a0).offset(0x4L).setu(sp10);
    MEMORY.ref(1, a0).offset(0x5L).setu(sp14);
    MEMORY.ref(1, a0).offset(0x6L).setu(sp18);
    return numberOfNegativeComponents;
  }

  @Method(0x80106808L)
  public static void renderAdditionCentreSolidSquare(final BttlScriptData6cSubBase1 a0, final long a1, final long a2, final ScriptState<EffectManagerData6c> a3, final EffectManagerData6c a4) {
    if(a4._10.flags_00 >= 0) {
      final long v0 = MEMORY.ref(4, a1).offset(0x14L).get();

      //LAB_8010685c
      for(int s5 = 0; s5 < 2; s5++) {
        final int s3 = (short)MEMORY.ref(2, v0).offset(0x24L).getSigned() - s5 * 8;

        //LAB_80106874
        final int[] sp0x18 = new int[8];
        for(int i = 0; i < 4; i++) {
          sp0x18[i * 2    ] =  rcos((short)MEMORY.ref(2, v0).offset(0x1eL).getSigned() + i * 0x400) * s3 >> 12;
          sp0x18[i * 2 + 1] = (rsin((short)MEMORY.ref(2, v0).offset(0x1eL).getSigned() + i * 0x400) * s3 >> 12) + 30;
        }

        final GpuCommandPoly cmd = new GpuCommandPoly(4);

        if(a2 == 1) {
          cmd.monochrome(0xff);
          //LAB_80106918
        } else if(a2 != -2) {
          //LAB_80106988
          cmd.monochrome(0x30);
        } else if(MEMORY.ref(1, a1).offset(0x1cL).getSigned() != 0) {
          cmd.rgb((int)MEMORY.ref(1, v0).offset(0x12L).get() * 3, (int)MEMORY.ref(1, v0).offset(0x13L).get(), ((int)MEMORY.ref(1, v0).offset(0x14L).get() - 1) * 8);
        } else {
          //LAB_80106964
          cmd.rgb((int)MEMORY.ref(1, v0).offset(0x12L).get(), (int)MEMORY.ref(1, v0).offset(0x13L).get(), (int)MEMORY.ref(1, v0).offset(0x14L).get());
        }

        //LAB_80106994
        cmd
          .translucent(Translucency.B_PLUS_F)
          .pos(0, sp0x18[0], sp0x18[1])
          .pos(1, sp0x18[2], sp0x18[3])
          .pos(2, sp0x18[6], sp0x18[5])
          .pos(3, sp0x18[4], sp0x18[7]);
        GPU.queueCommand(30, cmd);
      }
    }

    //LAB_80106a4c
  }

  @Method(0x80106ac4L)
  public static void renderRotatedSquare(final long a0, final int angle, final int a2) {
    final int s2 = angle + 0x400;
    final int s0 = a2 - 1;
    final int s1 = a2 - 11;
    final int x0 = rcos(angle) * s0 >> 12;
    final int x1 = rcos(s2) * s0 >> 12;
    final int x2 = rcos(angle) * s1 >> 12;
    final int x3 = rcos(s2) * s1 >> 12;
    final int y0 = rsin(angle) * s0 >> 12;
    final int y1 = rsin(s2) * s0 >> 12;
    final int y2 = rsin(angle) * s1 >> 12;
    final int y3 = rsin(s2) * s1 >> 12;
    final int colour = (int)(MEMORY.ref(2, a0).offset(0x8L).getSigned() * 4);

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
  public static void renderAdditionBorderSquares(final long a0, final long a1, final BttlScriptData6cSubBase1 a2, final long a3, final ScriptState<EffectManagerData6c> a4) {
    long s7 = MEMORY.ref(4, a3).offset(0x18L).get();
    final long sp28 = _8011a014.offset(a1).getAddress();
    long s3 = s7 + 0x2L;

    //LAB_80106d18
    for(long s6 = 0; s6 < 17; s6++) {
      if(MEMORY.ref(1, s7).offset(0x0L).getSigned() != 0) {
        if(MEMORY.ref(2, s3).offset(0x8L).getSigned() <= 0) {
          final int s2 = (int)MEMORY.ref(2, s3).offset(0x6L).getSigned();
          int sp20 = rcos(MEMORY.ref(2, s3).offset(0x0L).getSigned()) * s2 >> 12;
          int sp24 = (rsin(MEMORY.ref(2, s3).offset(0x0L).getSigned()) * s2 >> 12) + 30;

          //LAB_80106d80
          int s4 = 0;
          for(long s5 = 0; s5 < 4; s5++) {
            final GpuCommandLine cmd = new GpuCommandLine();

            final long v1 = MEMORY.ref(1, s3).offset(0xbL).get();

            //LAB_80106dc0
            if(v1 != 0 && v1 != 0xffL || MEMORY.ref(1, sp28).offset(0x0L).getSigned() < 0) {
              //LAB_80106de8
              cmd.translucent(Translucency.B_PLUS_F);
            }


            if(MEMORY.ref(1, a3).offset(0x1cL).getSigned() != 0 && s6 != 0x10L) {
              cmd.rgb((int)MEMORY.ref(1, s3).offset(0x2L).get() * 3, (int)MEMORY.ref(1, s3).offset(0x3L).get(), ((int)MEMORY.ref(1, s3).offset(0x4L).get() + 1) / 8);
            } else {
              //LAB_80106e58
              cmd.rgb((int)MEMORY.ref(1, s3).offset(0x2L).get(), (int)MEMORY.ref(1, s3).offset(0x3L).get(), (int)MEMORY.ref(1, s3).offset(0x4L).get());
            }

            //LAB_80106e74
            s4 = s4 + 0x400;
            final int sp18 = rcos(MEMORY.ref(2, s3).offset(0x0L).getSigned() + s4) * s2 >> 12;
            final int sp1c = (rsin(MEMORY.ref(2, s3).offset(0x0L).getSigned() + s4) * s2 >> 12) + 30;
            cmd
              .pos(0, sp20, sp24)
              .pos(1, sp18, sp1c);

            GPU.queueCommand(30, cmd);

            sp20 = sp18;
            sp24 = sp1c;
          }

          if(MEMORY.ref(1, s3).offset(0xbL).get() == 0) {
            renderRotatedSquare(a3, (int)MEMORY.ref(2, s3).offset(0x0L).get() + s4        , s2);
            renderRotatedSquare(a3, (int)MEMORY.ref(2, s3).offset(0x0L).get() + s4 + 0x400, s2);
            renderRotatedSquare(a3, (int)MEMORY.ref(2, s3).offset(0x0L).get() + s4 + 0x800, s2);
            renderRotatedSquare(a3, (int)MEMORY.ref(2, s3).offset(0x0L).get() + s4 + 0xc00, s2);
          }
        }
      }

      //LAB_80106fac
      s3 = s3 + 0xeL;
      s7 = s7 + 0xeL;
    }
  }

  @Method(0x80107088L)
  public static long FUN_80107088(final long a0, final long a1, final AdditionOverlaysEffect44 a2, final long a3) {
    if(a2._34.get() >= MEMORY.ref(2, a3).offset(0x10L).getSigned() - 0x11L) {
      MEMORY.ref(2, a3).offset(0x8L).addu(0x1L);

      if(MEMORY.ref(2, a3).offset(0x8L).getSigned() >= 0xeL) {
        MEMORY.ref(2, a3).offset(0x8L).setu(0xdL);
      }
    }

    //LAB_801070ec
    final long s5 = _8011a014.offset(a1).getAddress();
    long s1 = MEMORY.ref(4, a3).offset(0x18L).get();
    long s4 = 0;

    //LAB_80107104
    for(int i = 0; i < 17; i++) {
      if(MEMORY.ref(1, s5).offset(0x0L).getSigned() < 0) {
        MEMORY.ref(2, a3).offset(0x8L).subu(0x3L);

        if(MEMORY.ref(2, a3).offset(0x8L).getSigned() < 0) {
          MEMORY.ref(2, a3).offset(0x8L).setu(0);
        }

        //LAB_80107134
        if(FUN_80106774(s1, 0x20L) == 0x3L) {
          MEMORY.ref(1, s1).offset(0x0L).setu(0);
        }
      }

      //LAB_80107150
      if(MEMORY.ref(1, s1).offset(0x0L).getSigned() != 0) {
        if(MEMORY.ref(2, s1).offset(0xaL).getSigned() > 0) {
          MEMORY.ref(2, s1).offset(0xaL).subu(0x1L);
        } else {
          //LAB_80107178
          if(MEMORY.ref(1, s1).offset(0xdL).get() != 0xffL) {
            MEMORY.ref(1, s1).offset(0xdL).addu(0x1L);
          }

          //LAB_80107190
          MEMORY.ref(1, s1).offset(0xcL).subu(0x1L);
          if(MEMORY.ref(1, s1).offset(0xcL).get() == 0) {
            MEMORY.ref(1, s1).offset(0x0L).setu(0);
          }

          //LAB_801071b0
          if(i < 14) {
            FUN_80106774(s1, 0x4eL);
          }

          s4 = 0x1L;
        }
      }

      //LAB_801071c0
      s1 = s1 + 0xeL;
    }

    return s4;
  }

  @Method(0x801071fcL)
  public static void FUN_801071fc(final AdditionOverlaysEffect44 a0, long a1, long a2) {
    final long a3 = _8011a014.offset(a2).getSigned();
    a0._32.set(1);

    a1 = a1 + 0x20L + 0xcL;

    //LAB_80107234
    a2 = a2 + 0x1L;
    for(; a2 < a0.count_30.get(); a2++) {
      MEMORY.ref(2, a1).offset(0x6L).setu(-0x1L);
      MEMORY.ref(2, a1).offset(0x4L).setu(-0x1L);
      MEMORY.ref(2, a1).offset(0x2L).setu(0);
      MEMORY.ref(2, a1).offset(0x0L).setu(0);
      _8011a014.offset(a2).setu(a3);
      a1 = a1 + 0x20L;
    }

    //LAB_80107264
  }

  @Method(0x8010726cL)
  public static void renderAdditionOverlaysEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final AdditionOverlaysEffect44 s2 = (AdditionOverlaysEffect44)data.effect_44;

    if(s2._31.get() != 0x1L) {
      if(data._10.flags_00 >= 0) {
        long s1 = s2._40.get();

        //LAB_801072c4
        int s0;
        for(s0 = 0; s0 < s2.count_30.get(); s0++) {
          renderAdditionBorderSquares(MEMORY.ref(1, s1).offset(0x2L).getSigned(), s0, s2, s1, state);
          s1 = s1 + 0x20L;
        }

        //LAB_801072f4
        s1 = s2._40.get();

        //LAB_8010730c
        for(s0 = 0; s0 < s2.count_30.get(); s0++) {
          if(_8011a014.offset(1, s0).getSigned() == 0) {
            break;
          }

          s1 = s1 + 0x20L;
        }

        //LAB_80107330
        if(s0 < s2.count_30.get()) {
          renderAdditionButton((byte)(MEMORY.ref(2, s1).offset(0x10L).getSigned() + (MEMORY.ref(2, s1).offset(0x12L).getSigned() - MEMORY.ref(2, s1).offset(0x10L).getSigned()) / 2 - s2._34.get() - 0x1L), MEMORY.ref(1, s1).offset(0x1cL).getSigned());

          final byte v1 = (byte)s2._34.get();
          if(v1 >= MEMORY.ref(2, s1).offset(0x10L).getSigned() && v1 <= MEMORY.ref(2, s1).offset(0x12L).getSigned()) {
            renderAdditionCentreSolidSquare(s2, s1, -2, state, data);
          }
        }
      }
    }

    //LAB_801073b4
  }

  @Method(0x801073d4L)
  public static void tickAdditionOverlaysEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    long v1;
    final long a0;
    long s0;
    long s1;
    long s2;
    long s4;
    final AdditionOverlaysEffect44 s3 = (AdditionOverlaysEffect44)data.effect_44;

    if(s3._31.get() == 0) {
      s4 = 0x1L;
      s2 = s3._40.get();
      s3._34.incr();

      //LAB_80107440
      for(s0 = 0; s0 < s3.count_30.get(); s0++) {
        if(s3._34.get() == MEMORY.ref(2, s2).offset(0x12L).getSigned() + 0x1L) {
          if(_8011a014.offset(s0).getSigned() == 0) {
            _8011a014.offset(s0).setu(-0x2L);
            FUN_801071fc(s3, s2, s0);

            //LAB_80107478
            if(_8011a014.offset(s0).getSigned() == 0) {
              s4 = 0;
            }
          }
        } else {
          if(_8011a014.offset(s0).getSigned() == 0) {
            s4 = 0;
          }
        }

        //LAB_8010748c
        s2 = s2 + 0x20L;
      }

      //LAB_801074a8
      if(s4 != 0) {
        FUN_801071fc(s3, s2, s0);
      }

      //LAB_801074bc
      s1 = 0;
      s2 = s3._40.get();

      //LAB_801074d0
      for(s0 = 0; s0 < s3.count_30.get(); s0++) {
        s1 = s1 + FUN_80107088(MEMORY.ref(1, s2).offset(0x2L).getSigned(), s0, s3, s2);
        s2 = s2 + 0x20L;
      }

      //LAB_80107500
      if(s1 == 0 && s3._32.get() != 0) {
        _80119f41.setu(0);

        //LAB_8010752c
        s2 = s3._40.get();
        for(s0 = 0; s0 < s3.count_30.get(); s0++) {
          free(MEMORY.ref(4, s2).offset(0x18L).get());
          s2 = s2 + 0x20L;
        }

        //LAB_80107554
        state.deallocateWithChildren();
      } else {
        //LAB_8010756c
        if(s3._34.get() >= 0x9L) {
          s2 = s3._40.get();

          //LAB_80107598
          for(s0 = 0; s0 < s3.count_30.get(); s0++) {
            if(_8011a014.offset(s0).getSigned() == 0) {
              break;
            }

            s2 = s2 + 0x20L;
          }

          //LAB_801075bc
          if(s0 < s3.count_30.get()) {
            if(state.storage_44[8] != 0) {
              MEMORY.ref(1, s2).offset(0x1cL).setu(0x1L);
              state.storage_44[8] = 0;
            }

            //LAB_801075e8
            if(s3._3a.get() > 0x2L) {
              v1 = s3._3a.get() & 0xffL;

              //LAB_8010763c
              if(v1 != 0x1 && v1 != 0x3L) {
                if(MEMORY.ref(1, s2).offset(0x1cL).getSigned() == 0) {
                  a0 = 0x20L;
                } else {
                  a0 = 0x40L;
                }

                //LAB_80107664
                v1 = joypadPress_8007a398.get();
                if((v1 & 0x60L) != 0) {
                  _8011a014.offset(1, s0).offset(0x0L).setu(-0x1L);
                  if((v1 & a0) == 0 || (v1 & ~a0) != 0) {
                    //LAB_801076d8
                    //LAB_801076dc
                    _8011a014.offset(s0).setu(-0x3L);
                  } else {
                    v1 = s3._34.get();

                    if(v1 >= MEMORY.ref(2, s2).offset(0x10L).getSigned() && v1 <= MEMORY.ref(2, s2).offset(0x12L).getSigned()) {
                      _8011a014.offset(1, s0).offset(0x0L).setu(0x1L);
                      MEMORY.ref(1, s2).offset(0x1L).setu(0x1L);
                    }
                  }

                  //LAB_801076f0
                  if(_8011a014.offset(s0).getSigned() < 0) {
                    FUN_801071fc(s3, s2, s0);
                  }

                  //LAB_80107718
                  //LAB_8010771c
                  s3._38.set(2);
                  s3._39.set((int)s0);
                  s3._3c.set(s2);
                }
              }
            } else {
              v1 = s3._34.get();

              if(v1 >= MEMORY.ref(2, s2).offset(0x10L).getSigned() && v1 <= MEMORY.ref(2, s2).offset(0x12L).getSigned()) {
                _8011a014.offset(s0).setu(0x1L);
                MEMORY.ref(1, s2).offset(0x1L).setu(0x1L);
                s3._38.set(2);
                s3._39.set((int)s0);
                s3._3c.set(s2);
              }
            }
          }

          //LAB_80107728
          if(s3._38.get() != 0) {
            s3._38.decr();
            renderAdditionCentreSolidSquare(s3, s3._3c.get(), _8011a014.offset(s3._39.get()).getSigned(), state, data);
          }
        }
      }
    }

    //LAB_80107764
  }

  @Method(0x80107790L)
  public static void deallocateAdditionOverlaysEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    free(((AdditionOverlaysEffect44)data.effect_44)._40.get());
  }

  @Method(0x801077bcL)
  public static FlowControl FUN_801077bc(final RunningScript<?> script) {
    script.params_20[2].set((int)_8011a014.offset(script.params_20[1].get()).getSigned());
    return FlowControl.CONTINUE;
  }

  @Method(0x801077e8L)
  public static FlowControl allocateAdditionOverlaysEffect(final RunningScript<? extends BattleScriptDataBase> script) {
    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0x44,
      SEffe::tickAdditionOverlaysEffect,
      SEffe::renderAdditionOverlaysEffect,
      SEffe::deallocateAdditionOverlaysEffect,
      AdditionOverlaysEffect44::new
    );

    FUN_801062a8(script.params_20[0].get(), script.params_20[1].get(), (AdditionOverlaysEffect44)state.innerStruct_00.effect_44, script.params_20[2].get());
    state.storage_44[8] = 0;
    script.params_20[4].set(state.index);
    _80119f41.setu(0x1L);
    return FlowControl.CONTINUE;
  }

  @Method(0x801078c0L)
  public static FlowControl FUN_801078c0(final RunningScript<?> script) {
    final EffectManagerData6c v0 = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    final AdditionOverlaysEffect44 a2 = (AdditionOverlaysEffect44)v0.effect_44;
    final int v1 = script.params_20[1].get();
    if(v1 != 1) {
      if(v1 < 2) {
        if(v1 == 0) {
          //LAB_80107924
          a2._31.set(a2._31.get() < 1 ? 1 : 0);
        }

        return FlowControl.CONTINUE;
      }

      //LAB_80107910
      if(v1 == 2) {
        //LAB_80107984
        //LAB_80107994
        a2._31.set(a2._31.get() < 1 ? 2 : 0);
      }

      return FlowControl.CONTINUE;
    }

    //LAB_80107930
    a2._32.set(v1);

    //LAB_80107954
    long ptr = a2._40.get() + 0x10L;
    for(int i = 0; i < a2.count_30.get(); i++) {
      MEMORY.ref(2, ptr).offset(0x0L).setu(0);
      MEMORY.ref(2, ptr).offset(0x2L).setu(0);
      _8011a014.offset(i).setu(-0x1L);
      ptr = ptr + 0x20L;
    }

    //LAB_80107998
    //LAB_8010799c
    return FlowControl.CONTINUE;
  }

  @Method(0x801079a4L)
  public static FlowControl FUN_801079a4(final RunningScript<?> script) {
    if(script.params_20[0].get() == 0) {
      script.params_20[1].set((int)_80119f41.getSigned());
    } else {
      //LAB_801079d0
      script.params_20[1].set((int)_80119f42.getSigned());
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
    final long func = getMethodAddress(Bttl_800d.class, "FUN_800d46d4", RunningScript.class);

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
      FUN_80018a5c((short)fp - 2, (short)s7 - 5, 232, 120, 255, 143, 0xcL, Translucency.B_PLUS_F, rgb, a0._11 * 256 + 6404, a0._11 * 256 + 4096);
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
    FUN_80018d60((short)x, (short)y, 128, 64, 16, 16, 51, Translucency.B_PLUS_F, rgb, 0x1000);

    rgb.set(0x80, 0x80, 0x80);

    //LAB_801080ac
    for(int i = 0; i < 5; i++) {
      if(a0._07 < i) {
        rgb.set(0x10, 0x10, 0x10);
      }

      //LAB_801080cc
      FUN_80018d60((short)_8011a01c.get() + (short)_800fb804.offset(2, i * 0x4L).getSigned(), (short)_8011a020.get() + (short)_800fb804.offset(2, i * 0x4L).offset(0x2L).getSigned(), (int)_800fb818.offset(1, i * 0x4L).getSigned(), (int)_800fb818.offset(1, i * 0x4L).offset(0x2L).getSigned(), (short)_800fb82c.offset(2, i * 0x4L).getSigned(), (short)_800fb82c.offset(2, i * 0x4L).offset(0x2L).getSigned(), 53 + i, Translucency.B_PLUS_F, rgb, 0x1000);
    }

    FUN_801079e8(a0);

    rgb.set(0x80, 0x80, 0x80);

    //LAB_801081a8
    for(int i = 0; i < a0._0d; i++) {
      FUN_80018d60((short)_8011a01c.get() + 18, (short)_8011a020.get() + 16, 224, 208, 31, 31, _800fb84c.offset(1, a0._18).get(), Translucency.B_PLUS_F, rgb, 0x1000);

      if(a0._18 == 9) {
        FUN_80018dec((int)_8011a01c.getSigned() + 23, (int)_8011a020.getSigned() + 21, 232, 120, 23, 23, 12, Translucency.B_PLUS_F, rgb, 0x800, 0x1800);
      }

      //LAB_80108250
    }

    //LAB_80108268
    FUN_80018d60((short)_8011a01c.get() + 32, (short)_8011a020.get() -  4, 152, 208,  8, 24, 50, Translucency.HALF_B_PLUS_HALF_F, rgb, 0x1000);
    FUN_80018d60((short)_8011a01c.get() + 18, (short)_8011a020.get() + 16, 224, 208, 31, 31, _800fb84c.offset(1, a0._18).getSigned(), Translucency.of((int)_800fb7fc.offset(1, a1 * 2).getSigned()), rgb, 0x1000);
    FUN_80018d60((short)_8011a01c.get() + 17, (short)_8011a020.get() + 14, 112, 200, 40, 40, 52, Translucency.of((int)_800fb7fc.offset(1, a1 * 2).offset(0x1L).getSigned()), rgb, 0x1000);
    FUN_80018d60((short)_8011a01c.get(),      (short)_8011a020.get(),      160, 192, 64, 48, _800fb840.offset(1, a0._18).getSigned(), null, rgb, 0x1000);
    FUN_80018d60((short)_8011a01c.get() +  8, (short)_8011a020.get() + 48, 200,  80, 42,  8, _800fb840.offset(1, a0._18).getSigned(), null, rgb, 0x1000);
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
        } else if((joypadPress_8007a398.get() >>> 4 & 0x2L) != 0 && data._13 != 2) {
          data._10 = 1;
          _80119f42.setu(1);
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
            _80119f42.setu(0);

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
          if(FUN_80108460(data, 0) != 0 && data._13 == 1 || (joypadPress_8007a398.get() >>> 4 & 0x2L) != 0 && data._13 == 0) {
            //LAB_8010870c
            data._11 = 4;
            data._0d = 0;

            final int v0 = FUN_80108460(data, 0);
            if(v0 != 0) {
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

    final ScriptState<DragoonAdditionScriptData1c> state = SCRIPTS.allocateScriptState(new DragoonAdditionScriptData1c());
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
    s1._18 = s4;

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
    _80119f42.setu(0);
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
        struct._02--;

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
          FUN_80018d60(struct._02 + s0 * 6, (short)_8011a020.get() + 16, (int)MEMORY.ref(1, fp).offset(0x0L).get(), (int)MEMORY.ref(1, s2).offset(0x0L).get(), 8, 16, 41, Translucency.B_PLUS_F, rgb, 0x1000);
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
      FUN_80018d60(struct._02, (short)_8011a020.get() + 16, (int)MEMORY.ref(1, fp).offset(0x0L).get(), (int)MEMORY.ref(1, s2).offset(0x0L).get(), 8, 16, 41, Translucency.B_PLUS_F, rgb, 0x1000);

      if((struct._01 & 0x1) != 0) {
        FUN_80018d60(struct._02, (short)_8011a020.get() + 16, (int)MEMORY.ref(1, fp).offset(0x0L).get(), (int)MEMORY.ref(1, s2).offset(0x0L).get(), 8, 16, 41, Translucency.B_PLUS_F, rgb, 0x1000);
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

    final ScriptState<EffeScriptData30> state = SCRIPTS.allocateScriptState(new EffeScriptData30());
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
    script.params_20[0].set(allocateEffectManager(script.scriptState_04, 0, null, null, null, null).index);
    return FlowControl.CONTINUE;
  }

  @Method(0x80108e40L)
  public static void FUN_80108e40(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final BttlScriptData6cSub08_3 s4 = (BttlScriptData6cSub08_3)data.effect_44;
    long s1 = s4.ptr_04.get();

    //LAB_80108e84
    for(long s3 = 0; s3 < s4.count_00.get(); s3++) {
      if(Math.abs(Math.abs(MEMORY.ref(2, s1).offset(0x4L).getSigned() + MEMORY.ref(2, s1).offset(0x2L).getSigned()) - Math.abs(MEMORY.ref(2, s1).offset(0x8L).getSigned() + MEMORY.ref(2, s1).offset(0x6L).getSigned())) > 180) {
        GPU.queueCommand(30, new GpuCommandLine()
          .translucent(Translucency.of(data._10.flags_00 >>> 28 & 3))
          .monochrome(0, 0)
          .rgb(1, data._10.colour_1c.getX(), data._10.colour_1c.getY(), data._10.colour_1c.getZ())
          .pos(0, (int)MEMORY.ref(2, s1).offset(0x06L).get() - 256, (int)MEMORY.ref(2, s1).offset(0x08L).get() - 128)
          .pos(1, (int)MEMORY.ref(2, s1).offset(0x02L).get() - 256, (int)MEMORY.ref(2, s1).offset(0x04L).get() - 128)
        );
      }

      //LAB_80108f6c
      s1 = s1 + 0xcL;
    }

    //LAB_80108f84
  }

  @Method(0x80109000L)
  public static void FUN_80109000(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final BttlScriptData6cSub08_3 s3 = (BttlScriptData6cSub08_3)data.effect_44;
    long v1 = s3.ptr_04.get();

    //LAB_80109038
    for(int i = 0; i < s3.count_00.get(); i++) {
      long sp10 = rsin(data._10.rot_10.getX()) << 5 >> 12;
      long sp12 = rcos(data._10.rot_10.getX()) << 5 >> 12;
      sp10 = sp10 * data._10.scale_16.getX() * MEMORY.ref(2, v1).offset(0xaL).getSigned() >> 24;
      sp12 = sp12 * data._10.scale_16.getX() * MEMORY.ref(2, v1).offset(0xaL).getSigned() >> 24;
      MEMORY.ref(2, v1).offset(0x6L).setu(MEMORY.ref(2, v1).offset(0x2L).get());
      MEMORY.ref(2, v1).offset(0x8L).setu(MEMORY.ref(2, v1).offset(0x4L).get());
      MEMORY.ref(2, v1).offset(0x2L).addu(sp10).and(0x1ff);
      MEMORY.ref(2, v1).offset(0x4L).addu(sp12).and(0xff);
      v1 = v1 + 0xcL;
    }

    //LAB_80109110
  }

  @Method(0x8010912cL)
  public static void FUN_8010912c(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    free(((BttlScriptData6cSub08_3)data.effect_44).ptr_04.get());
  }

  @Method(0x80109158L)
  public static FlowControl FUN_80109158(final RunningScript<? extends BattleScriptDataBase> script) {
    final int count = script.params_20[1].get();
    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0x8,
      SEffe::FUN_80109000,
      SEffe::FUN_80108e40,
      SEffe::FUN_8010912c,
      BttlScriptData6cSub08_3::new
    );

    final EffectManagerData6c s1 = state.innerStruct_00;
    final BttlScriptData6cSub08_3 s0 = (BttlScriptData6cSub08_3)s1.effect_44;
    long t1 = mallocTail(count * 0xcL);
    s0.count_00.set(count);
    s0.ptr_04.set(t1);
    s1._10.flags_00 = 0x5000_0000;

    //LAB_80109204
    for(int i = 0; i < count; i++) {
      MEMORY.ref(1, t1).offset(0x0L).setu(0x1L);
      MEMORY.ref(2, t1).offset(0x2L).setu(seed_800fa754.advance().get() % 513);
      MEMORY.ref(2, t1).offset(0x4L).setu(seed_800fa754.advance().get() % 257);
      MEMORY.ref(2, t1).offset(0xaL).setu(seed_800fa754.advance().get() % 3073 + 1024);
      t1 = t1 + 0xcL;
    }

    //LAB_80109328
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x80109358L)
  public static void FUN_80109358(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final int u = doubleBufferFrame_800bb108.get() == 0 ? 16 : 0;

    final ScreenDistortionEffectData08 sp48 = (ScreenDistortionEffectData08)data.effect_44;
    final int sp30 = data._10.scale_16.getX() >> 8;
    final int sp2c = data._10.scale_16.getY() >> 11;
    final int sp38 = data._10.scale_16.getZ() * 15 >> 9;

    //LAB_801093f0
    for(int s3 = 1; s3 >= -1; s3 -= 2) {
      final int angle = sp48.angle_00.get();
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
          final int vramY = doubleBufferFrame_800bb108.get() == 0 ? 0 : 256;

          GPU.queueCommand(30, new GpuCommandQuad()
            .bpp(Bpp.BITS_15)
            .translucent(Translucency.of(data._10.flags_00 >>> 28 & 3))
            .vramPos(0, vramY)
            .rgb(data._10.colour_1c.getX(), data._10.colour_1c.getY(), data._10.colour_1c.getZ())
            .pos(-160 - x, y, 256, 1)
            .uv(0, u + sp40)
          );

          GPU.queueCommand(29, new GpuCommandQuad()
            .bpp(Bpp.BITS_15)
            .translucent(Translucency.of(data._10.flags_00 >>> 28 & 3))
            .vramPos(256, vramY)
            .rgb(data._10.colour_1c.getX(), data._10.colour_1c.getY(), data._10.colour_1c.getZ())
            .pos(96 - x, y, 64, 1)
            .uv(0, u + sp40)
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
    final int y = doubleBufferFrame_800bb108.get() == 0 ? 0 : 256;
    final int v = doubleBufferFrame_800bb108.get() == 0 ? 16 : 0;

    GPU.queueCommand(30, new GpuCommandQuad()
      .bpp(Bpp.BITS_15)
      .translucent(Translucency.of(data._10.flags_00 >>> 28 & 3))
      .vramPos(0, y)
      .rgb(data._10.colour_1c.getX(), data._10.colour_1c.getY(), data._10.colour_1c.getZ())
      .pos(-160, -120, 256, 240)
      .uv(0, v)
    );

    GPU.queueCommand(30, new GpuCommandQuad()
      .bpp(Bpp.BITS_15)
      .translucent(Translucency.of(data._10.flags_00 >>> 28 & 3))
      .vramPos(256, y)
      .rgb(data._10.colour_1c.getX(), data._10.colour_1c.getY(), data._10.colour_1c.getZ())
      .pos(96, -120, 64, 240)
      .uv(0, v)
    );
  }

  @Method(0x80109a4cL)
  public static void FUN_80109a4c(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final ScreenDistortionEffectData08 effect = (ScreenDistortionEffectData08)data.effect_44;
    effect.angle_00.add(effect.angleStep_04.get());
  }

  @Method(0x80109a6cL)
  public static void tickScreenDistortionBlurEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    // no-op
  }

  @Method(0x80109a7cL)
  public static FlowControl allocateScreenDistortionEffect(final RunningScript<? extends BattleScriptDataBase> script) {
    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0x8,
      // Ticker and renderer are swapped for some reason
      screenDistortionEffectRenderers_80119fd4[script.params_20[2].get()],
      screenDistortionEffectTickers_80119fe0[script.params_20[2].get()],
      null,
      ScreenDistortionEffectData08::new
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    final ScreenDistortionEffectData08 effect = (ScreenDistortionEffectData08)manager.effect_44;
    effect.angle_00.set(0x800);
    effect.angleStep_04.set(script.params_20[1].get());
    manager._10.flags_00 = 0x4000_0000;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x80109b44L)
  public static void FUN_80109b44(final ScriptState<EffeScriptData18> state, final EffeScriptData18 data) {
    long v1;

    data.ticksRemaining_00--;

    if(data.ticksRemaining_00 < 0) {
      state.deallocateWithChildren();
      return;
    }

    //LAB_80109b7c
    long t1 = data.ptr_0c;

    //LAB_80109b90
    for(int i = 0; i < data.count_08; i++) {
      final long a1 = i * 0x10;
      final long p10 = data.ptr_10 + a1;
      final long p14 = data.ptr_14 + a1;
      MEMORY.ref(4, p14).offset(0x0L).setu(MEMORY.ref(4, p10).offset(0x0L).get());
      MEMORY.ref(4, p14).offset(0x4L).setu(MEMORY.ref(4, p10).offset(0x4L).get());
      MEMORY.ref(4, p14).offset(0x8L).setu(MEMORY.ref(4, p10).offset(0x8L).get());
      v1 = MEMORY.ref(4, p10).offset(0x0L).get();
      v1 = v1 + (v1 * data._04 >> 8);
      MEMORY.ref(4, p10).offset(0x0L).setu(v1);
      v1 = MEMORY.ref(4, p10).offset(0x4L).get();
      v1 = v1 + (v1 * data._04 >> 8);
      MEMORY.ref(4, p10).offset(0x4L).setu(v1);
      v1 = MEMORY.ref(4, p10).offset(0x8L).get();
      v1 = v1 + (v1 * data._04 >> 8);
      MEMORY.ref(4, p10).offset(0x8L).setu(v1);
      MEMORY.ref(2, t1).offset(0x0L).setu(MEMORY.ref(4, p14).offset(0x0L).get() >> 8);
      MEMORY.ref(2, t1).offset(0x2L).setu(MEMORY.ref(4, p14).offset(0x4L).get() >> 8);
      MEMORY.ref(2, t1).offset(0x4L).setu(MEMORY.ref(4, p14).offset(0x8L).get() >> 8);
      t1 = t1 + 0x8L;
    }

    //LAB_80109ce0
  }

  @Method(0x80109cf0L)
  public static void FUN_80109cf0(final ScriptState<EffeScriptData18> state, final EffeScriptData18 data) {
    free(data.ptr_10);
    free(data.ptr_14);
  }

  @Method(0x80109d30L)
  public static FlowControl FUN_80109d30(final RunningScript<?> script) {
    final int s5 = script.params_20[2].get();
    final int s4 = script.params_20[3].get();
    final ScriptState<EffeScriptData18> state = SCRIPTS.allocateScriptState(new EffeScriptData18());
    state.loadScriptFile(doNothingScript_8004f650);
    state.setTicker(SEffe::FUN_80109b44);
    state.setDestructor(SEffe::FUN_80109cf0);
    long v0 = ((MemoryRef)((EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00).effect_44).getAddress(); //TODO
    long v1 = MEMORY.ref(4, v0).offset(0x8L).get();
    v0 = ((MemoryRef)((EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00).effect_44).getAddress(); //TODO
    long s2_0 = MEMORY.ref(4, v1).offset(0x0L).get();
    final int count = (int)MEMORY.ref(4, v1).offset(0x4L).get();
    final EffeScriptData18 s3 = state.innerStruct_00;
    s3.ticksRemaining_00 = s5;
    s3._04 = s4;
    s3.count_08 = count;
    s3.ptr_0c = s2_0;
    s3.ptr_10 = mallocTail(count * 0x10);
    s3.ptr_14 = mallocTail(count * 0x10);
    v1 = MEMORY.ref(4, v0).offset(0x8L).get();
    final long s6 = MEMORY.ref(4, v1).offset(0x0L).get();
    _8011a030.setu(0x1L);

    //LAB_80109e78
    for(int i = 0; i < count; i++) {
      final long a0_0 = i * 0x10L;
      v1 = s3.ptr_14 + a0_0;
      MEMORY.ref(4, v1).offset(0x0L).setu(MEMORY.ref(2, s2_0).offset(0x0L).getSigned() * 0x100);
      MEMORY.ref(4, v1).offset(0x4L).setu(MEMORY.ref(2, s2_0).offset(0x2L).getSigned() * 0x100);
      MEMORY.ref(4, v1).offset(0x8L).setu(MEMORY.ref(2, s2_0).offset(0x4L).getSigned() * 0x100);
      s2_0 = s2_0 + 0x8L;
    }

    //LAB_80109ecc
    s2_0 = s6;

    //LAB_80109ee4
    for(int i = 0; i < s3.count_08; i++) {
      final int a0_0 = i * 0x10;
      final long v0_0 = s3.ptr_14 + a0_0;
      final long v0_1 = s3.ptr_10 + a0_0;
      MEMORY.ref(4, v0_1).offset(0x0L).setu((MEMORY.ref(2, s2_0).offset(0x0L).getSigned() * 0x100 - MEMORY.ref(4, v0_0).offset(0x0L).get()) / s5);
      MEMORY.ref(4, v0_1).offset(0x4L).setu((MEMORY.ref(2, s2_0).offset(0x2L).getSigned() * 0x100 - MEMORY.ref(4, v0_0).offset(0x4L).get()) / s5);
      MEMORY.ref(4, v0_1).offset(0x8L).setu((MEMORY.ref(2, s2_0).offset(0x4L).getSigned() * 0x100 - MEMORY.ref(4, v0_0).offset(0x8L).get()) / s5);
      s2_0 = s2_0 + 0x8L;
    }

    //LAB_80109f90
    return FlowControl.CONTINUE;
  }

  @Method(0x80109fc4L)
  public static void FUN_80109fc4(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final FrozenJetEffect28 effect = (FrozenJetEffect28)manager.effect_44;
    final int s3 = effect._18.get();
    int s1 = s3;
    final long sp78 = effect.vertexCount_00.get();

    //LAB_8010a020
    outer:
    while(true) {
      long s4 = effect._1a.get() + (s1 << 11);
      s1++;

      //LAB_8010a04c
      for(int s2 = 1; s2 < s3 - 1; s2++) {
        if(s1 >= sp78 - s3) {
          break outer;
        }

        final SVECTOR sp0x10 = new SVECTOR().set(effect.verticesCopy_1c.deref().get(s1));
        sp0x10.y.add((short)(rsin(s4) * (manager._10._28 << 10 >> 8) >> 12)); // 2b
        effect.vertices_0c.deref().get(s1).set(sp0x10);
        s4 = s4 + (0x1000 / s3 / manager._10._2c >> 8);
        s1++;
      }

      //LAB_8010a124
      s1++;
    }

    //LAB_8010a130
    if((effect._24.get() & 0x1) != 0) {
      long s0 = effect.primitives_14.get();

      //LAB_8010a15c
      for(s1 = 0; s1 < effect.primitiveCount_08.get(); s1++) {
        final int sp70 = (int)MEMORY.ref(2, s0).offset(0x24L).get();
        final int sp72 = (int)MEMORY.ref(2, s0).offset(0x26L).get();
        final int sp74 = (int)MEMORY.ref(2, s0).offset(0x28L).get();
        final int sp76 = (int)MEMORY.ref(2, s0).offset(0x2aL).get();
        final SVECTOR sp0x18 = new SVECTOR().set(effect.vertices_0c.deref().get(sp70));
        final SVECTOR sp0x20 = new SVECTOR().set(effect.vertices_0c.deref().get(sp72));
        final SVECTOR sp0x28 = new SVECTOR().set(effect.vertices_0c.deref().get(sp74));
        final SVECTOR sp0x30 = new SVECTOR().set(effect.vertices_0c.deref().get(sp76));
        final VECTOR sp0x40 = new VECTOR().set(sp0x20).sub(sp0x18);
        final VECTOR sp0x50 = new VECTOR().set(sp0x28).sub(sp0x18);
        CPU.CTC2(sp0x50.getX(), 0);
        CPU.CTC2(sp0x50.getY(), 2);
        CPU.CTC2(sp0x50.getZ(), 4);
        CPU.MTC2(sp0x40.getX(), 9);
        CPU.MTC2(sp0x40.getY(), 10);
        CPU.MTC2(sp0x40.getZ(), 11);
        CPU.COP2(0x178000cL);
        final VECTOR sp0x60 = new VECTOR().set((int)CPU.MFC2(25), (int)CPU.MFC2(26), (int)CPU.MFC2(27));
        final SVECTOR sp0x38 = new SVECTOR().set(sp0x60);

        effect.normals_10.deref().get(sp70).set(sp0x38);
        effect.normals_10.deref().get(sp72).set(sp0x38);
        effect.normals_10.deref().get(sp74).set(sp0x38);
        effect.normals_10.deref().get(sp76).set(sp0x38);

        s0 = s0 + 0x2cL;
      }
    }

    //LAB_8010a374
    effect._1a.add((short)(manager._10._24 << 7 >> 8));
  }

  @Method(0x8010a3bcL)
  public static void FUN_8010a3bc(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final FrozenJetEffect28 effect = (FrozenJetEffect28)manager.effect_44;
    free(effect.normalsCopy_20.getPointer());
    free(effect.verticesCopy_1c.getPointer());
  }

  @Method(0x8010a3fcL)
  public static FlowControl FUN_8010a3fc(final RunningScript<? extends BattleScriptDataBase> script) {
    final int s4 = script.params_20[2].get();
    final int sp18 = script.params_20[3].get();

    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0x28,
      SEffe::FUN_80109fc4,
      null,
      SEffe::FUN_8010a3bc,
      FrozenJetEffect28::new
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    final FrozenJetEffect28 effect = (FrozenJetEffect28)manager.effect_44;

    final GuardHealEffect14 v1 = (GuardHealEffect14)((EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00).effect_44;
    final TmdObjTable tmd = v1.tmd_08.deref();

    final long vertexCount = tmd.n_vert_04.get();
    final long normalCount = tmd.n_normal_0c.get();

    final UnboundedArrayRef<SVECTOR> vertices = tmd.vert_top_00.deref();
    final long normals = tmd.normal_top_08.get();

    effect.vertexCount_00.set(vertexCount);
    effect.normalCount_04.set(normalCount);
    effect.primitiveCount_08.set(tmd.n_primitive_14.get());
    effect.vertices_0c.set(vertices);
    effect.normals_10.setPointer(normals);
    effect.primitives_14.set(tmd.primitives_10.getPointer());
    effect._18.set(s4);
    effect.verticesCopy_1c.setPointer(mallocTail(vertexCount * 8));
    effect.normalsCopy_20.setPointer(mallocTail(normalCount * 8));
    effect._24.set(sp18 & 0xff);
    manager._10._24 = 0x100;
    manager._10._28 = 0x100;
    manager._10._2c = 0x100;

    //LAB_8010a538
    for(int t0 = 0; t0 < vertexCount; t0++) {
      //LAB_8010a54c
      effect.verticesCopy_1c.deref().get(t0).set(vertices.get(t0));
    }

    //LAB_8010a578
    //LAB_8010a588
    for(int t0 = 0; t0 < normalCount; t0++) {
      //LAB_8010a59c
      effect.normalsCopy_20.deref().get(t0).set((SVECTOR)MEMORY.ref(4, normals).offset(t0 * 0x8L).cast(SVECTOR::new));
    }

    //LAB_8010a5c8
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x8010a610L)
  public static FlowControl FUN_8010a610(final RunningScript<? extends BattleScriptDataBase> script) {
    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0x24,
      SEffe::FUN_8010ae40,
      SEffe::FUN_8010af6c,
      SEffe::FUN_8010b00c,
      BttlScriptData6cSub24::new
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    final BttlScriptData6cSub24 effect = (BttlScriptData6cSub24)manager.effect_44;
    effect.count_04.set(script.params_20[1].get());
    effect._08.set(script.params_20[2].get());
    effect._0c.set(script.params_20[3].get());
    effect._10.set(script.params_20[4].get());
    effect._14.set(script.params_20[5].get());
    effect._18.set(script.params_20[6].get());
    effect._1c.set(script.params_20[7].get());
    effect._20.set((int)(CPU.CFC2(26) >> 2));

    long s2 = mallocTail(effect.count_04.get() * 4);
    effect.ptr_00.set(s2);

    //LAB_8010a754
    for(int i = 0; i < effect.count_04.get(); i++) {
      //LAB_8010a770
      MEMORY.ref(2, s2).offset(0x0L).setu(rand() % 0x1000);

      final int v0;
      if((effect._18.get() & 0x2L) == 0) {
        //LAB_8010a7a8
        v0 = rand() % 0x80;
      } else {
        //LAB_8010a7b4
        //LAB_8010a7d0
        v0 = rand() % 0x10;
      }

      //LAB_8010a7d4
      MEMORY.ref(2, s2).offset(0x2L).setu(v0);
      if((effect._18.get() & 0x1L) != 0) {
        MEMORY.ref(2, s2).offset(0x2L).addu(0x70L);
      }

      //LAB_8010a800
      s2 = s2 + 0x4L;
    }

    //LAB_8010a818
    script.params_20[0].set(state.index);
    manager._10.flags_00 |= 0x5400_0000;
    return FlowControl.CONTINUE;
  }

  /** Used in Rose transform */
  @Method(0x8010a860L)
  public static void FUN_8010a860(final EffectManagerData6c manager, final long a1) {
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

    final BttlScriptData6cSub24 effect = (BttlScriptData6cSub24)manager.effect_44;

    //LAB_8010a968
    if((effect._18.get() & 0x4L) == 0) {
      if(effect._10.get() * 2 < MEMORY.ref(2, a1).offset(0x2L).getSigned() * effect._08.get()) {
        sp0x40.setY((short)-effect._10.get());
        sp0x48.setY((short)-effect._10.get());
        sp0x50.setY((short)(-effect._10.get() * 2));
      } else {
        //LAB_8010a9ec
        sp0x40.setY((short)(MEMORY.ref(2, a1).offset(0x2L).getSigned() * -effect._08.get() / 2));
        sp0x48.setY((short)(MEMORY.ref(2, a1).offset(0x2L).getSigned() * -effect._08.get() / 2));
        sp0x50.setY((short)(MEMORY.ref(2, a1).offset(0x2L).getSigned() * -effect._08.get()));
      }

      //LAB_8010aa34
      sp0x40.setZ((short)effect._0c.get());
      sp0x48.setZ((short)-effect._0c.get());
    }

    //LAB_8010aa54
    final VECTOR sp0x28 = new VECTOR().set(0, (int)(MEMORY.ref(2, a1).offset(0x2L).getSigned() * effect._08.get()), 0);
    final SVECTOR sp0x78 = new SVECTOR().set((short)MEMORY.ref(2, a1).offset(0x0L).get(), (short)0, (short)0);
    RotMatrix_8003faf0(sp0x78, sp0xa0);
    TransMatrix(sp0x80, sp0x28);
    MulMatrix0(sp0xa0, sp0x80, sp0xc0);
    FUN_800e8594(sp0x80, manager);

    if((manager._10.flags_00 & 0x400_0000) == 0) {
      MulMatrix0(worldToScreenMatrix_800c3548, sp0x80, sp0xa0);
      RotMatrix_8003faf0(manager._10.rot_10, sp0xa0);
      MulMatrix0(sp0xa0, sp0xc0, sp0xc0);
      setRotTransMatrix(sp0xc0);
    } else {
      //LAB_8010ab10
      MulMatrix0(sp0x80, sp0xc0, sp0xa0);
      MulMatrix0(worldToScreenMatrix_800c3548, sp0xa0, sp0x80);
      setRotTransMatrix(sp0x80);
    }

    //LAB_8010ab34
    final int z = RotTransPers4(sp0x38, sp0x40, sp0x48, sp0x50, xy0, xy1, xy2, xy3, null, null);
    if(z >= effect._20.get()) {
      final GpuCommandPoly cmd = new GpuCommandPoly(4)
        .translucent(Translucency.B_PLUS_F);

      if(effect._1c.get() == 1) {
        //LAB_8010abf4
        final int v0 = (0x80 - (short)MEMORY.ref(2, a1).offset(0x2L).getSigned()) * manager._10.colour_1c.getX() / 0x80;
        final int v1 = (short)v0 / 2;

        cmd
          .monochrome(0, 0)
          .monochrome(1, 0)
          .rgb(2, v0, v1, v1)
          .rgb(3, v0, v1, v1);
      } else if(effect._1c.get() == 2) {
        //LAB_8010ac68
        final short s3 = (short)(FUN_8010b058((short)MEMORY.ref(2, a1).offset(0x2L).getSigned()) * manager._10.colour_1c.getX() * 8 / 0x80);
        final short s2 = (short)(FUN_8010b0dc((short)MEMORY.ref(2, a1).offset(0x2L).getSigned()) * manager._10.colour_1c.getY() * 8 / 0x80);
        final short a2 = (short)(FUN_8010b160((short)MEMORY.ref(2, a1).offset(0x2L).getSigned()) * manager._10.colour_1c.getZ() * 8 / 0x80);

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
  public static void FUN_8010ae40(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final BttlScriptData6cSub24 a2 = (BttlScriptData6cSub24)data.effect_44;

    //LAB_8010ae80
    for(int i = 0; i < a2.count_04.get(); i++) {
      final long a1 = a2.ptr_00.get() + i * 0x4L;

      if((a2._18.get() & 0x1L) == 0) {
        //LAB_8010aee8
        MEMORY.ref(2, a1).offset(0x2L).addu(a2._14.get());

        if((a2._18.get() & 0x2L) != 0 && (short)MEMORY.ref(2, a1).offset(0x2L).get() >= 0x80) {
          MEMORY.ref(2, a1).offset(0x2L).setu(0x80L);
        } else {
          //LAB_8010af28
          //LAB_8010af3c
          MEMORY.ref(2, a1).offset(0x2L).modu(0x80);
        }
      } else {
        MEMORY.ref(2, a1).offset(0x2L).subu(a2._14.get());

        if((a2._18.get() & 0x2L) != 0 && (short)MEMORY.ref(2, a1).offset(0x2L).get() <= 0) {
          MEMORY.ref(2, a1).offset(0x2L).setu(0);
        } else {
          //LAB_8010aecc
          MEMORY.ref(2, a1).offset(0x2L).addu(0x80).modu(0x80);
        }
      }

      //LAB_8010af4c
    }

    //LAB_8010af64
  }

  @Method(0x8010af6cL)
  public static void FUN_8010af6c(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    if(manager._10.flags_00 >= 0) {
      final BttlScriptData6cSub24 s2 = (BttlScriptData6cSub24)manager.effect_44;

      //LAB_8010afcc
      for(int i = 0; i < s2.count_04.get(); i++) {
        FUN_8010a860(manager, s2.ptr_00.get() + i * 0x4L);
      }
    }

    //LAB_8010aff0
  }

  @Method(0x8010b00cL)
  public static void FUN_8010b00c(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    free(((BttlScriptData6cSub24)manager.effect_44).ptr_00.get());
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

  @Method(0x8010b1d8L)
  public static FlowControl allocateDeathDimensionEffect(final RunningScript<? extends BattleScriptDataBase> script) {
    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0x1c,
      null,
      SEffe::renderDeathDimensionEffect,
      SEffe::deallocateDeathDimensionEffect,
      DeathDimensionEffect1c::new
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    final DeathDimensionEffect1c effect = (DeathDimensionEffect1c)manager.effect_44;
    effect.ptr_00.set(mallocTail(0x8));
    effect._04.set(script.params_20[4].get());
    effect._08.set(script.params_20[5].get());
    effect._0c.set(script.params_20[6].get());
    effect._10.set(0);
    script.params_20[0].set(state.index);
    FUN_8010c2e0(effect.ptr_00.get(), script.params_20[1].get());

    final int v0 = effect._0c.get();
    if(v0 == 0) {
      //LAB_8010b2e4
      final int x = DISPENV_800c34b0.disp.x.get() + script.params_20[2].get() + 160;
      final int y = DISPENV_800c34b0.disp.y.get() + script.params_20[3].get() + 120;
      final int w = effect._04.get() / 2;
      final int h = effect._08.get() / 2;

      //LAB_8010b308
      for(int i = 0; i < 4; i++) {
        final long v1 = effect.ptr_00.get();

        GPU.queueCommand(40, new GpuCommandCopyVramToVram(x + ((i & 1) - 1) * w, y + (i / 2 - 1) * h, (int)MEMORY.ref(2, v1).offset(0x0L).get(), (int)MEMORY.ref(2, v1).offset(0x2L).get() + i * 64, w, h));
        GPU.queueCommand(40, new GpuCommandSetMaskBit(true, Gpu.DRAW_PIXELS.ALWAYS));
      }
    } else if(v0 < 3) {
      //LAB_8010b3f0
      final int x = DISPENV_800c34b0.disp.x.get() + script.params_20[2].get() + 160 - effect._04.get() / 2;
      final int y = DISPENV_800c34b0.disp.y.get() + script.params_20[3].get() + 120 - effect._08.get() / 2;
      final int w = effect._04.get() / 5;
      final int h = effect._08.get() / 3;

      //LAB_8010b468
      for(int i = 0; i < 15; i++) {
        final long v1 = effect.ptr_00.get();

        GPU.queueCommand(40, new GpuCommandCopyVramToVram(x + i % 5 * w, y + i / 5 * h, (int)MEMORY.ref(2, v1).offset(0x0L).get() + i % 2 * 32, (int)MEMORY.ref(2, v1).offset(0x2L).get() + i / 2 * 32, w, h));
        GPU.queueCommand(40, new GpuCommandSetMaskBit(true, Gpu.DRAW_PIXELS.ALWAYS));
      }
    }

    //LAB_8010b548
    manager._10.flags_00 |= 0x5000_0000;
    return FlowControl.CONTINUE;
  }

  @Method(0x8010b594L)
  public static void FUN_8010b594(final EffectManagerData6c manager, final DeathDimensionEffect1c effect) {
    int v0;
    int v1;
    int a0;
    int a1;

    final COLOUR sp0x48 = new COLOUR();

    if((manager._10.flags_00 & 0x40) != 0) {
      final VECTOR sp0x70 = new VECTOR();
      RotTrans(_800fb8d0, sp0x70, null);
      FUN_80040df0(sp0x70, _800fb8cc, sp0x48);
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
            v0 = (a0 - 2) * effect._10.get() / 4;
            vert1.setZ((short)v0);
            v0 = (a0 - 1) * effect._10.get() / 4;
            vert0.setZ((short)v0);
            vert2.setZ((short)v0);
            a0 = i >> 2;
            v0 = (a0 - 1) * effect._14.get() / 2;
            vert0.setY((short)v0);
            v0 = a0 * effect._14.get() / 2;
            vert1.setY((short)v0);
            vert2.setY((short)v0);
            a0 = (i >> 1) * 64;
            v1 = (i & 0x1) * 32;

            cmd
              .uv(0, a0, v1 + effect._04.get() / 4 - 1)
              .uv(1, v1, a0 + effect._08.get() / 2 - 1)
              .uv(2, v1 + effect._04.get() / 4 - 1, a0 + effect._08.get() / 2 - 1);
          } else {
            //LAB_8010b8c8
            a0 = i & 0x3;
            v0 = (a0 - 2) * effect._10.get() / 4;
            vert1.setZ((short)v0);
            vert0.setZ((short)v0);
            v0 = (a0 - 1) * effect._10.get() / 4;
            vert2.setZ((short)v0);
            a0 = i >> 2;
            v0 = (a0 - 1) * effect._14.get() / 2;
            vert0.setY((short)v0);
            v1 = (i & 1) * 32;
            a0 = (i >> 1) * 64;
            v0 = a0 * effect._14.get() / 2;
            vert2.setY((short)v0);
            vert1.setY((short)v0);

            cmd
              .uv(0, v1, a0)
              .uv(1, v1, a0 + effect._08.get() / 2 - 1)
              .uv(2, v1 + effect._04.get() / 4 - 1, a0 + effect._08.get() / 2 - 1);
          }

          //LAB_8010b9a4
          final int z = perspectiveTransformTriple(vert0, vert1, vert2, sxy0, sxy1, sxy2, null, null);

          if(effect._10.get() == 0) {
            //LAB_8010b638
            final int sp8c = (int)CPU.CFC2(26);
            a1 = (z << 12) * 4;
            effect._10.set(effect._04.get() * a1 / sp8c >>> 12);
            effect._14.set(effect._08.get() * a1 / sp8c >>> 12);
            break;
          }

          final long addr = effect.ptr_00.get();

          cmd
            .bpp(Bpp.BITS_15)
            .vramPos((int)MEMORY.ref(2, addr).offset(0x0L).get() & 0x3c0, (MEMORY.ref(1, addr).offset(0x3L).get() & 0x1) == 0 ? 0 : 256)
            .pos(0, sxy0.getX(), sxy0.getY())
            .pos(0, sxy1.getX(), sxy1.getY())
            .pos(0, sxy2.getX(), sxy2.getY());

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
          v0 = (a0 - 2) * effect._10.get() / 4;
          vert2.setZ((short)v0);
          vert0.setZ((short)v0);
          v0 = (a1 - 1) * effect._14.get() / 2;
          vert1.setY((short)v0);
          vert0.setY((short)v0);
          v0 = (a0 - 1) * effect._10.get() / 4;
          vert3.setZ((short)v0);
          vert1.setZ((short)v0);
          v0 = a1 * effect._14.get() / 2;
          vert3.setY((short)v0);
          vert2.setY((short)v0);
          final int z = RotTransPers4(vert0, vert1, vert2, vert3, sxy0, sxy1, sxy2, sxy3, null, null);

          if(effect._10.get() == 0) {
            //LAB_8010b664
            final int sp90 = (int)CPU.CFC2(26);
            a1 = (z << 12) * 4;

            //LAB_8010b688
            effect._10.set(effect._04.get() * a1 / sp90 >>> 12);
            effect._14.set(effect._08.get() * a1 / sp90 >>> 12);
            break;
          }

          final int u = (i & 0x1) * 32;
          final int v = (i >> 1) * 64;
          final long addr = effect.ptr_00.get();

          GPU.queueCommand(z >> 2, new GpuCommandPoly(4)
            .bpp(Bpp.BITS_15)
            .vramPos((int)MEMORY.ref(2, addr).offset(0x0L).get() & 0x3c0, (MEMORY.ref(1, addr).offset(0x3L).get() & 0x1) != 0 ? 256 : 0)
            .rgb(sp0x48.getR(), sp0x48.getG(), sp0x48.getB())
            .pos(0, sxy0.getX(), sxy0.getY())
            .pos(1, sxy1.getX(), sxy1.getY())
            .pos(2, sxy2.getX(), sxy2.getY())
            .pos(3, sxy3.getX(), sxy3.getY())
            .uv(0, u, v)
            .uv(1, u + effect._04.get() / 4 - 1, v)
            .uv(2, u, v + effect._08.get() / 2 - 1)
            .uv(3, u + effect._04.get() / 4 - 1, v + effect._08.get() / 2 - 1)
          );
        }
      }
    }

    //LAB_8010bc40
  }

  @Method(0x8010bc60L)
  public static void FUN_8010bc60(final EffectManagerData6c manager, final DeathDimensionEffect1c effect) {
    final COLOUR rgb = new COLOUR();

    if((manager._10.flags_00 & 0x40) != 0) {
      final VECTOR sp0x70 = new VECTOR();
      RotTrans(_800fb8d0, sp0x70, null);
      FUN_80040df0(sp0x70, _800fb8cc, rgb);
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

      int a1 = s0 / 5;
      final int a0 = s0 % 5;
      int v1 = effect._10.get();
      int v0 = a0 * v1 / 5 - v1 / 2;
      sp0x28.setZ((short)v0);
      sp0x38.setZ((short)v0);
      v1 = effect._14.get();
      v0 = a1 * v1 / 3 - v1 / 2;
      sp0x28.setY((short)v0);
      sp0x30.setY((short)v0);
      v1 = effect._10.get();
      v0 = (a0 + 1) * v1 / 5 - v1 / 2;
      sp0x30.setZ((short)v0);
      sp0x40.setZ((short)v0);
      v1 = effect._14.get();
      v0 = (a1 + 1) * v1 / 3 - v1 / 2;
      sp0x38.setY((short)v0);
      sp0x40.setY((short)v0);

      final SVECTOR sxy0 = new SVECTOR();
      final SVECTOR sxy1 = new SVECTOR();
      final SVECTOR sxy2 = new SVECTOR();
      final SVECTOR sxy3 = new SVECTOR();
      final int z = RotTransPers4(sp0x28, sp0x30, sp0x38, sp0x40, sxy0, sxy1, sxy2, sxy3, null, null);

      if(effect._10.get() == 0) {
        //LAB_8010bd08
        final int sp8c = (int)CPU.CFC2(26);
        a1 = (z << 12) * 4;
        effect._10.set(effect._04.get() * a1 / sp8c >>> 12);
        effect._14.set(sp8c / (effect._08.get() * a1) >>> 12);
        break;
      }

      final int leftU = s0 % 2 * 32;
      final int topV = s0 / 2 * 32;
      final int rightU = leftU + effect._04.get() / 5 - 1;
      final int bottomV = topV + effect._08.get() / 3 - 1;

      final long addr = effect.ptr_00.get();

      GPU.queueCommand(z >> 2, new GpuCommandPoly(4)
        .bpp(Bpp.BITS_15)
        .vramPos((int)MEMORY.ref(2, addr).offset(0x0L).get() & 0x3c0, (MEMORY.ref(1, addr).offset(0x3L).get() & 0x1) != 0 ? 256 : 0)
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
  public static void renderDeathDimensionEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final MATRIX sp0x10 = new MATRIX().set(identityMatrix_800c3568);
    final MATRIX sp0x30 = new MATRIX().set(identityMatrix_800c3568);

    if(manager._10.flags_00 >= 0) {
      final DeathDimensionEffect1c effect = (DeathDimensionEffect1c)manager.effect_44;
      FUN_800e8594(sp0x10, manager);
      MulMatrix0(worldToScreenMatrix_800c3548, sp0x10, sp0x30);
      CPU.CTC2(sp0x30.getPacked(0), 0);
      CPU.CTC2(sp0x30.getPacked(2), 1);
      CPU.CTC2(sp0x30.getPacked(4), 2);
      CPU.CTC2(sp0x30.getPacked(6), 3);
      CPU.CTC2(sp0x30.getPacked(8), 4);
      CPU.CTC2(sp0x30.transfer.getX(), 5);
      CPU.CTC2(sp0x30.transfer.getY(), 6);
      CPU.CTC2(sp0x30.transfer.getZ(), 7);
      deathDimensionRenderers_80119fec.get(effect._0c.get()).deref().run(manager, effect);
    }

    //LAB_8010c278
  }

  @Method(0x8010c294L)
  public static void deallocateDeathDimensionEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    free(((DeathDimensionEffect1c)manager.effect_44).ptr_00.get());
  }

  @Method(0x8010c2e0L)
  public static void FUN_8010c2e0(final long a0, final int a1) {
    if((a1 & 0xf_ff00) != 0xf_ff00) {
      final DeffPart.SpriteType spriteType = (DeffPart.SpriteType)getDeffPart(a1 | 0x400_0000);
      final DeffPart.SpriteMetrics deffMetrics = spriteType.metrics_08.deref();
      MEMORY.ref(2, a0).offset(0x0L).setu(deffMetrics.u_00.get());
      MEMORY.ref(2, a0).offset(0x2L).setu(deffMetrics.v_02.get());
      MEMORY.ref(1, a0).offset(0x4L).setu(deffMetrics.w_04.get() * 4);
      MEMORY.ref(1, a0).offset(0x5L).setu(deffMetrics.h_06.get());
      MEMORY.ref(2, a0).offset(0x6L).setu(deffMetrics.clutY_0a.get() << 6 | (deffMetrics.clutX_08.get() & 0x3f0) >>> 4);
    }

    //LAB_8010c368
  }

  @Method(0x8010c378L)
  public static FlowControl FUN_8010c378(final RunningScript<? extends BattleScriptDataBase> script) {
    final int sp18 = script.params_20[1].get();
    final int sp1c = script.params_20[1].get();
    final int sp20 = script.params_20[3].get();
    final int sp24 = script.params_20[4].get();
    _8011a034.get(0).set(script.params_20[5].get());
    _8011a034.get(1).set(script.params_20[6].get());
    _8011a034.get(2).set(script.params_20[7].get());
    _8011a034.get(3).set(script.params_20[8].get());
    _8011a034.get(4).set(script.params_20[9].get());

    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0x50,
      SEffe::FUN_8010c69c,
      SEffe::FUN_8010c8f8,
      SEffe::FUN_8010f94c,
      BttlScriptData6cSub50::new
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    final BttlScriptData6cSub50 effect = (BttlScriptData6cSub50)manager.effect_44;
    effect._00.set(5);
    effect._02.set(0);
    effect._38.setPointer(mallocTail(0x12cL));

    //LAB_8010c4a4
    for(int i = 0; i < 5; i++) {
      final BttlScriptData6cSub50Sub3c s0 = effect._38.deref().get(i);
      s0._03.set(1);
      s0.x_04.set((short)0);
      s0.y_06.set((short)0);
      s0._08.set(0);
      s0._0c.set(0);
      s0._0e.set(0);
      s0._10.set(0);
      s0._14.set(0);
      s0._16.set(0);
      s0._18.set(0);
      s0._28.set(0);
      s0._2e.set((short)0x1600);
      s0._30.set((short)0x1600);
      s0._32.set(0);
      s0._34.set(0);

      final int a1 = _8011a034.get(i).get();
      if(a1 == -1) {
        s0._02.set(0);
      } else {
        //LAB_8010c500
        s0._02.set(1);

        if((a1 & 0xf_ff00) == 0xf_ff00) {
          final SpriteMetrics08 metrics = spriteMetrics_800c6948[a1 & 0xff];
          effect.u_04.get(i).set(metrics.u_00.get());
          effect.v_0e.get(i).set(metrics.v_02.get());
          effect.w_18.get(i).set(metrics.w_04.get());
          effect.h_22.get(i).set(metrics.h_05.get());
          effect.clut_2c.get(i).set(metrics.clut_06.get());
        } else {
          //LAB_8010c5a8
          final DeffPart.SpriteType spriteType = (DeffPart.SpriteType)getDeffPart(a1 | 0x400_0000);
          final DeffPart.SpriteMetrics deffMetrics = spriteType.metrics_08.deref();
          effect.u_04.get(i).set(deffMetrics.u_00.get());
          effect.v_0e.get(i).set(deffMetrics.v_02.get());
          effect.w_18.get(i).set(deffMetrics.w_04.get() * 4);
          effect.h_22.get(i).set(deffMetrics.h_06.get());
          effect.clut_2c.get(i).set(GetClut(deffMetrics.clutX_08.get(), deffMetrics.clutY_0a.get()));
        }
      }

      //LAB_8010c608
    }

    effect.bobjIndex_3c.set(sp18);
    effect._40.set((short)sp1c);
    effect._42.set((short)sp20);
    effect._44.set((short)sp24);
    effect._48.set((short)0);
    manager._10.flags_00 |= 0x5000_0000;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x8010c69cL)
  public static void FUN_8010c69c(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final BttlScriptData6cSub50 effect = (BttlScriptData6cSub50)manager.effect_44;
    effect._4a.set((short)(rand() % 30));

    if(effect._4a.get() != 0) {
      final DVECTOR screenCoords = perspectiveTransformXyz(((BattleObject27c)scriptStatePtrArr_800bc1c0[effect.bobjIndex_3c.get()].innerStruct_00).model_148, effect._40.get(), effect._42.get(), effect._44.get());
      final int t4 = (int)-(screenCoords.getX() * 2.5f);
      final int t3 = (int)-(screenCoords.getY() * 2.5f);

      //LAB_8010c7c0
      for(int i = 0; i < 5; i++) {
        final BttlScriptData6cSub50Sub3c s2 = effect._38.deref().get(i);
        final int dispW = displayWidth_1f8003e0.get();
        final int dispH = displayHeight_1f8003e4.get();
        s2.x_04.set((short)(screenCoords.getX() + dispW / 2));
        s2.y_06.set((short)(screenCoords.getY() + dispH / 2));

        if(s2.x_04.get() > 0 && s2.x_04.get() < dispW && s2.y_06.get() > 0 && s2.y_06.get() < dispH) {
          s2._03.set(1);

          final int scale = (int)_800fb8fc.offset(i * 0x4L).get();
          s2.x_04.add((short)(t4 * scale >> 8));
          s2.y_06.add((short)(t3 * scale >> 8));
        } else {
          //LAB_8010c870
          s2._03.set(0);
        }

        //LAB_8010c874
      }

      int a0 = Math.abs(screenCoords.getX());
      final int v1 = displayWidth_1f8003e0.get() / 2;
      if(v1 < a0) {
        a0 = v1;
      }

      //LAB_8010c8b8
      effect._48.set((short)(0xff - (0xff00 / v1 * a0 >> 8)));
    }

    //LAB_8010c8e0
  }

  /** Used in Shana transform */
  @Method(0x8010c8f8L)
  public static void FUN_8010c8f8(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final BttlScriptData6cSub50 s5 = (BttlScriptData6cSub50)manager.effect_44;

    if(s5._4a.get() != 0) {
      s5._02.incr();
      final int sp10 = manager._10.flags_00;

      //LAB_8010c9fc
      for(int s6 = 0; s6 < 5; s6++) {
        final BttlScriptData6cSub50Sub3c s0 = s5._38.deref().get(s6);

        if(s0._03.get() != 0 && s0._02.get() == 1) {
          final int sp14 = -s5.w_18.get(s6).get() / 2;
          final int sp16 = -s5.h_22.get(s6).get() / 2;
          final int w = s5.w_18.get(s6).get();
          final int h = s5.h_22.get(s6).get();
          final int tpage = (s5.v_0e.get(s6).get() & 0x100) >>> 4 | (s5.u_04.get(s6).get() & 0x3ff) >>> 6;
          final int u = (s5.u_04.get(s6).get() & 0x3f) << 2;
          final int v = s5.v_0e.get(s6).get() & 0xff;
          final int clutX = s5.clut_2c.get(s6).get() << 4 & 0x3ff;
          final int clutY = s5.clut_2c.get(s6).get() >>> 6 & 0x1ff;
          final int r = manager._10.colour_1c.getX() * s5._48.get() >> 8;
          final int g = manager._10.colour_1c.getY() * s5._48.get() >> 8;
          final int b = manager._10.colour_1c.getZ() * s5._48.get() >> 8;

          if(s6 == 0) {
            //LAB_8010cb38
            for(int s3 = 0; s3 < 4; s3++) {
              final int x = (s0._2e.get() * s5.w_18.get(s6).get() >> 12) * (int)_800fb910.offset(s3 * 0x8L).offset(0x0L).get();
              final int y = (s0._30.get() * s5.h_22.get(s6).get() >> 12) * (int)_800fb910.offset(s3 * 0x8L).offset(0x4L).get();
              final int halfW = displayWidth_1f8003e0.get() / 2;
              final int halfH = displayHeight_1f8003e4.get() / 2;
              final int[] sp0x48 = new int[8];
              sp0x48[0] = s0.x_04.get() - halfW + x;
              sp0x48[1] = s0.y_06.get() - halfH + y;
              sp0x48[2] = s0.x_04.get() - halfW + x + (s5.w_18.get(s6).get() * s0._2e.get() >> 12);
              sp0x48[3] = s0.y_06.get() - halfH + y;
              sp0x48[4] = s0.x_04.get() - halfW + x;
              sp0x48[5] = s0.y_06.get() - halfH + y + (s5.h_22.get(s6).get() * s0._30.get() >> 12);
              sp0x48[6] = s0.x_04.get() - halfW + x + (s5.w_18.get(s6).get() * s0._2e.get() >> 12);
              sp0x48[7] = s0.y_06.get() - halfH + y + (s5.h_22.get(s6).get() * s0._30.get() >> 12);

              final GpuCommandPoly cmd = new GpuCommandPoly(4)
                .bpp(Bpp.BITS_4)
                .clut(clutX, clutY)
                .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
                .rgb(r, g, b)
                .pos(0, sp0x48[(int)_800fb930.offset(s3 * 0x4L).offset(0x0L).get() * 2], sp0x48[(int)_800fb930.offset(s3 * 0x4L).offset(0x0L).get() * 2 + 1])
                .pos(1, sp0x48[(int)_800fb930.offset(s3 * 0x4L).offset(0x1L).get() * 2], sp0x48[(int)_800fb930.offset(s3 * 0x4L).offset(0x1L).get() * 2 + 1])
                .pos(2, sp0x48[(int)_800fb930.offset(s3 * 0x4L).offset(0x2L).get() * 2], sp0x48[(int)_800fb930.offset(s3 * 0x4L).offset(0x2L).get() * 2 + 1])
                .pos(3, sp0x48[(int)_800fb930.offset(s3 * 0x4L).offset(0x3L).get() * 2], sp0x48[(int)_800fb930.offset(s3 * 0x4L).offset(0x3L).get() * 2 + 1])
                .uv(0, u, v)
                .uv(1, u + w, v)
                .uv(2, u, v + h)
                .uv(3, u + w, v + h);

              if((sp10 >>> 30 & 1) != 0) {
                cmd.translucent(Translucency.of(sp10 >>> 28 & 0b11));
              }

              GPU.queueCommand(30, cmd);
            }
          } else {
            //LAB_8010ceec
            final int halfW = displayWidth_1f8003e0.get() / 2;
            final int halfH = displayHeight_1f8003e4.get() / 2;
            final int x = s0.x_04.get() - halfW - (s0._2e.get() * s5.w_18.get(s6).get() >> 12) / 2;
            final int y = s0.y_06.get() - halfH - (s0._30.get() * s5.h_22.get(s6).get() >> 12) / 2;
            final int w2 = s5.w_18.get(s6).get() * s0._2e.get() >> 12;
            final int h2 = s5.h_22.get(s6).get() * s0._30.get() >> 12;

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
              .uv(1, u + w, v)
              .uv(2, u, v + h)
              .uv(3, u + w, v + h);

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
  public static FlowControl FUN_8010d1dc(final RunningScript<? extends BattleScriptDataBase> script) {
    final int count = script.params_20[1].get();
    final int s5 = script.params_20[2].get();

    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0x14,
      SEffe::FUN_8010f978,
      SEffe::FUN_8010d5b4,
      SEffe::FUN_8010fa20,
      BttlScriptData6cSub14_4::new
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    final BttlScriptData6cSub14_4 effect = (BttlScriptData6cSub14_4)manager.effect_44;
    effect.count_00.set(count);
    effect._02.set(0);

    effect.ptr_10.setPointer(mallocTail(count * 0x70));

    //LAB_8010d298
    for(int s2 = 0; s2 < count; s2++) {
      final BttlScriptData6cSub14_4Sub70 s4 = effect.ptr_10.deref().get(s2);
      s4._00.set(0);
      s4._02.set(0);
      s4._04.set(0);
      s4._08.set(0);
      s4._10.set(0);
      s4._64.set((short)1);
      int v0 = -1500 / count * s2 << 8;
      s4._54.set(v0);
      s4._0c.set(v0);
      s4._48.set(rand() % 0x361 + 0x100 << 8);
      v0 = rand() % 13 + 8;
      s4._4c.set((short)v0);
      final int v1 = s4._48.get() * 2 / v0;
      v0 = -v1 / s4._4c.get();
      s4._40.set(v1);
      s4._44.set(v0);

      if((simpleRand() & 1) != 0) {
        v0 = 0x5000 - s4._48.get() >> 4 << 1;
      } else {
        //LAB_8010d3b4
        v0 = -(0x5000 - (s4._48.get() >> 4) << 1);
      }

      //LAB_8010d3cc
      s4._60.set(v0);
      s4._50.set(rand() % 0x178 << 8);
      simpleRand();
      s4._6e.set((short)(rand() % 0x1000));
      s4._3c.set(0);
      s4._58.set(0);
      s4._5c.set(rand() % 0x1000 << 8);
      s4._38.set(0x7f);
      s4._39.set(0x7f);
      s4._3a.set(0x7f);
      s4._66.set(0);
      s4._68.set(0);
      s4._6a.set(0);
      s4._6c.set(0);
    }

    //LAB_8010d4a4
    if((s5 & 0xf_ff00) == 0xf_ff00) {
      final SpriteMetrics08 metrics = spriteMetrics_800c6948[s5 & 0xff];
      effect.u_06.set((short)metrics.u_00.get());
      effect.v_08.set((short)metrics.v_02.get());
      effect.width_0a.set((short)metrics.w_04.get());
      effect.height_0c.set((short)metrics.h_05.get());
      effect.clut_0e.set((short)metrics.clut_06.get());
    } else {
      //LAB_8010d508
      final DeffPart.SpriteType spriteType = (DeffPart.SpriteType)getDeffPart(s5 | 0x400_0000);
      final DeffPart.SpriteMetrics deffMetrics = spriteType.metrics_08.deref();
      effect.u_06.set((short)deffMetrics.u_00.get());
      effect.v_08.set((short)deffMetrics.v_02.get());
      effect.width_0a.set((short)(deffMetrics.w_04.get() * 4));
      effect.height_0c.set((short)deffMetrics.h_06.get());
      effect.clut_0e.set((short)GetClut(deffMetrics.clutX_08.get(), deffMetrics.clutY_0a.get()));
    }

    //LAB_8010d564
    manager._10.flags_00 |= 0x5000_0000;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x8010d5b4L)
  public static void FUN_8010d5b4(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final BttlScriptData6cSub14_4 effect = (BttlScriptData6cSub14_4)manager.effect_44;
    effect._02.incr();

    final BattleStruct24 sp0x10 = new BattleStruct24();

    sp0x10._00.set(manager._10.flags_00);
    sp0x10.x_04.set((short)(-effect.width_0a.get() / 2));
    sp0x10.y_06.set((short)(-effect.height_0c.get() / 2));
    sp0x10.w_08.set(effect.width_0a.get());
    sp0x10.h_0a.set(effect.height_0c.get());
    sp0x10.tpage_0c.set((effect.v_08.get() & 0x100) >>> 4 | (effect.u_06.get() & 0x3ff) >>> 6);
    sp0x10.u_0e.set((effect.u_06.get() & 0x3f) * 4);
    sp0x10.v_0f.set(effect.v_08.get());
    sp0x10.clutX_10.set(effect.clut_0e.get() << 4 & 0x3ff);
    sp0x10.clutY_12.set(effect.clut_0e.get() >>> 6 & 0x1ff);
    sp0x10._18.set((short)0);
    sp0x10._1a.set((short)0);

    final VECTOR sp0x38 = new VECTOR();

    //LAB_8010d6c8
    for(int s4 = 0; s4 < effect.count_00.get(); s4++) {
      final BttlScriptData6cSub14_4Sub70 s3 = effect.ptr_10.deref().get(s4);

      if(s3._00.get() != 0) {
        if(s3._38.get() == 0x7f && s3._39.get() == 0x7f && s3._3a.get() == 0x7f) {
          sp0x10.r_14.set(manager._10.colour_1c.getX());
          sp0x10.g_15.set(manager._10.colour_1c.getY());
          sp0x10.b_16.set(manager._10.colour_1c.getZ());
        } else {
          //LAB_8010d718
          sp0x10.r_14.set(s3._38.get());
          sp0x10.g_15.set(s3._39.get());
          sp0x10.b_16.set(s3._3a.get());
        }

        //LAB_8010d73c
        sp0x10._1c.set(manager._10.scale_16.getX());
        sp0x10._1e.set(manager._10.scale_16.getY());
        sp0x10.rotation_20.set(s3._6e.get());
        sp0x38.setX(manager._10.trans_04.getX() + (s3._08.get() >> 8));
        sp0x38.setY(manager._10.trans_04.getY() + (s3._0c.get() >> 8));
        sp0x38.setZ(manager._10.trans_04.getZ() + (s3._10.get() >> 8));
        FUN_800e7ea4(sp0x10, sp0x38);
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
      script.scriptState_04,
      0x20,
      SEffe::goldDragoonTransformEffectTicker,
      SEffe::goldDragoonTransformEffectRenderer,
      SEffe::goldDragoonTransformEffectDestructor,
      GoldDragoonTransformEffect20::new
    );

    final GoldDragoonTransformEffect20 effect = (GoldDragoonTransformEffect20)state.innerStruct_00.effect_44;

    effect.count_00.set(count);
    effect._04.set(0);
    effect.parts_08.setPointer(mallocTail(count * 0x84));

    //LAB_8010d8ec
    for(int i = 0; i < count; i++) {
      final GoldDragoonTransformEffectInstance84 instance = effect.parts_08.deref().get(i);
      instance.used_00.set(true);
      instance.counter_04.set(0);
      instance._68.set(0x7f);
      instance._69.set(0x7f);
      instance._6a.set(0x7f);

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
        instance._7c.set((short)(rand() % (sp2c + 1)));
      } else {
        instance._7c.set((short)0);
      }

      //LAB_8010dbe8
      instance._7e.set((short)(rand() % (sp30 + 2)));
      instance._80.set((short)-1);

      if((s7 & 0xf_ff00) == 0xf_ff00) {
        //TODO I added the first deref here, this might have been a retail bug...
        //     Looking at how _800c6944 gets set, I don't see how it could have been correct
        instance.tmd_70.set(tmds_800c6944[s7 & 0xff]);
      } else {
        //LAB_8010dc40
        final DeffPart.TmdType tmdType = (DeffPart.TmdType)getDeffPart(s7 | 0x300_0000);
        instance.tmd_70.set(tmdType.tmd_0c.deref().tmdPtr_00.deref().tmd.objTable.get(0));
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
    for(int i = 0; i < effect.count_00.get(); i++) {
      final GoldDragoonTransformEffectInstance84 instance = effect.parts_08.deref().get(i);

      if(!instance.used_00.get()) {
        unusedCount++;
        continue;
      }

      //LAB_8010dd18
      instance._80.incr();

      if(instance._7e.get() != -1) {
        if(instance._7e.get() == instance._80.get()) {
          instance._7e.set((short)-1);
        }
      }

      //LAB_8010dd40
      if(instance._7e.get() == -1) {
        //LAB_8010dd50
        instance.counter_04.incr();
        final int counter = instance.counter_04.get();

        instance.trans_08.x.add(instance.transStep_28.getX());
        instance.trans_08.y.set(counter * counter * 0x1800 - instance.transStep_28.getY() * counter);
        instance.trans_08.z.add(instance.transStep_28.getZ());

        instance.rot_38.add(instance.rotStep_48);

        if(instance._7c.get() <= 0) {
          if(instance.trans_08.getY() > 0x400) {
            instance.used_00.set(false);
          }
          //LAB_8010ddf8
        } else if(instance.trans_08.getY() >= 0) {
          instance.counter_04.set(1);
          instance.transStep_28.y.div(2);
          instance._7c.decr();
        }
      }
    }

    if(unusedCount >= effect.count_00.get()) {
      //LAB_8010de5c
      state.deallocateWithChildren();
    }

    //LAB_8010de6c
  }

  @Method(0x8010de7cL)
  public static void goldDragoonTransformEffectRenderer(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final GoldDragoonTransformEffect20 effect = (GoldDragoonTransformEffect20)manager.effect_44;
    effect._04.incr();

    final GsDOBJ2 dobj2 = new GsDOBJ2();
    final VECTOR trans = new VECTOR();
    final SVECTOR rot = new SVECTOR();
    final MATRIX transforms = new MATRIX();
    final MATRIX sp0x98 = new MATRIX();
    final VECTOR scale = new VECTOR();

    //LAB_8010ded8
    for(int i = 0; i < effect.count_00.get(); i++) {
      final GoldDragoonTransformEffectInstance84 instance = effect.parts_08.deref().get(i);

      if(instance.used_00.get()) {
        trans.setX((instance.trans_08.getX() >> 8) + manager._10.trans_04.getX());
        trans.setY((instance.trans_08.getY() >> 8) + manager._10.trans_04.getY());
        trans.setZ((instance.trans_08.getZ() >> 8) + manager._10.trans_04.getZ());

        rot.set(instance.rot_38);
        scale.set(manager._10.scale_16);

        RotMatrix_8003faf0(rot, transforms);
        TransMatrix(transforms, trans);
        ScaleMatrix(transforms, scale);

        dobj2.tmd_08 = instance.tmd_70.deref();

        MulMatrix0(worldToScreenMatrix_800c3548, transforms, sp0x98);
        setRotTransMatrix(sp0x98);

        zOffset_1f8003e8.set(0);
        tmdGp0Tpage_1f8003ec.set(2);

        renderDobj2(dobj2);
      }
    }

    //LAB_8010e020
  }

  @Method(0x8010e04cL)
  public static FlowControl FUN_8010e04c(final RunningScript<? extends BattleScriptDataBase> script) {
    final int count = script.params_20[2].get();
    final int s4 = script.params_20[1].get();

    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0x10,
      SEffe::FUN_8010e6b0,
      SEffe::FUN_8010e2fc,
      SEffe::FUN_8010feb8,
      BttlScriptData6cSub10_2::new
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    manager._10.flags_00 |= 0x5000_0000;

    final BttlScriptData6cSub10_2 effect = (BttlScriptData6cSub10_2)manager.effect_44;
    long addr = mallocTail(count * 0x10);
    effect.count_00.set(count);
    effect.ptr_0c.set(addr);

    //LAB_8010e100
    for(int i = 0; i < count; i++) {
      MEMORY.ref(1, addr).offset(0x0L).setu(0x1L);
      MEMORY.ref(2, addr).offset(0x2L).setu(rand() % 321 - 160);
      MEMORY.ref(2, addr).offset(0x4L).setu(rand() % 241 - 120);
      final long v0 = rand() % 1025 + 1024;
      MEMORY.ref(2, addr).offset(0xaL).setu(v0);
      MEMORY.ref(2, addr).offset(0xcL).setu(v0);
      MEMORY.ref(2, addr).offset(0xeL).setu(v0);
      MEMORY.ref(2, addr).offset(0xaL).shl(1);
      addr += 0x10L;
    }

    //LAB_8010e1d8
    if((s4 & 0xf_ff00) == 0xf_ff00) {
      final SpriteMetrics08 metrics = spriteMetrics_800c6948[s4 & 0xff];
      effect.metrics_04.u_00.set(metrics.u_00.get());
      effect.metrics_04.v_02.set(metrics.v_02.get());
      effect.metrics_04.w_04.set(metrics.w_04.get());
      effect.metrics_04.h_05.set(metrics.h_05.get());
      effect.metrics_04.clut_06.set(metrics.clut_06.get());
    } else {
      //LAB_8010e254
      final DeffPart.SpriteType spriteType = (DeffPart.SpriteType)getDeffPart(s4 | 0x400_0000);
      final DeffPart.SpriteMetrics deffMetrics = spriteType.metrics_08.deref();
      effect.metrics_04.u_00.set(deffMetrics.u_00.get());
      effect.metrics_04.v_02.set(deffMetrics.v_02.get());
      effect.metrics_04.w_04.set(deffMetrics.w_04.get() * 4);
      effect.metrics_04.h_05.set(deffMetrics.h_06.get());
      effect.metrics_04.clut_06.set(GetClut(deffMetrics.clutX_08.get(), deffMetrics.clutY_0a.get()));
    }

    //LAB_8010e2b0
    manager._10.flags_00 |= 0x5000_0000;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  /** Used in Star Children */
  @Method(0x8010e2fcL)
  public static void FUN_8010e2fc(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final BttlScriptData6cSub10_2 s1 = (BttlScriptData6cSub10_2)manager.effect_44;
    final int flags = manager._10.flags_00;
    final int sp14 = -s1.metrics_04.w_04.get() / 2;
    final int sp16 = -s1.metrics_04.h_05.get() / 2;
    final int tw = s1.metrics_04.w_04.get();
    final int th = s1.metrics_04.h_05.get();
    final int tpage = (s1.metrics_04.v_02.get() & 0x100) >>> 4 | (s1.metrics_04.u_00.get() & 0x3ff) >>> 6;
    final int u = (s1.metrics_04.u_00.get() & 0x3f) * 4;
    final int v = s1.metrics_04.v_02.get();
    final int clutX = s1.metrics_04.clut_06.get() << 4 & 0x3ff;
    final int sp28 = 0;
    final int sp2a = 0;
    final int clutY = s1.metrics_04.clut_06.get() >>> 6 & 0x1ff;
    final int r = manager._10.colour_1c.getX();
    final int g = manager._10.colour_1c.getY();
    final int b = manager._10.colour_1c.getZ();
    long s2 = s1.ptr_0c.get();

    //LAB_8010e414
    for(int s3 = 0; s3 < s1.count_00.get(); s3++) {
      if(MEMORY.ref(1, s2).offset(0x0L).getSigned() != 0) {
        final int w = (int)MEMORY.ref(2, s2).offset(0xcL).getSigned() * s1.metrics_04.w_04.get() >> 12;
        final int h = (int)MEMORY.ref(2, s2).offset(0xeL).getSigned() * s1.metrics_04.h_05.get() >> 12;
        final int x = (int)MEMORY.ref(2, s2).offset(0x2L).get() - w / 2;
        final int y = (int)MEMORY.ref(2, s2).offset(0x4L).get() - h / 2;

        final GpuCommandPoly cmd = new GpuCommandPoly(4)
          .bpp(Bpp.BITS_4)
          .clut(clutX, clutY)
          .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
          .rgb(r, g, b)
          .pos(0, x, y)
          .pos(1, x + w, y)
          .pos(2, x, y + h)
          .pos(3, x + w, y + h)
          .uv(0, u, v)
          .uv(1, u + tw, v)
          .uv(2, u, v + th)
          .uv(3, u + tw, v + th);

        if((flags >>> 30 & 1) != 0) {
          cmd.translucent(Translucency.of(flags >>> 28 & 0b11));
        }

        GPU.queueCommand(30, cmd);
      }

      //LAB_8010e678
      s2 += 0x10L;
    }

    //LAB_8010e694
  }

  @Method(0x8010e6b0L)
  public static void FUN_8010e6b0(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final BttlScriptData6cSub10_2 effect = (BttlScriptData6cSub10_2)manager.effect_44;

    //LAB_8010e6ec
    long s2 = effect.ptr_0c.get();
    for(int s3 = 0; s3 < effect.count_00.get(); s3++) {
      MEMORY.ref(2, s2).offset(0x6L).setu(MEMORY.ref(2, s2).offset(0x2L).get());
      MEMORY.ref(2, s2).offset(0x8L).setu(MEMORY.ref(2, s2).offset(0x4L).get());
      MEMORY.ref(2, s2).offset(0x2L).addu((rsin(manager._10.rot_10.getX()) * 32 >> 12) * manager._10.scale_16.getX() * MEMORY.ref(2, s2).offset(0x6L).getSigned() >> 24);
      MEMORY.ref(2, s2).offset(0x4L).addu((rcos(manager._10.rot_10.getX()) * 32 >> 12) * manager._10.scale_16.getX() * MEMORY.ref(2, s2).offset(0x6L).getSigned() >> 24);

      if(MEMORY.ref(2, s2).offset(0xaL).getSigned() * 120 + 50 >> 12 < MEMORY.ref(2, s2).offset(0x4L).getSigned()) {
        MEMORY.ref(2, s2).offset(0x8L).setu(-120);
        MEMORY.ref(2, s2).offset(0x4L).setu(-120);
        final long v0 = rand() % 321 - 160;
        MEMORY.ref(2, s2).offset(0x6L).setu(v0);
        MEMORY.ref(2, s2).offset(0x2L).setu(v0);
        MEMORY.ref(1, s2).offset(0x0L).setu(0x1L);
      }

      //LAB_8010e828
      final long v1 = MEMORY.ref(2, s2).offset(0x2L).getSigned();
      if(v1 > 0xa0) {
        MEMORY.ref(2, s2).offset(0x6L).setu(-0xa0);
        MEMORY.ref(2, s2).offset(0x2L).setu(-0xa0);
        MEMORY.ref(2, s2).offset(0x8L).setu(MEMORY.ref(2, s2).offset(0x4L).get());
        //LAB_8010e848
      } else if(v1 < -0xa0) {
        //LAB_8010e854
        MEMORY.ref(2, s2).offset(0x6L).setu(0xa0);
        MEMORY.ref(2, s2).offset(0x2L).setu(0xa0);
        MEMORY.ref(2, s2).offset(0x8L).setu(MEMORY.ref(2, s2).offset(0x4L).get());
      }

      //LAB_8010e860
      s2 += 0x10L;
    }

    //LAB_8010e87c
  }

  @Method(0x8010e89cL)
  public static FlowControl FUN_8010e89c(final RunningScript<? extends BattleScriptDataBase> script) {
    final int count = script.params_20[4].get();
    final int sp1c = script.params_20[1].get();
    final int sp20 = script.params_20[2].get();
    final int sp24 = script.params_20[3].get();

    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0x18,
      SEffe::FUN_8010ff10,
      SEffe::FUN_8010ec08,
      SEffe::FUN_8010ffd8,
      BttlScriptData6cSub18::new
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    final BttlScriptData6cSub18 effect = (BttlScriptData6cSub18)manager.effect_44;
    effect.count_00.set(count);
    effect.ptr_0c.setPointer(mallocTail(count * 0x3c));
    manager._10.flags_00 |= 0x5000_0000;

    //LAB_8010e980
    for(int i = 0; i < count; i++) {
      final BttlScriptData6cSub18Sub3c s4 = effect.ptr_0c.deref().get(i);
      s4._00.set((short)0);
      s4._03.set(1);
      s4._06.set((short)0);
      s4._36.set((short)sp20);
      s4._38.set((short)(seed_800fa754.advance().get() % 181));
      final int s2 = (int)(seed_800fa754.advance().get() % (sp24 + 1));
      final int s1 = (int)(seed_800fa754.advance().get() & 0xfff);
      s4._04.set((short)((rcos(s1) - rsin(s1)) * s2 >> 12));
      s4._08.set((short)((rcos(s1) + rsin(s1)) * s2 >> 12));
    }

    //LAB_8010ead0
    if((sp1c & 0xf_ff00) == 0xf_ff00) {
      final SpriteMetrics08 metrics = spriteMetrics_800c6948[sp1c & 0xff];
      effect.metrics_04.u_00.set(metrics.u_00.get());
      effect.metrics_04.v_02.set(metrics.v_02.get());
      effect.metrics_04.w_04.set(metrics.w_04.get());
      effect.metrics_04.h_05.set(metrics.h_05.get());
      effect.metrics_04.clut_06.set(metrics.clut_06.get());
    } else {
      //LAB_8010eb50
      final DeffPart.SpriteType spriteType = (DeffPart.SpriteType)getDeffPart(sp1c | 0x400_0000);
      final DeffPart.SpriteMetrics deffMetrics = spriteType.metrics_08.deref();
      effect.metrics_04.u_00.set(deffMetrics.u_00.get());
      effect.metrics_04.v_02.set(deffMetrics.v_02.get());
      effect.metrics_04.w_04.set(deffMetrics.w_04.get() * 4);
      effect.metrics_04.h_05.set(deffMetrics.h_06.get());
      effect.metrics_04.clut_06.set(GetClut(deffMetrics.clutX_08.get(), deffMetrics.clutY_0a.get()));
    }

    //LAB_8010ebac
    manager._10.flags_00 |= 0x5000_0000;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x8010ec08L)
  public static void FUN_8010ec08(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final BttlScriptData6cSub18 effect = (BttlScriptData6cSub18)manager.effect_44;

    final BattleStruct24 sp0x10 = new BattleStruct24();
    sp0x10._00.set(manager._10.flags_00 & 0xffff_ffffL);
    sp0x10.x_04.set((short)(-effect.metrics_04.w_04.get() / 2));
    sp0x10.y_06.set((short)(-effect.metrics_04.h_05.get() / 2));
    sp0x10.w_08.set(effect.metrics_04.w_04.get());
    sp0x10.h_0a.set(effect.metrics_04.h_05.get());
    sp0x10.tpage_0c.set((effect.metrics_04.v_02.get() & 0x100) >>> 4 | (effect.metrics_04.u_00.get() & 0x3ff) >>> 6);
    sp0x10.u_0e.set((effect.metrics_04.u_00.get() & 0x3f) << 2);
    sp0x10.v_0f.set(effect.metrics_04.v_02.get());
    sp0x10.clutX_10.set(effect.metrics_04.clut_06.get() << 4 & 0x3ff);
    sp0x10.clutY_12.set(effect.metrics_04.clut_06.get() >>> 6 & 0x1ff);
    sp0x10._18.set((short)0);
    sp0x10._1a.set((short)0);

    final VECTOR sp0x38 = new VECTOR();

    //LAB_8010ed00
    for(int i = 0; i < effect.count_00.get(); i++) {
      final BttlScriptData6cSub18Sub3c v1 = effect.ptr_0c.deref().get(i);

      if(v1._03.get() == 0) {
        break;
      }

      sp0x10.r_14.set(manager._10.colour_1c.getX());
      sp0x10.g_15.set(manager._10.colour_1c.getY());
      sp0x10.b_16.set(manager._10.colour_1c.getZ());
      sp0x10._1c.set(manager._10.scale_16.getX());
      sp0x10._1e.set(manager._10.scale_16.getY());
      sp0x10.rotation_20.set(manager._10.rot_10.getX());
      sp0x38.setX(manager._10.trans_04.getX() + v1._04.get());
      sp0x38.setY(manager._10.trans_04.getY() + v1._06.get());
      sp0x38.setZ(manager._10.trans_04.getZ() + v1._08.get());
      FUN_800e7ea4(sp0x10, sp0x38);
    }

    //LAB_8010edac
  }

  @Method(0x8010edc8L)
  public static FlowControl FUN_8010edc8(final RunningScript<? extends BattleScriptDataBase> script) {
    final int sp18 = (int)_800fb940.offset(0x0L).get();
    final int sp1c = (int)_800fb940.offset(0x4L).get();
    final int sp20 = (int)_800fb940.offset(0x8L).get();
    final int count = script.params_20[1].get();
    final int s5 = script.params_20[2].get();
    final int sp38 = script.params_20[3].get();

    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0x20,
      SEffe::FUN_8010f124,
      SEffe::FUN_8010f340,
      SEffe::FUN_8010fee4,
      BttlScriptData6cSub20_2::new
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    final BttlScriptData6cSub20_2 effect = (BttlScriptData6cSub20_2)manager.effect_44;
    long addr = mallocTail(count * 0xa8);
    effect.count_00.set(count);
    effect._04.set(0);
    effect.ptr_08.set(addr);

    //LAB_8010eecc
    for(int i = 0; i < count; i++) {
      MEMORY.ref(1, addr).offset(0x8cL).setu(0x7fL); //TODO
      MEMORY.ref(1, addr).offset(0x8dL).setu(0x7fL);
      MEMORY.ref(1, addr).offset(0x8eL).setu(0x7fL);
      MEMORY.ref(4, addr).offset(0x04L).setu(rand() % s5 + 1);
      MEMORY.ref(1, addr).offset(0x00L).setu(0);
      MEMORY.ref(4, addr).offset(0x34L).setu(0);
      MEMORY.ref(4, addr).offset(0x30L).setu(0);
      MEMORY.ref(4, addr).offset(0x2cL).setu(0);
      MEMORY.ref(4, addr).offset(0x74L).setu(0);
      MEMORY.ref(4, addr).offset(0x70L).setu(0);
      MEMORY.ref(4, addr).offset(0x6cL).setu(0);
      final int s3 = rand() * (sp38 + 1);
      final int s2 = rand() % 4096;
      MEMORY.ref(4, addr).offset(0x10L).setu(0);
      MEMORY.ref(4, addr).offset(0x0cL).setu((rcos(s2) - rsin(s2)) * s3 >> 12);
      MEMORY.ref(1, addr).offset(0x8cL).setu(0xffL);
      MEMORY.ref(1, addr).offset(0x8dL).setu(0xffL);
      MEMORY.ref(1, addr).offset(0x8eL).setu(0xffL);
      MEMORY.ref(2, addr).offset(0xa2L).setu(0);
      MEMORY.ref(2, addr).offset(0xa0L).setu(0);
      MEMORY.ref(4, addr).offset(0x14L).setu((rcos(s2) + rsin(s2)) * s3 >> 12);
      MEMORY.ref(1, addr).offset(0x90L).setu(0x7fL);
      MEMORY.ref(1, addr).offset(0x91L).setu(0x7fL);
      MEMORY.ref(1, addr).offset(0x92L).setu(0x7fL);
      MEMORY.ref(1, addr).offset(0x01L).setu(0);
      MEMORY.ref(4, addr).offset(0x44L).setu(0);
      MEMORY.ref(4, addr).offset(0x3cL).setu(0);
      MEMORY.ref(4, addr).offset(0x08L).setu(rand() % s5 + 1);
      MEMORY.ref(4, addr).offset(0x4cL).setu(rand() % 4096);
      MEMORY.ref(4, addr).offset(0x84L).setu(0xc00L);
      MEMORY.ref(4, addr).offset(0x7cL).setu(0xc00L);
      MEMORY.ref(4, addr).offset(0x80L).setu(0x400L);
      MEMORY.ref(1, addr).offset(0x90L).setu(0xffL);
      MEMORY.ref(1, addr).offset(0x91L).setu(0xffL);
      MEMORY.ref(1, addr).offset(0x92L).setu(0xffL);
      MEMORY.ref(4, addr).offset(0x1cL).setu(MEMORY.ref(4, addr).offset(0x0cL).get());
      MEMORY.ref(4, addr).offset(0x20L).setu(MEMORY.ref(4, addr).offset(0x10L).get() - 0x100L);
      MEMORY.ref(4, addr).offset(0x24L).setu(MEMORY.ref(4, addr).offset(0x14L).get());
      MEMORY.ref(4, addr).offset(0x94L).setu(((DeffPart.TmdType)getDeffPart(sp18 | 0x300_0000)).tmd_0c.deref().tmdPtr_00.deref().tmd.objTable.get(0).getAddress());
      MEMORY.ref(4, addr).offset(0x98L).setu(((DeffPart.TmdType)getDeffPart(sp1c | 0x300_0000)).tmd_0c.deref().tmdPtr_00.deref().tmd.objTable.get(0).getAddress());
      MEMORY.ref(4, addr).offset(0x9cL).setu(((DeffPart.TmdType)getDeffPart(sp20 | 0x300_0000)).tmd_0c.deref().tmdPtr_00.deref().tmd.objTable.get(0).getAddress());
      addr += 0xa8L;
    }

    //LAB_8010f0d0
    manager._10.flags_00 |= 0x1400_0000;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x8010f124L)
  public static void FUN_8010f124(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final BttlScriptData6cSub20_2 effect = (BttlScriptData6cSub20_2)manager.effect_44;
    long s1 = effect.ptr_08.get();

    //LAB_8010f168
    for(int i = 0; i < effect.count_00.get(); i++) {
      if(MEMORY.ref(4, s1).offset(0x04L).get() < effect._04.get()) {
        if(MEMORY.ref(2, s1).offset(0xa0L).getSigned() >= 0x1000) {
          MEMORY.ref(1, s1).offset(0x00L).setu(0);
        } else {
          //LAB_8010f19c
          long v1 = MEMORY.ref(2, s1).offset(0xa2L).getSigned();

          if(v1 >= 9) {
            if(v1 == 9) {
              MEMORY.ref(4, s1).offset(0x10L).setu(-0x800);
              MEMORY.ref(4, s1).offset(0x6cL).setu(0x6800);
              MEMORY.ref(4, s1).offset(0x74L).setu(0x6800);
            }

            //LAB_8010f1dc
            if(v1 >= 10) {
              if(v1 == 10) {
                MEMORY.ref(1, s1).offset(0x01L).setu(0);
              }

              //LAB_8010f1f0
              MEMORY.ref(4, s1).offset(0x40L).addu(0x80);
              MEMORY.ref(4, s1).offset(0x80L).addu(0x600);
              MEMORY.ref(2, s1).offset(0xa0L).addu(0x200);
              MEMORY.ref(4, s1).offset(0x84L).subu(0x1c0);
              MEMORY.ref(4, s1).offset(0x7cL).subu(0x1c0);
            }

            //LAB_8010f22c
            MEMORY.ref(2, s1).offset(0xa2L).addu(0x1L);
          } else {
            //LAB_8010f240
            MEMORY.ref(1, s1).offset(0x00L).setu(0x1L);
            MEMORY.ref(1, s1).offset(0x01L).setu(0x1L);
            MEMORY.ref(4, s1).offset(0x30L).addu(0x80);
            MEMORY.ref(4, s1).offset(0x74L).addu(0x266);
            MEMORY.ref(2, s1).offset(0xa0L).addu(0xcc);
            MEMORY.ref(4, s1).offset(0x6cL).addu(0x266);

            v1 = rsin(MEMORY.ref(2, s1).offset(0xa0L).getSigned()) * 0x1600 >> 12;
            if(v1 >= 0) {
              MEMORY.ref(4, s1).offset(0x70L).setu(v1);
            } else {
              MEMORY.ref(4, s1).offset(0x70L).setu(0);
            }

            //LAB_8010f2a8
            MEMORY.ref(1, s1).offset(0x8cL).subu(23);
            MEMORY.ref(1, s1).offset(0x8dL).subu(23);
            MEMORY.ref(1, s1).offset(0x8eL).subu(23);
            MEMORY.ref(2, s1).offset(0xa2L).addu(1);
          }
        }
      }

      //LAB_8010f2d8
      s1 += 0xa8L;
    }

    //LAB_8010f2f4
    effect._04.incr();

    if(effect.count_00.get() <= 0) {
      state.deallocateWithChildren();
    }

    //LAB_8010f31c
  }

  @Method(0x8010f340L)
  public static void FUN_8010f340(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final BttlScriptData6cSub20_2 effect = (BttlScriptData6cSub20_2)manager.effect_44;
    if(manager._10.flags_00 >= 0) {
      long s3 = effect.ptr_08.get();

      final VECTOR sp0x14 = new VECTOR();
      final SVECTOR sp0x20 = new SVECTOR();
      final MATRIX sp0x3c = new MATRIX();
      final MATRIX sp0x88 = new MATRIX();
      final VECTOR sp0xa8 = new VECTOR();
      final MATRIX sp0xb8 = new MATRIX();
      final MATRIX sp0xd8 = new MATRIX();

      final GsDOBJ2 sp0xf8 = new GsDOBJ2();

      //LAB_8010f3a4
      for(int i = 0; i < effect.count_00.get(); i++) {
        if(MEMORY.ref(1, s3).offset(0x00L).get() != 0) {
          FUN_800e8594(sp0xb8, manager);
          final int a1 = MEMORY.ref(2, s3).offset(0xa2L).getSigned() >= 10 ? 1 : 0;
          final long a0 = s3 + a1 * 0x10;
          final long a2 = s3 + a1 * 0x4;
          final long sp10 = 0x5000_0000L;
          sp0x14.setX((int)(MEMORY.ref(4, a0).offset(0x0cL).getSigned() + manager._10.trans_04.getX()));
          sp0x14.setY((int)(MEMORY.ref(4, a0).offset(0x10L).getSigned() + manager._10.trans_04.getY()));
          sp0x14.setZ((int)(MEMORY.ref(4, a0).offset(0x14L).getSigned() + manager._10.trans_04.getZ()));
          sp0x20.setX((short)MEMORY.ref(2, a0).offset(0x2cL).get());
          sp0x20.setY((short)MEMORY.ref(2, a0).offset(0x30L).get());
          sp0x20.setZ((short)MEMORY.ref(2, a0).offset(0x34L).get());
          final short sp26 = (short)(MEMORY.ref(4, a0).offset(0x6cL).get() * manager._10.scale_16.getX() >> 12);
          final short sp28 = (short)(MEMORY.ref(4, a0).offset(0x70L).get() * manager._10.scale_16.getY() >> 12);
          final short sp2a = (short)(MEMORY.ref(4, a0).offset(0x74L).get() * manager._10.scale_16.getZ() >> 12);
          final int sp2c = (int)(MEMORY.ref(1, a2).offset(0x8cL).get() * manager._10.colour_1c.getX() >> 8);
          final int sp2e = (int)(MEMORY.ref(1, a2).offset(0x8dL).get() * manager._10.colour_1c.getY() >> 8);
          final int sp30 = (int)(MEMORY.ref(1, a2).offset(0x8eL).get() * manager._10.colour_1c.getZ() >> 8);

          if((manager._10.flags_00 & 0x40) == 0) {
            FUN_800e61e4(sp2c << 5, sp2e << 5, sp30 << 5);
          }

          //LAB_8010f50c
          GsSetLightMatrix(sp0xb8);
          MulMatrix0(worldToScreenMatrix_800c3548, sp0xb8, sp0xd8);
          setRotTransMatrix(sp0xd8);
          RotMatrix_8003faf0(sp0x20, sp0x3c);
          TransMatrix(sp0x3c, sp0x14);
          sp0xa8.setX(sp26);
          sp0xa8.setY(sp28);
          sp0xa8.setZ(sp2a);
          ScaleMatrix(sp0x3c, sp0xa8);
          final long sp80 = 0;
          final long sp38 = 0;
          sp0xf8.attribute_00 = manager._10.flags_00;
          MulMatrix0(worldToScreenMatrix_800c3548, sp0x3c, sp0x88);
          setRotTransMatrix(sp0x88);
          zOffset_1f8003e8.set(0);
          tmdGp0Tpage_1f8003ec.set(manager._10.flags_00 >>> 23 & 0x60);

          if(MEMORY.ref(1, s3).offset(0x01L).get() != 0) {
            sp0xf8.tmd_08 = MEMORY.ref(4, MEMORY.ref(4, s3).offset(0x98L).get(), TmdObjTable::new);
            renderCtmd(sp0xf8);
          }

          //LAB_8010f5d0
          final long v1 = MEMORY.ref(2, s3).offset(0xa2L).getSigned();

          if(v1 < 9) {
            sp0xf8.tmd_08 = MEMORY.ref(4, MEMORY.ref(4, s3).offset(0x94L).get(), TmdObjTable::new);
            renderCtmd(sp0xf8);
          } else if(v1 >= 11) {
            sp0xf8.tmd_08 = MEMORY.ref(4, MEMORY.ref(4, s3).offset(0x9cL).get(), TmdObjTable::new);
            renderCtmd(sp0xf8);
          }

          //LAB_8010f608
          if((manager._10.flags_00 & 0x40) == 0) {
            FUN_800e62a8();
          }
        }

        //LAB_8010f624
        s3 += 0xa8L;
      }
    }

    //LAB_8010f640
  }

  @Method(0x8010f94cL)
  public static void FUN_8010f94c(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    free(((BttlScriptData6cSub50)manager.effect_44)._38.getPointer());
  }

  @Method(0x8010f978L)
  public static void FUN_8010f978(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final BttlScriptData6cSub14_4 effect = (BttlScriptData6cSub14_4)manager.effect_44;

    //LAB_8010f9c0
    for(int i = 0; i < effect.count_00.get(); i++) {
      final BttlScriptData6cSub14_4Sub70 s0 = effect.ptr_10.deref().get(i);
      _80119ff4.get(s0._02.get()).deref().run(manager, s0);
    }

    //LAB_8010f9fc
  }

  @Method(0x8010fa20L)
  public static void FUN_8010fa20(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    free(((BttlScriptData6cSub14_4)manager.effect_44).ptr_10.getPointer());
  }

  @Method(0x8010fa4cL)
  public static void FUN_8010fa4c(final EffectManagerData6c manager, final BttlScriptData6cSub14_4Sub70 a2) {
    a2._04.incr();

    if(a2._04.get() >= a2._64.get()) {
      a2._00.set(1);
      a2._04.set(0);
      a2._02.incr();
    }
  }

  @Method(0x8010fa88L)
  public static void FUN_8010fa88(final EffectManagerData6c manager, final BttlScriptData6cSub14_4Sub70 a2) {
    a2._58.add(a2._60.get());
    a2._40.add(a2._44.get());
    a2._3c.add(a2._40.get());
    a2._08.set((rcos(a2._58.get() + a2._5c.get() >> 8) - rsin(a2._58.get() + a2._5c.get() >> 8)) * (a2._3c.get() >> 8) >> 4);
    a2._0c.set(a2._54.get() + (a2._50.get() * rsin(a2._58.get() >> 8 & 0xfff) >> 12));
    a2._10.set((rcos(a2._58.get() + a2._5c.get() >> 8) + rsin(a2._58.get() + a2._5c.get() >> 8)) * (a2._3c.get() >> 8) >> 4);

    a2._04.incr();
    if(a2._04.get() >= a2._4c.get()) {
      a2._04.set(0);
      a2._02.incr();
    }

    //LAB_8010fbc0
  }

  @Method(0x8010fbd4L)
  public static void FUN_8010fbd4(final EffectManagerData6c manager, final BttlScriptData6cSub14_4Sub70 a2) {
    a2._58.add(a2._60.get());
    a2._08.set((rcos(a2._58.get() + a2._5c.get() >> 8) - rsin(a2._58.get() + a2._5c.get() >> 8)) * (a2._3c.get() >> 8) >> 4);
    a2._0c.set(a2._54.get() + (a2._50.get() * rsin(a2._58.get() >> 8 & 0xfff) >> 12));
    a2._10.set((rcos(a2._58.get() + a2._5c.get() >> 8) + rsin(a2._58.get() + a2._5c.get() >> 8)) * (a2._3c.get() >> 8) >> 4);

    a2._04.incr();
    if(a2._04.get() >= 0xf) {
      a2._04.set(0);
      a2._02.incr();
      a2._40.set(-a2._3c.get() / a2._4c.get());
      a2._1c.set(-a2._50.get() / a2._4c.get());
    }

    //LAB_8010fd20
  }

  @Method(0x8010fd34L)
  public static void FUN_8010fd34(final EffectManagerData6c manager, final BttlScriptData6cSub14_4Sub70 a2) {
    a2._58.add(a2._60.get());
    a2._3c.add(a2._40.get());
    a2._08.set((rcos(a2._58.get() + a2._5c.get() >> 8) - rsin(a2._58.get() + a2._5c.get() >> 8)) * (a2._3c.get() >> 8) >> 4);
    a2._0c.set(a2._54.get() + (a2._50.get() * rsin(a2._58.get() >> 8 & 0xfff) >> 12));
    a2._50.add(a2._1c.get());
    a2._10.set((rcos(a2._58.get() + a2._5c.get() >> 8) + rsin(a2._58.get() + a2._5c.get() >> 8)) * (a2._3c.get() >> 8) >> 4);

    a2._04.incr();
    if(a2._04.get() >= a2._4c.get()) {
      a2._00.set(0);
      a2._04.set(0);
      a2._02.incr();
    }

    //LAB_8010fe70
  }

  @Method(0x8010fe84L)
  public static void FUN_8010fe84(final EffectManagerData6c manager, final BttlScriptData6cSub14_4Sub70 a2) {
    // no-op
  }

  @Method(0x8010fe8cL)
  public static void goldDragoonTransformEffectDestructor(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    free(((GoldDragoonTransformEffect20)manager.effect_44).parts_08.getPointer());
  }

  @Method(0x8010feb8L)
  public static void FUN_8010feb8(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    free(((BttlScriptData6cSub10_2)manager.effect_44).ptr_0c.get());
  }

  @Method(0x8010fee4L)
  public static void FUN_8010fee4(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    free(((BttlScriptData6cSub20_2)manager.effect_44).ptr_08.get());
  }

  @Method(0x8010ff10L)
  public static void FUN_8010ff10(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final BttlScriptData6cSub18 effect = (BttlScriptData6cSub18)manager.effect_44;

    //LAB_8010ff34
    for(int i = 0; i < effect.count_00.get(); i++) {
      final BttlScriptData6cSub18Sub3c a2 = effect.ptr_0c.deref().get(i);

      a2._00.incr();
      if(a2._38.get() < a2._00.get()) {
        a2._03.set(0);
        a2._00.set((short)0);
        a2._38.set((short)(seed_800fa754.advance().get() % (a2._36.get() + 1)));
      } else {
        //LAB_8010ffb0
        a2._03.set(1);
      }
    }
  }

  @Method(0x8010ffd8L)
  public static void FUN_8010ffd8(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    free(((BttlScriptData6cSub18)manager.effect_44).ptr_0c.getPointer());
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

  @Method(0x80110120L)
  public static SVECTOR FUN_80110120(final SVECTOR a0, @Nullable VECTOR a1, final VECTOR a2) {
    if(a1 == null) {
      a1 = new VECTOR();
    }

    //LAB_8011014c
    final VECTOR sp0x10 = new VECTOR().set(a2).sub(a1);
    a0.setZ((short)0);
    a0.setY((short)ratan2(sp0x10.getX(), sp0x10.getZ()));

    final short s1 = (short)(rcos(-a0.getY()) * sp0x10.getZ() - rsin(-a0.getY()) * sp0x10.getX());
    a0.setX((short)ratan2(-sp0x10.getY(), s1 / 0x1000));
    return a0;
  }

  @Method(0x80110228L)
  public static SVECTOR FUN_80110228(final SVECTOR s2, @Nullable VECTOR a3, final VECTOR a2) {
    if(a3 == null) {
      a3 = new VECTOR();
    }

    //LAB_80110258
    final VECTOR sp0x10 = new VECTOR().set(a2).sub(a3).negate();
    final SVECTOR sp0x30 = new SVECTOR();
    sp0x30.setY((short)ratan2(sp0x10.getX(), sp0x10.getZ()));

    final int s1 = rcos(-sp0x30.getY()) * sp0x10.getZ() - rsin(-sp0x30.getY()) * sp0x10.getX();
    sp0x30.setX((short)ratan2(-sp0x10.getY(), s1 / 0x1000));

    final MATRIX sp0x38 = new MATRIX();
    RotMatrix_80040010(sp0x30, sp0x38);
    FUN_800de544(s2, sp0x38);

    return s2;
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
      RotMatrix_8003faf0(rotation2.get(), sp0x38);

      final VECTOR sp0x28 = new VECTOR();
      ApplyTransposeMatrixLV(sp0x38, translationDelta, sp0x28);
      a2.set(sp0x28);
    }

    //LAB_80110450
  }

  @Method(0x80110488L)
  public static void FUN_80110488(final int scriptIndex1, final int scriptIndex2, final VECTOR s1) {
    final MATRIX sp0x10 = new MATRIX();
    FUN_800e8594(sp0x10, (EffectManagerData6c)scriptStatePtrArr_800bc1c0[scriptIndex1].innerStruct_00);

    if(scriptIndex2 == -1) {
      s1.set(sp0x10.transfer);
    } else {
      //LAB_80110500
      final MATRIX sp0x30 = new MATRIX();
      FUN_800e8594(sp0x30, (EffectManagerData6c)scriptStatePtrArr_800bc1c0[scriptIndex2].innerStruct_00);
      sp0x10.transfer.sub(sp0x30.transfer);

      final VECTOR sp0x50 = new VECTOR();
      ApplyTransposeMatrixLV(sp0x30, sp0x10.transfer, sp0x50);
      s1.set(sp0x50);
    }

    //LAB_80110594
  }

  @Method(0x801105ccL)
  public static void FUN_801105cc(final VECTOR out, final int scriptIndex, final VECTOR a2) {
    final Ref<SVECTOR> rotation = new Ref<>();
    final Ref<VECTOR> translation = new Ref<>();
    getScriptedObjectRotationAndTranslation(scriptIndex, rotation, translation);

    final MATRIX sp0x10 = new MATRIX();
    RotMatrix_8003faf0(rotation.get(), sp0x10);

    out
      .set(ApplyMatrixLV(sp0x10, a2))
      .add(translation.get());
  }

  @Method(0x8011066cL)
  public static BattleScriptDataBase FUN_8011066c(final int scriptIndex1, final int scriptIndex2, final VECTOR translation) {
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
      FUN_801105cc(objTranslation, scriptIndex2, translation);
    }

    //LAB_80110720
    return obj;
  }

  @Method(0x80110740L)
  public static long FUN_80110740(final EffectManagerData6c s2, final BttlScriptData6cSub34 s1) {
    s1._18.add(s1._24);
    s1._0c.add(s1._18);

    if(s1.scriptIndex_30.get() == -1) {
      s2._10.trans_04.set(s1._0c).div(0x100);
    } else {
      //LAB_80110814
      final Ref<SVECTOR> sp0x40 = new Ref<>();
      final Ref<VECTOR> sp0x44 = new Ref<>();
      getScriptedObjectRotationAndTranslation(s1.scriptIndex_30.get(), sp0x40, sp0x44);

      final MATRIX sp0x10 = new MATRIX();
      RotMatrix_8003faf0(sp0x40.get(), sp0x10);

      final VECTOR sp0x30 = new VECTOR().set(s1._0c).div(0x100);
      s2._10.trans_04.set(ApplyMatrixLV(sp0x10, sp0x30)).add(sp0x44.get());
    }

    //LAB_801108bc
    if(s1._32.get() == -1) {
      return 0x1L;
    }

    s1._32.decr();

    if(s1._32.get() > 0) {
      //LAB_801108e0
      return 0x1L;
    }

    //LAB_801108e4
    return 0;
  }

  @Method(0x801108fcL)
  public static BttlScriptData6cSub34 FUN_801108fc(final int a0, final int scriptIndex, final int x1, final int y1, final int z1, final int x2, final int y2, final int z2) {
    final EffectManagerData6c s0 = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[a0].innerStruct_00;
    if((s0.flags_04 & 0x2) != 0) {
      FUN_800e8d04(s0, 0x1L);
    }

    //LAB_80110980
    final BttlScriptData6cSub34 s2 = FUN_800e8dd4(s0, 0x1L, 0, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80110740", EffectManagerData6c.class, BttlScriptData6cSub34.class), BiFunctionRef::new), 0x34L, BttlScriptData6cSub34::new);
    s2._0c.set(s0._10.trans_04.getX() << 8, s0._10.trans_04.getY() << 8, s0._10.trans_04.getZ() << 8);
    s2.scriptIndex_30.set(-1);
    s2._32.set((short)-1);

    final int transformedX1;
    final int transformedY1;
    final int transformedZ1;
    final int transformedX2;
    final int transformedY2;
    final int transformedZ2;
    if(scriptIndex != -1) {
      final MATRIX rotation = new MATRIX();
      RotMatrix_8003faf0(getScriptedObjectRotation(scriptIndex), rotation);
      final VECTOR sp0x38 = new VECTOR().set(x1, y1, z1);
      final VECTOR sp0x48 = new VECTOR();
      sp0x48.set(ApplyMatrixLV(rotation, sp0x38));
      transformedX1 = sp0x48.getX();
      transformedY1 = sp0x48.getY();
      transformedZ1 = sp0x48.getZ();
      sp0x38.set(x2, y2, z2);
      sp0x48.set(ApplyMatrixLV(rotation, sp0x38));
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
    s2._18.set(transformedX1, transformedY1, transformedZ1);
    s2._24.set(transformedX2, transformedY2, transformedZ2);
    return s2;
  }

  @Method(0x80110aa8L)
  public static BttlScriptData6cSub34 FUN_80110aa8(final int a0, final int scriptIndex1, final int scriptIndex2, final int a3, final int x, final int y, final int z) {
    if(a3 < 0) {
      return null;
    }

    //LAB_80110afc
    final EffectManagerData6c s2 = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[scriptIndex1].innerStruct_00;
    if((s2.flags_04 & 0x2) != 0) {
      FUN_800e8d04(s2, 0x1L);
    }

    final VECTOR sp0x18 = new VECTOR();

    //LAB_80110b38
    final BttlScriptData6cSub34 s0 = FUN_800e8dd4(s2, 0x1L, 0, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80110740", EffectManagerData6c.class, BttlScriptData6cSub34.class), BiFunctionRef::new), 0x34L, BttlScriptData6cSub34::new);
    if(scriptIndex2 == -1) {
      sp0x18.set(x, y, z)
        .sub(getScriptedObjectTranslation(scriptIndex1));
      //LAB_80110b9c
    } else if(a0 == 0) {
      //LAB_80110bc0
      sp0x18.set(x, y, z)
        .sub(getScriptedObjectTranslation(scriptIndex1))
        .add(getScriptedObjectTranslation(scriptIndex2));
    } else if(a0 == 1) {
      //LAB_80110c0c
      FUN_801105cc(sp0x18, scriptIndex2, new VECTOR().set(x, y, z));
      sp0x18
        .sub(getScriptedObjectTranslation(scriptIndex1));
    }

    //LAB_80110c6c
    s0.scriptIndex_30.set(-1);
    s0._32.set((short)a3);

    if(a3 != 0) {
      s0._0c.setX((s2._10.trans_04.getX() << 8) / a3);
      s0._0c.setY((s2._10.trans_04.getY() << 8) / a3);
      s0._0c.setZ((s2._10.trans_04.getZ() << 8) / a3);
      s0._18.setX((sp0x18.getX() << 8) / a3);
      s0._18.setY((sp0x18.getY() << 8) / a3);
      s0._18.setZ((sp0x18.getZ() << 8) / a3);
    } else {
      s0._0c.set(-1, -1, -1);
      s0._18.set(-1, -1, -1);
    }

    s0._24.set(0, 0, 0);

    //LAB_80110d04
    return s0;
  }

  @Method(0x80110d34L)
  public static BttlScriptData6cSub34 FUN_80110d34(final int a0, final int scriptIndex1, final int scriptIndex2, final int a3, final int x, final int y, final int z) {
    final EffectManagerData6c s1 = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[scriptIndex1].innerStruct_00;
    if((s1.flags_04 & 0x2) != 0) {
      FUN_800e8d04(s1, 1);
    }

    //LAB_80110db8
    final BttlScriptData6cSub34 s0 = FUN_800e8dd4(s1, 1, 0, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80110740", EffectManagerData6c.class, BttlScriptData6cSub34.class), BiFunctionRef::new), 0x34L, BttlScriptData6cSub34::new);
    s0._0c.setX(s1._10.trans_04.getX() << 8);
    s0._0c.setY(s1._10.trans_04.getY() << 8);
    s0._0c.setZ(s1._10.trans_04.getZ() << 8);
    s0.scriptIndex_30.set(-1);

    if(a3 <= 0) {
      s0._32.set((short)-1);
      s0._18.set(0, 0, 0);
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
        FUN_801105cc(sp0x18, scriptIndex2, new VECTOR().set(x, y, z));
        sp0x18
          .sub(getScriptedObjectTranslation(scriptIndex1));
      }

      //LAB_80110f38
      final VECTOR sp0x28 = new VECTOR();
      FUN_80040e10(sp0x18, sp0x28);

      s0._32.set((short)((SquareRoot0(sp0x28.getX() + sp0x28.getY() + sp0x28.getZ()) << 8) / a3));
      if(s0._32.get() == 0) {
        s0._32.set((short)1);
      }

      //LAB_80110f80
      s0._18.setX((sp0x18.getX() << 8) / s0._32.get());
      s0._18.setY((sp0x18.getY() << 8) / s0._32.get());
      s0._18.setZ((sp0x18.getZ() << 8) / s0._32.get());
    }

    //LAB_80110fec
    s0._24.set(0, 0, 0);
    return s0;
  }

  @Method(0x8011102cL)
  public static BttlScriptData6cSub34 FUN_8011102c(final int scriptIndex1, final int scriptIndex2) {
    final EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[scriptIndex1].innerStruct_00;

    if((manager.flags_04 & 0x2) != 0) {
      FUN_800e8d04(manager, 1);
    }

    //LAB_80111084
    final BttlScriptData6cSub34 effect = FUN_800e8dd4(manager, 1, 0, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80110740", EffectManagerData6c.class, BttlScriptData6cSub34.class), BiFunctionRef::new), 0x34, BttlScriptData6cSub34::new);
    final VECTOR v1 = getScriptedObjectTranslation(scriptIndex1);
    final VECTOR v2 = getScriptedObjectTranslation(scriptIndex2);
    final VECTOR sp0x18 = new VECTOR().set(v1).sub(v2);
    effect._0c.set(sp0x18);
    effect.scriptIndex_30.set(scriptIndex2);
    effect._32.set((short)-1);
    effect._18.set(0, 0, 0);
    effect._24.set(0, 0, 0);
    return effect;
  }

  @Method(0x80111154L)
  public static long FUN_80111154(final EffectManagerData6c manager, final BttlScriptData6cSub24_2 effect) {
    long a2 = 0;
    final long t0 = 0;
    if(true) throw new RuntimeException("This method is completely bugged"); //TODO

    long t1;
    long a1 = effect._10.get() / 2;
    a1 = a1 % MEMORY.ref(2, a2).offset(0xaL).getSigned();
    t1 = a1 + 1;
    t1 = t1 % MEMORY.ref(2, a2).offset(0xaL).getSigned();

    if(t1 == 0) {
      t1 = a1;
      a1 = 0;
    }

    final SVECTOR sp0x10 = new SVECTOR();

    //LAB_801111b4
    final long v0 = effect.lmb_14.deref().getAddress(); //TODO
    a2 = v0 + MEMORY.ref(4, v0).offset(0x8L).get();
    final long a3 = a2 + MEMORY.ref(4, a2).offset(0xcL).get();
    long a0;
    if(a1 == 0) {
      throw new RuntimeException("Bugged");
//      v0 = t2 + t0 * 0x14L;
//      sp10 = MEMORY.ref(2, v0).offset(0x6L).get();
//      sp10 = MEMORY.ref(2, v0).offset(0x8L).get();
//      sp10 = MEMORY.ref(2, v0).offset(0xaL).get();
    } else {
      //LAB_80111208
      a0 = a2 + MEMORY.ref(4, a2).offset(0x14L).get() + a1 * MEMORY.ref(2, a2).offset(0x8L).getSigned();
      if((MEMORY.ref(2, a3).offset(t0 * 0x4L).get() & 0x8000L) == 0) {
        a0 = a0 + 0x2L;
      }

      //LAB_80111244
      if((MEMORY.ref(2, a3).offset(t0 * 0x4L).get() & 0x4000L) == 0) {
        a0 = a0 + 0x2L;
      }

      //LAB_80111264
      if((MEMORY.ref(2, a3).offset(t0 * 0x4L).get() & 0x2000L) == 0) {
        a0 = a0 + 0x2L;
      }

      //LAB_80111280
      if((MEMORY.ref(2, a3).offset(t0 * 0x4L).get() & 0x200L) == 0) {
        sp0x10.setX((short)MEMORY.ref(2, a0).offset(0x0L).get());
        a0 = a0 + 0x2L;
      }

      //LAB_801112ac
      if((MEMORY.ref(2, a3).offset(t0 * 0x4L).get() & 0x100L) == 0) {
        sp0x10.setY((short)MEMORY.ref(2, a0).offset(0x0L).get());
        a0 = a0 + 0x2L;
      }

      //LAB_801112d8
      if((MEMORY.ref(2, a3).offset(t0 * 0x4L).get() & 0x80L) == 0) {
        sp0x10.setZ((short)MEMORY.ref(2, a0).offset(0x0L).get());
      }
    }

    //LAB_801112fc
    a0 = a2 + MEMORY.ref(4, a2).offset(0x14L).get() + t1 * MEMORY.ref(2, a2).offset(0x8L).getSigned();
    if((MEMORY.ref(2, a3).offset(t0 * 0x4L).get() & 0x8000L) == 0) {
      a0 = a0 + 0x2L;
    }

    //LAB_80111338
    if((MEMORY.ref(2, a3).offset(t0 * 0x4L).get() & 0x4000L) == 0) {
      a0 = a0 + 0x2L;
    }

    //LAB_80111358
    if((MEMORY.ref(2, a3).offset(t0 * 0x4L).get() & 0x2000L) == 0) {
      a0 = a0 + 0x2L;
    }

    //LAB_80111374
    if((MEMORY.ref(2, a3).offset(t0 * 0x4L).get() & 0x200L) == 0) {
      sp0x10.setX((short)((MEMORY.ref(2, a0).offset(0x0L).getSigned() - sp0x10.getX()) / 2));
      a0 = a0 + 0x2L;
    }

    //LAB_801113b4
    if((MEMORY.ref(2, a3).offset(t0 * 0x4L).get() & 0x100L) == 0) {
      sp0x10.setY((short)((MEMORY.ref(2, a0).offset(0x0L).getSigned() - sp0x10.getY()) / 2));
      a0 = a0 + 0x2L;
    }

    //LAB_801113f4
    if((MEMORY.ref(2, a3).offset(t0 * 0x4L).get() & 0x80L) == 0) {
      sp0x10.setZ((short)((MEMORY.ref(2, a0).offset(0x0L).getSigned() - sp0x10.getZ()) / 2));
    }

    //LAB_8011142c
    final MATRIX sp0x28 = new MATRIX();
    RotMatrix_8003faf0(effect._1c, sp0x28);
    SetRotMatrix(sp0x28);

    final VECTOR sp0x18 = new VECTOR();
    ApplyMatrix(sp0x28, sp0x10, sp0x18);
    manager._10.trans_04.add(sp0x18);
    effect._10.incr();
    return 1;
  }

  @Method(0x801114b8L)
  public static FlowControl FUN_801114b8(final RunningScript<?> script) {
    final int s2 = script.params_20[1].get();
    final int s4 = script.params_20[2].get();
    final int s3 = script.params_20[3].get();
    final EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    if((manager.flags_04 & 0x2) != 0) {
      FUN_800e8d04(manager, 1);
    }

    //LAB_80111540
    final BttlScriptData6cSub24_2 s1 = FUN_800e8dd4(manager, 1, 1, MEMORY.ref(4, getMethodAddress(Bttl_800e.class, "FUN_80111154", EffectManagerData6c.class, BttlScriptData6cSub24_2.class), BiFunctionRef::new), 0x24, BttlScriptData6cSub24_2::new);

    final MATRIX sp0x18 = new MATRIX();
    FUN_800e8594(sp0x18, manager);
    FUN_800de544(s1._1c, sp0x18);
    s1._0c.set(s3);
    s1._18.set(s4);
    if((s2 & 0xf_ff00) == 0xf_ff00) {
      s1.lmb_14.set(deffManager_800c693c.lmbs_390[s2 & 0xff]);
    } else {
      //LAB_801115b4
      s1.lmb_14.set((DeffPart.LmbType)getDeffPart(s2));
    }

    //LAB_801115c0
    s1._10.set(1);
    return FlowControl.CONTINUE;
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
    final long v0;
    long v1;
    final long a3;
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
      v1 = effects.flags_04 & 0xff00_0000;

      //LAB_80111768
      if(v1 == 0x100_0000 || v1 == 0x200_0000) {
        //LAB_80111998
        final BttlScriptData6cSub13c struct13c = (BttlScriptData6cSub13c)effects.effect_44;
        final Model124 model = struct13c.model_134;
        model.coord2_14.flg = 0;
        model.coord2_14.coord.set(sp0x10);

        //LAB_80111a0c
        final GsCOORDINATE2 coord2 = model.coord2ArrPtr_04[a2];
        GsGetLw(coord2, a0);
        coord2.flg = 0;
      } else if(v1 == 0) {
        //LAB_80111778
        final BttlScriptData6cSub5c a2_0 = (BttlScriptData6cSub5c)effects.effect_44;

        if((a2_0.lmbType_00.get() & 0x7) == 0) {
          final long v1_0 = a2_0.lmb_0c.derefAs(LmbType0.class).getAddress(); //TODO

          //LAB_801117ac
          final int a0_0 = Math.max(0, effects._10._24) % (a2_0._08.get() * 2);
          final int a1_0 = a0_0 / 2;
          a3 = v1_0 + MEMORY.ref(4, v1_0).offset(0x10L).offset(a2 * 0xcL).get();
          v0 = a3 + a1_0 * 0x14L;
          scale.set((SVECTOR)MEMORY.ref(2, v0).offset(0x0L).cast(SVECTOR::new));
          trans.set((SVECTOR)MEMORY.ref(2, v0).offset(0x6L).cast(SVECTOR::new));
          rot.set((SVECTOR)MEMORY.ref(2, v0).offset(0xcL).cast(SVECTOR::new));

          if((a0_0 & 0x1L) != 0) {
            v1 = a1_0 + 1;

            if(v1 == a2_0._08.get()) {
              v1 = 0;
            }

            //LAB_8011188c
            final long a0_1 = a3 + v1 * 0x14L;
            scale.add((SVECTOR)MEMORY.ref(2, a0_1).offset(0x0L).cast(SVECTOR::new)).div(2);
            trans.add((SVECTOR)MEMORY.ref(2, a0_1).offset(0x6L).cast(SVECTOR::new)).div(2);
          }

          //LAB_80111958
          RotMatrix_80040010(rot, transforms);
          TransMatrix(transforms, trans);
          ScaleMatrixL(transforms, scale);
          FUN_8003ec90(sp0x10, transforms, a0);
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
  public static FlowControl FUN_80111ae4(final RunningScript<?> script) {
    final VECTOR sp0x10 = new VECTOR().set(script.params_20[2].get(), script.params_20[3].get(), script.params_20[4].get());
    FUN_8011066c(script.params_20[0].get(), script.params_20[1].get(), sp0x10);
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
      final BttlScriptData6cSub34 s2 = (BttlScriptData6cSub34)FUN_800e8c84(manager, 1);

      if(s2._06.get() == 0) {
        if(s0 != -1) {
          final MATRIX sp0x20 = new MATRIX();
          RotMatrix_8003faf0(getScriptedObjectRotation(s0), sp0x20);

          VECTOR sp0x50 = ApplyMatrixLV(sp0x20, new VECTOR().set(s4, s6, s3));
          s4 = sp0x50.getX();
          s6 = sp0x50.getY();
          s3 = sp0x50.getZ();

          sp0x50 = ApplyMatrixLV(sp0x20, new VECTOR().set(s5, s7, fp));
          s5 = sp0x50.getX();
          s7 = sp0x50.getY();
          fp = sp0x50.getZ();
        }

        //LAB_80111e40
        s2._18.x.add(s4);
        s2._18.y.add(s6);
        s2._18.z.add(s3);
        s2._24.x.add(s5);
        s2._24.y.add(s7);
        s2._24.z.add(fp);
      }
    }

    //LAB_80111ea4
    return FlowControl.CONTINUE;
  }

  @Method(0x80111ed4L)
  public static FlowControl FUN_80111ed4(final RunningScript<?> script) {
    final int scriptIndex = script.params_20[0].get();
    final EffectManagerData6c data = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[scriptIndex].innerStruct_00;

    if((data.flags_04 & 0x2) != 0) {
      final BttlScriptData6cSub34 s3 = (BttlScriptData6cSub34)FUN_800e8c84(data, 0x1L);

      if(s3._32.get() != -1 && s3._06.get() == 0) {
        final VECTOR v0 = getScriptedObjectTranslation(scriptIndex);
        int s0 = s3._32.get();

        final VECTOR sp0x10 = new VECTOR().set(
          (s0 * s3._24.getX() / 2 + s3._18.getX()) * s0 + s3._0c.getX() >> 8,
          (s0 * s3._24.getY() / 2 + s3._18.getY()) * s0 + s3._0c.getY() >> 8,
          (s0 * s3._24.getZ() / 2 + s3._18.getZ()) * s0 + s3._0c.getZ() >> 8
        );

        final SVECTOR sp0x20 = new SVECTOR();
        FUN_80110120(sp0x20, v0, sp0x10);

        final MATRIX sp0x28 = new MATRIX();
        RotMatrix_80040010(sp0x20, sp0x28);

        final VECTOR sp0x48 = new VECTOR().set(
          script.params_20[2].get(),
          script.params_20[3].get(),
          script.params_20[4].get()
        );

        final VECTOR sp0x58 = ApplyMatrixLV(sp0x28, sp0x48);
        s0++;
        s3._18.x.sub(sp0x58.getX() * s0 / 2);
        s3._18.y.sub(sp0x58.getY() * s0 / 2);
        s3._18.z.sub(sp0x58.getZ() * s0 / 2);
        s3._24.add(sp0x58);
      }
    }

    //LAB_8011215c
    return FlowControl.CONTINUE;
  }

  @Method(0x80112184L)
  public static FlowControl FUN_80112184(final RunningScript<?> script) {
    FUN_80110aa8(0, script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x801121fcL)
  public static FlowControl FUN_801121fc(final RunningScript<?> script) {
    FUN_80110aa8(1, script.params_20[0].get(), script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get(), script.params_20[4].get(), script.params_20[5].get());
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
      final BttlScriptData6cSub34 v1 = (BttlScriptData6cSub34)FUN_800e8c84(a0, 1);

      if(v1._06.get() == 0) {
        sp0x10.set(v1._18);
      }
    }

    //LAB_80112430
    script.params_20[2].set(sp0x10.getX());
    script.params_20[3].set(sp0x10.getY());
    script.params_20[4].set(sp0x10.getZ());
    return FlowControl.CONTINUE;
  }

  @Method(0x80112474L)
  public static void FUN_80112474(final int a0, final int a1, final SVECTOR a2) {
    final SVECTOR s0 = getScriptedObjectRotation(a0);

    if(a1 == -1) {
      a2.set(s0);
    } else {
      //LAB_801124c8
      a2.set(s0).sub(getScriptedObjectRotation(a1)).and(0xfff);
    }

    //LAB_80112518
  }

  @Method(0x80112530L)
  public static long FUN_80112530(final int scriptIndex1, final int scriptIndex2, final SVECTOR a2) {
    final EffectManagerData6c data = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[scriptIndex1].innerStruct_00;

    if(BattleScriptDataBase.EM__.equals(data.magic_00) && (data.flags_04 & 0x4) != 0) {
      FUN_800e8d04(data, 0x2L);
    }

    //LAB_8011259c
    final SVECTOR s0 = getScriptedObjectRotation(scriptIndex1).set(a2);
    if(scriptIndex2 != -1) {
      //LAB_801125d8
      s0.add(getScriptedObjectRotation(scriptIndex2));
    }

    //LAB_8011261c
    return 0;
  }

  @Method(0x80112638L)
  public static long FUN_80112638(final EffectManagerData6c a0, final BttlScriptData6cSub34 a1) {
    a1._18.add(a1._24);
    a1._0c.add(a1._18);
    a0._10.rot_10.set(a1._0c);

    if(a1._32.get() == -1) {
      return 0x1L;
    }

    a1._32.decr();

    if(a1._32.get() > 0) {
      //LAB_801126f8
      return 0x1L;
    }

    //LAB_801126fc
    return 0;
  }

  @Method(0x80112704L)
  public static FlowControl FUN_80112704(final RunningScript<?> script) {
    final SVECTOR sp0x10 = new SVECTOR();
    FUN_80112474(script.params_20[0].get(), script.params_20[1].get(), sp0x10);
    script.params_20[2].set(sp0x10.getX());
    script.params_20[3].set(sp0x10.getY());
    script.params_20[4].set(sp0x10.getZ());
    return FlowControl.CONTINUE;
  }

  @Method(0x80112770L)
  public static FlowControl FUN_80112770(final RunningScript<?> script) {
    FUN_80112530(
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
    final MATRIX sp0x20 = new MATRIX();
    FUN_800e8594(sp0x20, (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00);

    final SVECTOR sp0x10 = new SVECTOR();
    FUN_800de544(sp0x10, sp0x20);
    script.params_20[2].set(sp0x10.getX());
    script.params_20[3].set(sp0x10.getY());
    script.params_20[4].set(sp0x10.getZ());
    return FlowControl.CONTINUE;
  }

  @Method(0x8011287cL)
  public static FlowControl FUN_8011287c(final RunningScript<?> script) {
    final SVECTOR sp0x10 = new SVECTOR();
    final MATRIX sp0x20 = new MATRIX();
    FUN_801116c4(sp0x20, script.params_20[0].get(), script.params_20[1].get());
    FUN_800de544(sp0x10, sp0x20);
    script.params_20[2].set(sp0x10.getX());
    script.params_20[3].set(sp0x10.getY());
    script.params_20[4].set(sp0x10.getZ());
    return FlowControl.CONTINUE;
  }

  @Method(0x80112900L)
  public static FlowControl FUN_80112900(final RunningScript<?> script) {
    final SVECTOR sp0x10 = new SVECTOR();
    FUN_80110228(sp0x10, getScriptedObjectTranslation(script.params_20[0].get()), getScriptedObjectTranslation(script.params_20[1].get()));

    // XZY is the correct order
    script.params_20[2].set(sp0x10.getX());
    script.params_20[3].set(sp0x10.getZ());
    script.params_20[4].set(sp0x10.getY());
    return FlowControl.CONTINUE;
  }

  @Method(0x8011299cL)
  public static FlowControl FUN_8011299c(final RunningScript<?> script) {
    final int s0 = script.params_20[1].get();

    final VECTOR sp0x10 = new VECTOR().set(script.params_20[2].get(), script.params_20[3].get(), script.params_20[4].get());
    final Ref<SVECTOR> sp0x50 = new Ref<>(new SVECTOR());
    final Ref<VECTOR> sp0x54 = new Ref<>(new VECTOR());

    getScriptedObjectRotationAndTranslation(script.params_20[0].get(), sp0x50, sp0x54);

    if(s0 != -1) {
      final Ref<SVECTOR> sp0x58 = new Ref<>(new SVECTOR());
      final Ref<VECTOR> sp0x5c = new Ref<>(new VECTOR());

      getScriptedObjectRotationAndTranslation(s0, sp0x58, sp0x5c);

      final MATRIX sp0x20 = new MATRIX();
      RotMatrix_8003faf0(sp0x58.get(), sp0x20);
      sp0x10.set(ApplyMatrixLV(sp0x20, sp0x10)).add(sp0x5c.get());
    }

    //LAB_80112a80
    FUN_80110228(sp0x50.get(), sp0x54.get(), sp0x10);
    return FlowControl.CONTINUE;
  }

  @Method(0x80112aa4L)
  public static FlowControl FUN_80112aa4(final RunningScript<?> script) {
    final EffectManagerData6c s0 = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    if((s0.flags_04 & 0x4) != 0) {
      FUN_800e8d04(s0, 0x2L);
    }

    //LAB_80112b58
    final BttlScriptData6cSub34 v0 = FUN_800e8dd4(s0, 0x2L, 0, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80112638", EffectManagerData6c.class, BttlScriptData6cSub34.class), BiFunctionRef::new), 0x34L, BttlScriptData6cSub34::new);
    v0.scriptIndex_30.set(-1);
    v0._32.set((short)-1);
    v0._0c.set(s0._10.rot_10);
    v0._18.setX(script.params_20[2].get());
    v0._18.setY(script.params_20[3].get());
    v0._18.setZ(script.params_20[4].get());
    v0._24.setX(script.params_20[5].get());
    v0._24.setY(script.params_20[6].get());
    v0._24.setZ(script.params_20[7].get());
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
      final BttlScriptData6cSub34 effect = FUN_800e8dd4(manager, 2, 0, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80112638", EffectManagerData6c.class, BttlScriptData6cSub34.class), BiFunctionRef::new), 0x34, BttlScriptData6cSub34::new);

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
      effect.scriptIndex_30.set(-1);
      effect._32.set((short)s2);
      effect._0c.set(manager._10.rot_10);
      effect._0c.set(manager._10.rot_10);
      effect._0c.set(manager._10.rot_10);
      effect._18.setX(sp18 / s2);
      effect._18.setY(sp1c / s2);
      effect._18.setZ(sp20 / s2);
      effect._24.set(0, 0, 0);
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
    final SVECTOR sp0x10 = new SVECTOR().set((short)script.params_20[1].get(), (short)script.params_20[2].get(), (short)script.params_20[3].get());
    final MATRIX sp0x18 = new MATRIX();
    RotMatrix_8003fd80(sp0x10, sp0x18);
    FUN_800de544(sp0x10, sp0x18);
    script.params_20[4].set(sp0x10.getX());
    script.params_20[5].set(sp0x10.getY());
    script.params_20[6].set(sp0x10.getZ());
    return FlowControl.CONTINUE;
  }

  @Method(0x80113624L)
  public static SVECTOR FUN_80113624(final int scriptIndex, final int a1, final SVECTOR a2) {
    ScriptState<?> a3 = scriptStatePtrArr_800bc1c0[scriptIndex];
    BattleScriptDataBase t0 = (BattleScriptDataBase)a3.innerStruct_00;

    final SVECTOR t1;
    if(BattleScriptDataBase.EM__.equals(t0.magic_00)) {
      t1 = ((EffectManagerData6c)t0)._10.scale_16;
    } else {
      //LAB_80113660
      t1 = new SVECTOR().set(((BattleObject27c)t0).model_148.scaleVector_fc);
    }

    //LAB_801136a0
    if(a1 == -1) {
      a2.set(t1);
    } else {
      //LAB_801136d0
      a3 = scriptStatePtrArr_800bc1c0[scriptIndex];
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
      a2.setX((short)((t1.getX() << 12) / svec.getX()));
      a2.setY((short)((t1.getY() << 12) / svec.getY()));
      a2.setZ((short)((t1.getZ() << 12) / svec.getZ()));
    }

    //LAB_801137ec
    return a2;
  }

  @Method(0x801137f8L)
  public static SVECTOR FUN_801137f8(final int scriptIndex1, final int scriptIndex2, final SVECTOR a2) {
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
  public static FlowControl FUN_80113964(final RunningScript<?> script) {
    final SVECTOR sp0x10 = new SVECTOR();
    FUN_80113624(script.params_20[0].get(), script.params_20[1].get(), sp0x10);
    script.params_20[2].set(sp0x10.getX());
    script.params_20[3].set(sp0x10.getY());
    script.params_20[4].set(sp0x10.getZ());
    return FlowControl.CONTINUE;
  }

  @Method(0x801139d0L)
  public static FlowControl FUN_801139d0(final RunningScript<?> script) {
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

  @Method(0x80113ba0L)
  public static long FUN_80113ba0(final EffectManagerData6c data, final BttlScriptData6cSub34 sub) {
    sub._18.add(sub._24);
    sub._0c.add(sub._18);
    data._10.scale_16.set(sub._0c);

    if(sub._32.get() == -1) {
      return 0x1L;
    }

    sub._32.decr();
    if(sub._32.get() > 0) {
      //LAB_80113c60
      return 0x1L;
    }

    //LAB_80113c64
    return 0;
  }

  @Method(0x80113c6cL)
  public static FlowControl FUN_80113c6c(final RunningScript<?> script) {
    final EffectManagerData6c s0 = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

    if((s0.flags_04 & 0x8) != 0) {
      FUN_800e8d04(s0, 0x3L);
    }

    //LAB_80113d20
    final BttlScriptData6cSub34 v0 = FUN_800e8dd4(s0, 0x3L, 0, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80113ba0", EffectManagerData6c.class, BttlScriptData6cSub34.class), BiFunctionRef::new), 0x34L, BttlScriptData6cSub34::new);
    v0.scriptIndex_30.set(-1);
    v0._32.set((short)-1);
    v0._0c.set(s0._10.scale_16);
    v0._18.set(script.params_20[1].get(), script.params_20[2].get(), script.params_20[3].get());
    v0._24.set(script.params_20[4].get(), script.params_20[5].get(), script.params_20[6].get());
    return FlowControl.CONTINUE;
  }

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
      final BttlScriptData6cSub34 s0 = FUN_800e8dd4(s1, 0x3L, 0, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80113ba0", EffectManagerData6c.class, BttlScriptData6cSub34.class), BiFunctionRef::new), 0x34L, BttlScriptData6cSub34::new);

      final VECTOR sp0x18 = new VECTOR().set(s4, s5, s6);
      if(a0 == 0) {
        //LAB_80113eac
        final SVECTOR sp0x28 = new SVECTOR();
        FUN_801137f8(s7, s3, sp0x28);
        sp0x18.sub(sp0x28);
      } else if(a0 == 1) {
        //LAB_80113ee8
        if(s3 != -1) {
          //LAB_80113f04
          final SVECTOR sp0x28 = new SVECTOR();
          FUN_80113624(s3, -1, sp0x28);

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
      s0.scriptIndex_30.set(-1);
      s0._32.set((short)s2);
      s0._0c.set(s1._10.scale_16);

      if(s2 != 0) {
        s0._18.set(sp0x18).div(s2);
      } else {
        s0._18.set(-1, -1, -1);
      }

      s0._24.set(0, 0, 0);
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

  @Method(0x8011441cL)
  public static long FUN_8011441c(final int scriptIndex1, final int scriptIndex2, final SVECTOR a2) {
    final BattleScriptDataBase data1 = (BattleScriptDataBase)scriptStatePtrArr_800bc1c0[scriptIndex1].innerStruct_00;
    final SVECTOR svec1;
    if(BattleScriptDataBase.EM__.equals(data1.magic_00)) {
      svec1 = ((EffectManagerData6c)data1)._10.colour_1c;
    } else {
      svec1 = _800fb94c;
    }

    //LAB_80114480
    if(scriptIndex2 == -1) {
      a2.set(svec1);
    } else {
      //LAB_801144b0
      final BattleScriptDataBase data2 = (BattleScriptDataBase)scriptStatePtrArr_800bc1c0[scriptIndex2].innerStruct_00;
      final SVECTOR svec2;
      if(BattleScriptDataBase.EM__.equals(data2.magic_00)) {
        svec2 = ((EffectManagerData6c)data2)._10.colour_1c;
      } else {
        svec2 = _800fb94c;
      }

      //LAB_801144e4
      a2.set(svec1).sub(svec2);
    }

    //LAB_80114520
    return 0;
  }

  @Method(0x8011452cL)
  public static FlowControl FUN_8011452c(final RunningScript<?> script) {
    final SVECTOR sp0x10 = new SVECTOR();
    FUN_8011441c(script.params_20[0].get(), script.params_20[1].get(), sp0x10);
    script.params_20[2].set(sp0x10.getX());
    script.params_20[3].set(sp0x10.getY());
    script.params_20[4].set(sp0x10.getZ());
    return FlowControl.CONTINUE;
  }

  @Method(0x80114598L)
  public static FlowControl FUN_80114598(final RunningScript<?> script) {
    BattleScriptDataBase a1 = (BattleScriptDataBase)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;

    final SVECTOR a3;
    if(BattleScriptDataBase.EM__.equals(a1.magic_00)) {
      a3 = ((EffectManagerData6c)a1)._10.colour_1c;
    } else {
      a3 = new SVECTOR().set(_800fb94c);
    }

    //LAB_80114614
    if(script.params_20[1].get() == -1) {
      a3.setX((short)script.params_20[2].get());
      a3.setY((short)script.params_20[3].get());
      a3.setZ((short)script.params_20[4].get());
    } else {
      //LAB_80114668
      a1 = (BattleScriptDataBase)scriptStatePtrArr_800bc1c0[script.params_20[1].get()].innerStruct_00;

      final SVECTOR a2;
      if(BattleScriptDataBase.EM__.equals(a1.magic_00)) {
        a2 = ((EffectManagerData6c)a1)._10.colour_1c;
      } else {
        a2 = _800fb94c;
      }

      //LAB_8011469c
      a3.setX((short)(script.params_20[2].get() + a2.getX()));
      a3.setY((short)(script.params_20[3].get() + a2.getY()));
      a3.setZ((short)(script.params_20[4].get() + a2.getZ()));
    }

    //LAB_801146f0
    return FlowControl.CONTINUE;
  }

  @Method(0x801146fcL)
  public static long FUN_801146fc(final EffectManagerData6c a0, final BttlScriptData6cSub34 a1) {
    a1._18.add(a1._24);
    a1._0c.add(a1._18);
    a0._10.colour_1c.setX((short)(a1._0c.getX() >> 8 & 0xff));
    a0._10.colour_1c.setY((short)(a1._0c.getY() >> 8 & 0xff));
    a0._10.colour_1c.setZ((short)(a1._0c.getZ() >> 8 & 0xff));

    if(a1._32.get() != -1) {
      a1._32.decr();

      if(a1._32.get() <= 0) {
        return 0;
      }
    }

    //LAB_801147bc
    //LAB_801147c0
    return 0x1L;
  }

  @Method(0x801147c8L)
  public static FlowControl FUN_801147c8(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x80114920L)
  public static FlowControl FUN_80114920(final RunningScript<?> script) {
    final int scriptIndex1 = script.params_20[0].get();
    final int scriptIndex2 = script.params_20[1].get();
    final int scale = script.params_20[2].get();
    final VECTOR vec = new VECTOR().set(
      script.params_20[3].get(),
      script.params_20[4].get(),
      script.params_20[5].get()
    );

    if(scale >= 0) {
      final EffectManagerData6c s2 = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[scriptIndex1].innerStruct_00;
      if((s2.flags_04 & 0x10) != 0) {
        FUN_800e8d04(s2, 0x4L);
      }

      //LAB_801149d0
      final BttlScriptData6cSub34 s0 = FUN_800e8dd4(s2, 0x4L, 0, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_801146fc", EffectManagerData6c.class, BttlScriptData6cSub34.class), BiFunctionRef::new), 0x34L, BttlScriptData6cSub34::new);
      final SVECTOR sp0x28 = new SVECTOR();
      FUN_8011441c(scriptIndex1, scriptIndex2, sp0x28);
      s0.scriptIndex_30.set(-1);
      s0._32.set((short)scale);
      s0._0c.set(s2._10.colour_1c).mul(0x100);
      s0._18.set(vec).sub(sp0x28).mul(0x100).div(scale);
      s0._24.set(0, 0, 0);
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
    final BttlScriptData6cSub1c v0 = FUN_800e8dd4(state, a1 + 0x5L, 0, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80114d98", EffectManagerData6c.class, BttlScriptData6cSub1c.class), BiFunctionRef::new), 0x1cL, BttlScriptData6cSub1c::new);

    final int val = switch(a1) {
      case 0 -> state._10._24;
      case 1 -> state._10._28;
      case 2 -> state._10._2c;
      case 3 -> state._10._30;
      default -> throw new RuntimeException("Invalid value (I think) " + a1);
    };

    MEMORY.ref(4, v0.getAddress()).offset(0x0cL).setu(val << 8);
    MEMORY.ref(4, v0.getAddress()).offset(0x10L).setu(a2);
    MEMORY.ref(4, v0.getAddress()).offset(0x14L).setu(a3);
    MEMORY.ref(1, v0.getAddress()).offset(0x18L).setu(-0x1L);
    MEMORY.ref(2, v0.getAddress()).offset(0x1aL).setu(-0x1L);
  }

  /** TODO this method advances animation frames */
  @Method(0x80114d98L)
  public static long FUN_80114d98(final EffectManagerData6c a0, final BttlScriptData6cSub1c a1) {
    //TODO
    MEMORY.ref(4, a1.getAddress()).offset(0x10L).addu(MEMORY.ref(4, a1.getAddress()).offset(0x14L).get());
    MEMORY.ref(4, a1.getAddress()).offset(0xcL).addu(MEMORY.ref(4, a1.getAddress()).offset(0x10L).get());

    switch((int)MEMORY.ref(1, a1.getAddress()).offset(0x5L).getSigned() - 5) {
      case 0 -> a0._10._24 = (int)MEMORY.ref(4, a1.getAddress()).offset(0xcL).getSigned() >> 8;
      case 1 -> a0._10._28 = (int)MEMORY.ref(4, a1.getAddress()).offset(0xcL).getSigned() >> 8;
      case 2 -> a0._10._2c = (int)MEMORY.ref(4, a1.getAddress()).offset(0xcL).getSigned() >> 8;
      case 3 -> a0._10._30 = (int)MEMORY.ref(4, a1.getAddress()).offset(0xcL).getSigned() >> 8;
      default -> throw new RuntimeException("Invalid value (I think) " + (MEMORY.ref(1, a1.getAddress()).offset(0x5L).getSigned() - 5));
    }

    if(MEMORY.ref(2, a1.getAddress()).offset(0x1aL).getSigned() == -1) {
      return 1;
    }

    MEMORY.ref(2, a1.getAddress()).offset(0x1aL).subu(0x1L);
    if(MEMORY.ref(2, a1.getAddress()).offset(0x1aL).getSigned() > 0) {
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
    final long v0 = FUN_800e8dd4(s0, s1 + 0x5L, 0, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80114d98", EffectManagerData6c.class, BttlScriptData6cSub1c.class), BiFunctionRef::new), 0x1cL, BttlScriptData6cSub1c::new).getAddress(); //TODO

    final int val = switch(s1) {
      case 0 -> s0._10._24;
      case 1 -> s0._10._28;
      case 2 -> s0._10._2c;
      case 3 -> s0._10._30;
      default -> throw new RuntimeException("Invalid value (I think) " + s1);
    };

    MEMORY.ref(4, v0).offset(0x0cL).setu(val << 8);
    MEMORY.ref(4, v0).offset(0x10L).setu((int)(s3 * 0x100 - MEMORY.ref(4, v0).offset(0xcL).get()) / s2);
    MEMORY.ref(4, v0).offset(0x14L).setu(0);
    MEMORY.ref(1, v0).offset(0x18L).setu(-0x1L);
    MEMORY.ref(2, v0).offset(0x1aL).setu(s2);
    return FlowControl.CONTINUE;
  }

  @Method(0x80115168L)
  public static FlowControl FUN_80115168(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x80115288L)
  public static long FUN_80115288(final EffectManagerData6c a0, final BttlScriptData6cSub1c a1) {
    MEMORY.ref(2, a1.getAddress()).offset(0x1aL).subu(0x1L); //TODO

    //LAB_801152a8
    return MEMORY.ref(2, a1.getAddress()).offset(0x1aL).getSigned() > 0 ? 0x1L : 0x2L; //TODO
  }

  @Method(0x801152b0L)
  public static FlowControl FUN_801152b0(final RunningScript<?> script) {
    final BttlScriptData6cSub1c v0 = FUN_800e8dd4((EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00, 0, 0, MEMORY.ref(4, getMethodAddress(SEffe.class, "FUN_80115288", EffectManagerData6c.class, BttlScriptData6cSub1c.class), BiFunctionRef::new), 0x1cL, BttlScriptData6cSub1c::new);
    MEMORY.ref(2, v0.getAddress()).offset(0x1aL).setu(script.params_20[1].get()); //TODO
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
    ((EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00)._10.z_22 = script.params_20[1].get();
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
      final Memory.TemporaryReservation sp0x50tmp = MEMORY.temp(0x6c);
      final EffectManagerData6c sp0x50 = new EffectManagerData6c();

      sp0x50._10.trans_04.set(0, 0, 0);
      sp0x50._10.rot_10.set((short)0, (short)0, (short)0);
      sp0x50._10.scale_16.set((short)0x1000, (short)0x1000, (short)0x1000);

      sp0x50.scriptIndex_0c = scriptIndex;
      sp0x50.coord2Index_0d = coord2Index;

      final MATRIX sp0xc0 = new MATRIX();
      FUN_800e8594(sp0xc0, sp0x50);

      final SVECTOR sp0xe0 = new SVECTOR();
      final SVECTOR sp0xe8 = new SVECTOR();
      FUN_800de618(sp0xe0, sp0xe8, sp0xc0);
      sp0xe0.negate();
      RotMatrix_80040010(sp0xe0, sp0xc0);

      final VECTOR sp0xf0 = new VECTOR().set(0x100_0000, 0x100_0000, 0x100_0000).div(sp0xe8);
      ScaleMatrixL(sp0xc0, sp0xf0);
      FUN_80021de4(sp0xc0, sp0x10, sp0x30);

      final VECTOR sp0x100 = new VECTOR().set(sp0x10.transfer).sub(sp0xc0.transfer);
      sp0x30.transfer.set(ApplyMatrixLV(sp0xc0, sp0x100));

      sp0x50tmp.release();
    }

    //LAB_801159cc
    FUN_800de618(manager._10.rot_10, manager._10.scale_16, sp0x30);
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
      v0 = script.params_20[0].get() | v1._20;
    } else {
      v0 = script.params_20[0].get() & v1._20;
    }

    //LAB_80115b20
    v1._20 = v0;
    return FlowControl.CONTINUE;
  }

  @Method(0x80115b2cL)
  public static void FUN_80115b2c(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final int s0 = state.storage_44[8];
    final int s1 = state.storage_44[9];

    if(s1 == s0) {
      state.deallocateWithChildren();
    } else {
      //LAB_80115b80
      applyScreenDarkening(s0);

      //LAB_80115bd4
      if(s1 < s0) {
        state.storage_44[8]--;
      } else {
        //LAB_80115bb4
        state.storage_44[8]++;
      }
    }

    //LAB_80115bd8
  }

  @Method(0x80115bf0L)
  public static void FUN_80115bf0(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    applyScreenDarkening(state.storage_44[9]);
  }

  @Method(0x80115c2cL)
  public static void FUN_80115c2c(final int a0, final int a1) {
    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      deffManager_800c693c.scriptState_1c,
      0,
      SEffe::FUN_80115b2c,
      null,
      SEffe::FUN_80115bf0,
      null
    );

    state.storage_44[8] = a0;
    state.storage_44[9] = a1;
  }

  @Method(0x80115cacL)
  public static long FUN_80115cac(final int a0) {
    final int _00;
    final int _02;
    final int _04;

    if(currentStage_800c66a4.get() < 71 || currentStage_800c66a4.get() > 78) {
      //LAB_80115d14
      //LAB_80115d2c
      for(int i = 0; ; i++) {
        if(stageIndices_800fb064.offset(i).get() == 0xffL) {
          //LAB_80115cd8
          final DeffManager7cc.Struct08 v0 = deffManager_800c693c._00;
          _00 = v0._00;
          _02 = v0._02;
          _04 = v0._04;
          break;
        }

        if(stageIndices_800fb064.offset(i).get() == currentStage_800c66a4.get()) {
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
          FUN_80115c2c(6, 16);
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
          FUN_80115c2c(16, 6);
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
    FUN_80115cac(script.params_20[0].get());
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
        v0 = _800fb0ec.offset(((BattleObject27c)a0).model_148.colourMap_9d * 0x4L).get();
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
            final BttlScriptData6cSub10_2 effect = (BttlScriptData6cSub10_2)manager.effect_44;
            u = effect.metrics_04.u_00.get();
            v = effect.metrics_04.v_02.get();
            w = effect.metrics_04.w_04.get();
            h = effect.metrics_04.h_05.get();
          } else {
            throw new RuntimeException("Invalid state");
          }
        } else {
          //LAB_80115fd8
          v0 = _800fb0ec.offset(((BttlScriptData6cSub13c)manager.effect_44).model_134.colourMap_9d * 0x4L).get();
          u = (int)((v0 & 0xf) << 6);
          v = (int)((v0 & 0x10) << 4);
          w = 256;
          h = 256;
        }
      }
    } else if(type == 0x200_0000) {
      //LAB_80116098
      final DeffPart.AnimatedTmdType animatedTmdType = (DeffPart.AnimatedTmdType)getDeffPart(a2);
      final DeffPart.TextureInfo textureInfo = animatedTmdType.textureInfo_08.deref().get(0);
      u = textureInfo.vramPos_00.x.get();
      v = textureInfo.vramPos_00.y.get();
      w = 256;
      h = 256;
    } else if(type == 0x100_0000 || type == 0x300_0000) {
      //LAB_801160c0
      //LAB_801160d4
      final DeffPart.TmdType tmdType = (DeffPart.TmdType)getDeffPart(a2);
      final DeffPart.TextureInfo textureInfo = tmdType.textureInfo_08.deref().get(s0);
      u = textureInfo.vramPos_00.x.get();
      v = textureInfo.vramPos_00.y.get();
      w = textureInfo.vramPos_00.w.get() * 4;
      h = textureInfo.vramPos_00.h.get();
      //LAB_80115f34
    } else if(type == 0x400_0000) {
      //LAB_801160f4
      final Memory.TemporaryReservation sp0x10tmp = MEMORY.temp(0xc);
      final AttackHitFlashEffect0c sp0x10 = new AttackHitFlashEffect0c(sp0x10tmp.get());
      FUN_800e95f0(sp0x10, a2 & 0xff_ffff);
      u = sp0x10.metrics_04.u_00.get();
      v = sp0x10.metrics_04.v_02.get();
      w = sp0x10.metrics_04.w_04.get();
      h = sp0x10.metrics_04.h_05.get();
      sp0x10tmp.release();
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

  @Method(0x8011619cL)
  public static void FUN_8011619c(final EffectManagerData6c manager, final BttlScriptData6cSub5c effect, final int deffFlags, final MATRIX matrix) {
    final MATRIX sp0x10 = new MATRIX();
    RotMatrix_80040010(manager._10.rot_10, sp0x10);
    TransMatrix(sp0x10, manager._10.trans_04);
    ScaleVectorL_SVEC(sp0x10, manager._10.scale_16);
    MulMatrix0(matrix, sp0x10, sp0x10);
    final int s0 = manager._10._28;
    final VECTOR sp0x30 = new VECTOR().set(s0, s0, s0);
    ScaleMatrixL(sp0x10, sp0x30);
    manager._10.scale_16.setX((short)(manager._10.scale_16.getX() * s0 / 0x1000));
    manager._10.scale_16.setY((short)(manager._10.scale_16.getY() * s0 / 0x1000));
    manager._10.scale_16.setZ((short)(manager._10.scale_16.getZ() * s0 / 0x1000));

    final int type = deffFlags & 0xff00_0000;
    if(type == 0x300_0000) {
      //LAB_80116708
      final TmdObjTable tmdObjTable;
      if(effect.deffTmdFlags_48.get() == deffFlags) {
        tmdObjTable = effect.deffTmdObjTable_4c.deref();
      } else {
        //LAB_80116724
        if((deffFlags & 0xf_ff00) == 0xf_ff00) {
          tmdObjTable = deffManager_800c693c.tmds_2f8[deffFlags & 0xff];
        } else {
          //LAB_80116750
          tmdObjTable = ((DeffPart.TmdType)getDeffPart(deffFlags | 0x300_0000)).tmd_0c.deref().tmdPtr_00.deref().tmd.objTable.get(0);
        }

        //LAB_8011676c
        effect.deffTmdFlags_48.set(deffFlags);
        effect.deffTmdObjTable_4c.set(tmdObjTable);
      }

      //LAB_80116778
      FUN_800de3f4(tmdObjTable, manager._10, sp0x10);
    } else if(type == 0x400_0000) {
      if(effect.deffSpriteFlags_50.get() != deffFlags) {
        //LAB_801162e8
        final Memory.TemporaryReservation tmp = MEMORY.temp(0xc);
        final AttackHitFlashEffect0c sp0x48 = new AttackHitFlashEffect0c(tmp.get());

        FUN_800e95f0(sp0x48, deffFlags);
        effect.metrics_54.u_00.set(sp0x48.metrics_04.u_00.get());
        effect.metrics_54.v_02.set(sp0x48.metrics_04.v_02.get());
        effect.metrics_54.w_04.set(sp0x48.metrics_04.w_04.get());
        effect.metrics_54.h_05.set(sp0x48.metrics_04.h_05.get());
        effect.metrics_54.clut_06.set(sp0x48.metrics_04.clut_06.get());

        tmp.release();

        effect.deffSpriteFlags_50.set(deffFlags);
      }

      final int u = (effect.metrics_54.u_00.get() & 0x3f) * 4;
      final int v = effect.metrics_54.v_02.get() & 0xff;
      final int w = effect.metrics_54.w_04.get() & 0xff;
      final int h = effect.metrics_54.h_05.get() & 0xff;
      final int clut = effect.metrics_54.clut_06.get();

      //LAB_8011633c
      final SVECTOR sp0x58 = new SVECTOR();
      final MATRIX sp0x60 = new MATRIX();
      final DVECTOR sp0x80 = new DVECTOR();
      MulMatrix0(worldToScreenMatrix_800c3548, sp0x10, sp0x60);
      setRotTransMatrix(sp0x60);

      final int z = perspectiveTransform(sp0x58, sp0x80, null, null);
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
          .vramPos(effect.metrics_54.u_00.get() & 0x3ff, (effect.metrics_54.v_02.get() & 0x100) != 0 ? 256 : 0)
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
      FUN_800de618(manager._10.rot_10, manager._10.scale_16, sp0x10);

      final int oldScriptIndex = manager2.scriptIndex_0c;
      final int oldCoord2Index = manager2.coord2Index_0d;
      manager2.scriptIndex_0c = manager.myScriptState_0e.index;
      manager2.coord2Index_0d = -1;
      final short r = manager2._10.colour_1c.getX();
      final short g = manager2._10.colour_1c.getY();
      final short b = manager2._10.colour_1c.getZ();
      manager2._10.colour_1c.setX((short)(manager._10.colour_1c.getX() * manager2._10.colour_1c.getX() / 128));
      manager2._10.colour_1c.setY((short)(manager._10.colour_1c.getX() * manager2._10.colour_1c.getY() / 128));
      manager2._10.colour_1c.setZ((short)(manager._10.colour_1c.getX() * manager2._10.colour_1c.getZ() / 128));
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
    final long s3 = effect.lmb_0c.getPointer(); //TODO
    final int s6 = a2 / 0x2000;
    final int v1 = s6 + 1;
    final int s0 = a2 & 0x1fff;
    final int s1 = 0x2000 - s0;
    final int fp = v1 % (short)MEMORY.ref(2, s3).offset(0xcL).getSigned();

    //LAB_80116960
    for(int i = 0; i < MEMORY.ref(4, s3).offset(0x4L).get(); i++) {
      long a0 = s3 + MEMORY.ref(4, s3).offset(0x8L).offset(i * 0xcL).offset(0x8L).get();
      final long a1 = a0 + s6 * 0x14;

      if(effect._14.get((int)MEMORY.ref(2, s3).offset(0x8L).offset(i * 0xcL).getSigned()).get() != 0) {
        a0 = a0 + fp * 0x14;
        manager._10.rot_10.setX((short)MEMORY.ref(2, a1).offset(0x0cL).get());
        manager._10.rot_10.setY((short)MEMORY.ref(2, a1).offset(0x0eL).get());
        manager._10.rot_10.setZ((short)MEMORY.ref(2, a1).offset(0x10L).get());
        manager._10.trans_04.setX((int)((MEMORY.ref(2, a1).offset(0x06L).getSigned() * s1 + MEMORY.ref(2, a0).offset(0x06L).getSigned() * s0) / 0x2000));
        manager._10.trans_04.setY((int)((MEMORY.ref(2, a1).offset(0x08L).getSigned() * s1 + MEMORY.ref(2, a0).offset(0x08L).getSigned() * s0) / 0x2000));
        manager._10.trans_04.setZ((int)((MEMORY.ref(2, a1).offset(0x0aL).getSigned() * s1 + MEMORY.ref(2, a0).offset(0x0aL).getSigned() * s0) / 0x2000));
        manager._10.scale_16.setX((short)((MEMORY.ref(2, a1).offset(0x00L).getSigned() * s1 + MEMORY.ref(2, a0).offset(0x00L).getSigned() * s0) / 0x2000));
        manager._10.scale_16.setY((short)((MEMORY.ref(2, a1).offset(0x02L).getSigned() * s1 + MEMORY.ref(2, a0).offset(0x02L).getSigned() * s0) / 0x2000));
        manager._10.scale_16.setZ((short)((MEMORY.ref(2, a1).offset(0x04L).getSigned() * s1 + MEMORY.ref(2, a0).offset(0x04L).getSigned() * s0) / 0x2000));
        FUN_8011619c(manager, effect, effect._14.get((int)MEMORY.ref(2, s3).offset(0x8L).offset(i * 0xcL).getSigned()).get(), matrix);
      }
    }
  }

  @Method(0x80116b7cL)
  public static void processLmbType1(final EffectManagerData6c manager, final BttlScriptData6cSub5c effect, final int t0, final MATRIX matrix) {
    final long s3 = effect.lmb_0c.getPointer(); //TODO
    final long fp = s3 + MEMORY.ref(4, s3).offset(0xcL).get();
    long a0 = t0 / 0x2000;
    long a1 = s3 + MEMORY.ref(4, s3).offset(0x10L).get();
    long s0 = t0 & 0x1fffL;
    long s5 = (a0 + 1) % MEMORY.ref(2, s3).offset(0xaL).getSigned();
    final long v0 = effect._04.get();
    final long s7 = effect.ptr_10.getPointer(); //TODO
    long s2;
    long s1;
    if(v0 != t0) {
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
        memcpy(s7, a1, (int)(MEMORY.ref(4, s3).offset(0x4L).get() * 0x14));
      } else {
        //LAB_80116c50
        final long t1 = (a0 - 1) * MEMORY.ref(2, s3).offset(0x8L).getSigned();
        a0 = s3 + MEMORY.ref(4, s3).offset(0x14L).get() + t1;
        a1 = s7;
        long v1 = fp;

        //LAB_80116c80
        for(s2 = 0; s2 < MEMORY.ref(4, s3).offset(0x4L).get(); s2++) {
          if((MEMORY.ref(2, v1).offset(0x0L).get() & 0x8000) == 0) {
            MEMORY.ref(2, a1).offset(0x0L).setu(MEMORY.ref(2, a0).offset(0x0L).get());
            a0 = a0 + 0x2L;
          }

          //LAB_80116ca0
          if((MEMORY.ref(2, v1).offset(0x0L).get() & 0x4000) == 0) {
            MEMORY.ref(2, a1).offset(0x2L).setu(MEMORY.ref(2, a0).offset(0x0L).get());
            a0 = a0 + 0x2L;
          }

          //LAB_80116cc0
          if((MEMORY.ref(2, v1).offset(0x0L).get() & 0x2000) == 0) {
            MEMORY.ref(2, a1).offset(0x4L).setu(MEMORY.ref(2, a0).offset(0x0L).get());
            a0 = a0 + 0x2L;
          }

          //LAB_80116ce0
          if((MEMORY.ref(2, v1).offset(0x0L).get() & 0x1000) == 0) {
            MEMORY.ref(2, a1).offset(0x6L).setu(MEMORY.ref(2, a0).offset(0x0L).get());
            a0 = a0 + 0x2L;
          }

          //LAB_80116d00
          if((MEMORY.ref(2, v1).offset(0x0L).get() & 0x800) == 0) {
            MEMORY.ref(2, a1).offset(0x8L).setu(MEMORY.ref(2, a0).offset(0x0L).get());
            a0 = a0 + 0x2L;
          }

          //LAB_80116d20
          if((MEMORY.ref(2, v1).offset(0x0L).get() & 0x400) == 0) {
            MEMORY.ref(2, a1).offset(0xaL).setu(MEMORY.ref(2, a0).offset(0x0L).get());
            a0 = a0 + 0x2L;
          }

          //LAB_80116d40
          if((MEMORY.ref(2, v1).offset(0x0L).get() & 0x200) == 0) {
            MEMORY.ref(2, a1).offset(0xcL).setu(MEMORY.ref(2, a0).offset(0x0L).get());
            a0 = a0 + 0x2L;
          }

          //LAB_80116d60
          if((MEMORY.ref(2, v1).offset(0x0L).get() & 0x100) == 0) {
            MEMORY.ref(2, a1).offset(0xeL).setu(MEMORY.ref(2, a0).offset(0x0L).get());
            a0 = a0 + 0x2L;
          }

          //LAB_80116d80
          if((MEMORY.ref(2, v1).offset(0x0L).get() & 0x80) == 0) {
            MEMORY.ref(2, a1).offset(0x10L).setu(MEMORY.ref(2, a0).offset(0x0L).get());
            a0 = a0 + 0x2L;
          }

          //LAB_80116da0
          a1 = a1 + 0x14L;
          v1 = v1 + 0x4L;
        }
      }

      //LAB_80116db8
      a0 = s3 + MEMORY.ref(4, s3).offset(0x14L).get() + (s5 - 1) * MEMORY.ref(2, s3).offset(0x8L).getSigned();
      a1 = s7;
      long a2 = fp;

      //LAB_80116de8
      for(s2 = 0; s2 < MEMORY.ref(4, s3).offset(0x4L).get(); s2++) {
        if((MEMORY.ref(2, a2).offset(0x0L).get() & 0x8000) == 0) {
          MEMORY.ref(2, a1).offset(0x0L).setu((MEMORY.ref(2, a1).offset(0x0L).getSigned() * s1 + MEMORY.ref(2, a0).offset(0x0L).getSigned() * s0) / 0x2000);
          a0 = a0 + 0x2L;
        }

        //LAB_80116e34
        if((MEMORY.ref(2, a2).offset(0x0L).get() & 0x4000) == 0) {
          MEMORY.ref(2, a1).offset(0x2L).setu((MEMORY.ref(2, a1).offset(0x2L).getSigned() * s1 + MEMORY.ref(2, a0).offset(0x0L).getSigned() * s0) / 0x2000);
          a0 = a0 + 0x2L;
        }

        //LAB_80116e80
        if((MEMORY.ref(2, a2).offset(0x0L).get() & 0x2000) == 0) {
          MEMORY.ref(2, a1).offset(0x4L).setu((MEMORY.ref(2, a1).offset(0x4L).getSigned() * s1 + MEMORY.ref(2, a0).offset(0x0L).getSigned() * s0) / 0x2000);
          a0 = a0 + 0x2L;
        }

        //LAB_80116ecc
        if((MEMORY.ref(2, a2).offset(0x0L).get() & 0x1000) == 0) {
          MEMORY.ref(2, a1).offset(0x6L).setu((MEMORY.ref(2, a1).offset(0x6L).getSigned() * s1 + MEMORY.ref(2, a0).offset(0x0L).getSigned() * s0) / 0x2000);
          a0 = a0 + 0x2L;
        }

        //LAB_80116f18
        if((MEMORY.ref(2, a2).offset(0x0L).get() & 0x800) == 0) {
          MEMORY.ref(2, a1).offset(0x8L).setu((MEMORY.ref(2, a1).offset(0x8L).getSigned() * s1 + MEMORY.ref(2, a0).offset(0x0L).getSigned() * s0) / 0x2000);
          a0 = a0 + 0x2L;
        }

        //LAB_80116f64
        if((MEMORY.ref(2, a2).offset(0x0L).get() & 0x400) == 0) {
          MEMORY.ref(2, a1).offset(0xaL).setu((MEMORY.ref(2, a1).offset(0xaL).getSigned() * s1 + MEMORY.ref(2, a0).offset(0x0L).getSigned() * s0) / 0x2000);
          a0 = a0 + 0x2L;
        }

        //LAB_80116fb0
        if((MEMORY.ref(2, a2).offset(0x0L).get() & 0x200L) == 0) {
          a0 = a0 + 0x2L;
        }

        //LAB_80116fc8
        if((MEMORY.ref(2, a2).offset(0x0L).get() & 0x100L) == 0) {
          a0 = a0 + 0x2L;
        }

        //LAB_80116fd4
        if((MEMORY.ref(2, a2).offset(0x0L).get() & 0x80L) == 0) {
          a0 = a0 + 0x2L;
        }

        //LAB_80116fe0
        a2 = a2 + 0x4L;
        a1 = a1 + 0x14L;
      }

      //LAB_80116ff8
      effect._04.set(t0);
    }

    //LAB_80116ffc
    s0 = s7;
    s1 = fp;

    //LAB_80117014
    for(s2 = 0; s2 < MEMORY.ref(4, s3).offset(0x4L).get(); s2++) {
      if(effect._14.get((int)MEMORY.ref(1, s1).offset(0x3L).get()).get() != 0) {
        manager._10.rot_10.setX((short)MEMORY.ref(2, s0).offset(0x0cL).get());
        manager._10.rot_10.setY((short)MEMORY.ref(2, s0).offset(0x0eL).get());
        manager._10.rot_10.setZ((short)MEMORY.ref(2, s0).offset(0x10L).get());
        manager._10.trans_04.setX((int)MEMORY.ref(2, s0).offset(0x6L).getSigned());
        manager._10.trans_04.setY((int)MEMORY.ref(2, s0).offset(0x8L).getSigned());
        manager._10.trans_04.setZ((int)MEMORY.ref(2, s0).offset(0xaL).getSigned());
        manager._10.scale_16.setX((short)MEMORY.ref(2, s0).offset(0x0L).get());
        manager._10.scale_16.setY((short)MEMORY.ref(2, s0).offset(0x2L).get());
        manager._10.scale_16.setZ((short)MEMORY.ref(2, s0).offset(0x4L).get());

        FUN_8011619c(manager, effect, effect._14.get((int)MEMORY.ref(1, s1).offset(0x3L).get()).get(), matrix);
      }

      //LAB_801170bc
      s0 = s0 + 0x14L;
      s1 = s1 + 0x4L;
    }

    //LAB_801170d4
  }

  @Method(0x80117104L)
  public static void processLmbType2(final EffectManagerData6c manager, final BttlScriptData6cSub5c effect, final int t5, final MATRIX matrix) {
    final LmbType2 lmb = effect.lmb_0c.derefAs(LmbType2.class);
    final UnboundedArrayRef<IntRef> t3 = lmb._0c.deref();
    final UnboundedArrayRef<LmbTransforms14> originalTransforms = lmb._10.deref();
    final int s6 = t5 / 0x2000;
    final int s0 = t5 & 0x1fff;
    final int s2 = 0x2000 - s0;
    final int size = lmb.count_04.get() * 0x14;
    final UnboundedArrayRef<LmbTransforms14> transformsLo = effect.ptr_10.deref();
    final UnboundedArrayRef<LmbTransforms14> transformsHi = transformsLo.slice(size);
    final int v1 = effect._04.get();
    final int fp = (s6 + 1) % lmb._0a.get();
    if(v1 != t5) {
      int s1 = v1 / 0x2000;

      if(fp == 0 && s6 == 0) {
        return;
      }

      //LAB_801171c8
      if(s6 < s1) {
        s1 = 0;
        memcpy(transformsLo.getAddress(), originalTransforms.getAddress(), size);
      }

      //LAB_801171f8
      //LAB_8011720c
      for(; s1 < s6; s1++) {
        long a0 = lmb.getAddress() + MEMORY.ref(4, lmb.getAddress()).offset(0x14L).get() + s1 * lmb._08.get(); //TODO

        //LAB_80117234
        for(int i = 0; i < lmb._08.get() * 2; i += 2) {
          final long v1_0 = _8011a048.offset(i).getAddress();
          MEMORY.ref(1, v1_0).offset(0x0L).setu(MEMORY.ref(1, a0).offset(0x0L).getSigned() >> 4);
          MEMORY.ref(1, v1_0).offset(0x1L).setu((int)MEMORY.ref(1, a0).offset(0x0L).getSigned() << 28 >> 28);
          a0++;
        }

        //LAB_80117270
        a0 = _8011a048.getAddress();

        //LAB_8011728c
        for(int i = 0; i < lmb.count_04.get(); i++) {
          final LmbTransforms14 transform = transformsLo.get(i);

          final int flags = t3.get(i).get();

          if((flags & 0xe000) != 0xe000) {
            final int shift = (int)MEMORY.ref(1, a0).offset(0x0L).getSigned() & 0xf;
            a0++;

            if((flags & 0x8000) == 0) {
              transform.scale_00.x.add((short)(MEMORY.ref(1, a0).offset(0x0L).getSigned() << shift));
              a0++;
            }

            //LAB_801172c8
            if((flags & 0x4000) == 0) {
              transform.scale_00.y.add((short)(MEMORY.ref(1, a0).offset(0x0L).getSigned() << shift));
              a0++;
            }

            //LAB_801172f0
            if((flags & 0x2000) == 0) {
              transform.scale_00.z.add((short)(MEMORY.ref(1, a0).offset(0x0L).getSigned() << shift));
              a0++;
            }
          }

          //LAB_80117310
          //LAB_80117314
          if((flags & 0x1c00) != 0x1c00) {
            final int shift = (int)MEMORY.ref(1, a0).offset(0x0L).getSigned() & 0xf;
            a0++;

            if((flags & 0x1000) == 0) {
              transform.trans_06.x.add((short)(MEMORY.ref(1, a0).offset(0x0L).getSigned() << shift));
              a0++;
            }

            //LAB_80117348
            if((flags & 0x800) == 0) {
              transform.trans_06.y.add((short)(MEMORY.ref(1, a0).offset(0x0L).getSigned() << shift));
              a0++;
            }

            //LAB_80117370
            if((flags & 0x400) == 0) {
              transform.trans_06.z.add((short)(MEMORY.ref(1, a0).offset(0x0L).getSigned() << shift));
              a0++;
            }
          }

          //LAB_80117390
          //LAB_80117394
          if((flags & 0x380) != 0x380) {
            final int shift = (int)MEMORY.ref(1, a0).offset(0x0L).getSigned() & 0xf;
            a0++;

            if((flags & 0x200) == 0) {
              transform.rot_0c.x.add((short)(MEMORY.ref(1, a0).offset(0x0L).getSigned() << shift));
              a0++;
            }

            //LAB_801173c8
            if((flags & 0x100) == 0) {
              transform.rot_0c.y.add((short)(MEMORY.ref(1, a0).offset(0x0L).getSigned() << shift));
              a0++;
            }

            //LAB_801173f0
            if((flags & 0x80) == 0) {
              transform.rot_0c.z.add((short)(MEMORY.ref(1, a0).offset(0x0L).getSigned() << shift));
              a0++;
            }
          }
        }
      }

      //LAB_80117438
      if(fp == 0) {
        //LAB_801176c0
        //LAB_801176e0
        for(int i = 0; i < lmb.count_04.get(); i++) {
          final LmbTransforms14 originalTransform = originalTransforms.get(i);
          final LmbTransforms14 transformLo = transformsLo.get(i);
          final LmbTransforms14 transformHi = transformsHi.get(i);

          final int flags = t3.get(i).get();

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
        long a0_0 = lmb.getAddress() + MEMORY.ref(4, lmb.getAddress()).offset(0x14L).get() + (fp - 1) * lmb._08.get(); //TODO

        //LAB_80117470
        for(int i = 0; i < lmb._08.get() * 2; i += 2) {
          final long v1_1 = _8011a048.offset(i).getAddress();
          MEMORY.ref(1, v1_1).offset(0x0L).setu(MEMORY.ref(1, a0_0).offset(0x0L).getSigned() >> 4);
          MEMORY.ref(1, v1_1).offset(0x1L).setu((int)MEMORY.ref(1, a0_0).offset(0x0L).getSigned() << 28 >> 28);
          a0_0++;
        }

        //LAB_801174ac
        a0_0 = _8011a048.getAddress();

        //LAB_801174d0
        for(int i = 0; i < lmb.count_04.get(); i++) {
          final LmbTransforms14 transformLo = transformsLo.get(i);
          final LmbTransforms14 transformHi = transformsHi.get(i);

          final int flags = t3.get(i).get();

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
      effect._04.set(t5);
    }

    //LAB_801178a4
    //LAB_801178c0
    for(int i = 0; i < lmb.count_04.get(); i++) {
      final LmbTransforms14 transformLo = transformsLo.get(i);
      final LmbTransforms14 transformHi = transformsHi.get(i);
      final int flags = t3.get(i).get();

      if(effect._14.get(flags >>> 24).get() != 0) {
        if(manager._10._2c != 0) {
          manager._10.rot_10.set(transformLo.rot_0c);
        } else {
          //LAB_80117914
          manager._10.rot_10.set(transformHi.rot_0c);
        }

        //LAB_80117938
        manager._10.trans_04.set(transformHi.trans_06);
        manager._10.scale_16.set(transformHi.scale_00);
        FUN_8011619c(manager, effect, effect._14.get(flags >>> 24).get(), matrix);
      }
    }

    //LAB_801179c0
  }

  @Method(0x801179f0L)
  public static void FUN_801179f0(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final int flags = manager._10.flags_00;
    final BttlScriptData6cSub5c effect = (BttlScriptData6cSub5c)manager.effect_44;
    if(flags >= 0) {
      int s1 = Math.max(0, manager._10._24) % (effect._08.get() * 2) << 12;
      tmdGp0Tpage_1f8003ec.set(flags >>> 23 & 0x60);
      zOffset_1f8003e8.set(manager._10.z_22);
      if((manager._10.flags_00 & 0x40) == 0) {
        FUN_800e61e4(manager._10.colour_1c.getX() * 0x20, manager._10.colour_1c.getY() * 0x20, manager._10.colour_1c.getZ() * 0x20);
      }

      //LAB_80117ac0
      //LAB_80117acc
      final EffectManagerData6c sp0x10 = new EffectManagerData6c();
      sp0x10.set(manager);

      final MATRIX sp0x80 = new MATRIX();
      FUN_800e8594(sp0x80, manager);

      final int type = effect.lmbType_00.get() & 0x7;
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
      final int s4 = effect._38.get();

      if(s4 >= 2) {
        final int spe4 = effect._3c.get();
        int s5;
        int s6;
        int s7;
        final int spd0;
        final int spd4;
        final int spd8;
        if((effect._34.get() & 0x4L) != 0) {
          final int v0 = effect._40.get() - 0x1000;
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
        if((effect._34.get() & 0x8L) != 0) {
          final int t4 = sp0x10._10._28 * (effect._40.get() - 0x1000);
          spdc = sp0x10._10._28 << 12;
          spe0 = t4 / s4;
        }

        //LAB_80117c88
        s1 -= spe4;

        //LAB_80117ca0
        for(int i = 1; i < s4 && s1 >= 0; i++) {
          if((effect._34.get() & 0x4L) != 0) {
            s5 = s5 + spd0;
            s6 = s6 + spd4;
            s7 = s7 + spd8;
            manager._10.colour_1c.setX((short)(s5 >> 12));
            manager._10.colour_1c.setY((short)(s6 >> 12));
            manager._10.colour_1c.setZ((short)(s7 >> 12));
          }

          //LAB_80117d1c
          if((effect._34.get() & 0x8L) != 0) {
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
      script.scriptState_04,
      0x5c,
      null,
      SEffe::FUN_801179f0,
      SEffe::FUN_80118148,
      BttlScriptData6cSub5c::new
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
    effect.lmbType_00.set(lmbType.type_04.get());
    effect._04.set(0);
    effect.lmb_0c.set(lmbType.lmb_08.deref());
    effect.ptr_10.clear();
    effect._38.set(1);
    effect._3c.set(0x1000);
    effect.deffTmdFlags_48.set(-1);
    effect.deffSpriteFlags_50.set(-1);

    //LAB_80117fc4
    for(int i = 0; i < 8; i++) {
      effect._14.get(i).set(0);
    }

    final int type = effect.lmbType_00.get() & 0x7;
    if(type == 0) {
      //LAB_80118004
      final LmbType0 lmb = effect.lmb_0c.derefAs(LmbType0.class);
      effect._08.set(lmb._0c.get());
    } else if(type == 1) {
      //LAB_80118018
      final LmbType1 lmb = effect.lmb_0c.derefAs(LmbType1.class);
      effect._08.set(lmb._0a.get());
      effect.ptr_10.setPointer(mallocHead(lmb.count_04.get() * 0x14));
      memcpy(effect.ptr_10.getPointer(), lmb._10.deref().getAddress(), lmb.count_04.get() * 0x14);
    } else if(type == 2) {
      //LAB_80118068
      final LmbType2 lmb = effect.lmb_0c.derefAs(LmbType2.class);
      effect._08.set(lmb._0a.get());
      effect.ptr_10.setPointer(mallocHead(lmb.count_04.get() * 0x28));
      final long src = lmb._10.deref().getAddress();
      final int size = lmb.count_04.get() * 0x14;
      memcpy(effect.ptr_10.getPointer(), src, size);
      memcpy(effect.ptr_10.getPointer() + size, src, size);
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

  @Method(0x80118148L)
  public static void FUN_80118148(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final BttlScriptData6cSub5c s0 = (BttlScriptData6cSub5c)manager.effect_44;

    if(!s0.ptr_10.isNull()) {
      free(s0.ptr_10.getPointer());
      s0.ptr_10.clear();
    }

    //LAB_80118198
  }

  @Method(0x801181a8L)
  public static FlowControl FUN_801181a8(final RunningScript<?> script) {
    final EffectManagerData6c manager = (EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00;
    ((BttlScriptData6cSub5c)manager.effect_44)._14.get(script.params_20[1].get()).set(script.params_20[2].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x801181f0L)
  public static FlowControl FUN_801181f0(final RunningScript<?> script) {
    final BttlScriptData6cSub5c v0 = (BttlScriptData6cSub5c)((EffectManagerData6c)scriptStatePtrArr_800bc1c0[script.params_20[0].get()].innerStruct_00).effect_44;

    final int v1 = script.params_20[3].get() + 1;
    v0._34.set(script.params_20[1].get());
    v0._38.set(script.params_20[2].get() * v1);
    v0._3c.set(0x1000 / v1);
    v0._40.set(script.params_20[4].get());
    return FlowControl.CONTINUE;
  }

  /** TODO renders other effects too? Burnout, more? */
  @Method(0x8011826cL)
  public static void renderGuardHealEffect(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final GuardHealEffect14 s1 = (GuardHealEffect14)data.effect_44;

    if(data._10.flags_00 >= 0) {
      final MATRIX sp0x10 = new MATRIX();
      FUN_800e8594(sp0x10, data);
      if((data._10.flags_00 & 0x4000_0000) != 0) {
        tmdGp0Tpage_1f8003ec.set(data._10.flags_00 >>> 23 & 0x60);
      } else {
        //LAB_801182bc
        tmdGp0Tpage_1f8003ec.set(s1._10.get());
      }

      //LAB_801182c8
      zOffset_1f8003e8.set(data._10.z_22);
      if((data._10.flags_00 & 0x40) == 0) {
        FUN_800e61e4(data._10.colour_1c.getX() << 5, data._10.colour_1c.getY() << 5, data._10.colour_1c.getZ() << 5);
      } else {
        //LAB_80118304
        FUN_800e60e0(0x1000, 0x1000, 0x1000);
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
        dobj2.tmd_08 = s1.tmd_08.deref();
        renderCtmd(dobj2);
      } else {
        //LAB_80118370
        FUN_800de3f4(s1.tmd_08.deref(), data._10, sp0x10);
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
  public static FlowControl allocateGuardHealEffect(final RunningScript<? extends BattleScriptDataBase> script) {
    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0x14,
      null,
      // This renderer is for the circle under the player
      SEffe::renderGuardHealEffect,
      null,
      GuardHealEffect14::new
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    manager.flags_04 |= 0x300_0000;

    final GuardHealEffect14 effect = (GuardHealEffect14)manager.effect_44;

    final int s1 = script.params_20[1].get();
    effect._00.set(s1 | 0x300_0000L);

    if((s1 & 0xf_ff00) == 0xf_ff00) {
      effect.tmdType_04.clear();
      effect.tmd_08.set(deffManager_800c693c.tmds_2f8[s1 & 0xff]);
      effect._10.set(0x20);
    } else {
      //LAB_8011847c
      final DeffPart.TmdType tmdType = (DeffPart.TmdType)getDeffPart(s1 | 0x300_0000);
      final TmdWithId tmdWithId = tmdType.tmd_0c.deref().tmdPtr_00.deref();
      effect.tmdType_04.set(tmdType);
      effect.tmd_08.set(tmdWithId.tmd.objTable.get(0));
      effect._10.set((int)((tmdWithId.id.get() & 0xffff_0000L) >>> 11));
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

    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0x14,
      null,
      SEffe::renderGuardHealEffect,
      null,
      GuardHealEffect14::new
    );

    final EffectManagerData6c s4 = state.innerStruct_00;
    s4.flags_04 = 0x300_0000;

    final GuardHealEffect14 s0 = (GuardHealEffect14)s4.effect_44;
    s0._10.set(0x20);
    s0._00.set(0x300_0000L);
    s0.tmdType_04.clear();

    final int type = flags & 0xff00_0000;
    if(type == 0x100_0000) {
      //LAB_801185e4
      final DeffPart.AnimatedTmdType animatedTmdType = (DeffPart.AnimatedTmdType)getDeffPart(flags);
      final Tmd tmd = animatedTmdType.tmd_0c.deref().tmdPtr_00.deref().tmd;
      s0.tmd_08.set(tmd.objTable.get(0));
    } else if(type == 0x200_0000) {
      //LAB_801185c0
      final DeffPart.AnimatedTmdType animatedTmdType = (DeffPart.AnimatedTmdType)getDeffPart(flags);
      s0.tmd_08.set(optimisePacketsIfNecessary(animatedTmdType.tmd_0c.deref().tmdPtr_00.deref(), objIndex));
      //LAB_801185b0
    } else if(type == 0x700_0000) {
      //LAB_80118610
      s0.tmd_08.set(_1f8003f4.stage_963c.dobj2s_00[objIndex].tmd_08);
    } else {
      //LAB_80118634
      final BattleScriptDataBase a0_0 = (BattleScriptDataBase)scriptStatePtrArr_800bc1c0[flags].innerStruct_00;
      if(BattleScriptDataBase.EM__.equals(a0_0.magic_00)) {
        final EffectManagerData6c effects = (EffectManagerData6c)a0_0;
        final int v1 = effects.flags_04 & 0xff00_0000;
        if(v1 == 0x100_0000 || v1 == 0x200_0000) {
          //LAB_8011867c
          s0.tmd_08.set(((BttlScriptData6cSub13c)effects.effect_44).model_134.dobj2ArrPtr_00[objIndex].tmd_08);
        }
      } else {
        //LAB_801186a4
        //LAB_801186b4
        s0.tmd_08.set(((BattleObject27c)a0_0).model_148.dobj2ArrPtr_00[objIndex].tmd_08);
      }
    }

    //LAB_801186bc
    //LAB_801186c0
    s4._10.flags_00 = 0x1400_0000;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  /** TODO */
  @Method(0x801186f8L)
  public static void FUN_801186f8(final long a0, final int flags) {
    MEMORY.ref(4, a0).offset(0x0L).setu(flags | 0x300_0000L);
    if((flags & 0xf_ff00L) == 0xf_ff00L) {
      MEMORY.ref(2, a0).offset(0x10L).setu(0x20L);
      MEMORY.ref(4, a0).offset(0x4L).setu(0);
      MEMORY.ref(4, a0).offset(0x8L).setu(deffManager_800c693c.tmds_2f8[flags & 0xff].getAddress()); //TODO
    } else {
      //LAB_80118750
      final DeffPart.TmdType tmdType = (DeffPart.TmdType)getDeffPart(flags | 0x300_0000);
      final TmdWithId tmdWithId = tmdType.tmd_0c.deref().tmdPtr_00.deref();
      MEMORY.ref(4, a0).offset(0x4L).setu(tmdType.getAddress());
      MEMORY.ref(4, a0).offset(0x8L).setu(tmdWithId.tmd.objTable.get(0).getAddress());
      MEMORY.ref(2, a0).offset(0x10L).setu((tmdWithId.id.get() & 0xffff_0000L) >>> 11);
    }

    //LAB_80118780
  }

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
      RotMatrixY(-rotY, sp0x10);
      final short rotZ = (short)ratan2(sp0x10.get(3), sp0x10.get(0));
      RotMatrixZ(-rotZ, sp0x10);
      final short rotX = (short)ratan2(-sp0x10.get(5), sp0x10.get(8));
      RotMatrixX(-rotX, sp0x10);
      RotMatrixY(rotY, sp0x10);
      sp0x10.set(3, (short)0);
      sp0x10.set(4, (short)0);
      sp0x10.set(5, (short)0);
      tmdGp0Tpage_1f8003ec.set(manager._10.flags_00 >>> 23 & 0x60);
      zOffset_1f8003e8.set(manager._10.z_22);
      FUN_800e60e0(manager._10.colour_1c.getX() << 5, manager._10.colour_1c.getY() << 5, manager._10.colour_1c.getZ() << 5);
      FUN_800de3f4(model_800bda10.dobj2ArrPtr_00[0].tmd_08, manager._10, sp0x10);
      FUN_800e6170();
    }

    //LAB_801188d8
  }

  @Method(0x801188ecL)
  public static FlowControl FUN_801188ec(final RunningScript<? extends BattleScriptDataBase> script) {
    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0,
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
    final Anim anim;
    if(part instanceof DeffPart.CmbType) {
      anim = ((DeffPart.CmbType)part).cmb_14.deref();
    } else {
      anim = ((DeffPart.AnimatedTmdType)part).anim_14.deref();
    }

    effect.anim_0c = anim;
    loadModelAnim(effect.model_134, anim);
    manager._10._24 = 0;
    FUN_80114f3c(state, 0, 0x100, 0);
    return FlowControl.CONTINUE;
  }

  @Method(0x80118a24L)
  public static void FUN_80118a24(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final BttlScriptData6cSub08_4 effect = (BttlScriptData6cSub08_4)manager.effect_44;
    final int sp10 = DISPENV_800c34b0.disp.x.get() + manager._10.trans_04.getX() + 160 - manager._10._24 / 2;
    final int sp12 = DISPENV_800c34b0.disp.y.get() + manager._10.trans_04.getY() + 120 - manager._10._28 / 2;
    final int minZ = manager._10.trans_04.getZ() - manager._10._24 / 2 >> 2;
    final int maxZ = manager._10.trans_04.getZ() + manager._10._24 / 2 >> 2;
    final int l = DISPENV_800c34b0.disp.x.get();
    final int r = l + 320;
    final int t = DISPENV_800c34b0.disp.y.get();
    final int b = t + 240;

    final RECT sp0x20 = new RECT();
    final RECT sp0x28 = new RECT();

    //LAB_80118ba8
    for(int i = 0; i < 4; i++) {
      sp0x28.x.set((short)(sp10 + manager._10._24 / 2 * (i & 0x1L)));
      sp0x28.y.set((short)(sp12 + manager._10._28 / 2 * (i >> 1)));
      sp0x28.w.set((short)(manager._10._24 / 2));
      sp0x28.h.set((short)(manager._10._28 / 2));
      if(sp0x28.x.get() < r) {
        if(sp0x28.x.get() < l) {
          sp0x28.w.add((short)(sp0x28.x.get() - l));
          sp0x28.x.set((short)l);
        }

        //LAB_80118c58
        if(r < sp0x28.x.get() + sp0x28.w.get()) {
          sp0x28.w.set((short)(r - sp0x28.x.get()));
        }

        //LAB_80118c7c
        if(sp0x28.w.get() > 0) {
          if(sp0x28.y.get() < b) {
            if(sp0x28.y.get() < t) {
              sp0x20.h.add((short)(sp0x28.y.get() - t));
              sp0x28.y.set((short)t);
            }

            //LAB_80118cc0
            if(b < sp0x28.y.get() + sp0x28.h.get()) {
              sp0x28.h.set((short)(b - sp0x28.y.get()));
            }

            //LAB_80118ce4
            if(sp0x28.h.get() > 0) {
              sp0x20.x.set(effect._00.get());
              sp0x20.y.set((short)(effect._02.get() + i * 64));
              sp0x20.w.set(sp0x28.w.get());
              sp0x20.h.set(sp0x28.h.get());

              // This was depth-queued at both minZ and maxZ, not really sure why... minZ sometimes had a negative value and would crash
              GPU.command80CopyRectFromVramToVram(sp0x28.x.get(), sp0x28.y.get(), sp0x20.x.get(), sp0x20.y.get(), sp0x28.w.get(), sp0x28.h.get());
            }
          }
        }
      }

      //LAB_80118db4
    }
  }

  @Method(0x80118df4L)
  public static FlowControl FUN_80118df4(final RunningScript<? extends BattleScriptDataBase> script) {
    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0x8,
      null,
      SEffe::FUN_80118a24,
      null,
      BttlScriptData6cSub08_4::new
    );

    final EffectManagerData6c manager = state.innerStruct_00;
    final BttlScriptData6cSub08_4 effect = (BttlScriptData6cSub08_4)manager.effect_44;
    effect._00.set((short)0x300);
    effect._02.set((short)0);
    effect._04.set(-1);
    effect._05.set(-1);
    effect._06.set((short)0);
    manager._10.trans_04.setZ(0x100);
    manager._10._24 = 0x80;
    manager._10._28 = 0x80;
    manager._10._2c = 0x100;
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x80118e98L)
  public static void FUN_80118e98(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final VECTOR sp0x84 = new VECTOR();
    final VECTOR sp0x90 = new VECTOR();
    final VECTOR sp0x9c = new VECTOR();

    final BttlScriptData6cSub30 effect = (BttlScriptData6cSub30)manager.effect_44;
    if(manager._10.flags_00 >= 0) {
      final MATRIX sp0x10 = new MATRIX();
      FUN_800e8594(sp0x10, manager);

      final long v1 = effect._04.get() & 0xff00_0000L;
      if(v1 == 0x300_0000L) {
        //LAB_80118f38
        if((manager._10.flags_00 & 0x4000_0000) != 0) {
          tmdGp0Tpage_1f8003ec.set(manager._10.flags_00 >>> 23 & 0x60);
        } else {
          //LAB_80118f5c
          tmdGp0Tpage_1f8003ec.set(effect.tpage_2c.get());
        }

        //LAB_80118f68
        zOffset_1f8003e8.set(manager._10.z_22);

        if((manager._10.flags_00 & 0x40) == 0) {
          FUN_800e61e4(manager._10.colour_1c.getX() << 5, manager._10.colour_1c.getY() << 5, manager._10.colour_1c.getZ() << 5);
        }

        //LAB_80118f9c
        FUN_800de3f4(effect.tmd_24.deref(), manager._10, sp0x10);
      } else if(v1 == 0x400_0000L) {
        FUN_800e9428(effect._1c.metrics_04, manager._10, sp0x10);
      }

      //LAB_80118fac
      if(effect._08.get() != 0) {
        final EffectManagerData6cInner sp0x50 = new EffectManagerData6cInner();

        //LAB_80118fc4
        sp0x50.set(manager._10);

        final int a2 = effect._08.get() * (effect._0c.get() + 1);
        if((effect._00.get() & 0x4L) != 0) {
          final int v0 = effect._10.get() - 0x1000;
          sp0x50._28 = sp0x50.colour_1c.getX() << 12;
          sp0x50._2c = sp0x50.colour_1c.getY() << 12;
          sp0x50._30 = sp0x50.colour_1c.getZ() << 12;
          sp0x84.setX(sp0x50.colour_1c.getX() * v0 / a2);
          sp0x84.setY(sp0x50.colour_1c.getY() * v0 / a2);
          sp0x84.setZ(sp0x50.colour_1c.getZ() * v0 / a2);
        }

        //LAB_801190a8
        if((effect._00.get() & 0x8L) != 0) {
          final int v0 = effect._10.get() - 0x1000;
          sp0x90.setX(sp0x50.scale_16.getX() << 12);
          sp0x90.setY(sp0x50.scale_16.getY() << 12);
          sp0x90.setZ(sp0x50.scale_16.getZ() << 12);
          sp0x9c.setX(sp0x50.scale_16.getX() * v0 / a2);
          sp0x9c.setY(sp0x50.scale_16.getY() * v0 / a2);
          sp0x9c.setZ(sp0x50.scale_16.getZ() * v0 / a2);
        }

        //LAB_80119130
        //LAB_8011914c
        for(int s6 = 1; s6 < effect._08.get() && s6 < effect._14.get(); s6++) {
          final VECTOR a1 = effect._18.deref().get((effect._14.get() - s6 - 1) % effect._08.get());

          final int steps = effect._0c.get() + 1;
          final int stepX = (a1.getX() - sp0x10.transfer.getX() << 12) / steps;
          final int stepY = (a1.getY() - sp0x10.transfer.getY() << 12) / steps;
          final int stepZ = (a1.getZ() - sp0x10.transfer.getZ() << 12) / steps;

          int x = sp0x10.transfer.getX() << 12;
          int y = sp0x10.transfer.getY() << 12;
          int z = sp0x10.transfer.getZ() << 12;

          final long s5 = effect._04.get() & 0xff00_0000L;

          //LAB_80119204
          for(int s1 = effect._0c.get(); s1 >= 0; s1--) {
            if((effect._00.get() & 0x4L) != 0) {
              sp0x50._28 += sp0x84.getX();
              sp0x50._2c += sp0x84.getY();
              sp0x50._30 += sp0x84.getZ();
              //LAB_80119254
              //LAB_80119270
              //LAB_8011928c
              sp0x50.colour_1c.setX((short)(sp0x50._28 >> 12));
              sp0x50.colour_1c.setY((short)(sp0x50._2c >> 12));
              sp0x50.colour_1c.setZ((short)(sp0x50._30 >> 12));
            }

            //LAB_80119294
            if((effect._00.get() & 0x8L) != 0) {
              sp0x90.add(sp0x9c);

              //LAB_801192e4
              //LAB_80119300
              //LAB_8011931c
              sp0x50.scale_16.setX((short)(sp0x90.getX() >> 12));
              sp0x50.scale_16.setY((short)(sp0x90.getY() >> 12));
              sp0x50.scale_16.setZ((short)(sp0x90.getZ() >> 12));
            }

            //LAB_80119324
            x += stepX;
            y += stepY;
            z += stepZ;

            //LAB_80119348
            //LAB_80119360
            //LAB_80119378
            final MATRIX sp0x30 = new MATRIX().set(sp0x10);
            sp0x30.transfer.set(x >> 12, y >> 12, z >> 12);

            ScaleVectorL_SVEC(sp0x30, sp0x50.scale_16);
            if(s5 == 0x300_0000L) {
              //LAB_801193f0
              FUN_800de3f4(effect.tmd_24.deref(), sp0x50, sp0x30);
            } else if(s5 == 0x400_0000L) {
              FUN_800e9428(effect._1c.metrics_04, sp0x50, sp0x30);
            }

            //LAB_80119400
            //LAB_80119404
          }

          //LAB_8011940c
        }

        //LAB_80119420
        if((effect._04.get() & 0xff00_0000L) == 0x300_0000L && (manager._10.flags_00 & 0x40) == 0) {
          FUN_800e62a8();
        }
      }
    }

    //LAB_80119454
  }

  @Method(0x80119484L)
  public static FlowControl FUN_80119484(final RunningScript<? extends BattleScriptDataBase> script) {
    int s4 = script.params_20[1].get();
    final int s2 = script.params_20[2].get();
    final int s6 = script.params_20[3].get();
    final int s0 = script.params_20[4].get();
    final int s1 = script.params_20[5].get();

    final ScriptState<EffectManagerData6c> state = allocateEffectManager(
      script.scriptState_04,
      0x30,
      SEffe::FUN_801196bc,
      SEffe::FUN_80118e98,
      SEffe::FUN_80119788,
      BttlScriptData6cSub30::new
    );

    final EffectManagerData6c data = state.innerStruct_00;
    data.type_5c = _800fb954.get();

    final BttlScriptData6cSub30 s3 = (BttlScriptData6cSub30)data.effect_44;
    s3._00.set(s2);
    s3._04.set(s4);
    s3._08.set(s6);
    s3._0c.set(s0);
    s3._10.set(s1);
    s3._14.set(0);

    if(s6 != 0) {
      s3._18.setPointer(mallocHead(s6 * 0x10L));
    } else {
      //LAB_80119568
      s3._18.clear();
    }

    //LAB_8011956c
    final long v1 = s4 & 0xff00_0000L;
    if(v1 == 0x400_0000L) {
      //LAB_80119640
      FUN_800e95f0(s3._1c, s4);
      data._10.flags_00 = data._10.flags_00 & 0xfbff_ffff | 0x5000_0000;
    } else if(v1 == 0x300_0000L) {
      //LAB_80119668
      FUN_801186f8(s3.getAddress() + 0x1cL, s4); //TODO
      data._10.flags_00 = 0x1400_0000;
    } else if(v1 == 0) {
      //LAB_801195a8
      throw new RuntimeException("The type is " + ((EffectManagerData6c)scriptStatePtrArr_800bc1c0[s4].innerStruct_00).effect_44.getClass().getSimpleName());

/*
      final BttlScriptData6cSubBase1 v1_0 = ((EffectManagerData6c)scriptStatePtrArr_800bc1c0[s4].innerStruct_00).effect_44;
      s4 = v1_0._00.get();
      s3._04.set(s4);

      final long a1 = s4 & 0xff00_0000L;
      if(a1 == 0x300_0000L) {
        //LAB_8011960c
        //TODO
        MEMORY.ref(4, s3.getAddress()).offset(0x1cL).setu(MEMORY.ref(4, v1_0.getAddress()).offset(0x0L).get());
        MEMORY.ref(4, s3.getAddress()).offset(0x20L).setu(MEMORY.ref(4, v1_0.getAddress()).offset(0x4L).get());
        MEMORY.ref(4, s3.getAddress()).offset(0x24L).setu(MEMORY.ref(4, v1_0.getAddress()).offset(0x8L).get());
        MEMORY.ref(4, s3.getAddress()).offset(0x28L).setu(MEMORY.ref(4, v1_0.getAddress()).offset(0xcL).get());
        MEMORY.ref(4, s3.getAddress()).offset(0x2cL).setu(MEMORY.ref(4, v1_0.getAddress()).offset(0x10L).get());
      } else if(a1 == 0x400_0000L) {
        //TODO
        MEMORY.ref(4, s3.getAddress()).offset(0x1cL).setu(MEMORY.ref(4, v1_0.getAddress()).offset(0x0L).get());
        MEMORY.ref(4, s3.getAddress()).offset(0x20L).setu(MEMORY.ref(4, v1_0.getAddress()).offset(0x4L).get());
        MEMORY.ref(4, s3.getAddress()).offset(0x24L).setu(MEMORY.ref(4, v1_0.getAddress()).offset(0x8L).get());
      }
*/
    }

    //LAB_8011967c
    script.params_20[0].set(state.index);
    return FlowControl.CONTINUE;
  }

  @Method(0x801196bcL)
  public static void FUN_801196bc(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final BttlScriptData6cSub30 s0 = (BttlScriptData6cSub30)manager.effect_44;

    if(s0._08.get() != 0) {
      final MATRIX sp0x10 = new MATRIX();
      FUN_800e8594(sp0x10, manager);
      s0._18.deref().get(s0._14.get() % s0._08.get()).set(sp0x10.transfer);
      s0._14.incr();
    }

    //LAB_80119778
  }

  @Method(0x80119788L)
  public static void FUN_80119788(final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final BttlScriptData6cSub30 effect = (BttlScriptData6cSub30)data.effect_44;

    if(!effect._18.isNull()) {
      free(effect._18.getPointer());
    }
  }
}
