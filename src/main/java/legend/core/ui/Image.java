package legend.core.ui;

import legend.core.opengl.MatrixStack;
import legend.core.opengl.Mesh;
import legend.core.opengl.Shader;
import legend.core.opengl.ShaderManager;
import legend.core.opengl.Texture;
import org.joml.Vector2f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11C.GL_TRIANGLE_STRIP;

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
  private Mesh mesh;

  private final Vector2f uv = new Vector2f();
  private final Vector4f colour = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);

  public Image(final Texture texture) {
    this.texture = texture;
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

  public void setUv(final float u, final float v) {
    this.uv.set(u, v);
  }

  public void setColour(final float r, final float g, final float b, final float a) {
    this.colour.set(r, g, b, a);
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

    final float l;
    final float t;
    final float r;
    final float b;
    switch(this.getScaleMode()) {
      case NONE -> {
        if(this.getHorizontalAlign() == HorizontalAlign.LEFT) {
          l = 0;
        } else if(this.getHorizontalAlign() == HorizontalAlign.RIGHT) {
          l = width - this.texture.width;
        } else {
          l = (width - this.texture.width) / 2.0f;
        }

        if(this.getVerticalAlign() == VerticalAlign.TOP) {
          t = 0;
        } else if(this.getVerticalAlign() == VerticalAlign.BOTTOM) {
          t = height - this.texture.height;
        } else {
          t = (height - this.texture.height) / 2.0f;
        }

        r = l + this.texture.width;
        b = t + this.texture.height;
      }

      case SCALE -> {
        final float aspect = (float)this.texture.width / this.texture.height;

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
        final float aspect = (float)this.texture.width / this.texture.height;

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
        final float aspect = (float)this.texture.width / this.texture.height;

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
      l, t, 0, 0, 0,
      l, b, 0, 0, 1,
      r, t, 0, 1, 0,
      r, b, 0, 1, 1,
    }, 4);
    this.mesh.attribute(0, 0L, 3, 5);
    this.mesh.attribute(1, 3L, 2, 5);
  }

  @Override
  protected void render(final MatrixStack matrixStack) {
    matrixStack.get(this.transformsBuffer);
    this.transforms2.set(this.transformsBuffer);

    this.shader.use();
    this.uniformUv.set(this.uv.x / this.texture.width, this.uv.y / this.texture.height);
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
}
