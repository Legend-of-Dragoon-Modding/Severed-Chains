package legend.core.audio.sequencer.assets;

import legend.core.audio.SampleRate;
import legend.core.audio.sequencer.assets.sequence.Command;
import legend.core.audio.sequencer.assets.sequence.bgm.SequenceBuilder;
import legend.game.unpacker.FileData;
import legend.game.unpacker.Loader;

import java.util.List;

public final class BackgroundMusic {
  private final int songId;

  private float volume;
  private final int tickPerQuarterNote;
  private int tempo;
  private double tempoTicks;
  private double samplesPerTick;

  private final short[][] breathControls;
  private final byte[] velocityRamp = new byte[0x80];

  private final Channel[] channels;
  private final Command[] sequence;
  private int sequencePosition;
  private final SoundFont soundFont;

  private int nrpn;
  private int lsbType;
  private int dataInstrumentIndex;
  private int repeatCount;
  private int repeatCounter;
  private int repeatPosition;
  private boolean repeat;

  public BackgroundMusic(final List<FileData> files, final int fileId, final SampleRate sampleRate) {
    this.songId = files.get(0).readUShort(0);

    final int fileOffset = files.size() == 5 ? 1 : 0;

    final FileData sshd = files.get(2 + fileOffset);

    final SoundBank soundBank;
    if(fileOffset == 0) {
      soundBank = new SoundBank(files.get(3));
    } else {
      final byte[] soundBankData = new byte[sshd.readInt(0x4)];
      int offset = files.get(4).size();
      files.get(4).copyFrom(0, soundBankData, 0, offset);
      final int bankCount = files.get(1).readUShort(0x0);

      for(int i = 1; i < bankCount; i++) {
        final FileData extraBank = Loader.loadFile("SECT/DRGN0.BIN/" + (fileId + i));
        extraBank.copyFrom(0, soundBankData, offset, extraBank.size());
        offset += extraBank.size();
      }

      soundBank = new SoundBank(new FileData(soundBankData));
    }

    final int[] subfileOffsets = new int[3];

    for(int i = 0; i < subfileOffsets.length; i++) {
      subfileOffsets[i] = sshd.readInt(16 + i * 4);
    }

    final FileData sssq = files.get(1 + fileOffset);

    this.volume = sssq.readUByte(0x0) / 128.0f;
    this.tickPerQuarterNote = sssq.readUShort(0x2);
    this.tempoTicks = sampleRate.value * 60;
    this.setTempo(sssq.readUShort(0x4));

    this.soundFont = new SoundFont(sshd.slice(subfileOffsets[0], subfileOffsets[1] - subfileOffsets[0]), soundBank, sampleRate);

    this.channels = new Channel[0x10];
    for(int channel = 0; channel < this.channels.length; channel++) {
      this.channels[channel] = new Channel(sssq.slice(16 + channel * 16, 16), this.volume, this.soundFont);
    }

    this.sequence = SequenceBuilder.create(sssq.slice(0x110), this.channels);

    sshd.copyFrom(subfileOffsets[1] + 2, this.velocityRamp, 0, 0x80);

    if(subfileOffsets[2] == -1) {
      this.breathControls = new short[0][];
    } else {
      this.breathControls = new short[sshd.readUShort(subfileOffsets[2]) + 1][];
    }

    for(int i = 0; i < this.breathControls.length; i++) {
      final int relativeOffset = sshd.readShort(2 + i * 2 + subfileOffsets[2]);

      if(relativeOffset != -1) {
        this.breathControls[i] = new short[63];
        final int startingPosition = subfileOffsets[2] + relativeOffset;
        for(int b = 0; b < 60; b++) {
          this.breathControls[i][b + 1] = (short)(sshd.readUByte(startingPosition + b) - 0x80);
        }

        this.breathControls[i][0] = this.breathControls[i][59];
        this.breathControls[i][61] = this.breathControls[i][2];
        this.breathControls[i][62] = this.breathControls[i][3];
      }
    }
  }

