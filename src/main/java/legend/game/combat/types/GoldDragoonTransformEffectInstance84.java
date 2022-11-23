package legend.game.combat.types;

import legend.core.gte.TmdObjTable;
import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.BoolRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;

public class GoldDragoonTransformEffectInstance84 implements MemoryRef {
  public final Value ref;

  public final BoolRef used_00;

  public final IntRef counter_04;
  public final VECTOR trans_08;
  public final VECTOR transStep_28;
  public final VECTOR rot_38;
  public final VECTOR rotStep_48;
  public final ByteRef _68;
  public final ByteRef _69;
  public final ByteRef _6a;

  public final Pointer<TmdObjTable> tmd_70;

  public final ShortRef _7c;
  public final ShortRef _7e;
  public final ShortRef _80;

  public GoldDragoonTransformEffectInstance84(final Value ref) {
    this.ref = ref;

    this.used_00 = ref.offset(1, 0x00L).cast(BoolRef::new);

    this.counter_04 = ref.offset(4, 0x04L).cast(IntRef::new);
    this.trans_08 = ref.offset(4, 0x08L).cast(VECTOR::new);
    this.transStep_28 = ref.offset(4, 0x28L).cast(VECTOR::new);
    this.rot_38 = ref.offset(4, 0x38L).cast(VECTOR::new);
    this.rotStep_48 = ref.offset(4, 0x48L).cast(VECTOR::new);
    this._68 = ref.offset(1, 0x68L).cast(ByteRef::new);
    this._69 = ref.offset(1, 0x69L).cast(ByteRef::new);
    this._6a = ref.offset(1, 0x6aL).cast(ByteRef::new);

    this.tmd_70 = ref.offset(4, 0x70L).cast(Pointer.deferred(4, TmdObjTable::new));

    this._7c = ref.offset(2, 0x7cL).cast(ShortRef::new);
    this._7e = ref.offset(2, 0x7eL).cast(ShortRef::new);
    this._80 = ref.offset(2, 0x80L).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
