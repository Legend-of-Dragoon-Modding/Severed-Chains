package legend.core;

import legend.core.DebugHelper;
import legend.core.Hardware;
import legend.core.InterruptType;
import legend.core.IoHelper;
import legend.core.memory.IllegalAddressException;
import legend.core.memory.Memory;
import legend.core.memory.MisalignedAccessException;
import legend.core.memory.Segment;
import legend.core.memory.Value;

import java.nio.ByteBuffer;

import static legend.core.Hardware.INTERRUPTS;
import static legend.core.Hardware.MEMORY;

public class Timers implements Runnable {
  public static final Value TMR_DOTCLOCK_VAL = MEMORY.ref(4, 0x1f801100L);
  public static final Value TMR_DOTCLOCK_MODE = MEMORY.ref(4, 0x1f801104L);
  public static final Value TMR_DOTCLOCK_MAX = MEMORY.ref(4, 0x1f801108L);
  public static final Value TMR_HRETRACE_VAL = MEMORY.ref(4, 0x1f801110L);
  public static final Value TMR_HRETRACE_MODE = MEMORY.ref(4, 0x1f801114L);
  public static final Value TMR_HRETRACE_MAX = MEMORY.ref(4, 0x1f801118L);
  public static final Value TMR_SYSCLOCK_VAL = MEMORY.ref(4, 0x1f801120L);
  public static final Value TMR_SYSCLOCK_MODE = MEMORY.ref(4, 0x1f801124L);
  public static final Value TMR_SYSCLOCK_MAX = MEMORY.ref(4, 0x1f801128L);

  private final Timer[] timers = {
    new Timer(0x1f801100L, 0),
    new Timer(0x1f801110L, 1),
    new Timer(0x1f801120L, 2),
  };

  private boolean running;

  public Timers(final Memory memory) {
    for(final Timer timer : this.timers) {
      memory.addSegment(timer);
    }
  }

  @Override
  public void run() {
    this.running = true;

    while(this.running) {
      if(this.tick(0, 100)) {
        INTERRUPTS.set(InterruptType.TMR0);
      }

      if(this.tick(1, 100)) {
        INTERRUPTS.set(InterruptType.TMR1);
      }

      if(this.tick(2, 100)) {
        INTERRUPTS.set(InterruptType.TMR2);
      }

      while(Hardware.dumping) {
        Hardware.timerWaiting = true;
        DebugHelper.sleep(1);
      }

      Hardware.timerWaiting = false;

      DebugHelper.sleep(1);
    }
  }

  public void stop() {
    this.running = false;
  }

  public boolean tick(final int index, final int cycles) {
    return this.timers[index].tick(cycles);
  }

  public static class Sync {
    public final int dotDiv;
    public final boolean hblank;
    public final boolean vblank;

    public Sync(final int dotDiv, final boolean hblank, final boolean vblank) {
      this.dotDiv = dotDiv;
      this.hblank = hblank;
      this.vblank = vblank;
    }
  }

  public void syncGPU(final Sync sync) {
    this.timers[0].syncGPU(sync);
    this.timers[1].syncGPU(sync);
  }

  public static class Timer extends Segment {
    private final int timerNumber;

    private long val;
    private long max;

    private byte syncEnable;
    private byte syncMode;
    private byte resetCounterOnTarget;
    private byte irqWhenCounterTarget;
    private byte irqWhenCounterFFFF;
    private byte irqRepeat;
    private byte irqPulse;
    private byte clockSource;
    private byte interruptRequest;
    private byte reachedTarget;
    private byte reachedFFFF;

    private boolean vblank;
    private boolean hblank;
    private int dotDiv = 1;

    private boolean prevHblank;
    private boolean prevVblank;

    private boolean irq;
    private boolean alreadyFiredIrq;

    public Timer(final long address, final int index) {
      super(address, 0xc);
      this.timerNumber = index;
    }

    @Override
    public byte get(final int offset) {
      throw new MisalignedAccessException("Timer ports may only be accessed with 32-bit reads and writes");
    }

