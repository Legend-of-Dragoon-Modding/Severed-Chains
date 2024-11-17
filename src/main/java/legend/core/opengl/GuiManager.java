package legend.core.opengl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.nuklear.NkAllocator;
import org.lwjgl.nuklear.NkBuffer;
import org.lwjgl.nuklear.NkContext;
import org.lwjgl.nuklear.NkConvertConfig;
import org.lwjgl.nuklear.NkDrawCommand;
import org.lwjgl.nuklear.NkDrawNullTexture;
import org.lwjgl.nuklear.NkDrawVertexLayoutElement;
import org.lwjgl.nuklear.NkMouse;
import org.lwjgl.nuklear.NkPluginFilterI;
import org.lwjgl.nuklear.NkVec2;
import org.lwjgl.nuklear.Nuklear;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Platform;

import java.nio.ByteBuffer;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Objects;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_B;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_C;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DELETE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_END;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_HOME;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_PAGE_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_V;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;
import static org.lwjgl.glfw.GLFW.GLFW_MOD_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_MOD_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_MIDDLE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.nuklear.Nuklear.NK_ANTI_ALIASING_ON;
import static org.lwjgl.nuklear.Nuklear.NK_BUTTON_LEFT;
import static org.lwjgl.nuklear.Nuklear.NK_BUTTON_MIDDLE;
import static org.lwjgl.nuklear.Nuklear.NK_BUTTON_RIGHT;
import static org.lwjgl.nuklear.Nuklear.NK_FORMAT_COUNT;
import static org.lwjgl.nuklear.Nuklear.NK_FORMAT_FLOAT;
import static org.lwjgl.nuklear.Nuklear.NK_FORMAT_R8G8B8A8;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_BACKSPACE;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_COPY;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_CUT;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_DEL;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_DOWN;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_ENTER;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_LEFT;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_PASTE;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_RIGHT;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_SCROLL_DOWN;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_SCROLL_END;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_SCROLL_START;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_SCROLL_UP;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_SHIFT;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_TAB;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_TEXT_END;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_TEXT_LINE_END;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_TEXT_LINE_START;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_TEXT_REDO;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_TEXT_START;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_TEXT_UNDO;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_TEXT_WORD_LEFT;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_TEXT_WORD_RIGHT;
import static org.lwjgl.nuklear.Nuklear.NK_KEY_UP;
import static org.lwjgl.nuklear.Nuklear.NK_VERTEX_ATTRIBUTE_COUNT;
import static org.lwjgl.nuklear.Nuklear.NK_VERTEX_COLOR;
import static org.lwjgl.nuklear.Nuklear.NK_VERTEX_POSITION;
import static org.lwjgl.nuklear.Nuklear.NK_VERTEX_TEXCOORD;
import static org.lwjgl.nuklear.Nuklear.nk__draw_begin;
import static org.lwjgl.nuklear.Nuklear.nk__draw_next;
import static org.lwjgl.nuklear.Nuklear.nk_buffer_free;
import static org.lwjgl.nuklear.Nuklear.nk_buffer_init;
import static org.lwjgl.nuklear.Nuklear.nk_buffer_init_fixed;
import static org.lwjgl.nuklear.Nuklear.nk_clear;
import static org.lwjgl.nuklear.Nuklear.nk_convert;
import static org.lwjgl.nuklear.Nuklear.nk_free;
import static org.lwjgl.nuklear.Nuklear.nk_init;
import static org.lwjgl.nuklear.Nuklear.nk_input_begin;
import static org.lwjgl.nuklear.Nuklear.nk_input_button;
import static org.lwjgl.nuklear.Nuklear.nk_input_end;
import static org.lwjgl.nuklear.Nuklear.nk_input_key;
import static org.lwjgl.nuklear.Nuklear.nk_input_motion;
import static org.lwjgl.nuklear.Nuklear.nk_input_scroll;
import static org.lwjgl.nuklear.Nuklear.nk_input_unicode;
import static org.lwjgl.nuklear.Nuklear.nk_style_set_font;
import static org.lwjgl.nuklear.Nuklear.nnk_strlen;
import static org.lwjgl.nuklear.Nuklear.nnk_textedit_paste;
import static org.lwjgl.opengl.GL11C.GL_BLEND;
import static org.lwjgl.opengl.GL11C.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11C.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL11C.GL_NEAREST;
import static org.lwjgl.opengl.GL11C.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.GL_RGBA;
import static org.lwjgl.opengl.GL11C.GL_RGBA8;
import static org.lwjgl.opengl.GL11C.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11C.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11C.GL_TRUE;
import static org.lwjgl.opengl.GL11C.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11C.GL_UNSIGNED_SHORT;
import static org.lwjgl.opengl.GL11C.glBindTexture;
import static org.lwjgl.opengl.GL11C.glBlendFunc;
import static org.lwjgl.opengl.GL11C.glDeleteTextures;
import static org.lwjgl.opengl.GL11C.glDisable;
import static org.lwjgl.opengl.GL11C.glDrawElements;
import static org.lwjgl.opengl.GL11C.glEnable;
import static org.lwjgl.opengl.GL11C.glGenTextures;
import static org.lwjgl.opengl.GL11C.glScissor;
import static org.lwjgl.opengl.GL11C.glTexImage2D;
import static org.lwjgl.opengl.GL11C.glTexParameteri;
import static org.lwjgl.opengl.GL11C.glViewport;
import static org.lwjgl.opengl.GL12C.GL_UNSIGNED_INT_8_8_8_8_REV;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13C.glActiveTexture;
import static org.lwjgl.opengl.GL14C.GL_FUNC_ADD;
import static org.lwjgl.opengl.GL14C.glBlendEquation;
import static org.lwjgl.opengl.GL15C.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15C.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15C.GL_STREAM_DRAW;
import static org.lwjgl.opengl.GL15C.GL_WRITE_ONLY;
import static org.lwjgl.opengl.GL15C.glBindBuffer;
import static org.lwjgl.opengl.GL15C.glBufferData;
import static org.lwjgl.opengl.GL15C.glDeleteBuffers;
import static org.lwjgl.opengl.GL15C.glGenBuffers;
import static org.lwjgl.opengl.GL15C.glMapBuffer;
import static org.lwjgl.opengl.GL15C.glUnmapBuffer;
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
import static org.lwjgl.opengl.GL20C.glDetachShader;
import static org.lwjgl.opengl.GL20C.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glGetAttribLocation;
import static org.lwjgl.opengl.GL20C.glGetProgrami;
import static org.lwjgl.opengl.GL20C.glGetShaderi;
import static org.lwjgl.opengl.GL20C.glGetUniformLocation;
import static org.lwjgl.opengl.GL20C.glLinkProgram;
import static org.lwjgl.opengl.GL20C.glShaderSource;
import static org.lwjgl.opengl.GL20C.glUniform1i;
import static org.lwjgl.opengl.GL20C.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20C.glUseProgram;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;
import static org.lwjgl.system.MemoryStack.stackGet;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAddress;
import static org.lwjgl.system.MemoryUtil.memAddressSafe;
import static org.lwjgl.system.MemoryUtil.memCopy;
import static org.lwjgl.system.MemoryUtil.nmemAllocChecked;
import static org.lwjgl.system.MemoryUtil.nmemFree;

