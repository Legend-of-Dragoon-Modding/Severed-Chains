package legend.game.combat.effects;

import legend.core.MathHelper;
import legend.core.QueuedModelStandard;
import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.PolyBuilder;
import legend.core.opengl.QuadBuilder;
import legend.core.opengl.Texture;
import legend.game.combat.deff.DeffPart;
import legend.game.scripting.ScriptState;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.util.function.BiConsumer;

import static legend.core.GameEngine.GTE;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment_800c.worldToScreenMatrix_800c3548;
import static legend.game.combat.Battle.deffManager_800c693c;
import static legend.game.combat.SEffe.calculateEffectTransforms;
import static org.lwjgl.opengl.GL11C.GL_LINEAR;
import static org.lwjgl.opengl.GL11C.GL_NEAREST;
import static org.lwjgl.opengl.GL11C.GL_RGBA;
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
  final Vector3f normal = new Vector3f();
  private final MV transforms = new MV();
  private final Obj screenshot;
  private final Texture texture;

  public ScreenCaptureEffect1c(final int captureW, final int captureH, final int rendererIndex) {
    this.captureW_04 = captureW;
    this.captureH_08 = captureH;
    this.rendererIndex_0c = rendererIndex;

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
    this.texture.getData(data);
    this.texture.data(0, 0, w, h, data.flip());

    final float widthFactor = ((float)RENDERER.getRenderWidth() / RENDERER.getRenderHeight()) / (4.0f / 3.0f);
    final float normalizedCaptureW = (float)captureW / RENDERER.getNativeWidth() / widthFactor;
    final float normalizedCaptureH = (float)captureH / RENDERER.getNativeHeight();

    if(rendererIndex == 0) {
      this.screenshot = new PolyBuilder("Screen Capture")
        .bpp(Bpp.BITS_24)
        .disableBackfaceCulling()
        .addVertex(-0.5f, 0.5f, 0)
        .uv((1.0f - normalizedCaptureW) / 2, -(1.0f + normalizedCaptureH) / 2)
        .addVertex(0, -0.5f, 0)
        .uv(0.5f, -(1.0f - normalizedCaptureH) / 2)
        .addVertex(0.5f, 0.5f, 0)
        .uv((1.0f + normalizedCaptureW) / 2, -(1.0f + normalizedCaptureH) / 2)
        .build();
    } else {
      this.screenshot = new QuadBuilder("Screen Capture")
        .bpp(Bpp.BITS_24)
        .disableBackfaceCulling()
        .pos(-0.5f, -0.5f, 0)
        .posSize(1.0f, 1.0f)
        .uv((1.0f - normalizedCaptureW) / 2, -(1.0f - normalizedCaptureH) / 2)
        .uvSize(normalizedCaptureW, -normalizedCaptureH)
        .build();
    }
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
    final Vector3i rgb = new Vector3i();

    if((manager.params_10.flags_00 & 0x40) != 0) {
      this.normalizedLightingDirection_800fb8d0.mul(transforms, this.normal);
      this.normal.add(transforms.transfer.x / 4096.0f, transforms.transfer.y / 4096.0f, transforms.transfer.z / 4096.0f);
      GTE.normalColour(this.normal, 0xffffff, rgb);
    } else {
      //LAB_8010b6c8
      rgb.set(0x80, 0x80, 0x80);
    }

    //LAB_8010b6d8
    rgb.x = rgb.x * manager.params_10.colour_1c.x / 128;
    rgb.y = rgb.y * manager.params_10.colour_1c.y / 128;
    rgb.z = rgb.z * manager.params_10.colour_1c.z / 128;

    this.vert.z = this.screenspaceW_10 / 2;
    this.vert.y = this.screenspaceH_14 / 2;
    GTE.perspectiveTransform(this.vert);
    final float z = GTE.getScreenZ(3);

    if(this.screenspaceW_10 == 0) {
      //LAB_8010b638
      final float displaySizeMultiplier = z / 320.0f;
      this.screenspaceW_10 = this.captureW_04 * displaySizeMultiplier;
      this.screenspaceH_14 = this.captureH_08 * displaySizeMultiplier;
      return;
    }

    this.transforms.rotateY(-MathHelper.HALF_PI);
    this.transforms.scale(this.screenspaceW_10, this.screenspaceH_14, 1);
    RENDERER.queueModel(this.screenshot, this.transforms, QueuedModelStandard.class)
      .texture(this.texture)
      .colour(rgb.x / 128.0f, rgb.y / 128.0f, rgb.z / 128.0f);
  }

  @Method(0x8010bc60L)
  private void renderScreenCapture(final EffectManagerData6c<EffectManagerParams.VoidType> manager, final MV transforms) {
    final Vector3i rgb = new Vector3i();

    if((manager.params_10.flags_00 & 0x40) != 0) {
      this.normalizedLightingDirection_800fb8d0.mul(transforms, this.normal);
      this.normal.add(transforms.transfer.x / 4096.0f, transforms.transfer.y / 4096.0f, transforms.transfer.z / 4096.0f);
      GTE.normalColour(this.normal, 0xffffff, rgb);
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
      return;
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
