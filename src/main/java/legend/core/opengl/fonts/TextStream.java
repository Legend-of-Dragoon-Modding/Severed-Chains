package legend.core.opengl.fonts;

import legend.core.RenderEngine;
import legend.core.opengl.FontShaderOptions;
import legend.core.opengl.Shader;
import legend.core.opengl.ShaderManager;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class TextStream {
  private static final TextStreamable[] EMPTY_STREAMABLE = new TextStreamable[0];

  public static TextStream create(final Font font, final Consumer<Builder> stream) {
    final Builder builder = new Builder(font);
    stream.accept(builder);
    return builder.build();
  }

  private final Font font;
  private final Shader<FontShaderOptions> shader;
  private final FontShaderOptions options;
  private final Shader.UniformBuffer transforms2;
  private final FloatBuffer transformsBuffer = BufferUtils.createFloatBuffer(4 * 4);
  private final Matrix4f transforms = new Matrix4f();
  private final TextStreamable[] elements;

  private TextStream(final Font font, final TextStreamable[] elements) {
    this.font = font;
    this.shader = ShaderManager.getShader(RenderEngine.FONT_SHADER);
    this.options = this.shader.makeOptions();
    this.transforms2 = ShaderManager.getUniformBuffer("transforms2");
    this.elements = elements;
  }

  public void setColour(final float r, final float g, final float b) {
    this.shader.use();
    this.options.colour(r, g, b);
    this.options.apply();
  }

  public void delete() {
    for(final TextStreamable element : this.elements) {
      element.delete();
    }
  }

  public float width() {
    float width = 0;

    for(final TextStreamable streamable : this.elements) {
      width += streamable.width();
    }

    return width;
  }

  public void draw(final float xOffset, final float yOffset) {
    this.draw(xOffset, yOffset, 1.0f, 1.0f);
  }

  public void draw(final float xOffset, final float yOffset, final float scaleX, final float scaleY) {
    this.font.use();
    this.shader.use();

    float x = xOffset;

    for(final TextStreamable streamable : this.elements) {
      this.transforms
        .translation(x, yOffset, 1.0f)
        .scale(scaleX, scaleY, 1.0f)
      ;
      this.transforms.get(this.transformsBuffer);
      this.transforms2.set(this.transformsBuffer);

      x += streamable.draw(this.options);
    }
  }

  public static final class Builder {
    private final Font font;
    private final List<TextStreamable> elements = new ArrayList<>();

    private Builder(final Font font) {
      this.font = font;
    }

    public Builder text(final CharSequence text) {
      this.elements.add(new TextStreamText(this.font, text));
      return this;
    }

    public Builder colour(final TextStreamColour colour) {
      this.elements.add(colour);
      return this;
    }

    public Builder colour(final Vector3f colour) {
      this.elements.add(new TextStreamColour(colour));
      return this;
    }

    private TextStream build() {
      return new TextStream(this.font, this.elements.toArray(EMPTY_STREAMABLE));
    }
  }
}
