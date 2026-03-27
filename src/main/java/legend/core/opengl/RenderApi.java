package legend.core.opengl;

import legend.core.QueuedModel;
import legend.core.RenderBatch;
import legend.core.RenderEngine;
import legend.core.gpu.Rect4i;
import legend.game.EngineState;
import legend.game.modding.coremod.CoreMod;
import legend.game.types.Translucency;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;

import java.nio.FloatBuffer;

import static legend.core.GameEngine.CONFIG;
import static org.lwjgl.opengl.GL11C.GL_BLEND;
import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11C.GL_FILL;
import static org.lwjgl.opengl.GL11C.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11C.GL_LINE;
import static org.lwjgl.opengl.GL11C.GL_LINE_SMOOTH;
import static org.lwjgl.opengl.GL11C.GL_ONE;
import static org.lwjgl.opengl.GL11C.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.GL_VENDOR;
import static org.lwjgl.opengl.GL11C.GL_VERSION;
import static org.lwjgl.opengl.GL11C.glBlendFunc;
import static org.lwjgl.opengl.GL11C.glClear;
import static org.lwjgl.opengl.GL11C.glClearColor;
import static org.lwjgl.opengl.GL11C.glDepthFunc;
import static org.lwjgl.opengl.GL11C.glDepthMask;
import static org.lwjgl.opengl.GL11C.glDisable;
import static org.lwjgl.opengl.GL11C.glEnable;
import static org.lwjgl.opengl.GL11C.glGetString;
import static org.lwjgl.opengl.GL11C.glLineWidth;
import static org.lwjgl.opengl.GL11C.glPolygonMode;
import static org.lwjgl.opengl.GL11C.glViewport;
import static org.lwjgl.opengl.GL14C.GL_FUNC_ADD;
import static org.lwjgl.opengl.GL14C.GL_FUNC_REVERSE_SUBTRACT;
import static org.lwjgl.opengl.GL14C.glBlendEquation;
import static org.lwjgl.opengl.GL20C.GL_SHADING_LANGUAGE_VERSION;

public class RenderApi {
  private static final Logger LOGGER = LogManager.getFormatterLogger(RenderApi.class);

  private final RenderEngine engine;
  private RenderBatch batch;
  private boolean widescreen;
  private float w;
  private float h;

  private final Vector3f clearColour = new Vector3f(-1.0f);

  private boolean backfaceCulling;

  private final Rect4i tempScissorRect = new Rect4i();
  private final Rect4i activeScissorRect = new Rect4i();

  private boolean depthTest;
  private boolean depthMask;
  private int depthComparator;

  private boolean translucencyEnabled;
  private Translucency translucency;

  private boolean wireframe;

  private float lineWidth;

  public RenderApi(final RenderEngine engine) {
    this.engine = engine;
  }

  public void create() {
    GL.createCapabilities();

    LOGGER.info("OpenGL version: %s", glGetString(GL_VERSION));
    LOGGER.info("GLSL version: %s", glGetString(GL_SHADING_LANGUAGE_VERSION));
    LOGGER.info("Device manufacturer: %s", glGetString(GL_VENDOR));

    if("true".equals(System.getenv("opengl_debug"))) {
      GLUtil.setupDebugMessageCallback(System.err);
    }

    glEnable(GL_LINE_SMOOTH);
  }

  public void initBatch(final RenderBatch batch) {
    this.batch = batch;
    this.widescreen = batch.getRenderMode() == EngineState.RenderMode.PERSPECTIVE && CoreMod.ALLOW_WIDESCREEN_CONFIG.isValid() && CONFIG.getConfig(CoreMod.ALLOW_WIDESCREEN_CONFIG.get()) || batch.getRenderMode() == EngineState.RenderMode.LEGACY && CoreMod.LEGACY_WIDESCREEN_MODE_CONFIG.isValid() && CONFIG.getConfig(CoreMod.LEGACY_WIDESCREEN_MODE_CONFIG.get()) == SubmapWidescreenMode.EXPANDED;
    this.w = (float)this.engine.getRenderWidth() / batch.nativeWidth;
    this.h = (float)this.engine.getRenderHeight() / batch.nativeHeight;

    this.backfaceCulling = false;
    glDisable(GL_CULL_FACE);
  }

  public void viewport(final int x, final int y, final int width, final int height) {
    glViewport(x, y, width, height);
  }

  public void setClearColour(final float r, final float g, final float b) {
    if(this.clearColour.x != r || this.clearColour.y != g || this.clearColour.z != b) {
      glClearColor(r, g, b, 1.0f);
      this.clearColour.set(r, g, b);
    }
  }

