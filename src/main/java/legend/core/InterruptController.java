package legend.core;

import legend.core.memory.IllegalAddressException;
import legend.core.memory.Memory;
import legend.core.memory.MisalignedAccessException;
import legend.core.memory.Segment;
import legend.core.memory.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;

import static legend.core.Hardware.MEMORY;

public class InterruptController {
  private static final Logger LOGGER = LogManager.getFormatterLogger(InterruptControlSegment.class);

  public static final Value I_STAT = MEMORY.ref(2, 0x1f80_1070L);
  public static final Value I_MASK = MEMORY.ref(2, 0x1f80_1074L);

  private long stat;
  private long mask;

  public InterruptController(final Memory memory) {
    memory.addSegment(new InterruptControlSegment(0x1f80_1070L));
  }

  public void set(final InterruptType interrupt) {
    this.stat |= interrupt.getBit();
  }

  public boolean interruptPending() {
    return (this.stat & this.mask) != 0;
  }

  public void dump(final ByteBuffer stream) {
    legend.core.IoHelper.write(stream, this.stat);
    legend.core.IoHelper.write(stream, this.mask);
  }

  public void load(final ByteBuffer stream, final int version) {
    this.stat = legend.core.IoHelper.readLong(stream);
    this.mask = IoHelper.readLong(stream);
  }

  public class InterruptControlSegment extends Segment {
    public InterruptControlSegment(final long address) {
      super(address, 0x8);
    }

    @Override
    public byte get(final int offset) {
      throw new MisalignedAccessException("Interrupt control ports may only be accessed with 16-bit reads and writes");
    }

    @Override
    public long get(final int offset, final int size) {
      if(size != 2) {
        throw new MisalignedAccessException("Interrupt control ports may only be accessed with 16-bit reads and writes");
      }

      return switch(offset & 0x6) {
        case 0x0 -> InterruptController.this.stat;
        case 0x4 -> InterruptController.this.mask;
        default -> throw new IllegalAddressException("There is no interrupt control port at " + Long.toHexString(offset));
      };
    }

    @Override
    public void set(final int offset, final byte value) {
      throw new MisalignedAccessException("Interrupt control ports may only be accessed with 16-bit reads and writes");
    }

    @Override
    public void set(final int offset, final int size, final long value) {
      if(size != 2) {
        throw new MisalignedAccessException("Interrupt control ports may only be accessed with 16-bit reads and writes");
      }

      switch(offset & 0x6) {
        case 0x0 -> {
          LOGGER.debug("Setting I_STAT to %04x", value);
          InterruptController.this.stat &= value & 0xffffL;
        }

        case 0x4 -> {
          LOGGER.debug("Setting I_MASK to %04x", value);
          InterruptController.this.mask = value & 0xffffL;
        }

        default -> throw new IllegalAddressException("There is no interrupt control port at " + Long.toHexString(offset));
      }
    }
  }
}