  private BackgroundMusic(final List<FileData> files, final short[][] breathControls, final byte[] velocityRamp, final SoundFont soundFont, final double tempoTicks) {
    this.songId = files.get(0).readUShort(0);

    this.breathControls = breathControls;
    System.arraycopy(velocityRamp, 0, this.velocityRamp, 0, velocityRamp.length);
    this.soundFont = soundFont;

    final FileData sssq = files.get(2);

    this.volume = sssq.readUByte(0x0) / 128.0f;
    this.tickPerQuarterNote = sssq.readUShort(0x2);
    this.tempoTicks = tempoTicks;
    this.setTempo(sssq.readUShort(0x4));

    this.channels = new Channel[0x10];
    for(int channel = 0; channel < this.channels.length; channel++) {
      this.channels[channel] = new Channel(sssq.slice(16 + channel * 16, 16), this.volume, this.soundFont);
    }

    this.sequence = SequenceBuilder.create(sssq.slice(0x110), this.channels);
  }

  public void reset() {
    this.sequencePosition = 0;
  }

  public BackgroundMusic createVictoryMusic(final List<FileData> files) {
    return new BackgroundMusic(files, this.breathControls, this.velocityRamp, this.soundFont, this.tempoTicks);
  }

  public Command getNextCommand() {
    return this.sequence[this.sequencePosition++];
  }

  public float getVolume() {
    return this.volume;
  }

  public void setTempo(final int tempo) {
    this.tempo = tempo;
    this.samplesPerTick = calculateSamplesPerTick(this.tempoTicks, tempo, this.tickPerQuarterNote);
  }

  public double getSamplesPerTick() {
    return this.samplesPerTick;
  }

  public short[][] getBreathControls() {
    return this.breathControls;
  }

  public int getVelocityVolume(final int velocity) {
    return this.velocityRamp[velocity];
  }

  public int getSongId() {
    return this.songId;
  }

  public int getNrpn() {
    return this.nrpn;
  }

  public void setRepeatCount(final int repeatCount) {
    this.repeatCount = repeatCount;
  }

  public void dataEntryLsb(final int value) {
    switch(this.lsbType) {
      case 0x00 -> {
        this.nrpn = 0x00;
        this.repeatCount = value;
      }
      case 0x01, 0x02 -> this.nrpn = value;
    }
  }

  public void dataEntryMsb(final int nrpn) {
    if(nrpn >= 0x00 && nrpn < 0x10) {
      this.lsbType = 0x10;
      this.dataInstrumentIndex = nrpn;
    } else if(nrpn == 0x10) {
      this.lsbType = 0x01;
    } else if(nrpn == 0x14) {
      this.lsbType = 0x00;
      this.nrpn = 0x00;
      this.repeatPosition = this.sequencePosition;
    } else if(nrpn == 0x1e) {
      this.lsbType = 0x00;

      if(this.repeatCount == 0x7f) {
        this.repeat = true;
      } else if(this.repeatCounter < this.repeatCount) {
        this.repeatCounter++;
        this.repeat = true;
      } else {
        this.repeatPosition = 0;
        this.repeatCounter = 0;
        this.repeat = false;
      }
    } else if(nrpn == 0x7f) {
      this.lsbType = 0;
      this.dataInstrumentIndex = 0xff;
    }
  }

  public boolean rewind() {
    this.sequencePosition = 0;

    return this.repeat;
  }

  public boolean handleRepeat() {
    if(!this.repeat) {
      return false;
    }

    this.sequencePosition = this.repeatPosition;
    this.repeat = false;

    return true;
  }

  public void setVolume(final float volume) {
    this.volume = volume;

    for(final Channel channel : this.channels) {
      channel.changeVolume(channel.getVolume(), this.volume);
    }
  }

  public void changeSampleRate(final SampleRate sampleRate) {
    this.soundFont.changeSampleRate(sampleRate);

    this.tempoTicks = sampleRate.value * 60;
    this.samplesPerTick = calculateSamplesPerTick(this.tempoTicks, this.tempo, this.tickPerQuarterNote);
  }

  private static double calculateSamplesPerTick(final double tempoTicks, final int tempo, final int ticksPerQuarterNote) {
    return tempoTicks / (tempo * ticksPerQuarterNote);
  }
}
