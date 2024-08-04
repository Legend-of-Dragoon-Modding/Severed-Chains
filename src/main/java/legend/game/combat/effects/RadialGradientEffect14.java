package legend.game.combat.effects;

import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.memory.types.QuadConsumer;
import legend.core.opengl.Obj;
import legend.core.opengl.PolyBuilder;
import legend.game.scripting.ScriptState;
import legend.game.types.Translucency;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment.rcos;
import static legend.game.Scus94491BpeSegment.rsin;
import static legend.game.combat.SEffe.FUN_800cfb14;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLE_STRIP;

public class RadialGradientEffect14 implements Effect<EffectManagerParams.RadialGradientType> {
  private final int circleSubdivisionModifier_00;
  private final float scaleModifier_01;

  private float z_04;
  private int angleStep_08;
  private int r_0c;
  private int g_0d;
  private int b_0e;

  private final QuadConsumer<EffectManagerData6c<EffectManagerParams.RadialGradientType>, Integer, Vector2f[], Translucency> renderer_10;

  public RadialGradientEffect14(final int type, final int subdivisionModifier) {
    this.circleSubdivisionModifier_00 = subdivisionModifier;
    this.scaleModifier_01 = (type - 3 & 0xffff_ffffL) >= 2 ? 4.0f : 1.0f;
    this.renderer_10 = switch(type) {
      case 0, 3 -> this::renderDiscGradientEffect;
      case 1 -> this::FUN_800d1e80; // Not implemented
      case 2, 4 -> this::renderRingGradientEffect;
      default -> throw new IllegalArgumentException("Invalid type " + type);
    };
  }

  /** Renders things like the two-tone disc at the start of Detonating Arrow */
  @Method(0x800d1d3cL)
  private void renderDiscGradientEffect(final EffectManagerData6c<EffectManagerParams.RadialGradientType> manager, final int angle, final Vector2f[] vertices, final Translucency translucency) {
    if(manager.params_10.flags_00 >= 0) {
      final Obj obj = new PolyBuilder("Ring gradient triangle", GL_TRIANGLES)
        .translucency(translucency)
        .addVertex(vertices[0].x, vertices[0].y, 0.0f)
        .rgb(manager.params_10.colour_1c.x / 255.0f, manager.params_10.colour_1c.y / 255.0f, manager.params_10.colour_1c.z / 255.0f)
        .addVertex(vertices[1].x, vertices[1].y, 0.0f)
        .rgb(this.r_0c / 255.0f, this.g_0d / 255.0f, this.b_0e / 255.0f)
        .addVertex(vertices[2].x, vertices[2].y, 0.0f)
        .rgb(this.r_0c / 255.0f, this.g_0d / 255.0f, this.b_0e / 255.0f)
        .build();

      final MV transforms = new MV();
      transforms.transfer.set(GPU.getOffsetX(), GPU.getOffsetY(), this.z_04 + manager.params_10.z_22);
      RENDERER.queueOrthoModel(obj, transforms);

      obj.delete(); // Mark for deletion after this frame
    }

    //LAB_800d1e70
  }

  @Method(0x800d1e80L)
  private void FUN_800d1e80(final EffectManagerData6c<EffectManagerParams.RadialGradientType> manager, final int angle, final Vector2f[] vertices, final Translucency translucency) {
    throw new RuntimeException("Not implemented");
  }

  /** Renders things like the ring effect when using a healing potion */
  @Method(0x800d21b8L)
  private void renderRingGradientEffect(final EffectManagerData6c<EffectManagerParams.RadialGradientType> manager, final int angle, final Vector2f[] vertices, final Translucency translucency) {
    if(manager.params_10.flags_00 >= 0) {
      //TODO why does rsin/rcos not have to be >> 12?
      final Vector3f sp0x20 = new Vector3f(
        rcos(angle) * (manager.params_10.scale_16.x / this.scaleModifier_01 + manager.params_10.size_28 / 4096.0f),
        rsin(angle) * (manager.params_10.scale_16.y / this.scaleModifier_01 + manager.params_10.size_28 / 4096.0f),
        manager.params_10.z_2c
      );

      final Vector2f screenVert0 = new Vector2f();
      FUN_800cfb14(manager, sp0x20, screenVert0);

      //TODO why does rsin/rcos not have to be >> 12?
      final Vector3f sp0x30 = new Vector3f(
        rcos(angle + this.angleStep_08) * (manager.params_10.scale_16.x / this.scaleModifier_01 + manager.params_10.size_28 / 4096.0f),
        rsin(angle + this.angleStep_08) * (manager.params_10.scale_16.y / this.scaleModifier_01 + manager.params_10.size_28 / 4096.0f),
        manager.params_10.z_2c
      );

      final Vector2f screenVert1 = new Vector2f();
      FUN_800cfb14(manager, sp0x30, screenVert1);

      final Obj obj = new PolyBuilder("Ring gradient quad", GL_TRIANGLE_STRIP)
        .translucency(translucency)
        .addVertex(screenVert0.x, screenVert0.y, 0.0f)
        .rgb(manager.params_10.colour_1c.x / 255.0f, manager.params_10.colour_1c.y / 255.0f, manager.params_10.colour_1c.z / 255.0f)
        .addVertex(screenVert1.x, screenVert1.y, 0.0f)
        .rgb(manager.params_10.colour_1c.x / 255.0f, manager.params_10.colour_1c.y / 255.0f, manager.params_10.colour_1c.z / 255.0f)
        .addVertex(vertices[1].x, vertices[1].y, 0.0f)
        .rgb(this.r_0c / 255.0f, this.g_0d / 255.0f, this.b_0e / 255.0f)
        .addVertex(vertices[2].x, vertices[2].y, 0.0f)
        .rgb(this.r_0c / 255.0f, this.g_0d / 255.0f, this.b_0e / 255.0f)
        .build();

      final MV transforms = new MV();
      transforms.transfer.set(GPU.getOffsetX(), GPU.getOffsetY(), this.z_04 + manager.params_10.z_22);
      RENDERER.queueOrthoModel(obj, transforms);

      obj.delete(); // Mark for deletion after this frame
    }

    //LAB_800d2460
  }

  @Override
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.RadialGradientType>> state) {

  }

  @Override
  @Method(0x800d247cL)
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.RadialGradientType>> state) {
    final EffectManagerData6c<EffectManagerParams.RadialGradientType> manager = state.innerStruct_00;
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
        this.renderer_10.accept(manager, angle, new Vector2f[] {screenVert0, screenVert1, screenVert2}, (manager.params_10.flags_00 & 0x1000_0000) != 0 ? Translucency.B_PLUS_F : Translucency.B_MINUS_F);
        angle += this.angleStep_08;
      }
    }

    //LAB_800d2710
  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.RadialGradientType>> state) {

  }
}
