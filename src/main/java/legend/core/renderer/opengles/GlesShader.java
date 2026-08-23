package legend.core.renderer.opengles;

import legend.core.memory.types.IntRef;
import legend.core.renderer.Shader;
import legend.core.renderer.ShaderManager;
import legend.core.renderer.ShaderOptions;
import legend.core.renderer.ShaderStage;
import legend.core.renderer.ShaderUniformFloat;
import legend.core.renderer.ShaderUniformInt;
import legend.core.renderer.ShaderUniformMat4;
import legend.core.renderer.ShaderUniformVec2;
import legend.core.renderer.ShaderUniformVec3;
import legend.core.renderer.ShaderUniformVec4;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4fc;
import org.joml.Vector2fc;
import org.joml.Vector3fc;
import org.joml.Vector4fc;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.lwjgl.opengles.GLES20.GL_COMPILE_STATUS;
import static org.lwjgl.opengles.GLES20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengles.GLES20.GL_LINK_STATUS;
import static org.lwjgl.opengles.GLES20.GL_NO_ERROR;
import static org.lwjgl.opengles.GLES20.GL_VERTEX_SHADER;
import static org.lwjgl.opengles.GLES20.glAttachShader;
import static org.lwjgl.opengles.GLES20.glCompileShader;
import static org.lwjgl.opengles.GLES20.glCreateProgram;
import static org.lwjgl.opengles.GLES20.glCreateShader;
import static org.lwjgl.opengles.GLES20.glDeleteProgram;
import static org.lwjgl.opengles.GLES20.glDeleteShader;
import static org.lwjgl.opengles.GLES20.glGetError;
import static org.lwjgl.opengles.GLES20.glGetProgramInfoLog;
import static org.lwjgl.opengles.GLES20.glGetProgrami;
import static org.lwjgl.opengles.GLES20.glGetShaderInfoLog;
import static org.lwjgl.opengles.GLES20.glGetShaderi;
import static org.lwjgl.opengles.GLES20.glGetUniformLocation;
import static org.lwjgl.opengles.GLES20.glLinkProgram;
import static org.lwjgl.opengles.GLES20.glShaderSource;
import static org.lwjgl.opengles.GLES20.glUniform1f;
import static org.lwjgl.opengles.GLES20.glUniform1i;
import static org.lwjgl.opengles.GLES20.glUniform2f;
import static org.lwjgl.opengles.GLES20.glUniform2fv;
import static org.lwjgl.opengles.GLES20.glUniform3f;
import static org.lwjgl.opengles.GLES20.glUniform3fv;
import static org.lwjgl.opengles.GLES20.glUniform4f;
import static org.lwjgl.opengles.GLES20.glUniform4fv;
import static org.lwjgl.opengles.GLES20.glUniformMatrix4fv;
import static org.lwjgl.opengles.GLES20.glUseProgram;
import static org.lwjgl.opengles.GLES30.GL_INVALID_INDEX;
import static org.lwjgl.opengles.GLES30.glGetUniformBlockIndex;
import static org.lwjgl.opengles.GLES30.glUniformBlockBinding;
import static org.lwjgl.opengles.GLES32.GL_GEOMETRY_SHADER;

public class GlesShader<Options extends ShaderOptions> implements Shader<Options> {
  private static final Logger LOGGER = LogManager.getFormatterLogger(GlesShader.class);

  private final Map<Path, ShaderStage> stages = new HashMap<>();
  private final Function<Shader<Options>, Supplier<Options>> optionsSupplier;
  private Supplier<Options> options;
  private int shader = -1;

  GlesShader(final Path vert, final Path frag, final Function<Shader<Options>, Supplier<Options>> options) throws IOException {
    LOGGER.info("Compiling shader vs[%s] fs[%s]", vert, frag);

    this.stages.put(vert, ShaderStage.VERTEX);
    this.stages.put(frag, ShaderStage.FRAGMENT);
    this.optionsSupplier = options;
    this.reload();
  }

  GlesShader(final Path vert, final Path geom, final Path frag, final Function<Shader<Options>, Supplier<Options>> options) throws IOException {
    LOGGER.info("Compiling shader vs[%s] gs[%s] fs[%s]", vert, geom, frag);

    this.stages.put(vert, ShaderStage.VERTEX);
    this.stages.put(geom, ShaderStage.GEOMETRY);
    this.stages.put(frag, ShaderStage.FRAGMENT);
    this.optionsSupplier = options;
    this.reload();
  }

