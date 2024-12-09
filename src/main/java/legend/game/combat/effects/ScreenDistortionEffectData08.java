package legend.game.combat.effects;

import legend.core.MathHelper;
import legend.core.QueuedModelStandard;
import legend.core.gpu.Bpp;
import legend.core.gte.MV;
import legend.core.memory.Method;
import legend.core.opengl.Obj;
import legend.core.opengl.PolyBuilder;
import legend.game.modding.coremod.CoreMod;
import legend.game.scripting.ScriptState;
import legend.game.types.Translucency;
import org.joml.Math;

import java.util.function.Consumer;

import static legend.core.GameEngine.CONFIG;
import static legend.core.GameEngine.GPU;
import static legend.core.GameEngine.RENDERER;
import static legend.game.Scus94491BpeSegment.displayHeight_1f8003e4;
import static legend.game.Scus94491BpeSegment.displayWidth_1f8003e0;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;

public class ScreenDistortionEffectData08 implements Effect<EffectManagerParams.VoidType> {
  private final int type;

  public float angle_00;
  public float angleStep_04;

  public final MV transforms = new MV();

  public ScreenDistortionEffectData08(final int type) {
    this.type = type;
  }

  /** Ticker and renderer are swapped */
  @Override
  public void tick(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    this.screenDistortionEffectRenderers_80119fd4[this.type].accept(state);
  }

  /** Ticker and renderer are swapped */
  @Override
  public void render(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    this.screenDistortionEffectTickers_80119fe0[this.type].accept(state);
  }

  @Override
  public void destroy(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {

  }

  /** Used in burning wave, psych bomb */
  @Method(0x80109358L)
  private void renderScreenDistortionWaveEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;

    // Dunno why these actually need to be truncated instead of fractions, but it breaks the effect otherwise
    final float multiplierX = (int)(manager.params_10.scale_16.x * 0x1000) >> 8;
    final float multiplierHeight = (int)(manager.params_10.scale_16.y * 0x1000) >> 11;
    final float rowLimit = (int)(manager.params_10.scale_16.z * 0x1000) * 15 >> 9;

    final boolean widescreen = RENDERER.getAllowWidescreen() && CONFIG.getConfig(CoreMod.ALLOW_WIDESCREEN_CONFIG.get());
    final float fullWidth;
    if(widescreen) {
      fullWidth = Math.max(displayWidth_1f8003e0, RENDERER.window().getWidth() / (float)RENDERER.window().getHeight() * displayHeight_1f8003e4);
    } else {
      fullWidth = displayWidth_1f8003e0;
    }

    final float extraWidth = fullWidth - displayWidth_1f8003e0;
    final float inverseScreenHeight = 1.0f / 240.0f;

    final PolyBuilder builder = new PolyBuilder("Wave effect", GL_TRIANGLES)
      .bpp(Bpp.BITS_24)
      .translucency(Translucency.of(manager.params_10.flags_00 >>> 28 & 0x3));

    //LAB_801093f0
    // whichHalf: 1 = bottom, -1 = top
    for(int whichHalf = 1; whichHalf >= -1; whichHalf -= 2) {
      float angle1 = this.angle_00;
      float angle2 = this.angle_00;
      float rowOffset = whichHalf == 1 ? 0.0f : -1.0f;
      int v = whichHalf == 1 ? 120 : 119;

      //LAB_80109430
      //LAB_8010944c
      while(whichHalf == -1 && rowOffset > -rowLimit || whichHalf == 1 && rowOffset < rowLimit) {
        float height = (MathHelper.sin(angle1) + 1.0f) * multiplierHeight + 1.0f;

        if((int)height == 0.0f) {
          height = 1.0f;
        }

        //LAB_8010949c
        //LAB_801094b8
        for(int row = 0; row < (int)height; row++) {
          final int x = (int)(MathHelper.sin(angle2) * multiplierX);
          final int y = (int)(row * whichHalf + rowOffset);

          this.addLineToEffect(builder, GPU.getOffsetX() - 160.0f - x, GPU.getOffsetY() + y, 1.0f - v * inverseScreenHeight, manager.params_10.colour_1c.x / 255.0f, manager.params_10.colour_1c.y / 255.0f, manager.params_10.colour_1c.z / 255.0f);

          angle2 += whichHalf * 0.05f;
        }

        //LAB_80109678
        angle1 += height * 0.05f;
        v += whichHalf;
        rowOffset += height * whichHalf;
      }
    }

    this.transforms.scaling(fullWidth, 1.0f, 1.0f);
    this.transforms.transfer.set(-extraWidth / 2, 0.0f, 120.0f);

    final Obj obj = builder.build();
    obj.delete();
    RENDERER.queueOrthoModel(obj, this.transforms, QueuedModelStandard.class)
      .texture(RENDERER.getLastFrame());
  }

