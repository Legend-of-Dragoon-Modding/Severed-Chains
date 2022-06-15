package legend.game.combat.types;

import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class BttlScriptData6cSub44 extends BttlScriptData6cSubBase1 {
  public final VECTOR vec_10;
  public final VECTOR vec_20;

  public final UnsignedByteRef _30;
  public final UnsignedByteRef _31;
  public final ByteRef _32;

  public final ShortRef _34;
  public final UnsignedShortRef _36;
  public final UnsignedByteRef _38;
  public final UnsignedByteRef _39;
  public final UnsignedByteRef _3a;

  public final UnsignedIntRef _3c;
  public final UnsignedIntRef _40; //TODO 8 0x20-byte structs

  public BttlScriptData6cSub44(final Value ref) {
    super(ref);

    this.vec_10 = ref.offset(4, 0x10L).cast(VECTOR::new);
    this.vec_20 = ref.offset(4, 0x20L).cast(VECTOR::new);

    this._30 = ref.offset(1, 0x30L).cast(UnsignedByteRef::new);
    this._31 = ref.offset(1, 0x31L).cast(UnsignedByteRef::new);
    this._32 = ref.offset(1, 0x32L).cast(ByteRef::new);

    this._34 = ref.offset(2, 0x34L).cast(ShortRef::new);
    this._36 = ref.offset(2, 0x36L).cast(UnsignedShortRef::new);
    this._38 = ref.offset(1, 0x38L).cast(UnsignedByteRef::new);
    this._39 = ref.offset(1, 0x39L).cast(UnsignedByteRef::new);
    this._3a = ref.offset(1, 0x3aL).cast(UnsignedByteRef::new);

    this._3c = ref.offset(4, 0x3cL).cast(UnsignedIntRef::new);
    this._40 = ref.offset(4, 0x40L).cast(UnsignedIntRef::new);
  }
}
