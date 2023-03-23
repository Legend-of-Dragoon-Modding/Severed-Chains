package legend.game.sound2;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import legend.game.sound.Sssq;
import legend.game.unpacker.FileData;

public final class Bgm {
  private final Int2ObjectMap<Channel> midiChannels = new Int2ObjectArrayMap<>();
  private final MidiState state;

  public Bgm(final Sssq sssq, final FileData sshdData, final FileData soundBankData) {
    final SoundBank soundBank = new SoundBank(soundBankData);
    final Sshd sshd = new Sshd(sshdData, soundBank);

    for(int i = 0; i < 0x10; i++) {
      if(sssq.channelInfo_10[i].instrumentIndex_02 != -1) { // preset (SSHD entry)
        this.midiChannels.put(i, new Channel(i, sssq.channelInfo_10[i], sshd));
      }
    }

    this.state = new MidiState(sssq.data());
    this.state.ticksPerQuarterNote = sssq.ticksPerQuarterNote_02;
    this.state.tempo = sssq.tempo_04;
    this.state.msPerTick = this.state.tempo * this.state.ticksPerQuarterNote / 60_000.0f;
  }

  //TODO use RealTimeSequencer
  public void tick() {
    while(this.state.deltaMs == 0.0f) {
      this.readEvent();

      if(this.state.command != MidiCommand.META) {
        final Channel channel = this.midiChannels.get(this.state.channel);

        switch(this.state.command) {
          case KEY_OFF -> channel.handleKeyOff(this.state);
          case KEY_ON -> channel.handleKeyOn(this.state);
          case POLYPHONIC_KEY_PRESSURE -> channel.handlePolyphonicKeyPressure(this.state);
          case CONTROL_CHANGE -> channel.handleControlChange(this.state);
          case PROGRAM_CHANGE -> channel.handleProgramChange(this.state);
          case PITCH_BEND -> channel.handlePitchBend(this.state);
        }
      } else {
        this.handleMeta();
      }

      this.state.deltaMs = this.readVarInt() * this.state.msPerTick;

      System.out.printf("Delta ms %.02f%n", this.state.deltaMs);
    }

    for(final Channel channel : this.midiChannels.values()) {
      channel.tick(this.state.deltaMs);
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

  void handleMeta() {
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

  public void gracefulStop() {

  }

  public void stop() {
    for(final Channel channel : this.midiChannels.values()) {
      channel.stop();
    }
  }

  public void pause() {

  }

  public void destroy() {
    for(final Channel channel : this.midiChannels.values()) {
      channel.destroy();
    }
  }
}
