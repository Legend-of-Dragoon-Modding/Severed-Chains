package legend.core.opengl;

import legend.core.IoHelper;
import legend.core.MathHelper;
import legend.core.gte.Tmd;
import legend.core.gte.TmdObjTable1c;
import legend.game.tmd.Polygon;
import legend.game.tmd.Vertex;
import org.joml.Vector3f;

import static legend.game.Scus94491BpeSegment.tmdGp0CommandId_1f8003ee;
import static org.lwjgl.opengl.GL11C.GL_TRIANGLES;

public final class ObjLoader {
  private ObjLoader() { }

  private static final int POS_SIZE = 3;
  private static final int NORM_SIZE = 3;
  private static final int UV_SIZE = 2;
  private static final int COLOUR_SIZE = 4;
  //TODO this isn't great, clut/tpage is only set per face, but we're uploading it per vertex
  private static final int TPAGE_SIZE = 2;
  private static final int CLUT_SIZE = 2;
  private static final int BPP_SIZE = 1;
  private static final int TRANSLUCENCY_SIZE = 1;

  public static Obj[] fromTmd(final Tmd tmd, final int specialFlags) {
    final Shader shader = ShaderManager.getShader("tmd");
    final Shader.UniformInt shaderLit = shader.new UniformInt("lit");
    final Shader.UniformInt shaderTextured = shader.new UniformInt("textured");
    final Shader.UniformInt shaderColoured = shader.new UniformInt("coloured");

    final Obj[] objs = new Obj[tmd.objTable.length];

    for(int objIndex = 0; objIndex < tmd.objTable.length; objIndex++) {
      final TmdObjTable1c objTable = tmd.objTable[objIndex];
      final Mesh[] meshes = new Mesh[objTable.primitives_10.length];
      final Runnable[] shaderSetup = new Runnable[objTable.primitives_10.length];

      for(int primitiveIndex = 0; primitiveIndex < objTable.primitives_10.length; primitiveIndex++) {
        final TmdObjTable1c.Primitive primitive = objTable.primitives_10[primitiveIndex];

        // Read type info from command ---
        final int command = (primitive.header() | specialFlags) & 0xff04_0000;
        final int primitiveId = command >>> 24;
        if((primitiveId >>> 5 & 0b11) != 1) {
          throw new RuntimeException("Unsupported primitive type");
        }

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
        final long specialTrans = tmdGp0CommandId_1f8003ee.get();

        final int vertexCount = quad ? 4 : 3;
        // ---

        final boolean coloured = shaded || !lit || !textured;

        int vertexSize = POS_SIZE;

//        if(lit) { //TODO enable if we move to modular shaders
          vertexSize += NORM_SIZE;
//        }

//        if(textured) { //TODO enable if we move to modular shaders
          vertexSize += UV_SIZE + TPAGE_SIZE + CLUT_SIZE + BPP_SIZE + TRANSLUCENCY_SIZE;
//        }

//        if(coloured) { //TODO enable if we move to modular shaders
          vertexSize += COLOUR_SIZE;
//        }

        final float[] vertices = new float[vertexSize * vertexCount * primitive.data().length];
        final int[] indices = new int[(quad ? 6 : 3) * primitive.data().length];
        int vertexIndex = 0;
        int vertexOffset = 0;
        int indexOffset = 0;

        final Polygon poly = new Polygon(vertexCount);

        for(final byte[] data : primitive.data()) {
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
              }

              primitivesOffset += 2;
            }
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

          indices[indexOffset++] = vertexIndex + 2;
          indices[indexOffset++] = vertexIndex + 1;
          indices[indexOffset++] = vertexIndex;

          if(quad) {
            indices[indexOffset++] = vertexIndex + 1;
            indices[indexOffset++] = vertexIndex + 2;
            indices[indexOffset++] = vertexIndex + 3;
          }

          for(final Vertex vertex : poly.vertices) {
            final Vector3f pos = objTable.vert_top_00[vertex.vertexIndex];
            vertices[vertexOffset++] = -pos.x;
            vertices[vertexOffset++] = -pos.y;
            vertices[vertexOffset++] = pos.z;

            if(lit) {
              final Vector3f normal = objTable.normal_top_08[vertex.normalIndex];
              vertices[vertexOffset++] = normal.x;
              vertices[vertexOffset++] = normal.y;
              vertices[vertexOffset++] = normal.z;
            } else {
              vertexOffset += NORM_SIZE; //TODO remove if we switch to modular shaders
            }

            if(textured) {
              final int bpp = poly.tpage >>> 7 & 0b11;

              if(bpp >= 2) {
                throw new RuntimeException("Only 4/8 BPP supported");
              }

              final int translucency = poly.tpage >>> 5 & 0b11;

              if(translucency != 0 && translucency != 1) {
                throw new RuntimeException("Only 0/1 translucency supported");
              }

              vertices[vertexOffset++] = vertex.u;
              vertices[vertexOffset++] = vertex.v;
              vertices[vertexOffset++] = (poly.tpage & 0b1111) * 64;
              vertices[vertexOffset++] = (poly.tpage & 0b10000) != 0 ? 256 : 0;
              vertices[vertexOffset++] = (poly.clut & 0b111111) * 16;
              vertices[vertexOffset++] = poly.clut >>> 6;
              vertices[vertexOffset++] = bpp;
              vertices[vertexOffset++] = translucency;
            } else {
              vertexOffset += UV_SIZE + TPAGE_SIZE + CLUT_SIZE + BPP_SIZE + TRANSLUCENCY_SIZE; //TODO remove if we switch to modular shaders
            }

            if(coloured) {
              MathHelper.colourToFloat(vertex.colour, vertices, vertexOffset);
              vertexOffset += COLOUR_SIZE;
            } else {
              vertexOffset += COLOUR_SIZE; //TODO remove if we switch to modular shaders
            }
          }

          vertexIndex += vertexCount;
        }

        final Mesh mesh = new Mesh(GL_TRIANGLES, vertices, indices);

        mesh.attribute(0, 0L, 3, vertexSize);

        int meshIndex = 1;
        int meshOffset = 3;

//        if(lit) { //TODO enable if we move to modular shaders
          mesh.attribute(meshIndex, meshOffset, NORM_SIZE, vertexSize);
          meshIndex++;
          meshOffset += NORM_SIZE;
//        }

//        if(textured) { //TODO enable if we move to modular shaders
          mesh.attribute(meshIndex, meshOffset, UV_SIZE, vertexSize);
          meshIndex++;
          meshOffset += UV_SIZE;

          mesh.attribute(meshIndex, meshOffset, TPAGE_SIZE, vertexSize);
          meshIndex++;
          meshOffset += TPAGE_SIZE;

          mesh.attribute(meshIndex, meshOffset, CLUT_SIZE, vertexSize);
          meshIndex++;
          meshOffset += CLUT_SIZE;

        mesh.attribute(meshIndex, meshOffset, BPP_SIZE, vertexSize);
        meshIndex++;
        meshOffset += BPP_SIZE;

        mesh.attribute(meshIndex, meshOffset, TRANSLUCENCY_SIZE, vertexSize);
        meshIndex++;
        meshOffset += TRANSLUCENCY_SIZE;
//        }

//        if(coloured) { //TODO enable if we move to modular shaders
          mesh.attribute(meshIndex, meshOffset, COLOUR_SIZE, vertexSize);
//        }

        meshes[primitiveIndex] = mesh;

        final int litInt = lit ? 1 : 0;
        final int texturedInt = textured ? 1 : 0;
        final int colouredInt = coloured ? 1 : 0;

        shaderSetup[primitiveIndex] = () -> {
          shaderLit.set(litInt);
          shaderTextured.set(texturedInt);
          shaderColoured.set(colouredInt);
        };
      }

      objs[objIndex] = new Obj(shader, meshes, shaderSetup); //TODO update if we move to modular shaders
    }

    return objs;
  }
}
