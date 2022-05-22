package legend.game;

import legend.core.InterruptType;
import legend.core.dma.DmaChannelType;
import legend.core.kernel.EXEC;
import legend.core.kernel.jmp_buf;
import legend.core.memory.EntryPoint;
import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.BiFunctionRef;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.RunnableRef;
import legend.core.memory.types.SupplierRef;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Function;

import static legend.core.Hardware.DMA;
import static legend.core.Hardware.GATE;
import static legend.core.Hardware.MEMORY;
import static legend.core.InterruptController.I_MASK;
import static legend.core.InterruptController.I_STAT;
import static legend.core.MemoryHelper.getMethodAddress;
import static legend.core.Timers.TMR_HRETRACE_MODE;
import static legend.core.dma.DmaManager.DMA_DICR;
import static legend.core.dma.DmaManager.DMA_DPCR;
import static legend.core.kernel.Bios.EnterCriticalSection;

@EntryPoint
public final class Scus94491 {
  private Scus94491() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491.class);

  private static final Object[] EMPTY_OBJ_ARRAY = new Object[0];

  public static final BiFunctionRef<Long, Object[], Object> functionVectorA_000000a0 = MEMORY.ref(4, 0x000000a0L, BiFunctionRef::new);
  public static final BiFunctionRef<Long, Object[], Object> functionVectorB_000000b0 = MEMORY.ref(4, 0x000000b0L, BiFunctionRef::new);
  public static final BiFunctionRef<Long, Object[], Object> functionVectorC_000000c0 = MEMORY.ref(4, 0x000000c0L, BiFunctionRef::new);

  public static final Value _1f800000 = MEMORY.ref(4, 0x1f800000L);

  public static final Value _80010000 = MEMORY.ref(4, 0x80010000L);

  public static final EXEC _80188898 = MEMORY.ref(4, 0x80188898L, EXEC::new);

  public static final Value _80188a88 = MEMORY.ref(4, 0x80188a88L);

  public static final Value _ramsize = MEMORY.ref(4, 0x801c5220L);
  public static final Value _stacksize = MEMORY.ref(4, 0x801c5224L);

  public static final Value isInitialized_801c5228 = MEMORY.ref(4, 0x801c5228L);
  public static final Value _801c522c = MEMORY.ref(4, 0x801c522cL);
  public static final Value _801c5230 = MEMORY.ref(4, 0x801c5230L);

  public static final Value _801c52ac = MEMORY.ref(4, 0x801c52acL);

  public static final BoolRef interruptHandlersInitialized_801c5424 = MEMORY.ref(2, 0x801c5424L, BoolRef::new);
  public static final Value inExceptionHandler_801c5426 = MEMORY.ref(2, 0x801c5426L);
  public static final Value interruptCallbacks_801c5428 = MEMORY.ref(4, 0x801c5428L);

  public static final Value _801c5454 = MEMORY.ref(2, 0x801c5454L);
  public static final Value _801c5456 = MEMORY.ref(2, 0x801c5456L);
  public static final Value _801c5458 = MEMORY.ref(2, 0x801c5458L);

  public static final jmp_buf _801c545c = MEMORY.ref(4, 0x801c545cL, jmp_buf::new);

  public static final Value _801c643c = MEMORY.ref(1, 0x801c643cL);

  public static final Value _801c64ac = MEMORY.ref(4, 0x801c64acL);

  public static final Value _801c64bc = MEMORY.ref(4, 0x801c64bcL);

  public static final ArrayRef<Pointer<RunnableRef>> vsyncCallbacks_801c64cc = (ArrayRef<Pointer<RunnableRef>>)MEMORY.ref(32, 0x801c64ccL, ArrayRef.of(Pointer.class, 8, 4, (Function)Pointer.of(4, RunnableRef::new)));

  public static final Value Vcount = MEMORY.ref(4, 0x801c64ecL);

  public static final ArrayRef<Pointer<RunnableRef>> dmaCallbacks_801c6500 = (ArrayRef<Pointer<RunnableRef>>)MEMORY.ref(28, 0x801c6500L, ArrayRef.of(Pointer.class, 7, 4, (Function)Pointer.of(4, RunnableRef::new)));

  public static final Value _801c66e0 = MEMORY.ref(4, 0x801c66e0L);

  public static final Value _801c66f4 = MEMORY.ref(4, 0x801c66f4L);

  public static final Value _801c7f68 = MEMORY.ref(4, 0x801c7f68L);

  public static final Value _bfc7ff52 = MEMORY.ref(1, 0xbfc7ff52L);

  @Method(0x801bf2a0L)
  public static long decompress(final long archiveAddress, final long destinationAddress) {
    // Check BPE header - this check is in the BPE block method but not the main EXE method
    if(MEMORY.ref(4, archiveAddress).offset(0x4L).get() != 0x1a455042L) {
      throw new RuntimeException("Attempted to decompress non-BPE segment at " + Long.toString(archiveAddress, 16));
    }

    LOGGER.info("Decompressing BPE segment at %08x to %08x", archiveAddress, destinationAddress);

    final Deque<Byte> unresolved_byte_list = new LinkedList<>();
    final byte[] dict_leftch = new byte[0x100];
    final byte[] dict_rightch = new byte[0x100];

    int totalSize = 0;
    int archiveOffset = 8;
    int destinationOffset = 0;

    // Each block is preceded by 4-byte int up to 0x800 giving the number
    // of decompressed bytes in the block. 0x00000000 indicates that there
    // are no further blocks and decompression is complete.
    int bytes_remaining_in_block = (int)MEMORY.get(archiveAddress + archiveOffset, 4);
    archiveOffset += 4;

    while(bytes_remaining_in_block != 0) {
      if(bytes_remaining_in_block > 0x800) {
        LOGGER.error("Decompress: 0x%08x at offset 0x%08x is an invalid block size", bytes_remaining_in_block, archiveOffset - 4);
        throw new RuntimeException("Decompression error");
      }

      totalSize += bytes_remaining_in_block;

      // Build the initial dictionary/lookup table. The left-character dict
      // is filled so that each key contains itself as a value, while the
      // right-character dict is filled with empty values.
      Arrays.fill(dict_rightch, (byte)0);
      for(int i = 0; i < 0x100; i++) {
        dict_leftch[i] = (byte)i;
      }

      // Build adaptive dictionary.
      int key = 0;
      while(key < 0x100) {
        // Dictionary is 256 bytes long. Loop until all keys filled.
        // If byte_pairs_to_read is >= 0x80, then only the next byte will
        // be read into the dictionary, placed at the index value calculated
        // using the below formula. Otherwise, the byte indicates how many
        // sequential bytes to read into the dictionary.
        int byte_pairs_to_read = MEMORY.get(archiveAddress + archiveOffset) & 0xff;
        archiveOffset++;

        if(byte_pairs_to_read >= 0x80) {
          key = key - 0x7f + byte_pairs_to_read;
          byte_pairs_to_read = 0;
        }

        // For each byte/byte pair to read, read the next byte and add it
        // to the leftch dict at the current key. If the character matches
        // the key it's at, increment key and continue. If it does not,
        // read the next character and add it to the same key in the
        // rightch dict before incrementing key and continuing.
        if(key < 0x100) {
          // Check that dictionary length not exceeded.
          for(int i = 0; i < byte_pairs_to_read + 1; i++) {
            dict_leftch[key] = MEMORY.get(archiveAddress + archiveOffset);
            archiveOffset++;

            if((dict_leftch[key] & 0xff) != key) {
              dict_rightch[key] = MEMORY.get(archiveAddress + archiveOffset);
              archiveOffset++;
            }

            key++;
          }
        }
      }

      // Decompress block
      // On each pass, read one byte and add it to a list of unresolved bytes.
      while(bytes_remaining_in_block > 0) {
        unresolved_byte_list.clear();
        unresolved_byte_list.push(MEMORY.get(archiveAddress + archiveOffset));
        archiveOffset++;

        // Pop the first item in the list of unresolved bytes. If the
        // byte key == value in dict_leftch, append it to the list of
        // decompressed bytes. If the byte key !=value in dict_leftch,
        // insert the leftch followed by rightch to the unresolved byte
        // list. Loop until the unresolved byte list is empty.
        while(!unresolved_byte_list.isEmpty()) {
          final byte compressed_byte = unresolved_byte_list.pop();
          if(compressed_byte == dict_leftch[compressed_byte & 0xff]) {
            MEMORY.set(destinationAddress + destinationOffset, compressed_byte);
            destinationOffset++;
            bytes_remaining_in_block -= 1;
          } else {
            unresolved_byte_list.push(dict_rightch[compressed_byte & 0xff]);
            unresolved_byte_list.push(dict_leftch[compressed_byte & 0xff]);
          }
        }
      }

      if(archiveOffset % 4 != 0) {
        // Word - align the pointer.
        archiveOffset = archiveOffset + 4 - archiveOffset % 4;
      }

      bytes_remaining_in_block = (int)MEMORY.get(archiveAddress + archiveOffset, 4);
      archiveOffset += 4;
    }

    //TODO not 100% sure this is the actual archive size
    LOGGER.info("Archive size: %d, decompressed size: %d", archiveOffset, totalSize);

    return totalSize;
  }

  @Method(0x801bf460L)
  public static void main() {
    setInitializedFlag();
    ResetCallback();
    detectRegion();
    _1f800000.setu(decompress(_80188a88.getAddress(), _80010000.getAddress()));
    StopCallback();
    EnterCriticalSection();

    MEMORY.addFunctions(Scus94491BpeSegment.class);
    MEMORY.addFunctions(Scus94491BpeSegment_8002.class);
    MEMORY.addFunctions(Scus94491BpeSegment_8003.class);
    MEMORY.addFunctions(Scus94491BpeSegment_8004.class);
    MEMORY.addFunctions(Scus94491BpeSegment_800e.class);
    Exec(_80188898, 1, 0);
  }

  @Method(0x801bf4e8L)
  public static void start(final int argc, final long argv) {
    LOGGER.info("--- SCUS 94491 start! ---");

    for(int i = 0; i < 0x622L; i++) {
      _801c66e0.offset(i * 4L).setu(0);
    }

    final long a0 = _801c7f68.getAddress() & 0x1fff_ffffL;
    _801c522c.setu(0x8000_0000L | a0);
    _801c5230.setu(_ramsize.get() - 0x8L - _stacksize.get() - a0);

    main();

    assert false : "Shouldn't get here";
  }

  @Method(0x801bf588L)
  public static void setInitializedFlag() {
    if(isInitialized_801c5228.get() == 0) {
      isInitialized_801c5228.setu(0x1L);
    }
  }

  @Method(0x801bf660L)
  public static void detectRegion() {
    GATE.acquire();

    if(_bfc7ff52.get() == 0x45) {
      _801c52ac.setu(0);
      _801c66f4.setu(2);
    } else {
      if(_bfc7ff52.get() < 0x46 && _bfc7ff52.get() == 0x41) {
        _801c52ac.setu(1);
        _801c66f4.setu(1);
      } else {
        _801c52ac.setu(1);
        _801c66f4.setu(0);
      }
    }

    GATE.release();
  }

  @Method(0x801c0990L)
  public static long Exec(final EXEC header, final int argc, final long argv) {
    return (long)functionVectorA_000000a0.run(0x43L, new Object[] {header, argc, argv});
  }

  /**
   * Set the control driver.
   * <p>
   * if val is 1, interrupt processing in a control driver started by a vertical retrace line interrupt is completed. If
   * val is 0, processing is passed to a lower priority interrupt module without completion.
   *
   * @param val Vertical retrace line interruption clear flag
   */
  @Method(0x801c0c20L)
  public static boolean ChangeClearPAD(final boolean val) {
    return (boolean)functionVectorB_000000b0.run(0x5bL, new Object[] {val});
  }

  /**
   * Selects what the kernel's timer/vblank IRQ handlers shall do after they have processed an IRQ
   *
   * @param t t=0..2: timer 0..2, or t=3: vblank
   * @param flag flag=0: do nothing; or flag=1: automatically acknowledge the IRQ and immediately return from exception
   * @return the previous flag value
   */
  @Method(0x801c0c30L)
  public static boolean ChangeClearRCnt(final int t, final boolean flag) {
    return (boolean)functionVectorC_000000c0.run(0xaL, new Object[] {t, flag});
  }

  @Method(0x801c0f10L)
  public static long ResetCallback() {
    return (long)_801c64ac.deref(4).offset(0xcL).deref(4).cast(SupplierRef::new).run();
  }

  @Method(0x801c0f40L)
  public static long InterruptCallback(final InterruptType interrupt, final long callback) {
    return (long)_801c64ac.deref(4).offset(0x8L).deref(4).cast(BiFunctionRef::new).run(interrupt, callback);
  }

  @Method(0x801c1004L)
  public static long StopCallback() {
    return (long)_801c64ac.deref(4).offset(0x10L).deref(4).cast(SupplierRef::new).run();
  }

  @Method(0x801c10a4L)
  public static long ResetCallback_Impl() {
    if(interruptHandlersInitialized_801c5424.get()) {
      return 0;
    }

    I_MASK.setu(0);
    I_STAT.setu(0);
    DMA_DPCR.setu(0x3333_3333L);

    zeroMemory_801c15ac(interruptHandlersInitialized_801c5424.getAddress(), 1050);

    setjmp(_801c545c, MEMORY.ref(4, getMethodAddress(Scus94491.class, "handleException"), RunnableRef::new));

    //LAB_801c111c
    _801c545c.sp.set(_801c643c.getAddress());
    SetCustomExitFromException(_801c545c);
    interruptHandlersInitialized_801c5424.set(true);
    _801c64ac.deref(4).offset(0x14L).setu(startInterruptVsync());
    _801c64ac.deref(4).offset(0x04L).setu(startInterruptDma());
    _96_remove();

    //LAB_801c116c
    return interruptHandlersInitialized_801c5424.getAddress();
  }

  @Method(0x801c117cL)
  public static void handleException() {
    if(interruptHandlersInitialized_801c5424.get()) {
      LOGGER.error("unexpected interrupt(%04x)", I_STAT.get());
      ReturnFromException();
      return;
    }

    //LAB_801c11d4
    inExceptionHandler_801c5426.setu(0x1L);
    long s0 = I_MASK.get(_801c5454.get(I_STAT));

    //LAB_801c1210
    while(s0 != 0) {
      long s1 = 0;
      long s2 = 0;

      //LAB_801c121c
      while(s0 != 0 && s1 < 0xbL) {
        if((s0 & 0x1L) != 0) {
          I_STAT.setu(~(0x1L << s1));
          final long v0 = interruptCallbacks_801c5428.offset(s2).get();
          if(v0 != 0) {
            MEMORY.ref(4, v0).call();
          }
        }

        //LAB_801c1258
        s2 += 0x4L;
        s0 >>>= 0x1L;
        s1++;
      }

      //LAB_801c126c
      s0 = I_MASK.get(_801c5454.get(I_STAT));
    }

    //LAB_801c129c
    if(I_STAT.get(I_MASK) == 0) {
      //LAB_801c1318
      _801c64bc.setu(0);
    } else {
      _801c64bc.addu(0x1L);
      if(_801c64bc.get() >= 0x800L) {
        LOGGER.error("intr timeout(%04x:%04x)", I_STAT.get(), I_MASK.get());
        _801c64bc.setu(0);
        I_STAT.setu(0);
      }
    }

    //LAB_801c1320
    inExceptionHandler_801c5426.setu(0);
    ReturnFromException();
  }

  @Method(0x801c134cL)
  public static long InterruptCallback_Impl(final InterruptType interrupt, final long callback) {
    final long oldCallback = interruptCallbacks_801c5428.offset(interrupt.ordinal() * 4L).get();
    if(callback == oldCallback || !interruptHandlersInitialized_801c5424.get()) {
      return oldCallback;
    }

    long oldIMask = I_MASK.get();
    I_MASK.setu(0);

    if(callback == 0) {
      //LAB_801c13dc
      interruptCallbacks_801c5428.offset(interrupt.ordinal() * 4L).setu(0);

      final long v0 = ~interrupt.getBit();
      oldIMask &= v0;
      _801c5454.and(v0);
    } else {
      interruptCallbacks_801c5428.offset(interrupt.ordinal() * 4L).setu(callback);

      final long v1 = interrupt.getBit();
      oldIMask |= v1;
      _801c5454.oru(v1);
    }

    //LAB_801c13fc
    if(interrupt == InterruptType.VBLANK) {
      ChangeClearPAD(callback == 0);
      ChangeClearRCnt(3, callback == 0);
    }

    //LAB_801c1420
    if(interrupt == InterruptType.TMR0) {
      ChangeClearRCnt(0, callback == 0);
    }

    //LAB_801c1438
    if(interrupt == InterruptType.TMR1) {
      ChangeClearRCnt(1, callback == 0);
    }

    //LAB_801c1450
    if(interrupt == InterruptType.TMR2) {
      ChangeClearRCnt(2, callback == 0);
    }

    //LAB_801c1460
    I_MASK.setu(oldIMask);

    //LAB_801c1470
    //LAB_801c1474
    return oldCallback;
  }

  @Method(0x801c1494L)
  public static long StopCallback_Impl() {
    if(!interruptHandlersInitialized_801c5424.get()) {
      return 0;
    }

    EnterCriticalSection();
    _801c5456.setu(I_MASK);
    _801c5458.setu(DMA_DPCR);
    I_MASK.setu(0);
    I_STAT.setu(I_MASK);
    DMA_DPCR.and(0x7777_7777L);
    SetDefaultExitFromException();
    interruptHandlersInitialized_801c5424.set(false);

    //LAB_801c1524
    return interruptHandlersInitialized_801c5424.getAddress();
  }

  @Method(0x801c15acL)
  public static void zeroMemory_801c15ac(final long address, final int words) {
    for(long i = address; i < address + words * 4L; i += 4L) {
      MEMORY.set(i, 4, 0L);
    }
  }

  @Method(0x801c15d8L)
  public static void _96_remove() {
    functionVectorA_000000a0.run(0x72L, EMPTY_OBJ_ARRAY);
  }

  @Method(0x801c15f0L)
  public static void ReturnFromException() {
    functionVectorB_000000b0.run(0x17L, EMPTY_OBJ_ARRAY);
  }

  @Method(0x801c1600L)
  public static void SetDefaultExitFromException() {
    functionVectorB_000000b0.run(0x18L, EMPTY_OBJ_ARRAY);
  }

  @Method(0x801c1610L)
  public static void SetCustomExitFromException(final jmp_buf buffer) {
    functionVectorB_000000b0.run(0x19L, new Object[] {buffer});
  }

  @Method(0x801c1d60L)
  public static void setjmp(final jmp_buf buffer, final RunnableRef callback) {
    buffer.set(callback);
  }

  @Method(0x801c1de0L)
  public static long startInterruptVsync() {
    TMR_HRETRACE_MODE.setu(0x100L);
    Vcount.setu(0);

    zeroMemory_801c1ed0(vsyncCallbacks_801c64cc.getAddress(), 8);
    InterruptCallback(InterruptType.VBLANK, getMethodAddress(Scus94491.class, "executeVsyncCallbacks"));

    return getMethodAddress(Scus94491.class, "SetVsyncInterruptCallback_Impl", InterruptType.class, long.class);
  }

  @Method(0x801c1e38L)
  public static void executeVsyncCallbacks() {
    Vcount.addu(0x1L);

    for(int i = 0; i < 8; i++) {
      final Pointer<RunnableRef> ptr = vsyncCallbacks_801c64cc.get(i);

      if(!ptr.isNull()) {
        ptr.deref().run();
      }
    }
  }

  @Method(0x801c1ea4L)
  public static void SetVsyncInterruptCallback_Impl(final InterruptType interrupt, final long callback) {
    final Pointer<RunnableRef> ptr = vsyncCallbacks_801c64cc.get(interrupt.ordinal());

    if(callback != ptr.deref().getAddress()) {
      ptr.set(MEMORY.ref(4, callback, RunnableRef::new));
    }
  }

  @Method(0x801c1ed0L)
  public static void zeroMemory_801c1ed0(final long address, final int words) {
    for(long i = address; i < address + words * 4L; i += 4L) {
      MEMORY.set(i, 4, 0L);
    }
  }

  @Method(0x801c1f00L)
  public static long startInterruptDma() {
    DMA_DICR.setu(0);

    zeroMemory_801c2178(dmaCallbacks_801c6500.getAddress(), 8);
    InterruptCallback(InterruptType.DMA, getMethodAddress(Scus94491.class, "executeDmaInterruptCallbacks"));

    return getMethodAddress(Scus94491.class, "SetDmaInterruptCallback_Impl", DmaChannelType.class, long.class);
  }

  @Method(0x801c1f4cL)
  public static void executeDmaInterruptCallbacks() {
    long s1 = DMA_DICR.get() >>> 24 & 0x7fL;

    //LAB_801c1fa0
    while(s1 != 0) {
      //LAB_801c1fac
      for(int i = 0; i < 7 && s1 != 0; i++) {
        if((s1 & 0x1L) != 0) {
          DMA_DICR.and(0xff_ffffL | 0x1L << i + 24);
          final Pointer<RunnableRef> callback = dmaCallbacks_801c6500.get(i);
          if(!callback.isNull()) {
            callback.deref().run();
          }
        }

        //LAB_801c1ff4
        s1 >>>= 0x1L;
      }

      //LAB_801c2004
      s1 = DMA_DICR.get() >>> 24 & 0x7fL;
    }

    //LAB_801c2028
    if(DMA_DICR.get(0xff00_0000L) == 0x8000_0000L || DMA_DICR.get(0x8000L) != 0) {
      //LAB_801c2060
      LOGGER.error("DMA bus error: code=%08x", DMA_DICR.get());

      //LAB_801c2074
      for(int i = 0; i < 7; i++) {
        LOGGER.info("MADR[%d]=%08x", i, DMA.mdecIn.MADR.offset(i << 0x4L).get());
      }
    }

    //LAB_801c20a8
  }

  @Method(0x801c20ccL)
  public static long SetDmaInterruptCallback_Impl(final DmaChannelType channel, final long callback) {
    final Pointer<RunnableRef> ptr = dmaCallbacks_801c6500.get(channel.ordinal());
    final long previous = ptr.isNull() ? 0 : ptr.deref().getAddress();

    if(callback != previous) {
      if(callback == 0) {
        //LAB_801c2134
        ptr.clear();
        DMA_DICR.setu(0x80_0000L | DMA_DICR.get(0xff_ffffL) & ~(0x1L << channel.ordinal() + 16L));
      } else {
        ptr.set(MEMORY.ref(4, callback, RunnableRef::new));
        DMA_DICR.setu(DMA_DICR.get(0xff_ffffL) | 0x80_0000L | 0x1L << channel.ordinal() + 16L);
      }
    }

    //LAB_801c2170
    return previous;
  }

  @Method(0x801c2178L)
  public static void zeroMemory_801c2178(final long address, final int words) {
    for(long i = address; i < address + words * 4L; i += 4L) {
      MEMORY.set(i, 4, 0L);
    }
  }
}
