package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.RelativePointer;
import legend.core.memory.types.UnsignedIntRef;

import java.util.function.Function;

public class DeffPointerTable08 implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef flags_00;
  public final RelativePointer<DeffPart> part_04;

  public static Function<Value, DeffPointerTable08> of(final DeffFile deff) {
    return value -> new DeffPointerTable08(value, deff);
  }

  public DeffPointerTable08(final Value ref, final DeffFile deff) {
    this.ref = ref;

    this.flags_00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.part_04 = ref.offset(4, 0x04L).cast(RelativePointer.deferred(4, deff.getAddress(), DeffPart::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
