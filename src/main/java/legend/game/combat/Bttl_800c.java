package legend.game.combat;

import legend.core.MathHelper;
import legend.core.Tuple;
import legend.core.gpu.RECT;
import legend.core.gpu.TimHeader;
import legend.core.gte.DVECTOR;
import legend.core.gte.GsCOORDINATE2;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.TmdObjTable;
import legend.core.gte.VECTOR;
import legend.core.memory.Memory;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.CString;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.QuadConsumerRef;
import legend.core.memory.types.QuadFunctionRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.TriConsumerRef;
import legend.core.memory.types.TriFunctionRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.SItem;
import legend.game.Scus94491BpeSegment_8005;
import legend.game.combat.types.BattleCamera;
import legend.game.combat.types.BattleDisplayStats144;
import legend.game.combat.types.BattleLightStruct64;
import legend.game.combat.types.BattleMenuStruct58;
import legend.game.combat.types.BattleObject27c;
import legend.game.combat.types.BattleRenderStruct;
import legend.game.combat.types.BattleScriptDataBase;
import legend.game.combat.types.BattleStruct18cb0;
import legend.game.combat.types.BattleStruct1a8_c;
import legend.game.combat.types.BattleStruct24_2;
import legend.game.combat.types.BattleStruct3c;
import legend.game.combat.types.BattleStruct7cc;
import legend.game.combat.types.BttlLightStruct84;
import legend.game.combat.types.BttlScriptData6cSub0e;
import legend.game.combat.types.BttlScriptData6cSub13c;
import legend.game.combat.types.BttlScriptData6cSub14;
import legend.game.combat.types.BttlScriptData6cSub3c;
import legend.game.combat.types.BttlScriptData6cSub3cSub2c;
import legend.game.combat.types.BttlStruct50;
import legend.game.combat.types.BttlStructa4;
import legend.game.combat.types.CombatantStruct1a8;
import legend.game.combat.types.DeffFile;
import legend.game.combat.types.DragoonSpells09;
import legend.game.combat.types.EffectManagerData6c;
import legend.game.combat.types.MersenneTwisterSeed;
import legend.game.types.BigStruct;
import legend.game.types.CharacterData2c;
import legend.game.types.DR_MODE;
import legend.game.types.ExtendedTmd;
import legend.game.types.GsF_LIGHT;
import legend.game.types.LodString;
import legend.game.types.McqHeader;
import legend.game.types.MrgFile;
import legend.game.types.RunningScript;
import legend.game.types.ScriptFile;
import legend.game.types.ScriptState;
import legend.game.types.SpellStats0c;
import legend.game.types.TmdAnimationFile;

import javax.annotation.Nullable;

import static legend.core.Hardware.CPU;
import static legend.core.Hardware.MEMORY;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.game.Scus94491BpeSegment.FUN_80012444;
import static legend.game.Scus94491BpeSegment.FUN_800127cc;
import static legend.game.Scus94491BpeSegment.FUN_800128a8;
import static legend.game.Scus94491BpeSegment.loadAndRunOverlay;
import static legend.game.Scus94491BpeSegment.FUN_80012bb4;
import static legend.game.Scus94491BpeSegment.FUN_8001324c;
import static legend.game.Scus94491BpeSegment.FUN_80013404;
import static legend.game.Scus94491BpeSegment.FUN_80015704;
import static legend.game.Scus94491BpeSegment.renderMcq;
import static legend.game.Scus94491BpeSegment.FUN_8001ad18;
import static legend.game.Scus94491BpeSegment.FUN_8001af00;
import static legend.game.Scus94491BpeSegment.FUN_8001ff74;
import static legend.game.Scus94491BpeSegment._1f8003c8;
import static legend.game.Scus94491BpeSegment._1f8003f4;
import static legend.game.Scus94491BpeSegment.addToLinkedListTail;
import static legend.game.Scus94491BpeSegment.allocateScriptState;
import static legend.game.Scus94491BpeSegment.centreScreenX_1f8003dc;
import static legend.game.Scus94491BpeSegment.centreScreenY_1f8003de;
import static legend.game.Scus94491BpeSegment.deallocateScriptAndChildren;
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
import static legend.game.Scus94491BpeSegment.setScriptDestructor;
import static legend.game.Scus94491BpeSegment.setWidthAndFlags;
import static legend.game.Scus94491BpeSegment.simpleRand;
import static legend.game.Scus94491BpeSegment.tags_1f8003d0;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020308;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020a00;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020b98;
import static legend.game.Scus94491BpeSegment_8002.FUN_80020fe0;
import static legend.game.Scus94491BpeSegment_8002.FUN_800211d8;
import static legend.game.Scus94491BpeSegment_8002.FUN_800214bc;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021520;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021584;
import static legend.game.Scus94491BpeSegment_8002.FUN_80021868;
import static legend.game.Scus94491BpeSegment_8002.FUN_800218a4;
import static legend.game.Scus94491BpeSegment_8002.FUN_80029e04;
import static legend.game.Scus94491BpeSegment_8002.SquareRoot0;
import static legend.game.Scus94491BpeSegment_8003.ApplyMatrixLV;
import static legend.game.Scus94491BpeSegment_8003.DrawSync;
import static legend.game.Scus94491BpeSegment_8003.GetTPage;
import static legend.game.Scus94491BpeSegment_8003.GsGetLw;
import static legend.game.Scus94491BpeSegment_8003.LoadImage;
import static legend.game.Scus94491BpeSegment_8003.MoveImage;
import static legend.game.Scus94491BpeSegment_8003.SetDrawMode;
import static legend.game.Scus94491BpeSegment_8003.StoreImage;
import static legend.game.Scus94491BpeSegment_8003.TransMatrix;
import static legend.game.Scus94491BpeSegment_8003.bzero;
import static legend.game.Scus94491BpeSegment_8003.getProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8003.parseTimHeader;
import static legend.game.Scus94491BpeSegment_8003.setProjectionPlaneDistance;
import static legend.game.Scus94491BpeSegment_8004.FUN_80040980;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004cd50;
import static legend.game.Scus94491BpeSegment_8004.previousMainCallbackIndex_8004dd28;
import static legend.game.Scus94491BpeSegment_8004.additionCounts_8004f5c0;
import static legend.game.Scus94491BpeSegment_8004.additionOffsets_8004f5ac;
import static legend.game.Scus94491BpeSegment_8004.fileCount_8004ddc8;
import static legend.game.Scus94491BpeSegment_8004.ratan2;
import static legend.game.Scus94491BpeSegment_8005._80052c34;
import static legend.game.Scus94491BpeSegment_8005._8005e398_SCRIPT_SIZES;
import static legend.game.Scus94491BpeSegment_8005._8005f428;
import static legend.game.Scus94491BpeSegment_8005.combatants_8005e398;
import static legend.game.Scus94491BpeSegment_8005.submapCut_80052c30;
import static legend.game.Scus94491BpeSegment_8006._8006e398;
import static legend.game.Scus94491BpeSegment_8006._8006e918;
import static legend.game.Scus94491BpeSegment_8006._8006f1a4;
import static legend.game.Scus94491BpeSegment_8006._8006f1d8;
import static legend.game.Scus94491BpeSegment_8006._8006f1e8;
import static legend.game.Scus94491BpeSegment_8006._8006f244;
import static legend.game.Scus94491BpeSegment_8006._8006f28c;
import static legend.game.Scus94491BpeSegment_8007._8007a3a8;
import static legend.game.Scus94491BpeSegment_8007.joypadPress_8007a398;
import static legend.game.Scus94491BpeSegment_8007.vsyncMode_8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800babc0;
import static legend.game.Scus94491BpeSegment_800b._800bb104;
import static legend.game.Scus94491BpeSegment_800b._800bb168;
import static legend.game.Scus94491BpeSegment_800b._800bc910;
import static legend.game.Scus94491BpeSegment_800b._800bc914;
import static legend.game.Scus94491BpeSegment_800b._800bc918;
import static legend.game.Scus94491BpeSegment_800b._800bc91c;
import static legend.game.Scus94491BpeSegment_800b.goldGainedFromCombat_800bc920;
import static legend.game.Scus94491BpeSegment_800b._800bc928;
import static legend.game.Scus94491BpeSegment_800b._800bc94c;
import static legend.game.Scus94491BpeSegment_800b.totalXpFromCombat_800bc95c;
import static legend.game.Scus94491BpeSegment_800b._800bc960;
import static legend.game.Scus94491BpeSegment_800b._800bc968;
import static legend.game.Scus94491BpeSegment_800b._800bc974;
import static legend.game.Scus94491BpeSegment_800b._800bc978;
import static legend.game.Scus94491BpeSegment_800b._800bc97c;
import static legend.game.Scus94491BpeSegment_800b._800bf0dc;
import static legend.game.Scus94491BpeSegment_800b._800bf0ec;
import static legend.game.Scus94491BpeSegment_800b.encounterId_800bb0f8;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.pregameLoadingStage_800bb10c;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;
import static legend.game.Scus94491BpeSegment_800b.spGained_800bc950;
import static legend.game.Scus94491BpeSegment_800b.submapStage_800bb0f4;
import static legend.game.Scus94491BpeSegment_800b.whichMenu_800bdc38;
import static legend.game.Scus94491BpeSegment_800c.matrix_800c3548;
import static legend.game.combat.Bttl_800d.FUN_800dabec;
import static legend.game.combat.Bttl_800d.FUN_800dd0d4;
import static legend.game.combat.Bttl_800d.FUN_800dd118;
import static legend.game.combat.Bttl_800e.FUN_800e8ffc;
import static legend.game.combat.Bttl_800e.FUN_800e9120;
import static legend.game.combat.Bttl_800e.FUN_800eb9ac;
import static legend.game.combat.Bttl_800e.FUN_800ec4bc;
import static legend.game.combat.Bttl_800e.FUN_800ec4f0;
import static legend.game.combat.Bttl_800e.FUN_800ec51c;
import static legend.game.combat.Bttl_800e.FUN_800ec744;
import static legend.game.combat.Bttl_800e.FUN_800ec8d0;
import static legend.game.combat.Bttl_800e.FUN_800ee610;
import static legend.game.combat.Bttl_800e.FUN_800eeaec;
import static legend.game.combat.Bttl_800e.FUN_800ef9e4;
import static legend.game.combat.Bttl_800e.drawUiElements;
import static legend.game.combat.Bttl_800e.allocateEffectManager;
import static legend.game.combat.Bttl_800e.loadBattleHudDeff_;
import static legend.game.combat.Bttl_800f.FUN_800f1a00;
import static legend.game.combat.Bttl_800f.FUN_800f417c;
import static legend.game.combat.Bttl_800f.FUN_800f4268;
import static legend.game.combat.Bttl_800f.FUN_800f60ac;
import static legend.game.combat.Bttl_800f.FUN_800f6134;
import static legend.game.combat.Bttl_800f.FUN_800f6330;
import static legend.game.combat.Bttl_800f.FUN_800f84c0;
import static legend.game.combat.Bttl_800f.FUN_800f8aa4;
import static legend.game.combat.Bttl_800f.FUN_800f8c38;
import static legend.game.combat.Bttl_800f.loadBattleHudTextures;

public final class Bttl_800c {
  private Bttl_800c() { }

  public static final UnsignedShortRef _800c6690 = MEMORY.ref(2, 0x800c6690L, UnsignedShortRef::new);

  public static final Value _800c6698 = MEMORY.ref(4, 0x800c6698L);
  public static final IntRef _800c669c = MEMORY.ref(4, 0x800c669cL, IntRef::new);
  /** The number of {@link Scus94491BpeSegment_8005#combatants_8005e398}s */
  public static final Value combatantCount_800c66a0 = MEMORY.ref(4, 0x800c66a0L);
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
  public static final IntRef _800c66d0 = MEMORY.ref(4, 0x800c66d0L, IntRef::new);
  public static final Value _800c66d4 = MEMORY.ref(1, 0x800c66d4L);

  public static final Value _800c66d8 = MEMORY.ref(4, 0x800c66d8L);

  public static final Pointer<ScriptFile> script_800c66fc = MEMORY.ref(4, 0x800c66fcL, Pointer.deferred(4, ScriptFile::new));
  public static int script_800c66fc_length;

  public static final Pointer<ScriptFile> script_800c670c = MEMORY.ref(4, 0x800c670cL, Pointer.deferred(4, ScriptFile::new));

  public static final Value _800c6718 = MEMORY.ref(4, 0x800c6718L);

  public static final Value _800c6724 = MEMORY.ref(4, 0x800c6724L);

  public static final Value _800c6740 = MEMORY.ref(4, 0x800c6740L);

  public static final Value _800c6748 = MEMORY.ref(4, 0x800c6748L);
  public static final Value scriptIndex_800c674c = MEMORY.ref(4, 0x800c674cL);

  public static final Value _800c6754 = MEMORY.ref(4, 0x800c6754L);
  public static final Value _800c6758 = MEMORY.ref(4, 0x800c6758L);
  public static final Value _800c675c = MEMORY.ref(4, 0x800c675cL);
  public static final Value _800c6760 = MEMORY.ref(4, 0x800c6760L);
  public static final Value _800c6764 = MEMORY.ref(4, 0x800c6764L);
  public static final IntRef _800c6768 = MEMORY.ref(4, 0x800c6768L, IntRef::new);
  public static final Value _800c676c = MEMORY.ref(4, 0x800c676cL);
  public static final Value _800c6770 = MEMORY.ref(4, 0x800c6770L);
  public static final Value _800c6774 = MEMORY.ref(4, 0x800c6774L);
  public static final Value _800c6778 = MEMORY.ref(4, 0x800c6778L);
  /** The number of player chars in combat (i.e. 1-3) */
  public static final IntRef charCount_800c677c = MEMORY.ref(4, 0x800c677cL, IntRef::new);
  public static final Value _800c6780 = MEMORY.ref(4, 0x800c6780L);

  public static final Value _800c6790 = MEMORY.ref(4, 0x800c6790L);

  public static final MATRIX _800c6798 = MEMORY.ref(4, 0x800c6798L, MATRIX::new);
  public static final UnsignedIntRef _800c67b8 = MEMORY.ref(4, 0x800c67b8L, UnsignedIntRef::new);
  public static final Value x_800c67bc = MEMORY.ref(4, 0x800c67bcL);
  public static final Value y_800c67c0 = MEMORY.ref(4, 0x800c67c0L);
  public static final Value _800c67c4 = MEMORY.ref(4, 0x800c67c4L);

  public static final Value _800c67d4 = MEMORY.ref(4, 0x800c67d4L);
  public static final VECTOR _800c67d8 = MEMORY.ref(4, 0x800c67d8L, VECTOR::new);
  public static final Value _800c67e4 = MEMORY.ref(4, 0x800c67e4L);
  public static final Value _800c67e8 = MEMORY.ref(4, 0x800c67e8L);

  public static final BattleCamera camera_800c67f0 = MEMORY.ref(4, 0x800c67f0L, BattleCamera::new);

  public static final Value callbackIndex_800c6878 = MEMORY.ref(4, 0x800c6878L);

  public static final Value callbackIndex_800c68ec = MEMORY.ref(4, 0x800c68ecL);

  public static final Value _800c6912 = MEMORY.ref(1, 0x800c6912L);
  public static final Value _800c6913 = MEMORY.ref(1, 0x800c6913L);
  public static final Value _800c6914 = MEMORY.ref(4, 0x800c6914L);

  public static final Pointer<BttlStruct50> _800c6920 = MEMORY.ref(4, 0x800c6920L, Pointer.deferred(4, BttlStruct50::new));

  public static final Value _800c6928 = MEMORY.ref(4, 0x800c6928L);
  public static final Pointer<ArrayRef<BttlLightStruct84>> lights_800c692c = MEMORY.ref(4, 0x800c692cL, Pointer.deferred(4, ArrayRef.of(BttlLightStruct84.class, 3, 0x84, BttlLightStruct84::new)));
  public static final Pointer<BattleLightStruct64> _800c6930 = MEMORY.ref(4, 0x800c6930L, Pointer.deferred(4, BattleLightStruct64::new));

  public static final Pointer<BattleStruct24_2> _800c6938 = MEMORY.ref(4, 0x800c6938L, Pointer.deferred(4, BattleStruct24_2::new));
  public static final Pointer<BattleStruct7cc> struct7cc_800c693c = MEMORY.ref(4, 0x800c693cL, Pointer.deferred(4, BattleStruct7cc::new));
  public static final Value _800c6940 = MEMORY.ref(4, 0x800c6940L);
  public static final Value _800c6944 = MEMORY.ref(4, 0x800c6944L);
  public static final Value _800c6948 = MEMORY.ref(4, 0x800c6948L);

  public static final Pointer<DeffFile> deff_800c6950 = MEMORY.ref(4, 0x800c6950L, Pointer.deferred(4, DeffFile::new));

  public static final Value _800c6958 = MEMORY.ref(4, 0x800c6958L);
  public static final Value _800c695c = MEMORY.ref(2, 0x800c695cL);

  public static final ArrayRef<DragoonSpells09> dragoonSpells_800c6960 = MEMORY.ref(1, 0x800c6960L, ArrayRef.of(DragoonSpells09.class, 3, 9, DragoonSpells09::new));

  public static final Value _800c697c = MEMORY.ref(2, 0x800c697cL);
  public static final Value _800c697e = MEMORY.ref(2, 0x800c697eL);
  public static final Value _800c6980 = MEMORY.ref(2, 0x800c6980L);

  public static final Value _800c6988 = MEMORY.ref(1, 0x800c6988L);

  public static final Value _800c69c8 = MEMORY.ref(4, 0x800c69c8L);

  public static final ArrayRef<LodString> currentEnemyNames_800c69d0 = MEMORY.ref(2, 0x800c69d0L, ArrayRef.of(LodString.class, 9, 0x2c, LodString::new));

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

  /** One per character slot */
  public static final Pointer<ArrayRef<BattleDisplayStats144>> displayStats_800c6c2c = MEMORY.ref(4, 0x800c6c2cL, Pointer.deferred(4, ArrayRef.of(BattleDisplayStats144.class, 3, 0x144, BattleDisplayStats144::new)));
  public static final Value _800c6c30 = MEMORY.ref(1, 0x800c6c30L);

  public static final Pointer<BattleMenuStruct58> battleMenu_800c6c34 = MEMORY.ref(4, 0x800c6c34L, Pointer.deferred(4, BattleMenuStruct58::new));
  public static final Value _800c6c38 = MEMORY.ref(4, 0x800c6c38L);
  public static final UnsignedShortRef usedRepeatItems_800c6c3c = MEMORY.ref(2, 0x800c6c3cL, UnsignedShortRef::new);

  public static final ArrayRef<BattleStruct3c> _800c6c40 = MEMORY.ref(2, 0x800c6c40L, ArrayRef.of(BattleStruct3c.class, 3, 0x3c, BattleStruct3c::new));

  public static final Value _800c6cf4 = MEMORY.ref(4, 0x800c6cf4L);

