package legend.core;

import legend.core.gpu.Rect4i;
import legend.core.gte.MV;
import legend.core.opengl.Obj;
import legend.core.opengl.Shader;
import legend.core.opengl.ShaderOptionsBase;
import legend.core.opengl.Texture;
import legend.game.types.Translucency;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.nio.FloatBuffer;
import java.util.Arrays;

import static legend.core.GameEngine.GPU;
import static org.lwjgl.opengl.GL11C.GL_LEQUAL;
import static org.lwjgl.opengl.GL11C.GL_LESS;

public abstract class QueuedModel<Options extends ShaderOptionsBase<Options>, T extends QueuedModel<Options, T>> {
  protected final RenderBatch batch;
  protected final Shader<Options> shader;
  protected final Options shaderOptions;

  Obj obj;
  final Matrix4f transforms = new Matrix4f();
  final Vector3f screenspaceOffset = new Vector3f();
  final Vector3f colour = new Vector3f();
  final Vector2f clutOverride = new Vector2f();
  final Vector2f tpageOverride = new Vector2f();
  final Vector2f uvOffset = new Vector2f();

  final Rect4i worldScissor = new Rect4i();
  final Rect4i modelScissor = new Rect4i();

  private final Vector3f tempColour = new Vector3f();

  int startVertex;
  int vertexCount;

  final Texture[] textures = new Texture[32];
  boolean texturesUsed;

  int opaqueDepthComparator;
  int translucentDepthComparator;

  public QueuedModel(final RenderBatch batch, final Shader<Options> shader, final Options shaderOptions) {
    this.batch = batch;
    this.shader = shader;
    this.shaderOptions = shaderOptions;
  }

  public T screenspaceOffset(final Vector2f offset) {
    this.screenspaceOffset.x = offset.x;
    this.screenspaceOffset.y = offset.y;
    //noinspection unchecked
    return (T)this;
  }

  public T screenspaceOffset(final float x, final float y) {
    this.screenspaceOffset.x = x;
    this.screenspaceOffset.y = y;
    //noinspection unchecked
    return (T)this;
  }

  public T depthOffset(final float z) {
    this.screenspaceOffset.z = z;
    //noinspection unchecked
    return (T)this;
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

  /**
   * Note: origin is top-left corner
   */
  public T scissor(final int x, final int y, final int w, final int h) {
    this.modelScissor.set(x, y, w, h);
    //noinspection unchecked
    return (T)this;
  }

  /**
   * Note: origin is top-left corner
   */
  public T scissor(final Rect4i scissor) {
    this.modelScissor.set(scissor);
    //noinspection unchecked
    return (T)this;
  }

  public T vertices(final int startVertex, final int vertexCount) {
    this.startVertex = startVertex;
    this.vertexCount = vertexCount;
    //noinspection unchecked
    return (T)this;
  }

  public T texture(final Texture texture, final int textureUnit) {
    this.textures[textureUnit] = texture;
    this.texturesUsed = true;
    //noinspection unchecked
    return (T)this;
  }

  public T texture(final Texture texture) {
    return this.texture(texture, 0);
  }

  public T opaqueDepthComparator(final int comparator) {
    this.opaqueDepthComparator = comparator;
    //noinspection unchecked
    return (T)this;
  }

  public T translucentDepthComparator(final int comparator) {
    this.translucentDepthComparator = comparator;
    //noinspection unchecked
    return (T)this;
  }

  void acquire(final Obj obj, final MV transforms) {
    this.transforms.set(transforms).setTranslation(transforms.transfer);
    this.acquire(obj);
  }

  void acquire(final Obj obj, final Matrix4f transforms) {
    this.transforms.set(transforms);
    this.acquire(obj);
  }

  void acquire(final Obj obj) {
    this.obj = obj;
    this.screenspaceOffset.zero();
    this.colour.set(1.0f, 1.0f, 1.0f);
    this.clutOverride.zero();
    this.tpageOverride.zero();
    this.uvOffset.zero();
    this.modelScissor.set(0, 0, 0, 0);
    this.vertexCount = 0;
    Arrays.fill(this.textures, null);
    this.texturesUsed = false;
    this.worldScissor.set(this.batch.engine.scissorStack.top());
    this.opaqueDepthComparator = GL_LESS;
    this.translucentDepthComparator = GL_LEQUAL;
  }

  void setTransforms(final MV transforms) {
    this.transforms.set(transforms).setTranslation(transforms.transfer);
  }

  void setTransforms(final Matrix4f transforms) {
    this.transforms.set(transforms);
  }

  void useTexture() {
    if(this.texturesUsed) {
      for(int i = 0; i < this.textures.length; i++) {
        if(this.textures[i] != null) {
          this.textures[i].use(i);
        }
      }
    } else {
      GPU.useVramTexture();
    }
  }

  public Rect4i worldScissor() {
    return this.worldScissor;
  }

  public Rect4i modelScissor() {
    return this.modelScissor;
  }

  public boolean hasTranslucency() {
    return this.obj.hasTranslucency();
  }

  public boolean hasTranslucency(final int index) {
    return this.obj.hasTranslucency(index);
  }

  public void useShader(final int modelIndex, final int discardMode) {
    this.shader.use();
    this.shaderOptions.discardMode(discardMode);
    this.shaderOptions.modelIndex(modelIndex);
    this.shaderOptions.clut(this.clutOverride);
    this.shaderOptions.tpage(this.tpageOverride);
    this.shaderOptions.uvOffset(this.uvOffset);
  }

  public boolean shouldRender(@Nullable final Translucency translucency) {
    return this.obj.shouldRender(translucency);
  }

  public boolean shouldRender(@Nullable final Translucency translucency, final int layer) {
    return this.obj.shouldRender(translucency, layer);
  }

  protected void updateColours(@Nullable final Translucency translucency) {
    switch(translucency) {
      case B_PLUS_QUARTER_F -> this.shaderOptions.colour(this.colour.mul(0.25f, this.tempColour));
      case null, default -> this.shaderOptions.colour(this.colour);
    }
  }

  void storeTransforms(final int modelIndex, final FloatBuffer transforms2Buffer) {
    this.transforms.get(modelIndex * 20, transforms2Buffer);
    this.screenspaceOffset.get(modelIndex * 20 + 16, transforms2Buffer);
  }

  int getLayers() {
    return this.obj.getLayers();
  }

  void render(@Nullable final Translucency translucency, final int layer) {
    this.updateColours(translucency);
    this.obj.render(translucency, layer, this.startVertex, this.vertexCount);
  }

  @Override
  public String toString() {
    return this.obj.toString();
  }
}
