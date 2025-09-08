package legend.core.opengl;

import legend.core.RenderEngine;
import legend.core.gpu.Rect4i;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengles.GLES;

import static org.lwjgl.opengles.GLES20.GL_CULL_FACE;
import static org.lwjgl.opengles.GLES20.GL_DEPTH_TEST;
import static org.lwjgl.opengles.GLES20.GL_SCISSOR_TEST;
import static org.lwjgl.opengles.GLES20.GL_SHADING_LANGUAGE_VERSION;
import static org.lwjgl.opengles.GLES20.GL_VENDOR;
import static org.lwjgl.opengles.GLES20.GL_VERSION;
import static org.lwjgl.opengles.GLES20.glDepthFunc;
import static org.lwjgl.opengles.GLES20.glDisable;
import static org.lwjgl.opengles.GLES20.glEnable;
import static org.lwjgl.opengles.GLES20.glGetString;
import static org.lwjgl.opengles.GLES20.glScissor;

public class RenderStateEs extends RenderState {
  private static final Logger LOGGER = LogManager.getFormatterLogger(RenderStateEs.class);

  public RenderStateEs(final RenderEngine engine) {
    super(engine);
    GLES.createCapabilities();

    LOGGER.info("OpenGL version: %s", glGetString(GL_VERSION));
    LOGGER.info("GLSL version: %s", glGetString(GL_SHADING_LANGUAGE_VERSION));
    LOGGER.info("Device manufacturer: %s", glGetString(GL_VENDOR));

    if("true".equals(System.getenv("opengl_debug"))) {
      //TODO GLUtil.setupDebugMessageCallback(System.err);
    }

    //TODO glEnable(GL_LINE_SMOOTH);
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
