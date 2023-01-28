package legend.game;

import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gpu.GpuCommandQuad;
import legend.core.gpu.RECT;
import legend.core.gpu.TimHeader;
import legend.core.gte.COLOUR;
import legend.core.gte.DVECTOR;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.TmdWithId;
import legend.core.gte.VECTOR;
import legend.core.memory.Memory;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.ConsumerRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.RunnableRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.inventory.WhichMenu;
import legend.game.tim.Tim;
import legend.game.tmd.Renderer;
import legend.game.types.CoolonWarpDestination20;
import legend.game.types.Coord2AndThenSomeStruct_60;
import legend.game.types.ExtendedTmd;
import legend.game.types.GameState52c;
import legend.game.types.GsF_LIGHT;
import legend.game.types.LodString;
import legend.game.types.McqHeader;
import legend.game.types.Model124;
import legend.game.types.MrgFile;
import legend.game.types.Place0c;
import legend.game.types.TexPageY;
import legend.game.types.TmdAnimationFile;
import legend.game.types.Translucency;
import legend.game.types.WMapAreaData08;
import legend.game.types.WMapRender10;
import legend.game.types.WMapRender28;
import legend.game.types.WMapRender40;
import legend.game.types.WMapStruct0c_2;
import legend.game.types.WMapStruct14;
import legend.game.types.WMapStruct19c0;
import legend.game.types.WMapStruct258;
import legend.game.types.WMapStruct258Sub60;
import legend.game.types.WMapTmdRenderingStruct18;
import legend.game.types.WeirdTimHeader;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static legend.core.GameEngine.CPU;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.MEMORY;
import static legend.core.MemoryHelper.getBiFunctionAddress;
import static legend.game.Scus94491BpeSegment.FUN_80019c80;
import static legend.game.Scus94491BpeSegment.FUN_8001eea8;
import static legend.game.Scus94491BpeSegment.FUN_8001f708;
import static legend.game.Scus94491BpeSegment.deferReallocOrFree;
import static legend.game.Scus94491BpeSegment.free;
import static legend.game.Scus94491BpeSegment.getLoadedDrgnFiles;
import static legend.game.Scus94491BpeSegment.loadDrgnBinFile;
import static legend.game.Scus94491BpeSegment.loadDrgnDir;
import static legend.game.Scus94491BpeSegment.mallocTail;
import static legend.game.Scus94491BpeSegment.memcpy;
import static legend.game.Scus94491BpeSegment.orderingTableSize_1f8003c8;
import static legend.game.Scus94491BpeSegment.playSound;
import static legend.game.Scus94491BpeSegment.qsort;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment.setWidthAndFlags;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.unloadSoundFile;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021048;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021050;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021058;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021060;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a32c;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a3ec;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a488;
import static legend.game.Scus94491BpeSegment_8002.SquareRoot0;
import static legend.game.Scus94491BpeSegment_8002.animateModel;
import static legend.game.Scus94491BpeSegment_8002.applyModelRotationAndScale;
import static legend.game.Scus94491BpeSegment_8002.clearTextbox;
import static legend.game.Scus94491BpeSegment_8002.deallocateModel;
import static legend.game.Scus94491BpeSegment_8002.initModel;
import static legend.game.Scus94491BpeSegment_8002.loadAndRenderMenus;
import static legend.game.Scus94491BpeSegment_8002.loadModelStandardAnimation;
import static legend.game.Scus94491BpeSegment_8002.rand;
import static legend.game.Scus94491BpeSegment_8002.renderDobj2;
import static legend.game.Scus94491BpeSegment_8002.renderModel;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_8002.strcmp;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b8f0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b900;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003ea80;
import static legend.game.Scus94491BpeSegment_8003.GetTPage;
import static legend.game.Scus94491BpeSegment_8003.GsGetLs;
import static legend.game.Scus94491BpeSegment_8003.GsGetLws;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.GsSetAmbient;
import static legend.game.Scus94491BpeSegment_8003.GsSetFlatLight;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_8003.GsSetRefView2L;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_8003faf0;
import static legend.game.Scus94491BpeSegment_8003.RotTransPers4;
import static legend.game.Scus94491BpeSegment_8003.StoreImage;
import static legend.game.Scus94491BpeSegment_8003.adjustTmdPointers;
import static legend.game.Scus94491BpeSegment_8003.gpuLinkedListSetCommandTextureUnshaded;
import static legend.game.Scus94491BpeSegment_8003.gpuLinkedListSetCommandTransparency;
import static legend.game.Scus94491BpeSegment_8003.parseTimHeader;
import static legend.game.Scus94491BpeSegment_8003.perspectiveTransform;
import static legend.game.Scus94491BpeSegment_8003.perspectiveTransformTriple;
import static legend.game.Scus94491BpeSegment_8003.setLightMode;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.setRotTransMatrix;
import static legend.game.Scus94491BpeSegment_8003.updateTmdPacketIlen;
import static legend.game.Scus94491BpeSegment_8004.FUN_80040e40;
import static legend.game.Scus94491BpeSegment_8004.mainCallbackIndexOnceLoaded_8004dd24;
import static legend.game.Scus94491BpeSegment_8004.previousMainCallbackIndex_8004dd28;
import static legend.game.Scus94491BpeSegment_8004.ratan2;
import static legend.game.Scus94491BpeSegment_8005._80052c6c;
import static legend.game.Scus94491BpeSegment_8005.index_80052c38;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8005.submapScene_80052c34;
import static legend.game.Scus94491BpeSegment_8007._8007a3a8;
import static legend.game.Scus94491BpeSegment_8007.joypadInput_8007a39c;
import static legend.game.Scus94491BpeSegment_8007.joypadPress_8007a398;
import static legend.game.Scus94491BpeSegment_8007.joypadRepeat_8007a3a0;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800babc0;
import static legend.game.Scus94491BpeSegment_800b._800bb104;
import static legend.game.Scus94491BpeSegment_800b._800bdc34;
import static legend.game.Scus94491BpeSegment_800b._800bee90;
import static legend.game.Scus94491BpeSegment_800b.combatStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800b.continentIndex_800bf0b0;
import static legend.game.Scus94491BpeSegment_800b.doubleBufferFrame_800bb108;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.texPages_800bb110;
import static legend.game.Scus94491BpeSegment_800b.textZ_800bdf00;
import static legend.game.Scus94491BpeSegment_800b.textboxes_800be358;
import static legend.game.Scus94491BpeSegment_800b.tickCount_800bb0fc;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.Scus94491BpeSegment_800c.identityMatrix_800c3568;

public class WMap {
  private static final Value _800c6690 = MEMORY.ref(4, 0x800c6690L);

  private static final Value worldMapState_800c6698 = MEMORY.ref(4, 0x800c6698L);
  private static final Value playerState_800c669c = MEMORY.ref(4, 0x800c669cL);
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

  private static final WeirdTimHeader _800c66c0 = MEMORY.ref(4, 0x800c66c0L, WeirdTimHeader::new);

  private static final IntRef tempZ_800c66d8 = MEMORY.ref(4, 0x800c66d8L, IntRef::new);
  private static final Value _800c66dc = MEMORY.ref(2, 0x800c66dcL);

  private static final McqHeader mcqHeader_800c6768 = MEMORY.ref(4, 0x800c6768L, McqHeader::new);

  private static final Value _800c6794 = MEMORY.ref(2, 0x800c6794L);

  /** TODO struct */
  private static final Value _800c6798 = MEMORY.ref(4, 0x800c6798L);
  private static final Value _800c679c = MEMORY.ref(4, 0x800c679cL);
  private static final Value _800c67a0 = MEMORY.ref(4, 0x800c67a0L);
  private static final Value _800c67a4 = MEMORY.ref(4, 0x800c67a4L);
  private static final Value _800c67a8 = MEMORY.ref(2, 0x800c67a8L);
  public static final UnsignedShortRef areaIndex_800c67aa = MEMORY.ref(2, 0x800c67aaL, UnsignedShortRef::new);
  /** The section of the path that the player is on */
  public static final UnsignedShortRef pathIndex_800c67ac = MEMORY.ref(2, 0x800c67acL, UnsignedShortRef::new);
  /** The path dot the player is on */
  public static final ShortRef dotIndex_800c67ae = MEMORY.ref(2, 0x800c67aeL, ShortRef::new);
  /** The distance the player is from the dot (range: 0-3) */
  public static final ShortRef dotOffset_800c67b0 = MEMORY.ref(2, 0x800c67b0L, ShortRef::new);

  /** +1 - left, -1 - right */
  public static final IntRef facing_800c67b4 = MEMORY.ref(4, 0x800c67b4L, IntRef::new);
  /** Not the canonical player pos, just a copy (for animation purposes?) */
  private static final VECTOR playerPos_800c67b8 = MEMORY.ref(4, 0x800c67b8L, VECTOR::new);
  private static final VECTOR nextDotPos_800c67c8 = MEMORY.ref(4, 0x800c67c8L, VECTOR::new);

  private static final ArrayRef<VECTOR> _800c67d8 = MEMORY.ref(4, 0x800c67d8L, ArrayRef.of(VECTOR.class, 7, 0x10, VECTOR::new));
  private static final VECTOR _800c6848 = MEMORY.ref(4, 0x800c6848L, VECTOR::new);
  private static final Value _800c6858 = MEMORY.ref(2, 0x800c6858L);
  private static final ShortRef previousPlayerRotation_800c685a = MEMORY.ref(2, 0x800c685aL, ShortRef::new);
  private static final Value _800c685c = MEMORY.ref(2, 0x800c685cL);
  private static final Value _800c685e = MEMORY.ref(2, 0x800c685eL);
  private static final Value _800c6860 = MEMORY.ref(2, 0x800c6860L);
  private static final Value _800c6862 = MEMORY.ref(2, 0x800c6862L);

  private static final Value _800c6868 = MEMORY.ref(4, 0x800c6868L);
  private static final Value _800c686c = MEMORY.ref(4, 0x800c686cL);
  private static final Value _800c6870 = MEMORY.ref(4, 0x800c6870L);
  private static final ArrayRef<IntRef> _800c6874 = MEMORY.ref(4, 0x800c6874L, ArrayRef.of(IntRef.class, 7, 4, IntRef::new));
  private static final Value _800c6890 = MEMORY.ref(4, 0x800c6890L);
  private static final Value _800c6894 = MEMORY.ref(4, 0x800c6894L);
  private static final Pointer<WMapRender40> _800c6898 = MEMORY.ref(4, 0x800c6898L, Pointer.deferred(4, WMapRender40::new));
  private static final Pointer<WMapRender40> _800c689c = MEMORY.ref(4, 0x800c689cL, Pointer.deferred(4, WMapRender40::new));
  private static final Value _800c68a0 = MEMORY.ref(4, 0x800c68a0L);
  private static final Value _800c68a4 = MEMORY.ref(4, 0x800c68a4L);
  private static final Value _800c68a8 = MEMORY.ref(4, 0x800c68a8L);

  private static final UnboundedArrayRef<WMapStruct0c_2> _800c68ac = MEMORY.ref(4, 0x800c68acL, UnboundedArrayRef.of(0xc, WMapStruct0c_2::new));

  private static final IntRef encounterAccumulator_800c6ae8 = MEMORY.ref(4, 0x800c6ae8L, IntRef::new);

  /** TODO array of 0x10-byte things, looks like VECTORs */
  private static final Value _800c74b8 = MEMORY.ref(4, 0x800c74b8L);
  private static final Value _800c74bc = MEMORY.ref(4, 0x800c74bcL);
  private static final Value _800c74c0 = MEMORY.ref(4, 0x800c74c0L);
  // ---

  private static final Value _800c84c8 = MEMORY.ref(2, 0x800c84c8L);

  private static final Value _800c86cc = MEMORY.ref(4, 0x800c86ccL);

  private static final Value _800c86d0 = MEMORY.ref(2, 0x800c86d0L);
  private static final Value _800c86d2 = MEMORY.ref(1, 0x800c86d2L);

  private static final Value _800c86d4 = MEMORY.ref(4, 0x800c86d4L);

  private static final Value _800c86f0 = MEMORY.ref(4, 0x800c86f0L);

  private static Coord2AndThenSomeStruct_60[] _800c86f8;
  private static final Value _800c86fc = MEMORY.ref(4, 0x800c86fcL);
  private static final RECT _800c8700 = MEMORY.ref(4, 0x800c8700L, RECT::new);

  private static final Value _800c8778 = MEMORY.ref(4, 0x800c8778L);
  private static final Value _800c877c = MEMORY.ref(4, 0x800c877cL);

  private static final VECTOR _800c87d8 = MEMORY.ref(4, 0x800c87d8L, VECTOR::new);
  private static final Value _800c87e8 = MEMORY.ref(4, 0x800c87e8L);
  private static final Value _800c87ec = MEMORY.ref(4, 0x800c87ecL);

  private static final Value _800c87f4 = MEMORY.ref(4, 0x800c87f4L);
  private static final Value _800c87f8 = MEMORY.ref(4, 0x800c87f8L);

  private static final Value _800eee48 = MEMORY.ref(4, 0x800eee48L);

  /**
   * <ol start="0">
   *   <li>{@link WMap#FUN_800ccbe0}</li>
   *   <li>{@link WMap#FUN_800ccc30}</li>
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
  private static final ArrayRef<Pointer<RunnableRef>> _800ef000 = MEMORY.ref(4, 0x800ef000L, ArrayRef.of(Pointer.classFor(RunnableRef.class), 13, 4, Pointer.deferred(4, RunnableRef::new)));
  /**
   * <ol start="0">
   *   <li>{@link Scus94491BpeSegment_8003#setGp0_40}</li>
   *   <li>{@link Scus94491BpeSegment_8003#setGp0_50}</li>
   *   <li>{@link Scus94491BpeSegment_8003#setGp0_48}</li>
   *   <li>{@link Scus94491BpeSegment_8003#setGp0_58}</li>
   *   <li>{@link Scus94491BpeSegment_8003#setGp0_4c}</li>
   *   <li>{@link Scus94491BpeSegment_8003#setGp0_5c}</li>
   *   <li>{@link Scus94491BpeSegment_8003#setGp0_20}</li>
   *   <li>{@link Scus94491BpeSegment_8003#setGp0_30}</li>
   *   <li>{@link Scus94491BpeSegment_8003#setGp0_28}</li>
   *   <li>{@link Scus94491BpeSegment_8003#setGp0_38}</li>
   *   <li>{@link Scus94491BpeSegment_8003#setGp0_24}</li>
   *   <li>{@link Scus94491BpeSegment_8003#setGp0_34}</li>
   *   <li>{@link Scus94491BpeSegment_8003#setGp0_2c}</li>
   *   <li>{@link Scus94491BpeSegment_8003#setGp0_3c}</li>
   *   <li>{@link Scus94491BpeSegment_8003#setGp0_64}</li>
   *   <li>{@link Scus94491BpeSegment_8003#setGp0_74}</li>
   *   <li>{@link Scus94491BpeSegment_8003#setGp0_7c}</li>
   * </ol>
   */
  private static final ArrayRef<Pointer<ConsumerRef<Long>>> gpuPacketSetters_800ef034 = MEMORY.ref(4, 0x800ef034L, ArrayRef.of(Pointer.classFor(ConsumerRef.classFor(Long.class)), 17, 4, Pointer.deferred(4, ConsumerRef::new)));

  private static final Value _800ef0d4 = MEMORY.ref(2, 0x800ef0d4L);
  private static final Value _800ef0d6 = MEMORY.ref(2, 0x800ef0d6L);
  private static final Value _800ef0d8 = MEMORY.ref(2, 0x800ef0d8L);
  private static final Value _800ef0da = MEMORY.ref(2, 0x800ef0daL);

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
  private static final ArrayRef<VECTOR> vec_800ef1a8 = MEMORY.ref(4, 0x800ef1a8L, ArrayRef.of(VECTOR.class, 8, 0x10, VECTOR::new));
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
  private static final ArrayRef<Pointer<RunnableRef>> shadowRenderers_800ef684 = MEMORY.ref(4, 0x800ef684L, ArrayRef.of(Pointer.classFor(RunnableRef.class), 4, 4, Pointer.deferred(4, RunnableRef::new)));

  private static final Value _800ef694 = MEMORY.ref(1, 0x800ef694L);

  private static final LodString No_800effa4 = MEMORY.ref(4, 0x800effa4L, LodString::new);
  private static final LodString Yes_800effb0 = MEMORY.ref(4, 0x800effb0L, LodString::new);
  /** "Move?" */
  private static final LodString Move_800f00e8 = MEMORY.ref(4, 0x800f00e8L, LodString::new);

  private static final ArrayRef<Pointer<LodString>> services_800f01cc = MEMORY.ref(4, 0x800f01ccL, ArrayRef.of(Pointer.classFor(LodString.class), 5, 4, Pointer.deferred(4, LodString::new)));
  private static final Pointer<LodString> _800f01e0 = MEMORY.ref(4, 0x800f01e0L, Pointer.deferred(4, LodString::new));
  private static final Pointer<LodString> No_Entry_800f01e4 = MEMORY.ref(4, 0x800f01e4L, Pointer.deferred(4, LodString::new));
  private static final Pointer<LodString> Enter_800f01e8 = MEMORY.ref(4, 0x800f01e8L, Pointer.deferred(4, LodString::new));
  private static final ArrayRef<Pointer<LodString>> regions_800f01ec = MEMORY.ref(4, 0x800f01ecL, ArrayRef.of(Pointer.classFor(LodString.class), 3, 4, Pointer.deferred(4, LodString::new)));

  private static final Value _800f01fc = MEMORY.ref(4, 0x800f01fcL);

  private static final Value _800f0204 = MEMORY.ref(1, 0x800f0204L);

  private static final Value _800f0210 = MEMORY.ref(1, 0x800f0210L);

  private static final Value _800f021c = MEMORY.ref(2, 0x800f021cL);

  private static final UnboundedArrayRef<Place0c> places_800f0234 = MEMORY.ref(4, 0x800f0234L, UnboundedArrayRef.of(0xc, Place0c::new));

  private static final UnboundedArrayRef<WMapStruct14> _800f0e34 = MEMORY.ref(2, 0x800f0e34L, UnboundedArrayRef.of(0x14, WMapStruct14::new));

  private static final Value _800f1580 = MEMORY.ref(2, 0x800f1580L);
  private static final Value _800f1582 = MEMORY.ref(2, 0x800f1582L);

  public static final ArrayRef<WMapAreaData08> areaData_800f2248 = MEMORY.ref(2, 0x800f2248L, ArrayRef.of(WMapAreaData08.class, 133, 8, WMapAreaData08::new));

  private static final Value _800f5810 = MEMORY.ref(4, 0x800f5810L);

  private static final ArrayRef<Pointer<UnboundedArrayRef<VECTOR>>> pathDotPosPtrArr_800f591c = MEMORY.ref(4, 0x800f591cL, ArrayRef.of(Pointer.classFor(UnboundedArrayRef.classFor(VECTOR.class)), 66, 4, Pointer.deferred(4, UnboundedArrayRef.of(0x10, VECTOR::new))));

  /** TODO array of 0x2c-byte struct */
  private static final Value _800f5a6c = MEMORY.ref(2, 0x800f5a6cL);

  private static final Value _800f5a70 = MEMORY.ref(4, 0x800f5a70L);

  private static final Value _800f5a90 = MEMORY.ref(2, 0x800f5a90L);
  private static final Value _800f5a92 = MEMORY.ref(2, 0x800f5a92L);
  private static final Value _800f5a94 = MEMORY.ref(4, 0x800f5a94L);

  private static final Value _800f6598 = MEMORY.ref(4, 0x800f6598L);
  private static final Value _800f659c = MEMORY.ref(4, 0x800f659cL);
  private static final Value _800f65a0 = MEMORY.ref(4, 0x800f65a0L);
  /**
   * <ol start="0">
   *   <li>{{@link WMap#FUN_800ebb2c}}</li>
   *   <li>{{@link WMap#FUN_800ebb44}}</li>
   *   <li>{{@link WMap#FUN_800eca3c}}</li>
   * </ol>
   */
  private static final ArrayRef<Pointer<RunnableRef>> _800f65a4 = MEMORY.ref(4, 0x800f65a4L, ArrayRef.of(Pointer.classFor(RunnableRef.class), 3, 4, Pointer.deferred(4, RunnableRef::new)));
  /**
   * <ol start="0">
   *   <li>{{@link WMap#FUN_800ebb34}}</li>
   *   <li>{{@link WMap#FUN_800ebfc0}}</li>
   *   <li>{{@link WMap#FUN_800ecd10}}</li>
   * </ol>
   */
  private static final ArrayRef<Pointer<RunnableRef>> _800f65b0 = MEMORY.ref(4, 0x800f65b0L, ArrayRef.of(Pointer.classFor(RunnableRef.class), 3, 4, Pointer.deferred(4, RunnableRef::new)));
  /**
   * <ol start="0">
   *   <li>{{@link WMap#FUN_800ebb3c}}</li>
   *   <li>{{@link WMap#FUN_800eed3c}}</li>
   *   <li>{{@link WMap#FUN_800eed90}}</li>
   * </ol>
   */
  private static final ArrayRef<Pointer<RunnableRef>> _800f65bc = MEMORY.ref(4, 0x800f65bcL, ArrayRef.of(Pointer.classFor(RunnableRef.class), 3, 4, Pointer.deferred(4, RunnableRef::new)));

  private static final Value _800f65c8 = MEMORY.ref(1, 0x800f65c8L);

  private static final Value _800f65d4 = MEMORY.ref(1, 0x800f65d4L);

  @Method(0x800c8844L)
  public static void FUN_800c8844(final GsDOBJ2 dobj2, final long a1) {
    long primitiveCount = dobj2.tmd_08.n_primitive_14.get();
    long primitives = dobj2.tmd_08.primitives_10.getPointer();
    final long s2 = a1 & 0x7fL;

    //LAB_800c8870
    while(primitiveCount != 0) {
      final long cmd = MEMORY.ref(4, primitives).offset(0x0L).get() & 0xff04_0000L;
      final long count = MEMORY.ref(2, primitives).get();

      if(cmd == 0x3700_0000L) {
        //LAB_800c8a0c
        FUN_800c9130(primitives, count, s2);
        primitiveCount -= count;
        primitives += count * 0x24L;
        //LAB_800c8918
      } else if(cmd == 0x3a04_0000L) {
        //LAB_800c8a30
        FUN_80021060(primitives, count);
        primitiveCount -= count;
        primitives += count * 0x24L;
        //LAB_800c898c
      } else if(cmd == 0x3e00_0000L) {
        //LAB_800c8a7c
        FUN_800c9090(primitives, count, s2);
        primitiveCount -= count;
        primitives += count * 0x24L;
      } else if(cmd == 0x3d00_0000L || cmd == 0x3f00_0000L) {
        //LAB_800c8968
        //LAB_800c8aa4
        FUN_800c91bc(primitives, count, s2);
        primitiveCount -= count;

        //LAB_800c8ac8
        //LAB_800c8acc
        //LAB_800c8ad0
        primitives += count * 0x2cL;
      } else if(cmd == 0x3c00_0000L) {
        //LAB_800c8a7c
        FUN_800c9090(primitives, count, s2);
        primitiveCount -= count;
        primitives += count * 0x24L;
      } else if(cmd == 0x3804_0000L) {
        //LAB_800c8a30
        FUN_80021060(primitives, count);
        primitiveCount -= count;
        primitives += count * 0x24L;
        //LAB_800c8958
      } else if(cmd == 0x3a00_0000L) {
        //LAB_800c8a54
        FUN_80021050(primitives, count);
        primitiveCount -= count;
        primitives += count * 0x18L;
      } else if(cmd == 0x3800_0000L) {
        //LAB_800c8a54
        FUN_80021050(primitives, count);
        primitiveCount -= count;
        primitives += count * 0x18L;
      } else if(cmd == 0x3204_0000L) {
        //LAB_800c89a8
        FUN_80021058(primitives, count);
        primitiveCount -= count;
        primitives += count * 0x1cL;
        //LAB_800c88e0
      } else if(cmd == 0x3500_0000L) {
        //LAB_800c8a0c
        FUN_800c9130(primitives, count, s2);
        primitiveCount -= count;
        primitives += count * 0x24L;
        //LAB_800c8908
      } else if(cmd == 0x3600_0000L) {
        //LAB_800c89ec
        FUN_800c9004(primitives, count, s2);
        primitiveCount -= count;
        primitives += count * 0x1cL;
      } else if(cmd == 0x3400_0000L) {
        //LAB_800c89ec
        FUN_800c9004(primitives, count, s2);
        primitiveCount -= count;
        primitives += count * 0x1cL;
      } else if(cmd == 0x3004_0000L) {
        //LAB_800c89a8
        FUN_80021058(primitives, count);
        primitiveCount -= count;
        primitives += count * 0x1cL;
        //LAB_800c88d0
      } else if(cmd == 0x3200_0000L) {
        //LAB_800c89c8
        FUN_80021048(primitives, count);
        primitiveCount -= count;
        primitives += count * 0x14L;
      } else if(cmd == 0x3000_0000L) {
        //LAB_800c89c8
        FUN_80021048(primitives, count);
        primitiveCount -= count;
        primitives += count * 0x14L;
      }

      //LAB_800c8ad4
    }

    //LAB_800c8adc
  }

  @Method(0x800c8d90L)
  public static void FUN_800c8d90(final Model124 model) {
    assert false;
  }

  @Method(0x800c9004L)
  public static void FUN_800c9004(final long primitives, long count, long a2) {
    final long a3 = _800eee48.offset(a2 * 0x14L).getAddress();

    //LAB_800c9024
    a2 = 0;
    while(count != 0) {
      MEMORY.ref(4, primitives).offset(a2).offset(0x4L).and(MEMORY.ref(4, a3).offset(0x4L)).oru(MEMORY.ref(4, a3).offset(0x0L)).addu(MEMORY.ref(4, a3).offset(0x10L));
      MEMORY.ref(4, primitives).offset(a2).offset(0x8L).and(MEMORY.ref(4, a3).offset(0xcL)).oru(MEMORY.ref(4, a3).offset(0x8L)).addu(MEMORY.ref(4, a3).offset(0x10L));
      MEMORY.ref(4, primitives).offset(a2).offset(0xcL).addu(MEMORY.ref(4, a3).offset(0x10L).get());
      a2 += 0x1cL;
      count--;
    }

    //LAB_800c9088
  }

  @Method(0x800c9090L)
  public static void FUN_800c9090(final long primitives, long count, long a2) {
    final long a3 = _800eee48.offset(a2 * 0x14L).getAddress();

    //LAB_800c90b0
    a2 = 0;
    while(count != 0) {
      MEMORY.ref(4, primitives).offset(a2).offset(0x04L).and(MEMORY.ref(4, a3).offset(0x4L)).oru(MEMORY.ref(4, a3).offset(0x0L)).addu(MEMORY.ref(4, a3).offset(0x10L));
      MEMORY.ref(4, primitives).offset(a2).offset(0x08L).and(MEMORY.ref(4, a3).offset(0xcL)).oru(MEMORY.ref(4, a3).offset(0x8L)).addu(MEMORY.ref(4, a3).offset(0x10L));
      MEMORY.ref(4, primitives).offset(a2).offset(0x0cL).addu(MEMORY.ref(4, a3).offset(0x10L));
      MEMORY.ref(4, primitives).offset(a2).offset(0x10L).addu(MEMORY.ref(4, a3).offset(0x10L));
      a2 += 0x24L;
      count--;
    }

    //LAB_800c9128
  }

  @Method(0x800c9130L)
  public static void FUN_800c9130(final long primitives, long count, long a2) {
    final long a3 = _800eee48.offset(a2 * 0x14L).getAddress();

    //LAB_800c9150
    a2 = 0;
    while(count != 0) {
      MEMORY.ref(4, primitives).offset(a2).offset(0x4L).and(MEMORY.ref(4, a3).offset(0x4L)).oru(MEMORY.ref(4, a3).offset(0x0L)).addu(MEMORY.ref(4, a3).offset(0x10L));
      MEMORY.ref(4, primitives).offset(a2).offset(0x8L).and(MEMORY.ref(4, a3).offset(0xcL)).oru(MEMORY.ref(4, a3).offset(0x8L)).addu(MEMORY.ref(4, a3).offset(0x10L));
      MEMORY.ref(4, primitives).offset(a2).offset(0xcL).addu(MEMORY.ref(4, a3).offset(0x10L));
      a2 += 0x24L;
      count--;
    }

    //LAB_800c91b4
  }

  @Method(0x800c91bcL)
  public static void FUN_800c91bc(final long primitives, long count, long a2) {
    final long a3 = _800eee48.offset(a2 * 0x14L).getAddress();

    //LAB_800c91dc
    a2 = 0;
    while(count != 0) {
      MEMORY.ref(4, primitives).offset(a2).offset(0x04L).and(MEMORY.ref(4, a3).offset(0x4L)).oru(MEMORY.ref(4, a3).offset(0x0L)).addu(MEMORY.ref(4, a3).offset(0x10L));
      MEMORY.ref(4, primitives).offset(a2).offset(0x08L).and(MEMORY.ref(4, a3).offset(0xcL)).oru(MEMORY.ref(4, a3).offset(0x8L)).addu(MEMORY.ref(4, a3).offset(0x10L));
      MEMORY.ref(4, primitives).offset(a2).offset(0x0cL).addu(MEMORY.ref(4, a3).offset(0x10L));
      MEMORY.ref(4, primitives).offset(a2).offset(0x10L).addu(MEMORY.ref(4, a3).offset(0x10L));
      a2 += 0x2cL;
      count--;
    }

    //LAB_800c9254
  }

  @Method(0x800c925cL) // Renders the player
  public static void renderWmapModel(final Model124 model) {
    final int nobj = model.ObjTable_0c.nobj;

    zOffset_1f8003e8.set(model.zOffset_a0);
    tmdGp0Tpage_1f8003ec.set(model.tpage_108);

    //LAB_800c92c8
    for(int i = 0; i < nobj; i++) {
      final GsDOBJ2 dobj2 = model.ObjTable_0c.top[i];

      if((model.ui_f4 & 1L << i) == 0) {
        final MATRIX ls = new MATRIX();
        final MATRIX lw = new MATRIX();
        GsGetLws(dobj2.coord2_04, lw, ls);
        GsSetLightMatrix(lw);
        CPU.CTC2(ls.getPacked(0), 0);
        CPU.CTC2(ls.getPacked(2), 1);
        CPU.CTC2(ls.getPacked(4), 2);
        CPU.CTC2(ls.getPacked(6), 3);
        CPU.CTC2(ls.getPacked(8), 4);
        CPU.CTC2(ls.transfer.getX(), 5);
        CPU.CTC2(ls.transfer.getY(), 6);
        CPU.CTC2(ls.transfer.getZ(), 7);
        Renderer.renderDobj2(dobj2, false);
      }
    }

    //LAB_800c9354
    if(model.b_cc != 0) {
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
      if(_800bdc34.get() != 0) {
        final WMapStruct258 struct258 = struct258_800c66a8;

        //LAB_800cc7d0
        free(struct258.imageData_2c);
        free(struct258.imageData_30);

        pregameLoadingStage_800bb10c.set(gameState_800babc8.isOnWorldMap_4e4.get() != 0 ? 9 : 7);
      } else {
        //LAB_800cc804
        setWidthAndFlags(320);
        FUN_8001f708(gameState_800babc8.chapterIndex_98.get(), 0);
        pregameLoadingStage_800bb10c.set(12);
      }

      //LAB_800cc828
    }

    //LAB_800cc82c
  }

