package legend.game;

import legend.core.DebugHelper;
import legend.core.gpu.RECT;
import legend.core.memory.Method;
import legend.core.memory.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static legend.core.Hardware.CPU;
import static legend.core.Hardware.DMA;
import static legend.core.Hardware.MEMORY;
import static legend.core.mdec.Mdec.MDEC_REG0;
import static legend.core.mdec.Mdec.MDEC_REG1;

public final class SStrm {
  private SStrm() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(SStrm.class);

  public static final Value _800fcd24 = MEMORY.ref(4, 0x800fcd24L);
  public static final Value _800fcd28 = MEMORY.ref(4, 0x800fcd28L);
  public static final Value _800fcd2c = MEMORY.ref(1, 0x800fcd2cL);

  public static final Value mdecQuantTableHeader_800fdb3c = MEMORY.ref(4, 0x800fdb3cL);
  public static final Value _800fdb8c = MEMORY.ref(4, 0x800fdb8cL); // Ref to the middle of the quant table...?
  public static final Value mdecScaleTableHeader_800fdbc0 = MEMORY.ref(4, 0x800fdbc0L);

  public static final Value _800fe794 = MEMORY.ref(2, 0x800fe794L);

  public static final Value _800fe7a1 = MEMORY.ref(1, 0x800fe7a1L);

  public static final Value resetMdec_800fe7a4 = MEMORY.ref(4, 0x800fe7a4L);
  public static final Value _800fe7a8 = MEMORY.ref(4, 0x800fe7a8L);

  public static final Value _800fe7b0 = MEMORY.ref(1, 0x800fe7b0L);

  public static final Value _8010f7b0 = MEMORY.ref(2, 0x8010f7b0L);
  public static final Value _8010f7b2 = MEMORY.ref(2, 0x8010f7b2L);
  public static final Value _8010f7b4 = MEMORY.ref(2, 0x8010f7b4L);

  public static final Value _8010f7b8 = MEMORY.ref(2, 0x8010f7b8L);

  public static final Value _8010f7ba = MEMORY.ref(2, 0x8010f7baL);

  public static final RECT _8010f7c0 = MEMORY.ref(8, 0x8010f7c0L, RECT::new);
  public static final Value _8010f7c8 = MEMORY.ref(2, 0x8010f7c8L);
  public static final Value _8010f7ca = MEMORY.ref(2, 0x8010f7caL);
  public static final Value _8010f7cc = MEMORY.ref(2, 0x8010f7ccL);
  public static final Value _8010f7ce = MEMORY.ref(2, 0x8010f7ceL);
  public static final Value linkedListEntry_8010f7d0 = MEMORY.ref(4, 0x8010f7d0L);
  public static final Value mdecInDoubleBufferFrame0_8010f7d4 = MEMORY.ref(4, 0x8010f7d4L);
  public static final Value mdecInDoubleBufferFrame1_8010f7d8 = MEMORY.ref(4, 0x8010f7d8L);
  public static final Value mdecOutDoubleBufferFrame0_8010f7dc = MEMORY.ref(4, 0x8010f7dcL);
  public static final Value mdecOutDoubleBufferFrame1_8010f7e0 = MEMORY.ref(4, 0x8010f7e0L);
  public static final Value mdecInDoubleBufferFrame_8010f7e4 = MEMORY.ref(4, 0x8010f7e4L);
  public static final Value mdecOutDoubleBufferFrame_8010f7e8 = MEMORY.ref(4, 0x8010f7e8L);
  public static final Value doubleBufferFrame_8010f7ec = MEMORY.ref(4, 0x8010f7ecL);
  public static final Value _8010f7f0 = MEMORY.ref(4, 0x8010f7f0L);
  public static final Value _8010f7f4 = MEMORY.ref(4, 0x8010f7f4L);
  public static final Value _8010f7f8 = MEMORY.ref(4, 0x8010f7f8L);

  private static long fmvTimer;

  @Method(0x800fc5f4L)
  public static void mdecReset(final boolean softReset) {
    MDEC_REG1.setu(0x8000_0000L); // Reset MDEC
    DMA.mdecIn.CHCR.setu(0);
    DMA.mdecOut.CHCR.setu(0);
    MDEC_REG1.setu(0x6000_0000L); // Enable data-in request, enable data-out request

    if(!softReset) {
      startMdecIn(mdecQuantTableHeader_800fdb3c.getAddress(), 0x20L);
      startMdecIn(mdecScaleTableHeader_800fdbc0.getAddress(), 0x20L);
    }
  }

