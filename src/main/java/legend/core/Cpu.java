package legend.core;

import legend.core.gte.Gte;
import legend.core.memory.types.RunnableRef;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static legend.core.Hardware.GATE;
import static legend.core.Hardware.INTERRUPTS;
import static legend.core.Hardware.MEMORY;
import static legend.core.Hardware.codeThread;

public class Cpu {
  public final Register R3_BPC = new Register();
  public final Register R5_BDA = new Register();
  public final Register R6_JUMPDEST = new Register();
  public final Register R7_DCIC = new Register();
  public final Register R8_BadVaddr = new Register();
  public final Register R9_BDAM = new Register();
  public final Register R11_BPCM = new Register();
  public final StatusRegister R12_SR = new StatusRegister(); // Called Status by ghidra
  public final CauseRegister R13_CAUSE = new CauseRegister();
  public final Register R14_EPC = new Register();
  public final Register R15_PRID = new Register();

  private final Map<CpuRegisterType, Register> registers = new EnumMap<>(CpuRegisterType.class);

  private final Gte gte = new Gte();

  private final List<Runnable> delegatedExecutions = new ArrayList<>();

  private int lastSyscall;
  private boolean exceptionHandled = true;

  private final RunnableRef[] exceptionAddresses;

  public Cpu() {
    this.registers.put(CpuRegisterType.BPC, this.R3_BPC);
    this.registers.put(CpuRegisterType.BDA, this.R5_BDA);
    this.registers.put(CpuRegisterType.JUMPDEST, this.R6_JUMPDEST);
    this.registers.put(CpuRegisterType.DCIC, this.R7_DCIC);
    this.registers.put(CpuRegisterType.BadVaddr, this.R8_BadVaddr);
    this.registers.put(CpuRegisterType.BDAM, this.R9_BDAM);
    this.registers.put(CpuRegisterType.BPCM, this.R11_BPCM);
    this.registers.put(CpuRegisterType.SR, this.R12_SR);
    this.registers.put(CpuRegisterType.CAUSE, this.R13_CAUSE);
    this.registers.put(CpuRegisterType.EPC, this.R14_EPC);
    this.registers.put(CpuRegisterType.PRID, this.R15_PRID);

    this.exceptionAddresses = new RunnableRef[] {
      MEMORY.ref(4, 0x80000080L, RunnableRef::new),
      MEMORY.ref(4, 0xbfc00180L, RunnableRef::new),
    };
  }

  public void tick() {
    synchronized(this.delegatedExecutions) {
      for(final Runnable delegate : this.delegatedExecutions) {
        delegate.run();
      }

      this.delegatedExecutions.clear();
    }

    while(this.handleInterrupts()) {
      DebugHelper.sleep(0);
    }
  }

  public int getLastSyscall() {
    return this.lastSyscall;
  }

  public boolean wasExceptionHandled() {
    return this.exceptionHandled;
  }

  private boolean handleInterrupts() {
    if(INTERRUPTS.interruptPending()) {
      this.R13_CAUSE.value |= 0x400L;
    } else {
      this.R13_CAUSE.value &= ~0x400L;
    }

    final boolean IEc = this.R12_SR.getIEc();
    final int IM = (int)this.R12_SR.getIm();
    final int IP = (int)(this.R13_CAUSE.get() >>> 8 & 0xff);

    if(IEc && (IM & IP) > 0) {
      this.EXCEPTION(CpuException.INTERRUPT);
      return true;
    }

    return false;
  }

  private void EXCEPTION(final CpuException cause) {
    this.EXCEPTION(cause, 0);
  }

  private void EXCEPTION(final CpuException cause, final int coprocessor) {
    if(Thread.currentThread() == codeThread) {
      synchronized(this.delegatedExecutions) {
        this.delegatedExecutions.add(() -> {
          this.EXCEPTION(cause, coprocessor);
          codeThread.resume();
        });

        assert this.delegatedExecutions.size() == 1 : "Shouldn't be possible since the code thread gets suspended";
      }

      codeThread.suspend();
      return;
    }

    final long mode = this.R12_SR.get() & 0x3fL;
    this.R12_SR.set(this.R12_SR.get() & ~0x3fL | mode << 2 & 0x3fL);

    final long oldCause = this.R13_CAUSE.value & 0xff00L;
    this.R13_CAUSE.value = cause.value << 2 | oldCause | coprocessor << 28;

    this.exceptionHandled = false;

    GATE.acquire();
    this.exceptionAddresses[this.R12_SR.getBEV() ? 1 : 0].run();
    GATE.release();
  }

  public void SYSCALL(final int index) {
    this.lastSyscall = index;
    this.EXCEPTION(CpuException.SYSCALL, 0);
  }

