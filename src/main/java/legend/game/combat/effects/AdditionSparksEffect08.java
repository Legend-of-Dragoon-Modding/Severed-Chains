package legend.game.combat.effects;

import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.PolyBuilder;
import legend.game.scripting.ScriptState;
import legend.game.types.Translucency;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static legend.game.combat.Battle.seed_800fa754;
import static legend.game.combat.SEffe.getRelativeOffset;
import static legend.game.combat.SEffe.rotateAndTranslateEffect;
import static legend.game.combat.SEffe.transformWorldspaceToScreenspace;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLE_STRIP;

public class AdditionSparksEffect08 implements Effect<EffectManagerParams.VoidType> {
  /** ubyte */
  private final int count_00;
  private final AdditionSparksEffectInstance4c[] instances_04;

  private Obj spark;
  private final Matrix4f transforms = new Matrix4f();

  public AdditionSparksEffect08(final int count, final int distance, final int ticks, final int r, final int g, final int b) {
    this.count_00 = count;
    this.instances_04 = new AdditionSparksEffectInstance4c[count];

    final int step = distance / ticks;

    //LAB_800d0ee0
    for(int i = 0; i < count; i++) {
      final AdditionSparksEffectInstance4c inst = new AdditionSparksEffectInstance4c();
      this.instances_04[i] = inst;

      inst.delay_04 = (byte)(seed_800fa754.nextInt(ticks + 1));
      inst.ticksRemaining_05 = (byte)(seed_800fa754.nextInt(9) + 7);

      inst.startPos_08.set(inst.delay_04 * step, 0, 0);
      inst.endPos_18.set(0, 0, 0);
      inst.speed_28.set(seed_800fa754.nextInt(201), seed_800fa754.nextInt(201) - 100, seed_800fa754.nextInt(201) - 100);
      inst.acceleration_38.set(0.0f, 15.0f, 0.0f);

      inst.r_40 = r << 8;
      inst.g_42 = g << 8;
      inst.b_44 = b << 8;

      inst.stepR_46 = inst.r_40 / inst.ticksRemaining_05;
      inst.stepG_48 = inst.g_42 / inst.ticksRemaining_05;
      inst.stepB_4a = inst.b_44 / inst.ticksRemaining_05;
    }
  }

  @Method(0x800d09c0L)
  private void FUN_800d09c0(final EffectManagerData6c<EffectManagerParams.VoidType> a0, final AdditionSparksEffectInstance4c inst) {
    getRelativeOffset(a0, null, inst.startPos_08, inst.startPos_08);
    rotateAndTranslateEffect(a0, null, inst.speed_28, inst.speed_28);
    inst.endPos_18.set(inst.startPos_08);
  }

  @Override
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {

  }

  @Override
  @Method(0x800d0a30L)
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;

    if(this.spark == null) {
      this.spark = new PolyBuilder("Addition spark", GL_TRIANGLE_STRIP)
        .addVertex(0.0f, 0.0f, 0.0f)
        .monochrome(1.0f)
        .addVertex(1.0f, 0.0f, 0.0f)
        .addVertex(0.0f, 1.0f, 0.0f)
        .monochrome(0.5f)
        .addVertex(1.0f, 1.0f, 0.0f)
        .build();
    }

    //LAB_800d0a7c
    float s7 = 0;
    for(int i = 0; i < this.count_00; i++) {
      final AdditionSparksEffectInstance4c inst = this.instances_04[i];

      if(inst.delay_04 != 0) {
        inst.delay_04--;
        //LAB_800d0a94
      } else if(inst.ticksRemaining_05 != 0) {
        if(inst.ticksExisted_00 == 0) {
          this.FUN_800d09c0(manager, inst);
        }

        //LAB_800d0ac8
        inst.ticksExisted_00++;
        inst.ticksRemaining_05--;
        inst.startPos_08.add(inst.speed_28);
        final Vector2f start = new Vector2f();
        final Vector2f end = new Vector2f();
        final float instZ = transformWorldspaceToScreenspace(inst.startPos_08, start);
        transformWorldspaceToScreenspace(inst.endPos_18, end);

        if(i == 0) {
          s7 = instZ / 4.0f;
        }

        //LAB_800d0b3c
        inst.speed_28.add(inst.acceleration_38);

        if(inst.startPos_08.y > 0.0f) {
          inst.speed_28.y = -inst.speed_28.y / 2.0f;
        }

        //LAB_800d0b88
        float a3 = manager.params_10.z_22;
        final float v1 = s7 + a3;
        if(v1 >= 0xa0) {
          if(v1 >= 0xffe) {
            a3 = 0xffe - s7;
          }

          //LAB_800d0c84
          start.x += GPU.getOffsetX();
          start.y += GPU.getOffsetY();
          end.x += GPU.getOffsetX();
          end.y += GPU.getOffsetY();
          RENDERER.queueLine(this.spark, this.transforms, s7 + a3, start, end)
            .translucency(Translucency.B_PLUS_F)
            .colour((inst.r_40 >>> 8) / 255.0f, (inst.g_42 >>> 8) / 255.0f, (inst.b_44 >>> 8) / 255.0f);
        }

        //LAB_800d0ca0
        inst.r_40 -= inst.stepR_46;
        inst.g_42 -= inst.stepG_48;
        inst.b_44 -= inst.stepB_4a;
        inst.endPos_18.set(inst.startPos_08);
      }

      //LAB_800d0cec
      //LAB_800d0cf0
    }

    //LAB_800d0d10
    //LAB_800d0d94
  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    if(this.spark != null) {
      this.spark.delete();
      this.spark = null;
    }
  }
}
