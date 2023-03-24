package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;

public class UiFile implements MemoryRef {
  private final Value ref;

  public final UiType uiElements_0000;
  public final UiType itemIcons_c6a4;
  public final UiType portraits_cfac;
  public final UiType _d2d8;

  public UiFile(final Value ref) {
    this.ref = ref;

    this.uiElements_0000 = ref.offset(4, 0x0000L).cast(UiType::new);
    this.itemIcons_c6a4 = ref.offset(4, 0xc6a4L).cast(UiType::new);
    this.portraits_cfac = ref.offset(4, 0xcfacL).cast(UiType::new);
    this._d2d8 = ref.offset(4, 0xd2d8L).cast(UiType::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