  @Override
  public void reload() throws IOException {
    final int[] stages = new int[this.stages.size()];
    boolean error = false;
    int i = 0;
    final IntRef uniformIndex = new IntRef();

    for(final var entry : this.stages.entrySet()) {
      stages[i] = this.compileShader(entry.getKey(), entry.getValue(), uniformIndex);

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

  private int compileShader(final Path file, final ShaderStage stage, final IntRef uniformIndex) throws IOException {
    final String transpiled = ShaderManager.transpileShader(Files.readString(file), stage, uniformIndex);

    final int type = switch(stage) {
      case VERTEX -> GL_VERTEX_SHADER;
      case GEOMETRY -> GL_GEOMETRY_SHADER;
      case FRAGMENT -> GL_FRAGMENT_SHADER;
    };

    final int shader = glCreateShader(type);
    glShaderSource(shader, transpiled);
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

  @Override
  public Options makeOptions() {
    return this.options.get();
  }

  @Override
  public void bindUniformBlock(final CharSequence name, final int binding) {
    final int index = glGetUniformBlockIndex(this.shader, name);

    if(index == GL_INVALID_INDEX) {
      LOGGER.error("Uniform block %s not found in shader %d", name, this.shader);
    } else {
      glUniformBlockBinding(this.shader, index, binding);
    }
  }

  @Override
  public void use() {
    glUseProgram(this.shader);
  }

  @Override
  public void delete() {
    glDeleteProgram(this.shader);
    this.shader = -1;
  }

  @Override
  public ShaderUniformVec2 uniformVec2(final String name) {
    return new UniformVec2(name);
  }

  @Override
  public ShaderUniformVec3 uniformVec3(final String name) {
    return new UniformVec3(name);
  }

  @Override
  public ShaderUniformVec4 uniformVec4(final String name) {
    return new UniformVec4(name);
  }

  @Override
  public ShaderUniformMat4 uniformMat4(final String name) {
    return new UniformMat4(name);
  }

  @Override
  public ShaderUniformInt uniformInt(final String name) {
    return new UniformInt(name);
  }

  @Override
  public ShaderUniformFloat uniformFloat(final String name) {
    return new UniformFloat(name);
  }

  private class Uniform {
    final int loc;

    private Uniform(final String name) {
      this.loc = glGetUniformLocation(GlesShader.this.shader, name);

      if(this.loc == GL_INVALID_INDEX) {
        LOGGER.error("Uniform %s not found in shader %d", name, GlesShader.this.shader);
      }
    }
  }

  public class UniformVec2 extends Uniform implements ShaderUniformVec2 {
    private UniformVec2(final String name) {
      super(name);
    }

    @Override
    public void set(final FloatBuffer buffer) {
      glUniform2fv(this.loc, buffer);
    }

    @Override
    public void set(final Vector2fc vec) {
      glUniform2f(this.loc, vec.x(), vec.y());
    }

    @Override
    public void set(final float x, final float y) {
      glUniform2f(this.loc, x, y);
    }
  }

  public class UniformVec3 extends Uniform implements ShaderUniformVec3 {
    private UniformVec3(final String name) {
      super(name);
    }

    @Override
    public void set(final FloatBuffer buffer) {
      glUniform3fv(this.loc, buffer);
    }

    @Override
    public void set(final Vector3fc vec) {
      glUniform3f(this.loc, vec.x(), vec.y(), vec.z());
    }

    @Override
    public void set(final float x, final float y, final float z) {
      glUniform3f(this.loc, x, y, z);
    }
  }

  public class UniformVec4 extends Uniform implements ShaderUniformVec4 {
    private UniformVec4(final String name) {
      super(name);
    }

    @Override
    public void set(final FloatBuffer buffer) {
      glUniform4fv(this.loc, buffer);
    }

    @Override
    public void set(final Vector4fc vec) {
      glUniform4f(this.loc, vec.x(), vec.y(), vec.z(), vec.w());
    }

    @Override
    public void set(final float x, final float y, final float z, final float w) {
      glUniform4f(this.loc, x, y, z, w);
    }
  }

  private final FloatBuffer uniformMatrixBuffer = BufferUtils.createFloatBuffer(4 * 4);

  public class UniformMat4 extends Uniform implements ShaderUniformMat4 {
    private UniformMat4(final String name) {
      super(name);
    }

    @Override
    public void set(final Matrix4fc mat) {
      this.set(mat, false);
    }

    @Override
    public void set(final Matrix4fc mat, final boolean transpose) {
      mat.get(GlesShader.this.uniformMatrixBuffer);
      this.set(GlesShader.this.uniformMatrixBuffer, transpose);
    }

    @Override
    public void set(final FloatBuffer mat) {
      this.set(mat, false);
    }

    @Override
    public void set(final FloatBuffer mat, final boolean transpose) {
      glUniformMatrix4fv(this.loc, transpose, mat);
    }
  }

  public class UniformInt extends Uniform implements ShaderUniformInt {
    private UniformInt(final String name) {
      super(name);
    }

    @Override
    public void set(final int val) {
      glUniform1i(this.loc, val);
    }
  }

  public class UniformFloat extends Uniform implements ShaderUniformFloat {
    private UniformFloat(final String name) {
      super(name);
    }

    @Override
    public void set(final float val) {
      glUniform1f(this.loc, val);
    }
  }
}
