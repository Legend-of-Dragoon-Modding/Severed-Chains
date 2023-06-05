package legend.game.sound;

import legend.core.memory.Method;
import legend.core.spu.Spu;
import legend.core.spu.Voice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.function.Supplier;

import static legend.core.GameEngine.SPU;
import static legend.game.Scus94491BpeSegment_8004.calculateNoteVolume;
import static legend.game.Scus94491BpeSegment_8004.setKeyOff;
import static legend.game.Scus94491BpeSegment_8004.setKeyOn;
import static legend.game.Scus94491BpeSegment_8004.setMainVolume;
import static legend.game.Scus94491BpeSegment_8004.setSequenceVolume;
import static legend.game.Scus94491BpeSegment_8004.sssqSetReverbType;
import static legend.game.Scus94491BpeSegment_8004.sssqSetReverbVolume;
import static legend.game.Scus94491BpeSegment_8004.stopMusicSequence;
import static legend.game.Scus94491BpeSegment_8004.updateVoiceVolume;
import static legend.game.Scus94491BpeSegment_8005._80059b3c;
import static legend.game.Scus94491BpeSegment_8005.panVolume_80059f3c;
import static legend.game.Scus94491BpeSegment_8005.sssqFadeCurrent_8005a1ce;
import static legend.game.Scus94491BpeSegment_800c.instrumentLayerIndex_800c6678;
import static legend.game.Scus94491BpeSegment_800c.instrumentLayer_800c6678;
import static legend.game.Scus94491BpeSegment_800c.instrumentLayers_800c6678;
import static legend.game.Scus94491BpeSegment_800c.instrument_800c6674;
import static legend.game.Scus94491BpeSegment_800c.instruments_800c4aa8;
import static legend.game.Scus94491BpeSegment_800c.playingNotes_800c3a40;
import static legend.game.Scus94491BpeSegment_800c.sequenceData_800c4ac8;
import static legend.game.Scus94491BpeSegment_800c.soundEnv_800c6630;
import static legend.game.Scus94491BpeSegment_800c.sshdPtr_800c4ac0;
import static legend.game.Scus94491BpeSegment_800c.sssqChannelInfo_800C6680;
import static legend.game.Scus94491BpeSegment_800c.sssqReader_800c667c;
import static legend.game.Scus94491BpeSegment_800c.sssqish_800c4aa8;
import static legend.game.Scus94491BpeSegment_800c.volumeRamp_800c4ab0;
import static legend.game.Scus94491BpeSegment_800c.waveforms_800c4ab8;

public class Sequencer {
  private static final Logger LOGGER = LogManager.getFormatterLogger();
  private static final Marker SEQUENCER_MARKER = MarkerManager.getMarker("SEQUENCER");

  private final Object lock = new Object();

  public <T> T waitForLock(final Supplier<T> task) {
    synchronized(this.lock) {
      return task.get();
    }
  }

  @Method(0x80045cb8L)
  public void tick() {
    synchronized(this.lock) {
      this.sssqFreeFinishedNotes();

      final SoundEnv44 soundEnv = soundEnv_800c6630;

      //LAB_80045d04
      for(soundEnv.channelIndex_01 = 0; soundEnv.channelIndex_01 < 24; soundEnv.channelIndex_01++) {
        final SequenceData124 sequenceData = sequenceData_800c4ac8[soundEnv.channelIndex_01];

        if(sequenceData.musicPlaying_028 || sequenceData.soundPlaying_02a) {
          //LAB_80045d24
          this.setActiveSequence(sequenceData);

          LAB_80045d40:
          while(sequenceData.deltaTime_118 == 0) {
            this.sssqReadEvent(sequenceData);

            if(!this.setActiveSequence(soundEnv.channelIndex_01)) {
              sequenceData.sssqReader_010.advance(3);
              soundEnv.sequenceLoaded_04 = false;
            } else {
              //LAB_80045d7c
              soundEnv.sequenceLoaded_04 = true;

              final int command = sequenceData.command_000 & 0xf0;
              if(command == 0x80) { // Key off event
                //LAB_80045fdc
                this.sssqHandleKeyOff(sequenceData);
                //LAB_80045dc0
              } else if(command == 0x90) { // Key on event
                //LAB_80046004
                this.sssqHandleKeyOn(sequenceData);
              } else if(command == 0xa0) { // Polyphonic key pressure (aftertouch)
                //LAB_80045ff0
                this.sssqHandlePolyphonicKeyPressure(sequenceData);
                //LAB_80045dd4
              } else if(command == 0xb0) { // Control change
                //LAB_80045e60
                switch(sequenceData.param0_002) { // Control number
                  case 0x1 -> this.sssqHandleModulationWheel(sequenceData); // Modulation wheel
                  case 0x2 -> this.sssqHandleBreathControl(sequenceData); // Breath control
                  case 0x6 -> this.sssqHandleDataEntry(sequenceData); // Data entry
                  case 0x7 -> this.sssqHandleVolume(sequenceData); // Volume
                  case 0xa -> this.sssqHandlePan(sequenceData); // Pan
                  case 0x40 -> this.sssqHandleSustain(sequenceData); // Damper pedal (sustain)
                  case 0x41 -> this.sssqHandlePortamento(sequenceData); // Portamento

                  case 0x60 -> { // Only used in SFX? Sets some kind of repeat flag. Not "data increment" like in MIDI spec
                    this.sssqHandleRepeat(sequenceData); // Seems to jump to a different part of the sequence
                    this.sssqReadDeltaTime(soundEnv.channelIndex_01);
                    break LAB_80045d40;
                  }

                  case 0x62 -> this.sssqDataEntryLsb(soundEnv.channelIndex_01); // Non-registered parameter number LSB
                  case 0x63 -> this.sssqDataEntryMsb(soundEnv.channelIndex_01); // Non-registered parameter number MSB
                }
              } else if(command == 0xc0) { // Program change
                //LAB_80045e4c
                this.sssqHandleProgramChange(sequenceData);
              } else if(command == 0xe0) { // Pitch bend
                //LAB_80045fc8
                this.sssqHandlePitchBend(sequenceData);
              } else if(command == 0xf0) { // Meta event
                //LAB_80045df8
                if(sequenceData.param0_002 == 0x2f) { // End of track
                  //LAB_80045e24
                  this.sssqHandleEndOfTrack(sequenceData);
                  break;
                }

                if(sequenceData.param0_002 == 0x51) { // Tempo
                  //LAB_80045e38
                  this.sssqHandleTempo(sequenceData);
                }
              }
            }

            //LAB_80046010
            this.sssqReadDeltaTime(soundEnv.channelIndex_01);
          }

          //LAB_8004602c
          if(soundEnv.sequenceLoaded_04) {
            soundEnv.keyOn_3a |= sequenceData.keyOn_0de;
            soundEnv.keyOff_3e |= sequenceData.keyOff_0e2;

            sequenceData.keyOn_0de = 0;
            sequenceData.keyOff_0e2 = 0;

            soundEnv.sequenceLoaded_04 = false;
          }

          //LAB_800460a0
          if(sequenceData.tempo_108 != 0 || sequenceData.soundPlaying_02a) {
            //LAB_800460c0
            if(sequenceData.deltaTime_118 != 0) {
              sequenceData.deltaTime_118--;
            }
          }

          //LAB_800460d4
          if(sequenceData.repeat_037) {
            sequenceData.command_000 = sequenceData.repeatDestCommand_039;
            sequenceData.previousCommand_001 = sequenceData.repeatDestCommand_039;
            sequenceData.sssqReader_010.jump(sequenceData.repeatOffset_02c);
            sequenceData.repeat_037 = false;

            if(!sequenceData._0e6) {
              //LAB_80046118
              sequenceData.musicPlaying_028 = true;
              sequenceData.deltaTime_118 = 0;
            } else {
              sequenceData.soundPlaying_02a = true;
              sequenceData._0e6 = false;
            }
          }
        }

        //LAB_80046120
        this.handleVolumeChanging(sequenceData);
      }

      SPU.setNoiseMode(soundEnv.noiseMode_16);
      SPU.setReverbMode(soundEnv.reverbMode_12);

      if(soundEnv.keyOff_3e != 0) {
        SPU.keyOff(soundEnv.keyOff_3e);
      }

      //LAB_800461b0
      if(soundEnv.keyOn_3a != 0) {
        SPU.keyOn(soundEnv.keyOn_3a);
      }

      //LAB_800461e0
      soundEnv.keyOn_3a = 0;
      soundEnv.keyOff_3e = 0;

      this.sssqTickEffectsOverTime();
      this.handleFadeInOut();
    }
  }

