package legend.game;

import legend.core.Tuple;
import legend.core.gpu.RECT;
import legend.core.gpu.TimHeader;
import legend.core.gte.GsCOORD2PARAM;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.Tmd;
import legend.core.gte.TmdObjTable;
import legend.core.gte.VECTOR;
import legend.core.memory.Memory;
import legend.core.memory.Method;
import legend.core.memory.Ref;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.CString;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.TriConsumerRef;
import legend.core.memory.types.TriFunctionRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.game.types.ActiveStatsa0;
import legend.game.types.BattleRenderStruct;
import legend.game.types.BattleStruct;
import legend.game.types.BattleStruct1a8;
import legend.game.types.BattleStruct1a8_c;
import legend.game.types.BigStruct;
import legend.game.types.BtldScriptData27c;
import legend.game.types.BttlScriptData6c;
import legend.game.types.DR_MOVE;
import legend.game.types.ExtendedTmd;
import legend.game.types.GsF_LIGHT;
import legend.game.types.GsRVIEW2;
import legend.game.types.LodString;
import legend.game.types.McqHeader;
import legend.game.types.MrgFile;
import legend.game.types.RotateTranslateStruct;
import legend.game.types.RunningScript;
import legend.game.types.ScriptFile;
import legend.game.types.ScriptState;
import legend.game.types.TmdAnimationFile;

import javax.annotation.Nullable;

import static legend.core.Hardware.CPU;
import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.SItem.FUN_80110030;
import static legend.game.SItem.renderText;
import static legend.game.Scus94491BpeSegment.FUN_80012444;
import static legend.game.Scus94491BpeSegment.FUN_800127cc;
import static legend.game.Scus94491BpeSegment.FUN_800128a8;
import static legend.game.Scus94491BpeSegment.FUN_80012b1c;
import static legend.game.Scus94491BpeSegment.FUN_80012bb4;
import static legend.game.Scus94491BpeSegment.FUN_8001324c;
import static legend.game.Scus94491BpeSegment.FUN_800133ac;
import static legend.game.Scus94491BpeSegment.FUN_80013404;
import static legend.game.Scus94491BpeSegment.FUN_80015704;
import static legend.game.Scus94491BpeSegment.FUN_80015d38;
import static legend.game.Scus94491BpeSegment.FUN_8001814c;
import static legend.game.Scus94491BpeSegment.FUN_8001af00;
import static legend.game.Scus94491BpeSegment.FUN_8001ff74;
import static legend.game.Scus94491BpeSegment._1f8003e8;
import static legend.game.Scus94491BpeSegment._1f8003ec;
import static legend.game.Scus94491BpeSegment._1f8003ee;
import static legend.game.Scus94491BpeSegment._1f8003f4;
import static legend.game.Scus94491BpeSegment._1f8003f8;
import static legend.game.Scus94491BpeSegment.addToLinkedListTail;
import static legend.game.Scus94491BpeSegment.allocateScriptState;
import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment.decompress;
import static legend.game.Scus94491BpeSegment.insertElementIntoLinkedList;
import static legend.game.Scus94491BpeSegment.linkedListAddress_1f8003d8;
import static legend.game.Scus94491BpeSegment.loadDrgnBinFile;
import static legend.game.Scus94491BpeSegment.loadMcq;
import static legend.game.Scus94491BpeSegment.loadMusicPackage;
import static legend.game.Scus94491BpeSegment.loadScriptFile;
import static legend.game.Scus94491BpeSegment.memcpy;
import static legend.game.Scus94491BpeSegment.playSound;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.removeFromLinkedList;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment.setCallback04;
import static legend.game.Scus94491BpeSegment.setCallback08;
import static legend.game.Scus94491BpeSegment.setCallback0c;
import static legend.game.Scus94491BpeSegment.setCallback10;
import static legend.game.Scus94491BpeSegment.setWidthAndFlags;
import static legend.game.Scus94491BpeSegment.tags_1f8003d0;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020308;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020a00;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020b98;
import static legend.game.Scus94491BpeSegment_8002.FUN_800211d8;
import static legend.game.Scus94491BpeSegment_8002.FUN_800214bc;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021520;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021584;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021868;
import static legend.game.Scus94491BpeSegment_8002.FUN_800218a4;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021b08;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021ca0;
import static legend.game.Scus94491BpeSegment_8002.FUN_80022928;
import static legend.game.Scus94491BpeSegment_8002.FUN_80023264;
import static legend.game.Scus94491BpeSegment_8002.FUN_800232dc;
import static legend.game.Scus94491BpeSegment_8002.FUN_80023a88;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a55c;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002a59c;
import static legend.game.Scus94491BpeSegment_8002.SetGeomOffset;
import static legend.game.Scus94491BpeSegment_8002.SetRotMatrix;
import static legend.game.Scus94491BpeSegment_8002.SetTransMatrix;
import static legend.game.Scus94491BpeSegment_8002.SquareRoot0;
import static legend.game.Scus94491BpeSegment_8002.strcpy;
import static legend.game.Scus94491BpeSegment_8003.DrawSync;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b590;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b5b0;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003b690;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003ef50;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003f900;
import static legend.game.Scus94491BpeSegment_8003.FUN_8003f990;
import static legend.game.Scus94491BpeSegment_8003.GetTPage;
import static legend.game.Scus94491BpeSegment_8003.GsGetLs;
import static legend.game.Scus94491BpeSegment_8003.GsGetLws;
import static legend.game.Scus94491BpeSegment_8003.GsInitCoordinate2;
import static legend.game.Scus94491BpeSegment_8003.GsSetAmbient;
import static legend.game.Scus94491BpeSegment_8003.GsSetFlatLight;
import static legend.game.Scus94491BpeSegment_8003.GsSetLightMatrix;
import static legend.game.Scus94491BpeSegment_8003.GsSetRefView2;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.MoveImage;
import static legend.game.Scus94491BpeSegment_8003.RotMatrix_8003faf0;
import static legend.game.Scus94491BpeSegment_8003.ScaleMatrixL;
import static legend.game.Scus94491BpeSegment_8003.SetDrawMove;
import static legend.game.Scus94491BpeSegment_8003.TransMatrix;
import static legend.game.Scus94491BpeSegment_8003.adjustTmdPointers;
import static legend.game.Scus94491BpeSegment_8003.bzero;
import static legend.game.Scus94491BpeSegment_8003.getProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.gpuLinkedListSetCommandTransparency;
import static legend.game.Scus94491BpeSegment_8003.parseTimHeader;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.setRotTransMatrix;
import static legend.game.Scus94491BpeSegment_8003.updateTmdPacketIlen;
import static legend.game.Scus94491BpeSegment_8004.RotMatrix_80040010;
import static legend.game.Scus94491BpeSegment_8004.RotMatrix_80040780;
import static legend.game.Scus94491BpeSegment_8004.additionOffsets_8004f5ac;
import static legend.game.Scus94491BpeSegment_8004.fileCount_8004ddc8;
import static legend.game.Scus94491BpeSegment_8004.ratan2;
import static legend.game.Scus94491BpeSegment_8005._8005e398;
import static legend.game.Scus94491BpeSegment_8005._8005e398_SCRIPT_SIZES;
import static legend.game.Scus94491BpeSegment_8005._8005f428;
import static legend.game.Scus94491BpeSegment_8006._8006e398;
import static legend.game.Scus94491BpeSegment_8006._8006e918;
import static legend.game.Scus94491BpeSegment_8006._8006f28c;
import static legend.game.Scus94491BpeSegment_8007.joypadInput_8007a39c;
import static legend.game.Scus94491BpeSegment_8007.joypadPress_8007a398;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800babc0;
import static legend.game.Scus94491BpeSegment_800b._800bb0fc;
import static legend.game.Scus94491BpeSegment_800b._800bb110;
import static legend.game.Scus94491BpeSegment_800b._800bb112;
import static legend.game.Scus94491BpeSegment_800b._800bc910;
import static legend.game.Scus94491BpeSegment_800b._800bc914;
import static legend.game.Scus94491BpeSegment_800b._800bc918;
import static legend.game.Scus94491BpeSegment_800b._800bc920;
import static legend.game.Scus94491BpeSegment_800b._800bc94c;
import static legend.game.Scus94491BpeSegment_800b._800bc950;
import static legend.game.Scus94491BpeSegment_800b._800bc954;
import static legend.game.Scus94491BpeSegment_800b._800bc958;
import static legend.game.Scus94491BpeSegment_800b._800bc95c;
import static legend.game.Scus94491BpeSegment_800b._800bc960;
import static legend.game.Scus94491BpeSegment_800b._800bc974;
import static legend.game.Scus94491BpeSegment_800b._800bc978;
import static legend.game.Scus94491BpeSegment_800b._800bda0c;
import static legend.game.Scus94491BpeSegment_800b._800be5d0;
import static legend.game.Scus94491BpeSegment_800b.bigStruct_800bda10;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.stats_800be5f8;
import static legend.game.Scus94491BpeSegment_800b.submapStage_800bb0f4;

public final class Bttl {
  private Bttl() { }

  public static final Value _800c6698 = MEMORY.ref(4, 0x800c6698L);
  public static final Value _800c669c = MEMORY.ref(4, 0x800c669cL);
  /** The number of {@link Scus94491BpeSegment_8005#_8005e398}s */
  public static final Value battleStruct1a8Count_800c66a0 = MEMORY.ref(4, 0x800c66a0L);
  public static final Value currentStage_800c66a4 = MEMORY.ref(4, 0x800c66a4L);

  public static final Value _800c66b0 = MEMORY.ref(4, 0x800c66b0L);

  public static final Value _800c66b8 = MEMORY.ref(1, 0x800c66b8L);
  public static final Value _800c66b9 = MEMORY.ref(1, 0x800c66b9L);

  public static final Value _800c66bc = MEMORY.ref(4, 0x800c66bcL);
  public static final Value _800c66c0 = MEMORY.ref(1, 0x800c66c0L);
  public static final Value _800c66c1 = MEMORY.ref(1, 0x800c66c1L);

  public static final Value _800c66c4 = MEMORY.ref(4, 0x800c66c4L);
  public static final Value _800c66c8 = MEMORY.ref(4, 0x800c66c8L);
  public static final Value _800c66cc = MEMORY.ref(4, 0x800c66ccL);
  public static final Value _800c66d0 = MEMORY.ref(4, 0x800c66d0L);
  public static final Value _800c66d4 = MEMORY.ref(1, 0x800c66d4L);

  public static final Value _800c66d8 = MEMORY.ref(4, 0x800c66d8L);

  public static final Pointer<ScriptFile> script_800c66fc = MEMORY.ref(4, 0x800c66fcL, Pointer.deferred(4, ScriptFile::new));
  public static int script_800c66fc_length;

  public static final Pointer<ScriptFile> script_800c670c = MEMORY.ref(4, 0x800c670cL, Pointer.deferred(4, ScriptFile::new));

  public static final Value _800c6718 = MEMORY.ref(4, 0x800c6718L);

  public static final Value _800c6748 = MEMORY.ref(4, 0x800c6748L);
  public static final Value scriptIndex_800c674c = MEMORY.ref(4, 0x800c674cL);

  public static final Value _800c6754 = MEMORY.ref(4, 0x800c6754L);
  public static final Value _800c6758 = MEMORY.ref(4, 0x800c6758L);
  public static final Value _800c675c = MEMORY.ref(4, 0x800c675cL);
  public static final Value _800c6760 = MEMORY.ref(4, 0x800c6760L);
  public static final Value _800c6764 = MEMORY.ref(4, 0x800c6764L);
  public static final Value _800c6768 = MEMORY.ref(4, 0x800c6768L);

  public static final Value _800c677c = MEMORY.ref(4, 0x800c677cL);
  public static final Value _800c6780 = MEMORY.ref(4, 0x800c6780L);

  public static final MATRIX _800c6798 = MEMORY.ref(4, 0x800c6798L, MATRIX::new);
  public static final UnsignedIntRef _800c67b8 = MEMORY.ref(4, 0x800c67b8L, UnsignedIntRef::new);
  public static final Value x_800c67bc = MEMORY.ref(4, 0x800c67bcL);
  public static final Value y_800c67c0 = MEMORY.ref(4, 0x800c67c0L);
  public static final Value _800c67c4 = MEMORY.ref(4, 0x800c67c4L);

  public static final Value _800c67d4 = MEMORY.ref(4, 0x800c67d4L);

  public static final Value _800c67e4 = MEMORY.ref(4, 0x800c67e4L);
  public static final Value _800c67e8 = MEMORY.ref(4, 0x800c67e8L);

  public static final GsRVIEW2 rview2_800c67f0 = MEMORY.ref(4, 0x800c67f0L, GsRVIEW2::new);

  public static final Value _800c6878 = MEMORY.ref(4, 0x800c6878L);

  public static final Value _800c68ec = MEMORY.ref(4, 0x800c68ecL);

  public static final Value _800c6912 = MEMORY.ref(1, 0x800c6912L);
  public static final Value _800c6913 = MEMORY.ref(1, 0x800c6913L);

  public static final Value _800c6928 = MEMORY.ref(4, 0x800c6928L);
  public static final Value _800c692c = MEMORY.ref(4, 0x800c692cL);
  public static final Value _800c6930 = MEMORY.ref(4, 0x800c6930L);

  public static final Value _800c6938 = MEMORY.ref(4, 0x800c6938L);
  public static final Value _800c693c = MEMORY.ref(4, 0x800c693cL);
  public static final Value _800c6940 = MEMORY.ref(4, 0x800c6940L);
  public static final Value _800c6944 = MEMORY.ref(4, 0x800c6944L);
  public static final Value _800c6948 = MEMORY.ref(4, 0x800c6948L);

  public static final Value _800c6950 = MEMORY.ref(4, 0x800c6950L);

  public static final Value _800c6958 = MEMORY.ref(4, 0x800c6958L);
  public static final Value _800c695c = MEMORY.ref(2, 0x800c695cL);

  public static final Value _800c6960 = MEMORY.ref(1, 0x800c6960L);

  public static final Value _800c697c = MEMORY.ref(2, 0x800c697cL);
  public static final Value _800c697e = MEMORY.ref(2, 0x800c697eL);
  public static final Value _800c6980 = MEMORY.ref(2, 0x800c6980L);

  public static final Value _800c6988 = MEMORY.ref(1, 0x800c6988L);

  public static final Value _800c69c8 = MEMORY.ref(4, 0x800c69c8L);

  /** TODO array of 0x2c-byte structs */
  public static final Value _800c69d0 = MEMORY.ref(2, 0x800c69d0L);

  public static final Value _800c6b5c = MEMORY.ref(4, 0x800c6b5cL);
  public static final Value _800c6b60 = MEMORY.ref(4, 0x800c6b60L);
  public static final Value _800c6b64 = MEMORY.ref(4, 0x800c6b64L);
  public static final Value _800c6b68 = MEMORY.ref(4, 0x800c6b68L);
  public static final Value _800c6b6c = MEMORY.ref(4, 0x800c6b6cL);
  public static final Value _800c6b70 = MEMORY.ref(2, 0x800c6b70L);

  public static final Value _800c6b78 = MEMORY.ref(4, 0x800c6b78L);

  public static final Value _800c6b9c = MEMORY.ref(4, 0x800c6b9cL);

  /** TODO array of 0x2c-byte structs */
  public static final Value _800c6ba8 = MEMORY.ref(2, 0x800c6ba8L);

  public static final Value _800c6c2c = MEMORY.ref(4, 0x800c6c2cL);

  public static final Value _800c6c34 = MEMORY.ref(4, 0x800c6c34L);
  public static final Value _800c6c38 = MEMORY.ref(4, 0x800c6c38L);
  public static final Value _800c6c3c = MEMORY.ref(2, 0x800c6c3cL);

  /** TODO array of 0x3c-byte structs */
  public static final Value _800c6c40 = MEMORY.ref(2, 0x800c6c40L);

  public static final Value _800c6cf4 = MEMORY.ref(4, 0x800c6cf4L);

  public static final CString _800c6e18 = MEMORY.ref(7, 0x800c6e18L, CString::new);

  public static final Value _800c6e34 = MEMORY.ref(2, 0x800c6e34L);

  public static final Value _800c6e48 = MEMORY.ref(2, 0x800c6e48L);
  public static final Value _800c6e60 = MEMORY.ref(2, 0x800c6e60L);

  /** TODO unknown size, maybe struct or array */
  public static final Value _800c6e9c = MEMORY.ref(2, 0x800c6e9cL);

  /** TODO unknown size, maybe struct or array */
  public static final Value _800c6ecc = MEMORY.ref(2, 0x800c6eccL);

  /** TODO unknown size, maybe struct or array */
  public static final Value _800c6ef0 = MEMORY.ref(2, 0x800c6ef0L);

  /** TODO unknown size, maybe struct or array */
  public static final Value _800c6f04 = MEMORY.ref(2, 0x800c6f04L);

  public static final Value _800c6f4c = MEMORY.ref(2, 0x800c6f4cL);

  public static final Value _800c6fec = MEMORY.ref(1, 0x800c6fecL);

  public static final Value _800c7114 = MEMORY.ref(2, 0x800c7114L);

  public static final Value _800c7190 = MEMORY.ref(1, 0x800c7190L);
  public static final Value _800c7191 = MEMORY.ref(1, 0x800c7191L);
  public static final Value _800c7192 = MEMORY.ref(1, 0x800c7192L);
  public static final Value _800c7193 = MEMORY.ref(1, 0x800c7193L);

  public static final Value _800c71ec = MEMORY.ref(1, 0x800c71ecL);

  public static final Value _800c71f0 = MEMORY.ref(4, 0x800c71f0L);

  public static final Value _800fa6dc = MEMORY.ref(4, 0x800fa6dcL);
  public static final UnboundedArrayRef<RECT> _800fa6e0 = MEMORY.ref(2, 0x800fa6e0L, UnboundedArrayRef.of(0x8, RECT::new));

  public static final SVECTOR _800fab98 = MEMORY.ref(2, 0x800fab98L, SVECTOR::new);
  public static final SVECTOR _800faba0 = MEMORY.ref(2, 0x800faba0L, SVECTOR::new);
  public static final VECTOR _800faba8 = MEMORY.ref(4, 0x800faba8L, VECTOR::new);

  public static final Value _800fabb8 = MEMORY.ref(1, 0x800fabb8L);

  /** TODO jump table */
  public static final Value _800fabbc = MEMORY.ref(4, 0x800fabbcL);
  /** TODO jump table */
  public static final Value _800fabdc = MEMORY.ref(4, 0x800fabdcL);
  /** TODO jump table */
  public static final Value _800fabfc = MEMORY.ref(4, 0x800fabfcL);
  /** TODO jump table */
  public static final Value _800fac3c = MEMORY.ref(4, 0x800fac3cL);
  /** TODO jump table */
  public static final Value _800fac5c = MEMORY.ref(4, 0x800fac5cL);
  /** TODO jump table */
  public static final Value _800fac9c = MEMORY.ref(4, 0x800fac9cL);
  /** TODO jump table */
  public static final Value _800fad7c = MEMORY.ref(4, 0x800fad7cL);
  /** TODO jump table */
  public static final Value _800fad90 = MEMORY.ref(4, 0x800fad90L);
  /** TODO jump table */
  public static final Value _800fad9c = MEMORY.ref(4, 0x800fad9cL);

  public static final ScriptFile script_800faebc = MEMORY.ref(4, 0x800faebcL, ScriptFile::new);

  public static final Value _800faec4 = MEMORY.ref(2, 0x800faec4L);

  public static final Value _800fafe8 = MEMORY.ref(4, 0x800fafe8L);

  public static final ArrayRef<UnsignedByteRef> _800fb148 = MEMORY.ref(1, 0x800fb148L, ArrayRef.of(UnsignedByteRef.class, 0x40, 1, UnsignedByteRef::new));

  /** TODO array of unsigned shorts */
  public static final Value _800fb188 = MEMORY.ref(2, 0x800fb188L);

  /** TODO array of unsigned shorts */
  public static final Value _800fb198 = MEMORY.ref(2, 0x800fb198L);

  public static final ArrayRef<Pointer<LodString>> _800fb36c = MEMORY.ref(4, 0x800fb36cL, ArrayRef.of(Pointer.classFor(LodString.class),  3, 4, Pointer.deferred(4, LodString::new)));
  public static final ArrayRef<Pointer<LodString>> _800fb378 = MEMORY.ref(4, 0x800fb378L, ArrayRef.of(Pointer.classFor(LodString.class), 11, 4, Pointer.deferred(4, LodString::new)));
  public static final ArrayRef<Pointer<LodString>> _800fb3a0 = MEMORY.ref(4, 0x800fb3a0L, ArrayRef.of(Pointer.classFor(LodString.class),  7, 4, Pointer.deferred(4, LodString::new)));

  /** TODO array of pointers to shorts? */
  public static final Value _800fb444 = MEMORY.ref(4, 0x800fb444L);

  public static final ArrayRef<ByteRef> _800fb46c = MEMORY.ref(1, 0x800fb46cL, ArrayRef.of(ByteRef.class, 0x10, 1, ByteRef::new));
  public static final ArrayRef<ByteRef> _800fb47c = MEMORY.ref(1, 0x800fb47cL, ArrayRef.of(ByteRef.class, 0x10, 1, ByteRef::new));

  public static final Value _800fb5dc = MEMORY.ref(4, 0x800fb5dcL);

  public static final Value _800fb614 = MEMORY.ref(4, 0x800fb614L);

  public static final Value _800fb674 = MEMORY.ref(4, 0x800fb674L);

  public static final Value _800fb6bc = MEMORY.ref(4, 0x800fb6bcL);

  public static final Value _800fb6f4 = MEMORY.ref(4, 0x800fb6f4L);

  public static final Value _800fb72c = MEMORY.ref(4, 0x800fb72cL);

  @Method(0x800c7304L)
  public static void FUN_800c7304() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long t0;
    long t1;
    v0 = 0x8007_0000L;
    a3 = v0 - 0xe5cL;
    t0 = a3 + 0x6cL;
    v0 = 0x800c_0000L;
    a2 = MEMORY.ref(4, v0).offset(0x66d0L).get();
    a0 = 0;
    if((int)a2 <= 0) {
      a1 = a0;
    } else {
      a1 = a0;
      v0 = 0x800c_0000L;
      t1 = v0 - 0x3e40L;

      //LAB_800c7330
      do {
        v0 = a0 << 2;
        v0 = v0 + a3;
        v1 = MEMORY.ref(4, v0).offset(0x0L).get();

        v0 = v1 << 2;
        v0 = v0 + t1;
        v0 = MEMORY.ref(4, v0).offset(0x0L).get();

        v0 = MEMORY.ref(4, v0).offset(0x60L).get();

        v0 = v0 & 0x40L;
        if(v0 == 0) {
          v0 = a1 << 2;
          v0 = v0 + t0;
          MEMORY.ref(4, v0).offset(0x0L).setu(v1);
          a1 = a1 + 0x1L;
        }

        //LAB_800c736c
        a0 = a0 + 0x1L;
      } while((int)a0 < (int)a2);
    }

    //LAB_800c737c
    v0 = 0x800c_0000L;
    MEMORY.ref(4, v0).offset(0x669cL).setu(a1);
    v0 = 0x8007_0000L;
    a3 = v0 - 0xe28L;
    t0 = a3 + 0x6cL;
    v0 = 0x800c_0000L;
    a2 = MEMORY.ref(4, v0).offset(0x677cL).get();
    a0 = 0;
    if((int)a2 <= 0) {
      a1 = a0;
    } else {
      a1 = a0;
      v0 = 0x800c_0000L;
      t1 = v0 - 0x3e40L;

      //LAB_800c73b0
      do {
        v0 = a0 << 2;
        v0 = v0 + a3;
        v1 = MEMORY.ref(4, v0).offset(0x0L).get();

        v0 = v1 << 2;
        v0 = v0 + t1;
        v0 = MEMORY.ref(4, v0).offset(0x0L).get();

        v0 = MEMORY.ref(4, v0).offset(0x60L).get();

        v0 = v0 & 0x40L;
        if(v0 == 0) {
          v0 = a1 << 2;
          v0 = v0 + t0;
          MEMORY.ref(4, v0).offset(0x0L).setu(v1);
          a1 = a1 + 0x1L;
        }

        //LAB_800c73ec
        a0 = a0 + 0x1L;
      } while((int)a0 < (int)a2);
    }

    //LAB_800c73fc
    v0 = 0x800c_0000L;
    MEMORY.ref(4, v0).offset(0x6760L).setu(a1);
    v0 = 0x8007_0000L;
    a3 = v0 - 0xe18L;
    t0 = a3 + 0x6cL;
    v0 = 0x800c_0000L;
    a2 = MEMORY.ref(4, v0).offset(0x6768L).get();
    a0 = 0;
    if((int)a2 <= 0) {
      a1 = a0;
    } else {
      a1 = a0;
      v0 = 0x800c_0000L;
      t1 = v0 - 0x3e40L;

      //LAB_800c7430
      do {
        v0 = a0 << 2;
        v0 = v0 + a3;
        v1 = MEMORY.ref(4, v0).offset(0x0L).get();

        v0 = v1 << 2;
        v0 = v0 + t1;
        v0 = MEMORY.ref(4, v0).offset(0x0L).get();

        v0 = MEMORY.ref(4, v0).offset(0x60L).get();

        v0 = v0 & 0x40L;
        if(v0 == 0) {
          v0 = a1 << 2;
          v0 = v0 + t0;
          MEMORY.ref(4, v0).offset(0x0L).setu(v1);
          a1 = a1 + 0x1L;
        }

        //LAB_800c746c
        a0 = a0 + 0x1L;
      } while((int)a0 < (int)a2);
    }

    //LAB_800c747c
    v0 = 0x800c_0000L;
    MEMORY.ref(4, v0).offset(0x6758L).setu(a1);
  }

  @Method(0x800c7524L)
  public static void FUN_800c7524() {
    FUN_800c8624();

    gameState_800babc8._b4.incr();
    _800bc910.setu(0);
    _800bc914.setu(0);
    _800bc918.setu(0);
    _800bc920.setu(0);
    _800bc950.setu(0);
    _800bc954.setu(0);
    _800bc958.setu(0);
    _800bc95c.setu(0);
    _800bc960.setu(0);
    _800bc974.setu(0);
    _800bc978.setu(0);

    int charIndex = gameState_800babc8.charIndex_88.get(1).get();
    if(charIndex < 0) {
      gameState_800babc8.charIndex_88.get(1).set(gameState_800babc8.charIndex_88.get(2).get());
      gameState_800babc8.charIndex_88.get(2).set(charIndex);
    }

    //LAB_800c75c0
    charIndex = gameState_800babc8.charIndex_88.get(0).get();
    if(charIndex < 0) {
      gameState_800babc8.charIndex_88.get(0).set(gameState_800babc8.charIndex_88.get(1).get());
      gameState_800babc8.charIndex_88.get(1).set(charIndex);
    }

    //LAB_800c75e8
    charIndex = gameState_800babc8.charIndex_88.get(1).get();
    if(charIndex < 0) {
      gameState_800babc8.charIndex_88.get(1).set(gameState_800babc8.charIndex_88.get(2).get());
      gameState_800babc8.charIndex_88.get(2).set(charIndex);
    }

    //LAB_800c760c
    FUN_800ec4bc();
    FUN_800218a4();
    FUN_8001ff74();

    pregameLoadingStage_800bb10c.addu(0x1L);
  }

  @Method(0x800c7648L)
  public static void FUN_800c7648() {
    loadStage(submapStage_800bb0f4.get());
    FUN_80012b1c(0x1L, getMethodAddress(SBtld.class, "FUN_80109050", long.class), 0);
    pregameLoadingStage_800bb10c.addu(0x1L);
  }

  @Method(0x800c76a0L)
  public static void FUN_800c76a0() {
    final long s0 = _800bc960.get() & 0x3L;

    if(s0 == 0x3L) {
      setWidthAndFlags(320, 0);
      FUN_8001324c(0xcL);
      vsyncMode_8007a3b8.setu(s0);
      _800bc960.oru(0x40L);
      setProjectionPlaneDistance(320);
      FUN_800dabec();
      pregameLoadingStage_800bb10c.addu(0x1L);
    }

    //LAB_800c7718
  }

  @Method(0x800c772cL)
  public static void FUN_800c772c() {
    //LAB_800c7748
    for(int i = 0; i < 16; i++) {
      _8006e398.offset(0xd90L).offset(1, i * 0x8L).setu(0);
    }

    //LAB_800c7770
    for(int i = 0; i < 0x100; i++) {
      _8006e398.offset(0x180L).offset(i * 0x4L).setu(0);
    }

    FUN_800c8e48();

    _800bc94c.setu(0x1L);

    scriptStartEffect(0x4L, 0x1eL);

    _800bc960.oru(0x20L);
    _8006e398.offset(0xeecL).setu(0);

    FUN_800ca980();
    FUN_800c8ee4();
    FUN_800cae44();

    _800c66d0.setu(0);
    _800c6768.setu(0);
    _800c677c.setu(0);

    _8006e398.offset(1, 0xee4L).setu(gameState_800babc8.morphMode_4e2.get());

    FUN_80012b1c(0x1L, getMethodAddress(SBtld.class, "FUN_80109250", long.class), 0);

    //LAB_800c7830
    for(int i = 0; i < 12; i++) {
      _8006e398.offset(0xe0cL).offset(i * 0x4L).setu(-0x1L);
    }

    FUN_800ee610();
    FUN_800f84c0();
    FUN_800f60ac();
    FUN_800e8ffc();

    pregameLoadingStage_800bb10c.addu(0x1L);
  }

  @Method(0x800c788cL)
  public static void FUN_800c788c() {
    FUN_80012b1c(0x1L, getMethodAddress(SBtld.class, "FUN_8010955c", long.class), 0);
    pregameLoadingStage_800bb10c.addu(0x1L);
  }

  @Method(0x800c78d4L)
  public static void FUN_800c78d4() {
    FUN_80012b1c(0x2L, getMethodAddress(SItem.class, "FUN_800fbd78", long.class), 0);
    pregameLoadingStage_800bb10c.addu(0x1L);
  }

  @Method(0x800c791cL)
  public static void FUN_800c791c() {
    FUN_80012b1c(0x2L, getMethodAddress(SItem.class, "loadEncounterAssets", long.class), 0);
    pregameLoadingStage_800bb10c.addu(0x1L);
  }

  @Method(0x800c7964L)
  public static void FUN_800c7964() {
    _800bc960.oru(0xcL);

    FUN_800f84c8();
    FUN_800e9100();

    //LAB_800c79a8
    for(int index = 0; index < battleStruct1a8Count_800c66a0.get(); index++) {
      FUN_800c9708(index);
    }

    //LAB_800c79c8
    pregameLoadingStage_800bb10c.addu(0x1L);
  }

  @Method(0x800c8624L)
  public static void FUN_800c8624() {
    final BattleStruct struct = MEMORY.ref(4, addToLinkedListTail(0x1_8cb0L), BattleStruct::new);
    _1f8003f4.set(struct);

    //LAB_800c8654
    bzero(struct.getAddress(), 0x1_8cb0);

    if(gameState_800babc8.charIndex_88.get(1).get() == 0x2L || gameState_800babc8.charIndex_88.get(1).get() == 0x8L) {
      //LAB_800c8688
      struct._9cdc.setu(_8005f428.getAddress());
      struct._9ce0.setu(struct._d4b0.getAddress());
      struct._9ce4.setu(_8006f28c.getAddress());
      //LAB_800c86bc
    } else if(gameState_800babc8.charIndex_88.get(2).get() == 0x2L || gameState_800babc8.charIndex_88.get(2).get() == 0x8L) {
      //LAB_800c86d8
      struct._9cdc.setu(_8005f428.getAddress());
      struct._9ce0.setu(_8006f28c.getAddress());
      struct._9ce4.setu(struct._d4b0.getAddress());
    } else {
      //LAB_800c870c
      struct._9cdc.setu(struct._d4b0.getAddress());
      struct._9ce0.setu(_8005f428.getAddress());
      struct._9ce4.setu(_8006f28c.getAddress());
    }

    //LAB_800c8738
  }

  @Method(0x800c8774L)
  public static void FUN_800c8774(final MrgFile mrg) {
    FUN_800c8ce4();

    if(mrg.entries.get(0).size.get() > 0 && mrg.entries.get(1).size.get() > 0 && mrg.entries.get(2).size.get() > 0) {
      _800c6754.setu(0x1L);
      _800c66b8.setu(0x1L);

      final BattleRenderStruct struct = _1f8003f4.deref().render_963c;
      FUN_800eb9ac(struct, mrg.getFile(0, ExtendedTmd::new), mrg.getFile(1, TmdAnimationFile::new));
      struct.coord2_558.coord.transfer.set(0, 0, 0);
      struct.param_5a8.rotate.set((short)0, (short)0x400, (short)0);
    }

    //LAB_800c8818
  }

  @Method(0x800c79f0L)
  public static void FUN_800c79f0() {
    long v0;
    long v1;
    v1 = 0x800c_0000L;
    v0 = 0x8007_0000L;
    v0 = MEMORY.ref(4, v0).offset(-0xe5cL).get();
    MEMORY.ref(4, v1).offset(0x66c8L).setu(v0);
    FUN_800f417c();
    pregameLoadingStage_800bb10c.addu(0x1L);
  }

  @Method(0x800c7a30L)
  public static void FUN_800c7a30() {
    FUN_80012b1c(0x3L, getMethodAddress(Bttl.class, "FUN_800c7a78", long.class), 0);
    pregameLoadingStage_800bb10c.addu(0x1L);
  }

  @Method(0x800c7a80L)
  public static void FUN_800c7a80() {
    long v0;
    long v1;
    long s0;
    long s1;
    long s2;
    long s3;
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x66a8L).get();

    if(v0 != 0) {
      v1 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v1).offset(-0x36a0L).get();

      v0 = v0 | 0x10L;
      MEMORY.ref(4, v1).offset(-0x36a0L).setu(v0);
      v0 = 0x8007_0000L;
      s3 = v0 - 0xe5cL;
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x66d0L).get();

      if((int)v0 > 0) {
        s0 = 0;
        v0 = 0x800c_0000L;
        s2 = v0 - 0x3e40L;

        //LAB_800c7ae4
        do {
          v0 = s0 << 2;
          v0 = v0 + s3;
          v0 = MEMORY.ref(4, v0).offset(0x0L).get();

          v0 = v0 << 2;
          v0 = v0 + s2;
          v0 = MEMORY.ref(4, v0).offset(0x0L).get();

          s1 = MEMORY.ref(4, v0).offset(0x0L).get();
          v0 = MEMORY.ref(4, v0).offset(0x60L).get();

          v0 = v0 & 0x4L;
          if(v0 != 0) {
            s0 = s0 + 0x1L;
            v0 = FUN_800133ac();
            v1 = v0 << 3;
            v1 = v1 - v0;
            v0 = v1 << 5;
            v0 = v0 - v1;
            v0 = (int)v0 >> 16;
            MEMORY.ref(2, s1).offset(0x4cL).setu(v0);
          } else {
            //LAB_800c7b3c
            s0 = s0 + 0x1L;
            v0 = FUN_800133ac();
            v1 = v0 << 2;
            v1 = v1 + v0;
            v1 = v1 << 2;
            v1 = v1 + v0;
            v1 = v1 << 3;
            v1 = v1 - v0;
            v1 = (int)v1 >> 16;
            v1 = v1 + 0x32L;
            MEMORY.ref(2, s1).offset(0x4cL).setu(v1);
          }

          //LAB_800c7b68
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x66d0L).get();
        } while((int)s0 < (int)v0);
      }

      //LAB_800c7b80
      FUN_80021868();
      v1 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v1).offset(-0x4ef4L).get();

      v0 = v0 + 0x1L;
      MEMORY.ref(4, v1).offset(-0x4ef4L).setu(v0);
    }

    //LAB_800c7b9c
  }

  @Method(0x800c7a78L)
  public static void FUN_800c7a78(final long param) {
    // empty
  }

  @Method(0x800c7bb8L)
  public static void FUN_800c7bb8() {
    FUN_800ef9e4();
    FUN_800efd34();

    if(_800bc974.get() != 0) {
      pregameLoadingStage_800bb10c.addu(0x1L);
      return;
    }

    if(fileCount_8004ddc8.get() == 0 && (int)_800c66d0.get() > 0 && _800c66b9.get() == 0 && FUN_800c7da8() != 0) {
      vsyncMode_8007a3b8.setu(0x3L);
      _800fa6dc.setu(0x80L);
      scriptStatePtrArr_800bc1c0.get((int)_800c66c8.get()).deref().ui_60.and(0xffff_efffL);

      if((int)_800c6760.get() <= 0) {
        loadMusicPackage(19, 0);
        _800bc974.setu(0x2L);
      } else {
        //LAB_800c7c98
        final long a1 = FUN_800c7e24();
        _800c66bc.setu(a1);

        if((int)a1 >= 0) {
          scriptStatePtrArr_800bc1c0.get((int)a1).deref().ui_60.or(0x1008L).and(0xffff_ffdfL);
          _800c66c8.setu(a1);
        } else {
          //LAB_800c7ce8
          if((int)_800c6758.get() > 0) {
            //LAB_800c7d3c
            final long a1_0 = FUN_800c7ea0();
            _800c66c8.setu(a1_0);
            scriptStatePtrArr_800bc1c0.get((int)a1_0).deref().ui_60.or(0x1008L);

            //LAB_800c7d74
          } else {
            FUN_80020308();

            if(encounterId_800bb0f8.get() != 0x1bbL) {
              _800bc974.setu(0x1L);
              FUN_8001af00(0x6L);
            } else {
              //LAB_800c7d30
              _800bc974.setu(0x4L);
            }
          }
        }
      }
    }

    //LAB_800c7d78
    if(_800bc974.get() != 0) {
      //LAB_800c7d88
      pregameLoadingStage_800bb10c.addu(0x1L);
    }

    //LAB_800c7d98
  }

  @Method(0x800c7da8L)
  public static long FUN_800c7da8() {
    //LAB_800c7dd8
    for(int i = 0; i < _800c66d0.get(); i++) {
      if((scriptStatePtrArr_800bc1c0.get((int)_8006e398.offset(4, 0xe0cL).offset(i * 0x4L).get()).deref().ui_60.get() & 0x408L) != 0) {
        return 0;
      }

      //LAB_800c7e10
    }

    //LAB_800c7e1c
    return 0x1L;
  }

  @Method(0x800c7e24L)
  public static long FUN_800c7e24() {
    //LAB_800c7e54
    for(int i = 0; i < _800c669c.get(); i++) {
      final long v1 = _8006e398.offset(4, 0xe78L).offset(i * 0x4L).get();
      if((scriptStatePtrArr_800bc1c0.get((int)v1).deref().ui_60.get() & 0x20L) != 0) {
        return v1;
      }

      //LAB_800c7e8c
    }

    //LAB_800c7e98
    return -0x1L;
  }

  @Method(0x800c7ea0L)
  public static long FUN_800c7ea0() {
    long v0;
    long v1;
    long a0;
    long a1;
    long s0;
    long s6 = 0;
    long hi;
    long lo;

    //LAB_800c7ee4
    for(int s4 = 0; s4 < 32; s4++) {
      //LAB_800c7ef0
      a0 = 0;
      for(int s1 = 0; s1 < _800c669c.get(); s1++) {
        s0 = scriptStatePtrArr_800bc1c0.get((int)_8006e398.offset(4, 0xe78L).offset(s1 * 0x4L).get()).deref().innerStruct_00.getPointer(); //TODO
        v1 = MEMORY.ref(2, s0).offset(0x4cL).getSigned();

        if((int)v1 >= (int)a0) {
          a0 = v1;
          s6 = s1;
        }

        //LAB_800c7f30
      }

      //LAB_800c7f40
      if((int)a0 >= 0xdaL) {
        a1 = _8006e398.offset(0xe78L).offset(s6 * 0x4L).get();
        v1 = scriptStatePtrArr_800bc1c0.get((int)a1).getPointer(); //TODO
        s0 = MEMORY.ref(4, v1).offset(0x0L).get();
        MEMORY.ref(2, s0).offset(0x4cL).setu(a0 - 0xd9L);

        if((MEMORY.ref(4, v1).offset(0x60L).get() & 0x4L) == 0) {
          v1 = 0x800c_0000L;
          v1 = v1 - 0x5438L;
          MEMORY.ref(4, v1).offset(0xb8L).addu(0x1L);
        }

        //LAB_800c7f9c
        return a1;
      }

      //LAB_800c7fa4
      //LAB_800c7fb0
      for(int s1 = 0; s1 < _800c669c.get(); s1++) {
        s0 = scriptStatePtrArr_800bc1c0.get((int)_8006e398.offset(0xe78L).offset(s1 * 0x4L).get()).deref().innerStruct_00.getPointer(); //TODO
        a0 = MEMORY.ref(2, s0).offset(0x32L).getSigned();
        v0 = FUN_800133ac() + 0x4_4925L;
        lo = ((long)(int)a0 * (int)v0) & 0xffff_ffffL;
        a0 = lo;
        v0 = 0x35c2_9183L; //TODO _pretty_ sure this is roughly /312,000 (seems oddly specific?)
        hi = ((long)(int)a0 * (int)v0) >>> 32;
        v1 = hi;
        v1 = (int)v1 >> 16;
        a0 = (int)a0 >> 31;
        v1 = v1 - a0;
        MEMORY.ref(2, s0).offset(0x4cL).addu(v1);
      }

      //LAB_800c8028
    }

    v0 = 0x8007_0000L;
    v0 = MEMORY.ref(4, v0).offset(-0xdbcL).get();

    //LAB_800c8040
    return v0;
  }

  @Method(0x800c882cL)
  public static void FUN_800c882c() {
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
    long t5;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long s6;
    long s7;
    long fp;
    long hi;
    long lo;
    long sp10;
    long sp18;
    long sp14;
    if(_800c6764.get() == 0 || _800c66d4.get() == 0 || (_800bc960.get() & 0x80L) == 0) {
      //LAB_800c8ad8
      v0 = 0x800c_0000L;

      //LAB_800c8adc
      MEMORY.ref(4, v0).offset(-0x5440L).setu(0);
      v0 = 0x800c_0000L;
      MEMORY.ref(4, v0).offset(-0x4efcL).setu(0);
      v0 = 0x8008_0000L;
      MEMORY.ref(4, v0).offset(-0x5c58L).setu(0);
    } else {
      s7 = 0x1f80_0000L;
      v0 = FUN_800dd118();
      s0 = v0;
      v0 = FUN_800dd0d4();
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x66ccL).get();

      lo = (long)(int)v1 * (int)s0 & 0xffff_ffffL;
      t4 = 0x800c_0000L;
      fp = 0x8000L;
      t1 = MEMORY.ref(4, t4).offset(0x6774L).get();
      v1 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v1).offset(0x676cL).get();
      a0 = MEMORY.ref(4, s7).offset(0x3f4L).get();
      t1 = t1 + v1;
      v1 = a0 + fp;
      t3 = MEMORY.ref(2, v1).offset(0x1cc4L).get();
      t0 = lo;
      a1 = (int)t0 >> 12;
      a1 = a1 + t1;
      hi = (int)a1 % (int)t3;
      a3 = hi;
      a2 = 0;
      t2 = 0x800c_0000L;
      s6 = 0x9cb0L;
      v0 = v0 + 0x800L;
      v0 = v0 & 0xfffL;
      s1 = 0x1f80_0000L;
      s4 = 0x1f80_0000L;
      s5 = 0x8010_0000L;
      v1 = MEMORY.ref(4, t2).offset(0x6778L).get();
      t0 = 0x800c_0000L;
      t0 = MEMORY.ref(4, t0).offset(0x6770L).get();
      a0 = a0 + s6;
      MEMORY.ref(4, t4).offset(0x6774L).setu(t1);
      v1 = v1 + t0;
      t0 = s1 + 0x3dcL;
      MEMORY.ref(4, t2).offset(0x6778L).setu(v1);
      v1 = v1 - v0;
      v1 = v1 + 0x760L;
      v0 = MEMORY.ref(2, t0).offset(0x2L).getSigned();
      t0 = MEMORY.ref(2, s1).offset(0x3dcL).getSigned();
      s2 = v1 - v0;
      v0 = MEMORY.ref(4, s4).offset(0x3c8L).get();
      v1 = MEMORY.ref(4, s5).offset(-0x5924L).get();
      a1 = 0x140L;
      sp10 = s2;
      v0 = v0 - 0x2L;
      sp14 = v0;
      sp18 = v1;
      a3 = a3 - t0;
      s0 = a3 - t3;
      s3 = a3 + t3;
      FUN_8001814c(a0, a1, a2, a3, sp10, sp14, sp18);
      a1 = 0x140L;
      a2 = 0;
      a0 = MEMORY.ref(4, s7).offset(0x3f4L).get();
      v0 = MEMORY.ref(4, s4).offset(0x3c8L).get();
      v1 = MEMORY.ref(4, s5).offset(-0x5924L).get();
      a3 = s0;
      sp10 = s2;
      a0 = a0 + s6;
      v0 = v0 - 0x2L;
      sp14 = v0;
      sp18 = v1;
      FUN_8001814c(a0, a1, a2, a3, sp10, sp14, sp18);
      v0 = MEMORY.ref(2, s1).offset(0x3dcL).getSigned();

      if((int)v0 >= (int)s3) {
        a1 = 0x140L;
        a2 = 0;
        a0 = MEMORY.ref(4, s7).offset(0x3f4L).get();
        v0 = MEMORY.ref(4, s4).offset(0x3c8L).get();
        v1 = MEMORY.ref(4, s5).offset(-0x5924L).get();
        a3 = s3;
        sp10 = s2;
        a0 = a0 + s6;
        v0 = v0 - 0x2L;
        sp14 = v0;
        sp18 = v1;
        FUN_8001814c(a0, a1, a2, a3, sp10, sp14, sp18);
      }

      //LAB_800c89d4
      v0 = MEMORY.ref(4, s7).offset(0x3f4L).get();
      v1 = 0x151_0000L;
      a0 = v0 + fp;
      v0 = MEMORY.ref(4, a0).offset(0x1cb0L).get();
      v1 = v1 | 0x434dL;
      if(v0 == v1) {
        a0 = s2;
      } else {
        //LAB_800c89f8
        v0 = MEMORY.ref(2, a0).offset(0x1cdaL).getSigned();
        a0 = s2 + v0;
      }

      //LAB_800c8a04
      v0 = 0x1f80_0000L;
      v1 = 0x8010_0000L;
      v0 = MEMORY.ref(2, v0).offset(0x3deL).getSigned();
      a1 = MEMORY.ref(4, v1).offset(-0x5924L).get();
      v0 = -v0;
      if((int)a0 >= (int)v0) {
        v0 = 0x1f80_0000L;
        a0 = MEMORY.ref(4, v0).offset(0x3f4L).get();
        v0 = 0x8000L;
        a0 = a0 + v0;
        v0 = MEMORY.ref(1, a0).offset(0x1cc8L).get();

        lo = (long)(int)v0 * (int)a1 & 0xffff_ffffL;
        v1 = 0x8008_0000L;
        t5 = lo;
        v0 = (int)t5 >> 7;
        MEMORY.ref(4, v1).offset(-0x5c58L).setu(v0);
        v0 = MEMORY.ref(1, a0).offset(0x1cc9L).get();

        lo = (long)(int)v0 * (int)a1 & 0xffff_ffffL;
        v1 = 0x800c_0000L;
        t5 = lo;
        v0 = (int)t5 >> 7;
        MEMORY.ref(4, v1).offset(-0x4efcL).setu(v0);
        v0 = MEMORY.ref(1, a0).offset(0x1ccaL).get();
        lo = (long)(int)v0 * (int)a1 & 0xffff_ffffL;
      } else {
        v0 = 0x1f80_0000L;

        //LAB_800c8a74
        a0 = MEMORY.ref(4, v0).offset(0x3f4L).get();
        v0 = 0x8000L;
        a0 = a0 + v0;
        v0 = MEMORY.ref(1, a0).offset(0x1cd0L).get();

        lo = (long)(int)v0 * (int)a1 & 0xffff_ffffL;
        v1 = 0x8008_0000L;
        t5 = lo;
        v0 = (int)t5 >> 7;
        MEMORY.ref(4, v1).offset(-0x5c58L).setu(v0);
        v0 = MEMORY.ref(1, a0).offset(0x1cd1L).get();

        lo = (long)(int)v0 * (int)a1 & 0xffff_ffffL;
        v1 = 0x800c_0000L;
        t5 = lo;
        v0 = (int)t5 >> 7;
        MEMORY.ref(4, v1).offset(-0x4efcL).setu(v0);
        v0 = MEMORY.ref(1, a0).offset(0x1cd2L).get();

        lo = (long)(int)v0 * (int)a1 & 0xffff_ffffL;
      }

      //LAB_800c8ac4
      t5 = lo;
      _800babc0.setu((int)t5 >> 7);
    }

    //LAB_800c8af0
  }

  @Method(0x800c8b20L)
  public static void loadStage(final long stage) {
    loadDrgnBinFile(0, 2497 + stage, 0, getMethodAddress(Bttl.class, "stageMrgLoadedCallback", long.class, long.class, long.class), 0, 0x2L);
    currentStage_800c66a4.setu(stage);
  }

  @Method(0x800c8b74L)
  public static void stageMrgLoadedCallback(final long address, final long fileSize, final long param) {
    _1f8003f4.deref().stageMrg_638.setPointer(address);

    final MrgFile mrg = _1f8003f4.deref().stageMrg_638.deref();

    // MCQ
    if((int)mrg.entries.get(1).size.get() > 0) {
      loadStageMcq(mrg.getFile(1, McqHeader::new));
    }

    //LAB_800c8bb0
    // TIM
    if((int)mrg.entries.get(2).size.get() > 0) {
      loadStageTim(mrg.getFile(2));
    }

    //LAB_800c8bcc
    // Scripted TMD
    if((int)mrg.entries.get(0).size.get() > 0) {
      decompress(mrg.getFile(0), _1f8003f4.deref().stageTmdMrg_63c.getAddress(), getMethodAddress(Bttl.class, "stageTmdMrgLoaded", long.class, long.class, long.class), 0, 0);
    } else {
      //LAB_800c8c0c
      FUN_800127cc(mrg.getAddress(), 0, 0x1L);
    }

    //LAB_800c8c24
  }

  @Method(0x800c8c38L)
  public static void stageTmdMrgLoaded(final long address, final long fileSize, final long param) {
    final MrgFile mrg = MEMORY.ref(4, address, MrgFile::new);

    FUN_800c8774(mrg);
    FUN_800127cc(_1f8003f4.deref().stageMrg_638.getPointer(), 0, 0x1L);
    _1f8003f4.deref().stageMrg_638.clear();
  }

  @Method(0x800c8c84L)
  public static void loadStageTim(final long a0) {
    final TimHeader tim = parseTimHeader(MEMORY.ref(4, a0 + 0x4L));
    LoadImage(tim.getImageRect(), tim.getImageAddress());

    if(tim.hasClut()) {
      LoadImage(tim.getClutRect(), tim.getClutAddress());
    }

    //LAB_800c8ccc
    FUN_800ec8d0(a0);
  }

  @Method(0x800c8ce4L)
  public static void FUN_800c8ce4() {
    _800c66b8.setu(0);
  }

  @Method(0x800c8cf0L)
  public static void FUN_800c8cf0() {
    if(_800c66b8.get() != 0 && _800c6754.get() != 0 && (_800bc960.get() & 0x20L) != 0) {
      FUN_800ec744(_1f8003f4.deref().render_963c);
      FUN_800ec51c(_1f8003f4.deref().render_963c);
    }

    //LAB_800c8d50
  }

  @Method(0x800c8d64L)
  public static void loadStageMcq(final McqHeader mcq) {
    final long x;
    if((_800bc960.get() & 0x80L) != 0) {
      x = 320;
      _800c6764.setu(0x1L);
    } else {
      //LAB_800c8d98
      x = 512;
    }

    //LAB_800c8d9c
    loadMcq(mcq, x, 0);

    //LAB_800c8dc0
    memcpy(_1f8003f4.deref().stageMcq_9cb0.getAddress(), mcq.getAddress(), 0x2c);

    _800c66d4.setu(0x1L); //1b
    _800c66cc.setu((0x400L / mcq._14.get() + 0x1L) * mcq._14.get());
  }

  @Method(0x800c8e48L)
  public static void FUN_800c8e48() {
    if(_800c66d4.get() != 0 && (_800bc960.get() & 0x80L) == 0) {
      final RECT sp0x10 = new RECT((short)512, (short)0, _1f8003f4.deref().stageMcq_9cb0.width_08.get(), (short)256);
      MoveImage(sp0x10, 320, 0);
      _800c6764.setu(0x1L);
      _800bc960.oru(0x80);
    }

    //LAB_800c8ec8
  }

  @Method(0x800c8ee4L)
  public static void FUN_800c8ee4() {
    //LAB_800c8ef4
    for(int i = 0; i < 0x424; i++) {
      MEMORY.ref(4, _8005e398.getAddress()).offset(i * 0x4L).setu(0); //TODO
    }

    _800c66c0.setu(0x1L);
  }

  @Method(0x800c9708L)
  public static void FUN_800c9708(final long a0) {
    final long fileIndex;
    long a2 = 0;
    long a3 = 0;
    final BattleStruct1a8 v1 = _8005e398.get((int)a0);
    long a0_0;

    if((int)v1._1a2.get() >= 0 && v1._04.get() == 0) {
      v1._19e.or(0x10);

      if((v1._19e.get() & 0x4L) == 0) {
        a3 = a3 | 0x7fL;
        a3 = a3 & 0xffff_81ffL;
        a3 = a3 | (a0 & 0x3fL) << 9;
        a3 = a3 & 0xffff_ff7fL;
        a3 = a3 | 0x100L;
        fileIndex = 3593 + v1._1a2.get();
      } else {
        //LAB_800c97a4
        a2 = a2 & 0xffff_ff80L;
        a0_0 = v1._1a2.get() & 0x1L;
        a2 = a2 | v1._19c.get() & 0x7fL;
        a2 = a2 & 0xffff_81ffL;
        a2 = a2 | (a0 & 0x3fL) << 9;
        a2 = a2 & 0xffff_ff7fL;
        a2 = a2 | a0_0 << 7;
        a3 = a2 & 0xffff_feffL;
        final long charIndex = gameState_800babc8.charIndex_88.get(v1._19c.get()).get();
        if(a0_0 == 0) {
          fileIndex = 4031 + gameState_800babc8.charData_32c.get((int)charIndex).selectedAddition_19.get() + charIndex * 0x8L - additionOffsets_8004f5ac.get((int)charIndex).get();
          //LAB_800c983c
        } else if(charIndex == 0 && ((byte)gameState_800babc8.dragoonSpirits_19c.get(0).get() >>> 7) != 0) { // Divine dragoon?
          fileIndex = 4112;
        } else {
          fileIndex = 4103 + charIndex;
        }
      }

      //LAB_800c9860
      //LAB_800c9864
      loadDrgnBinFile(0, fileIndex, 0, getMethodAddress(Bttl.class, "FUN_800c9898", long.class, long.class, long.class), a3, 0x3L);
    }

    //LAB_800c9888
  }

  @Method(0x800c8f24L)
  public static BattleStruct1a8 getBattleStruct1a8(final int index) {
    return _8005e398.get(index);
  }

  @Method(0x800c8f50L)
  public static long FUN_800c8f50(final long a0, final long a1) {
    //LAB_800c8f6c
    for(int i = 0; i < 10; i++) {
      final BattleStruct1a8 a2 = _8005e398.get(i);

      if((a2._19e.get() & 0x1L) == 0) {
        if((int)a1 < 0) {
          a2._19e.set(1);
        } else {
          //LAB_800c8f90
          a2._19e.set(5);
        }

        //LAB_800c8f94
        a2._19c.set((short)a1);
        a2._1a0.set((short)0);
        a2._1a2.set((short)a0);
        a2._1a4.set((short)-1);
        a2._1a6.set((short)-1);
        battleStruct1a8Count_800c66a0.addu(0x1L);
        return i;
      }

      //LAB_800c8fbc
    }

    return -0x1L;
  }

  @Method(0x800c9060L)
  public static int FUN_800c9060(final long a0) {
    //LAB_800c906c
    for(int i = 0; i < 10; i++) {
      final BattleStruct1a8 v1 = _8005e398.get(i);

      if((v1._19e.get() & 0x1L) != 0 && v1._1a2.get() == a0) {
        //LAB_800c90a8
        return i;
      }

      //LAB_800c9090
    }

    return -1;
  }

  @Method(0x800c90b0L)
  public static long FUN_800c90b0(final int index) {
    //LAB_800c9114
    if((_8005e398.get(index)._1a4.get() >= 0 || !_8005e398.get(index).mrg_00.isNull() && _8005e398.get(index).mrg_00.deref().entries.get(32).size.get() != 0) && FUN_800ca054(index, 0) != 0) { //TODO
      return 0x1L;
    }

    //LAB_800c9128
    //LAB_800c912c
    return 0;
  }

  @Method(0x800c913cL)
  public static ScriptFile FUN_800c913c(final int index) {
    return _8005e398.get(index).script_10.deref();
  }

  @Method(0x800c9290L)
  public static void FUN_800c9290(long a0) {
    long a2 = 0; //TODO this was uninitialized, is the flow right?
    long a3 = 0; //TODO this was uninitialized, is the flow right?

    final BattleStruct1a8 t1 = _8005e398.get((int)a0);
    final long callbackParam;
    final long fileIndex;

    if((int)t1._1a2.get() >= 0) {
      if((t1._19e.get() & 0x8L) == 0) {
        if(t1.mrg_00.isNull()) {
          t1._19e.or(0x28);
          if((t1._19e.get() & 0x4L) == 0) {
            a3 = a3 | 0x7fL;
            a3 = a3 & 0xffff_81ffL;
            a3 = a3 | (a0 & 0x3fL) << 9;
            a3 = a3 & 0xffff_ff7fL;
            callbackParam = a3 | 0x100L;
            fileIndex = 3137 + t1._1a2.get();
            a2 = 0;
          } else {
            //LAB_800c9334
            a2 = a2 & 0xffff_ff80L;
            a2 = a2 | t1._19c.get() & 0x7fL;
            a2 = a2 & 0xffff_81ffL;
            a2 = a2 | (a0 & 0x3fL) << 9;
            a2 = a2 & 0xffff_ff7fL;
            a2 = a2 | (t1._1a2.get() & 0x1L) << 7;
            callbackParam = a2 & 0xffff_feffL;
            a0 = gameState_800babc8.charIndex_88.get(t1._19c.get()).get();
            if(t1._1a2.get() != 0) {
              if(a0 == 0) {
                if((gameState_800babc8.dragoonSpirits_19c.get(0).get() >>> 7) == 0) {
                  a0 = a0 + 0x9L;
                } else {
                  a0 = 0x12L;
                }
              } else {
                //LAB_800c93b4
                a0 = a0 + 0x9L;
              }

              //LAB_800c93b8
            }

            //LAB_800c93bc
            fileIndex = 3994 + a0 * 2;
            a2 = _1f8003f4.deref()._9cdc.offset(t1._19c.get() * 0x4L).get();
            t1._19e.or(0x2);
          }

          //LAB_800c93e8
          loadDrgnBinFile(0, fileIndex, a2, getMethodAddress(Bttl.class, "FUN_800c941c", long.class, long.class, long.class), callbackParam, 0x3L);
        }
      }
    }

    //LAB_800c940c
  }

  @Method(0x800c941cL)
  public static void FUN_800c941c(final long address, final long fileSize, final long param) {
    final long s3 = param >>> 9 & 0x3fL;
    final long s0 = param >>> 8 & 0x1L;

    final BattleStruct1a8 a0 = getBattleStruct1a8((int)s3);
    a0._19e.and(0xffdf);

    if(s0 == 0) {
      _800bc960.oru(0x4L);
    }

    //LAB_800c947c
    a0.mrg_00.setPointer(address);
    final MrgFile mrg = a0.mrg_00.deref();

    if(mrg.entries.get(34).size.get() != 0) {
      a0.script_10.set(mrg.getFile(34, ScriptFile::new));
      _8005e398_SCRIPT_SIZES.remove((int)s3);
      _8005e398_SCRIPT_SIZES.put((int)s3, new Tuple<>("BTTL %d script MRG file 34".formatted(s3), (int)fileSize));
    }

    //LAB_800c94a0
    //LAB_800c94a4
    for(int i = 0; i < 32; i++) {
      final long size = mrg.entries.get(i).size.get();

      if(size != 0) {
        FUN_800c9a80(mrg.getFile(i), size, 0x1L, 0, s3, i);
      }

      //LAB_800c94cc
    }
  }

  @Method(0x800c952cL)
  public static void FUN_800c952c(final long a0, final int index) {
    final BattleStruct1a8 s0 = _8005e398.get(index);

    final ExtendedTmd s2;
    if(s0._1a4.get() >= 0) {
      s2 = MEMORY.ref(4, FUN_800cad34(s0._1a4.get()), ExtendedTmd::new); //TODO
    } else {
      //LAB_800c9590
      final MrgFile mrg = s0.mrg_00.derefNullable();

      if(mrg != null && mrg.entries.get(32).size.get() != 0) {
        s2 = mrg.getFile(32, ExtendedTmd::new);
      } else {
        throw new RuntimeException("s2 undefined");
      }
    }

    //LAB_800c95bc
    s0.tmd_08.set(s2);

    if((s0._19e.get() & 0x4L) != 0) {
      final TmdAnimationFile v0 = FUN_800ca31c(index, 0);
      final long a0_0 = _1f8003f4.deref()._9ce8.offset(s0._19c.get() * 0x1298L).getAddress(); //TODO

      MEMORY.ref(4, a0).offset(0x0L).setu(a0_0);
      MEMORY.ref(4, a0).offset(0x4L).setu(a0_0 + 0x230L);
      MEMORY.ref(4, a0).offset(0x8L).setu(a0_0 + 0xd20L);
      MEMORY.ref(2, a0).offset(0xc8L).setu(0x23L);

      final long a3;
      if((s0._1a2.get() & 0x1L) != 0) {
        a3 = 0x9L;
      } else {
        a3 = (s0._1a2.get() - 0x200) >>> 1;
      }

      //LAB_800c9650
      FUN_80021520(MEMORY.ref(4, a0, BigStruct::new), s2, v0, a3); //TODO
    } else {
      //LAB_800c9664
      FUN_80020a00(MEMORY.ref(4, a0, BigStruct::new), s2, FUN_800ca31c(index, 0)); //TODO
    }

    //LAB_800c9680
    s0._14.get(0)._09.incr();
  }

  @Method(0x800c9898L)
  public static void FUN_800c9898(final long address, final long fileSize, final long param) {
    long s5 = address;
    long s6 = param >>> 9 & 0x3fL;
    long s0 = param >>> 8 & 0x1L;
    long s7 = (int)(param << 25) >> 25;
    final BattleStruct1a8 fp = getBattleStruct1a8((int)s6);

    if(fp._04.get() != 0) {
      removeFromLinkedList(s5);
    } else {
      //LAB_800c9910
      if(s0 == 0 && MEMORY.ref(4, s5).offset(0x4L).get() == 0x40L) {
        _8006e398.offset(0xd80L).offset(s7 * 0x4L).setu(0);
        long s4 = 0x100L;

        //LAB_800c9940
        for(int i = 0; i < 32; i++) {
          if(MEMORY.ref(4, s5).offset(0xcL).offset(s4).get() != 0) {
            if(fp._14.get(i)._09.get() != 0) {
              FUN_800c9c7c(s6, i);
            }

            //LAB_800c9974
            FUN_800c9a80(s5 + MEMORY.ref(4, s5).offset(0x8L).offset(s4).get(), MEMORY.ref(4, s5).offset(0xcL).offset(s4).get(), 0x6L, s7, s6, i);
          }

          //LAB_800c9990
          s4 = s4 + 0x8L;
        }

        DrawSync(0);

        long v0 = FUN_80012444(s5, FUN_80015704(s5, 0x20L));
        if(v0 != 0) {
          s5 = v0;
        }
      }

      //LAB_800c99d8
      fp._04.set(s5);

      //LAB_800c99e8
      for(int i = 0; i < 32; i++) {
        if(MEMORY.ref(4, s5).offset(i * 0x8L).offset(0xcL).get() != 0) {
          if(fp._14.get(i)._09.get() != 0) {
            FUN_800c9c7c(s6, i);
          }

          //LAB_800c9a18
          FUN_800c9a80(s5 + MEMORY.ref(4, s5).offset(i * 0x8L).offset(0x8L).get(), MEMORY.ref(4, s5).offset(i * 0x8L).offset(0xcL).get(), 0x2L, 0x1L, s6, i);
        }

        //LAB_800c9a34
      }
    }

    //LAB_800c9a48
  }

  @Method(0x800c9a80L)
  public static void FUN_800c9a80(final long a0, long a1, long a2, long a3, long a4, long a5) {
    final boolean isBpe = MEMORY.ref(4, a0).offset(0x4L).get() == 0x1a45_5042L; // BPE
    final BattleStruct1a8_c s3 = _8005e398.get((int)a4)._14.get((int)a5);

    if(s3._0a.get() != 0) {
      FUN_800c9c7c(a4, a5);
    }

    //LAB_800c9b28
    if(a2 == 0x1L) {
      //LAB_800c9b68
      if(isBpe) {
        s3._0a.set(0x4);
        s3._00.set(a0);
        s3._0b.set(0);
        s3._08.set((int)a3);
      } else {
        s3._0a.set((int)a2);
        s3._00.set(a0);
        s3._0b.set((int)a2);
        s3._08.set((int)a3);
      }
    } else if(a2 == 0x2L) {
      //LAB_800c9b80
      if(isBpe) {
        //LAB_800c9b88
        s3._0a.set(0x5);
        s3._00.set(a0);
        s3._0b.set(0);
        s3._08.set((int)a3);
      } else {
        //LAB_800c9b98
        s3._0a.set((int)a2);
        s3._00.set(a0);
        s3._0b.set(0x1);

        //LAB_800c9ba8
        s3._08.set((int)a3);
      }
      //LAB_800c9b4c
    } else if(a2 == 0x3L) {
      //LAB_800c9bb0
      s3._0b.set(0x1);
      s3._0a.set((int)a2);
      //TODO wtf?
      s3._00.set(a3);
      s3._08.set(-0x1);
    } else if(a2 == 0x6L) {
      //LAB_800c9bcc
      final RECT sp0x10 = new RECT((short)(512 + a3 * 64), (short)_8006e398.offset(0xd80L).offset(4, a3 * 0x4L).get(), (short)64, (short)(a1 / 128));
      LoadImage(sp0x10, a0);

      _8006e398.offset(0xd80L).offset(4, a3 * 0x4L).addu(a1 / 128);
      //TODO wtf?
      s3._00.set((sp0x10.h.get() & 0xff) << 24 | (sp0x10.y.get() & 0xff) << 16 | sp0x10.x.get() & 0xffff);
      s3._08.set(-0x1);
      s3._0a.set((int)a2);
      s3._0b.set(0);
    } else {
      return;
    }

    //LAB_800c9c44
    s3._04.set((short)-1);
    s3._06.set((short)-1);
    s3._09.set(0);

    //LAB_800c9c54
  }

  @Method(0x800c9c7cL)
  public static void FUN_800c9c7c(long a0, long a1) {
    long v0;
    long v1;
    long s0;
    long s1 = a0;
    v0 = s1 << 1;
    v0 = v0 + s1;
    v0 = v0 << 2;
    v0 = v0 + s1;
    v0 = v0 << 2;
    v0 = v0 + s1;
    v0 = v0 << 3;
    v1 = 0x8006_0000L;
    v1 = v1 - 0x1c68L;
    v0 = v0 + v1;
    v1 = a1 << 1;
    v1 = v1 + a1;
    v1 = v1 << 2;
    v1 = v1 + 0x14L;
    s0 = v0 + v1;

    //LAB_800c9cec
    while(MEMORY.ref(1, s0).offset(0x9L).getSigned() > 0) {
      FUN_800ca194(s1, a1);
    }

    //LAB_800c9d04
    switch((int)MEMORY.ref(1, s0).offset(0xaL).get()) {
      case 3 -> FUN_800cad64(MEMORY.ref(4, s0).offset(0x0L).get());
      case 4, 5 -> {
        if(MEMORY.ref(1, s0).offset(0xbL).get() == 0) {
          break;
        }
        a0 = MEMORY.ref(2, s0).offset(0x4L).getSigned();
        if((int)a0 < 0) {
          v1 = 0x8007_0000L;
          v0 = MEMORY.ref(2, s0).offset(0x6L).getSigned();
          v1 = v1 - 0x1c68L;
          v0 = v0 << 3;
          v0 = v0 + v1;
          MEMORY.ref(1, v0).offset(0xd90L).setu(0);
        } else {
          //LAB_800c9d78
          FUN_800cad64(a0);
        }
      }

      //LAB_800c9d80
    }

    //LAB_800c9d84
    MEMORY.ref(1, s0).offset(0xaL).setu(0);
    MEMORY.ref(4, s0).offset(0x0L).setu(0);
    MEMORY.ref(2, s0).offset(0x4L).setu(-0x1L);
    MEMORY.ref(2, s0).offset(0x6L).setu(-0x1L);
    MEMORY.ref(1, s0).offset(0xbL).setu(0);
    MEMORY.ref(1, s0).offset(0x9L).setu(0);
    MEMORY.ref(1, s0).offset(0x8L).setu(-0x1L);

    //LAB_800c9da0
  }

  @Method(0x800c9e10L)
  public static void FUN_800c9e10(long a0, long a1) {
    assert false;
  }

  @Method(0x800ca054L)
  public static long FUN_800ca054(final int index, final long a1) {
    switch(_8005e398.get(index)._14.get((int)a1)._0a.get()) {
      case 0:
        return 0;

      case 1:
      case 2:
      case 3:
        return 1;

      case 4:
      case 5:
      case 6:
        if(_8005e398.get(index)._14.get((int)a1)._0b.get() == 0) {
          return 0;
        }

        //LAB_800ca0f0
        return ~_8005e398.get(index)._14.get((int)a1)._04.get() >>> 31;
    }

    //LAB_800ca0f8
    return 0;
  }

  @Method(0x800ca100L)
  public static void FUN_800ca100(final BigStruct a0, final long a1, final long a2) {
    FUN_80021584(a0, FUN_800ca31c(a1, a2));
    _8005e398.get((int)a1)._14.get(0)._09.incr();
  }

  @Method(0x800ca194L)
  public static long FUN_800ca194(long a0, long a1) {
    long v0;
    long v1;
    long s0;
    v1 = a0 << 1;
    v1 = v1 + a0;
    v1 = v1 << 2;
    v1 = v1 + a0;
    v1 = v1 << 2;
    v1 = v1 + a0;
    v1 = v1 << 3;
    v0 = 0x8006_0000L;
    v0 = v0 - 0x1c68L;
    v1 = v1 + v0;
    v0 = a1 << 1;
    v0 = v0 + a1;
    v0 = v0 << 2;
    v0 = v0 + 0x14L;
    s0 = v1 + v0;
    v0 = MEMORY.ref(1, s0).offset(0x9L).getSigned();

    a0 = v0 - 0x1L;
    if((int)a0 < 0) {
      a0 = 0;
    }

    //LAB_800ca1f4
    v1 = MEMORY.ref(1, s0).offset(0xaL).get();

    if(v1 == 0) {
      MEMORY.ref(1, s0).offset(0x9L).setu(a0);
      //LAB_800ca250
      return 0;
    }

    MEMORY.ref(1, s0).offset(0x9L).setu(a0);

    if((int)v1 < 0x4L) {
      return 0x1L;
    }

    if((int)v1 >= 0x7L) {
      return 0;
    }

    if(a0 == 0) {
      a0 = MEMORY.ref(2, s0).offset(0x4L).getSigned();

      if((int)a0 >= 0) {
        FUN_800cad64(a0);
      }

      //LAB_800ca240
      MEMORY.ref(2, s0).offset(0x4L).setu(-0x1L);
      MEMORY.ref(2, s0).offset(0x6L).setu(-0x1L);
      MEMORY.ref(1, s0).offset(0xbL).setu(0);
    }

    //LAB_800ca258
    //LAB_800ca25c
    return 0x1L;
  }

  @Method(0x800ca31cL)
  public static TmdAnimationFile FUN_800ca31c(final long a0, final long a1) {
    final BattleStruct1a8_c a0_0 = _8005e398.get((int)a0)._14.get((int)a1);

    return switch(a0_0._0a.get()) {
      case 1, 2 -> MEMORY.ref(4, a0_0._00.get(), TmdAnimationFile::new); //TODO

      case 3 -> {
//        final TmdAnimationFile s0 = a0_0.tmdAnim_00.deref();
//
//        if(a0_0._09.get() == 0 || encounterId_800bb0f8.get() != 0x1bbL) {
          //LAB_800ca3c4
//          FUN_800cadbc(s0);
//        }
//
//        yield FUN_800cad34(s0);
        throw new RuntimeException("This seems wrong - param is definitely an index");
      }

      case 4, 5, 6 -> {
        if(a0_0._0b.get() != 0) {
          final long s0 = a0_0._04.get();

          if((int)s0 >= 0) {
            //LAB_800ca3f4
            yield MEMORY.ref(4, FUN_800cad34(s0), TmdAnimationFile::new); //TODO
          }
        }

        yield null;
      }

      default ->
        //LAB_800ca404
        null;
    };

    //LAB_800ca408
  }

  @Method(0x800ca418L)
  public static void FUN_800ca418(final int index) {
    assert false;
  }

  @Method(0x800ca75cL)
  public static void FUN_800ca75c(final int index, final long timFile) {
    short a0;

    if(index >= 0) {
      //LAB_800ca77c
      final BattleStruct1a8 s0 = getBattleStruct1a8(index);
      a0 = s0._1a0.get();

      if(a0 == 0) {
        final short v0 = s0._19c.get();

        if(v0 < 0) {
          a0 = (short)FUN_800ca89c(s0._1a2.get());
          s0._1a0.set(a0);
        } else {
          a0 = (short)(v0 + 1);

          //LAB_800ca7c4
          s0._1a0.set(a0);
        }
      }
    } else {
      a0 = 0;
    }

    //LAB_800ca7d0
    FUN_800ca7ec(a0, timFile);
  }

  @Method(0x800ca7ecL)
  public static void FUN_800ca7ec(final long a0, final long timFile) {
    final TimHeader sp0x10 = parseTimHeader(MEMORY.ref(4, timFile + 0x4L));

    if(a0 != 0) {
      //LAB_800ca83c
      final RECT s0 = _800fa6e0.get((int)a0);
      LoadImage(s0, sp0x10.getImageAddress());

      if((sp0x10.flags.get() & 0x8L) != 0) {
        sp0x10.clutRect.x.set(s0.x.get());
        sp0x10.clutRect.y.set((short)(s0.y.get() + 240));

        //LAB_800ca884
        LoadImage(sp0x10.clutRect, sp0x10.getClutAddress());
      }
    } else {
      LoadImage(sp0x10.imageRect, sp0x10.getImageAddress());

      if((sp0x10.flags.get() & 0x8L) != 0) {
        LoadImage(sp0x10.clutRect, sp0x10.getClutAddress());
      }
    }

    //LAB_800ca88c
  }

  @Method(0x800ca89cL)
  public static long FUN_800ca89c(final long a0) {
    //LAB_800ca8ac
    //LAB_800ca8c4
    for(int i = a0 < 0x200L ? 4 : 1; i < 9; i++) {
      final long a0_0 = 0x1L << i;

      if((_800c66c4.get() & a0_0) == 0) {
        _800c66c4.oru(a0_0);
        return i;
      }

      //LAB_800ca8e4
    }

    //LAB_800ca8f4
    return 0;
  }

  @Method(0x800ca938L)
  public static long FUN_800ca938(final long a0) {
    long v0;
    long v1;
    v0 = a0 * 0x1a8L;
    v1 = 0x8006_0000L;
    v1 = v1 - 0x1c68L;
    v0 = v0 + v1;
    v1 = 0x8010_0000L;
    v0 = MEMORY.ref(2, v0).offset(0x1a0L).getSigned();
    v1 = v1 - 0x58d0L;
    v0 = v0 << 1;
    v0 = v0 + v1;
    return MEMORY.ref(2, v0).offset(0x0L).getSigned();
  }

  @Method(0x800ca980L)
  public static void FUN_800ca980() {
    //LAB_800ca990
    for(int i = 0; i < 0x200; i++) {
      _8006e918.offset(i * 0x4L).setu(0);
    }

    _800c66c1.setu(0x1L);
  }

  @Method(0x800cad34L)
  public static long FUN_800cad34(final long a0) {
    return _8006e918.offset(4, a0 * 0x8L).get();
  }

  @Method(0x800cad64L)
  public static void FUN_800cad64(long a0) {
    long v0;
    long v1;
    long a1;
    long a2;
    long s0;
    a0 = a0 << 3;
    v0 = 0x8007_0000L;
    v0 = v0 - 0x16e8L;
    s0 = a0 + v0;
    v1 = MEMORY.ref(1, s0).offset(0x4L).get();
    v0 = 0x1L;
    if(v1 != v0) {
      a1 = 0;
      a2 = v0;
      a0 = MEMORY.ref(4, s0).offset(0x0L).get();
      FUN_800127cc(a0, a1, a2);
      MEMORY.ref(4, s0).offset(0x0L).setu(0);
    }

    //LAB_800cada8
    MEMORY.ref(1, s0).offset(0x4L).setu(0);
  }

  @Method(0x800cadbcL)
  public static long FUN_800cadbc(final long a0) {
    final long s1 = _8006e918.offset(a0 * 0x8L).getAddress();
    final long s0 = MEMORY.ref(4, s1).get();

    final long v0 = FUN_80012444(s0, FUN_800128a8(s0));
    if(v0 == 0 || v0 == s0) {
      //LAB_800cae1c
      return -0x1L;
    }

    //LAB_800cae24
    MEMORY.ref(4, s1).setu(v0);

    //LAB_800cae2c
    return a0;
  }

  @Method(0x800cae44L)
  public static void FUN_800cae44() {
    _800c675c.setu(0);
  }

  @Method(0x800cae50L)
  public static void FUN_800cae50(final int index, final ScriptState<BtldScriptData27c> state, final BtldScriptData27c data) {
    data._278.set(0);

    long v1 = _800bc960.get();
    if((state.ui_60.get() & 0x4L) != 0) {
      v1 = v1 & 0x110L;
    } else {
      //LAB_800cae94
      v1 = v1 & 0x210L;
    }

    //LAB_800cae98
    if(v1 != 0) {
      if(FUN_800c90b0(data._26c.get()) != 0) {
        data._1e5.set((int)FUN_800ca938(data._26c.get()));
        data._26e.set((short)0);
        FUN_800c952c(data._148.getAddress(), data._26c.get());
        data._278.set(1);
        data._270.set((short)-1);

        v1 = state.ui_60.get();
        if((v1 & 0x800L) == 0) {
          final ScriptFile script;
          final String scriptName;
          final int scriptLength;
          if((v1 & 0x4L) != 0) {
            script = FUN_800c913c(data._26c.get());

            final Tuple<String, Integer> tuple = _8005e398_SCRIPT_SIZES.get(data._26c.get());
            scriptName = tuple.a();
            scriptLength = tuple.b();
          } else {
            //LAB_800caf18
            script = script_800c66fc.deref();
            scriptName = "S_BTLD BPE 800fb77c";
            scriptLength = script_800c66fc_length;
          }

          //LAB_800caf20
          loadScriptFile(index, script, scriptName, scriptLength);
        }

        //LAB_800caf2c
        setCallback04(index, MEMORY.ref(4, getMethodAddress(Bttl.class, "FUN_800caf2c", int.class, ScriptState.classFor(BtldScriptData27c.class), BtldScriptData27c.class), TriConsumerRef::new));
      }
    }

    //LAB_800caf38
  }

  @Method(0x800cbb00L)
  public static long FUN_800cbb00(final RunningScript t1) {
    final long s0 = t1.params_20.get(0).deref().get();
    final ScriptState<BtldScriptData27c> a0 = scriptStatePtrArr_800bc1c0.get((int)s0).derefAs(ScriptState.classFor(BtldScriptData27c.class));
    BtldScriptData27c v1 = a0.innerStruct_00.derefAs(BtldScriptData27c.class);

    long x = v1._148.coord2_14.coord.transfer.getX();
    long y = v1._148.coord2_14.coord.transfer.getY();
    long z = v1._148.coord2_14.coord.transfer.getZ();

    final long t0 = t1.params_20.get(1).deref().get();
    if((int)t0 >= 0) {
      v1 = scriptStatePtrArr_800bc1c0.get((int)t0).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
      x -= v1._148.coord2_14.coord.transfer.getX();
      y -= v1._148.coord2_14.coord.transfer.getY();
      z -= v1._148.coord2_14.coord.transfer.getZ();
    }

    //LAB_800cbb98
    a0._c8.set(t0);
    FUN_800cdc1c(a0, x, y, z, t1.params_20.get(3).deref().get(), t1.params_20.get(4).deref().get(), t1.params_20.get(5).deref().get(), 0, t1.params_20.get(2).deref().get());
    setCallback10(s0, MEMORY.ref(4, getMethodAddress(Bttl.class, "FUN_800cb250", int.class, ScriptState.classFor(BtldScriptData27c.class), BtldScriptData27c.class), TriFunctionRef::new));
    return 0;
  }

  @Method(0x800caf2cL)
  public static void FUN_800caf2c(final int index, final ScriptState<BtldScriptData27c> state, final BtldScriptData27c data) {
    setCallback08(index, MEMORY.ref(4, getMethodAddress(Bttl.class, "FUN_800cb024", int.class, ScriptState.classFor(BtldScriptData27c.class), BtldScriptData27c.class), TriConsumerRef::new));
    setCallback04(index, MEMORY.ref(4, getMethodAddress(Bttl.class, "FUN_800cafb4", int.class, ScriptState.classFor(BtldScriptData27c.class), BtldScriptData27c.class), TriConsumerRef::new));
    FUN_800cafb4(index, state, data);
  }

  @Method(0x800cafb4L)
  public static void FUN_800cafb4(final int index, final ScriptState<BtldScriptData27c> state, final BtldScriptData27c data) {
    if((state.ui_60.get() & 0x211L) == 0) {
      FUN_800214bc(data._148);
      if((state.ui_60.get() & 0x80L) == 0 || data._1e6.get() != 0) {
        //LAB_800cb004
        FUN_80020b98(data._148);
      }
    }

    //LAB_800cb00c
  }

  @Method(0x800cb024L)
  public static void FUN_800cb024(final int index, final ScriptState<BtldScriptData27c> state, final BtldScriptData27c data) {
    if((state.ui_60.get() & 0x211L) == 0) {
      FUN_800211d8(data._148);
    }

    //LAB_800cb048
  }

  @Method(0x800cb058L)
  public static void FUN_800cb058(final int index, final ScriptState<BtldScriptData27c> state, final BtldScriptData27c data) {
    assert false;
  }

  @Method(0x800cb250L)
  public static void FUN_800cb250(final int index, final ScriptState<BtldScriptData27c> state, final BtldScriptData27c data) {
    assert false;
  }

  @Method(0x800cb468L)
  public static long FUN_800cb468(final RunningScript a0) {
    final BtldScriptData27c v1 = scriptStatePtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
    a0.params_20.get(1).deref().set(v1._148.coord2_14.coord.transfer.getX() & 0xffff_ffffL);
    a0.params_20.get(2).deref().set(v1._148.coord2_14.coord.transfer.getY() & 0xffff_ffffL);
    a0.params_20.get(3).deref().set(v1._148.coord2_14.coord.transfer.getZ() & 0xffff_ffffL);
    return 0;
  }

  @Method(0x800cb534L)
  public static long FUN_800cb534(final RunningScript a0) {
    scriptStatePtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class)._1be.set((int)a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800cb84cL)
  public static long FUN_800cb84c(final RunningScript a0) {
    final ScriptState<BtldScriptData27c> s2 = scriptStatePtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).derefAs(ScriptState.classFor(BtldScriptData27c.class));
    final BtldScriptData27c s0 = s2.innerStruct_00.derefAs(BtldScriptData27c.class);

    if((s2.ui_60.get() & 0x1L) == 0) {
      final long s1 = a0.params_20.get(1).deref().get();
      final long a1 = s0._270.get();

      if((int)a1 >= 0) {
        if(a1 != s1) {
          FUN_800ca194(s0._26c.get(), a1);
        }

        //LAB_800cb8d0
        s0._270.set((short)-1);
      }

      //LAB_800cb8d4
      if(FUN_800ca054(s0._26c.get(), s1) != 0) {
        FUN_800ca194(s0._26c.get(), s0._26e.get());
        FUN_800ca100(s0._148, s0._26c.get(), s1);
        s2.ui_60.and(0xffff_ff6fL);
        s0._1e4.set(1);
        s0._26e.set((short)s1);
        s0._270.set((short)-1);
        return 0;
      }

      //LAB_800cb934
      FUN_800c9e10(s0._26c.get(), s1);
    }

    //LAB_800cb944
    return 0x2L;
  }

  @Method(0x800cb9b0L)
  public static long FUN_800cb9b0(final RunningScript a0) {
    a0.params_20.get(1).deref().set(scriptStatePtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class)._26e.get());
    return 0;
  }

  @Method(0x800cc608L)
  public static long FUN_800cc608(final RunningScript a0) {
    final BtldScriptData27c s0 = scriptStatePtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
    final BtldScriptData27c v0 = scriptStatePtrArr_800bc1c0.get((int)a0.params_20.get(1).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);

    s0._1be.set((int)(ratan2(v0._148.coord2_14.coord.transfer.getX() - s0._148.coord2_14.coord.transfer.getX(), v0._148.coord2_14.coord.transfer.getZ() - s0._148.coord2_14.coord.transfer.getZ()) + 0x800L));
    return 0;
  }

  @Method(0x800cca34L)
  public static long FUN_800cca34(final RunningScript s1) {
    if(_800c675c.get() != s1.params_20.get(0).deref().get() || (s1.scriptState_04.deref().ui_60.get() & 0x1000L) != 0) {
      //LAB_800cca7c
      final long a0;
      final long a1;
      final long a2;
      if(s1.childCount_14.get() == 0x2L) {
        a0 = s1.scriptStateIndex_00.get();
        a1 = s1.params_20.get(0).deref().get();
        a2 = 0;
      } else {
        //LAB_800ccaa0
        a0 = s1.scriptStateIndex_00.get();
        a1 = s1.params_20.get(0).deref().get();
        a2 = s1.params_20.get(1).deref().get();
      }

      //LAB_800ccab4
      FUN_800f6134(a0, a1, a2);

      s1.scriptState_04.deref().ui_60.and(0xffff_efffL);
      _800c675c.setu(s1.params_20.get(0).deref().get());
    }

    //LAB_800ccaec
    FUN_800f8c38(0x1L);
    final long s0 = FUN_800f6330();
    final long v0;
    if(s0 != 0) {
      FUN_800f8c38(0);
      s1.params_20.get(2).deref().set(s0 - 0x1L);
      v0 = 0;
    } else {
      //LAB_800ccb24
      v0 = 0x2L;
    }

    //LAB_800ccb28
    return v0;
  }

  @Method(0x800cccf4L)
  public static long FUN_800cccf4(final RunningScript a0) {
    a0.params_20.get(1).deref().set(scriptStatePtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class)._272.get());
    return 0;
  }

  @Method(0x800ccd34L)
  public static long FUN_800ccd34(final RunningScript a0) {
    long v1 = a0.params_20.get(1).deref().get();
    if(a0.params_20.get(2).deref().get() == 0x2L && (int)v1 < 0) {
      v1 = 0;
    }

    //LAB_800ccd8c
    final BtldScriptData27c a1 = scriptStatePtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
    a1._04.get((int)a0.params_20.get(2).deref().get()).set((short)v1);
    return 0;
  }

  @Method(0x800cce04L)
  public static long FUN_800cce04(final RunningScript a0) {
    final BtldScriptData27c a1 = scriptStatePtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);

    if(a0.params_20.get(1).deref().get() == 0x2L) {
      a0.params_20.get(2).deref().set(a1._08.get());
    } else {
      //LAB_800cce54
      a0.params_20.get(2).deref().set(a1._04.get((int)a0.params_20.get(1).deref().get()).get());
    }

    //LAB_800cce68
    return 0;
  }

  @Method(0x800cce70L)
  public static long FUN_800cce70(final RunningScript a0) {
    a0.params_20.get(2).deref().set(scriptStatePtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class)._04.get((int)a0.params_20.get(1).deref().get()).get());
    return 0;
  }

  @Method(0x800ccec8L)
  public static long FUN_800ccec8(final RunningScript a0) {
    FUN_800f1a00(a0.params_20.get(0).deref().get() > 0 ? 1 : 0);
    return 0;
  }

  @Method(0x800cd998L)
  public static long FUN_800cd998(final RunningScript a0) {
    final BtldScriptData27c v1 = scriptStatePtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);

    if(a0.params_20.get(2).deref().get() != 0) {
      a0.params_20.get(1).deref().set(v1._276.get() & 0xffff_ffffL);
    } else {
      //LAB_800cd9e8
      a0.params_20.get(1).deref().set(v1._274.get() & 0xffff_ffffL);
    }

    //LAB_800cd9f4
    return 0;
  }

  @Method(0x800cdc1cL)
  public static void FUN_800cdc1c(final ScriptState<BtldScriptData27c> s1, final long x, final long y, final long z, final long a4, final long a5, final long a6, final long a7, final long a8) {
    final long v0 = (a4 - x) << 8;
    final long v1 = (a5 - y) << 8;
    final long s0 = (a6 - z) << 8;
    final long s3 = a7 << 8;

    s1._cc.set(a8);
    s1._e8.set(a4);
    s1._ec.set(a5);
    s1._f0.set(a6);
    s1._d0.set(v0);
    s1._d4.set(v1);
    s1._d8.set(s0);
    s1._dc.set((int)v0 / (int)a8);
    s1._e0.set(FUN_80013404(s3, v1, a8));
    s1._e4.set((int)s0 / (int)a8);
    s1._f4.set(s3);
  }

  @Method(0x800d47dcL)
  public static void FUN_800d47dc(long a0, long a1, long a2, long a3, long a4, long a5, long a6) {
    long v0;
    long v1;
    long t0;
    long t1;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long lo;
    s1 = a3;
    s2 = 0x800c_0000L;
    v0 = MEMORY.ref(4, s2).offset(0x67f0L).get();
    a3 = a5;
    s4 = a0;
    s5 = a1;
    s3 = a2;
    s0 = s2 + 0x67f0L;
    v1 = MEMORY.ref(4, s0).offset(0x8L).get();
    a0 = v0 << 8;
    v0 = MEMORY.ref(4, s0).offset(0x4L).get();
    a1 = v1 << 8;
    MEMORY.ref(4, s0).offset(0x94L).setu(a0);
    MEMORY.ref(4, s0).offset(0x9cL).setu(a1);
    a2 = v0 << 8;
    MEMORY.ref(4, s0).offset(0x98L).setu(a2);
    if(a3 == 0) {
      //LAB_800d4854
      a0 = s4 - a0;
      lo = (int)a0 / (int)s1;
      a0 = lo;
      v1 = s5 - a2;

      lo = (int)v1 / (int)s1;
      v1 = lo;
      v0 = s3 - a1;

      lo = (int)v0 / (int)s1;
      v0 = lo;
      MEMORY.ref(4, s0).offset(0xd0L).setu(s1);
      MEMORY.ref(4, s0).offset(0xb0L).setu(a0);
      MEMORY.ref(4, s0).offset(0xbcL).setu(v1);
      MEMORY.ref(4, s0).offset(0xc8L).setu(v0);
    } else assert a3 != 0x1L : "Undefined t0/t1";

    //LAB_800d492c
    v0 = 0x800c_0000L;
    v0 = v0 + 0x67f0L;

    //LAB_800d4934
    v1 = MEMORY.ref(4, v0).offset(0x11cL).get();
    a0 = 0x8L;
    MEMORY.ref(1, v0).offset(0x120L).setu(a0);
    v1 = v1 | 0x1L;
    MEMORY.ref(4, v0).offset(0x11cL).setu(v1);
  }

  @Method(0x800d4bacL)
  public static void FUN_800d4bac(long a0, long a1, long a2, long a3, long a4, long a5, long a6) {
    long v0;
    long v1;
    long s0;
    long s1;
    long s2;
    long s6;
    long s7;
    long lo;
    s6 = a0;
    s7 = a1;
    s2 = a2;
    s1 = a3;
    a0 = 0;
    a1 = 0x4L;
    a2 = a0;
    a3 = a0;
    v0 = FUN_800dc384(a0, a1, a2, a3);
    a0 = 0;
    a1 = 0x4L;
    a2 = 0x1L;
    a3 = a0;
    v1 = 0x800c_0000L;
    s0 = v1 + 0x67f0L;
    v0 = v0 << 8;
    MEMORY.ref(4, s0).offset(0x94L).setu(v0);
    v0 = FUN_800dc384(a0, a1, a2, a3);
    a0 = 0;
    a1 = 0x4L;
    a2 = 0x2L;
    a3 = a0;
    v0 = v0 << 8;
    MEMORY.ref(4, s0).offset(0x98L).setu(v0);
    v0 = FUN_800dc384(a0, a1, a2, a3);
    v1 = v0 << 8;
    MEMORY.ref(4, s0).offset(0x9cL).setu(v1);
    if(a5 == 0) {
      //LAB_800d4c5c
      a0 = MEMORY.ref(4, s0).offset(0x94L).get();

      a0 = s6 - a0;
      lo = (int)a0 / (int)s1;
      a0 = lo;
      v1 = s2 - v1;

      lo = (int)v1 / (int)s1;
      v1 = lo;
      v0 = MEMORY.ref(4, s0).offset(0x98L).get();

      v0 = s7 - v0;
      lo = (int)v0 / (int)s1;
      v0 = lo;
      MEMORY.ref(4, s0).offset(0xd0L).setu(s1);
      MEMORY.ref(4, s0).offset(0xc8L).setu(v1);
      MEMORY.ref(4, s0).offset(0xb0L).setu(a0);
      MEMORY.ref(4, s0).offset(0xbcL).setu(v0);
    } else assert a5 != 0x1L : "Undefined s3/s5";

    //LAB_800d4d34
    v0 = 0x800c_0000L;
    v0 = v0 + 0x67f0L;

    //LAB_800d4d3c
    MEMORY.ref(1, v0).offset(0x120L).setu(0xcL);
    MEMORY.ref(4, v0).offset(0x11cL).oru(0x1L);
  }

  @Method(0x800d5ec8L)
  public static void FUN_800d5ec8(long a0, long a1, long a2, long a3, long a4, long a5, long a6, long a7) {
    long v0;
    long v1;
    long t0;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long s6;
    long s7;
    long fp;
    long lo;
    long sp40;
    long sp38;
    long sp3c;
    v1 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v1).offset(0x67f0L).get();
    sp38 = a0;
    s3 = (int)a0 >> 8;
    s5 = v1 + 0x67f0L;
    s3 = s3 - v0;
    s2 = s3 >>> 31;
    s2 = s3 + s2;
    s2 = (int)s2 >> 1;
    lo = ((long)(int)s2 * (int)s2) & 0xffff_ffffL;
    sp3c = a1;
    sp40 = a2;
    v0 = MEMORY.ref(4, s5).offset(0x4L).get();
    s4 = (int)a1 >> 8;
    s4 = s4 - v0;
    v0 = s4 >>> 31;
    s2 = lo;
    v0 = s4 + v0;
    v0 = (int)v0 >> 1;
    lo = ((long)(int)v0 * (int)v0) & 0xffff_ffffL;
    v0 = MEMORY.ref(4, s5).offset(0x8L).get();
    s1 = (int)a2 >> 8;
    s1 = s1 - v0;
    s0 = s1 >>> 31;
    v1 = lo;
    s0 = s1 + s0;
    s0 = (int)s0 >> 1;
    lo = ((long)(int)s0 * (int)s1) & 0xffff_ffffL;
    fp = a3;
    v1 = s2 + v1;
    a0 = lo;
    v0 = a0 >>> 31;
    a0 = a0 + v0;
    a0 = (int)a0 >> 1;
    a0 = v1 + a0;
    v0 = SquareRoot0(a0);
    a0 = s1;
    a1 = s3;
    v0 = v0 << 9;
    MEMORY.ref(4, s5).offset(0xdcL).setu(v0);
    v0 = ratan2(a0, a1);
    lo = ((long)(int)s0 * (int)s0) & 0xffff_ffffL;
    v0 = v0 & 0xfffL;
    v0 = v0 << 8;
    MEMORY.ref(4, s5).offset(0xd4L).setu(v0);
    v1 = lo;
    a0 = s2 + v1;
    v0 = SquareRoot0(a0);
    a0 = s4;
    a1 = v0 << 1;
    v0 = ratan2(a0, a1);
    v0 = v0 & 0xfffL;
    v0 = v0 << 8;
    MEMORY.ref(4, s5).offset(0xd8L).setu(v0);
    MEMORY.ref(4, s5).offset(0xd0L).setu(fp);
    t0 = a4;

    if(t0 == 0) {
      //LAB_800d5ff0
      v0 = MEMORY.ref(4, s5).offset(0xdcL).get();

      v0 = v0 << 1;
      lo = (int)v0 / (int)fp;
      v0 = lo;
      s6 = a5;
      s7 = v0 - s6;
    } else if(t0 == 0x1L) {
      //LAB_800d6010
      v0 = MEMORY.ref(4, s5).offset(0xdcL).get();

      v0 = v0 << 1;
      lo = (int)v0 / (int)fp;
      v0 = lo;
      s7 = a5;
      s6 = v0 - s7;
    } else {
      throw new RuntimeException("Undefined s6/s7");
    }

    //LAB_800d6030
    v0 = 0x800c_0000L;
    v0 = v0 + 0x67f0L;

    //LAB_800d6038
    v1 = MEMORY.ref(4, v0).offset(0xd0L).get();
    a1 = s7 - s6;
    lo = (int)a1 / (int)v1;
    a1 = lo;
    MEMORY.ref(4, v0).offset(0xa4L).setu(s6);
    t0 = sp38;

    MEMORY.ref(4, v0).offset(0xe8L).setu(t0);
    t0 = sp3c;
    v1 = MEMORY.ref(4, v0).offset(0x11cL).get();
    a0 = 0x10L;
    MEMORY.ref(4, v0).offset(0xecL).setu(t0);
    t0 = sp40;
    v1 = v1 | 0x1L;
    MEMORY.ref(4, v0).offset(0xf0L).setu(t0);
    MEMORY.ref(1, v0).offset(0x120L).setu(a0);
    MEMORY.ref(4, v0).offset(0x11cL).setu(v1);
    MEMORY.ref(4, v0).offset(0xb4L).setu(a1);
  }

  @Method(0x800d6b90L)
  public static void FUN_800d6b90(long a0, long a1, long a2, long a3, long a4, long a5, long a6) {
    long v0;
    long v1;
    long t0;
    long t1;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long lo;
    s3 = a0;
    s4 = a1;
    s2 = a2;
    s1 = a3;
    v0 = 0x800c_0000L;
    s0 = v0 + 0x67f0L;
    v0 = MEMORY.ref(4, s0).offset(0xcL).get();
    a3 = a5;
    v1 = MEMORY.ref(4, s0).offset(0x14L).get();
    a0 = v0 << 8;
    v0 = MEMORY.ref(4, s0).offset(0x10L).get();
    a1 = v1 << 8;
    MEMORY.ref(4, s0).offset(0x20L).setu(a0);
    MEMORY.ref(4, s0).offset(0x28L).setu(a1);
    a2 = v0 << 8;
    if(a3 == 0) {
      MEMORY.ref(4, s0).offset(0x24L).setu(a2);
      //LAB_800d6c04
      a0 = s3 - a0;
      lo = (int)a0 / (int)s1;
      a0 = lo;
      v1 = s4 - a2;

      lo = (int)v1 / (int)s1;
      v1 = lo;
      v0 = s2 - a1;

      lo = (int)v0 / (int)s1;
      v0 = lo;
      MEMORY.ref(4, s0).offset(0x5cL).setu(s1);
      MEMORY.ref(4, s0).offset(0x3cL).setu(a0);
      MEMORY.ref(4, s0).offset(0x48L).setu(v1);
      MEMORY.ref(4, s0).offset(0x54L).setu(v0);
    } else {
      MEMORY.ref(4, s0).offset(0x24L).setu(a2);
      v0 = 0x1L;
      if(a3 == v0) {
        //LAB_800d6c44
        //TODO undefined t0/t1
/*
        v0 = s2 - a1;
        lo = ((long)(int)v0 * (int)v0) & 0xffff_ffffL;
        v1 = lo;
        lo = ((long)(int)t0 * (int)t0) & 0xffff_ffffL;
        t0 = lo;
        lo = ((long)(int)t1 * (int)t1) & 0xffff_ffffL;
        a0 = v1 + t0;
        v0 = lo;
        a0 = a0 + v0;
        v0 = SquareRoot0(a0);
        lo = (int)v0 / (int)s1;
        v0 = lo;
        a1 = MEMORY.ref(4, s0).offset(0xcL).get();
        a1 = a1 << 8;
        a1 = s3 - a1;
        lo = (int)a1 / (int)v0;
        a1 = lo;
        a0 = MEMORY.ref(4, s0).offset(0x10L).get();
        a0 = a0 << 8;
        a0 = s4 - a0;
        lo = (int)a0 / (int)v0;
        a0 = lo;
        v1 = MEMORY.ref(4, s0).offset(0x14L).get();
        v1 = v1 << 8;
        v1 = s2 - v1;
        lo = (int)v1 / (int)v0;
        v1 = lo;
        MEMORY.ref(4, s0).offset(0x5cL).setu(v0);
        MEMORY.ref(4, s0).offset(0x3cL).setu(a1);
        MEMORY.ref(4, s0).offset(0x48L).setu(a0);
        MEMORY.ref(4, s0).offset(0x54L).setu(v1);
*/
      }
    }

    //LAB_800d6cdc
    v0 = 0x800c_0000L;
    v0 = v0 + 0x67f0L;

    //LAB_800d6ce4
    MEMORY.ref(1, v0).offset(0x121L).setu(0x8L);
    MEMORY.ref(4, v0).offset(0x11cL).oru(0x2L);
  }

  @Method(0x800d8274L)
  public static void FUN_800d8274(long a0, long a1, long a2, long a3, long a4, long a5, long a6, long a7) {
    long v0;
    long v1;
    long t0;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long s6;
    long s7;
    long fp;
    long lo;
    long sp4c;
    long sp50;
    long sp54;
    fp = a0;
    s1 = 0x800c_0000L;
    s1 = s1 + 0x67f0L;
    sp4c = a1;
    sp50 = a2;
    sp54 = a3;
    v0 = MEMORY.ref(4, s1).offset(0xcL).get();
    s4 = (int)fp >> 8;
    s4 = s4 - v0;
    s3 = s4 >>> 31;
    s3 = s4 + s3;
    s3 = (int)s3 >> 1;
    lo = ((long)(int)s3 * (int)s3) & 0xffff_ffffL;
    v0 = MEMORY.ref(4, s1).offset(0x10L).get();
    s5 = (int)a1 >> 8;
    s5 = s5 - v0;
    v0 = s5 >>> 31;
    s3 = lo;
    v0 = s5 + v0;
    v0 = (int)v0 >> 1;
    lo = ((long)(int)v0 * (int)v0) & 0xffff_ffffL;
    v0 = MEMORY.ref(4, s1).offset(0x14L).get();
    s2 = (int)a2 >> 8;
    s2 = s2 - v0;
    s0 = s2 >>> 31;
    v1 = lo;
    s0 = s2 + s0;
    s0 = (int)s0 >> 1;
    lo = ((long)(int)s0 * (int)s2) & 0xffff_ffffL;
    s6 = a4;
    s7 = a5;
    v1 = s3 + v1;
    a0 = lo;
    v0 = a0 >>> 31;
    a0 = a0 + v0;
    a0 = (int)a0 >> 1;
    a0 = v1 + a0;
    v0 = SquareRoot0(a0);
    a0 = s2;
    a1 = s4;
    v0 = v0 << 9;
    MEMORY.ref(4, s1).offset(0x68L).setu(v0);
    v0 = ratan2(a0, a1);
    lo = ((long)(int)s0 * (int)s0) & 0xffff_ffffL;
    v0 = v0 & 0xfffL;
    v0 = v0 << 8;
    MEMORY.ref(4, s1).offset(0x60L).setu(v0);
    v1 = lo;
    a0 = s3 + v1;
    v0 = SquareRoot0(a0);
    a0 = s5;
    a1 = v0 << 1;
    v0 = ratan2(a0, a1);
    a0 = s6;
    a1 = s7;
    v0 = v0 & 0xfffL;
    v0 = v0 << 8;
    MEMORY.ref(4, s1).offset(0x64L).setu(v0);
    t0 = sp54;
    MEMORY.ref(4, s1).offset(0x5cL).setu(t0);
    a2 = MEMORY.ref(4, s1).offset(0x68L).get();
    a3 = MEMORY.ref(4, s1).offset(0x5cL).get();
    final Ref<Long> sp0x18 = new Ref<>();
    final Ref<Long> sp0x1c = new Ref<>();
    FUN_800dcebc(a0, a1, a2, a3, sp0x18, sp0x1c);
    a1 = sp0x18.get();
    a0 = sp0x1c.get();
    v0 = MEMORY.ref(4, s1).offset(0x5cL).get();
    a0 = a0 - a1;
    lo = (int)a0 / (int)v0;
    a0 = lo;
    MEMORY.ref(4, s1).offset(0x74L).setu(fp);
    t0 = sp4c;
    v1 = MEMORY.ref(4, s1).offset(0x11cL).get();
    v0 = 0x10L;
    MEMORY.ref(4, s1).offset(0x78L).setu(t0);
    t0 = sp50;
    v1 = v1 | 0x2L;
    MEMORY.ref(4, s1).offset(0x7cL).setu(t0);
    MEMORY.ref(1, s1).offset(0x121L).setu(v0);
    MEMORY.ref(4, s1).offset(0x30L).setu(a1);
    MEMORY.ref(4, s1).offset(0x11cL).setu(v1);
    MEMORY.ref(4, s1).offset(0x40L).setu(a0);
  }

  @Method(0x800d8f10L)
  public static void FUN_800d8f10() {
    long v0;
    long v1;
    long t0;
    long s0;
    v0 = 0x800c_0000L;
    s0 = v0 + 0x67f0L;
    v0 = 0x800c_0000L;
    t0 = v0 + 0x6dd4L;
    long a1 = MEMORY.ref(4, t0).offset(0x0L).get();
    long a2 = MEMORY.ref(4, t0).offset(0x4L).get();
    long sp10 = a1;
    long sp14 = a2;

    if(MEMORY.ref(4, s0).offset(0x11cL).get() != 0) {
      if((MEMORY.ref(4, s0).offset(0x11cL).get() & 0x1L) != 0) {
        v0 = 0x8010_0000L;
        v0 = v0 - 0x5344L;
        v1 = MEMORY.ref(1, s0).offset(0x120L).get() * 0x4L;
        v1 = v1 + v0;
        MEMORY.ref(4, v1).offset(0x0L).deref(4).call();
      }

      //LAB_800d8f80
      if((MEMORY.ref(4, s0).offset(0x11cL).get() & 0x2L) != 0) {
        v0 = 0x8010_0000L;
        v0 = v0 - 0x52e4L;
        v1 = MEMORY.ref(1, s0).offset(0x121L).get() * 0x4L;
        v1 = v1 + v0;
        MEMORY.ref(4, v1).offset(0x0L).deref(4).call();
      }
    }

    //LAB_800d8fb4
    GsSetRefView2(rview2_800c67f0);
    FUN_800daa80();
    FUN_800d8fe0();
  }

  @Method(0x800d8fe0L)
  public static void FUN_800d8fe0() {
    final long s0 = rview2_800c67f0.getAddress();

    if(MEMORY.ref(1, s0).offset(0x118L).get() != 0 && MEMORY.ref(4, s0).offset(0x108L).get() == 0) {
      setProjectionPlaneDistance((int)MEMORY.ref(2, s0).offset(0x102L).getSigned());
      MEMORY.ref(1, s0).offset(0x118L).setu(0);
    }

    //LAB_800d9028
    if(MEMORY.ref(1, s0).offset(0x118L).get() != 0) {
      if(MEMORY.ref(4, s0).offset(0x108L).get() != 0) {
        if(MEMORY.ref(4, s0).offset(0x114L).get() == 0) {
          MEMORY.ref(4, s0).offset(0x100L).addu(MEMORY.ref(4, s0).offset(0x10cL).get());
        } else {
          //LAB_800d906c
          MEMORY.ref(4, s0).offset(0x100L).subu(MEMORY.ref(4, s0).offset(0x10cL).get());
        }

        //LAB_800d907c
        MEMORY.ref(4, s0).offset(0x10cL).addu(MEMORY.ref(4, s0).offset(0x110L).get());
        setProjectionPlaneDistance((int)MEMORY.ref(2, s0).offset(0x102L).getSigned());

        MEMORY.ref(4, s0).offset(0x108L).subu(0x1L);
        if(MEMORY.ref(4, s0).offset(0x108L).get() == 0) {
          MEMORY.ref(1, s0).offset(0x118L).setu(0);
        }
      }
    }

    //LAB_800d90b8
  }

  @Method(0x800d90c8L)
  public static void FUN_800d90c8() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long s0;
    v0 = 0x800c_0000L;
    s0 = v0 + 0x67f0L;
    a0 = MEMORY.ref(4, s0).offset(0x94L).get();
    v0 = MEMORY.ref(4, s0).offset(0xb0L).get();
    a1 = MEMORY.ref(4, s0).offset(0x98L).get();
    a2 = MEMORY.ref(4, s0).offset(0x9cL).get();
    v1 = MEMORY.ref(4, s0).offset(0xc8L).get();
    a0 = a0 + v0;
    v0 = MEMORY.ref(4, s0).offset(0xbcL).get();
    a2 = a2 + v1;
    MEMORY.ref(4, s0).offset(0x94L).setu(a0);
    a0 = (int)a0 >> 8;
    MEMORY.ref(4, s0).offset(0x9cL).setu(a2);
    a2 = (int)a2 >> 8;
    a1 = a1 + v0;
    MEMORY.ref(4, s0).offset(0x98L).setu(a1);
    a1 = (int)a1 >> 8;
    setViewpoint((int)a0, (int)a1, (int)a2);
    v0 = MEMORY.ref(4, s0).offset(0xd0L).get();

    v0 = v0 - 0x1L;
    if((int)v0 > 0) {
      MEMORY.ref(4, s0).offset(0xd0L).setu(v0);
    } else {
      MEMORY.ref(4, s0).offset(0xd0L).setu(v0);
      v0 = MEMORY.ref(4, s0).offset(0x11cL).get();
      v1 = -0x2L;
      MEMORY.ref(1, s0).offset(0x122L).setu(0);
      v0 = v0 & v1;
      MEMORY.ref(4, s0).offset(0x11cL).setu(v0);
    }

    //LAB_800d9144
  }

  @Method(0x800d9220L)
  public static void FUN_800d9220() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long s0;
    v0 = 0x800c_0000L;
    s0 = v0 + 0x67f0L;
    a1 = MEMORY.ref(4, s0).offset(0x94L).get();
    v0 = MEMORY.ref(4, s0).offset(0xb0L).get();
    v1 = MEMORY.ref(4, s0).offset(0x98L).get();
    a2 = MEMORY.ref(4, s0).offset(0x9cL).get();
    a0 = MEMORY.ref(4, s0).offset(0xc8L).get();
    a1 = a1 + v0;
    a2 = a2 + a0;
    MEMORY.ref(4, s0).offset(0x94L).setu(a1);
    a1 = (int)a1 >> 8;
    MEMORY.ref(4, s0).offset(0x9cL).setu(a2);
    a2 = (int)a2 >> 8;
    v0 = MEMORY.ref(4, s0).offset(0xbcL).get();
    a0 = MEMORY.ref(4, s0).offset(0xcL).get();
    v1 = v1 + v0;
    MEMORY.ref(4, s0).offset(0x98L).setu(v1);
    v1 = (int)v1 >> 8;
    a0 = a0 + a1;
    a1 = MEMORY.ref(4, s0).offset(0x10L).get();
    v0 = MEMORY.ref(4, s0).offset(0x14L).get();
    a1 = a1 + v1;
    a2 = v0 + a2;
    setViewpoint((int)a0, (int)a1, (int)a2);
    v0 = MEMORY.ref(4, s0).offset(0xd0L).get();

    v0 = v0 - 0x1L;
    MEMORY.ref(4, s0).offset(0xd0L).setu(v0);
    if((int)v0 <= 0) {
      v0 = 0x4L;
      MEMORY.ref(1, s0).offset(0x122L).setu(0);
      MEMORY.ref(1, s0).offset(0x120L).setu(v0);
    }

    //LAB_800d92ac
  }

  @Method(0x800d9518L)
  public static void FUN_800d9518() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long s0;
    long s1;
    long s2;
    a2 = 0x8010_0000L;
    a0 = _800fab98.getAddress();
    s2 = rview2_800c67f0.getAddress();
    MEMORY.ref(2, a0).offset(0x4L).setu(0);
    v0 = MEMORY.ref(4, s2).offset(0xd4L).get();
    v1 = MEMORY.ref(4, s2).offset(0xd8L).get();
    v0 = (int)v0 >> 8;
    v1 = (int)v1 >> 8;
    MEMORY.ref(2, a2).offset(-0x5468L).setu(v0);
    MEMORY.ref(2, a0).offset(0x2L).setu(v1);
    RotMatrix_8003faf0(_800fab98, _800c6798);
    SetRotMatrix(_800c6798);
    SetTransMatrix(_800c6798);
    v0 = 0x8010_0000L;
    a0 = _800faba0.getAddress();
    s1 = 0x8010_0000L;
    s0 = _800faba8.getAddress();
    MEMORY.ref(2, v0).offset(-0x5460L).setu(0);
    MEMORY.ref(2, a0).offset(0x2L).setu(0);
    v1 = MEMORY.ref(4, s2).offset(0xa4L).get();
    a3 = MEMORY.ref(4, s2).offset(0xb4L).get();
    v0 = MEMORY.ref(4, s2).offset(0xdcL).get();
    v1 = v1 + a3;
    v0 = v0 - v1;
    MEMORY.ref(4, s2).offset(0xdcL).setu(v0);
    v0 = (int)v0 >> 8;
    MEMORY.ref(4, s2).offset(0xa4L).setu(v1);
    MEMORY.ref(2, a0).offset(0x4L).setu(v0);
    FUN_8003f990(_800faba0, _800faba8, _800c67b8);
    v0 = MEMORY.ref(4, s0).offset(0x8L).get();
    a3 = MEMORY.ref(4, s2).offset(0xe8L).get();
    v1 = MEMORY.ref(4, s2).offset(0xecL).get();
    a2 = MEMORY.ref(4, s2).offset(0xf0L).get();
    v0 = v0 << 8;
    a3 = a3 - v0;
    v0 = MEMORY.ref(4, s1).offset(-0x5458L).get();
    a0 = (int)a3 >> 8;
    v0 = v0 << 8;
    v1 = v1 - v0;
    v0 = MEMORY.ref(4, s0).offset(0x4L).get();
    a1 = (int)v1 >> 8;
    MEMORY.ref(4, s2).offset(0x94L).setu(a3);
    MEMORY.ref(4, s2).offset(0x98L).setu(v1);
    v0 = v0 << 8;
    v0 = v0 + a2;
    a2 = (int)v0 >> 8;
    MEMORY.ref(4, s2).offset(0x9cL).setu(v0);
    setViewpoint((int)a0, (int)a1, (int)a2);
    v0 = MEMORY.ref(4, s2).offset(0xd0L).get();

    v0 = v0 - 0x1L;
    MEMORY.ref(4, s2).offset(0xd0L).setu(v0);
    if((int)v0 <= 0) {
      v0 = MEMORY.ref(4, s2).offset(0x11cL).get();
      v1 = -0x2L;
      MEMORY.ref(1, s2).offset(0x122L).setu(0);
      v0 = v0 & v1;
      MEMORY.ref(4, s2).offset(0x11cL).setu(v0);
    }

    //LAB_800d9638
  }

  @Method(0x800d9da0L)
  public static void FUN_800d9da0() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long s0;
    v0 = 0x800c_0000L;
    s0 = v0 + 0x67f0L;
    a0 = MEMORY.ref(4, s0).offset(0x20L).get();
    v0 = MEMORY.ref(4, s0).offset(0x3cL).get();
    a1 = MEMORY.ref(4, s0).offset(0x24L).get();
    a2 = MEMORY.ref(4, s0).offset(0x28L).get();
    v1 = MEMORY.ref(4, s0).offset(0x54L).get();
    a0 = a0 + v0;
    v0 = MEMORY.ref(4, s0).offset(0x48L).get();
    a2 = a2 + v1;
    MEMORY.ref(4, s0).offset(0x20L).setu(a0);
    a0 = (int)a0 >> 8;
    MEMORY.ref(4, s0).offset(0x28L).setu(a2);
    a2 = (int)a2 >> 8;
    a1 = a1 + v0;
    MEMORY.ref(4, s0).offset(0x24L).setu(a1);
    a1 = (int)a1 >> 8;
    setRefpoint((int)a0, (int)a1, (int)a2);
    v0 = MEMORY.ref(4, s0).offset(0x5cL).get();

    v0 = v0 - 0x1L;
    MEMORY.ref(4, s0).offset(0x5cL).setu(v0);
    if((int)v0 <= 0) {
      v0 = MEMORY.ref(4, s0).offset(0x11cL).get();
      v1 = -0x3L;
      MEMORY.ref(1, s0).offset(0x123L).setu(0);
      v0 = v0 & v1;
      MEMORY.ref(4, s0).offset(0x11cL).setu(v0);
    }

    //LAB_800d9e1c
  }

  @Method(0x800da1f0L)
  public static void FUN_800da1f0() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long s0;
    long s1;
    long s2;
    a2 = 0x8010_0000L;
    v0 = 0x800c_0000L;
    s2 = v0 + 0x67f0L;
    _800fab98.setZ((short)0);
    v0 = MEMORY.ref(4, s2).offset(0x60L).get();
    v1 = MEMORY.ref(4, s2).offset(0x64L).get();
    v0 = (int)v0 >> 8;
    v1 = (int)v1 >> 8;
    MEMORY.ref(2, a2).offset(-0x5468L).setu(v0);
    _800fab98.setY((short)v1);
    RotMatrix_8003faf0(_800fab98, _800c6798);
    SetRotMatrix(_800c6798);
    SetTransMatrix(_800c6798);
    v0 = 0x8010_0000L;
    s1 = 0x8010_0000L;
    s0 = _800faba8.getAddress();
    MEMORY.ref(2, v0).offset(-0x5460L).setu(0);
    _800faba0.setY((short)0);
    v1 = MEMORY.ref(4, s2).offset(0x30L).get();
    a3 = MEMORY.ref(4, s2).offset(0x40L).get();
    v0 = MEMORY.ref(4, s2).offset(0x68L).get();
    v1 = v1 + a3;
    v0 = v0 - v1;
    MEMORY.ref(4, s2).offset(0x68L).setu(v0);
    v0 = (int)v0 >> 8;
    MEMORY.ref(4, s2).offset(0x30L).setu(v1);
    _800faba0.setZ((short)v0);
    FUN_8003f990(_800faba0, _800faba8, _800c67b8);
    v0 = MEMORY.ref(4, s0).offset(0x8L).get();
    a3 = MEMORY.ref(4, s2).offset(0x74L).get();
    v1 = MEMORY.ref(4, s2).offset(0x78L).get();
    a2 = MEMORY.ref(4, s2).offset(0x7cL).get();
    v0 = v0 << 8;
    a3 = a3 - v0;
    v0 = MEMORY.ref(4, s1).offset(-0x5458L).get();
    a0 = (int)a3 >> 8;
    v0 = v0 << 8;
    v1 = v1 - v0;
    v0 = MEMORY.ref(4, s0).offset(0x4L).get();
    a1 = (int)v1 >> 8;
    MEMORY.ref(4, s2).offset(0x20L).setu(a3);
    MEMORY.ref(4, s2).offset(0x24L).setu(v1);
    v0 = v0 << 8;
    v0 = v0 + a2;
    a2 = (int)v0 >> 8;
    MEMORY.ref(4, s2).offset(0x28L).setu(v0);
    setRefpoint((int)a0, (int)a1, (int)a2);
    v0 = MEMORY.ref(4, s2).offset(0x5cL).get();

    v0 = v0 - 0x1L;
    MEMORY.ref(4, s2).offset(0x5cL).setu(v0);
    if((int)v0 <= 0) {
      v0 = MEMORY.ref(4, s2).offset(0x11cL).get();
      v1 = -0x3L;
      MEMORY.ref(1, s2).offset(0x123L).setu(0);
      v0 = v0 & v1;
      MEMORY.ref(4, s2).offset(0x11cL).setu(v0);
    }

    //LAB_800da310
  }

  @Method(0x800daa80L)
  public static void FUN_800daa80() {
    if(_800fabb8.get() == 0x1L) { //1b
      if(_800c67d4.get() != 0) {
        _800c67d4.subu(0x1L);
        return;
      }

      //LAB_800daabc
      final long x;
      final long y;
      final long a0 = _800bb0fc.get() & 0x3L;
      if(a0 == 0) {
        //LAB_800dab04
        x = _800c67e4.get();
        y = _800c67e8.get() * 0x2L;
      } else if(a0 == 0x1L) {
        //LAB_800dab1c
        x = -_800c67e4.get() * 0x2L;
        y = -_800c67e8.get();
        //LAB_800daaec
      } else if(a0 == 0x2L) {
        //LAB_800dab3c
        x = _800c67e4.get() * 0x2L;
        y = _800c67e8.get();
      } else {
        //LAB_800dab54
        x = -_800c67e4.get();
        y = -_800c67e8.get() * 0x2L;
      }

      //LAB_800dab70
      //LAB_800dab78
      SetGeomOffset((int)(x_800c67bc.get() + x), (int)(y_800c67c0.get() + y));

      _800c67c4.subu(0x1L);
      if((int)_800c67c4.get() <= 0) {
        _800fabb8.setu(0); //1b
        SetGeomOffset((int)x_800c67bc.get(), (int)y_800c67c0.get());
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
    final long v0 = rview2_800c67f0.getAddress();
    MEMORY.ref(2, v0).offset(0x8cL).setu(0);
    MEMORY.ref(2, v0).offset(0x8eL).setu(0);
    MEMORY.ref(2, v0).offset(0x90L).setu(0);
    MEMORY.ref(4, v0).offset(0x108L).setu(0);
    MEMORY.ref(1, v0).offset(0x118L).setu(0);
    MEMORY.ref(4, v0).offset(0x11cL).setu(0);
    MEMORY.ref(1, v0).offset(0x120L).setu(0);
    MEMORY.ref(1, v0).offset(0x121L).setu(0);
    MEMORY.ref(1, v0).offset(0x122L).setu(0);
    MEMORY.ref(1, v0).offset(0x123L).setu(0);
  }

  @Method(0x800dac20L)
  public static long FUN_800dac20(final RunningScript a0) {
    FUN_800dac70(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get(), a0.params_20.get(4).deref().get());
    return 0;
  }

  @Method(0x800dac70L)
  public static void FUN_800dac70(final long index, final long a1, final long a2, final long a3, final long a4) {
    _800fabbc.offset(index * 0x4L).deref(4).call(a1, a2, a3, a4);
    _800c68ec.setu(index);
  }

  @Method(0x800dacc4L)
  public static void FUN_800dacc4(final long x, final long y, final long z, final long a3) {
    final long s0 = rview2_800c67f0.getAddress();
    MEMORY.ref(4, s0).offset(0x94L).setu(x);
    MEMORY.ref(4, s0).offset(0x98L).setu(y);
    MEMORY.ref(4, s0).offset(0x9cL).setu(z);
    setViewpoint((int)x >> 8, (int)y >> 8, (int)z >> 8);
    MEMORY.ref(1, s0).offset(0x120L).setu(0);
    MEMORY.ref(4, s0).offset(0x11cL).oru(0x1L);
  }

  @Method(0x800dad14L)
  public static void FUN_800dad14(final long x, final long y, final long z, final long a3) {
    final Ref<Long> refX = new Ref<>(x);
    final Ref<Long> refY = new Ref<>(y);
    final Ref<Long> refZ = new Ref<>(z);
    final long s0 = rview2_800c67f0.getAddress();
    MEMORY.ref(4, s0).offset(0xacL).setu(x);
    MEMORY.ref(4, s0).offset(0xb8L).setu(y);
    MEMORY.ref(4, s0).offset(0xa0L).setu(z);
    FUN_800dcc94(0, 0, 0, refX, refY, refZ);
    setViewpoint(refX.get().intValue() >> 8, refY.get().intValue() >> 8, refZ.get().intValue() >> 8);

    MEMORY.ref(1, s0).offset(0x120L).setu(0x1L);
    MEMORY.ref(4, s0).offset(0x11cL).oru(0x1L);
  }

  @Method(0x800db034L)
  public static long FUN_800db034(final RunningScript a0) {
    FUN_800db084(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get(), a0.params_20.get(4).deref().get());
    return 0;
  }

  @Method(0x800db084L)
  public static void FUN_800db084(final long index, final long a1, final long a2, final long a3, final long a4) {
    _800fabdc.offset(index * 0x4L).deref(4).call(a1, a2, a3, a4);
    _800c6878.setu(index);
  }

  @Method(0x800db0d8L)
  public static void FUN_800db0d8(final long x, final long y, final long z, final long a3) {
    final long s0 = rview2_800c67f0.getAddress();
    MEMORY.ref(4, s0).offset(0x20L).setu(x);
    MEMORY.ref(4, s0).offset(0x24L).setu(y);
    MEMORY.ref(4, s0).offset(0x28L).setu(z);
    setRefpoint((int)x >> 8, (int)y >> 8, (int)z >> 8);
    MEMORY.ref(1, s0).offset(0x121L).setu(0);
    MEMORY.ref(4, s0).offset(0x11cL).oru(0x2L);
  }

  @Method(0x800db128L)
  public static void FUN_800db128(final long x, final long y, final long z, final long a3) {
    final long s0 = rview2_800c67f0.getAddress();
    MEMORY.ref(4, s0).offset(0x38L).setu(x);
    MEMORY.ref(4, s0).offset(0x44L).setu(y);
    MEMORY.ref(4, s0).offset(0x2cL).setu(z);
    final Ref<Long> refX = new Ref<>((long)((int)x >> 8));
    final Ref<Long> refY = new Ref<>((long)((int)y >> 8));
    final Ref<Long> refZ = new Ref<>((long)((int)z >> 8));
    FUN_800dcc94(0, 0, 0, refX, refY, refZ);
    setRefpoint(refX.get().intValue(), refY.get().intValue(), refZ.get().intValue());
    MEMORY.ref(1, s0).offset(0x121L).setu(0x1L);
    MEMORY.ref(4, s0).offset(0x11cL).oru(0x2L);
  }

  @Method(0x800db460L)
  public static long FUN_800db460(final RunningScript a0) {
    FUN_800db4ec(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get(), a0.params_20.get(4).deref().get(), a0.params_20.get(5).deref().get(), a0.params_20.get(6).deref().get(), a0.params_20.get(7).deref().get());
    return 0;
  }

  @Method(0x800db4ecL)
  public static void FUN_800db4ec(final long a0, final long a1, final long a2, final long a3, final long a4, final long a5, final long a6, final long a7) {
    _800fabfc.offset(a0 * 0x4L).deref(4).call(a1, a2, a3, a5, a6, a4, a7);
    final long v0 = rview2_800c67f0.getAddress();
    MEMORY.ref(4, v0).offset(0xfcL).setu(a0);
    MEMORY.ref(1, v0).offset(0x122L).setu(0x1L);
  }

  @Method(0x800db574L)
  public static long FUN_800db574(final RunningScript a0) {
    FUN_800db600(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get(), a0.params_20.get(4).deref().get(), a0.params_20.get(5).deref().get(), a0.params_20.get(6).deref().get(), a0.params_20.get(7).deref().get());
    return 0;
  }

  @Method(0x800db600L)
  public static void FUN_800db600(final long a0, final long a1, final long a2, final long a3, final long a4, final long a5, final long a6, final long a7) {
    _800fac5c.offset(a0 * 0x4L).deref(4).call(a1, a2, a3, a5, a6, a4, a7);
    final long v0 = rview2_800c67f0.getAddress();
    MEMORY.ref(4, v0).offset(0x88L).setu(a0);
    MEMORY.ref(1, v0).offset(0x123L).setu(0x1L);
  }

  @Method(0x800db8b0L)
  public static long FUN_800db8b0(final RunningScript a0) {
    FUN_800db950(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get(), a0.params_20.get(4).deref().get(), a0.params_20.get(5).deref().get(), a0.params_20.get(6).deref().get(), a0.params_20.get(7).deref().get(), a0.params_20.get(8).deref().get());
    return 0;
  }

  @Method(0x800db950L)
  public static void FUN_800db950(final long a0, final long a1, final long a2, final long a3, final long a4, final long a5, final long a6, final long a7, final long a8) {
    _800fac3c.offset(a0 * 0x4L).deref(4).call(a1, a2, a3, a4, a5, a6, a7, a8);
    final long v0 = rview2_800c67f0.getAddress();
    MEMORY.ref(4, v0).offset(0xfcL).setu(a0);
    MEMORY.ref(1, v0).offset(0x122L).setu(0x1L);
  }

  @Method(0x800db9e0L)
  public static long FUN_800db9e0(final RunningScript a0) {
    FUN_800dba80(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), a0.params_20.get(2).deref().get(), a0.params_20.get(3).deref().get(), a0.params_20.get(4).deref().get(), a0.params_20.get(5).deref().get(), a0.params_20.get(6).deref().get(), a0.params_20.get(7).deref().get(), a0.params_20.get(8).deref().get());
    return 0;
  }

  @Method(0x800dba80L)
  public static void FUN_800dba80(final long a0, final long a1, final long a2, final long a3, final long a4, final long a5, final long a6, final long a7, final long a8) {
    _800fac9c.offset(a0 * 0x4L).deref(4).call(a1, a2, a3, a4, a5, a6, a7, a8);
    final long v0 = rview2_800c67f0.getAddress();
    MEMORY.ref(4, v0).offset(0x88L).setu(a0);
    MEMORY.ref(1, v0).offset(0x123L).setu(0x1L);
  }

  @Method(0x800dbb10L)
  public static long FUN_800dbb10(final RunningScript a0) {
    final long v1 = a0.params_20.get(0).deref().get();
    final long a1;
    if(v1 == 0) {
      //LAB_800dbb3c
      a1 = _800c6912.get();
    } else if(v1 == 0x1L) {
      //LAB_800dbb48
      a1 = _800c6913.get();
    } else {
      throw new RuntimeException("Undefined a1");
    }

    //LAB_800dbb50
    a0.params_20.get(1).deref().set(a1);
    return 0;
  }

  @Method(0x800dbe60L)
  public static void FUN_800dbe60() {
    final long v0 = rview2_800c67f0.getAddress();
    MEMORY.ref(1, v0).offset(0x122L).setu(0);
    MEMORY.ref(4, v0).offset(0x11cL).and(0xffff_fffeL);
  }

  @Method(0x800dc090L)
  public static void FUN_800dc090() {
    final long v0 = rview2_800c67f0.getAddress();
    MEMORY.ref(1, v0).offset(0x123L).setu(0);
    MEMORY.ref(4, v0).offset(0x11cL).and(0xffff_fffdL);
  }

  @Method(0x800dc0b0L)
  public static void FUN_800dc0b0() {
    final long v0 = rview2_800c67f0.getAddress();
    MEMORY.ref(1, v0).offset(0x123L).setu(0);
    MEMORY.ref(4, v0).offset(0x11cL).and(0xffff_fffdL);
  }

  @Method(0x800dc2d8L)
  public static long FUN_800dc2d8(final RunningScript s0) {
    final long x;
    final long y;
    final long z;
    final long v1;
    if(s0.params_20.get(0).deref().get() == 0) {
      x = rview2_800c67f0.viewpoint_00.getX();
      y = rview2_800c67f0.viewpoint_00.getY();
      z = rview2_800c67f0.viewpoint_00.getZ();
      v1 = _800fad7c.getAddress();
    } else {
      //LAB_800dc32c
      x = rview2_800c67f0.refpoint_0c.getX();
      y = rview2_800c67f0.refpoint_0c.getY();
      z = rview2_800c67f0.refpoint_0c.getZ();
      v1 = _800fad9c.getAddress();
    }

    //LAB_800dc344
    s0.params_20.get(4).deref().set((long)MEMORY.ref(4, v1).offset(s0.params_20.get(1).deref().get() * 0x4L).deref(4).call(s0.params_20.get(2).deref().get(), s0.params_20.get(3).deref().get(), x, y, z) & 0xffff_ffffL);
    return 0;
  }

  @Method(0x800dc384L)
  public static long FUN_800dc384(long a0, long a1, long a2, long a3) {
    long v0;
    long v1 = a2;
    long t0 = a3;
    if(a0 != 0) {
      v0 = 0x800c_0000L;
      v0 = v0 + 0x67f0L;
      a0 = v1;
      a2 = MEMORY.ref(4, v0).offset(0xcL).get();
      a3 = MEMORY.ref(4, v0).offset(0x10L).get();
      v0 = MEMORY.ref(4, v0).offset(0x14L).get();
      v1 = 0x8010_0000L;
      v1 = v1 - 0x5264L;
    } else {
      //LAB_800dc3bc
      v0 = 0x800c_0000L;
      a2 = MEMORY.ref(4, v0).offset(0x67f0L).get();
      v0 = v0 + 0x67f0L;
      a0 = v1;
      a3 = MEMORY.ref(4, v0).offset(0x4L).get();
      v0 = MEMORY.ref(4, v0).offset(0x8L).get();
      v1 = 0x8010_0000L;
      v1 = v1 - 0x5284L;
    }

    //LAB_800dc3dc
    return (long)MEMORY.ref(4, v1).offset(a1 * 0x4L).deref(4).call(a0, t0, a2, a3, v0);
  }

  @Method(0x800dc408L)
  public static long FUN_800dc408(final long a0, final long a1, final long a2, final long a3, final long a4) {
    if(a0 == 0) {
      //LAB_800dc440
      return a2;
    }

    if(a0 == 0x1L) {
      //LAB_800dc448
      return a3;
    }

    //LAB_800dc42c
    if(a0 == 0x2L) {
      //LAB_800dc450
      return a4;
    }

    if(a0 > 0x2L) {
      return 0x2L;
    }

    return 0x1L;
  }

  @Method(0x800dc45cL)
  public static long FUN_800dc45c(final long a0, final long a1, final long x, final long y, final long z) {
    final Ref<Long> refX = new Ref<>(x);
    final Ref<Long> refY = new Ref<>(y);
    final Ref<Long> refZ = new Ref<>(z);
    FUN_800dcd9c(0, 0, 0, refX, refY, refZ);

    if(a0 == 0) {
      //LAB_800dc4d8
      return refX.get();
    }

    if(a0 == 0x1L) {
      //LAB_800dc4e4
      return refY.get();
    }

    //LAB_800dc4c4
    if(a0 == 0x2L) {
      //LAB_800dc4f0
      return refZ.get();
    }

    if(a0 > 0x2L) {
      return 0x2L;
    }

    //LAB_800dc4f4
    return 0x1L;
  }

  @Method(0x800dc514L)
  public static long FUN_800dc514(final long a0, final long a1, final long x, final long y, final long z) {
    if(a0 == 0) {
      //LAB_800dc550
      return x - rview2_800c67f0.refpoint_0c.getX();
    }

    if(a0 == 0x1L) {
      //LAB_800dc560
      return y - rview2_800c67f0.refpoint_0c.getY();
    }

    //LAB_800dc53c
    if(a0 == 0x2L) {
      //LAB_800dc56c
      return z - rview2_800c67f0.refpoint_0c.getZ();
    }

    if(a0 > 0x2L) {
      return 0x2L;
    }

    return 0x1L;
  }

  @Method(0x800dc580L)
  public static long FUN_800dc580(final long a0, final long a1, final long x, final long y, final long z) {
    final Ref<Long> refX = new Ref<>(x);
    final Ref<Long> refY = new Ref<>(y);
    final Ref<Long> refZ = new Ref<>(z);
    FUN_800dcd9c(rview2_800c67f0.refpoint_0c.getX(), rview2_800c67f0.refpoint_0c.getY(), rview2_800c67f0.refpoint_0c.getZ(), refX, refY, refZ);

    if(a0 == 0) {
      //LAB_800dc604
      return refX.get();
    }

    if(a0 == 0x1L) {
      //LAB_800dc610
      return refY.get();
    }

    if(a0 == 0x2L) {
      //LAB_800dc61c
      return refZ.get();
    }

    if(a0 > 0x2L) {
      //LAB_800dc5f0
      return 0x2L;
    }

    //LAB_800dc620
    return 0x1L;
  }

  @Method(0x800dc798L)
  public static long FUN_800dc798(final long a0, final long a1, final long a2, final long a3, final long a4) {
    if(a0 == 0) {
      //LAB_800dc7d0
      return a2;
    }

    if(a0 == 0x1L) {
      //LAB_800dc7d8
      return a3;
    }

    //LAB_800dc7bc
    if(a0 == 0x2L) {
      //LAB_800dc7e0
      return a4;
    }

    if(a0 > 0x2L) {
      return 0x2L;
    }

    return 0x1L;
  }

  @Method(0x800dc7ecL)
  public static long FUN_800dc7ec(final long a0, final long a1, final long a2, final long a3, final long a4) {
    final Ref<Long> sp0x18 = new Ref<>(a2);
    final Ref<Long> sp0x1c = new Ref<>(a3);
    final Ref<Long> sp0x20 = new Ref<>(a4);
    FUN_800dcd9c(0, 0, 0, sp0x18, sp0x1c, sp0x20);

    if(a0 == 0) {
      //LAB_800dc868
      return sp0x18.get();
    }

    if(a0 == 0x1L) {
      //LAB_800dc874
      return sp0x1c.get();
    }

    if(a0 == 0x2L) {
      //LAB_800dc880
      return sp0x20.get();
    }

    if(a0 > 0x2L) {
      //LAB_800dc854
      return 0x2L;
    }

    //LAB_800dc884
    return 0x1L;
  }

  @Method(0x800dcc64L)
  public static void setViewpoint(final int x, final int y, final int z) {
    rview2_800c67f0.viewpoint_00.setX(x);
    rview2_800c67f0.viewpoint_00.setY(y);
    rview2_800c67f0.viewpoint_00.setZ(z);
  }

  @Method(0x800dcc7cL)
  public static void setRefpoint(final int x, final int y, final int z) {
    rview2_800c67f0.refpoint_0c.setX(x);
    rview2_800c67f0.refpoint_0c.setY(y);
    rview2_800c67f0.refpoint_0c.setZ(z);
  }

  @Method(0x800dcc94L)
  public static void FUN_800dcc94(final long a0, final long a1, final long a2, final Ref<Long> x, final Ref<Long> y, final Ref<Long> z) {
    _800fab98.set(x.get().shortValue(), y.get().shortValue(), (short)0);
    RotMatrix_8003faf0(_800fab98, _800c6798);
    SetRotMatrix(_800c6798);
    SetTransMatrix(_800c6798);
    _800faba0.set((short)0, (short)0, z.get().shortValue());
    FUN_8003f990(_800faba0, _800faba8, _800c67b8);
    x.set(a0 - _800faba8.getZ());
    y.set(a1 - _800faba8.getX());
    z.set(_800faba8.getY() + a2);
  }

  @Method(0x800dcd9cL)
  public static void FUN_800dcd9c(final long a0, final long a1, final long a2, final Ref<Long> x, final Ref<Long> y, final Ref<Long> z) {
    final long dx = a0 - x.get();
    final long dy = a1 - y.get();
    final long dz = a2 - z.get();

    long s1 = (int)dx / 2;
    s1 = s1 * s1;

    final long halfDx = (int)dy / 2;
    final long halfDy = (int)dz / 2;

    x.set(ratan2(dz, dx) & 0xfffL);
    y.set(ratan2(dy, SquareRoot0(s1 + halfDy * halfDy) * 0x2L) & 0xfffL);
    z.set(SquareRoot0(s1 + halfDx * halfDx + (int)(halfDy * dz) / 2) * 0x2L);
  }

  @Method(0x800dcebcL)
  public static void FUN_800dcebc(long a0, long a1, long a2, long a3, final Ref<Long> a4, final Ref<Long> a5) {
    long v0;
    long lo;
    if(a0 == 0) {
      //LAB_800dcedc
      v0 = a2 << 1;
      lo = (int)v0 / (int)a3;
      v0 = lo;
      a4.set(a1);
      v0 = v0 - a1;
      a5.set(v0);
      return;
    }
    v0 = 0x1L;
    if(a0 != v0) {
      return;
    }

    //LAB_800dcef8
    v0 = a2 << 1;
    lo = (int)v0 / (int)a3;
    v0 = lo;
    a5.set(a1);
    v0 = v0 - a1;
    a4.set(v0);
  }

  @Method(0x800dd0d4L)
  public static long FUN_800dd0d4() {
    return (long)_800fad90.deref(4).call(1, 0, rview2_800c67f0.viewpoint_00.getX(), rview2_800c67f0.viewpoint_00.getY(), rview2_800c67f0.viewpoint_00.getZ());
  }

  @Method(0x800dd118L)
  public static long FUN_800dd118() {
    return (long)_800fad90.deref(4).call(0, 0, rview2_800c67f0.viewpoint_00.getX(), rview2_800c67f0.viewpoint_00.getY(), rview2_800c67f0.viewpoint_00.getZ());
  }

  @Method(0x800de76cL)
  public static long FUN_800de76c(final long a0, final int objIndex) {
    if((MEMORY.ref(4, a0).offset(0x4L).get() & 0x2L) == 0) {
      final Memory.TemporaryReservation tmp = MEMORY.temp(0x10);
      final GsDOBJ2 dobj2 = tmp.get().cast(GsDOBJ2::new);
      updateTmdPacketIlen(MEMORY.ref(4, a0 + 0xcL, UnboundedArrayRef.of(0x1c, TmdObjTable::new)), dobj2, objIndex); //TODO
      final long tmd = dobj2.tmd_08.getPointer();
      tmp.release();
      return tmd;
    }

    //LAB_800de7a0
    //LAB_800de7b4
    return a0 + objIndex * 0x1cL + 0xcL;
  }

  @Method(0x800e4674L)
  public static VECTOR FUN_800e4674(final VECTOR a0, final SVECTOR a1) {
    final MATRIX sp0x10 = new MATRIX();
    RotMatrix_80040010(a1, sp0x10);
    SetRotMatrix(sp0x10);
    final SVECTOR sp0x30 = new SVECTOR().set((short)0, (short)0, (short)0x1000);
    FUN_8003ef50(sp0x30, a0);
    return a0;
  }

  @Method(0x800e46c8L)
  public static void FUN_800e46c8() {
    long a2 = 0x41L;
    long v0 = 0x800c_0000L;
    long a1 = 0x800c_0000L;
    long v1 = MEMORY.ref(4, v0).offset(0x6930L).get();
    long a0 = MEMORY.ref(4, a1).offset(0x692cL).get();
    MEMORY.ref(4, v1).offset(0x8L).setu(0x800L);
    MEMORY.ref(4, v1).offset(0x4L).setu(0x800L);
    MEMORY.ref(4, v1).offset(0x0L).setu(0x800L);
    MEMORY.ref(1, a0).offset(0xeL).setu(0x80L);
    MEMORY.ref(1, a0).offset(0xdL).setu(0x80L);
    MEMORY.ref(1, a0).offset(0xcL).setu(0x80L);
    v1 = MEMORY.ref(4, a1).offset(0x692cL).get();
    MEMORY.ref(4, a0).offset(0x0L).setu(0);
    MEMORY.ref(4, a0).offset(0x4L).setu(0x1000L);
    MEMORY.ref(4, a0).offset(0x8L).setu(0);
    MEMORY.ref(4, v1).offset(0x10L).setu(0);
    MEMORY.ref(4, v1).offset(0x4cL).setu(0);

    //LAB_800e4720
    a0 = v1 + 0x84L;
    do {
      MEMORY.ref(4, a0).offset(0x0L).setu(0);
      a0 = a0 + 0x4L;
      v0 = a2;
      a2 = a2 - 0x1L;
    } while((int)v0 > 0);
  }

  @Method(0x800e4cf8L)
  public static void FUN_800e4cf8(long r, long g, long b) {
    long v0;
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x6930L).get();
    MEMORY.ref(4, v0).offset(0x0L).setu(r);
    MEMORY.ref(4, v0).offset(0x4L).setu(g);
    MEMORY.ref(4, v0).offset(0x8L).setu(b);
    MEMORY.ref(4, v0).offset(0x24L).setu(0);
    GsSetAmbient(r, g, b);
  }

  @Method(0x800e5768L)
  public static void FUN_800e5768(long a0) {
    long v0;
    long v1;
    long a1;
    long a2;
    long a3;
    long t0;
    long t1;
    long t2;
    long t3;
    long s0;
    s0 = a0;
    a0 = MEMORY.ref(2, s0).offset(0x0L).get();
    a1 = MEMORY.ref(2, s0).offset(0x2L).get();
    a2 = MEMORY.ref(2, s0).offset(0x4L).get();
    FUN_800e4cf8(a0, a1, a2);

    v0 = MEMORY.ref(2, s0).offset(0xeL).getSigned();

    if((int)v0 > 0) {
      v0 = 0x800c_0000L;
      v1 = MEMORY.ref(4, v0).offset(0x6930L).get();
      v0 = 0x3L;
      MEMORY.ref(4, v1).offset(0x24L).setu(v0);
      v0 = MEMORY.ref(2, s0).offset(0xcL).get();

      MEMORY.ref(2, v1).offset(0x2cL).setu(v0);
      v0 = MEMORY.ref(2, s0).offset(0xeL).get();

      MEMORY.ref(2, v1).offset(0x2eL).setu(v0);
      v0 = MEMORY.ref(2, s0).offset(0x0L).get();

      MEMORY.ref(4, v1).offset(0xcL).setu(v0);
      v0 = MEMORY.ref(2, s0).offset(0x2L).get();

      MEMORY.ref(4, v1).offset(0x10L).setu(v0);
      v0 = MEMORY.ref(2, s0).offset(0x4L).get();

      MEMORY.ref(4, v1).offset(0x14L).setu(v0);
      v0 = MEMORY.ref(2, s0).offset(0x6L).get();

      MEMORY.ref(4, v1).offset(0x18L).setu(v0);
      v0 = MEMORY.ref(2, s0).offset(0x8L).get();

      MEMORY.ref(4, v1).offset(0x1cL).setu(v0);
      v0 = MEMORY.ref(2, s0).offset(0xaL).get();
      MEMORY.ref(4, v1).offset(0x20L).setu(v0);
    } else {
      v0 = 0x800c_0000L;

      //LAB_800e5808
      v0 = MEMORY.ref(4, v0).offset(0x6930L).get();

      MEMORY.ref(4, v0).offset(0x24L).setu(0);
    }

    //LAB_800e5814
    t1 = 0;
    t3 = 0x800c_0000L;
    t2 = 0x3L;
    a0 = s0;
    t0 = t1;

    //LAB_800e5828
    do {
      v0 = MEMORY.ref(4, t3).offset(0x692cL).get();
      v1 = MEMORY.ref(2, a0).offset(0x10L).getSigned();
      a1 = v0 + t0;
      MEMORY.ref(4, a1).offset(0x0L).setu(v1);
      v0 = MEMORY.ref(2, a0).offset(0x12L).getSigned();

      MEMORY.ref(4, a1).offset(0x4L).setu(v0);
      v0 = MEMORY.ref(2, a0).offset(0x14L).getSigned();

      MEMORY.ref(4, a1).offset(0x8L).setu(v0);
      v0 = MEMORY.ref(1, a0).offset(0x1aL).get();

      MEMORY.ref(1, a1).offset(0xcL).setu(v0);
      v0 = MEMORY.ref(1, a0).offset(0x1bL).get();

      MEMORY.ref(1, a1).offset(0xdL).setu(v0);
      v0 = MEMORY.ref(1, a0).offset(0x1cL).get();
      a3 = a1 + 0x10L;
      MEMORY.ref(1, a1).offset(0xeL).setu(v0);
      v0 = MEMORY.ref(2, a0).offset(0x16L).get();
      v1 = MEMORY.ref(2, a0).offset(0x18L).get();

      v0 = v0 | v1;
      a2 = a1 + 0x4cL;
      if(v0 != 0) {
        v0 = MEMORY.ref(4, a1).offset(0x0L).get();
        MEMORY.ref(4, a1).offset(0x10L).setu(t2);
        MEMORY.ref(4, a3).offset(0x4L).setu(v0);
        v0 = MEMORY.ref(4, a1).offset(0x4L).get();

        MEMORY.ref(4, a3).offset(0x8L).setu(v0);
        v0 = MEMORY.ref(4, a1).offset(0x8L).get();

        MEMORY.ref(4, a3).offset(0xcL).setu(v0);
        v0 = MEMORY.ref(2, a0).offset(0x16L).getSigned();

        MEMORY.ref(4, a3).offset(0x10L).setu(v0);
        v0 = MEMORY.ref(2, a0).offset(0x18L).getSigned();
        MEMORY.ref(4, a3).offset(0x18L).setu(0);
        MEMORY.ref(4, a3).offset(0x14L).setu(v0);
      } else {
        //LAB_800e58cc
        MEMORY.ref(4, a1).offset(0x10L).setu(0);
      }

      //LAB_800e58d0
      v0 = MEMORY.ref(2, a0).offset(0x22L).getSigned();

      if(v0 != 0) {
        MEMORY.ref(4, a2).offset(0x0L).setu(t2);
        v0 = MEMORY.ref(1, a1).offset(0xcL).get();

        MEMORY.ref(4, a2).offset(0x4L).setu(v0);
        v0 = MEMORY.ref(1, a1).offset(0xdL).get();

        MEMORY.ref(4, a2).offset(0x8L).setu(v0);
        v0 = MEMORY.ref(1, a1).offset(0xeL).get();

        MEMORY.ref(4, a2).offset(0xcL).setu(v0);
        v0 = MEMORY.ref(1, a0).offset(0x1dL).get();

        MEMORY.ref(4, a2).offset(0x10L).setu(v0);
        v0 = MEMORY.ref(1, a0).offset(0x1eL).get();

        MEMORY.ref(4, a2).offset(0x14L).setu(v0);
        v0 = MEMORY.ref(1, a0).offset(0x1fL).get();

        MEMORY.ref(4, a2).offset(0x18L).setu(v0);
        v0 = MEMORY.ref(2, a0).offset(0x20L).getSigned();

        MEMORY.ref(4, a2).offset(0x28L).setu(v0);
        v0 = MEMORY.ref(2, a0).offset(0x22L).getSigned();
        MEMORY.ref(4, a2).offset(0x2cL).setu(v0);
      } else {
        //LAB_800e5944
        MEMORY.ref(4, a2).offset(0x0L).setu(0);
      }

      //LAB_800e5948
      a0 = a0 + 0x14L;
      t1 = t1 + 0x1L;
      t0 = t0 + 0x84L;
    } while((int)t1 < 0x3L);
  }

  @Method(0x800e5a78L)
  public static void FUN_800e5a78(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c struct) {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long t0;
    long t1;
    long t2;
    long s0;
    long s3;
    long hi;
    long lo;
    a0 = _800c6930.get();
    _800c6928.addu(0x1L);
    a1 = _800c6928.get();
    if(MEMORY.ref(4, a0).offset(0x24L).get() == 0x3L) {
      v1 = MEMORY.ref(2, a0).offset(0x2eL).getSigned();
      v0 = a1 + MEMORY.ref(2, a0).offset(0x2cL).getSigned();
      a0 = (v0 & 0xffff_ffffL) % (v1 & 0xffff_ffffL);

      a0 = a0 << 12;
      lo = (int)a0 / (int)v1;
      a0 = lo;

      v0 = rcos(a0);

      a1 = _800c6930.get();
      v1 = MEMORY.ref(4, a1).offset(0xcL).get();
      a3 = v0 + 0x1000L;
      lo = (long)(int)v1 * (int)a3 & 0xffff_ffffL;
      a0 = MEMORY.ref(4, a1).offset(0x18L).get();
      t0 = lo;
      a2 = 0x1000L - v0;
      lo = (long)(int)a0 * (int)a2 & 0xffff_ffffL;
      t2 = lo;
      v1 = t0 + t2;
      if((int)v1 < 0) {
        v1 = v1 + 0x1fffL;
      }

      //LAB_800e5b20
      v0 = MEMORY.ref(4, a1).offset(0x10L).get();

      lo = (long)(int)v0 * (int)a3 & 0xffff_ffffL;
      a0 = lo;
      v0 = MEMORY.ref(4, a1).offset(0x1cL).get();

      lo = (long)(int)v0 * (int)a2 & 0xffff_ffffL;
      v0 = (int)v1 >> 13;
      t0 = lo;
      a0 = a0 + t0;
      MEMORY.ref(4, a1).offset(0x0L).setu(v0);
      if((int)a0 < 0) {
        a0 = a0 + 0x1fffL;
      }

      //LAB_800e5b54
      v0 = MEMORY.ref(4, a1).offset(0x14L).get();

      lo = (long)(int)v0 * (int)a3 & 0xffff_ffffL;
      v1 = lo;
      v0 = MEMORY.ref(4, a1).offset(0x20L).get();

      lo = (long)(int)v0 * (int)a2 & 0xffff_ffffL;
      v0 = (int)a0 >> 13;
      MEMORY.ref(4, a1).offset(0x4L).setu(v0);
      a2 = lo;
      v0 = v1 + a2;
      if((int)v0 < 0) {
        v0 = v0 + 0x1fffL;
      }

      //LAB_800e5b8c
      v0 = (int)v0 >> 13;
      MEMORY.ref(4, a1).offset(0x8L).setu(v0);
    }

    //LAB_800e5b98
    long s1 = 0;

    //LAB_800e5ba0
    for(s3 = 0; s3 < 3; s3++) {
      a3 = _800c692c.get() + s1;
      a2 = a3 + 0x10L;
      v1 = MEMORY.ref(1, a3).offset(0x10L).get();
      if(v1 == 0x1L) {
        //LAB_800e5c50
        MEMORY.ref(4, a2).offset(0x10L).addu(MEMORY.ref(4, a2).offset(0x1cL).get());
        MEMORY.ref(4, a2).offset(0x14L).addu(MEMORY.ref(4, a2).offset(0x20L).get());
        MEMORY.ref(4, a2).offset(0x18L).addu(MEMORY.ref(4, a2).offset(0x24L).get());
        MEMORY.ref(4, a2).offset(0x04L).addu(MEMORY.ref(4, a2).offset(0x10L).get());
        MEMORY.ref(4, a2).offset(0x08L).addu(MEMORY.ref(4, a2).offset(0x14L).get());
        MEMORY.ref(4, a2).offset(0x0cL).addu(MEMORY.ref(4, a2).offset(0x18L).get());

        if((MEMORY.ref(4, a3).offset(0x10L).get() & 0x8000L) != 0) {
          MEMORY.ref(4, a2).offset(0x34L).subu(0x1L);

          if((int)MEMORY.ref(4, a2).offset(0x34L).get() <= 0) {
            MEMORY.ref(4, a3).offset(0x10L).setu(0);
            MEMORY.ref(4, a2).offset(0x4L).setu(MEMORY.ref(4, a2).offset(0x28L).get());
            MEMORY.ref(4, a2).offset(0x8L).setu(MEMORY.ref(4, a2).offset(0x2cL).get());
            MEMORY.ref(4, a2).offset(0xcL).setu(MEMORY.ref(4, a2).offset(0x30L).get());
          }
        }

        //LAB_800e5cf4
        v1 = MEMORY.ref(4, a2).offset(0x0L).get();

        if((v1 & 0x2000L) != 0) {
          MEMORY.ref(4, a3).offset(0x0L).setu((int)MEMORY.ref(4, a2).offset(0x4L).get() >> 12);
          MEMORY.ref(4, a3).offset(0x4L).setu((int)MEMORY.ref(4, a2).offset(0x8L).get() >> 12);
          MEMORY.ref(4, a3).offset(0x8L).setu((int)MEMORY.ref(4, a2).offset(0xcL).get() >> 12);
          //LAB_800e5d40
        } else if((v1 & 0x4000L) != 0) {
          final SVECTOR sp0x18 = new SVECTOR();
          sp0x18.setX((short)MEMORY.ref(2, a2).offset(0x4L).get());
          sp0x18.setY((short)MEMORY.ref(2, a2).offset(0x8L).get());
          sp0x18.setZ((short)MEMORY.ref(2, a2).offset(0xcL).get());
          FUN_800e4674(MEMORY.ref(4, a3, VECTOR::new), sp0x18); //TODO
        }
      } else if(v1 == 0x2L) {
        //LAB_800e5bf0
        v0 = scriptStatePtrArr_800bc1c0.get((int)MEMORY.ref(4, a2).offset(0x38L).get()).deref().getAddress(); //TODO
        a1 = MEMORY.ref(4, v0).offset(0x0L).get() + 0x1bcL;

        final SVECTOR sp0x10 = new SVECTOR();
        sp0x10.setX((short)(MEMORY.ref(2, a1).offset(0x0L).get() + MEMORY.ref(2, a2).offset(0x4L).get()));
        sp0x10.setY((short)(MEMORY.ref(2, a1).offset(0x2L).get() + MEMORY.ref(2, a2).offset(0x8L).get()));
        sp0x10.setZ((short)(MEMORY.ref(2, a1).offset(0x4L).get() + MEMORY.ref(2, a2).offset(0xcL).get()));
        FUN_800e4674(MEMORY.ref(4, a3, VECTOR::new), sp0x10); //TODO
      } else if(v1 == 0x3L) {
        //LAB_800e5bdc
        //LAB_800e5d6c
        final SVECTOR sp0x18 = new SVECTOR();

        v1 = _800c6928.get() & 0xfffL;
        v0 = MEMORY.ref(4, a2).offset(0x10L).get();
        t1 = (long)(int)v0 * (int)v1 & 0xffff_ffffL;
        sp0x18.setX((short)(MEMORY.ref(2, a2).offset(0x4L).get() + t1));

        v0 = MEMORY.ref(4, a2).offset(0x14L).get();
        t1 = (long)(int)v0 * (int)v1 & 0xffff_ffffL;
        sp0x18.setY((short)(MEMORY.ref(2, a2).offset(0x8L).get() + t1));

        v0 = MEMORY.ref(4, a2).offset(0x18L).get();
        t1 = (long)(int)v0 * (int)v1 & 0xffff_ffffL;

        //LAB_800e5dc8
        sp0x18.setZ((short)(MEMORY.ref(2, a2).offset(0xcL).get() + t1));

        //LAB_800e5dcc
        FUN_800e4674(MEMORY.ref(4, a3, VECTOR::new), sp0x18); //TODO
      }

      //LAB_800e5dd4
      s0 = a3 + 0x4cL;
      v1 = MEMORY.ref(1, s0).offset(0x0L).get();
      if(v1 == 0x1L) {
        //LAB_800e5df4
        v0 = MEMORY.ref(4, s0).offset(0x10L).get();
        v1 = MEMORY.ref(4, s0).offset(0x1cL).get();
        a0 = MEMORY.ref(4, s0).offset(0x20L).get();
        a1 = MEMORY.ref(4, s0).offset(0x24L).get();
        v0 = v0 + v1;
        MEMORY.ref(4, s0).offset(0x10L).setu(v0);
        v0 = MEMORY.ref(4, s0).offset(0x14L).get();
        v1 = MEMORY.ref(4, s0).offset(0x18L).get();
        v0 = v0 + a0;
        MEMORY.ref(4, s0).offset(0x14L).setu(v0);
        v0 = MEMORY.ref(4, s0).offset(0x4L).get();
        a0 = MEMORY.ref(4, s0).offset(0x10L).get();
        v1 = v1 + a1;
        MEMORY.ref(4, s0).offset(0x18L).setu(v1);
        v1 = MEMORY.ref(4, s0).offset(0x8L).get();
        a1 = MEMORY.ref(4, s0).offset(0x14L).get();
        v0 = v0 + a0;
        MEMORY.ref(4, s0).offset(0x4L).setu(v0);
        v0 = MEMORY.ref(4, s0).offset(0xcL).get();
        a0 = MEMORY.ref(4, s0).offset(0x18L).get();
        v1 = v1 + a1;
        MEMORY.ref(4, s0).offset(0x8L).setu(v1);
        v1 = MEMORY.ref(4, s0).offset(0x0L).get();
        v0 = v0 + a0;
        v1 = v1 & 0x8000L;
        MEMORY.ref(4, s0).offset(0xcL).setu(v0);
        if(v1 != 0) {
          v0 = MEMORY.ref(4, s0).offset(0x34L).get();

          v0 = v0 - 0x1L;
          MEMORY.ref(4, s0).offset(0x34L).setu(v0);
          if((int)v0 <= 0) {
            v0 = MEMORY.ref(4, s0).offset(0x28L).get();
            v1 = MEMORY.ref(4, s0).offset(0x2cL).get();
            a0 = MEMORY.ref(4, s0).offset(0x30L).get();
            MEMORY.ref(4, s0).offset(0x0L).setu(0);
            MEMORY.ref(4, s0).offset(0x4L).setu(v0);
            MEMORY.ref(4, s0).offset(0x8L).setu(v1);
            MEMORY.ref(4, s0).offset(0xcL).setu(a0);
          }
        }

        //LAB_800e5e90
        v1 = _800c692c.get();
        v0 = MEMORY.ref(4, s0).offset(0x4L).get();
        v1 = s1 + v1;
        v0 = (int)v0 >> 12;
        MEMORY.ref(1, v1).offset(0xcL).setu(v0);
        v1 = _800c692c.get();
        v0 = MEMORY.ref(4, s0).offset(0x8L).get();
        v1 = s1 + v1;
        v0 = (int)v0 >> 12;
        MEMORY.ref(1, v1).offset(0xdL).setu(v0);
        v1 = _800c692c.get();
        v0 = MEMORY.ref(4, s0).offset(0xcL).get();
        v1 = s1 + v1;
        v0 = (int)v0 >> 12;
        MEMORY.ref(1, v1).offset(0xeL).setu(v0);
      } else {
        v0 = 0x3L;
        if(v1 == v0) {
          v0 = 0x800c_0000L;

          //LAB_800e5ed0
          a0 = MEMORY.ref(4, s0).offset(0x28L).get();
          v0 = MEMORY.ref(4, v0).offset(0x6928L).get();
          v1 = MEMORY.ref(4, s0).offset(0x2cL).get();
          v0 = v0 + a0;
          hi = (v0 & 0xffff_ffffL) % (v1 & 0xffff_ffffL);
          a0 = hi;

          a0 = a0 << 12;
          lo = (int)a0 / (int)v1;
          a0 = lo;

          v0 = rcos(a0);
          v1 = MEMORY.ref(4, s0).offset(0x4L).get();
          a3 = v0 + 0x1000L;
          lo = (long)(int)v1 * (int)a3 & 0xffff_ffffL;
          a0 = MEMORY.ref(4, s0).offset(0x10L).get();
          a1 = lo;
          v1 = 0x1000L;
          a2 = v1 - v0;
          lo = (long)(int)a0 * (int)a2 & 0xffff_ffffL;
          v0 = _800c692c.get();
          t0 = lo;
          v1 = a1 + t0;
          a0 = s1 + v0;
          if((int)v1 < 0) {
            v1 = v1 + 0x1fffL;
          }

          //LAB_800e5f38
          v0 = (int)v1 >> 13;
          MEMORY.ref(1, a0).offset(0xcL).setu(v0);
          v0 = MEMORY.ref(4, s0).offset(0x8L).get();

          lo = (long)(int)v0 * (int)a3 & 0xffff_ffffL;
          a0 = lo;
          v0 = MEMORY.ref(4, s0).offset(0x14L).get();

          lo = (long)(int)v0 * (int)a2 & 0xffff_ffffL;
          v0 = _800c692c.get();
          t0 = lo;
          a0 = a0 + t0;
          a1 = s1 + v0;
          if((int)a0 < 0) {
            a0 = a0 + 0x1fffL;
          }

          //LAB_800e5f74
          v0 = (int)a0 >> 13;
          MEMORY.ref(1, a1).offset(0xdL).setu(v0);
          v0 = MEMORY.ref(4, s0).offset(0xcL).get();

          lo = (long)(int)v0 * (int)a3 & 0xffff_ffffL;
          a0 = lo;
          v0 = MEMORY.ref(4, s0).offset(0x18L).get();

          lo = (long)(int)v0 * (int)a2 & 0xffff_ffffL;
          v0 = _800c692c.get();
          v1 = lo;
          v1 = a0 + v1;
          a0 = s1 + v0;
          if((int)v1 < 0) {
            v1 = v1 + 0x1fffL;
          }

          //LAB_800e5fb0
          v0 = (int)v1 >> 13;
          MEMORY.ref(1, a0).offset(0xeL).setu(v0);
        }
      }

      //LAB_800e5fb8
      s1 = s1 + 0x84L;

      //LAB_800e5fbc
    }
  }

  @Method(0x800e5fe8L)
  public static void FUN_800e5fe8(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c struct) {
    //LAB_800e6008
    for(int i = 0; i < 3; i++) {
      GsSetFlatLight(i, _800c692c.deref(4).offset(i * 0x84L).cast(GsF_LIGHT::new)); //TODO
    }

    final long v0 = _800c6930.get(); //TODO
    GsSetAmbient(MEMORY.ref(4, v0).offset(0x0L).get(), MEMORY.ref(4, v0).offset(0x4L).get(), MEMORY.ref(4, v0).offset(0x8L).get());
    _1f8003f8.setu(getProjectionPlaneDistance());
  }

  @Method(0x800e6070L)
  public static void FUN_800e6070() {
    allocateScriptState(1, 0, false, 0, 0);
    loadScriptFile(1, script_800faebc, "BTTL Script 800faebc", 0); //TODO unknown size
    setCallback04(1, MEMORY.ref(4, getMethodAddress(Bttl.class, "FUN_800e5a78", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new));
    setCallback08(1, MEMORY.ref(4, getMethodAddress(Bttl.class, "FUN_800e5fe8", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new));
    _800c6930.deref(4).offset(0x60L).setu(0);
    FUN_800e46c8();
  }

  @Method(0x800e6314L)
  public static void FUN_800e6314(long a0) {
    assert false;
  }

  @Method(0x800e6470L)
  public static long FUN_800e6470(final RunningScript a0) {
    final long t0 = a0.params_20.get(0).deref().get();
    final long a3 = _800c693c.get();
    MEMORY.ref(4, a3).offset(0x20L).oru(t0 & 0x1_0000L).oru(t0 & 0x2_0000L).oru(t0 & 0x10_0000L);

    if((MEMORY.ref(4, a3).offset(0x20L).get() & 0x10_0000L) != 0) {
      //LAB_800e651c
      for(int i = 0; i < battleStruct1a8Count_800c66a0.get(); i++) {
        final BattleStruct1a8 v1 = getBattleStruct1a8(i);

        if((v1._19e.get() & 0x1L) != 0 && v1._04.get() != 0 && v1._1a2.get() >= 0) {
          FUN_800ca418(i);
        }

        //LAB_800e6564
      }
    }

    //LAB_800e6578
    FUN_800e883c(_800c693c.deref(4).offset(0x1cL).get(), -0x1L);
    final long s2 = FUN_800e832c(a0.scriptStateIndex_00.get(), 0, getMethodAddress(Bttl.class, "FUN_800e70bc", long.class), MEMORY.ref(4, getMethodAddress(Bttl.class, "FUN_800e71dc", long.class), TriConsumerRef::new), getMethodAddress(Bttl.class, "FUN_800e6314", long.class));
    long v0 = scriptStatePtrArr_800bc1c0.get((int)s2).deref().innerStruct_00.getPointer(); //TODO
    MEMORY.ref(4, v0).offset(0x4L).setu(0x600_0400L);
    v0 = _800c6938.get();
    MEMORY.ref(4, v0).offset(0x18L).setu(s2);
    MEMORY.ref(4, v0).offset(0x0L).setu(t0 & 0xffffL);
    MEMORY.ref(4, v0).offset(0x4L).setu(a0.params_20.get(1).deref().get());
    MEMORY.ref(4, v0).offset(0x8L).setu(a0.params_20.get(2).deref().get());
    MEMORY.ref(4, v0).offset(0x10L).setu(a0.params_20.get(3).deref().get() & 0xff);
    MEMORY.ref(4, v0).offset(0x1cL).setu(0);
    MEMORY.ref(4, v0).offset(0xcL).setu(a0.scriptStateIndex_00.get());
    FUN_80012b1c(0x3L, getMethodAddress(Bttl.class, "FUN_800e704c", long.class), v0 + 0x1cL);
    _800c6938.deref(4).offset(0x20L).setu(-0x1L);
    return s2;
  }

  @Method(0x800e6920L)
  public static long FUN_800e6920(final RunningScript a0) {
    long v1;
    long s1;
    long sp20;

    sp20 = (short)a0.params_20.get(0).deref().get();
    s1 = a0.params_20.get(0).deref().get() & 0xff_0000L;
    if(sp20 == -0x1L) {
      final BtldScriptData27c v0 = scriptStatePtrArr_800bc1c0.get((int)a0.params_20.get(1).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
      assert false : "?"; //a0.params_20.get(0).set(sp0x20);
      sp20 = getBattleStruct1a8(v0._26c.get())._1a2.get();
    }

    //LAB_800e69a8
    _800c693c.deref(4).offset(0x20L).oru(s1 & 0x10_0000L);
    FUN_800e6470(a0);

    v1 = _800c6938.get();
    MEMORY.ref(4, v1).offset(0x14L).setu(0);
    MEMORY.ref(4, v1).offset(0x0L).oru(0x300_0000L);
    if(sp20 < 0x100L) {
      loadDrgnBinFile(0, 4433 + sp20 * 2, 0, getMethodAddress(Bttl.class, "FUN_800e929c", long.class, long.class, long.class), 0, 0x4L);
      loadDrgnBinFile(0, 4434 + sp20 * 2, 0, getMethodAddress(Bttl.class, "FUN_800e7060", long.class, long.class, long.class), _800c6938.deref(4).offset(0x18L).get(), 0x2L);
    } else {
      //LAB_800e6a30
      final long a0_0 = sp20 >>> 4;
      long s0_0 = _800faec4.offset(2, (a0_0 - 0x100L) * 0x2L).get() + (sp20 & 0xfL);
      if((int)a0_0 >= 0x140L) {
        s0_0 = s0_0 + 0x75L;
      }

      //LAB_800e6a60
      s0_0 = (s0_0 - 0x1L) * 0x2L;
      loadDrgnBinFile(0, 4945 + s0_0, 0, getMethodAddress(Bttl.class, "FUN_800e929c", long.class, long.class, long.class), 0, 0x4L);
      loadDrgnBinFile(0, 4946 + s0_0, 0, getMethodAddress(Bttl.class, "FUN_800e7060", long.class, long.class, long.class), _800c6938.deref(4).offset(0x18L).get(), 0x2L);
    }

    //LAB_800e6a9c
    _800fafe8.setu(0x1L);
    return 0;
  }

  @Method(0x800e704cL)
  public static void FUN_800e704c(final long param) {
    assert false;
  }

  @Method(0x800e7060L)
  public static void FUN_800e7060(final long address, final long fileSize, final long param) {
    assert false;
  }

  @Method(0x800e70bcL)
  public static void FUN_800e70bc(long a0) {
    assert false;
  }

  @Method(0x800e71dcL)
  public static void FUN_800e71dc(long a0) {
    assert false;
  }

  @Method(0x800e727cL)
  public static long FUN_800e727c(final RunningScript a0) {
    if(_800fafe8.get() != 0 && a0.scriptStateIndex_00.get() != _800c6938.deref(4).offset(0xcL).get()) {
      return 0x2L;
    }

    //LAB_800e72b4
    //LAB_800e72b8
    final long v1 = _800fafe8.get();

    //LAB_800e72dc
    if(v1 == 0) {
      FUN_800e6920(a0);
      return 0x2L;
    }

    if(v1 < 0x4L) {
      return 0x2L;
    }

    if(v1 == 0x4L) {
      //LAB_800e72f4
      _800fafe8.setu(0);
      _800c6938.deref(4).offset(0x18L).setu(0);
      return 0;
    }

    //LAB_800e7304
    throw new RuntimeException("Undefined v0");
  }

  @Method(0x800e7ec4L)
  public static void FUN_800e7ec4(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c struct) {
    long v0;
    long v1;
    long a0 = scriptStatePtrArr_800bc1c0.get(index).deref().innerStruct_00.getPointer(); //TODO
    v1 = MEMORY.ref(2, a0).offset(0x50L).getSigned();
    if(v1 != -0x1L) {
      v0 = MEMORY.ref(2, a0).offset(0x56L).getSigned();

      if(v0 != -0x1L) {
        scriptStatePtrArr_800bc1c0.get((int)v0).deref().innerStruct_00.derefAs(BttlScriptData6c.class)._54.set((short)MEMORY.ref(2, a0).offset(0x54L).get());
      } else {
        //LAB_800e7f4c
        scriptStatePtrArr_800bc1c0.get((int)v1).deref().innerStruct_00.derefAs(BttlScriptData6c.class)._52.set((short)MEMORY.ref(2, a0).offset(0x54L).get());
      }

      //LAB_800e7f6c
      if(MEMORY.ref(2, a0).offset(0x54L).getSigned() != -0x1L) {
        scriptStatePtrArr_800bc1c0.get((int)MEMORY.ref(2, a0).offset(0x54L).getSigned()).deref().innerStruct_00.derefAs(BttlScriptData6c.class)._56.set((short)MEMORY.ref(2, a0).offset(0x56L).get());
      }

      //LAB_800e7fa0
      MEMORY.ref(2, a0).offset(0x56L).setu(-0x1L);
      MEMORY.ref(2, a0).offset(0x54L).setu(-0x1L);
      MEMORY.ref(2, a0).offset(0x50L).setu(-0x1L);
    }

    //LAB_800e7fac
    //LAB_800e7fcc
    while(struct._52.get() != -0x1L) {
      a0 = scriptStatePtrArr_800bc1c0.get(struct._52.get()).deref().innerStruct_00.getPointer(); //TODO

      //LAB_800e7ff8
      while(MEMORY.ref(2, a0).offset(0x52L).getSigned() != -0x1L) {
        a0 = scriptStatePtrArr_800bc1c0.get((int)MEMORY.ref(2, a0).offset(0x52L).get()).deref().innerStruct_00.getPointer(); //TODO
      }

      //LAB_800e8020
      FUN_80015d38(MEMORY.ref(1, a0).offset(0xeL).getSigned());
    }

    //LAB_800e8040
    if(!struct._4c.isNull()) {
      struct._4c.deref().run(index, state, struct);
    }

    //LAB_800e805c
    a0 = struct._44.get();

    if(a0 != 0) {
      removeFromLinkedList(a0);
    }

    //LAB_800e8074
    while(!struct._58.isNull()) {
      a0 = struct._58.getPointer();

      struct._58.setNullable(struct._58.deref()._00.derefNullable());

      //LAB_800e8088
      removeFromLinkedList(a0);

      //LAB_800e8090
    }
  }

  @Method(0x800e80c4L)
  public static long FUN_800e80c4(final long a0, final long a1, final long a2, @Nullable final TriConsumerRef<Integer, ScriptState<BttlScriptData6c>, BttlScriptData6c> callback, final long a4) {
    final long index = allocateScriptState(0x6cL, BttlScriptData6c::new);
    long s3 = scriptStatePtrArr_800bc1c0.get((int)index).deref().getAddress();

    loadScriptFile(index, script_800faebc, "BTTL Script 800faebc", 0); //TODO unknown size
    setCallback04(index, MEMORY.ref(4, getMethodAddress(Bttl.class, "FUN_800e8e9c", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new));

    if(callback != null) {
      setCallback08(index, callback);
    }

    //LAB_800e8150
    setCallback0c(index, MEMORY.ref(4, getMethodAddress(Bttl.class, "FUN_800e7ec4", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new));
    final long s0 = MEMORY.ref(4, s3).offset(0x0L).get();
    MEMORY.ref(4, s0).offset(0x8L).setu(a1);
    if(a1 != 0) {
      MEMORY.ref(4, s0).offset(0x44L).setu(addToLinkedListTail(a1));
    } else {
      //LAB_800e8184
      MEMORY.ref(4, s0).offset(0x44L).setu(0);
    }

    //LAB_800e8188
    MEMORY.ref(4, s0).offset(0x48L).setu(a2);
    MEMORY.ref(1, s0).offset(0xeL).setu(index);
    MEMORY.ref(4, s0).offset(0x14L).setu(0);
    MEMORY.ref(4, s0).offset(0x18L).setu(0);
    MEMORY.ref(4, s0).offset(0x1cL).setu(0);
    MEMORY.ref(2, s0).offset(0x20L).setu(0);
    MEMORY.ref(2, s0).offset(0x22L).setu(0);
    MEMORY.ref(2, s0).offset(0x24L).setu(0);
    MEMORY.ref(2, s0).offset(0x32L).setu(0);
    MEMORY.ref(4, s0).offset(0x34L).setu(0);
    MEMORY.ref(4, s0).offset(0x38L).setu(0);
    MEMORY.ref(4, s0).offset(0x3cL).setu(0);
    MEMORY.ref(4, s0).offset(0x40L).setu(0);
    MEMORY.ref(4, s0).offset(0x4cL).setu(a4);
    MEMORY.ref(1, s0).offset(0x0L).setu(0x45L);
    MEMORY.ref(1, s0).offset(0x1L).setu(0x4dL);
    MEMORY.ref(1, s0).offset(0x2L).setu(0x20L);
    MEMORY.ref(1, s0).offset(0x3L).setu(0x20L);
    MEMORY.ref(4, s0).offset(0x4L).setu(0xff00_0000L);
    MEMORY.ref(1, s0).offset(0xcL).setu(-0x1L);
    MEMORY.ref(1, s0).offset(0xdL).setu(-0x1L);
    MEMORY.ref(4, s0).offset(0x10L).setu(0x5400_0000L);
    MEMORY.ref(2, s0).offset(0x26L).setu(0x1000L);
    MEMORY.ref(2, s0).offset(0x28L).setu(0x1000L);
    MEMORY.ref(2, s0).offset(0x2aL).setu(0x1000L);
    MEMORY.ref(2, s0).offset(0x2cL).setu(0x80L);
    MEMORY.ref(2, s0).offset(0x2eL).setu(0x80L);
    MEMORY.ref(2, s0).offset(0x30L).setu(0x80L);
    MEMORY.ref(2, s0).offset(0x50L).setu(-0x1L);
    MEMORY.ref(2, s0).offset(0x52L).setu(-0x1L);
    MEMORY.ref(2, s0).offset(0x54L).setu(-0x1L);
    MEMORY.ref(2, s0).offset(0x56L).setu(-0x1L);
    MEMORY.ref(4, s0).offset(0x58L).setu(0);
    MEMORY.ref(4, s3).offset(0xf8L).setu(s0 + 0x5cL);
    strcpy(MEMORY.ref(7, s0 + 0x5cL, CString::new), _800c6e18.get());

    if(a0 != -0x1L) {
      long v0 = scriptStatePtrArr_800bc1c0.get((int)a0).deref().getAddress();
      v0 = MEMORY.ref(4, v0).offset(0x0L).get();
      v0 = MEMORY.ref(4, v0).offset(0x0L).get();
      final long a0_0;
      if(v0 != 0x2020_4d45L) {
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x693cL).get();

        a0_0 = MEMORY.ref(2, v0).offset(0x1cL).get();
      } else {
        a0_0 = a0;
      }

      //LAB_800e8294
      long v1 = scriptStatePtrArr_800bc1c0.get((int)a0_0).deref().getAddress();
      v0 = scriptStatePtrArr_800bc1c0.get((int)index).deref().getAddress();
      v1 = MEMORY.ref(4, v1).offset(0x0L).get();
      long a1_0 = MEMORY.ref(4, v0).offset(0x0L).get();

      MEMORY.ref(2, a1_0).offset(0x50L).setu(a0_0);
      v0 = MEMORY.ref(2, v1).offset(0x52L).getSigned();
      if(v0 != -0x1L) {
        MEMORY.ref(2, a1_0).offset(0x54L).setu(MEMORY.ref(2, v1).offset(0x52L).get());
        v0 = MEMORY.ref(2, v1).offset(0x52L).getSigned();

        v0 = scriptStatePtrArr_800bc1c0.get((int)v0).deref().getAddress();

        v0 = MEMORY.ref(4, v0).offset(0x0L).get();

        MEMORY.ref(2, v0).offset(0x56L).setu(index);
      }

      //LAB_800e8300
      MEMORY.ref(2, v1).offset(0x52L).setu(index);
    }

    //LAB_800e8304
    return index;
  }

  @Method(0x800e832cL)
  public static <T extends MemoryRef> long FUN_800e832c(long a0, long a1, long a2, @Nullable final TriConsumerRef<Integer, ScriptState<T>, T> callback08, long a4) {
    final long index = allocateScriptState(0x6cL, BttlScriptData6c::new);
    loadScriptFile(index, script_800faebc, "BTTL Script FUN_800e832c", 0); //TODO
    setCallback04(index, MEMORY.ref(4, getMethodAddress(Bttl.class, "FUN_800e8e9c", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new));

    if(callback08 != null) {
      setCallback08(index, callback08);
    }

    //LAB_800e83b8
    setCallback0c(index, MEMORY.ref(4, getMethodAddress(Bttl.class, "FUN_800e7ec4", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new));

    final ScriptState<BttlScriptData6c> s3 = scriptStatePtrArr_800bc1c0.get((int)index).derefAs(ScriptState.classFor(BttlScriptData6c.class));
    final long s0 = s3.innerStruct_00.derefAs(BttlScriptData6c.class).getAddress();

    MEMORY.ref(4, s0).offset(0x08L).setu(a1);

    if(a1 != 0) {
      assert false : "This has to be a bug, a1 is a pointer to a function"; //TODO
      MEMORY.ref(4, s0).offset(0x44L).setu(addToLinkedListTail(a1));
    } else {
      //LAB_800e83ec
      MEMORY.ref(4, s0).offset(0x44L).setu(0);
    }

    //LAB_800e83f0
    MEMORY.ref(4, s0).offset(0x48L).setu(a2);
    MEMORY.ref(1, s0).offset(0x0eL).setu(index);
    MEMORY.ref(4, s0).offset(0x14L).setu(0);
    MEMORY.ref(4, s0).offset(0x18L).setu(0);
    MEMORY.ref(4, s0).offset(0x1cL).setu(0);
    MEMORY.ref(2, s0).offset(0x20L).setu(0);
    MEMORY.ref(2, s0).offset(0x22L).setu(0);
    MEMORY.ref(2, s0).offset(0x24L).setu(0);
    MEMORY.ref(2, s0).offset(0x32L).setu(0);
    MEMORY.ref(4, s0).offset(0x34L).setu(0);
    MEMORY.ref(4, s0).offset(0x38L).setu(0);
    MEMORY.ref(4, s0).offset(0x3cL).setu(0);
    MEMORY.ref(4, s0).offset(0x40L).setu(0);
    MEMORY.ref(4, s0).offset(0x4cL).setu(a4);
    MEMORY.ref(1, s0).offset(0x00L).setu(0x45L);
    MEMORY.ref(1, s0).offset(0x01L).setu(0x4dL);
    MEMORY.ref(1, s0).offset(0x02L).setu(0x20L);
    MEMORY.ref(1, s0).offset(0x03L).setu(0x20L);
    MEMORY.ref(4, s0).offset(0x04L).setu(0xff00_0000L);
    MEMORY.ref(1, s0).offset(0x0cL).setu(-0x1L);
    MEMORY.ref(1, s0).offset(0x0dL).setu(-0x1L);
    MEMORY.ref(4, s0).offset(0x10L).setu(0x5400_0000L);
    MEMORY.ref(2, s0).offset(0x26L).setu(0x1000L);
    MEMORY.ref(2, s0).offset(0x28L).setu(0x1000L);
    MEMORY.ref(2, s0).offset(0x2aL).setu(0x1000L);
    MEMORY.ref(2, s0).offset(0x2cL).setu(0x80L);
    MEMORY.ref(2, s0).offset(0x2eL).setu(0x80L);
    MEMORY.ref(2, s0).offset(0x30L).setu(0x80L);
    MEMORY.ref(2, s0).offset(0x50L).setu(-0x1L);
    MEMORY.ref(2, s0).offset(0x52L).setu(-0x1L);
    MEMORY.ref(2, s0).offset(0x54L).setu(-0x1L);
    MEMORY.ref(2, s0).offset(0x56L).setu(-0x1L);
    MEMORY.ref(4, s0).offset(0x58L).setu(0);
    s3.ui_f8.set(s0 + 0x5cL);
    strcpy(MEMORY.ref(7, s0 + 0x5cL, CString::new), _800c6e18.get()); //TODO
    if((int)a0 != -0x1L) {
      final long a0_0;
      if(scriptStatePtrArr_800bc1c0.get((short)a0).deref().innerStruct_00.derefAs(BtldScriptData27c.class)._00.get() == 0x2020_4d45L) {
        a0_0 = a0;
      } else {
        a0_0 = _800c693c.deref(4).offset(0x1cL).get();
      }

      //LAB_800e84fc
      //TODO
      long v1 = scriptStatePtrArr_800bc1c0.get((short)a0_0).deref().innerStruct_00.derefAs(BtldScriptData27c.class).getAddress();
      final long a1_0 = scriptStatePtrArr_800bc1c0.get((short)index).deref().innerStruct_00.derefAs(BtldScriptData27c.class).getAddress(); //TODO

      MEMORY.ref(2, a1_0).offset(0x50L).setu(a0_0);
      if(MEMORY.ref(2, v1).offset(0x52L).getSigned() != -0x1L) {
        MEMORY.ref(2, a1_0).offset(0x54L).setu(MEMORY.ref(2, v1).offset(0x52L).get());
        //TODO
        long v0 = scriptStatePtrArr_800bc1c0.get((int)MEMORY.ref(2, v1).offset(0x52L).getSigned()).deref().innerStruct_00.derefAs(BtldScriptData27c.class).getAddress();
        MEMORY.ref(2, v0).offset(0x56L).setu(index);
      }

      //LAB_800e8568
      MEMORY.ref(2, v1).offset(0x52L).setu(index);
    }

    //LAB_800e856c
    return index;
  }

  @Method(0x800e883cL)
  public static void FUN_800e883c(long a0, long a1) {
    long v0;
    long v1;
    long a2;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    s5 = a0;
    v0 = 0x800c_0000L;
    v1 = v0 - 0x3e40L;
    v0 = s5 << 2;
    v0 = v0 + v1;
    v0 = MEMORY.ref(4, v0).offset(0x0L).get();

    s3 = MEMORY.ref(4, v0).offset(0x0L).get();

    s0 = MEMORY.ref(2, s3).offset(0x52L).getSigned();
    v0 = -0x1L;
    if(s0 == v0) {
      s1 = a1;
    } else {
      s1 = a1;
      s4 = v1;
      s2 = v0;

      //LAB_800e889c
      do {
        a0 = s0;
        a1 = s1;
        FUN_800e883c(a0, a1);
        v0 = s0 << 2;
        v0 = v0 + s4;
        v0 = MEMORY.ref(4, v0).offset(0x0L).get();

        v0 = MEMORY.ref(4, v0).offset(0x0L).get();

        s0 = MEMORY.ref(2, v0).offset(0x54L).getSigned();
      } while(s0 != s2);
    }

    //LAB_800e88cc
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x693cL).get();

    v0 = MEMORY.ref(4, v0).offset(0x1cL).get();

    if(s5 != v0) {
      if(s5 != s1) {
        v0 = 0x4_0000L;
        v1 = MEMORY.ref(4, s3).offset(0x4L).get();

        v0 = v1 & v0;
        if(v0 == 0) {
          v0 = 0xff00_0000L;
          v0 = v1 & v0;
          v1 = 0x200_0000L;
          if(v0 != v1) {
            v0 = MEMORY.ref(4, s3).offset(0x58L).get();

            if(v0 != 0) {
              s2 = s3 + 0x58L;
              v0 = MEMORY.ref(4, s2).offset(0x0L).get();

              //LAB_800e892c
              do {
                v0 = MEMORY.ref(1, v0).offset(0x4L).get();
                s0 = v0 << 24;
                a0 = (int)s0 >> 24;
                v0 = addToLinkedListTail(a0);
                v1 = MEMORY.ref(4, s2).offset(0x0L).get();
                s1 = v0;
                if(v1 < s1) {
                  a2 = s1;
                  a1 = v1;
                  a0 = (int)s0 >> 26;
                  v0 = a0;
                  if((int)v0 > 0) {
                    a0 = a0 - 0x1L;

                    //LAB_800e8968
                    do {
                      v0 = MEMORY.ref(4, a1).offset(0x0L).get();
                      a1 = a1 + 0x4L;
                      v1 = a0;
                      a0 = a0 - 0x1L;
                      MEMORY.ref(4, a2).offset(0x0L).setu(v0);
                      a2 = a2 + 0x4L;
                    } while((int)v1 > 0);
                  }

                  //LAB_800e8984
                  a0 = MEMORY.ref(4, s2).offset(0x0L).get();
                  removeFromLinkedList(a0);
                  MEMORY.ref(4, s2).offset(0x0L).setu(s1);
                } else {
                  //LAB_800e899c
                  removeFromLinkedList(s1);
                }

                //LAB_800e89ac
                s2 = MEMORY.ref(4, s2).offset(0x0L).get();

                v0 = MEMORY.ref(4, s2).offset(0x0L).get();
              } while(v0 != 0);
            }

            //LAB_800e89c4
            v0 = MEMORY.ref(4, s3).offset(0x44L).get();

            if(v0 != 0) {
              s0 = MEMORY.ref(4, s3).offset(0x8L).get();
              v0 = addToLinkedListTail(s0);
              v1 = MEMORY.ref(4, s3).offset(0x44L).get();
              s1 = v0;
              if(v1 < s1) {
                a2 = s1;
                a1 = v1;
                a0 = (int)s0 >> 2;
                v0 = a0;
                if((int)v0 > 0) {
                  a0 = a0 - 0x1L;

                  //LAB_800e8a0c
                  do {
                    v0 = MEMORY.ref(4, a1).offset(0x0L).get();
                    a1 = a1 + 0x4L;
                    v1 = a0;
                    a0 = a0 - 0x1L;
                    MEMORY.ref(4, a2).offset(0x0L).setu(v0);
                    a2 = a2 + 0x4L;
                  } while((int)v1 > 0);
                }

                //LAB_800e8a28
                a0 = MEMORY.ref(4, s3).offset(0x44L).get();
                removeFromLinkedList(a0);
                MEMORY.ref(4, s3).offset(0x44L).setu(s1);
              } else {
                //LAB_800e8a40
                removeFromLinkedList(s1);
              }
            }

            //LAB_800e8a50
            v0 = addToLinkedListTail(0x16cL);
            v1 = 0x800c_0000L;
            v1 = v1 - 0x3e40L;
            a0 = s5 << 2;
            a0 = a0 + v1;
            a0 = MEMORY.ref(4, a0).offset(0x0L).get();
            s1 = v0;
            if(a0 < s1) {
              a2 = s1;
              a1 = 0x5aL;

              //LAB_800e8a88
              do {
                v0 = MEMORY.ref(4, a0).offset(0x0L).get();
                MEMORY.ref(4, a2).offset(0x0L).setu(v0);
                a0 = a0 + 0x4L;
                a2 = a2 + 0x4L;
                v1 = a1;
                a1 = a1 - 0x1L;
              } while((int)v1 > 0);

              v0 = 0x800c_0000L;
              v0 = v0 - 0x3e40L;
              s0 = s5 << 2;
              s0 = s0 + v0;
              a0 = MEMORY.ref(4, s0).offset(0x0L).get();
              removeFromLinkedList(a0);
              v0 = s1 + 0x100L;
              MEMORY.ref(4, s0).offset(0x0L).setu(s1);
              MEMORY.ref(4, s1).offset(0x0L).setu(v0);
            } else {
              //LAB_800e8ad4
              removeFromLinkedList(s1);
            }
          }
        }
      }
    }

    //LAB_800e8ae4
  }

  @Method(0x800e8dd4L)
  public static long FUN_800e8dd4(final long a0, final long a1, final long a2, final long a3, final long size) {
    final long addr = addToLinkedListTail(size); //TODO struct
    MEMORY.ref(1, addr).offset(0x4L).setu(size);
    MEMORY.ref(1, addr).offset(0x5L).setu(a1);
    MEMORY.ref(2, addr).offset(0x6L).setu(a2);
    MEMORY.ref(4, addr).offset(0x8L).setu(a3);
    MEMORY.ref(4, addr).offset(0x0L).setu(MEMORY.ref(4, a0).offset(0x58L).get());
    MEMORY.ref(4, a0).offset(0x58L).setu(addr);
    MEMORY.ref(4, a0).offset(0x4L).oru(0x1L << a1);
    return addr;
  }

  @Method(0x800e8e9cL)
  public static void FUN_800e8e9c(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c struct) {
    Pointer<BttlScriptData6c> s0 = struct._58;

    //LAB_800e8ee0
    while(!s0.isNull()) {
      final long v1 = s0.deref()._08.deref().run(struct, s0.deref());
      //LAB_800e8f1c
      if(v1 == 0) {
        //LAB_800e8f2c
        struct._04.and(~(1 << (byte)(s0.deref()._04.get() >> 8))); //TODO wtf is this
        final BttlScriptData6c a0 = s0.deref();
        s0.set(a0._00.deref());
        removeFromLinkedList(a0.getAddress());
      } else if(v1 == 0x1L) {
        //LAB_800e8f6c
        s0 = s0.deref()._00;
      } else if(v1 == 0x2L) {
        //LAB_800e8f78
        FUN_80015d38(index);
        return;
      }

      //LAB_800e8f8c
    }

    //LAB_800e8f9c
    if(!struct._48.isNull()) {
      struct._48.deref().run(index, state, struct);
    }

    //LAB_800e8fb8
  }

  @Method(0x800e8ffcL)
  public static void FUN_800e8ffc() {
    long v0 = addToLinkedListTail(0x7ccL);
    _800c6938.setu(v0 + 0x5b8L);
    _800c6930.setu(v0 + 0x5dcL);
    _800c692c.setu(v0 + 0x640L);
    MEMORY.ref(4, v0).offset(0x20L).setu(0x4L);
    MEMORY.ref(4, v0).offset(0x24L).setu(v0 + 0x28L);
    _800c6944.setu(v0 + 0x2f8L);
    _800c6940.setu(v0 + 0x390L);
    _800c693c.setu(v0);
    MEMORY.ref(4, v0).offset(0x1cL).setu(0);
    _800c6948.setu(v0 + 0x39cL);
    v0 = FUN_800e80c4(-0x1L, 0, 0, null, 0);
    long a0 = _800c693c.get();
    MEMORY.ref(4, a0).offset(0x1cL).setu(v0);
    long v1 = scriptStatePtrArr_800bc1c0.getAddress();
    v0 = MEMORY.ref(4, v1).offset(v0 * 0x4L).deref(4).get();
    MEMORY.ref(4, v0).offset(0x4L).setu(0x600_0400L);
    MEMORY.ref(4, a0).offset(0x2cL).setu(0);
    MEMORY.ref(4, a0).offset(0x30L).setu(0);
    MEMORY.ref(4, a0).offset(0x34L).setu(0);
    MEMORY.ref(4, a0).offset(0x38L).setu(0);
    FUN_800e6070();
    FUN_80012b1c(0x1L, getMethodAddress(SBtld.class, "FUN_801098f4", long.class), 0);
  }

  @Method(0x800e9100L)
  public static void FUN_800e9100() {
    FUN_800eacf4();
  }

  @Method(0x800e9288L)
  public static void FUN_800e9288(final long a0, final long a1, final long a2) {
    if(a2 != 0) {
      MEMORY.ref(4, a2).offset(0x0L).setu(a0);
    }
  }

  @Method(0x800e929cL)
  public static void FUN_800e929c(final long address, final long fileSize, final long param) {
    final long count = MEMORY.ref(4, address).offset(0x4L).get();

    //LAB_800e92d4
    for(int i = 0; i < count; i++) {
      if(MEMORY.ref(4, address).offset(i * 0x8L).offset(0xcL).get() != 0) {
        final TimHeader tim = parseTimHeader(MEMORY.ref(4, address + MEMORY.ref(4, address).offset(i * 0x8L).offset(0x8L).get() + 0x4L)); //TODO
        LoadImage(tim.getImageRect(), tim.getImageAddress());

        if((tim.flags.get() & 0x8L) != 0) {
          LoadImage(tim.getClutRect(), tim.getClutAddress());
        }

        //LAB_800e9324
        DrawSync(0);
      }

      //LAB_800e932c
    }

    //LAB_800e933c
    if((param & 0x1L) == 0) {
      removeFromLinkedList(address);
    }

    //LAB_800e9354
  }

  @Method(0x800e93e0L)
  public static long FUN_800e93e0(final RunningScript a0) {
    a0.params_20.get(0).deref().set(FUN_800e80c4(a0.scriptStateIndex_00.get(), 0, 0, null, 0));
    return 0;
  }

  @Method(0x800ea620L)
  public static void FUN_800ea620(final long a0, final long a1, final long a2) {
    //LAB_800ea674
    for(int s5 = 0; s5 < MEMORY.ref(2, a0).offset(0x6L).get(); s5++) {
      long s2 = a0 + MEMORY.ref(4, a0).offset(s5 * 0x8L).offset(0xcL).get();
      long v1 = MEMORY.ref(4, a0).offset(s5 * 0x8L).offset(0x8L).get() & 0xff00_0000L;
      if(v1 == 0x100_0000L) {
        //LAB_800ea6d4
        final long a0_0 = s2 + MEMORY.ref(4, s2).offset(0xcL).get();
        adjustTmdPointers(MEMORY.ref(4, a0_0 + 0x10L, Tmd::new)); //TODO

        //LAB_800ea700
        long s1 = a0_0 + 0xcL;
        for(int s0 = 0; s0 < MEMORY.ref(4, s1).offset(0x8L).get(); s0++) {
          FUN_800de76c(s1, s0);
        }
        //LAB_800ea6b4
      } else if(v1 == 0x300_0000L) {
        //LAB_800ea724
        final long a0_0 = s2 + MEMORY.ref(4, s2).offset(0xcL).get();
        adjustTmdPointers(MEMORY.ref(4, a0_0 + 0x10L, Tmd::new)); //TODO
        FUN_800de76c(a0_0 + 0xcL, 0);
      }

      if(v1 == 0x100_0000L || v1 == 0x200_0000L || v1 == 0x300_0000L) {
        //LAB_800ea748
        final long a2_0 = MEMORY.ref(4, s2).offset(0x8L).get();
        final long v1_0 = MEMORY.ref(4, s2).offset(0xcL).get();

        if(a2_0 != v1_0 && a2 != 0) {
          FUN_800eb308(scriptStatePtrArr_800bc1c0.get((int)a2).deref().innerStruct_00.getPointer(), s2 + v1_0, s2 + a2_0); //TODO
        }
      }

      //LAB_800ea778
      //LAB_800ea77c
    }

    //LAB_800ea790
    _800c6950.setu(a0);
    _800c693c.deref(4).offset(0x5acL).setu(a0);
  }

  @Method(0x800ea7d0L)
  public static void FUN_800ea7d0(long a0, long a1, long a2) {
    long v0;
    long v1;
    long a3;
    long t0;
    long t1;
    long t2;
    long t3;
    long s0;
    s0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, s0).offset(0x693cL).get();
    a2 = MEMORY.ref(4, v0).offset(0x1cL).get();

    FUN_800ea620(a0, a1, a2);
    a2 = 0;
    v1 = MEMORY.ref(4, s0).offset(0x693cL).get();
    v0 = a2;
    a3 = MEMORY.ref(4, v1).offset(0x5acL).get();
    if(v0 == 0) {
      t1 = 0xff00_0000L;
      t0 = v1;
      a0 = a3;

      //LAB_800ea814
      do {
        v0 = MEMORY.ref(4, a0).offset(0x8L).get();

        v1 = v0 & t1;
        if(v1 != 0) {
          break;
        }
        a1 = v0 & 0xffL;
        v0 = MEMORY.ref(4, a0).offset(0xcL).get();
        v1 = a1 << 2;
        v1 = t0 + v1;
        v0 = a3 + v0;
        MEMORY.ref(4, v1).offset(0x390L).setu(v0);
        v0 = MEMORY.ref(2, a3).offset(0x6L).get();
        a2 = a2 + 0x1L;
        a0 = a0 + 0x8L;
      } while((int)a2 <= (int)v0);

      //LAB_800ea850
      a0 = MEMORY.ref(2, a3).offset(0x6L).get();

      if((int)a0 >= (int)a2) {
        t0 = 0xff00_0000L;
        a1 = 0x100_0000L;
        v0 = a2 << 3;
        v1 = v0 + a3;

        //LAB_800ea874
        do {
          v0 = MEMORY.ref(4, v1).offset(0x8L).get();

          v0 = v0 & t0;
          if(v0 != a1) {
            break;
          }
          a2 = a2 + 0x1L;
          v1 = v1 + 0x8L;
        } while((int)a0 >= (int)a2);
      }
    }

    v0 = 0x800c_0000L;

    //LAB_800ea89c
    v0 = MEMORY.ref(4, v0).offset(0x693cL).get();
    v1 = 0x3fL;
    v0 = v0 + 0xfcL;

    //LAB_800ea8a8
    do {
      MEMORY.ref(4, v0).offset(0x2f8L).setu(0);
      v1 = v1 - 0x1L;
      v0 = v0 - 0x4L;
    } while((int)v1 >= 0);

    v0 = MEMORY.ref(2, a3).offset(0x6L).get();

    if((int)v0 >= (int)a2) {
      v0 = 0x800c_0000L;
      t2 = 0xff00_0000L;
      t1 = 0x300_0000L;
      t0 = MEMORY.ref(4, v0).offset(0x693cL).get();
      v0 = a2 << 3;
      a0 = v0 + a3;

      //LAB_800ea8e0
      do {
        v0 = MEMORY.ref(4, a0).offset(0x8L).get();

        v1 = v0 & t2;
        if(v1 != t1) {
          break;
        }
        a1 = v0 & 0xffL;
        if(a1 >= 0x5L) {
          v0 = MEMORY.ref(4, a0).offset(0xcL).get();

          v0 = a3 + v0;
          v1 = MEMORY.ref(4, v0).offset(0xcL).get();

          v0 = v0 + v1;
          v1 = a1 << 2;
          v1 = t0 + v1;
          v0 = v0 + 0x18L;
          MEMORY.ref(4, v1).offset(0x2f8L).setu(v0);
        }

        //LAB_800ea928
        v0 = MEMORY.ref(2, a3).offset(0x6L).get();
        a2 = a2 + 0x1L;
        a0 = a0 + 0x8L;
      } while((int)a2 <= (int)v0);

      //LAB_800ea93c
      v0 = MEMORY.ref(2, a3).offset(0x6L).get();

      if((int)v0 >= (int)a2) {
        t3 = 0xff00_0000L;
        t2 = 0x400_0000L;
        t1 = 0x800c_0000L;
        v0 = a2 << 3;
        t0 = v0 + a3;

        //LAB_800ea964
        do {
          v0 = MEMORY.ref(4, t0).offset(0x8L).get();

          v1 = v0 & t3;
          if(v1 != t2) {
            break;
          }
          a1 = v0 & 0xffL;
          a0 = MEMORY.ref(4, t0).offset(0xcL).get();
          a1 = a1 << 3;
          a0 = a3 + a0;
          v0 = MEMORY.ref(4, a0).offset(0x8L).get();
          v1 = MEMORY.ref(4, t1).offset(0x693cL).get();
          a0 = a0 + v0;
          v0 = MEMORY.ref(2, a0).offset(0x0L).get();
          v1 = a1 + v1;
          MEMORY.ref(2, v1).offset(0x39cL).setu(v0);
          v0 = MEMORY.ref(2, a0).offset(0x2L).get();
          v1 = v1 + 0x39cL;
          MEMORY.ref(2, v1).offset(0x2L).setu(v0);
          v0 = MEMORY.ref(2, a0).offset(0x4L).getSigned();

          v0 = v0 << 2;
          MEMORY.ref(1, v1).offset(0x4L).setu(v0);
          v0 = MEMORY.ref(4, t1).offset(0x693cL).get();
          v1 = MEMORY.ref(1, a0).offset(0x6L).get();
          v0 = a1 + v0;
          MEMORY.ref(1, v0).offset(0x3a1L).setu(v1);
          v0 = MEMORY.ref(4, t1).offset(0x693cL).get();
          v1 = MEMORY.ref(2, a0).offset(0xaL).get();
          a1 = a1 + v0;
          v0 = MEMORY.ref(2, a0).offset(0x8L).get();
          v1 = v1 << 6;
          v0 = v0 & 0x3f0L;
          v0 = v0 >>> 4;
          v1 = v1 | v0;
          MEMORY.ref(2, a1).offset(0x3a2L).setu(v1);
          v0 = MEMORY.ref(2, a3).offset(0x6L).get();
          a2 = a2 + 0x1L;
          t0 = t0 + 0x8L;
        } while((int)a2 <= (int)v0);
      }
    }

    //LAB_800eaa00
    v0 = 0x800c_0000L;

    //LAB_800eaa04
    v0 = MEMORY.ref(4, v0).offset(0x693cL).get();

    MEMORY.ref(4, v0).offset(0x38L).setu(a3);
    MEMORY.ref(4, v0).offset(0x5acL).setu(0);
  }

  @Method(0x800eaa24L)
  public static void FUN_800eaa24(final long address, final long fileSize, final long param) {
    long v0;
    long v1;
    long a0 = address;
    long a1;
    long a2;
    long a3;
    long s0;
    long s1;
    long s2;
    s2 = a0;
    v0 = MEMORY.ref(4, s2).offset(0x18L).get();
    s1 = MEMORY.ref(4, s2).offset(0x1cL).get();
    s0 = s2 + v0;
    a0 = s1;
    v0 = addToLinkedListTail(a0);
    a0 = v0;
    a3 = a0;
    a2 = s0;
    a1 = (int)s1 >> 2;
    v0 = a1;
    if((int)v0 > 0) {
      a1 = a1 - 0x1L;

      //LAB_800eaa74
      do {
        v0 = MEMORY.ref(4, a2).offset(0x0L).get();
        a2 = a2 + 0x4L;
        v1 = a1;
        a1 = a1 - 0x1L;
        MEMORY.ref(4, a3).offset(0x0L).setu(v0);
        a3 = a3 + 0x4L;
      } while((int)v1 > 0);
    }

    //LAB_800eaa90
    a1 = s1;
    a2 = 0;
    FUN_800ea7d0(a0, a1, a2);
    a1 = 0;
    a2 = a1;
    v0 = MEMORY.ref(4, s2).offset(0x20L).get();
    s1 = MEMORY.ref(4, s2).offset(0x24L).get();
    s0 = s2 + v0;
    a0 = s1;
    v0 = addToLinkedListTail(a0);
    a0 = v0;
    a3 = a0;
    a2 = s0;
    a1 = (int)s1 >> 2;
    v0 = a1;
    if((int)v0 > 0) {
      a1 = a1 - 0x1L;

      //LAB_800eaad4
      do {
        v0 = MEMORY.ref(4, a2).offset(0x0L).get();
        a2 = a2 + 0x4L;
        v1 = a1;
        a1 = a1 - 0x1L;
        MEMORY.ref(4, a3).offset(0x0L).setu(v0);
        a3 = a3 + 0x4L;
      } while((int)v1 > 0);
    }

    //LAB_800eaaf0
    a1 = s1;
    a2 = 0;
    FUN_800e929c(a0, a1, a2);
    v0 = MEMORY.ref(4, s2).offset(0x10L).get();
    s1 = MEMORY.ref(4, s2).offset(0x14L).get();
    s0 = s2 + v0;
    a0 = s1;
    v0 = addToLinkedListTail(a0);
    a0 = v0;
    a3 = a0;
    a2 = s0;
    a1 = (int)s1 >> 2;
    v0 = a1;
    if((int)v0 > 0) {
      a1 = a1 - 0x1L;

      //LAB_800eab34
      do {
        v0 = MEMORY.ref(4, a2).offset(0x0L).get();
        a2 = a2 + 0x4L;
        v1 = a1;
        a1 = a1 - 0x1L;
        MEMORY.ref(4, a3).offset(0x0L).setu(v0);
        a3 = a3 + 0x4L;
      } while((int)v1 > 0);
    }

    //LAB_800eab50
    v0 = 0x800c_0000L;
    a2 = MEMORY.ref(4, v0).offset(0x693cL).get();
    a1 = s1;
    a2 = a2 + 0x2cL;
    FUN_800e9288(a0, a1, a2);
    removeFromLinkedList(s2);
  }

  @Method(0x800eacf4L)
  public static void FUN_800eacf4() {
    loadDrgnBinFile(0, 4114, 0, getMethodAddress(Bttl.class, "FUN_800eaa24", long.class, long.class, long.class), _800c693c.get() + 0x2cL, 0x4L);
  }

  @Method(0x800ead44L)
  public static void FUN_800ead44(final RECT a0, final long a1) {
    //TODO ptrs
    SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), new RECT((short)960, (short)256, a0.w.get(), (short)a1), a0.x.get(), a0.y.get() + a0.h.get() - a1);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x4L, linkedListAddress_1f8003d8.get());
    linkedListAddress_1f8003d8.addu(0x18L);

    SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), new RECT(a0.x.get(), (short)(a0.y.get() + a1), a0.w.get(), (short)(a0.h.get() - a1)), a0.x.get(), a0.y.get());
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x4L, linkedListAddress_1f8003d8.get());
    linkedListAddress_1f8003d8.addu(0x18L);

    SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), new RECT(a0.x.get(), a0.y.get(), a0.w.get(), (short)a1), 0x3c0L, 0x100L);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x4L, linkedListAddress_1f8003d8.get());
    linkedListAddress_1f8003d8.addu(0x18L);
  }

  /** TODO this might be completely wrong */
  @Method(0x800eaec8L)
  public static long FUN_800eaec8(BttlScriptData6c a0, BttlScriptData6c a1) {
    a0 = a1;

    long a1_0 = a0._14.get() / 0x100;

    //LAB_800eaef0
    a0._14.add(a0._18.get());

    //LAB_800eaf08
    a1_0 = (a0._14.get() / 0x100 - a1_0) % a0._12.get();

    if((int)a1_0 < 0) {
      a1_0 = a1_0 + a0._12.get();
    }

    //LAB_800eaf30
    if(a1_0 != 0) {
      FUN_800ead44(a0.rect_0c, a1_0);
    }

    //LAB_800eaf44
    return 0x1L;
  }

  @Method(0x800eb308L)
  public static void FUN_800eb308(long a0, long a1, long a2) {
    long v0;
    long v1;
    long a3;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long lo;
    long sp10;
    s4 = a0;
    v0 = a1;
    a1 = MEMORY.ref(4, v0).offset(0x8L).get();

    if(a1 != 0) {
      s3 = a2;
      s2 = v0 + a1;
      s1 = 0;

      //LAB_800eb348
      do {
        v0 = s1 << 2;
        v0 = v0 + s2;
        v0 = MEMORY.ref(4, v0).offset(0x0L).get();

        s0 = s2 + v0;
        v0 = MEMORY.ref(2, s0).offset(0x0L).get();

        v0 = v0 & 0x4000L;
        if(v0 != 0) {
          a3 = 0x800f_0000L;
          v0 = 0x1cL;
          sp10 = v0;
          a0 = s4;
          a1 = 0xaL;
          a2 = 0;
          a3 = a3 - 0x5138L;
          v0 = FUN_800e8dd4(a0, a1, a2, a3, sp10);
          a0 = v0;
          v1 = MEMORY.ref(2, s0).offset(0x2L).get();

          v0 = v1 & 0x3c0L;
          if(v0 == 0) {
            v0 = MEMORY.ref(2, s3).offset(0x0L).get();
            v0 = v0 & 0x3c0L;
            v0 = v1 | v0;
            MEMORY.ref(2, a0).offset(0xcL).setu(v0);
            v1 = MEMORY.ref(2, s3).offset(0x2L).get();
            v1 = v1 & 0x100L;
            v0 = MEMORY.ref(2, s0).offset(0x4L).get();
            v0 = v0 | v1;
          } else {
            //LAB_800eb3cc
            v0 = MEMORY.ref(2, s0).offset(0x2L).get();
            MEMORY.ref(2, a0).offset(0xcL).setu(v0);
            v0 = MEMORY.ref(2, s0).offset(0x4L).get();
          }

          //LAB_800eb3dc
          MEMORY.ref(2, a0).offset(0xeL).setu(v0);
          v0 = MEMORY.ref(2, s0).offset(0x6L).getSigned();

          if((int)v0 < 0) {
            v0 = v0 + 0x3L;
          }

          //LAB_800eb3f8
          v0 = (int)v0 >> 2;
          MEMORY.ref(2, a0).offset(0x10L).setu(v0);
          v0 = MEMORY.ref(2, s0).offset(0x8L).get();

          MEMORY.ref(2, a0).offset(0x12L).setu(v0);
          MEMORY.ref(4, a0).offset(0x14L).setu(0);
          v1 = MEMORY.ref(2, s0).offset(0xcL).get();

          if(v1 >= 0x10L) {
            v0 = v1 << 4;
          } else {
            v0 = 0x100L;

            //LAB_800eb42c
            lo = (int)v0 / (int)v1;
            v0 = lo;
          }

          //LAB_800eb434

          MEMORY.ref(4, a0).offset(0x18L).setu(v0);
          v0 = MEMORY.ref(2, s0).offset(0xaL).get();

          if(v0 == 0) {
            v0 = MEMORY.ref(4, a0).offset(0x18L).get();
            v0 = -v0;
            MEMORY.ref(4, a0).offset(0x18L).setu(v0);
          }
        }

        //LAB_800eb45c
        s1 = s1 + 0x1L;
      } while((int)s1 < 0x7L);
    }

    //LAB_800eb46c
  }

  @Method(0x800eb9acL)
  public static void FUN_800eb9ac(final BattleRenderStruct s2, final ExtendedTmd extTmd, final TmdAnimationFile tmdAnim) {
    final int x = s2.coord2_558.coord.transfer.getX();
    final int y = s2.coord2_558.coord.transfer.getY();
    final int z = s2.coord2_558.coord.transfer.getZ();

    _800bda0c.set(s2);

    //LAB_800eb9fc
    for(int i = 0; i < 10; i++) {
      s2._618.get(i).set(0);
    }

    s2.tmd_5d0.set(extTmd.tmdPtr_00.deref().tmd);

    if(extTmd.ptr_08.get() != 0) {
      s2._5ec.set(extTmd.getAddress() + extTmd.ptr_08.get() / 0x4L * 0x4L); //TODO

      //LAB_800eba38
      for(int i = 0; i < 10; i++) {
        s2._5f0.get(i).set(s2._5ec.get() + MEMORY.ref(2, s2._5ec.get()).offset(i * 0x4L).get());
        FUN_800ec86c(s2, i);
      }
    } else {
      //LAB_800eba74
      //LAB_800eba7c
      for(int i = 0; i < 10; i++) {
        s2._5f0.get(i).set(0);
      }
    }

    //LAB_800eba8c
    adjustTmdPointers(s2.tmd_5d0.deref());
    FUN_80021b08(s2.objtable2_550, s2.dobj2s_00, s2.coord2s_a0, s2.params_3c0, 10);
    s2.coord2_558.param.set(s2.param_5a8);
    GsInitCoordinate2(null, s2.coord2_558);
    FUN_80021ca0(s2.objtable2_550, s2.tmd_5d0.deref(), s2.coord2_558, 10, extTmd.tmdPtr_00.deref().tmd.header.nobj.get() + 0x1L);
    FUN_800ec774(s2, tmdAnim);

    s2.coord2_558.coord.transfer.setX(x);
    s2.coord2_558.coord.transfer.setY(y);
    s2.coord2_558.coord.transfer.setZ(z);
    s2._5e4.set(0);
    s2._5e8.set((short)0x200);
  }

  @Method(0x800ebd34L)
  public static void FUN_800ebd34(final BattleRenderStruct struct, final int index) {
    long v0;
    long a2;
    long t0;
    long t1;
    long s0;
    long s1;
    long s4;
    long s6;

    v0 = struct._5f0.get(index).get(); //TODO ptr to RECT?

    if(v0 == 0) {
      struct._618.get(index).set(0);
      return;
    }

    //LAB_800ebd84
    long x = MEMORY.ref(2, v0).offset(0x0L).get();
    long y = MEMORY.ref(2, v0).offset(0x2L).get();
    long w = MEMORY.ref(2, v0).offset(0x4L).get() >>> 2;
    long h = MEMORY.ref(2, v0).offset(0x6L).get();

    //LAB_800ebdcc
    a2 = v0 + 0x8L;

    // There was a loop here, but each iteration overwrote the results from the previous iteration... I collapsed it into a single iteration
    a2 += (struct._65e.get(index).get() - 1) * 0x4L;
    s0 = MEMORY.ref(2, a2).offset(0x2L).get();
    t1 = MEMORY.ref(2, a2).offset(0x0L).get() & 0x1L;
    t0 = MEMORY.ref(2, a2).offset(0x0L).get() >>> 1;
    a2 = a2 + 0x4L;

    //LAB_800ebdf0
    if((s0 & 0xfL) != 0 && (struct._622.get(index).get() & 0xfL) != 0) {
      struct._622.get(index).decr();

      if(struct._622.get(index).get() == 0) {
        struct._622.get(index).set((int)s0);
        s0 = 0x10L;
      } else {
        //LAB_800ebe34
        s0 = 0;
      }
    }

    //LAB_800ebe38
    struct._64a.get(index).incr();

    if(struct._64a.get(index).get() >= (short)t0) {
      struct._64a.get(index).set((short)0);

      if(MEMORY.ref(2, a2).offset(0x0L).get() != 0xffffL) {
        v0 = struct._65e.get(index).get() + 0x1L;
      } else {
        //LAB_800ebe88
        v0 = 0x1L;
      }

      //LAB_800ebe8c
      struct._65e.get(index).set((short)v0);
    }

    //LAB_800ebe94
    if(s0 != 0) {
      if(t1 == 0) {
        s1 = (int)s0 >> 4;
        s4 = h - s1;
        s6 = 0x100L + s1;

        SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), new RECT((short)960, (short)256, (short)w, (short)h), x, y);
        insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x4L, linkedListAddress_1f8003d8.get());
        linkedListAddress_1f8003d8.addu(0x18L);

        SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), new RECT((short)x, (short)(y + s4), (short)w, (short)s1), 960, 256);
        insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x4L, linkedListAddress_1f8003d8.get());
        linkedListAddress_1f8003d8.addu(0x18L);

        SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), new RECT((short)x, (short)y, (short)w, (short)s4), 960, s6 & 0xffffL);
        insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x4L, linkedListAddress_1f8003d8.get());
        linkedListAddress_1f8003d8.addu(0x18L);
      } else {
        //LAB_800ebf88
        s1 = (int)s0 >> 4;
        s4 = h - s1;
        s6 = 0x100L + s4;

        SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), new RECT((short)960, (short)256, (short)w, (short)h), x, y);
        insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x4L, linkedListAddress_1f8003d8.get());
        linkedListAddress_1f8003d8.addu(0x18L);

        SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), new RECT((short)x, (short)y, (short)w, (short)s1), 960, s6 & 0xffffL);
        insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x4L, linkedListAddress_1f8003d8.get());
        linkedListAddress_1f8003d8.addu(0x18L);

        SetDrawMove(linkedListAddress_1f8003d8.deref(4).cast(DR_MOVE::new), new RECT((short)x, (short)(y + s1), (short)w, (short)s4), 960, 256);
        insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x4L, linkedListAddress_1f8003d8.get());
        linkedListAddress_1f8003d8.addu(0x18L);
      }
    }

    //LAB_800ec080
  }

  @Method(0x800ec0b0L)
  public static void FUN_800ec0b0(final GsDOBJ2 dobj2) {
    final TmdObjTable tmd = dobj2.tmd_08.deref();
    long primitives = tmd.primitives_10.getPointer();
    long count = tmd.n_primitive_14.get();
    long vertices = tmd.vert_top_00.get();
    long normals = tmd.normal_top_08.get();

    //LAB_800ec0ec
    while(count != 0) {
      final long primitiveCount = MEMORY.ref(2, primitives).get();
      count = count - MEMORY.ref(2, primitives).get();

      _1f8003ee.setu(MEMORY.ref(4, primitives).offset(0x0L).get() >>> 25 & 0x1L);

      final long command = MEMORY.ref(4, primitives).get() & 0xfd04_0000L;
      if(command == 0x3000_0000L) {
        //LAB_800ec190
        primitives = FUN_800edb4c(primitives, vertices, normals, primitiveCount);
      } else if(command == 0x3004_0000L) {
        //LAB_800ec1a4
        primitives = FUN_800ed414(primitives, vertices, normals, primitiveCount);
        //LAB_800ec14c
      } else if(command == 0x3400_0000L) {
        //LAB_800ec1bc
        primitives = FUN_800edd54(primitives, vertices, normals, primitiveCount);
      } else if(command == 0x3500_0000L) {
        //LAB_800ec1d0
        primitives = FUN_800ed90c(primitives, vertices, primitiveCount);
        //LAB_800ec15c
      } else if(command == 0x3800_0000L) {
        //LAB_800ec1e4
        primitives = FUN_800ecee8(primitives, vertices, normals, primitiveCount);
      } else if(command == 0x3804_0000L) {
        //LAB_800ec1f8
        primitives = FUN_800ed160(primitives, vertices, normals, primitiveCount);
        //LAB_800ec180
      } else if(command == 0x3c00_0000L) {
        //LAB_800ec210
        primitives = FUN_800edf80(primitives, vertices, normals, primitiveCount);
      } else {
        //LAB_800ec224
        primitives = FUN_800ed67c(primitives, vertices, primitiveCount);
      }

      //LAB_800ec234
    }

    //LAB_800ec23c
  }

  @Method(0x800ec258L)
  public static void FUN_800ec258(final BigStruct a0) {
    final BigStruct s2 = bigStruct_800bda10;

    GsInitCoordinate2(a0.coord2_14, s2.coord2_14);

    if(a0.ub_cc.get() != 0x3L) {
      s2.coord2_14.coord.transfer.setX(a0.vector_118.getX());

      if(a0.ub_cc.get() == 0x1L) {
        s2.coord2_14.coord.transfer.setY(a0.vector_118.getY());
      } else {
        //LAB_800ec2bc
        s2.coord2_14.coord.transfer.setY(a0.vector_118.getY() - (a0.coord2_14.coord.transfer.getY() << 12) / a0.scaleVector_fc.getY());
      }

      //LAB_800ec2e0
      s2.coord2_14.coord.transfer.setZ(a0.vector_118.getZ());
    } else {
      //LAB_800ec2ec
      s2.coord2_14.coord.transfer.setX(a0.vector_118.getX() + a0.coord2ArrPtr_04.deref().get(a0.ub_cd.get()).coord.transfer.getX());
      s2.coord2_14.coord.transfer.setY(a0.vector_118.getY() - (a0.coord2_14.coord.transfer.getY() << 12) / a0.scaleVector_fc.getY());
      s2.coord2_14.coord.transfer.setZ(a0.vector_118.getZ() + a0.coord2ArrPtr_04.deref().get(a0.ub_cd.get()).coord.transfer.getZ());
    }

    //LAB_800ec370
    s2.us_a0.set((short)(a0.us_a0.get() + 0x10));
    s2.scaleVector_fc.setX(a0.vector_10c.getX() / 4);
    s2.scaleVector_fc.setY(a0.vector_10c.getY() / 4);
    s2.scaleVector_fc.setZ(a0.vector_10c.getZ() / 4);
    RotMatrix_8003faf0(s2.coord2Param_64.rotate, s2.coord2_14.coord);
    final VECTOR scale = new VECTOR().set(s2.scaleVector_fc);
    ScaleMatrixL(s2.coord2_14.coord, scale);
    s2.coord2_14.flg.set(0);
    final GsCOORDINATE2 v0 = s2.dobj2ArrPtr_00.deref().get(0).coord2_04.deref();
    final GsCOORD2PARAM s0 = v0.param.deref();
    s0.rotate.set((short)0, (short)0, (short)0);
    RotMatrix_80040780(s0.rotate, v0.coord);
    s0.trans.set(0, 0, 0);
    TransMatrix(v0.coord, s0.trans);

    final MATRIX sp0x30 = new MATRIX();
    final MATRIX sp0x10 = new MATRIX();
    GsGetLws(s2.ObjTable_0c.top.deref().get(0).coord2_04.deref(), sp0x30, sp0x10);
    GsSetLightMatrix(sp0x30);
    CPU.CTC2((sp0x10.get(1) & 0xffffL) << 16 | sp0x10.get(0) & 0xffffL, 0);
    CPU.CTC2((sp0x10.get(3) & 0xffffL) << 16 | sp0x10.get(2) & 0xffffL, 1);
    CPU.CTC2((sp0x10.get(5) & 0xffffL) << 16 | sp0x10.get(4) & 0xffffL, 2);
    CPU.CTC2((sp0x10.get(7) & 0xffffL) << 16 | sp0x10.get(6) & 0xffffL, 3);
    CPU.CTC2(                                  sp0x10.get(8) & 0xffffL, 4);
    CPU.CTC2(sp0x10.transfer.getX(), 5);
    CPU.CTC2(sp0x10.transfer.getY(), 6);
    CPU.CTC2(sp0x10.transfer.getZ(), 7);
    FUN_800ec0b0(s2.ObjTable_0c.top.deref().get(0));
    s2.coord2ArrPtr_04.deref().get(0).flg.decr();
  }

  @Method(0x800ec4bcL)
  public static void FUN_800ec4bc() {
    _800c6958.setu(addToLinkedListTail(0x1800L));
  }

  @Method(0x800ec51cL)
  public static void FUN_800ec51c(final BattleRenderStruct a0) {
    //LAB_800ec548
    for(int i = 0; i < 10; i++) {
      if(a0._618.get(i).get() != 0) {
        FUN_800ebd34(a0, i);
      }

      //LAB_800ec560
    }

    _1f8003ec.setu(0);
    _1f8003e8.setu(a0._5e8.get());

    //LAB_800ec5a0
    long s4 = 0x1L;
    for(int i = 0; i < a0.objtable2_550.nobj.get(); i++) {
      final GsDOBJ2 dobj2 = a0.objtable2_550.top.deref().get(i);
      if((s4 & a0._5e4.get()) == 0) {
        final MATRIX ls = new MATRIX();
        final MATRIX lw = new MATRIX();
        GsGetLws(dobj2.coord2_04.deref(), lw, ls);
        GsSetLightMatrix(lw);
        CPU.CTC2((ls.get(1) & 0xffff) << 16 | ls.get(0) & 0xffff, 0);
        CPU.CTC2((ls.get(3) & 0xffff) << 16 | ls.get(2) & 0xffff, 1);
        CPU.CTC2((ls.get(5) & 0xffff) << 16 | ls.get(4) & 0xffff, 2);
        CPU.CTC2((ls.get(7) & 0xffff) << 16 | ls.get(6) & 0xffff, 3);
        CPU.CTC2(                             ls.get(8) & 0xffff, 4);
        CPU.CTC2(ls.transfer.getX(), 5);
        CPU.CTC2(ls.transfer.getY(), 6);
        CPU.CTC2(ls.transfer.getZ(), 7);
        FUN_800ec0b0(dobj2);
      }

      //LAB_800ec608
      s4 = s4 << 1;
    }

    //LAB_800ec618
  }

  @Method(0x800ec63cL)
  public static void FUN_800ec63c(final BattleRenderStruct a0) {
    //LAB_800ec688
    for(int i = 0; i < a0.rotTransCount_5dc.get(); i++) {
      final RotateTranslateStruct rotTrans = a0.rotTrans_5d8.deref().get(i);
      final GsCOORDINATE2 coord2 = a0.dobj2s_00.get(i).coord2_04.deref();
      GsCOORD2PARAM param = coord2.param.deref();

      param.rotate.set(rotTrans.rotate_00);
      RotMatrix_80040010(param.rotate, coord2.coord);

      param.trans.set(rotTrans.translate_06);
      TransMatrix(coord2.coord, param.trans);
    }

    //LAB_800ec710
    a0.rotTrans_5d8.set(a0.rotTrans_5d8.deref().slice(a0.rotTransCount_5dc.get()));
  }

  @Method(0x800ec744L)
  public static void FUN_800ec744(final BattleRenderStruct a0) {
    RotMatrix_8003faf0(a0.param_5a8.rotate, a0.coord2_558.coord);
    a0.coord2_558.flg.set(0);
  }

  @Method(0x800ec774L)
  public static void FUN_800ec774(final BattleRenderStruct a0, final TmdAnimationFile a1) {
    a0.rotTrans_5d4.set(a1.rotateTranslateArr_10);
    a0.rotTrans_5d8.set(a1.rotateTranslateArr_10);
    a0.rotTransCount_5dc.set((short)a1.count_0c.get());
    a0._5de.set(a1._0e.get());
    a0._5e0.set(0);
    FUN_800ec63c(a0);
    a0._5e0.set(1);
    a0._5e2.set(a0._5de.get());
    a0.rotTrans_5d8.set(a0.rotTrans_5d4.deref());
  }

  @Method(0x800ec7e4L)
  public static long FUN_800ec7e4(final BigStruct a0, final short x, final short y, final short z) {
    final MATRIX sp0x20 = new MATRIX();
    GsGetLs(a0.coord2_14, sp0x20);
    setRotTransMatrix(sp0x20);

    final SVECTOR vxyz0 = new SVECTOR().set(x, y, z);
    final SVECTOR sxy2 = new SVECTOR();
    FUN_8003f900(vxyz0, sxy2, new Ref<>(), new Ref<>());
    return sxy2.getXY();
  }

  @Method(0x800ec86cL)
  public static void FUN_800ec86c(final BattleRenderStruct a0, final int index) {
    final long a2 = a0._5f0.get(index).get();

    if(a2 == 0) {
      a0._618.get(index).set(0);
      return;
    }

    //LAB_800ec890
    if(MEMORY.ref(2, a2).get() == 0xffffL) {
      a0._5f0.get(index).set(0);
      return;
    }

    //LAB_800ec8a8
    a0._618.get(index).set(1);
    a0._622.get(index).set((int)MEMORY.ref(2, a2).offset(0xaL).get());
    a0._64a.get(index).set((short)0);
    a0._65e.get(index).set((short)1);
  }

  @Method(0x800ec8d0L)
  public static void FUN_800ec8d0(long a0) {
    final long t2 = _800c6958.get();

    //LAB_800ec8ec
    for(int a3 = 0; a3 < 0x40; a3++) {
      //LAB_800ec8f4
      for(int a1 = 0; a1 < 0x10; a1++) {
        MEMORY.ref(2, t2).offset(_800fb148.get(a3).get() * 0x20L).offset(a1 * 0x2L).setu(MEMORY.ref(2, a0).offset(0x14L).offset(a1 * 0x2L).get());
      }
    }

    if(MEMORY.ref(2, a0).offset(0x8812L).offset(0x0L).get() == 0x7422L) {
      _800c695c.setu(MEMORY.ref(2, a0).offset(0x8812L).offset(0x4L).get());
    } else {
      //LAB_800ec954
      _800c695c.setu(0x3fL);
    }

    //LAB_800ec95c
    _800c695c.addu(0x1L);
  }

  @Method(0x800ec974L)
  public static void FUN_800ec974(final BigStruct a0) {
    _1f8003ec.setu(a0.ui_108.get());
    _1f8003e8.setu(a0.us_a0.get());

    //LAB_800ec9d0
    long s6 = a0.ui_f4.get();
    long s0 = 0x1L;
    for(int i = 0; i < a0.ObjTable_0c.nobj.get(); i++) {
      final GsDOBJ2 s2 = a0.ObjTable_0c.top.deref().get(i);

      if((s0 & s6) == 0) {
        final MATRIX sp0x30 = new MATRIX();
        final MATRIX sp0x10 = new MATRIX();
        GsGetLws(s2.coord2_04.deref(), sp0x30, sp0x10);
        GsSetLightMatrix(sp0x30);
        CPU.CTC2((sp0x10.get(1) & 0xffffL) << 16 | sp0x10.get(0) & 0xffffL, 0);
        CPU.CTC2((sp0x10.get(3) & 0xffffL) << 16 | sp0x10.get(2) & 0xffffL, 1);
        CPU.CTC2((sp0x10.get(5) & 0xffffL) << 16 | sp0x10.get(4) & 0xffffL, 2);
        CPU.CTC2((sp0x10.get(7) & 0xffffL) << 16 | sp0x10.get(6) & 0xffffL, 3);
        CPU.CTC2(                                  sp0x10.get(8) & 0xffffL, 4);
        CPU.CTC2(sp0x10.transfer.getX(), 5);
        CPU.CTC2(sp0x10.transfer.getY(), 6);
        CPU.CTC2(sp0x10.transfer.getZ(), 7);
        FUN_800ec0b0(s2);
      }

      //LAB_800eca38
      s0 = s0 << 1;
      if(s0 == 0) {
        s0 = 0x1L;
        s6 = a0.ui_f8.get();
      }

      //LAB_800eca4c
    }

    //LAB_800eca58
    if(a0.ub_cc.get() != 0) {
      FUN_800ec258(a0);
    }

    //LAB_800eca70
  }

  @Method(0x800eca98L)
  public static void FUN_800eca98(long a0, long a1) {
    long v0;
    long v1;
    long s3 = a0;
    long s1 = 0;
    long s4 = s1;
    if(a1 != -0x1L) {
      if(s3 == 0) {
        //LAB_800ecb00
        s1 = _8006e398.offset(0xe40L).offset(a1 * 0x4L).get();
      } else if(s3 == 0x1L) {
        //LAB_800ecb1c
        s1 = _8006e398.offset(0xebcL).offset(a1 * 0x4L).get();
        //LAB_800ecaf0
      } else if(s3 == 0x2L) {
        //LAB_800ecb38
        s1 = _8006e398.offset(0xe0cL).offset(a1 * 0x4L).get();
      }

      //LAB_800ecb50
      //LAB_800ecb54
      final BtldScriptData27c a3 = scriptStatePtrArr_800bc1c0.get((int)s1).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
      v1 = a3._10.get();
      a0 = a3._08.get();
      v0 = v1 >>> 1;
      v0 = v0 < a0 ? 1 : 0;
      v1 = v1 >>> 2;
      if(v1 < a0) {
        a1 = v0 ^ 0x1L;
      } else {
        a1 = 0x2L;
      }

      //LAB_800ecb90
      FUN_800eccfc(a3._148, a1, s1, a3);
    } else {
      //LAB_800ecba4
      if(s3 == 0) {
        //LAB_800ecbdc
        s4 = _800c677c.get();
      } else if(s3 == 0x1L) {
        //LAB_800ecbec
        s4 = _800c6758.get();
        //LAB_800ecbc8
      } else if(s3 == 0x2L) {
        //LAB_800ecbfc
        s4 = _800c669c.get();
      }

      //LAB_800ecc04
      //LAB_800ecc1c
      for(int i = 0; i < s4; i++) {
        final long s0 = _8006e398.offset(i * 0x4L).getAddress();

        if(s3 == 0) {
          //LAB_800ecc50
          s1 = MEMORY.ref(4, s0).offset(0xe40L).get();
        } else if(s3 == 0x1L) {
          //LAB_800ecc5c
          s1 = MEMORY.ref(4, s0).offset(0xebcL).get();
          //LAB_800ecc40
        } else if(s3 == 0x2L) {
          //LAB_800ecc68
          s1 = MEMORY.ref(4, s0).offset(0xe78L).get();
        }

        //LAB_800ecc74
        //LAB_800ecc78
        final ScriptState<BtldScriptData27c> state = scriptStatePtrArr_800bc1c0.get((int)s1).derefAs(ScriptState.classFor(BtldScriptData27c.class));
        final BtldScriptData27c data = state.innerStruct_00.deref();

        v1 = data._10.get();
        a0 = data._08.get();
        v0 = v1 >>> 1;
        v0 = v0 < a0 ? 1 : 0;
        v1 = v1 >>> 2;
        if(v1 < a0) {
          a1 = v0 ^ 0x1L;
        } else {
          a1 = 0x2L;
        }

        //LAB_800eccac
        if((state.ui_60.get() & 0x4000L) == 0) {
          FUN_800eccfc(data._148, a1, s1, data);
        }

        //LAB_800eccc8
      }
    }

    //LAB_800eccd8
  }

  @Method(0x800eccfcL)
  public static void FUN_800eccfc(final BigStruct a0, long a1, long a2, final BtldScriptData27c data) {
    long v0;
    long v1;
    long s0;
    long a3;
    v0 = 0x800c_0000L;
    v0 = v0 + -0x3e40L;
    a2 = a2 << 2;
    a2 = a2 + v0;
    v0 = MEMORY.ref(4, a2).offset(0x0L).get();
    s0 = a1;
    v1 = MEMORY.ref(4, v0).offset(0x60L).get();

    v0 = v1 & 0x4L;
    if(v0 != 0) {
      v1 = data._78.getX();
      v0 = v1 << 1;
      v0 = v0 + v1;
      v0 = v0 << 3;
      v0 = v0 + v1;
      v0 = v0 << 2;
      v1 = data._78.getY();
      a3 = -v0;
      v0 = v1 << 1;
      v0 = v0 + v1;
      v0 = v0 << 3;
      v0 = v0 + v1;
      v0 = v0 << 2;
      v1 = data._78.getZ();
      a2 = -v0;
      v0 = v1 << 1;
      v0 = v0 + v1;
      v0 = v0 << 3;
      v0 = v0 + v1;
      v0 = v0 << 2;
      v0 = -v0;
    } else {
      //LAB_800ecd90
      v0 = v1 & 0x2L;
      if(v0 != 0) {
        v0 = 0;
        a2 = -0x680L;
      } else {
        v0 = 0;

        //LAB_800ecda4
        a2 = -0x580L;
      }

      //LAB_800ecda8
      a3 = v0;
    }

    //LAB_800ecdac
    a1 = v0 << 16;
    a1 = (int)a1 >> 16;
    a2 = a2 << 16;
    a3 = a3 << 16;
    a2 = (int)a2 >> 16;
    a3 = (int)a3 >> 16;
    v0 = FUN_800ec7e4(a0, (short)a1, (short)a2, (short)a3);
    a2 = 0x6680_0000L;
    a1 = linkedListAddress_1f8003d8.get();
    a2 = a2 | 0x8080L;
    v1 = a1 + 0x14L;
    linkedListAddress_1f8003d8.setu(v1);
    v1 = 0xf0L;
    MEMORY.ref(1, a1).offset(0xcL).setu(v1);
    v1 = 0x10L;
    MEMORY.ref(2, a1).offset(0x10L).setu(v1);
    v1 = 0x18L;
    MEMORY.ref(2, a1).offset(0x12L).setu(v1);
    v1 = v0 + -0x8L;
    MEMORY.ref(2, a1).offset(0x8L).setu(v1);
    v1 = 0x800c_0000L;
    MEMORY.ref(1, a1).offset(0xdL).setu(0);
    v1 = MEMORY.ref(4, v1).offset(-0x4f04L).get();
    v0 = (int)v0 >> 16;
    v1 = v1 & 0x7L;
    MEMORY.ref(1, a1).offset(0x3L).setu(0x4L);
    MEMORY.ref(4, a1).offset(0x4L).setu(a2);
    MEMORY.ref(1, a1).offset(0x4L).setu(0x80L);
    MEMORY.ref(1, a1).offset(0x5L).setu(0x80L);
    MEMORY.ref(1, a1).offset(0x6L).setu(0x80L);
    MEMORY.ref(2, a1).offset(0xaL).setu(_800fb188.offset(2, v1 * 0x2L).get() + v0);

    if(s0 == 0) {
      //LAB_800ece80
      MEMORY.ref(2, a1).offset(0xeL).setu(0x7fadL);
    } else if(s0 == 0x1L) {
      //LAB_800ece88
      MEMORY.ref(2, a1).offset(0xeL).setu(0x7fedL);
      //LAB_800ece70
    } else if(s0 == 0x2L) {
      //LAB_800ece90
      //LAB_800ece94
      MEMORY.ref(2, a1).offset(0xeL).setu(0x7c2eL);
    }

    //LAB_800ece9c
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x70L, a1);
    a1 = linkedListAddress_1f8003d8.get();
    MEMORY.ref(1, a1).offset(0x3L).setu(0x1L);
    MEMORY.ref(4, a1).offset(0x4L).setu(0xe100_021bL);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x70L, a1);
    linkedListAddress_1f8003d8.addu(0x8L);
  }

  @Method(0x800ecee8L)
  public static long FUN_800ecee8(long a0, long a1, long a2, long a3) {
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
    t4 = a0;
    t7 = 0x1f80_0000L;
    t0 = MEMORY.ref(4, t7).offset(0x3d8L).get();
    t6 = MEMORY.ref(4, t7).offset(0x3ecL).get();
    s8 = MEMORY.ref(4, t7).offset(0x3d0L).get();
    s0 = MEMORY.ref(4, t7).offset(0x3e8L).get();
    t9 = (int)t6 >> 15;
    if(a3 == 0) {
      v0 = 0x1f80_0000L;
    } else {
      v0 = 0x1f80_0000L;
      s2 = v0 + 0x3c0L;
      v0 = 0x1f80_0000L;
      s1 = v0 + 0x3c8L;
      t8 = 0xff_0000L;
      t8 = t8 | 0xffffL;
      a0 = a0 + 0x14L;

      //LAB_800ecf34
      do {
        t5 = MEMORY.ref(2, t4).offset(0xaL).get();
        t6 = MEMORY.ref(2, t4).offset(0xeL).get();
        t7 = MEMORY.ref(2, t4).offset(0x12L).get();
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


        CPU.COP2(0x280030L);
        a3 = a3 - 0x1L;
        t3 = CPU.CFC2(31);

        if((int)t3 >= 0) {
          CPU.COP2(0x1400006L);
          t3 = CPU.MFC2(24);
          //LAB_800ecfb0
          if(t9 == 0 && (int)t3 > 0 || t9 != 0 && t3 != 0) {
            //LAB_800ecfb8
            MEMORY.ref(4, t0).offset(0x8L).setu(CPU.MFC2(12));
            MEMORY.ref(4, t0).offset(0x10L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t0).offset(0x18L).setu(CPU.MFC2(14));
            v0 = MEMORY.ref(2, a0).offset(0x2L).get();

            v0 = v0 << 3;
            v0 = a1 + v0;
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);


            CPU.COP2(0x180001L);
            t3 = CPU.CFC2(31);

            if((int)t3 < 0) {
              v0 = t0 + 0x20L;
            } else {
              v0 = t0 + 0x20L;
              MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(14));


              CPU.COP2(0x168002eL);
              t1 = CPU.MFC2(7);
              v0 = MEMORY.ref(4, s2).offset(0x4L).get();
              t1 = t1 + s0;
              t1 = (int)t1 >> v0;
              if((int)t1 >= 0xbL) {
                v1 = MEMORY.ref(4, s1).offset(0x4L).get();

                if((int)t1 >= (int)v1) {
                  t1 = v1;
                }
                v0 = t1 << 2;

                //LAB_800ed040
                t2 = s8 + v0;
                v0 = t4 + 0x4L;
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
                t5 = MEMORY.ref(2, t4).offset(0x8L).get();
                t6 = MEMORY.ref(2, t4).offset(0xcL).get();
                t7 = MEMORY.ref(2, t4).offset(0x10L).get();
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


                CPU.COP2(0x118043fL);
                MEMORY.ref(4, t0).offset(0x4L).setu(CPU.MFC2(20));
                MEMORY.ref(4, t0).offset(0xcL).setu(CPU.MFC2(21));
                MEMORY.ref(4, t0).offset(0x14L).setu(CPU.MFC2(22));
                v0 = MEMORY.ref(4, t2).offset(0x0L).get();
                v1 = 0x800_0000L;
                v0 = v0 & t8;
                v0 = v0 | v1;
                MEMORY.ref(4, t0).offset(0x0L).setu(v0);
                v0 = MEMORY.ref(2, a0).offset(0x0L).get();

                v0 = v0 << 3;
                v0 = a2 + v0;
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);


                CPU.COP2(0x108041bL);
                v0 = t0 & t8;
                MEMORY.ref(4, t2).offset(0x0L).setu(v0);
                v0 = t0 + 0x1cL;
                MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(22));
                if(t9 == 0) {
                  t0 = t0 + 0x24L;
                } else {
                  t0 = t0 + 0x24L;
                  v1 = 0xe100_0000L;
                  v0 = 0x1L;
                  MEMORY.ref(1, t0).offset(0x3L).setu(v0);
                  v0 = 0x1f80_0000L;
                  v0 = MEMORY.ref(2, v0).offset(0x3ecL).get();
                  v1 = v1 | 0x200L;
                  v0 = v0 & 0x9ffL;
                  v0 = v0 | v1;
                  MEMORY.ref(4, t0).offset(0x4L).setu(v0);
                  v0 = MEMORY.ref(4, t2).offset(0x0L).get();
                  v1 = 0x100_0000L;
                  v0 = v0 & t8;
                  v0 = v0 | v1;
                  MEMORY.ref(4, t0).offset(0x0L).setu(v0);
                  v0 = t0 & t8;
                  t0 = t0 + 0x8L;
                  MEMORY.ref(4, t2).offset(0x0L).setu(v0);
                }
              }
            }
          }
        }

        //LAB_800ed134
        a0 = a0 + 0x18L;

        //LAB_800ed138
        t4 = t4 + 0x18L;
      } while(a3 != 0);
    }

    //LAB_800ed140
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t0);
    v0 = t4;
    return v0;
  }

  @Method(0x800ed160L)
  public static long FUN_800ed160(long a0, long a1, long a2, long a3) {
    long at;
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
    long s6;
    long t8;
    long t9;
    long s8;
    long sp;
    long ra;
    long sp0;
    long sp4;
    long sp8;
    t3 = a0;
    t7 = 0x1f80_0000L;
    t1 = MEMORY.ref(4, t7).offset(0x3d8L).get();
    t6 = MEMORY.ref(4, t7).offset(0x3ecL).get();
    s8 = MEMORY.ref(4, t7).offset(0x3d0L).get();
    s0 = MEMORY.ref(4, t7).offset(0x3e8L).get();
    t9 = (int)t6 >> 15;
    if(a3 == 0) {
      v0 = 0x1f80_0000L;
    } else {
      v0 = 0x1f80_0000L;
      s2 = v0 + 0x3c0L;
      v0 = 0x1f80_0000L;
      s1 = v0 + 0x3c8L;
      t8 = 0xff_0000L;
      t8 = t8 | 0xffffL;
      a0 = a0 + 0x20L;
      t4 = t1 + 0x4L;

      //LAB_800ed1b0
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


        CPU.COP2(0x280030L);
        a3 = a3 - 0x1L;
        t0 = CPU.CFC2(31);

        if((int)t0 >= 0) {
          CPU.COP2(0x1400006L);
          t0 = CPU.MFC2(24);
          //LAB_800ed22c
          if(t9 == 0 && (int)t0 > 0 || t9 != 0 && t0 != 0) {
            //LAB_800ed234
            MEMORY.ref(4, t1).offset(0x8L).setu(CPU.MFC2(12));
            MEMORY.ref(4, t1).offset(0x10L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t1).offset(0x18L).setu(CPU.MFC2(14));
            v0 = MEMORY.ref(2, a0).offset(0x2L).get();

            v0 = v0 << 3;
            v0 = a1 + v0;
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);


            CPU.COP2(0x180001L);
            t0 = CPU.CFC2(31);

            if((int)t0 < 0) {
              v0 = t1 + 0x20L;
            } else {
              v0 = t1 + 0x20L;
              MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(14));


              CPU.COP2(0x168002eL);
              t0 = CPU.MFC2(7);
              v0 = MEMORY.ref(4, s2).offset(0x4L).get();
              t0 = t0 + s0;
              t0 = (int)t0 >> v0;
              if((int)t0 < 0xbL) {
                v0 = t3 + 0x4L;
              } else {
                v0 = t3 + 0x4L;
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
                v0 = MEMORY.ref(2, a0).offset(-0xcL).get();

                v0 = v0 << 3;
                v0 = a2 + v0;
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
                v1 = MEMORY.ref(4, s1).offset(0x4L).get();

                if((int)t0 >= (int)v1) {
                  t0 = v1;
                }

                //LAB_800ed2d4
                CPU.COP2(0x108041bL);
                MEMORY.ref(4, t4).offset(0x0L).setu(CPU.MFC2(22));
                v0 = t3 + 0x8L;
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
                v0 = MEMORY.ref(2, a0).offset(-0x8L).get();

                v0 = v0 << 3;
                v0 = a2 + v0;
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
                v0 = t0 << 2;
                t2 = s8 + v0;
                CPU.COP2(0x108041bL);
                v0 = t1 + 0xcL;
                MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(22));
                v0 = t3 + 0xcL;
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
                v0 = MEMORY.ref(2, a0).offset(-0x4L).get();

                v0 = v0 << 3;
                v0 = a2 + v0;
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);


                CPU.COP2(0x108041bL);
                v0 = t1 + 0x14L;
                MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(22));
                v0 = t3 + 0x10L;
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
                v0 = MEMORY.ref(2, a0).offset(0x0L).get();

                v0 = v0 << 3;
                v0 = a2 + v0;
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);


                CPU.COP2(0x108041bL);
                v0 = t1 + 0x1cL;
                MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(22));
                t4 = t4 + 0x24L;
                v1 = MEMORY.ref(4, t2).offset(0x0L).get();
                v0 = 0x800_0000L;
                v1 = v1 & t8;
                v1 = v1 | v0;
                v0 = t1 & t8;
                MEMORY.ref(4, t1).offset(0x0L).setu(v1);
                t1 = t1 + 0x24L;
                if(t9 == 0) {
                  MEMORY.ref(4, t2).offset(0x0L).setu(v0);
                } else {
                  MEMORY.ref(4, t2).offset(0x0L).setu(v0);
                  v1 = 0xe100_0000L;
                  v0 = 0x1L;
                  MEMORY.ref(1, t4).offset(-0x1L).setu(v0);
                  v0 = 0x1f80_0000L;
                  v0 = MEMORY.ref(2, v0).offset(0x3ecL).get();
                  v1 = v1 | 0x200L;
                  v0 = v0 & 0x9ffL;
                  v0 = v0 | v1;
                  MEMORY.ref(4, t4).offset(0x0L).setu(v0);
                  t4 = t4 + 0x8L;
                  v1 = MEMORY.ref(4, t2).offset(0x0L).get();
                  v0 = 0x100_0000L;
                  v1 = v1 & t8;
                  v1 = v1 | v0;
                  v0 = t1 & t8;
                  MEMORY.ref(4, t1).offset(0x0L).setu(v1);
                  t1 = t1 + 0x8L;
                  MEMORY.ref(4, t2).offset(0x0L).setu(v0);
                }
              }
            }
          }
        }

        //LAB_800ed3e8
        a0 = a0 + 0x24L;

        //LAB_800ed3ec
        t3 = t3 + 0x24L;
      } while(a3 != 0);
    }

    //LAB_800ed3f4
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t1);
    v0 = t3;
    return v0;
  }

  @Method(0x800ed414L)
  public static long FUN_800ed414(long a0, long a1, long a2, long a3) {
    long at;
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
    long s3;
    long s6;
    long t8;
    long t9;
    long s8;
    long sp;
    long ra;
    long sp0;
    long sp4;
    long spc;
    long sp8;
    t4 = a0;
    s3 = a1;
    t7 = 0x1f80_0000L;
    t0 = MEMORY.ref(4, t7).offset(0x3d8L).get();
    t6 = MEMORY.ref(4, t7).offset(0x3ecL).get();
    s0 = MEMORY.ref(4, t7).offset(0x3d0L).get();
    s1 = MEMORY.ref(4, t7).offset(0x3e8L).get();
    s8 = (int)t6 >> 15;
    if(a3 == 0) {
      v0 = 0x1f80_0000L;
    } else {
      v0 = 0x1f80_0000L;
      s2 = v0 + 0x3c0L;
      v0 = 0x1f80_0000L;
      a1 = v0 + 0x3c8L;
      t9 = 0xff_0000L;
      t9 = t9 | 0xffffL;
      a0 = a0 + 0x18L;
      t3 = t0 + 0x4L;

      //LAB_800ed46c
      do {
        t5 = MEMORY.ref(2, t4).offset(0x12L).get();
        t6 = MEMORY.ref(2, t4).offset(0x16L).get();
        t7 = MEMORY.ref(2, t4).offset(0x1aL).get();
        t5 = t5 << 3;
        t6 = t6 << 3;
        t7 = t7 << 3;
        t5 = s3 + t5;
        t6 = s3 + t6;
        t7 = s3 + t7;
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
          //LAB_800ed4e8
          if(s8 == 0 && (int)t8 > 0 || s8 != 0 && t8 != 0) {
            //LAB_800ed4f0
            MEMORY.ref(4, t0).offset(0x8L).setu(CPU.MFC2(12));
            MEMORY.ref(4, t0).offset(0x10L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t0).offset(0x18L).setu(CPU.MFC2(14));


            CPU.COP2(0x158002dL);
            t1 = CPU.MFC2(7);
            v0 = MEMORY.ref(4, s2).offset(0x4L).get();
            t1 = t1 + s1;
            t1 = (int)t1 >> v0;
            if((int)t1 >= 0xbL) {
              v1 = MEMORY.ref(4, a1).offset(0x4L).get();

              if((int)t1 >= (int)v1) {
                t1 = v1;
              }
              v0 = t1 << 2;

              //LAB_800ed540
              t2 = s0 + v0;
              v0 = t4 + 0x4L;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
              v0 = MEMORY.ref(2, a0).offset(-0x8L).get();

              v0 = v0 << 3;
              v0 = a2 + v0;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);


              CPU.COP2(0x108041bL);
              MEMORY.ref(4, t3).offset(0x0L).setu(CPU.MFC2(22));
              v0 = t4 + 0x8L;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
              v0 = MEMORY.ref(2, a0).offset(-0x4L).get();

              v0 = v0 << 3;
              v0 = a2 + v0;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);


              CPU.COP2(0x108041bL);
              v0 = t0 + 0xcL;
              MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(22));
              v0 = t4 + 0xcL;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
              v0 = MEMORY.ref(2, a0).offset(0x0L).get();

              v0 = v0 << 3;
              v0 = a2 + v0;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);


              CPU.COP2(0x108041bL);
              v0 = t0 + 0x14L;
              MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(22));
              t3 = t3 + 0x1cL;
              v1 = MEMORY.ref(4, t2).offset(0x0L).get();
              v0 = 0x600_0000L;
              v1 = v1 & t9;
              v1 = v1 | v0;
              v0 = t0 & t9;
              MEMORY.ref(4, t0).offset(0x0L).setu(v1);
              t0 = t0 + 0x1cL;
              if(s8 == 0) {
                MEMORY.ref(4, t2).offset(0x0L).setu(v0);
              } else {
                MEMORY.ref(4, t2).offset(0x0L).setu(v0);
                v1 = 0xe100_0000L;
                v0 = 0x1L;
                MEMORY.ref(1, t3).offset(-0x1L).setu(v0);
                v0 = 0x1f80_0000L;
                v0 = MEMORY.ref(2, v0).offset(0x3ecL).get();
                v1 = v1 | 0x200L;
                v0 = v0 & 0x9ffL;
                v0 = v0 | v1;
                MEMORY.ref(4, t3).offset(0x0L).setu(v0);
                t3 = t3 + 0x8L;
                v1 = MEMORY.ref(4, t2).offset(0x0L).get();
                v0 = 0x100_0000L;
                v1 = v1 & t9;
                v1 = v1 | v0;
                v0 = t0 & t9;
                MEMORY.ref(4, t0).offset(0x0L).setu(v1);
                t0 = t0 + 0x8L;
                MEMORY.ref(4, t2).offset(0x0L).setu(v0);
              }
            }
          }
        }

        //LAB_800ed64c
        a0 = a0 + 0x1cL;

        //LAB_800ed650
        t4 = t4 + 0x1cL;
      } while(a3 != 0);
    }

    //LAB_800ed658
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t0);
    v0 = t4;
    return v0;
  }

  @Method(0x800ed67cL)
  public static long FUN_800ed67c(long a0, long a1, long a2) {
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
    long s2;
    long t8;
    long t9;
    long s8;
    t4 = a0;
    t7 = 0x1f80_0000L;
    t2 = MEMORY.ref(4, t7).offset(0x3d8L).get();
    t6 = MEMORY.ref(4, t7).offset(0x3ecL).get();
    s8 = MEMORY.ref(4, t7).offset(0x3d0L).get();
    s0 = MEMORY.ref(4, t7).offset(0x3e8L).get();
    t7 = (int)t6 >> 16;
    t9 = t7 << 25;
    if(a2 != 0) {
      v0 = 0x1f80_0000L;
      s2 = v0 + 0x3c0L;
      v0 = 0x1f80_0000L;
      s1 = v0 + 0x3c8L;
      t8 = 0xff_0000L;
      t8 = t8 | 0xffffL;
      t0 = a0 + 0x22L;
      a3 = t2 + 0x2aL;

      //LAB_800ed6d0
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
        CPU.COP2(0x280030L);
        v0 = MEMORY.ref(4, t0-0x1eL).get();

        MEMORY.ref(4, a3-0x1eL).setu(v0);
        v0 = MEMORY.ref(4, t0-0x1aL).get();
        a2 = a2 - 0x1L;
        MEMORY.ref(4, a3-0x12L).setu(v0);
        t3 = CPU.CFC2(31);

        if((int)t3 >= 0) {
          CPU.COP2(0x1400006L);
          v0 = MEMORY.ref(4, t0-0x16L).get();

          MEMORY.ref(4, a3-0x6L).setu(v0);
          t3 = CPU.MFC2(24);
          //LAB_800ed76c
          if(t9 == 0 && (int)t3 > 0 || t3 != 0) {
            //LAB_800ed774
            MEMORY.ref(4, t2).offset(0x8L).setu(CPU.MFC2(12));
            MEMORY.ref(4, t2).offset(0x14L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t2).offset(0x20L).setu(CPU.MFC2(14));
            v0 = MEMORY.ref(2, t0).offset(0x8L).get();

            v0 = v0 << 3;
            v0 = a1 + v0;
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);
            CPU.COP2(0x180001L);
            a0 = 0x3c80_0000L;
            a0 = a0 | 0x8080L;
            v1 = MEMORY.ref(4, t0-0x12L).get();
            v0 = 0xcL;
            MEMORY.ref(1, a3).offset(-0x27L).setu(v0);
            MEMORY.ref(4, a3-0x26L).setu(a0);
            MEMORY.ref(4, a3+0x6L).setu(v1);
            t7 = MEMORY.ref(4, t2).offset(0x4L).get();

            t7 = t7 | t9;
            MEMORY.ref(4, t2).offset(0x4L).setu(t7);
            t3 = CPU.CFC2(31);

            if((int)t3 >= 0) {
              v0 = t2 + 0x2cL;
              MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(14));


              CPU.COP2(0x168002eL);
              MEMORY.ref(1, a3).offset(-0x26L).setu(MEMORY.ref(1, t0).offset(-0xeL).get());
              MEMORY.ref(1, a3).offset(-0x25L).setu(MEMORY.ref(1, t0).offset(-0xdL).get());
              MEMORY.ref(1, a3).offset(-0x24L).setu(MEMORY.ref(1, t0).offset(-0xcL).get());
              MEMORY.ref(1, a3).offset(-0x1aL).setu(MEMORY.ref(1, t0).offset(-0xaL).get());
              MEMORY.ref(1, a3).offset(-0x19L).setu(MEMORY.ref(1, t0).offset(-0x9L).get());
              MEMORY.ref(1, a3).offset(-0x18L).setu(MEMORY.ref(1, t0).offset(-0x8L).get());
              MEMORY.ref(1, a3).offset(-0xeL).setu(MEMORY.ref(1, t0).offset(-0x6L).get());
              MEMORY.ref(1, a3).offset(-0xdL).setu(MEMORY.ref(1, t0).offset(-0x5L).get());
              MEMORY.ref(1, a3).offset(-0xcL).setu(MEMORY.ref(1, t0).offset(-0x4L).get());
              MEMORY.ref(1, a3).offset(-0x2L).setu(MEMORY.ref(1, t0).offset(-0x2L).get());
              MEMORY.ref(1, a3).offset(-0x1L).setu(MEMORY.ref(1, t0).offset(-0x1L).get());
              MEMORY.ref(1, a3).offset(0x0L).setu(MEMORY.ref(1, t0).offset(0x0L).get());

              t1 = CPU.MFC2(7);
              v0 = MEMORY.ref(4, s2).offset(0x4L).get();
              t1 = t1 + s0;
              t1 = (int)t1 >> v0;

              if((int)t1 >= 0xbL) {
                v1 = MEMORY.ref(4, s1).offset(0x4L).get();

                if((int)t1 >= (int)v1) {
                  t1 = v1;
                }
                a0 = t1 << 2;

                //LAB_800ed8b8
                a0 = s8 + a0;
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

        //LAB_800ed8e0
        t0 = t0 + 0x2cL;

        //LAB_800ed8e4
        t4 = t4 + 0x2cL;
      } while(a2 != 0);
    }

    //LAB_800ed8ec
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t2);
    v0 = t4;
    return v0;
  }

  @Method(0x800ed90cL)
  public static long FUN_800ed90c(long a0, long a1, long a2) {
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
    long s2;
    long t8;
    long t9;
    long s8;
    t4 = a0;
    s2 = a1;
    t7 = 0x1f80_0000L;
    t2 = MEMORY.ref(4, t7).offset(0x3d8L).get();
    t6 = MEMORY.ref(4, t7).offset(0x3ecL).get();
    s8 = MEMORY.ref(4, t7).offset(0x3d0L).get();
    s0 = MEMORY.ref(4, t7).offset(0x3e8L).get();
    t7 = (int)t6 >> 16;
    t9 = t7 << 25;
    if(a2 != 0) {
      v0 = 0x1f80_0000L;
      s1 = v0 + 0x3c0L;
      v0 = 0x1f80_0000L;
      a1 = v0 + 0x3c8L;
      t8 = 0xff_0000L;
      t8 = t8 | 0xffffL;
      t0 = a0 + 0x1aL;
      a3 = t2 + 0x1eL;

      //LAB_800ed964
      do {
        t5 = MEMORY.ref(2, t4).offset(0x1cL).get();
        t6 = MEMORY.ref(2, t4).offset(0x1eL).get();
        t7 = MEMORY.ref(2, t4).offset(0x20L).get();
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
        v0 = MEMORY.ref(4, t0-0x16L).get();

        MEMORY.ref(4, a3-0x12L).setu(v0);
        v0 = MEMORY.ref(4, t0-0x12L).get();
        a2 = a2 - 0x1L;
        MEMORY.ref(4, a3-0x6L).setu(v0);
        t3 = CPU.CFC2(31);

        if((int)t3 >= 0) {
          CPU.COP2(0x1400006L);
          v0 = MEMORY.ref(4, t0-0xeL).get();

          MEMORY.ref(4, a3+0x6L).setu(v0);
          t3 = CPU.MFC2(24);
          //LAB_800eda00
          if(t9 == 0 && (int)t3 > 0 || t3 != 0) {
            //LAB_800eda08
            MEMORY.ref(4, t2).offset(0x8L).setu(CPU.MFC2(12));
            MEMORY.ref(4, t2).offset(0x14L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t2).offset(0x20L).setu(CPU.MFC2(14));
            v1 = 0x3480_0000L;
            v1 = v1 | 0x8080L;
            v0 = 0x9L;
            MEMORY.ref(1, a3).offset(-0x1bL).setu(v0);
            MEMORY.ref(4, a3-0x1aL).setu(v1);
            t7 = MEMORY.ref(4, t2).offset(0x4L).get();

            t7 = t7 | t9;
            MEMORY.ref(4, t2).offset(0x4L).setu(t7);
            t3 = CPU.CFC2(31);

            if((int)t3 >= 0) {
              CPU.COP2(0x158002dL);
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
              v0 = MEMORY.ref(4, s1).offset(0x4L).get();
              t1 = t1 + s0;
              t1 = (int)t1 >> v0;
              if((int)t1 >= 0xbL) {
                v1 = MEMORY.ref(4, a1).offset(0x4L).get();

                if((int)t1 >= (int)v1) {
                  t1 = v1;
                }
                a0 = t1 << 2;

                //LAB_800edaf8
                a0 = s8 + a0;
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

        //LAB_800edb20
        t0 = t0 + 0x24L;

        //LAB_800edb24
        t4 = t4 + 0x24L;
      } while(a2 != 0);
    }

    //LAB_800edb2c
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t2);
    v0 = t4;
    return v0;
  }

  @Method(0x800edb4cL)
  public static long FUN_800edb4c(long a0, long a1, long a2, long a3) {
    long at;
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
    long s4;
    long s5;
    long s6;
    long t8;
    long t9;
    long s8;
    long sp;
    long ra;
    long sp0;
    long sp4;
    s0 = a1;
    s1 = a2;
    t7 = 0x1f80_0000L;
    t0 = MEMORY.ref(4, t7).offset(0x3d8L).get();
    t6 = MEMORY.ref(4, t7).offset(0x3ecL).get();
    t9 = MEMORY.ref(4, t7).offset(0x3d0L).get();
    s8 = MEMORY.ref(4, t7).offset(0x3e8L).get();
    t8 = (int)t6 >> 15;
    if(a3 == 0) {
      v0 = 0x1f80_0000L;
    } else {
      v0 = 0x1f80_0000L;
      a2 = v0 + 0x3c0L;
      v0 = 0x1f80_0000L;
      a1 = v0 + 0x3c8L;
      t4 = 0xff_0000L;
      t4 = t4 | 0xffffL;

      //LAB_800edb94
      do {
        t5 = MEMORY.ref(2, a0).offset(0xaL).get();
        t6 = MEMORY.ref(2, a0).offset(0xeL).get();
        t7 = MEMORY.ref(2, a0).offset(0x12L).get();
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


        CPU.COP2(0x280030L);
        a3 = a3 - 0x1L;
        t3 = CPU.CFC2(31);

        if((int)t3 >= 0) {
          CPU.COP2(0x1400006L);
          t3 = CPU.MFC2(24);

          //LAB_800edc10
          if(t8 == 0 && t3 > 0 || t8 != 0 && t3 != 0) {
            //LAB_800edc18
            MEMORY.ref(4, t0).offset(0x8L).setu(CPU.MFC2(12));
            MEMORY.ref(4, t0).offset(0x10L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t0).offset(0x18L).setu(CPU.MFC2(14));


            CPU.COP2(0x158002dL);
            t1 = CPU.MFC2(7);
            v0 = MEMORY.ref(4, a2).offset(0x4L).get();
            t1 = t1 + s8;
            t1 = (int)t1 >> v0;
            if((int)t1 >= 0xbL) {
              v1 = MEMORY.ref(4, a1).offset(0x4L).get();

              if((int)t1 >= (int)v1) {
                t1 = v1;
              }
              v0 = t1 << 2;

              //LAB_800edc68
              t2 = t9 + v0;
              v0 = a0 + 0x4L;
              CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
              t5 = MEMORY.ref(2, a0).offset(0x8L).get();
              t6 = MEMORY.ref(2, a0).offset(0xcL).get();
              t7 = MEMORY.ref(2, a0).offset(0x10L).get();
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


              CPU.COP2(0x118043fL);
              v0 = MEMORY.ref(4, t2).offset(0x0L).get();
              v1 = 0x600_0000L;
              v0 = v0 & t4;
              v0 = v0 | v1;
              MEMORY.ref(4, t0).offset(0x0L).setu(v0);
              v0 = t0 & t4;
              MEMORY.ref(4, t2).offset(0x0L).setu(v0);
              MEMORY.ref(4, t0).offset(0x4L).setu(CPU.MFC2(20));
              MEMORY.ref(4, t0).offset(0xcL).setu(CPU.MFC2(21));
              MEMORY.ref(4, t0).offset(0x14L).setu(CPU.MFC2(22));
              if(t8 == 0) {
                t0 = t0 + 0x1cL;
              } else {
                t0 = t0 + 0x1cL;
                v1 = 0xe100_0000L;
                v0 = 0x1L;
                MEMORY.ref(1, t0).offset(0x3L).setu(v0);
                v0 = 0x1f80_0000L;
                v0 = MEMORY.ref(2, v0).offset(0x3ecL).get();
                v1 = v1 | 0x200L;
                v0 = v0 & 0x9ffL;
                v0 = v0 | v1;
                MEMORY.ref(4, t0).offset(0x4L).setu(v0);
                v0 = MEMORY.ref(4, t2).offset(0x0L).get();
                v1 = 0x100_0000L;
                v0 = v0 & t4;
                v0 = v0 | v1;
                MEMORY.ref(4, t0).offset(0x0L).setu(v0);
                v0 = t0 & t4;
                t0 = t0 + 0x8L;
                MEMORY.ref(4, t2).offset(0x0L).setu(v0);
              }
            }
          }
        }

        //LAB_800edd30
        a0 = a0 + 0x14L;
      } while(a3 != 0);
    }

    //LAB_800edd38
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t0);
    v0 = a0;
    return v0;
  }

  @Method(0x800edd54L)
  public static long FUN_800edd54(long a0, long a1, long a2, long a3) {
    long at;
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
    long s3;
    long s4;
    long s5;
    long s6;
    long t8;
    long t9;
    long s8;
    long sp;
    long ra;
    long sp0;
    long sp4;
    long spc;
    long sp8;
    t8 = a0;
    s2 = a1;
    s3 = a2;
    t7 = 0x1f80_0000L;
    t0 = MEMORY.ref(4, t7).offset(0x3d8L).get();
    t6 = MEMORY.ref(4, t7).offset(0x3ecL).get();
    s0 = MEMORY.ref(4, t7).offset(0x3d0L).get();
    s1 = MEMORY.ref(4, t7).offset(0x3e8L).get();
    t7 = (int)t6 >> 16;
    s8 = t7 << 25;
    v0 = 0x3480_0000L;
    v0 = v0 | 0x8080L;
    v1 = 0x9L;
    MEMORY.ref(1, t0).offset(0x3L).setu(v1);
    MEMORY.ref(4, t0).offset(0x4L).setu(v0);
    t7 = MEMORY.ref(4, t0).offset(0x4L).get();

    t7 = t7 | s8;
    MEMORY.ref(4, t0).offset(0x4L).setu(t7);
    v0 = t0 + 0x4L;
    CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
    if(a3 == 0) {
      v0 = 0x1f80_0000L;
    } else {
      v0 = 0x1f80_0000L;
      a2 = v0 + 0x3c0L;
      v0 = 0x1f80_0000L;
      a1 = v0 + 0x3c8L;
      t9 = 0xff_0000L;
      t9 = t9 | 0xffffL;
      t3 = a0 + 0xcL;
      t2 = t0 + 0x24L;

      //LAB_800edde0
      do {
        t5 = MEMORY.ref(2, t8).offset(0x12L).get();
        t6 = MEMORY.ref(2, t8).offset(0x16L).get();
        t7 = MEMORY.ref(2, t8).offset(0x1aL).get();
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
        v0 = MEMORY.ref(4, t3).offset(-0x8L).get();

        MEMORY.ref(4, t2).offset(-0x18L).setu(v0);
        v0 = MEMORY.ref(4, t3).offset(-0x4L).get();
        a3 = a3 - 0x1L;
        MEMORY.ref(4, t2).offset(-0xcL).setu(v0);
        t4 = CPU.CFC2(31);

        if((int)t4 >= 0) {
          CPU.COP2(0x1400006L);
          v0 = MEMORY.ref(4, t3).offset(0x0L).get();

          MEMORY.ref(4, t2).offset(0x0L).setu(v0);
          t4 = CPU.MFC2(24);
          //LAB_800ede7c
          if(s8 == 0 && (int)t4 > 0 || s8 != 0 && t4 != 0) {
            //LAB_800ede84
            MEMORY.ref(4, t0).offset(0x8L).setu(CPU.MFC2(12));
            MEMORY.ref(4, t0).offset(0x14L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t0).offset(0x20L).setu(CPU.MFC2(14));


            CPU.COP2(0x158002dL);
            t1 = CPU.MFC2(7);
            v0 = MEMORY.ref(4, a2).offset(0x4L).get();
            t1 = t1 + s1;
            t1 = (int)t1 >> v0;
            if((int)t1 >= 0xbL) {
              v1 = MEMORY.ref(4, a1).offset(0x4L).get();

              if((int)t1 >= (int)v1) {
                t1 = v1;
              }
              a0 = t1 << 2;

              //LAB_800eded4
              a0 = s0 + a0;
              t5 = MEMORY.ref(2, t8).offset(0x10L).get();
              t6 = MEMORY.ref(2, t8).offset(0x14L).get();
              t7 = MEMORY.ref(2, t8).offset(0x18L).get();
              t5 = t5 << 3;
              t6 = t6 << 3;
              t7 = t7 << 3;
              t5 = s3 + t5;
              t6 = s3 + t6;
              t7 = s3 + t7;
              CPU.MTC2(MEMORY.ref(4, t5).offset(0x0L).get(), 0);
              CPU.MTC2(MEMORY.ref(4, t5).offset(0x4L).get(), 1);
              CPU.MTC2(MEMORY.ref(4, t6).offset(0x0L).get(), 2);
              CPU.MTC2(MEMORY.ref(4, t6).offset(0x4L).get(), 3);
              CPU.MTC2(MEMORY.ref(4, t7).offset(0x0L).get(), 4);
              CPU.MTC2(MEMORY.ref(4, t7).offset(0x4L).get(), 5);


              CPU.COP2(0x118043fL);
              MEMORY.ref(4, t0).offset(0x4L).setu(CPU.MFC2(20));
              MEMORY.ref(4, t0).offset(0x10L).setu(CPU.MFC2(21));
              MEMORY.ref(4, t0).offset(0x1cL).setu(CPU.MFC2(22));
              t2 = t2 + 0x28L;
              v1 = MEMORY.ref(4, a0).offset(0x0L).get();
              v0 = 0x900_0000L;
              v1 = v1 & t9;
              v1 = v1 | v0;
              v0 = t0 & t9;
              MEMORY.ref(4, t0).offset(0x0L).setu(v1);
              t0 = t0 + 0x28L;
              MEMORY.ref(4, a0).offset(0x0L).setu(v0);
            }
          }
        }

        //LAB_800edf50
        t3 = t3 + 0x1cL;

        //LAB_800edf54
        t8 = t8 + 0x1cL;
      } while(a3 != 0);
    }

    //LAB_800edf5c
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t0);
    v0 = t8;
    return v0;
  }

  @Method(0x800edf80L)
  public static long FUN_800edf80(long a0, long a1, long a2, long a3) {
    long at;
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
    long s3;
    long s4;
    long s5;
    long s6;
    long t8;
    long t9;
    long s8;
    long sp;
    long ra;
    long sp0;
    long sp4;
    long spc;
    long sp8;
    t8 = a0;
    t7 = 0x1f80_0000L;
    t1 = MEMORY.ref(4, t7).offset(0x3d8L).get();
    t6 = MEMORY.ref(4, t7).offset(0x3ecL).get();
    s0 = MEMORY.ref(4, t7).offset(0x3d0L).get();
    s1 = MEMORY.ref(4, t7).offset(0x3e8L).get();
    t7 = (int)t6 >> 16;
    s8 = t7 << 25;
    v0 = 0x3c80_0000L;
    v0 = v0 | 0x8080L;
    v1 = 0xcL;
    MEMORY.ref(1, t1).offset(0x3L).setu(v1);
    MEMORY.ref(4, t1).offset(0x4L).setu(v0);
    t7 = MEMORY.ref(4, t1).offset(0x4L).get();

    t7 = t7 | s8;
    MEMORY.ref(4, t1).offset(0x4L).setu(t7);
    v0 = t1 + 0x4L;
    CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 6);
    if(a3 == 0) {
      v0 = 0x1f80_0000L;
    } else {
      v0 = 0x1f80_0000L;
      s3 = v0 + 0x3c0L;
      v0 = 0x1f80_0000L;
      s2 = v0 + 0x3c8L;
      t9 = 0xff_0000L;
      t9 = t9 | 0xffffL;
      t0 = a0 + 0x20L;
      t3 = t1 + 0x28L;

      //LAB_800ee004
      do {
        t5 = MEMORY.ref(2, t8).offset(0x16L).get();
        t6 = MEMORY.ref(2, t8).offset(0x1aL).get();
        t7 = MEMORY.ref(2, t8).offset(0x1eL).get();
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


        CPU.COP2(0x280030L);
        v0 = MEMORY.ref(4, t0-0x1cL).get();

        MEMORY.ref(4, t3-0x1cL).setu(v0);
        v0 = MEMORY.ref(4, t0-0x18L).get();
        a3 = a3 - 0x1L;
        MEMORY.ref(4, t3-0x10L).setu(v0);
        t4 = CPU.CFC2(31);

        if((int)t4 >= 0) {
          CPU.COP2(0x1400006L);
          v0 = MEMORY.ref(4, t0-0x14L).get();

          MEMORY.ref(4, t3-0x4L).setu(v0);
          t4 = CPU.MFC2(24);
          //LAB_800ee0a0
          if(s8 != 0 || (int)t4 > 0 || s8 == 0 && t4 != 0) {
            //LAB_800ee0a8
            MEMORY.ref(4, t1).offset(0x8L).setu(CPU.MFC2(12));
            MEMORY.ref(4, t1).offset(0x14L).setu(CPU.MFC2(13));
            MEMORY.ref(4, t1).offset(0x20L).setu(CPU.MFC2(14));
            v0 = MEMORY.ref(2, t0).offset(0x2L).get();

            v0 = v0 << 3;
            v0 = a1 + v0;
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
            CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);


            CPU.COP2(0x180001L);
            t4 = CPU.CFC2(31);

            if((int)t4 < 0) {
              v0 = t1 + 0x2cL;
            } else {
              v0 = t1 + 0x2cL;
              MEMORY.ref(4, v0).offset(0x0L).setu(CPU.MFC2(14));


              CPU.COP2(0x168002eL);
              v0 = MEMORY.ref(4, t0-0x10L).get();

              MEMORY.ref(4, t3).offset(0x8L).setu(v0);
              t2 = CPU.MFC2(7);
              v0 = MEMORY.ref(4, s3).offset(0x4L).get();
              t2 = t2 + s1;
              t2 = (int)t2 >> v0;
              if((int)t2 >= 0xbL) {
                v1 = MEMORY.ref(4, s2).offset(0x4L).get();

                if((int)t2 >= (int)v1) {
                  t2 = v1;
                }
                a0 = t2 << 2;

                //LAB_800ee13c
                a0 = s0 + a0;
                t5 = MEMORY.ref(2, t8).offset(0x14L).get();
                t6 = MEMORY.ref(2, t8).offset(0x18L).get();
                t7 = MEMORY.ref(2, t8).offset(0x1cL).get();
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


                CPU.COP2(0x118043fL);
                MEMORY.ref(4, t1).offset(0x4L).setu(CPU.MFC2(20));
                MEMORY.ref(4, t1).offset(0x10L).setu(CPU.MFC2(21));
                MEMORY.ref(4, t1).offset(0x1cL).setu(CPU.MFC2(22));
                v0 = MEMORY.ref(2, t0).offset(0x0L).get();

                v0 = v0 << 3;
                v0 = a2 + v0;
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x0L).get(), 0);
                CPU.MTC2(MEMORY.ref(4, v0).offset(0x4L).get(), 1);


                CPU.COP2(0x108041bL);
                v0 = MEMORY.ref(4, a0).offset(0x0L).get();
                v1 = 0xc00_0000L;
                v0 = v0 & t9;
                v0 = v0 | v1;
                MEMORY.ref(4, t1).offset(0x0L).setu(v0);
                v0 = t1 & t9;
                MEMORY.ref(4, a0).offset(0x0L).setu(v0);
                MEMORY.ref(4, t3).offset(0x0L).setu(CPU.MFC2(22));
                t3 = t3 + 0x34L;
                t1 = t1 + 0x34L;
              }
            }
          }
        }

        //LAB_800ee1e0
        t0 = t0 + 0x24L;

        //LAB_800ee1e4
        t8 = t8 + 0x24L;
      } while(a3 != 0);
    }

    //LAB_800ee1ec
    v0 = 0x1f80_0000L;
    MEMORY.ref(4, v0).offset(0x3d8L).setu(t1);
    v0 = t8;
    return v0;
  }

  @Method(0x800ee3c0L)
  public static long FUN_800ee3c0(final RunningScript a0) {
    final BtldScriptData27c v1 = scriptStatePtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
    v1._214.set(0x3);
    v1._215.set((int)a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800ee49cL)
  public static long FUN_800ee49c(final RunningScript a0) {
    final BtldScriptData27c a1 = scriptStatePtrArr_800bc1c0.get((int)a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
    a1._254.set(a0.params_20.get(1).deref().get());
    a1._25c.set(a0.params_20.get(2).deref().get());
    return 0;
  }

  @Method(0x800ee610L)
  public static void FUN_800ee610() {
    final int[] sp0x10 = new int[9];
    for(int i = 0; i < 9; i++) {
      sp0x10[i] = (int)_800c6e34.offset(i * 0x2L).get(); //2b
    }

    _800c6cf4.setu(0);
    _800c6c38.setu(0x1L);
    _800c6c2c.setu(addToLinkedListTail(0x3ccL));
    _800c6b5c.setu(addToLinkedListTail(0x930L));
    _800c6b60.setu(addToLinkedListTail(0xa4L));
    _800c6c34.setu(addToLinkedListTail(0x58L));
    _800c6b6c.setu(addToLinkedListTail(0x3cL));

    FUN_800ef7c4();
    FUN_800f4964();

    final long v0 = _800c6b60.get();
    MEMORY.ref(2, v0).offset(0x26L).setu(0);
    MEMORY.ref(2, v0).offset(0x28L).setu(0);
    MEMORY.ref(2, v0).offset(0x2aL).setu(0);
    MEMORY.ref(4, v0).offset(0x2cL).setu(0);
    MEMORY.ref(2, v0).offset(0x30L).setu(0);

    FUN_800f60ac();
    FUN_800f9584();

    _800c6b9c.setu(0);
    _800c69c8.setu(0);
    _800c6b68.setu(0);

    //LAB_800ee764
    for(int i = 0; i < 9; i++) {
      _800c6b78.offset(i * 0x4L).setu(-0x1L);

      //LAB_800ee770
      for(int v1 = 0; v1 < 22; v1++) {
        _800c69d0.offset(i * 0x2cL).offset(2, v1 * 0x2L).setu(0xa0ffL);
      }
    }

    //LAB_800ee7b0
    for(int i = 0; i < 3; i++) {
      //LAB_800ee7b8
      for(int v1 = 0; v1 < 22; v1++) {
        _800c6ba8.offset(i * 0x2cL).offset(2, v1 * 0x2L).setu(0xa0ffL);
      }
    }

    FUN_80023264();

    _800c6c3c.setu(0);

    //LAB_800ee80c
    for(int i = 0; i < 9; i++) {
      //LAB_800ee824
      for(int v1 = 0; v1 < gameState_800babc8._1e6.get(); v1++) {
        if(gameState_800babc8._2e9.get(v1).get() == sp0x10[i]) {
          _800c6c3c.oru(0x1L << i);
          break;
        }

        //LAB_800ee848
      }

      //LAB_800ee858
    }

    _800c697e.setu(0);
    _800c6980.setu(0);
    _800c6b64.setu(-0x1L);

    //LAB_800ee894
    for(int i = 0; i < 3; i++) {
      _800bc950.offset(i * 0x4L).setu(0);
    }

    FUN_80023a88();
    FUN_800f83c8();
  }

  @Method(0x800ee8c4L)
  public static void FUN_800ee8c4(final long address, final long fileSize, final long param) {
    final short[] sp0x38 = new short[12];
    for(int i = 0; i < sp0x38.length; i++) {
      sp0x38[i] = (short)_800c6e48.offset(i * 0x2L).get();
    }

    final short[] sp0x50 = new short[6];
    for(int i = 0; i < sp0x50.length; i++) {
      sp0x50[i] = (short)_800c6e60.offset(i * 0x2L).get();
    }

    //LAB_800ee9c0
    for(int s0 = 0; s0 < MEMORY.ref(4, address).offset(0x4L).get(); s0++) {
      if(MEMORY.ref(4, address).offset(s0 * 0x8L).offset(0xcL).get() != 0) {
        final TimHeader sp0x10 = parseTimHeader(MEMORY.ref(4, address + MEMORY.ref(4, address).offset(s0 * 0x8L).offset(0x8L).get() + 0x4L)); //TODO

        if(s0 == 0) {
          final RECT sp0x30 = new RECT((short)704, (short)256, (short)64, (short)256);
          LoadImage(sp0x30, sp0x10.getImageAddress());
        }

        //LAB_800eea20
        final RECT sp0x30 = new RECT();
        if(s0 < 0x4L) {
          sp0x30.x.set((short)(sp0x50[s0] + 704));
          sp0x30.y.set((short)496);
        } else {
          //LAB_800eea3c
          sp0x30.x.set((short)(sp0x50[s0] + 896));
          sp0x30.y.set((short)304);
        }

        //LAB_800eea50
        sp0x30.w.set(sp0x38[s0 * 2    ]);
        sp0x30.h.set(sp0x38[s0 * 2 + 1]);
        LoadImage(sp0x30, sp0x10.getClutAddress());
        DrawSync(0);
        _800c6cf4.addu(0x1L);
      }

      //LAB_800eea8c
    }

    //LAB_800eeaac
    removeFromLinkedList(address);
  }

  @Method(0x800eee80L)
  public static void FUN_800eee80(long a0) {
    long v0;
    long v1;
    long a1;
    long a2;
    long a3;
    long t0;
    long t2;
    long t3;
    long t4;
    long t8;
    t4 = a0;
    v0 = 0x800c_0000L;
    t8 = v0 + 0x6e90L;

    final long[] sp0x10 = {
      MEMORY.ref(4, t8).offset(0x0L).get(),
      MEMORY.ref(4, t8).offset(0x4L).get(),
      MEMORY.ref(4, t8).offset(0x8L).get(),
    };

    v0 = 0x8011_0000L;
    t3 = v0 + 0x2068L;
    v0 = 0x800c_0000L;
    t2 = v0 + 0x6ba8L;
    a3 = 0;

    //LAB_800eeecc
    for(int i = 0; i < 3; i++) {
      a1 = a3;
      a0 = MEMORY.ref(4, t3).offset(sp0x10[i] * 0x4L).get();

      //LAB_800eeee0
      do {
        MEMORY.ref(2, t2).offset(a1).setu(MEMORY.ref(2, a0).get());

        if(MEMORY.ref(2, a0).get() > 0xa0feL) {
          break;
        }

        a1 = a1 + 0x2L;
        a0 = a0 + 0x2L;
      } while(true);

      //LAB_800eef0c
      a3 = a3 + 0x2cL;
    }

    a0 = 0x800c_0000L;
    v0 = 0x800c_0000L;
    t2 = v0 + 0x69d0L;
    a0 = a0 - 0x3e40L;
    v1 = t4 << 2;
    t3 = 0x800c_0000L;
    v1 = v1 + a0;
    a3 = MEMORY.ref(4, t3).offset(0x6b9cL).get();
    v1 = MEMORY.ref(4, v1).offset(0x0L).get();
    v0 = a3 << 1;
    v0 = v0 + a3;
    v0 = v0 << 2;
    v0 = v0 - a3;
    a2 = v0 << 2;
    a1 = MEMORY.ref(4, v1).offset(0x0L).get();
    v1 = 0x8011_0000L;
    v1 = v1 + 0x2068L;
    v0 = MEMORY.ref(2, a1).offset(0x272L).getSigned();
    t0 = MEMORY.ref(2, a1).offset(0x272L).get();
    v0 = v0 << 2;
    v0 = v0 + v1;
    a0 = MEMORY.ref(4, v0).offset(0x0L).get();

    //LAB_800eef7c
    do {
      MEMORY.ref(2, t2).offset(a2).setu(MEMORY.ref(2, a0).get());

      if(MEMORY.ref(2, a0).get() > 0xa0feL) {
        break;
      }

      a2 = a2 + 0x2L;
      a0 = a0 + 0x2L;
    } while(true);

    //LAB_800eefa8
    a2 = 0x9fL;
    a0 = a1 + 0x13eL;
    v0 = 0x800c_0000L;
    v0 = v0 + 0x6b78L;
    v1 = a3 << 2;
    v1 = v1 + v0;
    v0 = a3 + 0x1L;
    MEMORY.ref(4, v1).offset(0x0L).setu(t4);
    MEMORY.ref(4, t3).offset(0x6b9cL).setu(v0);

    //LAB_800eefcc
    do {
      MEMORY.ref(2, a0).offset(0x4L).setu(0);
      a0 = a0 - 0x2L;
      a2 = a2 - 0x1L;
    } while((int)a2 >= 0);

    v0 = t0 << 16;
    v0 = (int)v0 >> 16;
    v1 = v0 << 3;
    v1 = v1 - v0;
    v1 = v1 << 2;
    v0 = 0x8011_0000L;
    v0 = v0 - 0x4568L;
    v1 = v1 + v0;
    v0 = MEMORY.ref(2, v1).offset(0x0L).get();

    MEMORY.ref(2, a1).offset(0x5cL).setu(v0);
    v0 = MEMORY.ref(2, v1).offset(0x2L).get();

    MEMORY.ref(2, a1).offset(0x5eL).setu(v0);
    v0 = MEMORY.ref(2, v1).offset(0x4L).get();

    MEMORY.ref(2, a1).offset(0x60L).setu(v0);
    v0 = MEMORY.ref(2, v1).offset(0x6L).get();

    MEMORY.ref(2, a1).offset(0x62L).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0x8L).get();

    MEMORY.ref(2, a1).offset(0x64L).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0x9L).get();

    MEMORY.ref(2, a1).offset(0x66L).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0xaL).get();

    MEMORY.ref(2, a1).offset(0x68L).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0xbL).get();

    MEMORY.ref(2, a1).offset(0x6aL).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0xcL).get();

    MEMORY.ref(2, a1).offset(0x6cL).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0xdL).get();

    MEMORY.ref(2, a1).offset(0x6eL).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0xeL).get();

    MEMORY.ref(2, a1).offset(0x70L).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0xfL).get();

    MEMORY.ref(2, a1).offset(0x72L).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0x10L).get();

    MEMORY.ref(2, a1).offset(0x74L).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0x11L).get();

    MEMORY.ref(2, a1).offset(0x76L).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0x12L).get();

    v0 = v0 << 24;
    v0 = (int)v0 >> 24;
    MEMORY.ref(2, a1).offset(0x78L).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0x13L).get();

    v0 = v0 << 24;
    v0 = (int)v0 >> 24;
    MEMORY.ref(2, a1).offset(0x7aL).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0x14L).get();

    v0 = v0 << 24;
    v0 = (int)v0 >> 24;
    MEMORY.ref(2, a1).offset(0x7cL).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0x15L).get();

    MEMORY.ref(2, a1).offset(0x7eL).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0x16L).get();

    MEMORY.ref(2, a1).offset(0x80L).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0x17L).get();

    MEMORY.ref(2, a1).offset(0x82L).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0x18L).get();

    v0 = v0 << 24;
    v0 = (int)v0 >> 24;
    MEMORY.ref(2, a1).offset(0x84L).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0x19L).get();

    v0 = v0 << 24;
    v0 = (int)v0 >> 24;
    MEMORY.ref(2, a1).offset(0x86L).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0x1aL).get();

    v0 = v0 << 24;
    v0 = (int)v0 >> 24;
    MEMORY.ref(2, a1).offset(0x88L).setu(v0);
    v0 = MEMORY.ref(1, v1).offset(0x1bL).get();

    v0 = v0 << 24;
    v0 = (int)v0 >> 24;
    MEMORY.ref(2, a1).offset(0x8aL).setu(v0);
    MEMORY.ref(2, a1).offset(0x8L).setu(MEMORY.ref(2, v1).offset(0x0L).get());
    MEMORY.ref(2, a1).offset(0xcL).setu(MEMORY.ref(2, v1).offset(0x2L).get());
    MEMORY.ref(2, a1).offset(0x10L).setu(MEMORY.ref(2, v1).offset(0x0L).get());
    MEMORY.ref(2, a1).offset(0x12L).setu(MEMORY.ref(2, v1).offset(0x2L).get());
    MEMORY.ref(2, a1).offset(0x16L).setu(0);
    MEMORY.ref(2, a1).offset(0x18L).setu(0);
    MEMORY.ref(2, a1).offset(0x1aL).setu(0);
    MEMORY.ref(2, a1).offset(0x14L).setu(MEMORY.ref(1, v1).offset(0xdL).get());
    MEMORY.ref(2, a1).offset(0x1cL).setu(MEMORY.ref(1, v1).offset(0xfL).get());
    MEMORY.ref(2, a1).offset(0x20L).setu(0);
    MEMORY.ref(2, a1).offset(0x1eL).setu(MEMORY.ref(1, v1).offset(0xeL).get());
    MEMORY.ref(2, a1).offset(0x22L).setu(MEMORY.ref(1, v1).offset(0x10L).get());
    MEMORY.ref(2, a1).offset(0x26L).setu(0);
    MEMORY.ref(2, a1).offset(0x28L).setu(0);
    MEMORY.ref(2, a1).offset(0x2aL).setu(0);
    MEMORY.ref(2, a1).offset(0x2cL).setu(0);
    MEMORY.ref(2, a1).offset(0x2eL).setu(0);
    MEMORY.ref(2, a1).offset(0x30L).setu(0);
    MEMORY.ref(2, a1).offset(0x24L).setu(MEMORY.ref(1, v1).offset(0x11L).get());
    MEMORY.ref(2, a1).offset(0x32L).setu(MEMORY.ref(1, v1).offset(0x8L).get());
    MEMORY.ref(2, a1).offset(0x34L).setu(MEMORY.ref(2, v1).offset(0x4L).get());
    MEMORY.ref(2, a1).offset(0x36L).setu(MEMORY.ref(2, v1).offset(0x6L).get());
    MEMORY.ref(2, a1).offset(0x38L).setu(MEMORY.ref(1, v1).offset(0x9L).get());
    MEMORY.ref(2, a1).offset(0x3cL).setu(0);
    MEMORY.ref(2, a1).offset(0x3eL).setu(0);
    MEMORY.ref(2, a1).offset(0x3aL).setu(MEMORY.ref(1, v1).offset(0xaL).get());
    MEMORY.ref(2, a1).offset(0x40L).setu(MEMORY.ref(1, v1).offset(0xbL).get());
    MEMORY.ref(2, a1).offset(0x44L).setu(0);
    MEMORY.ref(2, a1).offset(0x46L).setu(0);
    MEMORY.ref(2, a1).offset(0x48L).setu(0);
    MEMORY.ref(2, a1).offset(0x4aL).setu(0);
    MEMORY.ref(2, a1).offset(0x58L).setu(-0x1L);
    MEMORY.ref(2, a1).offset(0x42L).setu(MEMORY.ref(1, v1).offset(0xcL).get());

    if((MEMORY.ref(2, a1).offset(0x6eL).get() & 0x8L) != 0) {
      MEMORY.ref(2, a1).offset(0x110L).setu(0x1L);
    }

    //LAB_800ef25c
    if((MEMORY.ref(2, a1).offset(0x6eL).get() & 0x4L) != 0) {
      MEMORY.ref(2, a1).offset(0x112L).setu(0x1L);
    }

    //LAB_800ef274
    FUN_80012bb4();
  }

  @Method(0x800ef28cL)
  public static void FUN_800ef28c(long a0) {
    //LAB_800ef2c4
    //TODO sp0x18 is unused, why?
    //memcpy(sp0x18, _800c6e68.getAddress(), 0x28);

    FUN_80110030(0x1L);

    //LAB_800ef31c
    for(int charIndex = 0; charIndex < 3; charIndex++) {
      //LAB_800ef328
      for(int i = 0; i < 9; i++) {
        _800c6960.offset(charIndex * 0x9L).offset(1, i).setu(0xffL);
      }
    }

    //LAB_800ef36c
    //LAB_800ef38c
    for(int charIndex = 0; charIndex < Math.min(_800c677c.get(), 3); charIndex++) {
      long s0 = scriptStatePtrArr_800bc1c0.get((int)_8006e398.offset(4, 0xe40L).offset(charIndex * 0x4L).get()).deref().innerStruct_00.getPointer(); //TODO
      long s1 = MEMORY.ref(2, s0).offset(0x272L).get();
      final byte[] sp0x10 = new byte[9];
      FUN_80022928(sp0x10, (int)MEMORY.ref(2, s0).offset(0x272L).getSigned());
      _800c6960.offset(charIndex * 0x9L).offset(1, 0x0L).setu(s1);

      //LAB_800ef3d8
      for(int i = 1; i < 9; i++) {
        _800c6960.offset(charIndex * 0x9L).offset(1, i).setu(sp0x10[i - 1]);
      }

      //LAB_800ef400
      for(int i = 0; i < 0xa0; i++) {
        MEMORY.ref(2, s0).offset(i * 0x2L).offset(0x4L).setu(0);
      }

      final ActiveStatsa0 stats = stats_800be5f8.get((short)s1);
      MEMORY.ref(2, s0).offset(0x04L).setu(stats.level_0e.get());
      MEMORY.ref(2, s0).offset(0x06L).setu(stats.dlevel_0f.get());
      MEMORY.ref(2, s0).offset(0x08L).setu(stats.hp_04.get());
      MEMORY.ref(2, s0).offset(0x0aL).setu(stats.sp_08.get());
      MEMORY.ref(2, s0).offset(0x0cL).setu(stats.mp_06.get());
      MEMORY.ref(2, s0).offset(0x0eL).setu(stats._0c.get());
      MEMORY.ref(2, s0).offset(0x10L).setu(stats.maxHp_66.get());
      MEMORY.ref(2, s0).offset(0x12L).setu(stats.maxMp_6e.get());
      MEMORY.ref(2, s0).offset(0xacL).setu(stats.dragoonAttack_72.get());
      MEMORY.ref(2, s0).offset(0xaeL).setu(stats.dragoonMagicAttack_73.get());
      MEMORY.ref(2, s0).offset(0xb0L).setu(stats.dragoonDefence_74.get());
      MEMORY.ref(2, s0).offset(0xb2L).setu(stats.dragoonMagicDefence_75.get());
      MEMORY.ref(2, s0).offset(0x14L).setu(stats._76.get());
      MEMORY.ref(2, s0).offset(0x16L).setu(stats._77.get());
      MEMORY.ref(2, s0).offset(0x18L).setu(stats._78.get());
      MEMORY.ref(2, s0).offset(0x1aL).setu(stats._79.get());
      MEMORY.ref(2, s0).offset(0x1cL).setu(stats._7a.get());
      MEMORY.ref(2, s0).offset(0x1eL).setu(stats._7b.get());
      MEMORY.ref(2, s0).offset(0x20L).setu(stats._7c.get());
      MEMORY.ref(2, s0).offset(0x22L).setu(stats._7d.get());
      MEMORY.ref(2, s0).offset(0x24L).setu(stats._7e.get());
      MEMORY.ref(2, s0).offset(0x26L).setu(stats._7f.get());
      MEMORY.ref(2, s0).offset(0x28L).setu(stats._80.get());
      MEMORY.ref(2, s0).offset(0x2aL).setu(stats._81.get());
      MEMORY.ref(2, s0).offset(0x2cL).setu(stats._82.get());
      MEMORY.ref(2, s0).offset(0x2eL).setu(stats._83.get());
      MEMORY.ref(2, s0).offset(0x30L).setu(stats._84.get());
      MEMORY.ref(2, s0).offset(0x32L).setu(stats.gearSpeed_86.get() + stats.bodySpeed_69.get());
      MEMORY.ref(2, s0).offset(0x34L).setu(stats.gearAttack_88.get() + stats.bodyAttack_6a.get());
      MEMORY.ref(2, s0).offset(0x36L).setu(stats.gearMagicAttack_8a.get() + stats.bodyMagicAttack_6b.get());
      MEMORY.ref(2, s0).offset(0x38L).setu(stats.gearDefence_8c.get() + stats.bodyDefence_6c.get());
      MEMORY.ref(2, s0).offset(0x3aL).setu(stats.gearMagicDefence_8e.get() + stats.bodyMagicDefence_6d.get());
      MEMORY.ref(2, s0).offset(0x3cL).setu(stats.attackHit_90.get());
      MEMORY.ref(2, s0).offset(0x3eL).setu(stats.magicHit_92.get());
      MEMORY.ref(2, s0).offset(0x40L).setu(stats.attackAvoid_94.get());
      MEMORY.ref(2, s0).offset(0x42L).setu(stats.magicAvoid_96.get());
      MEMORY.ref(2, s0).offset(0x44L).setu(stats._98.get());
      MEMORY.ref(2, s0).offset(0x46L).setu(stats._99.get());
      MEMORY.ref(2, s0).offset(0x48L).setu(stats._9a.get());
      MEMORY.ref(2, s0).offset(0x4aL).setu(stats._9b.get());
      MEMORY.ref(2, s0).offset(0x58L).setu(stats.selectedAddition_35.get());
      MEMORY.ref(2, s0).offset(0x118L).setu(stats._9c.get());
      MEMORY.ref(2, s0).offset(0x11aL).setu(stats._9e.get());
      MEMORY.ref(2, s0).offset(0x11cL).setu(stats._9f.get());
      MEMORY.ref(2, s0).offset(0x4eL).setu(stats._9b.get());
      MEMORY.ref(2, s0).offset(0x142L).setu(stats._9b.get());
      MEMORY.ref(2, s0).offset(0x110L).setu(stats._46.get());
      MEMORY.ref(2, s0).offset(0x112L).setu(stats._48.get());
      MEMORY.ref(2, s0).offset(0x114L).setu(stats._4a.get());
      MEMORY.ref(2, s0).offset(0x128L).setu(stats._4c.get());
      MEMORY.ref(2, s0).offset(0x12aL).setu(stats._4e.get());
      MEMORY.ref(2, s0).offset(0x12cL).setu(stats._50.get());
      MEMORY.ref(2, s0).offset(0x12eL).setu(stats._52.get());
      MEMORY.ref(2, s0).offset(0x130L).setu(stats._54.get());
      MEMORY.ref(2, s0).offset(0x132L).setu(stats._56.get());
      MEMORY.ref(2, s0).offset(0x134L).setu(stats._58.get());
      MEMORY.ref(2, s0).offset(0x136L).setu(stats._5a.get());
      MEMORY.ref(2, s0).offset(0x138L).setu(stats._5c.get());
      MEMORY.ref(2, s0).offset(0x13aL).setu(stats._5e.get());
      MEMORY.ref(2, s0).offset(0x116L).setu(stats._60.get());
      MEMORY.ref(2, s0).offset(0x13cL).setu(stats._62.get());
      MEMORY.ref(2, s0).offset(0x13eL).setu(stats._64.get());
      MEMORY.ref(2, s0).offset(0x11eL).setu(stats.equipment_30.get(0).get());
      MEMORY.ref(2, s0).offset(0x120L).setu(stats.equipment_30.get(1).get());
      MEMORY.ref(2, s0).offset(0x122L).setu(stats.equipment_30.get(2).get());
      MEMORY.ref(2, s0).offset(0x124L).setu(stats.equipment_30.get(3).get());
      MEMORY.ref(2, s0).offset(0x126L).setu(stats.equipment_30.get(4).get());
    }

    //LAB_800ef798
  }

  @Method(0x800ef7c4L)
  public static void FUN_800ef7c4() {
    //LAB_800ef7d4
    long v1 = _800c6c40.getAddress();
    for(int i = 0; i < 3; i++) {
      MEMORY.ref(2, v1).offset(0x0L).setu(-0x1L);
      MEMORY.ref(2, v1).offset(0x4L).setu(0);
      MEMORY.ref(2, v1).offset(0x6L).setu(0);
      MEMORY.ref(2, v1).offset(0x8L).setu(0);
      MEMORY.ref(2, v1).offset(0xaL).setu(0);
      MEMORY.ref(2, v1).offset(0xeL).setu(0);
      MEMORY.ref(2, v1).offset(0xcL).setu(0);
      MEMORY.ref(2, v1).offset(0x10L).setu(0);
      MEMORY.ref(2, v1).offset(0x12L).setu(0);
      v1 = v1 + 0x3cL;
    }

    long a3 = _800c6c2c.get();

    //LAB_800ef818
    for(int i = 0; i < 3; i++) {
      //LAB_800ef820
      for(int a1 = 0; a1 < 5; a1++) {
        v1 = a1 * 0x40L;

        //LAB_800ef828
        for(int a0 = 0; a0 < 4; a0++) {
          MEMORY.ref(2, a3).offset(v1).offset(0x4L).setu(-0x1L);
          v1 = v1 + 0x10L;
        }
      }

      a3 = a3 + 0x144L;
    }

    long a0 = _800c6b5c.get();

    //LAB_800ef878
    for(int i = 0; i < 12; i++) {
      MEMORY.ref(4, a0).offset(0x18L).setu(-0x1L);
      MEMORY.ref(4, a0).offset(0x14L).setu(-0x1L);
      MEMORY.ref(2, a0).offset(0x0L).setu(0);
      MEMORY.ref(2, a0).offset(0x2L).setu(0);
      MEMORY.ref(4, a0).offset(0x4L).setu(-0x1L);
      MEMORY.ref(4, a0).offset(0x8L).setu(0);
      MEMORY.ref(4, a0).offset(0xcL).setu(0x80_8080L);

      //LAB_800ef89c
      v1 = a0;
      for(int a1 = 0; a1 < 5; a1++) {
        MEMORY.ref(2, v1).offset(0x30L).setu(-0x1L);
        MEMORY.ref(4, v1).offset(0x24L).setu(0);
        MEMORY.ref(4, v1).offset(0x28L).setu(0);
        MEMORY.ref(4, v1).offset(0x2cL).setu(0);
        MEMORY.ref(2, v1).offset(0x40L).setu(0);
        v1 = v1 + 0x20L;
      }

      a0 = a0 + 0xc4L;
    }
  }

  @Method(0x800ef8d8L)
  public static void FUN_800ef8d8(long a0) {
    long v0;
    long v1;
    long a1;
    long a2;
    long a3;
    long t0;
    long t1;
    a3 = a0;
    t0 = 0;
    v0 = 0x800c_0000L;
    v0 = v0 + 0x6c40L;
    t1 = v0;
    v0 = a3 << 4;
    v0 = v0 - a3;
    v0 = v0 << 2;
    a2 = v0;
    a0 = a2 + t1;
    a1 = 0x800c_0000L;
    v1 = 0x8007_0000L;
    v1 = v1 - 0x1c68L;
    v0 = a3 << 2;
    v0 = v0 + v1;
    a1 = a1 - 0x3e40L;
    MEMORY.ref(2, a0).offset(0x0L).setu(a3);
    v1 = MEMORY.ref(4, v0).offset(0xe40L).get();
    v0 = a3 << 1;
    v0 = v0 + a3;
    v1 = v1 << 2;
    v1 = v1 + a1;
    v1 = MEMORY.ref(4, v1).offset(0x0L).get();
    v0 = v0 << 4;
    v1 = MEMORY.ref(4, v1).offset(0x0L).get();
    v0 = v0 - a3;
    a1 = MEMORY.ref(2, v1).offset(0x272L).get();
    v0 = v0 << 1;
    MEMORY.ref(2, a0).offset(0x6L).setu(0);
    v1 = MEMORY.ref(2, a0).offset(0x6L).get();
    v0 = v0 + 0x3fL;
    MEMORY.ref(2, a0).offset(0x8L).setu(v0);
    v0 = 0x26L;
    MEMORY.ref(2, a0).offset(0xaL).setu(v0);
    v0 = 0x11L;
    MEMORY.ref(2, a0).offset(0x12L).setu(v0);
    v0 = 0x20L;
    MEMORY.ref(2, a0).offset(0x4L).setu(0);
    MEMORY.ref(2, a0).offset(0x10L).setu(v0);
    v1 = v1 | 0x2L;
    MEMORY.ref(2, a0).offset(0x2L).setu(a1);
    MEMORY.ref(2, a0).offset(0x6L).setu(v1);

    //LAB_800ef980
    do {
      v0 = a2 + t1;
      MEMORY.ref(4, v0).offset(0x14L).setu(0);
      t0 = t0 + 0x1L;
      a2 = a2 + 0x4L;
    } while((int)t0 < 0xaL);

    a1 = 0x800c_0000L;
    v1 = a3 << 2;
    v1 = v1 + a3;
    v1 = v1 << 4;
    v1 = v1 + a3;
    v1 = v1 << 2;
    a0 = 0x800c_0000L;
    a0 = a0 + 0x6c40L;
    v0 = a3 << 4;
    v0 = v0 - a3;
    v0 = v0 << 2;
    v0 = v0 + a0;
    a0 = MEMORY.ref(4, a1).offset(0x6c2cL).get();
    a1 = MEMORY.ref(2, v0).offset(0x8L).get();
    v1 = v1 + a0;
    MEMORY.ref(2, v1).offset(0x0L).setu(a1);
    v0 = MEMORY.ref(2, v0).offset(0xaL).get();
    MEMORY.ref(2, v1).offset(0x2L).setu(v0);
  }

  @Method(0x800ef9e4L)
  public static void FUN_800ef9e4() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long t0;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long hi;
    v0 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v0).offset(0x6cf4L).get();
    v0 = 0x6L;
    if(v1 == v0) {
      v0 = 0x800c_0000L;
      v0 = MEMORY.ref(4, v0).offset(0x677cL).get();

      if((int)v0 > 0) {
        s3 = 0;
        s1 = -0x1L;
        v0 = 0x800c_0000L;
        s0 = v0 + 0x6c40L;

        //LAB_800efa34
        do {
          v0 = MEMORY.ref(2, s0).offset(0x0L).getSigned();

          if(v0 == s1) {
            v1 = _800be5d0.get();
            v0 = 0x1L;
            if(v1 == v0) {
              a0 = s3;
              FUN_800ef8d8(a0);
            }
          }

          //LAB_800efa64
          v0 = 0x800c_0000L;
          v0 = MEMORY.ref(4, v0).offset(0x677cL).get();
          s3 = s3 + 0x1L;
          s0 = s0 + 0x3cL;
        } while((int)s3 < (int)v0);
      }

      //LAB_800efa78
      v0 = 0x800c_0000L;
      s4 = MEMORY.ref(4, v0).offset(0x677cL).get();

      if((int)s4 >= 0x4L) {
        s4 = 0x3L;
      }

      //LAB_800efa94
      if((int)s4 > 0) {
        s3 = 0;
        v0 = 0x800c_0000L;
        s5 = v0 - 0x3e40L;
        v0 = 0x800c_0000L;
        s2 = v0 + 0x6c40L;

        //LAB_800efaac
        do {
          v1 = MEMORY.ref(2, s2).offset(0x0L).getSigned();
          v0 = -0x1L;
          if(v1 != v0) {
            v1 = MEMORY.ref(2, s2).offset(0x6L).get();

            v0 = v1 & 0x1L;
            if(v0 != 0) {
              v0 = v1 & 0x2L;
              if(v0 != 0) {
                v1 = 0x8007_0000L;
                v1 = v1 - 0x1c68L;
                v0 = s3 << 2;
                v0 = v0 + v1;
                v0 = MEMORY.ref(4, v0).offset(0xe40L).get();

                v0 = v0 << 2;
                v0 = v0 + s5;
                v0 = MEMORY.ref(4, v0).offset(0x0L).get();

                s1 = MEMORY.ref(4, v0).offset(0x0L).get();

                v0 = MEMORY.ref(2, s1).offset(0x10L).get();
                a2 = MEMORY.ref(2, s1).offset(0x8L).getSigned();
                v0 = v0 << 16;
                v1 = (int)v0 >> 16;
                v0 = v0 >>> 31;
                v0 = v1 + v0;
                v0 = (int)v0 >> 1;
                if((int)v0 < (int)a2) {
                  a3 = 0x1L;
                } else {
                  a3 = 0x2L;
                }

                //LAB_800efb30
                v0 = v1;
                if((int)v0 < 0) {
                  v0 = v0 + 0x3L;
                }

                //LAB_800efb40
                v0 = (int)v0 >> 2;
                s0 = s3 << 16;
                if((int)v0 >= (int)a2) {
                  a3 = 0x3L;
                }

                //LAB_800efb54
                s0 = (int)s0 >> 16;
                a0 = s0;
                a1 = 0;
                FUN_800f1550(a0, a1, a2, a3);
                a0 = s0;
                a1 = 0x1L;
                a2 = MEMORY.ref(2, s1).offset(0x10L).getSigned();
                a3 = a1;
                FUN_800f1550(a0, a1, a2, a3);
                a0 = s0;
                a1 = 0x2L;
                a2 = MEMORY.ref(2, s1).offset(0xcL).getSigned();
                a3 = 0x1L;
                FUN_800f1550(a0, a1, a2, a3);
                a0 = s0;
                a1 = 0x3L;
                a2 = MEMORY.ref(2, s1).offset(0x12L).getSigned();
                a3 = 0x1L;
                FUN_800f1550(a0, a1, a2, a3);
                a0 = 0x51eb_0000L;
                v1 = MEMORY.ref(2, s1).offset(0xaL).get();
                a0 = a0 | 0x851fL;
                v1 = v1 << 16;
                v0 = (int)v1 >> 16;
                hi = ((long)(int)v0 * (int)a0) >>> 32;
                a1 = 0x4L;
                a3 = 0x1L;
                a0 = s0;
                v1 = (int)v1 >> 31;
                t0 = hi;
                a2 = (int)t0 >> 5;
                a2 = a2 - v1;
                a2 = a2 << 16;
                a2 = (int)a2 >> 16;
                FUN_800f1550(a0, a1, a2, a3);
                v0 = 0x800c_0000L;
                v0 = MEMORY.ref(4, v0).offset(-0x4f04L).get();

                v0 = v0 & 0x3L;
                MEMORY.ref(4, s2).offset(0x18L).setu(v0);
                v0 = MEMORY.ref(2, s1).offset(0x6L).getSigned();
                v1 = MEMORY.ref(2, s1).offset(0x6L).get();
                if((int)v0 >= 0x5L) {
                  v1 = 0x5L;
                }

                //LAB_800efc0c
                a0 = MEMORY.ref(2, s1).offset(0xaL).getSigned();
                v1 = v1 << 16;
                v1 = (int)v1 >> 16;
                v0 = v1 << 1;
                v0 = v0 + v1;
                v0 = v0 << 3;
                v0 = v0 + v1;
                v1 = v0 << 2;
                if((int)a0 < (int)v1) {
                  MEMORY.ref(2, s2).offset(0x6L).and(0xfff3L);
                } else {
                  MEMORY.ref(2, s2).offset(0x6L).oru(0x4L);

                  if(MEMORY.ref(2, s1).offset(0xaL).getSigned() < (int)v1) {
                    //LAB_800efc5c
                    MEMORY.ref(2, s2).offset(0x6L).and(0xfff3L);
                  }
                }

                //LAB_800efc6c
                v1 = MEMORY.ref(2, s2).offset(0x6L).get();

                v0 = v1 & 0x4L;
                if(v0 != 0) {
                  v0 = v1 ^ 0x8L;
                  MEMORY.ref(2, s2).offset(0x6L).setu(v0);
                }

                //LAB_800efc84
                v1 = MEMORY.ref(4, s2).offset(0x1cL).get();

                if((int)v1 < 0x6L) {
                  v0 = v1 + 0x1L;
                  MEMORY.ref(4, s2).offset(0x1cL).setu(v0);
                }
              }
            }
          }

          //LAB_800efc9c
          s3 = s3 + 0x1L;
          s2 = s2 + 0x3cL;
        } while((int)s3 < (int)s4);
      }

      //LAB_800efcac
      if((int)s4 > 0) {
        s3 = 0;
        v0 = 0x800c_0000L;
        a1 = v0 + 0x6c40L;
        v0 = 0x800c_0000L;
        v1 = 0x8010_0000L;
        a0 = MEMORY.ref(4, v0).offset(0x6c2cL).get();
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x6c38L).get();
        v1 = v1 - 0x4e68L;
        v0 = v0 << 1;
        v1 = v0 + v1;

        //LAB_800efcdc
        do {
          v0 = MEMORY.ref(2, v1).offset(0x0L).get();
          s3 = s3 + 0x1L;
          MEMORY.ref(2, a1).offset(0xaL).setu(v0);
          v0 = MEMORY.ref(2, v1).offset(0x0L).get();
          a1 = a1 + 0x3cL;
          MEMORY.ref(2, a0).offset(0x2L).setu(v0);
          a0 = a0 + 0x144L;
        } while((int)s3 < (int)s4);
      }

      //LAB_800efd00
      FUN_800f3940();
      FUN_800f4b80();
    }

    //LAB_800efd10
  }

  @Method(0x800efd34L)
  public static void FUN_800efd34() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long t0;
    long t1;
    long t5;
    long t7;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long s7;
    long fp;
    long spe8;
    long spec;
    long spf0;
    long spf8;
    long spfc;
    spf0 = 0;

    final Memory.TemporaryReservation sp0x38tmp = MEMORY.temp(0x30);
    final Value sp0x38 = sp0x38tmp.get();
    memcpy(sp0x38.getAddress(), _800c6e9c.getAddress(), 0x30);

    //LAB_800efe04
    final Memory.TemporaryReservation sp0x78tmp = MEMORY.temp(0x24);
    final Value sp0x78 = sp0x78tmp.get();
    memcpy(sp0x78.getAddress(), _800c6ecc.getAddress(), 0x24);

    //LAB_800efe9c
    final Memory.TemporaryReservation sp0xa0tmp = MEMORY.temp(0x14);
    final Value sp0xa0 = sp0xa0tmp.get();
    memcpy(sp0xa0.getAddress(), _800c6ef0.getAddress(), 0x14);

    //LAB_800eff1c
    //LAB_800eff70
    final Memory.TemporaryReservation sp0xb8tmp = MEMORY.temp(0x2a);
    final Value sp0xb8 = sp0xb8tmp.get();
    memcpy(sp0xb8.getAddress(), _800c6f04.getAddress(), 0x2a);

    //LAB_800effa0
    if((int)_800c6cf4.get() >= 0x6L) {
      spe8 = _800c677c.get();
      if(spe8 > 0x3L) {
        spe8 = 0x3L;
      }

      //LAB_800f0000
      final Memory.TemporaryReservation sp0x68tmp = MEMORY.temp(0xc);
      final Value sp0x68 = sp0x68tmp.get();

      //LAB_800f0008
      for(s4 = 0; s4 < 3; s4++) {
        sp0x68.offset(s4 * 0x4L).setu(-0x1L);
      }

      //LAB_800f002c
      for(s4 = 0, v1 = 0; s4 < 3; s4++) {
        if(_800c6c40.offset(2, s4 * 0x3cL).getSigned() != -0x1L) {
          sp0x68.offset(v1 * 0x4L).setu(s4);
          v1++;
        }

        //LAB_800f0044
      }

      s7 = _800c6c40.getAddress();
      spf8 = 0;
      spfc = 0;

      //LAB_800f0074
      for(s4 = 0; s4 < spe8; s4++) {
        if(MEMORY.ref(2, s7).offset(0x0L).getSigned() != -0x1L) {
          v1 = MEMORY.ref(2, s7).offset(0x6L).get();

          if((v1 & 0x1L) != 0 && (v1 & 0x2L) != 0) {
            a2 = _8006e398.offset(0xe40L).offset(spfc).get();
            final BtldScriptData27c data = scriptStatePtrArr_800bc1c0.get((int)a2).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
            if((scriptStatePtrArr_800bc1c0.get((int)_800c66c8.get()).deref().ui_60.get() & 0x4L) != 0x1L && _800c66c8.get() == a2) {
              spec = 0x2L;
              s5 = 0x2L;
            } else {
              spec = 0;
              s5 = 0x1L;
            }

            //LAB_800f0108
            if((data._0e.get() & 0x2000L) == 0) {
              s2 = 0x4L;
            } else {
              s2 = 0x5L;
            }

            //LAB_800f0120
            //LAB_800f0128
            for(s0 = 0; s0 < s2; s0++) {
              //LAB_800f0134
              for(s1 = 0; s1 < 4; s1++) {
                a2 = _800c6c2c.get() + spf8;
                t0 = a2 + s0 * 0x40L + s1 * 0x10L;
                if(MEMORY.ref(2, t0).offset(0x4L).getSigned() == -0x1L) {
                  break;
                }
                FUN_800f8dfc(MEMORY.ref(2, t0).offset(0x6L).get() + MEMORY.ref(2, a2).offset(0x0L).get() - centreScreenX_1f8003dc.get(), MEMORY.ref(2, t0).offset(0x8L).get() + MEMORY.ref(2, a2).offset(0x2L).get() - centreScreenY_1f8003de.get(), MEMORY.ref(1, t0).offset(0xaL).get(), MEMORY.ref(1, t0).offset(0xcL).get(), MEMORY.ref(2, t0).offset(0xeL).getSigned(), MEMORY.ref(2, t0).offset(0x10L).getSigned(), MEMORY.ref(2, t0).offset(0x12L).getSigned(), spec, MEMORY.ref(4, s7).offset(0x1cL).get());
              }

              //LAB_800f01e0
            }

            //LAB_800f01f0
            a1 = _800c6c2c.get() + spf8;
            s0 = _800fb444.offset(data._272.get() * 0x4L).get();
            FUN_800f8dfc(MEMORY.ref(2, a1).offset(0x0L).get() - centreScreenX_1f8003dc.get() + 1, MEMORY.ref(2, a1).offset(0x2L).get() - centreScreenY_1f8003de.get() - 25, MEMORY.ref(1, s0).offset(0x0L).get(), MEMORY.ref(1, s0).offset(0x1L).get(), MEMORY.ref(1, s0).offset(0x2L).get(), MEMORY.ref(1, s0).offset(0x3L).get(), 0x2cL, spec, MEMORY.ref(4, s7).offset(0x1cL).get());
            v0 = _800c6c2c.get() + spf8;
            FUN_800f8dfc(MEMORY.ref(2, v0).offset(0x0L).get() - centreScreenX_1f8003dc.get() - 44, MEMORY.ref(2, v0).offset(0x2L).get() - centreScreenY_1f8003de.get() - 22, MEMORY.ref(1, s0).offset(0x4L).get(), MEMORY.ref(1, s0).offset(0x5L).get(), MEMORY.ref(1, s0).offset(0x6L).get(), MEMORY.ref(1, s0).offset(0x7L).get(), MEMORY.ref(1, s0).offset(0x8L).get(), s5, MEMORY.ref(4, s7).offset(0x1cL).get());

            if(spec != 0) {
              final long v1_0 = (6 - MEMORY.ref(4, s7).offset(0x1cL).get()) * 8 + 100;
              t1 = _800c6c2c.get() + spf8;
              a0 = MEMORY.ref(2, t1).offset(0x0L).get() - centreScreenX_1f8003dc.get() + (MEMORY.ref(1, s0).offset(0x6L).get() / 2 - 44);
              v1 = (MEMORY.ref(1, s0).offset(0x6L).get() + 2) * v1_0 / 100 / 2;
              v0 = a0 - v1;
              a0 = a0 + v1 - 0x1L;

              final Memory.TemporaryReservation sp0x28tmp = MEMORY.temp(0x8);
              final Value sp0x28 = sp0x28tmp.get();
              sp0x28.offset(0x0L).setu(v0);
              sp0x28.offset(0x2L).setu(a0);
              sp0x28.offset(0x4L).setu(v0);
              sp0x28.offset(0x6L).setu(a0);
              a1 = MEMORY.ref(2, t1).offset(0x2L).get() - centreScreenY_1f8003de.get() + (MEMORY.ref(1, s0).offset(0x7L).get() / 2 - 22);
              v1 = (MEMORY.ref(1, s0).offset(0x7L).get() + 2) * v1_0 / 100 / 2;
              v0 = a1 - v1;
              a1 = a1 + v1 - 0x1L;
              final Memory.TemporaryReservation sp0x30tmp = MEMORY.temp(0x8);
              final Value sp0x30 = sp0x30tmp.get();
              sp0x30.offset(0x0L).setu(v0);
              sp0x30.offset(0x2L).setu(v0);
              sp0x30.offset(0x4L).setu(a1);
              sp0x30.offset(0x6L).setu(a1);

              //LAB_800f0438
              for(s2 = 0; s2 < 8; s2++) {
                s3 = 0xffL;
                s0 = 0xffL;
                t7 = 0xffL;
                v1 = MEMORY.ref(4, s7).offset(0x1cL).get();

                s1 = 0;
                if((int)v1 < 0x6L) {
                  s1 = 0x1L;
                  v0 = v1 * 0x2aL;
                  t7 = v0;
                  s3 = v0;
                  s0 = v0;
                }

                //LAB_800f0470
                //LAB_800f047c
                t5 = s2 / 4;
                t0 = sp0x38.offset(s2 % 4 * 0xcL).getAddress();
                a2 = sp0x28.offset(MEMORY.ref(1, t0).offset(0x0L).getSigned() * 2).getAddress();
                a1 = sp0x30.offset(MEMORY.ref(1, t0).offset(0x1L).getSigned() / 2).getAddress();
                v1 = sp0x28.offset(MEMORY.ref(1, t0).offset(0x2L).getSigned() / 2).getAddress();
                v0 = sp0x30.offset(MEMORY.ref(1, t0).offset(0x3L).getSigned() / 2).getAddress();
                FUN_800f9ee8(
                  MEMORY.ref(2, a2).get() + MEMORY.ref(1, t0).offset(0x4L).getSigned() + MEMORY.ref(1, t0).offset(0x8L).getSigned() * t5,
                  MEMORY.ref(2, a1).get() + MEMORY.ref(1, t0).offset(0x5L).getSigned() + MEMORY.ref(1, t0).offset(0x9L).getSigned() * t5,
                  MEMORY.ref(2, v1).get() + MEMORY.ref(1, t0).offset(0x6L).getSigned() + MEMORY.ref(1, t0).offset(0xaL).getSigned() * t5,
                  MEMORY.ref(2, v0).get() + MEMORY.ref(1, t0).offset(0x7L).getSigned() + MEMORY.ref(1, t0).offset(0xbL).getSigned() * t5,
                  t7 & 0xffL,
                  s0 & 0xffL,
                  s3 & 0xffL,
                  s1 != 0
                );
              }

              sp0x28tmp.release();
              sp0x30tmp.release();
            }

            //LAB_800f05d4
            s3 = 0;
            s0 = 0;
            s1 = (data._0e.get() & 0x2000L) > 0 ? 1 : 0;

            //LAB_800f05f4
            for(int i = 0; i < 3; i++) {
              if(i == 0x2L && s1 == 0) {
                s3 = -0xaL;
              }

              //LAB_800f060c
              v1 = sp0x78.offset(s0).getAddress();

              //LAB_800f0610
              v0 = _800c6c2c.get() + spf8;
              FUN_800f8dfc(
                MEMORY.ref(2, v1).offset(0x0L).get() + MEMORY.ref(2, v0).offset(0x0L).get() - centreScreenX_1f8003dc.get(),
                MEMORY.ref(2, v1).offset(0x2L).get() + MEMORY.ref(2, v0).offset(0x2L).get() - centreScreenY_1f8003de.get(),
                MEMORY.ref(1, v1).offset(0x4L).get(),
                MEMORY.ref(1, v1).offset(0x6L).get(),
                MEMORY.ref(2, v1).offset(0x8L).getSigned(),
                MEMORY.ref(2, v1).offset(0xaL).get() + s3,
                0x2cL,
                spec,
                MEMORY.ref(4, s7).offset(0x1cL).get()
              );

              s0 = s0 + 0xcL;
            }

            if(s1 != 0) {
              a0 = data._0a.get();
              fp = spf8;
              s5 = a0 / 100;
              s2 = a0 % 100;

              //LAB_800f0714
              for(s3 = 0; s3 < 2; s3++) {
                if(s3 == 0) {
                  s1 = s2;
                  spf0 = s5 + 0x1L;
                  //LAB_800f0728
                } else if(s5 == 0) {
                  s1 = 0;
                } else {
                  s1 = 100;
                  spf0 = s5;
                }

                //LAB_800f0738
                v1 = (short)s1 * 35 / 100;
                s1 = v1 < 0 ? 0 : v1;

                //LAB_800f0780
                s0 = linkedListAddress_1f8003d8.get();
                FUN_8003b5b0(s0);
                gpuLinkedListSetCommandTransparency(s0, false);
                linkedListAddress_1f8003d8.addu(0x24L);

                a0 = _800c6c2c.get() + fp;
                v0 = MEMORY.ref(2, a0).get() - centreScreenX_1f8003dc.get() + 0x3L;
                MEMORY.ref(2, s0).offset(0x18L).setu(v0);
                MEMORY.ref(2, s0).offset(0x08L).setu(v0);
                v0 = MEMORY.ref(2, a0).get() - centreScreenX_1f8003dc.get() + s1 + 0x3L;
                MEMORY.ref(2, s0).offset(0x20L).setu(v0);
                MEMORY.ref(2, s0).offset(0x10L).setu(v0);
                v0 = MEMORY.ref(2, a0).offset(0x2L).get() - centreScreenY_1f8003de.get() + 0x8L;
                MEMORY.ref(2, s0).offset(0x12L).setu(v0);
                MEMORY.ref(2, s0).offset(0x0aL).setu(v0);
                v0 = MEMORY.ref(2, a0).offset(0x2L).get() - centreScreenY_1f8003de.get() + 0xbL;
                MEMORY.ref(2, s0).offset(0x22L).setu(v0);
                MEMORY.ref(2, s0).offset(0x1aL).setu(v0);

                a0 = spf0 * 2;
                v0 = sp0xb8.offset(spf0 * 0x6L).getAddress();
                MEMORY.ref(1, s0).offset(0x4L).setu(MEMORY.ref(1, v0).offset(0x0L).get());
                MEMORY.ref(1, s0).offset(0x5L).setu(MEMORY.ref(1, v0).offset(0x1L).get());
                MEMORY.ref(1, s0).offset(0x6L).setu(MEMORY.ref(1, v0).offset(0x2L).get());
                MEMORY.ref(1, s0).offset(0xcL).setu(MEMORY.ref(1, v0).offset(0x0L).get());
                MEMORY.ref(1, s0).offset(0xdL).setu(MEMORY.ref(1, v0).offset(0x1L).get());
                MEMORY.ref(1, s0).offset(0xeL).setu(MEMORY.ref(1, v0).offset(0x2L).get());
                a0 = a0 + 0x1L;
                v0 = sp0xb8.offset(a0 * 0x3L).getAddress();
                MEMORY.ref(1, s0).offset(0x14L).setu(MEMORY.ref(1, v0).offset(0x0L).get());
                MEMORY.ref(1, s0).offset(0x15L).setu(MEMORY.ref(1, v0).offset(0x1L).get());
                MEMORY.ref(1, s0).offset(0x16L).setu(MEMORY.ref(1, v0).offset(0x2L).get());
                MEMORY.ref(1, s0).offset(0x1cL).setu(MEMORY.ref(1, v0).offset(0x0L).get());
                MEMORY.ref(1, s0).offset(0x1dL).setu(MEMORY.ref(1, v0).offset(0x1L).get());
                MEMORY.ref(1, s0).offset(0x1eL).setu(MEMORY.ref(1, v0).offset(0x2L).get());
                insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x7cL, s0);
              }

              //LAB_800f0910
              for(int i = 0; i < 4; i++) {
                v0 = _800c6c2c.get() + spf8;
                final long offsetX = MEMORY.ref(2, v0).offset(0x0L).get() - centreScreenX_1f8003dc.get();
                final long offsetY = MEMORY.ref(2, v0).offset(0x2L).get() - centreScreenY_1f8003de.get();
                FUN_800f9ee8(_800fb46c.get(i * 4).get() + offsetX, _800fb46c.get(i * 4 + 1).get() + offsetY, _800fb46c.get(i * 4 + 2).get() + offsetX, _800fb46c.get(i * 4 + 3).get() + offsetY, 0x60L, 0x60L, 0x60L, false);
              }

              if((MEMORY.ref(2, s7).offset(0x6L).get() & 0x8L) != 0) {
                //LAB_800f09ec
                for(int i = 0; i < 4; i++) {
                  v0 = _800c6c2c.get() + spf8;
                  final long offsetX = MEMORY.ref(2, v0).offset(0x0L).get() - centreScreenX_1f8003dc.get();
                  final long offsetY = MEMORY.ref(2, v0).offset(0x2L).get() - centreScreenY_1f8003de.get();
                  FUN_800f9ee8(_800fb47c.get(i * 4).get() + offsetX, _800fb47c.get(i * 4 + 1).get() + offsetY, _800fb47c.get(i * 4 + 2).get() + offsetX, _800fb47c.get(i * 4 + 3).get() + offsetY, 0x80L, 0, 0, false);
                }
              }
            }
          }
        }

        //LAB_800f0aa8
        s7 = s7 + 0x3cL;
        spf8 = spf8 + 0x144L;
        spfc = spfc + 0x4L;
      }

      //LAB_800f0ad4
      if(_800c6c40.getSigned() != -0x1L && (_800c6c40.offset(2, 0x6L).get() & 0x1L) != 0) {
        FUN_800f1268(0x10L, _800fb198.offset(2, _800c6c38.get() * 0x2L).get() - 0x1aL, 0x120L, 0x28L, 0x8L);
      }

      //LAB_800f0b3c
      FUN_800f3dbc();
      FUN_800f5c94();

      v1 = _800c6c34.get();
      if(MEMORY.ref(4, v1).offset(0x4cL).get() != 0) {
        FUN_800eca98(MEMORY.ref(4, v1).offset(0x50L).get(), MEMORY.ref(4, v1).offset(0x54L).get());
        a2 = _800c6c34.get();
        a1 = MEMORY.ref(4, a2).offset(0x54L).get();
        LodString str;
        if((int)a1 == -0x1L) {
          str = _800fb36c.get((int)MEMORY.ref(4, a2).offset(0x50L).get()).deref();
          spf0 = 0x3L;
          FUN_8002a55c(str);
        } else {
          final BtldScriptData27c data;

          //LAB_800f0bb0
          v1 = MEMORY.ref(4, a2).offset(0x50L).get();

          if(v1 == 0x1L) {
            //LAB_800f0ca4
            data = scriptStatePtrArr_800bc1c0.get((int)_8006e398.offset(a1 * 0x4L).offset(0xebcL).get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);

            //LAB_800f0cf0
            for(s4 = 0; s4 < _800c6768.get(); s4++) {
              if(_800c6b78.offset(s4 * 0x4L).get() == MEMORY.ref(4, a2).offset(0x48L).get()) {
                break;
              }
            }

            //LAB_800f0d10
            str = FUN_800f8568(data, _800c69d0.offset(s4 * 0x2cL).getAddress());
            FUN_8002a55c(str);
            spf0 = FUN_800f8ca0(data._1c.get());
          } else if((int)v1 >= 0x2L || v1 != 0) {
            //LAB_800f0d58
            //LAB_800f0d5c
            s4 = _800c6c34.deref(4).offset(0x54L).get();
            a1 = _8006e398.offset(s4 * 0x4L).getAddress();
            data = scriptStatePtrArr_800bc1c0.get((int)MEMORY.ref(4, a1).offset(0xe0cL).get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
            if((scriptStatePtrArr_800bc1c0.get((int)MEMORY.ref(4, a1).offset(0xe0cL).get()).deref().ui_60.get() & 0x4L) == 0) {
              str = _800fb378.get(data._272.get()).deref();
              spf0 = sp0xa0.offset(2, data._272.get() * 0x2L).get();

              if(data._272.get() == 0 && ((gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xffL) >>> 7) != 0 && (scriptStatePtrArr_800bc1c0.get((int)MEMORY.ref(4, a1).offset(0xe40L).get()).deref().ui_60.get() & 0x2L) != 0) {
                spf0 = sp0xa0.offset(0x12L).get();
              }
            } else {
              //LAB_800f0e24
              str = FUN_800f8568(data, _800c69d0.offset(s4 * 0x2cL).getAddress());
              spf0 = FUN_800f8ca0(data._1c.get());
            }

            //LAB_800f0e58
            FUN_8002a55c(str);
          } else {
            data = scriptStatePtrArr_800bc1c0.get((int)_8006e398.offset(a1 * 0x4L).offset(0xe40L).get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
            str = _800fb378.get(data._272.get()).deref();
            FUN_8002a55c(str);

            if(data._272.get() != 0) {
              spf0 = sp0xa0.offset(2, data._272.get() * 0x2L).get();
            } else {
              if(((gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xffL) >>> 7) != 0) {
                if((scriptStatePtrArr_800bc1c0.get((int)_8006e398.offset(_800c6c34.deref(4).offset(0x54L).get() * 0x4L).offset(0xe40L).get()).deref().ui_60.get() & 0x2L) != 0) {
                  spf0 = sp0xa0.offset(0x12L).get();
                }
              }
            }
          }

          //LAB_800f0e60
          a0 = data._0e.get();

          if((a0 & 0xffL) != 0) {
            if((_800bb0fc.get() & 0x10L) != 0) {
              s0 = 0x80L;

              //LAB_800f0e94
              for(s4 = 0; s4 < 8; s4++) {
                if((a0 & s0) != 0) {
                  break;
                }

                s0 = (int)s0 >> 1;
              }

              //LAB_800f0eb4
              if(s4 == 0x8L) {
                s4 = 0x7L;
              }

              //LAB_800f0ec0
              str = _800fb3a0.get((int)s4).deref();

              //LAB_800f0ed0
              FUN_8002a55c(str);
            }
          }
        }

        //LAB_800f0ed8
        FUN_800f1268(0x2cL, 0x17L, 0xe8L, 0xeL, (short)spf0);
        renderText(str, 160 - FUN_8002a59c(str) / 2, 24, 0);
      }

      sp0x68tmp.release();
    }

    sp0x38tmp.release();
    sp0x78tmp.release();
    sp0xa0tmp.release();
    sp0xb8tmp.release();

    //LAB_800f0f2c
  }

  @Method(0x800f0f5cL)
  public static void FUN_800f0f5c(long a0) {
    long v0;
    long v1;

    //LAB_800f0fe4
    //LAB_800f0fe8
    final int[] sp0x20 = new int[80];
    for(int i = 0; i < sp0x20.length; i++) {
      sp0x20[i] = (int)_800c6f4c.offset(i * 0x2L).get();
    }

    //LAB_800f1014
    final long[] sp0x10 = new long[4];
    final long[] sp0x18 = new long[4];
    v0 = MEMORY.ref(2, a0).offset(0x8L).get() + 0x1L;
    sp0x10[0] = v0;
    sp0x10[2] = v0;
    v0 = MEMORY.ref(2, a0).offset(0x10L).get() - 0x1L;
    sp0x10[1] = v0;
    sp0x10[3] = v0;
    v0 = MEMORY.ref(2, a0).offset(0xaL).get();
    sp0x18[0] = v0;
    sp0x18[1] = v0;
    v0 = MEMORY.ref(2, a0).offset(0x1aL).get();
    sp0x18[2] = v0;
    sp0x18[3] = v0;

    //LAB_800f1060
    for(int i = 0; i < 8; i++) {
      long s0 = linkedListAddress_1f8003d8.get();
      linkedListAddress_1f8003d8.addu(0x28L);
      FUN_8003b590(s0);
      gpuLinkedListSetCommandTransparency(s0, false);
      MEMORY.ref(1, s0).offset(0x6L).setu(0x80L);
      MEMORY.ref(1, s0).offset(0x5L).setu(0x80L);
      MEMORY.ref(1, s0).offset(0x4L).setu(0x80L);
      if(i == 0x5L || i == 0x7L) {
        //LAB_800f10ac
        v1 = sp0x10[sp0x20[i * 10]] - sp0x20[i * 10 + 4];
        MEMORY.ref(2, s0).offset(0x20L).setu(v1);
        MEMORY.ref(2, s0).offset(0x10L).setu(v1);
        v1 = sp0x10[sp0x20[i * 10 + 1]] + sp0x20[i * 10 + 4];
        MEMORY.ref(2, s0).offset(0x18L).setu(v1);
        MEMORY.ref(2, s0).offset(0x8L).setu(v1);
        v0 = sp0x20[i * 10 + 2];
        MEMORY.ref(1, s0).offset(0x24L).setu(v0);
        MEMORY.ref(1, s0).offset(0x14L).setu(v0);
        v1 = sp0x20[i * 10 + 2] + sp0x20[i * 10 + 6] - 0x1L;
        MEMORY.ref(1, s0).offset(0x1cL).setu(v1);
        MEMORY.ref(1, s0).offset(0xcL).setu(v1);
      } else {
        //LAB_800f1128
        v1 = sp0x10[sp0x20[i * 10]] - sp0x20[i * 10 + 4];
        MEMORY.ref(2, s0).offset(0x18L).setu(v1);
        MEMORY.ref(2, s0).offset(0x8L).setu(v1);
        v1 = sp0x10[sp0x20[i * 10 + 1]] + sp0x20[i * 10 + 4];
        MEMORY.ref(2, s0).offset(0x20L).setu(v1);
        MEMORY.ref(2, s0).offset(0x10L).setu(v1);
        v0 = sp0x20[i * 10 + 2];
        MEMORY.ref(1, s0).offset(0x1cL).setu(v0);
        MEMORY.ref(1, s0).offset(0xcL).setu(v0);
        v1 = sp0x20[i * 10 + 2] + sp0x20[i * 10 + 6];
        MEMORY.ref(1, s0).offset(0x24L).setu(v1);
        MEMORY.ref(1, s0).offset(0x14L).setu(v1);
      }

      //LAB_800f11a0
      v1 = sp0x18[sp0x20[i * 10]] - sp0x20[i * 10 + 5];
      MEMORY.ref(2, s0).offset(0x12L).setu(v1);
      MEMORY.ref(2, s0).offset(0xaL).setu(v1);
      v0 = sp0x18[sp0x20[i * 10 + 1]] + sp0x20[i * 10 + 5];
      MEMORY.ref(2, s0).offset(0x22L).setu(v0);
      MEMORY.ref(2, s0).offset(0x1aL).setu(v0);
      v0 = sp0x20[i * 10 + 3];
      MEMORY.ref(1, s0).offset(0x15L).setu(v0);
      MEMORY.ref(1, s0).offset(0xdL).setu(v0);
      MEMORY.ref(2, s0).offset(0xeL).setu(0x7c6dL);
      v1 = sp0x20[i * 10 + 3] + sp0x20[i * 10 + 7];
      MEMORY.ref(1, s0).offset(0x25L).setu(v1);
      MEMORY.ref(1, s0).offset(0x1dL).setu(v1);
      MEMORY.ref(2, s0).offset(0x16L).setu(GetTPage(0, 0, 0x2c0L, 0x100L));
      insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x7cL, s0);
    }
  }

  @Method(0x800f1268L)
  public static void FUN_800f1268(final long a0, final long a1, final long a2, final long a3, final long a4) {
    final byte[] sp0x10 = new byte[0x1b];
    for(int i = 0; i < sp0x10.length; i++) {
      sp0x10[i] = (byte)_800c6fec.offset(i).get();
    }

    final int s5;
    if((a4 & 0xfL) < 9) {
      s5 = (int)(a4 & 0xf);
    } else {
      s5 = 8;
    }

    //LAB_800f1340
    long s0 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x24L);
    FUN_8003b5b0(s0);
    gpuLinkedListSetCommandTransparency(s0, true);

    long s1 = a0 + a2;
    long v0 = a0 - centreScreenX_1f8003dc.get();
    MEMORY.ref(2, s0).offset(0x18L).setu(v0);
    MEMORY.ref(2, s0).offset(0x8L).setu(v0);

    v0 = s1 - centreScreenX_1f8003dc.get();
    MEMORY.ref(2, s0).offset(0x20L).setu(v0);
    MEMORY.ref(2, s0).offset(0x10L).setu(v0);

    v0 = a1 - centreScreenY_1f8003de.get();
    MEMORY.ref(2, s0).offset(0x12L).setu(v0);
    MEMORY.ref(2, s0).offset(0xaL).setu(v0);

    long s4 = a1 + a3;
    v0 = s4 - centreScreenY_1f8003de.get();
    MEMORY.ref(2, s0).offset(0x22L).setu(v0);
    MEMORY.ref(2, s0).offset(0x1aL).setu(v0);

    long v1 = sp0x10[s5 * 3];
    MEMORY.ref(1, s0).offset(0x14L).setu(v1);
    MEMORY.ref(1, s0).offset(0xcL).setu(v1);

    v1 = sp0x10[s5 * 3 + 1];
    MEMORY.ref(1, s0).offset(0x15L).setu(v1);
    MEMORY.ref(1, s0).offset(0xdL).setu(v1);

    v0 = sp0x10[s5 * 3 + 2];
    MEMORY.ref(1, s0).offset(0x1cL).setu(0);
    MEMORY.ref(1, s0).offset(0x4L).setu(0);
    MEMORY.ref(1, s0).offset(0x1dL).setu(0);
    MEMORY.ref(1, s0).offset(0x5L).setu(0);
    MEMORY.ref(1, s0).offset(0x1eL).setu(0);
    MEMORY.ref(1, s0).offset(0x6L).setu(0);
    MEMORY.ref(1, s0).offset(0x16L).setu(v0);
    MEMORY.ref(1, s0).offset(0xeL).setu(v0);
    FUN_800f0f5c(s0);

    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x7cL, s0);
    s0 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x24L);
    FUN_8003b5b0(s0);
    gpuLinkedListSetCommandTransparency(s0, true);

    v0 = a0 - centreScreenX_1f8003dc.get();
    MEMORY.ref(2, s0).offset(0x18L).setu(v0);
    MEMORY.ref(2, s0).offset(0x8L).setu(v0);

    s1 = s1 - centreScreenX_1f8003dc.get();
    MEMORY.ref(2, s0).offset(0x20L).setu(s1);
    MEMORY.ref(2, s0).offset(0x10L).setu(s1);

    v0 = a1 - centreScreenY_1f8003de.get();
    MEMORY.ref(2, s0).offset(0x12L).setu(v0);
    MEMORY.ref(2, s0).offset(0xaL).setu(v0);

    MEMORY.ref(1, s0).offset(0x14L).setu(0);
    MEMORY.ref(1, s0).offset(0xcL).setu(0);
    MEMORY.ref(1, s0).offset(0x15L).setu(0);
    MEMORY.ref(1, s0).offset(0xdL).setu(0);
    MEMORY.ref(1, s0).offset(0x16L).setu(0);
    MEMORY.ref(1, s0).offset(0xeL).setu(0);
    MEMORY.ref(1, s0).offset(0x1cL).setu(0);
    MEMORY.ref(1, s0).offset(0x4L).setu(0);
    MEMORY.ref(1, s0).offset(0x1dL).setu(0);
    MEMORY.ref(1, s0).offset(0x5L).setu(0);
    MEMORY.ref(1, s0).offset(0x1eL).setu(0);
    MEMORY.ref(1, s0).offset(0x6L).setu(0);

    s4 = s4 - centreScreenY_1f8003de.get();
    MEMORY.ref(2, s0).offset(0x22L).setu(s4);
    MEMORY.ref(2, s0).offset(0x1aL).setu(s4);

    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x7cL, s0);

    final long a1_0 = linkedListAddress_1f8003d8.get();
    MEMORY.ref(1, a1_0).offset(0x3L).setu(0x1L);
    MEMORY.ref(4, a1_0).offset(0x4L).setu(0xe100_0200L | (_800bb112.get() | 0xbL) & 0x9ffL);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x7cL, a1_0);
    linkedListAddress_1f8003d8.addu(0x8L);
  }

  @Method(0x800f1550L)
  public static void FUN_800f1550(long a0, long a1, long a2, long a3) {
    long v0;
    long v1;
    long t1;
    long t2;
    long s5;
    t2 = a2;
    v0 = 0x800c_0000L;
    s5 = v0 + 0x7008L;
    final short[] sp0x08 = new short[5];
    for(int i = 0; i < sp0x08.length; i++) {
      sp0x08[i] = (short)MEMORY.ref(2, s5).offset(i * 0x2L).get();
    }

    v0 = 0x800c_0000L;
    s5 = v0 + 0x7014L;
    final long[] sp0x18 = new long[10];
    for(int i = 0; i < sp0x18.length; i++) {
      sp0x18[i] = MEMORY.ref(2, s5).offset(i * 0x2L).get();
    }

    v0 = 0x800c_0000L;
    s5 = v0 + 0x7028L;
    final long[] sp0x30 = new long[10];
    for(int i = 0; i < sp0x30.length; i++) {
      sp0x30[i] = MEMORY.ref(2, s5).offset(i * 0x2L).get();
    }

    v1 = sp0x08[(int)a1];
    if(v1 == 0x2L) {
      //LAB_800f16a0
      if((short)a2 >= 0x64L) {
        t2 = 0x63L;
      }
    } else if((int)v1 >= 0x3L) {
      //LAB_800f16bc
      //LAB_800f16c0
      if((short)t2 >= 0x2710L) {
        t2 = 0x270fL;
      }
    } else if(v1 != 0x1L) {
      if((short)t2 >= 0x2710L) {
        t2 = 0x270fL;
      }
    } else {
      if((short)a2 >= 0xaL) {
        t2 = 0x9L;
      }
    }

    //LAB_800f16d4
    //LAB_800f16d8
    if((short)t2 <= 0) {
      t2 = 0;
    }

    //LAB_800f16e4
    v1 = _800c6c2c.get() + a0 * 0x144L;

    final short[] sp0x00 = new short[4];

    //LAB_800f171c
    for(int i = 0; i < 4; i++) {
      v0 = v1 + a1 * 0x40L + i * 0x10L;
      MEMORY.ref(2, v0).offset(0x4L).setu(-0x1L);
      sp0x00[i] = -1;
    }

    a2 = 0x1L;

    //LAB_800f1768
    for(t1 = 0; t1 < sp0x08[(int)a1] - 0x1L; t1++) {
      a2 = a2 * 10;
    }

    //LAB_800f1780
    //LAB_800f17b0
    for(int i = 0; i < sp0x08[(int)a1]; i++) {
      v1 = (int)t2 / (int)a2;
      t2 = (int)t2 % (int)a2;
      a2 = a2 / 10;
      sp0x00[i] = (short)v1;
    }

    //LAB_800f1800
    //LAB_800f1828
    for(a2 = 0; a2 < sp0x08[(int)a1] - 0x1L; a2++) {
      if(sp0x00[(int)a2] != 0) {
        break;
      }
    }

    //LAB_800f1848
    //LAB_800f184c
    long a3_0 = _800c6c2c.get() + a0 * 0x144L;

    //LAB_800f18cc
    for(t1 = 0; t1 < sp0x08[(int)a1] && a2 < sp0x08[(int)a1]; t1++, a2++) {
      if(a1 == 0x1L || (int)a1 > 0 && (int)a1 < 0x5L && (int)a1 >= 0x3L) {
        //LAB_800f18f0
        MEMORY.ref(2, a3_0).offset(a1 * 0x40L).offset(t1 * 0x10L).offset(0x6L).setu(sp0x18[(int)(a1 * 2)] + t1 * 5);
      } else {
        MEMORY.ref(2, a3_0).offset(a1 * 0x40L).offset(t1 * 0x10L).offset(0x6L).setu(sp0x18[(int)(a1 * 2)] + a2 * 5);
      }

      //LAB_800f1920
      final long a0_0 = a3_0 + a1 * 0x40L + t1 * 0x10L;
      MEMORY.ref(2, a0_0).offset(0x8L).setu(sp0x18[(int)(a1 * 2 + 1)]);
      MEMORY.ref(2, a0_0).offset(0xcL).setu(0x20L);
      MEMORY.ref(2, a0_0).offset(0xeL).setu(0x8L);
      MEMORY.ref(2, a0_0).offset(0x10L).setu(0x8L);
      MEMORY.ref(2, a0_0).offset(0xaL).setu(sp0x30[sp0x00[(int)a2]]);
      if(a3 == 0x1L) {
        //LAB_800f1984
        v0 = 0x80L;
      } else if((int)a3 < 0x2L) {
        v0 = 0x2dL;
      } else if(a3 == 0x2L) {
        //LAB_800f198c
        v0 = 0x82L;
      } else if(a3 != 0x3L) {
        v0 = 0x2dL;
      } else {
        //LAB_800f1994
        v0 = 0x83L;
      }

      //LAB_800f1998
      MEMORY.ref(2, a0_0).offset(0x12L).setu(v0);

      //LAB_800f199c
      MEMORY.ref(2, a3_0).offset(0x4L).offset(a1 * 0x40L).offset(t1 * 0x10L).setu(sp0x00[(int)a2]);
    }

    //LAB_800f19e0
  }

  @Method(0x800f1a00L)
  public static void FUN_800f1a00(final long a0) {
    if(a0 != 1) {
      //LAB_800f1a10
      long v1 = _800c6c40.getAddress();

      //LAB_800f1a28
      for(int i = 0; i < 3; i++) {
        if(MEMORY.ref(2, v1).offset(0x0L).getSigned() != -0x1L) {
          MEMORY.ref(4, v1).offset(0x1cL).setu(0);
          MEMORY.ref(2, v1).offset(0x6L).and(0xffff_fffeL).and(0xffff_fffdL);
        }

        //LAB_800f1a4c
        v1 = v1 + 0x3cL;
      }

      return;
    }

    //LAB_800f1a64
    long v1 = _800c6c40.getAddress();

    //LAB_800f1a70
    for(int i = 0; i < 3; i++) {
      if(MEMORY.ref(2, v1).offset(0x0L).getSigned() != -0x1L) {
        MEMORY.ref(4, v1).offset(0x1cL).setu(0);
        MEMORY.ref(2, v1).offset(0x6L).oru(0x3L);
      }

      //LAB_800f1a90
      v1 = v1 + 0x3cL;
    }
  }

  @Method(0x800f3354L)
  public static void FUN_800f3354(long a0, long a1, long a2, long a3, long a4, long a5, long a6, long a7) {
    assert false;
  }

  @Method(0x800f3940L)
  public static void FUN_800f3940() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long lo;
    s3 = 0;
    s1 = 0x800c_0000L;
    s2 = -0x1L;
    s5 = 0x61L;
    s4 = 0x63L;
    s0 = s3;

    //LAB_800f3978
    do {
      v0 = MEMORY.ref(4, s1).offset(0x6b5cL).get();

      v1 = s0 + v0;
      v0 = MEMORY.ref(2, v1).offset(0x2L).getSigned();

      v0 = v0 & 0x8000L;
      if(v0 != 0) {
        v0 = MEMORY.ref(2, v1).offset(0x0L).getSigned();

        if(v0 != 0) {
          v0 = MEMORY.ref(4, v1).offset(0x4L).get();

          if(v0 != s2) {
            final ScriptState<BtldScriptData27c> state = scriptStatePtrArr_800bc1c0.get((int)v0).derefAs(ScriptState.classFor(BtldScriptData27c.class));
            final BtldScriptData27c data = state.innerStruct_00.deref();

            a2 = -0x280L;
            if((state.ui_60.get() & 0x4L) != 0) {
              v1 = data._78.getX();
              v1 = -v1;
              v0 = v1 << 1;
              v0 = v0 + v1;
              v0 = v0 << 3;
              v0 = v0 + v1;
              v1 = data._78.getY();
              a3 = v0 << 2;
              v1 = -v1;
              v0 = v1 << 1;
              v0 = v0 + v1;
              v0 = v0 << 3;
              v0 = v0 + v1;
              v1 = data._78.getZ();
              a2 = v0 << 2;
              v1 = -v1;
              v0 = v1 << 1;
              v0 = v0 + v1;
              v0 = v0 << 3;
              v0 = v0 + v1;
              v0 = v0 << 2;
            } else {
              //LAB_800f3a3c
              v0 = 0;
              a3 = v0;
            }

            //LAB_800f3a44
            v0 = FUN_800ec7e4(data._148, (short)v0, (short)a2, (short)a3);
            v1 = v0 << 16;
            a1 = 0x1f80_0000L;
            v1 = (int)v1 >> 16;
            a2 = MEMORY.ref(4, s1).offset(0x6b5cL).get();
            a0 = MEMORY.ref(2, a1).offset(0x3dcL).getSigned();
            a1 = a1 + 0x3dcL;
            a2 = s0 + a2;
            v1 = v1 + a0;
            MEMORY.ref(4, a2).offset(0x1cL).setu(v1);
            a0 = v1;
            v1 = MEMORY.ref(2, a1).offset(0x2L).getSigned();
            v0 = (int)v0 >> 16;
            v0 = v0 + v1;
            MEMORY.ref(4, a2).offset(0x20L).setu(v0);
            v0 = FUN_800fa068(a0);
            v1 = MEMORY.ref(4, s1).offset(0x6b5cL).get();

            v1 = s0 + v1;
            a0 = MEMORY.ref(4, v1).offset(0x20L).get();
            MEMORY.ref(4, v1).offset(0x1cL).setu(v0);
            v0 = FUN_800fa090(a0);
            v1 = MEMORY.ref(4, s1).offset(0x6b5cL).get();

            v1 = s0 + v1;
            MEMORY.ref(4, v1).offset(0x20L).setu(v0);
          }

          //LAB_800f3ac8
          v0 = MEMORY.ref(4, s1).offset(0x6b5cL).get();

          a1 = s0 + v0;
          a0 = MEMORY.ref(2, a1).offset(0x0L).getSigned();

          if(a0 == s5) {
            //LAB_800f3c34
            v0 = MEMORY.ref(4, a1).offset(0x14L).get();

            if((int)v0 <= 0) {
              v0 = 0x64L;
              MEMORY.ref(2, a1).offset(0x0L).setu(v0);
            } else {
              v0 = v0 - 0x1L;

              //LAB_800f3c50
              v1 = MEMORY.ref(4, a1).offset(0xcL).get();
              a0 = MEMORY.ref(1, a1).offset(0x18L).get();
              MEMORY.ref(4, a1).offset(0x14L).setu(v0);
              v0 = 0xff00_0000L;
              a0 = v1 - a0;
              v0 = v1 & v0;
              a0 = a0 & 0xffL;
              v1 = a0 << 16;
              v0 = v0 | v1;
              v1 = a0 << 8;
              v0 = v0 | v1;
              v1 = v0 | a0;
              MEMORY.ref(4, a1).offset(0xcL).setu(v1);
            }
          } else {
            if((int)a0 > 0x63L) {
              //LAB_800f3b04
              if(a0 != s4) {
                if((int)a0 < 0x63L) {
                  //LAB_800f3c88
                  v0 = MEMORY.ref(2, a1).offset(0x2L).get();

                  v0 = v0 & 0x1L;
                  if(v0 != 0) {
                    MEMORY.ref(2, a1).offset(0x0L).setu(s4);
                  } else {
                    //LAB_800f3ca4
                    v0 = MEMORY.ref(4, a1).offset(0x14L).get();
                    v0 = v0 - 0x1L;
                    MEMORY.ref(4, a1).offset(0x14L).setu(v0);
                    if((int)v0 <= 0) {
                      v1 = MEMORY.ref(4, a1).offset(0x10L).get();

                      if((int)v1 > 0 && (int)v1 < 0x3L) {
                        v0 = 0x8008_0000L;
                        v1 = MEMORY.ref(4, v0).offset(-0x5c48L).get();
                        v0 = 0x3cL;
                        lo = (int)v0 / (int)v1;
                        v0 = lo;

                        v1 = v0 >>> 31;
                        v0 = v0 + v1;
                        a2 = (int)v0 >> 1;
                        v1 = 0x60L;
                        lo = (int)v1 / (int)a2;
                        v1 = lo;
                        MEMORY.ref(2, a1).offset(0x0L).setu(s5);
                        v0 = 0x60_0000L;
                        v0 = v0 | 0x6060L;
                        MEMORY.ref(4, a1).offset(0xcL).setu(v0);
                        v0 = 0x1L;
                        MEMORY.ref(4, a1).offset(0x8L).setu(v0);
                        MEMORY.ref(4, a1).offset(0x14L).setu(a2);
                        MEMORY.ref(4, a1).offset(0x18L).setu(v1);
                      } else {
                        //LAB_800f3d24
                        v0 = MEMORY.ref(4, s1).offset(0x6b5cL).get();
                        v1 = 0x64L;

                        //LAB_800f3d2c
                        v0 = s0 + v0;
                        MEMORY.ref(2, v0).offset(0x0L).setu(v1);
                      }
                    }
                  }
                } else {
                  v0 = 0x64L;
                  if(a0 == v0) {
                    v0 = 0x80_0000L;

                    //LAB_800f3d38
                    v0 = v0 | 0x8080L;
                    a2 = 0;
                    v1 = a1;
                    MEMORY.ref(4, v1).offset(0x18L).setu(s2);
                    MEMORY.ref(4, v1).offset(0x14L).setu(s2);
                    MEMORY.ref(2, v1).offset(0x0L).setu(0);
                    MEMORY.ref(2, v1).offset(0x2L).setu(0);
                    MEMORY.ref(4, v1).offset(0x4L).setu(s2);
                    MEMORY.ref(4, v1).offset(0x8L).setu(0);
                    MEMORY.ref(4, v1).offset(0xcL).setu(v0);

                    //LAB_800f3d60
                    do {
                      MEMORY.ref(2, v1).offset(0x30L).setu(s2);
                      MEMORY.ref(4, v1).offset(0x24L).setu(0);
                      MEMORY.ref(4, v1).offset(0x28L).setu(0);
                      MEMORY.ref(4, v1).offset(0x2cL).setu(0);
                      MEMORY.ref(2, v1).offset(0x40L).setu(0);
                      a2 = a2 + 0x1L;
                      v1 = v1 + 0x20L;
                    } while((int)a2 < 0x5L);
                  }
                }
              }
            } else {
              v0 = 0x1L;
              if(a0 == v0) {
                v0 = 0x2L;
                //LAB_800f3b24
                v1 = MEMORY.ref(4, a1).offset(0x10L).get();

                if(v1 != a0 && v1 == v0) {
                  MEMORY.ref(2, a1).offset(0x0L).setu(v1);
                } else {
                  //LAB_800f3b44
                  v0 = MEMORY.ref(4, s1).offset(0x6b5cL).get();
                  v1 = 0x62L;
                  v0 = s0 + v0;
                  MEMORY.ref(2, v0).offset(0x0L).setu(v1);
                }
              } else {
                v0 = 0x2L;
                if(a0 == v0) {
                  a2 = 0;
                  //LAB_800f3b50
                  do {
                    v0 = MEMORY.ref(2, a1).offset(0x30L).getSigned();

                    if(v0 == s2) {
                      break;
                    }

                    a0 = MEMORY.ref(4, a1).offset(0x24L).get();

                    v0 = a0 & 0x1L;
                    if(v0 != 0) {
                      v0 = a0 & 0x2L;
                      if(v0 != 0) {
                        v0 = MEMORY.ref(4, a1).offset(0x2cL).get();

                        if((int)v0 < 0x5L) {
                          v0 = MEMORY.ref(2, a1).offset(0x34L).get();
                          a0 = MEMORY.ref(2, a1).offset(0x2cL).get();
                          v1 = MEMORY.ref(4, a1).offset(0x2cL).get();
                          v0 = v0 + a0;
                          v1 = v1 + 0x1L;
                          MEMORY.ref(2, a1).offset(0x34L).setu(v0);
                          MEMORY.ref(4, a1).offset(0x2cL).setu(v1);
                        }
                      } else {
                        v0 = a0 | 0x8002L;

                        //LAB_800f3bb0
                        v1 = MEMORY.ref(2, a1).offset(0x34L).getSigned();
                        MEMORY.ref(4, a1).offset(0x24L).setu(v0);
                        v0 = -0x4L;
                        MEMORY.ref(4, a1).offset(0x2cL).setu(v0);
                        MEMORY.ref(4, a1).offset(0x28L).setu(v1);
                      }
                    } else {
                      //LAB_800f3bc8
                      v1 = MEMORY.ref(4, a1).offset(0x2cL).get();
                      v0 = MEMORY.ref(4, a1).offset(0x28L).get();

                      if(v1 == v0) {
                        v0 = a0 | 0x1L;
                        MEMORY.ref(4, a1).offset(0x24L).setu(v0);
                      }

                      //LAB_800f3be0
                      v0 = MEMORY.ref(4, a1).offset(0x2cL).get();

                      v0 = v0 + 0x1L;
                      MEMORY.ref(4, a1).offset(0x2cL).setu(v0);
                    }

                    //LAB_800f3bf0
                    a2 = a2 + 0x1L;
                    a1 = a1 + 0x20L;
                  } while((int)a2 < 0x5L);

                  //LAB_800f3c00
                  v0 = MEMORY.ref(4, s1).offset(0x6b5cL).get();

                  a0 = s0 + v0;
                  v0 = MEMORY.ref(4, a0).offset(0x14L).get();

                  v0 = v0 - 0x1L;
                  MEMORY.ref(4, a0).offset(0x14L).setu(v0);
                  if((int)v0 <= 0) {
                    v1 = MEMORY.ref(4, a0).offset(0x18L).get();
                    v0 = 0x62L;
                    MEMORY.ref(2, a0).offset(0x0L).setu(v0);
                    MEMORY.ref(4, a0).offset(0x14L).setu(v1);
                  }
                }
              }
            }
          }
        }
      }

      //LAB_800f3d84
      s0 = s0 + 0xc4L;

      //LAB_800f3d88
      s3 = s3 + 0x1L;
    } while((int)s3 < 0xcL);
  }

  @Method(0x800f3dbcL)
  public static void FUN_800f3dbc() {
    //LAB_800f3e20
    long fp = 0;
    for(long sp20 = 0; sp20 < 12; sp20++) {
      long a1 = _800c6b5c.get() + fp;

      if((MEMORY.ref(2, a1).offset(0x2L).getSigned() & 0x8000L) != 0) {
        if(MEMORY.ref(2, a1).offset(0x0L).getSigned() != 0) {
          long sp28 = MEMORY.ref(2, a1).offset(0x8L).get();
          long sp30 = MEMORY.ref(1, a1).offset(0xeL).get();
          long sp31 = MEMORY.ref(1, a1).offset(0xdL).get();
          long sp32 = MEMORY.ref(1, a1).offset(0xcL).get();

          //LAB_800f3e80
          long s6 = 0;
          for(long s7 = 0; s7 < 5; s7++) {
            long a0 = _800c6b5c.get() + fp + s6;

            if(MEMORY.ref(2, a0).offset(0x30L).getSigned() == -0x1L) {
              break;
            }

            if((MEMORY.ref(4, a0).offset(0x24L).get() & 0x8000L) != 0) {
              long s3 = 0x1L;

              //LAB_800f3ec0
              while(true) {
                long s0 = linkedListAddress_1f8003d8.get();
                FUN_8003b590(s0);
                gpuLinkedListSetCommandTransparency(s0, sp28 != 0);
                linkedListAddress_1f8003d8.addu(0x28L);

                MEMORY.ref(1, s0).offset(0x4L).setu(sp30);
                MEMORY.ref(1, s0).offset(0x5L).setu(sp31);
                MEMORY.ref(1, s0).offset(0x6L).setu(sp32);
                long v1 = _800c6b5c.get() + fp;
                a1 = MEMORY.ref(4, v1).offset(0x1cL).get() - centreScreenX_1f8003dc.getSigned();
                a0 = v1 + s6;
                long a2 = MEMORY.ref(4, v1).offset(0x20L).get();
                long v0 = MEMORY.ref(2, a0).offset(0x32L).get() + a1;
                MEMORY.ref(2, s0).offset(0x18L).setu(v0);
                MEMORY.ref(2, s0).offset(0x8L).setu(v0);
                v1 = MEMORY.ref(2, a0).offset(0x32L).get() + MEMORY.ref(2, a0).offset(0x3aL).get() + a1;
                MEMORY.ref(2, s0).offset(0x20L).setu(v1);
                MEMORY.ref(2, s0).offset(0x10L).setu(v1);
                a2 = a2 - centreScreenY_1f8003de.getSigned();
                v0 = MEMORY.ref(2, a0).offset(0x34L).get() + a2;
                MEMORY.ref(2, s0).offset(0x12L).setu(v0);
                MEMORY.ref(2, s0).offset(0xaL).setu(v0);
                v1 = MEMORY.ref(2, a0).offset(0x34L).get() + MEMORY.ref(2, a0).offset(0x3cL).get() + a2;
                MEMORY.ref(2, s0).offset(0x22L).setu(v1);
                MEMORY.ref(2, s0).offset(0x1aL).setu(v1);
                v0 = MEMORY.ref(1, a0).offset(0x36L).get();
                MEMORY.ref(1, s0).offset(0x1cL).setu(v0);
                MEMORY.ref(1, s0).offset(0xcL).setu(v0);
                v0 = _800c6b5c.get() + fp + s6;
                v1 = MEMORY.ref(1, v0).offset(0x36L).get() + MEMORY.ref(1, v0).offset(0x3aL).get();
                MEMORY.ref(1, s0).offset(0x24L).setu(v1);
                MEMORY.ref(1, s0).offset(0x14L).setu(v1);
                v0 = _800c6b5c.get() + fp + s6;
                v0 = MEMORY.ref(1, v0).offset(0x38L).get();
                MEMORY.ref(1, s0).offset(0x15L).setu(v0);
                MEMORY.ref(1, s0).offset(0xdL).setu(v0);
                v0 = _800c6b5c.get() + fp + s6;
                v1 = MEMORY.ref(1, v0).offset(0x38L).get() + MEMORY.ref(1, v0).offset(0x3cL).get();
                MEMORY.ref(1, s0).offset(0x25L).setu(v1);
                MEMORY.ref(1, s0).offset(0x1dL).setu(v1);
                v0 = _800c6b5c.get() + fp;
                v0 = v0 + s6;
                v1 = MEMORY.ref(2, v0).offset(0x3eL).getSigned();

                long t1;
                long t0;
                if((int)v1 >= 0x80L) {
                  t1 = 0x1L;
                  t0 = v1 - 0x80L;
                } else {
                  t1 = 0;

                  //LAB_800f4044
                  t0 = v1;
                }

                //LAB_800f4048
                long a3 = t0;

                //LAB_800f4058
                t0 = t0 / 0x10L;

                //LAB_800f4068
                a2 = (_800c7114.offset(2, (t1 * 0x2L + 0x1L) * 0x4L).get() + a3 % 0x10L) * 0x40L;
                v0 = _800c7114.offset(4, t1 * 0x8L).get() + t0 * 0x10L;
                v0 = v0 & 0x3f0L;
                v0 = (int)v0 >> 4;
                a2 = a2 | v0;
                MEMORY.ref(2, s0).offset(0xeL).setu(a2);
                v0 = GetTPage(0, s3, 704, 496);
                MEMORY.ref(2, s0).offset(0x16L).setu(v0);
                insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x1cL, s0);

                v0 = _800c6b5c.get() + fp;

                if((MEMORY.ref(2, v0).offset(0x0L).get() & 0x61L) == 0) {
                  //LAB_800f4118
                  break;
                }

                if(s3 == 0x2L) {
                  break;
                }

                //LAB_800f4110
                s3 = s3 + 0x1L;

                //LAB_800f411c
              }
            }

            //LAB_800f4124
            s6 = s6 + 0x20L;
          }
        }
      }

      //LAB_800f4134
      fp = fp + 0xc4L;
    }
  }

  @Method(0x800f417cL)
  public static void FUN_800f417c() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long s0;
    long s1;
    long s2;
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x677cL).get();
    s0 = 0;
    if((int)v0 > 0) {
      s2 = -0x1L;
      v0 = 0x800c_0000L;
      s1 = v0 + 0x6c40L;

      //LAB_800f41ac
      do {
        v0 = MEMORY.ref(2, s1).offset(0x0L).getSigned();

        if(v0 == s2) {
          v0 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v0).offset(-0x1a30L).get();
          v0 = 0x1L;
          if(v1 == v0) {
            a0 = s0;
            FUN_800ef8d8(a0);
          }
        }
        v0 = 0x800c_0000L;

        //LAB_800f41dc
        v0 = MEMORY.ref(4, v0).offset(0x677cL).get();
        s0 = s0 + 0x1L;
        s1 = s1 + 0x3cL;
      } while((int)s0 < (int)v0);

      s0 = 0;
    }

    //LAB_800f41f4
    //LAB_800f41f8
    do {
      s0 = s0 + 0x1L;
    } while((int)s0 < 0x3L);

    s0 = 0;
    a2 = -0x1L;
    v0 = 0x800c_0000L;
    a1 = MEMORY.ref(4, v0).offset(0x6c2cL).get();
    a0 = 0x3fL;
    v0 = 0x800c_0000L;
    v1 = v0 + 0x6c40L;

    //LAB_800f4220
    do {
      v0 = MEMORY.ref(2, v1).offset(0x0L).getSigned();

      if(v0 != a2) {
        MEMORY.ref(2, v1).offset(0x8L).setu(a0);
        MEMORY.ref(2, a1).offset(0x0L).setu(a0);
      }

      //LAB_800f4238
      a1 = a1 + 0x144L;
      a0 = a0 + 0x5eL;
      s0 = s0 + 0x1L;
      v1 = v1 + 0x3cL;
    } while((int)s0 < 0x3L);
  }

  @Method(0x800f43dcL)
  public static long FUN_800f43dc(final RunningScript a3) {
    final long a1 = Math.min(_800c677c.get(), 3);

    //LAB_800f43f8
    long a0 = _8006e398.getAddress();

    //LAB_800f4410
    long a2;
    for(a2 = 0; a2 < a1; a2++) {
      if(MEMORY.ref(4, a0).offset(0xe40L).get() == a3.params_20.get(0).deref().get()) {
        break;
      }

      a0 = a0 + 0x4L;
    }

    //LAB_800f4430
    final BtldScriptData27c struct = scriptStatePtrArr_800bc1c0.get((int)a3.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
    struct._0a.add((short)a3.params_20.get(1).deref().get());
    _800bc950.offset(a2 * 0x4L).addu(a3.params_20.get(1).deref().get());

    if(struct._0a.get() < struct._04.get(1).get() * 100) {
      //LAB_800f44d4
      if(struct._0a.get() < 500) {
        struct._0a.set((short)500);
      }
    } else {
      struct._0a.set((short)(struct._04.get(1).get() * 100));
      if(struct._04.get(1).get() * 100 >= 500) {
        struct._0a.set((short)500);
      }
    }

    //LAB_800f44ec
    if(struct._0a.get() < 0) {
      struct._0a.set((short)0);
    }

    //LAB_800f4500
    a3.params_20.get(2).deref().set(struct._0a.get());
    return 0;
  }

  @Method(0x800f480cL)
  public static long FUN_800f480c(final RunningScript s0) {
    long v0;
    long v1;
    long a0;
    BtldScriptData27c a1 = null;
    long t0;
    long t5;
    v0 = 0x800c_0000L;
    t5 = v0 + 0x7148L;
    final long[] sp0x10 = new long[8];
    for(int i = 0; i < sp0x10.length; i++) {
      sp0x10[i] = MEMORY.ref(4, t5).offset(i * 0x4L).get();
    }

    t0 = s0.params_20.get(0).deref().get();

    //LAB_800f489c
    for(a0 = 0; a0 < _800c677c.get(); a0++) {
      a1 = scriptStatePtrArr_800bc1c0.get((int)_8006e398.offset(4, 0xe40L).offset(a0 * 0x4L).get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);

      if(_800c6c34.deref(2).offset(0x4L).getSigned() == a1._272.get()) {
        break;
      }
    }

    //LAB_800f48d8
    if((a1._14.get() & 0x8L) != 0) {
      t0 = 0x3L;
    }

    //LAB_800f48f4
    v1 = FUN_800f7768(sp0x10[(int)t0 * 2], sp0x10[(int)t0 * 2 + 1]);
    if(v1 == 0) {
      return 0x2L;
    }

    if(v1 == 0x1L) {
      //LAB_800f4930
      v1 = _800c6c34.deref(4).offset(0x48L).get();
    } else {
      //LAB_800f4944
      //LAB_800f4948
      v1 = -0x1L;
    }

    //LAB_800f4950
    s0.params_20.get(1).deref().set(v1);

    //LAB_800f4954
    return 0;
  }

  @Method(0x800f4964L)
  public static void FUN_800f4964() {
    final long v0 = _800c6b60.get();
    MEMORY.ref(2, v0).offset(0x00L).setu(0);
    MEMORY.ref(2, v0).offset(0x02L).setu(0);
    MEMORY.ref(2, v0).offset(0x04L).setu(0);
    MEMORY.ref(2, v0).offset(0x06L).setu(0);
    MEMORY.ref(2, v0).offset(0x08L).setu(0);
    MEMORY.ref(2, v0).offset(0x0aL).setu(0);
    MEMORY.ref(2, v0).offset(0x0cL).setu(0);
    MEMORY.ref(2, v0).offset(0x0eL).setu(0);
    MEMORY.ref(2, v0).offset(0x10L).setu(0);
    MEMORY.ref(2, v0).offset(0x12L).setu(0);
    MEMORY.ref(2, v0).offset(0x14L).setu(0);
    MEMORY.ref(2, v0).offset(0x16L).setu(0x1000L);
    MEMORY.ref(2, v0).offset(0x18L).setu(0);
    MEMORY.ref(2, v0).offset(0x1aL).setu(0);
    MEMORY.ref(2, v0).offset(0x1cL).setu(-0x1L);
    MEMORY.ref(2, v0).offset(0x22L).setu(0);
    MEMORY.ref(2, v0).offset(0x24L).setu(0);
  }

  @Method(0x800f4b80L)
  public static void FUN_800f4b80() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long t0;
    long t1;
    long t2;
    long s0;
    long s1;
    long s2;
    long sp18;
    long sp14;
    long sp10;
    long sp1c;
    v0 = 0x800c_0000L;
    v1 = MEMORY.ref(4, v0).offset(0x6b60L).get();
    v0 = MEMORY.ref(2, v1).offset(0x0L).getSigned();
    a0 = MEMORY.ref(2, v1).offset(0x0L).get();
    if(v0 == 0) {
      return;
    }

    v0 = MEMORY.ref(2, v1).offset(0xaL).getSigned();

    if(v0 != 0) {
      s0 = 0x80L;
    } else {
      s0 = 0xbaL;
    }

    //LAB_800f4bc0
    v0 = a0 - 0x1L;
    switch((short)v0) {
      case 0 -> {
        v0 = 0x800c_0000L;
        a3 = MEMORY.ref(4, v0).offset(0x6b60L).get();
        v0 = MEMORY.ref(2, a3).offset(0xaL).getSigned();
        MEMORY.ref(4, a3).offset(0x90L).setu(0);
        MEMORY.ref(4, a3).offset(0xa0L).setu(0);
        MEMORY.ref(2, a3).offset(0x12L).setu(0);
        MEMORY.ref(2, a3).offset(0x10L).setu(0);
        if(v0 == 0) {
          v1 = MEMORY.ref(2, a3).offset(0x26L).get();
          a0 = MEMORY.ref(2, a3).offset(0x28L).get();
          a1 = MEMORY.ref(2, a3).offset(0x2aL).get();
          a2 = MEMORY.ref(4, a3).offset(0x2cL).get();
          v0 = MEMORY.ref(2, a3).offset(0x2L).get();
          MEMORY.ref(2, a3).offset(0x24L).setu(v1);
          v1 = MEMORY.ref(2, a3).offset(0x22L).getSigned();
          v0 = v0 | 0x20L;
          MEMORY.ref(2, a3).offset(0x2L).setu(v0);
          v0 = MEMORY.ref(2, a3).offset(0x24L).getSigned();
          MEMORY.ref(2, a3).offset(0x1eL).setu(a0);
          a0 = MEMORY.ref(2, a3).offset(0x1eL).getSigned();
          MEMORY.ref(2, a3).offset(0x20L).setu(a1);
          MEMORY.ref(4, a3).offset(0x94L).setu(a2);
          v1 = v1 - 0x1L;
          v0 = v0 + a0;
          if((int)v1 < (int)v0) {
            v0 = MEMORY.ref(2, a3).offset(0x24L).get() - 0x1L;
            MEMORY.ref(2, a3).offset(0x24L).setu(v0);
            v0 = v0 << 16;
            if((int)v0 < 0) {
              v1 = MEMORY.ref(2, a3).offset(0x1aL).get();
              v0 = MEMORY.ref(2, a3).offset(0x1aL).getSigned();
              MEMORY.ref(2, a3).offset(0x24L).setu(0);
              MEMORY.ref(2, a3).offset(0x1eL).setu(0);
              MEMORY.ref(2, a3).offset(0x20L).setu(v1);
              v1 = v1 << 16;
              v1 = (int)v1 >> 16;
              v0 = v0 - v1;
              MEMORY.ref(4, a3).offset(0x94L).setu(v0);
            }
          }
        } else {
          //LAB_800f4ca0
          v0 = MEMORY.ref(2, a3).offset(0x30L).get();
          MEMORY.ref(2, a3).offset(0x1eL).setu(0);
          MEMORY.ref(2, a3).offset(0x20L).setu(0);
          MEMORY.ref(4, a3).offset(0x94L).setu(0);
          MEMORY.ref(2, a3).offset(0x24L).setu(v0);
        }

        //LAB_800f4cb4
        v0 = FUN_800f56c4();
        v1 = 0x800c_0000L;
        a0 = MEMORY.ref(4, v1).offset(0x6b60L).get();
        MEMORY.ref(2, a0).offset(0x1cL).setu(v0);
        v0 = MEMORY.ref(2, a0).offset(0x2L).get();
        v1 = 0x7L;
        MEMORY.ref(2, a0).offset(0x0L).setu(v1);
        v0 = v0 | 0x40L;
        MEMORY.ref(2, a0).offset(0x2L).setu(v0);
      }

      case 1 -> {
        s0 = 0x800c_0000L;
        v1 = MEMORY.ref(4, s0).offset(0x6b60L).get();
        v0 = MEMORY.ref(2, v1).offset(0x2L).get();
        v0 = v0 & 0xfcffL;
        MEMORY.ref(2, v1).offset(0x2L).setu(v0);
        v0 = FUN_800f56c4();
        v1 = 0x8008_0000L;
        t2 = MEMORY.ref(4, s0).offset(0x6b60L).get();
        a0 = MEMORY.ref(4, v1).offset(-0x5c68L).get();
        MEMORY.ref(2, t2).offset(0x1cL).setu(v0);
        if((a0 & 0x4L) != 0) {
          if(MEMORY.ref(2, t2).offset(0x24L).getSigned() != 0) {
            MEMORY.ref(4, t2).offset(0x88L).setu(0x2L);
            MEMORY.ref(2, t2).offset(0x24L).setu(0);
            MEMORY.ref(2, t2).offset(0x0L).setu(0x5L);
            playSound(0, 1, 0, 0, (short)0, (short)0);
          }

          break;
        }

        //LAB_800f4d54
        if((a0 & 0x1L) != 0) {
          s0 = MEMORY.ref(2, t2).offset(0x24L).getSigned();
          v0 = MEMORY.ref(2, t2).offset(0x1eL).getSigned();
          v1 = MEMORY.ref(2, t2).offset(0x22L).getSigned();
          a0 = MEMORY.ref(2, t2).offset(0x22L).get();
          v0 = v0 + 0x6L;
          v1 = v1 - 0x1L;
          if((int)v1 >= (int)v0) {
            v0 = 0x6L;
          } else {
            v0 = MEMORY.ref(2, t2).offset(0x1eL).get();
            v0 = v0 + 0x1L;

            //LAB_800f4d8c
            v0 = a0 - v0;
          }

          //LAB_800f4d90
          MEMORY.ref(2, t2).offset(0x24L).setu(v0);
          v0 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v0).offset(0x6b60L).get();

          a0 = MEMORY.ref(2, v1).offset(0x24L).getSigned();
          v0 = 0x2L;
          MEMORY.ref(4, v1).offset(0x88L).setu(v0);
          v0 = 0x5L;
          MEMORY.ref(2, v1).offset(0x0L).setu(v0);
          if(s0 != a0) {
            playSound(0, 1, 0, 0, (short)0, (short)0);
          }

          break;
        }
        v0 = a0 & 0x8L;

        //LAB_800f4dc4
        if(v0 != 0) {
          v0 = MEMORY.ref(2, t2).offset(0x1eL).getSigned();
          v1 = MEMORY.ref(2, t2).offset(0x1eL).get();
          if(v0 == 0) {
            break;
          }
          v1 = v1 - 0x7L;
          if((int)v0 < 0x7L) {
            v0 = MEMORY.ref(2, t2).offset(0x1aL).get();
            MEMORY.ref(2, t2).offset(0x24L).setu(0);
            MEMORY.ref(2, t2).offset(0x1eL).setu(0);
          } else {
            //LAB_800f4df4
            v0 = MEMORY.ref(2, t2).offset(0x20L).get();
            MEMORY.ref(2, t2).offset(0x1eL).setu(v1);
            v0 = v0 + 0x62L;
          }

          //LAB_800f4e00
          MEMORY.ref(2, t2).offset(0x20L).setu(v0);
          v0 = 0x800c_0000L;
          t0 = MEMORY.ref(4, v0).offset(0x6b60L).get();
          v1 = MEMORY.ref(2, t0).offset(0x1aL).getSigned();
          t1 = MEMORY.ref(2, t0).offset(0x20L).getSigned();
          v0 = 0x2L;
          MEMORY.ref(4, t0).offset(0x88L).setu(v0);
          v0 = 0x5L;
          MEMORY.ref(2, t0).offset(0x0L).setu(v0);
          v1 = v1 - t1;
          MEMORY.ref(4, t0).offset(0x94L).setu(v1);
          playSound(0, 1, 0, 0, (short)0, (short)0);
          break;
        }
        v0 = a0 & 0x2L;

        //LAB_800f4e40
        if(v0 != 0) {
          v1 = MEMORY.ref(2, t2).offset(0x1eL).getSigned();
          v0 = MEMORY.ref(2, t2).offset(0x22L).getSigned();
          a0 = MEMORY.ref(2, t2).offset(0x1eL).get();
          v1 = v1 + 0x6L;
          v0 = v0 - 0x1L;
          if((int)v1 >= (int)v0) {
            break;
          }
          v1 = a0 + 0x7L;
          v0 = MEMORY.ref(2, t2).offset(0x20L).get();
          MEMORY.ref(2, t2).offset(0x1eL).setu(v1);
          v1 = MEMORY.ref(2, t2).offset(0x1eL).getSigned();
          v0 = v0 - 0x62L;
          MEMORY.ref(2, t2).offset(0x20L).setu(v0);
          v0 = MEMORY.ref(2, t2).offset(0x22L).getSigned();
          v1 = v1 + 0x6L;
          v0 = v0 - 0x1L;
          if((int)v1 >= (int)v0) {
            MEMORY.ref(2, t2).offset(0x24L).setu(0);
          }

          //LAB_800f4e98
          t0 = MEMORY.ref(4, s0).offset(0x6b60L).get();
          v1 = MEMORY.ref(2, t0).offset(0x1aL).getSigned();
          t1 = MEMORY.ref(2, t0).offset(0x20L).getSigned();
          v0 = 0x2L;
          MEMORY.ref(4, t0).offset(0x88L).setu(v0);
          v0 = 0x5L;
          MEMORY.ref(2, t0).offset(0x0L).setu(v0);
          v1 = v1 - t1;
          MEMORY.ref(4, t0).offset(0x94L).setu(v1);
          playSound(0, 1, 0, 0, (short)0, (short)0);
          break;
        }
        v0 = 0x8008_0000L;

        //LAB_800f4ecc
        v1 = MEMORY.ref(4, v0).offset(-0x5c64L).get();
        v0 = v1 & 0x1000L;
        if(v0 != 0) {
          v0 = MEMORY.ref(2, t2).offset(0x24L).getSigned();
          v1 = MEMORY.ref(2, t2).offset(0x24L).get();
          if(v0 != 0) {
            v0 = v1 - 0x1L;
            MEMORY.ref(2, t2).offset(0x24L).setu(v0);
            v0 = 0x2L;
            MEMORY.ref(4, t2).offset(0x88L).setu(v0);
            v0 = 0x5L;
            MEMORY.ref(2, t2).offset(0x0L).setu(v0);
          } else {
            //LAB_800f4f18
            v0 = MEMORY.ref(2, t2).offset(0x1eL).getSigned();

            if(v0 == 0) {
              break;
            }
            t1 = MEMORY.ref(2, t2).offset(0x20L).getSigned();
            v1 = MEMORY.ref(2, t2).offset(0x20L).get();
            v0 = 0x5L;
            MEMORY.ref(4, t2).offset(0x80L).setu(v0);
            t0 = MEMORY.ref(2, t2).offset(0x80L).get();
            v0 = 0x3L;
            MEMORY.ref(2, t2).offset(0x0L).setu(v0);
            v0 = MEMORY.ref(2, t2).offset(0x1eL).get();
            v1 = v1 + t0;
            MEMORY.ref(2, t2).offset(0x20L).setu(v1);
            v1 = MEMORY.ref(2, t2).offset(0x2L).get();
            v0 = v0 - 0x1L;
            MEMORY.ref(4, t2).offset(0x7cL).setu(t1);
            MEMORY.ref(2, t2).offset(0x1eL).setu(v0);
            v1 = v1 | 0x200L;
            MEMORY.ref(2, t2).offset(0x2L).setu(v1);
          }
          playSound(0, 1, 0, 0, (short)0, (short)0);
          break;
        }
        v0 = v1 & 0x4000L;

        //LAB_800f4f74
        if(v0 != 0) {
          a0 = MEMORY.ref(2, t2).offset(0x22L).getSigned();
          v1 = MEMORY.ref(2, t2).offset(0x24L).getSigned();
          v0 = a0 - 0x1L;
          if(v1 != v0) {
            v0 = MEMORY.ref(2, t2).offset(0x1eL).getSigned();

            v0 = v0 + 0x1L;
            v0 = v0 + v1;
            if((int)v0 < (int)a0) {
              playSound(0, 1, 0, 0, (short)0, (short)0);
              v0 = 0x7L;
              a2 = MEMORY.ref(4, s0).offset(0x6b60L).get();
              v0 = v0 - 0x1L;
              v1 = MEMORY.ref(2, a2).offset(0x24L).getSigned();
              a0 = MEMORY.ref(2, a2).offset(0x24L).get();
              if(v1 != v0) {
                v0 = a0 + 0x1L;
                MEMORY.ref(2, a2).offset(0x24L).setu(v0);
                v0 = 0x2L;
                MEMORY.ref(4, a2).offset(0x88L).setu(v0);
                v0 = 0x5L;
                MEMORY.ref(2, a2).offset(0x0L).setu(v0);
              } else {
                //LAB_800f4ff8
                a1 = MEMORY.ref(2, a2).offset(0x20L).getSigned();
                v1 = MEMORY.ref(2, a2).offset(0x20L).get();
                v0 = -0x5L;
                MEMORY.ref(4, a2).offset(0x80L).setu(v0);
                a0 = MEMORY.ref(2, a2).offset(0x80L).get();
                v0 = 0x4L;
                MEMORY.ref(2, a2).offset(0x0L).setu(v0);
                v0 = MEMORY.ref(2, a2).offset(0x1eL).get();
                v1 = v1 + a0;
                MEMORY.ref(2, a2).offset(0x20L).setu(v1);
                v1 = MEMORY.ref(2, a2).offset(0x2L).get();
                v0 = v0 + 0x1L;
                MEMORY.ref(4, a2).offset(0x7cL).setu(a1);
                MEMORY.ref(2, a2).offset(0x1eL).setu(v0);
                v1 = v1 | 0x100L;
                MEMORY.ref(2, a2).offset(0x2L).setu(v1);
              }
            }
          }

          break;
        }
        v0 = a0 & 0x20L;

        //LAB_800f5044
        MEMORY.ref(4, t2).offset(0x90L).setu(0);
        if(v0 != 0) {
          v1 = _800c677c.get();

          s0 = 0;
          v0 = 0x800c_0000L;
          a2 = v0 - 0x3e40L;
          a1 = MEMORY.ref(2, t2).offset(0x8L).getSigned();
          a0 = v1;
          v0 = 0x8007_0000L;
          v1 = v0 - 0x1c68L;

          //LAB_800f5078
          do {
            s2 = MEMORY.ref(4, v1).offset(0xe40L).get();
            s1 = MEMORY.ref(4, a2).offset(s2 * 0x4L).deref(4).get();

            if(a1 == MEMORY.ref(2, s1).offset(0x272L).getSigned()) {
              //LAB_800f503c
              _800c6980.setu(s0);
              break;
            }
            s0 = s0 + 0x1L;
            v1 = v1 + 0x4L;
          } while((int)s0 < (int)a0);

          //LAB_800f50b8
          v0 = 0x800c_0000L;
          v1 = MEMORY.ref(4, v0).offset(0x6b60L).get();

          v0 = MEMORY.ref(2, v1).offset(0xaL).getSigned();

          if(v0 == 0) {
            v0 = MEMORY.ref(2, v1).offset(0x1cL).get();
            MEMORY.ref(2, s1).offset(0x52L).setu(v0);
            FUN_800f7a74(s2);
            v0 = MEMORY.ref(2, s1).offset(0xd4L).get();

            v0 = v0 & 0x4L;
            v1 = 0x800c_0000L;
            if(v0 != 0) {
              v0 = 0x1L;
              MEMORY.ref(4, v1).offset(0x6b68L).setu(v0);
            } else {
              //LAB_800f5100
              v0 = 0x800c_0000L;
              MEMORY.ref(4, v0).offset(0x6b68L).setu(0);
            }

            //LAB_800f5108
            v0 = MEMORY.ref(2, s1).offset(0xd4L).get();
            v0 = v0 & 0x2L;
            v1 = 0x800c_0000L;
            if(v0 != 0) {
              v0 = 0x1L;
              MEMORY.ref(4, v1).offset(0x69c8L).setu(v0);
            } else {
              //LAB_800f5128
              v0 = 0x800c_0000L;
              MEMORY.ref(4, v0).offset(0x69c8L).setu(0);
            }
          } else {
            //LAB_800f5134
            a0 = MEMORY.ref(2, v1).offset(0x1cL).getSigned();

            v0 = FUN_800f9e50(a0);
            s1 = v0;
            v0 = MEMORY.ref(2, s1).offset(0xcL).getSigned();
            v1 = MEMORY.ref(2, s1).offset(0xa0L).getSigned();

            if((int)v0 < (int)v1) {
              //LAB_800f5160
              //LAB_800f5168
              playSound(0, 3, 0, 0, (short)0, (short)0);
              break;
            }

            //LAB_800f517c
            v0 = 0x800c_0000L;
            v0 = MEMORY.ref(4, v0).offset(0x6b5cL).get();

            MEMORY.ref(2, v0).offset(0x0L).setu(0);
            MEMORY.ref(2, v0).offset(0x2L).setu(0);
          }

          //LAB_800f5190
          playSound(0, 2, 0, 0, (short)0, (short)0);
          a1 = 0x800c_0000L;
          a0 = MEMORY.ref(4, a1).offset(0x6b60L).get();
          v0 = MEMORY.ref(2, a0).offset(0x2L).get();
          v1 = MEMORY.ref(2, a0).offset(0xaL).getSigned();
          MEMORY.ref(4, a0).offset(0x8cL).setu(0);
          v0 = v0 | 0x4L;
          v0 = v0 & 0xfff7L;
          MEMORY.ref(2, a0).offset(0x2L).setu(v0);
          if(v1 == 0) {
            v0 = MEMORY.ref(2, a0).offset(0x1aL).getSigned();
            v1 = MEMORY.ref(2, a0).offset(0x20L).getSigned();
            v0 = v0 - v1;
            MEMORY.ref(4, a0).offset(0x94L).setu(v0);
          }

          //LAB_800f51e8
          v0 = MEMORY.ref(4, a1).offset(0x6b60L).get();
          v1 = MEMORY.ref(2, v0).offset(0x2L).get();
          a0 = 0x6L;
          MEMORY.ref(2, v0).offset(0x0L).setu(a0);
          v1 = v1 & 0xfffdL;
          MEMORY.ref(2, v0).offset(0x2L).setu(v1);
          break;
        }

        //LAB_800f5208
        v0 = a0 & 0x40L;
        if(v0 != 0) {
          playSound(0, 3, 0, 0, (short)0, (short)0);
          v0 = MEMORY.ref(4, s0).offset(0x6b60L).get();
          v1 = MEMORY.ref(2, v0).offset(0x2L).get();
          a0 = 0x8L;
          MEMORY.ref(2, v0).offset(0x0L).setu(a0);
          v1 = v1 & 0xfff7L;
          MEMORY.ref(2, v0).offset(0x2L).setu(v1);
        }
      }

      case 2 -> {
        v0 = 0x800c_0000L;
        a0 = MEMORY.ref(4, v0).offset(0x6b60L).get();
        v0 = MEMORY.ref(4, a0).offset(0x90L).get();
        s0 = MEMORY.ref(4, a0).offset(0x80L).get();
        v0 = v0 + 0x1L;
        MEMORY.ref(4, a0).offset(0x90L).setu(v0);
        if((int)v0 >= 0x3L) {
          s0 = s0 << 1;
        }

        //LAB_800f5278
        v0 = MEMORY.ref(2, a0).offset(0x20L).get();
        v1 = MEMORY.ref(2, a0).offset(0x7cL).get();
        v0 = v0 + s0;
        a1 = v1 + 0xeL;
        MEMORY.ref(2, a0).offset(0x20L).setu(v0);
        v0 = v0 << 16;
        v1 = a1 << 16;
        if((int)v0 >= (int)v1) {
          v0 = 0x2L;
          MEMORY.ref(2, a0).offset(0x20L).setu(a1);
          MEMORY.ref(2, a0).offset(0x0L).setu(v0);
        }
      }

      case 3 -> {
        v0 = 0x800c_0000L;
        a0 = MEMORY.ref(4, v0).offset(0x6b60L).get();
        v0 = MEMORY.ref(4, a0).offset(0x90L).get();
        s0 = MEMORY.ref(4, a0).offset(0x80L).get();
        v0 = v0 + 0x1L;
        MEMORY.ref(4, a0).offset(0x90L).setu(v0);
        if((int)v0 >= 0x3L) {
          s0 = s0 << 1;
        }

        //LAB_800f52d4
        v0 = MEMORY.ref(2, a0).offset(0x20L).get();
        v1 = MEMORY.ref(2, a0).offset(0x7cL).get();
        v0 = v0 + s0;
        a1 = v1 - 0xeL;
        MEMORY.ref(2, a0).offset(0x20L).setu(v0);
        v0 = v0 << 16;
        v1 = a1 << 16;
        if((int)v1 >= (int)v0) {
          v0 = 0x2L;

          //LAB_800f5300
          MEMORY.ref(2, a0).offset(0x20L).setu(a1);
          MEMORY.ref(2, a0).offset(0x0L).setu(v0);
        }
      }

      case 4 -> {
        v0 = 0x800c_0000L;
        v1 = MEMORY.ref(4, v0).offset(0x6b60L).get();
        v0 = MEMORY.ref(4, v1).offset(0x90L).get();
        s0 = MEMORY.ref(4, v1).offset(0x88L).get();
        v0 = v0 + 0x1L;
        MEMORY.ref(4, v1).offset(0x90L).setu(v0);
        if((int)v0 >= 0x3L) {
          s0 = (int)s0 >> 1;
        }

        //LAB_800f5338
        s0 = s0 - 0x1L;
        if((int)s0 <= 0) {
          v0 = 0x2L;
          MEMORY.ref(2, v1).offset(0x0L).setu(v0);
        }
      }

      case 5 -> {
        s0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, s0).offset(0x6b60L).get();
        MEMORY.ref(4, v0).offset(0xa0L).setu(0);
        v0 = FUN_800f56c4();
        v1 = 0x800c_0000L;
        a0 = MEMORY.ref(4, s0).offset(0x6b60L).get();
        v1 = MEMORY.ref(4, v1).offset(0x677cL).get();
        s0 = 0;
        MEMORY.ref(2, a0).offset(0x1cL).setu(v0);
        v0 = 0x800c_0000L;
        a2 = v0 - 0x3e40L;
        a1 = MEMORY.ref(2, a0).offset(0x8L).getSigned();
        a0 = v1;
        v0 = 0x8007_0000L;
        v1 = v0 - 0x1c68L;

        //LAB_800f538c
        do {
          s2 = MEMORY.ref(4, v1).offset(0xe40L).get();

          v0 = s2 << 2;
          v0 = v0 + a2;
          v0 = MEMORY.ref(4, v0).offset(0x0L).get();

          s1 = MEMORY.ref(4, v0).offset(0x0L).get();

          v0 = MEMORY.ref(2, s1).offset(0x272L).getSigned();

          if(a1 == v0) {
            break;
          }
          s0 = s0 + 0x1L;
          v1 = v1 + 0x4L;
        } while((int)s0 < (int)a0);

        //LAB_800f53c8
        v0 = 0x800c_0000L;
        v0 = MEMORY.ref(4, v0).offset(0x6b60L).get();
        v0 = MEMORY.ref(2, v0).offset(0xaL).getSigned();
        if(v0 == 0) {
          v0 = 0x800c_0000L;
          a0 = MEMORY.ref(4, v0).offset(0x6b68L).get();
          v0 = 0x800c_0000L;
          a1 = MEMORY.ref(4, v0).offset(0x69c8L).get();
        } else {
          //LAB_800f53f8
          a1 = MEMORY.ref(2, s1).offset(0x94L).get();

          a0 = a1 & 0x40L;
          a0 = 0 < a0 ? 1 : 0;
          a1 = a1 & 0x8L;
          a1 = 0 < a1 ? 1 : 0;
        }

        //LAB_800f5410
        v0 = FUN_800f7768(a0, a1);
        s0 = v0;
        if(s0 != 0) {
          v0 = 0x1L;
          if(s0 == v0) {
            s2 = 0x800c_0000L;
            v1 = MEMORY.ref(4, s2).offset(0x6b60L).get();
            v0 = MEMORY.ref(2, v1).offset(0xaL).getSigned();

            if(v0 == 0) {
              a0 = MEMORY.ref(1, v1).offset(0x1cL).get();
              a0 = a0 - 0x40L;
              a0 = a0 & 0xffL;
              FUN_800232dc(a0);
            }

            //LAB_800f545c
            v0 = MEMORY.ref(4, s2).offset(0x6b60L).get();
            v0 = MEMORY.ref(2, v0).offset(0xaL).getSigned();

            if(v0 == s0) {
              v0 = MEMORY.ref(2, s1).offset(0xcL).get();
              v1 = MEMORY.ref(2, s1).offset(0xa0L).get();

              v0 = v0 - v1;
              MEMORY.ref(2, s1).offset(0xcL).setu(v0);
            }

            //LAB_800f5488
            playSound(0, 2, 0, 0, (short)0, (short)0);
            v1 = MEMORY.ref(4, s2).offset(0x6b60L).get();
            v0 = 0x9L;
            MEMORY.ref(4, v1).offset(0xa0L).setu(s0);
            MEMORY.ref(2, v1).offset(0x0L).setu(v0);
          } else {
            //LAB_800f54b4
            playSound(0, 0, 3, 0, (short)0, (short)0);
            v0 = 0x800c_0000L;
            a0 = MEMORY.ref(4, v0).offset(0x6b60L).get();
            v0 = MEMORY.ref(2, a0).offset(0x2L).get();
            v1 = 0x7L;
            MEMORY.ref(2, a0).offset(0x0L).setu(v1);
            v0 = v0 & 0xfffbL;
            v0 = v0 | 0x20L;
            MEMORY.ref(2, a0).offset(0x2L).setu(v0);
          }
        }
      }

      case 6 -> {
        s1 = 0x800c_0000L;
        v1 = MEMORY.ref(4, s1).offset(0x6b60L).get();
        v0 = 0x2L;
        MEMORY.ref(2, v1).offset(0x0L).setu(v0);
        playSound(0, 4, 0, 0, (short)0, (short)0);
        a1 = MEMORY.ref(4, s1).offset(0x6b60L).get();
        v0 = 0x52L;
        MEMORY.ref(2, a1).offset(0x12L).setu(v0);
        v0 = MEMORY.ref(2, a1).offset(0x4L).get();
        a0 = MEMORY.ref(2, a1).offset(0x6L).get();
        v1 = s0 >>> 1;
        MEMORY.ref(2, a1).offset(0x10L).setu(s0);
        v0 = v0 - v1;
        v1 = MEMORY.ref(2, a1).offset(0x12L).get();
        v0 = v0 + 0x9L;
        MEMORY.ref(2, a1).offset(0x18L).setu(v0);
        v0 = -0x10L;
        a0 = a0 - v1;
        v1 = MEMORY.ref(2, a1).offset(0x2L).get();
        v0 = v0 + a0;
        MEMORY.ref(2, a1).offset(0x1aL).setu(v0);
        MEMORY.ref(2, a1).offset(0x20L).setu(v0);
        v1 = v1 | 0xbL;
        MEMORY.ref(2, a1).offset(0x2L).setu(v1);
        v1 = v1 & 0x20L;
        if(v1 != 0) {
          v1 = MEMORY.ref(2, a1).offset(0x94L).get();
          v0 = v0 - v1;
          MEMORY.ref(2, a1).offset(0x20L).setu(v0);
        }

        //LAB_800f5588
        v0 = MEMORY.ref(4, s1).offset(0x6b60L).get();
        v0 = MEMORY.ref(2, v0).offset(0xaL).getSigned();
        if(v0 != 0) {
          v0 = FUN_800f56c4();
          a0 = v0 << 16;
          v1 = MEMORY.ref(4, s1).offset(0x6b60L).get();
          a0 = (int)a0 >> 16;
          MEMORY.ref(2, v1).offset(0x1cL).setu(v0);
          v0 = FUN_800f9e50(a0);
          a0 = 0;
          a1 = 0x1L;
          a2 = a0;
          a3 = MEMORY.ref(2, v0).offset(0xa0L).getSigned();
          v0 = 0x118L;
          sp10 = v0;
          v0 = 0x87L;
          sp14 = v0;
          v0 = a1;
          sp18 = 0;
          sp1c = v0;
          FUN_800f3354(a0, a1, a2, a3, sp10, sp14, sp18, sp1c);
        }
      }

      case 7 -> {
        _800c69c8.setu(0);
        v1 = _800c6b60.get();
        a0 = _800c6b5c.get();
        _800c6b68.setu(0);
        MEMORY.ref(4, v1).offset(0xa0L).setu(-0x1L);
        MEMORY.ref(2, v1).offset(0x0L).setu(0x9L);
        MEMORY.ref(2, v1).offset(0x12L).setu(0);
        MEMORY.ref(2, v1).offset(0x10L).setu(0);
        MEMORY.ref(2, v1).offset(0x2L).and(0xfffcL);
        MEMORY.ref(2, a0).offset(0x0L).setu(0);
        MEMORY.ref(2, a0).offset(0x2L).setu(0);
      }

      case 8 -> {
        a3 = _800c6b60.get();
        if(MEMORY.ref(2, a3).offset(0xaL).getSigned() == 0) {
          v0 = MEMORY.ref(2, a3).offset(0x1aL).getSigned() - MEMORY.ref(2, a3).offset(0x20L).getSigned();
          MEMORY.ref(2, a3).offset(0x26L).setu(MEMORY.ref(2, a3).offset(0x24L).get());
          MEMORY.ref(2, a3).offset(0x28L).setu(MEMORY.ref(2, a3).offset(0x1eL).get());
          MEMORY.ref(2, a3).offset(0x2aL).setu(MEMORY.ref(2, a3).offset(0x20L).get());
          MEMORY.ref(4, a3).offset(0x94L).setu(v0);
          MEMORY.ref(4, a3).offset(0x2cL).setu(v0);
        }

        //LAB_800f568c
        FUN_800f4964();
      }
    }

    //LAB_800f5694
    //LAB_800f5698
    _800c6b60.deref(4).offset(0x84L).setu(_800bb0fc.get() & 0x7L);

    //LAB_800f56ac
  }

  @Method(0x800f56c4L)
  public static long FUN_800f56c4() {
    assert false;
    return 0;
  }

  @Method(0x800f57f8L)
  public static void FUN_800f57f8(long a0) {
    assert false;
  }

  @Method(0x800f5c94L)
  public static void FUN_800f5c94() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long t0;
    long s0;
    long s1;
    a0 = _800c6b60.get();

    if(MEMORY.ref(2, a0).offset(0x0L).getSigned() != 0) {
      v1 = MEMORY.ref(2, a0).offset(0x2L).get();

      if((v1 & 0x1L) != 0) {
        if((v1 & 0x2L) != 0) {
          FUN_800f57f8(MEMORY.ref(2, a0).offset(0xaL).getSigned());

          a1 = _800c6b60.get();
          if((MEMORY.ref(2, a1).offset(0x2L).get() & 0x8L) != 0) {
            //LAB_800f5d78
            //LAB_800f5d90
            t0 = MEMORY.ref(4, a1).offset(0x84L).get();
            s1 = t0 % 4;
            t0 = t0 / 0x4L;
            FUN_800f8cd8(MEMORY.ref(2, a1).offset(0x18L).get() - centreScreenX_1f8003dc.get() - 0x10L, MEMORY.ref(2, a1).offset(0x1aL).get() - centreScreenY_1f8003de.get() + MEMORY.ref(2, a1).offset(0x24L).getSigned() * 0xeL + 0x2L, (s1 * 0x10L + 0xc0L) & 0xf0L, (t0 * 0x8L + 0x20L) & 0xf8L, 0xfL, 0x8L, 0xdL, 0x1L);

            v1 = _800c6b60.get();
            if(MEMORY.ref(2, v1).offset(0xaL).getSigned() != 0) {
              s0 = 0;
            } else {
              s0 = 0x1aL;
            }

            //LAB_800f5e00
            a0 = MEMORY.ref(2, v1).offset(0x2L).get();

            if((a0 & 0x100L) != 0) {
              s1 = 0x2L;
            } else {
              s1 = 0;
            }

            //LAB_800f5e18
            if((a0 & 0x200L) != 0) {
              t0 = -0x2L;
            } else {
              t0 = 0;
            }

            //LAB_800f5e24
            if(MEMORY.ref(2, v1).offset(0x1eL).getSigned() > 0) {
              FUN_800f74f4(_800c7190.getAddress(), MEMORY.ref(2, v1).offset(0x4L).get() + s0 + 0x38L, MEMORY.ref(2, v1).offset(0x6L).get() + t0 - 0x64L, _800c7192.get(), _800c7193.get(), 0xdL, -0x1L, 0);
            }

            //LAB_800f5e7c
            a0 = _800c6b60.get();
            if(MEMORY.ref(2, a0).offset(0x1eL).getSigned() + 0x6L < MEMORY.ref(2, a0).offset(0x22L).getSigned() - 0x1L) {
              FUN_800f74f4(_800c7190.getAddress(), MEMORY.ref(2, a0).offset(0x4L).get() + s0 + 0x38L, MEMORY.ref(2, a0).offset(0x6L).get() + s1 - 0x7L, _800c7192.get(), _800c7193.get(), 0xdL, -0x1L, 0x1L);
            }
          }

          //LAB_800f5ee8
          v1 = _800c6b60.get();
          a2 = MEMORY.ref(2, v1).offset(0x10L).get() + 0x6L;
          v0 = (int)a2 >> 1;
          a3 = MEMORY.ref(2, v1).offset(0x12L).get() + 0x11L;
          FUN_800f1268(MEMORY.ref(2, v1).offset(0x4L).get() - v0, MEMORY.ref(2, v1).offset(0x6L).get() - a3, a2, a3, 0x8L);
        }

        //LAB_800f5f50
        v1 = _800c6b60.get();
        a0 = MEMORY.ref(2, v1).offset(0x2L).get();

        if((a0 & 0x40L) != 0) {
          s0 = MEMORY.ref(2, v1).offset(0xaL).getSigned();

          if(s0 == 0) {
            //LAB_800f5f8c
            s1 = 0x4L;
          } else if(s0 == 0x1L) {
            //LAB_800f5f94
            s1 = 0x5L;
            if((a0 & 0x2L) != 0) {
              v0 = FUN_800f9e50(MEMORY.ref(2, v1).offset(0x1cL).getSigned());
              FUN_800f3354(0, 0x1L, 0, MEMORY.ref(2, v0).offset(0xa0L).getSigned(), 0x118L, 0x87L, 0, s0);
              FUN_800f8cd8(236 - centreScreenX_1f8003dc.get(), 130 - centreScreenY_1f8003de.get(), 0x10L, 0x80L, 0x18L, 0x10L, 0x2cL, -0x1L);
              FUN_800f1268(0xecL, 0x82L, 0x40L, 0xeL, 0x8L);
            }
          } else {
            throw new RuntimeException("Undefined s1");
          }

          //LAB_800f604c
          //LAB_800f6050
          FUN_800f1268(0x2cL, 0x9cL, 0xe8L, 0xeL, 0x8L);
          FUN_800f8ac4((short)s1, _800c6b60.deref(2).offset(0x1cL).getSigned(), 0xa0L, 0xa3L);
        }
      }
    }

    //LAB_800f6088
  }

  @Method(0x800f60acL)
  public static void FUN_800f60ac() {
    final long v0 = _800c6c34.get();
    MEMORY.ref(2, v0).offset(0x0L).setu(0);
    MEMORY.ref(2, v0).offset(0x2L).setu(0);
    MEMORY.ref(2, v0).offset(0x4L).setu(0xffL);
    MEMORY.ref(2, v0).offset(0x8L).setu(0);
    MEMORY.ref(2, v0).offset(0x6L).setu(0);
    MEMORY.ref(2, v0).offset(0xcL).setu(0);
    MEMORY.ref(2, v0).offset(0xaL).setu(0);
    MEMORY.ref(2, v0).offset(0xeL).setu(0);
    MEMORY.ref(2, v0).offset(0x22L).setu(0);
    MEMORY.ref(2, v0).offset(0x24L).setu(0);
    MEMORY.ref(2, v0).offset(0x26L).setu(0);
    MEMORY.ref(2, v0).offset(0x28L).setu(0);
    MEMORY.ref(2, v0).offset(0x2aL).setu(0);
    MEMORY.ref(2, v0).offset(0x2cL).setu(0);

    //LAB_800f60fc
    for(int i = 0; i < 9; i++) {
      MEMORY.ref(2, v0).offset(0x10L).offset(i * 0x2L).setu(-0x1L);
    }

    //LAB_800f611c
    for(int i = 0; i < 10; i++) {
      MEMORY.ref(4, v0).offset(0x30L).offset(i * 0x4L).setu(0);
    }
  }

  @Method(0x800f6134L)
  public static void FUN_800f6134(long a0, long a1, long a2) {
    long v0;
    long v1;
    long a3;
    long t0;
    long t5;
    a3 = 0x8L;
    v1 = 0x800c_0000L;
    t5 = v1 + 0x7194L;

    final long[] sp0x10 = new long[8];
    for(int i = 0; i < 8; i++) {
      sp0x10[i] = MEMORY.ref(2, t5).offset(i * 0x2L).get();
    }

    v0 = _800c6c34.get();
    t0 = v0 + 0x10L;
    MEMORY.ref(2, v0).offset(0x0L).setu(0x1L);
    MEMORY.ref(2, v0).offset(0x2L).setu(0x2L);
    MEMORY.ref(2, v0).offset(0x6L).setu(0xa0L);
    MEMORY.ref(2, v0).offset(0x8L).setu(0xacL);
    MEMORY.ref(2, v0).offset(0x22L).setu(0);
    MEMORY.ref(2, v0).offset(0x24L).setu(0);
    MEMORY.ref(2, v0).offset(0x26L).setu(0);
    MEMORY.ref(2, v0).offset(0x28L).setu(0);
    MEMORY.ref(2, v0).offset(0x2aL).setu(0);
    MEMORY.ref(2, v0).offset(0x2cL).setu(0x80L);

    //LAB_800f61d8
    do {
      MEMORY.ref(2, t0).offset(0x10L).setu(-0x1L);
      a3 = a3 - 0x1L;
      t0 = t0 - 0x2L;
    } while((int)a3 >= 0);

    v0 = _800c6c34.get();
    a3 = 0x9L;
    v0 = v0 + 0x24L;

    //LAB_800f61f8
    do {
      MEMORY.ref(4, v0).offset(0x30L).setu(0);
      a3 = a3 - 0x1L;
      v0 = v0 - 0x4L;
    } while((int)a3 >= 0);

    t0 = _800c677c.get();

    if((int)t0 >= 0x4L) {
      t0 = 0x3L;
    }

    //LAB_800f6224
    v1 = _8006e398.getAddress();

    //LAB_800f6234
    for(a3 = 0; a3 < t0; a3++) {
      if(MEMORY.ref(4, v1).offset(0xe40L).get() == a0) {
        break;
      }

      v1 = v1 + 0x4L;
    }

    //LAB_800f6254
    v1 = _800c6c34.get();
    MEMORY.ref(2, v1).offset(0xeL).setu(0);
    MEMORY.ref(2, v1).offset(0x4L).setu(scriptStatePtrArr_800bc1c0.get((int)_8006e398.offset(a3 * 0x4L).offset(4, 0xe40L).get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class)._272.get());

    //LAB_800f62a4
    a0 = 0;
    for(int i = 0; i < 8; i++) {
      if((a1 & (1 << i)) != 0) {
        MEMORY.ref(2, v1).offset(0x10L).offset(a0).setu(sp0x10[i]);
        MEMORY.ref(2, v1).offset(0xeL).addu(0x1L);
        a0 = a0 + 0x2L;
      }

      //LAB_800f62d0
    }

    a1 = _800c6c34.get();
    MEMORY.ref(2, a1).offset(0xcL).setu(0);
    MEMORY.ref(2, a1).offset(0xaL).setu((MEMORY.ref(2, a1).offset(0xeL).getSigned() * 19 - 3) / 2);
    FUN_800f8b74(a2);
  }

  @Method(0x800f6330L)
  public static long FUN_800f6330() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long t0;
    long t1;
    long t2;
    long s0;
    long s1;
    long s2;
    long lo;
    v0 = 0x800c_0000L;
    v0 = MEMORY.ref(4, v0).offset(0x6c34L).get();
    v1 = MEMORY.ref(2, v0).offset(0x0L).getSigned();
    v0 = MEMORY.ref(2, v0).offset(0x0L).get();
    if(v1 != 0) {
      s1 = 0;
      s2 = 0x13L;
      v0 = v0 - 0x1L;
      v0 = v0 << 16;
      v1 = (int)v0 >> 16;
      s0 = 0x3L;

      switch((int)v1) {
        case 0 -> {
          v0 = 0x800c_0000L;
          a2 = 0x9L;
          a1 = MEMORY.ref(4, v0).offset(0x6c34L).get();
          v0 = 0x2L;
          v1 = MEMORY.ref(2, a1).offset(0x6L).get();
          a3 = a1 + 0x24L;
          MEMORY.ref(2, a1).offset(0x0L).setu(v0);
          v0 = MEMORY.ref(2, a1).offset(0xaL).get();
          a0 = MEMORY.ref(2, a1).offset(0x22L).getSigned();
          v1 = v1 - v0;
          v0 = a0 << 2;
          v0 = v0 + a0;
          v0 = v0 << 2;
          v0 = v0 - a0;
          v1 = v1 + v0;
          v0 = MEMORY.ref(2, a1).offset(0x8L).get();
          v1 = v1 - 0x4L;
          MEMORY.ref(2, a1).offset(0x28L).setu(v1);
          v0 = v0 - 0x16L;
          MEMORY.ref(2, a1).offset(0x2aL).setu(v0);

          //LAB_800f63e8
          do {
            MEMORY.ref(4, a3).offset(0x30L).setu(0);
            a2 = a2 - 0x1L;
            a3 = a3 - 0x4L;
          } while((int)a2 >= 0);
          v0 = 0x800c_0000L;
          a1 = v0 + 0x6c30L;
          MEMORY.ref(2, v0).offset(0x697cL).setu(0);
          MEMORY.ref(1, v0).offset(0x6ba1L).setu(0);
          MEMORY.ref(1, v0).offset(0x6ba0L).setu(0);

          //LAB_800f6424
          final long[] sp0x18 = new long[4];
          for(int i = 0; i < 4; i++) {
            sp0x18[i] = 0xffL;
            MEMORY.ref(1, a1).offset(i).setu(0);
          }
          v0 = 0x800c_0000L;
          t2 = v0 + 0x6718L;
          t0 = 0x800c_0000L;
          a3 = 0x18L;

          //LAB_800f6458
          for(a2 = 0; a2 < 4; a2++) {
            a0 = 0;
            v0 = a3 + t2;
            a1 = MEMORY.ref(1, v0).offset(0x0L).get();

            //LAB_800f646c
            for(int i = 0; i < 4; i++) {
              if(sp0x18[i] == a1) {
                a0 = 0x1L;
                break;
              }

              //LAB_800f6480
            }

            if(a0 == 0) {
              v1 = a3 + t2;
              v0 = MEMORY.ref(1, t0).offset(0x6ba0L).get();
              v1 = MEMORY.ref(1, v1).offset(0x0L).get();
              sp0x18[(int)v0] = v1;
              v0 = 0x800c_0000L;
              v1 = MEMORY.ref(1, t0).offset(0x6ba0L).get();
              v0 = v0 + 0x6c30L;
              v1 = v1 + v0;
              v0 = 0x800c_0000L;
              MEMORY.ref(1, v1).offset(0x0L).setu(a2);
              v1 = MEMORY.ref(4, v0).offset(0x66b0L).get();
              v0 = a2 & 0xffL;
              if(v1 == v0) {
                v0 = 0x800c_0000L;
                v1 = MEMORY.ref(1, t0).offset(0x6ba0L).get();
                MEMORY.ref(1, v0).offset(0x6ba1L).setu(v1);
              }

              //LAB_800f64dc
              v0 = MEMORY.ref(1, t0).offset(0x6ba0L).get();

              v0 = v0 + 0x1L;
              MEMORY.ref(1, t0).offset(0x6ba0L).setu(v0);
            }

            //LAB_800f64ec
            a3 = a3 + 0x4L;
          }
        }
        case 1 -> {
          v0 = 0x800c_0000L;
          v1 = 0x800c_0000L;
          a0 = MEMORY.ref(1, v1).offset(0x6ba0L).get();
          a3 = MEMORY.ref(4, v0).offset(0x6c34L).get();
          MEMORY.ref(4, a3).offset(0x40L).setu(0);
          MEMORY.ref(4, a3).offset(0x44L).setu(0);
          if(a0 >= 0x2L && (joypadInput_8007a39c.get() & 0x2L) != 0) {
            v1 = 0x800c_0000L;
            v0 = MEMORY.ref(1, v1).offset(0x6ba1L).get();

            v0 = v0 + 0x1L;
            MEMORY.ref(1, v1).offset(0x6ba1L).setu(v0);
            v0 = v0 & 0xffL;
            if(v0 < a0) {
              v0 = 0x8008_0000L;
            } else {
              v0 = 0x8008_0000L;
              MEMORY.ref(1, v1).offset(0x6ba1L).setu(0);
            }

            //LAB_800f6560
            v0 = MEMORY.ref(4, v0).offset(-0x5c48L).get();
            a1 = 0x3cL;
            lo = (int)a1 / (int)v0;
            a1 = lo;
            a0 = 0;
            a2 = 0x800c_0000L;
            v1 = MEMORY.ref(1, v1).offset(0x6ba1L).get();
            v0 = 0x800c_0000L;
            v0 = v0 + 0x6c30L;
            v1 = v1 + v0;
            v1 = MEMORY.ref(1, v1).offset(0x0L).get();
            v0 = 0x21L;
            MEMORY.ref(4, a2).offset(0x6748L).setu(v0);
            v0 = 0x5L;
            MEMORY.ref(2, a3).offset(0x0L).setu(v0);
            v0 = 0x800c_0000L;
            MEMORY.ref(4, v0).offset(0x66b0L).setu(v1);
            a1 = a1 + 0x2L;
            MEMORY.ref(4, a3).offset(0x44L).setu(a1);
            FUN_800f8c38(a0);
            break;
          }

          //LAB_800f65b8
          v0 = 0x8008_0000L;
          v1 = MEMORY.ref(4, v0).offset(-0x5c64L).get();
          v0 = v1 & 0x2000L;
          if(v0 != 0) {
            playSound(0, 1, 0, 0, (short)0, (short)0);
            v0 = 0x800c_0000L;
            a1 = MEMORY.ref(4, v0).offset(0x6c34L).get();

            v0 = MEMORY.ref(2, a1).offset(0xeL).getSigned();
            v1 = MEMORY.ref(2, a1).offset(0x22L).getSigned();
            v0 = v0 - 0x1L;
            if((int)v1 < (int)v0) {
              v0 = MEMORY.ref(2, a1).offset(0x22L).get();
              v0 = v0 + 0x1L;

              //LAB_800f6640
              MEMORY.ref(2, a1).offset(0x22L).setu(v0);
              v0 = 0x3L;
              MEMORY.ref(2, a1).offset(0x0L).setu(v0);

              //LAB_800f664c
              v0 = 0x800c_0000L;
              v0 = MEMORY.ref(4, v0).offset(0x6c34L).get();

              MEMORY.ref(4, v0).offset(0x30L).setu(s0);
              MEMORY.ref(4, v0).offset(0x34L).setu(s2);
              MEMORY.ref(4, v0).offset(0x38L).setu(0);
              MEMORY.ref(2, v0).offset(0x26L).setu(0);
              break;
            }
            v0 = 0x4L;
            MEMORY.ref(2, a1).offset(0x0L).setu(v0);
            v0 = MEMORY.ref(2, a1).offset(0x2L).get();
            v1 = MEMORY.ref(2, a1).offset(0x6L).getSigned();
            a0 = MEMORY.ref(2, a1).offset(0xaL).getSigned();
            MEMORY.ref(2, a1).offset(0x22L).setu(0);
            v0 = v0 | 0x1L;
            v1 = v1 - a0;
            v1 = v1 - 0x17L;
            MEMORY.ref(2, a1).offset(0x2L).setu(v0);
            MEMORY.ref(4, a1).offset(0x3cL).setu(v1);
            v0 = 0x800c_0000L;
            v0 = MEMORY.ref(4, v0).offset(0x6c34L).get();

            MEMORY.ref(4, v0).offset(0x30L).setu(s0);
            MEMORY.ref(4, v0).offset(0x34L).setu(s2);
            MEMORY.ref(4, v0).offset(0x38L).setu(0);
            MEMORY.ref(2, v0).offset(0x26L).setu(0);
            break;
          }

          //LAB_800f6664
          v0 = v1 & 0x8000L;
          if(v0 != 0) {
            playSound(0, 1, 0, 0, (short)0, (short)0);
            v0 = 0x800c_0000L;
            a1 = MEMORY.ref(4, v0).offset(0x6c34L).get();

            v0 = MEMORY.ref(2, a1).offset(0x22L).getSigned();
            v1 = MEMORY.ref(2, a1).offset(0x22L).get();
            if(v0 != 0) {
              v0 = v1 - 0x1L;
              //LAB_800f66f0
              MEMORY.ref(2, a1).offset(0x22L).setu(v0);
              v0 = 0x3L;
              MEMORY.ref(2, a1).offset(0x0L).setu(v0);

              //LAB_800f66fc
              v0 = 0x800c_0000L;
              v0 = MEMORY.ref(4, v0).offset(0x6c34L).get();
              v1 = -s2;
              MEMORY.ref(4, v0).offset(0x30L).setu(s0);
              MEMORY.ref(4, v0).offset(0x34L).setu(v1);

              //LAB_800f6710
              MEMORY.ref(4, v0).offset(0x38L).setu(0);
              MEMORY.ref(2, v0).offset(0x26L).setu(0);
              break;
            }
            v0 = v1 - 0x1L;
            v1 = MEMORY.ref(2, a1).offset(0x2L).get();
            v0 = 0x4L;
            MEMORY.ref(2, a1).offset(0x0L).setu(v0);
            v0 = MEMORY.ref(2, a1).offset(0xeL).get();
            v1 = v1 | 0x1L;
            MEMORY.ref(2, a1).offset(0x2L).setu(v1);
            v1 = MEMORY.ref(2, a1).offset(0x6L).getSigned();
            v0 = v0 - 0x1L;
            MEMORY.ref(2, a1).offset(0x22L).setu(v0);
            v0 = MEMORY.ref(2, a1).offset(0xaL).getSigned();
            a0 = MEMORY.ref(2, a1).offset(0xeL).getSigned();
            v1 = v1 - v0;
            v0 = a0 << 2;
            v0 = v0 + a0;
            v0 = v0 << 2;
            v0 = v0 - a0;
            v1 = v1 + v0;
            v1 = v1 - 0x4L;
            MEMORY.ref(4, a1).offset(0x3cL).setu(v1);
            v0 = 0x800c_0000L;
            v0 = MEMORY.ref(4, v0).offset(0x6c34L).get();
            v1 = -s2;
            MEMORY.ref(4, v0).offset(0x30L).setu(s0);
            MEMORY.ref(4, v0).offset(0x34L).setu(v1);
            MEMORY.ref(4, v0).offset(0x38L).setu(0);
            MEMORY.ref(2, v0).offset(0x26L).setu(0);
            break;
          }

          //LAB_800f671c
          v0 = 0x8008_0000L;
          v1 = MEMORY.ref(4, v0).offset(-0x5c68L).get();
          v0 = v1 & 0x20L;
          if(v0 == 0) {
            //LAB_800f6898
            v0 = v1 & 0x40L;
            if(v0 != 0) {
              //LAB_800f68a4
              //LAB_800f68bc
              playSound(0, 3, 0, 0, (short)0, (short)0);
            }
          } else {
            s0 = 0x800c_0000L;
            a0 = MEMORY.ref(4, s0).offset(0x6c34L).get();

            v0 = MEMORY.ref(2, a0).offset(0x22L).getSigned();

            v0 = v0 << 1;
            v0 = a0 + v0;
            v1 = MEMORY.ref(2, v0).offset(0x10L).get();

            v0 = v1 & 0x80L;
            if(v0 != 0) {
              v1 = v1 & 0xfL;
              playSound(0, 3, 0, 0, (short)0, (short)0);
            } else {
              v1 = v1 & 0xfL;
              v0 = 0x5L;
              if(v1 != v0) {
                v0 = 0x3L;

                //LAB_800f6790
                if(v1 != v0) {
                  //LAB_800f6858
                  a1 = 0x2L;

                  //LAB_800f6860
                  playSound(0, (int)a1, 0, 0, (short)0, (short)0);
                  v0 = MEMORY.ref(4, s0).offset(0x6c34L).get();
                  v0 = v0 + MEMORY.ref(2, v0).offset(0x22L).getSigned() * 0x2L;
                  s1 = MEMORY.ref(2, v0).offset(0x10L).get() & 0xfL;
                } else {
                  v0 = 0x800c_0000L;
                  v0 = MEMORY.ref(4, v0).offset(0x677cL).get();

                  if((int)v0 <= 0) {
                    a2 = 0;
                  } else {
                    a2 = 0;
                    a1 = MEMORY.ref(1, a0).offset(0x4L).get();
                    a0 = v0;
                    v0 = 0x800c_0000L;
                    v1 = v0 + 0x6960L;

                    //LAB_800f67b8
                    do {
                      v0 = MEMORY.ref(1, v1).offset(0x0L).get();

                      if(v0 == a1) {
                        break;
                      }

                      a2 = a2 + 0x1L;
                      v1 = v1 + 0x9L;
                    } while((int)a2 < (int)a0);
                  }

                  //LAB_800f67d8
                  a0 = 0x1L;
                  v0 = 0x800c_0000L;
                  a3 = v0 + 0x6960L;
                  v0 = a2 << 3;
                  v1 = v0 + a2;
                  a1 = 0xffL;

                  //LAB_800f67f4
                  do {
                    v0 = a0 + v1;
                    v0 = v0 + a3;
                    v0 = MEMORY.ref(1, v0).offset(0x0L).get();

                    if(v0 != a1) {
                      break;
                    }
                    a0 = a0 + 0x1L;
                  } while((int)a0 < 0x9L);

                  v0 = 0x9L;

                  //LAB_800f681c
                  if(a0 == v0) {
                    playSound(0, 3, 0, 0, (short)0, (short)0);
                  } else {
                    v0 = 0x800c_0000L;
                    v1 = MEMORY.ref(4, v0).offset(0x6c34L).get();
                    v0 = MEMORY.ref(2, v1).offset(0x22L).getSigned();
                    v0 = v0 << 1;
                    v1 = v1 + v0;
                    v0 = MEMORY.ref(2, v1).offset(0x10L).get();
                    s1 = v0 & 0xfL;
                    playSound(0, 2, 0, 0, (short)0, (short)0);
                  }
                }
              } else {
                FUN_800f83c8();
                v0 = 0x800c_0000L;
                v0 = MEMORY.ref(2, v0).offset(0x6b70L).getSigned();

                if(v0 == 0) {
                  playSound(0, 3, 0, 0, (short)0, (short)0);
                } else {
                  playSound(0, 2, 0, 0, (short)0, (short)0);
                  v0 = MEMORY.ref(4, s0).offset(0x6c34L).get();
                  v0 = v0 + MEMORY.ref(2, v0).offset(0x22L).getSigned() * 0x2L;
                  v0 = MEMORY.ref(2, v0).offset(0x10L).get();
                  s1 = MEMORY.ref(2, v0).offset(0x10L).get() & 0xfL;
                }
              }
            }
          }

          //LAB_800f68c4
          v0 = 0x800c_0000L;

          //LAB_800f68c8
          v1 = MEMORY.ref(4, v0).offset(0x6c34L).get();
          v0 = 0x1L;
          MEMORY.ref(4, v1).offset(0x40L).setu(v0);
        }
        case 2 -> {
          v0 = 0x800c_0000L;
          a2 = MEMORY.ref(4, v0).offset(0x6c34L).get();
          a0 = MEMORY.ref(4, a2).offset(0x34L).get();
          v0 = MEMORY.ref(4, a2).offset(0x30L).get();
          lo = (int)a0 / (int)v0;
          a0 = lo;
          v1 = MEMORY.ref(4, a2).offset(0x38L).get();
          a1 = MEMORY.ref(4, a2).offset(0x30L).get();
          v0 = MEMORY.ref(2, a2).offset(0x28L).get();
          v1 = v1 + 0x1L;
          MEMORY.ref(4, a2).offset(0x38L).setu(v1);
          v0 = v0 + a0;
          MEMORY.ref(2, a2).offset(0x28L).setu(v0);
          if((int)v1 >= (int)a1) {
            v1 = MEMORY.ref(2, a2).offset(0x6L).get();
            v0 = 0x2L;
            MEMORY.ref(2, a2).offset(0x0L).setu(v0);
            v0 = MEMORY.ref(2, a2).offset(0xaL).get();
            a0 = MEMORY.ref(2, a2).offset(0x22L).getSigned();
            MEMORY.ref(4, a2).offset(0x38L).setu(0);
            MEMORY.ref(4, a2).offset(0x34L).setu(0);
            MEMORY.ref(4, a2).offset(0x30L).setu(0);
            v1 = v1 - v0;
            v0 = a0 << 2;
            v0 = v0 + a0;
            v0 = v0 << 2;
            v0 = v0 - a0;
            v1 = v1 + v0;
            v0 = MEMORY.ref(2, a2).offset(0x8L).get();
            v1 = v1 - 0x4L;
            MEMORY.ref(2, a2).offset(0x28L).setu(v1);
            v0 = v0 - 0x16L;
            MEMORY.ref(2, a2).offset(0x2aL).setu(v0);
          }
        }
        case 3 -> {
          v0 = 0x800c_0000L;
          t0 = MEMORY.ref(4, v0).offset(0x6c34L).get();
          a2 = MEMORY.ref(4, t0).offset(0x34L).get();
          v0 = MEMORY.ref(4, t0).offset(0x30L).get();
          lo = (int)a2 / (int)v0;
          a2 = lo;
          a1 = MEMORY.ref(4, t0).offset(0x34L).get();
          v0 = MEMORY.ref(4, t0).offset(0x30L).get();
          lo = (int)a1 / (int)v0;
          a1 = lo;
          a3 = MEMORY.ref(4, t0).offset(0x30L).get();
          t1 = 0x80L;
          lo = (int)t1 / (int)a3;
          a3 = lo;
          a0 = MEMORY.ref(4, t0).offset(0x38L).get();
          v1 = MEMORY.ref(4, t0).offset(0x3cL).get();
          v0 = MEMORY.ref(2, t0).offset(0x28L).get();
          a0 = a0 + 0x1L;
          MEMORY.ref(4, t0).offset(0x38L).setu(a0);
          v0 = v0 + a2;
          MEMORY.ref(2, t0).offset(0x28L).setu(v0);
          v0 = MEMORY.ref(2, t0).offset(0x2cL).get();
          v1 = v1 + a1;
          MEMORY.ref(4, t0).offset(0x3cL).setu(v1);
          v1 = MEMORY.ref(4, t0).offset(0x30L).get();
          v0 = v0 - a3;
          MEMORY.ref(2, t0).offset(0x2cL).setu(v0);
          if((int)a0 >= (int)v1) {
            v1 = MEMORY.ref(2, t0).offset(0x6L).get();
            v0 = 0x2L;
            MEMORY.ref(2, t0).offset(0x0L).setu(v0);
            v0 = MEMORY.ref(2, t0).offset(0xaL).get();
            a0 = MEMORY.ref(2, t0).offset(0x22L).getSigned();
            MEMORY.ref(2, t0).offset(0x2cL).setu(t1);
            MEMORY.ref(4, t0).offset(0x38L).setu(0);
            MEMORY.ref(4, t0).offset(0x34L).setu(0);
            MEMORY.ref(4, t0).offset(0x30L).setu(0);
            v1 = v1 - v0;
            v0 = a0 << 2;
            v0 = v0 + a0;
            v0 = v0 << 2;
            v0 = v0 - a0;
            v1 = v1 + v0;
            v0 = MEMORY.ref(2, t0).offset(0x8L).get();
            v1 = v1 - 0x4L;
            MEMORY.ref(2, t0).offset(0x28L).setu(v1);
            v1 = MEMORY.ref(2, t0).offset(0x2L).get();
            v0 = v0 - 0x16L;
            v1 = v1 & 0xfffeL;
            MEMORY.ref(2, t0).offset(0x2aL).setu(v0);
            MEMORY.ref(2, t0).offset(0x2L).setu(v1);
          }
        }
        case 4 -> {
          v0 = _800c6c34.get();
          MEMORY.ref(4, v0).offset(0x44L).subu(0x1L);
          if(MEMORY.ref(4, v0).offset(0x44L).get() == 0x1L) {
            FUN_800f8c38(0x1L);
            v1 = _800c6c34.get();
            MEMORY.ref(2, v1).offset(0x0L).setu(0x2L);
          }
        }
      }

      //LAB_800f6a88
      //LAB_800f6a8c
      v1 = _800c6c34.get();
      MEMORY.ref(2, v1).offset(0x24L).addu(0x1L);
      if(MEMORY.ref(2, v1).offset(0x24L).getSigned() >= 0x4L) {
        MEMORY.ref(2, v1).offset(0x24L).setu(0);
        MEMORY.ref(2, v1).offset(0x26L).addu(0x1L);
        if(MEMORY.ref(2, v1).offset(0x26L).getSigned() >= 0x4L) {
          MEMORY.ref(2, v1).offset(0x26L).setu(0);
        }
      }

      //LAB_800f6ae0
      FUN_800f6b04();
      v0 = s1;
    }

    //LAB_800f6aec
    return v0;
  }

  @Method(0x800f6b04L)
  public static void FUN_800f6b04() {
    long v0;
    long v1;
    long a0;
    long a1;
    long a2;
    long a3;
    long t0;
    long t1;
    long t2;
    long t7;
    long s0;
    long s1;
    long s2;
    long s3;
    long s4;
    long s5;
    long s6;
    long s7;
    long fp;
    a1 = 0x800c_0000L;
    v1 = MEMORY.ref(4, a1).offset(0x6c34L).get();
    v0 = 0x800c_0000L;
    t7 = v0 + 0x71bcL;
    final long[] sp0x30 = new long[10];
    for(int i = 0; i < 10; i++) {
      sp0x30[i] = MEMORY.ref(2, t7).offset(i * 0x2L).get();
    }

    t7 = v0 + 0x71d0L;
    final long[] sp0x48 = new long[10];
    for(int i = 0; i < 10; i++) {
      sp0x48[i] = MEMORY.ref(2, t7).offset(i * 0x2L).get();
    }

    t7 = v0 + 0x71e4L;
    final long[] sp0x60 = new long[4];
    for(int i = 0; i < 4; i++) {
      sp0x60[i] = MEMORY.ref(2, t7).offset(i * 0x2L).get();
    }

    if(MEMORY.ref(2, v1).offset(0x0L).getSigned() != 0) {
      if((MEMORY.ref(2, v1).offset(0x2L).get() & 0x2L) != 0) {
        //LAB_800f6c48
        for(s5 = 0; s5 < _800c6c34.deref(2).offset(0xeL).getSigned(); s5++) {
          a1 = _800c6c34.get();
          fp = (MEMORY.ref(2, a1).offset(0x10L).offset(s5 * 0x2L).get() & 0xfL) - 0x1L;
          if(MEMORY.ref(2, a1).offset(0x22L).getSigned() == s5) {
            s6 = sp0x60[(int)MEMORY.ref(2, a1).offset(0x26L).getSigned()];
          } else {
            //LAB_800f6c88
            s6 = 0;
          }

          //LAB_800f6c90
          a2 = _800c6c34.get();
          a0 = MEMORY.ref(2, a2).offset(0x6L).get() - MEMORY.ref(2, a2).offset(0xaL).get() + s5 * 19;
          s3 = a0 - centreScreenX_1f8003dc.get();
          s2 = s6;
          s0 = fp;
          v0 = _800fb6bc.offset(s0 * 6 + s2 * 2).getAddress();
          a2 = a2 + s5 * 0x2L;
          s4 = MEMORY.ref(2, a2).offset(0x8L).get() - MEMORY.ref(2, v0).offset(0x0L).get() - centreScreenY_1f8003de.get();
          if((MEMORY.ref(2, a2).offset(0x10L).get() & 0x80L) != 0) {
            FUN_800f8cd8(s3, MEMORY.ref(2, a2).offset(0x8L).get() - (centreScreenY_1f8003de.get() + 0x10L), 0x60L, 0x70L, 0x10L, 0x10L, 0x19L, -0x1L);
          }

          //LAB_800f6d70
          a0 = _800c6c34.get();
          if((MEMORY.ref(2, a0).offset(s5 * 0x2L).offset(0x10L).get() & 0xfL) != 0x2L) {
            //LAB_800f6e24
            s0 = _800fb674.offset(s0 * 0x8L).offset(2, 0x4L).get();
          } else if(MEMORY.ref(2, a0).offset(0x4L).getSigned() == 0 && (gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xffL) >>> 7 != 0) {
            s0 = sp0x48[9];
            if(s2 != 0) {
              //LAB_800f6de0
              FUN_800f8cd8(s3 + 0x4L, s4, s2 != 0x1L ? 0x58L : 0x50L, 0x70L, 0x8L, 0x10L, 0x98L, 0x1L);
            }
          } else {
            s0 = sp0x48[(int)MEMORY.ref(2, a0).offset(0x4L).getSigned()];
          }

          //LAB_800f6e34
          //LAB_800f6e38
          //LAB_800f6e3c
          t1 = _800fb674.offset(fp * 8).getAddress();
          v1 = s6 * 0x2L + fp * 0x6L;
          t0 = _800fb6f4.offset(v1).getAddress();
          v1 = _800fb6bc.offset(v1).getAddress();
          FUN_800f8cd8(s3, s4, MEMORY.ref(1, t1).offset(0x0L).get(), (MEMORY.ref(1, t1).offset(0x2L).get() + MEMORY.ref(1, t0).offset(0x0L).get()) & 0xffL, 0x10L, MEMORY.ref(2, v1).getSigned(), s0, MEMORY.ref(2, t1).offset(0x6L).getSigned());

          a1 = _800c6c34.get();
          if(MEMORY.ref(2, a1).offset(0x22L).getSigned() == s5 && MEMORY.ref(4, a1).offset(0x40L).get() == 0x1L) {
            t1 = _800fb72c.offset(fp * 8).getAddress();
            a0 = MEMORY.ref(2, a1).offset(0x6L).get() - MEMORY.ref(2, a1).offset(0xaL).get() + s5 * 11 - centreScreenX_1f8003dc.get() - MEMORY.ref(2, t1).offset(0x4L).get() / 2 + 0x8L;
            a1 = MEMORY.ref(2, a1).offset(0x8L).get() - centreScreenY_1f8003de.get() - 0x18L;
            FUN_800f8cd8(a0, a1, MEMORY.ref(1, t1).offset(0x0L).get(), MEMORY.ref(1, t1).offset(0x2L).get(), MEMORY.ref(2, t1).offset(0x4L).get(), 0x8L, MEMORY.ref(2, t1).offset(0x6L).getSigned(), -0x1L);
          }

          //LAB_800f6fa4
        }

        //LAB_800f6fc8
        v0 = _800c6c34.get();
        FUN_800f7210(MEMORY.ref(2, v0).offset(0x28L).getSigned(), MEMORY.ref(2, v0).offset(0x2aL).getSigned(), sp0x30, 0x1fL, 0xcL, 0x1L, MEMORY.ref(2, v0).offset(0x2cL).getSigned());

        if((MEMORY.ref(2, v0).offset(0x2L).get() & 0x1L) != 0) {
          FUN_800f7210(MEMORY.ref(2, v0).offset(0x3cL).getSigned(), MEMORY.ref(2, v0).offset(0x2aL).getSigned(), sp0x30, 0x1fL, 0xcL, 0x1L, 0x80L - MEMORY.ref(2, v0).offset(0x2cL).get());
        }

        //LAB_800f704c
        s0 = MEMORY.ref(2, v0).offset(0xeL).getSigned() * 0x13 + 1;
        s1 = MEMORY.ref(2, v0).offset(0x6L).get() - s0 / 2;
        s2 = MEMORY.ref(2, v0).offset(0x8L).get() - 0xaL;
        FUN_800f74f4(_800fb5dc.getAddress(), s1, s2, s0, 0x2L, 0x2bL, 0x1L, _800fb5dc.offset(1, 0x4L).get());

        final long[] sp0x20 = new long[4];
        final long[] sp0x28 = new long[4];

        sp0x20[0] = s1;
        sp0x20[2] = s1;
        s1 = s1 + s0;
        s3 = MEMORY.ref(2, v0).offset(0x8L).get() - 0x8L;
        sp0x20[1] = s1;
        sp0x20[3] = s1;
        sp0x28[0] = s2;
        sp0x28[1] = s2;
        sp0x28[2] = s3;
        sp0x28[3] = s3;

        //LAB_800f710c
        s7 = _800fb5dc.getAddress();
        fp = _800fb5dc.getAddress() + 0x6L;
        s6 = _800fb614.getAddress();
        for(s5 = 0; s5 < 8; s5++) {
          t0 = s6 + s5 * 0xcL;
          a2 = MEMORY.ref(2, t0).offset(0x6L).getSigned();
          a1 = sp0x20[(int)MEMORY.ref(2, t0).getSigned()] + MEMORY.ref(2, t0).offset(0x2L).get();
          t2 = sp0x28[(int)MEMORY.ref(2, t0).getSigned()] + MEMORY.ref(2, t0).offset(0x4L).get();
          if(a2 != 0) {
            a3 = a2;
          } else {
            a3 = s0;
          }

          //LAB_800f7158
          if(MEMORY.ref(2, t0).offset(0x8L).getSigned() == 0) {
            v1 = 0x2L;
          } else {
            v1 = MEMORY.ref(2, t0).offset(0x8L).get();
          }

          //LAB_800f716c
          FUN_800f74f4(fp + s5 * 0x6L, (short)a1, (short)t2, (short)a3, (short)v1, 0x2bL, 0x1L, MEMORY.ref(1, s7).offset((s5 + 0x1L) * 0x6L).offset(0x4L).get());
        }
      }
    }

    //LAB_800f71e0
  }

  @Method(0x800f7210L)
  public static void FUN_800f7210(long a0, long a1, long[] a2, long a3, long a4, long a5, long a6) {
    long v0;
    long v1;
    final long s0 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x28L);
    FUN_8003b590(s0);
    final long s5;
    if((int)a5 == -0x1L) {
      gpuLinkedListSetCommandTransparency(s0, false);
      s5 = 0;
    } else {
      //LAB_800f728c
      gpuLinkedListSetCommandTransparency(s0, true);
      s5 = a5;
    }

    //LAB_800f7294
    MEMORY.ref(1, s0).offset(0x6L).setu(a6);
    MEMORY.ref(1, s0).offset(0x5L).setu(a6);
    MEMORY.ref(1, s0).offset(0x4L).setu(a6);
    v1 = a2[0] + (a0 - centreScreenX_1f8003dc.get());
    MEMORY.ref(2, s0).offset(0x18L).setu(v1);
    MEMORY.ref(2, s0).offset(0x8L).setu(v1);
    v1 = a2[2] + a2[0] + (a0 - centreScreenX_1f8003dc.get());
    MEMORY.ref(2, s0).offset(0x20L).setu(v1);
    MEMORY.ref(2, s0).offset(0x10L).setu(v1);
    v1 = a2[1] + (a1 - centreScreenY_1f8003de.get());
    MEMORY.ref(2, s0).offset(0x12L).setu(v1);
    MEMORY.ref(2, s0).offset(0xaL).setu(v1);
    v1 = a2[3] + a2[1] + (a1 - centreScreenY_1f8003de.get());
    MEMORY.ref(2, s0).offset(0x22L).setu(v1);
    MEMORY.ref(2, s0).offset(0x1aL).setu(v1);
    v1 = (short)a2[8];
    if(v1 == 0) {
      //LAB_800f7360
      v0 = a2[4];
      MEMORY.ref(1, s0).offset(0x1cL).setu(v0);
      MEMORY.ref(1, s0).offset(0xcL).setu(v0);
      v0 = a2[4] + a2[6];
      MEMORY.ref(1, s0).offset(0x24L).setu(v0);
      MEMORY.ref(1, s0).offset(0x14L).setu(v0);

      //LAB_800f73e0
      v0 = a2[5];
      MEMORY.ref(1, s0).offset(0x15L).setu(v0);
      MEMORY.ref(1, s0).offset(0xdL).setu(v0);
      v0 = a2[5] + a2[7];
      MEMORY.ref(1, s0).offset(0x25L).setu(v0);
      MEMORY.ref(1, s0).offset(0x1dL).setu(v0);
    } else if(v1 == 0x1L) {
      //LAB_800f738c
      v0 = a2[4];
      MEMORY.ref(1, s0).offset(0x1cL).setu(v0);
      MEMORY.ref(1, s0).offset(0xcL).setu(v0);
      v0 = a2[4] + a2[6];
      MEMORY.ref(1, s0).offset(0x24L).setu(v0);
      MEMORY.ref(1, s0).offset(0x14L).setu(v0);
      v0 = a2[5] - 0x1L;
      MEMORY.ref(1, s0).offset(0x25L).setu(v0);
      MEMORY.ref(1, s0).offset(0x1dL).setu(v0);
      v0 = v0 + a2[7];
      MEMORY.ref(1, s0).offset(0x15L).setu(v0);
      MEMORY.ref(1, s0).offset(0xdL).setu(v0);
      //LAB_800f7344
    } else if(v1 == 0x2L) {
      //LAB_800f73b8
      v0 = a2[4] - 0x1L;
      MEMORY.ref(1, s0).offset(0x24L).setu(v0);
      MEMORY.ref(1, s0).offset(0x14L).setu(v0);
      v0 = v0 + a2[6];
      MEMORY.ref(1, s0).offset(0x1cL).setu(v0);
      MEMORY.ref(1, s0).offset(0xcL).setu(v0);

      //LAB_800f73e0
      v0 = a2[5];
      MEMORY.ref(1, s0).offset(0x15L).setu(v0);
      MEMORY.ref(1, s0).offset(0xdL).setu(v0);
      v0 = a2[5] + a2[7];
      MEMORY.ref(1, s0).offset(0x25L).setu(v0);
      MEMORY.ref(1, s0).offset(0x1dL).setu(v0);
    } else if(v1 == 0x3L) {
      //LAB_800f740c
      v0 = a2[4] - 0x1L;
      MEMORY.ref(1, s0).offset(0x24L).setu(v0);
      MEMORY.ref(1, s0).offset(0x14L).setu(v0);
      v0 = v0 + a2[6];
      MEMORY.ref(1, s0).offset(0x1cL).setu(v0);
      MEMORY.ref(1, s0).offset(0xcL).setu(v0);

      //LAB_800f7434
      v0 = a2[5] - 0x1L;
      MEMORY.ref(1, s0).offset(0x25L).setu(v0);
      MEMORY.ref(1, s0).offset(0x1dL).setu(v0);
      v0 = v0 + a2[7];
      MEMORY.ref(1, s0).offset(0x15L).setu(v0);
      MEMORY.ref(1, s0).offset(0xdL).setu(v0);
    }

    //LAB_800f745c
    //LAB_800f7460
    if((int)a4 >= 0) {
      v0 = a4;
    } else {
      v0 = a4 + 0xfL;
    }

    //LAB_800f746c
    v0 = (int)v0 >> 4;
    v0 = v0 << 4;
    v1 = a4 - v0 + 0x1f0L;
    v1 = v1 << 6;
    v0 = v0 + 0x2c0L;
    v0 = v0 & 0x3f0L;
    v0 = (int)v0 >> 4;
    v1 = v1 | v0;
    MEMORY.ref(2, s0).offset(0xeL).setu(v1);
    MEMORY.ref(2, s0).offset(0x16L).setu(GetTPage(0, s5, 0x2c0L, 0x100L));
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + a3 * 0x4L, s0);
  }

  @Method(0x800f74f4L)
  public static void FUN_800f74f4(long a0, long a1, long a2, long a3, long a4, long a5, long a6, long a7) {
    final long s0 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x28L);
    FUN_8003b590(s0);

    final long s5;
    if((int)a6 == -0x1L) {
      gpuLinkedListSetCommandTransparency(s0, false);
      s5 = 0;
    } else {
      //LAB_800f7578
      gpuLinkedListSetCommandTransparency(s0, true);
      s5 = a6;
    }

    //LAB_800f7580
    MEMORY.ref(1, s0).offset(0x4L).setu(0x80L);
    MEMORY.ref(1, s0).offset(0x5L).setu(0x80L);
    MEMORY.ref(1, s0).offset(0x6L).setu(0x80L);
    FUN_800f8fac(s0, a1 - centreScreenX_1f8003dc.get(), a2 - centreScreenY_1f8003de.get(), 0, 0, a3, a4, 0);

    final long v1 = (short)a7;
    long v0;
    if(v1 == 0) {
      //LAB_800f7628
      v0 = MEMORY.ref(1, a0).offset(0x0L).get();
      MEMORY.ref(1, s0).offset(0x1cL).setu(v0);
      MEMORY.ref(1, s0).offset(0xcL).setu(v0);
      v0 = MEMORY.ref(1, a0).offset(0x0L).get() + MEMORY.ref(1, a0).offset(0x2L).get();
      MEMORY.ref(1, s0).offset(0x24L).setu(v0);
      MEMORY.ref(1, s0).offset(0x14L).setu(v0);
      v0 = MEMORY.ref(1, a0).offset(0x1L).get();
      MEMORY.ref(1, s0).offset(0x15L).setu(v0);
      MEMORY.ref(1, s0).offset(0xdL).setu(v0);
      v0 = MEMORY.ref(1, a0).offset(0x1L).get() + MEMORY.ref(1, a0).offset(0x3L).get();
      MEMORY.ref(1, s0).offset(0x25L).setu(v0);
      MEMORY.ref(1, s0).offset(0x1dL).setu(v0);
    } else if(v1 == 0x1L) {
      //LAB_800f7654
      v0 = MEMORY.ref(1, a0).offset(0x0L).get();
      MEMORY.ref(1, s0).offset(0x1cL).setu(v0);
      MEMORY.ref(1, s0).offset(0xcL).setu(v0);
      v0 = MEMORY.ref(1, a0).offset(0x0L).get() + MEMORY.ref(1, a0).offset(0x2L).get();
      MEMORY.ref(1, s0).offset(0x24L).setu(v0);
      MEMORY.ref(1, s0).offset(0x14L).setu(v0);
      v0 = MEMORY.ref(1, a0).offset(0x1L).get() - 0x1L;
      MEMORY.ref(1, s0).offset(0x25L).setu(v0);
      MEMORY.ref(1, s0).offset(0x1dL).setu(v0);
      v0 = v0 + MEMORY.ref(1, a0).offset(0x3L).get();
      MEMORY.ref(1, s0).offset(0x15L).setu(v0);
      MEMORY.ref(1, s0).offset(0xdL).setu(v0);
      //LAB_800f7610
    } else if(v1 == 0x2L) {
      //LAB_800f7680
      v0 = MEMORY.ref(1, a0).offset(0x0L).get() - 0x1L;
      MEMORY.ref(1, s0).offset(0x24L).setu(v0);
      MEMORY.ref(1, s0).offset(0x14L).setu(v0);
      v0 = v0 + MEMORY.ref(1, a0).offset(0x2L).get();
      MEMORY.ref(1, s0).offset(0x1cL).setu(v0);
      MEMORY.ref(1, s0).offset(0xcL).setu(v0);

      //LAB_800f76a8
      v0 = MEMORY.ref(1, a0).offset(0x1L).get();
      MEMORY.ref(1, s0).offset(0x15L).setu(v0);
      MEMORY.ref(1, s0).offset(0xdL).setu(v0);
      v0 = MEMORY.ref(1, a0).offset(0x1L).get() + MEMORY.ref(1, a0).offset(0x3L).get();
      MEMORY.ref(1, s0).offset(0x25L).setu(v0);
      MEMORY.ref(1, s0).offset(0x1dL).setu(v0);
    } else if(v1 == 0x3L) {
      //LAB_800f76d4
      v0 = MEMORY.ref(1, a0).offset(0x0L).get() - 0x1L;
      MEMORY.ref(1, s0).offset(0x24L).setu(v0);
      MEMORY.ref(1, s0).offset(0x14L).setu(v0);
      v0 = v0 + MEMORY.ref(1, a0).offset(0x2L).get();
      MEMORY.ref(1, s0).offset(0x1cL).setu(v0);
      MEMORY.ref(1, s0).offset(0xcL).setu(v0);

      //LAB_800f76fc
      v0 = MEMORY.ref(1, a0).offset(0x1L).get() - 0x1L;
      MEMORY.ref(1, s0).offset(0x25L).setu(v0);
      MEMORY.ref(1, s0).offset(0x1dL).setu(v0);
      v0 = v0 + MEMORY.ref(1, a0).offset(0x3L).get();
      MEMORY.ref(1, s0).offset(0x15L).setu(v0);
      MEMORY.ref(1, s0).offset(0xdL).setu(v0);
    }

    //LAB_800f7724
    //LAB_800f772c
    FUN_800f9024(s0, (short)a5, s5);
  }

  @Method(0x800f7768L)
  public static long FUN_800f7768(final long a0, final long a1) {
    long v1;
    long t1;
    long t4 = 0;
    long t6 = 0;
    long t3 = 0x1L;

    if(a0 == 0x1L) {
      _800c6c34.deref(4).offset(0x4cL).setu(0x1L);
      //LAB_800f77d4
      t1 = _800c6758.get();

      //LAB_800f77e8
      _800c697c.setu(_800c697e.get());
    } else {
      _800c6c34.deref(4).offset(0x4cL).setu(0x1L);
      if((int)a0 < 0x2L && a0 == 0) {
        _800c697c.setu(_800c6980.get());
        t1 = _800c677c.get();
      } else {
        //LAB_800f77f0
        t1 = _800c669c.get();
      }
    }

    //LAB_800f77f4
    if((joypadPress_8007a398.get() & 0x3000L) != 0) {
      _800c697c.addu(0x1L);
      if(_800c697c.getSigned() >= t1) {
        _800c697c.setu(0);
      }
      t3 = 0x1L;
    }

    //LAB_800f7830
    if((joypadPress_8007a398.get() & 0xc000L) != 0) {
      _800c697c.subu(0x1L);
      if(_800c697c.getSigned() < 0) {
        _800c697c.setu(t1 - 0x1L);
      }
      t3 = -0x1L;
    }

    //LAB_800f786c
    //LAB_800f7880
    if(_800c697c.getSigned() < 0 || _800c697c.getSigned() >= (short)t1) {
      //LAB_800f78a0
      _800c697c.setu(0);
      t3 = 0x1L;
    }

    //LAB_800f78ac
    //LAB_800f78d4
    for(v1 = 0; v1 < t1; v1++) {
      t4 = _800c71f0.offset(a0 * 0x4L).deref(4).offset(_800c697c.getSigned() * 0x4L).get();

      if((scriptStatePtrArr_800bc1c0.get((int)t4).deref().ui_60.get() & 0x4000L) == 0) {
        break;
      }

      _800c697c.addu(t3);

      if(_800c697c.get() >= t1) {
        _800c697c.setu(0);
      }

      //LAB_800f792c
      if(_800c697c.getSigned() < 0) {
        _800c697c.setu(t1 - 0x1L);
      }

      //LAB_800f7948
    }

    //LAB_800f7960
    if(v1 == t1) {
      t4 = _800c71f0.offset(a0 * 0x4L).deref(4).offset(_800c697c.getSigned() * 0x4L).get();
      _800c697c.setu(0);
    }

    //LAB_800f7998
    v1 = _800c6c34.get();
    MEMORY.ref(4, v1).offset(0x50L).setu(a0);
    if(a1 == 0) {
      MEMORY.ref(4, v1).offset(0x54L).setu(_800c697c.getSigned());
    } else {
      //LAB_800f79b4
      MEMORY.ref(4, v1).offset(0x54L).setu(-0x1L);
    }

    //LAB_800f79bc
    _800c6c34.deref(4).offset(0x48L).setu(t4);

    if(a0 == 0x1L) {
      //LAB_800f79fc
      _800c697e.setu(_800c697c.get());
    } else if(a0 == 0) {
      _800c6980.setu(_800c697c.get());
    }

    //LAB_800f7a0c
    //LAB_800f7a10
    if((joypadPress_8007a398.get() & 0x20L) != 0) {
      t6 = 0x1L;
      v1 = _800c6c34.get();
      _800c697c.setu(0);
      MEMORY.ref(4, v1).offset(0x4cL).setu(0);
    }

    //LAB_800f7a38
    if((joypadPress_8007a398.get() & 0x40L) != 0) {
      t6 = -0x1L;
      _800c697c.setu(0);
      v1 = _800c6c34.get();
      MEMORY.ref(4, v1).offset(0x48L).setu(t6);
      MEMORY.ref(4, v1).offset(0x4cL).setu(0);
    }

    //LAB_800f7a68
    return t6;
  }

  @Method(0x800f7a74L)
  public static void FUN_800f7a74(long a0) {
    assert false;
  }

  @Method(0x800f83c8L)
  public static void FUN_800f83c8() {
    //LAB_800f83dc
    for(int i = 0; i < 0x40; i++) {
      _800c6988.offset(i).setu(0xffL);
    }

    _800c6b70.setu(0);

    //LAB_800f8420
    for(int a3 = 0; a3 < gameState_800babc8._1e6.get(); a3++) {
      //LAB_800f843c
      for(int a2 = 0; a2 < gameState_800babc8._1e6.get(); a2++) {
        final long a0 = gameState_800babc8._2e9.get(a3).get();
        final long v0 = _800c6988.offset(a2 * 0x2L).get();

        if(v0 == a0) {
          _800c6988.offset(a2 * 0x2L).offset(0x1L).addu(0x1L);
          break;
        }

        //LAB_800f8468
        if(v0 == 0xffL) {
          _800c6988.offset(a2 * 0x2L).setu(a0);
          _800c6988.offset(a2 * 0x2L).offset(0x1L).setu(0x1L);
          _800c6b70.addu(0x1L);
          break;
        }

        //LAB_800f848c
      }

      //LAB_800f84a4
      //LAB_800f84a8
    }

    //LAB_800f84b8
  }

  @Method(0x800f84c0L)
  public static void FUN_800f84c0() {
    // empty
  }

  @Method(0x800f84c8L)
  public static void FUN_800f84c8() {
    loadDrgnBinFile(0, 4113, 0, getMethodAddress(Bttl.class, "FUN_800ee8c4", long.class, long.class, long.class), 0, 0x4L);
  }

  @Method(0x800f8568L)
  public static LodString FUN_800f8568(final BtldScriptData27c a0, long a1) {
    assert false;
    return null;
  }

  @Method(0x800f863cL)
  public static void FUN_800f863c() {
    FUN_80012b1c(0x2L, getMethodAddress(Bttl.class, "FUN_800ef28c", long.class), 0x1L);
  }

  @Method(0x800f8670L)
  public static void FUN_800f8670(final long a0) {
    FUN_80012b1c(0x1L, getMethodAddress(Bttl.class, "FUN_800eee80", long.class), a0);
  }

  @Method(0x800f8ac4L)
  public static void FUN_800f8ac4(long a0, long a1, long a2, long a3) {
    assert false;
  }

  @Method(0x800f8b74L)
  public static void FUN_800f8b74(long a0) {
    long v0 = 0x800c_0000L;
    long t8 = v0 + 0x7194L;
    final short[] sp0x00 = new short[8];
    for(int i = 0; i < 8; i++) {
      sp0x00[i] = (short)MEMORY.ref(2, t8).offset(i * 0x2L).get();
    }

    //LAB_800f8bd8
    for(int t1 = 0; t1 < 8; t1++) {
      if((a0 & (0x1L << t1)) != 0) {
        long a2 = _800c6c34.get();

        //LAB_800f8bf4
        for(int a3 = 0; a3 < 8; a3++) {
          if((MEMORY.ref(2, a2).offset(0x10L).get() & 0xfL) == sp0x00[a3]) {
            MEMORY.ref(2, a2).offset(0x10L).oru(0x80L);
            break;
          }

          //LAB_800f8c10
          a2 = a2 + 0x2L;
        }
      }

      //LAB_800f8c20
    }
  }

  @Method(0x800f8c38L)
  public static void FUN_800f8c38(final long a0) {
    final long v1 = _800c6c34.get();

    if(MEMORY.ref(2, v1).getSigned() != 0) {
      //LAB_800f8c78
      if(a0 != 0x1L || MEMORY.ref(4, v1).offset(0x44L).get() != 0) {
        //LAB_800f8c64
        MEMORY.ref(2, v1).offset(0x2L).and(0xfffdL);
        return;
      }

      MEMORY.ref(2, v1).offset(0x2L).oru(0x2L);
    }

    //LAB_800f8c98
  }

  @Method(0x800f8ca0L)
  public static long FUN_800f8ca0(long a0) {
    assert false;
    return 0;
  }

  @Method(0x800f8cd8L)
  public static void FUN_800f8cd8(final long a0, final long a1, final long a2, final long a3, final long a4, final long a5, final long a6, final long a7) {
    final long v0 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x28L);
    FUN_8003b590(v0);

    final long s1;
    if(a7 == -0x1L) {
      gpuLinkedListSetCommandTransparency(v0, false);
      s1 = 0;
    } else {
      //LAB_800f8d5c
      gpuLinkedListSetCommandTransparency(v0, true);
      s1 = a7;
    }

    //LAB_800f8d64
    MEMORY.ref(1, v0).offset(0x4L).setu(0x80L);
    MEMORY.ref(1, v0).offset(0x5L).setu(0x80L);
    MEMORY.ref(1, v0).offset(0x6L).setu(0x80L);
    FUN_800f8fac(v0, a0, a1, a2 & 0xffL, a3 & 0xffL, a4, a5, 0x1L);
    FUN_800f9024(v0, a6, s1);
  }

  @Method(0x800f8dfcL)
  public static void FUN_800f8dfc(final long a0, final long a1, final long a2, final long a3, final long a4, final long a5, final long a6, final long a7, final long a8) {
    final long t3 = _800c71ec.getAddress();

    final byte[] sp0x20 = new byte[] {
      (byte)MEMORY.ref(1, t3).offset(0x0L).getSigned(),
      (byte)MEMORY.ref(1, t3).offset(0x1L).getSigned(),
      (byte)MEMORY.ref(1, t3).offset(0x2L).getSigned(),
    };

    final long s0 = linkedListAddress_1f8003d8.get();
    linkedListAddress_1f8003d8.addu(0x28L);
    FUN_8003b590(s0);
    gpuLinkedListSetCommandTransparency(s0, false);

    if((int)a8 < 0x6L) {
      final long v0 = (sp0x20[(int)a7] + 0x80) / 6 * a8 - 0x80L;
      MEMORY.ref(1, s0).offset(0x4L).setu(v0);
      MEMORY.ref(1, s0).offset(0x5L).setu(v0);
      MEMORY.ref(1, s0).offset(0x6L).setu(v0);
    } else {
      //LAB_800f8ef4
      MEMORY.ref(1, s0).offset(0x4L).setu(sp0x20[(int)a7]);
      MEMORY.ref(1, s0).offset(0x5L).setu(sp0x20[(int)a7]);
      MEMORY.ref(1, s0).offset(0x6L).setu(sp0x20[(int)a7]);
    }

    FUN_800f8fac(s0, a0, a1, a2 & 0xffL, a3 & 0xffL, a4, a5, 0x1L);
    FUN_800f9024(s0, a6, 0);
  }

  @Method(0x800f8facL)
  public static void FUN_800f8fac(final long a0, final long a1, final long a2, final long a3, final long a4, final long a5, final long a6, final long a7) {
    MEMORY.ref(2, a0).offset(0x18L).setu(a1);
    MEMORY.ref(2, a0).offset(0x8L).setu(a1);
    MEMORY.ref(2, a0).offset(0x12L).setu(a2);
    MEMORY.ref(2, a0).offset(0xaL).setu(a2);
    MEMORY.ref(2, a0).offset(0x22L).setu(a2 + a6);
    MEMORY.ref(2, a0).offset(0x1aL).setu(a2 + a6);
    MEMORY.ref(2, a0).offset(0x20L).setu(a1 + a5);
    MEMORY.ref(2, a0).offset(0x10L).setu(a1 + a5);

    if(a7 == 0x1L) {
      MEMORY.ref(1, a0).offset(0x24L).setu(a3 + a5);
      MEMORY.ref(1, a0).offset(0x14L).setu(a3 + a5);
      MEMORY.ref(1, a0).offset(0x1cL).setu(a3);
      MEMORY.ref(1, a0).offset(0xcL).setu(a3);
      MEMORY.ref(1, a0).offset(0x15L).setu(a4);
      MEMORY.ref(1, a0).offset(0xdL).setu(a4);
      MEMORY.ref(1, a0).offset(0x25L).setu(a4 + a6);
      MEMORY.ref(1, a0).offset(0x1dL).setu(a4 + a6);
    }

    //LAB_800f901c
  }

  @Method(0x800f9024L)
  public static void FUN_800f9024(final long a0, final long a1, final long a2) {
    final long t0;
    final long t1;
    if((int)a1 >= 0x80L) {
      t1 = 0x1L;
      t0 = a1 - 0x80L;
    } else {
      //LAB_800f9080
      t1 = 0;
      t0 = a1;
    }

    //LAB_800f9088
    //LAB_800f9098
    //LAB_800f90a8
    long v1 = (_800c7114.offset(2, t1 * 0x8L + 0x4L).get() + t0 % 0x10L) * 0x40L;
    long v0 = _800c7114.offset(4, t1 * 0x8L).get() + t0 / 0x10L * 0x10L;
    v0 = v0 & 0x3f0L;
    v0 = (int)v0 >> 4;
    v1 = v1 | v0;
    MEMORY.ref(2, a0).offset(0xeL).setu(v1);
    MEMORY.ref(2, a0).offset(0x16L).setu(GetTPage(0, a2, 704, 496));
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x7cL, a0);
  }

  @Method(0x800f9584L)
  public static void FUN_800f9584() {
    final long v0 = _800c6b6c.get();
    MEMORY.ref(2, v0).offset(0x00L).setu(0);
    MEMORY.ref(2, v0).offset(0x02L).setu(0);
    MEMORY.ref(2, v0).offset(0x04L).setu(0);
    MEMORY.ref(2, v0).offset(0x06L).setu(0);
    MEMORY.ref(2, v0).offset(0x08L).setu(0);
    MEMORY.ref(2, v0).offset(0x0aL).setu(0);
    MEMORY.ref(2, v0).offset(0x0cL).setu(0);
    MEMORY.ref(2, v0).offset(0x0eL).setu(0);
    MEMORY.ref(2, v0).offset(0x10L).setu(0);

    //LAB_800f95b8
    for(int i = 0; i < 10; i++) {
      MEMORY.ref(4, v0).offset(0x14L).offset(i * 0x4L).setu(0);
    }
  }

  @Method(0x800f9b78L)
  public static long FUN_800f9b78(final RunningScript a0) {
    _800c6b64.setu(a0.params_20.get(0).deref().get());
    return 0;
  }

  @Method(0x800f9e50L)
  public static long FUN_800f9e50(long a0) {
    assert false;
    return 0;
  }

  @Method(0x800f9ee8L)
  public static void FUN_800f9ee8(final long a0, final long a1, final long a2, final long a3, final long r, final long g, final long b, final boolean transparent) {
    final long s0 = linkedListAddress_1f8003d8.get();
    FUN_8003b690(s0);
    gpuLinkedListSetCommandTransparency(s0, transparent);
    MEMORY.ref(1, s0).offset(0x4L).setu(r);
    MEMORY.ref(1, s0).offset(0x5L).setu(g);
    MEMORY.ref(1, s0).offset(0x6L).setu(b);
    MEMORY.ref(2, s0).offset(0x8L).setu(a0);
    MEMORY.ref(2, s0).offset(0xaL).setu(a1);
    MEMORY.ref(1, s0).offset(0xcL).setu(r);
    MEMORY.ref(1, s0).offset(0xdL).setu(g);
    MEMORY.ref(1, s0).offset(0xeL).setu(b);
    MEMORY.ref(2, s0).offset(0x10L).setu(a2);
    MEMORY.ref(2, s0).offset(0x12L).setu(a3);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x7cL, s0);
    linkedListAddress_1f8003d8.addu(0x14L);

    final long a1_0 = linkedListAddress_1f8003d8.get();
    MEMORY.ref(1, a1_0).offset(0x3L).setu(0x1L);
    MEMORY.ref(4, a1_0).offset(0x4L).setu(0xe100_0200L | (_800bb110.offset((transparent ? 1 : 0) * 0x4L).offset(0x2L).get() | 0xbL) & 0x9ffL);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x7cL, a1_0);
    linkedListAddress_1f8003d8.addu(0x8L);
  }

  @Method(0x800fa068L)
  public static long FUN_800fa068(long a0) {
    assert false;
    return 0;
  }

  @Method(0x800fa090L)
  public static long FUN_800fa090(long a0) {
    assert false;
    return 0;
  }
}
