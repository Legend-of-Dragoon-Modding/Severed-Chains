package legend.game.combat.types;

import legend.core.gte.SVECTOR;
import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class BttlScriptData6cSub38Sub14Sub30 implements MemoryRef {
  private final Value ref;

  public final VECTOR _00;
  public final SVECTOR colour_10;
  public final SVECTOR colour_16;
  public final SVECTOR _1c;
  public final SVECTOR _22;
  public final ByteRef _28;

  public final ShortRef _2a;
  public final ShortRef _2c;
  public final ShortRef _2e;

  public BttlScriptData6cSub38Sub14Sub30(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(4, 0x00L).cast(VECTOR::new);
    this.colour_10 = ref.offset(2, 0x10L).cast(SVECTOR::new);
    this.colour_16 = ref.offset(2, 0x16L).cast(SVECTOR::new);
    this._1c = ref.offset(2, 0x1cL).cast(SVECTOR::new);
    this._22 = ref.offset(2, 0x22L).cast(SVECTOR::new);
    this._28 = ref.offset(1, 0x28L).cast(ByteRef::new);

    this._2a = ref.offset(2, 0x2aL).cast(ShortRef::new);
    this._2c = ref.offset(2, 0x2cL).cast(ShortRef::new);
    this._2e = ref.offset(2, 0x2eL).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
