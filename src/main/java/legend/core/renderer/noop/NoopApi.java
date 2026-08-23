package legend.core.renderer.noop;

import legend.core.renderer.BufferUsage;
import legend.core.renderer.DepthComparator;
import legend.core.renderer.FrameBuffer;
import legend.core.renderer.FrameBufferAttachment;
import legend.core.renderer.Mesh;
import legend.core.renderer.QueuedModel;
import legend.core.renderer.RenderApi;
import legend.core.renderer.RenderBatch;
import legend.core.renderer.Shader;
import legend.core.renderer.ShaderOptions;
import legend.core.renderer.ShaderUniformBuffer;
import legend.core.renderer.Texture;
import legend.core.renderer.TextureDataFormat;
import legend.core.renderer.TextureDataType;
import legend.core.renderer.TextureInternalFormat;
import legend.core.renderer.Translucency;
import legend.core.renderer.VertexOrder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.function.Supplier;

public class NoopApi implements RenderApi {
  private static final Logger LOGGER = LogManager.getFormatterLogger(NoopApi.class);

  @Override
  public void init() {
    LOGGER.info("No-op renderer version: 1.0");
  }

  @Override
  public void resize(final int renderWidth, final int renderHeight) {

  }

  @Override
  public Mesh makeMesh(final VertexOrder vertexOrder, final float[] vertexData, final int[] indices) {
    return new NoopMesh(vertexData, false, false, null);
  }

  @Override
  public Mesh makeMesh(final VertexOrder vertexOrder, final float[] vertexData, final int[] indices, final boolean textured, final boolean translucent, @Nullable final Translucency translucencyMode, final BufferUsage bufferUsage) {
    return new NoopMesh(vertexData, false, false, null);
  }

  @Override
  public Mesh makeMesh(final VertexOrder vertexOrder, final float[] vertexData, final int vertexCount) {
    return new NoopMesh(vertexData, false, false, null);
  }

  @Override
  public Mesh makeMesh(final VertexOrder vertexOrder, final float[] vertexData, final int vertexCount, final boolean textured, final boolean translucent, @Nullable final Translucency translucencyMode, final BufferUsage bufferUsage) {
    return new NoopMesh(vertexData, false, false, null);
  }

  @Override
  public Texture makeTexture(@Nullable final Buffer buffer, final String name, final int w, final int h, final TextureInternalFormat internalFormat, final TextureDataFormat dataFormat, final TextureDataType dataType, final boolean minFilter, final boolean magFilter, final boolean wrapS, final boolean wrapT) {
    return new NoopTexture(name, w, h, internalFormat, dataFormat, dataType, minFilter, magFilter, wrapS, wrapT);
  }

  @Override
  public FrameBuffer makeFrameBuffer(final FrameBufferAttachment[] attachments) {
    return new NoopFrameBuffer();
  }

  @Override
  public <Options extends ShaderOptions> Shader<Options> makeShader(final Path vert, final Path frag, final Function<Shader<Options>, Supplier<Options>> options) {
    return new NoopShader<>(options);
  }

  @Override
  public <Options extends ShaderOptions> Shader<Options> makeShader(final Path vert, final Path geom, final Path frag, final Function<Shader<Options>, Supplier<Options>> options) throws IOException {
    return new NoopShader<>(options);
  }

  @Override
  public ShaderUniformBuffer makeUniformBuffer(final long size, final int binding) {
    return new NoopShaderUniformBuffer();
  }

  @Override
  public void clear(final boolean colour, final boolean depth, final boolean stencil) {

  }

  @Override
  public void clearColour(final float r, final float g, final float b) {

  }

  @Override
  public void viewport(final int x, final int y, final int w, final int h) {

  }

  @Override
  public void unbindFramebuffer() {

  }

  @Override
  public void unbindTexture() {

  }

  @Override
  public void initBatch(final RenderBatch batch) {

  }

  @Override
  public void backfaceCulling(final boolean enable) {

  }

  @Override
  public void enableDepthTest(final DepthComparator comparator) {

  }

  @Override
  public void disableDepthTest() {

  }

  @Override
  public void scissor(final QueuedModel<?, ?> model, final FloatBuffer scissorBuffer, final ShaderUniformBuffer scissorUniform) {

  }

  @Override
  public void translucency(@Nullable final Translucency translucency) {

  }

  @Override
  public void wireframe(final boolean enable) {

  }
}
