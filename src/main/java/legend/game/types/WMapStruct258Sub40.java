package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;

public class WMapStruct258Sub40 implements MemoryRef {
  private final Value ref;

  public final Pointer<ExtendedTmd> extendedTmd_00;

  public final Pointer<TmdAnimationFile> tmdAnim_08;

  public WMapStruct258Sub40(final Value ref) {
    this.ref = ref;

    this.extendedTmd_00 = ref.offset(4, 0x00L).cast(Pointer.deferred(4, ExtendedTmd::new));

    this.tmdAnim_08 = ref.offset(4, 0x08L).cast(Pointer.deferred(4, TmdAnimationFile::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
