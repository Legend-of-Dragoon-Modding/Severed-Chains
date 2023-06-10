package legend.core.audio;

import legend.core.audio.assets.Channel;
import legend.core.audio.assets.Instrument;
import legend.core.audio.assets.InstrumentLayer;

final class Voice {
  private final int index;
  private final LookupTables lookupTables;

  private final BufferedSound sound;

  private final VoiceCounter counter = new VoiceCounter();
  private final AdsrEnvelope adsrEnvelope = new AdsrEnvelope();
  private final SoundBankEntry soundBankEntry = new SoundBankEntry();

  private Channel channel;
  private Instrument instrument;
  private InstrumentLayer layer;

  //Playing note
  private boolean used;
  private int sampleRate;
  private int note;


  private boolean hasSamples;
  private final short[] samples = new short[31];


  Voice(final int index, final LookupTables lookupTables, final int bufferSize, final boolean stereo) {
    this.index = index;
    this.lookupTables = lookupTables;
    this.sound = new BufferedSound(bufferSize, stereo);
  }

  //TODO stereo probably shouldn't be passed here, doesn't sound too safe for changing
  void tick(final boolean stereo) {
    if(!this.used) {
      for(int channel = 0; channel < (stereo ? 2 : 1); channel++) {
        this.sound.bufferSample((short)0);
      }

      return;
    }

    this.adsrEnvelope.tick();

    final short sample = this.sampleVoice();

    final short adsrApplied = (short)((sample * this.adsrEnvelope.getCurrentLevel()) / 0x8000);

    this.sound.bufferSample(adsrApplied);

    if((this.soundBankEntry.isEnd() && this.counter.getCurrentSampleIndex() >= 2)
      || this.adsrEnvelope.isFinished()) {
      this.used = false;
    }
  }

  private short sampleVoice() {
    if(!this.hasSamples) {
      System.arraycopy(this.samples, 28, this.samples, 0, 3);

      this.soundBankEntry.loadSamples(this.samples);

      this.hasSamples = true;
    }

    final int interpolationIndex = this.counter.getInterpolationIndex();
    final int sampleIndex = this.counter.getCurrentSampleIndex();

    final double[] interpolationWeights = this.lookupTables.getInterpolationWeights(interpolationIndex);
    final double value = interpolationWeights[0] * this.samples[sampleIndex]
      + interpolationWeights[1] * this.samples[sampleIndex + 1]
      + interpolationWeights[2] * this.samples[sampleIndex + 2]
      + interpolationWeights[3] * this.samples[sampleIndex + 3];

    int step = this.sampleRate;

    //TODO modulation

    if(step > 0x3fff) {
      step = 0x4000;
    }

    if(this.counter.add(step)) {
      this.hasSamples = false;
    }

    return (short)value;
  }

  void keyOn(final Channel channel, final Instrument instrument, final InstrumentLayer layer, final int note, final int velocity) {
    System.out.printf("[VOICE] Voice %d Key On%n", this.index);
    this.channel = channel;
    this.instrument = instrument;
    this.layer = layer;
    this.note = note;

    this.counter.reset();
    this.adsrEnvelope.load(layer.getAdsr());
    this.soundBankEntry.load(layer.getPcm());

    this.sampleRate = this.calculateSampleRate(this.layer.getKeyRoot(), note, this.layer.getSixtyFourths(), this.channel.getPitchBend(), this.layer.getPitchBendMultiplier());

    this.used = true;
  }

  void keyOff() {
    System.out.printf("[VOICE] Voice %d Key Off%n", this.index);
    this.adsrEnvelope.keyOff();
  }

  boolean isUsed() {
    return this.used;
  }

  Channel getChannel() {
    return this.channel;
  }

  int getNote() {
    return this.note;
  }

  private int calculateSampleRate(final int rootKey, final int note, final int sixtyFourths, final int pitchBend, final int pitchBendMultiplier) {
    final int offsetIn64ths = (note - rootKey) * 64 + sixtyFourths + (pitchBend - 64) * pitchBendMultiplier;

    if(offsetIn64ths >= 0) {
      final int octaveOffset = offsetIn64ths / 768;
      final int sampleRateOffset = offsetIn64ths - octaveOffset * 768;
      return this.lookupTables.getSampleRate(sampleRateOffset) << octaveOffset;
    }

    final int octaveOffset = (offsetIn64ths + 1) / -768 + 1;
    final int sampleRateOffset = offsetIn64ths + octaveOffset * 768;
    return this.lookupTables.getSampleRate(sampleRateOffset) >> octaveOffset;
  }

  //TODO needs to reflect possibility of Instrument pitch bend multiplier
  void updateSampleRate() {
    if(this.layer != null) {
      this.sampleRate = this.calculateSampleRate(this.layer.getKeyRoot(), this.note, this.layer.getSixtyFourths(), this.channel.getPitchBend(), this.layer.getPitchBendMultiplier());
    }
  }

  void play() {
    this.sound.play();
  }

  void destroy() {
    this.sound.destroy();
  }

  void processBuffers() {
    this.sound.processBuffers();
  }
}
