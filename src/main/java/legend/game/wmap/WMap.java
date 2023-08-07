package legend.game.wmap;

import legend.core.IoHelper;
import legend.core.MathHelper;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gpu.GpuCommandQuad;
import legend.core.gpu.RECT;
import legend.core.gte.COLOUR;
import legend.core.gte.DVECTOR;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.ModelPart10;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.TmdObjTable1c;
import legend.core.gte.TmdWithId;
import legend.core.gte.VECTOR;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.input.Input;
import legend.game.input.InputAction;
import legend.game.inventory.WhichMenu;
import legend.game.inventory.screens.TextColour;
import legend.game.modding.coremod.CoreMod;
import legend.game.submap.EncounterRateMode;
import legend.game.tim.Tim;
import legend.game.tmd.Renderer;
import legend.game.types.CContainer;
import legend.game.types.CoolonWarpDestination20;
import legend.game.types.Coord2AndThenSomeStruct_60;
import legend.game.types.EngineState;
import legend.game.types.GsF_LIGHT;
import legend.game.types.LodString;
import legend.game.types.McqHeader;
import legend.game.types.Model124;
import legend.game.types.TexPageY;
import legend.game.types.TmdAnimationFile;
import legend.game.types.Translucency;
import legend.game.unpacker.FileData;
import org.joml.Math;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.MEMORY;
import static legend.game.Scus94491BpeSegment.FUN_80019c80;
import static legend.game.Scus94491BpeSegment.FUN_8001eea8;
import static legend.game.Scus94491BpeSegment.getLoadedDrgnFiles;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.loadDrgnFile;
import static legend.game.Scus94491BpeSegment.loadWmapMusic;
import static legend.game.Scus94491BpeSegment.orderingTableSize_1f8003c8;
import static legend.game.Scus94491BpeSegment.playSound;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.resizeDisplay;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.unloadSoundFile;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a3ec;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a488;
import static legend.game.Scus94491BpeSegment_8002.SquareRoot0;
import static legend.game.Scus94491BpeSegment_8002.animateModel;
import static legend.game.Scus94491BpeSegment_8002.applyModelRotationAndScale;
import static legend.game.Scus94491BpeSegment_8002.clearTextbox;
import static legend.game.Scus94491BpeSegment_8002.initModel;
import static legend.game.Scus94491BpeSegment_8002.initTextbox;
import static legend.game.Scus94491BpeSegment_8002.loadAndRenderMenus;
import static legend.game.Scus94491BpeSegment_8002.loadModelStandardAnimation;
import static legend.game.Scus94491BpeSegment_8002.rand;
import static legend.game.Scus94491BpeSegment_8002.renderDobj2;
import static legend.game.Scus94491BpeSegment_8002.renderModel;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_8002.strcmp;
import static legend.game.Scus94491BpeSegment_8002.textWidth;
import static legend.game.Scus94491BpeSegment_8003.GsGetLs;
import static legend.game.Scus94491BpeSegment_8003.GsGetLws;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.GsSetFlatLight;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_8003.GsSetRefView2L;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_Xyz;
import static legend.game.Scus94491BpeSegment_8003.RotTransPers4;
import static legend.game.Scus94491BpeSegment_8003.StoreImage;
import static legend.game.Scus94491BpeSegment_8003.perspectiveTransform;
import static legend.game.Scus94491BpeSegment_8003.perspectiveTransformTriple;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.setRotTransMatrix;
import static legend.game.Scus94491BpeSegment_8004.engineStateOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_8004.previousEngineState_8004dd28;
import static legend.game.Scus94491BpeSegment_8004.ratan2;
import static legend.game.Scus94491BpeSegment_8005._80052c6c;
import static legend.game.Scus94491BpeSegment_8005.index_80052c38;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8005.submapScene_80052c34;
import static legend.game.Scus94491BpeSegment_8007.clearRed_8007a3a8;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b.analogMagnitude_800beeb4;
import static legend.game.Scus94491BpeSegment_800b.clearBlue_800babc0;
import static legend.game.Scus94491BpeSegment_800b.clearGreen_800bb104;
import static legend.game.Scus94491BpeSegment_800b.combatStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800b.continentIndex_800bf0b0;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.input_800bee90;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.repeat_800bee98;
import static legend.game.Scus94491BpeSegment_800b.savedGameSelected_800bdc34;
import static legend.game.Scus94491BpeSegment_800b.soundFiles_800bcf80;
import static legend.game.Scus94491BpeSegment_800b.texPages_800bb110;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;
import static legend.game.Scus94491BpeSegment_800b.textboxes_800be358;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;

public class WMap {
  private static final Value _800c6690 = MEMORY.ref(4, 0x800c6690L);

  private static int worldMapState_800c6698;
  private static int playerState_800c669c;
  private static final Value _800c66a0 = MEMORY.ref(4, 0x800c66a0L);
  private static final Value _800c66a4 = MEMORY.ref(4, 0x800c66a4L);
  private static WMapStruct258 struct258_800c66a8;

  private static WMapStruct19c0 _800c66b0;

  /**
   * <ul>
   *   <li>0x2 - general wmap textures</li>
   *   <li>0x4 - wmap mesh</li>
   * </ul>
   */
  private static final Value filesLoadedFlags_800c66b8 = MEMORY.ref(4, 0x800c66b8L);

  private static final IntRef tempZ_800c66d8 = MEMORY.ref(4, 0x800c66d8L, IntRef::new);
  private static final Value _800c66dc = MEMORY.ref(2, 0x800c66dcL);

  private static McqHeader mcqHeader_800c6768;

  private static final Value _800c6794 = MEMORY.ref(2, 0x800c6794L);

  public static final MapState100 mapState_800c6798 = new MapState100();

  private static WMapRender40 _800c6898;
  private static WMapRender40 _800c689c;
  private static final Value _800c68a0 = MEMORY.ref(4, 0x800c68a0L);
  private static final Value _800c68a4 = MEMORY.ref(4, 0x800c68a4L);
  private static final Value _800c68a8 = MEMORY.ref(4, 0x800c68a8L);

  private static final IntRef encounterAccumulator_800c6ae8 = MEMORY.ref(4, 0x800c6ae8L, IntRef::new);

  private static final ArrayRef<VECTOR> _800c74b8 = MEMORY.ref(4, 0x800c74b8L, ArrayRef.of(VECTOR.class, 0x101, 0x10, VECTOR::new));
  private static final ArrayRef<ShortRef> _800c84c8 = MEMORY.ref(2, 0x800c84c8L, ArrayRef.of(ShortRef.class, 0x101, 2, ShortRef::new));

  private static final IntRef _800c86cc = MEMORY.ref(4, 0x800c86ccL, IntRef::new);

  private static final Value colour_800c86d0 = MEMORY.ref(2, 0x800c86d0L);
  private static final Value _800c86d2 = MEMORY.ref(1, 0x800c86d2L);

  private static final int[] _800c86d4 = new int[8];

  private static final Value _800c86f0 = MEMORY.ref(4, 0x800c86f0L);

  private static Coord2AndThenSomeStruct_60[] _800c86f8;
  private static final Value _800c86fc = MEMORY.ref(4, 0x800c86fcL);
  private static final RECT _800c8700 = MEMORY.ref(4, 0x800c8700L, RECT::new);
  private static final COLOUR _800c8778 = MEMORY.ref(4, 0x800c8778L, COLOUR::new);
  private static final RECT _800c877c = MEMORY.ref(4, 0x800c877cL, RECT::new);

  private static final Vector3f _800c87d8 = new Vector3f(0.0f, 1.0f, 0.0f);
  private static final COLOUR _800c87e8 = MEMORY.ref(4, 0x800c87e8L, COLOUR::new);
  private static final RECT _800c87ec = MEMORY.ref(4, 0x800c87ecL, RECT::new);
  private static final COLOUR _800c87f4 = MEMORY.ref(4, 0x800c87f4L, COLOUR::new);
  private static final RECT _800c87f8 = MEMORY.ref(4, 0x800c87f8L, RECT::new);

  private static final Value _800eee48 = MEMORY.ref(4, 0x800eee48L);

  /**
   * <ol start="0">
   *   <li>{@link WMap#initWmap}</li>
   *   <li>{@link WMap#waitForWmapMusicToLoad}</li>
   *   <li>{@link WMap#FUN_800ccc64}</li>
   *   <li>{@link WMap#FUN_800cccbc}</li>
   *   <li>{@link WMap#FUN_800ccce4}</li>
   *   <li>{@link WMap#FUN_800cc758}</li>
   *   <li>{@link WMap#FUN_800ccd70}</li>
   *   <li>{@link WMap#FUN_800ccda4}</li>
   *   <li>{@link WMap#transitionToCombat}</li>
   *   <li>{@link WMap#FUN_800cce9c}</li>
   *   <li>{@link WMap#FUN_800ccecc}</li>
   *   <li>{@link WMap#FUN_800ccbd8}</li>
   *   <li>{@link WMap#FUN_800ccef4}</li>
   * </ol>
   */
  private static final Runnable[] _800ef000 = new Runnable[13];
  static {
    _800ef000[0] = WMap::initWmap;
    _800ef000[1] = WMap::waitForWmapMusicToLoad;
    _800ef000[2] = WMap::FUN_800ccc64;
    _800ef000[3] = WMap::FUN_800cccbc;
    _800ef000[4] = WMap::FUN_800ccce4;
    _800ef000[5] = WMap::FUN_800cc758;
    _800ef000[6] = WMap::FUN_800ccd70;
    _800ef000[7] = WMap::FUN_800ccda4;
    _800ef000[8] = WMap::transitionToCombat;
    _800ef000[9] = WMap::FUN_800cce9c;
    _800ef000[10] = WMap::FUN_800ccecc;
    _800ef000[11] = WMap::FUN_800ccbd8;
    _800ef000[12] = WMap::FUN_800ccef4;
  }

  private static final UnsignedShortRef vramX_800ef0d4 = MEMORY.ref(2, 0x800ef0d4L, UnsignedShortRef::new);
  private static final UnsignedShortRef vramY_800ef0d6 = MEMORY.ref(2, 0x800ef0d6L, UnsignedShortRef::new);
  private static final UnsignedShortRef clutX_800ef0d8 = MEMORY.ref(2, 0x800ef0d8L, UnsignedShortRef::new);
  private static final UnsignedShortRef clutY_800ef0da = MEMORY.ref(2, 0x800ef0daL, UnsignedShortRef::new);

  /** array of 8-byte structs */
  private static final Value imageX_800ef0cc = MEMORY.ref(2, 0x800ef0ccL);
  private static final Value imageY_800ef0ce = MEMORY.ref(2, 0x800ef0ceL);
  private static final Value clutX_800ef0d0 = MEMORY.ref(2, 0x800ef0d0L);
  private static final Value clutY_800ef0d2 = MEMORY.ref(2, 0x800ef0d2L);

  private static final Value _800ef104 = MEMORY.ref(1, 0x800ef104L);

  private static final Value _800ef130 = MEMORY.ref(1, 0x800ef130L);

  private static final Value _800ef140 = MEMORY.ref(1, 0x800ef140L);

  private static final Value _800ef154 = MEMORY.ref(1, 0x800ef154L);

  private static final Value _800ef158 = MEMORY.ref(1, 0x800ef158L);

  private static final Value _800ef168 = MEMORY.ref(1, 0x800ef168L);

  private static final Value _800ef170 = MEMORY.ref(1, 0x800ef170L);

  private static final Value _800ef194 = MEMORY.ref(1, 0x800ef194L);

  private static final Value _800ef19c = MEMORY.ref(1, 0x800ef19cL);

  private static final Value _800ef1a4 = MEMORY.ref(2, 0x800ef1a4L);
  private static final ArrayRef<VECTOR> mapPositions_800ef1a8 = MEMORY.ref(4, 0x800ef1a8L, ArrayRef.of(VECTOR.class, 8, 0x10, VECTOR::new));
  private static final ArrayRef<CoolonWarpDestination20> coolonWarpDest_800ef228 = MEMORY.ref(4, 0x800ef228L, ArrayRef.of(CoolonWarpDestination20.class, 9, 0x20, CoolonWarpDestination20::new));
  private static final Value _800ef348 = MEMORY.ref(2, 0x800ef348L);

  private static final ArrayRef<ArrayRef<UnsignedShortRef>> encounterIds_800ef364 = MEMORY.ref(2, 0x800ef364L, ArrayRef.of(ArrayRef.classFor(UnsignedShortRef.class), 100, 8, ArrayRef.of(UnsignedShortRef.class, 4, 2, UnsignedShortRef::new)));
  /**
   * <ol start="0">
   *   <li>{@link WMap#renderDartShadow}</li>
   *   <li>{@link WMap#renderQueenFuryShadow}</li>
   *   <li>{@link WMap#FUN_800e32fc}</li>
   *   <li>{@link WMap#FUN_800e32fc}</li>
   * </ul>
   */
  private static final Runnable[] shadowRenderers_800ef684 = new Runnable[4];
  static {
    shadowRenderers_800ef684[0] = WMap::renderDartShadow;
    shadowRenderers_800ef684[1] = WMap::renderQueenFuryShadow;
    shadowRenderers_800ef684[2] = WMap::FUN_800e32fc;
    shadowRenderers_800ef684[3] = WMap::FUN_800e32fc;
  }

  private static final Value _800ef694 = MEMORY.ref(1, 0x800ef694L);
  private static final ArrayRef<WMapStruct08> _800ef698 = MEMORY.ref(4, 0x800ef698L, ArrayRef.of(WMapStruct08.class, 6, 0x8, WMapStruct08::new));
  private static final ArrayRef<WMapStruct0c> _800ef6c8 = MEMORY.ref(4, 0x800ef6c8L, ArrayRef.of(WMapStruct0c.class, 6, 0xc, WMapStruct0c::new));

  private static final LodString No_800effa4 = MEMORY.ref(4, 0x800effa4L, LodString::new);
  private static final LodString Yes_800effb0 = MEMORY.ref(4, 0x800effb0L, LodString::new);
  /** "Move?" */
  private static final LodString Move_800f00e8 = MEMORY.ref(4, 0x800f00e8L, LodString::new);

  private static final ArrayRef<Pointer<LodString>> services_800f01cc = MEMORY.ref(4, 0x800f01ccL, ArrayRef.of(Pointer.classFor(LodString.class), 5, 4, Pointer.deferred(4, LodString::new)));
  private static final Pointer<LodString> _800f01e0 = MEMORY.ref(4, 0x800f01e0L, Pointer.deferred(4, LodString::new));
  private static final Pointer<LodString> No_Entry_800f01e4 = MEMORY.ref(4, 0x800f01e4L, Pointer.deferred(4, LodString::new));
  private static final Pointer<LodString> Enter_800f01e8 = MEMORY.ref(4, 0x800f01e8L, Pointer.deferred(4, LodString::new));
  private static final ArrayRef<Pointer<LodString>> regions_800f01ec = MEMORY.ref(4, 0x800f01ecL, ArrayRef.of(Pointer.classFor(LodString.class), 3, 4, Pointer.deferred(4, LodString::new)));

  private static final Runnable[] _800f01fc = new Runnable[2];
  static {
    _800f01fc[0] = WMap::FUN_800e406c;
    _800f01fc[1] = WMap::FUN_800e469c;
  }

  private static final ArrayRef<UnsignedByteRef> _800f0204 = MEMORY.ref(1, 0x800f0204L, ArrayRef.of(UnsignedByteRef.class, 0xc, 1, UnsignedByteRef::new));
  private static final ArrayRef<UnsignedByteRef> _800f0210 = MEMORY.ref(1, 0x800f0210L, ArrayRef.of(UnsignedByteRef.class, 0xc, 1, UnsignedByteRef::new));
  private static final Value _800f021c = MEMORY.ref(2, 0x800f021cL);

  private static final UnboundedArrayRef<Place0c> places_800f0234 = MEMORY.ref(4, 0x800f0234L, UnboundedArrayRef.of(0xc, Place0c::new));

  private static final ArrayRef<Location14> locations_800f0e34 = MEMORY.ref(2, 0x800f0e34L, ArrayRef.of(Location14.class, 0x100, 0x14, Location14::new));
  public static final ArrayRef<AreaData08> areaData_800f2248 = MEMORY.ref(2, 0x800f2248L, ArrayRef.of(AreaData08.class, 133, 8, AreaData08::new));

  private static final ArrayRef<IntRef> _800f5810 = MEMORY.ref(4, 0x800f5810L, ArrayRef.of(IntRef.class, 0x43, 4, IntRef::new));

  private static final ArrayRef<Pointer<UnboundedArrayRef<VECTOR>>> pathDotPosPtrArr_800f591c = MEMORY.ref(4, 0x800f591cL, ArrayRef.of(Pointer.classFor(UnboundedArrayRef.classFor(VECTOR.class)), 66, 4, Pointer.deferred(4, UnboundedArrayRef.of(0x10, VECTOR::new))));

  private static final ArrayRef<WMapStruct2c> _800f5a6c = MEMORY.ref(2, 0x800f5a6cL, ArrayRef.of(WMapStruct2c.class, 0x40, 0x2c, WMapStruct2c::new));
  private static final Value _800f6598 = MEMORY.ref(4, 0x800f6598L);
  private static final Value _800f659c = MEMORY.ref(4, 0x800f659cL);
  private static final Value _800f65a0 = MEMORY.ref(4, 0x800f65a0L);
  /**
   * <ol start="0">
   *   <li>{@link WMap#FUN_800ebb2c}</li>
   *   <li>{@link WMap#FUN_800ebb44}</li>
   *   <li>{@link WMap#FUN_800eca3c}</li>
   * </ol>
   */
  private static final Runnable[] _800f65a4 = new Runnable[3];
  static {
    _800f65a4[0] = WMap::FUN_800ebb2c;
    _800f65a4[1] = WMap::FUN_800ebb44;
    _800f65a4[2] = WMap::FUN_800eca3c;
  }
  /**
   * These are probably effects that can be rendered over a place
   * <ol start="0">
   *   <li>{@link WMap#FUN_800ebb34}</li>
   *   <li>{@link WMap#FUN_800ebfc0}</li>
   *   <li>{@link WMap#renderSnow}</li>
   * </ol>
   */
  private static final Runnable[] _800f65b0 = new Runnable[3];
  static {
    _800f65b0[0] = WMap::FUN_800ebb34;
    _800f65b0[1] = WMap::FUN_800ebfc0;
    _800f65b0[2] = WMap::renderSnow;
  }
  /**
   * <ol start="0">
   *   <li>{@link WMap#FUN_800ebb3c}</li>
   *   <li>{@link WMap#FUN_800eed3c}</li>
   *   <li>{@link WMap#FUN_800eed90}</li>
   * </ol>
   */
  private static final Runnable[] _800f65bc = new Runnable[3];
  static {
    _800f65bc[0] = WMap::FUN_800ebb3c;
    _800f65bc[1] = WMap::FUN_800eed3c;
    _800f65bc[2] = WMap::FUN_800eed90;
  }

  private static final Value _800f65c8 = MEMORY.ref(1, 0x800f65c8L);

  private static final Value _800f65d4 = MEMORY.ref(1, 0x800f65d4L);

  @Method(0x800c8844L)
  public static void adjustWmapUvs(final ModelPart10 dobj2, final int colourMapIndex) {
    for(final TmdObjTable1c.Primitive primitive : dobj2.tmd_08.primitives_10) {
      final int cmd = primitive.header() & 0xff04_0000;

      if(cmd == 0x3700_0000) {
        FUN_800c9130(primitive, colourMapIndex & 0x7f);
      } else if(cmd == 0x3e00_0000) {
        FUN_800c9090(primitive, colourMapIndex & 0x7f);
      } else if(cmd == 0x3d00_0000 || cmd == 0x3f00_0000) {
        FUN_800c91bc(primitive, colourMapIndex & 0x7f);
      } else if(cmd == 0x3c00_0000) {
        FUN_800c9090(primitive, colourMapIndex & 0x7f);
      } else if(cmd == 0x3500_0000) {
        FUN_800c9130(primitive, colourMapIndex & 0x7f);
      } else if(cmd == 0x3600_0000) {
        FUN_800c9004(primitive, colourMapIndex & 0x7f);
      } else if(cmd == 0x3400_0000) {
        FUN_800c9004(primitive, colourMapIndex & 0x7f);
      }
    }
  }

  @Method(0x800c8d90L)
  public static void FUN_800c8d90(final Model124 model) {
    assert false;
  }

  @Method(0x800c9004L)
  public static void FUN_800c9004(final TmdObjTable1c.Primitive primitive, final int colourMap) {
    final long a3 = _800eee48.offset(colourMap * 0x14L).getAddress();

    //LAB_800c9024
    for(final byte[] data : primitive.data()) {
      MathHelper.set(data, 0x0, 4, (MathHelper.get(data, 0x0, 4) & (int)MEMORY.ref(4, a3).offset(0x4L).get() | (int)MEMORY.ref(4, a3).offset(0x0L).get()) + (int)MEMORY.ref(4, a3).offset(0x10L).get());
      MathHelper.set(data, 0x4, 4, (MathHelper.get(data, 0x4, 4) & (int)MEMORY.ref(4, a3).offset(0xcL).get() | (int)MEMORY.ref(4, a3).offset(0x8L).get()) + (int)MEMORY.ref(4, a3).offset(0x10L).get());
      MathHelper.set(data, 0x8, 4,  MathHelper.get(data, 0x8, 4) + (int)MEMORY.ref(4, a3).offset(0x10L).get());
    }
  }

  @Method(0x800c9090L)
  public static void FUN_800c9090(final TmdObjTable1c.Primitive primitive, final int colourMap) {
    final long a3 = _800eee48.offset(colourMap * 0x14L).getAddress();

    //LAB_800c90b0
    for(final byte[] data : primitive.data()) {
      MathHelper.set(data, 0x0, 4, (MathHelper.get(data, 0x0, 4) & (int)MEMORY.ref(4, a3).offset(0x4L).get() | (int)MEMORY.ref(4, a3).offset(0x0L).get()) + (int)MEMORY.ref(4, a3).offset(0x10L).get());
      MathHelper.set(data, 0x4, 4, (MathHelper.get(data, 0x4, 4) & (int)MEMORY.ref(4, a3).offset(0xcL).get() | (int)MEMORY.ref(4, a3).offset(0x8L).get()) + (int)MEMORY.ref(4, a3).offset(0x10L).get());
      MathHelper.set(data, 0x8, 4,  MathHelper.get(data, 0x8, 4) + (int)MEMORY.ref(4, a3).offset(0x10L).get());
      MathHelper.set(data, 0xc, 4,  MathHelper.get(data, 0xc, 4) + (int)MEMORY.ref(4, a3).offset(0x10L).get());
    }
  }

  @Method(0x800c9130L)
  public static void FUN_800c9130(final TmdObjTable1c.Primitive primitive, final int colourMap) {
    final long a3 = _800eee48.offset(colourMap * 0x14L).getAddress();

    //LAB_800c9150
    for(final byte[] data : primitive.data()) {
      MathHelper.set(data, 0x0, 4, (MathHelper.get(data, 0x0, 4) & (int)MEMORY.ref(4, a3).offset(0x4L).get() | (int)MEMORY.ref(4, a3).offset(0x0L).get()) + (int)MEMORY.ref(4, a3).offset(0x10L).get());
      MathHelper.set(data, 0x4, 4, (MathHelper.get(data, 0x4, 4) & (int)MEMORY.ref(4, a3).offset(0xcL).get() | (int)MEMORY.ref(4, a3).offset(0x8L).get()) + (int)MEMORY.ref(4, a3).offset(0x10L).get());
      MathHelper.set(data, 0x8, 4,  MathHelper.get(data, 0x8, 4) + (int)MEMORY.ref(4, a3).offset(0x10L).get());
    }
  }

  @Method(0x800c91bcL)
  public static void FUN_800c91bc(final TmdObjTable1c.Primitive primitive, final int colourMap) {
    final long a3 = _800eee48.offset(colourMap * 0x14L).getAddress();

    //LAB_800c91dc
    for(final byte[] data : primitive.data()) {
      MathHelper.set(data, 0x0, 4, (MathHelper.get(data, 0x0, 4) & (int)MEMORY.ref(4, a3).offset(0x4L).get() | (int)MEMORY.ref(4, a3).offset(0x0L).get()) + (int)MEMORY.ref(4, a3).offset(0x10L).get());
      MathHelper.set(data, 0x4, 4, (MathHelper.get(data, 0x4, 4) & (int)MEMORY.ref(4, a3).offset(0xcL).get() | (int)MEMORY.ref(4, a3).offset(0x8L).get()) + (int)MEMORY.ref(4, a3).offset(0x10L).get());
      MathHelper.set(data, 0x8, 4,  MathHelper.get(data, 0x8, 4) + (int)MEMORY.ref(4, a3).offset(0x10L).get());
      MathHelper.set(data, 0xc, 4,  MathHelper.get(data, 0xc, 4) + (int)MEMORY.ref(4, a3).offset(0x10L).get());
    }
  }

  @Method(0x800c925cL) // Renders the player
  public static void renderWmapModel(final Model124 model) {
    zOffset_1f8003e8.set(model.zOffset_a0);
    tmdGp0Tpage_1f8003ec.set(model.tpage_108);

    //LAB_800c92c8
    for(int i = 0; i < model.modelParts_00.length; i++) {
      final ModelPart10 dobj2 = model.modelParts_00[i];

      if((model.partInvisible_f4 & 1L << i) == 0) {
        final MATRIX ls = new MATRIX();
        final MATRIX lw = new MATRIX();
        GsGetLws(dobj2.coord2_04, lw, ls);
        GsSetLightMatrix(lw);
        GTE.setRotationMatrix(ls);
        GTE.setTranslationVector(ls.transfer);
        Renderer.renderDobj2(dobj2, false, 0);
      }
    }

    //LAB_800c9354
    if(model.movementType_cc != 0) {
      FUN_800c8d90(model);
    }

    //LAB_800c936c
  }

  @Method(0x800cc738L) // Pretty sure this is the entry point to WMap
  public static void FUN_800cc738() {
    FUN_800ccb98();
  }

  @Method(0x800cc758L)
  public static void FUN_800cc758() {
    loadAndRenderMenus();

    if(whichMenu_800bdc38 == WhichMenu.NONE_0) {
      if(savedGameSelected_800bdc34.get()) {
        final WMapStruct258 struct258 = struct258_800c66a8;

        //LAB_800cc7d0
        struct258.imageData_2c = null;
        struct258.imageData_30 = null;

        pregameLoadingStage_800bb10c.set(gameState_800babc8.isOnWorldMap_4e4 ? 9 : 7);
      } else {
        //LAB_800cc804
        resizeDisplay(320, 240);
        loadWmapMusic(gameState_800babc8.chapterIndex_98, 0);
        pregameLoadingStage_800bb10c.set(12);
      }

      //LAB_800cc828
    }

    //LAB_800cc82c
  }

