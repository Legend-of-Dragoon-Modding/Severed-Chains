package legend.game.combat.environment;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedShortRef;

/** Used to darken the stage during things like counterattacks and dragoon transformations */
public class BattleStageDarkening1800 implements MemoryRef {
  private final Value ref;

  public final ArrayRef<ArrayRef<UnsignedShortRef>> _000;
  public final ArrayRef<ArrayRef<UnsignedShortRef>> _800;
  public final ArrayRef<ArrayRef<UnsignedShortRef>> _1000;

  public BattleStageDarkening1800(final Value ref) {
    this.ref = ref;

    this._000 = ref.offset(4, 0x000L).cast(ArrayRef.of(ArrayRef.classFor(UnsignedShortRef.class), 0x40, 0x20, ArrayRef.of(UnsignedShortRef.class, 0x10, 2, UnsignedShortRef::new)));
    this._800 = ref.offset(4, 0x800L).cast(ArrayRef.of(ArrayRef.classFor(UnsignedShortRef.class), 0x40, 0x20, ArrayRef.of(UnsignedShortRef.class, 0x10, 2, UnsignedShortRef::new)));
    this._1000 = ref.offset(4, 0x1000L).cast(ArrayRef.of(ArrayRef.classFor(UnsignedShortRef.class), 0x40, 0x20, ArrayRef.of(UnsignedShortRef.class, 0x10, 2, UnsignedShortRef::new)));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
