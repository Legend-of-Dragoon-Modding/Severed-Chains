package legend.game.combat.effects;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;

public class ParticleInnerStuff04 implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef renderFrameCount_00;
  public final UnsignedByteRef colour_01;
  public final UnsignedByteRef _02;
  public final ByteRef flag_03;

  public ParticleInnerStuff04(final Value ref) {
    this.ref = ref;

    this.renderFrameCount_00 = ref.offset(1, 0x00).cast(UnsignedByteRef::new);
    this.colour_01 = ref.offset(1, 0x01).cast(UnsignedByteRef::new);
    this._02 = ref.offset(1, 0x02).cast(UnsignedByteRef::new);
    this.flag_03 = ref.offset(1, 0x03).cast(ByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