  @Method(0x800fc6e4L)
  public static void startMdecIn(final long address, final long size) {
    mdecInSync();

    DMA.mdecIn.enable();
    DMA.mdecOut.enable();

    DMA.mdecIn.MADR.setu(address + 0x4L); // Set source address
    assert size / 0x20 > 0 : "Size was " + size;
    DMA.mdecIn.BCR.setu(size / 0x20 << 16 | 0x20); // Number of blocks << 16 | block size
    MDEC_REG0.setu(MEMORY.ref(4, address)); // Send command
    DMA.mdecIn.CHCR.setu(0x100_0201L); // Start/busy; sync blocks to DMA requests; from main RAM
  }

  @Method(0x800fc800L)
  public static long mdecInSync() {
    long timeoutCounter = 0x10_0000L;

    //LAB_800fc830
    while(MDEC_REG1.get(0b10_0000_0000_0000_0000_0000_0000_0000L) != 0) { // Command busy
      if(timeoutCounter-- == 0) {
        mdecTimeout("MDEC_in_sync");
        return -0x1L;
      }

      DebugHelper.sleep(1);
    }

    //LAB_800fc884
    return 0;
  }

  @Method(0x800fc894L)
  public static long mdecOutSync() {
    long timeoutCounter = 0x10_0000L;

    //LAB_800fc8c4
    while(DMA.mdecOut.channelControl.isBusy()) {
      if(timeoutCounter-- == 0) {
        mdecTimeout("MDEC_out_sync");
        return -0x1L;
      }

      DebugHelper.sleep(1);
    }

    //LAB_800fc918
    return 0;
  }

  @Method(0x800fc940L)
  public static void mdecTimeout(final String functionName) {
    LOGGER.error("%s timeout:", functionName);
    MDEC_REG1.setu(0x8000_0000L); // Reset MDEC
    DMA.mdecIn.CHCR.setu(0);
    DMA.mdecOut.CHCR.setu(0);
    MDEC_REG1.setu(0x6000_0000L); // Enable data-in request, enable data-out request
  }

