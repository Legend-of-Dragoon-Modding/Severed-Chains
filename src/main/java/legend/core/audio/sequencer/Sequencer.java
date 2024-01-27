package legend.core.audio.sequencer;

import legend.core.MathHelper;
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
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static legend.game.Scus94491BpeSegment_8005.reverbConfigs_80059f7c;
import static org.lwjgl.openal.AL10.AL_BUFFERS_PROCESSED;
import static org.lwjgl.openal.AL10.AL_BUFFERS_QUEUED;
import static org.lwjgl.openal.AL10.AL_FORMAT_STEREO16;
import static org.lwjgl.openal.AL10.AL_PLAYING;
import static org.lwjgl.openal.AL10.AL_SOURCE_STATE;
import static org.lwjgl.openal.AL10.alBufferData;
import static org.lwjgl.openal.AL10.alDeleteBuffers;
import static org.lwjgl.openal.AL10.alDeleteSources;
import static org.lwjgl.openal.AL10.alGenBuffers;
import static org.lwjgl.openal.AL10.alGenSources;
import static org.lwjgl.openal.AL10.alGetSourcei;
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.AL10.alSourceQueueBuffers;
import static org.lwjgl.openal.AL10.alSourceStop;
import static org.lwjgl.openal.AL10.alSourceUnqueueBuffers;

public final class Sequencer {
  private static final Logger LOGGER = LogManager.getFormatterLogger();
  private static final Marker SEQUENCER_MARKER = MarkerManager.getMarker("SEQUENCER");
  // TODO switch between mono and stereo
  private final boolean stereo;
  private final Voice[] voices;
  private int playingVoices;
  private final int[] voiceOutputBuffer = new int[2];
  private final int[] voiceReverbBuffer = new int[2];
  // TODO consider making this variable length for mono, but it might be better to simply always playback as stereo, just with down mixing
  private final short[] outputBuffer;

  private final Reverberizer reverb = new Reverberizer();
  private float reverbVolumeLeft = 0x3000 / 32_768f;
  private float reverbVolumeRight = 0x3000 / 32_768f;

  /** Use powers of 2 to avoid % operator */
  private static final int BUFFER_COUNT = 4;
  private final int[] buffers = new int[BUFFER_COUNT];
  private int bufferIndex;
  private final int sourceId;

  private float mainVolumeLeft = 0.5f;
  private float mainVolumeRight = 0.5f;
  private Fading fading = Fading.NONE;
  private int fadeInVolume;
  private int fadeOutVolumeLeft;
  private int fadeOutVolumeRight;
  private int fadeTime;
  private int fadeCounter;

  private final Map<Class, CommandCallback> commandCallbackMap = new HashMap<>();

  private BackgroundMusic backgroundMusic;
  private final Queue<BackgroundMusic> backgroundMusicQueue = new LinkedList<>();
  private final Lock bgmLock = new ReentrantLock();
  private boolean unload;
  private int samplesToProcess;

  public Sequencer (final int frequency, final boolean stereo, final int voiceCount, final int interpolationBitDepth) {
    if(44_100 % frequency != 0) {
      throw new IllegalArgumentException("Sample Rate (44_100) is not divisible by frequency");
    }

    this.outputBuffer = new short[(44_100 / frequency) * 2];

    this.stereo = stereo;

    if(interpolationBitDepth > 12) {
      throw new IllegalArgumentException("Interpolation Bit Depth must be less or equal to 12");
    }

    final LookupTables lookupTables = new LookupTables(interpolationBitDepth);

    this.voices = new Voice[voiceCount];

    for(int voice = 0; voice < this.voices.length; voice++) {
      this.voices[voice] = new Voice(voice, lookupTables, interpolationBitDepth);
    }

    this.sourceId = alGenSources();

    alGenBuffers(this.buffers);

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
  }

  public void tick() {
    for(int sample = 0; sample < this.outputBuffer.length; sample += 2) {
      this.clearFinishedVoices();

      this.tickSequence();

      this.handleFadeInOut();

      this.voiceOutputBuffer[0] = 0;
      this.voiceOutputBuffer[1] = 0;

      this.voiceReverbBuffer[0] = 0;
      this.voiceReverbBuffer[1] = 0;

      for(final Voice voice : this.voices) {
        voice.tick(this.voiceOutputBuffer, this.voiceReverbBuffer);
      }

      this.reverb.processReverb(this.voiceReverbBuffer[0] / 32_768f, this.voiceReverbBuffer[1] / 32_768f);

      this.outputBuffer[sample] = (short)MathHelper.clamp((int)((this.voiceOutputBuffer[0] + (this.reverb.getOutputLeft() * this.reverbVolumeLeft)) * this.mainVolumeLeft), -0x8000, 0x7fff);
      this.outputBuffer[sample + 1] = (short)MathHelper.clamp((int)((this.voiceOutputBuffer[1] + (this.reverb.getOutputRight() * this.reverbVolumeRight)) * this.mainVolumeRight), -0x8000, 0x7fff);
    }

    this.bufferOutput();
  }

