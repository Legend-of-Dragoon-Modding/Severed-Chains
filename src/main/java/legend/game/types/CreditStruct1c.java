package legend.game.types;

import legend.core.gte.COLOUR;
import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class CreditStruct1c implements MemoryRef {
  private final Value ref;

  public final COLOUR colour_00;
  public final IntRef prevCreditSlot_04;
  public final IntRef type_08;
  public final ShortRef y_0c;
  public final UnsignedShortRef width_0e;
  public final UnsignedShortRef height_10;
  public final UnsignedShortRef scroll_12;
  public final UnsignedShortRef _14;
  /**
   * <ul>
   *   <li>0 - load</li>
   *   <li>2 - render</li>
   *   <li>3 - off screen</li>
   * </ul>
   */
  public final UnsignedByteRef state_16;

  public CreditStruct1c(final Value ref) {
    this.ref = ref;

    this.colour_00 = ref.offset(1, 0x00L).cast(COLOUR::new);
    this.prevCreditSlot_04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this.type_08 = ref.offset(4, 0x08L).cast(IntRef::new);
    this.y_0c = ref.offset(2, 0x0cL).cast(ShortRef::new);
    this.width_0e = ref.offset(2, 0x0eL).cast(UnsignedShortRef::new);
    this.height_10 = ref.offset(2, 0x10L).cast(UnsignedShortRef::new);
    this.scroll_12 = ref.offset(2, 0x12L).cast(UnsignedShortRef::new);
    this._14 = ref.offset(2, 0x14L).cast(UnsignedShortRef::new);
    this.state_16 = ref.offset(1, 0x16L).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
