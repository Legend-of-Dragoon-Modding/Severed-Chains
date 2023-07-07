package legend.core.audio;

import legend.core.DebugHelper;
import legend.core.MathHelper;
import legend.core.audio.assets.BackgroundMusic;
import legend.core.audio.assets.Channel;
import legend.core.audio.assets.Instrument;
import legend.core.audio.assets.InstrumentLayer;
import legend.game.sound.ReverbConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import static legend.game.Scus94491BpeSegment_8005.reverbConfigs_80059f7c;
import static org.lwjgl.openal.ALC10.ALC_DEFAULT_DEVICE_SPECIFIER;
import static org.lwjgl.openal.ALC10.alcCloseDevice;
import static org.lwjgl.openal.ALC10.alcCreateContext;
import static org.lwjgl.openal.ALC10.alcDestroyContext;
import static org.lwjgl.openal.ALC10.alcGetString;
import static org.lwjgl.openal.ALC10.alcMakeContextCurrent;
import static org.lwjgl.openal.ALC10.alcOpenDevice;

public final class AudioThread implements Runnable {
  private static final Logger LOGGER = LogManager.getFormatterLogger();
  private static final Marker SEQUENCER_MARKER = MarkerManager.getMarker("SEQUENCER");

  private final long audioContext;
  private final long audioDevice;
  private final int nanosPerTick;
  private final int samplesPerTick;
  private final boolean stereo;
  private final Voice[] voices;
  private int voiceIndex;
  private int playingVoices;

  private long time;
  private boolean running;
  private boolean paused;

  private final BufferedSound output;

  private BackgroundMusic backgroundMusic;

  private final Reverberizer reverb = new Reverberizer();
  private float reverbVolumeLeft;
  private float reverbVolumeRight;

  /**
   * @param frequency Amount of updates per second. Has to be a divisor of 1_000_000_000 and 44_100 (1, 2, 4, 5, 10, 20, 25, 50, 100).
   * @param voiceCount Amount of voices that can play at once. Retail uses 24.
   */
  public AudioThread(final int frequency, final boolean stereo, final int voiceCount) {
    if(1_000_000_000 % frequency != 0) {
      throw new IllegalArgumentException("Nanos (1_000_000_000) is not divisible by frequency " + frequency);
    }

    if(44_100 % frequency != 0) {
      throw new IllegalArgumentException("Sample Rate (44_100) is not divisible by frequency " + frequency);
    }

    this.nanosPerTick = 1_000_000_000 / frequency;
    this.samplesPerTick = 44_100 / frequency;

    final String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
    this.audioDevice = alcOpenDevice(defaultDeviceName);

    final int[] attributes = {0};
    this.audioContext = alcCreateContext(this.audioDevice, attributes);
    alcMakeContextCurrent(this.audioContext);

    final ALCCapabilities alcCapabilities = ALC.createCapabilities(this.audioDevice);
    final ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

    //TODO disable audio instead of crashing
    if(!alCapabilities.OpenAL10) {
      throw new UnsupportedOperationException("Device does not support OpenAL10");
    }

    this.stereo = stereo;

    final LookupTables lookupTables = new LookupTables(64, 512);

    this.voices = new Voice[voiceCount];

    this.voices[0] = new Voice(0, lookupTables, this.samplesPerTick, stereo, null);

    for(int voice = 1; voice < this.voices.length; voice++) {
      this.voices[voice] = new Voice(voice, lookupTables, this.samplesPerTick, stereo, this.voices[voice - 1]);
    }

    this.output = new BufferedSound(this.samplesPerTick, stereo);

    this.setReverbConfig(reverbConfigs_80059f7c.get(2).config_02);
    this.setReverbVolume(0x30, 0x30);
  }

  @Override
  public void run() {
    this.running = true;
    this.paused = false;

    this.time = System.nanoTime();

    while(this.running) {
      while(this.paused) {
        try {
          this.wait();
        } catch(Exception ex) {

        }
      }

      this.output.processBuffers();
      this.tick();

      final long interval = System.nanoTime() - this.time;
      final int toSleep = (int)Math.max(0, this.nanosPerTick - interval) / 1_000_000;
      DebugHelper.sleep(toSleep);
      this.time += this.nanosPerTick;

      //Restart playback if the audio thread ever lags behind
      this.output.play();
    }

    this.output.destroy();

    alcDestroyContext(this.audioContext);
    alcCloseDevice(this.audioDevice);
  }

