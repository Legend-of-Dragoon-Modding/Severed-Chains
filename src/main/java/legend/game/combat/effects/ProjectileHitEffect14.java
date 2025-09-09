package legend.game.combat.effects;

import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.PolyBuilder;
import legend.game.scripting.ScriptState;
import legend.game.types.Translucency;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static legend.game.combat.Battle.seed_800fa754;
import static legend.game.combat.SEffe.FUN_800cfb14;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLE_STRIP;

public class ProjectileHitEffect14 implements Effect<EffectManagerParams.VoidType> {
  private final int count_00;

  private final ProjectileHitEffectInstance48[] instances;

  private Obj obj;
  private final Matrix4f transforms = new Matrix4f();

  public ProjectileHitEffect14(final int count, final int r, final int g, final int b) {
    this.count_00 = count;

    this.instances = new ProjectileHitEffectInstance48[count];

    //LAB_800d0634
    for(int i = 0; i < count; i++) {
      final ProjectileHitEffectInstance48 inst = new ProjectileHitEffectInstance48();
      this.instances[i] = inst;

      inst.r_34 = r << 8;
      inst.g_36 = g << 8;
      inst.b_38 = b << 8;

      final short x = (short)(seed_800fa754.nextInt(301) + 200);
      final short y = (short)(seed_800fa754.nextInt(401) - 300);
      final short z = (short)(seed_800fa754.nextInt(601) - 300);
      inst._24[0].set(x, y, z);
      inst._24[1].set(x, y, z);

      inst._04[0].x = 0.0f;
      inst._04[0].y = seed_800fa754.nextInt(101) - 50;
      inst._04[0].z = seed_800fa754.nextInt(101) - 50;
      inst.frames_44 = seed_800fa754.nextInt(9) + 7;

      inst._24[1].y += 25.0f;
      inst._04[1].set(inst._04[0]).add(inst._24[0]);
      inst.fadeR_3a = inst.r_34 / inst.frames_44;
      inst.fadeG_3c = inst.g_36 / inst.frames_44;
      inst.fadeB_3e = inst.b_38 / inst.frames_44;
    }
  }

  @Override
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {

  }

  @Override
  @Method(0x800d019cL)
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;

    if(this.obj == null) {
      this.obj = new PolyBuilder("Projectile hit effect", GL_TRIANGLE_STRIP)
        .addVertex(0.0f, 0.0f, 0.0f)
        .monochrome(0.0f)
        .addVertex(1.0f, 0.0f, 0.0f)
        .addVertex(0.0f, 1.0f, 0.0f)
        .monochrome(1.0f)
        .addVertex(1.0f, 1.0f, 0.0f)
        .build();
    }

    float a0 = 0.0f;

    //LAB_800d01ec
    for(int i = 0; i < this.count_00; i++) {
      final ProjectileHitEffectInstance48 s4 = this.instances[i];

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
          a0 = FUN_800cfb14(manager, s1, screenVert[s3]);
          s1.add(a1);
          a1.y += 25.0f;

          if(a1.x > 10.0f) {
            a1.x -= 10.0f;
          }

          //LAB_800d0308
          if(s1.y + manager.params_10.trans_04.y >= 0) {
            s1.y = -manager.params_10.trans_04.y;
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
          float a2_0 = manager.params_10.z_22;
          final float v1 = s1_0 + a2_0;
          if(v1 >= 0xa0) {
            if(v1 >= 0xffe) {
              a2_0 = 0xffe - s1_0;
            }

            //LAB_800d0444
            RENDERER.queueLine(this.obj, this.transforms, s1_0 + a2_0, screenVert[0], screenVert[1])
              .translucency(Translucency.B_PLUS_F)
              .colour((s4.r_34 >>> 8) / 255.0f, (s4.g_36 >>> 8) / 255.0f, (s4.b_38 >>> 8) / 255.0f)
              .screenspaceOffset(GPU.getOffsetX(), GPU.getOffsetY());
          }

          //LAB_800d0460
        }
      }
    }

    //LAB_800d0508
  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    if(this.obj != null) {
      this.obj.delete();
      this.obj = null;
    }
  }
}