  public int buffersToQueue() {
    if(this.backgroundMusic == null) {
      return 0;
    }

    return BUFFER_COUNT - 1 - alGetSourcei(this.sourceId, AL_BUFFERS_QUEUED);
  }

  public void processMusicQueue() {
    this.bgmLock.lock();
    try {
      if(this.unload) {
        this.unload = false;

        this.backgroundMusic = null;
        for(final Voice voice : this.voices) {
          voice.clear();
        }

        this.playingVoices = 0;

        alSourceStop(this.sourceId);

        this.processBuffers();
      }

      if(!this.backgroundMusicQueue.isEmpty()) {
        for(final Voice voice : this.voices) {
          voice.clear();
        }

        this.playingVoices = 0;

        this.backgroundMusic = this.backgroundMusicQueue.remove();
      }
    } finally {
      this.bgmLock.unlock();
    }
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
    LOGGER.info(SEQUENCER_MARKER, "Control Change Volume Channel: %d Volume: %d", volumeChange.getChannel().getIndex(), volumeChange.getVolume());

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
    programChange.getChannel().setPitchBend(0x40);
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

  private void setReverbConfig(final ReverbConfig config) {
    this.reverb.setConfig(config);
  }

  public void setReverbVolume(final int reverbVolumeLeft, final int reverbVolumeRight) {
    this.reverbVolumeLeft = (reverbVolumeLeft << 8) / 32768.0f;
    this.reverbVolumeRight = (reverbVolumeRight << 8) / 32768.0f;
  }

  private void endOfTrack(final EndOfTrack endOfTrack) {
    this.backgroundMusic = null;
  }

  public void processBuffers() {
    final int processedBufferCount = alGetSourcei(this.sourceId, AL_BUFFERS_PROCESSED);

    for(int buffer = 0; buffer < processedBufferCount; buffer++) {
      final int processedBufferName = alSourceUnqueueBuffers(this.sourceId);
      alDeleteBuffers(processedBufferName);
    }

    alGenBuffers(this.buffers);
  }

  private void bufferOutput() {
    final int bufferId = this.buffers[this.bufferIndex++];
    alBufferData(bufferId, AL_FORMAT_STEREO16, this.outputBuffer, 44_100);
    alSourceQueueBuffers(this.sourceId, bufferId);
    this.bufferIndex &= BUFFER_COUNT - 1;
  }

  public void play() {
    if(alGetSourcei(this.sourceId, AL_SOURCE_STATE) == AL_PLAYING) {
      return;
    }

    alSourcePlay(this.sourceId);
  }

  public void destroy() {
    alSourceStop(this.sourceId);

    final int processedBufferCount = alGetSourcei(this.sourceId, AL_BUFFERS_PROCESSED);

    for(int buffer = 0; buffer < processedBufferCount; buffer++) {
      final int processedBufferName = alSourceUnqueueBuffers(this.sourceId);
      alDeleteBuffers(processedBufferName);
    }

    alDeleteBuffers(this.buffers);
    alDeleteSources(this.sourceId);
  }

  public void setMainVolume(final int left, final int right) {
    this.mainVolumeLeft = left >= 0x80 ? 1 : left / 254f;

    this.mainVolumeRight = right >= 0x80 ? 1 : right / 254f;
  }

  private void handleFadeInOut() {
    if(this.fadeTime == 0) {
      return;
    }

    switch(this.fading) {
      case FADE_IN -> {
        final int volume = (this.fadeInVolume * this.fadeCounter) / this.fadeTime;
        this.fadeCounter++;
        this.setMainVolume(volume, volume);
      }
      case FADE_OUT -> {
        final int volumeLeft = (this.fadeOutVolumeLeft * (this.fadeTime - this.fadeCounter)) / this.fadeTime;
        final int volumeRight = (this.fadeOutVolumeRight * (this.fadeTime - this.fadeCounter)) / this.fadeTime;
        this.fadeCounter++;
        this.setMainVolume(volumeLeft, volumeRight);
      }
    }

    if(this.fadeCounter > this.fadeTime) {
      this.fading = Fading.NONE;
      this.fadeCounter = 0;
    }
  }

  public void fadeIn(final int time, final int volume) {
    this.fadeTime = time;
    this.fadeInVolume = volume;
    this.fading = Fading.FADE_IN;
  }

  public void fadeOut(final int time) {
    this.fadeTime = time;
    this.fadeOutVolumeLeft = (int)(this.mainVolumeLeft * 0x7e);
    this.fadeOutVolumeRight = (int)(this.mainVolumeRight * 0x7e);
    this.fading = Fading.FADE_OUT;
  }

  public int getSongId() {
    if(this.backgroundMusic == null) {
      return -1;
    }

    return this.backgroundMusic.getSongId();
  }

  public void loadBackgroundMusic(final BackgroundMusic backgroundMusic) {
    this.backgroundMusicQueue.add(backgroundMusic);
  }

  public void unloadMusic() {
    this.unload = true;
  }
}
