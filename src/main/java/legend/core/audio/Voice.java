package legend.core.audio;

final class Voice implements AudioStream {
  private static final short[] EMPTY = new short[3];

  public final int index;
  private final BufferedSound sound;
  private final VoiceCounter counter = new VoiceCounter();


  private boolean empty = true;

  private MidiChannel channel;
  private MidiInstrument instrument;
  private MidiInstrumentLayer layer;
  private AdsrEnvelope adsrEnvelope;
  private MidiSoundFontEntry soundFontEntry;
  private int note;
  private double velocity;
  private int sampleRate;
  private int pitchBendMultiplier;
  private double volume;
  private byte[][] breathControls;
  private boolean isModulation;
  private int modulation;
  private int breath;
  private int playingNote_10;
  private int playingNote_12;
  private int playingNote_1c;
  private int playingNote_42;
  private int playingNote_4e;
  private boolean portamento;

  private boolean hasSamples;
  private final short[] samples = new short[31];

  Voice(final int index, final int bufferSize, final boolean stereo) {
    this.index = index;
    this.sound = new BufferedSound(bufferSize, stereo);
  }

  void tick(final boolean stereo) {
    if(this.empty) {
      for(int channel = 0; channel < (stereo ? 2 : 1); channel++) {
        this.sound.bufferSample((short) 0);
      }

      // We probably need to clear out the last samples for interpolation reasons,
      // but this should be verified.
      if(this.hasSamples) {
        System.arraycopy(EMPTY, 0, this.samples, 28, 3);

        this.hasSamples = false;
      }

      return;
    }

    this.variableParameters();

    final short sample = this.sampleVoice();

    final short adsrValue = this.adsrEnvelope.tick();

    final short actualVolume = (short) (adsrValue * this.volume);

    if(!this.adsrEnvelope.isAttack() && actualVolume <= 16) {
      System.out.printf("Clear [Voice: %d]%n", this.index);
      this.modulation = 0;
      this.isModulation = false;
      this.empty = true;
      this.breathControls = null;
      this.playingNote_10 = 0;
      this.playingNote_12 = 0;
      this.playingNote_1c = 0;
      this.playingNote_42 = 0;
      this.playingNote_4e = 0;
      this.breath = 0;
    }

    final short processedSample = (short)(((int)(sample * adsrValue * this.volume)) >> 15);

    if(this.sound.isStereo()) {
      final double leftPan = Offsets.pan[127 - this.layer.getPan()] * Offsets.pan[127 - this.channel.getPan()] * Offsets.pan[127 - this.instrument.getPan()];
      final double rightPan = Offsets.pan[this.layer.getPan()] * Offsets.pan[this.channel.getPan()] * Offsets.pan[this.instrument.getPan()];

      this.sound.bufferSample((short)(processedSample * leftPan));
      this.sound.bufferSample((short)(processedSample * rightPan));

      return;
    }

    this.sound.bufferSample(processedSample);
  }

  private short sampleVoice() {
    if(!this.hasSamples) {
      System.arraycopy(this.samples, 28, this.samples, 0, 3);

      final boolean isEnd = this.soundFontEntry.get(this.samples);

      if(isEnd) {
        this.adsrEnvelope.mute();
        this.empty = true;
        return 0;
      }

      this.hasSamples = true;
    }

    final int interpolationIndex = this.counter.getInterpolationIndex();
    final int sampleIndex = this.counter.getCurrentSampleIndex();

    int interpolated;

    interpolated  = gaussTable[0x0FF - interpolationIndex] * this.samples[sampleIndex];
    interpolated += gaussTable[0x1FF - interpolationIndex] * this.samples[sampleIndex + 1];
    interpolated += gaussTable[0x100 + interpolationIndex] * this.samples[sampleIndex + 2];
    interpolated += gaussTable[interpolationIndex] * this.samples[sampleIndex + 3];
    interpolated >>= 15;

    int step = this.sampleRate;

    //TODO voice modulation (see Spu sampleVoice)

    if(step > 0x3fff) {
      step = 0x4000;
    }

    if(this.counter.add(step)) {
      this.hasSamples = false;
    }

    return (short)interpolated;
  }