public class GuiManager {
  private static final Logger LOGGER = LogManager.getLogger(GuiManager.class.getName());

  public static final long FILTER_ASCII = memAddressSafe((NkPluginFilterI)Nuklear::nnk_filter_ascii);
  public static final long FILTER_DECIMAL = memAddressSafe((NkPluginFilterI)Nuklear::nnk_filter_decimal);

  private static final int MAX_VERTEX_BUFFER = 512 * 1024;
  private static final int MAX_ELEMENT_BUFFER = 128 * 1024;
  private static final int BUFFER_INITIAL_SIZE = 4 * 1024;
  private static final NkAllocator ALLOCATOR;

  private static final NkDrawVertexLayoutElement.Buffer VERTEX_LAYOUT;

  static {
    LOGGER.info("Initialising Nuklear...");

    ALLOCATOR = NkAllocator.create()
      .alloc((handle, old, size) -> nmemAllocChecked(size))
      .mfree((handle, ptr) -> nmemFree(ptr));

    VERTEX_LAYOUT = NkDrawVertexLayoutElement.create(4)
      .position(0).attribute(NK_VERTEX_POSITION).format(NK_FORMAT_FLOAT).offset(0)
      .position(1).attribute(NK_VERTEX_TEXCOORD).format(NK_FORMAT_FLOAT).offset(8)
      .position(2).attribute(NK_VERTEX_COLOR).format(NK_FORMAT_R8G8B8A8).offset(16)
      .position(3).attribute(NK_VERTEX_ATTRIBUTE_COUNT).format(NK_FORMAT_COUNT).offset(0)
      .flip();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      LOGGER.info("Shutting down Nuklear...");

      ALLOCATOR.alloc().free();
      ALLOCATOR.mfree().free();
    }));
  }

  private final Deque<Gui> guiStack = new LinkedList<>();

  public final Window window;
  public final NkContext ctx;

  private final NkBuffer cmds = NkBuffer.create();
  private final NkDrawNullTexture nullTexture = NkDrawNullTexture.create();

  private int vbo, vao, ebo;
  private int prog;
  private int vertShdr;
  private int fragShdr;
  private int uniformTex;
  private int uniformProj;

  public GuiManager(final Window window) {
    this.window = window;
    this.ctx = NkContext.create();

    window.events.onMouseScroll(this::mouseScroll);
    window.events.onCharPress(this::charPress);
    window.events.onKeyPress(this::keyPress);
    window.events.onKeyRelease(this::keyRelease);
    window.events.onMouseMove(this::mouseMove);
    window.events.onMousePress(this::mousePress);
    window.events.onMouseRelease(this::mouseRelease);

    nk_init(this.ctx, ALLOCATOR, null);

    this.ctx
      .clip()

      .copy((handle, text, len) -> {
        if(len == 0) {
          return;
        }

        try(final MemoryStack stack = stackPush()) {
          final ByteBuffer str = stack.malloc(len + 1);
          memCopy(text, memAddress(str), len);
          str.put(len, (byte)0);

          window.setClipboardString(str);
        }
      })

      .paste((handle, edit) -> {
        final String string = window.getClipboardString();

        if(!string.isEmpty()) {
          try(final MemoryStack stack = stackGet()) {
            final int stackPointer = stack.getPointer();
            final long text;

            try {
              stack.nUTF8(string, true);
              text = stack.getPointerAddress();
            } finally {
              stack.setPointer(stackPointer);
            }

            nnk_textedit_paste(edit, text, nnk_strlen(text));
          }
        }
      });

    this.setupContext();
  }

  private void setupContext() {
    final String NK_SHADER_VERSION = Platform.get() == Platform.MACOSX ? "#version 150\n" : "#version 300 es\n";
    final String vertexShader =
      NK_SHADER_VERSION +
        "uniform mat4 ProjMtx;\n" +
        "in vec2 Position;\n" +
        "in vec2 TexCoord;\n" +
        "in vec4 Color;\n" +
        "out vec2 Frag_UV;\n" +
        "out vec4 Frag_Color;\n" +
        "void main() {\n" +
        "   Frag_UV = TexCoord;\n" +
        "   Frag_Color = Color;\n" +
        "   gl_Position = ProjMtx * vec4(Position.xy, 0, 1);\n" +
        "}\n";
    final String fragmentShader =
      NK_SHADER_VERSION +
        "precision mediump float;\n" +
        "uniform sampler2D Texture;\n" +
        "in vec2 Frag_UV;\n" +
        "in vec4 Frag_Color;\n" +
        "out vec4 Out_Color;\n" +
        "void main(){\n" +
        "   Out_Color = Frag_Color * texture(Texture, Frag_UV.st);\n" +
        "}\n";

    nk_buffer_init(this.cmds, ALLOCATOR, BUFFER_INITIAL_SIZE);
    this.prog = glCreateProgram();
    this.vertShdr = glCreateShader(GL_VERTEX_SHADER);
    this.fragShdr = glCreateShader(GL_FRAGMENT_SHADER);
    glShaderSource(this.vertShdr, vertexShader);
    glShaderSource(this.fragShdr, fragmentShader);
    glCompileShader(this.vertShdr);
    glCompileShader(this.fragShdr);
    if(glGetShaderi(this.vertShdr, GL_COMPILE_STATUS) != GL_TRUE) {
      throw new IllegalStateException();
    }
    if(glGetShaderi(this.fragShdr, GL_COMPILE_STATUS) != GL_TRUE) {
      throw new IllegalStateException();
    }
    glAttachShader(this.prog, this.vertShdr);
    glAttachShader(this.prog, this.fragShdr);
    glLinkProgram(this.prog);
    if(glGetProgrami(this.prog, GL_LINK_STATUS) != GL_TRUE) {
      throw new IllegalStateException();
    }

    this.uniformTex = glGetUniformLocation(this.prog, "Texture");
    this.uniformProj = glGetUniformLocation(this.prog, "ProjMtx");
    final int attrib_pos = glGetAttribLocation(this.prog, "Position");
    final int attrib_uv = glGetAttribLocation(this.prog, "TexCoord");
    final int attrib_col = glGetAttribLocation(this.prog, "Color");

    {
      // buffer setup
      this.vbo = glGenBuffers();
      this.ebo = glGenBuffers();
      this.vao = glGenVertexArrays();

      glBindVertexArray(this.vao);
      glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
      glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.ebo);

      glEnableVertexAttribArray(attrib_pos);
      glEnableVertexAttribArray(attrib_uv);
      glEnableVertexAttribArray(attrib_col);

      glVertexAttribPointer(attrib_pos, 2, GL_FLOAT, false, 20, 0);
      glVertexAttribPointer(attrib_uv, 2, GL_FLOAT, false, 20, 8);
      glVertexAttribPointer(attrib_col, 4, GL_UNSIGNED_BYTE, true, 20, 16);
    }

    {
      // null texture setup
      final int nullTexID = glGenTextures();

      this.nullTexture.texture().id(nullTexID);
      this.nullTexture.uv().set(0.5f, 0.5f);

      glBindTexture(GL_TEXTURE_2D, nullTexID);
      try(final MemoryStack stack = stackPush()) {
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, 1, 1, 0, GL_RGBA, GL_UNSIGNED_INT_8_8_8_8_REV, stack.ints(0xFFFFFFFF));
      }
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    }

    glBindTexture(GL_TEXTURE_2D, 0);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    glBindVertexArray(0);
  }

  public void setFont(final NuklearFont nuklearFont) {
    nk_style_set_font(this.ctx, nuklearFont.font);
  }

  public void captureInput() {
    nk_input_begin(this.ctx);
    glfwPollEvents();

    final NkMouse mouse = this.ctx.input().mouse();
    if(mouse.grab()) {
      this.window.hideCursor();
    } else if(mouse.grabbed()) {
      final float prevX = mouse.prev().x();
      final float prevY = mouse.prev().y();
      this.window.setCursorPos(prevX * this.window.getScale(), prevY * this.window.getScale());
      mouse.pos().x(prevX);
      mouse.pos().y(prevY);
    } else if(mouse.ungrab()) {
      this.window.showCursor();
    }

    nk_input_end(this.ctx);
  }

  public <T extends Gui> T pushGui(final T gui) {
    this.guiStack.push(gui);
    return gui;
  }

  public void popGui() {
    this.guiStack.pop();
  }

  public void removeGui(final Gui gui) {
    this.guiStack.remove(gui);
  }

  public void draw(final int displayWidth, final int displayHeight, final float width, final float height) {
    for(final Gui gui : this.guiStack) {
      gui.draw(this);
    }

    try(final MemoryStack stack = stackPush()) {
      // setup global state
      glEnable(GL_BLEND);
      glBlendEquation(GL_FUNC_ADD);
      glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
      glDisable(GL_CULL_FACE);
      glDisable(GL_DEPTH_TEST);
      glEnable(GL_SCISSOR_TEST);
      glActiveTexture(GL_TEXTURE0);

      // setup program
      glUseProgram(this.prog);
      glUniform1i(this.uniformTex, 0);
      glUniformMatrix4fv(this.uniformProj, false, stack.floats(
        2.0f / width, 0.0f, 0.0f, 0.0f,
        0.0f, -2.0f / height, 0.0f, 0.0f,
        0.0f, 0.0f, -1.0f, 0.0f,
        -1.0f, 1.0f, 0.0f, 1.0f
      ));
      glViewport(0, 0, displayWidth, displayHeight);
    }

    {
      // convert from command queue into draw list and draw to screen

      // allocate vertex and element buffer
      glBindVertexArray(this.vao);
      glBindBuffer(GL_ARRAY_BUFFER, this.vbo);
      glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.ebo);

      glBufferData(GL_ARRAY_BUFFER, MAX_VERTEX_BUFFER, GL_STREAM_DRAW);
      glBufferData(GL_ELEMENT_ARRAY_BUFFER, MAX_ELEMENT_BUFFER, GL_STREAM_DRAW);

      // load draw vertices & elements directly into vertex + element buffer
      final ByteBuffer vertices = Objects.requireNonNull(glMapBuffer(GL_ARRAY_BUFFER, GL_WRITE_ONLY, MAX_VERTEX_BUFFER, null));
      final ByteBuffer elements = Objects.requireNonNull(glMapBuffer(GL_ELEMENT_ARRAY_BUFFER, GL_WRITE_ONLY, MAX_ELEMENT_BUFFER, null));
      try(final MemoryStack stack = stackPush()) {
        // fill convert configuration
        final NkConvertConfig config = NkConvertConfig.calloc(stack)
          .vertex_layout(VERTEX_LAYOUT)
          .vertex_size(20)
          .vertex_alignment(4)
          .tex_null(this.nullTexture)
          .circle_segment_count(22)
          .curve_segment_count(22)
          .arc_segment_count(22)
          .global_alpha(1.0f)
          .shape_AA(NK_ANTI_ALIASING_ON)
          .line_AA(NK_ANTI_ALIASING_ON);

        // setup buffers to load vertices and elements
        final NkBuffer vbuf = NkBuffer.malloc(stack);
        final NkBuffer ebuf = NkBuffer.malloc(stack);

        nk_buffer_init_fixed(vbuf, vertices/*, max_vertex_buffer*/);
        nk_buffer_init_fixed(ebuf, elements/*, max_element_buffer*/);
        nk_convert(this.ctx, this.cmds, vbuf, ebuf, config);
      }
      glUnmapBuffer(GL_ELEMENT_ARRAY_BUFFER);
      glUnmapBuffer(GL_ARRAY_BUFFER);

      // iterate over and execute each draw command
      final float fb_scale_x = displayWidth / width;
      final float fb_scale_y = displayHeight / height;

      long offset = NULL;
      for(NkDrawCommand cmd = nk__draw_begin(this.ctx, this.cmds); cmd != null; cmd = nk__draw_next(cmd, this.cmds, this.ctx)) {
        if(cmd.elem_count() == 0) {
          continue;
        }
        glBindTexture(GL_TEXTURE_2D, cmd.texture().id());
        glScissor(
          (int)(cmd.clip_rect().x() * fb_scale_x),
          (int)((height - (int)(cmd.clip_rect().y() + cmd.clip_rect().h())) * fb_scale_y),
          (int)(cmd.clip_rect().w() * fb_scale_x),
          (int)(cmd.clip_rect().h() * fb_scale_y)
        );
        glDrawElements(GL_TRIANGLES, cmd.elem_count(), GL_UNSIGNED_SHORT, offset);
        offset += cmd.elem_count() * 2L;
      }
      nk_clear(this.ctx);
    }

    // default OpenGL state
    glUseProgram(0);
    glBindBuffer(GL_ARRAY_BUFFER, 0);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    glBindVertexArray(0);
    glDisable(GL_BLEND);
    glDisable(GL_SCISSOR_TEST);
  }

  public void free() {
    this.ctx.clip().copy().free();
    this.ctx.clip().paste().free();
    nk_free(this.ctx);

    glDetachShader(this.prog, this.vertShdr);
    glDetachShader(this.prog, this.fragShdr);
    glDeleteShader(this.vertShdr);
    glDeleteShader(this.fragShdr);
    glDeleteProgram(this.prog);
    glDeleteTextures(this.nullTexture.texture().id());
    glDeleteBuffers(this.vbo);
    glDeleteBuffers(this.ebo);
    nk_buffer_free(this.cmds);
  }

  private void mouseScroll(final Window window, final double deltaX, final double deltaY) {
    try(final MemoryStack stack = stackPush()) {
      final NkVec2 scroll = NkVec2
        .malloc(stack)
        .x((float)deltaX)
        .y((float)deltaY);

      nk_input_scroll(this.ctx, scroll);
    }
  }

  private void charPress(final Window window, final int codepoint) {
    nk_input_unicode(this.ctx, codepoint);
  }

  private void keyPress(final Window window, final int key, final int scancode, final int mods) {
    switch(key) {
      case GLFW_KEY_DELETE -> nk_input_key(this.ctx, NK_KEY_DEL, true);
      case GLFW_KEY_ENTER -> nk_input_key(this.ctx, NK_KEY_ENTER, true);
      case GLFW_KEY_TAB -> nk_input_key(this.ctx, NK_KEY_TAB, true);
      case GLFW_KEY_BACKSPACE -> nk_input_key(this.ctx, NK_KEY_BACKSPACE, true);

      case GLFW_KEY_UP -> nk_input_key(this.ctx, NK_KEY_UP, true);
      case GLFW_KEY_DOWN -> nk_input_key(this.ctx, NK_KEY_DOWN, true);
      case GLFW_KEY_LEFT -> nk_input_key(this.ctx, (mods & GLFW_MOD_CONTROL) == 0 ? NK_KEY_LEFT : NK_KEY_TEXT_WORD_LEFT, true);
      case GLFW_KEY_RIGHT -> nk_input_key(this.ctx, (mods & GLFW_MOD_CONTROL) == 0 ? NK_KEY_RIGHT : NK_KEY_TEXT_WORD_RIGHT, true);

      case GLFW_KEY_HOME -> {
        nk_input_key(this.ctx, NK_KEY_TEXT_START, true);
        nk_input_key(this.ctx, NK_KEY_SCROLL_START, true);
      }

      case GLFW_KEY_END -> {
        nk_input_key(this.ctx, NK_KEY_TEXT_END, true);
        nk_input_key(this.ctx, NK_KEY_SCROLL_END, true);
      }

      case GLFW_KEY_PAGE_DOWN -> nk_input_key(this.ctx, NK_KEY_SCROLL_DOWN, true);
      case GLFW_KEY_PAGE_UP -> nk_input_key(this.ctx, NK_KEY_SCROLL_UP, true);

      case GLFW_KEY_LEFT_SHIFT, GLFW_KEY_RIGHT_SHIFT -> nk_input_key(this.ctx, NK_KEY_SHIFT, true);

      case GLFW_KEY_C -> {
        if((mods & GLFW_MOD_CONTROL) != 0) {
          nk_input_key(this.ctx, NK_KEY_COPY, true);
        }
      }

      case GLFW_KEY_V -> {
        if((mods & GLFW_MOD_CONTROL) != 0) {
          nk_input_key(this.ctx, NK_KEY_PASTE, true);
        }
      }

      case GLFW_KEY_X -> {
        if((mods & GLFW_MOD_CONTROL) != 0) {
          nk_input_key(this.ctx, NK_KEY_CUT, true);
        }
      }

      case GLFW_KEY_Z -> {
        if((mods & GLFW_MOD_CONTROL) != 0) {
          nk_input_key(this.ctx, (mods & GLFW_MOD_SHIFT) == 0? NK_KEY_TEXT_UNDO : NK_KEY_TEXT_REDO, true);
        }
      }

      case GLFW_KEY_B -> {
        if((mods & GLFW_MOD_CONTROL) != 0) {
          nk_input_key(this.ctx, NK_KEY_TEXT_LINE_START, true);
        }
      }

      case GLFW_KEY_E -> {
        if((mods & GLFW_MOD_CONTROL) != 0) {
          nk_input_key(this.ctx, NK_KEY_TEXT_LINE_END, true);
        }
      }
    }

    final Gui gui = this.guiStack.peekFirst();
    if(gui != null) {
      gui.keyPress(key, scancode, mods);
    }
  }

  private void keyRelease(final Window window, final int key, final int scancode, final int mods) {
    switch(key) {
      case GLFW_KEY_DELETE -> nk_input_key(this.ctx, NK_KEY_DEL, false);
      case GLFW_KEY_ENTER -> nk_input_key(this.ctx, NK_KEY_ENTER, false);
      case GLFW_KEY_TAB -> nk_input_key(this.ctx, NK_KEY_TAB, false);
      case GLFW_KEY_BACKSPACE -> nk_input_key(this.ctx, NK_KEY_BACKSPACE, false);

      case GLFW_KEY_UP -> nk_input_key(this.ctx, NK_KEY_UP, false);
      case GLFW_KEY_DOWN -> nk_input_key(this.ctx, NK_KEY_DOWN, false);
      case GLFW_KEY_LEFT -> nk_input_key(this.ctx, (mods & GLFW_MOD_CONTROL) == 0 ? NK_KEY_LEFT : NK_KEY_TEXT_WORD_LEFT, false);
      case GLFW_KEY_RIGHT -> nk_input_key(this.ctx, (mods & GLFW_MOD_CONTROL) == 0 ? NK_KEY_RIGHT : NK_KEY_TEXT_WORD_RIGHT, false);

      case GLFW_KEY_HOME -> {
        nk_input_key(this.ctx, NK_KEY_TEXT_START, false);
        nk_input_key(this.ctx, NK_KEY_SCROLL_START, false);
      }

      case GLFW_KEY_END -> {
        nk_input_key(this.ctx, NK_KEY_TEXT_END, false);
        nk_input_key(this.ctx, NK_KEY_SCROLL_END, false);
      }

      case GLFW_KEY_PAGE_DOWN -> nk_input_key(this.ctx, NK_KEY_SCROLL_DOWN, false);
      case GLFW_KEY_PAGE_UP -> nk_input_key(this.ctx, NK_KEY_SCROLL_UP, false);

      case GLFW_KEY_LEFT_SHIFT, GLFW_KEY_RIGHT_SHIFT -> nk_input_key(this.ctx, NK_KEY_SHIFT, false);

      case GLFW_KEY_C -> {
        if((mods & GLFW_MOD_CONTROL) != 0) {
          nk_input_key(this.ctx, NK_KEY_COPY, false);
        }
      }

      case GLFW_KEY_V -> {
        if((mods & GLFW_MOD_CONTROL) != 0) {
          nk_input_key(this.ctx, NK_KEY_PASTE, false);
        }
      }

      case GLFW_KEY_X -> {
        if((mods & GLFW_MOD_CONTROL) != 0) {
          nk_input_key(this.ctx, NK_KEY_CUT, false);
        }
      }

      case GLFW_KEY_Z -> {
        if((mods & GLFW_MOD_CONTROL) != 0) {
          nk_input_key(this.ctx, (mods & GLFW_MOD_SHIFT) == 0? NK_KEY_TEXT_UNDO : NK_KEY_TEXT_REDO, false);
        }
      }

      case GLFW_KEY_B -> {
        if((mods & GLFW_MOD_CONTROL) != 0) {
          nk_input_key(this.ctx, NK_KEY_TEXT_LINE_START, false);
        }
      }

      case GLFW_KEY_E -> {
        if((mods & GLFW_MOD_CONTROL) != 0) {
          nk_input_key(this.ctx, NK_KEY_TEXT_LINE_END, false);
        }
      }
    }

    final Gui gui = this.guiStack.peekFirst();
    if(gui != null) {
      gui.keyRelease(key, scancode, mods);
    }
  }

  private void mouseMove(final Window window, final double x, final double y) {
    nk_input_motion(this.ctx, (int)x, (int)y);
  }

  private void mousePress(final Window window, final double x, final double y, final int button, final int mods) {
    final int nkButton = switch(button) {
      case GLFW_MOUSE_BUTTON_RIGHT -> NK_BUTTON_RIGHT;
      case GLFW_MOUSE_BUTTON_MIDDLE -> NK_BUTTON_MIDDLE;
      default -> NK_BUTTON_LEFT;
    };

    nk_input_button(this.ctx, nkButton, (int)x, (int)y, true);
  }

  private void mouseRelease(final Window window, final double x, final double y, final int button, final int mods) {
    final int nkButton = switch(button) {
      case GLFW_MOUSE_BUTTON_RIGHT -> NK_BUTTON_RIGHT;
      case GLFW_MOUSE_BUTTON_MIDDLE -> NK_BUTTON_MIDDLE;
      default -> NK_BUTTON_LEFT;
    };

    nk_input_button(this.ctx, nkButton, (int)x, (int)y, false);
  }
}
