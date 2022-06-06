package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;

public class BttlScriptData6cSub3cSub2c implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef _00;
  public final UnsignedByteRef _01;
  public final UnsignedByteRef _02;
  public final UnsignedByteRef _03;

  public final UnsignedIntRef _24;
  public final UnsignedIntRef _28;

  public BttlScriptData6cSub3cSub2c(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);
    this._01 = ref.offset(1, 0x01L).cast(UnsignedByteRef::new);
    this._02 = ref.offset(1, 0x02L).cast(UnsignedByteRef::new);
    this._03 = ref.offset(1, 0x03L).cast(UnsignedByteRef::new);

    this._24 = ref.offset(4, 0x24L).cast(UnsignedIntRef::new);
    this._28 = ref.offset(4, 0x28L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
