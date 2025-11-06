package legend.game;

import legend.core.DebugHelper;
import legend.core.MathHelper;
import legend.core.audio.sequencer.assets.BackgroundMusic;
import legend.core.memory.Method;
import legend.core.spu.Voice;
import legend.game.combat.bent.BattleEntity27c;
import legend.game.combat.encounters.Encounter;
import legend.game.modding.events.battle.BattleMusicEvent;
import legend.game.scripting.FlowControl;
import legend.game.scripting.RunningScript;
import legend.game.scripting.ScriptDescription;
import legend.game.scripting.ScriptParam;
import legend.game.scripting.ScriptState;
import legend.game.sound.Instrument;
import legend.game.sound.InstrumentLayer10;
import legend.game.sound.PatchList;
import legend.game.sound.PlayableSound0c;
import legend.game.sound.PlayingNote66;
import legend.game.sound.QueuedSound28;
import legend.game.sound.ReverbConfig;
import legend.game.sound.ReverbConfigAndLocation;
import legend.game.sound.SequenceData124;
import legend.game.sound.SoundEnv44;
import legend.game.sound.SoundFile;
import legend.game.sound.SoundFileIndices;
import legend.game.sound.SpuStruct08;
import legend.game.sound.Sshd;
import legend.game.sound.Sssq;
import legend.game.sound.SssqReader;
import legend.game.sound.Sssqish;
import legend.game.sound.VolumeRamp;
import legend.game.sound.WaveformList;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Loader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static legend.core.GameEngine.AUDIO_THREAD;
import static legend.core.GameEngine.EVENTS;
import static legend.core.GameEngine.SEQUENCER;
import static legend.core.GameEngine.SPU;
import static legend.game.EngineStates.currentEngineState_8004dd04;
import static legend.game.EngineStates.engineState_8004dd20;
import static legend.game.DrgnFiles.loadDir;
import static legend.game.DrgnFiles.loadDrgnDir;
import static legend.game.DrgnFiles.loadDrgnFileSync;
import static legend.game.DrgnFiles.loadDrgnFiles;
import static legend.game.Scus94491BpeSegment.getCharacterName;
import static legend.game.Scus94491BpeSegment_8006.battleState_8006e398;
import static legend.game.Scus94491BpeSegment_800b.encounter;
import static legend.game.Scus94491BpeSegment_800b.gameState_800babc8;
import static legend.game.Scus94491BpeSegment_800b.scriptStatePtrArr_800bc1c0;

