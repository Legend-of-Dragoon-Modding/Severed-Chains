package legend.game.combat;

import legend.core.Tuple;
import legend.core.gpu.RECT;
import legend.core.gpu.TimHeader;
import legend.core.gte.DVECTOR;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.TmdObjTable;
import legend.core.gte.VECTOR;
import legend.core.memory.Method;
import legend.core.memory.Ref;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.CString;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.TriConsumerRef;
import legend.core.memory.types.TriFunctionRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.SItem;
import legend.game.Scus94491BpeSegment_8005;
import legend.game.combat.types.BattleRenderStruct;
import legend.game.combat.types.BattleScriptDataBase;
import legend.game.combat.types.BattleStruct18cb0;
import legend.game.combat.types.BattleStruct1a8;
import legend.game.combat.types.BattleStruct1a8_c;
import legend.game.combat.types.BtldScriptData27c;
import legend.game.combat.types.BttlLightStruct84;
import legend.game.combat.types.BttlScriptData6c;
import legend.game.combat.types.BttlScriptData6cSub3c;
import legend.game.combat.types.BttlScriptData6cSub3cSub2c;
import legend.game.combat.types.BttlStruct50;
import legend.game.combat.types.BttlStruct58;
import legend.game.combat.types.BttlStructa4;
import legend.game.combat.types.MersenneTwisterSeed;
import legend.game.types.BigStruct;
import legend.game.types.DR_MODE;
import legend.game.types.ExtendedTmd;
import legend.game.types.GsF_LIGHT;
import legend.game.types.GsRVIEW2;
import legend.game.types.LodString;
import legend.game.types.McqHeader;
import legend.game.types.MrgFile;
import legend.game.types.RunningScript;
import legend.game.types.ScriptFile;
import legend.game.types.ScriptState;
import legend.game.types.TmdAnimationFile;

import static legend.core.Hardware.CPU;
import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.Scus94491BpeSegment.FUN_80012444;
import static legend.game.Scus94491BpeSegment.FUN_800127cc;
import static legend.game.Scus94491BpeSegment.FUN_800128a8;
import static legend.game.Scus94491BpeSegment.FUN_80012b1c;
import static legend.game.Scus94491BpeSegment.FUN_8001324c;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment.FUN_80013404;
import static legend.game.Scus94491BpeSegment.FUN_80015704;
import static legend.game.Scus94491BpeSegment.FUN_8001814c;
import static legend.game.Scus94491BpeSegment.FUN_8001af00;
import static legend.game.Scus94491BpeSegment.FUN_8001ff74;
import static legend.game.Scus94491BpeSegment._1f8003c8;
import static legend.game.Scus94491BpeSegment._1f8003f4;
import static legend.game.Scus94491BpeSegment.addToLinkedListTail;
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
import static legend.game.Scus94491BpeSegment.removeFromLinkedList;
import static legend.game.Scus94491BpeSegment.scriptStartEffect;
import static legend.game.Scus94491BpeSegment.setCallback04;
import static legend.game.Scus94491BpeSegment.setCallback08;
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
import static legend.game.Scus94491BpeSegment_8003.ApplyMatrixLV;
import static legend.game.Scus94491BpeSegment_8003.DrawSync;
import static legend.game.Scus94491BpeSegment_8003.GetTPage;
import static legend.game.Scus94491BpeSegment_8003.GsGetLw;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.MoveImage;
import static legend.game.Scus94491BpeSegment_8003.SetDrawMode;
import static legend.game.Scus94491BpeSegment_8003.StoreImage;
import static legend.game.Scus94491BpeSegment_8003.bzero;
import static legend.game.Scus94491BpeSegment_8003.getProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.parseTimHeader;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8004.FUN_80040980;
import static legend.game.Scus94491BpeSegment_8004.additionOffsets_8004f5ac;
import static legend.game.Scus94491BpeSegment_8004.fileCount_8004ddc8;
import static legend.game.Scus94491BpeSegment_8004.ratan2;
import static legend.game.Scus94491BpeSegment_8005._8005e398;
import static legend.game.Scus94491BpeSegment_8005._8005e398_SCRIPT_SIZES;
import static legend.game.Scus94491BpeSegment_8005._8005f428;
import static legend.game.Scus94491BpeSegment_8006._8006e398;
import static legend.game.Scus94491BpeSegment_8006._8006e918;
import static legend.game.Scus94491BpeSegment_8006._8006f1a4;
import static legend.game.Scus94491BpeSegment_8006._8006f1d8;
import static legend.game.Scus94491BpeSegment_8006._8006f1e8;
import static legend.game.Scus94491BpeSegment_8006._8006f244;
import static legend.game.Scus94491BpeSegment_8006._8006f28c;
import static legend.game.Scus94491BpeSegment_8007._8007a3a8;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800babc0;
import static legend.game.Scus94491BpeSegment_800b._800bb104;
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
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.submapStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800c.matrix_800c3548;
import static legend.game.combat.Bttl_800d.FUN_800dabec;
import static legend.game.combat.Bttl_800d.FUN_800dd0d4;
import static legend.game.combat.Bttl_800d.FUN_800dd118;
import static legend.game.combat.Bttl_800e.FUN_800e80c4;
import static legend.game.combat.Bttl_800e.FUN_800e8ffc;
import static legend.game.combat.Bttl_800e.FUN_800e9100;
import static legend.game.combat.Bttl_800e.FUN_800eb9ac;
import static legend.game.combat.Bttl_800e.FUN_800ec4bc;
import static legend.game.combat.Bttl_800e.FUN_800ec51c;
import static legend.game.combat.Bttl_800e.FUN_800ec744;
import static legend.game.combat.Bttl_800e.FUN_800ec8d0;
import static legend.game.combat.Bttl_800e.FUN_800ee610;
import static legend.game.combat.Bttl_800e.FUN_800ef9e4;
import static legend.game.combat.Bttl_800e.FUN_800efd34;
import static legend.game.combat.Bttl_800f.FUN_800f1a00;
import static legend.game.combat.Bttl_800f.FUN_800f417c;
import static legend.game.combat.Bttl_800f.FUN_800f60ac;
import static legend.game.combat.Bttl_800f.FUN_800f6134;
import static legend.game.combat.Bttl_800f.FUN_800f6330;
import static legend.game.combat.Bttl_800f.FUN_800f84c0;
import static legend.game.combat.Bttl_800f.FUN_800f84c8;
import static legend.game.combat.Bttl_800f.FUN_800f8aa4;
import static legend.game.combat.Bttl_800f.FUN_800f8c38;

public final class Bttl_800c {
  private Bttl_800c() { }

  public static final Value _800c6698 = MEMORY.ref(4, 0x800c6698L);
  public static final Value _800c669c = MEMORY.ref(4, 0x800c669cL);
  /** The number of {@link Scus94491BpeSegment_8005#_8005e398}s */
  public static final Value battleStruct1a8Count_800c66a0 = MEMORY.ref(4, 0x800c66a0L);
  public static final Value currentStage_800c66a4 = MEMORY.ref(4, 0x800c66a4L);

  public static final Value _800c66a8 = MEMORY.ref(4, 0x800c66a8L);
  public static final Value _800c66ac = MEMORY.ref(2, 0x800c66acL);

  public static final Value _800c66b0 = MEMORY.ref(4, 0x800c66b0L);

  public static final Value _800c66b4 = MEMORY.ref(4, 0x800c66b4L);
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
  public static final Value _800c676c = MEMORY.ref(4, 0x800c676cL);
  public static final Value _800c6770 = MEMORY.ref(4, 0x800c6770L);
  public static final Value _800c6774 = MEMORY.ref(4, 0x800c6774L);
  public static final Value _800c6778 = MEMORY.ref(4, 0x800c6778L);
  public static final Value _800c677c = MEMORY.ref(4, 0x800c677cL);
  public static final Value _800c6780 = MEMORY.ref(4, 0x800c6780L);

  public static final MATRIX _800c6798 = MEMORY.ref(4, 0x800c6798L, MATRIX::new);
  public static final UnsignedIntRef _800c67b8 = MEMORY.ref(4, 0x800c67b8L, UnsignedIntRef::new);
  public static final Value x_800c67bc = MEMORY.ref(4, 0x800c67bcL);
  public static final Value y_800c67c0 = MEMORY.ref(4, 0x800c67c0L);
  public static final Value _800c67c4 = MEMORY.ref(4, 0x800c67c4L);

  public static final Value _800c67d4 = MEMORY.ref(4, 0x800c67d4L);
  public static final Value _800c67d8 = MEMORY.ref(4, 0x800c67d8L);
  public static final Value _800c67dc = MEMORY.ref(4, 0x800c67dcL);
  public static final Value _800c67e0 = MEMORY.ref(4, 0x800c67e0L);

  public static final Value _800c67e4 = MEMORY.ref(4, 0x800c67e4L);
  public static final Value _800c67e8 = MEMORY.ref(4, 0x800c67e8L);

  /** TODO a struct containing an rview2 */
  public static final GsRVIEW2 rview2_800c67f0 = MEMORY.ref(4, 0x800c67f0L, GsRVIEW2::new);

  public static final Value _800c6878 = MEMORY.ref(4, 0x800c6878L);

  public static final Value _800c68ec = MEMORY.ref(4, 0x800c68ecL);

  public static final Value _800c6912 = MEMORY.ref(1, 0x800c6912L);
  public static final Value _800c6913 = MEMORY.ref(1, 0x800c6913L);

  public static final Pointer<BttlStruct50> _800c6920 = MEMORY.ref(4, 0x800c6920L, Pointer.deferred(4, BttlStruct50::new));

  public static final Value _800c6928 = MEMORY.ref(4, 0x800c6928L);
  public static final Pointer<ArrayRef<BttlLightStruct84>> lights_800c692c = MEMORY.ref(4, 0x800c692cL, Pointer.deferred(4, ArrayRef.of(BttlLightStruct84.class, 3, 0x84, BttlLightStruct84::new)));
  public static final Value _800c6930 = MEMORY.ref(4, 0x800c6930L);

  public static final Value _800c6938 = MEMORY.ref(4, 0x800c6938L);
  /** TODO struct ptr */
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