  @Method(0x800cc83cL)
  public static void FUN_800cc83c() {
    if(_800c6690.get() == 0) {
      if((joypadInput_8007a39c.get() & 0x1afL) == 0) {
        final WMapStruct19c0 v1 = _800c66b0;

        if(v1._c5 == 0) {
          if(v1._c4 == 0) {
            final WMapStruct258 a0 = struct258_800c66a8;

            if(a0.zoomState_1f8 == 0) {
              if(a0._220 == 0) {
                if(worldMapState_800c6698.get() >= 0x3L || playerState_800c669c.get() >= 0x3L) {
                  //LAB_800cc900
                  if((joypadPress_8007a398.get() & 0x10L) != 0) {
                    if(_800c6798.offset(0xfcL).get() != 0x1L) {
                      if(a0._05 == 0) {
                        if(_800c6798.offset(0xd8L).get() == 0) {
                          if(a0._250 == 0) {
                            scriptStartEffect(1, 15);
                            _800c6798.offset(0xd0L).setu(0x1L);
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

    final RECT rect = new RECT().set(_800c8700);
    long v0 = mallocTail(0x1_0000L);
    struct258_800c66a8.imageData_2c = v0;
    StoreImage(rect, v0);

    rect.set((short)320, (short)0, (short)64, (short)512);
    v0 = mallocTail(0x1_0000L);
    struct258_800c66a8.imageData_30 = v0;
    StoreImage(rect, v0);

    //LAB_800cca5c
  }

  @Method(0x800cca74L)
  public static void FUN_800cca74() {
    final WMapStruct258 struct = struct258_800c66a8;
    vsyncMode_8007a3b8.set(3);
    scriptStartEffect(2, 15);
    LoadImage(_800c8700, struct.imageData_2c);
    free(struct.imageData_2c);
    LoadImage(new RECT().set((short)320, (short)0, (short)64, (short)512), struct.imageData_30);
    free(struct.imageData_30);
    FUN_800d1914();

    if(struct.zoomState_1f8 == 0) {
      _800c6868.setu(0);
    }

    //LAB_800ccb6c
    _800c6690.setu(0);
    setProjectionPlaneDistance(1100);
    pregameLoadingStage_800bb10c.set(3);
  }

  @Method(0x800ccb98L)
  public static void FUN_800ccb98() {
    _800ef000.get(pregameLoadingStage_800bb10c.get()).deref().run();
  }

  @Method(0x800ccbd8L)
  public static void FUN_800ccbd8() {
    // no-op
  }

  @Method(0x800ccbe0L)
  public static void FUN_800ccbe0() {
    setWidthAndFlags(320);
    vsyncMode_8007a3b8.set(3);
    unloadSoundFile((int)0x9L);
    FUN_8001f708(gameState_800babc8.chapterIndex_98.get(), 0);
    pregameLoadingStage_800bb10c.set(1);
  }

  @Method(0x800ccc30L)
  public static void FUN_800ccc30() {
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
      gameState_800babc8.scriptFlags1_13c.get(i).set(0);
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
    final long v0 = _800c6798.getAddress();
    final GameState52c state = gameState_800babc8;
    state.areaIndex_4de.set((int)MEMORY.ref(2, v0).offset(0x12L).get());
    state.pathIndex_4d8.set((int)MEMORY.ref(2, v0).offset(0x14L).get());
    state.dotIndex_4da.set((int)MEMORY.ref(2, v0).offset(0x16L).get());
    state.dotOffset_4dc.set((int)MEMORY.ref(1, v0).offset(0x18L).get());
    state.facing_4dd.set((int)MEMORY.ref(1, v0).offset(0x1cL).get());

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
    gameState_800babc8.areaIndex_4de.set(areaIndex_800c67aa.get());
    gameState_800babc8.pathIndex_4d8.set(pathIndex_800c67ac.get());
    gameState_800babc8.dotIndex_4da.set(dotIndex_800c67ae.get());
    gameState_800babc8.dotOffset_4dc.set(dotOffset_800c67b0.get());
    gameState_800babc8.facing_4dd.set(facing_800c67b4.get());

    FUN_800cd278();

    _80052c6c.setu(0);
    mainCallbackIndexOnceLoaded_8004dd24.set(5);
    pregameLoadingStage_800bb10c.set(0);
    vsyncMode_8007a3b8.set(2);
  }

  @Method(0x800cce1cL)
  public static void transitionToCombat() {
    gameState_800babc8.areaIndex_4de.set(areaIndex_800c67aa.get());
    gameState_800babc8.pathIndex_4d8.set(pathIndex_800c67ac.get());
    gameState_800babc8.dotIndex_4da.set(dotIndex_800c67ae.get());
    gameState_800babc8.dotOffset_4dc.set(dotOffset_800c67b0.get());
    gameState_800babc8.facing_4dd.set(facing_800c67b4.get());

    FUN_800cd030();
    FUN_800cd278();

    _80052c6c.setu(0);
    mainCallbackIndexOnceLoaded_8004dd24.set(6);
    pregameLoadingStage_800bb10c.set(0);
    vsyncMode_8007a3b8.set(2);
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
    worldMapState_800c6698.setu(0x2L);
    playerState_800c669c.setu(0x2L);
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
    FUN_800d6880();
    FUN_800d177c();
    FUN_800d8d18();
    FUN_800dfa70();
    FUN_800e4f60();
    FUN_800eb914();
    FUN_800e4e1c();

    if((int)_800c6798.get() < 3) {
      FUN_8001eea8(1);
    } else {
      //LAB_800cd004
      FUN_8001eea8((int)_800c6798.get() + 1);
    }

    //LAB_800cd020
  }

  /** This is a hack to "fix" a bug caused by the game loading too fast. Without this delay, Dart will automatically walk forward a bit when leaving a submap. */
  private static int loadWait = 20;

  @Method(0x800cd030L)
  public static void FUN_800cd030() {
    FUN_800d1d88();
    FUN_800e3ff0();

    switch((int)worldMapState_800c6698.get()) {
      case 0:
        break;

      case 2:
        if((filesLoadedFlags_800c66b8.get() & 0x2L) != 0 && (filesLoadedFlags_800c66b8.get() & 0x4L) != 0) { // World map textures and mesh loaded
          worldMapState_800c6698.setu(0x3L);
        }

        //LAB_800cd0d4
        break;

      case 3:
        FUN_800d8efc();
        worldMapState_800c6698.setu(0x4L);
        break;

      case 4:
        worldMapState_800c6698.setu(0x5L);
        break;

      case 5:
        renderWorldMap();
        break;

      case 6:
        worldMapState_800c6698.setu(0x7L);
        break;

      case 7:
        deallocateWorldMap();
        worldMapState_800c6698.setu(0);
        break;
    }

    //LAB_800cd148
    switch((int)playerState_800c669c.get()) {
      case 0:
        loadWait = 20;
        break;

      case 2:
        if((filesLoadedFlags_800c66b8.get() & 0x2a8L) == 0x2a8L && (filesLoadedFlags_800c66b8.get() & 0x550L) == 0x550L) {
          playerState_800c669c.setu(0x3L);
        }

        //LAB_800cd1dc
        break;

      case 3:
        if(loadWait-- > 10) break;

        FUN_800dfbd8();
        playerState_800c669c.setu(0x4L);
        break;

      case 4:
        if(loadWait-- > 0) break;

        playerState_800c669c.setu(0x5L);
        break;

      case 5:
        renderPlayer();
        break;

      case 6:
        playerState_800c669c.setu(0x7L);
        break;

      case 7:
        FUN_800e05c4();
        playerState_800c669c.setu(0);
        break;
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
    FUN_800e05c4();
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
    vsyncMode_8007a3b8.set(2);
  }

  /**
   * Type controls which GPU packet type it is
   *
   * TODO list what packet type each type maps to (e.g. type 0 is 0x40)
   */
  @Method(0x800cd358L)
  public static void setGpuPacketType(final long type, final long packetAddr, final boolean transparency, final boolean unshaded) {
    gpuPacketSetters_800ef034.get((int)type).deref().run(packetAddr);
    gpuLinkedListSetCommandTransparency(packetAddr, transparency);
    gpuLinkedListSetCommandTextureUnshaded(packetAddr, unshaded);
  }

  @Method(0x800cd3c8L)
  public static WMapRender40 FUN_800cd3c8(final long a0, final COLOUR a1, final COLOUR a2, final COLOUR a3, final COLOUR a4, final long a5, final long a6, final long a7, final long a8, final long a9, final boolean transparency, final Translucency transparencyMode, final long a12, final int z, final long a14, final long a15, final long a16) {
    long sp2c = 0;
    long sp30 = 0;
    long sp38;
    long sp3c;
    long sp48;
    long sp4c;
    long sp54_s;
    long sp56_s;

    final WMapRender40 sp34 = MEMORY.ref(4, mallocTail(0x40L), WMapRender40::new);

    sp34._28.set(a7);
    sp34._2c.set(a8);
    sp34._30.set(a7 * a8);
    sp34._34.set((short)a0);
    sp34.x_38.set(0);
    sp34.y_3a.set(0);
    sp34.transparency_3c.set(transparency);
    sp34._3d.set((int)a12);
    sp34.z_3e.set(z);

    if(a9 == 0) {
      sp34._00.set(MEMORY.ref(4, mallocTail(0x10L), UnboundedArrayRef.of(0x10, WMapRender10::new)));
    } else {
      //LAB_800cd4fc
      sp34._00.set(MEMORY.ref(4, mallocTail(sp34._30.get() * 0x10L), UnboundedArrayRef.of(0x10, WMapRender10::new)));
    }

    //LAB_800cd534
    FUN_800ce0bc(sp34, a9, a1, a2, a3, a4, a5);

    //LAB_800cd578
    for(int i = 0; i < 2; i++) {
      //LAB_800cd594
      sp34.tpagePacket_04.get(i).set(0);
      sp34.renderPacket_0c.get(i).set(0);
      sp34._14.get(i).clear();
    }

    //LAB_800cd600
    sp34._1c.set(MEMORY.ref(4, mallocTail(sp34._30.get() * 0x8L), UnboundedArrayRef.of(0x8, RECT::new)));

    final long sp58_s = MEMORY.ref(2, a6).offset(0x4L).getSigned() / (int)a7;
    final long sp5a_s = MEMORY.ref(2, a6).offset(0x6L).getSigned() / (int)a8;

    if(a12 != 0x9L) {
      if(a12 == 0xcL) {
        //LAB_800cdbec
        //LAB_800cdbf0
        for(int i = 0; i < 2; i++) {
          //LAB_800cdc0c
          sp34._14.get(i).set(MEMORY.ref(4, mallocTail(sp34._30.get() * 0x28L), UnboundedArrayRef.of(0x28, WMapRender28::new, () -> (int)sp34._30.get())));
        }

        //LAB_800cdc74
        //LAB_800cdca0
        for(int i = 0; i < sp34._30.get(); i++) {
          final long sp40 = sp34._14.get(0).deref().get(i).getAddress(); //TODO
          final long sp44 = sp34._14.get(1).deref().get(i).getAddress();
          final long sp5c = sp34._1c.deref().get(i).getAddress();

          //LAB_800cdcc4
          setGpuPacketType(0xcL, sp40, transparency, false);
          setGpuPacketType(0xcL, sp44, transparency, false);

          MEMORY.ref(2, sp40).offset(0x16L).setu(a15);
          MEMORY.ref(2, sp40).offset(0xeL).setu(a16);
          MEMORY.ref(2, sp44).offset(0x16L).setu(a15);
          MEMORY.ref(2, sp44).offset(0xeL).setu(a16);

          sp54_s = MEMORY.ref(2, a6).offset(0x0L).get() + sp58_s * sp2c - 0xa0L;
          sp56_s = MEMORY.ref(2, a6).offset(0x2L).get() + sp5a_s * sp30 - 0x78L;

          MEMORY.ref(2, sp40).offset(0x8L).setu(sp54_s);
          MEMORY.ref(2, sp40).offset(0xaL).setu(sp56_s);
          MEMORY.ref(2, sp40).offset(0x10L).setu(sp54_s + sp58_s);
          MEMORY.ref(2, sp40).offset(0x12L).setu(sp56_s);
          MEMORY.ref(2, sp40).offset(0x18L).setu(sp54_s);
          MEMORY.ref(2, sp40).offset(0x1aL).setu(sp56_s + sp5a_s);
          MEMORY.ref(2, sp40).offset(0x20L).setu(sp54_s + sp58_s);
          MEMORY.ref(2, sp40).offset(0x22L).setu(sp56_s + sp5a_s);
          MEMORY.ref(2, sp44).offset(0x8L).setu(sp54_s);
          MEMORY.ref(2, sp44).offset(0xaL).setu(sp56_s);
          MEMORY.ref(2, sp44).offset(0x10L).setu(sp54_s + sp58_s);
          MEMORY.ref(2, sp44).offset(0x12L).setu(sp56_s);
          MEMORY.ref(2, sp44).offset(0x18L).setu(sp54_s);
          MEMORY.ref(2, sp44).offset(0x1aL).setu(sp56_s + sp5a_s);
          MEMORY.ref(2, sp44).offset(0x20L).setu(sp54_s + sp58_s);
          MEMORY.ref(2, sp44).offset(0x22L).setu(sp56_s + sp5a_s);
          MEMORY.ref(2, sp5c).offset(0x0L).setu(sp54_s);
          MEMORY.ref(2, sp5c).offset(0x2L).setu(sp56_s);
          MEMORY.ref(2, sp5c).offset(0x4L).setu(sp58_s);
          MEMORY.ref(2, sp5c).offset(0x6L).setu(sp5a_s);

          if(sp2c < a7 - 0x1L) {
            //LAB_800cdf88
            sp2c++;
          } else {
            sp2c = 0;

            if(sp30 < a8 - 0x1L) {
              sp30++;
            }
          }

          //LAB_800cdf80
          //LAB_800cdf98
        }

        //LAB_800cdfd0
        FUN_800ce170(sp34._14.get(0).deref(), a14, sp34._30.get(), sp34._28.get(), sp34._2c.get(), 0, 0, 0x1L);
        FUN_800ce170(sp34._14.get(1).deref(), a14, sp34._30.get(), sp34._28.get(), sp34._2c.get(), 0, 0, 0x1L);
        sp34._20.get(0).set(MEMORY.ref(4, a14).offset(0x0L).get());
        sp34._20.get(1).set(MEMORY.ref(4, a14).offset(0x4L).get());
      }
    } else {
      //LAB_800cd6b8
      if(transparency) {
        //LAB_800cd6cc
        for(int i = 0; i < 2; i++) {
          //LAB_800cd6e8
          sp34.tpagePacket_04.get(i).set(mallocTail(sp34._30.get() * 0x8L));
        }
      }

      //LAB_800cd748
      //LAB_800cd74c
      for(int i = 0; i < 2; i++) {
        //LAB_800cd768
        sp34.renderPacket_0c.get(i).set(mallocTail(sp34._30.get() * 0x24L));
      }

      //LAB_800cd7d0
      sp48 = sp34.tpagePacket_04.get(0).get();
      sp4c = sp34.tpagePacket_04.get(1).get();
      sp38 = sp34.renderPacket_0c.get(0).get();
      sp3c = sp34.renderPacket_0c.get(1).get();

      sp2c = 0;
      sp30 = 0;

      //LAB_800cd82c
      for(int i = 0; i < sp34._30.get(); i++) {
        final long sp5c = sp34._1c.deref().get(i).getAddress(); //TODO

        //LAB_800cd850
        if(transparency) {
          MEMORY.ref(1, sp48).offset(0x3L).setu(0x1L);
          MEMORY.ref(4, sp48).offset(0x4L).setu(0xe100_0000L | GetTPage(Bpp.BITS_4, transparencyMode, 0, 0) & 0x9ffL);

          MEMORY.ref(1, sp4c).offset(0x3L).setu(0x1L);
          MEMORY.ref(4, sp4c).offset(0x4L).setu(0xe100_0000L | GetTPage(Bpp.BITS_4, transparencyMode, 0, 0) & 0x9ffL);
        }

        //LAB_800cd8e8
        setGpuPacketType(0x9L, sp38, transparency, false);
        setGpuPacketType(0x9L, sp3c, transparency, false);

        sp54_s = MEMORY.ref(2, a6).offset(0x0L).get() + sp58_s * sp2c - 0xa0L;
        sp56_s = MEMORY.ref(2, a6).offset(0x2L).get() + sp5a_s * sp30 - 0x78L;

        MEMORY.ref(2, sp38).offset(0x8L).setu(sp54_s);
        MEMORY.ref(2, sp38).offset(0xaL).setu(sp56_s);
        MEMORY.ref(2, sp38).offset(0x10L).setu(sp54_s + sp58_s);
        MEMORY.ref(2, sp38).offset(0x12L).setu(sp56_s);
        MEMORY.ref(2, sp38).offset(0x18L).setu(sp54_s);
        MEMORY.ref(2, sp38).offset(0x1aL).setu(sp56_s + sp5a_s);
        MEMORY.ref(2, sp38).offset(0x20L).setu(sp54_s + sp58_s);
        MEMORY.ref(2, sp38).offset(0x22L).setu(sp56_s + sp5a_s);

        MEMORY.ref(2, sp3c).offset(0x8L).setu(sp54_s);
        MEMORY.ref(2, sp3c).offset(0xaL).setu(sp56_s);
        MEMORY.ref(2, sp3c).offset(0x10L).setu(sp54_s + sp58_s);
        MEMORY.ref(2, sp3c).offset(0x12L).setu(sp56_s);
        MEMORY.ref(2, sp3c).offset(0x18L).setu(sp54_s);
        MEMORY.ref(2, sp3c).offset(0x1aL).setu(sp56_s + sp5a_s);
        MEMORY.ref(2, sp3c).offset(0x20L).setu(sp54_s + sp58_s);
        MEMORY.ref(2, sp3c).offset(0x22L).setu(sp56_s + sp5a_s);

        MEMORY.ref(2, sp5c).offset(0x0L).setu(sp54_s);
        MEMORY.ref(2, sp5c).offset(0x2L).setu(sp56_s);
        MEMORY.ref(2, sp5c).offset(0x4L).setu(sp58_s);
        MEMORY.ref(2, sp5c).offset(0x6L).setu(sp5a_s);

        if(sp2c < a7 - 0x1L) {
          //LAB_800cdb6c
          sp2c++;
        } else {
          sp2c = 0;

          if(sp30 < a8 - 0x1L) {
            sp30++;
          }
        }

        //LAB_800cdb64
        //LAB_800cdb7c
        sp38 += 0x24L;
        sp3c += 0x24L;
        sp48 += 0x8L;
        sp4c += 0x8L;
      }

      //LAB_800cdbe4
    }

    //LAB_800ce094
    //LAB_800ce0a8
    return sp34;
  }

  @Method(0x800ce0bcL)
  public static void FUN_800ce0bc(final WMapRender40 a0, final long a1, final COLOUR a2, final COLOUR a3, final COLOUR a4, final COLOUR a5, final long a6) {
    a0._3f.set((int)a1);
    FUN_800cf20c(a0._00.deref(), a1, a0._28.get(), a0._2c.get(), a2, a3, a4, a5, a6);
    a0._36.set((short)-1);
  }

  @Method(0x800ce170L)
  public static void FUN_800ce170(final UnboundedArrayRef<WMapRender28> a0, final long a1, final long a2, final long a3, final long a4, final long a5, final long a6, final long a7) {
    assert false;
  }

  @Method(0x800ce4dcL)
  public static void FUN_800ce4dc(final WMapRender40 a0) {
    long renderPacket;
    long tpagePacket;

    FUN_800cea1c(a0);

    if(a0._3d.get() == 9) {
      //LAB_800ce538
      renderPacket = a0.renderPacket_0c.get(doubleBufferFrame_800bb108.get()).get();

      if(a0.transparency_3c.get()) {
        tpagePacket = a0.tpagePacket_04.get(doubleBufferFrame_800bb108.get()).get();
      } else {
        tpagePacket = 0; // Only used if transparency is set, just needs to be initialized
      }

      //LAB_800ce5a0
      //LAB_800ce5a4
      for(int i = 0; i < a0._30.get(); i++) {
        final RECT sp20 = a0._1c.deref().get(i);

        //LAB_800ce5c8
        final int left = a0.x_38.get() + sp20.x.get();
        final int top = a0.y_3a.get() + sp20.y.get();
        final int right = left + sp20.w.get();
        final int bottom = top + sp20.h.get();
        MEMORY.ref(2, renderPacket).offset(0x08L).setu(left);
        MEMORY.ref(2, renderPacket).offset(0x0aL).setu(top);
        MEMORY.ref(2, renderPacket).offset(0x10L).setu(right);
        MEMORY.ref(2, renderPacket).offset(0x12L).setu(top);
        MEMORY.ref(2, renderPacket).offset(0x18L).setu(left);
        MEMORY.ref(2, renderPacket).offset(0x1aL).setu(bottom);
        MEMORY.ref(2, renderPacket).offset(0x20L).setu(right);
        MEMORY.ref(2, renderPacket).offset(0x22L).setu(bottom);

        final GpuCommandPoly cmd = new GpuCommandPoly(4)
          .rgb(0, (int)MEMORY.get(renderPacket + 0x04L, 3))
          .rgb(1, (int)MEMORY.get(renderPacket + 0x0cL, 3))
          .rgb(2, (int)MEMORY.get(renderPacket + 0x14L, 3))
          .rgb(3, (int)MEMORY.get(renderPacket + 0x1cL, 3))
          .pos(0, left, top)
          .pos(1, right, top)
          .pos(2, left, bottom)
          .pos(3, right, bottom);

        if(a0.transparency_3c.get()) {
          cmd.translucent(Translucency.of((int)MEMORY.get(tpagePacket + 0x4L, 4) >>> 5 & 0b11));
        }

        GPU.queueCommand(a0.z_3e.get(), cmd);

        //LAB_800ce7a0
        renderPacket += 0x24L;
        tpagePacket += 0x8L;
      }
    } else assert a0._3d.get() != 0xcL : "Bugged; sp18 was uninitialized";

    //LAB_800ce7e8
    //LAB_800cea08
  }

  @Method(0x800cea1cL)
  public static void FUN_800cea1c(final WMapRender40 a0) {
    final long v0;
    long sp22;
    long sp21;
    long sp32;
    long sp20;
    long sp31;
    long sp30;
    long sp19;
    long sp18;
    long sp29;
    long sp28;
    long sp1a;
    long sp2a;

    if(a0._34.get() < 0) {
      a0._34.set((short)0);
      //LAB_800cea54
    } else if(a0._34.get() > 0x100) {
      a0._34.set((short)0x100);
    }

    //LAB_800cea7c
    if(a0._34.get() == a0._36.get()) {
      return;
    }

    //LAB_800ceaa0
    v0 = a0._3d.get();
    if(v0 == 0x9L) {
      //LAB_800ceacc
      long sp4 = a0.renderPacket_0c.get(doubleBufferFrame_800bb108.get()).get();
      long sp8 = a0.renderPacket_0c.get(doubleBufferFrame_800bb108.get() ^ 1).get();

      //LAB_800ceb38
      int n = 0;
      for(int i = 0; i < a0._30.get(); i++) {
        final WMapRender10 sp14 = a0._00.deref().get(n);

        //LAB_800ceb5c
        //LAB_800ceb8c
        sp18 = sp14._00.getR() * a0._34.get() / 0x100L;
        //LAB_800cebc4
        sp19 = sp14._00.getG() * a0._34.get() / 0x100L;
        //LAB_800cebfc
        sp1a = sp14._00.getB() * a0._34.get() / 0x100L;
        //LAB_800cec34
        sp20 = sp14._04.getR() * a0._34.get() / 0x100L;
        //LAB_800cec6c
        sp21 = sp14._04.getG() * a0._34.get() / 0x100L;
        //LAB_800ceca4
        sp22 = sp14._04.getB() * a0._34.get() / 0x100L;
        //LAB_800cecdc
        sp28 = sp14._08.getR() * a0._34.get() / 0x100L;
        //LAB_800ced14
        sp29 = sp14._08.getG() * a0._34.get() / 0x100L;
        //LAB_800ced4c
        sp2a = sp14._08.getB() * a0._34.get() / 0x100L;
        //LAB_800ced84
        sp30 = sp14._0c.getR() * a0._34.get() / 0x100L;
        //LAB_800cedbc
        sp31 = sp14._0c.getG() * a0._34.get() / 0x100L;
        //LAB_800cedf4
        sp32 = sp14._0c.getB() * a0._34.get() / 0x100L;

        MEMORY.ref(1, sp4).offset(0x4L).setu(sp18);
        MEMORY.ref(1, sp4).offset(0x5L).setu(sp19);
        MEMORY.ref(1, sp4).offset(0x6L).setu(sp1a);
        MEMORY.ref(1, sp4).offset(0xcL).setu(sp20);
        MEMORY.ref(1, sp4).offset(0xdL).setu(sp21);
        MEMORY.ref(1, sp4).offset(0xeL).setu(sp22);
        MEMORY.ref(1, sp4).offset(0x14L).setu(sp28);
        MEMORY.ref(1, sp4).offset(0x15L).setu(sp29);
        MEMORY.ref(1, sp4).offset(0x16L).setu(sp2a);
        MEMORY.ref(1, sp4).offset(0x1cL).setu(sp30);
        MEMORY.ref(1, sp4).offset(0x1dL).setu(sp31);
        MEMORY.ref(1, sp4).offset(0x1eL).setu(sp32);
        MEMORY.ref(1, sp8).offset(0x4L).setu(sp18);
        MEMORY.ref(1, sp8).offset(0x5L).setu(sp19);
        MEMORY.ref(1, sp8).offset(0x6L).setu(sp1a);
        MEMORY.ref(1, sp8).offset(0xcL).setu(sp20);
        MEMORY.ref(1, sp8).offset(0xdL).setu(sp21);
        MEMORY.ref(1, sp8).offset(0xeL).setu(sp22);
        MEMORY.ref(1, sp8).offset(0x14L).setu(sp28);
        MEMORY.ref(1, sp8).offset(0x15L).setu(sp29);
        MEMORY.ref(1, sp8).offset(0x16L).setu(sp2a);
        MEMORY.ref(1, sp8).offset(0x1cL).setu(sp30);
        MEMORY.ref(1, sp8).offset(0x1dL).setu(sp31);
        MEMORY.ref(1, sp8).offset(0x1eL).setu(sp32);

        if(a0._3f.get() != 0) {
          n++;
        }

        //LAB_800cefa4
        sp4 += 0x24L;
        sp8 += 0x24L;
      }

      //LAB_800cefdc
    } else if(v0 == 0xcL) {
      //LAB_800cefe4
      //LAB_800cf050
      int n = 0;
      for(int i = 0; i < a0._30.get(); i++) {
        final WMapRender28 spc = a0._14.get(doubleBufferFrame_800bb108.get()).deref().get(i);
        final WMapRender28 sp10 = a0._14.get(doubleBufferFrame_800bb108.get() ^ 1).deref().get(i);
        final WMapRender10 sp14 = a0._00.deref().get(n);

        //LAB_800cf074
        //LAB_800cf0a4
        sp18 = sp14._00.getR() * a0._34.get() / 0x100L;
        //LAB_800cf0dc
        sp19 = sp14._00.getG() * a0._34.get() / 0x100L;
        //LAB_800cf114
        sp1a = sp14._00.getB() * a0._34.get() / 0x100L;

        spc._04.setR((int)sp18);
        spc._04.setG((int)sp19);
        spc._04.setB((int)sp1a);
        sp10._04.setR((int)sp18);
        sp10._04.setG((int)sp19);
        sp10._04.setB((int)sp1a);

        if(a0._3f.get() != 0) {
          n++;
        }

        //LAB_800cf1a4
      }
    }

    //LAB_800cf1dc
    //LAB_800cf1e4
    a0._36.set(a0._34.get());

    //LAB_800cf1fc
  }

  @Method(0x800cf20cL)
  public static void FUN_800cf20c(final UnboundedArrayRef<WMapRender10> a0, final long a1, final long a2, final long a3, final COLOUR a4, final COLOUR a5, final COLOUR a6, final COLOUR a7, final long a8) {
    int sp14;
    long sp18;
    long sp1c;
    final long[] sp0x48 = new long[8];

    switch((int)a1) {
      case 0 -> {
        a0.get(0)._00.set(a4);
        a0.get(0)._04.set(a5);
        a0.get(0)._08.set(a6);
        a0.get(0)._0c.set(a7);
      }

      case 1 -> {
        sp0x48[0] = a4.getAddress();
        sp0x48[1] = a5.getAddress();
        sp0x48[2] = a6.getAddress();
        sp0x48[3] = a7.getAddress();
        sp14 = 0;

        //LAB_800cf32c
        for(sp1c = 0; sp1c < a3; sp1c++) {
          //LAB_800cf34c
          //LAB_800cf350
          for(sp18 = 0; sp18 < a2; sp18++) {
            final WMapRender10 sp20 = a0.get(sp14);

            //LAB_800cf370
            sp0x48[4] = sp18;
            sp0x48[5] = a2 - sp18;
            sp0x48[6] = sp1c;
            sp0x48[7] = a3 - sp1c;
            FUN_800d112c(sp0x48, sp20._00);

            sp0x48[4] = sp18 + 0x1L;
            sp0x48[5] = a2 - 0x1L - sp18;
            sp0x48[6] = sp1c;
            sp0x48[7] = a3 - sp1c;
            FUN_800d112c(sp0x48, sp20._04);

            sp0x48[4] = sp18;
            sp0x48[5] = a2 - sp18;
            sp0x48[6] = sp1c + 0x1L;
            sp0x48[7] = a3 - 0x1L - sp1c;
            FUN_800d112c(sp0x48, sp20._08);

            sp0x48[4] = sp18 + 0x1L;
            sp0x48[5] = a2 - 0x1L - sp18;
            sp0x48[6] = sp1c + 0x1L;
            sp0x48[7] = a3 - 0x1L - sp1c;
            FUN_800d112c(sp0x48, sp20._0c);

            sp14++;
          }

          //LAB_800cf54c
        }
      }

      //LAB_800cf564
      case 2 -> {
        sp0x48[0] = a4.getAddress();
        sp0x48[1] = a5.getAddress();
        sp0x48[2] = a8;
        sp0x48[3] = a8;
        sp14 = 0;

        //LAB_800cf5a4
        for(sp1c = 0; sp1c < a3 / 2; sp1c++) {
          //LAB_800cf5d8
          //LAB_800cf5dc
          for(sp18 = 0; sp18 < a2; sp18++) {
            final WMapRender10 sp20 = a0.get(sp14);

            //LAB_800cf5fc
            sp0x48[4] = sp18;
            sp0x48[5] = a2 - sp18;
            sp0x48[6] = sp1c;
            sp0x48[7] = a3 / 2 - sp1c;
            FUN_800d112c(sp0x48, sp20._00);

            sp0x48[4] = sp18 + 0x1L;
            sp0x48[5] = a2 - 0x1L - sp18;
            sp0x48[6] = sp1c;
            sp0x48[7] = a3 / 2 - sp1c;
            FUN_800d112c(sp0x48, sp20._04);

            sp0x48[4] = sp18;
            sp0x48[5] = a2 - sp18;
            sp0x48[6] = sp1c + 0x1L;
            sp0x48[7] = a3 / 2 - 0x1L - sp1c;
            FUN_800d112c(sp0x48, sp20._08);

            sp0x48[4] = sp18 + 0x1L;
            sp0x48[5] = a2 - 0x1L - sp18;
            sp0x48[6] = sp1c + 0x1L;
            sp0x48[7] = a3 / 2 - 0x1L - sp1c;
            FUN_800d112c(sp0x48, sp20._0c);

            sp14++;
          }

          //LAB_800cf820
        }

        //LAB_800cf838
        sp0x48[0] = a8;
        sp0x48[1] = a8;
        sp0x48[2] = a6.getAddress();
        sp0x48[3] = a7.getAddress();

        //LAB_800cf870
        for(sp1c = 0; sp1c < a3 / 2; sp1c++) {
          //LAB_800cf8a4
          //LAB_800cf8a8
          for(sp18 = 0; sp18 < a2; sp18++) {
            final WMapRender10 sp20 = a0.get(sp14);

            //LAB_800cf8c8
            sp0x48[4] = sp18;
            sp0x48[5] = a2 - sp18;
            sp0x48[6] = sp1c;
            sp0x48[7] = a3 / 2 - sp1c;
            FUN_800d112c(sp0x48, sp20._00);

            sp0x48[4] = sp18 + 0x1L;
            sp0x48[5] = a2 - 0x1L - sp18;
            sp0x48[6] = sp1c;
            sp0x48[7] = a3 / 2 - sp1c;
            FUN_800d112c(sp0x48, sp20._04);

            sp0x48[4] = sp18;
            sp0x48[5] = a2 - sp18;
            sp0x48[6] = sp1c + 0x1L;
            sp0x48[7] = a3 / 2 - 0x1L - sp1c;
            FUN_800d112c(sp0x48, sp20._08);

            sp0x48[4] = sp18 + 0x1L;
            sp0x48[5] = a2 - 0x1L - sp18;
            sp0x48[6] = sp1c + 0x1L;
            sp0x48[7] = a3 / 2 - 0x1L - sp1c;
            FUN_800d112c(sp0x48, sp20._0c);

            sp14++;
          }

          //LAB_800cfaec
        }
      }

      //LAB_800cfb04
      case 3 -> {
        sp0x48[0] = a4.getAddress();
        sp0x48[1] = a8;
        sp0x48[2] = a6.getAddress();
        sp0x48[3] = a8;
        sp14 = 0;

        //LAB_800cfb50
        for(sp1c = 0; sp1c >= a3; sp1c++) {
          //LAB_800cfb70
          //LAB_800cfb74
          for(sp18 = 0; sp18 < a2 / 2; sp18++) {
            final WMapRender10 sp20 = a0.get(sp14);

            //LAB_800cfba8
            sp0x48[4] = sp18;
            sp0x48[5] = a2 / 2 - sp18;
            sp0x48[6] = sp1c;
            sp0x48[7] = a3 - sp1c;
            FUN_800d112c(sp0x48, sp20._00);

            sp0x48[4] = sp18 + 0x1L;
            sp0x48[5] = a2 / 2 - 0x1L - sp18;
            sp0x48[6] = sp1c;
            sp0x48[7] = a3 - sp1c;
            FUN_800d112c(sp0x48, sp20._04);

            sp0x48[4] = sp18;
            sp0x48[5] = a2 / 2 - sp18;
            sp0x48[6] = sp1c + 0x1L;
            sp0x48[7] = a3 - 0x1L - sp1c;
            FUN_800d112c(sp0x48, sp20._08);

            sp0x48[4] = sp18 + 0x1L;
            sp0x48[5] = a2 / 2 - 0x1L - sp18;
            sp0x48[6] = sp1c + 0x1L;
            sp0x48[7] = a3 - 0x1L - sp1c;
            FUN_800d112c(sp0x48, sp20._0c);

            sp14++;
          }

          //LAB_800cfdcc
          sp14 += a2 / 2;
        }

        //LAB_800cfe14
        sp0x48[0] = a8;
        sp0x48[1] = a5.getAddress();
        sp0x48[2] = a8;
        sp0x48[3] = a7.getAddress();
        sp14 = (int)(a2 / 2);

        //LAB_800cfe7c
        for(sp1c = 0; sp1c < a3; sp1c++) {
          //LAB_800cfe9c
          //LAB_800cfea0
          for(sp18 = 0; sp18 < a2 / 2; sp18++) {
            final WMapRender10 sp20 = a0.get(sp14);

            //LAB_800cfed4
            sp0x48[4] = sp18;
            sp0x48[5] = a2 / 2 - sp18;
            sp0x48[6] = sp1c;
            sp0x48[7] = a3 - sp1c;
            FUN_800d112c(sp0x48, sp20._00);

            sp0x48[4] = sp18 + 0x1L;
            sp0x48[5] = a2 / 2 - 0x1L - sp18;
            sp0x48[6] = sp1c;
            sp0x48[7] = a3 - sp1c;
            FUN_800d112c(sp0x48, sp20._04);

            sp0x48[4] = sp18;
            sp0x48[5] = a2 / 2 - sp18;
            sp0x48[6] = sp1c + 0x1L;
            sp0x48[7] = a3 - 0x1L - sp1c;
            FUN_800d112c(sp0x48, sp20._08);

            sp0x48[4] = sp18 + 0x1L;
            sp0x48[5] = a2 / 2 - 0x1L - sp18;
            sp0x48[6] = sp1c + 0x1L;
            sp0x48[7] = a3 - 0x1L - sp1c;
            FUN_800d112c(sp0x48, sp20._0c);

            sp14++;
          }

          //LAB_800d00f8
          sp14 += a2 / 2;
        }
      }

      //LAB_800d0140
      case 4 -> {
        final Memory.TemporaryReservation sp0x28tmp = MEMORY.temp(4);
        final Memory.TemporaryReservation sp0x30tmp = MEMORY.temp(4);
        final Memory.TemporaryReservation sp0x38tmp = MEMORY.temp(4);
        final Memory.TemporaryReservation sp0x40tmp = MEMORY.temp(4);
        final COLOUR sp0x28 = sp0x28tmp.get().cast(COLOUR::new);
        final COLOUR sp0x30 = sp0x30tmp.get().cast(COLOUR::new);
        final COLOUR sp0x38 = sp0x38tmp.get().cast(COLOUR::new);
        final COLOUR sp0x40 = sp0x40tmp.get().cast(COLOUR::new);
        sp0x48[0] = a4.getAddress();
        sp0x48[1] = a5.getAddress();
        sp0x48[2] = a6.getAddress();
        sp0x48[3] = a7.getAddress();
        sp0x48[4] = a2 / 2;
        sp0x48[5] = a2 / 2;
        sp0x48[6] = 0;
        sp0x48[7] = a3;
        FUN_800d112c(sp0x48, sp0x28);
        sp0x48[4] = 0;
        sp0x48[5] = a2;
        sp0x48[6] = a3 / 2;
        sp0x48[7] = a3 / 2;
        FUN_800d112c(sp0x48, sp0x30);
        sp0x48[4] = a2;
        sp0x48[5] = 0;
        sp0x48[6] = a3 / 2;
        sp0x48[7] = a3 / 2;
        FUN_800d112c(sp0x48, sp0x38);
        sp0x48[4] = a2 / 2;
        sp0x48[5] = a2 / 2;
        sp0x48[6] = a3;
        sp0x48[7] = 0;
        FUN_800d112c(sp0x48, sp0x40);
        sp0x48[0] = a4.getAddress();
        sp0x48[1] = sp0x28.getAddress();
        sp0x48[2] = sp0x30.getAddress();
        sp0x48[3] = a8;
        sp14 = 0;

        //LAB_800d0334
        for(sp1c = 0; sp1c < a3 / 2; sp1c++) {
          //LAB_800d0368
          //LAB_800d036c
          for(sp18 = 0; sp18 < a2 / 2; sp18++) {
            final WMapRender10 sp20 = a0.get(sp14);

            //LAB_800d03a0
            sp0x48[4] = sp18;
            sp0x48[5] = a2 / 2 - sp18;
            sp0x48[6] = sp1c;
            sp0x48[7] = a3 / 2 - sp1c;
            FUN_800d112c(sp0x48, sp20._00);

            sp0x48[4] = sp18 + 0x1L;
            sp0x48[5] = a2 / 2 - 0x1L - sp18;
            sp0x48[6] = sp1c;
            sp0x48[7] = a3 / 2 - sp1c;
            FUN_800d112c(sp0x48, sp20._04);

            sp0x48[4] = sp18;
            sp0x48[5] = a2 / 2 - sp18;
            sp0x48[6] = sp1c + 0x1L;
            sp0x48[7] = a3 / 2 - 0x1L - sp1c;
            FUN_800d112c(sp0x48, sp20._08);

            sp0x48[4] = sp18 + 0x1L;
            sp0x48[5] = a2 / 2 - 0x1L - sp18;
            sp0x48[6] = sp1c + 0x1L;
            sp0x48[7] = a3 / 2 - 0x1L - sp1c;
            FUN_800d112c(sp0x48, sp20._0c);

            sp14++;
          }

          //LAB_800d060c
          sp14 += a2 / 2;
        }

        //LAB_800d0654
        sp0x48[0] = sp0x28.getAddress();
        sp0x48[1] = a5.getAddress();
        sp0x48[2] = a8;
        sp0x48[3] = sp0x38.getAddress();
        sp14 = (int)(a2 / 2);

        //LAB_800d06b4
        for(sp1c = 0; sp1c < a3 / 2; sp1c++) {
          //LAB_800d06e8
          //LAB_800d06ec
          for(sp18 = 0; sp18 < a2 / 2; sp18++) {
            final WMapRender10 sp20 = a0.get(sp14);

            //LAB_800d0720
            sp0x48[4] = sp18;
            sp0x48[5] = a2 / 2 - sp18;
            sp0x48[6] = sp1c;
            sp0x48[7] = a3 / 2 - sp1c;
            FUN_800d112c(sp0x48, sp20._00);

            sp0x48[4] = sp18 + 0x1L;
            sp0x48[5] = a2 / 2 - 0x1L - sp18;
            sp0x48[6] = sp1c;
            sp0x48[7] = a3 / 2 - sp1c;
            FUN_800d112c(sp0x48, sp20._04);

            sp0x48[4] = sp18;
            sp0x48[5] = a2 / 2 - sp18;
            sp0x48[6] = sp1c + 0x1L;
            sp0x48[7] = a3 / 2 - 0x1L - sp1c;
            FUN_800d112c(sp0x48, sp20._08);

            sp0x48[4] = sp18 + 0x1L;
            sp0x48[5] = a2 / 2 - 0x1L - sp18;
            sp0x48[6] = sp1c + 0x1L;
            sp0x48[7] = a3 / 2 - 0x1L - sp1c;
            FUN_800d112c(sp0x48, sp20._0c);

            sp14++;
          }

          //LAB_800d098c
          sp14 += a2 / 2;
        }

        //LAB_800d09d4
        sp0x48[0] = sp0x30.getAddress();
        sp0x48[1] = a8;
        sp0x48[2] = a6.getAddress();
        sp0x48[3] = sp0x40.getAddress();
        sp14 = (int)(a2 * a3 / 2);

        //LAB_800d0a40
        for(sp1c = 0; sp1c < a3 / 2; sp1c++) {
          //LAB_800d0a74
          //LAB_800d0a78
          for(sp18 = 0; sp18 < a2 / 2; sp18++) {
            final WMapRender10 sp20 = a0.get(sp14);

            //LAB_800d0aac
            sp0x48[4] = sp18;
            sp0x48[5] = a2 / 2 - sp18;
            sp0x48[6] = sp1c;
            sp0x48[7] = a3 / 2 - sp1c;
            FUN_800d112c(sp0x48, sp20._00);

            sp0x48[4] = sp18 + 0x1L;
            sp0x48[5] = a2 / 2 - 0x1L - sp18;
            sp0x48[6] = sp1c;
            sp0x48[7] = a3 / 2 - sp1c;
            FUN_800d112c(sp0x48, sp20._04);

            sp0x48[4] = sp18;
            sp0x48[5] = a2 / 2 - sp18;
            sp0x48[6] = sp1c + 0x1L;
            sp0x48[7] = a3 / 2 - 0x1L - sp1c;
            FUN_800d112c(sp0x48, sp20._08);

            sp0x48[4] = sp18 + 0x1L;
            sp0x48[5] = a2 / 2 - 0x1L - sp18;
            sp0x48[6] = sp1c + 0x1L;
            sp0x48[7] = a3 / 2 - 0x1L - sp1c;
            FUN_800d112c(sp0x48, sp20._0c);

            sp14++;
          }

          //LAB_800d0d18
          sp14 += a2 / 2;
        }

        //LAB_800d0d60
        sp0x48[0] = a8;
        sp0x48[1] = sp0x38.getAddress();
        sp0x48[2] = sp0x40.getAddress();
        sp0x48[3] = a7.getAddress();
        sp14 = (int)(a2 * a3 / 2 + a2 / 2);

        //LAB_800d0df0
        for(sp1c = 0; sp1c < a3 / 2; sp1c++) {
          //LAB_800d0e24
          //LAB_800d0e28
          for(sp18 = 0; sp18 < a2 / 2; sp18++) {
            final WMapRender10 sp20 = a0.get(sp14);

            //LAB_800d0e5c
            sp0x48[4] = sp18;
            sp0x48[5] = a2 / 2 - sp18;
            sp0x48[6] = sp1c;
            sp0x48[7] = a3 / 2 - sp1c;
            FUN_800d112c(sp0x48, sp20._00);

            sp0x48[4] = sp18 + 0x1L;
            sp0x48[5] = a2 / 2 - 0x1L - sp18;
            sp0x48[6] = sp1c;
            sp0x48[7] = a3 / 2 - sp1c;
            FUN_800d112c(sp0x48, sp20._04);

            sp0x48[4] = sp18;
            sp0x48[5] = a2 / 2 - sp18;
            sp0x48[6] = sp1c + 0x1L;
            sp0x48[7] = a3 / 2 - 0x1L - sp1c;
            FUN_800d112c(sp0x48, sp20._08);

            sp0x48[4] = sp18 + 0x1L;
            sp0x48[5] = a2 / 2 - 0x1L - sp18;
            sp0x48[6] = sp1c + 0x1L;
            sp0x48[7] = a3 / 2 - 0x1L - sp1c;
            FUN_800d112c(sp0x48, sp20._0c);

            sp14++;
          }

          //LAB_800d10c8
          sp14 += a2 / 2;
        }

        sp0x28tmp.release();
        sp0x30tmp.release();
        sp0x38tmp.release();
        sp0x40tmp.release();
      }

      //LAB_800d1110
    }

    //LAB_800d1118
  }

  @Method(0x800d112cL)
  public static void FUN_800d112c(final long[] a0, final COLOUR a1) {
    final long sp10 = (MEMORY.ref(1, a0[1]).offset(0x0L).get() * a0[4] + MEMORY.ref(1, a0[0]).offset(0x0L).get() * a0[5]) / (a0[4] + a0[5]);
    final long sp14 = (MEMORY.ref(1, a0[1]).offset(0x1L).get() * a0[4] + MEMORY.ref(1, a0[0]).offset(0x1L).get() * a0[5]) / (a0[4] + a0[5]);
    final long sp18 = (MEMORY.ref(1, a0[1]).offset(0x2L).get() * a0[4] + MEMORY.ref(1, a0[0]).offset(0x2L).get() * a0[5]) / (a0[4] + a0[5]);

    final long sp0 = (MEMORY.ref(1, a0[3]).offset(0x0L).get() * a0[4] + MEMORY.ref(1, a0[2]).offset(0x0L).get() * a0[5]) / (a0[4] + a0[5]);
    final long sp4 = (MEMORY.ref(1, a0[3]).offset(0x1L).get() * a0[4] + MEMORY.ref(1, a0[2]).offset(0x1L).get() * a0[5]) / (a0[4] + a0[5]);
    final long sp8 = (MEMORY.ref(1, a0[3]).offset(0x2L).get() * a0[4] + MEMORY.ref(1, a0[2]).offset(0x2L).get() * a0[5]) / (a0[4] + a0[5]);

    a1.r.set((int)((a0[6] * sp0 + a0[7] * sp10) / (a0[6] + a0[7])));
    a1.g.set((int)((a0[6] * sp4 + a0[7] * sp14) / (a0[6] + a0[7])));
    a1.b.set((int)((a0[6] * sp8 + a0[7] * sp18) / (a0[6] + a0[7])));
  }

  @Method(0x800d15d8L)
  public static void FUN_800d15d8(final WMapRender40 a0) {
    //LAB_800d15ec
    for(int i = 0; i < 2; i++) {
      //LAB_800d1608
      if(a0.renderPacket_0c.get(i).get() != 0) {
        free(a0.renderPacket_0c.get(i).get());
      }

      //LAB_800d165c
      if(!a0._14.get(i).isNull()) {
        free(a0._14.get(i).getPointer());
      }

      //LAB_800d16b0
      if(a0.tpagePacket_04.get(i).get() != 0) {
        free(a0.tpagePacket_04.get(i).get());
      }

      //LAB_800d1704
    }

    //LAB_800d171c
    free(a0._1c.getPointer());
    free(a0._00.getPointer());
    free(a0.getAddress());
  }

  @Method(0x800d177cL)
  public static void FUN_800d177c() {
    _800c66b0 = new WMapStruct19c0();

    GsInitCoordinate2(null, _800c66b0.coord2_20);

    _800c66b0.coord2_20.coord.transfer.set(0, 0, 0);
    _800c66b0.mapRotation_70.set((short)0, (short)0, (short)0);
    _800c66b0.rview2_00.viewpoint_00.set(0, -300, -900);
    _800c66b0.rview2_00.refpoint_0c.set(0, 300, 900);
    _800c66b0.rview2_00.viewpointTwist_18 = 0;
    _800c66b0.rview2_00.super_1c = _800c66b0.coord2_20;

    FUN_800d1d28();
    FUN_800d1914();

    _800c66b0._114 = 0;
    _800c66b0._118 = 1100;
    _800c66b0._11a = 0;
  }

  @Method(0x800d1914L)
  public static void FUN_800d1914() {
    final WMapStruct19c0 v0 = _800c66b0;

    _8007a3a8.set(0);
    _800bb104.set(0);
    _800babc0.set(0);

    v0._154[0].index_00 = -1;
    v0._196c = 0;
    v0._1970 = 0;
    v0._1974 = -1;

    FUN_800d1db8();

    //LAB_800d1984
    for(int i = 0; i < 3; i++) {
      //LAB_800d19a0
      v0._19a8[i] = 15;
      v0._19ae[i] = 315;

      final GsF_LIGHT light = v0.lights_11c[i];
      light.r_0c.set(0x20);
      light.g_0d.set(0x20);
      light.b_0e.set(0x20);
      light.direction_00.setX(rsin((v0._19a8[i] << 12) / 360) << 12 >> 12);
      light.direction_00.setY(rcos((v0._19ae[i] << 12) / 360) << 12 >> 12);
      light.direction_00.setZ(rcos((v0._19a8[i] << 12) / 360) << 12 >> 12);
      light.direction_00.setX(1000);
      light.direction_00.setY(100);
      light.direction_00.setZ(0);
      GsSetFlatLight(i, light);
    }

    //LAB_800d1c88
    setLightMode(0);
    v0.ambientLight_14c.set((short)0x600, (short)0x600, (short)0x600);
    GsSetAmbient(v0.ambientLight_14c.getX(), v0.ambientLight_14c.getY(), v0.ambientLight_14c.getZ());
    v0._88 = 0;
  }

  @Method(0x800d1d28L)
  public static void FUN_800d1d28() {
    _800c66b0.mapRotating_80 = 0;
    _800c66b0.mapRotationStep_7c = 0;
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
    for(int i = 0; i < _800c67a0.get(); i++) {
      //LAB_800d1e38
      if(!places_800f0234.get(_800f0e34.get(i).placeIndex_02.get()).name_00.isNull()) {
        //LAB_800d1e90
        if(FUN_800eb09c(i, 1, _800c66b0._154[count].vec_08) == 0) {
          //LAB_800d1ee0
          final long dx = x - _800c66b0._154[count].vec_08.getX();
          final long dy = y - _800c66b0._154[count].vec_08.getY();
          final long dz = z - _800c66b0._154[count].vec_08.getZ();

          _800c66b0._154[count].index_00 = i;
          _800c66b0._154[count].vecLength_04 = SquareRoot0(dx * dx + dy * dy + dz * dz);

          count++;
        }
      }

      //LAB_800d2070
    }

    //LAB_800d2088
    _800c66b0._154[count].index_00 = -1;
    Arrays.sort(_800c66b0._154, Comparator.comparingInt(a -> a.vecLength_04));
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
            struct.colour_8c[i].setR(struct.lights_11c[i].r_0c.get());
            struct.colour_8c[i].setG(struct.lights_11c[i].g_0d.get());
            struct.colour_8c[i].setB(struct.lights_11c[i].b_0e.get());
          }

          //LAB_800d235c
          _800c66b0._84 = 256;
          _800c66b0._88 = 1;
        }

        //LAB_800d237c
        _800c66b0._84 -= 36;

        if(_800c66b0._84 < 64) {
          _800c66b0._84 = 32;
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
          light.r_0c.set(_800c66b0.colour_8c[i].getR() * _800c66b0._84 / 0x100);
          light.g_0d.set(_800c66b0.colour_8c[i].getG() * _800c66b0._84 / 0x100);
          light.b_0e.set(_800c66b0.colour_8c[i].getB() * _800c66b0._84 / 0x100);
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
      _800c66b0._84 = 64;
      _800c66b0._88 = 3;
    } else if(v0 == 3) {
      //LAB_800d2628
      _800c66b0._84 += 36;

      if(_800c66b0._84 > 255) {
        _800c66b0._84 = 255;
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
        light.r_0c.set(_800c66b0.colour_8c[i].getR() * _800c66b0._84 / 0x100);
        light.g_0d.set(_800c66b0.colour_8c[i].getG() * _800c66b0._84 / 0x100);
        light.b_0e.set(_800c66b0.colour_8c[i].getB() * _800c66b0._84 / 0x100);
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

    struct.mapRotation_70.and(0xfff);
    struct.mapRotationEndAngle_7a &= 0xfff;
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
    if(struct.mapRotationStep_7c == 0) {
      struct.mapRotating_80 = 0;
    }

    //LAB_800d3040
    if(struct._110 == 0) {
      if(struct258_800c66a8.zoomState_1f8 == 0) {
        if(struct._c4 == 0) {
          if(_800c6798.get() != 0x7L) {
            final long mapRotating = struct.mapRotating_80;

            if(mapRotating == 0) {
              //LAB_800d30d8
              if((joypadPress_8007a398.get() & 0x8L) != 0) { // R2
                startMapRotation(1);
                struct.mapRotating_80 = 1;
              }

              //LAB_800d310c
              if((joypadPress_8007a398.get() & 0x4L) != 0) { // L2
                startMapRotation(-1);
                struct.mapRotating_80 = 1;
              }

              //LAB_800d3140
            } else if(mapRotating == 1) {
              //LAB_800d3148
              struct.mapRotation_70.y.add((short)struct.mapRotationStep_7c);
              struct.mapRotationCounter_7e++;

              if(struct.mapRotationCounter_7e > 5) {
                struct.mapRotation_70.setY((short)struct.mapRotationEndAngle_7a);
                struct.mapRotating_80 = 0;
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
      _800c66b0.rview2_00.viewpoint_00.setY(_800c66b0.rview2_c8.viewpoint_00.getY() + _800c66b0.viewpointY_ec * _800c66b0._10e);
      _800c66b0.rview2_00.viewpoint_00.setZ(_800c66b0.rview2_c8.viewpoint_00.getZ() + _800c66b0.viewpointZ_f0 * _800c66b0._10e);
      _800c66b0.rview2_00.refpoint_0c.setY(_800c66b0.rview2_c8.refpoint_0c.getY() + _800c66b0.refpointY_f8 * _800c66b0._10e);
      _800c66b0.rview2_00.refpoint_0c.setZ(_800c66b0.rview2_c8.refpoint_0c.getZ() + _800c66b0.refpointZ_fc * _800c66b0._10e);
      _800c66b0.mapRotation_70.setY((short)(_800c66b0._10a + _800c66b0._10c * _800c66b0._10e));

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
    _800c66b0.rview2_00.viewpoint_00.setY(_800c66b0.rview2_c8.viewpoint_00.getY() + _800c66b0.viewpointY_ec * _800c66b0._10e);
    _800c66b0.rview2_00.viewpoint_00.setZ(_800c66b0.rview2_c8.viewpoint_00.getZ() + _800c66b0.viewpointZ_f0 * _800c66b0._10e);
    _800c66b0.rview2_00.refpoint_0c.setY(_800c66b0.rview2_c8.refpoint_0c.getY() + _800c66b0.refpointY_f8 * _800c66b0._10e);
    _800c66b0.rview2_00.refpoint_0c.setZ(_800c66b0.rview2_c8.refpoint_0c.getZ() + _800c66b0.refpointZ_fc * _800c66b0._10e);
    _800c66b0.mapRotation_70.setY((short)(_800c66b0._10a + _800c66b0._10c * _800c66b0._10e));

    _800c66b0._10e++;
    if((short)_800c66b0._10e >= 16) {
      _800c66b0._10e = 16;
      _800c66b0.mapRotation_70.setY((short)_800c66b0._108);
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
      if(_800c6798.get() != 0x7L && _800c6870.get() == 0 && _800c6690.get() == 0) {
        //LAB_800d36a8
        if(_800c6894.get() != 0x1L) {
          if(_800c66b0.mapRotating_80 == 0) {
            if(struct258_800c66a8._05 == 0) {
              if(_800c66b0._110 == 0) {
                if((joypadPress_8007a398.get() & 0x2L) != 0) { // R1
                  if(struct258_800c66a8.zoomState_1f8 == 0) {
                    playSound(0, 4, 0, 0, (short)0, (short)0);
                    _800c66b0._9e = -9000;
                    _800c66b0._c5 = 1;
                    _800c66b0._11a = 1;
                    FUN_800d4bc8(0);
                    _800c6868.setu(0x1L);
                    _800c66b0._c4 = 1;
                  }
                }

                //LAB_800d37bc
                if((joypadPress_8007a398.get() & 0x1L) != 0) { // L1
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
      _800c66b0.rview2_00.viewpoint_00.y.sub(1450);
      _800c66b0.rview2_00.refpoint_0c.y.add(1450);
      _800c66b0.mapRotation_70.setY((short)(_800c66b0._9a + _800c66b0._9c * _800c66b0._a0));
      _800c66b0.vec_b4.add(_800c66b0.vec_a4);
      _800c66b0.coord2_20.coord.transfer.setX(struct258_800c66a8.coord2_34.coord.transfer.getX() - _800c66b0.vec_b4.getX() / 0x100);
      _800c66b0.coord2_20.coord.transfer.setY(struct258_800c66a8.coord2_34.coord.transfer.getY() - _800c66b0.vec_b4.getY() / 0x100);
      _800c66b0.coord2_20.coord.transfer.setZ(struct258_800c66a8.coord2_34.coord.transfer.getZ() - _800c66b0.vec_b4.getZ() / 0x100);
      _800c66b0._a0++;

      if(_800c66b0._a0 >= 6) {
        _800c66b0.rview2_00.viewpoint_00.y.set(_800c66b0._9e);
        _800c66b0.rview2_00.refpoint_0c.y.set(-_800c66b0._9e);
        _800c66b0.mapRotation_70.setY((short)_800c66b0._98);
        _800c66b0.coord2_20.coord.transfer.set(0, 0, 0);
        _800c66b0._c5 = 0;
        struct258_800c66a8.zoomState_1f8 = 1;
      }
    } else if(v0 == 2) {
      //LAB_800d3bd8
      if(struct258_800c66a8._05 == 0) {
        _800c66b0.rview2_00.viewpoint_00.y.add(1450);
        _800c66b0.rview2_00.refpoint_0c.y.sub(1450);
      } else {
        //LAB_800d3c44
        _800c66b0.rview2_00.viewpoint_00.y.add(290);
        _800c66b0.rview2_00.refpoint_0c.y.sub(290);
      }

      //LAB_800d3c8c
      _800c66b0.mapRotation_70.setY((short)(_800c66b0._9a + _800c66b0._9c * _800c66b0._a0));
      _800c66b0.vec_b4.add(_800c66b0.vec_a4);
      _800c66b0.coord2_20.coord.transfer.setX(_800c66b0.vec_b4.getX() >> 8);
      _800c66b0.coord2_20.coord.transfer.setY(_800c66b0.vec_b4.getY() >> 8);
      _800c66b0.coord2_20.coord.transfer.setZ(_800c66b0.vec_b4.getZ() >> 8);
      _800c66b0._a0++;

      long sp18 = 0;
      if(struct258_800c66a8._05 == 0) {
        if(_800c66b0._a0 >= 6) {
          sp18 = 0x1L;
        }

        //LAB_800d3e78
        //LAB_800d3e80
      } else if(_800c66b0._a0 >= 30) {
        sp18 = 0x1L;
      }

      //LAB_800d3ea8
      if(sp18 != 0) {
        _800c66b0.rview2_00.viewpoint_00.setY(_800c66b0._9e);
        _800c66b0.rview2_00.refpoint_0c.setY(-_800c66b0._9e);
        _800c66b0.mapRotation_70.setY((short)_800c66b0._98);
        _800c66b0.coord2_20.coord.transfer.set(struct258_800c66a8.coord2_34.coord.transfer);
        _800c66b0._c5 = 0;
        _800c6868.setu(0);
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
      _800c66b0.mapRotation_70.y.add((short)8);
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
      if((joypadPress_8007a398.get() & 0x1) != 0) { // L2
        FUN_8002a3ec(7, 0);
      }

      //LAB_800d4198
      size = 8;
      v = 48;

      //LAB_800d40e8
    } else if(zoomState == 5) {
      //LAB_800d41b0
      FUN_8002a3ec(7, 0);

      if((joypadPress_8007a398.get() & 0x2) != 0) { // R2
        _800c86f0.setu(0);
      }

      //LAB_800d41e0
      return;
    } else if(zoomState == 6) {
      //LAB_800d4128
      FUN_8002a3ec(7, 0);

      if((joypadPress_8007a398.get() & 0x2) != 0) { // R2
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
    perspectiveTransform(sp0x58, sp0x60, null, null);

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
      for(int sp20 = 0; _800f5a6c.offset(sp20 * 0x2cL).getSigned() != -1; sp20++) {
        //LAB_800d4518
        final int sp7c = (int)_800f5a6c.offset(sp20 * 0x2cL).getSigned();
        final int mask = 0x1 << (sp7c & 0x1f);

        if((gameState_800babc8.scriptFlags2_bc.get(sp7c >>> 5).get() & mask) > 0) {
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
          .pos((int)_800f5a90.offset(sp24 * 0x2cL).get() - 160, (int)_800f5a92.offset(sp24 * 0x2cL).get() - 120, size, size)
          .uv((tickCount_800bb0fc.get() & 0x7) * size, v)
        );

        if(!places_800f0234.get((int)_800f5a94.offset(sp24 * 0x2cL).get()).name_00.isNull()) {
          //LAB_800d4878
          final int x = (int)_800f5a90.offset(sp24 * 0x2cL).get();
          final int y = (int)_800f5a92.offset(sp24 * 0x2cL).get() - 8;

          final IntRef width = new IntRef();
          final IntRef lines = new IntRef();
          measureText(places_800f0234.get((int)_800f5a94.offset(sp24 * 0x2cL).get()).name_00.deref(), width, lines);

          final long v0 = _800c86f0.get();
          if(v0 == 0x1L) {
            //LAB_800d49e4
            textZ_800bdf00.set(14);
            textboxes_800be358[7].z_0c = 14;

            if(width.get() < 4) {
              textboxes_800be358[7].chars_18 = 4;
            } else {
              textboxes_800be358[7].chars_18 = width.get();
            }

            //LAB_800d4a28
            textboxes_800be358[7].lines_1a = lines.get();
            _800c86f0.setu(0x2L);
            //LAB_800d4974
          } else if(v0 == 0) {
            //LAB_800d4988
            FUN_8002a32c(7, 0, x, y, width.get() - 1, lines.get() - 1);
            _800c86f0.setu(0x1L);

            //LAB_800d49e4
            textZ_800bdf00.set(14);
            textboxes_800be358[7].z_0c = 14;

            if(width.get() < 4) {
              textboxes_800be358[7].chars_18 = 4;
            } else {
              textboxes_800be358[7].chars_18 = width.get();
            }

            //LAB_800d4a28
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

          FUN_800e774c(places_800f0234.get((int)_800f5a94.offset(sp24 * 0x2cL).get()).name_00.deref(), x - width.get() * 3, y - lines.get() * 7, 0, 0);
        }
      }
    }
  }

  @Method(0x800d4bc8L)
  public static void FUN_800d4bc8(final int a0) {
    final int sp18;
    final int sp14;
    int sp10;

    final WMapStruct19c0 struct = _800c66b0;

    if(a0 == 0) {
      struct._9a = struct.mapRotation_70.getY();
      struct._98 = 0;
      sp10 = struct._98 - struct._9a;
      sp14 = struct._98 - (struct._9a - 0x1000);
    } else {
      //LAB_800d4c80
      struct._98 = struct._9a;
      struct._9a = struct.mapRotation_70.getY();

      if(struct._9a < struct._98) {
        sp18 = -0x1000;
      } else {
        //LAB_800d4cf8
        sp18 = 0x1000;
      }

      //LAB_800d4d00
      sp10 = struct._98 - struct._9a;
      sp14 = struct._9a - struct._98 + sp18;
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
    struct._9c = sp10 / 6;
    struct._a0 = 0;
  }

  @Method(0x800d4ed8L)
  public static void startMapRotation(final int direction) {
    final WMapStruct19c0 struct = _800c66b0;
    struct.mapRotationCounter_7e = 0;
    struct.mapRotationStartAngle_78 = struct.mapRotation_70.getY();
    struct.mapRotationEndAngle_7a = struct.mapRotation_70.getY() + direction * 0x200;
    int sp10 = -direction * 0x200;
    final int sp14 = sp10 + 0x1000;

    if(Math.abs(sp14) < Math.abs(sp10)) {
      sp10 = sp14;
    }

    //LAB_800d4fd0
    struct.mapRotationStep_7c = -sp10 / 6;
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
    struct.viewpointY_ec = (-100 - struct.rview2_c8.viewpoint_00.getY()) / 16;
    struct.viewpointZ_f0 = (-600 - struct.rview2_c8.viewpoint_00.getZ()) / 16;
    struct.refpointY_f8 = ( -90 - struct.rview2_c8.refpoint_0c.getY()) / 16;
    struct.refpointZ_fc = -struct.rview2_c8.refpoint_0c.getZ() / 16;
    struct._10a = struct.mapRotation_70.getY();

    final int sp18 = struct258_800c66a8.rotation_a4.getY() + 0x800;
    struct._108 = sp18;

    int sp10 = struct.mapRotation_70.getY() - sp18;
    final int sp14 = struct.mapRotation_70.getY() - (sp18 - 0x1000);

    if(Math.abs(sp14) < Math.abs(sp10)) {
      sp10 = sp14;
    }

    //LAB_800d5244
    struct._10c = -sp10 / 16;
  }

  @Method(0x800d5288L)
  public static void FUN_800d5288() {
    final WMapStruct19c0 struct = _800c66b0;
    final int v0 = struct._11a;

    if(v0 == 0) {
      if(struct._154[0].vecLength_04 < 90) {
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
      struct._118 += Math.max(4, 64 - struct._114 * 2);

      if(struct._118 >= 800) {
        struct._118 = 800;
        struct._11a = 0;
      }
    } else if(v0 == 2) {
      //LAB_800d53b4
      struct._114++;

      //LAB_800d5424
      struct._118 += Math.max(4, 64 - struct._114 * 2);

      if(struct._118 >= 800) {
        struct._118 = 800;
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
      struct._118 -= Math.max(4, 64 - struct._114 * 2);

      if(struct._118 <= 600) {
        struct._118 = 600;
        struct._11a = 0;
      }
    } else if(v0 == 4) {
      //LAB_800d54e8
      struct._114++;

      //LAB_800d5558
      struct._118 -= Math.max(4, 64 - struct._114 * 2);

      if(struct._118 <= 600) {
        struct._118 = 600;
        struct._11a = 0;
      }
    }

    setProjectionPlaneDistance(struct._118);
  }

  @Method(0x800d55fcL)
  public static void FUN_800d55fc() {
    _800c66b0 = null;
  }

  @Method(0x800d562cL)
  public static void FUN_800d562c(final long address, final int size, final int param) {
    final McqHeader mcq = MEMORY.ref(4, address, McqHeader::new);
    final int x = 320 + (int)(param & 0xffff_fffeL) * 64;

    final int y;
    if((param & 0x1) != 0) {
      y = 256;
    } else {
      //LAB_800d5688
      y = 0;
    }

    //LAB_800d568c
    final RECT sp0x18 = new RECT(
      (short)x,
      (short)y,
      mcq.vramWidth_08.get(),
      mcq.vramHeight_0a.get()
    );

    LoadImage(sp0x18, mcq.getAddress() + mcq.imageDataOffset_04.get());
    memcpy(mcqHeader_800c6768.getAddress(), mcq.getAddress(), 0x2c);
    deferReallocOrFree(address, 0, 1);

    filesLoadedFlags_800c66b8.oru(0x1L);
  }

  @Method(0x800d5768L)
  public static void FUN_800d5768(final long address, final int size, final int param) {
    final long ix = imageX_800ef0cc.offset(param * 0x8L).getSigned();
    final long iy = imageY_800ef0ce.offset(param * 0x8L).getSigned();
    final long cx = clutX_800ef0d0.offset(param * 0x8L).getSigned();
    final long cy = clutY_800ef0d2.offset(param * 0x8L).getSigned();
    FUN_800d5c50(address, ix, iy, cx, cy);
    free(address);
    filesLoadedFlags_800c66b8.oru(0x800L);

    //LAB_800d5848
  }

  @Method(0x800d5858L) //TODO loads general world map stuff (location text, doors, buttons, etc.), several blobs that may be smoke?, tons of terrain and terrain sprites
  public static void timsLoaded(final List<byte[]> files, final int param) {
    //LAB_800d5874
    for(final byte[] file : files) {
      //LAB_800d5898
      if(file.length != 0) {
        //LAB_800d58c8
        new Tim(file).uploadToGpu();
      }
    }

    //LAB_800d5938
    filesLoadedFlags_800c66b8.oru(param);

    //LAB_800d5970
  }

  @Method(0x800d5984L)
  public static void loadTmdCallback(final long address, final int size, final int param) {
    final TmdWithId tmd = MEMORY.ref(4, address, TmdWithId::new);

    struct258_800c66a8.tmdRendering_08 = loadTmd(tmd);
    initTmdTransforms(struct258_800c66a8.tmdRendering_08, null);
    struct258_800c66a8.tmdRendering_08.tmd_14 = tmd;
    setAllCoord2Attribs(struct258_800c66a8.tmdRendering_08, 0);
    filesLoadedFlags_800c66b8.oru(0x4L);
  }

  @Method(0x800d5a30L)
  public static void FUN_800d5a30(final List<byte[]> files, final int whichFile) {
    final MrgFile mrg = MrgFile.alloc(files, Math.min(files.size(), 16));
    struct258_800c66a8._1b4[whichFile] = mrg;

    if(mrg.entries.get(0).size.get() != 0) {
      struct258_800c66a8._b4[whichFile].extendedTmd_00 = mrg.getFile(0, ExtendedTmd::new);
    }

    if(mrg.entries.get(1).size.get() != 0) {
      struct258_800c66a8._b4[whichFile].unknownFile_04 = mrg.getFile(1);
    }

    //LAB_800d5a48
    for(int i = 2; i < Math.min(16, mrg.count.get()); i++) {
      //LAB_800d5a6c
      if(mrg.entries.get(i).size.get() != 0) {
        //LAB_800d5a9c
        //LAB_800d5ab8
        struct258_800c66a8._b4[whichFile].tmdAnim_08[i - 2] = mrg.getFile(i, TmdAnimationFile::new);
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
  public static void FUN_800d5c50(final long a0, final long imageX, final long imageY, final long clutX, final long clutY) {
    FUN_8003b8f0(a0);
    FUN_8003b900(_800c66c0);

    final RECT rect = new RECT((short)imageX, (short)imageY, _800c66c0.imageRect.deref().w.get(), _800c66c0.imageRect.deref().h.get());
    LoadImage(rect, _800c66c0.imageAddress.get());

    if((_800c66c0.flags.get() & 0x8L) != 0 && (short)clutX != -0x1L) {
      rect.set((short)clutX, (short)clutY, _800c66c0.clutRect.deref().w.get(), _800c66c0.clutRect.deref().h.get());
      LoadImage(rect, _800c66c0.clutAddress.get());
    }

    //LAB_800d5d84
  }

  @Method(0x800d5d98L)
  public static void FUN_800d5d98(final long a0) {
    final TimHeader header = parseTimHeader(MEMORY.ref(4, a0 + 0x4L));
    LoadImage(header.getImageRect(), header.getImageAddress());

    if(header.hasClut()) {
      LoadImage(header.getClutRect(), header.getClutAddress());
    }
  }

  @Method(0x800d5e70L)
  public static long FUN_800d5e70(final RECT sp28, final long a1, final long a2, final long a3) {
    final long sp14 = mallocTail(0x20);
    MEMORY.ref(2, sp14).offset(0x00L).setu(sp28.x.get());
    MEMORY.ref(2, sp14).offset(0x02L).setu(sp28.y.get());
    MEMORY.ref(2, sp14).offset(0x04L).setu(sp28.w.get() / (4 - a1 * 2));
    MEMORY.ref(2, sp14).offset(0x06L).setu(sp28.h.get());
    MEMORY.ref(4, sp14).offset(0x08L).setu(mallocTail(sp28.w.get() / (2 - a1) * sp28.h.get()));
    MEMORY.ref(4, sp14).offset(0x0cL).setu(mallocTail(sp28.w.get() / (2 - a1) * sp28.h.get()));
    MEMORY.ref(4, sp14).offset(0x10L).setu(a2);
    MEMORY.ref(4, sp14).offset(0x14L).setu(a1);
    MEMORY.ref(2, sp14).offset(0x18L).setu(a3);
    MEMORY.ref(2, sp14).offset(0x1aL).setu((int)a2 / 2 * 2);
    MEMORY.ref(2, sp14).offset(0x1cL).setu(MEMORY.ref(2, sp14).offset(0x1aL).get());
    return sp14;
  }

  @Method(0x800d6080L)
  public static void animateTextures(final long a0) {
    if(MEMORY.ref(2, a0).offset(0x18L).getSigned() == 0) {
      return;
    }

    //LAB_800d60b0
    MEMORY.ref(2, a0).offset(0x1cL).addu(0x1L);

    if(MEMORY.ref(2, a0).offset(0x1cL).getSigned() < MEMORY.ref(2, a0).offset(0x1aL).getSigned()) {
      return;
    }

    final RECT sp0x10 = new RECT();
    final RECT sp0x18 = new RECT();
    final RECT sp0x20 = new RECT();
    final RECT sp0x28 = new RECT();

    //LAB_800d60f8
    MEMORY.ref(2, a0).offset(0x1cL).setu(0);

    if((MEMORY.ref(4, a0).offset(0x10L).get() & 0x1L) == 0) {
      MEMORY.ref(2, a0).offset(0x18L).setu(MEMORY.ref(2, a0).offset(0x18L).getSigned() % MEMORY.ref(2, a0).offset(0x4L).getSigned());

      if(MEMORY.ref(2, a0).offset(0x18L).getSigned() > 0) {
        sp0x10.set(
          (short)(MEMORY.ref(2, a0).offset(0x0L).get() + MEMORY.ref(2, a0).offset(0x4L).get() - MEMORY.ref(2, a0).offset(0x18L).get()),
          (short)MEMORY.ref(2, a0).offset(0x2L).get(),
          (short)MEMORY.ref(2, a0).offset(0x18L).get(),
          (short)MEMORY.ref(2, a0).offset(0x6L).get()
        );

        sp0x18.set(
          (short)MEMORY.ref(2, a0).offset(0x0L).get(),
          (short)MEMORY.ref(2, a0).offset(0x2L).get(),
          (short)(MEMORY.ref(2, a0).offset(0x4L).get() - MEMORY.ref(2, a0).offset(0x18L).get()),
          (short)MEMORY.ref(2, a0).offset(0x6L).get()
        );

        sp0x20.set(
          (short)MEMORY.ref(2, a0).offset(0x0L).get(),
          (short)MEMORY.ref(2, a0).offset(0x2L).get(),
          (short)MEMORY.ref(2, a0).offset(0x18L).get(),
          (short)MEMORY.ref(2, a0).offset(0x6L).get()
        );

        sp0x28.set(
          (short)(MEMORY.ref(2, a0).offset(0x0L).get() + MEMORY.ref(2, a0).offset(0x18L).get()),
          (short)MEMORY.ref(2, a0).offset(0x2L).get(),
          (short)(MEMORY.ref(2, a0).offset(0x4L).get() - MEMORY.ref(2, a0).offset(0x18L).get()),
          (short)MEMORY.ref(2, a0).offset(0x6L).get()
        );
      } else {
        //LAB_800d62e4
        sp0x10.set(
          (short)MEMORY.ref(2, a0).offset(0x0L).get(),
          (short)MEMORY.ref(2, a0).offset(0x2L).get(),
          (short)-MEMORY.ref(2, a0).offset(0x18L).get(),
          (short)MEMORY.ref(2, a0).offset(0x6L).get()
        );

        sp0x18.set(
          (short)(MEMORY.ref(2, a0).offset(0x0L).get() - MEMORY.ref(2, a0).offset(0x18L).get()),
          (short)MEMORY.ref(2, a0).offset(0x2L).get(),
          (short)(MEMORY.ref(2, a0).offset(0x4L).get() + MEMORY.ref(2, a0).offset(0x18L).get()),
          (short)MEMORY.ref(2, a0).offset(0x6L).get()
        );

        sp0x20.set(
          (short)(MEMORY.ref(2, a0).offset(0x0L).get() + MEMORY.ref(2, a0).offset(0x4L).get() + MEMORY.ref(2, a0).offset(0x18L).get()),
          (short)MEMORY.ref(2, a0).offset(0x2L).get(),
          (short)-MEMORY.ref(2, a0).offset(0x18L).get(),
          (short)MEMORY.ref(2, a0).offset(0x6L).get()
        );

        sp0x28.set(
          (short)MEMORY.ref(2, a0).offset(0x0L).get(),
          (short)MEMORY.ref(2, a0).offset(0x2L).get(),
          (short)(MEMORY.ref(2, a0).offset(0x4L).get() + MEMORY.ref(2, a0).offset(0x18L).get()),
          (short)MEMORY.ref(2, a0).offset(0x6L).get()
        );
      }

      //LAB_800d6460
    } else {
      //LAB_800d6468
      MEMORY.ref(2, a0).offset(0x18L).setu(MEMORY.ref(2, a0).offset(0x18L).getSigned() % MEMORY.ref(2, a0).offset(0x6L).getSigned());

      if(MEMORY.ref(2, a0).offset(0x18L).getSigned() > 0) {
        sp0x10.set(
          (short)MEMORY.ref(2, a0).offset(0x0L).get(),
          (short)(MEMORY.ref(2, a0).offset(0x2L).get() + MEMORY.ref(2, a0).offset(0x6L).get() - MEMORY.ref(2, a0).offset(0x18L).get()),
          (short)MEMORY.ref(2, a0).offset(0x4L).get(),
          (short)MEMORY.ref(2, a0).offset(0x18L).get()
        );

        sp0x18.set(
          (short)MEMORY.ref(2, a0).offset(0x0L).get(),
          (short)MEMORY.ref(2, a0).offset(0x2L).get(),
          (short)MEMORY.ref(2, a0).offset(0x4L).get(),
          (short)(MEMORY.ref(2, a0).offset(0x6L).get() - MEMORY.ref(2, a0).offset(0x18L).get())
        );

        sp0x20.set(
          (short)MEMORY.ref(2, a0).offset(0x0L).get(),
          (short)MEMORY.ref(2, a0).offset(0x2L).get(),
          (short)MEMORY.ref(2, a0).offset(0x4L).get(),
          (short)MEMORY.ref(2, a0).offset(0x18L).get()
        );

        sp0x28.set(
          (short)MEMORY.ref(2, a0).offset(0x0L).get(),
          (short)(MEMORY.ref(2, a0).offset(0x2L).get() + MEMORY.ref(2, a0).offset(0x18L).get()),
          (short)MEMORY.ref(2, a0).offset(0x4L).get(),
          (short)(MEMORY.ref(2, a0).offset(0x6L).get() - MEMORY.ref(2, a0).offset(0x18L).get())
        );
      } else {
        //LAB_800d662c
        sp0x10.set(
          (short)MEMORY.ref(2, a0).offset(0x0L).get(),
          (short)MEMORY.ref(2, a0).offset(0x2L).get(),
          (short)MEMORY.ref(2, a0).offset(0x4L).get(),
          (short)-MEMORY.ref(2, a0).offset(0x18L).get()
        );

        sp0x18.set(
          (short)MEMORY.ref(2, a0).offset(0x0L).get(),
          (short)(MEMORY.ref(2, a0).offset(0x2L).get() - MEMORY.ref(2, a0).offset(0x18L).get()),
          (short)MEMORY.ref(2, a0).offset(0x4L).get(),
          (short)(MEMORY.ref(2, a0).offset(0x6L).get() + MEMORY.ref(2, a0).offset(0x18L).get())
        );

        sp0x20.set(
          (short)MEMORY.ref(2, a0).offset(0x0L).get(),
          (short)(MEMORY.ref(2, a0).offset(0x2L).get() + MEMORY.ref(2, a0).offset(0x6L).get() + MEMORY.ref(2, a0).offset(0x18L).get()),
          (short)MEMORY.ref(2, a0).offset(0x4L).get(),
          (short)-MEMORY.ref(2, a0).offset(0x18L).get()
        );

        sp0x28.set(
          (short)MEMORY.ref(2, a0).offset(0x0L).get(),
          (short)MEMORY.ref(2, a0).offset(0x2L).get(),
          (short)MEMORY.ref(2, a0).offset(0x4L).get(),
          (short)(MEMORY.ref(2, a0).offset(0x6L).get() + MEMORY.ref(2, a0).offset(0x18L).get())
        );
      }
    }

    //LAB_800d67a8
    StoreImage(sp0x10, MEMORY.ref(4, a0).offset(0xcL).get());
    StoreImage(sp0x18, MEMORY.ref(4, a0).offset(0x8L).get());
    LoadImage(sp0x20, MEMORY.ref(4, a0).offset(0xcL).get());
    LoadImage(sp0x28, MEMORY.ref(4, a0).offset(0x8L).get());

    //LAB_800d6804
  }

  @Method(0x800d6818L)
  public static void FUN_800d6818(final long a0) {
    free(MEMORY.ref(4, a0).offset(0xcL).get());
    free(MEMORY.ref(4, a0).offset(0x8L).get());
    free(a0);
  }

  @Method(0x800d6880L)
  public static void FUN_800d6880() {
    filesLoadedFlags_800c66b8.and(0xffff_efffL);
    loadDrgnDir(0, 5695, files -> WMap.timsLoaded(files, 0x1_1000));
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
      .uv(128, (int)_800c6798.get() * 24)
    );

    struct258_800c66a8.colour_20 += 0x10;

    if(struct258_800c66a8.colour_20 > 0x80) {
      struct258_800c66a8.colour_20 = 0x80;
    }

    //LAB_800d6b5c
    renderPath();

    if(_800c6798.get() == 0x7L) {
      return;
    }

    //LAB_800d6b80
    if(_800c6870.get() != 0) {
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
    final SVECTOR sp9c = new SVECTOR();
    int spac = 0;
    int spae = 0;

    final MATRIX sp0x18 = new MATRIX();
    final VECTOR sp0x40 = new VECTOR();
    final SVECTOR sp0x70 = new SVECTOR();
    final long[] sp0xb0 = new long[0xff];

    if(worldMapState_800c6698.get() < 4 || playerState_800c669c.get() < 4) {
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
    for(int i = 0; i < _800c67a0.get(); i++) {
      //LAB_800d7d90
      if(FUN_800eb09c(i, 1, sp0x40) == 0) {
        //LAB_800d7db4
        if(_800c6798.get() != 7 || i == 31 || i == 78) {
          //LAB_800d7df0
          sp0x70.set(sp0x40);

          CPU.MTC2(sp0x70.getXY(), 0);
          CPU.MTC2(sp0x70.getZ(), 1);
          CPU.COP2(0x18_0001L); // Perspective transform single

          sp9c.setXY(CPU.MFC2(14));
          final int screenZ = (int)CPU.MFC2(19) >> 2;

          if(screenZ >= 4 && screenZ < orderingTableSize_1f8003c8.get()) {
            final GpuCommandPoly cmd = new GpuCommandPoly(4)
              .bpp(Bpp.BITS_4)
              .translucent(Translucency.B_PLUS_F)
              .clut(640, 496)
              .vramPos(640, 256);

            if(struct258_800c66a8.zoomState_1f8 == 0) {
              final int dx = x - sp0x70.getX();
              final int dy = y - sp0x70.getY();
              final int dz = z - sp0x70.getZ();
              final int sp90 = Math.max(0, 0x200 - SquareRoot0(dx * dx + dy * dy + dz * dz)) / 2;
              cmd.rgb(sp90 * 31 / 256, sp90 * 63 / 256, 0);
            } else {
              //LAB_800d8048
              cmd.rgb(31, 63, 0);
            }

            //LAB_800d806c
            spac = sp9c.getX();
            spae = sp9c.getY();

            cmd
              .uv(0, u, v)
              .uv(1, u + uw, v)
              .uv(2, u, v + vw)
              .uv(3, u + uw, v + vw)
              .pos(0, spac - ((int)_800ef170.offset(sp98 * 4 + sp94 + 2).get() >>> 2), spae - ((int)_800ef170.offset(sp98 * 4 + sp94 + 3).get() >>> 2))
              .pos(1, spac - ((int)_800ef170.offset(sp98 * 4 + sp94 + 2).get() >>> 2) + ((int)_800ef170.offset(sp98 * 4 + sp94 + 2).get() >>> 1), spae - ((int)_800ef170.offset(sp98 * 4 + sp94 + 3).get() >>> 2))
              .pos(2, spac - ((int)_800ef170.offset(sp98 * 4 + sp94 + 2).get() >>> 2), spae - ((int)_800ef170.offset(sp98 * 4 + sp94 + 3).get() >>> 2) + ((int)_800ef170.offset(sp98 * 4 + sp94 + 2).get() >>> 1))
              .pos(3, spac - ((int)_800ef170.offset(sp98 * 4 + sp94 + 2).get() >>> 2) + ((int)_800ef170.offset(sp98 * 4 + sp94 + 2).get() >>> 1), spae - ((int)_800ef170.offset(sp98 * 4 + sp94 + 3).get() >>> 2) + ((int)_800ef170.offset(sp98 * 4 + sp94 + 2).get() >>> 1));

            GPU.queueCommand(10 + screenZ, cmd);
          }

          //LAB_800d84b0
        }
      }

      //LAB_800d84c0
    }

    //LAB_800d84d8
    //LAB_800d84e8
    for(int i = 0; i < 0xff; i++) {
      //LAB_800d8504
      sp0xb0[i] = 0;
    }

    //LAB_800d852c
    //LAB_800d8540
    for(int i = 0; i < _800c67a0.get(); i++) {
      //LAB_800d8564
      if(FUN_800eb09c(i, 0, null) == 0) {
        //LAB_800d8584
        if(_800c6798.get() != 7 || i == 31 || i == 78) {
          //LAB_800d85c0
          final int sp88 = areaData_800f2248.get(_800f0e34.get(i).areaIndex_00.get())._00.get();
          final int sp80 = Math.abs(sp88) - 1;

          if(sp0xb0[sp80] == 0) {
            //LAB_800d863c
            sp0xb0[sp80] = 1;
            final int pathPointCount = (int)_800f5810.offset(sp80 * 4).get() - 1;

            final UnboundedArrayRef<VECTOR> pathPoints = pathDotPosPtrArr_800f591c.get(sp80).deref();
            final int pathPointIndexBase = sp88 >= 0 ? 0 : pathPointCount - 1;

            //LAB_800d86d0
            //LAB_800d86d4
            for(int pathPointIndex = 0; pathPointIndex < pathPointCount; pathPointIndex++) {
              //LAB_800d86f4
              if(sp88 > 0) {
                sp0x70.set(pathPoints.get(pathPointIndexBase + pathPointIndex));
              } else {
                //LAB_800d8784
                sp0x70.set(pathPoints.get(pathPointIndexBase - pathPointIndex));
              }

              //LAB_800d87fc
              CPU.MTC2(sp0x70.getXY(), 0);
              CPU.MTC2(sp0x70.getZ(),  1);
              CPU.COP2(0x18_0001L);

              sp9c.setXY(CPU.MFC2(14));
              final int screenZ = (int)CPU.MFC2(19) >> 2;

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
                    .pos(0, spac - 2, spae - 2)
                    .pos(1, spac + 2, spae - 2)
                    .pos(2, spac - 2, spae + 2)
                    .pos(3, spac + 2, spae + 2)
                    .uv(0, 48, 0)
                    .uv(1, 63, 0)
                    .uv(2, 48, 15)
                    .uv(3, 63, 15);
                } else {
                  //LAB_800d8b40
                  cmd
                    .rgb(0x2f, 0x27, 0)
                    .pos(0, spac - 1, spae - 1)
                    .pos(1, spac + 2, spae - 2)
                    .pos(2, spac - 1, spae + 2)
                    .pos(3, spac + 2, spae + 2)
                    .uv(0, 16, 24)
                    .uv(1, 23, 24)
                    .uv(2, 16, 31)
                    .uv(3, 23, 31);
                }

                //LAB_800d8c64
                spac = sp9c.getX();
                spae = sp9c.getY();

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
    FUN_800d8e4c((int)_800c6798.get());

    struct258_800c66a8.zoomState_1f8 = 0;
    struct258_800c66a8._220 = 0;

    final Memory.TemporaryReservation sp0x48tmp = MEMORY.temp(4);
    final Memory.TemporaryReservation sp0x50tmp = MEMORY.temp(8);
    final Memory.TemporaryReservation sp0x58tmp = MEMORY.temp(8);
    final COLOUR sp0x48 = sp0x48tmp.get().cast(COLOUR::new);
    final Value sp0x50 = sp0x50tmp.get();
    final Value sp0x58 = sp0x58tmp.get();

    MEMORY.memfill(sp0x48.getAddress(), 0x4, 0);

    sp0x50.setu(_800c8778);
    sp0x58.offset(0x0L).setu(_800c877c.offset(0x0L));
    sp0x58.offset(0x4L).setu(_800c877c.offset(0x4L));

    struct258_800c66a8._1fc = FUN_800cd3c8(
      0x80L,
      sp0x48,
      sp0x48,
      sp0x48,
      sp0x48,
      sp0x50.getAddress(),
      sp0x58.getAddress(),
      0x4L,
      0x4L,
      0x2L,
      true,
      Translucency.B_PLUS_F,
      0x9L,
      13,
      0,
      0,
      0
    );

    sp0x48tmp.release();
    sp0x50tmp.release();
    sp0x58tmp.release();
  }

  @Method(0x800d8e4cL)
  public static void FUN_800d8e4c(final int index) {
    filesLoadedFlags_800c66b8.and(0xffff_fffdL);
    loadDrgnDir(0, 5697 + index, files -> WMap.timsLoaded(files, 0x2));
    loadDrgnBinFile(0, 5705 + index, 0, WMap::loadTmdCallback, 0, 0x2L);
  }

  @Method(0x800d8efcL)
  public static void FUN_800d8efc() {
    final RECT sp0x10 = new RECT((short)448, (short)0, (short)64, (short)64);
    struct258_800c66a8.ptr_1c = FUN_800d5e70(sp0x10, 0, 0x3L, 0x1L);
    struct258_800c66a8._28 = 0;

    if(_800c6798.get() == 2) {
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
      final GsDOBJ2 dobj2 = struct258_800c66a8.tmdRendering_08.dobj2s_00[i];
      final GsCOORDINATE2 coord2 = struct258_800c66a8.tmdRendering_08.coord2s_04[i];
      final SVECTOR rotation = struct258_800c66a8.tmdRendering_08.rotations_08[i];

      //LAB_800d9180
      if(_800c6798.get() != 0x7L) {
        //LAB_800d91cc
        if(_800ef194.offset(_800c6798.get()).get() == i || _800ef19c.offset(_800c6798.get()).get() == i) {
          zOffset_1f8003e8.set(500);
        } else {
          //LAB_800d9204
          zOffset_1f8003e8.set(100);
        }
      }

      //LAB_800d9210
      rotateCoord2(rotation, coord2);

      if(_800c6798.get() == 0x2L) {
        //LAB_800d9264
        if(i >= 2 && i < 9 || i >= 15 && i < 17) {
          //LAB_800d9294
          final int sin = rsin(struct258_800c66a8.tmdRendering_08._10[i]) * 0x20 / 0x1000;
          if((i & 0x1L) != 0) {
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

      if((int)_800c6798.get() < 0x9L && i == 0) {
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
    if((int)_800c6798.get() < 0x9L) {
      animateTextures(struct258_800c66a8.ptr_1c);
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
    if(_800c6798.get() == 0x7L) {
      return;
    }

    //LAB_800d955c
    switch(struct258_800c66a8.zoomState_1f8) {
      case 1, 6:
        if((joypadPress_8007a398.get() & 0x2L) != 0) { // Zoom out
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
          _800c66b0.coord2_20.coord.transfer.set(vec_800ef1a8.get((int)_800c6798.get()));
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
        if((joypadPress_8007a398.get() & 0x2L) != 0) { // Can't zoom out more
          playSound(0, 40, 0, 0, (short)0, (short)0);
        }

        //LAB_800d9858
        //LAB_800d985c
        for(int i = 0; i < 6; i++) {
          //LAB_800d9878
          FUN_8002a3ec(i, 0);
        }

        //LAB_800d98a8
        if((joypadPress_8007a398.get() & 0x1L) != 0) { // Zoom in
          playSound(0, 4, 0, 0, (short)0, (short)0);
          FUN_800d9d24(-1);

          struct258_800c66a8.zoomState_1f8 = 5;

          //LAB_800d9900
          for(int i = 0; i < 3; i++) {
            //LAB_800d991c
            //LAB_800d996c
            //LAB_800d99c4
            //LAB_800d9a1c
            _800c66b0.lights_11c[i].r_0c.set(_800c66b0.colour_8c[i].r.get() / 4);
            _800c66b0.lights_11c[i].g_0d.set(_800c66b0.colour_8c[i].g.get() / 4);
            _800c66b0.lights_11c[i].b_0e.set(_800c66b0.colour_8c[i].b.get() / 4);

            GsSetFlatLight(i, _800c66b0.lights_11c[i]);
          }

          //LAB_800d9a70
          if((joypadInput_8007a39c.get() & 0x800L) != 0) {
            //LAB_800d9a8c
            for(int i = 0; i < 8; i++) {
              //LAB_800d9aa8
              _800c86d4.offset(i * 0x4L).setu(0);
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
    renderMcq(mcqHeader_800c6768, 320, 0, -160, -120, 30, 1, (int)_800ef1a4.get() & 0xff);

    //LAB_800d9d10
  }

  /**
   * @param zoomDirection -1 or +1
   */
  @Method(0x800d9d24L)
  public static void FUN_800d9d24(final int zoomDirection) {
    final VECTOR vec = vec_800ef1a8.get((int)_800c6798.get());
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
    final long a0;
    long sp24;
    final VECTOR sp0x38 = new VECTOR();
    final VECTOR sp0x48 = new VECTOR();
    long sp58;
    final long sp5c;
    long sp60;

    if(_800c6894.get() == 0x1L) {
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
      sp58 = 0x97;
      sp5c = 1 << (sp58 & 0x1f);
      sp58 = sp58 >>> 5;

      if((gameState_800babc8.scriptFlags2_bc.get((int)sp58).get() & sp5c) != 0 && _800c6870.get() == 0) {
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
    sp60 = 0x15aL;
    sp5c = 1 << (sp60 & 0x1fL);
    sp60 = sp60 >>> 5;

    if((gameState_800babc8.scriptFlags2_bc.get((int)sp60).get() & sp5c) == 0) {
      return;
    }

    //LAB_800da4ec
    renderQueenFuryUi(0);

    if((joypadPress_8007a398.get() & 0x80L) != 0) { // Square
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

        struct258.svec_208.setX((short)(struct258.vec_94.getX() >> 12));
        struct258.svec_208.setY((short)(struct258.vec_94.getY() >> 12));
        struct258.svec_208.setZ((short)(struct258.vec_94.getZ() >> 12));

        struct258._21c = struct258.rotation_a4.getY();
        struct258._21e = _800c66b0.mapRotation_70.getY();
        struct258._223 = 0;
        struct258._220 = 1;
        struct258.models_0c[2].coord2Param_64.rotate.set((short)0, struct258.rotation_a4.getY(), (short)0);
        struct258.models_0c[2].scaleVector_fc.setX(0x400);
        struct258.coord2_34.coord.transfer.setX(struct258.vec_94.getX() >> 12);
        struct258.coord2_34.coord.transfer.setY(struct258.vec_94.getY() >> 12);
        struct258.coord2_34.coord.transfer.setZ(struct258.vec_94.getZ() >> 12);
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
          _800c86d4.offset(i * 0x4L).setu(0);
        }

        //LAB_800da940
        if((tickCount_800bb0fc.get() & 0x3L) == 0) {
          playSound(12, 1, 0, 0, (short)0, (short)0);
        }

        //LAB_800da978
        break;

      case 2:
        renderWinglyTeleportScreenEffect();

        struct258.models_0c[2].scaleVector_fc.y.add(0x40);

        if(struct258.models_0c[2].scaleVector_fc.getX() > 0x600) {
          struct258.models_0c[2].scaleVector_fc.setX(0x600);
        }

        //LAB_800da9fc
        a0 = struct258.models_0c[2].scaleVector_fc.getX();
        struct258.models_0c[2].scaleVector_fc.setY((int)a0);
        struct258.models_0c[2].scaleVector_fc.setZ((int)a0);
        struct258.vec_94.y.sub(0x6_0000);

        _800c66b0.coord2_20.coord.transfer.y.sub(0x60);

        if(_800c66b0.coord2_20.coord.transfer.getY() < -1500) {
          _800c66b0.coord2_20.coord.transfer.setY(-1500);
        }

        //LAB_800daab8
        if(struct258.vec_94.getY() < -2512) {
          struct258.vec_94.setY(-2500);
        }

        //LAB_800daaf0
        if(struct258.vec_94.getY() <= -2500) {
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
        struct258.models_0c[2].scaleVector_fc.set(0, 0, 0);
        struct258.models_0c[2].coord2Param_64.rotate.set((short)0x400, (short)0x800, (short)0);

        _800c66b0.mapRotation_70.setY((short)0);
        _800c66b0.coord2_20.coord.transfer.set(720, -1500, 628);
        _800c66b0._11a = 3;

        sp24 = 0;

        //LAB_800dac80
        for(int i = 0; i < 9; i++) {
          //LAB_800dac9c
          if(_800f0e34.get((int)coolonWarpDest_800ef228.get(i)._10.get())._0e.get() == _800c6798.get() + 1) {
            struct258.coolonWarpIndex_221 = i;
            sp24 = 0x1L;
            break;
          }

          //LAB_800dad14
        }

        //LAB_800dad2c
        if(sp24 == 0) {
          struct258.coolonWarpIndex_221 = 8;
        }

        //LAB_800dad4c
        if(_800c6798.get() == 0x4L) {
          if(struct258.vec_94.getZ() >> 12 < -400) {
            struct258.coolonWarpIndex_221 = 5;
          } else {
            //LAB_800dad9c
            struct258.coolonWarpIndex_221 = 6;
          }
        }

        //LAB_800dadac
        struct258.coolonWarpIndex_222 = coolonWarpDest_800ef228.get(struct258.coolonWarpIndex_221)._14.get();
        struct258._220 = 3;
        struct258.vec_94.set(coolonWarpDest_800ef228.get(struct258.coolonWarpIndex_221).vec_00);
        break;

      case 4:
        if((joypadPress_8007a398.get() & 0xc0L) != 0) {
          playSound(0, 3, 0, 0, (short)0, (short)0);

          //LAB_800daef8
          for(int i = 0; i < 8; i++) {
            //LAB_800daf14
            FUN_8002a3ec(i, 0);
          }

          //LAB_800daf44
          if(struct258._254 != 0) {
            _800c6860.setu(_800f0e34.get((int)_800c67a8.get())._08.get());
            _800c6862.setu(_800f0e34.get((int)_800c67a8.get())._0a.get());
            submapCut_80052c30.set((int)_800c6860.get());
            submapScene_80052c34.set((int)_800c6862.get());

            FUN_800e3fac(1);
          } else {
            //LAB_800daff4
            struct258._220 = 10;
          }

          //LAB_800db004
          break;
        }

        //LAB_800db00c
        if((joypadPress_8007a398.get() & 0x20L) != 0) {
          playSound(0, 2, 0, 0, (short)0, (short)0);
          FUN_8002a32c(6, 1, 240, 64, 9, 4);
          struct258._220 = 4;
        }

        //LAB_800db07c
        struct258.models_0c[2].scaleVector_fc.x.add(0x200);

        if(struct258.models_0c[2].scaleVector_fc.getX() > 0x800) {
          struct258.models_0c[2].scaleVector_fc.setX(0x800);
        }

        //LAB_800db0f0
        a0 = struct258.models_0c[2].scaleVector_fc.getX();
        struct258.models_0c[2].scaleVector_fc.setY((int)a0);
        struct258.models_0c[2].scaleVector_fc.setZ((int)a0);

        renderCoolonMap(0x1L, 0x1L);
        break;

      case 5:
        struct258.models_0c[2].scaleVector_fc.set(0x800, 0x800, 0x800);

        if(FUN_8002a488(6) != 0) {
          struct258._220 = 5;
          struct258._223 = 0;
          struct258._218 = 0;
        }

        //LAB_800db1d8
        renderCoolonMap(0, 0);
        break;

      case 6:
        textboxes_800be358[6].z_0c = 18;

        final IntRef width = new IntRef();
        final IntRef lines = new IntRef();
        measureText(Move_800f00e8, width, lines);
        FUN_800e774c(Move_800f00e8, (short)(240 - width.get() * 3), 41, 0, 0);
        measureText(No_800effa4, width, lines);
        FUN_800e774c(No_800effa4, (short)(240 - width.get() * 3), 57, 0, 0);
        measureText(Yes_800effb0, width, lines);
        FUN_800e774c(Yes_800effb0, (short)(240 - width.get() * 3), 73, 0, 0);
        renderCoolonMap(0, 0);

        if((joypadPress_8007a398.get() & 0x40L) != 0) {
          playSound(0, 3, 0, 0, (short)0, (short)0);
          FUN_8002a3ec(6, 1);
          struct258._220 = 3;
        }

        //LAB_800db39c
        if((joypadPress_8007a398.get() & 0x5000L) != 0) {
          playSound(0, 1, 0, 0, (short)0, (short)0);
          struct258._223 ^= 1;
        }

        //LAB_800db3f8
        if((joypadPress_8007a398.get() & 0x20L) != 0) {
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
        struct258._1fc.y_3a.set(struct258._223 * 0x10);

        FUN_800ce4dc(struct258._1fc);
        break;

      case 7:
        sp0x38.setX(coolonWarpDest_800ef228.get(struct258.coolonWarpIndex_221).vec_00.getX() >> 12);
        sp0x38.setY(coolonWarpDest_800ef228.get(struct258.coolonWarpIndex_221).vec_00.getY() >> 12);
        sp0x38.setZ(coolonWarpDest_800ef228.get(struct258.coolonWarpIndex_221).vec_00.getZ() >> 12);
        sp0x48.setX(coolonWarpDest_800ef228.get(struct258.coolonWarpIndex_222).vec_00.getX() >> 12);
        sp0x48.setY(coolonWarpDest_800ef228.get(struct258.coolonWarpIndex_222).vec_00.getY() >> 12);
        sp0x48.setZ(coolonWarpDest_800ef228.get(struct258.coolonWarpIndex_222).vec_00.getZ() >> 12);

        struct258._218++;

        if(struct258._218 > 12) {
          struct258._218 = 12;
          struct258._220 = 7;
        }

        //LAB_800db698
        FUN_800dcc20(struct258.vec_94, sp0x38, sp0x48, 12, struct258._218);

        struct258.models_0c[2].scaleVector_fc.x.sub(170);

        if(struct258.models_0c[2].scaleVector_fc.getX() < 0) {
          struct258.models_0c[2].scaleVector_fc.setX(0);
        }

        //LAB_800db74c
        a0 = struct258.models_0c[2].scaleVector_fc.getX();
        struct258.models_0c[2].scaleVector_fc.setY((int)a0);
        struct258.models_0c[2].scaleVector_fc.setZ((int)a0);

        renderCoolonMap(0, 0);
        break;

      case 8:
        FUN_80019c80(12, 1, 1);

        if(struct258.coolonWarpIndex_222 == 8) {
          sp60 = coolonWarpDest_800ef228.get(struct258.coolonWarpIndex_222)._10.get();
          gameState_800babc8._17c.get((int)(sp60 >>> 5)).or(0x1 << (sp60 & 0x1f));

          //LAB_800db8f4
          _800c6860.setu(_800f0e34.get((int)coolonWarpDest_800ef228.get(struct258.coolonWarpIndex_222)._10.get())._08.get());
          _800c6862.setu(_800f0e34.get((int)coolonWarpDest_800ef228.get(struct258.coolonWarpIndex_222)._10.get())._0a.get());
          submapCut_80052c30.set((int)_800c6860.get());
          submapScene_80052c34.set((int)_800c6862.get());
        } else {
          //LAB_800db9bc
          _800c6860.setu(_800f0e34.get((int)coolonWarpDest_800ef228.get(struct258.coolonWarpIndex_222)._10.get()).submapCut_04.get());
          _800c6862.setu(_800f0e34.get((int)coolonWarpDest_800ef228.get(struct258.coolonWarpIndex_222)._10.get())._06.get());
          submapCut_80052c30.set((int)_800c6860.get());
          index_80052c38.set((int)_800c6862.get());
          struct258._250 = 3;
          previousMainCallbackIndex_8004dd28.set(-1);
        }

        //LAB_800dba98

        FUN_800e3fac(1);
        renderCoolonMap(0, 0);
        break;

      case 0xb:
        _800c66b0.coord2_20.coord.transfer.set(struct258.svec_200);
        _800c66b0.coord2_20.coord.transfer.setY(-1500);
        struct258.vec_94.setX(struct258.svec_208.getX() << 12);
        struct258.vec_94.setY(struct258.svec_208.getY() << 12);
        struct258.vec_94.setZ(struct258.svec_208.getZ() << 12);
        struct258.vec_94.setY(-5000 << 12);
        struct258.rotation_a4.setY((short)struct258._21c);
        _800c66b0.mapRotation_70.setY((short)struct258._21e);
        struct258.models_0c[2].coord2Param_64.rotate.set((short)0, struct258.rotation_a4.getY(), (short)0);
        struct258.models_0c[2].scaleVector_fc.setX(0x600);
        a0 = struct258.models_0c[2].scaleVector_fc.getX();
        struct258.models_0c[2].scaleVector_fc.setY((int)a0);
        struct258.models_0c[2].scaleVector_fc.setZ((int)a0);
        struct258._220 = 11;

        FUN_80019c80(12, 1, 1);

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
          struct258.vec_94.setY(-400);
        }

        //LAB_800dbdb8
        _800ef1a4.subu(0x1L);

        if(_800ef1a4.getSigned() < 0) {
          _800ef1a4.setu(0);
        }

        //LAB_800dbdec
        break;

      case 0xd:
        struct258.vec_94.y.add(0x1_0000);

        if(struct258.svec_208.getY() << 12 < struct258.vec_94.getY()) {
          struct258.vec_94.setY(struct258.svec_208.getY() << 12);
        }

        //LAB_800dbe70
        if(struct258.svec_208.getY() << 12 <= struct258.vec_94.getY()) {
          struct258._220 = -1;
        }

        //LAB_800dbeb4
        struct258.models_0c[2].scaleVector_fc.x.sub(16);

        if(struct258.models_0c[2].scaleVector_fc.getX() < 1024) {
          struct258.models_0c[2].scaleVector_fc.setZ(1024);
        }

        //LAB_800dbf28
        final int x = struct258.models_0c[2].scaleVector_fc.getX();
        struct258.models_0c[2].scaleVector_fc.setY(x);
        struct258.models_0c[2].scaleVector_fc.setZ(x);

        _800ef1a4.subu(0x1L);

        if(_800ef1a4.getSigned() < 0) {
          _800ef1a4.setu(0);
        }

        //LAB_800dbfa0
        break;

      case 0:
        _800ef1a4.setu(0);

        _800c66b0.coord2_20.coord.transfer.set(struct258.svec_200);

        struct258.vec_94.setX(struct258.svec_208.getX() << 12);
        struct258.vec_94.setY(struct258.svec_208.getY() << 12);
        struct258.vec_94.setZ(struct258.svec_208.getZ() << 12);
        struct258.rotation_a4.setY((short)struct258._21c);

        _800c66b0.mapRotation_70.setY((short)struct258._21e);

        struct258._250 = 0;
        struct258._220 = 0;
        return;
    }

    //LAB_800dc114
    renderMcq(mcqHeader_800c6768, 320, 0, -160, -120, orderingTableSize_1f8003c8.get() - 4, 1, (int)_800ef1a4.get());

    //LAB_800dc164
  }

  @Method(0x800dc178L)
  public static void renderCoolonMap(final long a0, final long a1) {
    final WMapStruct258 struct = struct258_800c66a8;

    final CoolonWarpDestination20 warp1 = coolonWarpDest_800ef228.get(struct.coolonWarpIndex_221);
    final CoolonWarpDestination20 warp2 = coolonWarpDest_800ef228.get(struct.coolonWarpIndex_222);

    short x = (short)(warp1.x_18.get() - warp2.x_18.get());
    short y = (short)(warp1.y_1a.get() - warp2.y_1a.get());

    struct.rotation_a4.setY((short)(ratan2(y, x) + 0x400 & 0xfff));
    struct.models_0c[2].coord2Param_64.rotate.y.add((short)((struct.rotation_a4.getY() - struct.models_0c[2].coord2Param_64.rotate.getY()) / 8));

    if(a0 != 0) {
      if((joypadRepeat_8007a3a0.get() & 0x6000) != 0) {
        playSound(0, 1, 0, 0, (short)0, (short)0);

        if(struct.coolonWarpIndex_222 > 0) {
          struct.coolonWarpIndex_222--;
        } else {
          struct.coolonWarpIndex_222 = 8;
        }
      }

      //LAB_800dc384
      if((joypadRepeat_8007a3a0.get() & 0x9000) != 0) {
        playSound(0, 1, 0, 0, (short)0, (short)0);

        struct.coolonWarpIndex_222++;
        if(struct.coolonWarpIndex_222 > 8) {
          struct.coolonWarpIndex_222 = 0;
        }
      }
    }

    //LAB_800dc410
    final int sp20 = tickCount_800bb0fc.get() / 5 % 3;

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
        .uv(0, sp20 * 16, 0)
        .uv(1, (sp20 + 1) * 16, 0)
        .uv(2, sp20 * 16, 16)
        .uv(3, (sp20 + 1) * 16, 16)
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
        FUN_8002a32c(7, 0, x, y, (short)((width & 0xffff) - 1), (short)((lines & 0xffff) - 1));
        _800c86f0.setu(1);

        //LAB_800dca40
        textZ_800bdf00.set(14);
        textboxes_800be358[7].z_0c = 14;

        if(width >= 4) {
          textboxes_800be358[7].chars_18 = width;
        } else {
          textboxes_800be358[7].chars_18 = 4;
        }

        //LAB_800dca84
        textboxes_800be358[7].lines_1a = lines;
        _800c86f0.setu(2);
      } else if(v0 == 1) {
        textZ_800bdf00.set(14);
        textboxes_800be358[7].z_0c = 14;

        if(width >= 4) {
          textboxes_800be358[7].chars_18 = width;
        } else {
          textboxes_800be358[7].chars_18 = 4;
        }

        //LAB_800dca84
        textboxes_800be358[7].lines_1a = lines;
        _800c86f0.setu(2);
        //LAB_800dc9d0
      } else if(v0 == 2) {
        //LAB_800dca9c
        if(width >= 4) {
          textboxes_800be358[7].chars_18 = width;
        } else {
          textboxes_800be358[7].chars_18 = 4;
        }

        //LAB_800dcac8
        textboxes_800be358[7].lines_1a = lines;
        textboxes_800be358[7].width_1c = textboxes_800be358[7].chars_18 * 9 / 2;
        textboxes_800be358[7].height_1e = textboxes_800be358[7].lines_1a * 6;
        textboxes_800be358[7].x_14 = x;
        textboxes_800be358[7].y_16 = y;
      }

      //LAB_800dcb48
      textZ_800bdf00.set(18);
      textboxes_800be358[7].z_0c = 18;
      FUN_800e774c(coolonWarpDest_800ef228.get(struct.coolonWarpIndex_222).placeName_1c.deref(), (short)(x - width * 3), (short)(y - lines * 7), 0, 0);
    }

    //LAB_800dcc0c
  }

  @Method(0x800dcc20L)
  public static void FUN_800dcc20(final VECTOR a0, final VECTOR a1, final VECTOR a2, final int a3, final int a4) {
    if(a3 == a4) {
      a0.setX(a2.getX() << 12);
      a0.setY(a2.getY() << 12);
      a0.setZ(a2.getZ() << 12);
    } else {
      //LAB_800dcca4
      a0.setX(((a2.getX() - a1.getX() << 12) / a3 * a4 >> 12) + a1.getX() << 12);
      a0.setY(((a2.getY() - a1.getY() << 12) / a3 * a4 >> 12) + a1.getY() << 12);
      a0.setZ(((a2.getZ() - a1.getZ() << 12) / a3 * a4 >> 12) + a1.getZ() << 12);
    }

    //LAB_800dcddc
  }

  @Method(0x800dcde8L)
  public static void deallocateWorldMap() {
    if(struct258_800c66a8.tmdRendering_08 != null) {
      deallocateTmdRenderer(struct258_800c66a8.tmdRendering_08);
    }

    //LAB_800dce24
    FUN_800d6818(struct258_800c66a8.ptr_1c);
    FUN_800d15d8(struct258_800c66a8._1fc);
  }

  @Method(0x800dce64L)
  public static void rotateCoord2(final SVECTOR rotation, final GsCOORDINATE2 coord2) {
    final MATRIX mat = new MATRIX().set(identityMatrix_800c3568);

    mat.transfer.set(coord2.coord.transfer);

    RotMatrix_8003faf0(rotation, mat);

    coord2.flg = 0;
    coord2.coord.set(mat);
  }

  /**
   * Resets the matrix to identity but maintains transforms
   */
  @Method(0x800dcf80L)
  public static void clearLinearTransforms(final MATRIX mat) {
    final int x = mat.transfer.getX();
    final int y = mat.transfer.getY();
    final int z = mat.transfer.getZ();
    mat.set(identityMatrix_800c3568);
    mat.transfer.setX(x);
    mat.transfer.setY(y);
    mat.transfer.setZ(z);
  }

  /** Don't really know what makes it special. Seems to use a fixed Z value and doesn't check if the triangles are on screen. Used for water. */
  @Method(0x800dd05cL)
  public static void renderSpecialDobj2(final GsDOBJ2 dobj2) {
    final UnboundedArrayRef<SVECTOR> vertices = dobj2.tmd_08.vert_top_00.deref();
    long primitives = dobj2.tmd_08.primitives_10.getPointer();
    long count = dobj2.tmd_08.n_primitive_14.get();

    //LAB_800dd0dc
    while(count != 0) {
      final long primitiveCount = MEMORY.ref(2, primitives).get();

      //LAB_800dd0f4
      count -= primitiveCount;

      final long v0 = MEMORY.ref(4, primitives).get() & 0xff04_0000L;
      if(v0 == 0x3d00_0000L) {
        primitives = FUN_800deeac(primitives, vertices, primitiveCount);
      } else {
        assert false;
      }

      //LAB_800dd384
      //LAB_800dd38c
    }

    //LAB_800dd394
  }

  @Method(0x800deeacL)
  public static long FUN_800deeac(long primitives, final UnboundedArrayRef<SVECTOR> vertices, final long count) {
    //LAB_800deee8
    for(int i = 0; i < count; i++) {
      final int tpage = (int)MEMORY.ref(2, primitives).offset(0x0aL).get();

      final GpuCommandPoly cmd = new GpuCommandPoly(4)
        .bpp(Bpp.of(tpage >>> 7 & 0b11))
        .clut(1008, (int)_800ef348.offset(struct258_800c66a8._28 * 0x2L).get())
        .vramPos((tpage & 0b1111) * 64, (tpage & 0b10000) != 0 ? 256 : 0)
        .uv(0, MEMORY.get(primitives + 0x04L) & 0xff, MEMORY.get(primitives + 0x05L) & 0xff)
        .uv(1, MEMORY.get(primitives + 0x08L) & 0xff, MEMORY.get(primitives + 0x09L) & 0xff)
        .uv(2, MEMORY.get(primitives + 0x0cL) & 0xff, MEMORY.get(primitives + 0x0dL) & 0xff)
        .uv(3, MEMORY.get(primitives + 0x10L) & 0xff, MEMORY.get(primitives + 0x11L) & 0xff);

      //LAB_800def00
      final SVECTOR vert0 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x24L).get());
      final SVECTOR vert1 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x26L).get());
      final SVECTOR vert2 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x28L).get());
      CPU.MTC2(vert0.getXY(), 0);
      CPU.MTC2(vert0.getZ(),  1);
      CPU.MTC2(vert1.getXY(), 2);
      CPU.MTC2(vert1.getZ(),  3);
      CPU.MTC2(vert2.getXY(), 4);
      CPU.MTC2(vert2.getZ(),  5);
      CPU.COP2(0x28_0030L); // Perspective transform triple

      if((int)CPU.CFC2(31) >= 0) { // No errors
        //LAB_800defac
        CPU.COP2(0x140_0006L); // Normal clipping

        if((int)CPU.MFC2(24) > 0) { // Is visible
          //LAB_800defe8
          final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(12));
          final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(13));
          final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));

          cmd
            .pos(0, v0.getX(), v0.getY())
            .pos(1, v1.getX(), v1.getY())
            .pos(2, v2.getX(), v2.getY());

          final SVECTOR vert3 = vertices.get((int)MEMORY.ref(2, primitives).offset(0x2aL).get());
          CPU.MTC2(vert3.getXY(), 0);
          CPU.MTC2(vert3.getZ(),  1);
          CPU.COP2(0x18_0001L); // Perspective transform single

          if((int)CPU.CFC2(31) >= 0) { // No errors
            //LAB_800df0ac
            final DVECTOR v3 = new DVECTOR().setXY(CPU.MFC2(14));

            cmd
              .pos(3, v3.getX(), v3.getY())
              .rgb(0, MEMORY.get(primitives + 0x14L) & 0xff, MEMORY.get(primitives + 0x15L) & 0xff, MEMORY.get(primitives + 0x16L) & 0xff)
              .rgb(1, MEMORY.get(primitives + 0x18L) & 0xff, MEMORY.get(primitives + 0x19L) & 0xff, MEMORY.get(primitives + 0x1aL) & 0xff)
              .rgb(2, MEMORY.get(primitives + 0x1cL) & 0xff, MEMORY.get(primitives + 0x1dL) & 0xff, MEMORY.get(primitives + 0x1eL) & 0xff)
              .rgb(3, MEMORY.get(primitives + 0x20L) & 0xff, MEMORY.get(primitives + 0x21L) & 0xff, MEMORY.get(primitives + 0x22L) & 0xff);

            GPU.queueCommand(tempZ_800c66d8.get(), cmd);
          }
        }
      }

      //LAB_800df1ec
      primitives += 0x2cL;
    }

    //LAB_800df204
    //LAB_800df220
    return primitives;
  }

  @Method(0x800dfa70L)
  public static void FUN_800dfa70() {
    filesLoadedFlags_800c66b8.and(0xffff_fd57L);

    loadDrgnDir(0, 5713, files -> WMap.timsLoaded(files, 0x2a8));

    //LAB_800dfacc
    for(int i = 0; i < 4; i++) {
      //LAB_800dfae8
      struct258_800c66a8.models_0c[i] = new Model124();
      final int finalI = i;
      loadDrgnDir(0, 5714 + i, files -> WMap.FUN_800d5a30(files, finalI));
      struct258_800c66a8.models_0c[i].colourMap_9d = (int)_800ef694.offset(i).get() + 0x80;
    }

    //LAB_800dfbb4
    struct258_800c66a8._248 = 0;
  }

  @Method(0x800dfbd8L)
  public static void FUN_800dfbd8() {
    final WMapStruct258 struct258 = struct258_800c66a8;
    struct258.vec_94.setX(struct258.coord2_34.coord.transfer.getX() << 12);
    struct258.vec_94.setY(struct258.coord2_34.coord.transfer.getY() << 12);
    struct258.vec_94.setZ(struct258.coord2_34.coord.transfer.getZ() << 12);
    struct258.vec_84.set(struct258.vec_94);

    //LAB_800dfca4
    for(int i = 0; i < 4; i++) {
      final Model124 model = struct258.models_0c[i];

      //LAB_800dfcc0
      initModel(model, struct258._b4[i].extendedTmd_00, struct258._b4[i].tmdAnim_08[0]);
      loadModelStandardAnimation(model, struct258._b4[i].tmdAnim_08[0]);

      model.coord2_14.coord.transfer.set(struct258.coord2_34.coord.transfer);
      model.coord2Param_64.rotate.set((short)0, struct258.rotation_a4.getY(), (short)0);
      model.scaleVector_fc.set(0, 0, 0);
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
    struct258.modelIndex_1e4 = areaData_800f2248.get(areaIndex_800c67aa.get()).modelIndex_06.get();
    FUN_800e28dc(40, 1);

    final int modelIndex = struct258.modelIndex_1e4;
    final Model124 model = struct258.models_0c[modelIndex];
    if(modelIndex == 0) {
      //LAB_800e00c4
      model.scaleVector_fc.set(0x800, 0x666, 0x800);
    } else if(modelIndex == 1) {
      //LAB_800e0114
      if(_800c6798.get() == 7) {
        model.scaleVector_fc.set(0x1000, 0x1000, 0x1000);
      } else {
        model.scaleVector_fc.set(0x2000, 0x2000, 0x2000);
      }

      //LAB_800e01b8
      //LAB_800e00a4
    } else if(modelIndex == 2) {
      //LAB_800e01c0
      model.scaleVector_fc.set(0, 0, 0);
    } else if(modelIndex == 3) {
      //LAB_800e0210
      model.scaleVector_fc.set(0, 0, 0);
    }

    //LAB_800e0260
  }

  @Method(0x800e0274L) // Pretty sure this renders the player
  public static void renderPlayer() {
    final WMapStruct258 struct = struct258_800c66a8;

    if(struct._250 != 2) {
      struct.modelIndex_1e4 = areaData_800f2248.get(areaIndex_800c67aa.get()).modelIndex_06.get();

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
      GsSetAmbient(0xc80, 0xc80, 0xc80);

      struct.models_0c[0].scaleVector_fc.set(0x800, 0x666, 0x800);
    } else if(modelIndex == 1) {
      //LAB_800e0404
      GsSetAmbient(0x800, 0x800, 0x800);


      if(_800c6798.get() == 7) {
        struct.models_0c[1].scaleVector_fc.set(0x1000, 0x1000, 0x1000);
      } else {
        struct.models_0c[1].scaleVector_fc.set(0x2000, 0x2000, 0x2000);
      }

      //LAB_800e04bc
      //LAB_800e0380
    } else if(modelIndex == 2) {
      //LAB_800e04c4
      GsSetAmbient(0x800, 0x800, 0x800);
    } else if(modelIndex == 3) {
      //LAB_800e04e0
      GsSetAmbient(0x800, 0x800, 0x800);
    }

    //LAB_800e04fc
    struct.models_0c[struct.modelIndex_1e4].zOffset_a0 = 78;
    renderModel(struct.models_0c[struct.modelIndex_1e4]);
    GsSetAmbient(_800c66b0.ambientLight_14c.getX(), _800c66b0.ambientLight_14c.getY(), _800c66b0.ambientLight_14c.getZ());
    FUN_800e06d0();
    FUN_800e1364();
  }

  @Method(0x800e05c4L)
  public static void FUN_800e05c4() {
    //LAB_800e05d8
    for(int i = 0; i < 4; i++) {
      //LAB_800e05f4
      deallocateModel(struct258_800c66a8.models_0c[i]);
      free(struct258_800c66a8._1b4[i].getAddress());
      struct258_800c66a8.models_0c[i] = null;
    }

    //LAB_800e06b4
    FUN_800e3230();
  }

  @Method(0x800e06d0L)
  public static void FUN_800e06d0() {
    long at; //TODO

    struct258_800c66a8.vec_84.set(struct258_800c66a8.vec_94);

    if(struct258_800c66a8._250 == 0) {
      //LAB_800e0760
      FUN_800e8a10();
    } else if(struct258_800c66a8._250 == 1) {
      //LAB_800e0770
      //LAB_800e0774
      int sp3c = 0;
      for(int i = 0; i < 6; i++) {
        //LAB_800e0790
        at = 0x800f_0000L;

        if(_800c67a8.get() == MEMORY.ref(4, at).offset(-0x968L).offset(i * 0x8L).get()) {
          sp3c = (int)MEMORY.ref(4, at).offset(-0x968L).offset((i * 2 + 1) * 0x4L).get();
          break;
        }
      }

      //LAB_800e0810
      final SVECTOR sp0x18 = new SVECTOR();
      final SVECTOR sp0x20 = new SVECTOR();
      FUN_800e0d70(_800c67a8.get(), sp0x18);
      FUN_800e0d70(sp3c, sp0x20);

      //LAB_800e0878
      if(struct258_800c66a8._248 == 0 || struct258_800c66a8._248 == 1) {
        if(struct258_800c66a8._248 == 0) {
          //LAB_800e0898
          struct258_800c66a8._24c = 0;
          struct258_800c66a8._248 = 1;
        }

        //LAB_800e08b8
        renderWinglyTeleportScreenEffect();

        FUN_800e0e4c(struct258_800c66a8.vec_94, sp0x18, sp0x20, 0x20, struct258_800c66a8._24c);

        struct258_800c66a8._24c++;
        if(struct258_800c66a8._24c > 32) {
          struct258_800c66a8._248 = 2;
        }

        //LAB_800e0980
        final int a0 = struct258_800c66a8._24c * 0x40 + (rsin(struct258_800c66a8._24c * 0x200) * 0x100 >> 12);
        struct258_800c66a8.models_0c[3].scaleVector_fc.set(a0, a0, a0);
        struct258_800c66a8.models_0c[struct258_800c66a8.modelIndex_1e4].coord2Param_64.rotate.setY(_800c66b0.mapRotation_70.getY());
        struct258_800c66a8.rotation_a4.setY(_800c66b0.mapRotation_70.getY());
      } else if(struct258_800c66a8._248 == 2) {
        //LAB_800e0a6c
        gameState_800babc8._17c.get(sp3c >>> 5).or(0x1 << (sp3c & 0x1f));

        //LAB_800e0b64
        at = 0x800f_0000L; //TODO
        _800c6860.setu(MEMORY.ref(2, at).offset(0xe3cL).offset(sp3c * 0x14L).get());
        _800c6862.setu(MEMORY.ref(2, at).offset(0xe3eL).offset(sp3c * 0x14L).get());
        submapCut_80052c30.set((int)_800c6860.get());
        submapScene_80052c34.set((int)_800c6862.get());

        FUN_800e3fac(1);
        struct258_800c66a8._248 = 3;
      } else if(struct258_800c66a8._248 == 3) {
        //LAB_800e0c00
        struct258_800c66a8.models_0c[3].scaleVector_fc.x.sub(0x400);

        if(struct258_800c66a8.models_0c[3].scaleVector_fc.getX() < 0) {
          struct258_800c66a8.models_0c[3].scaleVector_fc.setX(0);
        }

        //LAB_800e0c70
        final int a0 = struct258_800c66a8.models_0c[3].scaleVector_fc.getX();
        struct258_800c66a8.models_0c[3].scaleVector_fc.setY(a0);
        struct258_800c66a8.models_0c[3].scaleVector_fc.setZ(a0);
      }

      //LAB_800e0cbc
    }

    //LAB_800e0cc4
    struct258_800c66a8.rotation_a4.x.and(0xfff);
    struct258_800c66a8.rotation_a4.y.and(0xfff);
    struct258_800c66a8.rotation_a4.z.and(0xfff);

    FUN_800e10a0();
  }

  @Method(0x800e0d70L)
  public static void FUN_800e0d70(final long a0, final SVECTOR a1) {
    //LAB_800e0d84
    for(int i = 0; i < 6; i++) {
      //LAB_800e0da0
      final long at = 0x800f_0000L; //TODO
      if(a0 == MEMORY.ref(4, at).offset(-0x938L).offset(i * 0xc).get()) {
        final long a0_0 = 0x800f_0000L - 0x934L; //TODO
        a1.set((SVECTOR)MEMORY.ref(4, a0_0).offset(i * 0xc).cast(SVECTOR::new));
        break;
      }
    }

    //LAB_800e0e3c
  }

  @Method(0x800e0e4cL)
  public static void FUN_800e0e4c(final VECTOR out, final SVECTOR a1, final SVECTOR a2, final int a3, final int a4) {
    if(a3 == a4) {
      out.set(a2).shl(12);
    } else {
      //LAB_800e0ed8
      out.setX( a1.getX() + ((a2.getX() - a1.getX() << 12) / a3 * a4 >> 12) << 12);
      out.setY((a1.getY() + ((a2.getY() - a1.getY() << 12) / a3 * a4 >> 12) << 12) + rsin(0x800 / a3 * a4) * -200);
      out.setZ( a1.getZ() + ((a2.getZ() - a1.getZ() << 12) / a3 * a4 >> 12) << 12);
    }

    //LAB_800e108c
  }

  @Method(0x800e10a0L) //TODO this might control player animation?
  public static void FUN_800e10a0() {
    final WMapStruct258 struct = struct258_800c66a8;

    struct.currentAnimIndex_ac = struct.animIndex_b0;

    if(struct.vec_84.getX() != struct.vec_94.getX() || struct.vec_84.getY() != struct.vec_94.getY() || struct.vec_84.getZ() != struct.vec_94.getZ()) {
      //LAB_800e117c
      //LAB_800e11b0
      if((joypadInput_8007a39c.get() & 0x40) != 0) {
        //LAB_800e11d0
        struct.animIndex_b0 = 4;
        handleEncounters(2);
      } else {
        //LAB_800e11f4
        struct.animIndex_b0 = 3;
        handleEncounters(1);
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
      //TODO this was a relative offset from the start of the struct, rather than 0x8, so I think -2 is correct
      loadModelStandardAnimation(struct.models_0c[struct.modelIndex_1e4], struct._b4[struct.modelIndex_1e4].tmdAnim_08[struct.animIndex_b0 - 2]);
    }

    //LAB_800e1354
  }

  @Method(0x800e1364L)
  public static void FUN_800e1364() {
    renderPlayerShadow();

    final WMapStruct258 struct = struct258_800c66a8;
    struct.coord2_34.coord.transfer.setX(struct.vec_94.getX() >> 12);
    struct.coord2_34.coord.transfer.setY(struct.vec_94.getY() >> 12);
    struct.coord2_34.coord.transfer.setZ(struct.vec_94.getZ() >> 12);
    struct.models_0c[struct.modelIndex_1e4].coord2_14.coord.transfer.set(struct.coord2_34.coord.transfer);

    if(struct._250 == 0) {
      int sp10 = struct.rotation_a4.getY() - struct.models_0c[struct.modelIndex_1e4].coord2Param_64.rotate.getY();
      final int sp14 = struct.rotation_a4.getY() - (struct.models_0c[struct.modelIndex_1e4].coord2Param_64.rotate.getY() - 0x1000);

      if(Math.abs(sp14) < Math.abs(sp10)) {
        sp10 = sp14;
      }

      //LAB_800e15e4
      struct.models_0c[struct.modelIndex_1e4].coord2Param_64.rotate.y.add((short)(sp10 / 2));
      struct.models_0c[struct.modelIndex_1e4].coord2Param_64.rotate.setX(struct.rotation_a4.getX());
      struct.models_0c[struct.modelIndex_1e4].coord2Param_64.rotate.setZ(struct.rotation_a4.getZ());
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

      final int z = perspectiveTransformTriple(vert0, vert1, vert2, sxy0, sxy1, sxy2, null, null);

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

    final VECTOR sp0x88 = new VECTOR();
    final VECTOR sp0x98 = new VECTOR();
    final VECTOR sp0xa8 = new VECTOR();
    final VECTOR sp0xb8 = new VECTOR();
    final VECTOR sp0xc8 = new VECTOR();

    sp0xb8.set(_800c87d8);

    sp0xa8.setX(struct258_800c66a8.vec_84.getX() - struct258_800c66a8.vec_94.getX() >> 12);
    sp0xa8.setY(struct258_800c66a8.vec_84.getY() - struct258_800c66a8.vec_94.getY() >> 12);
    sp0xa8.setZ(struct258_800c66a8.vec_84.getZ() - struct258_800c66a8.vec_94.getZ() >> 12);
    FUN_8003ea80(sp0xa8, sp0xa8);
    FUN_80040e40(sp0xa8, sp0xb8, sp0xc8);
    sp0x88.setX(struct258_800c66a8.vec_94.getX() >> 12);
    sp0x88.setY(struct258_800c66a8.vec_94.getY() >> 12);
    sp0x88.setZ(struct258_800c66a8.vec_94.getZ() >> 12);
    FUN_800e2ae4(sp0xc8, sp0x88);
    rotateCoord2(struct258_800c66a8.tmdRendering_08.rotations_08[0], struct258_800c66a8.tmdRendering_08.coord2s_04[0]);
    GsGetLs(struct258_800c66a8.tmdRendering_08.coord2s_04[0], sp0x28);
    setRotTransMatrix(sp0x28);

    //LAB_800e1ccc
    for(int i = 0; i < 39; i++) {
      //LAB_800e1ce8
      FUN_800e2e1c(i, sp0x88, sp0x98, sp0x74, sp0x70);
      int spc8 = sp0x88.getX() * sp0x70.get() >> 12;
      int spcc = sp0x88.getY() * sp0x70.get() >> 12;
      int spd0 = sp0x88.getZ() * sp0x70.get() >> 12;
      final int spd8 = -spc8;
      final int spdc = -spcc;
      final int spe0 = -spd0;
      sp0x48.setX((short)(spc8 + sp0x98.getX()));
      sp0x48.setY((short)(spcc + sp0x98.getY()));
      sp0x48.setZ((short)(spd0 + sp0x98.getZ()));
      sp0x50.setX((short)sp0x98.getX());
      sp0x50.setY((short)sp0x98.getY());
      sp0x50.setZ((short)sp0x98.getZ());

      FUN_800e2e1c(i + 1, sp0x88, sp0xa8, sp0x74, sp0x70);
      spc8 = sp0x88.getX() * sp0x70.get() >> 12;
      spcc = sp0x88.getY() * sp0x70.get() >> 12;
      spd0 = sp0x88.getZ() * sp0x70.get() >> 12;
      final int spe8 = -spc8;
      final int spec = -spcc;
      final int spf0 = -spd0;
      sp0x58.setX((short)(spc8 + sp0xa8.getX()));
      sp0x58.setY((short)(spcc + sp0xa8.getY()));
      sp0x58.setZ((short)(spd0 + sp0xa8.getZ()));
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

      int z = RotTransPers4(sp0x48, sp0x50, sp0x58, sp0x60, sxyz0, sxyz1, sxyz2, sxyz3, null, null);

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
      sp0x48.setX((short)(spd8 + sp0x98.getX()));
      sp0x48.setY((short)(spdc + sp0x98.getY()));
      sp0x48.setZ((short)(spe0 + sp0x98.getZ()));
      sp0x58.setX((short)(spe8 + sp0xa8.getX()));
      sp0x58.setY((short)(spec + sp0xa8.getY()));
      sp0x58.setZ((short)(spf0 + sp0xa8.getZ()));
      z = RotTransPers4(sp0x48, sp0x50, sp0x58, sp0x60, sxyz0, sxyz1, sxyz2, sxyz3, null, null);

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
      final long v0 = struct258_800c66a8.ptr_22c + sp6c * 0x4L;
      MEMORY.ref(4, v0).offset(0x0L).addu(0x1L);
    }

    //LAB_800e289c
    struct258_800c66a8._240++;
  }

  @Method(0x800e28dcL)
  public static void FUN_800e28dc(final int a0, final int a1) {
    final int count = a0 * a1;

    struct258_800c66a8.vecs_224 = MEMORY.ref(4, mallocTail(count * 0x10L), UnboundedArrayRef.of(0x10, VECTOR::new, () -> count));
    struct258_800c66a8.vecs_228 = MEMORY.ref(4, mallocTail(count * 0x10L), UnboundedArrayRef.of(0x10, VECTOR::new, () -> count));
    struct258_800c66a8.ptr_22c = mallocTail(count * 0x4L);
    struct258_800c66a8._230 = 0;
    struct258_800c66a8._234 = count - 1;
    struct258_800c66a8._238 = count;
    struct258_800c66a8._23c = a1;

    //LAB_800e29f8
    //NOTE: there's a bug in the original code, it just sets the first vector in the array over and over again
    for(int i = 0; i < count; i++) {
      //LAB_800e2a18
      struct258_800c66a8.vecs_224.get(i).set(0, 0, 0);
      struct258_800c66a8.vecs_228.get(i).set(0, 0, 0);
    }

    //LAB_800e2ac0
    struct258_800c66a8._244 = 0;
  }

  @Method(0x800e2ae4L)
  public static void FUN_800e2ae4(final VECTOR a0, final VECTOR a1) {
    final WMapStruct258 struct258 = struct258_800c66a8;

    if(struct258._244 == 0) {
      //LAB_800e2b14
      for(int i = 0; i < struct258._238; i++) {
        //LAB_800e2b3c
        struct258.vecs_224.get(i).set(a0);
        struct258.vecs_228.get(i).set(a1);
      }

      //LAB_800e2ca4
      struct258._244 = 1;
      struct258._240 = 0;
    }

    //LAB_800e2cc4
    struct258.vecs_224.get(struct258._230).set(a0);
    struct258.vecs_228.get(struct258._230).set(a1);

    MEMORY.ref(4, struct258.ptr_22c).offset(struct258._230 * 0x4L).setu(0);

    struct258._234 = struct258._230;
    struct258._230 = (struct258._230 + 1) % struct258._238;
  }

  @Method(0x800e2e1cL)
  public static void FUN_800e2e1c(final int a0, final VECTOR a1, final VECTOR a2, final IntRef a3, final IntRef a4) {
    if(a0 == 0) {
      a1.set(struct258_800c66a8.vecs_224.get(struct258_800c66a8._234));
      a2.set(struct258_800c66a8.vecs_228.get(struct258_800c66a8._234));
      a3.set((int)MEMORY.ref(4, struct258_800c66a8.ptr_22c).offset(struct258_800c66a8._234 * 0x4L).get());
      final long v0 = MEMORY.ref(4, struct258_800c66a8.ptr_22c).offset(struct258_800c66a8._234 * 0x4L).get() - struct258_800c66a8._240;
      a4.set((int)(MEMORY.ref(4, struct258_800c66a8.ptr_22c).offset(struct258_800c66a8._234 * 0x4L).get() + (rsin(v0 << 8 & 0x7ff) * MEMORY.ref(4, struct258_800c66a8.ptr_22c).offset(struct258_800c66a8._234 * 0x4L).get() >> 12)));
    } else {
      //LAB_800e3024
      int sp10 = struct258_800c66a8._230 - a0 * struct258_800c66a8._23c;

      if(sp10 < 0) {
        sp10 += struct258_800c66a8._238;
      }

      //LAB_800e3090
      a1.set(struct258_800c66a8.vecs_224.get(sp10));
      a2.set(struct258_800c66a8.vecs_228.get(sp10));
      a3.set((int)MEMORY.ref(4, struct258_800c66a8.ptr_22c).offset(sp10 * 0x4L).get());
      final long v0 = MEMORY.ref(4, struct258_800c66a8.ptr_22c).offset(sp10 * 0x4L).get() - struct258_800c66a8._240;
      a4.set((int)(MEMORY.ref(4, struct258_800c66a8.ptr_22c).offset(sp10 * 0x4L).get() + (rsin(v0 << 8 & 0x7ff) * MEMORY.ref(4, struct258_800c66a8.ptr_22c).offset(sp10 * 0x4L).get() >> 12)));
    }

    //LAB_800e321c
  }

  @Method(0x800e3230L)
  public static void FUN_800e3230() {
    free(struct258_800c66a8.vecs_224.getAddress());
    free(struct258_800c66a8.vecs_228.getAddress());
    free(struct258_800c66a8.ptr_22c);
  }

  @Method(0x800e32a8L)
  public static void renderPlayerShadow() {
    shadowRenderers_800ef684.get(struct258_800c66a8.modelIndex_1e4).deref().run();
  }

  @Method(0x800e32fcL)
  public static void FUN_800e32fc() {
    // no-op
  }

  /** Some kind of full-screen effect during the Wingly teleportation between Aglis and Zenebatos */
  @Method(0x800e3304L)
  public static void renderWinglyTeleportScreenEffect() {
    final int v = doubleBufferFrame_800bb108.get() ^ 1;

    final GpuCommandQuad cmd = new GpuCommandQuad()
      .bpp(Bpp.BITS_15)
      .translucent(Translucency.HALF_B_PLUS_HALF_F)
      .vramPos(0, doubleBufferFrame_800bb108.get() * 256)
      .monochrome(0x80)
      .pos(-160, -120, 320, 240)
      .uv(0, v * 16);

    GPU.queueCommand(5, cmd);
  }

  @Method(0x800e367cL)
  public static void handleEncounters(final int encounterRateMultiplier) {
    //LAB_800e36a8
    if(worldMapState_800c6698.get() != 0x5L) {
      return;
    }

    //LAB_800e36c4
    if(playerState_800c669c.get() != 0x5L) {
      return;
    }

    //LAB_800e36e0
    if(struct258_800c66a8.modelIndex_1e4 >= 2) {
      return;
    }

    //LAB_800e3708
    if(_800c6870.get() != 0) {
      return;
    }

    //LAB_800e3724
    if(struct258_800c66a8._05 != 0) {
      return;
    }

    //LAB_800e3748
    if(_800c686c.get() != 0 || _800c6870.get() != 0) {
      //LAB_800e3778
      return;
    }

    //LAB_800e3780
    //LAB_800e3794
    final WMapAreaData08 area = areaData_800f2248.get(areaIndex_800c67aa.get());
    encounterAccumulator_800c6ae8.add(area.encounterRate_03.get() * encounterRateMultiplier * 70);

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
      gameState_800babc8.areaIndex_4de.set(areaIndex_800c67aa.get());
      gameState_800babc8.pathIndex_4d8.set(pathIndex_800c67ac.get());
      gameState_800babc8.dotIndex_4da.set(dotIndex_800c67ae.get());
      gameState_800babc8.dotOffset_4dc.set(dotOffset_800c67b0.get());
      gameState_800babc8.facing_4dd.set(facing_800c67b4.get());
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

  @Method(0x800e3b14L)
  public static void deallocateTmdRenderer(final WMapTmdRenderingStruct18 a0) {
    free(a0.tmd_14.getAddress());
  }

  @Method(0x800e3bd4L)
  public static int allocateTmdRenderer(final WMapTmdRenderingStruct18 a0, final TmdWithId tmd) {
    adjustTmdPointers(tmd.tmd);

    final int nobj = tmd.tmd.header.nobj.get();
    a0.dobj2s_00 = new GsDOBJ2[nobj];
    a0.coord2s_04 = new GsCOORDINATE2[nobj];
    a0.rotations_08 = new SVECTOR[nobj];
    a0._10 = new int[nobj];

    Arrays.setAll(a0.dobj2s_00, i -> new GsDOBJ2());
    Arrays.setAll(a0.coord2s_04, i -> new GsCOORDINATE2());
    Arrays.setAll(a0.rotations_08, i -> new SVECTOR());

    //LAB_800e3d24
    for(int i = 0; i < nobj; i++) {
      //LAB_800e3d44
      updateTmdPacketIlen(tmd.tmd.objTable, a0.dobj2s_00[i], i);
    }

    //LAB_800e3d80
    //LAB_800e3d94
    return nobj;
  }

  @Method(0x800e3da8L)
  public static void initTmdTransforms(final WMapTmdRenderingStruct18 a0, @Nullable final GsCOORDINATE2 superCoord) {
    //LAB_800e3dfc
    for(int i = 0; i < a0.count_0c; i++) {
      final GsDOBJ2 dobj2 = a0.dobj2s_00[i];
      final GsCOORDINATE2 coord2 = a0.coord2s_04[i];
      final SVECTOR rotation = a0.rotations_08[i];

      //LAB_800e3e20
      GsInitCoordinate2(superCoord, coord2);

      dobj2.coord2_04 = coord2;
      coord2.coord.transfer.set(0, 0, 0);
      rotation.set((short)0, (short)0, (short)0);
    }

    //LAB_800e3ee8
  }

  @Method(0x800e3efcL)
  public static void setAllCoord2Attribs(final WMapTmdRenderingStruct18 a0, final int attribute) {
    //LAB_800e3f24
    for(int i = 0; i < a0.count_0c; i++) {
      final GsDOBJ2 sp4 = a0.dobj2s_00[i];

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
      _800f01fc.offset((struct258_800c66a8._05 - 1) * 0x4L).deref(4).call();
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
        if(playerState_800c669c.get() >= 3) {
          struct258_800c66a8._00++;

          if(struct258_800c66a8._00 >= 2) {
            _800c686c.setu(0);
          }
        }

        //LAB_800e4624
        if(_800c66b0._c5 == 0 && _800c686c.get() == 0) {
          _800c6868.setu(0);
          struct258_800c66a8._05 = 0;
          struct258_800c66a8._04 = 2;
        }
        //LAB_800e4478
      } else if(v0 == 0 && ((int)worldMapState_800c6698.get() >= 3 || (int)playerState_800c669c.get() >= 3)) {
        //LAB_800e44b0
        scriptStartEffect(2, 15);

        _800c66b0._11a = 1;
        _800c66b0.coord2_20.coord.transfer.setX(0);
        _800c66b0.coord2_20.coord.transfer.setY(0);
        _800c66b0.coord2_20.coord.transfer.setZ(0);
        _800c66b0._9a = 0;
        _800c66b0.mapRotation_70.setY((short)0);

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
        if((int)playerState_800c669c.get() >= 3) {
          struct258_800c66a8._00++;

          if(struct258_800c66a8._00 >= 2) {
            _800c686c.setu(0);
          }
        }

        //LAB_800e43c4
        if(_800c66b0._c5 == 0 && _800c686c.get() == 0) {
          _800c6868.setu(0);
          struct258_800c66a8._05 = 0;
          struct258_800c66a8._04 = 2;
        }

        //LAB_800e441c
        //LAB_800e4424
        //LAB_800e410c
        //LAB_800e42fc
      } else if(v0 == 0 && ((int)worldMapState_800c6698.get() >= 3 || (int)playerState_800c669c.get() >= 3)) {
        //LAB_800e4144
        scriptStartEffect(2, 15);

        _800c66b0.rview2_00.viewpoint_00.setY(-9000);
        _800c66b0.rview2_00.refpoint_0c.setY(9000);
        _800c66b0._11a = 1;
        _800c66b0.coord2_20.coord.transfer.set(0, 0, 0);
        _800c66b0._9e = -300;
        _800c66b0._9a = 0;
        _800c66b0.mapRotation_70.setY((short)0);

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
  public static void renderMcq(final McqHeader mcq, final int vramOffsetX, final int vramOffsetY, final int x, final int y, final int z, final int a6, final int colour) {
    int clutX = vramOffsetX + mcq.clutX_0c.get();
    int clutY = vramOffsetY + mcq.clutY_0e.get();
    final int width = mcq.screenWidth_14.get();
    final int height = mcq.screenHeight_16.get();
    int u = vramOffsetX + mcq.u_10.get();
    int v = vramOffsetY + mcq.v_12.get();
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
    loadDrgnBinFile(0, 5696, 0, WMap::FUN_800d562c, 0, 0x4L);
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
    renderMcq(mcqHeader_800c6768, 320, 0, -160, -120, orderingTableSize_1f8003c8.get() - 3, 0, (int)_800c6794.get() & 0xff);

    //LAB_800e4f50
  }

  @Method(0x800e4f60L)
  public static void FUN_800e4f60() {
    final Memory.TemporaryReservation sp0x48tmp = MEMORY.temp(4);
    final Memory.TemporaryReservation sp0x50tmp = MEMORY.temp(4);
    final Memory.TemporaryReservation sp0x58tmp = MEMORY.temp(8);

    final Value sp0x48 = sp0x48tmp.get();
    final COLOUR sp0x50 = sp0x50tmp.get().cast(COLOUR::new);
    final Value sp0x58 = sp0x58tmp.get();

    sp0x48.setu(_800c87e8);
    MEMORY.memfill(sp0x50.getAddress(), 0x4, 0);
    sp0x58.offset(0x0L).setu(_800c87ec.offset(0x0L));
    sp0x58.offset(0x4L).setu(_800c87ec.offset(0x4L));

    _800c6898.set(FUN_800cd3c8(
      0,
      sp0x50,
      sp0x50,
      sp0x50,
      sp0x50,
      sp0x48.getAddress(),
      sp0x58.getAddress(),
      0x8L,
      0x8L,
      0x4L,
      true,
      Translucency.B_MINUS_F,
      0x9L,
      14,
      0,
      0,
      0
    ));

    sp0x48tmp.release();
    sp0x50tmp.release();
    sp0x58tmp.release();

    final Memory.TemporaryReservation sp0x60tmp = MEMORY.temp(4);
    final Memory.TemporaryReservation sp0x68tmp = MEMORY.temp(4);
    final Memory.TemporaryReservation sp0x70tmp = MEMORY.temp(8);

    final COLOUR sp0x60 = sp0x60tmp.get().cast(COLOUR::new);
    final Value sp0x68 = sp0x68tmp.get();
    final Value sp0x70 = sp0x70tmp.get();

    MEMORY.memfill(sp0x60.getAddress(), 0x4, 0);
    sp0x68.setu(_800c87f4);
    sp0x70.offset(0x0L).setu(_800c87f8.offset(0x0L));
    sp0x70.offset(0x4L).setu(_800c87f8.offset(0x4L));

    _800c689c.set(FUN_800cd3c8(
      0x80L,
      sp0x60,
      sp0x60,
      sp0x60,
      sp0x60,
      sp0x68.getAddress(),
      sp0x70.getAddress(),
      0x4L,
      0x4L,
      0x2L,
      true,
      Translucency.B_PLUS_F,
      0x9L,
      13,
      0,
      0,
      0
    ));

    sp0x60tmp.release();
    sp0x68tmp.release();
    sp0x70tmp.release();
  }

  @Method(0x800e5150L)
  public static void handleMapTransitions() {
    final long sp20;
    final long sp2c;
    final long sp38;
    final long sp3c;
    int sp50;
    long sp58;

    if(_800c6690.get() != 0) {
      return;
    }

    //LAB_800e5178
    //LAB_800e5194
    if(_800c6894.get() != 0x1L) {
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
    switch((int)_800c68a4.get()) {
      case 0:
        sp2c = -areaData_800f2248.get(_800c6874.get(0).get())._00.get();

        //LAB_800e52cc
        for(sp28 = 0; sp28 < _800c67a4.get() && areaData_800f2248.get(sp28)._00.get() != sp2c; sp28++) {
          // intentionally empty
        }

        //LAB_800e533c
        FUN_800ea4dc(sp28);

        facing_800c67b4.neg();

        FUN_800eab94((int)_800c67a8.get());

        _800c6868.setu(0x1L);
        _800c6894.setu(0x1L);

        //LAB_800e5394
        for(int i = 0; i < 8; i++) {
          //LAB_800e53b0
          FUN_8002a3ec(i, 0);
        }

        //LAB_800e53e0
        textZ_800bdf00.set(13);
        _800c6860.setu(_800f0e34.get((int)_800c67a8.get())._08.get());
        _800c6862.setu(_800f0e34.get((int)_800c67a8.get())._0a.get());
        _800c68a4.setu(0x1L);

        if(places_800f0234.get(_800f0e34.get((int)_800c67a8.get()).placeIndex_02.get()).name_00.isNull()) {
          _800c68a4.setu(0x8L);
        }

        //LAB_800e54c4
        _800c6898.deref()._34.set((short)0);
        _800c86d0.setu(0x100L);
        _800c86d2.setu(0);
        break;

      case 1:
        filesLoadedFlags_800c66b8.and(0xffff_f7ffL);

        loadDrgnBinFile(0, 5655 + places_800f0234.get(_800f0e34.get((int)_800c67a8.get()).placeIndex_02.get()).fileIndex_04.get(), 0, WMap::FUN_800d5768, 1, 0x4L);
        FUN_8002a32c(7, 1, 240, 120, 14, 16);

        _800c68a4.setu(0x2L);

        playSound(0, 4, 0, 0, (short)0, (short)0);

        //LAB_800e55f0
        for(int i = 0; i < 4; i++) {
          //LAB_800e560c
          final int soundIndex = places_800f0234.get(_800f0e34.get((int)_800c67a8.get()).placeIndex_02.get()).soundIndices_06.get(i).get();

          if(soundIndex > 0) {
            playSound(0xc, soundIndex, 0, 0, (short)0, (short)0);
          }

          //LAB_800e5698
        }

        //LAB_800e56b0
        break;

      case 2:
        if(FUN_8002a488(7) != 0) {
          FUN_8002a32c(6, 0, 240, 70, 13, 7);
          _800c68a4.setu(0x3L);
        }

        //LAB_800e5700
        break;

      case 3: // Trying to enter an area
        _800c6898.deref()._34.add((short)0x40);

        FUN_800ce4dc(_800c6898.deref());

        if(_800c6860.get() == 999L) { // Going to a different region
          sp38 = _800c6862.get() >>> 4 & 0xffffL;
          sp3c = _800c6862.get() & 0xfL;

          final IntRef width = new IntRef();
          final IntRef height = new IntRef();
          measureText(No_Entry_800f01e4.deref(), width, height);
          FUN_800e774c(No_Entry_800f01e4.deref(), 240 - width.get() * 3, 164, 0, 0);
          measureText(regions_800f01ec.get((int)sp38).deref(), width, height);
          FUN_800e774c(regions_800f01ec.get((int)sp38).deref(), 240 - width.get() * 3, 182, 0, 0);
          measureText(regions_800f01ec.get((int)sp3c).deref(), width, height);
          FUN_800e774c(regions_800f01ec.get((int)sp3c).deref(), 240 - width.get() * 3, 200, 0, 0);

          if((joypadPress_8007a398.get() & 0x1000L) != 0) {
            _800c86d2.subu(0x1L);

            if(_800c86d2.getSigned() < 0) {
              _800c86d2.setu(0x2L);
            }

            //LAB_800e5950
            playSound(0, 1, 0, 0, (short)0, (short)0);
          }

          //LAB_800e5970
          if((joypadPress_8007a398.get() & 0x4000L) != 0) {
            _800c86d2.addu(0x1L);

            if(_800c86d2.getSigned() >= 0x3L) {
              _800c86d2.setu(0);
            }

            //LAB_800e59c0
            playSound(0, 1, 0, 0, (short)0, (short)0);
          }

          //LAB_800e59e0
          _800c689c.deref().y_3a.set((short)_800c86d2.getSigned() * 18 + 8);
        } else { // Entering a town, etc.
          //LAB_800e5a18
          final IntRef width = new IntRef();
          final IntRef lines = new IntRef();
          measureText(No_Entry_800f01e4.deref(), width, lines);
          FUN_800e774c(No_Entry_800f01e4.deref(), 240 - width.get() * 3, 170, 0, 0);
          measureText(Enter_800f01e8.deref(), width, lines);
          FUN_800e774c(Enter_800f01e8.deref(), 240 - width.get() * 3, 190, 0, 0);

          if((joypadPress_8007a398.get() & 0x5000L) != 0) {
            _800c86d2.xoru(0x1L);

            playSound(0, 1, 0, 0, (short)0, (short)0);
          }

          //LAB_800e5b38
          _800c689c.deref().y_3a.set((short)_800c86d2.getSigned() * 20 + 14);
        }

        //LAB_800e5b68
        FUN_800ce4dc(_800c689c.deref());

        final int placeIndex = _800f0e34.get((int)_800c67a8.get()).placeIndex_02.get();
        final IntRef width = new IntRef();
        final IntRef lines = new IntRef();
        measureText(places_800f0234.get(placeIndex).name_00.deref(), width, lines);
        FUN_800e774c(places_800f0234.get(placeIndex).name_00.deref(), 240 - width.get() * 3, 140 - lines.get() * 7, 0, 0);

        if((filesLoadedFlags_800c66b8.get() & 0x800L) != 0) {
          final GpuCommandPoly cmd = new GpuCommandPoly(4)
            .bpp(Bpp.BITS_8)
            .clut((int)_800ef0d8.get(), (int)_800ef0da.get())
            .vramPos((int)_800ef0d4.get(), (int)_800ef0d6.get());

          sp50 = (int)_800c67a8.get();
          final int sp54 = 0x1 << (sp50 & 0x1f);
          sp50 = sp50 >>> 5;

          if((gameState_800babc8._17c.get(sp50).get() & sp54) > 0) {
            //LAB_800e5e98
            cmd.monochrome((int)_800c86d0.getSigned() / 2);
          } else {
            //LAB_800e5e18
            cmd.monochrome((int)_800c86d0.getSigned() * 0x30 / 0x100);
          }

          //LAB_800e5f04
          if(_800f0e34.get((int)_800c67a8.get())._10.get() != 0) {
            cmd.monochrome((int)_800c86d0.getSigned() / 2);
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

          if((joypadPress_8007a398.get() & 0x80L) != 0 && _800c6860.get() != 999) { // Square
            playSound(0, 2, 0, 0, (short)0, (short)0);
          }

          //LAB_800e60d0
          if((joypadInput_8007a39c.get() & 0x80L) != 0 && _800c6860.get() != 999) { // Square
            _800c86d0.subu(0x80L);

            if(_800c86d0.getSigned() < 0x80L) {
              _800c86d0.setu(0x40L);
            }

            //LAB_800e6138
            final long services = places_800f0234.get(placeIndex).services_05.get();

            //LAB_800e619c
            int servicesCount = 0;
            for(int i = 0; i < 5; i++) {
              //LAB_800e61b8
              if((services & 1L << i) != 0) {
                FUN_800e774c(services_800f01cc.get(i).deref(), 205, servicesCount * 16 + 30, 0, 0);
                servicesCount++;
              }

              //LAB_800e6248
            }

            //LAB_800e6260
            if(servicesCount == 0) {
              FUN_800e774c(_800f01e0.deref(), 201, 62, 0, 0);
            }

            //LAB_800e6290
          } else {
            //LAB_800e6298
            _800c86d0.addu(0x40L);

            if(_800c86d0.getSigned() > 0x100L) {
              _800c86d0.setu(0x100L);
            }
          }
        }

        //LAB_800e62d4
        if((joypadPress_8007a398.get() & 0x20L) != 0) {
          if(_800c86d2.getSigned() == 0) {
            FUN_8002a3ec(6, 0);
            FUN_8002a3ec(7, 1);
            _800c68a4.setu(0x6L);

            playSound(0, 3, 0, 0, (short)0, (short)0);

            //LAB_800e6350
            for(int i = 0; i < 4; i++) {
              //LAB_800e636c
              final int soundIndex = places_800f0234.get(_800f0e34.get((int)_800c67a8.get()).placeIndex_02.get()).soundIndices_06.get(i).get();

              if(soundIndex > 0) {
                FUN_80019c80(12, soundIndex, 1);
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
              final int soundIndex = places_800f0234.get(_800f0e34.get((int)_800c67a8.get()).placeIndex_02.get()).soundIndices_06.get(i).get();

              if(soundIndex > 0) {
                FUN_80019c80(12, soundIndex, 1);
              }

              //LAB_800e6504
            }
          }

          //LAB_800e651c
        } else {
          //LAB_800e6524
          if((joypadPress_8007a398.get() & 0x40L) != 0) {
            playSound(0, 3, 0, 0, (short)0, (short)0);

            //LAB_800e6560
            for(int i = 0; i < 4; i++) {
              //LAB_800e657c
              final int soundIndex = places_800f0234.get(_800f0e34.get((int)_800c67a8.get()).placeIndex_02.get()).soundIndices_06.get(i).get();

              if(soundIndex > 0) {
                FUN_80019c80(12, soundIndex, 1);
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
        _800c6898.deref()._34.sub((short)0x80);

        FUN_800ce4dc(_800c6898.deref());

        if(textboxes_800be358[6]._00 == 0 && textboxes_800be358[7]._00 == 0 && _800c6898.deref()._34.get() == 0) {
          _800c68a4.setu(0x9L);
        }

        //LAB_800e66cc
        break;

      case 6:
        if(_800c6858.getSigned() != 0) {
          _800c6858.setu(0);
          facing_800c67b4.set(1);
        } else {
          //LAB_800e6704
          _800c6858.setu(0x800L);
          facing_800c67b4.set(-1);
        }

        //LAB_800e671c
        _800c686c.setu(0x1L);
        _800c68a0.setu(0);
        _800c68a4.setu(0x7L);

      case 7:
        _800c68a0.addu(0x1L);

        if(_800c68a0.get() > 0x3L) {
          _800c68a4.setu(0x8L);
        }

        //LAB_800e6770
        break;

      case 8:
        _800c68a4.setu(0);
        _800c6868.setu(0);
        _800c686c.setu(0);
        _800c6894.setu(0);
        _800c68a8.setu(0x1L);

        //LAB_800e67a8
        for(int i = 0; i < 7; i++) {
          //LAB_800e67c4
          _800c86d4.offset(i * 0x4L).setu(0);
        }

        //LAB_800e67f8
        break;

      case 9:
        sp58 = _800c67a8.get();
        final int sp54 = 0x1 << (sp58 & 0x1f);
        sp58 = sp58 >>> 5;
        gameState_800babc8._17c.get((int)sp58).or(sp54);

        //LAB_800e6900
        if(_800c6860.get() != 999L) {
          submapCut_80052c30.set((int)_800c6860.get());
          submapScene_80052c34.set((int)_800c6862.get());
        } else {
          //LAB_800e693c
          submapCut_80052c30.set(_800f0e34.get((int)_800c67a8.get()).submapCut_04.get());

          if(_800c86d2.getSigned() == 0x1L) {
            sp20 = _800c6862.get() >>> 4 & 0xffffL;
          } else {
            //LAB_800e69a0
            sp20 = _800c6862.get() & 0xfL;
          }

          //LAB_800e69b8
          index_80052c38.set((int)sp20);
        }

        //LAB_800e69c4
        _800c6868.setu(0);
        break;
    }

    //LAB_800e69d4
  }

  @Method(0x800e69e8L)
  public static void FUN_800e69e8() {
    final SVECTOR sp0x30 = new SVECTOR();
    final MATRIX sp0x38 = new MATRIX();
    final SVECTOR sp58 = new SVECTOR();

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
    if(_800c68a8.get() == 0) {
      if((joypadPress_8007a398.get() & 0x800L) != 0) {
        playSound(0, 2, 0, 0, (short)0, (short)0);
        _800c68a8.setu(0x1L);

        //LAB_800e6aac
        for(int i = 0; i < 7; i++) {
          //LAB_800e6ac8
          _800c86d4.offset(i * 0x4L).setu(0);
        }
      }

      //LAB_800e6afc
    } else {
      //LAB_800e6b04
      if((joypadInput_8007a39c.get() & 0x800L) == 0) {
        //LAB_800e6b20
        for(int i = 0; i < 7; i++) {
          //LAB_800e6b3c
          FUN_8002a3ec(i, 0);
        }

        //LAB_800e6b6c
        _800c68a8.setu(0);
      }

      //LAB_800e6b74
      if((joypadInput_8007a39c.get() & 0x10L) != 0) {
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

    //LAB_800e6c38
    int count = 0;
    for(int i = 0; i < _800c86cc.get(); i++) {
      //LAB_800e6c5c
      if(!places_800f0234.get(_800f0e34.get((int)_800c84c8.offset(i * 0x2L).getSigned()).placeIndex_02.get()).name_00.isNull()) {
        //LAB_800e6ccc
        sp0x30.setX((short)_800c74b8.offset(i * 0x10L).offset(0x0L).get());
        sp0x30.setY((short)_800c74b8.offset(i * 0x10L).offset(0x4L).get());
        sp0x30.setZ((short)_800c74b8.offset(i * 0x10L).offset(0x8L).get());

        GsGetLs(struct258_800c66a8.tmdRendering_08.coord2s_04[0], sp0x38);
        setRotTransMatrix(sp0x38);

        CPU.MTC2(sp0x30.getXY(), 0); // VXY0
        CPU.MTC2(sp0x30.getZ(), 1); // VZ0
        CPU.COP2(0x18_0001L); // Perspective transform single
        sp58.setXY(CPU.MFC2(14)); // SXY2
        final long sp60 = CPU.MFC2(8); // IR0
        final long sp5c = CPU.CFC2(31); // FLAGS
        final int z = (int)CPU.MFC2(19) >> 2; // SZ3
        final short x = (short)(sp58.getX() + 160);
        final short y = (short)(sp58.getY() + 104);

        //LAB_800e6e24
        if(x >= -32 && x < 353) {
          //LAB_800e6e2c
          //LAB_800e6e5c
          if(y >= -32 && y < 273) {
            //LAB_800e6e64
            if(z >= 6 && z < orderingTableSize_1f8003c8.get() - 1) {
              final WMapStruct0c_2 struct = _800c68ac.get(count);
              struct.z_00.set(z);
              struct._04.set((int)_800c84c8.offset(i * 0x2L).getSigned());
              struct.xy_08.setXY(sp58.getXY());
              count++;
            }
          }
        }
      }
    }

    // Render world map place names when start is held down

    //LAB_800e6f54
    _800c68ac.get(count)._04.set(-1);

    qsort(_800c68ac.bound(WMapStruct0c_2.class, count), count, 0xc, getBiFunctionAddress(WMap.class, "sortPlaceNamesByZ", WMapStruct0c_2.class, WMapStruct0c_2.class, long.class));

    //LAB_800e6fa0
    int i;
    WMapStruct0c_2 struct;
    for(i = 0, struct = _800c68ac.get(0); i < 7 && struct._04.get() >= 0; i++, struct = _800c68ac.get(i)) {
      //LAB_800e6fec
      //LAB_800e6fec
      //LAB_800e6ff4
      final int x = struct.xy_08.getX() + 160;
      final int y = struct.xy_08.getY() + 104;
      final int place = _800f0e34.get(struct._04.get()).placeIndex_02.get();

      if(!places_800f0234.get(place).name_00.isNull()) {
        //LAB_800e70f4
        final IntRef width = new IntRef();
        final IntRef lines = new IntRef();
        measureText(places_800f0234.get(place).name_00.deref(), width, lines);

        final long v0 = _800c86d4.offset(i * 0x4L).get();
        if(v0 == 0x1L) {
          //LAB_800e71d8
          textZ_800bdf00.set(i + 14);
          textboxes_800be358[i].z_0c = i + 14;
          textboxes_800be358[i].chars_18 = Math.max(width.get(), 4);
          textboxes_800be358[i].lines_1a = lines.get();
          textboxes_800be358[i].width_1c = textboxes_800be358[i].chars_18 * 9 / 2;
          textboxes_800be358[i].height_1e = textboxes_800be358[i].lines_1a * 6;
          textboxes_800be358[i].x_14 = x;
          textboxes_800be358[i].y_16 = y;
          _800c86d4.offset(i * 0x4L).setu(0x2L);
        } else if((int)v0 >= 0x2L) {
          //LAB_800e7154
          if(v0 == 0x2L) {
            //LAB_800e72e8
            textboxes_800be358[i].chars_18 = Math.max(width.get(), 4);
            textboxes_800be358[i].lines_1a = lines.get();
            textboxes_800be358[i].width_1c = textboxes_800be358[i].chars_18 * 9 / 2;
            textboxes_800be358[i].height_1e = textboxes_800be358[i].lines_1a * 6;
            textboxes_800be358[i].x_14 = x;
            textboxes_800be358[i].y_16 = y;
          }
        } else if(v0 == 0) {
          //LAB_800e7168
          FUN_8002a32c(i, 0, x, y, width.get() - 1, lines.get() - 1);

          _800c86d4.offset(i * 0x4L).setu(0x1L);

          //LAB_800e71d8
          textZ_800bdf00.set(i + 14);
          textboxes_800be358[i].chars_18 = Math.max(width.get(), 4);
          textboxes_800be358[i].lines_1a = lines.get();
          textboxes_800be358[i].width_1c = textboxes_800be358[i].chars_18 * 9 / 2;
          textboxes_800be358[i].height_1e = textboxes_800be358[i].lines_1a * 6;
          textboxes_800be358[i].x_14 = x;
          textboxes_800be358[i].y_16 = y;
          textboxes_800be358[i].z_0c = i + 14;
          _800c86d4.offset(i * 0x4L).setu(0x2L);
        }

        //LAB_800e74d8
        textZ_800bdf00.set(i + 119);
        textboxes_800be358[i].z_0c = i + 119;

        FUN_800e774c(places_800f0234.get(place).name_00.deref(), (short)(x - width.get() * 3), (short)(y - lines.get() * 7), 0, 0);
      }

      //LAB_800e7590
    }

    //LAB_800e75a8
    for(; i < 7; i++) {
      //LAB_800e75c4
      FUN_8002a3ec(i, 0);
      _800c86d4.offset(i * 0x4L).setu(0);
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
  public static void FUN_800e774c(final LodString text, final int x, final int y, final long a3, final long a4) {
    final IntRef width = new IntRef();
    final IntRef lines = new IntRef();
    measureText(text, width, lines);
    renderText(text, x - width.get() + 3, y, (short)a3, (short)a4);
    renderText(text, x - (width.get() - 1) + 3, y + 1, 9, (short)a4);
  }

  @Method(0x800e7854L)
  public static long sortPlaceNamesByZ(final WMapStruct0c_2 a, final WMapStruct0c_2 b) {
    return a.z_00.get() - b.z_00.get();
  }

  @Method(0x800e7888L)
  public static void FUN_800e7888() {
    FUN_800d15d8(_800c6898.deref());
    FUN_800d15d8(_800c689c.deref());
  }

  @Method(0x800e78c0L)
  public static void FUN_800e78c0() {
    //LAB_800e78d4
    for(int i = 0; i < 8; i++) {
      //LAB_800e78f0
      gameState_800babc8._4b8.get(i).set(gameState_800babc8._15c.get(i).get());
    }

    //LAB_800e7940
    //LAB_800e7944
    for(int i = 0; _800f5a6c.offset(i * 0x2cL).getSigned() != -1; i++) {
      //LAB_800e7984
      long sp48 = _800f5a6c.offset(i * 0x2cL).getSigned();
      final long sp4c = 0x1L << (sp48 & 0x1fL);
      sp48 = sp48 >>> 5;

      if((gameState_800babc8.scriptFlags2_bc.get((int)sp48).get() & sp4c) != 0) {
        //LAB_800e7a38
        //LAB_800e7a3c
        for(int sp24 = 0; sp24 < 8; sp24++) {
          //LAB_800e7a58
          gameState_800babc8._15c.get(sp24).set((int)_800f5a70.offset(i * 0x2cL).offset(sp24 * 0x4L).get());
        }
      }

      //LAB_800e7acc
    }

    //LAB_800e7ae4
    _800c685c.setu(submapCut_80052c30.get());
    _800c685e.setu(index_80052c38.get());

    if(_800c685c.get() == 0 && _800c685e.get() == 0) {
      _800c685c.setu(0xdL);
      _800c685e.setu(0x11L);
    }

    //LAB_800e7b44
    //LAB_800e7b54
    boolean sp18 = false;
    int sp1c;
    for(sp1c = 0; sp1c < 0x100; sp1c++) {
      //LAB_800e7b70
      if(_800f0e34.get(sp1c).submapCut_04.get() == _800c685c.getSigned() && _800f0e34.get(sp1c)._06.get() == _800c685e.getSigned()) {
        sp18 = true;
        break;
      }

      //LAB_800e7bc0
    }

    //LAB_800e7be8
    if(!sp18) {
      _800c685c.setu(0xdL);
      _800c685e.setu(0x11L);
      sp1c = 5;
    }

    //LAB_800e7c18
    long sp4c = 0x1L << (sp1c & 0x1fL);
    long sp50 = sp1c >>> 5;

    if((gameState_800babc8._15c.get((int)sp50).get() & sp4c) == 0) {
      _800c685c.setu(0xdL);
      _800c685e.setu(0x11L);
      sp1c = 5;
    }

    //LAB_800e7cb8
    //LAB_800e7cbc
    int sp24;
    for(sp24 = 0; _800f0e34.get(sp24).areaIndex_00.get() != -2; sp24++) {
      // intentionally empty
    }

    //LAB_800e7d0c
    _800c67a0.setu(sp24);

    //LAB_800e7d1c
    for(sp24 = 0; areaData_800f2248.get(sp24)._00.get() != 0; sp24++) {
      // intentionally empty
    }

    //LAB_800e7d64
    _800c67a4.setu(sp24);

    GsInitCoordinate2(null, struct258_800c66a8.coord2_34);

    _800c6798.setu(_800f0e34.get(sp1c)._0e.get() - 0x1L);
    _800c679c.setu(_800c6798.get());
    continentIndex_800bf0b0.set((int)_800c6798.get());

    FUN_800ea630(sp1c);

    _800c6870.setu(0);

    long sp2c;
    if(previousMainCallbackIndex_8004dd28.get() == 6 && _800c685c.get() != 999L) {
      sp2c = 1;
    } else {
      sp2c = 0;
    }

    //LAB_800e7e2c
    if(_800c685e.get() == 0x1fL && _800c685c.get() == 279L) {
      sp2c = 1;
    }

    //LAB_800e7e5c
    //LAB_800e7e88
    if(sp2c == 0 && _800bdc34.get() == 0 || _80052c6c.get() != 0) {
      //LAB_800e844c
      _800c686c.setu(0x1L);
      _800c6868.setu(0x1L);
    } else {
      // Transition from combat to world map (maybe also from smap?)
      areaIndex_800c67aa.set(gameState_800babc8.areaIndex_4de.get());
      pathIndex_800c67ac.set(gameState_800babc8.pathIndex_4d8.get());
      dotIndex_800c67ae.set((short)gameState_800babc8.dotIndex_4da.get());
      dotOffset_800c67b0.set((short)gameState_800babc8.dotOffset_4dc.get());
      facing_800c67b4.set(gameState_800babc8.facing_4dd.get());
      _800c686c.setu(0);
      _800c6868.setu(0);

      //LAB_800e7f00
      for(int i = 0; i < _800c67a0.get(); i++) {
        //LAB_800e7f24
        final int areaIndex = _800f0e34.get(i).areaIndex_00.get();

        if(areaIndex != -1) {
          //LAB_800e7f68
          if(FUN_800eb09c(i, -1, null) == 0) {
            //LAB_800e7f88
            if(areaIndex == areaIndex_800c67aa.get()) {
              sp1c = i;
              break;
            }
          }
        }

        //LAB_800e7fb4
      }

      //LAB_800e7fcc
      _800c67a8.setu(sp1c);
      _800c6798.setu(_800f0e34.get(sp1c)._0e.get() - 0x1L);
      _800c679c.setu(_800c6798.get());
      continentIndex_800bf0b0.set((int)_800c6798.get());

      final WMapAreaData08 area = areaData_800f2248.get(areaIndex_800c67aa.get());
      if(area._00.get() < 0) {
        sp4c = -1;
      } else {
        //LAB_800e8064
        sp4c = 1;
      }

      //LAB_800e8068
      final UnboundedArrayRef<VECTOR> sp48 = pathDotPosPtrArr_800f591c.get(pathIndex_800c67ac.get()).deref();

      final int dx;
      final int dz;
      if((int)sp4c > 0) {
        struct258_800c66a8.coord2_34.coord.transfer.set(sp48.get(0).getX(), sp48.get(0).getY() - 2, sp48.get(0).getZ());

        dx = sp48.get(0).getX() - sp48.get(1).getX();
        dz = sp48.get(0).getZ() - sp48.get(1).getZ();

        _800c6858.setu(0);
      } else {
        //LAB_800e8190
        final int index = (int)_800f5810.offset((Math.abs(area._00.get()) - 1) * 0x4L).get() - 1;
        dx = sp48.get(index).getX() - sp48.get(index - 1).getX();
        dz = sp48.get(index).getZ() - sp48.get(index - 1).getZ();

        struct258_800c66a8.coord2_34.coord.transfer.set(sp48.get(index).getX(), sp48.get(index).getY() - 2, sp48.get(index).getZ());

        _800c6858.setu(0x800L);
      }

      //LAB_800e838c
      struct258_800c66a8.rotation_a4.set((short)0, (short)ratan2(dx, dz), (short)0);
      previousPlayerRotation_800c685a.set(struct258_800c66a8.rotation_a4.getY());
      struct258_800c66a8.rotation_a4.y.add((short)_800c6858.get());

      _800c6890.setu(0);
      _800c6894.setu(0);
      _800bdc34.setu(0);
    }

    //LAB_800e8464
    if(previousMainCallbackIndex_8004dd28.get() == 6 && _800c685c.get() == 999L) {
      submapCut_80052c30.set(0);
    }

    //LAB_800e8494
    _800c6860.setu(_800f0e34.get(sp1c)._08.get());
    _800c6862.setu(_800f0e34.get(sp1c)._0a.get());

    final VECTOR sp0x58 = new VECTOR();
    final VECTOR sp0x68 = new VECTOR();
    final VECTOR sp0x78 = new VECTOR();

    getPathPositions(sp0x68, sp0x78);
    weightedAvg(4 - dotOffset_800c67b0.get(), dotOffset_800c67b0.get(), sp0x58, sp0x68, sp0x78);

    struct258_800c66a8.coord2_34.coord.transfer.set(sp0x58).div(0x1000);
    struct258_800c66a8.coord2_34.coord.transfer.y.sub(2);

    if(_800c685c.get() == 0xf2L) {
      if(_800c685e.get() == 0x3L) {
        sp50 = 0x8fL;
        sp4c = 0x1L << (sp50 & 0x1fL);
        sp50 = sp50 >>> 5;

        if((gameState_800babc8.scriptFlags2_bc.get((int)sp50).get() & sp4c) != 0) {
          _800c686c.setu(0);
          _800c6870.setu(0x1L);
          _800c6868.setu(0x1L);
        }

        //LAB_800e8684
        sp50 = 0x90L;
        sp4c = 0x1L << (sp50 & 0x1fL);
        sp50 = sp50 >>> 5;

        if((gameState_800babc8.scriptFlags2_bc.get((int)sp50).get() & sp4c) != 0) {
          _800c6868.setu(0x1L);
          _800c686c.setu(0x1L);
          _800c6870.setu(0);
        }
      }
    }

    //LAB_800e8720
    struct258_800c66a8._250 = 0;
    struct258_800c66a8._254 = 0;

    //LAB_800e8770
    //LAB_800e87a0
    //LAB_800e87d0
    //LAB_800e8800
    if(_800c685c.get() == 0x210L && _800c685e.get() == 0xdL || _800c685c.get() == 0x210L && _800c685e.get() == 0xeL || _800c685c.get() == 0x210L && _800c685e.get() == 0xfL || _800c685c.get() == 0x21cL && _800c685e.get() == 0x13L || _800c685c.get() == 0x23cL && _800c685e.get() == 0x17L) {
      //LAB_800e8830
      struct258_800c66a8._250 = 1;
      //LAB_800e8848
    } else if(_800c685c.get() == 0x211L && _800c685e.get() == 0x29L) {
      struct258_800c66a8._250 = 2;
      struct258_800c66a8._254 = 1;

      sp50 = _800c67a8.get();
      sp4c = 0x1L << (sp50 & 0x1fL);
      sp50 = sp50 >>> 5;
      gameState_800babc8._17c.get((int)sp50).or((int)sp4c);
    }

    //LAB_800e8990
    _800c68a4.setu(0);
    _800c68a8.setu(0);

    //LAB_800e89a4
    for(int i = 0; i < 8; i++) {
      //LAB_800e89c0
      _800c86d4.offset(i * 0x4L).setu(0);
    }

    //LAB_800e89f4

    FUN_800eb3c8();
  }

  @Method(0x800e8a10L)
  public static void FUN_800e8a10() {
    //LAB_800e8a38
    if((int)worldMapState_800c6698.get() >= 0x4L && (int)playerState_800c669c.get() >= 0x4L) {
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
    if(_800c6870.get() != 0) {
      _800c6868.setu(0x1L);

      if(struct258_800c66a8._05 != 0) {
        return;
      }

      //LAB_800e8ae0
    } else {
      //LAB_800e8ae8
      if(_800c686c.get() == 0) {
        return;
      }

      //LAB_800e8b04
      if(struct258_800c66a8.modelIndex_1e4 >= 2) {
        return;
      }
    }

    //LAB_800e8b2c
    final int sp4;
    if(areaData_800f2248.get(areaIndex_800c67aa.get())._00.get() < 0) {
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
    if(sp4 < 0 && facing_800c67b4.get() > 0 || sp4 > 0 && facing_800c67b4.get() < 0) {
      //LAB_800e8bec
      sp0 = -sp0;
    }

    //LAB_800e8bfc
    if(_800c686c.get() == 0x2L || _800c6870.get() == 0x2L) {
      //LAB_800e8c2c
      sp0 *= 2;
    }

    //LAB_800e8c40
    if(sp0 < 0) {
      _800c6858.setu(0x800L);
      facing_800c67b4.set(-1);
    } else {
      //LAB_800e8c70
      _800c6858.setu(0);
      facing_800c67b4.set(1);
    }

    //LAB_800e8c84
    dotOffset_800c67b0.add((short)sp0);

    //LAB_800e8ca0
  }

  @Method(0x800e8cb0L)
  public static void FUN_800e8cb0() {
    if(_800c6890.get() != 0) {
      return;
    }

    //LAB_800e8cd8
    FUN_800e8a90();

    if(_800c6868.get() == 0) {
      processInput();
    }

    //LAB_800e8cfc
    if(dotOffset_800c67b0.get() >= 4) {
      dotIndex_800c67ae.incr();

      //LAB_800e8d48
      dotOffset_800c67b0.mod((short)4);

      final int sp10 = (int)_800f5810.offset((Math.abs(areaData_800f2248.get(areaIndex_800c67aa.get())._00.get()) - 1) * 0x4L).get() - 1;

      if(dotIndex_800c67ae.get() >= sp10) {
        dotIndex_800c67ae.set((short)(sp10 - 1));
        dotOffset_800c67b0.set((short)3);
        _800c6890.setu(0x2L);
      }

      //LAB_800e8dfc
      //LAB_800e8e04
    } else if(dotOffset_800c67b0.get() < 0) {
      dotIndex_800c67ae.decr();
      dotOffset_800c67b0.add((short)4);

      if(dotIndex_800c67ae.get() < 0) {
        dotIndex_800c67ae.set((short)0);
        dotOffset_800c67b0.set((short)0);
        _800c6890.setu(0x1L);
      }
    }

    //LAB_800e8e78
    FUN_800e8e94();

    //LAB_800e8e80
  }

  @Method(0x800e8e94L)
  public static void FUN_800e8e94() {
    long sp10 = 0x97L;
    final long sp14 = 0x1L << (sp10 & 0x1fL);
    sp10 = sp10 >>> 5;

    if((gameState_800babc8.scriptFlags2_bc.get((int)sp10).get() & sp14) != 0) {
      //LAB_800e8f24
      if(struct258_800c66a8.modelIndex_1e4 == 1) {
        //LAB_800e8f48
        if(_800c6870.get() == 0) {
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
                          if((joypadPress_8007a398.get() & 0x80L) != 0) { // Square
                            if(_800c6894.get() != 0x1L) {
                              _800c6860.setu(_800f1580.get());
                              _800c6862.setu(_800f1582.get());
                              submapCut_80052c30.set((int)_800c6860.get());
                              submapScene_80052c34.set((int)_800c6862.get());
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
    if(worldMapState_800c6698.get() != 0x5L) {
      return;
    }

    //LAB_800e9194
    if(playerState_800c669c.get() != 0x5L) {
      return;
    }

    //LAB_800e91b0
    if(_800c6870.get() != 0) {
      return;
    }

    //LAB_800e91cc
    final int sp0 = _800c66b0.mapRotation_70.getY() - previousPlayerRotation_800c685a.get() - 0x700 & 0xfff;
    final long sp10 = (_800bee90.get() & 0xffff) >>> 12;
    int sp4 = 0;

    if(sp10 != 0) {
      final int sp2 = sp0 >> 9;

      final long sp8 = sp10 & _800f0204.offset(sp2).get();
      final long spc = sp10 & _800f0210.offset(sp2).get();

      if(sp8 == 0 || spc != 0) {
        //LAB_800e92d0
        if(sp8 != 0 || spc == 0) {
          //LAB_800e9300
          sp4 = facing_800c67b4.get();
        } else {
          sp4 = -1;
        }
      } else {
        sp4 = 1;
      }

      //LAB_800e9310
      if(sp4 == 0) {
        sp4 = facing_800c67b4.get();
      }
    }

    //LAB_800e9330
    //LAB_800e9364
    if((joypadInput_8007a39c.get() & 0x40L) != 0) {
      //LAB_800e9384
      sp4 *= 2; // Running
    }

    //LAB_800e9398
    dotOffset_800c67b0.add((short)sp4);

    if(sp4 != 0) {
      if(sp4 < 0) {
        _800c6858.setu(0x800L);
        facing_800c67b4.set(-1);
      } else {
        //LAB_800e93f4
        _800c6858.setu(0);
        facing_800c67b4.set(1);
      }
    }

    //LAB_800e9408
  }

  @Method(0x800e9418L)
  public static void getPathPositions(final VECTOR playerPos, final VECTOR nextPathPos) {
    final UnboundedArrayRef<VECTOR> dots = pathDotPosPtrArr_800f591c.get(pathIndex_800c67ac.get()).deref();
    playerPos.set(dots.get(dotIndex_800c67ae.get()));
    nextPathPos.set(dots.get(dotIndex_800c67ae.get() + 1));
  }

  @Method(0x800e94f0L)
  public static void weightedAvg(final int weight1, final int weight2, final VECTOR out, final VECTOR vec1, final VECTOR vec2) {
    out.setX((weight1 * vec1.getX() + weight2 * vec2.getX()) / (weight1 + weight2) * 0x1000);
    out.setY((weight1 * vec1.getY() + weight2 * vec2.getY()) / (weight1 + weight2) * 0x1000);
    out.setZ((weight1 * vec1.getZ() + weight2 * vec2.getZ()) / (weight1 + weight2) * 0x1000);
  }

  @Method(0x800e9648L)
  public static void updatePlayerRotation() {
    final WMapStruct258 struct258 = struct258_800c66a8;
    struct258.rotation_a4.set((short)0, (short)ratan2(playerPos_800c67b8.getX() - nextDotPos_800c67c8.getX(), playerPos_800c67b8.getZ() - nextDotPos_800c67c8.getZ()), (short)0);
    previousPlayerRotation_800c685a.set(struct258.rotation_a4.getY());
    struct258.rotation_a4.y.add((short)_800c6858.get());
  }

  @Method(0x800e975cL)
  public static void FUN_800e975c() {
    if(_800c6890.get() == 0) {
      return;
    }

    //LAB_800e9784
    //LAB_800e9788
    for(int i = 0; i < 7; i++) {
      //LAB_800e97a4
      _800c6874.get(i).set(-1);
    }

    final VECTOR sp0x10 = new VECTOR();
    final VECTOR sp0x20 = new VECTOR();
    final VECTOR sp0x30 = new VECTOR();

    //LAB_800e97dc
    getPathPositions(sp0x10, sp0x20);

    if(_800c6890.get() == 0x1L) {
      sp0x30.set(sp0x10);
    } else {
      //LAB_800e9834
      sp0x30.set(sp0x20);
    }

    //LAB_800e985c
    int sp4c = 0;

    //LAB_800e9864
    for(int i = 0; i < _800c67a0.get(); i++) {
      //LAB_800e9888
      if(FUN_800eb09c(i, 0, null) == 0) {
        //LAB_800e98a8
        if(_800f0e34.get(i)._0c.get() != -1) {
          //LAB_800e98e0
          final int sp54 = _800f0e34.get(i).areaIndex_00.get();
          final int sp50 = areaData_800f2248.get(sp54)._00.get();

          if(facing_800c67b4.get() <= 0 || sp50 >= 0) {
            //LAB_800e995c
            if(facing_800c67b4.get() >= 0 || sp50 <= 0) {
              //LAB_800e9988
              final int pathIndex = Math.abs(sp50) - 1;
              final int dotIndex = (int)_800f5810.offset(pathIndex * 0x4L).get();
              final UnboundedArrayRef<VECTOR> dots = pathDotPosPtrArr_800f591c.get(pathIndex).deref();
              sp0x10.set(dots.get(dotIndex - 1));
              sp0x20.set(dots.get(0));

              if(sp0x30.getX() == sp0x10.getX() && sp0x30.getY() == sp0x10.getY() && sp0x30.getZ() == sp0x10.getZ()) {
                _800c67d8.get(sp4c).set(dots.get(dotIndex - 2));
                _800c6874.get(sp4c).set(sp54);
                sp4c++;
                //LAB_800e9bd8
              } else if(sp0x30.getX() == sp0x20.getX() && sp0x30.getY() == sp0x20.getY() && sp0x30.getZ() == sp0x20.getZ()) {
                _800c67d8.get(sp4c).set(dots.get(1));
                _800c6874.get(sp4c).set(sp54);
                sp4c++;
              }
            }
          }
        }
      }

      //LAB_800e9ce0
    }

    //LAB_800e9cf8
    _800c6848.set(sp0x30);
    _800c6890.setu(0);

    if(sp4c == 1) {
      _800c6894.setu(0x1L);
    } else {
      //LAB_800e9d48
      _800c6894.setu(0x2L);
    }

    //LAB_800e9d54
  }

  @Method(0x800e9d68L)
  public static void FUN_800e9d68() {
    long v0;
    long sp14;
    final VECTOR sp0xb0 = new VECTOR();
    final short[] sp0xc8 = new short[7];
    long spd8;

    int sp18 = 0;
    long sp28 = 0;
    long spda = 0x1000L;

    if(_800c6894.get() != 0x2L) {
      return;
    }

    //LAB_800e9da0
    if(_800c6870.get() != 0) {
      if((int)_800c6870.get() < 0x3L) {
        FUN_800e3fac(1);
        submapCut_80052c30.set(285); // I think this is a Queen Fury cut
        submapScene_80052c34.set(32);
        _800c6870.setu(0x3L);
      }

      //LAB_800e9dfc
      return;
    }

    //LAB_800e9e04
    if(_800c6868.get() != 0) {
      return;
    }

    //LAB_800e9e20
    final int spa0 = struct258_800c66a8.vec_94.getX() >> 12;
    final int spa4 = struct258_800c66a8.vec_94.getY() >> 12;
    final int spa8 = struct258_800c66a8.vec_94.getZ() >> 12;
    final long spe0 = (_800bee90.get() & 0xffff) >>> 12;

    //LAB_800e9e90
    for(int i = 0; i < 7; i++) {
      //LAB_800e9eac
      if(_800c6874.get(i).get() < 0) {
        break;
      }

      //LAB_800e9edc
      sp0xb0.setX(spa0 - _800c67d8.get(i).getX());
      sp0xb0.setY(spa4 - _800c67d8.get(i).getY());
      sp0xb0.setZ(spa8 - _800c67d8.get(i).getZ());

      sp0xc8[i] = (short)(_800c66b0.mapRotation_70.getY() - ratan2(sp0xb0.getX(), sp0xb0.getZ()) + 0x800 & 0xfff);
      v0 = sp0xc8[i] + 0x100L & 0xfffL;
      v0 = (int)v0 >> 9;

      if((_800f0204.offset((short)v0).get() & spe0) != 0) {
        spd8 = spda;
        spda = Math.abs(sp0xc8[i] - _800f021c.offset((spe0 - 1) * 0x2L).getSigned());
        sp14 = Math.abs(sp0xc8[i] - _800f021c.offset((spe0 - 1) * 0x2L).getSigned() - 0x1000L);

        if(sp14 < (short)spda) {
          spda = sp14;
        }

        //LAB_800ea118
        if((short)spd8 >= (short)spda) {
          sp18 = i;
        }

        //LAB_800ea13c
        sp28 = 0x1L;
      }

      //LAB_800ea144
    }

    //LAB_800ea15c
    if(sp28 == 0) {
      return;
    }

    //LAB_800ea174
    _800c6894.setu(0);

    FUN_800ea4dc(_800c6874.get(sp18).get());

    final WMapAreaData08 area = areaData_800f2248.get(areaIndex_800c67aa.get());
    final int sp20;
    if(area._00.get() < 0) {
      sp20 = -1;
    } else {
      //LAB_800ea1d8
      sp20 = 1;
    }

    //LAB_800ea1dc
    final UnboundedArrayRef<VECTOR> dots = pathDotPosPtrArr_800f591c.get(pathIndex_800c67ac.get()).deref();

    if(sp20 > 0) {
      sp0xb0.set(dots.get(0));
    } else {
      //LAB_800ea248
      sp0xb0.set(dots.get((int)_800f5810.offset(pathIndex_800c67ac.get() * 0x4L).get() - 1));
    }

    //LAB_800ea2a8
    if(_800c6848.getX() != sp0xb0.getX() || _800c6848.getY() != sp0xb0.getY() || _800c6848.getZ() != sp0xb0.getZ()) {
      //LAB_800ea2f8
      if(sp20 > 0) {
        dotIndex_800c67ae.set((short)(_800f5810.offset((Math.abs(area._00.get()) - 1) * 0x4L).get() - 2));
        dotOffset_800c67b0.set((short)2);
        facing_800c67b4.set(-1);
        _800c6858.setu(0x800L);
      } else {
        //LAB_800ea39c
        dotIndex_800c67ae.set((short)0);
        dotOffset_800c67b0.set((short)1);
        facing_800c67b4.set(1);
        _800c6858.setu(0);
      }
    }

    //LAB_800ea3c4
  }

  @Method(0x800ea3d8L)
  public static void updatePlayer() {
    final VECTOR playerPos = new VECTOR();
    final VECTOR nextDotPos = new VECTOR();

    getPathPositions(playerPos, nextDotPos);
    weightedAvg(4 - dotOffset_800c67b0.get(), dotOffset_800c67b0.get(), struct258_800c66a8.vec_94, playerPos, nextDotPos);
    struct258_800c66a8.vec_94.y.sub(0x2000);
    playerPos_800c67b8.set(playerPos);
    nextDotPos_800c67c8.set(nextDotPos);

    updatePlayerRotation();
  }

  @Method(0x800ea4dcL)
  public static void FUN_800ea4dc(final int areaIndex) {
    areaIndex_800c67aa.set(areaIndex);

    //LAB_800ea4fc
    int i;
    for(i = 0; i < _800c67a0.get(); i++) {
      //LAB_800ea520
      if(_800f0e34.get(i).areaIndex_00.get() != -1) {
        //LAB_800ea558
        if(FUN_800eb09c(i, 0, null) == 0) {
          //LAB_800ea578
          if(_800f0e34.get(i)._0e.get() == _800c6798.get() + 1) {
            //LAB_800ea5bc
            if(_800f0e34.get(i).areaIndex_00.get() == areaIndex) {
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
  public static void FUN_800ea630(final int a0) {
    if(_800f0e34.get(a0).areaIndex_00.get() == -1) {
      return;
    }

    //LAB_800ea678
    if(_800f0e34.get(a0)._0e.get() != _800c6798.get() + 1) {
      return;
    }

    //LAB_800ea6bc
    if(FUN_800eb09c(a0, 0, null) != 0) {
      return;
    }

    //LAB_800ea6dc
    _800c67a8.setu(a0);
    areaIndex_800c67aa.set(_800f0e34.get(a0).areaIndex_00.get());

    final WMapAreaData08 area = areaData_800f2248.get(areaIndex_800c67aa.get());
    pathIndex_800c67ac.set(Math.abs(area._00.get()) - 1); // Transition to a different path

    final int sign;
    if(area._00.get() < 0) {
      sign = -1;
    } else {
      //LAB_800ea78c
      sign = 1;
    }

    //LAB_800ea790
    final WMapStruct258 struct258 = struct258_800c66a8;
    final UnboundedArrayRef<VECTOR> dots = pathDotPosPtrArr_800f591c.get(pathIndex_800c67ac.get()).deref();

    final int dx;
    final int dz;
    if(sign > 0) {
      struct258.coord2_34.coord.transfer.set(dots.get(0).getX(), dots.get(0).getY() - 2, dots.get(0).getZ());

      dx = dots.get(0).getX() - dots.get(1).getX();
      dz = dots.get(0).getZ() - dots.get(1).getZ();

      dotIndex_800c67ae.set((short)0);
      dotOffset_800c67b0.set((short)0);
      _800c6858.setu(0);
      facing_800c67b4.set(1);
    } else {
      //LAB_800ea8d4
      final int dotIndex = (int)_800f5810.offset((Math.abs(area._00.get()) - 1) * 0x4L).get() - 1;
      dx = dots.get(dotIndex).getX() - dots.get(dotIndex - 1).getX();
      dz = dots.get(dotIndex).getZ() - dots.get(dotIndex - 1).getZ();

      struct258.coord2_34.coord.transfer.set(dots.get(dotIndex).getX(), dots.get(dotIndex).getY() - 2, dots.get(dotIndex).getZ());

      dotIndex_800c67ae.set((short)(dotIndex - 1));
      dotOffset_800c67b0.set((short)3);
      _800c6858.setu(0x800L);
      facing_800c67b4.set(-1);
    }

    //LAB_800eaafc
    struct258.rotation_a4.set((short)0, (short)ratan2(dx, dz), (short)0);

    previousPlayerRotation_800c685a.set(struct258.rotation_a4.getY());
    _800c6890.setu(0);
    _800c6894.setu(0);

    //LAB_800eab80
  }

  @Method(0x800eab94L)
  public static void FUN_800eab94(final int a0) {
    if(_800f0e34.get(a0).areaIndex_00.get() == -1) {
      return;
    }

    //LAB_800eabdc
    if(_800f0e34.get(a0)._0e.get() != _800c6798.get() + 1) {
      return;
    }

    //LAB_800eac20
    if(FUN_800eb09c(a0, 0, null) != 0) {
      return;
    }

    //LAB_800eac40
    _800c67a8.setu(a0);
    areaIndex_800c67aa.set(_800f0e34.get(a0).areaIndex_00.get());

    final WMapAreaData08 areaData = areaData_800f2248.get(areaIndex_800c67aa.get());
    pathIndex_800c67ac.set(Math.abs(areaData._00.get()) - 1);

    final WMapStruct258 struct258 = struct258_800c66a8;
    final UnboundedArrayRef<VECTOR> dots = pathDotPosPtrArr_800f591c.get(pathIndex_800c67ac.get()).deref();

    final int dx;
    final int dz;
    if(facing_800c67b4.get() > 0) {
      final int dotIndex = (int)_800f5810.offset((Math.abs(areaData._00.get()) - 1) * 0x4L).get() - 1;
      struct258.coord2_34.coord.transfer.set(dots.get(dotIndex).getX(), dots.get(dotIndex).getY() - 2, dots.get(dotIndex).getZ());
      dotIndex_800c67ae.set((short)(dotIndex - 1));
      dotOffset_800c67b0.set((short)3);
      _800c6858.setu(0);
      dx = dots.get(dotIndex).getX() - dots.get(dotIndex - 1).getX();
      dz = dots.get(dotIndex).getZ() - dots.get(dotIndex - 1).getZ();
    } else {
      //LAB_800eaf14
      struct258.coord2_34.coord.transfer.set(dots.get(0).getX(), dots.get(0).getY() - 2, dots.get(0).getZ());
      dotIndex_800c67ae.set((short)0);
      dotOffset_800c67b0.set((short)0);
      _800c6858.setu(0x800);
      dx = dots.get(0).getX() - dots.get(1).getX();
      dz = dots.get(0).getZ() - dots.get(1).getZ();
    }

    //LAB_800eb00c
    struct258.rotation_a4.set((short)0, (short)ratan2(dx, dz), (short)0);
    previousPlayerRotation_800c685a.set(struct258.rotation_a4.getY());
    _800c6894.setu(0);

    //LAB_800eb088
  }

  /**
   * a1 used to be either 0, -1, or a VECTOR. If passing a VECTOR, pass it as vec and set a1 to 1
   */
  @Method(0x800eb09cL)
  public static int FUN_800eb09c(final int a0, final int a1, @Nullable final VECTOR vec) {
    if(_800f0e34.get(a0).areaIndex_00.get() == -1) {
      return -1;
    }

    //LAB_800eb0ec
    if(a1 != -1) {
      if(_800f0e34.get(a0)._0e.get() != _800c6798.get() + 1) {
        return -2;
      }
    }

    //LAB_800eb144
    if((gameState_800babc8._15c.get(a0 >>> 5).get() & 0x1 << (a0 & 0x1f)) == 0) {
      return 1;
    }

    //LAB_800eb1d0
    if(a1 == 0 || a1 == -1) {
      //LAB_800eb1f8
      return 0;
    }

    //LAB_800eb204
    final int sp14 = areaData_800f2248.get(_800f0e34.get(a0).areaIndex_00.get())._00.get();

    if(sp14 == 0) {
      return -3;
    }

    //LAB_800eb264
    final int sp18 = Math.abs(sp14) - 1;
    final UnboundedArrayRef<VECTOR> v1 = pathDotPosPtrArr_800f591c.get(sp18).deref();

    if(sp14 > 0) {
      vec.set(v1.get(0));
    } else {
      //LAB_800eb2fc
      final int v0 = (int)_800f5810.offset(sp18 * 0x4L).get() - 1;
      vec.set(v1.get(v0));
    }

    //LAB_800eb3a8
    //LAB_800eb3b4
    return 0;
  }

  @Method(0x800eb3c8L)
  public static void FUN_800eb3c8() {
    final byte[] sp0xd0 = new byte[0x101];
    long sp24 = 0;

    //LAB_800eb3dc
    for(int i = 0; i < 0x101; i++) {
      //LAB_800eb3f8
      sp0xd0[i] = 0;
    }

    final VECTOR sp0x30 = new VECTOR();
    final VECTOR sp0x40 = new VECTOR();
    final VECTOR sp0x50 = new VECTOR();
    final VECTOR[] sp0x60 = new VECTOR[0x101];

    for(int i = 0; i < sp0x60.length; i++) {
      sp0x60[i] = new VECTOR();
    }

    //LAB_800eb420
    //LAB_800eb424
    for(int i = 0; i < _800c67a0.get(); i++) {
      //LAB_800eb448
      if(FUN_800eb09c(i, 0, null) == 0) {
        //LAB_800eb468
        if(sp0xd0[i] == 0) {
          //LAB_800eb48c
          final long sp28 = _800f0e34.get(i).placeIndex_02.get();
          int sp20 = 0;

          //LAB_800eb4c8
          for(int sp1c = i; sp1c < _800c67a0.get(); sp1c++) {
            //LAB_800eb4ec
            if(FUN_800eb09c(sp1c, 0, null) == 0) {
              //LAB_800eb50c
              if(sp0xd0[sp1c] == 0) {
                //LAB_800eb530
                final long sp2c = _800f0e34.get(sp1c).placeIndex_02.get();

                if(!places_800f0234.get((int)sp28).name_00.isNull() || !places_800f0234.get((int)sp2c).name_00.isNull()) {
                  // Added this check since these pointers can be null
                  if(!places_800f0234.get((int)sp28).name_00.isNull() && !places_800f0234.get((int)sp2c).name_00.isNull()) {
                    //LAB_800eb5d8
                    if(strcmp(MEMORY.ref(1, places_800f0234.get((int)sp28).name_00.getPointer()).getString(), MEMORY.ref(1, places_800f0234.get((int)sp2c).name_00.getPointer()).getString()) == 0) {
                      FUN_800eb09c(sp1c, 1, sp0x60[sp20]);

                      sp20++;
                      sp0xd0[sp1c] = 1;
                    }
                  }
                } else {
                  sp0xd0[sp1c] = 1;
                }
              }
            }

            //LAB_800eb67c
          }

          //LAB_800eb694
          if(sp20 == 0x1L) {
            _800c74b8.offset(sp24 * 0x10L).offset(0x0L).setu(sp0x60[0].getX());
            _800c74b8.offset(sp24 * 0x10L).offset(0x4L).setu(sp0x60[0].getY());
            _800c74b8.offset(sp24 * 0x10L).offset(0x8L).setu(sp0x60[0].getZ());
          } else {
            //LAB_800eb724
            sp0x30.set(sp0x60[0]);

            //LAB_800eb750
            for(int sp1c = 0; sp1c < sp20 - 1; sp1c++) {
              //LAB_800eb778
              sp0x40.set(sp0x60[sp1c + 1]);
              weightedAvg(1, 1, sp0x50, sp0x30, sp0x40);
              sp0x30.set(sp0x50).div(0x1000);
            }

            //LAB_800eb828
            _800c74b8.offset(sp24 * 0x10L).offset(0x0L).setu(sp0x50.getX() >> 12);
            _800c74b8.offset(sp24 * 0x10L).offset(0x4L).setu(sp0x50.getY() >> 12);
            _800c74b8.offset(sp24 * 0x10L).offset(0x8L).setu(sp0x50.getZ() >> 12);
          }

          //LAB_800eb8ac
          _800c84c8.offset(sp24 * 0x2L).setu(i);

          sp24++;
        }
      }

      //LAB_800eb8dc
    }

    //LAB_800eb8f4
    _800c86cc.setu(sp24);
  }

  @Method(0x800eb914L)
  public static void FUN_800eb914() {
    _800f6598.setu((_800f0e34.get((int)_800c67a8.get())._12.get() & 0x30L) >>> 4);
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

    final SVECTOR sp0x60 = new SVECTOR();
    final SVECTOR sp0x68 = new SVECTOR();
    final SVECTOR sp0x70 = new SVECTOR();
    final SVECTOR sp0x78 = new SVECTOR();

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
        clearLinearTransforms(sp0x10);
        setRotTransMatrix(sp0x10);
        sp0x60.setX((short)-sp38._58);
        sp0x60.setY((short)-sp38._5a);
        sp0x60.setZ((short)0);
        sp0x68.setX((short)sp38._58);
        sp0x68.setY((short)-sp38._5a);
        sp0x68.setZ((short)0);
        sp0x70.setX((short)-sp38._58);
        sp0x70.setY((short)sp38._5a);
        sp0x70.setZ((short)0);
        sp0x78.setX((short)sp38._58);
        sp0x78.setY((short)sp38._5a);
        sp0x78.setZ((short)0);
        CPU.MTC2(sp0x60.getXY(), 0);
        CPU.MTC2(sp0x60.getZ(),  1);
        CPU.COP2(0x180001L);
        final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(14));
        cmd.pos(0, v0.getX(), v0.getY());
        int z = (int)CPU.MFC2(19) >> 2;

        if(z >= 5 && z < orderingTableSize_1f8003c8.get() - 3) {
          //LAB_800ec534
          CPU.MTC2(sp0x68.getXY(), 0);
          CPU.MTC2(sp0x68.getZ(),  1);
          CPU.COP2(0x180001L);
          final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(14));
          cmd.pos(1, v1.getX(), v1.getY());
          z = (int)CPU.MFC2(19) >> 2;

          if(z >= 5 && z < orderingTableSize_1f8003c8.get() - 3) {
            //LAB_800ec5b8
            if(v1.getX() - v0.getX() <= 0x400) {
              //LAB_800ec5ec
              CPU.MTC2(sp0x70.getXY(), 0);
              CPU.MTC2(sp0x70.getZ(),  1);
              CPU.COP2(0x180001L);
              final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));
              cmd.pos(2, v2.getX(), v2.getY());
              z = (int)CPU.MFC2(19) >> 2;

              if(z >= 5 && z < orderingTableSize_1f8003c8.get() - 3) {
                //LAB_800ec670
                if(v2.getY() - v0.getY() <= 0x200) {
                  //LAB_800ec6a4
                  if(v2.getY() > 0) {
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
                    CPU.MTC2(sp0x78.getXY(), 0);
                    CPU.MTC2(sp0x78.getZ(),  1);
                    CPU.COP2(0x180001L);
                    final DVECTOR v3 = new DVECTOR().setXY(CPU.MFC2(14));
                    cmd.pos(3, v3.getX(), v3.getY());
                    z = (int)CPU.MFC2(19) >> 2;

                    if(z >= 5 && orderingTableSize_1f8003c8.get() - 3 < z) {
                      //LAB_800ec83c
                      if(v3.getX() - v2.getX() <= 0x400) {
                        //LAB_800ec870
                        if(v3.getY() - v1.getY() <= 0x200) {
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
  public static void FUN_800ecd10() {
    final MATRIX sp0x10 = new MATRIX();
    final SVECTOR sp0x60 = new SVECTOR().set((short)-2, (short)-2, (short)0);
    final SVECTOR sp0x68 = new SVECTOR().set((short) 2, (short)-2, (short)0);
    final SVECTOR sp0x70 = new SVECTOR().set((short)-2, (short) 2, (short)0);
    final SVECTOR sp0x78 = new SVECTOR().set((short) 2, (short) 2, (short)0);
    final SVECTOR sp0x80 = new SVECTOR();

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
        clearLinearTransforms(sp0x10);
        setRotTransMatrix(sp0x10);
        CPU.MTC2(sp0x60.getXY(), 0);
        CPU.MTC2(sp0x60.getZ(),  1);
        CPU.COP2(0x180001L);
        final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(14));

        int z = (int)CPU.MFC2(19) >> 2;

        if(z >= 5 && z < orderingTableSize_1f8003c8.get() - 3) {
          //LAB_800ed37c
          CPU.MTC2(sp0x68.getXY(), 0);
          CPU.MTC2(sp0x68.getZ(),  1);
          CPU.COP2(0x180001L);
          final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(14));

          z = (int)CPU.MFC2(19) >> 2;

          if(z >= 5 && z < orderingTableSize_1f8003c8.get() - 3) {
            //LAB_800ed400
            if(v1.getX() - v0.getX() <= 0x400) {
              //LAB_800ed434
              CPU.MTC2(sp0x70.getXY(), 0);
              CPU.MTC2(sp0x70.getZ(),  1);
              CPU.COP2(0x180001L);
              final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));

              z = (int)CPU.MFC2(19) >> 2;

              if(z >= 5 && z < orderingTableSize_1f8003c8.get() - 3) {
                //LAB_800ed4b8
                if(v2.getY() - v0.getY() <= 0x200) {
                  //LAB_800ed4ec
                  CPU.MTC2(sp0x78.getXY(), 0);
                  CPU.MTC2(sp0x78.getZ(),  1);
                  CPU.COP2(0x180001L);
                  final DVECTOR v3 = new DVECTOR().setXY(CPU.MFC2(14));

                  z = (int)CPU.MFC2(19) >> 2;

                  if(z >= 5 && z < orderingTableSize_1f8003c8.get() - 3) {
                    //LAB_800ed570
                    if(v3.getX() - v2.getX() <= 0x400) {
                      //LAB_800ed5a4
                      if(v3.getY() - v1.getY() <= 0x200) {
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
                          .pos(0, v0.getX(), v0.getY())
                          .pos(1, v1.getX(), v1.getY())
                          .pos(2, v2.getX(), v2.getY())
                          .pos(3, v3.getX(), v3.getY())
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
        _800f65a4.get((int)_800f6598.get()).deref().run();
        _800c66a4.setu(0x4L);
        break;

      case 4:
        if(worldMapState_800c6698.get() >= 0x3L || playerState_800c669c.get() >= 0x3L) {
          //LAB_800eda98
          _800c66a4.setu(0x5L);
        }

        //LAB_800edaa4
        break;

      case 5:
        _800f659c.setu(_800f6598);
        _800f6598.setu((_800f0e34.get((int)_800c67a8.get())._12.get() & 0x30L) >>> 4);

        if(_800f6598.get() != _800f659c.get()) {
          _800f65bc.get((int)_800f659c.get()).deref().run();
          _800c66a4.setu(0x3L);
        } else {
          //LAB_800edb5c
          _800f65b0.get((int)_800f6598.get()).deref().run();
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
    long a0;
    long sp1c;

    final SVECTOR vert0 = new SVECTOR();
    final SVECTOR vert1 = new SVECTOR();
    final SVECTOR vert2 = new SVECTOR();
    final SVECTOR vert3 = new SVECTOR();
    final SVECTOR rotation = new SVECTOR(); // Just (0, 0, 0)
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
    if((int)worldMapState_800c6698.get() < 0x4L) {
      return;
    }

    //LAB_800edc64
    if((int)playerState_800c669c.get() < 0x4L) {
      return;
    }

    //LAB_800edc84
    //LAB_800edca8
    for(int i = 0; i < _800c86cc.get(); i++) {
      final Coord2AndThenSomeStruct_60 struct = _800c86f8[i];

      //LAB_800edccc
      if(!places_800f0234.get(_800f0e34.get((int)_800c84c8.offset(i * 0x2L).getSigned()).placeIndex_02.get()).name_00.isNull()) {
        //LAB_800edd3c
        final long sp18 = _800f0e34.get((int)_800c84c8.offset(i * 0x2L).getSigned())._12.get() & 0xcL;

        if(sp18 != 0) {
          //LAB_800edda0
          if(_800f0e34.get((int)_800c84c8.offset(i * 0x2L).getSigned())._0e.get() == _800c6798.get() + 1) {
            //LAB_800eddfc
            if(i >= 9) {
              break;
            }

            //LAB_800ede18
            //LAB_800ede1c
            for(int sp14 = 0; sp14 < 6; sp14++) {
              //LAB_800ede38
              if(sp18 == 0x8L) {
                sp1c = struct._50 / 5;
              } else {
                //LAB_800ede88
                sp1c = struct._50 / 3;
              }

              //LAB_800edebc
              vert0.setX((short)-sp1c);
              vert0.setY((short)-sp1c);
              vert0.setZ((short)0);
              vert1.setX((short)sp1c);
              vert1.setY((short)-sp1c);
              vert1.setZ((short)0);
              vert2.setX((short)-sp1c);
              vert2.setY((short)sp1c);
              vert2.setZ((short)0);
              vert3.setX((short)sp1c);
              vert3.setY((short)sp1c);
              vert3.setZ((short)0);

              //LAB_800edf88
              struct.coord2_00.coord.transfer.setX((int)(_800c74b8.offset(i * 0x10L).get() + struct.svec_54.getX() * struct._50 / 0x10));
              struct.coord2_00.coord.transfer.setY((int)(_800c74bc.offset(i * 0x10L).get() + struct.svec_54.getY() * struct._50 / 0x4L));
              struct.coord2_00.coord.transfer.setZ((int)(_800c74c0.offset(i * 0x10L).get() + struct.svec_54.getZ() * struct._50 / 0x10L));

              if(_800c6798.get() == 0) {
                if(sp18 == 0x4L) {
                  //LAB_800ee0e4
                  struct.coord2_00.coord.transfer.setX((int)(_800c74b8.offset(i * 0x10L).get() + struct.svec_54.getX() * struct._50 / 0x10L));
                  struct.coord2_00.coord.transfer.setY((int)(_800c74bc.offset(i * 0x10L).get() + struct.svec_54.getY() * struct._50 / 0x4L));
                  struct.coord2_00.coord.transfer.setZ((int)(_800c74c0.offset(i * 0x10L).get() + struct.svec_54.getZ() * struct._50 / 0x10L + 0x50L));
                  //LAB_800ee1dc
                } else if(sp18 == 0x8L) {
                  //LAB_800ee238
                  struct.coord2_00.coord.transfer.setX((int)(_800c74b8.offset(i * 0x10L).get() + struct.svec_54.getX() * struct._50 / 0x10L + 0x30L));
                  struct.coord2_00.coord.transfer.setY((int)(_800c74bc.offset(i * 0x10L).get() + struct.svec_54.getY() * struct._50 / 0x4L));
                  struct.coord2_00.coord.transfer.setZ((int)(_800c74c0.offset(i * 0x10L).get() + struct.svec_54.getZ() * struct._50 / 0x10L + 0x30L));
                }

                //LAB_800ee32c
                //LAB_800ee334
              } else if(_800c6798.get() == 0x1L) {
                if(sp18 == 0x4L) {
                  //LAB_800ee3a4
                  struct.coord2_00.coord.transfer.setX((int)(_800c74b8.offset(i * 0x10L).get() + struct.svec_54.getX() * struct._50 / 0x10L));
                  struct.coord2_00.coord.transfer.setY((int)(_800c74bc.offset(i * 0x10L).get() + struct.svec_54.getY() * struct._50 / 0x4L + 0x30L));
                  struct.coord2_00.coord.transfer.setZ((int)(_800c74c0.offset(i * 0x10L).get() + struct.svec_54.getZ() * struct._50 / 0x10L - 0x64L));
                  //LAB_800ee4a0
                } else if(sp18 == 0x8L) {
                  //LAB_800ee4fc
                  a0 = struct.svec_54.getX() * struct._50 / 0x10L;
                  struct.coord2_00.coord.transfer.setX((int)(_800c74b8.offset(i * 0x10L).get() + a0 - 0x30L));
                  a0 = struct.svec_54.getY() * struct._50 / 0x4L;
                  struct.coord2_00.coord.transfer.setY((int)(_800c74bc.offset(i * 0x10L).get() + a0));
                  a0 = struct.svec_54.getZ() * struct._50 / 0x10L;
                  struct.coord2_00.coord.transfer.setZ((int)(_800c74c0.offset(i * 0x10L).get() + a0 + 0x20L));
                }
              }

              //LAB_800ee5f0
              rotateCoord2(rotation, struct.coord2_00);
              GsGetLs(struct.coord2_00, ls);
              clearLinearTransforms(ls);
              setRotTransMatrix(ls);

              final GpuCommandPoly cmd = new GpuCommandPoly(4)
                .bpp(Bpp.BITS_4)
                .vramPos(640, 256);

              CPU.MTC2(vert0.getXY(), 0);
              CPU.MTC2(vert0.getZ(), 1);
              CPU.COP2(0x18_0001L); // Perspective transform single
              final DVECTOR v0 = new DVECTOR().setXY(CPU.MFC2(14));
              cmd.pos(0, v0.getX(), v0.getY());
              int z = (int)CPU.MFC2(19) >> 2;

              //LAB_800ee6cc
              if(z >= 5 || z < orderingTableSize_1f8003c8.get() - 3) {
                //LAB_800ee6d4
                CPU.MTC2(vert1.getXY(), 0);
                CPU.MTC2(vert1.getZ(), 1);
                CPU.COP2(0x18_0001L); // Perspective transform single
                final DVECTOR v1 = new DVECTOR().setXY(CPU.MFC2(14));
                cmd.pos(1, v1.getX(), v1.getY());
                z = (int)CPU.MFC2(19) >> 2;

                //LAB_800ee750
                if(z >= 5 || z < orderingTableSize_1f8003c8.get() - 3) {
                  //LAB_800ee758
                  if(v1.getX() - v0.getX() <= 0x400) {
                    //LAB_800ee78c
                    CPU.MTC2(vert2.getXY(), 0);
                    CPU.MTC2(vert2.getZ(), 1);
                    CPU.COP2(0x18_0001L); // Perspective transform single
                    final DVECTOR v2 = new DVECTOR().setXY(CPU.MFC2(14));
                    cmd.pos(2, v2.getX(), v2.getY());
                    z = (int)CPU.MFC2(19) >> 2;

                    //LAB_800ee808
                    if(z >= 5 && z < orderingTableSize_1f8003c8.get() - 3) {
                      //LAB_800ee810
                      if(v2.getY() - v0.getY() <= 0x200) {
                        //LAB_800ee844
                        CPU.MTC2(vert3.getXY(), 0);
                        CPU.MTC2(vert3.getZ(), 1);
                        CPU.COP2(0x18_0001L); // Perspective transform single
                        final DVECTOR v3 = new DVECTOR().setXY(CPU.MFC2(14));
                        cmd.pos(3, v3.getX(), v3.getY());
                        z = (int)CPU.MFC2(19) >> 2;

                        //LAB_800ee8c0
                        if(z >= 5 && z < orderingTableSize_1f8003c8.get() - 3) {
                          //LAB_800ee8c8
                          if(v3.getX() - v2.getX() <= 0x400) {
                            //LAB_800ee8fc
                            if(v3.getY() - v1.getY() <= 0x200) {
                              //LAB_800ee930
                              if(z >= 6 && z < orderingTableSize_1f8003c8.get() - 1) {
                                if(sp18 == 0x8L) {
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
    _800f65bc.get((int)_800f6598.get()).deref().run();
  }
}
