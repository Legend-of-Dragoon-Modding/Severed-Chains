package legend.game.combat.environment;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;

public class NameAndPortraitDisplayMetrics0c implements MemoryRef {
  public final Value ref;

  public final UnsignedByteRef nameU_00;
  public final UnsignedByteRef nameV_01;
  public final UnsignedByteRef nameW_02;
  public final UnsignedByteRef nameH_03;
  public final UnsignedByteRef portraitU_04;
  public final UnsignedByteRef portraitV_05;
  public final UnsignedByteRef portraitW_06;
  public final UnsignedByteRef portraitH_07;
  public final UnsignedByteRef portraitClutOffset_08;
  /** Last 3 fields aren't anything, they were just word-aligning the structs. */
  public final UnsignedByteRef _09;
  public final UnsignedByteRef _0a;
  public final UnsignedByteRef _0b;

  public NameAndPortraitDisplayMetrics0c(final Value ref) {
    this.ref = ref;

    this.nameU_00 = ref.offset(1, 0x00).cast(UnsignedByteRef::new);
    this.nameV_01 = ref.offset(1, 0x01).cast(UnsignedByteRef::new);
    this.nameW_02 = ref.offset(1, 0x02).cast(UnsignedByteRef::new);
    this.nameH_03 = ref.offset(1, 0x03).cast(UnsignedByteRef::new);
    this.portraitU_04 = ref.offset(1, 0x04).cast(UnsignedByteRef::new);
    this.portraitV_05 = ref.offset(1, 0x05).cast(UnsignedByteRef::new);
    this.portraitW_06 = ref.offset(1, 0x06).cast(UnsignedByteRef::new);
    this.portraitH_07 = ref.offset(1, 0x07).cast(UnsignedByteRef::new);
    this.portraitClutOffset_08 = ref.offset(1, 0x08).cast(UnsignedByteRef::new);
    this._09 = ref.offset(1, 0x09).cast(UnsignedByteRef::new);
    this._0a = ref.offset(1, 0x0a).cast(UnsignedByteRef::new);
    this._0b = ref.offset(1, 0x0b).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