  private void addLineToEffect(final PolyBuilder builder, final float x, final float y, final float v, final float r, final float g, final float b) {
    builder
      .addVertex(0.0f, y, 0.0f)
      .uv(x / 320.0f, v)
      .rgb(r, g, b)
      .addVertex(1.0f, y, 0.0f)
      .uv(x / 320.0f + 1.0f, v)
      .rgb(r, g, b)
      .addVertex(0.0f, y + 1.0f, 0.0f)
      .uv(x / 320.0f, v - 1.0f / 240.0f)
      .rgb(r, g, b)
      .addVertex(1.0f, y, 0.0f)
      .uv(x / 320.0f + 1.0f, v)
      .rgb(r, g, b)
      .addVertex(0.0f, y + 1.0f, 0.0f)
      .uv(x / 320.0f, v - 1.0f / 240.0f)
      .rgb(r, g, b)
      .addVertex(1.0f, y + 1.0f, 0.0f)
      .uv(x / 320.0f + 1.0f, v - 1.0f / 240.0f)
      .rgb(r, g, b);
  }

  @Method(0x801097e0L)
  private void renderScreenDistortionBlurEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    final EffectManagerData6c<EffectManagerParams.VoidType> manager = state.innerStruct_00;

    // Make sure effect fills the whole screen
    final boolean widescreen = RENDERER.getAllowWidescreen() && CONFIG.getConfig(CoreMod.ALLOW_WIDESCREEN_CONFIG.get());
    final float fullWidth;
    if(widescreen) {
      fullWidth = Math.max(displayWidth_1f8003e0, RENDERER.window().getWidth() / (float)RENDERER.window().getHeight() * displayHeight_1f8003e4);
    } else {
      fullWidth = displayWidth_1f8003e0;
    }

    final float extraWidth = fullWidth - displayWidth_1f8003e0;
    this.transforms.scaling(fullWidth, displayHeight_1f8003e4, 1.0f);
    this.transforms.transfer.set(-extraWidth / 2, 0.0f, 120.0f);

    RENDERER.queueOrthoModel(RENDERER.renderBufferQuad, this.transforms, QueuedModelStandard.class)
      .translucency(Translucency.of(manager.params_10.flags_00 >>> 28 & 0x3))
      .colour(manager.params_10.colour_1c.x / 128.0f, manager.params_10.colour_1c.y / 128.0f, manager.params_10.colour_1c.z / 128.0f)
      .texture(RENDERER.getLastFrame());
  }

  @Method(0x80109a4cL)
  private void tickScreenDistortionWaveEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    this.angle_00 += this.angleStep_04;
  }

  @Method(0x80109a6cL)
  private void tickScreenDistortionBlurEffect(final ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>> state) {
    // no-op
  }

  /**
   * <ol start="0">
   *   <li>{@link this#renderScreenDistortionWaveEffect}</li>
   *   <li>{@link this#renderScreenDistortionWaveEffect}</li>
   *   <li>{@link this#renderScreenDistortionBlurEffect}</li>
   * </ol>
   */
  private final Consumer<ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>>>[] screenDistortionEffectRenderers_80119fd4 = new Consumer[3];
  {
    this.screenDistortionEffectRenderers_80119fd4[0] = this::renderScreenDistortionWaveEffect;
    this.screenDistortionEffectRenderers_80119fd4[1] = this::renderScreenDistortionWaveEffect;
    this.screenDistortionEffectRenderers_80119fd4[2] = this::renderScreenDistortionBlurEffect;
  }
  /**
   * <ol start="0">
   *   <li>{@link this#tickScreenDistortionWaveEffect}</li>
   *   <li>{@link this#tickScreenDistortionWaveEffect}</li>
   *   <li>{@link this#tickScreenDistortionBlurEffect}</li>
   * </ol>
   */
  private final Consumer<ScriptState<EffectManagerData6c<EffectManagerParams.VoidType>>>[] screenDistortionEffectTickers_80119fe0 = new Consumer[3];
  {
    this.screenDistortionEffectTickers_80119fe0[0] = this::tickScreenDistortionWaveEffect;
    this.screenDistortionEffectTickers_80119fe0[1] = this::tickScreenDistortionWaveEffect;
    this.screenDistortionEffectTickers_80119fe0[2] = this::tickScreenDistortionBlurEffect;
  }
}
