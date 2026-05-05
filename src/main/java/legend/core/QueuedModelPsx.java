package legend.core;

import legend.core.opengl.Obj;
import legend.core.opengl.Shader;
import legend.core.opengl.ShaderOptionsPsx;
import legend.game.types.Translucency;
import org.joml.Vector2f;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public abstract class QueuedModelPsx<Options extends ShaderOptionsPsx<Options>, T extends QueuedModelPsx<Options, T>> extends QueuedModel<Options, T> {
  final Vector3f colour = new Vector3f();
  final Vector2f clutOverride = new Vector2f();
  final Vector2f tpageOverride = new Vector2f();
  final Vector2f uvOffset = new Vector2f();

  private final Vector3f tempColour = new Vector3f();

  public QueuedModelPsx(final RenderBatch batch, final Shader<Options> shader, final Options shaderOptions) {
    super(batch, shader, shaderOptions);
  }

  public T colour(final Vector3f colour) {
    this.colour.set(colour);
    //noinspection unchecked
    return (T)this;
  }

  public T colour(final float r, final float g, final float b) {
    this.colour.set(r, g, b);
    //noinspection unchecked
    return (T)this;
  }

  public T monochrome(final float shade) {
    this.colour.set(shade);
    //noinspection unchecked
    return (T)this;
  }

  public T clutOverride(final float x, final float y) {
    this.clutOverride.set(x, y);
    //noinspection unchecked
    return (T)this;
  }

  public T tpageOverride(final float x, final float y) {
    this.tpageOverride.set(x, y);
    //noinspection unchecked
    return (T)this;
  }

  public T uvOffset(final float x, final float y) {
    this.uvOffset.set(x, y);
    //noinspection unchecked
    return (T)this;
  }

  @Override
  void acquire(final Obj obj, final int sequence) {
    super.acquire(obj, sequence);
    this.colour.set(1.0f, 1.0f, 1.0f);
    this.clutOverride.zero();
    this.tpageOverride.zero();
    this.uvOffset.zero();
  }

  @Override
  public void useShader(final int modelIndex, final int discardMode) {
    super.useShader(modelIndex, discardMode);
    this.shaderOptions.clut(this.clutOverride);
    this.shaderOptions.tpage(this.tpageOverride);
    this.shaderOptions.uvOffset(this.uvOffset);
  }

  protected void updateColours(@Nullable final Translucency translucency) {
    switch(translucency) {
      case B_PLUS_QUARTER_F -> this.shaderOptions.colour(this.colour.mul(0.25f, this.tempColour));
      case null, default -> this.shaderOptions.colour(this.colour);
    }
  }

  @Override
  void render(@Nullable final Translucency translucency, final int layer) {
    this.updateColours(translucency);
    super.render(translucency, layer);
  }
}
