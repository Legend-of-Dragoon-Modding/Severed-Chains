package legend.game;

import legend.core.MathHelper;
import legend.core.gte.COLOUR;
import legend.core.gte.MATRIX;
import legend.core.gte.SVECTOR;
import legend.core.gte.VECTOR;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.core.spu.Voice;
import legend.game.combat.Bttl_800c;
import legend.game.combat.Bttl_800d;
import legend.game.combat.Bttl_800e;
import legend.game.combat.Bttl_800f;
import legend.game.combat.SEffe;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptFile;
import legend.game.sound.PatchList;
import legend.game.sound.PlayableSound0c;
import legend.game.sound.PlayingNote66;
import legend.game.sound.SequenceData124;
import legend.game.sound.SoundEnv44;
import legend.game.sound.Sshd;
import legend.game.sound.Sssq;
import legend.game.sound.SssqReader;
import legend.game.sound.Sssqish;
import legend.game.sound.VolumeRamp;
import legend.game.title.GameOver;
import legend.game.title.Ttle;
import legend.game.types.CallbackStruct;
import legend.game.types.EngineState;
import legend.game.types.ItemStats0c;
import legend.game.types.MoonMusic08;
import legend.game.types.Struct10;
import legend.game.types.SubmapMusic08;
import legend.game.unpacker.FileData;
import legend.game.wmap.WMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.Function;

import static legend.core.GameEngine.CPU;
import static legend.core.GameEngine.MEMORY;
import static legend.core.GameEngine.SEQUENCER;
import static legend.core.GameEngine.SPU;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.sin;
import static legend.game.Scus94491BpeSegment_8005.atanTable_80058d0c;
import static legend.game.Scus94491BpeSegment_8005.reverbConfigs_80059f7c;
import static legend.game.Scus94491BpeSegment_800c.patchList_800c4abc;
import static legend.game.Scus94491BpeSegment_800c.playableSounds_800c43d0;
import static legend.game.Scus94491BpeSegment_800c.playingNotes_800c3a40;
import static legend.game.Scus94491BpeSegment_800c.sequenceData_800c4ac8;
import static legend.game.Scus94491BpeSegment_800c.soundEnv_800c6630;
import static legend.game.Scus94491BpeSegment_800c.spuDmaCompleteCallback_800c6628;
import static legend.game.Scus94491BpeSegment_800c.sshdPtr_800c4ac0;
import static legend.game.Scus94491BpeSegment_800c.sssqChannelInfo_800C6680;
import static legend.game.Scus94491BpeSegment_800c.sssqReader_800c667c;
import static legend.game.Scus94491BpeSegment_800c.sssqish_800c4aa8;
import static legend.game.Scus94491BpeSegment_800c.volumeRamp_800c4ab0;

