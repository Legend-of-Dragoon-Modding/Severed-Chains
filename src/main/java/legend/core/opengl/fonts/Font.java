package legend.core.opengl.fonts;

import legend.core.GameEngine;
import legend.core.IoHelper;
import legend.core.opengl.Mesh;
import legend.core.opengl.Texture;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.file.Path;
import java.util.function.Consumer;

import static org.lwjgl.BufferUtils.createByteBuffer;
import static org.lwjgl.opengl.GL11C.GL_LINEAR;
import static org.lwjgl.opengl.GL11C.GL_RED;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11C.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL30C.GL_R8;
import static org.lwjgl.stb.STBTruetype.stbtt_GetPackedQuad;
import static org.lwjgl.stb.STBTruetype.stbtt_PackBegin;
import static org.lwjgl.stb.STBTruetype.stbtt_PackEnd;
import static org.lwjgl.stb.STBTruetype.stbtt_PackFontRange;
import static org.lwjgl.stb.STBTruetype.stbtt_PackSetOversampling;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.MemoryUtil.memAllocFloat;
import static org.lwjgl.system.MemoryUtil.memFree;

public class Font {
  private static final float[] scale = {
    24.0f,
    14.0f,
  };

  private static final int[] sf = {
    0, 1, 2,
    0, 1, 2,
  };

  private static final int BITMAP_W = 512;
  private static final int BITMAP_H = 512;

  private final Texture texture;
  private final STBTTPackedchar.Buffer charData;

  private final STBTTAlignedQuad q = STBTTAlignedQuad.malloc();
  private final FloatBuffer xb = memAllocFloat(1);
  private final FloatBuffer yb = memAllocFloat(1);

  private final boolean integerAlign = true;
  private final int font = 1;

  public Font(final Path font) throws IOException {
    this.charData = STBTTPackedchar.malloc(6 * 128);

    try(final STBTTPackContext pc = STBTTPackContext.malloc()) {
      final ByteBuffer ttf = IoHelper.pathToByteBuffer(font);
      final ByteBuffer bitmap = createByteBuffer(BITMAP_W * BITMAP_H);

      stbtt_PackBegin(pc, bitmap, BITMAP_W, BITMAP_H, 0, 1, NULL);
      for(int i = 0; i < 2; i++) {
        int p = i * 3 * 128 + 32;
        this.charData.limit(p + 95);
        this.charData.position(p);
        stbtt_PackSetOversampling(pc, 1, 1);
        stbtt_PackFontRange(pc, ttf, 0, scale[i], 32, this.charData);

        p = (i * 3 + 1) * 128 + 32;
        this.charData.limit(p + 95);
        this.charData.position(p);
        stbtt_PackSetOversampling(pc, 2, 2);
        stbtt_PackFontRange(pc, ttf, 0, scale[i], 32, this.charData);

        p = (i * 3 + 2) * 128 + 32;
        this.charData.limit(p + 95);
        this.charData.position(p);
        stbtt_PackSetOversampling(pc, 3, 1);
        stbtt_PackFontRange(pc, ttf, 0, scale[i], 32, this.charData);
      }
      this.charData.clear();
      stbtt_PackEnd(pc);

      this.texture = Texture.create(builder -> {
        builder.dataType(GL_UNSIGNED_BYTE);
        builder.dataFormat(GL_RED);
        builder.internalFormat(GL_R8);
        builder.data(bitmap, BITMAP_W, BITMAP_H);
        builder.magFilter(GL_LINEAR);
        builder.minFilter(GL_LINEAR);
      });
    }
  }

  public void free() {
    this.texture.delete();
    this.charData.free();
    this.q.free();
    memFree(this.xb);
    memFree(this.yb);
  }

  public TextStream text(final Consumer<TextStream.Builder> stream) {
    return TextStream.create(this, stream);
  }

  public void use() {
    this.texture.use();
  }

  public Mesh buildTextQuads(final CharSequence text) {
    final int font = sf[this.font];

    this.xb.put(0, 0);
    this.yb.put(0, 0);

    this.charData.position(font * 128);

    final float[] vertices = new float[text.length() * 6 * 5];

    for(int i = 0; i < text.length(); i++) {
      stbtt_GetPackedQuad(this.charData, BITMAP_W, BITMAP_H, text.charAt(i), this.xb, this.yb, this.q, font == 0 && this.integerAlign);

      final float scale = GameEngine.RENDERER.window().getScale();

      System.arraycopy(new float[] {
        this.q.x0() / scale, (this.q.y1() + 12.0f) / scale, 0.0f, this.q.s0(), this.q.t1(),
        this.q.x1() / scale, (this.q.y1() + 12.0f) / scale, 0.0f, this.q.s1(), this.q.t1(),
        this.q.x1() / scale, (this.q.y0() + 12.0f) / scale, 0.0f, this.q.s1(), this.q.t0(),
        this.q.x1() / scale, (this.q.y0() + 12.0f) / scale, 0.0f, this.q.s1(), this.q.t0(),
        this.q.x0() / scale, (this.q.y0() + 12.0f) / scale, 0.0f, this.q.s0(), this.q.t0(),
        this.q.x0() / scale, (this.q.y1() + 12.0f) / scale, 0.0f, this.q.s0(), this.q.t1(),
      }, 0, vertices, i * 6 * 5, 6 * 5);
    }

    final Mesh mesh = new Mesh(GL_TRIANGLES, vertices, vertices.length / 5);
    mesh.attribute(0, 0L, 3, 5);
    mesh.attribute(1, 3L, 2, 5);

    return mesh;
  }

  public float getCurrentX() {
    return this.xb.get(0);
  }
}
