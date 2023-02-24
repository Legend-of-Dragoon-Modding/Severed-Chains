package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

import javax.annotation.Nullable;

public class BattleStruct24 implements MemoryRef {
  @Nullable
  private final Value ref;

  public final UnsignedIntRef flags_00;
  public final ShortRef x_04;
  public final ShortRef y_06;
  public final UnsignedShortRef w_08;
  public final UnsignedShortRef h_0a;
  public final UnsignedShortRef tpage_0c;
  public final UnsignedByteRef u_0e;
  public final UnsignedByteRef v_0f;
  public final UnsignedShortRef clutX_10;
  public final UnsignedShortRef clutY_12;
  public final ByteRef r_14;
  public final ByteRef g_15;
  public final ByteRef b_16;

  public final ShortRef _18;
  public final ShortRef _1a;
  public final ShortRef scaleX_1c;
  public final ShortRef scaleY_1e;
  public final IntRef rotation_20;

  public BattleStruct24(final Value ref) {
    this.ref = ref;

    this.flags_00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.x_04 = ref.offset(2, 0x04L).cast(ShortRef::new);
    this.y_06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this.w_08 = ref.offset(2, 0x08L).cast(UnsignedShortRef::new);
    this.h_0a = ref.offset(2, 0x0aL).cast(UnsignedShortRef::new);
    this.tpage_0c = ref.offset(2, 0x0cL).cast(UnsignedShortRef::new);
    this.u_0e = ref.offset(1, 0x0eL).cast(UnsignedByteRef::new);
    this.v_0f = ref.offset(1, 0x0fL).cast(UnsignedByteRef::new);
    this.clutX_10 = ref.offset(2, 0x10L).cast(UnsignedShortRef::new);
    this.clutY_12 = ref.offset(2, 0x12L).cast(UnsignedShortRef::new);
    this.r_14 = ref.offset(1, 0x14L).cast(ByteRef::new);
    this.g_15 = ref.offset(1, 0x15L).cast(ByteRef::new);
    this.b_16 = ref.offset(1, 0x16L).cast(ByteRef::new);

    this._18 = ref.offset(2, 0x18L).cast(ShortRef::new);
    this._1a = ref.offset(2, 0x1aL).cast(ShortRef::new);
    this.scaleX_1c = ref.offset(2, 0x1cL).cast(ShortRef::new);
    this.scaleY_1e = ref.offset(2, 0x1eL).cast(ShortRef::new);
    this.rotation_20 = ref.offset(4, 0x20L).cast(IntRef::new);
  }

  public BattleStruct24() {
    this.ref = null;

    this.flags_00 = new UnsignedIntRef();
    this.x_04 = new ShortRef();
    this.y_06 = new ShortRef();
    this.w_08 = new UnsignedShortRef();
    this.h_0a = new UnsignedShortRef();
    this.tpage_0c = new UnsignedShortRef();
    this.u_0e = new UnsignedByteRef();
    this.v_0f = new UnsignedByteRef();
    this.clutX_10 = new UnsignedShortRef();
    this.clutY_12 = new UnsignedShortRef();
    this.r_14 = new ByteRef();
    this.g_15 = new ByteRef();
    this.b_16 = new ByteRef();

    this._18 = new ShortRef();
    this._1a = new ShortRef();
    this.scaleX_1c = new ShortRef();
    this.scaleY_1e = new ShortRef();
    this.rotation_20 = new IntRef();
  }

  @Override
  public long getAddress() {
    if(this.ref == null) {
      throw new RuntimeException("Can't get address of stack object");
    }

    return this.ref.getAddress();
  }
}
