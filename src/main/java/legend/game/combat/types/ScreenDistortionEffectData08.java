package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;

public class ScreenDistortionEffectData08 extends BttlScriptData6cSubBase1 {
  public final IntRef angle_00;
  public final IntRef angleStep_04;

  public ScreenDistortionEffectData08(final Value ref) {
    super(ref);

    this.angle_00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this.angleStep_04 = ref.offset(4, 0x04L).cast(IntRef::new);
  }
}