    @Override
    public long get(final int offset, final int size) {
      if(size != 4) {
        throw new MisalignedAccessException("Timer ports may only be accessed with 32-bit reads and writes");
      }

      return switch(offset & 0xc) {
        case 0x0 -> this.val;
        case 0x4 -> this.getCounterMode();
        case 0x8 -> this.max;
        default -> throw new IllegalAddressException("There is no timer port at " + Long.toHexString(this.getAddress() + offset));
      };
    }

    @Override
    public void set(final int offset, final byte value) {
      throw new MisalignedAccessException("Timer ports may only be accessed with 32-bit reads and writes");
    }

    @Override
    public void set(final int offset, final int size, final long value) {
      if(size != 4) {
        throw new MisalignedAccessException("Timer ports may only be accessed with 32-bit reads and writes");
      }

      switch(offset & 0xc) {
        case 0x0 -> this.val = value;
        case 0x4 -> this.setCounterMode(value);
        case 0x8 -> this.max = value;
        default -> throw new IllegalAddressException("There is no timer port at " + Long.toHexString(this.getAddress() + offset));
      }
    }

    public void syncGPU(final Sync sync) {
      this.prevHblank = this.hblank;
      this.prevVblank = this.vblank;
      this.dotDiv = sync.dotDiv;
      this.hblank = sync.hblank;
      this.vblank = sync.vblank;
    }

    int cycles;

    public boolean tick(final int cyclesTicked) { //todo this needs rework
      this.cycles += cyclesTicked;
      switch(this.timerNumber) {
        case 0:
          if(this.syncEnable == 1) {
            switch(this.syncMode) {
              case 0:
                if(this.hblank) {
                  return false;
                }
                break;
              case 1:
                if(this.hblank) {
                  this.val = 0;
                }
                break;
              case 2:
                if(this.hblank) {
                  this.val = 0;
                }
                if(!this.hblank) {
                  return false;
                }
                break;
              case 3:
                if(!this.prevHblank && this.hblank) {
                  this.syncEnable = 0;
                } else {
                  return false;
                }
                break;
            }
          } //else free run

          if(this.clockSource == 0 || this.clockSource == 2) {
            this.val += (short)this.cycles;
          } else {
            this.val += (short)(this.cycles * 11 / 7 / this.dotDiv); //DotClock
          }

          this.cycles = 0;
          return this.handleIrq();

        case 1:
          if(this.syncEnable == 1) {
            switch(this.syncMode) {
              case 0:
                if(this.vblank) {
                  return false;
                }
                break;
              case 1:
                if(this.vblank) {
                  this.val = 0;
                }
                break;
              case 2:
                if(this.vblank) {
                  this.val = 0;
                }
                if(!this.vblank) {
                  return false;
                }
                break;
              case 3:
                if(!this.prevVblank && this.vblank) {
                  this.syncEnable = 0;
                } else {
                  return false;
                }
                break;
            }
          }

          if(this.clockSource == 0 || this.clockSource == 2) {
            this.val += (short)this.cycles;
            this.cycles = 0;
          } else {
            this.val += (short)(this.cycles / 2160);
            this.cycles %= 2160;
          }

          return this.handleIrq();
        case 2:
          if(this.syncEnable == 1 && this.syncMode == 0 || this.syncMode == 3) {
            return false; // counter stopped
          }

          if(this.clockSource == 0 || this.clockSource == 1) {
            this.val += (short)this.cycles;
            this.cycles = 0;
          } else {
            this.val += (short)(this.cycles / 8);
            this.cycles %= 8;
          }

          return this.handleIrq();
        default:
          return false;
      }
    }

    private boolean handleIrq() {
      this.irq = false;

      if(this.val >= this.max) {
        this.reachedTarget = 1;
        if(this.resetCounterOnTarget == 1) {
          this.val = 0;
        }
        if(this.irqWhenCounterTarget == 1) {
          this.irq = true;
        }
      }

      if(this.val >= 0xffff) {
        this.reachedFFFF = 1;
        if(this.irqWhenCounterFFFF == 1) {
          this.irq = true;
        }
      }

      this.val &= 0xffff;

      if(!this.irq) {
        return false;
      }

      if(this.irqPulse == 0) { // short bit10
        this.interruptRequest = 0;
      } else { // toggle it
        this.interruptRequest = (byte)(this.interruptRequest + 1 & 0x1);
      }

      final boolean trigger = this.interruptRequest == 0;

      if(this.irqRepeat == 0) { // once
        if(!this.alreadyFiredIrq && trigger) {
          this.alreadyFiredIrq = true;
        } else { // already fired
          return false;
        }
      } // repeat

      this.interruptRequest = 1;

      return trigger;
    }

