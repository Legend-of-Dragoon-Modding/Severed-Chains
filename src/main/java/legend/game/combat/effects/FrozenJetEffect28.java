package legend.game.combat.effects;

import legend.core.gte.TmdObjTable1c;
import legend.core.memory.Method;
import legend.game.scripting.ScriptState;
import org.joml.Vector3f;

import java.util.Arrays;

import static legend.game.Scus94491BpeSegment.rsin;

public class FrozenJetEffect28 implements Effect<EffectManagerParams.FrozenJetType> {
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

  /**
   * Code deleted from LAB_8010a130. Condition variable was set from script, and value
   * was hardcoded to 0 so that the code in the condition was never run.
   */
  @Override
  @Method(0x80109fc4L)
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.FrozenJetType>> state) {
    final EffectManagerData6c<EffectManagerParams.FrozenJetType> manager = state.innerStruct_00;
    int s1 = this._18;

    //LAB_8010a020
    outer:
    while(true) {
      int s4 = this._1a + (s1 << 11);
      s1++;

      //LAB_8010a04c
      for(int s2 = 1; s2 < this._18 - 1; s2++) {
        if(s1 >= this.vertices_0c.length - this._18) {
          break outer;
        }

        final Vector3f sp0x10 = new Vector3f(this.verticesCopy_1c[s1]);
        sp0x10.y += rsin(s4) * (manager.params_10._28 << 10 >> 8) >> 12;
        this.vertices_0c[s1].set(sp0x10);
        s4 += 0x1000 / this._18 / manager.params_10._2c >> 8;
        s1++;
      }

      //LAB_8010a124
      s1++;
    }

    //LAB_8010a130
    //LAB_8010a15c
    //LAB_8010a374
    this._1a += (short)(manager.params_10._24 << 7 >> 8);
  }

  @Override
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.FrozenJetType>> state) {

  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.FrozenJetType>> state) {

  }
}
