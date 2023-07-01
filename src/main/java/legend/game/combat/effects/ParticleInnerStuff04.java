package legend.game.combat.effects;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;

public class ParticleInnerStuff04 implements MemoryRef {
  private final Value ref;

  public final ByteRef renderFrameCount_00;
  public final ByteRef colour_01;
  public final ByteRef _02;
  public final ByteRef flag_03;

  public ParticleInnerStuff04(final Value ref) {
    this.ref = ref;

    this.renderFrameCount_00 = ref.offset(1, 0x00).cast(ByteRef::new);
    this.colour_01 = ref.offset(1, 0x01).cast(ByteRef::new);
    this._02 = ref.offset(1, 0x02).cast(ByteRef::new);
    this.flag_03 = ref.offset(1, 0x03).cast(ByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