public final class Scus94491BpeSegment_8004 {
  private Scus94491BpeSegment_8004() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491BpeSegment_8004.class);
  private static final Marker SEQUENCER_MARKER = MarkerManager.getMarker("SEQUENCER");

  public static final String[] supportOverlays_8004db88 = {
    "\\OVL\\S_INIT.OV_",
    "\\OVL\\S_BTLD.OV_",
    "\\OVL\\S_ITEM.OV_",
    "\\OVL\\S_EFFE.OV_",
    "\\OVL\\S_STRM.OV_",
  };

  /**
   * <ol start="0">
   *   <li>{@link Scus94491BpeSegment_800e#preload()}</li>
   *   <li>finalizePregameLoading</li>
   *   <li>{@link Ttle#executeTtleLoadingStage()}</li>
   *   <li>{@link Ttle#executeTtleUnloadingStage()}</li>
   *   <li>{@link SMap#theEnd}</li>
   *   <li>{@link SMap#executeSmapLoadingStage()} Sets up rendering and loads scene</li>
   *   <li>{@link Scus94491BpeSegment#FUN_80018658()}</li>
   *   <li>{@link GameOver#gameOver()}</li>
   *   <li>{@link WMap#FUN_800cc738()}</li>
   *   <li>{@link SMap#startFmvLoadingStage()}</li>
   *   <li>swapDiskLoadingStage</li>
   *   <li>{@link SMap#FUN_800d9e08()}</li>
   *   <li>0x800c6eb8 (TODO)</li>
   *   <li>0x800cab8c (TODO)</li>
   *   <li>null</li>
   *   <li>0x800c6978 (TODO)</li>
   *   <li>null</li>
   *   <li>0x800cdcdc (TODO)</li>
   *   <li>0x800cabd4 (TODO)</li>
   *   <li>null</li>
   * </ol>
   */
  public static final CallbackStruct[] gameStateCallbacks_8004dbc0 = new CallbackStruct[20];
  static {
    gameStateCallbacks_8004dbc0[0] = new CallbackStruct(Scus94491BpeSegment_800e::preload, null, 0, 0);
//    gameStateCallbacks_8004dbc0[1] = new CallbackStruct(finalizePregameLoading);
    gameStateCallbacks_8004dbc0[2] = new CallbackStruct(Ttle::executeTtleLoadingStage, "\\OVL\\TTLE.OV_", 0x800c6690L, 0xf0);
    gameStateCallbacks_8004dbc0[3] = new CallbackStruct(Ttle::executeTtleUnloadingStage, "\\OVL\\TTLE.OV_", 0x800c6690L, 0xf0);
    gameStateCallbacks_8004dbc0[4] = new CallbackStruct(SMap::theEnd, "\\OVL\\SMAP.OV_", 0x800c6690L, 0xf9f0);
    gameStateCallbacks_8004dbc0[5] = new CallbackStruct(SMap::executeSmapLoadingStage, "\\OVL\\SMAP.OV_", 0x800c6690L, 0xf9f0);
    gameStateCallbacks_8004dbc0[6] = new CallbackStruct(Scus94491BpeSegment::FUN_80018658, "\\OVL\\BTTL.OV_", 0x800c6690L, 0x668);
    gameStateCallbacks_8004dbc0[7] = new CallbackStruct(GameOver::gameOver, "\\OVL\\TTLE.OV_", 0x800c6690L, 0xf0);
    gameStateCallbacks_8004dbc0[8] = new CallbackStruct(WMap::FUN_800cc738, "\\OVL\\WMAP.OV_", 0x800c6690L, 0x2070);
    gameStateCallbacks_8004dbc0[9] = new CallbackStruct(SMap::startFmvLoadingStage, "\\OVL\\SMAP.OV_", 0x800c6690L, 0xf9f0);
//    gameStateCallbacks_8004dbc0[10] = new CallbackStruct(swapDiskLoadingStage);
    gameStateCallbacks_8004dbc0[11] = new CallbackStruct(SMap::FUN_800d9e08, "\\OVL\\SMAP.OV_", 0x800c6690L, 0xf9f0);
//    gameStateCallbacks_8004dbc0[12] = new CallbackStruct(0x800c6eb8);
//    gameStateCallbacks_8004dbc0[13] = new CallbackStruct(0x800cab8c);
//    gameStateCallbacks_8004dbc0[14] = new CallbackStruct(null);
//    gameStateCallbacks_8004dbc0[15] = new CallbackStruct(0x800c6978);
//    gameStateCallbacks_8004dbc0[16] = new CallbackStruct(null);
//    gameStateCallbacks_8004dbc0[17] = new CallbackStruct(0x800cdcdc);
//    gameStateCallbacks_8004dbc0[18] = new CallbackStruct(0x800cabd4);
//    gameStateCallbacks_8004dbc0[19] = new CallbackStruct(null);
  }

  public static String currentlyLoadingFileEntry_8004dd04;
  public static final Value _8004dd0c = MEMORY.ref(4, 0x8004dd0cL);
  public static int loadedOverlayIndex_8004dd10;

  /**
   * <ol>
   *   <li value="5">SMAP</li>
   *   <li value="6">Combat</li>
   *   <li value="7">Game over</li>
   *   <li value="8">WMAP</li>
   *   <li value="9">FMV?</li>
   *   <li value="11">Credits?</li>
   * </ol>
   */
  public static EngineState engineState_8004dd20 = EngineState.PRELOAD_00;
  /** When the overlay finishes loading, switch to this */
  public static EngineState engineStateOnceLoaded_8004dd24 = EngineState.PRELOAD_00;
  /** The previous state before the file finished loading */
  public static EngineState previousEngineState_8004dd28;

  public static int width_8004dd34 = 320;
  public static int height_8004dd34 = 240;

  public static final UnsignedShortRef reinitOrderingTableBits_8004dd38 = MEMORY.ref(2, 0x8004dd38L, UnsignedShortRef::new);

  public static Runnable syncFrame_8004dd3c;
  public static Runnable swapDisplayBuffer_8004dd40;
  public static final Value simpleRandSeed_8004dd44 = MEMORY.ref(4, 0x8004dd44L);
  public static final Value _8004dd48 = MEMORY.ref(2, 0x8004dd48L);

  public static final BoolRef preloadingAudioAssets_8004ddcc = MEMORY.ref(1, 0x8004ddccL, BoolRef::new);

  public static final Function<RunningScript, FlowControl>[] scriptSubFunctions_8004e29c = new Function[1024];
  static {
    Arrays.setAll(scriptSubFunctions_8004e29c, i -> Scus94491BpeSegment::scriptRewindAndPause2);

    scriptSubFunctions_8004e29c[0] = Scus94491BpeSegment::scriptSetIndicatorsDisabled;
    scriptSubFunctions_8004e29c[1] = Scus94491BpeSegment::scriptReadIndicatorsDisabled;
    scriptSubFunctions_8004e29c[2] = Scus94491BpeSegment::scriptSetGlobalFlag1;
    scriptSubFunctions_8004e29c[3] = Scus94491BpeSegment::scriptReadGlobalFlag1;
    scriptSubFunctions_8004e29c[4] = Scus94491BpeSegment::scriptSetGlobalFlag2;
    scriptSubFunctions_8004e29c[5] = Scus94491BpeSegment::scriptReadGlobalFlag2;
    scriptSubFunctions_8004e29c[6] = Scus94491BpeSegment::scriptStartEffect;
    scriptSubFunctions_8004e29c[7] = Scus94491BpeSegment::scriptWaitForFilesToLoad;
    scriptSubFunctions_8004e29c[8] = Scus94491BpeSegment::FUN_80017584;
    scriptSubFunctions_8004e29c[9] = Scus94491BpeSegment::FUN_800175b4;
    scriptSubFunctions_8004e29c[10] = Scus94491BpeSegment::FUN_80017648;
    scriptSubFunctions_8004e29c[11] = Scus94491BpeSegment::FUN_80017688;
    scriptSubFunctions_8004e29c[12] = SMap::FUN_800d9bc0;
    scriptSubFunctions_8004e29c[13] = SMap::FUN_800d9bf4;
    scriptSubFunctions_8004e29c[14] = SMap::FUN_800d9c1c;
    scriptSubFunctions_8004e29c[15] = SMap::scriptSetCharAddition;
    scriptSubFunctions_8004e29c[16] = Scus94491BpeSegment::FUN_800176c0;
    scriptSubFunctions_8004e29c[17] = Scus94491BpeSegment::FUN_800176ec;
    scriptSubFunctions_8004e29c[18] = SMap::scriptGetCharAddition;
    scriptSubFunctions_8004e29c[19] = SMap::FUN_800d9d60;

    scriptSubFunctions_8004e29c[32] = Bttl_800d::FUN_800dabcc;
    scriptSubFunctions_8004e29c[33] = Bttl_800d::FUN_800dac20;
    scriptSubFunctions_8004e29c[34] = Bttl_800d::FUN_800db034;
    scriptSubFunctions_8004e29c[35] = Bttl_800d::FUN_800db460;
    scriptSubFunctions_8004e29c[36] = Bttl_800d::FUN_800db574;
    scriptSubFunctions_8004e29c[37] = Bttl_800d::FUN_800db688;
    scriptSubFunctions_8004e29c[38] = Bttl_800d::FUN_800db79c;
    scriptSubFunctions_8004e29c[39] = Bttl_800d::FUN_800db8b0;
    scriptSubFunctions_8004e29c[40] = Bttl_800d::FUN_800db9e0;
    scriptSubFunctions_8004e29c[41] = Bttl_800d::FUN_800dbb10;
    scriptSubFunctions_8004e29c[42] = Bttl_800d::FUN_800dc2d8;
    scriptSubFunctions_8004e29c[43] = Bttl_800d::FUN_800dbb9c;
    scriptSubFunctions_8004e29c[44] = Bttl_800d::FUN_800dcbec;
    scriptSubFunctions_8004e29c[45] = Bttl_800d::FUN_800dcb84;
    scriptSubFunctions_8004e29c[46] = Bttl_800d::scriptSetViewportTwist;
    scriptSubFunctions_8004e29c[47] = Bttl_800d::FUN_800dbc80;
    scriptSubFunctions_8004e29c[48] = Bttl_800d::FUN_800dbcc8;
    scriptSubFunctions_8004e29c[49] = Bttl_800d::scriptGetProjectionPlaneDistance;
    scriptSubFunctions_8004e29c[50] = Bttl_800d::FUN_800d8dec;

    scriptSubFunctions_8004e29c[96] = SMap::FUN_800df168;
    scriptSubFunctions_8004e29c[97] = SMap::FUN_800df198;
    scriptSubFunctions_8004e29c[98] = SMap::FUN_800df1c8;
    scriptSubFunctions_8004e29c[99] = SMap::FUN_800df1f8;
    scriptSubFunctions_8004e29c[100] = SMap::FUN_800df228;
    scriptSubFunctions_8004e29c[101] = SMap::scriptSetModelPosition;
    scriptSubFunctions_8004e29c[102] = SMap::scriptReadModelPosition;
    scriptSubFunctions_8004e29c[103] = SMap::scriptSetModelRotate;
    scriptSubFunctions_8004e29c[104] = SMap::scriptReadModelRotate;
    scriptSubFunctions_8004e29c[105] = SMap::FUN_800df3d0;
    scriptSubFunctions_8004e29c[106] = SMap::FUN_800df410;
    scriptSubFunctions_8004e29c[107] = SMap::FUN_800df440;
    scriptSubFunctions_8004e29c[108] = SMap::FUN_800df488;
    scriptSubFunctions_8004e29c[109] = SMap::FUN_800df4d0;
    scriptSubFunctions_8004e29c[110] = SMap::FUN_800df500;
    scriptSubFunctions_8004e29c[111] = SMap::FUN_800df530;
    scriptSubFunctions_8004e29c[112] = SMap::FUN_800df560;
    scriptSubFunctions_8004e29c[113] = SMap::FUN_800df5c0;
    scriptSubFunctions_8004e29c[114] = SMap::FUN_800df590;
    scriptSubFunctions_8004e29c[115] = SMap::FUN_800df5f0;
    scriptSubFunctions_8004e29c[116] = SMap::FUN_800df620;
    scriptSubFunctions_8004e29c[117] = SMap::FUN_800df650;
    scriptSubFunctions_8004e29c[118] = SMap::FUN_800df680;
    scriptSubFunctions_8004e29c[119] = SMap::FUN_800df6a4;
    scriptSubFunctions_8004e29c[120] = SMap::scriptRotateSobj;
    scriptSubFunctions_8004e29c[121] = SMap::scriptRotateSobjAbsolute;
    scriptSubFunctions_8004e29c[122] = SMap::FUN_800df904;
    scriptSubFunctions_8004e29c[123] = SMap::FUN_800de1d0;
    scriptSubFunctions_8004e29c[124] = SMap::scriptFacePlayer;
    scriptSubFunctions_8004e29c[125] = SMap::FUN_800df9a8;
    scriptSubFunctions_8004e29c[126] = SMap::FUN_800dfb28;
    scriptSubFunctions_8004e29c[127] = SMap::FUN_800dfb44;

    scriptSubFunctions_8004e29c[128] = Bttl_800c::scriptSetBobjPos;
    scriptSubFunctions_8004e29c[129] = Bttl_800c::scriptGetBobjPos;
    scriptSubFunctions_8004e29c[130] = Bttl_800c::scriptSetBobjRotation;
    scriptSubFunctions_8004e29c[131] = Bttl_800c::FUN_800cc7d8;
    scriptSubFunctions_8004e29c[132] = Bttl_800c::scriptSetBobjRotationY;
    scriptSubFunctions_8004e29c[133] = Bttl_800c::FUN_800cc948;
    scriptSubFunctions_8004e29c[134] = Bttl_800c::scriptGetBobjRotation;
    scriptSubFunctions_8004e29c[135] = Scus94491BpeSegment::scriptRewindAndPause2;
    scriptSubFunctions_8004e29c[136] = Bttl_800c::scriptGetMonsterStatusResistFlags;
    scriptSubFunctions_8004e29c[137] = Scus94491BpeSegment::scriptRewindAndPause2;
    scriptSubFunctions_8004e29c[138] = Bttl_800c::FUN_800cb618;
    scriptSubFunctions_8004e29c[139] = Bttl_800c::FUN_800cb674;
    scriptSubFunctions_8004e29c[140] = Bttl_800c::FUN_800cb6bc;
    scriptSubFunctions_8004e29c[141] = Bttl_800c::FUN_800cb764;
    scriptSubFunctions_8004e29c[142] = Bttl_800c::FUN_800cb76c;
    scriptSubFunctions_8004e29c[143] = Bttl_800c::FUN_800cb9b0;
    scriptSubFunctions_8004e29c[144] = Bttl_800c::scriptPauseAnimation;
    scriptSubFunctions_8004e29c[145] = Bttl_800c::scriptResumeAnimation;
    scriptSubFunctions_8004e29c[146] = Bttl_800c::scriptSetBobjAnimationLoopState;
    scriptSubFunctions_8004e29c[147] = Bttl_800c::scriptAnimationHasFinished;
    scriptSubFunctions_8004e29c[148] = Bttl_800c::FUN_800cbb00;
    scriptSubFunctions_8004e29c[149] = Bttl_800c::FUN_800cbc14;
    scriptSubFunctions_8004e29c[150] = Bttl_800c::FUN_800cbde0;
    scriptSubFunctions_8004e29c[151] = Bttl_800c::FUN_800cbef8;
    scriptSubFunctions_8004e29c[152] = Bttl_800c::FUN_800cc0c8;
    scriptSubFunctions_8004e29c[153] = Bttl_800c::FUN_800cc1cc;
    scriptSubFunctions_8004e29c[154] = Bttl_800c::FUN_800cc364;
    scriptSubFunctions_8004e29c[155] = Bttl_800c::FUN_800cc46c;
    scriptSubFunctions_8004e29c[156] = Bttl_800c::scriptBobjLookAtBobj;
    scriptSubFunctions_8004e29c[157] = Bttl_800c::FUN_800cc698;
    scriptSubFunctions_8004e29c[158] = Bttl_800c::FUN_800cc784;
    scriptSubFunctions_8004e29c[159] = Bttl_800c::scriptLoadAttackAnimations;
    scriptSubFunctions_8004e29c[160] = Bttl_800c::scriptSetUpAndHandleCombatMenu;
    scriptSubFunctions_8004e29c[161] = Scus94491BpeSegment::scriptRewindAndPause2;
    scriptSubFunctions_8004e29c[162] = Scus94491BpeSegment::scriptRewindAndPause2;
    scriptSubFunctions_8004e29c[163] = Scus94491BpeSegment::scriptRewindAndPause2;
    scriptSubFunctions_8004e29c[164] = Bttl_800c::scriptRenderDamage;
    scriptSubFunctions_8004e29c[165] = Bttl_800c::scriptAddFloatingNumberForBobj;
    scriptSubFunctions_8004e29c[166] = Bttl_800c::FUN_800ccba4;
    scriptSubFunctions_8004e29c[167] = Bttl_800c::scriptGetCharOrMonsterId;
    scriptSubFunctions_8004e29c[168] = Bttl_800c::scriptSetBobjStat;
    scriptSubFunctions_8004e29c[169] = Bttl_800c::scriptGetBobjStat;
    scriptSubFunctions_8004e29c[170] = Bttl_800c::scriptSetPostBattleAction;
    scriptSubFunctions_8004e29c[171] = Bttl_800c::FUN_800ccec8;
    scriptSubFunctions_8004e29c[172] = Bttl_800c::FUN_800ccef8;
    scriptSubFunctions_8004e29c[173] = Bttl_800c::scriptSetBobjDeadAndDropLoot;
    scriptSubFunctions_8004e29c[174] = Bttl_800c::scriptGetHitProperty;
    scriptSubFunctions_8004e29c[175] = Bttl_800c::scriptSetBobjDead;
    scriptSubFunctions_8004e29c[176] = Bttl_800c::levelUpAddition;
    scriptSubFunctions_8004e29c[177] = Bttl_800c::scriptGetBobjStat2;
    scriptSubFunctions_8004e29c[178] = Bttl_800c::scriptSetBobjRawStat;

    scriptSubFunctions_8004e29c[192] = Scus94491BpeSegment_8002::scriptGetFreeTextboxIndex;
    scriptSubFunctions_8004e29c[193] = Scus94491BpeSegment_8002::FUN_80029bd4;
    scriptSubFunctions_8004e29c[194] = Scus94491BpeSegment_8002::FUN_80025158;
    scriptSubFunctions_8004e29c[195] = Scus94491BpeSegment_8002::FUN_80029c98;
    scriptSubFunctions_8004e29c[196] = Scus94491BpeSegment_8002::FUN_80029cf4;
    scriptSubFunctions_8004e29c[197] = Scus94491BpeSegment_8002::FUN_80029d34;
    scriptSubFunctions_8004e29c[198] = Scus94491BpeSegment_8002::scriptAddSobjTextbox;
    scriptSubFunctions_8004e29c[199] = Scus94491BpeSegment_8002::FUN_80029e8c;
    scriptSubFunctions_8004e29c[200] = Scus94491BpeSegment_8002::FUN_800254bc;
    scriptSubFunctions_8004e29c[201] = Scus94491BpeSegment_8002::FUN_80029d6c;
    scriptSubFunctions_8004e29c[202] = Scus94491BpeSegment_8002::FUN_80029e04;
    scriptSubFunctions_8004e29c[203] = Scus94491BpeSegment_8002::FUN_80029ecc;
    scriptSubFunctions_8004e29c[204] = Scus94491BpeSegment_8002::FUN_80028ff8;
    scriptSubFunctions_8004e29c[205] = Scus94491BpeSegment_8002::FUN_80029f48;
    scriptSubFunctions_8004e29c[206] = Scus94491BpeSegment_8002::FUN_80029f80;
    scriptSubFunctions_8004e29c[207] = Scus94491BpeSegment_8002::FUN_80025718;

    scriptSubFunctions_8004e29c[224] = Scus94491BpeSegment::FUN_8001e640;
    scriptSubFunctions_8004e29c[225] = Scus94491BpeSegment::FUN_8001e918;
    scriptSubFunctions_8004e29c[226] = Scus94491BpeSegment::FUN_8001e920;
    scriptSubFunctions_8004e29c[227] = Scus94491BpeSegment::FUN_8001eb30;
    scriptSubFunctions_8004e29c[228] = Scus94491BpeSegment::scriptLoadBattleCutsceneSounds;
    scriptSubFunctions_8004e29c[229] = Scus94491BpeSegment::scriptLoadMonsterAttackSounds;
    scriptSubFunctions_8004e29c[230] = Scus94491BpeSegment::scriptLoadMusicPackage;
    scriptSubFunctions_8004e29c[231] = Scus94491BpeSegment::FUN_8001fe28;
    scriptSubFunctions_8004e29c[232] = Scus94491BpeSegment::scriptUnloadSoundFile;
    scriptSubFunctions_8004e29c[233] = Scus94491BpeSegment_8002::FUN_8002013c;
    scriptSubFunctions_8004e29c[234] = Scus94491BpeSegment_8002::FUN_80020230;
    scriptSubFunctions_8004e29c[235] = Scus94491BpeSegment_8002::FUN_800202a4;
    scriptSubFunctions_8004e29c[236] = Scus94491BpeSegment::scriptPlaySound;
    scriptSubFunctions_8004e29c[237] = Scus94491BpeSegment::FUN_8001ab98;
    scriptSubFunctions_8004e29c[238] = Scus94491BpeSegment::scriptPlayBobjSound;
    scriptSubFunctions_8004e29c[239] = Scus94491BpeSegment::FUN_8001ac48;
    scriptSubFunctions_8004e29c[240] = Scus94491BpeSegment::FUN_8001ad5c;
    scriptSubFunctions_8004e29c[241] = Scus94491BpeSegment::FUN_8001adc8;
    scriptSubFunctions_8004e29c[242] = Scus94491BpeSegment::FUN_8001ae18;
    scriptSubFunctions_8004e29c[243] = Scus94491BpeSegment::FUN_8001ae68;
    scriptSubFunctions_8004e29c[244] = Scus94491BpeSegment::FUN_8001aec8;
    scriptSubFunctions_8004e29c[245] = Scus94491BpeSegment::FUN_8001af34;
    scriptSubFunctions_8004e29c[246] = Scus94491BpeSegment::FUN_8001afa4;
    scriptSubFunctions_8004e29c[247] = Scus94491BpeSegment::FUN_8001b014;
    scriptSubFunctions_8004e29c[248] = Scus94491BpeSegment::FUN_8001b094;
    scriptSubFunctions_8004e29c[249] = Scus94491BpeSegment::FUN_8001b134;
    scriptSubFunctions_8004e29c[250] = Scus94491BpeSegment::FUN_8001b13c;
    scriptSubFunctions_8004e29c[251] = Scus94491BpeSegment::FUN_8001b144;
    scriptSubFunctions_8004e29c[252] = Scus94491BpeSegment::scriptSetMainVolume;
    scriptSubFunctions_8004e29c[253] = Scus94491BpeSegment::scriptSetSequenceVolume;
    scriptSubFunctions_8004e29c[254] = Scus94491BpeSegment::scriptSetAllSoundSequenceVolumes;
    scriptSubFunctions_8004e29c[255] = Scus94491BpeSegment::scriptSssqFadeIn;

    scriptSubFunctions_8004e29c[256] = SMap::FUN_800e67d4;
    scriptSubFunctions_8004e29c[257] = SMap::FUN_800e68b4;
    scriptSubFunctions_8004e29c[258] = SMap::FUN_800e6904;
    scriptSubFunctions_8004e29c[259] = SMap::FUN_800e69a4;
    scriptSubFunctions_8004e29c[260] = SMap::FUN_800e69e8;
    scriptSubFunctions_8004e29c[261] = SMap::FUN_800e69f0;
    scriptSubFunctions_8004e29c[262] = SMap::FUN_800e6a28;
    scriptSubFunctions_8004e29c[263] = SMap::FUN_800e6a64;
    scriptSubFunctions_8004e29c[264] = SMap::FUN_800e683c;
    scriptSubFunctions_8004e29c[265] = SMap::FUN_800e6af0;
    scriptSubFunctions_8004e29c[266] = SMap::FUN_800e6aa0;
    scriptSubFunctions_8004e29c[267] = SMap::FUN_800e6b64;
    scriptSubFunctions_8004e29c[268] = SMap::FUN_800e6bd8;
    scriptSubFunctions_8004e29c[269] = SMap::FUN_800e6be0;
    scriptSubFunctions_8004e29c[270] = SMap::FUN_800e6cac;
    scriptSubFunctions_8004e29c[271] = SMap::FUN_800e6ce0;

    scriptSubFunctions_8004e29c[288] = SMap::FUN_800dfb74;
    scriptSubFunctions_8004e29c[289] = SMap::FUN_800dfba4;
    scriptSubFunctions_8004e29c[290] = SMap::FUN_800dfbd4;
    scriptSubFunctions_8004e29c[291] = SMap::scriptScaleXyz;
    scriptSubFunctions_8004e29c[292] = SMap::scriptScaleUniform;
    scriptSubFunctions_8004e29c[293] = SMap::FUN_800dfca0;
    scriptSubFunctions_8004e29c[294] = SMap::FUN_800dfcd8;
    scriptSubFunctions_8004e29c[295] = SMap::FUN_800dfd10;
    scriptSubFunctions_8004e29c[296] = SMap::FUN_800de334;
    scriptSubFunctions_8004e29c[297] = SMap::FUN_800de4b4;
    scriptSubFunctions_8004e29c[298] = SMap::scriptShowAlertIndicator;
    scriptSubFunctions_8004e29c[299] = SMap::scriptHideAlertIndicator;
    scriptSubFunctions_8004e29c[300] = SMap::FUN_800dfd48;
    scriptSubFunctions_8004e29c[301] = SMap::FUN_800e05c8;
    scriptSubFunctions_8004e29c[302] = SMap::FUN_800e05f0;
    scriptSubFunctions_8004e29c[303] = SMap::FUN_800e0614;
    scriptSubFunctions_8004e29c[304] = SMap::FUN_800e0684;
    scriptSubFunctions_8004e29c[305] = SMap::FUN_800e06c4;
    scriptSubFunctions_8004e29c[306] = SMap::FUN_800e0710;
    scriptSubFunctions_8004e29c[307] = SMap::FUN_800e0894;
    scriptSubFunctions_8004e29c[308] = SMap::FUN_800e08f4;
    scriptSubFunctions_8004e29c[309] = SMap::scriptSetAmbientColour;
    scriptSubFunctions_8004e29c[310] = SMap::scriptResetAmbientColour;
    scriptSubFunctions_8004e29c[311] = SMap::FUN_800e09e0;
    scriptSubFunctions_8004e29c[312] = SMap::FUN_800e0a14;
    scriptSubFunctions_8004e29c[313] = SMap::FUN_800e0a48;
    scriptSubFunctions_8004e29c[314] = SMap::FUN_800e0a94;
    scriptSubFunctions_8004e29c[315] = SMap::scriptCheckPlayerCollision;
    scriptSubFunctions_8004e29c[316] = SMap::scriptGetPlayerMovement;
    scriptSubFunctions_8004e29c[317] = SMap::FUN_800e0b34;
    scriptSubFunctions_8004e29c[318] = SMap::FUN_800e0ba0;
    scriptSubFunctions_8004e29c[319] = SMap::FUN_800e0cb8;

    scriptSubFunctions_8004e29c[320] = Bttl_800c::FUN_800cc9d8;
    scriptSubFunctions_8004e29c[321] = Scus94491BpeSegment::scriptRewindAndPause2;
    scriptSubFunctions_8004e29c[322] = Bttl_800c::FUN_800cb84c;
    scriptSubFunctions_8004e29c[323] = Bttl_800c::FUN_800cb95c;
    scriptSubFunctions_8004e29c[324] = Scus94491BpeSegment::scriptRewindAndPause2;

    scriptSubFunctions_8004e29c[352] = Bttl_800c::scriptSetModelPartVisibility;
    scriptSubFunctions_8004e29c[353] = Bttl_800e::scriptCopyVram;
    scriptSubFunctions_8004e29c[354] = Bttl_800c::FUN_800cd468;
    scriptSubFunctions_8004e29c[355] = Bttl_800c::FUN_800cd4b0;
    scriptSubFunctions_8004e29c[356] = Bttl_800c::FUN_800cd4f0;
    scriptSubFunctions_8004e29c[357] = Bttl_800c::scriptAddCombatant;
    scriptSubFunctions_8004e29c[358] = Bttl_800c::scriptDeallocateAndRemoveCombatant;
    scriptSubFunctions_8004e29c[359] = Bttl_800c::FUN_800cda78;
    scriptSubFunctions_8004e29c[360] = Bttl_800c::FUN_800cd5b4;
    scriptSubFunctions_8004e29c[361] = Bttl_800c::FUN_800cd740;
    scriptSubFunctions_8004e29c[362] = Bttl_800c::FUN_800cd7a8;
    scriptSubFunctions_8004e29c[363] = Bttl_800c::FUN_800cd810;
    scriptSubFunctions_8004e29c[364] = Bttl_800c::FUN_800cd8a4;
    scriptSubFunctions_8004e29c[365] = Bttl_800c::scriptGetBobjNobj;
    scriptSubFunctions_8004e29c[366] = Bttl_800c::scriptDeallocateCombatant;
    scriptSubFunctions_8004e29c[367] = Bttl_800c::FUN_800cdb18;
    scriptSubFunctions_8004e29c[368] = Bttl_800c::scriptLoadStage;
    scriptSubFunctions_8004e29c[369] = Bttl_800c::FUN_800cd910;
    scriptSubFunctions_8004e29c[370] = Bttl_800c::scriptGetCombatantIndex;
    scriptSubFunctions_8004e29c[371] = Bttl_800c::FUN_800cd998;
    scriptSubFunctions_8004e29c[372] = Bttl_800c::FUN_800cdb74;

    scriptSubFunctions_8004e29c[416] = Bttl_800e::FUN_800e6fb4;

    scriptSubFunctions_8004e29c[419] = Bttl_800e::scriptResetLights;
    scriptSubFunctions_8004e29c[420] = Bttl_800e::scriptSetLightDirection;
    scriptSubFunctions_8004e29c[421] = Bttl_800e::scriptGetLightDirection;
    scriptSubFunctions_8004e29c[422] = Bttl_800e::FUN_800e48a8;
    scriptSubFunctions_8004e29c[423] = Bttl_800e::FUN_800e48e8;
    scriptSubFunctions_8004e29c[424] = Bttl_800e::FUN_800e4964;
    scriptSubFunctions_8004e29c[425] = Bttl_800e::FUN_800e4abc;
    scriptSubFunctions_8004e29c[426] = Bttl_800e::FUN_800e4c10;
    scriptSubFunctions_8004e29c[427] = Bttl_800e::FUN_800e4c90;
    scriptSubFunctions_8004e29c[428] = Bttl_800e::FUN_800e4d2c;
    scriptSubFunctions_8004e29c[429] = Bttl_800e::scriptGetLightColour;
    scriptSubFunctions_8004e29c[430] = Bttl_800e::FUN_800e4dfc;
    scriptSubFunctions_8004e29c[431] = Bttl_800e::FUN_800e4e2c;
    scriptSubFunctions_8004e29c[432] = Bttl_800e::FUN_800e4e64;
    scriptSubFunctions_8004e29c[433] = Bttl_800e::FUN_800e4ea0;
    scriptSubFunctions_8004e29c[434] = Bttl_800e::FUN_800e4fa0;
    scriptSubFunctions_8004e29c[435] = Bttl_800e::FUN_800e50e8;
    scriptSubFunctions_8004e29c[436] = Bttl_800e::FUN_800e52f8;
    scriptSubFunctions_8004e29c[437] = Bttl_800e::FUN_800e540c;
    scriptSubFunctions_8004e29c[438] = Bttl_800e::FUN_800e54f8;
    scriptSubFunctions_8004e29c[439] = Bttl_800e::FUN_800e5528;
    scriptSubFunctions_8004e29c[440] = Bttl_800e::FUN_800e5560;
    scriptSubFunctions_8004e29c[441] = Bttl_800e::FUN_800e559c;
    scriptSubFunctions_8004e29c[442] = Bttl_800e::FUN_800e569c;
    scriptSubFunctions_8004e29c[443] = Bttl_800e::FUN_800e596c;
    scriptSubFunctions_8004e29c[444] = Bttl_800e::FUN_800e59d8;

    // scriptSubFunctions_8004e29c[445] = Temp::FUN_800ca734;

    scriptSubFunctions_8004e29c[480] = Bttl_800f::scriptCheckPhysicalHit;
    scriptSubFunctions_8004e29c[481] = Bttl_800f::scriptPhysicalAttack;
    scriptSubFunctions_8004e29c[482] = Bttl_800f::scriptGetBobjPos;
    scriptSubFunctions_8004e29c[483] = Bttl_800f::scriptAddFloatingNumber;
    scriptSubFunctions_8004e29c[484] = Bttl_800f::scriptCheckPhysicalHit;
    scriptSubFunctions_8004e29c[485] = Bttl_800f::scriptCheckPhysicalHit;
    scriptSubFunctions_8004e29c[486] = Bttl_800f::scriptCheckPhysicalHit;
    scriptSubFunctions_8004e29c[487] = Bttl_800f::scriptGiveSp;
    scriptSubFunctions_8004e29c[488] = Bttl_800f::scriptConsumeSp;
    scriptSubFunctions_8004e29c[489] = Bttl_800f::scriptInitCombatMenu;
    scriptSubFunctions_8004e29c[490] = Bttl_800f::FUN_800f4600;
    scriptSubFunctions_8004e29c[491] = Bttl_800f::FUN_800f480c;
    scriptSubFunctions_8004e29c[492] = Bttl_800f::scriptDragoonMagicStatusItemAttack;
    scriptSubFunctions_8004e29c[493] = Bttl_800f::scriptSetTempSpellStats;
    scriptSubFunctions_8004e29c[494] = Bttl_800f::scriptRenderRecover;
    scriptSubFunctions_8004e29c[495] = Bttl_800f::scriptItemMagicAttack;
    scriptSubFunctions_8004e29c[496] = Bttl_800f::scriptSetTempItemMagicStats;
    scriptSubFunctions_8004e29c[497] = Bttl_800f::scriptTakeItem;
    scriptSubFunctions_8004e29c[498] = Bttl_800f::scriptGiveItem;
    scriptSubFunctions_8004e29c[499] = Bttl_800f::FUN_800f9a50;
    scriptSubFunctions_8004e29c[500] = Bttl_800f::scriptIsFloatingNumberOnScreen;
    scriptSubFunctions_8004e29c[501] = Bttl_800f::scriptSetDragoonSpaceElementIndex;
    scriptSubFunctions_8004e29c[502] = Bttl_800f::FUN_800f9b94;
    scriptSubFunctions_8004e29c[503] = Bttl_800f::FUN_800f9bd4;
    scriptSubFunctions_8004e29c[504] = Bttl_800f::FUN_800f9c00;
    scriptSubFunctions_8004e29c[505] = Bttl_800f::FUN_800f9c2c;
    scriptSubFunctions_8004e29c[506] = Bttl_800f::FUN_800f9cac;
    scriptSubFunctions_8004e29c[507] = Bttl_800f::scriptCheckSpellOrStatusHit;
    scriptSubFunctions_8004e29c[508] = Bttl_800f::scriptCheckItemHit;
    scriptSubFunctions_8004e29c[509] = Bttl_800f::scriptFinishBobjTurn;

    scriptSubFunctions_8004e29c[512] = Bttl_800e::scriptSetBobjZOffset;
    scriptSubFunctions_8004e29c[513] = Bttl_800e::scriptSetBobjScaleUniform;
    scriptSubFunctions_8004e29c[514] = Bttl_800e::scriptSetBobjScale;
    scriptSubFunctions_8004e29c[515] = Bttl_800e::FUN_800ee384;
    scriptSubFunctions_8004e29c[516] = Bttl_800e::FUN_800ee468;
    scriptSubFunctions_8004e29c[517] = Bttl_800e::FUN_800ee49c;
    scriptSubFunctions_8004e29c[518] = Bttl_800e::FUN_800ee4e8;
    scriptSubFunctions_8004e29c[519] = Bttl_800e::scriptApplyScreenDarkening;
    scriptSubFunctions_8004e29c[520] = Bttl_800e::FUN_800ee384;
    scriptSubFunctions_8004e29c[521] = Bttl_800e::scriptGetStageNobj;
    scriptSubFunctions_8004e29c[522] = Bttl_800e::FUN_800ee594;
    scriptSubFunctions_8004e29c[523] = Bttl_800e::FUN_800ee5c0;
    scriptSubFunctions_8004e29c[524] = Bttl_800e::FUN_800ee3c0;
    scriptSubFunctions_8004e29c[525] = Bttl_800e::FUN_800ee408;
    scriptSubFunctions_8004e29c[526] = Bttl_800e::scriptSetStageZ;

    scriptSubFunctions_8004e29c[544] = SEffe::FUN_801115ec;
    scriptSubFunctions_8004e29c[545] = SEffe::scriptSetRelativeTranslation;
    scriptSubFunctions_8004e29c[546] = SEffe::scriptGetRotationDifference;
    scriptSubFunctions_8004e29c[547] = SEffe::scriptSetRelativeRotation;
    scriptSubFunctions_8004e29c[548] = SEffe::scriptGetScaleRatio;
    scriptSubFunctions_8004e29c[549] = SEffe::scriptSetRelativeScale;
    scriptSubFunctions_8004e29c[550] = SEffe::scriptGetColourDifference;
    scriptSubFunctions_8004e29c[551] = SEffe::scriptSetRelativeColour;
    scriptSubFunctions_8004e29c[552] = SEffe::FUN_80114e0c;
    scriptSubFunctions_8004e29c[553] = SEffe::FUN_80114e60;
    scriptSubFunctions_8004e29c[554] = SEffe::FUN_80111b60;
    scriptSubFunctions_8004e29c[555] = SEffe::FUN_8011554c;
    scriptSubFunctions_8004e29c[556] = SEffe::FUN_801155a0;
    scriptSubFunctions_8004e29c[557] = SEffe::FUN_80111be8;
    scriptSubFunctions_8004e29c[558] = SEffe::FUN_80111c2c; // scaler
    scriptSubFunctions_8004e29c[559] = SEffe::scriptSetTranslationScalerWithRotation0;
    scriptSubFunctions_8004e29c[560] = SEffe::FUN_80112274; // scaler
    scriptSubFunctions_8004e29c[561] = SEffe::FUN_80112364; // scaler
    scriptSubFunctions_8004e29c[562] = SEffe::FUN_801155f8; // no-op
    scriptSubFunctions_8004e29c[563] = SEffe::FUN_80112900;
    scriptSubFunctions_8004e29c[564] = SEffe::FUN_8011299c;
    scriptSubFunctions_8004e29c[565] = SEffe::FUN_80115440;
    scriptSubFunctions_8004e29c[566] = SEffe::FUN_80111658;
    scriptSubFunctions_8004e29c[567] = SEffe::scriptSetRotationScaler;
    scriptSubFunctions_8004e29c[568] = SEffe::FUN_80112bf0;
    scriptSubFunctions_8004e29c[569] = SEffe::FUN_80112e00; // not implemented
    scriptSubFunctions_8004e29c[570] = SEffe::FUN_8011306c; // not implemented
    scriptSubFunctions_8004e29c[571] = SEffe::FUN_801132c8; // not implemented
    scriptSubFunctions_8004e29c[572] = SEffe::FUN_80115600; // no-op
    scriptSubFunctions_8004e29c[573] = Bttl_800e::FUN_800e9f68;
    scriptSubFunctions_8004e29c[574] = SEffe::FUN_80118984;
    scriptSubFunctions_8004e29c[575] = SEffe::scriptSetScaleVectorScaler;

    scriptSubFunctions_8004e29c[576] = SEffe::FUN_80114094;
    scriptSubFunctions_8004e29c[577] = SEffe::FUN_801143f8; // not implemented
    scriptSubFunctions_8004e29c[578] = SEffe::FUN_80115608;
    scriptSubFunctions_8004e29c[579] = SEffe::FUN_80114070;
    scriptSubFunctions_8004e29c[580] = SEffe::FUN_801147c8; // not implemented
    scriptSubFunctions_8004e29c[581] = SEffe::scriptSetColorVectorScaler;
    scriptSubFunctions_8004e29c[582] = SEffe::FUN_80114b00; // not implemented
    scriptSubFunctions_8004e29c[583] = SEffe::FUN_80114eb4;
    scriptSubFunctions_8004e29c[584] = SEffe::FUN_80114f34; // not implemented
    scriptSubFunctions_8004e29c[585] = SEffe::FUN_80115014;
    scriptSubFunctions_8004e29c[586] = SEffe::FUN_80115058;
    scriptSubFunctions_8004e29c[587] = SEffe::FUN_80115168; // not implemented
    scriptSubFunctions_8004e29c[588] = SEffe::scriptSetEffectTicksRemaining;
    scriptSubFunctions_8004e29c[589] = SEffe::FUN_80115324;
    scriptSubFunctions_8004e29c[590] = SEffe::FUN_80115388;
    scriptSubFunctions_8004e29c[591] = SEffe::FUN_801153e4;
    scriptSubFunctions_8004e29c[592] = Bttl_800e::FUN_800e74ac;
    scriptSubFunctions_8004e29c[593] = SEffe::FUN_80112398;
    scriptSubFunctions_8004e29c[594] = Bttl_800e::FUN_800eb518;
    scriptSubFunctions_8004e29c[595] = SEffe::FUN_8011549c;
    scriptSubFunctions_8004e29c[596] = SEffe::FUN_801122ec; // scaler
    scriptSubFunctions_8004e29c[597] = SEffe::scriptSetTranslationScalerWithRotation1;
    scriptSubFunctions_8004e29c[598] = SEffe::FUN_80111cc4;
    scriptSubFunctions_8004e29c[599] = SEffe::FUN_80111ed4;
    scriptSubFunctions_8004e29c[600] = Bttl_800e::scriptAllocateEmptyEffectManagerChild;
    scriptSubFunctions_8004e29c[601] = Bttl_800e::allocateBillboardSpriteEffect;
    scriptSubFunctions_8004e29c[602] = Bttl_800e::FUN_800e9854;
    // scriptSubFunctions_8004e29c[603] = Temp::FUN_800ca648;

    scriptSubFunctions_8004e29c[605] = SEffe::FUN_80117eb0;
    scriptSubFunctions_8004e29c[606] = SEffe::allocateDeffTmd;
    scriptSubFunctions_8004e29c[607] = Bttl_800e::FUN_800e99bc;

    scriptSubFunctions_8004e29c[608] = SEffe::FUN_801181a8;

    scriptSubFunctions_8004e29c[610] = Bttl_800e::FUN_800ea200;
    scriptSubFunctions_8004e29c[611] = SEffe::FUN_801156f8;
    scriptSubFunctions_8004e29c[612] = Bttl_800e::FUN_800ea13c;
    scriptSubFunctions_8004e29c[613] = Bttl_800e::FUN_800ea19c;
    scriptSubFunctions_8004e29c[614] = Bttl_800e::FUN_800eb84c;
    scriptSubFunctions_8004e29c[615] = Bttl_800e::FUN_800eb188;
    scriptSubFunctions_8004e29c[616] = Bttl_800e::FUN_800eb01c;
    // scriptSubFunctions_8004e29c[617] = Temp::FUN_800caae4;
    scriptSubFunctions_8004e29c[618] = SEffe::scriptLoadSameScriptAndJump;
    scriptSubFunctions_8004e29c[619] = SEffe::allocateShirleyTransformWipeEffect;
    scriptSubFunctions_8004e29c[620] = SEffe::FUN_80111a58;
    scriptSubFunctions_8004e29c[621] = Bttl_800e::FUN_800ea384;
    scriptSubFunctions_8004e29c[622] = SEffe::allocateSpriteWithTrailEffect;
    scriptSubFunctions_8004e29c[623] = Bttl_800e::scriptLoadDeff;
    scriptSubFunctions_8004e29c[624] = Bttl_800e::FUN_800e6db4;
    scriptSubFunctions_8004e29c[625] = Bttl_800e::FUN_800e7490;
    scriptSubFunctions_8004e29c[626] = SEffe::scriptGetEffectZ;
    scriptSubFunctions_8004e29c[627] = SEffe::scriptSetEffectZ;
    scriptSubFunctions_8004e29c[628] = SEffe::FUN_801184e4;
    scriptSubFunctions_8004e29c[629] = SEffe::FUN_801157d0;
    scriptSubFunctions_8004e29c[630] = SEffe::FUN_801127e0;
    scriptSubFunctions_8004e29c[631] = SEffe::FUN_801181f0;
    scriptSubFunctions_8004e29c[632] = SEffe::FUN_801114b8;

    scriptSubFunctions_8004e29c[634] = SEffe::FUN_80115ab0;
    scriptSubFunctions_8004e29c[635] = SEffe::FUN_80115a94;
    scriptSubFunctions_8004e29c[636] = SEffe::scriptPlayXaAudio;
    scriptSubFunctions_8004e29c[637] = Bttl_800e::FUN_800e7314;
    scriptSubFunctions_8004e29c[638] = Bttl_800e::FUN_800e71e4;
    scriptSubFunctions_8004e29c[639] = Bttl_800e::FUN_800e727c;

    scriptSubFunctions_8004e29c[640] = SEffe::FUN_8011357c;
    scriptSubFunctions_8004e29c[641] = SEffe::FUN_80115a28;
    scriptSubFunctions_8004e29c[642] = SEffe::FUN_8011287c;
    scriptSubFunctions_8004e29c[643] = Bttl_800e::FUN_800e9798;
    scriptSubFunctions_8004e29c[644] = Bttl_800e::FUN_800ea2a0;
    scriptSubFunctions_8004e29c[645] = Bttl_800e::FUN_800ea30c;
    scriptSubFunctions_8004e29c[646] = SEffe::FUN_801188ec;
    scriptSubFunctions_8004e29c[647] = SEffe::FUN_80115ad8;
    scriptSubFunctions_8004e29c[648] = SEffe::FUN_80115ea4;
    scriptSubFunctions_8004e29c[649] = SEffe::FUN_80115ed4;
    scriptSubFunctions_8004e29c[650] = SEffe::FUN_801154f4;
    scriptSubFunctions_8004e29c[651] = SEffe::scriptConsolidateEffectMemory;

    scriptSubFunctions_8004e29c[672] = SMap::FUN_800dfe0c;
    scriptSubFunctions_8004e29c[673] = SMap::FUN_800dfec8;
    scriptSubFunctions_8004e29c[674] = SMap::FUN_800dff68;
    scriptSubFunctions_8004e29c[675] = SMap::FUN_800dffa4;
    scriptSubFunctions_8004e29c[676] = SMap::FUN_800dffdc;
    scriptSubFunctions_8004e29c[677] = SMap::scriptFacePoint;
    scriptSubFunctions_8004e29c[678] = SMap::FUN_800e0094;
    scriptSubFunctions_8004e29c[679] = SMap::FUN_800de668;
    scriptSubFunctions_8004e29c[680] = SMap::FUN_800de944;
    scriptSubFunctions_8004e29c[681] = SMap::FUN_800e00cc;
    scriptSubFunctions_8004e29c[682] = SMap::FUN_800e0148;
    scriptSubFunctions_8004e29c[683] = SMap::FUN_800e01bc;
    scriptSubFunctions_8004e29c[684] = SMap::scriptEnableTextureAnimation;
    scriptSubFunctions_8004e29c[685] = SMap::FUN_800e0204;
    scriptSubFunctions_8004e29c[686] = SMap::scriptDisableTextureAnimation;
    scriptSubFunctions_8004e29c[687] = SMap::FUN_800e02c0;
    scriptSubFunctions_8004e29c[688] = SMap::FUN_800e02fc;
    scriptSubFunctions_8004e29c[689] = SMap::FUN_800deba0;
    scriptSubFunctions_8004e29c[690] = SMap::scriptGetSobjNobj;
    scriptSubFunctions_8004e29c[691] = SMap::FUN_800e03e4;
    scriptSubFunctions_8004e29c[692] = SMap::FUN_800e0448;
    scriptSubFunctions_8004e29c[693] = SMap::scriptFaceCamera;
    scriptSubFunctions_8004e29c[694] = SMap::FUN_800e0520;
    scriptSubFunctions_8004e29c[695] = SMap::FUN_800e057c;
    scriptSubFunctions_8004e29c[696] = SMap::FUN_800e074c;
    scriptSubFunctions_8004e29c[697] = SMap::FUN_800e07f0;
    scriptSubFunctions_8004e29c[698] = SMap::FUN_800e0184;
    scriptSubFunctions_8004e29c[699] = SMap::FUN_800e0c40;
    scriptSubFunctions_8004e29c[700] = SMap::FUN_800e0c80;
    scriptSubFunctions_8004e29c[701] = SMap::scriptLoadChapterTitleCard;
    scriptSubFunctions_8004e29c[702] = SMap::scriptIsChapterTitleCardLoaded;
    scriptSubFunctions_8004e29c[703] = SMap::FUN_800e0c9c;

    scriptSubFunctions_8004e29c[704] = Scus94491BpeSegment::scriptStartSequenceAndChangeVolumeOverTime;
    scriptSubFunctions_8004e29c[705] = Scus94491BpeSegment::scriptSssqFadeOut;
    scriptSubFunctions_8004e29c[706] = Scus94491BpeSegment::scriptChangeSequenceVolumeOverTime;
    scriptSubFunctions_8004e29c[707] = Scus94491BpeSegment::scriptGetSequenceFlags;
    scriptSubFunctions_8004e29c[708] = Scus94491BpeSegment::scriptGetSssqTempoScale;
    scriptSubFunctions_8004e29c[709] = Scus94491BpeSegment::scriptSetSssqTempoScale;
    scriptSubFunctions_8004e29c[710] = Scus94491BpeSegment::scriptGetLoadedSoundFiles;
    scriptSubFunctions_8004e29c[711] = Scus94491BpeSegment::scriptGetSequenceVolume;
    scriptSubFunctions_8004e29c[712] = Scus94491BpeSegment::scriptPlayCombatantSound;
    scriptSubFunctions_8004e29c[713] = Scus94491BpeSegment::scriptStopBobjSound;
    scriptSubFunctions_8004e29c[714] = Scus94491BpeSegment_8002::FUN_80020060;
    scriptSubFunctions_8004e29c[715] = Scus94491BpeSegment::scriptLoadPlayerAttackSounds;
    scriptSubFunctions_8004e29c[716] = Scus94491BpeSegment_8002::scriptReplaceMonsterSounds;
    scriptSubFunctions_8004e29c[717] = Scus94491BpeSegment::FUN_8001f674;
    scriptSubFunctions_8004e29c[718] = Scus94491BpeSegment::FUN_8001f560;

    scriptSubFunctions_8004e29c[736] = Bttl_800d::FUN_800d3090;
    scriptSubFunctions_8004e29c[737] = Bttl_800c::scriptAllocateFullScreenOverlay;
    scriptSubFunctions_8004e29c[738] = Bttl_800c::FUN_800cee50;
    scriptSubFunctions_8004e29c[739] = Bttl_800c::scriptSetWeaponTrailSegmentCount;
    scriptSubFunctions_8004e29c[740] = Bttl_800d::FUN_800d3098;
    scriptSubFunctions_8004e29c[741] = Bttl_800d::FUN_800d30a0;
    scriptSubFunctions_8004e29c[742] = Bttl_800d::FUN_800d30a8;
    scriptSubFunctions_8004e29c[743] = Bttl_800d::FUN_800d30b0;
    scriptSubFunctions_8004e29c[744] = Bttl_800c::FUN_800cef00;
    scriptSubFunctions_8004e29c[745] = Bttl_800c::FUN_800cf0b4;
    scriptSubFunctions_8004e29c[746] = SEffe::allocateParticleEffect;
    scriptSubFunctions_8004e29c[747] = SEffe::FUN_80102364;
    scriptSubFunctions_8004e29c[748] = Bttl_800d::FUN_800d30b8;
    scriptSubFunctions_8004e29c[749] = Bttl_800d::allocateProjectileHitEffect;
    scriptSubFunctions_8004e29c[750] = Bttl_800d::FUN_800d09b8;
    scriptSubFunctions_8004e29c[751] = Bttl_800d::allocateAdditionSparksEffect;
    scriptSubFunctions_8004e29c[752] = SEffe::FUN_80102608;
    scriptSubFunctions_8004e29c[753] = SEffe::allocateAdditionOverlaysEffect;
    scriptSubFunctions_8004e29c[754] = SEffe::scriptGetHitCompletionState;
    scriptSubFunctions_8004e29c[755] = SEffe::FUN_80108de8;
    scriptSubFunctions_8004e29c[756] = Bttl_800d::allocateAdditionStarburstEffect;
    scriptSubFunctions_8004e29c[757] = Bttl_800d::FUN_800d1cac;
    scriptSubFunctions_8004e29c[758] = Bttl_800d::FUN_800d1cf4;
    scriptSubFunctions_8004e29c[759] = SEffe::scriptAlterAdditionContinuationState;
    scriptSubFunctions_8004e29c[760] = SEffe::FUN_80108df0;
    scriptSubFunctions_8004e29c[761] = Bttl_800d::allocateGuardEffect;
    scriptSubFunctions_8004e29c[762] = Bttl_800c::allocateWeaponTrailEffect;
    scriptSubFunctions_8004e29c[763] = Bttl_800d::allocateRadialGradientEffect;
    scriptSubFunctions_8004e29c[764] = Bttl_800d::scriptAllocateAdditionScript;
    scriptSubFunctions_8004e29c[765] = Bttl_800c::FUN_800cfccc;
    scriptSubFunctions_8004e29c[766] = Bttl_800d::FUN_800d4338;
    scriptSubFunctions_8004e29c[767] = Bttl_800d::FUN_800d4580;

    scriptSubFunctions_8004e29c[768] = SMap::FUN_800f2048;
    scriptSubFunctions_8004e29c[769] = SMap::FUN_800f1f9c;
    scriptSubFunctions_8004e29c[770] = SMap::FUN_800f1060;
    scriptSubFunctions_8004e29c[771] = SMap::FUN_800f2264;
    scriptSubFunctions_8004e29c[772] = SMap::scriptAddSavePoint;
    scriptSubFunctions_8004e29c[773] = SMap::FUN_800f23ec;
    scriptSubFunctions_8004e29c[774] = SMap::FUN_800f2780;
    scriptSubFunctions_8004e29c[775] = SMap::FUN_800f2090;
    scriptSubFunctions_8004e29c[776] = SMap::FUN_800f2198;
    scriptSubFunctions_8004e29c[777] = SMap::FUN_800f1eb8;
    scriptSubFunctions_8004e29c[778] = SMap::FUN_800f2618;
    scriptSubFunctions_8004e29c[779] = SMap::FUN_800f1b64;
    scriptSubFunctions_8004e29c[780] = SMap::FUN_800f26c8;
    scriptSubFunctions_8004e29c[781] = SMap::FUN_800f1d0c;
    scriptSubFunctions_8004e29c[782] = SMap::FUN_800f14f0;
    scriptSubFunctions_8004e29c[783] = SMap::FUN_800f24d8;
    scriptSubFunctions_8004e29c[784] = SMap::FUN_800f24b0;
    scriptSubFunctions_8004e29c[785] = SMap::FUN_800f23a0;
    scriptSubFunctions_8004e29c[786] = SMap::FUN_800f1634;
    scriptSubFunctions_8004e29c[787] = SMap::FUN_800f22c4;
    scriptSubFunctions_8004e29c[788] = SMap::FUN_800f2554;
    scriptSubFunctions_8004e29c[789] = SMap::FUN_800f25a8;
    scriptSubFunctions_8004e29c[790] = SMap::FUN_800f1274;

    scriptSubFunctions_8004e29c[800] = SEffe::allocateLensFlareEffect;
    scriptSubFunctions_8004e29c[801] = SEffe::allocateWsDragoonTransformationFeathersEffect;
    scriptSubFunctions_8004e29c[802] = SEffe::allocateGoldDragoonTransformEffect;
    scriptSubFunctions_8004e29c[803] = SEffe::allocateStarChildrenMeteorEffect;
    scriptSubFunctions_8004e29c[804] = SEffe::allocateStarChildrenImpactEffect;
    scriptSubFunctions_8004e29c[805] = SEffe::allocateMoonlightStarsEffect;
    scriptSubFunctions_8004e29c[806] = Scus94491BpeSegment::FUN_8001c5fc;
    scriptSubFunctions_8004e29c[807] = Scus94491BpeSegment::FUN_8001c604;

    scriptSubFunctions_8004e29c[832] = Bttl_800d::scriptRenderButtonPressHudElement;
    scriptSubFunctions_8004e29c[833] = SEffe::FUN_80108df8;
    scriptSubFunctions_8004e29c[834] = SEffe::FUN_80102610;
    scriptSubFunctions_8004e29c[835] = Bttl_800c::scriptGetBobjDimension;
    scriptSubFunctions_8004e29c[836] = SEffe::allocateRainEffect;
    scriptSubFunctions_8004e29c[837] = Bttl_800c::FUN_800ce9b0;
    scriptSubFunctions_8004e29c[838] = SEffe::allocateElectricityEffect;
    scriptSubFunctions_8004e29c[839] = SEffe::FUN_80105604;
    scriptSubFunctions_8004e29c[840] = SEffe::allocateDragoonAdditionScript;
    scriptSubFunctions_8004e29c[841] = SEffe::FUN_80105c38;
    // scriptSubFunctions_8004e29c[842] = Temp::FUN_800c6968;
    scriptSubFunctions_8004e29c[843] = SEffe::allocateScreenDistortionEffect;
    scriptSubFunctions_8004e29c[844] = SEffe::FUN_801089cc;
    scriptSubFunctions_8004e29c[845] = SEffe::FUN_801023f4;
    scriptSubFunctions_8004e29c[846] = Bttl_800c::FUN_800cfec8;
    scriptSubFunctions_8004e29c[847] = SEffe::FUN_801023fc;
    scriptSubFunctions_8004e29c[848] = SEffe::FUN_8010246c;
    scriptSubFunctions_8004e29c[849] = Bttl_800c::scriptSetMtSeed;
    scriptSubFunctions_8004e29c[850] = SEffe::allocateVertexDifferenceAnimation;
    scriptSubFunctions_8004e29c[851] = SEffe::FUN_8010a3fc;
    scriptSubFunctions_8004e29c[852] = Bttl_800d::allocateMonsterDeathEffect;
    scriptSubFunctions_8004e29c[853] = Bttl_800d::FUN_800d0124;
    scriptSubFunctions_8004e29c[854] = SEffe::scriptGetAdditionOverlayActiveStatus;

    scriptSubFunctions_8004e29c[864] = Scus94491BpeSegment_8002::scriptGiveChestContents;
    scriptSubFunctions_8004e29c[865] = Scus94491BpeSegment_8002::scriptTakeItem;
    scriptSubFunctions_8004e29c[866] = Scus94491BpeSegment_8002::scriptGiveGold;

    scriptSubFunctions_8004e29c[896] = SEffe::allocateGradientRaysEffect;
    scriptSubFunctions_8004e29c[897] = SEffe::allocateScreenCaptureEffect;
  }
  // 8004f29c end of jump table

  public static final Value _8004f2a8 = MEMORY.ref(4, 0x8004f2a8L);
  public static final ItemStats0c[] itemStats_8004f2ac = new ItemStats0c[64];
  public static final ArrayRef<ShortRef> additionOffsets_8004f5ac = MEMORY.ref(2, 0x8004f5acL, ArrayRef.of(ShortRef.class, 10, 0x2, ShortRef::new));
  public static final ArrayRef<ShortRef> additionCounts_8004f5c0 = MEMORY.ref(2, 0x8004f5c0L, ArrayRef.of(ShortRef.class, 10, 0x2, ShortRef::new));
  /**
   * <ol start="0">
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link Bttl_800c#FUN_800c7524}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link Bttl_800c#FUN_800c7648}</li>
   *   <li>{@link Bttl_800c#FUN_800c76a0}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_80018998}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_80018998}</li>
   *   <li>{@link Bttl_800c#battleInitiateAndPreload_800c772c}</li>
   *   <li>{@link Bttl_800c#deferAllocateEnemyBattleObjects()}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link Bttl_800c#deferAllocatePlayerBattleObjects()}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link Bttl_800c#deferLoadEncounterAssets}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link Bttl_800c#FUN_800c7964}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link Bttl_800c#FUN_800c79f0}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link Bttl_800c#deferDoNothing}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link Bttl_800c#FUN_800c7a80}</li>
   *   <li>{@link Bttl_800c#battleTick}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link Bttl_800c#performPostBattleAction}</li>
   *   <li>{@link Bttl_800c#deallocateCombat}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_80018998}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_80018508}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_800189b0}</li>
   * </ol>
   */
  public static final Runnable[] _8004f5d4 = new Runnable[31];
  static {
    _8004f5d4[0] = Scus94491BpeSegment::waitForFilesToLoad;
    _8004f5d4[1] = Bttl_800c::FUN_800c7524;
    _8004f5d4[2] = Scus94491BpeSegment::waitForFilesToLoad;
    _8004f5d4[3] = Scus94491BpeSegment::waitForFilesToLoad;
    _8004f5d4[4] = Bttl_800c::FUN_800c7648;
    _8004f5d4[5] = Bttl_800c::FUN_800c76a0;
    _8004f5d4[6] = Scus94491BpeSegment::FUN_80018998;
    _8004f5d4[7] = Scus94491BpeSegment::waitForFilesToLoad;
    _8004f5d4[8] = Scus94491BpeSegment::FUN_80018998;
    _8004f5d4[9] = Bttl_800c::battleInitiateAndPreload_800c772c;
    _8004f5d4[10] = Bttl_800c::deferAllocateEnemyBattleObjects;
    _8004f5d4[11] = Scus94491BpeSegment::waitForFilesToLoad;
    _8004f5d4[12] = Bttl_800c::deferAllocatePlayerBattleObjects;
    _8004f5d4[13] = Scus94491BpeSegment::waitForFilesToLoad;
    _8004f5d4[14] = Bttl_800c::deferLoadEncounterAssets;
    _8004f5d4[15] = Scus94491BpeSegment::waitForFilesToLoad;
    _8004f5d4[16] = Bttl_800c::FUN_800c7964;
    _8004f5d4[17] = Scus94491BpeSegment::waitForFilesToLoad;
    _8004f5d4[18] = Bttl_800c::FUN_800c79f0;
    _8004f5d4[19] = Scus94491BpeSegment::waitForFilesToLoad;
    _8004f5d4[20] = Bttl_800c::deferDoNothing;
    _8004f5d4[21] = Scus94491BpeSegment::waitForFilesToLoad;
    _8004f5d4[22] = Bttl_800c::FUN_800c7a80;
    _8004f5d4[23] = Bttl_800c::battleTick;
    _8004f5d4[24] = Scus94491BpeSegment::waitForFilesToLoad;
    _8004f5d4[25] = Bttl_800c::performPostBattleAction;
    _8004f5d4[26] = Bttl_800c::deallocateCombat;
    _8004f5d4[27] = Scus94491BpeSegment::waitForFilesToLoad;
    _8004f5d4[28] = Scus94491BpeSegment::FUN_80018998;
    _8004f5d4[29] = Scus94491BpeSegment::FUN_80018508;
    _8004f5d4[30] = Scus94491BpeSegment::FUN_800189b0;
  }

  public static final ScriptFile doNothingScript_8004f650 = new ScriptFile("Do nothing", new int[] {0x4, 0x1});
  public static Struct10 _8004f658;

  public static final Value _8004f6e4 = MEMORY.ref(4, 0x8004f6e4L);
  public static final Value _8004f6e8 = MEMORY.ref(4, 0x8004f6e8L);
  public static final Value _8004f6ec = MEMORY.ref(4, 0x8004f6ecL);

  public static final ArrayRef<SubmapMusic08> _8004fa98 = MEMORY.ref(1, 0x8004fa98L, ArrayRef.of(SubmapMusic08.class, 13, 8, SubmapMusic08::new));
  public static final ArrayRef<SubmapMusic08> _8004fb00 = MEMORY.ref(1, 0x8004fb00L, ArrayRef.of(SubmapMusic08.class, 130, 8, SubmapMusic08::new));
  public static final ArrayRef<MoonMusic08> moonMusic_8004ff10 = MEMORY.ref(4, 0x8004ff10L, ArrayRef.of(MoonMusic08.class, 43, 8, MoonMusic08::new));

  @Method(0x80040010L)
  public static void RotMatrix_Zyx(final SVECTOR rotation, final MATRIX matrixOut) {
    matrixOut.set(new Matrix4f()
      .rotateZ(MathHelper.psxDegToRad(rotation.getZ()))
      .rotateY(MathHelper.psxDegToRad(rotation.getY()))
      .rotateX(MathHelper.psxDegToRad(rotation.getX())));
  }

  @Method(0x800402a0L)
  public static void RotMatrixX(final int rotation, final MATRIX matrixOut) {
    final short sin = sin(rotation);
    final short cos = rcos(Math.abs(rotation));

    //LAB_80040304
    final long m10 = matrixOut.get(1, 0);
    final long m11 = matrixOut.get(1, 1);
    final long m12 = matrixOut.get(1, 2);
    final long m20 = matrixOut.get(2, 0);
    final long m21 = matrixOut.get(2, 1);
    final long m22 = matrixOut.get(2, 2);

    matrixOut.set(1, 0, (short)(cos * m10 - sin * m20 >> 12));
    matrixOut.set(1, 1, (short)(cos * m11 - sin * m21 >> 12));
    matrixOut.set(1, 2, (short)(cos * m12 - sin * m22 >> 12));
    matrixOut.set(2, 0, (short)(sin * m10 + cos * m20 >> 12));
    matrixOut.set(2, 1, (short)(sin * m11 + cos * m21 >> 12));
    matrixOut.set(2, 2, (short)(sin * m12 + cos * m22 >> 12));
  }

  @Method(0x80040440L)
  public static void RotMatrixY(final int rotation, final MATRIX matrixOut) {
    final short sin = (short)-sin(rotation);
    final short cos = rcos(Math.abs(rotation));

    //LAB_800404a4
    final short m0 = matrixOut.get(0);
    final short m1 = matrixOut.get(1);
    final short m2 = matrixOut.get(2);
    final short m6 = matrixOut.get(6);
    final short m7 = matrixOut.get(7);
    final short m8 = matrixOut.get(8);
    matrixOut.set(0, (short)(cos * m0 - sin * m6 >> 12));
    matrixOut.set(1, (short)(cos * m1 - sin * m7 >> 12));
    matrixOut.set(2, (short)(cos * m2 - sin * m8 >> 12));
    matrixOut.set(6, (short)(sin * m0 + cos * m6 >> 12));
    matrixOut.set(7, (short)(sin * m1 + cos * m7 >> 12));
    matrixOut.set(8, (short)(sin * m2 + cos * m8 >> 12));
  }

  @Method(0x800405e0L)
  public static void RotMatrixZ(final int rotation, final MATRIX matrixOut) {
    final short sin = sin(rotation);
    final short cos = rcos(Math.abs(rotation));

    //LAB_80040644
    final long m00 = matrixOut.get(0, 0);
    final long m01 = matrixOut.get(0, 1);
    final long m02 = matrixOut.get(0, 2);
    final long m10 = matrixOut.get(1, 0);
    final long m11 = matrixOut.get(1, 1);
    final long m12 = matrixOut.get(1, 2);

    matrixOut.set(0, 0, (short)(cos * m00 - sin * m10 >> 12));
    matrixOut.set(0, 1, (short)(cos * m01 - sin * m11 >> 12));
    matrixOut.set(0, 2, (short)(cos * m02 - sin * m12 >> 12));
    matrixOut.set(1, 0, (short)(sin * m00 + cos * m10 >> 12));
    matrixOut.set(1, 1, (short)(sin * m01 + cos * m11 >> 12));
    matrixOut.set(1, 2, (short)(sin * m02 + cos * m12 >> 12));
  }

  /**
   * Uses PlayStation format (4096 = 360 degrees = 2pi) to finish the x/y arctan function (-180 degrees and +180 degrees, -pi...pi).
   *
   *  1-bit sign
   * 19-bit whole
   * 12-bit decimal
   */
  @Method(0x80040b90L)
  public static int ratan2(int x, int y) {
    boolean negativeY = false;
    boolean negativeX = false;

    if(y < 0) {
      negativeY = true;
      y = -y;
    }

    //LAB_80040ba4
    if(x < 0) {
      negativeX = true;
      x = -x;
    }

    //LAB_80040bb4
    if(y == 0 && x == 0) {
      return 0;
    }

    //LAB_80040bc8
    int atan;
    if(x < y) {
      if((x & 0x7fe0_0000) == 0) {
        //LAB_80040c10
        //LAB_80040c3c
        x = Math.floorDiv(x << 10, y);
      } else {
        x = Math.floorDiv(x, y >> 10);
      }

      //LAB_80040c44
      atan = atanTable_80058d0c.get(x).get();
    } else {
      //LAB_80040c58
      if((y & 0x7fe0_0000) == 0) {
        //LAB_80040c98
        //LAB_80040cc4
        x = Math.floorDiv(y << 10, x);
      } else {
        //LAB_80040c8c
        x = Math.floorDiv(y, x >> 10);
      }

      //LAB_80040ccc
      atan = 0x400 - atanTable_80058d0c.get(x).get();
    }

    //LAB_80040ce0
    if(negativeY) {
      atan = 0x800 - atan;
    }

    //LAB_80040cec
    if(negativeX) {
      return -atan;
    }

    //LAB_80040cfc
    return atan;
  }

  @Method(0x80040df0L)
  public static void FUN_80040df0(final VECTOR a0, final COLOUR in, final COLOUR out) {
    CPU.MTC2(a0.getX(), 0); // VXY0
    CPU.MTC2(a0.getY(), 1); // VZ0
    CPU.MTC2(in.pack(), 6); // RGBC
    CPU.COP2(0x108_041bL);
    out.unpack(CPU.MFC2(22)); // RGB FIFO
  }

  @Method(0x80040e10L)
  public static VECTOR FUN_80040e10(final VECTOR a0, final VECTOR a1) {
    CPU.MTC2(a0.getX(),  9);
    CPU.MTC2(a0.getY(), 10);
    CPU.MTC2(a0.getZ(), 11);
    CPU.COP2(0xa00428L);
    a1.setX((int)CPU.MFC2(25));
    a1.setY((int)CPU.MFC2(26));
    a1.setZ((int)CPU.MFC2(27));
    return a1;
  }

  @Method(0x80040e40L)
  public static void FUN_80040e40(final VECTOR a0, final VECTOR a1, final VECTOR out) {
    final long t5 = CPU.CFC2(0);
    final long t6 = CPU.CFC2(2);
    final long t7 = CPU.CFC2(4);
    CPU.CTC2(a0.getX(),  0);
    CPU.CTC2(a0.getY(),  2);
    CPU.CTC2(a0.getZ(),  4);
    CPU.MTC2(a1.getX(),  9);
    CPU.MTC2(a1.getY(), 10);
    CPU.MTC2(a1.getZ(), 11);
    CPU.COP2(0x178000cL);
    out.setX((int)CPU.MFC2(25));
    out.setY((int)CPU.MFC2(26));
    out.setZ((int)CPU.MFC2(27));
    CPU.CTC2(t5, 0);
    CPU.CTC2(t6, 2);
    CPU.CTC2(t7, 4);
  }

  @Method(0x80040ea0L)
  public static int Lzc(final int val) {
    CPU.MTC2(val, 30);
    return (int)CPU.MFC2(31);
  }

  /**
   * Transform vector a1 and store in vector a2. Matrix a0 is uploaded to GTE in transposed order.
   */
  @Method(0x80040ec0L)
  public static VECTOR ApplyTransposeMatrixLV(final MATRIX a0, final VECTOR a1, final VECTOR a2) {
    CPU.CTC2((a0.get(3) & 0xffffL) << 16 | a0.get(0) & 0xffffL, 0);
    CPU.CTC2((a0.get(1) & 0xffffL) << 16 | a0.get(6) & 0xffffL, 1);
    CPU.CTC2((a0.get(7) & 0xffffL) << 16 | a0.get(4) & 0xffffL, 2);
    CPU.CTC2((a0.get(5) & 0xffffL) << 16 | a0.get(2) & 0xffffL, 3);
    CPU.CTC2(a0.get(8), 4);

    long t0;
    long t3;
    if(a1.getX() < 0) {
      t0 = -a1.getX();
      t3 = (int)t0 >> 15;
      t3 = -t3;
      t0 = t0 & 0x7fffL;
      t0 = -t0;
    } else {
      //LAB_80040f54
      t3 = a1.getX() >> 15;
      t0 = a1.getX() & 0x7fffL;
    }

    //LAB_80040f5c
    long t1;
    long t4;
    if(a1.getY() < 0) {
      t1 = -a1.getY();
      t4 = (int)t1 >> 15;
      t4 = -t4;
      t1 = t1 & 0x7fffL;
      t1 = -t1;
    } else {
      //LAB_80040f7c
      t4 = a1.getY() >> 15;
      t1 = a1.getY() & 0x7fffL;
    }

    //LAB_80040f84
    long t2;
    long t5;
    if(a1.getZ() < 0) {
      t2 = -a1.getZ();
      t5 = (int)t2 >> 15;
      t5 = -t5;
      t2 = t2 & 0x7fffL;
      t2 = -t2;
    } else {
      //LAB_80040fa4
      t5 = a1.getZ() >> 15;
      t2 = a1.getZ() & 0x7fffL;
    }

    //LAB_80040fac
    CPU.MTC2((t4 & 0xffffL) << 16 | t3 & 0xffffL, 0);
    CPU.MTC2(t5, 1);

    CPU.COP2(0x406012L);
    t3 = CPU.MFC2(25);
    t4 = CPU.MFC2(26);
    t5 = CPU.MFC2(27);

    CPU.MTC2((t1 & 0xffffL) << 16 | t0 & 0xffffL, 0);
    CPU.MTC2(t2, 1);
    CPU.COP2(0x486012L);

    //LAB_80041008
    //LAB_8004100c
    //LAB_80041024
    //LAB_80041028
    //LAB_80041040
    //LAB_80041044
    a2.setX((int)(CPU.MFC2(25) + t3 * 0x8L));
    a2.setY((int)(CPU.MFC2(26) + t4 * 0x8L));
    a2.setZ((int)(CPU.MFC2(27) + t5 * 0x8L));
    return a2;
  }

  // Start of SPU code

  @Method(0x80048828L)
  public static void setKeyOn(final SequenceData124 sequenceData, final long voiceIndex) {
    sequenceData.keyOn_0de |= 0x1L << voiceIndex;
  }

  @Method(0x800488d4L)
  public static void setKeyOff(final SequenceData124 sequenceData, final int voiceIndex) {
    sequenceData.keyOff_0e2 |= 0x1L << voiceIndex;
  }

  @Method(0x80048c38L)
  public static SssqReader getSequence(final PlayableSound0c playableSound, final int patchIndex, final int sequenceIndex) {
    assert patchIndex >= 0;
    assert sequenceIndex >= 0;

    final SoundEnv44 soundEnv = soundEnv_800c6630;
    final Sshd sshd = playableSound.sshdPtr_04;
    soundEnv.sshdPtr_08 = sshd;
    sshdPtr_800c4ac0 = sshd;
    final PatchList patchList = sshd.getSubfile(3, PatchList::new);

    patchList_800c4abc = patchList;

    if(sshd.hasSubfile(4)) {
      if(soundEnv.playingSoundsUpperBound_03 != 0) {
        if(playableSound.used_00) {
          if(patchIndex <= patchList.patchCount_00) {
            if(patchList.patches_02[patchIndex] != null) {
              final PatchList.SequenceList sequenceList = patchList.patches_02[patchIndex];
              final int sequenceCount = sequenceList.sequenceCount_00;

              if(sequenceCount >= sequenceIndex) {
                sssqish_800c4aa8 = sshd.getSubfile(4, (data, offset) -> new Sssqish(data, offset, sshd.getSubfileSize(4)));
                volumeRamp_800c4ab0 = sshd.getSubfile(1, VolumeRamp::new);
                return sequenceList.sequences_02[sequenceIndex].reader();
              }
            }
          }
        }
      }
    }

    //LAB_80048d3c
    LOGGER.warn("Failed to find sequence"); // Known to happen in Archangel sword attack
    return null;
  }

  @Method(0x80048d44L)
  public static int loadSoundIntoSequencer(final PlayableSound0c playableSound, final int patchIndex, final int sequenceIndex) {
    final SssqReader reader = getSequence(playableSound, patchIndex, sequenceIndex);

    if(reader != null) {
      final SoundEnv44 soundEnv = soundEnv_800c6630;

      //LAB_80048dac
      for(int loadedSequenceIndex = 0; loadedSequenceIndex < 24; loadedSequenceIndex++) {
        final SequenceData124 sequenceData = sequenceData_800c4ac8[loadedSequenceIndex];
        if(!sequenceData.musicLoaded_027 && !sequenceData.soundLoaded_029) {
          sequenceData.musicLoaded_027 = false;
          sequenceData.musicPlaying_028 = false;
          sequenceData.soundLoaded_029 = true;
          sequenceData.soundPlaying_02a = true;
          sequenceData.deltaTime_118 = 0;
          sequenceData.soundEnded_0e7 = false;
          sequenceData.sssqReader_010 = reader;
          sequenceData.playableSound_020 = playableSound;
          sequenceData.sequenceIndex_022 = sequenceIndex;
          sequenceData.patchIndex_024 = patchIndex;

          if(soundEnv.reverbEnabled_23) {
            sequenceData.reverbEnabled_0ea = true;
            soundEnv.reverbEnabled_23 = false;
          }

          //LAB_80048e10
          sequenceData.repeatCounter_035 = 0;
          sequenceData.repeat_037 = false;
          sequenceData._0e6 = false;
          sequenceData.pitchShiftVolLeft_0ee = 0;
          sequenceData.pitchShifted_0e9 = false;
          sequenceData.pitchShiftVolRight_0f0 = 0;

          if(soundEnv.pitchShifted_22) {
            sequenceData.pitchShifted_0e9 = true;
            sequenceData.pitch_0ec = soundEnv.pitch_24;
            sequenceData.pitchShiftVolLeft_0ee = soundEnv.pitchShiftVolLeft_26;
            sequenceData.pitchShiftVolRight_0f0 = soundEnv.pitchShiftVolRight_28;
            soundEnv.pitchShifted_22 = false;
            soundEnv.pitch_24 = 0;
            soundEnv.pitchShiftVolLeft_26 = 0;
            soundEnv.pitchShiftVolRight_28 = 0;
          }

          return loadedSequenceIndex;
        }

        //LAB_80048e74
      }
    }

    //LAB_80048e8c
    //LAB_80048e90
    LOGGER.warn("No empty sequence data"); // Known to happen in Archangel sword attack
    return -1;
  }

  @Method(0x8004ab2cL)
  public static void spuDmaCallback() {
    //LAB_8004ab5c
    if(soundEnv_800c6630.hasCallback_38) {
      spuDmaCompleteCallback_800c6628.run();
    }
  }

  @Method(0x8004ad2cL)
  public static void updateVoiceVolume(final int voiceIndex) {
    final PlayingNote66 playingNote = playingNotes_800c3a40[voiceIndex];
    final SequenceData124 sequenceData = playingNote.sequenceData_06;

    if(playingNote.isPolyphonicKeyPressure_1a) {
      final Sshd sshd = sequenceData.playableSound_020.sshdPtr_04;
      sshdPtr_800c4ac0 = sshd;
      sssqChannelInfo_800C6680 = sshd.getSubfile(4, (data, offset) -> new Sssqish(data, offset, sshd.getSubfileSize(4))).entries_10[playingNote.sequenceChannel_04];
    } else {
      //LAB_8004adf4
      sssqChannelInfo_800C6680 = sequenceData.sssqReader_010.channelInfo(playingNote.sequenceChannel_04);
    }

    //LAB_8004ae10
    playingNote.channelVolume_28 = sssqChannelInfo_800C6680.volume_0e;
    SPU.voices[voiceIndex].volumeLeft.set(calculateNoteVolume(voiceIndex, 0));
    SPU.voices[voiceIndex].volumeRight.set(calculateNoteVolume(voiceIndex, 1));
  }

  @Method(0x8004ae94L)
  public static int calculateNoteVolume(final int voiceIndex, final int leftOrRight) {
    final PlayingNote66 playingNote = playingNotes_800c3a40[voiceIndex];

    final short a0 = (short)((playingNote.channelVolume_28 * playingNote.instrumentVolume_2a * playingNote.velocityVolume_2c * playingNote.instrumentLayerVolume_2e >> 14) * playingNote.volumeLeftRight_30[leftOrRight] >> 7);
    if(playingNote._4a == 0) {
      return a0;
    }

    //LAB_8004af30
    return (short)(playingNote._4a << 8 | a0 >> 7);
  }

  @Method(0x8004b1e8L)
  public static int changeSequenceVolumeOverTime(final SequenceData124 sequenceData, final int transitionTime, final int channel, final int newVolume) {
    final int ret;
    if(channel == -1) {
      sssqReader_800c667c = sequenceData.sssqReader_010;
      sequenceData.volumeChange_03e[0].used_0a = true;
      sequenceData.volumeChange_03e[0].newValue_0c = newVolume;
      sequenceData.volumeChange_03e[0].remainingTime_0e = transitionTime;
      sequenceData.volumeChange_03e[0].totalTime_10 = transitionTime;
      sequenceData.volumeChange_03e[0].oldValue_12 = sssqReader_800c667c.baseVolume();

      ret = sssqReader_800c667c.baseVolume();
    } else {
      //LAB_8004b268
      sssqChannelInfo_800C6680 = sequenceData.sssqReader_010.channelInfo(channel);
      sequenceData.volumeChange_03e[channel].used_00 = true;
      sequenceData.volumeChange_03e[channel].newValue_02 = newVolume;
      sequenceData.volumeChange_03e[channel].remainingTime_04 = transitionTime;
      sequenceData.volumeChange_03e[channel].totalTime_06 = transitionTime;
      sequenceData.volumeChange_03e[channel].oldValue_08 = sssqChannelInfo_800C6680.volume_03;

      ret = sssqChannelInfo_800C6680.volume_03;
    }

    //LAB_8004b2b8
    sequenceData.volumeIsChanging_03c = true;
    return ret;
  }

  @Method(0x8004b834L)
  public static void initSpu() {
    final SoundEnv44 soundEnv = soundEnv_800c6630;

    //LAB_8004b8ac
    soundEnv.playingSoundsUpperBound_03 = 8;
    soundEnv._00 = 0;
    soundEnv._0d = 0;
    soundEnv.ticksPerSecond_42 = 60;
    SPU.unmute();

    //LAB_8004b9e8
    //LAB_8004ba58
    for(int voiceIndex = 0; voiceIndex < SPU.voices.length; voiceIndex++) {
      //LAB_8004ba74
      playingNotes_800c3a40[voiceIndex].clear();
    }

    //LAB_8004bab8
    playableSounds_800c43d0.clear();

    //LAB_8004bb14
    for(int voiceIndex = 0; voiceIndex < SPU.voices.length; voiceIndex++) {
      playingNotes_800c3a40[voiceIndex].portamentoNote_4e = 120;
    }
  }

  @Method(0x8004be7cL)
  public static void setSpuDmaCompleteCallback(@Nullable final Runnable callback) {
    if(callback == null) {
      soundEnv_800c6630.hasCallback_38 = false;
    } else {
      spuDmaCompleteCallback_800c6628 = callback;
      soundEnv_800c6630.hasCallback_38 = true;
    }
  }

  /**
   * @return Index into {@link Scus94491BpeSegment_800c#playableSounds_800c43d0}, or -1 on error
   */
  @Method(0x8004bea4L)
  public static PlayableSound0c loadSshdAndSoundbank(final String name, final FileData soundbank, final Sshd sshd, final int addressInSoundBuffer) {
    if(addressInSoundBuffer > 0x8_0000 || (addressInSoundBuffer & 0xf) != 0) {
      throw new IllegalArgumentException("Invalid sound buffer offset");
    }

    LOGGER.info(SEQUENCER_MARKER, "Loaded SShd into playableSound %s", name);

    //LAB_8004bfc8
    final PlayableSound0c sound = new PlayableSound0c();
    sound.name = name;
    sound.used_00 = true;
    sound.sshdPtr_04 = sshd;
    sound.soundBufferPtr_08 = addressInSoundBuffer / 8;

    if(sshd.soundBankSize_04 != 0) {
      SPU.directWrite(addressInSoundBuffer, soundbank.getBytes());
    }

    playableSounds_800c43d0.add(sound);
    return sound;
  }

  @Method(0x8004c114L)
  public static long sssqUnloadPlayableSound(final PlayableSound0c playableSound) {
    if(!playableSound.used_00) {
      //LAB_8004c1f0
      assert false : "Error";
      return -0x1L;
    }

    //LAB_8004c160
    for(int voiceIndex = 0; voiceIndex < SPU.voices.length; voiceIndex++) {
      final PlayingNote66 playingNote = playingNotes_800c3a40[voiceIndex];

      if(playingNote.used_00 && playingNote.playableSound_22 == playableSound) {
        //LAB_8004c1e8
        LOGGER.error("Tried to unload PlayingNote %d playableSound_22 while still in use", voiceIndex);
        LOGGER.error("", new Throwable());
        return -0x1L;
      }

      //LAB_8004c19c
    }

    LOGGER.info("Unloading playableSound %s", playableSound.name);

    playableSounds_800c43d0.remove(playableSound);
    return 0;
  }

  @Method(0x8004c1f8L)
  public static SequenceData124 loadSssq(final PlayableSound0c playableSound, final Sssq sssq) {
    //LAB_8004c258
    for(int sequenceIndex = 0; sequenceIndex < 24; sequenceIndex++) {
      final SequenceData124 sequenceData = sequenceData_800c4ac8[sequenceIndex];

      if(!sequenceData.musicLoaded_027 && !sequenceData.soundLoaded_029) {
        if(!playableSound.used_00) {
          throw new RuntimeException("Found sound but it wasn't used");
        }

        sequenceData.musicLoaded_027 = true;
        sequenceData.musicPlaying_028 = false;
        sequenceData.soundLoaded_029 = false;
        sequenceData.soundPlaying_02a = false;
        sequenceData.deltaTime_118 = 0;
        sequenceData.sssqReader_010 = sssq.reader();
        sequenceData.command_000 = sequenceData.sssqReader_010.readByte(0);
        sequenceData.previousCommand_001 = sequenceData.command_000;
        sequenceData.param0_002 = sequenceData.sssqReader_010.readByte(1);
        sequenceData.param1_003 = sequenceData.sssqReader_010.readByte(2);
        sequenceData.playableSound_020 = playableSound;

        //LAB_8004c308
        for(int n = 0; n < 16; n++) {
          sequenceData.sssqReader_010.channelInfo(n).volume_0e = sssq.volume_00 * sequenceData.sssqReader_010.channelInfo(n).volume_03 / 0x100;
        }

        sequenceData.ticksPerQuarterNote_10a = sssq.ticksPerQuarterNote_02;
        sequenceData.tempo_108 = sssq.tempo_04;
        return sequenceData;
      }

      //LAB_8004c364
    }

    //LAB_8004c380
    //LAB_8004c384
    //LAB_8004c388
    throw new RuntimeException("Didn't find sound");
  }

  @Method(0x8004c390L)
  public static long freeSequence(final SequenceData124 sequenceData) {
    if(sequenceData.musicPlaying_028) {
      assert false : "Error";
      return -0x1L;
    }

    //LAB_8004c3d0
    sequenceData.musicLoaded_027 = false;
    sequenceData.musicPlaying_028 = false;
    sequenceData.soundLoaded_029 = false;
    sequenceData.soundPlaying_02a = false;

    //LAB_8004c3e4
    return 0;
  }

  @Method(0x8004c3f0L)
  public static long setMaxSounds(final int playingSoundsUpperBound) {
    if(playingSoundsUpperBound < 0 || playingSoundsUpperBound >= 24) {
      throw new IllegalArgumentException("Must be [0, 24)");
    }

    //LAB_8004c420
    int playingSounds = 0;
    for(int voiceIndex = 0; voiceIndex < SPU.voices.length; voiceIndex++) {
      if(playingNotes_800c3a40[voiceIndex].isPolyphonicKeyPressure_1a) {
        playingSounds++;
      }

      //LAB_8004c44c
    }

    if(playingSounds > playingSoundsUpperBound) {
      //LAB_8004c484
      assert false;
      return -0x1L;
    }

    soundEnv_800c6630.playingSoundsUpperBound_03 = playingSoundsUpperBound;

    //LAB_8004c488
    return 0;
  }

  @Method(0x8004c494L)
  public static void sssqSetReverbType(final int type) {
    soundEnv_800c6630.reverbType_34 = type;

    if(type != 0) {
      SPU.setReverbMode(0);
      SPU.enableReverb();
      SPU.setReverb(reverbConfigs_80059f7c.get(type - 1).config_02);
      return;
    }

    //LAB_8004c538
    SPU.clearKeyOn();
    SPU.setReverbVolume(0, 0);
    SPU.disableReverb();
  }

  /**
   * Sets the reverb volume for left and right channels. The value ranges from 0 to 127.
   */
  @Method(0x8004c558L)
  public static void sssqSetReverbVolume(final int left, final int right) {
    if(soundEnv_800c6630.reverbType_34 != 0 && left < 0x80 && right < 0x80) {
      //LAB_8004c5d0
      SPU.setReverbVolume(left << 8, right << 8);
    }

    //LAB_8004c5d8
  }

  @Method(0x8004c894L)
  public static void setMainVolume(final int left, final int right) {
    final int l;
    if((left & 0x80) != 0) {
      l = (left << 7) + 0x7fff;
    } else {
      //LAB_8004c8a8
      l = left << 7;
    }

    //LAB_8004c8ac
    final int r;
    if((right & 0x80) != 0) {
      r = (right << 7) + 0x7fff;
    } else {
      //LAB_8004c8c0
      r = right << 7;
    }

    //LAB_8004c8c4
    SPU.setMainVolume(l, r);
  }

  @Method(0x8004c8dcL)
  public static int setSequenceVolume(@Nullable final SequenceData124 sequenceData, final int volume) {
    if(volume >= 128) {
      assert false : "Error";
      return -1;
    }

    if(sequenceData == null || !sequenceData.musicLoaded_027) {
      // This is normal
//      assert false : "Error";
      return -1;
    }

    sssqReader_800c667c = sequenceData.sssqReader_010;
    final int oldVolume = sssqReader_800c667c.baseVolume();
    sssqReader_800c667c.baseVolume(volume);

    //LAB_8004c97c
    for(int i = 0; i < 16; i++) {
      final Sssq.ChannelInfo channelInfo = sequenceData.sssqReader_010.channelInfo(i);
      channelInfo.volume_0e = channelInfo.volume_03 * volume >> 7;
    }

    //LAB_8004c9d8
    for(int voiceIndex = 0; voiceIndex < SPU.voices.length; voiceIndex++) {
      final PlayingNote66 playingNote = playingNotes_800c3a40[voiceIndex];

      if(playingNote.used_00 && playingNote.playableSound_22 == sequenceData.playableSound_020 && !playingNote.isPolyphonicKeyPressure_1a && playingNote.sequenceData_06 == sequenceData) {
        updateVoiceVolume(voiceIndex);
      }

      //LAB_8004ca44
    }

    //LAB_8004ca64
    //LAB_8004ca6c
    return (short)oldVolume;
  }

  @Method(0x8004cb0cL)
  public static long setSoundSequenceVolume(final PlayableSound0c playableSound, int volume) {
    if(volume < 0) {
      //TODO GH#193
      // This happens during the killing blow in the first virage fight. In retail, a1 counts down from 0x7f to 0 (I'm assuming it's volume).
      // In the decomp, it jumps down from 0x7f to 0 to -3. It seems like the previous script frame is setting it to a position vector value...?
      LOGGER.error("Negative volume, changing to 0");
      volume = 0;

//      throw new RuntimeException("Negative a1");
    }

    sshdPtr_800c4ac0 = playableSound.sshdPtr_04;

    if(!playableSound.used_00) {
      assert false : "Error";
      return -0x1L;
    }

    if(!playableSound.sshdPtr_04.hasSubfile(4)) {
      assert false : "Error";
      return -0x1L;
    }

    if(volume >= 0x80) {
      assert false : "Error";
      return -0x1L;
    }

    final Sssqish sssq = playableSound.sshdPtr_04.getSubfile(4, (data, offset) -> new Sssqish(data, offset, playableSound.sshdPtr_04.getSubfileSize(4)));
    sssqReader_800c667c = null; //sssq.reader(); TODO?
    final int ret = sssq.volume_00;
    sssq.volume_00 = volume;

    //LAB_8004cbc8
    for(int i = 0; i < 24; i++) {
      final Sssq.ChannelInfo channelInfo = sssq.entries_10[i];
      channelInfo.volume_0e = channelInfo.volume_03 * volume >> 7;
    }

    //LAB_8004cc1c
    for(int voiceIndex = 0; voiceIndex < SPU.voices.length; voiceIndex++) {
      final PlayingNote66 playingNote = playingNotes_800c3a40[voiceIndex];

      if(playingNote.used_00 && playingNote.isPolyphonicKeyPressure_1a && playingNote.playableSound_22 == playableSound) {
        updateVoiceVolume(voiceIndex);
      }

      //LAB_8004cc6c
    }

    //LAB_8004cc90
    return ret;
  }

  @Method(0x8004ccb0L)
  public static long sssqFadeIn(final int fadeTime, final int maxVol) {
    assert fadeTime >= 0;
    assert maxVol >= 0;

    final SoundEnv44 soundEnv = soundEnv_800c6630;

    if(fadeTime >= 0x100) {
      assert false : "Error";
      return -0x1L;
    }

    if(maxVol >= 0x80L) {
      assert false : "Error";
      return -0x1L;
    }

    if(soundEnv.fadingOut_2b) {
      assert false : "Error";
      return -0x1L;
    }

    setMainVolume(0, 0);
    soundEnv.fadingIn_2a = true;
    soundEnv.fadeTime_2c = fadeTime;
    soundEnv.fadeInVol_2e = maxVol;

    //LAB_8004cd30
    //LAB_8004cd34
    return 0;
  }

  @Method(0x8004cd50L)
  public static long sssqFadeOut(final short fadeTime) {
    if(fadeTime < 256 && !soundEnv_800c6630.fadingIn_2a) {
      soundEnv_800c6630.fadingOut_2b = true;
      soundEnv_800c6630.fadeTime_2c = fadeTime;
      soundEnv_800c6630.fadeOutVolL_30 = SPU.getMainVolumeLeft() >>> 8;
      soundEnv_800c6630.fadeOutVolR_32 = SPU.getMainVolumeRight() >>> 8;
      return 0;
    }

    //LAB_8004cdb0
    return -1;
  }

  @Method(0x8004cf8cL)
  public static void startMusicSequence(final SequenceData124 sequenceData) {
    final PlayableSound0c playableSound = sequenceData.playableSound_020;

    sshdPtr_800c4ac0 = playableSound.sshdPtr_04;

    if(sequenceData.musicLoaded_027) {
      if(sshdPtr_800c4ac0.hasSubfile(0)) {
        if(playableSound.used_00) {
          sequenceData.musicPlaying_028 = true;
          sequenceData._0e8 = false;
        }
      }
    }

    //LAB_8004d02c
    sequenceData._018 = false;
  }

  @Method(0x8004d034L)
  public static void stopMusicSequence(final SequenceData124 sequenceData, final int a1) {
    boolean resetAdsr = false;
    final PlayableSound0c playableSound = sequenceData.playableSound_020;
    final Sshd sshd = playableSound.sshdPtr_04;

    sshdPtr_800c4ac0 = sshd;

    if(sequenceData.musicLoaded_027) {
      // Retail NPE when transitioning from combat into post-combat scene (happens after Kongol I and Divine Dragon)
      if(sshd != null && sshd.hasSubfile(0)) {
        if(playableSound.used_00) {
          boolean stopNotes = false;

          if(a1 == 0) {
            //LAB_8004d13c
            sequenceData.sssqReader_010.jump(0x110);
            sequenceData.musicPlaying_028 = false;
            sequenceData._018 = true;
            sequenceData._0e8 = false;
            sequenceData.repeatCounter_035 = 0;
            stopNotes = true;
          } else if(a1 == 1) {
            //LAB_8004d134
            resetAdsr = true;

            //LAB_8004d13c
            sequenceData.sssqReader_010.jump(0x110);
            sequenceData.musicPlaying_028 = false;
            sequenceData._018 = true;
            sequenceData._0e8 = false;
            sequenceData.repeatCounter_035 = 0;
            stopNotes = true;
            //LAB_8004d11c
          } else if(a1 == 2) {
            //LAB_8004d154
            if(sequenceData.musicPlaying_028) {
              sequenceData.musicPlaying_028 = false;
              sequenceData._0e8 = true;
              stopNotes = true;
              //LAB_8004d170
            } else if(sequenceData._018) {
              stopNotes = true;
            } else {
              sequenceData.musicPlaying_028 = true;
              sequenceData._0e8 = false;
            }
          } else if(a1 == 3) {
            //LAB_8004d188
            if(sequenceData.musicPlaying_028) {
              //LAB_8004d1b4
              sequenceData.musicPlaying_028 = false;
              sequenceData._0e8 = true;
            } else if(!sequenceData._018) {
              sequenceData.musicPlaying_028 = true;
              sequenceData._0e8 = false;
            }
          }

          if(stopNotes) {
            //LAB_8004d1c0
            //LAB_8004d1c4
            //LAB_8004d1d8
            for(int voiceIndex = 0; voiceIndex < SPU.voices.length; voiceIndex++) {
              final PlayingNote66 playingNote = playingNotes_800c3a40[voiceIndex];

              if(playingNote.sequenceData_06 == sequenceData) {
                if(!playingNote.isPolyphonicKeyPressure_1a) {
                  playingNote.used_00 = false;
                  playingNote.finished_08 = true;
                  playingNote.pitchBend_38 = 0x40;
                  playingNote.modulationEnabled_14 = false;
                  playingNote.modulation_16 = 0;
                  setKeyOff(sequenceData, voiceIndex);

                  if(resetAdsr) {
                    final Voice voice = SPU.voices[voiceIndex];
                    voice.adsr.lo = 0;
                    voice.adsr.hi = 0;
                  }
                }
              }
            }

            //LAB_8004d27c
            for(int i = 0; i < 16; i++) {
              sequenceData.sssqReader_010.channelInfo(i).modulation_09 = 0;
            }

            SPU.keyOff(sequenceData.keyOff_0e2);
            sequenceData.keyOff_0e2 = 0;
          }
        }
      }
    }

    //LAB_8004d2d4
  }

  @Method(0x8004d2fcL)
  public static int startSequenceAndChangeVolumeOverTime(final SequenceData124 sequenceData, final short transitionTime, final short newVolume) {
    sssqReader_800c667c = sequenceData.sssqReader_010;

    int ret = -1;

    if(transitionTime >= 0x100 || newVolume >= 0x80) {
      throw new IllegalArgumentException("Invalid transitionTime or newVolume");
    }

    if(!sequenceData.musicPlaying_028) {
      //LAB_8004d3b0
      setSequenceVolume(sequenceData, 0);
      startMusicSequence(sequenceData);
      sequenceData.volumeIsDecreasing_03a = false;

      //LAB_8004d3c8
      ret = changeSequenceVolumeOverTime(sequenceData, transitionTime, -1, newVolume);
    } else if(sequenceData.musicPlaying_028 && sequenceData.sssqReader_010.baseVolume() < newVolume) {
      sequenceData.volumeIsDecreasing_03a = false;
      ret = changeSequenceVolumeOverTime(sequenceData, transitionTime, -1, newVolume);
    }

    //LAB_8004d3f4
    //LAB_8004d3f8
    return ret;
  }

  @Method(0x8004d41cL)
  public static int changeSequenceVolumeOverTime(final SequenceData124 sequenceData, final int transitionTime, final int newVolume) {
    assert (short)transitionTime >= 0;
    assert (short)newVolume >= 0;

    if(transitionTime >= 0x100) {
      //LAB_8004d49c
      assert false : "Error";
      return -1;
    }

    if(newVolume >= 0x80) {
      assert false : "Error";
      return -1;
    }

    if(!sequenceData.musicPlaying_028) {
      return -1;
    }

    if(newVolume == 0) {
      sequenceData.volumeIsDecreasing_03a = true;
    }

    //LAB_8004d48c
    //LAB_8004d4a4
    return changeSequenceVolumeOverTime(sequenceData, transitionTime, -1, newVolume);
  }

  @Method(0x8004d4b4L)
  public static void sssqSetTempo(final SequenceData124 sequenceData, final int tempo) {
    if(tempo <= 960) {
      sequenceData.tempo_108 = tempo;
    }
  }

  @Method(0x8004d4f8L)
  public static int sssqGetTempo(final SequenceData124 sequenceData) {
    return sequenceData.tempo_108;
  }

  @Method(0x8004d52cL)
  public static int getSequenceFlags(final SequenceData124 sequenceData) {
    int flags = 0;

    if(sequenceData.musicPlaying_028) {
      flags |= 0x1;
    }

    if(sequenceData._0e8) {
      flags |= 0x2;
    }

    if(sequenceData.volumeIsChanging_03c) {
      if(!sequenceData.volumeIsDecreasing_03a) {
        flags |= 0x4;
      } else {
        flags |= 0x8;
      }
    }

    final SoundEnv44 soundEnv = soundEnv_800c6630;

    if(soundEnv.fadingIn_2a) {
      flags |= 0x10;
    }

    if(soundEnv.fadingOut_2b) {
      flags |= 0x20;
    }

    return flags;
  }

  @Method(0x8004d648L)
  public static int startRegularSound(final PlayableSound0c playableSound, final int patchIndex, final int sequenceIndex) {
    return SEQUENCER.waitForLock(() -> loadSoundIntoSequencer(playableSound, patchIndex, sequenceIndex));
  }

  @Method(0x8004d6a8L)
  public static int startPitchShiftedSound(final PlayableSound0c playableSound, final int patchIndex, final int sequenceIndex, final int pitchShiftVolLeft, final int pitchShiftVolRight, final int pitch) {
    return SEQUENCER.waitForLock(() -> {
      final SoundEnv44 soundEnv = soundEnv_800c6630;
      //TODO was this ever actually used? I didn't see anywhere upstream that flag 0x80 could have been set
//      final int soundIndex;
//      if((playableSound & 0x80) == 0) {
//        soundIndex = playableSound;
//      } else {
//        soundIndex = playableSound & 0x7f;
//        soundEnv.reverbEnabled_23 = true;
//      }

      //LAB_8004d714
      soundEnv.pitchShiftVolLeft_26 = MathHelper.clamp(pitchShiftVolLeft, -0x1000, 0x1000);
      soundEnv.pitchShiftVolRight_28 = MathHelper.clamp(pitchShiftVolRight, -0x1000, 0x1000);
      soundEnv.pitch_24 = pitch;
      soundEnv.pitchShifted_22 = true;

      //LAB_8004d760
      return loadSoundIntoSequencer(playableSound, patchIndex & 0xffff, sequenceIndex & 0xffff);
    });
  }

  @Method(0x8004d78cL)
  public static void stopSoundSequence(final SequenceData124 sequenceData, final boolean reset) {
    SEQUENCER.waitForLock(() -> {
      if(sequenceData.soundLoaded_029) {
        sequenceData.deltaTime_118 = 0;
        sequenceData.soundPlaying_02a = false;
        sequenceData.soundLoaded_029 = false;
        sequenceData.soundEnded_0e7 = false;
        sequenceData._105 = false;
        sequenceData._104 = false;
        sequenceData._0e6 = false;
        sequenceData.repeatCounter_035 = 0;
        sequenceData.repeat_037 = false;
      }

      //LAB_8004d824
      //LAB_8004d83c
      for(int voiceIndex = 0; voiceIndex < SPU.voices.length; voiceIndex++) {
        final PlayingNote66 playingNote = playingNotes_800c3a40[voiceIndex];

        if(playingNote.isPolyphonicKeyPressure_1a && playingNote.sequenceData_06 == sequenceData) {
          //LAB_8004d880
          if(reset) {
            final Voice voice = SPU.voices[voiceIndex];
            voice.adsr.lo = 0;
            voice.adsr.hi = 0;
            playingNote.used_00 = false;
          }

          //LAB_8004d8a0
          playingNote.finished_08 = true;

          SPU.keyOff(0x1L << voiceIndex);
        }
      }

      return null;
    });

    //LAB_8004d8fc
  }

  @Method(0x8004d91cL)
  public static void FUN_8004d91c(final boolean resetVoice) {
    SEQUENCER.waitForLock(() -> {
      //LAB_8004d96c
      for(int voiceIndex = 0; voiceIndex < SPU.voices.length; voiceIndex++) {
        final PlayingNote66 playingNote = playingNotes_800c3a40[voiceIndex];

        if(playingNote.isPolyphonicKeyPressure_1a) {
          playingNote.finished_08 = true;

          if(resetVoice) {
            final Voice voice = SPU.voices[voiceIndex];
            voice.adsr.lo = 0;
            voice.adsr.hi = 0;
            playingNote.used_00 = false;
          }

          //LAB_8004d9b8
          SPU.keyOff(0x1L << voiceIndex);

          //LAB_8004d9e8
//          wasteSomeCycles(0x2L);
        }
      }

      for(int sequenceIndex = 0; sequenceIndex < 24; sequenceIndex++) {
        //LAB_8004d9f0
        final SequenceData124 sequenceData = sequenceData_800c4ac8[sequenceIndex];
        if(sequenceData.soundLoaded_029) {
          sequenceData.deltaTime_118 = 0;
          sequenceData.soundLoaded_029 = false;
          sequenceData.soundPlaying_02a = false;
          sequenceData._105 = false;
          sequenceData._104 = false;
          sequenceData.pitchShiftVolRight_0f0 = 0;
          sequenceData.pitchShiftVolLeft_0ee = 0;
          sequenceData.pitchShifted_0e9 = false;
          sequenceData.soundEnded_0e7 = false;
        }

        //LAB_8004da24
      }

      return null;
    });

    //LAB_8004da38
  }
}