  private void tick() {
    for(int sample = 0; sample < this.samplesPerTick; sample++) {
      this.clearVoices();

      this.tickSequences();

      float sumLeft = 0.0f;
      float sumRight = 0.0f;

      float sumReverbLeft = 0.0f;
      float sumReverbRight = 0.0f;

      for(final Voice voice : this.voices) {
        voice.tick();

        sumLeft += voice.getOutLeft();
        sumRight += voice.getOutRight();

        if(voice.getLayer() != null && voice.getLayer().isReverb()) {
          sumReverbLeft += voice.getOutLeft();
          sumReverbRight += voice.getOutRight();
        }
      }

      this.reverb.processReverb(sumReverbLeft, sumReverbRight);

      sumLeft += this.reverb.getOutputLeft() * this.reverbVolumeLeft;
      sumRight += this.reverb.getOutputRight() * this.reverbVolumeRight;

      this.output.bufferSample((short)(MathHelper.clamp(sumLeft * 0x8000, -0x8000, 0x7fff)));

      if(this.stereo) {
        this.output.bufferSample((short)(MathHelper.clamp(sumRight * 0x8000, -0x8000, 0x7fff)));
      }
    }
  }

  private void clearVoices() {
    for(final Voice voice : this.voices) {
      if(voice.isUsed() && voice.isFinished()) {
        if(this.playingVoices > 0) {
          this.playingVoices--;
        }

        for(final Voice voice2 : this.voices) {
          if(voice2.getPriorityOrder() > voice.getPriorityOrder()) {
            voice2.setPriorityOrder(voice2.getPriorityOrder() - 1);
          }
        }
        voice.clear();
      }
    }
  }

  private void tickSequences() {
    this.backgroundMusicSequencer();
  }

  private void backgroundMusicSequencer() {
    if(this.backgroundMusic == null) {
      return;
    }

    if(this.backgroundMusic.getSamplesToProcess() > 0) {
      this.backgroundMusic.tickSequence();
      return;
    }

    while(this.backgroundMusic.getSamplesToProcess() == 0) {
      final BackgroundMusic.Command command = this.backgroundMusic.getNextCommand();

      switch(command.getMidiCommand()) {
        case KEY_OFF -> this.musicKeyOff(command.getChannel(), command.getValue1());
        case KEY_ON -> this.musicKeyOn(command.getChannel(), command.getValue1(), command.getValue2());
        case CONTROL_CHANGE -> this.musicControlChange(command.getChannel(), command.getValue1(), command.getValue2());
        case PROGRAM_CHANGE -> this.musicProgramChange(command.getChannel(), command.getValue1());
        case PITCH_BEND -> this.musicPitchBend(command.getChannel(), command.getValue1());
        case META -> this.musicMeta(command.getValue1(), command.getValue2());
        default -> LOGGER.warn(SEQUENCER_MARKER, "Unhandled command %s", command.getMidiCommand());
      }

      this.backgroundMusic.setSamplesToProcess(command.getDeltaTime());
    }

    LOGGER.info(SEQUENCER_MARKER, "Delta ms %.02f", this.backgroundMusic.getSamplesToProcess() / 44.1d);
  }

  private void musicKeyOn(final int channelIndex, final int note, final int velocity) {
    LOGGER.info(SEQUENCER_MARKER, "Ken On Channel: %d, Note %d, Velocity: %d", channelIndex, note, velocity);

    if(velocity == 0) {
      this.musicKeyOff(channelIndex, note);
      return;
    }

    final Channel channel = this.backgroundMusic.getChannel(channelIndex);

    if(channel.getVolume() == 0) {
      return;
    }

    final Instrument instrument = channel.getInstrument();

    if(instrument == null) {
      return;
    }

    for(final InstrumentLayer layer : instrument.getLayers(note)) {
      final Voice voice = this.selectVoice();

      voice.keyOn(channel, instrument, layer, note, this.backgroundMusic.getVelocity(velocity), this.backgroundMusic.getBreathControls(), this.playingVoices);
    }
  }

