package legend.core.opengl;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import legend.game.types.Translucency;

import javax.annotation.Nullable;

import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL11C.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11C.glDrawArrays;
import static org.lwjgl.opengl.GL11C.glDrawElements;
import static org.lwjgl.opengl.GL12C.glDrawRangeElements;
import static org.lwjgl.opengl.GL15C.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15C.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15C.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15C.glBindBuffer;
import static org.lwjgl.opengl.GL15C.glBufferData;
import static org.lwjgl.opengl.GL15C.glDeleteBuffers;
import static org.lwjgl.opengl.GL15C.glGenBuffers;
import static org.lwjgl.opengl.GL20C.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

public class Mesh {
  private static final Int2ObjectMap<Mesh> usedVaos = new Int2ObjectOpenHashMap<>();
  private static final Int2ObjectMap<Mesh> usedVbos = new Int2ObjectOpenHashMap<>();

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

  public Mesh(final int mode, final float[] vertexData, final int[] indices) {
    this(mode, vertexData, indices, false, false, null);
  }

  public Mesh(final int mode, final float[] vertexData, final int[] indices, final boolean textured, final boolean translucent, @Nullable final Translucency translucencyMode) {
    this.textured = textured;
    this.translucent = translucent;
    this.translucencyMode = translucencyMode;
    this.count = indices.length;
    this.mode = mode;
    this.useIndices = true;

    this.vao = glGenVertexArrays();
    glBindVertexArray(this.vao);

    this.vbo = glGenBuffers();
    glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
    glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);

    this.ebo = glGenBuffers();
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.ebo);
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

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

  public Mesh(final int mode, final float[] vertexData, final int vertexCount) {
    this(mode, vertexData, vertexCount, false, false, null);
  }

  public Mesh(final int mode, final float[] vertexData, final int vertexCount, final boolean textured, final boolean translucent, @Nullable final Translucency translucencyMode) {
    this.textured = textured;
    this.translucent = translucent;
    this.translucencyMode = translucencyMode;
    this.count = vertexCount;
    this.mode = mode;
    this.useIndices = false;

    this.vao = glGenVertexArrays();
    glBindVertexArray(this.vao);

    this.vbo = glGenBuffers();
    glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
    glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);

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

  public void attribute(final int index, final long offset, final int size, final int stride) {
    glBindVertexArray(this.vao);
    glVertexAttribPointer(index, size, GL_FLOAT, false, stride * Float.BYTES, offset * Float.BYTES);
    glEnableVertexAttribArray(index);
    glBindVertexArray(0);
  }

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
}
