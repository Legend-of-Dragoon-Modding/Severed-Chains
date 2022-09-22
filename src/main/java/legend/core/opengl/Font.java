package legend.core.opengl;

import org.lwjgl.BufferUtils;
import org.lwjgl.nuklear.NkUserFont;
import org.lwjgl.nuklear.NkUserFontGlyph;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.BufferUtils.createByteBuffer;
import static org.lwjgl.nuklear.Nuklear.NK_UTF_INVALID;
import static org.lwjgl.nuklear.Nuklear.nnk_utf_decode;
import static org.lwjgl.opengl.GL11C.GL_LINEAR;
import static org.lwjgl.opengl.GL11C.GL_RGBA;
import static org.lwjgl.opengl.GL11C.GL_RGBA8;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11C.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11C.glBindTexture;
import static org.lwjgl.opengl.GL11C.glDeleteTextures;
import static org.lwjgl.opengl.GL11C.glGenTextures;
import static org.lwjgl.opengl.GL11C.glTexImage2D;
import static org.lwjgl.opengl.GL11C.glTexParameteri;
import static org.lwjgl.opengl.GL12C.GL_UNSIGNED_INT_8_8_8_8_REV;
import static org.lwjgl.stb.STBTruetype.stbtt_GetCodepointHMetrics;
import static org.lwjgl.stb.STBTruetype.stbtt_GetFontVMetrics;
import static org.lwjgl.stb.STBTruetype.stbtt_GetPackedQuad;
import static org.lwjgl.stb.STBTruetype.stbtt_InitFont;
import static org.lwjgl.stb.STBTruetype.stbtt_PackBegin;
import static org.lwjgl.stb.STBTruetype.stbtt_PackEnd;
import static org.lwjgl.stb.STBTruetype.stbtt_PackFontRange;
import static org.lwjgl.stb.STBTruetype.stbtt_PackSetOversampling;
import static org.lwjgl.stb.STBTruetype.stbtt_ScaleForPixelHeight;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAddress;
import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.lwjgl.system.MemoryUtil.memFree;

public class Font {
  final NkUserFont font = NkUserFont.create();

  @SuppressWarnings("FieldCanBeLocal")
  private final ByteBuffer ttf; // This buffer MUST be kept in memory!

  public Font(final String font) throws IOException {
    this(font, 18);
  }

  public Font(final String font, final int size) throws IOException {
    this.ttf = ioResourceToByteBuffer(font, 512 * 1024);

    final int BITMAP_W = 1024;
    final int BITMAP_H = 1024;

    final int FONT_HEIGHT = size;
    final int fontTexID = glGenTextures();

    final STBTTFontinfo fontInfo = STBTTFontinfo.create();
    final STBTTPackedchar.Buffer cdata = STBTTPackedchar.create(95);

    final float scale;
    final float descent;

    try(final MemoryStack stack = stackPush()) {
      stbtt_InitFont(fontInfo, this.ttf);
      scale = stbtt_ScaleForPixelHeight(fontInfo, FONT_HEIGHT);

      final IntBuffer d = stack.mallocInt(1);
      stbtt_GetFontVMetrics(fontInfo, null, d, null);
      descent = d.get(0) * scale;

      final ByteBuffer bitmap = memAlloc(BITMAP_W * BITMAP_H);

      final STBTTPackContext pc = STBTTPackContext.mallocStack(stack);
      stbtt_PackBegin(pc, bitmap, BITMAP_W, BITMAP_H, 0, 1, NULL);
      stbtt_PackSetOversampling(pc, 4, 4);
      stbtt_PackFontRange(pc, this.ttf, 0, FONT_HEIGHT, 32, cdata);
      stbtt_PackEnd(pc);

      // Convert R8 to RGBA8
      final ByteBuffer texture = memAlloc(BITMAP_W * BITMAP_H * 4);
      for(int i = 0; i < bitmap.capacity(); i++) {
        texture.putInt(bitmap.get(i) << 24 | 0x00FFFFFF);
      }
      texture.flip();

      glBindTexture(GL_TEXTURE_2D, fontTexID);
      glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, BITMAP_W, BITMAP_H, 0, GL_RGBA, GL_UNSIGNED_INT_8_8_8_8_REV, texture);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
      glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

      memFree(texture);
      memFree(bitmap);
    }

