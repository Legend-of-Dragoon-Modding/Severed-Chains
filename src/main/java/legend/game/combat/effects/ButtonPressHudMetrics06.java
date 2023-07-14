package legend.game.combat.effects;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;

public class ButtonPressHudMetrics06 implements MemoryRef {
  public final Value ref;

  public UnsignedByteRef hudElementType_00;
  public UnsignedByteRef u_01;
  public UnsignedByteRef v_02;
  public UnsignedByteRef wOrRightU_03;
  public UnsignedByteRef hOrBottomV_04;
  public UnsignedByteRef clutOffset_05;

  public ButtonPressHudMetrics06(final Value ref) {
    this.ref = ref;

    this.hudElementType_00 = ref.offset(1, 0x00).cast(UnsignedByteRef::new);
    this.u_01 = ref.offset(1, 0x01).cast(UnsignedByteRef::new);
    this.v_02 = ref.offset(1, 0x02).cast(UnsignedByteRef::new);
    this.wOrRightU_03 = ref.offset(1, 0x03).cast(UnsignedByteRef::new);
    this.hOrBottomV_04 = ref.offset(1, 0x04).cast(UnsignedByteRef::new);
    this.clutOffset_05 = ref.offset(1, 0x05).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
