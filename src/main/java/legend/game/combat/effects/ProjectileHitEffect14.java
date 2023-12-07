package legend.game.combat.effects;

import legend.core.gpu.GpuCommandLine;
import legend.core.memory.Method;
import legend.game.scripting.ScriptState;
import legend.game.types.Translucency;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Arrays;

import static legend.core.GameEngine.GPU;
import static legend.game.combat.Bttl_800c.FUN_800cfb14;

public class ProjectileHitEffect14 implements Effect {
  public final int count_00;

  public final ProjectileHitEffect14Sub48[] _08;

  public ProjectileHitEffect14(final int count) {
    this.count_00 = count;

    this._08 = new ProjectileHitEffect14Sub48[count];
    Arrays.setAll(this._08, i -> new ProjectileHitEffect14Sub48());
  }

  @Method(0x800d019cL)
  public void renderProjectileHitEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> data) {
    float a0 = 0.0f;
    final ProjectileHitEffect14 effect = (ProjectileHitEffect14)data.effect_44;

    //LAB_800d01ec
    for(int s7 = 0; s7 < effect.count_00; s7++) {
      final ProjectileHitEffect14Sub48 s4 = effect._08[s7];

      if(s4.used_00) {
        s4._40++;
        s4.frames_44--;

        if(s4.frames_44 == 0) {
          s4.used_00 = false;
        }

        //LAB_800d0220
        s4.r_34 -= s4.fadeR_3a;
        s4.g_36 -= s4.fadeG_3c;
        s4.b_38 -= s4.fadeB_3e;

        //LAB_800d0254
        final Vector2f[] screenVert = {new Vector2f(), new Vector2f()};
        for(int s3 = 0; s3 < 2; s3++) {
          final Vector3f s1 = s4._04[s3];
          final Vector3f a1 = s4._24[s3];
          a0 = FUN_800cfb14(data, s1, screenVert[s3]);
          s1.add(a1);
          a1.y += 25.0f;

          if(a1.x > 10.0f) {
            a1.x -= 10.0f;
          }

          //LAB_800d0308
          if(s1.y + data.params_10.trans_04.y >= 0) {
            s1.y = -data.params_10.trans_04.y;
            a1.y = -a1.y;
          }

          //LAB_800d033c
        }

        float s1_0 = a0 / 4.0f;
        if(s1_0 >= 0x140) {
          if(s1_0 >= 0xffe) {
            s1_0 = 0xffe;
          }

          //LAB_800d037c
          float a2_0 = data.params_10.z_22;
          final float v1 = s1_0 + a2_0;
          if(v1 >= 0xa0) {
            if(v1 >= 0xffe) {
              a2_0 = 0xffe - s1_0;
            }

            //LAB_800d0444
            GPU.queueCommand((s1_0 + a2_0) / 4.0f, new GpuCommandLine()
              .translucent(Translucency.B_PLUS_F)
              .monochrome(0, 0)
              .rgb(1, s4.r_34 >>> 8, s4.g_36 >>> 8, s4.b_38 >>> 8)
              .pos(0, screenVert[0].x, screenVert[0].y)
              .pos(1, screenVert[1].x, screenVert[1].y)
            );
          }

          //LAB_800d0460
        }
      }
    }

    //LAB_800d0508
  }
}