  public void RFE() {
    final long mode = this.R12_SR.get() & 0x3fL;
    this.R12_SR.set(this.R12_SR.get() & ~0xfL);
    this.R12_SR.set(this.R12_SR.get() | mode >>> 2);

    this.exceptionHandled = true;
  }

  public long MFC0(final CpuRegisterType register) {
    if(!this.registers.containsKey(register)) {
      this.EXCEPTION(CpuException.ILLEGAL_INSTR, 0);
    }

    return this.registers.get(register).get();
  }

  public void MTC0(final CpuRegisterType register, final long value) {
    //MTC0 can trigger soft interrupts
    final boolean prevIEC = this.R12_SR.getIEc();

    this.registers.get(register).set(value);

    final long IM = this.R12_SR.getIm();
    final long IP = this.R13_CAUSE.get() >>> 8 & 0x3;

    if(!prevIEC && this.R12_SR.getIEc() && (IM & IP) > 0) {
      this.EXCEPTION(CpuException.INTERRUPT, 0);
    }
  }

  /**
   * Stores val in GTE control register
   *
   * Register is (cmd >>> 11) &amp; 0x1f
   */
  public void CTC2(final long val, final long register) {
    this.gte.writeControl((int)register, (int)val);
  }

  /**
   * Stores val in GTE data register
   *
   * Register is (cmd >>> 11) &amp; 0x1f
   */
  public void MTC2(final long val, final long register) {
    this.gte.writeData((int)register, (int)val);
  }

  /**
   * Returns value in GTE data register
   *
   * Return moved from a0 to return
   *
   * Register is (cmd >>> 11) &amp; 0x1f
   */
  public long MFC2(final long register) {
    return this.gte.loadData((int)register);
  }

  /**
   * Returns value in GTE control register
   *
   * Return moved from a0 to return
   *
   * Register is (cmd >>> 11) &amp; 0x1f
   */
  public long CFC2(final long register) {
    return this.gte.loadControl((int)register);
  }

  /** Use {@link Cpu#MTC2} */
  @Deprecated
  public void LWC2() {
    assert false;
  }

  /** Use {@link Cpu#MFC2} */
  @Deprecated
  public void SWC2() {
    assert false;
  }

  public long COP2(final long cmd) {
    //TODO I'm not sure why this part here is wrong... it thinks GTE commands are register reads/writes
//    final long rs = cmd >>> 21 & 0x1F;
//
//    if((rs & 0x10) == 0) {
//      assert false : "Use direct commands instead";
//
//      return switch((int)rs) {
//        case 0b0_0000 -> mfc2(cmd);
//        case 0b0_0010 ->
//          CFC2();
//          0;
//        case 0b0_0100 ->
//          mtc2();
//          0;
//        case 0b0_0110 ->
//          ctc2();
//          0;
//        default -> throw new RuntimeException("Invalid instruction");
//      };
//    }

    this.gte.execute((int)cmd);
    return 0;
  }

  public void dump(final ByteBuffer stream) {
    for(final Register reg : this.registers.values()) {
      reg.dump(stream);
    }

    this.gte.dump(stream);

    IoHelper.write(stream, this.lastSyscall);
    IoHelper.write(stream, this.exceptionHandled);
  }

  public void load(final ByteBuffer stream, final int version) {
    for(final Register reg : this.registers.values()) {
      reg.load(stream);
    }

    this.gte.load(stream);

    this.lastSyscall = IoHelper.readInt(stream);
    this.exceptionHandled = IoHelper.readBool(stream);
  }

  public static class Register {
    protected long value;

    public void set(final long value) {
      this.value = value;
    }

    public long get() {
      return this.value;
    }

    public void dump(final ByteBuffer stream) {
      IoHelper.write(stream, this.value);
    }

    public void load(final ByteBuffer stream) {
      this.value = IoHelper.readLong(stream);
    }
  }

  public static class StatusRegister extends Register {
    private boolean IEc;
    private boolean KUc;
    private boolean IEp;
    private boolean KUp;
    private boolean IEo;
    private boolean KUo;
    private long Im;
    private boolean Isc;
    private boolean Swc;
    private boolean PZ;
    private boolean CM;
    private boolean PE;
    private boolean TS;
    private boolean BEV;
    private boolean RE;
    private boolean CU0;
    private boolean CU2;

    @Override
    public long get() {
      return
        (this.IEc ? 0x1 : 0) |
        (this.KUc ? 0x2 : 0) |
        (this.IEp ? 0x4 : 0) |
        (this.KUp ? 0x8 : 0) |
        (this.IEo ? 0x10 : 0) |
        (this.KUo ? 0x20 : 0) |
        this.getIm() << 8 |
        (this.Isc ? 0x1_0000 : 0) |
        (this.Swc ? 0x2_0000 : 0) |
        (this.PZ ? 0x4_0000 : 0) |
        (this.CM ? 0x8_0000 : 0) |
        (this.PE ? 0x10_0000 : 0) |
        (this.TS ? 0x20_0000 : 0) |
        (this.BEV ? 0x40_0000 : 0) |
        (this.RE ? 0x200_0000 : 0) |
        (this.CU0 ? 0x1000_0000 : 0) |
        (this.CU2 ? 0x4000_0000 : 0);
    }

