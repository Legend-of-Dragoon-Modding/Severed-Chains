package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.UnboundedArrayRef;
import legend.core.memory.types.UnsignedIntRef;

public class AnmStruct implements MemoryRef {
  private final Value ref;

  public final Pointer<AnmFile> anm_00;
  public final Pointer<UnboundedArrayRef<AnmSpriteGroup>> spriteGroup_04;
  public final UnsignedIntRef _08;
  public final UnsignedIntRef _0c;

  public AnmStruct(final Value ref) {
    this.ref = ref;

    this.anm_00 = ref.offset(4, 0x0L).cast(Pointer.deferred(4, AnmFile::new));
    this.spriteGroup_04 = ref.offset(4, 0x4L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x18, AnmSpriteGroup::new)));
    this._08 = ref.offset(4, 0x8L).cast(UnsignedIntRef::new);
    this._0c = ref.offset(4, 0xcL).cast(UnsignedIntRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