  @Override
  public void keyOn(final MidiChannel channel, final MidiInstrument instrument, final MidiInstrumentLayer layer, final int note, final int velocity, final byte[][] breathControls) {
    this.channel = channel;
    this.instrument = instrument;
    this.layer = layer;
    this.soundFontEntry = layer.getSoundFontEntry();
    this.adsrEnvelope = layer.getAdsrEnvelope();
    this.note = note;
    this.velocity = velocity / 127d;

    if(this.layer.canUseModulation() && this.channel.getModulation() != 0) {
      this.playingNote_10 = (this.layer.getFlags() & 0x40) != 0 ? this.instrument.get_05() : this.layer.get_0e();

      this.isModulation = true;
      this.modulation = this.channel.getModulation();
    } else {
      this.isModulation = false;
      this.modulation = 0;
    }

    this.breathControls = breathControls;
    this.breath = this.channel.getBreath();

    this.pitchBendMultiplier = layer.isPitchBendMultiplierFromInstrument() ? this.instrument.getPitchBendMultiplier() : layer.getPitchBendMultiplier();


    this.playingNote_12 = 0;
    this.playingNote_1c = 0;
    this.playingNote_42 = 0;
    this.playingNote_4e = 120;

    this.updateSampleRate();
    this.updateVolume();

    this.empty = false;
  }

  @Override
  public void keyOff(final int velocity) {
    this.adsrEnvelope.keyOff();
    this.velocity = velocity / 127d;

    this.updateVolume();
  }

  @Override
  public void updateSampleRate() {
    if(this.layer != null) {
      this.sampleRate = calculateSampleRate(this.layer.getRootKey(), this.note, this.pitchBendMultiplier, this.channel.getPitchBend(), this.layer.getCents());
    }
  }

  @Override
  public void updateVolume() {
    if(this.layer.getLockedVolume() != 0) {
      this.volume = this.layer.getLockedVolume();
      return;
    }

    //TODO velocity should use the volume ramp
    this.volume = this.channel.getVolume() * this.instrument.getVolume() * this.layer.getVolume() * this.velocity;
  }

  @Override
  public void setModulation(final int modulation) {
    this.isModulation = true;
    this.modulation = modulation;
  }

  @Override
  public void setBreath(int value) {
    this.breath = value;
  }

  void play() {
    this.sound.play();
  }

  void processBuffers() {
    this.sound.processBuffers();
  }

  void destroy() {
    this.sound.destroy();
  }

  @Override
  public MidiChannel getChannel() {
    return this.channel;
  }

  @Override
  public int getNote() {
    return this.note;
  }

  @Override
  public boolean isEmpty() {
    return this.empty;
  }

  //see FUN_800470fc
  private void variableParameters() {
    if(this.empty) {
      return;
    }

    if(this.isModulation || this.portamento) { //TODO sequenceData_104
      int cents = this.layer.getCents();
      int note = this.note;
      int rootKey = this.layer.getRootKey();
      int pitchBend = this.channel.getPitchBend();
      int pitchBendMultiplier = this.pitchBendMultiplier;

      if(this.isModulation || this.portamento) {
        //This essentially swaps the offset from note to root key, since note is set to 120 down the line
        rootKey = 120 + rootKey - note;

        if(this.isModulation) {
          if((this.breath & 0xfff) != 120) { //TODO  || ticksPerSecond != 60
            this.playingNote_12 += this.breath & 0xfff;
          } else {
            final int var = this.breath & 0xf000;
            if(var != 0) {
              this.breath = this.breath & 0xfff | var - 0x1000;
              this.playingNote_12 += this.breath & 0xfff;
            } else {
              this.breath |= 0x6000;
            }
          }

          pitchBend = 0x80;

          if(this.breathControls != null && this.breathControls.length > 0) {

            if(this.playingNote_12 >= 0xf0) {
              this.playingNote_12 = (this.breath & 0xfff) >>> 1;
            }

            pitchBend = this.breathControls[this.playingNote_10][this.playingNote_12 >>> 2] & 0xff;
          }

          //Set the note to 120, unless portamento
          note = this.playingNote_4e;

          //Pitch bend will be overwritten, so it has to be converted into note offset and cents
          if(this.playingNote_1c == 0) {
            final int _64ths = (this.channel.getPitchBend() - 64) * this.pitchBendMultiplier;
            note = note + _64ths / 64;
            cents = cents + Math.floorMod(_64ths / 4, 16);
            pitchBendMultiplier = 1;
          }

          // Here, pitch bend is either 128 or the value from the breath control wave
          // Since modulation is a single byte, the * just sets it to 1 in the edge case of it being 0xFF, otherwise pitch bend is ignored, which has to be wrong.
          pitchBend = pitchBend * this.modulation / 255 - ((this.modulation + 1) / 2 - 64);
        }

        if(this.portamento) {
          //TODO
        }

        if(this.playingNote_42 == 1) { //TODO || sequenceData_104
          //pitch = sequenceData.pitch_0ec;
        }

        final int pitch = 0x1000;

        this.sampleRate = (pitch * calculateSampleRate(rootKey, note, pitchBendMultiplier, pitchBend, cents)) >> 12;
      }
    }

    //TODO playingNote_1a
  }

