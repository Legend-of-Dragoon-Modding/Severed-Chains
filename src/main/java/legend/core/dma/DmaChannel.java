package legend.core.dma;

import legend.core.IoHelper;
import legend.core.memory.IllegalAddressException;
import legend.core.memory.Memory;
import legend.core.memory.MisalignedAccessException;
import legend.core.memory.Segment;
import legend.core.memory.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;

import static legend.core.Hardware.DMA;

public class DmaChannel {
  private static final Logger LOGGER = LogManager.getFormatterLogger(DmaChannel.class);

  public final DmaChannelType channel;

  /**
   * DMA base address
   */
  public final Value MADR;
  /**
   * DMA Block Control
   */
  public final Value BCR;
  /**
   * DMA Channel Control
   */
  public final Value CHCR;

  private boolean enabled;
  private int priority;

  private long madr;
  private long bcr;
  public final ChannelControl channelControl = new ChannelControl();

  private DmaInterface dmaInterface = new DmaInterface() {
    @Override public void blockCopy(final int size) { }
    @Override public void linkedList() { }
  };

  public DmaChannel(final Memory memory, final DmaChannelType channel) {
    this.channel = channel;
    this.MADR = memory.ref(4, 0x1f801080L + channel.ordinal() * 0x10L);
    this.BCR  = memory.ref(4, 0x1f801084L + channel.ordinal() * 0x10L);
    this.CHCR = memory.ref(4, 0x1f801088L + channel.ordinal() * 0x10L);
    memory.addSegment(new DmaChannelSegment(0x1f801080L + channel.ordinal() * 0x10L));
  }

  public void setDmaInterface(final DmaInterface dmaConsumer) {
    this.dmaInterface = dmaConsumer;
  }

  public void transferComplete() {
    DMA.transferFinished(this.channel);
  }

  public boolean isEnabled() {
    return this.enabled;
  }

  public void enable() {
    LOGGER.debug("[DMA %s] Enabled", this.channel);
    this.enabled = true;
  }

  public void disable() {
    LOGGER.debug("[DMA %s] Disabled", this.channel);
    this.enabled = false;
  }

  public int getPriority() {
    return this.priority;
  }

  public void setPriority(final int priority) {
    if(priority < 0 || priority > 7) {
      throw new RuntimeException("Invalid priority " + priority + " - must be between 0 and 7 (inclusive)");
    }

    LOGGER.debug("[DMA %s] Priority set to %d", this.channel, priority);
    this.priority = priority;
  }

  public long getBlockSize() {
    final long size = this.BCR.get(0xffffL);
    return size == 0 ? 0x1_0000L : size;
  }

  public long getBlockCount() {
    final long count = this.BCR.get(0xffff0000L) >>> 0x10L;
    return count == 0 ? 0x1_0000L : count;
  }

  public void decrementBlockCount() {
    this.BCR.setu(this.getBlockCount() - 1 << 0x10L | this.getBlockSize());
  }

  private void onMadrWrite(final long value) {
    LOGGER.debug("DMA channel %s memory address set from %08x to %08x", this.channel, this.madr, value);
    this.madr = value;
  }

  private void onBcrWrite(final long value) {
    this.bcr = value;

    // CDROM or OTC
    if(this.channel == DmaChannelType.CDROM || this.channel == DmaChannelType.OTC) {
      LOGGER.debug("DMA channel %s number of words=%04x", this.channel, this.getBlockSize());
    } else {
      LOGGER.debug("DMA channel %s blocksize=%04x number of blocks=%04x", this.channel, this.getBlockSize(), this.getBlockCount());
    }
  }

