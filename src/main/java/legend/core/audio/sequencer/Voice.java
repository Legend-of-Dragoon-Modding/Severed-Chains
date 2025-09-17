package legend.core.audio.sequencer;

import legend.core.audio.sequencer.assets.Breath;
import legend.core.audio.sequencer.assets.Channel;
import legend.core.audio.sequencer.assets.Instrument;
import legend.core.audio.sequencer.assets.InstrumentLayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import it.unimi.dsi.fastutil.floats.FloatFloatImmutablePair;

import javax.annotation.Nullable;

final class Voice {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Voice.class);
  private static final Marker VOICE_MARKER = MarkerManager.getMarker("VOICE");
  static final float[] EMPTY = {0, 0, 0};

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
  private long sampleRate;
  /** playingNote.noteNumber_02 */
  private int note;
  /** playingNote.velocityVolume_2c */
  private float velocityVolume;
  private int pitchBendMultiplier;
  private boolean isModulation;
  private int modulation;
  /** waveforms_800c4ab8.waveforms_02 */
  @Nullable
  private Breath breath;

  private int priorityOrder;
  private VoicePriority priority;

  private float volumeLeft;
  private float volumeRight;

  private boolean hasSamples;
  private final float[] samples = new float[28 + EMPTY.length];

  Voice(final int index, final LookupTables lookupTables) {
    this.index = index;
    this.lookupTables = lookupTables;
    this.counter = new VoiceCounter();
  }

  void tick(final float[] output, final float[] reverb) {
    if(!this.used) {
      return;
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

  private float sampleVoice() {
    if(!this.hasSamples) {
      this.soundBankEntry.loadSamples(this.samples);

      this.hasSamples = true;
    }

    final int sampleIndex = this.counter.getCurrentSampleIndex();
    final int interpolationIndex = this.counter.getSampleInterpolationIndex();

    final float sample = this.lookupTables.interpolate(this.samples, sampleIndex, interpolationIndex);

    this.hasSamples = !this.counter.add(this.sampleRate);

    return sample;
  }

  void handleModulation() {
    if(!this.used || !this.isModulation) {
      return;
    }

    this.counter.addBreath(this.channel.getBreath());

    // TODO Pitch bend would be set to 0x80, which does nothing, might be worth to figure out, if we can remove this entirely (possibly check in modulation/breath control settings, since that is not run for every sample)
    if(this.breath == null) {
      return;
    }

    final int breathControlPosition = this.counter.getCurrentBreathIndex();
    final int breathControlInterpolationIndex = this.counter.getBreathInterpolationIndex();

    final float interpolatedBreath = this.breath.getValue(breathControlPosition, breathControlInterpolationIndex, this.lookupTables);

    final int finePitch = this.lookupTables.modulate(this.layer.getFinePitch(), interpolatedBreath, this.modulation);

    this.sampleRate = this.lookupTables.calculateSampleRate(this.layer.getKeyRoot(), this.note, finePitch, this.channel.getPitchBend(), this.pitchBendMultiplier);
  }

  void keyOn(final Channel channel, final Instrument instrument, final InstrumentLayer layer, final int note, final int velocityVolume, final Breath[] breathControls, final int playingVoices) {
    LOGGER.info(VOICE_MARKER, "Voice %d Key On", this.index);

    this.channel = channel;
    this.instrument = instrument;
    this.layer = layer;
    this.note = note;
    this.velocityVolume = velocityVolume / 128.0f;
    this.pitchBendMultiplier = this.layer.isPitchBendMultiplierFromInstrument() ? this.instrument.getPitchBendMultiplier() : this.layer.getPitchBendMultiplier();
    final int breathControlIndex = this.layer.isBreathControlIndexFromInstrument() ? this.instrument.getBreathControlIndex() : this.layer.getBreathControlIndex();
    this.breath = breathControlIndex == 0x7f ? null : breathControls[breathControlIndex];
    this.priority = VoicePriority.getPriority(this.layer.isHighPriority(), this.channel.getPriority());
    this.priorityOrder = playingVoices;

    this.isModulation = (this.layer.isModulation() && this.channel.getModulation() != 0);
    if(this.isModulation) {
      this.modulation = this.channel.getModulation();
    } else {
      // TODO is this really necessary?
      this.modulation = 0;
    }

    this.counter.reset();
    this.adsrEnvelope.load(this.layer.getAdsr());
    this.soundBankEntry.load(this.layer.getSoundBankEntry());

    this.sampleRate = this.lookupTables.calculateSampleRate(this.layer.getKeyRoot(), note, this.layer.getFinePitch(), this.channel.getPitchBend(), this.pitchBendMultiplier);
    this.calculateVolume();

    this.used = true;
    this.hasSamples = false;
    this.samples[30] = 0;
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
    this.breath = null;
    this.priority = VoicePriority.LOW;
    this.hasSamples = false;
    this.samples[30] = 0;
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
    return this.priority == VoicePriority.LOW;
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

  void updateSampleRate() {
    if(this.layer == null) {
      return;
    }

    this.sampleRate = this.lookupTables.calculateSampleRate(this.layer.getKeyRoot(), this.note, this.layer.getFinePitch(), this.channel.getPitchBend(), this.pitchBendMultiplier);
  }

  private void calculateVolume() {
    final float volume = this.channel.getAdjustedVolume() * this.instrument.getVolume() * this.layer.getVolume() * this.velocityVolume;

    final FloatFloatImmutablePair panVolumes = this.lookupTables.getPan(this.channel.getPan(), this.instrument.getPan(), this.layer.getPan());
    final float volumeL = volume * panVolumes.leftFloat();
    final float volumeR = volume * panVolumes.rightFloat();

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

  void setModulation(final int modulation) {
    this.modulation = modulation;
    this.isModulation = modulation != 0;

    if(!this.isModulation) {
      this.counter.resetBreath();
    }
  }
}
