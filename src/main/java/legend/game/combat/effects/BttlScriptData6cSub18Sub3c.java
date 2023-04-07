package legend.game.combat.effects;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;

public class BttlScriptData6cSub18Sub3c implements MemoryRef {
  private final Value ref;

  public final ShortRef _00;

  public final UnsignedByteRef _03;
  public final ShortRef _04;
  public final ShortRef _06;
  public final ShortRef _08;

  public final ShortRef _36;
  public final ShortRef _38;

  public BttlScriptData6cSub18Sub3c(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(2, 0x00L).cast(ShortRef::new);

    this._03 = ref.offset(1, 0x03L).cast(UnsignedByteRef::new);
    this._04 = ref.offset(2, 0x04L).cast(ShortRef::new);
    this._06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this._08 = ref.offset(2, 0x08L).cast(ShortRef::new);

    this._36 = ref.offset(2, 0x36L).cast(ShortRef::new);
    this._38 = ref.offset(2, 0x38L).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
