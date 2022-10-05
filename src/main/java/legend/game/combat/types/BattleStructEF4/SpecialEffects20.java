package legend.game.combat.types.BattleStructEF4;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.BoolRef;

/**
 * One for each ally and enemy
 */
public class SpecialEffects20 implements MemoryRef {
  private final Value ref;

  /**
   * <ul>
   *   <li>0x01 Attack</li>
   *   <li>0x02 Guard</li>
   *   <li>0x04 Items</li>
   *   <li>0x08 Escape</li>
   *   <li>0x10 Dragoon</li>
   *   <li>0x20 D-Attack</li>
   *   <li>0x40 Magic</li>
   *   <li>0x80 Special</li>
   * <ul>
   */
  public final UnsignedByteRef menuBlockFlag_18;
  /**
   * Each effect has two bits for up to 3 turns
   * <ul>
   *   <li>0 - 1 Material Shield</li>
   *   <li>2 - 3 Magical Shield</li>
   *   <li>4 - 5 Magic Sig Stone</li>
   *   <li>6 - 7 Charm Potion</li>
   * <ul>
   */
  public final UnsignedByteRef shieldsSigStoneCharmTurns_1c;
  public final UnsignedByteRef pandemoniumTurns_1d;
  public final BoolRef chargingSpirit_1e;

  public SpecialEffects20(final Value ref) {
    this.ref = ref;

    this.menuBlockFlag_18 = ref.offset(1, 0x18L).cast(UnsignedByteRef::new);
    this.shieldsSigStoneCharmTurns_1c = ref.offset(1, 0x1cL).cast(UnsignedByteRef::new);
    this.pandemoniumTurns_1d = ref.offset(1, 0x1dL).cast(UnsignedByteRef::new);
    this.chargingSpirit_1e = ref.offset(1, 0x1eL).cast(BoolRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
