package legend.core.opengl;

import legend.core.IoHelper;
import legend.core.MathHelper;
import legend.core.gpu.Bpp;
import legend.core.gte.ModelPart10;
import legend.core.gte.Tmd;
import legend.core.gte.TmdObjTable1c;
import legend.game.tmd.Polygon;
import legend.game.tmd.Vertex;
import legend.game.types.Model124;
import legend.game.types.Translucency;
import org.joml.Vector3f;

import static legend.game.Scus94491BpeSegment.tmdGp0CommandId_1f8003ee;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;

public final class TmdObjLoader {
  private TmdObjLoader() { }

  public static final int POS_SIZE = 4;
  public static final int NORM_SIZE = 3;
  public static final int UV_SIZE = 2;
  public static final int COLOUR_SIZE = 4;
  //TODO this isn't great, clut/tpage is only set per face, but we're uploading it per vertex
  public static final int TPAGE_SIZE = 1;
  public static final int CLUT_SIZE = 1;
  public static final int FLAGS_SIZE = 1;

  public static final int LIT_FLAG = 0x1;
  public static final int TEXTURED_FLAG = 0x2;
  public static final int COLOURED_FLAG = 0x4;
  public static final int TRANSLUCENT_FLAG = 0x8;

  public static Obj[] fromTmd(final String name, final Tmd tmd) {
    return fromTmd(name, tmd, 0);
  }

  public static Obj[] fromTmd(final String name, final Tmd tmd, final int specialFlags) {
    final Obj[] objs = new Obj[tmd.objTable.length];

    for(int objIndex = 0; objIndex < tmd.objTable.length; objIndex++) {
      objs[objIndex] = fromObjTable(name, tmd.objTable[objIndex], specialFlags);
    }

    return objs;
  }

  public static void fromModel(final String name, final Model124 model) {
    fromModel(name, model, 0, 0);
  }

  public static void fromModel(final String name, final Model124 model, final int textureWidth, final int textureHeight) {
    for(int i = 0; i < model.modelParts_00.length; i++) {
      final ModelPart10 part = model.modelParts_00[i];
      part.obj = TmdObjLoader.fromObjTable(name + " part " + i, part.tmd_08, 0, textureWidth, textureHeight);
    }
  }

  public static MeshObj fromObjTable(final String name, final TmdObjTable1c objTable) {
    return fromObjTable(name, objTable, 0);
  }

  public static MeshObj fromObjTable(final String name, final TmdObjTable1c objTable, final int specialFlags) {
    return fromObjTable(name, objTable, specialFlags, 0, 0);
  }

