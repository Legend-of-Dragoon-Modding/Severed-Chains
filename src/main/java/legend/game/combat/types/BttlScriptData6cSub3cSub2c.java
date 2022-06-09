package legend.game.combat.types;

import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnsignedByteRef;

public class BttlScriptData6cSub3cSub2c implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef _00;
  public final UnsignedByteRef _01;
  public final UnsignedByteRef _02;
  public final UnsignedByteRef _03;
  public final ArrayRef<VECTOR> _04;
  public final Pointer<BttlScriptData6cSub3cSub2c> _24;
  public final Pointer<BttlScriptData6cSub3cSub2c> _28;

  public BttlScriptData6cSub3cSub2c(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);
    this._01 = ref.offset(1, 0x01L).cast(UnsignedByteRef::new);
    this._02 = ref.offset(1, 0x02L).cast(UnsignedByteRef::new);
    this._03 = ref.offset(1, 0x03L).cast(UnsignedByteRef::new);
    this._04 = ref.offset(4, 0x04L).cast(ArrayRef.of(VECTOR.class, 2, 0x10, VECTOR::new));
    this._24 = ref.offset(4, 0x24L).cast(Pointer.deferred(4, BttlScriptData6cSub3cSub2c::new));
    this._28 = ref.offset(4, 0x28L).cast(Pointer.deferred(4, BttlScriptData6cSub3cSub2c::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
