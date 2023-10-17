package legend.game.submap;

import org.joml.Vector2f;
import org.joml.Vector3f;

/** Cached values related to submap objects */
public class SubmapCaches80 {
  public final Vector3f playerPos_00 = new Vector3f();
  public final Vector3f playerMovement_0c = new Vector3f();

  /** Screen coords of a sobj (X + 20) */
  public final Vector2f bottomRight_60 = new Vector2f();
  /** Screen coords of a sobj (X - 20) */
  public final Vector2f bottomLeft_68 = new Vector2f();
  /** Screen coords of a sobj (origin) */
  public final Vector2f bottomMiddle_70 = new Vector2f();
  /** Screen coords of a sobj (y - 130) */
  public final Vector2f topMiddle_78 = new Vector2f();
}