  private Voice selectVoice() {
    for(int i = 0; i < this.voices.length; i++) {
      this.voiceIndex = ++this.voiceIndex % this.voices.length;

      if(!this.voices[this.voiceIndex].isUsed()) {
        this.playingVoices++;
        return this.voices[this.voiceIndex];
      }
    }

    int lastVoice = this.voices.length;

    for(int i = 0; i < this.voices.length; i++) {
      final Voice voice = this.voices[i];

      if(voice.isLowPriority()) {
        final int currentPriority = voice.getPriorityOrder();

        if(currentPriority < lastVoice) {
          lastVoice = currentPriority;
          this.voiceIndex = i;
        }
      }
    }

    if(lastVoice == this.voices.length) {
      for(int i = 0; i < this.voices.length; i++) {
        final Voice voice = this.voices[i];

        //TODO if not poly pressure

        final int currentPriority = voice.getPriorityOrder();

        if(currentPriority < this.voices.length) {
          lastVoice = currentPriority;
          this.voiceIndex = i;
          break;
        }
      }
    }

    if(this.voiceIndex == this.voices.length) {
      throw new RuntimeException("Voice pool overflow");
    }

    for(final Voice voice : this.voices) {
      if(lastVoice < voice.getPriorityOrder()) {
        voice.setPriorityOrder(voice.getPriorityOrder() - 1);
      }
    }

    return this.voices[this.voiceIndex];
  }

  private void musicKeyOff(final int channelIndex, final int note) {
    LOGGER.info(SEQUENCER_MARKER, "Key Off Channel: %d Note: %d", channelIndex, note);

    final Channel channel = this.backgroundMusic.getChannel(channelIndex);

    for(final Voice voice : this.voices) {
      if(voice.isUsed() && voice.getChannel() == channel && voice.getNote() == note) {
        voice.keyOff();
      }
    }
  }

  private void musicControlChange(final int channelIndex, final int control, final int value) {
    switch(control) {
      case 0x01 -> this.musicModulation(channelIndex, value);
      case 0x02 -> this.musicBreathControl(channelIndex, value);
      case 0x06 -> this.musicDataEntry(value);
      case 0x07 -> this.musicVolume(channelIndex, value);
      case 0x0A -> this.musicPan(channelIndex, value);
      case 0x62 -> this.musicDataEntryLsb(value);
      case 0x63 -> this.musicDataEntryMsb(value);
      default -> LOGGER.warn(SEQUENCER_MARKER, "Bad Control Change Channel: %d Command: 0x%x Value: 0x%x%n", channelIndex, control, value);
    }
  }

  private void musicModulation(final int channelIndex, final int modulation) {
   LOGGER.info(SEQUENCER_MARKER, "Control Change Modulation Channel: %d Modulation: %d", channelIndex, modulation);

    final Channel channel = this.backgroundMusic.getChannel(channelIndex);
    channel.setModulation(modulation);

    for(final Voice voice : this.voices) {
      if(voice.isUsed() && voice.getChannel() == channel) {
        voice.setModulation(modulation);
      }
    }
  }

  private void musicBreathControl(final int channelIndex, final int value) {
    LOGGER.info(SEQUENCER_MARKER, "Control Change Breath Control Channel: %d Breath: %d", channelIndex, value);

    final int breath = 240 / (60 - value * 58 / 127);

    final Channel channel = this.backgroundMusic.getChannel(channelIndex);
    channel.setBreath(breath);

    for(final Voice voice : this.voices) {
      if(voice.isUsed() && voice.getChannel() == channel) {
        voice.setBreath(breath);
      }
    }
  }