    this.font
      .width((handle, h, text, len) -> {
        try(final MemoryStack stack = stackPush()) {
          final IntBuffer unicode = stack.mallocInt(1);

          int glyph_len = nnk_utf_decode(text, memAddress(unicode), len);
          int text_len = glyph_len;

          if(glyph_len == 0) {
            return 0;
          }

          final IntBuffer advance = stack.mallocInt(1);
          float text_width = 0;
          while(text_len <= len && glyph_len != 0) {
            if(unicode.get(0) == NK_UTF_INVALID) {
              return text_width;
            }

            /* query currently drawn glyph information */
            stbtt_GetCodepointHMetrics(fontInfo, unicode.get(0), advance, null);
            text_width += advance.get(0) * scale;

            /* offset next glyph */
            glyph_len = nnk_utf_decode(text + text_len, memAddress(unicode), len - text_len);
            text_len += glyph_len;
          }

          return text_width;
        }
      })
      .height(FONT_HEIGHT)
      .query((handle, font_height, glyph, codepoint, next_codepoint) -> {
        try(final MemoryStack stack = stackPush()) {
          final FloatBuffer x = stack.floats(0.0f);
          final FloatBuffer y = stack.floats(0.0f);

          final STBTTAlignedQuad q = STBTTAlignedQuad.mallocStack(stack);
          final IntBuffer advance = stack.mallocInt(1);

          stbtt_GetPackedQuad(cdata, BITMAP_W, BITMAP_H, codepoint - 32, x, y, q, false);
          stbtt_GetCodepointHMetrics(fontInfo, codepoint, advance, null);

          final NkUserFontGlyph ufg = NkUserFontGlyph.create(glyph);

          ufg.width(q.x1() - q.x0());
          ufg.height(q.y1() - q.y0());
          ufg.offset().set(q.x0(), q.y0() + (FONT_HEIGHT + descent));
          ufg.xadvance(advance.get(0) * scale);
          ufg.uv(0).set(q.s0(), q.t0());
          ufg.uv(1).set(q.s1(), q.t1());
        }
      })
      .texture(it -> it.id(fontTexID));
  }

  public void free() {
    this.font.query().free();
    this.font.width().free();
    glDeleteTextures(this.font.texture().id());
  }

  private static ByteBuffer ioResourceToByteBuffer(final String resource, final int bufferSize) throws IOException {
    ByteBuffer buffer;

    final Path path = Paths.get(resource);
    if(Files.isReadable(path)) {
      try(final SeekableByteChannel fc = Files.newByteChannel(path)) {
        buffer = createByteBuffer((int)fc.size() + 1);
        while(fc.read(buffer) != -1) {
        }
      }
    } else {
      try(
        final InputStream source = Font.class.getClassLoader().getResourceAsStream(resource);
        final ReadableByteChannel rbc = Channels.newChannel(source)
      ) {
        buffer = createByteBuffer(bufferSize);

        while(true) {
          final int bytes = rbc.read(buffer);
          if(bytes == -1) {
            break;
          }
          if(buffer.remaining() == 0) {
            buffer = resizeBuffer(buffer, buffer.capacity() * 3 / 2); // 50%
          }
        }
      }
    }

    buffer.flip();
    return buffer;
  }

  private static ByteBuffer resizeBuffer(final ByteBuffer buffer, final int newCapacity) {
    final ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
    buffer.flip();
    newBuffer.put(buffer);
    return newBuffer;
  }
}