  private void onChcrWrite(final long value) {
    this.channelControl.transferDirection = ChannelControl.TRANSFER_DIRECTION.values()[(int)(value & 0b1)];
    this.channelControl.addressStep = (value & 0b10) == 0 ? ChannelControl.ADDRESS_STEP.FORWARD : ChannelControl.ADDRESS_STEP.BACKWARD;

    if((value & 0b1_0000_0000) != 0) {
      throw new RuntimeException("Chopping enable not yet supported");
    }

    this.channelControl.mode = ChannelControl.MODE.values()[(int)((value & 0b110_0000_0000) >> 9)];

    if((value & 0b111_0000_0000_0000_0000) != 0) {
      throw new RuntimeException("Chopping DMA window size not yet supported");
    }

    if((value & 0b111_0000_0000_0000_0000_0000) != 0) {
      throw new RuntimeException("Chopping CPU window size not yet supported");
    }

    this.channelControl.busy = (value & 0b1_0000_0000_0000_0000_0000_0000) != 0;
    LOGGER.debug("DMA channel %s CHCR start/busy set to %b", this.channel, this.channelControl.busy);

    this.channelControl.startTrigger = ChannelControl.START_TRIGGER.values()[(int)((value & 0b1_0000_0000_0000_0000_0000_0000_0000) >> 28)];
    LOGGER.debug("DMA channel %s CHCR start/trigger set to %s", this.channel, this.channelControl.startTrigger);

    if(this.channelControl.isActive()) {
      if(this.channelControl.getMode() == ChannelControl.MODE.IMMEDIATE) {
        LOGGER.debug("DMA channel %s beginning block copy @ %08x for %04x bytes", this.channel, this.MADR.get(), this.getBlockSize());
        this.dmaInterface.blockCopy((int)this.getBlockSize());
      } else if(this.channelControl.getMode() == ChannelControl.MODE.SYNC_TO_DMA_REQUESTS) {
        LOGGER.debug("DMA channel %s beginning block transfer @ %08x for %04x * %04x bytes", this.channel, this.MADR.get(), this.getBlockSize(), this.getBlockCount());
        this.dmaInterface.blockCopy((int)this.getBlockSize() * (int)this.getBlockCount());
      } else if(this.channelControl.getMode() == ChannelControl.MODE.LINKED_LIST) {
        LOGGER.debug("DMA channel %s linked list transfer @ %08x for %04x bytes", this.channel, this.MADR.get());
        this.dmaInterface.linkedList();
      }

      //disable channel
      this.channelControl.resetBusy();
      this.channelControl.resetStartTrigger();
      this.transferComplete();
    }
  }

  private long onMadrRead() {
    return this.madr;
  }

  private long onBcrRead() {
    return this.bcr;
  }

  private long onChcrRead() {
    return this.channelControl.pack();
  }

  public void dump(final ByteBuffer stream) {
    IoHelper.write(stream, this.enabled);
    IoHelper.write(stream, this.priority);

    IoHelper.write(stream, this.madr);
    IoHelper.write(stream, this.bcr);
    IoHelper.write(stream, this.channelControl.transferDirection);
    IoHelper.write(stream, this.channelControl.addressStep);
    IoHelper.write(stream, this.channelControl.chopping);
    IoHelper.write(stream, this.channelControl.mode);
    IoHelper.write(stream, this.channelControl.choppingDmaWindowSize);
    IoHelper.write(stream, this.channelControl.choppingCpuWindowSize);
    IoHelper.write(stream, this.channelControl.busy);
    IoHelper.write(stream, this.channelControl.startTrigger);
  }

  public void load(final ByteBuffer stream) {
    this.enabled = IoHelper.readBool(stream);
    this.priority = IoHelper.readInt(stream);

    this.madr = IoHelper.readLong(stream);
    this.bcr = IoHelper.readLong(stream);
    this.channelControl.transferDirection = IoHelper.readEnum(stream, ChannelControl.TRANSFER_DIRECTION.class);
    this.channelControl.addressStep = IoHelper.readEnum(stream, ChannelControl.ADDRESS_STEP.class);
    this.channelControl.chopping = IoHelper.readBool(stream);
    this.channelControl.mode = IoHelper.readEnum(stream, ChannelControl.MODE.class);
    this.channelControl.choppingDmaWindowSize = IoHelper.readInt(stream);
    this.channelControl.choppingCpuWindowSize = IoHelper.readInt(stream);
    this.channelControl.busy = IoHelper.readBool(stream);
    this.channelControl.startTrigger = IoHelper.readEnum(stream, ChannelControl.START_TRIGGER.class);
  }

