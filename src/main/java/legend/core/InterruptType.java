package legend.core;

import java.util.EnumSet;
import java.util.Set;

public enum InterruptType {
  VBLANK,
  GPU,
  CDROM,
  DMA,
  TMR0,
  TMR1,
  TMR2,
  CONTROLLER,
  SIO,
  SPU,
  ;

  public long getBit() {
    return 1L << this.ordinal();
  }

  public static long pack(final Set<InterruptType> interrupts) {
    return interrupts.stream().mapToLong(InterruptType::getBit).reduce(0, (a, b) -> a | b);
  }

  public static Set<InterruptType> unpack(final long packed) {
    return unpack(EnumSet.noneOf(InterruptType.class), packed);
  }

  public static Set<InterruptType> unpack(final Set<InterruptType> out, final long packed) {
    out.clear();

    for(final InterruptType interrupt : InterruptType.values()) {
      if((packed & interrupt.getBit()) != 0) {
        out.add(interrupt);
      }
    }

    return out;
  }
}
