package legend.game.combat.types;

import legend.core.gte.SVECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedByteRef;
import legend.core.memory.types.UnsignedIntRef;
import legend.core.memory.types.UnsignedShortRef;

public class FrozenJetEffect28 implements BttlScriptData6cSubBase1, MemoryRef {
  private final Value ref;

  public final UnsignedIntRef vertexCount_00;
  public final UnsignedIntRef normalCount_04;
  public final UnsignedIntRef primitiveCount_08;
  public final Pointer<UnboundedArrayRef<SVECTOR>> vertices_0c;
  public final Pointer<UnboundedArrayRef<SVECTOR>> normals_10;
  public final UnsignedIntRef primitives_14; //TODO
  public final UnsignedShortRef _18;
  public final ShortRef _1a;
  public final Pointer<UnboundedArrayRef<SVECTOR>> verticesCopy_1c;
  public final Pointer<UnboundedArrayRef<SVECTOR>> normalsCopy_20;
  public final UnsignedByteRef _24;

  public FrozenJetEffect28(final Value ref) {
    this.ref = ref;

    this.vertexCount_00 = ref.offset(4, 0x00L).cast(UnsignedIntRef::new);
    this.normalCount_04 = ref.offset(4, 0x04L).cast(UnsignedIntRef::new);
    this.primitiveCount_08 = ref.offset(4, 0x08L).cast(UnsignedIntRef::new);
    this.vertices_0c = ref.offset(4, 0x0cL).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x8, SVECTOR::new)));
    this.normals_10 = ref.offset(4, 0x10L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x8, SVECTOR::new)));
    this.primitives_14 = ref.offset(4, 0x14L).cast(UnsignedIntRef::new);
    this._18 = ref.offset(2, 0x18L).cast(UnsignedShortRef::new);
    this._1a = ref.offset(2, 0x1aL).cast(ShortRef::new);
    this.verticesCopy_1c = ref.offset(4, 0x1cL).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x8, SVECTOR::new)));
    this.normalsCopy_20 = ref.offset(4, 0x20L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x8, SVECTOR::new)));
    this._24 = ref.offset(1, 0x24L).cast(UnsignedByteRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
