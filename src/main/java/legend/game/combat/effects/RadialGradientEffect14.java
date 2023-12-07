package legend.game.combat.effects;

import legend.core.memory.Method;
import legend.core.memory.types.QuintConsumer;
import legend.game.scripting.ScriptState;
import legend.game.types.Translucency;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.combat.Bttl_800c.FUN_800cfb14;

public class RadialGradientEffect14 implements Effect {
  public int circleSubdivisionModifier_00;
  public float scaleModifier_01;

  public float z_04;
  public int angleStep_08;
  public int r_0c;
  public int g_0d;
  public int b_0e;

  public QuintConsumer<EffectManagerData6c<EffectManagerParams.RadialGradientType>, Integer, Vector2f[], RadialGradientEffect14, Translucency> renderer_10;

  @Method(0x800d247cL)
  public void renderRadialGradientEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.RadialGradientType>> state, final EffectManagerData6c<EffectManagerParams.RadialGradientType> manager) {
    this.angleStep_08 = 0x1000 / (0x4 << this.circleSubdivisionModifier_00);

    final Vector2f screenVert0 = new Vector2f();
    this.z_04 = FUN_800cfb14(manager, new Vector3f(), screenVert0) / 4.0f;

    final float z = this.z_04 + manager.params_10.z_22;
    if(z >= 0xa0) {
      if(z >= 0xffe) {
        this.z_04 = 0xffe - manager.params_10.z_22;
      }

      //LAB_800d2510
      //TODO these are .12, why does this not have to be scaled down? Why is scale so small?
      final Vector3f sp0x38 = new Vector3f().set(
        rcos(0) * (manager.params_10.scale_16.x / this.scaleModifier_01),
        rsin(0) * (manager.params_10.scale_16.y / this.scaleModifier_01),
        0
      );

      final Vector2f screenVert2 = new Vector2f();
      FUN_800cfb14(manager, sp0x38, screenVert2);
      this.r_0c = manager.params_10.colour_24 >>> 16 & 0xff;
      this.g_0d = manager.params_10.colour_24 >>>  8 & 0xff;
      this.b_0e = manager.params_10.colour_24 & 0xff;

      //LAB_800d25b4
      for(int angle = 0; angle < 0x1000; ) {
        final Vector2f screenVert1 = new Vector2f(screenVert2);

        sp0x38.set(
          rcos(angle + this.angleStep_08) * (manager.params_10.scale_16.x / this.scaleModifier_01),
          rsin(angle + this.angleStep_08) * (manager.params_10.scale_16.y / this.scaleModifier_01),
          0
        );

        FUN_800cfb14(manager, sp0x38, screenVert2);
        this.renderer_10.accept(manager, angle, new Vector2f[] {screenVert0, screenVert1, screenVert2}, this, (manager.params_10.flags_00 & 0x1000_0000) != 0 ? Translucency.B_PLUS_F : Translucency.B_MINUS_F);
        angle += this.angleStep_08;
      }
    }

    //LAB_800d2710
  }
}
