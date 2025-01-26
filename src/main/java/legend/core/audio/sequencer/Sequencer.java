package legend.core.audio.sequencer;

import legend.core.MathHelper;
import legend.core.audio.AudioSource;
import legend.core.audio.EffectsOverTimeGranularity;
import legend.core.audio.InterpolationPrecision;
import legend.core.audio.PitchResolution;
import legend.core.audio.SampleRate;
import legend.core.audio.sequencer.assets.BackgroundMusic;
import legend.core.audio.sequencer.assets.InstrumentLayer;
import legend.core.audio.sequencer.assets.sequence.Command;
import legend.core.audio.sequencer.assets.sequence.CommandCallback;
import legend.core.audio.sequencer.assets.sequence.bgm.BreathChange;
import legend.core.audio.sequencer.assets.sequence.bgm.DataEntry;
import legend.core.audio.sequencer.assets.sequence.bgm.DataEntryLsb;
import legend.core.audio.sequencer.assets.sequence.bgm.DataEntryMsb;
import legend.core.audio.sequencer.assets.sequence.bgm.EndOfTrack;
import legend.core.audio.sequencer.assets.sequence.bgm.Key;
import legend.core.audio.sequencer.assets.sequence.bgm.KeyOff;
import legend.core.audio.sequencer.assets.sequence.bgm.KeyOn;
import legend.core.audio.sequencer.assets.sequence.bgm.ModulationChange;
import legend.core.audio.sequencer.assets.sequence.bgm.PanChange;
import legend.core.audio.sequencer.assets.sequence.bgm.PitchBendChange;
import legend.core.audio.sequencer.assets.sequence.bgm.ProgramChange;
import legend.core.audio.sequencer.assets.sequence.bgm.TempoChange;
import legend.core.audio.sequencer.assets.sequence.bgm.VolumeChange;
import legend.game.sound.ReverbConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.HashMap;
import java.util.Map;

import static legend.game.Scus94491BpeSegment_8005.reverbConfigs_80059f7c;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;

