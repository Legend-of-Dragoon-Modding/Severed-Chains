package legend.core.renderer;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.function.Supplier;

public interface RenderApi {
  void init();
  void resize(final int renderWidth, final int renderHeight);
  Mesh makeMesh(final VertexOrder vertexOrder, final float[] vertexData, final int[] indices);
  Mesh makeMesh(final VertexOrder vertexOrder, final float[] vertexData, final int[] indices, final boolean textured, final boolean translucent, @Nullable final Translucency translucencyMode, final BufferUsage bufferUsage);
  Mesh makeMesh(final VertexOrder vertexOrder, final float[] vertexData, final int vertexCount);
  Mesh makeMesh(final VertexOrder vertexOrder, final float[] vertexData, final int vertexCount, final boolean textured, final boolean translucent, @Nullable final Translucency translucencyMode, final BufferUsage bufferUsage);
  Texture makeTexture(@Nullable final Buffer buffer, final String name, final int w, final int h, final TextureInternalFormat internalFormat, final TextureDataFormat dataFormat, final TextureDataType dataType, final boolean minFilter, final boolean magFilter, final boolean wrapS, final boolean wrapT);
  FrameBuffer makeFrameBuffer(final FrameBufferAttachment[] attachments);
  <Options extends ShaderOptions> Shader<Options> makeShader(final Path vert, final Path frag, final Function<Shader<Options>, Supplier<Options>> options) throws IOException;
  <Options extends ShaderOptions> Shader<Options> makeShader(final Path vert, final Path geom, final Path frag, final Function<Shader<Options>, Supplier<Options>> options) throws IOException;

  ShaderUniformBuffer makeUniformBuffer(final long size, final int binding);

  void clear(final boolean colour, final boolean depth, final boolean stencil);
  void clearColour(final float r, final float g, final float b);

  void viewport(final int x, final int y, final int w, final int h);

  void unbindFramebuffer();
  void unbindTexture();

  void initBatch(final RenderBatch batch);
  void backfaceCulling(final boolean enable);
  void enableDepthTest(final DepthComparator comparator);
  void disableDepthTest();
  void scissor(final QueuedModel<?, ?> model, final FloatBuffer scissorBuffer, final ShaderUniformBuffer scissorUniform);
  void translucency(@Nullable final Translucency translucency);

  void wireframe(final boolean enable);
}