  public static final ArrayRef<LodString> _800c69d0 = MEMORY.ref(2, 0x800c69d0L, ArrayRef.of(LodString.class, 9, 0x2c, LodString::new));

  /** TODO obj */
  public static final Value _800c6b5c = MEMORY.ref(4, 0x800c6b5cL);
  public static final Pointer<BttlStructa4> _800c6b60 = MEMORY.ref(4, 0x800c6b60L, Pointer.deferred(4, BttlStructa4::new));
  public static final Value _800c6b64 = MEMORY.ref(4, 0x800c6b64L);
  public static final Value _800c6b68 = MEMORY.ref(4, 0x800c6b68L);
  public static final Value _800c6b6c = MEMORY.ref(4, 0x800c6b6cL);
  public static final Value _800c6b70 = MEMORY.ref(2, 0x800c6b70L);

  public static final Value _800c6b78 = MEMORY.ref(4, 0x800c6b78L);

  public static final Value _800c6b9c = MEMORY.ref(4, 0x800c6b9cL);
  public static final Value _800c6ba0 = MEMORY.ref(1, 0x800c6ba0L);
  public static final Value _800c6ba1 = MEMORY.ref(1, 0x800c6ba1L);

  /** Uhh, contains the monsters that Melbu summons during his fight...? */
  public static final ArrayRef<LodString> _800c6ba8 = MEMORY.ref(2, 0x800c6ba8L, ArrayRef.of(LodString.class, 3, 0x2c, LodString::new));

  public static final Value _800c6c2c = MEMORY.ref(4, 0x800c6c2cL);
  public static final Value _800c6c30 = MEMORY.ref(1, 0x800c6c30L);

  public static final Pointer<BttlStruct58> _800c6c34 = MEMORY.ref(4, 0x800c6c34L, Pointer.deferred(4, BttlStruct58::new));
  public static final Value _800c6c38 = MEMORY.ref(4, 0x800c6c38L);
  public static final Value _800c6c3c = MEMORY.ref(2, 0x800c6c3cL);

  /** TODO array of 0x3c-byte structs */
  public static final Value _800c6c40 = MEMORY.ref(2, 0x800c6c40L);

  public static final Value _800c6cf4 = MEMORY.ref(4, 0x800c6cf4L);

  public static final GsF_LIGHT light_800c6ddc = MEMORY.ref(4, 0x800c6ddcL, GsF_LIGHT::new);

  public static final CString _800c6e18 = MEMORY.ref(7, 0x800c6e18L, CString::new);

  public static final Value _800c6e34 = MEMORY.ref(2, 0x800c6e34L);

  public static final Value _800c6e48 = MEMORY.ref(2, 0x800c6e48L);
  public static final Value _800c6e60 = MEMORY.ref(2, 0x800c6e60L);

  public static final Value _800c6e90 = MEMORY.ref(4, 0x800c6e90L);

  /** TODO unknown size, maybe struct or array */
  public static final Value _800c6e9c = MEMORY.ref(2, 0x800c6e9cL);

  /** TODO unknown size, maybe struct or array */
  public static final Value _800c6ecc = MEMORY.ref(2, 0x800c6eccL);

  /** TODO unknown size, maybe struct or array */
  public static final Value _800c6ef0 = MEMORY.ref(2, 0x800c6ef0L);

  /** TODO unknown size, maybe struct or array */
  public static final Value _800c6f04 = MEMORY.ref(2, 0x800c6f04L);

  public static final Value _800c6f30 = MEMORY.ref(4, 0x800c6f30L);

  public static final Value _800c6f4c = MEMORY.ref(2, 0x800c6f4cL);

  public static final Value _800c6fec = MEMORY.ref(1, 0x800c6fecL);

  public static final Value _800c703c = MEMORY.ref(4, 0x800c703cL);

  public static final Value _800c706c = MEMORY.ref(2, 0x800c706cL);

  public static final Value _800c70a4 = MEMORY.ref(4, 0x800c70a4L);

  public static final Value _800c7028 = MEMORY.ref(4, 0x800c7028L);

  public static final Value _800c70e0 = MEMORY.ref(4, 0x800c70e0L);

  public static final Value _800c70f4 = MEMORY.ref(4, 0x800c70f4L);

  public static final Value _800c7114 = MEMORY.ref(2, 0x800c7114L);

  public static final Value _800c7124 = MEMORY.ref(2, 0x800c7124L);

  public static final Value _800c7190 = MEMORY.ref(1, 0x800c7190L);
  public static final Value _800c7191 = MEMORY.ref(1, 0x800c7191L);
  public static final Value _800c7192 = MEMORY.ref(1, 0x800c7192L);
  public static final Value _800c7193 = MEMORY.ref(1, 0x800c7193L);

  public static final Value _800c71ec = MEMORY.ref(1, 0x800c71ecL);

  public static final Value _800c71f0 = MEMORY.ref(4, 0x800c71f0L);

  public static final Value _800c71fc = MEMORY.ref(4, 0x800c71fcL);

  public static final Value _800c721c = MEMORY.ref(4, 0x800c721cL);

  public static final Value _800c726c = MEMORY.ref(4, 0x800c726cL);

  public static final Value _800c723c = MEMORY.ref(4, 0x800c723cL);

  public static final Value _800c724c = MEMORY.ref(4, 0x800c724cL);

  public static final Value _800c7284 = MEMORY.ref(4, 0x800c7284L);

  public static final Value _800c729c = MEMORY.ref(4, 0x800c729cL);

  public static final Value _800c72b4 = MEMORY.ref(4, 0x800c72b4L);

  public static final Value _800d66b0 = MEMORY.ref(1, 0x800d66b0L);

  public static final Value _800d6c30 = MEMORY.ref(1, 0x800d6c30L);

  public static final Value _800fa0b8 = MEMORY.ref(1, 0x800fa0b8L);

  public static final Value _800fa6dc = MEMORY.ref(4, 0x800fa6dcL);
  public static final UnboundedArrayRef<RECT> _800fa6e0 = MEMORY.ref(2, 0x800fa6e0L, UnboundedArrayRef.of(0x8, RECT::new));

  public static final Value _800fa730 = MEMORY.ref(2, 0x800fa730L);

  /** Mersenne Twister seed */
  public static final MersenneTwisterSeed seed_800fa754 = MEMORY.ref(4, 0x800fa754L, MersenneTwisterSeed::new);
  public static final Value _800fa758 = MEMORY.ref(4, 0x800fa758L);

  public static final Value _800fa76c = MEMORY.ref(4, 0x800fa76cL);

  public static final Value _800faaa0 = MEMORY.ref(4, 0x800faaa0L);

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
  /** TODO jump table */
  public static final Value _800fadbc = MEMORY.ref(4, 0x800fadbcL);

  public static final ScriptFile script_800faebc = MEMORY.ref(4, 0x800faebcL, ScriptFile::new);

  public static final Value _800faec4 = MEMORY.ref(2, 0x800faec4L);

  public static final Value _800fafe8 = MEMORY.ref(4, 0x800fafe8L);
  public static final Value _800fafec = MEMORY.ref(1, 0x800fafecL);
  public static final Value _800fb040 = MEMORY.ref(1, 0x800fb040L);

  public static final Value _800fb05c = MEMORY.ref(1, 0x800fb05cL);

  public static final Value _800fb064 = MEMORY.ref(1, 0x800fb064L);

  public static final ArrayRef<UnsignedByteRef> _800fb148 = MEMORY.ref(1, 0x800fb148L, ArrayRef.of(UnsignedByteRef.class, 0x40, 1, UnsignedByteRef::new));

  /** TODO array of unsigned shorts */
  public static final Value _800fb188 = MEMORY.ref(2, 0x800fb188L);

  /** TODO array of unsigned shorts */
  public static final Value _800fb198 = MEMORY.ref(2, 0x800fb198L);

  public static final ArrayRef<Pointer<LodString>> _800fb36c = MEMORY.ref(4, 0x800fb36cL, ArrayRef.of(Pointer.classFor(LodString.class),  3, 4, Pointer.deferred(4, LodString::new)));
  public static final ArrayRef<Pointer<LodString>> _800fb378 = MEMORY.ref(4, 0x800fb378L, ArrayRef.of(Pointer.classFor(LodString.class), 11, 4, Pointer.deferred(4, LodString::new)));
  public static final ArrayRef<Pointer<LodString>> _800fb3a0 = MEMORY.ref(4, 0x800fb3a0L, ArrayRef.of(Pointer.classFor(LodString.class),  7, 4, Pointer.deferred(4, LodString::new)));

  public static final ArrayRef<Pointer<UnboundedArrayRef<Pointer<LodString>>>> _800fb3c0 = MEMORY.ref(4, 0x800fb3c0L, ArrayRef.of(Pointer.classFor(UnboundedArrayRef.classFor(Pointer.classFor(LodString.class))),  6, 4, Pointer.deferred(4, UnboundedArrayRef.of(4, Pointer.deferred(4, LodString::new)))));

  /** TODO array of pointers to shorts? */
  public static final Value _800fb444 = MEMORY.ref(4, 0x800fb444L);

  public static final ArrayRef<ByteRef> _800fb46c = MEMORY.ref(1, 0x800fb46cL, ArrayRef.of(ByteRef.class, 0x10, 1, ByteRef::new));
  public static final ArrayRef<ByteRef> _800fb47c = MEMORY.ref(1, 0x800fb47cL, ArrayRef.of(ByteRef.class, 0x10, 1, ByteRef::new));

  public static final Value _800fb4b4 = MEMORY.ref(2, 0x800fb4b4L);

  public static final Value _800fb534 = MEMORY.ref(2, 0x800fb534L);

  public static final Value _800fb548 = MEMORY.ref(2, 0x800fb548L);

  public static final Value _800fb55c = MEMORY.ref(2, 0x800fb55cL);

  public static final Value _800fb5dc = MEMORY.ref(4, 0x800fb5dcL);

  public static final Value _800fb614 = MEMORY.ref(4, 0x800fb614L);

  public static final Value _800fb674 = MEMORY.ref(4, 0x800fb674L);

  public static final Value _800fb6bc = MEMORY.ref(4, 0x800fb6bcL);

  public static final Value _800fb6f4 = MEMORY.ref(4, 0x800fb6f4L);

