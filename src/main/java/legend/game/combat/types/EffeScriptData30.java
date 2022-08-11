package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.MemoryRef;

public class EffeScriptData30 implements MemoryRef {
  private final Value ref;

  public final ArrayRef<EffeScriptData30Sub06> _00;

  public EffeScriptData30(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(1, 0x00L).cast(ArrayRef.of(EffeScriptData30Sub06.class, 8, 0x6, EffeScriptData30Sub06::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
