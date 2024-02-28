package legend.core.audio.sequencer;

import legend.core.audio.sequencer.assets.Channel;
import legend.core.audio.sequencer.assets.Instrument;
import legend.core.audio.sequencer.assets.InstrumentLayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.annotation.Nullable;

final class Voice {
  private static final Logger LOGGER = LogManager.getFormatterLogger();
  private static final Marker VOICE_MARKER = MarkerManager.getMarker("VOICE");
  static final short[] EMPTY = {0, 0, 0};

  private final int index;
  private final LookupTables lookupTables;
  private final VoiceCounter counter;
  private final AdsrEnvelope adsrEnvelope = new AdsrEnvelope();
  private final SoundBankEntry soundBankEntry = new SoundBankEntry();

  private Channel channel;
  private Instrument instrument;
  private InstrumentLayer layer;

  /** playingNote.used_00 */
  private boolean used;
  /** voice.pitch */
  private int sampleRate;
  /** playingNote.noteNumber_02 */
  private int note;
  /** playingNote.velocityVolume_2c */
  private float velocityVolume;
  private int pitchBendMultiplier;
  private boolean isModulation;
  private int modulation;
  /** waveforms_800c4ab8.waveforms_02 */
  private byte[][] breathControls;
  /** playingNote.breath_3c */
  private int breath;
  /** playingNote.breathControlListIndex_10 */
  private int breathControlIndex;
  /** playingNote._12 */
  private int breathControlPosition;

  private int priorityOrder;
  private VoicePriority priority;

  private float volumeLeft;
  private float volumeRight;

  private boolean hasSamples;
  private final short[] samples = new short[28 + EMPTY.length];

  Voice(final int index, final LookupTables lookupTables, final int interpolationBitDepth) {
    this.index = index;
    this.lookupTables = lookupTables;
    this.counter = new VoiceCounter(interpolationBitDepth);
  }

  void tick(final float[] output, final float[] reverb, final boolean effectsOverTime) {
    if(!this.used) {
      return;
    }

    if(effectsOverTime) {
      this.handleModulation();
    }

    this.adsrEnvelope.tick();

    final float sample = this.sampleVoice() * this.adsrEnvelope.getCurrentLevel();

    final float left = sample * this.volumeLeft;
    final float right = sample * this.volumeRight;

    output[0] += left;
    output[1] += right;

    if(this.layer.isReverb()) {
      reverb[0] += left;
      reverb[1] += right;
    }
  }

  private int sampleVoice() {
    if(!this.hasSamples) {
      this.soundBankEntry.loadSamples(this.samples);

      this.hasSamples = true;
    }

    final int sampleIndex = this.counter.getCurrentSampleIndex();

    final float[] interpolationWeights = this.lookupTables.getInterpolationWeights(this.counter.getInterpolationIndex());

    final float sample = interpolationWeights[0] * this.samples[sampleIndex]
      + interpolationWeights[1] * this.samples[sampleIndex + 1]
      + interpolationWeights[2] * this.samples[sampleIndex + 2]
      + interpolationWeights[3] * this.samples[sampleIndex + 3];


    this.hasSamples = !this.counter.add(this.sampleRate);

    return (int)sample;
  }

  private void handleModulation() {
    if(!this.isModulation) {
      return;
    }

    // The other branch probably doesn't matter for bgm. But let's throw here, just in case
    if((this.breath & 0xfff) == 0x78) {
      throw new RuntimeException("BREATH 0x78");
    }

    this.breathControlPosition += this.breath & 0xfff;

    // TODO Pitch bend would be set to 0x80, which does nothing, might be worth to figure out, if we can remove this entirely (possibly check in modulation/breath control settings, since that is not run for every sample)
    if(this.breathControls == null || this.breathControls.length == 0) {
      return;
    }

    if(this.breathControlPosition >= 0xf0) {
      this.breathControlPosition = (this.breath & 0xfff) >>> 1;
    }

    final int _64ths = (this.channel.getPitchBend() - 64) * this.pitchBendMultiplier;
    final int note = this.note + _64ths / 64;
    final int sixtyFourths = this.layer.getSixtyFourths() + (_64ths - (note - this.note) * 64);

    // TODO Since breathControlIndex is set based on the asset, we might want to get rid of it entirely and simply load a byte[]
    final int pitchBend = (((this.breathControls[this.breathControlIndex][this.breathControlPosition >>> 2] & 0xff) - 128) * this.modulation) / 256 + 64;

    this.sampleRate = this.calculateSampleRate(this.layer.getKeyRoot(), note, sixtyFourths, pitchBend, 1);
  }

