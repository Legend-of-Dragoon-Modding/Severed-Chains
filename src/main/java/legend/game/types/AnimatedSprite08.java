package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.core.memory.types.RelativePointer;
import legend.core.memory.types.UnboundedArrayRef;

public class AnimatedSprite08 implements MemoryRef {
  private final Value ref;

  public final Pointer<AnmFile> anm_00;
  public final Pointer<UnboundedArrayRef<RelativePointer<AnmSpriteGroup>>> spriteGroup_04;

  public AnimatedSprite08(final Value ref) {
    this.ref = ref;

    this.anm_00 = ref.offset(4, 0x0L).cast(Pointer.deferred(4, AnmFile::new));
    this.spriteGroup_04 = ref.offset(4, 0x4L).cast(Pointer.deferred(4, UnboundedArrayRef.of(0x18, RelativePointer.deferred(4, AnmSpriteGroup::new))));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
