package legend.game.combat.effects;

import legend.core.MathHelper;
import legend.core.gpu.GpuCommandLine;
import legend.core.gpu.GpuCommandPoly;
import legend.core.memory.Method;
import legend.game.scripting.ScriptState;
import legend.game.types.Translucency;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Arrays;

import static legend.core.GameEngine.GPU;
import static legend.game.combat.Bttl_800c.guardEffectMetrics_800fa76c;
import static legend.game.combat.Bttl_800c.transformWorldspaceToScreenspace;

public class GuardEffect06 implements Effect {
  public int _00;
  public int _02;
  public short _04;

  @Method(0x800d2810L)
  public void renderGuardEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state, final EffectManagerData6c<EffectManagerParams.VoidType> manager) {
    final Vector3f translation = new Vector3f();
    final Vector2f[] pos = new Vector2f[7];

    Arrays.setAll(pos, i -> new Vector2f());

    this._02++;
    this._04 += 0x400;

    //LAB_800d2888
    GuardEffectMetrics04 guardEffectMetrics;
    float effectZ = 0;
    for(int i = 6; i >= 0; i--) {
      //LAB_800d289c
      guardEffectMetrics = guardEffectMetrics_800fa76c[i];
      translation.x = manager.params_10.trans_04.x + (i != 0 ? manager.params_10.scale_16.x * 0x1000 / 4 : 0);
      translation.y = manager.params_10.trans_04.y + guardEffectMetrics.y_02 * manager.params_10.scale_16.y;
      translation.z = manager.params_10.trans_04.z + guardEffectMetrics.z_00 * manager.params_10.scale_16.z;
      effectZ = transformWorldspaceToScreenspace(translation, pos[i]);
    }

    effectZ /= 4.0f;
    int r = MathHelper.clamp(manager.params_10.colour_1c.x - 1 << 8, 0, 0x8000) >>> 7;
    int g = MathHelper.clamp(manager.params_10.colour_1c.y - 1 << 8, 0, 0x8000) >>> 7;
    int b = MathHelper.clamp(manager.params_10.colour_1c.z - 1 << 8, 0, 0x8000) >>> 7;
    r = Math.min((r + g + b) / 3 * 2, 0xff);

    //LAB_800d2a80
    //LAB_800d2a9c
    for(int i = 0; i < 5; i++) {
      float managerZ = manager.params_10.z_22;
      final float totalZ = effectZ + managerZ;
      if(totalZ >= 0xa0) {
        if(totalZ >= 0xffe) {
          managerZ = 0xffe - effectZ;
        }

        //LAB_800d2bc0
        // Main part of shield effect
        GPU.queueCommand((effectZ + managerZ) / 4.0f, new GpuCommandPoly(3)
          .translucent(Translucency.B_PLUS_F)
          .pos(0, pos[i + 1].x, pos[i + 1].y)
          .pos(1, pos[i + 2].x, pos[i + 2].y)
          .pos(2, pos[0    ].x, pos[0    ].y)
          .rgb(0, manager.params_10.colour_1c)
          .rgb(1, manager.params_10.colour_1c)
          .monochrome(2, r)
        );
      }
    }

    //LAB_800d2c78
    int s6 = 0x1000;
    r = manager.params_10.colour_1c.x;
    g = manager.params_10.colour_1c.y;
    b = manager.params_10.colour_1c.z;
    final int stepR = r >>> 2;
    final int stepG = g >>> 2;
    final int stepB = b >>> 2;

    //LAB_800d2cfc
    int baseX = 0;
    for(int i = 0; i < 4; i++) {
      s6 = s6 + this._04 / 4;
      baseX = (int)(baseX + manager.params_10.scale_16.x * 0x1000 / 4);
      r = r - stepR;
      g = g - stepG;
      b = b - stepB;

      //LAB_800d2d4c
      for(int n = 1; n < 7; n++) {
        guardEffectMetrics = guardEffectMetrics_800fa76c[n];
        translation.x = baseX + manager.params_10.trans_04.x;
        translation.y = guardEffectMetrics.y_02 * manager.params_10.scale_16.y * s6 / 0x1000 + manager.params_10.trans_04.y;
        translation.z = guardEffectMetrics.z_00 * manager.params_10.scale_16.z * s6 / 0x1000 + manager.params_10.trans_04.z;
        effectZ = transformWorldspaceToScreenspace(translation, pos[n]) / 4.0f;
      }

      //LAB_800d2e20
      for(int n = 0; n < 5; n++) {
        float managerZ = manager.params_10.z_22;
        final float totalZ = effectZ + managerZ;
        if(totalZ >= 0xa0) {
          if(totalZ >= 0xffe) {
            managerZ = 0xffe - effectZ;
          }

          //LAB_800d2ee8
          // Radiant lines of shield effect
          GPU.queueCommand((effectZ + managerZ) / 4.0f, new GpuCommandLine()
            .translucent(Translucency.B_PLUS_F)
            .pos(0, pos[n + 1].x, pos[n + 1].y)
            .pos(1, pos[n + 2].x, pos[n + 2].y)
            .rgb(r, g, b)
          );
        }
      }

      //LAB_800d2fa4
    }
  }
}