  public static MeshObj fromObjTable(final String name, final TmdObjTable1c objTable, final int specialFlags, final int textureWidth, final int textureHeight) {
    final int translucencyCount = Translucency.values().length + 1;
    final float[][] allVertices = new float[translucencyCount][];
    final int[][] allIndices = new int[translucencyCount][];
    getTranslucencySizes(objTable, specialFlags, allVertices, allIndices);
    final int[] vertexIndices = new int[translucencyCount];
    final int[] vertexOffsets = new int[translucencyCount];
    final int[] indexOffsets = new int[translucencyCount];

    int vertexSize = POS_SIZE;
    vertexSize += NORM_SIZE;
    vertexSize += UV_SIZE + TPAGE_SIZE + CLUT_SIZE;
    vertexSize += COLOUR_SIZE;
    vertexSize += FLAGS_SIZE;

    // Backface culling is on by default for opaque primitives. LOD sets some untextured primitives to translucent
    // even though the translucency settings can only come from textures in order to disable backface culling
    boolean backfaceCulling = true;

    for(int primitiveIndex = 0; primitiveIndex < objTable.primitives_10.length; primitiveIndex++) {
      final TmdObjTable1c.Primitive primitive = objTable.primitives_10[primitiveIndex];

      // Read type info from command ---
      final int command = (primitive.header() | specialFlags) & 0xff04_0000;
      final int primitiveId = command >>> 24;
      if((primitiveId >>> 5 & 0b11) != 1) {
        throw new RuntimeException("Unsupported primitive type");
      }

      //TODO more uses for ctmd and uniformLit (battle lighting)
      final boolean ctmd = (specialFlags & 0x20) != 0;
      final boolean uniformLit = (specialFlags & 0x10) != 0;
      final boolean shaded = (command & 0x4_0000) != 0;
      final boolean gourad = (primitiveId & 0b1_0000) != 0;
      final boolean quad = (primitiveId & 0b1000) != 0;
      final boolean textured = (primitiveId & 0b100) != 0;
      final boolean translucent = ((primitiveId | specialFlags) & 0b10) != 0;
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

      final boolean coloured = shaded || !lit || !textured;

      final Polygon poly = new Polygon(vertexCount);

      for(final byte[] data : primitive.data()) {
        float[] vertices = allVertices[0];
        int[] indices = allIndices[0];
        int translucencyIndex = 0;

        // Read data from TMD ---
        int primitivesOffset = 0;

        if(textured) {
          for(int tmdVertexIndex = 0; tmdVertexIndex < vertexCount; tmdVertexIndex++) {
            poly.vertices[tmdVertexIndex].u = IoHelper.readUByte(data, primitivesOffset++);
            poly.vertices[tmdVertexIndex].v = IoHelper.readUByte(data, primitivesOffset++);

            if(tmdVertexIndex == 0) {
              poly.clut = IoHelper.readUShort(data, primitivesOffset);
            } else if(tmdVertexIndex == 1) {
              poly.tpage = IoHelper.readUShort(data, primitivesOffset);

              if(translucent) {
                translucencyIndex = (poly.tpage >>> 5 & 0b11) + 1;
                vertices = allVertices[translucencyIndex];
                indices = allIndices[translucencyIndex];
              }
            }

            primitivesOffset += 2;
          }
        } else if(translucent) {
          translucencyIndex = Translucency.B_PLUS_F.ordinal() + 1;
          vertices = allVertices[translucencyIndex];
          indices = allIndices[translucencyIndex];
        }

        if(shaded || !lit) {
          for(int tmdVertexIndex = 0; tmdVertexIndex < vertexCount; tmdVertexIndex++) {
            poly.vertices[tmdVertexIndex].colour = IoHelper.readInt(data, primitivesOffset);
            primitivesOffset += 4;
          }
        } else if(!textured) {
          final int colour = IoHelper.readInt(data, primitivesOffset);
          primitivesOffset += 4;

          for(int tmdVertexIndex = 0; tmdVertexIndex < vertexCount; tmdVertexIndex++) {
            poly.vertices[tmdVertexIndex].colour = colour;
          }
        }

        for(int tmdVertexIndex = 0; tmdVertexIndex < vertexCount; tmdVertexIndex++) {
          if(lit) {
            if(gourad || tmdVertexIndex == 0) {
              poly.vertices[tmdVertexIndex].normalIndex = IoHelper.readUShort(data, primitivesOffset);
              primitivesOffset += 2;
            } else {
              poly.vertices[tmdVertexIndex].normalIndex = poly.vertices[0].normalIndex;
            }
          }

          poly.vertices[tmdVertexIndex].vertexIndex = IoHelper.readUShort(data, primitivesOffset);
          primitivesOffset += 2;
        }
        // ---

        indices[indexOffsets[translucencyIndex]++] = vertexIndices[translucencyIndex] + 2;
        indices[indexOffsets[translucencyIndex]++] = vertexIndices[translucencyIndex] + 1;
        indices[indexOffsets[translucencyIndex]++] = vertexIndices[translucencyIndex];

        if(quad) {
          indices[indexOffsets[translucencyIndex]++] = vertexIndices[translucencyIndex] + 1;
          indices[indexOffsets[translucencyIndex]++] = vertexIndices[translucencyIndex] + 2;
          indices[indexOffsets[translucencyIndex]++] = vertexIndices[translucencyIndex] + 3;
        }

        for(final Vertex vertex : poly.vertices) {
          final Vector3f pos = objTable.vert_top_00[vertex.vertexIndex];
          vertices[vertexOffsets[translucencyIndex]++] = pos.x;
          vertices[vertexOffsets[translucencyIndex]++] = pos.y;
          vertices[vertexOffsets[translucencyIndex]++] = pos.z;
          vertices[vertexOffsets[translucencyIndex]++] = vertex.vertexIndex;

          if(lit) {
            final Vector3f normal;

            if(vertex.normalIndex >= objTable.normal_top_08.length) {
              normal = new Vector3f();
            } else {
              normal = objTable.normal_top_08[vertex.normalIndex];
            }

            vertices[vertexOffsets[translucencyIndex]++] = normal.x;
            vertices[vertexOffsets[translucencyIndex]++] = normal.y;
            vertices[vertexOffsets[translucencyIndex]++] = normal.z;
          } else {
            vertexOffsets[translucencyIndex] += NORM_SIZE;
          }

          if(textured) {
            final Bpp bpp = Bpp.of(poly.tpage >>> 7 & 0b11);

            // 24bpp textures use normalized coordinates
            if(bpp == Bpp.BITS_24) {
              if((textureWidth | textureHeight) == 0) {
                throw new RuntimeException("24bpp textures must have texture width/height specified");
              }

              vertices[vertexOffsets[translucencyIndex]++] = (float)vertex.u / textureWidth;
              vertices[vertexOffsets[translucencyIndex]++] = (float)vertex.v / textureHeight;
            } else {
              vertices[vertexOffsets[translucencyIndex]++] = vertex.u;
              vertices[vertexOffsets[translucencyIndex]++] = vertex.v;
            }

            vertices[vertexOffsets[translucencyIndex]++] = poly.tpage;
            vertices[vertexOffsets[translucencyIndex]++] = poly.clut;
          } else {
            vertexOffsets[translucencyIndex] += UV_SIZE + TPAGE_SIZE + CLUT_SIZE;
          }

          if(coloured) {
            MathHelper.colourToFloat(vertex.colour, vertices, vertexOffsets[translucencyIndex]);
            vertexOffsets[translucencyIndex] += COLOUR_SIZE;
          } else {
            vertices[vertexOffsets[translucencyIndex]++] = 1.0f;
            vertices[vertexOffsets[translucencyIndex]++] = 1.0f;
            vertices[vertexOffsets[translucencyIndex]++] = 1.0f;
            vertices[vertexOffsets[translucencyIndex]++] = 1.0f;
          }

          int flags = 0;

          if(lit) {
            flags |= LIT_FLAG;
          }

          if(textured) {
            flags |= TEXTURED_FLAG;
          }

          if(coloured) {
            flags |= COLOURED_FLAG;
          }

          if(translucent) {
            flags |= TRANSLUCENT_FLAG;
            if(!textured || uniformLit) {
              backfaceCulling = false;
            }
          }

          vertices[vertexOffsets[translucencyIndex]++] = flags;
        }

        vertexIndices[translucencyIndex] += vertexCount;
      }
    }

    final Mesh[] meshes = new Mesh[translucencyCount];

    for(int i = 0; i < translucencyCount; i++) {
      if(vertexOffsets[i] != 0) {
        final Mesh mesh = new Mesh(GL_TRIANGLES, allVertices[i], allIndices[i]);

        mesh.attribute(0, 0L, POS_SIZE, vertexSize);

        int meshIndex = 1;
        int meshOffset = POS_SIZE;

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

        meshes[i] = mesh;
      }
    }

    return new MeshObj(name, meshes, backfaceCulling);
  }

