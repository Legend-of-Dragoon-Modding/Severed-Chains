package legend.game.combat.types;

import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.IntRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;
import legend.game.types.Model124;

public class WeaponTrailEffect3c extends BttlScriptData6cSubBase1 {
  public final IntRef _00;
  public final UnsignedIntRef _04;
  public final ShortRef dobjIndex_08;
  public final UnsignedShortRef smallestVertexIndex_0a;
  public final UnsignedShortRef largestVertexIndex_0c;
  public final UnsignedByteRef _0e;
  public final VECTOR smallestVertex_10;
  public final VECTOR largestVertex_20;
  public final Pointer<Model124> parentModel_30;
  public final Pointer<ArrayRef<WeaponTrailEffectSegment2c>> segments_34;
  public final Pointer<WeaponTrailEffectSegment2c> _38;

  public WeaponTrailEffect3c(final Value ref) {
    super(ref);

    this._00 = ref.offset(4, 0x00L).cast(IntRef::new);
    this._04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this.dobjIndex_08 = ref.offset(2, 0x08L).cast(ShortRef::new);
    this.smallestVertexIndex_0a = ref.offset(2, 0x0aL).cast(UnsignedShortRef::new);
    this.largestVertexIndex_0c = ref.offset(2, 0x0cL).cast(UnsignedShortRef::new);
    this._0e = ref.offset(1, 0x0eL).cast(UnsignedByteRef::new);
    this.smallestVertex_10 = ref.offset(4, 0x10L).cast(VECTOR::new);
    this.largestVertex_20 = ref.offset(4, 0x20L).cast(VECTOR::new);
    this.parentModel_30 = ref.offset(4, 0x30L).cast(Pointer.deferred(4, Model124::new));
    this.segments_34 = ref.offset(4, 0x34L).cast(Pointer.deferred(4, ArrayRef.of(WeaponTrailEffectSegment2c.class, 65, 0x2c, WeaponTrailEffectSegment2c::new)));
    this._38 = ref.offset(4, 0x38L).cast(Pointer.deferred(4, WeaponTrailEffectSegment2c::new));
  }
}
