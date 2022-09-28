package legend.game.combat.types.BattleStructEF4;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;

/**
 * One for each ally and enemy
 */
public class Status04 implements MemoryRef {
    private final Value ref;
    /**
     * Bunch of flags. Somehow monster/ally turn matters.
     */
    public final UnsignedByteRef statusEffect_00;
    /**
     * Does not decrease on all statuses
     */
    public final UnsignedByteRef statusTurns_01;
    // 0x02 is possibly bool has status??

    public Status04(final Value ref) {
        this.ref = ref;
        this.statusEffect_00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);
        this.statusTurns_01 = ref.offset(1, 0x01L).cast(UnsignedByteRef::new);

    }

    @Override
    public long getAddress() { return this.ref.getAddress(); }
}
