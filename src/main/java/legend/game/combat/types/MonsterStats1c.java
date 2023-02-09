package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedShortRef;

public class MonsterStats1c implements MemoryRef {
  private final Value ref;

  public final UnsignedShortRef hp_00;
  public final UnsignedShortRef mp_02;
  public final UnsignedShortRef attack_04;
  public final UnsignedShortRef magicAttack_06;
  public final UnsignedByteRef speed_08;
  public final UnsignedByteRef defence_09;
  public final UnsignedByteRef magicDefence_0a;
  public final UnsignedByteRef attackAvoid_0b;
  public final UnsignedByteRef magicAvoid_0c;
  public final UnsignedByteRef specialEffectFlag_0d;
  public final UnsignedByteRef _0e;
  public final UnsignedByteRef elementFlag_0f;
  public final UnsignedByteRef elementalImmunityFlag_10;
  public final UnsignedByteRef statusResistFlag_11;
  public final ByteRef targetArrowX_12;
  public final ByteRef targetArrowY_13;
  public final ByteRef targetArrowZ_14;
  public final UnsignedByteRef _15;
  public final UnsignedByteRef _16;
  public final UnsignedByteRef _17;
  public final ByteRef _18;
  public final ByteRef _19;
  public final ByteRef _1a;
  public final ByteRef _1b;

  public MonsterStats1c(final Value ref) {
    this.ref = ref;

    this.hp_00 = ref.offset(2, 0x00L).cast(UnsignedShortRef::new);
    this.mp_02 = ref.offset(2, 0x02L).cast(UnsignedShortRef::new);
    this.attack_04 = ref.offset(2, 0x04L).cast(UnsignedShortRef::new);
    this.magicAttack_06 = ref.offset(2, 0x06L).cast(UnsignedShortRef::new);
    this.speed_08 = ref.offset(1, 0x08L).cast(UnsignedByteRef::new);
    this.defence_09 = ref.offset(1, 0x09L).cast(UnsignedByteRef::new);
    this.magicDefence_0a = ref.offset(1, 0x0aL).cast(UnsignedByteRef::new);
    this.attackAvoid_0b = ref.offset(1, 0x0bL).cast(UnsignedByteRef::new);
    this.magicAvoid_0c = ref.offset(1, 0x0cL).cast(UnsignedByteRef::new);
    this.specialEffectFlag_0d = ref.offset(1, 0x0dL).cast(UnsignedByteRef::new);
    this._0e = ref.offset(1, 0x0eL).cast(UnsignedByteRef::new);
    this.elementFlag_0f = ref.offset(1, 0x0fL).cast(UnsignedByteRef::new);
    this.elementalImmunityFlag_10 = ref.offset(1, 0x10L).cast(UnsignedByteRef::new);
    this.statusResistFlag_11 = ref.offset(1, 0x11L).cast(UnsignedByteRef::new);
    this.targetArrowX_12 = ref.offset(1, 0x12L).cast(ByteRef::new);
    this.targetArrowY_13 = ref.offset(1, 0x13L).cast(ByteRef::new);
    this.targetArrowZ_14 = ref.offset(1, 0x14L).cast(ByteRef::new);
    this._15 = ref.offset(1, 0x15L).cast(UnsignedByteRef::new);
    this._16 = ref.offset(1, 0x16L).cast(UnsignedByteRef::new);
    this._17 = ref.offset(1, 0x17L).cast(UnsignedByteRef::new);
    this._18 = ref.offset(1, 0x18L).cast(ByteRef::new);
    this._19 = ref.offset(1, 0x19L).cast(ByteRef::new);
    this._1a = ref.offset(1, 0x1aL).cast(ByteRef::new);
    this._1b = ref.offset(1, 0x1bL).cast(ByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
