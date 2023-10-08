package legend.game.combat.effects;

import legend.core.gte.TmdObjTable1c;
import org.joml.Vector3f;

import java.util.Arrays;

public class FrozenJetEffect28 implements Effect {
  public final Vector3f[] vertices_0c;

  public final TmdObjTable1c.Primitive[] primitives_14;
  /** ushort */
  public final int _18;
  public short _1a;
  public final Vector3f[] verticesCopy_1c;

  /** ubyte */
  public final int _24;

  public FrozenJetEffect28(final Vector3f[] vertices, final TmdObjTable1c.Primitive[] primitives, final int _18, final int _24) {
    this.vertices_0c = vertices;
    this.primitives_14 = primitives;
    this._18 = _18;
    this._24 = _24;

    this.verticesCopy_1c = new Vector3f[vertices.length];
    Arrays.setAll(this.verticesCopy_1c, i -> new Vector3f(vertices[i]));
  }
}