  private static void getTranslucencySizes(final TmdObjTable1c objTable, final int specialFlags, final float[][] vertices, final int[][] indices) {
    // Extra element 0 means none
    final int translucencyCount = Translucency.values().length + 1;
    final int[] vertexSizes = new int[translucencyCount];
    final int[] indexSizes = new int[translucencyCount];

    for(int primitiveIndex = 0; primitiveIndex < objTable.primitives_10.length; primitiveIndex++) {
      final TmdObjTable1c.Primitive primitive = objTable.primitives_10[primitiveIndex];

      final int command = (primitive.header() | specialFlags) & 0xff04_0000;
      final int primitiveId = command >>> 24;

      final boolean quad = (primitiveId & 0b1000) != 0;
      final boolean textured = (primitiveId & 0b100) != 0;
      final boolean translucent = ((primitiveId | specialFlags) & 0b10) != 0;

      final int vertexCount = quad ? 4 : 3;

      int vertexSize = POS_SIZE;
      vertexSize += NORM_SIZE;
      vertexSize += UV_SIZE + TPAGE_SIZE + CLUT_SIZE;
      vertexSize += COLOUR_SIZE;
      vertexSize += FLAGS_SIZE;

      for(final byte[] data : primitive.data()) {
        if(translucent && textured) {
          final int tpage = IoHelper.readUShort(data, 6);
          final Translucency translucency = Translucency.of(tpage >>> 5 & 0b11);
          vertexSizes[translucency.ordinal() + 1] += vertexSize * vertexCount;
          indexSizes[translucency.ordinal() + 1] += quad ? 6 : 3;
        } else if(translucent) {
          // Dunno if this is gonna work everywhere... if set to translucent but untextured, default to B+F
          vertexSizes[Translucency.B_PLUS_F.ordinal() + 1] += vertexSize * vertexCount;
          indexSizes[Translucency.B_PLUS_F.ordinal() + 1] += quad ? 6 : 3;
        } else {
          vertexSizes[0] += vertexSize * vertexCount;
          indexSizes[0] += quad ? 6 : 3;
        }
      }
    }

    for(int i = 0; i < vertexSizes.length; i++) {
      if(vertexSizes[i] != 0) {
        vertices[i] = new float[vertexSizes[i]];
        indices[i] = new int[indexSizes[i]];
      }
    }
  }
}
