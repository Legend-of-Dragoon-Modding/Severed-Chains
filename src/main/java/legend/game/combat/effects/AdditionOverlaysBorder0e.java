package legend.game.combat.effects;

import legend.core.memory.Value;
import legend.core.memory.types.ByteRef;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.ShortRef;
import legend.core.memory.types.UnsignedByteRef;

public class AdditionOverlaysBorder0e implements MemoryRef {
  private final Value ref;

  public final ByteRef isVisible_00;

  public final ShortRef angleModifier_02; // 0x200 is unrotated square
  public final UnsignedByteRef r_04;
  public final UnsignedByteRef g_05;
  public final UnsignedByteRef b_06;

  public final ShortRef size_08;
  public final ShortRef ticksUntilRender_0a; // Counts from start of addition, not hit
  public final ByteRef countTicksVisible_0c;
  public final ByteRef sideEffects_0d; // -1 = no translucency, 0 = no translucency + shadow, 1+ = no side effect

  public AdditionOverlaysBorder0e(final Value ref) {
    this.ref = ref;

    this.isVisible_00 = ref.offset(1, 0x00L).cast(ByteRef::new);

    this.angleModifier_02 = ref.offset(2, 0x02L).cast(ShortRef::new);
    this.r_04 = ref.offset(1, 0x04L).cast(UnsignedByteRef::new);
    this.g_05 = ref.offset(1, 0x05L).cast(UnsignedByteRef::new);
    this.b_06 = ref.offset(1, 0x06L).cast(UnsignedByteRef::new);

    this.size_08 = ref.offset(2, 0x08L).cast(ShortRef::new);
    this.ticksUntilRender_0a = ref.offset(2, 0x0aL).cast(ShortRef::new);
    this.countTicksVisible_0c = ref.offset(1, 0x0cL).cast(ByteRef::new);
    this.sideEffects_0d = ref.offset(1, 0x0dL).cast(ByteRef::new);
  }

  @Override
  public long getAddress() { return this.ref.getAddress(); }
}
