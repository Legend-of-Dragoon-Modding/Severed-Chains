package legend.core.opengl;

import legend.core.RenderEngine;
import legend.core.gpu.Rect4i;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;

import static org.lwjgl.opengl.GL11C.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11C.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11C.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11C.GL_VENDOR;
import static org.lwjgl.opengl.GL11C.GL_VERSION;
import static org.lwjgl.opengl.GL11C.glDepthFunc;
import static org.lwjgl.opengl.GL11C.glDisable;
import static org.lwjgl.opengl.GL11C.glEnable;
import static org.lwjgl.opengl.GL11C.glGetString;
import static org.lwjgl.opengl.GL11C.glScissor;
import static org.lwjgl.opengl.GL20C.GL_SHADING_LANGUAGE_VERSION;

public class RenderStateGl extends RenderState {
  private static final Logger LOGGER = LogManager.getFormatterLogger(RenderStateGl.class);

  public RenderStateGl(final RenderEngine engine) {
    super(engine);
    GL.createCapabilities();

    LOGGER.info("OpenGL version: %s", glGetString(GL_VERSION));
    LOGGER.info("GLSL version: %s", glGetString(GL_SHADING_LANGUAGE_VERSION));
    LOGGER.info("Device manufacturer: %s", glGetString(GL_VENDOR));

    if("true".equals(System.getenv("opengl_debug"))) {
      GLUtil.setupDebugMessageCallback(System.err);
    }

    glEnable(GL_LINE_SMOOTH);
  }

  @Override
  protected void setCulling(final boolean enable) {
    this.backfaceCulling = enable;

    if(enable) {
      glEnable(GL_CULL_FACE);
    } else {
      glDisable(GL_CULL_FACE);
    }
  }

  @Override
  protected void setDepthTest(final boolean enable) {
    this.depthTest = enable;

    if(enable) {
      glEnable(GL_DEPTH_TEST);
    } else {
      glDisable(GL_DEPTH_TEST);
    }
  }

  @Override
  protected void setDepthComparator(final int comparator) {
    this.depthComparator = comparator;
    glDepthFunc(comparator);
  }

  @Override
  protected void setScissor(final boolean enable) {
    this.scissor = enable;

    if(enable) {
      glEnable(GL_SCISSOR_TEST);
    } else {
      glDisable(GL_SCISSOR_TEST);
    }
  }

  @Override
  protected void setScissorRect(final Rect4i rect) {
    glScissor(rect.x, rect.y, rect.w, rect.h);
    this.activeScissorRect.set(rect);
  }
}
