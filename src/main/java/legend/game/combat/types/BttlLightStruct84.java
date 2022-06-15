package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.game.types.GsF_LIGHT;

public class BttlLightStruct84 implements MemoryRef {
  private final Value ref;

  public final GsF_LIGHT light_00;
  public final BttlLightStruct84Sub3c _10;
  public final BttlLightStruct84Sub3c _4c;

  public BttlLightStruct84(final Value ref) {
    this.ref = ref;

    this.light_00 = ref.offset(4, 0x00L).cast(GsF_LIGHT::new);
    this._10 = ref.offset(4, 0x10L).cast(BttlLightStruct84Sub3c::new);
    this._4c = ref.offset(4, 0x4cL).cast(BttlLightStruct84Sub3c::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
