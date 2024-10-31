package legend.core;

import legend.core.gpu.Rect4i;
import legend.core.opengl.Obj;
import legend.core.opengl.Shader;
import legend.core.opengl.ShaderOptions;
import legend.core.opengl.Texture;
import legend.game.types.Translucency;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import javax.annotation.Nullable;
import java.nio.FloatBuffer;
import java.util.Arrays;

import static legend.core.GameEngine.GPU;

public class QueuedModel<Options extends ShaderOptions<Options>> {
  private final RenderBatch batch;

  Obj obj;
  final Matrix4f transforms = new Matrix4f();
  final Matrix4f lightTransforms = new Matrix4f();
  final Vector3f screenspaceOffset = new Vector3f();
  final Vector3f colour = new Vector3f();
  final Vector2f clutOverride = new Vector2f();
  final Vector2f tpageOverride = new Vector2f();
  final Vector2f uvOffset = new Vector2f();

  final Matrix4f lightDirection = new Matrix4f();
  final Matrix3f lightColour = new Matrix3f();
  final Vector4f backgroundColour = new Vector4f();
  boolean lightUsed;

  final Rect4i scissor = new Rect4i();

  Shader<Options> shader;
  Options shaderOptions;

  int startVertex;
  int vertexCount;

  final Texture[] textures = new Texture[32];
  boolean texturesUsed;

  Translucency translucency;
  boolean hasTranslucencyOverride;

  boolean isTmd;
  int tmdTranslucency;
  int ctmdFlags;
  final Vector3f battleColour = new Vector3f();

  Vector3f[] vdf;

  public QueuedModel(final RenderBatch batch) {
    this.batch = batch;
  }

  public Options options() {
    return this.shaderOptions;
  }

  public QueuedModel<Options> screenspaceOffset(final Vector2f offset) {
    this.screenspaceOffset.x = offset.x;
    this.screenspaceOffset.y = offset.y;
    return this;
  }

  public QueuedModel<Options> screenspaceOffset(final float x, final float y) {
    this.screenspaceOffset.x = x;
    this.screenspaceOffset.y = y;
    return this;
  }

  public QueuedModel<Options> depthOffset(final float z) {
    this.screenspaceOffset.z = z;
    return this;
  }

  public QueuedModel<Options> colour(final Vector3f colour) {
    this.colour.set(colour);
    return this;
  }

  public QueuedModel<Options> colour(final float r, final float g, final float b) {
    this.colour.set(r, g, b);
    return this;
  }

  public QueuedModel<Options> monochrome(final float shade) {
    this.colour.set(shade);
    return this;
  }

  public QueuedModel<Options> clutOverride(final float x, final float y) {
    this.clutOverride.set(x, y);
    return this;
  }

  public QueuedModel<Options> tpageOverride(final float x, final float y) {
    this.tpageOverride.set(x, y);
    return this;
  }

  public QueuedModel<Options> uvOffset(final float x, final float y) {
    this.uvOffset.set(x, y);
    return this;
  }

  public QueuedModel<Options> lightDirection(final Matrix3f lightDirection) {
    this.lightDirection.set(lightDirection).mul(this.lightTransforms).setTranslation(0.0f, 0.0f, 0.0f);
    this.lightUsed = true;
    return this;
  }

  public QueuedModel<Options> lightColour(final Matrix3f lightColour) {
    this.lightColour.set(lightColour);
    this.lightUsed = true;
    return this;
  }

  public QueuedModel<Options> backgroundColour(final Vector3f backgroundColour) {
    this.backgroundColour.set(backgroundColour, 0.0f);
    this.lightUsed = true;
    return this;
  }

  /**
   * Note: origin is top-left corner
   */
  public QueuedModel<Options> scissor(final int x, final int y, final int w, final int h) {
    this.scissor.set(x, y, w, h);
    return this;
  }

  /**
   * Note: origin is top-left corner
   */
  public QueuedModel<Options> scissor(final Rect4i scissor) {
    this.scissor.set(scissor);
    return this;
  }

