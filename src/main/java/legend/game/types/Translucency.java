package legend.game.types;

import javax.annotation.Nonnull;

import static org.lwjgl.opengl.GL11C.GL_ONE;
import static org.lwjgl.opengl.GL11C.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.glBlendFunc;
import static org.lwjgl.opengl.GL14C.GL_FUNC_ADD;
import static org.lwjgl.opengl.GL14C.GL_FUNC_REVERSE_SUBTRACT;
import static org.lwjgl.opengl.GL14C.glBlendEquation;

public enum Translucency {
  /** 0.5 x background + 0.5 x foreground */
  HALF_B_PLUS_HALF_F,
  /** 1.0 x background + 1.0 x foreground */
  B_PLUS_F,
  /** 1.0 x background - 1.0 x foreground */
  B_MINUS_F,
  /** 1.0 x background + 0.25 x foreground */
  B_PLUS_QUARTER_F,
  ;

  public static final Translucency[] FOR_RENDERING = {HALF_B_PLUS_HALF_F, B_PLUS_F, B_MINUS_F, B_PLUS_QUARTER_F};
  public static final Translucency[] ORDER_DEPENDENT = {HALF_B_PLUS_HALF_F};

  /** NOTE: returns null if value is -1 */
  @Nonnull
  public static Translucency of(final int value) {
    if(value == -1) {
      return null;
    }

    return values()[value];
  }

  public void setGlState() {
    switch(this) {
      case HALF_B_PLUS_HALF_F -> {
        glBlendEquation(GL_FUNC_ADD);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
      }

      case B_PLUS_F -> {
        glBlendEquation(GL_FUNC_ADD);
        glBlendFunc(GL_ONE, GL_ONE);
      }

      case B_MINUS_F -> {
        glBlendEquation(GL_FUNC_REVERSE_SUBTRACT);
        glBlendFunc(GL_ONE, GL_ONE);
      }

      default -> throw new RuntimeException(this + " not yet supported");
    }
  }
}
