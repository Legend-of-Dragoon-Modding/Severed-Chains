package legend.core.renderer.opengles;

import legend.core.gpu.Rect4i;
import legend.core.lang.RawText;
import legend.core.renderer.BufferUsage;
import legend.core.renderer.DepthComparator;
import legend.core.renderer.FrameBuffer;
import legend.core.renderer.FrameBufferAttachment;
import legend.core.renderer.Mesh;
import legend.core.renderer.QueuedModel;
import legend.core.renderer.RenderApi;
import legend.core.renderer.RenderBatch;
import legend.core.renderer.RenderEngine;
import legend.core.renderer.Shader;
import legend.core.renderer.ShaderOptions;
import legend.core.renderer.ShaderUniformBuffer;
import legend.core.renderer.SubmapWidescreenMode;
import legend.core.renderer.Texture;
import legend.core.renderer.TextureDataFormat;
import legend.core.renderer.TextureDataType;
import legend.core.renderer.TextureInternalFormat;
import legend.core.renderer.Translucency;
import legend.core.renderer.VertexOrder;
import legend.core.renderer.opengl.GlShader;
import legend.game.EngineState;
import legend.game.modding.coremod.CoreMod;
import legend.game.ui.GameOverlay;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.opengles.GLES;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.function.Supplier;

import static legend.core.GameEngine.CONFIG;
import static org.lwjgl.opengles.GLES20.GL_ALWAYS;
import static org.lwjgl.opengles.GLES20.GL_BLEND;
import static org.lwjgl.opengles.GLES20.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengles.GLES20.GL_CULL_FACE;
import static org.lwjgl.opengles.GLES20.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengles.GLES20.GL_DEPTH_TEST;
import static org.lwjgl.opengles.GLES20.GL_EQUAL;
import static org.lwjgl.opengles.GLES20.GL_FRAMEBUFFER;
import static org.lwjgl.opengles.GLES20.GL_FUNC_ADD;
import static org.lwjgl.opengles.GLES20.GL_FUNC_REVERSE_SUBTRACT;
import static org.lwjgl.opengles.GLES20.GL_GEQUAL;
import static org.lwjgl.opengles.GLES20.GL_GREATER;
import static org.lwjgl.opengles.GLES20.GL_LEQUAL;
import static org.lwjgl.opengles.GLES20.GL_LESS;
import static org.lwjgl.opengles.GLES20.GL_NEVER;
import static org.lwjgl.opengles.GLES20.GL_NOTEQUAL;
import static org.lwjgl.opengles.GLES20.GL_ONE;
import static org.lwjgl.opengles.GLES20.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengles.GLES20.GL_SHADING_LANGUAGE_VERSION;
import static org.lwjgl.opengles.GLES20.GL_SRC_ALPHA;
import static org.lwjgl.opengles.GLES20.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengles.GLES20.GL_VENDOR;
import static org.lwjgl.opengles.GLES20.GL_VERSION;
import static org.lwjgl.opengles.GLES20.glBindFramebuffer;
import static org.lwjgl.opengles.GLES20.glBlendEquation;
import static org.lwjgl.opengles.GLES20.glBlendFunc;
import static org.lwjgl.opengles.GLES20.glClear;
import static org.lwjgl.opengles.GLES20.glClearColor;
import static org.lwjgl.opengles.GLES20.glDepthFunc;
import static org.lwjgl.opengles.GLES20.glDepthMask;
import static org.lwjgl.opengles.GLES20.glDisable;
import static org.lwjgl.opengles.GLES20.glEnable;
import static org.lwjgl.opengles.GLES20.glGetString;
import static org.lwjgl.opengles.GLES20.glLineWidth;
import static org.lwjgl.opengles.GLES20.glViewport;

public class GlesApi implements RenderApi {
  private static final Logger LOGGER = LogManager.getFormatterLogger(GlesApi.class);

  private boolean backfaceCulling;

  private final RenderEngine engine;
  private RenderBatch batch;
  private boolean widescreen;
  private float w;
  private float h;

  private final Rect4i tempScissorRect = new Rect4i();
  private final Rect4i activeScissorRect = new Rect4i();

  private boolean depthTest;
  private int depthComparator;

  private Translucency translucency;

  public GlesApi(final RenderEngine engine) {
    this.engine = engine;
  }

  @Override
  public void init() {
    GLES.createCapabilities();

    LOGGER.info("OpenGLES version: %s", glGetString(GL_VERSION));
    LOGGER.info("GLSL version: %s", glGetString(GL_SHADING_LANGUAGE_VERSION));
    LOGGER.info("Device manufacturer: %s", glGetString(GL_VENDOR));

    if("true".equals(System.getenv("opengl_debug"))) {
      GLUtil.setupDebugMessageCallback(System.err);
    }
  }

  @Override
  public void resize(final int renderWidth, final int renderHeight) {
    glLineWidth(Math.max(1, renderHeight / 480.0f));
  }

  @Override
  public Mesh makeMesh(final VertexOrder vertexOrder, final float[] vertexData, final int[] indices) {
    return new GlesMesh(vertexOrder, vertexData, indices, false, false, null, BufferUsage.STATIC);
  }

  @Override
  public Mesh makeMesh(final VertexOrder vertexOrder, final float[] vertexData, final int[] indices, final boolean textured, final boolean translucent, @Nullable final Translucency translucencyMode, final BufferUsage bufferUsage) {
    return new GlesMesh(vertexOrder, vertexData, indices, textured, translucent, translucencyMode, bufferUsage);
  }