  public static class ChannelControl {
    private TRANSFER_DIRECTION transferDirection = TRANSFER_DIRECTION.TO_MAIN_RAM;
    private ADDRESS_STEP addressStep = ADDRESS_STEP.FORWARD;
    private boolean chopping;
    private MODE mode = MODE.IMMEDIATE;
    private int choppingDmaWindowSize;
    private int choppingCpuWindowSize;
    private boolean busy;
    private START_TRIGGER startTrigger = START_TRIGGER.NORMAL;

    public boolean isActive() {
      return this.mode == MODE.IMMEDIATE ?
        this.busy && this.startTrigger == START_TRIGGER.MANUAL :
        this.busy;
    }

    public TRANSFER_DIRECTION getTransferDirection() {
      return this.transferDirection;
    }

    public ADDRESS_STEP getAddressStep() {
      return this.addressStep;
    }

    public boolean isChopping() {
      return this.chopping;
    }

    public MODE getMode() {
      return this.mode;
    }

    public int getChoppingDmaWindowSize() {
      return this.choppingDmaWindowSize;
    }

    public int getChoppingCpuWindowSize() {
      return this.choppingCpuWindowSize;
    }

    public boolean isBusy() {
      return this.busy;
    }

    public void resetBusy() {
      this.busy = false;
    }

    public START_TRIGGER getStartTrigger() {
      return this.startTrigger;
    }

    public void resetStartTrigger() {
      this.startTrigger = START_TRIGGER.NORMAL;
    }

    public int pack() {
      return
        this.transferDirection.ordinal() |
        this.addressStep.ordinal() << 1 |
        (this.chopping ? 1 : 0) << 8 |
        this.mode.ordinal() << 9 |
        this.choppingDmaWindowSize << 16 |
        this.choppingCpuWindowSize << 20 |
        (this.busy ? 1 : 0) << 24 |
        this.startTrigger.ordinal() << 28;
    }

    public enum TRANSFER_DIRECTION {
      TO_MAIN_RAM,
      FROM_MAIN_RAM,
    }

    public enum ADDRESS_STEP {
      FORWARD(4),
      BACKWARD(-4),
      ;

      public final int step;

      ADDRESS_STEP(final int step) {
        this.step = step;
      }
    }

    public enum MODE {
      IMMEDIATE,
      SYNC_TO_DMA_REQUESTS,
      LINKED_LIST,
    }

    public enum START_TRIGGER {
      NORMAL,
      MANUAL,
    }
  }

  public class DmaChannelSegment extends Segment {
    public DmaChannelSegment(final long address) {
      super(address, 0xc);
    }

    @Override
    public byte get(final int offset) {
      throw new MisalignedAccessException("DMA channel ports may only be accessed with 32-bit reads and writes");
    }

    @Override
    public long get(final int offset, final int size) {
      if(size != 4) {
        throw new MisalignedAccessException("DMA channel ports may only be accessed with 32-bit reads and writes");
      }

      return switch(offset & 0b1100) {
        case 0x0 -> DmaChannel.this.onMadrRead();
        case 0x4 -> DmaChannel.this.onBcrRead();
        case 0x8 -> DmaChannel.this.onChcrRead();
        default -> throw new IllegalAddressException("There is no DMA channel port at " + Long.toHexString(this.getAddress() + offset));
      };
    }

    @Override
    public void set(final int offset, final byte value) {
      throw new MisalignedAccessException("DMA channel ports may only be accessed with 32-bit reads and writes");
    }

    @Override
    public void set(final int offset, final int size, final long value) {
      if(size != 4) {
        throw new MisalignedAccessException("DMA channel ports may only be accessed with 32-bit reads and writes");
      }

      switch(offset & 0b1100) {
        case 0x0 -> DmaChannel.this.onMadrWrite(value);
        case 0x4 -> DmaChannel.this.onBcrWrite(value);
        case 0x8 -> DmaChannel.this.onChcrWrite(value);
        default -> throw new IllegalAddressException("There is no DMA channel port at " + Long.toHexString(this.getAddress() + offset));
      }
    }
  }
}
