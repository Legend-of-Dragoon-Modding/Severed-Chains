package legend.core.opengl.fonts;

import legend.core.GameEngine;
import legend.core.opengl.Mesh;
import legend.core.opengl.Shader;

public class TextStreamText implements TextStreamable {
  private final Mesh drawable;
  public final float width;

  public TextStreamText(final Font font, final CharSequence text) {
    this.drawable = font.buildTextQuads(text);
    this.width = font.getCurrentX() / GameEngine.RENDERER.window().getScale();
  }

  @Override
  public void delete() {
    this.drawable.delete();
  }

  @Override
  public float width() {
    return this.width;
  }

  @Override
  public float draw(final Shader.UniformVec3 colourUniform) {
    this.drawable.draw();
    return this.width;
  }
}
