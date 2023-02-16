package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;

public class EquipmentStats1c implements MemoryRef {
  private final Value ref;

  /**
   * <ul>
   *   <li>0x4 - can't be discarded</li>
   * </ul>
   */
  public final UnsignedByteRef flags_00;
  public final UnsignedByteRef type_01;
  public final UnsignedByteRef _02;
  public final UnsignedByteRef equipableFlags_03;
  public final UnsignedByteRef element_04;
  public final UnsignedByteRef _05;
  public final UnsignedByteRef elementalResistance_06;
  public final UnsignedByteRef elementalImmunity_07;
  public final UnsignedByteRef statusResist_08;
  public final UnsignedByteRef _09;
  public final UnsignedByteRef atk_0a;
  /**
   * Half Physical
   * SP on Magic/Physical Hit
   * MP on Magic Physical Hit
   * SP Multiplier
   */
  public final UnsignedByteRef special1_0b;
  /**
   * Half Magic
   * Revive
   * HP/MP/SP Regen
   */
  public final UnsignedByteRef special2_0c;
  /** Amount for MP/SP per hit or SP multiplier */
  public final UnsignedByteRef specialAmount_0d;
  public final UnsignedByteRef icon_0e;
  public final ByteRef spd_0f;
  public final ByteRef atkHi_10;
  public final ByteRef matk_11;
  public final ByteRef def_12;
  public final ByteRef mdef_13;
  public final ByteRef aHit_14;
  public final ByteRef mHit_15;
  public final ByteRef aAv_16;
  public final ByteRef mAv_17;
  public final ByteRef onStatusChance_18;
  public final UnsignedByteRef _19;
  public final UnsignedByteRef _1a;
  public final UnsignedByteRef onHitStatus_1b;

  public EquipmentStats1c(final Value ref) {
    this.ref = ref;

    this.flags_00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);
    this.type_01 = ref.offset(1, 0x01L).cast(UnsignedByteRef::new);
    this._02 = ref.offset(1, 0x02L).cast(UnsignedByteRef::new);
    this.equipableFlags_03 = ref.offset(1, 0x03L).cast(UnsignedByteRef::new);
    this.element_04 = ref.offset(1, 0x04L).cast(UnsignedByteRef::new);
    this._05 = ref.offset(1, 0x05L).cast(UnsignedByteRef::new);
    this.elementalResistance_06 = ref.offset(1, 0x06L).cast(UnsignedByteRef::new);
    this.elementalImmunity_07 = ref.offset(1, 0x07L).cast(UnsignedByteRef::new);
    this.statusResist_08 = ref.offset(1, 0x08L).cast(UnsignedByteRef::new);
    this._09 = ref.offset(1, 0x09L).cast(UnsignedByteRef::new);
    this.atk_0a = ref.offset(1, 0x0aL).cast(UnsignedByteRef::new);
    this.special1_0b = ref.offset(1, 0x0bL).cast(UnsignedByteRef::new);
    this.special2_0c = ref.offset(1, 0x0cL).cast(UnsignedByteRef::new);
    this.specialAmount_0d = ref.offset(1, 0x0dL).cast(UnsignedByteRef::new);
    this.icon_0e = ref.offset(1, 0x0eL).cast(UnsignedByteRef::new);
    this.spd_0f = ref.offset(1, 0x0fL).cast(ByteRef::new);
    this.atkHi_10 = ref.offset(1, 0x10L).cast(ByteRef::new);
    this.matk_11 = ref.offset(1, 0x11L).cast(ByteRef::new);
    this.def_12 = ref.offset(1, 0x12L).cast(ByteRef::new);
    this.mdef_13 = ref.offset(1, 0x13L).cast(ByteRef::new);
    this.aHit_14 = ref.offset(1, 0x14L).cast(ByteRef::new);
    this.mHit_15 = ref.offset(1, 0x15L).cast(ByteRef::new);
    this.aAv_16 = ref.offset(1, 0x16L).cast(ByteRef::new);
    this.mAv_17 = ref.offset(1, 0x17L).cast(ByteRef::new);
    this.onStatusChance_18 = ref.offset(1, 0x18L).cast(ByteRef::new);
    this._19 = ref.offset(1, 0x19L).cast(UnsignedByteRef::new);
    this._1a = ref.offset(1, 0x1aL).cast(UnsignedByteRef::new);
    this.onHitStatus_1b = ref.offset(1, 0x1bL).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
