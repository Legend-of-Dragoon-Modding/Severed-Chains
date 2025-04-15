package legend.core.opengl;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector2fc;
import org.joml.Vector3fc;
import org.joml.Vector4fc;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.lwjgl.opengl.GL11C.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11C.glGetError;
import static org.lwjgl.opengl.GL15C.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15C.glBindBuffer;
import static org.lwjgl.opengl.GL15C.glBufferData;
import static org.lwjgl.opengl.GL15C.glBufferSubData;
import static org.lwjgl.opengl.GL15C.glDeleteBuffers;
import static org.lwjgl.opengl.GL15C.glGenBuffers;
import static org.lwjgl.opengl.GL20C.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20C.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20C.GL_LINK_STATUS;
import static org.lwjgl.opengl.GL20C.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20C.glAttachShader;
import static org.lwjgl.opengl.GL20C.glCompileShader;
import static org.lwjgl.opengl.GL20C.glCreateProgram;
import static org.lwjgl.opengl.GL20C.glCreateShader;
import static org.lwjgl.opengl.GL20C.glDeleteProgram;
import static org.lwjgl.opengl.GL20C.glDeleteShader;
import static org.lwjgl.opengl.GL20C.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20C.glGetProgrami;
import static org.lwjgl.opengl.GL20C.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20C.glGetShaderi;
import static org.lwjgl.opengl.GL20C.glGetUniformLocation;
import static org.lwjgl.opengl.GL20C.glLinkProgram;
import static org.lwjgl.opengl.GL20C.glShaderSource;
import static org.lwjgl.opengl.GL20C.glUniform1f;
import static org.lwjgl.opengl.GL20C.glUniform1i;
import static org.lwjgl.opengl.GL20C.glUniform2f;
import static org.lwjgl.opengl.GL20C.glUniform2fv;
import static org.lwjgl.opengl.GL20C.glUniform3f;
import static org.lwjgl.opengl.GL20C.glUniform3fv;
import static org.lwjgl.opengl.GL20C.glUniform4f;
import static org.lwjgl.opengl.GL20C.glUniform4fv;
import static org.lwjgl.opengl.GL20C.glUseProgram;
import static org.lwjgl.opengl.GL30C.glBindBufferBase;
import static org.lwjgl.opengl.GL31C.GL_INVALID_INDEX;
import static org.lwjgl.opengl.GL31C.GL_UNIFORM_BUFFER;
import static org.lwjgl.opengl.GL31C.glGetUniformBlockIndex;
import static org.lwjgl.opengl.GL31C.glUniformBlockBinding;
import static org.lwjgl.opengl.GL32C.GL_GEOMETRY_SHADER;

public class Shader<Options extends ShaderOptions<Options>> {
  private static final Logger LOGGER = LogManager.getFormatterLogger(Shader.class);

  private final Object2IntMap<Path> stages = new Object2IntOpenHashMap<>();
  private final Function<Shader<Options>, Supplier<Options>> optionsSupplier;
  private Supplier<Options> options;
  private int shader = -1;

  public Shader(final Path vert, final Path frag, final Function<Shader<Options>, Supplier<Options>> options) throws IOException {
    LOGGER.info("Compiling shader vs[%s] fs[%s]", vert, frag);

    this.stages.put(vert, GL_VERTEX_SHADER);
    this.stages.put(frag, GL_FRAGMENT_SHADER);
    this.optionsSupplier = options;
    this.reload();
  }

  public Shader(final Path vert, final Path geom, final Path frag, final Function<Shader<Options>, Supplier<Options>> options) throws IOException {
    LOGGER.info("Compiling shader vs[%s] gs[%s] fs[%s]", vert, geom, frag);

    this.stages.put(vert, GL_VERTEX_SHADER);
    this.stages.put(geom, GL_GEOMETRY_SHADER);
    this.stages.put(frag, GL_FRAGMENT_SHADER);
    this.optionsSupplier = options;
    this.reload();
  }

  public void reload() throws IOException {
    final int[] stages = new int[this.stages.size()];
    boolean error = false;
    int i = 0;

    for(final var entry : this.stages.object2IntEntrySet()) {
      stages[i] = this.compileShader(entry.getKey(), entry.getIntValue());

      if(stages[i] == 0) {
        error = true;
        break;
      }

      i++;
    }

    // Clear out errors
    while(glGetError() != GL_NO_ERROR) {
      // do nothing
    }

    // Delete stages that were compiled and bail
    if(error) {
      this.deleteShaders(stages);
      return;
    }

    // Delete the old shader after loading the parts of the new one so
    // that we can keep using the old one if the new one fails to load
    if(this.shader != -1) {
      this.delete();
    }

    this.shader = this.linkProgram(stages);
    this.deleteShaders(stages);
    this.options = this.optionsSupplier.apply(this);
  }

