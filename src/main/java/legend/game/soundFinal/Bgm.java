package legend.game.soundFinal;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import legend.game.sound.Sssq;
import legend.game.unpacker.FileData;

import java.util.List;

public final class Bgm {
  static final boolean STEREO = true;
  private final Voice[] voicePool = new Voice[128];
  private final MidiState state;
  private final Int2ObjectMap<Channel> channels = new Int2ObjectArrayMap<>();

  private int samplesToProcess;

  public Bgm(final Sssq sssq, final FileData sshdData, final FileData soundBankData) {
    final SoundBank soundBank = new SoundBank(soundBankData);
    final Sshd sshd = new Sshd(sshdData, soundBank);

    for(int i = 0; i < 0x10; i++) {
      if(sssq.channelInfo_10[i].instrumentIndex_02 != -1) { // preset (SShd entry)
        this.channels.put(i, new Channel(i, sssq.channelInfo_10[i], sshd));
      }
    }

    for(int voice = 0; voice < this.voicePool.length; voice++) {
      this.voicePool[voice] = new Voice();
    }

    this.state = new MidiState(sssq.data());
    this.state.ticksPerQuarterNote = sssq.ticksPerQuarterNote_02;
    this.state.tempo = sssq.tempo_04;
    this.state.ticksPerMs = (this.state.tempo * this.state.ticksPerQuarterNote) / 60_000d;
  }

  public boolean tick(final int sampleCount) {
    int processed = 0;

    while(processed < sampleCount) {
      while(this.samplesToProcess > 0 && processed < sampleCount) {
        for(final Voice voice : this.voicePool) {
          voice.processSample();
        }

        this.samplesToProcess--;
        processed++;
      }

      if(processed == sampleCount) {
        return true;
      }

      while(this.state.deltaMs == 0.0f) {
        this.readEvent();

        if(this.state.command != MidiCommand.META) {
          switch(this.state.command) {
            case KEY_OFF -> this.keyOff();
            case KEY_ON -> this.keyOn();
            case POLYPHONIC_KEY_PRESSURE -> this.polyphonicKeyPressure();
            case CONTROL_CHANGE -> this.controlChange();
            case PROGRAM_CHANGE -> this.programChange();
            case PITCH_BEND -> this.pitchBend();
            default -> throw new RuntimeException("Bad message " + this.state.command);
          }
        } else {
          this.handleMeta();

          if(this.state.endOfTrack) {
            for(final Voice voice : this.voicePool) {
              //TODO process end of track (push remaining samples)
            }
            return false;
          }
        }

        this.state.deltaMs = (float) (this.readVarInt() / this.state.ticksPerMs);
        this.samplesToProcess = (int) (this.state.deltaMs * 44.1f);
      }

      System.out.printf("Delta ms %.02f%n", this.state.deltaMs);
      this.state.deltaMs = 0;
    }

    return true;
  }

  private void readEvent() {
    this.state.event = this.state.sequence.readUByte(this.state.offset);

    if((this.state.event & 0x80) != 0) {
      this.state.offset++;
      this.state.previousEvent = this.state.event;

      this.state.command = MidiCommand.fromEvent(this.state.event);
      this.state.channel = this.state.event & 0x0f;

      return;
    }

    this.state.event = this.state.previousEvent;
  }

  private void handleMeta() {
    final int meta = this.state.sequence.readUByte(this.state.offset);
    this.state.offset++;

    switch(meta) {
      case 0x2f -> {
        System.out.println("End of track");
        this.state.endOfTrack = true;
      }

      case 0x51 -> {
        this.state.tempo = this.state.sequence.readUShort(this.state.offset);
        this.state.ticksPerMs = (this.state.tempo * this.state.ticksPerQuarterNote) / 60_000d;
        this.state.offset += 2;

        System.out.println("Tempo " + this.state.tempo);
      }
    }
  }

  private int readVarInt() {
    int varint = 0;

    while(true) {
      final int part = this.state.sequence.readUByte(this.state.offset);
      this.state.offset++;

      varint <<= 7;
      varint |= part & 0x7f;

      if((part & 0x80) == 0) {
        break;
      }
    }

    return varint;
  }

