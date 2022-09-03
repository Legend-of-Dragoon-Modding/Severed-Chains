package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.ArrayRef;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;

public class DragoonSpells09 implements MemoryRef {
  private final Value ref;

  public final ByteRef charIndex_00;
  public final ArrayRef<ByteRef> spellIndex_01;

  public DragoonSpells09(final Value ref) {
    this.ref = ref;

    this.charIndex_00 = ref.offset(1, 0x00L).cast(ByteRef::new);
    this.spellIndex_01 = ref.offset(1, 0x01L).cast(ArrayRef.of(ByteRef.class, 8, 1, ByteRef::new));
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
