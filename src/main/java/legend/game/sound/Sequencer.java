package legend.game.sound;

import legend.core.memory.Method;
import legend.core.spu.Voice;

import java.util.function.Supplier;

import static legend.core.GameEngine.SPU;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004ad2c;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004ae94;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004c8dc;
import static legend.game.Scus94491BpeSegment_8004.FUN_8004d034;
import static legend.game.Scus94491BpeSegment_8004.setKeyOff;
import static legend.game.Scus94491BpeSegment_8004.setKeyOn;
import static legend.game.Scus94491BpeSegment_8004.setMainVolume;
import static legend.game.Scus94491BpeSegment_8004.sssqSetReverbType;
import static legend.game.Scus94491BpeSegment_8004.sssqSetReverbVolume;
import static legend.game.Scus94491BpeSegment_8005._8005967c;
import static legend.game.Scus94491BpeSegment_8005._80059b3c;
import static legend.game.Scus94491BpeSegment_8005.panVolume_80059f3c;
import static legend.game.Scus94491BpeSegment_8005.sssqFadeCurrent_8005a1ce;
import static legend.game.Scus94491BpeSegment_800c._800c3a40;
import static legend.game.Scus94491BpeSegment_800c._800c4ac8;
import static legend.game.Scus94491BpeSegment_800c._800c6630;
import static legend.game.Scus94491BpeSegment_800c.playableSoundPtrArr_800c43d0;
import static legend.game.Scus94491BpeSegment_800c.sshd10Arr_800c6678;
import static legend.game.Scus94491BpeSegment_800c.sshd10Index_800c6678;
import static legend.game.Scus94491BpeSegment_800c.sshd10_800c6678;
import static legend.game.Scus94491BpeSegment_800c.sshdPtr_800c4ac0;
import static legend.game.Scus94491BpeSegment_800c.sssqEntry_800c6680;
import static legend.game.Scus94491BpeSegment_800c.sssqReader_800c667c;
import static legend.game.Scus94491BpeSegment_800c.sssqish_800c4aa8;
import static legend.game.Scus94491BpeSegment_800c.subfile0_800c4aa8;
import static legend.game.Scus94491BpeSegment_800c.subfile0_800c4aac;
import static legend.game.Scus94491BpeSegment_800c.sublist_800c6674;
import static legend.game.Scus94491BpeSegment_800c.voicePtr_800c4ac4;
import static legend.game.Scus94491BpeSegment_800c.volumeRamp_800c4ab0;
import static legend.game.Scus94491BpeSegment_800c.waveforms_800c4ab8;

public class Sequencer {
  private final Object lock = new Object();

  public <T> T waitForLock(final Supplier<T> task) {
    synchronized(this.lock) {
      return task.get();
    }
  }

  @Method(0x80045cb8L)
  public void tick() {
    synchronized(this.lock) {
      this.FUN_8004a8b8();

      final SpuStruct44 spu44 = _800c6630;

      //LAB_80045d04
      for(spu44.channelIndex_01 = 0; spu44.channelIndex_01 < 24; spu44.channelIndex_01++) {
        final SpuStruct124 spu124 = _800c4ac8[spu44.channelIndex_01];

        if(spu124._028 == 1 || spu124._02a == 1) {
          //LAB_80045d24
          this.FUN_80047b38(spu44.channelIndex_01);

          LAB_80045d40:
          while(spu124.deltaTime_118 == 0) {
            this.sssqReadEvent(spu44.channelIndex_01);

            if(!this.FUN_80047bd0(spu44.channelIndex_01)) {
              spu124.sssqReader_010.advance(3);
              spu44._04 = 0;
            } else {
              //LAB_80045d7c
              spu44._04 = 1;

              final int command = spu124.command_000 & 0xf0;
              if(command == 0x80) { // Key off event
                //LAB_80045fdc
                this.sssqHandleKeyOff(spu44.channelIndex_01);
                //LAB_80045dc0
              } else if(command == 0x90) { // Key on event
                //LAB_80046004
                this.sssqHandleKeyOn(spu44.channelIndex_01);
              } else if(command == 0xa0) { // Polyphonic key pressure (aftertouch)
                //LAB_80045ff0
                this.sssqHandlePolyphonicKeyPressure(spu44.channelIndex_01);
                //LAB_80045dd4
              } else if(command == 0xb0) { // Control change
                //LAB_80045e60
                switch(spu124.param0_002) { // Control number
                  case 0x1 -> this.sssqHandleModulationWheel(spu44.channelIndex_01); // Modulation wheel
                  case 0x2 -> this.sssqHandleBreathControl(spu44.channelIndex_01); // Breath control
                  case 0x6 -> this.sssqHandleDataEntry(spu44.channelIndex_01); // Data entry
                  case 0x7 -> this.sssqHandleVolume(spu44.channelIndex_01); // Volume

                  case 0xa -> { // Pan
                    if(!spu44.mono_36) {
                      //LAB_80045f44
                      this.sssqHandlePan(spu44.channelIndex_01);
                    } else if(spu124._028 == 0) {
                      //LAB_80045f30
                      spu124.sssqReader_010.advance(6);
                    } else {
                      sssqEntry_800c6680.pan_04 = spu124.sssqReader_010.readByte(2);
                      spu124.sssqReader_010.advance(3);
                    }
                  }

                  case 0x40 -> this.sssqHandleSustain(spu44.channelIndex_01); // Damper pedal (sustain)
                  case 0x41 -> this.sssqHandlePortamento(spu44.channelIndex_01); // Portamento

                  case 0x60 -> { // Data increment (???)
                    this.FUN_80049e2c(spu44.channelIndex_01); // Seems to jump to a different part of the sequence
                    this.sssqReadDeltaTime(spu44.channelIndex_01);
                    break LAB_80045d40;
                  }

                  case 0x62 -> this.sssqDataEntryLsb(spu44.channelIndex_01); // Non-registered parameter number LSB
                  case 0x63 -> this.sssqDataEntryMsb(spu44.channelIndex_01); // Non-registered parameter number MSB
                }
              } else if(command == 0xc0) { // Program change
                //LAB_80045e4c
                this.sssqHandleProgramChange(spu44.channelIndex_01);
              } else if(command == 0xe0) { // Pitch bend
                //LAB_80045fc8
                this.sssqHandlePitchBend(spu44.channelIndex_01);
              } else if(command == 0xf0) { // Meta event
                //LAB_80045df8
                if(spu124.param0_002 == 0x2f) { // End of track
                  //LAB_80045e24
                  this.sssqHandleEndOfTrack(spu44.channelIndex_01);
                  break;
                }

                if(spu124.param0_002 == 0x51) { // Tempo
                  //LAB_80045e38
                  this.sssqHandleTempo(spu44.channelIndex_01);
                }
              }
            }

            //LAB_80046010
            this.sssqReadDeltaTime(spu44.channelIndex_01);
          }

          //LAB_8004602c
          if(spu44._04 != 0) {
            spu44.keyOn_3a |= spu124.keyOn_0de;
            spu44.keyOff_3e |= spu124.keyOff_0e2;

            spu124.keyOn_0de = 0;
            spu124.keyOff_0e2 = 0;

            spu44._04 = 0;
          }

          //LAB_800460a0
          if(spu124.tempo_108 != 0 || spu124._02a == 1) {
            //LAB_800460c0
            if(spu124.deltaTime_118 != 0) {
              spu124.deltaTime_118--;
            }
          }

          //LAB_800460d4
          if(spu124.repeat_037) {
            spu124.command_000 = spu124.repeatDestCommand_039;
            spu124.previousCommand_001 = spu124.repeatDestCommand_039;
            spu124.sssqReader_010.jump(spu124.repeatOffset_02c);
            spu124.repeat_037 = false;

            if(spu124._0e6 == 0) {
              //LAB_80046118
              spu124._028 = 1;
              spu124.deltaTime_118 = 0;
            } else {
              spu124._02a = 1;
              spu124._0e6 = 0;
            }
          }
        }

        //LAB_80046120
        this.FUN_8004af98(spu44.channelIndex_01);
      }

      SPU.VOICE_CHN_NOISE_MODE.set(spu44.noiseMode_16);
      SPU.VOICE_CHN_REVERB_MODE.set(spu44.reverbMode_12);

      if(spu44.keyOff_3e != 0) {
        SPU.VOICE_KEY_OFF.set(spu44.keyOff_3e);
      }

      //LAB_800461b0
      if(spu44.keyOn_3a != 0) {
        SPU.VOICE_KEY_ON.set(spu44.keyOn_3a);
      }

      //LAB_800461e0
      spu44.keyOn_3a = 0;
      spu44.keyOff_3e = 0;

      this.FUN_800470fc();
      this.FUN_8004b2c4();
    }
  }

