package legend.core.opengl;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import legend.core.MathHelper;
import legend.core.gpu.Bpp;
import legend.game.types.LodString;

import static legend.core.MathHelper.makeClut;
import static legend.core.MathHelper.makeTpage;
import static legend.core.opengl.TmdObjLoader.CLUT_SIZE;
import static legend.core.opengl.TmdObjLoader.COLOUR_SIZE;
import static legend.core.opengl.TmdObjLoader.FLAGS_SIZE;
import static legend.core.opengl.TmdObjLoader.NORM_SIZE;
import static legend.core.opengl.TmdObjLoader.POS_SIZE;
import static legend.core.opengl.TmdObjLoader.TPAGE_SIZE;
import static legend.core.opengl.TmdObjLoader.UV_SIZE;
import static legend.game.Scus94491BpeSegment_8002.charWidth;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;

public class TextBuilder {
  private final String name;
  private final IntList chars = new IntArrayList();
  private final IntList widths = new IntArrayList();
  private int totalSize;

  private float r = 1.0f;
  private float g = 1.0f;
  private float b = 1.0f;
  private int trim;
  private boolean centred;
  private boolean shadowed;

  private final int flags = TmdObjLoader.TEXTURED_FLAG | TmdObjLoader.COLOURED_FLAG;

  public TextBuilder(final String name) {
    this.name = name;
    this.widths.add(0);
  }

  public TextBuilder text(final String text) {
    int currentLineWidth = 0;
    int charWidth;
    for(int i = 0; i < text.length(); i++) {
      final char chr = text.charAt(i);

      if(chr == '\n') {
        int spacesTrimmed = 0;
        for(int j = this.chars.size() - 1; j >= 0; j--) {
          if(this.chars.getInt(j) != 0) {
            break;
          }

          spacesTrimmed++;
        }

        if(spacesTrimmed > 0) {
          this.chars.removeElements(this.chars.size() - spacesTrimmed, this.chars.size());
          this.addToWidth(-8 * spacesTrimmed);
        }

        this.newLine();
        currentLineWidth = 0;
      } else if(chr != ' ' || currentLineWidth != 0) {
        this.chars.add(LodString.toLodChar(chr));
        charWidth = charWidth(chr);
        this.addToWidth(charWidth);
        currentLineWidth += charWidth;
      }
    }

    this.totalSize += text.length();
    return this;
  }

  public TextBuilder newLine() {
    this.chars.add(0xa1ff);
    this.widths.add(0);
    return this;
  }

  private void addToWidth(final int amount) {
    this.widths.set(this.widths.size() - 1, this.widths.getInt(this.widths.size() - 1) + amount);
  }

  public TextBuilder trim(final int trim) {
    this.trim = MathHelper.clamp(trim, -12, 12);
    return this;
  }

  public TextBuilder centred() {
    this.centred = true;
    return this;
  }

  public TextBuilder shadowed() {
    this.shadowed = true;
    return this;
  }

