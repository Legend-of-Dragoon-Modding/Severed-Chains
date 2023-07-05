package legend.core.memory;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import legend.core.MathHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class Memory {
  private static final Logger LOGGER = LogManager.getFormatterLogger();

  private static final int[] REGION_MASK = {
    0xffff_ffff, 0xffff_ffff, 0xffff_ffff, 0xffff_ffff, // KUSEG: 2048MB
    0x7fff_ffff,                                        // KSEG0:  512MB
    0x1fff_ffff,                                        // KSEG1:  512MB
    0xffff_ffff, 0xffff_ffff,                           // KSEG2: 1024MB
  };

  private final Object lock = new Object();

  private final List<Segment> segments = new ArrayList<>();

  public static final IntSet watches = new IntOpenHashSet();

  public static void addWatch(final long address) {
    watches.add((int)(address & 0xffffff));
  }

  public static void removeWatch(final long address) {
    watches.remove((int)(address & 0xffffff));
  }

  public void waitForLock(final Runnable callback) {
    synchronized(this.lock) {
      callback.run();
    }
  }

  public <T> T waitForLock(final Supplier<T> callback) {
    synchronized(this.lock) {
      return callback.get();
    }
  }

  private void checkAlignment(final long address, final int size) {
    // Don't check alignment for large or odd types
    if(size > 4 || (size & 1) != 0) {
      return;
    }

    if((address & size - 1) != 0) {
      throw new MisalignedAccessException("Misaligned memory access at address " + Long.toHexString(address) + " for size " + size);
    }
  }

  private long maskAddress(final long address) {
    final int region = (int)(address >>> 29 & 0b111);
    return address & REGION_MASK[region];
  }

  public void addSegment(final Segment segment) {
    synchronized(this.lock) {
      this.segments.add(segment);
    }
  }

  private Segment getSegment(final long address) {
    final long masked = this.maskAddress(address);

    for(final Segment segment : this.segments) {
      if(segment.accepts(masked)) {
        return segment;
      }
    }

    throw new IllegalAddressException("There is no memory segment at " + Long.toHexString(masked) + " (address: " + Long.toHexString(address) + ')');
  }

  public byte get(final long address) {
    synchronized(this.lock) {
      final Segment segment = this.getSegment(address);
      final byte val = segment.get((int)(this.maskAddress(address) - segment.getAddress()));

      if(watches.contains((int)address & 0xffffff)) {
        LOGGER.error("%08x read %x", address, val);
        LOGGER.error(new Throwable());
      }

      return val;
    }
  }

  public long get(final long address, final int size) {
    this.checkAlignment(address, size);

    synchronized(this.lock) {
      final Segment segment = this.getSegment(address);
      final long val = segment.get((int)(this.maskAddress(address) - segment.getAddress()), size);

      if(watches.contains((int)address & 0xffffff)) {
        LOGGER.error("%08x read %x", address, val);
        LOGGER.error(new Throwable());
      }

      return val;
    }
  }

  public void set(final long address, final byte data) {
    synchronized(this.lock) {
      final Segment segment = this.getSegment(address);
      segment.set((int)(this.maskAddress(address) - segment.getAddress()), data);
    }

    if(watches.contains((int)address & 0xffffff)) {
      LOGGER.error("%08x set to %x", address, data);
      LOGGER.error(new Throwable());
    }
  }

  public void set(final long address, final int size, final long data) {
    this.checkAlignment(address, size);

    synchronized(this.lock) {
      final Segment segment = this.getSegment(address);
      final int addr = (int)(this.maskAddress(address) - segment.getAddress());
      segment.set(addr, size, data);
    }

    if(watches.contains((int)address & 0xffffff)) {
      LOGGER.error("%08x set to %x", address, data);
      LOGGER.error(new Throwable());
    }
  }

  public byte[] getBytes(final long address, final int size) {
    synchronized(this.lock) {
      final Segment segment = this.getSegment(address);
      return segment.getBytes((int)(this.maskAddress(address) - segment.getAddress()), size);
    }
  }

  public void getBytes(final long address, final byte[] dest, final int offset, final int size) {
    if(watches.contains((int)address & 0xffffff)) {
      LOGGER.error("%08x read", address);
      LOGGER.error(new Throwable());
    }

    synchronized(this.lock) {
      final Segment segment = this.getSegment(address);
      segment.getBytes((int)(this.maskAddress(address) - segment.getAddress()), dest, offset, size);
    }
  }

  public void setBytes(final long address, final byte[] data) {
    this.setBytes(address, data, 0, data.length);
  }

  public void setBytes(final long address, final byte[] data, final int offset, final int size) {
    synchronized(this.lock) {
      final Segment segment = this.getSegment(address);
      segment.setBytes((int)(this.maskAddress(address) - segment.getAddress()), data, offset, size);
    }

    for(final int watch : watches) {
      if(watch >= (address & 0xffffff) && watch < (address & 0xffffff) + size) {
        LOGGER.error("%08x set to %x", address & 0xff00_0000L | watch, this.get(address & 0xff00_0000L | watch, 4));
        LOGGER.error(new Throwable());
      }
    }
  }

  public void memcpy(final long dest, final long src, final int length) {
    if(dest == src || length == 0) {
      return;
    }

    synchronized(this.lock) {
      final Segment srcSegment = this.getSegment(src);
      Segment destSegment = this.getSegment(dest);

      if(destSegment == srcSegment) {
        srcSegment.memcpy((int)(this.maskAddress(dest) - srcSegment.getAddress()), (int)(this.maskAddress(src) - srcSegment.getAddress()), length);
      } else {
        final byte[] data = srcSegment.getBytes((int)(this.maskAddress(src) - srcSegment.getAddress()), length);

        int offset = 0;
        while(offset < data.length) {
          final int copyLen = Math.min(data.length, destSegment.getLength());
          destSegment.setBytes((int)(this.maskAddress(dest + offset) - destSegment.getAddress()), data, offset, copyLen);
          offset += copyLen;

          if(offset < data.length) {
            destSegment = this.getSegment(dest + offset);
          }
        }
      }

      for(final int watch : watches) {
        if(watch >= (dest & 0xffffff) && watch < (dest & 0xffffff) + length) {
          LOGGER.error("%08x set to %x", dest & 0xff00_0000L | watch, this.get(dest & 0xff00_0000L | watch, 4));
          LOGGER.error(new Throwable());
        }
      }
    }
  }

  public void memfill(final long addr, final int length, final int value) {
    if(length == 0) {
      return;
    }

    synchronized(this.lock) {
      final Segment srcSegment = this.getSegment(addr);
      srcSegment.memfill((int)(this.maskAddress(addr) - srcSegment.getAddress()), length, value);

      for(final int watch : watches) {
        if(watch >= (addr & 0xffffff) && watch < (addr & 0xffffff) + length) {
          LOGGER.error("%08x set to %x", addr & 0xff00_0000L | watch, value);
          LOGGER.error(new Throwable());
        }
      }
    }
  }

  public Value ref(final int byteSize, final long address) {
    this.checkAlignment(address, byteSize);
    return new MemoryValue(byteSize, address);
  }

  public <T> T ref(final int byteSize, final long address, final Function<Value, T> constructor) {
    if(address == 0) {
      return null;
    }

    return constructor.apply(this.ref(byteSize, address));
  }

  public class MemoryValue extends Value {
    private final long address;
    private Segment segment;
    private int segmentOffset;

    public MemoryValue(final int byteSize, final long address) {
      super(byteSize);
      this.address = address;
    }

    private Segment getSegment() {
      if(this.segment == null) {
        this.segment = Memory.this.getSegment(this.address);
        this.segmentOffset = (int)(Memory.this.maskAddress(this.address) - this.segment.getAddress());
      }

      return this.segment;
    }

    @Override
    public Value offset(final long offset) {
      if(offset == 0) {
        return this;
      }

      return Memory.this.ref(this.getSize(), this.address + offset);
    }

    @Override
    public Value offset(final int size, final long offset) {
      if(offset == 0 && size == this.getSize()) {
        return this;
      }

      return Memory.this.ref(size, this.address + offset);
    }

    @Override
    public Value deref(final int size) {
      if(this.getSize() != 4) {
        throw new UnsupportedOperationException("Can only dereference 4-byte values %s".formatted(this));
      }

      try {
        return Memory.this.ref(size, this.get());
      } catch(final MisalignedAccessException e) {
        LOGGER.error("Misaligned deref %s", this);
        LOGGER.error("", new Throwable());
        throw e;
      }
    }

    @Override
    public long getAddress() {
      return this.address;
    }

    @Override
    public String toString() {
      return super.toString() + " @ " + Long.toString(this.address, 16);
    }

    @Override
    public long get() {
      synchronized(Memory.this.lock) {
        final long val = this.getSegment().get(this.segmentOffset, this.getSize());

        if(watches.contains((int)this.address & 0xffffff)) {
          LOGGER.error("%08x read %x", this.address, val);
          LOGGER.error(new Throwable());
        }

        return val;
      }
    }

    @Override
    public long getSigned() {
      return MathHelper.sign(this.get(), this.getSize());
    }

    @Override
    public Value set(final long value) {
      synchronized(Memory.this.lock) {
        this.getSegment().set(this.segmentOffset, this.getSize(), value);
      }

      if(watches.contains((int)this.address & 0xffffff)) {
        LOGGER.error("%08x set to %x", this.address, value);
        LOGGER.error(new Throwable());
      }

      return this;
    }

    @Override
    public Value setu(long value) {
      value &= (0b1L << this.getSize() * 8) - 1;
      this.validateUnsigned(value);
      this.set(value);
      return this;
    }
  }
}