  public static final Value _800c6d94 = MEMORY.ref(2, 0x800c6d94L);
  public static final Value _800c6dac = MEMORY.ref(2, 0x800c6dacL);

  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800d1220}</li>
   *   <li>{@link Bttl_800d#FUN_800d15d8}</li>
   *   <li>{@link Bttl_800d#FUN_800d15d8}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<TriConsumerRef<Integer, ScriptState<EffectManagerData6c>, EffectManagerData6c>>> _800c6dc4 = MEMORY.ref(4, 0x800c6dc4L, ArrayRef.of(Pointer.classFor(TriConsumerRef.classFor(int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class)), 3, 4, Pointer.deferred(4, TriConsumerRef::new)));

  public static final GsF_LIGHT light_800c6ddc = MEMORY.ref(4, 0x800c6ddcL, GsF_LIGHT::new);

  public static final CString _800c6e18 = MEMORY.ref(7, 0x800c6e18L, CString::new);

  public static final ArrayRef<UnsignedShortRef> repeatItemIds_800c6e34 = MEMORY.ref(2, 0x800c6e34L, ArrayRef.of(UnsignedShortRef.class, 9, 2, UnsignedShortRef::new));

  public static final Value _800c6e48 = MEMORY.ref(2, 0x800c6e48L);
  public static final Value _800c6e60 = MEMORY.ref(2, 0x800c6e60L);

  public static final ArrayRef<UnsignedIntRef> characterDragoonIndices_800c6e68 = MEMORY.ref(4, 0x800c6e68L, ArrayRef.of(UnsignedIntRef.class, 10, 4, UnsignedIntRef::new));

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

  public static final ArrayRef<UnsignedShortRef> characterElements_800c706c = MEMORY.ref(2, 0x800c706cL, ArrayRef.of(UnsignedShortRef.class, 10, 2, UnsignedShortRef::new));

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
  public static final ArrayRef<ShortRef> _800c7194 = MEMORY.ref(2, 0x800c7194L, ArrayRef.of(ShortRef.class, 8, 2, ShortRef::new));

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

  public static final ArrayRef<UnsignedShortRef> _800c72cc = MEMORY.ref(2, 0x800c72ccL, ArrayRef.of(UnsignedShortRef.class, 10, 2, UnsignedShortRef::new));

  public static final Value _800d66b0 = MEMORY.ref(1, 0x800d66b0L);

  public static final Value _800d6c30 = MEMORY.ref(1, 0x800d6c30L);

  public static final ArrayRef<SpellStats0c> spellStats_800fa0b8 = MEMORY.ref(1, 0x800fa0b8L, ArrayRef.of(SpellStats0c.class, 128, 0xc, SpellStats0c::new));

  public static final Value _800fa6c4 = MEMORY.ref(2, 0x800fa6c4L);

  public static final Value _800fa6b8 = MEMORY.ref(2, 0x800fa6b8L);

  public static final Value _800fa6d0 = MEMORY.ref(2, 0x800fa6d0L);

  public static final Value _800fa6d4 = MEMORY.ref(2, 0x800fa6d4L);

  public static final Value _800fa6dc = MEMORY.ref(4, 0x800fa6dcL);
  public static final UnboundedArrayRef<RECT> _800fa6e0 = MEMORY.ref(2, 0x800fa6e0L, UnboundedArrayRef.of(0x8, RECT::new));

  public static final Value _800fa730 = MEMORY.ref(2, 0x800fa730L);

  public static final ArrayRef<UnsignedShortRef> additionNextLevelXp_800fa744 = MEMORY.ref(2, 0x800fa744L, ArrayRef.of(UnsignedShortRef.class, 5, 2, UnsignedShortRef::new));

  /** Mersenne Twister seed */
  public static final MersenneTwisterSeed seed_800fa754 = MEMORY.ref(4, 0x800fa754L, MersenneTwisterSeed::new);
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800d1d3c}</li>
   *   <li>{@link Bttl_800d#FUN_800d1e80}</li>
   *   <li>{@link Bttl_800d#FUN_800d21b8}</li>
   *   <li>{@link Bttl_800d#FUN_800d1d3c}</li>
   *   <li>{@link Bttl_800d#FUN_800d21b8}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<QuadConsumerRef<EffectManagerData6c, Long, long[], BttlScriptData6cSub14>>> effectRenderers_800fa758 = MEMORY.ref(4, 0x800fa758L, ArrayRef.of(Pointer.classFor(QuadConsumerRef.classFor(EffectManagerData6c.class, long.class, long[].class, BttlScriptData6cSub14.class)), 5, 4, Pointer.deferred(4, QuadConsumerRef::new)));

  public static final Value _800fa76c = MEMORY.ref(4, 0x800fa76cL);

  /** ASCII chars - [0-9][A-Z][a-z]'-& <null> */
  public static final Value _800fa788 = MEMORY.ref(1, 0x800fa788L);
  public static final Value _800fa7cc = MEMORY.ref(4, 0x800fa7ccL);

  public static final Value additionNames_800fa8d4 = MEMORY.ref(4, 0x800fa8d4L);

  public static final Value _800faa90 = MEMORY.ref(2, 0x800faa90L);
  public static final Value _800faa92 = MEMORY.ref(2, 0x800faa92L);
  public static final Value _800faa94 = MEMORY.ref(1, 0x800faa94L);

  public static final Value _800faa98 = MEMORY.ref(4, 0x800faa98L);
  public static final Value _800faa9c = MEMORY.ref(1, 0x800faa9cL);
  public static final Value _800faa9d = MEMORY.ref(1, 0x800faa9dL);

  public static final Value _800faaa0 = MEMORY.ref(4, 0x800faaa0L);

  public static final SVECTOR _800fab98 = MEMORY.ref(2, 0x800fab98L, SVECTOR::new);
  public static final SVECTOR _800faba0 = MEMORY.ref(2, 0x800faba0L, SVECTOR::new);
  public static final VECTOR _800faba8 = MEMORY.ref(4, 0x800faba8L, VECTOR::new);

  public static final Value _800fabb8 = MEMORY.ref(1, 0x800fabb8L);

  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800dacc4}</li>
   *   <li>{@link Bttl_800d#FUN_800dad14}</li>
   *   <li>{@link Bttl_800d#FUN_800dadc0}</li>
   *   <li>{@link Bttl_800d#FUN_800dadc8}</li>
   *   <li>{@link Bttl_800d#FUN_800dadd0}</li>
   *   <li>{@link Bttl_800d#FUN_800dae3c}</li>
   *   <li>{@link Bttl_800d#FUN_800daedc}</li>
   *   <li>{@link Bttl_800d#FUN_800daf6c}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<QuadConsumerRef<Integer, Integer, Integer, Integer>>> _800fabbc = MEMORY.ref(4, 0x800fabbcL, ArrayRef.of(Pointer.classFor(QuadConsumerRef.classFor(Integer.class, Integer.class, Integer.class, Integer.class)), 8, 4, Pointer.deferred(4, QuadConsumerRef::new)));
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800db0d8}</li>
   *   <li>{@link Bttl_800d#FUN_800db128}</li>
   *   <li>{@link Bttl_800d#FUN_800db1d4}</li>
   *   <li>{@link Bttl_800d#FUN_800db240}</li>
   *   <li>{@link Bttl_800d#FUN_800db2e0}</li>
   *   <li>{@link Bttl_800d#FUN_800db2e8}</li>
   *   <li>{@link Bttl_800d#FUN_800db2f0}</li>
   *   <li>{@link Bttl_800d#FUN_800db398}</li>
   * </ol>
   */
  public static final ArrayRef<Pointer<QuadConsumerRef<Integer, Integer, Integer, Integer>>> _800fabdc = MEMORY.ref(4, 0x800fabdcL, ArrayRef.of(Pointer.classFor(QuadConsumerRef.classFor(Integer.class, Integer.class, Integer.class, Integer.class)), 8, 4, Pointer.deferred(4, QuadConsumerRef::new)));
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800d47dc}</li>
   *   <li>{@link Bttl_800d#FUN_800d496c}</li>
   *   <li>{@link Bttl_800d#FUN_800db564}</li>
   *   <li>{@link Bttl_800d#FUN_800db56c}</li>
   *   <li>{@link Bttl_800d#FUN_800d4bac}</li>
   *   <li>{@link Bttl_800d#FUN_800d4d7c}</li>
   *   <li>{@link Bttl_800d#FUN_800d4fbc}</li>
   *   <li>{@link Bttl_800d#FUN_800d519c}</li>
   * </ol>
   */
  public static final Value _800fabfc = MEMORY.ref(4, 0x800fabfcL);
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800d53e4}</li>
   *   <li>{@link Bttl_800d#FUN_800d5574}</li>
   *   <li>{@link Bttl_800d#FUN_800db78c}</li>
   *   <li>{@link Bttl_800d#FUN_800db794}</li>
   *   <li>{@link Bttl_800d#FUN_800d5740}</li>
   *   <li>{@link Bttl_800d#FUN_800d5930}</li>
   *   <li>{@link Bttl_800d#FUN_800d5afc}</li>
   *   <li>{@link Bttl_800d#FUN_800d5cf4}</li>
   * </ol>
   */
  public static final Value _800fac1c = MEMORY.ref(4, 0x800fac1cL);
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800d5ec8}</li>
   *   <li>{@link Bttl_800d#FUN_800d60b0}</li>
   *   <li>{@link Bttl_800d#FUN_800db9d0}</li>
   *   <li>{@link Bttl_800d#FUN_800db9d8}</li>
   *   <li>{@link Bttl_800d#FUN_800d62d8}</li>
   *   <li>{@link Bttl_800d#FUN_800d64e4}</li>
   *   <li>{@link Bttl_800d#FUN_800d670c}</li>
   *   <li>{@link Bttl_800d#FUN_800d6960}</li>
   * </ol>
   */
  public static final Value _800fac3c = MEMORY.ref(4, 0x800fac3cL);
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800d6b90}</li>
   *   <li>{@link Bttl_800d#FUN_800d6d18}</li>
   *   <li>{@link Bttl_800d#FUN_800d6f58}</li>
   *   <li>{@link Bttl_800d#FUN_800d7128}</li>
   *   <li>{@link Bttl_800d#FUN_800db678}</li>
   *   <li>{@link Bttl_800d#FUN_800db680}</li>
   *   <li>{@link Bttl_800d#FUN_800d7368}</li>
   *   <li>{@link Bttl_800d#FUN_800d7548}</li>
   * </ol>
   */
  public static final Value _800fac5c = MEMORY.ref(4, 0x800fac5cL);
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800d7790}</li>
   *   <li>{@link Bttl_800d#FUN_800d7920}</li>
   *   <li>{@link Bttl_800d#FUN_800d7aec}</li>
   *   <li>{@link Bttl_800d#FUN_800d7cdc}</li>
   *   <li>{@link Bttl_800d#FUN_800db8a0}</li>
   *   <li>{@link Bttl_800d#FUN_800db8a8}</li>
   *   <li>{@link Bttl_800d#FUN_800d7ea8}</li>
   *   <li>{@link Bttl_800d#FUN_800d80a0}</li>
   * </ol>
   */
  public static final Value _800fac7c = MEMORY.ref(4, 0x800fac7cL);
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800d8274}</li>
   *   <li>{@link Bttl_800d#FUN_800d8424}</li>
   *   <li>{@link Bttl_800d#FUN_800d8614}</li>
   *   <li>{@link Bttl_800d#FUN_800d8808}</li>
   *   <li>{@link Bttl_800d#FUN_800dbb00}</li>
   *   <li>{@link Bttl_800d#FUN_800dbb08}</li>
   *   <li>{@link Bttl_800d#FUN_800d89f8}</li>
   *   <li>{@link Bttl_800d#FUN_800d8bf4}</li>
   * </ol>
   */
  public static final Value _800fac9c = MEMORY.ref(4, 0x800fac9cL);
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800dbe40}</li>
   *   <li>{@link Bttl_800d#FUN_800dbe60}</li>
   *   <li>{@link Bttl_800d#FUN_800dbe80}</li>
   *   <li>{@link Bttl_800d#FUN_800dbe8c}</li>
   *   <li>{@link Bttl_800d#FUN_800dbe98}</li>
   *   <li>{@link Bttl_800d#FUN_800dbef0}</li>
   *   <li>{@link Bttl_800d#FUN_800dbf70}</li>
   *   <li>{@link Bttl_800d#FUN_800dbfd4}</li>
   *   <li>{@link Bttl_800d#FUN_800d90c8}</li>
   *   <li>{@link Bttl_800d#FUN_800d9154}</li>
   *   <li>{@link Bttl_800d#FUN_800dc070}</li>
   *   <li>{@link Bttl_800d#FUN_800dc078}</li>
   *   <li>{@link Bttl_800d#FUN_800d9220}</li>
   *   <li>{@link Bttl_800d#FUN_800d92bc}</li>
   *   <li>{@link Bttl_800d#FUN_800d9380}</li>
   *   <li>{@link Bttl_800d#FUN_800d9438}</li>
   *   <li>{@link Bttl_800d#FUN_800d9518}</li>
   *   <li>{@link Bttl_800d#FUN_800d9650}</li>
   *   <li>{@link Bttl_800d#FUN_800dc080}</li>
   *   <li>{@link Bttl_800d#FUN_800dc088}</li>
   *   <li>{@link Bttl_800d#FUN_800d9788}</li>
   *   <li>{@link Bttl_800d#FUN_800d98d0}</li>
   *   <li>{@link Bttl_800d#FUN_800d9a68}</li>
   *   <li>{@link Bttl_800d#FUN_800d9bd4}</li>
   * </ol>
   */
  public static final Value _800facbc = MEMORY.ref(4, 0x800facbcL);
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800dc090}</li>
   *   <li>{@link Bttl_800d#FUN_800dc0b0}</li>
   *   <li>{@link Bttl_800d#FUN_800dc0d0}</li>
   *   <li>{@link Bttl_800d#FUN_800dc128}</li>
   *   <li>{@link Bttl_800d#FUN_800dc1a8}</li>
   *   <li>{@link Bttl_800d#FUN_800dc1b0}</li>
   *   <li>{@link Bttl_800d#FUN_800dc1b8}</li>
   *   <li>{@link Bttl_800d#FUN_800dc21c}</li>
   *   <li>{@link Bttl_800d#FUN_800d9da0}</li>
   *   <li>{@link Bttl_800d#FUN_800d9e2c}</li>
   *   <li>{@link Bttl_800d#FUN_800d9ef8}</li>
   *   <li>{@link Bttl_800d#FUN_800d9f94}</li>
   *   <li>{@link Bttl_800d#FUN_800dc2b8}</li>
   *   <li>{@link Bttl_800d#FUN_800dc2c0}</li>
   *   <li>{@link Bttl_800d#FUN_800da058}</li>
   *   <li>{@link Bttl_800d#FUN_800da110}</li>
   *   <li>{@link Bttl_800d#FUN_800da1f0}</li>
   *   <li>{@link Bttl_800d#FUN_800da328}</li>
   *   <li>{@link Bttl_800d#FUN_800da460}</li>
   *   <li>{@link Bttl_800d#FUN_800da5b0}</li>
   *   <li>{@link Bttl_800d#FUN_800dc2c8}</li>
   *   <li>{@link Bttl_800d#FUN_800dc2d0}</li>
   *   <li>{@link Bttl_800d#FUN_800da750}</li>
   *   <li>{@link Bttl_800d#FUN_800da8bc}</li>
   * </ol>
   */
  public static final Value _800fad1c = MEMORY.ref(4, 0x800fad1cL);
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800dc408}</li>
   *   <li>{@link Bttl_800d#FUN_800dc45c}</li>
   *   <li>{@link Bttl_800d#FUN_800dc504}</li>
   *   <li>{@link Bttl_800d#FUN_800dc50c}</li>
   *   <li>{@link Bttl_800d#FUN_800dc514}</li>
   *   <li>{@link Bttl_800d#FUN_800dc580}</li>
   *   <li>{@link Bttl_800d#FUN_800dc630}</li>
   *   <li>{@link Bttl_800d#FUN_800dc6d8}</li>
   * </ol>
   */
  public static final Value _800fad7c = MEMORY.ref(4, 0x800fad7cL);
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800d#FUN_800dc798}</li>
   *   <li>{@link Bttl_800d#FUN_800dc7ec}</li>
   *   <li>{@link Bttl_800d#FUN_800dc894}</li>
   *   <li>{@link Bttl_800d#FUN_800dc900}</li>
   *   <li>{@link Bttl_800d#FUN_800dc9b0}</li>
   *   <li>{@link Bttl_800d#FUN_800dc9b8}</li>
   *   <li>{@link Bttl_800d#FUN_800dc9c0}</li>
   *   <li>{@link Bttl_800d#FUN_800dca68}</li>
   * </ol>
   */
  public static final Value _800fad9c = MEMORY.ref(4, 0x800fad9cL);
  /**
   * <ol start="0">
   *   <li>{@link Bttl_800e#FUN_800e3f88}</li>
   *   <li>{@link Bttl_800d#FUN_800df130}</li>
   *   <li>{@link Bttl_800e#FUN_800e3f88}</li>
   *   <li>{@link Bttl_800d#FUN_800df130}</li>
   *   <li>{@link Bttl_800e#FUN_800e4184}</li>
   *   <li>{@link Bttl_800d#FUN_800df6f0}</li>
   *   <li>{@link Bttl_800e#FUN_800e4184}</li>
   *   <li>{@link Bttl_800d#FUN_800df6f0}</li>
   *   <li>{@link Bttl_800d#FUN_800de9bc}</li>
   *   <li>{@link Bttl_800d#FUN_800dee8c}</li>
   *   <li>{@link Bttl_800d#FUN_800de9bc}</li>
   *   <li>{@link Bttl_800d#FUN_800dee8c}</li>
   *   <li>{@link Bttl_800d#FUN_800dec14}</li>
   *   <li>{@link Bttl_800d#FUN_800df370}</li>
   *   <li>{@link Bttl_800d#FUN_800dec14}</li>
   *   <li>{@link Bttl_800d#FUN_800df370}</li>
   *   <li>?</li>
   *   <li>?</li>
   *   <li>{@link Bttl_800e#FUN_800e3f88}</li>
   *   <li>{@link Bttl_800d#FUN_800df130}</li>
   *   <li>?</li>
   *   <li>?</li>
   *   <li>{@link Bttl_800e#FUN_800e43a8}</li>
   *   <li>{@link Bttl_800d#FUN_800dffe4}</li>
   *   <li>?</li>
   *   <li>?</li>
   *   <li>{@link Bttl_800d#FUN_800de9bc}</li>
   *   <li>{@link Bttl_800d#FUN_800dee8c}</li>
   *   <li>?</li>
   *   <li>?</li>
   *   <li>{@link Bttl_800d#FUN_800df9e8}</li>
   *   <li>{@link Bttl_800d#FUN_800dfc5c}</li>
   *   <li>{@link Bttl_800e#FUN_800e0848}</li>
   *   <li>{@link Bttl_800e#FUN_800e1c24}</li>
   *   <li>{@link Bttl_800e#FUN_800e0848}</li>
   *   <li>{@link Bttl_800e#FUN_800e1c24}</li>
   *   <li>{@link Bttl_800e#FUN_800e121c}</li>
   *   <li>{@link Bttl_800e#FUN_800e2620}</li>
   *   <li>{@link Bttl_800e#FUN_800e121c}</li>
   *   <li>{@link Bttl_800e#FUN_800e2620}</li>
   *   <li>{@link Bttl_800e#FUN_800e02e8}</li>
   *   <li>{@link Bttl_800e#FUN_800e16a0}</li>
   *   <li>{@link Bttl_800e#FUN_800e02e8}</li>
   *   <li>{@link Bttl_800e#FUN_800e16a0}</li>
   *   <li>{@link Bttl_800e#FUN_800e0c98}</li>
   *   <li>{@link Bttl_800e#FUN_800e20bc}</li>
   *   <li>{@link Bttl_800e#FUN_800e0c98}</li>
   *   <li>{@link Bttl_800e#FUN_800e20bc}</li>
   *   <li>?</li>
   *   <li>?</li>
   *   <li>{@link Bttl_800e#FUN_800e0848}</li>
   *   <li>{@link Bttl_800e#FUN_800e1c24}</li>
   *   <li>?</li>
   *   <li>?</li>
   *   <li>{@link Bttl_800e#FUN_800e300c}</li>
   *   <li>{@link Bttl_800e#FUN_800e39e8}</li>
   *   <li>?</li>
   *   <li>?</li>
   *   <li>{@link Bttl_800e#FUN_800e02e8}</li>
   *   <li>{@link Bttl_800e#FUN_800e16a0}</li>
   *   <li>?</li>
   *   <li>?</li>
   *   <li>{@link Bttl_800e#FUN_800e2a98}</li>
   *   <li>{@link Bttl_800e#FUN_800e3478}</li>
   * </ol>
   *
   * Note: blank lines are probably impossible combinations
   */
  public static final ArrayRef<Pointer<QuadFunctionRef<Long, UnboundedArrayRef<SVECTOR>, Long, Long, Long>>> ctmdRenderers_800fadbc = MEMORY.ref(4, 0x800fadbcL, ArrayRef.of(Pointer.classFor(QuadFunctionRef.classFor(Long.class, UnboundedArrayRef.classFor(SVECTOR.class), Long.class, Long.class, Long.class)), 0x40, 4, Pointer.deferred(4, QuadFunctionRef::new)));

  public static final ScriptFile script_800faebc = MEMORY.ref(4, 0x800faebcL, ScriptFile::new);

  public static final Value _800faec4 = MEMORY.ref(2, 0x800faec4L);

  public static final Value _800fafe8 = MEMORY.ref(4, 0x800fafe8L);
  public static final Value _800fafec = MEMORY.ref(1, 0x800fafecL);
  public static final Value _800fb040 = MEMORY.ref(1, 0x800fb040L);

  public static final Value _800fb05c = MEMORY.ref(1, 0x800fb05cL);

  public static final Value stageIndices_800fb064 = MEMORY.ref(1, 0x800fb064L);

  public static final Value _800fb06c = MEMORY.ref(1, 0x800fb06cL);

  public static final Value _800fb0ec = MEMORY.ref(4, 0x800fb0ecL);

  public static final ArrayRef<UnsignedByteRef> _800fb148 = MEMORY.ref(1, 0x800fb148L, ArrayRef.of(UnsignedByteRef.class, 0x40, 1, UnsignedByteRef::new));

  /** TODO array of unsigned shorts */
  public static final Value _800fb188 = MEMORY.ref(2, 0x800fb188L);

  /** TODO array of unsigned shorts */
  public static final Value _800fb198 = MEMORY.ref(2, 0x800fb198L);

  /** Targeting ("All allies", "All players", "All") */
  public static final ArrayRef<Pointer<LodString>> targeting_800fb36c = MEMORY.ref(4, 0x800fb36cL, ArrayRef.of(Pointer.classFor(LodString.class),  3, 4, Pointer.deferred(4, LodString::new)));
  public static final ArrayRef<Pointer<LodString>> playerNames_800fb378 = MEMORY.ref(4, 0x800fb378L, ArrayRef.of(Pointer.classFor(LodString.class), 11, 4, Pointer.deferred(4, LodString::new)));
  /** Poisoned, etc */
  public static final ArrayRef<Pointer<LodString>> ailments_800fb3a0 = MEMORY.ref(4, 0x800fb3a0L, ArrayRef.of(Pointer.classFor(LodString.class),  7, 4, Pointer.deferred(4, LodString::new)));

  /** Player names, player names, item names, dragoon spells, item descriptions, spell descriptions */
  public static final ArrayRef<Pointer<UnboundedArrayRef<Pointer<LodString>>>> allText_800fb3c0 = MEMORY.ref(4, 0x800fb3c0L, ArrayRef.of(Pointer.classFor(UnboundedArrayRef.classFor(Pointer.classFor(LodString.class))),  6, 4, Pointer.deferred(4, UnboundedArrayRef.of(4, Pointer.deferred(4, LodString::new)))));

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

  public static final Value _800fb6bc = MEMORY.ref(2, 0x800fb6bcL);

  public static final Value _800fb6f4 = MEMORY.ref(1, 0x800fb6f4L);

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
    _800c669c.set(a1);

    //LAB_800c73b0
    for(a0 = 0, a1 = 0; a0 < charCount_800c677c.get(); a0++) {
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
  public static int FUN_800c7488(final int charSlot, final long a1, final long a2) {
    if((scriptStatePtrArr_800bc1c0.get(_8006e398.charBobjIndices_e40.get(charSlot).get()).deref().ui_60.get() & 0x2L) != 0) {
      final long a0_0 = _1f8003f4.getPointer() + (charSlot + 0x3L) * 0x100L + a1 * 0x20L + a2 * 0x2L; //TODO
      return (int)MEMORY.ref(2, a0_0).offset(0x38L).getSigned();
    }

    //LAB_800c74fc
    final long a0_0 = _1f8003f4.getPointer() + charSlot * 0x100L + a1 * 0x20L + a2 * 0x2L; //TODO
    return (int)MEMORY.ref(2, a0_0).offset(0x38L).getSigned();
  }

  @Method(0x800c7524L)
  public static void FUN_800c7524() {
    FUN_800c8624();

    gameState_800babc8._b4.incr();
    _800bc910.setu(0);
    _800bc914.setu(0);
    _800bc918.setu(0);
    goldGainedFromCombat_800bc920.set(0);

    spGained_800bc950.get(0).set(0);
    spGained_800bc950.get(1).set(0);
    spGained_800bc950.get(2).set(0);

    totalXpFromCombat_800bc95c.set(0);
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
    loadAndRunOverlay(1, getMethodAddress(SBtld.class, "FUN_80109050", long.class), 0);
    pregameLoadingStage_800bb10c.addu(0x1L);
  }

  @Method(0x800c76a0L)
  public static void FUN_800c76a0() {
    if((_800bc960.get() & 0x3L) == 0x3L) {
      setWidthAndFlags(320, 0);
      FUN_8001324c(0xcL);
      vsyncMode_8007a3b8.setu(0x3L);
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
      _8006e398._d8c.get(i).used_04.set(false);
    }

    //LAB_800c7770
    for(int i = 0; i < 0x100; i++) {
      _8006e398._180.get(i).set(0);
    }

    FUN_800c8e48();

    _800bc94c.setu(0x1L);

    scriptStartEffect(0x4L, 0x1eL);

    _800bc960.oru(0x20L);
    _8006e398._eec.set(0);

    FUN_800ca980();
    FUN_800c8ee4();
    FUN_800cae44();

    _800c66d0.set(0);
    _800c6768.set(0);
    charCount_800c677c.set(0);

    _8006e398._ee4.set(gameState_800babc8.morphMode_4e2.get());

    loadAndRunOverlay(1, getMethodAddress(SBtld.class, "FUN_80109250", long.class), 0);

    //LAB_800c7830
    for(int i = 0; i < 12; i++) {
      _8006e398.bobjIndices_e0c.get(i).set(-1);
    }

    FUN_800ee610();
    FUN_800f84c0();
    FUN_800f60ac();
    FUN_800e8ffc();

    pregameLoadingStage_800bb10c.addu(0x1L);
  }

  @Method(0x800c788cL)
  public static void deferAllocateEnemyBattleObjects() {
    loadAndRunOverlay(1, getMethodAddress(SBtld.class, "allocateEnemyBattleObjects", long.class), 0);
    pregameLoadingStage_800bb10c.addu(0x1L);
  }

  @Method(0x800c78d4L)
  public static void deferAllocatePlayerBattleObjects() {
    loadAndRunOverlay(2, getMethodAddress(SItem.class, "allocatePlayerBattleObjects", long.class), 0);
    pregameLoadingStage_800bb10c.addu(0x1L);
  }

  @Method(0x800c791cL)
  public static void deferLoadEncounterAssets() {
    loadAndRunOverlay(2, getMethodAddress(SItem.class, "loadEncounterAssets", long.class), 0);
    pregameLoadingStage_800bb10c.addu(0x1L);
  }

  @Method(0x800c7964L)
  public static void FUN_800c7964() {
    _800bc960.oru(0xcL);

    loadBattleHudTextures();
    loadBattleHudDeff_();

    //LAB_800c79a8
    for(int index = 0; index < combatantCount_800c66a0.get(); index++) {
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
  public static void deferDoNothing() {
    loadAndRunOverlay(3, getMethodAddress(Bttl_800c.class, "doNothing", long.class), 0);
    pregameLoadingStage_800bb10c.addu(0x1L);
  }

  @Method(0x800c7a80L)
  public static void FUN_800c7a80() {
    if(_800c66a8.get() != 0) {
      _800bc960.oru(0x10L);

      //LAB_800c7ae4
      for(int i = 0; i < _800c66d0.get(); i++) {
        final ScriptState<?> v0 = scriptStatePtrArr_800bc1c0.get((int)_8006f1a4.offset(i * 0x4L).get()).deref();
        final BattleObject27c s1 = v0.innerStruct_00.derefAs(BattleObject27c.class);

        if((v0.ui_60.get() & 0x4L) != 0) {
          s1.turnValue_4c.set((short)(simpleRand() * 0xd9 / 0x10000));
        } else {
          //LAB_800c7b3c
          s1.turnValue_4c.set((short)(simpleRand() * 0xa7 / 0x10000 + 0x32));
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
  public static void doNothing(final long param) {
    // empty
  }

  @Method(0x800c7bb8L)
  public static void FUN_800c7bb8() {
    FUN_800ef9e4();
    drawUiElements();

    if(_800bc974.get() != 0) {
      pregameLoadingStage_800bb10c.addu(0x1L);
      return;
    }

    if(fileCount_8004ddc8.get() == 0 && _800c66d0.get() > 0 && _800c66b9.get() == 0 && FUN_800c7da8() != 0) {
      vsyncMode_8007a3b8.setu(0x3L);
      _800fa6dc.setu(0x80L);
      scriptStatePtrArr_800bc1c0.get((int)_800c66c8.get()).deref().ui_60.and(0xffff_efffL);

      if((int)_800c6760.get() <= 0) {
        loadMusicPackage(19, 0);
        _800bc974.setu(0x2L);
      } else {
        //LAB_800c7c98
        final int scriptIndex = FUN_800c7e24();
        _800c66bc.setu(scriptIndex);

        if(scriptIndex >= 0) {
          scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().ui_60.or(0x1008L).and(0xffff_ffdfL);
          _800c66c8.setu(scriptIndex);
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
      if((scriptStatePtrArr_800bc1c0.get(_8006e398.bobjIndices_e0c.get(i).get()).deref().ui_60.get() & 0x408L) != 0) {
        return 0;
      }

      //LAB_800c7e10
    }

    //LAB_800c7e1c
    return 0x1L;
  }

  @Method(0x800c7e24L)
  public static int FUN_800c7e24() {
    //LAB_800c7e54
    for(int i = 0; i < _800c669c.get(); i++) {
      final int scriptIndex = _8006e398.bobjIndices_e78.get(i).get();
      if((scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().ui_60.get() & 0x20L) != 0) {
        return scriptIndex;
      }

      //LAB_800c7e8c
    }

    //LAB_800c7e98
    return -1;
  }

  @Method(0x800c7ea0L)
  public static long FUN_800c7ea0() {
    long v0;
    long v1;
    long a0;
    int s6 = 0;
    long hi;
    long lo;

    //LAB_800c7ee4
    for(int s4 = 0; s4 < 32; s4++) {
      //LAB_800c7ef0
      a0 = 0;
      for(int s1 = 0; s1 < _800c669c.get(); s1++) {
        final int turnValue = scriptStatePtrArr_800bc1c0.get(_8006e398.bobjIndices_e78.get(s1).get()).deref().innerStruct_00.derefAs(BattleObject27c.class).turnValue_4c.get();

        if(turnValue >= a0) {
          a0 = turnValue;
          s6 = s1;
        }

        //LAB_800c7f30
      }

      //LAB_800c7f40
      if(a0 > 0xd9L) {
        final int bobjIndex = _8006e398.bobjIndices_e78.get(s6).get();
        final ScriptState<?> state = scriptStatePtrArr_800bc1c0.get(bobjIndex).deref();
        state.innerStruct_00.derefAs(BattleObject27c.class).turnValue_4c.set((short)(a0 - 0xd9));

        if((state.ui_60.get() & 0x4L) == 0) {
          gameState_800babc8._b8.incr();
        }

        //LAB_800c7f9c
        return bobjIndex;
      }

      //LAB_800c7fa4
      //LAB_800c7fb0
      for(int s1 = 0; s1 < _800c669c.get(); s1++) {
        final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(_8006e398.bobjIndices_e78.get(s1).get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
        v0 = simpleRand() + 0x4_4925L;
        lo = bobj.speed_32.get() * (int)v0 & 0xffff_ffffL;
        a0 = lo;
        v0 = 0x35c2_9183L; //TODO _pretty_ sure this is roughly /312,110 (seems oddly specific?)
        hi = (long)(int)a0 * (int)v0 >>> 32;
        v1 = hi;
        v1 = (int)v1 >> 16;
        a0 = (int)a0 >> 31;
        v1 = v1 - a0;
        bobj.turnValue_4c.add((short)v1);
      }

      //LAB_800c8028
    }

    //LAB_800c8040
    return _8006f244.get();
  }

  @Method(0x800c8068L)
  public static void FUN_800c8068() {
    final long s0 = _800bc974.get();

    if(_800c6690.get() == 0) {
      final long a1 = _800fa6c4.offset(s0 * 0x2L).getSigned();

      if((int)a1 >= 0) {
        _800c6748.setu(a1);
        _800c6914.setu(_800c66c8.get());
      }

      //LAB_800c80c8
      final long v0 = _800c6760.get();
      _800bc97c.setu(v0);

      //LAB_800c8104
      for(int i = 0; i < v0; i++) {
        _800bc968.offset(i * 0x4L).setu(scriptStatePtrArr_800bc1c0.get((int)_8006f244.offset(i * 0x4L).get()).deref().innerStruct_00.derefAs(BattleObject27c.class).charIndex_272.get());
      }

      //LAB_800c8144
      if(s0 == 0x1L) {
        //LAB_800c8180
        for(int i = 0; i < charCount_800c677c.get(); i++) {
          scriptStatePtrArr_800bc1c0.get(_8006e398.charBobjIndices_e40.get(_800c6690.get()).get()).deref().ui_60.or(0x8L);
        }
      }
    }

    //LAB_800c81bc
    //LAB_800c81c0
    _800c6690.incr();

    if(_800c6690.get() >= _800fa6b8.offset(s0 * 0x2L).getSigned() || (joypadPress_8007a398.get() & 0xff) != 0 && _800c6690.get() >= 0x19L) {
      //LAB_800c8214
      FUN_800e9120();
      FUN_80012bb4();
      loadAndRunOverlay(2, getMethodAddress(SItem.class, "FUN_800fc3a0", long.class), 0);

      if(_800bb168.get() == 0) {
        scriptStartEffect(0x1L, _800fa6d0.offset(s0 * 0x2L).getSigned());
      }

      //LAB_800c8274
      if(s0 == 0x2L) {
        FUN_8004cd50((short)(_800fa6d4.getSigned() - 2));
      }

      //LAB_800c8290
      _800c6690.set(0);
      pregameLoadingStage_800bb10c.addu(0x1L);
    }

    //LAB_800c82a8
  }

  @Method(0x800c82b8L)
  public static void FUN_800c82b8() {
    if(_800bb168.get() == 0xffL) {
      FUN_800eeaec();
      FUN_800c8ce4();

      if(!script_800c66fc.isNull()) {
        removeFromLinkedList(script_800c66fc.getPointer());
      }

      //LAB_800c8314
      FUN_80029e04(null);
      deallocateScriptAndChildren((int)scriptIndex_800c674c.get());
      removeFromLinkedList(script_800c670c.getPointer());

      //LAB_800c8368
      for(int i = 0; i < _800c6698.get(); i++) {
        removeFromLinkedList(_800c66d8.offset(i * 0x4L).get());
      }

      //LAB_800c8394
      FUN_8001ad18();

      //LAB_800c83b8
      while(_800c66d0.get() > 0) {
        deallocateScriptAndChildren(_8006e398.bobjIndices_e0c.get(0).get());
      }

      //LAB_800c83d8
      //LAB_800c83f4
      for(int combatantIndex = 0; combatantIndex < combatantCount_800c66a0.get(); combatantIndex++) {
        final CombatantStruct1a8 combatant = combatants_8005e398.get(combatantIndex);

        //LAB_800c8418
        if((combatant.flags_19e.get() & 0x2L) == 0) {
          //LAB_800c8434
          if(!combatant.mrg_00.isNull()) {
            removeFromLinkedList(combatant.mrg_00.getPointer());
          }
        }

        if(combatant._04.get() != 0) {
          removeFromLinkedList(combatant._04.get());
        }

        //LAB_800c8454
      }

      //LAB_800c847c
      FUN_800c8f18();
      FUN_800ca9b4();
      FUN_800ec4f0();
      FUN_800c8748();

      long a1 = previousMainCallbackIndex_8004dd28.get();
      if(a1 == 0x9L) {
        a1 = 0x5L;
      }

      //LAB_800c84b4
      switch((int)_800bc974.get()) {
        case 2 -> {
          final long v1 = encounterId_800bb0f8.get();
          if(v1 == 0x187L || v1 >= 0x194L && v1 < 0x198L) {
            //LAB_800c8514
            gameState_800babc8.scriptFlags2_bc.get(0x1d).or(0x800_0000L);
          } else {
            //LAB_800c8534
            a1 = 0x7L;
          }
        }

        case 4 -> {
          _800bf0dc.setu(0x10L);
          _800bf0ec.setu(0xbL);
          a1 = 0x9L;
        }
      }

      //LAB_800c8558
      _800bc91c.setu(a1);

      long v1 = _800c6724.get();
      if(v1 != 0xff) {
        _80052c34.setu(v1);
      }

      //LAB_800c8578
      v1 = _800c6740.get();
      if(v1 != 0xffff) {
        submapCut_80052c30.set((int)v1);
      }

      //LAB_800c8590
      FUN_8001324c(0xeL);
      _800bc94c.setu(0);

      switch((int)_800bc974.get()) {
        case 1, 3 -> whichMenu_800bdc38.setu(0x1aL);
        case 2, 4, 5 -> whichMenu_800bdc38.setu(0);
      }

      //LAB_800c85f0
      pregameLoadingStage_800bb10c.addu(0x1L);
    }

    //LAB_800c8604
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

  @Method(0x800c8748L)
  public static void FUN_800c8748() {
    removeFromLinkedList(_1f8003f4.getPointer());
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
    final long a0;
    final long a1;
    final long a3;
    final long s0;
    final long s2;
    final long s3;
    if(_800c6764.get() == 0 || _800c66d4.get() == 0 || (_800bc960.get() & 0x80L) == 0) {
      //LAB_800c8ad8
      //LAB_800c8adc
      _800babc0.setu(0);
      _800bb104.setu(0);
      _8007a3a8.setu(0);
    } else {
      _800c6774.addu(_800c676c.get());
      _800c6778.addu(_800c6770.get());
      a3 = (_800c66cc.getSigned() * FUN_800dd118() / 0x1000L + _800c6774.get()) % _1f8003f4.deref().stageMcq_9cb0._14.get() - centreScreenX_1f8003dc.getSigned();
      s2 = _800c6778.get() - (FUN_800dd0d4() + 0x800L & 0xfffL) + 0x760L - centreScreenY_1f8003de.getSigned();
      s0 = a3 - _1f8003f4.deref().stageMcq_9cb0._14.get();
      s3 = a3 + _1f8003f4.deref().stageMcq_9cb0._14.get();
      renderMcq(_1f8003f4.deref().stageMcq_9cb0, 320, 0, a3, s2, _1f8003c8.get() - 0x2L, _800fa6dc.get());
      renderMcq(_1f8003f4.deref().stageMcq_9cb0, 320, 0, s0, s2, _1f8003c8.get() - 0x2L, _800fa6dc.get());

      if(centreScreenX_1f8003dc.getSigned() >= (int)s3) {
        renderMcq(_1f8003f4.deref().stageMcq_9cb0, 320, 0, s3, s2, _1f8003c8.get() - 0x2L, _800fa6dc.get());
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
  public static void loadStage(final int stage) {
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

  @Method(0x800c8ed8L)
  public static void FUN_800c8ed8() {
    _800c66d4.setu(0);
  }

  @Method(0x800c8ee4L)
  public static void FUN_800c8ee4() {
    //LAB_800c8ef4
    //NOTE: zeroes 0x50 bytes after this array of structs ends
    bzero(combatants_8005e398.getAddress(), 0x1090);

    _800c66c0.setu(0x1L);
  }

  @Method(0x800c8f18L)
  public static void FUN_800c8f18() {
    _800c66c0.setu(0);
  }

  @Method(0x800c8f24L)
  public static CombatantStruct1a8 getCombatant(final int index) {
    return combatants_8005e398.get(index);
  }

  @Method(0x800c8f50L)
  public static int addCombatant(final long a0, final int charSlot) {
    //LAB_800c8f6c
    for(int combatantIndex = 0; combatantIndex < 10; combatantIndex++) {
      final CombatantStruct1a8 combatant = combatants_8005e398.get(combatantIndex);

      if((combatant.flags_19e.get() & 0x1L) == 0) {
        if(charSlot < 0) {
          combatant.flags_19e.set(1);
        } else {
          //LAB_800c8f90
          combatant.flags_19e.set(5);
        }

        //LAB_800c8f94
        combatant.charSlot_19c.set((short)charSlot);
        combatant._1a0.set((short)0);
        combatant.charIndex_1a2.set((short)a0);
        combatant._1a4.set((short)-1);
        combatant._1a6.set((short)-1);
        combatantCount_800c66a0.addu(0x1L);
        return combatantIndex;
      }

      //LAB_800c8fbc
    }

    return -1;
  }

  @Method(0x800c8fd4L)
  public static void FUN_800c8fd4(final int combatantIndex) {
    final CombatantStruct1a8 combatant = combatants_8005e398.get(combatantIndex);

    if(combatant._1a0.get() != 0) {
      FUN_800ca918(combatant._1a0.get());
    }

    //LAB_800c9020
    //LAB_800c902c
    bzero(combatant.getAddress(), 0x1a8);

    combatantCount_800c66a0.subu(0x1L);
  }

  @Method(0x800c9060L)
  public static int getCombatantIndex(final int charIndex) {
    //LAB_800c906c
    for(int i = 0; i < 10; i++) {
      final CombatantStruct1a8 combatant = combatants_8005e398.get(i);

      if((combatant.flags_19e.get() & 0x1L) != 0 && combatant.charIndex_1a2.get() == charIndex) {
        //LAB_800c90a8
        return i;
      }

      //LAB_800c9090
    }

    return -1;
  }

  @Method(0x800c90b0L)
  public static long FUN_800c90b0(final int combatantIndex) {
    //LAB_800c9114
    if((combatants_8005e398.get(combatantIndex)._1a4.get() >= 0 || !combatants_8005e398.get(combatantIndex).mrg_00.isNull() && combatants_8005e398.get(combatantIndex).mrg_00.deref().entries.get(32).size.get() != 0) && FUN_800ca054(combatantIndex, 0) != 0) {
      return 0x1L;
    }

    //LAB_800c9128
    //LAB_800c912c
    return 0;
  }

  @Method(0x800c913cL)
  public static long getCombatantFile(final int index) {
    return combatants_8005e398.get(index).filePtr_10.get();
  }

  @Method(0x800c9170L)
  public static void FUN_800c9170(final int combatantIndex) {
    final CombatantStruct1a8 combatant = combatants_8005e398.get(combatantIndex);

    //LAB_800c91bc
    if(!combatant.mrg_00.isNull()) {
      combatant.mrg_00.clear();
    }

    if(combatant._04.get() != 0) {
      if((combatant.flags_19e.get() & 0x2L) == 0) {
        //LAB_800c91e8
        removeFromLinkedList(combatant._04.get());
      }

      combatant._04.set(0);
    }

    if(combatant._1a4.get() >= 0) {
      FUN_800cad64(combatant._1a4.get());
    }

    //LAB_800c921c
    if(combatant._1a6.get() >= 0) {
      FUN_800cad64(combatant._1a6.get());
    }

    //LAB_800c9234
    //LAB_800c9238
    for(int i = 0; i < 32; i++) {
      if(combatant._14.get(i)._09.get() != 0) {
        FUN_800c9c7c(combatantIndex, i);
      }

      //LAB_800c9254
    }

    combatant.flags_19e.and(0xffe7);
  }

  @Method(0x800c9290L)
  public static void loadCombatantTmdAndAnims(final int combatantIndex) {
    long a2 = 0; //TODO this was uninitialized, is the flow right?
    long a3 = 0; //TODO this was uninitialized, is the flow right?

    final CombatantStruct1a8 combatant = combatants_8005e398.get(combatantIndex);
    final long callbackParam;
    final int fileIndex;
    final long transferDest;

    if(combatant.charIndex_1a2.get() >= 0) {
      if((combatant.flags_19e.get() & 0x8L) == 0) {
        if(combatant.mrg_00.isNull()) {
          combatant.flags_19e.or(0x28);

          if((combatant.flags_19e.get() & 0x4) == 0) {
            // Enemy TMDs
            a3 = a3 | 0x7fL;
            a3 = a3 & 0xffff_81ffL;
            a3 = a3 | (combatantIndex & 0x3f) << 9;
            a3 = a3 & 0xffff_ff7fL;
            callbackParam = a3 | 0x100L;
            fileIndex = 3137 + combatant.charIndex_1a2.get();
            transferDest = 0;
          } else {
            // Player TMDs
            //LAB_800c9334
            a2 = a2 & 0xffff_ff80L;
            a2 = a2 | combatant.charSlot_19c.get() & 0x7fL;
            a2 = a2 & 0xffff_81ffL;
            a2 = a2 | (combatantIndex & 0x3f) << 9;
            a2 = a2 & 0xffff_ff7fL;
            a2 = a2 | (combatant.charIndex_1a2.get() & 0x1) << 7;
            callbackParam = a2 & 0xffff_feffL;
            int charIndex = gameState_800babc8.charIndex_88.get(combatant.charSlot_19c.get()).get();
            if((combatant.charIndex_1a2.get() & 0x1) != 0) {
              if(charIndex == 0 && (gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xff) >>> 7 != 0) {
                charIndex = 18; // Divine dragoon
              } else {
                //LAB_800c93b4
                charIndex += 9; // Dragoon
              }

              //LAB_800c93b8
            }

            //LAB_800c93bc
            fileIndex = 3994 + charIndex * 2;
            transferDest = _1f8003f4.deref()._9cdc.offset(combatant.charSlot_19c.get() * 0x4L).get();
            combatant.flags_19e.or(0x2);
          }

          //LAB_800c93e8
          loadDrgnBinFile(0, fileIndex, transferDest, getMethodAddress(Bttl_800c.class, "combatantTmdAndAnimLoadedCallback", long.class, long.class, long.class), callbackParam, 0x3L);
        }
      }
    }

    //LAB_800c940c
  }

  @Method(0x800c941cL)
  public static void combatantTmdAndAnimLoadedCallback(final long address, final long fileSize, final long param) {
    final int combatantIndex = (int)(param >>> 9 & 0x3f);
    final long s0 = param >>> 8 & 0x1L;

    final CombatantStruct1a8 combatant = getCombatant(combatantIndex);
    combatant.flags_19e.and(0xffdf);

    if(s0 == 0) {
      _800bc960.oru(0x4L);
    }

    //LAB_800c947c
    combatant.mrg_00.setPointer(address);
    final MrgFile mrg = combatant.mrg_00.deref();

    if(mrg.entries.get(34).size.get() != 0) {
      combatant.filePtr_10.set(mrg.getFile(34)); // This should be an extended TMD
    }

    //LAB_800c94a0
    //LAB_800c94a4
    for(int animIndex = 0; animIndex < 32; animIndex++) {
      final long size = mrg.entries.get(animIndex).size.get();

      if(size != 0) {
        FUN_800c9a80(mrg.getFile(animIndex), size, 0x1L, 0, combatantIndex, animIndex);
      }

      //LAB_800c94cc
    }
  }

  @Method(0x800c94f8L)
  public static short FUN_800c94f8(final int combatantIndex, final short a1) {
    final CombatantStruct1a8 combatant = combatants_8005e398.get(combatantIndex);
    final short oldVal = combatant._1a4.get();
    combatant._1a4.set(a1);
    return oldVal;
  }

  @Method(0x800c952cL)
  public static void FUN_800c952c(final BigStruct a0, final int combatantIndex) {
    final CombatantStruct1a8 s0 = combatants_8005e398.get(combatantIndex);

    final ExtendedTmd tmd;
    if(s0._1a4.get() >= 0) {
      tmd = MEMORY.ref(4, FUN_800cad34(s0._1a4.get()), ExtendedTmd::new);
    } else {
      //LAB_800c9590
      final MrgFile mrg = s0.mrg_00.derefNullable();

      if(mrg != null && mrg.entries.get(32).size.get() != 0) {
        tmd = mrg.getFile(32, ExtendedTmd::new);
      } else {
        throw new RuntimeException("anim undefined");
      }
    }

    //LAB_800c95bc
    s0.tmd_08.set(tmd);

    final TmdAnimationFile anim = FUN_800ca31c(combatantIndex, 0);
    if((s0.flags_19e.get() & 0x4L) != 0) {
      final long a0_0 = _1f8003f4.deref()._9ce8.offset(s0.charSlot_19c.get() * 0x1298L).getAddress(); //TODO

      a0.dobj2ArrPtr_00.setPointer(a0_0);
      a0.coord2ArrPtr_04.setPointer(a0_0 + 0x230L);
      a0.coord2ParamArrPtr_08.setPointer(a0_0 + 0xd20L);
      a0.count_c8.set((short)35);

      final long a3;
      if((s0.charIndex_1a2.get() & 0x1L) != 0) {
        a3 = 0x9L;
      } else {
        a3 = s0.charIndex_1a2.get() - 0x200 >>> 1;
      }

      //LAB_800c9650
      FUN_80021520(a0, tmd, anim, a3);
    } else {
      //LAB_800c9664
      FUN_80020a00(a0, tmd, anim);
    }

    //LAB_800c9680
    s0._14.get(0)._09.incr();
  }

  @Method(0x800c96acL)
  public static void FUN_800c96ac(final BigStruct a0, final int combatantIndex) {
    if((combatants_8005e398.get(combatantIndex).flags_19e.get() & 0x4L) == 0) {
      FUN_80020fe0(a0);
    }

    //LAB_800c96f8
  }

  @Method(0x800c9708L)
  public static void FUN_800c9708(final int combatantIndex) {
    final int fileIndex;
    long a2 = 0;
    long a3 = 0;
    final CombatantStruct1a8 combatant = combatants_8005e398.get(combatantIndex);
    final long a0_0;

    if(combatant.charIndex_1a2.get() >= 0 && combatant._04.get() == 0) {
      combatant.flags_19e.or(0x10);

      if((combatant.flags_19e.get() & 0x4L) == 0) {
        a3 = a3 | 0x7fL;
        a3 = a3 & 0xffff_81ffL;
        a3 = a3 | (combatantIndex & 0x3fL) << 9;
        a3 = a3 & 0xffff_ff7fL;
        a3 = a3 | 0x100L;
        fileIndex = 3593 + combatant.charIndex_1a2.get();
      } else {
        //LAB_800c97a4
        a2 = a2 & 0xffff_ff80L;
        a0_0 = combatant.charIndex_1a2.get() & 0x1L;
        a2 = a2 | combatant.charSlot_19c.get() & 0x7fL;
        a2 = a2 & 0xffff_81ffL;
        a2 = a2 | (combatantIndex & 0x3fL) << 9;
        a2 = a2 & 0xffff_ff7fL;
        a2 = a2 | a0_0 << 7;
        a3 = a2 & 0xffff_feffL;
        final int charIndex = gameState_800babc8.charIndex_88.get(combatant.charSlot_19c.get()).get();
        if(a0_0 == 0) {
          // Additions
          fileIndex = 4031 + gameState_800babc8.charData_32c.get(charIndex).selectedAddition_19.get() + charIndex * 8 - additionOffsets_8004f5ac.get(charIndex).get();
          //LAB_800c983c
        } else if(charIndex == 0 && (gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xff) >>> 7 != 0) { // Divine dragoon
          // Divine dragoon addition
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

  @Method(0x800c9898L)
  public static void FUN_800c9898(final long address, final long fileSize, final long param) {
    long s5 = address;
    final int combatantIndex = (int)(param >>> 9 & 0x3f);
    final long s0 = param >>> 8 & 0x1L;
    final int s7 = (int)(param << 25) >> 25;
    final CombatantStruct1a8 combatant = getCombatant(combatantIndex);

    if(combatant._04.get() != 0) {
      removeFromLinkedList(s5);
    } else {
      //LAB_800c9910
      if(s0 == 0 && MEMORY.ref(4, s5).offset(0x4L).get() == 0x40L) {
        _8006e398.bobjIndices_d80.get(s7).set(0);
        long s4 = 0x100L;

        //LAB_800c9940
        for(int animIndex = 0; animIndex < 32; animIndex++) {
          if(MEMORY.ref(4, s5).offset(0xcL).offset(s4).get() != 0) {
            if(combatant._14.get(animIndex)._09.get() != 0) {
              FUN_800c9c7c(combatantIndex, animIndex);
            }

            //LAB_800c9974
            FUN_800c9a80(s5 + MEMORY.ref(4, s5).offset(0x8L).offset(s4).get(), MEMORY.ref(4, s5).offset(0xcL).offset(s4).get(), 0x6L, s7, combatantIndex, animIndex);
          }

          //LAB_800c9990
          s4 = s4 + 0x8L;
        }

        DrawSync(0);

        final long v0 = FUN_80012444(s5, FUN_80015704(s5, 0x20L));
        if(v0 != 0) {
          s5 = v0;
        }
      }

      //LAB_800c99d8
      combatant._04.set(s5);

      //LAB_800c99e8
      for(int aninmIndex = 0; aninmIndex < 32; aninmIndex++) {
        if(MEMORY.ref(4, s5).offset(aninmIndex * 0x8L).offset(0xcL).get() != 0) {
          if(combatant._14.get(aninmIndex)._09.get() != 0) {
            FUN_800c9c7c(combatantIndex, aninmIndex);
          }

          //LAB_800c9a18
          FUN_800c9a80(s5 + MEMORY.ref(4, s5).offset(aninmIndex * 0x8L).offset(0x8L).get(), MEMORY.ref(4, s5).offset(aninmIndex * 0x8L).offset(0xcL).get(), 0x2L, 1, combatantIndex, aninmIndex);
        }

        //LAB_800c9a34
      }
    }

    //LAB_800c9a48
  }

  /** TODO this method is very weird, are we dealing with a polymorphic struct? */
  @Method(0x800c9a80L)
  public static void FUN_800c9a80(final long addr, final long size, final long a2, final int a3, final int combatantIndex, final int animIndex) {
    final BattleStruct1a8_c s3 = combatants_8005e398.get(combatantIndex)._14.get(animIndex);

    if(s3._0a.get() != 0) {
      FUN_800c9c7c(combatantIndex, animIndex);
    }

    //LAB_800c9b28
    if(a2 == 0x1L) {
      //LAB_800c9b68
      if(MEMORY.ref(4, addr).offset(0x4L).get() == 0x1a45_5042L) {
        s3._0a.set(0x4);
        s3.bpe_00.set(addr);
        s3._0b.set(0);
        s3._08.set(a3);
      } else {
        s3._0a.set((int)a2);
        s3.bpe_00.set(addr);
        s3._0b.set((int)a2);
        s3._08.set(a3);
      }
    } else if(a2 == 0x2L) {
      //LAB_800c9b80
      if(MEMORY.ref(4, addr).offset(0x4L).get() == 0x1a45_5042L) {
        //LAB_800c9b88
        s3._0a.set(0x5);
        s3.bpe_00.set(addr);
        s3._0b.set(0);
        s3._08.set(a3);
      } else {
        //LAB_800c9b98
        s3._0a.set((int)a2);
        s3.bpe_00.set(addr);
        s3._0b.set(0x1);

        //LAB_800c9ba8
        s3._08.set(a3);
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
      final RECT sp0x10 = new RECT((short)(512 + a3 * 64), (short)_8006e398.bobjIndices_d80.get(a3).get(), (short)64, (short)(size / 128));
      LoadImage(sp0x10, addr);

      _8006e398.bobjIndices_d80.get(a3).add((int)(size / 128));
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
  public static void FUN_800c9c7c(final int combatantIndex, final int animIndex) {
    final BattleStruct1a8_c s0 = combatants_8005e398.get(combatantIndex)._14.get(animIndex);

    //LAB_800c9cec
    while(s0._09.get() > 0) {
      FUN_800ca194(combatantIndex, animIndex);
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
          _8006e398._d8c.get(s0._06.get()).used_04.set(false);
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

  @Method(0x800c9db8L)
  public static void FUN_800c9db8(final int combatantIndex, final int animIndex, final int a2) {
    FUN_800c9c7c(combatantIndex, animIndex);
    FUN_800c9a80(0, 0, 0x3L, a2, combatantIndex, animIndex);
  }

  @Method(0x800c9e10L)
  public static long FUN_800c9e10(final int combatantIndex, final int animIndex) {
    final BattleStruct1a8_c s0 = combatants_8005e398.get(combatantIndex)._14.get(animIndex);

    return switch(s0._0a.get()) {
      case 1, 2 -> s0.bpe_00.get() != 0 ? 0x1L : 0;
      case 3 -> (int)s0.bpe_00.get() >= 0 ? 0x1L : 0;
      case 4, 5 -> {
        if(s0._0b.get() == 0) {
          final int a3 = (short)_800c66ac.getSigned() + 1 & 0xffff_fff0;
          _800c66ac.setu(a3);
          _8006e398._d8c.get(a3)._00.set(s0);
          _8006e398._d8c.get(a3).used_04.set(true);
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
    final BattleStruct1a8_c s0 = _8006e398._d8c.get((int)param)._00.deref();

    if(s0._0b.get() != 0 && _8006e398._d8c.get((int)param).used_04.get()) {
      s0._04.set((short)FUN_800caae4(address, 0x3L, 0, 0));
      s0._06.set((short)-1);
      _8006e398._d8c.get((int)param).used_04.set(false);
    } else {
      //LAB_800ca034
      //LAB_800ca038
      removeFromLinkedList(address);
    }

    //LAB_800ca040
  }

  @Method(0x800ca054L)
  public static long FUN_800ca054(final int combatantIndex, final int animIndex) {
    switch(combatants_8005e398.get(combatantIndex)._14.get(animIndex)._0a.get()) {
      case 0:
        return 0;

      case 1:
      case 2:
      case 3:
        return 1;

      case 4:
      case 5:
      case 6:
        if(combatants_8005e398.get(combatantIndex)._14.get(animIndex)._0b.get() == 0) {
          return 0;
        }

        //LAB_800ca0f0
        return ~combatants_8005e398.get(combatantIndex)._14.get(animIndex)._04.get() >>> 31;
    }

    //LAB_800ca0f8
    return 0;
  }

  @Method(0x800ca100L)
  public static void FUN_800ca100(final BigStruct a0, final int combatantIndex, final int animIndex) {
    FUN_80021584(a0, FUN_800ca31c(combatantIndex, animIndex));
    combatants_8005e398.get(combatantIndex)._14.get(0)._09.incr();
  }

  @Method(0x800ca194L)
  public static long FUN_800ca194(final int combatantIndex, final int animIndex) {
    final BattleStruct1a8_c s0 = combatants_8005e398.get(combatantIndex)._14.get(animIndex);

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

  @Method(0x800ca26cL)
  public static boolean FUN_800ca26c(final int combatantIndex) {
    final CombatantStruct1a8 combatant = combatants_8005e398.get(combatantIndex);

    //LAB_800ca2bc
    boolean s3 = true;
    for(int i = 0; i < 32; i++) {
      if(combatant._14.get(i)._09.get() == 0) {
        if(FUN_800ca194(combatantIndex, i) != 0) {
          s3 = !s3;
        } else {
          s3 = false;
        }
      }

      //LAB_800ca2e8
    }

    return s3;
  }

  @Method(0x800ca31cL)
  public static TmdAnimationFile FUN_800ca31c(final int combatantIndex, final int animIndex) {
    final BattleStruct1a8_c a0_0 = combatants_8005e398.get(combatantIndex)._14.get(animIndex);

    return switch(a0_0._0a.get()) {
      case 1, 2 -> MEMORY.ref(4, a0_0.bpe_00.get(), TmdAnimationFile::new); //TODO

      case 3 -> {
        final long s0 = a0_0.bpe_00.get(); //TODO

        if(a0_0._09.get() == 0 || encounterId_800bb0f8.get() != 0x1bbL) {
          //LAB_800ca3c4
          FUN_800cadbc(s0);
        }

        yield MEMORY.ref(4, FUN_800cad34(s0), TmdAnimationFile::new); //TODO
      }

      case 4, 5, 6 -> {
        if(a0_0._0b.get() != 0) {
          final long s0 = a0_0._04.get();

          if((int)s0 >= 0) {
            //LAB_800ca3f4
            yield MEMORY.ref(4, FUN_800cad34(s0), TmdAnimationFile::new);
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
    final CombatantStruct1a8 combatant = combatants_8005e398.get(index);

    if((combatant.flags_19e.get() & 0x4L) != 0) {
      _8006e398.bobjIndices_d80.get(combatant.charSlot_19c.get()).set(0);
    }

    //LAB_800ca488
    //LAB_800ca494
    for(int i = 0; i < 32; i++) {
      final int v1 = combatant._14.get(i)._0a.get();

      if(v1 == 2 || v1 >= 5 && v1 < 7) {
        //LAB_800ca4c0
        FUN_800c9c7c(index, i);
      }

      //LAB_800ca4cc
    }

    final long addr = combatant._04.get();
    if(addr != 0) {
      removeFromLinkedList(addr);
      combatant._04.set(0);
    }

    //LAB_800ca4f8
    combatant.flags_19e.and(0xffef);
  }

  @Method(0x800ca528L)
  public static short FUN_800ca528(final int combatantIndex, final short a1) {
    final CombatantStruct1a8 combatant = combatants_8005e398.get(combatantIndex);
    final short oldVal = combatant._1a6.get();
    combatant._1a6.set(a1);
    return oldVal;
  }

  @Method(0x800ca55cL)
  public static void FUN_800ca55c(final int combatantIndex) {
    long v0;
    long v1;
    long a2 = 0;
    final CombatantStruct1a8 combatant = combatants_8005e398.get(combatantIndex);
    final int a1 = combatant.charIndex_1a2.get();

    if(a1 >= 0) {
      v1 = combatant.charSlot_19c.get();
      a2 = a2 & 0xffff_ff80L;
      v0 = v1 & 0x7fL;
      a2 = a2 | v0;
      a2 = a2 & 0xffff_81ffL;
      v0 = combatantIndex & 0x3fL;
      v0 = v0 << 9;
      a2 = a2 | v0;
      a2 = a2 & 0xffff_ff7fL;
      final long a0 = a1 & 0x1L;
      v0 = a0 << 7;
      a2 = a2 | v0;
      int fileIndex = gameState_800babc8.charIndex_88.get((int)v1).get();
      v1 = a2 & 0xffff_feffL;
      if(a0 != 0) {
        if(fileIndex == 0) {
          if((gameState_800babc8.dragoonSpirits_19c.get(0).get() & 0xff) >>> 7 == 0) {
            fileIndex += 9;
          } else {
            fileIndex = 18;
          }
        } else {
          //LAB_800ca618
          fileIndex += 9;
        }
      }

      //LAB_800ca61c
      loadDrgnBinFile(0, 3993 + fileIndex * 2, 0, getMethodAddress(Bttl_800c.class, "FUN_800ca65c", long.class, long.class, long.class), v1, 0x5L);
    }

    //LAB_800ca64c
  }

  @Method(0x800ca65cL)
  public static void FUN_800ca65c(final long address, final long fileSize, final long param) {
    final int combatantIndex = (int)param >>> 9 & 0x3f;
    final CombatantStruct1a8 s1 = getCombatant(combatantIndex);

    final MrgFile mrg = MEMORY.ref(4, address, MrgFile::new);

    final long count = mrg.count.get();
    if(count != 1) {
      //LAB_800ca6c4
      if(s1._1a0.get() != 0) {
        FUN_800ca918(s1._1a0.get());
        s1._1a0.set((short)0);
      }

      //LAB_800ca6e0
      //LAB_800ca6f0
      for(int i = 0; i < count; i++) {
        FUN_800ca8fc(0x6L - i);

        if(mrg.entries.get(i).size.get() != 0) {
          FUN_800ca75c(-1, mrg.getFile(i));
        }

        //LAB_800ca714
      }
    } else if(mrg.entries.get(0).size.get() != 0) {
      FUN_800ca75c(combatantIndex, mrg.getFile(0));
    }

    //LAB_800ca724
    //LAB_800ca728
    FUN_800127cc(address, 0, 0x1L);
  }

  @Method(0x800ca75cL)
  public static void FUN_800ca75c(final int index, final long timFile) {
    short a0;

    if(index >= 0) {
      //LAB_800ca77c
      final CombatantStruct1a8 s0 = getCombatant(index);
      a0 = s0._1a0.get();

      if(a0 == 0) {
        final short charSlot = s0.charSlot_19c.get();

        if(charSlot < 0) {
          a0 = (short)FUN_800ca89c(s0.charIndex_1a2.get());
          s0._1a0.set(a0);
        } else {
          a0 = (short)(charSlot + 1);

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

  @Method(0x800ca8fcL)
  public static void FUN_800ca8fc(final long a0) {
    _800c66c4.oru(1L << a0);
  }

  @Method(0x800ca918L)
  public static void FUN_800ca918(final int a0) {
    _800c66c4.and(~(1L << a0));
  }

  @Method(0x800ca938L)
  public static long FUN_800ca938(final long a0) {
    return _800fa730.offset(2, combatants_8005e398.get((int)a0)._1a0.get() * 0x2L).getSigned();
  }

  @Method(0x800ca980L)
  public static void FUN_800ca980() {
    //LAB_800ca990
    for(int i = 0; i < 0x200; i++) {
      _8006e918.offset(i * 0x4L).setu(0);
    }

    _800c66c1.setu(0x1L);
  }

  @Method(0x800ca9b4L)
  public static void FUN_800ca9b4() {
    _800c66c1.setu(0);

    //LAB_800ca9d8
    long s0 = _8006e918.getAddress();
    for(int s1 = 0; s1 < 0x100; s1++) {
      if(MEMORY.ref(1, s0).offset(0x4L).get() >= 0x2L) {
        removeFromLinkedList(MEMORY.ref(4, s0).get());
        MEMORY.ref(1, s0).offset(0x4L).setu(0);
      }

      //LAB_800ca9fc
      s0 = s0 + 0x8L;
    }
  }

  @Method(0x800caa20L)
  public static int FUN_800caa20() {
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
    return -1;
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

  @Method(0x800cac38L)
  public static int FUN_800cac38(final int drgnIndex, final int fileIndex) {
    final int s0 = FUN_800caa20();

    if(s0 < 0) {
      //LAB_800cac94
      return -1;
    }

    loadDrgnBinFile(drgnIndex, fileIndex, 0, getMethodAddress(Bttl_800c.class, "FUN_800cacb0", long.class, long.class, long.class), s0, 0x2L);

    //LAB_800cac98
    return s0;
  }

  @Method(0x800cacb0L)
  public static void FUN_800cacb0(final long address, final long size, final long param) {
    final long a1 = _8006e918.offset(param * 0x8L).getAddress();
    if(MEMORY.ref(1, a1).offset(0x4L).get() == 1) {
      MEMORY.ref(4, a1).offset(0x0L).setu(address);
      MEMORY.ref(1, a1).offset(0x4L).setu(0x2L);
      FUN_800cadbc(param);
    } else {
      //LAB_800cacf4
      removeFromLinkedList(address);
    }

    //LAB_800cad04
  }

  @Method(0x800cad34L)
  public static long FUN_800cad34(final long a0) {
    return _8006e918.offset(4, a0 * 0x8L).get();
  }

  @Method(0x800cad50L)
  public static long FUN_800cad50(final long a0) {
    return _8006e918.offset(a0 * 0x8L).getAddress();
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
  public static void FUN_800cae50(final int index, final ScriptState<BattleObject27c> state, final BattleObject27c data) {
    data._278.set(0);

    final long v1;
    if((state.ui_60.get() & 0x4L) != 0) {
      v1 = _800bc960.get() & 0x110L;
    } else {
      //LAB_800cae94
      v1 = _800bc960.get() & 0x210L;
    }

    //LAB_800cae98
    if(v1 != 0 && FUN_800c90b0(data.combatantIndex_26c.get()) != 0) {
      data.colourMap_1e5.set((int)FUN_800ca938(data.combatantIndex_26c.get()));
      data.animIndex_26e.set((short)0);
      FUN_800c952c(data._148, data.combatantIndex_26c.get());
      data._278.set(1);
      data.animIndex_270.set((short)-1);

      if((state.ui_60.get() & 0x800L) == 0) {
        final ScriptFile script;
        final String scriptName;
        final int scriptLength;
        if((state.ui_60.get() & 0x4L) != 0) {
          script = MEMORY.ref(4, getCombatantFile(data.combatantIndex_26c.get()), ScriptFile::new);

          final Tuple<String, Integer> tuple = _8005e398_SCRIPT_SIZES.get(data.combatantIndex_26c.get());
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
      setCallback04(index, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800caf50", int.class, ScriptState.classFor(BattleObject27c.class), BattleObject27c.class), TriConsumerRef::new));
    }

    //LAB_800caf38
  }

  @Method(0x800caf2cL)
  public static void FUN_800caf50(final int index, final ScriptState<BattleObject27c> state, final BattleObject27c data) {
    setCallback08(index, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cb024", int.class, ScriptState.classFor(BattleObject27c.class), BattleObject27c.class), TriConsumerRef::new));
    setCallback04(index, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cafb4", int.class, ScriptState.classFor(BattleObject27c.class), BattleObject27c.class), TriConsumerRef::new));
    FUN_800cafb4(index, state, data);
  }

  @Method(0x800cafb4L)
  public static void FUN_800cafb4(final int index, final ScriptState<BattleObject27c> state, final BattleObject27c data) {
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
  public static void FUN_800cb024(final int index, final ScriptState<BattleObject27c> state, final BattleObject27c data) {
    if((state.ui_60.get() & 0x211L) == 0) {
      FUN_800211d8(data._148);
    }

    //LAB_800cb048
  }

  @Method(0x800cb058L)
  public static void FUN_800cb058(final int index, final ScriptState<BattleObject27c> state, final BattleObject27c data) {
    if(data._278.get() != 0) {
      FUN_800c96ac(data._148, data.combatantIndex_26c.get());
    }

    //LAB_800cb088
    FUN_800ca194(data.combatantIndex_26c.get(), data.animIndex_26e.get());

    _800c66d0.decr();

    //LAB_800cb0d4
    for(int i = data._274.get(); i < _800c66d0.get(); i++) {
      _8006e398.bobjIndices_e0c.get(i).set(_8006e398.bobjIndices_e0c.get(i + 1).get());
      scriptStatePtrArr_800bc1c0.get(_8006e398.bobjIndices_e0c.get(i).get()).deref().innerStruct_00.derefAs(BattleObject27c.class)._274.set((short)i);
    }

    //LAB_800cb11c
    if((state.ui_60.get() & 0x4L) != 0) {
      _800c6768.decr();

      //LAB_800cb168
      for(int i = data.charSlot_276.get(); i < _800c6768.get(); i++) {
        _8006e398.bobjIndices_e50.get(i).set(_8006e398.bobjIndices_e50.get(i + 1).get());
        scriptStatePtrArr_800bc1c0.get(_8006e398.bobjIndices_e50.get(i).get()).deref().innerStruct_00.derefAs(BattleObject27c.class).charSlot_276.set((short)i);
      }
    } else {
      //LAB_800cb1b8
      charCount_800c677c.decr();

      //LAB_800cb1f4
      for(int i = data.charSlot_276.get(); i < charCount_800c677c.get(); i++) {
        _8006e398.charBobjIndices_e40.get(i).set(_8006e398.charBobjIndices_e40.get(i + 1).get());
        scriptStatePtrArr_800bc1c0.get(_8006e398.charBobjIndices_e40.get(i).get()).deref().innerStruct_00.derefAs(BattleObject27c.class).charSlot_276.set((short)i);
      }
    }

    //LAB_800cb23c
  }

  @Method(0x800cb250L)
  public static long FUN_800cb250(final int index, final ScriptState<BattleObject27c> state, final BattleObject27c data) {
    int x = state._e8.get();
    int y = state._ec.get();
    int z = state._f0.get();

    if(state.scriptIndex_c8.get() >= 0) {
      final BattleObject27c data2 = scriptStatePtrArr_800bc1c0.get(state.scriptIndex_c8.get()).deref().innerStruct_00.derefAs(BattleObject27c.class);

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

  @Method(0x800cb34cL)
  public static long FUN_800cb34c(final int index, final ScriptState<BattleObject27c> state, final BattleObject27c data) {
    final VECTOR vec = scriptStatePtrArr_800bc1c0.get(state.scriptIndex_c8.get()).deref().innerStruct_00.derefAs(BattleObject27c.class)._148.coord2_14.coord.transfer;
    final int a0 = ratan2(vec.getX() - data._148.coord2_14.coord.transfer.getX(), vec.getZ() - data._148.coord2_14.coord.transfer.getZ()) + 0x800;
    state._cc.decr();
    if(state._cc.get() > 0) {
      state._d0.sub(state._d4.get());
      data._148.coord2Param_64.rotate.setY((short)(a0 + state._d0.get()));
      return 0;
    }

    //LAB_800cb3e0
    data._148.coord2Param_64.rotate.setY((short)a0);

    //LAB_800cb3e8
    return 1;
  }

  @Method(0x800cb3fcL)
  public static long FUN_800cb3fc(final RunningScript a0) {
    final BattleObject27c v1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    v1._148.coord2_14.coord.transfer.setX(a0.params_20.get(1).deref().get());
    v1._148.coord2_14.coord.transfer.setY(a0.params_20.get(2).deref().get());
    v1._148.coord2_14.coord.transfer.setZ(a0.params_20.get(3).deref().get());
    return 0;
  }

  @Method(0x800cb468L)
  public static long FUN_800cb468(final RunningScript a0) {
    final BattleObject27c v1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    a0.params_20.get(1).deref().set(v1._148.coord2_14.coord.transfer.getX());
    a0.params_20.get(2).deref().set(v1._148.coord2_14.coord.transfer.getY());
    a0.params_20.get(3).deref().set(v1._148.coord2_14.coord.transfer.getZ());
    return 0;
  }

  @Method(0x800cb4c8L)
  public static long FUN_800cb4c8(final RunningScript a0) {
    final BattleObject27c v1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    v1._148.coord2Param_64.rotate.setX((short)a0.params_20.get(1).deref().get());
    v1._148.coord2Param_64.rotate.setY((short)a0.params_20.get(2).deref().get());
    v1._148.coord2Param_64.rotate.setZ((short)a0.params_20.get(3).deref().get());
    return 0;
  }

  @Method(0x800cb534L)
  public static long FUN_800cb534(final RunningScript a0) {
    scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class)._148.coord2Param_64.rotate.setY((short)a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800cb578L)
  public static long FUN_800cb578(final RunningScript a0) {
    final BattleObject27c v1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    a0.params_20.get(1).deref().set(v1._148.coord2Param_64.rotate.getX());
    a0.params_20.get(2).deref().set(v1._148.coord2Param_64.rotate.getY());
    a0.params_20.get(3).deref().set(v1._148.coord2Param_64.rotate.getZ());
    return 0;
  }

  @Method(0x800cb5d8L)
  public static long FUN_800cb5d8(final RunningScript a0) {
    a0.params_20.get(1).deref().set((short)scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class).monsterStatusResistFlag_76.get());
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
    final BattleObject27c v1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    v1._1ea.set(a0.params_20.get(1).deref().get() < 1 ? 1 : 0);
    return 0;
  }

  @Method(0x800cb6bcL)
  public static long FUN_800cb6bc(final RunningScript a0) {
    final ScriptState<?> v0 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref();
    if((v0.ui_60.get() & 0x1L) != 0) {
      return 0x2L;
    }

    final BattleObject27c s0 = v0.innerStruct_00.derefAs(BattleObject27c.class);
    final int animIndex = a0.params_20.get(1).deref().get();

    if(s0.animIndex_270.get() < 0) {
      FUN_800c9e10(s0.combatantIndex_26c.get(), animIndex);
      s0.animIndex_270.set((short)animIndex);
    } else if(s0.animIndex_270.get() != animIndex) {
      FUN_800ca194(s0.combatantIndex_26c.get(), s0.animIndex_270.get());

      //LAB_800cb73c
      FUN_800c9e10(s0.combatantIndex_26c.get(), animIndex);
      s0.animIndex_270.set((short)animIndex);
    }

    //LAB_800cb750
    return 0x1L;
  }

  @Method(0x800cb764L)
  public static long FUN_800cb764(final RunningScript a0) {
    return 0;
  }

  @Method(0x800cb76cL)
  public static long FUN_800cb76c(final RunningScript a0) {
    final ScriptState<BattleObject27c> s2 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).derefAs(ScriptState.classFor(BattleObject27c.class));
    final BattleObject27c s0 = s2.innerStruct_00.deref();
    if((s2.ui_60.get() & 0x1L) == 0) {
      short animIndex = s0.animIndex_270.get();

      if(animIndex < 0) {
        animIndex = 0;
      }

      //LAB_800cb7d0
      if(FUN_800ca054(s0.combatantIndex_26c.get(), animIndex) != 0) {
        FUN_800ca194(s0.combatantIndex_26c.get(), s0.animIndex_26e.get());
        FUN_800ca100(s0._148, s0.combatantIndex_26c.get(), animIndex);
        s2.ui_60.and(0xffff_ff6fL);
        s0._1e4.set(1);
        s0.animIndex_26e.set(animIndex);
        s0.animIndex_270.set((short)-1);
        return 0;
      }
    }

    //LAB_800cb830
    //LAB_800cb834
    return 0x2L;
  }

  @Method(0x800cb84cL)
  public static long FUN_800cb84c(final RunningScript a0) {
    final ScriptState<BattleObject27c> s2 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).derefAs(ScriptState.classFor(BattleObject27c.class));
    final BattleObject27c s0 = s2.innerStruct_00.derefAs(BattleObject27c.class);

    if((s2.ui_60.get() & 0x1L) == 0) {
      final int newAnim = a0.params_20.get(1).deref().get();
      final short currentAnim = s0.animIndex_270.get();

      if(currentAnim >= 0) {
        if(currentAnim != newAnim) {
          FUN_800ca194(s0.combatantIndex_26c.get(), currentAnim);
        }

        //LAB_800cb8d0
        s0.animIndex_270.set((short)-1);
      }

      //LAB_800cb8d4
      if(FUN_800ca054(s0.combatantIndex_26c.get(), newAnim) != 0) {
        FUN_800ca194(s0.combatantIndex_26c.get(), s0.animIndex_26e.get());
        FUN_800ca100(s0._148, s0.combatantIndex_26c.get(), newAnim);
        s2.ui_60.and(0xffff_ff6fL);
        s0._1e4.set(1);
        s0.animIndex_26e.set((short)newAnim);
        s0.animIndex_270.set((short)-1);
        return 0;
      }

      //LAB_800cb934
      FUN_800c9e10(s0.combatantIndex_26c.get(), newAnim);
    }

    //LAB_800cb944
    return 0x2L;
  }

  @Method(0x800cb95cL)
  public static long FUN_800cb95c(final RunningScript a0) {
    FUN_800ca26c(scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class).combatantIndex_26c.get());
    return 0;
  }

  @Method(0x800cb9b0L)
  public static long FUN_800cb9b0(final RunningScript a0) {
    a0.params_20.get(1).deref().set(scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class).animIndex_26e.get());
    return 0;
  }

  @Method(0x800cb9f0L)
  public static long FUN_800cb9f0(final RunningScript a0) {
    scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class)._1e4.set(2);
    return 0;
  }

  @Method(0x800cba28L)
  public static long FUN_800cba28(final RunningScript a0) {
    scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class)._1e4.set(1);
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
    a0.params_20.get(1).deref().set(scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class)._1e6.get() < 0x1L ? 1 : 0);
    return 0;
  }

  @Method(0x800cbb00L)
  public static long FUN_800cbb00(final RunningScript t1) {
    final int s0 = t1.params_20.get(0).deref().get();
    final ScriptState<BattleObject27c> a0 = scriptStatePtrArr_800bc1c0.get(s0).derefAs(ScriptState.classFor(BattleObject27c.class));
    BattleObject27c v1 = a0.innerStruct_00.derefAs(BattleObject27c.class);

    int x = v1._148.coord2_14.coord.transfer.getX();
    int y = v1._148.coord2_14.coord.transfer.getY();
    int z = v1._148.coord2_14.coord.transfer.getZ();

    final int t0 = t1.params_20.get(1).deref().get();
    if(t0 >= 0) {
      v1 = scriptStatePtrArr_800bc1c0.get(t0).deref().innerStruct_00.derefAs(BattleObject27c.class);
      x -= v1._148.coord2_14.coord.transfer.getX();
      y -= v1._148.coord2_14.coord.transfer.getY();
      z -= v1._148.coord2_14.coord.transfer.getZ();
    }

    //LAB_800cbb98
    a0.scriptIndex_c8.set(t0);
    FUN_800cdc1c(a0, x, y, z, t1.params_20.get(3).deref().get(), t1.params_20.get(4).deref().get(), t1.params_20.get(5).deref().get(), 0, t1.params_20.get(2).deref().get());
    setCallback10(s0, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cb250", int.class, ScriptState.classFor(BattleObject27c.class), BattleObject27c.class), TriFunctionRef::new));
    return 0;
  }

  @Method(0x800cbc14L)
  public static long FUN_800cbc14(final RunningScript s0) {
    final int scriptIndex1 = s0.params_20.get(0).deref().get();
    final ScriptState<BattleObject27c> state = scriptStatePtrArr_800bc1c0.get(scriptIndex1).derefAs(ScriptState.classFor(BattleObject27c.class));
    final BattleObject27c bobj1 = state.innerStruct_00.deref();
    final VECTOR vec = new VECTOR().set(bobj1._148.coord2_14.coord.transfer);
    final int scriptIndex2 = s0.params_20.get(1).deref().get();

    if(scriptIndex2 >= 0) {
      final BattleObject27c bobj2 = scriptStatePtrArr_800bc1c0.get(scriptIndex2).deref().innerStruct_00.derefAs(BattleObject27c.class);
      vec.sub(bobj2._148.coord2_14.coord.transfer);
    }

    //LAB_800cbcc4
    final int x = s0.params_20.get(3).deref().get() - vec.getX();
    final int y = s0.params_20.get(4).deref().get() - vec.getY();
    final int z = s0.params_20.get(5).deref().get() - vec.getZ();
    state.scriptIndex_c8.set(scriptIndex2);
    FUN_800cdc1c(state, vec.getX(), vec.getY(), vec.getZ(), s0.params_20.get(3).deref().get(), s0.params_20.get(4).deref().get(), s0.params_20.get(5).deref().get(), 0, SquareRoot0(x * x + y * y + z * z) / s0.params_20.get(2).deref().get());
    setCallback10(scriptIndex1, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cb250", int.class, ScriptState.classFor(BattleObject27c.class), BattleObject27c.class), TriFunctionRef::new));
    return 0;
  }

  @Method(0x800cbde0L)
  public static long FUN_800cbde0(final RunningScript t1) {
    final ScriptState<BattleObject27c> a0 = scriptStatePtrArr_800bc1c0.get(t1.params_20.get(0).deref().get()).derefAs(ScriptState.classFor(BattleObject27c.class));
    BattleObject27c v1 = a0.innerStruct_00.deref();
    int a1 = v1._148.coord2_14.coord.transfer.getX();
    int a2 = v1._148.coord2_14.coord.transfer.getY();
    int a3 = v1._148.coord2_14.coord.transfer.getZ();

    if(t1.params_20.get(1).deref().get() >= 0) {
      final ScriptState<BattleObject27c> v0 = scriptStatePtrArr_800bc1c0.get(t1.params_20.get(1).deref().get()).derefAs(ScriptState.classFor(BattleObject27c.class));
      v1 = v0.innerStruct_00.deref();
      a1 -= v1._148.coord2_14.coord.transfer.getX();
      a2 -= v1._148.coord2_14.coord.transfer.getY();
      a3 -= v1._148.coord2_14.coord.transfer.getZ();
    }

    //LAB_800cbe78
    a0.scriptIndex_c8.set(t1.params_20.get(1).deref().get());
    FUN_800cdc1c(a0, a1, a2, a3, t1.params_20.get(3).deref().get(), t1.params_20.get(4).deref().get(), t1.params_20.get(5).deref().get(), 0x20, t1.params_20.get(2).deref().get());
    setCallback10(t1.params_20.get(0).deref().get(), MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cb250", int.class, ScriptState.classFor(BattleObject27c.class), BattleObject27c.class), TriFunctionRef::new));
    return 0;
  }

  @Method(0x800cbef8L)
  public static long FUN_800cbef8(final RunningScript s0) {
    final int scriptIndex1 = s0.params_20.get(0).deref().get();
    final int scriptIndex2 = s0.params_20.get(1).deref().get();
    final ScriptState<BattleObject27c> s5 = scriptStatePtrArr_800bc1c0.get(scriptIndex1).derefAs(ScriptState.classFor(BattleObject27c.class));
    final BattleObject27c bobj1 = s5.innerStruct_00.deref();
    final VECTOR vec = new VECTOR().set(bobj1._148.coord2_14.coord.transfer);

    if(scriptIndex2 >= 0) {
      final BattleObject27c bobj2 = scriptStatePtrArr_800bc1c0.get(scriptIndex2).deref().innerStruct_00.derefAs(BattleObject27c.class);
      vec.sub(bobj2._148.coord2_14.coord.transfer);
    }

    //LAB_800cbfa8
    final int x = s0.params_20.get(3).deref().get() - vec.getX();
    final int y = s0.params_20.get(4).deref().get() - vec.getY();
    final int z = s0.params_20.get(5).deref().get() - vec.getZ();
    s5.scriptIndex_c8.set(scriptIndex2);
    FUN_800cdc1c(s5, vec.getX(), vec.getY(), vec.getZ(), s0.params_20.get(3).deref().get(), s0.params_20.get(4).deref().get(), s0.params_20.get(5).deref().get(), 0x20, SquareRoot0(x * x + y * y + z * z) / s0.params_20.get(2).deref().get());
    setCallback10(scriptIndex1, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cb250", int.class, ScriptState.classFor(BattleObject27c.class), BattleObject27c.class), TriFunctionRef::new));
    return 0;
  }

  @Method(0x800cc0c8L)
  public static long FUN_800cc0c8(final RunningScript t1) {
    final int s0 = t1.params_20.get(0).deref().get();
    final ScriptState<BattleObject27c> a0 = scriptStatePtrArr_800bc1c0.get(s0).derefAs(ScriptState.classFor(BattleObject27c.class));
    BattleObject27c v1 = a0.innerStruct_00.deref();
    final VECTOR a1 = new VECTOR().set(v1._148.coord2_14.coord.transfer);
    final int t0 = t1.params_20.get(1).deref().get();

    if(t0 >= 0) {
      v1 = scriptStatePtrArr_800bc1c0.get(t0).derefAs(ScriptState.classFor(BattleObject27c.class)).innerStruct_00.deref();
      a1.sub(v1._148.coord2_14.coord.transfer);
    }

    //LAB_800cc160
    a0.scriptIndex_c8.set(t0);
    FUN_800cdc1c(a0, a1.getX(), a1.getY(), a1.getZ(), t1.params_20.get(3).deref().get(), a1.getY(), t1.params_20.get(4).deref().get(), 0, t1.params_20.get(2).deref().get());
    setCallback10(s0, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cb250", int.class, ScriptState.classFor(BattleObject27c.class), BattleObject27c.class), TriFunctionRef::new));
    return 0;
  }

  @Method(0x800cc1ccL)
  public static long FUN_800cc1cc(final RunningScript a0) {
    final int scriptIndex1 = a0.params_20.get(0).deref().get();
    final int scriptIndex2 = a0.params_20.get(1).deref().get();
    final ScriptState<BattleObject27c> state1 = scriptStatePtrArr_800bc1c0.get(scriptIndex1).derefAs(ScriptState.classFor(BattleObject27c.class));
    final BattleObject27c bobj1 = state1.innerStruct_00.deref();
    final VECTOR vec = new VECTOR().set(bobj1._148.coord2_14.coord.transfer);

    if(scriptIndex2 >= 0) {
      final BattleObject27c bobj2 = scriptStatePtrArr_800bc1c0.get(scriptIndex2).deref().innerStruct_00.derefAs(BattleObject27c.class);
      vec.sub(bobj2._148.coord2_14.coord.transfer);
    }

    //LAB_800cc27c
    final int x = a0.params_20.get(3).deref().get() - vec.getX();
    final int z = a0.params_20.get(4).deref().get() - vec.getZ();
    state1.scriptIndex_c8.set(scriptIndex2);
    FUN_800cdc1c(state1, vec.getX(), vec.getY(), vec.getZ(), a0.params_20.get(3).deref().get(), vec.getY(), a0.params_20.get(4).deref().get(), 0, SquareRoot0(x * x + z * z) / a0.params_20.get(2).deref().get());
    setCallback10(scriptIndex1, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cb250", int.class, ScriptState.classFor(BattleObject27c.class), BattleObject27c.class), TriFunctionRef::new));
    return 0;
  }

  @Method(0x800cc364L)
  public static long FUN_800cc364(final RunningScript t1) {
    final int scriptIndex1 = t1.params_20.get(0).deref().get();
    final int scriptIndex2 = t1.params_20.get(1).deref().get();
    final ScriptState<BattleObject27c> state1 = scriptStatePtrArr_800bc1c0.get(scriptIndex1).derefAs(ScriptState.classFor(BattleObject27c.class));
    final BattleObject27c bobj1 = state1.innerStruct_00.deref();
    final VECTOR vec = new VECTOR().set(bobj1._148.coord2_14.coord.transfer);

    if(scriptIndex2 >= 0) {
      final BattleObject27c bobj2 = scriptStatePtrArr_800bc1c0.get(scriptIndex2).deref().innerStruct_00.derefAs(BattleObject27c.class);
      vec.sub(bobj2._148.coord2_14.coord.transfer);
    }

    //LAB_800cc3fc
    state1.scriptIndex_c8.set(scriptIndex2);
    FUN_800cdc1c(state1, vec.getX(), vec.getY(), vec.getZ(), t1.params_20.get(3).deref().get(), vec.getY(), t1.params_20.get(4).deref().get(), 0x20, t1.params_20.get(2).deref().get());
    setCallback10(scriptIndex1, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cb250", int.class, ScriptState.classFor(BattleObject27c.class), BattleObject27c.class), TriFunctionRef::new));
    return 0;
  }

  @Method(0x800cc46cL)
  public static long FUN_800cc46c(final RunningScript a0) {
    final int scriptIndex1 = a0.params_20.get(0).deref().get();
    final int scriptIndex2 = a0.params_20.get(1).deref().get();
    final ScriptState<BattleObject27c> s5 = scriptStatePtrArr_800bc1c0.get(scriptIndex1).derefAs(ScriptState.classFor(BattleObject27c.class));
    final BattleObject27c bobj1 = s5.innerStruct_00.derefAs(BattleObject27c.class);
    final VECTOR vec = new VECTOR().set(bobj1._148.coord2_14.coord.transfer);

    if(scriptIndex2 >= 0) {
      final BattleObject27c bobj2 = scriptStatePtrArr_800bc1c0.get(scriptIndex2).deref().innerStruct_00.derefAs(BattleObject27c.class);
      vec.sub(bobj2._148.coord2_14.coord.transfer);
    }

    //LAB_800cc51c
    final int x = a0.params_20.get(3).deref().get() - vec.getX();
    final int z = a0.params_20.get(4).deref().get() - vec.getZ();
    s5.scriptIndex_c8.set(scriptIndex2);
    FUN_800cdc1c(s5, vec.getX(), vec.getY(), vec.getZ(), a0.params_20.get(3).deref().get(), vec.getY(), a0.params_20.get(4).deref().get(), 0x20, SquareRoot0(x * x + z * z) / a0.params_20.get(2).deref().get());
    setCallback10(scriptIndex1, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cb250", int.class, ScriptState.classFor(BattleObject27c.class), BattleObject27c.class), TriFunctionRef::new));
    return 0;
  }

  @Method(0x800cc608L)
  public static long FUN_800cc608(final RunningScript a0) {
    final BattleObject27c s0 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    final BattleObject27c v0 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(1).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);

    s0._148.coord2Param_64.rotate.setY((short)(ratan2(v0._148.coord2_14.coord.transfer.getX() - s0._148.coord2_14.coord.transfer.getX(), v0._148.coord2_14.coord.transfer.getZ() - s0._148.coord2_14.coord.transfer.getZ()) + 0x800L));
    return 0;
  }

  @Method(0x800cc698L)
  public static long FUN_800cc698(final RunningScript a0) {
    final int scriptIndex1 = a0.params_20.get(0).deref().get();
    final int scriptIndex2 = a0.params_20.get(1).deref().get();
    final ScriptState<BattleObject27c> state1 = scriptStatePtrArr_800bc1c0.get(scriptIndex1).derefAs(ScriptState.classFor(BattleObject27c.class));
    final BattleObject27c bobj1 = state1.innerStruct_00.deref();
    final BattleObject27c bobj2 = scriptStatePtrArr_800bc1c0.get(scriptIndex2).deref().innerStruct_00.derefAs(BattleObject27c.class);
    final int s2 = a0.params_20.get(2).deref().get();
    int v0 = ratan2(bobj2._148.coord2_14.coord.transfer.getX() - bobj1._148.coord2_14.coord.transfer.getX(), bobj2._148.coord2_14.coord.transfer.getZ() - bobj1._148.coord2_14.coord.transfer.getZ()) - bobj1._148.coord2Param_64.rotate.getY() + 0x1000;
    v0 = v0 & 0xfff;
    v0 = v0 - 0x800;
    state1.scriptIndex_c8.set(scriptIndex2);
    state1._cc.set(s2);
    state1._d0.set(v0);
    state1._d4.set(v0 / s2);
    setCallback10(scriptIndex1, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cb34c", int.class, ScriptState.classFor(BattleObject27c.class), BattleObject27c.class), TriFunctionRef::new));
    return 0;
  }

  @Method(0x800cc784L)
  public static long FUN_800cc784(final RunningScript a0) {
    FUN_800ca418(scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class).combatantIndex_26c.get());
    return 0;
  }

  @Method(0x800cc7d8L)
  public static long FUN_800cc7d8(final RunningScript a0) {
    final ScriptState<?> v1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref();
    final long s2 = v1.ui_60.get() & 0x4L;
    final int s1 = v1.innerStruct_00.derefAs(BattleObject27c.class).combatantIndex_26c.get();

    //LAB_800cc83c
    for(int i = 0; i < combatantCount_800c66a0.get(); i++) {
      if(i != s1) {
        final CombatantStruct1a8 combatant = getCombatant(i);

        if((combatant.flags_19e.get() & 0x1L) != 0 && combatant._04.get() != 0 && combatant.charIndex_1a2.get() >= 0) {
          final int v0 = combatant.flags_19e.get() >>> 2 ^ 1;

          if(s2 == 0) {
            //LAB_800cc8ac
            if((v0 & 1) == 0) {
              //LAB_800cc8b4
              FUN_800ca418(i);
            }
          } else {
            if((v0 & 1) != 0) {
              FUN_800ca418(i);
            }
          }
        }
      }

      //LAB_800cc8bc
    }

    //LAB_800cc8d8
    return 0;
  }

  @Method(0x800cc8f4L)
  public static long FUN_800cc8f4(final RunningScript a0) {
    FUN_800c9708(scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class).combatantIndex_26c.get());
    return 0;
  }

  @Method(0x800cc948L)
  public static long FUN_800cc948(final RunningScript a0) {
    //LAB_800cc970
    for(int i = 0; i < combatantCount_800c66a0.get(); i++) {
      final CombatantStruct1a8 v1 = getCombatant(i);
      if((v1.flags_19e.get() & 0x1L) != 0 && v1.charIndex_1a2.get() >= 0) {
        FUN_800c9708(i);
      }

      //LAB_800cc9a8
    }

    //LAB_800cc9c0
    return 0;
  }

  @Method(0x800cc9d8L)
  public static long FUN_800cc9d8(final RunningScript a0) {
    scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class)._234.get(a0.params_20.get(1).deref().get()).set(a0.params_20.get(2).deref().get() > 0 ? 1 : 0);
    return 0;
  }

  @Method(0x800cca34L)
  public static long FUN_800cca34(final RunningScript s1) {
    if(_800c675c.get() != s1.params_20.get(0).deref().get() || (s1.scriptState_04.deref().ui_60.get() & 0x1000L) != 0) {
      //LAB_800cca7c
      final int a0 = s1.scriptStateIndex_00.get();
      final int a1 = s1.params_20.get(0).deref().get();
      final int a2;

      if(s1.paramCount_14.get() == 0x2L) {
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

  @Method(0x800ccb70L)
  public static long FUN_800ccb70(final RunningScript a0) {
    FUN_800f4268(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), 0xdL);
    return 0;
  }

  @Method(0x800ccba4L)
  public static long FUN_800ccba4(final RunningScript a0) {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref();
    final BattleObject27c bobj = state.innerStruct_00.derefAs(BattleObject27c.class);
    final CombatantStruct1a8 combatant = bobj.combatant_144.deref();
    final int combatantIndex = bobj.combatantIndex_26c.get();

    if((state.ui_60.get() & 0x1L) == 0) {
      if(bobj.animIndex_270.get() >= 0) {
        FUN_800ca194(combatantIndex, bobj.animIndex_270.get());
      }

      //LAB_800ccc24
      FUN_800c96ac(bobj._148, combatantIndex);
      FUN_800c9170(combatantIndex);

      if(a0.params_20.get(1).deref().get() != 0) {
        state.ui_60.or(0x3);
        combatant.charIndex_1a2.or(0x1);
      } else {
        //LAB_800ccc60
        state.ui_60.and(0xffff_fffd).or(0x1);
        combatant.charIndex_1a2.and(0xfffe);
      }

      //LAB_800ccc78
      FUN_800ca55c(combatantIndex);
      loadCombatantTmdAndAnims(combatantIndex);
      //LAB_800ccc94
    } else if((combatant.flags_19e.get() & 0x20L) == 0) {
      FUN_800c952c(bobj._148, combatantIndex);
      bobj.animIndex_26e.set((short)0);
      bobj.animIndex_270.set((short)-1);
      state.ui_60.and(0xffff_fffe);
      return 0;
    }

    //LAB_800ccccc
    return 0x2L;
  }

  @Method(0x800cccf4L)
  public static long FUN_800cccf4(final RunningScript a0) {
    a0.params_20.get(1).deref().set(scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class).charIndex_272.get());
    return 0;
  }

  @Method(0x800ccd34L)
  public static long FUN_800ccd34(final RunningScript a0) {
    int v1 = a0.params_20.get(1).deref().get();
    if(a0.params_20.get(2).deref().get() == 0x2L && v1 < 0) {
      v1 = 0;
    }

    //LAB_800ccd8c
    final BattleObject27c a1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    a1.all_04.get(a0.params_20.get(2).deref().get()).set((short)v1);
    return 0;
  }

  @Method(0x800ccda0L)
  public static long scriptSetStat(final RunningScript a0) {
    final BattleObject27c a1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    a1.all_04.get(Math.max(0, a0.params_20.get(2).deref().get())).set((short)a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800cce04L)
  public static long scriptGetStat(final RunningScript a0) {
    final BattleObject27c a1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);

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
    a0.params_20.get(2).deref().set(scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class).all_04.get(a0.params_20.get(1).deref().get()).get());
    return 0;
  }

  @Method(0x800ccec8L)
  public static long FUN_800ccec8(final RunningScript a0) {
    FUN_800f1a00(a0.params_20.get(0).deref().get() > 0 ? 1 : 0);
    return 0;
  }

  @Method(0x800ccef8L)
  public static long FUN_800ccef8(final RunningScript a0) {
    _800bc974.setu(0x3L);
    return 2;
  }

  @Method(0x800ccf0cL)
  public static long FUN_800ccf0c(final RunningScript a0) {
    _800bc974.setu(a0.params_20.get(0).deref().get());
    return 0x2L;
  }

  @Method(0x800ccf2cL)
  public static long FUN_800ccf2c(final RunningScript a0) {
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref();
    final BattleObject27c data = state.innerStruct_00.derefAs(BattleObject27c.class);

    long s0 = state.ui_60.get();
    if(a0.params_20.get(1).deref().get() != 0) {
      if((s0 & 0x40L) == 0) {
        s0 = s0 | 0x40L;

        if((s0 & 0x4L) != 0) {
          final long s1 = data.combatant_144.getPointer(); //TODO
          goldGainedFromCombat_800bc920.add((int)MEMORY.ref(2, s1).offset(0x196L).get());
          totalXpFromCombat_800bc95c.add((int)MEMORY.ref(2, s1).offset(0x194L).get());

          if((s0 & 0x2000L) == 0) {
            if(simpleRand() * 100 >> 16 < MEMORY.ref(1, s1).offset(0x198L).get()) {
              if(MEMORY.ref(1, s1).offset(0x199L).get() != 0xffL) {
                _800bc928.offset(_800bc978.get() * 0x4L).setu(MEMORY.ref(1, s1).offset(0x199L).get());
                _800bc978.addu(0x1L);
              }

              //LAB_800cd044
              s0 = s0 | 0x2000L;
            }
          }
        }
      }
    } else {
      //LAB_800cd04c
      s0 = s0 & 0xffff_ffbfL;
    }

    //LAB_800cd054
    state.ui_60.set(s0);
    FUN_800c7304();
    return 0;
  }

  @Method(0x800cd078L)
  public static long FUN_800cd078(final RunningScript a0) {
    final ScriptState<?> a1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref();

    //LAB_800cd0d0
    if(a0.params_20.get(1).deref().get() != 0) {
      a1.ui_60.or(0x40L);
    } else {
      //LAB_800cd0c4
      a1.ui_60.and(0xffff_ffbfL);
    }

    FUN_800c7304();
    return 0;
  }

  @Method(0x800cd0ecL)
  public static long FUN_800cd0ec(final RunningScript a0) {
    a0.params_20.get(3).deref().set(FUN_800c7488(
      scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class).charSlot_276.get(),
      a0.params_20.get(1).deref().get(),
      a0.params_20.get(2).deref().get()
    ));

    return 0;
  }

  @Method(0x800cd160L)
  public static long levelUpAddition(final RunningScript a0) {
    final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);

    if(a0.params_20.get(1).deref().get() != 0) {
      final int charIndex = bobj.charIndex_272.get();
      final CharacterData2c charData = gameState_800babc8.charData_32c.get(charIndex);

      final int additionIndex = charData.selectedAddition_19.get() - additionOffsets_8004f5ac.get(charIndex).get();
      if(charIndex == 2 || charIndex == 8 || additionIndex < 0) {
        //LAB_800cd200
        return 0;
      }

      //LAB_800cd208
      final int additionXp = Math.min(99, charData.additionXp_22.get(additionIndex).get() + 1);

      //LAB_800cd240
      //LAB_800cd288
      while(charData.additionLevels_1a.get(additionIndex).get() < 5 && additionXp >= additionNextLevelXp_800fa744.get(charData.additionLevels_1a.get(additionIndex).get()).get()) {
        charData.additionLevels_1a.get(additionIndex).incr();
      }

      //LAB_800cd2ac
      int nonMaxedAdditions = additionCounts_8004f5c0.get(charIndex).get();
      int firstNonMaxAdditionIndex = -1;

      // Find the first addition that isn't already maxed out
      //LAB_800cd2ec
      for(int additionIndex2 = 0; additionIndex2 < additionCounts_8004f5c0.get(charIndex).get(); additionIndex2++) {
        if(charData.additionLevels_1a.get(additionIndex2).get() == 5) {
          nonMaxedAdditions--;
        } else {
          //LAB_800cd308
          firstNonMaxAdditionIndex = additionIndex2;
        }

        //LAB_800cd30c
      }

      //LAB_800cd31c
      if(nonMaxedAdditions < 2 && (charData.partyFlags_04.get() & 0x40L) == 0) {
        charData.partyFlags_04.or(0x40L);

        if(firstNonMaxAdditionIndex >= 0) {
          charData.additionLevels_1a.get(firstNonMaxAdditionIndex).set(1);
        }

        //LAB_800cd36c
        _800bc910.offset(bobj.charSlot_276.get() * 0x4L).setu(0x1L);
      }

      //LAB_800cd390
      charData.additionXp_22.get(additionIndex).set(additionXp);
    }

    //LAB_800cd3ac
    return 0;
  }

  @Method(0x800cd3b4L)
  public static long FUN_800cd3b4(final RunningScript a0) {
    final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    if(a0.params_20.get(1).deref().get() < 32) {
      if(a0.params_20.get(2).deref().get() != 0) {
        bobj._23c.set(~(1L << a0.params_20.get(1).deref().get()) & bobj._23c.get());
      } else {
        //LAB_800cd420
        bobj._23c.or(1L << a0.params_20.get(1).deref().get());
      }
    } else {
      //LAB_800cd434
      final long v1 = a0.params_20.get(1).deref().get() - 32;
      if(a0.params_20.get(2).deref().get() != 0) {
        bobj._240.set(~(1L << v1) & bobj._240.get());
      } else {
        //LAB_800cd450
        bobj._240.or(1L << v1);
      }
    }

    //LAB_800cd460
    return 0;
  }

  @Method(0x800cd468L)
  public static long FUN_800cd468(final RunningScript a0) {
    a0.params_20.get(2).deref().set(FUN_800cac38(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get()));
    return 0;
  }

  @Method(0x800cd4b0L)
  public static long FUN_800cd4b0(final RunningScript a0) {
    final long v0 = FUN_800cad50(a0.params_20.get(0).deref().get());
    return MEMORY.ref(1, v0).offset(0x4L).get() == 1 ? 2 : 0;
  }

  @Method(0x800cd4f0L)
  public static long FUN_800cd4f0(final RunningScript a0) {
    FUN_800cad50(a0.params_20.get(0).deref().get());
    FUN_800cad64(a0.params_20.get(0).deref().get());
    return 0;
  }

  @Method(0x800cd52cL)
  public static long FUN_800cd52c(final RunningScript a0) {
    a0.params_20.get(1).deref().set(addCombatant(a0.params_20.get(0).deref().get(), -1));
    return 0;
  }

  @Method(0x800cd570L)
  public static long FUN_800cd570(final RunningScript a0) {
    FUN_800c9170(a0.params_20.get(0).deref().get());
    FUN_800c8fd4(a0.params_20.get(0).deref().get());
    return 0;
  }

  @Method(0x800cd5b4L)
  public static long FUN_800cd5b4(final RunningScript a0) {
    final int bobjIndex = allocateScriptState(0x27c, BattleObject27c::new);
    a0.params_20.get(2).deref().set(bobjIndex);
    final ScriptState<?> state = scriptStatePtrArr_800bc1c0.get(bobjIndex).deref();
    setCallback04(bobjIndex, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cae50", int.class, ScriptState.classFor(BattleObject27c.class), BattleObject27c.class), TriConsumerRef::new));
    setScriptDestructor(bobjIndex, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cb058", int.class, ScriptState.classFor(BattleObject27c.class), BattleObject27c.class), TriConsumerRef::new));
    loadScriptFile(bobjIndex, a0.scriptState_04.deref().scriptPtr_14.deref(), a0.params_20.get(0).deref().get(), "", 0); //TODO
    state.ui_60.or(0x804);
    _8006e398.bobjIndices_e0c.get(_800c66d0.get()).set(bobjIndex);
    _8006e398.bobjIndices_e50.get(_800c6768.get()).set(bobjIndex);

    final BattleObject27c bobj = state.innerStruct_00.derefAs(BattleObject27c.class);
    bobj.magic_00.set(BattleScriptDataBase.BOBJ);
    final CombatantStruct1a8 combatant = getCombatant(a0.params_20.get(1).deref().get());
    bobj.combatant_144.set(combatant);
    bobj.combatantIndex_26c.set((short)a0.params_20.get(1).deref().get());
    bobj.charIndex_272.set(combatant.charIndex_1a2.get());
    bobj._274.set((short)_800c66d0.get());
    _800c66d0.incr();
    bobj.charSlot_276.set((short)_800c6768.get());
    _800c6768.incr();
    bobj._148.coord2_14.coord.transfer.set(0, 0, 0);
    bobj._148.coord2Param_64.rotate.set((short)0, (short)0, (short)0);
    return 0;
  }

  @Method(0x800cd740L)
  public static long FUN_800cd740(final RunningScript a0) {
    final long v0 = FUN_800cad50(a0.params_20.get(0).deref().get());

    if(MEMORY.ref(1, v0).offset(0x4L).get() == 1) {
      //LAB_800cd794
      return 0x2L;
    }

    FUN_800c94f8(a0.params_20.get(0).deref().get(), (short)a0.params_20.get(1).deref().get());

    //LAB_800cd798
    return 0;
  }

  @Method(0x800cd7a8L)
  public static long FUN_800cd7a8(final RunningScript a0) {
    final long v0 = a0.params_20.get(0).deref().get();

    if(MEMORY.ref(1, v0).offset(0x4L).get() == 1) {
      //LAB_800cd7fc
      return 0x2L;
    }

    FUN_800ca528(a0.params_20.get(0).deref().get(), (short)a0.params_20.get(1).deref().get());

    //LAB_800cd800
    return 0;
  }

  @Method(0x800cd810L)
  public static long FUN_800cd810(final RunningScript a0) {
    final int s0 = a0.params_20.get(2).deref().get();

    if(s0 >= 0) {
      //LAB_800cd85c
      final long v0 = FUN_800cad50(s0);

      if(MEMORY.ref(1, v0).offset(0x4L).get() == 1) {
        return 0x2L;
      }

      FUN_800c9db8(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get(), s0);
    } else {
      FUN_800c9c7c(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get());
    }

    //LAB_800cd890
    return 0;
  }

  @Method(0x800cd8a4L)
  public static long FUN_800cd8a4(final RunningScript a0) {
    final long a1 = FUN_800cad50(a0.params_20.get(1).deref().get());

    if(MEMORY.ref(1, a1).offset(0x4L).get() == 1) {
      //LAB_800cd8fc
      return 0x2L;
    }

    FUN_800ca75c(a0.params_20.get(0).deref().get(), MEMORY.ref(4, a1).offset(0x0L).get());

    //LAB_800cd900
    return 0x1L;
  }

  @Method(0x800cd910L)
  public static long FUN_800cd910(final RunningScript a0) {
    a0.params_20.get(2).deref().set(FUN_800cac38(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get()));
    return 0;
  }

  @Method(0x800cd958L)
  public static long scriptGetCombatantIndex(final RunningScript a0) {
    a0.params_20.get(1).deref().set(scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class).combatantIndex_26c.get());
    return 0;
  }

  @Method(0x800cd998L)
  public static long FUN_800cd998(final RunningScript a0) {
    final BattleObject27c v1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);

    if(a0.params_20.get(2).deref().get() != 0) {
      a0.params_20.get(1).deref().set(v1.charSlot_276.get());
    } else {
      //LAB_800cd9e8
      a0.params_20.get(1).deref().set(v1._274.get());
    }

    //LAB_800cd9f4
    return 0;
  }

  @Method(0x800cd9fcL)
  public static long scriptGetBobjNobj(final RunningScript a0) {
    a0.params_20.get(1).deref().set((int)scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class)._148.ObjTable_0c.nobj.get());
    return 0;
  }

  @Method(0x800cda3cL)
  public static long FUN_800cda3c(final RunningScript a0) {
    FUN_800c9170(a0.params_20.get(0).deref().get());
    return 0;
  }

  @Method(0x800cda78L)
  public static long FUN_800cda78(final RunningScript a0) {
    final BattleObject27c bobj = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);

    if(FUN_800c90b0(bobj.combatantIndex_26c.get()) == 0) {
      return 2;
    }

    //LAB_800cdacc
    bobj.combatant_144.deref().charIndex_1a2.set((short)-1);

    if(bobj._278.get() != 0) {
      FUN_800c96ac(bobj._148, bobj.combatantIndex_26c.get());
    }

    //LAB_800cdaf4
    bobj.animIndex_26e.set((short)0);
    FUN_800c952c(bobj._148, bobj.combatantIndex_26c.get());

    //LAB_800cdb08
    return 0;
  }

  @Method(0x800cdb18L)
  public static long FUN_800cdb18(final RunningScript a0) {
    FUN_800c8ce4();
    FUN_800c8ed8();
    return 0;
  }

  @Method(0x800cdb44L)
  public static long scriptLoadStage(final RunningScript a0) {
    loadStage(a0.params_20.get(0).deref().get());
    return 0;
  }

  @Method(0x800cdb74L)
  public static long FUN_800cdb74(final RunningScript a0) {
    _800c66b9.setu(0x1L);

    //LAB_800cdbb8
    for(int i = 0; i < charCount_800c677c.get(); i++) {
      loadScriptFile(_8006e398.charBobjIndices_e40.get(i).get(), null, "", 0); //TODO
    }

    //LAB_800cdbe0
    if(!script_800c66fc.isNull()) {
      removeFromLinkedList(script_800c66fc.getPointer());
      script_800c66fc.clear();
    }

    //LAB_800cdc00
    return 0;
  }

  @Method(0x800cdc1cL)
  public static void FUN_800cdc1c(final ScriptState<BattleObject27c> s1, final int x, final int y, final int z, final int a4, final int a5, final int a6, final int a7, final int a8) {
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

    // Fix for retail /0 bug
    if(a8 > 0) {
      s1._dc.set(v0 / a8);
      s1._e4.set(s0 / a8);
    } else {
      s1._dc.set(v0 >= 0 ? -1 : 1);
      s1._e4.set(s0 >= 0 ? -1 : 1);
    }

    s1._e0.set(FUN_80013404(s3, v1, a8));
    s1._f4.set(s3);
  }

  @Method(0x800cdcecL)
  public static void FUN_800cdcec(final BigStruct a0, final int dobjIndex, final VECTOR largestVertRef, final VECTOR smallestVertRef, final EffectManagerData6c manager, final UnsignedShortRef largestIndexRef, final UnsignedShortRef smallestIndexRef) {
    short largest = 0x7fff;
    short smallest = -1;
    int largestIndex = 0;
    int smallestIndex = 0;
    final TmdObjTable v0 = a0.dobj2ArrPtr_00.deref().get(dobjIndex).tmd_08.deref();

    //LAB_800cdd24
    for(int i = 0; i < v0.n_vert_04.get(); i++) {
      final SVECTOR vert = v0.vert_top_00.deref().get(i);
      final ShortRef component = vert.component((int)manager._10._24.get());
      final short val = component.get();

      if(val <= largest) {
        largest = component.get();
        largestIndex = i;
        largestVertRef.set(vert);
        //LAB_800cdd7c
      } else if(val >= smallest) {
        smallest = component.get();
        smallestIndex = i;
        smallestVertRef.set(vert);
      }

      //LAB_800cddbc
    }

    //LAB_800cddcc
    largestIndexRef.set(largestIndex);
    smallestIndexRef.set(smallestIndex);
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
  public static void FUN_800cde94(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    final long v1;
    long a0;
    long a1;
    long a2;
    final long s1;
    long s4;

    final BttlScriptData6cSub3c s2 = data._44.derefAs(BttlScriptData6cSub3c.class);

    if(!s2._38.isNull()) {
      final SVECTOR sp0x18 = new SVECTOR().set(data._10.svec_1c);
      final SVECTOR sp0x20 = new SVECTOR().set(sp0x18).div(s2._0e.get());
      BttlScriptData6cSub3cSub2c s0 = s2._38.deref();

      final IntRef sp0x38 = new IntRef();
      final IntRef sp0x3c = new IntRef();
      FUN_800cf244(s0._04.get(0), sp0x38, sp0x3c);

      final IntRef sp0x40 = new IntRef();
      final IntRef sp0x44 = new IntRef();
      s1 = (int)FUN_800cf244(s0._04.get(1), sp0x40, sp0x44) / 0x4L;

      //LAB_800cdf94
      s0 = s0._24.derefNullable();
      for(s4 = 0; s4 < s2._0e.get() && s0 != null; s4++) {
        final IntRef sp0x28 = new IntRef();
        final IntRef sp0x2c = new IntRef();
        FUN_800cf244(s0._04.get(0), sp0x28, sp0x2c);
        final IntRef sp0x30 = new IntRef();
        final IntRef sp0x34 = new IntRef();
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
            a0 = s1 + 0xffeL - s1;
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
  public static void FUN_800ce254(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    long s5;
    long s6;

    final BttlScriptData6cSub3c s3 = data._44.derefAs(BttlScriptData6cSub3c.class);
    s3._00.incr();
    if(s3._00.get() == 0) {
      FUN_800cdcec(s3._30.deref(), s3.dobjIndex_08.get(), s3.largestVertex_20, s3.smallestVertex_10, data, s3.largestVertexIndex_0c, s3.smallestVertexIndex_0a);
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
      GsGetLw(s3._30.deref().coord2ArrPtr_04.deref().get(s3.dobjIndex_08.get()), sp0x20);
      final VECTOR sp0x40 = ApplyMatrixLV(sp0x20, s3.smallestVertex_10);
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

  @Method(0x800ce678L)
  public static void FUN_800ce678(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c data) {
    removeFromLinkedList(data._44.derefAs(BttlScriptData6cSub3c.class)._34.getPointer());
  }

  @Method(0x800ce6a8L)
  public static long FUN_800ce6a8(final RunningScript a0) {
    final int s4 = allocateEffectManager(a0.scriptStateIndex_00.get(), 0x3cL, MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800ce254", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new), MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cde94", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new), MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800ce678", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new), BttlScriptData6cSub3c::new);
    final EffectManagerData6c s1 = scriptStatePtrArr_800bc1c0.get(s4).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
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
    s0.dobjIndex_08.set((short)a0.params_20.get(2).deref().get());
    s0._0e.set(0x14);
    s1._10.svec_1c.set((short)255, (short)128, (short)96);
    final BattleScriptDataBase a0_0 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(1).deref().get()).deref().innerStruct_00.derefAs(BattleScriptDataBase.class);

    if(a0_0.magic_00.get() == BattleScriptDataBase.EM__) {
      s0._30.set(((EffectManagerData6c)a0_0)._44.derefAs(BttlScriptData6cSub13c.class)._10);
    } else {
      //LAB_800ce7f8
      s0._30.set(((BattleObject27c)a0_0)._148);
    }

    //LAB_800ce804
    a0.params_20.get(0).deref().set(s4);
    return 0;
  }

  @Method(0x800ce83cL)
  public static void FUN_800ce83c(final long a0, long a1) {
    final long v1 = scriptStatePtrArr_800bc1c0.get((int)a0).deref().innerStruct_00.derefAs(EffectManagerData6c.class)._44.getPointer(); //TODO
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

    sp0x10.set(
      sp0x00.getX() * a2 >> 12,
      sp0x00.getY() * a2 >> 12,
      sp0x00.getZ() * a2 >> 12
    );

    a0.set(a1).add(sp0x10);

    sp0x10.set(
      sp0x00.getX() * a3 >> 12,
      sp0x00.getY() * a3 >> 12,
      sp0x00.getZ() * a3 >> 12
    );

    a1.add(sp0x10);
  }

  @Method(0x800ce9b0L)
  public static long FUN_800ce9b0(final RunningScript a0) {
    final BttlScriptData6cSub3c a1 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(EffectManagerData6c.class)._44.derefAs(BttlScriptData6cSub3c.class);
    FUN_800ce880(a1.smallestVertex_10, a1.largestVertex_20, a0.params_20.get(2).deref().get(), a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800cea1cL)
  public static VECTOR FUN_800cea1c(final int scriptIndex, final VECTOR a1) {
    final ScriptState<BattleScriptDataBase> state = scriptStatePtrArr_800bc1c0.get(scriptIndex).derefAs(ScriptState.classFor(BattleScriptDataBase.class));
    final BattleScriptDataBase data = state.innerStruct_00.deref();

    final VECTOR s0;
    if(data.magic_00.get() == BattleScriptDataBase.EM__) {
      //LAB_800cea78
      s0 = ((EffectManagerData6c)data)._10.vec_04;
    } else {
      s0 = ((BattleObject27c)data)._148.coord2_14.coord.transfer;
    }

    a1.set(s0);

    //LAB_800cea8c
    return s0;
  }

  @Method(0x800cea9cL)
  public static void FUN_800cea9c(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final BttlScriptData6cSub0e effect = manager._44.derefAs(BttlScriptData6cSub0e.class);

    if(effect.scale_0c.get() != 0) {
      effect.svec_00.add(effect.svec_06);
      effect.scale_0c.decr();
    }

    //LAB_800ceb20
  }

  @Method(0x800ceb28L)
  public static void FUN_800ceb28(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    final BttlScriptData6cSub0e a0 = manager._44.derefAs(BttlScriptData6cSub0e.class);

    final long a1 = linkedListAddress_1f8003d8.get();
    MEMORY.ref(1, a1).offset(0x3L).setu(0x3L);
    MEMORY.ref(4, a1).offset(0x4L).setu(0x6280_8080L);
    MEMORY.ref(1, a1).offset(0x4L).setu(a0.svec_00.getX() >>> 8);
    MEMORY.ref(1, a1).offset(0x5L).setu(a0.svec_00.getY() >>> 8);
    MEMORY.ref(1, a1).offset(0x6L).setu(a0.svec_00.getZ() >>> 8);
    MEMORY.ref(1, a1).offset(0x7L).setu(0x62);
    MEMORY.ref(2, a1).offset(0x8L).setu(-160);
    MEMORY.ref(2, a1).offset(0xaL).setu(-120);
    MEMORY.ref(2, a1).offset(0xcL).setu(320);
    MEMORY.ref(2, a1).offset(0xeL).setu(280);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x78L, a1);
    linkedListAddress_1f8003d8.addu(0x10L);

    SetDrawMode(linkedListAddress_1f8003d8.deref(4).cast(DR_MODE::new), false, true, GetTPage(1, manager._10._00.get() >>> 28 & 0x3L, 0, 0), null);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x78L, linkedListAddress_1f8003d8.get());
    linkedListAddress_1f8003d8.addu(0xcL);
  }

  @Method(0x800cec84L)
  public static void FUN_800cec84(final int index, final ScriptState<EffectManagerData6c> state, final EffectManagerData6c manager) {
    // no-op
  }

  @Method(0x800cec8cL)
  public static long FUN_800cec8c(final RunningScript a0) {
    final short sp18 = (short)(a0.params_20.get(1).deref().get() << 8);
    final short sp1a = (short)(a0.params_20.get(2).deref().get() << 8);
    final short sp1c = (short)(a0.params_20.get(3).deref().get() << 8);
    final short sp20 = (short)(a0.params_20.get(4).deref().get() << 8);
    final short sp22 = (short)(a0.params_20.get(5).deref().get() << 8);
    final short sp24 = (short)(a0.params_20.get(6).deref().get() << 8);
    final short s1 = (short)a0.params_20.get(7).deref().get();

    final int managerIndex = allocateEffectManager(
      a0.scriptStateIndex_00.get(),
      0xe,
      MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cea9c", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800ceb28", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      MEMORY.ref(4, getMethodAddress(Bttl_800c.class, "FUN_800cec84", int.class, ScriptState.classFor(EffectManagerData6c.class), EffectManagerData6c.class), TriConsumerRef::new),
      BttlScriptData6cSub0e::new
    );

    final EffectManagerData6c manager = scriptStatePtrArr_800bc1c0.get(managerIndex).deref().innerStruct_00.derefAs(EffectManagerData6c.class);
    manager._10._00.set(0x5000_0000L);

    final BttlScriptData6cSub0e a2 = manager._44.derefAs(BttlScriptData6cSub0e.class);
    a2.svec_00.set(sp18, sp1a, sp1c);
    a2.svec_06.set((short)((sp20 - sp18) / s1), (short)((sp22 - sp1a) / s1), (short)((sp24 - sp1c) / s1));
    a2.scale_0c.set(s1);

    a0.params_20.get(0).deref().set(managerIndex);
    return 0;
  }

  @Method(0x800cee50L)
  public static long FUN_800cee50(final RunningScript script) {
    final int a2 = script.params_20.get(1).deref().get();
    script.params_20.get(0).deref().set((int)(seed_800fa754.advance().get() % (script.params_20.get(2).deref().get() - a2 + 1) + a2));
    return 0;
  }

  @Method(0x800ceeccL)
  public static long FUN_800ceecc(final RunningScript a0) {
    FUN_800ce83c(a0.params_20.get(0).deref().get(), a0.params_20.get(1).deref().get());
    return 0;
  }

  @Method(0x800cef00L)
  public static long FUN_800cef00(final RunningScript script) {
    final long a1 = linkedListAddress_1f8003d8.get();
    MEMORY.ref(1, a1).offset(0x3L).setu(0x3L);
    MEMORY.ref(1, a1).offset(0x4L).setu(script.params_20.get(0).deref().get());
    MEMORY.ref(1, a1).offset(0x5L).setu(script.params_20.get(1).deref().get());
    MEMORY.ref(1, a1).offset(0x6L).setu(script.params_20.get(2).deref().get());
    MEMORY.ref(1, a1).offset(0x7L).setu(0x62L);
    MEMORY.ref(2, a1).offset(0x8L).setu(-160);
    MEMORY.ref(2, a1).offset(0xaL).setu(-120);
    MEMORY.ref(2, a1).offset(0xcL).setu(320);
    MEMORY.ref(2, a1).offset(0xeL).setu(280);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x78L, a1);
    linkedListAddress_1f8003d8.addu(0x10L);

    SetDrawMode(linkedListAddress_1f8003d8.deref(4).cast(DR_MODE::new), false, true, GetTPage(1, script.params_20.get(3).deref().get() + 1, 0, 0), null);
    insertElementIntoLinkedList(tags_1f8003d0.getPointer() + 0x78L, linkedListAddress_1f8003d8.get());
    linkedListAddress_1f8003d8.addu(0xcL);
    return 0;
  }

  @Method(0x800cf244L)
  public static long FUN_800cf244(final VECTOR a0, final IntRef a1, final IntRef a2) {
    CPU.CTC2(matrix_800c3548.getPacked(0), 0);
    CPU.CTC2(matrix_800c3548.getPacked(2), 1);
    CPU.CTC2(matrix_800c3548.getPacked(4), 2);
    CPU.CTC2(matrix_800c3548.getPacked(6), 3);
    CPU.CTC2(matrix_800c3548.getPacked(8), 4);

    final SVECTOR a0s = new SVECTOR().set(a0);
    CPU.MTC2(a0s.getXY(), 0); // VXY0
    CPU.MTC2(a0s.getZ(),  1); // VZ0
    CPU.COP2(0x48_6012L); // MVMVA - multiply V0 by rotation matrix and add nothing

    final VECTOR sp0x10 = new VECTOR().set((int)CPU.MFC2(25), (int)CPU.MFC2(26), (int)CPU.MFC2(27));
    sp0x10.add(matrix_800c3548.transfer);
    a1.set(MathHelper.safeDiv(getProjectionPlaneDistance() * sp0x10.getX(), sp0x10.getZ()));
    a2.set(MathHelper.safeDiv(getProjectionPlaneDistance() * sp0x10.getY(), sp0x10.getZ()));
    return sp0x10.getZ();
  }

  @Method(0x800cf37cL)
  public static void FUN_800cf37c(final EffectManagerData6c a0, @Nullable final SVECTOR a1, final VECTOR a2, final VECTOR a3) {
    final VECTOR sp0x10 = new VECTOR();
    final SVECTOR sp0x20 = new SVECTOR();
    final MATRIX sp0x28 = new MATRIX();

    sp0x20.set(a0._10.svec_10);

    if(a1 != null) {
      //LAB_800cf3c4
      sp0x20.add(a1);
    }

    //LAB_800cf400
    FUN_80040980(sp0x20, sp0x28);
    TransMatrix(sp0x28, sp0x10);
    CPU.CTC2(sp0x28.getPacked(0), 0);
    CPU.CTC2(sp0x28.getPacked(2), 1);
    CPU.CTC2(sp0x28.getPacked(4), 2);
    CPU.CTC2(sp0x28.getPacked(6), 3);
    CPU.CTC2(sp0x28.getPacked(8), 4);
    CPU.CTC2(sp0x28.transfer.getX(), 5);
    CPU.CTC2(sp0x28.transfer.getY(), 6);
    CPU.CTC2(sp0x28.transfer.getZ(), 7);

    sp0x20.set(a2);
    CPU.MTC2(sp0x20.getXY(), 0);
    CPU.MTC2(sp0x20.getZ(),  1);
    CPU.COP2(0x480012L);
    sp0x10.setX((int)CPU.MFC2(25));
    sp0x10.setY((int)CPU.MFC2(26));
    sp0x10.setZ((int)CPU.MFC2(27));
    a3.set(sp0x10);
  }

  @Method(0x800cf4f4L)
  public static void FUN_800cf4f4(final EffectManagerData6c a0, @Nullable final SVECTOR a1, final VECTOR a2, final VECTOR a3) {
    final VECTOR sp0x10 = new VECTOR();
    final SVECTOR sp0x20 = new SVECTOR();
    final MATRIX sp0x28 = new MATRIX();

    sp0x20.set(a0._10.svec_10);

    if(a1 != null) {
      //LAB_800cf53c
      sp0x20.add(a1);
    }

    //LAB_800cf578
    sp0x10.set(a0._10.vec_04);
    FUN_80040980(sp0x20, sp0x28);
    TransMatrix(sp0x28, sp0x10);
    CPU.CTC2(sp0x28.getPacked(0), 0);
    CPU.CTC2(sp0x28.getPacked(2), 1);
    CPU.CTC2(sp0x28.getPacked(4), 2);
    CPU.CTC2(sp0x28.getPacked(6), 3);
    CPU.CTC2(sp0x28.getPacked(8), 4);
    CPU.CTC2(sp0x28.transfer.getX(), 5);
    CPU.CTC2(sp0x28.transfer.getY(), 6);
    CPU.CTC2(sp0x28.transfer.getZ(), 7);
    sp0x20.set(a2);
    CPU.MTC2(sp0x20.getXY(), 0);
    CPU.MTC2(sp0x20.getZ(),  1);
    CPU.COP2(0x480012L);
    sp0x10.setX((int)CPU.MFC2(25));
    sp0x10.setY((int)CPU.MFC2(26));
    sp0x10.setZ((int)CPU.MFC2(27));
    a3.set(sp0x10);
  }

  @Method(0x800cf684L)
  public static void FUN_800cf684(final SVECTOR a0, final VECTOR a1, final VECTOR a2, final VECTOR a3) {
    final SVECTOR sp0x20 = new SVECTOR().set(a0);
    final VECTOR sp0x10 = new VECTOR().set(a1);
    final MATRIX sp0x28 = new MATRIX();
    FUN_80040980(sp0x20, sp0x28);
    TransMatrix(sp0x28, sp0x10);
    CPU.CTC2(sp0x28.getPacked(0), 0);
    CPU.CTC2(sp0x28.getPacked(2), 1);
    CPU.CTC2(sp0x28.getPacked(4), 2);
    CPU.CTC2(sp0x28.getPacked(6), 3);
    CPU.CTC2(sp0x28.getPacked(8), 4);
    CPU.CTC2(sp0x28.transfer.getX(), 5);
    CPU.CTC2(sp0x28.transfer.getY(), 6);
    CPU.CTC2(sp0x28.transfer.getZ(), 7);
    sp0x20.set(a2);
    CPU.MTC2(sp0x20.getXY(), 0);
    CPU.MTC2(sp0x20.getZ(),  1);
    CPU.COP2(0x480012L);
    sp0x10.set((int)CPU.MFC2(25), (int)CPU.MFC2(26), (int)CPU.MFC2(27));
    a3.set(sp0x10);
  }

  @Method(0x800cf7d4L)
  public static int FUN_800cf7d4(final SVECTOR a0, final VECTOR a1, final VECTOR a2, final ShortRef outX, final ShortRef outY) {
    final SVECTOR sp0x30 = new SVECTOR().set(a1);
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
    outX.set(sp0x20.getX());
    outY.set(sp0x20.getY());
    return (int)CPU.MFC2(19); // SZ3
  }

  @Method(0x800cfb14L)
  public static int FUN_800cfb14(final EffectManagerData6c a0, final VECTOR a1, final ShortRef outX, final ShortRef outY) {
    final SVECTOR sp0x18 = new SVECTOR().set(a0._10.svec_10);
    final VECTOR sp0x20 = new VECTOR().set(a0._10.vec_04);
    return FUN_800cf7d4(sp0x18, sp0x20, a1, outX, outY);
  }

  @Method(0x800cfb94L)
  public static int FUN_800cfb94(final EffectManagerData6c a0, final SVECTOR a1, final VECTOR a2, final ShortRef outX, final ShortRef outY) {
    final SVECTOR sp0x18 = new SVECTOR().set(a0._10.svec_10).add(a1);
    final VECTOR sp0x20 = new VECTOR().set(a0._10.vec_04);
    return FUN_800cf7d4(sp0x18, sp0x20, a2, outX, outY);
  }

  @Method(0x800cfc20L)
  public static int FUN_800cfc20(final SVECTOR a0, final VECTOR a1, final VECTOR a2, final ShortRef outX, final ShortRef outY) {
    final SVECTOR sp0x18 = new SVECTOR().set(a0);
    final VECTOR sp0x20 = new VECTOR().set(a1);
    return FUN_800cf7d4(sp0x18, sp0x20, a2, outX, outY);
  }

  @Method(0x800cfcccL)
  public static long FUN_800cfccc(final RunningScript s0) {
    final ScriptState<?> a1 = scriptStatePtrArr_800bc1c0.get(s0.params_20.get(0).deref().get()).deref();
    final BattleScriptDataBase a0 = a1.innerStruct_00.derefAs(BattleScriptDataBase.class);

    final BigStruct v0;
    if(a0.magic_00.get() == BattleScriptDataBase.EM__) {
      v0 = ((EffectManagerData6c)a0)._44.derefAs(BttlScriptData6cSub13c.class)._10;
    } else {
      //LAB_800cfd34
      v0 = ((BattleObject27c)a0)._148;
    }

    //LAB_800cfd40
    final MATRIX sp0x10 = new MATRIX();
    GsGetLw(v0.coord2ArrPtr_04.deref().get(s0.params_20.get(1).deref().get()), sp0x10);
    final VECTOR sp0x40 = ApplyMatrixLV(sp0x10, new VECTOR());
    sp0x40.add(sp0x10.transfer);
    s0.params_20.get(2).deref().set(sp0x40.getX());
    s0.params_20.get(3).deref().set(sp0x40.getY());
    s0.params_20.get(4).deref().set(sp0x40.getZ());
    return 0;
  }

  @Method(0x800cfdf8L)
  public static long FUN_800cfdf8(final RunningScript a0) {
    final BattleObject27c v0 = scriptStatePtrArr_800bc1c0.get(a0.params_20.get(0).deref().get()).deref().innerStruct_00.derefAs(BattleObject27c.class);
    int a1 = v0._148.animCount_98.get() - 1;
    int a2 = 0x8000_0001;
    int a3 = 0x7fff_ffff;
    final UnboundedArrayRef<GsCOORDINATE2> t1 = v0._148.coord2ArrPtr_04.deref();
    final int t0 = a0.params_20.get(1).deref().get();

    //LAB_800cfe54
    for(; a1 >= 0; a1--) {
      final int v1 = t1.get(a1).coord.transfer.get(t0);

      if(a2 <= v1) {
        a2 = v1;
        //LAB_800cfe84
      } else if(a3 >= v1) {
        a3 = v1;
      }

      //LAB_800cfe90
    }

    //LAB_800cfe9c
    a0.params_20.get(2).deref().set(a2 - a3);
    return 0;
  }

  @Method(0x800cfed0L)
  public static void setMtSeed(final long seed) {
    seed_800fa754.set(seed ^ 0x75b_d924L);
  }

  @Method(0x800cff24L)
  public static long scriptSetMtSeed(final RunningScript a0) {
    setMtSeed(a0.params_20.get(0).deref().get());
    return 0;
  }

  /**
   * Holy crap this method was complicated... the way the stack is set up, all params after a3 are part of the sp0x90 array. Count is the number of parameters.
   * It's basically a variadic method so I'm changing the signature to that.
   * <p>
   * This method allows you to call a script function from the main game engine. Variadic params get passed in as the param array.
   */
  @Method(0x800cff54L)
  public static long callScriptFunction(final long func, final int... params) {
    final Memory.TemporaryReservation tmp = MEMORY.temp(0x44);
    final RunningScript sp0x10 = new RunningScript(tmp.get());
    final IntRef[] sp0x58 = new IntRef[params.length];

    //LAB_800cff90
    for(int i = 0; i < params.length; i++) {
      sp0x58[i] = new IntRef().set(params[i]);
      sp0x10.params_20.get(i).set(sp0x58[i]);
    }

    //LAB_800cffbc
    MEMORY.ref(4, func).call(sp0x10);
    tmp.release();

    return sp0x58[0].get();
  }

  @Method(0x800cffd8L)
  public static void FUN_800cffd8(final int scriptIndex, final VECTOR a1, final int animIndex) {
    final MATRIX sp0x10 = new MATRIX();
    GsGetLw(scriptStatePtrArr_800bc1c0.get(scriptIndex).deref().innerStruct_00.derefAs(BattleObject27c.class)._148.coord2ArrPtr_04.deref().get(animIndex), sp0x10);
    a1.set(ApplyMatrixLV(sp0x10, new VECTOR()));
    a1.add(sp0x10.transfer);
  }
}