  @Method(0x80046224L)
  public void sssqHandlePolyphonicKeyPressure(final int channelIndex) {
    final SpuStruct124 spu124 = _800c4ac8[channelIndex];
    if(spu124.param1_003 == 0) {
      this.FUN_80048514(channelIndex);
      return;
    }

    //LAB_8004629c
    final int instrumentIndex = spu124.param0_002 - sublist_800c6674.startingKeyPosition_06;
    if(instrumentIndex < 0) {
      return;
    }

    sshd10Index_800c6678 += instrumentIndex;
    sshd10_800c6678 = sshd10Arr_800c6678[sshd10Index_800c6678];
    sssqEntry_800c6680.pitchBend_0a = 64;

    final short voiceIndex = (short)this.FUN_80048000(sshd10_800c6678.minKeyRange_00, sshd10_800c6678.maxKeyRange_01, spu124.playableSoundIndex_020);
    if(voiceIndex == -1) {
      spu124.sssqReader_010.advance(4);
      _800c6630._04 = 0;
      return;
    }

    //LAB_8004632c
    final SpuStruct66 struct66 = _800c3a40[voiceIndex];
    if((sshd10_800c6678.flags_0f & 0x1) != 0) {
      struct66._08 = 0;
      struct66._0c = 1;
    } else {
      //LAB_80046374
      struct66._08 = 1;
      struct66._0c = 0;
    }

    //LAB_8004639c
    struct66.used_00 = true;
    struct66.noteNumber_02 = spu124.param0_002;
    struct66.commandChannel_04 = spu124.command_000 & 0xf;
    struct66.channelIndex_06 = channelIndex;
    struct66.voiceIndex_0a = _800c6630.voiceIndex_00;
    struct66.instrumentIndex_0e = instrumentIndex;
    struct66._12 = 0;
    struct66._18 = 0;
    struct66._1a = 1;
    struct66._1c = 4;
    struct66.maxKeyRange_1e = sshd10_800c6678.maxKeyRange_01;
    struct66.minKeyRange_20 = sshd10_800c6678.minKeyRange_00;
    struct66.playableSoundIndex_22 = spu124.playableSoundIndex_020;
    struct66.patchIndex_24 = spu124.patchIndex_024;
    struct66.sequenceIndex_26 = spu124.sequenceIndex_022;
    struct66.volume_28 = sssqEntry_800c6680.volume_0e;
    struct66._2a = sublist_800c6674.patchVolume_01;
    struct66.volume_2c = volumeRamp_800c4ab0.ramp_02[spu124.param1_003];
    struct66.volume_2e = sshd10_800c6678.volume_0b;
    struct66._30[0] = panVolume_80059f3c.get((this.calculatePan(4, 0) / 2 & 0x7ffe) / 2).left_00.get();
    struct66._30[1] = panVolume_80059f3c.get((this.calculatePan(4, 0) / 2 & 0x7ffe) / 2).right_01.get();
    struct66.volume_34 = sssqEntry_800c6680.volume_03;
    struct66.cents_36 = sshd10_800c6678.cents_03;
    struct66.pitchBend_38 = sssqEntry_800c6680.pitchBend_0a;
    struct66._3a = sshd10_800c6678._0d;
    struct66.breath_3c = sssqEntry_800c6680.breath_0c;
    struct66._3e = spu124.param2_005;
    struct66.portamentoChanging_44 = false;
    struct66.rootKey_40 = sshd10_800c6678.rootKey_02;
    struct66._4a = sshd10_800c6678._0a;
    struct66.pan_4c = sshd10_800c6678.pan_0c;
    struct66._4e = 120;
    struct66.portamentoTimeRemaining_62 = 0;

    if(_800c6630.voiceIndex_00 < 24) {
      _800c6630.voiceIndex_00++;
    }

    final int v1 = sshd10_800c6678.flags_0f;
    if((v1 & 0x20) != 0) {
      if((v1 & 0x40) != 0) {
        struct66._10 = sublist_800c6674._05;
      } else {
        //LAB_800465a4
        struct66._10 = sshd10_800c6678._0e;
      }

      //LAB_800465b0
      struct66.modulationEnabled_14 = true;
      struct66.modulation_16 = 127;
    } else {
      //LAB_800465ec
      struct66.modulationEnabled_14 = false;
    }

    //LAB_800465f0
    int l = this.calculateVolume(channelIndex, this.calculatePan(4, 0), 0);
    int r = this.calculateVolume(channelIndex, this.calculatePan(4, 0), 1);

    final int t0;
    if((sshd10_800c6678.flags_0f & 0x10) != 0) {
      t0 = sublist_800c6674._04;
    } else {
      //LAB_80046668
      t0 = sshd10_800c6678._0d;
    }

    //LAB_8004666c
    if(spu124.pitchShifted_0e9 != 0) {
      //LAB_8004669c
      voicePtr_800c4ac4.deref().voices[voiceIndex].ADPCM_SAMPLE_RATE.set(this.calculateSampleRate(sshd10_800c6678.rootKey_02, spu124.param0_002, sshd10_800c6678.cents_03, sssqEntry_800c6680.pitchBend_0a, t0) * spu124.pitch_0ec / 0x1000);
      l = this.scaleValue12((short)l, (short)spu124.pitchShiftVolLeft_0ee);
      r = this.scaleValue12((short)r, (short)spu124.pitchShiftVolRight_0f0);
      struct66._42 = 1;
    } else {
      //LAB_80046730
      //LAB_80046750
      voicePtr_800c4ac4.deref().voices[voiceIndex].ADPCM_SAMPLE_RATE.set(this.calculateSampleRate(sshd10_800c6678.rootKey_02, spu124.param0_002, sshd10_800c6678.cents_03, sssqEntry_800c6680.pitchBend_0a, t0));
      l = this.scaleValue12((short)l, (short)0x1000);
      r = this.scaleValue12((short)r, (short)0x1000);
      struct66._42 = 0;
    }

    //LAB_800467c8
    if(_800c6630.mono_36) {
      l = Math.max(l, r);
      r = l;
    }

    //LAB_800467f0
    final Voice voice = voicePtr_800c4ac4.deref().voices[voiceIndex];
    voice.LEFT.set(l);
    voice.RIGHT.set(r);
    voice.ADPCM_START_ADDR.set(playableSoundPtrArr_800c43d0[spu124.playableSoundIndex_020].soundBufferPtr_08 + sshd10_800c6678.soundOffset_04);
    voice.ADSR_LO.set(sshd10_800c6678.adsrLo_06);
    voice.ADSR_HI.set(sshd10_800c6678.adsrHi_08);
    setKeyOn(channelIndex, voiceIndex);

    if(spu124.reverbEnabled_0ea != 0 || (sshd10_800c6678.flags_0f & 0x80) != 0) {
      _800c6630.reverbMode_12 |= 1 << voiceIndex;
      //LAB_80046884
      //LAB_800468d8
    } else if((sshd10_800c6678.flags_0f & 0x80) == 0) {
      _800c6630.reverbMode_12 &= ~(1 << voiceIndex);
    }

    //LAB_80046914
    if((sshd10_800c6678.flags_0f & 0x2) == 0) {
      //LAB_80046964
      _800c6630.noiseMode_16 &= ~(1 << voiceIndex);
    } else {
      this.setNoiseMode(channelIndex, voiceIndex);
      voicePtr_800c4ac4.deref().SPUCNT
        .and(0xc0ff) // Mask off noise freq step/shift
        .or(sshd10_800c6678.rootKey_02 << 8);
    }

    //LAB_800469ac
    sshd10Index_800c6678 -= instrumentIndex;
    sshd10_800c6678 = sshd10Arr_800c6678[sshd10Index_800c6678];
    spu124.sssqReader_010.advance(4);

    //LAB_800469d4
  }

