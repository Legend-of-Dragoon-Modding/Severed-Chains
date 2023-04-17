package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;

public class SpellStats0c implements MemoryRef {
  private final Value ref;

  /**
   * <ul>
   *   <li>0x8 - attack all</li>
   * </ul>
   */
  public final UnsignedByteRef targetType_00;
  /**
   * <ul>
   *   <li>0x4 - either buff spell or always hit (or both)</li>
   * </ul>
   */
  public final UnsignedByteRef flags_01;
  public final UnsignedByteRef specialEffect_02;
  public final UnsignedByteRef damage_03;
  public final UnsignedByteRef multi_04;
  public final UnsignedByteRef accuracy_05;
  public final UnsignedByteRef mp_06;
  public final UnsignedByteRef statusChance_07;
  public final UnsignedByteRef element_08;
  public final UnsignedByteRef statusType_09;
  public final UnsignedByteRef buffType_0a;
  public final UnsignedByteRef _0b;

  public SpellStats0c(final Value ref) {
    this.ref = ref;

    this.targetType_00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);
    this.flags_01 = ref.offset(1, 0x01L).cast(UnsignedByteRef::new);
    this.specialEffect_02 = ref.offset(1, 0x02L).cast(UnsignedByteRef::new);
    this.damage_03 = ref.offset(1, 0x03L).cast(UnsignedByteRef::new);
    this.multi_04 = ref.offset(1, 0x04L).cast(UnsignedByteRef::new);
    this.accuracy_05 = ref.offset(1, 0x05L).cast(UnsignedByteRef::new);
    this.mp_06 = ref.offset(1, 0x06L).cast(UnsignedByteRef::new);
    this.statusChance_07 = ref.offset(1, 0x07L).cast(UnsignedByteRef::new);
    this.element_08 = ref.offset(1, 0x08L).cast(UnsignedByteRef::new);
    this.statusType_09 = ref.offset(1, 0x09L).cast(UnsignedByteRef::new);
    this.buffType_0a = ref.offset(1, 0x0aL).cast(UnsignedByteRef::new);
    this._0b = ref.offset(1, 0x0bL).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
