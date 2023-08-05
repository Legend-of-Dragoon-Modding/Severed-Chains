package legend.game.combat.effects;

import legend.core.gte.COLOUR;
import legend.core.gte.TmdObjTable1c;
import legend.core.gte.VECTOR;
import org.joml.Vector3f;

public class StarChildrenImpactEffectInstancea8 {
  public final int index;

  public boolean renderImpact_00;
  public boolean renderShockwave_01;

  /** Frame of effect to start rendering on */
  public int startingFrame_04;
  public int unused_08;
  // Explosion vectors have two stages: the burst and the plume
  public final VECTOR[] translation_0c = {new VECTOR(), new VECTOR()};
  public final Vector3f[] rotation_2c = {new Vector3f(), new Vector3f()};

  public final Vector3f[] scale_6c = {new Vector3f(), new Vector3f()};
  public final COLOUR[] opacity_8c = {new COLOUR(), new COLOUR()};
  public TmdObjTable1c explosionObjTable_94;
  public TmdObjTable1c shockwaveObjTable_98;
  public TmdObjTable1c plumeObjTable_9c;
  public float explosionHeightAngle_a0;
  /** Starts ticking once rendering begins for an instance. */
  public short animationFrame_a2;

  public StarChildrenImpactEffectInstancea8(final int index) {
    this.index = index;
  }
}
