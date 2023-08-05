package legend.game.combat.effects;

import legend.core.memory.types.QuintConsumer;
import legend.game.types.Translucency;

public class RadialGradientEffect14 implements Effect {
  public int circleSubdivisionModifier_00;
  public float scaleModifier_01;

  public int z_04;
  public int angleStep_08;
  public int r_0c;
  public int g_0d;
  public int b_0e;

  public QuintConsumer<EffectManagerData6c<EffectManagerData6cInner.RadialGradientType>, Integer, short[], RadialGradientEffect14, Translucency> renderer_10;
}
