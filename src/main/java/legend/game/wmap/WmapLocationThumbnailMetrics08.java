package legend.game.wmap;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;

public class WmapLocationThumbnailMetrics08 implements MemoryRef {
  public final Value ref;

  public final ShortRef imageX_00;
  public final ShortRef imageY_02;
  public final ShortRef clutX_04;
  public final ShortRef clutY_06;

  public WmapLocationThumbnailMetrics08(final Value ref) {
    this.ref = ref;

    this.imageX_00 = ref.offset(2, 0x00).cast(ShortRef::new);
    this.imageY_02 = ref.offset(2, 0x02).cast(ShortRef::new);
    this.clutX_04 = ref.offset(2, 0x04).cast(ShortRef::new);
    this.clutY_06 = ref.offset(2, 0x06).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