  private static int calculateSampleRate(final int rootKey, final int note, final int pitchBendMultiplier, final int pitchBend, final int cents) {
    final int semitoneOffset;
    final int octaveOffset;
    final int adjustedCents = (int) (cents * 6.25f); //Cents are actually 16ths

    final double pitchBendMulti = Math.pow(2, (pitchBendMultiplier * ((pitchBend - 64) / 64d)) / 12d);

    if(note < rootKey) {
      octaveOffset = ((rootKey - note - 1)) / 12 + 1;
      semitoneOffset = (12 * octaveOffset) - (rootKey - note);

      return (int)((0x1000 >> octaveOffset) * (Offsets.semitone[semitoneOffset] * Offsets.cent[adjustedCents] * pitchBendMulti));
    }

    semitoneOffset = ((note - rootKey) % 12);
    octaveOffset = (note - rootKey) / 12;

    return (int)((0x1000 << octaveOffset) * (Offsets.semitone[semitoneOffset] * Offsets.cent[adjustedCents] * pitchBendMulti));
  }

  private static final short[] gaussTable = {
    -0x001, -0x001, -0x001, -0x001, -0x001, -0x001, -0x001, -0x001,
    -0x001, -0x001, -0x001, -0x001, -0x001, -0x001, -0x001, -0x001,
    0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0000, 0x0001,
    0x0001, 0x0001, 0x0001, 0x0002, 0x0002, 0x0002, 0x0003, 0x0003,
    0x0003, 0x0004, 0x0004, 0x0005, 0x0005, 0x0006, 0x0007, 0x0007,
    0x0008, 0x0009, 0x0009, 0x000A, 0x000B, 0x000C, 0x000D, 0x000E,
    0x000F, 0x0010, 0x0011, 0x0012, 0x0013, 0x0015, 0x0016, 0x0018,
    0x0019, 0x001B, 0x001C, 0x001E, 0x0020, 0x0021, 0x0023, 0x0025,
    0x0027, 0x0029, 0x002C, 0x002E, 0x0030, 0x0033, 0x0035, 0x0038,
    0x003A, 0x003D, 0x0040, 0x0043, 0x0046, 0x0049, 0x004D, 0x0050,
    0x0054, 0x0057, 0x005B, 0x005F, 0x0063, 0x0067, 0x006B, 0x006F,
    0x0074, 0x0078, 0x007D, 0x0082, 0x0087, 0x008C, 0x0091, 0x0096,
    0x009C, 0x00A1, 0x00A7, 0x00AD, 0x00B3, 0x00BA, 0x00C0, 0x00C7,
    0x00CD, 0x00D4, 0x00DB, 0x00E3, 0x00EA, 0x00F2, 0x00FA, 0x0101,
    0x010A, 0x0112, 0x011B, 0x0123, 0x012C, 0x0135, 0x013F, 0x0148,
    0x0152, 0x015C, 0x0166, 0x0171, 0x017B, 0x0186, 0x0191, 0x019C,
    0x01A8, 0x01B4, 0x01C0, 0x01CC, 0x01D9, 0x01E5, 0x01F2, 0x0200,
    0x020D, 0x021B, 0x0229, 0x0237, 0x0246, 0x0255, 0x0264, 0x0273,
    0x0283, 0x0293, 0x02A3, 0x02B4, 0x02C4, 0x02D6, 0x02E7, 0x02F9,
    0x030B, 0x031D, 0x0330, 0x0343, 0x0356, 0x036A, 0x037E, 0x0392,
    0x03A7, 0x03BC, 0x03D1, 0x03E7, 0x03FC, 0x0413, 0x042A, 0x0441,
    0x0458, 0x0470, 0x0488, 0x04A0, 0x04B9, 0x04D2, 0x04EC, 0x0506,
    0x0520, 0x053B, 0x0556, 0x0572, 0x058E, 0x05AA, 0x05C7, 0x05E4,
    0x0601, 0x061F, 0x063E, 0x065C, 0x067C, 0x069B, 0x06BB, 0x06DC,
    0x06FD, 0x071E, 0x0740, 0x0762, 0x0784, 0x07A7, 0x07CB, 0x07EF,
    0x0813, 0x0838, 0x085D, 0x0883, 0x08A9, 0x08D0, 0x08F7, 0x091E,
    0x0946, 0x096F, 0x0998, 0x09C1, 0x09EB, 0x0A16, 0x0A40, 0x0A6C,
    0x0A98, 0x0AC4, 0x0AF1, 0x0B1E, 0x0B4C, 0x0B7A, 0x0BA9, 0x0BD8,
    0x0C07, 0x0C38, 0x0C68, 0x0C99, 0x0CCB, 0x0CFD, 0x0D30, 0x0D63,
    0x0D97, 0x0DCB, 0x0E00, 0x0E35, 0x0E6B, 0x0EA1, 0x0ED7, 0x0F0F,
    0x0F46, 0x0F7F, 0x0FB7, 0x0FF1, 0x102A, 0x1065, 0x109F, 0x10DB,
    0x1116, 0x1153, 0x118F, 0x11CD, 0x120B, 0x1249, 0x1288, 0x12C7,
    0x1307, 0x1347, 0x1388, 0x13C9, 0x140B, 0x144D, 0x1490, 0x14D4,
    0x1517, 0x155C, 0x15A0, 0x15E6, 0x162C, 0x1672, 0x16B9, 0x1700,
    0x1747, 0x1790, 0x17D8, 0x1821, 0x186B, 0x18B5, 0x1900, 0x194B,
    0x1996, 0x19E2, 0x1A2E, 0x1A7B, 0x1AC8, 0x1B16, 0x1B64, 0x1BB3,
    0x1C02, 0x1C51, 0x1CA1, 0x1CF1, 0x1D42, 0x1D93, 0x1DE5, 0x1E37,
    0x1E89, 0x1EDC, 0x1F2F, 0x1F82, 0x1FD6, 0x202A, 0x207F, 0x20D4,
    0x2129, 0x217F, 0x21D5, 0x222C, 0x2282, 0x22DA, 0x2331, 0x2389,
    0x23E1, 0x2439, 0x2492, 0x24EB, 0x2545, 0x259E, 0x25F8, 0x2653,
    0x26AD, 0x2708, 0x2763, 0x27BE, 0x281A, 0x2876, 0x28D2, 0x292E,
    0x298B, 0x29E7, 0x2A44, 0x2AA1, 0x2AFF, 0x2B5C, 0x2BBA, 0x2C18,
    0x2C76, 0x2CD4, 0x2D33, 0x2D91, 0x2DF0, 0x2E4F, 0x2EAE, 0x2F0D,
    0x2F6C, 0x2FCC, 0x302B, 0x308B, 0x30EA, 0x314A, 0x31AA, 0x3209,
    0x3269, 0x32C9, 0x3329, 0x3389, 0x33E9, 0x3449, 0x34A9, 0x3509,
    0x3569, 0x35C9, 0x3629, 0x3689, 0x36E8, 0x3748, 0x37A8, 0x3807,
    0x3867, 0x38C6, 0x3926, 0x3985, 0x39E4, 0x3A43, 0x3AA2, 0x3B00,
    0x3B5F, 0x3BBD, 0x3C1B, 0x3C79, 0x3CD7, 0x3D35, 0x3D92, 0x3DEF,
    0x3E4C, 0x3EA9, 0x3F05, 0x3F62, 0x3FBD, 0x4019, 0x4074, 0x40D0,
    0x412A, 0x4185, 0x41DF, 0x4239, 0x4292, 0x42EB, 0x4344, 0x439C,
    0x43F4, 0x444C, 0x44A3, 0x44FA, 0x4550, 0x45A6, 0x45FC, 0x4651,
    0x46A6, 0x46FA, 0x474E, 0x47A1, 0x47F4, 0x4846, 0x4898, 0x48E9,
    0x493A, 0x498A, 0x49D9, 0x4A29, 0x4A77, 0x4AC5, 0x4B13, 0x4B5F,
    0x4BAC, 0x4BF7, 0x4C42, 0x4C8D, 0x4CD7, 0x4D20, 0x4D68, 0x4DB0,
    0x4DF7, 0x4E3E, 0x4E84, 0x4EC9, 0x4F0E, 0x4F52, 0x4F95, 0x4FD7,
    0x5019, 0x505A, 0x509A, 0x50DA, 0x5118, 0x5156, 0x5194, 0x51D0,
    0x520C, 0x5247, 0x5281, 0x52BA, 0x52F3, 0x532A, 0x5361, 0x5397,
    0x53CC, 0x5401, 0x5434, 0x5467, 0x5499, 0x54CA, 0x54FA, 0x5529,
    0x5558, 0x5585, 0x55B2, 0x55DE, 0x5609, 0x5632, 0x565B, 0x5684,
    0x56AB, 0x56D1, 0x56F6, 0x571B, 0x573E, 0x5761, 0x5782, 0x57A3,
    0x57C3, 0x57E2, 0x57FF, 0x581C, 0x5838, 0x5853, 0x586D, 0x5886,
    0x589E, 0x58B5, 0x58CB, 0x58E0, 0x58F4, 0x5907, 0x5919, 0x592A,
    0x593A, 0x5949, 0x5958, 0x5965, 0x5971, 0x597C, 0x5986, 0x598F,
    0x5997, 0x599E, 0x59A4, 0x59A9, 0x59AD, 0x59B0, 0x59B2, 0x59B3,
  };
}