  private void deleteShaders(final int[] shaders) {
    for(int i = 0; i < shaders.length; i++) {
      if(shaders[i] != 0) {
        glDeleteShader(shaders[i]);
      }
    }
  }

  private int compileShader(final Path file, final int type) throws IOException {
    final int shader = glCreateShader(type);
    glShaderSource(shader, Files.readString(file));
    glCompileShader(shader);

    if(glGetShaderi(shader, GL_COMPILE_STATUS) == 0) {
      LOGGER.error("Shader compile error %s: %s", file, glGetShaderInfoLog(shader));
    }

    return shader;
  }

  private int linkProgram(final int[] stages) {
    final int shader = glCreateProgram();

    for(int i = 0; i < stages.length; i++) {
      glAttachShader(shader, stages[i]);
    }

    glLinkProgram(shader);

    if(glGetProgrami(shader, GL_LINK_STATUS) == 0) {
      LOGGER.error("Program link error: %s", glGetProgramInfoLog(shader));
    }

    return shader;
  }

  public Options makeOptions() {
    return this.options.get();
  }

  public void bindUniformBlock(final CharSequence name, final int binding) {
    final int index = glGetUniformBlockIndex(this.shader, name);

    if(index == GL_INVALID_INDEX) {
      LOGGER.error("Uniform block %s not found in shader %d", name, this.shader);
    } else {
      glUniformBlockBinding(this.shader, index, binding);
    }
  }

  public void use() {
    glUseProgram(this.shader);
  }

  public void delete() {
    glDeleteProgram(this.shader);
    this.shader = -1;
  }

  private class Uniform {
    final int loc;

    private Uniform(final String name) {
      this.loc = glGetUniformLocation(Shader.this.shader, name);

      if(this.loc == GL_INVALID_INDEX) {
        LOGGER.error("Uniform %s not found in shader %d", name, Shader.this.shader);
      }
    }
  }

  public static class UniformBuffer {
    public static final int TRANSFORM = 0;
    public static final int TRANSFORM2 = 1;
    public static final int LIGHTING = 2;
    public static final int PROJECTION_INFO = 3;
    public static final int VDF = 4;

    private final int id;

    public UniformBuffer(final long size, final int binding) {
      this.id = glGenBuffers();

      glBindBuffer(GL_UNIFORM_BUFFER, this.id);
      glBufferData(GL_UNIFORM_BUFFER, size, GL_DYNAMIC_DRAW);
      glBindBuffer(GL_UNIFORM_BUFFER, 0);

      glBindBufferBase(GL_UNIFORM_BUFFER, binding, this.id);
    }

    public void delete() {
      glDeleteBuffers(this.id);
    }

    public void set(final FloatBuffer buffer) {
      this.set(0L, buffer);
    }

    public void set(final long offset, final FloatBuffer buffer) {
      glBindBuffer(GL_UNIFORM_BUFFER, this.id);
      glBufferSubData(GL_UNIFORM_BUFFER, offset, buffer);
      glBindBuffer(GL_UNIFORM_BUFFER, 0);
    }
  }

  public class UniformVec2 extends Uniform {
    public UniformVec2(final String name) {
      super(name);
    }

    public void set(final FloatBuffer buffer) {
      glUniform2fv(this.loc, buffer);
    }

    public void set(final Vector2fc vec) {
      glUniform2f(this.loc, vec.x(), vec.y());
    }

    public void set(final float x, final float y) {
      glUniform2f(this.loc, x, y);
    }
  }

  public class UniformVec3 extends Uniform {
    public UniformVec3(final String name) {
      super(name);
    }

    public void set(final FloatBuffer buffer) {
      glUniform3fv(this.loc, buffer);
    }

    public void set(final Vector3fc vec) {
      glUniform3f(this.loc, vec.x(), vec.y(), vec.z());
    }

    public void set(final float x, final float y, final float z) {
      glUniform3f(this.loc, x, y, z);
    }
  }

  public class UniformVec4 extends Uniform {
    public UniformVec4(final String name) {
      super(name);
    }

    public void set(final FloatBuffer buffer) {
      glUniform4fv(this.loc, buffer);
    }

    public void set(final Vector4fc vec) {
      glUniform4f(this.loc, vec.x(), vec.y(), vec.z(), vec.w());
    }

    public void set(final float x, final float y, final float z, final float w) {
      glUniform4f(this.loc, x, y, z, w);
    }
  }

  public class UniformInt extends Uniform {
    public UniformInt(final String name) {
      super(name);
    }

    public void set(final int val) {
      glUniform1i(this.loc, val);
    }
  }

  public class UniformFloat extends Uniform {
    public UniformFloat(final String name) {
      super(name);
    }

    public void set(final float val) {
      glUniform1f(this.loc, val);
    }
  }
}
