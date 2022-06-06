package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class BttlScriptData6cSub98Inner24 implements MemoryRef {
  private final Value ref;

  public final UnsignedShortRef _10;

  public final UnsignedShortRef _14;

  public final UnsignedShortRef _18;

  public final UnsignedIntRef _1c;
  public final UnsignedShortRef _20;

  public BttlScriptData6cSub98Inner24(final Value ref) {
    this.ref = ref;

    this._10 = ref.offset(2, 0x10L).cast(UnsignedShortRef::new);

    this._14 = ref.offset(2, 0x14L).cast(UnsignedShortRef::new);

    this._18 = ref.offset(2, 0x18L).cast(UnsignedShortRef::new);

    this._1c = ref.offset(4, 0x1cL).cast(UnsignedIntRef::new);
    this._20 = ref.offset(2, 0x20L).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
