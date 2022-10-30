package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class AttackHitFlashEffect0c extends BttlScriptData6cSubBase1 {
  public final UnsignedIntRef _00;
  public final UnsignedShortRef u_04;
  public final UnsignedShortRef v_06;
  public final ByteRef w_08;
  public final UnsignedByteRef h_09;
  public final UnsignedShortRef clut_0a;

  public AttackHitFlashEffect0c(final Value ref) {
    super(ref);

    this._00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.u_04 = ref.offset(2, 0x04L).cast(UnsignedShortRef::new);
    this.v_06 = ref.offset(2, 0x06L).cast(UnsignedShortRef::new);
    this.w_08 = ref.offset(1, 0x08L).cast(ByteRef::new);
    this.h_09 = ref.offset(1, 0x09L).cast(UnsignedByteRef::new);
    this.clut_0a = ref.offset(2, 0x0aL).cast(UnsignedShortRef::new);
  }
}
