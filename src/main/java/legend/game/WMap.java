package legend.game;

import legend.core.DebugHelper;
import legend.core.gpu.RECT;
import legend.core.gpu.TimHeader;
import legend.core.gte.COLOUR;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.TmdObjTable;
import legend.core.gte.TmdWithId;
import legend.core.gte.VECTOR;
import legend.core.memory.Memory;
import legend.core.memory.Method;
import legend.core.memory.Ref;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.ConsumerRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.RunnableRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.game.types.BigStruct;
import legend.game.types.Coord2AndThenSomeStruct_60;
import legend.game.types.GsF_LIGHT;
import legend.game.types.GsOT_TAG;
import legend.game.types.LodString;
import legend.game.types.McqHeader;
import legend.game.types.TmdAnimationFile;
import legend.game.types.WMapRender08_2;
import legend.game.types.WMapRender10;
import legend.game.types.WMapRender28;
import legend.game.types.WMapRender40;
import legend.game.types.WMapStruct0c;
import legend.game.types.WMapStruct14;
import legend.game.types.WMapStruct19c0;
import legend.game.types.WMapStruct258;
import legend.game.types.WMapTmdRenderingStruct18;
import legend.game.types.WeirdTimHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

import static legend.core.Hardware.CPU;
import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.Scus94491BpeSegment.FUN_800127cc;
import static legend.game.Scus94491BpeSegment.FUN_800133ac;
import static legend.game.Scus94491BpeSegment.FUN_80013434;
import static legend.game.Scus94491BpeSegment.FUN_80019c80;
import static legend.game.Scus94491BpeSegment.FUN_8001eea8;
import static legend.game.Scus94491BpeSegment.FUN_8001f708;
import static legend.game.Scus94491BpeSegment._1f8003c4;
import static legend.game.Scus94491BpeSegment._1f8003c8;
import static legend.game.Scus94491BpeSegment._1f8003cc;
import static legend.game.Scus94491BpeSegment._1f8003e8;
import static legend.game.Scus94491BpeSegment._1f8003ec;
import static legend.game.Scus94491BpeSegment.addToLinkedListTail;
import static legend.game.Scus94491BpeSegment.fillMemory;
import static legend.game.Scus94491BpeSegment.getLoadedDrgnFiles;
import static legend.game.Scus94491BpeSegment.insertElementIntoLinkedList;
import static legend.game.Scus94491BpeSegment.linkedListAddress_1f8003d8;
import static legend.game.Scus94491BpeSegment.loadDrgnBinFile;
import static legend.game.Scus94491BpeSegment.memcpy;
import static legend.game.Scus94491BpeSegment.playSound;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.removeFromLinkedList;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment.setWidthAndFlags;
import static legend.game.Scus94491BpeSegment.tags_1f8003d0;
import static legend.game.Scus94491BpeSegment.unloadSoundFile;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020a00;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020b98;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020fe0;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021048;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021050;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021058;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021060;
import static legend.game.Scus94491BpeSegment_8002.FUN_800211d8;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021258;
import static legend.game.Scus94491BpeSegment_8002.FUN_800214bc;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021584;
import static legend.game.Scus94491BpeSegment_8002.FUN_800257e0;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a32c;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a3ec;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a488;
import static legend.game.Scus94491BpeSegment_8002.SquareRoot0;
import static legend.game.Scus94491BpeSegment_8002.rand;
import static legend.game.Scus94491BpeSegment_8002.renderText;
import static legend.game.Scus94491BpeSegment_8002.strcmp;
import static legend.game.Scus94491BpeSegment_8003.DrawSync;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b8f0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b900;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003dfc0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003f900;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003f930;
import static legend.game.Scus94491BpeSegment_8003.GetTPage;
import static legend.game.Scus94491BpeSegment_8003.GsGetLs;
import static legend.game.Scus94491BpeSegment_8003.GsGetLws;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.GsSetAmbient;
import static legend.game.Scus94491BpeSegment_8003.GsSetFlatLight;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_8003faf0;
import static legend.game.Scus94491BpeSegment_8003.StoreImage;
import static legend.game.Scus94491BpeSegment_8003.adjustTmdPointers;
import static legend.game.Scus94491BpeSegment_8003.gpuLinkedListSetCommandTextureUnshaded;
import static legend.game.Scus94491BpeSegment_8003.gpuLinkedListSetCommandTransparency;
import static legend.game.Scus94491BpeSegment_8003.parseTimHeader;
import static legend.game.Scus94491BpeSegment_8003.setLightMode;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.setRotTransMatrix;
import static legend.game.Scus94491BpeSegment_8003.updateTmdPacketIlen;
import static legend.game.Scus94491BpeSegment_8004._8004dd24;
import static legend.game.Scus94491BpeSegment_8004._8004dd28;
import static legend.game.Scus94491BpeSegment_8004.fileCount_8004ddc8;
import static legend.game.Scus94491BpeSegment_8004.ratan2;
import static legend.game.Scus94491BpeSegment_8005._80052c34;
import static legend.game.Scus94491BpeSegment_8005._80052c6c;
import static legend.game.Scus94491BpeSegment_8005.index_80052c38;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8007.joypadInput_8007a39c;
import static legend.game.Scus94491BpeSegment_8007.joypadPress_8007a398;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800bac60;
import static legend.game.Scus94491BpeSegment_800b._800bad24;
import static legend.game.Scus94491BpeSegment_800b._800bad44;
import static legend.game.Scus94491BpeSegment_800b._800bb080;
import static legend.game.Scus94491BpeSegment_800b._800bb0a0;
import static legend.game.Scus94491BpeSegment_800b._800bb0a2;
import static legend.game.Scus94491BpeSegment_800b._800bb0a4;
import static legend.game.Scus94491BpeSegment_800b._800bb0a5;
import static legend.game.Scus94491BpeSegment_800b._800bb0a6;
import static legend.game.Scus94491BpeSegment_800b._800bb0fc;
import static legend.game.Scus94491BpeSegment_800b._800bb112;
import static legend.game.Scus94491BpeSegment_800b._800bb114;
import static legend.game.Scus94491BpeSegment_800b._800bb116;
import static legend.game.Scus94491BpeSegment_800b._800bb118;
import static legend.game.Scus94491BpeSegment_800b._800bb11a;
import static legend.game.Scus94491BpeSegment_800b._800bb120;
import static legend.game.Scus94491BpeSegment_800b._800bdc34;
import static legend.game.Scus94491BpeSegment_800b._800bdf00;
import static legend.game.Scus94491BpeSegment_800b._800be358;
import static legend.game.Scus94491BpeSegment_800b._800bed60;
import static legend.game.Scus94491BpeSegment_800b._800bee90;
import static legend.game.Scus94491BpeSegment_800b._800beebc;
import static legend.game.Scus94491BpeSegment_800b._800bf0b0;
import static legend.game.Scus94491BpeSegment_800b.doubleBufferFrame_800bb108;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.scriptFlags1_800bad04;
import static legend.game.Scus94491BpeSegment_800b.scriptFlags2_800bac84;
import static legend.game.Scus94491BpeSegment_800b.submapScene_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.submapStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.Scus94491BpeSegment_800c.identityMatrix_800c3568;

public class WMap {
  private static final Logger LOGGER = LogManager.getFormatterLogger(WMap.class);

  private static final Value _800c6690 = MEMORY.ref(4, 0x800c6690L);

  private static final Value _800c6698 = MEMORY.ref(4, 0x800c6698L);
  private static final Value _800c669c = MEMORY.ref(4, 0x800c669cL);
  private static final Value _800c66a0 = MEMORY.ref(4, 0x800c66a0L);
  private static final Value _800c66a4 = MEMORY.ref(4, 0x800c66a4L);
  private static final Pointer<WMapStruct258> struct258_800c66a8 = MEMORY.ref(4, 0x800c66a8L, Pointer.deferred(4, WMapStruct258::new));

  private static final Pointer<WMapStruct19c0> _800c66b0 = MEMORY.ref(4, 0x800c66b0L, Pointer.deferred(4, WMapStruct19c0::new));

  private static final Value _800c66b8 = MEMORY.ref(4, 0x800c66b8L);

  private static final WeirdTimHeader _800c66c0 = MEMORY.ref(4, 0x800c66c0L, WeirdTimHeader::new);

  private static final Value _800c66d8 = MEMORY.ref(4, 0x800c66d8L);
  private static final Value _800c66dc = MEMORY.ref(2, 0x800c66dcL);

  private static final McqHeader mcqHeader_800c6768 = MEMORY.ref(4, 0x800c6768L, McqHeader::new);

  private static final Value _800c6794 = MEMORY.ref(2, 0x800c6794L);

  private static final Value _800c6798 = MEMORY.ref(4, 0x800c6798L);
  private static final Value _800c679c = MEMORY.ref(4, 0x800c679cL);
  private static final Value _800c67a0 = MEMORY.ref(4, 0x800c67a0L);
  private static final Value _800c67a4 = MEMORY.ref(4, 0x800c67a4L);
  private static final Value _800c67a8 = MEMORY.ref(2, 0x800c67a8L);
  private static final Value _800c67aa = MEMORY.ref(2, 0x800c67aaL);
  /** The section of the path that the player is on */
  private static final Value pathIndex_800c67ac = MEMORY.ref(2, 0x800c67acL);
  /** The path dot the player is on */
  private static final Value dotIndex_800c67ae = MEMORY.ref(2, 0x800c67aeL);
  /** The distance the player is from the dot (range: 0-3) */
  private static final Value dotOffset_800c67b0 = MEMORY.ref(2, 0x800c67b0L);

  /** +1 - left, -1 - right */
  private static final Value facing_800c67b4 = MEMORY.ref(4, 0x800c67b4L);
  private static final VECTOR playerPos_800c67b8 = MEMORY.ref(4, 0x800c67b8L, VECTOR::new);
  private static final VECTOR nextDotPos_800c67c8 = MEMORY.ref(4, 0x800c67c8L, VECTOR::new);

  private static final ArrayRef<VECTOR> _800c67d8 = MEMORY.ref(4, 0x800c67d8L, ArrayRef.of(VECTOR.class, 7, 0x10, VECTOR::new));
  private static final VECTOR _800c6848 = MEMORY.ref(4, 0x800c6848L, VECTOR::new);
  private static final Value _800c6858 = MEMORY.ref(2, 0x800c6858L);
  private static final Value previousPlayerRotation_800c685a = MEMORY.ref(2, 0x800c685aL);
  private static final Value _800c685c = MEMORY.ref(2, 0x800c685cL);
  private static final Value _800c685e = MEMORY.ref(2, 0x800c685eL);
  private static final Value _800c6860 = MEMORY.ref(2, 0x800c6860L);
  private static final Value _800c6862 = MEMORY.ref(2, 0x800c6862L);

  private static final Value _800c6868 = MEMORY.ref(4, 0x800c6868L);
  private static final Value _800c686c = MEMORY.ref(4, 0x800c686cL);
  private static final Value _800c6870 = MEMORY.ref(4, 0x800c6870L);
  private static final Value _800c6874 = MEMORY.ref(4, 0x800c6874L);

  private static final Value _800c6890 = MEMORY.ref(4, 0x800c6890L);
  private static final Value _800c6894 = MEMORY.ref(4, 0x800c6894L);
  private static final Pointer<WMapRender40> _800c6898 = MEMORY.ref(4, 0x800c6898L, Pointer.deferred(4, WMapRender40::new));
  private static final Pointer<WMapRender40> _800c689c = MEMORY.ref(4, 0x800c689cL, Pointer.deferred(4, WMapRender40::new));
  private static final Value _800c68a0 = MEMORY.ref(4, 0x800c68a0L);
  private static final Value _800c68a4 = MEMORY.ref(4, 0x800c68a4L);
  private static final Value _800c68a8 = MEMORY.ref(4, 0x800c68a8L);

  /** TODO array of 0xc-byte things */
  private static final Value _800c68ac = MEMORY.ref(4, 0x800c68acL);
  private static final Value _800c68b0 = MEMORY.ref(4, 0x800c68b0L);
  private static final Value _800c68b4 = MEMORY.ref(4, 0x800c68b4L);

  private static final Value _800c6ae8 = MEMORY.ref(4, 0x800c6ae8L);

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

  private static final Pointer<ArrayRef<Coord2AndThenSomeStruct_60>> _800c86f8 = MEMORY.ref(4, 0x800c86f8L, Pointer.deferred(4, ArrayRef.of(Coord2AndThenSomeStruct_60.class, 0x30, 0x60, Coord2AndThenSomeStruct_60::new)));
  private static final Value _800c86fc = MEMORY.ref(4, 0x800c86fcL);
  private static final RECT _800c8700 = MEMORY.ref(4, 0x800c8700L, RECT::new);

  private static final Value _800c8778 = MEMORY.ref(4, 0x800c8778L);
  private static final Value _800c877c = MEMORY.ref(4, 0x800c877cL);

  private static final Value _800c87e8 = MEMORY.ref(4, 0x800c87e8L);
  private static final Value _800c87ec = MEMORY.ref(4, 0x800c87ecL);

  private static final Value _800c87f4 = MEMORY.ref(4, 0x800c87f4L);
  private static final Value _800c87f8 = MEMORY.ref(4, 0x800c87f8L);

  private static final Value _800eee48 = MEMORY.ref(4, 0x800eee48L);

  private static final ArrayRef<Pointer<RunnableRef>> _800ef000 = MEMORY.ref(4, 0x800ef000L, ArrayRef.of(Pointer.classFor(RunnableRef.class), 13, 4, Pointer.deferred(4, RunnableRef::new)));
  private static final ArrayRef<Pointer<ConsumerRef<Long>>> gpuPacketSetters_800ef034 = MEMORY.ref(4, 0x800ef034L, ArrayRef.of(Pointer.classFor(ConsumerRef.classFor(Long.class)), 17, 4, Pointer.deferred(4, ConsumerRef::new)));

  private static final Value _800ef0d4 = MEMORY.ref(2, 0x800ef0d4L);
  private static final Value _800ef0d6 = MEMORY.ref(2, 0x800ef0d6L);
  private static final Value _800ef0d8 = MEMORY.ref(2, 0x800ef0d8L);
  private static final Value _800ef0da = MEMORY.ref(2, 0x800ef0daL);

  private static final Value _800ef104 = MEMORY.ref(1, 0x800ef104L);

  private static final Value _800ef194 = MEMORY.ref(1, 0x800ef194L);

  private static final Value _800ef19c = MEMORY.ref(1, 0x800ef19cL);

  private static final Value _800ef1a4 = MEMORY.ref(2, 0x800ef1a4L);

  private static final Value _800ef364 = MEMORY.ref(2, 0x800ef364L);
  private static final Value _800ef366 = MEMORY.ref(2, 0x800ef366L);
  private static final Value _800ef368 = MEMORY.ref(2, 0x800ef368L);
  private static final Value _800ef36a = MEMORY.ref(2, 0x800ef36aL);

  private static final Value _800ef684 = MEMORY.ref(4, 0x800ef684L);

  private static final Value _800ef694 = MEMORY.ref(1, 0x800ef694L);

  private static final LodString _800effa4 = MEMORY.ref(4, 0x800effa4L, LodString::new);
  private static final LodString _800effb0 = MEMORY.ref(4, 0x800effb0L, LodString::new);
  private static final LodString _800f00e8 = MEMORY.ref(4, 0x800f00e8L, LodString::new);

  private static final ArrayRef<Pointer<LodString>> _800f01cc = MEMORY.ref(4, 0x800f01ccL, ArrayRef.of(Pointer.classFor(LodString.class), 3, 4, Pointer.deferred(4, LodString::new)));

  private static final Pointer<LodString> _800f01e0 = MEMORY.ref(4, 0x800f01e0L, Pointer.deferred(4, LodString::new));
  private static final Pointer<LodString> _800f01e4 = MEMORY.ref(4, 0x800f01e4L, Pointer.deferred(4, LodString::new));
  private static final Pointer<LodString> _800f01e8 = MEMORY.ref(4, 0x800f01e8L, Pointer.deferred(4, LodString::new));
  private static final ArrayRef<Pointer<LodString>> _800f01ec = MEMORY.ref(4, 0x800f01ecL, ArrayRef.of(Pointer.classFor(LodString.class), 3, 4, Pointer.deferred(4, LodString::new)));

  private static final Value _800f01fc = MEMORY.ref(4, 0x800f01fcL);

  private static final Value _800f0204 = MEMORY.ref(1, 0x800f0204L);

  private static final Value _800f0210 = MEMORY.ref(1, 0x800f0210L);

  private static final Value _800f021c = MEMORY.ref(2, 0x800f021cL);

  private static final UnboundedArrayRef<WMapStruct0c> _800f0234 = MEMORY.ref(4, 0x800f0234L, UnboundedArrayRef.of(0xc, WMapStruct0c::new));

  private static final UnboundedArrayRef<WMapStruct14> _800f0e34 = MEMORY.ref(2, 0x800f0e34L, UnboundedArrayRef.of(0x14, WMapStruct14::new));

  private static final Value _800f1580 = MEMORY.ref(2, 0x800f1580L);
  private static final Value _800f1582 = MEMORY.ref(2, 0x800f1582L);

  /** TODO array of 0x8-byte struct */
  private static final Value _800f2248 = MEMORY.ref(2, 0x800f2248L);

  private static final Value _800f224b = MEMORY.ref(1, 0x800f224bL);
  private static final Value _800f224c = MEMORY.ref(1, 0x800f224cL);
  private static final Value _800f224d = MEMORY.ref(1, 0x800f224dL);
  private static final Value _800f224e = MEMORY.ref(1, 0x800f224eL);

  private static final Value _800f5810 = MEMORY.ref(4, 0x800f5810L);

  private static final Value pathPosPtrArr_800f591c = MEMORY.ref(4, 0x800f591cL);

  /** TODO array of 0x2c-byte struct */
  private static final Value _800f5a6c = MEMORY.ref(2, 0x800f5a6cL);

  private static final Value _800f5a70 = MEMORY.ref(4, 0x800f5a70L);

  private static final Value _800f6598 = MEMORY.ref(4, 0x800f6598L);
  private static final Value _800f659c = MEMORY.ref(4, 0x800f659cL);
  private static final Value _800f65a0 = MEMORY.ref(4, 0x800f65a0L);
  private static final ArrayRef<Pointer<RunnableRef>> _800f65a4 = MEMORY.ref(4, 0x800f65a4L, ArrayRef.of(Pointer.classFor(RunnableRef.class), 3, 4, Pointer.deferred(4, RunnableRef::new)));
  private static final ArrayRef<Pointer<RunnableRef>> _800f65b0 = MEMORY.ref(4, 0x800f65b0L, ArrayRef.of(Pointer.classFor(RunnableRef.class), 3, 4, Pointer.deferred(4, RunnableRef::new)));
  private static final ArrayRef<Pointer<RunnableRef>> _800f65bc = MEMORY.ref(4, 0x800f65bcL, ArrayRef.of(Pointer.classFor(RunnableRef.class), 3, 4, Pointer.deferred(4, RunnableRef::new)));

  private static final Value _800f65d4 = MEMORY.ref(1, 0x800f65d4L);

  @Method(0x800c8844L)
  public static void FUN_800c8844(final GsDOBJ2 dobj2, final long a1) {
    long primitiveCount = dobj2.tmd_08.deref().n_primitive_14.get();
    long primitives = dobj2.tmd_08.deref().primitives_10.getPointer();
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

  @Method(0x800c8af4L)
  public static void renderWmapDobj2(final GsDOBJ2 dobj2) {
    final TmdObjTable objTable = dobj2.tmd_08.deref();
    long vertices = objTable.vert_top_00.get();
    long normals = objTable.normal_top_08.get();
    long primitives = objTable.primitives_10.getPointer();
    long count = objTable.n_primitive_14.get();

    //LAB_800c8b28
    while(count != 0) {
      final long primitiveCount = MEMORY.ref(2, primitives).get();
      final long cmd = MEMORY.ref(4, primitives).get() & 0xff04_0000L;

      count -= primitiveCount;

      if(count < 0) {
        LOGGER.warn("renderDobj2 count less than 0! %d", count);
      }

      //LAB_800c8b8c
      if(cmd == 0x3000_0000L) {
        // 3-vert poly, light calc, gourad, no tex (solid)
        // 0x14 bytes long
        //LAB_800c8c54
        primitives = renderPrimitive30(primitives, vertices, normals, primitiveCount);
//        primitives += primitiveCount * 0x14L;
      } else if(cmd == 0x3004_0000L) {
        // 3-vert poly, light calc, gourad, no tex (gradation)
        // 0x1c bytes long
        //LAB_800c8c74
        primitives = renderPrimitive3004(primitives, vertices, normals, primitiveCount);
//        primitives += primitiveCount * 0x1cL;
      } else if(cmd == 0x3200_0000L) {
        // ?
        //LAB_800c8c64
        primitives = renderPrimitive3200(primitives, vertices, normals);
      } else if(cmd == 0x3204_0000L) {
        // ?
        //LAB_800c8c88
        primitives = renderPrimitive3204(primitives, vertices, normals);
      } else if(cmd == 0x3400_0000L) {
        // 3-vert poly, light calc, gourad, tex
        // 0x1c bytes long
        //LAB_800c8c9c
        primitives = renderPrimitive34(primitives, vertices, normals, primitiveCount);
//        primitives += primitiveCount * 0x1cL;
        //LAB_800c8b9c
      } else if(cmd == 0x3500_0000L) {
        // 3-vert poly, no light calc, tex, no gradation
        // 0x24 bytes long
        //LAB_800c8cbc
        primitives = renderPrimitive35(primitives, vertices, primitiveCount);
//        primitives += primitiveCount * 0x24L;
        //LAB_800c8bc0
      } else if(cmd == 0x3600_0000L) {
        // ?
        //LAB_800c8cac
        primitives = renderPrimitive36(primitives, vertices, normals);
      } else if(cmd == 0x3700_0000L) {
        // ?
        //LAB_800c8cd0
        primitives = renderPrimitive37(primitives, vertices, primitiveCount);
      } else if(cmd == 0x3800_0000L) {
        // 4-vert poly, light calc, gourad, no tex (solid)
        // 0x18 bytes long
        //LAB_800c8ce4
        primitives = renderPrimitive38(primitives, vertices, normals, primitiveCount);
//        primitives += primitiveCount * 0x18L;
      } else if(cmd == 0x3804_0000L) {
        // 4-vert poly, light calc, gourad, no tex, gradation
        // 0x24 bytes long
        //LAB_800c8d04
        primitives = renderPrimitive3804(primitives, vertices, normals, primitiveCount);
//        primitives += primitiveCount * 0x24L;
        //LAB_800c8c08
      } else if(cmd == 0x3a00_0000L) {
        // ?
        //LAB_800c8cf4
        primitives = renderPrimitive3a(primitives, vertices, normals);
        //LAB_800c8bd0
      } else if(cmd == 0x3a04_0000L) {
        // ?
        //LAB_800c8d18
        primitives = renderPrimitive3a04(primitives, vertices, normals);
      } else if(cmd == 0x3c00_0000L) {
        // 4-vert poly, light calc, gourad, tex
        // 0x24 bytes long
        //LAB_800c8d2c
        primitives = renderPrimitive3c(primitives, vertices, normals, primitiveCount);
//        primitives += primitiveCount * 0x24L;
        //LAB_800c8c18
      } else if(cmd == 0x3d00_0000L) {
        // 4-vert poly, no light calc, gourad, tex
        // 0x2c bytes long
        //LAB_800c8d50
        primitives = renderPrimitive3d(primitives, vertices, primitiveCount);
//        primitives += primitiveCount * 0x2cL;
        //LAB_800c8c3c
      } else if(cmd == 0x3e00_0000L) {
        // 4-vert poly, light calc, tex, transparent, gradation
        // 0x24 bytes long
        //LAB_800c8d3c
        primitives = renderPrimitive3e(primitives, vertices, normals);
      } else if(cmd == 0x3f00_0000L) {
        // 4-vert poly, light calc, gourad, tex, transparent, gradation
        // 0x2c bytes long
        //LAB_800c8d64
        primitives = renderPrimitive3f(primitives, vertices, primitiveCount);
//        primitives += primitiveCount * 0x2cL;
      }

      //LAB_800c8d70
    }

    //LAB_800c8d78
  }

  @Method(0x800c8d90L)
  public static void FUN_800c8d90(BigStruct a0) {
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
  public static void FUN_800c925c(final BigStruct a0) {
    long s0 = 0x1L;
    long s6 = a0.ui_f4.get();
    long nobj = a0.ObjTable_0c.nobj.get();
    long fp = a0.ui_f8.get();

    _1f8003e8.setu((short)a0.us_a0.get());
    _1f8003ec.setu(a0.ui_108.get());

    //LAB_800c92c8
    for(int i = 0; i < nobj; i++) {
      final GsDOBJ2 dobj2 = a0.ObjTable_0c.top.deref().get(i);

      if((s0 & s6) == 0) {
        final MATRIX ls = new MATRIX();
        final MATRIX lw = new MATRIX();
        GsGetLws(dobj2.coord2_04.deref(), lw, ls);
        GsSetLightMatrix(lw);
        CPU.CTC2((ls.get(1) & 0xffffL) << 16 | ls.get(0) & 0xffffL, 0);
        CPU.CTC2((ls.get(3) & 0xffffL) << 16 | ls.get(2) & 0xffffL, 1);
        CPU.CTC2((ls.get(5) & 0xffffL) << 16 | ls.get(4) & 0xffffL, 2);
        CPU.CTC2((ls.get(7) & 0xffffL) << 16 | ls.get(6) & 0xffffL, 3);
        CPU.CTC2(                              ls.get(8) & 0xffffL, 4);
        CPU.CTC2(ls.transfer.getX(), 5);
        CPU.CTC2(ls.transfer.getY(), 6);
        CPU.CTC2(ls.transfer.getZ(), 7);
        renderWmapDobj2(dobj2);
      }

      //LAB_800c9330
      s0 = s0 << 1;
      if((int)s0 == 0) {
        s0 = 0x1L;
        s6 = fp;
      }

      //LAB_800c9344
    }

    //LAB_800c9354
    if(a0.ub_cc.get() != 0) {
      FUN_800c8d90(a0);
    }

    //LAB_800c936c
  }

  @Method(0x800c939cL)
  public static long renderPrimitive38(long a0, long a1, long a2, long a3) {
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

    v0 = 0x1f80_0000L;
    t1 = MEMORY.ref(4, v0).offset(0x3d8L).get();
    v0 = 0x1f80_0000L;
    v1 = 0x1f80_0000L;
    s0 = MEMORY.ref(4, v0).offset(0x3e8L).get();
    s8 = MEMORY.ref(4, v1).offset(0x3d0L).get();
    t3 = a0;
    if(a3 != 0) {
      v0 = 0x1f80_0000L;
      s2 = v0 + 0x3c0L;
      v0 = 0x1f80_0000L;
      s1 = v0 + 0x3c8L;
      t9 = 0xff_0000L;
      t9 = t9 | 0xffffL;
      t4 = a0 + 0x14L;
      t0 = t1 + 0x1cL;

      //LAB_800c93ec
      do {
        t5 = MEMORY.ref(2, t3).offset(0xaL).get();
        t6 = MEMORY.ref(2, t3).offset(0xeL).get();
        t7 = MEMORY.ref(2, t3).offset(0x12L).get();
        t5 = t5 << 3;
        t6 = t6 << 3;
        t7 = t7 << 3;
        t5 = a1 + t5;
        t6 = a1 + t6;
        t7 = a1 + t7;
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);
        CPU.COP2(0x28_0030L);

        a3 = a3 - 0x1L;
        t8 = CPU.CFC2(31);

        if((int)t8 >= 0) {
          CPU.COP2(0x140_0006L);
          t8 = CPU.MFC2(24);

          if((int)t8 > 0) {
            MEMORY.ref(4, t1).offset(0x08L).setu(CPU.MFC2(12));
            MEMORY.ref(4, t1).offset(0x10L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t1).offset(0x18L).setu(CPU.MFC2(14));
            v0 = MEMORY.ref(2, t4).offset(0x2L).get();

            v0 = v0 << 3;
            v0 = a1 + v0;
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
            CPU.COP2(0x18_0001L);
            t8 = CPU.CFC2(31);

            v0 = t1 + 0x20L;
            MEMORY.ref(4, v0).setu(CPU.MFC2(14));

            if(MEMORY.ref(2, t0).offset(-0x14L).getSigned() >= -0xc0L || MEMORY.ref(2, t0).offset(-0xcL).getSigned() >= -0xc0L || MEMORY.ref(2, t0).offset(-0x4L).getSigned() >= -0xc0L || MEMORY.ref(2, t0).offset(0x4L).getSigned() >= -0xc0L) {
              //LAB_800c94e8
              if(MEMORY.ref(2, t0).offset(-0x12L).getSigned() >= -0x80L || MEMORY.ref(2, t0).offset(-0xaL).getSigned() >= -0x80L || MEMORY.ref(2, t0).offset(-0x2L).getSigned() >= -0x80L || MEMORY.ref(2, t0).offset(0x6L).getSigned() >= -0x80L) {
                //LAB_800c9538
                if(MEMORY.ref(2, t0).offset(-0x14L).getSigned() < 0xc1L || MEMORY.ref(2, t0).offset(-0xcL).getSigned() < 0xc1L || MEMORY.ref(2, t0).offset(-0x4L).getSigned() < 0xc1L || MEMORY.ref(2, t0).offset(0x4L).getSigned() < 0xc1L) {
                  //LAB_800c9588
                  if(MEMORY.ref(2, t0).offset(-0x12L).getSigned() < 0x81L || MEMORY.ref(2, t0).offset(-0xaL).getSigned() < 0x81L || MEMORY.ref(2, t0).offset(-0x2L).getSigned() < 0x81L || MEMORY.ref(2, t0).offset(0x6L).getSigned() < 0x81L) {
                    //LAB_800c95d8
                    CPU.COP2(0x168_002eL);
                    t2 = CPU.MFC2(7);

                    t2 = t2 + s0;
                    v0 = MEMORY.ref(4, s2).offset(0x4L).get();
                    v1 = MEMORY.ref(4, s1).offset(0x4L).get();
                    t2 = (int)t2 >> v0;
                    if((int)t2 < (int)v1) {
                      a0 = t2 << 2;
                    } else {
                      a0 = t2 << 2;
                      t2 = v1;
                      a0 = t2 << 2;
                    }

                    //LAB_800c9608
                    a0 = s8 + a0;
                    v0 = t3 + 0x4L;
                    CPU.MTC2(MEMORY.ref(4, v0).get(), 6);
                    t5 = MEMORY.ref(2, t3).offset(0x8L).get();
                    t6 = MEMORY.ref(2, t3).offset(0xcL).get();
                    t7 = MEMORY.ref(2, t3).offset(0x10L).get();
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
                    CPU.COP2(0x118_043fL);
                    MEMORY.ref(4, t1).offset(0x04L).setu(CPU.MFC2(20));
                    MEMORY.ref(4, t1).offset(0x0cL).setu(CPU.MFC2(21));
                    MEMORY.ref(4, t1).offset(0x14L).setu(CPU.MFC2(22));

                    v0 = MEMORY.ref(4, a0).offset(0x0L).get();
                    v1 = 0x800_0000L;
                    v0 = v0 & t9;
                    v0 = v0 | v1;
                    MEMORY.ref(4, t1).offset(0x0L).setu(v0);
                    v0 = MEMORY.ref(2, t4).offset(0x0L).get();

                    v0 = v0 << 3;
                    v0 = a2 + v0;
                    CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
                    CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
                    CPU.COP2(0x108_041bL);

                    v0 = t1 & t9;
                    MEMORY.ref(4, a0).offset(0x0L).setu(v0);
                    MEMORY.ref(4, t0).setu(CPU.MFC2(22));
                    t0 = t0 + 0x24L;
                    t1 = t1 + 0x24L;
                  }
                }
              }
            }
          }
        }

        //LAB_800c96b4
        t4 = t4 + 0x18L;
        t3 = t3 + 0x18L;
      } while(a3 != 0);
    }

    //LAB_800c96c0
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t1);
    v0 = t3;
    return v0;
  }

  @Method(0x800c96e0L)
  public static long renderPrimitive30(long a0, long a1, long a2, long a3) {
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
    long t8;
    long t9;
    long s8;

    t3 = a0;
    s0 = a1;
    v0 = 0x1f80_0000L;
    t2 = MEMORY.ref(4, v0).offset(0x3d8L).get();
    v0 = 0x1f80_0000L;
    v1 = 0x1f80_0000L;
    s8 = MEMORY.ref(4, v0).offset(0x3e8L).get();
    t9 = MEMORY.ref(4, v1).offset(0x3d0L).get();
    s1 = a2;
    if(a3 != 0) {
      v0 = 0x1f80_0000L;
      a2 = v0 + 0x3c0L;
      v0 = 0x1f80_0000L;
      a1 = v0 + 0x3c8L;
      t8 = 0xff_0000L;
      t8 = t8 | 0xffffL;
      t0 = t2 + 0x1aL;

      //LAB_800c9730
      do {
        t5 = MEMORY.ref(2, t3).offset(0xaL).get();
        t6 = MEMORY.ref(2, t3).offset(0xeL).get();
        t7 = MEMORY.ref(2, t3).offset(0x12L).get();
        t5 = t5 << 3;
        t6 = t6 << 3;
        t7 = t7 << 3;
        t5 = s0 + t5;
        t6 = s0 + t6;
        t7 = s0 + t7;
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);
        CPU.COP2(0x28_0030L);
        a3 = a3 - 0x1L;
        t4 = CPU.CFC2(31);

        if((int)t4 >= 0) {
          CPU.COP2(0x140_0006L);
          t4 = CPU.MFC2(24);

          if((int)t4 > 0) {
            MEMORY.ref(4, t2).offset(0x08L).setu(CPU.MFC2(12));
            MEMORY.ref(4, t2).offset(0x10L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t2).offset(0x18L).setu(CPU.MFC2(14));

            if(MEMORY.ref(2, t0).offset(-0x12L).getSigned() >= -0xc0L || MEMORY.ref(2, t0).offset(-0xaL).getSigned() >= -0xc0L || MEMORY.ref(2, t0).offset(-0x2L).getSigned() >= -0xc0L) {
              //LAB_800c97e8
              if(MEMORY.ref(2, t0).offset(-0x10L).getSigned() >= -0x80L || MEMORY.ref(2, t0).offset(-0x8L).getSigned() >= -0x80L || MEMORY.ref(2, t0).offset(0x0L).getSigned() >= -0x80L) {
                //LAB_800c9824
                if(MEMORY.ref(2, t0).offset(-0x12L).getSigned() < 0xc1L || MEMORY.ref(2, t0).offset(-0xaL).getSigned() < 0xc1L || MEMORY.ref(2, t0).offset(-0x2L).getSigned() < 0xc1L) {
                  //LAB_800c9860
                  if(MEMORY.ref(2, t0).offset(-0x10L).getSigned() < 0x81L || MEMORY.ref(2, t0).offset(-0x8L).getSigned() < 0x81L || MEMORY.ref(2, t0).offset(0x0L).getSigned() < 0x81L) {
                    //LAB_800c989c
                    CPU.COP2(0x158_002dL);
                    t1 = CPU.MFC2(7);

                    t1 = t1 + s8;
                    v0 = MEMORY.ref(4, a2).offset(0x4L).get();
                    v1 = MEMORY.ref(4, a1).offset(0x4L).get();
                    t1 = (int)t1 >> v0;
                    if((int)t1 < (int)v1) {
                      a0 = t1 << 2;
                    } else {
                      a0 = t1 << 2;
                      t1 = v1;
                      a0 = t1 << 2;
                    }

                    //LAB_800c98cc
                    a0 = t9 + a0;
                    v0 = t3 + 0x4L;
                    CPU.MTC2(MEMORY.ref(4, v0).get(), 6);
                    t5 = MEMORY.ref(2, t3).offset(0x8L).get();
                    t6 = MEMORY.ref(2, t3).offset(0xcL).get();
                    t7 = MEMORY.ref(2, t3).offset(0x10L).get();
                    t5 = t5 << 3;
                    t6 = t6 << 3;
                    t7 = t7 << 3;
                    t5 = s1 + t5;
                    t6 = s1 + t6;
                    t7 = s1 + t7;
                    CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
                    CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
                    CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
                    CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
                    CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
                    CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);
                    CPU.COP2(0x118_043fL);

                    v0 = MEMORY.ref(4, a0).offset(0x0L).get();
                    v1 = 0x600_0000L;
                    v0 = v0 & t8;
                    v0 = v0 | v1;
                    MEMORY.ref(4, t2).offset(0x0L).setu(v0);
                    v0 = t2 & t8;
                    MEMORY.ref(4, a0).offset(0x0L).setu(v0);
                    MEMORY.ref(4, t2).offset(0x04L).setu(CPU.MFC2(20));
                    MEMORY.ref(4, t2).offset(0x0cL).setu(CPU.MFC2(21));
                    MEMORY.ref(4, t2).offset(0x14L).setu(CPU.MFC2(22));
                    t0 = t0 + 0x1cL;
                    t2 = t2 + 0x1cL;
                  }
                }
              }
            }
          }
        }

        //LAB_800c9950
        t3 = t3 + 0x14L;
      } while(a3 != 0);
    }

    //LAB_800c9958
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t2);
    v0 = t3;
    return v0;
  }

  @Method(0x800c9974L)
  public static long renderPrimitive3c(long primitives, long vertices, long normals, long count) {
    long a0;
    long v0;
    long v1;
    long t2;
    long t3;
    long t5;
    long t6;
    long t7;

    long packet = linkedListAddress_1f8003d8.get();
    MEMORY.ref(1, packet).offset(0x3L).setu(0xcL);
    MEMORY.ref(4, packet).offset(0x4L).setu(0x3c80_8080L);
    CPU.MTC2(MEMORY.ref(4, packet).offset(0x4L).get(), 6);

    //LAB_800c99e4
    while(count != 0) {
      t5 = vertices + MEMORY.ref(2, primitives).offset(0x16L).get() * 0x8L;
      t6 = vertices + MEMORY.ref(2, primitives).offset(0x1aL).get() * 0x8L;
      t7 = vertices + MEMORY.ref(2, primitives).offset(0x1eL).get() * 0x8L;
      CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
      CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
      CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
      CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
      CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
      CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);
      CPU.COP2(0x28_0030L); // Perspective transform triple

      MEMORY.ref(4, packet).offset(0xcL).setu(MEMORY.ref(4, primitives).offset(0x4L).get());
      MEMORY.ref(4, packet).offset(0x18L).setu(MEMORY.ref(4, primitives).offset(0x8L).get());

      if((int)CPU.CFC2(31) >= 0) { // Flags
        CPU.COP2(0x140_0006L); // Normal clipping

        MEMORY.ref(4, packet).offset(0x24L).setu(MEMORY.ref(4, primitives).offset(0xcL).get());

        if((int)CPU.MFC2(24) > 0) { // MAC0 (vertices arranged clockwise)
          MEMORY.ref(4, packet).offset(0x08L).setu(CPU.MFC2(12)); // SXY0
          MEMORY.ref(4, packet).offset(0x14L).setu(CPU.MFC2(13)); // SXY1
          MEMORY.ref(4, packet).offset(0x20L).setu(CPU.MFC2(14)); // SXY2

          v0 = vertices + MEMORY.ref(2, primitives).offset(0x22L).get() * 0x8L;
          CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0); // VXY0
          CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1); // VZ0
          CPU.COP2(0x18_0001L); // Perspective transform single
          t3 = CPU.CFC2(31); // Flags
          MEMORY.ref(4, packet).offset(0x2cL).setu(CPU.MFC2(14)); // SXY2

          if(MEMORY.ref(2, packet).offset(0x8L).getSigned() >= -0xc0L || MEMORY.ref(2, packet).offset(0x14L).getSigned() >= -0xc0L || MEMORY.ref(2, packet).offset(0x20L).getSigned() >= -0xc0L || MEMORY.ref(2, packet).offset(0x2cL).getSigned() >= -0xc0L) {
            //LAB_800c9b00
            if(MEMORY.ref(2, packet).offset(0xaL).getSigned() >= -0x80L || MEMORY.ref(2, packet).offset(0x16L).getSigned() >= -0x80L || MEMORY.ref(2, packet).offset(0x22L).getSigned() >= -0x80L || MEMORY.ref(2, packet).offset(0x2eL).getSigned() >= -0x80L) {
              //LAB_800c9b50
              if(MEMORY.ref(2, packet).offset(0x8L).getSigned() < 0xc1L || MEMORY.ref(2, packet).offset(0x14L).getSigned() < 0xc1L || MEMORY.ref(2, packet).offset(0x20L).getSigned() < 0xc1L || MEMORY.ref(2, packet).offset(0x2cL).getSigned() < 0xc1L) {
                //LAB_800c9ba0
                if(MEMORY.ref(2, packet).offset(0xaL).getSigned() < 0x81L || MEMORY.ref(2, packet).offset(0x16L).getSigned() < 0x81L || MEMORY.ref(2, packet).offset(0x22L).getSigned() < 0x81L || MEMORY.ref(2, packet).offset(0x2eL).getSigned() < 0x81L) {
                  //LAB_800c9bf0
                  CPU.COP2(0x168_002eL); // Average of four Z values

                  MEMORY.ref(4, packet).offset(0x30L).setu(MEMORY.ref(4, primitives).offset(0x10L));

                  t2 = CPU.MFC2(7); // Average Z value
                  t2 = t2 + _1f8003e8.get();
                  v1 = _1f8003cc.get();
                  t2 = (int)t2 >> _1f8003c4.get();
                  if((int)t2 >= (int)v1) {
                    t2 = v1;
                  }

                  //LAB_800c9c2c
                  a0 = tags_1f8003d0.deref().get((int)t2).getAddress();
                  t5 = normals + MEMORY.ref(2, primitives).offset(0x14L).get() * 0x8L;
                  t6 = normals + MEMORY.ref(2, primitives).offset(0x18L).get() * 0x8L;
                  t7 = normals + MEMORY.ref(2, primitives).offset(0x1cL).get() * 0x8L;
                  CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0); // VXY0
                  CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1); // VZ0
                  CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2); // VXY1
                  CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3); // VZ1
                  CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4); // VXY2
                  CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5); // VZ2
                  CPU.COP2(0x118_043fL); // Normal colour colour triple vector (NCCT)
                  MEMORY.ref(4, packet).offset(0x04L).setu(CPU.MFC2(20)); // RGB0
                  MEMORY.ref(4, packet).offset(0x10L).setu(CPU.MFC2(21)); // RGB1
                  MEMORY.ref(4, packet).offset(0x1cL).setu(CPU.MFC2(22)); // RGB2

                  v0 = normals + MEMORY.ref(2, primitives).offset(0x20L).get() * 0x8L;
                  CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0); // VXY0
                  CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1); // VZ0
                  CPU.COP2(0x108_041bL); // Normal colour colour single vector (NCCS)

                  MEMORY.ref(4, packet).offset(0x0L).setu(0xc00_0000L | MEMORY.ref(4, a0).offset(0x0L).get() & 0xff_ffffL);
                  MEMORY.ref(4, a0).offset(0x0L).setu(packet & 0xff_ffffL);
                  MEMORY.ref(4, packet).offset(0x28L).setu(CPU.MFC2(22)); // RGB2
                  packet = packet + 0x34L;
                }
              }
            }
          }
        }
      }

      //LAB_800c9cd0
      primitives = primitives + 0x24L;
      count--;
    }

    //LAB_800c9cdc
    linkedListAddress_1f8003d8.setu(packet);
    return primitives;
  }

  @Method(0x800c9cfcL)
  public static long renderPrimitive34(long a0, long a1, long a2, long a3) {
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

    t3 = a0;
    s1 = a1;
    s2 = a2;
    a1 = 0x3480_0000L;
    a1 = a1 | 0x8080L;
    v0 = 0x1f80_0000L;
    t0 = MEMORY.ref(4, v0).offset(0x3d8L).get();
    v0 = 0x1f80_0000L;
    v1 = 0x1f80_0000L;
    s8 = MEMORY.ref(4, v0).offset(0x3e8L).get();
    t9 = MEMORY.ref(4, v1).offset(0x3d0L).get();
    v0 = 0x9L;
    MEMORY.ref(1, t0).offset(0x3L).setu(v0);
    v0 = t0 + 0x4L;
    MEMORY.ref(4, t0).offset(0x4L).setu(a1);
    CPU.MTC2(MEMORY.ref(4, v0).get(), 6);
    if(a3 == 0) {
      v0 = 0x1f80_0000L;
    } else {
      v0 = 0x1f80_0000L;
      s0 = v0 + 0x3c0L;
      v0 = 0x1f80_0000L;
      a2 = v0 + 0x3c8L;
      t8 = 0xff_0000L;
      t8 = t8 | 0xffffL;
      t2 = a0 + 0xcL;
      a1 = t0 + 0x22L;

      //LAB_800c9d70
      do {
        t5 = MEMORY.ref(2, t3).offset(0x12L).get();
        t6 = MEMORY.ref(2, t3).offset(0x16L).get();
        t7 = MEMORY.ref(2, t3).offset(0x1aL).get();
        t5 = t5 << 3;
        t6 = t6 << 3;
        t7 = t7 << 3;
        t5 = s1 + t5;
        t6 = s1 + t6;
        t7 = s1 + t7;
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);
        CPU.COP2(0x28_0030L);

        v0 = MEMORY.ref(4, t2).offset(-0x8L).get();

        MEMORY.ref(4, a1-0x16L).setu(v0);
        v0 = MEMORY.ref(4, t2).offset(-0x4L).get();
        a3 = a3 - 0x1L;
        MEMORY.ref(4, a1-0xaL).setu(v0);
        t4 = CPU.CFC2(31);

        if((int)t4 >= 0) {
          CPU.COP2(0x140_0006L);
          v0 = MEMORY.ref(4, t2).offset(0x0L).get();

          MEMORY.ref(4, a1+0x2L).setu(v0);
          t4 = CPU.MFC2(24);

          if((int)t4 > 0) {
            MEMORY.ref(4, t0).offset(0x08L).setu(CPU.MFC2(12));
            MEMORY.ref(4, t0).offset(0x14L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t0).offset(0x20L).setu(CPU.MFC2(14));

            if(MEMORY.ref(2, a1).offset(-0x1aL).getSigned() >= -0xc0L || MEMORY.ref(2, a1).offset(-0xeL).getSigned() >= -0xc0L || MEMORY.ref(2, a1).offset(-0x2L).getSigned() >= -0xc0L) {
              //LAB_800c9e48
              if(MEMORY.ref(2, a1).offset(-0x18L).getSigned() >= -0x80L || MEMORY.ref(2, a1).offset(-0xcL).getSigned() >= -0x80L || MEMORY.ref(2, a1).offset(0x0L).getSigned() >= -0x80L) {
                //LAB_800c9e84
                if(MEMORY.ref(2, a1).offset(-0x1aL).getSigned() < 0xc1L || MEMORY.ref(2, a1).offset(-0xeL).getSigned() < 0xc1L || MEMORY.ref(2, a1).offset(-0x2L).getSigned() < 0xc1L) {
                  //LAB_800c9ec0
                  if(MEMORY.ref(2, a1).offset(-0x18L).getSigned() < 0x81L || MEMORY.ref(2, a1).offset(-0xcL).getSigned() < 0x81L || MEMORY.ref(2, a1).offset(0x0L).getSigned() < 0x81L) {
                    //LAB_800c9efc
                    CPU.COP2(0x158_002dL);
                    t1 = CPU.MFC2(7);

                    t1 = t1 + s8;
                    v0 = MEMORY.ref(4, s0).offset(0x4L).get();
                    v1 = MEMORY.ref(4, a2).offset(0x4L).get();
                    t1 = (int)t1 >> v0;
                    if((int)t1 < (int)v1) {
                      a0 = t1 << 2;
                    } else {
                      a0 = t1 << 2;
                      t1 = v1;
                      a0 = t1 << 2;
                    }

                    //LAB_800c9f2c
                    a0 = t9 + a0;
                    t5 = MEMORY.ref(2, t3).offset(0x10L).get();
                    t6 = MEMORY.ref(2, t3).offset(0x14L).get();
                    t7 = MEMORY.ref(2, t3).offset(0x18L).get();
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
                    CPU.COP2(0x118_043fL);
                    MEMORY.ref(4, t0).offset(0x04L).setu(CPU.MFC2(20));
                    MEMORY.ref(4, t0).offset(0x10L).setu(CPU.MFC2(21));
                    MEMORY.ref(4, t0).offset(0x1cL).setu(CPU.MFC2(22));
                    a1 = a1 + 0x28L;
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
            }
          }
        }

        //LAB_800c9fa8
        t2 = t2 + 0x1cL;
        t3 = t3 + 0x1cL;
      } while(a3 != 0);
    }

    //LAB_800c9fb4
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t0);
    v0 = t3;
    return v0;
  }

  @Method(0x800c9fd4L)
  public static long renderPrimitive3804(long a0, long a1, long a2, long a3) {
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
    long sp;

    v0 = 0x1f80_0000L;
    t1 = MEMORY.ref(4, v0).offset(0x3d8L).get();
    v0 = 0x1f80_0000L;
    v1 = 0x1f80_0000L;
    s0 = MEMORY.ref(4, v0).offset(0x3e8L).get();
    s8 = MEMORY.ref(4, v1).offset(0x3d0L).get();
    t3 = a0;
    if(a3 != 0) {
      v0 = 0x1f80_0000L;
      s2 = v0 + 0x3c0L;
      v0 = 0x1f80_0000L;
      s1 = v0 + 0x3c8L;
      t9 = 0xff_0000L;
      t9 = t9 | 0xffffL;
      t4 = a0 + 0x20L;
      t0 = t1 + 0x1cL;

      //LAB_800ca024
      do {
        t5 = MEMORY.ref(2, t3).offset(0x16L).get();
        t6 = MEMORY.ref(2, t3).offset(0x1aL).get();
        t7 = MEMORY.ref(2, t3).offset(0x1eL).get();
        t5 = t5 << 3;
        t6 = t6 << 3;
        t7 = t7 << 3;
        t5 = a1 + t5;
        t6 = a1 + t6;
        t7 = a1 + t7;
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);
        CPU.COP2(0x28_0030L);
        a3 = a3 - 0x1L;
        t8 = CPU.CFC2(31);

        if((int)t8 >= 0) {
          CPU.COP2(0x140_0006L);
          t8 = CPU.MFC2(24);

          if((int)t8 > 0) {
            MEMORY.ref(4, t1).offset(0x08L).setu(CPU.MFC2(12));
            MEMORY.ref(4, t1).offset(0x10L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t1).offset(0x18L).setu(CPU.MFC2(14));
            v0 = MEMORY.ref(2, t4).offset(0x2L).get();

            v0 = v0 << 3;
            v0 = a1 + v0;
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
            CPU.COP2(0x18_0001L);
            t8 = CPU.CFC2(31);
            v0 = t1 + 0x20L;
            MEMORY.ref(4, v0).setu(CPU.MFC2(14));

            if(MEMORY.ref(2, t0).offset(-0x14L).getSigned() >= -0xc0L || MEMORY.ref(2, t0).offset(-0xcL).getSigned() >= -0xc0L || MEMORY.ref(2, t0).offset(-0x4L).getSigned() >= -0xc0L || MEMORY.ref(2, t0).offset(0x4L).getSigned() >= -0xc0L) {
              //LAB_800ca120
              if(MEMORY.ref(2, t0).offset(-0x12L).getSigned() >= -0x80L || MEMORY.ref(2, t0).offset(-0xaL).getSigned() >= -0x80L || MEMORY.ref(2, t0).offset(-0x2L).getSigned() >= -0x80L || MEMORY.ref(2, t0).offset(0x6L).getSigned() >= -0x80L) {
                //LAB_800ca170
                if(MEMORY.ref(2, t0).offset(-0x14L).getSigned() < 0xc1L || MEMORY.ref(2, t0).offset(-0xcL).getSigned() < 0xc1L || MEMORY.ref(2, t0).offset(-0x4L).getSigned() < 0xc1L || MEMORY.ref(2, t0).offset(0x4L).getSigned() < 0xc1L) {
                  //LAB_800ca1c0
                  if(MEMORY.ref(2, t0).offset(-0x12L).getSigned() < 0x81L || MEMORY.ref(2, t0).offset(-0xaL).getSigned() < 0x81L || MEMORY.ref(2, t0).offset(-0x2L).getSigned() < 0x81L || MEMORY.ref(2, t0).offset(0x6L).getSigned() < 0x81L) {
                    //LAB_800ca210
                    CPU.COP2(0x168_002eL);
                    t2 = CPU.MFC2(7);

                    t2 = t2 + s0;
                    v0 = MEMORY.ref(4, s2).offset(0x4L).get();
                    v1 = MEMORY.ref(4, s1).offset(0x4L).get();
                    t2 = (int)t2 >> v0;
                    if((int)t2 < (int)v1) {
                      a0 = t2 << 2;
                    } else {
                      a0 = t2 << 2;
                      t2 = v1;
                      a0 = t2 << 2;
                    }

                    //LAB_800ca240
                    a0 = s8 + a0;
                    v0 = t3 + 0x4L;
                    CPU.MTC2(MEMORY.ref(4, v0).get(), 6);
                    v0 = MEMORY.ref(2, t4).offset(-0xcL).get();

                    v0 = v0 << 3;
                    v0 = a2 + v0;
                    CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
                    CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
                    CPU.COP2(0x108_041bL);
                    v0 = t1 + 0x4L;
                    MEMORY.ref(4, v0).setu(CPU.MFC2(22));
                    v0 = t3 + 0x8L;
                    CPU.MTC2(MEMORY.ref(4, v0).get(), 6);
                    v0 = MEMORY.ref(2, t4).offset(-0x8L).get();

                    v0 = v0 << 3;
                    v0 = a2 + v0;
                    CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
                    CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
                    CPU.COP2(0x108_041bL);
                    v0 = t1 + 0xcL;
                    MEMORY.ref(4, v0).setu(CPU.MFC2(22));
                    v0 = t3 + 0xcL;
                    CPU.MTC2(MEMORY.ref(4, v0).get(), 6);
                    v0 = MEMORY.ref(2, t4).offset(-0x4L).get();

                    v0 = v0 << 3;
                    v0 = a2 + v0;
                    CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
                    CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
                    CPU.COP2(0x108_041bL);
                    v0 = t1 + 0x14L;
                    MEMORY.ref(4, v0).setu(CPU.MFC2(22));
                    v0 = t3 + 0x10L;
                    CPU.MTC2(MEMORY.ref(4, v0).get(), 6);
                    v0 = MEMORY.ref(2, t4).offset(0x0L).get();

                    v0 = v0 << 3;
                    v0 = a2 + v0;
                    CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
                    CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
                    CPU.COP2(0x108_041bL);
                    MEMORY.ref(4, t0).setu(CPU.MFC2(22));
                    t0 = t0 + 0x24L;
                    v1 = MEMORY.ref(4, a0).offset(0x0L).get();
                    v0 = 0x800_0000L;
                    v1 = v1 & t9;
                    v1 = v1 | v0;
                    v0 = t1 & t9;
                    MEMORY.ref(4, t1).offset(0x0L).setu(v1);
                    t1 = t1 + 0x24L;
                    MEMORY.ref(4, a0).offset(0x0L).setu(v0);
                  }
                }
              }
            }
          }
        }

        //LAB_800ca334
        t4 = t4 + 0x24L;
        t3 = t3 + 0x24L;
      } while(a3 != 0);
    }

    //LAB_800ca340
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t1);
    v0 = t3;
    return v0;
  }

  @Method(0x800ca360L)
  public static long renderPrimitive3004(long a0, long a1, long a2, long a3) {
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
    v0 = 0x1f80_0000L;
    t1 = MEMORY.ref(4, v0).offset(0x3d8L).get();
    v0 = 0x1f80_0000L;
    v1 = 0x1f80_0000L;
    a1 = MEMORY.ref(4, v0).offset(0x3e8L).get();
    s8 = MEMORY.ref(4, v1).offset(0x3d0L).get();
    t3 = a0;
    if(a3 != 0) {
      v0 = 0x1f80_0000L;
      s1 = v0 + 0x3c0L;
      v0 = 0x1f80_0000L;
      s0 = v0 + 0x3c8L;
      t9 = 0xff_0000L;
      t9 = t9 | 0xffffL;
      t4 = a0 + 0x18L;
      t0 = t1 + 0x14L;

      //LAB_800ca3b4
      do {
        t5 = MEMORY.ref(2, t3).offset(0x12L).get();
        t6 = MEMORY.ref(2, t3).offset(0x16L).get();
        t7 = MEMORY.ref(2, t3).offset(0x1aL).get();
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
        t8 = CPU.CFC2(31);

        if((int)t8 >= 0) {
          CPU.COP2(0x1400006L);
          t8 = CPU.MFC2(24);

          if((int)t8 > 0) {
            MEMORY.ref(4, t1).offset(0x8L).setu(CPU.MFC2(12));
            MEMORY.ref(4, t1).offset(0x10L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t1).offset(0x18L).setu(CPU.MFC2(14));

            if(MEMORY.ref(2, t0).offset(-0xcL).getSigned() >= -0xc0L || MEMORY.ref(2, t0).offset(-0x4L).getSigned() >= -0xc0L || MEMORY.ref(2, t0).offset(0x4L).getSigned() >= -0xc0L) {
              //LAB_800ca46c
              if(MEMORY.ref(2, t0).offset(-0xaL).getSigned() >= -0x80L || MEMORY.ref(2, t0).offset(-0x2L).getSigned() >= -0x80L || MEMORY.ref(2, t0).offset(0x6L).getSigned() >= -0x80L) {
                //LAB_800ca4a8
                if(MEMORY.ref(2, t0).offset(-0xcL).getSigned() < 0xc1L || MEMORY.ref(2, t0).offset(-0x4L).getSigned() < 0xc1L || MEMORY.ref(2, t0).offset(0x4L).getSigned() < 0xc1L) {
                  //LAB_800ca4e4
                  if(MEMORY.ref(2, t0).offset(-0xaL).getSigned() < 0x81L || MEMORY.ref(2, t0).offset(-0x2L).getSigned() < 0x81L || MEMORY.ref(2, t0).offset(0x6L).getSigned() < 0x81L) {
                    //LAB_800ca520
                    CPU.COP2(0x158002dL);
                    t2 = CPU.MFC2(7);

                    t2 = t2 + a1;
                    v0 = MEMORY.ref(4, s1).offset(0x4L).get();
                    v1 = MEMORY.ref(4, s0).offset(0x4L).get();
                    t2 = (int)t2 >> v0;
                    if((int)t2 < (int)v1) {
                      a0 = t2 << 2;
                    } else {
                      a0 = t2 << 2;
                      t2 = v1;
                      a0 = t2 << 2;
                    }

                    //LAB_800ca550
                    a0 = s8 + a0;
                    v0 = t3 + 0x4L;
                    CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
                    v0 = MEMORY.ref(2, t4).offset(-0x8L).get();

                    v0 = v0 << 3;
                    v0 = a2 + v0;
                    CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
                    CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);


                    CPU.COP2(0x108041bL);
                    v0 = t1 + 0x4L;
                    MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(22));
                    v0 = t3 + 0x8L;
                    CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
                    v0 = MEMORY.ref(2, t4).offset(-0x4L).get();

                    v0 = v0 << 3;
                    v0 = a2 + v0;
                    CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
                    CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);


                    CPU.COP2(0x108041bL);
                    v0 = t1 + 0xcL;
                    MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(22));
                    v0 = t3 + 0xcL;
                    CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
                    v0 = MEMORY.ref(2, t4).offset(0x0L).get();

                    v0 = v0 << 3;
                    v0 = a2 + v0;
                    CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
                    CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);


                    CPU.COP2(0x108041bL);
                    MEMORY.ref(4, t0).offset(0x0L).setu(CPU.MFC2(22));
                    t0 = t0 + 0x1cL;
                    v1 = MEMORY.ref(4, a0).offset(0x0L).get();
                    v0 = 0x600_0000L;
                    v1 = v1 & t9;
                    v1 = v1 | v0;
                    v0 = t1 & t9;
                    MEMORY.ref(4, t1).offset(0x0L).setu(v1);
                    t1 = t1 + 0x1cL;
                    MEMORY.ref(4, a0).offset(0x0L).setu(v0);
                  }
                }
              }
            }
          }
        }

        //LAB_800ca610
        t4 = t4 + 0x1cL;
        t3 = t3 + 0x1cL;
      } while(a3 != 0);
    }

    //LAB_800ca61c
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t1);
    v0 = t3;
    return v0;
  }

  @Method(0x800ca63cL)
  public static long renderPrimitive3d(long a0, long a1, long a2) {
    long v0;
    long v1;
    long a3;
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
    long t8;
    long t9;
    long s8;
    v0 = 0x1f80_0000L;
    t2 = MEMORY.ref(4, v0).offset(0x3d8L).get();
    v0 = 0x1f80_0000L;
    v1 = 0x1f80_0000L;
    s8 = MEMORY.ref(4, v0).offset(0x3e8L).get();
    t9 = MEMORY.ref(4, v1).offset(0x3d0L).get();
    t4 = a0;
    if(a2 != 0) {
      v0 = 0x1f80_0000L;
      s1 = v0 + 0x3c0L;
      v0 = 0x1f80_0000L;
      s0 = v0 + 0x3c8L;
      t8 = 0xff_0000L;
      t8 = t8 | 0xffffL;
      t0 = a0 + 0x22L;
      a3 = t2 + 0x2aL;

      //LAB_800ca688
      do {
        t5 = MEMORY.ref(2, t4).offset(0x24L).get();
        t6 = MEMORY.ref(2, t4).offset(0x26L).get();
        t7 = MEMORY.ref(2, t4).offset(0x28L).get();
        t5 = t5 << 3;
        t6 = t6 << 3;
        t7 = t7 << 3;
        t5 = a1 + t5;
        t6 = a1 + t6;
        t7 = a1 + t7;
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);
        CPU.COP2(0x28_0030L); // Perspective transform triple

        v0 = MEMORY.ref(4, t0-0x1eL).get();

        MEMORY.ref(4, a3-0x1eL).setu(v0);
        v0 = MEMORY.ref(4, t0-0x1aL).get();
        a2 = a2 - 0x1L;
        MEMORY.ref(4, a3-0x12L).setu(v0);
        t3 = CPU.CFC2(31);

        if((int)t3 >= 0) {
          CPU.COP2(0x140_0006L);

          v0 = MEMORY.ref(4, t0-0x16L).get();

          MEMORY.ref(4, a3-0x6L).setu(v0);
          t3 = CPU.MFC2(24);

          if((int)t3 > 0) {
            MEMORY.ref(4, t2).offset(0x08L).setu(CPU.MFC2(12));
            MEMORY.ref(4, t2).offset(0x14L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t2).offset(0x20L).setu(CPU.MFC2(14));

            v0 = MEMORY.ref(2, t0).offset(0x8L).get();

            v0 = v0 << 3;
            v0 = a1 + v0;
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
            CPU.COP2(0x18_0001L);

            a0 = 0x3c80_0000L;
            a0 = a0 | 0x8080L;
            v1 = MEMORY.ref(4, t0-0x12L).get();
            v0 = 0xcL;
            MEMORY.ref(1, a3).offset(-0x27L).setu(v0);
            MEMORY.ref(4, a3-0x26L).setu(a0);
            MEMORY.ref(4, a3+0x6L).setu(v1);
            t3 = CPU.CFC2(31);
            v0 = t2 + 0x2cL;
            MEMORY.ref(4, v0).setu(CPU.MFC2(14));

            if(MEMORY.ref(2, a3).offset(-0x22L).getSigned() >= -0xc0L || MEMORY.ref(2, a3).offset(-0x16L).getSigned() >= -0xc0L || MEMORY.ref(2, a3).offset(-0xaL).getSigned() >= -0xc0L || MEMORY.ref(2, a3).offset(0x2L).getSigned() >= -0xc0L) {
              //LAB_800ca7c8
              if(MEMORY.ref(2, a3).offset(-0x20L).getSigned() >= -0x80L || MEMORY.ref(2, a3).offset(-0x14L).getSigned() >= -0x80L || MEMORY.ref(2, a3).offset(-0x8L).getSigned() >= -0x80L || MEMORY.ref(2, a3).offset(0x4L).getSigned() >= -0x80L) {
                //LAB_800ca818
                if(MEMORY.ref(2, a3).offset(-0x22L).getSigned() < 0xc1L || MEMORY.ref(2, a3).offset(-0x16L).getSigned() < 0xc1L || MEMORY.ref(2, a3).offset(-0xaL).getSigned() < 0xc1L || MEMORY.ref(2, a3).offset(0x2L).getSigned() < 0xc1L) {
                  //LAB_800ca868
                  if(MEMORY.ref(2, a3).offset(-0x20L).getSigned() < 0x81L || MEMORY.ref(2, a3).offset(-0x14L).getSigned() < 0x81L || MEMORY.ref(2, a3).offset(-0x8L).getSigned() < 0x81L || MEMORY.ref(2, a3).offset(0x4L).getSigned() < 0x81L) {
                    //LAB_800ca8b8
                    CPU.COP2(0x168_002eL);

                    v0 = MEMORY.ref(1, t0).offset(-0xeL).get();

                    MEMORY.ref(1, a3).offset(-0x26L).setu(v0);
                    v0 = MEMORY.ref(1, t0).offset(-0xdL).get();

                    MEMORY.ref(1, a3).offset(-0x25L).setu(v0);
                    v0 = MEMORY.ref(1, t0).offset(-0xcL).get();

                    MEMORY.ref(1, a3).offset(-0x24L).setu(v0);
                    v0 = MEMORY.ref(1, t0).offset(-0xaL).get();

                    MEMORY.ref(1, a3).offset(-0x1aL).setu(v0);
                    v0 = MEMORY.ref(1, t0).offset(-0x9L).get();

                    MEMORY.ref(1, a3).offset(-0x19L).setu(v0);
                    v0 = MEMORY.ref(1, t0).offset(-0x8L).get();

                    MEMORY.ref(1, a3).offset(-0x18L).setu(v0);
                    v0 = MEMORY.ref(1, t0).offset(-0x6L).get();

                    MEMORY.ref(1, a3).offset(-0xeL).setu(v0);
                    v0 = MEMORY.ref(1, t0).offset(-0x5L).get();

                    MEMORY.ref(1, a3).offset(-0xdL).setu(v0);
                    v0 = MEMORY.ref(1, t0).offset(-0x4L).get();

                    MEMORY.ref(1, a3).offset(-0xcL).setu(v0);
                    v0 = MEMORY.ref(1, t0).offset(-0x2L).get();

                    MEMORY.ref(1, a3).offset(-0x2L).setu(v0);
                    v0 = MEMORY.ref(1, t0).offset(-0x1L).get();

                    MEMORY.ref(1, a3).offset(-0x1L).setu(v0);
                    v0 = MEMORY.ref(1, t0).offset(0x0L).get();

                    MEMORY.ref(1, a3).offset(0x0L).setu(v0);
                    t1 = CPU.MFC2(7);
                    t1 = t1 + s8;
                    v0 = MEMORY.ref(4, s1).offset(0x4L).get();
                    v1 = MEMORY.ref(4, s0).offset(0x4L).get();
                    t1 = (int)t1 >> v0;
                    a0 = t1 << 2;
                    if((int)t1 >= (int)v1) {
                      t1 = v1;
                      a0 = t1 << 2;
                    }

                    //LAB_800ca978
                    a0 = t9 + a0;
                    a3 = a3 + 0x34L;
                    v1 = MEMORY.ref(4, a0).offset(0x0L).get();
                    v0 = 0xc00_0000L;
                    v1 = v1 & t8;
                    v1 = v1 | v0;
                    v0 = t2 & t8;
                    MEMORY.ref(4, t2).offset(0x0L).setu(v1);
                    t2 = t2 + 0x34L;
                    MEMORY.ref(4, a0).offset(0x0L).setu(v0);
                  }
                }
              }
            }
          }
        }

        //LAB_800ca9a0
        t0 = t0 + 0x2cL;
        t4 = t4 + 0x2cL;
      } while(a2 != 0);
    }

    //LAB_800ca9ac
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t2);
    v0 = t4;
    return v0;
  }

  @Method(0x800ca9c8L)
  public static long renderPrimitive35(long a0, long a1, long a2) {
    long v0;
    long v1;
    long a3;
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
    long t8;
    long t9;
    long s8;
    v0 = 0x1f80_0000L;
    t2 = MEMORY.ref(4, v0).offset(0x3d8L).get();
    v0 = 0x1f80_0000L;
    v1 = 0x1f80_0000L;
    s8 = MEMORY.ref(4, v0).offset(0x3e8L).get();
    t9 = MEMORY.ref(4, v1).offset(0x3d0L).get();
    t4 = a0;
    s1 = a1;
    if(a2 != 0) {
      v0 = 0x1f80_0000L;
      s0 = v0 + 0x3c0L;
      v0 = 0x1f80_0000L;
      a1 = v0 + 0x3c8L;
      t8 = 0xff_0000L;
      t8 = t8 | 0xffffL;
      t0 = a0 + 0x1aL;
      a3 = t2 + 0x1eL;

      //LAB_800caa18
      do {
        t5 = MEMORY.ref(2, t4).offset(0x1cL).get();
        t6 = MEMORY.ref(2, t4).offset(0x1eL).get();
        t7 = MEMORY.ref(2, t4).offset(0x20L).get();
        t5 = t5 << 3;
        t6 = t6 << 3;
        t7 = t7 << 3;
        t5 = s1 + t5;
        t6 = s1 + t6;
        t7 = s1 + t7;
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);
        CPU.COP2(0x28_0030L);

        v0 = MEMORY.ref(4, t0-0x16L).get();

        MEMORY.ref(4, a3-0x12L).setu(v0);
        v0 = MEMORY.ref(4, t0-0x12L).get();
        a2 = a2 - 0x1L;
        MEMORY.ref(4, a3-0x6L).setu(v0);
        t3 = CPU.CFC2(31);

        if((int)t3 >= 0) {
          CPU.COP2(0x140_0006L);
          v0 = MEMORY.ref(4, t0-0xeL).get();

          MEMORY.ref(4, a3+0x6L).setu(v0);
          t3 = CPU.MFC2(24);

          if((int)t3 > 0) {
            MEMORY.ref(4, t2).offset(0x08L).setu(CPU.MFC2(12));
            MEMORY.ref(4, t2).offset(0x14L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t2).offset(0x20L).setu(CPU.MFC2(14));
            v1 = 0x3480_0000L;
            v1 = v1 | 0x8080L;
            v0 = 0x9L;
            MEMORY.ref(1, a3).offset(-0x1bL).setu(v0);
            MEMORY.ref(4, a3-0x1aL).setu(v1);
            t3 = CPU.CFC2(31);

            if(MEMORY.ref(2, a3).offset(-0x16L).getSigned() >= -0xc0L || MEMORY.ref(2, a3).offset(-0xaL).getSigned() >= -0xc0L || MEMORY.ref(2, a3).offset(0x2L).getSigned() >= -0xc0L) {
              //LAB_800cab10
              if(MEMORY.ref(2, a3).offset(-0x14L).getSigned() >= -0x80L || MEMORY.ref(2, a3).offset(-0x8L).getSigned() >= -0x80L || MEMORY.ref(2, a3).offset(0x4L).getSigned() >= -0x80L) {
                //LAB_800cab4c
                if(MEMORY.ref(2, a3).offset(-0x16L).getSigned() < 0xc1L || MEMORY.ref(2, a3).offset(-0xaL).getSigned() < 0xc1L || MEMORY.ref(2, a3).offset(0x2L).getSigned() < 0xc1L) {
                  //LAB_800cab88
                  if(MEMORY.ref(2, a3).offset(-0x14L).getSigned() < 0x81L || MEMORY.ref(2, a3).offset(-0x8L).getSigned() < 0x81L || MEMORY.ref(2, a3).offset(0x4L).getSigned() < 0x81L) {
                    //LAB_800cabc4
                    CPU.COP2(0x158_002dL);
                    v0 = MEMORY.ref(1, t0).offset(-0xaL).get();

                    MEMORY.ref(1, a3).offset(-0x1aL).setu(v0);
                    v0 = MEMORY.ref(1, t0).offset(-0x9L).get();

                    MEMORY.ref(1, a3).offset(-0x19L).setu(v0);
                    v0 = MEMORY.ref(1, t0).offset(-0x8L).get();

                    MEMORY.ref(1, a3).offset(-0x18L).setu(v0);
                    v0 = MEMORY.ref(1, t0).offset(-0x6L).get();

                    MEMORY.ref(1, a3).offset(-0xeL).setu(v0);
                    v0 = MEMORY.ref(1, t0).offset(-0x5L).get();

                    MEMORY.ref(1, a3).offset(-0xdL).setu(v0);
                    v0 = MEMORY.ref(1, t0).offset(-0x4L).get();

                    MEMORY.ref(1, a3).offset(-0xcL).setu(v0);
                    v0 = MEMORY.ref(1, t0).offset(-0x2L).get();

                    MEMORY.ref(1, a3).offset(-0x2L).setu(v0);
                    v0 = MEMORY.ref(1, t0).offset(-0x1L).get();

                    MEMORY.ref(1, a3).offset(-0x1L).setu(v0);
                    v0 = MEMORY.ref(1, t0).offset(0x0L).get();

                    MEMORY.ref(1, a3).offset(0x0L).setu(v0);
                    t1 = CPU.MFC2(7);

                    t1 = t1 + s8;
                    v0 = MEMORY.ref(4, s0).offset(0x4L).get();
                    v1 = MEMORY.ref(4, a1).offset(0x4L).get();
                    t1 = (int)t1 >> v0;
                    if((int)t1 < (int)v1) {
                      a0 = t1 << 2;
                    } else {
                      a0 = t1 << 2;
                      t1 = v1;
                      a0 = t1 << 2;
                    }

                    //LAB_800cac60
                    a0 = t9 + a0;
                    a3 = a3 + 0x28L;
                    v1 = MEMORY.ref(4, a0).offset(0x0L).get();
                    v0 = 0x900_0000L;
                    v1 = v1 & t8;
                    v1 = v1 | v0;
                    v0 = t2 & t8;
                    MEMORY.ref(4, t2).offset(0x0L).setu(v1);
                    t2 = t2 + 0x28L;
                    MEMORY.ref(4, a0).offset(0x0L).setu(v0);
                  }
                }
              }
            }
          }
        }

        //LAB_800cac88
        t0 = t0 + 0x24L;
        t4 = t4 + 0x24L;
      } while(a2 != 0);
    }

    //LAB_800cac94
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t2);
    v0 = t4;
    return v0;
  }

  @Method(0x800cacb0L)
  public static long renderPrimitive3e(long a0, long a1, long a2) {
    assert false;
    return 0;
  }

  @Method(0x800cb040L)
  public static long renderPrimitive36(long a0, long a1, long a2) {
    assert false;
    return 0;
  }

  @Method(0x800cb318L)
  public static long renderPrimitive3a(long a0, long a1, long a2) {
    assert false;
    return 0;
  }

  @Method(0x800cb6b4L)
  public static long renderPrimitive3200(long a0, long a1, long a2) {
    assert false;
    return 0;
  }

  @Method(0x800cb994L)
  public static long renderPrimitive3a04(long a0, long a1, long a2) {
    assert false;
    return 0;
  }

  @Method(0x800cbd80L)
  public static long renderPrimitive3204(long a0, long a1, long a2) {
    assert false;
    return 0;
  }

  @Method(0x800cc0b0L)
  public static long renderPrimitive3f(long a0, long a1, long a2) {
    long v0;
    long v1;
    long a3;
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
    long t8;
    long t9;
    long s8;

    v0 = 0x1f80_0000L;
    t2 = MEMORY.ref(4, v0).offset(0x3d8L).get();
    v0 = 0x1f80_0000L;
    v1 = 0x1f80_0000L;
    s8 = MEMORY.ref(4, v0).offset(0x3e8L).get();
    t9 = MEMORY.ref(4, v1).offset(0x3d0L).get();
    t4 = a0;
    if(a2 != 0) {
      v0 = 0x1f80_0000L;
      s1 = v0 + 0x3c0L;
      v0 = 0x1f80_0000L;
      s0 = v0 + 0x3c8L;
      t8 = 0xff_0000L;
      t8 = t8 | 0xffffL;
      t0 = a0 + 0x22L;
      a3 = t2 + 0x2aL;

      //LAB_800cc0fc
      do {
        t5 = MEMORY.ref(2, t4).offset(0x24L).get();
        t6 = MEMORY.ref(2, t4).offset(0x26L).get();
        t7 = MEMORY.ref(2, t4).offset(0x28L).get();
        t5 = t5 << 3;
        t6 = t6 << 3;
        t7 = t7 << 3;
        t5 = a1 + t5;
        t6 = a1 + t6;
        t7 = a1 + t7;
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
        CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
        CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
        CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);
        CPU.COP2(0x28_0030L);

        v0 = MEMORY.ref(4, t0-0x1eL).get();

        MEMORY.ref(4, a3-0x1eL).setu(v0);
        v0 = MEMORY.ref(4, t0-0x1aL).get();
        a2 = a2 - 0x1L;
        MEMORY.ref(4, a3-0x12L).setu(v0);
        t3 = CPU.CFC2(31);

        if((int)t3 >= 0) {
          CPU.COP2(0x140_0006L);

          v0 = MEMORY.ref(4, t0-0x16L).get();

          MEMORY.ref(4, a3-0x6L).setu(v0);
          t3 = CPU.MFC2(24);

          if(t3 != 0) {
            MEMORY.ref(4, t2).offset(0x08L).setu(CPU.MFC2(12));
            MEMORY.ref(4, t2).offset(0x14L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t2).offset(0x20L).setu(CPU.MFC2(14));
            v0 = MEMORY.ref(2, t0).offset(0x8L).get();

            v0 = v0 << 3;
            v0 = a1 + v0;
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
            CPU.COP2(0x18_0001L);

            a0 = 0x3e80_0000L;
            a0 = a0 | 0x8080L;
            v1 = MEMORY.ref(4, t0-0x12L).get();
            v0 = 0xcL;
            MEMORY.ref(1, a3).offset(-0x27L).setu(v0);
            MEMORY.ref(4, a3-0x26L).setu(a0);
            MEMORY.ref(4, a3+0x6L).setu(v1);
            t3 = CPU.CFC2(31);

            if((int)t3 < 0) {
              v0 = t2 + 0x2cL;
            } else {
              v0 = t2 + 0x2cL;
              MEMORY.ref(4, v0).setu(CPU.MFC2(14));

              if(MEMORY.ref(2, a3).offset(-0x22L).getSigned() >= -0xc0L || MEMORY.ref(2, a3).offset(-0x16L).getSigned() >= -0xc0L || MEMORY.ref(2, a3).offset(-0xaL).getSigned() >= -0xc0L || MEMORY.ref(2, a3).offset(0x2L).getSigned() >= -0xc0L) {
                //LAB_800cc244
                if(MEMORY.ref(2, a3).offset(-0x20L).getSigned() >= -0x80L || MEMORY.ref(2, a3).offset(-0x14L).getSigned() >= -0x80L || MEMORY.ref(2, a3).offset(-0x8L).getSigned() >= -0x80L || MEMORY.ref(2, a3).offset(0x4L).getSigned() >= -0x80L) {
                  //LAB_800cc294
                  if(MEMORY.ref(2, a3).offset(-0x22L).getSigned() < 0xc1L || MEMORY.ref(2, a3).offset(-0x16L).getSigned() < 0xc1L || MEMORY.ref(2, a3).offset(-0xaL).getSigned() < 0xc1L || MEMORY.ref(2, a3).offset(0x2L).getSigned() < 0xc1L) {
                    //LAB_800cc2e4
                    if(MEMORY.ref(2, a3).offset(-0x20L).getSigned() < 0x81L || MEMORY.ref(2, a3).offset(-0x14L).getSigned() < 0x81L || MEMORY.ref(2, a3).offset(-0x8L).getSigned() < 0x81L || MEMORY.ref(2, a3).offset(0x4L).getSigned() < 0x81L) {
                      //LAB_800cc334
                      CPU.COP2(0x168_002eL);
                      v0 = MEMORY.ref(1, t0).offset(-0xeL).get();

                      MEMORY.ref(1, a3).offset(-0x26L).setu(v0);
                      v0 = MEMORY.ref(1, t0).offset(-0xdL).get();

                      MEMORY.ref(1, a3).offset(-0x25L).setu(v0);
                      v0 = MEMORY.ref(1, t0).offset(-0xcL).get();

                      MEMORY.ref(1, a3).offset(-0x24L).setu(v0);
                      v0 = MEMORY.ref(1, t0).offset(-0xaL).get();

                      MEMORY.ref(1, a3).offset(-0x1aL).setu(v0);
                      v0 = MEMORY.ref(1, t0).offset(-0x9L).get();

                      MEMORY.ref(1, a3).offset(-0x19L).setu(v0);
                      v0 = MEMORY.ref(1, t0).offset(-0x8L).get();

                      MEMORY.ref(1, a3).offset(-0x18L).setu(v0);
                      v0 = MEMORY.ref(1, t0).offset(-0x6L).get();

                      MEMORY.ref(1, a3).offset(-0xeL).setu(v0);
                      v0 = MEMORY.ref(1, t0).offset(-0x5L).get();

                      MEMORY.ref(1, a3).offset(-0xdL).setu(v0);
                      v0 = MEMORY.ref(1, t0).offset(-0x4L).get();

                      MEMORY.ref(1, a3).offset(-0xcL).setu(v0);
                      v0 = MEMORY.ref(1, t0).offset(-0x2L).get();

                      MEMORY.ref(1, a3).offset(-0x2L).setu(v0);
                      v0 = MEMORY.ref(1, t0).offset(-0x1L).get();

                      MEMORY.ref(1, a3).offset(-0x1L).setu(v0);
                      v0 = MEMORY.ref(1, t0).offset(0x0L).get();

                      MEMORY.ref(1, a3).offset(0x0L).setu(v0);
                      t1 = CPU.MFC2(7);

                      t1 = t1 + s8;
                      v0 = MEMORY.ref(4, s1).offset(0x4L).get();
                      v1 = MEMORY.ref(4, s0).offset(0x4L).get();
                      t1 = (int)t1 >> v0;
                      if((int)t1 < (int)v1) {
                        a0 = t1 << 2;
                      } else {
                        a0 = t1 << 2;
                        t1 = v1;
                        a0 = t1 << 2;
                      }

                      //LAB_800cc3f4
                      a0 = t9 + a0;
                      a3 = a3 + 0x34L;
                      v1 = MEMORY.ref(4, a0).offset(0x0L).get();
                      v0 = 0xc00_0000L;
                      v1 = v1 & t8;
                      v1 = v1 | v0;
                      v0 = t2 & t8;
                      MEMORY.ref(4, t2).offset(0x0L).setu(v1);
                      t2 = t2 + 0x34L;
                      MEMORY.ref(4, a0).offset(0x0L).setu(v0);
                    }
                  }
                }
              }
            }
          }
        }

        //LAB_800cc41c
        t0 = t0 + 0x2cL;
        t4 = t4 + 0x2cL;
      } while(a2 != 0);
    }

    //LAB_800cc428
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t2);
    v0 = t4;
    return v0;
  }

  @Method(0x800cc444L)
  public static long renderPrimitive37(long a0, long a1, long a2) {
    assert false;
    return 0;
  }

  @Method(0x800cc738L) // Pretty sure this is the entry point to WMap
  public static void FUN_800cc738() {
    FUN_800ccb98();
  }

  @Method(0x800cc83cL)
  public static void FUN_800cc83c() {
    if(fileCount_8004ddc8.get() != 0) {
      return;
    }

    if(_800c6690.get() == 0) {
      if((joypadInput_8007a39c.get() & 0x1afL) == 0) {
        final WMapStruct19c0 v1 = _800c66b0.deref();

        if(v1._c5.get() == 0) {
          if(v1._c4.get() == 0) {
            final WMapStruct258 a0 = struct258_800c66a8.deref();

            if(a0._1f8.get() == 0) {
              if(a0._220.get() == 0) {
                if(_800c6698.get() >= 0x3L || _800c669c.get() >= 0x3L) {
                  //LAB_800cc900
                  if((joypadPress_8007a398.get() & 0x10L) != 0) {
                    if(_800c6798.offset(0xfcL).get() != 0x1L) {
                      if(a0._05.get() == 0) {
                        if(_800c6798.offset(0xd8L).get() == 0) {
                          if(a0._250.get() == 0) {
                            scriptStartEffect(0x1L, 0xfL);
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
    struct258_800c66a8.deref()._20.sub((short)0x20);
    if(struct258_800c66a8.deref()._20.get() < 0) {
      struct258_800c66a8.deref()._20.set((short)0);
    }

    //LAB_800cc998
    _800c6690.addu(0x1L);
    if(_800c6690.get() < 0x10L) {
      return;
    }

    pregameLoadingStage_800bb10c.setu(0x4L);
    whichMenu_800bdc38.setu(0x1L);

    final RECT rect = new RECT().set(_800c8700);
    long v0 = addToLinkedListTail(0x1_0000L);
    struct258_800c66a8.deref()._2c.set(v0);
    StoreImage(rect, v0);

    rect.set((short)320, (short)64, (short)0, (short)512);
    v0 = addToLinkedListTail(0x1_0000L);
    struct258_800c66a8.deref()._30.set(v0);
    StoreImage(rect, v0);

    //LAB_800cca5c
  }

  @Method(0x800ccb98L)
  public static void FUN_800ccb98() {
    _800ef000.get((int)pregameLoadingStage_800bb10c.get()).deref().run();
  }

  @Method(0x800ccbe0L)
  public static void FUN_800ccbe0() {
    setWidthAndFlags(0x140L, 0);
    vsyncMode_8007a3b8.setu(0x3L);
    unloadSoundFile((int)0x9L);
    FUN_8001f708(_800bac60.get(), 0);
    pregameLoadingStage_800bb10c.setu(0x1L);
  }

  @Method(0x800ccc30L)
  public static void FUN_800ccc30() {
    if((getLoadedDrgnFiles() & 0x80L) == 0) {
      pregameLoadingStage_800bb10c.setu(0x2L);
    }

    //LAB_800ccc54
  }

  @Method(0x800ccc64L)
  public static void FUN_800ccc64() {
    setProjectionPlaneDistance(1100);

    long v1 = 0x7L;
    long v0 = scriptFlags1_800bad04.getAddress() + 0x1cL;

    //LAB_800ccc84
    do {
      MEMORY.ref(4, v0).setu(0);
      v1 = v1 - 0x1L;
      v0 = v0 - 0x4L;
    } while(v1 >= 0);

    FUN_800ccf04();
    _800c6690.setu(0);
    pregameLoadingStage_800bb10c.setu(0x3L);
  }

  @Method(0x800cccbcL)
  public static void FUN_800cccbc() {
    FUN_800cd030();
    FUN_800cc83c();
  }

  @Method(0x800ccda4L)
  public static void FUN_800ccda4() {
    long v0;
    long v1;

    v1 = 0x800c_0000L; //TODO
    v0 = 0x800c_0000L;
    v0 = v0 + 0x6798L;
    v1 = v1 + -0x5438L;
    MEMORY.ref(2, v1).offset(0x4deL).setu(MEMORY.ref(2, v0).offset(0x12L).get());
    MEMORY.ref(2, v1).offset(0x4d8L).setu(MEMORY.ref(2, v0).offset(0x14L).get());
    MEMORY.ref(2, v1).offset(0x4daL).setu(MEMORY.ref(2, v0).offset(0x16L).get());
    MEMORY.ref(1, v1).offset(0x4dcL).setu(MEMORY.ref(1, v0).offset(0x18L).get());
    MEMORY.ref(1, v1).offset(0x4ddL).setu(MEMORY.ref(1, v0).offset(0x1cL).get());
    FUN_800cd278();
    _80052c6c.setu(0);
    _8004dd24.setu(0x5L);
    pregameLoadingStage_800bb10c.setu(0);
    vsyncMode_8007a3b8.setu(0x2L);
  }

  @Method(0x800ccf04L)
  public static void FUN_800ccf04() {
    struct258_800c66a8.set(MEMORY.ref(4, addToLinkedListTail(0x258L), WMapStruct258::new));
    _800c6698.setu(0x2L);
    _800c669c.setu(0x2L);
    _800c66a0.setu(0x2L);
    _800c66a4.setu(0x2L);
    _800c66b8.setu(0);
    _1f8003e8.setu(0);
    _1f8003ec.setu(0x20L);
    _800c66d8.setu(0);
    _800c66dc.setu(_800bb114);

    FUN_800e3fac(0);
    FUN_800e78c0();
    FUN_800d6880();
    FUN_800d177c();
    FUN_800d8d18();
    FUN_800dfa70();
    FUN_800e4f60();
    FUN_800eb914();
    FUN_800e4e1c();

    if((int)_800c6798.get() < 0x3L) {
      FUN_8001eea8(0x1L);
    } else {
      //LAB_800cd004
      FUN_8001eea8(_800c6798.get() + 0x1L);
    }

    //LAB_800cd020
  }

  @Method(0x800cd030L)
  public static void FUN_800cd030() {
    FUN_800d1d88();
    FUN_800e3ff0();

    switch((int)_800c6698.get()) {
      case 0:
        break;

      case 2:
        if((_800c66b8.get() & 0x2L) != 0 && (_800c66b8.get() & 0x4L) != 0) {
          _800c6698.setu(0x3L);
        }

        //LAB_800cd0d4
        break;

      case 3:
        FUN_800d8efc();
        _800c6698.setu(0x4L);
        break;

      case 4:
        _800c6698.setu(0x5L);
        break;

      case 5:
        FUN_800d9044();
        break;

      case 6:
        _800c6698.setu(0x7L);
        break;

      case 7:
        FUN_800dcde8();
        _800c6698.setu(0);
        break;
    }

    //LAB_800cd148
    switch((int)_800c669c.get()) {
      case 0:
        break;

      case 2:
        if((_800c66b8.get() & 0x2a8L) == 0x2a8L && (_800c66b8.get() & 0x550L) == 0x550L) {
          _800c669c.setu(0x3L);
        }

        //LAB_800cd1dc
        break;

      case 3:
        FUN_800dfbd8();
        _800c669c.setu(0x4L);
        break;

      case 4:
        _800c669c.setu(0x5L);
        break;

      case 5:
        FUN_800e0274();
        break;

      case 6:
        _800c669c.setu(0x7L);
        break;

      case 7:
        FUN_800e05c4();
        _800c669c.setu(0);
        break;
    }

    //LAB_800cd250
    FUN_800e4e84();
    FUN_800d6900();
    FUN_800ed95c();
  }

  @Method(0x800cd278L)
  public static void FUN_800cd278() {
    FUN_800d55fc();
    FUN_800dcde8();
    FUN_800e05c4();
    FUN_800e7888();
    FUN_800eede4();
    removeFromLinkedList(struct258_800c66a8.getPointer());
    _800bdf00.setu(0xdL);

    //LAB_800cd2d4
    for(int i = 0; i < 8; i++) {
      //LAB_800cd2f0
      FUN_800257e0(i);
      FUN_8002a3ec(i, 0);
    }

    //LAB_800cd32c
    unloadSoundFile(9);
    vsyncMode_8007a3b8.setu(0x2L);
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
  public static WMapRender40 FUN_800cd3c8(long a0, COLOUR a1, COLOUR a2, COLOUR a3, COLOUR a4, long a5, long a6, long a7, long a8, long a9, final boolean transparency, long a11, long a12, long a13, long a14, long a15, long a16) {
    long sp2c = 0;
    long sp30 = 0;
    long sp38;
    long sp3c;
    long sp48;
    long sp4c;
    long sp54_s;
    long sp56_s;

    final WMapRender40 sp34 = MEMORY.ref(4, addToLinkedListTail(0x40L), WMapRender40::new);

    sp34._28.set(a7);
    sp34._2c.set(a8);
    sp34._30.set(a7 * a8);
    sp34._34.set((short)a0);
    sp34._38.set(0);
    sp34._3a.set(0);
    sp34.transparency_3c.set(transparency);
    sp34._3d.set((int)a12);
    sp34._3e.set((int)a13);

    if(a9 == 0) {
      sp34._00.set(MEMORY.ref(4, addToLinkedListTail(0x10L), UnboundedArrayRef.of(0x10, WMapRender10::new)));
    } else {
      //LAB_800cd4fc
      sp34._00.set(MEMORY.ref(4, addToLinkedListTail(sp34._30.get() * 0x10L), UnboundedArrayRef.of(0x10, WMapRender10::new)));
    }

    //LAB_800cd534
    FUN_800ce0bc(sp34, a9, a1, a2, a3, a4, a5);

    //LAB_800cd578
    for(int i = 0; i < 2; i++) {
      //LAB_800cd594
      sp34._04.get(i).set(0);
      sp34._0c.get(i).set(0);
      sp34._14.get(i).clear();
    }

    //LAB_800cd600
    sp34._1c.set(MEMORY.ref(4, addToLinkedListTail(sp34._30.get() * 0x8L), UnboundedArrayRef.of(0x8, WMapRender08_2::new)));

    long sp58_s = MEMORY.ref(2, a6).offset(0x4L).getSigned() / (int)a7;
    long sp5a_s = MEMORY.ref(2, a6).offset(0x6L).getSigned() / (int)a8;

    if(a12 != 0x9L) {
      if(a12 == 0xcL) {
        //LAB_800cdbec
        //LAB_800cdbf0
        for(int i = 0; i < 2; i++) {
          //LAB_800cdc0c
          sp34._14.get(i).set(MEMORY.ref(4, addToLinkedListTail(sp34._30.get() * 0x28L), UnboundedArrayRef.of(0x28, WMapRender28::new, () -> (int)sp34._30.get())));
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
          sp34._04.get(i).set(addToLinkedListTail(sp34._30.get() * 0x8L));
        }
      }

      //LAB_800cd748
      //LAB_800cd74c
      for(int i = 0; i < 2; i++) {
        //LAB_800cd768
        sp34._0c.get(i).set(addToLinkedListTail(sp34._30.get() * 0x24L));
      }

      //LAB_800cd7d0
      sp48 = sp34._04.get(0).get();
      sp4c = sp34._04.get(1).get();
      sp38 = sp34._0c.get(0).get();
      sp3c = sp34._0c.get(1).get();

      sp2c = 0;
      sp30 = 0;

      //LAB_800cd82c
      for(int i = 0; i < sp34._30.get(); i++) {
        final long sp5c = sp34._1c.deref().get(i).getAddress(); //TODO

        //LAB_800cd850
        if(transparency) {
          MEMORY.ref(1, sp48).offset(0x3L).setu(0x1L);
          MEMORY.ref(4, sp48).offset(0x4L).setu(0xe100_0000L | GetTPage(0, a11, 0, 0) & 0x9ffL);

          MEMORY.ref(1, sp4c).offset(0x3L).setu(0x1L);
          MEMORY.ref(4, sp4c).offset(0x4L).setu(0xe100_0000L | GetTPage(0, a11, 0, 0) & 0x9ffL);
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
  public static void FUN_800ce170(UnboundedArrayRef<WMapRender28> a0, long a1, long a2, long a3, long a4, long a5, long a6, long a7) {
    assert false;
  }

  @Method(0x800ce4dcL)
  public static void FUN_800ce4dc(final WMapRender40 a0) {
    long sp18;
    long sp14;

    FUN_800cea1c(a0);

    if(a0._3d.get() == 0x9L) {
      //LAB_800ce538
      sp18 = a0._0c.get((int)doubleBufferFrame_800bb108.get()).get();

      if(a0.transparency_3c.get()) {
        sp14 = a0._04.get((int)doubleBufferFrame_800bb108.get()).get();
      } else {
        sp14 = 0; // Only used if transparency is set, just needs to be initialized
      }

      //LAB_800ce5a0
      //LAB_800ce5a4
      for(int i = 0; i < a0._30.get(); i++) {
        final WMapRender08_2 sp20 = a0._1c.deref().get(i);

        //LAB_800ce5c8
        MEMORY.ref(2, sp18).offset(0x08L).setu(sp20._00.get() + a0._38.get());
        MEMORY.ref(2, sp18).offset(0x0aL).setu(sp20._02.get() + a0._3a.get());
        MEMORY.ref(2, sp18).offset(0x10L).setu(sp20._00.get() + a0._38.get() + sp20._04.get());
        MEMORY.ref(2, sp18).offset(0x12L).setu(sp20._02.get() + a0._3a.get());
        MEMORY.ref(2, sp18).offset(0x18L).setu(sp20._00.get() + a0._38.get());
        MEMORY.ref(2, sp18).offset(0x1aL).setu(sp20._02.get() + a0._3a.get() + sp20._06.get());
        MEMORY.ref(2, sp18).offset(0x20L).setu(sp20._00.get() + a0._38.get() + sp20._04.get());
        MEMORY.ref(2, sp18).offset(0x22L).setu(sp20._02.get() + a0._3a.get() + sp20._06.get());

        insertElementIntoLinkedList(tags_1f8003d0.deref().get(a0._3e.get()).getAddress(), sp18);

        if(a0.transparency_3c.get()) {
          insertElementIntoLinkedList(tags_1f8003d0.deref().get(a0._3e.get()).getAddress(), sp14);
        }

        //LAB_800ce7a0
        sp18 += 0x24L;
        sp14 += 0x8L;
      }
    } else if(a0._3d.get() == 0xcL) {
      assert false : "Bugged; sp18 was uninitialized";
      sp18 = 0;

      //LAB_800ce7f0
      //LAB_800ce81c
      for(int i = 0; i < a0._30.get(); i++) {
        final WMapRender08_2 sp20 = a0._1c.deref().get(i);
        final WMapRender28 sp1c = a0._14.get((int)doubleBufferFrame_800bb108.get()).deref().get(i);

        //LAB_800ce840
        MEMORY.ref(2, sp18).offset(0x08L).setu(sp20._00.get() + a0._38.get());
        MEMORY.ref(2, sp18).offset(0x0aL).setu(sp20._02.get() + a0._3a.get());
        MEMORY.ref(2, sp18).offset(0x10L).setu(sp20._00.get() + a0._38.get() + sp20._04.get());
        MEMORY.ref(2, sp18).offset(0x12L).setu(sp20._02.get() + a0._3a.get());
        MEMORY.ref(2, sp18).offset(0x18L).setu(sp20._00.get() + a0._38.get());
        MEMORY.ref(2, sp18).offset(0x1aL).setu(sp20._02.get() + a0._3a.get() + sp20._06.get());
        MEMORY.ref(2, sp18).offset(0x20L).setu(sp20._00.get() + a0._38.get() + sp20._04.get());
        MEMORY.ref(2, sp18).offset(0x22L).setu(sp20._02.get() + a0._3a.get() + sp20._06.get());
        insertElementIntoLinkedList(tags_1f8003d0.deref().get(a0._3e.get()).getAddress(), sp1c.getAddress());
      }

      //LAB_800cea00
    }

    //LAB_800ce7e8
    //LAB_800cea08
  }

  @Method(0x800cea1cL)
  public static void FUN_800cea1c(WMapRender40 a0) {
    long v0;
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
      long sp4 = a0._0c.get((int)doubleBufferFrame_800bb108.get()).get();
      long sp8 = a0._0c.get((int)(doubleBufferFrame_800bb108.get() ^ 0x1L)).get();

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
        final WMapRender28 spc = a0._14.get((int)doubleBufferFrame_800bb108.get()).deref().get(i);
        final WMapRender28 sp10 = a0._14.get((int)(doubleBufferFrame_800bb108.get() ^ 0x1L)).deref().get(i);
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
      if(a0._0c.get(i).get() != 0) {
        removeFromLinkedList(a0._0c.get(i).get());
      }

      //LAB_800d165c
      if(!a0._14.get(i).isNull()) {
        removeFromLinkedList(a0._14.get(i).getPointer());
      }

      //LAB_800d16b0
      if(a0._04.get(i).get() != 0) {
        removeFromLinkedList(a0._04.get(i).get());
      }

      //LAB_800d1704
    }

    //LAB_800d171c
    removeFromLinkedList(a0._1c.getPointer());
    removeFromLinkedList(a0._00.getPointer());
    removeFromLinkedList(a0.getAddress());
  }

  @Method(0x800d177cL)
  public static void FUN_800d177c() {
    _800c66b0.set(MEMORY.ref(4, addToLinkedListTail(0x19c0L), WMapStruct19c0::new));

    GsInitCoordinate2(null, _800c66b0.deref().coord2_20);

    _800c66b0.deref().coord2_20.coord.transfer.setX(0);
    _800c66b0.deref().coord2_20.coord.transfer.setY(0);
    _800c66b0.deref().coord2_20.coord.transfer.setY(0);
    _800c66b0.deref().rotation_70.setX((short)0);
    _800c66b0.deref().rotation_70.setY((short)0);
    _800c66b0.deref().rotation_70.setZ((short)0);
    _800c66b0.deref()._00._00.set(0);
    _800c66b0.deref()._00._04.set(-300);
    _800c66b0.deref()._00._08.set(-900);
    _800c66b0.deref()._00._0c.set(0);
    _800c66b0.deref()._00._10.set(300);
    _800c66b0.deref()._00._14.set(900);
    _800c66b0.deref()._00._18.set(0);
    _800c66b0.deref()._00._1c.set(_800c66b0.deref().coord2_20);

    FUN_800d1d28();
    FUN_800d1914();

    _800c66b0.deref()._114.set(0);
    _800c66b0.deref()._118.set((short)1100);
    _800c66b0.deref()._11a.set(0);
  }

  @Method(0x800d1914L)
  public static void FUN_800d1914() {
    long at;
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long hi;
    at = 0x8008_0000L;
    MEMORY.ref(4, at).offset(-0x5c58L).setu(0);
    at = 0x800c_0000L;
    MEMORY.ref(4, at).offset(-0x4efcL).setu(0);
    at = 0x800c_0000L;
    MEMORY.ref(4, at).offset(-0x5440L).setu(0);
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
    v1 = -0x1L;
    MEMORY.ref(4, v0).offset(0x154L).setu(v1);
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
    v1 = -0x1L;
    MEMORY.ref(4, v0).offset(0x1974L).setu(v1);
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();

    MEMORY.ref(4, v0).offset(0x1970L).setu(0);
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();

    MEMORY.ref(4, v0).offset(0x196cL).setu(0);

    FUN_800d1db8();
    long sp10 = 0;

    //LAB_800d1984
    do {
      v0 = sp10;

      if((int)v0 >= 0x3L) {
        break;
      }

      //LAB_800d19a0
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
      v1 = sp10;

      a0 = v1;
      v1 = a0 << 4;
      v0 = v0 + v1;
      v1 = 0x20L;
      MEMORY.ref(1, v0).offset(0x128L).setu(v1);
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
      v1 = sp10;

      a0 = v1;
      v1 = a0 << 4;
      v0 = v0 + v1;
      v1 = 0x20L;
      MEMORY.ref(1, v0).offset(0x129L).setu(v1);
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
      v1 = sp10;

      a0 = v1;
      v1 = a0 << 4;
      v0 = v0 + v1;
      v1 = 0x20L;
      MEMORY.ref(1, v0).offset(0x12aL).setu(v1);
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
      v1 = sp10;

      a0 = v1;
      v1 = a0 << 1;
      v0 = v0 + v1;
      v1 = 0xfL;
      MEMORY.ref(2, v0).offset(0x19a8L).setu(v1);
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
      v1 = sp10;

      a0 = v1;
      v1 = a0 << 1;
      v0 = v0 + v1;
      v1 = 0x13bL;
      MEMORY.ref(2, v0).offset(0x19aeL).setu(v1);
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
      v1 = sp10;

      a0 = v1;
      v1 = a0 << 1;
      v0 = v0 + v1;
      v1 = MEMORY.ref(2, v0).offset(0x19a8L).getSigned();

      a0 = v1;
      v0 = a0 << 12;
      v1 = 0xb60b_0000L;
      v1 = v1 | 0x60b7L;
      hi = ((long)(int)v0 * (int)v1) >>> 32;
      a3 = hi;
      v1 = a3 + v0;
      a0 = (int)v1 >> 8;
      v1 = (int)v0 >> 31;
      v0 = a0 - v1;
      a0 = v0;

      v0 = rcos(a0);
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();
      a0 = sp10;

      a1 = a0;
      a0 = a1 << 4;
      v1 = v1 + a0;
      a0 = v0 << 12;
      v0 = (int)a0 >> 12;
      MEMORY.ref(4, v1).offset(0x124L).setu(v0);
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
      v1 = sp10;

      a0 = v1;
      v1 = a0 << 1;
      v0 = v0 + v1;
      v1 = MEMORY.ref(2, v0).offset(0x19a8L).getSigned();

      a0 = v1;
      v0 = a0 << 12;
      v1 = 0xb60b_60b7L;
      a3 = ((long)(int)v0 * (int)v1) >>> 32; //TODO
      v1 = a3 + v0;
      a0 = (int)v1 >> 8;
      v1 = (int)v0 >> 31;
      v0 = a0 - v1;
      a0 = v0;

      v0 = rsin(a0);
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();
      a0 = sp10;

      a1 = a0;
      a0 = a1 << 4;
      v1 = v1 + a0;
      a0 = v0 << 12;
      v0 = (int)a0 >> 12;
      MEMORY.ref(4, v1).offset(0x11cL).setu(v0);
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
      v1 = sp10;

      a0 = v1;
      v1 = a0 << 1;
      v0 = v0 + v1;
      v1 = MEMORY.ref(2, v0).offset(0x19aeL).getSigned();

      a0 = v1;
      v0 = a0 << 12;
      v1 = 0xb60b_60b7L;
      a3 = ((long)(int)v0 * (int)v1) >>> 32; //TODO
      v1 = a3 + v0;
      a0 = (int)v1 >> 8;
      v1 = (int)v0 >> 31;
      v0 = a0 - v1;
      a0 = v0;

      v0 = rcos(a0);
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();
      a0 = sp10;

      a1 = a0;
      a0 = a1 << 4;
      v1 = v1 + a0;
      a0 = v0 << 12;
      v0 = (int)a0 >> 12;
      MEMORY.ref(4, v1).offset(0x120L).setu(v0);
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
      v1 = sp10;

      a0 = v1;
      v1 = a0 << 4;
      v0 = v0 + v1;
      MEMORY.ref(4, v0).offset(0x124L).setu(0);
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
      v1 = sp10;

      a0 = v1;
      v1 = a0 << 4;
      v0 = v0 + v1;
      v1 = 0x3e8L;
      MEMORY.ref(4, v0).offset(0x11cL).setu(v1);
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
      v1 = sp10;

      a0 = v1;
      v1 = a0 << 4;
      v0 = v0 + v1;
      v1 = 0x64L;
      MEMORY.ref(4, v0).offset(0x120L).setu(v1);
      v0 = sp10;

      v1 = v0;
      v0 = v1 << 4;
      v1 = v0 + 0x11cL;
      a0 = 0x800c_0000L;
      a0 = MEMORY.ref(4, a0).offset(0x66b0L).get();

      v0 = v1 + a0;
      a0 = sp10;
      a1 = v0;

      GsSetFlatLight(a0, MEMORY.ref(4, a1, GsF_LIGHT::new)); //TODO
      v0 = sp10;

      v1 = v0 + 0x1L;
      sp10 = v1;
    } while(true);

    //LAB_800d1c88
    setLightMode(0);
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();
    a0 = 0x800c_0000L;
    a0 = MEMORY.ref(4, a0).offset(0x66b0L).get();
    a1 = 0x600L;
    MEMORY.ref(2, a0).offset(0x150L).setu(a1);
    a0 = 0x600L;
    MEMORY.ref(2, v1).offset(0x14eL).setu(a0);
    v1 = 0x600L;
    MEMORY.ref(2, v0).offset(0x14cL).setu(v1);
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

    v0 = MEMORY.ref(2, v1).offset(0x14cL).getSigned();
    a0 = 0x800c_0000L;
    a0 = MEMORY.ref(4, a0).offset(0x66b0L).get();

    v1 = MEMORY.ref(2, a0).offset(0x14eL).getSigned();
    a0 = 0x800c_0000L;
    a0 = MEMORY.ref(4, a0).offset(0x66b0L).get();

    a2 = MEMORY.ref(2, a0).offset(0x150L).getSigned();
    a0 = v0;
    a1 = v1;

    GsSetAmbient(a0, a1, a2);
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();

    MEMORY.ref(4, v0).offset(0x88L).setu(0);
  }

  @Method(0x800d1d28L)
  public static void FUN_800d1d28() {
    _800c66b0.deref()._80.set(0);
    _800c66b0.deref()._7c.set((short)0);
    _800c66b0.deref()._c5.set(0);
    _800c66b0.deref()._c4.set(0);

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
    final WMapStruct258 v0 = struct258_800c66a8.deref();
    final long x = v0.coord2_34.coord.transfer.getX();
    final long y = v0.coord2_34.coord.transfer.getY();
    final long z = v0.coord2_34.coord.transfer.getZ();

    //LAB_800d1e14
    int sp14 = 0;
    for(int i = 0; i < _800c67a0.get(); i++) {
      //LAB_800d1e38
      if(!_800f0234.get(_800f0e34.get(i)._02.get()).text_00.isNull()) {
        //LAB_800d1e90
        if(FUN_800eb09c(i, 1, _800c66b0.deref()._154.get(sp14).vec_08) == 0) {
          //LAB_800d1ee0
          final long dx = x - _800c66b0.deref()._154.get(sp14).vec_08.getX();
          final long dy = y - _800c66b0.deref()._154.get(sp14).vec_08.getY();
          final long dz = z - _800c66b0.deref()._154.get(sp14).vec_08.getZ();

          _800c66b0.deref()._154.get(sp14).index_00.set(i);
          _800c66b0.deref()._154.get(sp14).vecLength_04.set(SquareRoot0(dx * dx + dy * dy + dz * dz));

          sp14++;
        }
      }

      //LAB_800d2070
    }

    //LAB_800d2088
    _800c66b0.deref()._154.get(sp14).index_00.set(-1);
    FUN_80013434(_800c66b0.deref()._154.getAddress(), sp14, 0x18L, getMethodAddress(WMap.class, "FUN_800d20f4", long.class, long.class));
  }

  @Method(0x800d20f4L)
  public static long FUN_800d20f4(final long a0, final long a1) {
    //LAB_800d2120
    return MEMORY.ref(4, a0).offset(0x4L).get() - MEMORY.ref(4, a1).offset(0x4L).get();
  }

  @Method(0x800d219cL)
  public static void updateLights() {
    long v0;
    long v1;

    if(struct258_800c66a8.deref()._1f8.get() == 0) {
      return;
    }

    //LAB_800d21cc
    if(struct258_800c66a8.deref()._1f8.get() == 0x2L || struct258_800c66a8.deref()._1f8.get() == 0x3L || struct258_800c66a8.deref()._1f8.get() == 0x4L) {
      //LAB_800d2228
      v0 = _800c66b0.deref()._88.get();

      if(v0 == 0 || v0 == 1) {
        if(v0 == 0) {
          //LAB_800d2258
          //LAB_800d225c
          for(int i = 0; i < 0x3L; i++) {
            //LAB_800d2278
            final WMapStruct19c0 struct = _800c66b0.deref();
            struct.colour_8c.get(i).setR(struct.lights_11c.get(i).r_0c.get());
            struct.colour_8c.get(i).setG(struct.lights_11c.get(i).g_0d.get());
            struct.colour_8c.get(i).setB(struct.lights_11c.get(i).b_0e.get());
          }

          //LAB_800d235c
          _800c66b0.deref()._84.set(0x100L);
          _800c66b0.deref()._88.set(0x1L);
        }

        //LAB_800d237c
        _800c66b0.deref()._84.sub(0x24L);

        if((int)_800c66b0.deref()._84.get() < 0x40L) {
          _800c66b0.deref()._84.set(0x20L);
          _800c66b0.deref()._88.set(0x2L);
        }

        //LAB_800d23e0
        //LAB_800d23e4
        for(int i = 0; i < 3; i++) {
          final GsF_LIGHT light = _800c66b0.deref().lights_11c.get(i);

          //LAB_800d2400
          v1 = _800c66b0.deref().colour_8c.get(i).getR() * _800c66b0.deref()._84.get();
          if((int)v1 < 0) {
            v1 = v1 + 0xffL;
          }

          //LAB_800d2464
          light.r_0c.set((int)v1 / 0x100);

          v1 = _800c66b0.deref().colour_8c.get(i).getG() * _800c66b0.deref()._84.get();
          if((int)v1 < 0) {
            v1 = v1 + 0xffL;
          }

          //LAB_800d24d0
          light.g_0d.set((int)v1 / 0x100);

          v1 = _800c66b0.deref().colour_8c.get(i).getB() * _800c66b0.deref()._84.get();
          if((int)v1 < 0) {
            v1 = v1 + 0xffL;
          }

          //LAB_800d253c
          light.b_0e.set((int)v1 / 0x100);

          GsSetFlatLight(i, _800c66b0.deref().lights_11c.get(i));
        }
      }
    }

    //LAB_800d2590
    //LAB_800d2598
    if(struct258_800c66a8.deref()._1f8.get() != 0x5L && struct258_800c66a8.deref()._1f8.get() != 0x6L) {
      return;
    }

    //LAB_800d25d8
    v0 = _800c66b0.deref()._88.get();
    if(v0 == 0x2L) {
      //LAB_800d2608
      _800c66b0.deref()._84.set(0x40L);
      _800c66b0.deref()._88.set(0x3L);
    } else if(v0 != 0x3L) {
      return;
    }

    //LAB_800d2628
    _800c66b0.deref()._84.add(0x24L);

    if((int)_800c66b0.deref()._84.get() >= 0x100L) {
      _800c66b0.deref()._84.set(0xffL);
      _800c66b0.deref()._88.set(0);
    }

    //LAB_800d268c
    //LAB_800d2690
    for(int i = 0; i < 3; i++) {
      final GsF_LIGHT light = _800c66b0.deref().lights_11c.get(i);

      //LAB_800d26ac
      v1 = _800c66b0.deref().colour_8c.get(i).getR() * _800c66b0.deref()._84.get();
      if((int)v1 < 0) {
        v1 = v1 + 0xffL;
      }

      //LAB_800d2710
      light.r_0c.set((int)v1 / 0x100);

      v1 = _800c66b0.deref().colour_8c.get(i).getG() * _800c66b0.deref()._84.get();
      if((int)v1 < 0) {
        v1 = v1 + 0xffL;
      }

      //LAB_800d277c
      light.g_0d.set((int)v1 / 0x100);

      v1 = _800c66b0.deref().colour_8c.get(i).getB() * _800c66b0.deref()._84.get();
      if((int)v1 < 0) {
        v1 = v1 + 0xffL;
      }

      //LAB_800d27e8
      light.b_0e.set((int)v1 / 0x100);

      GsSetFlatLight(i, _800c66b0.deref().lights_11c.get(i));
    }

    //LAB_800d283c
    //LAB_800d2844
  }

  @Method(0x800d2d90L)
  public static void FUN_800d2d90() {
    FUN_800d5288();

    final WMapStruct19c0 struct = _800c66b0.deref();

    rotateCoord2(struct.rotation_70, struct.coord2_20);

    if(struct._c5.get() == 0) {
      if(struct._c4.get() == 0) {
        if(struct258_800c66a8.deref()._1f8.get() == 0) {
          if(struct258_800c66a8.deref()._220.get() == 0) {
            struct.coord2_20.coord.transfer.setX(struct258_800c66a8.deref().coord2_34.coord.transfer.getX());
            struct.coord2_20.coord.transfer.setY(struct258_800c66a8.deref().coord2_34.coord.transfer.getY());
            struct.coord2_20.coord.transfer.setZ(struct258_800c66a8.deref().coord2_34.coord.transfer.getZ());
          }
        }
      }
    }

    //LAB_800d2ec4
    FUN_8003dfc0(struct._00);
    FUN_800d2fa8();
    FUN_800d3fc8();

    struct.rotation_70.x.and(0xfff);
    struct.rotation_70.y.and(0xfff);
    struct.rotation_70.z.and(0xfff);
    struct._7a.and(0xfff);
  }

  @Method(0x800d2fa8L)
  public static void FUN_800d2fa8() {
    if(struct258_800c66a8.deref()._250.get() == 0x1L) {
      return;
    }

    //LAB_800d2fd4
    if(struct258_800c66a8.deref()._250.get() == 0x2L && struct258_800c66a8.deref()._05.get() == 0) {
      return;
    }

    //LAB_800d3014
    if(_800c66b0.deref()._7c.get() == 0) {
      _800c66b0.deref()._80.set(0);
    }

    //LAB_800d3040
    if(_800c66b0.deref()._110.get() == 0) {
      if(struct258_800c66a8.deref()._1f8.get() == 0) {
        if(_800c66b0.deref()._c4.get() == 0) {
          if(_800c6798.get() != 0x7L) {
            final long v0 = _800c66b0.deref()._80.get();
            if(v0 == 0) {
              //LAB_800d30d8
              if((joypadPress_8007a398.get() & 0x8L) != 0) {
                FUN_800d4ed8(0x1L);
                _800c66b0.deref()._80.set(1);
              }

              //LAB_800d310c
              if((joypadPress_8007a398.get() & 0x4L) != 0) {
                FUN_800d4ed8(-0x1L);
                _800c66b0.deref()._80.set(1);
              }

              //LAB_800d3140
            } else if(v0 == 0x1L) {
              //LAB_800d3148
              _800c66b0.deref().rotation_70.y.add(_800c66b0.deref()._7c);
              _800c66b0.deref()._7e.incr();

              if(_800c66b0.deref()._7e.get() >= 0x6L) {
                _800c66b0.deref()._80.set(0);
                _800c66b0.deref().rotation_70.setY((short)_800c66b0.deref()._7a.get());
              }
            }
          }
        }
      }
    }

    //LAB_800d31e8
    FUN_800d35fc();

    final long v0 = _800c66b0.deref()._110.get();
    if(v0 == 0x1L) {
      //LAB_800d3250
      FUN_800d5018();
      _800c66b0.deref()._110.set(2);
    } else if(v0 == 0x3L) {
      //LAB_800d3434
      _800c66b0.deref()._00._04.set(_800c66b0.deref()._c8._04.get() + _800c66b0.deref()._ec.get() * _800c66b0.deref()._10e.get());
      _800c66b0.deref()._00._08.set(_800c66b0.deref()._c8._08.get() + _800c66b0.deref()._f0.get() * _800c66b0.deref()._10e.get());
      _800c66b0.deref()._00._10.set(_800c66b0.deref()._c8._10.get() + _800c66b0.deref()._f8.get() * _800c66b0.deref()._10e.get());
      _800c66b0.deref()._00._14.set(_800c66b0.deref()._c8._14.get() + _800c66b0.deref()._fc.get() * _800c66b0.deref()._10e.get());
      _800c66b0.deref().rotation_70.setY((short)(_800c66b0.deref()._10a.get() + _800c66b0.deref()._10c.get() * _800c66b0.deref()._10e.get()));

      if(_800c66b0.deref()._10e.get() > 0) {
        _800c66b0.deref()._10e.decr();
      } else {
        _800c66b0.deref()._110.set(0);
      }

      return;
    } else if((int)v0 < 0x2L) {
      //LAB_800d3248
      return;
    }

    // if == 1 or 2

    //LAB_800d3228
    //LAB_800d3268
    _800c66b0.deref()._00._04.set(_800c66b0.deref()._c8._04.get() + _800c66b0.deref()._ec.get() * _800c66b0.deref()._10e.get());
    _800c66b0.deref()._00._08.set(_800c66b0.deref()._c8._08.get() + _800c66b0.deref()._f0.get() * _800c66b0.deref()._10e.get());
    _800c66b0.deref()._00._10.set(_800c66b0.deref()._c8._10.get() + _800c66b0.deref()._f8.get() * _800c66b0.deref()._10e.get());
    _800c66b0.deref()._00._14.set(_800c66b0.deref()._c8._14.get() + _800c66b0.deref()._fc.get() * _800c66b0.deref()._10e.get());
    _800c66b0.deref().rotation_70.setY((short)(_800c66b0.deref()._10a.get() + _800c66b0.deref()._10c.get() * _800c66b0.deref()._10e.get()));

    _800c66b0.deref()._10e.incr();
    if((short)_800c66b0.deref()._10e.get() >= 0x10L) {
      _800c66b0.deref()._10e.set(0x10);
      _800c66b0.deref().rotation_70.setY(_800c66b0.deref()._108.get());
    }

    //LAB_800d342c
    //LAB_800d35e4
    //LAB_800d35ec
  }

  @Method(0x800d35fcL)
  public static void FUN_800d35fc() {
    final long v0 = _800c66b0.deref()._c5.get();
    if(v0 == 0x1L) {
      //LAB_800d38dc
      _800c66b0.deref()._00._04.sub(1450);
      _800c66b0.deref()._00._10.add(1450);
      _800c66b0.deref().rotation_70.setY((short)(_800c66b0.deref()._9a.get() + _800c66b0.deref()._9c.get() * _800c66b0.deref()._a0.get()));
      _800c66b0.deref().vec_b4.x.add(_800c66b0.deref().vec_a4.x);
      _800c66b0.deref().vec_b4.y.add(_800c66b0.deref().vec_a4.y);
      _800c66b0.deref().vec_b4.z.add(_800c66b0.deref().vec_a4.z);
      _800c66b0.deref().coord2_20.coord.transfer.setX(struct258_800c66a8.deref().coord2_34.coord.transfer.getX() - _800c66b0.deref().vec_b4.getX() / 0x100);
      _800c66b0.deref().coord2_20.coord.transfer.setY(struct258_800c66a8.deref().coord2_34.coord.transfer.getY() - _800c66b0.deref().vec_b4.getY() / 0x100);
      _800c66b0.deref().coord2_20.coord.transfer.setZ(struct258_800c66a8.deref().coord2_34.coord.transfer.getZ() - _800c66b0.deref().vec_b4.getZ() / 0x100);
      _800c66b0.deref()._a0.incr();

      if((short)_800c66b0.deref()._a0.get() >= 0x6L) {
        _800c66b0.deref()._00._04.set(_800c66b0.deref()._9e.get());
        _800c66b0.deref()._00._10.set(-_800c66b0.deref()._9e.get());
        _800c66b0.deref().rotation_70.setY((short)_800c66b0.deref()._98.get());
        _800c66b0.deref().coord2_20.coord.transfer.setX(0);
        _800c66b0.deref().coord2_20.coord.transfer.setY(0);
        _800c66b0.deref().coord2_20.coord.transfer.setZ(0);
        _800c66b0.deref()._c5.set(0);
        struct258_800c66a8.deref()._1f8.set(1);
      }
    } else if((int)v0 < 0x2L) {
      if(v0 == 0) {
        //LAB_800d3654
        //LAB_800d3670
        //LAB_800d368c
        if(_800c6798.get() != 0x7L && _800c6870.get() == 0 && _800c6690.get() == 0) {
          //LAB_800d36a8
          if(_800c6894.get() != 0x1L) {
            if(_800c66b0.deref()._80.get() == 0) {
              if(struct258_800c66a8.deref()._05.get() == 0) {
                if(_800c66b0.deref()._110.get() == 0) {
                  if((joypadPress_8007a398.get() & 0x2L) != 0) {
                    if(struct258_800c66a8.deref()._1f8.get() == 0) {
                      playSound(0, 4, 0, 0, (short)0, (short)0);
                      _800c66b0.deref()._9e.set((short)-9000);
                      _800c66b0.deref()._c5.set(1);
                      _800c66b0.deref()._11a.set(1);
                      FUN_800d4bc8(0);
                      _800c6868.setu(0x1L);
                      _800c66b0.deref()._c4.set(1);
                    }
                  }

                  //LAB_800d37bc
                  if((joypadPress_8007a398.get() & 0x1L) != 0) {
                    if(struct258_800c66a8.deref()._1f8.get() == 0x1L || struct258_800c66a8.deref()._1f8.get() == 0x6L) {
                      //LAB_800d3814
                      FUN_8002a3ec(7, 0);
                      playSound(0, 4, 0, 0, (short)0, (short)0);
                      _800c66b0.deref()._9e.set((short)-300);
                      _800c66b0.deref()._c5.set(2);
                      FUN_800d4bc8(0x1L);
                      _800c66b0.deref()._c4.set(0);
                      struct258_800c66a8.deref()._1f8.set(0);
                    } else {
                      //LAB_800d3898
                      if(struct258_800c66a8.deref()._1f8.get() == 0) {
                        playSound(0, 0x28, 0, 0, (short)0, (short)0);
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
      //LAB_800d3640
    } else if(v0 == 0x2L) {
      //LAB_800d3bd8
      if(struct258_800c66a8.deref()._05.get() == 0) {
        _800c66b0.deref()._00._04.add(1450);
        _800c66b0.deref()._00._10.sub(1450);
      } else {
        //LAB_800d3c44
        _800c66b0.deref()._00._04.add(290);
        _800c66b0.deref()._00._10.sub(290);
      }

      //LAB_800d3c8c
      _800c66b0.deref().rotation_70.setY((short)(_800c66b0.deref()._9a.get() + _800c66b0.deref()._9c.get() * _800c66b0.deref()._a0.get()));
      _800c66b0.deref().vec_b4.add(_800c66b0.deref().vec_a4);
      _800c66b0.deref().coord2_20.coord.transfer.setX(_800c66b0.deref().vec_b4.getX() >> 8);
      _800c66b0.deref().coord2_20.coord.transfer.setY(_800c66b0.deref().vec_b4.getY() >> 8);
      _800c66b0.deref().coord2_20.coord.transfer.setZ(_800c66b0.deref().vec_b4.getZ() >> 8);
      _800c66b0.deref()._a0.incr();

      long sp18 = 0;
      if(struct258_800c66a8.deref()._05.get() == 0) {
        if((short)_800c66b0.deref()._a0.get() >= 0x6L) {
          sp18 = 0x1L;
        }

        //LAB_800d3e78
        //LAB_800d3e80
      } else if((short)_800c66b0.deref()._a0.get() >= 0x1eL) {
        sp18 = 0x1L;
      }

      //LAB_800d3ea8
      if(sp18 != 0) {
        _800c66b0.deref()._00._04.set(_800c66b0.deref()._9e.get());
        _800c66b0.deref()._00._04.set(-_800c66b0.deref()._9e.get());
        _800c66b0.deref().rotation_70.setY((short)_800c66b0.deref()._98.get());
        _800c66b0.deref().coord2_20.coord.transfer.set(struct258_800c66a8.deref().coord2_34.coord.transfer);
        _800c66b0.deref()._c5.set(0);
        _800c6868.setu(0);
        struct258_800c66a8.deref()._1f8.set(0);
      }
    }

    //LAB_800d38d4
    //LAB_800d3bd0
    //LAB_800d3fa4
    //LAB_800d3fac
    FUN_800d4058();
  }

  @Method(0x800d3fc8L)
  public static void FUN_800d3fc8() {
    if(struct258_800c66a8.deref()._250.get() == 0x1L) {
      //LAB_800d401c
      _800c66b0.deref().rotation_70.y.add((short)8);
    }
  }

  @Method(0x800d4058L)
  public static void FUN_800d4058() {
    long at;
    long v0;
    long v1;
    long a0;
    long a1;
    long t0;
    long lo;
    long sp10;
    long sp14;
    long sp18;
    long sp1c;
    long sp20;
    long sp24;
    long sp28;
    long sp2c;
    long sp30;
    long sp34;
    long sp6c;
    long sp70;
    long sp72;
    long sp74;
    long sp78;
    long sp7c;
    long sp80;
    long sp84;

    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();

    v1 = MEMORY.ref(1, v0).offset(0xc4L).getSigned();

    //LAB_800d4088
    if(v1 == 0 || _800c66b0.deref()._c5.get() != 0) {
      //LAB_800d41f0
      return;
    }

    //LAB_800d40ac
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

    v0 = MEMORY.ref(1, v1).offset(0x1f8L).get();

    if(v0 == 0x1L) {
      //LAB_800d4108
      at = 0x800d_0000L;
      MEMORY.ref(4, at).offset(-0x7910L).setu(0);
      sp1c = 0x10L;
      sp18 = 0x20L;
    } else if(v0 == 0x4L) {
      //LAB_800d4170
      v1 = 0x8008_0000L;
      v1 = MEMORY.ref(4, v1).offset(-0x5c68L).get();

      v0 = v1 & 0x1L;
      if(v0 != 0) {
        FUN_8002a3ec(7, 0);
      }

      //LAB_800d4198
      sp1c = 0x8L;
      sp18 = 0x30L;

      //LAB_800d40e8
    } else if(v0 == 0x5L) {
      //LAB_800d41b0
      FUN_8002a3ec(7, 0);
      v1 = 0x8008_0000L;
      v1 = MEMORY.ref(4, v1).offset(-0x5c68L).get();

      v0 = v1 & 0x2L;
      if(v0 != 0) {
        at = 0x800d_0000L;
        MEMORY.ref(4, at).offset(-0x7910L).setu(0);
      }

      //LAB_800d41e0
      sp1c = 0x10L;
      sp18 = 0x20L;
      return;
    } else if(v0 == 0x6L) {
      //LAB_800d4128
      FUN_8002a3ec(7, 0);
      v1 = 0x8008_0000L;
      v1 = MEMORY.ref(4, v1).offset(-0x5c68L).get();

      v0 = v1 & 0x2L;
      if(v0 != 0) {
        at = 0x800d_0000L;
        MEMORY.ref(4, at).offset(-0x7910L).setu(0);
      }

      //LAB_800d4158
      sp1c = 0x10L;
      sp18 = 0x20L;
    } else {
      return;
    }

    //LAB_800d41f8
    final MATRIX sp0x38 = new MATRIX();
    rotateCoord2(struct258_800c66a8.deref().tmdRendering_08.deref().rotations_08.deref().get(0), struct258_800c66a8.deref().tmdRendering_08.deref().coord2s_04.deref().get(0));
    GsGetLs(struct258_800c66a8.deref().tmdRendering_08.deref().coord2s_04.deref().get(0), sp0x38);
    setRotTransMatrix(sp0x38);

    final SVECTOR sp0x58 = new SVECTOR();
    sp0x58.setX((short)struct258_800c66a8.deref().coord2_34.coord.transfer.getX());
    sp0x58.setY((short)struct258_800c66a8.deref().coord2_34.coord.transfer.getY());
    sp0x58.setZ((short)struct258_800c66a8.deref().coord2_34.coord.transfer.getZ());

    final SVECTOR sp0x60 = new SVECTOR(); // sxy2
    final Ref<Long> sp0x68 = new Ref<>(); // ir0
    final Ref<Long> sp0x64 = new Ref<>(); // flags
    sp6c = FUN_8003f900(sp0x58, sp0x60, sp0x68, sp0x64);
    sp70 = sp0x60.getXY() & 0xffff_ffffL;
    v1 = sp0x60.getXY();

    v0 = (int)v1 >> 16;
    sp72 = v0;
    v0 = 0x1f80_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x3d8L).get();

    sp74 = v0;
    v1 = sp74;

    v0 = v1 + 0x14L;
    v1 = v0;
    at = 0x1f80_0000L;
    MEMORY.ref(4, at).offset(0x3d8L).setu(v1);
    v0 = sp74;

    sp30 = v0;
    a0 = 0xeL;
    a1 = sp30;

    setGpuPacketType(a0, a1, false, false);
    v0 = 0x1f80_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x3d8L).get();

    sp78 = v0;
    v1 = sp78;

    v0 = v1 + 0x8L;
    v1 = v0;
    at = 0x1f80_0000L;
    MEMORY.ref(4, at).offset(0x3d8L).setu(v1);
    v0 = sp78;

    sp34 = v0;
    v0 = sp30;
    v1 = 0x7c28L;
    MEMORY.ref(2, v0).offset(0xeL).setu(v1);
    v0 = sp34;
    v1 = 0x1L;
    MEMORY.ref(1, v0).offset(0x3L).setu(v1);
    v0 = sp34;
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(2, v1).offset(-0x4eeaL).get();

    a0 = v1 | 0xaL;
    v1 = a0;
    a0 = v1 & 0x9ffL;
    v1 = a0 & 0xffffL;
    a0 = 0xe100_0000L;
    v1 = v1 | a0;
    MEMORY.ref(4, v0).offset(0x4L).setu(v1);
    v0 = sp30;
    v1 = 0x55L;
    MEMORY.ref(1, v0).offset(0x4L).setu(v1);
    v0 = sp30;

    MEMORY.ref(1, v0).offset(0x5L).setu(0);
    v0 = sp30;

    MEMORY.ref(1, v0).offset(0x6L).setu(0);
    v0 = sp30;
    v1 = sp1c;

    a0 = (int)v1 >> 31;
    a1 = a0 >>> 31;
    a0 = v1 + a1;
    v1 = (int)a0 >> 1;
    a0 = sp70;

    a0 = a0 - v1;
    MEMORY.ref(2, v0).offset(0x8L).setu(a0);
    v0 = sp30;
    v1 = sp72;
    a0 = sp1c;

    v1 = v1 - a0;
    MEMORY.ref(2, v0).offset(0xaL).setu(v1);
    v0 = sp30;
    v1 = sp1c;

    MEMORY.ref(2, v0).offset(0x10L).setu(v1);
    v0 = sp30;
    v1 = sp1c;

    MEMORY.ref(2, v0).offset(0x12L).setu(v1);
    v0 = sp30;
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(-0x4f04L).get();

    a0 = v1 & 0x7L;
    v1 = sp1c;

    lo = ((long)(int)a0 * (int)v1) & 0xffff_ffffL;
    t0 = lo;
    MEMORY.ref(1, v0).offset(0xcL).setu(t0);
    v0 = sp30;
    v1 = sp18;

    MEMORY.ref(1, v0).offset(0xdL).setu(v1);
    v1 = 0x1f80_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x3d0L).get();

    v0 = v1 + 0x64L;
    a0 = v0;
    a1 = sp30;

    insertElementIntoLinkedList(a0, a1);
    v1 = 0x1f80_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x3d0L).get();

    v0 = v1 + 0x64L;
    a0 = v0;
    a1 = sp34;

    insertElementIntoLinkedList(a0, a1);
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

    v1 = MEMORY.ref(1, v0).offset(0x1f8L).get();
    v0 = 0x4L;
    if(v1 != v0) {
      return;
    }

    //LAB_800d44d0
    sp24 = 0;
    sp20 = 0;

    //LAB_800d44d8
    do {
      v0 = sp20;

      a0 = v0;
      v1 = a0 << 1;
      v1 = v1 + v0;
      a0 = v1 << 2;
      a0 = a0 - v0;
      v0 = a0 << 2;
      at = 0x800f_0000L;
      at = at + v0;
      v1 = MEMORY.ref(2, at).offset(0x5a6cL).getSigned();
      v0 = -0x1L;
      if(v1 != v0) {
        break;
      }

      //LAB_800d4518
      v0 = 0x800c_0000L;
      v0 = v0 - 0x537cL;
      sp84 = v0;
      v0 = sp20;

      a0 = v0;
      v1 = a0 << 1;
      v1 = v1 + v0;
      a0 = v1 << 2;
      a0 = a0 - v0;
      v0 = a0 << 2;
      at = 0x800f_0000L;
      at = at + v0;
      v1 = MEMORY.ref(2, at).offset(0x5a6cL).getSigned();

      v0 = v1;
      sp7c = v0;
      v1 = sp7c;

      v0 = v1 & 0x1fL;
      v1 = 0x1L;
      v0 = v1 << v0;
      v1 = v0;
      sp80 = v1;
      v1 = sp7c;

      v0 = v1 >>> 5;
      v1 = v0;
      sp7c = v1;
      v0 = sp7c;

      v1 = v0;
      v0 = v1 << 2;
      v1 = sp84;

      v0 = v0 + v1;
      v1 = MEMORY.ref(4, v0).offset(0x0L).get();
      a0 = sp80;

      v0 = v1 & a0;
      if(v0 > 0) {
        //LAB_800d45cc
        v0 = sp20;

        sp24 = v0;
      }

      //LAB_800d45d8
      v0 = sp20;

      v1 = v0 + 0x1L;
      sp20 = v1;
    } while(true);

    //LAB_800d45f0
    v0 = sp24;

    if(v0 == 0) {
      return;
    }

    //LAB_800d4608
    v0 = sp24;

    a0 = v0;
    v1 = a0 << 1;
    v1 = v1 + v0;
    a0 = v1 << 2;
    a0 = a0 - v0;
    v0 = a0 << 2;
    at = 0x800f_0000L;
    at = at + v0;
    v1 = MEMORY.ref(2, at).offset(0x5a90L).get();

    v0 = v1 - 0xa0L;
    sp70 = v0;
    v0 = sp24;

    a0 = v0;
    v1 = a0 << 1;
    v1 = v1 + v0;
    a0 = v1 << 2;
    a0 = a0 - v0;
    v0 = a0 << 2;
    at = 0x800f_0000L;
    at = at + v0;
    v1 = MEMORY.ref(2, at).offset(0x5a92L).get();

    v0 = v1 - 0x78L;
    sp72 = v0;
    v0 = 0x1f80_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x3d8L).get();

    sp84 = v0;
    v1 = sp84;

    v0 = v1 + 0x14L;
    v1 = v0;
    at = 0x1f80_0000L;
    MEMORY.ref(4, at).offset(0x3d8L).setu(v1);
    v0 = sp84;

    sp30 = v0;
    a0 = 0xeL;
    a1 = sp30;

    setGpuPacketType(a0, a1, false, false);
    v0 = 0x1f80_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x3d8L).get();

    sp80 = v0;
    v1 = sp80;

    v0 = v1 + 0x8L;
    v1 = v0;
    at = 0x1f80_0000L;
    MEMORY.ref(4, at).offset(0x3d8L).setu(v1);
    v0 = sp80;

    sp34 = v0;
    v0 = sp30;
    v1 = 0x7c28L;
    MEMORY.ref(2, v0).offset(0xeL).setu(v1);
    v0 = sp34;
    v1 = 0x1L;
    MEMORY.ref(1, v0).offset(0x3L).setu(v1);
    v0 = sp34;
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(2, v1).offset(-0x4eeaL).get();

    a0 = v1 | 0xaL;
    v1 = a0;
    a0 = v1 & 0x9ffL;
    v1 = a0 & 0xffffL;
    a0 = 0xe100_0000L;
    v1 = v1 | a0;
    MEMORY.ref(4, v0).offset(0x4L).setu(v1);
    v0 = sp30;

    MEMORY.ref(1, v0).offset(0x4L).setu(0);
    v0 = sp30;

    MEMORY.ref(1, v0).offset(0x5L).setu(0);
    v0 = sp30;
    v1 = 0x55L;
    MEMORY.ref(1, v0).offset(0x6L).setu(v1);
    v0 = sp30;
    v1 = sp70;

    MEMORY.ref(2, v0).offset(0x8L).setu(v1);
    v0 = sp30;
    v1 = sp72;

    MEMORY.ref(2, v0).offset(0xaL).setu(v1);
    v0 = sp30;
    v1 = sp1c;

    MEMORY.ref(2, v0).offset(0x10L).setu(v1);
    v0 = sp30;
    v1 = sp1c;

    MEMORY.ref(2, v0).offset(0x12L).setu(v1);
    v0 = sp30;
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(-0x4f04L).get();

    a0 = v1 & 0x7L;
    v1 = sp1c;

    lo = ((long)(int)a0 * (int)v1) & 0xffff_ffffL;
    t0 = lo;
    MEMORY.ref(1, v0).offset(0xcL).setu(t0);
    v0 = sp30;
    v1 = sp18;

    MEMORY.ref(1, v0).offset(0xdL).setu(v1);
    v1 = 0x1f80_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x3d0L).get();

    v0 = v1 + 0x64L;
    a0 = v0;
    a1 = sp30;

    insertElementIntoLinkedList(a0, a1);
    v1 = 0x1f80_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x3d0L).get();

    v0 = v1 + 0x64L;
    a0 = v0;
    a1 = sp34;

    insertElementIntoLinkedList(a0, a1);
    v0 = sp24;

    a0 = v0;
    v1 = a0 << 1;
    v1 = v1 + v0;
    a0 = v1 << 2;
    a0 = a0 - v0;
    v1 = a0 << 2;
    at = 0x800f_0000L;
    at = at + v1;
    v0 = MEMORY.ref(4, at).offset(0x5a94L).get();

    a0 = v0;
    v1 = a0 << 1;
    v1 = v1 + v0;
    v0 = v1 << 2;
    at = 0x800f_0000L;
    at = at + v0;
    v1 = MEMORY.ref(4, at).offset(0x234L).get();

    if(v1 == 0) {
      return;
    }

    //LAB_800d4878
    v0 = sp24;

    a0 = v0;
    v1 = a0 << 1;
    v1 = v1 + v0;
    a0 = v1 << 2;
    a0 = a0 - v0;
    v0 = a0 << 2;
    at = 0x800f_0000L;
    at = at + v0;
    v1 = MEMORY.ref(2, at).offset(0x5a90L).get();

    sp70 = v1;
    v0 = sp24;

    a0 = v0;
    v1 = a0 << 1;
    v1 = v1 + v0;
    a0 = v1 << 2;
    a0 = a0 - v0;
    v0 = a0 << 2;
    at = 0x800f_0000L;
    at = at + v0;
    v1 = MEMORY.ref(2, at).offset(0x5a92L).get();

    v0 = v1 - 0x8L;
    sp72 = v0;

    at = 0x800f_0000L;
    v0 = MEMORY.ref(4, at).offset(0x5a94L).offset(sp24 * 0x2cL).get();

    final Ref<Long> sp0x28 = new Ref<>();
    final Ref<Long> sp0x2c = new Ref<>();
    FUN_800e7624(_800f0234.get((int)v0).text_00.deref(), sp0x28, sp0x2c);
    v0 = 0x800d_0000L;
    v0 = MEMORY.ref(4, v0).offset(-0x7910L).get();
    if(v0 == 0x1L) {
      //LAB_800d49e4
      _800bdf00.setu(0xeL);
      _800be358.get(7)._0c.set(0xeL);

      if(sp0x28.get().intValue() < 0x4L) {
        _800be358.get(7)._18.set((short)4);
      } else {
        assert false : "sp28?";
        //_800be584.setu(sp28); //2b
      }

      //LAB_800d4a28
      _800be358.get(7)._1a.set(sp0x2c.get().shortValue());
      _800c86f0.setu(0x2L);
      //LAB_800d4974
    } else if(v0 == 0) {
      sp28 = 0;
      sp2c = 0;
      assert false : "sp28/sp2c?";

      //LAB_800d4988
      sp10 = sp28 - 0x1L;
      sp14 = sp2c - 0x1L;

      FUN_8002a32c(7, 0, sp70, sp72, sp10, sp14);
      v0 = 0x1L;
      at = 0x800d_0000L;
      MEMORY.ref(4, at).offset(-0x7910L).setu(v0);

      //LAB_800d49e4
      _800bdf00.setu(0xeL);
      _800be358.get(7)._0c.set(0xeL);

      if((int)sp28 < 0x4L) {
        _800be358.get(7)._18.set((short)4);
      } else {
        _800be358.get(7)._18.set((short)sp28);
      }

      //LAB_800d4a28
      _800be358.get(7)._1a.set((short)sp2c);
      _800c86f0.setu(0x2L);
    } else if(v0 == 0x2L) {
      assert false : "sp28?"; sp28 = 0;

      //LAB_800d4a40
      v0 = sp28;
      at = 0x800c_0000L;
      MEMORY.ref(2, at).offset(-0x1a7cL).setu(v0);
      v0 = sp28;

      if((int)v0 < 0x4L) {
        v0 = 0x4L;
        at = 0x800c_0000L;
        MEMORY.ref(2, at).offset(-0x1a7cL).setu(v0);
      }

      assert false : "sp2c?"; sp2c = 0;

      //LAB_800d4a6c
      v0 = sp2c;
      at = 0x800c_0000L;
      MEMORY.ref(2, at).offset(-0x1a7aL).setu(v0);
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(2, v1).offset(-0x1a7cL).getSigned();

      a0 = v1;
      v0 = a0 << 3;
      v0 = v0 + v1;
      v1 = (int)v0 >> 31;
      a0 = v1 >>> 31;
      v1 = v0 + a0;
      v0 = (int)v1 >> 1;
      at = 0x800c_0000L;
      MEMORY.ref(2, at).offset(-0x1a78L).setu(v0);
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(2, v0).offset(-0x1a7aL).getSigned();

      a0 = v0;
      v1 = a0 << 1;
      v1 = v1 + v0;
      v0 = v1 << 1;
      at = 0x800c_0000L;
      MEMORY.ref(2, at).offset(-0x1a76L).setu(v0);
      v0 = sp70;
      at = 0x800c_0000L;
      MEMORY.ref(2, at).offset(-0x1a80L).setu(v0);
      v0 = sp72;
      at = 0x800c_0000L;
      MEMORY.ref(2, at).offset(-0x1a7eL).setu(v0);
    }

    //LAB_800d4aec
    v0 = 0x1aL;
    at = 0x800c_0000L;
    MEMORY.ref(4, at).offset(-0x2100L).setu(v0);
    v0 = 0x1aL;
    at = 0x800c_0000L;
    MEMORY.ref(4, at).offset(-0x1a88L).setu(v0);
    v0 = sp24;

    a0 = v0;
    v1 = a0 << 1;
    v1 = v1 + v0;
    a0 = v1 << 2;
    a0 = a0 - v0;
    v1 = a0 << 2;
    at = 0x800f_0000L;
    at = at + v1;
    v0 = MEMORY.ref(4, at).offset(0x5a94L).get();

    a0 = v0;
    v1 = a0 << 1;
    v1 = v1 + v0;
    v0 = v1 << 2;

    assert false : "Can't figure this out";
//    v1 = sp70 - sp28 * 3;
//    a0 = sp72 - sp2c * 7;
//    FUN_800e774c(_800f0234.offset(v0).deref(4).cast(ArrayRef.of(UnsignedShortRef.class, 0xff, 2, UnsignedShortRef::new)), v1, (short)a0, 0, 0); //TODO

    //LAB_800d4bb4
  }

  @Method(0x800d4bc8L)
  public static void FUN_800d4bc8(long a0) {
    long v0;
    long v1;
    long a1;
    long s0;
    long hi;
    long sp18;
    long sp14;
    long sp10;
    long sp30;
    sp30 = a0;
    v0 = sp30;

    if(v0 == 0) {
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

      a0 = MEMORY.ref(2, v1).offset(0x72L).get();

      MEMORY.ref(2, v0).offset(0x9aL).setu(a0);
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();

      MEMORY.ref(2, v0).offset(0x98L).setu(0);
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

      v0 = MEMORY.ref(2, v1).offset(0x98L).getSigned();
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

      a0 = MEMORY.ref(2, v1).offset(0x9aL).getSigned();

      v0 = v0 - a0;
      sp10 = v0;
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

      v0 = MEMORY.ref(2, v1).offset(0x98L).getSigned();
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

      a0 = MEMORY.ref(2, v1).offset(0x9aL).getSigned();

      v1 = a0 - 0x1000L;
      v0 = v0 - v1;
      sp14 = v0;
    } else {
      //LAB_800d4c80
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

      a0 = MEMORY.ref(2, v1).offset(0x9aL).get();

      MEMORY.ref(2, v0).offset(0x98L).setu(a0);
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

      a0 = MEMORY.ref(2, v1).offset(0x72L).get();

      MEMORY.ref(2, v0).offset(0x9aL).setu(a0);
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();
      v0 = MEMORY.ref(2, v0).offset(0x98L).getSigned();
      v1 = MEMORY.ref(2, v1).offset(0x9aL).getSigned();

      if((int)v1 < (int)v0) {
        v0 = -0x1000L;
        sp18 = v0;
      } else {
        //LAB_800d4cf8
        v0 = 0x1000L;
        sp18 = v0;
      }

      //LAB_800d4d00
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

      v0 = MEMORY.ref(2, v1).offset(0x98L).getSigned();
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

      a0 = MEMORY.ref(2, v1).offset(0x9aL).getSigned();

      v0 = v0 - a0;
      sp10 = v0;
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();

      v1 = MEMORY.ref(2, v0).offset(0x98L).getSigned();
      a0 = sp18;

      v0 = v1 + a0;
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

      a0 = MEMORY.ref(2, v1).offset(0x9aL).getSigned();

      v0 = v0 - a0;
      sp14 = v0;
    }

    //LAB_800d4d64
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

    a0 = MEMORY.ref(4, v1).offset(0x4cL).get();

    v1 = a0 << 8;
    a0 = 0x2aaa_0000L;
    a0 = a0 | 0xaaabL;
    hi = ((long)(int)v1 * (int)a0) >>> 32;
    v1 = (int)v1 >> 31;
    a1 = hi;
    a0 = a1 - v1;
    MEMORY.ref(4, v0).offset(0xa4L).setu(a0);
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

    a0 = MEMORY.ref(4, v1).offset(0x50L).get();

    v1 = a0 << 8;
    a0 = 0x2aaa_0000L;
    a0 = a0 | 0xaaabL;
    hi = ((long)(int)v1 * (int)a0) >>> 32;
    v1 = (int)v1 >> 31;
    a1 = hi;
    a0 = a1 - v1;
    MEMORY.ref(4, v0).offset(0xa8L).setu(a0);
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

    a0 = MEMORY.ref(4, v1).offset(0x54L).get();

    v1 = a0 << 8;
    a0 = 0x2aaa_0000L;
    a0 = a0 | 0xaaabL;
    hi = ((long)(int)v1 * (int)a0) >>> 32;
    v1 = (int)v1 >> 31;
    a1 = hi;
    a0 = a1 - v1;
    MEMORY.ref(4, v0).offset(0xacL).setu(a0);
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

    v0 = v1 + 0xb4L;
    MEMORY.ref(4, v0).offset(0x0L).setu(0);
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

    v0 = v1 + 0xb4L;
    MEMORY.ref(4, v0).offset(0x4L).setu(0);
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

    v0 = v1 + 0xb4L;
    MEMORY.ref(4, v0).offset(0x8L).setu(0);
    a0 = sp10;

    v0 = Math.abs(a0);
    s0 = v0;
    a0 = sp14;

    v0 = Math.abs(a0);
    if((int)v0 < (int)s0) {
      v0 = sp14;
      sp10 = v0;
    }

    //LAB_800d4e88
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
    v1 = sp10;
    a0 = 0x2aaa_0000L;
    a0 = a0 | 0xaaabL;
    hi = ((long)(int)v1 * (int)a0) >>> 32;
    a0 = (int)v1 >> 31;
    a1 = hi;
    v1 = a1 - a0;
    MEMORY.ref(2, v0).offset(0x9cL).setu(v1);
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();

    MEMORY.ref(2, v0).offset(0xa0L).setu(0);
  }

  @Method(0x800d4ed8L)
  public static void FUN_800d4ed8(long a0) {
    long v0;
    long v1;
    long a1;
    long s0;
    long hi;
    long sp18;
    long sp14;
    long sp10;
    long sp30;
    sp30 = a0;

    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();

    MEMORY.ref(2, v0).offset(0x7eL).setu(0);
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

    a0 = MEMORY.ref(2, v1).offset(0x72L).get();

    MEMORY.ref(2, v0).offset(0x78L).setu(a0);
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

    v0 = MEMORY.ref(2, v1).offset(0x72L).getSigned();
    v1 = sp30;

    a0 = v1;
    v1 = a0 << 9;
    v0 = v0 + v1;
    sp18 = v0;
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
    v1 = sp18;

    MEMORY.ref(2, v0).offset(0x7aL).setu(v1);
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();

    v1 = MEMORY.ref(2, v0).offset(0x72L).getSigned();
    v0 = sp18;

    v1 = v1 - v0;
    sp10 = v1;
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

    v0 = MEMORY.ref(2, v1).offset(0x72L).getSigned();
    a0 = sp18;

    v1 = a0 - 0x1000L;
    v0 = v0 - v1;
    sp14 = v0;
    a0 = sp10;

    v0 = Math.abs(a0);
    s0 = v0;
    a0 = sp14;

    v0 = Math.abs(a0);
    if((int)v0 < (int)s0) {
      v0 = sp14;
      sp10 = v0;
    }

    //LAB_800d4fd0
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
    a0 = sp10;

    v1 = -a0;
    a0 = 0x2aaa_0000L;
    a0 = a0 | 0xaaabL;
    hi = ((long)(int)v1 * (int)a0) >>> 32;
    a0 = (int)v1 >> 31;
    a1 = hi;
    v1 = a1 - a0;
    MEMORY.ref(2, v0).offset(0x7cL).setu(v1);
  }

  @Method(0x800d5018L)
  public static void FUN_800d5018() {
    long v1;

    _800c66b0.deref()._110.set(0);
    _800c66b0.deref()._10e.set(0);
    _800c66b0.deref()._c8._00.set(_800c66b0.deref()._00._00.get());
    _800c66b0.deref()._c8._04.set(_800c66b0.deref()._00._04.get());
    _800c66b0.deref()._c8._08.set(_800c66b0.deref()._00._08.get());
    _800c66b0.deref()._c8._0c.set(_800c66b0.deref()._00._0c.get());
    _800c66b0.deref()._c8._10.set(_800c66b0.deref()._00._10.get());
    _800c66b0.deref()._c8._14.set(_800c66b0.deref()._00._14.get());
    _800c66b0.deref()._c8._18.set(_800c66b0.deref()._00._18.get());
    _800c66b0.deref()._c8._1c.set(_800c66b0.deref()._00._1c.deref());

    v1 = -100 - _800c66b0.deref()._c8._04.get();
    if((int)v1 < 0) {
      v1 = v1 + 0xfL;
    }

    //LAB_800d50cc
    _800c66b0.deref()._ec.set((int)v1 >> 4);

    v1 = -600 - _800c66b0.deref()._c8._08.get();
    if((int)v1 < 0) {
      v1 = v1 + 0xfL;
    }

    //LAB_800d5104
    _800c66b0.deref()._f0.set((int)v1 >> 4);

    v1 = -90 - _800c66b0.deref()._c8._10.get();
    if((int)v1 < 0) {
      v1 = v1 + 0xfL;
    }

    //LAB_800d513c
    _800c66b0.deref()._f8.set((int)v1 >> 4);

    v1 = -_800c66b0.deref()._c8._14.get();
    if((int)v1 < 0) {
      v1 = v1 + 0xfL;
    }

    //LAB_800d5174
    _800c66b0.deref()._fc.set((int)v1 >> 4);

    _800c66b0.deref()._10a.set(_800c66b0.deref().rotation_70.getY());

    short sp18 = (short)(struct258_800c66a8.deref().rotation_a4.getY() + 0x800);
    _800c66b0.deref()._108.set(sp18);

    long sp10 = _800c66b0.deref().rotation_70.getY() - sp18;
    long sp14 = _800c66b0.deref().rotation_70.getY() - (sp18 - 0x1000L);

    if(Math.abs(sp14) < Math.abs(sp10)) {
      sp10 = sp14;
    }

    //LAB_800d5244
    v1 = -sp10;
    if((int)v1 < 0) {
      v1 = v1 + 0xfL;
    }

    //LAB_800d5268
    _800c66b0.deref()._10c.set((short)(v1 >> 4));
  }

  @Method(0x800d5288L)
  public static void FUN_800d5288() {
    long v0;
    long v1;
    long sp10;
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();

    v1 = MEMORY.ref(1, v0).offset(0x11aL).get();

    if(v1 == 0) {
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();

      v1 = MEMORY.ref(4, v0).offset(0x158L).get();

      if(v1 < 0x5aL) {
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
        v1 = 0x1L;
        MEMORY.ref(1, v0).offset(0x11aL).setu(v1);
      } else {
        //LAB_800d52e8
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(1, v0).offset(0x5L).get();

        if(v1 != 0) {
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();

          v1 = MEMORY.ref(1, v0).offset(0xc5L).get();
          v0 = 0x2L;
          if(v1 == v0) {
            return;
          }
        }

        //LAB_800d5328
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
        v1 = 0x3L;
        MEMORY.ref(1, v0).offset(0x11aL).setu(v1);
      }
    }

    //LAB_800d5338
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

    v0 = MEMORY.ref(1, v1).offset(0x11aL).get();
    v1 = 0x2L;
    if(v0 == v1) {
      //LAB_800d53b4
      _800c66b0.deref()._114.incr();
      sp10 = 0x40L - _800c66b0.deref()._114.get() * 0x2L;

      if(sp10 < 0x4L) {
        sp10 = 0x4L;
      }

      //LAB_800d5424
      _800c66b0.deref()._118.add((short)sp10);

      if(_800c66b0.deref()._118.get() >= 0x320L) {
        _800c66b0.deref()._118.set((short)0x320);
        _800c66b0.deref()._11a.set(0);
      }

      //LAB_800d548c
      setProjectionPlaneDistance(_800c66b0.deref()._118.get());
      return;
    }

    if((int)v0 < 0x3L) {
      v1 = 0x1L;
      if(v0 == v1) {
        //LAB_800d5394
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();

        MEMORY.ref(4, v0).offset(0x114L).setu(0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
        v1 = 0x2L;
        MEMORY.ref(1, v0).offset(0x11aL).setu(v1);

        //LAB_800d53b4
        _800c66b0.deref()._114.incr();
        sp10 = 0x40L - _800c66b0.deref()._114.get() * 0x2L;

        if(sp10 < 0x4L) {
          sp10 = 0x4L;
        }

        //LAB_800d5424
        _800c66b0.deref()._118.add((short)sp10);

        if(_800c66b0.deref()._118.get() >= 0x320L) {
          _800c66b0.deref()._118.set((short)0x320);
          _800c66b0.deref()._11a.set(0);
        }

        //LAB_800d548c
        setProjectionPlaneDistance(_800c66b0.deref()._118.get());
        return;
      }

      setProjectionPlaneDistance(_800c66b0.deref()._118.get());
      return;
    }

    //LAB_800d5374
    v1 = 0x3L;
    if(v0 != v1) {
      v1 = 0x4L;
      if(v0 == v1) {
        //LAB_800d54e8
        _800c66b0.deref()._114.incr();

        sp10 = 0x40L - _800c66b0.deref()._114.get() * 0x2L;

        if(sp10 < 0x4L) {
          sp10 = 0x4L;
        }

        //LAB_800d5558
        _800c66b0.deref()._118.sub((short)sp10);

        if(_800c66b0.deref()._118.get() < 0x259L) {
          _800c66b0.deref()._118.set((short)0x258);
          _800c66b0.deref()._11a.set(0);

          //LAB_800d55c0
        }

        //LAB_800d55c8
        setProjectionPlaneDistance(_800c66b0.deref()._118.get());
        return;
      }

      setProjectionPlaneDistance(_800c66b0.deref()._118.get());
      return;
    }

    //LAB_800d5494
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();

    v1 = MEMORY.ref(1, v0).offset(0xc4L).getSigned();

    if(v1 != 0) {
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();

      MEMORY.ref(1, v0).offset(0x11aL).setu(0);
    } else {
      //LAB_800d54c8
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();

      MEMORY.ref(4, v0).offset(0x114L).setu(0);
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
      v1 = 0x4L;
      MEMORY.ref(1, v0).offset(0x11aL).setu(v1);

      //LAB_800d54e8
      _800c66b0.deref()._114.incr();

      sp10 = 0x40L - _800c66b0.deref()._114.get() * 0x2L;

      if(sp10 < 0x4L) {
        sp10 = 0x4L;
      }

      //LAB_800d5558
      _800c66b0.deref()._118.sub((short)sp10);

      if(_800c66b0.deref()._118.get() < 0x259L) {
        _800c66b0.deref()._118.set((short)0x258);
        _800c66b0.deref()._11a.set(0);

        //LAB_800d55c0
      }

      //LAB_800d55c8
      setProjectionPlaneDistance(_800c66b0.deref()._118.get());
    }

    //LAB_800d55e8
  }

  @Method(0x800d55fcL)
  public static void FUN_800d55fc() {
    removeFromLinkedList(_800c66b0.getPointer());
  }

  @Method(0x800d562cL)
  public static void FUN_800d562c(final Value address, final long size, final long param) {
    final McqHeader mcq = address.deref(4).cast(McqHeader::new);
    final long sp20 = (param & 0xffff_fffeL) * 0x40L + 0x140L;

    final long sp24;
    if((param & 0x1L) != 0) {
      sp24 = 0x100L;
    } else {
      //LAB_800d5688
      sp24 = 0;
    }

    //LAB_800d568c
    final RECT sp0x18 = new RECT(
      (short)sp20,
      (short)sp24,
      mcq.width_08.get(),
      mcq.height_0a.get()
    );

    LoadImage(sp0x18, mcq.getAddress() + mcq.imageDataOffset_04.get());
    DrawSync(0);
    memcpy(mcqHeader_800c6768.getAddress(), mcq.getAddress(), 0x2c);
    FUN_800127cc(address.get(), 0, 0x1L);

    _800c66b8.oru(0x1L);
  }

  @Method(0x800d5768L)
  public static void FUN_800d5768(final Value address, long size, long param) {
    long at;
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long sp;
    long ra;
    long sp18;
    long sp28;
    long sp10;
    long sp24;
    long sp20;
    sp20 = address.get();
    sp24 = size;
    sp28 = param;
    v0 = sp28;

    v1 = v0;
    a0 = v1 << 3;
    at = 0x800f_0000L;
    at = at + a0;
    v0 = MEMORY.ref(2, at).offset(-0xf34L).getSigned();
    v1 = sp28;

    a0 = v1;
    a1 = a0 << 3;
    at = 0x800f_0000L;
    at = at + a1;
    v1 = MEMORY.ref(2, at).offset(-0xf32L).getSigned();
    a0 = sp28;

    a1 = a0;
    a0 = a1 << 3;
    at = 0x800f_0000L;
    at = at + a0;
    a3 = MEMORY.ref(2, at).offset(-0xf30L).getSigned();
    a0 = sp28;

    a1 = a0;
    a0 = a1 << 3;
    at = 0x800f_0000L;
    at = at + a0;
    a1 = MEMORY.ref(2, at).offset(-0xf2eL).getSigned();

    sp10 = a1;
    a0 = sp20;
    a1 = v0;
    a2 = v1;

    FUN_800d5c50(a0, a1, a2, a3, sp10);
    a0 = 0;

    v0 = DrawSync(0);
    a0 = sp20;
    a1 = 0;
    a2 = 0;

    removeFromLinkedList(a0);
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66b8L).get();

    v1 = v0 | 0x800L;
    at = 0x800c_0000L;
    MEMORY.ref(4, at).offset(0x66b8L).setu(v1);

    //LAB_800d5848
  }

  @Method(0x800d5858L) //TODO loads general world map stuff (location text, doors, buttons, etc.), several blobs that may be smoke?, tons of terrain and terrain sprites
  public static void FUN_800d5858(final Value address, long size, long param) {
    final long a0 = address.get();

    //LAB_800d5874
    for(int i = 0; i < MEMORY.ref(4, a0).offset(0x4L).get(); i++) {
      //LAB_800d5898
      if(MEMORY.ref(4, a0).offset(i * 0x8L).offset(0xcL).get() != 0) {
        //LAB_800d58c8
        FUN_800d5d98(a0 + MEMORY.ref(4, a0).offset((i + 1) * 0x8L).get());
        DrawSync(0);
      }

      //LAB_800d5920
    }

    //LAB_800d5938
    removeFromLinkedList(a0);

    _800c66b8.oru(param);

    //LAB_800d5970
  }

  @Method(0x800d5984L)
  public static void loadTmdCallback(final Value address, final long size, final long param) {
    final TmdWithId tmd = address.deref(4).cast(TmdWithId::new);

    struct258_800c66a8.deref().tmdRendering_08.set(loadTmd(tmd));
    initTmdTransforms(struct258_800c66a8.deref().tmdRendering_08.deref(), null);
    struct258_800c66a8.deref().tmdRendering_08.deref().tmd_14.set(tmd);
    setAllCoord2Attribs(struct258_800c66a8.deref().tmdRendering_08.deref(), 0);
    _800c66b8.oru(0x4L);
  }

  @Method(0x800d5a30L)
  public static void FUN_800d5a30(Value address, long size, long param) {
    long at;
    long v0;
    long v1;
    long a0 = address.get();
    long a1 = size;
    long a2 = param;
    long sp18;
    long sp0;
    long sp4;
    long sp1c;
    long sp8;
    long sp20;
    sp18 = a0;
    sp1c = a1;
    sp20 = a2;
    sp0 = 0;

    //LAB_800d5a48
    do {
      v0 = sp18;
      v1 = sp0;
      v0 = MEMORY.ref(4, v0).offset(0x4L).get();

      if(v1 >= v0) {
        break;
      }

      //LAB_800d5a6c
      v0 = sp18;
      v1 = sp0;

      a0 = v1;
      v1 = a0 << 3;
      v0 = v0 + v1;
      v1 = MEMORY.ref(4, v0).offset(0xcL).get();

      if(v1 != 0) {
        //LAB_800d5a9c
        v0 = sp0;

        if((int)v0 >= 0x10L) {
          break;
        }

        //LAB_800d5ab8
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
        v1 = sp0;

        a0 = v1;
        v1 = a0 << 2;
        a0 = sp20;

        a1 = a0;
        a0 = a1 << 6;
        v1 = v1 + a0;
        v0 = v0 + v1;
        v1 = sp18;

        sp4 = v1;
        v1 = sp4;
        a0 = sp0;

        a1 = a0;
        a0 = a1 << 3;
        v1 = v1 + a0;
        a0 = MEMORY.ref(4, v1).offset(0x8L).get();

        sp8 = a0;
        v1 = sp4;
        a0 = sp8;

        v1 = v1 + a0;
        MEMORY.ref(4, v0).offset(0xb4L).setu(v1);
      }

      //LAB_800d5b2c
      v0 = sp0;

      v1 = v0 + 0x1L;
      sp0 = v1;
    } while(true);

    //LAB_800d5b44
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
    v1 = sp20;

    a0 = v1;
    v1 = a0 << 2;
    v0 = v0 + v1;
    v1 = sp18;

    MEMORY.ref(4, v0).offset(0x1b4L).setu(v1);
    v0 = sp20;
    v1 = 0x1L;
    if(v0 == v1) {
      //LAB_800d5bd8
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66b8L).get();

      v1 = v0 | 0x40L;
      at = 0x800c_0000L;
      MEMORY.ref(4, at).offset(0x66b8L).setu(v1);
    } else if((int)v0 >= 0x2L) {
      //LAB_800d5b98
      v1 = 0x2L;
      if(v0 == v1) {
        //LAB_800d5bf8
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66b8L).get();

        v1 = v0 | 0x100L;
        at = 0x800c_0000L;
        MEMORY.ref(4, at).offset(0x66b8L).setu(v1);
      } else if(v0 == 0x3L) {
        //LAB_800d5c18
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66b8L).get();

        v1 = v0 | 0x400L;
        at = 0x800c_0000L;
        MEMORY.ref(4, at).offset(0x66b8L).setu(v1);
      }
    } else if(v0 == 0) {
      //LAB_800d5bb8
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66b8L).get();

      v1 = v0 | 0x10L;
      at = 0x800c_0000L;
      MEMORY.ref(4, at).offset(0x66b8L).setu(v1);
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
  public static long FUN_800d5e70(RECT sp28, long a1, long a2, long a3) {
    long v0;
    long v1;
    long t0;
    long lo;
    long sp18;
    long sp14;
    long sp10;
    long sp2c;
    long sp30;
    long a0;
    sp2c = a1;
    sp30 = a2;
    v0 = a3;
    sp10 = v0;
    a0 = 0x20L;

    v0 = addToLinkedListTail(a0);
    sp14 = v0;
    v0 = sp14;

    a0 = sp28.x.get();

    MEMORY.ref(2, v0).offset(0x0L).setu(a0);
    v0 = sp14;

    a0 = sp28.y.get();

    MEMORY.ref(2, v0).offset(0x2L).setu(a0);
    v0 = sp14;

    v1 = sp28.w.get();
    a0 = sp2c;

    a1 = a0;
    a0 = a1 << 1;
    a1 = 0x4L;
    a0 = a1 - a0;
    lo = (int)v1 / (int)a0;
    v1 = lo;

    MEMORY.ref(2, v0).offset(0x4L).setu(v1);
    v0 = sp14;

    a0 = sp28.h.get();

    MEMORY.ref(2, v0).offset(0x6L).setu(a0);
    v0 = sp14;
    v1 = sp30;

    MEMORY.ref(4, v0).offset(0x10L).setu(v1);
    v0 = sp14;
    v1 = sp2c;

    MEMORY.ref(4, v0).offset(0x14L).setu(v1);
    v0 = sp14;
    v1 = sp10;

    MEMORY.ref(2, v0).offset(0x18L).setu(v1);

    v0 = sp28.w.get();
    v1 = 0x2L;
    a0 = sp2c;

    v1 = v1 - a0;
    lo = (v0 & 0xffff_ffffL) / (v1 & 0xffff_ffffL);
    v0 = lo;

    a0 = sp28.h.get();

    lo = ((long)(int)v0 * (int)a0) & 0xffff_ffffL;
    t0 = lo;
    sp18 = t0;
    a0 = sp18;

    v0 = addToLinkedListTail(a0);
    v1 = sp14;

    MEMORY.ref(4, v1).offset(0x8L).setu(v0);

    v0 = sp28.w.get();
    v1 = 0x2L;
    a0 = sp2c;

    v1 = v1 - a0;
    lo = (v0 & 0xffff_ffffL) / (v1 & 0xffff_ffffL);
    v0 = lo;

    a0 = sp28.h.get();

    lo = ((long)(int)v0 * (int)a0) & 0xffff_ffffL;
    t0 = lo;
    sp18 = t0;
    a0 = sp18;

    v0 = addToLinkedListTail(a0);
    v1 = sp14;

    MEMORY.ref(4, v1).offset(0xcL).setu(v0);
    v0 = sp14;
    v1 = sp30;

    a0 = (int)v1 >> 31;
    a1 = a0 >>> 31;
    a0 = v1 + a1;
    v1 = (int)a0 >> 1;
    a0 = v1;
    v1 = a0 << 1;
    MEMORY.ref(2, v0).offset(0x1aL).setu(v1);
    v0 = sp14;
    v1 = sp14;

    a0 = MEMORY.ref(2, v1).offset(0x1aL).get();

    MEMORY.ref(2, v0).offset(0x1cL).setu(a0);
    v1 = sp14;

    v0 = v1;

    //LAB_800d606c
    return v0;
  }

  @Method(0x800d6080L)
  public static void FUN_800d6080(final long a0) {
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
    removeFromLinkedList(MEMORY.ref(4, a0).offset(0xcL).get());
    removeFromLinkedList(MEMORY.ref(4, a0).offset(0x8L).get());
    removeFromLinkedList(a0);
  }

  @Method(0x800d6880L)
  public static void FUN_800d6880() {
    _800c66b8.and(0xffff_efffL);
    loadDrgnBinFile(0, 0x163fL, 0, getMethodAddress(WMap.class, "FUN_800d5858", Value.class, long.class, long.class), 0x1_1000L, 0x4L);
    struct258_800c66a8.deref()._20.set((short)0);
  }

  @Method(0x800d6900L)
  public static void FUN_800d6900() {
    if((_800c66b8.get() & 0x1000L) == 0) {
      return;
    }

    //LAB_800d692c
    if(struct258_800c66a8.deref()._250.get() == 0x2L) {
      return;
    }

    //LAB_800d6950
    final long sp10 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x14L);

    setGpuPacketType(0xeL, sp10, false, false);

    final long sp14 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x8L);
    MEMORY.ref(1, sp14).offset(0x3L).setu(0x1L);
    MEMORY.ref(4, sp14).offset(0x4L).setu(0xe100_000aL | _800bb116.get() & 0x9ffL);

    MEMORY.ref(1, sp10).offset(0x4L).setu(struct258_800c66a8.deref()._20.get());
    MEMORY.ref(1, sp10).offset(0x5L).setu(struct258_800c66a8.deref()._20.get());
    MEMORY.ref(1, sp10).offset(0x6L).setu(struct258_800c66a8.deref()._20.get());
    MEMORY.ref(2, sp10).offset(0x8L).setu(-0x90L);
    MEMORY.ref(2, sp10).offset(0xaL).setu(-0x68L);
    MEMORY.ref(1, sp10).offset(0xcL).setu(0x80L);
    MEMORY.ref(2, sp10).offset(0xeL).setu(0x7c68L);
    MEMORY.ref(1, sp10).offset(0xdL).setu(_800c6798.get() * 0x18L);
    MEMORY.ref(2, sp10).offset(0x10L).setu(0x80L);
    MEMORY.ref(2, sp10).offset(0x12L).setu(0x18L);

    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x34L, sp10);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x34L, sp14);

    struct258_800c66a8.deref()._20.add((short)0x10);

    if(struct258_800c66a8.deref()._20.get() > 0x80L) {
      struct258_800c66a8.deref()._20.set((short)0x80);
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

    //LAB_800d6b9c
    long sp18 = switch(struct258_800c66a8.deref()._1f8.get()) {
      case 0 -> 0x2L;
      case 1, 2, 3, 6 -> 0x3L;
      case 4, 5 -> 0x4L;
      default -> 0;
    };

    //LAB_800d6c10
    //LAB_800d6c14
    final long[] sp0x20 = new long[7];
    final long[] sp0x40 = new long[7];
    for(int i = 0; i < 7; i++) {
      //LAB_800d6c30
      sp0x20[i] = linkedListAddress_1f8003d8.get();
      linkedListAddress_1f8003d8.addu(0x14L);

      sp0x40[i] = linkedListAddress_1f8003d8.get();
      linkedListAddress_1f8003d8.addu(0x8L);

      //LAB_800d6d14
      setGpuPacketType(0xeL, sp0x20[i], i < 0x2L, false);

      //LAB_800d6d44
      //LAB_800d6d84
      MEMORY.ref(2, sp0x20[i]).offset(0xeL).setu(i >= 0x5L ? 0x7de8L : 0x7da8L);

      //LAB_800d6da8
      MEMORY.ref(1, sp0x40[i]).offset(0x3L).setu(0x1L);
      MEMORY.ref(4, sp0x40[i]).offset(0x4L).setu(0xe100_000aL | _800bb112.get(0x9ffL));

      if(i < 0x2L || i >= 0x5L) {
        //LAB_800d6f34
        MEMORY.ref(1, sp0x20[i]).offset(0x4L).setu(0x80L);
        MEMORY.ref(1, sp0x20[i]).offset(0x5L).setu(0x80L);
        MEMORY.ref(1, sp0x20[i]).offset(0x6L).setu(0x80L);
      } else if(i == sp18) {
        MEMORY.ref(1, sp0x20[i]).offset(0x4L).setu(0xffL);
        MEMORY.ref(1, sp0x20[i]).offset(0x5L).setu(0xffL);
        MEMORY.ref(1, sp0x20[i]).offset(0x6L).setu(0xffL);
      } else {
        //LAB_800d6ec0
        MEMORY.ref(1, sp0x20[i]).offset(0x4L).setu(0x40L);
        MEMORY.ref(1, sp0x20[i]).offset(0x5L).setu(0x40L);
        MEMORY.ref(1, sp0x20[i]).offset(0x6L).setu(0x40L);
      }

      //LAB_800d6f2c
      //LAB_800d6fa0
      MEMORY.ref(2, sp0x20[i]).offset(0x08L).setu(_800ef104.offset(i * 0x6L       ).get() + 0x58L);
      MEMORY.ref(2, sp0x20[i]).offset(0x0aL).setu(_800ef104.offset(i * 0x6L + 0x1L).get() - 0x60L);
      MEMORY.ref(1, sp0x20[i]).offset(0x0cL).setu(_800ef104.offset(i * 0x6L + 0x2L).get());
      MEMORY.ref(1, sp0x20[i]).offset(0x0dL).setu(_800ef104.offset(i * 0x6L + 0x3L).get());
      MEMORY.ref(2, sp0x20[i]).offset(0x10L).setu(_800ef104.offset(i * 0x6L + 0x4L).get());
      MEMORY.ref(2, sp0x20[i]).offset(0x12L).setu(_800ef104.offset(i * 0x6L + 0x5L).get());

      insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x50L, sp0x20[i]);
      insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x50L, sp0x40[i]);
    }

    //LAB_800d71f4
  }

  @Method(0x800d7208L)
  public static void FUN_800d7208(long a0) {
    assert false;
  }

  @Method(0x800d7a34L)
  public static void renderPath() {
    long at;
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long t0;
    long t4;
    long lo;
    long sp10;
    long sp38;
    long sp50;
    long sp54;
    long sp58;
    long sp60;
    long sp64;
    long sp68;
    long sp80;
    long sp84;
    long sp88;
    long sp8c;
    long sp90;
    long sp94;
    long sp98;
    final SVECTOR sp9c = new SVECTOR();
    long spa0;
    long spa4;
    long spa8;
    long spac = 0;
    long spae = 0;
    long sp1b8;
    long sp1c0;
    long sp1c4;
    long sp1ba;
    long sp1bc;
    long sp1be;

    final MATRIX sp0x18 = new MATRIX();
    final VECTOR sp0x40 = new VECTOR();
    final SVECTOR sp0x70 = new SVECTOR();
    final long[] sp0xb0 = new long[0xff];

    if(_800c6698.get() < 0x4L) {
      return;
    }

    //LAB_800d7a60
    if(_800c669c.get() < 0x4L) {
      return;
    }

    //LAB_800d7a80
    if(struct258_800c66a8.deref()._1f8.get() == 0x2L || struct258_800c66a8.deref()._1f8.get() == 0x3L || struct258_800c66a8.deref()._1f8.get() == 0x4L || struct258_800c66a8.deref()._1f8.get() == 0x5L) {
      //LAB_800d7af8
      return;
    }

    //LAB_800d7b00
    v0 = struct258_800c66a8.deref()._1f8.get();
    if(v0 == 0x1L || v0 == 0x6L) {
      //LAB_800d7b64
      sp94 = 0xcL;
    } else if(v0 == 0) {
      //LAB_800d7b58
      sp94 = 0;
      //LAB_800d7b38
    } else if(v0 == 0x4L) {
      //LAB_800d7b74
      sp94 = 0x18L;
    } else {
      sp94 = 0; //TODO this was uninitialized in the code
    }

    //LAB_800d7b84
    sp98 = _800bb0fc.get() / 5 % 3;

    at = 0x800f_0000L; //TODO
    sp1b8 = MEMORY.ref(1, at).offset(-0xe90L).offset(sp94).offset(sp98 * 0x4L).offset(0x0L).get();
    sp1ba = MEMORY.ref(1, at).offset(-0xe90L).offset(sp94).offset(sp98 * 0x4L).offset(0x1L).get();
    sp1bc = MEMORY.ref(1, at).offset(-0xe90L).offset(sp94).offset(sp98 * 0x4L).offset(0x2L).get();
    sp1be = MEMORY.ref(1, at).offset(-0xe90L).offset(sp94).offset(sp98 * 0x4L).offset(0x3L).get();

    sp60 = struct258_800c66a8.deref().coord2_34.coord.transfer.getX();
    sp64 = struct258_800c66a8.deref().coord2_34.coord.transfer.getY();
    sp68 = struct258_800c66a8.deref().coord2_34.coord.transfer.getZ();

    rotateCoord2(struct258_800c66a8.deref().tmdRendering_08.deref().rotations_08.deref().get(0), struct258_800c66a8.deref().tmdRendering_08.deref().coord2s_04.deref().get(0));
    GsGetLs(struct258_800c66a8.deref().tmdRendering_08.deref().coord2s_04.deref().get(0), sp0x18);
    setRotTransMatrix(sp0x18);

    sp10 = linkedListAddress_1f8003d8.get();

    //LAB_800d7d6c
    for(int i = 0; i < _800c67a0.get(); i++) {
      //LAB_800d7d90
      if(FUN_800eb09c(i, 1, sp0x40) == 0) {
        //LAB_800d7db4
        if(_800c6798.get() != 0x7L || i == 0x1fL || i == 0x4eL) {
          //LAB_800d7df0
          sp0x70.setX((short)sp0x40.getX());
          sp0x70.setY((short)sp0x40.getY());
          sp0x70.setZ((short)sp0x40.getZ());

          CPU.MTC2(sp0x70.getXY(), 0);
          CPU.MTC2(sp0x70.getZ(), 1);
          CPU.COP2(0x18_0001L); // Perspective transform single

          sp9c.setXY(CPU.MFC2(14));
          spa4 = CPU.MFC2(8);
          spa0 = CPU.CFC2(31);
          spa8 = (int)CPU.MFC2(19) >> 2;

          if(spa8 >= 0x4L && spa8 < _1f8003c8.get()) {
            setGpuPacketType(0xcL, sp10, true, false);

            MEMORY.ref(2, sp10).offset(0x16L).setu(_800bb116.get() | 0xaL);
            MEMORY.ref(2, sp10).offset(0xeL).setu(0x7c28L);

            if(struct258_800c66a8.deref()._1f8.get() == 0) {
              v0 = sp0x70.getX();
              v1 = sp60;

              v0 = v1 - v0;
              sp50 = v0;
              v0 = sp0x70.getY();
              v1 = sp64;

              v0 = v1 - v0;
              sp54 = v0;
              v0 = sp0x70.getZ();
              v1 = sp68;

              v0 = v1 - v0;
              sp58 = v0;
              v0 = sp50;
              v1 = sp50;

              lo = ((long)(int)v0 * (int)v1) & 0xffff_ffffL;
              t0 = lo;
              sp1c4 = t0;
              v0 = sp54;
              a0 = sp54;

              lo = ((long)(int)v0 * (int)a0) & 0xffff_ffffL;
              v1 = lo;
              t0 = sp1c4;

              v0 = t0 + v1;
              v1 = sp58;
              a0 = sp58;

              lo = ((long)(int)v1 * (int)a0) & 0xffff_ffffL;
              t0 = lo;
              sp1c4 = t0;
              t0 = sp1c4;

              v0 = v0 + t0;
              a0 = v0;

              v0 = SquareRoot0(a0);
              sp8c = v0;
              v0 = 0x200L;
              v1 = sp8c;

              v0 = v0 - v1;
              sp90 = v0;
              v0 = sp90;

              if((int)v0 < 0) {
                sp90 = 0;
              }

              //LAB_800d7fb8
              v0 = sp90;

              v1 = (int)v0 >> 31;
              a0 = v1 >>> 31;
              v1 = v0 + a0;
              v0 = (int)v1 >> 1;
              sp90 = v0;
              v0 = sp10;
              v1 = sp90;

              a1 = v1;
              a0 = a1 << 5;
              a0 = a0 - v1;
              v1 = a0;
              if((int)v1 < 0) {
                v1 = v1 + 0xffL;
              }

              //LAB_800d7ffc
              v1 = (int)v1 >> 8;
              MEMORY.ref(1, v0).offset(0x4L).setu(v1);
              v0 = sp10;
              v1 = sp90;

              a1 = v1;
              a0 = a1 << 6;
              a0 = a0 - v1;
              v1 = a0;
              if((int)v1 < 0) {
                v1 = v1 + 0xffL;
              }

              //LAB_800d802c
              v1 = (int)v1 >> 8;
              MEMORY.ref(1, v0).offset(0x5L).setu(v1);
              v0 = sp10;

              MEMORY.ref(1, v0).offset(0x6L).setu(0);
            } else {
              //LAB_800d8048
              v0 = sp10;
              v1 = 0x1fL;
              MEMORY.ref(1, v0).offset(0x4L).setu(v1);
              v0 = sp10;
              v1 = 0x3fL;
              MEMORY.ref(1, v0).offset(0x5L).setu(v1);
              v0 = sp10;

              MEMORY.ref(1, v0).offset(0x6L).setu(0);
            }

            //LAB_800d806c
            v0 = sp10;
            v1 = sp1b8;

            MEMORY.ref(1, v0).offset(0xcL).setu(v1);
            v0 = sp10;
            v1 = sp1ba;

            MEMORY.ref(1, v0).offset(0xdL).setu(v1);
            v0 = sp10;
            v1 = sp1b8;
            a0 = sp1bc;

            v1 = v1 + a0;
            MEMORY.ref(1, v0).offset(0x14L).setu(v1);
            v0 = sp10;
            v1 = sp1ba;

            MEMORY.ref(1, v0).offset(0x15L).setu(v1);
            v0 = sp10;
            v1 = sp1b8;

            MEMORY.ref(1, v0).offset(0x1cL).setu(v1);
            v0 = sp10;
            v1 = sp1ba;
            a0 = sp1be;

            v1 = v1 + a0;
            MEMORY.ref(1, v0).offset(0x1dL).setu(v1);
            v0 = sp10;
            v1 = sp1b8;
            a0 = sp1bc;

            v1 = v1 + a0;
            MEMORY.ref(1, v0).offset(0x24L).setu(v1);
            v0 = sp10;
            v1 = sp1ba;
            a0 = sp1be;

            v1 = v1 + a0;
            MEMORY.ref(1, v0).offset(0x25L).setu(v1);
            spac = sp9c.getX();
            spae = sp9c.getY();
            v0 = sp10;
            v1 = sp98;

            a0 = v1;
            v1 = a0 << 2;
            a0 = sp94;

            v1 = v1 + a0;
            a0 = v1 + 0x2L;
            at = 0x800f_0000L;
            at = at + a0;
            a1 = MEMORY.ref(1, at).offset(-0xe90L).get();

            v1 = a1 >>> 2;
            a0 = v1 & 0xffL;
            v1 = spac;

            a0 = v1 - a0;
            MEMORY.ref(2, v0).offset(0x8L).setu(a0);
            v0 = sp10;
            v1 = sp98;

            a0 = v1;
            v1 = a0 << 2;
            a0 = sp94;

            v1 = v1 + a0;
            a0 = v1 + 0x3L;
            at = 0x800f_0000L;
            at = at + a0;
            a1 = MEMORY.ref(1, at).offset(-0xe90L).get();

            v1 = a1 >>> 2;
            a0 = v1 & 0xffL;
            v1 = spae;

            a0 = v1 - a0;
            MEMORY.ref(2, v0).offset(0xaL).setu(a0);
            v0 = sp10;
            v1 = sp98;

            a0 = v1;
            v1 = a0 << 2;
            a0 = sp94;

            v1 = v1 + a0;
            a0 = v1 + 0x2L;
            at = 0x800f_0000L;
            at = at + a0;
            a1 = MEMORY.ref(1, at).offset(-0xe90L).get();

            v1 = a1 >>> 2;
            a0 = v1 & 0xffL;
            a1 = spac;

            v1 = a1 - a0;
            a0 = sp98;

            a1 = a0;
            a0 = a1 << 2;
            a1 = sp94;

            a0 = a0 + a1;
            a1 = a0 + 0x2L;
            at = 0x800f_0000L;
            at = at + a1;
            a2 = MEMORY.ref(1, at).offset(-0xe90L).get();

            a0 = a2 >>> 1;
            a1 = a0 & 0xffL;
            a0 = v1 + a1;
            MEMORY.ref(2, v0).offset(0x10L).setu(a0);
            v0 = sp10;
            v1 = sp98;

            a0 = v1;
            v1 = a0 << 2;
            a0 = sp94;

            v1 = v1 + a0;
            a0 = v1 + 0x3L;
            at = 0x800f_0000L;
            at = at + a0;
            a1 = MEMORY.ref(1, at).offset(-0xe90L).get();

            v1 = a1 >>> 2;
            a0 = v1 & 0xffL;
            v1 = spae;

            a0 = v1 - a0;
            MEMORY.ref(2, v0).offset(0x12L).setu(a0);
            v0 = sp10;
            v1 = sp98;

            a0 = v1;
            v1 = a0 << 2;
            a0 = sp94;

            v1 = v1 + a0;
            a0 = v1 + 0x2L;
            at = 0x800f_0000L;
            at = at + a0;
            a1 = MEMORY.ref(1, at).offset(-0xe90L).get();

            v1 = a1 >>> 2;
            a0 = v1 & 0xffL;
            v1 = spac;

            a0 = v1 - a0;
            MEMORY.ref(2, v0).offset(0x18L).setu(a0);
            v0 = sp10;
            v1 = sp98;

            a0 = v1;
            v1 = a0 << 2;
            a0 = sp94;

            v1 = v1 + a0;
            a0 = v1 + 0x3L;
            at = 0x800f_0000L;
            at = at + a0;
            a1 = MEMORY.ref(1, at).offset(-0xe90L).get();

            v1 = a1 >>> 2;
            a0 = v1 & 0xffL;
            a1 = spae;

            v1 = a1 - a0;
            a0 = sp98;

            a1 = a0;
            a0 = a1 << 2;
            a1 = sp94;

            a0 = a0 + a1;
            a1 = a0 + 0x2L;
            at = 0x800f_0000L;
            at = at + a1;
            a2 = MEMORY.ref(1, at).offset(-0xe90L).get();

            a0 = a2 >>> 1;
            a1 = a0 & 0xffL;
            a0 = v1 + a1;
            MEMORY.ref(2, v0).offset(0x1aL).setu(a0);
            v0 = sp10;
            v1 = sp98;

            a0 = v1;
            v1 = a0 << 2;
            a0 = sp94;

            v1 = v1 + a0;
            a0 = v1 + 0x2L;
            at = 0x800f_0000L;
            at = at + a0;
            a1 = MEMORY.ref(1, at).offset(-0xe90L).get();

            v1 = a1 >>> 2;
            a0 = v1 & 0xffL;
            a1 = spac;

            v1 = a1 - a0;
            a0 = sp98;

            a1 = a0;
            a0 = a1 << 2;
            a1 = sp94;

            a0 = a0 + a1;
            a1 = a0 + 0x2L;
            at = 0x800f_0000L;
            at = at + a1;
            a2 = MEMORY.ref(1, at).offset(-0xe90L).get();

            a0 = a2 >>> 1;
            a1 = a0 & 0xffL;
            a0 = v1 + a1;
            MEMORY.ref(2, v0).offset(0x20L).setu(a0);
            v0 = sp10;
            v1 = sp98;

            a0 = v1;
            v1 = a0 << 2;
            a0 = sp94;

            v1 = v1 + a0;
            a0 = v1 + 0x3L;
            at = 0x800f_0000L;
            at = at + a0;
            a1 = MEMORY.ref(1, at).offset(-0xe90L).get();

            v1 = a1 >>> 2;
            a0 = v1 & 0xffL;
            a1 = spae;

            v1 = a1 - a0;
            a0 = sp98;

            a1 = a0;
            a0 = a1 << 2;
            a1 = sp94;

            a0 = a0 + a1;
            a1 = a0 + 0x2L;
            at = 0x800f_0000L;
            at = at + a1;
            a2 = MEMORY.ref(1, at).offset(-0xe90L).get();

            a0 = a2 >>> 1;
            a1 = a0 & 0xffL;
            a0 = v1 + a1;
            MEMORY.ref(2, v0).offset(0x22L).setu(a0);
            v0 = spa8;

            v1 = v0;
            v0 = v1 << 2;
            v1 = 0x1f80_0000L;
            v1 = MEMORY.ref(4, v1).offset(0x3d0L).get();

            v0 = v0 + v1;
            v1 = v0 + 0x28L;
            a0 = v1;
            a1 = sp10;

            insertElementIntoLinkedList(a0, a1);
          }

          //LAB_800d84b0
          v0 = sp10;

          v1 = v0 + 0x28L;
          sp10 = v1;
        }
      }

      //LAB_800d84c0
    }

    //LAB_800d84d8
    v0 = sp10;
    at = 0x1f80_0000L;
    MEMORY.ref(4, at).offset(0x3d8L).setu(v0);

    //LAB_800d84e8
    for(int i = 0; i < 0xffL; i++) {
      //LAB_800d8504
      sp0xb0[i] = 0;
    }

    //LAB_800d852c
    v0 = 0x1f80_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x3d8L).get();

    sp10 = v0;

    //LAB_800d8540
    for(int i = 0; i < _800c67a0.get(); i++) {
      //LAB_800d8564
      v0 = FUN_800eb09c(i, 0, null);
      if(v0 == 0) {
        //LAB_800d8584
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x6798L).get();
        v1 = 0x7L;
        if(v0 != v1 || i == 0x1fL || i == 0x4eL) {
          //LAB_800d85c0
          v0 = i;

          a0 = v0;
          v1 = a0 << 2;
          v1 = v1 + v0;
          v0 = v1 << 2;
          at = 0x800f_0000L;
          at = at + v0;
          v1 = MEMORY.ref(2, at).offset(0xe34L).getSigned();

          v0 = v1;
          v1 = v0 << 3;
          at = 0x800f_0000L;
          at = at + v1;
          v0 = MEMORY.ref(2, at).offset(0x2248L).getSigned();

          sp88 = v0;
          a0 = sp88;

          v0 = Math.abs(a0);
          v1 = v0 - 0x1L;
          sp80 = v1;
          v1 = sp0xb0[(int)sp80];

          if(v1 == 0) {
            //LAB_800d863c
            v1 = 0x1L;
            sp0xb0[(int)sp80] = v1;
            v0 = sp80;

            v1 = v0;
            v0 = v1 << 2;
            at = 0x800f_0000L;
            at = at + v0;
            v1 = MEMORY.ref(4, at).offset(0x5810L).get();

            v0 = v1 - 0x1L;
            sp84 = v0;
            v0 = sp80;

            v1 = v0;
            v0 = v1 << 2;
            at = 0x800f_0000L;
            at = at + v0;
            v1 = MEMORY.ref(4, at).offset(0x591cL).get();

            sp38 = v1;
            v0 = sp88;

            if((int)v0 < 0) {
              v0 = sp84;

              v1 = v0;
              v0 = v1 << 4;
              v1 = v0 - 0x10L;
              v0 = sp38;

              v1 = v0 + v1;
              sp38 = v1;
            }

            //LAB_800d86d0
            //LAB_800d86d4
            for(int n = 0; n < sp84; n++) {
              //LAB_800d86f4
              v0 = sp88;

              if((int)v0 > 0) {
                v0 = n;

                v1 = v0;
                v0 = v1 << 4;
                v1 = sp38;

                v0 = v0 + v1;
                v1 = MEMORY.ref(2, v0).offset(0x0L).get();

                sp0x70.setX((short)v1);
                v0 = n;

                v1 = v0;
                v0 = v1 << 4;
                v1 = sp38;

                v0 = v0 + v1;
                v1 = MEMORY.ref(2, v0).offset(0x4L).get();

                sp0x70.setY((short)v1);
                v0 = n;

                v1 = v0;
                v0 = v1 << 4;
                v1 = sp38;

                v0 = v0 + v1;
                v1 = MEMORY.ref(2, v0).offset(0x8L).get();

                sp0x70.setZ((short)v1);
              } else {
                //LAB_800d8784
                v0 = n;

                v1 = v0;
                v0 = v1 << 4;
                v1 = sp38;

                v0 = v1 - v0;
                v1 = MEMORY.ref(2, v0).offset(0x0L).get();

                sp0x70.setX((short)v1);
                v0 = n;

                v1 = v0;
                v0 = v1 << 4;
                v1 = sp38;

                v0 = v1 - v0;
                v1 = MEMORY.ref(2, v0).offset(0x4L).get();

                sp0x70.setY((short)v1);
                v0 = n;

                v1 = v0;
                v0 = v1 << 4;
                v1 = sp38;

                v0 = v1 - v0;
                v1 = MEMORY.ref(2, v0).offset(0x8L).get();

                sp0x70.setZ((short)v1);
              }

              //LAB_800d87fc
              CPU.MTC2(sp0x70.getXY(), 0);
              CPU.MTC2(sp0x70.getZ(), 1);
              CPU.COP2(0x18_0001L);

              sp9c.setXY(CPU.MFC2(14));
              spa4 = CPU.MFC2(8);
              t4 = CPU.CFC2(31);

              spa0 = t4;
              t4 = CPU.MFC2(19);

              t4 = (int)t4 >> 2;
              spa8 = t4;
              v0 = spa8;

              if((int)v0 >= 0x4L) {
                v0 = spa8;
                v1 = 0x1f80_0000L;
                v1 = MEMORY.ref(4, v1).offset(0x3c8L).get();

                if((int)v0 < (int)v1) {
                  a0 = 0xcL;
                  a1 = sp10;

                  setGpuPacketType(a0, a1, true, false);
                  v0 = sp10;
                  v1 = 0x800c_0000L;
                  v1 = MEMORY.ref(2, v1).offset(-0x4eeaL).get();

                  a0 = v1 | 0xaL;
                  MEMORY.ref(2, v0).offset(0x16L).setu(a0);
                  v0 = sp10;
                  v1 = 0x7c28L;
                  MEMORY.ref(2, v0).offset(0xeL).setu(v1);
                  v0 = 0x800c_0000L;
                  v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

                  v1 = MEMORY.ref(1, v0).offset(0x1f8L).get();

                  if(v1 == 0) {
                    v0 = sp0x70.getX();
                    v1 = sp60;

                    v0 = v1 - v0;
                    sp50 = v0;
                    v0 = sp0x70.getY();
                    v1 = sp64;

                    v0 = v1 - v0;
                    sp54 = v0;
                    v0 = sp0x70.getZ();
                    v1 = sp68;

                    v0 = v1 - v0;
                    sp58 = v0;
                    v0 = sp50;
                    v1 = sp50;

                    lo = ((long)(int)v0 * (int)v1) & 0xffff_ffffL;
                    t0 = lo;
                    sp1c4 = t0;
                    v0 = sp54;
                    a0 = sp54;

                    lo = ((long)(int)v0 * (int)a0) & 0xffff_ffffL;
                    v1 = lo;
                    t0 = sp1c4;

                    v0 = t0 + v1;
                    v1 = sp58;
                    a0 = sp58;

                    lo = ((long)(int)v1 * (int)a0) & 0xffff_ffffL;
                    t0 = lo;
                    sp1c4 = t0;
                    t0 = sp1c4;

                    v0 = v0 + t0;
                    a0 = v0;

                    v0 = SquareRoot0(a0);
                    sp8c = v0;
                    v0 = 0x200L;
                    v1 = sp8c;

                    v0 = v0 - v1;
                    sp90 = v0;
                    v0 = sp90;

                    if((int)v0 < 0) {
                      sp90 = 0;
                    }

                    //LAB_800d89a0
                    v0 = sp90;

                    v1 = (int)v0 >> 31;
                    a0 = v1 >>> 31;
                    v1 = v0 + a0;
                    v0 = (int)v1 >> 1;
                    sp90 = v0;
                    v0 = sp10;
                    v1 = sp90;

                    a1 = v1;
                    a0 = a1 << 1;
                    a0 = a0 + v1;
                    a1 = a0 << 4;
                    a1 = a1 - v1;
                    v1 = a1;
                    if((int)v1 < 0) {
                      v1 = v1 + 0xffL;
                    }

                    //LAB_800d89ec
                    v1 = (int)v1 >> 8;
                    MEMORY.ref(1, v0).offset(0x4L).setu(v1);
                    v0 = sp10;
                    v1 = sp90;

                    a1 = v1;
                    a0 = a1 << 2;
                    a0 = a0 + v1;
                    a1 = a0 << 3;
                    a1 = a1 - v1;
                    v1 = a1;
                    if((int)v1 < 0) {
                      v1 = v1 + 0xffL;
                    }

                    //LAB_800d8a24
                    v1 = (int)v1 >> 8;
                    MEMORY.ref(1, v0).offset(0x5L).setu(v1);
                    v0 = sp10;

                    MEMORY.ref(1, v0).offset(0x6L).setu(0);
                    v0 = sp10;
                    v1 = spac;

                    a0 = v1 - 0x2L;
                    MEMORY.ref(2, v0).offset(0x8L).setu(a0);
                    v0 = sp10;
                    v1 = spae;

                    a0 = v1 - 0x2L;
                    MEMORY.ref(2, v0).offset(0xaL).setu(a0);
                    v0 = sp10;
                    v1 = spac;

                    a0 = v1 + 0x2L;
                    MEMORY.ref(2, v0).offset(0x10L).setu(a0);
                    v0 = sp10;
                    v1 = spae;

                    a0 = v1 - 0x2L;
                    MEMORY.ref(2, v0).offset(0x12L).setu(a0);
                    v0 = sp10;
                    v1 = spac;

                    a0 = v1 - 0x2L;
                    MEMORY.ref(2, v0).offset(0x18L).setu(a0);
                    v0 = sp10;
                    v1 = spae;

                    a0 = v1 + 0x2L;
                    MEMORY.ref(2, v0).offset(0x1aL).setu(a0);
                    v0 = sp10;
                    v1 = spac;

                    a0 = v1 + 0x2L;
                    MEMORY.ref(2, v0).offset(0x20L).setu(a0);
                    v0 = sp10;
                    v1 = spae;

                    a0 = v1 + 0x2L;
                    MEMORY.ref(2, v0).offset(0x22L).setu(a0);
                    v0 = sp10;
                    v1 = 0x30L;
                    MEMORY.ref(1, v0).offset(0xcL).setu(v1);
                    v0 = sp10;

                    MEMORY.ref(1, v0).offset(0xdL).setu(0);
                    v0 = sp10;
                    v1 = 0x3fL;
                    MEMORY.ref(1, v0).offset(0x14L).setu(v1);
                    v0 = sp10;

                    MEMORY.ref(1, v0).offset(0x15L).setu(0);
                    v0 = sp10;
                    v1 = 0x30L;
                    MEMORY.ref(1, v0).offset(0x1cL).setu(v1);
                    v0 = sp10;
                    v1 = 0xfL;
                    MEMORY.ref(1, v0).offset(0x1dL).setu(v1);
                    v0 = sp10;
                    v1 = 0x3fL;
                    MEMORY.ref(1, v0).offset(0x24L).setu(v1);
                    v0 = sp10;
                    v1 = 0xfL;
                    MEMORY.ref(1, v0).offset(0x25L).setu(v1);
                  } else {
                    //LAB_800d8b40
                    v0 = sp10;
                    v1 = 0x2fL;
                    MEMORY.ref(1, v0).offset(0x4L).setu(v1);
                    v0 = sp10;
                    v1 = 0x27L;
                    MEMORY.ref(1, v0).offset(0x5L).setu(v1);
                    v0 = sp10;

                    MEMORY.ref(1, v0).offset(0x6L).setu(0);
                    v0 = sp10;
                    v1 = spac;

                    a0 = v1 - 0x1L;
                    MEMORY.ref(2, v0).offset(0x8L).setu(a0);
                    v0 = sp10;
                    v1 = spae;

                    a0 = v1 - 0x1L;
                    MEMORY.ref(2, v0).offset(0xaL).setu(a0);
                    v0 = sp10;
                    v1 = spac;

                    a0 = v1 + 0x2L;
                    MEMORY.ref(2, v0).offset(0x10L).setu(a0);
                    v0 = sp10;
                    v1 = spae;

                    a0 = v1 - 0x1L;
                    MEMORY.ref(2, v0).offset(0x12L).setu(a0);
                    v0 = sp10;
                    v1 = spac;

                    a0 = v1 - 0x1L;
                    MEMORY.ref(2, v0).offset(0x18L).setu(a0);
                    v0 = sp10;
                    v1 = spae;

                    a0 = v1 + 0x2L;
                    MEMORY.ref(2, v0).offset(0x1aL).setu(a0);
                    v0 = sp10;
                    v1 = spac;

                    a0 = v1 + 0x2L;
                    MEMORY.ref(2, v0).offset(0x20L).setu(a0);
                    v0 = sp10;
                    v1 = spae;

                    a0 = v1 + 0x2L;
                    MEMORY.ref(2, v0).offset(0x22L).setu(a0);
                    v0 = sp10;
                    v1 = 0x10L;
                    MEMORY.ref(1, v0).offset(0xcL).setu(v1);
                    v0 = sp10;
                    v1 = 0x18L;
                    MEMORY.ref(1, v0).offset(0xdL).setu(v1);
                    v0 = sp10;
                    v1 = 0x17L;
                    MEMORY.ref(1, v0).offset(0x14L).setu(v1);
                    v0 = sp10;
                    v1 = 0x18L;
                    MEMORY.ref(1, v0).offset(0x15L).setu(v1);
                    v0 = sp10;
                    v1 = 0x10L;
                    MEMORY.ref(1, v0).offset(0x1cL).setu(v1);
                    v0 = sp10;
                    v1 = 0x1fL;
                    MEMORY.ref(1, v0).offset(0x1dL).setu(v1);
                    v0 = sp10;
                    v1 = 0x17L;
                    MEMORY.ref(1, v0).offset(0x24L).setu(v1);
                    v0 = sp10;
                    v1 = 0x1fL;
                    MEMORY.ref(1, v0).offset(0x25L).setu(v1);
                  }

                  //LAB_800d8c64
                  spac = sp9c.getX();
                  spae = sp9c.getY();
                  v0 = spa8;

                  v1 = v0;
                  v0 = v1 << 2;
                  v1 = 0x1f80_0000L;
                  v1 = MEMORY.ref(4, v1).offset(0x3d0L).get();

                  v0 = v0 + v1;
                  v1 = v0 + 0x28L;
                  a0 = v1;
                  a1 = sp10;

                  insertElementIntoLinkedList(a0, a1);
                }
              }

              //LAB_800d8cb8
              sp10 += 0x28L;
            }
          }
        }
      }

      //LAB_800d8ce0
    }

    //LAB_800d8cf8
    v0 = sp10;
    at = 0x1f80_0000L;
    MEMORY.ref(4, at).offset(0x3d8L).setu(v0);

    //LAB_800d8d04
  }

  @Method(0x800d8d18L)
  public static void FUN_800d8d18() {
    FUN_800d8e4c(_800c6798.get());

    struct258_800c66a8.deref()._1f8.set(0);
    struct258_800c66a8.deref()._220.set(0);

    final Memory.TemporaryReservation sp0x48tmp = MEMORY.temp(4);
    final Memory.TemporaryReservation sp0x50tmp = MEMORY.temp(8);
    final Memory.TemporaryReservation sp0x58tmp = MEMORY.temp(8);
    final COLOUR sp0x48 = sp0x48tmp.get().cast(COLOUR::new);
    final Value sp0x50 = sp0x50tmp.get();
    final Value sp0x58 = sp0x58tmp.get();

    fillMemory(sp0x48.getAddress(), 0, 0x4L);

    sp0x50.setu(_800c8778);
    sp0x58.offset(0x0L).setu(_800c877c.offset(0x0L));
    sp0x58.offset(0x4L).setu(_800c877c.offset(0x4L));

    struct258_800c66a8.deref()._1fc.set(FUN_800cd3c8(
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
      0x1L,
      0x9L,
      0xdL,
      0,
      0,
      0
    ));

    sp0x48tmp.release();
    sp0x50tmp.release();
    sp0x58tmp.release();
  }

  @Method(0x800d8e4cL)
  public static void FUN_800d8e4c(final long a0) {
    _800c66b8.and(0xffff_fffdL);
    loadDrgnBinFile(0, 5697L + a0, 0, getMethodAddress(WMap.class, "FUN_800d5858", Value.class, long.class, long.class), 0x2L, 0x4L);
    loadDrgnBinFile(0, 5705L + a0, 0, getMethodAddress(WMap.class, "loadTmdCallback", Value.class, long.class, long.class), 0, 0x2L);
  }

  @Method(0x800d8efcL)
  public static void FUN_800d8efc() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long t0;
    long hi;
    long sp18;
    long sp1c;

    final RECT sp0x10 = new RECT((short)448, (short)0, (short)64, (short)64);
    v0 = FUN_800d5e70(sp0x10, 0, 0x3L, 0x1L);
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

    MEMORY.ref(4, v1).offset(0x1cL).setu(v0);
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

    MEMORY.ref(4, v0).offset(0x28L).setu(0);
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x6798L).get();
    v1 = 0x2L;
    if(v0 == v1) {
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

      v1 = MEMORY.ref(4, v0).offset(0x8L).get();

      v0 = MEMORY.ref(4, v1).offset(0x10L).get();

      sp1c = v0;
      sp18 = 0;

      //LAB_800d8f94
      do {
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x8L).get();
        v0 = sp18;
        v1 = MEMORY.ref(4, v1).offset(0xcL).get();

        if((int)v0 >= (int)v1) {
          break;
        }

        //LAB_800d8fc4
        v0 = rand();
        v1 = v0;
        v0 = sp1c;
        a0 = 0x8008_0000L;
        a0 = a0 | 0x81L;
        hi = ((long)(int)v1 * (int)a0) >>> 32; //TODO
        t0 = hi;
        a0 = t0 + v1;
        a1 = (int)a0 >> 11;
        a2 = (int)v1 >> 31;
        a0 = a1 - a2;
        a2 = a0;
        a1 = a2 << 12;
        a1 = a1 - a0;
        v1 = v1 - a1;
        MEMORY.ref(4, v0).offset(0x0L).setu(v1);
        v0 = sp18;

        v1 = v0 + 0x1L;
        sp18 = v1;
        v0 = sp1c;

        v1 = v0 + 0x4L;
        sp1c = v1;
      } while(true);
    }

    //LAB_800d9030
  }

  @Method(0x800d9044L) // Renders the map (anything else?)
  public static void FUN_800d9044() {
    final MATRIX sp0x10 = new MATRIX();
    final MATRIX sp0x30 = new MATRIX();

    FUN_800d94cc();
    FUN_800da248();

    if(struct258_800c66a8.deref()._220.get() >= 0x2L && struct258_800c66a8.deref()._220.get() < 0x8L) {
      return;
    }

    //LAB_800d90a8
    if(struct258_800c66a8.deref()._1f8.get() == 0x4L) {
      return;
    }

    //LAB_800d90cc
    //LAB_800d9150
    for(int i = 0; i < struct258_800c66a8.deref().tmdRendering_08.deref().count_0c.get(); i++) {
      final GsDOBJ2 dobj2 = struct258_800c66a8.deref().tmdRendering_08.deref().dobj2s_00.deref().get(i);
      final GsCOORDINATE2 coord2 = struct258_800c66a8.deref().tmdRendering_08.deref().coord2s_04.deref().get(i);
      final SVECTOR rotation = struct258_800c66a8.deref().tmdRendering_08.deref().rotations_08.deref().get(i);
      final UnsignedIntRef sp60 = struct258_800c66a8.deref().tmdRendering_08.deref()._10.deref().get(i);

      //LAB_800d9180
      if(_800c6798.get() != 0x7L) {
        //LAB_800d91cc
        if(_800ef194.offset(_800c6798.get()).get() == i || _800ef19c.offset(_800c6798.get()).get() == i) {
          _1f8003e8.setu(500L);
        } else {
          //LAB_800d9204
          _1f8003e8.setu(100L);
        }
      }

      //LAB_800d9210
      rotateCoord2(rotation, coord2);

      if(_800c6798.get() == 0x2L) {
        //LAB_800d9264
        if(i >= 0x2L && i < 0x9L || i >= 0xfL && i < 0x11L) {
          //LAB_800d9294
          final int sin = (int)(rsin(sp60.get()) * 0x20) / 0x1000;
          if((i & 0x1L) != 0) {
            coord2.coord.transfer.setY(sin);
          } else {
            //LAB_800d92d8
            coord2.coord.transfer.setY(-sin);
          }

          //LAB_800d9304
          sp60.add(0x8L);
        }
      }

      //LAB_800d9320
      GsGetLws(dobj2.coord2_04.deref(), sp0x10, sp0x30);
      GsSetLightMatrix(sp0x10);
      setRotTransMatrix(sp0x30);

      if((int)_800c6798.get() < 0x9L && i == 0) {
        _800c66d8.setu(_1f8003c8.get() - 0x3L);
        FUN_800dd05c(dobj2);
        _800c66d8.setu(0);
        //LAB_800d93c0
      } else {
        //LAB_800d93b4
        //LAB_800d93c8
        FUN_80021258(dobj2);
      }

      //LAB_800d93d4
    }

    //LAB_800d942c
    if((int)_800c6798.get() < 0x9L) {
      FUN_800d6080(struct258_800c66a8.deref()._1c.get());
    }

    //LAB_800d945c
    struct258_800c66a8.deref()._28.incr();

    if((int)struct258_800c66a8.deref()._28.get() >= 0xeL) {
      struct258_800c66a8.deref()._28.set(0);
    }

    //LAB_800d94b8
  }

  @Method(0x800d94ccL)
  public static void FUN_800d94cc() {
    long at;
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long sp14;
    long sp10;
    long sp20;
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66b8L).get();

    v0 = v1 & 0x1L;
    if(v0 == 0) {
      return;
    }

    //LAB_800d94f8
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

    v1 = MEMORY.ref(1, v0).offset(0x1f8L).get();

    if(v1 == 0) {
      return;
    }

    //LAB_800d951c
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

    v1 = MEMORY.ref(4, v0).offset(0x250L).get();

    if(v1 != 0) {
      return;
    }

    //LAB_800d9540
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x6798L).get();
    v1 = 0x7L;
    if(v0 == v1) {
      return;
    }

    //LAB_800d955c
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

    v1 = MEMORY.ref(1, v0).offset(0x1f8L).get();

    switch((int)v1) {
      case 1:
        v1 = 0x8008_0000L;
        v1 = MEMORY.ref(4, v1).offset(-0x5c68L).get();

        v0 = v1 & 0x2L;
        if(v0 != 0) {
          sp10 = 0;
          sp14 = 0;
          a0 = 0;
          a1 = 0x4L;
          a2 = 0;
          a3 = 0;

          playSound((int)a0, (int)a1, a2, a3, (short)sp10, (short)sp14);
          v1 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

          v0 = v1 + 0x1e8L;
          v1 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

          a0 = MEMORY.ref(2, v1).offset(0x38L).get();

          MEMORY.ref(2, v0).offset(0x0L).setu(a0);
          v1 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

          v0 = v1 + 0x1e8L;
          v1 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

          a0 = MEMORY.ref(2, v1).offset(0x3cL).get();

          MEMORY.ref(2, v0).offset(0x2L).setu(a0);
          v1 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

          v0 = v1 + 0x1e8L;
          v1 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

          a0 = MEMORY.ref(2, v1).offset(0x40L).get();

          MEMORY.ref(2, v0).offset(0x4L).setu(a0);
          a0 = 0x1L;

          FUN_800d9d24(a0);
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
          v1 = 0x2L;
          MEMORY.ref(1, v0).offset(0x1f8L).setu(v1);
          at = 0x800f_0000L;
          MEMORY.ref(2, at).offset(-0xe5cL).setu(0);
        }

        //LAB_800d9674
        break;

      case 2:
        v0 = 0x800f_0000L;
        v0 = MEMORY.ref(2, v0).offset(-0xe5cL).get();

        v1 = v0 + 0x10L;
        at = 0x800f_0000L;
        MEMORY.ref(2, at).offset(-0xe5cL).setu(v1);
        v0 = 0x800f_0000L;
        v0 = MEMORY.ref(2, v0).offset(-0xe5cL).getSigned();

        if((int)v0 >= 0x81L) {
          v0 = 0x80L;
          at = 0x800f_0000L;
          MEMORY.ref(2, at).offset(-0xe5cL).setu(v0);
        }

        //LAB_800d96b8
        FUN_800d9eb0();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(1, v1).offset(0x1f9L).get();

        v1 = a0 + 0x1L;
        a0 = v1;
        MEMORY.ref(1, v0).offset(0x1f9L).setu(a0);
        v0 = a0 & 0xffL;
        if(v0 >= 0x6L) {
          v1 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

          v0 = v1 + 0x20L;
          v1 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v1).offset(0x6798L).get();

          a0 = v1;
          v1 = a0 << 4;
          at = 0x800f_0000L;
          at = at + v1;
          a0 = MEMORY.ref(4, at).offset(-0xe58L).get();

          MEMORY.ref(4, v0).offset(0x18L).setu(a0);
          v1 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

          v0 = v1 + 0x20L;
          v1 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v1).offset(0x6798L).get();

          a0 = v1;
          v1 = a0 << 4;
          at = 0x800f_0000L;
          at = at + v1;
          a0 = MEMORY.ref(4, at).offset(-0xe54L).get();

          MEMORY.ref(4, v0).offset(0x1cL).setu(a0);
          v1 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

          v0 = v1 + 0x20L;
          v1 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v1).offset(0x6798L).get();

          a0 = v1;
          v1 = a0 << 4;
          at = 0x800f_0000L;
          at = at + v1;
          a0 = MEMORY.ref(4, at).offset(-0xe50L).get();

          MEMORY.ref(4, v0).offset(0x20L).setu(a0);
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
          v1 = 0x3L;
          MEMORY.ref(1, v0).offset(0x1f8L).setu(v1);

          //LAB_800d97bc
          for(int i = 0; i < 7; i++) {
            //LAB_800d97d8
            FUN_8002a3ec(i, 0);
          }
        }

        //LAB_800d9808
        break;

      case 3:
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
        v1 = 0x4L;
        MEMORY.ref(1, v0).offset(0x1f8L).setu(v1);

      case 4:
        v1 = 0x8008_0000L;
        v1 = MEMORY.ref(4, v1).offset(-0x5c68L).get();

        v0 = v1 & 0x2L;
        if(v0 != 0) {
          sp10 = 0;
          sp14 = 0;
          a0 = 0;
          a1 = 0x28L;
          a2 = 0;
          a3 = 0;

          playSound((int)a0, (int)a1, a2, a3, (short)sp10, (short)sp14);
        }

        //LAB_800d9858
        //LAB_800d985c
        for(int i = 0; i < 6; i++) {
          //LAB_800d9878
          FUN_8002a3ec(i, 0);
        }

        //LAB_800d98a8
        v1 = 0x8008_0000L;
        v1 = MEMORY.ref(4, v1).offset(-0x5c68L).get();

        v0 = v1 & 0x1L;
        if(v0 != 0) {
          playSound(0, 4, 0, 0, (short)0, (short)0);
          FUN_800d9d24(-0x1L);

          struct258_800c66a8.deref()._1f8.set(5);

          //LAB_800d9900
          for(int i = 0; i < 3; i++) {
            //LAB_800d991c
            v1 = _800c66b0.deref().colour_8c.get(i).r.get();

            if((int)v1 < 0) {
              v1 = v1 + 0x3L;
            }

            //LAB_800d996c
            _800c66b0.deref().lights_11c.get(i).r_0c.set((int)(v1 / 4));

            v1 = _800c66b0.deref().colour_8c.get(i).g.get();

            if((int)v1 < 0) {
              v1 = v1 + 0x3L;
            }

            //LAB_800d99c4
            _800c66b0.deref().lights_11c.get(i).g_0d.set((int)(v1 / 4));

            v1 = _800c66b0.deref().colour_8c.get(i).b.get();

            if((int)v1 < 0) {
              v1 = v1 + 0x3L;
            }

            //LAB_800d9a1c
            _800c66b0.deref().lights_11c.get(i).b_0e.set((int)(v1 / 4));

            GsSetFlatLight(i, _800c66b0.deref().lights_11c.get(i));
          }

          //LAB_800d9a70
          if((joypadInput_8007a39c.get() & 0x800L) != 0) {
            //LAB_800d9a8c
            for(sp20 = 0; sp20 < 8; sp20++) {
              //LAB_800d9aa8
              _800c86d4.offset(sp20 * 0x4L).setu(0);
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

        struct258_800c66a8.deref()._1f9.incr();

        if(struct258_800c66a8.deref()._1f9.get() >= 0x6L) {
          _800c66b0.deref().coord2_20.coord.transfer.setX(struct258_800c66a8.deref().svec_1e8.getX());
          _800c66b0.deref().coord2_20.coord.transfer.setY(struct258_800c66a8.deref().svec_1e8.getY());
          _800c66b0.deref().coord2_20.coord.transfer.setZ(struct258_800c66a8.deref().svec_1e8.getZ());
          struct258_800c66a8.deref()._1f8.set(6);
        }

        //LAB_800d9be8
        break;

      case 6:
        if((joypadPress_8007a398.get() & 0x2L) != 0) {
          playSound(0, 4, 0, 0, (short)0, (short)0);

          struct258_800c66a8.deref().svec_1e8.setX((short)_800c66b0.deref().coord2_20.coord.transfer.getX());
          struct258_800c66a8.deref().svec_1e8.setY((short)_800c66b0.deref().coord2_20.coord.transfer.getY());
          struct258_800c66a8.deref().svec_1e8.setZ((short)_800c66b0.deref().coord2_20.coord.transfer.getZ());

          FUN_800d9d24(0x1L);

          struct258_800c66a8.deref()._1f8.set(2);
          _800ef1a4.setu(0);
        }

        //LAB_800d9cc4
        break;
    }

    //LAB_800d9ccc
    FUN_800e4934(mcqHeader_800c6768, 320L, 0, -160L, -120L, 30L, 1L, _800ef1a4.get() & 0xff);

    //LAB_800d9d10
  }

  @Method(0x800d9d24L)
  public static void FUN_800d9d24(long a0) {
    assert false;
  }

  @Method(0x800d9eb0L)
  public static void FUN_800d9eb0() {
    assert false;
  }

  @Method(0x800da248L)
  public static void FUN_800da248() {
    long at;
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long sp10;
    long sp14;
    long sp18;
    long sp1c;
    long sp20;
    long sp24;
    Ref<Long> sp0x28 = new Ref<>();
    Ref<Long> sp0x2c = new Ref<>();
    VECTOR sp0x38 = new VECTOR();
    VECTOR sp0x48 = new VECTOR();
    long sp58;
    long sp5c;
    long sp60;
    long sp68;
    long sp6c;
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x6894L).get();
    v1 = 0x1L;
    if(v0 == v1) {
      return;
    }

    //LAB_800da270
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

    v1 = MEMORY.ref(1, v0).offset(0x5L).get();

    if(v1 != 0) {
      return;
    }

    //LAB_800da294
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();

    v1 = MEMORY.ref(1, v0).offset(0x110L).get();

    if(v1 != 0) {
      return;
    }

    //LAB_800da2b8
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

    v1 = MEMORY.ref(1, v0).offset(0x1f8L).get();

    if(v1 != 0) {
      return;
    }

    //LAB_800da2dc
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();

    v1 = MEMORY.ref(1, v0).offset(0xc5L).get();

    if(v1 != 0) {
      return;
    }

    //LAB_800da300
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();

    v1 = MEMORY.ref(1, v0).offset(0xc4L).getSigned();

    if(v1 != 0) {
      return;
    }

    //LAB_800da324
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66b8L).get();

    v0 = v1 & 0x1L;
    if(v0 == 0) {
      return;
    }

    //LAB_800da344
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x6690L).get();

    if(v0 != 0) {
      return;
    }

    //LAB_800da360
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

    v1 = MEMORY.ref(4, v0).offset(0x1e4L).get();
    v0 = 0x1L;
    if(v1 == v0) {
      v0 = 0x800c_0000L;
      v0 = v0 - 0x537cL;
      sp60 = v0;
      v0 = 0x97L;
      sp58 = v0;
      v1 = sp58;

      v0 = v1 & 0x1fL;
      v1 = 0x1L;
      v0 = v1 << v0;
      v1 = v0;
      sp5c = v1;
      v1 = sp58;

      v0 = v1 >>> 5;
      v1 = v0;
      sp58 = v1;
      v0 = sp58;

      v1 = v0;
      v0 = v1 << 2;
      v1 = sp60;

      v0 = v0 + v1;
      v1 = MEMORY.ref(4, v0).offset(0x0L).get();
      a0 = sp5c;

      v0 = v1 & a0;
      if(v0 > 0) {
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x6870L).get();

        if(v0 == 0) {
          a0 = 0x1L;

          FUN_800d7208(a0);
        }
      }

      //LAB_800da418
      return;
    }

    //LAB_800da420
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

    v1 = MEMORY.ref(4, v0).offset(0x250L).get();
    v0 = 0x1L;
    if(v1 == v0) {
      return;
    }

    //LAB_800da444
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

    v1 = MEMORY.ref(4, v0).offset(0x1e4L).get();
    v0 = 0x1L;
    if(v1 == v0) {
      return;
    }

    //LAB_800da468
    v0 = 0x800c_0000L;
    v0 = v0 - 0x537cL;
    sp58 = v0;
    v0 = 0x15aL;
    sp60 = v0;
    v1 = sp60;

    v0 = v1 & 0x1fL;
    v1 = 0x1L;
    v0 = v1 << v0;
    v1 = v0;
    sp5c = v1;
    v1 = sp60;

    v0 = v1 >>> 5;
    v1 = v0;
    sp60 = v1;
    v0 = sp60;

    v1 = v0;
    v0 = v1 << 2;
    v1 = sp58;

    v0 = v0 + v1;
    v1 = MEMORY.ref(4, v0).offset(0x0L).get();
    a0 = sp5c;

    v0 = v1 & a0;
    if(v0 <= 0) {
      return;
    }

    //LAB_800da4ec
    a0 = 0;

    FUN_800d7208(a0);
    v1 = 0x8008_0000L;
    v1 = MEMORY.ref(4, v1).offset(-0x5c68L).get();

    v0 = v1 & 0x80L;
    if(v0 != 0) {
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
      v1 = 0x2L;
      MEMORY.ref(4, v0).offset(0x250L).setu(v1);
    }

    //LAB_800da520
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

    v1 = MEMORY.ref(4, v0).offset(0x250L).get();
    v0 = 0x2L;
    if(v1 != v0) {
      return;
    }

    //LAB_800da544
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

    v1 = MEMORY.ref(1, v0).offset(0x220L).get();

    switch((int)v1) {
      case 1:
        sp10 = 0;
        sp14 = 0;
        a0 = 0;
        a1 = 0x4L;
        a2 = 0;
        a3 = 0;

        playSound((int)a0, (int)a1, a2, a3, (short)sp10, (short)sp14);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = v1 + 0x200L;
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

        a0 = MEMORY.ref(2, v1).offset(0x38L).get();

        MEMORY.ref(2, v0).offset(0x0L).setu(a0);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = v1 + 0x200L;
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

        a0 = MEMORY.ref(2, v1).offset(0x3cL).get();

        MEMORY.ref(2, v0).offset(0x2L).setu(a0);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = v1 + 0x200L;
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

        a0 = MEMORY.ref(2, v1).offset(0x40L).get();

        MEMORY.ref(2, v0).offset(0x4L).setu(a0);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = v1 + 0x208L;
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(4, v1).offset(0x94L).get();

        v1 = (int)a0 >> 12;
        MEMORY.ref(2, v0).offset(0x0L).setu(v1);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = v1 + 0x208L;
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(4, v1).offset(0x98L).get();

        v1 = (int)a0 >> 12;
        MEMORY.ref(2, v0).offset(0x2L).setu(v1);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = v1 + 0x208L;
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(4, v1).offset(0x9cL).get();

        v1 = (int)a0 >> 12;
        MEMORY.ref(2, v0).offset(0x4L).setu(v1);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(2, v1).offset(0xa6L).get();

        MEMORY.ref(2, v0).offset(0x21cL).setu(a0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

        a0 = MEMORY.ref(2, v1).offset(0x72L).get();

        MEMORY.ref(2, v0).offset(0x21eL).setu(a0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        MEMORY.ref(1, v0).offset(0x223L).setu(0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
        v1 = 0x1L;
        MEMORY.ref(1, v0).offset(0x220L).setu(v1);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x14L).get();

        MEMORY.ref(2, v1).offset(0x74L).setu(0);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = MEMORY.ref(4, v1).offset(0x14L).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(2, v1).offset(0xa6L).get();

        MEMORY.ref(2, v0).offset(0x76L).setu(a0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x14L).get();

        MEMORY.ref(2, v1).offset(0x78L).setu(0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x14L).get();
        v0 = 0x400L;
        MEMORY.ref(4, v1).offset(0xfcL).setu(v0);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = v1 + 0x34L;
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(4, v1).offset(0x94L).get();

        v1 = (int)a0 >> 12;
        MEMORY.ref(4, v0).offset(0x18L).setu(v1);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = v1 + 0x34L;
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(4, v1).offset(0x98L).get();

        v1 = (int)a0 >> 12;
        MEMORY.ref(4, v0).offset(0x1cL).setu(v1);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = v1 + 0x34L;
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(4, v1).offset(0x9cL).get();

        v1 = (int)a0 >> 12;
        MEMORY.ref(4, v0).offset(0x20L).setu(v1);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x14L).get();

        v0 = v1 + 0x14L;
        a0 = 0x800c_0000L;
        a0 = MEMORY.ref(4, a0).offset(0x66a8L).get();

        v1 = a0 + 0x34L;
        a0 = MEMORY.ref(4, v1).offset(0x18L).get();

        MEMORY.ref(4, v0).offset(0x18L).setu(a0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x14L).get();

        v0 = v1 + 0x14L;
        a0 = 0x800c_0000L;
        a0 = MEMORY.ref(4, a0).offset(0x66a8L).get();

        v1 = a0 + 0x34L;
        a0 = MEMORY.ref(4, v1).offset(0x1cL).get();

        MEMORY.ref(4, v0).offset(0x1cL).setu(a0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x14L).get();

        v0 = v1 + 0x14L;
        a0 = 0x800c_0000L;
        a0 = MEMORY.ref(4, a0).offset(0x66a8L).get();

        v1 = a0 + 0x34L;
        a0 = MEMORY.ref(4, v1).offset(0x20L).get();

        MEMORY.ref(4, v0).offset(0x20L).setu(a0);

        //LAB_800da8a0
        for(int i = 0; i < 8; i++) {
          //LAB_800da8bc
          FUN_8002a3ec(i, 0);
        }

        //LAB_800da8ec
        sp20 = 0;

        //LAB_800da8f0
        do {
          v0 = sp20;

          if((int)v0 >= 0x8L) {
            break;
          }

          //LAB_800da90c
          v0 = sp20;

          v1 = v0;
          v0 = v1 << 2;
          at = 0x800d_0000L;
          at = at + v0;
          MEMORY.ref(4, at).offset(-0x792cL).setu(0);
          v0 = sp20;

          v1 = v0 + 0x1L;
          sp20 = v1;
        } while(true);

        //LAB_800da940
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(-0x4f04L).get();

        v1 = v0 & 0x3L;
        if(v1 == 0) {
          sp10 = 0;
          sp14 = 0;
          a0 = 0xcL;
          a1 = 0x1L;
          a2 = 0;
          a3 = 0;

          playSound((int)a0, (int)a1, a2, a3, (short)sp10, (short)sp14);
        }

        //LAB_800da978
        break;

      case 2:
        FUN_800e3304();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = MEMORY.ref(4, v1).offset(0x14L).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(4, v1).offset(0x14L).get();

        v1 = MEMORY.ref(4, a0).offset(0xfcL).get();

        a0 = v1 + 0x40L;
        MEMORY.ref(4, v0).offset(0xfcL).setu(a0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x14L).get();

        v0 = MEMORY.ref(4, v1).offset(0xfcL).get();

        if((int)v0 >= 0x601L) {
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

          v1 = MEMORY.ref(4, v0).offset(0x14L).get();
          v0 = 0x600L;
          MEMORY.ref(4, v1).offset(0xfcL).setu(v0);
        }

        //LAB_800da9fc
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = MEMORY.ref(4, v1).offset(0x14L).get();
        a0 = 0x800c_0000L;
        a0 = MEMORY.ref(4, a0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, a0).offset(0x14L).get();
        a0 = 0x800c_0000L;
        a0 = MEMORY.ref(4, a0).offset(0x66a8L).get();

        a1 = MEMORY.ref(4, a0).offset(0x14L).get();

        a0 = MEMORY.ref(4, a1).offset(0xfcL).get();

        MEMORY.ref(4, v1).offset(0x104L).setu(a0);
        MEMORY.ref(4, v0).offset(0x100L).setu(a0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(4, v1).offset(0x98L).get();
        v1 = 0xfffa_0000L;
        a0 = a0 + v1;
        MEMORY.ref(4, v0).offset(0x98L).setu(a0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

        a0 = MEMORY.ref(4, v1).offset(0x3cL).get();

        v1 = a0 - 0x60L;
        MEMORY.ref(4, v0).offset(0x3cL).setu(v1);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();

        v1 = MEMORY.ref(4, v0).offset(0x3cL).get();

        if((int)v1 < -0x5dcL) {
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
          v1 = -0x5dcL;
          MEMORY.ref(4, v0).offset(0x3cL).setu(v1);
        }

        //LAB_800daab8
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x98L).get();
        v0 = 0xff63_0000L;
        v0 = v0 | 0xc000L;
        if((int)v1 < (int)v0) {
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
          v1 = 0xff63_0000L;
          v1 = v1 | 0xc000L;
          MEMORY.ref(4, v0).offset(0x98L).setu(v1);
        }

        //LAB_800daaf0
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x98L).get();
        v0 = 0xff63_0000L;
        v0 = v0 | 0xc000L;
        if((int)v0 >= (int)v1) {
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();

          v1 = MEMORY.ref(4, v0).offset(0x3cL).get();

          if((int)v1 < -0x5dbL) {
            v0 = 0x800c_0000L;
            v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
            v1 = 0x2L;
            MEMORY.ref(1, v0).offset(0x220L).setu(v1);
          }
        }

        //LAB_800dab44
        v0 = 0x800f_0000L;
        v0 = MEMORY.ref(2, v0).offset(-0xe5cL).get();

        v1 = v0 + 0x1L;
        at = 0x800f_0000L;
        MEMORY.ref(2, at).offset(-0xe5cL).setu(v1);
        v0 = 0x800f_0000L;
        v0 = MEMORY.ref(2, v0).offset(-0xe5cL).getSigned();

        if((int)v0 >= 0x21L) {
          v0 = 0x20L;
          at = 0x800f_0000L;
          MEMORY.ref(2, at).offset(-0xe5cL).setu(v0);
        }

        //LAB_800dab80
        break;

      case 3:
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = MEMORY.ref(4, v1).offset(0x14L).get();
        a0 = 0x800c_0000L;
        a0 = MEMORY.ref(4, a0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, a0).offset(0x14L).get();
        a0 = 0x800c_0000L;
        a0 = MEMORY.ref(4, a0).offset(0x66a8L).get();

        a1 = MEMORY.ref(4, a0).offset(0x14L).get();

        MEMORY.ref(4, a1).offset(0xfcL).setu(0);
        MEMORY.ref(4, v1).offset(0x104L).setu(0);
        MEMORY.ref(4, v0).offset(0x100L).setu(0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x14L).get();
        v0 = 0x400L;
        MEMORY.ref(2, v1).offset(0x74L).setu(v0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x14L).get();
        v0 = 0x800L;
        MEMORY.ref(2, v1).offset(0x76L).setu(v0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x14L).get();

        MEMORY.ref(2, v1).offset(0x78L).setu(0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();

        MEMORY.ref(2, v0).offset(0x72L).setu(0);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

        v0 = v1 + 0x20L;
        v1 = 0x2d0L;
        MEMORY.ref(4, v0).offset(0x18L).setu(v1);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

        v0 = v1 + 0x20L;
        v1 = -0x5dcL;
        MEMORY.ref(4, v0).offset(0x1cL).setu(v1);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

        v0 = v1 + 0x20L;
        v1 = 0x274L;
        MEMORY.ref(4, v0).offset(0x20L).setu(v1);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
        v1 = 0x3L;
        MEMORY.ref(1, v0).offset(0x11aL).setu(v1);
        sp24 = 0;
        sp20 = 0;

        //LAB_800dac80
        do {
          v0 = sp20;

          if((int)v0 >= 0x9L) {
            break;
          }

          //LAB_800dac9c
          v0 = sp20;

          v1 = v0;
          a0 = v1 << 5;
          at = 0x800f_0000L;
          at = at + a0;
          v0 = MEMORY.ref(4, at).offset(-0xdc8L).get();

          a0 = v0;
          v1 = a0 << 2;
          v1 = v1 + v0;
          a0 = v1 << 2;
          at = 0x800f_0000L;
          at = at + a0;
          v0 = MEMORY.ref(1, at).offset(0xe42L).get();
          a0 = 0x800c_0000L;
          a0 = MEMORY.ref(4, a0).offset(0x6798L).get();

          v1 = a0 + 0x1L;
          if(v0 == v1) {
            v0 = 0x800c_0000L;
            v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
            v1 = sp20;

            MEMORY.ref(1, v0).offset(0x221L).setu(v1);
            v0 = 0x1L;
            sp24 = v0;
            break;
          }

          //LAB_800dad14
          v0 = sp20;

          v1 = v0 + 0x1L;
          sp20 = v1;
        } while(true);

        //LAB_800dad2c
        v0 = sp24;

        if(v0 == 0) {
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
          v1 = 0x8L;
          MEMORY.ref(1, v0).offset(0x221L).setu(v1);
        }

        //LAB_800dad4c
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x6798L).get();
        v1 = 0x4L;
        if(v0 == v1) {
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

          v1 = MEMORY.ref(4, v0).offset(0x9cL).get();

          v0 = (int)v1 >> 12;
          if((int)v0 < -0x190L) {
            v0 = 0x800c_0000L;
            v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
            v1 = 0x5L;
            MEMORY.ref(1, v0).offset(0x221L).setu(v1);
          } else {
            //LAB_800dad9c
            v0 = 0x800c_0000L;
            v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
            v1 = 0x6L;
            MEMORY.ref(1, v0).offset(0x221L).setu(v1);
          }
        }

        //LAB_800dadac
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(1, v1).offset(0x221L).getSigned();

        v1 = a0;
        a0 = v1 << 5;
        at = 0x800f_0000L;
        at = at + a0;
        v1 = MEMORY.ref(1, at).offset(-0xdc4L).get();

        MEMORY.ref(1, v0).offset(0x222L).setu(v1);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
        v1 = 0x3L;
        MEMORY.ref(1, v0).offset(0x220L).setu(v1);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = v1 + 0x94L;
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(1, v1).offset(0x221L).getSigned();

        v1 = a0;
        a0 = v1 << 5;
        at = 0x800f_0000L;
        at = at + a0;
        v1 = MEMORY.ref(4, at).offset(-0xdd8L).get();

        MEMORY.ref(4, v0).offset(0x0L).setu(v1);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = v1 + 0x94L;
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(1, v1).offset(0x221L).getSigned();

        v1 = a0;
        a0 = v1 << 5;
        at = 0x800f_0000L;
        at = at + a0;
        v1 = MEMORY.ref(4, at).offset(-0xdd4L).get();

        MEMORY.ref(4, v0).offset(0x4L).setu(v1);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = v1 + 0x94L;
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(1, v1).offset(0x221L).getSigned();

        v1 = a0;
        a0 = v1 << 5;
        at = 0x800f_0000L;
        at = at + a0;
        v1 = MEMORY.ref(4, at).offset(-0xdd0L).get();

        MEMORY.ref(4, v0).offset(0x8L).setu(v1);
        break;

      case 4:
        v1 = 0x8008_0000L;
        v1 = MEMORY.ref(4, v1).offset(-0x5c68L).get();

        v0 = v1 & 0xc0L;
        if(v0 != 0) {
          sp10 = 0;
          sp14 = 0;
          a0 = 0;
          a1 = 0x3L;
          a2 = 0;
          a3 = 0;

          playSound((int)a0, (int)a1, a2, a3, (short)sp10, (short)sp14);

          //LAB_800daef8
          for(int i = 0; i < 8; i++) {
            //LAB_800daf14
            FUN_8002a3ec(i, 0);
          }

          //LAB_800daf44
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

          v1 = MEMORY.ref(4, v0).offset(0x254L).get();

          if(v1 != 0) {
            v0 = 0x800c_0000L;
            v0 = MEMORY.ref(2, v0).offset(0x67a8L).get();

            a0 = v0;
            v1 = a0 << 2;
            v1 = v1 + v0;
            v0 = v1 << 2;
            at = 0x800f_0000L;
            at = at + v0;
            v1 = MEMORY.ref(2, at).offset(0xe3cL).get();
            at = 0x800c_0000L;
            MEMORY.ref(2, at).offset(0x6860L).setu(v1);
            v0 = 0x800c_0000L;
            v0 = MEMORY.ref(2, v0).offset(0x67a8L).get();

            a0 = v0;
            v1 = a0 << 2;
            v1 = v1 + v0;
            v0 = v1 << 2;
            at = 0x800f_0000L;
            at = at + v0;
            v1 = MEMORY.ref(2, at).offset(0xe3eL).get();
            at = 0x800c_0000L;
            MEMORY.ref(2, at).offset(0x6862L).setu(v1);
            v0 = 0x800c_0000L;
            v0 = MEMORY.ref(2, v0).offset(0x6860L).get();
            at = 0x8005_0000L;
            MEMORY.ref(4, at).offset(0x2c30L).setu(v0);
            v0 = 0x800c_0000L;
            v0 = MEMORY.ref(2, v0).offset(0x6862L).get();
            at = 0x8005_0000L;
            MEMORY.ref(4, at).offset(0x2c34L).setu(v0);

            FUN_800e3fac(1);
          } else {
            //LAB_800daff4
            v0 = 0x800c_0000L;
            v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
            v1 = 0xaL;
            MEMORY.ref(1, v0).offset(0x220L).setu(v1);
          }

          //LAB_800db004
          break;
        }

        //LAB_800db00c
        v1 = 0x8008_0000L;
        v1 = MEMORY.ref(4, v1).offset(-0x5c68L).get();

        v0 = v1 & 0x20L;
        if(v0 != 0) {
          sp10 = 0;
          sp14 = 0;
          a0 = 0;
          a1 = 0x2L;
          a2 = 0;
          a3 = 0;

          playSound((int)a0, (int)a1, a2, a3, (short)sp10, (short)sp14);
          v0 = 0x9L;
          sp10 = v0;
          v0 = 0x4L;
          sp14 = v0;
          a1 = 0x1L;
          a2 = 0xf0L;
          a3 = 0x40L;

          FUN_8002a32c(6, a1, a2, a3, sp10, sp14);
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
          v1 = 0x4L;
          MEMORY.ref(1, v0).offset(0x220L).setu(v1);
        }

        //LAB_800db07c
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = MEMORY.ref(4, v1).offset(0x14L).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(4, v1).offset(0x14L).get();

        v1 = MEMORY.ref(4, a0).offset(0xfcL).get();

        a0 = v1 + 0x200L;
        MEMORY.ref(4, v0).offset(0xfcL).setu(a0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x14L).get();

        v0 = MEMORY.ref(4, v1).offset(0xfcL).get();

        if((int)v0 >= 0x801L) {
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

          v1 = MEMORY.ref(4, v0).offset(0x14L).get();
          v0 = 0x800L;
          MEMORY.ref(4, v1).offset(0xfcL).setu(v0);
        }

        //LAB_800db0f0
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = MEMORY.ref(4, v1).offset(0x14L).get();
        a0 = 0x800c_0000L;
        a0 = MEMORY.ref(4, a0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, a0).offset(0x14L).get();
        a0 = 0x800c_0000L;
        a0 = MEMORY.ref(4, a0).offset(0x66a8L).get();

        a1 = MEMORY.ref(4, a0).offset(0x14L).get();

        a0 = MEMORY.ref(4, a1).offset(0xfcL).get();

        MEMORY.ref(4, v1).offset(0x104L).setu(a0);
        MEMORY.ref(4, v0).offset(0x100L).setu(a0);
        a0 = 0x1L;
        a1 = 0x1L;

        FUN_800dc178(a0, a1);
        break;

      case 5:
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = MEMORY.ref(4, v1).offset(0x14L).get();
        a0 = 0x800c_0000L;
        a0 = MEMORY.ref(4, a0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, a0).offset(0x14L).get();
        a0 = 0x800c_0000L;
        a0 = MEMORY.ref(4, a0).offset(0x66a8L).get();

        a1 = MEMORY.ref(4, a0).offset(0x14L).get();
        a0 = 0x800L;
        MEMORY.ref(4, a1).offset(0xfcL).setu(a0);
        a0 = 0x800L;
        MEMORY.ref(4, v1).offset(0x104L).setu(a0);
        v1 = 0x800L;
        MEMORY.ref(4, v0).offset(0x100L).setu(v1);
        a0 = 0x6L;

        v0 = FUN_8002a488(a0);
        if(v0 != 0) {
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
          v1 = 0x5L;
          MEMORY.ref(1, v0).offset(0x220L).setu(v1);
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

          MEMORY.ref(1, v0).offset(0x223L).setu(0);
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

          MEMORY.ref(4, v0).offset(0x218L).setu(0);
        }

        //LAB_800db1d8
        a0 = 0;
        a1 = 0;

        FUN_800dc178(a0, a1);
        break;

      case 6:
        _800be358.get(6)._0c.set(0x12L);
        FUN_800e7624(_800f00e8, sp0x28, sp0x2c);
        FUN_800e774c(_800f00e8, (short)(0xf0L - sp0x28.get() * 3), 0x29L, 0, 0);
        FUN_800e7624(_800effa4, sp0x28, sp0x2c);
        FUN_800e774c(_800effa4, (short)(0xf0L - sp0x28.get() * 3), 0x39L, 0, 0);
        FUN_800e7624(_800effb0, sp0x28, sp0x2c);
        FUN_800e774c(_800effb0, (short)(0xf0L - sp0x28.get() * 3), 0x49L, 0, 0);
        FUN_800dc178(0, 0);

        if((joypadPress_8007a398.get() & 0x40L) != 0) {
          playSound(0, 3, 0, 0, (short)0, (short)0);
          FUN_8002a3ec(6, 1);
          struct258_800c66a8.deref()._220.set(3);
        }

        //LAB_800db39c
        v1 = 0x8008_0000L;
        v1 = MEMORY.ref(4, v1).offset(-0x5c68L).get();

        v0 = v1 & 0x5000L;
        if(v0 != 0) {
          sp10 = 0;
          sp14 = 0;
          a0 = 0;
          a1 = 0x1L;
          a2 = 0;
          a3 = 0;

          playSound((int)a0, (int)a1, a2, a3, (short)sp10, (short)sp14);
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
          v1 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

          a0 = MEMORY.ref(1, v1).offset(0x223L).get();

          v1 = a0 ^ 0x1L;
          MEMORY.ref(1, v0).offset(0x223L).setu(v1);
        }

        //LAB_800db3f8
        v1 = 0x8008_0000L;
        v1 = MEMORY.ref(4, v1).offset(-0x5c68L).get();

        v0 = v1 & 0x20L;
        if(v0 != 0) {
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

          v1 = MEMORY.ref(1, v0).offset(0x223L).get();

          if(v1 == 0) {
            sp10 = 0;
            sp14 = 0;
            a0 = 0;
            a1 = 0x3L;
            a2 = 0;
            a3 = 0;

            playSound((int)a0, (int)a1, a2, a3, (short)sp10, (short)sp14);
            a1 = 0x1L;

            FUN_8002a3ec(6, a1);
            v0 = 0x800c_0000L;
            v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
            v1 = 0x3L;
            MEMORY.ref(1, v0).offset(0x220L).setu(v1);
          } else {
            //LAB_800db474
            sp10 = 0;
            sp14 = 0;
            a0 = 0;
            a1 = 0x2L;
            a2 = 0;
            a3 = 0;

            playSound((int)a0, (int)a1, a2, a3, (short)sp10, (short)sp14);
            a1 = 0x1L;

            FUN_8002a3ec(6, a1);
            v0 = 0x800c_0000L;
            v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
            v1 = 0x6L;
            MEMORY.ref(1, v0).offset(0x220L).setu(v1);
          }
        }

        //LAB_800db4b4
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = MEMORY.ref(4, v1).offset(0x1fcL).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(1, v1).offset(0x223L).get();

        v1 = a0;
        a0 = v1 << 4;
        MEMORY.ref(2, v0).offset(0x3aL).setu(a0);

        FUN_800ce4dc(struct258_800c66a8.deref()._1fc.deref());
        break;

      case 7:
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(1, v0).offset(0x221L).getSigned();

        v0 = v1;
        v1 = v0 << 5;
        at = 0x800f_0000L;
        at = at + v1;
        v0 = MEMORY.ref(4, at).offset(-0xdd8L).get();

        v1 = (int)v0 >> 12;
        sp0x38.setX((int)v1);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(1, v0).offset(0x221L).getSigned();

        v0 = v1;
        v1 = v0 << 5;
        at = 0x800f_0000L;
        at = at + v1;
        v0 = MEMORY.ref(4, at).offset(-0xdd4L).get();

        v1 = (int)v0 >> 12;
        sp0x38.setY((int)v1);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(1, v0).offset(0x221L).getSigned();

        v0 = v1;
        v1 = v0 << 5;
        at = 0x800f_0000L;
        at = at + v1;
        v0 = MEMORY.ref(4, at).offset(-0xdd0L).get();

        v1 = (int)v0 >> 12;
        sp0x38.setZ((int)v1);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(1, v0).offset(0x222L).getSigned();

        v0 = v1;
        v1 = v0 << 5;
        at = 0x800f_0000L;
        at = at + v1;
        v0 = MEMORY.ref(4, at).offset(-0xdd8L).get();

        v1 = (int)v0 >> 12;
        sp0x48.setX((int)v1);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(1, v0).offset(0x222L).getSigned();

        v0 = v1;
        v1 = v0 << 5;
        at = 0x800f_0000L;
        at = at + v1;
        v0 = MEMORY.ref(4, at).offset(-0xdd4L).get();

        v1 = (int)v0 >> 12;
        sp0x48.setY((int)v1);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(1, v0).offset(0x222L).getSigned();

        v0 = v1;
        v1 = v0 << 5;
        at = 0x800f_0000L;
        at = at + v1;
        v0 = MEMORY.ref(4, at).offset(-0xdd0L).get();

        v1 = (int)v0 >> 12;
        sp0x48.setZ((int)v1);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(4, v1).offset(0x218L).get();

        v1 = a0 + 0x1L;
        a0 = v1;
        MEMORY.ref(4, v0).offset(0x218L).setu(a0);
        if((int)a0 >= 0xdL) {
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
          v1 = 0xcL;
          MEMORY.ref(4, v0).offset(0x218L).setu(v1);
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
          v1 = 0x7L;
          MEMORY.ref(1, v0).offset(0x220L).setu(v1);
        }

        //LAB_800db698
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = v1 + 0x94L;
        a0 = 0x800c_0000L;
        a0 = MEMORY.ref(4, a0).offset(0x66a8L).get();

        a1 = MEMORY.ref(4, a0).offset(0x218L).get();

        sp10 = a1;
        a0 = v0;
        a3 = 0xcL;

        FUN_800dcc20(a0, sp0x38, sp0x48, a3, sp10);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = MEMORY.ref(4, v1).offset(0x14L).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(4, v1).offset(0x14L).get();

        v1 = MEMORY.ref(4, a0).offset(0xfcL).get();

        a0 = v1 - 0xaaL;
        MEMORY.ref(4, v0).offset(0xfcL).setu(a0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x14L).get();

        v0 = MEMORY.ref(4, v1).offset(0xfcL).get();

        if((int)v0 < 0) {
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

          v1 = MEMORY.ref(4, v0).offset(0x14L).get();

          MEMORY.ref(4, v1).offset(0xfcL).setu(0);
        }

        //LAB_800db74c
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = MEMORY.ref(4, v1).offset(0x14L).get();
        a0 = 0x800c_0000L;
        a0 = MEMORY.ref(4, a0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, a0).offset(0x14L).get();
        a0 = 0x800c_0000L;
        a0 = MEMORY.ref(4, a0).offset(0x66a8L).get();

        a1 = MEMORY.ref(4, a0).offset(0x14L).get();

        a0 = MEMORY.ref(4, a1).offset(0xfcL).get();

        MEMORY.ref(4, v1).offset(0x104L).setu(a0);
        MEMORY.ref(4, v0).offset(0x100L).setu(a0);
        a0 = 0;
        a1 = 0;

        FUN_800dc178(a0, a1);
        break;

      case 8:
        a0 = 0xcL;
        a1 = 0x1L;
        a2 = 0x1L;

        FUN_80019c80(a0, a1, a2);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(1, v0).offset(0x222L).getSigned();
        v0 = 0x8L;
        if(v1 == v0) {
          v0 = 0x800c_0000L;
          v0 = v0 - 0x52bcL;
          sp58 = v0;
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

          v1 = MEMORY.ref(1, v0).offset(0x222L).getSigned();

          v0 = v1;
          v1 = v0 << 5;
          at = 0x800f_0000L;
          at = at + v1;
          v0 = MEMORY.ref(4, at).offset(-0xdc8L).get();

          sp60 = v0;
          v1 = sp60;

          v0 = v1 & 0x1fL;
          v1 = 0x1L;
          v0 = v1 << v0;
          v1 = v0;
          sp5c = v1;
          v1 = sp60;

          v0 = v1 >>> 5;
          v1 = v0;
          sp60 = v1;
          v0 = sp60;

          v1 = v0;
          v0 = v1 << 2;
          v1 = sp58;

          v0 = v0 + v1;
          v1 = sp60;

          a0 = v1;
          v1 = a0 << 2;
          a0 = sp58;

          v1 = v1 + a0;
          a0 = MEMORY.ref(4, v1).offset(0x0L).get();
          a1 = sp5c;

          v1 = a0 | a1;
          a0 = v1;
          MEMORY.ref(4, v0).offset(0x0L).setu(a0);

          //LAB_800db8f4
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

          v1 = MEMORY.ref(1, v0).offset(0x222L).getSigned();

          v0 = v1;
          v1 = v0 << 5;
          at = 0x800f_0000L;
          at = at + v1;
          v0 = MEMORY.ref(4, at).offset(-0xdc8L).get();

          a0 = v0;
          v1 = a0 << 2;
          v1 = v1 + v0;
          v0 = v1 << 2;
          at = 0x800f_0000L;
          at = at + v0;
          v1 = MEMORY.ref(2, at).offset(0xe3cL).get();
          at = 0x800c_0000L;
          MEMORY.ref(2, at).offset(0x6860L).setu(v1);
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

          v1 = MEMORY.ref(1, v0).offset(0x222L).getSigned();

          v0 = v1;
          v1 = v0 << 5;
          at = 0x800f_0000L;
          at = at + v1;
          v0 = MEMORY.ref(4, at).offset(-0xdc8L).get();

          a0 = v0;
          v1 = a0 << 2;
          v1 = v1 + v0;
          v0 = v1 << 2;
          at = 0x800f_0000L;
          at = at + v0;
          v1 = MEMORY.ref(2, at).offset(0xe3eL).get();
          at = 0x800c_0000L;
          MEMORY.ref(2, at).offset(0x6862L).setu(v1);
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(2, v0).offset(0x6860L).get();
          at = 0x8005_0000L;
          MEMORY.ref(4, at).offset(0x2c30L).setu(v0);
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(2, v0).offset(0x6862L).get();
          at = 0x8005_0000L;
          MEMORY.ref(4, at).offset(0x2c34L).setu(v0);
        } else {
          //LAB_800db9bc
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

          v1 = MEMORY.ref(1, v0).offset(0x222L).getSigned();

          v0 = v1;
          v1 = v0 << 5;
          at = 0x800f_0000L;
          at = at + v1;
          v0 = MEMORY.ref(4, at).offset(-0xdc8L).get();

          a0 = v0;
          v1 = a0 << 2;
          v1 = v1 + v0;
          v0 = v1 << 2;
          at = 0x800f_0000L;
          at = at + v0;
          v1 = MEMORY.ref(2, at).offset(0xe38L).get();
          at = 0x800c_0000L;
          MEMORY.ref(2, at).offset(0x6860L).setu(v1);
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

          v1 = MEMORY.ref(1, v0).offset(0x222L).getSigned();

          v0 = v1;
          v1 = v0 << 5;
          at = 0x800f_0000L;
          at = at + v1;
          v0 = MEMORY.ref(4, at).offset(-0xdc8L).get();

          a0 = v0;
          v1 = a0 << 2;
          v1 = v1 + v0;
          v0 = v1 << 2;
          at = 0x800f_0000L;
          at = at + v0;
          v1 = MEMORY.ref(2, at).offset(0xe3aL).get();
          at = 0x800c_0000L;
          MEMORY.ref(2, at).offset(0x6862L).setu(v1);
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(2, v0).offset(0x6860L).get();
          at = 0x8005_0000L;
          MEMORY.ref(4, at).offset(0x2c30L).setu(v0);
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(2, v0).offset(0x6862L).get();
          at = 0x8005_0000L;
          MEMORY.ref(4, at).offset(0x2c38L).setu(v0);
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
          v1 = 0x3L;
          MEMORY.ref(4, v0).offset(0x250L).setu(v1);
          v0 = -0x1L;
          at = 0x8005_0000L;
          MEMORY.ref(4, at).offset(-0x22d8L).setu(v0);
        }

        //LAB_800dba98

        FUN_800e3fac(1);
        a0 = 0;
        a1 = 0;

        FUN_800dc178(a0, a1);
        break;

      case 0xb:
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

        v0 = v1 + 0x20L;
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(2, v1).offset(0x200L).getSigned();

        MEMORY.ref(4, v0).offset(0x18L).setu(a0);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

        v0 = v1 + 0x20L;
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(2, v1).offset(0x202L).getSigned();

        MEMORY.ref(4, v0).offset(0x1cL).setu(a0);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

        v0 = v1 + 0x20L;
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(2, v1).offset(0x204L).getSigned();

        MEMORY.ref(4, v0).offset(0x20L).setu(a0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
        v1 = -0x5dcL;
        MEMORY.ref(4, v0).offset(0x3cL).setu(v1);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = v1 + 0x94L;
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(2, v1).offset(0x208L).getSigned();

        v1 = a0 << 12;
        MEMORY.ref(4, v0).offset(0x0L).setu(v1);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = v1 + 0x94L;
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(2, v1).offset(0x20aL).getSigned();

        v1 = a0 << 12;
        MEMORY.ref(4, v0).offset(0x4L).setu(v1);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = v1 + 0x94L;
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(2, v1).offset(0x20cL).getSigned();

        v1 = a0 << 12;
        MEMORY.ref(4, v0).offset(0x8L).setu(v1);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
        v1 = 0xfec7_0000L;
        v1 = v1 | 0x8000L;
        MEMORY.ref(4, v0).offset(0x98L).setu(v1);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(2, v1).offset(0x21cL).get();

        MEMORY.ref(2, v0).offset(0xa6L).setu(a0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(2, v1).offset(0x21eL).get();

        MEMORY.ref(2, v0).offset(0x72L).setu(a0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x14L).get();

        MEMORY.ref(2, v1).offset(0x74L).setu(0);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = MEMORY.ref(4, v1).offset(0x14L).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(2, v1).offset(0xa6L).get();

        MEMORY.ref(2, v0).offset(0x76L).setu(a0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x14L).get();

        MEMORY.ref(2, v1).offset(0x78L).setu(0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x14L).get();
        v0 = 0x600L;
        MEMORY.ref(4, v1).offset(0xfcL).setu(v0);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = MEMORY.ref(4, v1).offset(0x14L).get();
        a0 = 0x800c_0000L;
        a0 = MEMORY.ref(4, a0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, a0).offset(0x14L).get();
        a0 = 0x800c_0000L;
        a0 = MEMORY.ref(4, a0).offset(0x66a8L).get();

        a1 = MEMORY.ref(4, a0).offset(0x14L).get();

        a0 = MEMORY.ref(4, a1).offset(0xfcL).get();

        MEMORY.ref(4, v1).offset(0x104L).setu(a0);
        MEMORY.ref(4, v0).offset(0x100L).setu(a0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
        v1 = 0xbL;
        MEMORY.ref(1, v0).offset(0x220L).setu(v1);
        a0 = 0xcL;
        a1 = 0x1L;
        a2 = 0x1L;

        FUN_80019c80(a0, a1, a2);

      case 0xc:
        FUN_800e3304();
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

        a0 = MEMORY.ref(4, v1).offset(0x3cL).get();

        v1 = a0 + 0x70L;
        MEMORY.ref(4, v0).offset(0x3cL).setu(v1);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(2, v1).offset(0x202L).getSigned();
        v0 = MEMORY.ref(4, v0).offset(0x3cL).get();

        if((int)a0 < (int)v0) {
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
          v1 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

          a0 = MEMORY.ref(2, v1).offset(0x202L).getSigned();

          MEMORY.ref(4, v0).offset(0x3cL).setu(a0);
        }

        //LAB_800dbd6c
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(2, v1).offset(0x202L).getSigned();
        v0 = MEMORY.ref(4, v0).offset(0x3cL).get();

        if((int)v0 >= (int)a0) {
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
          v1 = 0xcL;
          MEMORY.ref(1, v0).offset(0x220L).setu(v1);
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
          v1 = 0xffe7_0000L;
          MEMORY.ref(4, v0).offset(0x98L).setu(v1);
        }

        //LAB_800dbdb8
        v0 = 0x800f_0000L;
        v0 = MEMORY.ref(2, v0).offset(-0xe5cL).get();

        v1 = v0 - 0x1L;
        at = 0x800f_0000L;
        MEMORY.ref(2, at).offset(-0xe5cL).setu(v1);
        v0 = 0x800f_0000L;
        v0 = MEMORY.ref(2, v0).offset(-0xe5cL).getSigned();

        if((int)v0 < 0) {
          at = 0x800f_0000L;
          MEMORY.ref(2, at).offset(-0xe5cL).setu(0);
        }

        //LAB_800dbdec
        break;

      case 0xd:
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(4, v1).offset(0x98L).get();
        v1 = 0x1_0000L;
        a0 = a0 + v1;
        MEMORY.ref(4, v0).offset(0x98L).setu(a0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(2, v1).offset(0x20aL).getSigned();

        v1 = a0 << 12;
        v0 = MEMORY.ref(4, v0).offset(0x98L).get();

        if((int)v1 < (int)v0) {
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
          v1 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

          a0 = MEMORY.ref(2, v1).offset(0x20aL).getSigned();

          v1 = a0 << 12;
          MEMORY.ref(4, v0).offset(0x98L).setu(v1);
        }

        //LAB_800dbe70
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(2, v1).offset(0x20aL).getSigned();

        v1 = a0 << 12;
        v0 = MEMORY.ref(4, v0).offset(0x98L).get();

        if((int)v0 >= (int)v1) {
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
          v1 = -0x1L;
          MEMORY.ref(1, v0).offset(0x220L).setu(v1);
        }

        //LAB_800dbeb4
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = MEMORY.ref(4, v1).offset(0x14L).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(4, v1).offset(0x14L).get();

        v1 = MEMORY.ref(4, a0).offset(0xfcL).get();

        a0 = v1 - 0x10L;
        MEMORY.ref(4, v0).offset(0xfcL).setu(a0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x14L).get();

        v0 = MEMORY.ref(4, v1).offset(0xfcL).get();

        if((int)v0 < 0x400L) {
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

          v1 = MEMORY.ref(4, v0).offset(0x14L).get();
          v0 = 0x400L;
          MEMORY.ref(4, v1).offset(0xfcL).setu(v0);
        }

        //LAB_800dbf28
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = MEMORY.ref(4, v1).offset(0x14L).get();
        a0 = 0x800c_0000L;
        a0 = MEMORY.ref(4, a0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, a0).offset(0x14L).get();
        a0 = 0x800c_0000L;
        a0 = MEMORY.ref(4, a0).offset(0x66a8L).get();

        a1 = MEMORY.ref(4, a0).offset(0x14L).get();

        a0 = MEMORY.ref(4, a1).offset(0xfcL).get();

        MEMORY.ref(4, v1).offset(0x104L).setu(a0);
        MEMORY.ref(4, v0).offset(0x100L).setu(a0);
        v0 = 0x800f_0000L;
        v0 = MEMORY.ref(2, v0).offset(-0xe5cL).get();

        v1 = v0 - 0x1L;
        at = 0x800f_0000L;
        MEMORY.ref(2, at).offset(-0xe5cL).setu(v1);
        v0 = 0x800f_0000L;
        v0 = MEMORY.ref(2, v0).offset(-0xe5cL).getSigned();

        if((int)v0 < 0) {
          at = 0x800f_0000L;
          MEMORY.ref(2, at).offset(-0xe5cL).setu(0);
        }

        //LAB_800dbfa0
        break;

      case 0:
        at = 0x800f_0000L;
        MEMORY.ref(2, at).offset(-0xe5cL).setu(0);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

        v0 = v1 + 0x20L;
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(2, v1).offset(0x200L).getSigned();

        MEMORY.ref(4, v0).offset(0x18L).setu(a0);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

        v0 = v1 + 0x20L;
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(2, v1).offset(0x202L).getSigned();

        MEMORY.ref(4, v0).offset(0x1cL).setu(a0);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

        v0 = v1 + 0x20L;
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(2, v1).offset(0x204L).getSigned();

        MEMORY.ref(4, v0).offset(0x20L).setu(a0);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = v1 + 0x94L;
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(2, v1).offset(0x208L).getSigned();

        v1 = a0 << 12;
        MEMORY.ref(4, v0).offset(0x0L).setu(v1);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = v1 + 0x94L;
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(2, v1).offset(0x20aL).getSigned();

        v1 = a0 << 12;
        MEMORY.ref(4, v0).offset(0x4L).setu(v1);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = v1 + 0x94L;
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(2, v1).offset(0x20cL).getSigned();

        v1 = a0 << 12;
        MEMORY.ref(4, v0).offset(0x8L).setu(v1);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(2, v1).offset(0x21cL).get();

        MEMORY.ref(2, v0).offset(0xa6L).setu(a0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(2, v1).offset(0x21eL).get();

        MEMORY.ref(2, v0).offset(0x72L).setu(a0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        MEMORY.ref(4, v0).offset(0x250L).setu(0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        MEMORY.ref(1, v0).offset(0x220L).setu(0);
        return;
    }

    //LAB_800dc114
    v0 = -0x78L;
    sp10 = v0;
    v0 = 0x1f80_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x3c8L).get();

    v1 = v0 - 0x4L;
    sp14 = v1;
    v0 = 0x1L;
    sp18 = v0;
    v0 = 0x800f_0000L;
    v0 = MEMORY.ref(1, v0).offset(-0xe5cL).get();

    sp1c = v0;
    a1 = 0x140L;
    a2 = 0;
    a3 = -0xa0L;

    FUN_800e4934(mcqHeader_800c6768, a1, a2, a3, sp10, sp14, sp18, sp1c);

    //LAB_800dc164
  }

  @Method(0x800dc178L)
  public static void FUN_800dc178(long a0, long a1) {
    assert false;
  }

  @Method(0x800dcc20L)
  public static void FUN_800dcc20(long a0, VECTOR a1, VECTOR a2, long a3, long a4) {
    assert false;
  }

  @Method(0x800dcde8L)
  public static void FUN_800dcde8() {
    if(!struct258_800c66a8.deref().tmdRendering_08.isNull()) {
      deallocateTmdRenderer(struct258_800c66a8.deref().tmdRendering_08.deref());
    }

    //LAB_800dce24
    FUN_800d6818(struct258_800c66a8.deref()._1c.get());
    FUN_800d15d8(struct258_800c66a8.deref()._1fc.deref());
  }

  @Method(0x800dce64L)
  public static void rotateCoord2(final SVECTOR rotation, final GsCOORDINATE2 coord2) {
    final MATRIX mat = new MATRIX().set(identityMatrix_800c3568);

    mat.transfer.setX(coord2.coord.transfer.getX());
    mat.transfer.setY(coord2.coord.transfer.getY());
    mat.transfer.setZ(coord2.coord.transfer.getZ());

    RotMatrix_8003faf0(rotation, mat);

    coord2.flg.set(0);
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

  @Method(0x800dd05cL)
  public static void FUN_800dd05c(final GsDOBJ2 dobj2) {
    final long vertices = dobj2.tmd_08.deref().vert_top_00.get();
    final long normals = dobj2.tmd_08.deref().normal_top_08.get();
    long primitives = dobj2.tmd_08.deref().primitives_10.getPointer();
    long count = dobj2.tmd_08.deref().n_primitive_14.get();

    //LAB_800dd0dc
    while(count != 0) {
      //LAB_800dd0f4
      count -= MEMORY.ref(2, primitives).get();

      final long v0 = MEMORY.ref(4, primitives).get() & 0xff04_0000L;
      if(v0 == 0x3a04_0000L) {
        //LAB_800dd270
        primitives = FUN_800de534(primitives, vertices, normals, MEMORY.ref(2, primitives).get());
      } else if(v0 == 0x3500_0000L) {
        //LAB_800dd358
        primitives = FUN_800df6c8(primitives, vertices, MEMORY.ref(2, primitives).get());
        //LAB_800dd190
      } else if(v0 == 0x3800_0000L) {
        //LAB_800dd1e0
        primitives = FUN_800dd3a8(primitives, vertices, normals, MEMORY.ref(2, primitives).get());
        //LAB_800dd170
      } else if(v0 == 0x3804_0000L) {
        //LAB_800dd240
        primitives = FUN_800de0bc(primitives, vertices, normals, MEMORY.ref(2, primitives).get());
      } else if(v0 == 0x3a00_0000L) {
        //LAB_800dd210
        primitives = FUN_800dd798(primitives, vertices, normals, MEMORY.ref(2, primitives).get());
      } else if(v0 == 0x3c00_0000L) {
        //LAB_800dd2a0
        primitives = FUN_800ddc2c(primitives, vertices, normals, MEMORY.ref(2, primitives).get());
      } else if(v0 == 0x3d00_0000L) {
        //LAB_800dd300
        primitives = FUN_800deeac(primitives, vertices, MEMORY.ref(2, primitives).get());
        //LAB_800dd1c0
      } else if(v0 == 0x3e00_0000L) {
        //LAB_800dd2d0
        primitives = FUN_800dea58(primitives, vertices, normals, MEMORY.ref(2, primitives).get());
      } else if(v0 == 0x3f00_0000L) {
        //LAB_800dd32c
        primitives = FUN_800df228(primitives, vertices, MEMORY.ref(2, primitives).get());
      }

      //LAB_800dd384
      //LAB_800dd38c
    }

    //LAB_800dd394
  }

  @Method(0x800dd3a8L)
  public static long FUN_800dd3a8(long a0, long a1, long a2, long a3) {
    assert false;
    return 0;
  }

  @Method(0x800dd798L)
  public static long FUN_800dd798(long a0, long a1, long a2, long a3) {
    assert false;
    return 0;
  }

  @Method(0x800ddc2cL)
  public static long FUN_800ddc2c(long a0, long a1, long a2, long a3) {
    assert false;
    return 0;
  }

  @Method(0x800de0bcL)
  public static long FUN_800de0bc(long a0, long a1, long a2, long a3) {
    assert false;
    return 0;
  }

  @Method(0x800de534L)
  public static long FUN_800de534(long a0, long a1, long a2, long a3) {
    assert false;
    return 0;
  }

  @Method(0x800dea58L)
  public static long FUN_800dea58(long a0, long a1, long a2, long a3) {
    assert false;
    return 0;
  }

  @Method(0x800deeacL)
  public static long FUN_800deeac(long a0, long a1, long a2) {
    long at;
    long v0;
    long v1;
    long a3;
    long t0;
    long t1;
    long t2;
    long t5;
    long t6;
    long t7;
    long sp0;
    long sp4;
    long sp8;
    sp0 = a0;
    sp4 = a1;
    sp8 = a2;
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(-0x4ef8L).get();

    t0 = v0;
    a3 = t0 << 2;
    a3 = a3 + v0;
    v0 = a3 << 2;
    at = 0x8006_0000L;
    at = at + v0;
    a1 = MEMORY.ref(4, at).offset(-0x5c8cL).get();
    v1 = 0x1f80_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x3d8L).get();

    //LAB_800deee8
    do {
      v0 = sp8;

      if(v0 == 0) {
        break;
      }

      //LAB_800def00
      t1 = sp4;
      t2 = sp0;

      t5 = MEMORY.ref(2, t2).offset(0x24L).get();
      t6 = MEMORY.ref(2, t2).offset(0x26L).get();
      t7 = MEMORY.ref(2, t2).offset(0x28L).get();
      t5 = t5 << 3;
      t6 = t6 << 3;
      t7 = t7 << 3;
      t5 = t1 + t5;
      t6 = t1 + t6;
      t7 = t1 + t7;
      CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
      CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
      CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
      CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
      CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
      CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);
      CPU.COP2(0x28_0030L);

      a3 = sp0;

      v0 = a3 + 0x4L;
      a3 = MEMORY.ref(4, v0).offset(0x0L).get();

      MEMORY.ref(4, v1).offset(0xcL).setu(a3);
      a3 = sp0;

      v0 = a3 + 0x8L;
      a3 = MEMORY.ref(4, v0).offset(0x0L).get();

      MEMORY.ref(4, v1).offset(0x18L).setu(a3);
      v0 = sp8;

      a3 = v0 - 0x1L;
      sp8 = a3;
      a2 = CPU.CFC2(31);

      if((int)a2 >= 0) {
        //LAB_800defac
        CPU.COP2(0x140_0006L);

        a3 = sp0;

        v0 = a3 + 0xcL;
        a3 = MEMORY.ref(4, v0).offset(0x0L).get();

        MEMORY.ref(4, v1).offset(0x24L).setu(a3);
        a2 = CPU.MFC2(24);

        if((int)a2 > 0) {
          //LAB_800defe8
          MEMORY.ref(4, v1).offset(0x08L).setu(CPU.MFC2(12));
          MEMORY.ref(4, v1).offset(0x14L).setu(CPU.MFC2(13));
          MEMORY.ref(4, v1).offset(0x20L).setu(CPU.MFC2(14));

          v0 = sp0;

          a3 = MEMORY.ref(2, v0).offset(0x2aL).get();

          v0 = a3;
          a3 = v0 << 3;
          t0 = sp4;

          v0 = a3 + t0;
          CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
          CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
          CPU.COP2(0x18_0001L);

          a3 = sp0;

          v0 = a3 + 0x10L;
          a3 = MEMORY.ref(4, v0).offset(0x0L).get();

          MEMORY.ref(4, v1).offset(0x30L).setu(a3);
          v0 = 0xcL;
          MEMORY.ref(1, v1).offset(0x3L).setu(v0);
          v0 = 0x3c80_0000L;
          v0 = v0 | 0x8080L;
          MEMORY.ref(4, v1).offset(0x4L).setu(v0);
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

          a3 = MEMORY.ref(4, v0).offset(0x28L).get();

          v0 = a3;
          a3 = v0 << 1;
          at = 0x800f_0000L;
          at = at + a3;
          v0 = MEMORY.ref(2, at).offset(-0xcb8L).get();

          a3 = v0 << 6;
          v0 = a3;
          a3 = v0 | 0x3fL;
          MEMORY.ref(2, v1).offset(0xeL).setu(a3);
          a2 = CPU.CFC2(31);

          if((int)a2 >= 0) {
            //LAB_800df0ac
            v0 = v1 + 0x2cL;
            MEMORY.ref(4, v0).setu(CPU.MFC2(14));
            v0 = sp0;

            a3 = MEMORY.ref(1, v0).offset(0x14L).get();

            MEMORY.ref(1, v1).offset(0x4L).setu(a3);
            v0 = sp0;

            a3 = MEMORY.ref(1, v0).offset(0x15L).get();

            MEMORY.ref(1, v1).offset(0x5L).setu(a3);
            v0 = sp0;

            a3 = MEMORY.ref(1, v0).offset(0x16L).get();

            MEMORY.ref(1, v1).offset(0x6L).setu(a3);
            v0 = sp0;

            a3 = MEMORY.ref(1, v0).offset(0x18L).get();

            MEMORY.ref(1, v1).offset(0x10L).setu(a3);
            v0 = sp0;

            a3 = MEMORY.ref(1, v0).offset(0x19L).get();

            MEMORY.ref(1, v1).offset(0x11L).setu(a3);
            v0 = sp0;

            a3 = MEMORY.ref(1, v0).offset(0x1aL).get();

            MEMORY.ref(1, v1).offset(0x12L).setu(a3);
            v0 = sp0;

            a3 = MEMORY.ref(1, v0).offset(0x1cL).get();

            MEMORY.ref(1, v1).offset(0x1cL).setu(a3);
            v0 = sp0;

            a3 = MEMORY.ref(1, v0).offset(0x1dL).get();

            MEMORY.ref(1, v1).offset(0x1dL).setu(a3);
            v0 = sp0;

            a3 = MEMORY.ref(1, v0).offset(0x1eL).get();

            MEMORY.ref(1, v1).offset(0x1eL).setu(a3);
            v0 = sp0;

            a3 = MEMORY.ref(1, v0).offset(0x20L).get();

            MEMORY.ref(1, v1).offset(0x28L).setu(a3);
            v0 = sp0;

            a3 = MEMORY.ref(1, v0).offset(0x21L).get();

            MEMORY.ref(1, v1).offset(0x29L).setu(a3);
            v0 = sp0;

            a3 = MEMORY.ref(1, v0).offset(0x22L).get();

            MEMORY.ref(1, v1).offset(0x2aL).setu(a3);
            v0 = 0x800c_0000L;
            v0 = MEMORY.ref(4, v0).offset(0x66d8L).get();

            a3 = v0;
            v0 = a3 << 2;
            a0 = a1 + v0;
            v0 = MEMORY.ref(4, a0).offset(0x0L).get();
            a3 = 0xff_0000L;
            a3 = a3 | 0xffffL;
            v0 = v0 & a3;
            a3 = 0xc00_0000L;
            v0 = v0 | a3;
            MEMORY.ref(4, v1).offset(0x0L).setu(v0);
            v0 = 0xff_0000L;
            v0 = v0 | 0xffffL;
            a3 = v1 & v0;
            MEMORY.ref(4, a0).offset(0x0L).setu(a3);
            v1 = v1 + 0x34L;
          }
        }
      }

      //LAB_800df1ec
      v0 = sp0;

      a3 = v0 + 0x2cL;
      sp0 = a3;
    } while(true);

    //LAB_800df204
    at = 0x1f80_0000L;
    MEMORY.ref(4, at).offset(0x3d8L).setu(v1);
    a3 = sp0;

    v0 = a3;

    //LAB_800df220
    return v0;
  }

  @Method(0x800df228L)
  public static long FUN_800df228(long a0, long a1, long a2) {
    assert false;
    return 0;
  }

  @Method(0x800df6c8L)
  public static long FUN_800df6c8(long a0, long a1, long a2) {
    assert false;
    return 0;
  }

  @Method(0x800dfa70L)
  public static void FUN_800dfa70() {
    _800c66b8.and(0xffff_fd57L);

    loadDrgnBinFile(0, 5713L, 0, getMethodAddress(WMap.class, "FUN_800d5858", Value.class, long.class, long.class), 0x2a8L, 0x4L);

    //LAB_800dfacc
    for(int i = 0; i < 4; i++) {
      //LAB_800dfae8
      struct258_800c66a8.deref().bigStructs_0c.get(i).set(MEMORY.ref(4, addToLinkedListTail(0x124L), BigStruct::new));
      loadDrgnBinFile(0, 5714L + i, 0, getMethodAddress(WMap.class, "FUN_800d5a30", Value.class, long.class, long.class), i, 0x2L);
      struct258_800c66a8.deref().bigStructs_0c.get(i).deref().ub_9d.set((int)_800ef694.offset(i).get() + 0x80);
    }

    //LAB_800dfbb4
    struct258_800c66a8.deref()._248.set(0);
  }

  @Method(0x800dfbd8L)
  public static void FUN_800dfbd8() {
    long at;
    long v0;
    long v1;
    long a0;
    long a1;
    long sp10;

    final WMapStruct258 struct258 = struct258_800c66a8.deref();
    struct258.vec_94.setX(struct258.coord2_34.coord.transfer.getX() * 0x1000);
    struct258.vec_94.setY(struct258.coord2_34.coord.transfer.getY() * 0x1000);
    struct258.vec_94.setZ(struct258.coord2_34.coord.transfer.getZ() * 0x1000);
    struct258.vec_84.set(struct258.vec_94);

    //LAB_800dfca4
    for(int i = 0; i < 4; i++) {
      final BigStruct bigStruct = struct258.bigStructs_0c.get(i).deref();

      //LAB_800dfcc0
      FUN_80020a00(bigStruct, struct258._b4.get(i).extendedTmd_00.deref(), struct258._b4.get(i).tmdAnim_08.deref());
      FUN_80021584(bigStruct, struct258._b4.get(i).tmdAnim_08.deref());

      bigStruct.coord2_14.coord.transfer.setX(struct258.coord2_34.coord.transfer.getX());
      bigStruct.coord2_14.coord.transfer.setY(struct258.coord2_34.coord.transfer.getY());
      bigStruct.coord2_14.coord.transfer.setZ(struct258.coord2_34.coord.transfer.getZ());
      bigStruct.coord2Param_64.rotate.setX((short)0);
      bigStruct.coord2Param_64.rotate.setY(struct258.rotation_a4.getY());
      bigStruct.coord2Param_64.rotate.setZ((short)0);
      bigStruct.scaleVector_fc.setX(0);
      bigStruct.scaleVector_fc.setY(0);
      bigStruct.scaleVector_fc.setZ(0);
    }

    //LAB_800dff4c
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();
    a0 = 0x2L;
    MEMORY.ref(4, v1).offset(0xacL).setu(a0);
    v1 = 0x2L;
    MEMORY.ref(4, v0).offset(0xb0L).setu(v1);
    sp10 = 0;

    //LAB_800dff70
    while(sp10 < 0x8L) {
      //LAB_800dff8c
      v0 = sp10;

      v1 = v0;
      v0 = v1 << 9;
      a0 = v0;

      v0 = rcos(a0);
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();
      a0 = sp10;

      a1 = a0;
      a0 = a1 << 2;
      v1 = v1 + a0;
      a0 = v0 << 5;
      v0 = (int)a0 >> 12;
      MEMORY.ref(2, v1).offset(0x1c4L).setu(v0);
      v0 = sp10;

      v1 = v0;
      v0 = v1 << 9;
      a0 = v0;

      v0 = rsin(a0);
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();
      a0 = sp10;

      a1 = a0;
      a0 = a1 << 2;
      v1 = v1 + a0;
      a0 = v0 << 5;
      v0 = (int)a0 >> 12;
      MEMORY.ref(2, v1).offset(0x1c6L).setu(v0);

      sp10++;
    }

    //LAB_800e002c
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(2, v1).offset(0x67aaL).get();

    a0 = v1;
    v1 = a0 << 3;
    at = 0x800f_0000L;
    at = at + v1;
    a0 = MEMORY.ref(1, at).offset(0x224eL).get();

    MEMORY.ref(4, v0).offset(0x1e4L).setu(a0);

    FUN_800e28dc(0x28L, 0x1L);
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

    v0 = MEMORY.ref(4, v1).offset(0x1e4L).get();
    v1 = 0x1L;
    if(v0 == v1) {
      //LAB_800e0114
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

      v1 = MEMORY.ref(4, v0).offset(0x10L).get();
      v0 = 0x2000L;
      MEMORY.ref(4, v1).offset(0xfcL).setu(v0);
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

      v1 = MEMORY.ref(4, v0).offset(0x10L).get();
      v0 = 0x2000L;
      MEMORY.ref(4, v1).offset(0x100L).setu(v0);
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

      v1 = MEMORY.ref(4, v0).offset(0x10L).get();
      v0 = 0x2000L;
      MEMORY.ref(4, v1).offset(0x104L).setu(v0);
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x6798L).get();
      v1 = 0x7L;
      if(v0 == v1) {
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x10L).get();
        v0 = 0x1000L;
        MEMORY.ref(4, v1).offset(0xfcL).setu(v0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x10L).get();
        v0 = 0x1000L;
        MEMORY.ref(4, v1).offset(0x100L).setu(v0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x10L).get();
        v0 = 0x1000L;
        MEMORY.ref(4, v1).offset(0x104L).setu(v0);
      }

      //LAB_800e01b8
    } else if((int)v0 >= 0x2L) {
      //LAB_800e00a4
      v1 = 0x2L;
      if(v0 == v1) {
        //LAB_800e01c0
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x14L).get();

        MEMORY.ref(4, v1).offset(0xfcL).setu(0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x14L).get();

        MEMORY.ref(4, v1).offset(0x100L).setu(0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x14L).get();

        MEMORY.ref(4, v1).offset(0x104L).setu(0);
      } else if(v0 == 0x3L) {
        //LAB_800e0210
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x18L).get();

        MEMORY.ref(4, v1).offset(0xfcL).setu(0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x18L).get();

        MEMORY.ref(4, v1).offset(0x100L).setu(0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x18L).get();

        MEMORY.ref(4, v1).offset(0x104L).setu(0);
      }
    } else if(v0 == 0) {
      //LAB_800e00c4
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

      v1 = MEMORY.ref(4, v0).offset(0xcL).get();
      v0 = 0x800L;
      MEMORY.ref(4, v1).offset(0xfcL).setu(v0);
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

      v1 = MEMORY.ref(4, v0).offset(0xcL).get();
      v0 = 0x666L;
      MEMORY.ref(4, v1).offset(0x100L).setu(v0);
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

      v1 = MEMORY.ref(4, v0).offset(0xcL).get();
      v0 = 0x800L;
      MEMORY.ref(4, v1).offset(0x104L).setu(v0);
    }

    //LAB_800e0260
  }

  @Method(0x800e0274L)
  public static void FUN_800e0274() {
    final WMapStruct258 struct = struct258_800c66a8.deref();

    if(struct._250.get() != 0x2L) {
      struct._1e4.set(_800f224e.offset(_800c67aa.get() * 0x8L).get());
    } else {
      //LAB_800e02d0
      struct._1e4.set(0x2L);
    }

    //LAB_800e02e0
    FUN_800214bc(struct.bigStructs_0c.get((int)struct._1e4.get()).deref());
    FUN_80020b98(struct.bigStructs_0c.get((int)struct._1e4.get()).deref());

    final long v0 = struct._1e4.get();
    if(v0 == 0x1L) {
      //LAB_800e0404
      GsSetAmbient(0x800L, 0x800L, 0x800L);

      struct.bigStructs_0c.get(1).deref().scaleVector_fc.setX(0x2000);
      struct.bigStructs_0c.get(1).deref().scaleVector_fc.setY(0x2000);
      struct.bigStructs_0c.get(1).deref().scaleVector_fc.setZ(0x2000);

      if(_800c6798.get() == 0x7L) {
        struct.bigStructs_0c.get(1).deref().scaleVector_fc.setX(0x1000);
        struct.bigStructs_0c.get(1).deref().scaleVector_fc.setY(0x1000);
        struct.bigStructs_0c.get(1).deref().scaleVector_fc.setZ(0x1000);
      }

      //LAB_800e04bc
      //LAB_800e0380
    } else if(v0 == 0x2L) {
      //LAB_800e04c4
      GsSetAmbient(0x800L, 0x800L, 0x800L);
    } else if(v0 == 0x3L) {
      //LAB_800e04e0
      GsSetAmbient(0x800L, 0x800L, 0x800L);
    } else if(v0 == 0) {
      //LAB_800e03a0
      GsSetAmbient(0xc80L, 0xc80L, 0xc80L);

      struct.bigStructs_0c.get(0).deref().scaleVector_fc.setX(0x800);
      struct.bigStructs_0c.get(0).deref().scaleVector_fc.setY(0x666);
      struct.bigStructs_0c.get(0).deref().scaleVector_fc.setZ(0x800);
    }

    //LAB_800e04fc
    struct.bigStructs_0c.get((int)struct._1e4.get()).deref().us_a0.set(0x4e);
    FUN_800211d8(struct.bigStructs_0c.get((int)struct._1e4.get()).deref());
    GsSetAmbient(_800c66b0.deref().svec_14c.getX(), _800c66b0.deref().svec_14c.getY(), _800c66b0.deref().svec_14c.getZ());
    FUN_800e06d0();
    FUN_800e1364();
  }

  @Method(0x800e05c4L)
  public static void FUN_800e05c4() {
    long v0;
    long v1;
    long a0;

    //LAB_800e05d8
    for(int i = 0; i < 4; i++) {
      //LAB_800e05f4
      FUN_80020fe0(struct258_800c66a8.deref().bigStructs_0c.get(i).deref());
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
      v1 = i;

      a0 = v1;
      v1 = a0 << 2;
      v0 = v0 + v1;
      a0 = MEMORY.ref(4, v0).offset(0x1b4L).get();

      removeFromLinkedList(a0);
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
      v1 = i;

      a0 = v1;
      v1 = a0 << 2;
      v0 = v0 + v1;
      a0 = MEMORY.ref(4, v0).offset(0xcL).get();

      removeFromLinkedList(a0);
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
      v1 = i;

      a0 = v1;
      v1 = a0 << 2;
      v0 = v0 + v1;
      MEMORY.ref(4, v0).offset(0xcL).setu(0);
    }

    //LAB_800e06b4
    FUN_800e3230();
  }

  @Method(0x800e06d0L)
  public static void FUN_800e06d0() {
    long at;
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long t0;
    long t1;
    long t2;
    long t3;
    long t4;
    long sp38;
    long sp3c = 0;
    long sp40;
    long sp44;
    long sp48;

    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();
    v0 = v0 + 0x84L;
    v1 = v1 + 0x94L;
    t1 = MEMORY.ref(4, v1).offset(0x0L).get();
    t2 = MEMORY.ref(4, v1).offset(0x4L).get();
    t3 = MEMORY.ref(4, v1).offset(0x8L).get();
    t4 = MEMORY.ref(4, v1).offset(0xcL).get();
    MEMORY.ref(4, v0).offset(0x0L).setu(t1);
    MEMORY.ref(4, v0).offset(0x4L).setu(t2);
    MEMORY.ref(4, v0).offset(0x8L).setu(t3);
    MEMORY.ref(4, v0).offset(0xcL).setu(t4);
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

    v0 = MEMORY.ref(4, v1).offset(0x250L).get();
    if(v0 == 0x1L) {
      //LAB_800e0770
      sp38 = 0;

      //LAB_800e0774
      while(sp38 < 0x6L) {
        //LAB_800e0790
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(2, v0).offset(0x67a8L).get();
        v1 = sp38;

        a0 = v1;
        v1 = a0 << 3;
        at = 0x800f_0000L;
        at = at + v1;
        a0 = MEMORY.ref(4, at).offset(-0x968L).get();

        if(v0 == a0) {
          v1 = (sp38 * 0x2L + 0x1L) * 0x4L;
          at = 0x800f_0000L;
          at = at + v1;
          v0 = MEMORY.ref(4, at).offset(-0x968L).get();

          sp3c = v0;
          break;
        }

        //LAB_800e07f8
        sp38++;
      }

      //LAB_800e0810
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(2, v0).offset(0x67a8L).get();

      final SVECTOR sp0x18 = new SVECTOR();
      final SVECTOR sp0x20 = new SVECTOR();
      FUN_800e0d70(v0, sp0x18);
      FUN_800e0d70(sp3c, sp0x20);

      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

      v0 = MEMORY.ref(4, v1).offset(0x248L).get();

      //LAB_800e0878
      if(v0 == 0 || v0 == 0x1L) {
        if(v0 == 0) {
          //LAB_800e0898
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

          MEMORY.ref(4, v0).offset(0x24cL).setu(0);
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
          v1 = 0x1L;
          MEMORY.ref(4, v0).offset(0x248L).setu(v1);
        }

        //LAB_800e08b8
        FUN_800e3304();

        a0 = 0x800c_0000L;
        a0 = MEMORY.ref(4, a0).offset(0x66a8L).get();

        a1 = MEMORY.ref(4, a0).offset(0x24cL).get();

        final VECTOR sp0x28 = new VECTOR();
        FUN_800e0e4c(sp0x28, sp0x18, sp0x20, 0x20L, a1);

        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
        v1 = sp0x28.getX();

        MEMORY.ref(4, v0).offset(0x94L).setu(v1);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
        v1 = sp0x28.getY();

        MEMORY.ref(4, v0).offset(0x98L).setu(v1);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
        v1 = sp0x28.getZ();

        MEMORY.ref(4, v0).offset(0x9cL).setu(v1);
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(4, v1).offset(0x24cL).get();

        v1 = a0 + 0x1L;
        a0 = v1;
        MEMORY.ref(4, v0).offset(0x24cL).setu(a0);
        if((int)a0 >= 0x21L) {
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
          v1 = 0x2L;
          MEMORY.ref(4, v0).offset(0x248L).setu(v1);
        }

        //LAB_800e0980
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x24cL).get();

        v0 = v1;
        v1 = v0 << 9;
        a0 = v1;

        v0 = rsin(a0);
        a0 = v0;
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = MEMORY.ref(4, v1).offset(0x18L).get();
        a1 = 0x800c_0000L;
        a1 = MEMORY.ref(4, a1).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, a1).offset(0x18L).get();
        a2 = 0x800c_0000L;
        a2 = MEMORY.ref(4, a2).offset(0x66a8L).get();

        a1 = MEMORY.ref(4, a2).offset(0x18L).get();
        a2 = 0x800c_0000L;
        a2 = MEMORY.ref(4, a2).offset(0x66a8L).get();

        a3 = MEMORY.ref(4, a2).offset(0x24cL).get();

        t0 = a3;
        a2 = t0 << 6;
        a3 = a0 << 8;
        a0 = (int)a3 >> 12;
        a2 = a2 + a0;
        a0 = a2;
        MEMORY.ref(4, a1).offset(0x100L).setu(a0);
        MEMORY.ref(4, v1).offset(0x104L).setu(a0);
        MEMORY.ref(4, v0).offset(0xfcL).setu(a0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();
        a0 = 0x800c_0000L;
        a0 = MEMORY.ref(4, a0).offset(0x66a8L).get();

        a1 = MEMORY.ref(4, a0).offset(0x1e4L).get();

        a0 = a1;
        a1 = a0 << 2;
        a0 = v1 + a1;
        v1 = MEMORY.ref(4, a0).offset(0xcL).get();
        a0 = 0x800c_0000L;
        a0 = MEMORY.ref(4, a0).offset(0x66b0L).get();

        a1 = MEMORY.ref(2, a0).offset(0x72L).get();

        MEMORY.ref(2, v1).offset(0x76L).setu(a1);
        MEMORY.ref(2, v0).offset(0xa6L).setu(a1);
      } else if(v0 == 0x2L) {
        //LAB_800e0a6c
        v0 = 0x800c_0000L;
        v0 = v0 - 0x52bcL;
        sp48 = v0;
        v0 = sp3c;

        sp40 = v0;
        v1 = sp40;

        v0 = v1 & 0x1fL;
        v1 = 0x1L;
        v0 = v1 << v0;
        v1 = v0;
        sp44 = v1;
        v1 = sp40;

        v0 = v1 >>> 5;
        v1 = v0;
        sp40 = v1;
        v0 = sp40;

        v1 = v0;
        v0 = v1 << 2;
        v1 = sp48;

        v0 = v0 + v1;
        v1 = sp40;

        a0 = v1;
        v1 = a0 << 2;
        a0 = sp48;

        v1 = v1 + a0;
        a0 = MEMORY.ref(4, v1).offset(0x0L).get();
        a1 = sp44;

        v1 = a0 | a1;
        a0 = v1;
        MEMORY.ref(4, v0).offset(0x0L).setu(a0);

        //LAB_800e0b64
        v0 = sp3c;

        a0 = v0;
        v1 = a0 << 2;
        v1 = v1 + v0;
        v0 = v1 << 2;
        at = 0x800f_0000L;
        at = at + v0;
        v1 = MEMORY.ref(2, at).offset(0xe3cL).get();
        at = 0x800c_0000L;
        MEMORY.ref(2, at).offset(0x6860L).setu(v1);
        v0 = sp3c;

        a0 = v0;
        v1 = a0 << 2;
        v1 = v1 + v0;
        v0 = v1 << 2;
        at = 0x800f_0000L;
        at = at + v0;
        v1 = MEMORY.ref(2, at).offset(0xe3eL).get();
        at = 0x800c_0000L;
        MEMORY.ref(2, at).offset(0x6862L).setu(v1);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(2, v0).offset(0x6860L).get();
        at = 0x8005_0000L;
        MEMORY.ref(4, at).offset(0x2c30L).setu(v0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(2, v0).offset(0x6862L).get();
        at = 0x8005_0000L;
        MEMORY.ref(4, at).offset(0x2c34L).setu(v0);
        a0 = 0x1L;

        FUN_800e3fac((int)a0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
        v1 = 0x3L;
        MEMORY.ref(4, v0).offset(0x248L).setu(v1);
      } else if(v0 == 0x3L) {
        //LAB_800e0c00
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = MEMORY.ref(4, v1).offset(0x18L).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(4, v1).offset(0x18L).get();

        v1 = MEMORY.ref(4, a0).offset(0xfcL).get();

        a0 = v1 - 0x400L;
        MEMORY.ref(4, v0).offset(0xfcL).setu(a0);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, v0).offset(0x18L).get();

        v0 = MEMORY.ref(4, v1).offset(0xfcL).get();

        if((int)v0 < 0) {
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

          v1 = MEMORY.ref(4, v0).offset(0x18L).get();

          MEMORY.ref(4, v1).offset(0xfcL).setu(0);
        }

        //LAB_800e0c70
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        v0 = MEMORY.ref(4, v1).offset(0x18L).get();
        a0 = 0x800c_0000L;
        a0 = MEMORY.ref(4, a0).offset(0x66a8L).get();

        v1 = MEMORY.ref(4, a0).offset(0x18L).get();
        a0 = 0x800c_0000L;
        a0 = MEMORY.ref(4, a0).offset(0x66a8L).get();

        a1 = MEMORY.ref(4, a0).offset(0x18L).get();

        a0 = MEMORY.ref(4, a1).offset(0xfcL).get();

        MEMORY.ref(4, v1).offset(0x100L).setu(a0);
        MEMORY.ref(4, v0).offset(0x104L).setu(a0);
      }

      //LAB_800e0cbc
    } else if(v0 == 0) {
      //LAB_800e0760
      FUN_800e8a10();
    }

    //LAB_800e0cc4
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

    v0 = v1 + 0xa4L;
    a0 = 0x800c_0000L;
    a0 = MEMORY.ref(4, a0).offset(0x66a8L).get();

    v1 = a0 + 0xa4L;
    a0 = MEMORY.ref(2, v1).offset(0x0L).get();

    v1 = a0 & 0xfffL;
    MEMORY.ref(2, v0).offset(0x0L).setu(v1);
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

    v0 = v1 + 0xa4L;
    a0 = 0x800c_0000L;
    a0 = MEMORY.ref(4, a0).offset(0x66a8L).get();

    v1 = a0 + 0xa4L;
    a0 = MEMORY.ref(2, v1).offset(0x2L).get();

    v1 = a0 & 0xfffL;
    MEMORY.ref(2, v0).offset(0x2L).setu(v1);
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

    v0 = v1 + 0xa4L;
    a0 = 0x800c_0000L;
    a0 = MEMORY.ref(4, a0).offset(0x66a8L).get();

    v1 = a0 + 0xa4L;
    a0 = MEMORY.ref(2, v1).offset(0x4L).get();

    v1 = a0 & 0xfffL;
    MEMORY.ref(2, v0).offset(0x4L).setu(v1);

    FUN_800e10a0();
  }

  @Method(0x800e0d70L)
  public static void FUN_800e0d70(long a0, SVECTOR a1) {
    long at;
    long v0;
    long v1;
    long sp0;
    long sp10 = a0;
    sp0 = 0;

    //LAB_800e0d84
    do {
      v0 = sp0;

      if((int)v0 >= 0x6L) {
        break;
      }

      //LAB_800e0da0
      v0 = sp0;

      a0 = v0;
      v1 = a0 << 1;
      v1 = v1 + v0;
      v0 = v1 << 2;
      v1 = sp10;
      at = 0x800f_0000L;
      at = at + v0;
      v0 = MEMORY.ref(4, at).offset(-0x938L).get();

      if(v1 == v0) {
        a0 = 0x800f_0000L;
        a0 = a0 - 0x934L;
        v1 = a0 + sp0 * 12;
        a1.set(MEMORY.ref(4, v1).cast(SVECTOR::new));
        break;
      }

      //LAB_800e0e24
      v0 = sp0;

      v1 = v0 + 0x1L;
      sp0 = v1;
    } while(true);

    //LAB_800e0e3c
  }

  @Method(0x800e0e4cL)
  public static void FUN_800e0e4c(VECTOR a0, SVECTOR a1, SVECTOR a2, long a3, long a4) {
    long v0;
    long v1;
    long sp18;
    long sp10;

    if(a3 == a4) {
      a0.setX(a2.getX() * 0x1000);
      a0.setY(a2.getY() * 0x1000);
      a0.setZ(a2.getZ() * 0x1000);
    } else {
      //LAB_800e0ed8
      sp18 = ((long)((a2.getX() - a1.getX()) * 0x1000 / (int)a3) * (int)a4) & 0xffff_ffffL;

      v1 = a1.getX() + (int)sp18 / 0x1000;
      a0.setX((int)(v1 * 0x1000));

      v1 = (a2.getY() - a1.getY()) * 0x1000 / (int)a3;
      sp18 = ((long)(int)v1 * (int)a4) & 0xffff_ffffL;

      v1 = (int)sp18 >> 12;
      v0 = a1.getY() + v1;
      v1 = v0 << 12;
      sp10 = v1;

      v0 = 0x800L / (int)a3;
      sp18 = ((long)(int)v0 * (int)a4) & 0xffff_ffffL;

      a0.setY((int)(sp10 + rsin(sp18) * -200));

      sp18 = ((long)(((a2.getZ() - a1.getZ()) * 0x1000) / (int)a3) * (int)a4) & 0xffff_ffffL;

      v1 = a1.getZ() + (int)sp18 / 0x1000;
      a0.setZ((int)(v1 * 0x1000));
    }

    //LAB_800e108c
  }

  @Method(0x800e10a0L) //TODO this might control player animation?
  public static void FUN_800e10a0() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;

    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

    a0 = MEMORY.ref(4, v1).offset(0xb0L).get();

    MEMORY.ref(4, v0).offset(0xacL).setu(a0);
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

    v0 = v1 + 0x84L;
    a0 = 0x800c_0000L;
    a0 = MEMORY.ref(4, a0).offset(0x66a8L).get();

    v1 = a0 + 0x94L;
    v0 = MEMORY.ref(4, v0).offset(0x0L).get();
    v1 = MEMORY.ref(4, v1).offset(0x0L).get();

    if(v0 != v1 || struct258_800c66a8.deref().vec_84.getY() != struct258_800c66a8.deref().vec_94.getY() || struct258_800c66a8.deref().vec_84.getZ() != struct258_800c66a8.deref().vec_94.getZ()) {
      //LAB_800e117c
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(2, v0).offset(-0x124aL).get();

      //LAB_800e11b0
      if(v0 >= 0x7fL && _800beebc.get() != 0 || (joypadInput_8007a39c.get() & 0x40L) != 0) {
        //LAB_800e11d0
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
        v1 = 0x4L;
        MEMORY.ref(4, v0).offset(0xb0L).setu(v1);
        a0 = 0x2L;

        FUN_800e367c(a0);
      } else {
        //LAB_800e11f4
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
        v1 = 0x3L;
        MEMORY.ref(4, v0).offset(0xb0L).setu(v1);
        a0 = 0x1L;

        FUN_800e367c(a0);
      }

      //LAB_800e1210
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

      v1 = MEMORY.ref(4, v0).offset(0x1e4L).get();
      v0 = 0x1L;
      if(v1 == v0) {
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(-0x4f04L).get();

        v1 = v0 & 0x3L;
        if(v1 == 0) {
          playSound(0xc, 0, 0, 0, (short)0, (short)0);
        }
      }
    } else {
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
      v1 = 0x2L;
      MEMORY.ref(4, v0).offset(0xb0L).setu(v1);
    }

    //LAB_800e1264
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

    v0 = MEMORY.ref(4, v1).offset(0x1e4L).get();

    if((int)v0 < 0x4L) {
      if((int)v0 >= 0x1L) {
        //LAB_800e1298
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
        v1 = 0x2L;
        MEMORY.ref(4, v0).offset(0xb0L).setu(v1);
      }
    }

    //LAB_800e12b0
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();
    v0 = MEMORY.ref(4, v0).offset(0xacL).get();
    v1 = MEMORY.ref(4, v1).offset(0xb0L).get();

    if(v0 != v1) {
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

      a0 = MEMORY.ref(4, v1).offset(0x1e4L).get();

      v1 = a0;
      a0 = v1 << 2;
      v0 = v0 + a0;
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();
      a0 = 0x800c_0000L;
      a0 = MEMORY.ref(4, a0).offset(0x66a8L).get();

      a0 = MEMORY.ref(4, a0).offset(0xb0L).get() * 0x4L;
      a1 = 0x800c_0000L;
      a1 = MEMORY.ref(4, a1).offset(0x66a8L).get();

      a2 = MEMORY.ref(4, a1).offset(0x1e4L).get() * 0x40L;
      a0 = a0 + a2;
      v1 = v1 + a0;

      FUN_80021584(MEMORY.ref(4, v0).offset(0xcL).deref(4).cast(BigStruct::new), MEMORY.ref(4, v1).offset(0xb4L).deref(4).cast(TmdAnimationFile::new)); //TODO
    }

    //LAB_800e1354
  }

  @Method(0x800e1364L)
  public static void FUN_800e1364() {
    FUN_800e32a8();

    final WMapStruct258 struct = struct258_800c66a8.deref();
    struct.coord2_34.coord.transfer.setX(struct.vec_94.getX() >> 12);
    struct.coord2_34.coord.transfer.setY(struct.vec_94.getY() >> 12);
    struct.coord2_34.coord.transfer.setZ(struct.vec_94.getZ() >> 12);
    struct.bigStructs_0c.get((int)struct._1e4.get()).deref().coord2_14.coord.transfer.set(struct.coord2_34.coord.transfer);

    if(struct._250.get() == 0) {
      long sp10 = struct.rotation_a4.getY() - struct.bigStructs_0c.get((int)struct._1e4.get()).deref().coord2Param_64.rotate.getY();
      long sp14 = struct.rotation_a4.getY() - (struct.bigStructs_0c.get((int)struct._1e4.get()).deref().coord2Param_64.rotate.getY() - 0x1000L);

      if(Math.abs(sp14) < Math.abs(sp10)) {
        sp10 = sp14;
      }

      //LAB_800e15e4
      struct.bigStructs_0c.get((int)struct._1e4.get()).deref().coord2Param_64.rotate.y.add((short)(sp10 / 2));
      struct.bigStructs_0c.get((int)struct._1e4.get()).deref().coord2Param_64.rotate.setX(struct.rotation_a4.getX());
      struct.bigStructs_0c.get((int)struct._1e4.get()).deref().coord2Param_64.rotate.setZ(struct.rotation_a4.getZ());
    }

    //LAB_800e16f8
    rotateCoord2(struct.rotation_a4, struct.coord2_34);
  }

  @Method(0x800e1740L)
  public static void FUN_800e1740() {
    final MATRIX sp0x28 = new MATRIX();
    final SVECTOR sp0x48 = new SVECTOR();
    final SVECTOR sp0x50 = new SVECTOR();
    final SVECTOR sp0x58 = new SVECTOR();
    final Ref<Long> sp0x64 = new Ref<>();
    final Ref<Long> sp0x68 = new Ref<>();

    GsGetLs(struct258_800c66a8.deref().bigStructs_0c.get((int)struct258_800c66a8.deref()._1e4.get()).deref().coord2_14, sp0x28);
    setRotTransMatrix(sp0x28);

    //LAB_800e17b4
    for(int i = 0; i < 8; i++) {
      //LAB_800e17d0
      final long sp70 = linkedListAddress_1f8003d8.get();
      linkedListAddress_1f8003d8.addu(0x1cL);
      setGpuPacketType(0x7L, sp70, true, false);

      final long sp74 = linkedListAddress_1f8003d8.get();
      linkedListAddress_1f8003d8.addu(0x8L);

      MEMORY.ref(1, sp74).offset(0x3L).setu(0x1L);
      MEMORY.ref(4, sp74).offset(0x4L).setu(0xe100_0000L | _800bb118.get() & 0x9ffL);
      MEMORY.ref(1, sp70).offset(0x4L).setu(0x80L);
      MEMORY.ref(1, sp70).offset(0x5L).setu(0x80L);
      MEMORY.ref(1, sp70).offset(0x6L).setu(0x80L);
      MEMORY.ref(1, sp70).offset(0xcL).setu(0);
      MEMORY.ref(1, sp70).offset(0xdL).setu(0);
      MEMORY.ref(1, sp70).offset(0xeL).setu(0);
      MEMORY.ref(1, sp70).offset(0x14L).setu(0);
      MEMORY.ref(1, sp70).offset(0x15L).setu(0);
      MEMORY.ref(1, sp70).offset(0x16L).setu(0);

      //TODO
      final long v0 = 0x800c_0000L;
      sp0x50.set(
        (short)MEMORY.ref(4, v0).offset(0x66a8L).deref(2).offset(0x1c4L).offset(i * 0x4L).get(),
        (short)0,
        (short)MEMORY.ref(4, v0).offset(0x66a8L).deref(2).offset(0x1c6L).offset(i * 0x4L).get()
      );

      sp0x58.set(
        (short)MEMORY.ref(4, v0).offset(0x66a8L).deref(2).offset(0x1c4L).offset(((i + 1) & 0x7L) * 0x4L).get(),
        (short)0,
        (short)MEMORY.ref(4, v0).offset(0x66a8L).deref(2).offset(0x1c6L).offset(((i + 1) & 0x7L) * 0x4L).get()
      );

      final long sp6c = FUN_8003f930(sp0x48, sp0x50, sp0x58, sp70 + 0x8L, sp70 + 0x10L, sp70 + 0x18L, sp0x68, sp0x64);

      if(sp6c >= 0x3L && sp6c < _1f8003c8.get()) {
        insertElementIntoLinkedList(tags_1f8003d0.deref().get((int)sp6c).get() + 0x138L, sp70);
        insertElementIntoLinkedList(tags_1f8003d0.deref().get((int)sp6c).get() + 0x138L, sp74);
      }

      //LAB_800e1a98
    }

    //LAB_800e1ab0
  }

  @Method(0x800e28dcL)
  public static void FUN_800e28dc(final long a0, final long a1) {
    final long a3 = a0 * a1;

    struct258_800c66a8.deref().vecs_224.set(MEMORY.ref(4, addToLinkedListTail(a3 * 0x10L), UnboundedArrayRef.of(0x10, VECTOR::new, () -> (int)a3)));
    struct258_800c66a8.deref().vecs_228.set(MEMORY.ref(4, addToLinkedListTail(a3 * 0x10L), UnboundedArrayRef.of(0x10, VECTOR::new, () -> (int)a3)));
    struct258_800c66a8.deref().ptr_22c.set(addToLinkedListTail(a3 * 0x4L));
    struct258_800c66a8.deref()._230.set(0);
    struct258_800c66a8.deref()._234.set(a3 - 0x1L);
    struct258_800c66a8.deref()._238.set(a3);
    struct258_800c66a8.deref()._23c.set(a1);

    //LAB_800e29f8
    //NOTE: there's a bug in the original code, it just sets the first vector in the array over and over again
    for(int i = 0; i < a3; i++) {
      //LAB_800e2a18
      struct258_800c66a8.deref().vecs_224.deref().get(i).set(0, 0, 0);
      struct258_800c66a8.deref().vecs_228.deref().get(i).set(0, 0, 0);
    }

    //LAB_800e2ac0
    struct258_800c66a8.deref()._244.set(0);
  }

  @Method(0x800e3230L)
  public static void FUN_800e3230() {
    removeFromLinkedList(struct258_800c66a8.deref().vecs_224.getPointer());
    removeFromLinkedList(struct258_800c66a8.deref().vecs_228.getPointer());
    removeFromLinkedList(struct258_800c66a8.deref().ptr_22c.get());
  }

  @Method(0x800e32a8L)
  public static void FUN_800e32a8() {
    _800ef684.offset(struct258_800c66a8.deref()._1e4.get() * 0x4L).deref(4).call();
  }

  @Method(0x800e3304L)
  public static void FUN_800e3304() {
    assert false;
  }

  @Method(0x800e367cL)
  public static void FUN_800e367c(final long a0) {
    if(fileCount_8004ddc8.get() != 0) {
      return;
    }

    //LAB_800e36a8
    if(_800c6698.get() != 0x5L) {
      return;
    }

    //LAB_800e36c4
    if(_800c669c.get() != 0x5L) {
      return;
    }

    //LAB_800e36e0
    if((int)struct258_800c66a8.deref()._1e4.get() >= 0x2L) {
      return;
    }

    //LAB_800e3708
    if(_800c6870.get() != 0) {
      return;
    }

    //LAB_800e3724
    if(struct258_800c66a8.deref()._05.get() != 0) {
      return;
    }

    //LAB_800e3748
    if(_800c686c.get() != 0 || _800c6870.get() != 0) {
      //LAB_800e3778
      return;
    }

    //LAB_800e3780
    //LAB_800e3794
    _800c6ae8.addu(_800f224b.offset(_800c67aa.get() * 0x8L).get() * a0 * 70);

    if((int)_800c6ae8.get() >= 5120) {
      _800c6ae8.setu(0);

      if(_800f224c.offset(_800c67aa.get() * 0x8L).getSigned() == -0x1L) {
        submapStage_800bb0f4.setu(0x1L);
      } else {
        //LAB_800e386c
        submapStage_800bb0f4.setu(_800f224c.offset(_800c67aa.get() * 0x8L).get());
      }

      //LAB_800e3894
      final long sp10 = _800f224d.offset(_800c67aa.get() * 0x8L).get();

      if((byte)sp10 == -0x1L) {
        submapScene_800bb0f8.setu(0);
      } else {
        //LAB_800e38dc
        final long sp14 = FUN_800133ac() % 100;

        if((int)sp14 < 0x23L) {
          submapScene_800bb0f8.setu(_800ef364.offset(sp10 * 0x8L).getSigned());
        } else {
          //LAB_800e396c
          if((int)sp14 < 0x23L || (int)sp14 >= 0x46L) {
            //LAB_800e39c0
            if((int)sp14 < 0x46L || (int)sp14 >= 0x5aL) {
              //LAB_800e3a14
              submapScene_800bb0f8.setu(_800ef36a.offset(sp10 * 0x8L).getSigned());
            } else {
              submapScene_800bb0f8.setu(_800ef368.offset(sp10 * 0x8L).getSigned());
            }
          } else {
            submapScene_800bb0f8.setu(_800ef366.offset(sp10 * 0x8L).getSigned());
          }
        }
      }

      //LAB_800e3a38
      // Store data when transitioning to combat?
      _800bb0a6.setu(_800c67aa.get());
      _800bb0a0.setu(pathIndex_800c67ac.get());
      _800bb0a2.setu(dotIndex_800c67ae.get());
      _800bb0a4.setu(dotOffset_800c67b0.get() & 0xff);
      _800bb0a5.setu(facing_800c67b4.get() & 0xff);
      pregameLoadingStage_800bb10c.setu(0x8L);
    }

    //LAB_800e3a94
  }

  @Method(0x800e3aa8L)
  public static WMapTmdRenderingStruct18 loadTmd(final TmdWithId tmd) {
    final WMapTmdRenderingStruct18 sp10 = MEMORY.ref(4, addToLinkedListTail(0x18L), WMapTmdRenderingStruct18::new);
    sp10.count_0c.set(allocateTmdRenderer(sp10, tmd));

    //LAB_800e3b00
    return sp10;
  }

  @Method(0x800e3b14L)
  public static void deallocateTmdRenderer(final WMapTmdRenderingStruct18 a0) {
    removeFromLinkedList(a0._10.getPointer());
    removeFromLinkedList(a0.rotations_08.getPointer());
    removeFromLinkedList(a0.coord2s_04.getPointer());
    removeFromLinkedList(a0.dobj2s_00.getPointer());
    removeFromLinkedList(a0.tmd_14.getPointer());
    removeFromLinkedList(a0.getAddress());
  }

  @Method(0x800e3bd4L)
  public static long allocateTmdRenderer(final WMapTmdRenderingStruct18 a0, final TmdWithId tmd) {
    adjustTmdPointers(tmd.tmd);

    final long nobj = tmd.tmd.header.nobj.get();
    a0.dobj2s_00.set(MEMORY.ref(4, addToLinkedListTail(nobj * 0x10L), UnboundedArrayRef.of(0x10, GsDOBJ2::new)));
    a0.coord2s_04.set(MEMORY.ref(4, addToLinkedListTail(nobj * 0x50L), UnboundedArrayRef.of(0x50, GsCOORDINATE2::new)));
    a0.rotations_08.set(MEMORY.ref(4, addToLinkedListTail(nobj * 0x08L), UnboundedArrayRef.of(0x8, SVECTOR::new)));
    a0._10.set(MEMORY.ref(4, addToLinkedListTail(nobj * 0x04L), UnboundedArrayRef.of(0x4, UnsignedIntRef::new)));

    //LAB_800e3d24
    for(int i = 0; i < nobj; i++) {
      //LAB_800e3d44
      updateTmdPacketIlen(tmd.tmd.objTable, a0.dobj2s_00.deref().get(i), i);
    }

    //LAB_800e3d80
    //LAB_800e3d94
    return nobj;
  }

  @Method(0x800e3da8L)
  public static void initTmdTransforms(final WMapTmdRenderingStruct18 a0, @Nullable final GsCOORDINATE2 superCoord) {
    //LAB_800e3dfc
    for(int i = 0; i < a0.count_0c.get(); i++) {
      final GsDOBJ2 dobj2 = a0.dobj2s_00.deref().get(i);
      final GsCOORDINATE2 coord2 = a0.coord2s_04.deref().get(i);
      final SVECTOR rotation = a0.rotations_08.deref().get(i);

      //LAB_800e3e20
      GsInitCoordinate2(superCoord, coord2);

      dobj2.coord2_04.set(coord2);
      coord2.coord.transfer.setX(0);
      coord2.coord.transfer.setY(0);
      coord2.coord.transfer.setZ(0);
      rotation.setX((short)0);
      rotation.setY((short)0);
      rotation.setZ((short)0);
    }

    //LAB_800e3ee8
  }

  @Method(0x800e3efcL)
  public static void setAllCoord2Attribs(final WMapTmdRenderingStruct18 a0, final long attribute) {
    //LAB_800e3f24
    for(int i = 0; i < a0.count_0c.get(); i++) {
      final GsDOBJ2 sp4 = a0.dobj2s_00.deref().get(i);

      //LAB_800e3f48
      sp4.attribute_00.set(attribute);
    }

    //LAB_800e3f9c
  }

  @Method(0x800e3facL)
  public static void FUN_800e3fac(final int a0) {
    struct258_800c66a8.deref()._00.set(0);
    struct258_800c66a8.deref()._04.set(0);
    struct258_800c66a8.deref()._05.set(a0 + 1);
  }

  @Method(0x800e3ff0L)
  public static void FUN_800e3ff0() {
    if(struct258_800c66a8.deref()._05.get() != 0) {
      //LAB_800e4020
      _800f01fc.offset((struct258_800c66a8.deref()._05.get() - 0x1L) * 0x4L).deref(4).call();
    }

    //LAB_800e4058
  }

  @Method(0x800e406cL)
  public static void FUN_800e406c() {
    long v0 = struct258_800c66a8.deref()._250.get();
    if(v0 == 0x1L) {
      //LAB_800e442c
      v0 = struct258_800c66a8.deref()._04.get();
      if(v0 == 0x1L) {
        //LAB_800e4564
        struct258_800c66a8.deref()._00.incr();

        if((int)struct258_800c66a8.deref()._00.get() >= 0xfL) {
          struct258_800c66a8.deref()._04.set(2);
          struct258_800c66a8.deref()._00.set(0);
        }

        //LAB_800e45c0
        return;
      }

      if((int)v0 < 0x2L) {
        //LAB_800e4478
        if(v0 == 0 && ((int)_800c6698.get() >= 0x3L || (int)_800c669c.get() >= 0x3L)) {//LAB_800e44b0
          scriptStartEffect(0x2L, 0xfL);

          _800c66b0.deref()._11a.set(1);
          _800c66b0.deref().coord2_20.coord.transfer.setX(0);
          _800c66b0.deref().coord2_20.coord.transfer.setY(0);
          _800c66b0.deref().coord2_20.coord.transfer.setZ(0);
          _800c66b0.deref()._9a.set(0);
          _800c66b0.deref().rotation_70.setY((short)0);

          FUN_800d4bc8(0x1L);

          _800c66b0.deref()._c4.set(0);
          struct258_800c66a8.deref()._1f8.set(0);
          struct258_800c66a8.deref()._04.set(1);
        }

        //LAB_800e455c
        //LAB_800e4684
        //LAB_800e468c
      } else {
        //LAB_800e4464
        if(v0 == 0x2L) {
          //LAB_800e45c8
          if(_800c669c.get() >= 0x3L) {
            struct258_800c66a8.deref()._00.incr();

            if((int)struct258_800c66a8.deref()._00.get() >= 0x2L) {
              _800c686c.setu(0);
            }
          }

          //LAB_800e4624
          if(_800c66b0.deref()._c5.get() == 0 && _800c686c.get() == 0) {
            _800c6868.setu(0);
            struct258_800c66a8.deref()._05.set(0);
            struct258_800c66a8.deref()._04.set(2);
          }
        }

        //LAB_800e467c
      }
    } else {
      if((int)v0 < 0x2L) {
        if(v0 != 0) {
          return;
        }
      } else {
        //LAB_800e40ac
        if(v0 != 0x2L) {
          return;
        }
      }

      //LAB_800e40c0
      v0 = struct258_800c66a8.deref()._04.get();
      if(v0 == 0x1L) {
        //LAB_800e4304
        struct258_800c66a8.deref()._00.incr();

        if((int)struct258_800c66a8.deref()._00.get() >= 0xfL) {
          struct258_800c66a8.deref()._04.set(2);
          struct258_800c66a8.deref()._00.set(0);
        }

        //LAB_800e4360
      } else if((int)v0 < 0x2L) {
        //LAB_800e410c
        //LAB_800e42fc
        if(v0 == 0 && ((int)_800c6698.get() >= 0x3L || (int)_800c669c.get() >= 0x3L)) {
          //LAB_800e4144
          scriptStartEffect(0x2L, 0xfL);

          _800c66b0.deref()._00._04.set(-9000);
          _800c66b0.deref()._00._10.set(9000);
          _800c66b0.deref()._11a.set(1);
          _800c66b0.deref().coord2_20.coord.transfer.setX(0);
          _800c66b0.deref().coord2_20.coord.transfer.setY(0);
          _800c66b0.deref().coord2_20.coord.transfer.setZ(0);
          _800c66b0.deref()._9e.set((short)-300);
          _800c66b0.deref()._9a.set(0);
          _800c66b0.deref().rotation_70.setY((short)0);

          FUN_800d4bc8(0x1L);

          _800c66b0.deref().vec_a4.setX(struct258_800c66a8.deref().coord2_34.coord.transfer.getX() * 0x100 / 30);
          _800c66b0.deref().vec_a4.setY(struct258_800c66a8.deref().coord2_34.coord.transfer.getY() * 0x100 / 30);
          _800c66b0.deref().vec_a4.setZ(struct258_800c66a8.deref().coord2_34.coord.transfer.getZ() * 0x100 / 30);

          _800c66b0.deref()._c4.set(0);
          struct258_800c66a8.deref()._1f8.set(0);
          _800c66b0.deref()._c5.set(2);
          struct258_800c66a8.deref()._04.set(1);
        }
        //LAB_800e40f8
      } else if(v0 == 0x2L) {
        //LAB_800e4368
        if((int)_800c669c.get() >= 0x3L) {
          struct258_800c66a8.deref()._00.incr();

          if((int)struct258_800c66a8.deref()._00.get() >= 0x2L) {
            _800c686c.setu(0);
          }
        }

        //LAB_800e43c4
        if(_800c66b0.deref()._c5.get() == 0 && _800c686c.get() == 0) {
          _800c6868.setu(0);
          struct258_800c66a8.deref()._05.set(0);
          struct258_800c66a8.deref()._04.set(2);
        }

        //LAB_800e441c
        //LAB_800e4424
      }
    }
  }

  @Method(0x800e469cL)
  public static void FUN_800e469c() {
    long at;
    long v0;
    long v1;
    long a0;
    long a1;

    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();
    v0 = MEMORY.ref(1, v1).offset(0x4L).get();
    v1 = 0x1L;
    if(v0 == v1) {
      //LAB_800e4738
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
      v1 = 0x2L;
      MEMORY.ref(1, v0).offset(0x110L).setu(v1);
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(2, v0).offset(0x6794L).get();

      v1 = v0 + -0x10L;
      at = 0x800c_0000L;
      MEMORY.ref(2, at).offset(0x6794L).setu(v1);
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(2, v0).offset(0x6794L).getSigned();

      if((int)v0 < 0) {
        at = 0x800c_0000L;
        MEMORY.ref(2, at).offset(0x6794L).setu(0);
      }

      //LAB_800e477c
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

      a0 = MEMORY.ref(4, v1).offset(0x0L).get();

      v1 = a0 + 0x1L;
      a0 = v1;
      MEMORY.ref(4, v0).offset(0x0L).setu(a0);
      if((int)a0 >= 0x1eL) {
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
        v1 = 0x2L;
        MEMORY.ref(1, v0).offset(0x4L).setu(v1);
      }

      //LAB_800e47c8
    } else if((int)v0 >= 0x2L) {
      //LAB_800e46dc
      v1 = 0x2L;
      if(v0 == v1) {
        //LAB_800e47d0
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

        a0 = MEMORY.ref(2, v1).offset(0x20L).get();

        v1 = a0 + -0x40L;
        MEMORY.ref(2, v0).offset(0x20L).setu(v1);
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

        v1 = MEMORY.ref(2, v0).offset(0x20L).getSigned();

        if((int)v1 < 0) {
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
          MEMORY.ref(2, v0).offset(0x20L).setu(0);
        }

        //LAB_800e4820
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
        v1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v1).offset(0x66b0L).get();

        a0 = MEMORY.ref(2, v1).offset(0x10eL).get();

        v1 = a0 + 0x1L;
        a0 = v1;
        MEMORY.ref(2, v0).offset(0x10eL).setu(a0);
        v1 = a0 << 16;
        v0 = (int)v1 >> 16;
        if((int)v0 >= 0x10L) {
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

          v1 = MEMORY.ref(2, v0).offset(0x20L).getSigned();

          if(v1 == 0) {
            v0 = 0x800c_0000L;
            v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

            MEMORY.ref(1, v0).offset(0x5L).setu(0);
            v0 = 0x8005_0000L;
            v0 = MEMORY.ref(4, v0).offset(0x2c30L).get();
            v1 = 0x3e7L;
            if(v0 != v1) {
              v0 = 0x7L;
              at = 0x800c_0000L;
              MEMORY.ref(4, at).offset(-0x4ef4L).setu(v0);
            } else {
              //LAB_800e48b8
              v0 = 0x9L;
              at = 0x800c_0000L;
              MEMORY.ref(4, at).offset(-0x4ef4L).setu(v0);
            }

            //LAB_800e48c4
            v0 = 0x800c_0000L;
            v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
            v1 = MEMORY.ref(4, v0).offset(0x250L).get();
            v0 = 0x2L;
            if(v1 == v0) {
              v0 = 0x7L;
              at = 0x800c_0000L;
              MEMORY.ref(4, at).offset(-0x4ef4L).setu(v0);
            } else {
              //LAB_800e48f4
              v0 = 0x800c_0000L;
              v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
              v1 = MEMORY.ref(4, v0).offset(0x250L).get();
              v0 = 0x3L;
              if(v1 == v0) {
                v0 = 0x9L;
                at = 0x800c_0000L;
                MEMORY.ref(4, at).offset(-0x4ef4L).setu(v0);
              }
            }
          }
        }

        //LAB_800e491c
      }
    } else if(v0 == 0) {
      //LAB_800e46f0
      a0 = 0x1L;
      a1 = 0x1eL;

      scriptStartEffect(a0, a1);
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();
      v1 = 0x1L;
      MEMORY.ref(1, v0).offset(0x110L).setu(v1);
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66b0L).get();

      MEMORY.ref(2, v0).offset(0x10eL).setu(0);
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();
      v1 = 0x1L;
      MEMORY.ref(1, v0).offset(0x4L).setu(v1);
    }

    //LAB_800e4924
  }

  //TODO What is this rendering? Currently disabled since it's broken
  @Method(0x800e4934L)
  public static void FUN_800e4934(final McqHeader mcq, long a1, long a2, long x, long y, long a5, long a6, long colour) {
    boolean a = true;
    if(a) {
      return;
    }

    long sp18;

    long v0 = linkedListAddress_1f8003d8.get();

    long sp1c = v0;
    long sp14 = v0;
    long clutY = a1 + mcq._0c.get();
    long clutXDiv16 = a2 + mcq._0e.get();
    long width = mcq._14.get();
    long height = mcq._16.get();
    long u = a1 + mcq._10.get();
    long v = a2 + mcq._12.get();
    long sp30 = u & 0x3c0L;
    long sp34 = v & 0x100L;
    u = u * 0x4L;
    MEMORY.ref(1, sp1c).offset(0x3L).setu(0x1L);
    MEMORY.ref(4, sp1c).offset(0x4L).setu(0xe100_0200L | (_800bb114.offset((sp34 & 0x100L) / 0x100L * 0x2L).get() | (sp30 & 0x3c0L) / 0x40L) & 0x9ffL);
    sp18 = sp1c;
    sp1c = sp1c + 0x8L;
    MEMORY.ref(4, sp18, GsOT_TAG::new).p.set(sp1c & 0xff_ffffL);

    //LAB_800e4ad0
    for(long offsetX = 0; offsetX < width; offsetX += 0x10) {
      //LAB_800e4af0
      //LAB_800e4af4
      for(long offsetY = 0; offsetY < height; offsetY += 0x10) {
        //LAB_800e4b14
        MEMORY.ref(1, sp1c).offset(0x3L).setu(0x3L);
        MEMORY.ref(4, sp1c).offset(0x4L).setu(0x7c80_8080L); // Textured rect, 16x16, opaque, texture-blending

        gpuLinkedListSetCommandTransparency(sp1c, true);
        MEMORY.ref(2, sp1c).offset(0x8L).setu(x + offsetX); // X
        MEMORY.ref(2, sp1c).offset(0xaL).setu(y + offsetY); // Y
        MEMORY.ref(1, sp1c).offset(0xcL).setu(u); // U
        MEMORY.ref(1, sp1c).offset(0xdL).setu(v); // V
        MEMORY.ref(2, sp1c).offset(0xeL).setu(clutXDiv16 << 6 | (clutY & 0x3f0L) / 0x10L);
        MEMORY.ref(1, sp1c).offset(0x4L).setu(colour); // R
        MEMORY.ref(1, sp1c).offset(0x5L).setu(colour); // G
        MEMORY.ref(1, sp1c).offset(0x6L).setu(colour); // B
        sp18 = sp1c;
        sp1c = sp1c + 0x10L;
        MEMORY.ref(4, sp18, GsOT_TAG::new).p.set(sp1c & 0xff_ffffL);
        v = (v + 0x10L) & 0xf0L;

        if(v == 0) {
          u = (u + 0x10L) & 0xfcL;

          if(u == 0) {
            sp30 = sp30 + 0x40L;
            MEMORY.ref(1, sp1c).offset(0x3L).setu(0x1L);
            MEMORY.ref(4, sp1c).offset(0x4L).setu(0xe100_0200L | (_800bb114.offset((sp34 & 0x100L) / 0x100L * 0x2L).get() | ((sp30 & 0x3c0L) / 0x40L)) & 0x9ffL);

            sp18 = sp1c;
            sp1c = sp1c + 0x8L;
            MEMORY.ref(4, sp18, GsOT_TAG::new).p.set(sp1c & 0xff_ffffL);
          }
        }

        //LAB_800e4d18
        clutXDiv16 = (clutXDiv16 + 0x1L) & 0xffL;

        if(clutXDiv16 == 0) {
          clutY = clutY + 0x10L;
        }

        //LAB_800e4d4c
        clutXDiv16 = clutXDiv16 | sp34;
      }

      //LAB_800e4d78
    }

    //LAB_800e4d90
    linkedListAddress_1f8003d8.setu(sp1c);

    MEMORY.ref(4, sp18, GsOT_TAG::new).p.set(tags_1f8003d0.deref().get((int)a5).p);
    tags_1f8003d0.deref().get((int)a5).p.set(sp14 & 0xff_ffffL);
  }

  @Method(0x800e4e1cL)
  public static void FUN_800e4e1c() {
    _800c66b8.and(0xffff_fffeL);
    loadDrgnBinFile(0, 5696L, 0, getMethodAddress(WMap.class, "FUN_800d562c", Value.class, long.class, long.class), 0, 0x4L);
    _800c6794.setu(0);
  }

  @Method(0x800e4e84L)
  public static void FUN_800e4e84() {
    if((_800c66b8.get() & 0x1L) == 0) {
      return;
    }

    //LAB_800e4eac
    if(struct258_800c66a8.deref()._05.get() != 0x2L) {
      _800c6794.addu(0x10L);

      if(_800c6794.getSigned() > 0x20L) {
        _800c6794.setu(0x20L);
      }
    }

    //LAB_800e4f04
    FUN_800e4934(mcqHeader_800c6768, 0x140L, 0, -0xa0L, -0x78L, _1f8003c8.get() - 0x3L, 0, _800c6794.get() & 0xff);

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
    fillMemory(sp0x50.getAddress(), 0, 0x4L);
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
      0x2L,
      0x9L,
      0xeL,
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

    fillMemory(sp0x60.getAddress(), 0, 0x4L);
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
      0x1L,
      0x9L,
      0xdL,
      0,
      0,
      0
    ));

    sp0x60tmp.release();
    sp0x68tmp.release();
    sp0x70tmp.release();
  }

  @Method(0x800e5150L)
  public static void FUN_800e5150() {
    long v1;
    long a0;
    long sp20;
    long sp28;
    long sp2c;
    long sp30;
    long sp34;
    long sp38;
    long sp3c;
    final Ref<Long> sp0x38 = new Ref<>();
    final Ref<Long> sp0x3c = new Ref<>();
    final Ref<Long> sp0x40 = new Ref<>();
    final Ref<Long> sp0x44 = new Ref<>();
    long sp50;
    long sp54;
    long sp58;
    long sp5c;

    if(_800c6690.get() != 0) {
      return;
    }

    //LAB_800e5178
    if(fileCount_8004ddc8.get() != 0) {
      return;
    }

    //LAB_800e5194
    if(_800c6894.get() != 0x1L) {
      FUN_800e69e8();
      return;
    }

    //LAB_800e51b8
    if(_800c66b0.deref()._c5.get() != 0) {
      return;
    }

    //LAB_800e51dc
    if(_800c66b0.deref()._c4.get() != 0) {
      return;
    }

    //LAB_800e5200
    if(struct258_800c66a8.deref()._1f8.get() != 0) {
      return;
    }

    //LAB_800e5224
    if(struct258_800c66a8.deref()._220.get() != 0) {
      return;
    }

    //LAB_800e5248
    switch((int)_800c68a4.get()) {
      case 0:
        sp2c = -_800f2248.offset(_800c6874.get() * 0x8L).getSigned();

        //LAB_800e52cc
        for(sp28 = 0; sp28 < _800c67a4.get() && _800f2248.offset(sp28 * 0x8L).getSigned() != sp2c; sp28++) {
          //LAB_800e52f0
          //LAB_800e5324
          DebugHelper.sleep(0);
        }

        //LAB_800e533c
        FUN_800ea4dc(sp28);

        facing_800c67b4.neg();

        FUN_800eab94(_800c67a8.get());

        _800c6868.setu(0x1L);
        _800c6894.setu(0x1L);

        //LAB_800e5394
        for(int i = 0; i < 8; i++) {
          //LAB_800e53b0
          FUN_8002a3ec(i, 0);
        }

        //LAB_800e53e0
        _800bdf00.setu(0xdL);
        _800c6860.setu(_800f0e34.get((int)_800c67a8.get())._08.get());
        _800c6862.setu(_800f0e34.get((int)_800c67a8.get())._0a.get());
        _800c68a4.setu(0x1L);

        if(_800f0234.get(_800f0e34.get((int)_800c67a8.get())._02.get()).text_00.isNull()) {
          _800c68a4.setu(0x8L);
        }

        //LAB_800e54c4
        _800c6898.deref()._34.set((short)0);
        _800c86d0.setu(0x100L);
        _800c86d2.setu(0);
        break;

      case 1:
        _800c66b8.and(0xffff_f7ffL);

        loadDrgnBinFile(0, 5655 + _800f0234.get(_800f0e34.get((int)_800c67a8.get())._02.get())._04.get(), 0, getMethodAddress(WMap.class, "FUN_800d5768", Value.class, long.class, long.class), 0x1L, 0x4L);
        FUN_8002a32c(7, 0x1L, 0xf0L, 0x78L, 0xeL, 0x10L);

        _800c68a4.setu(0x2L);

        playSound(0, 4, 0, 0, (short)0, (short)0);

        //LAB_800e55f0
        for(int i = 0; i < 4; i++) {
          //LAB_800e560c
          v1 = _800f0234.get(_800f0e34.get((int)_800c67a8.get())._02.get())._06.get(i).get();

          if(v1 > 0) {
            playSound(0xc, (int)v1, 0, 0, (short)0, (short)0);
          }

          //LAB_800e5698
        }

        //LAB_800e56b0
        break;

      case 2:
        if(FUN_8002a488(0x7L) != 0) {
          FUN_8002a32c(6, 0, 0xf0L, 0x46L, 0xdL, 0x7L);
          _800c68a4.setu(0x3L);
        }

        //LAB_800e5700
        break;

      case 3:
        _800c6898.deref()._34.add((short)0x40);

        FUN_800ce4dc(_800c6898.deref());

        if(_800c6860.get() == 999L) {
          sp38 = (_800c6862.get() >>> 4) & 0xffffL;
          sp3c = _800c6862.get() & 0xfL;

          FUN_800e7624(_800f01e4.deref(), sp0x40, sp0x44);
          FUN_800e774c(_800f01e4.deref(), (short)(0xf0L - sp0x40.get() * 3), 0xa4L, 0, 0);
          FUN_800e7624(_800f01ec.get((int)sp38).deref(), sp0x40, sp0x44);
          FUN_800e774c(_800f01ec.get((int)sp38).deref(), (short)(0xf0L - sp0x40.get() * 3), 0xb6L, 0, 0);
          FUN_800e7624(_800f01ec.get((int)sp3c).deref(), sp0x40, sp0x44);
          FUN_800e774c(_800f01ec.get((int)sp3c).deref(), (short)(0xf0L - sp0x40.get() * 3), 0xc8L, 0, 0);

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
          _800c689c.deref()._3a.set((short)_800c86d2.getSigned() * 18 + 8);
        } else {
          //LAB_800e5a18
          FUN_800e7624(_800f01e4.deref(), sp0x40, sp0x44);
          FUN_800e774c(_800f01e4.deref(), (short)(0xf0L - sp0x40.get() * 3), 0xaaL, 0, 0);
          FUN_800e7624(_800f01e8.deref(), sp0x40, sp0x44);
          FUN_800e774c(_800f01e8.deref(), (short)(0xf0L - sp0x40.get() * 3), 0xbeL, 0, 0);

          if((joypadPress_8007a398.get() & 0x5000L) != 0) {
            _800c86d2.xoru(0x1L);

            playSound(0, 1, 0, 0, (short)0, (short)0);
          }

          //LAB_800e5b38
          _800c689c.deref()._3a.set((short)_800c86d2.getSigned() * 20 + 14);
        }

        //LAB_800e5b68
        FUN_800ce4dc(_800c689c.deref());

        sp30 = _800f0e34.get((int)_800c67a8.get())._02.get();
        FUN_800e7624(_800f0234.get((int)sp30).text_00.deref(), sp0x3c, sp0x38);
        FUN_800e774c(_800f0234.get((int)sp30).text_00.deref(), (short)(0xf0L - sp0x3c.get() * 3), (short)(0x8cL - sp0x38.get() * 7), 0, 0);

        if((_800c66b8.get() & 0x800L) != 0) {
          long sp4c = linkedListAddress_1f8003d8.get();
          linkedListAddress_1f8003d8.addu(0x28L);

          setGpuPacketType(0xcL, sp4c, false, false);

          a0 = (_800ef0d6.get() & 0x100L) >> 8;
          MEMORY.ref(2, sp4c).offset(0x16L).setu(_800bb120.offset(a0 * 0x2L).get() | (_800ef0d4.get() & 0x3c0L) >> 6);
          MEMORY.ref(2, sp4c).offset(0xeL).setu(_800ef0da.get() << 6 | (_800ef0d8.get() & 0x3f0L) >> 4);

          sp50 = _800c67a8.get();
          sp54 = 0x1L << (sp50 & 0x1fL);
          sp50 = sp50 >>> 5;

          if((_800bad44.offset(sp50 * 0x4L).get() & sp54) > 0) {
            //LAB_800e5e98
            MEMORY.ref(1, sp4c).offset(0x4L).setu(_800c86d0.getSigned() / 2);
            MEMORY.ref(1, sp4c).offset(0x5L).setu(_800c86d0.getSigned() / 2);
            MEMORY.ref(1, sp4c).offset(0x6L).setu(_800c86d0.getSigned() / 2);
          } else {
            //LAB_800e5e18
            MEMORY.ref(1, sp4c).offset(0x4L).setu(_800c86d0.getSigned() * 0x30L / 0x100L);

            //LAB_800e5e50
            MEMORY.ref(1, sp4c).offset(0x5L).setu(_800c86d0.getSigned() * 0x30L / 0x100L);

            //LAB_800e5e88
            MEMORY.ref(1, sp4c).offset(0x6L).setu(_800c86d0.getSigned() * 0x30L / 0x100L);
          }

          //LAB_800e5f04
          if(_800f0e34.get((int)_800c67a8.get())._10.get() != 0) {
            MEMORY.ref(1, sp4c).offset(0x4L).setu(_800c86d0.getSigned() / 2);
            MEMORY.ref(1, sp4c).offset(0x5L).setu(_800c86d0.getSigned() / 2);
            MEMORY.ref(1, sp4c).offset(0x6L).setu(_800c86d0.getSigned() / 2);
          }

          //LAB_800e5fa4
          MEMORY.ref(2, sp4c).offset(0x08L).setu(0x15L);
          MEMORY.ref(2, sp4c).offset(0x0aL).setu(-0x60L);
          MEMORY.ref(1, sp4c).offset(0x0cL).setu(0);
          MEMORY.ref(1, sp4c).offset(0x0dL).setu(0);
          MEMORY.ref(2, sp4c).offset(0x10L).setu(0x8dL);
          MEMORY.ref(2, sp4c).offset(0x12L).setu(-0x60L);
          MEMORY.ref(1, sp4c).offset(0x14L).setu(0x77L);
          MEMORY.ref(1, sp4c).offset(0x15L).setu(0);
          MEMORY.ref(2, sp4c).offset(0x18L).setu(0x15L);
          MEMORY.ref(2, sp4c).offset(0x1aL).setu(-0x6L);
          MEMORY.ref(1, sp4c).offset(0x1cL).setu(0);
          MEMORY.ref(1, sp4c).offset(0x1dL).setu(0x59L);
          MEMORY.ref(2, sp4c).offset(0x20L).setu(0x8dL);
          MEMORY.ref(2, sp4c).offset(0x22L).setu(-0x6L);
          MEMORY.ref(1, sp4c).offset(0x24L).setu(0x77L);
          MEMORY.ref(1, sp4c).offset(0x25L).setu(0x59L);

          insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x38L, sp4c);

          if((joypadPress_8007a398.get() & 0x80L) != 0 && _800c6860.get() != 999L) {
            playSound(0, 2, 0, 0, (short)0, (short)0);
          }

          //LAB_800e60d0
          if((joypadInput_8007a39c.get() & 0x80L) != 0 && _800c6860.get() != 999L) {
            _800c86d0.subu(0x80L);

            if(_800c86d0.getSigned() < 0x80L) {
              _800c86d0.setu(0x40L);
            }

            //LAB_800e6138
            sp30 = _800f0e34.get((int)_800c67a8.get())._02.get();
            sp5c = _800f0234.get((int)sp30)._05.get();

            //LAB_800e619c
            sp50 = 0;
            for(int i = 0; i < 5; i++) {
              //LAB_800e61b8
              if((sp5c & (1L << i)) != 0) {
                FUN_800e774c(_800f01cc.get(i).deref(), 0xcdL, (short)(sp50 * 0x10L + 0x1eL), 0, 0);
                sp50++;
              }

              //LAB_800e6248
            }

            //LAB_800e6260
            if(sp50 == 0) {
              FUN_800e774c(_800f01e0.deref(), 0xc9L, 0x3eL, 0, 0);
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
              sp34 = _800f0234.get(_800f0e34.get((int)_800c67a8.get())._02.get())._06.get(i).get();

              if((int)sp34 > 0) {
                FUN_80019c80(0xcL, sp34, 0x1L);
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
              sp34 = _800f0234.get(_800f0e34.get((int)_800c67a8.get())._02.get())._06.get(i).get();

              if((int)sp34 > 0) {
                FUN_80019c80(0xcL, sp34, 0x1L);
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
              sp34 = _800f0234.get(_800f0e34.get((int)_800c67a8.get())._02.get())._06.get(i).get();

              if((int)sp34 > 0) {
                FUN_80019c80(0xcL, sp34, 0x1L);
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

        if(_800be358.get(6)._00.get() == 0 && _800be358.get(7)._00.get() == 0 && _800c6898.deref()._34.get() == 0) {
          _800c68a4.setu(0x9L);
        }

        //LAB_800e66cc
        break;

      case 6:
        if(_800c6858.getSigned() != 0) {
          _800c6858.setu(0);
          facing_800c67b4.setu(0x1L);
        } else {
          //LAB_800e6704
          _800c6858.setu(0x800L);
          facing_800c67b4.setu(-0x1L);
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
        sp54 = 0x1L << (sp58 & 0x1fL);
        sp58 = sp58 >>> 5;
        _800bad44.offset(sp58 * 0x4L).oru(sp54);

        //LAB_800e6900
        if(_800c6860.get() != 999L) {
          submapCut_80052c30.setu(_800c6860.get());
          _80052c34.setu(_800c6862.get());
        } else {
          //LAB_800e693c
          submapCut_80052c30.setu(_800f0e34.get((int)_800c67a8.get())._04.get());

          if(_800c86d2.getSigned() == 0x1L) {
            sp20 = (_800c6862.get() >>> 4) & 0xffffL;
          } else {
            //LAB_800e69a0
            sp20 = _800c6862.get() & 0xfL;
          }

          //LAB_800e69b8
          index_80052c38.set(sp20);
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
    final Ref<Long> sp0x70 = new Ref<>();
    final Ref<Long> sp0x74 = new Ref<>();

    if(_800c6690.get() != 0) {
      return;
    }

    //LAB_800e6a10
    if(struct258_800c66a8.deref()._1f8.get() == 0x4L) {
      return;
    }

    //LAB_800e6a34
    if(pregameLoadingStage_800bb10c.get() == 0x8L) {
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
    rotateCoord2(struct258_800c66a8.deref().tmdRendering_08.deref().rotations_08.deref().get(0), struct258_800c66a8.deref().tmdRendering_08.deref().coord2s_04.deref().get(0));

    //LAB_800e6c38
    long sp1c = 0;
    for(int i = 0; i < _800c86cc.get(); i++) {
      //LAB_800e6c5c
      if(!_800f0234.get(_800f0e34.get((int)_800c84c8.offset(i * 0x2L).getSigned())._02.get()).text_00.isNull()) {
        //LAB_800e6ccc
        sp0x30.setX((short)_800c74b8.offset(i * 0x10L).offset(0x0L).get());
        sp0x30.setY((short)_800c74b8.offset(i * 0x10L).offset(0x4L).get());
        sp0x30.setZ((short)_800c74b8.offset(i * 0x10L).offset(0x8L).get());

        GsGetLs(struct258_800c66a8.deref().tmdRendering_08.deref().coord2s_04.deref().get(0), sp0x38);
        setRotTransMatrix(sp0x38);

        CPU.MTC2(sp0x30.getXY(), 0); // VXY0
        CPU.MTC2(sp0x30.getZ(), 1); // VZ0
        CPU.COP2(0x18_0001L); // Perspective transform single
        sp58.setXY(CPU.MFC2(14)); // SXY2
        final long sp60 = CPU.MFC2(8); // IR0
        final long sp5c = CPU.CFC2(31); // FLAGS
        final long sp64 = (int)CPU.MFC2(19) >> 2; // SZ3
        final long sp6c = sp58.getX() + 0xa0L;
        final long sp6e = sp58.getY() + 0x68L;

        //LAB_800e6e24
        if((short)sp6c >= -0x20L && (short)sp6c < 0x161L) {
          //LAB_800e6e2c
          //LAB_800e6e5c
          if((short)sp6e >= -0x20L && (short)sp6e < 0x111L) {
            //LAB_800e6e64
            if((int)sp64 >= 0x6L && sp64 < _1f8003c8.get() - 0x1L) {
              _800c68ac.offset(sp1c * 0xcL).setu(sp64);
              _800c68b0.offset(sp1c * 0xcL).setu(_800c84c8.offset(i * 0x2L).getSigned());
              _800c68b4.offset(sp1c * 0xcL).setu(sp58.getXY());
              sp1c++;
            }
          }
        }
      }

      //LAB_800e6f3c
    }

    //LAB_800e6f54
    _800c68b0.offset(sp1c * 0xcL).setu(-0x1L);

    FUN_80013434(_800c68ac.getAddress(), sp1c, 0xcL, getMethodAddress(SMap.class, "FUN_800e7854", long.class, long.class)); //TODO getAddress

    //LAB_800e6fa0
    int i;
    for(i = 0; i < 7 && (int)_800c68b0.offset(i * 0xcL).get() >= 0; i++) {
      //LAB_800e6fec
      //LAB_800e6fec
      //LAB_800e6ff4
      final long sp6c = _800c68b4.offset(i * 0xcL).offset(2, 0x0L).getSigned() + 0xa0L;
      final long sp6e = _800c68b4.offset(i * 0xcL).offset(2, 0x2L).getSigned() + 0x68L;
      final long sp24 = _800f0e34.get((int)_800c68b0.offset(i * 0xcL).get())._02.get();

      if(!_800f0234.get((int)sp24).text_00.isNull()) {
        //LAB_800e70f4
        FUN_800e7624(_800f0234.get((int)sp24).text_00.deref(), sp0x70, sp0x74);

        final long v0 = _800c86d4.offset(i * 0x4L).get();
        if(v0 == 0x1L) {
          //LAB_800e71d8
          _800bdf00.setu(i + 0xeL);
          _800be358.get(i)._0c.set(i + 0xeL);

          if(sp0x70.get().intValue() >= 0x4L) {
            _800be358.get(i)._18.set(sp0x70.get().shortValue());
          } else {
            _800be358.get(i)._18.set((short)4);
          }

          //LAB_800e7298
          _800be358.get(i)._1a.set(sp0x74.get().shortValue());
          _800c86d4.offset(i * 0x4L).setu(0x2L);

          //LAB_800e72e8
          if(sp0x70.get().intValue() >= 0x4L) {
            _800be358.get(i)._18.set(sp0x70.get().shortValue());
          } else {
            _800be358.get(i)._18.set((short)4);
          }

          //LAB_800e735c
          _800be358.get(i)._1a.set(sp0x74.get().shortValue());
          _800be358.get(i)._1c.set(_800be358.get(i)._18.get() * 9 / 2);
          _800be358.get(i)._1e.set(_800be358.get(i)._1a.get() * 6);
          _800be358.get(i)._14.set((int)sp6c);
          _800be358.get(i)._16.set((int)sp6e);
        } else if((int)v0 >= 0x2L) {
          //LAB_800e7154
          if(v0 == 0x2L) {
            //LAB_800e72e8
            if(sp0x70.get().intValue() >= 0x4L) {
              _800be358.get(i)._18.set(sp0x70.get().shortValue());
            } else {
              _800be358.get(i)._18.set((short)4);
            }

            //LAB_800e735c
            _800be358.get(i)._1a.set(sp0x74.get().shortValue());
            _800be358.get(i)._1c.set(_800be358.get(i)._18.get() * 9 / 2);
            _800be358.get(i)._1e.set(_800be358.get(i)._1a.get() * 6);
            _800be358.get(i)._14.set((int)sp6c);
            _800be358.get(i)._16.set((int)sp6e);
          }
        } else if(v0 == 0) {
          //LAB_800e7168
          FUN_8002a32c(i, 0, (short)sp6c, (short)sp6e, (short)(sp0x70.get() - 1), (short)(sp0x74.get() - 1));

          _800c86d4.offset(i * 0x4L).setu(0x1L);

          //LAB_800e71d8
          _800bdf00.setu(i + 0xeL);
          _800be358.get(i)._0c.set(i + 0xeL);

          if(sp0x70.get().intValue() >= 0x4L) {
            _800be358.get(i)._18.set(sp0x70.get().shortValue());
          } else {
            _800be358.get(i)._18.set((short)4);
          }

          //LAB_800e7298
          _800be358.get(i)._1a.set(sp0x74.get().shortValue());
          _800c86d4.offset(i * 0x4L).setu(0x2L);

          //LAB_800e72e8
          if(sp0x70.get().intValue() >= 0x4L) {
            _800be358.get(i)._18.set(sp0x70.get().shortValue());
          } else {
            _800be358.get(i)._18.set((short)4);
          }

          //LAB_800e735c
          _800be358.get(i)._1a.set(sp0x74.get().shortValue());
          _800be358.get(i)._1c.set(_800be358.get(i)._18.get() * 9 / 2);
          _800be358.get(i)._1e.set(_800be358.get(i)._1a.get() * 6);
          _800be358.get(i)._14.set((int)sp6c);
          _800be358.get(i)._16.set((int)sp6e);
        }

        //LAB_800e74d8
        _800bdf00.setu(i + 0x77L);
        _800be358.get(i)._0c.set(i + 0x77L);

        FUN_800e774c(_800f0234.get((int)sp24).text_00.deref(), (short)(sp6c - sp0x70.get() * 3), (short)(sp6e - sp0x74.get() * 7), 0, 0);
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
  public static void FUN_800e7624(final LodString a0, final Ref<Long> a1, final Ref<Long> a2) {
    long sp0 = 0;
    long sp4 = 0x1L;
    long sp8 = 0;

    //LAB_800e7648
    for(int sp18 = 0; a0.charAt(sp18) != 0xa0ffL; sp18++) {
      //LAB_800e7668
      if(a0.charAt(sp18) == 0xa1ffL) {
        sp4++;

        if((int)sp8 < (int)sp0) {
          sp8 = sp0;
        }

        //LAB_800e76c4
        sp0 = 0;
      } else {
        //LAB_800e76d0
        sp0++;
      }
    }

    //LAB_800e76f8
    if((int)sp0 < (int)sp8) {
      sp0 = sp8;
    }

    //LAB_800e771c
    a1.set(sp0);
    a2.set(sp4);
  }

  @Method(0x800e774cL)
  public static void FUN_800e774c(final LodString a0, final long a1, final long a2, final long a3, final long a4) {
    final Ref<Long> sp0x28 = new Ref<>();
    final Ref<Long> sp0x2c = new Ref<>();
    FUN_800e7624(a0, sp0x28, sp0x2c);
    renderText(a0, a1 - sp0x28.get() + 0x3L, (short)a2, (short)a3, (short)a4);
    renderText(a0, a1 - (sp0x28.get() - 0x1L) + 0x3L, a2 + 0x1L, 0x9L, (short)a4);
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
      _800bb080.offset(i * 0x4L).setu(_800bad24.offset(i * 0x4L).get());
    }

    //LAB_800e7940
    //LAB_800e7944
    for(int i = 0; _800f5a6c.offset(i * 0x2cL).getSigned() != -1; i++) {
      //LAB_800e7984
      long sp48 = _800f5a6c.offset(i * 0x2cL).getSigned();
      long sp4c = 0x1L << (sp48 & 0x1fL);
      sp48 = sp48 >>> 5;

      if((scriptFlags2_800bac84.get((int)sp48).get() & sp4c) > 0) {
        //LAB_800e7a38
        //LAB_800e7a3c
        for(int sp24 = 0; sp24 < 0x8L; sp24++) {
          //LAB_800e7a58
          _800bad24.offset(sp24 * 0x4L).setu(_800f5a70.offset(i * 0x2cL).offset(sp24 * 0x4L).get());
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
      if(_800f0e34.get(sp1c)._04.get() == _800c685c.getSigned() && _800f0e34.get(sp1c)._06.get() == _800c685e.getSigned()) {
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

    if((_800bad24.offset(sp50 * 0x4L).get() & sp4c) <= 0) {
      _800c685c.setu(0xdL);
      _800c685e.setu(0x11L);
      sp1c = 5;
    }

    //LAB_800e7cb8
    //LAB_800e7cbc
    int sp24;
    for(sp24 = 0; _800f0e34.get(sp24)._00.get() != -0x2L; sp24++) {
      //LAB_800e7cf4
      DebugHelper.sleep(0);
    }

    //LAB_800e7d0c
    _800c67a0.setu(sp24);

    //LAB_800e7d1c
    for(sp24 = 0; _800f2248.offset(sp24 * 0x8L).getSigned() != 0; sp24++) {
      //LAB_800e7d4c
      DebugHelper.sleep(0);
    }

    //LAB_800e7d64
    _800c67a4.setu(sp24);

    GsInitCoordinate2(null, struct258_800c66a8.deref().coord2_34);

    _800c6798.setu(_800f0e34.get(sp1c)._0e.get() - 0x1L);
    _800c679c.setu(_800c6798.get());
    _800bf0b0.setu(_800c6798.get());

    FUN_800ea630(sp1c);

    _800c6870.setu(0);

    long sp2c;
    if(_8004dd28.get() == 0x6L && _800c685c.get() != 999L) {
      sp2c = 0x1L;
    } else {
      sp2c = 0;
    }

    //LAB_800e7e2c
    if(_800c685e.get() == 0x1fL && _800c685c.get() == 279L) {
      sp2c = 0x1L;
    }

    //LAB_800e7e5c
    //LAB_800e7e88
    long sp48;
    if(sp2c == 0 && _800bdc34.get() == 0 || _80052c6c.get() != 0) {
      //LAB_800e844c
      _800c686c.setu(0x1L);
      _800c6868.setu(0x1L);
    } else {
      _800c67aa.setu(_800bb0a6.get());
      pathIndex_800c67ac.setu(_800bb0a0.get());
      dotIndex_800c67ae.setu(_800bb0a2.get());
      dotOffset_800c67b0.setu(_800bb0a4.get());
      facing_800c67b4.setu(_800bb0a5.getSigned());
      _800c686c.setu(0);
      _800c6868.setu(0);

      //LAB_800e7f00
      for(int i = 0; i < _800c67a0.get(); i++) {
        //LAB_800e7f24
        sp24 = _800f0e34.get(i)._00.get();

        if(sp24 != -0x1L) {
          //LAB_800e7f68
          if(FUN_800eb09c(i, -0x1L, null) == 0) {
            //LAB_800e7f88
            if(sp24 == _800c67aa.get()) {
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
      _800bf0b0.setu(_800c6798.get());

      if(_800f2248.offset(_800c67aa.get() * 0x8L).getSigned() < 0) {
        sp4c = -0x1L;
      } else {
        //LAB_800e8064
        sp4c = 0x1L;
      }

      //LAB_800e8068
      sp48 = pathPosPtrArr_800f591c.offset(pathIndex_800c67ac.get() * 0x4L).get();

      final long sp58;
      final long sp60;
      if((int)sp4c > 0) {
        struct258_800c66a8.deref().coord2_34.coord.transfer.setX((int)MEMORY.ref(4, sp48).offset(0x0L).get());
        struct258_800c66a8.deref().coord2_34.coord.transfer.setY((int)MEMORY.ref(4, sp48).offset(0x4L).get() - 2);
        struct258_800c66a8.deref().coord2_34.coord.transfer.setZ((int)MEMORY.ref(4, sp48).offset(0x8L).get());

        sp58 = MEMORY.ref(4, sp48).offset(0x00L).get() - MEMORY.ref(4, sp48).offset(0x10L).get();
        sp60 = MEMORY.ref(4, sp48).offset(0x08L).get() - MEMORY.ref(4, sp48).offset(0x18L).get();

        _800c6858.setu(0);
      } else {
        //LAB_800e8190
        sp50 = _800f5810.offset((Math.abs(_800f2248.offset(_800c67aa.get() * 0x8L).getSigned()) - 0x1L) * 0x4L).get() - 0x1L;
        sp58 = MEMORY.ref(4, sp48).offset(sp50 * 0x10L).get() - MEMORY.ref(4, sp48).offset((sp50 - 1) * 0x10L).get();
        sp60 = MEMORY.ref(4, sp48).offset(sp50 * 0x10L).offset(0x8L).get() - MEMORY.ref(4, sp48).offset((sp50 - 1) * 0x10L).offset(0x8L).get();

        struct258_800c66a8.deref().coord2_34.coord.transfer.setX((int)MEMORY.ref(4, sp48).offset(sp50 * 0x10L).offset(0x0L).get());
        struct258_800c66a8.deref().coord2_34.coord.transfer.setY((int)MEMORY.ref(4, sp48).offset(sp50 * 0x10L).offset(0x4L).get() - 2);
        struct258_800c66a8.deref().coord2_34.coord.transfer.setZ((int)MEMORY.ref(4, sp48).offset(sp50 * 0x10L).offset(0x8L).get());

        _800c6858.setu(0x800L);
      }

      //LAB_800e838c
      struct258_800c66a8.deref().rotation_a4.set((short)0, (short)0, (short)0);

      struct258_800c66a8.deref().rotation_a4.setY((short)ratan2(sp58, sp60));
      previousPlayerRotation_800c685a.setu(struct258_800c66a8.deref().rotation_a4.getY());
      struct258_800c66a8.deref().rotation_a4.y.add((short)_800c6858.get());

      _800c6890.setu(0);
      _800c6894.setu(0);
      _800bdc34.setu(0);
    }

    //LAB_800e8464
    if(_8004dd28.get() == 0x6L && _800c685c.get() == 999L) {
      submapCut_80052c30.setu(0);
    }

    //LAB_800e8494
    _800c6860.setu(_800f0e34.get(sp1c)._08.get());
    _800c6862.setu(_800f0e34.get(sp1c)._0a.get());

    final VECTOR sp0x58 = new VECTOR();
    final VECTOR sp0x68 = new VECTOR();
    final VECTOR sp0x78 = new VECTOR();

    getPathPositions(sp0x68, sp0x78);
    weightedAvg(0x4L - dotOffset_800c67b0.getSigned(), dotOffset_800c67b0.getSigned(), sp0x58, sp0x68, sp0x78);

    struct258_800c66a8.deref().coord2_34.coord.transfer.set(sp0x58).div(0x1000);
    struct258_800c66a8.deref().coord2_34.coord.transfer.y.sub(2);

    if(_800c685c.get() == 0xf2L) {
      if(_800c685e.get() == 0x3L) {
        sp50 = 0x8fL;
        sp4c = 0x1L << (sp50 & 0x1fL);
        sp50 = sp50 >>> 5;

        if((scriptFlags2_800bac84.get((int)sp50).get() & sp4c) > 0) {
          _800c686c.setu(0);
          _800c6870.setu(0x1L);
          _800c6868.setu(0x1L);
        }

        //LAB_800e8684
        sp50 = 0x90L;
        sp4c = 0x1L << (sp50 & 0x1fL);
        sp50 = sp50 >>> 5;

        if((scriptFlags2_800bac84.get((int)sp50).get() & sp4c) > 0) {
          _800c6868.setu(0x1L);
          _800c686c.setu(0x1L);
          _800c6870.setu(0);
        }
      }
    }

    //LAB_800e8720
    struct258_800c66a8.deref()._250.set(0);
    struct258_800c66a8.deref()._254.set(0);

    //LAB_800e8770
    //LAB_800e87a0
    //LAB_800e87d0
    //LAB_800e8800
    if(_800c685c.get() == 0x210L && _800c685e.get() == 0xdL || _800c685c.get() == 0x210L && _800c685e.get() == 0xeL || _800c685c.get() == 0x210L && _800c685e.get() == 0xfL || _800c685c.get() == 0x21cL && _800c685e.get() == 0x13L || _800c685c.get() == 0x23cL && _800c685e.get() == 0x17L) {
      //LAB_800e8830
      struct258_800c66a8.deref()._250.set(0x1L);
    } else {
      //LAB_800e8848
      if(_800c685c.get() == 0x211L) {
        if(_800c685e.get() == 0x29L) {
          struct258_800c66a8.deref()._250.set(0x2L);
          struct258_800c66a8.deref()._254.set(0x1L);

          sp48 = _800bad44.getAddress();
          sp50 = _800c67a8.get();
          sp4c = 0x1L << (sp50 & 0x1fL);
          sp50 = sp50 >>> 5;
          MEMORY.ref(4, sp48).offset(sp50 * 0x4L).oru(sp4c);
        }
      }
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
    if((int)_800c6698.get() >= 0x4L && (int)_800c669c.get() >= 0x4L) {
      //LAB_800e8a58
      FUN_800e8cb0();
      FUN_800e975c();
      FUN_800e9d68();
      FUN_800e5150();
      updatePlayer();
    }

    //LAB_800e8a80
  }

  @Method(0x800e8a90L)
  public static void FUN_800e8a90() {
    if(_800c6870.get() != 0) {
      _800c6868.setu(0x1L);

      if(struct258_800c66a8.deref()._05.get() != 0) {
        return;
      }

      //LAB_800e8ae0
    } else {
      //LAB_800e8ae8
      if(_800c686c.get() == 0) {
        return;
      }

      //LAB_800e8b04
      if((int)struct258_800c66a8.deref()._1e4.get() >= 0x2L) {
        return;
      }
    }

    //LAB_800e8b2c
    final long sp4;
    if(_800f2248.offset(_800c67aa.get() * 0x8L).getSigned() < 0) {
      sp4 = -0x1L;
    } else {
      //LAB_800e8b64
      sp4 = 0x1L;
    }

    //LAB_800e8b68
    long sp0;
    if((int)sp4 > 0) {
      sp0 = 0x1L;
    } else {
      //LAB_800e8b8c
      sp0 = -0x1L;
    }

    //LAB_800e8b94
    //LAB_800e8bc0
    if((int)sp4 < 0 && (int)facing_800c67b4.get() > 0 || (int)sp4 > 0 && (int)facing_800c67b4.get() < 0) {
      //LAB_800e8bec
      sp0 = -sp0;
    }

    //LAB_800e8bfc
    if(_800c686c.get() == 0x2L || _800c6870.get() == 0x2L) {
      //LAB_800e8c2c
      sp0 *= 0x2L;
    }

    //LAB_800e8c40
    if((int)sp0 < 0) {
      _800c6858.setu(0x800L);
      facing_800c67b4.setu(-0x1L);
    } else {
      //LAB_800e8c70
      _800c6858.setu(0);
      facing_800c67b4.setu(0x1L);
    }

    //LAB_800e8c84
    dotOffset_800c67b0.addu(sp0);

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
      FUN_800e9104();
    }

    //LAB_800e8cfc
    if(dotOffset_800c67b0.getSigned() >= 0x4L) {
      dotIndex_800c67ae.addu(0x1L);

      //LAB_800e8d48
      dotOffset_800c67b0.mod(4);

      final long sp10 = _800f5810.offset((Math.abs(_800f2248.offset(_800c67aa.get() * 0x8L).getSigned()) - 0x1L) * 0x4L).get() - 0x1L;

      if(dotIndex_800c67ae.getSigned() >= sp10) {
        dotIndex_800c67ae.setu(sp10 - 0x1L);
        dotOffset_800c67b0.setu(0x3L);
        _800c6890.setu(0x2L);
      }

      //LAB_800e8dfc
      //LAB_800e8e04
    } else if(dotOffset_800c67b0.getSigned() < 0) {
      dotIndex_800c67ae.subu(0x1L);
      dotOffset_800c67b0.addu(0x4L);

      if(dotIndex_800c67ae.getSigned() < 0) {
        dotIndex_800c67ae.setu(0);
        dotOffset_800c67b0.setu(0);
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
    long sp14 = 0x1L << (sp10 & 0x1fL);
    sp10 = sp10 >>> 5;

    if((scriptFlags2_800bac84.get((int)sp10).get() & sp14) > 0) {
      //LAB_800e8f24
      if(struct258_800c66a8.deref()._1e4.get() == 0x1L) {
        //LAB_800e8f48
        if(_800c6870.get() == 0) {
          //LAB_800e8f64
          if(struct258_800c66a8.deref()._05.get() != 0x2L) {
            //LAB_800e8f88
            if(struct258_800c66a8.deref()._05.get() == 0) {
              //LAB_800e8fac
              if(_800c66b0.deref()._110.get() == 0) {
                //LAB_800e8fd0
                if(struct258_800c66a8.deref()._1f8.get() == 0) {
                  //LAB_800e8ff4
                  if(_800c66b0.deref()._c5.get() == 0) {
                    //LAB_800e9018
                    if(_800c66b0.deref()._c4.get() == 0) {
                      //LAB_800e903c
                      if((_800c66b8.get() & 0x1L) != 0) {
                        //LAB_800e905c
                        if(_800c6690.get() == 0) {
                          //LAB_800e9078
                          if((joypadPress_8007a398.get() & 0x80L) != 0) {
                            if(_800c6894.get() != 0x1L) {
                              _800c6860.setu(_800f1580.get());
                              _800c6862.setu(_800f1582.get());
                              submapCut_80052c30.setu(_800c6860.get());
                              _80052c34.setu(_800c6862.get());
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

  /** TODO joypad stuff? */
  @Method(0x800e9104L)
  public static void FUN_800e9104() {
    long v0;
    long sp0;
    long sp2;
    long sp4;
    long spc;
    long sp10;
    long sp8;

    sp4 = 0;

    if(fileCount_8004ddc8.get() != 0) {
      return;
    }

    //LAB_800e912c
    if(struct258_800c66a8.deref()._05.get() != 0) {
      return;
    }

    //LAB_800e9150
    if((int)struct258_800c66a8.deref()._1e4.get() >= 0x2L) {
      return;
    }

    //LAB_800e9178
    if(_800c6698.get() != 0x5L) {
      return;
    }

    //LAB_800e9194
    if(_800c669c.get() != 0x5L) {
      return;
    }

    //LAB_800e91b0
    if(_800c6870.get() != 0) {
      return;
    }

    //LAB_800e91cc
    sp0 = (_800c66b0.deref().rotation_70.getY() - previousPlayerRotation_800c685a.get() - 0x700L) & 0xfffL;
    sp10 = (_800bee90.get() & 0xffffL) >>> 12;

    if(sp10 != 0) {
      v0 = (short)sp0;
      if((int)v0 < 0) {
        v0 = v0 + 0x1ffL;
      }

      //LAB_800e9240
      sp2 = (int)v0 >> 9;

      sp8 = sp10 & _800f0204.offset((short)sp2).get();
      spc = sp10 & _800f0210.offset((short)sp2).get();

      if(sp8 == 0 || spc != 0) {
        //LAB_800e92d0
        if(sp8 != 0 || spc == 0) {
          //LAB_800e9300
          sp4 = facing_800c67b4.getSigned();
        } else {
          sp4 = -0x1L;
        }
      } else {
        sp4 = 0x1L;
      }

      //LAB_800e9310
      if(sp4 == 0) {
        sp4 = facing_800c67b4.getSigned();
      }
    }

    //LAB_800e9330
    //LAB_800e9364
    if((_800bed60.get(0).sArr54.get(1).get() >= 0x7fL && _800beebc.get() != 0) || (joypadInput_8007a39c.get() & 0x40L) != 0) {
      //LAB_800e9384
      sp4 *= 2; // Running
    }

    //LAB_800e9398
    dotOffset_800c67b0.addu(sp4);

    if(sp4 != 0) {
      if((int)sp4 < 0) {
        _800c6858.setu(0x800L);
        facing_800c67b4.setu(-0x1L);
      } else {
        //LAB_800e93f4
        _800c6858.setu(0);
        facing_800c67b4.setu(0x1L);
      }
    }

    //LAB_800e9408
  }

  @Method(0x800e9418L)
  public static void getPathPositions(final VECTOR playerPos, final VECTOR nextPathPos) {
    final long sp00 = pathPosPtrArr_800f591c.offset(pathIndex_800c67ac.get() * 0x4L).get();

    final long v0 = sp00 + dotIndex_800c67ae.getSigned() * 0x10L;
    playerPos.set((VECTOR)MEMORY.ref(4, v0, VECTOR::new));

    final long v1 = sp00 + (dotIndex_800c67ae.getSigned() + 1) * 0x10L;
    nextPathPos.set((VECTOR)MEMORY.ref(4, v1, VECTOR::new));
  }

  @Method(0x800e94f0L)
  public static void weightedAvg(final long weight1, final long weight2, final VECTOR out, final VECTOR vec1, final VECTOR vec2) {
    out.setX((int)((weight1 * vec1.getX() + weight2 * vec2.getX()) / (weight1 + weight2) * 0x1000));
    out.setY((int)((weight1 * vec1.getY() + weight2 * vec2.getY()) / (weight1 + weight2) * 0x1000));
    out.setZ((int)((weight1 * vec1.getZ() + weight2 * vec2.getZ()) / (weight1 + weight2) * 0x1000));
  }

  @Method(0x800e9648L)
  public static void updatePlayerRotation() {
    struct258_800c66a8.deref().rotation_a4.setX((short)0);
    struct258_800c66a8.deref().rotation_a4.setY((short)0);
    struct258_800c66a8.deref().rotation_a4.setZ((short)0);

    struct258_800c66a8.deref().rotation_a4.setY((short)ratan2(playerPos_800c67b8.getX() - nextDotPos_800c67c8.getX(), playerPos_800c67b8.getZ() - nextDotPos_800c67c8.getZ()));

    previousPlayerRotation_800c685a.setu(struct258_800c66a8.deref().rotation_a4.getY());

    struct258_800c66a8.deref().rotation_a4.y.add((short)_800c6858.get());
  }

  @Method(0x800e975cL)
  public static void FUN_800e975c() {
    long v0;
    long v1;
    long a0;
    final VECTOR sp0x10 = new VECTOR();
    final VECTOR sp0x20 = new VECTOR();
    final VECTOR sp0x30 = new VECTOR();
    long sp40;
    long sp4c;
    long sp50;
    long sp54;
    long sp58;

    if(_800c6890.get() == 0) {
      return;
    }

    //LAB_800e9784
    //LAB_800e9788
    for(int i = 0; i < 7; i++) {
      //LAB_800e97a4
      _800c6874.offset(i * 0x4L).setu(-0x1L);
    }

    //LAB_800e97dc
    getPathPositions(sp0x10, sp0x20);

    if(_800c6890.get() == 0x1L) {
      sp0x30.set(sp0x10);
    } else {
      //LAB_800e9834
      sp0x30.set(sp0x20);
    }

    //LAB_800e985c
    sp4c = 0;

    //LAB_800e9864
    for(int i = 0; i < _800c67a0.get(); i++) {
      //LAB_800e9888
      if(FUN_800eb09c(i, 0, null) == 0) {
        //LAB_800e98a8
        if(_800f0e34.get(i)._06.get() != -0x1L) {
          //LAB_800e98e0
          sp54 = _800f0e34.get(i)._00.get();
          sp50 = _800f2248.offset(sp54 * 0x8L).getSigned();

          if((int)facing_800c67b4.get() <= 0 || (int)sp50 >= 0) {
            //LAB_800e995c
            if((int)facing_800c67b4.get() >= 0 || (int)sp50 <= 0) {
              //LAB_800e9988
              sp58 = Math.abs(sp50) - 0x1L;
              sp40 = pathPosPtrArr_800f591c.offset(sp58 * 0x4L).get();

              v1 = sp40 + (_800f5810.offset(sp58 * 0x4L).get() - 1) * 0x10L;
              sp0x10.set((VECTOR)MEMORY.ref(4, v1, VECTOR::new));

              v0 = sp40;
              sp0x20.set((VECTOR)MEMORY.ref(4, v0, VECTOR::new));

              if(sp0x30.getX() == sp0x10.getX() && sp0x30.getY() == sp0x10.getY() && sp0x30.getZ() == sp0x10.getZ()) {
                a0 = sp40 + _800f5810.offset(sp58 * 0x4L).get() * 0x10L - 0x20L;
                _800c67d8.get((int)sp4c).setX((int)MEMORY.ref(4, a0).offset(0x0L).get());

                a0 = sp40 + _800f5810.offset(sp58 * 0x4L).get() * 0x10L - 0x20L;
                _800c67d8.get((int)sp4c).setY((int)MEMORY.ref(4, a0).offset(0x4L).get());

                a0 = sp40 + _800f5810.offset(sp58 * 0x4L).get() * 0x10L - 0x20L;
                _800c67d8.get((int)sp4c).setZ((int)MEMORY.ref(4, a0).offset(0x8L).get());

                _800c6874.offset(sp4c * 0x4L).setu(sp54);

                sp4c++;
                //LAB_800e9bd8
              } else if(sp0x30.getX() == sp0x20.getX() && sp0x30.getY() == sp0x20.getY() && sp0x30.getZ() == sp0x20.getZ()) {
                _800c67d8.get((int)sp4c).setX((int)MEMORY.ref(4, sp40).offset(0x10L).get());
                _800c67d8.get((int)sp4c).setY((int)MEMORY.ref(4, sp40).offset(0x14L).get());
                _800c67d8.get((int)sp4c).setZ((int)MEMORY.ref(4, sp40).offset(0x18L).get());

                _800c6874.offset(sp4c * 0x4L).setu(sp54);

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

    if(sp4c == 0x1L) {
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
    long v1;
    long sp14;
    long sp20;
    final VECTOR sp0xb0 = new VECTOR();
    final short[] sp0xc8 = new short[7];
    long spd8;

    long sp18 = 0;
    long sp28 = 0;
    long spda = 0x1000L;

    if(_800c6894.get() != 0x2L) {
      return;
    }

    //LAB_800e9da0
    if(_800c6870.get() != 0) {
      if((int)_800c6870.get() < 0x3L) {
        FUN_800e3fac(1);
        submapCut_80052c30.setu(0x11dL);
        _80052c34.setu(0x20L);
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
    final int spa0 = struct258_800c66a8.deref().vec_94.getX() >> 12;
    final int spa4 = struct258_800c66a8.deref().vec_94.getY() >> 12;
    final int spa8 = struct258_800c66a8.deref().vec_94.getZ() >> 12;
    final long spe0 = (_800bee90.get() & 0xffffL) >>> 12;

    //LAB_800e9e90
    for(int i = 0; i < 7; i++) {
      //LAB_800e9eac
      if((int)_800c6874.offset(i * 0x4L).get() < 0) {
        break;
      }

      //LAB_800e9edc
      sp0xb0.setX(spa0 - _800c67d8.get(i).getX());
      sp0xb0.setY(spa4 - _800c67d8.get(i).getY());
      sp0xb0.setZ(spa8 - _800c67d8.get(i).getZ());

      sp0xc8[i] = (short)((_800c66b0.deref().rotation_70.getY() - ratan2(sp0xb0.getX(), sp0xb0.getZ()) + 0x800) & 0xfff);
      v0 = (sp0xc8[i] + 0x100L) & 0xfffL;
      if((int)v0 < 0) {
        v0 = v0 + 0x1ffL;
      }

      //LAB_800ea00c
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

    FUN_800ea4dc(_800c6874.offset(sp18 * 0x4L).get());

    if(_800f2248.offset(_800c67aa.get() * 0x8L).getSigned() < 0) {
      sp20 = -0x1L;
    } else {
      //LAB_800ea1d8
      sp20 = 0x1L;
    }

    //LAB_800ea1dc
    final long spc0 = pathPosPtrArr_800f591c.offset(pathIndex_800c67ac.get() * 0x4L).get();

    if((int)sp20 > 0) {
      sp0xb0.set((VECTOR)MEMORY.ref(4, spc0, VECTOR::new));
    } else {
      //LAB_800ea248
      v1 = spc0 + (_800f5810.offset(pathIndex_800c67ac.get() * 0x4L).get() - 1) * 0x10L;
      sp0xb0.set((VECTOR)MEMORY.ref(4, v1, VECTOR::new));
    }

    //LAB_800ea2a8
    if(_800c6848.getX() != sp0xb0.getX() || _800c6848.getY() != sp0xb0.getY() || _800c6848.getZ() != sp0xb0.getZ()) {
      //LAB_800ea2f8
      if((int)sp20 > 0) {
        dotIndex_800c67ae.setu(_800f5810.offset((Math.abs(_800f2248.offset(_800c67aa.get() * 0x8L).getSigned()) - 0x1L) * 0x4L).get() - 0x2L);
        dotOffset_800c67b0.setu(0x2L);
        facing_800c67b4.setu(-0x1L);
        _800c6858.setu(0x800L);
      } else {
        //LAB_800ea39c
        dotIndex_800c67ae.setu(0);
        dotOffset_800c67b0.setu(0x1L);
        facing_800c67b4.setu(0x1L);
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
    System.out.println(dotOffset_800c67b0 + ", " + playerPos + ", " + nextDotPos);
    weightedAvg(0x4L - dotOffset_800c67b0.getSigned(), dotOffset_800c67b0.getSigned(), struct258_800c66a8.deref().vec_94, playerPos, nextDotPos);
    struct258_800c66a8.deref().vec_94.y.sub(0x2000);
    playerPos_800c67b8.set(playerPos);
    nextDotPos_800c67c8.set(nextDotPos);

    updatePlayerRotation();
  }

  @Method(0x800ea4dcL)
  public static void FUN_800ea4dc(final long a0) {
    _800c67aa.setu(a0);

    //LAB_800ea4fc
    int i;
    for(i = 0; i < _800c67a0.get(); i++) {
      //LAB_800ea520
      if(_800f0e34.get(i)._00.get() != -1) {
        //LAB_800ea558
        if(FUN_800eb09c(i, 0, null) == 0) {
          //LAB_800ea578
          if(_800f0e34.get(i)._0e.get() == _800c6798.get() + 0x1L) {
            //LAB_800ea5bc
            if(_800f0e34.get(i)._00.get() == a0) {
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
    if(_800f0e34.get(a0)._00.get() == -1) {
      return;
    }

    //LAB_800ea678
    if(_800f0e34.get(a0)._0e.get() != _800c6798.get() + 0x1L) {
      return;
    }

    //LAB_800ea6bc
    if(FUN_800eb09c(a0, 0, null) != 0) {
      return;
    }

    //LAB_800ea6dc
    _800c67a8.setu(a0);
    _800c67aa.setu(_800f0e34.get(a0)._00.get());
    pathIndex_800c67ac.setu(Math.abs(_800f2248.offset(_800c67aa.get() * 0x8L).getSigned()) - 0x1L);

    final int sign;
    if(_800f2248.offset(_800c67aa.get() * 0x8L).getSigned() < 0) {
      sign = -1;
    } else {
      //LAB_800ea78c
      sign = 1;
    }

    //LAB_800ea790
    final long sp18 = pathPosPtrArr_800f591c.offset(pathIndex_800c67ac.get() * 0x4L).get();

    final long sp20;
    final long sp28;
    if(sign > 0) {
      struct258_800c66a8.deref().coord2_34.coord.transfer.setX((int)MEMORY.ref(4, sp18).offset(0x0L).get());
      struct258_800c66a8.deref().coord2_34.coord.transfer.setY((int)MEMORY.ref(4, sp18).offset(0x4L).get() - 2);
      struct258_800c66a8.deref().coord2_34.coord.transfer.setZ((int)MEMORY.ref(4, sp18).offset(0x8L).get());

      sp20 = MEMORY.ref(4, sp18).offset(0x0L).get() - MEMORY.ref(4, sp18).offset(0x10L).get();
      sp28 = MEMORY.ref(4, sp18).offset(0x8L).get() - MEMORY.ref(4, sp18).offset(0x18L).get();

      dotIndex_800c67ae.setu(0);
      dotOffset_800c67b0.setu(0);
      _800c6858.setu(0);
      facing_800c67b4.setu(0x1L);
    } else {
      //LAB_800ea8d4
      final long v1 = _800f2248.offset(_800c67aa.get() * 0x8L).getSigned();

      final long sp10 = _800f5810.offset((Math.abs(v1) - 0x1L) * 0x4L).get() - 0x1L;
      sp20 = MEMORY.ref(4, sp18).offset(sp10 * 0x10L).offset(0x0L).get() - MEMORY.ref(4, sp18).offset((sp10 - 1) * 0x10L).offset(0x0L).get();
      sp28 = MEMORY.ref(4, sp18).offset(sp10 * 0x10L).offset(0x8L).get() - MEMORY.ref(4, sp18).offset((sp10 - 1) * 0x10L).offset(0x8L).get();

      struct258_800c66a8.deref().coord2_34.coord.transfer.setX((int)MEMORY.ref(4, sp18).offset(sp10 * 0x10L).offset(0x0L).get());
      struct258_800c66a8.deref().coord2_34.coord.transfer.setY((int)MEMORY.ref(4, sp18).offset(sp10 * 0x10L).offset(0x4L).get() - 2);
      struct258_800c66a8.deref().coord2_34.coord.transfer.setZ((int)MEMORY.ref(4, sp18).offset(sp10 * 0x10L).offset(0x8L).get());

      dotIndex_800c67ae.setu(sp10 - 0x1L);
      dotOffset_800c67b0.setu(0x3L);
      _800c6858.setu(0x800L);
      facing_800c67b4.setu(-0x1L);
    }

    //LAB_800eaafc
    struct258_800c66a8.deref().rotation_a4.setX((short)0);
    struct258_800c66a8.deref().rotation_a4.setY((short)0);
    struct258_800c66a8.deref().rotation_a4.setZ((short)0);

    struct258_800c66a8.deref().rotation_a4.setY((short)ratan2(sp20, sp28));

    previousPlayerRotation_800c685a.setu(struct258_800c66a8.deref().rotation_a4.getY());
    _800c6890.setu(0);
    _800c6894.setu(0);

    //LAB_800eab80
  }

  @Method(0x800eab94L)
  public static void FUN_800eab94(long a0) {
    long at;
    long v0;
    long v1;
    long a1;
    long sp18;
    long sp14;
    long sp10;
    long sp1c;
    long sp2c;
    long sp20;
    long sp30;

    sp30 = a0;
    v0 = sp30;
    a0 = v0;
    v1 = a0 << 2;
    v1 = v1 + v0;
    v0 = v1 << 2;
    at = 0x800f_0000L;
    at = at + v0;
    v1 = MEMORY.ref(2, at).offset(0xe34L).getSigned();
    v0 = -0x1L;
    if(v1 == v0) {
      return;
    }

    //LAB_800eabdc
    v0 = sp30;

    a0 = v0;
    v1 = a0 << 2;
    v1 = v1 + v0;
    a0 = v1 << 2;
    at = 0x800f_0000L;
    at = at + a0;
    v0 = MEMORY.ref(1, at).offset(0xe42L).get();
    a0 = 0x800c_0000L;
    a0 = MEMORY.ref(4, a0).offset(0x6798L).get();

    v1 = a0 + 0x1L;
    if(v0 != v1) {
      return;
    }

    //LAB_800eac20
    a0 = sp30;
    a1 = 0;

    v0 = FUN_800eb09c((int)a0, a1, null);
    if(v0 != 0) {
      return;
    }

    //LAB_800eac40
    v0 = sp30;
    at = 0x800c_0000L;
    MEMORY.ref(2, at).offset(0x67a8L).setu(v0);
    v0 = sp30;

    a0 = v0;
    v1 = a0 << 2;
    v1 = v1 + v0;
    v0 = v1 << 2;
    at = 0x800f_0000L;
    at = at + v0;
    v1 = MEMORY.ref(2, at).offset(0xe34L).get();
    at = 0x800c_0000L;
    MEMORY.ref(2, at).offset(0x67aaL).setu(v1);
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(2, v0).offset(0x67aaL).get();

    v1 = v0;
    v0 = v1 << 3;
    at = 0x800f_0000L;
    at = at + v0;
    v1 = MEMORY.ref(2, at).offset(0x2248L).getSigned();

    a0 = v1;

    v0 = Math.abs(a0);
    v1 = v0;
    v0 = v1 + -0x1L;
    at = 0x800c_0000L;
    MEMORY.ref(2, at).offset(0x67acL).setu(v0);
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(2, v0).offset(0x67acL).get();

    v1 = v0;
    v0 = v1 << 2;
    at = 0x800f_0000L;
    at = at + v0;
    v1 = MEMORY.ref(4, at).offset(0x591cL).get();

    sp14 = v1;
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x67b4L).get();

    if((int)v0 > 0) {
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(2, v0).offset(0x67aaL).get();

      v1 = v0;
      v0 = v1 << 3;
      at = 0x800f_0000L;
      at = at + v0;
      v1 = MEMORY.ref(2, at).offset(0x2248L).getSigned();

      a0 = v1;

      v0 = Math.abs(a0);
      v1 = v0 + -0x1L;
      v0 = v1;
      v1 = v0 << 2;
      at = 0x800f_0000L;
      at = at + v1;
      v0 = MEMORY.ref(4, at).offset(0x5810L).get();

      v1 = v0 + -0x1L;
      sp10 = v1;
      v0 = sp10;

      v1 = v0;
      v0 = v1 << 4;
      v1 = sp14;

      v0 = v0 + v1;
      v1 = sp10;

      a0 = v1;
      v1 = a0 << 4;
      a0 = sp14;

      v1 = v1 + a0;
      a0 = v1 + -0x10L;
      v0 = MEMORY.ref(4, v0).offset(0x0L).get();
      v1 = MEMORY.ref(4, a0).offset(0x0L).get();

      v0 = v0 - v1;
      sp18 = v0;
      v0 = sp10;

      v1 = v0;
      v0 = v1 << 4;
      v1 = sp14;

      v0 = v0 + v1;
      v1 = sp10;

      a0 = v1;
      v1 = a0 << 4;
      a0 = sp14;

      v1 = v1 + a0;
      a0 = v1 + -0x10L;
      v0 = MEMORY.ref(4, v0).offset(0x4L).get();
      v1 = MEMORY.ref(4, a0).offset(0x4L).get();

      v0 = v0 - v1;
      sp1c = v0;
      v0 = sp10;

      v1 = v0;
      v0 = v1 << 4;
      v1 = sp14;

      v0 = v0 + v1;
      v1 = sp10;

      a0 = v1;
      v1 = a0 << 4;
      a0 = sp14;

      v1 = v1 + a0;
      a0 = v1 + -0x10L;
      v0 = MEMORY.ref(4, v0).offset(0x8L).get();
      v1 = MEMORY.ref(4, a0).offset(0x8L).get();

      v0 = v0 - v1;
      sp20 = v0;
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

      v0 = v1 + 0x34L;
      v1 = sp10;

      a0 = v1;
      v1 = a0 << 4;
      a0 = sp14;

      v1 = v1 + a0;
      a0 = MEMORY.ref(4, v1).offset(0x0L).get();

      MEMORY.ref(4, v0).offset(0x18L).setu(a0);
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

      v0 = v1 + 0x34L;
      v1 = sp10;

      a0 = v1;
      v1 = a0 << 4;
      a0 = sp14;

      v1 = v1 + a0;
      a0 = MEMORY.ref(4, v1).offset(0x4L).get();

      v1 = a0 + -0x2L;
      MEMORY.ref(4, v0).offset(0x1cL).setu(v1);
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

      v0 = v1 + 0x34L;
      v1 = sp10;

      a0 = v1;
      v1 = a0 << 4;
      a0 = sp14;

      v1 = v1 + a0;
      a0 = MEMORY.ref(4, v1).offset(0x8L).get();

      MEMORY.ref(4, v0).offset(0x20L).setu(a0);
      v0 = sp10;

      v1 = v0 + -0x1L;
      at = 0x800c_0000L;
      MEMORY.ref(2, at).offset(0x67aeL).setu(v1);
      v0 = 0x3L;
      at = 0x800c_0000L;
      MEMORY.ref(2, at).offset(0x67b0L).setu(v0);
      at = 0x800c_0000L;
      MEMORY.ref(2, at).offset(0x6858L).setu(0);
    } else {
      //LAB_800eaf14
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

      v0 = v1 + 0x34L;
      v1 = sp14;

      a0 = MEMORY.ref(4, v1).offset(0x0L).get();

      MEMORY.ref(4, v0).offset(0x18L).setu(a0);
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

      v0 = v1 + 0x34L;
      v1 = sp14;

      a0 = MEMORY.ref(4, v1).offset(0x4L).get();

      v1 = a0 + -0x2L;
      MEMORY.ref(4, v0).offset(0x1cL).setu(v1);
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

      v0 = v1 + 0x34L;
      v1 = sp14;

      a0 = MEMORY.ref(4, v1).offset(0x8L).get();

      MEMORY.ref(4, v0).offset(0x20L).setu(a0);
      v0 = sp14;
      a0 = sp14;

      v1 = a0 + 0x10L;
      v0 = MEMORY.ref(4, v0).offset(0x0L).get();
      v1 = MEMORY.ref(4, v1).offset(0x0L).get();

      v0 = v0 - v1;
      sp18 = v0;
      v0 = sp14;
      a0 = sp14;

      v1 = a0 + 0x10L;
      v0 = MEMORY.ref(4, v0).offset(0x4L).get();
      v1 = MEMORY.ref(4, v1).offset(0x4L).get();

      v0 = v0 - v1;
      sp1c = v0;
      v0 = sp14;
      a0 = sp14;

      v1 = a0 + 0x10L;
      v0 = MEMORY.ref(4, v0).offset(0x8L).get();
      v1 = MEMORY.ref(4, v1).offset(0x8L).get();

      v0 = v0 - v1;
      sp20 = v0;
      at = 0x800c_0000L;
      MEMORY.ref(2, at).offset(0x67aeL).setu(0);
      at = 0x800c_0000L;
      MEMORY.ref(2, at).offset(0x67b0L).setu(0);
      v0 = 0x800L;
      at = 0x800c_0000L;
      MEMORY.ref(2, at).offset(0x6858L).setu(v0);
    }

    //LAB_800eb00c
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

    v0 = v1 + 0xa4L;
    MEMORY.ref(2, v0).offset(0x0L).setu(0);
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

    v0 = v1 + 0xa4L;
    MEMORY.ref(2, v0).offset(0x2L).setu(0);
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

    v0 = v1 + 0xa4L;
    MEMORY.ref(2, v0).offset(0x4L).setu(0);
    a0 = sp18;
    a1 = sp20;

    v0 = ratan2(a0, a1);
    v1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v1).offset(0x66a8L).get();

    MEMORY.ref(2, v1).offset(0xa6L).setu(v0);
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

    v1 = MEMORY.ref(2, v0).offset(0xa6L).get();
    at = 0x800c_0000L;
    MEMORY.ref(2, at).offset(0x685aL).setu(v1);
    at = 0x800c_0000L;
    MEMORY.ref(4, at).offset(0x6894L).setu(0);

    //LAB_800eb088
  }

  /**
   * a1 used to be either 0, -1, or a VECTOR. If passing a VECTOR, pass it as vec and set a1 to 1
   */
  @Method(0x800eb09cL)
  public static long FUN_800eb09c(final int a0, final long a1, @Nullable final VECTOR vec) {
    if(_800f0e34.get(a0)._00.get() == -1) {
      return -0x1L;
    }

    //LAB_800eb0ec
    if(a1 != -0x1L) {
      if(_800f0e34.get(a0)._0e.get() != _800c6798.get() + 0x1L) {
        return -0x2L;
      }
    }

    //LAB_800eb144
    if((_800bad24.offset((a0 >>> 5) * 0x4L).get() & (0x1L << (a0 & 0x1fL))) <= 0) {
      return 0x1L;
    }

    //LAB_800eb1d0
    if(a1 == 0 || a1 == -0x1L) {
      //LAB_800eb1f8
      return 0;
    }

    //LAB_800eb204
    final long sp14 = _800f2248.offset(_800f0e34.get(a0)._00.get() * 0x8L).getSigned();

    if(sp14 == 0) {
      return -0x3L;
    }

    //LAB_800eb264
    final long sp18 = Math.abs(sp14) - 0x1L;
    final long v1 = pathPosPtrArr_800f591c.offset(sp18 * 0x4L).get();

    if((int)sp14 > 0) {
      vec.setX((int)MEMORY.ref(4, v1).offset(0x0L).get());
      vec.setY((int)MEMORY.ref(4, v1).offset(0x4L).get());
      vec.setZ((int)MEMORY.ref(4, v1).offset(0x8L).get());
    } else {
      //LAB_800eb2fc
      final long v0 = _800f5810.offset(sp18 * 0x4L).get() - 0x1L;
      vec.setX((int)MEMORY.ref(4, v1).offset(v0 * 0x10L).offset(0x0L).get());
      vec.setY((int)MEMORY.ref(4, v1).offset(v0 * 0x10L).offset(0x4L).get());
      vec.setZ((int)MEMORY.ref(4, v1).offset(v0 * 0x10L).offset(0x8L).get());
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
    for(int i = 0; i < 0x101L; i++) {
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
          final long sp28 = _800f0e34.get(i)._02.get();
          int sp20 = 0;

          //LAB_800eb4c8
          for(int sp1c = i; sp1c < _800c67a0.get(); sp1c++) {
            //LAB_800eb4ec
            if(FUN_800eb09c(sp1c, 0, null) == 0) {
              //LAB_800eb50c
              if(sp0xd0[sp1c] == 0) {
                //LAB_800eb530
                final long sp2c = _800f0e34.get(sp1c)._02.get();

                if(!_800f0234.get((int)sp28).text_00.isNull() || !_800f0234.get((int)sp2c).text_00.isNull()) {
                  // Added this check since these pointers can be null
                  if(!_800f0234.get((int)sp28).text_00.isNull() && !_800f0234.get((int)sp2c).text_00.isNull()) {
                    //LAB_800eb5d8
                    if(strcmp(MEMORY.ref(1, _800f0234.get((int)sp28).text_00.getPointer()).getString(), MEMORY.ref(1, _800f0234.get((int)sp2c).text_00.getPointer()).getString()) == 0) {
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
              weightedAvg(0x1L, 0x1L, sp0x50, sp0x30, sp0x40);
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

    _800c86f8.setPointer(addToLinkedListTail(0x1200L));
    _800c86fc.setu(0);

    //LAB_800eb9b8
    for(int i = 0; i < 0x30; i++) {
      final Coord2AndThenSomeStruct_60 struct = _800c86f8.deref().get(i);

      //LAB_800eb9d4
      GsInitCoordinate2(null, struct.coord2_00);

      //LAB_800eba0c
      //LAB_800ebaa0
      struct.svec_54.setX((short)(rand() % 8 - 4));
      struct.svec_54.setY((short)(-rand() % 3 - 2));
      struct.svec_54.setZ((short)(rand() % 8 - 4));

      //LAB_800ebadc
      struct._50.set(rand() % 0x80);
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

  @Method(0x800ed95cL)
  public static void FUN_800ed95c() {
    if(_800c66b0.deref()._c5.get() == 0x2L) {
      return;
    }

    //LAB_800ed98c
    switch((int)_800c66a4.get()) {
      case 0:
        break;

      case 2:
        if((_800c66b8.get() & 0x1_0000L) != 0 && (_800c66b8.get() & 0x1000L) != 0) {
          _800c66a4.setu(0x3L);
        }

        //LAB_800eda18
        break;

      case 3:
        _800f65a4.get((int)_800f6598.get()).deref().run();
        _800c66a4.setu(0x4L);
        break;

      case 4:
        if(_800c6698.get() >= 0x3L || _800c669c.get() >= 0x3L) {
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
    FUN_800edbc0();

    //LAB_800edbac
  }

  @Method(0x800edbc0L)
  public static void FUN_800edbc0() {
    long v1;
    long a0;
    long sp1c;
    long sp84;

    final SVECTOR vert0 = new SVECTOR();
    final SVECTOR vert1 = new SVECTOR();
    final SVECTOR vert2 = new SVECTOR();
    final SVECTOR vert3 = new SVECTOR();
    final SVECTOR rotation = new SVECTOR(); // Just (0, 0, 0)
    final MATRIX ls = new MATRIX();

    if((_800c66b8.get() & 0x1000L) == 0) {
      return;
    }

    //LAB_800edc04
    if(_800c6690.get() != 0) {
      return;
    }

    //LAB_800edc20
    if(struct258_800c66a8.deref()._1f8.get() == 0x4L) {
      return;
    }

    //LAB_800edc44
    if((int)_800c6698.get() < 0x4L) {
      return;
    }

    //LAB_800edc64
    if((int)_800c669c.get() < 0x4L) {
      return;
    }

    //LAB_800edc84
    long packetAddr = linkedListAddress_1f8003d8.get();

    //LAB_800edca8
    for(int i = 0; i < _800c86cc.get(); i++) {
      final Coord2AndThenSomeStruct_60 struct = _800c86f8.deref().get(i);

      //LAB_800edccc
      if(!_800f0234.get(_800f0e34.get((int)_800c84c8.offset(i * 0x2L).getSigned())._02.get()).text_00.isNull()) {
        //LAB_800edd3c
        final long sp18 = _800f0e34.get((int)_800c84c8.offset(i * 0x2L).getSigned())._12.get() & 0xcL;

        if(sp18 != 0) {
          //LAB_800edda0
          if(_800f0e34.get((int)_800c84c8.offset(i * 0x2L).getSigned())._0e.get() == _800c6798.get() + 0x1L) {
            //LAB_800eddfc
            if(i >= 0x9L) {
              break;
            }

            //LAB_800ede18
            //LAB_800ede1c
            for(long sp14 = 0; sp14 < 6; sp14++) {
              //LAB_800ede38
              if(sp18 == 0x8L) {
                sp1c = struct._50.get() / 5;
              } else {
                //LAB_800ede88
                sp1c = struct._50.get() / 3;
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
              struct.coord2_00.coord.transfer.setX((int)(_800c74b8.offset(i * 0x10L).get() + struct.svec_54.getX() * struct._50.get() / 0x10));

              //LAB_800edff0
              struct.coord2_00.coord.transfer.setY((int)(_800c74bc.offset(i * 0x10L).get() + struct.svec_54.getY() * struct._50.get() / 0x4L));

              //LAB_800ee058
              struct.coord2_00.coord.transfer.setZ((int)(_800c74c0.offset(i * 0x10L).get() + struct.svec_54.getZ() * struct._50.get() / 0x10L));

              if(_800c6798.get() == 0) {
                if(sp18 == 0x4L) {
                  //LAB_800ee0e4
                  struct.coord2_00.coord.transfer.setX((int)(_800c74b8.offset(i * 0x10L).get() + struct.svec_54.getX() * struct._50.get() / 0x10L));

                  //LAB_800ee14c
                  struct.coord2_00.coord.transfer.setY((int)(_800c74bc.offset(i * 0x10L).get() + struct.svec_54.getY() * struct._50.get() / 0x4L));

                  //LAB_800ee1b4
                  struct.coord2_00.coord.transfer.setZ((int)(_800c74c0.offset(i * 0x10L).get() + struct.svec_54.getZ() * struct._50.get() / 0x10L + 0x50L));
                  //LAB_800ee1dc
                } else if(sp18 == 0x8L) {
                  //LAB_800ee238
                  struct.coord2_00.coord.transfer.setX((int)(_800c74b8.offset(i * 0x10L).get() + struct.svec_54.getX() * struct._50.get() / 0x10L + 0x30L));

                  //LAB_800ee2a4
                  struct.coord2_00.coord.transfer.setY((int)(_800c74bc.offset(i * 0x10L).get() + struct.svec_54.getY() * struct._50.get() / 0x4L));

                  //LAB_800ee30c
                  struct.coord2_00.coord.transfer.setZ((int)(_800c74c0.offset(i * 0x10L).get() + struct.svec_54.getZ() * struct._50.get() / 0x10L + 0x30L));
                }

                //LAB_800ee32c
                //LAB_800ee334
              } else if(_800c6798.get() == 0x1L) {
                if(sp18 == 0x4L) {
                  //LAB_800ee3a4
                  struct.coord2_00.coord.transfer.setX((int)(_800c74b8.offset(i * 0x10L).get() + struct.svec_54.getX() * struct._50.get() / 0x10L));

                  //LAB_800ee40c
                  struct.coord2_00.coord.transfer.setY((int)(_800c74bc.offset(i * 0x10L).get() + struct.svec_54.getY() * struct._50.get() / 0x4L + 0x30L));

                  //LAB_800ee478
                  struct.coord2_00.coord.transfer.setZ((int)(_800c74c0.offset(i * 0x10L).get() + struct.svec_54.getZ() * struct._50.get() / 0x10L - 0x64L));
                  //LAB_800ee4a0
                } else if(sp18 == 0x8L) {
                  //LAB_800ee4fc
                  a0 = struct.svec_54.getX() * struct._50.get() / 0x10L;
                  struct.coord2_00.coord.transfer.setX((int)(_800c74b8.offset(i * 0x10L).get() + a0 - 0x30L));

                  //LAB_800ee568
                  a0 = struct.svec_54.getY() * struct._50.get() / 0x4L;
                  struct.coord2_00.coord.transfer.setY((int)(_800c74bc.offset(i * 0x10L).get() + a0));

                  //LAB_800ee5d0
                  a0 = struct.svec_54.getZ() * struct._50.get() / 0x10L;
                  struct.coord2_00.coord.transfer.setZ((int)(_800c74c0.offset(i * 0x10L).get() + a0 + 0x20L));
                }
              }

              //LAB_800ee5f0
              rotateCoord2(rotation, struct.coord2_00);
              GsGetLs(struct.coord2_00, ls);
              clearLinearTransforms(ls);
              setRotTransMatrix(ls);
              setGpuPacketType(0xcL, packetAddr, true, false);

              CPU.MTC2(vert0.getXY(), 0);
              CPU.MTC2(vert0.getZ(), 1);
              CPU.COP2(0x18_0001L); // Perspective transform single
              MEMORY.ref(4, packetAddr).offset(0x8L).setu(CPU.MFC2(14));
              sp84 = (int)CPU.MFC2(19) >> 2;

              //LAB_800ee6cc
              if((int)sp84 >= 0x5L || (int)sp84 < (int)(_1f8003c8.get() - 0x3L)) {
                //LAB_800ee6d4
                CPU.MTC2(vert1.getXY(), 0);
                CPU.MTC2(vert1.getZ(), 1);
                CPU.COP2(0x18_0001L); // Perspective transform single
                MEMORY.ref(4, packetAddr).offset(0x10L).setu(CPU.MFC2(14));
                sp84 = (int)CPU.MFC2(19) >> 2;

                //LAB_800ee750
                if((int)sp84 >= 0x5L || (int)sp84 < (int)(_1f8003c8.get() - 0x3L)) {
                  //LAB_800ee758
                  if(MEMORY.ref(2, packetAddr).offset(0x10L).getSigned() - MEMORY.ref(2, packetAddr).offset(0x8L).getSigned() < 0x401L) {
                    //LAB_800ee78c
                    CPU.MTC2(vert2.getXY(), 0);
                    CPU.MTC2(vert2.getZ(), 1);
                    CPU.COP2(0x18_0001L); // Perspective transform single
                    MEMORY.ref(4, packetAddr).offset(0x18L).setu(CPU.MFC2(14));
                    sp84 = (int)CPU.MFC2(19) >> 2;

                    //LAB_800ee808
                    if((int)sp84 >= 0x5L && sp84 < (int)(_1f8003c8.get() - 0x3L)) {
                      //LAB_800ee810
                      if(MEMORY.ref(2, packetAddr).offset(0x1aL).getSigned() - MEMORY.ref(2, packetAddr).offset(0xaL).getSigned() < 0x201L) {
                        //LAB_800ee844
                        CPU.MTC2(vert3.getXY(), 0);
                        CPU.MTC2(vert3.getZ(), 1);
                        CPU.COP2(0x18_0001L); // Perspective transform single
                        MEMORY.ref(4, packetAddr).offset(0x20L).setu(CPU.MFC2(14));
                        sp84 = (int)CPU.MFC2(19) >> 2;

                        //LAB_800ee8c0
                        if((int)sp84 >= 0x5L && sp84 < (int)(_1f8003c8.get() - 0x3L)) {
                          //LAB_800ee8c8
                          if(MEMORY.ref(2, packetAddr).offset(0x20L).getSigned() - MEMORY.ref(2, packetAddr).offset(0x18L).getSigned() < 0x401L) {
                            //LAB_800ee8fc
                            if(MEMORY.ref(2, packetAddr).offset(0x22L).getSigned() - MEMORY.ref(2, packetAddr).offset(0x12L).getSigned() < 0x201L) {
                              //LAB_800ee930
                              if((int)sp84 >= 0x6L && (int)sp84 < (int)(_1f8003c8.get() - 0x1L)) {
                                if(sp18 == 0x8L) {
                                  MEMORY.ref(2, packetAddr).offset(0x16L).setu(_800bb11a.get() | 0xaL);
                                } else {
                                  //LAB_800ee998
                                  MEMORY.ref(2, packetAddr).offset(0x16L).setu(_800bb116.get() | 0xaL);
                                }

                                //LAB_800ee9b0
                                MEMORY.ref(2, packetAddr).offset(0xeL).setu(0x7e68L);
                                MEMORY.ref(1, packetAddr).offset(0x4L).setu(-0x80L - struct._50.get());
                                MEMORY.ref(1, packetAddr).offset(0x5L).setu(-0x80L - struct._50.get());
                                MEMORY.ref(1, packetAddr).offset(0x6L).setu(-0x80L - struct._50.get());

                                //LAB_800eea34
                                v1 = (int)struct._50.get() / 0x40L;
                                MEMORY.ref(1, packetAddr).offset(0xcL).setu(_800f65d4.offset(v1 * 0x2L).offset(0x0L).get());

                                //LAB_800eea78
                                v1 = (int)struct._50.get() / 0x40L;
                                MEMORY.ref(1, packetAddr).offset(0xdL).setu(_800f65d4.offset(v1 * 0x2L).offset(0x1L).get());

                                //LAB_800eeac0
                                v1 = (int)struct._50.get() / 0x40L;
                                MEMORY.ref(1, packetAddr).offset(0x14L).setu(_800f65d4.offset(v1 * 0x2L).offset(0x0L).get() + 0x1fL);

                                //LAB_800eeb08
                                v1 = (int)struct._50.get() / 0x40L;
                                MEMORY.ref(1, packetAddr).offset(0x15L).setu(_800f65d4.offset(v1 * 0x2L).offset(0x1L).get());

                                //LAB_800eeb50
                                v1 = (int)struct._50.get() / 0x40L;
                                MEMORY.ref(1, packetAddr).offset(0x1cL).setu(_800f65d4.offset(v1 * 0x2L).offset(0x0L).get());

                                //LAB_800eeb94
                                v1 = (int)struct._50.get() / 0x40L;
                                MEMORY.ref(1, packetAddr).offset(0x1dL).setu(_800f65d4.offset(v1 * 0x2L).offset(0x1L).get() + 0x1fL);

                                //LAB_800eebe0
                                v1 = (int)struct._50.get() / 0x40L;
                                MEMORY.ref(1, packetAddr).offset(0x24L).setu(_800f65d4.offset(v1 * 0x2L).offset(0x0L).get() + 0x1fL);

                                //LAB_800eec28
                                v1 = (int)struct._50.get() / 0x40L;
                                MEMORY.ref(1, packetAddr).offset(0x25L).setu(_800f65d4.offset(v1 * 0x2L).offset(0x1L).get() + 0x1fL);

                                insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x190L + sp84 * 0x4L, packetAddr);

                                struct._50.incr();

                                if((int)struct._50.get() >= 0x80L) {
                                  struct._50.set(0);
                                }

                                //LAB_800eeccc
                                packetAddr += 0x28L;
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }

              //LAB_800eecec
            }
          }
        }
      }

      //LAB_800eed04
    }

    //LAB_800eed1c
    linkedListAddress_1f8003d8.setu(packetAddr);

    //LAB_800eed28
  }

  @Method(0x800eede4L)
  public static void FUN_800eede4() {
    removeFromLinkedList(_800c86f8.getPointer());
    _800f65bc.get((int)_800f6598.get()).deref().run();
  }
}
