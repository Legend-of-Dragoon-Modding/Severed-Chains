package legend.game.combat.types.BattleStructEF4;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;


/**
 * One for each ally and enemy
 */
public class AdditionExtra04 implements MemoryRef {
    private final Value ref;

    /**
     * <ul>
     *      <li>0x01 Destroyer Mace </li>
     *      <li>0x02 Wargod Sash (half damage) </li>
     *      <li>0x06 Ultimate Wargod (full damage) </li>
     * <ul>
     */
    public final UnsignedByteRef flag_00;

    public AdditionExtra04(final Value ref) {
        this.ref = ref;
        this.flag_00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);
    }

    @Override
    public long getAddress() { return this.ref.getAddress(); }
}
