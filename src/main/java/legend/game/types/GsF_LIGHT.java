package legend.game.types;

import legend.core.gte.VECTOR;
import legend.core.memory.Value;
import legend.core.memory.types.MemoryRef;
import legend.core.memory.types.UnsignedByteRef;

import javax.annotation.Nullable;

/**
 * Parallel light source.<p>
 * Holds information about a parallel light source. Use GsSetFlatLight() to assign the values to one of three
 * light sources.<p>
 * The light source directional vectors are specified by vx, vy, vz. It is unnecessary for the programmer to
 * perform normalization, because the system does it. A polygon whose normal vectors are opposite to these
 * directional vectors is exposed to the strongest light.
 */
public class GsF_LIGHT implements MemoryRef {
  @Nullable
  private final Value ref;

  /** Directional vector for light source */
  public final VECTOR direction_00;
  /** Light source red */
  public final UnsignedByteRef r_0c;
  /** Light source green */
  public final UnsignedByteRef g_0d;
  /** Light source blue */
  public final UnsignedByteRef b_0e;

  public GsF_LIGHT(final Value ref) {
    this.ref = ref;

    this.direction_00 = ref.offset(4, 0x0L).cast(VECTOR::new);
    this.r_0c = ref.offset(1, 0xcL).cast(UnsignedByteRef::new);
    this.g_0d = ref.offset(1, 0xdL).cast(UnsignedByteRef::new);
    this.b_0e = ref.offset(1, 0xeL).cast(UnsignedByteRef::new);
  }

  public GsF_LIGHT() {
    this.ref = null;

    this.direction_00 = new VECTOR();
    this.r_0c = new UnsignedByteRef();
    this.g_0d = new UnsignedByteRef();
    this.b_0e = new UnsignedByteRef();
  }

  public void clear() {
    this.direction_00.set(0, 0, 0);
    this.r_0c.set(0);
    this.g_0d.set(0);
    this.b_0e.set(0);
  }

  @Override
  public long getAddress() {
    return this.ref != null ? this.ref.getAddress() : 0;
  }
}
