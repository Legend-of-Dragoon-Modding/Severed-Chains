package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;

public class ScreenDistortionEffectData08 implements BttlScriptData6cSubBase1, MemoryRef {
  private final Value ref;

  public final IntRef angle_00;
  public final IntRef angleStep_04;

  public ScreenDistortionEffectData08(final Value ref) {
    this.ref = ref;

    this.angle_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this.angleStep_04 = ref.offset(4, 0x04L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