  public static final Value _800fb72c = MEMORY.ref(4, 0x800fb72cL);

  public static final CString _800fb954 = MEMORY.ref(5, 0x800fb954L, CString::new);

  @Method(0x800c7304L)
  public static void FUN_800c7304() {
    int a0;
    int a1;
    //LAB_800c7330
    for(a0 = 0, a1 = 0; a0 < _800c66d0.get(); a0++) {
      final long v1 = _8006f1a4.offset(a0 * 0x4L).get();
      if((scriptStatePtrArr_800bc1c0.get((int)v1).deref().ui_60.get() & 0x40L) == 0) {
        _8006f1a4.offset(0x6cL).offset(a1 * 0x4L).setu(v1);
        a1++;
      }

      //LAB_800c736c
    }

    //LAB_800c737c
    _800c669c.setu(a1);

    //LAB_800c73b0
    for(a0 = 0, a1 = 0; a0 < _800c677c.get(); a0++) {
      final long v1 = _8006f1d8.offset(a0 * 0x4L).get();
      if((scriptStatePtrArr_800bc1c0.get((int)v1).deref().ui_60.get() & 0x40L) == 0) {
        _8006f1d8.offset(0x6cL).offset(a1 * 0x4L).setu(v1);
        a1++;
      }

      //LAB_800c73ec
    }

    //LAB_800c73fc
    _800c6760.setu(a1);

    //LAB_800c7430
    for(a0 = 0, a1 = 0; a0 < _800c6768.get(); a0++) {
      final long v1 = _8006f1e8.offset(a0 * 0x4L).get();
      if((scriptStatePtrArr_800bc1c0.get((int)v1).deref().ui_60.get() & 0x40L) == 0) {
        _8006f1e8.offset(0x6cL).offset(a1 * 0x4L).setu(v1);
        a1++;
      }

      //LAB_800c746c
    }

    //LAB_800c747c
    _800c6758.setu(a1);
  }

