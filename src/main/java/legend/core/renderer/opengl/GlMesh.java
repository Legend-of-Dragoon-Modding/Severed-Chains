package legend.core.renderer.opengl;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import legend.core.renderer.BufferUsage;
import legend.core.renderer.Mesh;
import legend.core.renderer.VertexOrder;
import legend.core.renderer.Translucency;

import javax.annotation.Nullable;

import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL11C.GL_LINES;
import static org.lwjgl.opengl.GL11C.GL_LINE_LOOP;
import static org.lwjgl.opengl.GL11C.GL_LINE_STRIP;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11C.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11C.glDrawArrays;
import static org.lwjgl.opengl.GL11C.glDrawElements;
import static org.lwjgl.opengl.GL12C.glDrawRangeElements;
import static org.lwjgl.opengl.GL15C.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15C.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15C.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15C.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15C.GL_STREAM_DRAW;
import static org.lwjgl.opengl.GL15C.glBindBuffer;
import static org.lwjgl.opengl.GL15C.glBufferData;
import static org.lwjgl.opengl.GL15C.glBufferSubData;
import static org.lwjgl.opengl.GL15C.glDeleteBuffers;
import static org.lwjgl.opengl.GL15C.glGenBuffers;
import static org.lwjgl.opengl.GL20C.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;
import static org.lwjgl.opengl.GL32C.GL_TRIANGLES_ADJACENCY;

public class GlMesh implements Mesh {
  private static final Int2ObjectMap<Mesh> usedVaos = new Int2ObjectOpenHashMap<>();
  private static final Int2ObjectMap<Mesh> usedVbos = new Int2ObjectOpenHashMap<>();

  public final float[] vertexData;
  public final boolean textured;
  public final boolean translucent;
  public final Translucency translucencyMode;

  private final int vao;
  private final int vbo;
  private final int ebo;
  private final int count;
  private final int mode;
  private final boolean useIndices;

  private boolean deleted;

  GlMesh(final VertexOrder vertexOrder, final float[] vertexData, final int[] indices, final boolean textured, final boolean translucent, @Nullable final Translucency translucencyMode, final BufferUsage bufferUsage) {
    this.vertexData = vertexData;
    this.textured = textured;
    this.translucent = translucent;
    this.translucencyMode = translucencyMode;
    this.count = indices.length;
    this.mode = switch(vertexOrder) {
      case TRIANGLES -> GL_TRIANGLES;
      case TRIANGLES_ADJACENCY -> GL_TRIANGLES_ADJACENCY;
      case TRIANGLE_STRIP -> GL_TRIANGLE_STRIP;
      case LINES -> GL_LINES;
      case LINE_LOOP -> GL_LINE_LOOP;
      case LINE_STRIP -> GL_LINE_STRIP;
    };
    this.useIndices = true;

    final int usage = this.getUsage(bufferUsage);

    this.vao = glGenVertexArrays();
    glBindVertexArray(this.vao);

    this.vbo = glGenBuffers();
    glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
    glBufferData(GL_ARRAY_BUFFER, vertexData, usage);

    this.ebo = glGenBuffers();
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.ebo);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, usage);

    glBindVertexArray(0);

    if(usedVaos.containsKey(this.vao)) {
      throw new RuntimeException("Allocated already-used VAO " + this.vao);
    }

    if(usedVbos.containsKey(this.vbo)) {
      throw new RuntimeException("Allocated already-used VBO " + this.vbo);
    }

    usedVaos.put(this.vao, this);
    usedVbos.put(this.vbo, this);
  }

  GlMesh(final VertexOrder vertexOrder, final float[] vertexData, final int vertexCount, final boolean textured, final boolean translucent, @Nullable final Translucency translucencyMode, final BufferUsage bufferUsage) {
    this.vertexData = vertexData;
    this.textured = textured;
    this.translucent = translucent;
    this.translucencyMode = translucencyMode;
    this.count = vertexCount;
    this.mode = switch(vertexOrder) {
      case TRIANGLES -> GL_TRIANGLES;
      case TRIANGLES_ADJACENCY -> GL_TRIANGLES_ADJACENCY;
      case TRIANGLE_STRIP -> GL_TRIANGLE_STRIP;
      case LINES -> GL_LINES;
      case LINE_LOOP -> GL_LINE_LOOP;
      case LINE_STRIP -> GL_LINE_STRIP;
    };
    this.useIndices = false;

    this.vao = glGenVertexArrays();
    glBindVertexArray(this.vao);

    this.vbo = glGenBuffers();
    glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
    glBufferData(GL_ARRAY_BUFFER, vertexData, this.getUsage(bufferUsage));

    this.ebo = -1;

    glBindVertexArray(0);

    if(usedVaos.containsKey(this.vao)) {
      throw new RuntimeException("Allocated already-used VAO " + this.vao);
    }

    if(usedVbos.containsKey(this.vbo)) {
      throw new RuntimeException("Allocated already-used VBO " + this.vbo);
    }

    usedVaos.put(this.vao, this);
    usedVbos.put(this.vbo, this);
  }

  private int getUsage(final BufferUsage bufferUsage) {
    return switch(bufferUsage) {
      case STREAMING -> GL_STREAM_DRAW;
      case STATIC -> GL_STATIC_DRAW;
      case DYNAMIC -> GL_DYNAMIC_DRAW;
    };
  }

  @Override
  public void update() {
    if(this.deleted) {
      return;
    }

    glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
    glBufferSubData(GL_ARRAY_BUFFER, 0L, this.vertexData);
  }

  @Override
  public void delete() {
    this.deleted = true;

    if(this.ebo != -1) {
      glDeleteBuffers(this.ebo);
    }

    glDeleteBuffers(this.vbo);
    glDeleteVertexArrays(this.vao);

    usedVaos.remove(this.vao);
    usedVbos.remove(this.vbo);
  }

  @Override
  public void attribute(final int index, final long offset, final int size, final int stride) {
    glBindVertexArray(this.vao);
    glVertexAttribPointer(index, size, GL_FLOAT, false, stride * Float.BYTES, offset * Float.BYTES);
    glEnableVertexAttribArray(index);
    glBindVertexArray(0);
  }

  @Override
  public void draw() {
    if(this.deleted) {
      return;
    }

    glBindVertexArray(this.vao);

    if(this.useIndices) {
      glDrawElements(this.mode, this.count, GL_UNSIGNED_INT, 0L);
    } else {
      glDrawArrays(this.mode, 0, this.count);
    }
  }

  @Override
  public void draw(final int start, final int count) {
    if(this.deleted) {
      return;
    }

    if(count == 0) {
      this.draw();
      return;
    }

    glBindVertexArray(this.vao);

    if(this.useIndices) {
      glDrawRangeElements(this.mode, start, start + count - 1, count, GL_UNSIGNED_INT, 0L);
    } else {
      glDrawArrays(this.mode, start, count);
    }
  }

  @Override
  public float[] vertices() {
    return this.vertexData;
  }

  @Override
  public boolean textured() {
    return this.textured;
  }

  @Override
  public boolean translucent() {
    return this.translucent;
  }

  @Override
  public Translucency translucencyMode() {
    return this.translucencyMode;
  }
}
