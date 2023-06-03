package legend.game.combat.effects;

import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedByteRef;

public class WeaponTrailEffectSegment2c implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef unused_00;
  public final UnsignedByteRef unused_01;
  public final UnsignedByteRef unused_02;
  public final UnsignedByteRef _03;
  public final ArrayRef<VECTOR> endpointCoords_04;
  public final Pointer<WeaponTrailEffectSegment2c> previousSegmentRef_24;
  public final Pointer<WeaponTrailEffectSegment2c> nextSegmentRef_28;

  public WeaponTrailEffectSegment2c(final Value ref) {
    this.ref = ref;

    this.unused_00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);
    this.unused_01 = ref.offset(1, 0x01L).cast(UnsignedByteRef::new);
    this.unused_02 = ref.offset(1, 0x02L).cast(UnsignedByteRef::new);
    this._03 = ref.offset(1, 0x03L).cast(UnsignedByteRef::new);
    this.endpointCoords_04 = ref.offset(4, 0x04L).cast(ArrayRef.of(VECTOR.class, 2, 0x10, VECTOR::new));
    this.previousSegmentRef_24 = ref.offset(4, 0x24L).cast(Pointer.deferred(4, WeaponTrailEffectSegment2c::new));
    this.nextSegmentRef_28 = ref.offset(4, 0x28L).cast(Pointer.deferred(4, WeaponTrailEffectSegment2c::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