  public TextObj build() {
    int vertexSize = POS_SIZE;
    vertexSize += NORM_SIZE;
    vertexSize += UV_SIZE + TPAGE_SIZE + CLUT_SIZE;
    vertexSize += COLOUR_SIZE;
    vertexSize += FLAGS_SIZE;

    final int iterations = this.shadowed ? 2 : 1;

    final float[] vertices = new float[this.totalSize * 4 * vertexSize * iterations];
    final int[] indices = new int[this.totalSize * 6 * iterations];
    int vertexIndex = 0;
    int indexIndex = 0;
    int indicesVertexIndex = 0;

    for(int i = 0; i < iterations; i++) {
      int charIndex = 0;
      int lineIndex = 0;
      int glyphNudge = 0;

      float r = this.r;
      float g = this.g;
      float b = this.b;

      for(int totalCharIndex = 0; totalCharIndex < this.chars.size(); totalCharIndex++) {
        final int c = this.chars.getInt(totalCharIndex);

        if(c == 0xa1ff) {
          charIndex = 0;
          glyphNudge = 0;
          lineIndex++;
        } else {
          if(charIndex == 0) {
            glyphNudge = 0;
          }

          if(c == 0x45) { // m
            glyphNudge -= 1;
          } else if(c == 0x2) { // .
            glyphNudge -= 2;
          } else if(c >= 0x5 && c < 0x7) { // ?, !
            glyphNudge -= 3;
          }

          final float x = this.centred ? -this.widths.getInt(lineIndex) / 2.0f : 0.0f;
          final int textU = c & 0xf;
          final int textV = c / 16;
          final int v1 = textV * 12;
          final int v = this.trim >= 0 ? v1 : v1 - this.trim;
          final int h = this.trim >= 0 ? 12 - this.trim : 12 + this.trim;

          if(i == 1 && this.shadowed) {
            vertexIndex = this.setVertices(
              vertices, vertexIndex,
              x + charIndex * 8 - glyphNudge + 1.0f, lineIndex * 12 + 1.0f,
              textU * 16, v,
              8, h,
              0.0f, 0.0f, 0.0f
            );
          } else {
            vertexIndex = this.setVertices(
              vertices, vertexIndex,
              x + charIndex * 8 - glyphNudge, lineIndex * 12,
              textU * 16, v,
              8, h,
              r, g, b
            );
          }

          indices[indexIndex++] = indicesVertexIndex;
          indices[indexIndex++] = indicesVertexIndex + 1;
          indices[indexIndex++] = indicesVertexIndex + 2;
          indices[indexIndex++] = indicesVertexIndex + 3;
          indices[indexIndex++] = indicesVertexIndex + 2;
          indices[indexIndex++] = indicesVertexIndex + 1;
          indicesVertexIndex += 4;

          glyphNudge += switch(c) {
            case 0x5, 0x23, 0x24, 0x2a, 0x37, 0x38, 0x3a, 0x3b, 0x3c, 0x3d, 0x3f, 0x40, 0x43, 0x46, 0x47, 0x48, 0x49, 0x4a, 0x4b, 0x4d, 0x4e, 0x51, 0x52 -> 1;
            case 0x2, 0x8, 0x3e, 0x4c -> 2;
            case 0xb, 0xc, 0x42 -> 3;
            case 0x1, 0x3, 0x4, 0x9, 0x16, 0x41, 0x44 -> 4;
            case 0x6, 0x27 -> 5;
            default -> 0;
          };

          charIndex++;
        }
      }
    }

    final Mesh mesh = new Mesh(GL_TRIANGLES, vertices, indices);

    mesh.attribute(0, 0L, 3, vertexSize);

    int meshIndex = 1;
    int meshOffset = 3;

    mesh.attribute(meshIndex, meshOffset, NORM_SIZE, vertexSize);
    meshIndex++;
    meshOffset += NORM_SIZE;

    mesh.attribute(meshIndex, meshOffset, UV_SIZE, vertexSize);
    meshIndex++;
    meshOffset += UV_SIZE;

    mesh.attribute(meshIndex, meshOffset, TPAGE_SIZE, vertexSize);
    meshIndex++;
    meshOffset += TPAGE_SIZE;

    mesh.attribute(meshIndex, meshOffset, CLUT_SIZE, vertexSize);
    meshIndex++;
    meshOffset += CLUT_SIZE;

    mesh.attribute(meshIndex, meshOffset, COLOUR_SIZE, vertexSize);
    meshIndex++;
    meshOffset += COLOUR_SIZE;

    mesh.attribute(meshIndex, meshOffset, FLAGS_SIZE, vertexSize);

    return new TextObj(this.name, mesh);
  }

  private int setVertices(final float[] vertices, int offset, final float x, final float y, final float u, final float v, final float w, final float h, final float r, final float g, final float b) {
    offset = this.setVertex(vertices, offset, x, y, u, v, r, g, b);
    offset = this.setVertex(vertices, offset, x, y + h, u, v + h, r, g, b);
    offset = this.setVertex(vertices, offset, x + w, y, u + w, v, r, g, b);
    offset = this.setVertex(vertices, offset, x + w, y + h, u + w, v + h, r, g, b);
    return offset;
  }

  private int setVertex(final float[] vertices, int offset, final float x, final float y, final float u, final float v, final float r, final float g, final float b) {
    vertices[offset++] = x;
    vertices[offset++] = y;
    vertices[offset++] = 0.0f;
    vertices[offset++] = 0.0f; //
    vertices[offset++] = 0.0f; // normals
    vertices[offset++] = 0.0f; //
    vertices[offset++] = u;
    vertices[offset++] = v;
    vertices[offset++] = makeTpage(832, 256, Bpp.BITS_4, null);
    vertices[offset++] = makeClut(832, 480);
    vertices[offset++] = r;
    vertices[offset++] = g;
    vertices[offset++] = b;
    vertices[offset++] = 0;
    vertices[offset++] = this.flags;
    return offset;
  }
}