  private void keyOn() {
    final int channelIndex = this.state.channel;
    final int note = this.state.sequence.readUByte(this.state.offset++);
    final int velocity = this.state.sequence.readUByte(this.state.offset++); //TODO

    final Channel channel = this.channels.get(channelIndex);
    final List<Layer> layers = channel.getLayers(note);

    int layerIndex = 0;
    int voice;
    for(voice = 0; voice < this.voicePool.length && layerIndex < layers.size(); voice++) {
      if(this.voicePool[voice].getChannel() != null && this.voicePool[voice].getChannel().getChannelIndex() == channelIndex && this.voicePool[voice].getNote() == note) {
        this.voicePool[voice].keyOn(this.channels.get(channelIndex), layers.get(layerIndex), note, velocity, true);
        layerIndex++;

        System.out.println("Key on (Reset) Channel: " + channelIndex + " [Voice: " + voice + "] Note: " + note);
      }
    }

    for(voice = 0; voice < this.voicePool.length && layerIndex < layers.size(); voice++) {
      if(this.voicePool[voice].empty) {
        this.voicePool[voice].keyOn(this.channels.get(channelIndex), layers.get(layerIndex), note, velocity, false);
        layerIndex++;

        System.out.println("Key on Channel: " + channelIndex + " [Voice: " + voice + "] Note: " + note);
      }
    }

    if(voice == this.voicePool.length) {
      throw new IndexOutOfBoundsException("Voice Pool overflow!");
    }
  }

  private void keyOff() {
    final int channelIndex = this.state.channel;
    final int note = this.state.sequence.readUByte(this.state.offset++);
    final int velocity = this.state.sequence.readUByte(this.state.offset++); //TODO

    for(int voice = 0; voice < this.voicePool.length; voice++) {
      if(this.voicePool[voice].getChannel() != null && this.voicePool[voice].getChannel().getChannelIndex() == channelIndex && this.voicePool[voice].getNote() == note) {
        this.voicePool[voice].keyOff();

        System.out.println("Key off Channel: " + channelIndex + " [Voice: " + voice + ']');
      }
    }
  }

  private void polyphonicKeyPressure() {
    this.state.offset += 2;
  }

  private void controlChange() {
    final int channelIndex = this.state.channel;
    final int command = this.state.sequence.readUByte(this.state.offset++);
    final int value = this.state.sequence.readUByte(this.state.offset++);

    switch(command) {
      case 0x01 -> {
        this.channels.get(channelIndex).setModulation(value);
        System.out.println("Control Change Channel: " + channelIndex + " Modulation: " + value);
      }

      case 0x07 -> {
        this.channels.get(channelIndex).setVolume(value);
        System.out.println("Control Change Channel: " + channelIndex + " Volume: " + value);
      }

      case 0x0a -> {
        this.channels.get(channelIndex).setPan(value);
        System.out.println("Control Change Channel: " + channelIndex + " Pan: " + value);
      }

      default -> System.err.printf("Bad control change 0x%x%n", command);
    }
  }

  private void programChange() {
    final int channelIndex = this.state.channel;
    final int program = this.state.sequence.readUByte(this.state.offset++);

    this.channels.get(channelIndex).setPresetIndex(program);

    System.out.println("Program Change Channel: " + channelIndex + " Program: " + program);
  }

  private void pitchBend() {
    final int channelIndex = this.state.channel;
    final int value = this.state.sequence.readUByte(this.state.offset++);

    final Channel channel = this.channels.get(channelIndex);
    channel.setPitchBend(value);

    for(final Voice voice : this.voicePool) {
      if(voice.getChannel() == channel) {
        voice.updateSampleRate();
      }
    }

    System.out.println("Pitch Bend Channel: " + channelIndex + " Value: " + value);
  }

  public void play() {
    for(final Voice voice : this.voicePool) {
      voice.play();
    }
  }

  public void stop() {
    for(final Voice voice : this.voicePool) {
      voice.stop();
    }
  }

  public void destroy() {
    for(final Voice voice : this.voicePool) {
      voice.stop();
      voice.destroy();
    }
  }
}