public final class Sequencer extends AudioSource {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Sequencer.class);
  private static final Marker SEQUENCER_MARKER = MarkerManager.getMarker("SEQUENCER");
  // TODO switch between mono and stereo
  private final boolean stereo;
  private SampleRate sampleRate;
  private int effectsOverTimeSamples;
  private final LookupTables lookupTables;
  private final Voice[] voices;
  private int playingVoices;
  private final float[] voiceOutputBuffer = new float[2];
  private final float[] voiceReverbBuffer = new float[2];
  // TODO consider making this variable length for mono, but it might be better to simply always playback as stereo, just with down mixing
  private short[] outputBuffer;

  private final Reverberizer reverb = new Reverberizer();

  private float playerVolume = 1.0f;
  private float engineVolumeLeft = 0.5f;
  private float engineVolumeRight = 0.5f;
  private Fading fading = Fading.NONE;
  private float fadeInVolume;
  private float fadeOutVolumeLeft;
  private float fadeOutVolumeRight;
  private int fadeTime;
  private int fadeCounter;

  private boolean volumeChanging;
  private float newVolume;
  private float oldVolume;
  private int volumeChangingTimeTotal;
  private int volumeChangingTimeRemaining;

  private final Map<Class, CommandCallback> commandCallbackMap = new HashMap<>();

  private BackgroundMusic backgroundMusic;
  private int samplesToProcess;

  public Sequencer(final boolean stereo, final int voiceCount, final InterpolationPrecision bitDepth, final PitchResolution pitchResolution, final SampleRate sampleRate, final EffectsOverTimeGranularity effectsGranularity) {
    super(5);

    this.sampleRate = sampleRate;

    this.outputBuffer = new short[(this.sampleRate.value / 60) * 2];

    this.stereo = stereo;

    this.lookupTables = new LookupTables(bitDepth, pitchResolution, this.sampleRate);
    this.lookupTables.setEffectsOverTimeScale(effectsGranularity, sampleRate);
    this.effectsOverTimeSamples = sampleRate.value / (60 * this.lookupTables.getEffectsOverTimeScale());

    this.voices = new Voice[voiceCount];

    for(int voice = 0; voice < this.voices.length; voice++) {
      this.voices[voice] = new Voice(voice, this.lookupTables, bitDepth);
    }

    this.addCommandCallback(KeyOn.class, this::keyOn);
    this.addCommandCallback(KeyOff.class, this::keyOff);
    this.addCommandCallback(ModulationChange.class, this::modulation);
    this.addCommandCallback(BreathChange.class, this::breath);
    this.addCommandCallback(VolumeChange.class, this::volume);
    this.addCommandCallback(PanChange.class, this::pan);
    this.addCommandCallback(DataEntry.class, this::dataEntry);
    this.addCommandCallback(DataEntryLsb.class, this::dataEntryLsb);
    this.addCommandCallback(DataEntryMsb.class, this::dataEntryMsb);
    this.addCommandCallback(ProgramChange.class, this::programChange);
    this.addCommandCallback(PitchBendChange.class, this::pitchBend);
    this.addCommandCallback(TempoChange.class, this::changeTempo);
    this.addCommandCallback(EndOfTrack.class, this::endOfTrack);

    this.reverb.setConfig(reverbConfigs_80059f7c[2].config_02, sampleRate);
  }

  public void setVolume(final float volume) {
    this.playerVolume = volume;
  }

  @Override
  public void tick() {
    int samplePostition = 0;
    for(int effect = 0; effect < this.lookupTables.getEffectsOverTimeScale(); effect++) {
      for(int sample = 0; sample < this.effectsOverTimeSamples; sample++, samplePostition += 2) {
        this.clearFinishedVoices();

        this.tickSequence();

        this.voiceOutputBuffer[0] = 0;
        this.voiceOutputBuffer[1] = 0;

        this.voiceReverbBuffer[0] = 0;
        this.voiceReverbBuffer[1] = 0;

        for(final Voice voice : this.voices) {
          voice.tick(this.voiceOutputBuffer, this.voiceReverbBuffer);
        }

        this.reverb.processReverb(this.voiceReverbBuffer[0], this.voiceReverbBuffer[1]);

        this.outputBuffer[samplePostition    ] = (short)MathHelper.clamp(((this.voiceOutputBuffer[0] + this.reverb.getOutputLeft()) * this.engineVolumeLeft  * this.playerVolume), -0x8000, 0x7fff);
        this.outputBuffer[samplePostition + 1] = (short)MathHelper.clamp(((this.voiceOutputBuffer[1] + this.reverb.getOutputRight()) * this.engineVolumeRight * this.playerVolume), -0x8000, 0x7fff);
      }

      this.handleVolumeChanging();

      this.handleFadeInOut();

      for(final Voice voice : this.voices) {
        voice.handleModulation();
      }
    }

    this.bufferOutput(AL_FORMAT_STEREO16, this.outputBuffer, this.sampleRate.value);

    super.tick();
  }

  private void clearFinishedVoices() {
    for(final Voice voice : this.voices) {
      if(!voice.isUsed() || !voice.isFinished()) {
        continue;
      }

      if(this.playingVoices > 0) {
        this.playingVoices--;
      }

      for(final Voice otherVoice : this.voices) {
        if(otherVoice.getPriorityOrder() > voice.getPriorityOrder()) {
          otherVoice.setPriorityOrder(otherVoice.getPriorityOrder() - 1);
        }
      }

      voice.clear();
    }
  }

  private void tickSequence() {
    if(this.backgroundMusic == null) {
      return;
    }

    if(this.samplesToProcess > 0) {
      this.samplesToProcess--;
      return;
    }

    while(this.samplesToProcess == 0) {
      final Command command = this.backgroundMusic.getNextCommand();

      this.commandCallbackMap.get(command.getClass()).execute(command);

      if(this.backgroundMusic == null) {
        return;
      }

      this.samplesToProcess = (int)(command.getDeltaTime() * this.backgroundMusic.getSamplesPerTick());

      if(this.samplesToProcess != 0) {
        if(this.backgroundMusic.handleRepeat()) {
          this.samplesToProcess = 0;
        }
      }
    }



    LOGGER.info(SEQUENCER_MARKER, "Delta Time %d samples", this.samplesToProcess);
  }

  private void keyOff(final Key keyOff) {
    LOGGER.info(SEQUENCER_MARKER, "Key Off Channel: %d Note: %d", keyOff.getChannel().getIndex(), keyOff.getNote());

    for(final Voice voice : this.voices) {
      if(voice.isUsed() && voice.getChannel() == keyOff.getChannel() && voice.getNote() == keyOff.getNote()) {
        voice.keyOff();
      }
    }
  }

  private void keyOn(final KeyOn keyOn) {
    LOGGER.info(SEQUENCER_MARKER, "Ken On Channel: %d, Note %d, Velocity: %d", keyOn.getChannel().getIndex(), keyOn.getNote(), keyOn.getVelocity());

    if(keyOn.getVelocity() == 0) {
      this.keyOff(keyOn);
      return;
    }

    if(keyOn.getChannel().getVolume() == 0) {
      return;
    }

    if(keyOn.getChannel().getInstrument() == null) {
      return;
    }

    for(final InstrumentLayer layer : keyOn.getChannel().getInstrument().getLayers(keyOn.getNote())) {
      final Voice voice = this.selectVoice();

      voice.keyOn(keyOn.getChannel(), keyOn.getChannel().getInstrument(), layer, keyOn.getNote(), this.backgroundMusic.getVelocityVolume(keyOn.getVelocity()), this.backgroundMusic.getBreathControls(), this.playingVoices);
    }
  }

  private <T extends Command> void addCommandCallback(final Class<T> cls, final CommandCallback<T> callback) {
    this.commandCallbackMap.put(cls, callback);
  }

  private Voice selectVoice() {
    for(final Voice voice : this.voices) {
      if(!voice.isUsed()) {
        this.playingVoices++;
        return voice;
      }
    }

    int voiceIndex = this.findLowPriorityVoice();

    if(voiceIndex == this.voices.length) {
      voiceIndex = this.findOldestVoice();
    }

    if(voiceIndex == this.voices.length) {
      throw new RuntimeException("Voice pool overflow");
    }

    for(final Voice voice : this.voices) {
      if(voiceIndex < voice.getPriorityOrder()) {
        voice.setPriorityOrder(voice.getPriorityOrder() - 1);
      }
    }

    return this.voices[voiceIndex];
  }

  private int findLowPriorityVoice() {
    int maxPriority = this.voices.length;
    int voiceIndex = this.voices.length;

    for(int voice = 0; voice < this.voices.length; voice++) {
      if(this.voices[voice].isLowPriority() && this.voices[voice].getPriorityOrder() < maxPriority) {
        maxPriority = this.voices[voice].getPriorityOrder();
        voiceIndex = voice;
      }
    }

    return voiceIndex;
  }

  private int findOldestVoice() {
    int maxPriority = this.voices.length;
    int voiceIndex = this.voices.length;

    for(int voice = 0; voice < this.voices.length; voice++) {
      if(this.voices[voice].getPriorityOrder() < maxPriority) {
        maxPriority = this.voices[voice].getPriorityOrder();
        voiceIndex = voice;
      }
    }

    return voiceIndex;
  }

  private void modulation(final ModulationChange modulationChange) {
    LOGGER.info(SEQUENCER_MARKER, "Control Change Modulation Channel: %d Modulation: %d", modulationChange.getChannel().getIndex(), modulationChange.getModulation());

    modulationChange.getChannel().setModulation(modulationChange.getModulation());

    // TODO modulation could probably be straight up taken from channel
    for(final Voice voice : this.voices) {
      if(voice.isUsed() && voice.getChannel() == modulationChange.getChannel()) {
        voice.setModulation(modulationChange.getModulation());
      }
    }
  }

  private void breath(final BreathChange breathChange) {
    LOGGER.info(SEQUENCER_MARKER, "Control Change Breath Control Channel: %d Breath: %d", breathChange.getChannel().getIndex(), breathChange.getBreath());

    breathChange.getChannel().setBreath(breathChange.getBreath());

    for(final Voice voice : this.voices) {
      if(voice.isUsed() && voice.getChannel() ==  breathChange.getChannel()) {
        voice.setBreath(breathChange.getBreath());
      }
    }
  }

  private void volume(final VolumeChange volumeChange) {
    LOGGER.info(SEQUENCER_MARKER, "Control Change Volume Channel: %d Volume: %s", volumeChange.getChannel().getIndex(), volumeChange.getVolume());

    volumeChange.getChannel().changeVolume(volumeChange.getVolume(), this.backgroundMusic.getVolume());

    for(final Voice voice : this.voices) {
      if(voice.isUsed() && voice.getChannel() == volumeChange.getChannel()) {
        voice.updateVolume();
      }
    }
  }

  private void pan(final PanChange panChange) {
    LOGGER.info(SEQUENCER_MARKER, "Control Change Pan Channel: %d Pan: %d", panChange.getChannel().getIndex(), panChange.getPan());

    if(this.stereo) {
      panChange.getChannel().setPan(panChange.getPan());
    } else {
      panChange.getChannel().setPan(0x40);
    }

    for(final Voice voice : this.voices) {
      if(voice.isUsed() && voice.getChannel() == panChange.getChannel()) {
        voice.updateVolume();
      }
    }
  }

  private void programChange(final ProgramChange programChange) {
    LOGGER.info(SEQUENCER_MARKER, "Program Change Pan Channel: %d Instrument: %d", programChange.getChannel().getIndex(), programChange.getInstrumentIndex());

    programChange.getChannel().setInstrument(programChange.getInstrumentIndex());
    programChange.getChannel().setPitchBend(0);
    programChange.getChannel().setPriority(0x40);
  }

  private void pitchBend(final PitchBendChange pitchBend) {
    LOGGER.info(SEQUENCER_MARKER, "Pitch Bend Channel: %d, Pitch Bend: %d", pitchBend.getChannel().getIndex(), pitchBend.getPitchAmount());

    pitchBend.getChannel().setPitchBend(pitchBend.getPitchAmount());

    for(final Voice voice : this.voices) {
      if(voice.isUsed() && voice.getChannel() == pitchBend.getChannel()) {
        voice.updateSampleRate();
      }
    }
  }

  void dataEntry(final DataEntry dataEntry) {
    LOGGER.info(SEQUENCER_MARKER, "Data entry NRPN: %d Value: %d", this.backgroundMusic.getNrpn(), dataEntry.getValue());

    final int value = dataEntry.getValue();

    switch(this.backgroundMusic.getNrpn()) {
      case 0x00 -> {
        this.backgroundMusic.setRepeatCount(value);
        return;
      }
      case 0x04 -> LOGGER.warn(SEQUENCER_MARKER, "Unimplemented Data Entry - Attack (linear)");
      case 0x05 -> LOGGER.warn(SEQUENCER_MARKER, "Unimplemented Data Entry - Attack (exponential)");
      case 0x06 -> LOGGER.warn(SEQUENCER_MARKER, "Unimplemented Data Entry - Decay shift");
      case 0x07 -> LOGGER.warn(SEQUENCER_MARKER, "Unimplemented Data Entry - Sustain level");
      case 0x08 -> LOGGER.warn(SEQUENCER_MARKER, "Unimplemented Data Entry - Sustain (linear)");
      case 0x09 -> LOGGER.warn(SEQUENCER_MARKER, "Unimplemented Data Entry - Sustain (exponential)");
      case 0x0a -> LOGGER.warn(SEQUENCER_MARKER, "Unimplemented Data Entry - Release (linear)");
      case 0x0b -> LOGGER.warn(SEQUENCER_MARKER, "Unimplemented Data Entry - Release (exponential)");
      case 0x0c -> LOGGER.warn(SEQUENCER_MARKER, "Unimplemented Data Entry - Sustain direction");
      case 0x0f -> this.setReverbConfig(reverbConfigs_80059f7c[value].config_02);
      case 0x10 -> this.setReverbVolume(value, value);
      default -> LOGGER.error(SEQUENCER_MARKER, "Unknown Data entry NRPN: 0x%x", this.backgroundMusic.getNrpn());
    }
  }

  void dataEntryLsb(final DataEntryLsb dataEntryLsb) {
    LOGGER.info(SEQUENCER_MARKER, "Data Entry LSB Value: 0x%x", dataEntryLsb.getValue());

    this.backgroundMusic.dataEntryLsb(dataEntryLsb.getValue());
  }

  void dataEntryMsb(final DataEntryMsb dataEntryMsb) {
    LOGGER.info(SEQUENCER_MARKER, "Data Entry MSB Value: 0x%x", dataEntryMsb.getValue());

    this.backgroundMusic.dataEntryMsb(dataEntryMsb.getValue());
  }

  private void changeTempo(final TempoChange tempoChange) {
    LOGGER.info(SEQUENCER_MARKER, "Tempo change: %d", tempoChange.getTempo());

    this.backgroundMusic.setTempo(tempoChange.getTempo());
  }

  public void setReverbConfig(final ReverbConfig config) {
    this.reverb.setConfig(config, this.sampleRate);
  }

  public void setReverbVolume(final int reverbVolumeLeft, final int reverbVolumeRight) {
    this.reverb.setVolume(reverbVolumeLeft / 128.0f, reverbVolumeRight / 128.0f);
  }

  private void endOfTrack(final EndOfTrack endOfTrack) {
    if(!this.backgroundMusic.rewind()) {
      this.backgroundMusic = null;
    }
  }

  public void setMainVolume(final int left, final int right) {
    this.engineVolumeLeft = left >= 0x80 ? 1 : left / 256.0f;
    this.engineVolumeRight = right >= 0x80 ? 1 : right / 256.0f;
  }

  private void handleFadeInOut() {
    if(this.fading == Fading.NONE) {
      return;
    }

    if(this.fadeCounter >= this.fadeTime) {
      this.fading = Fading.NONE;
      this.fadeCounter = 0;
      return;
    }

    switch(this.fading) {
      case FADE_IN -> {
        final float volume = (this.fadeInVolume * this.fadeCounter) / this.fadeTime;
        this.fadeCounter++;
        this.engineVolumeLeft = volume;
        this.engineVolumeRight = volume;
      }
      case FADE_OUT -> {
        final float volumeLeft = (this.fadeOutVolumeLeft * (this.fadeTime - this.fadeCounter)) / this.fadeTime;
        final float volumeRight = (this.fadeOutVolumeRight * (this.fadeTime - this.fadeCounter)) / this.fadeTime;
        this.fadeCounter++;
        this.engineVolumeLeft = volumeLeft;
        this.engineVolumeRight = volumeRight;
      }
    }
  }

  private void handleVolumeChanging() {
    if(!this.volumeChanging)  {
      return;
    }

    if(this.volumeChangingTimeRemaining <= 0) {
      this.volumeChanging = false;
      this.backgroundMusic.setVolume(this.newVolume);
      return;
    }

    this.backgroundMusic.setVolume(this.newVolume + (this.oldVolume - this.newVolume) * this.volumeChangingTimeRemaining / this.volumeChangingTimeTotal);
    this.volumeChangingTimeRemaining--;

    for(final Voice voice : this.voices) {
      if(voice.isUsed()) {
        voice.updateVolume();
      }
    }
  }

  public void fadeIn(final int time, final int volume) {
    this.fadeTime = time * this.lookupTables.getEffectsOverTimeScale();
    this.fadeInVolume = volume / 256.0f;
    this.fadeCounter = 0;
    this.fading = Fading.FADE_IN;
  }

  public void fadeOut(final int time) {
    if(!this.isPlaying()) {
      this.engineVolumeLeft = 0;
      this.engineVolumeRight = 0;
      return;
    }

    this.fadeTime = time * this.lookupTables.getEffectsOverTimeScale();
    this.fadeOutVolumeLeft = this.engineVolumeLeft;
    this.fadeOutVolumeRight = this.engineVolumeRight;
    this.fadeCounter = 0;
    this.fading = Fading.FADE_OUT;
  }

  public int getSongId() {
    if(this.backgroundMusic == null) {
      return -1;
    }

    return this.backgroundMusic.getSongId();
  }

  public void loadBackgroundMusic(final BackgroundMusic backgroundMusic) {
    this.backgroundMusic = backgroundMusic;
  }

  public void unloadMusic() {
    this.stopSequence();
    this.backgroundMusic = null;
  }

  public void startSequence() {
    if(!this.isPlaying()) {
      this.setPlaying(true);
      this.samplesToProcess = 0;
    }
  }

  public void stopSequence() {
    this.stop();

    this.volumeChanging = false;
    this.volumeChangingTimeRemaining = 0;
    this.volumeChangingTimeTotal = 0;

    this.fading = Fading.NONE;
    this.fadeCounter = 0;
    this.fadeTime = 0;

    for(final Voice voice : this.voices) {
      voice.clear();

      this.playingVoices = 0;
    }

    // TODO this isn't the greatest solution, but it does stop reverb from bleeding into the next sequence
    this.reverb.clear();
  }

  public int getSequenceVolume() {
    return Math.round(this.backgroundMusic.getVolume() * 0x80);
  }

  public int setSequenceVolume(final int volume) {
    if(this.backgroundMusic == null) {
      return -1;
    }

    final float oldVolume = this.backgroundMusic.getVolume();

    this.backgroundMusic.setVolume(volume / 128.0f);

    for(final Voice voice : this.voices) {
      if(voice.isUsed()) {
        voice.updateVolume();
      }
    }

    return Math.round(oldVolume * 0x80);
  }

  public int changeSequenceVolumeOverTime(final int volume, final int time) {
    if(this.backgroundMusic == null) {
      return -1;
    }

    this.volumeChanging = true;
    this.newVolume = volume / 128.0f;
    this.oldVolume = this.backgroundMusic.getVolume();
    this.volumeChangingTimeTotal = time * this.lookupTables.getEffectsOverTimeScale();
    this.volumeChangingTimeRemaining = time * this.lookupTables.getEffectsOverTimeScale();

    return Math.round(this.oldVolume * 0x80);
  }

  public int getVolumeOverTimeFlags() {
    int flags = 0;

    if(this.volumeChanging) {
      if(this.newVolume < this.oldVolume) {
        flags |= 0x4;
      } else {
        flags |= 0x8;
      }
    }

    return flags;
  }

  /** This isn't thread safe and should never be called from outside the Audio Thread synchronized block */
  public void changePitchResolution(final PitchResolution pitchResolution) {
    this.lookupTables.changeSampleRates(pitchResolution, this.sampleRate);
  }

  /** This isn't thread safe and should never be called from outside the Audio Thread synchronized block */
  public void changeSampleRate(final SampleRate sampleRate, final EffectsOverTimeGranularity effectsGranularity) {
    this.resetBuffers();

    final SampleRate old = this.sampleRate;
    this.sampleRate = sampleRate;

    // Has to switch between 2 and 3;
    if(effectsGranularity == EffectsOverTimeGranularity.Finer) {
      this.changeEffectsOverTimeGranularity(effectsGranularity);
    }

    this.effectsOverTimeSamples = sampleRate.value / (60 * this.lookupTables.getEffectsOverTimeScale());

    this.lookupTables.changeSampleRates(this.lookupTables.getPitchResolution(), sampleRate);
    this.reverb.changeSampleRate(sampleRate);
    this.outputBuffer = new short[(this.sampleRate.value / 60) * 2];

    if(this.backgroundMusic != null) {
      this.backgroundMusic.changeSampleRate(sampleRate);

      for(final Voice voice : this.voices) {
        voice.scaleSampleRate(old, sampleRate);
      }

      this.play();
    }
  }

  /** This isn't thread safe and should never be called from outside the Audio Thread synchronized block */
  public void changeEffectsOverTimeGranularity(final EffectsOverTimeGranularity effectsGranularity) {
    final int oldScale = this.lookupTables.getEffectsOverTimeScale();
    this.lookupTables.setEffectsOverTimeScale(effectsGranularity, this.sampleRate);

    this.effectsOverTimeSamples = this.sampleRate.value / (60 * this.lookupTables.getEffectsOverTimeScale());

    this.scaleTimeValues(oldScale, this.lookupTables.getEffectsOverTimeScale());

    for(final Voice voice : this.voices) {
      voice.scaleBreath(oldScale, this.lookupTables.getEffectsOverTimeScale());
    }
  }

  private void scaleTimeValues(final double oldScale, final double newScale) {
    this.fadeCounter = (int)Math.round(this.fadeCounter * (newScale / oldScale));
    this.fadeTime = (int)Math.round(this.fadeTime * (newScale / oldScale));
    this.volumeChangingTimeRemaining = (int)Math.round(this.volumeChangingTimeRemaining * (newScale / oldScale));
    this.volumeChangingTimeTotal = (int)Math.round(this.volumeChangingTimeTotal * (newScale / oldScale));
  }

  /** This isn't thread safe and should never be called from outside the Audio Thread synchronized block */
  public void changeInterpolationBitDepth(final InterpolationPrecision interpolationPrecision) {
    this.lookupTables.changeInterpolationBitDepth(interpolationPrecision);

    for(final Voice voice : this.voices) {
      voice.changeInterpolationBitDepth(interpolationPrecision);
    }
  }

  public SampleRate getSampleRate() {
    return this.sampleRate;
  }
}
