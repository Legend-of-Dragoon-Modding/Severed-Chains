package legend.game.types;

import legend.core.gpu.RECT;
import legend.core.gte.SVECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

/** 0x24-bytes long */
public class EnvironmentStruct implements MemoryRef {
  private final Value ref;

  public final SVECTOR svec_00;
  /** Possible values: 0x4e, 0x4f, "anything else" (it's an else branch with no condition, but it's always been 0 in the files I've seen). First in-game cutscene has 0x4e for regular background textures and 0 for everything else. */
  public final ShortRef s_06;
  public final RECT pos_08;
  public final UnsignedShortRef us_10;
  public final UnsignedShortRef us_12;
  public final SVECTOR svec_14;
  public final UnsignedIntRef ui_1c;
  public final UnsignedShortRef tpage_20;
  public final ShortRef clutY_22;

  public EnvironmentStruct(final Value ref) {
    this.ref = ref;

    this.svec_00 = ref.offset(2, 0x00L).cast(SVECTOR::new);
    this.s_06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this.pos_08 = ref.offset(2, 0x08L).cast(RECT::new);
    this.us_10 = ref.offset(2, 0x10L).cast(UnsignedShortRef::new);
    this.us_12 = ref.offset(2, 0x12L).cast(UnsignedShortRef::new);
    this.svec_14 = ref.offset(4, 0x14L).cast(SVECTOR::new);
    this.ui_1c = ref.offset(4, 0x1cL).cast(UnsignedIntRef::new);
    this.tpage_20 = ref.offset(2, 0x20L).cast(UnsignedShortRef::new);
    this.clutY_22 = ref.offset(2, 0x22L).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
