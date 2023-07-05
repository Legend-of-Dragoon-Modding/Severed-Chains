package legend.core.ui;

import legend.core.gpu.Rect4i;
import legend.core.opengl.MatrixStack;
import legend.core.opengl.Mesh;
import legend.core.opengl.Shader;
import legend.core.opengl.ShaderManager;
import legend.core.opengl.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11C.GL_DST_COLOR;
import static org.lwjgl.opengl.GL11C.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.GL_SRC_COLOR;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11C.glBlendFunc;
import static org.lwjgl.opengl.GL14C.glBlendFuncSeparate;

public class Image extends Control {
  private HorizontalAlign horizontalAlign = HorizontalAlign.CENTRE;
  private VerticalAlign verticalAlign = VerticalAlign.MIDDLE;
  private ScaleMode scaleMode = ScaleMode.SCALE;

  private Shader.UniformBuffer transforms2;
  private Shader shader;
  private Shader.UniformVec2 uniformUv;
  private Shader.UniformVec4 uniformColour;
  private final FloatBuffer transformsBuffer = BufferUtils.createFloatBuffer(4 * 4);
  private final Texture texture;
  private final Rect4i uv;
  private Mesh mesh;

  private final Vector2f uvOffset = new Vector2f();
  private final Vector4f colour = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
  private BlendingMode blendingMode = BlendingMode.ALPHA;

  public Image(final Texture texture, final Rect4i uv) {
    this.texture = texture;
    this.uv = uv;
  }

  public Image(final Texture texture) {
    this(texture, new Rect4i(0, 0, texture.width, texture.height));
  }

  public HorizontalAlign getHorizontalAlign() {
    return this.horizontalAlign;
  }

  public void setHorizontalAlign(final HorizontalAlign horizontalAlign) {
    this.horizontalAlign = horizontalAlign;
    this.deferAction(this::updateLayout);
  }

  public VerticalAlign getVerticalAlign() {
    return this.verticalAlign;
  }

  public void setVerticalAlign(final VerticalAlign verticalAlign) {
    this.verticalAlign = verticalAlign;
    this.deferAction(this::updateLayout);
  }

  public ScaleMode getScaleMode() {
    return this.scaleMode;
  }

  public void setScaleMode(final ScaleMode scaleMode) {
    this.scaleMode = scaleMode;
    this.deferAction(this::updateLayout);
  }

  public void setUvOffset(final float u, final float v) {
    this.uvOffset.set(u, v);
  }

  public void setColour(final float r, final float g, final float b, final float a) {
    this.colour.set(r, g, b, a);
  }

  public BlendingMode getBlendingMode() {
    return this.blendingMode;
  }

  public void setBlendingMode(final BlendingMode blendingMode) {
    this.blendingMode = blendingMode;
  }

  @Override
  protected void initialize() {
    this.transforms2 = ShaderManager.getUniformBuffer("transforms2");
    this.shader = ShaderManager.getShader("simple");
    this.uniformUv = this.shader.new UniformVec2("shiftUv");
    this.uniformColour = this.shader.new UniformVec4("recolour");
  }

