package legend.game;

import legend.core.MathHelper;
import legend.core.memory.Method;
import legend.core.spu.Voice;
import legend.game.combat.Battle;
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
import legend.game.title.GameOver;
import legend.game.title.NewGame;
import legend.game.title.Ttle;
import legend.game.types.BattleReportOverlayList10;
import legend.game.types.OverlayStruct;
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

import static legend.core.GameEngine.AUDIO_THREAD;
import static legend.core.GameEngine.SEQUENCER;
import static legend.core.GameEngine.SPU;
import static legend.game.Scus94491BpeSegment_8005.reverbConfigs_80059f7c;
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
    gameStateOverlays_8004dbc0.put(EngineStateEnum.TITLE_02, new OverlayStruct(Ttle.class, Ttle::new));
    gameStateOverlays_8004dbc0.put(EngineStateEnum.TRANSITION_TO_NEW_GAME_03, new OverlayStruct(NewGame.class, NewGame::new));
    gameStateOverlays_8004dbc0.put(EngineStateEnum.CREDITS_04, new OverlayStruct(Credits.class, Credits::new));
    gameStateOverlays_8004dbc0.put(EngineStateEnum.SUBMAP_05, new OverlayStruct(SMap.class, SMap::new));
    gameStateOverlays_8004dbc0.put(EngineStateEnum.COMBAT_06, new OverlayStruct(Battle.class, Battle::new));
    gameStateOverlays_8004dbc0.put(EngineStateEnum.GAME_OVER_07, new OverlayStruct(GameOver.class, GameOver::new));
    gameStateOverlays_8004dbc0.put(EngineStateEnum.WORLD_MAP_08, new OverlayStruct(WMap.class, WMap::new));
    gameStateOverlays_8004dbc0.put(EngineStateEnum.FINAL_FMV_11, new OverlayStruct(FinalFmv.class, FinalFmv::new));
  }

  public static EngineState currentEngineState_8004dd04;

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
  /** The last savable state we were in, used for generating crash recovery saves */
  public static EngineStateEnum lastSavableEngineState;

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
    scriptSubFunctions_8004e29c[8] = Scus94491BpeSegment::scriptStartRumbleMode;
    scriptSubFunctions_8004e29c[9] = Scus94491BpeSegment::scriptSetFlag;
    scriptSubFunctions_8004e29c[10] = Scus94491BpeSegment::scriptReadFlag;
    scriptSubFunctions_8004e29c[11] = Scus94491BpeSegment::scriptStartRumble;

    scriptSubFunctions_8004e29c[16] = Scus94491BpeSegment::scriptSetRumbleDampener;
    scriptSubFunctions_8004e29c[17] = Scus94491BpeSegment::scriptResetRumbleDampener;

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

    scriptSubFunctions_8004e29c[240] = Scus94491BpeSegment::scriptStopSoundsAndSequences;
    scriptSubFunctions_8004e29c[241] = Scus94491BpeSegment::scriptStartCurrentMusicSequence;
    scriptSubFunctions_8004e29c[242] = Scus94491BpeSegment::scriptToggleMusicSequencePause;
    scriptSubFunctions_8004e29c[243] = Scus94491BpeSegment::scriptToggleMusicSequencePause2;
    scriptSubFunctions_8004e29c[244] = Scus94491BpeSegment::scriptStopCurrentMusicSequence;
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

    scriptSubFunctions_8004e29c[704] = Scus94491BpeSegment::scriptStartSequenceAndChangeVolumeOverTime;
    scriptSubFunctions_8004e29c[705] = Scus94491BpeSegment::scriptSssqFadeOut;
    scriptSubFunctions_8004e29c[706] = Scus94491BpeSegment::scriptChangeSequenceVolumeOverTime;
    scriptSubFunctions_8004e29c[707] = Scus94491BpeSegment::scriptGetSequenceFlags;
    scriptSubFunctions_8004e29c[708] = Scus94491BpeSegment::scriptGetSssqTempoScale;
    scriptSubFunctions_8004e29c[709] = Scus94491BpeSegment::scriptSetSssqTempoScale;
    scriptSubFunctions_8004e29c[710] = Scus94491BpeSegment::scriptGetLoadedSoundFiles;
    scriptSubFunctions_8004e29c[711] = Scus94491BpeSegment::scriptGetSequenceVolume;

    scriptSubFunctions_8004e29c[714] = Scus94491BpeSegment_8002::scriptStopAndUnloadSequences;
    scriptSubFunctions_8004e29c[715] = Scus94491BpeSegment::scriptLoadCharacterAttackSounds;
    scriptSubFunctions_8004e29c[716] = Scus94491BpeSegment_8002::scriptReplaceMonsterSounds;
    scriptSubFunctions_8004e29c[717] = Scus94491BpeSegment::scriptLoadCutsceneSounds;
    scriptSubFunctions_8004e29c[718] = Scus94491BpeSegment::scriptLoadFinalBattleSounds;

    scriptSubFunctions_8004e29c[864] = Scus94491BpeSegment_8002::scriptGiveChestContents;
    scriptSubFunctions_8004e29c[865] = Scus94491BpeSegment_8002::scriptTakeItem;
    scriptSubFunctions_8004e29c[866] = Scus94491BpeSegment_8002::scriptGiveGold;

    scriptSubFunctions_8004e29c[890] = Scus94491BpeSegment_8002::scriptReadRegistryEntryVar;

    scriptSubFunctions_8004e29c[900] = SItem::scriptGetMaxItemCount;
    scriptSubFunctions_8004e29c[901] = SItem::scriptGetMaxEquipmentCount;
    scriptSubFunctions_8004e29c[902] = SItem::scriptIsItemSlotUsed;
    scriptSubFunctions_8004e29c[903] = SItem::scriptIsEquipmentSlotUsed;
    scriptSubFunctions_8004e29c[904] = SItem::scriptGetItemSlot;
    scriptSubFunctions_8004e29c[905] = SItem::scriptGetEquipmentSlot;
    scriptSubFunctions_8004e29c[906] = SItem::scriptSetItemSlot;
    scriptSubFunctions_8004e29c[907] = SItem::scriptSetEquipmentSlot;
    scriptSubFunctions_8004e29c[908] = SItem::scriptGiveItem;
    scriptSubFunctions_8004e29c[909] = SItem::scriptGiveEquipment;
    scriptSubFunctions_8004e29c[910] = SItem::scriptTakeItem;
    scriptSubFunctions_8004e29c[911] = SItem::scriptTakeEquipment;
    scriptSubFunctions_8004e29c[912] = SItem::scriptGenerateAttackItem;
    scriptSubFunctions_8004e29c[913] = SItem::scriptGenerateRecoveryItem;
  }
  // 8004f29c end of jump table

  public static final int[] additionOffsets_8004f5ac = {0, 8, -1, 14, 29, 8, 23, 19, -1, 0};
  public static final int[] additionCounts_8004f5c0 = {7, 5, 0, 4, 6, 5, 5, 3, 0, 0};

  public static final ScriptFile doNothingScript_8004f650 = new ScriptFile("Do nothing", new byte[] {0x4, 0x0, 0x0, 0x0, 0x1, 0x0, 0x0, 0x0});
  public static BattleReportOverlayList10 battleReportOverlayLists_8004f658;

  public static int _8004f6e4 = -1;

  public static int battleStartDelayTicks_8004f6ec;

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
        if(!sequenceData.soundLoaded_029) {
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

    final Sshd sshd = sequenceData.playableSound_020.sshdPtr_04;
    sshdPtr_800c4ac0 = sshd;
    sssqChannelInfo_800C6680 = sshd.getSubfile(4, (data, offset) -> new Sssqish(data, offset, sshd.getSubfileSize(4))).entries_10[playingNote.sequenceChannel_04];

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

  @Method(0x8004b834L)
  public static void initSpu() {
    final SoundEnv44 soundEnv = soundEnv_800c6630;

    //LAB_8004b8ac
    soundEnv.playingSoundsUpperBound_03 = 20;
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

  @Method(0x8004c3f0L)
  public static int setMaxSounds(final int playingSoundsUpperBound) {
    if(playingSoundsUpperBound < 0 || playingSoundsUpperBound >= 24) {
      throw new IllegalArgumentException("Must be [0, 24)");
    }

    //LAB_8004c420
    int playingSounds = 0;
    for(int voiceIndex = 0; voiceIndex < SPU.voices.length; voiceIndex++) {
      if(playingNotes_800c3a40[voiceIndex].used_00) {
        playingSounds++;
      }

      //LAB_8004c44c
    }

    if(playingSounds > playingSoundsUpperBound) {
      //LAB_8004c484
      assert false;
      return -1;
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
      SPU.setReverb(reverbConfigs_80059f7c[type - 1].config_02);
      AUDIO_THREAD.setReverb(reverbConfigs_80059f7c[type - 1].config_02);
      return;
    }

    //LAB_8004c538
    SPU.clearKeyOn();
    SPU.setReverbVolume(0, 0);
    SPU.disableReverb();
    // TODO DisableReverb (might not be necessary, since we are not actually ticking??)
    AUDIO_THREAD.setReverbVolume(0, 0);
  }

  /**
   * Sets the reverb volume for left and right channels. The value ranges from 0 to 127.
   */
  @Method(0x8004c558L)
  public static void sssqSetReverbVolume(final int left, final int right) {
    if(soundEnv_800c6630.reverbType_34 != 0 && left < 0x80 && right < 0x80) {
      //LAB_8004c5d0
      SPU.setReverbVolume(left << 8, right << 8);
      AUDIO_THREAD.setReverbVolume(left, right);
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

    if(sequenceData != null) {
      throw new RuntimeException("Sequence should be null");
    }

    return AUDIO_THREAD.setSequenceVolume(volume);

  }

  @Method(0x8004cb0cL)
  public static long setSoundSequenceVolume(final PlayableSound0c playableSound, final int volume) {
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

      if(playingNote.used_00 && playingNote.playableSound_22 == playableSound) {
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
    AUDIO_THREAD.setMainVolume(0, 0);
    soundEnv.fadingIn_2a = true;
    soundEnv.fadeTime_2c = fadeTime;
    soundEnv.fadeInVol_2e = maxVol;

    //LAB_8004cd30
    //LAB_8004cd34

    AUDIO_THREAD.fadeIn(fadeTime, maxVol);
    return 0;
  }

  @Method(0x8004cd50L)
  public static long sssqFadeOut(final short fadeTime) {
    if(fadeTime < 256 && !soundEnv_800c6630.fadingIn_2a) {
      soundEnv_800c6630.fadingOut_2b = true;
      soundEnv_800c6630.fadeTime_2c = fadeTime;
      soundEnv_800c6630.fadeOutVolL_30 = SPU.getMainVolumeLeft() >>> 8;
      soundEnv_800c6630.fadeOutVolR_32 = SPU.getMainVolumeRight() >>> 8;

      // Retail bug: due to the way fade volume is lerped, fade out over 1
      // tick doesn't fade out at all. This was breaking music after Lenus 2.
      // See GH#1623
      if(fadeTime > 1) {
        AUDIO_THREAD.fadeOut(fadeTime);
      }

      return 0;
    }

    //LAB_8004cdb0
    return -1;
  }

  @Method(0x8004d034L)
  public static void stopMusicSequence() {
    AUDIO_THREAD.stopSequence();
  }

  @Method(0x8004d034L)
  public static void pauseMusicSequence() {
    if(AUDIO_THREAD.isMusicPlaying()) {
      AUDIO_THREAD.stopSequence();
    } else {
      AUDIO_THREAD.startSequence();
    }
  }

  @Method(0x8004d2fcL)
  public static int startSequenceAndChangeVolumeOverTime(final short transitionTime, final short newVolume) {
    if(transitionTime >= 0x100 || newVolume >= 0x80) {
      throw new IllegalArgumentException("Invalid transitionTime or newVolume");
    }

    final int volume = AUDIO_THREAD.changeSequenceVolumeOverTime(newVolume, transitionTime);

    if(!AUDIO_THREAD.isMusicPlaying()) {
      AUDIO_THREAD.setSequenceVolume(0);
      AUDIO_THREAD.startSequence();
    }

    return volume;
  }

  @Method(0x8004d41cL)
  public static int changeSequenceVolumeOverTime(final int transitionTime, final int newVolume) {
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

    return AUDIO_THREAD.changeSequenceVolumeOverTime(newVolume, transitionTime);
  }

  @Method(0x8004d52cL)
  public static int getSequenceFlags() {
    int flags = 0;

    if(AUDIO_THREAD.isMusicPlaying()) {
      flags |= 0x1;
    }

    // TODO I have no idea what this is.
    /*
    if(sequenceData._0e8) {
      flags |= 0x2;
    }
    */

    flags |= AUDIO_THREAD.getSequenceVolumeOverTimeFlags();

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

        if(playingNote.used_00 && playingNote.sequenceData_06 == sequenceData) {
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

        if(playingNote.used_00) {
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
