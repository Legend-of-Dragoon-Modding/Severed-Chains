package legend.game.combat.effects;

import legend.core.gte.SVECTOR;
import legend.core.gte.TmdObjTable1c;
import legend.game.combat.effects.BttlScriptData6cSubBase1;

import java.util.Arrays;

public class FrozenJetEffect28 implements BttlScriptData6cSubBase1 {
//  public final int vertexCount_00;
//  public final int normalCount_04;
//  public final int primitiveCount_08;
  public final SVECTOR[] vertices_0c;
  public final SVECTOR[] normals_10;
  public final TmdObjTable1c.Primitive[] primitives_14;
  /** ushort */
  public final int _18;
  public short _1a;
  public final SVECTOR[] verticesCopy_1c;
  public final SVECTOR[] normalsCopy_20;
  /** ubyte */
  public final int _24;

  public FrozenJetEffect28(final SVECTOR[] vertices, final SVECTOR[] normals, final TmdObjTable1c.Primitive[] primitives, final int _18, final int _24) {
    this.vertices_0c = vertices;
    this.normals_10 = normals;
    this.primitives_14 = primitives;
    this._18 = _18;
    this._24 = _24;

    this.verticesCopy_1c = new SVECTOR[vertices.length];
    this.normalsCopy_20 = new SVECTOR[normals.length];
    Arrays.setAll(this.verticesCopy_1c, i -> new SVECTOR().set(vertices[i]));
    Arrays.setAll(this.normals_10, i -> new SVECTOR().set(normals[i]));
  }
}
