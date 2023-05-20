package legend.game.combat.effects;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class RaindropEffect0c implements MemoryRef {
  private final Value ref;

  public final ByteRef _00;

  public final ShortRef x0_02;
  public final ShortRef y0_04;
  public final ShortRef x1_06;
  public final ShortRef y1_08;
  public final ShortRef angleModifier_0a;

  public RaindropEffect0c(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(1, 0x00L).cast(ByteRef::new);

    this.x0_02 = ref.offset(2, 0x02L).cast(ShortRef::new);
    this.y0_04 = ref.offset(2, 0x04L).cast(ShortRef::new);
    this.x1_06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this.y1_08 = ref.offset(2, 0x08L).cast(ShortRef::new);
    this.angleModifier_0a = ref.offset(2, 0x0aL).cast(ShortRef::new);
  }

  @Override
  public long getAddress() { return this.ref.getAddress(); }
}