  private void musicDataEntry(final int value) {
    LOGGER.info(SEQUENCER_MARKER, "Data entry NRPN: %d Value: %d", this.backgroundMusic.getNrpn(), value);

    switch(this.backgroundMusic.getNrpn()) {
      case 0x00 -> {
        this.backgroundMusic.setRepeatCount(value);
        return;
      }
      case 0x04 -> {
        //TODO Attack (linear)
      }
      case 0x05 -> {
        //TODO Attack (exponential)
      }
      case 0x06 -> {
        //TODO Decay Shift
      }
      case 0x07 -> {
        //TODO Sustain Level
      }
      case 0x08 -> {
        //TODO Sustain (linear)
      }
      case 0x09 -> {
        //TODO Sustain (exponential)
      }
      case 0x0a -> {
        //TODO Release (linear)
      }
      case 0x0b -> {
        //TODO Release (exponential)
      }
      case 0x0c -> {
        //TODO Sustain direction
      }
      case 0x0f -> {
        this.setReverbConfig(reverbConfigs_80059f7c.get(value).config_02);
      }
      case 0x10 -> {
        this.setReverbVolume(value, value);
      }
      case 0x11, 0x12, 0x13 -> {
        assert false;
      }
    }
  }

  private void musicVolume(final int channelIndex, final int volume) {
   LOGGER.info(SEQUENCER_MARKER, "Control Change Volume Channel: %d Volume: %d", channelIndex, volume);

    final Channel channel = this.backgroundMusic.getChannel(channelIndex);
    channel.changeVolume(volume, this.backgroundMusic.getVolume());

    for(final Voice voice : this.voices) {
      if(voice.isUsed() && voice.getChannel() == channel) {
        voice.updateVolume();
      }
    }
  }

  private void musicPan(final int channelIndex, final int pan) {
    LOGGER.info(SEQUENCER_MARKER, "Control Change Pan Channel: %d Pan: %d", channelIndex, pan);

    final Channel channel = this.backgroundMusic.getChannel(channelIndex);

    if(!this.stereo) {
      channel.setPan(64);
      return;
    }

    channel.setPan(pan);

    for(final Voice voice : this.voices) {
      if(voice.isUsed() && voice.getChannel() == channel) {
        voice.updateVolume();
      }
    }
  }

  private void musicDataEntryLsb(final int value) {
    LOGGER.info(SEQUENCER_MARKER, "Data Entry LSB Value: 0x%x%", value);

    this.backgroundMusic.dataEntryLsb(value);
  }

  private void musicDataEntryMsb(final int command) {
    LOGGER.info(SEQUENCER_MARKER, "Data Entry MSB Value: 0x%x%", command);

    this.backgroundMusic.dataEntryMsb(command);
  }

  private void musicProgramChange(final int channelIndex, final int instrumentIndex) {
    LOGGER.info(SEQUENCER_MARKER, "Program Change Pan Channel: %d Instrument: %d", channelIndex, instrumentIndex);

    //TODO this should only happen, if sound isn't playing yet.
    final Channel channel = this.backgroundMusic.getChannel(channelIndex);
    channel.setInstrument(instrumentIndex);
    channel.setPitchBend(0x40);
    channel.set_0b(0x40);
  }

  private void musicPitchBend(final int channelIndex, final int pitchBend) {
    LOGGER.info(SEQUENCER_MARKER, "Pitch Bend Channel: %d, Pitch Bend: %d%", channelIndex, pitchBend);

    final Channel channel = this.backgroundMusic.getChannel(channelIndex);
    channel.setPitchBend(pitchBend);

    for(final Voice voice : this.voices) {
      if(voice.isUsed() && voice.getChannel() == channel) {
        voice.updateSampleRate();
      }
    }
  }

  private void musicMeta(final int command, final int value) {
    switch(command) {
      case 0x51 -> {
        LOGGER.info(SEQUENCER_MARKER, "Tempo change: %d%", value);

        this.backgroundMusic.setTempo(value);
      }
      default -> LOGGER.warn(SEQUENCER_MARKER, "Unhandled meta event %d value %d", command, value);
    }
  }

  public synchronized void stop() {
    this.running = false;
    this.notify();
  }

  //TODO this needs to be made thread safe
  public void loadBackgroundMusic(final BackgroundMusic backgroundMusic) {
    this.backgroundMusic = backgroundMusic;
  }

  private void setReverbConfig(final ReverbConfig config) {
    this.reverb.setConfig(config);
  }

  private void setReverbVolume(final int reverbVolumeLeft, final int reverbVolumeRight) {
    this.reverbVolumeLeft = (reverbVolumeLeft << 8) / 32768.0f;
    this.reverbVolumeRight = (reverbVolumeRight << 8) / 32768.0f;
  }
}
