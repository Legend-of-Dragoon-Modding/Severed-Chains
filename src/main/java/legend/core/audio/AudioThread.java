package legend.core.audio;

import legend.core.DebugHelper;
import legend.core.audio.assets.Channel;
import legend.core.audio.assets.Instrument;
import legend.core.audio.assets.InstrumentLayer;
import legend.core.audio.assets.SequencedAudio;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.openal.ALC10.ALC_DEFAULT_DEVICE_SPECIFIER;
import static org.lwjgl.openal.ALC10.alcCloseDevice;
import static org.lwjgl.openal.ALC10.alcCreateContext;
import static org.lwjgl.openal.ALC10.alcDestroyContext;
import static org.lwjgl.openal.ALC10.alcGetString;
import static org.lwjgl.openal.ALC10.alcMakeContextCurrent;
import static org.lwjgl.openal.ALC10.alcOpenDevice;

public final class AudioThread implements Runnable {
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

  private final List<Sequence> sequences = new ArrayList<>();

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

      for(final Voice voice : this.voices) {
        voice.processBuffers();
      }

      this.tick();

      final long interval = System.nanoTime() - this.time;
      final int toSleep = (int)Math.max(0, this.nanosPerTick - interval) / 1_000_000;
      DebugHelper.sleep(toSleep);
      this.time += this.nanosPerTick;

