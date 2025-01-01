package legend.game.combat.effects;

import legend.core.MathHelper;
import legend.core.QueuedModelStandard;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.QuadBuilder;
import legend.core.opengl.Texture;
import legend.game.combat.deff.DeffPart;
import legend.game.scripting.ScriptState;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.util.function.BiConsumer;

import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment_8003.RotTransPers4;
import static legend.game.Scus94491BpeSegment_8003.perspectiveTransformTriple;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;
import static legend.game.combat.Battle.deffManager_800c693c;
import static legend.game.combat.SEffe.calculateEffectTransforms;
import static org.lwjgl.opengl.GL11.glGetTexImage;
import static org.lwjgl.opengl.GL11C.GL_LINEAR;
import static org.lwjgl.opengl.GL11C.GL_NEAREST;
import static org.lwjgl.opengl.GL11C.GL_RGBA;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11C.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL31C.GL_RGBA16_SNORM;

public class ScreenCaptureEffect1c implements Effect<EffectManagerParams.VoidType> {
  public final ScreenCaptureEffectMetrics8 metrics_00 = new ScreenCaptureEffectMetrics8();
  public int captureW_04;
  public int captureH_08;
  /**
   * 0 is unknown, 1 is for Death Dimension, Melbu screenshot attack, and demon frog
   */
  public int rendererIndex_0c;
  /**
   * Capture width and height scaled by depth into scene and projection plane distance
   */
  public float screenspaceW_10;
  public float screenspaceH_14;

  private final Vector3f normalizedLightingDirection_800fb8d0 = new Vector3f(1.0f, 0.0f, 0.0f);

  final Vector3f vert = new Vector3f();
  private final MV transforms = new MV();
  private final Obj screenshot;
  private final Texture texture;

  public ScreenCaptureEffect1c(final int captureW, final int captureH) {
    this.captureW_04 = captureW;
    this.captureH_08 = captureH;

    final int w = RENDERER.getRenderWidth();
    final int h = RENDERER.getRenderHeight();

    this.texture = Texture.create(builder -> {
      builder.size(w, h);
      builder.internalFormat(GL_RGBA16_SNORM);
      builder.dataFormat(GL_RGBA);
      builder.dataType(GL_UNSIGNED_BYTE);
      builder.magFilter(GL_NEAREST);
      builder.minFilter(GL_LINEAR);
    });

    final ByteBuffer data = BufferUtils.createByteBuffer(w * h * 4);
    RENDERER.getLastFrame().use();
    glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
    this.texture.data(0, 0, w, h, data.flip());

    final float normalizedCaptureW = captureW / RENDERER.getProjectionWidth();
    final float normalizedCaptureH = captureH / RENDERER.getProjectionHeight();
    this.screenshot = new QuadBuilder("Screen Capture")
      .bpp(Bpp.BITS_24)
      .flipY()
      .disableBackfaceCulling()
      .pos(-0.5f, -0.5f, 0)
      .posSize(1.0f, 1.0f)
      .uv((1.0f - normalizedCaptureW) / 2, (1.0f - normalizedCaptureH) / 2)
      .uvSize(normalizedCaptureW, normalizedCaptureH)
      .build();
  }

  @Method(0x8010c2e0L)
  public void setDeff(final int deffFlags) {
    if((deffFlags & 0xf_ff00) != 0xf_ff00) {
      final DeffPart.SpriteType spriteType = (DeffPart.SpriteType)deffManager_800c693c.getDeffPart(deffFlags | 0x400_0000);
      final DeffPart.SpriteMetrics deffMetrics = spriteType.metrics_08;
      this.metrics_00.u_00 = deffMetrics.u_00;
      this.metrics_00.v_02 = deffMetrics.v_02;
      this.metrics_00.clut_06 = deffMetrics.clutY_0a << 6 | (deffMetrics.clutX_08 & 0x3f0) >>> 4;
    }
    //LAB_8010c368
  }

  @Override
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {

  }

  @Override
  @Method(0x8010c114L)
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;

    if(manager.params_10.flags_00 >= 0) {
      final MV transforms = new MV();
      final ScreenCaptureEffect1c effect = (ScreenCaptureEffect1c)manager.effect_44;
      calculateEffectTransforms(transforms, manager);
      this.transforms.set(transforms);
      transforms.compose(worldToScreenMatrix_800c3548);
      GTE.setRotationMatrix(transforms);
      GTE.setTranslationVector(transforms.transfer);
      this.screenCaptureRenderers_80119fec[effect.rendererIndex_0c].accept(manager, transforms);
    }
    //LAB_8010c278
  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    if(this.texture != null) {
      this.texture.delete();
    }