  void keyOn(final Channel channel, final Instrument instrument, final InstrumentLayer layer, final int note, final int velocityVolume, final byte[][] breathControls, final int playingVoices) {
    LOGGER.info(VOICE_MARKER, "Voice %d Key On", this.index);

    this.channel = channel;
    this.instrument = instrument;
    this.layer = layer;
    this.note = note;
    this.velocityVolume = velocityVolume / 128f;
    this.pitchBendMultiplier = this.layer.isPitchBendMultiplierFromInstrument() ? this.instrument.getPitchBendMultiplier() : this.layer.getPitchBendMultiplier();
    this.breathControls = breathControls;
    this.breath = this.channel.getBreath();
    this.breathControlPosition = 0;
    this.priority = VoicePriority.getPriority(this.layer.isHighPriority(), this.channel.getPriority());
    this.priorityOrder = playingVoices;

    this.isModulation = (this.layer.isModulation() && this.channel.getModulation() != 0);
    if(this.isModulation) {
      this.breathControlIndex = this.layer.isBreathControlIndexFromInstrument() ? this.instrument.getBreathControlIndex() : this.layer.getBreathControlIndex();
      this.modulation = this.channel.getModulation();
    } else {
      // TODO is this really necessary?
      this.modulation = 0;
    }

    this.counter.reset();
    this.adsrEnvelope.load(this.layer.getAdsr());
    this.soundBankEntry.load(this.layer.getSoundBankEntry());

    this.sampleRate = this.calculateSampleRate(this.layer.getKeyRoot(), note, this.layer.getSixtyFourths(), this.channel.getPitchBend(), this.pitchBendMultiplier);
    this.calculateVolume();

    this.used = true;
    this.hasSamples = false;
    System.arraycopy(EMPTY, 0, this.samples, 28, EMPTY.length);
  }

  void keyOff() {
    LOGGER.info(VOICE_MARKER, "Voice %d Key Off", this.index);

    this.adsrEnvelope.keyOff();

    this.priority = VoicePriority.decreasePriority(this.priority);
  }

  void clear() {
    LOGGER.info(VOICE_MARKER, "Clearing Voice %d", this.index);

    this.used = false;
    this.note = 0;
    this.channel = null;
    this.layer = null;
    this.instrument = null;
    this.isModulation = false;
    this.modulation = 0;
    this.breath = 0;
    this.breathControlIndex = 0;
    this.breathControlPosition = 0;
    this.priority = VoicePriority.Low;
    System.arraycopy(EMPTY, 0, this.samples, 28, EMPTY.length);
  }

  @Nullable
  public InstrumentLayer getLayer() {
    return this.layer;
  }

  boolean isUsed() {
    return this.used;
  }

  boolean isFinished() {
    return this.adsrEnvelope.isFinished() || (this.soundBankEntry.isEnd() && this.counter.getCurrentSampleIndex() >= EMPTY.length);
  }

  boolean isLowPriority() {
    return this.priority == VoicePriority.Low;
  }

  int getPriorityOrder() {
    return this.priorityOrder;
  }

  void setPriorityOrder(final int priorityOrder) {
    this.priorityOrder = priorityOrder;
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

  void updateSampleRate() {
    if(this.layer == null) {
      return;
    }

    this.sampleRate = this.calculateSampleRate(this.layer.getKeyRoot(), this.note, this.layer.getSixtyFourths(), this.channel.getPitchBend(), this.pitchBendMultiplier);
  }

  private void calculateVolume() {
    final float volume = this.channel.getAdjustedVolume() * this.instrument.getVolume() * this.layer.getVolume() * this.velocityVolume;

    final float volumeL = volume * this.calculatePan(true);
    final float volumeR = volume * this.calculatePan(false);

    if(this.layer.getLockedVolume() == 0) {
      this.volumeLeft = volumeL;
      this.volumeRight = volumeR;

      return;
    }

    // TODO this should be verified with calculateVolume 0x80048ab8L. Note that because we are not using the Volume registers, this should return 2x the original value. However, it doesn't seem to be hit in game.
    this.volumeLeft = (float)((this.layer.getLockedVolume() << 8) | ((int)(volumeL * 0x80))) / 0x4000;
    this.volumeRight = (float)((this.layer.getLockedVolume() << 8) | ((int)(volumeR * 0x80))) / 0x4000;
  }

  void updateVolume() {
    if(this.layer == null) {
      return;
    }

    this.calculateVolume();
  }

  private float calculatePan(final boolean left) {
    return this.lookupTables.getPan(this.lookupTables.mergePan(this.channel.getPan(), this.lookupTables.mergePan(this.instrument.getPan(), this.layer.getPan())), left);
  }

  void setModulation(final int modulation) {
    this.isModulation = true;
    this.modulation = modulation;
  }

  void setBreath(final int breath) {
    this.breath = breath;
  }
}