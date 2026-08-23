package legend.core.renderer;

import legend.core.memory.types.IntRef;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.spvc.Spvc;
import org.lwjgl.util.spvc.SpvcReflectedResource;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.util.shaderc.Shaderc.shaderc_compilation_status_success;
import static org.lwjgl.util.shaderc.Shaderc.shaderc_compile_into_spv;
import static org.lwjgl.util.shaderc.Shaderc.shaderc_compile_options_initialize;
import static org.lwjgl.util.shaderc.Shaderc.shaderc_compile_options_release;
import static org.lwjgl.util.shaderc.Shaderc.shaderc_compile_options_set_auto_bind_uniforms;
import static org.lwjgl.util.shaderc.Shaderc.shaderc_compile_options_set_auto_map_locations;
import static org.lwjgl.util.shaderc.Shaderc.shaderc_compile_options_set_target_env;
import static org.lwjgl.util.shaderc.Shaderc.shaderc_compiler_initialize;
import static org.lwjgl.util.shaderc.Shaderc.shaderc_compiler_release;
import static org.lwjgl.util.shaderc.Shaderc.shaderc_env_version_opengl_4_5;
import static org.lwjgl.util.shaderc.Shaderc.shaderc_fragment_shader;
import static org.lwjgl.util.shaderc.Shaderc.shaderc_geometry_shader;
import static org.lwjgl.util.shaderc.Shaderc.shaderc_result_get_bytes;
import static org.lwjgl.util.shaderc.Shaderc.shaderc_result_get_compilation_status;
import static org.lwjgl.util.shaderc.Shaderc.shaderc_result_get_error_message;
import static org.lwjgl.util.shaderc.Shaderc.shaderc_result_release;
import static org.lwjgl.util.shaderc.Shaderc.shaderc_target_env_opengl;
import static org.lwjgl.util.shaderc.Shaderc.shaderc_vertex_shader;
import static org.lwjgl.util.spvc.Spv.SpvDecorationBinding;
import static org.lwjgl.util.spvc.Spv.SpvDecorationLocation;
import static org.lwjgl.util.spvc.Spvc.SPVC_BACKEND_GLSL;
import static org.lwjgl.util.spvc.Spvc.SPVC_CAPTURE_MODE_TAKE_OWNERSHIP;
import static org.lwjgl.util.spvc.Spvc.SPVC_COMPILER_OPTION_GLSL_ES;
import static org.lwjgl.util.spvc.Spvc.SPVC_COMPILER_OPTION_GLSL_VERSION;
import static org.lwjgl.util.spvc.Spvc.SPVC_RESOURCE_TYPE_GL_PLAIN_UNIFORM;
import static org.lwjgl.util.spvc.Spvc.SPVC_RESOURCE_TYPE_UNIFORM_BUFFER;
import static org.lwjgl.util.spvc.Spvc.spvc_compiler_compile;
import static org.lwjgl.util.spvc.Spvc.spvc_compiler_create_compiler_options;
import static org.lwjgl.util.spvc.Spvc.spvc_compiler_install_compiler_options;
import static org.lwjgl.util.spvc.Spvc.spvc_compiler_options_set_bool;
import static org.lwjgl.util.spvc.Spvc.spvc_compiler_options_set_uint;
import static org.lwjgl.util.spvc.Spvc.spvc_compiler_unset_decoration;
import static org.lwjgl.util.spvc.Spvc.spvc_context_create;
import static org.lwjgl.util.spvc.Spvc.spvc_context_create_compiler;
import static org.lwjgl.util.spvc.Spvc.spvc_context_destroy;
import static org.lwjgl.util.spvc.Spvc.spvc_context_parse_spirv;
import static org.lwjgl.util.spvc.Spvc.spvc_resources_get_resource_list_for_type;

public final class ShaderManager {
  private ShaderManager() { }

  private static final Map<ShaderType, Shader> shaders = new HashMap<>();
  private static final Map<String, ShaderUniformBuffer> uniformBuffers = new HashMap<>();

  public static String transpileShader(final String source, final ShaderStage shaderStage, final IntRef uniformIndex) {
    final ByteBuffer spirv = compileShaderToSpirv(source, shaderStage);
    return decompileSpirvToGles(spirv, uniformIndex);
  }

  private static ByteBuffer compileShaderToSpirv(final String source, final ShaderStage shaderStage) {
    final long compiler = shaderc_compiler_initialize();
    final long options = shaderc_compile_options_initialize();

    shaderc_compile_options_set_target_env(options, shaderc_target_env_opengl, shaderc_env_version_opengl_4_5);
    shaderc_compile_options_set_auto_map_locations(options, true);
    shaderc_compile_options_set_auto_bind_uniforms(options, true);

    final int stage = switch(shaderStage) {
      case VERTEX -> shaderc_vertex_shader;
      case GEOMETRY -> shaderc_geometry_shader;
      case FRAGMENT -> shaderc_fragment_shader;
    };

    final long result = shaderc_compile_into_spv(compiler, source, stage, "shader.glsl", "main", options);
    if(shaderc_result_get_compilation_status(result) != shaderc_compilation_status_success) {
      throw new RuntimeException("Shaderc compilation failed: " + shaderc_result_get_error_message(result));
    }

    final ByteBuffer nativeBuf = shaderc_result_get_bytes(result);
    final ByteBuffer copy = ByteBuffer.allocateDirect(nativeBuf.remaining());
    copy.put(nativeBuf);
    copy.flip();

    shaderc_result_release(result);
    shaderc_compile_options_release(options);
    shaderc_compiler_release(compiler);

    return copy;
  }

