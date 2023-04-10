package legend.game.soundFinal;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import legend.game.sound.Sssq;
import legend.game.unpacker.FileData;

public class Bgm {
  private final Int2ObjectMap<Channel> midiChannels = new Int2ObjectArrayMap<>();
  private final MidiState state;

  private int samplesToProcess;

  public Bgm(final Sssq sssq, final FileData sshdData, final FileData soundBankData) {
    final SoundBank soundBank = new SoundBank(soundBankData);
    final Sshd sshd = new Sshd(sshdData, soundBank);

    for(int i = 0; i < 0x10; i++) {
      if(sssq.channelInfo_10[i].instrumentIndex_02 != -1) { // preset (SShd entry)
        this.midiChannels.put(i, new Channel(i, sssq.channelInfo_10[i], sshd));
      }
    }

    this.state = new MidiState(sssq.data());
    this.state.ticksPerQuarterNote = sssq.ticksPerQuarterNote_02;
    this.state.tempo = sssq.tempo_04;
    this.state.msPerTick = this.state.tempo * this.state.ticksPerQuarterNote / 60_000.0f;
  }

  public void tick(final int sampleCount) {
    int processed = 0;

    while (processed < sampleCount) {
      while(this.samplesToProcess > 0 && processed < sampleCount) {
        for(final Channel channel : this.midiChannels.values()) {
          channel.processSample();
        }
        this.samplesToProcess--;
        processed++;
      }

      if(processed == sampleCount) {
        return;
      }

      while(this.state.deltaMs == 0.0f) {
        this.readEvent();

        if(this.state.command != MidiCommand.META) {
          final Channel channel = this.midiChannels.get(this.state.channel);

          switch(this.state.command) {
            case KEY_OFF -> channel.keyOff(this.state);
            case KEY_ON -> channel.keyOn(this.state, this.readUByte());
            case POLYPHONIC_KEY_PRESSURE -> channel.polyphonicKeyPressure(this.state);
            case CONTROL_CHANGE -> channel.controlChange(this.state);
            case PROGRAM_CHANGE -> channel.programChange(this.state);
            case PITCH_BEND -> channel.pitchBend(this.state, this.readUByte());
          }
        } else {
          this.handleMeta();
          if(this.state.endOfTrack) {
            for(final Channel channel : this.midiChannels.values()) {
              //channel.processEndOfTrack();
            }
            return;
          }
        }

        this.state.deltaMs = this.readVarInt() * this.state.msPerTick;
        this.samplesToProcess = (int) (this.state.deltaMs * 44.1f);
      }

      System.out.printf("Delta ms %.02f%n", this.state.deltaMs);
      this.state.deltaMs = 0;
    }
  }

  private void readEvent() {
    this.state.event = this.state.sequence.readUByte(this.state.offset);

    if((this.state.event & 0x80) != 0) {
      this.state.offset++;
      this.state.previousEvent = this.state.event;

      this.state.command = MidiCommand.fromEvent(this.state.event);
      this.state.channel = this.state.event & 0x0f;
    } else {
      this.state.event = this.state.previousEvent;
    }
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
        this.state.msPerTick = this.state.tempo * this.state.ticksPerQuarterNote / 60_000.0f;
        this.state.offset += 2;

        System.out.println("Tempo " + this.state.tempo);
      }
    }
  }

  private int readUByte() {
    return this.state.sequence.readUByte(this.state.offset);
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

  public void play() {
    for(final Channel channel : this.midiChannels.values()) {
      channel.play();
    }
  }

  public boolean isPlaying() {
    for(final Channel channel :this.midiChannels.values()) {
      return channel.isPlaying();
    }

    return false;
  }
}