  @Method(0x800cc83cL)
  public static void FUN_800cc83c() {
    if(_800c6690.get() == 0) {
      if((input_800bee90.get() & 0x1af) == 0) {
        final WMapStruct19c0 v1 = _800c66b0;

        if(v1._c5 == 0) {
          if(v1._c4 == 0) {
            final WMapStruct258 a0 = struct258_800c66a8;

            if(a0.zoomState_1f8 == 0) {
              if(a0._220 == 0) {
                if(worldMapState_800c6698 >= 3 || playerState_800c669c >= 3) {
                  //LAB_800cc900
                  if(Input.pressedThisFrame(InputAction.BUTTON_NORTH)) {
                    if(mapState_800c6798._fc != 1) {
                      if(a0._05 == 0) {
                        if(mapState_800c6798._d8 == 0) {
                          if(a0._250 == 0) {
                            scriptStartEffect(1, 15);
                            mapState_800c6798.disableInput_d0 = true;
                            _800c6690.setu(0x1L);
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }

      return;
    }

    //LAB_800cc970
    struct258_800c66a8.colour_20 -= 0x20;
    if(struct258_800c66a8.colour_20 < 0) {
      struct258_800c66a8.colour_20 = 0;
    }

    //LAB_800cc998
    _800c6690.addu(0x1L);
    if(_800c6690.get() < 0x10L) {
      return;
    }

    pregameLoadingStage_800bb10c.set(4);
    whichMenu_800bdc38 = WhichMenu.INIT_INVENTORY_MENU_1;

    struct258_800c66a8.imageData_2c = new FileData(new byte[0x1_0000]);
    struct258_800c66a8.imageData_30 = new FileData(new byte[0x1_0000]);

    StoreImage(_800c8700, struct258_800c66a8.imageData_2c);
    StoreImage(new RECT((short)320, (short)0, (short)64, (short)512), struct258_800c66a8.imageData_30);

    //LAB_800cca5c
  }

  @Method(0x800cca74L)
  public static void FUN_800cca74() {
    final WMapStruct258 struct = struct258_800c66a8;
    vsyncMode_8007a3b8 = 3;
    scriptStartEffect(2, 15);
    LoadImage(_800c8700, struct.imageData_2c);
    LoadImage(new RECT().set((short)320, (short)0, (short)64, (short)512), struct.imageData_30);
    struct.imageData_2c = null;
    struct.imageData_30 = null;
    FUN_800d1914();

    if(struct.zoomState_1f8 == 0) {
      mapState_800c6798.disableInput_d0 = false;
    }

    //LAB_800ccb6c
    _800c6690.setu(0);
    setProjectionPlaneDistance(1100);
    pregameLoadingStage_800bb10c.set(3);
  }

  @Method(0x800ccb98L)
  public static void FUN_800ccb98() {
    _800ef000[pregameLoadingStage_800bb10c.get()].run();
  }

  @Method(0x800ccbd8L)
  public static void FUN_800ccbd8() {
    // no-op
  }

  @Method(0x800ccbe0L)
  public static void initWmap() {
    resizeDisplay(320, 240);
    vsyncMode_8007a3b8 = 3;
    unloadSoundFile(9);
    loadWmapMusic(gameState_800babc8.chapterIndex_98, 0);
    pregameLoadingStage_800bb10c.set(1);
  }

  @Method(0x800ccc30L)
  public static void waitForWmapMusicToLoad() {
    if((getLoadedDrgnFiles() & 0x80L) == 0) {
      pregameLoadingStage_800bb10c.set(2);
    }

    //LAB_800ccc54
  }

  @Method(0x800ccc64L)
  public static void FUN_800ccc64() {
    setProjectionPlaneDistance(1100);

    //LAB_800ccc84
    for(int i = 0; i < 8; i++) {
      gameState_800babc8.scriptFlags1_13c.setRaw(i, 0);
    }

    FUN_800ccf04();
    _800c6690.setu(0);
    pregameLoadingStage_800bb10c.set(3);
  }

  @Method(0x800cccbcL)
  public static void FUN_800cccbc() {
    FUN_800cd030();
    FUN_800cc83c();
  }

  @Method(0x800ccce4L)
  public static void FUN_800ccce4() {
    gameState_800babc8.areaIndex_4de = mapState_800c6798.areaIndex_12;
    gameState_800babc8.pathIndex_4d8 = mapState_800c6798.pathIndex_14;
    gameState_800babc8.dotIndex_4da = mapState_800c6798.dotIndex_16;
    gameState_800babc8.dotOffset_4dc = mapState_800c6798.dotOffset_18;
    gameState_800babc8.facing_4dd = mapState_800c6798.facing_1c;

    //LAB_800ccd30
    for(int i = 0; i < 8; i++) {
      FUN_8002a3ec((short)i, 0);
    }

    _800c68a8.setu(0);
    pregameLoadingStage_800bb10c.set(5);
  }

  @Method(0x800ccd70L)
  public static void FUN_800ccd70() {
    if((getLoadedDrgnFiles() & 0x80L) == 0) {
      FUN_800cca74();
    }

    //LAB_800ccd94
  }

  @Method(0x800ccda4L)
  public static void FUN_800ccda4() {
    gameState_800babc8.areaIndex_4de = mapState_800c6798.areaIndex_12;
    gameState_800babc8.pathIndex_4d8 = mapState_800c6798.pathIndex_14;
    gameState_800babc8.dotIndex_4da = mapState_800c6798.dotIndex_16;
    gameState_800babc8.dotOffset_4dc = mapState_800c6798.dotOffset_18;
    gameState_800babc8.facing_4dd = mapState_800c6798.facing_1c;

    FUN_800cd278();

    _80052c6c.setu(0);
    engineStateOnceLoaded_8004dd24 = EngineState.SUBMAP_05;
    pregameLoadingStage_800bb10c.set(0);
    vsyncMode_8007a3b8 = 2;
  }

  @Method(0x800cce1cL)
  public static void transitionToCombat() {
    gameState_800babc8.areaIndex_4de = mapState_800c6798.areaIndex_12;
    gameState_800babc8.pathIndex_4d8 = mapState_800c6798.pathIndex_14;
    gameState_800babc8.dotIndex_4da = mapState_800c6798.dotIndex_16;
    gameState_800babc8.dotOffset_4dc = mapState_800c6798.dotOffset_18;
    gameState_800babc8.facing_4dd = mapState_800c6798.facing_1c;

    FUN_800cd030();
    FUN_800cd278();

    _80052c6c.setu(0);
    engineStateOnceLoaded_8004dd24 = EngineState.COMBAT_06;
    pregameLoadingStage_800bb10c.set(0);
    vsyncMode_8007a3b8 = 2;
  }

  @Method(0x800cce9cL)
  public static void FUN_800cce9c() {
    FUN_800cd278();
    _80052c6c.setu(0x1L);
    pregameLoadingStage_800bb10c.set(0);
  }

  @Method(0x800cceccL)
  public static void FUN_800ccecc() {
    FUN_800cd278();
    pregameLoadingStage_800bb10c.set(11);
  }

  @Method(0x800ccef4L)
  public static void FUN_800ccef4() {
    pregameLoadingStage_800bb10c.set(6);
  }

  @Method(0x800ccf04L)
  public static void FUN_800ccf04() {
    struct258_800c66a8 = new WMapStruct258();
    worldMapState_800c6698 = 2;
    playerState_800c669c = 2;
    loadWait = 20;
    _800c66a0.setu(0x2L);
    _800c66a4.setu(0x2L);
    filesLoadedFlags_800c66b8.setu(0);
    zOffset_1f8003e8.set(0);
    tmdGp0Tpage_1f8003ec.set(0x20);
    tempZ_800c66d8.set(0);
    _800c66dc.setu(texPages_800bb110.get(Bpp.BITS_4).get(Translucency.B_PLUS_F).get(TexPageY.Y_0).get());

    FUN_800e3fac(0);
    FUN_800e78c0();
    loadWmapTextures();
    FUN_800d177c();
    FUN_800d8d18();
    FUN_800dfa70();
    FUN_800e4f60();
    FUN_800eb914();
    FUN_800e4e1c();

    if(mapState_800c6798.continentIndex_00 < 3) { // South Serdio, North Serdio, Tiberoa
      FUN_8001eea8(1);
    } else {
      //LAB_800cd004
      FUN_8001eea8(mapState_800c6798.continentIndex_00 + 1);
    }

    //LAB_800cd020
  }

  /** This is a hack to "fix" a bug caused by the game loading too fast. Without this delay, Dart will automatically walk forward a bit when leaving a submap. */
  private static int loadWait = 20;

  @Method(0x800cd030L)
  public static void FUN_800cd030() {
    FUN_800d1d88();
    FUN_800e3ff0();

    switch(worldMapState_800c6698) {
      case 2 -> {
        if((filesLoadedFlags_800c66b8.get() & 0x2) != 0 && (filesLoadedFlags_800c66b8.get() & 0x4) != 0) { // World map textures and mesh loaded
          worldMapState_800c6698 = 3;
        }
      }

      //LAB_800cd0d4
      case 3 -> {
        initMapAnimation();
        worldMapState_800c6698 = 4;
      }

      case 4 -> worldMapState_800c6698 = 5;
      case 5 -> renderWorldMap();
      case 6 -> worldMapState_800c6698 = 7;

      case 7 -> {
        deallocateWorldMap();
        worldMapState_800c6698 = 0;
      }
    }

    //LAB_800cd148
    switch(playerState_800c669c) {
      case 0 -> loadWait = 20;

      case 2 -> {
        if((filesLoadedFlags_800c66b8.get() & 0x2a8L) == 0x2a8L && (filesLoadedFlags_800c66b8.get() & 0x550L) == 0x550L) {
          playerState_800c669c = 3;
        }
      }

      //LAB_800cd1dc
      case 3 -> {
        if(loadWait-- > 10) break;
        FUN_800dfbd8();
        playerState_800c669c = 4;
      }

      case 4 -> {
        if(loadWait-- > 0) break;
        playerState_800c669c = 5;
      }

      case 5 -> renderPlayer();
      case 6 -> playerState_800c669c = 7;

      case 7 -> {
        unloadWmapPlayerModels();
        playerState_800c669c = 0;
      }
    }

    //LAB_800cd250
    FUN_800e4e84();
    renderMapOverlay();
    FUN_800ed95c();
  }

  @Method(0x800cd278L)
  public static void FUN_800cd278() {
    FUN_800d55fc();
    deallocateWorldMap();
    unloadWmapPlayerModels();
    FUN_800e7888();
    FUN_800eede4();
    struct258_800c66a8 = null;
    textZ_800bdf00.set(13);

    //LAB_800cd2d4
    for(int i = 0; i < 8; i++) {
      //LAB_800cd2f0
      clearTextbox(i);
      FUN_8002a3ec(i, 0);
    }

    //LAB_800cd32c
    vsyncMode_8007a3b8 = 2;
  }

  @Method(0x800cd3c8L)
  public static WMapRender40 FUN_800cd3c8(final int a0, final COLOUR colour0, final COLOUR colour1, final COLOUR colour2, final COLOUR colour3, final COLOUR a5, final RECT a6, final int a7, final int a8, final int type, final boolean transparency, final Translucency transparencyMode, final int z) {
    int sp2c = 0;
    int sp30 = 0;
    short x;
    short y;

    final WMapRender40 sp34 = new WMapRender40();

    sp34._28 = a7;
    sp34._2c = a8;
    sp34.count_30 = a7 * a8;
    sp34._34 = (short)a0;
    sp34.x_38 = 0;
    sp34.y_3a = 0;
    sp34.transparency_3c = transparency;
    sp34.z_3e = z;

    if(type == 0) {
      sp34._00 = new WMapRender10[] { new WMapRender10() };
    } else {
      //LAB_800cd4fc
      sp34._00 = new WMapRender10[sp34.count_30];
      Arrays.setAll(sp34._00, i -> new WMapRender10());
    }

    //LAB_800cd534
    FUN_800ce0bc(sp34, type, colour0, colour1, colour2, colour3, a5);

    //LAB_800cd578
    for(int i = 0; i < 2; i++) {
      //LAB_800cd594
      sp34.tpagePacket_04[i] = null;
      sp34.renderPacket_0c[i] = null;
    }

    //LAB_800cd600
    sp34.rects_1c = new RECT[sp34.count_30];
    Arrays.setAll(sp34.rects_1c, i -> new RECT());

    final short w = (short)(a6.w.get() / a7);
    final short h = (short)(a6.h.get() / a8);

    //LAB_800cd6b8
    if(transparency) {
      //LAB_800cd6cc
      for(int i = 0; i < 2; i++) {
        //LAB_800cd6e8
        sp34.tpagePacket_04[i] = new Translucency[sp34.count_30];
      }
    }

    //LAB_800cd748
    //LAB_800cd74c
    for(int i = 0; i < 2; i++) {
      //LAB_800cd768
      sp34.renderPacket_0c[i] = new WMapRender24[sp34.count_30];
      Arrays.setAll(sp34.renderPacket_0c[i], n -> new WMapRender24());
    }

    //LAB_800cd7d0
    //LAB_800cd82c
    for(int i = 0; i < sp34.count_30; i++) {
      final WMapRender24 render0 = sp34.renderPacket_0c[0][i];
      final WMapRender24 render1 = sp34.renderPacket_0c[1][i];

      //LAB_800cd850
      if(transparency) {
        sp34.tpagePacket_04[0][i] = transparencyMode;
        sp34.tpagePacket_04[1][i] = transparencyMode;
      }

      //LAB_800cd8e8
      x = (short)(a6.x.get() + w * sp2c - 160);
      y = (short)(a6.y.get() + h * sp30 - 120);

      render0.x0_08 = x;
      render0.y0_0a = y;
      render0.x1_10 = x + w;
      render0.y1_12 = y;
      render0.x2_18 = x;
      render0.y2_1a = y + h;
      render0.x3_20 = x + w;
      render0.y3_22 = y + h;

      render1.x0_08 = x;
      render1.y0_0a = y;
      render1.x1_10 = x + w;
      render1.y1_12 = y;
      render1.x2_18 = x;
      render1.y2_1a = y + h;
      render1.x3_20 = x + w;
      render1.y3_22 = y + h;

      sp34.rects_1c[i].set(x, y, w, h);

      if(sp2c < a7 - 1) {
        //LAB_800cdb6c
        sp2c++;
      } else {
        sp2c = 0;

        if(sp30 < a8 - 1) {
          sp30++;
        }
      }
    }

    //LAB_800ce094
    //LAB_800ce0a8
    return sp34;
  }

  @Method(0x800ce0bcL)
  public static void FUN_800ce0bc(final WMapRender40 a0, final int type, final COLOUR colour0, final COLOUR colour1, final COLOUR colour2, final COLOUR colour3, final COLOUR a6) {
    a0.type_3f = type;
    FUN_800cf20c(a0._00, type, a0._28, a0._2c, colour0, colour1, colour2, colour3, a6);
    a0._36 = -1;
  }

  @Method(0x800ce4dcL)
  public static void FUN_800ce4dc(final WMapRender40 a0) {
    setRenderColours(a0);

    //LAB_800ce538
    //LAB_800ce5a0
    //LAB_800ce5a4
    for(int i = 0; i < a0.count_30; i++) {
      final WMapRender24 renderPacket = a0.renderPacket_0c[GPU.getDrawBufferIndex()][i];
      final RECT rect = a0.rects_1c[i];

      //LAB_800ce5c8
      final int left = a0.x_38 + rect.x.get();
      final int top = a0.y_3a + rect.y.get();
      final int right = left + rect.w.get();
      final int bottom = top + rect.h.get();
      renderPacket.x0_08 = left;
      renderPacket.y0_0a = top;
      renderPacket.x1_10 = right;
      renderPacket.y1_12 = top;
      renderPacket.x2_18 = left;
      renderPacket.y2_1a = bottom;
      renderPacket.x3_20 = right;
      renderPacket.y3_22 = bottom;

      final GpuCommandPoly cmd = new GpuCommandPoly(4)
        .rgb(0, renderPacket.colour_04)
        .rgb(1, renderPacket.colour_0c)
        .rgb(2, renderPacket.colour_14)
        .rgb(3, renderPacket.colour_1c)
        .pos(0, left, top)
        .pos(1, right, top)
        .pos(2, left, bottom)
        .pos(3, right, bottom);

      if(a0.transparency_3c) {
        cmd.translucent(a0.tpagePacket_04[GPU.getDrawBufferIndex()][i]);
      }

      GPU.queueCommand(a0.z_3e, cmd);
    }

    //LAB_800ce7e8
    //LAB_800cea08
  }

  @Method(0x800cea1cL)
  public static void setRenderColours(final WMapRender40 a0) {
    if(a0._34 < 0) {
      a0._34 = 0;
      //LAB_800cea54
    } else if(a0._34 > 0x100) {
      a0._34 = 0x100;
    }

    //LAB_800cea7c
    if(a0._34 == a0._36) {
      return;
    }

    //LAB_800ceaa0
    //LAB_800ceacc
    //LAB_800ceb38
    int n = 0;
    for(int i = 0; i < a0.count_30; i++) {
      final WMapRender24 sp4 = a0.renderPacket_0c[GPU.getDrawBufferIndex()][i];
      final WMapRender24 sp8 = a0.renderPacket_0c[GPU.getDrawBufferIndex() ^ 1][i];
      final WMapRender10 sp14 = a0._00[n];

      final int r0 = sp14._00.getR() * a0._34 / 0x100;
      final int g0 = sp14._00.getG() * a0._34 / 0x100;
      final int b0 = sp14._00.getB() * a0._34 / 0x100;
      final int r1 = sp14._04.getR() * a0._34 / 0x100;
      final int g1 = sp14._04.getG() * a0._34 / 0x100;
      final int b1 = sp14._04.getB() * a0._34 / 0x100;
      final int r2 = sp14._08.getR() * a0._34 / 0x100;
      final int g2 = sp14._08.getG() * a0._34 / 0x100;
      final int b2 = sp14._08.getB() * a0._34 / 0x100;
      final int r3 = sp14._0c.getR() * a0._34 / 0x100;
      final int g3 = sp14._0c.getG() * a0._34 / 0x100;
      final int b3 = sp14._0c.getB() * a0._34 / 0x100;

      sp4.colour_04.set(r0, g0, b0);
      sp4.colour_0c.set(r1, g1, b1);
      sp4.colour_14.set(r2, g2, b2);
      sp4.colour_1c.set(r3, g3, b3);

      sp8.colour_04.set(r0, g0, b0);
      sp8.colour_0c.set(r1, g1, b1);
      sp8.colour_14.set(r2, g2, b2);
      sp8.colour_1c.set(r3, g3, b3);

      if(a0.type_3f != 0) {
        n++;
      }
    }

    //LAB_800cf1dc
    //LAB_800cf1e4
    a0._36 = a0._34;

    //LAB_800cf1fc
  }

  @Method(0x800cf20cL)
  public static void FUN_800cf20c(final WMapRender10[] a0, final int type, final int a2, final int a3, final COLOUR colour0, final COLOUR colour1, final COLOUR colour2, final COLOUR colour3, final COLOUR a8) {
    int sp14;
    final ColourBlending20 blending = new ColourBlending20();

    switch(type) {
      case 0 -> {
        a0[0]._00.set(colour0);
        a0[0]._04.set(colour1);
        a0[0]._08.set(colour2);
        a0[0]._0c.set(colour3);
      }

      case 1 -> {
        blending.colour0Start_00 = colour0;
        blending.colour0End_04 = colour1;
        blending.colour1Start_08 = colour2;
        blending.colour1End_0c = colour3;
        sp14 = 0;

        //LAB_800cf32c
        for(int sp1c = 0; sp1c < a3; sp1c++) {
          //LAB_800cf34c
          //LAB_800cf350
          for(int sp18 = 0; sp18 < a2; sp18++) {
            final WMapRender10 sp20 = a0[sp14];

            //LAB_800cf370
            blending.colourEndRatio_10 = sp18;
            blending.colourStartRatio_14 = a2 - sp18;
            blending.colour1Ratio_18 = sp1c;
            blending.colour0Ratio_1c = a3 - sp1c;
            blendColours(blending, sp20._00);

            blending.colourEndRatio_10 = sp18 + 1;
            blending.colourStartRatio_14 = a2 - 1 - sp18;
            blending.colour1Ratio_18 = sp1c;
            blending.colour0Ratio_1c = a3 - sp1c;
            blendColours(blending, sp20._04);

            blending.colourEndRatio_10 = sp18;
            blending.colourStartRatio_14 = a2 - sp18;
            blending.colour1Ratio_18 = sp1c + 1;
            blending.colour0Ratio_1c = a3 - 1 - sp1c;
            blendColours(blending, sp20._08);

            blending.colourEndRatio_10 = sp18 + 1;
            blending.colourStartRatio_14 = a2 - 1 - sp18;
            blending.colour1Ratio_18 = sp1c + 1;
            blending.colour0Ratio_1c = a3 - 1 - sp1c;
            blendColours(blending, sp20._0c);

            sp14++;
          }

          //LAB_800cf54c
        }
      }

      //LAB_800cf564
      case 2 -> {
        blending.colour0Start_00 = colour0;
        blending.colour0End_04 = colour1;
        blending.colour1Start_08 = a8;
        blending.colour1End_0c = a8;
        sp14 = 0;

        //LAB_800cf5a4
        for(int sp1c = 0; sp1c < a3 / 2; sp1c++) {
          //LAB_800cf5d8
          //LAB_800cf5dc
          for(int sp18 = 0; sp18 < a2; sp18++) {
            final WMapRender10 sp20 = a0[sp14];

            //LAB_800cf5fc
            blending.colourEndRatio_10 = sp18;
            blending.colourStartRatio_14 = a2 - sp18;
            blending.colour1Ratio_18 = sp1c;
            blending.colour0Ratio_1c = a3 / 2 - sp1c;
            blendColours(blending, sp20._00);

            blending.colourEndRatio_10 = sp18 + 1;
            blending.colourStartRatio_14 = a2 - 1 - sp18;
            blending.colour1Ratio_18 = sp1c;
            blending.colour0Ratio_1c = a3 / 2 - sp1c;
            blendColours(blending, sp20._04);

            blending.colourEndRatio_10 = sp18;
            blending.colourStartRatio_14 = a2 - sp18;
            blending.colour1Ratio_18 = sp1c + 1;
            blending.colour0Ratio_1c = a3 / 2 - 1 - sp1c;
            blendColours(blending, sp20._08);

            blending.colourEndRatio_10 = sp18 + 1;
            blending.colourStartRatio_14 = a2 - 1 - sp18;
            blending.colour1Ratio_18 = sp1c + 1;
            blending.colour0Ratio_1c = a3 / 2 - 1 - sp1c;
            blendColours(blending, sp20._0c);

            sp14++;
          }

          //LAB_800cf820
        }

        //LAB_800cf838
        blending.colour0Start_00 = a8;
        blending.colour0End_04 = a8;
        blending.colour1Start_08 = colour2;
        blending.colour1End_0c = colour3;

        //LAB_800cf870
        for(int sp1c = 0; sp1c < a3 / 2; sp1c++) {
          //LAB_800cf8a4
          //LAB_800cf8a8
          for(int sp18 = 0; sp18 < a2; sp18++) {
            final WMapRender10 sp20 = a0[sp14];

            //LAB_800cf8c8
            blending.colourEndRatio_10 = sp18;
            blending.colourStartRatio_14 = a2 - sp18;
            blending.colour1Ratio_18 = sp1c;
            blending.colour0Ratio_1c = a3 / 2 - sp1c;
            blendColours(blending, sp20._00);

            blending.colourEndRatio_10 = sp18 + 1;
            blending.colourStartRatio_14 = a2 - 1 - sp18;
            blending.colour1Ratio_18 = sp1c;
            blending.colour0Ratio_1c = a3 / 2 - sp1c;
            blendColours(blending, sp20._04);

            blending.colourEndRatio_10 = sp18;
            blending.colourStartRatio_14 = a2 - sp18;
            blending.colour1Ratio_18 = sp1c + 1;
            blending.colour0Ratio_1c = a3 / 2 - 1 - sp1c;
            blendColours(blending, sp20._08);

            blending.colourEndRatio_10 = sp18 + 1;
            blending.colourStartRatio_14 = a2 - 1 - sp18;
            blending.colour1Ratio_18 = sp1c + 1;
            blending.colour0Ratio_1c = a3 / 2 - 1 - sp1c;
            blendColours(blending, sp20._0c);

            sp14++;
          }

          //LAB_800cfaec
        }
      }

      //LAB_800cfb04
      case 3 -> {
        blending.colour0Start_00 = colour0;
        blending.colour0End_04 = a8;
        blending.colour1Start_08 = colour2;
        blending.colour1End_0c = a8;
        sp14 = 0;

        //LAB_800cfb50
        for(int sp1c = 0; sp1c < a3; sp1c++) {
          //LAB_800cfb70
          //LAB_800cfb74
          for(int sp18 = 0; sp18 < a2 / 2; sp18++) {
            final WMapRender10 sp20 = a0[sp14];

            //LAB_800cfba8
            blending.colourEndRatio_10 = sp18;
            blending.colourStartRatio_14 = a2 / 2 - sp18;
            blending.colour1Ratio_18 = sp1c;
            blending.colour0Ratio_1c = a3 - sp1c;
            blendColours(blending, sp20._00);

            blending.colourEndRatio_10 = sp18 + 1;
            blending.colourStartRatio_14 = a2 / 2 - 1 - sp18;
            blending.colour1Ratio_18 = sp1c;
            blending.colour0Ratio_1c = a3 - sp1c;
            blendColours(blending, sp20._04);

            blending.colourEndRatio_10 = sp18;
            blending.colourStartRatio_14 = a2 / 2 - sp18;
            blending.colour1Ratio_18 = sp1c + 1;
            blending.colour0Ratio_1c = a3 - 1 - sp1c;
            blendColours(blending, sp20._08);

            blending.colourEndRatio_10 = sp18 + 1;
            blending.colourStartRatio_14 = a2 / 2 - 1 - sp18;
            blending.colour1Ratio_18 = sp1c + 1;
            blending.colour0Ratio_1c = a3 - 1 - sp1c;
            blendColours(blending, sp20._0c);

            sp14++;
          }

          //LAB_800cfdcc
          sp14 += a2 / 2;
        }

        //LAB_800cfe14
        blending.colour0Start_00 = a8;
        blending.colour0End_04 = colour1;
        blending.colour1Start_08 = a8;
        blending.colour1End_0c = colour3;
        sp14 = a2 / 2;

        //LAB_800cfe7c
        for(int sp1c = 0; sp1c < a3; sp1c++) {
          //LAB_800cfe9c
          //LAB_800cfea0
          for(int sp18 = 0; sp18 < a2 / 2; sp18++) {
            final WMapRender10 sp20 = a0[sp14];

            //LAB_800cfed4
            blending.colourEndRatio_10 = sp18;
            blending.colourStartRatio_14 = a2 / 2 - sp18;
            blending.colour1Ratio_18 = sp1c;
            blending.colour0Ratio_1c = a3 - sp1c;
            blendColours(blending, sp20._00);

            blending.colourEndRatio_10 = sp18 + 1;
            blending.colourStartRatio_14 = a2 / 2 - 1 - sp18;
            blending.colour1Ratio_18 = sp1c;
            blending.colour0Ratio_1c = a3 - sp1c;
            blendColours(blending, sp20._04);

            blending.colourEndRatio_10 = sp18;
            blending.colourStartRatio_14 = a2 / 2 - sp18;
            blending.colour1Ratio_18 = sp1c + 1;
            blending.colour0Ratio_1c = a3 - 1 - sp1c;
            blendColours(blending, sp20._08);

            blending.colourEndRatio_10 = sp18 + 1;
            blending.colourStartRatio_14 = a2 / 2 - 1 - sp18;
            blending.colour1Ratio_18 = sp1c + 1;
            blending.colour0Ratio_1c = a3 - 1 - sp1c;
            blendColours(blending, sp20._0c);

            sp14++;
          }

          //LAB_800d00f8
          sp14 += a2 / 2;
        }
      }

      //LAB_800d0140
      case 4 -> {
        final COLOUR sp0x28 = new COLOUR();
        final COLOUR sp0x30 = new COLOUR();
        final COLOUR sp0x38 = new COLOUR();
        final COLOUR sp0x40 = new COLOUR();
        blending.colour0Start_00 = colour0;
        blending.colour0End_04 = colour1;
        blending.colour1Start_08 = colour2;
        blending.colour1End_0c = colour3;
        blending.colourEndRatio_10 = a2 / 2;
        blending.colourStartRatio_14 = a2 / 2;
        blending.colour1Ratio_18 = 0;
        blending.colour0Ratio_1c = a3;
        blendColours(blending, sp0x28);
        blending.colourEndRatio_10 = 0;
        blending.colourStartRatio_14 = a2;
        blending.colour1Ratio_18 = a3 / 2;
        blending.colour0Ratio_1c = a3 / 2;
        blendColours(blending, sp0x30);
        blending.colourEndRatio_10 = a2;
        blending.colourStartRatio_14 = 0;
        blending.colour1Ratio_18 = a3 / 2;
        blending.colour0Ratio_1c = a3 / 2;
        blendColours(blending, sp0x38);
        blending.colourEndRatio_10 = a2 / 2;
        blending.colourStartRatio_14 = a2 / 2;
        blending.colour1Ratio_18 = a3;
        blending.colour0Ratio_1c = 0;
        blendColours(blending, sp0x40);
        blending.colour0Start_00 = colour0;
        blending.colour0End_04 = sp0x28;
        blending.colour1Start_08 = sp0x30;
        blending.colour1End_0c = a8;
        sp14 = 0;

        //LAB_800d0334
        for(int sp1c = 0; sp1c < a3 / 2; sp1c++) {
          //LAB_800d0368
          //LAB_800d036c
          for(int sp18 = 0; sp18 < a2 / 2; sp18++) {
            final WMapRender10 sp20 = a0[sp14];

            //LAB_800d03a0
            blending.colourEndRatio_10 = sp18;
            blending.colourStartRatio_14 = a2 / 2 - sp18;
            blending.colour1Ratio_18 = sp1c;
            blending.colour0Ratio_1c = a3 / 2 - sp1c;
            blendColours(blending, sp20._00);

            blending.colourEndRatio_10 = sp18 + 1;
            blending.colourStartRatio_14 = a2 / 2 - 1 - sp18;
            blending.colour1Ratio_18 = sp1c;
            blending.colour0Ratio_1c = a3 / 2 - sp1c;
            blendColours(blending, sp20._04);

            blending.colourEndRatio_10 = sp18;
            blending.colourStartRatio_14 = a2 / 2 - sp18;
            blending.colour1Ratio_18 = sp1c + 1;
            blending.colour0Ratio_1c = a3 / 2 - 1 - sp1c;
            blendColours(blending, sp20._08);

            blending.colourEndRatio_10 = sp18 + 1;
            blending.colourStartRatio_14 = a2 / 2 - 1 - sp18;
            blending.colour1Ratio_18 = sp1c + 1;
            blending.colour0Ratio_1c = a3 / 2 - 1 - sp1c;
            blendColours(blending, sp20._0c);

            sp14++;
          }

          //LAB_800d060c
          sp14 += a2 / 2;
        }

        //LAB_800d0654
        blending.colour0Start_00 = sp0x28;
        blending.colour0End_04 = colour1;
        blending.colour1Start_08 = a8;
        blending.colour1End_0c = sp0x38;
        sp14 = a2 / 2;

        //LAB_800d06b4
        for(int sp1c = 0; sp1c < a3 / 2; sp1c++) {
          //LAB_800d06e8
          //LAB_800d06ec
          for(int sp18 = 0; sp18 < a2 / 2; sp18++) {
            final WMapRender10 sp20 = a0[sp14];

            //LAB_800d0720
            blending.colourEndRatio_10 = sp18;
            blending.colourStartRatio_14 = a2 / 2 - sp18;
            blending.colour1Ratio_18 = sp1c;
            blending.colour0Ratio_1c = a3 / 2 - sp1c;
            blendColours(blending, sp20._00);

            blending.colourEndRatio_10 = sp18 + 1;
            blending.colourStartRatio_14 = a2 / 2 - 1 - sp18;
            blending.colour1Ratio_18 = sp1c;
            blending.colour0Ratio_1c = a3 / 2 - sp1c;
            blendColours(blending, sp20._04);

            blending.colourEndRatio_10 = sp18;
            blending.colourStartRatio_14 = a2 / 2 - sp18;
            blending.colour1Ratio_18 = sp1c + 1;
            blending.colour0Ratio_1c = a3 / 2 - 1 - sp1c;
            blendColours(blending, sp20._08);

            blending.colourEndRatio_10 = sp18 + 1;
            blending.colourStartRatio_14 = a2 / 2 - 1 - sp18;
            blending.colour1Ratio_18 = sp1c + 1;
            blending.colour0Ratio_1c = a3 / 2 - 1 - sp1c;
            blendColours(blending, sp20._0c);

            sp14++;
          }

          //LAB_800d098c
          sp14 += a2 / 2;
        }

        //LAB_800d09d4
        blending.colour0Start_00 = sp0x30;
        blending.colour0End_04 = a8;
        blending.colour1Start_08 = colour2;
        blending.colour1End_0c = sp0x40;
        sp14 = a2 * a3 / 2;

        //LAB_800d0a40
        for(int sp1c = 0; sp1c < a3 / 2; sp1c++) {
          //LAB_800d0a74
          //LAB_800d0a78
          for(int sp18 = 0; sp18 < a2 / 2; sp18++) {
            final WMapRender10 sp20 = a0[sp14];

            //LAB_800d0aac
            blending.colourEndRatio_10 = sp18;
            blending.colourStartRatio_14 = a2 / 2 - sp18;
            blending.colour1Ratio_18 = sp1c;
            blending.colour0Ratio_1c = a3 / 2 - sp1c;
            blendColours(blending, sp20._00);

            blending.colourEndRatio_10 = sp18 + 1;
            blending.colourStartRatio_14 = a2 / 2 - 1 - sp18;
            blending.colour1Ratio_18 = sp1c;
            blending.colour0Ratio_1c = a3 / 2 - sp1c;
            blendColours(blending, sp20._04);

            blending.colourEndRatio_10 = sp18;
            blending.colourStartRatio_14 = a2 / 2 - sp18;
            blending.colour1Ratio_18 = sp1c + 1;
            blending.colour0Ratio_1c = a3 / 2 - 1 - sp1c;
            blendColours(blending, sp20._08);

            blending.colourEndRatio_10 = sp18 + 1;
            blending.colourStartRatio_14 = a2 / 2 - 1 - sp18;
            blending.colour1Ratio_18 = sp1c + 1;
            blending.colour0Ratio_1c = a3 / 2 - 1 - sp1c;
            blendColours(blending, sp20._0c);

            sp14++;
          }

          //LAB_800d0d18
          sp14 += a2 / 2;
        }

        //LAB_800d0d60
        blending.colour0Start_00 = a8;
        blending.colour0End_04 = sp0x38;
        blending.colour1Start_08 = sp0x40;
        blending.colour1End_0c = colour3;
        sp14 = a2 * a3 / 2 + a2 / 2;

        //LAB_800d0df0
        for(int sp1c = 0; sp1c < a3 / 2; sp1c++) {
          //LAB_800d0e24
          //LAB_800d0e28
          for(int sp18 = 0; sp18 < a2 / 2; sp18++) {
            final WMapRender10 sp20 = a0[sp14];

            //LAB_800d0e5c
            blending.colourEndRatio_10 = sp18;
            blending.colourStartRatio_14 = a2 / 2 - sp18;
            blending.colour1Ratio_18 = sp1c;
            blending.colour0Ratio_1c = a3 / 2 - sp1c;
            blendColours(blending, sp20._00);

            blending.colourEndRatio_10 = sp18 + 1;
            blending.colourStartRatio_14 = a2 / 2 - 1 - sp18;
            blending.colour1Ratio_18 = sp1c;
            blending.colour0Ratio_1c = a3 / 2 - sp1c;
            blendColours(blending, sp20._04);

            blending.colourEndRatio_10 = sp18;
            blending.colourStartRatio_14 = a2 / 2 - sp18;
            blending.colour1Ratio_18 = sp1c + 1;
            blending.colour0Ratio_1c = a3 / 2 - 1 - sp1c;
            blendColours(blending, sp20._08);

            blending.colourEndRatio_10 = sp18 + 1;
            blending.colourStartRatio_14 = a2 / 2 - 1 - sp18;
            blending.colour1Ratio_18 = sp1c + 1;
            blending.colour0Ratio_1c = a3 / 2 - 1 - sp1c;
            blendColours(blending, sp20._0c);

            sp14++;
          }

          //LAB_800d10c8
          sp14 += a2 / 2;
        }
      }

      //LAB_800d1110
    }

    //LAB_800d1118
  }

  @Method(0x800d112cL)
  public static void blendColours(final ColourBlending20 a0, final COLOUR out) {
    final int r0 = (a0.colour0End_04.getR() * a0.colourEndRatio_10 + a0.colour0Start_00.getR() * a0.colourStartRatio_14) / (a0.colourEndRatio_10 + a0.colourStartRatio_14);
    final int g0 = (a0.colour0End_04.getG() * a0.colourEndRatio_10 + a0.colour0Start_00.getG() * a0.colourStartRatio_14) / (a0.colourEndRatio_10 + a0.colourStartRatio_14);
    final int b0 = (a0.colour0End_04.getB() * a0.colourEndRatio_10 + a0.colour0Start_00.getB() * a0.colourStartRatio_14) / (a0.colourEndRatio_10 + a0.colourStartRatio_14);

    final int r1 = (a0.colour1End_0c.getR() * a0.colourEndRatio_10 + a0.colour1Start_08.getR() * a0.colourStartRatio_14) / (a0.colourEndRatio_10 + a0.colourStartRatio_14);
    final int g1 = (a0.colour1End_0c.getG() * a0.colourEndRatio_10 + a0.colour1Start_08.getG() * a0.colourStartRatio_14) / (a0.colourEndRatio_10 + a0.colourStartRatio_14);
    final int b1 = (a0.colour1End_0c.getB() * a0.colourEndRatio_10 + a0.colour1Start_08.getB() * a0.colourStartRatio_14) / (a0.colourEndRatio_10 + a0.colourStartRatio_14);

    out.r.set((a0.colour1Ratio_18 * r1 + a0.colour0Ratio_1c * r0) / (a0.colour1Ratio_18 + a0.colour0Ratio_1c));
    out.g.set((a0.colour1Ratio_18 * g1 + a0.colour0Ratio_1c * g0) / (a0.colour1Ratio_18 + a0.colour0Ratio_1c));
    out.b.set((a0.colour1Ratio_18 * b1 + a0.colour0Ratio_1c * b0) / (a0.colour1Ratio_18 + a0.colour0Ratio_1c));
  }

  @Method(0x800d177cL)
  public static void FUN_800d177c() {
    _800c66b0 = new WMapStruct19c0();

    GsInitCoordinate2(null, _800c66b0.coord2_20);

    _800c66b0.coord2_20.coord.transfer.set(0, 0, 0);
    _800c66b0.mapRotation_70.zero();
    _800c66b0.rview2_00.viewpoint_00.set(0.0f, -300.0f, -900.0f);
    _800c66b0.rview2_00.refpoint_0c.set(0.0f, 300.0f, 900.0f);
    _800c66b0.rview2_00.viewpointTwist_18 = 0;
    _800c66b0.rview2_00.super_1c = _800c66b0.coord2_20;

    FUN_800d1d28();
    FUN_800d1914();

    _800c66b0._114 = 0;
    _800c66b0.projectionPlaneDistance_118 = 1100;
    _800c66b0._11a = 0;
  }

  @Method(0x800d1914L)
  public static void FUN_800d1914() {
    final WMapStruct19c0 v0 = _800c66b0;

    clearRed_8007a3a8.set(0);
    clearGreen_800bb104.set(0);
    clearBlue_800babc0.set(0);

    v0._154[0].index_00 = -1;
    v0._196c = 0;
    v0._1970 = 0;
    v0._1974 = -1;

    FUN_800d1db8();

    //LAB_800d1984
    for(int i = 0; i < 3; i++) {
      //LAB_800d19a0
      v0._19a8[i] = Math.toRadians(15);
      v0._19ae[i] = Math.toRadians(315);

      final GsF_LIGHT light = v0.lights_11c[i];
      light.r_0c = 0.125f;
      light.g_0d = 0.125f;
      light.b_0e = 0.125f;
      light.direction_00.x = MathHelper.sin(v0._19a8[i]);
      light.direction_00.y = MathHelper.cos(v0._19ae[i]);
      light.direction_00.z = MathHelper.cosFromSin(v0._19a8[i], light.direction_00.x);
      light.direction_00.set(0.24414062f, 0.024414062f, 0.0f);
      GsSetFlatLight(i, light);
    }

    //LAB_800d1c88
    v0.ambientLight_14c.set(0.375f, 0.375f, 0.375f);
    GTE.setBackgroundColour(v0.ambientLight_14c.x, v0.ambientLight_14c.y, v0.ambientLight_14c.z);
    v0._88 = 0;
  }

  @Method(0x800d1d28L)
  public static void FUN_800d1d28() {
    _800c66b0.mapRotating_80 = false;
    _800c66b0.mapRotationStep_7c = 0.0f;
    _800c66b0._c5 = 0;
    _800c66b0._c4 = 0;

    FUN_800d5018();
  }

  @Method(0x800d1d88L)
  public static void FUN_800d1d88() {
    FUN_800d1db8();
    FUN_800d2d90();
    updateLights();
  }

  @Method(0x800d1db8L)
  public static void FUN_800d1db8() {
    final WMapStruct258 v0 = struct258_800c66a8;
    final long x = v0.coord2_34.coord.transfer.getX();
    final long y = v0.coord2_34.coord.transfer.getY();
    final long z = v0.coord2_34.coord.transfer.getZ();

    //LAB_800d1e14
    int count = 0;
    for(int i = 0; i < mapState_800c6798.locationCount_08; i++) {
      //LAB_800d1e38
      if(!places_800f0234.get(locations_800f0e34.get(i).placeIndex_02.get()).name_00.isNull()) {
        //LAB_800d1e90
        if(FUN_800eb09c(i, 1, _800c66b0._154[count].vec_08) == 0) {
          //LAB_800d1ee0
          final float dx = x - _800c66b0._154[count].vec_08.x;
          final float dy = y - _800c66b0._154[count].vec_08.y;
          final float dz = z - _800c66b0._154[count].vec_08.z;

          _800c66b0._154[count].index_00 = i;
          _800c66b0._154[count].vecLength_04 = Math.sqrt(dx * dx + dy * dy + dz * dz);

          count++;
        }
      }

      //LAB_800d2070
    }

    //LAB_800d2088
    _800c66b0._154[count].index_00 = -1;
    Arrays.sort(_800c66b0._154, Comparator.comparingDouble(a -> a.vecLength_04));
  }

  @Method(0x800d219cL)
  public static void updateLights() {
    if(struct258_800c66a8.zoomState_1f8 == 0) {
      return;
    }

    //LAB_800d21cc
    if(struct258_800c66a8.zoomState_1f8 == 2 || struct258_800c66a8.zoomState_1f8 == 3 || struct258_800c66a8.zoomState_1f8 == 4) {
      //LAB_800d2228
      final int v0 = _800c66b0._88;

      if(v0 == 0 || v0 == 1) {
        if(v0 == 0) {
          //LAB_800d2258
          //LAB_800d225c
          for(int i = 0; i < 3; i++) {
            //LAB_800d2278
            final WMapStruct19c0 struct = _800c66b0;
            struct.colour_8c[i].setR((int)(struct.lights_11c[i].r_0c * 0x100));
            struct.colour_8c[i].setG((int)(struct.lights_11c[i].g_0d * 0x100));
            struct.colour_8c[i].setB((int)(struct.lights_11c[i].b_0e * 0x100));
          }

          //LAB_800d235c
          _800c66b0._84 = 1.0f;
          _800c66b0._88 = 1;
        }

        //LAB_800d237c
        _800c66b0._84 -= 0.140625f;

        if(_800c66b0._84 < 0.25f) {
          _800c66b0._84 = 0.125f;
          _800c66b0._88 = 2;
        }

        //LAB_800d23e0
        //LAB_800d23e4
        for(int i = 0; i < 3; i++) {
          final GsF_LIGHT light = _800c66b0.lights_11c[i];

          //LAB_800d2400
          //LAB_800d2464
          //LAB_800d24d0
          //LAB_800d253c
          light.r_0c = _800c66b0.colour_8c[i].getR() * _800c66b0._84 / 0x100;
          light.g_0d = _800c66b0.colour_8c[i].getG() * _800c66b0._84 / 0x100;
          light.b_0e = _800c66b0.colour_8c[i].getB() * _800c66b0._84 / 0x100;
          GsSetFlatLight(i, _800c66b0.lights_11c[i]);
        }
      }
    }

    //LAB_800d2590
    //LAB_800d2598
    if(struct258_800c66a8.zoomState_1f8 != 5 && struct258_800c66a8.zoomState_1f8 != 6) {
      return;
    }

    //LAB_800d25d8
    final int v0 = _800c66b0._88;
    if(v0 == 2) {
      //LAB_800d2608
      _800c66b0._84 = 0.25f;
      _800c66b0._88 = 3;
    } else if(v0 == 3) {
      //LAB_800d2628
      _800c66b0._84 += 0.140625f;

      if(_800c66b0._84 > 1.0f) {
        _800c66b0._84 = 1.0f;
        _800c66b0._88 = 0;
      }

      //LAB_800d268c
      //LAB_800d2690
      for(int i = 0; i < 3; i++) {
        final GsF_LIGHT light = _800c66b0.lights_11c[i];

        //LAB_800d26ac
        //LAB_800d2710
        //LAB_800d277c
        //LAB_800d27e8
        light.r_0c = _800c66b0.colour_8c[i].getR() * _800c66b0._84 / 0x100;
        light.g_0d = _800c66b0.colour_8c[i].getG() * _800c66b0._84 / 0x100;
        light.b_0e = _800c66b0.colour_8c[i].getB() * _800c66b0._84 / 0x100;
        GsSetFlatLight(i, _800c66b0.lights_11c[i]);
      }
    }

    //LAB_800d283c
    //LAB_800d2844
  }

  @Method(0x800d2d90L)
  public static void FUN_800d2d90() {
    FUN_800d5288();

    final WMapStruct19c0 struct = _800c66b0;

    rotateCoord2(struct.mapRotation_70, struct.coord2_20);

    if(struct._c5 == 0) {
      if(struct._c4 == 0) {
        if(struct258_800c66a8.zoomState_1f8 == 0) {
          if(struct258_800c66a8._220 == 0) {
            struct.coord2_20.coord.transfer.set(struct258_800c66a8.coord2_34.coord.transfer);
          }
        }
      }
    }

    //LAB_800d2ec4
    GsSetRefView2L(struct.rview2_00);
    FUN_800d2fa8();
    FUN_800d3fc8();

    MathHelper.floorMod(struct.mapRotation_70, MathHelper.TWO_PI);
    struct.mapRotationEndAngle_7a = MathHelper.floorMod(struct.mapRotationEndAngle_7a, MathHelper.TWO_PI);
  }

  @Method(0x800d2fa8L)
  public static void FUN_800d2fa8() {
    if(struct258_800c66a8._250 == 1) {
      return;
    }

    //LAB_800d2fd4
    if(struct258_800c66a8._250 == 2 && struct258_800c66a8._05 == 0) {
      return;
    }

    final WMapStruct19c0 struct = _800c66b0;

    //LAB_800d3014
    if(struct.mapRotationStep_7c == 0.0f) {
      struct.mapRotating_80 = false;
    }

    //LAB_800d3040
    if(struct._110 == 0) {
      if(struct258_800c66a8.zoomState_1f8 == 0) {
        if(struct._c4 == 0) {
          if(mapState_800c6798.continentIndex_00 != 7) { // Not teleporting
            if(!struct.mapRotating_80) {
              //LAB_800d30d8
              if(Input.pressedThisFrame(InputAction.BUTTON_SHOULDER_RIGHT_1)) { // R1
                startMapRotation(1);
                struct.mapRotating_80 = true;
              }

              //LAB_800d310c
              if(Input.pressedThisFrame(InputAction.BUTTON_SHOULDER_LEFT_1)) { // L1
                startMapRotation(-1);
                struct.mapRotating_80 = true;
              }

              //LAB_800d3140
            } else {
              //LAB_800d3148
              struct.mapRotation_70.y += struct.mapRotationStep_7c;
              struct.mapRotationCounter_7e++;

              if(struct.mapRotationCounter_7e > 5) {
                struct.mapRotation_70.y = struct.mapRotationEndAngle_7a;
                struct.mapRotating_80 = false;
              }
            }
          }
        }
      }
    }

    //LAB_800d31e8
    FUN_800d35fc();

    final long v0 = _800c66b0._110;
    if(v0 == 1) {
      //LAB_800d3250
      FUN_800d5018();
      _800c66b0._110 = 2;
    } else if(v0 == 3) {
      //LAB_800d3434
      _800c66b0.rview2_00.viewpoint_00.y = _800c66b0.rview2_c8.viewpoint_00.y + _800c66b0.viewpointY_ec * _800c66b0._10e;
      _800c66b0.rview2_00.viewpoint_00.z = _800c66b0.rview2_c8.viewpoint_00.z + _800c66b0.viewpointZ_f0 * _800c66b0._10e;
      _800c66b0.rview2_00.refpoint_0c.y = _800c66b0.rview2_c8.refpoint_0c.y + _800c66b0.refpointY_f8 * _800c66b0._10e;
      _800c66b0.rview2_00.refpoint_0c.z = _800c66b0.rview2_c8.refpoint_0c.z + _800c66b0.refpointZ_fc * _800c66b0._10e;
      _800c66b0.mapRotation_70.y = _800c66b0.angle_10a + _800c66b0.angle_10c * _800c66b0._10e;

      if(_800c66b0._10e > 0) {
        _800c66b0._10e--;
      } else {
        _800c66b0._110 = 0;
      }

      return;
    } else if((int)v0 < 2) {
      //LAB_800d3248
      return;
    }

    // if == 1 or 2

    //LAB_800d3228
    //LAB_800d3268
    _800c66b0.rview2_00.viewpoint_00.y = _800c66b0.rview2_c8.viewpoint_00.y + _800c66b0.viewpointY_ec * _800c66b0._10e;
    _800c66b0.rview2_00.viewpoint_00.z = _800c66b0.rview2_c8.viewpoint_00.z + _800c66b0.viewpointZ_f0 * _800c66b0._10e;
    _800c66b0.rview2_00.refpoint_0c.y = _800c66b0.rview2_c8.refpoint_0c.y + _800c66b0.refpointY_f8 * _800c66b0._10e;
    _800c66b0.rview2_00.refpoint_0c.z = _800c66b0.rview2_c8.refpoint_0c.z + _800c66b0.refpointZ_fc * _800c66b0._10e;
    _800c66b0.mapRotation_70.y = _800c66b0.angle_10a + _800c66b0.angle_10c * _800c66b0._10e;

    _800c66b0._10e++;
    if((short)_800c66b0._10e >= 16) {
      _800c66b0._10e = 16;
      _800c66b0.mapRotation_70.y = _800c66b0.angle_108;
    }

    //LAB_800d342c
    //LAB_800d35e4
    //LAB_800d35ec
  }

  @Method(0x800d35fcL)
  public static void FUN_800d35fc() {
    final int v0 = _800c66b0._c5;
    if(v0 == 0) {
      //LAB_800d3654
      //LAB_800d3670
      //LAB_800d368c
      if(mapState_800c6798.continentIndex_00 != 7 && mapState_800c6798._d8 == 0 && _800c6690.get() == 0) {
        //LAB_800d36a8
        if(mapState_800c6798._fc != 1) {
          if(!_800c66b0.mapRotating_80) {
            if(struct258_800c66a8._05 == 0) {
              if(_800c66b0._110 == 0) {
                if(Input.pressedThisFrame(InputAction.BUTTON_SHOULDER_RIGHT_2)) { // R2
                  if(struct258_800c66a8.zoomState_1f8 == 0) {
                    playSound(0, 4, 0, 0, (short)0, (short)0);
                    _800c66b0._9e = -9000;
                    _800c66b0._c5 = 1;
                    _800c66b0._11a = 1;
                    FUN_800d4bc8(0);
                    mapState_800c6798.disableInput_d0 = true;
                    _800c66b0._c4 = 1;
                  }
                }

                //LAB_800d37bc
                if(Input.pressedThisFrame(InputAction.BUTTON_SHOULDER_LEFT_2)) { // L2
                  if(struct258_800c66a8.zoomState_1f8 == 1 || struct258_800c66a8.zoomState_1f8 == 6) {
                    //LAB_800d3814
                    FUN_8002a3ec(7, 0);
                    playSound(0, 4, 0, 0, (short)0, (short)0);
                    _800c66b0._9e = -300;
                    _800c66b0._c5 = 2;
                    FUN_800d4bc8(1);
                    _800c66b0._c4 = 0;
                    struct258_800c66a8.zoomState_1f8 = 0;
                    //LAB_800d3898
                  } else if(struct258_800c66a8.zoomState_1f8 == 0) {
                    playSound(0, 0x28, 0, 0, (short)0, (short)0);
                  }
                }
              }
            }
          }
        }
      }
    } else if(v0 == 1) {
      //LAB_800d38dc
      _800c66b0.rview2_00.viewpoint_00.y -= 1450.0f;
      _800c66b0.rview2_00.refpoint_0c.y += 1450.0f;
      _800c66b0.mapRotation_70.y = _800c66b0.angle_9a + _800c66b0.angle_9c * _800c66b0._a0;
      _800c66b0.vec_b4.add(_800c66b0.vec_a4);
      _800c66b0.coord2_20.coord.transfer.setX(struct258_800c66a8.coord2_34.coord.transfer.getX() - _800c66b0.vec_b4.getX() / 0x100);
      _800c66b0.coord2_20.coord.transfer.setY(struct258_800c66a8.coord2_34.coord.transfer.getY() - _800c66b0.vec_b4.getY() / 0x100);
      _800c66b0.coord2_20.coord.transfer.setZ(struct258_800c66a8.coord2_34.coord.transfer.getZ() - _800c66b0.vec_b4.getZ() / 0x100);
      _800c66b0._a0++;

      if(_800c66b0._a0 >= 6) {
        _800c66b0.rview2_00.viewpoint_00.y = _800c66b0._9e;
        _800c66b0.rview2_00.refpoint_0c.y = -_800c66b0._9e;
        _800c66b0.mapRotation_70.y = _800c66b0.angle_98;
        _800c66b0.coord2_20.coord.transfer.set(0, 0, 0);
        _800c66b0._c5 = 0;
        struct258_800c66a8.zoomState_1f8 = 1;
      }
    } else if(v0 == 2) {
      //LAB_800d3bd8
      if(struct258_800c66a8._05 == 0) {
        _800c66b0.rview2_00.viewpoint_00.y += 1450.0f;
        _800c66b0.rview2_00.refpoint_0c.y -= 1450.0f;
      } else {
        //LAB_800d3c44
        _800c66b0.rview2_00.viewpoint_00.y += 290.0f;
        _800c66b0.rview2_00.refpoint_0c.y -= 290.0f;
      }

      //LAB_800d3c8c
      _800c66b0.mapRotation_70.y = _800c66b0.angle_9a + _800c66b0.angle_9c * _800c66b0._a0;
      _800c66b0.vec_b4.add(_800c66b0.vec_a4);
      _800c66b0.coord2_20.coord.transfer.setX(_800c66b0.vec_b4.getX() >> 8);
      _800c66b0.coord2_20.coord.transfer.setY(_800c66b0.vec_b4.getY() >> 8);
      _800c66b0.coord2_20.coord.transfer.setZ(_800c66b0.vec_b4.getZ() >> 8);
      _800c66b0._a0++;

      boolean sp18 = false;
      if(struct258_800c66a8._05 == 0) {
        if(_800c66b0._a0 >= 6) {
          sp18 = true;
        }

        //LAB_800d3e78
        //LAB_800d3e80
      } else if(_800c66b0._a0 >= 30) {
        sp18 = true;
      }

      //LAB_800d3ea8
      if(sp18) {
        _800c66b0.rview2_00.viewpoint_00.y = _800c66b0._9e;
        _800c66b0.rview2_00.refpoint_0c.y = -_800c66b0._9e;
        _800c66b0.mapRotation_70.y = _800c66b0.angle_98;
        _800c66b0.coord2_20.coord.transfer.set(struct258_800c66a8.coord2_34.coord.transfer);
        _800c66b0._c5 = 0;
        mapState_800c6798.disableInput_d0 = false;
        struct258_800c66a8.zoomState_1f8 = 0;
      }
    }

    //LAB_800d38d4
    //LAB_800d3bd0
    //LAB_800d3fa4
    //LAB_800d3fac
    renderPlayerAndDestinationIndicators();
  }

  @Method(0x800d3fc8L)
  public static void FUN_800d3fc8() {
    if(struct258_800c66a8._250 == 1) {
      //LAB_800d401c
      _800c66b0.mapRotation_70.y += MathHelper.psxDegToRad(8);
    }
  }

  @Method(0x800d4058L)
  public static void renderPlayerAndDestinationIndicators() {
    //LAB_800d4088
    if(_800c66b0._c4 == 0 || _800c66b0._c5 != 0) {
      //LAB_800d41f0
      return;
    }

    //LAB_800d40ac
    final int zoomState = struct258_800c66a8.zoomState_1f8;

    final int size;
    final int v;
    if(zoomState == 1) {
      //LAB_800d4108
      _800c86f0.setu(0);
      size = 16;
      v = 32;
    } else if(zoomState == 4) {
      //LAB_800d4170
      if(Input.pressedThisFrame(InputAction.BUTTON_SHOULDER_LEFT_2)) { // L2
        FUN_8002a3ec(7, 0);
      }

      //LAB_800d4198
      size = 8;
      v = 48;

      //LAB_800d40e8
    } else if(zoomState == 5) {
      //LAB_800d41b0
      FUN_8002a3ec(7, 0);

      if(Input.pressedThisFrame(InputAction.BUTTON_SHOULDER_RIGHT_2)) { // R2
        _800c86f0.setu(0);
      }

      //LAB_800d41e0
      return;
    } else if(zoomState == 6) {
      //LAB_800d4128
      FUN_8002a3ec(7, 0);

      if(Input.pressedThisFrame(InputAction.BUTTON_SHOULDER_RIGHT_2)) { // R2
        _800c86f0.setu(0);
      }

      //LAB_800d4158
      size = 16;
      v = 32;
    } else {
      return;
    }

    //LAB_800d41f8
    final MATRIX sp0x38 = new MATRIX();
    rotateCoord2(struct258_800c66a8.tmdRendering_08.rotations_08[0], struct258_800c66a8.tmdRendering_08.coord2s_04[0]);
    GsGetLs(struct258_800c66a8.tmdRendering_08.coord2s_04[0], sp0x38);
    setRotTransMatrix(sp0x38);

    final SVECTOR sp0x58 = new SVECTOR().set(struct258_800c66a8.coord2_34.coord.transfer);
    final DVECTOR sp0x60 = new DVECTOR(); // sxy2
    perspectiveTransform(sp0x58, sp0x60);

    // Player arrow on map
    GPU.queueCommand(25, new GpuCommandQuad()
      .bpp(Bpp.BITS_4)
      .clut(640, 496)
      .vramPos(640, 256)
      .rgb(0x55, 0, 0)
      .pos(sp0x60.getX() - size / 2, sp0x60.getY() - size, size, size)
      .uv((tickCount_800bb0fc.get() & 0x7) * size, v)
    );

    if(struct258_800c66a8.zoomState_1f8 == 4) {
      //LAB_800d44d0
      int sp24 = 0;

      //LAB_800d44d8
      for(int sp20 = 0; sp20 < 49; sp20++) {
        //LAB_800d4518
        if(gameState_800babc8.scriptFlags2_bc.get(_800f5a6c.get(sp20).packedFlag_00.get())) {
          //LAB_800d45cc
          sp24 = sp20;
        }

        //LAB_800d45d8
      }

      //LAB_800d45f0
      if(sp24 != 0) {
        //LAB_800d4608
        // Destination arrow on map
        GPU.queueCommand(25, new GpuCommandQuad()
          .bpp(Bpp.BITS_4)
          .clut(640, 496)
          .vramPos(640, 256)
          .rgb(0, 0, 0x55)
          .pos(_800f5a6c.get(sp24).x_24.get() - 160, _800f5a6c.get(sp24).y_26.get() - 120, size, size)
          .uv((tickCount_800bb0fc.get() & 0x7) * size, v)
        );

        if(!places_800f0234.get(_800f5a6c.get(sp24).placeIndex_28.get()).name_00.isNull()) {
          //LAB_800d4878
          final int x = _800f5a6c.get(sp24).x_24.get();
          final int y = _800f5a6c.get(sp24).y_26.get() - 8;

          final IntRef width = new IntRef();
          final IntRef lines = new IntRef();
          measureText(places_800f0234.get(_800f5a6c.get(sp24).placeIndex_28.get()).name_00.deref(), width, lines);

          final long v0 = _800c86f0.get();
          if(v0 == 0x1L) {
            //LAB_800d49e4
            textZ_800bdf00.set(14);
            textboxes_800be358[7].z_0c = 14;
            textboxes_800be358[7].chars_18 = Math.max(width.get(), 4);
            textboxes_800be358[7].lines_1a = lines.get();
            _800c86f0.setu(0x2L);
            //LAB_800d4974
          } else if(v0 == 0) {
            //LAB_800d4988
            initTextbox(7, 0, x, y, width.get() - 1, lines.get() - 1);
            _800c86f0.setu(0x1L);

            //LAB_800d49e4
            textZ_800bdf00.set(14);
            textboxes_800be358[7].z_0c = 14;
            textboxes_800be358[7].chars_18 = Math.max(width.get(), 4);
            textboxes_800be358[7].lines_1a = lines.get();
            _800c86f0.setu(0x2L);
          } else if(v0 == 0x2L) {
            //LAB_800d4a40
            textboxes_800be358[7].chars_18 = width.get();

            if(width.get() < 4) {
              textboxes_800be358[7].chars_18 = 4;
            }

            //LAB_800d4a6c
            textboxes_800be358[7].lines_1a = lines.get();
            textboxes_800be358[7].width_1c = textboxes_800be358[7].chars_18 * 9 / 2;
            textboxes_800be358[7].height_1e = textboxes_800be358[7].lines_1a * 6;
            textboxes_800be358[7].x_14 = x;
            textboxes_800be358[7].y_16 = y;
          }

          //LAB_800d4aec
          textZ_800bdf00.set(26);
          textboxes_800be358[7].z_0c = 26;

          renderCenteredShadowedText(places_800f0234.get(_800f5a6c.get(sp24).placeIndex_28.get()).name_00.deref(), x, y - lines.get() * 7 + 1, TextColour.WHITE, 0);
        }
      }
    }
  }

  @Method(0x800d4bc8L)
  public static void FUN_800d4bc8(final int a0) {
    final float sp18;
    final float sp14;
    float sp10;

    final WMapStruct19c0 struct = _800c66b0;

    if(a0 == 0) {
      struct.angle_9a = struct.mapRotation_70.y;
      struct.angle_98 = 0;
      sp10 = struct.angle_98 - struct.angle_9a;
      sp14 = struct.angle_98 - (struct.angle_9a - MathHelper.TWO_PI);
    } else {
      //LAB_800d4c80
      struct.angle_98 = struct.angle_9a;
      struct.angle_9a = struct.mapRotation_70.y;

      if(struct.angle_9a < struct.angle_98) {
        sp18 = -MathHelper.TWO_PI;
      } else {
        //LAB_800d4cf8
        sp18 = MathHelper.TWO_PI;
      }

      //LAB_800d4d00
      sp10 = struct.angle_98 - struct.angle_9a;
      sp14 = struct.angle_9a - struct.angle_98 + sp18;
    }

    //LAB_800d4d64
    final VECTOR transfer = struct258_800c66a8.coord2_34.coord.transfer;
    struct.vec_a4.setX((transfer.getX() << 8) / 6);
    struct.vec_a4.setY((transfer.getY() << 8) / 6);
    struct.vec_a4.setZ((transfer.getZ() << 8) / 6);
    struct.vec_b4.setX(0);
    struct.vec_b4.setY(0);
    struct.vec_b4.setZ(0);

    if(Math.abs(sp14) < Math.abs(sp10)) {
      sp10 = sp14;
    }

    //LAB_800d4e88
    struct.angle_9c = sp10 / 6.0f;
    struct._a0 = 0;
  }

  @Method(0x800d4ed8L)
  public static void startMapRotation(final int direction) {
    final float angleDelta = MathHelper.TWO_PI / 8.0f;

    final WMapStruct19c0 struct = _800c66b0;
    struct.mapRotationCounter_7e = 0;
    struct.mapRotationStartAngle_78 = struct.mapRotation_70.y;
    struct.mapRotationEndAngle_7a = struct.mapRotation_70.y + direction * angleDelta;
    float sp10 = -direction * angleDelta;
    final float sp14 = sp10 + MathHelper.TWO_PI;

    if(Math.abs(sp14) < Math.abs(sp10)) {
      sp10 = sp14;
    }

    //LAB_800d4fd0
    struct.mapRotationStep_7c = -sp10 / 6.0f;
  }

  @Method(0x800d5018L)
  public static void FUN_800d5018() {
    final WMapStruct19c0 struct = _800c66b0;
    struct._110 = 0;
    struct._10e = 0;
    struct.rview2_c8.viewpoint_00.set(struct.rview2_00.viewpoint_00);
    struct.rview2_c8.refpoint_0c.set(struct.rview2_00.refpoint_0c);
    struct.rview2_c8.viewpointTwist_18 = struct.rview2_00.viewpointTwist_18;
    struct.rview2_c8.super_1c = struct.rview2_00.super_1c;
    struct.viewpointY_ec = (-100.0f - struct.rview2_c8.viewpoint_00.y) / 16.0f;
    struct.viewpointZ_f0 = (-600.0f - struct.rview2_c8.viewpoint_00.z) / 16.0f;
    struct.refpointY_f8 = ( -90.0f - struct.rview2_c8.refpoint_0c.y) / 16.0f;
    struct.refpointZ_fc = -struct.rview2_c8.refpoint_0c.z / 16.0f;
    struct.angle_10a = struct.mapRotation_70.y;

    final float angle = struct258_800c66a8.rotation_a4.y + MathHelper.PI;
    struct.angle_108 = angle;

    float sp10 = struct.mapRotation_70.y - angle;
    final float sp14 = struct.mapRotation_70.y - (angle - MathHelper.TWO_PI);

    if(Math.abs(sp14) < Math.abs(sp10)) {
      sp10 = sp14;
    }

    //LAB_800d5244
    struct.angle_10c = -sp10 / 16;
  }

  @Method(0x800d5288L)
  public static void FUN_800d5288() {
    final WMapStruct19c0 struct = _800c66b0;
    final int v0 = struct._11a;

    if(v0 == 0) {
      if(struct._154[0].vecLength_04 < 90.0f) {
        struct._11a = 1;
        //LAB_800d52e8
      } else if(struct258_800c66a8._05 == 0 || struct._c5 != 2) {
        //LAB_800d5328
        struct._11a = 3;
      } else {
        return;
      }
    } else if(v0 == 1) {
      //LAB_800d5394
      struct._114 = 0;
      struct._11a = 2;

      //LAB_800d53b4
      struct._114++;

      //LAB_800d5424
      struct.projectionPlaneDistance_118 += Math.max(4, 64 - struct._114 * 2);

      if(struct.projectionPlaneDistance_118 >= 800) {
        struct.projectionPlaneDistance_118 = 800;
        struct._11a = 0;
      }
    } else if(v0 == 2) {
      //LAB_800d53b4
      struct._114++;

      //LAB_800d5424
      struct.projectionPlaneDistance_118 += Math.max(4, 64 - struct._114 * 2);

      if(struct.projectionPlaneDistance_118 >= 800) {
        struct.projectionPlaneDistance_118 = 800;
        struct._11a = 0;
      }
    } else if(v0 == 3) {
      //LAB_800d5494
      if(struct._c4 != 0) {
        struct._11a = 0;
        return;
      }

      //LAB_800d54c8
      struct._114 = 0;
      struct._11a = 4;

      //LAB_800d54e8
      struct._114++;

      //LAB_800d5558
      struct.projectionPlaneDistance_118 -= Math.max(4, 64 - struct._114 * 2);

      if(struct.projectionPlaneDistance_118 <= 600) {
        struct.projectionPlaneDistance_118 = 600;
        struct._11a = 0;
      }
    } else if(v0 == 4) {
      //LAB_800d54e8
      struct._114++;

      //LAB_800d5558
      struct.projectionPlaneDistance_118 -= Math.max(4, 64 - struct._114 * 2);

      if(struct.projectionPlaneDistance_118 <= 600) {
        struct.projectionPlaneDistance_118 = 600;
        struct._11a = 0;
      }
    }

    setProjectionPlaneDistance(struct.projectionPlaneDistance_118);
  }

  @Method(0x800d55fcL)
  public static void FUN_800d55fc() {
    _800c66b0 = null;
  }

  @Method(0x800d562cL)
  public static void FUN_800d562c(final FileData data) {
    final McqHeader mcq = new McqHeader(data);

    //LAB_800d568c
    final RECT sp0x18 = new RECT(
      (short)320,
      (short)0,
      (short)mcq.vramWidth_08,
      (short)mcq.vramHeight_0a
    );

    LoadImage(sp0x18, mcq.imageData);
    mcqHeader_800c6768 = mcq;

    filesLoadedFlags_800c66b8.oru(0x1L);
  }

  @Method(0x800d5768L)
  public static void FUN_800d5768(final Tim tim, final int param) {
    final long ix = imageX_800ef0cc.offset(param * 0x8L).getSigned();
    final long iy = imageY_800ef0ce.offset(param * 0x8L).getSigned();
    final long cx = clutX_800ef0d0.offset(param * 0x8L).getSigned();
    final long cy = clutY_800ef0d2.offset(param * 0x8L).getSigned();
    FUN_800d5c50(tim, ix, iy, cx, cy);
    filesLoadedFlags_800c66b8.oru(0x800L);

    //LAB_800d5848
  }

  @Method(0x800d5858L) //TODO loads general world map stuff (location text, doors, buttons, etc.), several blobs that may be smoke?, tons of terrain and terrain sprites
  public static void timsLoaded(final List<FileData> files, final int param) {
    //LAB_800d5874
    for(final FileData file : files) {
      //LAB_800d5898
      if(file.size() != 0) {
        //LAB_800d58c8
        new Tim(file).uploadToGpu();
      }
    }

    //LAB_800d5938
    filesLoadedFlags_800c66b8.oru(param);

    //LAB_800d5970
  }

  @Method(0x800d5984L)
  public static void loadTmdCallback(final String modelName, final FileData file) {
    final TmdWithId tmd = new TmdWithId(modelName, file);

    struct258_800c66a8.tmdRendering_08 = loadTmd(tmd);
    initTmdTransforms(struct258_800c66a8.tmdRendering_08, null);
    struct258_800c66a8.tmdRendering_08.tmd_14 = tmd;
    setAllCoord2Attribs(struct258_800c66a8.tmdRendering_08, 0);
    filesLoadedFlags_800c66b8.oru(0x4L);
  }

  @Method(0x800d5a30L)
  public static void FUN_800d5a30(final List<FileData> files, final int whichFile) {
    if(files.get(0).size() != 0) {
      struct258_800c66a8._b4[whichFile].extendedTmd_00 = new CContainer("DRGN0/" + (5714 + whichFile), files.get(0));
    }

    //LAB_800d5a48
    for(int i = 2; i < Math.min(16, files.size()); i++) {
      //LAB_800d5a6c
      if(files.get(i).size() != 0) {
        //LAB_800d5a9c
        //LAB_800d5ab8
        struct258_800c66a8._b4[whichFile].tmdAnim_08[i - 2] = new TmdAnimationFile(files.get(i));
      }

      //LAB_800d5b2c
    }

    //LAB_800d5b44
    if(whichFile == 0) {
      //LAB_800d5bb8
      filesLoadedFlags_800c66b8.oru(0x10L);
    } else if(whichFile == 1) {
      //LAB_800d5bd8
      filesLoadedFlags_800c66b8.oru(0x40L);
      //LAB_800d5b98
    } else if(whichFile == 2) {
      //LAB_800d5bf8
      filesLoadedFlags_800c66b8.oru(0x100L);
    } else if(whichFile == 3) {
      //LAB_800d5c18
      filesLoadedFlags_800c66b8.oru(0x400L);
    }

    //LAB_800d5c38
    //LAB_800d5c40
  }

  @Method(0x800d5c50L)
  public static void FUN_800d5c50(final Tim tim, final long imageX, final long imageY, final long clutX, final long clutY) {
    final RECT imageRect = tim.getImageRect();
    final RECT rect = new RECT((short)imageX, (short)imageY, imageRect.w.get(), imageRect.h.get());
    LoadImage(rect, tim.getImageData());

    if((tim.getFlags() & 0x8) != 0 && (short)clutX != -1) {
      final RECT clutRect = tim.getClutRect();
      rect.set((short)clutX, (short)clutY, clutRect.w.get(), clutRect.h.get());
      LoadImage(rect, tim.getClutData());
    }

    //LAB_800d5d84
  }

  @Method(0x800d5e70L)
  public static TextureAnimation20 prepareAnimationStruct(final RECT size, final int a1, final int a2, final int a3) {
    final int imageSize = size.w.get() / (2 - a1) * size.h.get();

    final TextureAnimation20 anim = new TextureAnimation20();
    anim.x_00 = size.x.get();
    anim.y_02 = size.y.get();
    anim.w_04 = size.w.get() / (4 - a1 * 2);
    anim.h_06 = size.h.get();
    anim.imageData_08 = new FileData(new byte[imageSize]);
    anim.imageData_0c = new FileData(new byte[imageSize]);
    anim._10 = a2;
    anim._14 = a1;
    anim._18 = (short)a3;
    anim._1a = (short)(a2 / 2 * 2);
    anim._1c = anim._1a;
    return anim;
  }

  @Method(0x800d6080L)
  public static void animateTextures(final TextureAnimation20 anim) {
    if(anim._18 == 0) {
      return;
    }

    //LAB_800d60b0
    anim._1c++;

    if(anim._1c < anim._1a) {
      return;
    }

    final RECT src0 = new RECT();
    final RECT src1 = new RECT();
    final RECT dest0 = new RECT();
    final RECT dest1 = new RECT();

    //LAB_800d60f8
    anim._1c = (short)0;

    if((anim._10 & 0x1) == 0) {
      anim._18 %= anim.w_04;

      if(anim._18 > 0) {
        src0.set(
          (short)(anim.x_00 + anim.w_04 - anim._18),
          (short)anim.y_02,
          anim._18,
          (short)anim.h_06
        );

        src1.set(
          (short)anim.x_00,
          (short)anim.y_02,
          (short)(anim.w_04 - anim._18),
          (short)anim.h_06
        );

        dest0.set(
          (short)anim.x_00,
          (short)anim.y_02,
          anim._18,
          (short)anim.h_06
        );

        dest1.set(
          (short)(anim.x_00 + anim._18),
          (short)anim.y_02,
          (short)(anim.w_04 - anim._18),
          (short)anim.h_06
        );
      } else {
        //LAB_800d62e4
        src0.set(
          (short)anim.x_00,
          (short)anim.y_02,
          (short)-anim._18,
          (short)anim.h_06
        );

        src1.set(
          (short)(anim.x_00 - anim._18),
          (short)anim.y_02,
          (short)(anim.w_04 + anim._18),
          (short)anim.h_06
        );

        dest0.set(
          (short)(anim.x_00 + anim.w_04 + anim._18),
          (short)anim.y_02,
          (short)-anim._18,
          (short)anim.h_06
        );

        dest1.set(
          (short)anim.x_00,
          (short)anim.y_02,
          (short)(anim.w_04 + anim._18),
          (short)anim.h_06
        );
      }

      //LAB_800d6460
    } else {
      //LAB_800d6468
      anim._18 %= anim.h_06;

      if(anim._18 > 0) {
        src0.set(
          (short)anim.x_00,
          (short)(anim.y_02 + anim.h_06 - anim._18),
          (short)anim.w_04,
          anim._18
        );

        src1.set(
          (short)anim.x_00,
          (short)anim.y_02,
          (short)anim.w_04,
          (short)(anim.h_06 - anim._18)
        );

        dest0.set(
          (short)anim.x_00,
          (short)anim.y_02,
          (short)anim.w_04,
          anim._18
        );

        dest1.set(
          (short)anim.x_00,
          (short)(anim.y_02 + anim._18),
          (short)anim.w_04,
          (short)(anim.h_06 - anim._18)
        );
      } else {
        //LAB_800d662c
        src0.set(
          (short)anim.x_00,
          (short)anim.y_02,
          (short)anim.w_04,
          (short)-anim._18
        );

        src1.set(
          (short)anim.x_00,
          (short)(anim.y_02 - anim._18),
          (short)anim.w_04,
          (short)(anim.h_06 + anim._18)
        );

        dest0.set(
          (short)anim.x_00,
          (short)(anim.y_02 + anim.h_06 + anim._18),
          (short)anim.w_04,
          (short)-anim._18
        );

        dest1.set(
          (short)anim.x_00,
          (short)anim.y_02,
          (short)anim.w_04,
          (short)(anim.h_06 + anim._18)
        );
      }
    }

    //LAB_800d67a8
    StoreImage(src0, anim.imageData_0c);
    StoreImage(src1, anim.imageData_08);
    LoadImage(dest0, anim.imageData_0c);
    LoadImage(dest1, anim.imageData_08);

    //LAB_800d6804
  }

  @Method(0x800d6880L)
  public static void loadWmapTextures() {
    filesLoadedFlags_800c66b8.and(0xffff_efffL);
    loadDrgnDir(0, 5695, files -> timsLoaded(files, 0x1_1000));
    struct258_800c66a8.colour_20 = 0;
  }

  /** Path, continent name, zoom level indicator */
  @Method(0x800d6900L)
  public static void renderMapOverlay() {
    if((filesLoadedFlags_800c66b8.get() & 0x1000L) == 0) {
      return;
    }

    //LAB_800d692c
    if(struct258_800c66a8._250 == 2) {
      return;
    }

    //LAB_800d6950
    // Continent name
    GPU.queueCommand(13, new GpuCommandQuad()
      .bpp(Bpp.BITS_4)
      .monochrome(struct258_800c66a8.colour_20)
      .clut(640, 497)
      .vramPos(640, 256)
      .pos(-144, -104, 128, 24)
      .uv(128, mapState_800c6798.continentIndex_00 * 24)
    );

    struct258_800c66a8.colour_20 += 0x10;

    if(struct258_800c66a8.colour_20 > 0x80) {
      struct258_800c66a8.colour_20 = 0x80;
    }

    //LAB_800d6b5c
    renderPath();

    if(mapState_800c6798.continentIndex_00 == 7) {
      return;
    }

    //LAB_800d6b80
    if(mapState_800c6798._d8 != 0) {
      return;
    }

    // Render map zoom level pyramid thing

    //LAB_800d6b9c
    final int sp18 = switch(struct258_800c66a8.zoomState_1f8) {
      case 0 -> 2;
      case 1, 2, 3, 6 -> 3;
      case 4, 5 -> 4;
      default -> 0;
    };

    //LAB_800d6c10
    //LAB_800d6c14
    for(int i = 0; i < 7; i++) {
      //LAB_800d6c30
      //LAB_800d6d14
      final GpuCommandQuad cmd = new GpuCommandQuad()
        .bpp(Bpp.BITS_4)
        .clut(640, i < 5 ? 502 : 503)
        .vramPos(640, 256);

      //LAB_800d6d44
      //LAB_800d6d84
      //LAB_800d6da8
      if(i < 2 || i >= 5) {
        //LAB_800d6f34
        cmd.monochrome(0x80);
      } else if(i == sp18) {
        cmd.monochrome(0xff);
      } else {
        //LAB_800d6ec0
        cmd.monochrome(0x40);
      }

      //LAB_800d6f2c
      //LAB_800d6fa0
      cmd
        .pos((int)_800ef104.offset(i * 0x6L).get() + 88, (int)_800ef104.offset(i * 0x6L + 0x1L).get() - 96, (int)_800ef104.offset(i * 0x6L + 0x4L).get(), (int)_800ef104.offset(i * 0x6L + 0x5L).get())
        .uv((int)_800ef104.offset(i * 0x6L + 0x2L).get(), (int)_800ef104.offset(i * 0x6L + 0x3L).get());

      GPU.queueCommand(20, cmd);
    }

    //LAB_800d71f4
  }

  /** The "press square to enter Queen Fury" overlay (square button and door icons), also renders something else if a0 == 0 but I'm not sure what it is */
  @Method(0x800d7208L)
  public static void renderQueenFuryUi(final int a0) {
    final int sp14 = (int)_800ef168.offset(tickCount_800bb0fc.get() / 2 % 7).get() * 16;

    // Square button
    GPU.queueCommand(13, new GpuCommandPoly(4)
      .bpp(Bpp.BITS_4)
      .clut(640, 508)
      .vramPos(640, 256)
      .monochrome(0x80)
      .pos(0,  86,  88)
      .pos(1, 102,  88)
      .pos(2,  86, 104)
      .pos(3, 102, 104)
      .uv(0, 64 + sp14, 168)
      .uv(1, 80 + sp14, 168)
      .uv(2, 64 + sp14, 184)
      .uv(3, 80 + sp14, 184)
    );

    if(a0 == 0) {
      final int sp18 = (int)_800ef154.offset(tickCount_800bb0fc.get() / 2 % 5).get();
      final int u = (int)_800ef130.offset(sp18 * 0x4L).offset(0x0L).get();
      final int v = (int)_800ef130.offset(sp18 * 0x4L).offset(0x1L).get();
      final int w = (int)_800ef130.offset(sp18 * 0x4L).offset(0x2L).get();
      final int h = (int)_800ef130.offset(sp18 * 0x4L).offset(0x3L).get();

      GPU.queueCommand(13, new GpuCommandPoly(4)
        .bpp(Bpp.BITS_4)
        .clut(640, 506)
        .vramPos(640, 256)
        .monochrome(0x80)
        .pos(0, 106, 80)
        .pos(1, 106 + w, 80)
        .pos(2, 106, 80 + h)
        .pos(3, 106 + w, 80 + h)
        .uv(0, u, v)
        .uv(1, u + w, v)
        .uv(2, u, v + h)
        .uv(3, u + w, v + h)
      );
    } else {
      //LAB_800d7734
      final int sp18 = (int)_800ef158.offset(tickCount_800bb0fc.get() / 3 % 15).get();
      final int u = (int)_800ef140.offset(sp18 * 0x4L).offset(0x0L).get();
      final int v = (int)_800ef140.offset(sp18 * 0x4L).offset(0x1L).get();
      final int w = (int)_800ef140.offset(sp18 * 0x4L).offset(0x2L).get();
      final int h = (int)_800ef140.offset(sp18 * 0x4L).offset(0x3L).get();

      // Door
      GPU.queueCommand(13, new GpuCommandPoly(4)
        .bpp(Bpp.BITS_4)
        .clut(640, 507)
        .vramPos(640, 256)
        .monochrome(0x80)
        .pos(0, 106, 80)
        .pos(1, 106 + w, 80)
        .pos(2, 106, 80 + h)
        .pos(3, 106 + w, 80 + h)
        .uv(0, u, v)
        .uv(1, u + w, v)
        .uv(2, u, v + h)
        .uv(3, u + w, v + h)
      );
    }

    //LAB_800d7a18
  }

  @Method(0x800d7a34L)
  public static void renderPath() {
    int sx = 0;
    int sy = 0;

    if(worldMapState_800c6698 < 4 || playerState_800c669c < 4) {
      return;
    }

    //LAB_800d7a80
    final int zoomState = struct258_800c66a8.zoomState_1f8;
    if(zoomState == 2 || zoomState == 3 || zoomState == 4 || zoomState == 5) {
      //LAB_800d7af8
      return;
    }

    //LAB_800d7b00
    final long sp94;
    if(zoomState == 1 || zoomState == 6) {
      //LAB_800d7b64
      sp94 = 0xcL;
    } else if(zoomState == 0) {
      //LAB_800d7b58
      sp94 = 0;
      //LAB_800d7b38
    } else if(zoomState == 4) {
      //LAB_800d7b74
      sp94 = 0x18L;
    } else {
      sp94 = 0; //TODO this was uninitialized in the code
    }

    final MATRIX sp0x18 = new MATRIX();
    final Vector3f sp0x40 = new Vector3f();

    //LAB_800d7b84
    final int sp98 = tickCount_800bb0fc.get() / 5 % 3;

    final int u = (int)_800ef170.offset(sp94).offset(sp98 * 0x4L).offset(0x0L).get();
    final int v = (int)_800ef170.offset(sp94).offset(sp98 * 0x4L).offset(0x1L).get();
    final int uw = (int)_800ef170.offset(sp94).offset(sp98 * 0x4L).offset(0x2L).get();
    final int vw = (int)_800ef170.offset(sp94).offset(sp98 * 0x4L).offset(0x3L).get();

    final int x = struct258_800c66a8.coord2_34.coord.transfer.getX();
    final int y = struct258_800c66a8.coord2_34.coord.transfer.getY();
    final int z = struct258_800c66a8.coord2_34.coord.transfer.getZ();

    rotateCoord2(struct258_800c66a8.tmdRendering_08.rotations_08[0], struct258_800c66a8.tmdRendering_08.coord2s_04[0]);
    GsGetLs(struct258_800c66a8.tmdRendering_08.coord2s_04[0], sp0x18);
    setRotTransMatrix(sp0x18);

    //LAB_800d7d6c
    for(int i = 0; i < mapState_800c6798.locationCount_08; i++) {
      //LAB_800d7d90
      if(FUN_800eb09c(i, 1, sp0x40) == 0) {
        //LAB_800d7db4
        if(mapState_800c6798.continentIndex_00 != 7 || i == 31 || i == 78) {
          //LAB_800d7df0
          GTE.perspectiveTransform(sp0x40);

          sx = GTE.getScreenX(2);
          sy = GTE.getScreenY(2);
          final int sz = GTE.getScreenZ(3) >> 2;

          if(sz >= 4 && sz < orderingTableSize_1f8003c8.get()) {
            final GpuCommandPoly cmd = new GpuCommandPoly(4)
              .bpp(Bpp.BITS_4)
              .translucent(Translucency.B_PLUS_F)
              .clut(640, 496)
              .vramPos(640, 256);

            if(struct258_800c66a8.zoomState_1f8 == 0) {
              final float dx = x - sp0x40.x;
              final float dy = y - sp0x40.y;
              final float dz = z - sp0x40.z;
              final float sp90 = Math.max(0, 0x200 - Math.sqrt(dx * dx + dy * dy + dz * dz)) / 2;
              cmd.rgb((int)(sp90 * 31 / 256), (int)(sp90 * 63 / 256), 0);
            } else {
              //LAB_800d8048
              cmd.rgb(31, 63, 0);
            }

            //LAB_800d806c
            cmd
              .uv(0, u, v)
              .uv(1, u + uw, v)
              .uv(2, u, v + vw)
              .uv(3, u + uw, v + vw)
              .pos(0, sx - ((int)_800ef170.offset(sp98 * 4 + sp94 + 2).get() >>> 2), sy - ((int)_800ef170.offset(sp98 * 4 + sp94 + 3).get() >>> 2))
              .pos(1, sx - ((int)_800ef170.offset(sp98 * 4 + sp94 + 2).get() >>> 2) + ((int)_800ef170.offset(sp98 * 4 + sp94 + 2).get() >>> 1), sy - ((int)_800ef170.offset(sp98 * 4 + sp94 + 3).get() >>> 2))
              .pos(2, sx - ((int)_800ef170.offset(sp98 * 4 + sp94 + 2).get() >>> 2), sy - ((int)_800ef170.offset(sp98 * 4 + sp94 + 3).get() >>> 2) + ((int)_800ef170.offset(sp98 * 4 + sp94 + 2).get() >>> 1))
              .pos(3, sx - ((int)_800ef170.offset(sp98 * 4 + sp94 + 2).get() >>> 2) + ((int)_800ef170.offset(sp98 * 4 + sp94 + 2).get() >>> 1), sy - ((int)_800ef170.offset(sp98 * 4 + sp94 + 3).get() >>> 2) + ((int)_800ef170.offset(sp98 * 4 + sp94 + 2).get() >>> 1));

            GPU.queueCommand(10 + sz, cmd);
          }

          //LAB_800d84b0
        }
      }

      //LAB_800d84c0
    }

    final boolean[] sp0xb0 = new boolean[0xff];

    //LAB_800d852c
    //LAB_800d8540
    for(int i = 0; i < mapState_800c6798.locationCount_08; i++) {
      //LAB_800d8564
      if(FUN_800eb09c(i, 0, null) == 0) {
        //LAB_800d8584
        if(mapState_800c6798.continentIndex_00 != 7 || i == 31 || i == 78) {
          //LAB_800d85c0
          final int sp88 = areaData_800f2248.get(locations_800f0e34.get(i).areaIndex_00.get())._00.get();
          final int sp80 = Math.abs(sp88) - 1;

          if(!sp0xb0[sp80]) {
            //LAB_800d863c
            sp0xb0[sp80] = true;
            final int pathPointCount = _800f5810.get(sp80).get() - 1;

            final UnboundedArrayRef<VECTOR> pathPoints = pathDotPosPtrArr_800f591c.get(sp80).deref();
            final int pathPointIndexBase = sp88 >= 0 ? 0 : pathPointCount - 1;

            //LAB_800d86d0
            //LAB_800d86d4
            for(int pathPointIndex = 0; pathPointIndex < pathPointCount; pathPointIndex++) {
              //LAB_800d86f4
              final VECTOR sp0x70;
              if(sp88 > 0) {
                sp0x70 = pathPoints.get(pathPointIndexBase + pathPointIndex);
              } else {
                //LAB_800d8784
                sp0x70 = pathPoints.get(pathPointIndexBase - pathPointIndex);
              }

              //LAB_800d87fc
              GTE.perspectiveTransform(sp0x70);

              final int sx2 = GTE.getScreenX(2);
              final int sy2 = GTE.getScreenY(2);
              final int screenZ = GTE.getScreenZ(3) >> 2;

              if(screenZ >= 4 && screenZ < orderingTableSize_1f8003c8.get()) {
                final GpuCommandPoly cmd = new GpuCommandPoly(4)
                  .bpp(Bpp.BITS_4)
                  .translucent(Translucency.B_PLUS_F)
                  .clut(640, 496)
                  .vramPos(640, 256);

                if(zoomState == 0) {
                  final int dx = x - sp0x70.getX();
                  final int dy = y - sp0x70.getY();
                  final int dz = z - sp0x70.getZ();
                  final int sp90 = Math.max(0, 0x200 - SquareRoot0(dx * dx + dy * dy + dz * dz)) / 2;

                  cmd
                    .rgb(sp90 * 47 / 256, sp90 * 39 / 256, 0)
                    .pos(0, sx - 2, sy - 2)
                    .pos(1, sx + 2, sy - 2)
                    .pos(2, sx - 2, sy + 2)
                    .pos(3, sx + 2, sy + 2)
                    .uv(0, 48, 0)
                    .uv(1, 63, 0)
                    .uv(2, 48, 15)
                    .uv(3, 63, 15);
                } else {
                  //LAB_800d8b40
                  cmd
                    .rgb(0x2f, 0x27, 0)
                    .pos(0, sx - 1, sy - 1)
                    .pos(1, sx + 2, sy - 2)
                    .pos(2, sx - 1, sy + 2)
                    .pos(3, sx + 2, sy + 2)
                    .uv(0, 16, 24)
                    .uv(1, 23, 24)
                    .uv(2, 16, 31)
                    .uv(3, 23, 31);
                }

                //LAB_800d8c64
                sx = sx2;
                sy = sy2;

                GPU.queueCommand(10 + screenZ, cmd);
              }

              //LAB_800d8cb8
            }
          }
        }
      }

      //LAB_800d8ce0
    }

    //LAB_800d8cf8
    //LAB_800d8d04
  }

  @Method(0x800d8d18L)
  public static void FUN_800d8d18() {
    loadMapModelAndTexture(mapState_800c6798.continentIndex_00);

    struct258_800c66a8.zoomState_1f8 = 0;
    struct258_800c66a8._220 = 0;

    final COLOUR sp0x48 = new COLOUR();

    struct258_800c66a8._1fc = FUN_800cd3c8(
      0x80,
      sp0x48,
      sp0x48,
      sp0x48,
      sp0x48,
      _800c8778,
      _800c877c,
      4,
      4,
      2,
      true,
      Translucency.B_PLUS_F,
      13
    );
  }

  @Method(0x800d8e4cL)
  public static void loadMapModelAndTexture(final int index) {
    filesLoadedFlags_800c66b8.and(0xffff_fffdL);
    loadDrgnDir(0, 5697 + index, files -> timsLoaded(files, 0x2));
    loadDrgnFile(0, 5705 + index, files -> loadTmdCallback("DRGN0/" + (5705 + index), files));
  }

  @Method(0x800d8efcL)
  public static void initMapAnimation() {
    final RECT size = new RECT((short)448, (short)0, (short)64, (short)64);
    struct258_800c66a8.textureAnimation_1c = prepareAnimationStruct(size, 0, 3, 1);
    struct258_800c66a8._28 = 0;

    if(mapState_800c6798.continentIndex_00 == 2) { // Tiberoa
      //LAB_800d8f94
      for(int i = 0; i < struct258_800c66a8.tmdRendering_08.count_0c; i++) {
        //LAB_800d8fc4
        struct258_800c66a8.tmdRendering_08._10[i] = rand() % 4095;
      }
    }

    //LAB_800d9030
  }

  @Method(0x800d9044L)
  public static void renderWorldMap() {
    final MATRIX sp0x10 = new MATRIX();
    final MATRIX sp0x30 = new MATRIX();

    renderAndHandleWorldMap();
    FUN_800da248();

    if(struct258_800c66a8._220 >= 2 && struct258_800c66a8._220 < 8) {
      return;
    }

    //LAB_800d90a8
    if(struct258_800c66a8.zoomState_1f8 == 4) {
      return;
    }

    //LAB_800d90cc
    //LAB_800d9150
    for(int i = 0; i < struct258_800c66a8.tmdRendering_08.count_0c; i++) {
      final ModelPart10 dobj2 = struct258_800c66a8.tmdRendering_08.dobj2s_00[i];
      final GsCOORDINATE2 coord2 = struct258_800c66a8.tmdRendering_08.coord2s_04[i];
      final Vector3f rotation = struct258_800c66a8.tmdRendering_08.rotations_08[i];

      //LAB_800d9180
      if(mapState_800c6798.continentIndex_00 != 7) {
        //LAB_800d91cc
        if(_800ef194.offset(mapState_800c6798.continentIndex_00).get() == i || _800ef19c.offset(mapState_800c6798.continentIndex_00).get() == i) {
          zOffset_1f8003e8.set(500);
        } else {
          //LAB_800d9204
          zOffset_1f8003e8.set(100);
        }
      }

      //LAB_800d9210
      rotateCoord2(rotation, coord2);

      if(mapState_800c6798.continentIndex_00 == 2) { // Tiberoa
        //LAB_800d9264
        if(i >= 2 && i < 9 || i >= 15 && i < 17) {
          //LAB_800d9294
          final int sin = rsin(struct258_800c66a8.tmdRendering_08._10[i]) * 0x20 / 0x1000;
          if((i & 0x1) != 0) {
            coord2.coord.transfer.setY(sin);
          } else {
            //LAB_800d92d8
            coord2.coord.transfer.setY(-sin);
          }

          //LAB_800d9304
          struct258_800c66a8.tmdRendering_08._10[i] += 8;
        }
      }

      //LAB_800d9320
      GsGetLws(dobj2.coord2_04, sp0x10, sp0x30);
      GsSetLightMatrix(sp0x10);
      setRotTransMatrix(sp0x30);

      if(mapState_800c6798.continentIndex_00 < 9 && i == 0) {
        tempZ_800c66d8.set(orderingTableSize_1f8003c8.get() - 3);
        renderSpecialDobj2(dobj2);
        tempZ_800c66d8.set(0);
        //LAB_800d93c0
      } else {
        //LAB_800d93b4
        //LAB_800d93c8
        renderDobj2(dobj2);
      }

      //LAB_800d93d4
    }

    //LAB_800d942c
    if(mapState_800c6798.continentIndex_00 < 9) {
      animateTextures(struct258_800c66a8.textureAnimation_1c);
    }

    //LAB_800d945c
    struct258_800c66a8._28++;

    if(struct258_800c66a8._28 >= 14) {
      struct258_800c66a8._28 = 0;
    }

    //LAB_800d94b8
  }

  @Method(0x800d94ccL)
  public static void renderAndHandleWorldMap() {
    if((filesLoadedFlags_800c66b8.get() & 0x1L) == 0) {
      return;
    }

    //LAB_800d94f8
    if(struct258_800c66a8.zoomState_1f8 == 0) {
      return;
    }

    //LAB_800d951c
    if(struct258_800c66a8._250 != 0) {
      return;
    }

    //LAB_800d9540
    if(mapState_800c6798.continentIndex_00 == 7) {
      return;
    }

    //LAB_800d955c
    switch(struct258_800c66a8.zoomState_1f8) {
      case 1, 6:
        if(Input.pressedThisFrame(InputAction.BUTTON_SHOULDER_RIGHT_2)) { // Zoom out
          playSound(0, 4, 0, 0, (short)0, (short)0);

          struct258_800c66a8.svec_1e8.set(_800c66b0.coord2_20.coord.transfer);

          FUN_800d9d24(1);

          struct258_800c66a8.zoomState_1f8 = 2;
          _800ef1a4.setu(0);
        }

        //LAB_800d9674
        //LAB_800d9cc4
        break;

      case 2:
        _800ef1a4.addu(0x10L);

        if(_800ef1a4.getSigned() > 0x80L) {
          _800ef1a4.setu(0x80L);
        }

        //LAB_800d96b8
        FUN_800d9eb0();

        struct258_800c66a8._1f9++;

        if(struct258_800c66a8._1f9 >= 6) {
          _800c66b0.coord2_20.coord.transfer.set(mapPositions_800ef1a8.get(mapState_800c6798.continentIndex_00));
          struct258_800c66a8.zoomState_1f8 = 3;

          //LAB_800d97bc
          for(int i = 0; i < 7; i++) {
            //LAB_800d97d8
            FUN_8002a3ec(i, 0);
          }
        }

        //LAB_800d9808
        break;

      case 3:
        struct258_800c66a8.zoomState_1f8 = 4;

      case 4:
        if(Input.pressedThisFrame(InputAction.BUTTON_SHOULDER_RIGHT_2)) { // Can't zoom out more
          playSound(0, 40, 0, 0, (short)0, (short)0);
        }

        //LAB_800d9858
        //LAB_800d985c
        for(int i = 0; i < 6; i++) {
          //LAB_800d9878
          FUN_8002a3ec(i, 0);
        }

        //LAB_800d98a8
        if(Input.pressedThisFrame(InputAction.BUTTON_SHOULDER_LEFT_2)) { // Zoom in
          playSound(0, 4, 0, 0, (short)0, (short)0);
          FUN_800d9d24(-1);

          struct258_800c66a8.zoomState_1f8 = 5;

          //LAB_800d9900
          for(int i = 0; i < 3; i++) {
            //LAB_800d991c
            //LAB_800d996c
            //LAB_800d99c4
            //LAB_800d9a1c
            _800c66b0.lights_11c[i].r_0c = _800c66b0.colour_8c[i].r.get() / 4.0f / 0x100;
            _800c66b0.lights_11c[i].g_0d = _800c66b0.colour_8c[i].g.get() / 4.0f / 0x100;
            _800c66b0.lights_11c[i].b_0e = _800c66b0.colour_8c[i].b.get() / 4.0f / 0x100;

            GsSetFlatLight(i, _800c66b0.lights_11c[i]);
          }

          //LAB_800d9a70
          if(Input.getButtonState(InputAction.BUTTON_CENTER_2)) {
            //LAB_800d9a8c
            for(int i = 0; i < 8; i++) {
              //LAB_800d9aa8
              _800c86d4[i] = 0;
            }
          }
        }

        //LAB_800d9adc
        break;

      case 5:
        _800ef1a4.subu(0x10L);

        if(_800ef1a4.getSigned() < 0) {
          _800ef1a4.setu(0);
        }

        //LAB_800d9b18
        FUN_800d9eb0();

        struct258_800c66a8._1f9++;

        if(struct258_800c66a8._1f9 >= 6) {
          _800c66b0.coord2_20.coord.transfer.set(struct258_800c66a8.svec_1e8);
          struct258_800c66a8.zoomState_1f8 = 6;
        }

        //LAB_800d9be8
        break;
    }

    //LAB_800d9ccc
    renderMcq(mcqHeader_800c6768, 320, 0, -160, -120, 30, (int)_800ef1a4.get() & 0xff);

    //LAB_800d9d10
  }

  /**
   * @param zoomDirection -1 or +1
   */
  @Method(0x800d9d24L)
  public static void FUN_800d9d24(final int zoomDirection) {
    final VECTOR vec = mapPositions_800ef1a8.get(mapState_800c6798.continentIndex_00);
    final WMapStruct258 wmap = struct258_800c66a8;
    wmap.svec_1f0.setX((short)((vec.getX() - wmap.svec_1e8.getX()) * zoomDirection / 6));
    wmap.svec_1f0.setY((short)((vec.getY() - wmap.svec_1e8.getY()) * zoomDirection / 6));
    wmap.svec_1f0.setZ((short)((vec.getZ() - wmap.svec_1e8.getZ()) * zoomDirection / 6));
    wmap._1f9 = 0;
  }

  @Method(0x800d9eb0L)
  public static void FUN_800d9eb0() {
    _800c66b0.coord2_20.coord.transfer.add(struct258_800c66a8.svec_1f0);
  }

  /**
   * Handles Coolon fast travel, Queen Fury overlay, probably other things
   */
  @Method(0x800da248L)
  public static void FUN_800da248() {
    if(mapState_800c6798._fc == 1) {
      return;
    }

    final WMapStruct258 struct258 = struct258_800c66a8;

    //LAB_800da270
    if(struct258._05 != 0) {
      return;
    }

    //LAB_800da294
    if(_800c66b0._110 != 0) {
      return;
    }

    //LAB_800da2b8
    if(struct258.zoomState_1f8 != 0) {
      return;
    }

    //LAB_800da2dc
    if(_800c66b0._c5 != 0) {
      return;
    }

    //LAB_800da300
    if(_800c66b0._c4 != 0) {
      return;
    }

    //LAB_800da324
    if((filesLoadedFlags_800c66b8.get() & 0x1L) == 0) {
      return;
    }

    //LAB_800da344
    if(_800c6690.get() != 0) {
      return;
    }

    //LAB_800da360
    if(struct258.modelIndex_1e4 == 1) {
      if(gameState_800babc8.scriptFlags2_bc.get(0x97) && mapState_800c6798._d8 == 0) {
        renderQueenFuryUi(1);
      }

      //LAB_800da418
      return;
    }

    //LAB_800da420
    if(struct258._250 == 1) {
      return;
    }

    //LAB_800da468
    if(!gameState_800babc8.scriptFlags2_bc.get(0x15a)) {
      return;
    }

    //LAB_800da4ec
    renderQueenFuryUi(0);

    if(Input.pressedThisFrame(InputAction.BUTTON_WEST)) { // Square
      struct258._250 = 2;
    }

    //LAB_800da520
    if(struct258._250 != 2) {
      return;
    }

    //LAB_800da544
    switch(struct258._220 + 1) {
      case 1:
        playSound(0, 4, 0, 0, (short)0, (short)0);

        struct258.svec_200.set(_800c66b0.coord2_20.coord.transfer);

        struct258.svec_208.setX((short)struct258.vec_94.x);
        struct258.svec_208.setY((short)struct258.vec_94.y);
        struct258.svec_208.setZ((short)struct258.vec_94.z);

        struct258.angle_21c = struct258.rotation_a4.y;
        struct258.angle_21e = _800c66b0.mapRotation_70.y;
        struct258._223 = 0;
        struct258._220 = 1;
        struct258.models_0c[2].coord2_14.transforms.rotate.set(0.0f, struct258.rotation_a4.y, 0.0f);
        struct258.models_0c[2].coord2_14.transforms.scale.x = 0.25f;
        struct258.coord2_34.coord.transfer.setX((int)struct258.vec_94.x);
        struct258.coord2_34.coord.transfer.setY((int)struct258.vec_94.y);
        struct258.coord2_34.coord.transfer.setZ((int)struct258.vec_94.z);
        struct258.models_0c[2].coord2_14.coord.transfer.set(struct258.coord2_34.coord.transfer);

        //LAB_800da8a0
        for(int i = 0; i < 8; i++) {
          //LAB_800da8bc
          FUN_8002a3ec(i, 0);
        }

        //LAB_800da8ec
        //LAB_800da8f0
        for(int i = 0; i < 8; i++) {
          //LAB_800da90c
          _800c86d4[i] = 0;
        }

        //LAB_800da940
        if((tickCount_800bb0fc.get() & 0x3L) == 0) {
          playSound(12, 1, 0, 0, (short)0, (short)0);
        }

        //LAB_800da978
        break;

      case 2:
        renderWinglyTeleportScreenEffect();

        struct258.models_0c[2].coord2_14.transforms.scale.y += 0.015625f; // 1/64

        if(struct258.models_0c[2].coord2_14.transforms.scale.x > 0.375f) { // 24/64
          struct258.models_0c[2].coord2_14.transforms.scale.x = 0.375f;
        }

        //LAB_800da9fc
        struct258.models_0c[2].coord2_14.transforms.scale.set(struct258.models_0c[2].coord2_14.transforms.scale.x);
        struct258.vec_94.y -= 96.0f;

        _800c66b0.coord2_20.coord.transfer.y.sub(0x60);

        if(_800c66b0.coord2_20.coord.transfer.getY() < -1500) {
          _800c66b0.coord2_20.coord.transfer.setY(-1500);
        }

        //LAB_800daab8
        if(struct258.vec_94.y < -2500.0f) {
          struct258.vec_94.y = -2500.0f;
        }

        //LAB_800daaf0
        if(struct258.vec_94.y <= -2500.0f) {
          if(_800c66b0.coord2_20.coord.transfer.getY() <= -1500) {
            struct258._220 = 2;
          }
        }

        //LAB_800dab44
        _800ef1a4.addu(0x1L);

        if(_800ef1a4.getSigned() > 0x20L) {
          _800ef1a4.setu(0x20L);
        }

        //LAB_800dab80
        break;

      case 3:
        struct258.models_0c[2].coord2_14.transforms.scale.zero();
        struct258.models_0c[2].coord2_14.transforms.rotate.set(MathHelper.TWO_PI / 4.0f, MathHelper.TWO_PI / 2.0f, 0.0f);

        _800c66b0.mapRotation_70.y = 0.0f;
        _800c66b0.coord2_20.coord.transfer.set(720, -1500, 628);
        _800c66b0._11a = 3;

        //LAB_800dac80
        boolean sp24 = false;
        for(int i = 0; i < 9; i++) {
          //LAB_800dac9c
          if(locations_800f0e34.get(coolonWarpDest_800ef228.get(i)._10.get()).continentNumber_0e.get() == mapState_800c6798.continentIndex_00 + 1) {
            struct258.coolonWarpIndex_221 = i;
            sp24 = true;
            break;
          }

          //LAB_800dad14
        }

        //LAB_800dad2c
        if(!sp24) {
          struct258.coolonWarpIndex_221 = 8;
        }

        //LAB_800dad4c
        if(mapState_800c6798.continentIndex_00 == 4) { // Mille Seseau
          if(struct258.vec_94.z < -400.0f) {
            struct258.coolonWarpIndex_221 = 5;
          } else {
            //LAB_800dad9c
            struct258.coolonWarpIndex_221 = 6;
          }
        }

        //LAB_800dadac
        struct258.coolonWarpIndex_222 = coolonWarpDest_800ef228.get(struct258.coolonWarpIndex_221)._14.get();
        struct258._220 = 3;
        coolonWarpDest_800ef228.get(struct258.coolonWarpIndex_221).vec_00.get(struct258.vec_94);
        break;

      case 4:
        if(Input.getButtonState(InputAction.BUTTON_EAST) || Input.getButtonState(InputAction.BUTTON_WEST)) {
          playSound(0, 3, 0, 0, (short)0, (short)0);

          //LAB_800daef8
          for(int i = 0; i < 8; i++) {
            //LAB_800daf14
            FUN_8002a3ec(i, 0);
          }

          //LAB_800daf44
          if(struct258._254 != 0) {
            mapState_800c6798.submapCut_c8 = locations_800f0e34.get(mapState_800c6798.locationIndex_10).submapCut_08.get();
            mapState_800c6798.submapScene_ca = locations_800f0e34.get(mapState_800c6798.locationIndex_10).submapScene_0a.get();
            submapCut_80052c30.set(mapState_800c6798.submapCut_c8);
            submapScene_80052c34.set(mapState_800c6798.submapScene_ca);

            FUN_800e3fac(1);
          } else {
            //LAB_800daff4
            struct258._220 = 10;
          }

          //LAB_800db004
          break;
        }

        //LAB_800db00c
        if(Input.pressedThisFrame(InputAction.BUTTON_SOUTH)) {
          playSound(0, 2, 0, 0, (short)0, (short)0);
          initTextbox(6, 1, 240, 64, 9, 4);
          struct258._220 = 4;
        }

        //LAB_800db07c
        struct258.models_0c[2].coord2_14.transforms.scale.x += 0.125f;

        if(struct258.models_0c[2].coord2_14.transforms.scale.x > 0.5f) {
          struct258.models_0c[2].coord2_14.transforms.scale.x = 0.5f;
        }

        //LAB_800db0f0
        struct258.models_0c[2].coord2_14.transforms.scale.set(struct258.models_0c[2].coord2_14.transforms.scale.x);

        renderCoolonMap(true, 0x1L);
        break;

      case 5:
        struct258.models_0c[2].coord2_14.transforms.scale.set(0.5f, 0.5f, 0.5f);

        if(FUN_8002a488(6) != 0) {
          struct258._220 = 5;
          struct258._223 = 0;
          struct258._218 = 0;
        }

        //LAB_800db1d8
        renderCoolonMap(false, 0);
        break;

      case 6:
        textboxes_800be358[6].z_0c = 18;

        renderCenteredShadowedText(Move_800f00e8, 240, 41, TextColour.WHITE, 0);
        renderCenteredShadowedText(No_800effa4, 240, 57, TextColour.WHITE, 0);
        renderCenteredShadowedText(Yes_800effb0, 240, 73, TextColour.WHITE, 0);
        renderCoolonMap(false, 0);

        if(Input.pressedThisFrame(InputAction.BUTTON_EAST)) {
          playSound(0, 3, 0, 0, (short)0, (short)0);
          FUN_8002a3ec(6, 1);
          struct258._220 = 3;
        }

        //LAB_800db39c
        if(Input.pressedThisFrame(InputAction.DPAD_UP) || Input.pressedThisFrame(InputAction.JOYSTICK_LEFT_BUTTON_UP) ||
          Input.pressedThisFrame(InputAction.DPAD_DOWN) || Input.pressedThisFrame(InputAction.JOYSTICK_LEFT_BUTTON_DOWN)) {
          playSound(0, 1, 0, 0, (short)0, (short)0);
          struct258._223 ^= 1;
        }

        //LAB_800db3f8
        if(Input.pressedThisFrame(InputAction.BUTTON_SOUTH)) {
          if(struct258._223 == 0) {
            playSound(0, 3, 0, 0, (short)0, (short)0);
            FUN_8002a3ec(6, 1);
            struct258._220 = 3;
          } else {
            //LAB_800db474
            playSound(0, 2, 0, 0, (short)0, (short)0);
            FUN_8002a3ec(6, 1);
            struct258._220 = 6;
          }
        }

        //LAB_800db4b4
        struct258._1fc.y_3a = struct258._223 * 0x10;

        FUN_800ce4dc(struct258._1fc);
        break;

      case 7:
        struct258._218++;

        if(struct258._218 > 12) {
          struct258._218 = 12;
          struct258._220 = 7;
        }

        //LAB_800db698
        lerp(struct258.vec_94, coolonWarpDest_800ef228.get(struct258.coolonWarpIndex_221).vec_00.toVec3().div(4096.0f), coolonWarpDest_800ef228.get(struct258.coolonWarpIndex_222).vec_00.toVec3().div(4096.0f), 12.0f / struct258._218);

        struct258.models_0c[2].coord2_14.transforms.scale.x -= 0.041503906f; // ~1/24

        if(struct258.models_0c[2].coord2_14.transforms.scale.x < 0.0f) {
          struct258.models_0c[2].coord2_14.transforms.scale.x = 0.0f;
        }

        //LAB_800db74c
        struct258.models_0c[2].coord2_14.transforms.scale.set(struct258.models_0c[2].coord2_14.transforms.scale.x);

        renderCoolonMap(false, 0);
        break;

      case 8:
        FUN_80019c80(soundFiles_800bcf80[12], 1, 1);

        if(struct258.coolonWarpIndex_222 == 8) {
          gameState_800babc8._17c.set(coolonWarpDest_800ef228.get(struct258.coolonWarpIndex_222)._10.get(), true);

          //LAB_800db8f4
          mapState_800c6798.submapCut_c8 = locations_800f0e34.get(coolonWarpDest_800ef228.get(struct258.coolonWarpIndex_222)._10.get()).submapCut_08.get();
          mapState_800c6798.submapScene_ca = locations_800f0e34.get(coolonWarpDest_800ef228.get(struct258.coolonWarpIndex_222)._10.get()).submapScene_0a.get();
          submapCut_80052c30.set(mapState_800c6798.submapCut_c8);
          submapScene_80052c34.set(mapState_800c6798.submapScene_ca);
        } else {
          //LAB_800db9bc
          mapState_800c6798.submapCut_c8 = locations_800f0e34.get(coolonWarpDest_800ef228.get(struct258.coolonWarpIndex_222)._10.get()).submapCut_04.get();
          mapState_800c6798.submapScene_ca = locations_800f0e34.get(coolonWarpDest_800ef228.get(struct258.coolonWarpIndex_222)._10.get()).submapScene_06.get();
          submapCut_80052c30.set(mapState_800c6798.submapCut_c8);
          index_80052c38.set(mapState_800c6798.submapScene_ca);
          struct258._250 = 3;
          previousEngineState_8004dd28 = null;
        }

        //LAB_800dba98

        FUN_800e3fac(1);
        renderCoolonMap(false, 0);
        break;

      case 0xb:
        _800c66b0.coord2_20.coord.transfer.set(struct258.svec_200);
        _800c66b0.coord2_20.coord.transfer.setY(-1500);
        struct258.vec_94.x = struct258.svec_208.getX();
        struct258.vec_94.y = struct258.svec_208.getY();
        struct258.vec_94.z = struct258.svec_208.getZ();
        struct258.vec_94.y = -5000.0f;
        struct258.rotation_a4.y = struct258.angle_21c;
        _800c66b0.mapRotation_70.y = struct258.angle_21e;
        struct258.models_0c[2].coord2_14.transforms.rotate.set(0.0f, struct258.rotation_a4.y, 0.0f);
        struct258.models_0c[2].coord2_14.transforms.scale.set(0.375f, 0.375f, 0.375f);
        struct258._220 = 11;

        FUN_80019c80(soundFiles_800bcf80[12], 1, 1);

        // Fall through

      case 0xc:
        renderWinglyTeleportScreenEffect();

        _800c66b0.coord2_20.coord.transfer.y.add(0x70);

        if(_800c66b0.coord2_20.coord.transfer.getY() < struct258.svec_200.getY()) {
          _800c66b0.coord2_20.coord.transfer.setY(struct258.svec_200.getY());
        }

        //LAB_800dbd6c
        if(_800c66b0.coord2_20.coord.transfer.getY() >= struct258.svec_200.getY()) {
          struct258._220 = 12;
          struct258.vec_94.y = -0.09765625f; // 100/1024
        }

        //LAB_800dbdb8
        _800ef1a4.subu(0x1L);

        if(_800ef1a4.getSigned() < 0) {
          _800ef1a4.setu(0);
        }

        //LAB_800dbdec
        break;

      case 0xd:
        struct258.vec_94.y += 16.0f;

        if(struct258.svec_208.getY() < struct258.vec_94.y) {
          struct258.vec_94.y = struct258.svec_208.getY();
        }

        //LAB_800dbe70
        if(struct258.svec_208.getY() <= struct258.vec_94.y) {
          struct258._220 = -1;
        }

        //LAB_800dbeb4
        struct258.models_0c[2].coord2_14.transforms.scale.x -= 0.00390625f; // 1/256

        if(struct258.models_0c[2].coord2_14.transforms.scale.x < 0.25f) { // 64/256
          struct258.models_0c[2].coord2_14.transforms.scale.x = 0.25f;
        }

        //LAB_800dbf28
        struct258.models_0c[2].coord2_14.transforms.scale.set(struct258.models_0c[2].coord2_14.transforms.scale.x);

        _800ef1a4.subu(0x1L);

        if(_800ef1a4.getSigned() < 0) {
          _800ef1a4.setu(0);
        }

        //LAB_800dbfa0
        break;

      case 0:
        _800ef1a4.setu(0);

        _800c66b0.coord2_20.coord.transfer.set(struct258.svec_200);

        struct258.vec_94.set(
          struct258.svec_208.getX(),
          struct258.svec_208.getY(),
          struct258.svec_208.getZ()
        );

        struct258.rotation_a4.y = struct258.angle_21c;

        _800c66b0.mapRotation_70.y = struct258.angle_21e;

        struct258._250 = 0;
        struct258._220 = 0;
        return;
    }

    //LAB_800dc114
    renderMcq(mcqHeader_800c6768, 320, 0, -160, -120, orderingTableSize_1f8003c8.get() - 4, (int)_800ef1a4.get());

    //LAB_800dc164
  }

  @Method(0x800dc178L)
  public static void renderCoolonMap(final boolean enableInput, final long a1) {
    final WMapStruct258 struct = struct258_800c66a8;

    final CoolonWarpDestination20 warp1 = coolonWarpDest_800ef228.get(struct.coolonWarpIndex_221);
    final CoolonWarpDestination20 warp2 = coolonWarpDest_800ef228.get(struct.coolonWarpIndex_222);

    short x = (short)(warp1.x_18.get() - warp2.x_18.get());
    short y = (short)(warp1.y_1a.get() - warp2.y_1a.get());

    struct.rotation_a4.y = MathHelper.floorMod(MathHelper.atan2(y, x) + MathHelper.PI / 2.0f, MathHelper.TWO_PI);
    struct.models_0c[2].coord2_14.transforms.rotate.y += (struct.rotation_a4.y - struct.models_0c[2].coord2_14.transforms.rotate.y) / 8;

    if(enableInput) {
      if((repeat_800bee98.get() & 0x6000) != 0) {
        playSound(0, 1, 0, 0, (short)0, (short)0);

        if(struct.coolonWarpIndex_222 > 0) {
          struct.coolonWarpIndex_222--;
        } else {
          struct.coolonWarpIndex_222 = 8;
        }
      }

      //LAB_800dc384
      if((repeat_800bee98.get() & 0x9000) != 0) {
        playSound(0, 1, 0, 0, (short)0, (short)0);

        struct.coolonWarpIndex_222++;
        if(struct.coolonWarpIndex_222 > 8) {
          struct.coolonWarpIndex_222 = 0;
        }
      }
    }

    //LAB_800dc410
    final int u = tickCount_800bb0fc.get() / 5 % 3;

    //LAB_800dc468
    for(int sp1c = 0; sp1c < 9; sp1c++) {
      //LAB_800dc484
      final int left = coolonWarpDest_800ef228.get(sp1c).x_18.get();
      final int top = coolonWarpDest_800ef228.get(sp1c).y_1a.get();

      GPU.queueCommand(orderingTableSize_1f8003c8.get() - 4, new GpuCommandPoly(4)
        .bpp(Bpp.BITS_4)
        .translucent(Translucency.B_PLUS_F)
        .clut(640, 496)
        .vramPos(640, 256)
        .rgb(0x80, 0x80, 0xff)
        .pos(0, left, top)
        .pos(1, left + 10, top)
        .pos(2, left, top + 10)
        .pos(3, left + 10, top + 10)
        .uv(0, u * 16, 0)
        .uv(1, (u + 1) * 16, 0)
        .uv(2, u * 16, 16)
        .uv(3, (u + 1) * 16, 16)
      );
    }

    //LAB_800dc734
    x = (short)(coolonWarpDest_800ef228.get(struct.coolonWarpIndex_222).x_18.get() - 2);
    y = (short)(coolonWarpDest_800ef228.get(struct.coolonWarpIndex_222).y_1a.get() - 12);

    // Selection arrow
    GPU.queueCommand(17, new GpuCommandQuad()
      .bpp(Bpp.BITS_4)
      .clut(640, 496)
      .vramPos(640, 256)
      .rgb(0x80, 0x80, 0xff)
      .pos(x, y, 16, 16)
      .uv((tickCount_800bb0fc.get() & 0x7) * 16, 32)
    );

    if(a1 == 0) {
      //LAB_800dcbf4
      FUN_8002a3ec(7, 0);
      _800c86f0.setu(0);
    } else {
      x += 167;
      y += 116;

      final IntRef widthRef = new IntRef();
      final IntRef linesRef = new IntRef();
      measureText(coolonWarpDest_800ef228.get(struct.coolonWarpIndex_222).placeName_1c.deref(), widthRef, linesRef);
      final int width = widthRef.get();
      final int lines = linesRef.get();

      final long v0 = _800c86f0.get();
      if(v0 == 0) {
        //LAB_800dc9e4
        initTextbox(7, 0, x, y, width - 1, lines - 1);
        _800c86f0.setu(1);

        //LAB_800dca40
        textZ_800bdf00.set(14);
        textboxes_800be358[7].z_0c = 14;
        textboxes_800be358[7].chars_18 = Math.max(width, 4);
        textboxes_800be358[7].lines_1a = lines;
        _800c86f0.setu(2);
      } else if(v0 == 1) {
        textZ_800bdf00.set(14);
        textboxes_800be358[7].z_0c = 14;
        textboxes_800be358[7].chars_18 = Math.max(width, 4);
        textboxes_800be358[7].lines_1a = lines;
        _800c86f0.setu(2);
        //LAB_800dc9d0
      } else if(v0 == 2) {
        //LAB_800dca9c
        textboxes_800be358[7].chars_18 = Math.max(width, 4);
        textboxes_800be358[7].lines_1a = lines;
        textboxes_800be358[7].width_1c = textboxes_800be358[7].chars_18 * 9 / 2;
        textboxes_800be358[7].height_1e = textboxes_800be358[7].lines_1a * 6;
        textboxes_800be358[7].x_14 = x;
        textboxes_800be358[7].y_16 = y;
      }

      //LAB_800dcb48
      textZ_800bdf00.set(18);
      textboxes_800be358[7].z_0c = 18;
      renderCenteredShadowedText(coolonWarpDest_800ef228.get(struct.coolonWarpIndex_222).placeName_1c.deref(), x, y - lines * 7 + 1, TextColour.WHITE, 0);
    }

    //LAB_800dcc0c
  }

  @Method(0x800dcc20L)
  public static void lerp(final Vector3f out, final Vector3f a, final Vector3f b, final float ratio) {
    if(ratio == 0.0f) {
      out.set(a);
    } else if(ratio == 1.0f) {
      out.set(b);
    } else {
      //LAB_800dcca4
      out.x = (b.x - a.x) * ratio + a.x;
      out.y = (b.y - a.y) * ratio + a.y;
      out.z = (b.z - a.z) * ratio + a.z;
    }

    //LAB_800dcddc
  }

  @Method(0x800dcde8L)
  public static void deallocateWorldMap() {
    struct258_800c66a8._1fc = null;
  }

  @Method(0x800dce64L)
  public static void rotateCoord2(final SVECTOR rotation, final GsCOORDINATE2 coord2) {
    final MATRIX mat = new MATRIX().identity();
    mat.transfer.set(coord2.coord.transfer);

    RotMatrix_Xyz(rotation, mat);

    coord2.flg = 0;
    coord2.coord.set(mat);
  }

  public static void rotateCoord2(final Vector3f rotation, final GsCOORDINATE2 coord2) {
    final MATRIX mat = new MATRIX().identity();
    mat.transfer.set(coord2.coord.transfer);

    RotMatrix_Xyz(rotation, mat);

    coord2.flg = 0;
    coord2.coord.set(mat);
  }

  /** Don't really know what makes it special. Seems to use a fixed Z value and doesn't check if the triangles are on screen. Used for water. */
  @Method(0x800dd05cL)
  public static void renderSpecialDobj2(final ModelPart10 dobj2) {
    final SVECTOR[] vertices = dobj2.tmd_08.vert_top_00;

    for(final TmdObjTable1c.Primitive primitive : dobj2.tmd_08.primitives_10) {
      final int command = primitive.header() & 0xff04_0000;

      if(command == 0x3d00_0000) {
        FUN_800deeac(primitive, vertices);
      } else {
        assert false;
      }
    }
  }

  @Method(0x800deeacL)
  public static void FUN_800deeac(final TmdObjTable1c.Primitive primitive, final SVECTOR[] vertices) {
    //LAB_800deee8
    for(final byte[] data : primitive.data()) {
      final int tpage = IoHelper.readUShort(data, 0x6);

      final GpuCommandPoly cmd = new GpuCommandPoly(4)
        .bpp(Bpp.of(tpage >>> 7 & 0b11))
        .clut(1008, (int)_800ef348.offset(struct258_800c66a8._28 * 0x2L).get())
        .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
        .uv(0, IoHelper.readUByte(data, 0x0), IoHelper.readUByte(data, 0x1))
        .uv(1, IoHelper.readUByte(data, 0x4), IoHelper.readUByte(data, 0x5))
        .uv(2, IoHelper.readUByte(data, 0x8), IoHelper.readUByte(data, 0x9))
        .uv(3, IoHelper.readUByte(data, 0xc), IoHelper.readUByte(data, 0xd));

      //LAB_800def00
      final SVECTOR vert0 = vertices[IoHelper.readUShort(data, 0x20)];
      final SVECTOR vert1 = vertices[IoHelper.readUShort(data, 0x22)];
      final SVECTOR vert2 = vertices[IoHelper.readUShort(data, 0x24)];
      GTE.perspectiveTransformTriangle(vert0, vert1, vert2);

      if(!GTE.hasError()) {
        //LAB_800defac
        if(GTE.normalClipping() > 0) { // Is visible
          //LAB_800defe8
          cmd
            .pos(0, GTE.getScreenX(0), GTE.getScreenY(0))
            .pos(1, GTE.getScreenX(1), GTE.getScreenY(1))
            .pos(2, GTE.getScreenX(2), GTE.getScreenY(2));

          GTE.perspectiveTransform(vertices[IoHelper.readUShort(data, 0x26)]);

          if(!GTE.hasError()) { // No errors
            //LAB_800df0ac
            cmd
              .pos(3, GTE.getScreenX(2), GTE.getScreenY(2))
              .rgb(0, IoHelper.readInt(data, 0x10))
              .rgb(1, IoHelper.readInt(data, 0x14))
              .rgb(2, IoHelper.readInt(data, 0x18))
              .rgb(3, IoHelper.readInt(data, 0x1c));

            GPU.queueCommand(tempZ_800c66d8.get(), cmd);
          }
        }
      }
    }
  }

  @Method(0x800dfa70L)
  public static void FUN_800dfa70() {
    filesLoadedFlags_800c66b8.and(0xffff_fd57L);

    loadDrgnDir(0, 5713, files -> timsLoaded(files, 0x2a8));

    //LAB_800dfacc
    for(int i = 0; i < 4; i++) {
      //LAB_800dfae8
      struct258_800c66a8.models_0c[i] = new Model124("Player " + i);
      final int finalI = i;
      loadDrgnDir(0, 5714 + i, files -> FUN_800d5a30(files, finalI));
      struct258_800c66a8.models_0c[i].colourMap_9d = (int)_800ef694.offset(i).get() + 0x80;
    }

    //LAB_800dfbb4
    struct258_800c66a8._248 = 0;
  }

  @Method(0x800dfbd8L)
  public static void FUN_800dfbd8() {
    final WMapStruct258 struct258 = struct258_800c66a8;
    struct258.vec_94.x = struct258.coord2_34.coord.transfer.getX();
    struct258.vec_94.y = struct258.coord2_34.coord.transfer.getY();
    struct258.vec_94.z = struct258.coord2_34.coord.transfer.getZ();
    struct258.vec_84.set(struct258.vec_94);

    //LAB_800dfca4
    for(int i = 0; i < 4; i++) {
      final Model124 model = struct258.models_0c[i];

      //LAB_800dfcc0
      initModel(model, struct258._b4[i].extendedTmd_00, struct258._b4[i].tmdAnim_08[0]);
      loadModelStandardAnimation(model, struct258._b4[i].tmdAnim_08[0]);

      model.coord2_14.coord.transfer.set(struct258.coord2_34.coord.transfer);
      model.coord2_14.transforms.rotate.set(0.0f, struct258.rotation_a4.y, 0.0f);
      model.coord2_14.transforms.scale.zero();
    }

    //LAB_800dff4c
    struct258.currentAnimIndex_ac = 2;
    struct258.animIndex_b0 = 2;

    //LAB_800dff70
    for(int i = 0; i < 8; i++) {
      //LAB_800dff8c
      struct258._1c4[i * 2    ] = rcos(i * 0x200) * 0x20 >> 12;
      struct258._1c4[i * 2 + 1] = rsin(i * 0x200) * 0x20 >> 12;
    }

    //LAB_800e002c
    struct258.modelIndex_1e4 = areaData_800f2248.get(mapState_800c6798.areaIndex_12).modelIndex_06.get();
    FUN_800e28dc(40, 1);

    final int modelIndex = struct258.modelIndex_1e4;
    final Model124 model = struct258.models_0c[modelIndex];
    if(modelIndex == 0) {
      //LAB_800e00c4
      model.coord2_14.transforms.scale.set(0.5f, 0.4f, 0.5f);
    } else if(modelIndex == 1) {
      //LAB_800e0114
      if(mapState_800c6798.continentIndex_00 == 7) { // Teleporting
        model.coord2_14.transforms.scale.set(1.0f, 1.0f, 1.0f);
      } else {
        model.coord2_14.transforms.scale.set(2.0f, 2.0f, 2.0f);
      }

      //LAB_800e01b8
      //LAB_800e00a4
    } else if(modelIndex == 2) {
      //LAB_800e01c0
      model.coord2_14.transforms.scale.zero();
    } else if(modelIndex == 3) {
      //LAB_800e0210
      model.coord2_14.transforms.scale.zero();
    }

    //LAB_800e0260
  }

  @Method(0x800e0274L) // Pretty sure this renders the player
  public static void renderPlayer() {
    final WMapStruct258 struct = struct258_800c66a8;

    if(struct._250 != 2) {
      struct.modelIndex_1e4 = areaData_800f2248.get(mapState_800c6798.areaIndex_12).modelIndex_06.get();

      assert struct.modelIndex_1e4 < 4;
    } else {
      //LAB_800e02d0
      struct.modelIndex_1e4 = 2;
    }

    //LAB_800e02e0
    applyModelRotationAndScale(struct.models_0c[struct.modelIndex_1e4]);
    animateModel(struct.models_0c[struct.modelIndex_1e4]);

    final int modelIndex = struct.modelIndex_1e4;
    if(modelIndex == 0) {
      //LAB_800e03a0
      GTE.setBackgroundColour(0.78125f, 0.78125f, 0.78125f);

      struct.models_0c[0].coord2_14.transforms.scale.set(0.5f, 0.4f, 0.5f);
    } else if(modelIndex == 1) {
      //LAB_800e0404
      GTE.setBackgroundColour(0.5f, 0.5f, 0.5f);

      if(mapState_800c6798.continentIndex_00 == 7) { // Teleporting
        struct.models_0c[1].coord2_14.transforms.scale.set(1.0f, 1.0f, 1.0f);
      } else {
        struct.models_0c[1].coord2_14.transforms.scale.set(2.0f, 2.0f, 2.0f);
      }

      //LAB_800e04bc
      //LAB_800e0380
    } else if(modelIndex == 2) {
      //LAB_800e04c4
      GTE.setBackgroundColour(0.5f, 0.5f, 0.5f);
    } else if(modelIndex == 3) {
      //LAB_800e04e0
      GTE.setBackgroundColour(0.5f, 0.5f, 0.5f);
    }

    //LAB_800e04fc
    struct.models_0c[struct.modelIndex_1e4].zOffset_a0 = 78;
    renderModel(struct.models_0c[struct.modelIndex_1e4]);
    GTE.setBackgroundColour(_800c66b0.ambientLight_14c.x, _800c66b0.ambientLight_14c.y, _800c66b0.ambientLight_14c.z);
    FUN_800e06d0();
    FUN_800e1364();
  }

  @Method(0x800e05c4L)
  public static void unloadWmapPlayerModels() {
    //LAB_800e05d8
    for(int i = 0; i < 4; i++) {
      //LAB_800e05f4
      struct258_800c66a8.models_0c[i] = null;
    }
  }

  @Method(0x800e06d0L)
  public static void FUN_800e06d0() {
    struct258_800c66a8.vec_84.set(struct258_800c66a8.vec_94);

    if(struct258_800c66a8._250 == 0) {
      //LAB_800e0760
      FUN_800e8a10();
    } else if(struct258_800c66a8._250 == 1) {
      //LAB_800e0770
      //LAB_800e0774
      int locationIndex = 0;
      for(int i = 0; i < 6; i++) {
        //LAB_800e0790
        if(mapState_800c6798.locationIndex_10 == _800ef698.get(i).locationIndex_00.get()) {
          locationIndex = _800ef698.get(i).locationIndex2_04.get();
          break;
        }
      }

      //LAB_800e0810
      final Vector3f sp0x18 = new Vector3f();
      final Vector3f sp0x20 = new Vector3f();
      FUN_800e0d70(mapState_800c6798.locationIndex_10, sp0x18);
      FUN_800e0d70(locationIndex, sp0x20);

      //LAB_800e0878
      if(struct258_800c66a8._248 == 0 || struct258_800c66a8._248 == 1) {
        if(struct258_800c66a8._248 == 0) {
          //LAB_800e0898
          struct258_800c66a8._24c = 0;
          struct258_800c66a8._248 = 1;
        }

        //LAB_800e08b8
        renderWinglyTeleportScreenEffect();

        lerpish(struct258_800c66a8.vec_94, sp0x18, sp0x20, 32.0f / struct258_800c66a8._24c);

        struct258_800c66a8._24c++;
        if(struct258_800c66a8._24c > 32) {
          struct258_800c66a8._248 = 2;
        }

        //LAB_800e0980
        final float scale = struct258_800c66a8._24c * 0x40 + (rsin(struct258_800c66a8._24c * 0x200) * 0x100 >> 12) / (float)0x1000;
        struct258_800c66a8.models_0c[3].coord2_14.transforms.scale.set(scale, scale, scale);
        struct258_800c66a8.models_0c[struct258_800c66a8.modelIndex_1e4].coord2_14.transforms.rotate.y = _800c66b0.mapRotation_70.y;
        struct258_800c66a8.rotation_a4.y = _800c66b0.mapRotation_70.y;
      } else if(struct258_800c66a8._248 == 2) {
        //LAB_800e0a6c
        gameState_800babc8._17c.set(locationIndex, true);

        //LAB_800e0b64
        mapState_800c6798.submapCut_c8 = locations_800f0e34.get(locationIndex).submapCut_08.get();
        mapState_800c6798.submapScene_ca = locations_800f0e34.get(locationIndex).submapScene_0a.get();
        submapCut_80052c30.set(mapState_800c6798.submapCut_c8);
        submapScene_80052c34.set(mapState_800c6798.submapScene_ca);

        FUN_800e3fac(1);
        struct258_800c66a8._248 = 3;
      } else if(struct258_800c66a8._248 == 3) {
        //LAB_800e0c00
        struct258_800c66a8.models_0c[3].coord2_14.transforms.scale.x -= 0.25f;

        if(struct258_800c66a8.models_0c[3].coord2_14.transforms.scale.x < 0.0f) {
          struct258_800c66a8.models_0c[3].coord2_14.transforms.scale.x = 0.0f;
        }

        //LAB_800e0c70
        final float a0 = struct258_800c66a8.models_0c[3].coord2_14.transforms.scale.x;
        struct258_800c66a8.models_0c[3].coord2_14.transforms.scale.y = a0;
        struct258_800c66a8.models_0c[3].coord2_14.transforms.scale.z = a0;
      }

      //LAB_800e0cbc
    }

    //LAB_800e0cc4
    struct258_800c66a8.rotation_a4.x = MathHelper.floorMod(struct258_800c66a8.rotation_a4.x, MathHelper.TWO_PI);
    struct258_800c66a8.rotation_a4.y = MathHelper.floorMod(struct258_800c66a8.rotation_a4.y, MathHelper.TWO_PI);
    struct258_800c66a8.rotation_a4.z = MathHelper.floorMod(struct258_800c66a8.rotation_a4.z, MathHelper.TWO_PI);

    FUN_800e10a0();
  }

  @Method(0x800e0d70L)
  public static void FUN_800e0d70(final int locationIndex, final Vector3f a1) {
    //LAB_800e0d84
    for(int i = 0; i < 6; i++) {
      //LAB_800e0da0
      if(locationIndex == _800ef6c8.get(i).locationIndex_00.get()) {
        a1.set(
          _800ef6c8.get(i)._04.getX(),
          _800ef6c8.get(i)._04.getY(),
          _800ef6c8.get(i)._04.getZ()
        );
        break;
      }
    }

    //LAB_800e0e3c
  }

  /** lerp, but I think it decreases Y more the lower the ratio */
  @Method(0x800e0e4cL)
  public static void lerpish(final Vector3f out, final Vector3f a1, final Vector3f a2, final float ratio) {
    if(ratio == 0.0f) {
      out.set(a1);
    } else if(ratio == 1.0f) {
      out.set(a2);
    } else {
      //LAB_800e0ed8
      out.x = a1.x + (a2.x - a1.x) * ratio;
      out.y = a1.y + (a2.y - a1.y) * ratio + MathHelper.sin(MathHelper.PI * ratio) * -200;
      out.z = a1.z + (a2.z - a1.z) * ratio;
    }

    //LAB_800e108c
  }

  @Method(0x800e10a0L) //TODO this might control player animation?
  public static void FUN_800e10a0() {
    final WMapStruct258 struct = struct258_800c66a8;

    struct.currentAnimIndex_ac = struct.animIndex_b0;

    if(struct.vec_84.x != struct.vec_94.x || struct.vec_84.y != struct.vec_94.y || struct.vec_84.z != struct.vec_94.z) {
      final EncounterRateMode mode = CONFIG.getConfig(CoreMod.ENCOUNTER_RATE_CONFIG.get());

      //LAB_800e117c
      //LAB_800e11b0
      if(Input.getButtonState(InputAction.BUTTON_EAST) || analogMagnitude_800beeb4.get() >= 0x7f) { // World Map Running
        //LAB_800e11d0
        struct.animIndex_b0 = 4;
        handleEncounters(mode.worldMapRunModifier);
      } else {
        //LAB_800e11f4
        struct.animIndex_b0 = 3;
        handleEncounters(mode.worldMapWalkModifier);
      }

      //LAB_800e1210
      if(struct.modelIndex_1e4 == 1) {
        if((tickCount_800bb0fc.get() & 0x3) == 0) {
          playSound(0xc, 0, 0, 0, (short)0, (short)0);
        }
      }
    } else {
      struct.animIndex_b0 = 2;
    }

    //LAB_800e1264
    final int modelIndex = struct.modelIndex_1e4;

    if(modelIndex >= 1 && modelIndex < 4) {
      //LAB_800e1298
      struct.animIndex_b0 = 2;
    }

    //LAB_800e12b0
    if(struct.currentAnimIndex_ac != struct.animIndex_b0) {
      loadModelStandardAnimation(struct.models_0c[struct.modelIndex_1e4], struct._b4[struct.modelIndex_1e4].tmdAnim_08[struct.animIndex_b0 - 2]);
    }

    //LAB_800e1354
  }

  @Method(0x800e1364L)
  public static void FUN_800e1364() {
    renderPlayerShadow();

    final WMapStruct258 struct = struct258_800c66a8;
    struct.coord2_34.coord.transfer.set(struct.vec_94);
    struct.models_0c[struct.modelIndex_1e4].coord2_14.coord.transfer.set(struct.coord2_34.coord.transfer);

    if(struct._250 == 0) {
      float sp10 = struct.rotation_a4.y - struct.models_0c[struct.modelIndex_1e4].coord2_14.transforms.rotate.y;
      final float sp14 = struct.rotation_a4.y - (struct.models_0c[struct.modelIndex_1e4].coord2_14.transforms.rotate.y - MathHelper.TWO_PI);

      if(Math.abs(sp14) < Math.abs(sp10)) {
        sp10 = sp14;
      }

      //LAB_800e15e4
      struct.models_0c[struct.modelIndex_1e4].coord2_14.transforms.rotate.y += sp10 / 2.0f;
      struct.models_0c[struct.modelIndex_1e4].coord2_14.transforms.rotate.x = struct.rotation_a4.x;
      struct.models_0c[struct.modelIndex_1e4].coord2_14.transforms.rotate.z = struct.rotation_a4.z;
    }

    //LAB_800e16f8
    rotateCoord2(struct.rotation_a4, struct.coord2_34);
  }

  @Method(0x800e1740L)
  public static void renderDartShadow() {
    final MATRIX sp0x28 = new MATRIX();
    final SVECTOR vert0 = new SVECTOR();
    final SVECTOR vert1 = new SVECTOR();
    final SVECTOR vert2 = new SVECTOR();
    final DVECTOR sxy0 = new DVECTOR();
    final DVECTOR sxy1 = new DVECTOR();
    final DVECTOR sxy2 = new DVECTOR();

    GsGetLs(struct258_800c66a8.models_0c[struct258_800c66a8.modelIndex_1e4].coord2_14, sp0x28);
    setRotTransMatrix(sp0x28);

    //LAB_800e17b4
    for(int i = 0; i < 8; i++) {
      //LAB_800e17d0
      vert1.set((short)struct258_800c66a8._1c4[ i            * 2], (short)0, (short)struct258_800c66a8._1c4[ i            * 2 + 1]);
      vert2.set((short)struct258_800c66a8._1c4[(i + 1 & 0x7) * 2], (short)0, (short)struct258_800c66a8._1c4[(i + 1 & 0x7) * 2 + 1]);

      final int z = perspectiveTransformTriple(vert0, vert1, vert2, sxy0, sxy1, sxy2);

      if(z >= 3 && z < orderingTableSize_1f8003c8.get()) {
        final GpuCommandPoly cmd = new GpuCommandPoly(3)
          .bpp(Bpp.BITS_4)
          .translucent(Translucency.B_MINUS_F)
          .monochrome(0, 0x80)
          .monochrome(1, 0)
          .monochrome(2, 0)
          .pos(0, sxy0.getX(), sxy0.getY())
          .pos(1, sxy1.getX(), sxy1.getY())
          .pos(2, sxy2.getX(), sxy2.getY());

        GPU.queueCommand(78 + z, cmd);
      }

      //LAB_800e1a98
    }

    //LAB_800e1ab0
  }

  @Method(0x800e1ac4L)
  public static void renderQueenFuryShadow() {
    final MATRIX sp0x28 = new MATRIX();
    final SVECTOR sp0x48 = new SVECTOR();
    final SVECTOR sp0x50 = new SVECTOR();
    final SVECTOR sp0x58 = new SVECTOR();
    final SVECTOR sp0x60 = new SVECTOR();

    final IntRef sp0x70 = new IntRef();
    final IntRef sp0x74 = new IntRef();

    final Vector3f sp0x88 = new Vector3f();
    final Vector3f sp0x98 = new Vector3f();
    final Vector3f sp0xa8 = new Vector3f();

    final Vector3f delta = new Vector3f(struct258_800c66a8.vec_84)
      .sub(struct258_800c66a8.vec_94)
      .normalize()
      .cross(_800c87d8);
    FUN_800e2ae4(delta, struct258_800c66a8.vec_94);
    rotateCoord2(struct258_800c66a8.tmdRendering_08.rotations_08[0], struct258_800c66a8.tmdRendering_08.coord2s_04[0]);
    GsGetLs(struct258_800c66a8.tmdRendering_08.coord2s_04[0], sp0x28);
    setRotTransMatrix(sp0x28);

    //LAB_800e1ccc
    for(int i = 0; i < 39; i++) {
      //LAB_800e1ce8
      FUN_800e2e1c(i, sp0x88, sp0x98, sp0x74, sp0x70);
      float spc8 = sp0x88.x * sp0x70.get();
      float spcc = sp0x88.y * sp0x70.get();
      float spd0 = sp0x88.z * sp0x70.get();
      final float spd8 = -spc8;
      final float spdc = -spcc;
      final float spe0 = -spd0;
      sp0x48.setX((short)(spc8 + sp0x98.x));
      sp0x48.setY((short)(spcc + sp0x98.y));
      sp0x48.setZ((short)(spd0 + sp0x98.z));
      sp0x50.setX((short)sp0x98.x);
      sp0x50.setY((short)sp0x98.y);
      sp0x50.setZ((short)sp0x98.z);

      FUN_800e2e1c(i + 1, sp0x88, sp0xa8, sp0x74, sp0x70);
      spc8 = sp0x88.x * sp0x70.get();
      spcc = sp0x88.y * sp0x70.get();
      spd0 = sp0x88.z * sp0x70.get();
      final float spe8 = -spc8;
      final float spec = -spcc;
      final float spf0 = -spd0;
      sp0x58.setX((short)(spc8 + sp0xa8.x));
      sp0x58.setY((short)(spcc + sp0xa8.y));
      sp0x58.setZ((short)(spd0 + sp0xa8.z));
      sp0x60.set(sp0xa8);

      int sp78 = 256 - sp0x74.get() * 256 / 40;
      final int r0 = sp78 * 96 / 256;
      final int g0 = sp78 * 96 / 256;
      final int b0 = sp78 * 96 / 256;
      final int r1 = 0;
      final int g1 = sp78 / 8;
      final int b1 = sp78 * 96 / 256;

      sp78 = 256 - sp0x74.get() * 256 / 40;
      final int r2 = sp78 * 96 / 256;
      final int g2 = sp78 * 96 / 256;
      final int b2 = sp78 * 96 / 256;
      final int r3 = 0;
      final int g3 = sp78 / 8;
      final int b3 = sp78 * 96 / 256;

      final SVECTOR sxyz0 = new SVECTOR();
      final SVECTOR sxyz1 = new SVECTOR();
      final SVECTOR sxyz2 = new SVECTOR();
      final SVECTOR sxyz3 = new SVECTOR();

      int z = RotTransPers4(sp0x48, sp0x50, sp0x58, sp0x60, sxyz0, sxyz1, sxyz2, sxyz3);

      if(z >= 3 && z < orderingTableSize_1f8003c8.get()) {
        final GpuCommandPoly cmd = new GpuCommandPoly(4)
          .bpp(Bpp.BITS_4)
          .translucent(Translucency.B_PLUS_F)
          .clut(1008, (int)_800ef348.offset(struct258_800c66a8._28 * 0x2L).get())
          .vramPos(448, 0)
          .rgb(0, r0, g0, b0)
          .rgb(1, r1, g1, b1)
          .rgb(2, r2, g2, b2)
          .rgb(3, r3, g3, b3)
          .uv(0,  0,  0)
          .uv(1, 63,  0)
          .uv(2,  0, 63)
          .uv(3, 63, 63)
          .pos(0, sxyz0.getX(), sxyz0.getY())
          .pos(1, sxyz1.getX(), sxyz1.getY())
          .pos(2, sxyz2.getX(), sxyz2.getY())
          .pos(3, sxyz3.getX(), sxyz3.getY());

        GPU.queueCommand(orderingTableSize_1f8003c8.get() - 4, cmd);
      }

      //LAB_800e2440
      sp0x48.setX((short)(spd8 + sp0x98.x));
      sp0x48.setY((short)(spdc + sp0x98.y));
      sp0x48.setZ((short)(spe0 + sp0x98.z));
      sp0x58.setX((short)(spe8 + sp0xa8.x));
      sp0x58.setY((short)(spec + sp0xa8.y));
      sp0x58.setZ((short)(spf0 + sp0xa8.z));
      z = RotTransPers4(sp0x48, sp0x50, sp0x58, sp0x60, sxyz0, sxyz1, sxyz2, sxyz3);

      if(z >= 3 && z < orderingTableSize_1f8003c8.get()) {
        final GpuCommandPoly cmd = new GpuCommandPoly(4)
          .bpp(Bpp.BITS_4)
          .translucent(Translucency.B_PLUS_F)
          .clut(1008, (int)_800ef348.offset(struct258_800c66a8._28 * 0x2L).get())
          .vramPos(448, 0)
          .rgb(0, r0, g0, b0)
          .rgb(1, r1, g1, b1)
          .rgb(2, r2, g2, b2)
          .rgb(3, r3, g3, b3)
          .uv(0,  0,  0)
          .uv(1, 63,  0)
          .uv(2,  0, 63)
          .uv(3, 63, 63)
          .pos(0, sxyz0.getX(), sxyz0.getY())
          .pos(1, sxyz1.getX(), sxyz1.getY())
          .pos(2, sxyz2.getX(), sxyz2.getY())
          .pos(3, sxyz3.getX(), sxyz3.getY());

        GPU.queueCommand(orderingTableSize_1f8003c8.get() - 4, cmd);
      }
    }

    //LAB_800e2770
    //LAB_800e2774
    for(int i = 0; i < 40; i++) {
      //LAB_800e2790
      int sp6c = struct258_800c66a8._230 - i * struct258_800c66a8._23c;

      if(sp6c < 0) {
        sp6c += struct258_800c66a8._238;
      }

      //LAB_800e2808
      struct258_800c66a8._22c[sp6c]++;
    }

    //LAB_800e289c
    struct258_800c66a8._240++;
  }

  @Method(0x800e28dcL)
  public static void FUN_800e28dc(final int a0, final int a1) {
    final int count = a0 * a1;

    struct258_800c66a8.vecs_224 = new Vector3f[count];
    struct258_800c66a8.vecs_228 = new Vector3f[count];

    struct258_800c66a8._22c = new int[count];
    struct258_800c66a8._230 = 0;
    struct258_800c66a8._234 = count - 1;
    struct258_800c66a8._238 = count;
    struct258_800c66a8._23c = a1;

    //NOTE: there's a bug in the original code, it just sets the first vector in the array over and over again
    Arrays.setAll(struct258_800c66a8.vecs_224, i -> new Vector3f());
    Arrays.setAll(struct258_800c66a8.vecs_228, i -> new Vector3f());

    struct258_800c66a8._244 = 0;
  }

  @Method(0x800e2ae4L)
  public static void FUN_800e2ae4(final Vector3f a0, final Vector3f a1) {
    final WMapStruct258 struct258 = struct258_800c66a8;

    if(struct258._244 == 0) {
      //LAB_800e2b14
      for(int i = 0; i < struct258._238; i++) {
        //LAB_800e2b3c
        struct258.vecs_224[i].set(a0);
        struct258.vecs_228[i].set(a1);
      }

      //LAB_800e2ca4
      struct258._244 = 1;
      struct258._240 = 0;
    }

    //LAB_800e2cc4
    struct258.vecs_224[struct258._230].set(a0);
    struct258.vecs_228[struct258._230].set(a1);

    struct258._22c[struct258._230] = 0;

    struct258._234 = struct258._230;
    struct258._230 = (struct258._230 + 1) % struct258._238;
  }

  @Method(0x800e2e1cL)
  public static void FUN_800e2e1c(final int a0, final Vector3f a1, final Vector3f a2, final IntRef a3, final IntRef a4) {
    if(a0 == 0) {
      a1.set(struct258_800c66a8.vecs_224[struct258_800c66a8._234]);
      a2.set(struct258_800c66a8.vecs_228[struct258_800c66a8._234]);
      a3.set(struct258_800c66a8._22c[struct258_800c66a8._234]);
      final int v0 = struct258_800c66a8._22c[struct258_800c66a8._234] - struct258_800c66a8._240;
      a4.set(struct258_800c66a8._22c[struct258_800c66a8._234] + (rsin(v0 << 8 & 0x7ff) * struct258_800c66a8._22c[struct258_800c66a8._234] >> 12));
    } else {
      //LAB_800e3024
      int sp10 = struct258_800c66a8._230 - a0 * struct258_800c66a8._23c;

      if(sp10 < 0) {
        sp10 += struct258_800c66a8._238;
      }

      //LAB_800e3090
      a1.set(struct258_800c66a8.vecs_224[sp10]);
      a2.set(struct258_800c66a8.vecs_228[sp10]);
      a3.set(struct258_800c66a8._22c[sp10]);
      final int v0 = struct258_800c66a8._22c[sp10] - struct258_800c66a8._240;
      a4.set(struct258_800c66a8._22c[sp10] + (rsin(v0 << 8 & 0x7ff) * struct258_800c66a8._22c[sp10] >> 12));
    }

    //LAB_800e321c
  }

  @Method(0x800e32a8L)
  public static void renderPlayerShadow() {
    shadowRenderers_800ef684[struct258_800c66a8.modelIndex_1e4].run();
  }

  @Method(0x800e32fcL)
  public static void FUN_800e32fc() {
    // no-op
  }

  /** Some kind of full-screen effect during the Wingly teleportation between Aglis and Zenebatos */
  @Method(0x800e3304L)
  public static void renderWinglyTeleportScreenEffect() {
    final GpuCommandQuad cmd = new GpuCommandQuad()
      .bpp(Bpp.BITS_15)
      .translucent(Translucency.HALF_B_PLUS_HALF_F)
      .vramPos(0, 0)
      .monochrome(0x80)
      .pos(-160, -120, 320, 240)
      .texture(GPU.getDisplayBuffer());

    GPU.queueCommand(5, cmd);
  }

  @Method(0x800e367cL)
  public static void handleEncounters(final float encounterRateMultiplier) {
    //LAB_800e36a8
    if(worldMapState_800c6698 != 5) {
      return;
    }

    //LAB_800e36c4
    if(playerState_800c669c != 5) {
      return;
    }

    //LAB_800e36e0
    if(struct258_800c66a8.modelIndex_1e4 >= 2) {
      return;
    }

    //LAB_800e3708
    if(mapState_800c6798._d8 != 0) {
      return;
    }

    //LAB_800e3724
    if(struct258_800c66a8._05 != 0) {
      return;
    }

    //LAB_800e3748
    if(mapState_800c6798._d4 != 0 || mapState_800c6798._d8 != 0) {
      //LAB_800e3778
      return;
    }

    //LAB_800e3780
    //LAB_800e3794
    final AreaData08 area = areaData_800f2248.get(mapState_800c6798.areaIndex_12);
    encounterAccumulator_800c6ae8.add(Math.round(area.encounterRate_03.get() * encounterRateMultiplier * 70));

    if(encounterAccumulator_800c6ae8.get() >= 5120) {
      encounterAccumulator_800c6ae8.set(0);

      if(area.stage_04.get() == -1) {
        combatStage_800bb0f4.set(1);
      } else {
        //LAB_800e386c
        combatStage_800bb0f4.set(area.stage_04.get());
      }

      //LAB_800e3894
      final byte encounterIndex = area.encounterIndex_05.get();

      if(encounterIndex == -1) {
        encounterId_800bb0f8.set(0);
      } else {
        //LAB_800e38dc
        final int rand = simpleRand() % 100;

        if(rand < 35) {
          encounterId_800bb0f8.set(encounterIds_800ef364.get(encounterIndex).get(0).get());
          //LAB_800e396c
        } else if(rand < 70) {
          encounterId_800bb0f8.set(encounterIds_800ef364.get(encounterIndex).get(1).get());
          //LAB_800e39c0
        } else if(rand < 90) {
          encounterId_800bb0f8.set(encounterIds_800ef364.get(encounterIndex).get(2).get());
        } else {
          //LAB_800e3a14
          encounterId_800bb0f8.set(encounterIds_800ef364.get(encounterIndex).get(3).get());
        }
      }

      //LAB_800e3a38
      gameState_800babc8.areaIndex_4de = mapState_800c6798.areaIndex_12;
      gameState_800babc8.pathIndex_4d8 = mapState_800c6798.pathIndex_14;
      gameState_800babc8.dotIndex_4da = mapState_800c6798.dotIndex_16;
      gameState_800babc8.dotOffset_4dc = mapState_800c6798.dotOffset_18;
      gameState_800babc8.facing_4dd = mapState_800c6798.facing_1c;
      pregameLoadingStage_800bb10c.set(8);
    }

    //LAB_800e3a94
  }

  @Method(0x800e3aa8L)
  public static WMapTmdRenderingStruct18 loadTmd(final TmdWithId tmd) {
    final WMapTmdRenderingStruct18 sp10 = new WMapTmdRenderingStruct18();
    sp10.count_0c = allocateTmdRenderer(sp10, tmd);

    //LAB_800e3b00
    return sp10;
  }

  @Method(0x800e3bd4L)
  public static int allocateTmdRenderer(final WMapTmdRenderingStruct18 a0, final TmdWithId tmd) {
    final int nobj = tmd.tmd.header.nobj;
    a0.dobj2s_00 = new ModelPart10[nobj];
    a0.coord2s_04 = new GsCOORDINATE2[nobj];
    a0.rotations_08 = new Vector3f[nobj];
    a0._10 = new int[nobj];

    Arrays.setAll(a0.dobj2s_00, i -> new ModelPart10());
    Arrays.setAll(a0.coord2s_04, i -> new GsCOORDINATE2());
    Arrays.setAll(a0.rotations_08, i -> new Vector3f());

    //LAB_800e3d24
    for(int i = 0; i < nobj; i++) {
      //LAB_800e3d44
      a0.dobj2s_00[i].tmd_08 = tmd.tmd.objTable[i];
    }

    //LAB_800e3d80
    //LAB_800e3d94
    return nobj;
  }

  @Method(0x800e3da8L)
  public static void initTmdTransforms(final WMapTmdRenderingStruct18 a0, @Nullable final GsCOORDINATE2 superCoord) {
    //LAB_800e3dfc
    for(int i = 0; i < a0.count_0c; i++) {
      final ModelPart10 dobj2 = a0.dobj2s_00[i];
      final GsCOORDINATE2 coord2 = a0.coord2s_04[i];
      final Vector3f rotation = a0.rotations_08[i];

      //LAB_800e3e20
      GsInitCoordinate2(superCoord, coord2);

      dobj2.coord2_04 = coord2;
      coord2.coord.transfer.set(0, 0, 0);
      rotation.set(0.0f, 0.0f, 0.0f);
    }

    //LAB_800e3ee8
  }

  @Method(0x800e3efcL)
  public static void setAllCoord2Attribs(final WMapTmdRenderingStruct18 a0, final int attribute) {
    //LAB_800e3f24
    for(int i = 0; i < a0.count_0c; i++) {
      final ModelPart10 sp4 = a0.dobj2s_00[i];

      //LAB_800e3f48
      sp4.attribute_00 = attribute;
    }

    //LAB_800e3f9c
  }

  @Method(0x800e3facL)
  public static void FUN_800e3fac(final int a0) {
    struct258_800c66a8._00 = 0;
    struct258_800c66a8._04 = 0;
    struct258_800c66a8._05 = a0 + 1;
  }

  @Method(0x800e3ff0L)
  public static void FUN_800e3ff0() {
    if(struct258_800c66a8._05 != 0) {
      //LAB_800e4020
      _800f01fc[struct258_800c66a8._05 - 1].run();
    }

    //LAB_800e4058
  }

  @Method(0x800e406cL)
  public static void FUN_800e406c() {
    if(struct258_800c66a8._250 == 1) {
      //LAB_800e442c
      final int v0 = struct258_800c66a8._04;
      if(v0 == 1) {
        //LAB_800e4564
        struct258_800c66a8._00++;

        if(struct258_800c66a8._00 >= 15) {
          struct258_800c66a8._04 = 2;
          struct258_800c66a8._00 = 0;
        }

        //LAB_800e45c0
        //LAB_800e4464
      } else if(v0 == 2) {
        //LAB_800e45c8
        if(playerState_800c669c >= 3) {
          struct258_800c66a8._00++;

          if(struct258_800c66a8._00 >= 2) {
            mapState_800c6798._d4 = 0;
          }
        }

        //LAB_800e4624
        if(_800c66b0._c5 == 0 && mapState_800c6798._d4 == 0) {
          mapState_800c6798.disableInput_d0 = false;
          struct258_800c66a8._05 = 0;
          struct258_800c66a8._04 = 2;
        }
        //LAB_800e4478
      } else if(v0 == 0 && (worldMapState_800c6698 >= 3 || playerState_800c669c >= 3)) {
        //LAB_800e44b0
        scriptStartEffect(2, 15);

        _800c66b0._11a = 1;
        _800c66b0.coord2_20.coord.transfer.set(0, 0, 0);
        _800c66b0.angle_9a = 0;
        _800c66b0.mapRotation_70.y = 0.0f;

        FUN_800d4bc8(1);

        _800c66b0._c4 = 0;
        struct258_800c66a8.zoomState_1f8 = 0;
        struct258_800c66a8._04 = 1;
      }
    } else if(struct258_800c66a8._250 == 0 || struct258_800c66a8._250 == 2) {
      //LAB_800e40c0
      final int v0 = struct258_800c66a8._04;
      if(v0 == 1) {
        //LAB_800e4304
        struct258_800c66a8._00++;

        if(struct258_800c66a8._00 >= 15) {
          struct258_800c66a8._04 = 2;
          struct258_800c66a8._00 = 0;
        }

        //LAB_800e4360
      } else if(v0 == 2) {
        //LAB_800e4368
        if(playerState_800c669c >= 3) {
          struct258_800c66a8._00++;

          if(struct258_800c66a8._00 >= 2) {
            mapState_800c6798._d4 = 0;
          }
        }

        //LAB_800e43c4
        if(_800c66b0._c5 == 0 && mapState_800c6798._d4 == 0) {
          mapState_800c6798.disableInput_d0 = false;
          struct258_800c66a8._05 = 0;
          struct258_800c66a8._04 = 2;
        }

        //LAB_800e441c
        //LAB_800e4424
        //LAB_800e410c
        //LAB_800e42fc
      } else if(v0 == 0 && (worldMapState_800c6698 >= 3 || playerState_800c669c >= 3)) {
        //LAB_800e4144
        scriptStartEffect(2, 15);

        _800c66b0.rview2_00.viewpoint_00.y = -9000.0f;
        _800c66b0.rview2_00.refpoint_0c.y = 9000.0f;
        _800c66b0._11a = 1;
        _800c66b0.coord2_20.coord.transfer.set(0, 0, 0);
        _800c66b0._9e = -300;
        _800c66b0.angle_9a = 0;
        _800c66b0.mapRotation_70.y = 0.0f;

        FUN_800d4bc8(1);

        _800c66b0.vec_a4.setX(struct258_800c66a8.coord2_34.coord.transfer.getX() * 0x100 / 30);
        _800c66b0.vec_a4.setY(struct258_800c66a8.coord2_34.coord.transfer.getY() * 0x100 / 30);
        _800c66b0.vec_a4.setZ(struct258_800c66a8.coord2_34.coord.transfer.getZ() * 0x100 / 30);

        _800c66b0._c4 = 0;
        struct258_800c66a8.zoomState_1f8 = 0;
        _800c66b0._c5 = 2;
        struct258_800c66a8._04 = 1;
      }
    }
  }

  @Method(0x800e469cL)
  public static void FUN_800e469c() {
    if(struct258_800c66a8._04 == 0) {
      //LAB_800e46f0
      scriptStartEffect(1, 30);
      _800c66b0._110 = 1;
      _800c66b0._10e = 0;
      struct258_800c66a8._04 = 1;
    } else if(struct258_800c66a8._04 == 1) {
      //LAB_800e4738
      _800c66b0._110 = 2;
      _800c6794.sub(16);

      if(_800c6794.getSigned() < 0) {
        _800c6794.set(0);
      }

      //LAB_800e477c
      struct258_800c66a8._00++;
      if(struct258_800c66a8._00 >= 30) {
        struct258_800c66a8._04 = 2;
      }

      //LAB_800e47c8
      //LAB_800e46dc
    } else if(struct258_800c66a8._04 == 2) {
      //LAB_800e47d0
      struct258_800c66a8.colour_20 -= 0x40;

      if(struct258_800c66a8.colour_20 < 0) {
        struct258_800c66a8.colour_20 = 0;
      }

      //LAB_800e4820
      _800c66b0._10e++;

      if(_800c66b0._10e >= 16) {
        if(struct258_800c66a8.colour_20 == 0) {
          struct258_800c66a8._05 = 0;

          if(submapCut_80052c30.get() != 999) {
            pregameLoadingStage_800bb10c.set(7);
          } else {
            //LAB_800e48b8
            pregameLoadingStage_800bb10c.set(9);
          }

          //LAB_800e48c4
          if(struct258_800c66a8._250 == 2) {
            pregameLoadingStage_800bb10c.set(7);
            //LAB_800e48f4
          } else if(struct258_800c66a8._250 == 3) {
            pregameLoadingStage_800bb10c.set(9);
          }
        }
      }

      //LAB_800e491c
    }

    //LAB_800e4924
  }

  @Method(0x800e4934L)
  public static void renderMcq(final McqHeader mcq, final int vramOffsetX, final int vramOffsetY, final int x, final int y, final int z, final int colour) {
    int clutX = vramOffsetX + mcq.clutX_0c;
    int clutY = vramOffsetY + mcq.clutY_0e;
    final int width = mcq.screenWidth_14;
    final int height = mcq.screenHeight_16;
    int u = vramOffsetX + mcq.u_10;
    int v = vramOffsetY + mcq.v_12;
    int vramX = u & 0x3c0;
    final int vramY = v & 0x100;
    u = u * 4 & 0xfc;

    //LAB_800e4ad0
    for(int chunkX = 0; chunkX < width; chunkX += 16) {
      //LAB_800e4af0
      //LAB_800e4af4
      for(int chunkY = 0; chunkY < height; chunkY += 16) {
        //LAB_800e4b14
        GPU.queueCommand(z, new GpuCommandQuad()
          .bpp(Bpp.BITS_4)
          .translucent(Translucency.B_PLUS_F)
          .clut(clutX, clutY)
          .vramPos(vramX, vramY)
          .monochrome(colour)
          .pos(x + chunkX, y + chunkY, 16, 16)
          .uv(u, v)
        );

        v = v + 16 & 0xf0;

        if(v == 0) {
          u = u + 16 & 0xfc;

          if(u == 0) {
            vramX = vramX + 64;
          }
        }

        //LAB_800e4d18
        clutY = clutY + 1 & 0xff;

        if(clutY == 0) {
          clutX = clutX + 16;
        }

        //LAB_800e4d4c
        clutY = clutY | vramY;
      }

      //LAB_800e4d78
    }

    //LAB_800e4d90
  }

  @Method(0x800e4e1cL)
  public static void FUN_800e4e1c() {
    filesLoadedFlags_800c66b8.and(0xffff_fffeL);
    loadDrgnFile(0, 5696, WMap::FUN_800d562c);
    _800c6794.setu(0);
  }

  @Method(0x800e4e84L)
  public static void FUN_800e4e84() {
    if((filesLoadedFlags_800c66b8.get() & 0x1L) == 0) {
      return;
    }

    //LAB_800e4eac
    if(struct258_800c66a8._05 != 2) {
      _800c6794.addu(0x10L);

      if(_800c6794.getSigned() > 0x20L) {
        _800c6794.setu(0x20L);
      }
    }

    //LAB_800e4f04
    renderMcq(mcqHeader_800c6768, 320, 0, -160, -120, orderingTableSize_1f8003c8.get() - 3, (int)_800c6794.get() & 0xff);

    //LAB_800e4f50
  }

  @Method(0x800e4f60L)
  public static void FUN_800e4f60() {
    final COLOUR sp0x50 = new COLOUR();

    _800c6898 = FUN_800cd3c8(
      0,
      sp0x50,
      sp0x50,
      sp0x50,
      sp0x50,
      _800c87e8,
      _800c87ec,
      8,
      8,
      4,
      true,
      Translucency.B_MINUS_F,
      14
    );

    final COLOUR sp0x60 = new COLOUR();

    _800c689c = FUN_800cd3c8(
      0x80,
      sp0x60,
      sp0x60,
      sp0x60,
      sp0x60,
      _800c87f4,
      _800c87f8,
      4,
      4,
      2,
      true,
      Translucency.B_PLUS_F,
      13
    );
  }

  @Method(0x800e5150L)
  public static void handleMapTransitions() {
    if(_800c6690.get() != 0) {
      return;
    }

    //LAB_800e5178
    //LAB_800e5194
    if(mapState_800c6798._fc != 1) {
      FUN_800e69e8();
      return;
    }

    //LAB_800e51b8
    if(_800c66b0._c5 != 0) {
      return;
    }

    //LAB_800e51dc
    if(_800c66b0._c4 != 0) {
      return;
    }

    //LAB_800e5200
    if(struct258_800c66a8.zoomState_1f8 != 0) {
      return;
    }

    //LAB_800e5224
    if(struct258_800c66a8._220 != 0) {
      return;
    }

    //LAB_800e5248
    int sp28;
    final int sp2c;
    switch((int)_800c68a4.get()) {
      case 0:
        sp2c = -areaData_800f2248.get(mapState_800c6798._dc[0])._00.get();

        //LAB_800e52cc
        for(sp28 = 0; sp28 < mapState_800c6798.areaCount_0c && areaData_800f2248.get(sp28)._00.get() != sp2c; sp28++) {
          // intentionally empty
        }

        //LAB_800e533c
        FUN_800ea4dc(sp28);

        mapState_800c6798.facing_1c = -mapState_800c6798.facing_1c;

        FUN_800eab94(mapState_800c6798.locationIndex_10);

        mapState_800c6798.disableInput_d0 = true;
        mapState_800c6798._fc = 1;

        //LAB_800e5394
        for(int i = 0; i < 8; i++) {
          //LAB_800e53b0
          FUN_8002a3ec(i, 0);
        }

        //LAB_800e53e0
        textZ_800bdf00.set(13);
        mapState_800c6798.submapCut_c8 = locations_800f0e34.get(mapState_800c6798.locationIndex_10).submapCut_08.get();
        mapState_800c6798.submapScene_ca = locations_800f0e34.get(mapState_800c6798.locationIndex_10).submapScene_0a.get();
        _800c68a4.setu(0x1L);

        if(places_800f0234.get(locations_800f0e34.get(mapState_800c6798.locationIndex_10).placeIndex_02.get()).name_00.isNull()) {
          _800c68a4.setu(0x8L);
        }

        //LAB_800e54c4
        _800c6898._34 = 0;
        colour_800c86d0.setu(0x100L);
        _800c86d2.setu(0);
        break;

      case 1:
        filesLoadedFlags_800c66b8.and(0xffff_f7ffL);

        loadDrgnFile(0, 5655 + places_800f0234.get(locations_800f0e34.get(mapState_800c6798.locationIndex_10).placeIndex_02.get()).fileIndex_04.get(), data -> FUN_800d5768(new Tim(data), 1));
        initTextbox(7, 1, 240, 120, 14, 16);

        _800c68a4.setu(0x2L);

        playSound(0, 4, 0, 0, (short)0, (short)0);

        //LAB_800e55f0
        for(int i = 0; i < 4; i++) {
          //LAB_800e560c
          final int soundIndex = places_800f0234.get(locations_800f0e34.get(mapState_800c6798.locationIndex_10).placeIndex_02.get()).soundIndices_06.get(i).get();

          if(soundIndex > 0) {
            playSound(0xc, soundIndex, 0, 0, (short)0, (short)0);
          }

          //LAB_800e5698
        }

        //LAB_800e56b0
        break;

      case 2:
        if(FUN_8002a488(7) != 0) {
          initTextbox(6, 0, 240, 70, 13, 7);
          _800c68a4.setu(0x3L);
        }

        //LAB_800e5700
        break;

      case 3: // Trying to enter an area
        _800c6898._34 += 0x40;

        FUN_800ce4dc(_800c6898);

        if(mapState_800c6798.submapCut_c8 == 999) { // Going to a different region
          final int sp38 = mapState_800c6798.submapScene_ca >>> 4 & 0xffff;
          final int sp3c = mapState_800c6798.submapScene_ca & 0xf;

          renderCenteredShadowedText(No_Entry_800f01e4.deref(), 240, 164, TextColour.WHITE, 0);
          renderCenteredShadowedText(regions_800f01ec.get(sp38).deref(), 240, 182, TextColour.WHITE, 0);
          renderCenteredShadowedText(regions_800f01ec.get(sp3c).deref(), 240, 200, TextColour.WHITE, 0);

          if(Input.pressedThisFrame(InputAction.DPAD_UP) || Input.pressedThisFrame(InputAction.JOYSTICK_LEFT_BUTTON_UP)) {
            _800c86d2.subu(0x1L);

            if(_800c86d2.getSigned() < 0) {
              _800c86d2.setu(0x2L);
            }

            //LAB_800e5950
            playSound(0, 1, 0, 0, (short)0, (short)0);
          }

          //LAB_800e5970
          if(Input.pressedThisFrame(InputAction.DPAD_DOWN) || Input.pressedThisFrame(InputAction.JOYSTICK_LEFT_BUTTON_DOWN)) {
            _800c86d2.addu(0x1L);

            if(_800c86d2.getSigned() >= 0x3L) {
              _800c86d2.setu(0);
            }

            //LAB_800e59c0
            playSound(0, 1, 0, 0, (short)0, (short)0);
          }

          //LAB_800e59e0
          _800c689c.y_3a = (short)_800c86d2.getSigned() * 18 + 8;
        } else { // Entering a town, etc.
          //LAB_800e5a18
          renderCenteredShadowedText(No_Entry_800f01e4.deref(), 240, 170, TextColour.WHITE, 0);
          renderCenteredShadowedText(Enter_800f01e8.deref(), 240, 190, TextColour.WHITE, 0);

          // World Map Location Menu (No Entry,Enter)
          if(Input.pressedThisFrame(InputAction.DPAD_UP) || Input.pressedThisFrame(InputAction.DPAD_DOWN) ||
            Input.pressedThisFrame(InputAction.JOYSTICK_LEFT_BUTTON_UP) || Input.pressedThisFrame(InputAction.JOYSTICK_LEFT_BUTTON_DOWN)) {
            _800c86d2.xoru(0x1L);

            playSound(0, 1, 0, 0, (short)0, (short)0);
          }

          //LAB_800e5b38
          _800c689c.y_3a = (short)_800c86d2.getSigned() * 20 + 14;
        }

        //LAB_800e5b68
        FUN_800ce4dc(_800c689c);

        final int placeIndex = locations_800f0e34.get(mapState_800c6798.locationIndex_10).placeIndex_02.get();
        final IntRef width = new IntRef();
        final IntRef lines = new IntRef();
        measureText(places_800f0234.get(placeIndex).name_00.deref(), width, lines);
        renderCenteredShadowedText(places_800f0234.get(placeIndex).name_00.deref(), 240, 140 - lines.get() * 7, TextColour.WHITE, 0);

        if((filesLoadedFlags_800c66b8.get() & 0x800L) != 0) {
          final GpuCommandPoly cmd = new GpuCommandPoly(4)
            .bpp(Bpp.BITS_8)
            .clut(clutX_800ef0d8.get(), clutY_800ef0da.get())
            .vramPos(vramX_800ef0d4.get(), vramY_800ef0d6.get());

          if(gameState_800babc8._17c.get(mapState_800c6798.locationIndex_10)) {
            //LAB_800e5e98
            cmd.monochrome((int)colour_800c86d0.getSigned() / 2);
          } else {
            //LAB_800e5e18
            cmd.monochrome((int)colour_800c86d0.getSigned() * 0x30 / 0x100);
          }

          //LAB_800e5f04
          if(locations_800f0e34.get(mapState_800c6798.locationIndex_10)._10.get() != 0) {
            cmd.monochrome((int)colour_800c86d0.getSigned() / 2);
          }

          //LAB_800e5fa4
          cmd
            .pos(0,  21, -96)
            .pos(1, 141, -96)
            .pos(2,  21,  -6)
            .pos(3, 141,  -6)
            .uv( 0,   0,   0)
            .uv( 1, 119,   0)
            .uv( 2,   0,  89)
            .uv( 3, 119,  89);

          GPU.queueCommand(14, cmd);

          if(Input.pressedThisFrame(InputAction.BUTTON_WEST) && mapState_800c6798.submapCut_c8 != 999) { // Square
            playSound(0, 2, 0, 0, (short)0, (short)0);
          }

          //LAB_800e60d0
          if(Input.getButtonState(InputAction.BUTTON_WEST) && mapState_800c6798.submapCut_c8 != 999) { // Square
            colour_800c86d0.subu(0x80L);

            if(colour_800c86d0.getSigned() < 0x80L) {
              colour_800c86d0.setu(0x40L);
            }

            //LAB_800e6138
            final int services = places_800f0234.get(placeIndex).services_05.get();

            //LAB_800e619c
            int servicesCount = 0;
            for(int i = 0; i < 5; i++) {
              //LAB_800e61b8
              if((services & 0x1 << i) != 0) {
                renderCenteredShadowedText(services_800f01cc.get(i).deref(), 240, servicesCount * 16 + 30, TextColour.WHITE, 0);
                servicesCount++;
              }

              //LAB_800e6248
            }

            //LAB_800e6260
            if(servicesCount == 0) {
              renderCenteredShadowedText(_800f01e0.deref(), 240, 62, TextColour.WHITE, 0);
            }

            //LAB_800e6290
          } else {
            //LAB_800e6298
            colour_800c86d0.addu(0x40L);

            if(colour_800c86d0.getSigned() > 0x100L) {
              colour_800c86d0.setu(0x100L);
            }
          }
        }

        //LAB_800e62d4
        if(Input.pressedThisFrame(InputAction.BUTTON_SOUTH)) {
          if(_800c86d2.getSigned() == 0) {
            FUN_8002a3ec(6, 0);
            FUN_8002a3ec(7, 1);
            _800c68a4.setu(0x6L);

            playSound(0, 3, 0, 0, (short)0, (short)0);

            //LAB_800e6350
            for(int i = 0; i < 4; i++) {
              //LAB_800e636c
              final int soundIndex = places_800f0234.get(locations_800f0e34.get(mapState_800c6798.locationIndex_10).placeIndex_02.get()).soundIndices_06.get(i).get();

              if(soundIndex > 0) {
                FUN_80019c80(soundFiles_800bcf80[12], soundIndex, 1);
              }

              //LAB_800e63ec
            }

            //LAB_800e6404
          } else {
            //LAB_800e640c
            FUN_800e3fac(1);
            FUN_8002a3ec(6, 0);
            FUN_8002a3ec(7, 1);
            _800c68a4.setu(0x5L);

            playSound(0, 2, 0, 0, (short)0, (short)0);

            //LAB_800e6468
            for(int i = 0; i < 4; i++) {
              //LAB_800e6484
              final int soundIndex = places_800f0234.get(locations_800f0e34.get(mapState_800c6798.locationIndex_10).placeIndex_02.get()).soundIndices_06.get(i).get();

              if(soundIndex > 0) {
                FUN_80019c80(soundFiles_800bcf80[12], soundIndex, 1);
              }

              //LAB_800e6504
            }
          }

          //LAB_800e651c
        } else {
          //LAB_800e6524
          if(Input.pressedThisFrame(InputAction.BUTTON_EAST)) {
            playSound(0, 3, 0, 0, (short)0, (short)0);

            //LAB_800e6560
            for(int i = 0; i < 4; i++) {
              //LAB_800e657c
              final int soundIndex = places_800f0234.get(locations_800f0e34.get(mapState_800c6798.locationIndex_10).placeIndex_02.get()).soundIndices_06.get(i).get();

              if(soundIndex > 0) {
                FUN_80019c80(soundFiles_800bcf80[12], soundIndex, 1);
              }

              //LAB_800e65fc
            }

            //LAB_800e6614
            FUN_8002a3ec(6, 0);
            FUN_8002a3ec(7, 1);
            _800c68a4.setu(0x6L);
          }
        }

        //LAB_800e6640
        break;

      case 5:
        _800c6898._34 -= 0x80;

        FUN_800ce4dc(_800c6898);

        if(textboxes_800be358[6]._00 == 0 && textboxes_800be358[7]._00 == 0 && _800c6898._34 == 0) {
          _800c68a4.setu(0x9L);
        }

        //LAB_800e66cc
        break;

      case 6:
        if(mapState_800c6798.angle_c0 != 0.0f) {
          mapState_800c6798.angle_c0 = 0.0f;
          mapState_800c6798.facing_1c = 1;
        } else {
          //LAB_800e6704
          mapState_800c6798.angle_c0 = MathHelper.PI;
          mapState_800c6798.facing_1c = -1;
        }

        //LAB_800e671c
        mapState_800c6798._d4 = 1;
        _800c68a0.setu(0);
        _800c68a4.setu(0x7);

      case 7:
        _800c68a0.addu(0x1L);

        if(_800c68a0.get() > 0x3L) {
          _800c68a4.setu(0x8L);
        }

        //LAB_800e6770
        break;

      case 8:
        _800c68a4.setu(0);
        mapState_800c6798.disableInput_d0 = false;
        mapState_800c6798._d4 = 0;
        mapState_800c6798._fc = 0;
        _800c68a8.setu(0x1L);

        //LAB_800e67a8
        for(int i = 0; i < 7; i++) {
          //LAB_800e67c4
          _800c86d4[i] = 0;
        }

        //LAB_800e67f8
        break;

      case 9:
        gameState_800babc8._17c.set(mapState_800c6798.locationIndex_10, true);

        //LAB_800e6900
        if(mapState_800c6798.submapCut_c8 != 999) {
          submapCut_80052c30.set(mapState_800c6798.submapCut_c8);
          submapScene_80052c34.set(mapState_800c6798.submapScene_ca);
        } else {
          //LAB_800e693c
          submapCut_80052c30.set(locations_800f0e34.get(mapState_800c6798.locationIndex_10).submapCut_04.get());

          final int sp20;
          if(_800c86d2.getSigned() == 0x1L) {
            sp20 = mapState_800c6798.submapScene_ca >>> 4 & 0xffff;
          } else {
            //LAB_800e69a0
            sp20 = mapState_800c6798.submapScene_ca & 0xf;
          }

          //LAB_800e69b8
          index_80052c38.set(sp20);
        }

        //LAB_800e69c4
        mapState_800c6798.disableInput_d0 = false;
        break;
    }

    //LAB_800e69d4
  }

  @Method(0x800e69e8L)
  public static void FUN_800e69e8() {
    if(_800c6690.get() != 0) {
      return;
    }

    //LAB_800e6a10
    if(struct258_800c66a8.zoomState_1f8 == 4) {
      return;
    }

    //LAB_800e6a34
    if(pregameLoadingStage_800bb10c.get() == 8) {
      return;
    }

    //LAB_800e6a50
    // World Map Name Info
    if(_800c68a8.get() == 0) {
      if(Input.pressedThisFrame(InputAction.BUTTON_CENTER_2)) {
        playSound(0, 2, 0, 0, (short)0, (short)0);
        _800c68a8.setu(0x1L);

        //LAB_800e6aac
        for(int i = 0; i < 7; i++) {
          //LAB_800e6ac8
          _800c86d4[i] = 0;
        }
      }

      //LAB_800e6afc
    } else {
      //LAB_800e6b04
      if(!Input.getButtonState(InputAction.BUTTON_CENTER_2)) {
        //LAB_800e6b20
        for(int i = 0; i < 7; i++) {
          //LAB_800e6b3c
          FUN_8002a3ec(i, 0);
        }

        //LAB_800e6b6c
        _800c68a8.setu(0);
      }

      //LAB_800e6b74
      if(Input.getButtonState(InputAction.BUTTON_NORTH)) {
        //LAB_800e6b90
        for(int i = 0; i < 7; i++) {
          //LAB_800e6bac
          FUN_8002a3ec(i, 0);
        }

        //LAB_800e6bdc
        _800c68a8.setu(0);
      }
    }

    //LAB_800e6be4
    if(_800c68a8.get() == 0) {
      return;
    }

    //LAB_800e6c00
    rotateCoord2(struct258_800c66a8.tmdRendering_08.rotations_08[0], struct258_800c66a8.tmdRendering_08.coord2s_04[0]);

    final List<WMapStruct0c_2> structs = new ArrayList<>();

    //LAB_800e6c38
    final MATRIX sp0x38 = new MATRIX();
    for(int i = 0; i < _800c86cc.get(); i++) {
      //LAB_800e6c5c
      if(!places_800f0234.get(locations_800f0e34.get(_800c84c8.get(i).get()).placeIndex_02.get()).name_00.isNull()) {
        //LAB_800e6ccc
        GsGetLs(struct258_800c66a8.tmdRendering_08.coord2s_04[0], sp0x38);
        setRotTransMatrix(sp0x38);

        GTE.perspectiveTransform(_800c74b8.get(i));
        final short sx = GTE.getScreenX(2);
        final short sy = GTE.getScreenY(2);
        final int z = GTE.getScreenZ(3) >> 2;
        final short x = (short)(sx + 160);
        final short y = (short)(sy + 104);

        //LAB_800e6e24
        if(x >= -32 && x < 353) {
          //LAB_800e6e2c
          //LAB_800e6e5c
          if(y >= -32 && y < 273) {
            //LAB_800e6e64
            if(z >= 6 && z < orderingTableSize_1f8003c8.get() - 1) {
              final WMapStruct0c_2 struct = new WMapStruct0c_2();
              struct.z_00 = z;
              struct._04 = _800c84c8.get(i).get();
              struct.xy_08.set(sx, sy);
              structs.add(struct);
            }
          }
        }
      }
    }

    // Render world map place names when start is held down

    //LAB_800e6f54
    structs.sort(Comparator.comparingInt(o -> o.z_00));

    //LAB_800e6fa0
    int i;
    for(i = 0; i < Math.min(7, structs.size()); i++) {
      final WMapStruct0c_2 struct = structs.get(i);

      //LAB_800e6fec
      //LAB_800e6fec
      //LAB_800e6ff4
      final int x = struct.xy_08.getX() + 160;
      final int y = struct.xy_08.getY() + 104;
      final int place = locations_800f0e34.get(struct._04).placeIndex_02.get();

      if(!places_800f0234.get(place).name_00.isNull()) {
        //LAB_800e70f4
        final IntRef width = new IntRef();
        final IntRef lines = new IntRef();
        measureText(places_800f0234.get(place).name_00.deref(), width, lines);

        final int v0 = _800c86d4[i];
        if(v0 == 0) {
          //LAB_800e7168
          initTextbox(i, 0, x, y, width.get() - 1, lines.get() - 1);

          _800c86d4[i] = 1;

          //LAB_800e71d8
          textZ_800bdf00.set(i + 14);
          textboxes_800be358[i].chars_18 = Math.max(width.get(), 4);
          textboxes_800be358[i].lines_1a = lines.get();
          textboxes_800be358[i].width_1c = textboxes_800be358[i].chars_18 * 9 / 2;
          textboxes_800be358[i].height_1e = textboxes_800be358[i].lines_1a * 6;
          textboxes_800be358[i].x_14 = x;
          textboxes_800be358[i].y_16 = y;
          textboxes_800be358[i].z_0c = i + 14;
          _800c86d4[i] = 2;
        } else if(v0 == 1) {
          //LAB_800e71d8
          textZ_800bdf00.set(i + 14);
          textboxes_800be358[i].z_0c = i + 14;
          textboxes_800be358[i].chars_18 = Math.max(width.get(), 4);
          textboxes_800be358[i].lines_1a = lines.get();
          textboxes_800be358[i].width_1c = textboxes_800be358[i].chars_18 * 9 / 2;
          textboxes_800be358[i].height_1e = textboxes_800be358[i].lines_1a * 6;
          textboxes_800be358[i].x_14 = x;
          textboxes_800be358[i].y_16 = y;
          _800c86d4[i] = 2;
        } else if(v0 == 2) {
          //LAB_800e72e8
          textboxes_800be358[i].chars_18 = Math.max(width.get(), 4);
          textboxes_800be358[i].lines_1a = lines.get();
          textboxes_800be358[i].width_1c = textboxes_800be358[i].chars_18 * 9 / 2;
          textboxes_800be358[i].height_1e = textboxes_800be358[i].lines_1a * 6;
          textboxes_800be358[i].x_14 = x;
          textboxes_800be358[i].y_16 = y;
        }

        //LAB_800e74d8
        textZ_800bdf00.set(i + 119);
        textboxes_800be358[i].z_0c = i + 119;

        renderCenteredShadowedText(places_800f0234.get(place).name_00.deref(), x, y - lines.get() * 7 + 1, TextColour.WHITE, 0);
      }

      //LAB_800e7590
    }

    //LAB_800e75a8
    for(; i < 7; i++) {
      //LAB_800e75c4
      FUN_8002a3ec(i, 0);
      _800c86d4[i] = 0;
    }

    //LAB_800e7610
  }

  @Method(0x800e7624L)
  public static void measureText(final LodString text, final IntRef widthRef, final IntRef linesRef) {
    int lines = 1;
    int lineWidth = 0;
    int longestLineWidth = 0;

    //LAB_800e7648
    for(int charIndex = 0; text.charAt(charIndex) != 0xa0ff; charIndex++) {
      //LAB_800e7668
      if(text.charAt(charIndex) == 0xa1ff) { // New line
        lines++;

        if(longestLineWidth < lineWidth) {
          longestLineWidth = lineWidth;
        }

        //LAB_800e76c4
        lineWidth = 0;
      } else {
        //LAB_800e76d0
        lineWidth++;
      }
    }

    //LAB_800e76f8
    if(lineWidth < longestLineWidth) {
      lineWidth = longestLineWidth;
    }

    //LAB_800e771c
    widthRef.set(lineWidth);
    linesRef.set(lines);
  }

  @Method(0x800e774cL)
  public static void renderCenteredShadowedText(final LodString text, final int x, final int y, final TextColour colour, final int trim) {
    final String[] split = text.get().split("\\n");

    for(int i = 0; i < split.length; i++) {
      final LodString part = new LodString(split[i]);
      final int textWidth = textWidth(part);
      renderText(part, x - textWidth / 2, y + i * 12, colour, trim);
      renderText(part, x - textWidth / 2 + 1, y + i * 12 + 1, TextColour.BLACK, trim);
    }
  }

  @Method(0x800e7888L)
  public static void FUN_800e7888() {
    _800c6898 = null;
    _800c689c = null;
  }

  @Method(0x800e78c0L)
  public static void FUN_800e78c0() {
    //LAB_800e7940
    //LAB_800e7944
    for(int i = 0; i < 49; i++) {
      //LAB_800e7984
      if(gameState_800babc8.scriptFlags2_bc.get(_800f5a6c.get(i).packedFlag_00.get())) {
        //LAB_800e7a38
        //LAB_800e7a3c
        for(int flagIndex = 0; flagIndex < 8; flagIndex++) {
          //LAB_800e7a58
          gameState_800babc8.wmapFlags_15c.setRaw(flagIndex, _800f5a6c.get(i)._04.get(flagIndex).get());
        }
      }

      //LAB_800e7acc
    }

    //LAB_800e7ae4
    mapState_800c6798.submapCut_c4 = submapCut_80052c30.get();
    mapState_800c6798.submapScene_c6 = index_80052c38.get();

    if(mapState_800c6798.submapCut_c4 == 0 && mapState_800c6798.submapScene_c6 == 0) {
      mapState_800c6798.submapCut_c4 = 13; // Hellena
      mapState_800c6798.submapScene_c6 = 17;
    }

    //LAB_800e7b44
    //LAB_800e7b54
    boolean sp18 = false;
    int locationIndex;
    for(locationIndex = 0; locationIndex < 0x100; locationIndex++) {
      //LAB_800e7b70
      if(locations_800f0e34.get(locationIndex).submapCut_04.get() == mapState_800c6798.submapCut_c4 && locations_800f0e34.get(locationIndex).submapScene_06.get() == mapState_800c6798.submapScene_c6) {
        sp18 = true;
        break;
      }

      //LAB_800e7bc0
    }

    //LAB_800e7be8
    //LAB_800e7c18
    if(!sp18 || !gameState_800babc8.wmapFlags_15c.get(locationIndex)) {
      mapState_800c6798.submapCut_c4 = 13; // Hellena
      mapState_800c6798.submapScene_c6 = 17;
      locationIndex = 5;
    }

    //LAB_800e7cb8
    //LAB_800e7cbc
    //LAB_800e7d0c
    mapState_800c6798.locationCount_08 = locations_800f0e34.length();

    //LAB_800e7d1c
    int sp24;
    for(sp24 = 0; areaData_800f2248.get(sp24)._00.get() != 0; sp24++) {
      // intentionally empty
    }

    //LAB_800e7d64
    mapState_800c6798.areaCount_0c = sp24;

    GsInitCoordinate2(null, struct258_800c66a8.coord2_34);

    mapState_800c6798.continentIndex_00 = locations_800f0e34.get(locationIndex).continentNumber_0e.get() - 1;
    continentIndex_800bf0b0.set(mapState_800c6798.continentIndex_00);

    FUN_800ea630(locationIndex);

    mapState_800c6798._d8 = 0;

    boolean sp2c = previousEngineState_8004dd28 == EngineState.COMBAT_06 && mapState_800c6798.submapCut_c4 != 999;

    //LAB_800e7e2c
    if(mapState_800c6798.submapScene_c6 == 31 && mapState_800c6798.submapCut_c4 == 279) { // Ship (maybe when you watch the ship moving on the world map while Puler sails?)
      sp2c = true;
    }

    //LAB_800e7e5c
    //LAB_800e7e88
    if(!sp2c && !savedGameSelected_800bdc34.get() || _80052c6c.get() != 0) {
      //LAB_800e844c
      mapState_800c6798._d4 = 1;
      mapState_800c6798.disableInput_d0 = true;
    } else {
      // Transition from combat to world map (maybe also from smap?)
      mapState_800c6798.areaIndex_12 = gameState_800babc8.areaIndex_4de;
      mapState_800c6798.pathIndex_14 = gameState_800babc8.pathIndex_4d8;
      mapState_800c6798.dotIndex_16 = gameState_800babc8.dotIndex_4da;
      mapState_800c6798.dotOffset_18 = gameState_800babc8.dotOffset_4dc;
      mapState_800c6798.facing_1c = gameState_800babc8.facing_4dd;
      mapState_800c6798._d4 = 0;
      mapState_800c6798.disableInput_d0 = false;

      //LAB_800e7f00
      for(int i = 0; i < mapState_800c6798.locationCount_08; i++) {
        //LAB_800e7f24
        final int areaIndex = locations_800f0e34.get(i).areaIndex_00.get();

        if(areaIndex != -1) {
          //LAB_800e7f68
          if(FUN_800eb09c(i, -1, null) == 0) {
            //LAB_800e7f88
            if(areaIndex == mapState_800c6798.areaIndex_12) {
              locationIndex = i;
              break;
            }
          }
        }

        //LAB_800e7fb4
      }

      //LAB_800e7fcc
      mapState_800c6798.locationIndex_10 = locationIndex;
      mapState_800c6798.continentIndex_00 = locations_800f0e34.get(locationIndex).continentNumber_0e.get() - 1;
      continentIndex_800bf0b0.set(mapState_800c6798.continentIndex_00);

      final AreaData08 area = areaData_800f2248.get(mapState_800c6798.areaIndex_12);

      //LAB_800e8064
      //LAB_800e8068
      final UnboundedArrayRef<VECTOR> sp48 = pathDotPosPtrArr_800f591c.get(mapState_800c6798.pathIndex_14).deref();

      final int dx;
      final int dz;
      if(area._00.get() >= 0) {
        struct258_800c66a8.coord2_34.coord.transfer.set(sp48.get(0).getX(), sp48.get(0).getY() - 2, sp48.get(0).getZ());

        dx = sp48.get(0).getX() - sp48.get(1).getX();
        dz = sp48.get(0).getZ() - sp48.get(1).getZ();

        mapState_800c6798.angle_c0 = 0.0f;
      } else {
        //LAB_800e8190
        final int index = _800f5810.get(Math.abs(area._00.get()) - 1).get() - 1;
        dx = sp48.get(index).getX() - sp48.get(index - 1).getX();
        dz = sp48.get(index).getZ() - sp48.get(index - 1).getZ();

        struct258_800c66a8.coord2_34.coord.transfer.set(sp48.get(index).getX(), sp48.get(index).getY() - 2, sp48.get(index).getZ());

        mapState_800c6798.angle_c0 = MathHelper.PI;
      }

      //LAB_800e838c
      struct258_800c66a8.rotation_a4.set(0.0f, MathHelper.atan2(dx, dz), 0.0f);
      mapState_800c6798.previousPlayerRotation_c2 = struct258_800c66a8.rotation_a4.y;
      struct258_800c66a8.rotation_a4.y += mapState_800c6798.angle_c0;

      mapState_800c6798._f8 = 0;
      mapState_800c6798._fc = 0;
      savedGameSelected_800bdc34.set(false);
    }

    //LAB_800e8464
    if(previousEngineState_8004dd28 == EngineState.COMBAT_06 && mapState_800c6798.submapCut_c4 == 999) {
      submapCut_80052c30.set(0);
    }

    //LAB_800e8494
    mapState_800c6798.submapCut_c8 = locations_800f0e34.get(locationIndex).submapCut_08.get();
    mapState_800c6798.submapScene_ca = locations_800f0e34.get(locationIndex).submapScene_0a.get();

    final Vector3f avg = new Vector3f();
    final Vector3f playerPos = new Vector3f();
    final Vector3f nextPathPos = new Vector3f();

    getPathPositions(playerPos, nextPathPos);
    weightedAvg(4 - mapState_800c6798.dotOffset_18, mapState_800c6798.dotOffset_18, avg, playerPos, nextPathPos);

    struct258_800c66a8.coord2_34.coord.transfer.set(avg);
    struct258_800c66a8.coord2_34.coord.transfer.y.sub(2);

    if(mapState_800c6798.submapCut_c4 == 242 && mapState_800c6798.submapScene_c6 == 3) { // Donau
      if(gameState_800babc8.scriptFlags2_bc.get(0x8f)) {
        mapState_800c6798._d4 = 0;
        mapState_800c6798._d8 = 1;
        mapState_800c6798.disableInput_d0 = true;
      }

      //LAB_800e8684
      if(gameState_800babc8.scriptFlags2_bc.get(0x90)) {
        mapState_800c6798.disableInput_d0 = true;
        mapState_800c6798._d4 = 1;
        mapState_800c6798._d8 = 0;
      }
    }

    //LAB_800e8720
    struct258_800c66a8._250 = 0;
    struct258_800c66a8._254 = 0;

    //LAB_800e8770
    //LAB_800e87a0
    //LAB_800e87d0
    //LAB_800e8800
    // Zenebatos
    if(mapState_800c6798.submapCut_c4 == 528 && mapState_800c6798.submapScene_c6 == 13 || mapState_800c6798.submapCut_c4 == 528 && mapState_800c6798.submapScene_c6 == 14 || mapState_800c6798.submapCut_c4 == 528 && mapState_800c6798.submapScene_c6 == 15 || mapState_800c6798.submapCut_c4 == 540 && mapState_800c6798.submapScene_c6 == 19 || mapState_800c6798.submapCut_c4 == 572 && mapState_800c6798.submapScene_c6 == 23) {
      //LAB_800e8830
      struct258_800c66a8._250 = 1;
      //LAB_800e8848

      // Zenebatos
    } else if(mapState_800c6798.submapCut_c4 == 529 && mapState_800c6798.submapScene_c6 == 41) {
      struct258_800c66a8._250 = 2;
      struct258_800c66a8._254 = 1;
      gameState_800babc8._17c.set(mapState_800c6798.locationIndex_10, true);
    }

    //LAB_800e8990
    _800c68a4.setu(0);
    _800c68a8.setu(0);

    //LAB_800e89a4
    for(int i = 0; i < 8; i++) {
      //LAB_800e89c0
      _800c86d4[i] = 0;
    }

    //LAB_800e89f4

    FUN_800eb3c8();
  }

  @Method(0x800e8a10L)
  public static void FUN_800e8a10() {
    //LAB_800e8a38
    if(worldMapState_800c6698 >= 4 && playerState_800c669c >= 4) {
      //LAB_800e8a58
      FUN_800e8cb0();
      FUN_800e975c();
      FUN_800e9d68();
      handleMapTransitions();
      updatePlayer();
    }

    //LAB_800e8a80
  }

  @Method(0x800e8a90L)
  public static void FUN_800e8a90() {
    if(mapState_800c6798._d8 != 0) {
      mapState_800c6798.disableInput_d0 = true;

      if(struct258_800c66a8._05 != 0) {
        return;
      }

      //LAB_800e8ae0
    } else {
      //LAB_800e8ae8
      if(mapState_800c6798._d4 == 0) {
        return;
      }

      //LAB_800e8b04
      if(struct258_800c66a8.modelIndex_1e4 >= 2) {
        return;
      }
    }

    //LAB_800e8b2c
    final int sp4;
    if(areaData_800f2248.get(mapState_800c6798.areaIndex_12)._00.get() < 0) {
      sp4 = -1;
    } else {
      //LAB_800e8b64
      sp4 = 1;
    }

    //LAB_800e8b68
    int sp0;
    if(sp4 > 0) {
      sp0 = 1;
    } else {
      //LAB_800e8b8c
      sp0 = -1;
    }

    //LAB_800e8b94
    //LAB_800e8bc0
    if(sp4 < 0 && mapState_800c6798.facing_1c > 0 || sp4 > 0 && mapState_800c6798.facing_1c < 0) {
      //LAB_800e8bec
      sp0 = -sp0;
    }

    //LAB_800e8bfc
    if(mapState_800c6798._d4 == 2 || mapState_800c6798._d8 == 2) {
      //LAB_800e8c2c
      sp0 *= 2;
    }

    //LAB_800e8c40
    if(sp0 < 0) {
      mapState_800c6798.angle_c0 = MathHelper.PI;
      mapState_800c6798.facing_1c = -1;
    } else {
      //LAB_800e8c70
      mapState_800c6798.angle_c0 = 0.0f;
      mapState_800c6798.facing_1c = 1;
    }

    //LAB_800e8c84
    mapState_800c6798.dotOffset_18 += sp0;

    //LAB_800e8ca0
  }

  @Method(0x800e8cb0L)
  public static void FUN_800e8cb0() {
    if(mapState_800c6798._f8 != 0) {
      return;
    }

    //LAB_800e8cd8
    FUN_800e8a90();

    if(!mapState_800c6798.disableInput_d0) {
      processInput();
    }

    //LAB_800e8cfc
    if(mapState_800c6798.dotOffset_18 >= 4) {
      mapState_800c6798.dotIndex_16++;

      //LAB_800e8d48
      mapState_800c6798.dotOffset_18 %= 4;

      final int sp10 = _800f5810.get(Math.abs(areaData_800f2248.get(mapState_800c6798.areaIndex_12)._00.get()) - 1).get() - 1;

      if(mapState_800c6798.dotIndex_16 >= sp10) {
        mapState_800c6798.dotIndex_16 = sp10 - 1;
        mapState_800c6798.dotOffset_18 = 3;
        mapState_800c6798._f8 = 2;
      }

      //LAB_800e8dfc
      //LAB_800e8e04
    } else if(mapState_800c6798.dotOffset_18 < 0) {
      mapState_800c6798.dotIndex_16--;
      mapState_800c6798.dotOffset_18 += 4;

      if(mapState_800c6798.dotIndex_16 < 0) {
        mapState_800c6798.dotIndex_16 = 0;
        mapState_800c6798.dotOffset_18 = 0;
        mapState_800c6798._f8 = 1;
      }
    }

    //LAB_800e8e78
    FUN_800e8e94();

    //LAB_800e8e80
  }

  @Method(0x800e8e94L)
  public static void FUN_800e8e94() {
    if(gameState_800babc8.scriptFlags2_bc.get(0x97)) {
      //LAB_800e8f24
      if(struct258_800c66a8.modelIndex_1e4 == 1) {
        //LAB_800e8f48
        if(mapState_800c6798._d8 == 0) {
          //LAB_800e8f64
          if(struct258_800c66a8._05 != 2) {
            //LAB_800e8f88
            if(struct258_800c66a8._05 == 0) {
              //LAB_800e8fac
              if(_800c66b0._110 == 0) {
                //LAB_800e8fd0
                if(struct258_800c66a8.zoomState_1f8 == 0) {
                  //LAB_800e8ff4
                  if(_800c66b0._c5 == 0) {
                    //LAB_800e9018
                    if(_800c66b0._c4 == 0) {
                      //LAB_800e903c
                      if((filesLoadedFlags_800c66b8.get() & 0x1L) != 0) {
                        //LAB_800e905c
                        if(_800c6690.get() == 0) {
                          //LAB_800e9078
                          if(Input.pressedThisFrame(InputAction.BUTTON_WEST)) { // Square
                            if(mapState_800c6798._fc != 1) {
                              mapState_800c6798.submapCut_c8 = locations_800f0e34.get(93).submapCut_08.get();
                              mapState_800c6798.submapScene_ca = locations_800f0e34.get(93).submapScene_0a.get();
                              submapCut_80052c30.set(mapState_800c6798.submapCut_c8);
                              submapScene_80052c34.set(mapState_800c6798.submapScene_ca);
                              FUN_800e3fac(1);
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }

    //LAB_800e90f0
  }

  @Method(0x800e9104L)
  public static void processInput() {
    //LAB_800e912c
    if(struct258_800c66a8._05 != 0) {
      return;
    }

    //LAB_800e9150
    if(struct258_800c66a8.modelIndex_1e4 >= 2) {
      return;
    }

    //LAB_800e9178
    if(worldMapState_800c6698 != 5) {
      return;
    }

    //LAB_800e9194
    if(playerState_800c669c != 5) {
      return;
    }

    //LAB_800e91b0
    if(mapState_800c6798._d8 != 0) {
      return;
    }

    //LAB_800e91cc
    final int directionInput = (input_800bee90.get() & 0xffff) >>> 12;

    if(directionInput != 0) {
      final int sp0 = MathHelper.radToPsxDeg(MathHelper.floorMod(_800c66b0.mapRotation_70.y - mapState_800c6798.previousPlayerRotation_c2 - 0.875f * MathHelper.PI, MathHelper.TWO_PI)) >> 9;
      final int sp8 = directionInput & _800f0204.get(sp0).get();
      final int spc = directionInput & _800f0210.get(sp0).get();

      int movement;
      if(sp8 != 0 && spc == 0) {
        movement = 1;
        //LAB_800e92d0
      } else if(sp8 == 0 && spc != 0) {
        movement = -1;
      } else {
        //LAB_800e9300
        movement = mapState_800c6798.facing_1c;
      }

      //LAB_800e9330
      //LAB_800e9364
      if(Input.getButtonState(InputAction.BUTTON_EAST) || analogMagnitude_800beeb4.get() >= 0x7f) {
        //LAB_800e9384
        movement *= 2; // Running
      }

      if(movement != 0) {
        //LAB_800e9398
        mapState_800c6798.dotOffset_18 += movement;

        if(movement < 0) {
          mapState_800c6798.angle_c0 = MathHelper.PI;
          mapState_800c6798.facing_1c = -1;
        } else {
          //LAB_800e93f4
          mapState_800c6798.angle_c0 = 0.0f;
          mapState_800c6798.facing_1c = 1;
        }
      }
    }

    //LAB_800e9408
  }

  @Method(0x800e9418L)
  public static void getPathPositions(final Vector3f playerPos, final Vector3f nextPathPos) {
    final UnboundedArrayRef<VECTOR> dots = pathDotPosPtrArr_800f591c.get(mapState_800c6798.pathIndex_14).deref();
    dots.get(mapState_800c6798.dotIndex_16).get(playerPos);
    dots.get(mapState_800c6798.dotIndex_16 + 1).get(nextPathPos);
  }

  @Method(0x800e94f0L)
  public static void weightedAvg(final int weight1, final int weight2, final Vector3f out, final Vector3f vec1, final Vector3f vec2) {
    out.x = (weight1 * vec1.x + weight2 * vec2.x) / (weight1 + weight2);
    out.y = (weight1 * vec1.y + weight2 * vec2.y) / (weight1 + weight2);
    out.z = (weight1 * vec1.z + weight2 * vec2.z) / (weight1 + weight2);
  }

  @Method(0x800e9648L)
  public static void updatePlayerRotation() {
    final WMapStruct258 struct258 = struct258_800c66a8;
    struct258.rotation_a4.set(0.0f, MathHelper.atan2(mapState_800c6798.playerPos_20.x - mapState_800c6798.nextDotPos_30.x, mapState_800c6798.playerPos_20.z - mapState_800c6798.nextDotPos_30.z), 0.0f);
    mapState_800c6798.previousPlayerRotation_c2 = struct258.rotation_a4.y;
    struct258.rotation_a4.y += mapState_800c6798.angle_c0;
  }

  @Method(0x800e975cL)
  public static void FUN_800e975c() {
    if(mapState_800c6798._f8 == 0) {
      return;
    }

    //LAB_800e9784
    //LAB_800e9788
    for(int i = 0; i < 7; i++) {
      //LAB_800e97a4
      mapState_800c6798._dc[i] = -1;
    }

    final Vector3f playerPos = new Vector3f();
    final Vector3f nextPathPos = new Vector3f();
    final Vector3f pos = new Vector3f();

    //LAB_800e97dc
    getPathPositions(playerPos, nextPathPos);

    if(mapState_800c6798._f8 == 1) {
      pos.set(playerPos);
    } else {
      //LAB_800e9834
      pos.set(nextPathPos);
    }

    //LAB_800e985c
    int sp4c = 0;

    //LAB_800e9864
    for(int i = 0; i < mapState_800c6798.locationCount_08; i++) {
      //LAB_800e9888
      if(FUN_800eb09c(i, 0, null) == 0) {
        //LAB_800e98a8
        if(locations_800f0e34.get(i)._0c.get() != -1) {
          //LAB_800e98e0
          final int sp54 = locations_800f0e34.get(i).areaIndex_00.get();
          final int sp50 = areaData_800f2248.get(sp54)._00.get();

          if(mapState_800c6798.facing_1c <= 0 || sp50 >= 0) {
            //LAB_800e995c
            if(mapState_800c6798.facing_1c >= 0 || sp50 <= 0) {
              //LAB_800e9988
              final int pathIndex = Math.abs(sp50) - 1;
              final int dotIndex = _800f5810.get(pathIndex).get();
              final UnboundedArrayRef<VECTOR> dots = pathDotPosPtrArr_800f591c.get(pathIndex).deref();
              dots.get(dotIndex - 1).get(playerPos);
              dots.get(0).get(nextPathPos);

              if(pos.x == playerPos.x && pos.y == playerPos.y && pos.z == playerPos.z) {
                mapState_800c6798._40[sp4c].set(dots.get(dotIndex - 2));
                mapState_800c6798._dc[sp4c] = sp54;
                sp4c++;
                //LAB_800e9bd8
              } else if(pos.x == nextPathPos.x && pos.y == nextPathPos.y && pos.z == nextPathPos.z) {
                mapState_800c6798._40[sp4c].set(dots.get(1));
                mapState_800c6798._dc[sp4c] = sp54;
                sp4c++;
              }
            }
          }
        }
      }

      //LAB_800e9ce0
    }

    //LAB_800e9cf8
    mapState_800c6798._b0.set(pos);
    mapState_800c6798._f8 = 0;

    if(sp4c == 1) {
      mapState_800c6798._fc = 1;
    } else {
      //LAB_800e9d48
      mapState_800c6798._fc = 2;
    }

    //LAB_800e9d54
  }

  @Method(0x800e9d68L)
  public static void FUN_800e9d68() {
    final VECTOR sp0xb0 = new VECTOR();
    final short[] sp0xc8 = new short[7];

    int sp18 = 0;
    boolean sp28 = false;
    int spda = 0x1000;

    if(mapState_800c6798._fc != 2) {
      return;
    }

    //LAB_800e9da0
    if(mapState_800c6798._d8 != 0) {
      if(mapState_800c6798._d8 < 3) {
        FUN_800e3fac(1);
        submapCut_80052c30.set(285); // I think this is a Queen Fury cut
        submapScene_80052c34.set(32);
        mapState_800c6798._d8 = 3;
      }

      //LAB_800e9dfc
      return;
    }

    //LAB_800e9e04
    if(mapState_800c6798.disableInput_d0) {
      return;
    }

    //LAB_800e9e20
    final int movementInput = (input_800bee90.get() & 0xffff) >>> 12;

    //LAB_800e9e90
    for(int i = 0; i < 7; i++) {
      //LAB_800e9eac
      if(mapState_800c6798._dc[i] < 0) {
        break;
      }

      //LAB_800e9edc
      sp0xb0.set(struct258_800c66a8.vec_94).sub(mapState_800c6798._40[i]);

      sp0xc8[i] = (short)(MathHelper.radToPsxDeg(_800c66b0.mapRotation_70.y) - ratan2(sp0xb0.getX(), sp0xb0.getZ()) + 0x800 & 0xfff);

      final int v0 = (sp0xc8[i] + 0x100 & 0xfff) >> 9;
      if((movementInput & _800f0204.get(v0).get()) != 0) {
        final int spd8 = spda;
        spda = (int)Math.abs(sp0xc8[i] - _800f021c.offset((movementInput - 1) * 0x2L).getSigned());
        final int sp14 = Math.abs(sp0xc8[i] - (int)_800f021c.offset((movementInput - 1) * 0x2L).getSigned() - 0x1000);

        if(sp14 < spda) {
          spda = sp14;
        }

        //LAB_800ea118
        if(spd8 >= spda) {
          sp18 = i;
        }

        //LAB_800ea13c
        sp28 = true;
      }

      //LAB_800ea144
    }

    //LAB_800ea15c
    if(!sp28) {
      return;
    }

    //LAB_800ea174
    mapState_800c6798._fc = 0;

    FUN_800ea4dc(mapState_800c6798._dc[sp18]);

    final AreaData08 area = areaData_800f2248.get(mapState_800c6798.areaIndex_12);

    //LAB_800ea1dc
    final UnboundedArrayRef<VECTOR> dots = pathDotPosPtrArr_800f591c.get(mapState_800c6798.pathIndex_14).deref();

    final VECTOR dot;
    if(area._00.get() >= 0) {
      dot = dots.get(0);
    } else {
      //LAB_800ea248
      dot = dots.get(_800f5810.get(mapState_800c6798.pathIndex_14).get() - 1);
    }

    //LAB_800ea2a8
    if(mapState_800c6798._b0.x != dot.getX() || mapState_800c6798._b0.y != dot.getY() || mapState_800c6798._b0.z != dot.getZ()) {
      //LAB_800ea2f8
      if(area._00.get() >= 0) {
        mapState_800c6798.dotIndex_16 = (short)(_800f5810.get(Math.abs(area._00.get()) - 1).get() - 2);
        mapState_800c6798.dotOffset_18 = 2;
        mapState_800c6798.facing_1c = -1;
        mapState_800c6798.angle_c0 = MathHelper.PI;
      } else {
        //LAB_800ea39c
        mapState_800c6798.dotIndex_16 = 0;
        mapState_800c6798.dotOffset_18 = 1;
        mapState_800c6798.facing_1c = 1;
        mapState_800c6798.angle_c0 = 0.0f;
      }
    }

    //LAB_800ea3c4
  }

  @Method(0x800ea3d8L)
  public static void updatePlayer() {
    final Vector3f playerPos = new Vector3f();
    final Vector3f nextDotPos = new Vector3f();

    getPathPositions(playerPos, nextDotPos);
    weightedAvg(4 - mapState_800c6798.dotOffset_18, mapState_800c6798.dotOffset_18, struct258_800c66a8.vec_94, playerPos, nextDotPos);
    struct258_800c66a8.vec_94.y -= 2.0f;
    mapState_800c6798.playerPos_20.set(playerPos);
    mapState_800c6798.nextDotPos_30.set(nextDotPos);

    updatePlayerRotation();
  }

  @Method(0x800ea4dcL)
  public static void FUN_800ea4dc(final int areaIndex) {
    mapState_800c6798.areaIndex_12 = areaIndex;

    //LAB_800ea4fc
    int i;
    for(i = 0; i < mapState_800c6798.locationCount_08; i++) {
      //LAB_800ea520
      if(locations_800f0e34.get(i).areaIndex_00.get() != -1) {
        //LAB_800ea558
        if(FUN_800eb09c(i, 0, null) == 0) {
          //LAB_800ea578
          if(locations_800f0e34.get(i).continentNumber_0e.get() == mapState_800c6798.continentIndex_00 + 1) {
            //LAB_800ea5bc
            if(locations_800f0e34.get(i).areaIndex_00.get() == areaIndex) {
              break;
            }
          }
        }
      }

      //LAB_800ea5f8
    }

    //LAB_800ea610
    FUN_800ea630(i);
  }

  @Method(0x800ea630L)
  public static void FUN_800ea630(final int locationIndex) {
    if(locations_800f0e34.get(locationIndex).areaIndex_00.get() == -1) {
      return;
    }

    //LAB_800ea678
    if(locations_800f0e34.get(locationIndex).continentNumber_0e.get() != mapState_800c6798.continentIndex_00 + 1) {
      return;
    }

    //LAB_800ea6bc
    if(FUN_800eb09c(locationIndex, 0, null) != 0) {
      return;
    }

    //LAB_800ea6dc
    mapState_800c6798.locationIndex_10 = locationIndex;
    mapState_800c6798.areaIndex_12 = locations_800f0e34.get(locationIndex).areaIndex_00.get();

    final AreaData08 area = areaData_800f2248.get(mapState_800c6798.areaIndex_12);
    mapState_800c6798.pathIndex_14 = Math.abs(area._00.get()) - 1; // Transition to a different path

    //LAB_800ea790
    final WMapStruct258 struct258 = struct258_800c66a8;
    final UnboundedArrayRef<VECTOR> dots = pathDotPosPtrArr_800f591c.get(mapState_800c6798.pathIndex_14).deref();

    final int dx;
    final int dz;
    if(area._00.get() >= 0) {
      struct258.coord2_34.coord.transfer.set(dots.get(0).getX(), dots.get(0).getY() - 2, dots.get(0).getZ());

      dx = dots.get(0).getX() - dots.get(1).getX();
      dz = dots.get(0).getZ() - dots.get(1).getZ();

      mapState_800c6798.dotIndex_16 = 0;
      mapState_800c6798.dotOffset_18 = 0;
      mapState_800c6798.angle_c0 = 0.0f;
      mapState_800c6798.facing_1c = 1;
    } else {
      //LAB_800ea8d4
      final int dotIndex = _800f5810.get(Math.abs(area._00.get()) - 1).get() - 1;
      dx = dots.get(dotIndex).getX() - dots.get(dotIndex - 1).getX();
      dz = dots.get(dotIndex).getZ() - dots.get(dotIndex - 1).getZ();

      struct258.coord2_34.coord.transfer.set(dots.get(dotIndex).getX(), dots.get(dotIndex).getY() - 2, dots.get(dotIndex).getZ());

      mapState_800c6798.dotIndex_16 = dotIndex - 1;
      mapState_800c6798.dotOffset_18 = 3;
      mapState_800c6798.angle_c0 = MathHelper.PI;
      mapState_800c6798.facing_1c = -1;
    }

    //LAB_800eaafc
    struct258.rotation_a4.set(0.0f, MathHelper.atan2(dx, dz), 0.0f);

    mapState_800c6798.previousPlayerRotation_c2 = struct258.rotation_a4.y;
    mapState_800c6798._f8 = 0;
    mapState_800c6798._fc = 0;

    //LAB_800eab80
  }

  @Method(0x800eab94L)
  public static void FUN_800eab94(final int locationIndex) {
    if(locations_800f0e34.get(locationIndex).areaIndex_00.get() == -1) {
      return;
    }

    //LAB_800eabdc
    if(locations_800f0e34.get(locationIndex).continentNumber_0e.get() != mapState_800c6798.continentIndex_00 + 1) {
      return;
    }

    //LAB_800eac20
    if(FUN_800eb09c(locationIndex, 0, null) != 0) {
      return;
    }

    //LAB_800eac40
    mapState_800c6798.locationIndex_10 = locationIndex;
    mapState_800c6798.areaIndex_12 = locations_800f0e34.get(locationIndex).areaIndex_00.get();

    final AreaData08 areaData = areaData_800f2248.get(mapState_800c6798.areaIndex_12);
    mapState_800c6798.pathIndex_14 = Math.abs(areaData._00.get()) - 1;

    final WMapStruct258 struct258 = struct258_800c66a8;
    final UnboundedArrayRef<VECTOR> dots = pathDotPosPtrArr_800f591c.get(mapState_800c6798.pathIndex_14).deref();

    final int dx;
    final int dz;
    if(mapState_800c6798.facing_1c > 0) {
      final int dotIndex = _800f5810.get(Math.abs(areaData._00.get()) - 1).get() - 1;
      struct258.coord2_34.coord.transfer.set(dots.get(dotIndex).getX(), dots.get(dotIndex).getY() - 2, dots.get(dotIndex).getZ());
      mapState_800c6798.dotIndex_16 = dotIndex - 1;
      mapState_800c6798.dotOffset_18 = 3;
      mapState_800c6798.angle_c0 = 0.0f;
      dx = dots.get(dotIndex).getX() - dots.get(dotIndex - 1).getX();
      dz = dots.get(dotIndex).getZ() - dots.get(dotIndex - 1).getZ();
    } else {
      //LAB_800eaf14
      struct258.coord2_34.coord.transfer.set(dots.get(0).getX(), dots.get(0).getY() - 2, dots.get(0).getZ());
      mapState_800c6798.dotIndex_16 = 0;
      mapState_800c6798.dotOffset_18 = 0;
      mapState_800c6798.angle_c0 = MathHelper.PI;
      dx = dots.get(0).getX() - dots.get(1).getX();
      dz = dots.get(0).getZ() - dots.get(1).getZ();
    }

    //LAB_800eb00c
    struct258.rotation_a4.set(0.0f, MathHelper.atan2(dx, dz), 0.0f);
    mapState_800c6798.previousPlayerRotation_c2 = struct258.rotation_a4.y;
    mapState_800c6798._fc = 0;

    //LAB_800eb088
  }

  /**
   * a1 used to be either 0, -1, or a VECTOR. If passing a VECTOR, pass it as vec and set a1 to 1
   */
  @Method(0x800eb09cL)
  public static int FUN_800eb09c(final int locationIndex, final int a1, @Nullable final Vector3f vec) {
    if(locations_800f0e34.get(locationIndex).areaIndex_00.get() == -1) {
      return -1;
    }

    //LAB_800eb0ec
    if(a1 != -1) {
      if(locations_800f0e34.get(locationIndex).continentNumber_0e.get() != mapState_800c6798.continentIndex_00 + 1) {
        return -2;
      }
    }

    //LAB_800eb144
    if(!gameState_800babc8.wmapFlags_15c.get(locationIndex)) {
      return 1;
    }

    //LAB_800eb1d0
    if(a1 == 0 || a1 == -1) {
      //LAB_800eb1f8
      return 0;
    }

    //LAB_800eb204
    final int sp14 = areaData_800f2248.get(locations_800f0e34.get(locationIndex).areaIndex_00.get())._00.get();

    if(sp14 == 0) {
      return -3;
    }

    //LAB_800eb264
    final int sp18 = Math.abs(sp14) - 1;
    final UnboundedArrayRef<VECTOR> v1 = pathDotPosPtrArr_800f591c.get(sp18).deref();

    if(sp14 > 0) {
      v1.get(0).get(vec);
    } else {
      //LAB_800eb2fc
      v1.get(_800f5810.get(sp18).get() - 1).get(vec);
    }

    //LAB_800eb3a8
    //LAB_800eb3b4
    return 0;
  }

  @Method(0x800eb3c8L)
  public static void FUN_800eb3c8() {
    final boolean[] sp0xd0 = new boolean[0x101];
    int sp24 = 0;

    final Vector3f sp0x30 = new Vector3f();
    final Vector3f sp0x40 = new Vector3f();
    final Vector3f sp0x50 = new Vector3f();
    final Vector3f[] sp0x60 = new Vector3f[0x101];

    for(int i = 0; i < sp0x60.length; i++) {
      sp0x60[i] = new Vector3f();
    }

    //LAB_800eb420
    //LAB_800eb424
    for(int i = 0; i < mapState_800c6798.locationCount_08; i++) {
      //LAB_800eb448
      if(FUN_800eb09c(i, 0, null) == 0) {
        //LAB_800eb468
        if(!sp0xd0[i]) {
          //LAB_800eb48c
          final int placeIndex0 = locations_800f0e34.get(i).placeIndex_02.get();
          int sp20 = 0;

          //LAB_800eb4c8
          for(int sp1c = i; sp1c < mapState_800c6798.locationCount_08; sp1c++) {
            //LAB_800eb4ec
            if(FUN_800eb09c(sp1c, 0, null) == 0) {
              //LAB_800eb50c
              if(!sp0xd0[sp1c]) {
                //LAB_800eb530
                final int placeIndex1 = locations_800f0e34.get(sp1c).placeIndex_02.get();

                if(!places_800f0234.get(placeIndex0).name_00.isNull() || !places_800f0234.get(placeIndex1).name_00.isNull()) {
                  // Added this check since these pointers can be null
                  if(!places_800f0234.get(placeIndex0).name_00.isNull() && !places_800f0234.get(placeIndex1).name_00.isNull()) {
                    //LAB_800eb5d8
                    if(strcmp(places_800f0234.get(placeIndex0).name_00.deref().get(), places_800f0234.get(placeIndex1).name_00.deref().get()) == 0) {
                      FUN_800eb09c(sp1c, 1, sp0x60[sp20]);

                      sp20++;
                      sp0xd0[sp1c] = true;
                    }
                  }
                } else {
                  sp0xd0[sp1c] = true;
                }
              }
            }

            //LAB_800eb67c
          }

          //LAB_800eb694
          if(sp20 == 1) {
            _800c74b8.get(sp24).set(sp0x60[0]);
          } else {
            //LAB_800eb724
            sp0x30.set(sp0x60[0]);

            //LAB_800eb750
            for(int sp1c = 0; sp1c < sp20 - 1; sp1c++) {
              //LAB_800eb778
              sp0x40.set(sp0x60[sp1c + 1]);
              weightedAvg(1, 1, sp0x50, sp0x30, sp0x40);
              sp0x30.set(sp0x50);
            }

            //LAB_800eb828
            _800c74b8.get(sp24).set(sp0x50);
          }

          //LAB_800eb8ac
          _800c84c8.get(sp24).set((short)i);

          sp24++;
        }
      }

      //LAB_800eb8dc
    }

    //LAB_800eb8f4
    _800c86cc.set(sp24);
  }

  @Method(0x800eb914L)
  public static void FUN_800eb914() {
    _800f6598.setu((locations_800f0e34.get(mapState_800c6798.locationIndex_10)._12.get() & 0x30) >>> 4);
    _800f659c.setu(_800f6598);
    _800f65a0.setu(0);

    _800c86f8 = new Coord2AndThenSomeStruct_60[48];
    _800c86fc.setu(0);

    Arrays.setAll(_800c86f8, i -> new Coord2AndThenSomeStruct_60());

    //LAB_800eb9b8
    for(int i = 0; i < 48; i++) {
      final Coord2AndThenSomeStruct_60 struct = _800c86f8[i];

      //LAB_800eb9d4
      GsInitCoordinate2(null, struct.coord2_00);

      //LAB_800eba0c
      //LAB_800ebaa0
      struct.svec_54.setX((short)(rand() % 8 - 4));
      struct.svec_54.setY((short)(-rand() % 3 - 2));
      struct.svec_54.setZ((short)(rand() % 8 - 4));

      //LAB_800ebadc
      struct._50 = rand() % 0x80;
    }

    //LAB_800ebb18
  }

  @Method(0x800ebb2cL)
  public static void FUN_800ebb2c() {
    // No-op
  }

  @Method(0x800ebb34L)
  public static void FUN_800ebb34() {
    // No-op
  }

  @Method(0x800ebb3cL)
  public static void FUN_800ebb3c() {
    // No-op
  }

  @Method(0x800ebb44L)
  public static void FUN_800ebb44() {
    final WMapStruct258 struct = struct258_800c66a8;

    _800c86fc.setu(0x1L);
    struct._24 = new WMapStruct258Sub60[24];

    //LAB_800ebbb4
    final VECTOR sp0x20 = new VECTOR();
    for(int i = 0; i < 12; i++) {
      final WMapStruct258Sub60 sp18 = new WMapStruct258Sub60();
      struct._24[i] = sp18;

      //LAB_800ebbd0
      GsInitCoordinate2(null, sp18.coord2_00);

      if((i & 0x1) == 0) {
        sp0x20.set(
          700 - rand() % 1400,
          -70 - rand() %   40,
          700 - rand() % 1400
        );

        sp18.coord2_00.coord.transfer.set(sp0x20);
      } else {
        //LAB_800ebd18
        sp18.coord2_00.coord.transfer.set(sp0x20).sub(
          rand() % 200 - 100,
          rand() %  80 -  40,
          rand() %  50 -  25
        );
      }

      //LAB_800ebe24
      sp18.rotation_50.set((short)0, (short)0, (short)0);
      sp18._58 = (288 - rand() % 64) / 2;
      sp18._5a = ( 80 - rand() % 32) / 2;
      sp18._5c = 0;
      sp18._5e = 0;
    }

    //LAB_800ebf2c
    //LAB_800ebf30
    for(int i = 0; i < 12; i++) {
      final WMapStruct258Sub60 sp18 = new WMapStruct258Sub60();
      struct._24[i + 12] = sp18;
      sp18.set(struct._24[i]);
      sp18.coord2_00.coord.transfer.setY(0);
    }
  }

  @Method(0x800ebfc0L)
  public static void FUN_800ebfc0() {
    final WMapStruct258 struct = struct258_800c66a8;
    final WMapStruct258Sub60 sp38_0 = struct._24[0];

    rotateCoord2(sp38_0.rotation_50, sp38_0.coord2_00);

    //LAB_800ec028
    for(int i = 0; i < 24; i++) {
      final WMapStruct258Sub60 sp38 = struct._24[i];

      //LAB_800ec044
      final GpuCommandPoly cmd = new GpuCommandPoly(4)
        .bpp(Bpp.BITS_4)
        .translucent(Translucency.B_PLUS_F)
        .clut(576, 496 + i % 3)
        .vramPos(576, 256)
        .uv(0,   0, i % 3 * 64)
        .uv(1, 255, i % 3 * 64)
        .uv(2,   0, i % 3 * 64 + 63)
        .uv(3, 255, i % 3 * 64 + 63);

      sp38._5e++;

      if(sp38._5e >> i % 3 + 4 != 0) {
        sp38.coord2_00.coord.transfer.x.incr();
        sp38._5e = 0;
      }

      //LAB_800ec288
      if(sp38.coord2_00.coord.transfer.getX() > 700) {
        sp38.coord2_00.coord.transfer.setX(-700);
      }

      //LAB_800ec2b0
      if(_800c66b0._c4 == 1) {
        sp38._5c -= 0x20;

        if(sp38._5c < 0) {
          sp38._5c = 0;
        }

        //LAB_800ec30c
      } else {
        //LAB_800ec314
        if(sp38._5c < 0x60) {
          sp38._5c += 0x10;
        }

        //LAB_800ec34c
        if(struct258_800c66a8._05 != 0) {
          sp38._5c -= 0x2;

          if(sp38._5c < 0) {
            sp38._5c = 0;
          }
        }
      }

      //LAB_800ec3a8
      if(sp38._5c != 0) {
        //LAB_800ec3c8
        final MATRIX sp0x10 = new MATRIX();
        GsGetLs(sp38.coord2_00, sp0x10);
        sp0x10.identity(); // NOTE: does not clear translation
        setRotTransMatrix(sp0x10);
        GTE.perspectiveTransform(-sp38._58, -sp38._5a, 0);
        final short sx0 = GTE.getScreenX(2);
        final short sy0 = GTE.getScreenY(2);
        cmd.pos(0, sx0, sy0);
        int z = GTE.getScreenZ(3) >> 2;

        if(z >= 5 && z < orderingTableSize_1f8003c8.get() - 3) {
          //LAB_800ec534
          GTE.perspectiveTransform(sp38._58, -sp38._5a, 0);
          final short sx1 = GTE.getScreenX(2);
          final short sy1 = GTE.getScreenY(2);
          cmd.pos(1, sx1, sy1);
          z = GTE.getScreenZ(3) >> 2;

          if(z >= 5 && z < orderingTableSize_1f8003c8.get() - 3) {
            //LAB_800ec5b8
            if(sx1 - sx0 <= 0x400) {
              //LAB_800ec5ec
              GTE.perspectiveTransform(-sp38._58, sp38._5a, 0);
              final short sx2 = GTE.getScreenX(2);
              final short sy2 = GTE.getScreenY(2);
              cmd.pos(2, sx2, sy2);
              z = GTE.getScreenZ(3) >> 2;

              if(z >= 5 && z < orderingTableSize_1f8003c8.get() - 3) {
                //LAB_800ec670
                if(sy2 - sy0 <= 0x200) {
                  //LAB_800ec6a4
                  if(sy2 > 0) {
                    sp38._5c -= 0x20;

                    if(sp38._5c < 0) {
                      sp38._5c = 0;
                    }

                    //LAB_800ec6fc
                  } else {
                    //LAB_800ec704
                    if(sp38._5c < 0x60) {
                      sp38._5c += 0x10;
                    }

                    //LAB_800ec73c
                    if(struct258_800c66a8._05 != 0) {
                      sp38._5c -= 0x20;

                      if(sp38._5c < 0) {
                        sp38._5c = 0;
                      }
                    }
                  }

                  //LAB_800ec798
                  if(sp38._5c != 0) {
                    //LAB_800ec7b8
                    GTE.perspectiveTransform(sp38._58, sp38._5a, 0);
                    final short sx3 = GTE.getScreenX(2);
                    final short sy3 = GTE.getScreenY(2);
                    cmd.pos(3, sx3, sy3);
                    z = GTE.getScreenZ(3) >> 2;

                    if(z >= 5 && orderingTableSize_1f8003c8.get() - 3 < z) {
                      //LAB_800ec83c
                      if(sx3 - sx2 <= 0x400) {
                        //LAB_800ec870
                        if(sy3 - sy1 <= 0x200) {
                          //LAB_800ec8a4
                          if(i < 12) {
                            cmd.monochrome(sp38._5c);
                            GPU.queueCommand(139, cmd);
                          } else {
                            //LAB_800ec928
                            cmd.monochrome(sp38._5c / 3);
                            GPU.queueCommand(orderingTableSize_1f8003c8.get() - 4, cmd);
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }

    //LAB_800eca1c
  }

  @Method(0x800eca3cL)
  public static void FUN_800eca3c() {
    _800c86fc.setu(0x1L);
    struct258_800c66a8._24 = new WMapStruct258Sub60[64];

    //LAB_800eca94
    for(int i = 0; i < 64; i++) {
      final WMapStruct258Sub60 sp18 = new WMapStruct258Sub60();
      struct258_800c66a8._24[i] = sp18;

      //LAB_800ecab0
      GsInitCoordinate2(null, sp18.coord2_00);
      sp18.coord2_00.coord.transfer.setX(500 - rand() % 1000);
      sp18.coord2_00.coord.transfer.setY(    - rand() %  200);
      sp18.coord2_00.coord.transfer.setZ(500 - rand() % 1000);
      sp18.rotation_50.setX((short)0);
      sp18.rotation_50.setY((short)0);
      sp18.rotation_50.setZ((short)(rand() % 12));
      sp18._58 = rand() % 2 - 1;
      sp18._5a = rand() % 2 + 1;
      sp18._5c = 0;
      sp18._5e = rand() % 2 - 1;
    }

    //LAB_800eccfc
  }

  @Method(0x800ecd10L)
  public static void renderSnow() {
    final MATRIX sp0x10 = new MATRIX();
    final Vector3f sp0x80 = new Vector3f();

    //LAB_800ecdb4
    for(int i = 0; i < 64; i++) {
      final WMapStruct258Sub60 sp38 = struct258_800c66a8._24[i];

      //LAB_800ecdd0
      if(_800c66b0._c4 == 1) {
        sp38._5c -= 0x20;

        if(sp38._5c < 0) {
          sp38._5c = 0;
        }

        //LAB_800ed0c8
      } else {
        //LAB_800ed0d0
        if(sp38._5c < 0x60) {
          sp38._5c += 0x10;
        }

        //LAB_800ed108
        if(struct258_800c66a8._05 != 0) {
          sp38._5c -= 0x20;

          if(sp38._5c < 0) {
            sp38._5c = 0;
          }
        }
      }

      //LAB_800ed164
      if(sp38._5c != 0) {
        //LAB_800ed184
        sp38.coord2_00.coord.transfer.x.add(sp38._58);
        sp38.coord2_00.coord.transfer.y.add(sp38._5a);
        sp38.coord2_00.coord.transfer.z.add(sp38._5e);

        if(sp38.coord2_00.coord.transfer.getY() > 0) {
          sp38.coord2_00.coord.transfer.setX(500 - rand() % 1000);
          sp38.coord2_00.coord.transfer.setY(-200);
          sp38.coord2_00.coord.transfer.setZ(500 - rand() % 1000);
        }

        //LAB_800ed2bc
        rotateCoord2(sp0x80, sp38.coord2_00);
        GsGetLs(sp38.coord2_00, sp0x10);
        sp0x10.identity(); // NOTE: does not clear translation
        setRotTransMatrix(sp0x10);
        GTE.perspectiveTransform(-2, -2, 0);

        final short sx0 = GTE.getScreenX(2);
        final short sy0 = GTE.getScreenY(2);
        int z = GTE.getScreenZ(3) >> 2;

        if(z >= 5 && z < orderingTableSize_1f8003c8.get() - 3) {
          //LAB_800ed37c
          GTE.perspectiveTransform(2, -2, 0);

          final short sx1 = GTE.getScreenX(2);
          final short sy1 = GTE.getScreenY(2);
          z = GTE.getScreenZ(3) >> 2;

          if(z >= 5 && z < orderingTableSize_1f8003c8.get() - 3) {
            //LAB_800ed400
            if(sx1 - sx0 <= 0x400) {
              //LAB_800ed434
              GTE.perspectiveTransform(-2, 2, 0);

              final short sx2 = GTE.getScreenX(2);
              final short sy2 = GTE.getScreenY(2);
              z = GTE.getScreenZ(3) >> 2;

              if(z >= 5 && z < orderingTableSize_1f8003c8.get() - 3) {
                //LAB_800ed4b8
                if(sy2 - sy0 <= 0x200) {
                  //LAB_800ed4ec
                  GTE.perspectiveTransform(2, 2, 0);

                  final short sx3 = GTE.getScreenX(2);
                  final short sy3 = GTE.getScreenY(2);
                  z = GTE.getScreenZ(3) >> 2;

                  if(z >= 5 && z < orderingTableSize_1f8003c8.get() - 3) {
                    //LAB_800ed570
                    if(sx3 - sx2 <= 0x400) {
                      //LAB_800ed5a4
                      if(sy3 - sy1 <= 0x200) {
                        //LAB_800ed5d8
                        sp38.rotation_50.setZ((short)((sp38.rotation_50.getZ() + 1) % 12));
                        final int index = sp38.rotation_50.getZ() / 2 * 2;

                        final int u = (int)_800f65c8.offset(index).get();
                        final int v = (int)_800f65c8.offset(index + 1).get();

                        GPU.queueCommand(139, new GpuCommandPoly(4)
                          .bpp(Bpp.BITS_4)
                          .translucent(Translucency.B_PLUS_F)
                          .clut(640, 496)
                          .vramPos(640, 256)
                          .monochrome(sp38._5c)
                          .pos(0, sx0, sy0)
                          .pos(1, sx1, sy1)
                          .pos(2, sx2, sy2)
                          .pos(3, sx3, sy3)
                          .uv(0, u, v)
                          .uv(1, u + 8, v)
                          .uv(2, u, v + 8)
                          .uv(3, u + 8, v + 8)
                        );
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }

    //LAB_800ed93c
  }

  @Method(0x800ed95cL)
  public static void FUN_800ed95c() {
    if(_800c66b0._c5 == 2) {
      return;
    }

    //LAB_800ed98c
    switch((int)_800c66a4.get()) {
      case 0:
        break;

      case 2:
        if((filesLoadedFlags_800c66b8.get() & 0x1_0000L) != 0 && (filesLoadedFlags_800c66b8.get() & 0x1000L) != 0) {
          _800c66a4.setu(0x3L);
        }

        //LAB_800eda18
        break;

      case 3:
        _800f65a4[(int)_800f6598.get()].run();
        _800c66a4.setu(0x4L);
        break;

      case 4:
        if(worldMapState_800c6698 >= 3 || playerState_800c669c >= 3) {
          //LAB_800eda98
          _800c66a4.setu(0x5L);
        }

        //LAB_800edaa4
        break;

      case 5:
        _800f659c.setu(_800f6598);
        _800f6598.setu((locations_800f0e34.get(mapState_800c6798.locationIndex_10)._12.get() & 0x30) >>> 4);

        if(_800f6598.get() != _800f659c.get()) {
          _800f65bc[(int)_800f659c.get()].run();
          _800c66a4.setu(0x3L);
        } else {
          //LAB_800edb5c
          _800f65b0[(int)_800f6598.get()].run();
        }

        break;

      case 6:
        _800c66a4.setu(0x7L);
        break;
    }

    //LAB_800edba4
    renderSmoke();

    //LAB_800edbac
  }

  @Method(0x800edbc0L)
  public static void renderSmoke() {
    final Vector3f rotation = new Vector3f(); // Just (0, 0, 0)
    final MATRIX ls = new MATRIX();

    if((filesLoadedFlags_800c66b8.get() & 0x1000L) == 0) {
      return;
    }

    //LAB_800edc04
    if(_800c6690.get() != 0) {
      return;
    }

    //LAB_800edc20
    if(struct258_800c66a8.zoomState_1f8 == 4) {
      return;
    }

    //LAB_800edc44
    if(worldMapState_800c6698 < 4) {
      return;
    }

    //LAB_800edc64
    if(playerState_800c669c < 4) {
      return;
    }

    //LAB_800edc84
    //LAB_800edca8
    for(int i = 0; i < _800c86cc.get(); i++) {
      final Coord2AndThenSomeStruct_60 struct = _800c86f8[i];

      //LAB_800edccc
      if(!places_800f0234.get(locations_800f0e34.get(_800c84c8.get(i).get()).placeIndex_02.get()).name_00.isNull()) {
        //LAB_800edd3c
        final int sp18 = locations_800f0e34.get(_800c84c8.get(i).get())._12.get() & 0xc;

        if(sp18 != 0) {
          //LAB_800edda0
          if(locations_800f0e34.get(_800c84c8.get(i).get()).continentNumber_0e.get() == mapState_800c6798.continentIndex_00 + 1) {
            //LAB_800eddfc
            if(i >= 9) {
              break;
            }

            //LAB_800ede18
            //LAB_800ede1c
            for(int sp14 = 0; sp14 < 6; sp14++) {
              //LAB_800ede38
              final int size;
              if(sp18 == 8) {
                size = struct._50 / 5;
              } else {
                //LAB_800ede88
                size = struct._50 / 3;
              }

              //LAB_800edebc
              //LAB_800edf88
              struct.coord2_00.coord.transfer.setX(_800c74b8.get(i).getX() + struct.svec_54.getX() * struct._50 / 16);
              struct.coord2_00.coord.transfer.setY(_800c74b8.get(i).getY() + struct.svec_54.getY() * struct._50 / 4);
              struct.coord2_00.coord.transfer.setZ(_800c74b8.get(i).getZ() + struct.svec_54.getZ() * struct._50 / 16);

              if(mapState_800c6798.continentIndex_00 == 0) {
                if(sp18 == 4) {
                  //LAB_800ee0e4
                  struct.coord2_00.coord.transfer.setX(_800c74b8.get(i).getX() + struct.svec_54.getX() * struct._50 / 16);
                  struct.coord2_00.coord.transfer.setY(_800c74b8.get(i).getY() + struct.svec_54.getY() * struct._50 / 4);
                  struct.coord2_00.coord.transfer.setZ(_800c74b8.get(i).getZ() + struct.svec_54.getZ() * struct._50 / 16 + 80);
                  //LAB_800ee1dc
                } else if(sp18 == 8) {
                  //LAB_800ee238
                  struct.coord2_00.coord.transfer.setX(_800c74b8.get(i).getX() + struct.svec_54.getX() * struct._50 / 16 + 48);
                  struct.coord2_00.coord.transfer.setY(_800c74b8.get(i).getY() + struct.svec_54.getY() * struct._50 / 4);
                  struct.coord2_00.coord.transfer.setZ(_800c74b8.get(i).getZ() + struct.svec_54.getZ() * struct._50 / 16 + 48);
                }

                //LAB_800ee32c
                //LAB_800ee334
              } else if(mapState_800c6798.continentIndex_00 == 1) {
                if(sp18 == 4) {
                  //LAB_800ee3a4
                  struct.coord2_00.coord.transfer.setX(_800c74b8.get(i).getX() + struct.svec_54.getX() * struct._50 / 16);
                  struct.coord2_00.coord.transfer.setY(_800c74b8.get(i).getY() + struct.svec_54.getY() * struct._50 / 4 + 48);
                  struct.coord2_00.coord.transfer.setZ(_800c74b8.get(i).getZ() + struct.svec_54.getZ() * struct._50 / 16 - 100);
                  //LAB_800ee4a0
                } else if(sp18 == 8) {
                  //LAB_800ee4fc
                  struct.coord2_00.coord.transfer.setX(_800c74b8.get(i).getX() + struct.svec_54.getX() * struct._50 / 16 - 48);
                  struct.coord2_00.coord.transfer.setY(_800c74b8.get(i).getY() + struct.svec_54.getY() * struct._50 / 4);
                  struct.coord2_00.coord.transfer.setZ(_800c74b8.get(i).getZ() + struct.svec_54.getZ() * struct._50 / 16 + 32);
                }
              }

              //LAB_800ee5f0
              rotateCoord2(rotation, struct.coord2_00);
              GsGetLs(struct.coord2_00, ls);
              ls.identity(); // NOTE: does not clear translation
              setRotTransMatrix(ls);

              final GpuCommandPoly cmd = new GpuCommandPoly(4)
                .bpp(Bpp.BITS_4)
                .vramPos(640, 256);

              GTE.perspectiveTransform(-size, -size, 0);
              final short sx0 = GTE.getScreenX(2);
              final short sy0 = GTE.getScreenY(2);
              int z = GTE.getScreenZ(3) >> 2;

              cmd.pos(0, sx0, sy0);

              //LAB_800ee6cc
              if(z >= 5 || z < orderingTableSize_1f8003c8.get() - 3) {
                //LAB_800ee6d4
                GTE.perspectiveTransform(size, -size, 0);
                final short sx1 = GTE.getScreenX(2);
                final short sy1 = GTE.getScreenY(2);
                z = GTE.getScreenZ(3) >> 2;

                cmd.pos(1, sx1, sy1);

                //LAB_800ee750
                if(z >= 5 || z < orderingTableSize_1f8003c8.get() - 3) {
                  //LAB_800ee758
                  if(sx1 - sx0 <= 0x400) {
                    //LAB_800ee78c
                    GTE.perspectiveTransform(-size, size, 0);
                    final short sx2 = GTE.getScreenX(2);
                    final short sy2 = GTE.getScreenY(2);
                    z = GTE.getScreenZ(3) >> 2;

                    cmd.pos(2, sx2, sy2);

                    //LAB_800ee808
                    if(z >= 5 && z < orderingTableSize_1f8003c8.get() - 3) {
                      //LAB_800ee810
                      if(sy2 - sy0 <= 0x200) {
                        //LAB_800ee844
                        GTE.perspectiveTransform(size, size, 0);
                        final short sx3 = GTE.getScreenX(2);
                        final short sy3 = GTE.getScreenY(2);
                        z = GTE.getScreenZ(3) >> 2;

                        cmd.pos(3, sx3, sy3);

                        //LAB_800ee8c0
                        if(z >= 5 && z < orderingTableSize_1f8003c8.get() - 3) {
                          //LAB_800ee8c8
                          if(sx3 - sx2 <= 0x400) {
                            //LAB_800ee8fc
                            if(sy3 - sy1 <= 0x200) {
                              //LAB_800ee930
                              if(z >= 6 && z < orderingTableSize_1f8003c8.get() - 1) {
                                if(sp18 == 8) {
                                  cmd.translucent(Translucency.B_MINUS_F);
                                } else {
                                  //LAB_800ee998
                                  cmd.translucent(Translucency.B_PLUS_F);
                                }

                                //LAB_800ee9b0
                                //LAB_800eea34
                                final int v1_0 = struct._50 / 0x40;

                                cmd
                                  .clut(640, 505)
                                  .monochrome(0x80 - struct._50)
                                  .uv(0, (int)_800f65d4.offset(v1_0 * 0x2L).offset(0x0L).get(), (int)_800f65d4.offset(v1_0 * 0x2L).offset(0x1L).get())
                                  .uv(1, (int)_800f65d4.offset(v1_0 * 0x2L).offset(0x0L).get() + 31, (int)_800f65d4.offset(v1_0 * 0x2L).offset(0x1L).get())
                                  .uv(2, (int)_800f65d4.offset(v1_0 * 0x2L).offset(0x0L).get(), (int)_800f65d4.offset(v1_0 * 0x2L).offset(0x1L).get() + 31)
                                  .uv(3, (int)_800f65d4.offset(v1_0 * 0x2L).offset(0x0L).get() + 31, (int)_800f65d4.offset(v1_0 * 0x2L).offset(0x1L).get() + 31);

                                GPU.queueCommand(100 + z, cmd);

                                struct._50++;

                                if(struct._50 >= 0x80) {
                                  struct._50 = 0;
                                }

                                //LAB_800eeccc
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }

    //LAB_800eed1c
    //LAB_800eed28
  }

  @Method(0x800eed3cL)
  public static void FUN_800eed3c() {
    if(_800c86fc.get() != 0) {
      _800c86fc.setu(0);
    }
  }

  @Method(0x800eed90L)
  public static void FUN_800eed90() {
    if(_800c86fc.get() != 0) {
      _800c86fc.setu(0);
    }
  }

  @Method(0x800eede4L)
  public static void FUN_800eede4() {
    _800c86f8 = null;
    _800f65bc[(int)_800f6598.get()].run();
  }
}