  private static String decompileSpirvToGles(final ByteBuffer spirvBuffer, final IntRef uniformIndex) {
    try(final MemoryStack stack = MemoryStack.stackPush()) {
      final IntBuffer spirvInts = spirvBuffer.asIntBuffer();

      final PointerBuffer contextBuffer = stack.mallocPointer(1);
      final PointerBuffer parsedIrBuffer = stack.mallocPointer(1);
      final PointerBuffer compilerBuffer = stack.mallocPointer(1);

      spvc_context_create(contextBuffer);
      final long context = contextBuffer.get(0);

      spvc_context_parse_spirv(context, spirvInts, spirvInts.remaining(), parsedIrBuffer);
      final long parsedIr = parsedIrBuffer.get(0);

      spvc_context_create_compiler(context, SPVC_BACKEND_GLSL, parsedIr, SPVC_CAPTURE_MODE_TAKE_OWNERSHIP, compilerBuffer);
      final long compiler = compilerBuffer.get(0);

      final PointerBuffer optionsBuffer = stack.mallocPointer(1);
      spvc_compiler_create_compiler_options(compiler, optionsBuffer);
      final long options = optionsBuffer.get(0);

      spvc_compiler_options_set_bool(options, SPVC_COMPILER_OPTION_GLSL_ES, true);
      spvc_compiler_options_set_uint(options, SPVC_COMPILER_OPTION_GLSL_VERSION, 320);
      spvc_compiler_install_compiler_options(compiler, options);

      // shaderc won't compile opengl shaders without locations on uniforms and bindings on UBOs. We tell it to assign them automatically, but it'll assign the same
      // indices to different stages in the same shader program. Let it do that so that it'll compile, but remove them when we decompile into gles shaders. Both
      // gl and gles work just fine without them.
      final PointerBuffer resourcesBuffer = stack.mallocPointer(1);
      Spvc.spvc_compiler_create_shader_resources(compiler, resourcesBuffer);
      final long resources = resourcesBuffer.get(0);

      final PointerBuffer resourceListBuffer = stack.mallocPointer(1);
      final PointerBuffer resourceCountBuffer = stack.mallocPointer(1);

      spvc_resources_get_resource_list_for_type(resources, SPVC_RESOURCE_TYPE_GL_PLAIN_UNIFORM, resourceListBuffer, resourceCountBuffer);
      final long uniformList = resourceListBuffer.get(0);
      final long uniformCount = resourceCountBuffer.get(0);

      spvc_resources_get_resource_list_for_type(resources, SPVC_RESOURCE_TYPE_UNIFORM_BUFFER, resourceListBuffer, resourceCountBuffer);
      final long bufferList = resourceListBuffer.get(0);
      final long bufferCount = resourceCountBuffer.get(0);

      removeBindings(compiler, uniformList, uniformCount, SpvDecorationLocation, uniformIndex);
      removeBindings(compiler, bufferList, bufferCount, SpvDecorationBinding, uniformIndex);

      // convert to gles
      final PointerBuffer sourceBuffer = stack.mallocPointer(1);
      spvc_compiler_compile(compiler, sourceBuffer);

      final String finalGlesCode = sourceBuffer.getStringUTF8(0);

      // clean up
      spvc_context_destroy(context);

      return finalGlesCode;
    }
  }

  private static void removeBindings(final long compiler, final long list, final long count, final int decoration, final IntRef uniformIndex) {
    for(int i = 0; i < count; i++) {
      final SpvcReflectedResource resource = SpvcReflectedResource.create(list + i * SpvcReflectedResource.SIZEOF);
      final int variableId = resource.id();

      spvc_compiler_unset_decoration(compiler, variableId, decoration);
      uniformIndex.incr();
    }
  }

  public static <Options extends ShaderOptions> Shader<Options> getShader(final ShaderType<Options> type) {
    return shaders.get(type);
  }

  public static <Options extends ShaderOptions> Shader<Options> addShader(final ShaderType<Options> type) {
    final Shader<Options> shader = type.shaderConstructor.apply(type.optionsConstructor);
    shaders.put(type, shader);
    return shader;
  }

  public static ShaderUniformBuffer getUniformBuffer(final String name) {
    return uniformBuffers.get(name);
  }

  public static ShaderUniformBuffer addUniformBuffer(final String name, final ShaderUniformBuffer uniformBuffer) {
    uniformBuffers.put(name, uniformBuffer);
    return uniformBuffer;
  }

  public static void reload() throws IOException {
    for(final Shader<?> shader : shaders.values()) {
      shader.reload();
    }
  }

  public static void delete() {
    for(final Shader<?> shader : shaders.values()) {
      shader.delete();
    }

    for(final ShaderUniformBuffer buffer : uniformBuffers.values()) {
      buffer.delete();
    }

    shaders.clear();
    uniformBuffers.clear();
  }
}