  @Override
  protected void updateLayout() {
    final int width = this.getWidth();
    final int height = this.getHeight();
    final float tw = this.uv.w();
    final float th = this.uv.h();

    final float l;
    final float t;
    final float r;
    final float b;
    switch(this.getScaleMode()) {
      case NONE -> {
        if(this.getHorizontalAlign() == HorizontalAlign.LEFT) {
          l = 0;
        } else if(this.getHorizontalAlign() == HorizontalAlign.RIGHT) {
          l = width - tw;
        } else {
          l = (width - tw) / 2.0f;
        }

        if(this.getVerticalAlign() == VerticalAlign.TOP) {
          t = 0;
        } else if(this.getVerticalAlign() == VerticalAlign.BOTTOM) {
          t = height - th;
        } else {
          t = (height - th) / 2.0f;
        }

        r = l + tw;
        b = t + th;
      }

      case SCALE -> {
        final float aspect = tw / th;

        float w = width;
        float h = w / aspect;

        if(h > height) {
          h = height;
          w = h * aspect;
        }

        if(this.getHorizontalAlign() == HorizontalAlign.LEFT) {
          l = 0;
        } else if(this.getHorizontalAlign() == HorizontalAlign.RIGHT) {
          l = width - w;
        } else {
          l = (width - w) / 2.0f;
        }

        if(this.getVerticalAlign() == VerticalAlign.TOP) {
          t = 0;
        } else if(this.getVerticalAlign() == VerticalAlign.BOTTOM) {
          t = height - h;
        } else {
          t = (height - h) / 2.0f;
        }

        r = l + w;
        b = t + h;
      }

      case SCALE_TO_WIDTH -> {
        final float aspect = tw / th;

        final float h = width / aspect;

        if(this.getVerticalAlign() == VerticalAlign.TOP) {
          t = 0;
        } else if(this.getVerticalAlign() == VerticalAlign.BOTTOM) {
          t = height - h;
        } else {
          t = (height - h) / 2.0f;
        }

        l = 0;
        r = width;
        b = t + h;
      }

      case SCALE_TO_HEIGHT -> {
        final float aspect = tw / th;

        final float w = height * aspect;

        if(this.getHorizontalAlign() == HorizontalAlign.LEFT) {
          l = 0;
        } else if(this.getHorizontalAlign() == HorizontalAlign.RIGHT) {
          l = width - w;
        } else {
          l = (width - w) / 2.0f;
        }

        r = l + w;
        t = 0;
        b = height;
      }

      case STRETCH -> {
        l = 0;
        t = 0;
        r = width;
        b = height;
      }

      default -> throw new IllegalStateException("Invalid scale mode");
    }

    this.mesh = new Mesh(GL_TRIANGLE_STRIP, new float[] {
      l, t, 0, (this.uv.x()              ) / (float)this.texture.width, (this.uv.y()              ) / (float)this.texture.height,
      l, b, 0, (this.uv.x()              ) / (float)this.texture.width, (this.uv.y() + this.uv.h()) / (float)this.texture.height,
      r, t, 0, (this.uv.x() + this.uv.w()) / (float)this.texture.width, (this.uv.y()              ) / (float)this.texture.height,
      r, b, 0, (this.uv.x() + this.uv.w()) / (float)this.texture.width, (this.uv.y() + this.uv.h()) / (float)this.texture.height,
    }, 4);
    this.mesh.attribute(0, 0L, 3, 5);
    this.mesh.attribute(1, 3L, 2, 5);
  }

  @Override
  protected void render(final MatrixStack matrixStack) {
    matrixStack.get(this.transformsBuffer);
    this.transforms2.set(this.transformsBuffer);

    this.blendingMode.use();

    this.shader.use();
    this.uniformUv.set(this.uvOffset.x / this.texture.width, this.uvOffset.y / this.texture.height);
    this.uniformColour.set(this.colour);
    this.texture.use();
    this.mesh.draw();
    this.uniformUv.set(0, 0);
  }

  public enum ScaleMode {
    /** Displays texture at its exact size */
    NONE,
    /** Resizes the texture to fit control but maintains aspect ratio */
    SCALE,
    /** Same as SCALE, but always fills the width */
    SCALE_TO_WIDTH,
    /** Same as SCALE, but always fills the height */
    SCALE_TO_HEIGHT,
    /** Stretches texture to fit control */
    STRETCH,
  }

  public enum BlendingMode {
    /** Standard alpha blending */
    ALPHA(() -> glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)),
    /** Adds foreground colour to background colour */
    ADD_COLOURS(() -> glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_SRC_COLOR, GL_DST_COLOR)),
    ;

    private final Runnable use;

    BlendingMode(final Runnable use) {
      this.use = use;
    }

    public void use() {
      this.use.run();
    }
  }
}
