package legend.core.gpu;

import legend.core.IoHelper;
import legend.core.gte.TmdObjTable1c;
import legend.game.types.Translucency;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.Arrays;

import static legend.game.Scus94491BpeSegment.tmdGp0CommandId_1f8003ee;

public class ModelLoader {
  public static ModelLoader quad(final String name, final int x, final int y, final int z, final int w, final int h, final int u, final int v, final int tw, final int th, final int paletteBase, final int pageX, final int pageY, final int r, final int g, final int b, @Nullable final Translucency translucency) {
    final int colour = b << 16 | g << 8 | r;

    final Mesh.Vertex2d[] vertices = new Mesh.Vertex2d[4];
    vertices[0] = new Mesh.Vertex2d(new Vector2f(x, y), new Vector2i(u, v), colour);
    vertices[1] = new Mesh.Vertex2d(new Vector2f(x + w, y), new Vector2i(u + tw, v), colour);
    vertices[2] = new Mesh.Vertex2d(new Vector2f(x, y + h), new Vector2i(u, v + th), colour);
    vertices[3] = new Mesh.Vertex2d(new Vector2f(x + w, y + h), new Vector2i(u + tw, v + th), colour);

    final Mesh.Poly2d[] polys = {new Mesh.Poly2d(vertices, paletteBase, pageX, pageY, translucency)};
    final Mesh.Segment2d[] segments = {new Mesh.Segment2d("Quad " + name, polys, 4, z, true, translucency != null)};
    return new ModelLoader(segments);
  }

  public static ModelLoader fromTmd(final String name, final TmdObjTable1c objTable) {
    final Mesh.Segment3d[] segments = new Mesh.Segment3d[objTable.primitives_10.length];

    for(int primitiveIndex = 0; primitiveIndex < objTable.primitives_10.length; primitiveIndex++) {
      final TmdObjTable1c.Primitive primitive = objTable.primitives_10[primitiveIndex];

      // Read type info from command ---
      final int command = primitive.header() & 0xff04_0000;
      final int primitiveId = command >>> 24;

      if((primitiveId >>> 5 & 0b11) != 1) {
        throw new RuntimeException("Unsupported primitive type");
      }

      boolean shaded = (command & 0x4_0000) != 0;
      final boolean quad = (primitiveId & 0b1000) != 0;
      final boolean textured = (primitiveId & 0b100) != 0;
      final boolean translucent = (primitiveId & 0b10) != 0;
      final boolean lit = (primitiveId & 0b1) == 0;

      if(textured && shaded) {
        throw new RuntimeException("Invalid primitive type");
      }

      if(!textured && !lit) {
        throw new RuntimeException("Invalid primitive type");
      }

      //TODO need to figure out what this was being used for
      final long specialTrans = tmdGp0CommandId_1f8003ee;

      final int vertexCount = quad ? 4 : 3;
      // ---

      final Mesh.Poly3d[] polys = new Mesh.Poly3d[primitive.data().length];
      final Vector3f[] positions = new Vector3f[vertexCount];
      final Vector3f[] normals = new Vector3f[vertexCount];
      final int[] us = new int[vertexCount];
      final int[] vs = new int[vertexCount];
      final int[] colours = new int[vertexCount];

      for(int i = 0; i < primitive.data().length; i++) {
        final byte[] data = primitive.data()[i];
        Translucency translucency = null;
        int paletteBase = 0;
        int pageX = 0;
        int pageY = 0;

        // Read data from TMD ---
        int primitivesOffset = 0;

        if(textured) {
          for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
            us[vertexIndex] = IoHelper.readUByte(data, primitivesOffset++);
            vs[vertexIndex] = IoHelper.readUByte(data, primitivesOffset++);

            if(vertexIndex == 0) {
              paletteBase = IoHelper.readUShort(data, primitivesOffset) >>> 6;
            } else if(vertexIndex == 1) {
              final int tpage = IoHelper.readUShort(data, primitivesOffset);
              translucency = Translucency.of(tpage >>> 5 & 0b11);
              pageX = (tpage & 0b1111) * 64;
              pageY = (tpage & 0b10000) != 0 ? 256 : 0;
            }

            primitivesOffset += 2;
          }
        }

        if(shaded || !lit) {
          shaded = true;
          for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
            colours[vertexIndex] = IoHelper.readInt(data, primitivesOffset);
            primitivesOffset += 4;
          }
        } else if(!textured) {
          shaded = true;
          final int colour = IoHelper.readInt(data, primitivesOffset);
          primitivesOffset += 4;

          Arrays.fill(colours, colour);
        }

        for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
          if(lit) {
            normals[vertexIndex] = objTable.normal_top_08[IoHelper.readUShort(data, primitivesOffset)];
            primitivesOffset += 2;
          }

          positions[vertexIndex] = objTable.vert_top_00[IoHelper.readUShort(data, primitivesOffset)];
          primitivesOffset += 2;
        }

        final Mesh.Vertex3d[] vertices = new Mesh.Vertex3d[vertexCount];
        for(int vertex = 0; vertex < vertexCount; vertex++) {
          vertices[vertex] = new Mesh.Vertex3d(positions[vertex], normals[vertex], us[vertex], vs[vertex], colours[vertex]);
        }

        polys[i] = new Mesh.Poly3d(vertices, paletteBase, pageX, pageY, translucency);
      }

      segments[primitiveIndex] = new Mesh.Segment3d("TMD " + name, polys, vertexCount, textured, shaded, translucent, lit, false);
    }

    return new ModelLoader(segments);
  }

  private final Mesh.Segment[] segments;
  private VramTexture texture;
  private VramTexture[] palettes;

  private ModelLoader(final Mesh.Segment[] segments) {
    this.segments = segments;
  }

  public ModelLoader texture(final VramTexture texture) {
    this.texture = texture;
    return this;
  }

  public ModelLoader palettes(final VramTexture[] palettes) {
    this.palettes = palettes;
    return this;
  }

  public Renderable build() {
    return new Renderable(new Mesh(this.segments), this.texture, this.palettes);
  }
}
