package legend.core.opengl.fonts;

import legend.core.opengl.FontShaderOptions;
import org.joml.Vector3f;

public class TextStreamColour implements TextStreamable {
  public static final TextStreamColour BLACK   = new TextStreamColour(new Vector3f(0.0f, 0.0f, 0.0f));
  public static final TextStreamColour WHITE   = new TextStreamColour(new Vector3f(1.0f, 1.0f, 1.0f));
  public static final TextStreamColour RED     = new TextStreamColour(new Vector3f(1.0f, 0.0f, 0.0f));
  public static final TextStreamColour GREEN   = new TextStreamColour(new Vector3f(0.0f, 1.0f, 0.0f));
  public static final TextStreamColour BLUE    = new TextStreamColour(new Vector3f(0.0f, 0.0f, 1.0f));
  public static final TextStreamColour YELLOW  = new TextStreamColour(new Vector3f(1.0f, 1.0f, 0.0f));
  public static final TextStreamColour CYAN    = new TextStreamColour(new Vector3f(0.0f, 1.0f, 1.0f));
  public static final TextStreamColour MAGENTA = new TextStreamColour(new Vector3f(1.0f, 0.0f, 1.0f));

  private final Vector3f colour;

  public TextStreamColour(final Vector3f colour) {
    this.colour = colour;
  }

  @Override
  public void delete() {

  }

  @Override
  public float width() {
    return 0.0f;
  }

  @Override
  public float draw(final FontShaderOptions shaderOptions) {
    shaderOptions.colour(this.colour);
    return 0;
  }
}