  @Method(0x80046224L)
  public void sssqHandlePolyphonicKeyPressure(final SequenceData124 sequenceData) {
    LOGGER.info(SEQUENCER_MARKER, "Polyphonic key pressure channel %d, note %d, velocity %d", sequenceData.command_000 & 0xf, sequenceData.param0_002, sequenceData.param1_003);

    if(sequenceData.param1_003 == 0) { // Velocity
      this.keyOffMatchingNotes(sequenceData);
      return;
    }

    //LAB_8004629c
    final int instrumentIndex = sequenceData.param0_002 - instrument_800c6674.startingKeyPosition_06;
    if(instrumentIndex < 0) {
      return;
    }

    instrumentLayerIndex_800c6678 += instrumentIndex;
    instrumentLayer_800c6678 = instrumentLayers_800c6678[instrumentLayerIndex_800c6678];
    sssqChannelInfo_800C6680.pitchBend_0a = 64;

    final short voiceIndex = (short)this.findMatchingOrFreeNote(instrumentLayer_800c6678.minKeyRange_00, instrumentLayer_800c6678.maxKeyRange_01, sequenceData.playableSound_020);
    if(voiceIndex == -1) {
      sequenceData.sssqReader_010.advance(4);
      soundEnv_800c6630.sequenceLoaded_04 = false;
      return;
    }

    //LAB_8004632c
    final PlayingNote66 playingNote = playingNotes_800c3a40[voiceIndex];
    if((instrumentLayer_800c6678.flags_0f & 0x1) != 0) {
      playingNote.finished_08 = false;
      playingNote._0c = 1;
    } else {
      //LAB_80046374
      playingNote.finished_08 = true;
      playingNote._0c = 0;
    }

    //LAB_8004639c
    playingNote.used_00 = true;
    playingNote.noteNumber_02 = sequenceData.param0_002;
    playingNote.sequenceChannel_04 = sequenceData.command_000 & 0xf;
    playingNote.sequenceData_06 = sequenceData;
    playingNote._0a = soundEnv_800c6630._00;
    playingNote.instrumentIndex_0e = instrumentIndex;
    playingNote._12 = 0;
    playingNote._18 = 0;
    playingNote.isPolyphonicKeyPressure_1a = true;
    playingNote._1c = 4;
    playingNote.maxKeyRange_1e = instrumentLayer_800c6678.maxKeyRange_01;
    playingNote.minKeyRange_20 = instrumentLayer_800c6678.minKeyRange_00;
    playingNote.playableSound_22 = sequenceData.playableSound_020;
    playingNote.patchIndex_24 = sequenceData.patchIndex_024;
    playingNote.sequenceIndex_26 = sequenceData.sequenceIndex_022;
    playingNote.channelVolume_28 = sssqChannelInfo_800C6680.volume_0e;
    playingNote.instrumentVolume_2a = instrument_800c6674.patchVolume_01;
    playingNote.velocityVolume_2c = volumeRamp_800c4ab0.ramp_02[sequenceData.param1_003];
    playingNote.instrumentLayerVolume_2e = instrumentLayer_800c6678.volume_0b;
    playingNote.volumeLeftRight_30[0] = panVolume_80059f3c.get((this.calculatePan(4, 0) / 2 & 0x7ffe) / 2).left_00.get();
    playingNote.volumeLeftRight_30[1] = panVolume_80059f3c.get((this.calculatePan(4, 0) / 2 & 0x7ffe) / 2).right_01.get();
    playingNote.volume_34 = sssqChannelInfo_800C6680.volume_03;
    playingNote.cents_36 = instrumentLayer_800c6678.cents_03;
    playingNote.pitchBend_38 = sssqChannelInfo_800C6680.pitchBend_0a;
    playingNote.pitchBendMultiplier_3a = instrumentLayer_800c6678.pitchBendMultiplier_0d;
    playingNote.breath_3c = sssqChannelInfo_800C6680.breath_0c;
    playingNote.nextCommand_3e = sequenceData.param2_005;
    playingNote.portamentoChanging_44 = false;
    playingNote.rootKey_40 = instrumentLayer_800c6678.rootKey_02;
    playingNote._4a = instrumentLayer_800c6678._0a;
    playingNote.pan_4c = instrumentLayer_800c6678.pan_0c;
    playingNote.portamentoNote_4e = 120;
    playingNote.portamentoTimeRemaining_62 = 0;

    if(soundEnv_800c6630._00 < 24) {
      soundEnv_800c6630._00++;
    }

    final int flags = instrumentLayer_800c6678.flags_0f;
    if((flags & 0x20) != 0) { // Modulation on
      if((flags & 0x40) != 0) {
        playingNote.breathControlListIndex_10 = instrument_800c6674._05;
      } else {
        //LAB_800465a4
        playingNote.breathControlListIndex_10 = instrumentLayer_800c6678._0e;
      }

      //LAB_800465b0
      playingNote.modulationEnabled_14 = true;
      playingNote.modulation_16 = 127;
    } else {
      //LAB_800465ec
      playingNote.modulationEnabled_14 = false;
    }

    //LAB_800465f0
    int l = this.calculateVolume(sequenceData, this.calculatePan(4, 0), 0);
    int r = this.calculateVolume(sequenceData, this.calculatePan(4, 0), 1);

    final int pitchBendMultiplier;
    if((instrumentLayer_800c6678.flags_0f & 0x10) != 0) {
      pitchBendMultiplier = instrument_800c6674.pitchBendMultiplier_04;
    } else {
      //LAB_80046668
      pitchBendMultiplier = instrumentLayer_800c6678.pitchBendMultiplier_0d;
    }

    //LAB_8004666c
    final Voice voice = SPU.voices[voiceIndex];

    if(sequenceData.pitchShifted_0e9) {
      //LAB_8004669c
      voice.pitch = this.calculateSampleRate(instrumentLayer_800c6678.rootKey_02, sequenceData.param0_002, instrumentLayer_800c6678.cents_03 * 4, sssqChannelInfo_800C6680.pitchBend_0a, pitchBendMultiplier) * sequenceData.pitch_0ec / 0x1000;
      l = this.scaleValue12((short)l, (short)sequenceData.pitchShiftVolLeft_0ee);
      r = this.scaleValue12((short)r, (short)sequenceData.pitchShiftVolRight_0f0);
      playingNote.pitchShifted_42 = true;
    } else {
      //LAB_80046730
      //LAB_80046750
      voice.pitch = this.calculateSampleRate(instrumentLayer_800c6678.rootKey_02, sequenceData.param0_002, instrumentLayer_800c6678.cents_03 * 4, sssqChannelInfo_800C6680.pitchBend_0a, pitchBendMultiplier);
      l = this.scaleValue12((short)l, (short)0x1000);
      r = this.scaleValue12((short)r, (short)0x1000);
      playingNote.pitchShifted_42 = false;
    }

    //LAB_800467c8
    //LAB_800467f0
    voice.volumeLeft.set(l);
    voice.volumeRight.set(r);
    voice.startAddress = sequenceData.playableSound_020.soundBufferPtr_08 + instrumentLayer_800c6678.soundOffset_04;
    voice.adsr.lo = instrumentLayer_800c6678.adsrLo_06;
    voice.adsr.hi = instrumentLayer_800c6678.adsrHi_08;
    setKeyOn(sequenceData, voiceIndex);

    if(sequenceData.reverbEnabled_0ea || (instrumentLayer_800c6678.flags_0f & 0x80) != 0) {
      soundEnv_800c6630.reverbMode_12 |= 1 << voiceIndex;
      //LAB_80046884
      //LAB_800468d8
    } else if((instrumentLayer_800c6678.flags_0f & 0x80) == 0) {
      soundEnv_800c6630.reverbMode_12 &= ~(1 << voiceIndex);
    }

    //LAB_80046914
    if((instrumentLayer_800c6678.flags_0f & 0x2) == 0) {
      //LAB_80046964
      soundEnv_800c6630.noiseMode_16 &= ~(1 << voiceIndex);
    } else {
      this.setNoiseMode(sequenceData, voiceIndex);
      SPU.setNoiseFrequency(instrumentLayer_800c6678.rootKey_02); //TODO this is weird... should be a packed value?
    }

    //LAB_800469ac
    instrumentLayerIndex_800c6678 -= instrumentIndex;
    instrumentLayer_800c6678 = instrumentLayers_800c6678[instrumentLayerIndex_800c6678];
    sequenceData.sssqReader_010.advance(4);

    //LAB_800469d4
  }

  @Method(0x80046a04L)
  public void sssqHandleKeyOn(final SequenceData124 sequenceData) {
    LOGGER.info(SEQUENCER_MARKER, "Key on channel %d, note %d, velocity %d", sequenceData.command_000 & 0xf, sequenceData.param0_002, sequenceData.param1_003);

    if(sequenceData.param1_003 == 0) { // Velocity
      this.sssqHandleKeyOff(sequenceData);
      return;
    }

    //LAB_80046a7c
    if(sssqChannelInfo_800C6680.volume_03 != 0) {
      final int count = instrument_800c6674.count_00;
      if(count == 0xff) {
        final int relativeNote = sequenceData.param0_002 - instrument_800c6674.startingKeyPosition_06;
        sequenceData.endingInstrument_026 = relativeNote;
        sequenceData.startingInstrument_01e = relativeNote;
        //LAB_80046acc
      } else if((count & 0x80) != 0) {
        sequenceData.endingInstrument_026 = count & ~0x80;
        sequenceData.startingInstrument_01e = 0;
        soundEnv_800c6630.onlyPlayFirstMatchingInstrument_0c = false;
      } else {
        //LAB_80046ae8
        sequenceData.endingInstrument_026 = count;
        sequenceData.startingInstrument_01e = 0;
        soundEnv_800c6630.onlyPlayFirstMatchingInstrument_0c = true;
      }

      //LAB_80046af8
      //LAB_80046b24
      for(int instrumentIndex = sequenceData.startingInstrument_01e; instrumentIndex < sequenceData.endingInstrument_026 + 1; instrumentIndex++) {
        if(this.instrumentCanPlayNote(instrument_800c6674.count_00, instrumentIndex, sequenceData.param0_002)) {
          final int voiceIndex = this.getFreeVoiceIndex();
          if(voiceIndex == -1) {
            break;
          }

          final PlayingNote66 playingNote = playingNotes_800c3a40[voiceIndex];

          instrumentLayerIndex_800c6678 += instrumentIndex;
          instrumentLayer_800c6678 = instrumentLayers_800c6678[instrumentLayerIndex_800c6678];
          if((instrumentLayer_800c6678.flags_0f & 0x1) != 0) {
            playingNote.finished_08 = false;
            playingNote._0c = 1;
          } else {
            //LAB_80046bb4
            playingNote.finished_08 = true;
            playingNote._0c = 0;
          }

          //LAB_80046bdc
          playingNote.used_00 = true;
          playingNote.noteNumber_02 = sequenceData.param0_002;
          playingNote.sequenceData_06 = sequenceData;
          playingNote.sequenceChannel_04 = sequenceData.command_000 & 0xf;
          playingNote._0a = soundEnv_800c6630._00;
          playingNote.instrumentIndex_0e = instrumentIndex;
          playingNote._12 = 0;
          playingNote.isPolyphonicKeyPressure_1a = false;
          playingNote._1c = 0;
          playingNote.maxKeyRange_1e = 0;
          playingNote.minKeyRange_20 = 0;
          playingNote.playableSound_22 = sequenceData.playableSound_020;
          playingNote.channelVolume_28 = sssqChannelInfo_800C6680.volume_0e;
          playingNote.instrumentVolume_2a = instrument_800c6674.patchVolume_01;
          playingNote.velocityVolume_2c = volumeRamp_800c4ab0.ramp_02[sequenceData.param1_003];
          playingNote.instrumentLayerVolume_2e = instrumentLayer_800c6678.volume_0b;
          playingNote.volumeLeftRight_30[0] = panVolume_80059f3c.get((this.calculatePan(0, 0) / 2 & 0x7ffe) / 2).left_00.get();
          playingNote.volumeLeftRight_30[1] = panVolume_80059f3c.get((this.calculatePan(0, 0) / 2 & 0x7ffe) / 2).right_01.get();
          playingNote.volume_34 = sssqChannelInfo_800C6680.volume_03;
          playingNote.cents_36 = instrumentLayer_800c6678.cents_03;
          playingNote.pitchBend_38 = sssqChannelInfo_800C6680.pitchBend_0a;
          playingNote.pitchBendMultiplier_3a = instrumentLayer_800c6678.pitchBendMultiplier_0d;
          playingNote.breath_3c = sssqChannelInfo_800C6680.breath_0c;
          playingNote.nextCommand_3e = sequenceData.param2_005;
          playingNote.rootKey_40 = instrumentLayer_800c6678.rootKey_02;
          playingNote.pitchShifted_42 = false;
          playingNote.portamentoChanging_44 = false;
          playingNote._4a = instrumentLayer_800c6678._0a;
          playingNote.pan_4c = sssqChannelInfo_800C6680.pan_04;
          playingNote.portamentoNote_4e = 120;

          if(sssqChannelInfo_800C6680._0b == 0x7f) {
            playingNote._18 = 1;
          }

          //LAB_80046d80
          if((instrumentLayer_800c6678.flags_0f & 0x20) != 0 && sssqChannelInfo_800C6680.modulation_09 != 0) {
            if((instrumentLayer_800c6678.flags_0f & 0x40) != 0) {
              playingNote.breathControlListIndex_10 = instrument_800c6674._05;
            } else {
              //LAB_80046dd0
              playingNote.breathControlListIndex_10 = instrumentLayer_800c6678._0e;
            }

            //LAB_80046ddc
            playingNote.modulationEnabled_14 = true;
            playingNote.modulation_16 = sssqChannelInfo_800C6680.modulation_09;
          } else {
            //LAB_80046e1c
            //LAB_80046e20
            playingNote.modulationEnabled_14 = false;
            playingNote.modulation_16 = 0;
          }

          //LAB_80046e4c
          final int pitchBendMultiplier;
          if((instrumentLayer_800c6678.flags_0f & 0x10) != 0) {
            pitchBendMultiplier = instrument_800c6674.pitchBendMultiplier_04;
          } else {
            //LAB_80046e7c
            pitchBendMultiplier = instrumentLayer_800c6678.pitchBendMultiplier_0d;
          }

          //LAB_80046e80
          //LAB_80046ea0
          SPU.voices[voiceIndex].pitch = this.calculateSampleRate(instrumentLayer_800c6678.rootKey_02, sequenceData.param0_002, instrumentLayer_800c6678.cents_03 * 4, sssqChannelInfo_800C6680.pitchBend_0a, pitchBendMultiplier);
          final int l = this.calculateVolume(sequenceData, this.calculatePan(0, 0), 0);
          final int r = this.calculateVolume(sequenceData, this.calculatePan(0, 0), 1);

          //LAB_80046f30
          final Voice voice = SPU.voices[voiceIndex];
          voice.volumeLeft.set(l);
          voice.volumeRight.set(r);
          voice.startAddress = sequenceData.playableSound_020.soundBufferPtr_08 + instrumentLayer_800c6678.soundOffset_04;
          voice.adsr.lo = instrumentLayer_800c6678.adsrLo_06;
          voice.adsr.hi = instrumentLayer_800c6678.adsrHi_08;
          setKeyOn(sequenceData, voiceIndex);

          if((instrumentLayer_800c6678.flags_0f & 0x80) != 0) {
            soundEnv_800c6630.reverbMode_12 |= 1 << voiceIndex;
          } else {
            //LAB_80046fe8
            soundEnv_800c6630.reverbMode_12 &= ~(1 << voiceIndex);
          }

          //LAB_80047024
          soundEnv_800c6630.noiseMode_16 &= ~(1 << voiceIndex);

          //LAB_8004706c
          if(soundEnv_800c6630._00 < 24) {
            soundEnv_800c6630._00++;
          }

          instrumentLayerIndex_800c6678 -= instrumentIndex;
          instrumentLayer_800c6678 = instrumentLayers_800c6678[instrumentLayerIndex_800c6678];

          if(soundEnv_800c6630.onlyPlayFirstMatchingInstrument_0c) {
            //LAB_80046ae0
            soundEnv_800c6630.onlyPlayFirstMatchingInstrument_0c = false;
            break;
          }
        }

        //LAB_8004709c
      }
    }

    //LAB_800470bc
    sequenceData.sssqReader_010.advance(3);

    //LAB_800470cc
  }