    if(this.screenshot != null) {
      this.screenshot.delete();
    }
  }

  @Method(0x8010b594L)
  private void renderImagoInstantDeathCapture(final EffectManagerData6c<EffectManagerParams.VoidType> manager, final MV transforms) {
    int a0;
    int a1;

    final Vector3i rgb = new Vector3i();

    if((manager.params_10.flags_00 & 0x40) != 0) {
      final Vector3f normal = new Vector3f();
      this.normalizedLightingDirection_800fb8d0.mul(transforms, normal);
      normal.add(transforms.transfer.x / 4096.0f, transforms.transfer.y / 4096.0f, transforms.transfer.z / 4096.0f);
      GTE.normalColour(normal, 0xffffff, rgb);
    } else {
      //LAB_8010b6c8
      rgb.set(0x80, 0x80, 0x80);
    }

    //LAB_8010b6d8
    rgb.x = rgb.x * manager.params_10.colour_1c.x / 128;
    rgb.y = rgb.y * manager.params_10.colour_1c.y / 128;
    rgb.z = rgb.z * manager.params_10.colour_1c.z / 128;

    //LAB_8010b764
    for(int i = 0; i < 8; i++) {
      final GpuCommandPoly cmd = new GpuCommandPoly(3)
        .rgb(rgb.x, rgb.y, rgb.z);

      switch(i) {
        case 1, 2, 4, 7 -> {
          final Vector3f vert0 = new Vector3f();
          final Vector3f vert1 = new Vector3f();
          final Vector3f vert2 = new Vector3f();
          final Vector2f sxy0 = new Vector2f();
          final Vector2f sxy1 = new Vector2f();
          final Vector2f sxy2 = new Vector2f();

          //LAB_8010b80c
          if(i == 1 || i == 4) {
            //LAB_8010b828
            a0 = i & 0x3;
            vert1.z = (a0 - 2) * this.screenspaceW_10 / 4.0f;
            vert0.z = (a0 - 1) * this.screenspaceW_10 / 4.0f;
            vert2.z = vert0.z;
            a0 = i >> 2;
            vert0.y = (a0 - 1) * this.screenspaceH_14 / 2.0f;
            vert1.y = a0 * this.screenspaceH_14 / 2.0f;
            vert2.y = vert1.y;
            final int u = (i >> 1) * 64;
            final int v = (i & 0x1) * 32;

            cmd
              .uv(0, u, v + this.captureW_04 / 4 - 1)
              .uv(1, v, u + this.captureH_08 / 2 - 1)
              .uv(2, v + this.captureW_04 / 4 - 1, u + this.captureH_08 / 2 - 1);
          } else {
            //LAB_8010b8c8
            a0 = i & 0x3;
            vert1.z = (a0 - 2) * this.screenspaceW_10 / 4.0f;
            vert0.z = vert1.z;
            vert2.z = (a0 - 1) * this.screenspaceW_10 / 4.0f;
            a0 = i >> 2;
            vert0.y = (a0 - 1) * this.screenspaceH_14 / 2.0f;
            vert2.y = a0 * this.screenspaceH_14 / 2.0f;
            vert1.y = vert2.y;
            final int u = (i & 1) * 32;
            final int v = (i >> 1) * 64;

            cmd
              .uv(0, u, v)
              .uv(1, u, v + this.captureH_08 / 2 - 1)
              .uv(2, u + this.captureW_04 / 4 - 1, v + this.captureH_08 / 2 - 1);
          }

          //LAB_8010b9a4
          final float z = perspectiveTransformTriple(vert0, vert1, vert2, sxy0, sxy1, sxy2);

          if(this.screenspaceW_10 == 0) {
            //LAB_8010b638
            final float displaySizeMultiplier = z * 4 / 320.0f;
            this.screenspaceW_10 = this.captureW_04 * displaySizeMultiplier;
            this.screenspaceH_14 = this.captureH_08 * displaySizeMultiplier;
            break;
          }

          final ScreenCaptureEffectMetrics8 metrics = this.metrics_00;

          cmd
            .bpp(Bpp.BITS_15)
            .vramPos(metrics.u_00 & 0x3c0, (metrics.v_02 & 0x1) == 0 ? 0 : 256)
            .pos(0, sxy0.x, sxy0.y)
            .pos(1, sxy1.x, sxy1.y)
            .pos(2, sxy2.x, sxy2.y);

          GPU.queueCommand(z / 4.0f, cmd);
        }

        case 5, 6 -> {
          final Vector3f vert0 = new Vector3f();
          final Vector3f vert1 = new Vector3f();
          final Vector3f vert2 = new Vector3f();
          final Vector3f vert3 = new Vector3f();
          final Vector2f sxy0 = new Vector2f();
          final Vector2f sxy1 = new Vector2f();
          final Vector2f sxy2 = new Vector2f();
          final Vector2f sxy3 = new Vector2f();

          a0 = i & 0x3;
          a1 = i >> 2;
          vert2.z = (a0 - 2) * this.screenspaceW_10 / 4.0f;
          vert0.z = vert2.z;
          vert1.y = (a1 - 1) * this.screenspaceH_14 / 2.0f;
          vert0.y = vert1.y;
          vert3.z = (a0 - 1) * this.screenspaceW_10 / 4.0f;
          vert1.z = vert3.z;
          vert3.y = a1 * this.screenspaceH_14 / 2.0f;
          vert2.y = vert3.y;
          final float z = RotTransPers4(vert0, vert1, vert2, vert3, sxy0, sxy1, sxy2, sxy3);

          final int u = (i & 0x1) * 32;
          final int v = (i >> 1) * 64;
          final ScreenCaptureEffectMetrics8 metrics = this.metrics_00;

          GPU.queueCommand(z / 4.0f, new GpuCommandPoly(4)
            .bpp(Bpp.BITS_15)
            .vramPos(metrics.u_00 & 0x3c0, (metrics.v_02 & 0x1) != 0 ? 256 : 0)
            .rgb(rgb.x, rgb.y, rgb.z)
            .pos(0, sxy0.x, sxy0.y)
            .pos(1, sxy1.x, sxy1.y)
            .pos(2, sxy2.x, sxy2.y)
            .pos(3, sxy3.x, sxy3.y)
            .uv(0, u, v)
            .uv(1, u + this.captureW_04 / 4 - 1, v)
            .uv(2, u, v + this.captureH_08 / 2 - 1)
            .uv(3, u + this.captureW_04 / 4 - 1, v + this.captureH_08 / 2 - 1)
          );
        }
      }
    }

    //LAB_8010bc40
  }

  @Method(0x8010bc60L)
  private void renderScreenCapture(final EffectManagerData6c<EffectManagerParams.VoidType> manager, final MV transforms) {
    final Vector3i rgb = new Vector3i();

    if((manager.params_10.flags_00 & 0x40) != 0) {
      final Vector3f normal = new Vector3f();
      this.normalizedLightingDirection_800fb8d0.mul(transforms, normal);
      normal.add(transforms.transfer.x / 4096.0f, transforms.transfer.y / 4096.0f, transforms.transfer.z / 4096.0f);
      GTE.normalColour(normal, 0xffffff, rgb);
    } else {
      //LAB_8010bd6c
      rgb.set(0x80, 0x80, 0x80);
    }

    //LAB_8010bd7c
    rgb.x = rgb.x * manager.params_10.colour_1c.x / 128;
    rgb.y = rgb.y * manager.params_10.colour_1c.y / 128;
    rgb.z = rgb.z * manager.params_10.colour_1c.z / 128;

    //LAB_8010be14
    this.vert.z = this.screenspaceW_10 / 2;
    this.vert.y = this.screenspaceH_14 / 2;
    GTE.perspectiveTransform(this.vert);
    final float z = GTE.getScreenZ(3);

    if(this.screenspaceW_10 == 0) {
      //LAB_8010bd08
      final float displaySizeMultiplier = z / 320.0f;
      this.screenspaceW_10 = this.captureW_04 * displaySizeMultiplier;
      this.screenspaceH_14 = this.captureH_08 * displaySizeMultiplier;
    }

    //LAB_8010c0f0
    this.transforms.rotateY(-MathHelper.HALF_PI);
    this.transforms.scale(this.screenspaceW_10, this.screenspaceH_14, 1);
    RENDERER.queueModel(this.screenshot, this.transforms, QueuedModelStandard.class)
      .texture(this.texture)
      .colour(rgb.x / 128.0f, rgb.y / 128.0f, rgb.z / 128.0f);
  }

  /**
   * <ol start="0">
   *   <li>{@link this#renderImagoInstantDeathCapture}</li>
   *   <li>{@link this#renderScreenCapture}</li>
   * </ol>
   */
  private final BiConsumer<EffectManagerData6c<EffectManagerParams.VoidType>, MV>[] screenCaptureRenderers_80119fec = new BiConsumer[2];
  {
    this.screenCaptureRenderers_80119fec[0] = this::renderImagoInstantDeathCapture;
    this.screenCaptureRenderers_80119fec[1] = this::renderScreenCapture;
  }
}
