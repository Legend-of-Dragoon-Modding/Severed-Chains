package legend.game.types;

import org.joml.Vector3f;

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
  public final Vector3f direction_00 = new Vector3f();
  /** Light source red */
  public float r_0c;
  /** Light source green */
  public float g_0d;
  /** Light source blue */
  public float b_0e;

  public GsF_LIGHT() { }

  public GsF_LIGHT(final float x, final float y, final float z) {
    this.direction_00.set(x, y, z);
  }

  public void clear() {
    this.direction_00.zero();
    this.r_0c = 0.0f;
    this.g_0d = 0.0f;
    this.b_0e = 0.0f;
  }
}
