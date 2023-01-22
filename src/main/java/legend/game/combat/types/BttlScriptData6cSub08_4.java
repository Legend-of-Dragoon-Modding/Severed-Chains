package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class BttlScriptData6cSub08_4 implements BttlScriptData6cSubBase1, MemoryRef {
  private final Value ref;

  public final ShortRef _00;
  public final ShortRef _02;
  public final ByteRef _04;
  public final ByteRef _05;
  public final ShortRef _06;

  public BttlScriptData6cSub08_4(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(2, 0x00L).cast(ShortRef::new);
    this._02 = ref.offset(2, 0x02L).cast(ShortRef::new);
    this._04 = ref.offset(1, 0x04L).cast(ByteRef::new);
    this._05 = ref.offset(1, 0x05L).cast(ByteRef::new);
    this._06 = ref.offset(2, 0x06L).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