  @Method(0x800470fcL)
  public void sssqTickEffectsOverTime() {
    final SoundEnv44 soundEnv = soundEnv_800c6630;

    //LAB_80047144
    for(short voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      final Voice voice = SPU.voices[voiceIndex];
      final PlayingNote66 playingNote = playingNotes_800c3a40[voiceIndex];

      if(playingNote.used_00) {
        if(playingNote.sequenceData_06  != null) {
          final SequenceData124 sequenceData = playingNote.sequenceData_06;

          if(playingNote.modulationEnabled_14 || playingNote.portamentoChanging_44 || sequenceData._104) {
            //LAB_800471d0
            //LAB_800471d4
            int sixtyFourths = playingNote.cents_36 * 4;
            int note = playingNote.noteNumber_02;
            int rootKey = playingNote.rootKey_40;
            int pitchBend = playingNote.pitchBend_38;
            int pitchBendMultiplier = playingNote.pitchBendMultiplier_3a;
            if(playingNote.modulationEnabled_14 || playingNote.portamentoChanging_44) {
              //LAB_80047220
              if(note >= rootKey) { //TODO I'm pretty sure these branches are equivalent?
                rootKey = 120 - (note - rootKey);
              } else {
                //LAB_80047244
                rootKey = 120 + rootKey - note;
              }

              //LAB_80047248
              if(playingNote.modulationEnabled_14) {
                if(soundEnv.ticksPerSecond_42 != 60 || (playingNote.breath_3c & 0xfff) != 120) {
                  //LAB_800472cc
                  //LAB_800472d0
                  playingNote._12 += playingNote.breath_3c & 0xfff;
                } else {
                  final int v0_0 = playingNote.breath_3c & 0xf000;
                  if(v0_0 != 0) {
                    playingNote.breath_3c = playingNote.breath_3c & 0xfff | v0_0 - 0x1000;
                    playingNote._12 += playingNote.breath_3c & 0xfff;
                  } else {
                    //LAB_800472c0
                    playingNote.breath_3c |= 0x6000;
                  }
                }

                //LAB_80047300
                final Sshd sshd = sequenceData.playableSound_020.sshdPtr_04;

                pitchBend = 0x80;
                sshdPtr_800c4ac0 = sshd;

                if(sshd.hasSubfile(2)) {
                  waveforms_800c4ab8 = sshd.getSubfile(2, WaveformList::new);

                  if(playingNote._12 >= 0xf0) {
                    playingNote._12 = (playingNote.breath_3c & 0xfff) >>> 1;
                  }

                  //LAB_800473a0
                  pitchBend = waveforms_800c4ab8.waveforms_02[playingNote.breathControlListIndex_10]._00[playingNote._12 >>> 2];
                }

                //LAB_800473d4
                //LAB_800473d8
                note = playingNote.portamentoNote_4e;
                if(playingNote._1c == 0) {
                  final int _64ths = (playingNote.pitchBend_38 - 64) * playingNote.pitchBendMultiplier_3a; // 64ths of notes
                  note = note + _64ths / 64; // Add whole number of notes
                  sixtyFourths = sixtyFourths + Math.floorMod(_64ths, 64);
                  pitchBendMultiplier = 1;
                }

                //LAB_80047498
                pitchBend = pitchBend * playingNote.modulation_16 / 255 - ((playingNote.modulation_16 + 1) / 2 - 64);
              }

              //LAB_800474f0
              if(playingNote.portamentoChanging_44) {
                if(playingNote.portamentoTimeRemaining_62 != 0) {
                  playingNote.portamentoTimeRemaining_62--;

                  if(playingNote.newPortamento_60 < 0) {
                    final int portamentoTimeElapsed = playingNote.portamentoTimeTotal_64 - playingNote.portamentoTimeRemaining_62;
                    note = playingNote.portamentoNote_4e - portamentoTimeElapsed * (256 - playingNote.newPortamento_60) / 10 / playingNote.portamentoTimeTotal_64;
                    //TODO remove the *4 by increasing resolution
                    sixtyFourths = sixtyFourths - (portamentoTimeElapsed * playingNote.newPortamento_60 * 192 / (playingNote.portamentoTimeTotal_64 * 120) % 16) * 4;
                  } else {
                    //LAB_8004762c
                    final int portamentoTimeElapsed = (playingNote.portamentoTimeTotal_64 - playingNote.portamentoTimeRemaining_62) * playingNote.newPortamento_60;
                    note = playingNote.portamentoNote_4e + portamentoTimeElapsed / 10 / playingNote.portamentoTimeTotal_64;
                    //TODO remove the *4 by increasing resolution
                    sixtyFourths = sixtyFourths + (portamentoTimeElapsed * 192 / (playingNote.portamentoTimeTotal_64 * 120) % 16) * 4;
                  }

                  //LAB_800476f4
                  if(note <= 0xc) {
                    note = 0xc;
                  }

                  //LAB_8004770c
                  if(note >= 0xf3) {
                    note = 0xf3;
                  }

                  //LAB_8004771c
                  sequenceData.portamentoNote_11c = note;

                  if(playingNote.portamentoTimeRemaining_62 == 0) {
                    playingNote.portamentoNote_4e = note;
                    playingNote.portamentoChanging_44 = false;
                  }
                }
              }
            }

            //LAB_80047754
            //LAB_80047758
            final int pitch;
            if(playingNote.pitchShifted_42 || sequenceData._104) {
              //LAB_80047794
              pitch = sequenceData.pitch_0ec;
            } else {
              pitch = 0x1000;
            }

            //LAB_800477a0
            //LAB_800477a4
            voice.pitch = pitch * this.calculateSampleRate(rootKey, note, sixtyFourths, pitchBend, pitchBendMultiplier) >> 12;
          }

          //LAB_800477ec
          if(playingNote.isPolyphonicKeyPressure_1a) {
            if(playingNote.volumeChanging_46 || playingNote.panChanging_48 || sequenceData._105) {
              //LAB_80047844
              //LAB_80047848
              if(playingNote.volumeChanging_46) {
                final int newVolume = playingNote.newVolume_50;

                if(newVolume == playingNote.previousVolume_52) {
                  playingNote.volumeChanging_46 = false;
                } else {
                  if(playingNote.remainingVolumeChangeTime_54 != 0) {
                    playingNote.velocityVolume_2c = this.interpolate(playingNote.newVolume_50, playingNote.previousVolume_52, playingNote.totalVolumeChangeTime_56, playingNote.remainingVolumeChangeTime_54);
                    playingNote.remainingVolumeChangeTime_54--;
                  } else {
                    //LAB_800478c8
                    playingNote.velocityVolume_2c = newVolume;
                    playingNote.volumeChanging_46 = false;
                  }
                }
              }

              //LAB_800478d0
              //LAB_800478d4
              if(playingNote.panChanging_48) {
                final int newPan = playingNote.newPan_58;

                if(newPan == playingNote.previousPan_5a) {
                  playingNote.panChanging_48 = false;
                } else {
                  //LAB_8004791c
                  if(playingNote.remainingPanTime_5c != 0) {
                    playingNote.pan_4c = this.interpolate(playingNote.newPan_58, playingNote.previousPan_5a, playingNote.totalPanTime_5e, playingNote.remainingPanTime_5c);
                    playingNote.remainingPanTime_5c--;
                  } else {
                    //LAB_8004795c
                    playingNote.pan_4c = newPan;
                    playingNote.panChanging_48 = false;
                  }

                  //LAB_80047964
                  playingNote.volumeLeftRight_30[0] = panVolume_80059f3c.get(playingNote.pan_4c >>> 2).left_00.get();
                  playingNote.volumeLeftRight_30[1] = panVolume_80059f3c.get(playingNote.pan_4c >>> 2).right_01.get();
                }
              }

              //LAB_800479c4
              int l = calculateNoteVolume(voiceIndex, 0);
              int r = calculateNoteVolume(voiceIndex, 1);
              if(playingNote.pitchShifted_42 || sequenceData._105) {
                //LAB_80047a24
                l = this.scaleValue12((short)l, (short)sequenceData.pitchShiftVolLeft_0ee);
                r = this.scaleValue12((short)r, (short)sequenceData.pitchShiftVolRight_0f0);
              }

              //LAB_80047a44
              //LAB_80047a6c
              voice.volumeLeft.set(l);
              voice.volumeRight.set(r);
            }
          }

          //LAB_80047a88
          //LAB_80047a90
        }
      }

      //LAB_80047acc
    }
  }