    @Override
    public void set(final long value) {
      if((value & 0b1101_1000_0000_0000_0000_1100_0000) != 0) {
        throw new RuntimeException("Attempted to set reserved bits of status register");
      }

      if((value & 0x1) == 0) {
        this.resetIEc();
      } else {
        this.setIEc();
      }

      if((value & 0x2) == 0) {
        this.resetKUc();
      } else {
        this.setKUc();
      }

      if((value & 0x4) == 0) {
        this.resetIEp();
      } else {
        this.setIEp();
      }

      if((value & 0x8) == 0) {
        this.resetKUp();
      } else {
        this.setKUp();
      }

      if((value & 0x10) == 0) {
        this.resetIEo();
      } else {
        this.setIEo();
      }

      if((value & 0x20) == 0) {
        this.resetKUo();
      } else {
        this.setKUo();
      }

      this.setIm(value >>> 8 & 0xff);

      if((value & 0x1_0000) == 0) {
        this.resetIsc();
      } else {
        this.setIsc();
      }

      if((value & 0x2_0000) == 0) {
        this.resetSwc();
      } else {
        this.setSwc();
      }

      if((value & 0x4_0000) == 0) {
        this.resetPZ();
      } else {
        this.setPZ();
      }

      if((value & 0x8_0000) == 0) {
        this.resetCM();
      } else {
        this.setCM();
      }

      if((value & 0x10_0000) == 0) {
        this.resetPE();
      } else {
        this.setPE();
      }

      if((value & 0x20_0000) == 0) {
        this.resetTS();
      } else {
        this.setTS();
      }

      if((value & 0x40_0000) == 0) {
        this.resetBEV();
      } else {
        this.setBEV();
      }

      if((value & 0x200_0000) == 0) {
        this.resetRE();
      } else {
        this.setRE();
      }

      if((value & 0x1000_0000) == 0) {
        this.resetCU0();
      } else {
        this.setCU0();
      }

      if((value & 0x4000_0000) == 0) {
        this.resetCU2();
      } else {
        this.setCU2();
      }
    }

    /**
     * Current interrupt enable
     */
    public boolean getIEc() {
      return this.IEc;
    }

    /**
     * Current interrupt enable
     */
    public void setIEc() {
      this.IEc = true;
    }

    /**
     * Current interrupt enable
     */
    public void resetIEc() {
      this.IEc = false;
    }

    /**
     * Current kernel/user mode (true: user mode)
     */
    public boolean getKUc() {
      return this.KUc;
    }

    /**
     * Current kernel/user mode (true: user mode)
     */
    public void setKUc() {
      assert false : "Unimplemented";
      this.KUc = true;
    }

    /**
     * Current kernel/user mode (true: user mode)
     */
    public void resetKUc() {
      this.KUc = false;
    }

    /**
     * Previous interrupt enable
     */
    public boolean getIEp() {
      return this.IEp;
    }

    /**
     * Previous interrupt enable
     */
    public void setIEp() {
      this.IEp = true;
    }

    /**
     * Previous interrupt enable
     */
    public void resetIEp() {
      this.IEp = false;
    }

    /**
     * Previous kernel/user mode (true: user mode)
     */
    public boolean getKUp() {
      return this.KUp;
    }

    /**
     * Previous kernel/user mode (true: user mode)
     */
    public void setKUp() {
      assert false : "Unimplemented";
      this.KUp = true;
    }

    /**
     * Previous kernel/user mode (true: user mode)
     */
    public void resetKUp() {
      this.KUp = false;
    }

    /**
     * Old interrupt enable
     */
    public boolean getIEo() {
      return this.IEo;
    }

    /**
     * Old interrupt enable
     */
    public void setIEo() {
      this.IEo = true;
    }

    /**
     * Old interrupt enable
     */
    public void resetIEo() {
      this.IEo = false;
    }

    /**
     * Old kernel/user mode (true: user mode)
     */
    public boolean getKUo() {
      return this.KUo;
    }

    /**
     * Old kernel/user mode (true: user mode)
     */
    public void setKUo() {
      assert false : "Unimplemented";
      this.KUo = true;
    }

    /**
     * Old kernel/user mode (true: user mode)
     */
    public void resetKUo() {
      this.KUo = false;
    }

    /**
     * Interrupt mask - enables or disables interrupts
     */
    public long getIm() {
      return this.Im;
    }

    /**
     * Interrupt mask - enables or disables interrupts
     */
    public void setIm(final long Im) {
      this.Im = Im;
    }

