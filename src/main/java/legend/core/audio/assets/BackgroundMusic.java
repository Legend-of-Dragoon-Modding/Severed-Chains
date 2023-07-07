package legend.core.audio.assets;

import legend.core.audio.MidiCommand;
import legend.game.unpacker.FileData;

public final class BackgroundMusic {
  private final Sssq sssq;
  private final byte[][] breathControls;
  private final byte[] velocityRamp;

  private final FileData sequence;
  private int sequencePosition;
  private int samplesToProcess;

  private Command currentCommand;
  private Command previousCommand;

  private int lsbType;
  private int nrpn;
  private int dataInstrumentIndex;
  private int repeatCount;
  private int repeatCounter;
  private int repeatPosition;
  private Command repeatCommand;
  private boolean repeat;

  BackgroundMusic(final Sssq sssq, final byte[][] breathControls, final byte[] velocityRamp) {
    this.sssq = sssq;
    this.breathControls = breathControls;
    this.velocityRamp = velocityRamp;

    this.sequence = new FileData(this.sssq.getSequence(0));
  }

  public Command getNextCommand() {
    this.previousCommand = this.currentCommand;

    final int event = this.sequence.readUByte(this.sequencePosition);

    this.currentCommand = new Command();
    if((event & 0x80) != 0) {
      this.currentCommand.command = MidiCommand.formEvent(event);
      this.currentCommand.channel = event & 0x0f;
      this.sequencePosition++;
    } else {
      this.currentCommand.command = this.previousCommand.command;
      this.currentCommand.channel = this.previousCommand.channel;
    }

    switch(this.currentCommand.command) {
      case KEY_OFF, KEY_ON, CONTROL_CHANGE -> {
        this.currentCommand.value1 = this.sequence.readUByte(this.sequencePosition++);
        this.currentCommand.value2 = this.sequence.readUByte(this.sequencePosition++);
      }
      case PROGRAM_CHANGE, PITCH_BEND -> this.currentCommand.value1 = this.sequence.readUByte(this.sequencePosition++);
      case META -> {
        this.currentCommand.value1 = this.sequence.readUByte(this.sequencePosition++);
        if(this.currentCommand.value1 == 0x51) {
          this.currentCommand.value2 = this.sequence.readUShort(this.sequencePosition);
          this.sequencePosition += 2;
        }
      }
      default -> throw new RuntimeException("Bad message: %s".formatted(this.currentCommand.command));
    }

    if(this.repeat) {
      this.repeat = false;
      this.sequencePosition = this.repeatPosition;
      this.currentCommand = this.repeatCommand;
      this.currentCommand.deltaTime = 0;
      return this.currentCommand;
    }

    this.currentCommand.deltaTime = this.readDeltaTime();
    return this.currentCommand;
  }

  public int getSamplesToProcess() {
    return this.samplesToProcess;
  }

  public void setSamplesToProcess(final int deltaTime) {
    if(deltaTime == 0) {
      this.samplesToProcess = 0;
      return;
    }

    final double deltaMs = deltaTime / this.sssq.getTicksPerMs();
    this.samplesToProcess = (int)(deltaMs * 44.1d);
  }

  public void tickSequence() {
    this.samplesToProcess--;
  }

  private int readDeltaTime() {
    int deltaTime = 0;

    while(true) {
      final int part = this.sequence.readUByte(this.sequencePosition++);

      deltaTime <<= 7;
      deltaTime |= part & 0x7f;

      if((part & 0x80) == 0) {
        break;
      }
    }

    return deltaTime;
  }

  public Channel getChannel(final int channelIndex) {
    return this.sssq.getChannel(channelIndex);
  }

  public int getVelocity(final int velocityIndex) {
    return this.velocityRamp[velocityIndex];
  }

  public byte[][] getBreathControls() {
    return this.breathControls;
  }

  public void setTempo(final int tempo) {
    this.sssq.setTempo(tempo);
  }

  public int getVolume() {
    return this.sssq.getVolume();
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
      this.repeatCommand = this.previousCommand;
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

  public int getNrpn() {
    return this.nrpn;
  }

  public void setRepeatCount(final int repeatCount) {
    this.repeatCount = repeatCount;
  }

  public final class Command {
    private MidiCommand command;
    private int channel;
    private int value1;
    private int value2;
    private int deltaTime;

    public MidiCommand getMidiCommand() {
      return this.command;
    }

    public int getChannel() {
      return this.channel;
    }

    public int getValue1() {
      return this.value1;
    }

    public int getValue2() {
      return this.value2;
    }

    public int getDeltaTime() {
      return this.deltaTime;
    }
  }
}
