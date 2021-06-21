package legend.game;

import legend.core.memory.Method;
import legend.core.memory.Value;
import legend.core.memory.types.BiFunctionRef;
import legend.core.memory.types.CString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static legend.core.Hardware.MEMORY;
import static legend.game.Scus94491BpeSegment_8002.FUN_8002ae0c;
import static legend.game.Scus94491BpeSegment_8005.linkedListHead_8005a2a0;
import static legend.game.Scus94491BpeSegment_8005.linkedListTail_8005a2a4;
import static legend.game.Scus94491BpeSegment_8007._8007a398;
import static legend.game.Scus94491BpeSegment_8007._8007a39c;
import static legend.game.Scus94491BpeSegment_8007._8007a3a0;
import static legend.game.Scus94491BpeSegment_8007._8007a3b8;
import static legend.game.Scus94491BpeSegment_800b._800bb0fc;
import static legend.game.Scus94491BpeSegment_800b._800bee90;
import static legend.game.Scus94491BpeSegment_800b._800bee94;
import static legend.game.Scus94491BpeSegment_800b._800bee98;

public final class Scus94491BpeSegment {
  private Scus94491BpeSegment() { }

  private static final Logger LOGGER = LogManager.getFormatterLogger(Scus94491BpeSegment.class);

  private static final Object[] EMPTY_OBJ_ARRAY = new Object[0];

  public static final BiFunctionRef<Long, Object[], Object> functionVectorA_000000a0 = MEMORY.ref(4, 0x000000a0L, BiFunctionRef::new);
  public static final BiFunctionRef<Long, Object[], Object> functionVectorB_000000b0 = MEMORY.ref(4, 0x000000b0L, BiFunctionRef::new);
  public static final BiFunctionRef<Long, Object[], Object> functionVectorC_000000c0 = MEMORY.ref(4, 0x000000c0L, BiFunctionRef::new);

  public static final Value isStackPointerModified_1f8003bc = MEMORY.ref(2, 0x1f8003bcL);

  public static final Value _1f8003c0 = MEMORY.ref(4, 0x1f8003c0L);
  public static final Value _1f8003c4 = MEMORY.ref(4, 0x1f8003c4L);
  public static final Value _1f8003c8 = MEMORY.ref(4, 0x1f8003c8L);
  public static final Value _1f8003cc = MEMORY.ref(4, 0x1f8003ccL);

  public static final Value _1f8003dc = MEMORY.ref(2, 0x1f8003dcL);
  public static final Value _1f8003de = MEMORY.ref(2, 0x1f8003deL);

  public static final Value displayWidth_1f8003e0 = MEMORY.ref(4, 0x1f8003e0L);
  public static final Value displayHeight_1f8003e4 = MEMORY.ref(4, 0x1f8003e4L);

  public static final Value _1f8003fc = MEMORY.ref(4, 0x1f8003fcL);

  public static final Value _800103d0 = MEMORY.ref(4, 0x800103d0L);

  public static final Value _8001051c = MEMORY.ref(4, 0x8001051cL);

  public static final Value timHeader_80010548 = MEMORY.ref(4, 0x80010548L);

  /**
   * String: CD_sync
   */
  public static final CString _80011394 = MEMORY.ref(8, 0x80011394L, CString::new);

  /**
   * String: CD_cw
   */
  public static final CString _800113c0 = MEMORY.ref(6, 0x800113c0L, CString::new);

  public static final Value _8011e210 = MEMORY.ref(4, 0x8011e210L);

  @Method(0x80011e1cL)
  public static void FUN_80011e1c() {
    assert false : "Unimplemented";
    //TODO

    do {
//      FUN_80012d58();
      processControllerInput();
//      FUN_80011f24();
//      FUN_80014d20();
//      FUN_80022518();
//      FUN_80011ec0();
//      FUN_80011ec8();
//      FUN_8001b410();
//      FUN_80013778();
//      FUN_800145c4();
//      FUN_8001aa24();
//      FUN_8002a058();
//      FUN_8002a0e4();
//      FUN_80020ed8();
//      FUN_80017f94();
      _800bb0fc.addu(0x1L);
//      FUN_80012df8();

      try {
        Thread.sleep(1);
      } catch(final InterruptedException ignored) { }
    } while(true);
  }

  @Method(0x80012094L)
  public static void allocateLinkedList(long address, long size) {
    size = size - 0x18L & 0xffff_fffcL;
    address = address + 0x3L & 0xffff_fffcL;

    MEMORY.ref(4, address).offset(0x00L).setu(0);
    MEMORY.ref(4, address).offset(0x04L).setu(0xcL);
    MEMORY.ref(2, address).offset(0x08L).setu(0x3L);
    MEMORY.ref(4, address).offset(0x0cL).setu(address);
    MEMORY.ref(4, address).offset(0x10L).setu(size);
    MEMORY.ref(2, address).offset(0x14L).setu(0);
    MEMORY.ref(4, address).offset(size).offset(0x0cL).setu(address).addu(0xcL);
    MEMORY.ref(4, address).offset(size).offset(0x10L).setu(0);
    MEMORY.ref(2, address).offset(size).offset(0x14L).setu(0x3L);

    linkedListHead_8005a2a0.setu(address).addu(0xcL);
    linkedListTail_8005a2a4.setu(address).addu(0xcL).addu(size);
  }

  /**
   * 800135d8
   *
   * Copies the first n bytes of src to dest.
   *
   * @param dest Pointer to copy destination memory block
   * @param src Pointer to copy source memory block
   * @param length Number of bytes copied
   *
   * @return Pointer to destination (dest)
   */
  @Method(0x800135d8L)
  public static long memcpy(final long dest, long src, int length) {
    if(length == 0) {
      return dest;
    }

    final long v0 = (length | dest | src) & 0xfffffffcL;
    long a3 = dest;
    if(v0 == 0) {
      length = length / 4;

      //LAB_80013600
      while(length > 0) {
        MEMORY.ref(4, a3).setu(MEMORY.ref(4, src));
        src += 0x4L;
        a3 += 0x4L;
        length--;
      }

      return dest;
    }

    //LAB_80013630
    while(length > 0) {
      MEMORY.ref(1, a3).setu(MEMORY.ref(1, src));
      src++;
      a3++;
      length--;
    }

    //LAB_8001364c
    return dest;
  }

  @Method(0x800184b0L)
  public static void processControllerInput() {
    long a0 = _8007a3b8.get();

    if(a0 == 0) {
      a0 = 0x1;
    }

    FUN_8002ae0c(a0);

    _8007a398.setu(_800bee94);
    _8007a39c.setu(_800bee90);
    _8007a3a0.setu(_800bee98);
  }

  @Method(0x80019500L)
  public static void FUN_80019500() {
    assert false : "Unimplemented";
    //TODO
  }
}
