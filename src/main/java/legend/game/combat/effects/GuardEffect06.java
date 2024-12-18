package legend.game.combat.effects;

import legend.core.MathHelper;
import legend.core.QueuedModelStandard;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.PolyBuilder;
import legend.game.scripting.ScriptState;
import legend.game.types.Translucency;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.Arrays;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static legend.game.combat.SEffe.transformWorldspaceToScreenspace;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;

public class GuardEffect06 implements Effect<EffectManagerParams.VoidType> {
  private static final GuardEffectMetrics04[] guardEffectMetrics_800fa76c = {
    new GuardEffectMetrics04(0, 0),
    new GuardEffectMetrics04(0, -1000),
    new GuardEffectMetrics04(600, -300),
    new GuardEffectMetrics04(500, 600),
    new GuardEffectMetrics04(0, 1000),
    new GuardEffectMetrics04(-500, -100),
    new GuardEffectMetrics04(0, -1000),
  };

  public int _00 = 1;
  public int _02;
  public short _04;

  private final MV transforms = new MV();

  @Override
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {

  }

  @Override
  @Method(0x800d2810L)
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;

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
    float managerZ = manager.params_10.z_22;
    final float totalZ = effectZ + managerZ;
    if(totalZ >= 0xa0) {
      if(totalZ >= 0xffe) {
        managerZ = 0xffe - effectZ;
      }

      final PolyBuilder builder = new PolyBuilder("Guard effect", GL_TRIANGLES)
        .translucency(Translucency.B_PLUS_F);

      for(int i = 0; i < 5; i++) {
        //LAB_800d2bc0
        // Main part of shield effect
        builder
          .addVertex(pos[i + 1].x, pos[i + 1].y, 0.0f)
          .rgb(manager.params_10.colour_1c.x / 255.0f, manager.params_10.colour_1c.y / 255.0f, manager.params_10.colour_1c.z / 255.0f)
          .addVertex(pos[i + 2].x, pos[i + 2].y, 0.0f)
          .addVertex(pos[    0].x, pos[    0].y, 0.0f)
          .monochrome(r / 255.0f);
      }

      final Obj obj = builder.build();
      this.transforms.transfer.set(GPU.getOffsetX(), GPU.getOffsetY(), effectZ + managerZ);
      RENDERER.queueOrthoModel(obj, this.transforms, QueuedModelStandard.class);
      obj.delete(); // Mark for deletion after this frame

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
          //LAB_800d2ee8
          // Radiant lines of shield effect
          RENDERER.queueLine(new Matrix4f(), effectZ + managerZ, pos[n + 1], pos[n + 2])
            .translucency(Translucency.B_PLUS_F)
            .colour(r / 255.0f, g / 255.0f, b / 255.0f)
            .screenspaceOffset(GPU.getOffsetX(), GPU.getOffsetY());
        }

        //LAB_800d2fa4
      }
    }
  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {

  }
}
