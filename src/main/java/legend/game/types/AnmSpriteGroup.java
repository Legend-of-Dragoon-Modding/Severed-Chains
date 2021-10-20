package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

/** 0x18 bytes long */
public class AnmSpriteGroup implements MemoryRef {
  private final Value ref;

  public final UnsignedIntRef n_sprite_00;
  public final UnsignedByteRef u_04;
  public final UnsignedByteRef v_05;
  public final UnsignedByteRef ofs_x_06;
  public final UnsignedByteRef ofs_y_07;
  public final UnsignedShortRef cba_08;
  public final UnsignedShortRef flag_0a;
  public final UnsignedShortRef w_0c;
  public final UnsignedShortRef h_0e;
  public final UnsignedShortRef rot_10;
  public final UnsignedShortRef flag2_12;
  public final UnsignedShortRef x_14;
  public final UnsignedShortRef y_16;

  public AnmSpriteGroup(final Value ref) {
    this.ref = ref;

    this.n_sprite_00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.u_04 = ref.offset(1, 0x04L).cast(UnsignedByteRef::new);
    this.v_05 = ref.offset(1, 0x05L).cast(UnsignedByteRef::new);
    this.ofs_x_06 = ref.offset(1, 0x06L).cast(UnsignedByteRef::new);
    this.ofs_y_07 = ref.offset(1, 0x07L).cast(UnsignedByteRef::new);
    this.cba_08 = ref.offset(2, 0x08L).cast(UnsignedShortRef::new);
    this.flag_0a = ref.offset(2, 0x0aL).cast(UnsignedShortRef::new);
    this.w_0c = ref.offset(2, 0x0cL).cast(UnsignedShortRef::new);
    this.h_0e = ref.offset(2, 0x0eL).cast(UnsignedShortRef::new);
    this.rot_10 = ref.offset(2, 0x10L).cast(UnsignedShortRef::new);
    this.flag2_12 = ref.offset(2, 0x12L).cast(UnsignedShortRef::new);
    this.x_14 = ref.offset(2, 0x14L).cast(UnsignedShortRef::new);
    this.y_16 = ref.offset(2, 0x16L).cast(UnsignedShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
