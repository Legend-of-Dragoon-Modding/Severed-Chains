package legend.core.gpu;

import legend.core.IoHelper;
import legend.core.gte.SVECTOR;
import legend.core.gte.TmdObjTable1c;
import legend.game.types.Translucency;

import javax.annotation.Nullable;
import java.util.Arrays;

import static legend.game.Scus94491BpeSegment.tmdGp0CommandId_1f8003ee;

public class ModelLoader {
  public static ModelLoader quad(final int x, final int y, final int z, final int w, final int h, final int u, final int v, final int tw, final int th, final int paletteBase, final int r, final int g, final int b, @Nullable final Translucency translucency) {
    final int colour = b << 16 | g << 8 | r;

    final Mesh.Vertex2d[] vertices = new Mesh.Vertex2d[4];
    vertices[0] = new Mesh.Vertex2d(new Vec2i(x, y), new Vec2i(u, v), colour);
    vertices[1] = new Mesh.Vertex2d(new Vec2i(x + w, y), new Vec2i(u + tw, v), colour);
    vertices[2] = new Mesh.Vertex2d(new Vec2i(x, y + h), new Vec2i(u, v + th), colour);
    vertices[3] = new Mesh.Vertex2d(new Vec2i(x + w, y + h), new Vec2i(u + tw, v + th), colour);

    final Mesh.Poly2d[] polys = {new Mesh.Poly2d(vertices, paletteBase, translucency)};
    final Mesh.Segment2d[] segments = {new Mesh.Segment2d(polys, 4, z, true, translucency != null)};
    return new ModelLoader(segments);
  }

  public static ModelLoader fromTmd(final TmdObjTable1c objTable) {
    final Mesh.Segment3d[] segments = new Mesh.Segment3d[objTable.primitives_10.length];

    for(int primitiveIndex = 0; primitiveIndex < objTable.primitives_10.length; primitiveIndex++) {
      final TmdObjTable1c.Primitive primitive = objTable.primitives_10[primitiveIndex];

      // Read type info from command ---
      final int command = primitive.header() & 0xff04_0000;
      final int primitiveId = command >>> 24;

      if((primitiveId >>> 5 & 0b11) != 1) {
        throw new RuntimeException("Unsupported primitive type");
      }

      final boolean shaded = (command & 0x4_0000) != 0;
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
      final long specialTrans = tmdGp0CommandId_1f8003ee.get();

      final int vertexCount = quad ? 4 : 3;
      // ---

      final Mesh.Poly3d[] polys = new Mesh.Poly3d[primitive.data().length];
      final SVECTOR[] positions = new SVECTOR[vertexCount];
      final SVECTOR[] normals = new SVECTOR[vertexCount];
      final int[] us = new int[vertexCount];
      final int[] vs = new int[vertexCount];
      final int[] colours = new int[vertexCount];

      for(int i = 0; i < primitive.data().length; i++) {
        final byte[] data = primitive.data()[i];
        Translucency translucency = null;
        int paletteBase = 0;

        // Read data from TMD ---
        int primitivesOffset = 0;

        if(textured) {
          for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
            us[vertexIndex] = IoHelper.readUByte(data, primitivesOffset++);
            vs[vertexIndex] = IoHelper.readUByte(data, primitivesOffset++);

            if(vertexIndex == 0) {
              paletteBase = IoHelper.readUShort(data, primitivesOffset) >>> 6;
            } else if(vertexIndex == 1) {
              translucency = Translucency.of(IoHelper.readUShort(data, primitivesOffset) >>> 5 & 0b11);
            }

            primitivesOffset += 2;
          }
        }

        if(shaded || !lit) {
          for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
            colours[vertexIndex] = IoHelper.readInt(data, primitivesOffset);
            primitivesOffset += 4;
          }
        } else if(!textured) {
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

        polys[i] = new Mesh.Poly3d(vertices, paletteBase, translucency);
      }

      segments[primitiveIndex] = new Mesh.Segment3d(polys, vertexCount, textured, shaded, translucent, lit, false);
    }

    return new ModelLoader(segments);
  }

  private final Mesh.Segment[] segments;
  private VramTexture2 texture;
  private VramTexture2[] palettes;

  private ModelLoader(final Mesh.Segment[] segments) {
    this.segments = segments;
  }

  public ModelLoader texture(final VramTexture2 texture) {
    this.texture = texture;
    return this;
  }

  public ModelLoader palettes(final VramTexture2[] palettes) {
    this.palettes = palettes;
    return this;
  }

  public Renderable build() {
    return new Renderable(new Mesh(this.segments), this.texture, this.palettes);
  }
}