public final class Audio {
  private Audio() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Audio.class);
  private static final Marker SEQUENCER_MARKER = MarkerManager.getMarker("SEQUENCER");

  public static final int[] monsterSoundFileIndices_800500e8 = {4, 5, 6, 7};
  public static final int[] characterSoundFileIndices_800500f8 = {1, 2, 3};

  public static final int[] charSlotSpuOffsets_80050190 = {0x44250, 0x4b780, 0x52cb0};

  public static final int[] combatSoundEffectsTypes_8005019c = {
    12, 13, 86, 12, 12, 12, 12, 12,
    12, 12, 12, 12, 12, 12, 12, 12,
    14, 15, 88, 90, 14, 14, 14, 14,
    14, 14, 14, 14, 14, 14, 14, 14,
  };
  public static final int[] combatMusicFileIndices_800501bc = {
    702, 707, 722, 702, 702, 702, 702, 702,
    702, 702, 702, 702, 702, 702, 702, 702,
    712, 717, 727, 732, 712, 712, 712, 712,
    712, 712, 712, 712, 712, 712, 712, 712,
  };

  private static final ReverbConfigAndLocation[] reverbConfigs_80059f7c = {
    new ReverbConfigAndLocation(0xfb28, new ReverbConfig(125, 91, 28032, 21688, -16688, 0, 0, -17792, 22528, 21248, 1238, 819, 1008, 551, 884, 495, 820, 437, 0, 0, 0, 0, 0, 0, 0, 0, 436, 310, 184, 92, -32768, -32768)),
    new ReverbConfigAndLocation(0xfc18, new ReverbConfig(51, 37, 28912, 20392, -17184, 17424, -16144, -25600, 21120, 20160, 996, 795, 932, 687, 882, 614, 796, 605, 604, 398, 559, 309, 466, 183, 399, 181, 180, 128, 76, 38, -32768, -32768)),
    new ReverbConfigAndLocation(0xf6f8, new ReverbConfig(177, 127, 28912, 20392, -17184, 17680, -16656, -19264, 21120, 20160, 2308, 1899, 2084, 1631, 1954, 1558, 1900, 1517, 1516, 1070, 1295, 773, 1122, 695, 1071, 613, 612, 434, 256, 128, -32768, -32768)),
    new ReverbConfigAndLocation(0xf204, new ReverbConfig(227, 169, 28512, 20392, -17184, 17680, -16656, -22912, 22144, 21184, 3579, 2904, 3337, 2620, 3033, 2419, 2905, 2266, 2265, 1513, 2028, 1200, 1775, 978, 1514, 797, 796, 568, 340, 170, -32768, -32768)),
    new ReverbConfigAndLocation(0xea44, new ReverbConfig(421, 313, 24576, 20480, 19456, -18432, -17408, -16384, 24576, 23552, 5562, 4539, 5314, 4285, 4540, 3521, 4544, 3523, 3520, 2497, 3012, 1985, 2560, 1741, 2498, 1473, 1472, 1050, 628, 314, -32768, -32768)),
    new ReverbConfigAndLocation(0xe128, new ReverbConfig(829, 561, 32256, 20480, -19456, -20480, 19456, -20480, 24576, 21504, 7894, 6705, 7444, 6203, 7106, 5810, 6706, 5615, 5614, 4181, 4916, 3885, 4598, 3165, 4182, 2785, 2784, 1954, 1124, 562, -32768, -32768)),
    new ReverbConfigAndLocation(0xe000, new ReverbConfig(1, 1, 32767, 32767, 0, 0, 0, -32512, 0, 0, 8191, 4095, 4101, 5, 0, 0, 4101, 5, 0, 0, 0, 0, 0, 0, 0, 0, 4100, 4098, 4, 2, -32768, -32768)),
    new ReverbConfigAndLocation(0xe000, new ReverbConfig(1, 1, 32767, 32767, 0, 0, 0, 0, 0, 0, 8191, 4095, 4101, 5, 0, 0, 4101, 5, 0, 0, 0, 0, 0, 0, 0, 0, 4100, 4098, 4, 2, -32768, -32768)),
    new ReverbConfigAndLocation(0xfc8c, new ReverbConfig(23, 19, 28912, 20392, -17184, 17680, -16656, -31488, 24448, 21696, 881, 687, 741, 479, 688, 471, 856, 618, 470, 286, 301, 177, 287, 89, 416, 227, 88, 64, 40, 20, -32768, -32768)),
  };

  /** short */
  public static int sssqFadeCurrent_8005a1ce;

  /** One per voice */
  public static final SpuStruct08[] _800bc9a8 = new SpuStruct08[24];
  static {
    Arrays.setAll(_800bc9a8, i -> new SpuStruct08());
  }

  public static final Queue<QueuedSound28> playingSoundsBackup_800bca78 = new LinkedList<>();

  /**
   * <ul>
   *   <li>0x1 - menu sounds</li>
   *   <li>0x2 - submap sounds</li>
   *   <li>0x4 - battle cutscene sounds</li>
   *   <li>0x8 - battle character sounds</li>
   *   <li>0x10 - battle phase sounds</li>
   *   <li>0x20 - battle monster sounds</li>
   *   <li>0x40 - battle DEFF sounds</li>
   *   <li>0x80 - music</li>
   *   <li>0x4000 - victory music</li>
   *   <li>0x8000 - world map destination sounds</li>
   *   <li>0x10000 - different battle character attack sounds?</li>
   * </ul>
   */
  public static final AtomicInteger loadedAudioFiles_800bcf78 = new AtomicInteger();

  public static final SoundFile[] soundFiles_800bcf80 = new SoundFile[13];
  static {
    Arrays.setAll(soundFiles_800bcf80, i -> new SoundFile());
  }

  public static int _800bd0f0;

  /** .8 */
  public static int sssqTempoScale_800bd100;

  public static final Queue<QueuedSound28> queuedSounds_800bd110 = new LinkedList<>();
  public static BackgroundMusic victoryMusic;

  public static boolean musicLoaded_800bd782;

  public static int _800bf0cf;

  /** 0x990 bytes long, one per voice */
  public static final PlayingNote66[] playingNotes_800c3a40 = new PlayingNote66[SPU.voices.length];
  static {
    Arrays.setAll(playingNotes_800c3a40, i -> new PlayingNote66());
  }
  /** 0x5f4 bytes long */
  public static final Queue<PlayableSound0c> playableSounds_800c43d0 = new LinkedList<>();

  public static Sssqish sssqish_800c4aa8;
  public static VolumeRamp volumeRamp_800c4ab0;
  public static WaveformList waveforms_800c4ab8;
  public static Sshd sshdPtr_800c4ac0;
  /** One per loaded sequence */
  public static final SequenceData124[] sequenceData_800c4ac8 = new SequenceData124[24];
  static {
    Arrays.setAll(sequenceData_800c4ac8, i -> new SequenceData124());
  }

  public static final SoundEnv44 soundEnv_800c6630 = new SoundEnv44();
  public static Instrument instrument_800c6674;
  public static InstrumentLayer10[] instrumentLayers_800c6678;
  public static InstrumentLayer10 instrumentLayer_800c6678;
  public static int instrumentLayerIndex_800c6678;
  public static SssqReader sssqReader_800c667c;
  public static Sssq.ChannelInfo sssqChannelInfo_800C6680;

  @Method(0x80019500L)
  public static void initSound() {
    initSpu();
    sssqFadeIn(0, 0);
    setMaxSounds(8);
    sssqSetReverbType(3);
    sssqSetReverbVolume(0x30, 0x30);

    for(int i = 0; i < 13; i++) {
      unuseSoundFile(i);
    }

    FUN_8001aa64();
    FUN_8001aa90();

    queuedSounds_800bd110.clear();

    for(int i = 0; i < 13; i++) {
      soundFiles_800bcf80[i].used_00 = false;
    }

    sssqTempoScale_800bd100 = 0x100;
  }

  /**
   * @param soundIndex 1: up/down, 2: choose menu option, 3: ...
   */
  @Method(0x80019a60L)
  public static void playSound(final int soundFileIndex, final int soundIndex, final int initialDelay, final int repeatDelay) {
    final SoundFile soundFile = soundFiles_800bcf80[soundFileIndex];

    if(!soundFile.used_00 || soundFile.playableSound_10 == null) {
      return;
    }

    switch(soundFileIndex) {
      case 0 -> {
        if((loadedAudioFiles_800bcf78.get() & 0x1) != 0) {
          return;
        }
      }

      case 8 -> {
        //LAB_80019bd4
        if((loadedAudioFiles_800bcf78.get() & 0x2) != 0 || engineState_8004dd20 != EngineStateEnum.SUBMAP_05) {
          return;
        }
      }

      case 9 -> {
        //LAB_80019bd4
        if((loadedAudioFiles_800bcf78.get() & 0x4) != 0 || engineState_8004dd20 != EngineStateEnum.COMBAT_06) {
          return;
        }
      }

      case 0xa -> {
        if((loadedAudioFiles_800bcf78.get() & 0x20) != 0) {
          return;
        }
      }

      case 0xc -> {
        //LAB_80019bd4
        if((loadedAudioFiles_800bcf78.get() & 0x8000) != 0 || engineState_8004dd20 != EngineStateEnum.WORLD_MAP_08) {
          return;
        }
      }
    }

    //LAB_80019be0
    //LAB_80019c00
    //LAB_80019b54
    final QueuedSound28 queuedSound = new QueuedSound28();
    queuedSounds_800bd110.add(queuedSound);
    playSound(3, soundFile, soundIndex, queuedSound, soundFile.playableSound_10, soundFile.indices_08[soundIndex], soundFileIndex == 8 ? soundFile.ptr_0c.readUByte(soundIndex) : 0, (short)-1, (short)-1, (short)-1, repeatDelay, initialDelay, null);

    //LAB_80019c70
  }

  @Method(0x80019c80L)
  public static void stopSound(final SoundFile soundFile, final int soundIndex, final int mode) {
    //LAB_80019cc4
    for(int sequenceIndex = 0; sequenceIndex < 24; sequenceIndex++) {
      final SpuStruct08 s0 = _800bc9a8[sequenceIndex];

      if(s0.soundFile_02 == soundFile && s0.soundIndex_03 == soundIndex) {
        stopSoundSequence(sequenceData_800c4ac8[sequenceIndex], true);

        if((mode & 0x1) == 0) {
          break;
        }
      }
    }

    //LAB_80019d0c
    //LAB_80019d1c
    queuedSounds_800bd110.removeIf(queuedSound -> queuedSound.type_00 == 4 && queuedSound.soundIndex_0c == soundIndex && queuedSound.soundFile_08 == soundFile);

    if((mode & 0x2) != 0) {
      //LAB_80019d84
      queuedSounds_800bd110.removeIf(queuedSound -> (queuedSound.type_00 == 3 && (queuedSound.repeatDelayTotal_20 != 0 || queuedSound.initialDelay_24 != 0) || queuedSound._1c != 0) && queuedSound.soundIndex_0c == soundIndex && queuedSound.soundFile_08 == soundFile);
    }

    //LAB_80019dfc
  }

  @Method(0x8001a4e8L)
  public static void startQueuedSounds() {
    //LAB_8001a50c
    final Iterator<QueuedSound28> it = queuedSounds_800bd110.iterator();

    while(it.hasNext()) {
      final QueuedSound28 queuedSound = it.next();

      if(queuedSound.type_00 != 0 && queuedSound.type_00 != 4) {
        if(queuedSound.initialDelay_24 != 0) {
          queuedSound.initialDelay_24--;

          if(queuedSound.initialDelay_24 <= 0) {
            queuedSound.initialDelay_24 = 0;

            startQueuedSound(queuedSound);

            if(queuedSound.repeatDelayTotal_20 == 0) {
              it.remove();
            }
          }
          //LAB_8001a564
        } else if(queuedSound.repeatDelayTotal_20 != 0) {
          queuedSound.repeatDelayCurrent_22--;

          if(queuedSound.repeatDelayCurrent_22 <= 0) {
            startQueuedSound(queuedSound);

            if(queuedSound.repeatDelayTotal_20 != 0) {
              queuedSound.repeatDelayCurrent_22 = queuedSound.repeatDelayTotal_20;
            }
          }
        } else {
          //LAB_8001a5b0
          startQueuedSound(queuedSound);

          if(queuedSound._1c != 0) {
            queuedSound.type_00 = 4;
          } else {
            //LAB_8001a5d0
            it.remove();
          }
        }
      }

      //LAB_8001a5d4
    }
  }

  /** Actually starts playing the sound */
  @Method(0x8001a5fcL)
  public static void startQueuedSound(final QueuedSound28 playingSound) {
    final int sequenceIndex;

    if(playingSound.pitchShiftVolRight_16 == -1 && playingSound.pitchShiftVolLeft_18 == -1 && playingSound.pitch_1a == -1) {
      sequenceIndex = startRegularSound(
        playingSound.playableSound_10,
        playingSound.patchIndex_12,
        playingSound.sequenceIndex_14
      );
    } else {
      sequenceIndex = startPitchShiftedSound(
        playingSound.playableSound_10,
        playingSound.patchIndex_12,
        playingSound.sequenceIndex_14,
        playingSound.pitchShiftVolLeft_18,
        playingSound.pitchShiftVolRight_16,
        playingSound.pitch_1a
      );
    }

    if(sequenceIndex != -1) {
      final SpuStruct08 struct08 = _800bc9a8[sequenceIndex];
      struct08.soundFile_02 = playingSound.soundFile_08;
      struct08.soundIndex_03 = playingSound.soundIndex_0c;
      struct08.bent_04 = playingSound.bent_04;
    }

    //LAB_8001a704
  }

  @Method(0x8001a714L)
  public static void playSound(final int type, final SoundFile soundFile, final int soundIndex, final QueuedSound28 playingSound, final PlayableSound0c playableSound, final SoundFileIndices soundFileIndices, final int a6, final short pitchShiftVolRight, final short pitchShiftVolLeft, final short pitch, final int repeatDelay, final int initialDelay, @Nullable final BattleEntity27c bent) {
    playingSound.type_00 = type;
    playingSound.bent_04 = bent;
    playingSound.soundFile_08 = soundFile;
    playingSound.soundIndex_0c = soundIndex;
    playingSound.playableSound_10 = playableSound;
    playingSound.patchIndex_12 = soundFileIndices.patchIndex_00;
    playingSound.sequenceIndex_14 = soundFileIndices.sequenceIndex_01;
    playingSound.pitchShiftVolRight_16 = pitchShiftVolRight;
    playingSound.pitchShiftVolLeft_18 = pitchShiftVolLeft;
    playingSound.pitch_1a = pitch;
    playingSound._1c = a6;
    playingSound.repeatDelayTotal_20 = repeatDelay;
    playingSound.repeatDelayCurrent_22 = 1;
    playingSound.initialDelay_24 = initialDelay;
  }

  @Method(0x8001aa44L)
  public static void unuseSoundFile(final int index) {
    soundFiles_800bcf80[index].used_00 = false;
  }

  @Method(0x8001aa64L)
  public static void FUN_8001aa64() {
    _800bd0f0 = 0;
  }

  @Method(0x8001aa90L)
  public static void FUN_8001aa90() {
    //LAB_8001aaa4
    for(int i = 0; i < 24; i++) {
      _800bc9a8[i]._00 = 0xffff;
    }
  }

  @ScriptDescription("Play a sound")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "soundFileIndex", description = "The sound file index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "soundIndex", description = "The sound index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "unused1")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "unused2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "initialDelay", description = "The initial delay before the sound starts")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "repeatDelay", description = "The delay before a sound repeats")
  @Method(0x8001ab34L)
  public static FlowControl scriptPlaySound(final RunningScript<?> script) {
    playSound(script.params_20[0].get(), script.params_20[1].get(), script.params_20[4].get() * currentEngineState_8004dd04.tickMultiplier(), script.params_20[5].get() * currentEngineState_8004dd04.tickMultiplier());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Stop a sound")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "soundFileIndex", description = "The sound file index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "soundIndex", description = "The sound index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "mode")
  @Method(0x8001ab98L)
  public static FlowControl scriptStopSound(final RunningScript<?> script) {
    stopSound(soundFiles_800bcf80[script.params_20[0].get()], script.params_20[1].get(), script.params_20[2].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x8001ad18L)
  public static void stopAndResetSoundsAndSequences() {
    //LAB_8001ad2c
    queuedSounds_800bd110.clear();
    stopSoundsAndSequences(true);
  }

  @ScriptDescription("Stops all sounds and sequences")
  @Method(0x8001ad5cL)
  public static FlowControl scriptStopSoundsAndSequences(final RunningScript<?> script) {
    //LAB_8001ad70
    stopAndResetSoundsAndSequences();
    return FlowControl.CONTINUE;
  }

  @Method(0x8001ada0L)
  public static void startCurrentMusicSequence() {
    AUDIO_THREAD.startSequence();
  }

  @ScriptDescription("Starts the current music sequence")
  @Method(0x8001adc8L)
  public static FlowControl scriptStartCurrentMusicSequence(final RunningScript<?> script) {
    startCurrentMusicSequence();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Toggles whether the current music sequence is paused")
  @Method(0x8001ae18L)
  public static FlowControl scriptToggleMusicSequencePause(final RunningScript<?> script) {
    pauseMusicSequence();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Toggles whether the current music sequence is paused")
  @Method(0x8001ae68L)
  public static FlowControl scriptToggleMusicSequencePause2(final RunningScript<?> script) {
    return scriptToggleMusicSequencePause(script);
  }

  @Method(0x8001ae90L)
  public static void stopCurrentMusicSequence() {
    if(_800bd0f0 == 2) {
      stopMusicSequence();
    }
  }

  @ScriptDescription("Stops the current music sequence if 800bd0f0 is 2")
  @Method(0x8001aec8L)
  public static FlowControl scriptStopCurrentMusicSequence(final RunningScript<?> script) {
    stopCurrentMusicSequence();
    return FlowControl.CONTINUE;
  }

  @Method(0x8001af00L)
  public static void startEncounterSounds() {
    AUDIO_THREAD.loadBackgroundMusic(victoryMusic);
    AUDIO_THREAD.startSequence();
  }

  @ScriptDescription("Starts the encounter sounds sequence")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x8001af34L)
  public static FlowControl scriptStartEncounterSounds(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @ScriptDescription("Stops the encounter sounds sequence")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x8001afa4L)
  public static FlowControl scriptStopEncounterSounds(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @ScriptDescription("Stops the encounter sounds sequence")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x8001b014L)
  public static FlowControl scriptStopEncounterSounds2(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @ScriptDescription("Stops the encounter sounds sequence if 800bd0f0 is 2")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @Method(0x8001b094L)
  public static FlowControl FUN_8001b094(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @ScriptDescription("Gets the current sequence's tempo scale")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "tempoScale", description = "The tempo scale")
  @Method(0x8001b0f0L)
  public static FlowControl scriptGetSssqTempoScale(final RunningScript<?> script) {
    script.params_20[0].set(sssqTempoScale_800bd100);
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the current sequence's tempo scale")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "tempoScale", description = "The tempo scale")
  @Method(0x8001b118L)
  public static FlowControl scriptSetSssqTempoScale(final RunningScript<?> script) {
    sssqTempoScale_800bd100 = script.params_20[0].get();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("No-op")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p0")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @Method(0x8001b134L)
  public static FlowControl FUN_8001b134(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("No-op")
  @Method(0x8001b13cL)
  public static FlowControl FUN_8001b13c(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("No-op")
  @Method(0x8001b144L)
  public static FlowControl FUN_8001b144(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the main volume")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "left", description = "The left volume")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "right", description = "The right volume")
  @Method(0x8001b14cL)
  public static FlowControl scriptSetMainVolume(final RunningScript<?> script) {
    soundEnv_800c6630.fadingIn_2a = false;
    soundEnv_800c6630.fadingOut_2b = false;

    setMainVolume((short)script.params_20[0].get(), (short)script.params_20[1].get());
    AUDIO_THREAD.setMainVolume((short)script.params_20[0].get(), (short)script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the sequence volume")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "volume", description = "The volume")
  @Method(0x8001b17cL)
  public static FlowControl scriptSetSequenceVolume(final RunningScript<?> script) {
    setSequenceVolume((short)script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x8001b1a8L)
  public static void setSequenceVolume(final int volume) {
    setSequenceVolume(null, volume);
  }

  @ScriptDescription("Gets the sequence volume")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "volume", description = "The volume")
  @Method(0x8001b1ecL)
  public static FlowControl scriptGetSequenceVolume(final RunningScript<?> script) {
    script.params_20[0].set(AUDIO_THREAD.getSequenceVolume());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Sets the volume for all sequences")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "volume", description = "The volume")
  @Method(0x8001b208L)
  public static FlowControl scriptSetAllSoundSequenceVolumes(final RunningScript<?> script) {
    //LAB_8001b22c
    for(int i = 0; i < 13; i++) {
      final SoundFile soundFile = soundFiles_800bcf80[i];

      // hasSubfile check added because music loads faster than retail. In Kongol I cutscene when Rose wakes up
      // Dart's dragoon spirit, the current music is unloaded and a new one is loaded. This method got called by
      // the script after the "load new music" method was called, but it was quick enough that it happened after
      // the current music was unloaded, but before the new music finished loading.
      if(soundFile.used_00 && soundFile.playableSound_10.sshdPtr_04.hasSubfile(4)) {
        setSoundSequenceVolume(soundFile.playableSound_10, (short)script.params_20[0].get());
      }

      //LAB_8001b250
    }

    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Fade in all sequenced audio")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "fadeTime", description = "The number of ticks to fade in")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "volume", description = "The volume once the fade in has finished")
  @Method(0x8001b27cL)
  public static FlowControl scriptSssqFadeIn(final RunningScript<?> script) {
    sssqFadeIn((short)script.params_20[0].get(), (short)script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Start the current sequence and fade it in")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "fadeTime", description = "The number of ticks to fade in")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "volume", description = "The volume once the fade in has finished")
  @Method(0x8001b2acL)
  public static FlowControl scriptStartSequenceAndChangeVolumeOverTime(final RunningScript<?> script) {
    startSequenceAndChangeVolumeOverTime((short)script.params_20[0].get(), (short)script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Fade out all sequenced audio")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "fadeTime", description = "The number of ticks to fade out")
  @Method(0x8001b310L)
  public static FlowControl scriptSssqFadeOut(final RunningScript<?> script) {
    sssqFadeOut((short)script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Adjust the current sequence's volume over time")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "fadeTime", description = "The number of ticks to fade for")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "volume", description = "The volume once the fade in has finished")
  @Method(0x8001b33cL)
  public static FlowControl scriptChangeSequenceVolumeOverTime(final RunningScript<?> script) {
    changeSequenceVolumeOverTime((short)script.params_20[0].get(), (short)script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Get the current sequence's flags")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "flags", description = "The current sequence's flags")
  @Method(0x8001b3a0L)
  public static FlowControl scriptGetSequenceFlags(final RunningScript<?> script) {
    script.params_20[0].set(getSequenceFlags());
    return FlowControl.CONTINUE;
  }

  @Method(0x8001cae0L)
  public static void charSoundEffectsLoaded(final List<FileData> files, final int charSlot) {
    final int charId = gameState_800babc8.charIds_88[charSlot];

    //LAB_8001cb34
    final int index = characterSoundFileIndices_800500f8[charSlot];
    final SoundFile sound = soundFiles_800bcf80[index];

    sound.name = "Char slot %d sound effects".formatted(charSlot);
    sound.id_02 = charId;
    sound.indices_08 = SoundFileIndices.load(files.get(1));

    //LAB_8001cc48
    //LAB_8001cc50
    sound.playableSound_10 = loadSshdAndSoundbank(sound.name, files.get(3), new Sshd(sound.name, files.get(2)), charSlotSpuOffsets_80050190[charSlot]);
    sound.used_00 = true;
  }

  @Method(0x8001cce8L)
  public static void loadCharAttackSounds(final int bentIndex, final int type) {
    final BattleEntity27c bent = (BattleEntity27c)scriptStatePtrArr_800bc1c0[bentIndex].innerStruct_00;

    //LAB_8001cd3c
    int charSlot;
    for(charSlot = 0; charSlot < 3; charSlot++) {
      final SoundFile soundFile = soundFiles_800bcf80[characterSoundFileIndices_800500f8[charSlot]];

      if(soundFile.id_02 == bent.charId_272) {
        break;
      }
    }

    //LAB_8001cd78
    final SoundFile soundFile = soundFiles_800bcf80[characterSoundFileIndices_800500f8[charSlot]];
    sssqUnloadPlayableSound(soundFile.playableSound_10);
    soundFile.used_00 = false;

    loadedAudioFiles_800bcf78.updateAndGet(val -> val | 0x8);

    final int fileIndex;
    final String soundName;
    if(type != 0) {
      //LAB_8001ce44
      fileIndex = 1298 + bent.charId_272;
      soundName = "Char slot %d attack sounds".formatted(bent.charId_272);
    } else if(bent.charId_272 != 0 || (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 == 0) {
      //LAB_8001ce18
      fileIndex = 1307 + bent.charId_272;
      soundName = "Char slot %d dragoon attack sounds".formatted(bent.charId_272);
    } else {
      fileIndex = 1307;
      soundName = "Divine dragoon attack sounds";
    }

    //LAB_8001ce70
    final int finalCharSlot = charSlot;
    loadDrgnDir(0, fileIndex, files -> charAttackSoundsLoaded(files, soundName, finalCharSlot));
  }

  @Method(0x8001ce98L)
  public static void charAttackSoundsLoaded(final List<FileData> files, final String soundName, final int charSlot) {
    final SoundFile sound = soundFiles_800bcf80[characterSoundFileIndices_800500f8[charSlot]];

    //LAB_8001cee8
    //LAB_8001cf2c
    sound.name = soundName;
    sound.indices_08 = SoundFileIndices.load(files.get(1));
    sound.id_02 = files.get(0).readShort(0);
    sound.playableSound_10 = loadSshdAndSoundbank(sound.name, files.get(3), new Sshd(sound.name, files.get(2)), charSlotSpuOffsets_80050190[charSlot]);
    cleanUpCharAttackSounds();
    setSoundSequenceVolume(sound.playableSound_10, 0x7f);
    sound.used_00 = true;
  }

  /**
   * <ol start="0">
   *   <li>Dragoon transformation sounds</li>
   *   <li>Encounter sound effects</li>
   *   <li>Maybe item magic?</li>
   * </ol>
   */
  @Method(0x8001d068L)
  public static void loadDeffSounds(final ScriptState<BattleEntity27c> bentState, final int type) {
    final BattleEntity27c bent = bentState.innerStruct_00;

    unloadSoundFile(3);
    unloadSoundFile(6);

    if(type == 0) {
      //LAB_8001d0e0
      loadedAudioFiles_800bcf78.updateAndGet(val -> val | 0x40);
      if(bent.charId_272 != 0 || (gameState_800babc8.goods_19c[0] & 0xff) >>> 7 == 0) {
        //LAB_8001d134
        // Regular dragoons
        loadDrgnDir(0, 1317 + bent.charId_272, files -> FUN_8001e98c(files, "%s dragoon transformation sounds (file %d)".formatted(getCharacterName(bent.charId_272), 1317 + bent.charId_272)));
      } else {
        // Divine dragoon
        loadDrgnDir(0, 1328, files -> FUN_8001e98c(files, "Divine dragoon transformation sounds (file 1328)"));
      }
    } else if(type == 1) {
      //LAB_8001d164
      loadMonsterSoundsWithPhases();
    } else if(type == 2) {
      //LAB_8001d174
      loadedAudioFiles_800bcf78.updateAndGet(val -> val | 0x40);

      //LAB_8001d1a8
      loadDrgnDir(0, 1327, files -> FUN_8001e98c(files, "Item sounds? (file 1327)"));
    }

    //LAB_8001d1b0
  }

  @Method(0x8001d1c4L)
  public static void loadMonsterSounds() {
    encounter.loadSounds(0);
  }

  @Method(0x8001d2d8L)
  public static void loadMonsterSoundsWithPhases() {
    encounter.loadSounds(battleState_8006e398.battlePhase_eec);
  }

  public static void loadBattlePhaseSounds(final String boss, final int phase) {
    loadedAudioFiles_800bcf78.updateAndGet(val -> val | 0x10);
    final AtomicInteger soundbankOffset = new AtomicInteger();
    final AtomicInteger count = new AtomicInteger(0);

    for(int monsterSlot = 0; monsterSlot < 4; monsterSlot++) {
      if(Loader.exists("monsters/phases/%s/%d/%d".formatted(boss, phase, monsterSlot))) {
        count.incrementAndGet();
      }
    }

    for(int monsterSlot = 0; monsterSlot < 4; monsterSlot++) {
      final SoundFile file = soundFiles_800bcf80[monsterSoundFileIndices_800500e8[monsterSlot]];
      file.id_02 = -1;
      file.used_00 = false;

      if(Loader.exists("monsters/phases/%s/%d/%d".formatted(boss, phase, monsterSlot))) {
        final int finalMonsterSlot = monsterSlot;
        loadDir("monsters/phases/%s/%d/%d".formatted(boss, phase, monsterSlot), files -> {
          final int offset = soundbankOffset.getAndUpdate(val -> val + MathHelper.roundUp(files.get(3).size(), 0x10));
          monsterSoundLoaded(files, "Monster slot %d (file %s/%d)".formatted(finalMonsterSlot, boss, phase), finalMonsterSlot, offset);

          if(count.decrementAndGet() == 0) {
            loadedAudioFiles_800bcf78.updateAndGet(val -> val & ~0x10);
          }
        });
      }
    }
  }

  public static void loadEncounterSounds(final Encounter encounter) {
    loadEncounterSounds(encounter.uniqueIds.toIntArray());
  }

  public static void loadEncounterSounds(final int[] monsterIds) {
    loadedAudioFiles_800bcf78.updateAndGet(val -> val | 0x10);

    final AtomicInteger soundbankOffset = new AtomicInteger();
    final AtomicInteger count = new AtomicInteger(0);

    for(int monsterSlot = 0; monsterSlot < monsterIds.length; monsterSlot++) {
      if(Loader.exists("monsters/" + monsterIds[monsterSlot] + "/sounds")) {
        count.incrementAndGet();
      }
    }

    for(int monsterSlot = 0; monsterSlot < monsterIds.length; monsterSlot++) {
      final SoundFile soundFile = soundFiles_800bcf80[monsterSoundFileIndices_800500e8[monsterSlot]];
      soundFile.id_02 = -1;
      soundFile.used_00 = false;

      if(Loader.exists("monsters/" + monsterIds[monsterSlot] + "/sounds")) {
        final int finalMonsterSlot = monsterSlot;
        loadDir("monsters/" + monsterIds[monsterSlot] + "/sounds", files -> {
          final int offset = soundbankOffset.getAndUpdate(val -> val + MathHelper.roundUp(files.get(3).size(), 0x10));
          monsterSoundLoaded(files, "Monster slot %d (file %d)".formatted(finalMonsterSlot, monsterIds[finalMonsterSlot]), finalMonsterSlot, offset);

          if(count.decrementAndGet() == 0) {
            loadedAudioFiles_800bcf78.updateAndGet(val -> val & ~0x10);
          }
        });
      }
    }
  }

  @Method(0x8001d51cL)
  public static void monsterSoundLoaded(final List<FileData> files, final String soundName, final int monsterSlot, final int soundBufferOffset) {
    //LAB_8001d698
    final int file3Size = files.get(3).size();

    if(file3Size > 48) {
      //LAB_8001d704
      //LAB_8001d718
      int v1 = 51024;
      for(int n = 3; n >= 0 && file3Size < v1; n--) {
        v1 = v1 - 17008;
      }

      //LAB_8001d72c
      final int soundFileIndex = monsterSoundFileIndices_800500e8[monsterSlot];
      final SoundFile soundFile = soundFiles_800bcf80[soundFileIndex];

      soundFile.name = soundName;
      soundFile.indices_08 = SoundFileIndices.load(files.get(1));
      soundFile.id_02 = files.get(0).readShort(0);

      //LAB_8001d80c
      soundFile.playableSound_10 = loadSshdAndSoundbank(soundFile.name, files.get(3), new Sshd(soundFile.name, files.get(2)), 0x5_a1e0 + soundBufferOffset);

      setSoundSequenceVolume(soundFile.playableSound_10, 0x7f);
      soundFile.used_00 = true;
    }

    //LAB_8001d8ac
  }

  @Method(0x8001d8d8L)
  public static void battleCutsceneSoundsLoaded(final List<FileData> files, final String soundName) {
    final SoundFile soundFile = soundFiles_800bcf80[9];
    soundFile.name = soundName;
    soundFile.used_00 = true;
    soundFile.id_02 = files.get(0).readUShort(0);
    soundFile.indices_08 = SoundFileIndices.load(files.get(2));
    soundFile.spuRamOffset_14 = files.get(4).size();
    soundFile.numberOfExtraSoundbanks_18 = files.get(1).readUShort(0) - 1;

    soundFile.playableSound_10 = loadSshdAndSoundbank(soundFile.name, files.get(4), new Sshd(soundFile.name, files.get(3)), 0x4_de90);
    loadExtraBattleCutsceneSoundbanks();
    setSoundSequenceVolume(soundFile.playableSound_10, 0x7f);
  }

  @Method(0x8001d9d0L)
  public static void loadEncounterMusic(final int musicIndex, final int victoryType) {
    if(victoryType == -1) {
      return;
    }

    loadedAudioFiles_800bcf78.updateAndGet(val -> val | 0x80);

    final int fileId;
    final Consumer<List<FileData>> callback;
    if(victoryType == 732) {
      callback = files -> musicPackageLoadedCallback(files, victoryType, true);
      fileId = victoryType;
    } else {
      callback = files -> FUN_8001fb44(files, musicIndex, victoryType);
      fileId = musicIndex;
    }

    loadDrgnDir(0, fileId, callback);
  }

  @Method(0x8001dabcL)
  public static void musicPackageLoadedCallback(final List<FileData> files, final int fileIndex, final boolean startSequence) {
    LOGGER.info("Music package %d loaded", fileIndex);

    playMusicPackage(new BackgroundMusic(files, fileIndex), startSequence);
    loadedAudioFiles_800bcf78.updateAndGet(val -> val & ~0x80);
  }

  public static void playMusicPackage(final BackgroundMusic music,  final boolean startSequence) {
    AUDIO_THREAD.loadBackgroundMusic(music);
    AUDIO_THREAD.setSequenceVolume(40);

    if(startSequence) {
      AUDIO_THREAD.startSequence();
    }

    musicLoaded_800bd782 = true;
    _800bd0f0 = 2;
  }

  @Method(0x8001de84L)
  public static void loadEncounterSoundsAndMusic() {
    unloadSoundFile(1);
    unloadSoundFile(3);
    unloadSoundFile(4);
    unloadSoundFile(5);
    unloadSoundFile(6);

    if(encounter.musicIndex != 0xff) {
      stopMusicSequence();

      // Pulled this up from below since the methods below queue files which are now loaded synchronously. This code would therefore run before the files were loaded.
      //LAB_8001df8c
      unloadSoundFile(8);

      final int musicIndex = combatMusicFileIndices_800501bc[encounter.musicIndex & 0x1f];
      final int victoryType = switch(combatSoundEffectsTypes_8005019c[encounter.musicIndex & 0x1f]) {
        case 0xc -> 696;
        case 0xd -> 697;
        case 0xe -> 698;
        case 0xf -> 699;
        case 0x56 -> 700;
        case 0x58 -> 701;
        default -> parseMelbuVictory(encounter.musicIndex & 0x1f);
      };

      final var battleMusicEvent = EVENTS.postEvent(new BattleMusicEvent(victoryType, musicIndex, encounter));

      loadedAudioFiles_800bcf78.updateAndGet(val -> val | 0x4000);

      loadEncounterMusic(battleMusicEvent.musicIndex, battleMusicEvent.victoryType);
    }

    //LAB_8001df9c
    loadedAudioFiles_800bcf78.updateAndGet(val -> val | 0x8);

    final AtomicInteger remaining = new AtomicInteger(3);

    // Player combat sounds for current party composition (example file: 764)
    for(int charSlot = 0; charSlot < 3; charSlot++) {
      final int charIndex = gameState_800babc8.charIds_88[charSlot];

      if(charIndex != -1) {
        final String name = getCharacterName(charIndex).toLowerCase();
        final int finalCharSlot = charSlot;
        loadDir("characters/%s/sounds/combat".formatted(name), files -> {
          charSoundEffectsLoaded(files, finalCharSlot);
          if(remaining.decrementAndGet() == 0) {
            //LAB_8001cc38
            //LAB_8001cc40
            FUN_8001e8d4();
          }
        });
      } else {
        if(remaining.decrementAndGet() == 0) {
          FUN_8001e8d4();
        }
      }
    }

    loadMonsterSounds();
  }

  private static int parseMelbuVictory(final int musicIndex) {
    if(musicIndex == 0x13) {
      return 732;
    }

    return -1;
  }

  /**
   * <ul>
   *   <li>0 - menu sounds</li>
   *   <li>1 - players</li>
   *   <li>2 - dragoon/spells/items</li>
   *   <li>3 - monsters</li>
   *   <li>4 - submap sounds</li>
   *   <li>5 - battle cutscene sounds</li>
   *   <li>6 - monster sounds</li>
   *   <li>7 - player sounds</li>
   *   <li>8 - music</li>
   *   <li>9 - world map sounds</li>
   * </ul>
   */
  @Method(0x8001e29cL)
  public static void unloadSoundFile(final int soundType) {
    switch(soundType) {
      case 0 -> {
        if(soundFiles_800bcf80[0].used_00) {
          sssqUnloadPlayableSound(soundFiles_800bcf80[0].playableSound_10);
          soundFiles_800bcf80[0].used_00 = false;
        }
      }

      case 1 -> {
        //LAB_8001e324
        for(int charSlot = 0; charSlot < 3; charSlot++) {
          final int index = characterSoundFileIndices_800500f8[charSlot];

          if(soundFiles_800bcf80[index].used_00) {
            sssqUnloadPlayableSound(soundFiles_800bcf80[index].playableSound_10);
            soundFiles_800bcf80[index].used_00 = false;
          }

          //LAB_8001e374
        }
      }

      case 2 -> {
        if(soundFiles_800bcf80[4].used_00) {
          sssqUnloadPlayableSound(soundFiles_800bcf80[4].playableSound_10);
          soundFiles_800bcf80[4].used_00 = false;
        }
      }

      case 3 -> {
        //LAB_8001e3dc
        for(int monsterIndex = 0; monsterIndex < 4; monsterIndex++) {
          final int index = monsterSoundFileIndices_800500e8[monsterIndex];

          if(soundFiles_800bcf80[index].used_00) {
            sssqUnloadPlayableSound(soundFiles_800bcf80[index].playableSound_10);
            soundFiles_800bcf80[index].used_00 = false;
          }

          //LAB_8001e450
        }
      }

      case 4 -> {
        if(soundFiles_800bcf80[8].used_00) {
          sssqUnloadPlayableSound(soundFiles_800bcf80[8].playableSound_10);
          soundFiles_800bcf80[8].used_00 = false;
        }
      }

      case 5 -> {
        if(soundFiles_800bcf80[9].used_00) {
          sssqUnloadPlayableSound(soundFiles_800bcf80[9].playableSound_10);
          soundFiles_800bcf80[9].used_00 = false;
        }
      }

      case 6, 7 -> {
        if(soundFiles_800bcf80[10].used_00) {
          sssqUnloadPlayableSound(soundFiles_800bcf80[10].playableSound_10);
          soundFiles_800bcf80[10].used_00 = false;
        }
      }

      case 8 -> {
        if(_800bd0f0 != 0) {
          AUDIO_THREAD.unloadMusic();

          //LAB_8001e56c
          _800bd0f0 = 0;
        }

        //LAB_8001e570
        if(soundFiles_800bcf80[11].used_00) {
          sssqUnloadPlayableSound(soundFiles_800bcf80[11].playableSound_10);
          soundFiles_800bcf80[11].used_00 = false;
        }
      }

      case 9 -> {
        if(soundFiles_800bcf80[12].used_00) {
          sssqUnloadPlayableSound(soundFiles_800bcf80[12].playableSound_10);
          soundFiles_800bcf80[12].used_00 = false;
        }
      }
    }

    //LAB_8001e5d4
  }

  @Method(0x8001e5ecL)
  public static void loadMenuSounds() {
    loadedAudioFiles_800bcf78.updateAndGet(val -> val | 0x1);
    loadDrgnFiles(0, Audio::menuSoundsLoaded, "5739/0", "5739/1", "5739/2", "5739/3");
  }

  /**
   * 0: unknown, 2-byte file (00 00)
   * 1: unknown, 0x64 byte file, counts up from 0000 to 0033
   * 2: SShd file
   * 3: Soundbank (has some map and battle sounds)
   */
  @Method(0x8001e694L)
  public static void menuSoundsLoaded(final List<FileData> files) {
    final SoundFile sound = soundFiles_800bcf80[0];
    sound.used_00 = true;
    sound.name = "Menu sounds (file 5739)";

    sound.indices_08 = SoundFileIndices.load(files.get(1));

    sound.playableSound_10 = loadSshdAndSoundbank(sound.name, files.get(3), new Sshd(sound.name, files.get(2)), 0x1010);
    unloadSoundbank_800bd778();
  }

  @Method(0x8001e780L)
  public static void unloadSoundbank_800bd778() {
    loadedAudioFiles_800bcf78.updateAndGet(val -> val & ~0x1);
  }

  @ScriptDescription("Loads the main menu sounds")
  @Method(0x8001e640L)
  public static FlowControl scriptLoadMenuSounds(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }


  @Method(0x8001e8d4L)
  public static void FUN_8001e8d4() {
    loadedAudioFiles_800bcf78.updateAndGet(val -> val & ~0x8);
  }

  @ScriptDescription("Unused")
  @Method(0x8001e918L)
  public static FlowControl FUN_8001e918(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Loads attack sounds for a character")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "type", description = "The attack sounds type, 1 for regular, any other value for dragoon")
  @Method(0x8001e920L)
  public static FlowControl scriptLoadCharAttackSounds(final RunningScript<?> script) {
    loadCharAttackSounds(script.params_20[0].get(), script.params_20[1].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x8001e950L)
  public static void cleanUpCharAttackSounds() {
    loadedAudioFiles_800bcf78.updateAndGet(val -> val & ~0x8);
  }

  @Method(0x8001e98cL)
  public static void FUN_8001e98c(final List<FileData> files, final String soundName) {
    final SoundFile sound = soundFiles_800bcf80[4];
    sound.used_00 = true;
    sound.name = soundName;

    sound.indices_08 = SoundFileIndices.load(files.get(1));
    sound.id_02 = files.get(0).readShort(0);

    sound.playableSound_10 = loadSshdAndSoundbank(sound.name, files.get(3), new Sshd(sound.name, files.get(2)), 0x5_a1e0);
    FUN_8001ea5c();
    setSoundSequenceVolume(sound.playableSound_10, 0x7f);
  }

  @Method(0x8001ea5cL)
  public static void FUN_8001ea5c() {
    loadedAudioFiles_800bcf78.updateAndGet(val -> val & ~0x40);
  }

  @Method(0x8001eadcL)
  public static void loadSubmapSounds(final int submapIndex) {
    loadedAudioFiles_800bcf78.updateAndGet(val -> val | 0x2);
    loadDrgnDir(0, 5750 + submapIndex, files -> submapSoundsLoaded(files, "Submap %d sounds (file %d)".formatted(submapIndex, 5750 + submapIndex)));
  }

  @ScriptDescription("Unused")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "unused")
  @Method(0x8001eb30L)
  public static FlowControl FUN_8001eb30(final RunningScript<?> script) {
    return FlowControl.CONTINUE;
  }

  @Method(0x8001eb38L)
  public static void submapSoundsLoaded(final List<FileData> files, final String soundName) {
    soundFiles_800bcf80[8].name = soundName;
    soundFiles_800bcf80[8].indices_08 = SoundFileIndices.load(files.get(2));
    soundFiles_800bcf80[8].ptr_0c = files.get(1);
    soundFiles_800bcf80[8].id_02 = files.get(0).readShort(0);

    final Sshd sshd = new Sshd(soundName, files.get(3));
    if(files.get(4).size() != sshd.soundBankSize_04) {
      throw new RuntimeException("Size didn't match, need to resize array or something");
    }

    soundFiles_800bcf80[8].playableSound_10 = loadSshdAndSoundbank(soundName, files.get(4), sshd, 0x4_de90);
    submapSoundsCleanup();
    setSoundSequenceVolume(soundFiles_800bcf80[8].playableSound_10, 0x7f);
    soundFiles_800bcf80[8].used_00 = true;
  }

  @Method(0x8001ec18L)
  public static void submapSoundsCleanup() {
    loadedAudioFiles_800bcf78.updateAndGet(val -> val & ~0x2);
    musicLoaded_800bd782 = true;
  }

  @ScriptDescription("Load a battle cutscene's sounds")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "cutsceneIndex", description = "The cutscene index")
  @Method(0x8001ecccL)
  public static FlowControl scriptLoadBattleCutsceneSounds(final RunningScript<?> script) {
    loadedAudioFiles_800bcf78.updateAndGet(val -> val | 0x4);
    sssqResetStuff();
    final int cutsceneIndex = script.params_20[0].get();
    final int dirIndex = 2437 + cutsceneIndex * 3;
    loadDrgnDir(0, dirIndex, fileData -> battleCutsceneSoundsLoaded(fileData, "Cutscene %d sounds (file %d)".formatted(cutsceneIndex, dirIndex)));
    return FlowControl.CONTINUE;
  }

  @Method(0x8001ed3cL)
  public static void loadExtraBattleCutsceneSoundbanks() {
    final SoundFile soundFile = soundFiles_800bcf80[9];

    //LAB_8001ed88
    for(int i = 1; i <= soundFile.numberOfExtraSoundbanks_18; i++) {
      loadDrgnFileSync(0, 2437 + soundFile.id_02 * 3 + i, Audio::uploadExtraBattleCutsceneSoundbank);
    }

    //LAB_8001edd4
    loadedAudioFiles_800bcf78.updateAndGet(val -> val & ~0x4);
    musicLoaded_800bd782 = true;
  }

  @Method(0x8001edfcL)
  public static void uploadExtraBattleCutsceneSoundbank(final FileData file) {
    final SoundFile soundFile = soundFiles_800bcf80[9];

    SPU.directWrite(0x4_de90 + soundFile.spuRamOffset_14, file.getBytes());
    soundFile.spuRamOffset_14 += file.size();
  }

  @Method(0x8001eea8L)
  public static void loadLocationMenuSoundEffects(final int index) {
    loadedAudioFiles_800bcf78.updateAndGet(val -> val | 0x8000);
    loadDrgnDir(0, 5740 + index, files -> FUN_8001eefc(files, "WMAP destination sounds %d (file %d)".formatted(index, 5740 + index)));
  }

  @Method(0x8001eefcL)
  public static void FUN_8001eefc(final List<FileData> files, final String soundName) {
    final SoundFile sound = soundFiles_800bcf80[12];
    sound.name = soundName;
    sound.used_00 = true;

    sound.indices_08 = SoundFileIndices.load(files.get(2));
    sound.id_02 = files.get(0).readShort(0);

    sound.playableSound_10 = loadSshdAndSoundbank(sound.name, files.get(4), new Sshd(sound.name, files.get(3)), 0x4_de90);
    FUN_8001efcc();

    setSoundSequenceVolume(sound.playableSound_10, 0x7f);
  }

  @Method(0x8001efccL)
  public static void FUN_8001efcc() {
    loadedAudioFiles_800bcf78.updateAndGet(val -> val & ~0x8000);
  }

  @ScriptDescription("Load a monster's sounds")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "monsterIndex", description = "The monster index")
  @Method(0x8001f070L)
  public static FlowControl scriptLoadMonsterAttackSounds(final RunningScript<?> script) {
    loadedAudioFiles_800bcf78.updateAndGet(val -> val | 0x20);
    unloadSoundFile(6);
    final int monsterIndex = script.params_20[0].get();
    final int dirIndex = 1841 + monsterIndex;
    loadDrgnDir(0, dirIndex, files -> monsterAttackSoundsLoaded(files, "Monster %d sounds (file %d)".formatted(monsterIndex, dirIndex)));
    return FlowControl.CONTINUE;
  }

  @Method(0x8001f0dcL)
  public static void monsterAttackSoundsLoaded(final List<FileData> files, final String soundName) {
    final SoundFile sound = soundFiles_800bcf80[10];
    sound.name = soundName;
    sound.used_00 = true;

    sound.indices_08 = SoundFileIndices.load(files.get(1));
    sound.id_02 = files.get(0).readShort(0);

    sound.playableSound_10 = loadSshdAndSoundbank(sound.name, files.get(3), new Sshd(sound.name, files.get(2)), 0x6_6930);

    setSoundSequenceVolume(sound.playableSound_10, 0x7f);
    loadedAudioFiles_800bcf78.updateAndGet(val -> val & ~0x20);
  }

  @ScriptDescription("Loads a character's attack sounds")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "charId", description = "The character ID")
  @Method(0x8001f250L)
  public static FlowControl scriptLoadCharacterAttackSounds(final RunningScript<?> script) {
    loadedAudioFiles_800bcf78.updateAndGet(val -> val | 0x1_0000);
    unloadSoundFile(7);
    final int charId = script.params_20[0].get();
    final int dirIndex = 1897 + charId;
    loadDrgnDir(0, dirIndex, files -> characterAttackSoundsLoaded(files, "Addition index %d sounds (file %d)".formatted(charId, dirIndex)));
    return FlowControl.CONTINUE;
  }

  @Method(0x8001f2c0L)
  public static void characterAttackSoundsLoaded(final List<FileData> files, final String soundName) {
    final SoundFile sound = soundFiles_800bcf80[10];
    sound.name = soundName;
    sound.used_00 = true;

    sound.indices_08 = SoundFileIndices.load(files.get(1));
    sound.id_02 = files.get(0).readShort(0);

    sound.playableSound_10 = loadSshdAndSoundbank(sound.name, files.get(3), new Sshd(sound.name, files.get(2)), 0x6_6930);
    FUN_8001f390();

    setSoundSequenceVolume(sound.playableSound_10, 0x7f);
  }

  @Method(0x8001f390L)
  public static void FUN_8001f390() {
    loadedAudioFiles_800bcf78.updateAndGet(val -> val & ~0x1_0000);
  }

  /**
   * Loads an audio MRG from DRGN0. File index is 5815 + index * 5.
   *
   * @param index <ol start="0">
   *   <li>Inventory music</li>
   *   <li>Main menu music</li>
   *   <li>Seems to be the previous one and the next one combined?</li>
   *   <li>Battle music, more?</li>
   *   <li>Same as previous</li>
   *   <li>...</li>
   * </ol>
   */
  @Method(0x8001f3d0L)
  public static void loadMusicPackage(final int index) {
    unloadSoundFile(8);
    loadedAudioFiles_800bcf78.updateAndGet(val -> val | 0x80);
    final int fileIndex = 5815 + index * 5;
    loadDrgnDir(0, fileIndex, files -> musicPackageLoadedCallback(files, fileIndex, true));
  }

  @ScriptDescription("Load a music package")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "musicIndex", description = "The music index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "dontStartSequence", description = "If true, the sequence will not be started automatically")
  @Method(0x8001f450L)
  public static FlowControl scriptLoadMusicPackage(final RunningScript<?> script) {
    unloadSoundFile(8);
    loadedAudioFiles_800bcf78.updateAndGet(val -> val | 0x80);
    final int fileIndex = 5815 + script.params_20[0].get() * 5;
    final boolean playSequence = script.params_20[1].get() == 0;
    loadDrgnDir(0, fileIndex, files -> musicPackageLoadedCallback(files, fileIndex, playSequence));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Loads sounds for the final battle")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "battleProgress", description = "The current stage of the multi-stage final fight (0-3)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "dontStartSequence", description = "If true, the sequence will not be started automatically")
  @Method(0x8001f560L)
  public static FlowControl scriptLoadFinalBattleSounds(final RunningScript<?> script) {
    unloadSoundFile(8);
    loadedAudioFiles_800bcf78.updateAndGet(val -> val | 0x80);
    final int fileIndex = 732 + script.params_20[0].get() * 5;
    final boolean playSequence = script.params_20[1].get() == 0;
    loadDrgnDir(0, fileIndex, files -> musicPackageLoadedCallback(files, fileIndex, playSequence));
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Loads sounds for a cutscene")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "cutsceneIndex", description = "The cutscene index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.BOOL, name = "dontStartSequence", description = "If true, the sequence will not be started automatically")
  @Method(0x8001f674L)
  public static FlowControl scriptLoadCutsceneSounds(final RunningScript<?> script) {
    unloadSoundFile(8);
    loadedAudioFiles_800bcf78.updateAndGet(val -> val | 0x80);
    final int fileIndex = 2353 + script.params_20[0].get() * 6;
    final boolean playSequence = script.params_20[1].get() == 0;
    loadDrgnDir(0, fileIndex, files -> musicPackageLoadedCallback(files, fileIndex, playSequence));
    return FlowControl.CONTINUE;
  }

  @Method(0x8001f708L)
  public static void loadWmapMusic(final int chapterIndex) {
    final int fileIndex = 5850 + chapterIndex * 5;

    if(AUDIO_THREAD.getSongId() * 5 + 5815 == fileIndex) {
      return;
    }

    unloadSoundFile(8);
    loadedAudioFiles_800bcf78.updateAndGet(val -> val | 0x80);
    loadDrgnDir(0, fileIndex, files -> musicPackageLoadedCallback(files, fileIndex, true));
  }

  @Method(0x8001fb44L)
  public static void FUN_8001fb44(final List<FileData> files, final int fileIndex, final int victoryType) {
    final BackgroundMusic bgm = new BackgroundMusic(files, fileIndex);
    bgm.setVolume(40 / 128.0f);

    loadDrgnDir(0, victoryType, victoryFiles -> loadVictoryMusic(victoryFiles, bgm));

    loadedAudioFiles_800bcf78.updateAndGet(val -> val & ~0x4000);

    AUDIO_THREAD.loadBackgroundMusic(bgm);

    _800bd0f0 = 2;

    loadedAudioFiles_800bcf78.updateAndGet(val -> val & ~0x80);

    AUDIO_THREAD.startSequence();

    musicLoaded_800bd782 = true;
  }

  private static void loadVictoryMusic(final List<FileData> files, final BackgroundMusic battleMusic) {
    victoryMusic = battleMusic.createVictoryMusic(files);
    victoryMusic.setVolume(40 / 128.0f);
  }

  @ScriptDescription("Load some kind of audio package")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "musicIndex", description = "The music index")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "p1")
  @Method(0x8001fe28L)
  public static FlowControl FUN_8001fe28(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x8001ffb0L)
  public static int getLoadedAudioFiles() {
    return loadedAudioFiles_800bcf78.get();
  }

  @ScriptDescription("Gets the currently-loaded sound files")
  @ScriptParam(direction = ScriptParam.Direction.OUT, type = ScriptParam.Type.INT, name = "flags", description = "The loaded file flags")
  @Method(0x8001ffc0L)
  public static FlowControl scriptGetLoadedSoundFiles(final RunningScript<?> script) {
    script.params_20[0].set(loadedAudioFiles_800bcf78.get());
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Unloads a sound file")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "soundType", description = "The sound type")
  @Method(0x8001ffdcL)
  public static FlowControl scriptUnloadSoundFile(final RunningScript<?> script) {
    unloadSoundFile(script.params_20[0].get());
    return FlowControl.CONTINUE;
  }

  @Method(0x80020008L)
  public static void sssqResetStuff() {
    stopAndResetSoundsAndSequences();
    unloadSoundFile(1);
    unloadSoundFile(3);
    unloadSoundFile(4);
    unloadSoundFile(5);
    unloadSoundFile(6);
    unloadSoundFile(8);
    stopMusicSequence();
  }

  @ScriptDescription("Stops and unloads all music and sound sequences")
  @Method(0x80020060L)
  public static FlowControl scriptStopAndUnloadSequences(final RunningScript<?> script) {
    stopAndResetSoundsAndSequences();
    unloadSoundFile(1);
    unloadSoundFile(3);
    unloadSoundFile(4);
    unloadSoundFile(5);
    unloadSoundFile(6);
    unloadSoundFile(8);
    stopMusicSequence();
    return FlowControl.CONTINUE;
  }

  @ScriptDescription("Mark a character battle entity's sound files as no longer used")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "bentIndex", description = "The BattleEntity27c index")
  @Method(0x8002013cL)
  public static FlowControl scriptUnuseCharSoundFile(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @ScriptDescription("Stops and unloads the encounter's sound effects")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "index", description = "The sound effect index")
  @Method(0x80020230L)
  public static FlowControl scriptStopEncounterSoundEffects(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @ScriptDescription("Frees the encounter's sound effects")
  @Method(0x800202a4L)
  public static FlowControl scriptFreeEncounterSoundEffects(final RunningScript<?> script) {
    throw new RuntimeException("Not implemented");
  }

  @Method(0x80020308L)
  public static void FUN_80020308() {
    stopCurrentMusicSequence();
    stopAndResetSoundsAndSequences();
    unloadSoundFile(1);
    unloadSoundFile(3);
    unloadSoundFile(4);
    unloadSoundFile(5);
    unloadSoundFile(6);
  }

  @Method(0x80020360L)
  public static void copyPlayingSounds(final Queue<QueuedSound28> sources, final Queue<QueuedSound28> dests) {
    //LAB_8002036c
    dests.clear();

    for(final QueuedSound28 queuedSound : sources) {
      dests.add(queuedSound);

      if(queuedSound.type_00 == 4 && queuedSound._1c != 0) {
        queuedSound.type_00 = 3;
      }
    }
  }

  @ScriptDescription("Replaces a monster's attack sounds (e.g. used when Melbu changes forms)")
  @ScriptParam(direction = ScriptParam.Direction.IN, type = ScriptParam.Type.INT, name = "monsterId", description = "The monster ID")
  @Method(0x800203f0L)
  public static FlowControl scriptReplaceMonsterSounds(final RunningScript<?> script) {
    unloadSoundFile(3);
    loadedAudioFiles_800bcf78.updateAndGet(val -> val | 0x10);

    final int fileIndex = 1290 + script.params_20[0].get();

    final String path;
    switch(fileIndex) {
      case 1290 -> path = "monsters/phases/doel/0";
      case 1291 -> path = "monsters/phases/doel/1";
      case 1292 -> path = "monsters/phases/melbu/0";
      case 1293 -> path = "monsters/phases/melbu/1";
      case 1294 -> path = "monsters/phases/melbu/4";
      case 1295 -> path = "monsters/phases/melbu/6";
      case 1296 -> path = "monsters/phases/zackwell/0";
      case 1297 -> path = "monsters/phases/zackwell/1";
      default -> throw new IllegalArgumentException("Unknown battle phase file index " + fileIndex);
    }

    final AtomicInteger soundbankOffset = new AtomicInteger();
    final AtomicInteger count = new AtomicInteger(0);

    for(int monsterSlot = 0; monsterSlot < 4; monsterSlot++) {
      if(Loader.exists(path + '/' + monsterSlot)) {
        count.incrementAndGet();
      }
    }

    for(int monsterSlot = 0; monsterSlot < 4; monsterSlot++) {
      final SoundFile file = soundFiles_800bcf80[monsterSoundFileIndices_800500e8[monsterSlot]];
      file.id_02 = -1;
      file.used_00 = false;

      if(Loader.exists(path + '/' + monsterSlot)) {
        final int finalMonsterSlot = monsterSlot;
        loadDir(path + '/' + monsterSlot, files -> {
          final int offset = soundbankOffset.getAndUpdate(val -> val + MathHelper.roundUp(files.get(3).size(), 0x10));
          monsterSoundLoaded(files, "Monster slot %d (file %s) (replaced)".formatted(finalMonsterSlot, path), finalMonsterSlot, offset);

          if(count.decrementAndGet() == 0) {
            loadedAudioFiles_800bcf78.updateAndGet(val -> val & ~0x10);
          }
        });
      }
    }

    return FlowControl.CONTINUE;
  }

  @Method(0x80023870L)
  public static void playMenuSound(final int soundIndex) {
    playSound(0, soundIndex, (short)0, (short)0);
  }

  @Method(0x8002c984L)
  public static int playXaAudio(final int xaLoadingStage, final int xaArchiveIndex, final int xaFileIndex) {
    //LAB_8002c9f0
    if(xaFileIndex == 0 || xaFileIndex >= 32 || xaLoadingStage >= 5) {
      return 0;
    }

    if(xaLoadingStage == 3) {
      LOGGER.info("Playing XA archive %d file %d", xaArchiveIndex, xaFileIndex);

      //LAB_8002c448
      AUDIO_THREAD.loadXa(Loader.loadFile("XA/LODXA0%d.XA/%d.opus".formatted(xaArchiveIndex, xaFileIndex)));
      _800bf0cf = 4;
    }

    if(xaLoadingStage == 2) {
      _800bf0cf = 4;
    }

    return 0;
  }

  public static void stopXaAudio() {
    AUDIO_THREAD.stopXa();
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

    if(sshd.hasSubfile(4)) {
      if(soundEnv.playingSoundsUpperBound_03 != 0) {
        if(playableSound.used_00) {
          if(patchIndex <= patchList.patchCount_00) {
            if(patchList.patches_02[patchIndex] != null) {
              final PatchList.SequenceList sequenceList = patchList.patches_02[patchIndex];
              final int sequenceCount = sequenceList.sequenceCount_00;

              if(sequenceCount >= sequenceIndex) {
                sssqish_800c4aa8 = sshd.getSubfile(4, (name, data, offset) -> new Sssqish(name, data, offset, sshd.getSubfileSize(4)));
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
    sssqChannelInfo_800C6680 = sshd.getSubfile(4, (name, data, offset) -> new Sssqish(name, data, offset, sshd.getSubfileSize(4))).entries_10[playingNote.sequenceChannel_04];

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
   * @return Index into {@link #playableSounds_800c43d0}, or -1 on error
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
      AUDIO_THREAD.setReverb(type);
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

    final Sssqish sssq = playableSound.sshdPtr_04.getSubfile(4, (name, data, offset) -> new Sssqish(name, data, offset, playableSound.sshdPtr_04.getSubfileSize(4)));
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
      soundEnv.pitchShiftVolLeft_26 = Math.clamp(pitchShiftVolLeft, -0x1000, 0x1000);
      soundEnv.pitchShiftVolRight_28 = Math.clamp(pitchShiftVolRight, -0x1000, 0x1000);
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

            // See stopSoundsAndSequences for a detailed explanation
            voice.adsrVolume = 0;

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

            // Since we run the SPU in lockstep now, the ADSR doesn't have time to tick and reduce the volume after
            // the registers are cleared when changing engine states. The sequencer doesn't actually cull finished
            // voices until their ADSR volume is < 0x10, so if this method is called when voices are all in use and
            // then another sound is immediately played, it won't have any voices available. Even though all sound
            // sequences are marked as stopped and the voices were reset.
            //
            // This was happening in the world map when piloting the Queen Fury. It plays far too many overlapping
            // sounds for the SPU to handle (LOD sets the max to 8) so if a battle started, the transition sound
            // would either be missing one or both channels (i.e. either the right channel would overwrite the left,
            // or neither would play at all).
            // See GH#1719
            voice.adsrVolume = 0;

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
  private static final int SOUND_TPS = 60;
  private static final int NANOS_PER_TICK = 1_000_000_000 / SOUND_TPS;
  private static boolean soundRunning;

  public static void startSound() {
    soundRunning = true;
    final Thread sfx = new Thread(Audio::soundLoop);
    sfx.setName("SFX");
    sfx.start();
  }

  public static void stopSound() {
    soundRunning = false;
  }

  private static void soundLoop() {
    long time = System.nanoTime();

    while(soundRunning) {
      try {
        SEQUENCER.tick();
        SPU.tick();
      } catch(final Throwable t) {
        LOGGER.error("Sound thread crashed!", t);
      }


      long interval = System.nanoTime() - time;

      // Failsafe if we run too far behind (also applies to pausing in IDE)
      if(interval >= NANOS_PER_TICK * 3) {
        LOGGER.debug("Sequencer running behind, skipping ticks to catch up");
        interval = NANOS_PER_TICK;
        time = System.nanoTime() - interval;
      }

      final int toSleep = (int)Math.max(0, NANOS_PER_TICK - interval) / 1_000_000;
      DebugHelper.sleep(toSleep);
      time += NANOS_PER_TICK;
    }
  }

}