      //Restart playback if the audio thread ever lags behind
      for(final Voice voice : this.voices) {
        voice.play();
      }
    }

    for(final Voice voice : this.voices) {
      voice.destroy();
    }

    alcDestroyContext(this.audioContext);
    alcCloseDevice(this.audioDevice);
  }

  private void tick() {
    for(int sample = 0; sample < this.samplesPerTick; sample++) {
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

      for(final Sequence sequence : this.sequences) {
        if(sequence.samplesToProcess > 0) {
          sequence.samplesToProcess--;
          continue;
        }

        while(sequence.samplesToProcess == 0) {
          final Sequence.Command command = sequence.getNextCommand();

          switch(command.getMidiCommand()) {
            case KEY_ON -> this.keyOn(sequence.getSequencedAudio(), command.getChannel(), command.getValue1(), command.getValue2());
            case KEY_OFF -> this.keyOff(sequence.getSequencedAudio(), command.getChannel(), command.getValue1());
            case POLYPHONIC_KEY_PRESSURE -> this.polyphonicKeyPressure();
            case CONTROL_CHANGE -> this.controlChange(sequence.getSequencedAudio(), command.getChannel(), command.getValue1(), command.getValue2());
            case PROGRAM_CHANGE -> this.programChange();
            case PITCH_BEND -> this.pitchBend(sequence.getSequencedAudio(), command.getChannel(), command.getValue1());
            case META -> this.meta(sequence.getSequencedAudio(), command.getValue1(), command.getValue2());
            default -> System.err.println("Unhandled message " + command.getMidiCommand());
          }

          sequence.setSamplesToProcess(command.getDeltaTime());
        }

        System.out.printf("[SEQUENCER] Delta ms %.02f%n", sequence.samplesToProcess / 44.1d);
      }

      for(final Voice voice : this.voices) {
        voice.tick(this.stereo);
      }
    }
  }

  private void keyOn(final SequencedAudio sequencedAudio, final int channelIndex, final int note, final int velocity) {
    System.out.printf("[SEQUENCER] Key On Channel: %d Note: %d Velocity: %d%n", channelIndex, note, velocity);
    if(velocity == 0) {
      this.keyOff(sequencedAudio, channelIndex, note);
    }

    //TODO volume 0

    final Channel channel = sequencedAudio.getChannel(channelIndex);
    final Instrument instrument = channel.getInstrument();
    for(final InstrumentLayer layer : instrument.getLayers(note)) {
      final Voice voice = this.selectVoice();

      voice.keyOn(channel, instrument, layer, note, sequencedAudio.getVelocity(velocity), sequencedAudio.getBreathControls(), this.playingVoices);
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
    int voiceIndex = this.voices.length;

    for(int i = 0; i < this.voices.length; i++) {
      final Voice voice = this.voices[i];

      if(voice.isLowPriority()) {
        final int currentPriority = voice.getPriorityOrder();

        if(currentPriority < lastVoice) {
          lastVoice = currentPriority;
          voiceIndex = i;
        }
      }
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

  private void keyOff(final SequencedAudio sequencedAudio, final int channelIndex, final int note) {
    System.out.printf("[SEQUENCER] Key Off Channel: %d Note: %d%n", channelIndex, note);
    final Channel channel = sequencedAudio.getChannel(channelIndex);

    for(final Voice voice : this.voices) {
      if(voice.getChannel() == channel && voice.getNote() == note) {
        voice.keyOff();
      }
    }
  }

  private void polyphonicKeyPressure() {

  }

  private void controlChange(final SequencedAudio sequencedAudio, final int channelIndex, final int control, final int value) {
    switch(control) {
      case 0x01 -> this.modulation(sequencedAudio, channelIndex, value);
      case 0x02 -> this.breathControl(sequencedAudio, channelIndex, value);
      case 0x07 -> this.volume(sequencedAudio, channelIndex, value);
      case 0x0A -> this.pan(sequencedAudio, channelIndex, value);
      default -> System.err.printf("[SEQUENCER] Bad Control Change Channel: %d Command: 0x%x Value: 0x%x%n", channelIndex, control, value);
    }
  }

  private void modulation(final SequencedAudio sequencedAudio, final int channelIndex, final int value) {
    System.out.printf("[SEQUENCER] Control Change Channel: %d Modulation: %d%n", channelIndex, value);

    final Channel channel = sequencedAudio.getChannel(channelIndex);
    channel.setModulation(value);

    for(final Voice voice : this.voices) {
      if(voice.isUsed() && voice.getChannel() == channel) {
        voice.setModulation(value);
      }
    }
  }

  private void breathControl(final SequencedAudio sequencedAudio, final int channelIndex, final int value) {
    System.out.printf("[SEQUENCER] Control Change Channel: %d Breath Control: %d%n", channelIndex, value);

    final int breath = 240 / (60 - value * 58 / 127);

    final Channel channel = sequencedAudio.getChannel(channelIndex);
    channel.setBreath(breath);

    for(final Voice voice : this.voices) {
      if(voice.isUsed() && voice.getChannel() == channel) {
        voice.setBreath(breath);
      }
    }
  }

  private void volume(final SequencedAudio sequencedAudio, final int channelIndex, final int value) {
    System.out.printf("[SEQUENCER] Control Change Channel: %d Volume: %d%n", channelIndex, value);

    final Channel channel = sequencedAudio.getChannel(channelIndex);
    channel.setVolume(value);
    //TODO multiply by sssq reader value and div by 0x80
    channel.setAdjustedVolume(value);

    for(final Voice voice : this.voices) {
      if(voice.isUsed() && voice.getChannel() == channel) {
        voice.updateVolume();
      }
    }
  }

  private void pan(final SequencedAudio sequencedAudio, final int channelIndex, final int value) {
    System.out.printf("[SEQUENCER] Control Change Channel: %d Pan: %d%n", channelIndex, value);

    final Channel channel = sequencedAudio.getChannel(channelIndex);

    if(!this.stereo) {
      channel.setPan(64);
      return;
    }

    channel.setPan(value);

    for(final Voice voice : this.voices) {
      if(voice.isUsed() && voice.getChannel() == channel) {
        voice.updateVolume();
      }
    }
  }

  private void programChange() {

  }

  private void pitchBend(final SequencedAudio sequencedAudio, final int channelIndex, final int value) {
    System.out.printf("[SEQUENCER] Pitch Bend Channel: %d, Value: %d%n", channelIndex, value);
    final Channel channel = sequencedAudio.getChannel(channelIndex);
    channel.setPitchBend(value);

    for(final Voice voice : this.voices) {
      if(voice.isUsed() && voice.getChannel() == channel) {
        voice.updateSampleRate();
      }
    }
  }

  private void meta(final SequencedAudio sequencedAudio, final int command, final int value) {
    System.out.printf("[SEQUENCER] Tempo change: %d%n", value);

    switch(command) {
      case 0x51 -> {
        sequencedAudio.setTempo(value);
      }
      default -> System.err.printf("[SEQUENCER] Unhandled meta event %d value %d%n", command, value);
    }
  }

  public synchronized void stop() {
    this.running = false;
    this.notify();
  }

  //TODO this needs to be made thread safe
  public void loadSequence(final SequencedAudio sequencedAudio, final int sequenceIndex) {
    final Sequence sequence = new Sequence(sequencedAudio, sequenceIndex);
    this.sequences.add(sequence);
  }
}
