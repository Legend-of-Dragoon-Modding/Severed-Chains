package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.BiFunctionRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;

public abstract class BttlScriptData6cSubBase2 implements MemoryRef {
  private final Value ref;

  public final Pointer<BttlScriptData6cSubBase2> _00;
  public final UnsignedByteRef size_04;
  public final ByteRef _05;
  public final ShortRef _06;
  public final Pointer<BiFunctionRef<EffectManagerData6c, ? extends BttlScriptData6cSubBase2, Long>> _08;

  public BttlScriptData6cSubBase2(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(Pointer.deferred(4, value -> {throw new RuntimeException("Can't be instantiated");}));
    this.size_04 = ref.offset(1, 0x04L).cast(UnsignedByteRef::new);
    this._05 = ref.offset(1, 0x05L).cast(ByteRef::new);
    this._06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this._08 = ref.offset(4, 0x08L).cast(Pointer.deferred(4, BiFunctionRef::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
