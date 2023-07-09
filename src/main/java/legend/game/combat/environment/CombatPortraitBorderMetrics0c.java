package legend.game.combat.environment;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ByteRef;

public class CombatPortraitBorderMetrics0c implements MemoryRef {
  public final Value ref;

  public final ByteRef x1Index_00;
  public final ByteRef y1Index_01;
  public final ByteRef x2Index_02;
  public final ByteRef y2Index_03;
  public final ByteRef x1Offset_04;
  public final ByteRef y1Offset_05;
  public final ByteRef x2Offset_06;
  public final ByteRef y2Offset_07;
  public final ByteRef _08;
  public final ByteRef _09;
  public final ByteRef _0a;
  public final ByteRef _0b;
  
  public CombatPortraitBorderMetrics0c(final Value ref) {
    this.ref = ref;

    this.x1Index_00 = ref.offset(1, 0x00).cast(ByteRef::new);
    this.y1Index_01 = ref.offset(1, 0x01).cast(ByteRef::new);
    this.x2Index_02 = ref.offset(1, 0x02).cast(ByteRef::new);
    this.y2Index_03 = ref.offset(1, 0x03).cast(ByteRef::new);
    this.x1Offset_04 = ref.offset(1, 0x04).cast(ByteRef::new);
    this.y1Offset_05 = ref.offset(1, 0x05).cast(ByteRef::new);
    this.x2Offset_06 = ref.offset(1, 0x06).cast(ByteRef::new);
    this.y2Offset_07 = ref.offset(1, 0x07).cast(ByteRef::new);
    this._08 = ref.offset(1, 0x08).cast(ByteRef::new);
    this._09 = ref.offset(1, 0x09).cast(ByteRef::new);
    this._0a = ref.offset(1, 0x0a).cast(ByteRef::new);
    this._0b = ref.offset(1, 0x0b).cast(ByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
