package legend.game.types;

import legend.core.gte.VECTOR;

/**
 * Parallel light source.<p>
 * Holds information about a parallel light source. Use GsSetFlatLight() to assign the values to one of three
 * light sources.<p>
 * The light source directional vectors are specified by vx, vy, vz. It is unnecessary for the programmer to
 * perform normalization, because the system does it. A polygon whose normal vectors are opposite to these
 * directional vectors is exposed to the strongest light.
 */
public class GsF_LIGHT {
  /** Directional vector for light source */
  public final VECTOR direction_00 = new VECTOR();
  /** Light source red */
  public int r_0c;
  /** Light source green */
  public int g_0d;
  /** Light source blue */
  public int b_0e;

  public GsF_LIGHT() { }

  public GsF_LIGHT(final int x, final int y, final int z) {
    this.direction_00.set(x, y, z);
  }

  public void clear() {
    this.direction_00.set(0, 0, 0);
    this.r_0c = 0;
    this.g_0d = 0;
    this.b_0e = 0;
  }
}