  @Method(0x80047b38L)
  public void setActiveSequence(final SequenceData124 sequenceData) {
    final Sshd sshd = sequenceData.playableSound_020.sshdPtr_04;

    soundEnv_800c6630.sshdPtr_08 = sshd;
    sshdPtr_800c4ac0 = sshd;
    volumeRamp_800c4ab0 = sshd.getSubfile(1, VolumeRamp::new);

    final Sshd.Subfile subfile = sshd.getSubfile(sequenceData.soundPlaying_02a ? 4 : 0);

    if(subfile == null || subfile instanceof InstrumentsSubfile) {
      instruments_800c4aa8 = sshd.getSubfile(sequenceData.soundPlaying_02a ? 4 : 0, InstrumentsSubfile::new);
      sssqish_800c4aa8 = null;
    } else {
      instruments_800c4aa8 = null;
      sssqish_800c4aa8 = (Sssqish)sshd.getSubfile(sequenceData.soundPlaying_02a ? 4 : 0);
    }
  }

  @Method(0x80047bd0L)
  public boolean setActiveSequence(final int channelIndex) {
    final SequenceData124 sequenceData = sequenceData_800c4ac8[channelIndex];

    if(!sequenceData.musicPlaying_028 && sequenceData.soundPlaying_02a && sshdPtr_800c4ac0.hasSubfile(4)) {
      final Sssqish sssq = soundEnv_800c6630.sshdPtr_08.getSubfile(4, (data, offset) -> new Sssqish(data, offset, soundEnv_800c6630.sshdPtr_08.getSubfileSize(4)));
      final int data = sequenceData_800c4ac8[channelIndex].sssqReader_010.readByte(3);

      if(data < sssqish_800c4aa8.entries2_190.entries_02.length) {
        instrument_800c6674 = sssqish_800c4aa8.entries2_190.entries_02[data];
        instrumentLayers_800c6678 = instrument_800c6674.layers_08;
        instrumentLayerIndex_800c6678 = 0;
        instrumentLayer_800c6678 = instrumentLayers_800c6678[instrumentLayerIndex_800c6678];
      } else {
        instrument_800c6674 = null;
        instrumentLayers_800c6678 = null;
        instrumentLayerIndex_800c6678 = 0;
        instrumentLayer_800c6678 = null;
      }

      sssqReader_800c667c = null; //sssq.reader(); TODO?
      sssqChannelInfo_800C6680 = sssq.entries_10[channelIndex];
      return true;
    }

    //LAB_80047cd0
    if(!sequenceData.musicPlaying_028 || sequenceData.soundPlaying_02a) {
      //LAB_80047cf0
      return false;
    }

    //LAB_80047cf8
    final int command = sequenceData.command_000;
    final int channel = command & 0xf;
    final int instrumentIndex = sequenceData.sssqReader_010.channelInfo(channel).instrumentIndex_02;

    if(command < 0xa0) {
      if(instrumentIndex < 0) {
        return false;
      }

      if(instrumentIndex > instruments_800c4aa8.count_00) {
        return false;
      }

      if(instruments_800c4aa8.instruments_02[instrumentIndex] == null) {
        return false;
      }

      if(!sshdPtr_800c4ac0.hasSubfile(0)) {
        return false;
      }
    }

    //LAB_80047d7c
    //LAB_80047d80
    if(instrumentIndex != -1) {
      instrument_800c6674 = instruments_800c4aa8.instruments_02[instrumentIndex];
      instrumentLayerIndex_800c6678 = 0;

      if(instruments_800c4aa8.instruments_02[instrumentIndex] != null) { // Not 100% sure this is okay, but it fixes Kazas BGM crashing the sound thread and doesn't seem to cause any other problems
        instrumentLayers_800c6678 = instruments_800c4aa8.instruments_02[instrumentIndex].layers_08;
        instrumentLayer_800c6678 = instrumentLayers_800c6678[instrumentLayerIndex_800c6678];
      }
    }

    sssqReader_800c667c = sequenceData.sssqReader_010;
    sssqChannelInfo_800C6680 = sequenceData.sssqReader_010.channelInfo(channel);

    //LAB_80047e14
    return true;
  }

  @Method(0x80047e1cL)
  public int getFreeVoiceIndex() {
    //LAB_80047e34
    for(int i = 0; i < 24; i++) {
      //LAB_80047e4c
      if(soundEnv_800c6630.voiceIndex_10 < 23) {
        soundEnv_800c6630.voiceIndex_10++;
      } else {
        soundEnv_800c6630.voiceIndex_10 = 0;
      }

      if(!playingNotes_800c3a40[soundEnv_800c6630.voiceIndex_10].used_00) {
        return soundEnv_800c6630.voiceIndex_10;
      }
    }

    //LAB_80047ea0
    int a1 = 24;
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      if(!playingNotes_800c3a40[voiceIndex].isPolyphonicKeyPressure_1a && playingNotes_800c3a40[voiceIndex].finished_08) {
        final int v1 = playingNotes_800c3a40[voiceIndex]._0a;

        if(v1 < a1) {
          a1 = v1;
          soundEnv_800c6630.voiceIndex_10 = voiceIndex;
        }
      }

      //LAB_80047ef4
    }

