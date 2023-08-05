package legend.game.combat.effects;

import legend.core.gte.SVECTOR;
import legend.core.gte.TmdObjTable1c;

import java.util.Arrays;

public class FrozenJetEffect28 implements Effect {
  public final SVECTOR[] vertices_0c;

  public final TmdObjTable1c.Primitive[] primitives_14;
  /** ushort */
  public final int _18;
  public short _1a;
  public final SVECTOR[] verticesCopy_1c;

  /** ubyte */
  public final int _24;

  public FrozenJetEffect28(final SVECTOR[] vertices, final TmdObjTable1c.Primitive[] primitives, final int _18, final int _24) {
    this.vertices_0c = vertices;
    this.primitives_14 = primitives;
    this._18 = _18;
    this._24 = _24;

    this.verticesCopy_1c = new SVECTOR[vertices.length];
    Arrays.setAll(this.verticesCopy_1c, i -> new SVECTOR().set(vertices[i]));
  }
}