  @Method(0x80046a04L)
  public void sssqHandleKeyOn(final int channelIndex) {
    final SpuStruct124 s2 = _800c4ac8[channelIndex];
    if(s2.param1_003 == 0) { // Velocity
      this.sssqHandleKeyOff(channelIndex);
      return;
    }

    //LAB_80046a7c
    if(sssqEntry_800c6680.volume_03 != 0) {
      final int count = sublist_800c6674.count_00;
      if(count == 0xff) {
        final int v0 = s2.param0_002 - sublist_800c6674.startingKeyPosition_06;
        s2.endingInstrument_026 = v0;
        s2.startingInstrument_01e = v0;
        //LAB_80046acc
      } else if((count & 0x80) != 0) {
        s2.endingInstrument_026 = count & ~0x80;
        s2.startingInstrument_01e = 0;
        _800c6630._0c = false;
      } else {
        //LAB_80046ae8
        s2.endingInstrument_026 = count;
        s2.startingInstrument_01e = 0;
        _800c6630._0c = true;
      }

      //LAB_80046af8
      //LAB_80046b24
      for(int instrumentIndex = s2.startingInstrument_01e; instrumentIndex < s2.endingInstrument_026 + 1; instrumentIndex++) {
        if(this.instrumentCanPlayNote(sublist_800c6674.patchVolume_01, instrumentIndex, s2.param0_002)) {
          final int voiceIndex = this.FUN_80047e1c();
          if(voiceIndex == -1) {
            break;
          }

          final SpuStruct66 s1 = _800c3a40[voiceIndex];

          sshd10Index_800c6678 += instrumentIndex;
          sshd10_800c6678 = sshd10Arr_800c6678[sshd10Index_800c6678];
          if((sshd10_800c6678.flags_0f & 0x1) != 0) {
            s1._08 = 0;
            s1._0c = 1;
          } else {
            //LAB_80046bb4
            s1._08 = 1;
            s1._0c = 0;
          }

          //LAB_80046bdc
          s1.used_00 = true;
          s1.noteNumber_02 = s2.param0_002;
          s1.channelIndex_06 = channelIndex;
          s1.commandChannel_04 = s2.command_000 & 0xf;
          s1.voiceIndex_0a = _800c6630.voiceIndex_00;
          s1.instrumentIndex_0e = instrumentIndex;
          s1._12 = 0;
          s1._1a = 0;
          s1._1c = 0;
          s1.maxKeyRange_1e = 0;
          s1.minKeyRange_20 = 0;
          s1.playableSoundIndex_22 = s2.playableSoundIndex_020;
          s1.volume_28 = sssqEntry_800c6680.volume_0e;
          s1._2a = sublist_800c6674.patchVolume_01;
          s1.volume_2c = volumeRamp_800c4ab0.ramp_02[s2.param1_003];
          s1.volume_2e = sshd10_800c6678.volume_0b;
          s1._30[0] = panVolume_80059f3c.get((this.calculatePan(0, 0) / 2 & 0x7ffe) / 2).left_00.get();
          s1._30[1] = panVolume_80059f3c.get((this.calculatePan(0, 0) / 2 & 0x7ffe) / 2).right_01.get();
          s1.volume_34 = sssqEntry_800c6680.volume_03;
          s1.cents_36 = sshd10_800c6678.cents_03;
          s1.pitchBend_38 = sssqEntry_800c6680.pitchBend_0a;
          s1._3a = sshd10_800c6678._0d;
          s1.breath_3c = sssqEntry_800c6680.breath_0c;
          s1._3e = s2.param2_005;
          s1.rootKey_40 = sshd10_800c6678.rootKey_02;
          s1._42 = 0;
          s1.portamentoChanging_44 = false;
          s1._4a = sshd10_800c6678._0a;
          s1.pan_4c = sssqEntry_800c6680.pan_04;
          s1._4e = 120;

          if(sssqEntry_800c6680._0b == 0x7f) {
            s1._18 = 1;
          }

          //LAB_80046d80
          if((sshd10_800c6678.flags_0f & 0x20) == 0 || sssqEntry_800c6680.modulation_09 == 0) {
            //LAB_80046e1c
            //LAB_80046e20
            s1.modulationEnabled_14 = false;
            s1.modulation_16 = 0;
          } else {
            if((sshd10_800c6678.flags_0f & 0x40) != 0) {
              s1._10 = sublist_800c6674._05;
            } else {
              //LAB_80046dd0
              s1._10 = sshd10_800c6678._0e;
            }

            //LAB_80046ddc
            s1.modulationEnabled_14 = true;
            s1.modulation_16 = sssqEntry_800c6680.modulation_09;
          }

          //LAB_80046e4c
          final int t0;
          if((sshd10_800c6678.flags_0f & 0x10) != 0) {
            t0 = sublist_800c6674._04;
          } else {
            //LAB_80046e7c
            t0 = sshd10_800c6678._0d;
          }

          //LAB_80046e80
          //LAB_80046ea0
          voicePtr_800c4ac4.deref().voices[voiceIndex].ADPCM_SAMPLE_RATE.set(this.calculateSampleRate(sshd10_800c6678.rootKey_02, s2.param0_002, sshd10_800c6678.cents_03, sssqEntry_800c6680.pitchBend_0a, t0));
          int l = this.calculateVolume(channelIndex, this.calculatePan(0, 0), 0);
          int r = this.calculateVolume(channelIndex, this.calculatePan(0, 0), 1);

          if(_800c6630.mono_36) {
            l = Math.max(l, r);
            r = l;
          }

          //LAB_80046f30
          final Voice voice = voicePtr_800c4ac4.deref().voices[voiceIndex];
          voice.LEFT.set(l);
          voice.RIGHT.set(r);
          voice.ADPCM_START_ADDR.set(playableSoundPtrArr_800c43d0[s2.playableSoundIndex_020].soundBufferPtr_08 + sshd10_800c6678.soundOffset_04);
          voice.ADSR_LO.set(sshd10_800c6678.adsrLo_06);
          voice.ADSR_HI.set(sshd10_800c6678.adsrHi_08);
          setKeyOn(channelIndex, voiceIndex);

          if((sshd10_800c6678.flags_0f & 0x80) != 0) {
            _800c6630.reverbMode_12 |= 1 << voiceIndex;
          } else {
            //LAB_80046fe8
            _800c6630.reverbMode_12 &= ~(1 << voiceIndex);
          }

          //LAB_80047024
          _800c6630.noiseMode_16 &= ~(1 << voiceIndex);

          //LAB_8004706c
          if(_800c6630.voiceIndex_00 < 24) {
            _800c6630.voiceIndex_00++;
          }

          sshd10Index_800c6678 -= instrumentIndex;
          sshd10_800c6678 = sshd10Arr_800c6678[sshd10Index_800c6678];

          if(_800c6630._0c) {
            //LAB_80046ae0
            _800c6630._0c = false;
            break;
          }
        }

        //LAB_8004709c
      }
    }

    //LAB_800470bc
    s2.sssqReader_010.advance(3);

    //LAB_800470cc
  }

