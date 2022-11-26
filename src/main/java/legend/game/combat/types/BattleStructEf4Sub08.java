package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;

public class BattleStructEf4Sub08 implements MemoryRef {
  private final Value ref;

  public final Pointer<CombatantStruct1a8_c> _00;
  public final BoolRef used_04;

  public BattleStructEf4Sub08(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(Pointer.deferred(4, CombatantStruct1a8_c::new));
    this.used_04 = ref.offset(1, 0x04L).cast(BoolRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