    if(a1 == 24) {
      //LAB_80047f28
      for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
        if(!playingNotes_800c3a40[voiceIndex].isPolyphonicKeyPressure_1a) {
          final int v1 = playingNotes_800c3a40[voiceIndex]._0a;

          if(v1 < 24) {
            //LAB_80047f84
            a1 = v1;
            soundEnv_800c6630.voiceIndex_10 = voiceIndex;
            break;
          }
        }

        //LAB_80047f64
      }
    }

    if(a1 == 24) {
      soundEnv_800c6630.voiceIndex_10 = -1;
      return -1;
    }

    //LAB_80047f90
    //LAB_80047fa0
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      if(a1 < playingNotes_800c3a40[voiceIndex]._0a) {
        playingNotes_800c3a40[voiceIndex]._0a--;
      }

      //LAB_80047fd0
    }

    soundEnv_800c6630._00--;

    //LAB_80047ff4
    return soundEnv_800c6630.voiceIndex_10;
  }

  /** This code has been verified */
  @Method(0x80048000L)
  public int findMatchingOrFreeNote(final int minKeyRange, final int maxKeyRange, final PlayableSound0c playableSound) {
    if(minKeyRange != 0) {
      //LAB_8004802c
      for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
        if(playingNotes_800c3a40[voiceIndex].isPolyphonicKeyPressure_1a && playingNotes_800c3a40[voiceIndex].minKeyRange_20 == minKeyRange && playingNotes_800c3a40[voiceIndex].playableSound_22 == playableSound) {
          //LAB_80048080
          for(int voiceIndex2 = 0; voiceIndex2 < 24; voiceIndex2++) {
            final int v1 = playingNotes_800c3a40[voiceIndex]._0a;

            if(v1 < playingNotes_800c3a40[voiceIndex2]._0a && v1 != 0x40) {
              playingNotes_800c3a40[voiceIndex2]._0a--;
            }
          }

          soundEnv_800c6630._00--;
          return voiceIndex;
        }
      }
    }

    //LAB_80048108
    if(soundEnv_800c6630._0d >= soundEnv_800c6630.playingSoundsUpperBound_03) {
      //LAB_80048134
      int t2 = 0;
      for(int i = 0; i < 24; i++) {
        int t1 = 24;

        //LAB_80048144
        for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
          if(playingNotes_800c3a40[voiceIndex].isPolyphonicKeyPressure_1a) {
            final int v1 = playingNotes_800c3a40[voiceIndex]._0a;

            if(v1 >= i && v1 < t1) {
              t1 = v1;
              t2 = voiceIndex;
            }
          }
        }

        if(playingNotes_800c3a40[t2].maxKeyRange_1e <= maxKeyRange) {
          //LAB_800481fc
          for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
            final int v1 = playingNotes_800c3a40[voiceIndex]._0a;

            if(v1 != -1 && t1 < v1) {
              playingNotes_800c3a40[voiceIndex]._0a--;
            }
          }

          //LAB_80048260
          soundEnv_800c6630._00--;
          return t2;
        }
      }

      return -1;
    }

    //LAB_800482a0
    //LAB_800482a8
    for(int i = 0; i < 24; i++) {
      if(soundEnv_800c6630.voiceIndex_10 < 23) {
        soundEnv_800c6630.voiceIndex_10++;
      } else {
        soundEnv_800c6630.voiceIndex_10 = 0;
      }

      //LAB_800482c0
      if(!playingNotes_800c3a40[soundEnv_800c6630.voiceIndex_10].used_00) {
        //LAB_8004828c
        soundEnv_800c6630._0d++;
        return soundEnv_800c6630.voiceIndex_10;
      }
    }

    int t3 = -1;
    int t1 = 24;

    //LAB_80048320
    jmp_80048478:
    {
      for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
        if(playingNotes_800c3a40[voiceIndex].finished_08 && !playingNotes_800c3a40[voiceIndex].isPolyphonicKeyPressure_1a) {
          //LAB_8004836c
          for(int voiceIndex2 = voiceIndex; voiceIndex2 < 24; voiceIndex2++) {
            if(playingNotes_800c3a40[voiceIndex2].finished_08 && !playingNotes_800c3a40[voiceIndex2].isPolyphonicKeyPressure_1a) {
              final int v1 = playingNotes_800c3a40[voiceIndex2]._0a;

              if(v1 < t1) {
                t1 = v1;
                t3 = voiceIndex2;
              }
            }
          }

          break jmp_80048478;
        }
      }

      //LAB_80048414
      for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
        if(!playingNotes_800c3a40[voiceIndex].isPolyphonicKeyPressure_1a) {
          final int v1 = playingNotes_800c3a40[voiceIndex]._0a;

          if(v1 < t1) {
            t1 = v1;
            t3 = voiceIndex;
          }
        }
      }
    }

    //LAB_80048478
    //LAB_8004847c
    //LAB_80048494
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      if(playingNotes_800c3a40[voiceIndex]._0a != -1 && playingNotes_800c3a40[voiceIndex]._0a > t1) {
        playingNotes_800c3a40[voiceIndex]._0a--;
      }
    }

    soundEnv_800c6630._0d++;
    soundEnv_800c6630._00--;

    //LAB_80048508
    return (short)t3;
  }

  @Method(0x80048514L)
  public void keyOffMatchingNotes(final SequenceData124 sequenceData) {
    int matchingNotesThisSequence = 0;
    int matchingNotesOtherSequence = 0;

    //LAB_8004857c
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      if(playingNotes_800c3a40[voiceIndex].used_00) {
        if(playingNotes_800c3a40[voiceIndex].isPolyphonicKeyPressure_1a) {
          if(playingNotes_800c3a40[voiceIndex].playableSound_22 == sequenceData.playableSound_020) {
            if(playingNotes_800c3a40[voiceIndex].nextCommand_3e == sequenceData.param2_005) {
              if(playingNotes_800c3a40[voiceIndex].noteNumber_02 == sequenceData.param0_002) {
                if(playingNotes_800c3a40[voiceIndex]._0c == 1) {
                  if(playingNotes_800c3a40[voiceIndex].sequenceData_06 == sequenceData) {
                    matchingNotesThisSequence |= 0x1 << voiceIndex;
                  } else {
                    //LAB_8004861c
                    matchingNotesOtherSequence |= 0x1 << voiceIndex;
                  }
                }
              }
            }
          }
        }
      }

      //LAB_80048620
    }

    final int matchingNotes = matchingNotesThisSequence != 0 ? matchingNotesThisSequence : matchingNotesOtherSequence;

    //LAB_80048640
    //LAB_80048650
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      if((matchingNotes & 0x1 << voiceIndex) != 0) {
        setKeyOff(sequenceData, voiceIndex);
        playingNotes_800c3a40[voiceIndex].finished_08 = true;
      }

      //LAB_80048684
    }

    sequenceData.sssqReader_010.advance(4);
  }

  @Method(0x800486d4L)
  public void sssqHandleKeyOff(final SequenceData124 sequenceData) {
    LOGGER.info(SEQUENCER_MARKER, "Key off channel %d, note %d", sequenceData.command_000 & 0xf, sequenceData.param0_002);

    //LAB_80048724
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      final PlayingNote66 playingNote = playingNotes_800c3a40[voiceIndex];
      if(playingNote.used_00) {
        if(!playingNote.isPolyphonicKeyPressure_1a) {
          if(playingNote.sequenceData_06 == sequenceData) {
            if(playingNote.playableSound_22 == sequenceData.playableSound_020) {
              if(playingNote.sequenceChannel_04 == (sequenceData.command_000 & 0xf)) {
                if(playingNote.noteNumber_02 == sequenceData.param0_002) {
                  if(playingNote._0c == 0) {
                    //LAB_800487d0
                    //LAB_800487d4
                    playingNote.finished_08 = true;
                    playingNote._18 = 0;
                  } else if(playingNote._18 == 0) {
                    playingNote.finished_08 = true;
                  } else {
                    //LAB_800487d4
                    playingNote._18 = 0;
                  }

                  //LAB_800487d8
                  setKeyOff(sequenceData, voiceIndex);
                }
              }
            }
          }
        }
      }

      //LAB_800487e4
    }

    sequenceData.sssqReader_010.advance(3);
  }

  @Method(0x8004888cL)
  public void setNoiseMode(final SequenceData124 sequenceData, final int voiceIndex) {
    soundEnv_800c6630.noiseMode_16 |= 1 << voiceIndex;
  }

  /** Checks if the note falls within the instrument's key range */
  @Method(0x80048938L)
  public boolean instrumentCanPlayNote(final int a0, final int instrumentIndex, final int note) {
    if(a0 == 0xff) {
      return true;
    }

    //LAB_80048950
    instrumentLayerIndex_800c6678 += instrumentIndex;
    instrumentLayer_800c6678 = instrumentLayers_800c6678[instrumentLayerIndex_800c6678];

    final boolean ret = note >= instrumentLayer_800c6678.minKeyRange_00 && note <= instrumentLayer_800c6678.maxKeyRange_01;

    //LAB_80048988
    instrumentLayerIndex_800c6678 -= instrumentIndex;
    instrumentLayer_800c6678 = instrumentLayers_800c6678[instrumentLayerIndex_800c6678];

    //LAB_80048990
    return ret;
  }

  /**
   * @param note 0-127, numeric representation of musical note, e.g. 60 = middle C
   */
  @Method(0x80048998L)
  public int calculateSampleRate(final int rootKey, final int note, final int sixtyFourths, final int pitchBend, final int pitchBendMultiplier) {
    final int offsetIn64ths = (note - rootKey) * 64 + sixtyFourths + (pitchBend - 64) * pitchBendMultiplier;


    if(offsetIn64ths >= 0) {
      final int octaveOffset = offsetIn64ths / 768;
      final int i = offsetIn64ths - octaveOffset * 768;
      final int sampleRate = Spu.sampleRates[i] << octaveOffset;
      return sampleRate;
    }

    final int octaveOffset = (offsetIn64ths + 1) / -768 + 1;
    final int i = offsetIn64ths + octaveOffset * 768;
    final int sampleRate = Spu.sampleRates[i] >> octaveOffset;
    return sampleRate;

  }

  @Method(0x80048ab8L)
  public int calculateVolume(final SequenceData124 sequenceData, final int pan, final int leftRight) {
    final int volume = sssqChannelInfo_800C6680.volume_0e
      * instrument_800c6674.patchVolume_01
      * volumeRamp_800c4ab0.ramp_02[sequenceData.param1_003]
      * instrumentLayer_800c6678.volume_0b
      / 0x4000
      * panVolume_80059f3c.get((pan / 2 & 0x7ffe) / 2).val_00.get(leftRight).get()
      / 0x80;

    //LAB_80048b88
    return instrumentLayer_800c6678._0a == 0 ? volume : instrumentLayer_800c6678._0a << 8 | volume >> 7;
  }

  @Method(0x80048b90L)
  public int calculatePan(final int a0, final int instrumentIndex) {
    instrumentLayerIndex_800c6678 += instrumentIndex;
    instrumentLayer_800c6678 = instrumentLayers_800c6678[instrumentLayerIndex_800c6678];

    final int pan;
    if(a0 == 4) {
      pan = instrumentLayer_800c6678.pan_0c;
    } else {
      //LAB_80048bc4
      pan = (int)_80059b3c.offset(_80059b3c.offset(instrumentLayer_800c6678.pan_0c / 4 + instrument_800c6674.pan_02 / 4 * 0x20L).get() / 4 + sssqChannelInfo_800C6680.pan_04 / 4 * 0x20L).get();
    }

    //LAB_80048c1c
    instrumentLayerIndex_800c6678 -= instrumentIndex;
    instrumentLayer_800c6678 = instrumentLayers_800c6678[instrumentLayerIndex_800c6678];

    return pan;
  }

  @Method(0x80048eb8L)
  public void sssqHandleEndOfTrack(final SequenceData124 sequenceData) {
    if(sequenceData.soundPlaying_02a) {
      sequenceData.soundEnded_0e7 = true;
      sequenceData.deltaTime_118 = 0;
      sequenceData.soundPlaying_02a = false;
      sequenceData._104 = false;
      sequenceData._105 = false;
      sequenceData.pitchShifted_0e9 = false;
      sequenceData.reverbEnabled_0ea = false;
    } else {
      //LAB_80048f0c
      sequenceData.sssqReader_010.jump(0x110);
      sequenceData.musicPlaying_028 = false;
    }

    //LAB_80048f18
    //LAB_80048f30
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      final PlayingNote66 playingNote = playingNotes_800c3a40[voiceIndex];

      if(playingNote.sequenceData_06 == sequenceData) {
        if(!playingNote.isPolyphonicKeyPressure_1a) {
          playingNote.modulationEnabled_14 = false;
          playingNote.pitchBend_38 = 64;
        }
      }

      //LAB_80048f74
    }

    sequenceData.previousCommand_001 = sequenceData.command_000;
  }

  @Method(0x80048f98L)
  public void sssqHandleTempo(final SequenceData124 sequenceData) {
    sequenceData.tempo_108 = sequenceData.sssqReader_010.readShort(2);
    sequenceData.sssqReader_010.advance(4);

    LOGGER.info(SEQUENCER_MARKER, "Tempo %d", sequenceData.tempo_108);
  }

  @Method(0x80048fecL)
  public void sssqHandleProgramChange(final SequenceData124 sequenceData) {
    if(!sequenceData.soundPlaying_02a) {
      sssqChannelInfo_800C6680.instrumentIndex_02 = sequenceData.sssqReader_010.readByte(1); // Read instrument index
      sssqChannelInfo_800C6680.pitchBend_0a = 0x40;
      sssqChannelInfo_800C6680._0b = 0x40;

      LOGGER.info(SEQUENCER_MARKER, "Program change program %d", sssqChannelInfo_800C6680.instrumentIndex_02);
    }

    //LAB_80049058
    sequenceData.sssqReader_010.advance(2);
  }

  @Method(0x8004906cL)
  public void sssqHandleModulationWheel(final SequenceData124 sequenceData) {
    if(sequenceData.soundPlaying_02a) {
      LOGGER.info(SEQUENCER_MARKER, "Mod wheel (sound) mod %d", sequenceData.sssqReader_010.readByte(2));

      //LAB_800490b8
      for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
        final PlayingNote66 playingNote = playingNotes_800c3a40[voiceIndex];

        if(playingNote.isPolyphonicKeyPressure_1a) {
          if(playingNote.nextCommand_3e == sequenceData.sssqReader_010.readByte(3)) {
            if(playingNote.noteNumber_02 == sequenceData.sssqReader_010.readByte(4)) {
              if(playingNote.playableSound_22 == sequenceData.playableSound_020) {
                if(playingNote.sequenceData_06 == sequenceData) {
                  if(playingNote.used_00) {
                    playingNote.modulationEnabled_14 = true;
                    playingNote.modulation_16 = sequenceData.sssqReader_010.readByte(2);
                  }
                }
              }
            }
          }
        }

        //LAB_80049150
      }

      sequenceData.sssqReader_010.advance(5);
      return;
    }

    LOGGER.info(SEQUENCER_MARKER, "Mod wheel (music) channel %d, mod %d", sequenceData.command_000 & 0xf, sequenceData.sssqReader_010.readByte(2));

    //LAB_80049178
    sssqChannelInfo_800C6680.modulation_09 = sequenceData.sssqReader_010.readByte(2);

    //LAB_800491b0
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      final PlayingNote66 playingNote = playingNotes_800c3a40[voiceIndex];
      if(playingNote.sequenceChannel_04 == (sequenceData.command_000 & 0xf)) {
        if(playingNote.playableSound_22 == sequenceData.playableSound_020) {
          if(playingNote.sequenceData_06 == sequenceData) {
            if(playingNote.used_00) {
              playingNote.modulationEnabled_14 = true;
              playingNote.modulation_16 = sequenceData.sssqReader_010.readByte(2);
            }
          }
        }
      }

      //LAB_80049228
    }

    sequenceData.sssqReader_010.advance(3);
  }

  @Method(0x80049250L)
  public void sssqHandleBreathControl(final SequenceData124 sequenceData) {
    final int breath = 240 / (60 - sequenceData.sssqReader_010.readByte(2) * 58 / 127);

    //LAB_800492dc
    //LAB_800492f4

    if(sequenceData.soundPlaying_02a) {
      LOGGER.info(SEQUENCER_MARKER, "Breath control (sound) breath %d", breath);

      //LAB_80049318
      for(int i = 0; i < 24; i++) {
        final PlayingNote66 playingNote = playingNotes_800c3a40[i];

        if(playingNote.sequenceChannel_04 == (sequenceData.command_000 & 0xf)) {
          if(playingNote.nextCommand_3e == sequenceData.sssqReader_010.readByte(3)) {
            if(playingNote.noteNumber_02 == sequenceData.sssqReader_010.readByte(4)) {
              if(playingNote.playableSound_22 == sequenceData.playableSound_020) {
                if(playingNote.sequenceData_06 == sequenceData) {
                  if(playingNote.used_00) {
                    playingNote.breath_3c = breath;
                  }
                }
              }
            }
          }
        }

        //LAB_800493ac
      }

      sequenceData.sssqReader_010.advance(5);
      return;
    }

    LOGGER.info(SEQUENCER_MARKER, "Breath control (music) channel %d, breath %d", sequenceData.command_000 & 0xf, breath);

    //LAB_800493d4
    sssqChannelInfo_800C6680.breath_0c = breath;

    //LAB_800493f4
    for(int i = 0; i < 24; i++) {
      final PlayingNote66 playingNote = playingNotes_800c3a40[i];

      if(playingNote.sequenceChannel_04 == (sequenceData.command_000 & 0xf)) {
        if(playingNote.playableSound_22 == sequenceData.playableSound_020) {
          if(playingNote.sequenceData_06 == sequenceData) {
            if(playingNote.used_00) {
              playingNote.breath_3c = breath;
            }
          }
        }
      }

      //LAB_80049458
    }

    sequenceData.sssqReader_010.advance(3);
  }

  @Method(0x80049480L)
  public void sssqHandlePortamento(final SequenceData124 sequenceData) {
    LOGGER.info(SEQUENCER_MARKER, "Portamento (sound) portamento %d, time %d", sequenceData.sssqReader_010.readByte(3), sequenceData.sssqReader_010.readByte(2));

    final SoundEnv44 soundEnv = soundEnv_800c6630;

    //LAB_800494d0
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      final PlayingNote66 playingNote = playingNotes_800c3a40[voiceIndex];

      if(playingNote.used_00) {
        if(playingNote.isPolyphonicKeyPressure_1a) {
          if(playingNote.playableSound_22 == sequenceData.playableSound_020) {
            if(playingNote.nextCommand_3e == sequenceData.sssqReader_010.readByte(4)) {
              if(playingNote.noteNumber_02 == sequenceData.sssqReader_010.readByte(5)) {
                if(playingNote.sequenceData_06 == sequenceData) {
                  if(playingNote.portamentoTimeRemaining_62 != 0) {
                    playingNote.portamentoNote_4e = sequenceData.portamentoNote_11c;
                  }

                  //LAB_80049578
                  playingNote.portamentoChanging_44 = true;
                  playingNote.newPortamento_60 = (byte)sequenceData.sssqReader_010.readByte(3);
                  playingNote.portamentoTimeTotal_64 = sequenceData.sssqReader_010.readByte(2) * 4 * soundEnv.ticksPerSecond_42 / 60;
                  playingNote.portamentoTimeRemaining_62 = playingNote.portamentoTimeTotal_64;
                }
              }
            }
          }
        }
      }
    }

    sequenceData.sssqReader_010.advance(6);
  }

  @Method(0x80049638L)
  public void sssqHandleVolume(final SequenceData124 sequenceData) {
    final SoundEnv44 soundEnv = soundEnv_800c6630;

    if(sequenceData.soundPlaying_02a) {
      LOGGER.info(SEQUENCER_MARKER, "Volume (sound) vol %d time %d", sequenceData.sssqReader_010.readByte(3), sequenceData.sssqReader_010.readByte(2));

      //LAB_800496bc
      for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
        final PlayingNote66 playingNote = playingNotes_800c3a40[voiceIndex];

        if(playingNote.used_00) {
          if(playingNote.isPolyphonicKeyPressure_1a) {
            if(playingNote.playableSound_22 == sequenceData.playableSound_020) {
              if(playingNote.nextCommand_3e == sequenceData.sssqReader_010.readByte(4)) {
                if(playingNote.noteNumber_02 == sequenceData.sssqReader_010.readByte(5)) {
                  if(playingNote.sequenceData_06 == sequenceData) {
                    playingNote.volumeChanging_46 = true;
                    playingNote.previousVolume_52 = playingNote.velocityVolume_2c;
                    playingNote.newVolume_50 = sequenceData.sssqReader_010.readByte(3);
                    playingNote.totalVolumeChangeTime_56 = sequenceData.sssqReader_010.readByte(2) * 4 * soundEnv.ticksPerSecond_42 / 60;
                    playingNote.remainingVolumeChangeTime_54 = playingNote.totalVolumeChangeTime_56;
                  }
                }
              }
            }
          }
        }

        //LAB_800497dc
      }

      //LAB_80049950
      sequenceData.sssqReader_010.advance(6);
    } else {
      LOGGER.info(SEQUENCER_MARKER, "Volume (music) channel %d, vol %d", sequenceData.command_000 & 0xf, sequenceData.sssqReader_010.readByte(2));

      //LAB_800497fc
      sssqChannelInfo_800C6680.volume_03 = sequenceData.sssqReader_010.readByte(2);
      sssqChannelInfo_800C6680.volume_0e = sssqReader_800c667c.baseVolume() * sssqChannelInfo_800C6680.volume_03 / 0x80;

      //LAB_8004985c
      for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
        final PlayingNote66 playingNote = playingNotes_800c3a40[voiceIndex];

        if(playingNote.used_00) {
          if(playingNote.sequenceChannel_04 == (sequenceData.command_000 & 0xf)) {
            if(playingNote.playableSound_22 == sequenceData.playableSound_020) {
              if(!playingNote.finished_08) {
                if(playingNote.sequenceData_06 == sequenceData) {
                  sequenceData.param1_003 = playingNote.velocityVolume_2c;
                  SPU.voices[voiceIndex].volumeLeft.set(this.calculateVolume(playingNote.sequenceData_06, this.calculatePan(0, playingNote.instrumentIndex_0e), 0));
                  SPU.voices[voiceIndex].volumeRight.set(this.calculateVolume(playingNote.sequenceData_06, this.calculatePan(0, playingNote.instrumentIndex_0e), 1));
                }
              }
            }
          }
        }

        //LAB_80049930
      }

      //LAB_80049950
      sequenceData.sssqReader_010.advance(3);
    }
  }

  @Method(0x80049980L)
  public void sssqHandlePan(final SequenceData124 sequenceData) {
    if(sequenceData.soundPlaying_02a) {
      LOGGER.info(SEQUENCER_MARKER, "Pan (sound) pan %d, time %d", sequenceData.sssqReader_010.readByte(3), sequenceData.sssqReader_010.readByte(2));

      //LAB_80049a08
      for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
        final PlayingNote66 playingNote = playingNotes_800c3a40[voiceIndex];
        if(playingNote.used_00) {
          if(playingNote.isPolyphonicKeyPressure_1a) {
            if(playingNote.playableSound_22 == sequenceData.playableSound_020) {
              if(playingNote.nextCommand_3e == sequenceData.sssqReader_010.readByte(4)) {
                if(playingNote.noteNumber_02 == sequenceData.sssqReader_010.readByte(5)) {
                  if(playingNote.sequenceData_06 == sequenceData) {
                    playingNote.panChanging_48 = true;
                    playingNote.previousPan_5a = playingNote.pan_4c;
                    playingNote.newPan_58 = sequenceData.sssqReader_010.readByte(3);
                    playingNote.totalPanTime_5e = sequenceData.sssqReader_010.readByte(2) * 4 * soundEnv_800c6630.ticksPerSecond_42 / 60;
                    playingNote.remainingPanTime_5c = playingNote.totalPanTime_5e;
                  }
                }
              }
            }
          }
        }

        //LAB_80049b28
      }

      //LAB_80049c88
      sequenceData.sssqReader_010.advance(6);
    } else {
      LOGGER.info(SEQUENCER_MARKER, "Pan (music) channel %d, pan %d", sequenceData.command_000 & 0xf, sequenceData.sssqReader_010.readByte(2));

      //LAB_80049b48
      sssqChannelInfo_800C6680.pan_04 = sequenceData.sssqReader_010.readByte(2);

      //LAB_80049b80
      for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
        final PlayingNote66 playingNote = playingNotes_800c3a40[voiceIndex];
        if(playingNote.sequenceChannel_04 == (sequenceData.command_000 & 0xf)) {
          if(playingNote.playableSound_22 == sequenceData.playableSound_020) {
            if(playingNote.sequenceData_06 == sequenceData) {
              if(!playingNote.finished_08) {
                if(playingNote.used_00) {
                  playingNote.pan_4c = sequenceData.sssqReader_010.readByte(2);
                  sequenceData.param1_003 = playingNote.velocityVolume_2c;
                  SPU.voices[voiceIndex].volumeLeft.set(this.calculateVolume(sequenceData, this.calculatePan(0, playingNote.instrumentIndex_0e), 0));
                  SPU.voices[voiceIndex].volumeRight.set(this.calculateVolume(sequenceData, this.calculatePan(0, playingNote.instrumentIndex_0e), 1));
                }
              }
            }
          }
        }

        //LAB_80049c68
      }

      //LAB_80049c88
      sequenceData.sssqReader_010.advance(3);
    }
  }

  @Method(0x80049cbcL)
  public void sssqHandleSustain(final SequenceData124 sequenceData) {
    assert false;
  }

  @Method(0x80049e2cL)
  public void sssqHandleRepeat(final SequenceData124 sequenceData) {
    final int repeatCount = sequenceData.sssqReader_010.readByte(4);

    if(repeatCount == 0) {
      //LAB_80049ecc
      sequenceData.repeatOffset_02c = sequenceData.sssqReader_010.readShort(2);
      sequenceData.repeatDestCommand_039 = sequenceData.sssqReader_010.readByteAbsolute(sequenceData.repeatOffset_02c);
      sequenceData.repeat_037 = true;
      sequenceData._0e6 = true;

      LOGGER.info(SEQUENCER_MARKER, "Repeat offset %d, count %d", sequenceData.repeatOffset_02c, repeatCount);
    } else if(repeatCount != sequenceData.repeatCounter_035) {
      //LAB_80049ea0
      sequenceData.repeatOffset_02c = sequenceData.sssqReader_010.readShort(2);
      sequenceData.repeatDestCommand_039 = sequenceData.sssqReader_010.readByteAbsolute(sequenceData.repeatOffset_02c);
      sequenceData.repeatCounter_035++;
      sequenceData.repeat_037 = true;
      sequenceData._0e6 = true;

      LOGGER.info(SEQUENCER_MARKER, "Repeat offset %d, count %d", sequenceData.repeatOffset_02c, repeatCount);
    } else {
      sequenceData.repeatCounter_035 = 0;
      sequenceData.repeat_037 = false;
      sequenceData._0e6 = false;

      LOGGER.info(SEQUENCER_MARKER, "Repeat finished");
    }

    //LAB_80049f00
    sequenceData.sssqReader_010.advance(5);
  }

  @Method(0x80049f14L)
  public void sssqHandleDataEntry(final SequenceData124 sequenceData) {
    LOGGER.info(SEQUENCER_MARKER, "Data entry channel %d, param0 %d, param1 %d", sequenceData.command_000 & 0xf, sequenceData.param0_002, sequenceData.param1_003);

    switch(sequenceData.nrpn_11f) {
      case 0 -> {
        sequenceData.repeatCount_11d = sequenceData.param1_003;
        sequenceData.sssqReader_010.advance(3);
        return;
      }

      //LAB_8004a050
      case 4 -> // Attack (linear)
        instrumentLayer_800c6678.adsrLo_06 = instrumentLayer_800c6678.adsrLo_06
          & 0xff
          | 0x7f - sequenceData.param1_003 << 8;

      case 5 -> // Attack (exponential)
        instrumentLayer_800c6678.adsrLo_06 = instrumentLayer_800c6678.adsrLo_06
          & 0xff
          | 0x7f - sequenceData.param1_003 << 8
          | 0x8000;

      case 6 -> // Decay shift
        instrumentLayer_800c6678.adsrLo_06 = instrumentLayer_800c6678.adsrLo_06
          & 0xff0f
          | (0x7f - sequenceData.param1_003) / 0x8 << 4;

      //LAB_8004a050
      case 7 -> // Sustain level
        instrumentLayer_800c6678.adsrLo_06 = instrumentLayer_800c6678.adsrLo_06
          & 0xfff0
          | sequenceData.param1_003 / 0x8;

      //LAB_8004a114
      case 8 -> // Sustain (linear) (everything but level)
        instrumentLayer_800c6678.adsrHi_08 = instrumentLayer_800c6678.adsrHi_08
          & 0x3f
          | (0x7f - sequenceData.param1_003) << 6
          | 0x4000 - sequenceData.sustainDirection_122; // Direction (0 = increase, 1 = decrease)

      //LAB_8004a114
      case 9 -> // Sustain (exponential) (everything but level)
        instrumentLayer_800c6678.adsrHi_08 = instrumentLayer_800c6678.adsrHi_08
          & 0x3f
          | (0x7f - sequenceData.param1_003) << 6
          | 0x4000 - sequenceData.sustainDirection_122 // Direction (0 = increase, 1 = decrease)
          | 0x8000;

      //LAB_8004a114
      case 0xa -> // Release (linear)
        instrumentLayer_800c6678.adsrHi_08 = instrumentLayer_800c6678.adsrHi_08
          & 0xffc0
          | (0x7f - sequenceData.param1_003) / 4;

      case 0xb -> // Release (exponential)
        instrumentLayer_800c6678.adsrHi_08 = instrumentLayer_800c6678.adsrHi_08
          & 0xffc0
          | (0x7f - sequenceData.param1_003) / 4
          | 0x20;

      case 0xc -> { // Sustain direction
        if(sequenceData.param1_003 > 0x40) {
          sequenceData.sustainDirection_122 = 0x4000; // Increase
        } else {
          //LAB_8004a178
          sequenceData.sustainDirection_122 = 0; // Decrease
        }
      }

      case 0xf -> { // Reverb type
        sssqSetReverbType(sequenceData.param1_003);
        sequenceData.sssqReader_010.advance(3);
        return;
      }

      case 0x10 -> { // Reverb volume
        sssqSetReverbVolume(sequenceData.param1_003, sequenceData.param1_003);
        sequenceData.sssqReader_010.advance(3);
        return;
      }

      case 0x11 -> {
        this.FUN_8004c690(sequenceData.param1_003);
        sequenceData.sssqReader_010.advance(3);
        return;
      }

      case 0x12, 0x13 -> {
        this.FUN_8004c5e8(sequenceData.param1_003);
        sequenceData.sssqReader_010.advance(3);
        return;
      }
    }

    //LAB_8004a1d0
    //LAB_8004a1f0
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      final PlayingNote66 playingNote = playingNotes_800c3a40[voiceIndex];

      if(playingNote.noteNumber_02 == 1) {
        if(playingNote.sequenceChannel_04 == (sequenceData.command_000 & 0xf)) {
          if(!playingNote.isPolyphonicKeyPressure_1a) {
            if(playingNote.sequenceData_06 == sequenceData) {
              if(sequenceData.instrumentIndex_120 == 0xff || playingNote.instrumentIndex_0e == sequenceData.instrumentIndex_120) {
                //LAB_8004a274
                SPU.voices[voiceIndex].adsr.lo = instrumentLayer_800c6678.adsrLo_06;
                SPU.voices[voiceIndex].adsr.hi = instrumentLayer_800c6678.adsrHi_08;
              }
            }
          }
        }
      }

      //LAB_8004a28c
    }

    //LAB_8004a2a0
    sequenceData.sssqReader_010.advance(3);
  }

  @Method(0x8004a2c0L)
  public void sssqDataEntryLsb(final int channelIndex) {
    final SequenceData124 sequenceData = sequenceData_800c4ac8[channelIndex];
    final int type = sequenceData.lsbType_11e;

    if(type == 0) {
      //LAB_8004a31c
      sequenceData.nrpn_11f = 0;
      sequenceData.repeatCount_11d = sequenceData.param1_003;

      LOGGER.info(SEQUENCER_MARKER, "NRPN LSB repeat count %d", sequenceData.repeatCount_11d);
    } else if(type == 1 || type == 2) {
      //LAB_8004a32c
      sequenceData.nrpn_11f = sequenceData.param1_003;

      LOGGER.info(SEQUENCER_MARKER, "NRPN LSB %d", sequenceData.nrpn_11f);
    }

    //LAB_8004a338
    sequenceData.sssqReader_010.advance(3);
  }

  @Method(0x8004a34cL)
  public void sssqDataEntryMsb(final int channelIndex) {
    final SequenceData124 sequenceData = sequenceData_800c4ac8[channelIndex];

    final int nrpn = sequenceData.param1_003; // NRPN

    LOGGER.info(SEQUENCER_MARKER, "NRPN MSB %d", nrpn);

    if(nrpn >= 0 && nrpn < 0x10) {
      //LAB_8004a44c
      sequenceData.lsbType_11e = 0x10;
      sequenceData.instrumentIndex_120 = sequenceData.param1_003;
    } else if(nrpn == 0x10) {
      //LAB_8004a430
      sequenceData.lsbType_11e = 1;
      //LAB_8004a398
    } else if(nrpn == 0x14) { // Set repeat offset
      //LAB_8004a3cc
      sequenceData.lsbType_11e = 0;
      sequenceData.nrpn_11f = 0;
      sequenceData.repeatDestCommand_039 = sequenceData.command_000;
      sequenceData.repeatOffset_02c = sequenceData.sssqReader_010.offset();
    } else if(nrpn == 0x1e) { // Repeat
      sequenceData.lsbType_11e = 0;

      //LAB_8004a3e8
      if(sequenceData.repeatCount_11d == 0x7f) { // Simple repeat
        //LAB_8004a424
        sequenceData.repeat_037 = true;
      } else if(sequenceData.repeatCounter_035 < sequenceData.repeatCount_11d) { // Repeat n times
        //LAB_8004a41c
        sequenceData.repeatCounter_035++;
        sequenceData.repeat_037 = true;
      } else { // Repeat finished
        sequenceData.repeatOffset_02c = 0;
        sequenceData.repeatCounter_035 = 0;
        sequenceData.repeat_037 = false;
      }

      //LAB_8004a428
      //LAB_8004a3b8
    } else if(nrpn == 0x7f) {
      //LAB_8004a43c
      sequenceData.lsbType_11e = 2;
      sequenceData.instrumentIndex_120 = 0xff;
    }

    //LAB_8004a458
    sequenceData.sssqReader_010.advance(3);
  }

  @Method(0x8004a46cL)
  public void sssqHandlePitchBend(final SequenceData124 sequenceData) {
    LOGGER.info(SEQUENCER_MARKER, "Pitch bend channel %d, pitch %d", sequenceData.command_000 & 0xf, sequenceData.sssqReader_010.readByte(1));

    sssqChannelInfo_800C6680.pitchBend_0a = sequenceData.sssqReader_010.readByte(1);

    //LAB_8004a4e4
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      final PlayingNote66 playingNote = playingNotes_800c3a40[voiceIndex];
      if(playingNote.sequenceChannel_04 == (sequenceData.command_000 & 0xf)) {
        if(playingNote.playableSound_22 == sequenceData.playableSound_020) {
          if(playingNote.sequenceData_06 == sequenceData) {
            if(playingNote.used_00) {
              SPU.voices[voiceIndex].pitch = this.calculateSampleRate(playingNote.rootKey_40, playingNote.noteNumber_02, playingNote.cents_36 * 4, sssqChannelInfo_800C6680.pitchBend_0a, playingNote.pitchBendMultiplier_3a);
              playingNote.pitchBend_38 = sequenceData.sssqReader_010.readByte(1);
            }
          }
        }
      }

      //LAB_8004a598
    }

    sequenceData.sssqReader_010.advance(2);
  }

  @Method(0x8004a5e0L)
  public void sssqReadDeltaTime(final int channelIndex) {
    final SequenceData124 sequenceData = sequenceData_800c4ac8[channelIndex];

    //LAB_8004a618
    // Read varint
    for(int i = 0; i < 4; i++) {
      final int varint = sequenceData.sssqReader_010.readByte(0);
      sequenceData.sssqReader_010.advance();
      sequenceData.deltaTime_118 <<= 7;

      if((varint & 0x80) == 0) {
        //LAB_8004a720
        sequenceData.deltaTime_118 |= varint;
        break;
      }

      sequenceData.deltaTime_118 |= varint & 0x7f;
    }

    //LAB_8004a664
    if(sequenceData.soundPlaying_02a) {
      sequenceData.tempo_108 = 60;
      sequenceData.ticksPerQuarterNote_10a = 480;
    }

    //LAB_8004a680
    if(sequenceData.tempo_108 != 0) {
      sequenceData.deltaTimeFixedPoint10_114 = sequenceData.deltaTime_118 * 10;

      //LAB_8004a6e8
      //LAB_8004a700
      // 1-decimal fixed-point
      final int msPerTick = sequenceData.tempo_108 * sequenceData.ticksPerQuarterNote_10a * 10 / (soundEnv_800c6630.ticksPerSecond_42 * 60);
      int time;
      if(!sequenceData.longTick_10c) {
        //LAB_8004a72c
        time = sequenceData.remainderTime_110;
      } else {
        sequenceData.longTick_10c = false;
        time = sequenceData.remainderTime_110 - msPerTick;
      }

      time += sequenceData.deltaTimeFixedPoint10_114;

      //LAB_8004a738
      //LAB_8004a748
      //LAB_8004a760
      final int remainder = time % msPerTick;

      if(remainder * 2 > msPerTick) { // More than halfway through a tick
        sequenceData.deltaTimeFixedPoint10_114 = time - remainder + msPerTick;
        sequenceData.longTick_10c = true;
      } else {
        //LAB_8004a78c
        sequenceData.deltaTimeFixedPoint10_114 = time - remainder;
      }

      //LAB_8004a794
      //LAB_8004a7a4
      //LAB_8004a7bc
      //LAB_8004a7d8
      sequenceData.remainderTime_110 = remainder;
      sequenceData.deltaTime_118 = sequenceData.deltaTimeFixedPoint10_114 / msPerTick;
    }

    //LAB_8004a7e4
  }

  @Method(0x8004a7ecL)
  public void sssqReadEvent(final SequenceData124 sequenceData) {
    final int command = sequenceData.sssqReader_010.readByte(0);
    if((command & 0x80) != 0) { // If this is a command
      sequenceData.command_000 = command;
      sequenceData.previousCommand_001 = command;
    } else { // Otherwise, repeat the previous command with new parameters
      //LAB_8004a854
      sequenceData.sssqReader_010.rewind();
      sequenceData.command_000 = sequenceData.previousCommand_001;
    }

    //LAB_8004a860
    sequenceData.param0_002 = sequenceData.sssqReader_010.readByte(1);
    sequenceData.param1_003 = sequenceData.sssqReader_010.readByte(2);

    // Don't read the next byte if we get to the end of the sequence
    if(sequenceData.command_000 != 0xff || sequenceData.param0_002 != 0x2f || sequenceData.param1_003 != 0) {
      sequenceData.param2_005 = sequenceData.sssqReader_010.readByte(3);
    }
  }

  @Method(0x8004a8b8L)
  public void sssqFreeFinishedNotes() {
    LAB_8004a8dc:
    for(int sequenceDataIndex = 0; sequenceDataIndex < 24; sequenceDataIndex++) {
      final SequenceData124 sequenceData = sequenceData_800c4ac8[sequenceDataIndex];

      if(sequenceData.soundLoaded_029 && sequenceData.soundEnded_0e7) {
        //LAB_8004a908
        for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
          if(playingNotes_800c3a40[voiceIndex].sequenceData_06 == sequenceData) {
            continue LAB_8004a8dc;
          }
        }

        sequenceData.deltaTime_118 = 0;
        sequenceData.soundEnded_0e7 = false;
        sequenceData.pitchShiftVolRight_0f0 = 0;
        sequenceData.pitchShiftVolLeft_0ee = 0;
        sequenceData._104 = false;
        sequenceData._105 = false;
        sequenceData.playableSound_020 = null;
        sequenceData.soundPlaying_02a = false;
        sequenceData.soundLoaded_029 = false;
        soundEnv_800c6630.pitchShifted_22 = false;
      }

      //LAB_8004a96c
    }

    //LAB_8004a99c
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      if((SPU.voices[voiceIndex].adsrVolume & 0x7fff) < 16) {
        final PlayingNote66 playingNote = playingNotes_800c3a40[voiceIndex];
        if(playingNote.finished_08) {
          if(playingNote.isPolyphonicKeyPressure_1a && soundEnv_800c6630._0d > 0) {
            soundEnv_800c6630._0d--;
          }

          //LAB_8004aa04
          //LAB_8004aa0c
          for(int voiceIndex2 = 0; voiceIndex2 < 24; voiceIndex2++) {
            if(playingNotes_800c3a40[voiceIndex2]._0a > playingNote._0a && playingNotes_800c3a40[voiceIndex2]._0a != -1) {
              playingNotes_800c3a40[voiceIndex2]._0a--;
            }

            //LAB_8004aa48
          }

          //LAB_8004aa7c
          playingNote.clear();
          playingNote.sequenceData_06 = null;
          playingNote.sequenceIndex_26 = -1;
          playingNote.patchIndex_24 = -1;
          playingNote.playableSound_22 = null;
          playingNote._0a = -1;
          playingNote.portamentoNote_4e = 120;

          if(soundEnv_800c6630._00 > 0) {
            soundEnv_800c6630._00--;
          }
        }
      }

      //LAB_8004aaec
    }
  }

  @Method(0x8004af3cL)
  public int interpolate(final int newValue, final int oldValue, final int totalTime, final int remainingTime) {
    return newValue + (oldValue - newValue) * remainingTime / totalTime;
  }

  @Method(0x8004af98L)
  public void handleVolumeChanging(final SequenceData124 sequenceData) {
    if(sequenceData.volumeIsChanging_03c) {
      int volumesChanged = 0;

      if(sequenceData.volumeChange_03e[0].used_0a) {
        sssqReader_800c667c = sequenceData.sssqReader_010;

        if(sequenceData.volumeChange_03e[0].newValue_0c != sssqReader_800c667c.baseVolume()) {
          if(sequenceData.volumeChange_03e[0].remainingTime_0e != 0) {
            sssqReader_800c667c.baseVolume(this.interpolate(sequenceData.volumeChange_03e[0].newValue_0c, sequenceData.volumeChange_03e[0].oldValue_12, sequenceData.volumeChange_03e[0].totalTime_10, sequenceData.volumeChange_03e[0].remainingTime_0e));
            sequenceData.volumeChange_03e[0].remainingTime_0e--;
          } else {
            //LAB_8004b064
            sssqReader_800c667c.baseVolume(sequenceData.volumeChange_03e[0].newValue_0c);
          }

          //LAB_8004b068
          setSequenceVolume(sequenceData, sssqReader_800c667c.baseVolume());
          volumesChanged++;
        }
      }

      //LAB_8004b084
      //LAB_8004b088
      //LAB_8004b0a0
      for(int commandChannel = 0; commandChannel < 16; commandChannel++) {
        final Sssq.ChannelInfo channelInfo = sequenceData.sssqReader_010.channelInfo(commandChannel);

        if(sequenceData.volumeChange_03e[commandChannel].used_00 && sequenceData.volumeChange_03e[commandChannel].newValue_02 != channelInfo.volume_03) {
          if(sequenceData.volumeChange_03e[commandChannel].remainingTime_04 != 0) {
            channelInfo.volume_03 = this.interpolate(sequenceData.volumeChange_03e[commandChannel].newValue_02, sequenceData.volumeChange_03e[commandChannel].oldValue_08, sequenceData.volumeChange_03e[commandChannel].totalTime_06, sequenceData.volumeChange_03e[commandChannel].remainingTime_04);
            sequenceData.volumeChange_03e[commandChannel].remainingTime_04--;
          } else {
            //LAB_8004b110
            channelInfo.volume_03 = sequenceData.volumeChange_03e[commandChannel].newValue_02;
          }

          //LAB_8004b114
          this.setSequenceChannelVolume(sequenceData, (short)commandChannel, (short)channelInfo.volume_03);
          volumesChanged++;
        }
      }

      if(volumesChanged == 0) {
        //LAB_8004b15c
        //LAB_8004b16c
        for(int n = 0; n < 16; n++) {
          sequenceData.volumeChange_03e[n].clear();
        }

        if(sequenceData.volumeIsDecreasing_03a) {
          sequenceData.volumeIsDecreasing_03a = false;
          stopMusicSequence(sequenceData, 1);
        }

        //LAB_8004b1c0
        sequenceData.volumeIsChanging_03c = false;
      }
    }

    //LAB_8004b1c4
  }

  @Method(0x8004b2c4L)
  public void handleFadeInOut() {
    final SoundEnv44 soundEnv = soundEnv_800c6630;

    if(soundEnv.fadingIn_2a) {
      if(sssqFadeCurrent_8005a1ce == soundEnv.fadeTime_2c) {
        sssqFadeCurrent_8005a1ce = 0;
        soundEnv.fadingIn_2a = false;
      } else {
        //LAB_8004b310
        //LAB_8004b33c
        //LAB_8004b354
        final int vol = soundEnv.fadeInVol_2e * sssqFadeCurrent_8005a1ce / soundEnv.fadeTime_2c;
        setMainVolume(vol, vol);
        sssqFadeCurrent_8005a1ce++;
      }
    }

    //LAB_8004b370
    if(soundEnv.fadingOut_2b) {
      if(sssqFadeCurrent_8005a1ce == soundEnv.fadeTime_2c) {
        sssqFadeCurrent_8005a1ce = 0;
        soundEnv.fadingOut_2b = false;
      } else {
        //LAB_8004b3a0
        //LAB_8004b3cc
        //LAB_8004b3e4
        //LAB_8004b410
        //LAB_8004b428
        final int l = soundEnv.fadeOutVolL_30 * (soundEnv.fadeTime_2c - sssqFadeCurrent_8005a1ce) / soundEnv.fadeTime_2c;
        final int r = soundEnv.fadeOutVolR_32 * (soundEnv.fadeTime_2c - sssqFadeCurrent_8005a1ce) / soundEnv.fadeTime_2c;
        setMainVolume(l, r);
        sssqFadeCurrent_8005a1ce++;
      }
    }

    //LAB_8004b450
  }

  @Method(0x8004b464L)
  public void setSequenceChannelVolume(final SequenceData124 sequenceData, final short sequenceChannel, final short volume) {
    sssqReader_800c667c = sequenceData.sssqReader_010;
    final Sssq.ChannelInfo channelInfo = sequenceData.sssqReader_010.channelInfo(sequenceChannel);
    sssqChannelInfo_800C6680 = channelInfo;
    channelInfo.volume_03 = volume;
    channelInfo.volume_0e = volume * sssqReader_800c667c.baseVolume() >> 7;

    //LAB_8004b514
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      final PlayingNote66 playingNote = playingNotes_800c3a40[voiceIndex];

      if(playingNote.used_00) {
        if(!playingNote.isPolyphonicKeyPressure_1a) {
          if(playingNote.playableSound_22 == sequenceData.playableSound_020) {
            if(playingNote.sequenceData_06 == sequenceData) {
              if(playingNote.sequenceChannel_04 == sequenceChannel) {
                updateVoiceVolume(voiceIndex);
              }
            }
          }
        }
      }
    }
  }

  /**
   * Scale a value using a 12-bit fixed-point multiplier.
   *
   * Dunno if negative scaling is right. It seems to match the ASM but does not yield the results I would expect.
   */
  @Method(0x8004b644L)
  public short scaleValue12(final short value, final short multiplier12) {
    final int v0;
    final int v1;
    if(value >= 0) {
      //LAB_8004b660
      v0 = multiplier12 & 0x7fff;
      v1 = value & 0xffff;
    } else {
      v0 = (value & 0x7f) << 7;
      v1 = multiplier12 & 0x7fff;
    }

    //LAB_8004b668
    final int ret;
    if(multiplier12 >= 0) {
      ret = v1 * v0 >> 12;
    } else {
      ret = 0x7fff - (v1 * v0 >> 12);
    }

    //LAB_8004b688
    return (short)ret;
  }

  @Method(0x8004c5e8L)
  public void FUN_8004c5e8(final long a0) {
    assert false;
  }

  @Method(0x8004c690L)
  public void FUN_8004c690(final long a0) {
    assert false;
  }
}