  public void clear() {
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
  }

  public void clearColour() {
    glClear(GL_COLOR_BUFFER_BIT);
  }

  public void clearDepth() {
    glClear(GL_DEPTH_BUFFER_BIT);
  }

  public void backfaceCulling(final boolean enable) {
    if(this.backfaceCulling != enable) {
      this.backfaceCulling = enable;

      if(enable) {
        glEnable(GL_CULL_FACE);
      } else {
        glDisable(GL_CULL_FACE);
      }
    }
  }

  public void enableDepthTest(final int comparator) {
    if(!this.depthTest) {
      glEnable(GL_DEPTH_TEST);
      this.depthTest = true;
    }

    if(this.depthComparator != comparator) {
      glDepthFunc(comparator);
      this.depthComparator = comparator;
    }
  }

  public void disableDepthTest() {
    if(this.depthTest) {
      glDisable(GL_DEPTH_TEST);
      this.depthTest = false;
    }
  }

  public void writeToDepthMask(final boolean enable) {
    if(this.depthMask != enable) {
      glDepthMask(enable);
      this.depthMask = enable;
    }
  }

  public void translucency(final boolean enable) {
    if(this.translucencyEnabled != enable) {
      if(enable) {
        glEnable(GL_BLEND);
      } else {
        glDisable(GL_BLEND);
      }

      this.translucencyEnabled = enable;
    }
  }

  public void translucencyMode(final Translucency translucency) {
    if(this.translucency != translucency) {
      this.translucency = translucency;

      switch(translucency) {
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

        default -> throw new RuntimeException(translucency + " not supported");
      }
    }
  }

  public void wireframe(final boolean enable) {
    if(this.wireframe != enable) {
      glPolygonMode(GL_FRONT_AND_BACK, enable ? GL_LINE : GL_FILL);
      this.wireframe = enable;
    }
  }

  public void lineWidth(final float width) {
    if(this.lineWidth != width) {
      glLineWidth(Math.max(1, width));
      this.lineWidth = width;
    }
  }

  public void scissor(final QueuedModel<?, ?> model, final FloatBuffer scissorBuffer, final Shader.UniformBuffer scissorUniform) {
    final Rect4i worldScissor = model.worldScissor();
    final Rect4i modelScissor = model.modelScissor();

    this.tempScissorRect.set(worldScissor.x, this.engine.getRenderHeight() - (worldScissor.y + worldScissor.h), worldScissor.w, worldScissor.h);

    if(modelScissor.w != 0 || modelScissor.h != 0) {
      if(this.widescreen) {
        this.tempScissorRect.subregion(Math.round((modelScissor.x + this.batch.widescreenOrthoOffsetX) * this.h * ((float)this.batch.expectedWidth / this.batch.nativeWidth)), this.engine.getRenderHeight() - Math.round((modelScissor.y + modelScissor.h) * this.h), Math.round(modelScissor.w * this.h * ((float)this.batch.expectedWidth / this.batch.nativeWidth)), Math.round(modelScissor.h * this.h));
      } else {
        final float offset;
        final float w;

        if(this.batch.getRenderMode() == EngineState.RenderMode.LEGACY && CONFIG.getConfig(CoreMod.LEGACY_WIDESCREEN_MODE_CONFIG.get()) == SubmapWidescreenMode.FORCED_4_3) {
          final float ratio = (float)this.engine.getRenderWidth() / this.engine.getRenderHeight();
          final float adjustedW = this.batch.nativeHeight * ratio;
          offset = (adjustedW - this.batch.nativeWidth) / 2.0f;
          w = this.h;
        } else {
          offset = this.batch.widescreenOrthoOffsetX;
          w = this.w;
        }

        this.tempScissorRect.subregion(Math.round((modelScissor.x + offset) * w), this.engine.getRenderHeight() - Math.round((modelScissor.y + modelScissor.h) * this.h), Math.round(modelScissor.w * w), Math.round(modelScissor.h * this.h));
      }
    }

    this.applyScissor(scissorBuffer, scissorUniform);
  }

  private void applyScissor(final FloatBuffer scissorBuffer, final Shader.UniformBuffer scissorUniform) {
    if(!this.activeScissorRect.equals(this.tempScissorRect)) {
      scissorBuffer.put(0, this.tempScissorRect.x);
      scissorBuffer.put(1, this.tempScissorRect.y);
      scissorBuffer.put(2, this.tempScissorRect.w);
      scissorBuffer.put(3, this.tempScissorRect.h);
      scissorUniform.set(scissorBuffer);
      this.activeScissorRect.set(this.tempScissorRect);
    }
  }
}
