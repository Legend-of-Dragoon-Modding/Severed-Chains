package legend.game.combat.effects;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedShortRef;

public class StarChildrenMeteorEffectInstance10 implements MemoryRef {
  private final Value ref;

  public final ByteRef _00;

  public final ShortRef _02;
  public final ShortRef _04;
  public final ShortRef _06;
  public final ShortRef _08;
  public final ShortRef _0a;
  public final UnsignedShortRef scaleW_0c;
  public final UnsignedShortRef scaleH_0e;

  public StarChildrenMeteorEffectInstance10(final Value ref) {
    this.ref = ref;

    this._00 = ref.offset(1, 0x00).cast(ByteRef::new);
    this._02 = ref.offset(2, 0x02).cast(ShortRef::new);
    this._04 = ref.offset(2, 0x04).cast(ShortRef::new);
    this._06 = ref.offset(2, 0x06).cast(ShortRef::new);
    this._08 = ref.offset(2, 0x08).cast(ShortRef::new);
    this._0a = ref.offset(2, 0x0a).cast(ShortRef::new);
    this.scaleW_0c = ref.offset(2, 0x0c).cast(UnsignedShortRef::new);
    this.scaleH_0e = ref.offset(2, 0x0e).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
