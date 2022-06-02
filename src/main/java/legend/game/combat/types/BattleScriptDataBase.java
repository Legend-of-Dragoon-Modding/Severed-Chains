package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

public class BattleScriptDataBase implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef magic_00;

  public BattleScriptDataBase(final Value ref) {
    this.ref = ref;

    this.magic_00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