  @Method(0x800fc9c4L)
  public static void FUN_800fc9c4(long a0, long a1) {
    long s0;

    final long s2 = a1;
    MEMORY.ref(2, s2).setu(MEMORY.ref(2, a0));
    MEMORY.ref(2, s2).offset(0x2L).setu(MEMORY.ref(2, a0).offset(0x2L));
    long v1 = (int)(MEMORY.ref(2, a0).offset(0x4L).get() + 0xfL) / 16;
    long v0 = (int)(MEMORY.ref(2, a0).offset(0x6L).get() + 0xfL) / 16;
    v1 = v1 * v0 & 0xffff_ffffL;
    s0 = a0 + 0xaL;
    long s1 = v1 * 6;
    final long[] arr = new long[(int)(v1 * 12 + 14 & ~0b111L)];
    FUN_800fcc50(arr, s0, v1 * 12);
    s0 += MEMORY.ref(2, a0).offset(0x8L).get();
    a1 = 0x10L;
    long t1 = 0;
    long t3 = s1;
    long t0 = s2 + 0x4L;
    s1--;
    long a2 = MEMORY.ref(2, s0).get() << 16 | MEMORY.ref(2, s0).offset(0x2L).get();
    s0 += 0x4L;

    //LAB_800fca8c
    while(s1 != -0x1L) {
      MEMORY.ref(2, t0).setu(arr[(int)t1] << 8 | arr[(int)t3]);
      t3++;
      t1++;
      t0 += 0x2L;

      //LAB_800fcaac
      while(true) {
        CPU.MTC2(a2, 0x1eL);
        a0 = a2 >>> 0x1dL;
        long a3;
        if((int)a2 < 0) {
          a3 = 0x3L;
          if(a0 < 0x6L) {
            MEMORY.ref(2, t0).setu(0xfe00L);
            t0 += 0x2L;
            a1 -= 0x2L;
            a2 = a2 << 0x2L & 0xffff_ffffL;
            if((int)a1 <= 0) {
              a1 += 0x10L;
              a2 |= MEMORY.ref(2, s0).get() << 0x10L - a1;
              s0 += 0x2L;
            }

            break;
          }

          //LAB_800fcaf8
          if(a0 == 0x6L) {
            v1 = 0x1L;
          } else {
            v1 = 0x3ffL;
          }

          //LAB_800fcb08
          MEMORY.ref(2, t0).setu(v1);
          t0 += 0x2L;
        } else {
          //LAB_800fcb14
          a0 = CPU.MFC2(0x1fL);
          a3 = a0 << 0x8L;
          boolean takeBranch = false;
          if(a2 >>> 0x17L == 0) {
            a1 -= 0x9L;
            a2 = a2 << 0x9L & 0xffff_ffffL;
            if((int)a1 <= 0) {
              a1 += 0x10L;
              a2 |= MEMORY.ref(2, s0).get() << 0x10L - a1;
              s0 += 0x2L;
            }

            //LAB_800fcb48
            a0 -= 0x9L;
            takeBranch = true;
          }

          //LAB_800fcb50
          if(a0 == 0x5L) {
            a1 -= 0x6L;
            a2 = a2 << 0x6L & 0xffff_ffffL;
            if((int)a1 <= 0) {
              a1 += 0x10L;
              a2 |= MEMORY.ref(2, s0).get() << 0x10L - a1;
              s0 += 0x2L;
            }

            //LAB_800fcb80
            MEMORY.ref(2, t0).setu(a2 >>> 0x10L);
            t0 += 0x2L;
            a3 = 0x10L;
          } else {
            takeBranch = true;
          }

          if(takeBranch) {
            //LAB_800fcb94
            v1 = _800fdb8c.offset(a3 + (a2 >>> 0x17L - a0 & 0xfcL)).get();
            MEMORY.ref(2, t0).setu(v1);
            t0 += 0x2L;
            a3 = v1 >>> 0x1aL;
          }
        }

        //LAB_800fcbc0
        a1 -= a3;
        a2 = a2 << a3 & 0xffff_ffffL;
        if((int)a1 <= 0) {
          a1 += 0x10L;
          a2 |= MEMORY.ref(2, s0).get() << 0x10L - a1;
          s0 += 0x2L;
        }
      }

      //LAB_800fcbe8
      s1--;
    }

    //LAB_800fcbf8
    //LAB_800fcc18
    while(t0 < s2 + (MEMORY.ref(2, s2).get() + 1) * 0x4L) {
      MEMORY.ref(2, t0).setu(0xfe00L);
      t0 += 0x2L;
    }

    //LAB_800fcc2c
  }

  /**
   * Returns an array rather than using a0 as an output
   *
   * I have verified that at least the first invocation of this method returns the correct array
   */
  @Method(0x800fcc50L)
  public static long[] FUN_800fcc50(final long[] out, long a1, long a2) {
    long a3;
    long t0;
    long t1;
    long t2;
    long t3;
    long v0;
    long v1;

    if(a2 == 0) {
      return out;
    }

    t1 = 0;

    //LAB_800fcc5c
    do {
      t3 = MEMORY.ref(1, a1).get();
      a1++;
      t2 = 0x7L;

      //LAB_800fcc68
      do {
        if(a2 == 0) {
          return out;
        }

        v1 = MEMORY.ref(1, a1).get();
        a1++;
        if((t3 & 0x1L) == 0) {
          out[(int)t1] = v1;
          t1++;
          a2--;
        } else {
          //LAB_800fcc90
          t0 = v1 & 0xffL;
          a3 = MEMORY.ref(1, a1).get();
          v0 = a3 & 0x80L;
          a1++;
          if(v0 == 0) {
            a3 &= 0x7fL;
          } else {
            //LAB_800fccb0
            v1 = MEMORY.ref(1, a1).get();
            a1++;
            v0 = a3 & 0x7fL;
            v0 <<= 0x8L;
            a3 = v0 | v1;
          }

          //LAB_800fccc4
          t0 += 0x3L;
          a3++;
          a3 = t1 - a3;
          if(a2 < t0) {
            t0 = a2;
          }

          //LAB_800fccdc
          a2 -= t0;
          v1 = t0;
          t0--;

          //LAB_800fccec
          while(v1 != 0) {
            v1 = t0;
            t0--;
            out[(int)t1] = out[(int)a3];
            t1++;
            a3++;
          }
        }

        //LAB_800fcd08
        t2--;
        t3 >>= 0x1L;
      } while(t2 != -0x1L);
    } while(a2 != 0);

    //LAB_800fcd1c
    return out;
  }
}