  @Method(0x800470fcL)
  public void FUN_800470fc() {
    final SpuStruct44 struct44 = _800c6630;

    //LAB_80047144
    for(short voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      final Voice voice = voicePtr_800c4ac4.deref().voices[voiceIndex];
      final SpuStruct66 struct66 = _800c3a40[voiceIndex];

      if(struct66.used_00) {
        if(struct66.channelIndex_06 >= 0 && struct66.channelIndex_06 < 24) {
          final SpuStruct124 struct124 = _800c4ac8[struct66.channelIndex_06];

          if(struct66.modulationEnabled_14 || struct66.portamentoChanging_44 || struct124._104 == 1) {
            //LAB_800471d0
            //LAB_800471d4
            int cents = struct66.cents_36;
            int note = struct66.noteNumber_02;
            int rootKey = struct66.rootKey_40;
            int pitchBend = struct66.pitchBend_38;
            int t3 = struct66._3a;
            if(struct66.modulationEnabled_14 || struct66.portamentoChanging_44) {
              //LAB_80047220
              if(note >= rootKey) { //TODO I'm pretty sure these branches are equivalent?
                rootKey = 120 - (note - rootKey);
              } else {
                //LAB_80047244
                rootKey = 120 + rootKey - note;
              }

              //LAB_80047248
              if(struct66.modulationEnabled_14) {
                if(struct44.ticksPerSecond_42 != 60 || (struct66.breath_3c & 0xfff) != 120) {
                  //LAB_800472cc
                  //LAB_800472d0
                  struct66._12 += struct66.breath_3c & 0xfff;
                } else {
                  final int v0_0 = struct66.breath_3c & 0xf000;
                  if(v0_0 != 0) {
                    struct66.breath_3c = struct66.breath_3c & 0xfff | v0_0 - 0x1000;
                    struct66._12 += struct66.breath_3c & 0xfff;
                  } else {
                    //LAB_800472c0
                    struct66.breath_3c |= 0x6000;
                  }
                }

                //LAB_80047300
                final Sshd sshd = playableSoundPtrArr_800c43d0[struct124.playableSoundIndex_020].sshdPtr_04;

                pitchBend = 0x80;
                sshdPtr_800c4ac0 = sshd;

                if(sshd.hasSubfile(2)) {
                  waveforms_800c4ab8 = sshd.getSubfile(2, WaveformList::new);

                  if(struct66._12 >= 0xf0) {
                    struct66._12 = (struct66.breath_3c & 0xfff) >>> 1;
                  }

                  //LAB_800473a0
                  pitchBend = waveforms_800c4ab8.waveforms_02[struct66._10]._00[struct66._12 >>> 2];
                }

                //LAB_800473d4
                //LAB_800473d8
                note = struct66._4e;
                if(struct66._1c == 0) {
                  int v0;
                  int v1;
                  if(struct66.pitchBend_38 >= 64) {
                    v0 = (struct66.pitchBend_38 - 0x40) * struct66._3a;
                    v1 = v0 / 64;
                    note = note + v1;
                    v0 = v0 / 4;
                    v1 = v1 * 16;
                  } else {
                    //LAB_80047454
                    v0 = (0x40 - struct66.pitchBend_38) * struct66._3a;
                    final int a0_0 = v0 / 64;
                    note = note - a0_0;
                    v1 = v0 / 4;
                    v0 = a0_0 * 16;
                  }

                  //LAB_8004748c
                  v0 = v0 - v1;
                  cents = cents + v0;
                  t3 = 1;
                }

                //LAB_80047498
                pitchBend = pitchBend * struct66.modulation_16 / 255 - ((struct66.modulation_16 + 1) / 2 - 64);
              }

              //LAB_800474f0
              if(struct66.portamentoChanging_44) {
                if(struct66.portamentoTimeRemaining_62 != 0) {
                  struct66.portamentoTimeRemaining_62--;

                  if(struct66.newPortamento_60 < 0) {
                    final int portamentoTimeElapsed = struct66.portamentoTimeTotal_64 - struct66.portamentoTimeRemaining_62;
                    note = struct66._4e - portamentoTimeElapsed * (0x100 - struct66.newPortamento_60) / 10 / struct66.portamentoTimeTotal_64;
                    cents = cents - portamentoTimeElapsed * struct66.newPortamento_60 * 192 / (struct66.portamentoTimeTotal_64 * 120) % 16;
                  } else {
                    //LAB_8004762c
                    final int portamentoTimeElapsed = (struct66.portamentoTimeTotal_64 - struct66.portamentoTimeRemaining_62) * struct66.newPortamento_60;
                    note = struct66._4e + portamentoTimeElapsed / 10 / struct66.portamentoTimeTotal_64;
                    cents = cents + portamentoTimeElapsed * 192 / (struct66.portamentoTimeTotal_64 * 120) % 16;
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
                  struct124._11c = note;

                  if(struct66.portamentoTimeRemaining_62 == 0) {
                    struct66._4e = note;
                    struct66.portamentoChanging_44 = false;
                  }
                }
              }
            }

            //LAB_80047754
            //LAB_80047758
            final int pitch;
            if(struct66._42 == 1 || struct124._104 == 1) {
              //LAB_80047794
              pitch = struct124.pitch_0ec;
            } else {
              pitch = 0x1000;
            }

            //LAB_800477a0
            //LAB_800477a4
            voice.ADPCM_SAMPLE_RATE.set(pitch * this.calculateSampleRate(rootKey, note, cents, pitchBend, t3) >> 12);
          }

          //LAB_800477ec
          if(struct66._1a == 1) {
            if(struct66.volumeChanging_46 || struct66.panChanging_48 || struct124._105 == 1) {
              //LAB_80047844
              //LAB_80047848
              if(struct66.volumeChanging_46) {
                final int newVolume = struct66.newVolume_50;

                if(newVolume == struct66.previousVolume_52) {
                  struct66.volumeChanging_46 = false;
                } else {
                  if(struct66.remainingVolumeChangeTime_54 != 0) {
                    struct66.volume_2c = this.interpolate(struct66.newVolume_50, struct66.previousVolume_52, struct66.totalVolumeChangeTime_56, struct66.remainingVolumeChangeTime_54);
                    struct66.remainingVolumeChangeTime_54--;
                  } else {
                    //LAB_800478c8
                    struct66.volume_2c = newVolume;
                    struct66.volumeChanging_46 = false;
                  }
                }
              }

              //LAB_800478d0
              //LAB_800478d4
              if(struct66.panChanging_48) {
                final int newPan = struct66.newPan_58;

                if(newPan == struct66.previousPan_5a) {
                  struct66.panChanging_48 = false;
                } else {
                  //LAB_8004791c
                  if(struct66.remainingPanTime_5c != 0) {
                    struct66.pan_4c = this.interpolate(struct66.newPan_58, struct66.previousPan_5a, struct66.totalPanTime_5e, struct66.remainingPanTime_5c);
                    struct66.remainingPanTime_5c--;
                  } else {
                    //LAB_8004795c
                    struct66.pan_4c = newPan;
                    struct66.panChanging_48 = false;
                  }

                  //LAB_80047964
                  struct66._30[0] = panVolume_80059f3c.get(struct66.pan_4c >>> 2).left_00.get();
                  struct66._30[1] = panVolume_80059f3c.get(struct66.pan_4c >>> 2).right_01.get();
                }
              }

              //LAB_800479c4
              int l = FUN_8004ae94(voiceIndex, 0);
              int r = FUN_8004ae94(voiceIndex, 1);
              if(struct66._42 == 1 || struct124._105 == 1) {
                //LAB_80047a24
                l = this.scaleValue12((short)l, (short)struct124.pitchShiftVolLeft_0ee);
                r = this.scaleValue12((short)r, (short)struct124.pitchShiftVolRight_0f0);
              }

              //LAB_80047a44
              if(struct44.mono_36) {
                r = Math.max(l, r);
                l = r;
              }

              //LAB_80047a6c
              voice.LEFT.set(l);
              voice.RIGHT.set(r);
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
  public void FUN_80047b38(final int channelIndex) {
    final SpuStruct124 spu124 = _800c4ac8[channelIndex];
    final Sshd sshd = playableSoundPtrArr_800c43d0[spu124.playableSoundIndex_020].sshdPtr_04;

    _800c6630.sshdPtr_08 = sshd;
    sshdPtr_800c4ac0 = sshd;
    volumeRamp_800c4ab0 = sshd.getSubfile(1, VolumeRamp::new);

    final Sshd.Subfile subfile = sshd.getSubfile(spu124._02a * 4);

    if(subfile == null || subfile instanceof Subfile0) {
      final Subfile0 v1 = sshd.getSubfile(spu124._02a * 4, Subfile0::new);
      subfile0_800c4aa8 = v1;
      subfile0_800c4aac = v1;
      sssqish_800c4aa8 = null;
    } else {
      subfile0_800c4aa8 = null;
      subfile0_800c4aac = null;
      sssqish_800c4aa8 = (Sssqish)sshd.getSubfile(spu124._02a * 4);
    }
  }

  @Method(0x80047bd0L)
  public boolean FUN_80047bd0(final int channelIndex) {
    final SpuStruct124 struct124 = _800c4ac8[channelIndex];

    if(struct124._028 == 0 && struct124._02a == 1 && sshdPtr_800c4ac0.hasSubfile(4)) {
      final Sssqish sssq = _800c6630.sshdPtr_08.getSubfile(4, (data, offset) -> new Sssqish(data, offset, _800c6630.sshdPtr_08.getSubfileSize(4)));
      final int data = _800c4ac8[channelIndex].sssqReader_010.readByte(3);

      if(data < sssqish_800c4aa8.entries2_190.entries_02.length) {
        sublist_800c6674 = sssqish_800c4aa8.entries2_190.entries_02[data];
        sshd10Arr_800c6678 = sublist_800c6674.entry_08;
        sshd10Index_800c6678 = 0;
        sshd10_800c6678 = sshd10Arr_800c6678[sshd10Index_800c6678];
      } else {
        sublist_800c6674 = null;
        sshd10Arr_800c6678 = null;
        sshd10Index_800c6678 = 0;
        sshd10_800c6678 = null;
      }

      sssqReader_800c667c = null; //sssq.reader(); TODO?
      sssqEntry_800c6680 = sssq.entries_10[channelIndex];
      return true;
    }

    //LAB_80047cd0
    if(struct124._028 == 0) {
      return false;
    }

    if(struct124._02a == 1) {
      //LAB_80047cf0
      return false;
    }

    //LAB_80047cf8
    final int command = struct124.command_000;
    final int channel = command & 0xf;
    final int patchNumber = struct124.sssqReader_010.entry(channel).patchNumber_02;

    if(command < 0xa0) {
      if(subfile0_800c4aa8.entries_02[patchNumber] == null) {
        return false;
      }

      if(subfile0_800c4aa8.count_00 < patchNumber) {
        return false;
      }

      if(!sshdPtr_800c4ac0.hasSubfile(0)) {
        return false;
      }
    }

    //LAB_80047d7c
    //LAB_80047d80
    if(patchNumber != -1) {
      sublist_800c6674 = subfile0_800c4aa8.entries_02[patchNumber];
      sshd10Arr_800c6678 = subfile0_800c4aa8.entries_02[patchNumber].entry_08;
      sshd10Index_800c6678 = 0;
      sshd10_800c6678 = sshd10Arr_800c6678[sshd10Index_800c6678];
    }

    sssqReader_800c667c = struct124.sssqReader_010;
    sssqEntry_800c6680 = struct124.sssqReader_010.entry(channel);

    //LAB_80047e14
    return true;
  }

  @Method(0x80047e1cL)
  public int FUN_80047e1c() {
    //LAB_80047e34
    for(int i = 0; i < 24; i++) {
      //LAB_80047e4c
      if(_800c6630.voiceIndex_10 < 23) {
        _800c6630.voiceIndex_10++;
      } else {
        _800c6630.voiceIndex_10 = 0;
      }

      if(_800c3a40[_800c6630.voiceIndex_10]._10 == 0) {
        return _800c6630.voiceIndex_10;
      }
    }

    //LAB_80047ea0
    int a1 = 24;
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      if(_800c3a40[voiceIndex]._1a == 0 && _800c3a40[voiceIndex]._08 == 1) {
        final int v1 = _800c3a40[voiceIndex].voiceIndex_0a;

        if(v1 < a1) {
          a1 = v1;
          _800c6630.voiceIndex_10 = voiceIndex;
        }
      }

      //LAB_80047ef4
    }

    if(a1 == 24) {
      //LAB_80047f28
      for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
        if(_800c3a40[voiceIndex]._1a == 0) {
          final int v1 = _800c3a40[voiceIndex].voiceIndex_0a;

          if(v1 < 24) {
            //LAB_80047f84
            a1 = v1;
            _800c6630.voiceIndex_10 = voiceIndex;
            break;
          }
        }

        //LAB_80047f64
      }
    }

    if(a1 == 24) {
      _800c6630.voiceIndex_10 = -1;
      return -1;
    }

    //LAB_80047f90
    //LAB_80047fa0
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      if(a1 < _800c3a40[voiceIndex].voiceIndex_0a) {
        _800c3a40[voiceIndex].voiceIndex_0a--;
      }

      //LAB_80047fd0
    }

    _800c6630.voiceIndex_00--;

    //LAB_80047ff4
    return _800c6630.voiceIndex_10;
  }

  @Method(0x80048000L)
  public int FUN_80048000(final int minKeyRange, final int maxKeyRange, final long playableSoundIndex) {
    if(minKeyRange != 0) {
      //LAB_8004802c
      for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
        if(_800c3a40[voiceIndex]._1a == 1 && _800c3a40[voiceIndex].minKeyRange_20 == minKeyRange && _800c3a40[voiceIndex].playableSoundIndex_22 == playableSoundIndex) {
          //LAB_80048080
          for(int voiceIndex2 = 0; voiceIndex2 < 24; voiceIndex2++) {
            final int v1 = _800c3a40[voiceIndex].voiceIndex_0a;

            if(v1 < _800c3a40[voiceIndex2].voiceIndex_0a && v1 != 64) {
              _800c3a40[voiceIndex2].voiceIndex_0a--;
            }

            //LAB_800480cc
          }

          //LAB_80048260
          _800c6630.voiceIndex_00--;
          return voiceIndex;
        }

        //LAB_800480f0
      }
    }

    int t2 = 0;

    //LAB_80048108
    if(_800c6630._0d >= _800c6630._03) {
      //LAB_80048134
      for(int i = 0; i < 24; i++) {
        int t1 = 24;

        //LAB_80048144
        for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
          if(_800c3a40[voiceIndex]._1a == 1) {
            final int v1 = _800c3a40[voiceIndex].voiceIndex_0a;

            if(v1 >= i && v1 < t1) {
              t1 = v1;
              t2 = voiceIndex;
            }
          }

          //LAB_800481a0
        }

        if(_800c3a40[t2].maxKeyRange_1e <= maxKeyRange) {
          //LAB_800481fc
          for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
            final int v1 = _800c3a40[voiceIndex].voiceIndex_0a;

            if(v1 > t1 && v1 != -1) {
              _800c3a40[voiceIndex].voiceIndex_0a--;
            }

            //LAB_80048240
          }

          //LAB_80048260
          _800c6630.voiceIndex_00--;
          return (short)t2;
        }

        //LAB_8004826c
      }

      return -1;
    }

    //LAB_800482a0
    //LAB_800482a8
    for(int i = 0; i < 24; i++) {
      if(_800c6630.voiceIndex_10 < 23) {
        _800c6630.voiceIndex_10++;
      } else {
        _800c6630.voiceIndex_10 = 0;
      }

      //LAB_800482c0
      if(!_800c3a40[_800c6630.voiceIndex_10].used_00) {
        //LAB_8004828c
        _800c6630._0d++;
        return _800c6630.voiceIndex_10;
      }
    }

    int t3 = -1;
    int t1 = 24;

    //LAB_80048320
    jmp_80048478:
    {
      for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
        if(_800c3a40[voiceIndex]._08 == 1 && _800c3a40[voiceIndex]._1a != 1) {
          //LAB_8004836c
          for(int voiceIndex2 = voiceIndex; voiceIndex2 < 24; voiceIndex2++) {
            if(_800c3a40[voiceIndex2]._08 == 1 && _800c3a40[voiceIndex2]._1a != 1) {
              final int v1 = _800c3a40[voiceIndex2].voiceIndex_0a;
              if(v1 < t1) {
                t1 = v1;
                t3 = voiceIndex2;
              }
            }

            //LAB_800483c8
          }

          break jmp_80048478;
        }

        //LAB_800483e8
      }

      //LAB_80048414
      for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
        if(_800c3a40[voiceIndex]._1a != 1) {
          final int v1 = _800c3a40[voiceIndex].voiceIndex_0a;
          if(v1 < t1) {
            t1 = v1;
            t3 = voiceIndex;
          }
        }

        //LAB_80048460
      }
    }

