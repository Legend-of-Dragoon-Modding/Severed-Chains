package legend.game.types;

import legend.core.memory.Value;
import legend.core.memory.types.EnumRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.Pointer;
import legend.game.inventory.screens.TextColour;

public class MenuStatus08 implements MemoryRef {
  private final Value ref;

  /** Poison, etc */
  public final Pointer<LodString> text_00;
  public final EnumRef<TextColour> colour_04;

  public MenuStatus08(final Value ref) {
    this.ref = ref;

    this.text_00 = ref.offset(4, 0x0L).cast(Pointer.deferred(4, LodString::new));
    this.colour_04 = ref.offset(1, 0x4L).cast(EnumRef.of(TextColour.values()));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
