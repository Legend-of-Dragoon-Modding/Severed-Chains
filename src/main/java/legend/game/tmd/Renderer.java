package legend.game.tmd;

import legend.core.MathHelper;
import legend.core.gpu.Bpp;
import legend.core.gpu.GpuCommandPoly;
import legend.core.gte.DVECTOR;
import legend.core.gte.GsDOBJ2;
import legend.core.gte.SVECTOR;
import legend.core.gte.TmdObjTable1c;
import legend.game.types.Translucency;

import static legend.core.GameEngine.CPU;
import static legend.core.GameEngine.GPU;
import static legend.game.Scus94491BpeSegment.tmdGp0CommandId_1f8003ee;
import static legend.game.Scus94491BpeSegment.tmdGp0Tpage_1f8003ec;
import static legend.game.Scus94491BpeSegment.zMax_1f8003cc;
import static legend.game.Scus94491BpeSegment.zOffset_1f8003e8;
import static legend.game.Scus94491BpeSegment.zShift_1f8003c4;

public final class Renderer {
  private Renderer() { }

  /**
   * @param useSpecialTranslucency Used in battle, some TMDs have translucency info in the upper 16 bits of their ID. Also enables backside culling.
   */
  public static void renderDobj2(final GsDOBJ2 dobj2, final boolean useSpecialTranslucency) {
    final TmdObjTable1c objTable = dobj2.tmd_08;
    final SVECTOR[] vertices = objTable.vert_top_00;
    final SVECTOR[] normals = objTable.normal_top_08;
    final int[] primitives = objTable.primitives_10;
    final int count = objTable.n_primitive_14;
    int primitivesOffset = 0;

    //LAB_800da2bc
    for(int i = 0; i < count; ) {
      final int header = primitives[primitivesOffset];
      final int length = header & 0xffff;
      primitivesOffset = renderTmdPrimitive(primitives, primitivesOffset, vertices, normals, length, useSpecialTranslucency);
      i += length;
    }
  }

  public static int renderTmdPrimitive(final int[] primitives, int primitivesOffset, final SVECTOR[] vertices, final SVECTOR[] normals, final int count, final boolean useSpecialTranslucency) {
    // Read type info from command ---
    final int command = primitives[primitivesOffset] & 0xff04_0000;
    final int primitiveId = command >>> 24;

    if((primitiveId >>> 5 & 0b11) != 1) {
      throw new RuntimeException("Unsupported primitive type");
    }

    final boolean gradated = (command & 0x4_0000) != 0;
    final boolean quad = (primitiveId & 0b1000) != 0;
    final boolean textured = (primitiveId & 0b100) != 0;
    final boolean translucent = (primitiveId & 0b10) != 0;
    final boolean lit = (primitiveId & 0b1) == 0;

    if(textured && gradated) {
      throw new RuntimeException("Invalid primitive type");
    }

    if(!textured && !lit) {
      throw new RuntimeException("Invalid primitive type");
    }

    //TODO need to figure out what this was being used for
    final long specialTrans = tmdGp0CommandId_1f8003ee.get();

    final int vertexCount = quad ? 4 : 3;
    // ---

    final Polygon poly = new Polygon(vertexCount);

    outer:
    for(int i = 0; i < count; i++) {
      // Read data from TMD ---
      primitivesOffset++;

      if(textured) {
        for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
          poly.vertices[vertexIndex].u = primitives[primitivesOffset] & 0xff;
          poly.vertices[vertexIndex].v = primitives[primitivesOffset] >>> 8 & 0xff;

          if(vertexIndex == 0) {
            poly.clut = primitives[primitivesOffset] >>> 16;
          } else if(vertexIndex == 1) {
            poly.tpage = primitives[primitivesOffset] >>> 16;
          }

          primitivesOffset++;
        }
      }

      if(gradated || !lit) {
        for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
          poly.vertices[vertexIndex].colour = primitives[primitivesOffset];
          primitivesOffset++;
        }
      } else if(!textured) {
        final int colour = primitives[primitivesOffset];
        primitivesOffset++;

        for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
          poly.vertices[vertexIndex].colour = colour;
        }
      }

      for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
        if(lit) {
          poly.vertices[vertexIndex].normalIndex = primitives[primitivesOffset] & 0xffff;
          poly.vertices[vertexIndex].vertexIndex = primitives[primitivesOffset] >>> 16;
        } else {
          poly.vertices[vertexIndex].vertexIndex = primitives[primitivesOffset] & 0xffff;
        }

        primitivesOffset++;
      }

      primitivesOffset = MathHelper.roundUp(primitivesOffset, 4); // 4-byte-align
      // ---

      final GpuCommandPoly cmd = new GpuCommandPoly(vertexCount);

      if(textured) {
        cmd.clut((poly.clut & 0b111111) * 16, poly.clut >>> 6);
        cmd.vramPos((poly.tpage & 0b1111) * 64, (poly.tpage & 0b10000) != 0 ? 256 : 0);
        cmd.bpp(Bpp.of(poly.tpage >>> 7 & 0b11));

        if(translucent) {
          cmd.translucent(Translucency.of(poly.tpage >>> 5 & 0b11));
        }
      }

      for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
        final SVECTOR vert = vertices[poly.vertices[vertexIndex].vertexIndex];
        CPU.MTC2(vert.getXY(), 0);
        CPU.MTC2(vert.getZ(),  1);
        CPU.COP2(0x18_0001L); // Perspective transform single

        if((int)CPU.CFC2(31) < 0) { // Errors
          continue outer;
        }

        final DVECTOR xy = new DVECTOR().setXY(CPU.MFC2(14));

        cmd.pos(vertexIndex, xy.getX(), xy.getY());

        if(textured) {
          cmd.uv(vertexIndex, poly.vertices[vertexIndex].u, poly.vertices[vertexIndex].v);
        }

        // Back-face culling
        if(useSpecialTranslucency) {
          if(vertexIndex == 2) {
            CPU.COP2(0x140_0006L); // Normal clipping
            final long winding = CPU.MFC2(24);

            if(!translucent && winding <= 0 || translucent && winding == 0) {
              continue outer;
            }
          }
        }
      }

      // Average Z
      if(quad) {
        CPU.COP2(0x168_002eL);
      } else {
        CPU.COP2(0x168_002dL);
      }

      final int z = (int)Math.min(CPU.MFC2(7) + zOffset_1f8003e8.get() >> zShift_1f8003c4.get(), zMax_1f8003cc.get());

      if(textured && !lit) {
        for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
          cmd.rgb(vertexIndex, poly.vertices[vertexIndex].colour);
        }
      } else {
        for(int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
          CPU.MTC2(poly.vertices[vertexIndex].colour, 6);
          final SVECTOR norm = normals[poly.vertices[vertexIndex].normalIndex];
          CPU.MTC2(norm.getXY(), 0);
          CPU.MTC2(norm.getZ(),  1);
          CPU.COP2(0x108_041bL); // Normal colour colour single vector
          cmd.rgb(vertexIndex, (int)CPU.MFC2(22));
        }
      }

      if(translucent && !textured) {
        cmd.translucent(Translucency.of(tmdGp0Tpage_1f8003ec.get() >>> 5 & 0b11));
      }

      GPU.queueCommand(z, cmd);
    }

    return primitivesOffset;
  }
}