  public QueuedModel<Options> vertices(final int startVertex, final int vertexCount) {
    this.startVertex = startVertex;
    this.vertexCount = vertexCount;
    return this;
  }

  public QueuedModel<Options> texture(final Texture texture, final int textureUnit) {
    this.textures[textureUnit] = texture;
    this.texturesUsed = true;
    return this;
  }

  public QueuedModel<Options> texture(final Texture texture) {
    return this.texture(texture, 0);
  }

  public QueuedModel<Options> translucency(final Translucency translucency) {
    this.translucency = translucency;
    this.hasTranslucencyOverride = true;

    if(this.obj.shouldRender(Translucency.HALF_B_PLUS_HALF_F)) {
      this.batch.needsSorting = true;
    }

    return this;
  }

  public QueuedModel<Options> ctmdFlags(final int ctmdFlags) {
    this.isTmd = true;
    this.ctmdFlags = ctmdFlags;
    return this;
  }

  public QueuedModel<Options> tmdTranslucency(final int tmdTranslucency) {
    this.isTmd = true;
    this.tmdTranslucency = tmdTranslucency;
    return this;
  }

  public QueuedModel<Options> battleColour(final Vector3f colour) {
    this.battleColour.set(colour);
    return this;
  }

  public QueuedModel<Options> vdf(final Vector3f[] vdf) {
    this.vdf = vdf;
    return this;
  }

  void reset() {
    this.shader = null;
    this.shaderOptions = null;
    this.transforms.identity();
    this.lightTransforms.identity();
    this.screenspaceOffset.zero();
    this.colour.set(1.0f, 1.0f, 1.0f);
    this.clutOverride.zero();
    this.tpageOverride.zero();
    this.uvOffset.zero();
    this.scissor.set(0, 0, 0, 0);
    this.vertexCount = 0;
    Arrays.fill(this.textures, null);
    this.hasTranslucencyOverride = false;
    this.texturesUsed = false;
    this.lightUsed = false;
    this.isTmd = false;
    this.tmdTranslucency = 0;
    this.ctmdFlags = 0;
    this.battleColour.zero();
    this.vdf = null;
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

  public boolean isUniformLit() {
    return (this.ctmdFlags & 0x10) != 0;
  }

  public boolean hasTranslucency() {
    return this.hasTranslucencyOverride || (this.ctmdFlags & 0x2) != 0 || this.obj.hasTranslucency();
  }

  public boolean shouldRender(@Nullable final Translucency translucency) {
    if(this.isTmd && this.hasTranslucency() && (!this.obj.hasTexture() || this.isUniformLit())) {
      return translucency != null && this.tmdTranslucency == translucency.ordinal();
    }

    return
      this.hasTranslucencyOverride && this.translucency == translucency ||
        (this.ctmdFlags & 0x2) != 0 && translucency != null && this.tmdTranslucency == translucency.ordinal() ||
        !this.hasTranslucencyOverride && this.obj.shouldRender(translucency)
      ;
  }

  void storeTransforms(final int modelIndex, final FloatBuffer transforms2Buffer, final FloatBuffer lightingBuffer) {
    this.transforms.get(modelIndex * 20, transforms2Buffer);
    this.screenspaceOffset.get(modelIndex * 20 + 16, transforms2Buffer);

    if(this.lightUsed) {
      this.lightDirection.get(modelIndex * 32, lightingBuffer);
      this.lightColour.get3x4(modelIndex * 32 + 16, lightingBuffer);
      this.backgroundColour.get(modelIndex * 32 + 28, lightingBuffer);
    }
  }

  void render(final Translucency translucency) {
    if(this.hasTranslucencyOverride || (this.ctmdFlags & 0x2) != 0 || this.isTmd && this.obj.hasTranslucency() && (!this.obj.hasTexture() || this.isUniformLit())) {
      // Translucency override
      this.obj.render(this.startVertex, this.vertexCount);
    } else {
      this.obj.render(translucency, this.startVertex, this.vertexCount);
    }
  }

  @Override
  public String toString() {
    return this.obj.toString();
  }
}