    private void setCounterMode(final long value) {
      this.syncEnable = (byte)(value & 0x1);
      this.syncMode = (byte)(value >>> 1 & 0x3);
      this.resetCounterOnTarget = (byte)(value >>> 3 & 0x1);
      this.irqWhenCounterTarget = (byte)(value >>> 4 & 0x1);
      this.irqWhenCounterFFFF = (byte)(value >>> 5 & 0x1);
      this.irqRepeat = (byte)(value >>> 6 & 0x1);
      this.irqPulse = (byte)(value >>> 7 & 0x1);
      this.clockSource = (byte)(value >>> 8 & 0x3);

      this.interruptRequest = 1;
      this.alreadyFiredIrq = false;

      this.val = 0;
    }

    private long getCounterMode() {
      long counterMode = 0;
      counterMode |= this.syncEnable;
      counterMode |= this.syncMode << 1;
      counterMode |= this.resetCounterOnTarget << 3;
      counterMode |= this.irqWhenCounterTarget << 4;
      counterMode |= this.irqWhenCounterFFFF << 5;
      counterMode |= this.irqRepeat << 6;
      counterMode |= this.irqPulse << 7;
      counterMode |= this.clockSource << 8;
      counterMode |= this.interruptRequest << 10;
      counterMode |= this.reachedTarget << 11;
      counterMode |= this.reachedFFFF << 12;

      this.reachedTarget = 0;
      this.reachedFFFF = 0;

      return counterMode;
    }

    /** Don't call directly - this is a segment */
    @Override
    @Deprecated
    public void dump(final ByteBuffer stream) {
      super.dump(stream);

      IoHelper.write(stream, this.val);
      IoHelper.write(stream, this.max);

      IoHelper.write(stream, this.syncEnable);
      IoHelper.write(stream, this.syncMode);
      IoHelper.write(stream, this.resetCounterOnTarget);
      IoHelper.write(stream, this.irqWhenCounterTarget);
      IoHelper.write(stream, this.irqWhenCounterFFFF);
      IoHelper.write(stream, this.irqRepeat);
      IoHelper.write(stream, this.irqPulse);
      IoHelper.write(stream, this.clockSource);
      IoHelper.write(stream, this.interruptRequest);
      IoHelper.write(stream, this.reachedTarget);
      IoHelper.write(stream, this.reachedFFFF);

      IoHelper.write(stream, this.vblank);
      IoHelper.write(stream, this.hblank);
      IoHelper.write(stream, this.dotDiv);

      IoHelper.write(stream, this.prevHblank);
      IoHelper.write(stream, this.prevVblank);

      IoHelper.write(stream, this.irq);
      IoHelper.write(stream, this.alreadyFiredIrq);
    }

    @Override
    public void load(final ByteBuffer stream) throws ClassNotFoundException {
      super.load(stream);

      this.val = IoHelper.readLong(stream);
      this.max = IoHelper.readLong(stream);

      this.syncEnable = IoHelper.readByte(stream);
      this.syncMode = IoHelper.readByte(stream);
      this.resetCounterOnTarget = IoHelper.readByte(stream);
      this.irqWhenCounterTarget = IoHelper.readByte(stream);
      this.irqWhenCounterFFFF = IoHelper.readByte(stream);
      this.irqRepeat = IoHelper.readByte(stream);
      this.irqPulse = IoHelper.readByte(stream);
      this.clockSource = IoHelper.readByte(stream);
      this.interruptRequest = IoHelper.readByte(stream);
      this.reachedTarget = IoHelper.readByte(stream);
      this.reachedFFFF = IoHelper.readByte(stream);

      this.vblank = IoHelper.readBool(stream);
      this.hblank = IoHelper.readBool(stream);
      this.dotDiv = IoHelper.readInt(stream);

      this.prevHblank = IoHelper.readBool(stream);
      this.prevVblank = IoHelper.readBool(stream);

      this.irq = IoHelper.readBool(stream);
      this.alreadyFiredIrq = IoHelper.readBool(stream);
    }
  }
}
