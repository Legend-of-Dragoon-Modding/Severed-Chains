package legend.game.combat.effects;

import legend.core.memory.types.QuintConsumer;
import legend.game.types.Translucency;
import org.joml.Vector2f;

public class RadialGradientEffect14 implements Effect {
  public int circleSubdivisionModifier_00;
  public float scaleModifier_01;

  public float z_04;
  public int angleStep_08;
  public int r_0c;
  public int g_0d;
  public int b_0e;

  public QuintConsumer<EffectManagerData6c<EffectManagerData6cInner.RadialGradientType>, Integer, Vector2f[], RadialGradientEffect14, Translucency> renderer_10;
}
