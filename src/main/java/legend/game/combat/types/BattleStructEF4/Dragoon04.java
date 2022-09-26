package legend.game.combat.types.BattleStructEF4;

import legend.core.memory.*;
import legend.core.memory.types.*;

public class Dragoon04 implements MemoryRef {
    private final Value ref;

    public UnsignedByteRef dragoonTurns_00;

    public Dragoon04(final Value ref) {
        this.ref = ref;
        this.dragoonTurns_00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);

    }

    @Override
    public long getAddress() { return this.ref.getAddress(); }
}
