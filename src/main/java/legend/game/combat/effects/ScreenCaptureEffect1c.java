package legend.game.combat.effects;

import legend.core.memory.Value;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedIntRef;

public class ScreenCaptureEffect1c implements BttlScriptData6cSubBase1, MemoryRef {
  private final Value ref;

  public final UnsignedIntRef ptr_00;
  public final IntRef captureW_04;
  public final IntRef captureH_08;
  /**
   * 0 is unknown, 1 is for Death Dimension, Melbu screenshot attack, and demon frog
   */
  public final IntRef rendererIndex_0c;
  /**
   * Capture width and height scaled by depth into scene and projection plane distance
   */
  public final IntRef screenspaceW_10;
  public final IntRef screenspaceH_14;

  public ScreenCaptureEffect1c(final Value ref) {
    this.ref = ref;

    this.ptr_00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.captureW_04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this.captureH_08 = ref.offset(4, 0x08L).cast(IntRef::new);
    this.rendererIndex_0c = ref.offset(4, 0x0cL).cast(IntRef::new);
    this.screenspaceW_10 = ref.offset(4, 0x10L).cast(IntRef::new);
    this.screenspaceH_14 = ref.offset(4, 0x14L).cast(IntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