    //LAB_80048478
    //LAB_8004847c
    //LAB_80048494
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      final int v1 = _800c3a40[voiceIndex].voiceIndex_0a;

      if(v1 > (short)t1 && v1 != -1) {
        _800c3a40[voiceIndex].voiceIndex_0a--;
      }

      //LAB_800484d8
    }

    _800c6630._0d++;
    _800c6630.voiceIndex_00--;

    //LAB_80048508
    return (short)t3;
  }

  @Method(0x80048514L)
  public void FUN_80048514(final int channelIndex) {
    long s3 = 0;
    long a3 = 0;

    //LAB_8004857c
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      if(_800c3a40[voiceIndex].used_00) {
        if(_800c3a40[voiceIndex]._1a == 1) {
          if(_800c3a40[voiceIndex].playableSoundIndex_22 == _800c4ac8[channelIndex].playableSoundIndex_020) {
            if(_800c3a40[voiceIndex]._3e == _800c4ac8[channelIndex].param2_005) {
              if(_800c3a40[voiceIndex].noteNumber_02 == _800c4ac8[channelIndex].param0_002) {
                if(_800c3a40[voiceIndex]._0c == 1) {
                  if(_800c3a40[voiceIndex].channelIndex_06 == channelIndex) {
                    s3 |= 0x1L << voiceIndex;
                  } else {
                    //LAB_8004861c
                    a3 |= 0x1L << voiceIndex;
                  }
                }
              }
            }
          }
        }
      }

      //LAB_80048620
    }

    if(s3 == 0) {
      s3 = a3;
    }

    //LAB_80048640
    //LAB_80048650
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      if((s3 & 0x1L << voiceIndex) != 0) {
        setKeyOff(channelIndex, voiceIndex);
        _800c3a40[voiceIndex]._08++;
      }

      //LAB_80048684
    }

    _800c4ac8[channelIndex].sssqReader_010.advance(4);
  }

  @Method(0x800486d4L)
  public void sssqHandleKeyOff(final int channelIndex) {
    final SpuStruct124 spu124 = _800c4ac8[channelIndex];

    //LAB_80048724
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      final SpuStruct66 spu66 = _800c3a40[voiceIndex];
      if(spu66.used_00) {
        if(spu66._1a == 0) {
          if(spu66.channelIndex_06 == channelIndex) {
            if(spu66.playableSoundIndex_22 == spu124.playableSoundIndex_020) {
              if(spu66.commandChannel_04 == (spu124.command_000 & 0xf)) {
                if(spu66.noteNumber_02 == spu124.param0_002) {
                  if(spu66._0c == 0) {
                    //LAB_800487d0
                    spu66._08 = 1;
                    //LAB_800487d4
                    spu66._18 = 0;
                  } else if(spu66._18 == 0) {
                    spu66._08 = 1;
                  } else {
                    //LAB_800487d4
                    spu66._18 = 0;
                  }

                  //LAB_800487d8
                  setKeyOff(channelIndex, voiceIndex);
                }
              }
            }
          }
        }
      }

      //LAB_800487e4
    }

    spu124.sssqReader_010.advance(3);
  }

  @Method(0x8004888cL)
  public void setNoiseMode(final int channelIndex, final int voiceIndex) {
    _800c6630.noiseMode_16 |= 1 << voiceIndex;
  }

  /** Checks if the note falls within the instrument's key range */
  @Method(0x80048938L)
  public boolean instrumentCanPlayNote(final int a0, final int instrumentIndex, final int note) {
    if(a0 == 0xff) {
      return true;
    }

    //LAB_80048950
    sshd10Index_800c6678 += instrumentIndex;
    sshd10_800c6678 = sshd10Arr_800c6678[sshd10Index_800c6678];

    final boolean ret = note >= sshd10_800c6678.minKeyRange_00 && note <= sshd10_800c6678.maxKeyRange_01;

    //LAB_80048988
    sshd10Index_800c6678 -= instrumentIndex;
    sshd10_800c6678 = sshd10Arr_800c6678[sshd10Index_800c6678];

    //LAB_80048990
    return ret;
  }

  /**
   * @param note 0-127, numeric representation of musical note, e.g. 60 = middle C
   */
  @Method(0x80048998L)
  public int calculateSampleRate(final int rootKey, final int note, final int cents, final int pitchBend, final int a4) {
    // There are 12 notes per octave, %12 is likely getting the note, and /12 the octave

    if(note < rootKey) {
      return (int)(_8005967c.offset(((12 - (rootKey - note) % 12) * 16 + a4 * (pitchBend - 64) / 4 + 0xd0L + cents) * 0x2L).get() >> ((rootKey - note) / 12 + 1));
    }

    //LAB_80048a38
    return (int)(_8005967c.offset(((note - rootKey) % 12 * 16 + a4 * (pitchBend - 64) / 4 + 0xd0L + cents) * 0x2L).get() << (note - rootKey) / 12);
  }

  @Method(0x80048ab8L)
  public int calculateVolume(final int channelIndex, final int pan, final int leftRight) {
    final int volume = sssqEntry_800c6680.volume_0e
      * sublist_800c6674.patchVolume_01
      * volumeRamp_800c4ab0.ramp_02[_800c4ac8[channelIndex].param1_003]
      * sshd10_800c6678.volume_0b
      / 0x4000
      * panVolume_80059f3c.get((pan / 2 & 0x7ffe) / 2).val_00.get(leftRight).get()
      / 0x80;

    //LAB_80048b88
    return sshd10_800c6678._0a == 0 ? volume : sshd10_800c6678._0a << 8 | volume >> 7;
  }

  @Method(0x80048b90L)
  public int calculatePan(final int a0, final int instrumentIndex) {
    sshd10Index_800c6678 += instrumentIndex;
    sshd10_800c6678 = sshd10Arr_800c6678[sshd10Index_800c6678];

    final int pan;
    if(a0 == 4) {
      pan = sshd10_800c6678.pan_0c;
    } else {
      //LAB_80048bc4
      pan = (int)_80059b3c.offset(_80059b3c.offset(sshd10_800c6678.pan_0c / 4 + sublist_800c6674.pan_02 / 4 * 0x20L).get() / 4 + sssqEntry_800c6680.pan_04 / 4 * 0x20L).get();
    }

    //LAB_80048c1c
    sshd10Index_800c6678 -= instrumentIndex;
    sshd10_800c6678 = sshd10Arr_800c6678[sshd10Index_800c6678];

    return pan;
  }

  @Method(0x80048eb8L)
  public void sssqHandleEndOfTrack(final int channelIndex) {
    final SpuStruct124 spu124 = _800c4ac8[channelIndex];

    if(spu124._02a != 0) {
      spu124._0e7 = 1;
      spu124.deltaTime_118 = 0;
      spu124._02a = 0;
      spu124._104 = 0;
      spu124._105 = 0;
      spu124.pitchShifted_0e9 = 0;
      spu124.reverbEnabled_0ea = 0;
    } else {
      //LAB_80048f0c
      spu124.sssqReader_010.jump(0x110);
      spu124._028 = 0;
    }

    //LAB_80048f18
    //LAB_80048f30
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      final SpuStruct66 spu66 = _800c3a40[voiceIndex];

      if(spu66.channelIndex_06 == channelIndex) {
        if(spu66._1a == 0) {
          spu66.modulationEnabled_14 = false;
          spu66.pitchBend_38 = 64;
        }
      }

      //LAB_80048f74
    }

    spu124.previousCommand_001 = spu124.command_000;
  }

  @Method(0x80048f98L)
  public void sssqHandleTempo(final int channelIndex) {
    final SpuStruct124 spu124 = _800c4ac8[channelIndex];
    spu124.tempo_108 = spu124.sssqReader_010.readShort(2);
    spu124.sssqReader_010.advance(4);
  }

  @Method(0x80048fecL)
  public void sssqHandleProgramChange(final int channelIndex) {
    final SpuStruct124 spu124 = _800c4ac8[channelIndex];

    if(spu124._02a == 0) {
      sssqEntry_800c6680.patchNumber_02 = spu124.sssqReader_010.readByte(1); // Read patch number
      sssqEntry_800c6680.pitchBend_0a = 0x40;
      sssqEntry_800c6680._0b = 0x40;
    }

    //LAB_80049058
    spu124.sssqReader_010.advance(2);
  }

  @Method(0x8004906cL)
  public void sssqHandleModulationWheel(final int channelIndex) {
    final SpuStruct124 spu124 = _800c4ac8[channelIndex];

    if(spu124._02a != 0) {
      //LAB_800490b8
      for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
        final SpuStruct66 spu66 = _800c3a40[voiceIndex];

        if(spu66._1a == 1) {
          if(spu66._3e == spu124.sssqReader_010.readByte(3)) {
            if(spu66.noteNumber_02 == spu124.sssqReader_010.readByte(4)) {
              if(spu66.playableSoundIndex_22 == spu124.playableSoundIndex_020) {
                if(spu66.channelIndex_06 == channelIndex) {
                  if(spu66.used_00) {
                    spu66.modulationEnabled_14 = true;
                    spu66.modulation_16 = spu124.sssqReader_010.readByte(2);
                  }
                }
              }
            }
          }
        }

        //LAB_80049150
      }

      spu124.sssqReader_010.advance(5);
      return;
    }

    //LAB_80049178
    sssqEntry_800c6680.modulation_09 = spu124.sssqReader_010.readByte(2);

    //LAB_800491b0
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      final SpuStruct66 spu66 = _800c3a40[voiceIndex];
      if(spu66.commandChannel_04 == (spu124.command_000 & 0xf)) {
        if(spu66.playableSoundIndex_22 == spu124.playableSoundIndex_020) {
          if(spu66.channelIndex_06 == channelIndex) {
            if(spu66.used_00) {
              spu66.modulationEnabled_14 = true;
              spu66.modulation_16 = spu124.sssqReader_010.readByte(2);
            }
          }
        }
      }

      //LAB_80049228
    }

    spu124.sssqReader_010.advance(3);
  }

  @Method(0x80049250L)
  public void sssqHandleBreathControl(final int channelIndex) {
    final SpuStruct124 spu124 = _800c4ac8[channelIndex];

    final int breath = 240 / (60 - spu124.sssqReader_010.readByte(2) * 58 / 127);

    //LAB_800492dc
    //LAB_800492f4

    if(spu124._02a != 0) {
      //LAB_80049318
      for(int i = 0; i < 24; i++) {
        final SpuStruct66 spu66 = _800c3a40[i];

        if(spu66.commandChannel_04 == (spu124.command_000 & 0xf)) {
          if(spu66._3e == spu124.sssqReader_010.readByte(3)) {
            if(spu66.noteNumber_02 == spu124.sssqReader_010.readByte(4)) {
              if(spu66.playableSoundIndex_22 == spu124.playableSoundIndex_020) {
                if(spu66.channelIndex_06 == channelIndex) {
                  if(spu66.used_00) {
                    spu66.breath_3c = breath;
                  }
                }
              }
            }
          }
        }

        //LAB_800493ac
      }

      spu124.sssqReader_010.advance(5);
      return;
    }

    //LAB_800493d4
    sssqEntry_800c6680.breath_0c = breath;

    //LAB_800493f4
    for(int i = 0; i < 24; i++) {
      final SpuStruct66 spu66 = _800c3a40[i];

      if(spu66.commandChannel_04 == (spu124.command_000 & 0xf)) {
        if(spu66.playableSoundIndex_22 == spu124.playableSoundIndex_020) {
          if(spu66.channelIndex_06 == channelIndex) {
            if(spu66.used_00) {
              spu66.breath_3c = breath;
            }
          }
        }
      }

      //LAB_80049458
    }

    spu124.sssqReader_010.advance(3);
  }

  @Method(0x80049480L)
  public void sssqHandlePortamento(final int channelIndex) {
    final SpuStruct44 struct44 = _800c6630;
    final SpuStruct124 struct124 = _800c4ac8[channelIndex];

    //LAB_800494d0
    for(int t2 = 0; t2 < 24; t2++) {
      final SpuStruct66 struct66 = _800c3a40[t2];

      if(struct66.used_00) {
        if(struct66._1a == 1) {
          if(struct66.playableSoundIndex_22 == struct124.playableSoundIndex_020) {
            if(struct66._3e == struct124.sssqReader_010.readByte(4)) {
              if(struct66.noteNumber_02 == struct124.sssqReader_010.readByte(5)) {
                if(struct66.channelIndex_06 == channelIndex) {
                  if(struct66.portamentoTimeRemaining_62 != 0) {
                    struct66._4e = struct124._11c;
                  }

                  //LAB_80049578
                  struct66.portamentoChanging_44 = true;
                  struct66.newPortamento_60 = (byte)struct124.sssqReader_010.readByte(3);
                  struct66.portamentoTimeRemaining_62 = struct124.sssqReader_010.readByte(2) * 4 * struct44.ticksPerSecond_42 / 60;
                  struct66.portamentoTimeTotal_64 = struct124.sssqReader_010.readByte(2) * 4 * struct44.ticksPerSecond_42 / 60;
                }
              }
            }
          }
        }
      }
    }

    struct124.sssqReader_010.advance(6);
  }

  @Method(0x80049638L)
  public void sssqHandleVolume(final int channelIndex) {
    final SpuStruct124 spu124 = _800c4ac8[channelIndex];
    final SpuStruct44 spu44 = _800c6630;

    if(spu124._02a != 0) {
      //LAB_800496bc
      for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
        final SpuStruct66 spu66 = _800c3a40[voiceIndex];

        if(spu66.used_00) {
          if(spu66._1a == 1) {
            if(spu66.playableSoundIndex_22 == spu124.playableSoundIndex_020) {
              if(spu66._3e == spu124.sssqReader_010.readByte(4)) {
                if(spu66.noteNumber_02 == spu124.sssqReader_010.readByte(5)) {
                  if(spu66.channelIndex_06 == channelIndex) {
                    spu66.volumeChanging_46 = true;
                    spu66.previousVolume_52 = spu66.volume_2c;
                    spu66.newVolume_50 = spu124.sssqReader_010.readByte(3);
                    spu66.remainingVolumeChangeTime_54 = spu124.sssqReader_010.readByte(2) * 4 * spu44.ticksPerSecond_42 / 60;
                    spu66.totalVolumeChangeTime_56 = spu124.sssqReader_010.readByte(2) * 4 * spu44.ticksPerSecond_42 / 60;
                  }
                }
              }
            }
          }
        }

        //LAB_800497dc
      }

      //LAB_80049950
      spu124.sssqReader_010.advance(6);
    } else {
      //LAB_800497fc
      sssqEntry_800c6680.volume_03 = spu124.sssqReader_010.readByte(2);
      sssqEntry_800c6680.volume_0e = sssqReader_800c667c.baseVolume() * sssqEntry_800c6680.volume_03 / 0x80;

      //LAB_8004985c
      for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
        final SpuStruct66 spu66 = _800c3a40[voiceIndex];

        if(spu66.used_00) {
          if(spu66.commandChannel_04 == (spu124.command_000 & 0xf)) {
            if(spu66.playableSoundIndex_22 == spu124.playableSoundIndex_020) {
              if(spu66._08 != 1) {
                if(spu66.channelIndex_06 == channelIndex) {
                  spu124.param1_003 = spu66.volume_2c;
                  voicePtr_800c4ac4.deref().voices[voiceIndex].LEFT.set(this.calculateVolume(spu66.channelIndex_06, this.calculatePan(0, spu66.instrumentIndex_0e), 0));
                  voicePtr_800c4ac4.deref().voices[voiceIndex].RIGHT.set(this.calculateVolume(spu66.channelIndex_06, this.calculatePan(0, spu66.instrumentIndex_0e), 1));
                }
              }
            }
          }
        }

        //LAB_80049930
      }

      //LAB_80049950
      spu124.sssqReader_010.advance(3);
    }
  }

  @Method(0x80049980L)
  public void sssqHandlePan(final int channelIndex) {
    assert channelIndex >= 0;

    final SpuStruct124 spu124 = _800c4ac8[channelIndex];
    final SpuStruct44 spu44 = _800c6630;

    if(spu124._02a != 0) {
      //LAB_80049a08
      for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
        final SpuStruct66 spu66 = _800c3a40[voiceIndex];
        if(spu66.used_00) {
          if(spu66._1a == 1) {
            if(spu66.playableSoundIndex_22 == spu124.playableSoundIndex_020) {
              if(spu66._3e == spu124.sssqReader_010.readByte(4)) {
                if(spu66.noteNumber_02 == spu124.sssqReader_010.readByte(5)) {
                  if(spu66.channelIndex_06 == channelIndex) {
                    spu66.panChanging_48 = true;
                    spu66.previousPan_5a = spu66.pan_4c;
                    spu66.newPan_58 = spu124.sssqReader_010.readByte(3);
                    spu66.totalPanTime_5e = spu124.sssqReader_010.readByte(2) * 4 * spu44.ticksPerSecond_42 / 60;
                    spu66.remainingPanTime_5c = spu66.totalPanTime_5e;
                  }
                }
              }
            }
          }
        }

        //LAB_80049b28
      }

      //LAB_80049c88
      spu124.sssqReader_010.advance(6);
    } else {
      //LAB_80049b48
      sssqEntry_800c6680.pan_04 = spu124.sssqReader_010.readByte(2);

      //LAB_80049b80
      for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
        final SpuStruct66 spu66 = _800c3a40[voiceIndex];
        if(spu66.commandChannel_04 == (spu124.command_000 & 0xf)) {
          if(spu66.playableSoundIndex_22 == spu124.playableSoundIndex_020) {
            if(spu66.channelIndex_06 == channelIndex) {
              if(spu66._08 != 1) {
                if(spu66.used_00) {
                  spu66.pan_4c = spu124.sssqReader_010.readByte(2);
                  spu124.param1_003 = spu66.volume_2c;
                  voicePtr_800c4ac4.deref().voices[voiceIndex].LEFT.set(this.calculateVolume(channelIndex, this.calculatePan(0, spu66.instrumentIndex_0e), 0));
                  voicePtr_800c4ac4.deref().voices[voiceIndex].RIGHT.set(this.calculateVolume(channelIndex, this.calculatePan(0, spu66.instrumentIndex_0e), 1));
                }
              }
            }
          }
        }

        //LAB_80049c68
      }

      //LAB_80049c88
      spu124.sssqReader_010.advance(3);
    }
  }

  @Method(0x80049cbcL)
  public void sssqHandleSustain(final int channelIndex) {
    assert false;
  }

  @Method(0x80049e2cL)
  public void FUN_80049e2c(final int channelIndex) {
    final SpuStruct124 struct124 = _800c4ac8[channelIndex];

    final int a0 = struct124.sssqReader_010.readByte(4);

    if(a0 == 0) {
      //LAB_80049ecc
      struct124.repeatOffset_02c = struct124.sssqReader_010.readShort(2);
      struct124.repeatDestCommand_039 = struct124.sssqReader_010.readByteAbsolute(struct124.repeatOffset_02c);
      struct124.repeat_037 = true;
      struct124._0e6 = 1;
    } else if(a0 != struct124.repeatCounter_035) {
      //LAB_80049ea0
      struct124.repeatOffset_02c = struct124.sssqReader_010.readShort(2);
      struct124.repeatDestCommand_039 = struct124.sssqReader_010.readByteAbsolute(struct124.repeatOffset_02c);
      struct124.repeatCounter_035++;
      struct124.repeat_037 = true;
      struct124._0e6 = 1;
    } else {
      struct124.repeatCounter_035 = 0;
      struct124.repeat_037 = false;
      struct124._0e6 = 0;
    }

    //LAB_80049f00
    struct124.sssqReader_010.advance(5);
  }

  @Method(0x80049f14L)
  public void sssqHandleDataEntry(final int channelIndex) {
    final SpuStruct124 spu124 = _800c4ac8[channelIndex];

    switch(spu124.nrpn_11f) {
      case 0 -> {
        spu124.repeatCount_11d = spu124.param1_003;
        spu124.sssqReader_010.advance(3);
        return;
      }

      //LAB_8004a050
      case 4 -> // Attack (linear)
        sshd10_800c6678.adsrLo_06 = sshd10_800c6678.adsrLo_06
          & 0xff
          | 0x7f - spu124.param1_003 << 8;

      case 5 -> // Attack (exponential)
        sshd10_800c6678.adsrLo_06 = sshd10_800c6678.adsrLo_06
          & 0xff
          | 0x7f - spu124.param1_003 << 8
          | 0x8000;

      case 6 -> // Decay shift
        sshd10_800c6678.adsrLo_06 = sshd10_800c6678.adsrLo_06
          & 0xff0f
          | (0x7f - spu124.param1_003) / 0x8 << 4;

      //LAB_8004a050
      case 7 -> // Sustain level
        sshd10_800c6678.adsrLo_06 = sshd10_800c6678.adsrLo_06
          & 0xfff0
          | spu124.param1_003 / 0x8;

      //LAB_8004a114
      case 8 -> // Sustain (linear) (everything but level)
        sshd10_800c6678.adsrHi_08 = sshd10_800c6678.adsrHi_08
          & 0x3f
          | (0x7f - spu124.param1_003) << 6
          | 0x4000 - spu124.sustainDirection_122; // Direction (0 = increase, 1 = decrease)

      //LAB_8004a114
      case 9 -> // Sustain (exponential) (everything but level)
        sshd10_800c6678.adsrHi_08 = sshd10_800c6678.adsrHi_08
          & 0x3f
          | (0x7f - spu124.param1_003) << 6
          | 0x4000 - spu124.sustainDirection_122 // Direction (0 = increase, 1 = decrease)
          | 0x8000;

      //LAB_8004a114
      case 0xa -> // Release (linear)
        sshd10_800c6678.adsrHi_08 = sshd10_800c6678.adsrHi_08
          & 0xffc0
          | (0x7f - spu124.param1_003) / 4;

      case 0xb -> // Release (exponential)
        sshd10_800c6678.adsrHi_08 = sshd10_800c6678.adsrHi_08
          & 0xffc0
          | (0x7f - spu124.param1_003) / 4
          | 0x20;

      case 0xc -> { // Sustain direction
        if(spu124.param1_003 > 0x40) {
          spu124.sustainDirection_122 = 0x4000; // Increase
        } else {
          //LAB_8004a178
          spu124.sustainDirection_122 = 0; // Decrease
        }
      }

      case 0xf -> { // Reverb type
        sssqSetReverbType(spu124.param1_003);
        spu124.sssqReader_010.advance(3);
        return;
      }

      case 0x10 -> { // Reverb volume
        sssqSetReverbVolume(spu124.param1_003, spu124.param1_003);
        spu124.sssqReader_010.advance(3);
        return;
      }

      case 0x11 -> {
        this.FUN_8004c690(spu124.param1_003);
        spu124.sssqReader_010.advance(3);
        return;
      }

      case 0x12, 0x13 -> {
        this.FUN_8004c5e8(spu124.param1_003);
        spu124.sssqReader_010.advance(3);
        return;
      }
    }

    //LAB_8004a1d0
    //LAB_8004a1f0
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      final SpuStruct66 spu66 = _800c3a40[voiceIndex];

      if(spu66.noteNumber_02 == 1) {
        if(spu66.commandChannel_04 == (spu124.command_000 & 0xf)) {
          if(spu66._1a == 0) {
            if(spu66.channelIndex_06 == channelIndex) {
              if(spu124.instrumentIndex_120 == 0xff || spu66.instrumentIndex_0e == spu124.instrumentIndex_120) {
                //LAB_8004a274
                voicePtr_800c4ac4.deref().voices[voiceIndex].ADSR_LO.set(sshd10_800c6678.adsrLo_06);
                voicePtr_800c4ac4.deref().voices[voiceIndex].ADSR_HI.set(sshd10_800c6678.adsrHi_08);
              }
            }
          }
        }
      }

      //LAB_8004a28c
    }

    //LAB_8004a2a0
    spu124.sssqReader_010.advance(3);
  }

  @Method(0x8004a2c0L)
  public void sssqDataEntryLsb(final int channelIndex) {
    final SpuStruct124 spu124 = _800c4ac8[channelIndex];
    final int type = spu124.lsbType_11e;

    if(type == 0) {
      //LAB_8004a31c
      spu124.nrpn_11f = 0;
      spu124.repeatCount_11d = spu124.param1_003;
    } else if(type == 1 || type == 2) {
      //LAB_8004a32c
      spu124.nrpn_11f = spu124.param1_003;
    }

    //LAB_8004a338
    spu124.sssqReader_010.advance(3);
  }

  @Method(0x8004a34cL)
  public void sssqDataEntryMsb(final int channelIndex) {
    final SpuStruct124 spu124 = _800c4ac8[channelIndex];

    final int nrpn = spu124.param1_003; // NRPN
    if(nrpn >= 0 && nrpn < 0x10) {
      //LAB_8004a44c
      spu124.lsbType_11e = 0x10;
      spu124.instrumentIndex_120 = spu124.param1_003;
    } else if(nrpn == 0x10) {
      //LAB_8004a430
      spu124.lsbType_11e = 1;
      //LAB_8004a398
    } else if(nrpn == 0x14) { // Set repeat offset
      //LAB_8004a3cc
      spu124.lsbType_11e = 0;
      spu124.nrpn_11f = 0;
      spu124.repeatDestCommand_039 = spu124.command_000;
      spu124.repeatOffset_02c = spu124.sssqReader_010.offset();
    } else if(nrpn == 0x1e) { // Repeat
      spu124.lsbType_11e = 0;

      //LAB_8004a3e8
      if(spu124.repeatCount_11d == 0x7f) { // Simple repeat
        //LAB_8004a424
        spu124.repeat_037 = true;
      } else if(spu124.repeatCounter_035 < spu124.repeatCount_11d) { // Repeat n times
        //LAB_8004a41c
        spu124.repeatCounter_035++;
        spu124.repeat_037 = true;
      } else { // Repeat finished
        spu124.repeatOffset_02c = 0;
        spu124.repeatCounter_035 = 0;
        spu124.repeat_037 = false;
      }

      //LAB_8004a428
      //LAB_8004a3b8
    } else if(nrpn == 0x7f) {
      //LAB_8004a43c
      spu124.lsbType_11e = 2;
      spu124.instrumentIndex_120 = 0xff;
    }

    //LAB_8004a458
    spu124.sssqReader_010.advance(3);
  }

  @Method(0x8004a46cL)
  public void sssqHandlePitchBend(final int channelIndex) {
    final SpuStruct124 spu124 = _800c4ac8[channelIndex];

    sssqEntry_800c6680.pitchBend_0a = spu124.sssqReader_010.readByte(1);

    //LAB_8004a4e4
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      final SpuStruct66 spu66 = _800c3a40[voiceIndex];
      if(spu66.commandChannel_04 == (spu124.command_000 & 0xf)) {
        if(spu66.playableSoundIndex_22 == spu124.playableSoundIndex_020) {
          if(spu66.channelIndex_06 == channelIndex) {
            if(spu66.used_00) {
              voicePtr_800c4ac4.deref().voices[voiceIndex].ADPCM_SAMPLE_RATE.set(this.calculateSampleRate(spu66.rootKey_40, spu66.noteNumber_02, spu66.cents_36, sssqEntry_800c6680.pitchBend_0a, spu66._3a));
              spu66.pitchBend_38 = spu124.sssqReader_010.readByte(1);
            }
          }
        }
      }

      //LAB_8004a598
    }

    spu124.sssqReader_010.advance(2);
  }

  @Method(0x8004a5e0L)
  public void sssqReadDeltaTime(final int channelIndex) {
    final SpuStruct124 spu124 = _800c4ac8[channelIndex];

    //LAB_8004a618
    // Read varint
    for(int i = 0; i < 4; i++) {
      final int varint = spu124.sssqReader_010.readByte(0);
      spu124.sssqReader_010.advance();
      spu124.deltaTime_118 <<= 7;

      if((varint & 0x80) == 0) {
        //LAB_8004a720
        spu124.deltaTime_118 |= varint;
        break;
      }

      spu124.deltaTime_118 |= varint & 0x7f;
    }

    //LAB_8004a664
    if(spu124._02a != 0) {
      spu124.tempo_108 = 60;
      spu124.ticksPerQuarterNote_10a = 480;
    }

    //LAB_8004a680
    if(spu124.tempo_108 != 0) {
      spu124._114 = spu124.deltaTime_118 * 10;

      //LAB_8004a6e8
      //LAB_8004a700
      // 1-decimal fixed-point
      final int msPerTick = spu124.tempo_108 * spu124.ticksPerQuarterNote_10a * 10 / (_800c6630.ticksPerSecond_42 * 60);
      int v1;
      if(spu124._10c == 0) {
        //LAB_8004a72c
        v1 = spu124._110;
      } else {
        spu124._10c = 0;
        v1 = -(msPerTick - spu124._110);
      }

      v1 += spu124._114;

      //LAB_8004a738
      //LAB_8004a748
      //LAB_8004a760
      final int a0 = v1 % msPerTick;

      if(msPerTick < a0 * 2) {
        spu124._114 = v1 + msPerTick - a0;
        spu124._10c = 1;
      } else {
        //LAB_8004a78c
        spu124._114 = v1 - a0;
      }

      //LAB_8004a794
      //LAB_8004a7a4
      //LAB_8004a7bc
      //LAB_8004a7d8
      spu124._110 = a0;
      spu124.deltaTime_118 = spu124._114 / msPerTick;
    }

    //LAB_8004a7e4
  }

  @Method(0x8004a7ecL)
  public void sssqReadEvent(final int channelIndex) {
    final SpuStruct124 spu124 = _800c4ac8[channelIndex];

    final int command = spu124.sssqReader_010.readByte(0);
    if((command & 0x80) != 0) { // If this is a command
      spu124.command_000 = command;
      spu124.previousCommand_001 = command;
    } else { // Otherwise, repeat the previous command with new parameters
      //LAB_8004a854
      spu124.sssqReader_010.rewind();
      spu124.command_000 = spu124.previousCommand_001;
    }

    //LAB_8004a860
    spu124.param0_002 = spu124.sssqReader_010.readByte(1);
    spu124.param1_003 = spu124.sssqReader_010.readByte(2);

    // Don't read the next byte if we get to the end of the sequence
    if(spu124.command_000 != 0xff || spu124.param0_002 != 0x2f || spu124.param1_003 != 0) {
      spu124.param2_005 = spu124.sssqReader_010.readByte(3);
    }
  }

  @Method(0x8004a8b8L)
  public void FUN_8004a8b8() {
    LAB_8004a8dc:
    for(int channelIndex = 0; channelIndex < 24; channelIndex++) {
      final SpuStruct124 spu124 = _800c4ac8[channelIndex];
      if(spu124._0e7 == 1 && spu124._029 == 1) {
        //LAB_8004a908
        for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
          if(_800c3a40[voiceIndex].channelIndex_06 == channelIndex) {
            continue LAB_8004a8dc;
          }
        }

        spu124.deltaTime_118 = 0;
        spu124._0e7 = 0;
        spu124.pitchShiftVolRight_0f0 = 0;
        spu124.pitchShiftVolLeft_0ee = 0;
        spu124._104 = 0;
        spu124._105 = 0;
        spu124.playableSoundIndex_020 = 0;
        spu124._02a = 0;
        spu124._029 = 0;
        _800c6630.pitchShifted_22 = false;
      }

      //LAB_8004a96c
    }

    //LAB_8004a99c
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      if((voicePtr_800c4ac4.deref().voices[voiceIndex].ADSR_CURR_VOL.get() & 0x7fff) < 16) {
        final SpuStruct66 spu66 = _800c3a40[voiceIndex];
        if(spu66._08 == 1) {
          if(spu66._1a != 0 && _800c6630._0d > 0) {
            _800c6630._0d--;
          }

          //LAB_8004aa04
          //LAB_8004aa0c
          for(int voiceIndex2 = 0; voiceIndex2 < 24; voiceIndex2++) {
            if(_800c3a40[voiceIndex2].voiceIndex_0a > spu66.voiceIndex_0a && _800c3a40[voiceIndex2].voiceIndex_0a != -1) {
              _800c3a40[voiceIndex2].voiceIndex_0a--;
            }

            //LAB_8004aa48
          }

          //LAB_8004aa7c
          spu66.clear();
          spu66.channelIndex_06 = -1;
          spu66.sequenceIndex_26 = -1;
          spu66.patchIndex_24 = -1;
          spu66.playableSoundIndex_22 = -1;
          spu66.voiceIndex_0a = -1;
          spu66._4e = 120;

          if(_800c6630.voiceIndex_00 > 0) {
            _800c6630.voiceIndex_00--;
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
  public void FUN_8004af98(final int channelIndex) {
    final SpuStruct124 spu124 = _800c4ac8[channelIndex];

    if(spu124._03c != 0) {
      int s4 = 0;

      if(spu124._03e[5][0] != 0) {
        sssqReader_800c667c = spu124.sssqReader_010;

        if(sssqReader_800c667c.baseVolume() != spu124._03e[6][0]) {
          if(spu124._03e[7][0] != 0) {
            sssqReader_800c667c.baseVolume(this.interpolate(spu124._03e[6][0], spu124._03e[9][0], spu124._03e[8][0], spu124._03e[7][0]));
            spu124._03e[7][0]--;
          } else {
            //LAB_8004b064
            sssqReader_800c667c.baseVolume(spu124._03e[6][0]);
          }

          //LAB_8004b068
          FUN_8004c8dc(channelIndex, sssqReader_800c667c.baseVolume());
          s4++;
        }
      }

      //LAB_8004b084
      //LAB_8004b088
      //LAB_8004b0a0
      for(int commandChannel = 0; commandChannel < 16; commandChannel++) {
        final Sssq.Entry entry = spu124.sssqReader_010.entry(commandChannel);

        if(entry.volume_03 != spu124._03e[1][commandChannel] && spu124._03e[0][commandChannel] == 1) {
          if(spu124._03e[2][commandChannel] != 0) {
            entry.volume_03 = this.interpolate(spu124._03e[1][commandChannel], spu124._03e[4][commandChannel], spu124._03e[3][commandChannel], spu124._03e[2][commandChannel]);
            spu124._03e[2][commandChannel]--;
          } else {
            //LAB_8004b110
            entry.volume_03 = spu124._03e[1][commandChannel];
          }

          //LAB_8004b114
          this.FUN_8004b464((short)channelIndex, (short)commandChannel, (short)entry.volume_03);
          s4++;
        }
      }

      if(s4 == 0) {
        //LAB_8004b15c
        for(int i = 0; i < 10; i++) {
          //LAB_8004b16c
          for(int n = 0; n < 16; n++) {
            spu124._03e[i][n] = 0;
          }
        }

        if(spu124._03a != 0) {
          spu124._03a = 0;
          FUN_8004d034(channelIndex, 1);
        }

        //LAB_8004b1c0
        spu124._03c = 0;
      }
    }

    //LAB_8004b1c4
  }

  @Method(0x8004b2c4L)
  public void FUN_8004b2c4() {
    final SpuStruct44 spu44 = _800c6630;

    if(spu44.fadingIn_2a) {
      if(sssqFadeCurrent_8005a1ce == spu44.fadeTime_2c) {
        sssqFadeCurrent_8005a1ce = 0;
        spu44.fadingIn_2a = false;
      } else {
        //LAB_8004b310
        //LAB_8004b33c
        //LAB_8004b354
        final int vol = spu44.fadeInVol_2e * sssqFadeCurrent_8005a1ce / spu44.fadeTime_2c;
        setMainVolume(vol, vol);
        sssqFadeCurrent_8005a1ce++;
      }
    }

    //LAB_8004b370
    if(spu44.fadingOut_2b) {
      if(sssqFadeCurrent_8005a1ce == spu44.fadeTime_2c) {
        sssqFadeCurrent_8005a1ce = 0;
        spu44.fadingOut_2b = false;
      } else {
        //LAB_8004b3a0
        //LAB_8004b3cc
        //LAB_8004b3e4
        //LAB_8004b410
        //LAB_8004b428
        final int l = spu44.fadeOutVolL_30 * (spu44.fadeTime_2c - sssqFadeCurrent_8005a1ce) / spu44.fadeTime_2c;
        final int r = spu44.fadeOutVolR_32 * (spu44.fadeTime_2c - sssqFadeCurrent_8005a1ce) / spu44.fadeTime_2c;
        setMainVolume(l, r);
        sssqFadeCurrent_8005a1ce++;
      }
    }

    //LAB_8004b450
  }

  @Method(0x8004b464L)
  public void FUN_8004b464(final short channelIndex, final short commandChannel, final short volume) {
    final SpuStruct124 struct124 = _800c4ac8[channelIndex];
    sssqReader_800c667c = struct124.sssqReader_010;
    final Sssq.Entry entry = struct124.sssqReader_010.entry(commandChannel);
    sssqEntry_800c6680 = entry;
    entry.volume_03 = volume;
    entry.volume_0e = volume * sssqReader_800c667c.baseVolume() >> 7;

    //LAB_8004b514
    for(int voiceIndex = 0; voiceIndex < 24; voiceIndex++) {
      final SpuStruct66 struct66 = _800c3a40[voiceIndex];

      if(struct66.used_00) {
        if(struct66._1a == 0) {
          if(struct66.playableSoundIndex_22 == struct124.playableSoundIndex_020) {
            if(struct66.channelIndex_06 == channelIndex) {
              if(struct66.commandChannel_04 == commandChannel) {
                FUN_8004ad2c(voiceIndex);
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