  @Method(0x800c7488L)
  public static int FUN_800c7488(final long a0, final long a1, final long a2) {
    if((scriptStatePtrArr_800bc1c0.get((int)_8006e398.offset(0xe40L).offset(a0 * 0x4L).get()).deref().ui_60.get() & 0x2L) != 0) {
      final long a0_0 = _1f8003f4.getPointer() + (a0 + 0x3L) * 0x100L + a1 * 0x20L + a2 * 0x2L; //TODO
      return (int)MEMORY.ref(2, a0_0).offset(0x38L).getSigned();
    }

    //LAB_800c74fc
    final long a0_0 = _1f8003f4.getPointer() + a0 * 0x100L + a1 * 0x20L + a2 * 0x2L; //TODO
    return (int)MEMORY.ref(2, a0_0).offset(0x38L).getSigned();
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

  @Method(0x800c79f0L)
  public static void FUN_800c79f0() {
    _800c66c8.setu(_8006f1a4.get());
    FUN_800f417c();
    pregameLoadingStage_800bb10c.addu(0x1L);
  }

  @Method(0x800c7a30L)
  public static void FUN_800c7a30() {
    FUN_80012b1c(0x3L, getMethodAddress(Bttl_800c.class, "FUN_800c7a78", long.class), 0);
    pregameLoadingStage_800bb10c.addu(0x1L);
  }

  @Method(0x800c7a80L)
  public static void FUN_800c7a80() {
    if(_800c66a8.get() != 0) {
      _800bc960.oru(0x10L);

      //LAB_800c7ae4
      for(int i = 0; i < _800c66d0.get(); i++) {
        final ScriptState<?> v0 = scriptStatePtrArr_800bc1c0.get((int)_8006f1a4.offset(i * 0x4L).get()).deref();
        final BtldScriptData27c s1 = v0.innerStruct_00.derefAs(BtldScriptData27c.class);

        if((v0.ui_60.get() & 0x4L) != 0) {
          s1._4c.set((short)(simpleRand() * 0xd9 / 0x10000));
        } else {
          //LAB_800c7b3c
          s1._4c.set((short)(simpleRand() * 0xa7 / 0x10000 + 0x32));
        }

        //LAB_800c7b68
      }

      //LAB_800c7b80
      FUN_80021868();
      pregameLoadingStage_800bb10c.addu(0x1L);
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
    long s6 = 0;
    long hi;
    long lo;

    //LAB_800c7ee4
    for(int s4 = 0; s4 < 32; s4++) {
      //LAB_800c7ef0
      a0 = 0;
      for(int s1 = 0; s1 < _800c669c.get(); s1++) {
        v1 = scriptStatePtrArr_800bc1c0.get((int)_8006e398.offset(4, 0xe78L).offset(s1 * 0x4L).get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class)._4c.get();

        if(v1 >= a0) {
          a0 = v1;
          s6 = s1;
        }

        //LAB_800c7f30
      }

      //LAB_800c7f40
      if(a0 > 0xd9L) {
        a1 = _8006e398.offset(0xe78L).offset(s6 * 0x4L).get();
        final ScriptState<?> state = scriptStatePtrArr_800bc1c0.get((int)a1).deref();
        state.innerStruct_00.derefAs(BtldScriptData27c.class)._4c.set((short)(a0 - 0xd9));

        if((state.ui_60.get() & 0x4L) == 0) {
          gameState_800babc8._b8.incr();
        }

        //LAB_800c7f9c
        return a1;
      }

      //LAB_800c7fa4
      //LAB_800c7fb0
      for(int s1 = 0; s1 < _800c669c.get(); s1++) {
        final BtldScriptData27c s0 = scriptStatePtrArr_800bc1c0.get((int)_8006e398.offset(0xe78L).offset(s1 * 0x4L).get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
        v0 = simpleRand() + 0x4_4925L;
        lo = s0.speed_32.get() * (int)v0 & 0xffff_ffffL;
        a0 = lo;
        v0 = 0x35c2_9183L; //TODO _pretty_ sure this is roughly /312,110 (seems oddly specific?)
        hi = (long)(int)a0 * (int)v0 >>> 32;
        v1 = hi;
        v1 = (int)v1 >> 16;
        a0 = (int)a0 >> 31;
        v1 = v1 - a0;
        s0._4c.add((short)v1);
      }

      //LAB_800c8028
    }

    //LAB_800c8040
    return _8006f244.get();
  }

  @Method(0x800c8624L)
  public static void FUN_800c8624() {
    final BattleStruct18cb0 struct = MEMORY.ref(4, addToLinkedListTail(0x1_8cb0L), BattleStruct18cb0::new);
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

  @Method(0x800c882cL)
  public static void FUN_800c882c() {
    long a0;
    long a1;
    long a3;
    long s0;
    long s2;
    long s3;
    if(_800c6764.get() == 0 || _800c66d4.get() == 0 || (_800bc960.get() & 0x80L) == 0) {
      //LAB_800c8ad8
      //LAB_800c8adc
      _800babc0.setu(0);
      _800bb104.setu(0);
      _8007a3a8.setu(0);
    } else {
      _800c6774.addu(_800c676c.get());
      _800c6778.addu(_800c6770.get());
      a3 = ((_800c66cc.getSigned() * FUN_800dd118()) / 0x1000L + _800c6774.get()) % _1f8003f4.deref().stageMcq_9cb0._14.get() - centreScreenX_1f8003dc.getSigned();
      s2 = _800c6778.get() - (FUN_800dd0d4() + 0x800L & 0xfffL) + 0x760L - centreScreenY_1f8003de.getSigned();
      s0 = a3 - _1f8003f4.deref().stageMcq_9cb0._14.get();
      s3 = a3 + _1f8003f4.deref().stageMcq_9cb0._14.get();
      FUN_8001814c(_1f8003f4.deref().stageMcq_9cb0, 320, 0, a3, s2, _1f8003c8.get() - 0x2L, _800fa6dc.get());
      FUN_8001814c(_1f8003f4.deref().stageMcq_9cb0, 320, 0, s0, s2, _1f8003c8.get() - 0x2L, _800fa6dc.get());

      if(centreScreenX_1f8003dc.getSigned() >= (int)s3) {
        FUN_8001814c(_1f8003f4.deref().stageMcq_9cb0, 320, 0, s3, s2, _1f8003c8.get() - 0x2L, _800fa6dc.get());
      }

      //LAB_800c89d4
      if(_1f8003f4.deref().stageMcq_9cb0.magic_00.get() == McqHeader.MAGIC_1) {
        a0 = s2;
      } else {
        //LAB_800c89f8
        a0 = s2 + _1f8003f4.deref().stageMcq_9cb0._2a.get();
      }

      //LAB_800c8a04
      a1 = _800fa6dc.getSigned();
      if((int)a0 >= -centreScreenY_1f8003de.getSigned()) {
        _8007a3a8.setu(_1f8003f4.deref().stageMcq_9cb0._18.get() * a1 / 0x80L);
        _800bb104.setu(_1f8003f4.deref().stageMcq_9cb0._19.get() * a1 / 0x80L);
        _800babc0.setu(_1f8003f4.deref().stageMcq_9cb0._1a.get() * a1 / 0x80L);
      } else {
        //LAB_800c8a74
        _8007a3a8.setu(_1f8003f4.deref().stageMcq_9cb0._20.get() * a1 / 0x80L);
        _800bb104.setu(_1f8003f4.deref().stageMcq_9cb0._21.get() * a1 / 0x80L);
        _800babc0.setu(_1f8003f4.deref().stageMcq_9cb0._22.get() * a1 / 0x80L);
      }
    }

    //LAB_800c8af0
  }

  @Method(0x800c8b20L)
  public static void loadStage(final long stage) {
    loadDrgnBinFile(0, 2497 + stage, 0, getMethodAddress(Bttl_800c.class, "stageMrgLoadedCallback", long.class, long.class, long.class), 0, 0x2L);
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
      decompress(mrg.getFile(0), _1f8003f4.deref().stageTmdMrg_63c.getAddress(), getMethodAddress(Bttl_800c.class, "stageTmdMrgLoaded", long.class, long.class, long.class), 0, 0);
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

    _800c66d4.setu(0x1L);
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
        } else if(charIndex == 0 && (byte)gameState_800babc8.dragoonSpirits_19c.get(0).get() >>> 7 != 0) { // Divine dragoon?
          fileIndex = 4112;
        } else {
          fileIndex = 4103 + charIndex;
        }
      }

      //LAB_800c9860
      //LAB_800c9864
      loadDrgnBinFile(0, fileIndex, 0, getMethodAddress(Bttl_800c.class, "FUN_800c9898", long.class, long.class, long.class), a3, 0x3L);
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
                if(gameState_800babc8.dragoonSpirits_19c.get(0).get() >>> 7 == 0) {
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
          loadDrgnBinFile(0, fileIndex, a2, getMethodAddress(Bttl_800c.class, "FUN_800c941c", long.class, long.class, long.class), callbackParam, 0x3L);
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
  public static void FUN_800c952c(final BigStruct a0, final int index) {
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

      a0.dobj2ArrPtr_00.setPointer(a0_0);
      a0.coord2ArrPtr_04.setPointer(a0_0 + 0x230L);
      a0.coord2ParamArrPtr_08.setPointer(a0_0 + 0xd20L);
      a0.count_c8.set((short)35);

      final long a3;
      if((s0._1a2.get() & 0x1L) != 0) {
        a3 = 0x9L;
      } else {
        a3 = s0._1a2.get() - 0x200 >>> 1;
      }

      //LAB_800c9650
      FUN_80021520(a0, s2, v0, a3);
    } else {
      //LAB_800c9664
      FUN_80020a00(a0, s2, FUN_800ca31c(index, 0));
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
        s3.bpe_00.set(a0);
        s3._0b.set(0);
        s3._08.set((int)a3);
      } else {
        s3._0a.set((int)a2);
        s3.bpe_00.set(a0);
        s3._0b.set((int)a2);
        s3._08.set((int)a3);
      }
    } else if(a2 == 0x2L) {
      //LAB_800c9b80
      if(isBpe) {
        //LAB_800c9b88
        s3._0a.set(0x5);
        s3.bpe_00.set(a0);
        s3._0b.set(0);
        s3._08.set((int)a3);
      } else {
        //LAB_800c9b98
        s3._0a.set((int)a2);
        s3.bpe_00.set(a0);
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
      s3.bpe_00.set(a3);
      s3._08.set(-0x1);
    } else if(a2 == 0x6L) {
      //LAB_800c9bcc
      final RECT sp0x10 = new RECT((short)(512 + a3 * 64), (short)_8006e398.offset(0xd80L).offset(4, a3 * 0x4L).get(), (short)64, (short)(a1 / 128));
      LoadImage(sp0x10, a0);

      _8006e398.offset(0xd80L).offset(4, a3 * 0x4L).addu(a1 / 128);
      s3.x_00.set(sp0x10.x.get());
      s3.y_02.set(sp0x10.y.get());
      s3.h_03.set(sp0x10.h.get());
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
  public static void FUN_800c9c7c(final long s1, final long a1) {
    final BattleStruct1a8_c s0 = _8005e398.get((int)s1)._14.get((int)a1);

    //LAB_800c9cec
    while(s0._09.get() > 0) {
      FUN_800ca194(s1, a1);
    }

    //LAB_800c9d04
    switch(s0._0a.get()) {
      case 3 -> FUN_800cad64(s0.bpe_00.get());
      case 4, 5 -> {
        if(s0._0b.get() == 0) {
          break;
        }

        final long a0 = s0._04.get();
        if(a0 >= 0) {
          //LAB_800c9d78
          FUN_800cad64(a0);
        } else {
          _8006e398.offset(s0._06.get() * 0x8L).offset(1, 0xd90L).setu(0);
        }
      }

      //LAB_800c9d80
    }

    //LAB_800c9d84
    s0.bpe_00.set(0);
    s0._04.set((short)-1);
    s0._06.set((short)-1);
    s0._08.set(-1);
    s0._09.set(0);
    s0._0a.set(0);
    s0._0b.set(0);

    //LAB_800c9da0
  }

  @Method(0x800c9e10L)
  public static long FUN_800c9e10(final long a0, final long a1) {
    final BattleStruct1a8_c s0 = _8005e398.get((int)a0)._14.get((int)a1);

    return switch(s0._0a.get()) {
      case 1, 2 -> s0.bpe_00.get() != 0 ? 0x1L : 0;
      case 3 -> (int)s0.bpe_00.get() >= 0 ? 0x1L : 0;
      case 4, 5 -> {
        if(s0._0b.get() == 0) {
          final long a3 = _800c66ac.getSigned() + 0x1L & 0xffff_fff0L;
          _800c66ac.setu(a3);
          final long v1 = _8006e398.offset(a3 * 0x8L).getAddress();
          MEMORY.ref(4, v1).offset(0xd8cL).setu(s0.getAddress());
          MEMORY.ref(1, v1).offset(0xd90L).setu(0x1L);
          s0._0b.set(1);
          s0._06.set((short)a3);

          if(decompress(s0.bpe_00.get(), 0, getMethodAddress(Bttl_800c.class, "FUN_800c9fcc", long.class, long.class, long.class), a3, 0x4L) != 0) {
            yield 0;
          }
        }

        yield 0x1L;
      }

      case 6 -> {
        if(s0._0b.get() == 0) {
          final long s1 = FUN_800cab58(s0.h_03.get() * 0x80, 0x3L, 0, 0);
          if((int)s1 < 0) {
            yield 0;
          }

          final RECT sp0x20 = new RECT((short)s0.x_00.get(), (short)s0.y_02.get(), (short)64, (short)s0.h_03.get());
          StoreImage(sp0x20, FUN_800cad34(s1));
          s0._04.set((short)s1);
          s0._0b.set(1);
        }

        //LAB_800c9fb4
        yield 0x1L;
      }

      default -> 0;
    };

    //LAB_800c9fb8
  }

  @Method(0x800c9fccL)
  public static void FUN_800c9fcc(final long address, final long fileSize, final long param) {
    final long s1 = _8006e398.offset(param * 0x8L).getAddress(); //TODO
    final long s0 = MEMORY.ref(4, s1).offset(0xd8cL).get();

    if(MEMORY.ref(1, s0).offset(0xbL).get() != 0 && MEMORY.ref(1, s1).offset(0xd90L).get() != 0) {
      MEMORY.ref(2, s0).offset(0x4L).setu(FUN_800caae4(address, 0x3L, 0, 0));
      MEMORY.ref(2, s0).offset(0x6L).setu(-0x1L);
      MEMORY.ref(1, s1).offset(0xd90L).setu(0);
    } else {
      //LAB_800ca034
      //LAB_800ca038
      removeFromLinkedList(address);
    }

    //LAB_800ca040
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
  public static long FUN_800ca194(final long a0, final long a1) {
    final BattleStruct1a8_c s0 = _8005e398.get((int)a0)._14.get((int)a1);

    if(s0._09.get() > 0) {
      s0._09.decr();
    }

    //LAB_800ca1f4
    final long v1 = s0._0a.get();

    if(v1 == 0) {
      //LAB_800ca250
      return 0;
    }

    if((int)v1 < 0x4L) {
      return 0x1L;
    }

    if((int)v1 >= 0x7L) {
      return 0;
    }

    if(s0._09.get() == 0) {
      if(s0._04.get() >= 0) {
        FUN_800cad64(s0._04.get());
      }

      //LAB_800ca240
      s0._04.set((short)-1);
      s0._06.set((short)-1);
      s0._0b.set(0);
    }

    //LAB_800ca258
    //LAB_800ca25c
    return 0x1L;
  }

  @Method(0x800ca31cL)
  public static TmdAnimationFile FUN_800ca31c(final long a0, final long a1) {
    final BattleStruct1a8_c a0_0 = _8005e398.get((int)a0)._14.get((int)a1);

    return switch(a0_0._0a.get()) {
      case 1, 2 -> MEMORY.ref(4, a0_0.bpe_00.get(), TmdAnimationFile::new); //TODO

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
    return _800fa730.offset(2, _8005e398.get((int)a0)._1a0.get() * 0x2L).getSigned();
  }

  @Method(0x800ca980L)
  public static void FUN_800ca980() {
    //LAB_800ca990
    for(int i = 0; i < 0x200; i++) {
      _8006e918.offset(i * 0x4L).setu(0);
    }

    _800c66c1.setu(0x1L);
  }

  @Method(0x800caa20L)
  public static long FUN_800caa20() {
    _800c66b4.addu(0x1L);
    if(_800c66b4.get() >= 0x100L) {
      _800c66b4.setu(0);
    }

    //LAB_800caa44
    //LAB_800caa64
    for(int i = (int)_800c66b4.get(); i < 0x100; i++) {
      final long a1 = _8006e918.offset(i * 0x8L).getAddress();

      if(MEMORY.ref(1, a1).offset(0x4L).get() == 0) {
        //LAB_800caacc
        _800c66b4.setu(i);
        MEMORY.ref(4, a1).offset(0x0L).setu(0);
        MEMORY.ref(1, a1).offset(0x4L).setu(0x1L);
        return i;
      }
    }

    //LAB_800caa88
    //LAB_800caaa4
    for(int i = 0; i < _800c66b4.get(); i++) {
      final long a1 = _8006e918.offset(i * 0x8L).getAddress();

      if(MEMORY.ref(1, a1).offset(0x4L).get() == 0) {
        //LAB_800caacc
        _800c66b4.setu(i);
        MEMORY.ref(4, a1).offset(0x0L).setu(0);
        MEMORY.ref(1, a1).offset(0x4L).setu(0x1L);
        return i;
      }
    }

    //LAB_800caac4
    return -0x1L;
  }

  @Method(0x800caae4L)
  public static long FUN_800caae4(final long s0, final long a1, final long a2, final long a3) {
    final long v0 = FUN_800caa20();
    if((int)v0 < 0) {
      //LAB_800cab38
      return -0x1L;
    }

    final long a0 = _8006e918.offset(v0 * 0x8L).getAddress();
    MEMORY.ref(4, a0).offset(0x0L).setu(s0);
    MEMORY.ref(1, a0).offset(0x4L).setu(a1);
    MEMORY.ref(1, a0).offset(0x5L).setu(a2);
    MEMORY.ref(1, a0).offset(0x6L).setu(a3);

    //LAB_800cab3c
    return v0;
  }

  @Method(0x800cab58L)
  public static long FUN_800cab58(final long size, final long a1, final long a2, final long a3) {
    final long s0 = addToLinkedListTail(size);
    if(s0 == 0) {
      return -0x1L;
    }

    final long v0 = FUN_800caae4(s0, a1, a2, a3);
    if((int)v0 < 0) {
      removeFromLinkedList(s0);
      return -0x1L;
    }

    return v0;
  }

  @Method(0x800cad34L)
  public static long FUN_800cad34(final long a0) {
    return _8006e918.offset(4, a0 * 0x8L).get();
  }

  @Method(0x800cad64L)
  public static void FUN_800cad64(final long a0) {
    final long s0 = _8006e918.offset(a0 * 0x8L).getAddress();

    if(MEMORY.ref(1, s0).offset(0x4L).get() != 0x1L) {
      FUN_800127cc(MEMORY.ref(4, s0).offset(0x0L).get(), 0, 0x1L);
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
    if(v1 != 0 && FUN_800c90b0(data._26c.get()) != 0) {
      data._1e5.set((int)FUN_800ca938(data._26c.get()));
      data._26e.set((short)0);
      FUN_800c952c(data._148, data._26c.get());
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
      setCallback04(index, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800caf50", int.class, ScriptState.classFor(BtldScriptData27c.class), BtldScriptData27c.class), TriConsumerRef::new));
    }

    //LAB_800caf38
  }

  @Method(0x800caf2cL)
  public static void FUN_800caf50(final int index, final ScriptState<BtldScriptData27c> state, final BtldScriptData27c data) {
    setCallback08(index, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cb024", int.class, ScriptState.classFor(BtldScriptData27c.class), BtldScriptData27c.class), TriConsumerRef::new));
    setCallback04(index, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cafb4", int.class, ScriptState.classFor(BtldScriptData27c.class), BtldScriptData27c.class), TriConsumerRef::new));
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
  public static long FUN_800cb250(final int index, final ScriptState<BtldScriptData27c> state, final BtldScriptData27c data) {
    int x = state._e8.get();
    int y = state._ec.get();
    int z = state._f0.get();

    if(state.scriptIndex_c8.get() >= 0) {
      final BtldScriptData27c data2 = scriptStatePtrArr_800bc1c0.get(state.scriptIndex_c8.get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);

      x += data2._148.coord2_14.coord.transfer.getX();
      y += data2._148.coord2_14.coord.transfer.getY();
      z += data2._148.coord2_14.coord.transfer.getZ();
    }

    //LAB_800cb2ac
    state._cc.decr();
    if(state._cc.get() > 0) {
      state._d0.sub(state._dc.get());
      state._d4.sub(state._e0.get());
      state._d8.sub(state._e4.get());
      data._148.coord2_14.coord.transfer.setX(x - (state._d0.get() >> 8));
      data._148.coord2_14.coord.transfer.setY(y - (state._d4.get() >> 8));
      data._148.coord2_14.coord.transfer.setZ(z - (state._d8.get() >> 8));
      state._e0.add(state._f4.get());
      return 0;
    }

    //LAB_800cb338
    data._148.coord2_14.coord.transfer.set(x, y, z);
    return 0x1L;
  }

  @Method(0x800cb468L)
  public static long FUN_800cb468(final RunningScript a0) {
    final BtldScriptData27c v1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
    a0.params_20.get(1).deref().set(v1._148.coord2_14.coord.transfer.getX());
    a0.params_20.get(2).deref().set(v1._148.coord2_14.coord.transfer.getY());
    a0.params_20.get(3).deref().set(v1._148.coord2_14.coord.transfer.getZ());
    return 0;
  }

  @Method(0x800cb534L)
  public static long FUN_800cb534(final RunningScript a0) {
    scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class)._148.coord2Param_64.rotate.setY((short)a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800cb618L)
  public static long FUN_800cb618(final RunningScript a0) {
    final ScriptState<?> a1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref();

    //LAB_800cb668
    if(a0.params_20.get(1).deref().get() != 0) {
      a1.ui_60.and(0xffff_ffefL);
    } else {
      //LAB_800cb65c
      a1.ui_60.or(0x10L);
    }

    return 0;
  }

  @Method(0x800cb674L)
  public static long FUN_800cb674(final RunningScript a0) {
    final BtldScriptData27c v1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
    v1._1ea.set(a0.params_20.get(1).deref().get() < 1 ? 1 : 0);
    return 0;
  }

  @Method(0x800cb6bcL)
  public static long FUN_800cb6bc(final RunningScript a0) {
    final ScriptState<?> v0 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref();
    if((v0.ui_60.get() & 0x1L) != 0) {
      return 0x2L;
    }

    final BtldScriptData27c s0 = v0.innerStruct_00.derefAs(BtldScriptData27c.class);
    final long s1 = a0.params_20.get(1).deref().get();

    if(s0._270.get() < 0) {
      FUN_800c9e10(s0._26c.get(), s1);
      s0._270.set((short)s1);
    } else if(s0._270.get() != s1) {
      FUN_800ca194(s0._26c.get(), s0._270.get());

      //LAB_800cb73c
      FUN_800c9e10(s0._26c.get(), s1);
      s0._270.set((short)s1);
    }

    //LAB_800cb750
    return 0x1L;
  }

  @Method(0x800cb76cL)
  public static long FUN_800cb76c(final RunningScript a0) {
    final ScriptState<BtldScriptData27c> s2 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).derefAs(ScriptState.classFor(BtldScriptData27c.class));
    final BtldScriptData27c s0 = s2.innerStruct_00.deref();
    if((s2.ui_60.get() & 0x1L) == 0) {
      short s1 = s0._270.get();

      if(s1 < 0) {
        s1 = 0;
      }

      //LAB_800cb7d0
      if(FUN_800ca054(s0._26c.get(), s1) != 0) {
        FUN_800ca194(s0._26c.get(), s0._26e.get());
        FUN_800ca100(s0._148, s0._26c.get(), s1);
        s2.ui_60.and(0xffff_ff6fL);
        s0._1e4.set(1);
        s0._26e.set(s1);
        s0._270.set((short)-1);
        return 0;
      }
    }

    //LAB_800cb830
    //LAB_800cb834
    return 0x2L;
  }

  @Method(0x800cb84cL)
  public static long FUN_800cb84c(final RunningScript a0) {
    final ScriptState<BtldScriptData27c> s2 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).derefAs(ScriptState.classFor(BtldScriptData27c.class));
    final BtldScriptData27c s0 = s2.innerStruct_00.derefAs(BtldScriptData27c.class);

    if((s2.ui_60.get() & 0x1L) == 0) {
      final int s1 = a0.params_20.get(1).deref().get();
      final short a1 = s0._270.get();

      if(a1 >= 0) {
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
    a0.params_20.get(1).deref().set(scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class)._26e.get());
    return 0;
  }

  @Method(0x800cb9f0L)
  public static long FUN_800cb9f0(final RunningScript a0) {
    scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class)._1e4.set(2);
    return 0;
  }

  @Method(0x800cba60L)
  public static long FUN_800cba60(final RunningScript a0) {
    //LAB_800cbab0
    if(a0.params_20.get(1).deref().get() != 0) {
      scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().ui_60.and(0xffff_ff7fL);
    } else {
      //LAB_800cbaa4
      scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().ui_60.or(0x80L);
    }

    return 0;
  }

  @Method(0x800cbabcL)
  public static long FUN_800cbabc(final RunningScript a0) {
    a0.params_20.get(1).deref().set(scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class)._1e6.get() < 0x1L ? 1 : 0);
    return 0;
  }

  @Method(0x800cbb00L)
  public static long FUN_800cbb00(final RunningScript t1) {
    final int s0 = t1.params_20.get(0).deref().get();
    final ScriptState<BtldScriptData27c> a0 = scriptStatePtrArr_800bc1c0.get(s0).derefAs(ScriptState.classFor(BtldScriptData27c.class));
    BtldScriptData27c v1 = a0.innerStruct_00.derefAs(BtldScriptData27c.class);

    int x = v1._148.coord2_14.coord.transfer.getX();
    int y = v1._148.coord2_14.coord.transfer.getY();
    int z = v1._148.coord2_14.coord.transfer.getZ();

    final int t0 = t1.params_20.get(1).deref().get();
    if(t0 >= 0) {
      v1 = scriptStatePtrArr_800bc1c0.get(t0).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
      x -= v1._148.coord2_14.coord.transfer.getX();
      y -= v1._148.coord2_14.coord.transfer.getY();
      z -= v1._148.coord2_14.coord.transfer.getZ();
    }

    //LAB_800cbb98
    a0.scriptIndex_c8.set(t0);
    FUN_800cdc1c(a0, x, y, z, t1.params_20.get(3).deref().get(), t1.params_20.get(4).deref().get(), t1.params_20.get(5).deref().get(), 0, t1.params_20.get(2).deref().get());
    setCallback10(s0, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cb250", int.class, ScriptState.classFor(BtldScriptData27c.class), BtldScriptData27c.class), TriFunctionRef::new));
    return 0;
  }

  @Method(0x800cbde0L)
  public static long FUN_800cbde0(final RunningScript t1) {
    final ScriptState<BtldScriptData27c> a0 = scriptStatePtrArr_800bc1c0.get(t1.params_20.get(0).deref().get()).derefAs(ScriptState.classFor(BtldScriptData27c.class));
    BtldScriptData27c v1 = a0.innerStruct_00.deref();
    int a1 = v1._148.coord2_14.coord.transfer.getX();
    int a2 = v1._148.coord2_14.coord.transfer.getY();
    int a3 = v1._148.coord2_14.coord.transfer.getZ();

    if(t1.params_20.get(1).deref().get() >= 0) {
      final ScriptState<BtldScriptData27c> v0 = scriptStatePtrArr_800bc1c0.get(t1.params_20.get(1).deref().get()).derefAs(ScriptState.classFor(BtldScriptData27c.class));
      v1 = v0.innerStruct_00.deref();
      a1 -= v1._148.coord2_14.coord.transfer.getX();
      a2 -= v1._148.coord2_14.coord.transfer.getY();
      a3 -= v1._148.coord2_14.coord.transfer.getZ();
    }

    //LAB_800cbe78
    a0.scriptIndex_c8.set(t1.params_20.get(1).deref().get());
    FUN_800cdc1c(a0, a1, a2, a3, t1.params_20.get(3).deref().get(), t1.params_20.get(4).deref().get(), t1.params_20.get(5).deref().get(), 0x20, t1.params_20.get(2).deref().get());
    setCallback10(t1.params_20.get(0).deref().get(), MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cb250", int.class, ScriptState.classFor(BtldScriptData27c.class), BtldScriptData27c.class), TriFunctionRef::new));
    return 0;
  }

  @Method(0x800cc0c8L)
  public static long FUN_800cc0c8(final RunningScript t1) {
    final int s0 = t1.params_20.get(0).deref().get();
    final ScriptState<BtldScriptData27c> a0 = scriptStatePtrArr_800bc1c0.get(s0).derefAs(ScriptState.classFor(BtldScriptData27c.class));
    BtldScriptData27c v1 = a0.innerStruct_00.deref();
    final VECTOR a1 = new VECTOR().set(v1._148.coord2_14.coord.transfer);
    final int t0 = t1.params_20.get(1).deref().get();

    if(t0 >= 0) {
      v1 = scriptStatePtrArr_800bc1c0.get(t0).derefAs(ScriptState.classFor(BtldScriptData27c.class)).innerStruct_00.deref();
      a1.sub(v1._148.coord2_14.coord.transfer);
    }

    //LAB_800cc160
    a0.scriptIndex_c8.set(t0);
    FUN_800cdc1c(a0, a1.getX(), a1.getY(), a1.getZ(), t1.params_20.get(3).deref().get(), a1.getY(), t1.params_20.get(4).deref().get(), 0, t1.params_20.get(2).deref().get());
    setCallback10(s0, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cb250", int.class, ScriptState.classFor(BtldScriptData27c.class), BtldScriptData27c.class), TriFunctionRef::new));
    return 0;
  }

  @Method(0x800cc608L)
  public static long FUN_800cc608(final RunningScript a0) {
    final BtldScriptData27c s0 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
    final BtldScriptData27c v0 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(1).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);

    s0._148.coord2Param_64.rotate.setY((short)(ratan2(v0._148.coord2_14.coord.transfer.getX() - s0._148.coord2_14.coord.transfer.getX(), v0._148.coord2_14.coord.transfer.getZ() - s0._148.coord2_14.coord.transfer.getZ()) + 0x800L));
    return 0;
  }

  @Method(0x800cca34L)
  public static long FUN_800cca34(final RunningScript s1) {
    if(_800c675c.get() != s1.params_20.get(0).deref().get() || (s1.scriptState_04.deref().ui_60.get() & 0x1000L) != 0) {
      //LAB_800cca7c
      final int a0 = s1.scriptStateIndex_00.get();
      final int a1 = s1.params_20.get(0).deref().get();
      final int a2;

      if(s1.childCount_14.get() == 0x2L) {
        a2 = 0;
      } else {
        //LAB_800ccaa0
        a2 = s1.params_20.get(1).deref().get();
      }

      //LAB_800ccab4
      FUN_800f6134(a0, a1, a2);

      s1.scriptState_04.deref().ui_60.and(0xffff_efffL);
      _800c675c.setu(s1.params_20.get(0).deref().get());
    }

    //LAB_800ccaec
    FUN_800f8c38(0x1L);

    final int s0 = FUN_800f6330();
    if(s0 == 0) {
      //LAB_800ccb24
      return 0x2L;
    }

    FUN_800f8c38(0);
    s1.params_20.get(2).deref().set(s0 - 1);

    //LAB_800ccb28
    return 0;
  }

  @Method(0x800ccb3cL)
  public static long FUN_800ccb3c(final RunningScript a0) {
    FUN_800f8aa4(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800cccf4L)
  public static long FUN_800cccf4(final RunningScript a0) {
    a0.params_20.get(1).deref().set(scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class)._272.get());
    return 0;
  }

  @Method(0x800ccd34L)
  public static long FUN_800ccd34(final RunningScript a0) {
    int v1 = a0.params_20.get(1).deref().get();
    if(a0.params_20.get(2).deref().get() == 0x2L && v1 < 0) {
      v1 = 0;
    }

    //LAB_800ccd8c
    final BtldScriptData27c a1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
    a1.all_04.get(a0.params_20.get(2).deref().get()).set((short)v1);
    return 0;
  }

  @Method(0x800ccda0L)
  public static long FUN_800ccda0(final RunningScript a0) {
    final BtldScriptData27c a1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);
    a1.all_04.get(Math.max(0, a0.params_20.get(2).deref().get())).set((short)a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800cce04L)
  public static long FUN_800cce04(final RunningScript a0) {
    final BtldScriptData27c a1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);

    if(a0.params_20.get(1).deref().get() == 0x2L) {
      a0.params_20.get(2).deref().set(a1.hp_08.get());
    } else {
      //LAB_800cce54
      a0.params_20.get(2).deref().set(a1.all_04.get(a0.params_20.get(1).deref().get()).get());
    }

    //LAB_800cce68
    return 0;
  }

  @Method(0x800cce70L)
  public static long FUN_800cce70(final RunningScript a0) {
    a0.params_20.get(2).deref().set(scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class).all_04.get(a0.params_20.get(1).deref().get()).get());
    return 0;
  }

  @Method(0x800ccec8L)
  public static long FUN_800ccec8(final RunningScript a0) {
    FUN_800f1a00(a0.params_20.get(0).deref().get() > 0 ? 1 : 0);
    return 0;
  }

  @Method(0x800ccf0cL)
  public static long FUN_800ccf0c(final RunningScript a0) {
    _800bc974.setu(a0.params_20.get(0).deref().get());
    return 0x2L;
  }

  @Method(0x800cd0ecL)
  public static long FUN_800cd0ec(final RunningScript a0) {
    a0.params_20.get(3).deref().set(FUN_800c7488(
      scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class)._276.get(),
      a0.params_20.get(1).deref().get(),
      a0.params_20.get(2).deref().get()
    ));

    return 0;
  }

  @Method(0x800cd998L)
  public static long FUN_800cd998(final RunningScript a0) {
    final BtldScriptData27c v1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BtldScriptData27c.class);

    if(a0.params_20.get(2).deref().get() != 0) {
      a0.params_20.get(1).deref().set(v1._276.get());
    } else {
      //LAB_800cd9e8
      a0.params_20.get(1).deref().set(v1._274.get());
    }

    //LAB_800cd9f4
    return 0;
  }

  @Method(0x800cdc1cL)
  public static void FUN_800cdc1c(final ScriptState<BtldScriptData27c> s1, final int x, final int y, final int z, final int a4, final int a5, final int a6, final int a7, final int a8) {
    final int v0 = a4 - x << 8;
    final int v1 = a5 - y << 8;
    final int s0 = a6 - z << 8;
    final int s3 = a7 << 8;

    s1._cc.set(a8);
    s1._e8.set(a4);
    s1._ec.set(a5);
    s1._f0.set(a6);
    s1._d0.set(v0);
    s1._d4.set(v1);
    s1._d8.set(s0);
    s1._dc.set(v0 / a8);
    s1._e0.set(FUN_80013404(s3, v1, a8));
    s1._e4.set(s0 / a8);
    s1._f4.set(s3);
  }

  @Method(0x800cdcecL)
  public static void FUN_800cdcec(final BigStruct a0, final int a1, final VECTOR a2, final VECTOR a3, final BttlScriptData6c a4, final UnsignedShortRef a5, final UnsignedShortRef a6) {
    short t4 = 0x7fff;
    short t3 = -1;
    int t7 = 0;
    int t5 = 0;
    final TmdObjTable v0 = a0.dobj2ArrPtr_00.deref().get(a1).tmd_08.deref();
    long verts = v0.vert_top_00.get();

    //LAB_800cdd24
    for(int i = 0; i < v0.n_vert_04.get(); i++) {
      final long a1_0 = verts + a4._10._24.get() * 0x2L;
      final long v1 = MEMORY.ref(2, a1_0).offset(0x0L).getSigned();
      if(v1 <= t4) {
        t4 = (short)MEMORY.ref(2, a1_0).offset(0x0L).getSigned();
        t7 = i;
        a2.setX((int)MEMORY.ref(2, verts).offset(0x0L).getSigned());
        a2.setY((int)MEMORY.ref(2, verts).offset(0x2L).getSigned());
        a2.setZ((int)MEMORY.ref(2, verts).offset(0x4L).getSigned());
        //LAB_800cdd7c
      } else if(v1 >= t3) {
        t3 = (short)MEMORY.ref(2, a1_0).offset(0x0L).get();
        t5 = i;
        a3.setX((int)MEMORY.ref(2, verts).offset(0x0L).getSigned());
        a3.setY((int)MEMORY.ref(2, verts).offset(0x2L).getSigned());
        a3.setZ((int)MEMORY.ref(2, verts).offset(0x4L).getSigned());
      }

      //LAB_800cddbc
      verts = verts + 0x8L;
    }

    //LAB_800cddcc
    a5.set(t7);
    a6.set(t5);
  }

  @Method(0x800cdde4L)
  public static BttlScriptData6cSub3cSub2c FUN_800cdde4(final BttlScriptData6cSub3c a0) {
    BttlScriptData6cSub3cSub2c v1 = a0._38.deref();

    //LAB_800cddfc
    while(!v1._24.isNull()) {
      v1 = v1._24.deref();
    }

    //LAB_800cde14
    return v1;
  }

  @Method(0x800cde1cL)
  public static BttlScriptData6cSub3cSub2c FUN_800cde1c(final BttlScriptData6cSub3c a0) {
    BttlScriptData6cSub3cSub2c v1 = a0._34.deref().get(0);

    //LAB_800cde3c
    int i;
    for(i = 1; v1._03.get() != 0; i++) {
      v1 = a0._34.deref().get(i);
    }

    //LAB_800cde50
    if(i == 64) {
      v1 = FUN_800cdde4(a0);
      v1._03.set(0);

      if(!v1._28.isNull()) {
        v1._28.deref()._24.clear();
      }
    }

    //LAB_800cde80
    //LAB_800cde84
    return v1;
  }

  @Method(0x800cde94L) // BttlScriptData6cSub3c
  public static void FUN_800cde94(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c data) {
    long v1;
    long a0;
    long a1;
    long a2;
    long s1;
    long s4;

    final BttlScriptData6cSub3c s2 = data._44.derefAs(BttlScriptData6cSub3c.class);

    if(!s2._38.isNull()) {
      final SVECTOR sp0x18 = new SVECTOR().set(data._10.svec_1c);
      final SVECTOR sp0x20 = new SVECTOR().set(sp0x18).div(s2._0e.get());
      BttlScriptData6cSub3cSub2c s0 = s2._38.deref();

      final Ref<Long> sp0x38 = new Ref<>();
      final Ref<Long> sp0x3c = new Ref<>();
      FUN_800cf244(s0._04.get(0), sp0x38, sp0x3c);

      final Ref<Long> sp0x40 = new Ref<>();
      final Ref<Long> sp0x44 = new Ref<>();
      s1 = (int)FUN_800cf244(s0._04.get(1), sp0x40, sp0x44) / 0x4L;

      //LAB_800cdf94
      s0 = s0._24.derefNullable();
      for(s4 = 0; s4 < s2._0e.get() && s0 != null; s4++) {
        final Ref<Long> sp0x28 = new Ref<>();
        final Ref<Long> sp0x2c = new Ref<>();
        FUN_800cf244(s0._04.get(0), sp0x28, sp0x2c);
        final Ref<Long> sp0x30 = new Ref<>();
        final Ref<Long> sp0x34 = new Ref<>();
        FUN_800cf244(s0._04.get(1), sp0x30, sp0x34);
        a1 = linkedListAddress_1f8003d8.get();
        linkedListAddress_1f8003d8.addu(0x24L);
        MEMORY.ref(1, a1).offset(0x03L).setu(0x8L);
        MEMORY.ref(4, a1).offset(0x04L).setu(0x3a00_0000L);
        MEMORY.ref(2, a1).offset(0x08L).setu(sp0x38.get());
        MEMORY.ref(2, a1).offset(0x0aL).setu(sp0x3c.get());
        MEMORY.ref(1, a1).offset(0x0cL).setu(0);
        MEMORY.ref(1, a1).offset(0x0dL).setu(0);
        MEMORY.ref(1, a1).offset(0x0eL).setu(0);
        MEMORY.ref(2, a1).offset(0x10L).setu(sp0x28.get());
        MEMORY.ref(2, a1).offset(0x12L).setu(sp0x2c.get());
        MEMORY.ref(1, a1).offset(0x14L).setu(sp0x18.getX() >>> 8);
        MEMORY.ref(1, a1).offset(0x15L).setu(sp0x18.getY() >>> 8);
        MEMORY.ref(1, a1).offset(0x16L).setu(sp0x18.getZ() >>> 8);
        sp0x18.sub(sp0x20);
        MEMORY.ref(2, a1).offset(0x18L).setu(sp0x40.get());
        MEMORY.ref(2, a1).offset(0x1aL).setu(sp0x44.get());
        MEMORY.ref(1, a1).offset(0x1cL).setu(sp0x18.getX() >>> 8);
        MEMORY.ref(1, a1).offset(0x1dL).setu(sp0x18.getY() >>> 8);
        MEMORY.ref(1, a1).offset(0x1eL).setu(sp0x18.getZ() >>> 8);
        MEMORY.ref(2, a1).offset(0x20L).setu(sp0x30.get());
        MEMORY.ref(2, a1).offset(0x22L).setu(sp0x34.get());
        a0 = s1 + data._10._22.get();
        if((int)a0 >= 0xa0L) {
          if((int)a0 >= 0xffeL) {
            a0 = s1 + (0xffeL - s1);
          }

          //LAB_800ce138
          insertElementIntoLinkedList(tags_1f8003d0.getPointer() + (int)a0 / 4 * 4, a1);
        }

        //LAB_800ce14c
        sp0x38.set(sp0x28.get());
        sp0x3c.set(sp0x34.get());
        sp0x40.set(sp0x34.get());
        sp0x44.set(sp0x34.get());
        s0 = s0._24.derefNullable();
      }

      //LAB_800ce1a0
      //LAB_800ce1a4
      SetDrawMode(linkedListAddress_1f8003d8.deref(4).cast(DR_MODE::new), false, true, GetTPage(0x1L, 0x1L, 0, 0), null);

      a2 = data._10._22.get();
      v1 = s1 + a2;
      if((int)v1 >= 0xa0L) {
        if((int)v1 >= 0xffeL) {
          a2 = 0xffeL - s1;
        }

        //LAB_800ce210
        insertElementIntoLinkedList(tags_1f8003d0.getPointer() + (int)(s1 + a2) / 4 * 4, linkedListAddress_1f8003d8.get());
      }

      linkedListAddress_1f8003d8.addu(0xcL);
    }

    //LAB_800ce230
  }

  @Method(0x800ce254L)
  public static void FUN_800ce254(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c data) {
    long s5;
    long s6;

    final BttlScriptData6cSub3c s3 = data._44.derefAs(BttlScriptData6cSub3c.class);
    s3._00.incr();
    if(s3._00.get() == 0) {
      FUN_800cdcec(s3._30.deref(), s3._08.get(), s3._20, s3._10, data, s3._0c, s3._0a);
      return;
    }

    //LAB_800ce2c4
    BttlScriptData6cSub3cSub2c s0 = FUN_800cde1c(s3);

    if(!s3._38.isNull()) {
      s3._38.deref()._28.set(s0);
    }

    //LAB_800ce2e4
    s0._00.set(0x6c);
    s0._01.set(0x63);
    s0._02.set(0x73);
    s0._03.set(0x1);
    s0._28.clear();
    s0._24.setNullable(s3._38.derefNullable());
    s3._38.set(s0);

    //LAB_800ce320
    for(int i = 0; i < 2; i++) {
      final MATRIX sp0x20 = new MATRIX();
      GsGetLw(s3._30.deref().coord2ArrPtr_04.deref().get(s3._08.get()), sp0x20);
      final VECTOR sp0x40 = ApplyMatrixLV(sp0x20, s3._10);
      sp0x40.add(sp0x20.transfer);
      s0._04.get(i).set(sp0x40);
    }

    //LAB_800ce3e0
    s0 = s3._38.derefNullable();
    while(s0 != null) {
      FUN_800ce880(s0._04.get(1), s0._04.get(0), 0x1000, 0x400);
      s0 = s0._24.derefNullable();
    }

    //LAB_800ce404
    //LAB_800ce40c
    for(s5 = 0; s5 < 2; s5++) {
      s0 = s3._38.derefNullable();
      s6 = 0;

      //LAB_800ce41c
      while(s0 != null) {
        if(!s0._28.isNull()) {
          if(!s0._24.isNull()) {
            BttlScriptData6cSub3cSub2c s1 = s0._24.deref();

            //LAB_800ce444
            final BttlScriptData6cSub3cSub2c[] sp0x50 = new BttlScriptData6cSub3cSub2c[2];
            for(int s2 = 0; s2 < 2; s2++) {
              final BttlScriptData6cSub3cSub2c v0 = FUN_800cde1c(s3);
              sp0x50[s2] = v0;
              v0._00.set(0x6c);
              v0._01.set(0x63);
              v0._02.set(0x73);
              v0._03.set(0x1);
              v0._04.get(0).set(s1._04.get(0)).sub(s0._04.get(0)).div(3).add(s0._04.get(0));
              v0._04.get(1).set(s1._04.get(1)).sub(s0._04.get(1)).div(3).add(s0._04.get(1));
              s1 = s0._28.deref();
            }

            sp0x50[0]._24.set(s0._24.deref());
            sp0x50[1]._24.set(sp0x50[0]);
            sp0x50[1]._28.set(s0._28.deref());
            sp0x50[0]._28.set(sp0x50[1]);
            s0._28.deref()._24.set(sp0x50[1]);
            s0._24.deref()._28.set(sp0x50[0]);
            s0._03.set(0);
            s0 = s0._24.derefNullable();
            s6++;
            if(s6 > s5 * 0x2L || s0 == null) {
              break;
            }
          }
        }

        //LAB_800ce630
        s0 = s0._24.derefNullable();
      }

      //LAB_800ce640
    }

    //LAB_800ce650
  }

  @Method(0x800ce678L) // BttlScriptData6cSub3c
  public static void FUN_800ce678(final int index, final ScriptState<BttlScriptData6c> state, final BttlScriptData6c data6c) {
    assert false;
  }

  @Method(0x800ce6a8L)
  public static long FUN_800ce6a8(final RunningScript a0) {
    final int s4 = FUN_800e80c4(a0.scriptStateIndex_00.get(), 0x3cL, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800ce254", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new), MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cde94", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new), MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800ce678", int.class, ScriptState.classFor(BttlScriptData6c.class), BttlScriptData6c.class), TriConsumerRef::new), BttlScriptData6cSub3c::new);
    final BttlScriptData6c s1 = scriptStatePtrArr_800bc1c0.get(s4).deref().innerStruct_00.derefAs(BttlScriptData6c.class);
    final BttlScriptData6cSub3c s0 = s1._44.derefAs(BttlScriptData6cSub3c.class);
    s0._34.setPointer(addToLinkedListTail(0x2c * 65));

    //LAB_800ce75c
    for(int i = 0; i < 65; i++) {
      final BttlScriptData6cSub3cSub2c v1 = s0._34.deref().get(i);
      v1._00.set(0x6c);
      v1._01.set(0x63);
      v1._02.set(0x73);
      v1._03.set(0);
      v1._24.clear();
      v1._28.clear();
    }

    s0._38.clear();
    s0._00.set(-1);
    s0._04.set(a0.params_20.get(1).deref().get());
    s0._08.set((short)a0.params_20.get(2).deref().get());
    s0._0e.set(0x14);
    s1._10.svec_1c.set((short)255, (short)128, (short)96);
    final BattleScriptDataBase a0_0 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(1).deref().get()).deref().innerStruct_00.derefAs(BattleScriptDataBase.class);

    if(a0_0.magic_00.get() == 0x2020_4d45L) {
      s0._30.set(MEMORY.ref(4, ((BttlScriptData6c)a0_0)._44.getPointer() + 0x10, BigStruct::new)); //TODO
    } else {
      //LAB_800ce7f8
      s0._30.set(((BtldScriptData27c)a0_0)._148);
    }

    //LAB_800ce804
    a0.params_20.get(0).deref().set(s4);
    return 0;
  }

  @Method(0x800ce83cL)
  public static void FUN_800ce83c(final long a0, long a1) {
    final long v1 = scriptStatePtrArr_800bc1c0.get((int)a0).deref().innerStruct_00.derefAs(BttlScriptData6c.class)._44.getPointer(); //TODO
    a1 = a1 * 0x4L;
    if((a1 & 0xffL) > 0x40L) {
      a1 = 0x40L;
    }

    //LAB_800ce878
    MEMORY.ref(1, v1).offset(0xeL).setu(a1);
  }

  @Method(0x800ce880L)
  public static void FUN_800ce880(final VECTOR a0, final VECTOR a1, final int a2, final int a3) {
    final VECTOR sp0x00 = new VECTOR();
    final VECTOR sp0x10 = new VECTOR();
    sp0x00.set(a0).sub(a1);
    sp0x10.set(sp0x00).mul(a2).div(0x1000);
    a0.set(a1).add(sp0x10);
    sp0x10.set(sp0x00).mul(a3).div(0x1000);
    a1.add(sp0x10);
  }

  @Method(0x800cea1cL)
  public static VECTOR FUN_800cea1c(final long a0, final VECTOR a1) {
    final ScriptState<BattleScriptDataBase> state = scriptStatePtrArr_800bc1c0.get((int)a0).derefAs(ScriptState.classFor(BattleScriptDataBase.class));
    final BattleScriptDataBase data = state.innerStruct_00.deref();

    final VECTOR s0;
    if(data.magic_00.get() == 0x2020_4d45L) {
      //LAB_800cea78
      s0 = ((BttlScriptData6c)data)._10.vec_04;
    } else {
      s0 = ((BtldScriptData27c)data)._148.coord2_14.coord.transfer;
    }

    a1.set(s0);

    //LAB_800cea8c
    return s0;
  }

  @Method(0x800ceeccL)
  public static long FUN_800ceecc(final RunningScript a0) {
    FUN_800ce83c(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800cf244L)
  public static long FUN_800cf244(final VECTOR a0, final Ref<Long> a1, final Ref<Long> a2) {
    CPU.CTC2(matrix_800c3548.getPacked(0), 0);
    CPU.CTC2(matrix_800c3548.getPacked(2), 1);
    CPU.CTC2(matrix_800c3548.getPacked(4), 2);
    CPU.CTC2(matrix_800c3548.getPacked(6), 3);
    CPU.CTC2(matrix_800c3548.getPacked(8), 4);

    final SVECTOR a0s = new SVECTOR().set(a0);
    CPU.MTC2(a0s.getXY(), 0);
    CPU.MTC2(a0s.getZ(),  1);
    CPU.COP2(0x486012L);

    final VECTOR sp0x10 = new VECTOR().set((int)CPU.MFC2(25), (int)CPU.MFC2(26), (int)CPU.MFC2(27));
    sp0x10.add(matrix_800c3548.transfer);
    a1.set(getProjectionPlaneDistance() * sp0x10.getX() / sp0x10.getZ());
    a2.set(getProjectionPlaneDistance() * sp0x10.getY() / sp0x10.getZ());
    return sp0x10.getZ();
  }

  @Method(0x800cf7d4L)
  public static long FUN_800cf7d4(final SVECTOR a0, final VECTOR a1, final VECTOR a2, final Ref<Long> outX, final Ref<Long> outY) {
    final SVECTOR sp0x30 = new SVECTOR().set((short)a1.getX(), (short)a1.getY(), (short)a1.getZ());
    CPU.CTC2(matrix_800c3548.getPacked(0), 0);
    CPU.CTC2(matrix_800c3548.getPacked(2), 1);
    CPU.CTC2(matrix_800c3548.getPacked(4), 2);
    CPU.CTC2(matrix_800c3548.getPacked(6), 3);
    CPU.CTC2(matrix_800c3548.getPacked(8), 4);
    CPU.MTC2(sp0x30.getXY(), 0);
    CPU.MTC2(sp0x30.getZ(),  1);
    final SVECTOR sp0x28 = new SVECTOR().set(a0);
    CPU.COP2(0x486012L);
    final VECTOR sp0x10 = new VECTOR();
    sp0x10.setX((int)CPU.MFC2(25));
    sp0x10.setY((int)CPU.MFC2(26));
    sp0x10.setZ((int)CPU.MFC2(27));
    CPU.CTC2(matrix_800c3548.getPacked(0), 0);
    CPU.CTC2(matrix_800c3548.getPacked(2), 1);
    CPU.CTC2(matrix_800c3548.getPacked(4), 2);
    CPU.CTC2(matrix_800c3548.getPacked(6), 3);
    CPU.CTC2(matrix_800c3548.getPacked(8), 4);
    CPU.CTC2(matrix_800c3548.transfer.getX(), 5);
    CPU.CTC2(matrix_800c3548.transfer.getY(), 6);
    CPU.CTC2(matrix_800c3548.transfer.getZ(), 7);

    final MATRIX sp0x38 = new MATRIX();
    sp0x38.setPacked(0, CPU.CFC2(0));
    sp0x38.setPacked(2, CPU.CFC2(1));
    sp0x38.setPacked(4, CPU.CFC2(2));
    sp0x38.setPacked(6, CPU.CFC2(3));
    sp0x38.setPacked(8, CPU.CFC2(4));
    sp0x38.transfer.setX((int)CPU.CFC2(5));
    sp0x38.transfer.setY((int)CPU.CFC2(6));
    sp0x38.transfer.setZ((int)CPU.CFC2(7));

    final MATRIX sp0x58 = new MATRIX();
    FUN_80040980(sp0x28, sp0x58);
    CPU.CTC2(sp0x38.getPacked(0), 0); //
    CPU.CTC2(sp0x38.getPacked(2), 1); //
    CPU.CTC2(sp0x38.getPacked(4), 2); // Rotation matrix
    CPU.CTC2(sp0x38.getPacked(6), 3); //
    CPU.CTC2(sp0x38.getPacked(8), 4); //
    CPU.MTC2(sp0x58.get(0),  9); // IR1
    CPU.MTC2(sp0x58.get(3), 10); // IR2
    CPU.MTC2(sp0x58.get(6), 11); // IR3
    CPU.COP2(0x49e012L);
    sp0x38.set(0, (short)CPU.MFC2( 9));
    sp0x38.set(3, (short)CPU.MFC2(10));
    sp0x38.set(6, (short)CPU.MFC2(11));
    CPU.MTC2(sp0x58.get(1),  9);
    CPU.MTC2(sp0x58.get(4), 10);
    CPU.MTC2(sp0x58.get(7), 11);
    CPU.COP2(0x49e012L);
    sp0x38.set(1, (short)CPU.MFC2( 9));
    sp0x38.set(4, (short)CPU.MFC2(10));
    sp0x38.set(7, (short)CPU.MFC2(11));
    CPU.MTC2(sp0x58.get(2),  9);
    CPU.MTC2(sp0x58.get(5), 10);
    CPU.MTC2(sp0x58.get(8), 11);
    CPU.COP2(0x49e012L);
    sp0x38.set(2, (short)CPU.MFC2( 9));
    sp0x38.set(5, (short)CPU.MFC2(10));
    sp0x38.set(8, (short)CPU.MFC2(11));
    sp0x38.transfer.add(sp0x10);
    CPU.CTC2(sp0x38.getPacked(0), 0);
    CPU.CTC2(sp0x38.getPacked(2), 1);
    CPU.CTC2(sp0x38.getPacked(4), 2);
    CPU.CTC2(sp0x38.getPacked(6), 3);
    CPU.CTC2(sp0x38.getPacked(8), 4);
    CPU.CTC2(sp0x38.transfer.getX(), 5);
    CPU.CTC2(sp0x38.transfer.getY(), 6);
    CPU.CTC2(sp0x38.transfer.getZ(), 7);
    CPU.MTC2((a2.getY() & 0xffff) << 16 | a2.getX() & 0xffff, 0);
    CPU.MTC2(a2.getZ(), 1);
    CPU.COP2(0x180001L);

    final DVECTOR sp0x20 = new DVECTOR();
    sp0x20.setXY(CPU.MFC2(14)); // SXY1
    outX.set((long)sp0x20.getX());
    outY.set((long)sp0x20.getY());
    return CPU.MFC2(19); // SZ3
  }

  @Method(0x800cfb14L)
  public static long FUN_800cfb14(final BttlScriptData6c a0, final VECTOR a1, final Ref<Long> a2, final Ref<Long> a3) {
    final SVECTOR sp0x18 = new SVECTOR().set(a0._10.svec_10);
    final VECTOR sp0x20 = new VECTOR().set(a0._10.vec_04);
    return FUN_800cf7d4(sp0x18, sp0x20, a1, a2, a3);
  }

  @Method(0x800cfc20L)
  public static long FUN_800cfc20(final SVECTOR a0, final VECTOR a1, final VECTOR a2, final Ref<Long> outX, final Ref<Long> outY) {
    final SVECTOR sp0x18 = new SVECTOR().set(a0);
    final VECTOR sp0x20 = new VECTOR().set(a1);
    return FUN_800cf7d4(sp0x18, sp0x20, a2, outX, outY);
  }

  @Method(0x800cff54L)
  public static long FUN_800cff54(final long callback, final long count, final long a2, final long a3, final long a4, final long a5, final long a6) {
    final Ref<Long>[] sp0x10 = new Ref[8 + (int)count];
    final long[] sp0x58 = new long[(int)count];
    final long[] sp0x94 = new long[(int)count];
    sp0x94[0] = a3;
    sp0x58[0] = a2;
    sp0x10[8] = new Ref<>(sp0x58[0]);

    //LAB_800cff90
    for(int i = 1; i < count; i++) {
      sp0x58[i] = sp0x94[i];
      sp0x10[8 + i] = new Ref<>(sp0x58[i]);
    }

    //LAB_800cffbc
    MEMORY.ref(4, callback).call((Object)sp0x10);
    return sp0x58[0];
  }
}
