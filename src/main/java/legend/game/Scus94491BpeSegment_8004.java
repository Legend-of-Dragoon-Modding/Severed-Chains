package legend.game;

import legend.core.MathHelper;
import legend.core.memory.Method;
import legend.core.spu.Voice;
import legend.game.combat.Battle;
import legend.game.combat.Bttl_800c;
import legend.game.combat.Bttl_800d;
import legend.game.combat.Bttl_800e;
import legend.game.combat.Bttl_800f;
import legend.game.combat.SEffe;
import legend.game.credits.Credits;
import legend.game.credits.FinalFmv;
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
import legend.game.submap.SMap;
import legend.game.submap.SubmapMusic08;
import legend.game.title.GameOver;
import legend.game.title.NewGame;
import legend.game.title.Ttle;
import legend.game.types.ItemStats0c;
import legend.game.types.MoonMusic08;
import legend.game.types.OverlayStruct;
import legend.game.types.Struct10;
import legend.game.unpacker.FileData;
import legend.game.wmap.WMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

import static legend.core.GameEngine.SEQUENCER;
import static legend.core.GameEngine.SPU;
import static legend.game.Scus94491BpeSegment_8005.reverbConfigs_80059f7c;
import static legend.game.Scus94491BpeSegment_800c.patchList_800c4abc;
import static legend.game.Scus94491BpeSegment_800c.playableSounds_800c43d0;
import static legend.game.Scus94491BpeSegment_800c.playingNotes_800c3a40;
import static legend.game.Scus94491BpeSegment_800c.sequenceData_800c4ac8;
import static legend.game.Scus94491BpeSegment_800c.soundEnv_800c6630;
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
   *   <li>preload</li>
   *   <li>finalizePregameLoading</li>
   *   <li>{@link Ttle}</li>
   *   <li>{@link NewGame}</li>
   *   <li>{@link Credits}</li>
   *   <li>{@link SMap} Sets up rendering and loads scene</li>
   *   <li>{@link Battle}</li>
   *   <li>{@link GameOver}</li>
   *   <li>{@link WMap}</li>
   *   <li>startFmvLoadingStage</li>
   *   <li>swapDiskLoadingStage</li>
   *   <li>{@link FinalFmv}</li>
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
  public static final Map<EngineStateEnum, OverlayStruct> gameStateOverlays_8004dbc0 = new EnumMap<>(EngineStateEnum.class);
  static {
    //TODO this is called directly, is it needed?
//    gameStateOverlays_8004dbc0.put(EngineStateEnum.PRELOAD_00, new OverlayStruct(Scus94491BpeSegment_800e::preload));
    gameStateOverlays_8004dbc0.put(EngineStateEnum.TITLE_02, new OverlayStruct(Ttle.class, Ttle::new));
    gameStateOverlays_8004dbc0.put(EngineStateEnum.TRANSITION_TO_NEW_GAME_03, new OverlayStruct(NewGame.class, NewGame::new));
    gameStateOverlays_8004dbc0.put(EngineStateEnum.CREDITS_04, new OverlayStruct(Credits.class, Credits::new, "\\OVL\\SMAP.OV_", 0x800c6690L, 0xf9f0));
    gameStateOverlays_8004dbc0.put(EngineStateEnum.SUBMAP_05, new OverlayStruct(SMap.class, SMap::new, "\\OVL\\SMAP.OV_", 0x800c6690L, 0xf9f0));
    gameStateOverlays_8004dbc0.put(EngineStateEnum.COMBAT_06, new OverlayStruct(Battle.class, Battle::new, "\\OVL\\BTTL.OV_", 0x800c6690L, 0x668));
    gameStateOverlays_8004dbc0.put(EngineStateEnum.GAME_OVER_07, new OverlayStruct(GameOver.class, GameOver::new));
    gameStateOverlays_8004dbc0.put(EngineStateEnum.WORLD_MAP_08, new OverlayStruct(WMap.class, WMap::new, "\\OVL\\WMAP.OV_", 0x800c6690L, 0x2070));
    gameStateOverlays_8004dbc0.put(EngineStateEnum.FINAL_FMV_11, new OverlayStruct(FinalFmv.class, FinalFmv::new));
  }

  public static EngineState currentEngineState_8004dd04;
  public static boolean dontZeroMemoryOnOverlayLoad_8004dd0c;
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
  public static EngineStateEnum engineState_8004dd20 = EngineStateEnum.PRELOAD_00;
  /** When the overlay finishes loading, switch to this */
  public static EngineStateEnum engineStateOnceLoaded_8004dd24 = EngineStateEnum.PRELOAD_00;
  /** The previous state before the file finished loading */
  public static EngineStateEnum previousEngineState_8004dd28;

  public static int width_8004dd34 = 320;
  public static int height_8004dd34 = 240;

  public static int reinitOrderingTableBits_8004dd38 = 14;

  public static Runnable syncFrame_8004dd3c;
  public static Runnable swapDisplayBuffer_8004dd40;
  public static int simpleRandSeed_8004dd44 = 3;
  public static final int[] _8004dd48 = {0, 1, 2, 1, 2, 1, 2};

  public static final Function<RunningScript, FlowControl>[] scriptSubFunctions_8004e29c = new Function[1024];
  public static Function<RunningScript, FlowControl>[] engineStateFunctions_8004e29c = new Function[1024];
  static {
    Arrays.setAll(scriptSubFunctions_8004e29c, i -> Scus94491BpeSegment::scriptRewindAndPause2);

    scriptSubFunctions_8004e29c[0] = Scus94491BpeSegment::scriptSetIndicatorsDisabled;
    scriptSubFunctions_8004e29c[1] = Scus94491BpeSegment::scriptReadIndicatorsDisabled;
    scriptSubFunctions_8004e29c[2] = Scus94491BpeSegment::scriptSetGlobalFlag1;
    scriptSubFunctions_8004e29c[3] = Scus94491BpeSegment::scriptReadGlobalFlag1;
    scriptSubFunctions_8004e29c[4] = Scus94491BpeSegment::scriptSetGlobalFlag2;
    scriptSubFunctions_8004e29c[5] = Scus94491BpeSegment::scriptReadGlobalFlag2;
    scriptSubFunctions_8004e29c[6] = Scus94491BpeSegment::scriptStartFadeEffect;
    scriptSubFunctions_8004e29c[7] = Scus94491BpeSegment::scriptWaitForFilesToLoad;
    scriptSubFunctions_8004e29c[8] = Scus94491BpeSegment::FUN_80017584;
    scriptSubFunctions_8004e29c[9] = Scus94491BpeSegment::scriptSetFlag;
    scriptSubFunctions_8004e29c[10] = Scus94491BpeSegment::scriptReadFlag;
    scriptSubFunctions_8004e29c[11] = Scus94491BpeSegment::FUN_80017688;

    scriptSubFunctions_8004e29c[16] = Scus94491BpeSegment::FUN_800176c0;
    scriptSubFunctions_8004e29c[17] = Scus94491BpeSegment::FUN_800176ec;

    scriptSubFunctions_8004e29c[32] = Bttl_800d::scriptResetCameraMovement;
    scriptSubFunctions_8004e29c[33] = Bttl_800d::FUN_800dac20;
    scriptSubFunctions_8004e29c[34] = Bttl_800d::FUN_800db034;
    scriptSubFunctions_8004e29c[35] = Bttl_800d::FUN_800db460;
    scriptSubFunctions_8004e29c[36] = Bttl_800d::FUN_800db574;
    scriptSubFunctions_8004e29c[37] = Bttl_800d::FUN_800db688;
    scriptSubFunctions_8004e29c[38] = Bttl_800d::FUN_800db79c;
    scriptSubFunctions_8004e29c[39] = Bttl_800d::FUN_800db8b0;
    scriptSubFunctions_8004e29c[40] = Bttl_800d::FUN_800db9e0;
    scriptSubFunctions_8004e29c[41] = Bttl_800d::scriptIsCameraMoving;
    scriptSubFunctions_8004e29c[42] = Bttl_800d::scriptCalculateCameraValue;
    scriptSubFunctions_8004e29c[43] = Bttl_800d::FUN_800dbb9c;
    scriptSubFunctions_8004e29c[44] = Bttl_800d::scriptWobbleCamera;
    scriptSubFunctions_8004e29c[45] = Bttl_800d::scriptStopCameraMovement;
    scriptSubFunctions_8004e29c[46] = Bttl_800d::scriptSetViewportTwist;
    scriptSubFunctions_8004e29c[47] = Bttl_800d::FUN_800dbc80;
    scriptSubFunctions_8004e29c[48] = Bttl_800d::scriptSetCameraProjectionPlaneDistance;
    scriptSubFunctions_8004e29c[49] = Bttl_800d::scriptGetProjectionPlaneDistance;
    scriptSubFunctions_8004e29c[50] = Bttl_800d::scriptMoveCameraProjectionPlane;

    scriptSubFunctions_8004e29c[128] = Bttl_800c::scriptSetBentPos;
    scriptSubFunctions_8004e29c[129] = Bttl_800c::scriptGetBentPos;
    scriptSubFunctions_8004e29c[130] = Bttl_800c::scriptSetBentRotation;
    scriptSubFunctions_8004e29c[131] = Bttl_800c::FUN_800cc7d8;
    scriptSubFunctions_8004e29c[132] = Bttl_800c::scriptSetBentRotationY;
    scriptSubFunctions_8004e29c[133] = Bttl_800c::loadAllCharAttackAnimations;
    scriptSubFunctions_8004e29c[134] = Bttl_800c::scriptGetBentRotation;
    scriptSubFunctions_8004e29c[135] = Scus94491BpeSegment::scriptRewindAndPause2;
    scriptSubFunctions_8004e29c[136] = Bttl_800c::scriptGetMonsterStatusResistFlags;
    scriptSubFunctions_8004e29c[137] = Scus94491BpeSegment::scriptRewindAndPause2;
    scriptSubFunctions_8004e29c[138] = Bttl_800c::FUN_800cb618;
    scriptSubFunctions_8004e29c[139] = Bttl_800c::scriptSetInterpolationEnabled;
    scriptSubFunctions_8004e29c[140] = Bttl_800c::FUN_800cb6bc;
    scriptSubFunctions_8004e29c[141] = Bttl_800c::FUN_800cb764;
    scriptSubFunctions_8004e29c[142] = Bttl_800c::FUN_800cb76c;
    scriptSubFunctions_8004e29c[143] = Bttl_800c::scriptGetLoadingBentAnimationIndex;
    scriptSubFunctions_8004e29c[144] = Bttl_800c::scriptPauseAnimation;
    scriptSubFunctions_8004e29c[145] = Bttl_800c::scriptResumeAnimation;
    scriptSubFunctions_8004e29c[146] = Bttl_800c::scriptSetBentAnimationLoopState;
    scriptSubFunctions_8004e29c[147] = Bttl_800c::scriptAnimationHasFinished;
    scriptSubFunctions_8004e29c[148] = Bttl_800c::FUN_800cbb00;
    scriptSubFunctions_8004e29c[149] = Bttl_800c::FUN_800cbc14;
    scriptSubFunctions_8004e29c[150] = Bttl_800c::FUN_800cbde0;
    scriptSubFunctions_8004e29c[151] = Bttl_800c::FUN_800cbef8;
    scriptSubFunctions_8004e29c[152] = Bttl_800c::FUN_800cc0c8;
    scriptSubFunctions_8004e29c[153] = Bttl_800c::FUN_800cc1cc;
    scriptSubFunctions_8004e29c[154] = Bttl_800c::FUN_800cc364;
    scriptSubFunctions_8004e29c[155] = Bttl_800c::FUN_800cc46c;
    scriptSubFunctions_8004e29c[156] = Bttl_800c::scriptBentLookAtBent;
    scriptSubFunctions_8004e29c[157] = Bttl_800c::FUN_800cc698;
    scriptSubFunctions_8004e29c[158] = Bttl_800c::FUN_800cc784;
    scriptSubFunctions_8004e29c[159] = Bttl_800c::scriptLoadAttackAnimations;
    scriptSubFunctions_8004e29c[160] = Bttl_800c::scriptSetUpAndHandleCombatMenu;
    scriptSubFunctions_8004e29c[161] = Scus94491BpeSegment::scriptRewindAndPause2;
    scriptSubFunctions_8004e29c[162] = Scus94491BpeSegment::scriptRewindAndPause2;
    scriptSubFunctions_8004e29c[163] = Scus94491BpeSegment::scriptRewindAndPause2;
    scriptSubFunctions_8004e29c[164] = Bttl_800c::scriptRenderDamage;
    scriptSubFunctions_8004e29c[165] = Bttl_800c::scriptAddFloatingNumberForBent;
    scriptSubFunctions_8004e29c[166] = Bttl_800c::FUN_800ccba4;
    scriptSubFunctions_8004e29c[167] = Bttl_800c::scriptGetCharOrMonsterId;
    scriptSubFunctions_8004e29c[168] = Bttl_800c::scriptSetBentStat;
    scriptSubFunctions_8004e29c[169] = Bttl_800c::scriptGetBentStat;
    scriptSubFunctions_8004e29c[170] = Bttl_800c::scriptSetPostBattleAction;
    scriptSubFunctions_8004e29c[171] = Bttl_800c::FUN_800ccec8;
    scriptSubFunctions_8004e29c[172] = Bttl_800c::FUN_800ccef8;
    scriptSubFunctions_8004e29c[173] = Bttl_800c::scriptSetBentDeadAndDropLoot;
    scriptSubFunctions_8004e29c[174] = Bttl_800c::scriptGetHitProperty;
    scriptSubFunctions_8004e29c[175] = Bttl_800c::scriptSetBentDead;
    scriptSubFunctions_8004e29c[176] = Bttl_800c::scriptLevelUpAddition;
    scriptSubFunctions_8004e29c[177] = Bttl_800c::scriptGetBentStat2;
    scriptSubFunctions_8004e29c[178] = Bttl_800c::scriptSetBentRawStat;

    scriptSubFunctions_8004e29c[192] = Scus94491BpeSegment_8002::scriptGetFreeTextboxIndex;
    scriptSubFunctions_8004e29c[193] = Scus94491BpeSegment_8002::scriptInitTextbox;
    scriptSubFunctions_8004e29c[194] = Scus94491BpeSegment_8002::scriptSetTextboxContents;
    scriptSubFunctions_8004e29c[195] = Scus94491BpeSegment_8002::scriptIsTextboxInitialized;
    scriptSubFunctions_8004e29c[196] = Scus94491BpeSegment_8002::scriptGetTextboxState;
    scriptSubFunctions_8004e29c[197] = Scus94491BpeSegment_8002::scriptGetTextboxTextState;

    scriptSubFunctions_8004e29c[199] = Scus94491BpeSegment_8002::scriptSetTextboxVariable;
    scriptSubFunctions_8004e29c[200] = Scus94491BpeSegment_8002::scriptAddTextbox;
    scriptSubFunctions_8004e29c[201] = Scus94491BpeSegment_8002::scriptDeallocateTextbox;
    scriptSubFunctions_8004e29c[202] = Scus94491BpeSegment_8002::scriptDeallocateAllTextboxes;
    scriptSubFunctions_8004e29c[203] = Scus94491BpeSegment_8002::FUN_80029ecc;
    scriptSubFunctions_8004e29c[204] = Scus94491BpeSegment_8002::FUN_80028ff8;
    scriptSubFunctions_8004e29c[205] = Scus94491BpeSegment_8002::scriptGetTextboxSelectionIndex;
    scriptSubFunctions_8004e29c[206] = Scus94491BpeSegment_8002::scriptGetTextboxElement;
    scriptSubFunctions_8004e29c[207] = Scus94491BpeSegment_8002::scriptAddSelectionTextbox;

    scriptSubFunctions_8004e29c[224] = Scus94491BpeSegment::scriptLoadMenuSounds;
    scriptSubFunctions_8004e29c[225] = Scus94491BpeSegment::FUN_8001e918;
    scriptSubFunctions_8004e29c[226] = Scus94491BpeSegment::scriptLoadCharAttackSounds;
    scriptSubFunctions_8004e29c[227] = Scus94491BpeSegment::FUN_8001eb30;
    scriptSubFunctions_8004e29c[228] = Scus94491BpeSegment::scriptLoadBattleCutsceneSounds;
    scriptSubFunctions_8004e29c[229] = Scus94491BpeSegment::scriptLoadMonsterAttackSounds;
    scriptSubFunctions_8004e29c[230] = Scus94491BpeSegment::scriptLoadMusicPackage;
    scriptSubFunctions_8004e29c[231] = Scus94491BpeSegment::FUN_8001fe28;
    scriptSubFunctions_8004e29c[232] = Scus94491BpeSegment::scriptUnloadSoundFile;
    scriptSubFunctions_8004e29c[233] = Scus94491BpeSegment_8002::scriptUnuseCharSoundFile;
    scriptSubFunctions_8004e29c[234] = Scus94491BpeSegment_8002::scriptStopEncounterSoundEffects;
    scriptSubFunctions_8004e29c[235] = Scus94491BpeSegment_8002::scriptFreeEncounterSoundEffects;
    scriptSubFunctions_8004e29c[236] = Scus94491BpeSegment::scriptPlaySound;
    scriptSubFunctions_8004e29c[237] = Scus94491BpeSegment::scriptStopSound;
    scriptSubFunctions_8004e29c[238] = Scus94491BpeSegment::scriptPlayBentSound;
    scriptSubFunctions_8004e29c[239] = Scus94491BpeSegment::scriptStopBentSound;
    scriptSubFunctions_8004e29c[240] = Scus94491BpeSegment::scriptStopSoundsAndSequences;
    scriptSubFunctions_8004e29c[241] = Scus94491BpeSegment::scriptStartCurrentMusicSequence;
    scriptSubFunctions_8004e29c[242] = Scus94491BpeSegment::scriptStopCurrentMusicSequence;
    scriptSubFunctions_8004e29c[243] = Scus94491BpeSegment::scriptStopCurrentMusicSequence2;
    scriptSubFunctions_8004e29c[244] = Scus94491BpeSegment::FUN_8001aec8;
    scriptSubFunctions_8004e29c[245] = Scus94491BpeSegment::scriptStartEncounterSounds;
    scriptSubFunctions_8004e29c[246] = Scus94491BpeSegment::scriptStopEncounterSounds;
    scriptSubFunctions_8004e29c[247] = Scus94491BpeSegment::scriptStopEncounterSounds2;
    scriptSubFunctions_8004e29c[248] = Scus94491BpeSegment::FUN_8001b094;
    scriptSubFunctions_8004e29c[249] = Scus94491BpeSegment::FUN_8001b134;
    scriptSubFunctions_8004e29c[250] = Scus94491BpeSegment::FUN_8001b13c;
    scriptSubFunctions_8004e29c[251] = Scus94491BpeSegment::FUN_8001b144;
    scriptSubFunctions_8004e29c[252] = Scus94491BpeSegment::scriptSetMainVolume;
    scriptSubFunctions_8004e29c[253] = Scus94491BpeSegment::scriptSetSequenceVolume;
    scriptSubFunctions_8004e29c[254] = Scus94491BpeSegment::scriptSetAllSoundSequenceVolumes;
    scriptSubFunctions_8004e29c[255] = Scus94491BpeSegment::scriptSssqFadeIn;

    scriptSubFunctions_8004e29c[320] = Bttl_800c::scriptEnableBentTextureAnimation;
    scriptSubFunctions_8004e29c[321] = Scus94491BpeSegment::scriptRewindAndPause2;
    scriptSubFunctions_8004e29c[322] = Bttl_800c::scriptSetLoadingBentAnimationIndex;
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
    scriptSubFunctions_8004e29c[360] = Bttl_800c::scriptAllocateBent;
    scriptSubFunctions_8004e29c[361] = Bttl_800c::FUN_800cd740;
    scriptSubFunctions_8004e29c[362] = Bttl_800c::FUN_800cd7a8;
    scriptSubFunctions_8004e29c[363] = Bttl_800c::FUN_800cd810;
    scriptSubFunctions_8004e29c[364] = Bttl_800c::FUN_800cd8a4;
    scriptSubFunctions_8004e29c[365] = Bttl_800c::scriptGetBentNobj;
    scriptSubFunctions_8004e29c[366] = Bttl_800c::scriptDeallocateCombatant;
    scriptSubFunctions_8004e29c[367] = Bttl_800c::scriptStopRenderingStage;
    scriptSubFunctions_8004e29c[368] = Bttl_800c::scriptLoadStage;
    scriptSubFunctions_8004e29c[369] = Bttl_800c::FUN_800cd910;
    scriptSubFunctions_8004e29c[370] = Bttl_800c::scriptGetCombatantIndex;
    scriptSubFunctions_8004e29c[371] = Bttl_800c::scriptGetBentSlot;
    scriptSubFunctions_8004e29c[372] = Bttl_800c::scriptDisableCombat;

    scriptSubFunctions_8004e29c[416] = Bttl_800e::FUN_800e6fb4;

    scriptSubFunctions_8004e29c[419] = Bttl_800e::scriptResetLights;
    scriptSubFunctions_8004e29c[420] = Bttl_800e::scriptSetLightDirection;
    scriptSubFunctions_8004e29c[421] = Bttl_800e::scriptGetLightDirection;
    scriptSubFunctions_8004e29c[422] = Bttl_800e::FUN_800e48a8;
    scriptSubFunctions_8004e29c[423] = Bttl_800e::FUN_800e48e8;
    scriptSubFunctions_8004e29c[424] = Bttl_800e::FUN_800e4964;
    scriptSubFunctions_8004e29c[425] = Bttl_800e::FUN_800e4abc;
    scriptSubFunctions_8004e29c[426] = Bttl_800e::scriptSetBattleLightColour;
    scriptSubFunctions_8004e29c[427] = Bttl_800e::scriptGetBattleLightColour;
    scriptSubFunctions_8004e29c[428] = Bttl_800e::scriptSetBattleBackgroundLightColour;
    scriptSubFunctions_8004e29c[429] = Bttl_800e::scriptGetBattleBackgroundLightColour;
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
    scriptSubFunctions_8004e29c[443] = Bttl_800e::scriptApplyStageAmbiance;
    scriptSubFunctions_8004e29c[444] = Bttl_800e::FUN_800e59d8;

    // scriptSubFunctions_8004e29c[445] = Temp::FUN_800ca734;

    scriptSubFunctions_8004e29c[480] = Bttl_800f::scriptCheckPhysicalHit;
    scriptSubFunctions_8004e29c[481] = Bttl_800f::scriptPhysicalAttack;
    scriptSubFunctions_8004e29c[482] = Bttl_800f::scriptGetBentPos;
    scriptSubFunctions_8004e29c[483] = Bttl_800f::scriptAddFloatingNumber;
    scriptSubFunctions_8004e29c[484] = Bttl_800f::scriptCheckPhysicalHit;
    scriptSubFunctions_8004e29c[485] = Bttl_800f::scriptCheckPhysicalHit;
    scriptSubFunctions_8004e29c[486] = Bttl_800f::scriptCheckPhysicalHit;
    scriptSubFunctions_8004e29c[487] = Bttl_800f::scriptGiveSp;
    scriptSubFunctions_8004e29c[488] = Bttl_800f::scriptConsumeSp;
    scriptSubFunctions_8004e29c[489] = Bttl_800f::scriptInitSpellAndItemMenu;
    scriptSubFunctions_8004e29c[490] = Bttl_800f::FUN_800f4600;
    scriptSubFunctions_8004e29c[491] = Bttl_800f::scriptGetItemOrSpellAttackTarget;
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
    scriptSubFunctions_8004e29c[505] = Bttl_800f::scriptRenderBattleHudBackground;
    scriptSubFunctions_8004e29c[506] = Bttl_800f::FUN_800f9cac;
    scriptSubFunctions_8004e29c[507] = Bttl_800f::scriptCheckSpellOrStatusHit;
    scriptSubFunctions_8004e29c[508] = Bttl_800f::scriptCheckItemHit;
    scriptSubFunctions_8004e29c[509] = Bttl_800f::scriptFinishBentTurn;

    scriptSubFunctions_8004e29c[512] = Bttl_800e::scriptSetBentZOffset;
    scriptSubFunctions_8004e29c[513] = Bttl_800e::scriptSetBentScaleUniform;
    scriptSubFunctions_8004e29c[514] = Bttl_800e::scriptSetBentScale;
    scriptSubFunctions_8004e29c[515] = Bttl_800e::FUN_800ee384;
    scriptSubFunctions_8004e29c[516] = Bttl_800e::scriptDisableBentShadow;
    scriptSubFunctions_8004e29c[517] = Bttl_800e::scriptSetBentShadowSize;
    scriptSubFunctions_8004e29c[518] = Bttl_800e::scriptSetBentShadowOffset;
    scriptSubFunctions_8004e29c[519] = Bttl_800e::scriptApplyScreenDarkening;
    scriptSubFunctions_8004e29c[520] = Bttl_800e::FUN_800ee384;
    scriptSubFunctions_8004e29c[521] = Bttl_800e::scriptGetStageNobj;
    scriptSubFunctions_8004e29c[522] = Bttl_800e::scriptShowStageModelPart;
    scriptSubFunctions_8004e29c[523] = Bttl_800e::scriptHideStageModelPart;
    scriptSubFunctions_8004e29c[524] = Bttl_800e::FUN_800ee3c0;
    scriptSubFunctions_8004e29c[525] = Bttl_800e::FUN_800ee408;
    scriptSubFunctions_8004e29c[526] = Bttl_800e::scriptSetStageZ;

    scriptSubFunctions_8004e29c[544] = SEffe::scriptGetRelativePosition;
    scriptSubFunctions_8004e29c[545] = SEffe::scriptSetRelativePosition;
    scriptSubFunctions_8004e29c[546] = SEffe::scriptGetRotationDifference;
    scriptSubFunctions_8004e29c[547] = SEffe::scriptSetRelativeRotation;
    scriptSubFunctions_8004e29c[548] = SEffe::scriptGetScaleRatio;
    scriptSubFunctions_8004e29c[549] = SEffe::scriptSetRelativeScale;
    scriptSubFunctions_8004e29c[550] = SEffe::scriptGetColourDifference;
    scriptSubFunctions_8004e29c[551] = SEffe::scriptSetRelativeColour;
    scriptSubFunctions_8004e29c[552] = SEffe::scriptGetGenericEffectValue;
    scriptSubFunctions_8004e29c[553] = SEffe::scriptSetGenericEffectValue;
    scriptSubFunctions_8004e29c[554] = SEffe::scriptTransformBobjPositionToScreenSpace;
    scriptSubFunctions_8004e29c[555] = SEffe::scriptRemoveEffectAttachment;
    scriptSubFunctions_8004e29c[556] = SEffe::scriptHasEffectAttachment;
    scriptSubFunctions_8004e29c[557] = SEffe::scriptWaitForPositionScalerToFinish;
    scriptSubFunctions_8004e29c[558] = SEffe::scriptAddPositionScalerAttachment;
    scriptSubFunctions_8004e29c[559] = SEffe::scriptAddRelativePositionScalerTicks0;
    scriptSubFunctions_8004e29c[560] = SEffe::scriptAddRelativePositionScalerDistance0;
    scriptSubFunctions_8004e29c[561] = SEffe::scriptAddPositionScalerMoveToParent;
    scriptSubFunctions_8004e29c[562] = SEffe::FUN_801155f8; // no-op
    scriptSubFunctions_8004e29c[563] = SEffe::scriptGetRelativeAngleBetweenBobjs;
    scriptSubFunctions_8004e29c[564] = SEffe::scriptRotateBobjTowardsPoint;
    scriptSubFunctions_8004e29c[565] = SEffe::FUN_80115440;
    scriptSubFunctions_8004e29c[566] = SEffe::scriptGetEffectTranslationRelativeToParent;
    scriptSubFunctions_8004e29c[567] = SEffe::scriptAddRotationScalerAttachment;
    scriptSubFunctions_8004e29c[568] = SEffe::scriptAddRotationScalerAttachmentTicks;
    scriptSubFunctions_8004e29c[569] = SEffe::scriptAddRotationScalerAttachmentDistance;
    scriptSubFunctions_8004e29c[570] = SEffe::scriptAddRotationScalerAttachmentTowardsPointTicks;
    scriptSubFunctions_8004e29c[571] = SEffe::scriptAddRotationScalerAttachmentTowardsPointDistance;
    scriptSubFunctions_8004e29c[572] = SEffe::FUN_80115600; // no-op
    scriptSubFunctions_8004e29c[573] = Bttl_800e::FUN_800e9f68;
    scriptSubFunctions_8004e29c[574] = SEffe::scriptLoadEffectModelAnimation;
    scriptSubFunctions_8004e29c[575] = SEffe::scriptAddScaleScalerAttachment;

    scriptSubFunctions_8004e29c[576] = SEffe::scriptAddScaleScalerMultiplicativeAttachmentTicks;
    scriptSubFunctions_8004e29c[577] = SEffe::scriptAddScaleScalerMultiplicativeAttachmentDistance; // not implemented
    scriptSubFunctions_8004e29c[578] = SEffe::scriptSetScriptScript;
    scriptSubFunctions_8004e29c[579] = SEffe::scriptAddScaleScalerDifferenceAttachmentTicks;
    scriptSubFunctions_8004e29c[580] = SEffe::scriptAddColourScalerAttachment;
    scriptSubFunctions_8004e29c[581] = SEffe::scriptAddConstantColourScalerAttachment;
    scriptSubFunctions_8004e29c[582] = SEffe::scriptAddConstantColourScalerDistance; // not implemented
    scriptSubFunctions_8004e29c[583] = SEffe::scriptRemoveGenericEffectAttachment;
    scriptSubFunctions_8004e29c[584] = SEffe::FUN_80114f34; // no-op
    scriptSubFunctions_8004e29c[585] = SEffe::scriptAddGenericAttachment;
    scriptSubFunctions_8004e29c[586] = SEffe::scriptAddGenericAttachmentTicks;
    scriptSubFunctions_8004e29c[587] = SEffe::scriptAddGenericAttachmentSpeed;
    scriptSubFunctions_8004e29c[588] = SEffe::scriptAddLifespanAttachment;
    scriptSubFunctions_8004e29c[589] = SEffe::FUN_80115324;
    scriptSubFunctions_8004e29c[590] = SEffe::FUN_80115388;
    scriptSubFunctions_8004e29c[591] = SEffe::FUN_801153e4;
    scriptSubFunctions_8004e29c[592] = Bttl_800e::FUN_800e74ac;
    scriptSubFunctions_8004e29c[593] = SEffe::scriptGetPositionScalerAttachmentVelocity;
    scriptSubFunctions_8004e29c[594] = Bttl_800e::scriptAddOrUpdateTextureAnimationAttachment;
    scriptSubFunctions_8004e29c[595] = SEffe::FUN_8011549c;
    scriptSubFunctions_8004e29c[596] = SEffe::scriptAddRelativePositionScalerDistance1;
    scriptSubFunctions_8004e29c[597] = SEffe::scriptAddRelativePositionScalerTicks1;
    scriptSubFunctions_8004e29c[598] = SEffe::scriptAddOrUpdatePositionScalerAttachment;
    scriptSubFunctions_8004e29c[599] = SEffe::scriptUpdateParabolicPositionScalerAttachment;
    scriptSubFunctions_8004e29c[600] = Bttl_800e::scriptAllocateEmptyEffectManagerChild;
    scriptSubFunctions_8004e29c[601] = Bttl_800e::allocateBillboardSpriteEffect;
    scriptSubFunctions_8004e29c[602] = Bttl_800e::FUN_800e9854;
    // scriptSubFunctions_8004e29c[603] = Temp::FUN_800ca648;

    scriptSubFunctions_8004e29c[605] = SEffe::scriptAllocateLmbAnimation;
    scriptSubFunctions_8004e29c[606] = SEffe::allocateDeffTmd;
    scriptSubFunctions_8004e29c[607] = Bttl_800e::FUN_800e99bc;

    scriptSubFunctions_8004e29c[608] = SEffe::FUN_801181a8;

    scriptSubFunctions_8004e29c[610] = Bttl_800e::scriptLoadCmbAnimation;
    scriptSubFunctions_8004e29c[611] = SEffe::FUN_801156f8;
    scriptSubFunctions_8004e29c[612] = Bttl_800e::scriptHideEffectModelPart;
    scriptSubFunctions_8004e29c[613] = Bttl_800e::scriptShowEffectModelPart;
    scriptSubFunctions_8004e29c[614] = Bttl_800e::scriptAddRedEyeDragoonTransformationFlameArmorEffectAttachment;
    scriptSubFunctions_8004e29c[615] = Bttl_800e::scriptApplyTextureAnimationAttachment;
    scriptSubFunctions_8004e29c[616] = Bttl_800e::scriptRemoveTextureAnimationAttachment;
    // scriptSubFunctions_8004e29c[617] = Temp::FUN_800caae4;
    scriptSubFunctions_8004e29c[618] = SEffe::scriptLoadSameScriptAndJump;
    scriptSubFunctions_8004e29c[619] = SEffe::allocateShirleyTransformWipeEffect;
    scriptSubFunctions_8004e29c[620] = SEffe::FUN_80111a58;
    scriptSubFunctions_8004e29c[621] = Bttl_800e::scriptGetEffectLoopCount;
    scriptSubFunctions_8004e29c[622] = SEffe::allocateSpriteWithTrailEffect;
    scriptSubFunctions_8004e29c[623] = Bttl_800e::scriptLoadDeff;
    scriptSubFunctions_8004e29c[624] = Bttl_800e::FUN_800e6db4;
    scriptSubFunctions_8004e29c[625] = Bttl_800e::scriptGetDeffLoadingStage;
    scriptSubFunctions_8004e29c[626] = SEffe::scriptGetEffectZ;
    scriptSubFunctions_8004e29c[627] = SEffe::scriptSetEffectZ;
    scriptSubFunctions_8004e29c[628] = SEffe::allocateDeffTmdRenderer;
    scriptSubFunctions_8004e29c[629] = SEffe::FUN_801157d0;
    scriptSubFunctions_8004e29c[630] = SEffe::scriptGetEffectRotation;
    scriptSubFunctions_8004e29c[631] = SEffe::FUN_801181f0;
    scriptSubFunctions_8004e29c[632] = SEffe::scriptAllocateBuggedEffect;

    scriptSubFunctions_8004e29c[634] = SEffe::scriptWaitForXaToLoad;
    scriptSubFunctions_8004e29c[635] = SEffe::scriptGetXaLoadingStage;
    scriptSubFunctions_8004e29c[636] = SEffe::scriptPlayXaAudio;
    scriptSubFunctions_8004e29c[637] = Bttl_800e::scriptLoadCutsceneDeff;
    scriptSubFunctions_8004e29c[638] = Bttl_800e::scriptLoadSpellOrItemDeff;
    scriptSubFunctions_8004e29c[639] = Bttl_800e::scriptLoadEnemyOrBossDeff;

    scriptSubFunctions_8004e29c[640] = SEffe::scriptConvertRotationYxzToXyz;
    scriptSubFunctions_8004e29c[641] = SEffe::FUN_80115a28;
    scriptSubFunctions_8004e29c[642] = SEffe::FUN_8011287c;
    scriptSubFunctions_8004e29c[643] = Bttl_800e::FUN_800e9798;
    scriptSubFunctions_8004e29c[644] = Bttl_800e::scriptSetBttlShadowSize;
    scriptSubFunctions_8004e29c[645] = Bttl_800e::scriptSetBttlShadowOffset;
    scriptSubFunctions_8004e29c[646] = SEffe::scriptAllocateShadowEffect;
    scriptSubFunctions_8004e29c[647] = SEffe::scriptUpdateDeffManagerFlags;
    scriptSubFunctions_8004e29c[648] = SEffe::scriptLoadDeffStageEffects;
    scriptSubFunctions_8004e29c[649] = SEffe::scriptGetEffectTextureMetrics;
    scriptSubFunctions_8004e29c[650] = SEffe::FUN_801154f4;
    scriptSubFunctions_8004e29c[651] = SEffe::scriptConsolidateEffectMemory;

    scriptSubFunctions_8004e29c[704] = Scus94491BpeSegment::scriptStartSequenceAndChangeVolumeOverTime;
    scriptSubFunctions_8004e29c[705] = Scus94491BpeSegment::scriptSssqFadeOut;
    scriptSubFunctions_8004e29c[706] = Scus94491BpeSegment::scriptChangeSequenceVolumeOverTime;
    scriptSubFunctions_8004e29c[707] = Scus94491BpeSegment::scriptGetSequenceFlags;
    scriptSubFunctions_8004e29c[708] = Scus94491BpeSegment::scriptGetSssqTempoScale;
    scriptSubFunctions_8004e29c[709] = Scus94491BpeSegment::scriptSetSssqTempoScale;
    scriptSubFunctions_8004e29c[710] = Scus94491BpeSegment::scriptGetLoadedSoundFiles;
    scriptSubFunctions_8004e29c[711] = Scus94491BpeSegment::scriptGetSequenceVolume;
    scriptSubFunctions_8004e29c[712] = Scus94491BpeSegment::scriptPlayCombatantSound;
    scriptSubFunctions_8004e29c[713] = Scus94491BpeSegment::scriptStopBentSound2;
    scriptSubFunctions_8004e29c[714] = Scus94491BpeSegment_8002::scriptStopAndUnloadSequences;
    scriptSubFunctions_8004e29c[715] = Scus94491BpeSegment::scriptLoadCharacterAttackSounds;
    scriptSubFunctions_8004e29c[716] = Scus94491BpeSegment_8002::scriptReplaceMonsterSounds;
    scriptSubFunctions_8004e29c[717] = Scus94491BpeSegment::scriptLoadCutsceneSounds;
    scriptSubFunctions_8004e29c[718] = Scus94491BpeSegment::scriptLoadFinalBattleSounds;

    scriptSubFunctions_8004e29c[736] = Bttl_800d::FUN_800d3090;
    scriptSubFunctions_8004e29c[737] = Bttl_800c::scriptAllocateFullScreenOverlay;
    scriptSubFunctions_8004e29c[738] = Bttl_800c::scriptRand;
    scriptSubFunctions_8004e29c[739] = Bttl_800c::scriptSetWeaponTrailSegmentCount;
    scriptSubFunctions_8004e29c[740] = Bttl_800d::FUN_800d3098;
    scriptSubFunctions_8004e29c[741] = Bttl_800d::FUN_800d30a0;
    scriptSubFunctions_8004e29c[742] = Bttl_800d::FUN_800d30a8;
    scriptSubFunctions_8004e29c[743] = Bttl_800d::FUN_800d30b0;
    scriptSubFunctions_8004e29c[744] = Bttl_800c::scriptRenderColouredQuad;
    scriptSubFunctions_8004e29c[745] = Bttl_800c::FUN_800cf0b4;
    scriptSubFunctions_8004e29c[746] = SEffe::scriptAllocateParticleEffect;
    scriptSubFunctions_8004e29c[747] = SEffe::scriptSetParticleAcceleration;
    scriptSubFunctions_8004e29c[748] = Bttl_800d::FUN_800d30b8;
    scriptSubFunctions_8004e29c[749] = Bttl_800d::scriptAllocateProjectileHitEffect;
    scriptSubFunctions_8004e29c[750] = Bttl_800d::FUN_800d09b8;
    scriptSubFunctions_8004e29c[751] = Bttl_800d::scriptAllocateAdditionSparksEffect;
    scriptSubFunctions_8004e29c[752] = SEffe::FUN_80102608;
    scriptSubFunctions_8004e29c[753] = SEffe::scriptAllocateAdditionOverlaysEffect;
    scriptSubFunctions_8004e29c[754] = SEffe::scriptGetHitCompletionState;
    scriptSubFunctions_8004e29c[755] = SEffe::FUN_80108de8;
    scriptSubFunctions_8004e29c[756] = Bttl_800d::scriptAllocateAdditionStarburstEffect;
    scriptSubFunctions_8004e29c[757] = Bttl_800d::FUN_800d1cac;
    scriptSubFunctions_8004e29c[758] = Bttl_800d::FUN_800d1cf4;
    scriptSubFunctions_8004e29c[759] = SEffe::scriptAlterAdditionContinuationState;
    scriptSubFunctions_8004e29c[760] = SEffe::FUN_80108df0;
    scriptSubFunctions_8004e29c[761] = Bttl_800d::scriptAllocateGuardEffect;
    scriptSubFunctions_8004e29c[762] = Bttl_800c::scriptAllocateWeaponTrailEffect;
    scriptSubFunctions_8004e29c[763] = Bttl_800d::scriptAllocateRadialGradientEffect;
    scriptSubFunctions_8004e29c[764] = Bttl_800d::scriptAllocateAdditionScript;
    scriptSubFunctions_8004e29c[765] = Bttl_800c::scriptGetBobjLocalWorldMatrixTranslation;
    scriptSubFunctions_8004e29c[766] = Bttl_800d::scriptAllocateSpTextEffect;
    scriptSubFunctions_8004e29c[767] = Bttl_800d::scriptAllocateAdditionNameEffect;

    scriptSubFunctions_8004e29c[800] = SEffe::scriptAllocateLensFlareEffect;
    scriptSubFunctions_8004e29c[801] = SEffe::scriptAllocateWsDragoonTransformationFeathersEffect;
    scriptSubFunctions_8004e29c[802] = SEffe::scriptAllocateGoldDragoonTransformEffect;
    scriptSubFunctions_8004e29c[803] = SEffe::scriptAllocateStarChildrenMeteorEffect;
    scriptSubFunctions_8004e29c[804] = SEffe::scriptAllocateStarChildrenImpactEffect;
    scriptSubFunctions_8004e29c[805] = SEffe::scriptAllocateMoonlightStarsEffect;
    scriptSubFunctions_8004e29c[806] = Scus94491BpeSegment::FUN_8001c5fc;
    scriptSubFunctions_8004e29c[807] = Scus94491BpeSegment::FUN_8001c604;

    scriptSubFunctions_8004e29c[832] = Bttl_800d::scriptRenderButtonPressHudElement;
    scriptSubFunctions_8004e29c[833] = SEffe::FUN_80108df8;
    scriptSubFunctions_8004e29c[834] = SEffe::FUN_80102610;
    scriptSubFunctions_8004e29c[835] = Bttl_800c::scriptGetBentDimension;
    scriptSubFunctions_8004e29c[836] = SEffe::scriptAllocateRainEffect;
    scriptSubFunctions_8004e29c[837] = Bttl_800c::scriptApplyWeaponTrailScaling;
    scriptSubFunctions_8004e29c[838] = SEffe::scriptAllocateElectricityEffect;
    scriptSubFunctions_8004e29c[839] = SEffe::scriptGetBoltSegmentEnd;
    scriptSubFunctions_8004e29c[840] = SEffe::scriptAllocateDragoonAdditionScript;
    scriptSubFunctions_8004e29c[841] = SEffe::scriptAllocateThunderArrowEffect;
    // scriptSubFunctions_8004e29c[842] = Temp::FUN_800c6968;
    scriptSubFunctions_8004e29c[843] = SEffe::scriptAllocateScreenDistortionEffect;
    scriptSubFunctions_8004e29c[844] = SEffe::scriptGetDragoonAdditionHitsCompleted;
    scriptSubFunctions_8004e29c[845] = SEffe::FUN_801023f4;
    scriptSubFunctions_8004e29c[846] = Bttl_800c::FUN_800cfec8;
    scriptSubFunctions_8004e29c[847] = SEffe::scriptGetAliveParticles;
    scriptSubFunctions_8004e29c[848] = SEffe::scriptGetParticlePosition;
    scriptSubFunctions_8004e29c[849] = Bttl_800c::scriptSetMtSeed;
    scriptSubFunctions_8004e29c[850] = SEffe::scriptAllocateVertexDifferenceAnimation;
    scriptSubFunctions_8004e29c[851] = SEffe::scriptAllocateFrozenJetEffect;
    scriptSubFunctions_8004e29c[852] = Bttl_800d::scriptAllocateMonsterDeathEffect;
    scriptSubFunctions_8004e29c[853] = Bttl_800d::scriptGetBobjModelPartCount;
    scriptSubFunctions_8004e29c[854] = SEffe::scriptGetAdditionOverlayActiveStatus;

    scriptSubFunctions_8004e29c[864] = Scus94491BpeSegment_8002::scriptGiveChestContents;
    scriptSubFunctions_8004e29c[865] = Scus94491BpeSegment_8002::scriptTakeItem;
    scriptSubFunctions_8004e29c[866] = Scus94491BpeSegment_8002::scriptGiveGold;

    scriptSubFunctions_8004e29c[896] = SEffe::scriptAllocateGradientRaysEffect;
    scriptSubFunctions_8004e29c[897] = SEffe::scriptAllocateScreenCaptureEffect;
  }
  // 8004f29c end of jump table

  public static final ItemStats0c[] itemStats_8004f2ac = new ItemStats0c[64];
  public static final int[] additionOffsets_8004f5ac = {0, 8, -1, 14, 29, 8, 23, 19, -1, 0};
  public static final int[] additionCounts_8004f5c0 = {7, 5, 0, 4, 6, 5, 5, 3, 0, 0};
  /**
   * <ol start="0">
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link Bttl_800c#initBattle}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link Bttl_800c#loadStageAndControllerScripts}</li>
   *   <li>{@link Bttl_800c#uploadBattleStageToGpu}</li>
   *   <li>{@link Bttl_800c#initializeViewportAndCamera}</li>
   *   <li>{@link Scus94491BpeSegment#nextLoadingStage}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link Scus94491BpeSegment#nextLoadingStage}</li>
   *   <li>{@link Bttl_800c#battleInitiateAndPreload_800c772c}</li>
   *   <li>{@link Bttl_800c#allocateEnemyBattleEntities()}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link Bttl_800c#allocatePlayerBattleEntities()}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link Bttl_800c#loadEncounterAssets}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link Bttl_800c#loadHudAndAttackAnimations}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link Bttl_800c#FUN_800c79f0}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link Bttl_800c#loadSEffe}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link Bttl_800c#calculateInitialTurnValues}</li>
   *   <li>{@link Bttl_800c#battleTick}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link Bttl_800c#performPostBattleAction}</li>
   *   <li>{@link Bttl_800c#deallocateCombat}</li>
   *   <li>{@link Scus94491BpeSegment#waitForFilesToLoad}</li>
   *   <li>{@link Scus94491BpeSegment#nextLoadingStage}</li>
   *   <li>{@link Scus94491BpeSegment#renderPostCombatScreen}</li>
   *   <li>{@link Scus94491BpeSegment#FUN_800189b0}</li>
   * </ol>
   */
  public static final Runnable[] battleLoadingStage_8004f5d4 = {
    Scus94491BpeSegment::waitForFilesToLoad,
    Bttl_800c::initBattle,
    Scus94491BpeSegment::waitForFilesToLoad,
    Scus94491BpeSegment::waitForFilesToLoad,
    Bttl_800c::loadStageAndControllerScripts,
    Bttl_800c::uploadBattleStageToGpu,
    Bttl_800c::initializeViewportAndCamera,
    Scus94491BpeSegment::nextLoadingStage,
    Scus94491BpeSegment::waitForFilesToLoad,
    Scus94491BpeSegment::nextLoadingStage,
    Bttl_800c::battleInitiateAndPreload_800c772c,
    Scus94491BpeSegment::waitForFilesToLoad,
    Bttl_800c::allocateEnemyBattleEntities,
    Scus94491BpeSegment::waitForFilesToLoad,
    Bttl_800c::allocatePlayerBattleEntities,
    Scus94491BpeSegment::waitForFilesToLoad,
    Bttl_800c::loadEncounterAssets,
    Scus94491BpeSegment::waitForFilesToLoad,
    Bttl_800c::loadHudAndAttackAnimations,
    Scus94491BpeSegment::waitForFilesToLoad,
    Bttl_800c::FUN_800c79f0,
    Scus94491BpeSegment::waitForFilesToLoad,
    Bttl_800c::loadSEffe,
    Scus94491BpeSegment::waitForFilesToLoad,
    Bttl_800c::calculateInitialTurnValues,
    Bttl_800c::battleTick,
    Scus94491BpeSegment::waitForFilesToLoad,
    Bttl_800c::performPostBattleAction,
    Bttl_800c::deallocateCombat,
    Scus94491BpeSegment::waitForFilesToLoad,
    Scus94491BpeSegment::nextLoadingStage,
    Scus94491BpeSegment::renderPostCombatScreen,
    Scus94491BpeSegment::FUN_800189b0,
  };

  public static final ScriptFile doNothingScript_8004f650 = new ScriptFile("Do nothing", new int[] {0x4, 0x1});
  public static Struct10 _8004f658;

  public static int _8004f6e4 = -1;

  public static int battleStartDelayTicks_8004f6ec;

  public static final SubmapMusic08[] _8004fa98 = {
    new SubmapMusic08(8, 59, 83, 84),
    new SubmapMusic08(19, 59, 204, 214, 211),
    new SubmapMusic08(22, 59, 247),
    new SubmapMusic08(32, 59, 332),
    new SubmapMusic08(34, 59, 357),
    new SubmapMusic08(49, 59, 515, 525),
    new SubmapMusic08(8, 60, 78, 79, 80),
    new SubmapMusic08(19, 60, 210, 209),
    new SubmapMusic08(22, 60, 246),
    new SubmapMusic08(30, 60, 316, 317),
    new SubmapMusic08(32, 60, 335),
    new SubmapMusic08(34, 60, 356, 361),
    new SubmapMusic08(99, 99, 83, 84),
  };
  public static final SubmapMusic08[] _8004fb00 = {
    new SubmapMusic08(57, -1, 675, 676, 677),
    new SubmapMusic08(3, 70, 9, 10, 725),
    new SubmapMusic08(3, 72, 694, 13),
    new SubmapMusic08(3, 71, 695, 694, 742),
    new SubmapMusic08(2, 66, 5, 6, 7, 624, 625),
    new SubmapMusic08(4, 70, 14, 15),
    new SubmapMusic08(5, 70, 38, 39),
    new SubmapMusic08(5, 28, 697, 740, 741),
    new SubmapMusic08(6, 72, 53),
    new SubmapMusic08(10, 63, 95, 98, 99, 100, 101, 102, 103, 104, 105),
    new SubmapMusic08(10, -1, 96),
    new SubmapMusic08(10, -1, 657, 726),
    new SubmapMusic08(10, 70, 674),
    new SubmapMusic08(11, 70, 108),
    new SubmapMusic08(11, 68, 109),
    new SubmapMusic08(13, 68, 716),
    new SubmapMusic08(14, 72, 140, 647),
    new SubmapMusic08(14, -1, 149, 150, 151, 637, 638),
    new SubmapMusic08(14, 61, 152, 634, 636, 639),
    new SubmapMusic08(4, 67, 643, 27),
    new SubmapMusic08(10, 70, 96, 112, 113, 657),
    new SubmapMusic08(8, -1, 66),
    new SubmapMusic08(8, -1, 68),
    new SubmapMusic08(9, -1, 94),
    new SubmapMusic08(17, -1, 181),
    new SubmapMusic08(18, -1, 658, 659, 660),
    new SubmapMusic08(57, 115, 703, 704),
    new SubmapMusic08(5, -1, 41),
    new SubmapMusic08(3, -1, 11),
    new SubmapMusic08(3, 45, 696, 743),
    new SubmapMusic08(12, 76, 120),
    new SubmapMusic08(20, 81, 221, 223, 225, 226, 663, 665, 666, 667),
    new SubmapMusic08(20, 70, 236),
    new SubmapMusic08(20, -1, 236, 669),
    new SubmapMusic08(20, 64, 238, 698),
    new SubmapMusic08(20, 67, 702),
    new SubmapMusic08(22, -1, 240),
    new SubmapMusic08(23, 79, 255),
    new SubmapMusic08(20, -1, 227),
    new SubmapMusic08(46, 25, 496, 497, 498),
    new SubmapMusic08(44, -1, 458),
    new SubmapMusic08(49, 43, 512, 522),
    new SubmapMusic08(56, -1, 733),
    new SubmapMusic08(56, 75, 597, 598, 735, 736, 737),
    new SubmapMusic08(53, 81, 701),
    new SubmapMusic08(53, 70, 629, 700),
    new SubmapMusic08(53, 38, 629),
    new SubmapMusic08(34, 75, 348),
    new SubmapMusic08(35, 68, 370),
    new SubmapMusic08(35, -1, 372),
    new SubmapMusic08(35, 70, 373, 374),
    new SubmapMusic08(35, -1, 375),
    new SubmapMusic08(34, 70, 349, 350, 355, 356),
    new SubmapMusic08(34, 68, 389),
    new SubmapMusic08(32, -1, 338, 670),
    new SubmapMusic08(32, 114, 671),
    new SubmapMusic08(32, -1, 710),
    new SubmapMusic08(33, -1, 346),
    new SubmapMusic08(37, -1, 381, 382),
    new SubmapMusic08(36, 70, 692, 724),
    new SubmapMusic08(37, 70, 388),
    new SubmapMusic08(38, 70, 393),
    new SubmapMusic08(35, -1, 376),
    new SubmapMusic08(31, 71, 325, 324),
    new SubmapMusic08(24, 63, 266),
    new SubmapMusic08(14, -1, 647),
    new SubmapMusic08(31, -1, 325),
    new SubmapMusic08(34, 63, 354),
    new SubmapMusic08(35, -1, 371),
    new SubmapMusic08(54, -1, 711, 714),
    new SubmapMusic08(54, 68, 713),
    new SubmapMusic08(50, -1, 718, 719),
    new SubmapMusic08(43, -1, 446),
    new SubmapMusic08(43, 71, 447),
    new SubmapMusic08(43, 68, 447),
    new SubmapMusic08(24, -1, 266),
    new SubmapMusic08(15, 74, 161),
    new SubmapMusic08(3, 71, 696, 743),
    new SubmapMusic08(22, 78, 244, 248, 259),
    new SubmapMusic08(54, -1, 580),
    new SubmapMusic08(28, 74, 678),
    new SubmapMusic08(28, -1, 364),
    new SubmapMusic08(27, 76, 288),
    new SubmapMusic08(53, -1, 568),
    new SubmapMusic08(19, 78, 216),
    new SubmapMusic08(19, 81, 201, 202, 744),
    new SubmapMusic08(20, -1, 664, 702),
    new SubmapMusic08(30, -1, 312, 313, 314, 318),
    new SubmapMusic08(28, -1, 297, 298, 299, 300, 364, 365, 366, 630, 678),
    new SubmapMusic08(29, -1, 301, 302, 303, 304, 305, 308),
    new SubmapMusic08(14, -1, 647, 646),
    new SubmapMusic08(26, -1, 286),
    new SubmapMusic08(58, -1, 672, 673),
    new SubmapMusic08(40, -1, 423),
    new SubmapMusic08(40, -1, 419),
    new SubmapMusic08(40, 71, 424),
    new SubmapMusic08(36, 28, 692, 693, 723),
    new SubmapMusic08(43, 70, 446),
    new SubmapMusic08(43, 75, 445, 449, 451, 452),
    new SubmapMusic08(46, 74, 499, 500, 501, 502),
    new SubmapMusic08(4, 63, 24),
    new SubmapMusic08(4, -1, 27),
    new SubmapMusic08(4, 70, 27),
    new SubmapMusic08(4, 70, 15, 56),
    new SubmapMusic08(4, -1, 36),
    new SubmapMusic08(32, 63, 329),
    new SubmapMusic08(33, 81, 343),
    new SubmapMusic08(33, 68, 343),
    new SubmapMusic08(33, -1, 345),
    new SubmapMusic08(9, -1, 94),
    new SubmapMusic08(18, -1, 200),
    new SubmapMusic08(53, 70, 563, 564, 565, 566, 567, 568, 629, 595),
    new SubmapMusic08(51, 66, 715, 722),
    new SubmapMusic08(55, -1, 588),
    new SubmapMusic08(55, -1, 593),
    new SubmapMusic08(27, -1, 295),
    new SubmapMusic08(49, 39, 525),
    new SubmapMusic08(54, 63, 580),
    new SubmapMusic08(54, 75, 714),
    new SubmapMusic08(54, 81, 580, 581, 594),
    new SubmapMusic08(54, -1, 570, 571, 572, 573, 574, 576, 578, 579, 582, 711, 712),
    new SubmapMusic08(43, -1, 446),
    new SubmapMusic08(54, -1, 575, 577, 580),
    new SubmapMusic08(5, 20, 38, 39, 40, 41, 42, 43, 44),
    new SubmapMusic08(46, -1, 486, 488, 489, 491),
    new SubmapMusic08(56, -1, 607),
    new SubmapMusic08(56, -1, 561),
    new SubmapMusic08(27, -1, 322),
    new SubmapMusic08(3, -1, 743),
    new SubmapMusic08(99, 99, 675, 676, 677),
  };
  public static final MoonMusic08[] moonMusic_8004ff10 = {
    new MoonMusic08(561, 56),
    new MoonMusic08(596, 45),
    new MoonMusic08(597, 56),
    new MoonMusic08(598, -1),
    new MoonMusic08(599, 79),
    new MoonMusic08(600, 79),
    new MoonMusic08(601, 60),
    new MoonMusic08(602, 79),
    new MoonMusic08(603, 79),
    new MoonMusic08(604, 79),
    new MoonMusic08(605, 20),
    new MoonMusic08(606, 23),
    new MoonMusic08(607, 23),
    new MoonMusic08(608, 72),
    new MoonMusic08(609, 72),
    new MoonMusic08(610, 72),
    new MoonMusic08(611, 72),
    new MoonMusic08(612, 21),
    new MoonMusic08(613, 21),
    new MoonMusic08(614, 21),
    new MoonMusic08(615, 47),
    new MoonMusic08(616, 47),
    new MoonMusic08(617, 47),
    new MoonMusic08(618, 47),
    new MoonMusic08(619, 29),
    new MoonMusic08(620, 29),
    new MoonMusic08(621, 45),
    new MoonMusic08(622, 39),
    new MoonMusic08(699, 79),
    new MoonMusic08(727, 39),
    new MoonMusic08(728, 39),
    new MoonMusic08(729, 63),
    new MoonMusic08(730, -1),
    new MoonMusic08(731, -1),
    new MoonMusic08(732, 79),
    new MoonMusic08(733, 79),
    new MoonMusic08(734, 56),
    new MoonMusic08(735, 56),
    new MoonMusic08(736, 56),
    new MoonMusic08(737, 56),
    new MoonMusic08(738, 21),
    new MoonMusic08(739, 72),
    new MoonMusic08(-1, -1),
  };

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
  public static void stopMusicSequence(final SequenceData124 sequenceData, final int mode) {
    boolean resetAdsr = false;
    final PlayableSound0c playableSound = sequenceData.playableSound_020;
    final Sshd sshd = playableSound.sshdPtr_04;

    sshdPtr_800c4ac0 = sshd;

    if(sequenceData.musicLoaded_027) {
      // Retail NPE when transitioning from combat into post-combat scene (happens after Kongol I and Divine Dragon)
      if(sshd != null && sshd.hasSubfile(0)) {
        if(playableSound.used_00) {
          boolean stopNotes = false;

          if(mode == 0) {
            //LAB_8004d13c
            sequenceData.sssqReader_010.jump(0x110);
            sequenceData.musicPlaying_028 = false;
            sequenceData._018 = true;
            sequenceData._0e8 = false;
            sequenceData.repeatCounter_035 = 0;
            stopNotes = true;
          } else if(mode == 1) {
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
          } else if(mode == 2) {
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
          } else if(mode == 3) {
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
  public static void stopSoundsAndSequences(final boolean resetVoice) {
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