  @Override
  public Mesh makeMesh(final VertexOrder vertexOrder, final float[] vertexData, final int vertexCount) {
    return new GlesMesh(vertexOrder, vertexData, vertexCount, false, false, null, BufferUsage.STATIC);
  }

  @Override
  public Mesh makeMesh(final VertexOrder vertexOrder, final float[] vertexData, final int vertexCount, final boolean textured, final boolean translucent, @Nullable final Translucency translucencyMode, final BufferUsage bufferUsage) {
    return new GlesMesh(vertexOrder, vertexData, vertexCount, textured, translucent, translucencyMode, bufferUsage);
  }

  @Override
  public Texture makeTexture(@Nullable final Buffer buffer, final String name, final int w, final int h, final TextureInternalFormat internalFormat, final TextureDataFormat dataFormat, final TextureDataType dataType, final boolean minFilter, final boolean magFilter, final boolean wrapS, final boolean wrapT) {
    return new GlesTexture(buffer, name, w, h, internalFormat, dataFormat, dataType, minFilter, magFilter, wrapS, wrapT);
  }

  @Override
  public FrameBuffer makeFrameBuffer(final FrameBufferAttachment[] attachments) {
    return new GlesFrameBuffer(attachments);
  }

  @Override
  public <Options extends ShaderOptions> Shader<Options> makeShader(final Path vert, final Path frag, final Function<Shader<Options>, Supplier<Options>> options) throws IOException {
    return new GlesShader<>(vert, frag, options);
  }

  @Override
  public <Options extends ShaderOptions> Shader<Options> makeShader(final Path vert, final Path geom, final Path frag, final Function<Shader<Options>, Supplier<Options>> options) throws IOException {
    return new GlesShader<>(vert, geom, frag, options);
  }

  @Override
  public ShaderUniformBuffer makeUniformBuffer(final long size, final int binding) {
    return new GlesShaderUniformBuffer(size, binding);
  }

  @Override
  public void clear(final boolean colour, final boolean depth, final boolean stencil) {
    int mask = 0;

    if(colour) {
      mask |= GL_COLOR_BUFFER_BIT;
    }

    if(depth) {
      mask |= GL_DEPTH_BUFFER_BIT;
    }

    if(stencil) {
      mask |= GL_STENCIL_BUFFER_BIT;
    }

    glClear(mask);
  }

  @Override
  public void clearColour(final float r, final float g, final float b) {
    glClearColor(r, g, b, 1.0f);
  }

  @Override
  public void viewport(final int x, final int y, final int w, final int h) {
    glViewport(x, y, w, h);
  }

  @Override
  public void unbindFramebuffer() {
    glBindFramebuffer(GL_FRAMEBUFFER, 0);
  }

  @Override
  public void unbindTexture() {
    GlesTexture.unbind();
  }

  @Override
  public void initBatch(final RenderBatch batch) {
    this.batch = batch;
    this.widescreen = batch.getRenderMode() == EngineState.RenderMode.PERSPECTIVE && CoreMod.ALLOW_WIDESCREEN_CONFIG.isValid() && CONFIG.getConfig(CoreMod.ALLOW_WIDESCREEN_CONFIG.get()) || batch.getRenderMode() == EngineState.RenderMode.LEGACY && CoreMod.LEGACY_WIDESCREEN_MODE_CONFIG.isValid() && CONFIG.getConfig(CoreMod.LEGACY_WIDESCREEN_MODE_CONFIG.get()) == SubmapWidescreenMode.EXPANDED;
    this.w = (float)this.engine.getRenderWidth() / batch.nativeWidth;
    this.h = (float)this.engine.getRenderHeight() / batch.nativeHeight;

    this.backfaceCulling(false);
  }

  @Override
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

  @Override
  public void enableDepthTest(final DepthComparator comparator) {
    if(!this.depthTest) {
      glEnable(GL_DEPTH_TEST);
      this.depthTest = true;
    }

    final int value = switch(comparator) {
      case NEVER -> GL_NEVER;
      case LESS -> GL_LESS;
      case EQUAL -> GL_EQUAL;
      case LESS_THAN_OR_EQUAL -> GL_LEQUAL;
      case GREATER -> GL_GREATER;
      case NOT_EQUAL -> GL_NOTEQUAL;
      case GREATER_OR_EQUAL -> GL_GEQUAL;
      case ALWAYS -> GL_ALWAYS;
    };

    if(this.depthComparator != value) {
      glDepthFunc(value);
      this.depthComparator = value;
    }
  }

  @Override
  public void disableDepthTest() {
    if(this.depthTest) {
      glDisable(GL_DEPTH_TEST);
      this.depthTest = false;
    }
  }

  @Override
  public void scissor(final QueuedModel<?, ?> model, final FloatBuffer scissorBuffer, final ShaderUniformBuffer scissorUniform) {
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

  @Override
  public void translucency(@Nullable final Translucency translucency) {
    if(this.translucency != translucency) {
      if(this.translucency == null) {
        // Do not update the depth mask so that we don't prevent things further away than this from rendering
        glDepthMask(false);
        glEnable(GL_BLEND);
      } else if(translucency == null) {
        // Update the depth mask so nothing further away than this will render
        glDepthMask(true);
        glDisable(GL_BLEND);
      }

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

        case null -> { }

        default -> throw new RuntimeException(translucency + " not yet supported");
      }

      this.translucency = translucency;
    }
  }

  @Override
  public void wireframe(final boolean enable) {
    if(enable) {
      GameOverlay.addNotification(3, new RawText("Wireframe not supported in OpenGLES"));
    }
  }

  private void applyScissor(final FloatBuffer scissorBuffer, final ShaderUniformBuffer scissorUniform) {
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
