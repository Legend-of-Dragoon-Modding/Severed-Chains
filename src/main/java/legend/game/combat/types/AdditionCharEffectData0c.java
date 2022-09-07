package legend.game.combat.types;

import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;

public class AdditionCharEffectData0c implements MemoryRef {
  private final Value ref;

  public final UnsignedByteRef scrolling_00; /**1 when character is scrolling, 0 when set**/

  public final ShortRef dupes_02; /**Number of dupe characters to render to create motion blur effect**/
  public final ShortRef position_04; /**Character print timing, stored as a negative int**/
  public final ShortRef offsetY_06; /**Offset from base y-coordinate**/
  public final ShortRef offsetX_08; /**Offset from base x-coordinate**/
  public final ShortRef offsetY_0a; /**Offset from base y-coordinate (seems to be dupe)**/

  public AdditionCharEffectData0c(final Value ref) {
    this.ref = ref;

    this.scrolling_00 = ref.offset(1, 0x00L).cast(UnsignedByteRef::new);

    this.dupes_02 = ref.offset(2, 0x02L).cast(ShortRef::new);
    this.position_04 = ref.offset(2, 0x04L).cast(ShortRef::new);
    this.offsetY_06 = ref.offset(2, 0x06L).cast(ShortRef::new);
    this.offsetX_08 = ref.offset(2, 0x08L).cast(ShortRef::new);
    this.offsetY_0a = ref.offset(2, 0x0aL).cast(ShortRef::new);
  }

  @Override
  public long getAddress() {
    return this.ref.getAddress();
  }
}