    /**
     * Isolate cache
     * <p>
     * When isolated, all load and store operations are
     * targeted to the data cache, rather than main memory.
     */
    public boolean getIsc() {
      return this.Isc;
    }

    /**
     * Isolate cache
     * <p>
     * When isolated, all load and store operations are
     * targeted to the data cache, rather than main memory.
     */
    public void setIsc() {
      throw new RuntimeException("Unsupported: isolating cache disables writing to RAM. It seems the BIOS just does this to zero out the data cache.");
    }

    /**
     * Isolate cache
     * <p>
     * When isolated, all load and store operations are
     * targeted to the data cache, rather than main memory.
     */
    public void resetIsc() {
      this.Isc = false;
    }

    /**
     * Swapped cache mode
     */
    public boolean getSwc() {
      return this.Swc;
    }

    /**
     * Swapped cache mode
     */
    public void setSwc() {
      assert false : "Unimplemented";
      this.Swc = true;
    }

    /**
     * Swapped cache mode
     */
    public void resetSwc() {
      this.Swc = false;
    }

    public boolean getPZ() {
      return this.PZ;
    }

    public void setPZ() {
      assert false : "Unimplemented";
      this.PZ = true;
    }

    public void resetPZ() {
      this.PZ = false;
    }

    public boolean getCM() {
      return this.CM;
    }

    public void setCM() {
      assert false : "Unimplemented";
      this.CM = true;
    }

    public void resetCM() {
      this.CM = false;
    }

    public boolean getPE() {
      return this.PE;
    }

    public void setPE() {
      assert false : "Unimplemented";
      this.PE = true;
    }

    public void resetPE() {
      this.PE = false;
    }

    public boolean getTS() {
      return this.TS;
    }

    public void setTS() {
      assert false : "Unimplemented";
      this.TS = true;
    }

    public void resetTS() {
      this.TS = false;
    }

    public boolean getBEV() {
      return this.BEV;
    }

    public void setBEV() {
      assert false : "Unimplemented";
      this.BEV = true;
    }

    public void resetBEV() {
      this.BEV = false;
    }

    public boolean getRE() {
      return this.RE;
    }

    public void setRE() {
      assert false : "Unimplemented";
      this.RE = true;
    }

    public void resetRE() {
      this.RE = false;
    }

    public boolean getCU0() {
      return this.CU0;
    }

    public void setCU0() {
      assert false : "Unimplemented";
      this.CU0 = true;
    }

    public void resetCU0() {
      this.CU0 = false;
    }

    public boolean getCU2() {
      return this.CU2;
    }

    public void setCU2() {
      this.CU2 = true;
    }

    public void resetCU2() {
      this.CU2 = false;
    }

    @Override
    public void dump(final ByteBuffer stream) {
      IoHelper.write(stream, this.IEc);
      IoHelper.write(stream, this.KUc);
      IoHelper.write(stream, this.IEp);
      IoHelper.write(stream, this.KUp);
      IoHelper.write(stream, this.IEo);
      IoHelper.write(stream, this.KUo);
      IoHelper.write(stream, this.Im);
      IoHelper.write(stream, this.Isc);
      IoHelper.write(stream, this.Swc);
      IoHelper.write(stream, this.PZ);
      IoHelper.write(stream, this.CM);
      IoHelper.write(stream, this.PE);
      IoHelper.write(stream, this.TS);
      IoHelper.write(stream, this.BEV);
      IoHelper.write(stream, this.RE);
      IoHelper.write(stream, this.CU0);
      IoHelper.write(stream, this.CU2);
    }

    @Override
    public void load(final ByteBuffer stream) {
      this.IEc = IoHelper.readBool(stream);
      this.KUc = IoHelper.readBool(stream);
      this.IEp = IoHelper.readBool(stream);
      this.KUp = IoHelper.readBool(stream);
      this.IEo = IoHelper.readBool(stream);
      this.KUo = IoHelper.readBool(stream);
      this.Im = IoHelper.readLong(stream);
      this.Isc = IoHelper.readBool(stream);
      this.Swc = IoHelper.readBool(stream);
      this.PZ = IoHelper.readBool(stream);
      this.CM = IoHelper.readBool(stream);
      this.PE = IoHelper.readBool(stream);
      this.TS = IoHelper.readBool(stream);
      this.BEV = IoHelper.readBool(stream);
      this.RE = IoHelper.readBool(stream);
      this.CU0 = IoHelper.readBool(stream);
      this.CU2 = IoHelper.readBool(stream);
    }
  }

  public static class CauseRegister extends Register {
    @Override
    public void set(final long value) {
      if((value & ~0b0000_0000_0000_0000_0000_0011_0000_0000) != 0) {
        throw new RuntimeException("Can't set readonly bits of cause register");
      }

      super.set(value);
    }
  }
}
